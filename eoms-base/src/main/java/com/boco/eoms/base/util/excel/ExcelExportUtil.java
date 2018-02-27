package com.boco.eoms.base.util.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelExportUtil {
	HSSFWorkbook wb = null;
	HSSFSheet sheet = null;
	public static final int Data_Start_Row = 4;
	public static final int EveryAppCellNum = 5;//app类型格子占用cell的个数
	public static final int APP_COLS = 4;//app 显示列数目显示
	public static final short appRowHight = 500;
	public static final short applyRowHight = 1000;
	public static int oneRowCellNum = 20;//每一行cell个数
	public int currentRowNum = Data_Start_Row-1;
	
	@SuppressWarnings("deprecation")
	public HSSFSheet info(String sheetName,String[] cellList){
		// 第一步，创建一个webbook，对应一个Excel文件  
		if (null == wb) {
			wb = new HSSFWorkbook();  
		}else{
			//通过一个wb完成所有sheet的赋值,不需要重新创建
		}
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        sheet = wb.createSheet(sheetName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 1);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中  
        style.setBorderBottom((short) 1);// 下边框   
	    style.setBorderLeft((short) 1);// 左边框   
	    style.setBorderRight((short) 1);// 右边框   
		style.setBorderTop((short) 1);// 上边框 
        HSSFFont headerFont = (HSSFFont) wb.createFont(); //创建字体样式  
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗  
        headerFont.setFontName("宋体");  //设置字体类型  
        headerFont.setFontHeightInPoints((short) 9);    //设置字体大小  
        style.setFont(headerFont);
        style.setWrapText(true);
        HSSFCell cell = null;  
        for (int i = 0; i < cellList.length; i++) {
        	 cell = row.createCell((short) i);
        	 cell.setCellValue(cellList[i]);  
             cell.setCellStyle(style); 
		}
        
        return sheet;
	}
	
 
	public HSSFWorkbook getHSSFWorkbook(){
		return wb;
    }
	
	// 第五步，写入实体数据 实际应用中这些数据从数据库得到， 
	public void setData(HSSFSheet newSheet){
		sheet = newSheet;
	}
	
	
	public void ExportExcel(String fileName,HttpServletResponse response) throws IOException{
		OutputStream out = response.getOutputStream();
		response.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
		response.setContentType("application/msexcel;charset=UTF-8");
		wb.write(out);
		out.flush();
		out.close();
	}
	
	
	public void saveAndExportExcel(String saveFile,String fileName,HttpServletResponse response){
		FileOutputStream fout = null;
        try {
        	fout = new FileOutputStream(saveFile);  
			wb.write(fout);
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        File file = new File(saveFile);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		try {
			response.setHeader("Content-Disposition", "filename=" + new String(fileName.getBytes("GB2312"), "iso8859-1"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setContentLength((int) file.length());
		OutputStream out = null;
		FileInputStream fis = null;
		BufferedInputStream buff = null;
		try {
			fis = new FileInputStream(file);
			buff = new BufferedInputStream(fis);
			byte[] b = new byte[1024];
			long k = 0;
			out = response.getOutputStream();// 从response对象中得到输出流,准备下载
			// 开始循环下载
			while (k < file.length()) {
				int j = buff.read(b, 0, 1024);
				k += j;
				out.write(b, 0, j);
			}
        }catch (Exception e){  
            e.printStackTrace();  
        }finally {
        	try {
				out.flush();
				out.close();
				buff.close();
				fis.close();
				if(file.exists()){
					file.delete();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}
