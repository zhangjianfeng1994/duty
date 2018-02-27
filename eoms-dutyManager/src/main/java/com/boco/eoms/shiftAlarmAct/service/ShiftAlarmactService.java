package com.boco.eoms.shiftAlarmAct.service;

import java.util.List;

import com.boco.eoms.shiftAlarmAct.model.ShiftAlarmactModel;


public interface ShiftAlarmactService {
	
	/**
	 * @see 查询接口告警(不包含值班告警已有的告警) 
	 */
	public List<ShiftAlarmactModel> selectTfaAlarm(String condition); 
	
	/**
	 * @see 查询值班告警 
	 */
	public List<ShiftAlarmactModel> selectDutyAlarm();
	
	/**
	 * @see 从接口告警选取的数据添加到值班告警表根据AlarmId
	 */
	public void insertByAlarmId(String  ids);
	
	/**
	 * @see 根据主键批量删除 
	 * 
	 */
	public void deleteBacthByPrimaryKey(String  ids);
	
	/**
	 * @see 值班告警表删除接口已清除的告警
	 */
	public void deleteByAlarmClear();
	
	/**
	 * @see 查询值班告警,每次查询之前都要先删除接口告警已清除的告警
	 */
	public List<ShiftAlarmactModel> deleteClearAndselectDutyAlarm();
}
