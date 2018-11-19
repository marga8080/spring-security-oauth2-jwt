package com.soj.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soj.filter.AuthenticationFilter;
import com.soj.model.UserInfo;

@Controller
public class ClientIndexController {

	@RequestMapping("index")
	@ResponseBody
	public Object index(HttpSession session) {
		Map<String, Object> map = new HashMap<>();
		map.put("message", "客户端");
		UserInfo user = (UserInfo) session.getAttribute(AuthenticationFilter.USER_INFO);
		map.put("user", user);
		return map;
	}
	
	
	@RequestMapping("logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		//2、注销session和cookies
		request.getSession().invalidate();//清除 session 中的所有信息
		//退出登录的时候清空cookie信息,cookie需要通过HttpServletRequest，HttpServletResponse获取
		Cookie[] cookie=request.getCookies();
		if (cookie != null) {
			for(Cookie c:cookie){
				c.setMaxAge(0);
				response.addCookie(c);
			}
		}
		return "redirect:index";
	}
}
