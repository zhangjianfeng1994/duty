package com.boco.eoms.successionShiftUser.service;


import java.util.List;
import java.util.Map;

import com.boco.eoms.successionShiftUser.model.SuccessionShiftUserModel;

public interface SuccessionShiftUserService {
	public SuccessionShiftUserModel getModelById(int id);
	
	public void saveModel(SuccessionShiftUserModel successionshiftusermodel);//增加记录
	
	public void updateModel(SuccessionShiftUserModel successionshiftusermodel);//更新记录
	
	public void updateByUserAndShiftIdModel(SuccessionShiftUserModel successionshiftusermodel);//更新记录
	
	public String  selectSuccessionUseridByshiftId(int shift_id);
	
	
	public String  selectShiftUseridByshiftId(int shift_id);
	
	public List<Map<String,Object>> selectAllByshiftId(int shift_id);
}
