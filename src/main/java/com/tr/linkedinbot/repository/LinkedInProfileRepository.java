package com.tr.linkedinbot.repository;

import com.tr.linkedinbot.model.LinkedInProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Transactional
    @Query(value = "with t as (select unnest(i.showed_ids) as ids from linked_in_profile i where i.chat_id = :chatId) " +
            "select p.* " +
            "from linked_in_profile p, t " +
            "where p.chat_id not in (t.ids) " +
            "  and to_remove = 'false' " +
            "order by random() " +
            "limit  :#{#limit}", nativeQuery = true)
    List<LinkedInProfile> selectRandomForRequester(@Param(value = "chatId") Long chatId, @Param(value = "limit") @Valid Integer limit);

    @Modifying
    @Transactional
    @Query(value = "update linked_in_profile set showed_ids = ARRAY_APPEND(showed_ids, :shownId) where chat_id = :hostId", nativeQuery = true)
    void writeShownChatId(@Param(value = "hostId") Long chatId,
                          @Param(value = "shownId") Long shownId);




    @Query(value = "select cardinality(i.showed_ids) as ids from linked_in_profile i where i.chat_id = :chatId", nativeQuery = true)
    Long selectRequesterLoadSize(@Param(value = "chatId") Long chatId);

}
