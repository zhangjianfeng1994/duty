package com.boco.eoms.shiftAlarmAct.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boco.eoms.shiftAlarmAct.dao.ShiftAlarmactMapper;
import com.boco.eoms.shiftAlarmAct.model.ShiftAlarmactModel;
import com.boco.eoms.shiftAlarmAct.service.ShiftAlarmactService;

@Service
public class ShiftAlarmactServiceImpl implements ShiftAlarmactService {

	@Resource
	ShiftAlarmactMapper shiftalarmactdao;
	
	@Override
	public List<ShiftAlarmactModel> selectTfaAlarm(String condition) {
		
		return shiftalarmactdao.selectTfaAlarm(condition);
	}

	@Override
	public List<ShiftAlarmactModel> selectDutyAlarm() {
		
		return shiftalarmactdao.selectDutyAlarm();
	}

	@Override
	public void insertByAlarmId(String ids) {
		if (!"".equals(ids)) {
			String[] id = ids.split(",");
			shiftalarmactdao.insertByAlarmId(id);
		}
	}

	@Override
	public void deleteBacthByPrimaryKey(String ids) {
		if (!"".equals(ids)) {
		String[] id = ids.split(",");
		shiftalarmactdao.deleteBacthByPrimaryKey(id);
		}
	}

	@Override
	public void deleteByAlarmClear() {
		shiftalarmactdao.deleteByAlarmClear();
	}
	
	@Transactional
	public List<ShiftAlarmactModel> deleteClearAndselectDutyAlarm(){
		deleteByAlarmClear();
		return selectDutyAlarm();
	}
}
