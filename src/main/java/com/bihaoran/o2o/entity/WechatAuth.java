package com.bihaoran.o2o.entity;

import java.util.Date;

/**
 * 微信账号
 * @author bihaoran
 *
 */
public class WechatAuth{
	private long wechatAuthId;
	private String opendId;
	private Date creatTime;
	private PersonInfo personInfo;
	public long getWechatAuthId() {
		return wechatAuthId;
	}
	public void setWechatAuthId(long wechatAuthId) {
		this.wechatAuthId = wechatAuthId;
	}
	public String getOpendId() {
		return opendId;
	}
	public void setOpendId(String opendId) {
		this.opendId = opendId;
	}
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	
	
	

}
