package com.boco.eoms.dutyInfo.model;

import java.util.Date;

import org.springframework.stereotype.Service;

/**
 * 值班记录model
 * @author zhangshijie
 *
 */
@Service("dutyInfo")
public class DutyInfo {
	private int id;							//这就是个id，数据库叫做duty_log_id
	
	
	private Date createTime;				//创建日期
	
	
	private String createUser;				//记录人
	
	private String dealUser;				//处理人
	
	private Date dealTime;				//处理完成时间
	
	private String mainSubCenter;			//分中心

	private String mainStations;			//台站
	
	private String mainFaultSubSystem;		//子系统
	
	private String mainFaultEquipmentType;	//设备类型
	
	private String mainFaultType;			//故障类型
	
	private String mainFaultDesc;			//故障描述
	
	private String processingProcedure;		//处理过程
	
	private String resolveIf;				//是否解决
	
	private String attentionIf;				//特别注意

	private Integer remainIf;				//是否遗留
	
	private String note;				//备注

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}
	
	public String getMainSubCenter() {
		return mainSubCenter;
	}

	public void setMainSubCenter(String mainSubCenter) {
		this.mainSubCenter = mainSubCenter;
	}

	public String getMainStations() {
		return mainStations;
	}

	public void setMainStations(String mainStations) {
		this.mainStations = mainStations;
	}

	public String getMainFaultSubSystem() {
		return mainFaultSubSystem;
	}

	public void setMainFaultSubSystem(String mainFaultSubSystem) {
		this.mainFaultSubSystem = mainFaultSubSystem;
	}

	public String getMainFaultEquipmentType() {
		return mainFaultEquipmentType;
	}

	public void setMainFaultEquipmentType(String mainFaultEquipmentType) {
		this.mainFaultEquipmentType = mainFaultEquipmentType;
	}

	public String getMainFaultType() {
		return mainFaultType;
	}

	public void setMainFaultType(String mainFaultType) {
		this.mainFaultType = mainFaultType;
	}

	public String getMainFaultDesc() {
		return mainFaultDesc;
	}

	public void setMainFaultDesc(String mainFaultDesc) {
		this.mainFaultDesc = mainFaultDesc;
	}

	public String getProcessingProcedure() {
		return processingProcedure;
	}

	public void setProcessingProcedure(String processingProcedure) {
		this.processingProcedure = processingProcedure;
	}

	public String getResolveIf() {
		return resolveIf;
	}

	public void setResolveIf(String resolveIf) {
		this.resolveIf = resolveIf;
	}

	public String getAttentionIf() {
		return attentionIf;
	}

	public void setAttentionIf(String attentionIf) {
		this.attentionIf = attentionIf;
	}

	public Integer getRemainIf() {
		return remainIf;
	}

	public void setRemainIf(Integer remainIf) {
		this.remainIf = remainIf;
	}
	
}
