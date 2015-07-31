package com.ray.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ray.tool.util.FileUtil;
import com.ray.tool.util.PropertiesUtil;
import com.ray.tool.util.StringUtil;

public class EntityTool {
	private static Logger logger = LoggerFactory.getLogger(EntityTool.class);

	public final static String key_table_prefix = "t_";
	public final static String key_drop_table = "drop table";
	public final static String key_table_head = "create table";
	public final static String key_table_tail = "ENGINE =";
	public final static String key_statement_head = "(";
	public final static String key_statement_tail = ")";
	public final static String key_primary_key = "primary key";
	public final static String[] key_coments = new String[] { "/*", "//" };

	public final static String key_imports = "_imports_";
	
	public final static String template_file = "t_";

	public static Map<String, List<String>> getTableMap(String schemaFile)
			throws Exception {
		Map<String, List<String>> tableMap = new HashMap<String, List<String>>();// tableName
																					// ->
																					// lines

		File file = new File(schemaFile);
		InputStream input = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input,
				"UTF-8"));
		try {
			String line = null;
			String tableName = null;
			List<String> list = null;
			while ((line = reader.readLine()) != null) {
				if (line.indexOf(key_drop_table) > -1
						|| line.indexOf(key_primary_key) > -1
						|| line.trim().equals(key_statement_head)
						|| line.trim().equals(key_statement_tail)) {
					// 如开始一行一个 '('
					continue;
				}

				if (line.indexOf(key_table_head) > -1) {
					tableName = line.substring(
							line.indexOf(key_table_head)
									+ key_table_head.length()).trim();
					list = new ArrayList<String>();
					tableMap.put(tableName, list);
				}
				if (tableName == null) {
					continue;
				}
				if (line.indexOf(key_table_tail) > -1) {
					tableName = null;
				}
				list.add(line);
			}
		} finally {
			reader.close();
			input.close();
		}
		return tableMap;
	}

	public static void main(String args[]) throws Exception {
		// 本地化配置文件
		Properties localProps = PropertiesUtil
				.read("file/config_localize.properties");
		logger.info("file/config_localize.properties");
		for (Map.Entry<Object, Object> entry : localProps.entrySet()) {
			logger.info(entry.getKey()
					+ "=" + PropertiesUtil.getProperty(localProps, (String) entry.getKey()));
		}

		DataConfig config = new DataConfig("file/db/db_schema2bean_config.txt");
		String dataPath = config.getValue(DataConfig.DATA_PATH);
		String javaOut = config.getValue(DataConfig.JAVA_OUT);
		String asOut = config.getValue(DataConfig.AS_OUT);
		String javaPackage = config.getValue(DataConfig.JAVA_PACKAGE);
		String javaSuffix = config.getValue(DataConfig.JAVA_SUFFIX);
		String xml_ibatis_template = config.getValue(DataConfig.xml_ibatis_template);
		String xmlOut = config.getValue(DataConfig.xml_out);
		String xmlSuffix = config.getValue(DataConfig.xml_suffix);

		Map<String, List<String>> tableMap = getTableMap(dataPath);
		for (String tableName : tableMap.keySet()) {
			// if(!tableName.startsWith(key_table_prefix)){//table必须以't_'开始
			// continue;
			// }
			String className = tableName.substring(key_table_prefix.length());
			className = StringUtil.uppercaseFirstLetter(className, "_");// 首字母大写

			List<String> lines = tableMap.get(tableName);
			/** 生成java */
			String java = createBean(tableName, className, javaPackage, lines);
			String javafilename = javaOut + className + javaSuffix;
			logger.info("java file = " + javafilename);
			File javaFile = FileUtil.createFile(javafilename, false);
			FileUtil.write(javaFile, java);
			
			/** 生成java */
			String xmlContent = createXml(xml_ibatis_template, tableName, className, javaPackage, lines);
			String xmlfilename = xmlOut + className + xmlSuffix;
			logger.info("xml file = " + xmlfilename);
			File xmlFile = FileUtil.createFile(xmlfilename, false);
			FileUtil.write(xmlFile, xmlContent);
		}
	}

	public static String createBean(String tableName, String className,
			String pack, List<String> lines) throws Exception {
		StringBuilder importStr = new StringBuilder();
//		importStr.append("\r\nimport com.db.bean.BaseBean;");
//		importStr.append("\r\nimport javax.persistence.Table;");

		StringBuffer setter = new StringBuffer();
		StringBuilder sb = new StringBuilder();
		sb.append("package ").append(pack).append(";");
		sb.append("\r\n");
		sb.append(key_imports);
		sb.append("\r\n");
		// sb.append("\r\n@Data");
		sb.append("\r\npublic class ").append(className)
				.append(" extends BaseBean<").append(className).append("> {");
		// sb.append("\r\npublic class ").append(className).append(" extends BaseBean {");
		for (String line : lines) {
			if (line.indexOf(key_table_head)>-1 || line.indexOf(key_table_tail)>-1) {
				continue;
			}
			String columnName = null;
			String type = null;
			String[] keyWords = line.split(" ");
			for (String keyWord : keyWords) {
				if (keyWord != null && !"".equals(keyWord)) {
					if (columnName == null) {// id bigint not nullauto_increment,
						columnName = keyWord;
					} else {
						type = keyWord;
						break;
					}
				}
			}
			// 属性名
			String name = columnName.startsWith("_") ? columnName.substring(1) : columnName;
			String typeStr = "";// 属性类型
			StringBuilder getter = new StringBuilder();
			StringBuilder propPrefix = new StringBuilder();
			if (columnName != null && type != null) {
				setter.append("\r\n\t").append("public ");
				getter.append("\r\n\t").append("public ");

//				if (!columnName.equals("id")) {
					if (type.toLowerCase().indexOf("char") > -1
							|| type.toLowerCase().indexOf("text") > -1) {
						typeStr = "String";
					} else if (type.toLowerCase().indexOf("bigint") > -1) {
						typeStr = "Long";
					} else if (type.toLowerCase().indexOf("int") > -1) {
						typeStr = "Integer";
					} else if (type.toLowerCase().indexOf("float") > -1) {
						typeStr = "Float";
					} else if (type.toLowerCase().indexOf("double") > -1) {
						typeStr = "Double";
					} else if (type.toLowerCase().indexOf("decimal") > -1) {
						typeStr = "BigDecimal";
						if (importStr.toString().indexOf("BigDecimal") == -1) {
							importStr
									.append("\r\nimport java.math.BigDecimal;");
						}
					} else {
						throw new RuntimeException("未知类型:" + type);
					}
					propPrefix.append("\r\n\t").append("private ").append(typeStr).append(" ").append(name)
							.append(";\t\t//").append("");// comment
//				} else {
//					propPrefix.append("\r\n\tprivate Long id;\t\t//").append("");// comment
//				}
			} else {
				logger.error("不能为空, name= " + columnName + ", type=" + type);
			}
//			if (!columnName.equals("id")) {
//				sb.append(typeStr).append(" ").append(name).append(";").append("\t\t//").append("");// comment
//			}
			sb.append(propPrefix);
			// setter
			setter.append("void set")
					.append(String.valueOf(name.charAt(0)).toUpperCase())
					.append(name.substring(1)).append("(").append(typeStr)
					.append(" ").append(name).append("){")
					.append("\r\n\t\tthis.").append(name).append(" = ")
					.append(name).append(";\r\n\t}");
			// getter
			getter.append(typeStr).append(" get")
					.append(String.valueOf(name.charAt(0)).toUpperCase())
					.append(name.substring(1)).append("(){")
					.append("\r\n\t\treturn this.").append(name)
					.append(";\r\n\t}");

			setter.append(getter).append("\r\n");
		}
		sb.append("\r\n").append(setter).append("}");

		return sb.toString().replace(key_imports, importStr.toString());
	}
	
	/**
	 * ibatis xml配置主体，粗糙能修改使用就行
	 */
	public static String createXml(String templateFile, String tableName, String className,
			String pack, List<String> lines) throws Exception {
		String[] tableNameSplits = tableName.split("_");
		String tableShortName = "";
		for(int i=1; i<tableNameSplits.length; i++){
			tableShortName += tableNameSplits[i].substring(0, 1).toLowerCase();
		}
		String mappingOR = "";
		String columnList = "";//ga.name, ga,name2
		String insertColumns = "";//name, name2, 
		String insertValues = "";
		String updateValues = "";
		String updateValuesNotNull = "";
		String idType = "Long";//id数据类型
		int lineIndex = 0;
		boolean firstColumn = true;//第一个处理的字段
		for (String line : lines) {
			if (line.indexOf(key_table_head)>-1 || line.indexOf(key_table_tail)>-1) {
				continue;
			}
			lineIndex++;
			String columnName = null;
			String type = null;
			String[] keyWords = line.split(" ");
			for (String keyWord : keyWords) {
				if (keyWord != null && !"".equals(keyWord)) {
					if (columnName == null) {// id bigint not nullauto_increment,
						columnName = keyWord;
					} else {
						type = keyWord;
						break;
					}
				}
			}
			if(lineIndex!=0 && lineIndex%6==0){
				columnList += "\r\n\t\t";
				insertColumns += "\r\n\t\t";
				insertValues += "\r\n\t\t";
				updateValues += "\r\n\t\t";
			}
			String columnNameSafe = columnName;
			if("name".equals(columnName) || "key".equals(columnName) || "level".equals(columnName) || 
					"time".equals(columnName) || "encryptKey".equals(columnName)){
				columnNameSafe = "`" + columnName + "`";
			}
			columnList += tableShortName + "." + columnNameSafe + ", ";
			
			// 属性名
			String name = columnName.startsWith("_") ? columnName.substring(1) : columnName;
			String typeStr = "";// 属性类型
			if (columnName != null && type != null) {
				if (type.toLowerCase().indexOf("char") > -1
						|| type.toLowerCase().indexOf("text") > -1) {
					typeStr = "String";
				} else if (type.toLowerCase().indexOf("bigint") > -1) {
					typeStr = "Long";
				} else if (type.toLowerCase().indexOf("int") > -1) {
					typeStr = "Integer";
				} else if (type.toLowerCase().indexOf("float") > -1) {
					typeStr = "Float";
				} else if (type.toLowerCase().indexOf("double") > -1) {
					typeStr = "Double";
				} else if (type.toLowerCase().indexOf("decimal") > -1) {
					typeStr = "java.math.BigDecimal";
				} else {
					throw new RuntimeException("未知类型:" + type);
				}
//				id字段类型
				if(columnName.equals("id")){
					idType = typeStr;
				}else{
					insertColumns += columnNameSafe + ", ";
					insertValues += "#" + columnName + "#, ";
					updateValues += columnNameSafe + "=#" + columnName + "#, ";
					if(!"id".equals(columnName.trim())){
						if(firstColumn){
							updateValuesNotNull += "\t\t" + "<isNotEmpty property=\"" + columnName + 
									"\" prepend=\"\">" + columnName + "=#" + columnName + "#</isNotEmpty>\r\n";
							firstColumn = false;
						}else{
							updateValuesNotNull += "\t\t" + "<isNotEmpty property=\"" + columnName + 
									"\" prepend=\", \">" + columnName + "=#" + columnName + "#</isNotEmpty>\r\n";
						}
					}
				}
			}
			
			mappingOR += "\t" + "<result property=\"" + name + "\" column=\"" + columnName + 
					"\" jdbcType=\"" + typeStr + "\" />\r\n";
		}
		mappingOR = mappingOR.trim();
		if(columnList.length() > 1){
			columnList = columnList.substring(0, columnList.length() - 2);
		}
		if(insertColumns.length() > 1){
			insertColumns = insertColumns.substring(0, insertColumns.length() - 2);
		}
		if(insertValues.length() > 1){
			insertValues = insertValues.substring(0, insertValues.length() - 2);
		}
		if(updateValues.length() > 1){
			updateValues = updateValues.substring(0, updateValues.length() - 2);
		}
		updateValuesNotNull = updateValuesNotNull.trim();
		
//		@ORMapping@
//		<result property="id" column="id" jdbcType="BIGINT" />
//		@namespace@
//		GameAccount
//		@BeanClass@
//		com.cmge.admin.entity.game.GameAccount
//		@classAlias@
//		gameAccount
//		@ColumnList@
//		ga.id id, ga.`token` token, 
//		@IdType@
//		Long
//		@TableName@
//		t_game_account
//		@TableShortName@
//		ga
//		@insertColumns@
//		value1, values
//		@insertValues@
//		#value1#, #value2#
//		@updateValues@
//		characterId=#characterId#, 
//		@updateValuesNotNull@
//		<isNotEmpty property="id" prepend=", ">id=#id#</isNotEmpty>
		
		String template = FileUtil.getConetnt(FileUtil.createFile(templateFile, false));
		template = template.replace("@ORMapping@", mappingOR);
		template = template.replace("@namespace@", className);
		template = template.replace("@BeanClass@", pack + "." + className);
		template = template.replace("@classAlias@", className);
		template = template.replace("@ColumnList@", columnList);
		template = template.replace("@IdType@", idType);
		template = template.replace("@TableName@", tableName);		
		template = template.replace("@TableShortName@", tableShortName);
		template = template.replace("@insertColumns@", insertColumns);
		template = template.replace("@insertValues@", insertValues);
		template = template.replace("@updateValues@", updateValues);
		template = template.replace("@updateValuesNotNull@", updateValuesNotNull);
		return template;
	}

	private static class DataConfig {
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
		public final static String JAVA_SUFFIX = "java_suffix";
		public final static String AS_PACKAGE = "as_package";
		public final static String AS_SUFFIX = "as_suffix";
		public final static String xml_ibatis_template = "xml_ibatis_template";
		public final static String xml_out = "xml_out";
		public final static String xml_suffix = "xml_suffix";

		public final static String DATA_SPLIT = "=";

		private Map<String, List<String>> map = new HashMap<String, List<String>>();// tableName
																					// ->
																					// lines

		public DataConfig(String config) throws Exception {
			File file = new File(config);
			InputStream input = new FileInputStream(file);
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(input, "UTF-8"));
				String line = null;
				while ((line = reader.readLine()) != null) {
					String p[] = line.split(DATA_SPLIT);
					List<String> list = new ArrayList<String>();
					// if(dataPath!=null && "data_path".equals(p[0])){
					// list.add(dataPath);//本地化目标路径替换配置路径
					// }else{
					list.add(p[1]);
					// }
					map.put(p[0], list);
				}
			} finally {
				input.close();
			}
		}

		public String getValue(String key) {
			return map.get(key) == null ? "" : map.get(key).get(0);
		}
	}
}
