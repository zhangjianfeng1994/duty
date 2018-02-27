package com.boco.eoms.base.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boco.eoms.base.service.IID2NameService;
import com.boco.eoms.base.tool.LogOutTool;
import com.boco.eoms.base.util.StaticMethod;
import com.hp.hpl.sparta.ParseException;

/**
 * id转name controller
 * @author chenjianghe
 *
 */
@Controller
@RequestMapping("/id2NameController")
public class Id2NameController {
	
	@Autowired
	public IID2NameService ID2NameService;

	@RequestMapping(value = { "id2name" }, method = RequestMethod.GET)
	@ResponseBody
	public Object idToName(HttpServletRequest request)
			throws Exception{
		String id = StaticMethod.nullObject2String(request.getParameter("id"));//字典值id
		String type = StaticMethod.nullObject2String(request.getParameter("type"));//字典值类型
		String parentDictId = StaticMethod.nullObject2String(request.getParameter("parentDictId"));//父id
		
		String name = "";
		//调用id2NameService
		if(!"".equals(parentDictId)){
			 name = ID2NameService.id2Name(id, type,parentDictId);
		}else{
			 name = ID2NameService.id2Name(id, type);
		}
		
		JSONObject json  = new JSONObject();
		json.put("name",name);
		return json;
	}
}
