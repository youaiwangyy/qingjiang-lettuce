package com.qingjiang.lettuce.Service.user;

import com.qingjiang.lettuce.domain.User;

import java.util.Map;
import java.util.Set;

/**
 * create by qingjiang.li on 2018-11-21
 */
public interface UserService {

    boolean addUser(User ... user);

    User getUserById(int id);

    Map<Integer, User> getUsers(Set<Integer> ids);

}
