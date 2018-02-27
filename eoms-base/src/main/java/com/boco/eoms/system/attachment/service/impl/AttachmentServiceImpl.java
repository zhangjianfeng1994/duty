package com.boco.eoms.system.attachment.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.base.tool.LogOutTool;
import com.boco.eoms.base.util.AttachmentStaticVariable;
import com.boco.eoms.base.util.StaticMethod;
import com.boco.eoms.base.util.UUIDHexGenerator;
import com.boco.eoms.system.attachment.dao.AttachmentMapper;
import com.boco.eoms.system.attachment.model.AttachmentInfo;
import com.boco.eoms.system.attachment.service.IAttachmentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service("IAttachmentService")
public class AttachmentServiceImpl implements IAttachmentService {

	@Autowired
	private AttachmentMapper attachmentMapper;
	
	@Transactional
	@Override
	public HashMap<String, Object> queryAttachmentInfoList(int startIndex,
			int length, String condition) {
		PageHelper.startPage(startIndex,length);
		List<AttachmentInfo> list = attachmentMapper.queryAttachmentInfoList(condition);
		Page<AttachmentInfo> attachmentInfoCount = (Page<AttachmentInfo>)list;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("attachmentInfoList", list);
		resultMap.put("total", attachmentInfoCount.getTotal());
		resultMap.put("pageIndex", startIndex);
		return resultMap;
	}
	
	@Transactional
	@Override
	public void insertAttachmentInfo(AttachmentInfo attachmentInfo) {
		attachmentMapper.insertAttachmentInfo(attachmentInfo);

	}

	@Transactional
	@Override
	public void updateAttachmentInfo(AttachmentInfo attachmentInfo) {
		attachmentMapper.updateAttachmentInfo(attachmentInfo);

	}
	
	@Transactional
	@Override
	public void deleteAttachmentInfoById(String id) {
		attachmentMapper.deleteAttachmentInfoById(id);

	}
	@Transactional
	@Override
	public void deleteAttachmentInfoByServerFileName(String serverFileName) {
		attachmentMapper.deleteAttachmentInfoByServerFileName(serverFileName);

	}
	
	
	@Transactional
	@Override
	public void insertAttachmentInfoList(List<HashMap<String, String>> list) {
		
		List<AttachmentInfo> attachmentInfoList = new ArrayList<AttachmentInfo>();
		try {
			for(int i = 0; i < list.size(); i++){
				AttachmentInfo ttachmentInfo = new AttachmentInfo();
				StaticMethod.map2BeanNormal(ttachmentInfo, list.get(i));
				ttachmentInfo.setId(UUIDHexGenerator.getInstance().getID());
				attachmentInfoList.add(ttachmentInfo);
			}
			attachmentMapper.insertAttachmentInfoList(attachmentInfoList);
		} catch (Exception e) {
			LogOutTool.error(this, "错误为：", e);
		}
	}

	@Override
	public List<AttachmentInfo> saveAttachmentInfoList(List<HashMap<String, String>> list,JSONObject userObj) throws Exception{
		String fileRootPath = StaticMethod.getUploadFilePath(AttachmentStaticVariable.FILEPATH, "");
		List<AttachmentInfo> attList = new ArrayList<AttachmentInfo>();
		if(list != null && list.size() > 0){
				list.forEach((HashMap<String, String> map) -> {
					AttachmentInfo attachmentInfo = new AttachmentInfo();
					attachmentInfo.setFileName(map.get("fileName"));
					attachmentInfo.setServerFileName(map.get("serverFileName"));
					//attachmentInfo.setUploadUserId(uploadUserId);
					attachmentInfo.setUploadTime(StaticMethod.getLocalTime());
					//attachmentInfo.setUploadUserName(uploadUserName);
					String path = "".equals(map.get("path"))?"":map.get("path").replace(fileRootPath, "");
					path = path.substring(0,path.indexOf(map.get("serverFileName")));
					attachmentInfo.setResourcePath(path);
					insertAttachmentInfo(attachmentInfo);
					attList.add(attachmentInfo);
				});
		}
		return attList;
	}

}
