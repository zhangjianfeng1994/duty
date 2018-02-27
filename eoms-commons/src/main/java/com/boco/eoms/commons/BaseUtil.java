package com.boco.eoms.commons;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BaseUtil {
	
	/**
	 * classpath标识
	 */
	public final static String CLASSPATH_FLAG = "classpath:";
	/**  
	    * 
	替换字符串中特殊字符
	 
	     */  
	    public static String encodeString(String strData) 
	    {  
	        if (strData == null) 
	        {  
	            return ""; 
	        }  
	        strData = replaceString(strData, "&", "&amp;"); 
	        strData = replaceString(strData, "<", "&lt;"); 
	        strData = replaceString(strData, ">", "&gt;"); 
	        strData = replaceString(strData, "'", "&apos;"); 

	        return strData; 
	    }
	    
	    /**  
	     * 替换一个字符串中的某些指定字符
	     * @param strData String 原始字符串
	     * @param regex String  要替换的字符串
	     * @param replacement String 替代字符串
	     * @return String 替换后的字符串
	     */  
	    public static String replaceString(String strData, String regex, 
	            String replacement) 
	    {  
	        if (strData == null) 
	        {  
	            return null; 
	        }  
	        int index;  
	        index = strData.indexOf(regex); 
	        String strNew = ""; 
	        if (index >= 0) 
	        {  
	            while (index >= 0) 
	            {  
	                strNew += strData.substring(0, index) + replacement; 
	                strData = strData.substring(index + regex.length()); 
	                index = strData.indexOf(regex); 
	            }  
	            strNew += strData; 
	            return strNew; 
	        }  
	        return strData; 
	    }
	    
	    /**  
	     * 合并字符串数组
	     * @param String1 []
	     * @param String2 []
	     * @return String []
	     */ 
	    public static String[] combineArray(String [] Array1,String [] Array2){
	    	

	    	String[] c = new String[Array1.length + Array2.length];

	    	  for (int j = 0; j < Array1.length; ++j) {
	    	   c[j] = Array1[j];
	    	  }

	    	  for (int j = 0; j < Array2.length; ++j) {
	    	   c[Array1.length + j] = Array2[j];
	    	  }

	    	  return c;
	    }
	    /**  
	     * 转换字符集
	     * @param String1 []
	     * @param String2 []
	     * @return String []
	     */ 
	    public static String tranferString(String pageString){
	    	
	    	try {
				return new String(pageString.getBytes("ISO-8859-1"),"utf-8").toString();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("页面解码错误！！！！");
			}
			return "";
	    }
	    
	    
	    /**
		 * 字符转换函数
		 * 
		 * @param s
		 * @return output:如果字符串为null,返回为空,否则返回该字符串
		 */
		public static String nullObject2String(Object s) {
			String str = "";
			try {
				str = s.toString();
				if("null".equals(s)){
					str="";
				}
			} catch (Exception e) {
				str = "";
			}
			
			return str;
		}
		
		  /**
		 * 字符转换函数
		 * 
		 * @param s
		 * @return output:如果字符串为null,返回为空,否则返回该字符串
		 */
		public static String nullObject2String(Object s,String tempStr) {
			String str = "";
			try {
				str = s.toString();
			} catch (Exception e) {
				str = tempStr;
			}
			return str;
		}
		

		/**
		 * 将一个对象转换为整形
		 * 
		 * @param s
		 * @return
		 */
		public static int nullObject2int(Object s) {
			String str = "";
			int i = 0;
			try {
				str = s.toString();
				i = Integer.parseInt(str);
			} catch (Exception e) {
				i = 0;
			}
			return i;
		}

		/**
		 * 将对象转换为整形
		 * 
		 * @param s
		 * @param in
		 * @return
		 */
		public static int nullObject2int(Object s, int in) {
			String str = "";
			int i = in;
			try {
				str = s.toString();
				i = Integer.parseInt(str);
			} catch (Exception e) {
				i = in;
			}
			return i;
		}

		public static Timestamp nullObject2Timestamp(Object s) {
			Timestamp str = null;
			try {
				str = Timestamp.valueOf(s.toString());
			} catch (Exception e) {
				str = null;
			}
			return str;
		}
		/**
		 *  将对象转换为时间类型
		 * 
		 * @param s
		 * @return
		 */
		public static Date nullString2Date(String str) {
			 DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		        Date date = null;  
		        try {  
		            // Fri Feb 24 00:00:00 CST 2012  
		            date = format.parse(str);   
		        } catch (ParseException e) {  
		        	date = null;  
		        }  
		          
		        return date;
		}
		/**
		 * 字符转换函数如果字符串为null,返回为空,否则返回该字符串
		 * 
		 * @param s
		 * @return
		 */
		public static String null2String(String s) {
			return s == null||s.equals("null") ? "" : s;
		}
		
		
		
		/**
		 * 将Unicode编码转换为汉字
		 * 
		 * @param s
		 * @return
		 */
		   public static String gbEncoding(final String gbString) {   
		        char[] utfBytes = gbString.toCharArray();   
		              String unicodeBytes = "";   
		               for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {   
		                    String hexB = Integer.toHexString(utfBytes[byteIndex]);   
		                      if (hexB.length() <= 2) {   
		                          hexB = "00" + hexB;   
		                     }   
		                      unicodeBytes = unicodeBytes + "\\u" + hexB;   
		                  }   
		                  System.out.println("unicodeBytes is: " + unicodeBytes);   
		                  return unicodeBytes;   
		             }   
		            
		   /**
			 * 将Unicode编码转换为汉字
			 * 
			 * @param s
			 * @return
			 */
		   public static String decodeUnicode(final String dataStr) {   
		               int start = 0;   
		                 int end = 0;   
		                final StringBuffer buffer = new StringBuffer();   
		                 while (start > -1) {   
		                    end = dataStr.indexOf("\\u", start + 2);   
		                     String charStr = "";   
		                     if (end == -1) {   
		                         charStr = dataStr.substring(start + 2, dataStr.length());   
		                    } else {   
		                        charStr = dataStr.substring(start + 2, end);   
		                     }   
		                     char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。   
		                   buffer.append(new Character(letter).toString());   
		                   start = end;   
		                 }   
		                 return buffer.toString();   
		             }
		   
		   
		   /**
			 * 获取字符串中的数字
			 * 
			 * @param s
			 * @return
			 */
		   
		   public static String getNumber(final String dataString) {    
		  
			   String regEx="[^0-9]";   
			   Pattern p = Pattern.compile(regEx);   
			   Matcher m = p.matcher(dataString);   
		   	   return m.replaceAll("").trim();
		   }
		   
		   /**
			 * 转化数字
			 * 
			 * @param s
			 * @return
			 */
		   public static String transNumber(final String numString) {    
				  
			   if(numString.length()==1){
				   return "0"+numString;
			   }else{
				   return numString;
			   }
		   	  
		   }
		   
		   
		   public static String getPoint2Per(String dataString){
			   if("NaN".equals(dataString)){
				   dataString="0";
			   }
			   
			   Double d=Double.parseDouble(dataString)*100;
			   DecimalFormat df=new DecimalFormat("##0.00");
			   
			   return df.format(d)+"%";
			   
			   
		   }
		   
		   public static String getPoint2NoPer(String dataString){
			   if("NaN".equals(dataString)){
				   dataString="0";
			   }
			   
			   Double d=Double.parseDouble(dataString);
			   DecimalFormat df=new DecimalFormat("##0.00");
			   
			   return df.format(d);
			   
			   
		   }
		   
		   
		   public static List<String[]> combineArray(Object a[], int n, int m){
			   m = m > n ? n : m;
			   int[] order = new int[m + 1];
			   for (int i = 0; i <= m; i++) {
			    order[i] = i - 1;// order[0]=-1用来作为循环判断标识
			   }
			   int count = 0;
			   int k = m;
			   boolean flag = true; // 标志找到一个有效组合
			   List<String[]> strList = new ArrayList<String[]>();
			   String[] strString = null;
			   while (order[0] == -1) {
			    strString = new String[m];
			    if (flag) // 输出符合要求的组合
			    {
			     for (int i = 1; i <= m; i++) {
			      strString[i - 1] = a[order[i]].toString();
			     }
			     count++;
			     flag = false;
			     strList.add(strString);
			    }
			    order[k]++; // 在当前位置选择新的数字
			    if (order[k] == n) // 当前位置已无数字可选，回溯
			    {
			     order[k--] = 0;
			     continue;
			    }
			    if (k < m) // 更新当前位置的下一位置的数字
			    {
			     order[++k] = order[k - 1];
			     continue;
			    }
			    if (k == m) {
			     flag = true;
			    }
			   }
			   return strList;
		   }
		   
		   
		   /**
			 * 读java包时返回的路径
			 * 
			 * @param filePath
			 *            文件路径
			 * @return
			 * @throws FileNotFoundException
			 */
			public static String getFilePathForUrl(String filePath)
					throws FileNotFoundException {
				URL url = getFileUrl(filePath);
				return url.getFile();
			}
		
			/**
			 * 获取filePath的url
			 * 
			 * @param filePath
			 * @return
			 * @throws FileNotFoundException
			 *             创建url失败将抛出MalformedURLException
			 */
			public static URL getFileUrl(String filePath) throws FileNotFoundException {
				URL url = null;
				if (filePath != null) {
					if (filePath.length() > CLASSPATH_FLAG.length()) {
						if (CLASSPATH_FLAG.equals(filePath.substring(0, CLASSPATH_FLAG
								.length()))) {
							// url =
							// loader.getResource(filePath.substring(CLASSPATH_FLAG
							// .length()));
							try {
								// 创建url失败将抛出MalformedURLException
								// url = ClassLoaderUtil
								// .getExtendResource(getPathButClasspath(filePath));
								url = BaseUtil.class.getClassLoader().getResource(
										getPathButClasspath(filePath));
								URL url1 = BaseUtil.class
										.getResource(getPathButClasspath(filePath));
								URL url2 = Thread.currentThread()
										.getContextClassLoader().getResource(
												getPathButClasspath(filePath));
								// url = new URL(URI.toString()
								// + filePath.substring(CLASSPATH_FLAG.length()));
							} catch (Exception e) {
								System.out.println(e);
								throw new FileNotFoundException(filePath
										+ "is not found.");
							}

						} else {
							// TODO 有问题，需修改
						}
					}
				}
				return url;
			}
			
			/**
			 * 去掉classpath
			 * 
			 * @param path
			 * @return
			 */
			private static String getPathButClasspath(String path) {
				return path.substring(CLASSPATH_FLAG.length());
			}
			
			
			/**
			 * @see 当前登录人
			 * @return String
			 */
			public static String getNowLoginUserId() {
				String userid = "wanggang";
				return  userid;
			}
}
