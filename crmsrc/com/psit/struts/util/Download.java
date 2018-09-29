package com.psit.struts.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;

import yh.core.global.YHSysProps;

import com.psit.struts.BIZ.AttBIZ;
import com.psit.struts.util.format.TransStr;

/**
 * 
 * 附件下载 <br>
 *
 * create_date: Aug 20, 2010,2:16:27 PM<br>
 * @author zjr
 */
public class Download extends DownloadAction {
	AttBIZ attBiz = null;
	/**
	 * 
	 * 下载附件<br>
	 * create_date: Aug 20, 2010,2:20:12 PM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * 			parameter > path:值为附件的路径
	 * 			parameter > fileId:值为附件的id	
	 * @param response
	 * @return 返回文件流<br>
	 */
	public StreamInfo getStreamInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String path=request.getParameter("path");
		path=YHSysProps.getAttachPath()+ "/crm/"+path;
		String fileId=request.getParameter("fileId");
		String fileName=request.getParameter("fileName");
		if(fileName!=null && !fileName.equals("")){
			fileName = TransStr.transStr(fileName);
		}
		else{
			fileName = attBiz.findById(Long.parseLong(fileId)).getAttName();
		}
		try {
			response.setHeader("Content-disposition","attachment;  filename="+new String(fileName.getBytes(),"iso8859-1"));//设置文件名称
			StreamInfo si = new FileStreamInfo("application/octet-stream",new File(path));
			return si;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public void setAttBiz(AttBIZ attBiz) {
		this.attBiz = attBiz;
	}  
}