package com.tr.linkedinbot.repository;

import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.Country;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.model.Role;
import jdk.jshell.Snippet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import com.tr.linkedinbot.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LinkedInProfileRepository extends JpaRepository<LinkedInProfile, Long> {


    boolean existsByChatIdOrTgUser(Long chatId, String tgUser);

    @Transactional
    @Query(value = "select * from linked_in_profile where to_remove = 'false' order by random() limit :#{#limit}", nativeQuery = true)
    List<LinkedInProfile> selectRandom(@Param(value = "limit") @Valid Integer limit);

    Optional<LinkedInProfile> getByChatId(Long chatId);

    List<LinkedInProfile> findAllByRoleInAndCountry(Set<Role> roles, Country country, Pageable pageable);

    List<LinkedInProfile> findAllByRoleIn(Set<Role> roles, Pageable pageable);

    @Query(value = "select * from linked_in_profile where :role = any(search_roles)", nativeQuery = true)
    List<LinkedInProfile> findAllBySearchRole(String role);


    @Query(value = "select * from linked_in_profile where registered_at >= :start_date and registered_at < :end_date", nativeQuery = true)
    List<LinkedInProfile> findAllByRegisterDate(@Param(value = "start_date") LocalDateTime startDate, @Param(value = "end_date") LocalDateTime endDate);
}
