package com.scm.scm20.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/dashboard")
    public String userDashboard() {
        System.out.println("User Dashboard requested");
        return "user/dashboard";
    }

    @RequestMapping(value = "/profile")
    public String userProfile() {
        System.out.println("User Profile requested");
        return "user/profile";
    }

}
