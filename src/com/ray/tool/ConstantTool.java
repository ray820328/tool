package com.ray.tool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import com.ray.tool.util.PropertiesUtil;
import com.ray.tool.util.db.DBConnectionPool;

public class ConstantTool {

	public static String createAS3Constant(String tablename, String pack, String className) throws Exception{
		Properties localProps = PropertiesUtil.read("file/config_localize.properties");
		DBConnectionPool pool = new DBConnectionPool("configDB");
		pool.init("file/config_datasource.properties", PropertiesUtil.getProperty(localProps, "jdbc_url"));
		StringBuffer sb = new StringBuffer();
		sb.append("package ").append(pack);
		sb.append("\r\n{");
		sb.append("\r\n\timport com.as3.db.config.ConstantConfig;");
		sb.append("\r\n\timport app.game.models.GameData;");
		sb.append("\r\n\tpublic class ").append(className);
		sb.append("\r\n\t{");
		StringBuffer fsb = new StringBuffer();
		fsb.append("\r\n\t\tpublic static function init():void{");
		StringBuffer gfsb = new StringBuffer();
		Connection conn = pool.getConnection(false);
		try{
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery("select * from " + tablename);
			while(result.next()){
				int id = result.getInt("n_id");
				String name = result.getString("n_name");
				int value = result.getInt("n_value");
				String remark = result.getString("n_remark");
				sb.append("\r\n\t\t").append("/**").append(id).append("-").append(remark).append("*/");
				sb.append("\r\n\t\t").append("private static var _").append(name).append(":int;");
				fsb.append("\r\n\t\t\tvar config" + id + ":ConstantConfig = GameData.staticData.getConstantConfig(" + id + ");");
				fsb.append("\r\n\t\t\t_" + name + "=config" + id + ".value;");
				gfsb.append("\r\n\t\t/**").append(id).append("-").append(remark).append("*/");
				gfsb.append("\r\n\t\tpublic static function get " + name + "():int{");
				gfsb.append("\r\n\t\t\t return _" + name + ";");
				gfsb.append("\r\n\t\t}");
			}
		}finally{
			conn.close();
		}
		fsb.append("\r\n\t\t}");
		sb.append(fsb.toString());
		sb.append(gfsb.toString());
		sb.append("\r\n\t}");
		sb.append("\r\n}");
		return sb.toString();
	}
	
	public static String createJavaConstant(String tablename, String pack, String className) throws Exception{
		Properties localProps = PropertiesUtil.read("file/config_localize.properties");
		DBConnectionPool pool = new DBConnectionPool("configDB");
		pool.init("file/config_datasource.properties", PropertiesUtil.getProperty(localProps, "jdbc_url"));
		StringBuffer sb = new StringBuffer();
		sb.append("package ").append(pack).append(";");
		sb.append("\r\nimport ").append("com.db.bean.config.*").append(";");
		sb.append("\r\npublic class ").append(className).append("{");
		StringBuffer fsb = new StringBuffer();
		fsb.append("\r\n\t").append("public static void init(){");
		Connection conn = pool.getConnection(false);
		try{
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery("select * from " + tablename);
			while(result.next()){
				int id = result.getInt("n_id");
				String name = result.getString("n_name");
				int value = result.getInt("n_value");
				String remark = result.getString("n_remark");
				sb.append("\r\n\t").append("/**").append(id).append("-").append(remark).append("*/");
				sb.append("\r\n\t").append("public static int ").append(name).append(";");
				fsb.append("\r\n\t\t").append("ConstantConfig config" + id + " = DBConfigMgr.getConstantConfigById(" + id + ");");
				fsb.append("\r\n\t\t").append(name).append("=").append("config" + id + ".getValue();");
			}
		}finally{
			conn.close();
		}
		fsb.append("\r\n\t}");
		sb.append(fsb.toString());
		sb.append("\r\n}");
		return sb.toString();
	}
	
}
