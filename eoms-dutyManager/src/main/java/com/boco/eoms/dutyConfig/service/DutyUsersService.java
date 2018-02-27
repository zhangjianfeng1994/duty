package com.boco.eoms.dutyConfig.service;

import java.util.List;

import com.boco.eoms.dutyConfig.model.DutyConfigModel;
import com.boco.eoms.dutyConfig.model.DutyUsers;


public interface DutyUsersService {

	/**
	 * 根据主键查询值班人员
	 * @param dutyUsersId 值班人员id
	 * @return 
	 */
	public DutyUsers queryByPrimaryKey(Integer dutyUsersId);
	
	/**
	 * 根据userid查询值班人员
	 * @param dutyUsersId 值班人员id
	 * @return 
	 */
	public DutyUsers queryByUserid(String usersId);
	
	/**
	 * 根据主键删除值班人员
	 * @param dutyUsersId 值班人员id
	 * @return 
	 */
	public int removeDutyUser(Integer dutyUsersId);
	
	/**
	 * 新增值班人员
	 * @param dutyUsers
	 * @return
	 */
	public int addDutyUser(DutyUsers dutyUsers);
	
	/**
	 * 批量新增值班人员
	 * @param dutyUsers
	 * @return
	 */
	public int addDutyUsers(DutyUsers dutyUsers);
	
	/**
	 * 修改值班人员信息
	 * @param dutyUsers
	 * @return
	 */
	public int modifyDutyUser(DutyUsers dutyUsers);
	
	
	public Integer selectMaxId(int dutyUserId);
	
	public int save(DutyUsers dutyUsers) ;
	
	/**
	 * 根据duty_config_id 查询相关人员
	 * @param duty_config_id
	 * @return
	 */
	public List selectByDutyConfigId(int duty_config_id);

	
	public int update(DutyUsers dutyUsers);
	
	public int deleteByConfigId(int  duty_config_id);
	
	public List selectByUserId(String userId,int  duty_config_id);
	
	public List selectByUserName(String userName,int  duty_config_id);
	
	public int deleteByDeleteIf(int  duty_config_id);
}
