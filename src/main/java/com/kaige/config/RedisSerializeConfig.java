package com.kaige.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kaige.config.LocalDateTimeConvert;
import org.babyfish.jimmer.jackson.JsonConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.time.LocalDateTime;

//package com.kaige.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.babyfish.jimmer.jackson.ImmutableModule;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.LocalDateTime;
//
///**
// * 配置Redis序列化器
// * 将默认的 JDK 序列化改为 JSON 序列化。
// */
//@Configuration
//public class RedisSerializeConfig {
//
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
////        创建实例，用于操作 Redis。
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//
////        设置redis连接工厂，确保其能够通过连接工厂与 Redis 服务交互。
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        // 创建ObjectMapper并注册ImmutableModule
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new ImmutableModule());
//        objectMapper.registerModule(new JavaTimeModule());
//
//        objectMapper.addSerializer(LocalDateTime.class, new LocalDateTimeConvert.Serializer());
//        objectMapper.addDeserializer(LocalDateTime.class, new LocalDateTimeConvert.Deserializer());
//
//        // 创建 Jackson2JsonRedisSerializer 时直接传入 ObjectMapper
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
//
//
////        设置默认序列化器
//        redisTemplate.setDefaultSerializer(serializer);
//
//
//        return redisTemplate;
//    }
//
//}
@Configuration
public class RedisSerializeConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 创建 ObjectMapper 并注册 JavaTimeModule
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // 默认处理 LocalDateTime

        // 创建 SimpleModule 并注册自定义序列化和反序列化器
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeConvert.Serializer());
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeConvert.Deserializer());

        // 将 SimpleModule 注册到 ObjectMapper
        objectMapper.registerModule(simpleModule);

        // 创建 Jackson2JsonRedisSerializer 时直接传入 ObjectMapper
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        // 设置默认序列化器
        redisTemplate.setDefaultSerializer(serializer);

        return redisTemplate;
    }
}

