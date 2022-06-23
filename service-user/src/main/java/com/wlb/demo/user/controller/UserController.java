package com.wlb.demo.user.controller;

import com.wlb.demo.user.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eric Wang
 * @since 2022/6/22 17:55
 */
@Slf4j
@RestController
@RequestMapping("info")
@Tag(name = "用户接口", description = "用户相关信息的接口文档")
public class UserController {

    @GetMapping("/{userId}")
    public User getUserInfo(@PathVariable("userId") Long userId){
        return User.builder().userName("wanglibo").birthday("2020-01-01").phoneNumber("15101171462").sex("男").build();
    }
}
