package com.tr.linkedinbot.logic;

import static com.tr.linkedinbot.commands.TextConstants.INVALID_LINKEDIN_LINK_ERROR_MESSAGE;
import static com.tr.linkedinbot.commands.TextConstants.PROFILE_ALREADY_SAVED_ERROR_MESSAGE;
import com.tr.linkedinbot.exception.IllegalLinkedInProfileException;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.Country;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.model.Role;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LinkedInAccountService {

    // Регекс для проверки, что URL относится к linkedin.com/in...
    public static final String REGEX = "^(https?://)?(www\\.)?linkedin\\.com/in.*$";
    private static final Pattern LINKED_IN_PATTERN = Pattern.compile(REGEX);
    // Ограничим 1 бесплатный вызов (можешь поменять на 2, 3, или убрать вовсе)
    private static final int MAX_FREE_USAGE = 1;
    private final LinkedInProfileRepository repository;

    /**
     * Проверка валидности URL LinkedIn
     */
    public static String checkValid(String linkedInUrl) {
        if (LINKED_IN_PATTERN.matcher(linkedInUrl).matches()) {
            return linkedInUrl.replace("/mwlite", "");
        } else {
            throw new IllegalLinkedInProfileException(INVALID_LINKEDIN_LINK_ERROR_MESSAGE.getText());
        }
    }

    private static BiFunction<LinkedInProfile, Set<String>, Set<String>> mapLinksForProfile(HashMap<Role, Set<String>> rolesWithLinksMap, Role role) {
        return (linkedInProfile, strings) -> {
            if (strings == null) {
                return new HashSet<>(rolesWithLinksMap.get(role));
            } else {
                strings.addAll(rolesWithLinksMap.get(role));
                return strings;
            }
        };
    }

    private static BiFunction<Role, Set<String>, Set<String>> mapLinksForRole(LinkedInProfile profile) {
        return (r, s) -> {
            if (s == null) {
                var set = new HashSet<String>();
                set.add(profile.getLinkedInUrl());
                return set;
            } else {
                s.add(profile.getLinkedInUrl());
                return s;
            }
        };
    }

    private static Predicate<LinkedInProfile> getLinkedInProfilePredicate() {
        return linkedInProfile -> linkedInProfile.getRole() == null || linkedInProfile.getCountry() == null || linkedInProfile.getSearchRoles() == null || linkedInProfile.getSearchRoles().isEmpty();
    }

    // -------------- Создание нового профиля при первом вводе ссылки
    public void createNewProfile(Message message, String username) {
        Optional<LinkedInProfile> byId = repository.findById(message.getChatId());
        if (byId.isPresent()) {
            throw new IllegalLinkedInProfileException(PROFILE_ALREADY_SAVED_ERROR_MESSAGE.getText());
        }
        var validUrl = checkValid(message.getText());
        var chatId = message.getChatId();

        LinkedInProfile newProfile = LinkedInProfile.builder()
                .chatId(chatId)
                .linkedInUrl(validUrl)
                .lastProfileGet(LocalDateTime.now().minusYears(1))
                .tgUser(username)
                .registeredAt(LocalDateTime.now())
                .pageNumber(0)
                .state(BotState.NOT_IN_INTERACTION)
                // Новые поля:
                .paid(false)
                .freeUsage(0)
                .build();

        repository.save(newProfile);
        // Запишем, что chatId показывали
        repository.writeShownChatId(chatId, chatId);

        log.info(">>> new member added {}", validUrl);
    }

    // -------------- Старая логика (пример): загружаем всех
    public List<LinkedInProfile> loadAll() {
        return repository.findAll();
    }

    /**
     * Метод, который проверяет, можно ли выдавать профили по лимиту "размер/количество"
     * Если user уже оплатил, то всё ок.
     * Если не оплатил, но freeUsage < MAX_FREE_USAGE, тоже ок (пока что).
     * Иначе проверяем твоё условие "usage <= 20".
     */
    public boolean checkRequesterLoadSize(Long chatId, String tgName) {
        log.info("Check requester load size {} {}", chatId, tgName);

        LinkedInProfile p = repository.getById(chatId);

        if (Boolean.TRUE.equals(p.getPaid())) {
            // оплаченный пользователь, пропускаем
            return true;
        }
        // если ещё не израсходовал бесплатную попытку
        if (p.getFreeUsage() < MAX_FREE_USAGE) {
            return true;
        }

        // Иначе считаем, что пользователь превысил лимит
        Long usage = repository.selectRequesterLoadSize(chatId);
        return usage <= 20; // твоя старая логика
    }

    /**
     * Метод, который проверяет, можно ли выдавать профили по "времени".
     * Если user уже оплатил – пропускаем.
     * Если не оплатил, но freeUsage < MAX_FREE_USAGE, тоже пропускаем 1 раз.
     * Иначе смотрим, прошло ли 2 минуты от lastProfileGet.
     */
    public boolean checkRequesterBillingTime(Long chatId, String tgName) {
        log.info("Check requester billing time {} {}", chatId, tgName);

        LinkedInProfile p = repository.getById(chatId);

        if (Boolean.TRUE.equals(p.getPaid())) {
            return true;
        }

        if (p.getFreeUsage() < MAX_FREE_USAGE) {
            return true;
        }

        return LocalDateTime.now().isAfter(
                p.getLastProfileGet().plusMinutes(2)
        );
    }

    /**
     * Загружаем случайные записи (для выдачи пользователю).
     * Здесь же, если реально отдали профили и user не оплачен, увеличиваем freeUsage.
     */
    public List<LinkedInProfile> loadRandomRecords(Long chatId, String tgName) {
        log.info("Loading profiles for {} {}", chatId, tgName);

        var requestor = repository.getByChatId(chatId)
                .orElseThrow(() -> new RuntimeException("Profile not found for chatId " + chatId));

        // выбираем некую логику рандомного подбора
        int limit = 10; // например, 10 штук
        var all = repository.selectRandomForRequester(chatId, limit);

        // записываем, что эти чаты "были показаны"
        all.forEach(l -> repository.writeShownChatId(chatId, l.getChatId()));

        if (!all.isEmpty()) {
            updateLastGetDate(chatId);
        }

        // Если пользователь ещё не оплачен и ещё не израсходовал свою бесплатную попытку,
        // то поднимем счётчик freeUsage
        if (!Boolean.TRUE.equals(requestor.getPaid()) && requestor.getFreeUsage() < MAX_FREE_USAGE && !all.isEmpty()) {
            requestor.setFreeUsage(requestor.getFreeUsage() + 1);
            repository.save(requestor);
        }

        return all;
    }

    /**
     * Обновлённая версия (если нужно указать limit явно)
     */
    public List<LinkedInProfile> loadRandomRecords(Long chatId, String tgName, int limit) {
        log.info("Loading profiles for {} {}, limit {}", chatId, tgName, limit);

        var requestor = repository.getByChatId(chatId)
                .orElseThrow(() -> new RuntimeException("Profile not found for chatId " + chatId));

        var all = repository.selectRandomForRequester(chatId, limit);
        all.forEach(l -> repository.writeShownChatId(chatId, l.getChatId()));

        if (!all.isEmpty()) {
            updateLastGetDate(chatId);
        }

        if (!Boolean.TRUE.equals(requestor.getPaid()) && requestor.getFreeUsage() < MAX_FREE_USAGE && !all.isEmpty()) {
            requestor.setFreeUsage(requestor.getFreeUsage() + 1);
            repository.save(requestor);
        }

        return all;
    }

    // обновляем время последнего запроса
    private void updateLastGetDate(Long chatId) {
        LinkedInProfile p = repository.getById(chatId);
        p.setLastProfileGet(LocalDateTime.now());
        repository.save(p);
    }

    /**
     * Ставим user.paid = true после оплаты
     */
    public void setPaid(Long chatId) {
        LinkedInProfile profile = repository.getById(chatId);
        profile.setPaid(true);
        // Если хочешь, можешь сбросить freeUsage в 0:
        // profile.setFreeUsage(0);
        repository.save(profile);
    }

    // ---------------------------------------
    // Логика validateUpload осталась как есть
    // ---------------------------------------
    public boolean validateUpload(Long chatId, String tgName) {
        return repository.existsByChatIdOrTgUser(chatId, tgName);
    }

    public long countUsers() {
        return repository.count();
    }

    private List<LinkedInProfile> getLinkedInProfiles(LinkedInProfile linkedInProfile) {
        var pageNumber = linkedInProfile.getPageNumber();
        Pageable pageable = PageRequest.of(pageNumber, 5, Sort.by("registeredAt"));

        return linkedInProfile.getCountry().equals(Country.ISRAEL) ?
                repository.findAllByRoleInAndCountry(linkedInProfile.getSearchRoles(), linkedInProfile.getCountry(), pageable) :
                repository.findAllByRoleIn(linkedInProfile.getSearchRoles(), pageable);
    }

    public List<LinkedInProfile> loadIncompleteProfilesWithDaysOffset(int days) {
        LocalDateTime startTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(days);
        LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

        return repository.findAllByRegisterDate(startTime, endTime)
                .stream()
                .filter(getLinkedInProfilePredicate())
                .collect(Collectors.toList());
    }

    public List<LinkedInProfile> loadIncompleteProfilesInBounds(int upperBound, int lowerBound) {
        LocalDateTime startTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(upperBound);
        LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(lowerBound);

        return repository.findAllByRegisterDate(startTime, endTime)
                .stream()
                .filter(getLinkedInProfilePredicate())
                .collect(Collectors.toList());
    }

    public List<LinkedInProfile> loadNewProfilesWithDaysInBounds(int upperBound, int lowerBound) {
        LocalDateTime startTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(upperBound);
        LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(lowerBound);

        return repository.findAllByRegisterDate(startTime, endTime)
                .stream()
                .filter(filterByCountryAndRole())
                .collect(Collectors.toList());
    }

    public Map<LinkedInProfile, Set<String>> getMapOfProfilesAndLinks(List<LinkedInProfile> profiles) {
        return Optional.of(profiles)
                .map(this::createMapOfRolesWithLinks)
                .map(this::createMapOfProfilesWithLinks)
                .map(this::removeLinksToSelf)
                .orElse(Collections.emptyMap());
    }

    private HashMap<Role, Set<String>> createMapOfRolesWithLinks(List<LinkedInProfile> profiles) {
        var rolesWithLinksMap = new HashMap<Role, Set<String>>();
        for (var profile : profiles) {
            rolesWithLinksMap.compute(profile.getRole(), mapLinksForRole(profile));
        }
        return rolesWithLinksMap;
    }

    private Map<LinkedInProfile, Set<String>> removeLinksToSelf(Map<LinkedInProfile, Set<String>> profilesWithLinksMap) {
        for (var e : profilesWithLinksMap.entrySet()) {
            e.getValue().remove(e.getKey().getLinkedInUrl());
        }

        return profilesWithLinksMap;
    }

    private Map<LinkedInProfile, Set<String>> createMapOfProfilesWithLinks(HashMap<Role, Set<String>> rolesWithLinksMap) {
        var profilesWithLinksMap = new HashMap<LinkedInProfile, Set<String>>();
        for (var role : rolesWithLinksMap.keySet()) {
            for (var lin : repository.findAllBySearchRole(role.name())) {
                profilesWithLinksMap.compute(lin, mapLinksForProfile(rolesWithLinksMap, role));
            }
        }

        return profilesWithLinksMap;
    }

    private Predicate<LinkedInProfile> filterByCountryAndRole() {
        return linkedInProfile -> linkedInProfile.getRole() != null && linkedInProfile.getCountry() != null;
    }

    private void updatePageNumber(LinkedInProfile linkedInProfile, List<LinkedInProfile> all) {
        if (all.isEmpty()) {
            linkedInProfile.setPageNumber(0);
        } else {
            var pageNumber = linkedInProfile.getPageNumber();
            linkedInProfile.setPageNumber(pageNumber + 1);
        }
        repository.save(linkedInProfile);
    }

}
