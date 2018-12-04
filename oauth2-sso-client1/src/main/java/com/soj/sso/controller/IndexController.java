package com.soj.sso.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {


	@GetMapping("index")
	@ResponseBody
	public Principal index(Principal principal) {
		System.out.println("===client1==");
		return principal;
	}

}
