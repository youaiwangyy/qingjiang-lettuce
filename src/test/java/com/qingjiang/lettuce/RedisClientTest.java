package com.qingjiang.lettuce;

import com.alicp.jetcache.redis.lettuce.JetCacheCodec;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.StringCodec;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * create by qingjiang.li on 2018/11/22
 */
@Slf4j
public class RedisClientTest extends LettuceApplicationTests {

    @Autowired private RedisClient redisClient;

    @Test
    public void codec(){
        ByteArrayCodec codec = ByteArrayCodec.INSTANCE;
        JetCacheCodec codec1 = new JetCacheCodec();
        StringCodec codec2 = StringCodec.UTF8;
        StatefulRedisConnection<byte[], byte[]> connect = redisClient.connect(codec);
        StatefulRedisConnection connect1 = redisClient.connect(codec1);
        StatefulRedisConnection<String, String> connect2 = redisClient.connect(codec2);

        RedisCommands<byte[], byte[]> sync = connect.sync();
        RedisAsyncCommands<byte[], byte[]> async = connect.async();


    }



    @Test
    public void redisClientInfo() throws ExecutionException, InterruptedException {

        RedisURI redisURI  = RedisURI.Builder.redis("127.0.0.1", 6379).build();
        RedisClusterClient redisClusterClient = RedisClusterClient.create(redisURI);

        StatefulRedisClusterConnection<String, String> connect = redisClusterClient.connect();
        RedisAdvancedClusterCommands<String, String> sync = connect.sync();
        String replication = sync.info("replication");
        log.info("replication:{}", replication);

    }

    @Test
    public void testRedisClient() {
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> sync = connect.sync();
        Long lpush = sync.lpush("users", "kaka");
        log.info("lpush:{}", lpush);

    }

    @Test
    public void testRedisClusterClient() {
        //TODO
        RedisURI redisURI = RedisURI.create("127.0.0.1", 7379);
        RedisURI redisURI2 = RedisURI.create("127.0.0.1", 7380);
        List<RedisURI> list = Arrays.asList(redisURI, redisURI2);
        RedisClusterClient redisClusterClient = RedisClusterClient.create(list);
        StatefulRedisClusterConnection<String, String> connect = redisClusterClient.connect();
        RedisAdvancedClusterCommands<String, String> sync = connect.sync();
        Long lpush = sync.lpush("users", "fafa");
        log.info("lpush:{}", lpush);
    }

}
