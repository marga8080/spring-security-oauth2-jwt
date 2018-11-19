package com.soj.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import net.sf.json.JSONObject;

public class HttpUtilsTest {

	@Test
	public void postTest() throws IOException {
		Map<String, String> map = new HashMap<>();
		map.put("client_id", "client1");
		map.put("client_secret", "secret");
		map.put("grant_type", "authorization_code");
		map.put("code", "sDt78j");
		map.put("redirect_uri", "http://www.baidu.com");
		JSONObject str = HttpUtils.postForm("http://localhost:8080/oauth/token", map);
		System.out.println(str);
	}
}
