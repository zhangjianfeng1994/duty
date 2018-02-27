package com.boco.eoms.base.util.excel;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.boco.eoms.base.util.StaticMethod;

public class ExcelImportUtil {
	
	/**
	 * 根据时间戳名称，获得文件列表
	 * */
	public static String getFilePath(String dateName) throws Exception{
		String filePath="";
		return filePath;
	}
	
	/**
	 * 返回excel文件。
	 * @param filePath 文件路径
	 * @return
	 */
	public static HSSFWorkbook getWorkbookByFile(String filePath){
		HSSFWorkbook wb = null;
        try {
        	wb = new HSSFWorkbook(new FileInputStream(filePath));
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        return wb;
	}
	
	public static HSSFRow getFirstRowByFile(String filePath){
		HSSFRow row=null;
		HSSFWorkbook wb=getWorkbookByFile(filePath);
		HSSFSheet sheet=wb.getSheetAt(0);
		if(sheet!=null){
			row=sheet.getRow(sheet.getFirstRowNum());
		}
		return row;
	}
	
	/**
     * 获取某个单元格值
     * @param cell
     * @return
     */
	public static String getCellValue(HSSFCell cell) {
		String value = null;
		if (cell != null) {
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_FORMULA:
				try {
			    	cell.getCellFormula(); 
			        value = String.valueOf(cell.getNumericCellValue()); 
			     } catch (IllegalStateException e) {
			        value = String.valueOf(cell.getRichStringCellValue());
			     }
				break;
			case HSSFCell.CELL_TYPE_NUMERIC: 
//				System.out.println("yyyy\\-m\\-d\\ h:mm:ss".equals(cell.getCellStyle().getDataFormatString()));
				if("yyyy\\-m\\-d\\ h:mm:ss".equals(cell.getCellStyle().getDataFormatString())){
					 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue()); 
					 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//					 value = String.valueOf((long) cell.getNumericCellValue());
					 value = df.format(date);
					 return value;
				}else {
				    value = String.valueOf(cell.getNumericCellValue());
				}
				break;
			case HSSFCell.CELL_TYPE_STRING:
			     value = String.valueOf(cell.getRichStringCellValue());
			     break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				 value = String.valueOf(cell.getBooleanCellValue());
			}
		}
		return value;
	}
	
	/**
	 * 根据单元格内容获取列号
	 * @param row 所属行
	 * @param value 单元格内容
	 * @return
	 */
	public static int getColumnNum(HSSFRow row,String value){
	   Iterator it=row.iterator();
	   int num=0;
	   while(it.hasNext()){
		   HSSFCell cell=	(HSSFCell)it.next();
		 String cellVaule=getCellValue(cell);
		 if(!"".equals(value)&&value.equals(cellVaule)){
			 num= cell.getColumnIndex();
		 }	
		}
	   return num;
	}
	
	public static boolean comparisonCellValue(HSSFRow standardRow,HSSFRow compareRow){
	  boolean checkFlag=false;
	  if(standardRow!=null&&compareRow!=null){
		  for(int k=standardRow.getFirstCellNum();k<=standardRow.getLastCellNum()-1;k++){  
			  HSSFCell standardCell=standardRow.getCell(k);
			  HSSFCell compareCell=compareRow.getCell(k);
			  String standardCellVaule=StaticMethod.nullObject2String(getCellValue(standardCell));
			  String compareCellVaule=StaticMethod.nullObject2String(getCellValue(compareCell));
			  if(!"".equals(standardCellVaule)&&!"".equals(compareCellVaule)&&standardCellVaule.equals(compareCellVaule)){
				  checkFlag=true; 
			  }
		  }
	  }
	  return checkFlag;
	}	
	
	/**
	 * 删除某个sheet页中的空行
	 * @param workbook
	 * @param sheetName
	 * @return
	 */
	public static HSSFWorkbook deleteNullRowBySheet(HSSFWorkbook workbook,String sheetName){
		try {
			HSSFSheet sheet=workbook.getSheet(sheetName);			 
			System.out.println("整理前总行数："+(sheet.getLastRowNum()+1)); 
			for (int i = 0;sheet!=null&& i <= sheet.getLastRowNum();) {
				HSSFRow row=sheet.getRow(i);
				if(row==null){// 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动 
					sheet.shiftRows(i+1, sheet.getLastRowNum(),-1);  
                    continue;	
				}
				boolean flag = false;
				for(int k=row.getFirstCellNum();k<=row.getLastCellNum()-1;k++){ 
					HSSFCell cell=row.getCell(k);
                    if(cell!=null&&cell.getCellType() != HSSFCell.CELL_TYPE_BLANK){  
                        flag = true;  
                        break;  
                    }  
                }
				if(flag){  
                    i++;  
                    continue;  
                }else {//如果是空白行（即可能没有数据，但是有一定格式）  
                    if(i == sheet.getLastRowNum()&&i!=0)//如果到了最后一行，直接将那一行remove掉  
                        sheet.removeRow(row);  
                    else//如果还没到最后一行，则数据往上移一行  
                        sheet.shiftRows(i+1, sheet.getLastRowNum(),-1);  
                }
			}
			System.out.println("整理后总行数："+(sheet.getLastRowNum()+1));
		}catch(Exception e){
			  
	    }
		return workbook;
	}
	
	/**
	 * 删除整个excel文件中的空行
	 * @param workbook
	 * @return
	 */
	public static HSSFWorkbook deleteNullRowAllSheet(HSSFWorkbook workbook){
		try {			
		  for(int sheetNum=0;sheetNum<workbook.getNumberOfSheets();sheetNum++){
           if(!workbook.isSheetHidden(sheetNum)){
        	   HSSFSheet sheet=workbook.getSheetAt(sheetNum);
			System.out.println("整理前总行数："+(sheet.getLastRowNum()+1)); 
			for (int i = 0;sheet!=null&& i <= sheet.getLastRowNum();) {
				HSSFRow row=sheet.getRow(i);
				if(row==null){// 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动 
					if(i!=sheet.getLastRowNum()){
						sheet.shiftRows(i+1, sheet.getLastRowNum(),-1); 
						 continue;
					}else {
						break;
					}
                   	
				}
				boolean flag = false;
				for(int k=row.getFirstCellNum();k<=row.getLastCellNum()-1;k++){ 
					HSSFCell cell=row.getCell(k);
                    if(cell!=null&&cell.getCellType() != HSSFCell.CELL_TYPE_BLANK){  
                        flag = true;  
                        break;  
                    }  
                }
				if(flag){  
                    i++;  
                    continue;  
                }else {//如果是空白行（即可能没有数据，但是有一定格式）  
                    if(i == sheet.getLastRowNum()&&i!=0)//如果到了最后一行，直接将那一行remove掉  
                        sheet.removeRow(row);  
                    else//如果还没到最后一行，则数据往上移一行  
                        sheet.shiftRows(i+1, sheet.getLastRowNum(),-1);  
                }
			}
			System.out.println("整理后总行数："+(sheet.getLastRowNum()+1));
		  }
		 }
		}catch(Exception e){
			e.printStackTrace();  
	    }
		return workbook;
	}
	
	
	public static String messageRow(int row) {
	    return "第" + (row + 1) + "行:";
	}
	
	public static boolean isNotBlank(String str) {
	    return (str != null) && (str.trim().length() > 0);
	  }
	
	public static boolean anyBlank(String[] strs) {
	    String[] arrayOfString = strs; 
	    int j = strs.length; 
	    for (int i = 0; i < j; i++) { 
	    	String s = arrayOfString[i];
		    if (!isNotBlank(s)) {
		       return true;
		    }
	    }
	    return false;
	  }
	
}
