package com.maple.volunteer.repository.user;

import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.type.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String userEmail);

    @Query("SELECT u FROM User u WHERE u.isDelete = false AND u.email = :email")
    Optional<User> findActiveUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.isDelete = false AND u.email = :email and u.provider = :provider")
    List<User> findActiveUserByEmail2(@Param("email") String email, @Param("provider")String provider);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isDelete = false  AND u.nickname = :nickname")
    Optional<User> findNickname(@Param("nickname") String nickname);

    @Query("SELECT u FROM User u WHERE u.isDelete = false  AND u.phoneNumber = :phone")
    Optional<User> findPhone(@Param("phone") String phone);

    @Modifying
    @Query("UPDATE User u SET u.phoneNumber = :phone, u.nickname = :nickname WHERE u.id = :id")
    void updateUserInfo(@Param("phone") String phone, @Param("nickname") String nickname, @Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.isDelete = false AND u.email = :email AND u.provider = :provider")
    Optional<User> findActiveUserByEmailAndProvider(@Param("email") String email, @Param("provider") String provider);

    Optional<User> findByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.isDelete = false")
    Optional<User> findByIdAndIsDelete(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.isDelete = true WHERE u.id = :userId")
    void updateUserIsDelete(@Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE u.nickname = :nickname AND NOT u.nickname = :oldNickname")
    Optional<User> findByNicknameToMod(@Param("nickname")String nickname, @Param("oldNickname")  String userNickname);

    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phone AND NOT u.phoneNumber = :oldPhone")
    Optional<Object> findPhoneToMod(@Param("phone")String phone, @Param("oldPhone")  String userPhone);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :userId")
    void rankUp(@Param("userId") Long userId , @Param("role")Role host); //이렇게 쓰면 롤의 값으로 들어가긴할텐데 한번해보고 안되면 바꾸죵
}
