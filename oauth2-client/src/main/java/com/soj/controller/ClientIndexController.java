package com.soj.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClientIndexController {

	@RequestMapping("index")
	@ResponseBody
	public Object index() {
		Map<String, String> map = new HashMap<>();
		map.put("message", "客户端");
		return map;
	}
}
