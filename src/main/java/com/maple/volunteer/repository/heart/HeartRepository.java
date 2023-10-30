package com.maple.volunteer.repository.heart;

import com.maple.volunteer.domain.heart.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface HeartRepository extends JpaRepository<Heart, Long> {

    //좋아요 여부 확인
    @Query("SELECT h FROM Heart h "
            + " LEFT JOIN h.user u "
            + " LEFT JOIN h.poster p "
            + " WHERE u.id = :userId AND p.id = :posterId")
    Heart findUserAndPoster(Long userId, Long posterId);


    //좋아요 상태 변경
    @Query("UPDATE Heart h "
            + " SET h.status = :status "
            + " WHERE h.id = :id")
    @Modifying(clearAutomatically = true)
    void updateStatus(@Param("id") Long heartId, @Param("status") boolean b);

//TODO 마이페이지에서 좋아요한 게시글 개수 로직짤 때 사용하기
    //userId에 해당하는 유저가 좋아요한 게시글 개수
    @Query("SELECT COUNT(h) FROM Heart h "
            + " LEFT JOIN h.user u"
            + " WHERE u.id = :userId")
    long countByUserId(@Param("userId") Long userId);


}
