package com.tr.linkedinbot.logic;

import static com.tr.linkedinbot.commands.TextConstants.INVALID_LINKEDIN_LINK_ERROR_MESSAGE;
import static com.tr.linkedinbot.commands.TextConstants.PROFILE_ALREADY_SAVED_ERROR_MESSAGE;

import com.tr.linkedinbot.exception.IllegalLinkedInProfileException;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.Country;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.model.Role;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import static java.util.regex.Pattern.compile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkedInAccountService {

    /*
    "^" matches the start of the string, ensuring that the regular expression only matches URLs that start with the specified pattern.
    "(https?://)?" is a group that matches "http://" or "https://" and is optional, meaning it can match URLs that start with "http://" or "https://" or not.
    "(www\.)?" is a group that matches "www." and is also optional, meaning it can match URLs that contain "www." or not.
    "linkedin\.com" matches the domain name "linkedin.com"
    "/in" matches the string "/in"
    ".*" matches any character (except for a newline) zero or more times.
    "$" matches the end of the string, ensuring that the regular expression only matches URLs that end with the specified pattern.
     */
    public static final String REGEX = "^(https?://)?(www\\.)?linkedin\\.com/in.*$";
    private static final Predicate<String> LINKED_IN_VALID_URL_PATTERN_PREDICATE = compile(REGEX).asMatchPredicate();
    private final LinkedInProfileRepository repository;

    public long countUsers() {
        return repository.count();
    }

    public void createNewProfile(Message message, String username) {
        Optional<LinkedInProfile> byId = repository.findById(message.getChatId());
        if (byId.isPresent()) {
            throw new IllegalLinkedInProfileException(PROFILE_ALREADY_SAVED_ERROR_MESSAGE.getText());
        }
        var validUrl = checkValid(message.getText());

        LinkedInProfile newProfile = LinkedInProfile.builder()
                .chatId(message.getChatId())
                .linkedInUrl(validUrl)
                .tgUser(username)
                .registeredAt(LocalDateTime.now())
                .state(BotState.NOT_IN_INTERACTION)
                .build();

        repository.save(newProfile);

        log.info(">>> new member added {}", validUrl);
    }

    public static String checkValid(String linkedInUrl) {
        return Optional.of(linkedInUrl)
                .filter(LINKED_IN_VALID_URL_PATTERN_PREDICATE)
                .map(linked -> linked.replace("/mwlite", ""))
                .orElseThrow(() -> new IllegalLinkedInProfileException(INVALID_LINKEDIN_LINK_ERROR_MESSAGE.getText()));
    }

    public List<LinkedInProfile> loadAll() {
        return repository.findAll();
    }

    public List<LinkedInProfile> loadRandomRecords(Long chatId, String tgName) {
        var linkedInProfile = repository.getByChatId(chatId).orElseThrow();

        List<LinkedInProfile> all = getLinkedInProfiles(linkedInProfile);
        all = removeRequester(all, tgName, chatId);

        updatePageNumber(linkedInProfile, all);
        return all;
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

    private List<LinkedInProfile> getLinkedInProfiles(LinkedInProfile linkedInProfile) {
        var pageNumber = linkedInProfile.getPageNumber();
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("registeredAt"));

        return Optional.of(repository.findAll(pageable))
                .map(Slice::getContent)
                .orElse(Collections.emptyList());
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
                var set  = new HashSet<String>();
                set.add(profile.getLinkedInUrl());
                return set;
            } else {
                s.add(profile.getLinkedInUrl());
                return s;
            }
        };
    }


    private Predicate<LinkedInProfile> filterByCountryAndRole() {
        return linkedInProfile -> linkedInProfile.getRole() != null && linkedInProfile.getCountry() != null;
    }

    private static Predicate<LinkedInProfile> getLinkedInProfilePredicate() {
        return linkedInProfile -> linkedInProfile.getRole() == null || linkedInProfile.getCountry() == null || linkedInProfile.getSearchRoles() == null || linkedInProfile.getSearchRoles().isEmpty();
    }

    private List<LinkedInProfile> removeRequester(List<LinkedInProfile> all, String tgName, Long chatId) {
        return all.stream()
                .filter(filterRequester(tgName, chatId).negate())
                .collect(Collectors.toList());
    }

    private static Predicate<LinkedInProfile> filterRequester(String tgName, Long chatId) {
        return linkedInProfile -> linkedInProfile.getTgUser().equals(tgName) ||
                linkedInProfile.getChatId().equals(chatId);
    }

    public boolean validateUpload(Long chatId, String tgName) {
        return repository.existsByChatIdOrTgUser(chatId, tgName);
    }

}
