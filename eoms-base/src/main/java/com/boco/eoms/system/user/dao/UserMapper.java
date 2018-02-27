package com.boco.eoms.system.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 用户User mapper接口
 * @author chenjianghe
 *
 */
public interface UserMapper {

	/** 
	 	* 根据userid查询username
		* @author chenjianghe
		* @version 版本1.0 
		* @param userid
		* @return username
	    * @throws 无
	*/
	public String getUserNameByUserId(@Param("userId") String userId);
	
	/**
	 * 根据mainid获取处理过此工单的userId
	 * @param mainId
	 * @return
	 */
	public List getUserIdByMainId(@Param("mainId") String mainId);
	
	/**
	 * 根据groupId获取userId
	 * @param mainId
	 * @return
	 */
	public List getUserByGroupId(@Param("groupId") String groupId);
	
	/**
	 * 根据用户id,大角色id,查询用户组id和用户组name
	 * @param groupId
	 * @return
	 */
	public List getGroupByUserIdAndRoleId(@Param("userId") String userId,@Param("roleId") String roleId);
	
	/**
	 * 根据groupId查询groupName
	 * @param groupId
	 * @return
	 */
	public String getGroupNameByGroupId(@Param("groupId") String groupId);
	
	/**
	 * 根据流程角色id查询角色id
	 * @param groupId
	 * @return
	 */
	public String getRoleIdByWorkFlow(@Param("workflow_role_id") String workflow_role_id);
}
