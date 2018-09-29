<%@ page language="java" import="yh.subsys.oa.vmeet.act.*,java.text.*,yh.core.global.YHSysProps"  contentType="text/html;charset=utf-8"%>
<%@ include file="inc.jsp" %> 
<% /*文件作用: 处理"共享文档"中的文件上传*/ %> 
<%	
	
	request.setCharacterEncoding("utf-8");
String uploadDir = application.getRealPath("/subsys/oa/vmeet/zlchat/upload/");
	HashMap fileInfo = UploadUtil.uploadFile(request, uploadDir, null);
 
	final String MEDIUM_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	java.util.Date date = new java.util.Date();// fileInfo.get(UploadUtil.FILEINFO_CREATEDTIME);

	DateFormat df = new SimpleDateFormat(MEDIUM_DATE_FORMAT);

	//zlchat 客户端传过来的会议ID
	String roomID = (request.getParameter("roomID"));
try{
	String sql = "INSERT INTO oa_zl_file(name,file_name,fsize,roomid,create_date) values('"
			+ (String) fileInfo.get(UploadUtil.FILEINFO_OLDNAME)
			+ "','"
			+ (String) fileInfo.get(UploadUtil.FILEINFO_NEWNAME)
			+ "','"
			+ (Integer) fileInfo.get(UploadUtil.FILEINFO_SIZE)
			+ "','" + roomID + "','" + (df.format(date)) + "')";
	
  Statement stmt=conn.createStatement();
  int d= stmt.executeUpdate(sql);
 
  conn.commit();
  stmt.close();
  conn.close();
}catch(SQLException e){
  System.out.println(e.toString());
}

%>