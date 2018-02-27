package com.boco.eoms.shiftCentralfault.dao;

import java.util.List;
import java.util.Map;

import com.boco.eoms.shiftCentralfault.model.ShiftCentralfaultModel;

public interface ShiftCentralfaultMapper {

	/** DUTY_SHIFT_CENTRALFAULT */
	int deleteByPrimaryKey(Map<String,Object> contionMap);

	/** DUTY_SHIFT_CENTRALFAULT */
	int insert(ShiftCentralfaultModel record);

	/** DUTY_SHIFT_CENTRALFAULT */
	int insertSelective(ShiftCentralfaultModel record);

	/** DUTY_SHIFT_CENTRALFAULT */
	ShiftCentralfaultModel selectByPrimaryKey(String sheetid);

	/** DUTY_SHIFT_CENTRALFAULT */
	int updateByPrimaryKeySelective(ShiftCentralfaultModel record);

	/** DUTY_SHIFT_CENTRALFAULT */
	int updateByPrimaryKey(ShiftCentralfaultModel record);

	/**
	 *@see 根据shift_id查询关联故障表数据
	 * 
	 */
	List<ShiftCentralfaultModel> selectByShiftId(int shift_id);
	
	/**
	 *@see 查询本班次遗留的故障和接口数据的故障
	 * 
	 */
	List<ShiftCentralfaultModel> selectShiftFaultandCentralFaultByContionMap(Map<String,Object> contionMap);
	
	/**
	 *@see 查询关联的故障工单根据sheetid
	 * 
	 */
	 Map<String,Object> selectCentralFaultBySheetid(String sheetid);
	
	/**
	 *@see 批量插入从接口故障查询出来的数据
	 * 
	 */
	int insertBatch(Map<String,Object> contionMap);
	
	
	/**
	 *@see 交班时删除本班次遗留中已归档的故障工单
	 * 
	 */
	int deleteByStatus(int shift_id);

}