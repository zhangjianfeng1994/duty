package com.boco.eoms.shiftCentralfault.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.commons.BaseUtil;
import com.boco.eoms.commons.TimeUtil;
import com.boco.eoms.dutyConfig.model.DutyFlightConfig;
import com.boco.eoms.dutyConfig.service.DutyFlightConfigService;
import com.boco.eoms.dutyInfo.model.DutyInfo;
import com.boco.eoms.shiftCentralfault.model.ShiftCentralfaultModel;
import com.boco.eoms.shiftCentralfault.service.ShiftCentralfaultService;
import com.boco.eoms.successionShift.model.SuccessionShiftModel;
import com.boco.eoms.successionShift.service.SuccessionShiftService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


@Controller
@RequestMapping(value = "shiftCentralfault")
public class ShiftCentralfaultController {
	
	
	private static final Logger logger = Logger.getLogger(ShiftCentralfaultController.class);
	
	public static Logger getLogger() {
		return logger;
	}
	
	@Resource
	ShiftCentralfaultService shiftcentralfaultservice ;
	
	
	@Resource
	private SuccessionShiftService successionshiftservice;
	
	@Resource
	private DutyFlightConfigService dutyflightconfigservice;
	
	
	/**
	 *@see 查询关联故障工单数据
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/relatefaults",method=RequestMethod.GET)
	public Object getShiftCentralfaultList(HttpServletRequest request){
		JSONObject jsonObject =new JSONObject();
		JSONArray arr = new JSONArray();
		String type = BaseUtil.nullObject2String(request.getParameter("type")); //确定要查询哪个班次的数据(本班次和上班次)
		int shift_id = BaseUtil.nullObject2int(request.getParameter("shift_id"));
		int currentPage = BaseUtil.nullObject2int(request.getParameter("page"));
		int pageSize = BaseUtil.nullObject2int(request.getParameter("pageSize"));
		currentPage =  currentPage == 0 ? 1:currentPage;
		pageSize =  pageSize == 0 ? 5:pageSize;
		if ("lastshift".equals(type)) { //上班次的数据为遗留数据
			//分页查询
			int last_shift_id = successionshiftservice.getModelById(shift_id).getLAST_SHIFT_ID();
			PageHelper.startPage(currentPage,pageSize);
			List<ShiftCentralfaultModel> shiftCentralfaultLists = shiftcentralfaultservice.selectByShiftId(last_shift_id);
			if(shiftCentralfaultLists!=null && shiftCentralfaultLists.size()>0){
				  for(int i=0;i<shiftCentralfaultLists.size();i++){
					  ShiftCentralfaultModel shiftCentralfault = (ShiftCentralfaultModel)shiftCentralfaultLists.get(i);
					  arr.add(shiftCentralfault);
					  }
			 }
			Page<ShiftCentralfaultModel> listCount = (Page<ShiftCentralfaultModel>)shiftCentralfaultLists;
			jsonObject.put("relatefaults", arr);
			//分页信息
			JSONObject pageObj = new JSONObject();
			pageObj.put("total", listCount.getTotal());
			pageObj.put("pageTotal",listCount.getPages());
			pageObj.put("pageIndex", currentPage);
			jsonObject.put("meta", pageObj);
		}else if ("nowshift".equals(type)) {
			//分页查询
			PageHelper.startPage(currentPage,pageSize);
			List<ShiftCentralfaultModel> shiftCentralfaultLists = shiftcentralfaultservice.selectByShiftId(shift_id);
			if(shiftCentralfaultLists!=null && shiftCentralfaultLists.size()>0){
				  for(int i=0;i<shiftCentralfaultLists.size();i++){
					  ShiftCentralfaultModel shiftCentralfault = (ShiftCentralfaultModel)shiftCentralfaultLists.get(i);
					  arr.add(shiftCentralfault);
					  }
			 }
			jsonObject.put("relatefaults", arr);
			
			//分页信息
			Page<ShiftCentralfaultModel> listCount = (Page<ShiftCentralfaultModel>)shiftCentralfaultLists;
			JSONObject pageObj = new JSONObject();
			pageObj.put("total", listCount.getTotal());
			pageObj.put("pageTotal",listCount.getPages());
			pageObj.put("pageIndex", currentPage);
			jsonObject.put("meta", pageObj);
		}else if ("relatefault".equals(type)) {
			int last_shift_id = successionshiftservice.getModelById(shift_id).getLAST_SHIFT_ID();//上班次交接班记录
			Map<String, String> flightTimeMap = getDutyFlightTime(shift_id);
			String beginTimeString = flightTimeMap.get("beginTime");
			String endTimeString = flightTimeMap.get("endTime");
			//开始查询本系统值班故障表上班次遗留数据及接口故障本班次时间数据
			PageHelper.startPage(currentPage,pageSize);
			List<ShiftCentralfaultModel> shiftCentralfaultLists = shiftcentralfaultservice.selectAllFaultByCondition(last_shift_id,shift_id, beginTimeString, endTimeString);
			if(shiftCentralfaultLists!=null && shiftCentralfaultLists.size()>0){
				  for(int i=0;i<shiftCentralfaultLists.size();i++){
					  ShiftCentralfaultModel shiftCentralfault = (ShiftCentralfaultModel)shiftCentralfaultLists.get(i);
					  arr.add(shiftCentralfault);
					  }
			 }
			jsonObject.put("relatefaults", arr);
			//分页信息
			Page<ShiftCentralfaultModel> listCount = (Page<ShiftCentralfaultModel>)shiftCentralfaultLists;
			JSONObject pageObj = new JSONObject();
			pageObj.put("total", listCount.getTotal());
			pageObj.put("pageTotal",listCount.getPages());
			pageObj.put("pageIndex", currentPage);
			jsonObject.put("meta", pageObj);
		}else if ("centrafault".equals(type)) {
			String sheetid = BaseUtil.nullObject2String(request.getParameter("sheetid"));
			Map<String,Object>  map =  shiftcentralfaultservice.selectCentralFaultBySheetid(sheetid);
			JSONObject jsonObject1 =new JSONObject();
			if (map !=null) {
				jsonObject1 = JSONObject.parseObject(JSON.toJSONString(map));
			}
			jsonObject.put("relatefaults", jsonObject1);
		}
		return jsonObject;
	}
	
	
	/**
	  * 新增关联故障
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/relatefaults", method=RequestMethod.POST)
	 @ResponseBody
	 public Object insertShiftCentralShift(HttpServletRequest request) throws Exception{
		 try {
			 String sheets = BaseUtil.nullObject2String(request.getParameter("sheetids")); //确定要查询哪个班次的数据(本班次和上班次)
			 String[] sheetStrings = sheets.split(",");
			 int shift_id = BaseUtil.nullObject2int(request.getParameter("shift_id"));
			 shiftcentralfaultservice.saveBatchFormCentralfault(sheetStrings,shift_id);
			 JSONObject jsonobj = new JSONObject();
			 jsonobj.put("status",1); //表示成功
			 return jsonobj;
		} catch (Exception e) {
			 e.printStackTrace();
			 JSONObject jsonobj = new JSONObject();
			 jsonobj.put("status",0); //表示失败
			 return jsonobj;
		}
		
	 }
	
	 /**
	  * 删除一条值班故障
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/deletenowfault", method=RequestMethod.POST)
	 @ResponseBody
	 public Object deleteShiftCentralShift(HttpServletRequest request) throws Exception{
		 try {
			 String sheetid = BaseUtil.nullObject2String(request.getParameter("sheetid"));
			 int shift_id = BaseUtil.nullObject2int(request.getParameter("shift_id"));
			 shiftcentralfaultservice.deleteModel(sheetid,shift_id);
			 JSONObject jsonobj = new JSONObject();
			 jsonobj.put("status",1); //表示成功
			 return jsonobj;
		} catch (Exception e) {
			 e.printStackTrace();
			 JSONObject jsonobj = new JSONObject();
			 jsonobj.put("status",0); //表示成功
			 return jsonobj;
		}
		 
	 }
	 
	/**
	 *@see 根据shift_id查询预计的交接班时间
	 * 
	 */
	public Map<String,String> getDutyFlightTime(int shift_id){
		SuccessionShiftModel successionshiftmodel = successionshiftservice.getModelById(shift_id);
		int duty_flight_id = successionshiftmodel.getDUTY_FLIGHT_ID();
		Date duty_time = successionshiftmodel.getDUTY_TIME();
		DutyFlightConfig dutyFlightModel = dutyflightconfigservice.selectByPrimaryKey(duty_flight_id);
		String beginTimeString = dutyFlightModel.getDutyFlightBegintime();
		String endTimeString = dutyFlightModel.getDutyFlightEndtime();
		String duty_time_string = TimeUtil.getDateParseString(duty_time,3); //转化为 yyyy-MM-dd的String
		beginTimeString = duty_time_string+" "+beginTimeString+":00" ; //开始时间 yyyy-MM-dd HH:mm:ss
		endTimeString = duty_time_string+" "+endTimeString+":00" ; //结束时间 yyyy-MM-dd HH:mm:ss
		//开始时间和结束时间为班次排班的接班开始时间和交班结束时间,并不是实际的交接班时间
		Date beginTimeDate = TimeUtil.getStringParseDate(beginTimeString,5);
		Date endTimeDate = TimeUtil.getStringParseDate(endTimeString,5);
		//判断开始时间和结束时间,如果结束时间大于开始时间,那么结束时间跨天
		if (endTimeDate.getTime()<=beginTimeDate.getTime()) {
			endTimeString = TimeUtil.addDaysToDate(endTimeString,1,5);
			endTimeDate = TimeUtil.getStringParseDate(endTimeString,5);
		}
		Map<String, String>  map = new HashMap<String, String>();
		map.put("beginTime", beginTimeString);
		map.put("endTime", endTimeString);
		return map;
	}
	
	
	
}
