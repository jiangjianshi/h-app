package com.huifenqi.usercomm.dao;

import com.huifenqi.usercomm.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by arison on 2015/9/4.
 */

public interface UserRepository extends JpaRepository<User, Long> {

    User findByPhone(String phone);
    User findByPhoneAndState(String phone, int state);
    User findByUseridAndState(long userId, int state);
    List<User> findByUserIdNoAndState(String userIdNo, int state);
    List<User> findByUserIdNoAndStateOrderByUseridDesc(String userIdNo, int state);
}
