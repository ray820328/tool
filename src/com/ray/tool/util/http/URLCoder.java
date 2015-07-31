package com.ray.tool.util.http;

import java.net.URLEncoder;

public class URLCoder {
	public static String encode(String url, String code) throws Exception{
		String s = URLEncoder.encode(url, code);
		s = s.replaceAll("\\*", "%2A");
		s = s.replaceAll("\\+", "%20");
		return s;
	}
}
