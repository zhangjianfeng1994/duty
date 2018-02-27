package com.boco.eoms.successionShiftUser.service.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.boco.eoms.successionShiftUser.dao.SuccessionShiftUserMapper;
import com.boco.eoms.successionShiftUser.model.SuccessionShiftUserModel;
import com.boco.eoms.successionShiftUser.service.SuccessionShiftUserService;

@Service
public class SuccessionShiftUserServiceimpl implements SuccessionShiftUserService {

	@Resource
	private SuccessionShiftUserMapper successionshiftuserdao;
	@Override
	public SuccessionShiftUserModel getModelById(int id) {
		return successionshiftuserdao.selectByPrimaryKey(id);
	}

	@Override
	public void saveModel(SuccessionShiftUserModel successionshiftusermodel) {
		successionshiftuserdao.insertSelective(successionshiftusermodel);
	}

	@Override
	public void updateModel(SuccessionShiftUserModel successionshiftusermodel) {
		
		successionshiftuserdao.updateByPrimaryKeySelective(successionshiftusermodel);
	}


	public String selectSuccessionUseridByshiftId(int shift_id) {
		
		return successionshiftuserdao.selectSuccessionUseridByshiftId(shift_id);
	}
	
	
	public String selectShiftUseridByshiftId(int shift_id) {
		
	  return successionshiftuserdao.selectShiftUseridByshiftId(shift_id);
	}
	/**
	 * @see 根据userid和shift_id更新数据
	 */
	@Override
	public void updateByUserAndShiftIdModel(SuccessionShiftUserModel successionshiftusermodel) {
		
		successionshiftuserdao.updateByUserAndShiftIdSelective(successionshiftusermodel);
	}

	@Override
	public List<Map<String,Object>> selectAllByshiftId(int shift_id) {
		return successionshiftuserdao.selectModelAndUserNameByshiftId(shift_id);
	}

}
