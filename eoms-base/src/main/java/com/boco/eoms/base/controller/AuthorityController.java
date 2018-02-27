package com.boco.eoms.base.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.base.service.IAuthorityService;
import com.boco.eoms.base.tool.LogOutTool;
import com.boco.eoms.base.util.StaticMethod;
import com.boco.eoms.system.user.service.IUserService;
import com.hp.hpl.sparta.ParseException;

/**
 * 根据用户获取相关信息controller
 * @author zhaobowen
 *
 */
@Controller
@RequestMapping("/authorityController")
public class AuthorityController {
	
	@Autowired
	private IAuthorityService getAuthorityService;
	
	@Autowired
	private IUserService userService;
	
	/**
	 * 根据userid查询菜单权限
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getAuthorityByUserId", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getAuthorityByUserId(HttpServletRequest request) throws Exception{
		String userid = StaticMethod.nullObject2String(request.getParameter("user_id"));//
		int appId = StaticMethod.nullObject2int(request.getParameter("appId"));//appid   例如 140001 
		int operId = StaticMethod.nullObject2int(request.getParameter("operId"));//模块Id  例如 故障工单 140001100
		
		System.out.println("==========================");
		JSONObject remodel = getAuthorityService.getAuthorityByUserId(userid,appId,operId);
		return remodel;
	}
	
	/**
	 * 根据userid获取用户信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getUserInfoByUserId", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getUserInfoByUserId(HttpServletRequest request) throws Exception{
		String userid = StaticMethod.nullObject2String(request.getParameter("user_id"));
		//获取用户信息
		JSONObject remodel = getAuthorityService.getUserByUserId(userid);//admin  userid：1cd09503-4a52-4d9a-beff-1db746b23b78
		
		if(remodel!=null){
			return remodel;
		}else{
			
		}
		return remodel;
	}
	
	@RequestMapping(value="getRoleByUserId", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getRoleByUserId(HttpServletRequest request) throws Exception{
		String userid = StaticMethod.nullObject2String(request.getParameter("user_id"));
		//获取用户信息
		JSONArray remodel = getAuthorityService.getRoleByUserId(userid);//admin  userid：1cd09503-4a52-4d9a-beff-1db746b23b78
		
		if(remodel!=null){
//			JSONObject  tjson = testjson.getJSONObject("_embedded");//获取返回json数据中embedded部分
//			JSONArray tArr = tjson.getJSONArray("groupResourceList");//获取用户组信息数组
			
			return null;
		}else{
			
		}
		return null;
	}
	
	
	/**
	 * 获取角色派发树
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getRoleTree", method=RequestMethod.POST)
	@ResponseBody
	public JSONArray getRoleTree(HttpServletRequest request) throws Exception{
		
		int id = StaticMethod.nullObject2int(request.getParameter("id"));
		String rId = StaticMethod.nullObject2String(request.getParameter("rId"));//获取进行查询用的id（roleId  userId groupId zoneId）
		String pId = StaticMethod.nullObject2String(request.getParameter("pId"));//获取点击节点的父节点id   0为根节点
		String nodeType = StaticMethod.nullObject2String(request.getParameter("nodeType"));
		JSONArray trees = new JSONArray();//返回前端的tree数组
//		if("1".equals(pid)){//上一级为根节点时
//			JSONArray zones = getAuthorityService.getZone();//获取地市区域数组
//			for(int i=0;i<zones.size();i++){
//				JSONObject rezone = zones.getJSONObject(i);
//				if(rid.equals(StaticMethod.nullObject2String(rezone.get("parentZoneId")))){//
//					JSONObject re = new JSONObject();
//					int index = i+1;
//					re.put("id", StaticMethod.nullObject2int(""+id+index));
//					re.put("rid", StaticMethod.nullObject2String(rezone.get("zoneId")));
//					re.put("pid", id);
//					re.put("name", StaticMethod.nullObject2String(rezone.get("zoneName")));
////					JSONArray subRole = getAuthorityService.getRoleByZoneId(StaticMethod.nullObject2String(rezone.get("zoneId")));
////					if(subRole.size()!=0){
////						re.put("isParent", true);
////					}else{
////						re.put("isParent", false);
////					}
//					re.put("isParent", true);
//					re.put("nodeType", "zone");
//					re.put("open", false);
//					trees.add(re);//set入子节点
//				}
//			}
//		}
		//点击区节点获取角色
		if("zone".equals(nodeType)){
			JSONArray subRole = getAuthorityService.getRoleByZoneId(rId);
			for(int i=0;i<subRole.size();i++){
				JSONObject role = subRole.getJSONObject(i);
				JSONObject re = new JSONObject();
				int index = i+1;
				re.put("id", StaticMethod.nullObject2int(""+id+index));
				re.put("pId", id);
				re.put("rId", StaticMethod.nullObject2String(role.get("roleId")));
				re.put("name", StaticMethod.nullObject2String(role.get("roleName")));
				JSONObject subGroup = getAuthorityService.getGroupByRoleId(StaticMethod.nullObject2String(role.get("roleId")));
				if(subGroup.size()!=0){
					re.put("isParent", true);
				}else{
					re.put("isParent", false);
				}
				re.put("nodeType", "role");
				re.put("open", false);
				trees.add(re);//set入子节点
			}
		}
		//获取用户组节点下用户
		if("group".equals(nodeType)){
			JSONObject subUsers = getAuthorityService.getUsersByGroupId(rId);
			JSONArray users = subUsers.getJSONArray("users");
			if(users!=null){
			for(int i =0;i<users.size();i++){
				JSONObject user = users.getJSONObject(i);
				JSONObject re = new JSONObject();
				int index = i+1;
				re.put("id", StaticMethod.nullObject2int(""+id+index));
				re.put("pId", id);
				re.put("rId", StaticMethod.nullObject2String(user.get("userId")));
				re.put("name", StaticMethod.nullObject2String(user.get("userName")));
				re.put("isParent", false);
				re.put("nodeType", "user");
				re.put("open", false);
				trees.add(re);
			}
			}
		}
		//获取处理过此工单的所有人的树
		if("dPerformUser".equals(nodeType)){
			String mainId = StaticMethod.nullObject2String(request.getParameter("rId"));
//					String userId = StaticMethod.nullObject2String(request.getParameter("userId"));
			List list = userService.getPreDealUserByMainId(mainId);
			
			if(list.size()!=0){
				for(int i =0;i<list.size();i++){
					JSONObject re = new JSONObject();
					Map uid = (Map) list.get(i);
					String userId = StaticMethod.nullObject2String(uid.get("OPERATEUSERID"));
					int index = i+1;
					re.put("id", StaticMethod.nullObject2int(""+id+index));
					re.put("pId", id);
					re.put("rId", userId);
					JSONObject user = getAuthorityService.getUserByUserId(userId);
					String userName = StaticMethod.nullObject2String(user.getString("userName"));
					re.put("name", userName);
					re.put("isParent", false);
					re.put("nodeType", "user");
					re.put("open", false);
					trees.add(re);
				}
			}
		}
		//点击大角色节点获取用户组
		if("role".equals(nodeType)){
			JSONObject subGroup = new JSONObject();
			if("".equals(rId)){
				subGroup = getAuthorityService.getAllGroup();
			}else{
				subGroup = getAuthorityService.getGroupByRoleId(rId);
			}
			JSONArray groups = subGroup.getJSONArray("groups");
			for(int w=0;w<groups.size();w++){
				JSONObject group = (JSONObject) groups.get(w);
				JSONObject re = new JSONObject();
				int index = w+1;
				re.put("id", StaticMethod.nullObject2int(""+id+index));
				re.put("pId", id);
				re.put("rId", StaticMethod.nullObject2String(group.get("groupId")));
				re.put("name", StaticMethod.nullObject2String(group.get("groupName")));
				JSONObject subUser = getAuthorityService.getUsersByGroupId(StaticMethod.nullObject2String(group.get("groupId")));
				JSONArray subUsers = subUser.getJSONArray("users");
				if(subUsers!=null && subUsers.size()!=0){
					re.put("isParent", true);
				}else{
					re.put("isParent", false);
				}
				re.put("nodeType", "subrole");
				re.put("icon", "/assets/img/main/subrole.gif");
				re.put("open", false);
				trees.add(re);
			}
		}
		//获取用户组节点下用户
		if("subrole".equals(nodeType)){
			JSONObject subUsers = getAuthorityService.getUsersByGroupId(rId);
			JSONArray users = subUsers.getJSONArray("users");
			if(users!=null){
			for(int i =0;i<users.size();i++){
				JSONObject user = users.getJSONObject(i);
				JSONObject re = new JSONObject();
				int index = i+1;
				re.put("id", StaticMethod.nullObject2int(""+id+index));
				re.put("pId", id);
				re.put("rId", StaticMethod.nullObject2String(user.get("userId")));
				re.put("name", StaticMethod.nullObject2String(user.get("userName")));
				re.put("isParent", false);
				re.put("nodeType", "user");
				re.put("icon", "/assets/img/main/user.png");
				re.put("open", false);
				trees.add(re);
			}
			}
		}
		return trees;
	}
	
	
	/**
	 * 获取值班配置人员树
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getTree", method=RequestMethod.POST)
	@ResponseBody
	public JSONArray getTree(HttpServletRequest request) throws Exception{
		int id = StaticMethod.nullObject2int(request.getParameter("id"));
		String rId = StaticMethod.nullObject2String(request.getParameter("rId"));//获取进行查询用的id（roleId  userId groupId zoneId）
		String pId = StaticMethod.nullObject2String(request.getParameter("pId"));//获取点击节点的父节点id   0为根节点
		String nodeType = StaticMethod.nullObject2String(request.getParameter("nodeType"));
		JSONArray trees = new JSONArray();//返回前端的tree数组
		
		if("dept".equals(nodeType)){//dept点击展现下级
			List userlist = getAuthorityService.getUsersByDeptId(StaticMethod.nullObject2String(rId));//查询下级user
			List deptlist = getAuthorityService.getDeptByParentDeptId(StaticMethod.nullObject2String(rId));//查询下级dept
			int idex = 0;
			if(deptlist.size()>0){
				Map map = new HashMap();
				for(int i =0;i<deptlist.size();i++){
					map = (Map) deptlist.get(i);
					JSONObject tree = new JSONObject();
					idex = i+1;
					String index  = "";
					if(idex<10){
						index = "0"+idex;
					}else{
						index = ""+idex;
					}
					tree.put("id", StaticMethod.nullObject2int(""+id+index));
					tree.put("pId", id);
					tree.put("rId", StaticMethod.nullObject2String(map.get("DEPT_ID")));
					tree.put("prId", rId);
					tree.put("name", StaticMethod.nullObject2String(map.get("DEPT_NAME")));
					List userlist2 = getAuthorityService.getUsersByDeptId(StaticMethod.nullObject2String(map.get("DEPT_ID")));
					List deptlist2 = getAuthorityService.getDeptByParentDeptId(StaticMethod.nullObject2String(map.get("DEPT_ID")));
					if(userlist2.size()>0||deptlist2.size()>0){//判断下级如果有内容
						tree.put("isParent", true);
						tree.put("icon", "/assets/img/main/dept.gif");
					}else{
						tree.put("isParent", false);
						tree.put("icon", "/assets/img/main/dept.gif");
					}
					tree.put("nodeType", "dept");
					tree.put("open", false);
					trees.add(tree);
				}
			}
			if(userlist.size()>0){//如果有user在下级
				Map map = new HashMap();
				int uidex = idex;
				for(int i =0;i<userlist.size();i++){
					map = (Map) userlist.get(i);
					JSONObject tree = new JSONObject();
					uidex += 1;
					String index  = "";
					if(idex<10){
						index = "0"+idex;
					}else{
						index = ""+idex;
					}
					tree.put("id", StaticMethod.nullObject2int(""+id+index));
					tree.put("pId", id);
					tree.put("rId", StaticMethod.nullObject2String(map.get("USER_ID")));
					tree.put("prId", rId);
					tree.put("name", StaticMethod.nullObject2String(map.get("USER_NAME")));
					tree.put("isParent", false);
					tree.put("icon", "/assets/img/main/user.png");
					tree.put("nodeType", "user");
					trees.add(tree);
				}
			}
		}
		if("alldept".equals(nodeType)){//根节点下获取所有dept
			JSONObject deptEntity = getAuthorityService.getAllDept();
			JSONObject _embedded  = deptEntity.getJSONObject("_embedded");
			JSONArray deptResourceList = _embedded.getJSONArray("deptResourceList");
			for(int i = 0; i<deptResourceList.size();i++){
				JSONObject dept = deptResourceList.getJSONObject(i);
				if("".equals(StaticMethod.nullObject2String(dept.get("parentDeptId")))){
					JSONObject tree = new JSONObject();
					int idex = i+1;
					String index  = "";
					if(idex<10){
						index = "0"+idex;
					}else{
						index = ""+idex;
					}
					tree.put("id", StaticMethod.nullObject2int(""+id+index));
					tree.put("pid", id);
					tree.put("rId", StaticMethod.nullObject2String(dept.get("deptId")));
//					tree.put("prId", StaticMethod.nullObject2String(dept.get("PARENT_DEPT_ID")));
					tree.put("name", StaticMethod.nullObject2String(dept.get("deptName")));
					List userlist = getAuthorityService.getUsersByDeptId(StaticMethod.nullObject2String(dept.get("deptId")));
					List deptlist = getAuthorityService.getDeptByParentDeptId(StaticMethod.nullObject2String(dept.get("deptId")));
					if(deptlist.size()>0||userlist.size()>0){//判断下级如果有内容
						tree.put("isParent", true);
						tree.put("icon", "/assets/img/main/dept.gif");
					}else{
						tree.put("isParent", false);
						tree.put("icon", "/assets/img/main/dept.gif");
					}
					tree.put("nodeType", "dept");
					tree.put("open", false);
					
					trees.add(tree);
				}
			}
		}
				
				
		return trees;
	}
	
}
