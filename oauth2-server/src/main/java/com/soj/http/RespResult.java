package com.soj.http;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 请求服务器返回的结果对象
 * @author mawei
 *
 * @param <T> 数据对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespResult<T> implements Serializable {

	private static final long serialVersionUID = -353661331696549464L;
	
	public static final String SUCCESS = "0";
	
	private String errcode = SUCCESS; //errcode = 0表示正常
	private String errmsg; // 描述
	private T data;
	
	public RespResult() {
	    this.setErrcode(SUCCESS);
	}
	
	public RespResult(String errcode, String errmsg) {
	    this.errcode = errcode;
	    this.errmsg = errmsg;
	}

	public void setResult(String errcode, String errmsg) {
		this.setErrcode(errcode);
		this.setErrmsg(errmsg);
	}
	
	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
}
