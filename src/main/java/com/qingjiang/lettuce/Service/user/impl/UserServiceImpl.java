package com.qingjiang.lettuce.Service.user.impl;

import com.qingjiang.lettuce.Service.user.UserService;
import com.qingjiang.lettuce.cache.redis.CacheService;
import com.qingjiang.lettuce.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * create by qingjiang.li on 2018/11/21
 */
@Service
public class UserServiceImpl implements UserService {



    @Autowired private CacheService cacheService;

    @Override
    public boolean addUser(User... users) {
        //添加数据库
        boolean add = true;//mock 添加数据库成功
        //添加缓存
        cacheService.addUser(users);
        return true;
    }

    @Override
    public User getUserById(int id) {
        User user = cacheService.getUserById(id);
        if (user == null) {
            //从数据库查
            //添加缓存
        }
        return user;
    }

    @Override
    public Map<Integer, User> getUsers(Set<Integer> ids) {
        Set<User> userSet = cacheService.getUsers(ids);
        Map<Integer, User> userMap = userSet.stream().collect(Collectors.toMap(User::getId, user -> user));
        return userMap;
    }

}
