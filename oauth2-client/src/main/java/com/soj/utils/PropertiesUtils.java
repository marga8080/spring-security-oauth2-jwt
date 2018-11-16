package com.soj.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

public class PropertiesUtils {

	static Properties props;
	
    public static void init(Properties props) {
    	PropertiesUtils.props = props;
    }
    
    /**
     * 初始化
     * @param in properties文件流
     */
    public static void init(InputStream in) {
    	props = new Properties();
    	try {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
		}
    	init(props);
    }
    
    /**
     * 初始化
     * @param path properties文件路勁 (classpath)
     */
    public static void init(String path) {
    	InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream(path);
    	init(in);
    }
	

	public static String getProperty(String key) {
		return props.getProperty(key);
	}

}
