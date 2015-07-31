package com.ray.tool.util;

import java.util.Collection;
import java.util.Map;

public class Output {

	public static String dumpMap(final Map map){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if(map == null){
			sb.append("null");
		}else{
			for(Object key : map.keySet()){
				if(map.get(key)!=null && map.get(key) instanceof Map){
					sb.append(dumpMap((Map)map.get(key)));
				}else if(map.get(key)!=null && map.get(key) instanceof Collection){
					sb.append(dumpCollection((Collection)map.get(key)));
				}
				sb.append("\n").append(key).append(" ---> ").append(map.get(key)).append("\n");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static String dumpCollection(final Collection set){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if(set == null){
			sb.append("null");
		}else{
			for(Object item : set){
				if(item!=null && item instanceof Map){
					sb.append(dumpMap((Map)item));
				}else if(item!=null && item instanceof Collection){
					sb.append(dumpCollection((Collection)item));
				}
				sb.append("").append(item).append("\n");
			}
		}
		sb.append("]");
		return sb.toString();
	}
}