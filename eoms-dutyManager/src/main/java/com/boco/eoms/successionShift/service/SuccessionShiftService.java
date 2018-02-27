package com.boco.eoms.successionShift.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.boco.eoms.successionShift.model.SuccessionShiftModel;

public interface SuccessionShiftService {
	
	public SuccessionShiftModel getModelById(int id);
	
	public void saveModel(SuccessionShiftModel successionshiftmodel);//增加记录
	
	public void updateModel(SuccessionShiftModel successionshiftmodel);//更新记录
	
	public SuccessionShiftModel  selectByDutySchedulingKey(int duty_flight_id,int duty_config_id,Date dutytime);
	
	public SuccessionShiftModel selectMaxShiftByDutyConfigId(int duty_config_id);
	
	
	//查询值班统计(值班配置id,值班userid,开始时间,结束时间,pivot函数in中条件)
	public List<Map<String,Object>> selectDutyCountByDutyConfig(int duty_config_id,String userid,String startTime,String endTime, String pivotcontionString);
	
	//批量往通知用户表中插入数据
	public void saveRemindUser(List<Map<String,Object>> list);
	
	//批量往通知用户表中插入数据
	public void saveSmsMonitor(List<Map<String,Object>> list);
	
	//根据业务id删除短信通知数据
	public void deleteSmsMonitor(String buiz_id);
}
