package com.boco.eoms.base.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface IAuthorityService {
	
	/**
	 * 根据userid获取菜单权限
	 * @param url
	 * @return
	 */
	public JSONObject getAuthorityByUserId(String userId,int appId,int operId);
	
	/**
	 * 根据userid 获取大角色
	 * @param url
	 * @return
	 */
	public JSONArray getRoleByUserId(String userId);
	
	/**
	 * 根据userid获取用户信息
	 * @param url
	 * @return
	 */
	public JSONObject getUserByUserId(String userId);
	
	/**
	 * 获取地、市、区域信息
	 * @param url
	 * @return
	 */
	public JSONArray getZone();
	
	
	/**
	 * 根据区域id获取 区域下大角色
	 * @return
	 */
	public JSONArray getRoleByZoneId(String zoneId); 
	
	/**
	 * 根据区域id获取区域下用户组
	 * @param zoneId
	 * @return
	 */
	public JSONArray getGroupByZoneId(String zoneId);
	
	
	/**
	 * 获取所有用户组
	 * @return
	 */
	public JSONObject getAllGroup();
	
	/**
	 * 根据roleid获取用户组
	 * @param roleId
	 * @return
	 */
	public JSONObject getGroupByRoleId(String roleId);
	
	/**
	 * 根据groupid获取用户
	 * @param roleId
	 * @return
	 */
	public JSONObject getUsersByGroupId(String groupid);
	
	/**
	 * 根据deptid获取用户
	 * @param roleId
	 * @return
	 */
	public List getUsersByDeptId(String deptid);
	
	/**
	 * 取得所有部门
	 * @param groupid
	 * @return
	 */
	public JSONObject getAllDept();
	
	
	
	/**
	 * 根据parentdeptid获取dept信息
	 */
	public List getDeptByParentDeptId(String parentDeptId);
	
}
