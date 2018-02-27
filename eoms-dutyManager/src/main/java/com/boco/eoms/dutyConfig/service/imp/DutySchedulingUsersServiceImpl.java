package com.boco.eoms.dutyConfig.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boco.eoms.dutyConfig.dao.DutySchedulingUsersMapper;
import com.boco.eoms.dutyConfig.model.DutySchedulingUsers;
import com.boco.eoms.dutyConfig.service.DutySchedulingUsersService;

@Service
public class DutySchedulingUsersServiceImpl implements
		DutySchedulingUsersService {

	@Autowired
	private DutySchedulingUsersMapper dutySchedulingUsersMapper;
	
	@Override
	public int saveDutySchedulingUser(DutySchedulingUsers dutySchedulingUsers) {
		try{
			int result = this.dutySchedulingUsersMapper.insertSelective(dutySchedulingUsers);
			return result;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public int saveBatchDutySchedulingUsers(List<Map<String, Object>> list) {
		try{
			int result = this.dutySchedulingUsersMapper.insertBatch(list);
			return result;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public int removeDutySchedulingUsers(Integer dutyId) {
		try{
			int result = this.dutySchedulingUsersMapper.deleteByPrimaryKey(dutyId);
			return result;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public DutySchedulingUsers queryDutySchedulingUsers(
			DutySchedulingUsers dutySchedulingUsers) {
		try{
			DutySchedulingUsers dslu =
					this.dutySchedulingUsersMapper.selectByPrimaryKey(dutySchedulingUsers.getDutyId());
			return dslu;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public int modifyDutySchedulingUsers(DutySchedulingUsers dutySchedulingUsers) {
		try{
			int result = this.dutySchedulingUsersMapper.updateByPrimaryKeySelective(dutySchedulingUsers);
			return result;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<DutySchedulingUsers> selectByUseridandDutyTime(
			String userid,int duty_config_id,Date dutytime) {
		Map<String,Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("userid", userid);
		conditionMap.put("dutyconfigid", duty_config_id);
		conditionMap.put("dutytime", dutytime);
		return dutySchedulingUsersMapper.selectByUseridandDutyTime(conditionMap);
	}

	@Override
	public List<Map<String, Object>> selectLastDutyByduty(int duty_flight_id,int duty_config_id, Date dutytime) {
		Map<String,Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("dutyflightid", duty_flight_id);
		conditionMap.put("dutyconfigid", duty_config_id);
		conditionMap.put("dutytime", dutytime);
		return dutySchedulingUsersMapper.selectLastDutyByduty(conditionMap);
	}

	@Override
	public List<Map<String, Object>> selectNextDutyByduty(int duty_flight_id,int duty_config_id, Date dutytime) {
		Map<String,Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("dutyflightid", duty_flight_id);
		conditionMap.put("dutyconfigid", duty_config_id);
		conditionMap.put("dutytime", dutytime);
		return dutySchedulingUsersMapper.selectNextDutyByduty(conditionMap);
	}

	@Override
	public List<Map<String, Object>> selectNowDutyByduty(int duty_flight_id,int duty_config_id, Date dutytime) {
		Map<String,Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("dutyflightid", duty_flight_id);
		conditionMap.put("dutyconfigid", duty_config_id);
		conditionMap.put("dutytime", dutytime);
		return dutySchedulingUsersMapper.selectNowDutyByduty(conditionMap);
	}
	
	
	public int removeDutySchedulingUsersByCondition(DutySchedulingUsers dutySchedulingUsers) {
		return dutySchedulingUsersMapper.deleteByPrimaryKeys(dutySchedulingUsers);
	}
	
	
	public List getDutySchedulingUserByConfigIdFlightIdDutyTime(int dutyId,
			int flightId, Date dutyTime) {
		Map map = new HashMap();
		map.put("dutyconfigid", dutyId);
		map.put("dutyflightid", flightId);
		map.put("dutytime", dutyTime);
		
		return dutySchedulingUsersMapper.getDutySchedulingUserByConfigIdFlightIdDutyTime(map);
	}

	@Override
	public List<Map<String, Object>> selectByConfigIdandDutyTime(
			int duty_config_id, Date startTime, Date endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dutyconfigid", duty_config_id);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return dutySchedulingUsersMapper.selectAllByConfigIdAndTime(map);
	}

	@Override
	@Transactional
	public void removeAndSaveDutySchedulingUsers(Map<String, Object> condition,
			List<Map<String, Object>> dataList) {
		dutySchedulingUsersMapper.deleteByByConfigIdAndTime(condition);
		dutySchedulingUsersMapper.insertBatch(dataList);
	}

}
