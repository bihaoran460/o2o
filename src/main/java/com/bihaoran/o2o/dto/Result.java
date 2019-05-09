package com.bihaoran.o2o.dto;

public class Result<T> {
	
	private boolean success;
	private T data;
	private String errorMsg;
	private int errorCode;
	
	public Result() {
		
	}
	//成功时的构造器
	public Result(boolean success,T data)
	{
		this.success=success;
		this.data=data;
	}
	public Result(boolean success,int errorCode,String errorMsg)
	{
		this.success=success;
		this.errorCode=errorCode;
		this.errorMsg=errorMsg;
	}
	public boolean isSuccsee() {
		return success;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
