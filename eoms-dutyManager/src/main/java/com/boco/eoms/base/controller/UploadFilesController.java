package com.boco.eoms.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.boco.eoms.base.service.IAuthorityService;
import com.boco.eoms.base.tool.LogOutTool;
import com.boco.eoms.base.util.AttachmentStaticVariable;
import com.boco.eoms.base.util.FileHandingUtil;
import com.boco.eoms.base.util.StaticMethod;
import com.boco.eoms.system.attachment.model.AttachmentInfo;
import com.boco.eoms.system.attachment.service.IAttachmentService;

/**
 * 附件处理控制层
 * 
 * */
@Controller
@RequestMapping("/uploadFilesController")
public class UploadFilesController {

	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
	private IAuthorityService authorityService;
	/**
	 * 附件上传
	 * @author szy
	 * @version 版本1.0
	 * @param request
	 * @return JSONArray{[fileName:文件真实名字,serverFileName:文件转义名字,path:服务器上保存路径],
						 [fileName:文件真实名字,serverFileName:文件转义名字,path:服务器上保存路径]}
	 * @throws Exception
	 */
	@RequestMapping(value = { "uploadFilesMethod" }, method = RequestMethod.POST)
	@ResponseBody
	public JSONArray uploadFilesMethod(HttpServletRequest request) throws Exception {
		//JSON对象里有时间对象，进行序列化
		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"; 
		String pathName = StaticMethod.nullObject2String(request.getParameter("pathName"));
		String uId = StaticMethod.nullObject2String(request.getParameter("uId"));
//		LogOutTool.info(getClass(), "pathName="+pathName+" uId="+uId);
		//获取uId对应的用户对象
		JSONObject userObj = new JSONObject();//authorityService.getUserByUserId(uId);
		LogOutTool.info(getClass(), "等待权限管理完善======================");
		FileHandingUtil util = new FileHandingUtil();
		JSONArray arry = new JSONArray();
		//上传附件
		List<HashMap<String, String>> list = util.uploadFile(request, pathName);
		
//		LogOutTool.info(getClass(), userObj.toJSONString());
		//保存附件入库
		List<AttachmentInfo> attlist = attachmentService.saveAttachmentInfoList(list,userObj);

		if(attlist != null && attlist.size()>0 ){
			String jsonStr = JSON.toJSONString(attlist,SerializerFeature.WriteDateUseDateFormat);
			arry = JSON.parseArray(jsonStr);
		}
		LogOutTool.info(getClass(), arry.toJSONString());
		return arry;

	}
	/**
	 * 附件删除
	 * @author szy
	 * @version 版本1.0
	 * @param request
	 * @return JSONObject[result:"success"]
	 * @throws Exception
	 */
	@RequestMapping(value = { "deleteFilesMethod" }, method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteFilesMethod(HttpServletRequest request) throws Exception {
		String serverFileName = request.getParameter("serverFileName");
		LogOutTool.info(UploadFilesController.class, serverFileName);
		HashMap<String, Object> attMap = attachmentService.queryAttachmentInfoList(1, 0, " and server_filename = '"+serverFileName+"'");
		//获取需要的附件信息对象
		List<AttachmentInfo> attachmentInfoList = (List<AttachmentInfo>)attMap.get("attachmentInfoList");
		AttachmentInfo attInfo = attachmentInfoList.get(0);
		//数据库中删除指定记录
		attachmentService.deleteAttachmentInfoByServerFileName(serverFileName);
		//服务器上删除指定文件
		FileHandingUtil util = new FileHandingUtil();
		if(!"".equals(attInfo.getResourcePath())){
			String deleteFilePath = StaticMethod.getUploadFilePath(AttachmentStaticVariable.FILEPATH, "")+attInfo.getResourcePath()+attInfo.getServerFileName();
			util.deleteFile(deleteFilePath);
		}
		JSONObject obj = new JSONObject();
		obj.put("result", "success");
		return obj;
	}
	/**
	 * 附件下载
	 * @author szy
	 * @version 版本1.0
	 * @param request
	 * @return JSONObject[result:"success"]
	 * @throws Exception
	 */
	@RequestMapping(value = { "downloadFilesMethod" }, method = RequestMethod.POST)
	@ResponseBody
	public JSONObject downloadFilesMethod(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String serverFileName = StaticMethod.nullObject2String(request.getParameter("serverFileName"));
		String pathName = StaticMethod.nullObject2String(request.getParameter("pathName"));
		
		LogOutTool.info(getClass(), serverFileName+pathName);
		HashMap<String, Object> attMap = attachmentService.queryAttachmentInfoList(1, 0, " and server_filename = '"+serverFileName+"'");
		//获取需要下载的附件信息对象
		List<AttachmentInfo> attachmentInfoList = (List<AttachmentInfo>)attMap.get("attachmentInfoList");
		AttachmentInfo attInfo = attachmentInfoList.get(0);
		//下载附件
		FileHandingUtil util = new FileHandingUtil();
		util.downloadFile(response, attInfo.getFileName(), attInfo.getServerFileName(),pathName );
		JSONObject obj = new JSONObject();
		obj.put("result", "success");
		return obj;
	}
	
	/**
	 * 查看附件列表
	 * @author szy
	 * @version 版本1.0
	 * @param request
	 * @return JSONObject[result:"success"]
	 * @throws Exception
	 */
	@RequestMapping(value = { "showFilesMethod" }, method = RequestMethod.POST)
	@ResponseBody
	public JSONArray showFilesMethod(HttpServletRequest request) throws Exception {
		
		//JSON对象里有时间对象，进行序列化
		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"; 
		JSONArray arry = new JSONArray();
		String value =  StaticMethod.nullObject2String(request.getParameter("value"));
		value = value.replaceAll(",", "','");
		HashMap<String, Object> map = attachmentService.queryAttachmentInfoList(1, 0, " and server_filename in ('"+value+"') order by upload_time asc");
		List<AttachmentInfo> list = (List<AttachmentInfo>)map.get("attachmentInfoList");
		if(list != null && list.size()>0 ){
			List<Map<String,Object>> arryList = new ArrayList<Map<String,Object>>();
			//为了后期获取环节预留入口
			for(AttachmentInfo info : list){
				Map<String,Object> infoMap = StaticMethod.bean2Map(info);
				arryList.add(infoMap);
			}
			String jsonStr = JSON.toJSONString(arryList,SerializerFeature.WriteDateUseDateFormat);
			arry = JSON.parseArray(jsonStr);
		}
		return arry;
	}
	
}
