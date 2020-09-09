package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/* 참고로 springboot 2.0이상부터는 auto-configuration으로
 아래의 빈(redisConnectionFactory, RedisTemplate, StringTemplate)들이 자동으로 생성되기 때문에 굳이 Configuration을 만들지 않아도 즉시 사용가능하다.)
 */
@Configuration
public class RedisConfig {

    // jedis vs lettuce -> thread safe 유무, 성능 -> lettuce 권장
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
        return lettuceConnectionFactory;
    }
/*
 setKeySerializer(), setValueSerializer() 이 메소드들을 빠트리면 실제 스프링에서 조회할 때는 값이 정상으로 보이지만
 redis-cli로 보면 key값에 \xac\xed\x00\x05t\x00\x0 이런 값들이 붙는다.
 Json serializer도 유용할듯.
*/
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
       redisTemplate.setKeySerializer(new StringRedisSerializer());
       redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }


    //대부분 레디스 key-value 는 문자열 위주이기 때문에 문자열에 특화된 템플릿을 제공
    //Redis는 기본적으로 Key, Value를 바이트의 배열로 저장한다. StringRedisTemplate를 사용하면 Key, Value 모두 문자열로 저장할 수 있다.
    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return stringRedisTemplate;
    }
}
