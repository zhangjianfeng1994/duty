package com.boco.eoms.shiftAlarmAct.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.commons.BaseUtil;
import com.boco.eoms.shiftAlarmAct.model.ShiftAlarmactModel;
import com.boco.eoms.shiftAlarmAct.service.ShiftAlarmactService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


@Controller
@RequestMapping(value = "shiftAlarmact")
public class ShiftAlarmactController {
	
	
	private static final Logger logger = Logger.getLogger(ShiftAlarmactController.class);
	
	public static Logger getLogger() {
		return logger;
	}
	
	@Resource
	ShiftAlarmactService shiftalarmactservice ;
	
	
	
	/**
	 *@see 查询值班告警工单数据
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/relatealarmacts",method=RequestMethod.GET)
	public Object getShiftAlarmactList(HttpServletRequest request){
		JSONObject jsonObject =new JSONObject();
		JSONArray arr = new JSONArray();
		String type = BaseUtil.nullObject2String(request.getParameter("type")); //确定要查询什么类型的数据
		int currentPage = BaseUtil.nullObject2int(request.getParameter("page"));
		int pageSize = BaseUtil.nullObject2int(request.getParameter("pageSize"));
		currentPage =  currentPage == 0 ? 1:currentPage;
		pageSize =  pageSize == 0 ? 10:pageSize;
		if ("nowshift".equals(type)) { 
			//开始查询本系统值班告警表数据
			PageHelper.startPage(currentPage,pageSize);
			List<ShiftAlarmactModel> shiftAlarmactLists = shiftalarmactservice.deleteClearAndselectDutyAlarm();
			for(int i=0;i<shiftAlarmactLists.size();i++){
				arr.add(shiftAlarmactLists.get(i));
			}
			Page<ShiftAlarmactModel> listCount = (Page<ShiftAlarmactModel>)shiftAlarmactLists;
			jsonObject.put("relatealarmacts", arr);
			//分页信息
			JSONObject pageObj = new JSONObject();
			pageObj.put("total", listCount.getTotal());
			pageObj.put("pageTotal",listCount.getPages());
			pageObj.put("pageIndex", currentPage);
			jsonObject.put("meta", pageObj);
			logger.info("get duty alarm success");
		}else if ("relatealarmact".equals(type)) {
			String condition = BaseUtil.nullObject2String(request.getParameter("condition")); //关联告警查询条件
			//开始查询关联告警表数据
			PageHelper.startPage(currentPage,pageSize);
			List<ShiftAlarmactModel> tfaAlarmactLists = shiftalarmactservice.selectTfaAlarm(condition);
			for(int i=0;i<tfaAlarmactLists.size();i++){
				arr.add(tfaAlarmactLists.get(i));
			}
			jsonObject.put("relatealarmacts", arr);
			//分页信息
			Page<ShiftAlarmactModel> listCount = (Page<ShiftAlarmactModel>)tfaAlarmactLists;
			JSONObject pageObj = new JSONObject();
			pageObj.put("total", listCount.getTotal());
			pageObj.put("pageTotal",listCount.getPages());
			pageObj.put("pageIndex", currentPage);
			jsonObject.put("meta", pageObj);
			logger.info("get Interface alarm success");
		}
		
		
		return jsonObject;
	}
	
	
	/**
	 *@see 接口告警数据添加到值班告警
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/savedutyalarmacts",method=RequestMethod.POST)
	public Object saveDutyAlarmacts(HttpServletRequest request){
		try {
			JSONObject jsonObject =new JSONObject();
			String alarmId = BaseUtil.nullObject2String(request.getParameter("alarmid"));
			//开始添加本系统值班告警表数据
			shiftalarmactservice.insertByAlarmId(alarmId);
			jsonObject.put("status",1); //表示成功
			logger.info("insert duty alarm success");
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject jsonObject =new JSONObject();
			jsonObject.put("status",0); //表示失败
			logger.info("insert duty alarm false");
			return jsonObject;
		}
	}
		/**
		 *@see 值班告警删除
		 * 
		 */
		@ResponseBody
		@RequestMapping(value="/relatealarmacts/{id}",method=RequestMethod.DELETE)
		public Object deleteDutyAlarmacts(@PathVariable String id)  throws Exception{
			try {
				JSONObject jsonObject =new JSONObject();
				String alarmId = id;
				//开始删除本系统值班告警表数据
				shiftalarmactservice.deleteBacthByPrimaryKey(alarmId);
				jsonObject.put("message", "success");
				logger.info("deelte duty alarm success");
				return jsonObject;
			} catch (Exception e) {
				e.printStackTrace();
				JSONObject jsonObject =new JSONObject();
				jsonObject.put("message", "false");
				logger.info("delete duty alarm false");
				return jsonObject;
			}
		
		
		
	}
	
	
	
	
}
