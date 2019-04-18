package com.taotao.commons.pojo;

import java.io.Serializable;

public class EasyUITreeNode implements Serializable {
	/**
	 * "id": 1, //当前节点的id  
	 *"text": "Node 1"节点显示的名称
	 *"state": "closed"节点的状态，如果是closed就是一个文件夹形式
	 * **/
	private Long id;
	private String text;
	private String state;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}	
}
