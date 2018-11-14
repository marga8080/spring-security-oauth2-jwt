package com.soj;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.junit.Test;

import com.soj.utils.RsaKeyExporter;

/**
 * 从RSA的jks文件导出私钥或公钥的工具类
 * 
 * @author mawei
 *
 */
public class RsaKeyExporterTest {

	@Test
	public void testExport() throws UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
		RsaKeyExporter exporter = new RsaKeyExporter();
		exporter.setKeyStoreType("JKS");
		exporter.setKeystoreFile("lwuums.jks");
		exporter.setPassword("uumspass");
		exporter.setAlias("lwuums");
		// 设置导出私钥或公钥
		exporter.setPrivateKeyFile("src/test/resources/private.txt");
		exporter.setPublicKeyFile("src/test/resources/public.txt");
		exporter.export();
	}

}
