package com.boco.eoms.base.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.base.dao.AuthorityMapper;
import com.boco.eoms.base.service.IAuthorityService;
import com.boco.eoms.base.util.HttpClientServlet;
import com.boco.eoms.base.util.StaticMethod;
import com.boco.eoms.base.util.XmlManage;


@Service("getAuthorityService")
public class AuthorityServiceImpl implements IAuthorityService{
	
	@Autowired
	private  AuthorityMapper authorityMapper;
	
	
	public static String getIp(){
		String ip = XmlManage.getFile("/com/boco/eoms/base/config/AuthorityMag.xml").getProperty("ip");
		return ip;
	}
	
	
	/**
	 * 根据userid获取权限信息
	 * 
	 * 
	 */
	public JSONObject getAuthorityByUserId(String userId, int appId, int operId) {

		JSONArray taskArr = new JSONArray();// 拼写返回的json数组
		String ip = getIp();
		String url = "http://" + ip + "/api/v1/users/" + userId + "/operations";
		JSONObject entity = HttpClientServlet.httpGetRaw(url);
		if (entity != null && entity.getJSONObject("_embedded") != null) {
			JSONObject tjson = entity.getJSONObject("_embedded");// 获取返回json数据中embedded部分
			JSONArray tArr = tjson.getJSONArray("operationIdsResourceList");// 获取数据operationIdsResourceList部分
			JSONObject tfor = new JSONObject();
			if (tArr.size() > 0) {//
				for (int i = 0; i < tArr.size(); i++) {// 遍历数组数据
					tfor = (JSONObject) tArr.get(i);
					if (appId == StaticMethod.nullObject2int(tfor.get("appId"))) {// 当确认该id权限中有该系统部分时进入
						// 插入总父节点
						Map menumap = new HashMap();

						String sOperIds = tfor.getString("operIds");// 获取此用户所有的权限id
						sOperIds = sOperIds.substring(1,sOperIds.length()-1);
						List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
						//sql遍历所有的菜单项，获取所有的父级菜单
						menuList = authorityMapper.getMenuByIteration(StaticMethod.nullObject2String(operId), sOperIds);
						if (menuList != null && menuList.size() > 0) {
							for (Map<String, Object> itemMap : menuList) {
								JSONObject reput = new JSONObject();
								reput.put("id", StaticMethod.nullObject2int(itemMap.get("MENUID")));
								reput.put("pId", StaticMethod.nullObject2int(itemMap.get("PARENTMENUID")));
								reput.put("name", StaticMethod.nullObject2String(itemMap.get("MENUNAME")));
								if ("1".equals(StaticMethod.nullObject2String(itemMap.get("ISPARENT")))) {// 判断是否为父节点 1为父节点
									reput.put("icon", "/assets/img/main/u17124.png");
								} else {
									reput.put("icon", "/assets/img/main/u19894.png");
									reput.put("urlPropertye", StaticMethod.nullObject2String(itemMap.get("URL")));
								}
								if ("1".equals(StaticMethod.nullObject2String(itemMap.get("HIDED")))) {// 判断节点是否展开 0为展开，1为收起
									reput.put("open", false);
								} else {
									reput.put("open", true);
								}
								
								taskArr.add(reput);
							}
						}
						break;
					}
				}
			}

		}
		JSONObject reqjson = new JSONObject();
		reqjson.put("remodel", taskArr);
		System.out.println(reqjson);
		return reqjson;
	}

	/**
	 * 根据userid获取角色信息（用户组）
	 */
	public JSONArray getRoleByUserId(String userId) {
		String ip = getIp();
		String url = "http://"+ip+"/api/v1/sers/"+userId+"/roles";
		JSONObject entity = HttpClientServlet.httpGetRaw(url);
		JSONObject  _embedded = entity.getJSONObject("_embedded");//返回json内_embedded
		JSONArray roleResourceList = _embedded.getJSONArray("roleResourceList");//获取地市区域数组
		return roleResourceList;
	}

	/**
	 * 根据userid获取用户信息
	 */
	public JSONObject getUserByUserId(String userId) {
		String ip = getIp();
		String url = "http://"+ip+"/api/v1/users/"+userId;
		JSONObject entity = HttpClientServlet.httpGetRaw(url);
		return entity;
	}
	
	
	/**
	 * 获取地市区域
	 */
	public JSONArray getZone() {
		String ip = getIp();
		String url = "http://"+ip+"/api/v1/zones";//拼写接口url
		JSONObject entity = HttpClientServlet.httpGetRaw(url);
		JSONObject  _embedded = entity.getJSONObject("_embedded");//返回json内_embedded
		JSONArray zoneResourceList = _embedded.getJSONArray("zoneResourceList");//获取地市区域数组
		return zoneResourceList;
	}
	/**
	 * 按区域id获取角色
	 */
	public JSONArray getRoleByZoneId(String zoneId) {
		
		//接口暂未实现
		String ip = getIp();
		String url = "http://"+ip+"/api/v1//zones/"+zoneId+"/roles";//拼写接口url
		JSONObject entity = HttpClientServlet.httpGetRaw(url);
		JSONObject  _embedded = entity.getJSONObject("_embedded");//返回json内_embedded
		JSONArray roleResourceList = _embedded.getJSONArray("roleResourceList");//获取角色数组
		return roleResourceList;
	}

	public JSONArray getGroupByZoneId(String zoneId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * 根据roleid取角色下用户组和用户
	 */
	public JSONObject getGroupByRoleId(String roleId) {
		String ip = getIp();
		String url = "http://"+ip+"/api/v1/roles/"+roleId+"/users";//拼写接口url
		JSONObject entity = HttpClientServlet.httpGetRaw(url);
		return entity;
	}
	
	/**
	 * 根据groupId取用户组下的用户
	 */
	public JSONObject getUsersByGroupId(String groupid) {
		String ip = getIp();
		String url = "http://"+ip+"/api/v1/user/groups/"+groupid+"/users";//拼写接口url
		JSONObject entity = HttpClientServlet.httpGetRaw(url);
		
		return entity;
	}
	
	
	/**
	 * 根据deptid获取user信息
	 */
	public List getUsersByDeptId(String deptid) {
		
		Map map = new HashMap();
		map.put("condition", "dept_id="+deptid);
		List list = authorityMapper.getUserByCndition(map);
		return list;
	}
	
	/**
	 * 根据parentdeptid获取dept信息
	 */
	public List getDeptByParentDeptId(String parentDeptId) {
		
		Map map = new HashMap();
		map.put("condition", "parent_dept_id="+parentDeptId);
		List list = authorityMapper.getDeptByCndition(map);
		return list;
	}
	
	/**
	 * 获取所有部门信息
	 */
	public JSONObject getAllDept() {
		String ip = getIp();
		String url = "http://"+ip+"/api/v1/depts/";//拼写接口url
		JSONObject entity = HttpClientServlet.httpGetRaw(url);
		return entity;
	}


	/**
	 * 获取所有用户组
	 */
	@Override
	public JSONObject getAllGroup() {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		List<Map> list = authorityMapper.getAllGroup();
		if(list!=null && list.size()>0){
			list.forEach((Map map) -> {
				JSONObject json = new JSONObject();
				json.put("groupId", StaticMethod.nullObject2String(map.get("groupId")));
				json.put("groupName", StaticMethod.nullObject2String(map.get("groupName")));
				jsonArray.add(json);
			});
		}
		jsonObject.put("groups", jsonArray);
		return jsonObject;
	}
}
