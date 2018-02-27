package com.mrathena.spring.data.mbg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;


public class Test {
	public static void main(String[] args) {
		
//		List<List<String>> fatherList = new LinkedList<List<String>>();
//		List<String> childList = new ArrayList<String>();
//		childList.add("before") ;
//		fatherList.add(childList);
//		childList.add("after");
//		System.out.println(fatherList);
		//test2(12345);
		//System.out.println(12345%10000);
		String a = "1124";
		String b = "1124";
		boolean c =  isItemsEquals(a,b);
		System.out.println(c);
		
		Map<String, Object> contionMap = new HashMap<String, Object>();
		contionMap.put("isInform", "0");
		contionMap.put("isHeadInform", "0");
		contionMap.put("isInform", "1");
		contionMap.put("isHeadInform", "1");
		System.out.println(contionMap);
	}
	
	public static String mergerNote(){
	String totalNote = "";
	for (int i = 0; i < 4; i++) {
		String userid = "张三";
		String note = "我不是真的说什么";
		totalNote = totalNote+userid+":"+note+";<br>";
		}
	return totalNote;
	}
	
	public static void s(int num){
		if (num<10) {
			System.out.print(num);
		}else {
			System.out.print(num%10);
			s(num/10);
		}
	}
	
	public static void test2(int num) {
        if (num < 10) {
            System.out.println(num);
        } else {
            String numStr = String.valueOf(num);
            System.out.print(num / (int) Math.pow(10, numStr.length() - 1));
            num = num % (int) Math.pow(10, numStr.length() - 1);
            test2(num);
        }
    }
	
	// json转object
		@SuppressWarnings("unchecked")
		public static Object jsonToObject(String json) {
			if (json.startsWith("[")) {
				@SuppressWarnings("rawtypes")
				List list = JSON.parseArray(json);
				List<Object> result = new LinkedList<>();
				for (Object item : list) {
					if (item != null && item.toString().startsWith("{")) {
						result.add(jsonToObject(item.toString()));
					} else {
						result.add(item);
					}
				}
				return result;
			} else if (json.startsWith("{")) {
				Map<String, Object> result = JSON.parseObject(json, LinkedHashMap.class);
				for (String key : result.keySet()) {
					Object value = result.get(key);
					if (value != null && value.toString().startsWith("{")) {
						result.put(key, jsonToObject(value.toString()));
					}
				}
				return result;
			}
			return null;
		}
		
		public static  boolean isItemsEquals(String expect_user_id, String actual_user_id) {
			if (expect_user_id.length()!= actual_user_id.length()) {
				return false;
			}
			String[] array = expect_user_id.split(",");
			for (String item : array) {
				if (actual_user_id.indexOf(item) < 0) {
					return false;
				}
			}
			array = actual_user_id.split(",");
			for (String item : array) {
				if (expect_user_id.indexOf(item) < 0) {
					return false;
				}
			}
			return true;
		
		}
}
