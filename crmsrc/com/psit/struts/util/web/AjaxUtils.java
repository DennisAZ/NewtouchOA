package com.psit.struts.util.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class AjaxUtils {
	public static void outPrintJSON(HttpServletResponse response, Object obj){
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(obj);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	public static void outPrintText(HttpServletResponse response, String text){
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(text);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}
}
