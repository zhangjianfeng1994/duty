package com.boco.eoms.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.boco.eoms.base.tool.LogOutTool;
import com.boco.eoms.base.util.excel.ExcelParse;

/** 
 * Description: 用于处理文件的公共方法，如附件上传、下载等
 * Copyright:   Copyright (c)2017
 * Company:     BOCO 
 * @author:     zsj
 * @version:    1.0 
 * Create at:   2017-07-20
 */
public class FileHandingUtil {
	
	/**
	  * 传入附件，上传至配置好的附件文件夹内
	  * 附件位置为fileHanding.xml中的fileUrl.fileRootPath配置
	  * 默认路径配置在/webapps/eoms-sheet/WEB-INF/classes/conf/fileHanding.xml下
	  * @param request
	  * @param pathName
	  * 			配置文件中所配置的路径的标签名
	  * @return List<HashMap<String, String>>
	  * 			Map里面放着：fileName 文件名
	  * 					   serverFileName 上传后的时间戳文件名
	  * 					   path 上传的文件路径
	  * @throws Exception
	  */
	public List<HashMap<String, String>> uploadFile(HttpServletRequest request,String pathName){
		//获得附件文件夹路径
		String filePath = StaticMethod.getUploadFilePath(AttachmentStaticVariable.FILEPATH, pathName);
		
		//执行附件上传工作
		try {
			List<HashMap<String, String>> list = dealUpload(request,filePath);
			LogOutTool.info(getClass(), "附件上传成功");
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			LogOutTool.info(getClass(), "附件上传失败");
			return null;
		}
	}
	
	/**
	  * 传入附件，上传至指定的文件夹路径filePath内
	  * @param request
	  * @param filePath
	  * 			需上传到的文件夹路径
	  * @return List<HashMap<String, String>>
	  * 			Map里面放着：fileName 文件名
	  * 					   serverFileName 上传后的时间戳文件名
	  * 					   path 上传的文件路径
	  * @throws Exception
	  */
	public List<HashMap<String, String>> uploadFileAnyPlace(HttpServletRequest request,String filePath){
		//判断结尾是否有“/”，没有就加上
		if(!filePath.endsWith("/")){
			filePath += "/";
		}
		//执行附件上传工作
		try {
			List<HashMap<String, String>> list = dealUpload(request,filePath);
			LogOutTool.info(getClass(), "附件上传成功");
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			LogOutTool.info(getClass(), "附件上传失败");
			return null;
		}
	}
	
	/**
	  * 上传Excel至临时文件夹，然后解析后删除
	  * 临时文件夹/webapps/eoms-sheet/upload/temp
	  * 默认路径配置在/webapps/eoms-sheet/WEB-INF/classes/conf/fileHanding.xml下
	  * @param request
	  * @param keyName fileHanding.xml的标签名
	  * 			导入excel字段所对应的英文名，按excel顺序写在标签内
	  * @return List<HashMap<String, String>>
	  * 			Map的内容：英文名1:字段1
	  * 					 英文名2:字段2
	  * 			一个map是一行excel内容，从第二行开始解析
	  * @throws Exception
	  */
	public List<HashMap<String, String>> analysisExcel(HttpServletRequest request,String keyName){
		
		List<HashMap<String, String>> readExcel = null;
		
		//获得附件文件夹路径
		String filePath = StaticMethod.getPath(AttachmentStaticVariable.FILEPATH, "fileUrl.temp");
		
		//执行附件上传工作
		try {
			List<HashMap<String, String>> list = dealUpload(request,filePath);
			if(list != null){
				//获取excel字段所对应map的key
		    	String[] key = XmlManage.getFile(AttachmentStaticVariable.FILEPATH).getProperty(keyName).split(",");
				ExcelParse excelParse = new ExcelParse();
				readExcel = excelParse.readExcel(list.get(0).get("path"), 1, key);
				if(readExcel != null){
					LogOutTool.info(getClass(), "Excel解析成功");
				}
				deleteFile(list.get(0).get("path"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LogOutTool.info(getClass(), "附件上传失败");
		}
		
		return readExcel;
	}
	
	/**
	  * 从request中获取附件并上传，返回一个List
	  * List里面是个map<文件原名,文件时间戳名,文件路径>
	  * @param request
	  * @param filePath 附件路径
	  * @return
	  * @throws Exception
	  */
	public List<HashMap<String, String>> dealUpload(HttpServletRequest request,String filePath) throws Exception{
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		//分析路径是否存在，不存在则创建路径
		boolean flag = createDir(filePath);
		
		if(flag == true){
			//将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
			CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
					request.getSession().getServletContext());
			//检查form中是否有enctype="multipart/form-data"
			if(multipartResolver.isMultipart(request)){
				//将request变成多部分request
				MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
				//获取multiRequest 中所有的文件名
				Iterator<?> iter=multiRequest.getFileNames();
				
				while(iter.hasNext()){
					//一次遍历所有文件
					MultipartFile file=multiRequest.getFile(iter.next().toString());
					
					if(file!=null){
						//取得当前文件后缀名
						String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
						
						//取得当前时间戳
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
						String time = sdf.format(new Date());
						//时间戳后加入四位随机码
						time = time +StaticMethod.getFourRandomNum();
						//取得文件名
						String fileName = file.getOriginalFilename();
						
						//实际文件名和上传后的文件名存入map,循环后放入list，并返回
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("fileName", fileName);
						map.put("serverFileName", time+suffix);
						
						String path=filePath + time + suffix;
						map.put("path", path);
						list.add(map);
						//上传
						file.transferTo(new File(path));
					}
				}
			}
		}else{
			list = null;
		}
		
		return list;
	}
	
	/**
	  * 检查附件上传路径是否存在，不存在则创建
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);  
        if (dir.exists()) {
            return true;  
        }  
        if (!destDirName.endsWith(File.separator)) {  
            destDirName = destDirName + File.separator;  
        }  
        //创建目录  
        if (dir.mkdirs()) {
        	LogOutTool.info(FileHandingUtil.class, "创建目录" + destDirName + "成功！");
            return true;  
        } else {
        	LogOutTool.info(FileHandingUtil.class, "创建目录" + destDirName + "失败！");
            return false;  
        }  
    }
	
	/**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
            	
            	LogOutTool.info(FileHandingUtil.class,"删除单个文件" + fileName + "成功！");
                return true;
            } else {
                LogOutTool.info(FileHandingUtil.class,"删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            LogOutTool.info(FileHandingUtil.class,"删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
    
    /**
     * 附件下载功能
     *
     * @param response
     * @param hbsCn
     * 			  附件中文名，下载下来的模板名字(例如：供货商列表.xlsx)
     * @param hbsEn
     *            附件英文名/时间戳名，目前附件在服务器上的名字(例如：20170731112323.xlsx)
     * @param fileSavePath
     * 			  附件的保存地址（在/conf/fileHanding.xml配置文件的标签名）
     * 			  例如：fileSavePath = fileUrl.hbs
     * 			  配置文件内容为：
     * 			  <fileUrl><hbs>hbs/</hbs></fileUrl>
     * 			  则会取到  hbs/   最后拼出路径webapps/eoms-sheet/upload/hbs/
     * @return 单个文件删除成功返回true，否则返回false
     */
    public void downloadFile(HttpServletResponse response, String hbsCnName, String hbsEnName, String fileSavePath) {
    	
    	//获得附件文件夹路径
    	String path = StaticMethod.getUploadFilePath(AttachmentStaticVariable.FILEPATH, fileSavePath);
    	String filePath = path + hbsEnName;
    	
    	try {
			 File file = new File(filePath);
			 //把文件名按UTF-8取出并按ISO8859-1编码，保证弹出窗口中的文件名中文不乱码，中文不要太多，最多支持17个中文，因为header有150个字节限制。
			 String fileName = new String(hbsCnName.getBytes("UTF-8"),"ISO8859-1");
			 //告诉浏览器输出内容为流
			 response.setContentType("application/octet-stream");
			 //Content-Disposition中指定的类型是文件的扩展名，并且弹出的下载对话框中的文件类型图片是按照文件的扩展名显示的，点保存后，文件以filename的值命名，保存类型以Content中设置的为准。注意：在设置Content-Disposition头字段之前，一定要设置Content-Type头字段。
			 response.addHeader("Content-Disposition", "attachment;filename="+fileName);
			 String len = String.valueOf(file.length());
			 response.setHeader("Content-Length", len);//设置内容长度
			 OutputStream out = response.getOutputStream();
			 FileInputStream in = new FileInputStream(file);
			 byte[] b = new byte[1024];
			 int n;
			 while((n=in.read(b))!=-1){
				 out.write(b, 0, n);
			 }
			 in.close();
			 out.close();
		 } catch (FileNotFoundException e) {
			 e.printStackTrace();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
    }
    
    /**
     * 模板下载功能
     *
     * @param response
     * @param hbsCn
     * 			  模板中文名，下载下来的模板名字(例如：XX模板.xlsx)
     * @param hbsEn
     *            模板英文名，目前模板在服务器上的名字(例如：XXmodel.xlsx)
     * @return 单个文件删除成功返回true，否则返回false
     */
    public void downloadHbs(HttpServletResponse response, String hbsCn, String hbsEn) {
    	
    	String hbsCnName = XmlManage.getFile(AttachmentStaticVariable.FILEPATH).getProperty(hbsCn) + ".xlsx";
		String hbsEnName = XmlManage.getFile(AttachmentStaticVariable.FILEPATH).getProperty(hbsEn) + ".xlsx";
		 
    	//获得附件文件夹路径
    	String path = StaticMethod.getPath(AttachmentStaticVariable.FILEPATH, "fileUrl.hbs");
    	String filePath = path + hbsEnName;
    	
    	try {
			 File file = new File(filePath);
			 //把文件名按UTF-8取出并按ISO8859-1编码，保证弹出窗口中的文件名中文不乱码，中文不要太多，最多支持17个中文，因为header有150个字节限制。
			 String fileName = new String(hbsCnName.getBytes("UTF-8"),"ISO8859-1");
			 //告诉浏览器输出内容为流
			 response.setContentType("application/octet-stream");
			 //Content-Disposition中指定的类型是文件的扩展名，并且弹出的下载对话框中的文件类型图片是按照文件的扩展名显示的，点保存后，文件以filename的值命名，保存类型以Content中设置的为准。注意：在设置Content-Disposition头字段之前，一定要设置Content-Type头字段。
			 response.addHeader("Content-Disposition", "attachment;filename="+fileName);
			 String len = String.valueOf(file.length());
			 response.setHeader("Content-Length", len);//设置内容长度
			 OutputStream out = response.getOutputStream();
			 FileInputStream in = new FileInputStream(file);
			 byte[] b = new byte[1024];
			 int n;
			 while((n=in.read(b))!=-1){
				 out.write(b, 0, n);
			 }
			 in.close();
			 out.close();
		 } catch (FileNotFoundException e) {
			 e.printStackTrace();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
    }
    
}
