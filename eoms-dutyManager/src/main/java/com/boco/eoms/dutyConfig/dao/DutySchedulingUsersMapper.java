package com.boco.eoms.dutyConfig.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.boco.eoms.dutyConfig.model.DutySchedulingUsers;


public interface DutySchedulingUsersMapper {
    public static final String DATA_SOURCE_NAME = "null";

	int deleteByPrimaryKey(Integer dutyId);
	
	/**
     *@see 根据配置id和值班时间删除数据 
     */
	int deleteByByConfigIdAndTime(Map<String,Object> conditionMap);
	
    int insert(DutySchedulingUsers record);

    int insertSelective(DutySchedulingUsers record);

    DutySchedulingUsers selectByPrimaryKey(Integer dutyId);

    int updateByPrimaryKeySelective(DutySchedulingUsers record);

    int updateByPrimaryKey(DutySchedulingUsers record);

    int insertBatch(List<Map<String,Object>> records);
    
    List<DutySchedulingUsers>  selectByUseridandDutyTime(Map<String,Object> conditionMap);
    
    /**
     *@see 根据本班次的条件查询上班次信息 
     */
    List<Map<String,Object>> selectLastDutyByduty(Map<String,Object> conditionMap);
    
    /**
     *@see 根据本班次的条件查询本班次信息 
     */
    List<Map<String,Object>> selectNowDutyByduty(Map<String,Object> conditionMap);
    /**
     *@see 根据本班次的条件查询下班次信息 
     */
    List<Map<String,Object>> selectNextDutyByduty(Map<String,Object> conditionMap);
    
    /**
     *@see 根据值班配置id和开始时间,结束时间查询排班数据 
     */
    List<Map<String,Object>> selectAllByConfigIdAndTime(Map<String,Object> conditionMap);
    
    int deleteByPrimaryKeys(DutySchedulingUsers record);
    
    List getDutySchedulingUserByConfigIdFlightIdDutyTime(Map<String,Object> conditionMap);
}