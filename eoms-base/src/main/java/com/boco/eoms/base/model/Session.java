package com.boco.eoms.base.model;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * session 对象
 * @author chenjianghe
 *
 */
public class Session implements Serializable {

	private static final long serialVersionUID = 741578523486383940L;

	/**
	 * 用户表id（主键），为portal登陆使用，由于portal使用主键与业务有关，故记入用户表id主键做为登陆条件
	 */
	private String id;
    //用户ID
	private String userid;
	//用户名称
	private String username;
	//地域ID
	private String areaId;
	//部门ID
	private String deptid;
	//部门名称
	private String deptname;
	//是否有权限
	private boolean isHavePriv; 
	//子角色ID
	private String roleid;
	//子角色名称
	private String rolename;
	//是否是管理员
	private boolean isadmin;
	//用户密码
	private String password;
	//登录时间
	private Timestamp loginTime;
	//登录时间(用于显示)
	private String loginTimeStr;
	//省份ID
	private String provinceId;
	//区县ID
	private String regionId;
	//地市ID
	private String cityId;
    //联系方式
	private String contactMobile;
	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public boolean isIsadmin() {
		return isadmin;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}


	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isAdmin() {
		return isadmin;
	}

	public void setIsadmin(boolean isadmin) {
		this.isadmin = isadmin;
	}

	public boolean isHavePriv() {
		return isHavePriv;
	}

	public void setHavePriv(boolean isHavePriv) {
		this.isHavePriv = isHavePriv;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public Timestamp getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginTimeStr() {
		return loginTimeStr;
	}

	public void setLoginTimeStr(String loginTimeStr) {
		this.loginTimeStr = loginTimeStr;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
}