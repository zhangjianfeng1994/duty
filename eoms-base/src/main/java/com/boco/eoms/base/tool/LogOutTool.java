package com.boco.eoms.base.tool;

import org.apache.log4j.Logger;

/**
 * 日志输出工具类
 * @author chenjianghe
 *
 */
public class LogOutTool {

	/**
	 	* BocoLog 日志输出
		* @author chenjianghe
		* @version 版本1.0
		* @param obj 输出类
		* @param message 输出信息
		* @return 无
	    * @throws 无
	*/
	public static void info(Object obj,String message){
		Logger.getLogger(obj.getClass()).info(message);

	}

	/**
	 	* BocoLog 异常日志输出
		* @author chenjianghe
		* @version 版本1.0
		* @param obj 输出类
		* @param message 输出信息
		* @return 无
	    * @throws 无
	*/
	public static void error(Object obj,String message,Throwable throwable){
		Logger.getLogger(obj.getClass()).error(message,throwable);
	}
}
