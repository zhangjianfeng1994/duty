package com.boco.eoms.base.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;



public interface AuthorityMapper {
	
	/**
	 * 根据接口返回权限数据查询该用户所能访问菜单
	 * @param map
	 */
	public List getMenuByAuthority(Map map);
	
	
	/**
	 * 根据条件获取user信息
	 * @param map
	 * @return
	 */
	public List getUserByCndition(Map map);
	
	/**
	 * 根据 模块id和权限数组查询当前用户拥有的菜单项
	 * @param parentOperId 模块Id
	 * @param operIds 权限数组
	 * @return List 菜单集合
	 * */
	public List getMenuByIteration(@Param("parentOperId")String parentOperId,@Param("operIds")String operIds);
	
	/**
	 * 根据条件获取dept信息
	 * @param map
	 * @return
	 */
	public List getDeptByCndition(Map map);
	
	/**
	 * 获取所有用户组
	 * @return
	 */
	public List getAllGroup();
	
}
