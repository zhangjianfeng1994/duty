package com.boco.eoms.dutyConfig.service.imp;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.boco.eoms.dutyConfig.dao.DutyConfigModelMapper;
import com.boco.eoms.dutyConfig.model.DutyConfigModel;
import com.boco.eoms.dutyConfig.service.DutyConfigModelService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class DutyConfigModelServiceImpl implements DutyConfigModelService{
	@Resource
	private DutyConfigModelMapper dutyMapper;
	@Override
	public int save(DutyConfigModel dutyConfigModel) {
		return dutyMapper.insert(dutyConfigModel);
	}
	@Override
	public Integer selectMaxId() {
		return dutyMapper.selectMaxId();
	}
	@Override
	public HashMap selectAll(Integer startIndex, Integer length) {
		
		PageHelper.startPage(startIndex,length);
		List taskList = dutyMapper.selectAll();
		Page taskListCount = (Page)taskList;
		HashMap resultMap = new HashMap();
		resultMap.put("taskList", taskList);
		resultMap.put("total", taskListCount.getTotal());
		resultMap.put("pageIndex", startIndex);
		
		return resultMap;
	}
	
	@Override
	public DutyConfigModel selectByPrimaryKey(int dutyConfigId) {
		return dutyMapper.selectByPrimaryKey(dutyConfigId);
	}
	@Override
	public int update(DutyConfigModel dutyConfigModel) {
		return dutyMapper.updateByPrimaryKey(dutyConfigModel);
	}
	
	
	/**
	 * 查询所有dutyConfig
	 */
	public List showAll() {
		return dutyMapper.selectAll();
	}
	
	
	public int deleteConfigByid(String dutyConfigId) {
		
		
		return dutyMapper.deleteByPrimaryKey(dutyConfigId);
	}

	
}
