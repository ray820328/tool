package com.ray.tool.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ValueUtil {
	
	@SuppressWarnings("rawtypes")
	public static void setValues(String[] fields, Object[] values, Object bean)throws Exception{
		if(fields==null || values==null || fields.length!=values.length || bean==null){
			return;
		}
		for(int i=0; i<fields.length; i++){
			if(fields[i] == null){
				continue;
			}
			Class clazz1 = bean.getClass();
			while(clazz1 != Object.class){
				Field f1 = getField(clazz1, fields[i]);
				if(f1 == null){
					continue;
				}
				f1.setAccessible(true);
				f1.set(bean, values[i]);
			}
		}
	}
	
	/**
	 * 1 -> 2
	 */
	@SuppressWarnings("rawtypes")
	public static void copy(Object object1, Object object2)throws Exception{
		if(object1!=null && object2!=null){
			Class clazz1 = object1.getClass();
			Class clazz2 = object2.getClass();
			while(clazz1 != Object.class){
				Field[] fields1 = clazz1.getDeclaredFields();
				for(Field f1 : fields1){
					f1.setAccessible(true);
					Field f2 = getField(clazz2, f1.getName());
					if(f2 == null){
						continue;
					}
					f2.setAccessible(true);
					f2.set(object2, f1.get(object1));
				}
				clazz1 = clazz1.getSuperclass();
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Field getField(Class clazz, String fieldname)throws Exception{
		Class c = clazz;
		while(c != Object.class){
			try{
				Field f1 = clazz.getDeclaredField(fieldname);
				if(f1 != null){
					return f1;//否则会取父类的方法
				}
			}catch(Exception ex){
				
			}
//			Field[] fields = c.getDeclaredFields();
//			for(Field field : fields){
//				if(field.getName().equals(fieldname)){
//					return field;
//				}
//			}
			c = c.getSuperclass();
		}
		return null;
	}
	
	public static Object getValue(Field field, Object object){
		try {
			field.setAccessible(true);
			return field.get(object);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static String toSimpleJsonString(Object object) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		Class clazz = object.getClass();
		Object value = null;
		if(clazz != Object.class){
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields){
				f.setAccessible(true);
				value = f.get(object);
				if(value != null){
					sb.append(f.getName() + ":" + f.get(object).toString() + ",");
				}
			}
			clazz = clazz.getSuperclass();
		}
		if(sb.length() > 0){
			sb.setLength(sb.length() - 1);
		}
		sb.append("}");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String toJsonString(Object object) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		Class clazz = object.getClass();
		Object value = null;
		while(clazz != Object.class){
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields){
				f.setAccessible(true);
				value = f.get(object);
				if(value != null){
					sb.append(f.getName() + ":" + f.get(object).toString() + ",");
				}
			}
			clazz = clazz.getSuperclass();
		}
		if(sb.length() > 0){
			sb.setLength(sb.length() - 1);
		}
		sb.append("}");
		return sb.toString();
	}
	
	public static String toJsonString(Map<String, Object> map) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		for(Map.Entry<String, Object> entry : map.entrySet()){
			sb.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
		}
		if(sb.length() > 1){
			sb.setLength(sb.length() - 1);
		}
		sb.append("}");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> toMap(Object object) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		Class clazz = object.getClass();
		while(clazz != Object.class){
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields){
				f.setAccessible(true);
				Object value = f.get(object);
				if(value != null){
					map.put(f.getName(), value);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return map;
	}

}
