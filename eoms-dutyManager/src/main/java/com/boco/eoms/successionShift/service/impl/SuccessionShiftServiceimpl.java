package com.boco.eoms.successionShift.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;









import org.springframework.stereotype.Service;

import com.boco.eoms.successionShift.dao.SuccessionShiftMapper;
import com.boco.eoms.successionShift.model.SuccessionShiftModel;
import com.boco.eoms.successionShift.service.SuccessionShiftService;
import com.boco.eoms.successionShiftUser.model.SuccessionShiftUserModel;
import com.boco.eoms.successionShiftUser.service.SuccessionShiftUserService;
import com.boco.eoms.successionShiftUser.service.impl.SuccessionShiftUserServiceimpl;


@Service
public class SuccessionShiftServiceimpl implements SuccessionShiftService {
	
    @Resource
    private SuccessionShiftMapper successionshiftdao;
    
   
    
	public SuccessionShiftModel getModelById(int id) {
		return successionshiftdao.selectByPrimaryKey(id);
	}

	public void saveModel(SuccessionShiftModel successionshiftmodel) {
		successionshiftdao.insertSelective(successionshiftmodel);
	}

	public void updateModel(SuccessionShiftModel successionshiftmodel) {
		successionshiftdao.updateByPrimaryKeySelective(successionshiftmodel);
	}
	
	@Override
	public SuccessionShiftModel selectMaxShiftByDutyConfigId(int duty_config_id) {
		
		return successionshiftdao.selectMaxShiftByDutyConfigId(duty_config_id);
	}

	@Override
	public SuccessionShiftModel selectByDutySchedulingKey(int duty_flight_id,int duty_config_id, Date dutytime) {
		Map<String,Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("duty_config_id", duty_config_id);
		conditionMap.put("duty_flight_id", duty_flight_id);
		conditionMap.put("duty_time", dutytime);
		return successionshiftdao.selectByDutySchedulingKey(conditionMap);
	}

	@Override
	public List<Map<String, Object>> selectDutyCountByDutyConfig(
			int duty_config_id, String userid, String startTime, String endTime, String pivotcontionString) {
		 Map<String,Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("dutyConfigId", duty_config_id);
		conditionMap.put("beginTime", startTime);
		conditionMap.put("endTime", endTime);
		conditionMap.put("pivotContion", pivotcontionString);
		//999代表全部,条件中不加userid
		if (! "999".equals(userid)) {
			conditionMap.put("userid", userid);
		}
		return successionshiftdao.selectDutyCount(conditionMap);
	}

	@Override
	public void saveRemindUser(List<Map<String, Object>> list) {
		successionshiftdao.insertRemindByBatch(list);
	}

	@Override
	public void saveSmsMonitor(List<Map<String, Object>> list) {
		
		successionshiftdao.insertSmsMonitor(list);
		
	}

	@Override
	public void deleteSmsMonitor(String buiz_id) {
		
		successionshiftdao.deleteSmsMonitor(buiz_id);
	}

	
}
