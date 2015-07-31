package com.ray.tool;

import java.io.File;
import java.util.Properties;

import com.ray.tool.util.FileUtil;
import com.ray.tool.util.PropertiesUtil;

public class CmdTool {

	public static void parse(String cmdFile, String destFile) throws Exception{
		File file = new File(cmdFile);
		String content = FileUtil.getConetnt(file);
		String[] lines = content.split("\n");
		StringBuffer sb = new StringBuffer();
		
		for(String line : lines){
			String pairs[] = line.split("//");
			if(pairs.length == 2){
				String p[] = pairs[0].split("=");
				p[1] = p[1].trim();
				p[1] = p[1].substring(0, p[1].length() - 1);
				String p1[] = pairs[1].split("\\|");
				p1[0] = p1[0].trim();
				sb.append(p[1]).append("=").append(p1[0]).append("\r\n");
			}
		}
		if(sb.length() > 0){
			sb.setLength(sb.length() - 1);
		}
		//sb.insert(0, "var CMD={");
		//sb.append("\r\n};");
		File dest = new File(destFile);
		FileUtil.write(dest, sb.toString());
	}
	
	public static void main(String args[]) throws Exception{
		Properties properties = PropertiesUtil.read("file/client_file.properties");
		String srcFile = properties.getProperty("src_file");
		String destFile = properties.getProperty("dest_file");
		parse(srcFile, destFile);
	}
}
