package com.boco.eoms.dutyConfig.service;

import java.util.List;

import com.boco.eoms.dutyConfig.model.DutyFlightConfig;



public interface DutyFlightConfigService {
	public  int save(DutyFlightConfig dutyFlightConfig);
	public Integer selectMaxId(int dutyConfigId);
	
	public DutyFlightConfig selectByPrimaryKey(int dutyFlightId);
	
	public List selectByDutyConfigId(int duty_config_id);
	
	public int update(DutyFlightConfig dutyFlightConfig);
	
	public int deleteByid(int dutyFlightId);
	
	public int deleteByDutyId(int duty_config_id);
}
