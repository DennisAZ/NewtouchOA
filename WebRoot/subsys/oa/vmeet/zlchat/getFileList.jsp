<%@ page language="java" import="yh.subsys.oa.vmeet.act.*,yh.core.global.YHSysProps"  contentType="text/html;charset=utf-8"%>
<%@ include file="inc.jsp" %> 

<% /*文件作用: 处理"共享文档"中的文件列表*/ %> 
<%	
	
	response.setDateHeader("Expires", 0); // date in the past
	response.addHeader("Cache-Control",
			"no-store, no-cache, must-revalidate"); // HTTP/1.1
	response.addHeader("Cache-Control", "post-check=0, pre-check=0");
	response.addHeader("Pragma", "no-cache"); // HTTP/1.0	
	
	//zlchat 客户端传过来的会议ID
	String roomID = (request.getParameter("roomID"));
	 
	//创建XML格式的返回数据,交给zlchat 客户端处理

	StringBuffer fileListXml = new StringBuffer("<FileList>");
	
	String sql = "select * from oa_zl_file where roomID='" + roomID + "'";
		
	ResultSet rs = JdbcUtils.query(sql, conn);
	
	while (rs != null && rs.next())
	{
		fileListXml.append("<File id=\"").append(rs.getInt("SEQ_ID"));
		fileListXml.append("\" name=\"").append(rs.getString("name"));
		fileListXml.append("\" fileName=\"").append(
				rs.getString("file_name"));
		fileListXml.append("\" size=\"").append(
				rs.getInt("fsize") / 1024).append("\" ");
		fileListXml.append("date=\"").append(
				rs.getString("create_date"));
		fileListXml.append("\"></File>");
	}
	fileListXml.append("</FileList>");
	out.print(fileListXml.toString());
	
	JdbcUtils.closeResultSet(rs);
	JdbcUtils.closeConnection(conn);
%>