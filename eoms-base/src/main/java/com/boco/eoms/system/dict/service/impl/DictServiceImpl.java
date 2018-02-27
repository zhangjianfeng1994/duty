package com.boco.eoms.system.dict.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.boco.eoms.system.dict.dao.DictMapper;
import com.boco.eoms.system.dict.service.IDictService;

/**
 * 字典值 dict service 实现类
 * @author chenjianghe
 *
 */
@Service("dictService")
public class DictServiceImpl implements IDictService{
	
	@Autowired
	public DictMapper dictMapper;
	
	@Override
	@SuppressWarnings("rawtypes")
	public JSONArray getDictInfoByParentDictId(String parentDictId) {
		List<Map> dictList = dictMapper.getDictInfoByParentDictId(parentDictId);
		JSONArray array = new JSONArray();
		//JSON.toJSONString(object)
		if(dictList!=null&&dictList.size()>0){
			dictList.forEach((Map map) -> {
				array.add(map);
			});
		}
		return array;
	}
	
	/**
	 * 根据字典标识查询字典集合
	 * @author zsj
	 * @version 版本1.0 
	 * @param dictKey 字典关键字
	 * @param tableName 表名
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	@Override
	@SuppressWarnings("rawtypes")
	public JSONArray getDictIdByTableInfo(String dictKey,String tableName,String sql) {
		List<Map> dictList = dictMapper.getDictIdByTableInfo(dictKey,tableName,sql);
		JSONArray array = new JSONArray();
		//JSON.toJSONString(object)
		if(dictList!=null&&dictList.size()>0){
			dictList.forEach((Map map) -> {
				array.add(map);
			});
		}
		return array;
	}

	@Override
	public Object itemId2name(Object dictId, Object itemId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据父字典id查询字典集合(JK 监控)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	@Override
	public JSONArray getDictInfoByParentDictIdFromJK(String parentDictId) {
		// TODO Auto-generated method stub
		List<Map> dictList = dictMapper.getDictInfoByParentDictIdFormJK(parentDictId);
		JSONArray array = new JSONArray();
		//JSON.toJSONString(object)
		if(dictList!=null&&dictList.size()>0){
			dictList.forEach((Map map) -> {
				array.add(map);
			});
		}
		return array;
	}

	/**
	 * 根据父字典id查询字典集合(alarm 监控告警表)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	@Override
	public JSONArray getDictInfoByParentDictIdFromAlarm(String parentDictId) {
		// TODO Auto-generated method stub
				List<Map> dictList = dictMapper.getDictInfoByParentDictIdFromAlarm(parentDictId);
				JSONArray array = new JSONArray();
				//JSON.toJSONString(object)
				if(dictList!=null&&dictList.size()>0){
					dictList.forEach((Map map) -> {
						array.add(map);
					});
				}
				return array;
	}

	/**
	 * 根据父字典id查询字典集合(Vendor 监控厂家表)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	@Override
	public JSONArray getDictInfoByParentDictIdFromVendor(String parentDictId){
		// TODO Auto-generated method stub
		List<Map> dictList = dictMapper.getDictInfoByParentDictIdFromVendor(parentDictId);
		JSONArray array = new JSONArray();
		//JSON.toJSONString(object)
		if(dictList!=null&&dictList.size()>0){
			dictList.forEach((Map map) -> {
				array.add(map);
			});
		}
		return array;
	}
	
	
	/**
	 * 根据父字典id查询字典集合(station 监控台站表)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	public JSONArray getDictInfoByParentDictIdFromStation(String parentDictId){
		// TODO Auto-generated method stub
				List<Map> dictList = dictMapper.getDictInfoByParentDictIdFromStation(parentDictId);
				JSONArray array = new JSONArray();
				//JSON.toJSONString(object)
				if(dictList!=null&&dictList.size()>0){
					dictList.forEach((Map map) -> {
						array.add(map);
					});
				}
				return array;
	}

	@Override
	public JSONArray getDictInfoByParentDictIdFromTenance(String parentDictId) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				List<Map> dictList = dictMapper.getDictInfoByParentDictIdFromTenance(parentDictId);
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
