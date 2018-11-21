package com.soj.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
	@GetMapping("/login")
    public String login(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
        	logger.error(error);
            model.addAttribute("error", error);
        }
        return "login";
    }
	
	@GetMapping("/client/logout")
	public String logoutClient(String redirect_uri, HttpServletRequest request, HttpServletResponse response) {
		//注销session和cookies
		request.getSession().invalidate();//清除 session 中的所有信息
		//退出登录的时候清空cookie信息,cookie需要通过HttpServletRequest，HttpServletResponse获取
		Cookie[] cookie=request.getCookies();
		if (cookie != null) {
			for(Cookie c:cookie){
				c.setMaxAge(0);
				response.addCookie(c);
			}
		}
		return "redirect:" + redirect_uri;
	}
}
