package com.taotao.commons.pojo;

import java.io.Serializable;

public class PictureResult implements Serializable {
	private int error;
	private String message;
	private String url;
	//成功时调用的方法
	public PictureResult(int error, String message, String url) {
		// TODO Auto-generated constructor stub
	}
	public static PictureResult ok(String url) {
	return new PictureResult(0, url, null);
	}
	//失败时调用的方法
	public static PictureResult error(String message) {
	return new PictureResult(1, null, message);
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
