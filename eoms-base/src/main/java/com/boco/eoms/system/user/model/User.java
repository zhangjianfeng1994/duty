package com.boco.eoms.system.user.model;

import java.io.Serializable;

/**
 * 用户model
 * @author chenjianghe
 *
 */
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	
	
}
