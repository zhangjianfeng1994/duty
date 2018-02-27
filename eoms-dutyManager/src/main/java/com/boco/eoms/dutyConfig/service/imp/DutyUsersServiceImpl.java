package com.boco.eoms.dutyConfig.service.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.eoms.dutyConfig.dao.DutyUsersMapper;
import com.boco.eoms.dutyConfig.model.DutyConfigModel;
import com.boco.eoms.dutyConfig.model.DutyUsers;
import com.boco.eoms.dutyConfig.service.DutyUsersService;

@Service
public class DutyUsersServiceImpl implements DutyUsersService {

	@Autowired
	private DutyUsersMapper dutyUsersMapper;
	
	@Override
	public DutyUsers queryByPrimaryKey(Integer dutyUsersId) {
		try {
			DutyUsers dutyUsers = this.dutyUsersMapper.selectByPrimaryKey(dutyUsersId);
			return dutyUsers;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int removeDutyUser(Integer dutyUsersId) {
		try{
			int result = this.dutyUsersMapper.deleteByPrimaryKey(dutyUsersId);
			return result;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public int addDutyUser(DutyUsers dutyUsers) {
		try {
			int result = this.dutyUsersMapper.insertSelective(dutyUsers);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	@Override
	public int addDutyUsers(DutyUsers dutyUsers) {
		try {
			List<DutyUsers> dutyUsersList = new ArrayList<>();
			dutyUsersList.add(dutyUsers);
			int result = this.dutyUsersMapper.insertBatch(dutyUsersList);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int modifyDutyUser(DutyUsers dutyUsers) {
		try {
			int result = this.dutyUsersMapper.updateByPrimaryKeySelective(dutyUsers);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Integer selectMaxId(int dutyUserId) {
		return this.dutyUsersMapper.selectMaxId();
	}

	@Override
	public DutyUsers queryByUserid(String userId) {
		try {
			DutyUsers dutyUsers = this.dutyUsersMapper.selectByUserid(userId);
			return dutyUsers;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public int save(DutyUsers dutyUsers) {
		return dutyUsersMapper.insert(dutyUsers);
	}
	
	
	/**
	 * 根据duty_config_id查询相关人员信息
	 */
	public List selectByDutyConfigId(int duty_config_id) {
		return dutyUsersMapper.selectByDutyConfigId(duty_config_id);
	}

	
	public int update(DutyUsers dutyUsers){
		return dutyUsersMapper.updateByPrimaryKey(dutyUsers);
	}
	
	
	public int deleteByConfigId(int  duty_config_id){
		return dutyUsersMapper.deleteByConfigId(duty_config_id);
	}
	
	
	public List selectByUserId(String userId,int  duty_config_id) {
		Map map = new HashMap();
		String condition = "USER_ID = '"+userId+"' and DUTY_CONFIG_ID <> "+duty_config_id;
		map.put("condition", condition);
		return dutyUsersMapper.selectByUserId(map);
	}
	
	public List selectByUserName(String userName,int  duty_config_id) {
		Map map = new HashMap();
		String condition = "USER_NAME = '"+userName+"' and DUTY_CONFIG_ID = "+duty_config_id;
		map.put("condition", condition);
		return dutyUsersMapper.selectByUserId(map);
	}

	public int deleteByDeleteIf(int duty_config_id) {
		
		return dutyUsersMapper.deleteByDeleteIf(duty_config_id);
	}
}
