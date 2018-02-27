package com.boco.eoms.dutyInfo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.boco.eoms.dutyInfo.model.DutyInfo;

public interface DutyInfoMapper {
	
	/**
	 * 查询值班记录列表信息
	 * @param duty_info
	 * @return
	 */
	public List<DutyInfo> queryDutyInfoList(@Param("condition")String condition);
	
	/**
	 * 插入一条值班记录
	 * @param duty_info
	 * @return
	 */
	public void insertDutyInfo(DutyInfo dutyInfo);
	
	/**
	 * 更新一条值班记录
	 * @param duty_info
	 * @return
	 */
	public void updateDutyInfo(DutyInfo dutyInfo);

	/**
	 * 批量更新值班记录信息是否遗留
	 * @param duty_info
	 * @return
	 */
	public void updateDutyInfoRemain(Map<String, Object> params);  
	
	/**
	 * 删除一条值班记录
	 * @param duty_info
	 * @return
	 */
	public void deleteDutyInfo(int id);
	
	/**
	 * 批量插入值班记录
	 * @param duty_info
	 * @return
	 */
	public void insertDutyInfoList(List<DutyInfo> list);
	
}
