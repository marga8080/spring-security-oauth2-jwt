package com.soj.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soj.http.RespResult;
import com.soj.utils.JedisUtils;

@RestController
@RequestMapping("sms")
public class SmsController {
	
	/**
	 * 登录发送验证码
	 * @param phone
	 * @return
	 */
	@GetMapping("loginPhoneCode")
	public RespResult<Object> loginPhoneCode(String phone, HttpSession session) {
		RespResult<Object> result = new RespResult<Object>();
		String code = "111111"; // TODO 随机码 测试用111111
		sendSms(code);
		JedisUtils.set(session.getId() + "-" + phone, code, 5 * 60); // 5分钟内有效
		return result;
	}
	
	/**
	 * 发送短信验证码
	 * @param code
	 */
	private void sendSms(String code) {
		// TODO
	}
}
