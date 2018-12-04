package com.soj.sso.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

//	@Autowired
//	ConsumerTokenServices consumerTokenServices;

	@GetMapping("index")
	@ResponseBody
	public Principal user(Principal principal) {
		System.out.println("===client2==");
		return principal;
	}

//	@RequestMapping("logout")
//	public String logout(Principal principal, HttpServletRequest request, HttpServletResponse response) {
//		OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
//		new SecurityContextLogoutHandler().logout(request, response, oAuth2Authentication);
//		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) oAuth2Authentication.getDetails();
//	    consumerTokenServices.revokeToken(details.getTokenValue());
//		return "redirect:index";
//	}

}
