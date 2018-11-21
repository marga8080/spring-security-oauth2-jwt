package com.soj.sso.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

	@Autowired
	ConsumerTokenServices consumerTokenServices;

	@GetMapping("index")
	@ResponseBody
	public Principal user(Principal principal) {
		System.out.println("===client1==");
		return principal;
	}

	@RequestMapping("logout")
	public String logout(Principal principal, HttpServletRequest request, HttpServletResponse response) {
		OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
		new SecurityContextLogoutHandler().logout(request, response, oAuth2Authentication);
		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) oAuth2Authentication.getDetails();
	    consumerTokenServices.revokeToken(details.getTokenValue());
		return "redirect:index";
	}

}
