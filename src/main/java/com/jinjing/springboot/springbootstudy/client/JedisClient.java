package com.jinjing.springboot.springbootstudy.client;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * @author JinJing
 * @date 2019/5/13 下午3:27
 */
@Component
public class JedisClient implements InitializingBean {

    private Jedis jedis;


    public Map<String, String> hgetAll(String key){
        return jedis.hgetAll(key);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        jedis = new Jedis("localhost");
    }
}
