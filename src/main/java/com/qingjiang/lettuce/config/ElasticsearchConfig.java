package com.qingjiang.lettuce.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetAddress;

/**
 * create by qingjiang.li on 2018/12/25
 */
@Configuration
public class ElasticsearchConfig {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchConfig.class);

    /**
     * elk集群地址
     */
    @Value("${elasticsearch.ip}")
    private String hostName;
    /**
     * 端口
     */
    @Value("${elasticsearch.port}")
    private String port;
    /**
     * 集群名称
     */
    @Value("${elasticsearch.cluster.name}")
    private String clusterName;

    /**
     * 连接池
     */
    @Value("${elasticsearch.pool}")
    private String poolSize;

    @PostConstruct
    public void  init(){
        log.info(" ===>> hostName:{}", hostName);
        log.info(" ===>> port:{}", port);
        log.info(" ===>> clusterName:{}", clusterName);
        log.info(" ===>> poolSize:{}", poolSize);
    }


    @Bean
    public TransportClient initES() {

        TransportClient transportClient = null;

        try {
            // 配置信息
            Settings esSetting = Settings.builder()
                    .put("cluster.name", clusterName)
                    .put("client.transport.sniff", true)//增加嗅探机制，找到ES集群
                    .put("thread_pool.search.size", Integer.parseInt(poolSize))//增加线程池个数，暂时设为5
                    .build();


            transportClient = new PreBuiltTransportClient(esSetting);
            TransportAddress inetSocketTransportAddress = new TransportAddress(InetAddress.getByName(hostName), Integer.valueOf(port));
            transportClient.addTransportAddresses(inetSocketTransportAddress);

        } catch (Exception e) {
            log.error(" ===>> elasticsearch TransportClient create error!!!", e);
        }

        return transportClient;
    }
}
