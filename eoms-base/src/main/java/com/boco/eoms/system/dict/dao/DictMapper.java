package com.boco.eoms.system.dict.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 字典值 dict mapper 接口
 * @author chenjianghe
 *
 */
public interface DictMapper {

	/** 
	 	* 根据dictId查询dictname eoms
		* @author chenjianghe
		* @version 版本1.0 
		* @param dictid
		* @return dictname
	    * @throws 无
	*/
	public String getDictNameByDictId(@Param("dictId") String dictId);
	
	/** 
 	* 根据dictId查询dictname JK
	* @author chenjianghe
	* @version 版本1.0 
	* @param dictid
	* @param parentDictId 父id
	* @return dictname
    * @throws 无
*/
	public String getDictNameFromJK(@Param("dictId") String dictId,@Param("parentDictId") String parentDictId);

/** 
	* 根据dictId查询dictname JK
	* @author chenjianghe
	* @version 版本1.0 
	* @param dictid
	* @param parentDictId 父id
	* @return dictname
	* @throws 无
	*/
	public String getDictNameFromAlarm(@Param("dictId") String dictId,@Param("parentDictId") String parentDictId);
	
	/** 
	* 根据dictId查询dictname JK
	* @author chenjianghe
	* @version 版本1.0 
	* @param dictid
	* @param parentDictId 父id
	* @return dictname
	* @throws 无
	*/
	public String getDictNameFromVendor(@Param("dictId") String dictId,@Param("parentDictId") String parentDictId);
	
	
	/** 
	* 根据dictId查询dictname JK
	* @author chenjianghe
	* @version 版本1.0 
	* @param dictid
	* @param parentDictId 父id
	* @return dictname
	* @throws 无
	*/
	public String getDictNameFromStation(@Param("dictId") String dictId);
	
	
	/**
	 * 根据父字典id查询字典集合(eoms)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	@SuppressWarnings("rawtypes")
	public List<Map> getDictInfoByParentDictId(@Param("parentDictId")String parentDictId);
	
	/**
	 * 根据父字典id查询字典集合(JK 监控)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	@SuppressWarnings("rawtypes")
	public List<Map> getDictInfoByParentDictIdFormJK(@Param("parentDictId")String parentDictId);
	
	
	/**
	 * 根据父字典id查询字典集合
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	@SuppressWarnings("rawtypes")
	public List<Map> getDictIdByTableInfo(@Param("dictKey")String dictKey,@Param("tableName")String tableName,@Param("sql")String sql);
	
	/**
	 * 根据父字典id查询字典集合(alarm 监控)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	@SuppressWarnings("rawtypes")
	public List<Map> getDictInfoByParentDictIdFromAlarm(@Param("parentDictId")String parentDictId);
	
	/**
	 * 根据父字典id查询字典集合(Vendor 监控厂家)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	@SuppressWarnings("rawtypes")
	public List<Map> getDictInfoByParentDictIdFromVendor(@Param("parentDictId")String parentDictId);
	
	
	/**
	 * 根据父字典id查询字典集合(Vendor 监控台站)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	@SuppressWarnings("rawtypes")
	public List<Map> getDictInfoByParentDictIdFromStation(@Param("parentDictId")String parentDictId);
	
	
	/**
	 * 根据父字典id查询字典集合(Tenance 监控维护部)
	 * @author szy
	 * @version 版本1.0 
	 * @param parentDictId 父字典id
	 * @return 返回JSONArray
	 * @throws 无
	 * */
	@SuppressWarnings("rawtypes")
	public List<Map> getDictInfoByParentDictIdFromTenance(@Param("parentDictId")String parentDictId);
	
	

	
}
