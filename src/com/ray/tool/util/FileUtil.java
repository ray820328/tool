package com.ray.tool.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileUtil {
	public static File createFile(String filename, boolean onlyDirectory) throws Exception{
		Log.debug("filename= " + filename);
		String dir = filename.endsWith(File.separator) ? filename : 
			filename.substring(0, filename.replace("\\", "/").lastIndexOf("/"));
		File file = new File(dir);
		if(file.exists() && file.isFile()){
			file.delete();
		}
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(filename);
		if(file.exists() && file.isDirectory()){
			file.delete();
		}
		if(!file.exists() && !onlyDirectory){
			file.createNewFile();
		}
		return file;
	}
	public static void write(File file, String content) throws Exception{
		write(file, content, "UTF-8");
	}
	public static void write(File file, String content, String encoding) throws Exception{
		FileOutputStream output = new FileOutputStream(file);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, encoding));
		try{
			writer.write(content, 0, content.length());
			writer.flush();
		}finally{
			writer.close();
			output.close();
		}
	}
	public static String getConetnt(File file) throws Exception{
		return getConetnt(file, "UTF-8");
	}
	public static String getConetnt(File file, String encoding) throws Exception{
		FileInputStream input = new FileInputStream(file);
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(input, encoding));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while((line = reader.readLine()) != null){
				sb.append(line).append("\r\n");
			}
			return sb.toString();
		}finally{
			input.close();
		}
	}
	public static void remove(String filename) throws Exception{
		String dir = filename.endsWith(File.separator) ? filename : 
			filename.substring(0, filename.replace("\\", "/").lastIndexOf("/"));
		File file = new File(dir);
		if (!file.exists()) {
			return;
		}
		file = new File(filename);
		if(file.exists()){
			file.delete();
		}
	}
	
	public static void main(String[] args){
		try{
			write(createFile("d:/test111.bat", true), "testset\r\n7878");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
