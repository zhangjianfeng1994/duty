package com.boco.eoms.system.dept.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 部门dept Mapper接口
 * @author chenjianghe
 *
 */
public interface DeptMapper {

	/** 
	 	* 根据deptId查询deptname
		* @author chenjianghe
		* @version 版本1.0 
		* @param deptid
		* @return deptname
	    * @throws 无
	*/
	public String getDeptNameByDeptId(@Param("deptId") String deptId);
	
	/**
	 * 根据父字典id查询字典集合(JK 监控)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	@SuppressWarnings("rawtypes")
	public List<Map> getDeptByParentDeptId(@Param("parentDictId")String parentDictId);

}
