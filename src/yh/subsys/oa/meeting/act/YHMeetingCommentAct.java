package yh.subsys.oa.meeting.act;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.meeting.logic.YHMeetingCommentLogic;

public class YHMeetingCommentAct {
	private YHMeetingCommentLogic logic = new YHMeetingCommentLogic();

	/**
	 * 新建会议纪要评论请信息--wyw
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addMeetingCommentInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		
		String meetingIdStr = fileForm.getParameter("meetingId");
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(meetingIdStr)) {
			seqId = Integer.parseInt(meetingIdStr);
		}
		
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			// 保存从文件柜、网络硬盘选择附件
			YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, YHMeetingAct.attachmentFolder);
			String attIdStr = sel.getAttachIdToString(",");
			String attNameStr = sel.getAttachNameToString("*");

			boolean fromFolderFlag = false;
			String newAttchId = "";
			String newAttchName = "";
			if (!"".equals(attIdStr) && !"".equals(attNameStr)) {
				newAttchId = attIdStr + ",";
				newAttchName = attNameStr + "*";
				fromFolderFlag = true;
			}
			Iterator<String> iKeys = fileForm.iterateFileFields();
			boolean uploadFlag = false;
			String attachmentId = "";
			String attachmentName = "";
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(date);
			String separator = File.separator;
			String filePath = YHSysProps.getAttachPath() + separator + YHMeetingAct.attachmentFolder + separator + currDate;

			while (iKeys.hasNext()) {
				String fieldName = iKeys.next();
				String fileName = fileForm.getFileName(fieldName);
				if (YHUtility.isNullorEmpty(fileName)) {
					continue;
				}
				YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
				String rand = emul.getRandom();
				attachmentId += currDate + "_" + rand + ",";
				attachmentName += fileName + "*";
				uploadFlag = true;

				fileName = rand + "_" + fileName;
				fileForm.saveFile(fieldName, filePath +File.separator +fileName);
			}

			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("fromFolderFlag", fromFolderFlag);
			map.put("newAttchId", newAttchId);
			map.put("newAttchName", newAttchName);
			map.put("uploadFlag", uploadFlag);
			map.put("attachmentId", attachmentId);
			map.put("attachmentName", attachmentName);
			this.logic.setCommentValueLogic(dbConn, fileForm, person, map);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		String contexPath = request.getContextPath();
		response.sendRedirect(contexPath + "/subsys/oa/meeting/apply/review.jsp?seqId=" + seqId);
		return null;
	}
	
	
	/**
	 * 获取最新评论--wyw
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getNewCommentInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String meetingIdStr = request.getParameter("seqId");
		int meetingId = 0;
		if(!YHUtility.isNullorEmpty(meetingIdStr)){
			meetingId = Integer.parseInt(meetingIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			 YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data =  this.logic.getCommentInfoLogic(dbConn,person,meetingId);
		
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	
	/**
	 * 删除会议信息--wyw
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delCommentInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String seqIdStr = request.getParameter("commentId");
		int seqId=0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId=Integer.parseInt(seqIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String filePath = YHSysProps.getAttachPath() + File.separator + YHMeetingAct.attachmentFolder + File.separator;
			this.logic.delCommentLogic(dbConn, seqId,filePath);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功删除数据");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	
	
	
	
	

}
