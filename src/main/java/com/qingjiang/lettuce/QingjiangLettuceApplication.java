package com.qingjiang.lettuce;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.qingjiang.lettuce")
@EnableCreateCacheAnnotation
@ComponentScan(value = "com.qingjiang.lettuce.*")
public class QingjiangLettuceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QingjiangLettuceApplication.class, args);
	}
}



