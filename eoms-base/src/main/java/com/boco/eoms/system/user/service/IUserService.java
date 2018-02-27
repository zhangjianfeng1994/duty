package com.boco.eoms.system.user.service;

import java.util.List;

/**
 * 用户 service 接口
 * @author chenjianghe
 *
 */
public interface IUserService {

	/**
	 * 根据子角色id查询用户
	 * @param subRoleId
	 * @return
	 */
	public List getUsersBySubRoleid(String subRoleId);
	
	/**
	 * 根据userid 查询用户手机号
	 * @param userId
	 * @return
	 */
	public String getMobilesByUserId(String userId);
	
	/**
	 * 根据dept查询user
	 * @param dept
	 * @return
	 */
	public List getUserBydeptids(String dept);
	
	/**
	 * 根据mainId查询所有处理过此工单的user
	 * @param mainId
	 * @return
	 */
	public List getPreDealUserByMainId(String mainId);
	
	/**
	 * 根据groupid查询用户
	 * @param groupId
	 * @return
	 */
	public List getUserByGroupId(String groupId);
	
	/**
	 * 根据用户id,大角色id,查询用户组id和用户组name
	 * @param groupId
	 * @return
	 */
	public List getGroupByUserIdAndRoleId(String userId,String roleId);
	
	/**
	 * 根据流程角色id获取角色id
	 * @param groupId
	 * @return
	 */
	public String getRoleIdByWorkFlow(String workflow_role_id);
}
