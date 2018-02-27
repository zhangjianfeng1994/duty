package com.boco.eoms.dutyInfo.service;

import java.util.HashMap;
import java.util.List;

import com.boco.eoms.dutyInfo.model.DutyInfo;

public interface DutyInfoService {
	
	public HashMap<String, Object> queryDutyInfoList(int startIndex,int length,String condition);

	public void insertDutyInfo(DutyInfo dutyInfo);
	
	public void updateDutyInfo(DutyInfo dutyInfo);
	
	
	public void batchUpdateDutyInfoRemain(int remainIf,String ids);
	
	public void deleteDutyInfo(int id);
	
	public void insertDutyInfoList(List<HashMap<String, String>> list);
	
}
