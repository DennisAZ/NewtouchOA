<%@ page language="java" import="yh.subsys.oa.vmeet.act.*,yh.core.global.YHSysProps" contentType="text/html;charset=utf-8"%>
<%@ include file="inc.jsp"%>
<% /*文件作用: 处理删除"PPT演示"功能中的PPT文件*/ %> 
<%
String uploadDir = YHSysProps.getAttachPath()+"\\zlchat\\ppt\\";
	if (!uploadDir.endsWith(File.separator))
		uploadDir += File.separatorChar;
	
	//zlchat 客户端传过来的文件夹名

	String folder = request.getParameter("folder");
	
	//删除文件及数据库中的记录
	if (StringHelper.isNotEmpty(folder))
	{
		File delFolder = new File(uploadDir + folder);
		File[] fileList = delFolder.listFiles();
		if (fileList != null)
		{
			for (int i = 0; i < fileList.length; i++)
			{
				File file = fileList[i];
				if (file.exists())
				{
					file.delete();
				}
			}
		}
		if (delFolder.exists())
		{
			delFolder.delete();
		}
		File pptFile = new File(uploadDir + folder + ".ppt");
		if (pptFile.exists())
		{
			pptFile.delete();
		}
		
		String sql="DELETE FROM oa_zl_ppt WHERE folder='"+folder+"'";

		int affectedRows=JdbcUtils.update(sql,conn);		
		JdbcUtils.closeConnection(conn);
	}
%>