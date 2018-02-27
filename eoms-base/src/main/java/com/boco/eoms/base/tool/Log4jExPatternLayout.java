package com.boco.eoms.base.tool;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternParser;
/**
 * 使log4j日志打印进程号 %t
 * @author sizhongyuan
 * @version 1.0
 * */
public class Log4jExPatternLayout extends PatternLayout{
	public Log4jExPatternLayout(String pattern){ 
        super(pattern); 
    } 
      
    public Log4jExPatternLayout(){ 
        super(); 
    } 
     /** 
      * 重写createPatternParser方法，返回PatternParser的子类 
      */
     @Override
     protected PatternParser createPatternParser(String pattern) { 
      return new Log4jExPatternParser(pattern); 
     }
}
