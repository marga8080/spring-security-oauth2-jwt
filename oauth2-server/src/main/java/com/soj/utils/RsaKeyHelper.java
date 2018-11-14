package com.soj.utils;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 助手
 * 
 *
 */
public class RsaKeyHelper {
	
    /**
     * 获取公钥
     * @param publicKey 公钥字符串
     * @return 公钥
     */
	public static PublicKey genPublicKey(String publicKey) {
//		System.out.println(publicKey);
//		publicKey = publicKey.replaceAll("-----BEGIN (.*)-----", "");
		publicKey = publicKey.replaceAll("-----BEGIN PUBLIC KEY-----", "");
		publicKey = publicKey.replaceAll("-----END (.*)----", "");
		publicKey = publicKey.replaceAll("\r\n", "");
		publicKey = publicKey.replaceAll("\n", "");
		publicKey = publicKey.replaceAll(" ", "");
		byte[] der = Base64.getDecoder().decode(publicKey);

		X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			return kf.generatePublic(spec);
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException("Failed to load public key from String '" + publicKey + "'", e);
		}
	}
    
    /**
     * 获取公钥
     * @param fileName 文件名
     * @return 公钥
     */
    public static PublicKey getPublicKey(String fileName) {
        InputStream resourceAsStream = RsaKeyHelper.class.getClassLoader().getResourceAsStream(fileName);
        DataInputStream dis = new DataInputStream(resourceAsStream);
        try {
        	byte[] keyBytes = new byte[resourceAsStream.available()];
            dis.readFully(keyBytes);
            dis.close();
            
            // convert to der format
            String pem = new String(keyBytes);
            
            pem = pem.replaceAll("-----BEGIN (.*)-----", "");
            pem = pem.replaceAll("-----END (.*)----", "");
            pem = pem.replaceAll("\r\n", "");
            pem = pem.replaceAll("\n", "");
            byte[] der = Base64.getDecoder().decode(pem); 
            
            X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to load public key from file '" + fileName + "'", e);
        }
    }
    
//    /**
//     * 获取密钥
//     * @param fileName
//     * @return
//     * @throws IOException 
//     * @throws NoSuchAlgorithmException 
//     * @throws InvalidKeySpecException 
//     */
//    private static PrivateKey getPrivateKey(String fileName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
//        InputStream resourceAsStream = RsaKeyHelper.class.getClassLoader().getResourceAsStream(fileName);
//        DataInputStream dis = new DataInputStream(resourceAsStream);
//        byte[] keyBytes = new byte[resourceAsStream.available()];
//        dis.readFully(keyBytes);
//        dis.close();
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return kf.generatePrivate(spec);
//    }
    
    /**
     * 获取密钥
     * @param privateKey 私钥字符串
     * @return 私钥
     * @throws IOException io异常
     * @throws NoSuchAlgorithmException 无此算法异常 
     * @throws InvalidKeySpecException 无效的key规范异常
     */
    public static PrivateKey genPrivateKey(String privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    	privateKey = privateKey.replaceAll("-----BEGIN (.*)-----", "");
    	privateKey = privateKey.replaceAll("-----END (.*)----", "");
    	privateKey = privateKey.replaceAll("\r\n", "");
    	privateKey = privateKey.replaceAll("\n", "");
    	byte[] der = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * 生成rsa公钥和密钥
     * @param publicKeyFilename 公钥文件名
     * @param privateKeyFilename 私钥文件名
     * @param password 密码
     * @throws IOException io异常
     * @throws NoSuchAlgorithmException 无此算法异常
     */
    public static void generateKey(String publicKeyFilename, String privateKeyFilename, String password) throws IOException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom(password.getBytes());
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
//        System.out.println(Base64.encode(publicKeyBytes));
        FileOutputStream fos = new FileOutputStream(publicKeyFilename);
        fos.write(publicKeyBytes);
        fos.close();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        fos = new FileOutputStream(privateKeyFilename);
        fos.write(privateKeyBytes);
        fos.close();
    }

}

