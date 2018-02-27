package com.boco.eoms.dutyConfig.controller;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.net.aso.s;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.base.tool.LogOutTool;
import com.boco.eoms.base.util.AttachmentStaticVariable;
import com.boco.eoms.base.util.FileHandingUtil;
import com.boco.eoms.base.util.StaticMethod;
import com.boco.eoms.base.util.XmlManage;
import com.boco.eoms.base.util.excel.ExcelExportUtil;
import com.boco.eoms.base.util.excel.ExcelImportUtil;
import com.boco.eoms.base.util.excel.ExcelParse;
import com.boco.eoms.commons.BaseUtil;
import com.boco.eoms.commons.TimeUtil;
import com.boco.eoms.dutyConfig.model.DutyConfigModel;
import com.boco.eoms.dutyConfig.model.DutyFlightConfig;
import com.boco.eoms.dutyConfig.model.DutySchedulingUsers;
import com.boco.eoms.dutyConfig.model.DutyUsers;
import com.boco.eoms.dutyConfig.service.DutyConfigModelService;
import com.boco.eoms.dutyConfig.service.DutyFlightConfigService;
import com.boco.eoms.dutyConfig.service.DutySchedulingUsersService;
import com.boco.eoms.dutyConfig.service.DutyUsersService;

@Controller
@RequestMapping("/dutySchedulingUsersController")
public class DutySchedulingUsersController {

	@Autowired
	private DutySchedulingUsersService dutySchedulingUsersService;
	@Resource
	private DutyConfigModelService dutyService;
	@Resource
	private DutyFlightConfigService dutyFlightService;
	@Resource
	private DutyUsersService dutyUserService;
	
	
	
	
	
	
	/**
	 * 获取反显用树图json
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="/getUserTreeJson", method=RequestMethod.POST)
	public Object getUserTreeJson(HttpServletRequest request) throws ParseException{
		String dutyTime = StaticMethod.nullObject2String(request.getParameter("dutyTime"));
		int dutyId = StaticMethod.nullObject2int(request.getParameter("dutyId"));
		int dutyFlightId = StaticMethod.nullObject2int(request.getParameter("dutyFlightId"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		JSONArray jsonarr = new JSONArray();
		JSONObject rejson = new JSONObject();
		String userName = "";
		List list = dutySchedulingUsersService.getDutySchedulingUserByConfigIdFlightIdDutyTime(dutyId,dutyFlightId,sdf.parse(dutyTime));
		if(list.size()>0){
			for(int i = 0;i<list.size();i++){//拼反显用的json数组 和 人员名字
				DutySchedulingUsers dsus = (DutySchedulingUsers) list.get(i);
				JSONObject json = new JSONObject();
				json.put("id", i+1);
				json.put("isParent", false);
				json.put("open", false);
				json.put("icon", "/assets/images/u36884.png");
				json.put("userId", dsus.getUserid());
				json.put("name", dsus.getUsername());
				if(i==0){
					userName += dsus.getUsername();
				}else{
					userName += ","+dsus.getUsername();
				}
					
				
				jsonarr.add(json);
			}
			rejson.put("userName",userName);
			rejson.put("userJson",jsonarr);
		}
		
		return rejson;
	}
	
	/**
	 * 保存SchedulingUsers
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="/saveDutySchedulingUsers", method=RequestMethod.POST)
	public Object saveDutySchedulingUsers(HttpServletRequest request) throws ParseException{
		String userId = StaticMethod.nullObject2String(request.getParameter("userIds"));
		String userName = StaticMethod.nullObject2String(request.getParameter("userNames"));
		String dutyHandoverType = StaticMethod.nullObject2String(request.getParameter("dutyHandoverType"));
		int dutyId = StaticMethod.nullObject2int(request.getParameter("dutyConfigId"));
		int dutyFlightId = StaticMethod.nullObject2int(request.getParameter("dutyFilghtId"));
		String dutyTime = StaticMethod.nullObject2String(request.getParameter("dutyTime"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DutySchedulingUsers dsus = new DutySchedulingUsers();
		dsus.setDutyConfigId(dutyId);
		dsus.setDutyFlightId(dutyFlightId);
		dsus.setDutyTime(sdf.parse(dutyTime));
		dutySchedulingUsersService.removeDutySchedulingUsersByCondition(dsus);
		String[] userNames = userName.split(",");
		String[] userIds = userId.split(",");
		for(int i=0;i<userIds.length;i++){
			DutySchedulingUsers dsu = new DutySchedulingUsers();
			dsu.setDeleteIf(0);
			dsu.setDutyConfigId(dutyId);
			dsu.setDutyFlightId(dutyFlightId);
			dsu.setDutyTime(sdf.parse(dutyTime));
			if(i==0){
				dsu.setHeadIf(1);
			}else{
				dsu.setHeadIf(0);
			}
			dsu.setUserid(userIds[i]);
			dsu.setUsername(userNames[i]);
			dsu.setDutyHandoverType(dutyHandoverType);
			dsu.setSchedulingId(0);//没什么用还得set的东西
			
			dutySchedulingUsersService.saveDutySchedulingUser(dsu);
		}
		JSONObject jjson = new JSONObject();
		jjson.put("dutyId",dutyId);
		return jjson;
	}
	
	
	/**
	 * 删除SchedulingUsers
	 * @param request
	 * @return
	 * @see 根据值班配置id,班次id,值班日期删除排班数据
	 */
	@ResponseBody
	@RequestMapping(value="/removeDutySchedulingUsers", method=RequestMethod.POST)
	public Object removeDutySchedulingUsers(HttpServletRequest request) {
		JSONObject jjson = new JSONObject();
		try {
			int dutyId = StaticMethod.nullObject2int(request.getParameter("dutyConfigId"));
			int dutyFlightId = StaticMethod.nullObject2int(request.getParameter("dutyFilghtId"));
			String dutyTime = StaticMethod.nullObject2String(request.getParameter("dutyTime"));
			Date dutyTimeDate = TimeUtil.getStringParseDate(dutyTime, 3);
			DutySchedulingUsers dsus = new DutySchedulingUsers();
			dsus.setDutyConfigId(dutyId);
			dsus.setDutyFlightId(dutyFlightId);
			dsus.setDutyTime(dutyTimeDate);
			dutySchedulingUsersService.removeDutySchedulingUsersByCondition(dsus);
			
			jjson.put("status",0); //删除成功
			return jjson;
		} catch (Exception e) {
			e.printStackTrace();
			jjson.put("status",1);
			return jjson;
		}
		
	}
	
	/**
	 * 获取排班人员树
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="/getDutyUserByFilght", method=RequestMethod.POST)
	public Object getDutyUserByFilght(HttpServletRequest request) throws ParseException{
		int dutyId = StaticMethod.nullObject2int(request.getParameter("rId"));
		List userList = dutyUserService.selectByDutyConfigId(dutyId);
		JSONArray jsonarr = new JSONArray();
		for(int i=0;i<userList.size();i++){
			DutyUsers du = (DutyUsers) userList.get(i);
			JSONObject jsonob = new JSONObject();
			String num = "1"+i;
			jsonob.put("id", StaticMethod.nullObject2int(num));
			jsonob.put("pId", 1);
			jsonob.put("isParent", false);
			jsonob.put("open", false);
			jsonob.put("icon", "/assets/images/u36884.png");
			jsonob.put("userId", du.getUserId());
			jsonob.put("name", du.getUserName());
			jsonarr.add(jsonob);
		}
		
		
		return jsonarr;
	}
	/**
	 * 根据选择的dutyconfigid查询配置信息   班次  相关人员 
	 * @param request
	 * @return
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping(value="/getDutyConfigBySelect", method=RequestMethod.POST)
	public Object getDutyConfigBySelect(HttpServletRequest request) throws ParseException{
		//获取dutyconfig表id
		int dutyConfigId = StaticMethod.nullObject2int(request.getParameter("selectValue"));
		String startTime = StaticMethod.nullObject2String(request.getParameter("startTime"));
		String endTime = StaticMethod.nullObject2String(request.getParameter("endTime"));
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//		Date stime  = sdf.parse(startTime);
//		Date etime  = sdf.parse(endTime);
//		int days = (int) ((etime.getTime() - stime.getTime()) / (1000*3600*24));//计算需要值班天数
		
		DutyConfigModel dmc = dutyService.selectByPrimaryKey(dutyConfigId);
		JSONArray configArr  = getConfigJson(dmc.getId(),dmc.getDutyConfigName(),dmc.getDutyHandoverType(),startTime,endTime);//获取返回dutyconfig数组
		List filghtList  = dutyFlightService.selectByDutyConfigId(dutyConfigId);
		JSONArray dfcArr = new JSONArray();
		for(int i=0;i<filghtList.size();i++){
			DutyFlightConfig dfc = (DutyFlightConfig) filghtList.get(i);
			JSONObject dfcjson = new JSONObject();
			dfcjson.put("dutyFlightId", dfc.getDutyFlightId());
			dfcjson.put("dutyFlightName", dfc.getDutyFlightName());
			dfcjson.put("dutyFlightBeginTime", dfc.getDutyFlightBegintime());
			dfcjson.put("dutyFlightEndTime", dfc.getDutyFlightEndtime());
			dfcjson.put("showId", "show"+dfc.getDutyFlightId());//用于页面展示排班人员spanid
			dfcjson.put("nodeId", "node"+dfc.getDutyFlightId());//页面存储排班人员信息node
			dfcArr.add(dfcjson);
		}
//		List userList = dutyUserService.selectByDutyConfigId(dutyConfigId);
//		for(int i=0;i<userList.size();i++){
//			DutyUsers dutyuser = new DutyUsers();
//			JSONObject dutyuserjson = new JSONObject();
//			dutyuserjson.put("dutyUserId", dutyuser.getDutyUsersId());
//			dutyuserjson.put("userId", dutyuser.getUserId());
//			dutyuserjson.put("userName", dutyuser.getUserName());
//		}
		JSONObject jsonob = new JSONObject();
		jsonob.put("dutyConfigArr", configArr);
		jsonob.put("dutyFilght", dfcArr); 
		
		return jsonob;
	}
	
	/**
	 * 获取返回排班管理页面用dutyconfig json数组
	 * @param dutyId
	 * @param dutyConfigName
	 * @param dutyType
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public JSONArray getConfigJson(int dutyId,String dutyConfigName,String dutyType,String startTime,String endTime) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//		startTime = "2017-09-20";
//		endTime = "2017-09-24";
		Date stime  = sdf.parse(startTime);
		Date etime  = sdf.parse(endTime);
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		dateFm.format(stime);
		int days = (int) ((etime.getTime() - stime.getTime()) / (1000*3600*24));
		Calendar c = Calendar.getInstance();  //时间操作
        c.setTime(stime);  
//        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天  
        JSONArray configarr = new JSONArray();
		for(int i =0;i<=days;i++){//拼写返回dutyconfig数组
			if(i==0){
				Date date  = c.getTime();
				JSONObject cjson = new JSONObject();
				cjson.put("week", dateFm.format(date));//星期几
				cjson.put("dutyTime", sdf.format(date));//值班时间
				cjson.put("dutyId",dutyId);//本值班配置id
				cjson.put("dutyConfigName", dutyConfigName);//本值班配置name
				cjson.put("dutyType", dutyType);//本值班配置类型
				c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天  
				configarr.add(cjson);
			}else{
				Date date  = c.getTime();
				JSONObject cjson = new JSONObject();
				cjson.put("week", dateFm.format(date));//星期几
				cjson.put("dutyTime", sdf.format(date));//值班时间
				cjson.put("dutyId",dutyId);//本值班配置id
				cjson.put("dutyConfigName", dutyConfigName);//本值班配置name
				cjson.put("dutyType", dutyType);//本值班配置类型
				c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天  
				configarr.add(cjson);
			}

			
		}
		return configarr;
	}
	
	/**
	 * 返回所有dutyconfig做select选项
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/showDutyConfigSelect", method=RequestMethod.POST)
	public Object showDutyConfigSelect(HttpServletRequest request){
		JSONArray jsonArr = new JSONArray();
		List list = dutyService.showAll();
		for(int i= 0;i<list.size();i++){
			JSONObject json = new JSONObject();
			DutyConfigModel dcm = (DutyConfigModel) list.get(i);
			json.put("ID", i);
			json.put("DICTID", dcm.getId());
			json.put("DICTNAME", dcm.getDutyConfigName());
			jsonArr.add(json);
		}
		
		return jsonArr;
	}
	
	/**
	 * 返回dutyuser做select选项
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/showDutyUserSelect", method=RequestMethod.POST)
	public Object showDutyUserSelect(HttpServletRequest request){
		int duty_config_id = StaticMethod.nullObject2int(request.getParameter("duty_config_id"));
		JSONArray jsonArr = new JSONArray();
		List list = dutyUserService.selectByDutyConfigId(duty_config_id);
		JSONObject json1 = new JSONObject();
		json1.put("ID", 999);
		json1.put("DICTID", 999);
		json1.put("DICTNAME", "全部");
		jsonArr.add(json1);
		for(int i= 0;i<list.size();i++){
			JSONObject json = new JSONObject();
			DutyUsers dcm = (DutyUsers) list.get(i);
			json.put("ID", i);
			json.put("DICTID", dcm.getUserId());
			json.put("DICTNAME", dcm.getUserName());
			jsonArr.add(json);
		}
		
		return jsonArr;
	}
	
	/**
	 * 新增
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/dutyschedulingusers", method=RequestMethod.POST)
	public Object saveDutySchedulingUsers(@RequestBody Map<String, Map<String,String>> map,
			HttpServletRequest request){
		try{
			Map<String, String> tmap = map.get("dutyschedulinguser");
			DutySchedulingUsers dutySchedulingUsers = new DutySchedulingUsers();
			dutySchedulingUsers.setDeleteIf(Integer.parseInt(tmap.get("duty_id")));
			dutySchedulingUsers.setDutyConfigId(Integer.parseInt(tmap.get("duty_config_id")));
			dutySchedulingUsers.setDutyFlightId(Integer.parseInt(tmap.get("duty_flight_id")));
			dutySchedulingUsers.setDutyHandoverType(tmap.get("duty_handover_type"));
			dutySchedulingUsers.setDutyId(Integer.parseInt(tmap.get("duty_id")));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dutySchedulingUsers.setDutyTime(sdf.parse(tmap.get("duty_time")));
			dutySchedulingUsers.setHeadIf(Integer.parseInt(tmap.get("head_if")));
			dutySchedulingUsers.setReason(tmap.get("reason"));
			dutySchedulingUsers.setSchedulingId(Integer.parseInt(tmap.get("scheduling_id")));
			dutySchedulingUsers.setUserid(tmap.get("user_id"));
			dutySchedulingUsers.setUsername(tmap.get("user_name"));
			int result = this.dutySchedulingUsersService.saveDutySchedulingUser(dutySchedulingUsers);
			JSONObject json = new JSONObject();
			json.put("id", result);
			return json;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/dutyschedulingusers", method=RequestMethod.GET)
	public Object queryDutySchedulingUsers(
			//@RequestBody Map<String, Integer> map,
			HttpServletRequest request){
		try{
			//Integer dutyId = map.get("duty_id");
			Integer dutyId = Integer.parseInt(request.getParameter("duty_id"));
			//System.out.println(dutyId);
			DutySchedulingUsers dutySchedulingUsers = new DutySchedulingUsers();
			dutySchedulingUsers.setDutyId(dutyId);
			DutySchedulingUsers result = this.dutySchedulingUsersService.queryDutySchedulingUsers(dutySchedulingUsers);
			JSONObject json = new JSONObject();
			json.put("id", result);
			return json;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 更新
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/dutyschedulingusers", method=RequestMethod.PUT)
	public Object modifyDutySchedulingUsers(@RequestBody Map<String, Map<String, String>> map,
			HttpServletRequest request){
		try{
			Map<String, String> tmap = map.get("dutyschedulinguser");
			DutySchedulingUsers dutySchedulingUsers = new DutySchedulingUsers();
			dutySchedulingUsers.setDeleteIf(Integer.parseInt(tmap.get("duty_id")));
			dutySchedulingUsers.setDutyConfigId(Integer.parseInt(tmap.get("duty_config_id")));
			dutySchedulingUsers.setDutyFlightId(Integer.parseInt(tmap.get("duty_flight_id")));
			dutySchedulingUsers.setDutyHandoverType(tmap.get("duty_handover_type"));
			dutySchedulingUsers.setDutyId(Integer.parseInt(tmap.get("duty_id")));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dutySchedulingUsers.setDutyTime(sdf.parse(tmap.get("duty_time")));
			dutySchedulingUsers.setHeadIf(Integer.parseInt(tmap.get("head_if")));
			dutySchedulingUsers.setReason(tmap.get("reason"));
			dutySchedulingUsers.setSchedulingId(Integer.parseInt(tmap.get("scheduling_id")));
			dutySchedulingUsers.setUserid(tmap.get("user_id"));
			dutySchedulingUsers.setUsername(tmap.get("user_name"));
			int result = this.dutySchedulingUsersService.modifyDutySchedulingUsers(dutySchedulingUsers);
			JSONObject json = new JSONObject();
			json.put("id", result);
			return json;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 删除
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/dutyschedulingusers", method=RequestMethod.DELETE)
	public Object removeDutySchedulingUsers(@RequestBody Map<String, Integer> map,
			HttpServletRequest request){
		try{
			Integer dutyId = map.get("duty_id");
			int result = this.dutySchedulingUsersService.removeDutySchedulingUsers(dutyId);
			JSONObject json = new JSONObject();
			json.put("id", result);
			return json;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**
	 * @see 排班查询导出
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/exportSchedulingExcel")
	public void exportSchedulingExcel(HttpServletRequest request,HttpServletResponse response) throws Exception {
		int dutyConfigId = StaticMethod.nullObject2int(request.getParameter("dutyConfigId"));
		String startTime = StaticMethod.nullObject2String(request.getParameter("startTime"));
		String endTime = StaticMethod.nullObject2String(request.getParameter("endTime"));
		//查询值班配置
		List<DutyFlightConfig> filghtList  = dutyFlightService.selectByDutyConfigId(dutyConfigId);
		String sheetName = "值班表";
		String note = "值班表"; //标题
	    if (!"".equals(startTime) && !"".equals(endTime)) {
	    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
	    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");  
			Date startTimeDate1 = sdf1.parse(startTime);  
			Date endTimeDate1 = sdf1.parse(endTime); 
			String startTime1 = sdf2.format(startTimeDate1);
			String endTime1 = sdf2.format(endTimeDate1);
			note = startTime1+"至"+endTime1+"值班表";
		}
		String comma[]=new String[filghtList.size()+4];//模板第一列数据
		int dutyflightids[] = new int[filghtList.size()+4];//根据第一列数据创建的班次id
		comma[0]="日   期";
		comma[1]="星   期";
		dutyflightids[0]=0;
		dutyflightids[1]=0;
		//拼接模板第一列
		for(int i=0;i<filghtList.size();i++){
			DutyFlightConfig filghtMapConfig = filghtList.get(i);
			String dutyflightname = filghtMapConfig.getDutyFlightName();
			int dutyflightid = filghtMapConfig.getDutyFlightId();
			String flightBegintime = filghtMapConfig.getDutyFlightBegintime();
			String flightEndtime = filghtMapConfig.getDutyFlightEndtime();
			String flighTitleString = dutyflightname+"("+flightBegintime+"-"+flightEndtime+")";
			comma[i+2]=flighTitleString;
			dutyflightids[i+2]=dutyflightid;
		}
		//第一行日期数据
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		Date startTimeDate = sdf.parse(startTime);  
		Date endTimeDate = sdf.parse(endTime); 
		//开始时间到结束时间的全部时间
		List<Date> listDate = getDatesBetweenTwoDate(startTimeDate, endTimeDate);
		//第二行星期数据
		String[] weekDaysName = { "日", "一", "二", "三", "四", "五", "六" };
		//定义导出Excel表头
		ExcelExportUtil exUtil = new ExcelExportUtil();
		String[] titleName = new String[0];
		//定义导出表sheet页名称
		HSSFSheet excelSheet = exUtil.info(sheetName, titleName);
		//获取wb
		HSSFWorkbook wb = exUtil.getHSSFWorkbook();
		//定义样式
		HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中  
        HSSFFont headerFont = (HSSFFont) wb.createFont(); //创建字体样式  
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗  
        headerFont.setFontName("宋体");  //设置字体类型  
        headerFont.setFontHeightInPoints((short) 11);    //设置字体大小  
        style.setFont(headerFont);
        style.setWrapText(true);
        //定义样式1
        HSSFCellStyle style1 = wb.createCellStyle();  
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中  
        HSSFFont headerFont1 = (HSSFFont) wb.createFont(); //创建字体样式  
        headerFont1.setFontName("宋体");  //设置字体类型  
        headerFont1.setFontHeightInPoints((short) 10);    //设置字体大小  
        style1.setFont(headerFont1);
        style1.setWrapText(true);
		//查询要导出的数据
		HSSFRow row = null;
		//循环取数据
		//行数由模板第一列数据宽度确定
		for(int i = 0;i <= comma.length ;i++){  
			row = excelSheet.createRow(i); 
			//创建单元格，并设置值  
			if(i == 0){ //第一行标题
				//列数由时间确定
				for (int j = 0; j < listDate.size()+1; j++) {
					HSSFCell cell = row.createCell((short) j);
					if (j == 0) {//第一列
						cell.setCellValue(note);
						cell.setCellStyle(style);
					}else {
						cell.setCellValue(""); 
						cell.setCellStyle(style); 
					}
				}
			}else if (i == 1) { //第二行日期
				//列数由时间确定
				for (int j = 0; j < listDate.size()+1; j++) {
					HSSFCell cell = row.createCell((short) j);
					if (j == 0) {//第一列
						excelSheet.setColumnWidth(0, 5000); //设置单元格宽度
						cell.setCellValue(comma[i-1]);
						cell.setCellStyle(style1);
					}else {
						excelSheet.setColumnWidth(j, 3000); //设置单元格宽度
						String time=sdf.format(listDate.get(j-1));
						String dayDate = time.split("-")[2];
						cell.setCellValue(dayDate); 
						cell.setCellStyle(style1); 
					}
				}
			}else if (i == 2) { //第三行星期
				//列数由时间确定
				for (int j = 0; j < listDate.size()+1; j++) {
					HSSFCell cell = row.createCell((short) j);
					if (j == 0) {//第一列
						cell.setCellValue(comma[i-1]);
						cell.setCellStyle(style1);
					}else {
						String time=sdf.format(listDate.get(j-1));  
						Date week=sdf.parse(time);
					    Calendar calendar = Calendar.getInstance();
					    calendar.setTime(week);
					    int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; 
					    String weektime=weekDaysName[intWeek];
					    cell.setCellValue(weektime); 
					    cell.setCellStyle(style1); 
					}
				}
			}else {
				int dutyflightid= dutyflightids[i-1];
				//列数由时间确定
				for (int j = 0; j < listDate.size()+1; j++) {
					HSSFCell cell = row.createCell((short) j);
					if (j == 0) {//第一列
						cell.setCellValue(comma[i-1]);
						cell.setCellStyle(style1);
					}else {
						Date dutyTime=listDate.get(j-1);
						//获取本班次值班信息
						List<Map<String,Object>> now_duty_data = dutySchedulingUsersService.selectNowDutyByduty(dutyflightid, dutyConfigId, dutyTime);
						String now_user_name = "";
						if (now_duty_data.size()>0) {
							now_user_name = StaticMethod.nullObject2String(now_duty_data.get(0).get("USERNAMES"));
						}
						cell.setCellValue(now_user_name); 
						cell.setCellStyle(style1);
					}
				}
			}
		}
		//第一行合并
		int  columnWidth = listDate.size();
		CellRangeAddress cra=new CellRangeAddress(0, 0, 0, columnWidth); 
		excelSheet.addMergedRegion(cra);
		exUtil.setData(excelSheet);
		//定义导出模板名称
		String fileName = "值班表-"+TimeUtil.getCurrentTimePure().substring(0, 8)+".xls";
		exUtil.ExportExcel(fileName, response);
	}
	
	/**
	 * @see 第一版值班下载excel模板
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/downloadTemplateOld")
	public void oldDownloadTemplate(HttpServletRequest request,HttpServletResponse response) throws Exception {
		int dutyConfigId = StaticMethod.nullObject2int(request.getParameter("dutyConfigId"));
		String startTime = StaticMethod.nullObject2String(request.getParameter("startTime"));
		String endTime = StaticMethod.nullObject2String(request.getParameter("endTime"));
		//查询值班配置
		List<DutyFlightConfig> filghtList  = dutyFlightService.selectByDutyConfigId(dutyConfigId);
		String comma[]=new String[filghtList.size()+4];//模板表头
		String sheetName = "值班导入-"+TimeUtil.getCurrentTimePure().substring(0, 7);
		//模板表头数据
		comma[0]="日期";
		comma[1]="星期";
		comma[filghtList.size()+2]="值班配置(不可更改)";
		comma[filghtList.size()+3]="可选人员";
		//拼接模板表头
		for(int i=0;i<filghtList.size();i++){
			DutyFlightConfig filghtMapConfig = filghtList.get(i);
			String dutyflightname = filghtMapConfig.getDutyFlightName();
			String flightBegintime = filghtMapConfig.getDutyFlightBegintime();
			String flightEndtime = filghtMapConfig.getDutyFlightEndtime();
			String flighTitleString = dutyflightname+"("+flightBegintime+"-"+flightEndtime+")";
			comma[i+2]=flighTitleString;
		}
		//定义导出Excel表头
		ExcelExportUtil exUtil = new ExcelExportUtil();
		String[] titleName = comma;
		//定义导出表sheet页名称
		HSSFSheet excelSheet = exUtil.info(sheetName, titleName);
		//查询要导出的数据
		HSSFRow row =null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        Date startTimeDate = sdf.parse(startTime);  
        Date endTimeDate = sdf.parse(endTime); 
        //开始时间到结束时间的全部时间
        List<Date> listDate = getDatesBetweenTwoDate(startTimeDate, endTimeDate);  
		//循环取数据
	    for(int i=0;i<listDate.size();i++){  
			row = excelSheet.createRow((int) i + 2); 
			String time=sdf.format(listDate.get(i));  
			Date week=sdf.parse(time);
		 	String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(week);
		    int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; 
		    String weektime=weekDaysName[intWeek];
	        //设置单元格宽度
	        excelSheet.setColumnWidth(0, 3000);
	        excelSheet.setColumnWidth(1, 2000);
	        for(int j=2;j<filghtList.size()+2;j++){
	        	excelSheet.setColumnWidth(j, 6500);
	        }
	        //值班配置宽度设置
	        excelSheet.setColumnWidth(filghtList.size()+2, 5000);
			//创建单元格，并设置值  
		    if(i==0){
		    	row.createCell((short) 0).setCellValue(time);  
		        row.createCell((short) 1).setCellValue(weektime);
		        //值班配置数据保存至excel
		        row.createCell((short) filghtList.size()+2).setCellValue(dutyConfigId);
		        row = excelSheet.createRow(i); 
		        row.createCell((short) 0).setCellValue("注意事项：多人值班请以英文“,”分隔(例如：张三,李四)，默认第一个人为组长(”张三“为组长)。");
		    }else{
		    	row.createCell((short) 0).setCellValue(time);  
		        row.createCell((short) 1).setCellValue(weektime);
		    }
        }
	    //合并
	    CellRangeAddress cra=new CellRangeAddress(0, 0, 0, filghtList.size()+2); 
        excelSheet.addMergedRegion(cra);
	    //模板可选人员数据
        List<DutyUsers> userList = dutyUserService.selectByDutyConfigId(dutyConfigId);
		for(int k=0;k<userList.size();k++){
			if(k+1>listDate.size()){
				row = excelSheet.createRow(k + 2); 
			}else{
				row = excelSheet.getRow(k+2); 
			}
			DutyUsers dutyUserModel=userList.get(k);
			String username = dutyUserModel.getUserName();
			row.createCell((short) filghtList.size()+3).setCellValue(username);  
		}
		exUtil.setData(excelSheet);
		//定义导出模板名称
		String fileName = "值班导入-"+TimeUtil.getCurrentTimePure().substring(0, 8)+".xls";
		exUtil.ExportExcel(fileName, response);
	}
	
	
	
	//开始时间和结束时间，自动计算中间时间
	public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {  
        List<Date> lDate = new ArrayList<Date>();  
        lDate.add(beginDate);// 把开始时间加入集合  
        Calendar cal = Calendar.getInstance();  
        // 使用给定的 Date 设置此 Calendar 的时间  
        cal.setTime(beginDate);  
        boolean bContinue = true;  
        while (bContinue) {  
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量  
            cal.add(Calendar.DAY_OF_MONTH, 1);  
            // 测试此日期是否在指定日期之后  
            if (endDate.after(cal.getTime())) {  
                lDate.add(cal.getTime());  
            } else {  
                break;  
            }  
        }  
        lDate.add(endDate);// 把结束时间加入集合  
        return lDate;  
    }
	
	
	/**
	 * @see 第一版值班导入功能
	 * @param request
	 * @param link
	 * @param modelPath
	 * @param beanName
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value = "/importExcelToSchedulingold", method = RequestMethod.POST)
	public Object importExcelToSchedulingold(HttpServletRequest request,String filePath) {
		if ("".equals(filePath)) {
			filePath = StaticMethod.nullObject2String(request.getParameter("filePath"));
		}
		HSSFWorkbook wb=ExcelImportUtil.getWorkbookByFile(filePath);
		//删除空行
		ExcelImportUtil.deleteNullRowAllSheet(wb);
		//第一个sheet页
		HSSFSheet sheet = null;
		sheet = wb.getSheetAt(0);
		//Excel表格里的列数
		int coloumNum = sheet.getRow(1).getLastCellNum();
		//Excel表格里的行数
		int rowNum = sheet.getLastRowNum();
		//Excel表格里第一列的行数
		int oneColoumRowNum = 0;
		//获取值班配置id
		String duty_config_idString = sheet.getRow(2).getCell(coloumNum-2).toString();
		duty_config_idString = duty_config_idString.substring(0, duty_config_idString.length()-2);
		int duty_config_id = Integer.valueOf(duty_config_idString);
		//获取值班配置信息
		DutyConfigModel dmc = dutyService.selectByPrimaryKey(duty_config_id);
		String dutyHandoverType = dmc.getDutyHandoverType();
		//查询值班配置
		List<DutyFlightConfig> filghtList  = dutyFlightService.selectByDutyConfigId(duty_config_id);
		String startTime = "";
		String endTime = "";
		//获取Excel里的排班开始时间与结束时间
		for(int i = 2;i <= rowNum+2;i++){
			 if(sheet.getRow(i)!=null){
				 HSSFCell duty_time = sheet.getRow(i).getCell(0);
				 if(duty_time==null){
					 startTime = sheet.getRow(2).getCell(0).toString();
					 endTime = sheet.getRow(i-1).getCell(0).toString();
					 break;
				 } 
			 }else{
				 startTime = sheet.getRow(2).getCell(0).toString();
				 endTime = sheet.getRow(i-1).getCell(0).toString();
				 break; 
			 }
			 oneColoumRowNum = i;
		}
		Date startTimeDate = TimeUtil.getStringParseDate(startTime, 3);
		Date endTimeDate = TimeUtil.getStringParseDate(endTime, 3);
		//数据保存
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for(int i=2;i <= oneColoumRowNum;i++){ //i控制行数
			//获取值班日期
			 String duty_time = sheet.getRow(i).getCell(0).toString();
			 Date duty_timeDate = TimeUtil.getStringParseDate(duty_time, 3);
			 for(int j=2;j<coloumNum-2;j++){ //j控制列数
				 //获取班次id
				 DutyFlightConfig dfc = filghtList.get(j-2);
				 int duty_flight_id = dfc.getDutyFlightId();
				 
				 //获取值班人员信息
				 String duty_users = sheet.getRow(i).getCell(j).toString();
				 String[] userStrings =  duty_users.split(",");
				 for (int k = 0; k < userStrings.length; k++) {
					String user_name = userStrings[k];
					List<DutyUsers>  listUsers = dutyUserService.selectByUserName(user_name,duty_config_id);
					String user_id = listUsers.get(0).getUserId();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userid", user_id);
					map.put("username", user_name);
					map.put("dutyflightid", duty_flight_id);
					map.put("dutyconfigid", duty_config_id);
					map.put("schedulingid", 0);
					map.put("deleteif", 0);
					map.put("reason", "");
					map.put("dutytime", duty_timeDate);
					if (k == 0) {
						map.put("headif", 1);
					}else {
						map.put("headif", 0);
					}
					map.put("dutyhandovertype", dutyHandoverType);
					map.put("remark", "");
					dataList.add(map);
				 }
			 }
		}
		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("dutyconfigid", duty_config_id);
		condition.put("startTime", startTimeDate);
		condition.put("endTime", endTimeDate);
		//删除时间范围内已经排班的数据及批量插入新的排班数据
		dutySchedulingUsersService.removeAndSaveDutySchedulingUsers(condition,dataList);
		JSONObject  judgeObject = new JSONObject();
		judgeObject.put("status", 0);
		return judgeObject;
	}
	
	/**
	 * @see 值班导入功能校验数据
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/excelImportCheckold", method = RequestMethod.POST)
	public JSONArray excelToVoForImportold(HttpServletRequest request) {
		//返回结果
		JSONArray arry = new JSONArray();
		JSONObject  judgeObject = new JSONObject();
		try {
			//获取上传文件路径
			String filePath = StaticMethod.getPath(AttachmentStaticVariable.FILEPATH, "fileUrl.duty");
			FileHandingUtil fileHandingUtil = new FileHandingUtil();
			List<HashMap<String, String>> list = fileHandingUtil.dealUpload(request,filePath);
			if(list != null){
				//获取对应的全部文件路径
				String filePath1 = list.get(0).get("path");
				HSSFWorkbook wb=ExcelImportUtil.getWorkbookByFile(filePath1);
				//删除空行
				ExcelImportUtil.deleteNullRowAllSheet(wb);
				//第一个sheet页
				HSSFSheet sheet = null;
				sheet = wb.getSheetAt(0);
				//Excel表格里的列数
				int coloumNum = sheet.getRow(1).getLastCellNum();
				//Excel表格里的行数
				int rowNum = sheet.getLastRowNum();
				//Excel表格里第一列的行数
				int oneColoumRowNum = 0;
				String startTime = "";
				String endTime = "";
				//获取Excel里的排班开始时间与结束时间
				for(int i = 2;i <= rowNum+2;i++){
					 if(sheet.getRow(i)!=null){
						 HSSFCell duty_time = sheet.getRow(i).getCell(0);
						 if(duty_time==null){
							 startTime = sheet.getRow(2).getCell(0).toString();
							 endTime = sheet.getRow(i-1).getCell(0).toString();
							 break;
						 } 
					 }else{
						 startTime = sheet.getRow(2).getCell(0).toString();
						 endTime = sheet.getRow(i-1).getCell(0).toString();
						 break; 
					 }
					 oneColoumRowNum = i;
				}
				//获取值班配置id
				String duty_config_idString = String.valueOf(sheet.getRow(2).getCell(coloumNum-2).getNumericCellValue());
				duty_config_idString = duty_config_idString.substring(0, duty_config_idString.length()-2);
				int duty_config_id = Integer.valueOf(duty_config_idString);
				
				//校验值班人员是否是值班配置中的人员
				for(int i=2;i <= oneColoumRowNum;i++){ //i控制行数
					
					 for(int j=2;j<coloumNum-2;j++){ //j控制列数
						 HSSFCell duty_userCell = sheet.getRow(i).getCell(j);
						 if (duty_userCell != null) {
							String duty_users = duty_userCell.toString();
							String[] userStrings =  duty_users.split(",");
							for (int k = 0; k < userStrings.length; k++) {
								String user_name = userStrings[k]; //excel中的值班人员
								//校验值班人员是否在值班配置中
								List<DutyUsers> userList = dutyUserService.selectByUserName(user_name,duty_config_id);
								if (userList.size() <= 0) {
									judgeObject.put("status", 1);
									String messageString = "第"+(i+1)+"行第"+(j+1)+"列,您填写的值班人员不正确!";
									judgeObject.put("message", messageString);
									arry.add(judgeObject);
									fileHandingUtil.deleteFile(filePath1);
									return arry;
								}
								
							}
						 }else {
							judgeObject.put("status", 1);
							judgeObject.put("message", "您没有填写值班人员!");
							arry.add(judgeObject);
							fileHandingUtil.deleteFile(filePath1);
							return arry;
						 }
					 }
				}
				//校验时间范围内是否已经排过班
				Date startTimeDate = TimeUtil.getStringParseDate(startTime, 3);
				Date endTimeDate = TimeUtil.getStringParseDate(endTime, 3);
				//根据时间获取排班数据
				List<Map<String, Object>> list2 = dutySchedulingUsersService.selectByConfigIdandDutyTime(duty_config_id,startTimeDate,endTimeDate);
				if (list2.size()>0) {
					judgeObject.put("status", 2);
					judgeObject.put("filePath", filePath1);
					arry.add(judgeObject);
					return arry;
				}else {
					//excel没问题,排班数据插入.
					importExcelToScheduling(request,filePath1);
				}
			}
			judgeObject.put("status", 0);
			judgeObject.put("message","导入成功!");
			arry.add(judgeObject);
			return arry;
			 
		 } catch (Exception e) {
			 e.printStackTrace();
			 judgeObject.put("status", 1);
			 judgeObject.put("message","导入失败!");
			 arry.add(judgeObject);
			 return arry;
		 }
		
		
	}
	
	
	/**
	 * @see 值班下载excel模板
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/downloadTemplate")
	public void DownloadTemplate(HttpServletRequest request,HttpServletResponse response) throws Exception {
		int dutyConfigId = StaticMethod.nullObject2int(request.getParameter("dutyConfigId"));
		String startTime = StaticMethod.nullObject2String(request.getParameter("startTime"));
		String endTime = StaticMethod.nullObject2String(request.getParameter("endTime"));
		//查询值班配置
		List<DutyFlightConfig> filghtList  = dutyFlightService.selectByDutyConfigId(dutyConfigId);
		String sheetName = "值班导入-"+TimeUtil.getCurrentTimePure().substring(0, 7);
		String note = "注意事项：多人值班请以英文“,”分隔(例如：张三,李四)，默认第一个人为组长(”张三“为组长)。"; //注意事项
		String comma[]=new String[filghtList.size()+4];//模板第一列数据
		comma[0]="日   期";
		comma[1]="星   期";
		comma[filghtList.size()+2]="可选人员";
		comma[filghtList.size()+3]="值班配置(不可更改)";
		//拼接模板第一列
		for(int i=0;i<filghtList.size();i++){
			DutyFlightConfig filghtMapConfig = filghtList.get(i);
			String dutyflightname = filghtMapConfig.getDutyFlightName();
			String flightBegintime = filghtMapConfig.getDutyFlightBegintime();
			String flightEndtime = filghtMapConfig.getDutyFlightEndtime();
			String flighTitleString = dutyflightname+"("+flightBegintime+"-"+flightEndtime+")";
			comma[i+2]=flighTitleString;
		}
		//第一行日期数据
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		Date startTimeDate = sdf.parse(startTime);  
		Date endTimeDate = sdf.parse(endTime); 
		//开始时间到结束时间的全部时间
		List<Date> listDate = getDatesBetweenTwoDate(startTimeDate, endTimeDate);
		//第二行星期数据
		String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		//模板可选人员数据
		List<DutyUsers> userList = dutyUserService.selectByDutyConfigId(dutyConfigId);
		//定义导出Excel表头
		ExcelExportUtil exUtil = new ExcelExportUtil();
		String[] titleName = new String[0];
		//定义导出表sheet页名称
		HSSFSheet excelSheet = exUtil.info(sheetName, titleName);
		//查询要导出的数据
		HSSFRow row = null;
		//循环取数据
		//行数由模板第一列数据宽度确定
		for(int i = 0;i <= comma.length ;i++){  
			row = excelSheet.createRow(i); 
			//创建单元格，并设置值  
			if(i == 0){ //第一行 注意事项
				row.createCell((short) 0).setCellValue(note);
			}else if (i == 1) { //第二行日期
				//行数由时间确定
				for (int j = 0; j < listDate.size()+1; j++) {
					if (j == 0) {//第一列
						excelSheet.setColumnWidth(0, 4500); //设置单元格宽度
						row.createCell((short) 0).setCellValue(comma[i-1]);
					}else {
						excelSheet.setColumnWidth(j, 3000); //设置单元格宽度
						String time=sdf.format(listDate.get(j-1));
						row.createCell((short) j).setCellValue(time); 
					}
				}
			}else if (i == 2) { //第三行星期
				//行数由时间确定
				for (int j = 0; j < listDate.size()+1; j++) {
					if (j == 0) {//第一列
						row.createCell((short) 0).setCellValue(comma[i-1]);
					}else {
						String time=sdf.format(listDate.get(j-1));  
						Date week=sdf.parse(time);
					    Calendar calendar = Calendar.getInstance();
					    calendar.setTime(week);
					    int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; 
					    String weektime=weekDaysName[intWeek];
					    row.createCell((short) j).setCellValue(weektime); 
					}
				}
			}else if (i == comma.length - 1) {//模板可选人员数据
				row.createCell((short) 0).setCellValue(comma[i-1]);
				for(int k = 1;k<userList.size();k++){
					DutyUsers dutyUserModel=userList.get(k-1);
					String username = dutyUserModel.getUserName();
					row.createCell((short) k).setCellValue(username);  
				}
			}else if (i == comma.length) { //值班配置
				row.createCell((short) 0).setCellValue(comma[i-1]);
				row.createCell((short) 1).setCellValue(dutyConfigId+"");
			}else {
				//行数由时间确定
				for (int j = 0; j < listDate.size()+1; j++) {
					if (j == 0) {//第一列
						row.createCell((short) 0).setCellValue(comma[i-1]);
					}else {
						row.createCell((short) j).setCellValue(""); 
					}
				}
			}
		}
		//第一行合并
		int  columnWidth = (listDate.size() > userList.size())?listDate.size()-1:userList.size()-1;
		CellRangeAddress cra=new CellRangeAddress(0, 0, 0, columnWidth); 
		excelSheet.addMergedRegion(cra);
		exUtil.setData(excelSheet);
		//定义导出模板名称
		String fileName = "值班导入-"+TimeUtil.getCurrentTimePure().substring(0, 8)+".xls";
		exUtil.ExportExcel(fileName, response);
	}
	
	
	/**
	 * @see 值班导入功能
	 * @param request
	 * @param link
	 * @param modelPath
	 * @param beanName
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value = "/importExcelToScheduling", method = RequestMethod.POST)
	public Object importExcelToScheduling(HttpServletRequest request,String filePath) {
		if ("".equals(filePath)) {
			filePath = StaticMethod.nullObject2String(request.getParameter("filePath"));
		}
		HSSFWorkbook wb=ExcelImportUtil.getWorkbookByFile(filePath);
		//删除空行
		ExcelImportUtil.deleteNullRowAllSheet(wb);
		//第一个sheet页
		HSSFSheet sheet = null;
		sheet = wb.getSheetAt(0);
		//Excel表格里的列数
		int coloumNum = sheet.getRow(1).getLastCellNum();
		//Excel表格里的行数
		int rowNum = sheet.getLastRowNum();
		//Excel表格里第一行的列
		int oneRowColoumNum = 0;
		//获取值班配置id
		//获取值班配置id
		String duty_config_idString = String.valueOf(sheet.getRow(rowNum).getCell(1));
		int duty_config_id = Integer.valueOf(duty_config_idString);
		//获取值班配置信息
		DutyConfigModel dmc = dutyService.selectByPrimaryKey(duty_config_id);
		String dutyHandoverType = dmc.getDutyHandoverType();
		//查询值班配置
		List<DutyFlightConfig> filghtList  = dutyFlightService.selectByDutyConfigId(duty_config_id);
		String startTime = "";
		String endTime = "";
		//获取Excel里的排班开始时间与结束时间
		HSSFRow daterow = sheet.getRow(1);//日期行
		for(int i = 1;i <= coloumNum;i++){
			 HSSFCell duty_time = daterow.getCell(i);
			 startTime = daterow.getCell(1).toString();
			 if(duty_time != null){
				 endTime =  daterow.getCell(i).toString();
			 }else{
				 endTime = daterow.getCell(i-1).toString();
				 break;
			 }
		}
		Date startTimeDate = TimeUtil.getStringParseDate(startTime, 3);
		Date endTimeDate = TimeUtil.getStringParseDate(endTime, 3);
		//数据保存
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for(int i=3;i <= rowNum-2;i++){ //i控制行数
			 for(int j=1;j<coloumNum;j++){ //j控制列数
				 //获取值班日期
				 String duty_time = sheet.getRow(1).getCell(j).toString();
				 Date duty_timeDate = TimeUtil.getStringParseDate(duty_time, 3);
				 //获取班次id
				 DutyFlightConfig dfc = filghtList.get(i-3);
				 int duty_flight_id = dfc.getDutyFlightId();
				 
				 //获取值班人员信息
				 String duty_users = sheet.getRow(i).getCell(j).toString();
				 String[] userStrings =  duty_users.split(",");
				 for (int k = 0; k < userStrings.length; k++) {
					String user_name = userStrings[k];
					List<DutyUsers>  listUsers = dutyUserService.selectByUserName(user_name,duty_config_id);
					String user_id = listUsers.get(0).getUserId();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userid", user_id);
					map.put("username", user_name);
					map.put("dutyflightid", duty_flight_id);
					map.put("dutyconfigid", duty_config_id);
					map.put("schedulingid", 0);
					map.put("deleteif", 0);
					map.put("reason", "");
					map.put("dutytime", duty_timeDate);
					if (k == 0) {
						map.put("headif", 1);
					}else {
						map.put("headif", 0);
					}
					map.put("dutyhandovertype", dutyHandoverType);
					map.put("remark", "");
					dataList.add(map);
				 }
			 }
		}
		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("dutyconfigid", duty_config_id);
		condition.put("startTime", startTimeDate);
		condition.put("endTime", endTimeDate);
		//删除时间范围内已经排班的数据及批量插入新的排班数据
		dutySchedulingUsersService.removeAndSaveDutySchedulingUsers(condition,dataList);
		JSONObject  judgeObject = new JSONObject();
		judgeObject.put("status", 0);
		return judgeObject;
	}
	
	/**
	 * @see 值班导入功能校验数据
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/excelImportCheck", method = RequestMethod.POST)
	public JSONArray excelToVoForImport(HttpServletRequest request) {
		//返回结果
		JSONArray arry = new JSONArray();
		JSONObject  judgeObject = new JSONObject();
		try {
			//获取上传文件路径
			String filePath = StaticMethod.getPath(AttachmentStaticVariable.FILEPATH, "fileUrl.duty");
			FileHandingUtil fileHandingUtil = new FileHandingUtil();
			List<HashMap<String, String>> list = fileHandingUtil.dealUpload(request,filePath);
			if(list != null){
				//获取对应的全部文件路径
				String filePath1 = list.get(0).get("path");
				HSSFWorkbook wb=ExcelImportUtil.getWorkbookByFile(filePath1);
				//删除空行
				ExcelImportUtil.deleteNullRowAllSheet(wb);
				//第一个sheet页
				HSSFSheet sheet = null;
				sheet = wb.getSheetAt(0);
				//Excel表格里的列数
				int coloumNum = sheet.getRow(1).getLastCellNum();
				//Excel表格里的行数
				int rowNum = sheet.getLastRowNum();
				//Excel表格里第一列的行数
				String startTime = "";
				String endTime = "";
				//获取Excel里的排班开始时间与结束时间
				HSSFRow daterow = sheet.getRow(1);//日期行
				for(int i = 1;i <= coloumNum;i++){
					 HSSFCell duty_time = daterow.getCell(i);
					 startTime = daterow.getCell(1).toString();
					 if(duty_time != null){
						 endTime =  daterow.getCell(i).toString();
					 }else{
						 endTime = daterow.getCell(i-1).toString();
						 break;
					 }
				}
				//获取值班配置id
				String duty_config_idString = String.valueOf(sheet.getRow(rowNum).getCell(1));
				int duty_config_id = Integer.valueOf(duty_config_idString);
				
				//校验值班人员是否是值班配置中的人员
				for(int i=3;i <= rowNum-2;i++){ //i控制行数
					
					 for(int j=1;j< coloumNum;j++){ //j控制列数
						 HSSFCell duty_userCell = sheet.getRow(i).getCell(j);
						 String duty_user = duty_userCell.toString();
						 if (duty_userCell != null && (!"".equals(duty_user))) {
							String duty_users = duty_userCell.toString();
							String[] userStrings =  duty_users.split(",");
							for (int k = 0; k < userStrings.length; k++) {
								String user_name = userStrings[k]; //excel中的值班人员
								//校验值班人员是否在值班配置中
								List<DutyUsers> userList = dutyUserService.selectByUserName(user_name,duty_config_id);
								if (userList.size() <= 0) {
									judgeObject.put("status", 1);
									String messageString = "第"+(i+1)+"行第"+(j+1)+"列,您填写的值班人员不正确!";
									judgeObject.put("message", messageString);
									arry.add(judgeObject);
									fileHandingUtil.deleteFile(filePath1);
									return arry;
								}
								
							}
						 }else {
							judgeObject.put("status", 1);
							judgeObject.put("message", "您没有填写值班人员!");
							arry.add(judgeObject);
							fileHandingUtil.deleteFile(filePath1);
							return arry;
						 }
					 }
				}
				//校验时间范围内是否已经排过班
				Date startTimeDate = TimeUtil.getStringParseDate(startTime, 3);
				Date endTimeDate = TimeUtil.getStringParseDate(endTime, 3);
				//根据时间获取排班数据
				List<Map<String, Object>> list2 = dutySchedulingUsersService.selectByConfigIdandDutyTime(duty_config_id,startTimeDate,endTimeDate);
				if (list2.size()>0) {
					judgeObject.put("status", 2);
					judgeObject.put("filePath", filePath1);
					arry.add(judgeObject);
					return arry;
				}else {
					//excel没问题,排班数据插入.
					importExcelToScheduling(request,filePath1);
				}
			}
			judgeObject.put("status", 0);
			judgeObject.put("message","导入成功!");
			arry.add(judgeObject);
			return arry;
			 
		 } catch (Exception e) {
			 e.printStackTrace();
			 judgeObject.put("status", 1);
			 judgeObject.put("message","导入失败!");
			 arry.add(judgeObject);
			 return arry;
		 }
	}
}
