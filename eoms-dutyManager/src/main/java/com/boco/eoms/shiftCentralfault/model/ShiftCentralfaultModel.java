package com.boco.eoms.shiftCentralfault.model;

import java.util.Date;

/** DUTY_SHIFT_CENTRALFAULT */
public class ShiftCentralfaultModel {

	/** DUTY_SHIFT_CENTRALFAULT.SHEETID */
	private String id;
	/** DUTY_SHIFT_CENTRALFAULT.SHIFT_ID */
	private Integer shiftid;
	/** DUTY_SHIFT_CENTRALFAULT.TITLE */
	private String title;
	/** DUTY_SHIFT_CENTRALFAULT.SENDTIME */
	private Date sendtime;
	/** DUTY_SHIFT_CENTRALFAULT.SHEETCOMPLETELIMIT */
	private String sheetcompletelimit;
	/** DUTY_SHIFT_CENTRALFAULT.MAINALARMCLEARTIME */
	private String mainalarmcleartime;
	/** DUTY_SHIFT_CENTRALFAULT.TASKNAME */
	private String taskname;
	/** DUTY_SHIFT_CENTRALFAULT.TASKSTATUS */
	private String taskstatus;
	/** DUTY_SHIFT_CENTRALFAULT.TASKOWNER */
	private String taskowner;
	/** DUTY_SHIFT_CENTRALFAULT.PREDEALDEPT */
	private String predealdept;
	/** DUTY_SHIFT_CENTRALFAULT.PREDEALUSERID */
	private String predealuserid;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getShiftid() {
		return shiftid;
	}
	public void setShiftid(Integer shiftid) {
		this.shiftid = shiftid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getSendtime() {
		return sendtime;
	}
	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}
	public String getSheetcompletelimit() {
		return sheetcompletelimit;
	}
	public void setSheetcompletelimit(String sheetcompletelimit) {
		this.sheetcompletelimit = sheetcompletelimit;
	}
	public String getMainalarmcleartime() {
		return mainalarmcleartime;
	}
	public void setMainalarmcleartime(String mainalarmcleartime) {
		this.mainalarmcleartime = mainalarmcleartime;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public String getTaskstatus() {
		return taskstatus;
	}
	public void setTaskstatus(String taskstatus) {
		this.taskstatus = taskstatus;
	}
	public String getTaskowner() {
		return taskowner;
	}
	public void setTaskowner(String taskowner) {
		this.taskowner = taskowner;
	}
	public String getPredealdept() {
		return predealdept;
	}
	public void setPredealdept(String predealdept) {
		this.predealdept = predealdept;
	}
	public String getPredealuserid() {
		return predealuserid;
	}
	public void setPredealuserid(String predealuserid) {
		this.predealuserid = predealuserid;
	}

	

}