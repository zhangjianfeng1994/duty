package com.boco.eoms.system.dict.service;

import com.alibaba.fastjson.JSONArray;

/**
 * 字典值 dict service 接口
 * @author chenjianghe
 *
 */
public interface IDictService {
	
	/**
	 * 根据父字典id查询字典集合(eoms)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	public JSONArray getDictInfoByParentDictId(String parentDictId);
	
	/**
	 * 根据父字典id查询字典集合(JK 监控)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	public JSONArray getDictInfoByParentDictIdFromJK(String parentDictId);
	
	/**
	 * 根据父字典id查询字典集合(alarm 监控告警表)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	public JSONArray getDictInfoByParentDictIdFromAlarm(String parentDictId);
	
	/**
	 * 根据父字典id查询字典集合(Vendor 监控厂家表)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	public JSONArray getDictInfoByParentDictIdFromVendor(String parentDictId);
	
	/**
	 * 根据父字典id查询字典集合(station 监控台站表)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	public JSONArray getDictInfoByParentDictIdFromStation(String parentDictId);
	
	/**
	 * 根据父字典id查询字典集合(tenance 监控维护部)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	public JSONArray getDictInfoByParentDictIdFromTenance(String parentDictId);
	
	
	
	
	/**
	 * 根据字典标识查询字典集合
	 * @author zsj
	 * @version 版本1.0 
	 * @param dictKey 字典关键字
	 * @param tableName 表名
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	public JSONArray getDictIdByTableInfo(String dictKey,String tableName,String sql);
	
	/**
	 * 唯一标识转成名称
	 * 
	 * @param itemId
	 *            唯一标识
	 * @throws Exception
	 * @return 名称
	 * @since 0.1
	 */
	public Object itemId2name(Object dictId, Object itemId)
			throws Exception;
	
}
