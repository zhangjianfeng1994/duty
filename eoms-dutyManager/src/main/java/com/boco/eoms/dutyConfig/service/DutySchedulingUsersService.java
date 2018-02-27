package com.boco.eoms.dutyConfig.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.boco.eoms.dutyConfig.model.DutySchedulingUsers;

public interface DutySchedulingUsersService {
	
	/**
	 * 获取SchedulingUsers by配置id班次id 值班时间
	 * @param dutyId
	 * @param FlightId
	 * @param dutyTime
	 * @return
	 */
	public List getDutySchedulingUserByConfigIdFlightIdDutyTime(int dutyId,int flightId,Date dutyTime);

	/**
	 * 新增排班信息
	 * @return
	 */
	public int saveDutySchedulingUser(DutySchedulingUsers dutySchedulingUsers);
	
	/**
	 * 批量新增排班信息
	 * @param dutySchedulingUsers
	 * @return
	 */
	public int saveBatchDutySchedulingUsers(List<Map<String, Object>> list);
	
	/**
	 * 根据主键删除排班信息
	 * @param dutyId
	 * @return
	 */
	public int removeDutySchedulingUsers(Integer dutyId);
	
	/**
	 * 根据 configId  flightId dutyTime 删除排班信息
	 * @param dutyId
	 * @return
	 */
	public int removeDutySchedulingUsersByCondition(DutySchedulingUsers dutySchedulingUsers);
	
	/**
	 * 查询排班信息
	 * @param dutySchedulingUsers
	 * @return
	 */
	public DutySchedulingUsers queryDutySchedulingUsers(DutySchedulingUsers dutySchedulingUsers);
	
	/**
	 * 修改排班信息
	 * @param dutySchedulingUsers
	 * @return
	 */
	public int modifyDutySchedulingUsers(DutySchedulingUsers dutySchedulingUsers);
	
	/**
	 * 根据人员,配置id,值班日期查询排班信息
	 * @param map: userid,dutyconfigid,dutytime
	 * @return DutySchedulingUsers
	 */
	public List<DutySchedulingUsers> selectByUseridandDutyTime(String userid,int duty_config_id,Date dutytime);
	
	/**
	 * 根据配置id,值班日期查询排班信息
	 * @param map: userid,dutyconfigid,dutytime
	 * @return DutySchedulingUsers
	 */
	public List<Map<String,Object>> selectByConfigIdandDutyTime(int duty_config_id,Date startTime,Date endTime);
	
	/**
	 * 根据班次id,配置id,值班日期查询上班次排班信息
	 * @param map: dutyflightid,dutyconfigid,dutytime
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectLastDutyByduty(int duty_flight_id,int duty_config_id,Date dutytime);
	
	/**
	 * 根据班次id,配置id,值班日期查询下班次排班信息
	 * @param map: dutyflightid,dutyconfigid,dutytime
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectNextDutyByduty(int duty_flight_id,int duty_config_id,Date dutytime);
	
	/**
	 * 根据班次id,配置id,值班日期查询本班次排班信息
	 * @param map: dutyflightid,dutyconfigid,dutytime
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectNowDutyByduty(int duty_flight_id,int duty_config_id,Date dutytime);
	
	/**
	 * 根据主键删除排班信息
	 * @param 删除条件,插入的数据
	 * @return
	 */
	public void removeAndSaveDutySchedulingUsers(Map<String, Object> condition,List<Map<String, Object>> dataList);
}
