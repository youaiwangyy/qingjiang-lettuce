package com.qingjiang.lettuce.cache.redis.impl;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.qingjiang.lettuce.cache.redis.CacheService;
import com.qingjiang.lettuce.domain.User;
import com.qingjiang.lettuce.utils.StringUtil;
import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qingjiang.li
 * @date 2018/11/23
 */
@Service
public class CacheServiceImpl implements CacheService {

		@Autowired
		private RedisClient redisClient;
		@CreateCache(name = "user.", localExpire = 60 * 60, cacheType = CacheType.LOCAL, localLimit = 100)
		private Cache<Integer, User> userCache;

		private static final ByteArrayCodec BYTE_ARRAY_CODEC = ByteArrayCodec.INSTANCE;
		private static final String OK = "OK";

		private static final String USERS_HASH = "users.hash";

		private static volatile StatefulRedisConnection<String, String> connect = null;
		private static volatile StatefulRedisConnection<byte[], byte[]> bconnect = null;

		private static volatile RedisCommands<String, String> sync = null;
		private static volatile RedisCommands<byte[], byte[]> bsync = null;

		private static volatile RedisAsyncCommands<String, String> async = null;
		private static volatile RedisAsyncCommands<byte[], byte[]> basync = null;


		private StatefulRedisConnection<String, String> getConnect() {
				if (connect == null) {
						synchronized (StatefulRedisConnection.class) {
								if (connect == null) {
										connect = redisClient.connect();
								}
						}
				}
				return connect;
		}

		private StatefulRedisConnection<byte[], byte[]> getBconnect() {
				if (bconnect == null) {
						synchronized (StatefulRedisConnection.class) {
								if (bconnect == null) {
										bconnect = redisClient.connect(BYTE_ARRAY_CODEC);
								}
						}
				}
				return bconnect;
		}

		private RedisCommands<String, String> getSync() {
				if (sync == null) {
						synchronized (RedisCommands.class) {
								if (sync == null) {
										sync = getConnect().sync();
								}
						}
				}
				return sync;
		}

		private RedisCommands<byte[], byte[]> getBsync() {
				if (bsync == null) {
						synchronized (RedisCommands.class) {
								if (bsync == null) {
										bsync = getBconnect().sync();
								}
						}
				}
				return bsync;
		}

		private RedisAsyncCommands<String, String> getAsync() {
				if (async == null) {
						synchronized (RedisAsyncCommands.class) {
								if (async == null) {
										async = getConnect().async();
								}
						}
				}
				return async;
		}

		private RedisAsyncCommands<byte[], byte[]> getBasync() {
				if (basync == null) {
						synchronized (RedisAsyncCommands.class) {
								if (basync == null) {
										basync = getBconnect().async();
								}
						}
				}
				return basync;
		}

		@Override
		public boolean addUser(User... users) {
				if (users == null || users.length == 0) {
						return false;
				}
				Map<String, String> redisMap = Maps.newHashMap();
				Map<Integer, User> userMap = Arrays.stream(users).collect(Collectors.toMap(User::getId, user -> user));

				//local
				userCache.putAll(userMap);
				//redis
				/*Map<String, String> userMap = Arrays.stream(users)
								.collect(Collectors.toMap((u) -> String.valueOf(u.getId()), (u) -> JSON.toJSONString(u)));*/
				userMap.forEach((k,v)->{
						redisMap.put(String.valueOf(k), JSON.toJSONString(v));
				});

				String hmset = getSync().hmset(USERS_HASH, redisMap);
				return OK.equalsIgnoreCase(hmset);
		}


		@Override
		public User getUserById(int id) {
				User user = null;
				String hget = getSync().hget(USERS_HASH, String.valueOf(id));
				if (StringUtil.notBlank(hget)) {
						user = JSON.parseObject(hget, User.class);
				}
				return user;
		}

		@Override
		public Set<User> getUsers(Set<Integer> idSet) {
				Set<User> userSet = Sets.newHashSet();
				List<KeyValue<String, String>> hmget = getSync().hmget(USERS_HASH, idSet.toArray(new String[idSet.size()]));
				if (!CollectionUtils.isEmpty(hmget)) {
						hmget.stream().forEach((kv -> {
								userSet.add(JSON.parseObject(kv.getValue(), User.class));
						}));
				}
				return userSet;
		}


}
