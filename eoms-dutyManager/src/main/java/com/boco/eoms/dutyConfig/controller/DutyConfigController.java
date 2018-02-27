package com.boco.eoms.dutyConfig.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.base.util.StaticMethod;
import com.boco.eoms.base.util.UUIDHexGenerator;
import com.boco.eoms.commons.BaseUtil;
import com.boco.eoms.dutyConfig.model.DutyConfigModel;
import com.boco.eoms.dutyConfig.model.DutyFlightConfig;
import com.boco.eoms.dutyConfig.model.DutyUsers;
import com.boco.eoms.dutyConfig.service.DutyConfigModelService;
import com.boco.eoms.dutyConfig.service.DutyFlightConfigService;
import com.boco.eoms.dutyConfig.service.DutyUsersService;

@Controller
@RequestMapping("/dutyConfigController")
public class DutyConfigController {
	
	@Resource
	private DutyConfigModelService dutyService;
	@Resource
	private DutyFlightConfigService dutyFlightService;
	@Resource
	private DutyUsersService dutyUserService;

	
	
	/**
	 * DutyConfig 值班表新增 
	 * @param param
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/saveDutyconfig",method=RequestMethod.POST)
	public Object saveDutyConfig(HttpServletRequest request){
		
		
		String duty_name = StaticMethod.nullObject2String(request.getParameter("duty_config_name"));
		String duty_handover_type = StaticMethod.nullObject2String(request.getParameter("duty_handover_type"));

		String duty_handover_time = StaticMethod.nullObject2String(request.getParameter("duty_handover_time"));
		Integer dutyId = dutyService.selectMaxId();
		int lastDutyId ;
		if(null == dutyId){
			lastDutyId = 0;
		}else{
			lastDutyId = dutyId.intValue();
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DutyConfigModel dcm = new DutyConfigModel();
		dcm.setDutyConfigName(duty_name);
		dcm.setDutyHandoverType(duty_handover_type);
		dcm.setDutyHandoverTime(duty_handover_time);
		dcm.setCreateTime(formatter.format(new Date()));
		dcm.setLastDutyConfigId(lastDutyId);
		dutyService.save(dcm);
		
		int id = dcm.getId();
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("id", id);

		return jsonobj;
	}
	
	
	/**
	 * 显示值班配置列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="dutyManagent/dutyConfigs",method=RequestMethod.GET)
	public Object selectAllDuty(HttpServletRequest request){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		// 获取每页显示条数
		Integer	pageSize = StaticMethod.nullObject2int(request.getParameter("limit"));
		// 当前页数
		Integer pageIndex = StaticMethod.nullObject2int(request.getParameter("pageIndex"));
		pageSize = pageSize == null ||pageSize == 0?15:pageSize;
		pageIndex = pageIndex == null || pageIndex == 0?1:pageIndex;
		
		
		Map resultMap =  dutyService.selectAll(pageIndex, pageSize);
		
		JSONObject joList = new JSONObject();
		
		int total = BaseUtil.nullObject2int(resultMap.get("total"));
		int pageTotal = total/15;
		if((total%15) != 0){
			pageTotal++;
		}

		JSONObject pageObj = new JSONObject();
		pageObj.put("total",resultMap.get("total"));
		pageObj.put("pageTotal",pageTotal);
		pageObj.put("pageIndex", pageIndex);
		
		//将数据传回页面
		joList.put("meta", pageObj);
		joList.put("dutyManagent/dutyConfig", resultMap.get("taskList"));
		return joList;
	}
	
	
	/**
	 * 根据dutyConfigId 查询相关班次
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getDutyFlights",method=RequestMethod.POST)
	public Object selectDutyFlightByDutyConfigId(HttpServletRequest request){
		int duty_config_id = StaticMethod.nullObject2int(request.getParameter("duty_config_id"));
		List dfList = dutyFlightService.selectByDutyConfigId(duty_config_id);//根据duty_config id查询此配置中班次信息
		JSONArray jsonarr = new JSONArray();
		
		for(int i =0;i<dfList.size();i++){
			JSONObject json = new JSONObject();
//			Map map = (Map) dfList.get(i);
			DutyFlightConfig dfc = (DutyFlightConfig) dfList.get(i);
			json.put("name", StaticMethod.nullObject2String(dfc.getDutyFlightName()));//map.get("DUTY_FLIGHT_NAME")));
			json.put("beginTime", StaticMethod.nullObject2String(dfc.getDutyFlightBegintime()));//map.get("DUTY_FLIGHT_BEGINTIME")));
			json.put("endTime", StaticMethod.nullObject2String(dfc.getDutyFlightEndtime()));//map.get("DUTY_FLIGHT_ENDTIME")));
			json.put("duty_flight_id", StaticMethod.nullObject2int(dfc.getDutyFlightId()));//map.get("DUTY_FLIGHT_ID")));
			jsonarr.add(json);
		}
		return jsonarr;
	}
	
	
	/**
	 * 根据dutyConfigId 查询相关人员
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getDutyUsers",method=RequestMethod.POST)
	public Object selectDutyUserByDutyConfigId(HttpServletRequest request){
		int duty_config_id = StaticMethod.nullObject2int(request.getParameter("duty_config_id"));
		List tasklist = dutyUserService.selectByDutyConfigId(duty_config_id);
		JSONArray jsonarr = new JSONArray();
		for(int i=0;i<tasklist.size();i++){
			DutyUsers duser = (DutyUsers)tasklist.get(i);
			JSONObject json = new JSONObject();
			json.put("id",  StaticMethod.nullObject2int(duser.getDutyUsersId()));
			json.put("pId", 1);
			json.put("name",  StaticMethod.nullObject2String(duser.getUserName()));
			json.put("rId",  StaticMethod.nullObject2String(duser.getUserId()));
			json.put("icon", "/assets/images/u36884.png");
			jsonarr.add(json);
		}
		return jsonarr;
	}
	
	
	/**
	 * DutyFlightConfig配置表信息保存
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/saveFlightconfig",method=RequestMethod.POST)
	public Object saveDutyFlightConfig(HttpServletRequest request) throws Exception{

		String dutyFlightName = StaticMethod.nullObject2String(request.getParameter("duty_flight_name"));
		String duty_flight_begintime = StaticMethod.nullObject2String(request.getParameter("duty_flight_begintime"));
		String duty_flight_endtime = StaticMethod.nullObject2String(request.getParameter("duty_flight_endtime"));
		
		String[] dutyFlightNames = dutyFlightName.split(",");
		String[] duty_flight_begintimes = duty_flight_begintime.split(",");
		String[] duty_flight_endtimes = duty_flight_endtime.split(",");
		Integer duty_config_id = StaticMethod.nullObject2int(request.getParameter("duty_config_id"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i =0;i<dutyFlightNames.length;i++){
			DutyFlightConfig dutyFlightConfig = new DutyFlightConfig();
			dutyFlightConfig.setDutyFlightName(StaticMethod.nullObject2String(dutyFlightNames[i]));
			dutyFlightConfig.setDutyFlightBegintime(StaticMethod.nullObject2String(duty_flight_begintimes[i]));
			dutyFlightConfig.setDutyFlightEndtime(StaticMethod.nullObject2String(duty_flight_endtimes[i]));
//			dutyFlightConfig.setLastDutyConfigId(last_duty_config_id);
//			dutyFlightConfig.setLastDutyFlightId(last_duty_flight_id);
			dutyFlightConfig.setCreateTime(formatter.format(new Date()));
			dutyFlightConfig.setDutyConfigId(duty_config_id);
			//保存到数据库
			dutyFlightService.save(dutyFlightConfig);
		}
		//获取上一次值班配置id
		
//		int last_duty_config_id = 0 ;
//		if(null == duty_config_id){
//			last_duty_config_id = 0;
//		}else{
//			last_duty_config_id = duty_config_id.intValue() - 1;
//		}
//		//获取上一次班次配置id
//		Integer duty_flight_id = dutyFlightService.selectMaxId(duty_config_id);
//		int last_duty_flight_id = 0;
//		if(null == duty_flight_id){
//			last_duty_flight_id = 0;
//		}else{
//			last_duty_flight_id = duty_flight_id.intValue();
//		}
		//建立模型对象
		
		
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("id", UUIDHexGenerator.getInstance().getID()); 
		JSONArray jsonarr = new JSONArray();
		jsonarr.add(jsonobj);
		JSONObject jsonobject = new JSONObject();
		jsonobject.put("flightconfig", jsonarr);
		return jsonobject;
	}
	
	/**
	 * dutyusers 表配置信息新增
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/saveDutyuser",method=RequestMethod.POST)
	public Object saveDutyUser(HttpServletRequest request) throws Exception{
		
		String user_name = StaticMethod.nullObject2String(request.getParameter("user_name"));
		String user_id = StaticMethod.nullObject2String(request.getParameter("user_id"));
		int duty_config_id = StaticMethod.nullObject2int(request.getParameter("duty_config_id"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DutyUsers dutyUser = new DutyUsers();
		dutyUser.setDutyConfigId(duty_config_id);
		dutyUser.setUserName(user_name);
		dutyUser.setCreateTime(new Date());
		dutyUser.setUserId(user_id);
		
		int id = dutyUserService.addDutyUser(dutyUser);
		
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("id", UUIDHexGenerator.getInstance().getID()); 
		JSONArray jsonarr = new JSONArray();
		jsonarr.add(jsonobj);
		JSONObject jsonobject = new JSONObject();
		jsonobject.put("dutyuser", jsonarr);
		return jsonobject;
	}
	
	
	
	/**
	 * 更新dutyconfg
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/updateDutyConfig",method=RequestMethod.POST)
	public Object updateDutyConfig(HttpServletRequest request) throws Exception{
//		Map parameterMap = param;
//		Map cloneParameterMap = new HashMap();
//		cloneParameterMap.putAll(parameterMap);
		String duty_name = StaticMethod.nullObject2String(request.getParameter("duty_config_name"));
		String duty_handover_type = StaticMethod.nullObject2String(request.getParameter("duty_handover_type"));
		String duty_handover_time = StaticMethod.nullObject2String(request.getParameter("duty_handover_time"));
		int duty_config_id = StaticMethod.nullObject2int(request.getParameter("duty_config_id"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DutyConfigModel dcm = new DutyConfigModel();
		dcm.setDutyConfigId(duty_config_id);
		dcm.setDutyConfigName(duty_name);
		dcm.setDutyHandoverTime(duty_handover_time);
		dcm.setDutyHandoverType(duty_handover_type);
		dcm.setCreateTime(formatter.format(new Date()));
		dutyService.update(dcm);
		
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("id", duty_config_id); 
		return jsonobj;
	}
	
	/**
	 * 根据filghtid逐条删除班次
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/deleteDutyFilghtById",method=RequestMethod.POST)
	public Object deleteDutyFilghtById(HttpServletRequest request) throws Exception{
		int duty_flight_id = StaticMethod.nullObject2int(request.getParameter("duty_flight_id"));
		dutyFlightService.deleteByid(duty_flight_id);
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("id", duty_flight_id); 
		return jsonobj;
	}
	/**
	 * 更新dutyfilgh
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/updateDutyFilght",method=RequestMethod.POST)
	public Object updateDutyFilght(HttpServletRequest request) throws Exception{
		String duty_flight_name = StaticMethod.nullObject2String(request.getParameter("duty_flight_name"));
		String duty_flight_begintime = StaticMethod.nullObject2String(request.getParameter("duty_flight_begintime"));
		String duty_flight_endtime = StaticMethod.nullObject2String(request.getParameter("duty_flight_endtime"));
		String duty_flight_id = StaticMethod.nullObject2String(request.getParameter("duty_flight_id"));
		int duty_config_id = StaticMethod.nullObject2int(request.getParameter("duty_config_id"));
		
		String[] dutyFlightNames = duty_flight_name.split(",");
		String[] duty_flight_begintimes = duty_flight_begintime.split(",");
		String[] duty_flight_endtimes = duty_flight_endtime.split(",");
		String[] duty_flight_ids =duty_flight_id.split(",");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for(int i =0;i<dutyFlightNames.length;i++){
			DutyFlightConfig dutyFlightConfig = new DutyFlightConfig();
			dutyFlightConfig.setDutyFlightName(StaticMethod.nullObject2String(dutyFlightNames[i]));
			dutyFlightConfig.setDutyFlightBegintime(StaticMethod.nullObject2String(duty_flight_begintimes[i]));
			dutyFlightConfig.setDutyFlightEndtime(StaticMethod.nullObject2String(duty_flight_endtimes[i]));
			dutyFlightConfig.setCreateTime(formatter.format(new Date()));
			dutyFlightConfig.setDutyConfigId(duty_config_id);
			dutyFlightConfig.setDutyFlightId(StaticMethod.nullObject2int(duty_flight_ids[i]));
			//保存到数据库
			dutyFlightService.update(dutyFlightConfig);
		}
		
		
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("id", duty_config_id); 
		return jsonobj;
	}
	
	
	/**
	 * 更改dutyuser信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/updateDutyUser",method=RequestMethod.POST)
	public Object updateDutyUser(HttpServletRequest request) throws Exception{
		String user_name = StaticMethod.nullObject2String(request.getParameter("user_name"));
		String user_id = StaticMethod.nullObject2String(request.getParameter("user_id"));
		int duty_users_id = StaticMethod.nullObject2int(request.getParameter("duty_users_id"));
		DutyUsers dutyUser = new DutyUsers();
		dutyUser.setDutyUsersId(duty_users_id);
		dutyUser.setUserId(user_id);
		dutyUser.setUserName(user_name);
		dutyUser.setCreateTime(new Date());
		dutyUserService.update(dutyUser);
		
		
		return request;
		
	}
	
	/**
	 * 根据configid删除dutyconfig信息以及相关的flight 和user
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/dutyManagent/dutyConfigs/{id}"}, method=RequestMethod.DELETE)
	@ResponseBody
	public Object removeFaulttimematching(@PathVariable String id) throws Exception{
		
		dutyService.deleteConfigByid(id);
		dutyFlightService.deleteByDutyId(StaticMethod.nullObject2int(id));
		dutyUserService.deleteByDeleteIf(StaticMethod.nullObject2int(id));
		return id;
		
	}
	/**
	 * 根据configid 删除相关人员
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/deleteDutyUser",method=RequestMethod.POST)
	public Object deleteDutyUser(HttpServletRequest request) throws Exception{
		int duty_config_id = StaticMethod.nullObject2int(request.getParameter("duty_config_id"));
		dutyUserService.deleteByConfigId(duty_config_id);
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("id", duty_config_id); 
		return jsonobj;
	}
	
	
	/**
	 * 选择人员时效验该人员是否出现在dutyUser表中 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/examinationUser",method=RequestMethod.POST)
	public Object examinationUser(HttpServletRequest request) throws Exception{
		String users = StaticMethod.nullObject2String(request.getParameter("userId"));
		int dutyConfigId = StaticMethod.nullObject2int(request.getParameter("dutyConfigId"));
		String userName = "";
		if(!"".equals(users)){//获取选择的userid 判断是否存在 
			if(users.indexOf(",")>0){//如果存在 判断是一个还是多个 
				String[] userIds = users.split(",");//如果是多个则切分
				for(int i=0;i<userIds.length;i++){//循环查询duty表中是否存在改user
					List list = dutyUserService.selectByUserId(userIds[i],dutyConfigId);
					if(list.size()>0){
						DutyUsers du = (DutyUsers) list.get(0);
						userName = du.getUserName();//如果已经存在则返回改user的名字
						break;
					}
				}
			}else{//如果不是多个则直接查询dutyuser表中是否已经存在改user
				List list = dutyUserService.selectByUserId(users,dutyConfigId);
				if(list.size()>0){
					DutyUsers du = (DutyUsers) list.get(0);
					userName = du.getUserName();//如果已经存在则返回改user的名字
				}
			}
		}
		
		
		JSONObject jjson = new JSONObject();
		jjson.put("userName", userName);
		return jjson;
	}
}
