package com.boco.eoms.system.attachment.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;


/**
 * 附件信息单表model
 * @author sizhongyuan
 * */
@Service("attachmentInfo")
public class AttachmentInfo implements Serializable{
	
	/**
	 * 没啥用
	 */
	private static final long serialVersionUID = 1L;

	private String id;//这就是个id，不要理它
	
	private String fileName;//文件真实名称
	
	private String serverFileName;//文件保存名称
	
	private String uploadUserId;//文件上传人id
	
	private String uploadUserName;//文件上传人名称
	
	private String moduleType;//所属模块
	
	private String modelId;//所属模块Id eg.mainId
	
	private String serverIp;//上传人IP
	
	private String resourcePath;//文件上传路径
	
	//@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
	private Date uploadTime;//文件上传时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getServerFileName() {
		return serverFileName;
	}

	public void setServerFileName(String serverFileName) {
		this.serverFileName = serverFileName;
	}

	public String getUploadUserId() {
		return uploadUserId;
	}

	public void setUploadUserId(String uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	public String getUploadUserName() {
		return uploadUserName;
	}

	public void setUploadUserName(String uploadUserName) {
		this.uploadUserName = uploadUserName;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	
}
