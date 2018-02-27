package com.boco.eoms.dutyChangeFlight.service;

import java.util.HashMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.dutyChangeFlight.model.DutyChangeFlight;

public interface DutyChangeFlightService {
	
	public HashMap<String, Object> queryDutyChangeFlightList(int startIndex,int length,String type,String userId,String condition);

	public void insertDutyChangeFlight(DutyChangeFlight dutyChangeFlight);
	
	public void updateDutyChangeFlight(DutyChangeFlight dutyChangeFlight);
	
	public JSONObject searchDutyStartTime(String createDutyTime,String dealDutyTime,String type,String userId,String dutyConfigId);
	
	public JSONArray searchDutyUser(String dutyFlightId,String dutyTime);
}
