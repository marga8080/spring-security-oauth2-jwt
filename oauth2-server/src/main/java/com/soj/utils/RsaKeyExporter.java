package com.soj.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import sun.misc.BASE64Encoder;

/**
 * 从RSA的jks文件导出私钥和公钥的工具类
 * 
 * 1、生成jks：
 * keytool -genkeypair -alias mytest -keyalg RSA -keypass mypass -keystore mytest.jks -storepass mypass
 * 2、使用此工具类导出私钥或公钥
 * 运行测试用例：RsaKeyExporterTest
 * 
 * 参考：
 * https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.2
 * http://blog.csdn.net/freezingxu/article/details/71547485
 * 
 */
@SuppressWarnings("restriction")
public class RsaKeyExporter {

	private String keystoreFile;
	private String keyStoreType;
	private String password;
	private String alias;
	private String privateKeyFile;
	private String publicKeyFile;

	public static KeyPair getKeyPair(KeyStore keystore, String alias, String password)
			throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
		Key key = keystore.getKey(alias, password.toCharArray());
		if (key instanceof PrivateKey) {
			Certificate cert = keystore.getCertificate(alias);
			PublicKey publicKey = cert.getPublicKey();
			return new KeyPair(publicKey, (PrivateKey) key);
		}
		throw new RuntimeException("Can't get private key!");
	}

	public void export() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, UnrecoverableKeyException {
		KeyStore keystore = KeyStore.getInstance(keyStoreType);
		BASE64Encoder encoder = new BASE64Encoder();
		InputStream inputstream = RsaKeyHelper.class.getClassLoader().getResourceAsStream(keystoreFile);
		keystore.load(inputstream, password.toCharArray());
		KeyPair keyPair = getKeyPair(keystore, alias, password);
		
		// 导出私钥
		if (privateKeyFile != null) {
			PrivateKey privateKey = keyPair.getPrivate();
			String encoded = encoder.encode(privateKey.getEncoded());
			FileWriter fw = new FileWriter(new File(privateKeyFile));
			fw.write("-----BEGIN PRIVATE KEY-----\n");
			fw.write(encoded);
			fw.write("\n");
			fw.write("-----END PRIVATE KEY-----");
			fw.close();
		}

		// 导出公钥
		if (publicKeyFile != null) {
			PublicKey publicKey = keyPair.getPublic();
			String encoded = encoder.encode(publicKey.getEncoded());
			FileWriter fw = new FileWriter(new File(publicKeyFile));
			fw.write("-----BEGIN PUBLIC KEY-----\n");
			fw.write(encoded);
			fw.write("\n");
			fw.write("-----END PUBLIC KEY-----");
			fw.close();
		}
	}

	public void setKeystoreFile(String keystoreFile) {
		this.keystoreFile = keystoreFile;
	}

	public void setKeyStoreType(String keyStoreType) {
		this.keyStoreType = keyStoreType;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setPrivateKeyFile(String privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
	}

	public void setPublicKeyFile(String publicKeyFile) {
		this.publicKeyFile = publicKeyFile;
	}
}