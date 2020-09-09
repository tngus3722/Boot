package com.example.demo.controller;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RedisController {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> StringRedis;

    @RequestMapping(value = "/redisGetString" , method = RequestMethod.GET)
    public String GetString(String key){
        return StringRedis.get(key);
    }

    @RequestMapping(value =  "/redisSetString", method = RequestMethod.GET)
    public void SetString(String key, String value){
        StringRedis.set(key,value);
    }
}
