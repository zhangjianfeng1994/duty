package com.boco.eoms.successionShift.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.base.service.IAuthorityService;
import com.boco.eoms.base.util.StaticMethod;
import com.boco.eoms.base.util.UUIDHexGenerator;
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
import com.boco.eoms.shiftCentralfault.model.ShiftCentralfaultModel;
import com.boco.eoms.shiftCentralfault.service.ShiftCentralfaultService;
import com.boco.eoms.successionShift.model.SuccessionShiftModel;
import com.boco.eoms.successionShift.service.SuccessionShiftService;
import com.boco.eoms.successionShiftUser.model.SuccessionShiftUserModel;
import com.boco.eoms.successionShiftUser.service.SuccessionShiftUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.tools.classfile.Annotation.element_value;



@Controller
@RequestMapping(value="SuccessionShift")
public class SuccessionShiftController {
	private static final Logger logger = Logger.getLogger(SuccessionShiftController.class);
	
	public static Logger getLogger() {
		return logger;
	}
	
	@Autowired
	private SuccessionShiftService successionshiftservice;
	
	@Autowired
	private DutySchedulingUsersService dutyschedulingusersservice;
	
	@Autowired
	private DutyUsersService dutyusersservice;
	
	@Autowired
	private DutyFlightConfigService dutyflightconfigservice;
	
	@Autowired
	private DutyConfigModelService dutyconfigmodelservice;
	
	@Autowired
	private SuccessionShiftUserService successionshiftuserservice;
	
	@Autowired
	ShiftCentralfaultService shiftcentralfaultservice ;
	
	@Autowired
	private DutyUsersService dutyUserService;
	
	@Autowired
	private IAuthorityService getAuthorityService;
	
	
	
	/**
	 *@throws ParseException 
	 * @see 判断是否接班或交班
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/successionorshift",method=RequestMethod.GET)
	public String decideSuccessionOrShiftOfDuty(HttpServletRequest request) throws ParseException{
		logger.info("decideSuccessionOrShiftOfDuty start!");
		String userid = StaticMethod.nullObject2String(request.getParameter("userId")); //登录人
		String page = "";
		String message = "";
		JSONObject json = new JSONObject();
		String nowDateString = TimeUtil.getCurrentTime();
		String yesterDateString = TimeUtil.getLastDay(nowDateString);
		Date nowDate = TimeUtil.getStringParseDate(nowDateString,5);
		Date yesterDate = TimeUtil.getStringParseDate(yesterDateString,5);
		Date now_duty_time = TimeUtil.getStringParseDate(nowDateString,3);
		Date yester_duty_time = TimeUtil.getStringParseDate(yesterDateString,3);
		//查询当前登录人是否在配置人员列表
		DutyUsers userModel = dutyusersservice.queryByUserid(userid);
		if (userModel == null) {
			page = "error";
		    message = "您不在当前配置人员表中!";
			json.put("page",page);
			json.put("message",message);
			logger.info(userid+" into error, login user id does not DUTY_USERS!");
		}else {
			int duty_config_id = userModel.getDutyConfigId(); //当前登录人所属的值班配置id
			//获取交接班表中最大的一条数据
			SuccessionShiftModel maxShiftModel = successionshiftservice.selectMaxShiftByDutyConfigId(duty_config_id);
			int succession_if; //是否接班
			int shift_if;   	//是否交班
			if (maxShiftModel == null) {
				succession_if = 1;
				shift_if = 1;
			}else {
				succession_if = maxShiftModel.getSUCCESSION_IF();
				shift_if = maxShiftModel.getSHIFT_IF();
			}
			//判断进入接班还是交班
			if ((succession_if == 0 && shift_if ==0) || (succession_if == 1 && shift_if == 1)) {//进入接班
				//排班情况查询
				if ((succession_if == 1 && shift_if == 1)) { //本班次第一次接班
					List<DutySchedulingUsers> nowDutySchedulingList = dutyschedulingusersservice.selectByUseridandDutyTime(userid, duty_config_id, now_duty_time);
					if (nowDutySchedulingList.size()==0) { //今天没班
						List<DutySchedulingUsers> yesterDutySchedulingList = dutyschedulingusersservice.selectByUseridandDutyTime(userid, duty_config_id, yester_duty_time);
						if (yesterDutySchedulingList.size() > 0) {
							DutySchedulingUsers yesDutyScheduling = yesterDutySchedulingList.get(yesterDutySchedulingList.size()-1);
							//当前时间是否在昨天最后一个班次的排班时间范围
							Map<String, Object> returnMap = isSchedulingByTime(yesDutyScheduling,nowDate);
							boolean flag = (boolean) returnMap.get("flag");
							if (flag) { //昨天的班次今天接班
								int duty_flight_id = yesDutyScheduling.getDutyFlightId();
								Date duty_time = yesDutyScheduling.getDutyTime();
								String firstTimeString = BaseUtil.nullObject2String(returnMap.get("firstShiftTime"));
								//最迟接班时间
								String finalsuccessiontime = BaseUtil.nullObject2String(returnMap.get("finalsuccessiontime"));
								//最迟交班时间
								String finalshifttime = BaseUtil.nullObject2String(returnMap.get("finalShiftTime"));
								//根据当前人员排班信息查找交接班数据
								SuccessionShiftModel successionShiftModel = successionshiftservice.selectByDutySchedulingKey(duty_flight_id,duty_config_id,duty_time);
								if (successionShiftModel == null) { //当前班次还没开始值班
									int head_if = yesDutyScheduling.getHeadIf();
									page = "succession";
									json.put("page",page);
									JSONObject successiondata =  successionData(yesDutyScheduling);
									successiondata.put("first_time", firstTimeString);
									successiondata.put("final_succession_time", finalsuccessiontime);
									successiondata.put("final_shift_time", finalshifttime);
									successiondata.put("succession_user_id", userid);
									if (head_if == 0) {  //当前登录人不是班长
										successiondata.put("head_if", 0);
									}else {
										successiondata.put("head_if", 1);
									}
									json.put("data", successiondata);
//									if (head_if == 0) {  //当前登录人不是班长
//										page = "succession";
//										json.put("page",page);
//										JSONObject successiondata =  successionData(yesDutyScheduling);
//										successiondata.put("first_time", firstTimeString);
//										successiondata.put("succession_user_id", userid);
//										successiondata.put("head_if", 0);
//										json.put("data", successiondata);
//									}else {
//										page = "error";
//										message = "请等待其他组员接班完成!";
//										json.put("page",page);
//										json.put("message",message);
//										logger.info(userid+" into error, login user is head,Wait for other team succession");
//									}
								}
							}else {
								page = "error";
								message = "您不在值班状态,无法进入此页面!";
								json.put("page",page);
								json.put("message",message);
								logger.info(userid+ " into error, login user id does not on duty");
							}
						}else { //昨天没班
							page = "error";
							message = "您不在值班状态,无法进入此页面!";
							json.put("page",page);
							json.put("message",message);
							logger.info(userid+" into error, login user id does not on duty");
						}
					
				}else {//今天有班
					//当前时间是否在排班时间范围内
					DutySchedulingUsers nowDutyScheduling = null;
					String firstTimeString = "";
					//最迟接班时间
					String finalsuccessiontime = "";
					//最迟交班时间
					String finalshifttime = "";
					for (int i = 0; i < nowDutySchedulingList.size(); i++) {
						DutySchedulingUsers schedulingUser = nowDutySchedulingList.get(i);
						Map<String, Object> returnMap = isSchedulingByTime(schedulingUser,nowDate);
						boolean flag = (boolean) returnMap.get("flag");
						if (flag) {
							nowDutyScheduling = schedulingUser;
							firstTimeString = BaseUtil.nullObject2String(returnMap.get("firstShiftTime"));
							finalsuccessiontime = BaseUtil.nullObject2String(returnMap.get("finalsuccessiontime"));
							finalshifttime = BaseUtil.nullObject2String(returnMap.get("finalShiftTime"));
							break;
						}
					}
					if (nowDutyScheduling == null) {
						page = "error";
						message = "您不在值班状态,无法进入此页面!";
						json.put("page",page);
						json.put("message",message);
						logger.info(userid+" into error, login user id does not on duty");
					}else {
						int duty_flight_id = nowDutyScheduling.getDutyFlightId();
						Date duty_time = nowDutyScheduling.getDutyTime();
						//根据当前人员排班信息查找交接班数据
						SuccessionShiftModel successionShiftModel = successionshiftservice.selectByDutySchedulingKey(duty_flight_id,duty_config_id,duty_time);
						if (successionShiftModel == null) { //当前班次还没开始值班
							int head_if = nowDutyScheduling.getHeadIf();
							page = "succession";
							json.put("page",page);
							JSONObject successiondata =  successionData(nowDutyScheduling);
							successiondata.put("first_time", firstTimeString);
							successiondata.put("final_shift_time", finalshifttime);
							successiondata.put("final_succession_time", finalsuccessiontime);
							successiondata.put("succession_user_id", userid);
							if (head_if == 0) {  //当前登录人不是班长
								successiondata.put("head_if", 0);
							}else {
								successiondata.put("head_if", 1);
							}
							json.put("data", successiondata);
//							if (head_if == 0) {  //当前登录人不是班长
//								page = "succession";
//								json.put("page",page);
//								JSONObject successiondata =  successionData(nowDutyScheduling);
//								successiondata.put("first_time", firstTimeString);
//								successiondata.put("succession_user_id", userid);
//								successiondata.put("head_if", 0);
//								json.put("data", successiondata);
//							}else {
//								page = "error";
//								message = "请等待其他组员接班完成!";
//								json.put("page",page);
//								json.put("message",message);
//								logger.info(userid+" into error, login user is head,Wait for other team succession");
//							}
						}else {
							page = "error";
							message = "您已交班!";
							json.put("page",page);
							json.put("message",message);
						}
					}
				}
			 }else { //本班次已经有人接班了
				 SuccessionShiftModel successionShiftModel =  maxShiftModel;  //交接班最后一条数据为本班次数据
				 String succession_userids = successionShiftModel.getSUCCESSION_USERID();
				 String now_duty_userid = successionShiftModel.getNOW_DUTY_USERID();
				 String head_userid = now_duty_userid.substring(now_duty_userid.lastIndexOf(",")+1);
				 String [] now_duty_userStrings = now_duty_userid.split(",");
				 boolean flag_now_duty=Arrays.asList(now_duty_userStrings).contains(userid);
				 if (flag_now_duty) {
				 //判断是否是本班次人员
//				 if (!userid.equals(head_userid)) { //当前登录人不是班长
						//判断是否已经接班
						boolean is_succession = false;
						for (String  succession_user: succession_userids.split(",")) {
							if (userid.equals(succession_user)) {
								is_succession = true;
								break;
							}
						}
						if (is_succession) {
							page = "shift";
							//交班数据
							JSONObject shiftdata = successionOrshiftData(successionShiftModel);
							shiftdata.put("shift_user_id", userid);
							if (!userid.equals(head_userid)) { //当前登录人不是班长
								shiftdata.put("head_if", 0);
							}else {
								shiftdata.put("head_if", 1);
							}
							json.put("page",page);
							json.put("data",shiftdata);
							logger.info(userid+" into shift, login user already the succession");
						}else {
							page = "succession";
							//接班数据
							JSONObject successiondata = successionOrshiftData(successionShiftModel);
							successiondata.put("succession_user_id", userid);
							if (!userid.equals(head_userid)) { //当前登录人不是班长
								successiondata.put("head_if", 0);
							}else {
								successiondata.put("head_if", 1);
							}
							json.put("page",page);
							json.put("data",successiondata);
							logger.info(userid+" into succession, login user into succession");
						}
				 }else {
						page = "error";
						message = "您不在值班状态,无法进入此页面!";
						json.put("page",page);
						json.put("message",message);
						logger.info("into error, login user id does not on duty");
					}
//					}else {  //当前登录人是班长
//						//判断其他组员是否全部接班完成
//						String now_duty_user = successionShiftModel.getNOW_DUTY_USERID();
//						String noe_duty_nohead = now_duty_user.substring(0,now_duty_user.lastIndexOf(","));//无班长的值班人员
//						boolean is_all_succession = isItemsEquals(noe_duty_nohead,succession_userids);
//						if (is_all_succession) { //其他组员已经全部接班
//							page = "succession";
//							//接班数据
//							JSONObject successiondata = successionOrshiftData(successionShiftModel);
//							successiondata.put("succession_user_id", userid);
//							successiondata.put("head_if", 1);
//							json.put("page",page);
//							json.put("data",successiondata);
//							logger.info(userid+" into succession, login user is head into succession");
//						}else {
//							page = "error";
//							message = "请等待其他组员接班完成!";
//							json.put("page",page);
//							json.put("message",message);
//							logger.info(userid+" into error, login user is head,Wait for other team succession");
//						}
//					}
				
			 }
			}else if(succession_if == 1 && shift_if ==0) { //进入交班
				String now_duty_userid = maxShiftModel.getNOW_DUTY_USERID();
				String shift_userids = StaticMethod.nullObject2String(maxShiftModel.getSHIFT_USERID());
				String [] now_duty_userStrings = now_duty_userid.split(",");
				boolean flag_now_duty=Arrays.asList(now_duty_userStrings).contains(userid);
				if (flag_now_duty) {
					boolean flag_shift =  false;
					if (shift_userids != "") {
						String [] shift_userStrings = shift_userids.split(",");
						 flag_shift=Arrays.asList(shift_userStrings).contains(userid);
					}
					if (flag_shift) {
						page = "error";
						message = "您已交班!";
						json.put("page",page);
						json.put("message",message);
						logger.info("into error, login user id does not on duty");
					}else {
						//交班数据
						String head_userid = now_duty_userid.substring(now_duty_userid.lastIndexOf(",")+1);
						JSONObject shiftdata = successionOrshiftData(maxShiftModel);
						shiftdata.put("shift_user_id", userid);
						String now_duty_nohead = "";
						if (userid.equals(head_userid)) {
							if (now_duty_userid.indexOf(",") < 0) {
								now_duty_nohead = now_duty_userid;
							}else {
								now_duty_nohead = now_duty_userid.substring(0,now_duty_userid.lastIndexOf(","));//无班长的值班人员
							}
							boolean is_all_shift = isItemsEquals(now_duty_nohead,shift_userids);
							if (is_all_shift) {
								shiftdata.put("all_shift", 1); //除班长外其他人已经全部交班
							}else {
								shiftdata.put("all_shift", 0); //除班长外其他人有人没交班
							}
							shiftdata.put("head_if", 1);
						}else {
							shiftdata.put("head_if", 0);
							shiftdata.put("all_shift", 0); //除班长外其他人有人没交班
						}
						page = "shift";
						json.put("page",page);
						json.put("data",shiftdata);
						logger.info("into shift, login user already the succession");
					}
				}else {
					page = "error";
					message = "您不在值班状态,无法进入此页面!";
					json.put("page",page);
					json.put("message",message);
					logger.info("into error, login user id does not on duty");
				}
 			}
		}
		
		return json.toJSONString();
	}
	
	
	/**
	 *@see 班长交班时判断其他人员是否全部交班
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/isAllShift",method=RequestMethod.GET)
	public Object isAllShift(HttpServletRequest request){
		JSONObject jsonObject =new JSONObject();
		int shift_id =  BaseUtil.nullObject2int(request.getParameter("shift_id"));
		SuccessionShiftModel successionShiftModel = successionshiftservice.getModelById(shift_id);
		String now_duty_userid = successionShiftModel.getNOW_DUTY_USERID();
		if (now_duty_userid.indexOf(",") < 0) {
			jsonObject.put("all_shift", 1);//除班长外其他人已经全部交班
			jsonObject.put("message", "");
		}else {
			//交班人员
			String shift_userids = successionShiftModel.getSHIFT_USERID();
			String now_duty_nohead = now_duty_userid.substring(0,now_duty_userid.lastIndexOf(","));//无班长的值班人员
			boolean is_all_shift = false; 
			if (shift_userids != null) {
				is_all_shift = isItemsEquals(now_duty_nohead,shift_userids);
			}
			if (is_all_shift) {
				jsonObject.put("all_shift", 1);//除班长外其他人已经全部交班
				jsonObject.put("message", "");
			}else {
				String no_shift_userName = "";
				if (shift_userids != null) {
					String[] duty_users = now_duty_nohead.split(",");
					String[] shift_users = shift_userids.split(",");
					List<String> la = new ArrayList(Arrays.asList(duty_users));
					List<String> lb = new ArrayList(Arrays.asList(shift_users));
					la.removeAll(lb);
					//未接班人员
					String no_shift_userID = String.join(",", la);
					no_shift_userName = getUserNamesByUserIds(no_shift_userID);
				}else {
					no_shift_userName = getUserNamesByUserIds(now_duty_nohead);
				}
				String message = "请等待"+no_shift_userName+"组员交班完成";
				jsonObject.put("all_shift", 0);
				jsonObject.put("message", message);
			}
		}
		return jsonObject;
		
	}
	/**
	 * @see 本班次第一次接班数据获取
	 * @param DutySchedulingUsers
	 */
	public JSONObject  successionData(DutySchedulingUsers dutyschedulinguser){
		JSONObject json = new JSONObject();
		int duty_config_id = dutyschedulinguser.getDutyConfigId();
		int duty_flight_id = dutyschedulinguser.getDutyFlightId();
		Date duty_time = dutyschedulinguser.getDutyTime();
		String duty_timeString =  TimeUtil.getDateParseString(duty_time,3);
		//获取上一班次shift_id
		int last_shift_id = 0;
		SuccessionShiftModel maxShiftModel = successionshiftservice.selectMaxShiftByDutyConfigId(duty_config_id);
		if (maxShiftModel != null) {
			last_shift_id = maxShiftModel.getSHIFT_ID();
		}
		String lastNote = getShiftNote(last_shift_id);
		String last_user_name = "";
		String last_user_id = "";
		String last_user_head_id = "";
		String last_user_head_name = "";
		String next_user_id = "";
		String next_user_name = "";
		String now_user_id = "";
		String now_user_name = "";
		String now_user_head_id = "";
		String now_user_head_name = "";
		String next_user_head_id = "";
		String next_user_head_name = "";
		//获取上班次值班信息
		List<Map<String,Object>> last_duty_data = dutyschedulingusersservice.selectLastDutyByduty(duty_flight_id, duty_config_id, duty_time);
		//获取本班次值班信息
		List<Map<String,Object>> now_duty_data = dutyschedulingusersservice.selectNowDutyByduty(duty_flight_id, duty_config_id, duty_time);
		//获取下班次值班信息
		List<Map<String,Object>> next_duty_data = dutyschedulingusersservice.selectNextDutyByduty(duty_flight_id, duty_config_id, duty_time);
		if (last_duty_data.size()>0) {
			last_user_id = BaseUtil.nullObject2String(last_duty_data.get(0).get("LAST_DUTY_USERID"));
			last_user_name = BaseUtil.nullObject2String(last_duty_data.get(0).get("LAST_DUTY_USERNAME"));
			if (last_user_id.indexOf(",") != -1) { //上班次值班人员不止一人
				last_user_head_id = last_user_id.substring(last_user_id.lastIndexOf(",")+1);
				last_user_head_name = last_user_name.substring(last_user_name.lastIndexOf(",")+1);
			}else {
				last_user_head_id = last_user_id;
				last_user_head_name = last_user_name;
			}
		}
		if (next_duty_data.size()>0) {
			next_user_id = BaseUtil.nullObject2String(next_duty_data.get(0).get("NEXT_DUTY_USERID"));
			next_user_name = BaseUtil.nullObject2String(next_duty_data.get(0).get("NEXT_DUTY_USERNAME"));
			if (next_user_id.indexOf(",") != -1) { //上班次值班人员不止一人
				next_user_head_id = next_user_id.substring(next_user_id.lastIndexOf(",")+1);
				next_user_head_name = next_user_name.substring(next_user_name.lastIndexOf(",")+1);
			}else {
				next_user_head_id = next_user_id;
				next_user_head_name = next_user_name;
			}
		}
		now_user_id = BaseUtil.nullObject2String(now_duty_data.get(0).get("USERIDS"));
		now_user_name = BaseUtil.nullObject2String(now_duty_data.get(0).get("USERNAMES"));
		now_user_head_id = now_user_id.substring(now_user_id.lastIndexOf(",")+1);
		now_user_head_name = now_user_name.substring(now_user_name.lastIndexOf(",")+1);
		json.put("shift_id", 0);
		json.put("last_user_name", last_user_name);
		json.put("last_user_id", last_user_id);
		json.put("last_user_head_id", last_user_head_id);
		json.put("last_user_head_name", last_user_head_name);
		json.put("next_user_id", next_user_id);
		json.put("next_user_name", next_user_name);
		json.put("next_user_head_id", next_user_head_id);
		json.put("next_user_head_name", next_user_head_name);
		json.put("now_user_id", now_user_id);
		json.put("now_user_name", now_user_name);
		json.put("now_user_head_id", now_user_head_id);
		json.put("now_user_head_name", now_user_head_name);
		json.put("duty_config_id", duty_config_id);
		json.put("duty_flight_id", duty_flight_id);
		json.put("duty_time", duty_timeString);
		json.put("last_note", lastNote);
		json.put("succession_user_names", "");
		return json;
		
	}
	
	/**
	 * @see 本班次非第一次接班或交班数据获取
	 * @param SuccessionShiftModel
	 */
	public JSONObject  successionOrshiftData(SuccessionShiftModel successionShiftModel){
		JSONObject json = new JSONObject();
		int shift_id = successionShiftModel.getSHIFT_ID();
		int last_shift_id = successionShiftModel.getLAST_SHIFT_ID();
		int duty_config_id = successionShiftModel.getDUTY_CONFIG_ID();
		int duty_flight_id = successionShiftModel.getDUTY_FLIGHT_ID();
		Date duty_time = successionShiftModel.getDUTY_TIME();
		Date first_time = successionShiftModel.getFIRST_TIME();
		String shift_user_id = successionShiftModel.getSHIFT_USERID();
		Date success_time = successionShiftModel.getSUCCESSION_TIME();
		String duty_timeString = TimeUtil.getDateParseString(duty_time, 3);
		String first_timeString = TimeUtil.getDateParseString(first_time, 5);
		String success_timeString = TimeUtil.getDateParseString(success_time, 5);
		String lastNote = getShiftNote(last_shift_id);
		String last_user_name = "";
		String last_user_id = "";
		String last_user_head_id = "";
		String last_user_head_name = "";
		String next_user_id = "";
		String next_user_name = "";
		String now_user_id = "";
		String now_user_name = "";
		String now_user_head_id = "";
		String now_user_head_name = "";
		String next_user_head_id = "";
		String next_user_head_name = "";
		//获取上班次值班信息
		List<Map<String,Object>> last_duty_data = dutyschedulingusersservice.selectLastDutyByduty(duty_flight_id, duty_config_id, duty_time);
		//获取本班次值班信息
		List<Map<String,Object>> now_duty_data = dutyschedulingusersservice.selectNowDutyByduty(duty_flight_id, duty_config_id, duty_time);
		//获取下班次值班信息
		List<Map<String,Object>> next_duty_data = dutyschedulingusersservice.selectNextDutyByduty(duty_flight_id, duty_config_id, duty_time);
		if (last_duty_data.size()>0) {
			last_user_id = BaseUtil.nullObject2String(last_duty_data.get(0).get("LAST_DUTY_USERID"));
			last_user_name = BaseUtil.nullObject2String(last_duty_data.get(0).get("LAST_DUTY_USERNAME"));
			if (last_user_id.indexOf(",") != -1) { //上班次值班人员不止一人
				last_user_head_id = last_user_id.substring(last_user_id.lastIndexOf(",")+1);
				last_user_head_name = last_user_name.substring(last_user_name.lastIndexOf(",")+1);
			}else {
				last_user_head_id = last_user_id;
				last_user_head_name = last_user_name;
			}
		}
		if (next_duty_data.size()>0) {
			next_user_id = BaseUtil.nullObject2String(next_duty_data.get(0).get("NEXT_DUTY_USERID"));
			next_user_name = BaseUtil.nullObject2String(next_duty_data.get(0).get("NEXT_DUTY_USERNAME"));
			if (next_user_id.indexOf(",") != -1) { //上班次值班人员不止一人
				next_user_head_id = next_user_id.substring(next_user_id.lastIndexOf(",")+1);
				next_user_head_name = next_user_name.substring(next_user_name.lastIndexOf(",")+1);
			}else {
				next_user_head_id = next_user_id;
				next_user_head_name = next_user_name;
			}
		}
		now_user_id = BaseUtil.nullObject2String(now_duty_data.get(0).get("USERIDS"));
		now_user_name = BaseUtil.nullObject2String(now_duty_data.get(0).get("USERNAMES"));
		now_user_head_id = now_user_id.substring(now_user_id.lastIndexOf(",")+1);
		now_user_head_name = now_user_name.substring(now_user_name.lastIndexOf(",")+1);
		//查询人员交接班表中本班次已接班人员
		String has_succession_userids = successionshiftuserservice.selectSuccessionUseridByshiftId(shift_id);
		String has_succession_usernames  = getUserNamesByUserIds(has_succession_userids);
		//查询人员交接班表中本班次已交班人员
		String has_shift_usernames  = getUserNamesByUserIds(shift_user_id);
		//在班次人员后面加班长
		if (!"".equals(last_user_name)) {
			last_user_name = last_user_name+"(班长)";
		}
		if (!"".equals(next_user_name)) {
			next_user_name = next_user_name+"(班长)";
		}
		if (!"".equals(now_user_name)) {
			now_user_name = now_user_name+"(班长)";
		}
		json.put("shift_id", shift_id);
		json.put("last_user_name", last_user_name);
		json.put("last_user_id", last_user_id);
		json.put("last_user_head_id", last_user_head_id);
		json.put("last_user_head_name", last_user_head_name);
		json.put("next_user_id", next_user_id);
		json.put("next_user_name", next_user_name);
		json.put("next_user_head_id", next_user_head_id);
		json.put("next_user_head_name", next_user_head_name);
		json.put("now_user_id", now_user_id);
		json.put("now_user_name", now_user_name);
		json.put("now_user_head_id", now_user_head_id);
		json.put("now_user_head_name", now_user_head_name);
		json.put("duty_config_id", duty_config_id);
		json.put("duty_flight_id", duty_flight_id);
		json.put("duty_time", duty_timeString);
		json.put("first_time", first_timeString);
		json.put("succession_time", success_timeString);
		json.put("last_note", lastNote);
		json.put("succession_user_names", has_succession_usernames);
		json.put("shift_user_names", has_shift_usernames);
		return json;
		
	}
	
	/**
	 * @see 上一班次交接班备注
	 * @param SuccessionShiftModel
	 */
	public String  getShiftNote(int shift_id){
		SuccessionShiftModel successionShiftModel = successionshiftservice.getModelById(shift_id);
		String noteString = "";
		if (successionShiftModel != null) {
			noteString = successionShiftModel.getNOTE();
		}
		return noteString;
	}
	
	/**
	 * @see 根据userId获得userNames，例如传入zhangsan,lisi,wangwu，返回张三,李四,王五
	 * @param userIds
	 * @param officeName
	 * @return
	 */
	public String getUserNamesByUserIds(String userIds){
		if(null==userIds||"".equals(userIds)){
			return "";
		}else{
			String[] tempUserIds = userIds.split(",");
			String userNames = "";
			for(String tempUserId:tempUserIds){
				if(!"".equals(tempUserId)){
					String temUserName = dutyUserService.queryByUserid(tempUserId).getUserName();
					if(userNames.length()>0){
						userNames = userNames+","+temUserName;
					}else{
						userNames = userNames+temUserName;
					}
				}
			}
			return userNames;
		}
	}
	/**
	 * 
	 * @see 两个','相连的字符串无关顺序  相匹配
	 * 例如: a,b,c,d 和 b,c,d,a 返回true
	 */
	public  boolean isItemsEquals(String expect_user_id, String actual_user_id) {
		if (expect_user_id.length()!= actual_user_id.length()) {
			return false;
		}
		String[] array = expect_user_id.split(",");
		for (String item : array) {
			if (actual_user_id.indexOf(item) < 0) {
				return false;
			}
		}
		array = actual_user_id.split(",");
		for (String item : array) {
			if (expect_user_id.indexOf(item) < 0) {
				return false;
			}
		}
		return true;
	
	}
	
	/**
	 * 
	 * @see 判断时间是否在排班时间范围内
	 */
	public Map<String, Object> isSchedulingByTime(DutySchedulingUsers schedulingUser,Date time){
			Map<String, Object>  map = new HashMap<String, Object>();
			int duty_flight_id = schedulingUser.getDutyFlightId();
			int duty_config_id = schedulingUser.getDutyConfigId();
			Date duty_time = schedulingUser.getDutyTime();
			DutyFlightConfig dutyFlightModel = dutyflightconfigservice.selectByPrimaryKey(duty_flight_id);
			String beginTimeString = dutyFlightModel.getDutyFlightBegintime();
			String endTimeString = dutyFlightModel.getDutyFlightEndtime();
			String duty_time_string = TimeUtil.getDateParseString(duty_time,3); //转化为 yyyy-MM-dd的String
			beginTimeString = duty_time_string+" "+beginTimeString+":00" ; //开始时间 yyyy-MM-dd HH:mm:ss
			endTimeString = duty_time_string+" "+endTimeString+":00" ; //结束时间 yyyy-MM-dd HH:mm:ss
			Date beginTimeDate = TimeUtil.getStringParseDate(beginTimeString,5);
			Date endTimeDate = TimeUtil.getStringParseDate(endTimeString,5);
			//判断开始时间和结束时间,如果结束时间大于开始时间,那么结束时间跨天
			if (endTimeDate.getTime()<=beginTimeDate.getTime()) {
				endTimeString = TimeUtil.addDaysToDate(endTimeString,1,5);
				endTimeDate = TimeUtil.getStringParseDate(endTimeString,5);
			}
			
			DutyConfigModel dutyconfigmodel = dutyconfigmodelservice.selectByPrimaryKey(duty_config_id);
			String duty_handover_timeString = dutyconfigmodel.getDutyHandoverTime(); //提前延后时间
			int duty_handover_time = Integer.parseInt(duty_handover_timeString)*1000*60; //获取毫秒数
			Long beginTimeLong = beginTimeDate.getTime()-duty_handover_time;
			Long endTimeLong = endTimeDate.getTime()+duty_handover_time; //最迟交班时间
			Long firstShiftTimeLong = endTimeDate.getTime()-duty_handover_time;//最早交班时间
			Long finalShiftTimeLong = endTimeDate.getTime()+duty_handover_time;//最迟交班时间
			Long finalsuccessiontimeLong = beginTimeDate.getTime()+duty_handover_time;//最迟接班时间
			Date firstShiftTimeDate = new Date(firstShiftTimeLong);//最早交班时间
			Date finalShiftTimeDate = new Date(finalShiftTimeLong);//最迟交班时间
			Date finalsuccessiontimeDate = new Date(finalsuccessiontimeLong);
			String firstShiftTimeString = TimeUtil.getDateParseString(firstShiftTimeDate, 5);//最早交班时间
			String finalShiftTimeString = TimeUtil.getDateParseString(finalShiftTimeDate, 5);//最迟交班时间
			String finalsuccessiontimeString = TimeUtil.getDateParseString(finalsuccessiontimeDate, 5);
			beginTimeDate = new Date(beginTimeLong);
			endTimeDate =  new Date(endTimeLong);
			//time是否在开始时间与结束时间之间
			if (time.getTime()>=beginTimeDate.getTime()&&time.getTime()<=endTimeDate.getTime()) {
				map.put("flag", true);
				map.put("firstShiftTime", firstShiftTimeString);
				map.put("finalShiftTime", finalShiftTimeString);
				map.put("finalsuccessiontime", finalsuccessiontimeString);
			}else {
				map.put("flag", false);
			}
			return map;
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
		//开始时间和结束时间为班次排班的接班开始时间和交班结束时间,并不是实际的交接班时间
		beginTimeString = duty_time_string+" "+beginTimeString+":00" ; //开始时间 yyyy-MM-dd HH:mm:ss
		endTimeString = duty_time_string+" "+endTimeString+":00" ; //结束时间 yyyy-MM-dd HH:mm:ss
		Map<String, String>  map = new HashMap<String, String>();
		map.put("beginTime", beginTimeString);
		map.put("endTime", endTimeString);
		return map;
	}
	
	/**
	 *@see 接班
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/succession",method=RequestMethod.GET)
	public String sumbitSuccessionOfDuty(HttpServletRequest request){
		try {
			String message = "";
			int shift_id = BaseUtil.nullObject2int(request.getParameter("shift_id"));
			int head_if = BaseUtil.nullObject2int(request.getParameter("head_if"));
			if (shift_id == 0) { //本班次第一次接班
				//获取接班数据
				int duty_config_id =  BaseUtil.nullObject2int(request.getParameter("duty_config_id"));
				int duty_flight_id =  BaseUtil.nullObject2int(request.getParameter("duty_flight_id"));
				String duty_time =  BaseUtil.nullObject2String(request.getParameter("duty_time"));
				String first_time =  BaseUtil.nullObject2String(request.getParameter("first_time"));
				String final_succession_time =  BaseUtil.nullObject2String(request.getParameter("final_succession_time"));
				String final_shift_time =  BaseUtil.nullObject2String(request.getParameter("final_shift_time"));
				String succession_time =  BaseUtil.nullObject2String(request.getParameter("succession_time"));
				String now_duty_userid =  BaseUtil.nullObject2String(request.getParameter("now_user_id"));
				String last_duty_userid =  BaseUtil.nullObject2String(request.getParameter("last_user_id"));
				String next_duty_userid =  BaseUtil.nullObject2String(request.getParameter("next_user_id"));
				String succession_userid =  BaseUtil.nullObject2String(request.getParameter("succession_user_id"));
				Date final_succession_timeDate = TimeUtil.getStringParseDate(final_succession_time, 5);
				Date succession_timeDate = TimeUtil.getStringParseDate(succession_time, 5);
				SuccessionShiftModel successionshiftmodel = new SuccessionShiftModel();
				successionshiftmodel.setDUTY_CONFIG_ID(duty_config_id);
				successionshiftmodel.setDUTY_FLIGHT_ID(duty_flight_id);
				successionshiftmodel.setDUTY_TIME(TimeUtil.getStringParseDate(duty_time, 3));
				successionshiftmodel.setFIRST_TIME(TimeUtil.getStringParseDate(first_time, 5));
				successionshiftmodel.setFINAL_SUCCESSION_TIME(TimeUtil.getStringParseDate(final_succession_time, 5));
				successionshiftmodel.setFINAL_SHIFT_TIME(TimeUtil.getStringParseDate(final_shift_time, 5));
				successionshiftmodel.setLAST_DUTY_USERID(last_duty_userid);
				successionshiftmodel.setNEXT_DUTY_USERID(next_duty_userid);
				successionshiftmodel.setNOW_DUTY_USERID(now_duty_userid);
				successionshiftmodel.setSUCCESSION_USERID(succession_userid);
				successionshiftmodel.setSUCCESSION_TIME(TimeUtil.getStringParseDate(succession_time, 5));
				//获取上一班次shift_id
				int last_shift_id = 0;
				SuccessionShiftModel maxShiftModel = successionshiftservice.selectMaxShiftByDutyConfigId(duty_config_id);
				if (maxShiftModel != null) {
					last_shift_id = maxShiftModel.getSHIFT_ID();
				}
				successionshiftmodel.setLAST_SHIFT_ID(last_shift_id);
				commitSuccessionDataIfNotExist(successionshiftmodel);
				logger.info(succession_userid+" user first succession, and "+succession_time+" succession ");
				//如果接班人在接班时间范围内接班,删除短信通知
				if (final_succession_timeDate.getTime()>succession_timeDate.getTime()) { //实际接班时间在最晚接班时间之内
					Date duty_timeDate = TimeUtil.getStringParseDate(duty_time, 3);
					String duty_timeString = TimeUtil.getDateParseString(duty_timeDate, 3);
					duty_timeString = duty_timeString.replaceAll("-", "");
					String buiz_id = duty_timeString+"-"+duty_flight_id+"-"+succession_userid+"succession";
					successionshiftservice.deleteSmsMonitor(buiz_id);
				}
			}else { //本班次已经接班
				String succession_time =  BaseUtil.nullObject2String(request.getParameter("succession_time"));
				Date succession_timeDate = BaseUtil.nullString2Date(succession_time);
				String succession_userid =  BaseUtil.nullObject2String(request.getParameter("succession_user_id"));
				commitSuccessionDataIfExist(succession_userid,shift_id,succession_timeDate,0);
				SuccessionShiftModel successionModel = successionshiftservice.getModelById(shift_id);
				//如果接班人在接班时间范围内接班,删除短信通知
				Date final_succession_time = successionModel.getFINAL_SUCCESSION_TIME(); //最晚接班时间
				if (final_succession_time.getTime()>succession_timeDate.getTime()) { //实际接班时间在最晚接班时间之内
					Date duty_time = successionModel.getDUTY_TIME();
					String duty_timeString = TimeUtil.getDateParseString(duty_time, 3);
					duty_timeString = duty_timeString.replaceAll("-", "");
					int duty_flight_id = successionModel.getDUTY_FLIGHT_ID();
					String buiz_id = duty_timeString+"-"+duty_flight_id+"-"+succession_userid+"succession";
					successionshiftservice.deleteSmsMonitor(buiz_id);
				}
//				if (head_if == 0) {  //接班人不是班长
//					commitSuccessionDataIfExist(succession_userid,shift_id,succession_timeDate,0);
//					logger.info(succession_userid+ " In succession, and "+succession_time+" succession ");
//				}else { //接班人是班长
//					//接班人员
//					commitSuccessionDataIfExist(succession_userid,shift_id,succession_timeDate,1);
//					logger.info(succession_userid+" userhead In succession, and "+succession_time+" succession");
//				}
			}
			
			message = "您好,您接班成功!";
			JSONObject messagejson = new JSONObject();
			messagejson.put("message", message);
			messagejson.put("status", 1);
			return messagejson.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			String message = "对不起,您接班失败!";
			logger.info("In succession, but succession fail");
			JSONObject messagejson = new JSONObject();
			messagejson.put("message", message);
			messagejson.put("status", 0);
			return messagejson.toJSONString();
		}
		
	}
	
	
	
	/**
	 * @see 本班次第一次接班时,人员交接班插入数据及交接班表中插入数据
	 */
	public void commitSuccessionDataIfNotExist(SuccessionShiftModel successionshiftmodel){
		successionshiftmodel.setSHIFT_IF(0);
		//第一次接班判断本班次是否是一个人
		String now_duty_userid = successionshiftmodel.getNOW_DUTY_USERID();
		String succession_userid = successionshiftmodel.getSUCCESSION_USERID();
		if (now_duty_userid.equals(succession_userid)) {
			successionshiftmodel.setSUCCESSION_IF(1);
		}else {
			successionshiftmodel.setSUCCESSION_IF(0);
		}
		successionshiftservice.saveModel(successionshiftmodel);
		
		//人员交接班表中插入数据
		SuccessionShiftUserModel successionUserModel = new SuccessionShiftUserModel();
		successionUserModel.setUSER_ID(successionshiftmodel.getSUCCESSION_USERID());
		successionUserModel.setSHIFT_ID(successionshiftmodel.getSHIFT_ID());
		successionUserModel.setSUCCESSION_TIME(successionshiftmodel.getSUCCESSION_TIME());
		successionUserModel.setSUCCESSION_IF(1);
		successionUserModel.setSHIFT_IF(0);
		successionshiftuserservice.saveModel(successionUserModel);
	}
	
	/**
	 * @see 本班次不是第一次接班,人员交接班插入数据及交接班表中更新数据
	 */
	public void commitSuccessionDataIfExist(String user_id,int shift_id,Date succession_time,int type){
		//人员交接班表中插入数据
		SuccessionShiftUserModel successionUserModel = new SuccessionShiftUserModel();
		successionUserModel.setUSER_ID(user_id);
		successionUserModel.setSHIFT_ID(shift_id);
		successionUserModel.setSUCCESSION_TIME(succession_time);
		successionUserModel.setSUCCESSION_IF(1);
		successionUserModel.setSHIFT_IF(0);
		successionshiftuserservice.saveModel(successionUserModel);
		//查询人员交接班表中本班次已接班人员
		String succession_userids = successionshiftuserservice.selectSuccessionUseridByshiftId(shift_id);
		SuccessionShiftModel successionShiftModel1 = successionshiftservice.getModelById(shift_id);
		String now_duty_user = successionShiftModel1.getNOW_DUTY_USERID();
		//判断本班次值班人员是否全部接班
		boolean is_all_succession = isItemsEquals(succession_userids,now_duty_user);
		//更新交接班表中数据
		SuccessionShiftModel successionShiftModel = new SuccessionShiftModel();
		successionShiftModel.setSHIFT_ID(shift_id);
		successionShiftModel.setSUCCESSION_TIME(succession_time);
		successionShiftModel.setSUCCESSION_USERID(succession_userids);
		if (is_all_succession) { //如果全部接班
			successionShiftModel.setSUCCESSION_IF(1);
		}
//		if (type == 1) { //接班人是班长
//			successionShiftModel.setSUCCESSION_IF(1);
//		}
		successionshiftservice.updateModel(successionShiftModel);
	}
	
	/**
	 * @see 本班次交班,人员交接班更新数据及交接班表中更新数据
	 */
	public void commitShiftData(String user_id,int shift_id,Date shift_time,int type,String note,String allnote){
		//人员交接班表中插入数据
		SuccessionShiftUserModel successionUserModel = new SuccessionShiftUserModel();
		successionUserModel.setUSER_ID(user_id);
		successionUserModel.setSHIFT_ID(shift_id);
		successionUserModel.setSHIFT_TIME(shift_time);
		successionUserModel.setNOTE(note);
		successionUserModel.setSHIFT_IF(1);
		successionshiftuserservice.updateByUserAndShiftIdModel(successionUserModel);
		//查询人员交接班表中本班次已交班人员
		String shift_userids = successionshiftuserservice.selectShiftUseridByshiftId(shift_id);
		
		//更新交接班表中数据
		SuccessionShiftModel successionShiftModel = new SuccessionShiftModel();
		successionShiftModel.setSHIFT_ID(shift_id);
		successionShiftModel.setSHIFT_TIME(shift_time);
		successionShiftModel.setSHIFT_USERID(shift_userids);
		if (type == 1) { //接班人是班长
			successionShiftModel.setNOTE(allnote);
			successionShiftModel.setSHIFT_IF(1);
		}
		successionshiftservice.updateModel(successionShiftModel);
	}
	
	/**
	 *@see 合并日志
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/mergeNote",method=RequestMethod.GET)
	public Object  mergeNoteOfDuty(HttpServletRequest request){
		JSONObject jsonObject =new JSONObject();
		int shift_id = BaseUtil.nullObject2int(request.getParameter("shift_id"));
		List<Map<String,Object>> shiftUserList = successionshiftuserservice.selectAllByshiftId(shift_id);
		String totalNote = "";
		for (int i = 0; i < shiftUserList.size(); i++) {
			Map<String,Object> map = shiftUserList.get(i);
			String username = BaseUtil.nullObject2String(map.get("USER_NAME"));
			String note = BaseUtil.nullObject2String(map.get("NOTE"));
			if (!"".equals(note)) {
				totalNote = totalNote+username+": "+note+";\n";
			}
		}
//		SuccessionShiftModel successionShiftModel = new SuccessionShiftModel();
//		successionShiftModel.setSHIFT_ID(shift_id);
//		successionShiftModel.setNOTE(totalNote);
//		successionshiftservice.updateModel(successionShiftModel);
		jsonObject.put("totalnote", totalNote);
		
		return jsonObject;
		
	}
	/**
	 *@see 交班
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/shift",method=RequestMethod.GET)
	public String sumbitShiftOfDuty(HttpServletRequest request){
		try {
			int shift_id = BaseUtil.nullObject2int(request.getParameter("shift_id"));
			int head_if = BaseUtil.nullObject2int(request.getParameter("head_if"));
			String shift_time =  BaseUtil.nullObject2String(request.getParameter("shift_time"));
			Date shift_timeDate = BaseUtil.nullString2Date(shift_time);
			String shift_user_id =  BaseUtil.nullObject2String(request.getParameter("shift_user_id"));
			String note = BaseUtil.nullObject2String(request.getParameter("note"));
			SuccessionShiftModel successionModel = successionshiftservice.getModelById(shift_id);
			//如果交班人在交班时间范围内交班,删除短信通知
			Date final_shift_time = successionModel.getFINAL_SHIFT_TIME();
			if (final_shift_time.getTime()>shift_timeDate.getTime()) { //实际交班时间在最晚交班时间之内
				Date duty_time = successionModel.getDUTY_TIME();
				String duty_timeString = TimeUtil.getDateParseString(duty_time, 3);
				duty_timeString = duty_timeString.replaceAll("-", "");
				int duty_flight_id = successionModel.getDUTY_FLIGHT_ID();
				String buiz_id = duty_timeString+"-"+duty_flight_id+"-"+shift_user_id+"shift";
				successionshiftservice.deleteSmsMonitor(buiz_id);
			}
			if (head_if != 1) { //交班人不是班长
				//交班信息更新
				commitShiftData(shift_user_id,shift_id,shift_timeDate,0,note,"");
				logger.info(shift_user_id+ " In shift, and "+shift_time+" shift ");
			}else {
				String aa = request.getParameter("totalNote");
				String total_note = BaseUtil.nullObject2String(request.getParameter("totalNote")); //交接班表中的备注,为本班次遗留问题
				//关联故障工单自动遗留下班次(删除遗留中已归档的数据)
				shiftcentralfaultservice.deleteBatchModel(shift_id);
				//交班
				commitShiftData(shift_user_id,shift_id,shift_timeDate,1,note,total_note);
				//通知下班次人员接班
				successionModel.setNOTE(total_note);
				remindNextFlightUsers(successionModel);
				//本班次交班完成之后,先在短信表中通知下班次人员迟到,或者超出交班时间.之后如果下班次人员在时间范围内就删除通知.
				saveMonitorSmsNextFlightUsers(successionModel);
				logger.info(shift_user_id+ " is head, In shift, and "+shift_time+" shift ");
			}
			String message = "您好,您交班成功!";
			JSONObject messagejson = new JSONObject();
			messagejson.put("message", message);
			return messagejson.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			String message = "对不起,您交班失败!";
			JSONObject messagejson = new JSONObject();
			messagejson.put("message", message);
			return messagejson.toJSONString();
		}
	}
	
	
	/**
	 * @throws Exception 
	 * @see 本班次交班完成之后,通知下一班次人员接班
	 */
	public void remindNextFlightUsers(SuccessionShiftModel successionModel) throws Exception{
		//获取本班次交接班记录
		if (successionModel != null) {
			String next_user_id = successionModel.getNEXT_DUTY_USERID();
			String noteString = successionModel.getNOTE();
			noteString = "上班次交班备注: " + noteString ;
			//String remidTime = TimeUtil.getCurrentTime(); //当前时间
			Date remidTime = new Date();
			String modelType = "duty_managent";
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if (next_user_id.length()>0) {
				String[] useridStrings = next_user_id.split(",");
				for (int i = 0; i < useridStrings.length; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					String id = UUIDHexGenerator.getID();
					map.put("id", id);
					map.put("userid", useridStrings[i]);
					map.put("remindContent", noteString);
					map.put("remindTime", remidTime);
					map.put("deleted", 0);
					map.put("modelType", modelType);
					list.add(map);
				}
				successionshiftservice.saveRemindUser(list);
			}
		}
		
	}
	
	
	/**
	 * @throws Exception 
	 * @see 本班次交班完成之后,短信通知下一班次人员迟到,或者超出交接班时间
	 */
	public void saveMonitorSmsNextFlightUsers(SuccessionShiftModel successionModel) throws Exception{
		//获取本班次交接班记录
		if (successionModel != null) {
			String next_user_id = successionModel.getNEXT_DUTY_USERID();
			Date duty_time = successionModel.getDUTY_TIME();
			int duty_flight_id = successionModel.getDUTY_FLIGHT_ID();
			int duty_config_id = successionModel.getDUTY_CONFIG_ID();
			//获取下班次值班信息
			List<Map<String,Object>> next_duty_data = dutyschedulingusersservice.selectNextDutyByduty(duty_flight_id, duty_config_id, duty_time);
			if (next_duty_data.size()>0) {
			      Date next_duty_time = (Date) next_duty_data.get(0).get("DUTY_TIME");
			      int  next_duty_flight_id = BaseUtil.nullObject2int(next_duty_data.get(0).get("DUTY_FLIGHT_ID"));
			      //获取班次信息
			      DutyFlightConfig dutyFlightModel = dutyflightconfigservice.selectByPrimaryKey(next_duty_flight_id);
			      String beginTimeString = dutyFlightModel.getDutyFlightBegintime();
			      String endTimeString = dutyFlightModel.getDutyFlightEndtime();
			      String next_duty_time_string = TimeUtil.getDateParseString(next_duty_time,3); //转化为 yyyy-MM-dd的String
			      beginTimeString = next_duty_time_string+" "+beginTimeString+":00" ; //开始时间 yyyy-MM-dd HH:mm:ss
			      endTimeString = next_duty_time_string+" "+endTimeString+":00" ; //结束时间 yyyy-MM-dd HH:mm:ss
			      Date beginTimeDate = TimeUtil.getStringParseDate(beginTimeString,5);
			      Date endTimeDate = TimeUtil.getStringParseDate(endTimeString,5);
			      //判断开始时间和结束时间,如果结束时间大于开始时间,那么结束时间跨天
			      if (endTimeDate.getTime()<=beginTimeDate.getTime()) {
			        endTimeString = TimeUtil.addDaysToDate(endTimeString,1,5);
			        endTimeDate = TimeUtil.getStringParseDate(endTimeString,5);
			      }
			      DutyConfigModel dutyconfigmodel = dutyconfigmodelservice.selectByPrimaryKey(duty_config_id);
			      String duty_handover_timeString = dutyconfigmodel.getDutyHandoverTime(); //提前延后时间
			      int duty_handover_time = Integer.parseInt(duty_handover_timeString)*1000*60; //获取毫秒数
			      Long finalShiftTimeLong = endTimeDate.getTime()+duty_handover_time;//最迟交班时间
			      Long finalsuccessiontimeLong = beginTimeDate.getTime()+duty_handover_time;//最迟接班时间
			      Date finalShiftTimeDate = new Date(finalShiftTimeLong);//最迟交班时间
			      Date finalsuccessiontimeDate = new Date(finalsuccessiontimeLong); //最迟接班时间
			      //短信通知表中插入数据
			      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			      if (next_user_id.length()>0) {
			    	  String duty_timeString = next_duty_time_string.replaceAll("-", "");
			    	  String[] useridStrings = next_user_id.split(",");
			    	  //接班迟到短信通知
			    	  for (int i = 0; i < useridStrings.length; i++) {
			    		  Map<String, Object> map = new HashMap<String, Object>();
			    		  String id = UUIDHexGenerator.getID();
			    		  String userid = useridStrings[i];
			    		  JSONObject json = getAuthorityService.getUserByUserId(userid);
			    		  String sendContact = StaticMethod.nullObject2String(json.get("userMobile"));
			    		  String buiz_id = duty_timeString+"-"+next_duty_flight_id+"-"+userid+"succession";
			    		  String contentString = "已超过接班时间，您尚未登录系统，已迟到";
			    		  map.put("id", id);
			    		  map.put("buiz_id", buiz_id);
			    		  map.put("mobile", sendContact);
			    		  map.put("dispatch_time", finalsuccessiontimeDate);
			    		  map.put("content", contentString);
			    		  map.put("deleted", "0");
			    		  list.add(map);
			    	  }
			    	  //交班超出预定时间短信通知
			    	  for (int i = 0; i < useridStrings.length; i++) {
			    		  Map<String, Object> map = new HashMap<String, Object>();
			    		  String id = UUIDHexGenerator.getID();
			    		  String userid = useridStrings[i];
			    		  JSONObject json = getAuthorityService.getUserByUserId(userid);
			    		  String sendContact = StaticMethod.nullObject2String(json.get("userMobile"));
			    		  String buiz_id = duty_timeString+"-"+next_duty_flight_id+"-"+userid+"shift";
			    		  String contentString = "已超过交班时间，您未登出系统";
			    		  map.put("id", id);
			    		  map.put("buiz_id", buiz_id);
			    		  map.put("mobile", sendContact);
			    		  map.put("dispatch_time", finalShiftTimeDate);
			    		  map.put("content", contentString);
			    		  map.put("deleted", "0");
			    		  list.add(map);
			    	  }
			    	  successionshiftservice.saveSmsMonitor(list);
			      }
			}
		}
		
	}
	
	
	/**
	 *@see 查询值班告警工单数据
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/getDutyCount",method=RequestMethod.GET)
	public Object getDutyCount(HttpServletRequest request){
		JSONObject jsonObject =new JSONObject();
		JSONArray arr = new JSONArray();
		int currentPage = BaseUtil.nullObject2int(request.getParameter("pageIndex"));
		int pageSize = BaseUtil.nullObject2int(request.getParameter("limit"));
		String startTime = BaseUtil.nullObject2String(request.getParameter("startTime")); //开始时间
		String endTime = BaseUtil.nullObject2String(request.getParameter("endTime")); //结束时间
		int duty_config_id = BaseUtil.nullObject2int(request.getParameter("duty_config_id")); //值班配置id
		String duty_user_id = BaseUtil.nullObject2String(request.getParameter("duty_user_id")); //值班人员id
		currentPage =  currentPage == 0 ? 1:currentPage;
	     pageSize =  pageSize == 0 ? 10:pageSize;
	    String pivotcontionString = ""; //列转行in中的条件
	    int cou = 4; // 给列取别名(1,2,3,4....) 此列从4开始
		List<DutyFlightConfig> dfList = dutyflightconfigservice.selectByDutyConfigId(duty_config_id);//根据duty_config id查询此配置中班次信息
		//值班统计表头
		JSONArray titleArr = new JSONArray();
		titleArr.add("值班人员");
		titleArr.add("迟到次数");
		titleArr.add("值班次数");
		//拼接列转行in中的条件
		if (dfList.size()>0) {
			for (int i = 0; i < dfList.size(); i++) {
				DutyFlightConfig dfc =  dfList.get(i);
				int ascount = (cou+i);
				String  dutyFllightNameString = StaticMethod.nullObject2String(dfc.getDutyFlightName());//剩余表头
				pivotcontionString =pivotcontionString+" '"+dutyFllightNameString+"' as \"" +ascount +"\"";
				if (i != dfList.size()-1) {
					pivotcontionString = pivotcontionString+",";
				}
				titleArr.add(dutyFllightNameString);
			}
		}
		jsonObject.put("dutyCountTitle", titleArr);
		//值班统计数据
		PageHelper.startPage(currentPage,pageSize);
		List<Map<String, Object>> result = successionshiftservice.selectDutyCountByDutyConfig(duty_config_id, duty_user_id, startTime, endTime, pivotcontionString);
		if (result.size()>0) {
			 for (int i = 0; i < result.size(); i++) {
				 Map<String, Object> map = result.get(i);
				 arr.add(map);
			}
		}
		jsonObject.put("dutyCountValues", arr);
		
		PageInfo listCount =  new PageInfo<>(result);
		//分页信息
		JSONObject pageObj = new JSONObject();
		pageObj.put("total", listCount.getTotal());
		pageObj.put("pageTotal",listCount.getPages());
		pageObj.put("pageIndex", currentPage);
		jsonObject.put("meta", pageObj);
		return jsonObject;
	}
}
