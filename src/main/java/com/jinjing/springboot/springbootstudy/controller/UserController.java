package com.jinjing.springboot.springbootstudy.controller;

import com.alibaba.fastjson.JSON;
import com.jinjing.springboot.springbootstudy.model.User;
import com.jinjing.springboot.springbootstudy.client.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * @author qilong.jj
 * @date 2018/7/16 下午4:17
 */

@RestController
@RequestMapping(value = "/springboot")
public class UserController {


    @Autowired
    private JedisClient jedisClient;

    @RequestMapping(value = "/getUserById", method = RequestMethod.GET)
    String getUserById(@RequestParam(value = "id") Long id){
        Map<String, String> paramMap = jedisClient.hgetAll(String.valueOf(id));
        if(paramMap != null) {
            User user = new User(id, paramMap);
            return JSON.toJSONString(user);
        }
        return "not found";
    }


    @RequestMapping(value = "/getUserByJson",method = RequestMethod.POST)
    String getUserByJson(@RequestBody String data){
        return "Json is " + data;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    String save(@RequestBody User user){
        Jedis jedis = new Jedis("localHost");
        jedis.hset(String.valueOf(user.getId()),"name",user.getName());
        jedis.hset(String.valueOf(user.getId()),"age",String.valueOf(user.getAge()));
        return "OK";
    }




}


