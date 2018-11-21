package com.qingjiang.lettuce.Service.user.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.MultiGetResult;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.qingjiang.lettuce.Service.user.UserService;
import com.qingjiang.lettuce.domain.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * create by qingjiang.li on 2018/11/21
 */
@Service
public class UserServiceImpl implements UserService {

    @CreateCache(name = "user.", expire = 3000, localExpire = 3000, cacheType = CacheType.BOTH, localLimit = 50)
    private Cache<Integer, User> userCache;


    @Override
    public boolean addUser(User... users) {
        Map<Integer, User> userMap = Maps.newHashMap();
        Arrays.stream(users).forEach(user -> {
            userMap.put(user.getId(), user);
        });
        userCache.putAll(userMap);
        return true;
    }

    @Override
    public User getUserById(int id) {
        User user = userCache.get(id);
        return user;
    }

    @Override
    public Map<Integer, User> getAll() {
        Set<Integer> ids = Sets.newHashSet();
        ids.add(100);
        ids.add(101);
        ids.add(102);
        MultiGetResult<Integer, User> userMultiGetResult = userCache.GET_ALL(ids);
        Map<Integer, User> userMap = userMultiGetResult.unwrapValues();
        return userMap;
    }

    @Override
    public Map<Integer, User> getUsers(Set<Integer> ids) {
        Map<Integer, User> users = userCache.getAll(ids);
        return users;
    }
}
