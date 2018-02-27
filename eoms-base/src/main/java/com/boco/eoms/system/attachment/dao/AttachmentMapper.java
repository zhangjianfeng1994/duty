package com.boco.eoms.system.attachment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.boco.eoms.system.attachment.model.AttachmentInfo;

/**
 * @ClassName: AttachmentMapper
 * @Description: 附件信息增删改查
 * @author szy
 * @date 2017-08-25 10:34:35
 * @version V1.0
 * @since JDK 1.8
 * 附件信息增删改查
*/
public interface AttachmentMapper {
	
	/**
	 * 查询附件列表信息
	 * @param AttachmentInfo
	 * @return List<AttachmentInfo>
	 */
	public List<AttachmentInfo> queryAttachmentInfoList(@Param("condition")String condition);
	
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
	 * @param List<AttachmentInfo>
	 * @return void
	 */
	public void insertAttachmentInfoList(List<AttachmentInfo> list);
	
	
	
}
