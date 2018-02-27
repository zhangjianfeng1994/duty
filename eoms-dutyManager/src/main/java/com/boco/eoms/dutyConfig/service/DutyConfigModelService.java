package com.boco.eoms.dutyConfig.service;


import java.util.HashMap;
import java.util.List;

import com.boco.eoms.dutyConfig.model.DutyConfigModel;


public interface DutyConfigModelService {
	public int save(DutyConfigModel dutyConfigModel);
	public Integer selectMaxId();
	public HashMap selectAll(Integer startIndex, Integer length);
	public DutyConfigModel selectByPrimaryKey(int dutyConfigId);
	
	
	public int update(DutyConfigModel dutyConfigModel);
	
	public List showAll();
	
	public int deleteConfigByid(String dutyConfigId);
}
