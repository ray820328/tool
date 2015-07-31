package com.ray.tool.util.http;

import java.util.ArrayList;
import java.util.List;

public class CommonHttpRequest {
	public final static String METHOD_GET = "GET";
	public final static String METHOD_POST = "POST";
	private String url;
	private String method;
	private boolean encoded = false;
	private int timeout = 1000;
	private List<CommonHttpRequestParam> params = new ArrayList<CommonHttpRequestParam>();
	private boolean cookieEnabled = false;
	private String result;
	
	public CommonHttpRequest(String url, String method){
		this.url = url;
		this.method = method;
		this.init();
	}
	public void init(){
		if(this.url==null || this.url.indexOf("?")==-1){
			return;
		}
//		分离uri和参数
		String paramStr = this.url.substring(this.url.indexOf("?") + 1);
//		参数
		String[] paramPairs = paramStr.split("&");
		if(paramPairs==null || paramPairs.length==0){
			return;
		}
		for(String pair : paramPairs){
			String[] keyValue = pair.split("=");
			if(keyValue==null || keyValue.length<2){
				continue;
			}
			addParam(keyValue[0], keyValue[1]);
		}
//		设置uri
		this.url = this.url.substring(0, this.url.indexOf("?"));
	}
	public void addParam(CommonHttpRequestParam param){
		params.add(param);
	}
	public void addParam(String name, Object value){
		if(value != null){
			CommonHttpRequestParam param = new CommonHttpRequestParam(name, value);
			addParam(param);
		}
	}
	public String getEncodeParamString() throws Exception{
		StringBuffer sb = new StringBuffer();
		for(CommonHttpRequestParam param : params){
			String value = URLCoder.encode(param.value.toString(), "UTF-8");
			sb.append(param.name).append("=").append(value).append("&");
		}
		if(sb.length() > 0){
			return sb.substring(0, sb.length() - 1);
		}
		return "";
	}
	public String getParamString(){
		StringBuffer sb = new StringBuffer();
		for(CommonHttpRequestParam param : params){
			sb.append(param.name).append("=").append(param.value).append("&");
		}
		if(sb.length() > 0){
			return sb.substring(0, sb.length() - 1);
		}
		return "";
	}
	public String getQeuryUri()throws Exception{
		if(this.method!=null && this.method.equals(METHOD_GET)){
			return this.params==null || this.params.size()==0 ? this.url : 
				this.url + "?" + this.getQeuryParam();
		}
		return this.url;
	}
	/**
	 * 如果已经编码（encoded=true），返回原始值，否则value重新编码
	 * @return
	 * @throws Exception
	 */
	public String getQeuryParam()throws Exception{
		return encoded ? this.getParamString() : this.getEncodeParamString();
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public boolean isEncoded() {
		return encoded;
	}
	public void setEncoded(boolean encoded) {
		this.encoded = encoded;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public List<CommonHttpRequestParam> getParams() {
		return params;
	}
	public void setParams(List<CommonHttpRequestParam> params) {
		this.params = params;
	}
	public boolean isCookieEnabled() {
		return cookieEnabled;
	}
	public void setCookieEnabled(boolean cookieEnabled) {
		this.cookieEnabled = cookieEnabled;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public static class CommonHttpRequestParam implements Comparable<CommonHttpRequestParam>{
		private String name;
		private Object value;
		public CommonHttpRequestParam(String name, Object value){
			this.name = name;
			this.value = value;
		}
		@Override
		public int compareTo(CommonHttpRequestParam param) {
			return name.compareTo(param.name);
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
	}
}
