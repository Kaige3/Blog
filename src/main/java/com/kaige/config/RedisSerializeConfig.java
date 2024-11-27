package com.kaige.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 配置Redis序列化器
 * 将默认的 JDK 序列化改为 JSON 序列化。
 */
@Configuration
public class RedisSerializeConfig {

    @Bean
    public RedisTemplate<Object, Object> jsonRedisTemplate(RedisConnectionFactory redisConnectionFactory){
//        创建实例，用于操作 Redis。
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
//        return redisTemplate;
//        设置redis连接工厂，确保其能够通过连接工厂与 Redis 服务交互。
        template.setConnectionFactory(redisConnectionFactory);
//        设置序列化器
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        设置默认序列化器
        template.setDefaultSerializer(serializer);

        return template;
    }

}
