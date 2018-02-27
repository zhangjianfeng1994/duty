package com.boco.eoms.dutyConfig.model;

import java.io.Serializable;
import java.util.Date;

public class DutySchedulingUsers implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer dutyId;//值班id
	private String userid;//人员id
	private String username;//人员名称
	private Integer dutyFlightId;//班次id
	private Integer dutyConfigId;//值班配置id
	private Integer schedulingId;//排班id
	private Integer deleteIf;//是否删除
	private String reason;//调整原因
	private Date dutyTime;//日期
	private Integer headIf;//是否组长
	private String dutyHandoverType;//交接班配置类型
	private String remark;//备注
	
	
	
	public DutySchedulingUsers() {
	}



	public DutySchedulingUsers(Integer dutyId, String userid, String username,
			Integer dutyFlightId, Integer dutyConfigId, Integer schedulingId,
			Integer deleteIf, String reason, Date dutyTime, Integer headIf,
			String dutyHandoverType) {
		super();
		this.dutyId = dutyId;
		this.userid = userid;
		this.username = username;
		this.dutyFlightId = dutyFlightId;
		this.dutyConfigId = dutyConfigId;
		this.schedulingId = schedulingId;
		this.deleteIf = deleteIf;
		this.reason = reason;
		this.dutyTime = dutyTime;
		this.headIf = headIf;
		this.dutyHandoverType = dutyHandoverType;
	}



	public Integer getDutyId() {
		return dutyId;
	}



	public void setDutyId(Integer dutyId) {
		this.dutyId = dutyId;
	}



	public String getUserid() {
		return userid;
	}



	public void setUserid(String userid) {
		this.userid = userid;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public Integer getDutyFlightId() {
		return dutyFlightId;
	}



	public void setDutyFlightId(Integer dutyFlightId) {
		this.dutyFlightId = dutyFlightId;
	}



	public Integer getDutyConfigId() {
		return dutyConfigId;
	}



	public void setDutyConfigId(Integer dutyConfigId) {
		this.dutyConfigId = dutyConfigId;
	}



	public Integer getSchedulingId() {
		return schedulingId;
	}



	public void setSchedulingId(Integer schedulingId) {
		this.schedulingId = schedulingId;
	}



	public Integer getDeleteIf() {
		return deleteIf;
	}



	public void setDeleteIf(Integer deleteIf) {
		this.deleteIf = deleteIf;
	}



	public String getReason() {
		return reason;
	}



	public void setReason(String reason) {
		this.reason = reason;
	}



	public Date getDutyTime() {
		return dutyTime;
	}



	public void setDutyTime(Date dutyTime) {
		this.dutyTime = dutyTime;
	}



	public Integer getHeadIf() {
		return headIf;
	}



	public void setHeadIf(Integer headIf) {
		this.headIf = headIf;
	}



	public String getDutyHandoverType() {
		return dutyHandoverType;
	}



	public void setDutyHandoverType(String dutyHandoverType) {
		this.dutyHandoverType = dutyHandoverType;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
