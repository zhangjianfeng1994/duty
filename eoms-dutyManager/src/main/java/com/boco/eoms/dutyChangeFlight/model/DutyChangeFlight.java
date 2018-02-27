package com.boco.eoms.dutyChangeFlight.model;

import java.util.Date;

import org.springframework.stereotype.Service;

/**
 * 值班记录model
 * @author zhangshijie
 *
 */
public class DutyChangeFlight {
	private int id;							//这个就是id
	
	private int createFlightId;				//申请人班次ID
	
	private String createUser;				//申请人
	
	private String dealUser;				//接收人
	
	private String auditUser;				//负责人
	
	private Date createDutyTime;			//申请人值班日期
	
	private String applyResult;				//申请原因
	
	private String status;					//状态（接收人待回复、接收人不同意、审核人审核中、审核人同意、审核人不同意）
	
	private int sheetStatus;				//工单状态（接收人环节：0，审核人环节：1，申请人环节：2，归档：3）
	
	private int dealFlightId;				//接收人班次ID
	
	private Date dealDutyTime;				//接收人值班日期
	
	private Date createTime;				//申请时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCreateFlightId() {
		return createFlightId;
	}

	public void setCreateFlightId(int createFlightId) {
		this.createFlightId = createFlightId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getDealUser() {
		return dealUser;
	}

	public void setDealUser(String dealUser) {
		this.dealUser = dealUser;
	}

	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public Date getCreateDutyTime() {
		return createDutyTime;
	}

	public void setCreateDutyTime(Date createDutyTime) {
		this.createDutyTime = createDutyTime;
	}

	public String getApplyResult() {
		return applyResult;
	}

	public void setApplyResult(String applyResult) {
		this.applyResult = applyResult;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSheetStatus() {
		return sheetStatus;
	}

	public void setSheetStatus(int sheetStatus) {
		this.sheetStatus = sheetStatus;
	}

	public int getDealFlightId() {
		return dealFlightId;
	}

	public void setDealFlightId(int dealFlightId) {
		this.dealFlightId = dealFlightId;
	}

	public Date getDealDutyTime() {
		return dealDutyTime;
	}

	public void setDealDutyTime(Date dealDutyTime) {
		this.dealDutyTime = dealDutyTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
