package com.ray.tool.util.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ray.tool.util.PropertiesUtil;

public class DBConnectionPool {
	private static Logger logger = LoggerFactory.getLogger(DBConnectionPool.class);
	
	private String name;
    private DataSource datasource;
    public DBConnectionPool(String name){
    	this.name = name;
    }
    public void init(String config, String localUrl) throws Exception{
    	logger.info("DBConnectionPool[" + name + "] init...");
    	Properties properties = PropertiesUtil.read(config);
    	if(localUrl != null){//本地化路径替换配置路径
    		properties.put("url", localUrl);
    	}
    	datasource = BasicDataSourceFactory.createDataSource(properties);
    	logger.info("DBConnectionPool[" + name + "] init complete!");
    }
    
    public String getName() {
		return name;
	}

	public DataSource getDataSource(){
        return datasource;
    }
    
    public Connection getConnection(boolean autoCommit){
        try {
        	Connection conn = datasource.getConnection();
        	conn.setAutoCommit(autoCommit);
        	return conn;
        } catch (SQLException e) {
        	logger.error("", e);
            throw new RuntimeException("得到数据库连接失败！");
        }
    }
    
    public void free(ResultSet rs,Statement sta,Connection conn){
        try {
            if(rs!=null){
                rs.close();
            }
        } catch (Exception e) {
            logger.error("", e);
        }finally{
            try {
                if(sta!=null){
                    sta.close();
                }
            } catch (Exception e) {
            	logger.error("", e);
            }finally{
                if(conn!=null){
                    try {
                    	conn.setAutoCommit(true);
                        conn.close();
                    } catch (SQLException e) {
                    	logger.error("", e);
                    }
                }
            }
        }
    }
}
