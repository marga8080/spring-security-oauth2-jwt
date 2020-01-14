package com.soj.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soj.model.User;
import com.soj.service.UserService;

@RestController
@RequestMapping("api")
public class UserApi {

	@Autowired
	UserService userService;
	
	@GetMapping("user")
    public User getUser(Principal principal){
		User user = userService.getByUsername(principal.getName());
        return user;
    }
}
