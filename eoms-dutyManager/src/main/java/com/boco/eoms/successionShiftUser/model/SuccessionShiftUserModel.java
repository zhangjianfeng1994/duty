package com.boco.eoms.successionShiftUser.model;


import java.util.Date;

/** DUTY_SUCCESSION_SHIFT_USER */
public class SuccessionShiftUserModel {

	/** DUTY_SUCCESSION_SHIFT_USER.SHIFT_USER_ID */
	private Integer SHIFT_USER_ID;
	/** DUTY_SUCCESSION_SHIFT_USER.USER_ID */
	private String USER_ID;
	/** DUTY_SUCCESSION_SHIFT_USER.SHIFT_ID */
	private Integer SHIFT_ID;
	/** DUTY_SUCCESSION_SHIFT_USER.SUCCESSION_TIME */
	private Date SUCCESSION_TIME;
	/** DUTY_SUCCESSION_SHIFT_USER.SHIFT_TIME */
	private Date SHIFT_TIME;
	/** DUTY_SUCCESSION_SHIFT_USER.SUCCESSION_IF */
	private Integer SUCCESSION_IF;
	/** DUTY_SUCCESSION_SHIFT_USER.SHIFT_IF */
	private Integer SHIFT_IF;
	
	/** DUTY_SUCCESSION_SHIFT_USER.NOTE */
	private String NOTE;

	public String getNOTE() {
		return NOTE;
	}
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}
	/** DUTY_SUCCESSION_SHIFT_USER.SHIFT_USER_ID */
	public Integer getSHIFT_USER_ID() {
		return SHIFT_USER_ID;
	}
	/** DUTY_SUCCESSION_SHIFT_USER.SHIFT_USER_ID */
	public void setSHIFT_USER_ID(Integer SHIFT_USER_ID) {
		this.SHIFT_USER_ID = SHIFT_USER_ID;
	}
	/** DUTY_SUCCESSION_SHIFT_USER.USER_ID */
	public String getUSER_ID() {
		return USER_ID;
	}
	/** DUTY_SUCCESSION_SHIFT_USER.USER_ID */
	public void setUSER_ID(String USER_ID) {
		this.USER_ID = USER_ID == null ? null : USER_ID.trim();
	}
	/** DUTY_SUCCESSION_SHIFT_USER.SHIFT_ID */
	public Integer getSHIFT_ID() {
		return SHIFT_ID;
	}
	/** DUTY_SUCCESSION_SHIFT_USER.SHIFT_ID */
	public void setSHIFT_ID(Integer SHIFT_ID) {
		this.SHIFT_ID = SHIFT_ID;
	}
	/** DUTY_SUCCESSION_SHIFT_USER.SUCCESSION_TIME */
	public Date getSUCCESSION_TIME() {
		return SUCCESSION_TIME;
	}
	/** DUTY_SUCCESSION_SHIFT_USER.SUCCESSION_TIME */
	public void setSUCCESSION_TIME(Date SUCCESSION_TIME) {
		this.SUCCESSION_TIME = SUCCESSION_TIME;
	}
	/** DUTY_SUCCESSION_SHIFT_USER.SHIFT_TIME */
	public Date getSHIFT_TIME() {
		return SHIFT_TIME;
	}
	/** DUTY_SUCCESSION_SHIFT_USER.SHIFT_TIME */
	public void setSHIFT_TIME(Date SHIFT_TIME) {
		this.SHIFT_TIME = SHIFT_TIME;
	}
	/** DUTY_SUCCESSION_SHIFT_USER.SUCCESSION_IF */
	public Integer getSUCCESSION_IF() {
		return SUCCESSION_IF;
	}
	/** DUTY_SUCCESSION_SHIFT_USER.SUCCESSION_IF */
	public void setSUCCESSION_IF(Integer SUCCESSION_IF) {
		this.SUCCESSION_IF = SUCCESSION_IF;
	}
	/** DUTY_SUCCESSION_SHIFT_USER.SHIFT_IF */
	public Integer getSHIFT_IF() {
		return SHIFT_IF;
	}
	/** DUTY_SUCCESSION_SHIFT_USER.SHIFT_IF */
	public void setSHIFT_IF(Integer SHIFT_IF) {
		this.SHIFT_IF = SHIFT_IF;
	}

}