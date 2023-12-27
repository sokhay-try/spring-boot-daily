package com.springbootdaily.controllers;

import com.springbootdaily.utils.AppConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstant.BASE_URL + "demo")
public class DemoController {
    @GetMapping("/user")
    public String roleUser() {
        return "Allow only role is USER";
    }

    @GetMapping("/admin")
    public String roleAdmin() {
        return "Allow only role is ADMIN";
    }
}
