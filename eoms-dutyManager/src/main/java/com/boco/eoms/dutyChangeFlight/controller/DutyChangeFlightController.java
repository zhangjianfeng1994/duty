package com.boco.eoms.dutyChangeFlight.controller;

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
import com.boco.eoms.base.util.StaticMethod;
import com.boco.eoms.dutyChangeFlight.model.DutyChangeFlight;
import com.boco.eoms.dutyChangeFlight.service.DutyChangeFlightService;

@Controller
@RequestMapping("/dutyChangeFlightController")
public class DutyChangeFlightController {
	
	@Resource
	private DutyChangeFlightService dutyChangeFlightService;
	
	/** 
	   * 获取本人换班列表
	  * @author zhangshijie
	  * @version 版本1.0 
	  * @param request
	  * @return String
	     * @throws Exception
	 */
	 @RequestMapping(value = {"dutyManagent/dutyChangeFlights"},method = RequestMethod.GET)
	 @ResponseBody
	 public Object getDutyChangeFlightList(HttpServletRequest request) throws Exception{

	  JSONObject json = new JSONObject();
	  int startIndex = StaticMethod.nullObject2int(request.getParameter("pageIndex"));
	  int length = StaticMethod.nullObject2int(request.getParameter("limit"));
	  String type = StaticMethod.nullObject2String(request.getParameter("type"));
	  String userId = StaticMethod.nullObject2String(request.getParameter("userId"));
	  String condition = StaticMethod.nullObject2String(request.getParameter("condition"));
	  
	  //值班记录查询结果
	  HashMap<String, Object> resultMap = dutyChangeFlightService.queryDutyChangeFlightList(startIndex,length,type,userId,condition);
	  JSONArray taskArr = new JSONArray();
	  
	  //待办分页信息
	  int total = StaticMethod.nullObject2int(resultMap.get("total"));
	  int pageIndex = StaticMethod.nullObject2int(resultMap.get("pageIndex"));
	  int pageTotal = StaticMethod.nullObject2int(resultMap.get("pageTotal"));;
	  JSONObject pageObj = new JSONObject();
	  pageObj.put("total", total);
	  pageObj.put("pageTotal",pageTotal);
	  pageObj.put("pageIndex", pageIndex);
	  
	  //将数据传回页面
	  json.put("meta", pageObj);
	  
	  @SuppressWarnings("unchecked")
	  List<DutyChangeFlight> dutyChangeFlightList = (List<DutyChangeFlight>) resultMap.get("dutyChangeFlight");
	  
	  if(dutyChangeFlightList!=null && dutyChangeFlightList.size()>0){
		  for(int i=0;i<dutyChangeFlightList.size();i++){
			  DutyChangeFlight dutyChangeFlight = (DutyChangeFlight)dutyChangeFlightList.get(i);
			  taskArr.add(dutyChangeFlight);
			  }
		  }
	  
	  json.put("dutyManagent/dutyChangeFlights", taskArr);

	  return json;
	 }
	 
	 /**
	  * 插入一条换班记录
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 @ResponseBody
	 @RequestMapping(value = {"dutyManagent/dutyChangeFlights"}, method=RequestMethod.POST)
	 public Object insertDutyChangeFlight(@RequestBody Map<String,Object> dutyChangeFlightMap) throws Exception{
		 DutyChangeFlight dutyChangeFlight = new DutyChangeFlight();
		 StaticMethod.map2BeanNormal(dutyChangeFlight, dutyChangeFlightMap);
		 dutyChangeFlightService.insertDutyChangeFlight(dutyChangeFlight);
		 int id = dutyChangeFlight.getId();
		 JSONObject jsonobj = new JSONObject();
		 jsonobj.put("id", id);
		 JSONObject jsonobject = new JSONObject();
		 jsonobject.put("dutyManagent/dutyChangeFlights", jsonobj);
		 return jsonobject;
	 }
	 
	 /**
	  * 更新一条换班记录
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = {"dutyManagent/dutyChangeFlights/{id}"}, method=RequestMethod.PUT)
	 @ResponseBody
	 public Object updateDutyChangeFlight(@RequestBody Map<String,Object> dutyChangeFlightMap,@PathVariable int id) throws Exception{
		 DutyChangeFlight dutyChangeFlight = new DutyChangeFlight();
		 StaticMethod.map2BeanNormal(dutyChangeFlight, dutyChangeFlightMap);
		 JSONObject jsonobj = new JSONObject();
		 dutyChangeFlight.setId(id);
		 dutyChangeFlightService.updateDutyChangeFlight(dutyChangeFlight);
		 jsonobj.put("id", id);
		 JSONObject jsonobject = new JSONObject();
		 jsonobject.put("dutyManagent/dutyChangeFlights", jsonobj);
		 return jsonobject;
	 }
	 
	 /**
	  * 获取班次开始时间
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 @ResponseBody
	 @RequestMapping(value = {"/searchDutyStartTime"}, method=RequestMethod.POST)
	 public JSONObject searchStartTime(HttpServletRequest request) throws Exception{
		 String createDutyTime = StaticMethod.nullObject2String(request.getParameter("createDutyTime"));
		 String dealDutyTime = StaticMethod.nullObject2String(request.getParameter("dealDutyTime"));
		 String type = StaticMethod.nullObject2String(request.getParameter("type"));
		 String userId = StaticMethod.nullObject2String(request.getParameter("userId"));
		 String dutyConfigId = StaticMethod.nullObject2String(request.getParameter("dutyConfigId"));
		 JSONObject dictObject = dutyChangeFlightService.searchDutyStartTime(createDutyTime, dealDutyTime, type, userId, dutyConfigId);
		 
		 return dictObject;
	 }
	 
	 /**
	  * 查询班次下的人
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 @ResponseBody
	 @RequestMapping(value = {"/searchUserByDutyFlightId"}, method=RequestMethod.POST)
	 public JSONArray searchUserByDutyFlightId(HttpServletRequest request) throws Exception{
		 String dutyFlightId = StaticMethod.nullObject2String(request.getParameter("dutyFlightId"));
		 String dutyTime = StaticMethod.nullObject2String(request.getParameter("dutyTime"));
		 JSONArray dictObject = dutyChangeFlightService.searchDutyUser(dutyFlightId, dutyTime);
		 
		 return dictObject;
	 }
}
