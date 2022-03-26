package com.tr.linkedinbot.repository;

import com.tr.linkedinbot.model.LinkedInProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedInProfileRepository extends JpaRepository<LinkedInProfile, Long> {


    boolean existsByChatIdOrTgUser(Long chatId, String tgUser);
}
