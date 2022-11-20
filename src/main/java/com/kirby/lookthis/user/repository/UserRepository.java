package com.kirby.lookthis.user.repository;

import com.kirby.lookthis.spot.dto.PointDto;
import com.kirby.lookthis.user.dto.UserDto;
import com.kirby.lookthis.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String>{
    User findByUserId(String userid);

    @Modifying
    @Query("UPDATE User u set u.point = :#{#userDto.point} " +
            "where u.userId = :#{#userDto.userId}" )
    void updatePoint(@Param("userDto") UserDto userDto);
}
