package com.boco.eoms.dutyConfig.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.boco.eoms.dutyConfig.dao.DutyFlightConfigMapper;
import com.boco.eoms.dutyConfig.model.DutyFlightConfig;
import com.boco.eoms.dutyConfig.service.DutyFlightConfigService;

@Service
public class DutyFlightConfigServiceImpl implements DutyFlightConfigService{
	
	@Resource
	private DutyFlightConfigMapper dutyFlightMapper;
	
	@Override
	public int save(DutyFlightConfig dutyFlightConfig) {
		
		return dutyFlightMapper.insert(dutyFlightConfig);
	}

	@Override
	public Integer selectMaxId(int dutyConfigId) {
		
		return dutyFlightMapper.selectMaxId(dutyConfigId);
	}
	@Override
	public DutyFlightConfig selectByPrimaryKey(int dutyFlightId) {
		
		return dutyFlightMapper.selectByPrimaryKey(dutyFlightId);
	}

	@Override
	public List selectByDutyConfigId(int duty_config_id) {
		
		return dutyFlightMapper.selectByDutyConfigId(duty_config_id);
	}

	@Override
	public int update(DutyFlightConfig dutyFlightConfig) {
		
		return dutyFlightMapper.updateByPrimaryKey(dutyFlightConfig);
	}

	@Override
	public int deleteByid(int dutyFlightId) {
		return dutyFlightMapper.deleteByPrimaryKey(dutyFlightId);
	}

	public int deleteByDutyId(int duty_config_id) {
		
		return dutyFlightMapper.deleteByDutyId(duty_config_id);
	}

}
