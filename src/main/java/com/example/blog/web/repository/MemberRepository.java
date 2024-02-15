package com.example.blog.web.repository;

import com.example.blog.web.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUserId(String userId);
    Optional<Member> findByEmail(String email);
    @Query(value = "select * from member where social_type = :socialType and social_id = :socialId", nativeQuery = true)
    Member findBySocial(@Param(value = "socialType") String socialType, @Param(value = "socialId") String socialId) throws Exception;
    @Modifying
    @Query(value = "update member set user_name = :userName, upd_date = :updDate where social_type = :socialType and social_id = :socialId", nativeQuery = true)
    void updateNameBySocial(@Param(value = "userName") String userName, @Param(value = "socialType") String socialType, @Param(value = "socialId") String socialId, @Param(value = "updDate") LocalDateTime date) throws Exception;
    @Modifying
    @Query(value = "update member set email = :email, upd_date = :updDate where social_type = :socialType and social_id = :socialId", nativeQuery = true)
    void updateEmailBySocial(@Param(value = "email") String email, @Param(value = "socialType") String socialType, @Param(value = "socialId") String socialId, @Param(value = "updDate") LocalDateTime date) throws Exception;

}
