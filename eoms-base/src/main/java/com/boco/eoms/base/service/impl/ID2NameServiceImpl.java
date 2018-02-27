package com.boco.eoms.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.eoms.base.service.IID2NameService;
import com.boco.eoms.base.util.StaticMethod;
import com.boco.eoms.system.dept.dao.DeptMapper;
import com.boco.eoms.system.dict.dao.DictMapper;
import com.boco.eoms.system.user.dao.UserMapper;


/**
 * id2nameSerivce
 * @author chenjianghe
 *
 */
@Service("ID2NameService")
public class ID2NameServiceImpl implements IID2NameService{

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private DictMapper dictMapper;
	
	@Autowired
	private DeptMapper deptMapper;
	
	/**
		 * id转name
		 * @param id 一般为表中的主键
		 * @param type 类型
		 * @param parentDictId 父id
		 * @return 返回主键对应的name(自定义)
		 * @throws BusinessException
		 * @since 0.1
		 */
	@Override
	public String id2Name(String id, String type,String parentDictId) {
		// TODO Auto-generated method stub
		
		String name = "";
		
		//人员id2name
		if("user".equals(type)){
			name = StaticMethod.nullObject2String(userMapper.getUserNameByUserId(id));
		}
		//部门id2name
		if("dept".equals(type)){
			//name = StaticMethod.nullObject2String(deptMapper.getDeptNameByDeptId(id));
			name = "开发部";
		}
		//用户组id2nme
		if("subrole".equals(type)){
			name = StaticMethod.nullObject2String(userMapper.getGroupNameByGroupId(id));
		}
		
		//操作对象id2name
		if("userOrSubrole".equals(type)){
			name = StaticMethod.nullObject2String(userMapper.getGroupNameByGroupId(id));
			if("".equals(name)){
				name = StaticMethod.nullObject2String(userMapper.getUserNameByUserId(id));
			}
		}
		
		//监控字典id2name
		if("dictJK".equals(type)){
			name = StaticMethod.nullObject2String(dictMapper.getDictNameFromJK(id,parentDictId));
		}
		
		//监控字典id2name
		if("dictAlarm".equals(type)){
			name = StaticMethod.nullObject2String(dictMapper.getDictNameFromAlarm(id, parentDictId));
		}
		
		//监控字典id2name
		if("dictVendor".equals(type)){
			name = StaticMethod.nullObject2String(dictMapper.getDictNameFromVendor(id, parentDictId));
		}
		
		//监控字典id2name
				if("station".equals(type)){
					name = StaticMethod.nullObject2String(dictMapper.getDictNameFromStation(id));
				}
		
		///
		return name;
	}

	/**
	 * id转name
	 * @param id 一般为表中的主键
	 * @param type 类型
	 * @return 返回主键对应的name(自定义)
	 * @throws BusinessException
	 * @since 0.1
	 */
	@Override
	public String id2Name(String id, String type) {
		// TODO Auto-generated method stub
		String name = "";
		
		//人员id2name
		if("user".equals(type)){
			name = StaticMethod.nullObject2String(userMapper.getUserNameByUserId(id));
		}
		//部门id2name
		if("dept".equals(type)){
			name = StaticMethod.nullObject2String(deptMapper.getDeptNameByDeptId(id));
		}
		//用户组id2nme
		if("subrole".equals(type)){
			name = StaticMethod.nullObject2String(userMapper.getGroupNameByGroupId(id));
		}
		
		//操作对象id2name
				if("userOrSubrole".equals(type)){
					name = StaticMethod.nullObject2String(userMapper.getGroupNameByGroupId(id));
					if("".equals(name)){
						name = StaticMethod.nullObject2String(userMapper.getUserNameByUserId(id));
					}
				}
		//eoms字典id2name
		if("dict".equals(type)){
			name = StaticMethod.nullObject2String(dictMapper.getDictNameByDictId(id));
		}
		
		//监控字典id2name
		if("station".equals(type)){
			name = StaticMethod.nullObject2String(dictMapper.getDictNameFromStation(id));
		}
		return name;
	}
}