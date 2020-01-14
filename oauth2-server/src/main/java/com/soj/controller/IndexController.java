package com.soj.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class IndexController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("index")
	public String index() {
		return "index";
	}
	
	@GetMapping("login")
    public String login(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
        	logger.error(error);
            model.addAttribute("error", error);
        }
        return "login";
    }
	
}
