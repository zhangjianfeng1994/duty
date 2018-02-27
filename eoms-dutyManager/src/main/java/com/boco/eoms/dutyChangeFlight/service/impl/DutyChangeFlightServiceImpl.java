package com.boco.eoms.dutyChangeFlight.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.dutyChangeFlight.dao.DutyChangeFlightMapper;
import com.boco.eoms.dutyChangeFlight.model.DutyChangeFlight;
import com.boco.eoms.dutyChangeFlight.service.DutyChangeFlightService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service("DutyChangeFlightService")
public class DutyChangeFlightServiceImpl implements DutyChangeFlightService{

	@Autowired
	private DutyChangeFlightMapper dutyChangeFlightMapper;
	
	/**
	 * @see 查询本人换班记录列表信息
	 * @param request
	 * @return
	 * @author
	 * @throws IOException
	 */
	@Transactional
	@Override
	public HashMap<String, Object> queryDutyChangeFlightList(int startIndex,int length,String type,String userId,String condition) {
		PageHelper.startPage(startIndex,length);
		//本人的换班申请
		if("myModel".equals(type)){
			condition = " AND CREATE_USER = '" + userId + "' AND SHEET_STATUS != 3";
		}
		//本人收到的换班申请
		if("otherModel".equals(type)){
			condition = " AND DEAL_USER = '" + userId + "' AND SHEET_STATUS = 0";
		}
		//审核人待审批列表
		if("auditModel".equals(type)){
			condition = " AND SHEET_STATUS = 1";
		}
		//所有列表
		if("allModel".equals(type)){
		}
		List<DutyChangeFlight> dutyChangeFlightList = dutyChangeFlightMapper.queryDutyChangeFlightList(condition);
		Page<DutyChangeFlight> dutyChangeFlightCount = (Page<DutyChangeFlight>)dutyChangeFlightList;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("dutyChangeFlight", dutyChangeFlightList);
		resultMap.put("total", dutyChangeFlightCount.getTotal());
		resultMap.put("pageTotal", dutyChangeFlightCount.getPages());
		resultMap.put("pageIndex", startIndex);
		return resultMap;
	}
	
	/**
	 * @see 插入一条换班记录
	 * @param request
	 * @return
	 * @author
	 * @throws IOException
	 */
	@Transactional
	@Override
	public void insertDutyChangeFlight(DutyChangeFlight dutyChangeFlight) {
		int createFlightId = dutyChangeFlight.getCreateFlightId();
		int dealFlightId = dutyChangeFlight.getDealFlightId();
		dutyChangeFlightMapper.insertDutyChangeFlight(dutyChangeFlight);
		dutyChangeFlightMapper.updateChangeIf(createFlightId,dealFlightId,1);
	}
	
	/**
	 * @see 更新一条换班记录
	 * @param request
	 * @return
	 * @author
	 * @throws IOException
	 */
	@Transactional
	@Override
	public void updateDutyChangeFlight(DutyChangeFlight dutyChangeFlight) {
		String status = dutyChangeFlight.getStatus();
		int sheetStatus = dutyChangeFlight.getSheetStatus();
		int createFlightId = dutyChangeFlight.getCreateFlightId();
		int dealFlightId = dutyChangeFlight.getDealFlightId();
		if("审核人同意".equals(status) && sheetStatus == 2){
			//完成换班，变更对应用户
			String createUser = dutyChangeFlight.getCreateUser();
			String dealUser = dutyChangeFlight.getDealUser();
			Date createTime = dutyChangeFlight.getCreateDutyTime();
			Date dealTime = dutyChangeFlight.getDealDutyTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			String createDutyTime = sdf.format(createTime);
			String dealDutyTime = sdf.format(dealTime);
			dutyChangeFlightMapper.changeFlightUser(createUser,dealDutyTime,dealUser,dealFlightId);
			dutyChangeFlightMapper.changeFlightUser(dealUser,createDutyTime,createUser,createFlightId);
			dutyChangeFlightMapper.updateChangeIf(createFlightId, dealFlightId, 0);
		}
		if("接收人不同意".equals(status) || "审核人不同意".equals(status)){
			dutyChangeFlightMapper.updateChangeIf(createFlightId, dealFlightId, 0);
		}
		dutyChangeFlightMapper.updateDutyChangeFlight(dutyChangeFlight);
	}
	
	/**
	 * @see 获取班次开始时间
	 * @param request
	 * @return
	 * @author
	 * @throws IOException
	 */
	@Transactional
	@Override
	public JSONObject searchDutyStartTime(String createDutyTime,String dealDutyTime,String type,String userId,String dutyConfigId) {
		JSONObject dictObject = new JSONObject();
		List<HashMap<String, String>> dictList = new ArrayList<HashMap<String,String>>();
		if("createDutyTime".equals(type)){
			dictList = dutyChangeFlightMapper.searchCreateDutyStartTime(createDutyTime,userId);
		}
		if("dealDutyTime".equals(type)){
			dictList = dutyChangeFlightMapper.searchDealDutyStartTime(dealDutyTime,userId,dutyConfigId);
		}
		
		JSONArray array = new JSONArray();
		//JSON.toJSONString(object)
		if(dictList!=null&&dictList.size()>0){
			dictList.forEach((Map<String, String> map) -> {
				array.add(map);
			});
		}
		dictObject.put(type, array);
		return dictObject;
	}
	
	/**
	 * @see 查询班次下的人
	 * @param request
	 * @return
	 * @author
	 * @throws IOException
	 */
	@Transactional
	@Override
	public JSONArray searchDutyUser(String dutyFlightId,String dutyTime) {
		List<HashMap<String, String>> dictList = new ArrayList<HashMap<String,String>>();
		dictList = dutyChangeFlightMapper.searchDutyUser(dutyFlightId,dutyTime);
		
		JSONArray array = new JSONArray();
		//JSON.toJSONString(object)
		if(dictList!=null&&dictList.size()>0){
			dictList.forEach((Map<String, String> map) -> {
				array.add(map);
			});
		}
		return array;
	}
}
