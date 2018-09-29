<%@ page language="java" import="yh.subsys.oa.vmeet.act.*,yh.core.global.YHSysProps" contentType="text/html;charset=utf-8"%>
<%@ include file="inc.jsp"%>
<% /*文件作用: 处理"PPT演示"中的文件PPT上传并转换为zlchat可识别的文件格式 */ %>
<%
	request.setCharacterEncoding("utf-8");
	response.setDateHeader("Expires", 0); // date in the past
	response.addHeader("Cache-Control",
			"no-store, no-cache, must-revalidate"); // HTTP/1.1
	response.addHeader("Cache-Control", "post-check=0, pre-check=0");
	response.addHeader("Pragma", "no-cache"); // HTTP/1.0

	//zlchat 客户端传过来的会议ID
	String roomID = request.getParameter("roomID");
	
	String uploadDir =application.getRealPath("/subsys/oa/vmeet/zlchat/pptUpload/");;
	if (!uploadDir.endsWith(File.separator))
		uploadDir += File.separatorChar;
	HashMap uploadFileInfo = UploadUtil.uploadFile(request, uploadDir,
			null);
	String localFileName = (String) uploadFileInfo
			.get(UploadUtil.FILEINFO_NEWNAME);

	int idx = localFileName.lastIndexOf(".");
	
	String fileNameNoExt = idx > 0 ? localFileName.substring(0, idx) : "";
	String ext=idx > 0 ? localFileName.substring(idx+1) : "";
	
  int totalFrame = PPTUtil.convert(uploadDir + localFileName,
      uploadDir + fileNameNoExt,fileNameNoExt,application.getRealPath("/subsys/oa/vmeet/zlchat/SWFTools/"));
  
  
	
	
	if (totalFrame > 0) // 转换成功
	{
	
		final String MEDIUM_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		java.util.Date date = (java.util.Date) uploadFileInfo
				.get(UploadUtil.FILEINFO_CREATEDTIME);
		DateFormat df = new SimpleDateFormat(MEDIUM_DATE_FORMAT);
		String sql = "INSERT INTO oa_zl_ppt(name,folder,total_Frame,create_date,roomID) VALUES ('"
				+ (String) uploadFileInfo
						.get(UploadUtil.FILEINFO_OLDNAME)
				+ "','"
				+ fileNameNoExt
				+ "','"
				+ totalFrame
				+ "','"
				+ (df.format(date)) + "','" + roomID + "')";
		int affectedRows = JdbcUtils.update(sql, conn);
		JdbcUtils.closeConnection(conn);
		
		//更改名字
		String folder = uploadDir + fileNameNoExt;
		File[] fileList = (new File(folder)).listFiles();
		if (fileList != null)
		{
			for (int i = 0; i < fileList.length; i++)
			{
				String fn = fileList[i].getName();
				String newFileName = PPTUtil.rename(fn);
				File newFile = new File(folder + "/" + newFileName);
				fileList[i].renameTo(newFile);				
			}
		}
	}
%>