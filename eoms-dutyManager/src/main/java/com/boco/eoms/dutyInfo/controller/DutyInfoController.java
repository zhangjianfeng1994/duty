package com.boco.eoms.dutyInfo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.base.util.FileHandingUtil;
import com.boco.eoms.base.util.StaticMethod;
import com.boco.eoms.dutyInfo.model.DutyInfo;
import com.boco.eoms.dutyInfo.service.DutyInfoService;

@Controller
@RequestMapping("/dutyInfoController")
public class DutyInfoController {
	
	@Resource
	private DutyInfoService dutyInfoService;
	
	/** 
	   * 获取值班记录列表
	  * @author zhangshijie
	  * @version 版本1.0 
	  * @param request
	  * @return String
	     * @throws Exception
	 */
	 @RequestMapping(value = {"dutyManagent/dutyInfos"},method = RequestMethod.GET)
	 @ResponseBody
	 public Object getDutyInfoList(HttpServletRequest request) throws Exception{

	  JSONObject json = new JSONObject();
	  int startIndex = StaticMethod.nullObject2int(request.getParameter("pageIndex"));
	  int length = StaticMethod.nullObject2int(request.getParameter("limit"));
	  String condition = StaticMethod.nullObject2String(request.getParameter("condition"));
	  
	  //值班记录查询结果
	  HashMap<String, Object> resultMap = dutyInfoService.queryDutyInfoList(startIndex,length,condition);
	  JSONArray taskArr = new JSONArray();
	  
	  //待办分页信息
	  int total = StaticMethod.nullObject2int(resultMap.get("total"));
	  int pageIndex = StaticMethod.nullObject2int(resultMap.get("pageIndex"));
	  int pageTotal = StaticMethod.nullObject2int(resultMap.get("pageTotal"));
	  
	  JSONObject pageObj = new JSONObject();
	  pageObj.put("total", total);
	  pageObj.put("pageTotal",pageTotal);
	  pageObj.put("pageIndex", pageIndex);
	  
	  //将数据传回页面
	  json.put("meta", pageObj);
	  
	  @SuppressWarnings("unchecked")
	  List<DutyInfo> dutyInfoList = (List<DutyInfo>) resultMap.get("dutyInfo");
	  
	  if(dutyInfoList!=null && dutyInfoList.size()>0){
		  for(int i=0;i<dutyInfoList.size();i++){
			  DutyInfo dutyInfo = (DutyInfo)dutyInfoList.get(i);
			  taskArr.add(dutyInfo);
			  }
		  }
	  
	  json.put("dutyManagent/dutyInfos", taskArr);

	  return json;
	 }
	 
	 /**
	  * 插入一条值班记录
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 @ResponseBody
	 @RequestMapping(value = {"dutyManagent/dutyInfos"}, method=RequestMethod.POST)
	 public Object insertDutyInfo(@RequestBody Map<String,Object> dutyInfoMap) throws Exception{
		 DutyInfo dutyInfo = new DutyInfo();
		 StaticMethod.map2BeanNormal(dutyInfo, dutyInfoMap);
		 dutyInfoService.insertDutyInfo(dutyInfo);
		 int id = dutyInfo.getId();
		 JSONObject jsonobj = new JSONObject();
		 jsonobj.put("id", id);
		 JSONObject jsonobject = new JSONObject();
		 jsonobject.put("dutyManagent/dutyInfos", jsonobj);
		 return jsonobject;
	 }
	 
	 /**
	  * 更新一条值班记录
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = {"dutyManagent/dutyInfos/{id}"}, method=RequestMethod.PUT)
	 @ResponseBody
	 public Object updateDutyInfo(@RequestBody Map<String,Object> dutyInfoMap,@PathVariable int id) throws Exception{
		 DutyInfo dutyInfo = new DutyInfo();
		 StaticMethod.map2BeanNormal(dutyInfo, dutyInfoMap);
		 JSONObject jsonobj = new JSONObject();
		 dutyInfo.setId(id);
		 dutyInfoService.updateDutyInfo(dutyInfo);
		 jsonobj.put("id", id);
		 JSONObject jsonobject = new JSONObject();
		 jsonobject.put("dutyManagent/dutyInfos", jsonobj);
		 return jsonobject;
	 }
	 
	 /**
	  * 删除一条值班记录
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = {"dutyManagent/dutyInfos/{id}"}, method=RequestMethod.DELETE)
	 @ResponseBody
	 public Object deleteDutyInfo(@PathVariable int id) throws Exception{
		  System.out.println(id);
		  dutyInfoService.deleteDutyInfo(id);
		  JSONObject jsonobj = new JSONObject();
		  jsonobj.put("id", id);
		  JSONObject jsonobject = new JSONObject();
		  jsonobject.put("dutyManagent/dutyInfos", jsonobj);
		  return jsonobject;
	 }
	 
	 /**
	  * Excel导入
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = {"/importExcel"}, method=RequestMethod.POST)
	 @ResponseBody
	 public Object importExcel(HttpServletRequest request) throws Exception{
		 
		 String result = "0";
		 
		 try {
			 FileHandingUtil fileHandingUtil = new FileHandingUtil();
			 //调用解析Excel方法
			 List<HashMap<String, String>> list = fileHandingUtil.analysisExcel(request,"excelKey.dutyInfoKey");
			 
			 if(list != null){
				 //list入库
				 dutyInfoService.insertDutyInfoList(list);
				 result = "1";
			 }
			 
		 } catch (Exception e) {
			 // TODO: handle exception
			 System.out.println(e);
		 }
		
		 
		 return result;
	 }
	 
	 
	 
	 /**
	  * 更新值班记录的数据是否可遗留
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 @ResponseBody
	 @RequestMapping(value = "updateDutyInfoRemain", method=RequestMethod.POST)
	 public Object updateDutyInfoRemain(HttpServletRequest request) throws Exception{
		try{
			String dutyId = StaticMethod.nullObject2String(request.getParameter("dutyId"));
			int remainIf = StaticMethod.nullObject2int(request.getParameter("remainIf"));
			dutyInfoService.batchUpdateDutyInfoRemain(remainIf,dutyId);
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("status",1); //表示成功
			return jsonobj;
		}catch (Exception e) {
				e.printStackTrace();
				JSONObject jsonObject =new JSONObject();
				jsonObject.put("status",0); //表示失败
				return jsonObject;
		}
	 }
}
