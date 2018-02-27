package com.boco.eoms.commons;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeUtil {
	
	/**
	 * author :lijin;
	 * discribe :this class is base java.util.Date
	 * to simplify the Time develop
	 * &&
	 * date :2014-04;
	 **/
	private static String localPattern0="yyyy";
	private static String localPattern1="MM";
	private static String localPattern2="dd";
	private static String localPattern3= "yyyy-MM-dd";
	private static String localPattern4="HH:mm:ss";
	private static String localPattern5= "yyyy-MM-dd HH:mm:ss";
	private static String localPattern6="HH";
	private static String localPattern7="mm";
	private static String localPattern8="ss";
	private static String localPattern9= "yyyyMMddHHmmss";
	/**
	 *get time like '1990-05-01 21:12:11'
	 *return '1990-05-01 21:12:11'current
	 */
	public static String getCurrentTime(){
		SimpleDateFormat formatter = new SimpleDateFormat(localPattern5);	
		return formatter.format(new Date());
	}
	
	public static String getDateParseString(Date time,int type){
		String formatTypeString ="";
		switch (type) {
		case 3:
			formatTypeString = localPattern3;
			break;
		case 5:
			formatTypeString = localPattern5;
			break;
		case 9:
			formatTypeString = localPattern9;
			break;
		default:
			formatTypeString = localPattern5;
			break;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(formatTypeString);	
		return formatter.format(time);
	}
	
	public static Date getStringParseDate(String time,int type){
		String formatTypeString ="";
		switch (type) {
		case 3:
			formatTypeString = localPattern3;
			break;
		case 5:
			formatTypeString = localPattern5;
			break;
		case 9:
			formatTypeString = localPattern9;
			break;
		default:
			formatTypeString = localPattern5;
			break;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(formatTypeString);	
		try {
			return formatter.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *get time like '19900501211211'
	 *return '1990-05-01 21:12:11'current
	 */
	public static String getCurrentTimePure(){
		SimpleDateFormat formatter = new SimpleDateFormat(localPattern9);	
		return formatter.format(new Date());
	}
	/**
	 *get time like '1990'
	 *return '1990'current
	 */
	public static String getCurrentYear(){
		SimpleDateFormat formatter = new SimpleDateFormat(localPattern0);	
		return formatter.format(new Date());
	}
	
	/**
	 *get time like month '5'
	 *return '05'current
	 */
	public static String getCurrentMonth(){
		SimpleDateFormat formatter = new SimpleDateFormat(localPattern1);	
		return formatter.format(new Date());
	}
	/**
	 *get time like Day '1'
	 *return '01'current
	 */
	public static String getCurrentDay(){
		SimpleDateFormat formatter = new SimpleDateFormat(localPattern2);	
		return formatter.format(new Date());
	}
	
	/**
	 *get time like HH '11'
	 *return '23'current
	 */
	public static String getCurrentHour(){
		SimpleDateFormat formatter = new SimpleDateFormat(localPattern6);	
		return formatter.format(new Date());
	}
	
	/**
	 *get time like MM '11'
	 *return '60'current
	 */
	public static String getCurrentMinute(){
		SimpleDateFormat formatter = new SimpleDateFormat(localPattern7);	
		return formatter.format(new Date());
	}
	
	/**
	 *get time like second '11'
	 *return '60'current
	 */
	public static String getCurrentSecond(){
		SimpleDateFormat formatter = new SimpleDateFormat(localPattern8);	
		return formatter.format(new Date());
	}
	
	/**
	 *get time like '21:12:11'
	 *return '21:12:11'current
	 */
	public static String getCurrentTimeOnly(){
		SimpleDateFormat formatter = new SimpleDateFormat(localPattern4);	
		return formatter.format(new Date());
	}
	
	/**
	 *use year month to get days
	 *return '30'
	 */
	public static String getDayByYearMonth(String year,String month){
		switch(Integer.parseInt(month)){
			case 1:return String.valueOf(31);
			case 2:{
				if((Integer.parseInt(year)%4==0&&Integer.parseInt(year)%100!=0)||(Integer.parseInt(year)%400==0)){
					return String.valueOf(29);
				}else{
					return String.valueOf(28);
				}
			}
			case 3:return String.valueOf(31);
			case 4:return String.valueOf(30);
			case 5:return String.valueOf(31);
			case 6:return String.valueOf(30);
			case 7:return String.valueOf(31);
			case 8:return String.valueOf(31);
			case 9:return String.valueOf(30);
			case 10:return String.valueOf(31);
			case 11:return String.valueOf(30);
			case 12:return String.valueOf(31);
			default : return "0";

		}
	}
	/**
	 *use year month day to get whole days from "curenyear-1-1"
	 *
	 */
	public static String getDaysByYearMonthDay(String year,String month,String day){
		if(month=="1"){
			return day;
		}else{
			int days=0;
			for(int i=1;i<Integer.parseInt(month);i++){
				days+=Integer.parseInt(getDayByYearMonth(year,String.valueOf(i)));
			}
			return String.valueOf((days+Integer.parseInt(day)));
		}
	}
	/**
	 *use year month day to get whole days from "curenyear-1-1"
	 *
	 */
	public static String getDaysByYear(String year){
		String days = "365"; 
		  if (Integer.parseInt(year) % 400 == 0 || (Integer.parseInt(year)% 4 == 0 && Integer.parseInt(year)% 100 != 0)) { 
			  days = "366"; 
		   } 
		  return days; 
	}
	
	
	/**
	 *user date1-date2 get whole days span
	 *"2012-09-08 10:10:10"-"2012-09-15 00:00:00"
	 *the return is double to String
	 */
	public static String getWholeDaysDouble(String starDateString,String endDateString){
		SimpleDateFormat sdf=new SimpleDateFormat(localPattern5); 
		java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");  
		Date starDate=null;  
        Date endDate=null;
        double between_days=0.0;
		try {
			starDate=sdf.parse(starDateString);
			endDate = sdf.parse(endDateString);
	        Calendar cal = Calendar.getInstance();    
	        cal.setTime(starDate);    
	        long time1 = cal.getTimeInMillis();                 
	        cal.setTime(endDate);    
	        long time2 = cal.getTimeInMillis();         
	        between_days=(time2-time1)/(1000*3600*24*1.0);  
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
       return String.valueOf(df.format(between_days)); 		
	}
	
	
	/**
	 *user date1-date2 get whole days span
	 *"2012-09-08 10:10:10"-"2012-09-15 00:00:00"
	 *the return is long to String
	 */
	public static String getWholeDaysLong(String starDateString,String endDateString){
		SimpleDateFormat sdf=new SimpleDateFormat(localPattern5); 
		java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");  
		Date starDate=null;  
        Date endDate=null;
        long between_days=0;
		try {
			starDate=sdf.parse(starDateString);
			endDate = sdf.parse(endDateString);
	        Calendar cal = Calendar.getInstance();    
	        cal.setTime(starDate);    
	        long time1 = cal.getTimeInMillis();                 
	        cal.setTime(endDate);    
	        long time2 = cal.getTimeInMillis();         
	        between_days=(time2-time1)/(1000*3600*24);  
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
       return String.valueOf(df.format(between_days)); 		
	}
	
	/**
	 *add days then get the day String
	 *"2012-09-08 10:10:10" +3days
	 * 3:yyyy-MM-dd;5:yyyy-MM-dd hh:mm:ss
	 *return "2012-09-11 10:10:10"
	 */
	public static String addDaysToDate(String date,int addDay,int type){
		String formatTypeString ="";
		switch (type) {
		case 3:
			formatTypeString = localPattern3;
			break;
		case 5:
			formatTypeString = localPattern5;
			break;
		case 9:
			formatTypeString = localPattern9;
			break;
		default:
			formatTypeString = localPattern5;
			break;
		}
		
		SimpleDateFormat df=new SimpleDateFormat(formatTypeString); 
		Calendar cal=Calendar.getInstance(); 
		try {
			cal.setTime(df.parse(date));
			cal.add(Calendar.DATE,addDay); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return df.format(cal.getTime());
	}
	
	
	
	/**
	 *use year month day to get weeks
	 *52 month mod 1/ 52 month mod 2
	 */
	public static String getWeekByYear(String year){	
		return String.valueOf(Integer.parseInt(getDaysByYear(year))/7);
	}
	
	/**
	 *use year month day to get current week
	 *return "52"
	 */
	public static String getWeekNoByDate(String date){
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		 
		  Calendar calendar = Calendar.getInstance();

		  try {
			calendar.setTime(format.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
	}
	
	
	
	/**
	 *use year year month day to get week
	 *return '星期一'
	 * 
	 */
	public static String getWeekOfDate(String time){
		SimpleDateFormat sdf=new SimpleDateFormat(localPattern3); 
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        int w =0;
        try {
			cal.setTime(sdf.parse(time));
		    w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	        if (w < 0)
	            w = 0;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return weekDays[w];
    }
	

	/**
	 *获得当年的第一周的第1天
	 *return String
	 * 
	 */
	public static String getFirstDayOfFirstWeek(String year){
		SimpleDateFormat sdf=new SimpleDateFormat(localPattern0); 
		String firstDayOfFirstWeek = "";
        Calendar cal = Calendar.getInstance();
        int w =0;
        try {
			cal.setTime(sdf.parse(year));
		    w = cal.get(Calendar.DAY_OF_WEEK);
		    switch(w){
		    case 1: firstDayOfFirstWeek = year+"-01-01";break;
		    case 2: firstDayOfFirstWeek = Integer.parseInt(year)-1+"-12-31";break;
		    case 3: firstDayOfFirstWeek = Integer.parseInt(year)-1+"-12-30";break;
		    case 4: firstDayOfFirstWeek = Integer.parseInt(year)-1+"-12-29";break;
		    case 5: firstDayOfFirstWeek = Integer.parseInt(year)-1+"-12-28";break;
		    case 6: firstDayOfFirstWeek = Integer.parseInt(year)-1+"-12-27";break;
		    case 7: firstDayOfFirstWeek = Integer.parseInt(year)-1+"-12-26";break;
		    }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return firstDayOfFirstWeek;
    }
	
	/**
	 * use date "1990-05-01 00:00:00" String to get the year "1990"
	 * return "1990"
	 * 
	 */
	public static String getYearByDate(String date){
		SimpleDateFormat df=new SimpleDateFormat(localPattern5);
		SimpleDateFormat retdf=new SimpleDateFormat(localPattern0);
		String retString="";
		try {
			retString=retdf.format(df.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retString;
	}
	
	
	
	/**
	 * compare time
	 * @param t1 1  yyyy-MM-dd HH:mm:ss
	 * @param t2 2
	 * @return return 1==t1>t2 -1==t1<t2 0==t1=t2
	 */
	public static int timeCompare(String t1,String t2){  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Calendar c1=Calendar.getInstance();  
        Calendar c2=Calendar.getInstance();  
        try {  
            c1.setTime(formatter.parse(t1));  
            c2.setTime(formatter.parse(t2));  
        } catch (Exception e) {  
            e.printStackTrace();  
            System.out.print(e);
        }  
        int result=c1.compareTo(c2);  
        return result;  
    }
	
	
	/**
	 *use year  month week to get dayString
	 *return '1990-05-01 11:12:00'
	 * 
	 */
//	public static String getWeekOfDate(String year,String month,String week){
//		
//		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
//		Calendar cal=Calendar.getInstance(); 
//		cal.set(Calendar.YEAR, year); 
//		cal.set(Calendar.WEEK_OF_YEAR, 1); 
//		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
//		System.out.println(df.format(cal.getTime())); 
//		
//		
//		
////		SimpleDateFormat sdf=new SimpleDateFormat(localPattern3); 
////        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
////        Calendar cal = Calendar.getInstance();
////        int w =0;
////        try {
////			cal.setTime(sdf.parse(time));
////		    w = cal.get(Calendar.DAY_OF_WEEK) - 1;
////	        if (w < 0)
////	            w = 0;
////		} catch (ParseException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////        return weekDays[w];
//    }
	
	
	public static String getLastMonth(String timeString){  
		DateFormat fmt =new SimpleDateFormat(localPattern5);
		Calendar cl = Calendar.getInstance();
		Date dateFrom=null;
			try {
				Date date = fmt.parse(timeString);
				cl.setTime(date);
				cl.add(Calendar.MONTH, -1);	
				dateFrom = cl.getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
			return fmt.format(dateFrom);
    }
	
	public static String getLastDay(String timeString){  
		DateFormat fmt =new SimpleDateFormat(localPattern5);
		Calendar cl = Calendar.getInstance();
		Date dateFrom=null;
			try {
				Date date = fmt.parse(timeString);
				cl.setTime(date);
				cl.add(Calendar.DAY_OF_MONTH, -1);	
				dateFrom = cl.getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
			return fmt.format(dateFrom);
    }
	
	////////////////////////////////////////////
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		//System.out.println(TimeUtil.getWeekByYearMonthDay("2001", "2","1")+"-");
//		System.out.println("--------==========>>>>>"+TimeUtil.getWeekNoByDate("2013-1-1 00:00:00"));
//	}
	
}
