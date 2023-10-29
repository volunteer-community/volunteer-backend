package com.maple.volunteer.repository.heart;

import com.maple.volunteer.domain.heart.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface HeartRepository extends JpaRepository<Heart, Long> {

    @Query("SELECT h FROM Heart h "
            + " LEFT JOIN h.user u "
            + " LEFT JOIN h.poster p "
            + " WHERE u.id = :userId AND p.id = :posterId")
    Heart findUserAndPoster(Long userId, Long posterId);


    @Query("UPDATE Heart h "
            + " SET h.status = :status "
            + " WHERE h.id = :id")
    @Modifying(clearAutomatically = true)
    void updateStatus(@Param("id") Long heartId, @Param("status") boolean b);

}
