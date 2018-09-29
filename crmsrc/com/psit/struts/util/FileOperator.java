package com.psit.struts.util;
import java.io.File;
import java.io.IOException;  

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.global.YHSysProps;

public class FileOperator {
	/**
	 * 文件夹新建 <br>
	 * @param folderPath 文件目录
	 * @param HttpServletRequest,如绝对路径此处为null
	 */
	public static void createFolder(String folderName,HttpServletRequest request) {
		String folderPath = folderName.replace("/", "\\");
		if(request!=null){
			folderPath = request.getSession().getServletContext().getRealPath("/") + folderPath;
		}
		File myFilePath = new File(folderPath);
		try {
			if (!myFilePath.isDirectory()) {
				myFilePath.mkdir();
			}
		} catch (Exception e) {
			System.out.println("新建目录["+folderName+"]出错");
			e.printStackTrace();
		}
	}
	/**
	 * 文件夹新建 <br>
	 * @param folderPath 文件目录
	 * @param HttpServletRequest,如绝对路径此处为null
	 */
	public static void createFolder2(String folderName,HttpServletRequest request) {
		String folderPath = folderName.replace("/", "\\");
		
		File myFilePath = new File(folderPath);
		try {
			if (!myFilePath.exists()&&!myFilePath.isDirectory()) {
				myFilePath.mkdir();
			}
		} catch (Exception e) {
			System.out.println("新建目录["+folderName+"]出错");
			e.printStackTrace();
		}
	}
	/**
	 * 物理删除文件 <br>
	 * @param relPath 文件路径
	 * @param request HttpServletRequest,如绝对路径此处为null
	 */
	public static void delFile(String filePath, HttpServletRequest request) {
		String path = filePath.replace("/", "\\");
		if(request!=null){
			path = request.getSession().getServletContext().getRealPath("/") + path;
		}
		File file = new File(path);
		deleteAllFiles(file);
	}
	/**
	 * 物理删除文件 <br>
	 * @param relPath 文件路径
	 * @param request HttpServletRequest,如绝对路径此处为null
	 */
	public static void delFile2(String filePath, HttpServletRequest request) {
		String path = filePath.replace("/", "\\");

		File file = new File(path);
		deleteAllFiles(file);
	}
	/**
	 * 删除文件或文件目录及其子目录下所有文件 <br>
	 * @param file 
	 */
	public static void deleteAllFiles(File file){
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists()) {
			if(file.isFile()){
				if (!file.delete()) {
					System.out.println("删除文件" + file.getAbsolutePath() + "失败！");
				}
			}
			else if(file.isDirectory()){
				File[] files = file.listFiles();
				for(File f:files){
					deleteAllFiles(f);
				}
				if (!file.delete()) {
					System.out.println("删除文件夹" + file.getAbsolutePath() + "失败！");
				}
			}
		} else {
			System.out.println("删除文件失败：" + file.getAbsolutePath() + "不存在！");
		}
	}
		
	/**
	 * 用户下载文件 <br>
	 * @param request HttpServletRequest,如绝对路径此处为null
	 * @param response HttpServletResponse
	 * @param fileUrl 文件路径
	 * @param fileName 文件名<br>
	 */
	public static void download(HttpServletRequest request,
			HttpServletResponse response, String fileUrl, String fileName) {
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;
		if(request!=null){
			fileUrl = request.getSession().getServletContext().getRealPath("/") + fileUrl;
		}
		try {
			// 使用保存文件的对话框：   
			fileName = fileName.replaceAll("\\+", "%20");
			fileUrl = new String(fileUrl.getBytes("utf-8"), "utf-8");
			response.setContentType("application/x-msdownload");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName);
			// 通知客户端文件的MIME类型：   
			bis = new java.io.BufferedInputStream(new java.io.FileInputStream(
					(fileUrl)));
			bos = new java.io.BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			int i = 0;

			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
				i++;
			}
			bos.flush();
//			bis.close();
//			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bis = null;
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bos = null;
			}
		}
	}
	/*public static void main(String[] args) throws IOException {
		File file = new File("D:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\webapps\\jcMis\\importFiles\\zipTemp\\1309318477312\\");
		deleteAllFiles(file);
	}*/
}