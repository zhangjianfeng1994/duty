package com.boco.eoms.system.dept.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.boco.eoms.system.dept.dao.DeptMapper;
import com.boco.eoms.system.dept.service.IDeptService;

/**
 * 部门dept service实现类
 * @author chenjianghe
 *
 */
@Service("deptService")
public class DeptServiceImpl implements IDeptService{

	@Autowired
	public DeptMapper deptMapper;
	/**
	 * 根据父部门查询部门(监控出)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	@Override
	public JSONArray getDeptByParentDeptId(String parentDictId) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				List<Map> dictList = deptMapper.getDeptByParentDeptId(parentDictId);
				JSONArray array = new JSONArray();
				//JSON.toJSONString(object)
				if(dictList!=null&&dictList.size()>0){
					dictList.forEach((Map map) -> {
						array.add(map);
					});
				}
				return array;
	}
}
