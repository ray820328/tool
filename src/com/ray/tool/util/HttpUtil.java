package com.ray.tool.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.ray.tool.util.http.CommonHttpRequest;
import com.ray.tool.util.http.CookieManager;


public class HttpUtil {
	public static final Charset charset_ascii = Charset.forName("iso8859-1");
	public static final Charset charset_default = Charset.forName("UTF-8");
	
	public static CookieManager cookieManager = new CookieManager();
	
	public static String send(CommonHttpRequest request) {
		if(CommonHttpRequest.METHOD_GET.equals(request.getMethod())){
			return sendGet(request);
		}else{
			return sendPost(request);
		}
	}
	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(CommonHttpRequest request) {
    	String params = "";
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = request.getQeuryUri();
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            if(request.isCookieEnabled()){
            	cookieManager.setCookies(conn);
            }
            if(urlNameString.startsWith("https")){
            	HttpsURLConnection httpsconn = (HttpsURLConnection)conn;
	            SSLContext sc = SSLContext.getInstance("SSL");
	            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
	            httpsconn.setSSLSocketFactory(sc.getSocketFactory());
	            httpsconn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            }
            conn.setConnectTimeout(5000);
	        conn.setReadTimeout(5000);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            conn.connect();

            if(request.isCookieEnabled()){
            	cookieManager.storeCookies(conn);
            }
            String contentType = conn.getContentType();
            if(contentType != null){
            	String[] contentTypeValues = contentType.split("=");
            	contentType = contentTypeValues.length>0 ? contentTypeValues[1].trim() : "";
            }
//            Log.debug("contentType = " + contentType);
            // 获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 定义 BufferedReader输入流来读取URL的响应
            int buffer_size = 8192;
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), contentType.trim()), buffer_size);
            String line;
            while ((line = in.readLine()) != null) {
//                result += new String(line.getBytes(charset_ascii), contentType.trim()) + "\r\n";
            	result += line + "\r\n";
            }
        } catch (Exception e) {
        	Log.error(request.getUrl() + params, e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(CommonHttpRequest request) {
        PrintWriter out = null;
        BufferedReader in = null;
    	String params = "";
        String result = "";
        try {
            URL realUrl = new URL(request.getQeuryUri());
        	params = request.getQeuryParam();
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            if(request.isCookieEnabled()){
            	cookieManager.setCookies(conn);
            }
            if(request.getUrl().startsWith("https")){
            	HttpsURLConnection httpsconn = (HttpsURLConnection)conn;
	            SSLContext sc = SSLContext.getInstance("SSL");
	            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
	            httpsconn.setSSLSocketFactory(sc.getSocketFactory());
	            httpsconn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            }
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(params);
            // flush输出流的缓冲
            out.flush();
            
            //获取服务器响应代码  
//            int responsecode = conn.getResponseCode();  
            String contentType = conn.getContentType();
            if(contentType != null){
            	String[] contentTypeValues = contentType.split("=");
            	contentType = contentTypeValues.length>0 ? contentTypeValues[1].trim() : "";
            }
//            Log.debug("contentType = " + contentType);
//            if(responsecode == 200){
//            	
//            }
            // 定义BufferedReader输入流来读取URL的响应
            int buffer_size = 8192;
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), contentType.trim()), buffer_size);
            if(request.isCookieEnabled()){
            	cookieManager.storeCookies(conn);
            }
            String line;
            while ((line = in.readLine()) != null) {
//                result += new String(line.getBytes(charset_ascii), contentType.trim()) + "\r\n";
            	result += line + "\r\n";
            }
        } catch (Exception e) {
        	Log.error(request.getUrl() + params, e);
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

}
