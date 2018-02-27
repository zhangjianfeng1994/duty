package com.boco.eoms.dutyChangeFlight.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.boco.eoms.dutyChangeFlight.model.DutyChangeFlight;

public interface DutyChangeFlightMapper {
	
	/**
	 * 查询本人换班记录列表信息
	 * @param duty_Change_Flight
	 * @return
	 */
	public List<DutyChangeFlight> queryDutyChangeFlightList(@Param("condition")String condition);
	
	/**
	 * 插入一条换班记录
	 * @param duty_Change_Flight
	 * @return
	 */
	public void insertDutyChangeFlight(DutyChangeFlight dutyChangeFlight);
	
	/**
	 * 更新换班状态：change_if
	 * @param duty_Change_Flight
	 * @return
	 */
	public void updateChangeIf(@Param("createFlightId")int createFlightId,@Param("dealFlightId")int dealFlightId,@Param("type")int type);
	
	/**
	 * 更新一条换班记录
	 * @param duty_Change_Flight
	 * @return
	 */
	public void updateDutyChangeFlight(DutyChangeFlight dutyChangeFlight);
	
	/**
	 * 完成换班，变更对应用户
	 * @param duty_Change_Flight
	 * @return
	 */
	public void changeFlightUser(@Param("firstUser")String firstUser,@Param("dutyTime")String dutyTime,@Param("lastUser")String lastUser,@Param("dutyFlightId")int dutyFlightId);

	/**
	 * 查询申请人班次开始时间
	 * @param duty_Change_Flight
	 * @return
	 */
	public List<HashMap<String, String>> searchCreateDutyStartTime(@Param("dutyFlightBeginTime")String dutyFlightBeginTime,@Param("userId")String userId);
	
	/**
	 * 查询接收人班次开始时间
	 * @param duty_Change_Flight
	 * @return
	 */
	public List<HashMap<String, String>> searchDealDutyStartTime(@Param("dutyFlightBeginTime")String dutyFlightBeginTime,@Param("userId")String userId,@Param("dutyConfigId")String dutyConfigId);
	
	/**
	 * 查询班次下的人
	 * @param duty_Change_Flight
	 * @return
	 */
	public List<HashMap<String, String>> searchDutyUser(@Param("dutyFlightId")String dutyFlightId,@Param("dutyTime")String dutyTime);
}
