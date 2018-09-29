<%@ page language="java" import="yh.subsys.oa.vmeet.act.*,yh.core.global.YHSysProps" contentType="text/html;charset=utf-8"%>
<%@ include file="inc.jsp"%>
<% /*文件作用: 处理"白板"功能中的图片文件上传*/ %>
<%
  request.setCharacterEncoding("utf-8");
String uploadDir =application.getRealPath("/subsys/oa/vmeet/zlchat/wbUpload/");

	String fileName = request.getParameter("fileName");
	HashMap uploadFileInfo = UploadUtil.uploadFile(request, uploadDir,fileName);
	if (uploadFileInfo == null)
	{
		out.print("upload failed");
	}
%>