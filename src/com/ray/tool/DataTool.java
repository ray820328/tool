package com.ray.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ray.tool.util.FileUtil;
import com.ray.tool.util.PropertiesUtil;
import com.ray.tool.util.db.DBConnectionPool;

public class DataTool {
	private static Logger logger = LoggerFactory.getLogger(DataTool.class);
	
	public static void main(String args[]) throws Exception{
//		本地化配置文件
		Properties localProps = PropertiesUtil.read("file/config_localize.properties");
		logger.info("file/config_localize.properties");
		for(Map.Entry<Object,Object> entry : localProps.entrySet()){
			logger.info(entry.getKey() + "=" + PropertiesUtil.getProperty(localProps, (String)entry.getKey()));
		}
		
		DataConfig config = new DataConfig("file/程序数据类.txt", PropertiesUtil.getProperty(localProps, "data_path"));
		String dataPath = config.getValue(DataConfig.DATA_PATH);
		String javaOut = config.getValue(DataConfig.JAVA_OUT);
		String asOut = config.getValue(DataConfig.AS_OUT);
		String javaPackage = config.getValue(DataConfig.JAVA_PACKAGE);
		String asPackage = config.getValue(DataConfig.AS_PACKAGE);
		String javaSuffix = config.getValue(DataConfig.JAVA_SUFFIX);
		String asSuffix = config.getValue(DataConfig.AS_SUFFIX);
		List<String> data = config.getValues(DataConfig.DATA);
		DBConnectionPool pool = new DBConnectionPool("configDB");
		pool.init("file/config_datasource.properties", PropertiesUtil.getProperty(localProps, "jdbc_url"));
		for(String str : data){
			String[] p = str.split("\\|");
			/**生成数据库mysql*/
			logger.info("正在处理文件[" + dataPath + p[0] + "]");
			XSSFSheet sheet = ExcelTool.read(dataPath + p[0], p[1]);
			String sql = ExcelTool.parseCreateDBSql(sheet, p[2]);
			Connection conn = pool.getConnection(false);
			try{
				Statement stmt = conn.createStatement();
				String[] ss = sql.split(";");
				for(String s : ss){
					s = s.trim();
					if(s.length() > 0){
						try{
							stmt.executeUpdate(s + ";");
						}catch(Exception e){
							logger.info(s);
							throw e;
						}
					}
				}
				conn.commit();
			}finally{
				pool.free(null, null, conn);
			}
			if(args.length == 0){
				/**生成java*/
				String java = ExcelTool.parseCreateBean(sheet, javaPackage, p[3]);
				String javafilename = javaOut + p[3] + javaSuffix;
				logger.info(javafilename);
				File javaFile = FileUtil.createFile(javafilename, false);
				FileUtil.write(javaFile, java);
				/**生成as3*/
				String as = ExcelTool.parseCreateAs3Bean(sheet, asPackage, p[4]);
				String asfilename = asOut + p[4] + asSuffix;
				logger.info(asfilename);
				File asFile = FileUtil.createFile(asfilename, false);
				FileUtil.write(asFile, as);
			}
			logger.info("处理文件[" + dataPath + p[0] + "]完成");
		}
		if(args.length == 0){
			/**生成常量*/
			String constantAS3 = ConstantTool.createAS3Constant(config.getValue(DataConfig.CONSTANT_TABLE), config.getValue(DataConfig.CONSTANT_AS_PACKAGE), config.getValue(DataConfig.CONSTANT_AS_NAME));
			String constantAS3filename = config.getValue(DataConfig.CONSTANT_AS_OUT) + config.getValue(DataConfig.CONSTANT_AS_NAME) + asSuffix;
			logger.info("生成:" + constantAS3filename);
			File constantAS3File = FileUtil.createFile(constantAS3filename, false);
			FileUtil.write(constantAS3File, constantAS3);
			
			String constantJava = ConstantTool.createJavaConstant(config.getValue(DataConfig.CONSTANT_TABLE), config.getValue(DataConfig.CONSTANT_JAVA_PACKAGE), config.getValue(DataConfig.CONSTANT_JAVA_NAME));
			String constantJavafilename = config.getValue(DataConfig.CONSTANT_JAVA_OUT) + config.getValue(DataConfig.CONSTANT_JAVA_NAME) + javaSuffix;
			logger.info("生成:" + constantJavafilename);
			File constantJavaFile= FileUtil.createFile(constantJavafilename, false);
			FileUtil.write(constantJavaFile, constantJava);
			
			/**生成错误码*/
			Properties properties = PropertiesUtil.read("file/error_file.properties");
			String srcFile = properties.getProperty("src_file");
			String asDestFile = properties.getProperty("as_dest_file");
			logger.info("生成:" + asDestFile);
			ErrorCodeTool.parse(srcFile, asDestFile);
			String javaDestFile = properties.getProperty("java_dest_file");
			String files[] = javaDestFile.split(SystemConstants.SPLIT_VERTICALLINE);
			for(String file : files){
				logger.info("生成:" + file);
				ErrorCodeTool.parse(srcFile, file);
			}
			
			/**生成CMD*/
			Properties properties1 = PropertiesUtil.read("file/cmd_file.properties");
			String srcFile1 = properties1.getProperty("src_file");
			String javaDestFile1 = properties1.getProperty("java_dest_file");
			String files1[] = javaDestFile1.split(SystemConstants.SPLIT_VERTICALLINE);
			for(String file : files1){
				logger.info("生成:" + file);
				CmdTool.parse(srcFile1, file);
			}
		}
	}
	
	public static class DataConfig{
		public final static String CONSTANT_TABLE = "constant_table";
		public final static String CONSTANT_AS_PACKAGE = "constant_as_package";
		public final static String CONSTANT_AS_OUT = "constant_as_out";
		public final static String CONSTANT_AS_NAME = "constant_as_name";
		public final static String CONSTANT_JAVA_PACKAGE = "constant_java_package";
		public final static String CONSTANT_JAVA_OUT = "constant_java_out";
		public final static String CONSTANT_JAVA_NAME = "constant_java_name";
		public final static String DATA_PATH = "data_path";
		public final static String JAVA_OUT = "java_out";
		public final static String AS_OUT = "as_out";
		public final static String JAVA_PACKAGE = "java_package";
		public final static String AS_PACKAGE = "as_package";
		public final static String JAVA_SUFFIX = "java_suffix";
		public final static String AS_SUFFIX = "as_suffix";
		public final static String DATA = "data";
		
		public final static String DATA_SPLIT = "=";
		
		private Map<String, List<String>> map = new HashMap<String, List<String>>();
		public DataConfig(String config, String dataPath) throws Exception{
			File file = new File(config);
			InputStream input = new FileInputStream(file);
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
				String line = null;
				String dataStart = DATA + DATA_SPLIT;
				while((line = reader.readLine()) != null){
					if(!line.equals(dataStart)){
						String p[] = line.split(DATA_SPLIT);
						List<String> list = new ArrayList<String>();
						if(dataPath!=null && "data_path".equals(p[0])){
							list.add(dataPath);//本地化目标路径替换配置路径
						}else{
							list.add(p[1]);
						}
						map.put(p[0], list);
					}else{
						List<String> list = new ArrayList<String>();
						while((line = reader.readLine()) != null){
							list.add(line);
						}
						map.put(DATA, list);
						break;
					}
				}
			}finally{
				input.close();
			}
		}
		public String getValue(String key){
			return map.get(key).get(0);
		}
		public List<String> getValues(String key){
			return map.get(key);
		}
	}
}
