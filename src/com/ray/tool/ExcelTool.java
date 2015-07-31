package com.ray.tool;

import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTool {
	public static XSSFSheet read(String file, String sheetName) throws Exception{
		FileInputStream finput = new FileInputStream(file);
		try{
			XSSFWorkbook wb = new XSSFWorkbook(finput);
			XSSFSheet sheet = wb.getSheet(sheetName);
			if(sheet == null){
				throw new Exception("文件[" + file + "." + sheetName + "]" + "不存在!");
			}
			return sheet;
		}finally{
			finput.close();
		}
	}
	
	public static String parseCreateDBSql(XSSFSheet sheet, String tableName) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("DROP TABLE IF EXISTS `" + tableName + "`;\r\n");
		sb.append("CREATE TABLE `" + tableName + "` (");
		StringBuffer insertsb = new StringBuffer();
		insertsb.append("INSERT INTO `" + tableName + "` (");
		XSSFRow row0 = sheet.getRow(0);
		XSSFRow row1 = sheet.getRow(1);
		XSSFRow row2 = sheet.getRow(2);
		for(int j = 0; j < row1.getLastCellNum(); j++){
			String cell0 = row0.getCell(j).getStringCellValue().trim();
			String cell1 = row1.getCell(j).getStringCellValue().trim();
			String cell2 = row2.getCell(j).getStringCellValue().trim();
			sb.append("\r\n`" + cell2+ "` ");
			sb.append(cell1).append(" NOT NULL COMMENT ").append("'").append(cell0).append("'");
			sb.append(",");
			insertsb.append(cell2).append(",");
		}
		sb.append("\r\nPRIMARY KEY (`n_id`)");
		sb.append("\r\n)");
		sb.append(" ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;\r\n");
		insertsb.setLength(insertsb.length() - 1);
		insertsb.append(")");
		insertsb.append(" values ");
		for(int j = 3; j <= sheet.getLastRowNum(); j++){
			XSSFRow row = sheet.getRow(j);
			insertsb.append("(");
			for(int i = 0; i < row.getLastCellNum(); i++){
				XSSFCell cell = row.getCell(i);
				if(cell == null){
					System.out.println(j + "_" + i);
					continue;
				}
				String value = null;
//				System.out.println(j+"_"+i);
				if(cell.getCellType() == XSSFCell.CELL_TYPE_BLANK){
					value = "''";
				}else if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
					double v = cell.getNumericCellValue();
					String type = row1.getCell(i).getStringCellValue().trim();
					if(type.contains("tinyint") || type.contains("int") || type.contains("varchar")){
						value = String.valueOf((int)v);
					}else{
						value = String.valueOf(v);
					}
				}else if(cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA){
					FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
					CellValue cellValue = evaluator.evaluate(cell);
					value = getValue(cellValue, row1.getCell(i));
				}else if(cell.getCellType() == XSSFCell.CELL_TYPE_STRING){
					value = String.valueOf("'" + filterString(cell.getStringCellValue()) + "'");
				}else{
					throw new Exception("找不到对应类型" + cell.getCellType());
				}
				insertsb.append(value).append(",");
			}
			insertsb.setLength(insertsb.length() - 1);
			insertsb.append("),");
		}
		insertsb.setLength(insertsb.length() - 1);
		insertsb.append(";\r\n");
		if(sheet.getLastRowNum() >= 3){
			sb.append(insertsb);
		}
		return sb.toString();
	}
	private static String getValue(CellValue cell, XSSFCell declareCell) throws Exception{
		String value = null;
		if(cell.getCellType() == XSSFCell.CELL_TYPE_BLANK){
			value = "''";
		}else if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
			double v = cell.getNumberValue();
			String type = declareCell.getStringCellValue().trim();
			if(type.contains("tinyint") || type.contains("int") || type.contains("varchar")){
				value = String.valueOf((int)v);
			}else{
				value = String.valueOf(v);
			}
		}else if(cell.getCellType() == XSSFCell.CELL_TYPE_STRING){
			value = String.valueOf("'" + filterString(cell.getStringValue()) + "'");
		}else{
			throw new Exception("找不到对应类型" + cell.getCellType());
		}
		return value;
	}
	private static String filterString(String str){
		str = str.replaceAll("\\\\", "\\\\\\\\");
		return str.replaceAll("\'","\\\\'");
	}
	public static String parseCreateBean(XSSFSheet sheet, String pack, String className) throws Exception{
		StringBuffer str = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		sb.append("package ").append(pack).append(";");
		sb.append("\r\n");
		sb.append("\r\nimport com.db.bean.BaseBean;");
		sb.append("\r\n");
//		sb.append("\r\n@Data");
		sb.append("\r\npublic class ").append(className).append(" extends BaseBean<").append(className).append(">{");
		XSSFRow row0 = sheet.getRow(0);
		XSSFRow row1 = sheet.getRow(1);
		XSSFRow row2 = sheet.getRow(2);
		for(int j = 0; j < row1.getLastCellNum(); j++){
			XSSFCell cell0 = row0.getCell(j);
			XSSFCell cell1 = row1.getCell(j);
			XSSFCell cell2 = row2.getCell(j);
			String type = cell1.getStringCellValue().trim();
			String name = cell2.getStringCellValue().trim();
			String typeStr = "";
			if(!name.equals("n_id")){
				StringBuilder getter = new StringBuilder();
				sb.append("\r\n\t").append("private ");
				str.append("\r\n\t").append("public ");
				getter.append("\r\n\t").append("public ");
				if(type.contains("varchar") || type.contains("text")){
					typeStr = "String";
				}else if(type.contains("tinyint")){
					typeStr = "int";
				}else if(type.contains("int")){
					typeStr = "int";
				}else if(type.contains("float")){
					typeStr = "float";
				}else{
					throw new RuntimeException("未知类型:" + type);
				}
				sb.append(typeStr).append(" ").append(name.substring(2)).append(";").append("\t\t//").append(cell0.getStringCellValue().trim());
//				setter
				str.append("void set").append(String.valueOf(name.charAt(2)).toUpperCase()).
				append(name.substring(3)).append("(").append(typeStr).append(" ").append(name.substring(2)).
				append("){").append("\r\n\t\tthis.").append(name.substring(2)).
				append(" = ").append(name.substring(2)).append(";\r\n\t}");
//				getter
				getter.append(typeStr).append(" get").append(String.valueOf(name.charAt(2)).toUpperCase()).
				append(name.substring(3)).append("(){").append("\r\n\t\t return this.").append(name.substring(2)).
				append(";\r\n\t}");
				
				str.append(getter);
			}
		}
		sb.append(str);
		sb.append("\r\n}");
		return sb.toString();
	}
	public static String parseCreateAs3Bean(XSSFSheet sheet, String pack, String className) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("package ").append(pack);
		sb.append("\r\n{");
		sb.append("\r\n\tpublic class ").append(className);
		sb.append("\r\n\t{");
		XSSFRow row0 = sheet.getRow(0);
		XSSFRow row1 = sheet.getRow(1);
		XSSFRow row2 = sheet.getRow(2);
		for(int j = 0; j < row1.getLastCellNum(); j++){
			XSSFCell cell0 = row0.getCell(j);
			XSSFCell cell1 = row1.getCell(j);
			XSSFCell cell2 = row2.getCell(j);
			String type = cell1.getStringCellValue().trim();
			String name = cell2.getStringCellValue().trim();
			sb.append("\r\n\t\t").append("private var ").append("_").append(name.substring(2)).append(":");
			if(type.contains("varchar") || type.contains("text")){
				sb.append("String");
			}else if(type.contains("tinyint")){
				sb.append("int");
			}else if(type.contains("int")){
				sb.append("int");
			}else if(type.contains("float")){
				sb.append("Number");
			}else{
				throw new RuntimeException("未知类型:" + type);
			}
			sb.append(";");
		}
		for(int j = 0; j < row1.getLastCellNum(); j++){
			XSSFCell cell0 = row0.getCell(j);
			XSSFCell cell1 = row1.getCell(j);
			XSSFCell cell2 = row2.getCell(j);
			String type = cell1.getStringCellValue().trim();
			String name = cell2.getStringCellValue().trim();
			sb.append("\r\n\t\t/**");
			sb.append("\r\n\t\t * ").append(cell0.getStringCellValue().trim());
			sb.append("\r\n\t\t */");
			sb.append("\r\n\t\t").append("public function get ").append(name.substring(2)).append("():");
			if(type.contains("varchar") || type.contains("text")){
				sb.append("String");
			}else if(type.contains("tinyint")){
				sb.append("int");
			}else if(type.contains("int")){
				sb.append("int");
			}else if(type.contains("float")){
				sb.append("Number");
			}else{
				throw new RuntimeException("未知类型:" + type);
			}
			sb.append("\r\n\t\t{");
			sb.append("\r\n\t\t\treturn _").append(name.substring(2)).append(";");
			sb.append("\r\n\t\t}");
			
			sb.append("\r\n\t\t/**");
			sb.append("\r\n\t\t * ").append(cell0.getStringCellValue().trim());
			sb.append("\r\n\t\t */");
			sb.append("\r\n\t\t").append("public function set ").append(name.substring(2)).append("(").append(name.substring(2)).append(":");
			if(type.contains("varchar") || type.contains("text")){
				sb.append("String");
			}else if(type.contains("tinyint")){
				sb.append("int");
			}else if(type.contains("int")){
				sb.append("int");
			}else if(type.contains("float")){
				sb.append("Number");
			}else{
				throw new RuntimeException("未知类型:" + type);
			}
			sb.append("):void");
			sb.append("\r\n\t\t{");
			sb.append("\r\n\t\t\t_").append(name.substring(2)).append("=").append(name.substring(2)).append(";");
			sb.append("\r\n\t\t}");
		}
		sb.append("\r\n\t}");
		sb.append("\r\n}");
		return sb.toString();
	}
}
