package com.jinjing.springboot.springbootstudy.api.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jinjing.springboot.springbootstudy.api.HelloService;
import org.springframework.stereotype.Component;

/**
 * @author JinJing
 * @date 2019/2/13 下午3:15
 */

@Service(interfaceClass = HelloService.class)
@Component
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello() {
        return "Hello world";
    }
}
