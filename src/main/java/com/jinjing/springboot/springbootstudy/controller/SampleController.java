package com.jinjing.springboot.springbootstudy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qilong.jj
 * @date 2018/7/16 下午4:07
 */
@RestController
public class SampleController {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }
}
