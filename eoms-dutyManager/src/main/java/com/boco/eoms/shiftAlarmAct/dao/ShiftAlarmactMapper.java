package com.boco.eoms.shiftAlarmAct.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.boco.eoms.shiftAlarmAct.model.ShiftAlarmactModel;

public interface ShiftAlarmactMapper {

	/** DUTY_SHIFT_ALARMACT */
	int deleteByPrimaryKey(String ALARM_ID);

	/** DUTY_SHIFT_ALARMACT */
	int insert(ShiftAlarmactModel record);

	/** DUTY_SHIFT_ALARMACT */
	int insertSelective(ShiftAlarmactModel record);

	/** DUTY_SHIFT_ALARMACT */
	ShiftAlarmactModel selectByPrimaryKey(String ALARM_ID);

	/** DUTY_SHIFT_ALARMACT */
	int updateByPrimaryKeySelective(ShiftAlarmactModel record);

	/** DUTY_SHIFT_ALARMACT */
	int updateByPrimaryKey(ShiftAlarmactModel record);
	
	/**
	 * @see 根据主键批量删除 
	 */
	int deleteBacthByPrimaryKey(String[]  ids);
	
	/**
	 * @see 查询接口告警(不包含值班告警已有的告警) 
	 */
	List<ShiftAlarmactModel> selectTfaAlarm(@Param("condition")String condition);
	/**
	 * @see 值班告警表删除接口已清除的告警
	 */
	int deleteByAlarmClear();
	
	/**
	 * @see 查询值班告警 
	 */
	List<ShiftAlarmactModel> selectDutyAlarm();
	/**
	 * @see 从接口告警选取的数据添加到值班告警表根据AlarmId
	 */
	int insertByAlarmId(String[]  ids);
}