package com.ray.tool;

import java.io.File;
import java.util.Properties;

import com.ray.tool.util.FileUtil;
import com.ray.tool.util.PropertiesUtil;

public class ErrorCodeTool {
	
	public static void parse(String codeFile, String destFile) throws Exception{
		File file = new File(codeFile);
		String content = FileUtil.getConetnt(file);
		String[] lines = content.split("\n");
		StringBuffer sb = new StringBuffer();
		for(String line : lines){
			String pairs[] = line.split("//");
			if(pairs.length == 2){
				String p[] = pairs[0].split("=");
				p[1] = p[1].trim();
				p[1] = p[1].substring(0, p[1].length() - 1);
				sb.append(p[1]).append("=").append(pairs[1].trim()).append("\r\n");
			}
		}
		File dest = new File(destFile);
		FileUtil.write(dest, sb.toString());
	}
	
	public static void main(String args[]) throws Exception{
//		Properties properties = PropertiesUtil.read("file/error_file.properties");
//		String srcFile = properties.getProperty("src_file");
//		String destFile = properties.getProperty("dest_file");
//		parse(srcFile, destFile);
		/**生成错误码*/
		Properties properties = PropertiesUtil.read("file/error_file.properties");
		String srcFile = properties.getProperty("src_file");
		String asDestFile = properties.getProperty("as_dest_file");
		System.out.println("生成:" + asDestFile);
		ErrorCodeTool.parse(srcFile, asDestFile);
		String javaDestFile = properties.getProperty("java_dest_file");
		String files[] = javaDestFile.split(SystemConstants.SPLIT_VERTICALLINE);
		for(String file : files){
			System.out.println("生成:" + file);
			ErrorCodeTool.parse(srcFile, file);
		}
	}
}
