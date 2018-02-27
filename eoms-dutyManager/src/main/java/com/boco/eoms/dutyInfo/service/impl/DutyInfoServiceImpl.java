package com.boco.eoms.dutyInfo.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boco.eoms.base.tool.LogOutTool;
import com.boco.eoms.base.util.StaticMethod;
import com.boco.eoms.dutyInfo.dao.DutyInfoMapper;
import com.boco.eoms.dutyInfo.model.DutyInfo;
import com.boco.eoms.dutyInfo.service.DutyInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service("DutyInfoService")
public class DutyInfoServiceImpl implements DutyInfoService{

	@Autowired
	private DutyInfoMapper dutyInfoMapper;
	
	/**
	 * @see 查询值班记录列表信息
	 * @param request
	 * @return
	 * @author
	 * @throws IOException
	 */
	@Transactional
	@Override
	public HashMap<String, Object> queryDutyInfoList(int startIndex,int length,String condition) {
		PageHelper.startPage(startIndex,length);
		List<DutyInfo> dutyInfoList = dutyInfoMapper.queryDutyInfoList(condition);
		Page<DutyInfo> dutyInfoCount = (Page<DutyInfo>)dutyInfoList;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("dutyInfo", dutyInfoList);
		resultMap.put("total", dutyInfoCount.getTotal());
		resultMap.put("pageTotal", dutyInfoCount.getPages());
		resultMap.put("pageIndex", startIndex);
		return resultMap;
	}
	
	/**
	 * @see 插入一条值班记录
	 * @param request
	 * @return
	 * @author
	 * @throws IOException
	 */
	@Transactional
	@Override
	public void insertDutyInfo(DutyInfo dutyInfo) {
		dutyInfoMapper.insertDutyInfo(dutyInfo);
	}
	
	/**
	 * @see 更新一条值班记录
	 * @param request
	 * @return
	 * @author
	 * @throws IOException
	 */
	@Transactional
	@Override
	public void updateDutyInfo(DutyInfo dutyInfo) {
		dutyInfoMapper.updateDutyInfo(dutyInfo);
	}
	
	/**
	 * @see 删除一条值班记录
	 * @param request
	 * @return
	 * @author
	 * @throws IOException
	 */
	@Transactional
	@Override
	public void deleteDutyInfo(int id) {
		dutyInfoMapper.deleteDutyInfo(id);
	}
	
	/**
	 * @see 批量插入值班记录
	 * @param request
	 * @return
	 * @author
	 * @throws IOException
	 */
	@Transactional
	@Override
	public void insertDutyInfoList(List<HashMap<String, String>> list) {
		
		
		List<DutyInfo> dutyInfoList = new ArrayList<DutyInfo>();
		try {
			for(int i = 0; i < list.size(); i++){
				DutyInfo dutyInfo = new DutyInfo();
				StaticMethod.map2BeanNormal(dutyInfo, list.get(i));
				dutyInfoList.add(dutyInfo);
			}
		} catch (Exception e) {
			 //TODO: handle exception
			LogOutTool.error(this, "错误为：", e);
		}
		
		dutyInfoMapper.insertDutyInfoList(dutyInfoList);
	}
	

	@Override
	public void batchUpdateDutyInfoRemain(int remainIf, String ids) {
		if (!"".equals(ids)) {
			String[] idStrings = ids.split(",");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("resolveIf", remainIf);
			params.put("ids", idStrings);
			dutyInfoMapper.updateDutyInfoRemain(params);
		}
	}
}
