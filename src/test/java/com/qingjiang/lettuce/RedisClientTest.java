package com.qingjiang.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * create by qingjiang.li on 2018/11/22
 */
@Slf4j
public class RedisClientTest extends LettuceApplicationTests {

    @Autowired private RedisClient redisClient;

    @Test
    public void testRedisClient() {
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> sync = connect.sync();
        Long lpush = sync.lpush("users", "qingjiang");
        log.info("lpush:{}", lpush);
    }

}
