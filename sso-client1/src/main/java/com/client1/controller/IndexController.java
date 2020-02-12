package com.client1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

	@GetMapping({"/", "index"})
	public Object index() {
		return "哈哈哈哈=========> index";
	}
	
	@GetMapping("index2")
	public Object index2() {
		return "呵呵呵呵=========> index2";
	}
}
