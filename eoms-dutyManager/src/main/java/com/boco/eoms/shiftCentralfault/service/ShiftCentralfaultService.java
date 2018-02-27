package com.boco.eoms.shiftCentralfault.service;

import java.util.List;
import java.util.Map;



import com.boco.eoms.shiftCentralfault.model.ShiftCentralfaultModel;

public interface ShiftCentralfaultService {
	
	public ShiftCentralfaultModel getModelById(String id);
	
	public List<ShiftCentralfaultModel> selectByShiftId(int shift_id);
	
	public List<ShiftCentralfaultModel> selectAllFaultByCondition(int last_shift_id,int shift_id,String beginTime,String endTime);
	
	/**
	 *@see 查询故障工单根据sheetid
	 * 
	 */
	Map<String,Object> selectCentralFaultBySheetid(String sheetid);
	
	/**
	 *@see 批量插入从接口故障查询出来的数据
	 * 
	 */
	public void saveBatchFormCentralfault(String[] sheetids,int shift_id);
	
	
	public void saveModel(ShiftCentralfaultModel shiftcentralfaultmodel);//增加记录
	
	public void updateModel(ShiftCentralfaultModel shiftcentralfaultmodel);//更新记录
	
	public void deleteModel(String sheetid,int shift_id );//删除记录
	
	/**
	 *@see 批量删除本班次故障遗留中已归档的数据
	 * 
	 */
	public void deleteBatchModel(int shift_id );//删除已归档的记录
}
