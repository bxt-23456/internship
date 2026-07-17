package com.whlg.hospital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author: wanjianhong
 * @Version: 1.0
 * @Description:
 */
@Configuration
public class RedisConfig {
    @Bean(name = "redisTemplate")
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String,Object> template = new RedisTemplate<String,Object>();

        //指定key序列化方式
        RedisSerializer<String> keySerializer = new StringRedisSerializer();
        //指定value序列化方式
        RedisSerializer<Object> valueSerializer = new GenericJackson2JsonRedisSerializer();

        //通过Java Config配置方式实现Spring依赖注入
        template.setConnectionFactory(factory);
        template.setKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        return template;
    }
}
