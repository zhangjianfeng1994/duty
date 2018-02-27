package com.boco.eoms.system.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.eoms.system.user.dao.UserMapper;
import com.boco.eoms.system.user.service.IUserService;

/**
 * 用户 user service 实现类
 * @author chenjianghe
 *
 */
@Service("userService")
public class UserServiceImpl implements IUserService{
	
	@Autowired
	private UserMapper userMapper;

	/**
	 * 根据子角色id查询用户
	 * @param subRoleId
	 * @return
	 */
	@Override
	public List getUsersBySubRoleid(String subRoleId) {
		// TODO Auto-generated method stub
		return new ArrayList();
	}

	/**
	 * 根据userid 查询用户手机号
	 * @param userId
	 * @return
	 */
	@Override
	public String getMobilesByUserId(String userId) {
		// TODO Auto-generated method stub
		return "";
	}

	/**
	 * 根据dept查询user
	 * @param dept
	 * @return
	 */
	@Override
	public List getUserBydeptids(String dept) {
		// TODO Auto-generated method stub
		return new ArrayList();
	}
	
	/**
	 * 根据mainId查询所有处理过此工单的user
	 * @param mainId
	 * @return
	 */
	public List getPreDealUserByMainId(String mainId) {
		List list = userMapper.getUserIdByMainId(mainId);
		return list;
	}

	/**
	 * 根据groupid查询用户
	 * @param groupId
	 * @return
	 */
	public List getUserByGroupId(String groupId) {
		// TODO Auto-generated method stub
		List list = userMapper.getUserByGroupId(groupId);
		return list;
	}
	
	/**
	 * 根据用户id,大角色id,查询用户组id和用户组name
	 * @param groupId
	 * @return
	 */
	public List getGroupByUserIdAndRoleId(String userId,String roleId){
		List list = userMapper.getGroupByUserIdAndRoleId(userId,roleId);
		return list;
	}
	
	/**
	 * 根据流程角色id获取角色id
	 * @param groupId
	 * @return
	 */
	public String getRoleIdByWorkFlow(String workflow_role_id){
		return userMapper.getRoleIdByWorkFlow(workflow_role_id);
	}
}
