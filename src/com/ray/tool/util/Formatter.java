package com.ray.tool.util;
/**
 * <p></p>
 * <p>Copyright (c) 2007 Sample King</p>
 * @author Ray
 * @version v1.0
 * <p>Last update (2010-8-10)</p>
 */
public class Formatter {

	/**
	 * 字符串转换短整型
	 * @param para
	 * @return
	 */
	public static short parseShort(String para){
		try{
			return Short.parseShort(para.trim());
		}catch(Exception e){
//			throw new   special exception later
		}
		return 0;
	}
	
	/**
	 * 字符串转换整型
	 * @param para
	 * @return
	 */
	public static int parseInt(String para){
		try{
			return Integer.parseInt(para.trim());
		}catch(Exception e){
//			throw new   special exception later
		}
		return 0;
	}
	
	/**
	 * 字符串转换长整型
	 * @param para
	 * @return
	 */
	public static long parseLong(String para){
		try{
			return Long.parseLong(para.trim());
		}catch(Exception e){
//			throw new   special exception later
		}
		return 0;
	}
	
	/**
	 * 字符串转换浮点型
	 * @param para
	 * @return
	 */
	public static float parseFloat(String para){
		try{
			return Float.parseFloat(para.trim());
		}catch(Exception e){
//			throw new   special exception later
		}
		return 0f;
	}
	
	/**
	 * 字符串转换布尔型
	 * @param para
	 * @return
	 */
	public static boolean parseBoolean(String para){
		try{
			return Boolean.parseBoolean(para.trim());
		}catch(Exception e){
//			throw new   special exception later
		}
		return false;
	}
}
