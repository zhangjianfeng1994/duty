package com.boco.eoms.shiftCentralfault.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.boco.eoms.commons.TimeUtil;
import com.boco.eoms.shiftCentralfault.dao.ShiftCentralfaultMapper;
import com.boco.eoms.shiftCentralfault.model.ShiftCentralfaultModel;
import com.boco.eoms.shiftCentralfault.service.ShiftCentralfaultService;

@Service
public class ShiftCentralfaultServiceImpl implements ShiftCentralfaultService {

	@Resource
	private ShiftCentralfaultMapper shiftcentralfaultdao;

	
	@Override
	public ShiftCentralfaultModel getModelById(String id) {
		
		return shiftcentralfaultdao.selectByPrimaryKey(id);
		
	}
	
	
	@Override
	public void saveModel(ShiftCentralfaultModel shiftcentralfaultmodel) {
		
		shiftcentralfaultdao.insertSelective(shiftcentralfaultmodel);
	}

	@Override
	public void updateModel(ShiftCentralfaultModel shiftcentralfaultmodel) {
		
		shiftcentralfaultdao.updateByPrimaryKeySelective(shiftcentralfaultmodel);
	}
	
	/**
	 *@see  根据shift_id查询数据
	 */
	@Override
	public List<ShiftCentralfaultModel> selectByShiftId(int shift_id) {
		
		return shiftcentralfaultdao.selectByShiftId(shift_id);
	}
	
	/**
	 *@see  查询本班次遗留的故障和接口数据的故障
	 * 条件:shift_id,开始时间,结束时间
	 */
	@Override
	public List<ShiftCentralfaultModel> selectAllFaultByCondition(int last_shift_id,int shift_id,String beginTime,String endTime) {
		 Map<String, Object> map = new HashMap<String, Object>();
		 //endTime = TimeUtil.getCurrentTime();
		 map.put("LAST_SHIFT_ID", last_shift_id);
		 map.put("SHIFT_ID", shift_id);
		 map.put("BEGINTIME", beginTime);
		 map.put("ENDTIME", endTime);
		 return shiftcentralfaultdao.selectShiftFaultandCentralFaultByContionMap(map);
	}
	
	/**
	 *@see  查询本班次遗留的故障和接口数据的故障
	 * 条件:shift_id,开始时间,结束时间
	 */
	@Override
	public void saveBatchFormCentralfault(String[] sheetids,int shift_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SHIFT_ID", shift_id);
		map.put("SHEETIDS", sheetids);
		 shiftcentralfaultdao.insertBatch(map);
	}
	
	@Override
	public void deleteModel(String sheetid, int shift_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SHIFT_ID", shift_id);
		map.put("SHEETID", sheetid);
		shiftcentralfaultdao.deleteByPrimaryKey(map);
	}


	@Override
	public void deleteBatchModel(int shift_id) {
		shiftcentralfaultdao.deleteByStatus(shift_id);
	}


	@Override
	public Map<String, Object> selectCentralFaultBySheetid(String sheetid) {
		 return shiftcentralfaultdao.selectCentralFaultBySheetid(sheetid);
	}
	

	
	

}
