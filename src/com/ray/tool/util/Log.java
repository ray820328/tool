package com.ray.tool.util;
/**
 * <p></p>
 * <p>Copyright (c) 2007 Sample King</p>
 * @author Ray
 * @version v1.0
 * <p>Last update (2010-8-10)</p>
 */
import org.apache.log4j.Logger;

public class Log {

	private final static Logger dbg = Logger.getLogger("DEBUGS");
	private final static Logger err = Logger.getLogger("ERR");
	
	public static void debug(String str){
//		if(!ServerConfig.isDebug()){
//			return;
//		}
		dbg.debug(str);
	}
	public static void info(String str){
		dbg.info(str);
	}
	
	public static void error(Object message){
		err.error(message);
	}
	public static void error(Throwable ex){
		error("", ex);
	}
	public static void error(Object message, Throwable ex){
		err.error(message, ex);
	}
}
