package com.boco.eoms.base.dao;

import java.util.List;
import java.util.Map;

/**
  * @ClassName: IQueryJdbcDao
  * @Description: TODO
  * @author szy
  * @date 2017-07-07 15:00:30
  * @version V1.0
  * @since JDK 1.8
  * 通用jdbc使用方法
 */
public interface IQueryJdbcDao {
	
	/**
	 * 根据sql语句查询对应结果
	 * @param sql
	 * @return List
	 * @throws Exception
	 */
	public List<Map<String,Object>> getQueryList(String sql) throws Exception;
	
	/**
	 * 根据sql语句插入数据库
	 * @param sql
	 * @throws Exception
	 */
	public void insertInfoBySql(String sql);
	
	/**
	 * 根据sql语句更新数据库
	 * @param sql
	 * @throws Exception
	 */
	public void updateInfoBySql(String sql);
}
