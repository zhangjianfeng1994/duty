package com.boco.eoms.system.attachment.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.system.attachment.model.AttachmentInfo;

public interface IAttachmentService {
	
	/**
	 * 查询附件列表信息
	 * @param AttachmentInfo
	 * @return List<AttachmentInfo>
	 */
	public HashMap<String, Object> queryAttachmentInfoList(int startIndex,int length,String condition);
	/**
	 * 插入一条附件信息
	 * @param AttachmentInfo
	 * @return void
	 */
	public void insertAttachmentInfo(AttachmentInfo attachmentInfo);
	
	/**
	 * 更新一条附件信息
	 * @param AttachmentInfo
	 * @return void
	 */
	public void updateAttachmentInfo(AttachmentInfo attachmentInfo);
	/**
	 * 删除一条附件信息
	 * @param id
	 * @return void
	 */
	public void deleteAttachmentInfoById(String id);
	
	/**
	 * 删除一条附件信息
	 * @param serverFileName
	 * @return void
	 */
	public void deleteAttachmentInfoByServerFileName(String serverFileName);
	/**
	 * 批量插入附件信息
	 * @param List<HashMap<String, String>>
	 * @return void
	 */
	public void insertAttachmentInfoList(List<HashMap<String, String>> list);
	
	/**
	 * 保存附件信息(带业务逻辑)
	 * @param List<HashMap<String, String>>
	 * @param JSONObject userObj user对象
	 * @return JSONArray
	 * */
	public List<AttachmentInfo> saveAttachmentInfoList(List<HashMap<String, String>> list,JSONObject userObj) throws Exception;
	
}
