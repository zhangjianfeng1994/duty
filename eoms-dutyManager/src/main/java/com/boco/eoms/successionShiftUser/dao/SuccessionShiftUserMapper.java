package com.boco.eoms.successionShiftUser.dao;

import java.util.List;
import java.util.Map;

import com.boco.eoms.successionShiftUser.model.SuccessionShiftUserModel;

public interface SuccessionShiftUserMapper {

	/** DUTY_SUCCESSION_SHIFT_USER */
	int deleteByPrimaryKey(int SHIFT_USER_ID);

	/** DUTY_SUCCESSION_SHIFT_USER */
	int insert(SuccessionShiftUserModel record);

	/** DUTY_SUCCESSION_SHIFT_USER */
	int insertSelective(SuccessionShiftUserModel record);

	/** DUTY_SUCCESSION_SHIFT_USER */
	SuccessionShiftUserModel selectByPrimaryKey(int SHIFT_USER_ID);

	/** DUTY_SUCCESSION_SHIFT_USER */
	int updateByPrimaryKeySelective(SuccessionShiftUserModel record);

	/** DUTY_SUCCESSION_SHIFT_USER */
	int updateByPrimaryKey(SuccessionShiftUserModel record);
	
	
	/** 根据人员和交接班id更新数据*/
	int updateByUserAndShiftIdSelective(SuccessionShiftUserModel record); 
	
	/** 查询接班人员id*/
	String selectSuccessionUseridByshiftId(int shift_id);
	
	/** 查询交班人员id*/
	String selectShiftUseridByshiftId(int shift_id);
	
	/** 根据shift_id查询交接班人员表多条数据(包含username)*/
	List<Map<String,Object>> selectModelAndUserNameByshiftId(int shift_id);
	
}