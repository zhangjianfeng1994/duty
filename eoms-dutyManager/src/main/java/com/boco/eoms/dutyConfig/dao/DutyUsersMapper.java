package com.boco.eoms.dutyConfig.dao;

import java.util.List;
import java.util.Map;

import com.boco.eoms.dutyConfig.model.DutyUsers;

public interface DutyUsersMapper {

	public static final String DATA_SOURCE_NAME = "null";
	
	public DutyUsers selectByPrimaryKey(Integer dutyUsersId);
	
	public DutyUsers selectByUserid(String uersId);
	
	public int deleteByPrimaryKey(Integer dutyUsersId);
	
	public int insert(DutyUsers dutyUsers);
	
	public int insertSelective(DutyUsers dutyUsers);
	
	public int updateByPrimaryKeySelective(DutyUsers dutyUsers);
	
	public int updateByPrimaryKey(DutyUsers dutyUsers);
	
	public int insertBatch(List<DutyUsers> dutyUsersList);
	
	public Integer selectMaxId();
	
	public List selectByDutyConfigId(int duty_config_id);
	
	public int deleteByConfigId(int duty_config_id);
	
	public List selectByUserId(Map map);
	
	public int deleteByDeleteIf(int duty_config_id);
}
