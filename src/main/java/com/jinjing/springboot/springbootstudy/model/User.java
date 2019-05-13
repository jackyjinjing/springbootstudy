package com.jinjing.springboot.springbootstudy.model;

import lombok.Data;

import java.util.Map;

/**
 * @author JinJing
 * @date 2019/5/13 上午11:28
 */
@Data
public class User {

    private Long id;

    private String name;

    private Integer age;

    public User(){

    }

    public User(Long id, Map<String, String> paramMap) {
        this.id = id;
        this.name = paramMap.get("name");
        String age = paramMap.get("age");
        if (age != null) {
            this.age = Integer.valueOf(age);
        }
    }
}
