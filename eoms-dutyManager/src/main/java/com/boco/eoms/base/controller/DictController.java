package com.boco.eoms.base.controller;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.base.util.StaticMethod;
import com.boco.eoms.system.dept.service.IDeptService;
import com.boco.eoms.system.dict.service.IDictService;

/**
 *  字典值controller
 * @author szy
 *
 */
@Controller
@RequestMapping("/dictController")
public class DictController {
	
	@Autowired
	private IDictService dictService;
	
	@Autowired
	private IDeptService deptService;
	/** 
 	* 根据父字典Id获取对应字典信息
	* @author szy
	* @version 版本1.0 
	* @param request
	* @return json
    * @throws Exception
	*/
	@RequestMapping(value="/getDictInfoByParentDictId", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getDictInfoByParentDictId(HttpServletRequest request) throws Exception{
		
		String dictModel = request.getParameter("dictModel");
		JSONObject obj = JSONObject.parseObject(dictModel);
		Iterator<String> it = obj.keySet().iterator();
		JSONObject dictObject = new JSONObject();
		while(it.hasNext()){
			String key = (String)it.next();
			String value = StaticMethod.nullObject2String(obj.getString(key));
			if(!"".equals(value)){
				//value的格式，例如：dictJK#10001 (查监控字典表) dict#10101（查eoms字典表）,sp#factoryName#sp_factory（通过factoryName这个字段名，去sp_factory取id）
				String[] valueArray = value.split("#");//分割出来的字符数组
				if(valueArray.length == 2){
					String dictType = valueArray[0];
					String dictKey = valueArray[1];
					//factory查询eoms字典
					if("dict".equals(dictType)){
						JSONArray array = dictService.getDictInfoByParentDictId(dictKey);
						dictObject.put(key, array);
					}else if("dictJK".equals(dictType)){//监控字典
						JSONArray array = dictService.getDictInfoByParentDictIdFromJK(dictKey);
						dictObject.put(key, array);
					}else if("dept".equals(dictType)){//分中心
						JSONArray array = deptService.getDeptByParentDeptId(dictKey);
						dictObject.put(key, array);
					}else if("dictAlarm".equals(dictType)){//告警字典
						JSONArray array = dictService.getDictInfoByParentDictIdFromAlarm(dictKey);
						dictObject.put(key, array);
					}else if("dictVendor".equals(dictType)){//厂家字典
						JSONArray array = dictService.getDictInfoByParentDictIdFromVendor(dictKey);
						dictObject.put(key, array);
					}else if("station".equals(dictType)){//台站字典
						JSONArray array = dictService.getDictInfoByParentDictIdFromStation(dictKey);
						dictObject.put(key, array);
					}else if("tenance".equals(dictType)){//维护部字典
						JSONArray array = dictService.getDictInfoByParentDictIdFromTenance(dictKey);
						dictObject.put(key, array);
					}
				}else if(valueArray.length >= 3){
					String dictType = valueArray[0];		
					
					if("sp".equals(dictType)){
						String dictKey = valueArray[1];		//分组关键字（dictName）
						String tableName = valueArray[2];	//表名
						String sql = "";
						if(valueArray.length == 4){
							String where = valueArray[3];
							sql = " and " + where;
						}
						JSONArray array = dictService.getDictIdByTableInfo(dictKey,tableName,sql);
						dictObject.put(key, array);
					}
				}
			}
		}
		return dictObject;
	}
	
	
}
