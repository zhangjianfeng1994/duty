package com.boco.eoms.system.dept.service;

import com.alibaba.fastjson.JSONArray;

/**
 * 部门dept service 接口
 * @author chenjianghe
 *
 */
public interface IDeptService {

	/**
	 * 根据父部门查询部门(监控出)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	public JSONArray getDeptByParentDeptId(String parentDictId);
}
