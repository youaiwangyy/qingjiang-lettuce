package com.qingjiang.lettuce.cache.redis;

import com.qingjiang.lettuce.domain.User;

import java.util.Set;

/**
 * @author qingjiang.li
 * @date 2018/11/23
 */
public interface CacheService {


		boolean addUser(User... user);

		User getUserById(int id);

		Set<User> getUsers(Set<Integer> idSet);



}
