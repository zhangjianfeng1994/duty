package com.boco.eoms.base.service;


/**
 * ID转name的公共类
 * @author chenjianghe
 *
 */
public interface IID2NameService {
	
	/**
	 * id转name
	 * @param id 一般为表中的主键
	 * @param type 类型
	 * @return 返回主键对应的name(自定义)
	 * @throws BusinessException
	 * @since 0.1
	 */
	public String id2Name(String id, String type);

	/**
	 * id转name
	 * @param id 一般为表中的主键
	 * @param type 类型
	 * @param parentDictId 父id 
	 * @return 返回主键对应的name(自定义)
	 * @throws BusinessException
	 * @since 0.1
	 */
	public String id2Name(String id, String type,String parentDictId);
}