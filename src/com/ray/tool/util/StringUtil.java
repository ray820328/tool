package com.ray.tool.util;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static final Charset charset_ascii = Charset.forName("iso8859-1");
	public static final Charset charset_default = Charset.forName("UTF-8");
	
	/******************************* 分隔符 ********************************/
	public static final String split_equal = "=";
	public static final String split_verticalline = "\\|";
	public static final String split_underline = "\\_";
	public static final String split_and = "&";
	public static final String split_semicolon = ";";
	public static final String plain_equal = "=";
	public static final String plain_verticalline = "|";
	public static final String plain_underline = "_";
	public static final String plain_and = "&";
	public static final String plain_semicolon = ";";
	public static final String multiple_sign = "×";
	public static final char plain_verticalline_char = plain_verticalline.charAt(0);
	public static final char unique_char = 0x1B;//不可视字符esc，用来标志输入字符不含有的字符
	
    public static final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  

    public static String encoding(String source, Charset sourceCharset, Charset destCharset)throws Exception{
    	if(source == null){
    		return null;
    	}
    	return new String(source.getBytes(sourceCharset.name()), destCharset.name());
    }
    public static String[] split(String str, String regex){
    	return str.split(regex);
    }
    /**
     * 将字符串替换成可被 "|" 分割的串
     */
    public static String splitEncode(String str, String defaultReturn){
    	String ret = str==null ? defaultReturn : str.replace(plain_verticalline_char, unique_char);
    	return ret;
    }
    /**
     * 将字符串替换成原始串
     */
    public static String splitDecode(String str, String defaultReturn){
    	String ret = str==null ? defaultReturn : str.replace(unique_char, plain_verticalline_char);
    	return ret;
    }
    
	/** 产生一个随机的字符串*/  
	public static String randomString(int length) { 
	    Random random = new Random();  
	    StringBuilder buf = new StringBuilder();  
	    for (int i = 0; i < length; i++) {  
	        int num = random.nextInt(62);  
	        buf.append(characters.charAt(num));  
	    }  
	    return buf.toString();  
	}
//	/** 产生一个随机的字符串，适用于JDK 1.7 */  
//	public static String random(int length) {  
//	    StringBuilder builder = new StringBuilder(length);  
//	    for (int i = 0; i < length; i++) {  
//	        builder.append((char) (ThreadLocalRandom.current().nextInt(33, 128)));  
//	    }  
//	    return builder.toString();  
//	}
	
	public static boolean isEmailAddress(String address){
		Pattern pattern = Pattern.compile(
				"^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(address);
		return matcher.matches();
	}
	
	public static boolean isIpAddress(String ip){
		Pattern pattern = Pattern.compile(
				"\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}
	/** 去头尾特殊字符*/
	public static String trim(String source, String key, boolean head, boolean tail){
		source = source.trim();
		if(key == null){
			return source;
		}
		while(head && source.startsWith(key)){
			source = source.substring(key.length()).trim();
		}
		while(tail && source.endsWith(key)){
			source = source.substring(0, source.length() - key.length()).trim();
		}
		return source;
	}
	/** 版本号转换成int(形如0.00.001) */
	public static int parseVersion2int32(String version)throws Exception{
		String[] parts = version.split("\\.");
		return Integer.valueOf(parts[0])*100000 + Integer.valueOf(parts[1])*1000 + Integer.valueOf(parts[2]);
	}
	/** int转换成版本号(形如0.00.001) */
	public static String parseInt2version(int version){
		return "" + version / 100000 + "." + new DecimalFormat("00").format(version%100000/1000) + 
				"." + new DecimalFormat("000").format(version%1000);
	}
	/** ip转换成int */
	public static int parseIpToInt32(String ip){
		if(ip==null || "".equals(ip.trim())){
			return 0;
		}
		int end = ip.indexOf(":");
		ip = ip.substring(ip.indexOf("/")+1, end>=0 ? end : ip.length());
		List<Integer> list = parseList(ip, "\\.");
		return (list.get(0) << 24) | (list.get(1) << 16) | (list.get(2) << 8) | list.get(3);
	}
	public static Map<Integer, Integer> parseMap(String str, String split1, String split2){
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		String ss[] = str.split(split1);
		for(String s : ss){
			if(s.length() > 0){
				String p[] = s.split(split2);
				map.put(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
			}
		}
		return map;
	}
	
	public static List<List<Integer>> parseLists(String str, String split1, String split2){
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		if(str.length() > 0){
			String ss[] = str.split(split1);
			for(String s : ss){
				String ps[] = s.split(split2);
				List<Integer> list1 = new ArrayList<Integer>();
				for(String p : ps){
					if(p.length() > 0){
						list1.add(Integer.parseInt(p));
					}
				}
				list.add(list1);
			}
		}
		return list;
	}
	
	public static List<Integer> parseList(String str, String split){
		List<Integer> list = new ArrayList<Integer>();
		String ss[] = str.split(split);
		for(String s : ss){
			if(s.length() > 0){
				list.add(Integer.parseInt(s));
			}
		}
		return list;
	}
	public static String parseString(Collection<Integer> list, String split){
		StringBuffer sb = new StringBuffer();
		for(Integer id : list){
			sb.append(id).append(split);
		}
		if(sb.length() > 0)
			return sb.substring(0, sb.length() - split.length());
		return sb.toString();
	}
	public static String parseString(List<List<Integer>> lists, String join1, String join2){
		StringBuffer sb = new StringBuffer();
		for(List<Integer> list : lists){
			sb.append(parseString(list, join2)).append(join1);
		}
		if(sb.length() > 0)
			return sb.substring(0, sb.length() - join1.length());
		return sb.toString();
	}
	public static String parseString(Map<Integer, Integer> map, String split1, String split2){
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<Integer, Integer> entry : map.entrySet()){
			sb.append(entry.getKey()).append(split2).append(entry.getValue()).append(split1);
		}
		if(sb.length() > 0)
			return sb.substring(0, sb.length() - split1.length());
		return sb.toString();
	}
	public static String replace(String content, Map<String, String> map){
		String s1 = "{";
		String s2 = "}";
		int begin = content.indexOf(s1);
		int end = content.indexOf(s2);
		while(begin >= 0 && end > begin){
			String str = content.substring(begin + 1, end);
			content = content.substring(0, begin) + map.get(str) + content.substring(end + 1, content.length());
			begin = content.indexOf(s1, begin + 1);
			end = content.indexOf(s2, begin + 1);
		}
		return content;
	}
	public static String encode(String content, String oldCharsetName, String charsetName) throws Exception{
		return new String(content.getBytes(oldCharsetName), charsetName);
	}
	public static String toDBC(String input) {		
		if(null != input) {
			char c[] = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if ('\u3000' == c[i]) {
					c[i] = ' ';
				}else if(c[i] > '\uFF00' && c[i] < '\uFF5F') {
					c[i] = (char) (c[i] - 65248);
				}
			}
			String dbc = new String(c);
			return dbc;		
		}else{
			return null;		
		}	
   }
   public static String toDBCLowerCaseTrim(String input){
	   if(input != null){
		  return toDBC(input).toLowerCase().trim();
	   }
	   return null;
   }
   
	public static String encodeQQParam(String str){
		StringBuffer sb = new StringBuffer();
		for(char c : str.toCharArray()){
			String s = String.valueOf(c);
			if(!Pattern.matches("[\\w\\!\\*\\(\\)]", s)){
				int n = (int)c;
				String hex = "%" + Integer.toHexString(n);
				sb.append(hex.toUpperCase());
			}else{
				sb.append(s);
			}
		}
		return sb.toString();
	}
	public static String getSqlValue(Object object) throws Exception{
		String str = object.toString();
		if(object instanceof String){
			str = str.replaceAll("\\\\", "\\\\\\\\");
			str = str.replaceAll("\'","\\\\'");
			str = "'" + str + "'";
		}
		return str;
	}
	public static String getLikeSqlValue(String str, boolean prefix, boolean suffix) throws Exception{
		str = str.replaceAll("\\\\", "\\\\\\\\");
		str = str.replaceAll("\'","\\\\'");
		if(prefix){
			str = "%" + str;
		}
		if(suffix){
			str = str + "%";
		}
		str = "'" + str + "'";
		return str;
	}
	
	public static String uppercaseFirstLetter(String source, String gap){
		String str = "";
		String[] letters = source.split(gap);
		for(String letter : letters){
			str += String.valueOf(new StringBuilder(letter).charAt(0)).toUpperCase() + 
					letter.substring(1);
		}
		return str;
	}
	
	public static void main(String[] args){
		try{
			System.out.println("parseIpToInt32: " + parseIpToInt32("/183.17.50.14:9898"));
			String source = " , , ,990, ,90 , ,";
			System.out.println("trim: " + trim(source, ",", true, true));
			String version = "1.01.001";
			System.out.println("parseVersion2int32: " + parseVersion2int32(version));
			System.out.println("parseInt2version: " + parseInt2version(parseVersion2int32(version)));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
