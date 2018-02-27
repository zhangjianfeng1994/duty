package com.boco.eoms.dutyConfig.model;

import java.io.Serializable;
import java.util.Date;

public class DutyUsers implements Serializable{
	private Integer dutyUsersId;// 值班人员id
	private String userId;// 人员id
	private String userName;// 人员姓名
	private Integer dutyConfigId;// 值班配置id
	private Date createTime;// 修改时间
	private String deleteIf;// 删除标记

	public DutyUsers() {

	}

	public Integer getDutyUsersId() {
		return dutyUsersId;
	}

	public void setDutyUsersId(Integer dutyUsersId) {
		this.dutyUsersId = dutyUsersId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getDutyConfigId() {
		return dutyConfigId;
	}

	public void setDutyConfigId(Integer dutyConfigId) {
		this.dutyConfigId = dutyConfigId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDeleteIf() {
		return deleteIf;
	}

	public void setDeleteIf(String deleteIf) {
		this.deleteIf = deleteIf;
	}


}
