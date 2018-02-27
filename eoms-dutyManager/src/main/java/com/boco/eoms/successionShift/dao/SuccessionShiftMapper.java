package com.boco.eoms.successionShift.dao;

import java.util.List;
import java.util.Map;

import com.boco.eoms.successionShift.model.SuccessionShiftModel;

public interface SuccessionShiftMapper {

	/** DUTY_SUCCESSION_SHIFT */
	int deleteByPrimaryKey(int SHIFT_ID);

	/** DUTY_SUCCESSION_SHIFT */
	int insert(SuccessionShiftModel record);

	/** DUTY_SUCCESSION_SHIFT */
	int insertSelective(SuccessionShiftModel record);

	/** DUTY_SUCCESSION_SHIFT */
	SuccessionShiftModel selectByPrimaryKey(int SHIFT_ID);

	/** DUTY_SUCCESSION_SHIFT */
	int updateByPrimaryKeySelective(SuccessionShiftModel record);

	/** DUTY_SUCCESSION_SHIFT */
	int updateByPrimaryKey(SuccessionShiftModel record);
	
	
	SuccessionShiftModel selectByDutySchedulingKey(Map<String,Object> conditionMap);

	SuccessionShiftModel selectMaxShiftByDutyConfigId(int config_id);
	
	/** 查询值班统计 */
	List<Map<String,Object>> selectDutyCount(Map<String,Object> conditionMap);
	
	/** 批量插入通知表数据 */
	int insertRemindByBatch(List<Map<String,Object>> list);
	
	/** 批量插入短信通知表数据 */
	int insertSmsMonitor(List<Map<String,Object>> list);
	
	/** 删除短信通知表数据 */
	int deleteSmsMonitor(String buiz_id);
}