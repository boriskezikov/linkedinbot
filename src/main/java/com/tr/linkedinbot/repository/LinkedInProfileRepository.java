package com.tr.linkedinbot.repository;

import com.tr.linkedinbot.model.LinkedInProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Repository
public interface LinkedInProfileRepository extends JpaRepository<LinkedInProfile, Long> {


    boolean existsByChatIdOrTgUser(Long chatId, String tgUser);

    @Transactional
    @Query(value = "select * from linked_in_profile where to_remove = 'false' order by random() limit :#{#limit}", nativeQuery = true)
    List<LinkedInProfile> selectRandom(@Param(value = "limit") @Valid Integer limit);
}
