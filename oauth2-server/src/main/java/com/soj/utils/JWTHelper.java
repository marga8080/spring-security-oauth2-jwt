package com.soj.utils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.util.Assert;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT助手
 * 
 */
public class JWTHelper {
	
	// 私钥
	private String privateKey;

	// 公钥
	private String publicKey;
    
    /**
     * 密钥加密token
     *
     * @param claims 用户信息
     * @param expire 过期时间，单位为秒
     * @return 加密的jwt token
     * @throws IOException io异常
     * @throws InvalidKeySpecException 无效的key规范异常
     * @throws NoSuchAlgorithmException 无此算法异常
     */
    public String generateToken(Map<String, Object> claims, int expire) throws NoSuchAlgorithmException, 
    	InvalidKeySpecException, IOException {
    	Assert.notNull(privateKey, "privateKey can't not be null");
        String compactJws = Jwts.builder().setClaims(claims)
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(SignatureAlgorithm.RS256, RsaKeyHelper.genPrivateKey(privateKey))
                .compact();
        return compactJws;
    }
    
    
    /**
     * 公钥解析token
     *
     * @param token jwt token
     * @return 解析结果
     */
    public Jws<Claims> parserToken(String token) {
    	Assert.notNull(publicKey, "publicKey can't not be null");
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(RsaKeyHelper.genPublicKey(publicKey)).parseClaimsJws(token);
        return claimsJws;
    }

	
	

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
}
