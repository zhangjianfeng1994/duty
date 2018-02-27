package com.boco.eoms.shiftAlarmAct.model;

import java.util.Date;

/** DUTY_SHIFT_ALARMACT */
public class ShiftAlarmactModel {

	/** DUTY_SHIFT_ALARMACT.ALARM_ID */
	private String id;
	/** DUTY_SHIFT_ALARMACT.EQP_OBJECT_CLASS */
	private String eqpobjectclass;
	/** DUTY_SHIFT_ALARMACT.EQP_LABEL */
	private String eqplabel;
	/** DUTY_SHIFT_ALARMACT.ALARM_TITLE_TEXT */
	private String alarmtitletext;
	/** DUTY_SHIFT_ALARMACT.ORG_SEVERITY */
	private Integer orgseverity;
	/** DUTY_SHIFT_ALARMACT.STATION_NAME */
	private String stationname;
	/** DUTY_SHIFT_ALARMACT.EVENT_TIME */
	private Date eventtime;
	/** DUTY_SHIFT_ALARMACT.SUBCENTER_NAME */
	private String subcentername;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEqpobjectclass() {
		return eqpobjectclass;
	}
	public void setEqpobjectclass(String eqpobjectclass) {
		this.eqpobjectclass = eqpobjectclass;
	}
	public String getEqplabel() {
		return eqplabel;
	}
	public void setEqplabel(String eqplabel) {
		this.eqplabel = eqplabel;
	}
	public String getAlarmtitletext() {
		return alarmtitletext;
	}
	public void setAlarmtitletext(String alarmtitletext) {
		this.alarmtitletext = alarmtitletext;
	}
	public Integer getOrgseverity() {
		return orgseverity;
	}
	public void setOrgseverity(Integer orgseverity) {
		this.orgseverity = orgseverity;
	}
	public String getStationname() {
		return stationname;
	}
	public void setStationname(String stationname) {
		this.stationname = stationname;
	}
	public Date getEventtime() {
		return eventtime;
	}
	public void setEventtime(Date eventtime) {
		this.eventtime = eventtime;
	}
	public String getSubcentername() {
		return subcentername;
	}
	public void setSubcentername(String subcentername) {
		this.subcentername = subcentername;
	}

	

}