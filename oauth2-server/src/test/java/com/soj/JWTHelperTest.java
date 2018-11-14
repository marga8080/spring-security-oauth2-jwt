package com.soj;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.soj.utils.JWTHelper;
import com.soj.utils.RsaKeyHelper;

import io.jsonwebtoken.Claims;

/**
 * JWT助手测试类
 * 
 * @author mawei
 *
 */
public class JWTHelperTest {

	private JWTHelper jwtHelper = new JWTHelper();

	@Before
	public void setUp() throws Exception {
		// 初始化一个rsa公钥
		String publicKey = getKeyString("public.txt");
		jwtHelper.setPublicKey(publicKey);

		// 初始化一个rsa公钥
		String privateKey = getKeyString("private.txt");
		jwtHelper.setPrivateKey(privateKey);
	}

	private String getKeyString(String fileName) {
		InputStream resourceAsStream = RsaKeyHelper.class.getClassLoader().getResourceAsStream(fileName);
		DataInputStream dis = new DataInputStream(resourceAsStream);
		byte[] keyBytes = null;
		try {
			keyBytes = new byte[resourceAsStream.available()];
			dis.readFully(keyBytes);
			dis.close();

		} catch (IOException e) {
			throw new RuntimeException("Failed to load public key from file '" + fileName + "'", e);
		}

		// convert to der format
		return new String(keyBytes);
	}
	
	@Test
	public void testParserToken() {
		String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImZvbyIsInJlYWQiLCJ3cml0ZSJdLCJuYW1lIjoi5byg5LiJIiwiaWQiOiIxIiwiZXhwIjoxNTQyMDA0MzEwLCJhdXRob3JpdGllcyI6WyJBQ1RVQVRPUiIsIlJPTEVfVVNFUiJdLCJqdGkiOiJmZmZhMjlhYy1jODNlLTQ3MzEtYWExMi0wYmU3MWQ2NzllY2EiLCJjbGllbnRfaWQiOiJjbGllbnQxIn0.kgMTz1dCcmzHd42ALRXEFAMgFeTRgN6alfYcvI8r-q75DaeC_f5bygf11c68-XB_X1oacq8rj5dbtHXuJRw71gf_OMjG1s8Qu_1OE-JN31BLys6nh0EE1xhSMo4zXW5FCKEkfYIEI5VE9VeHjHCFR7DjpQPsFxKY5NNj-Ru4t5oYYCyPOWhMWH_RWU68nJN1As9QIWAA0wIuJ6_k55J00mG6Z6p1geU5xRO6F-OXIghq5GkKsKPb5R2wIkDCN9XTU-bCIrIKh7o5Pq91ib_ctF9PoORKjGIx2MEfmUvDwKXpTmLwt9DLXmAJZ7nzdHE1jDHTGrjpciWC5kEOJ0gaWw";
		Claims c = jwtHelper.parserToken(token).getBody();
		System.out.println(c);
	}

}
