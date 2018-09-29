<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="oa.core.funcs.bbs.act.BbsService,oa.core.funcs.bbs.act.BbsComment,oa.core.funcs.bbs.act.BBSUtil" %>

<%@page import="java.io.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%
String rpath=BBSUtil.bbsPath;
 response.setContentType("image/JPEG");
 OutputStream outt = response.getOutputStream();
 String aid=request.getParameter("attachId");
 String []aids=BBSUtil.split(aid,"_");
 String ain=request.getParameter("attachName");
 File file = new File(rpath +aids[0]+"/"+ aids[1]+"."+ain);
 FileInputStream fis = new FileInputStream(file);
 byte[] b = new byte[1024];
 int len = -1;
 while ((len = fis.read(b, 0, 1024)) != -1) {
  outt.write(b, 0, len);
 }
 outt.flush();
 outt.close();

 out.clear();
 out = pageContext.pushBody();
%>
