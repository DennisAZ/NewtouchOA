package yh.subsys.oa.rollmanage.act;

import java.io.File;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.funcs.workflow.util.YHFlowUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.rollmanage.data.YHRmsFile;
import yh.subsys.oa.rollmanage.logic.YHRmsFileLogic;



public class YHRmsFileAct {
	private YHRmsFileLogic logic = new YHRmsFileLogic();

	/**
	 * 获取下拉列表值
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSelectOption(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String parentNo = request.getParameter("parentNo");
		String optionType = request.getParameter("optionType");
		if (YHUtility.isNullorEmpty(parentNo)) {
			parentNo = "";
		}
		if (YHUtility.isNullorEmpty(optionType)) {
			optionType = "";
		}
		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getSelectOption(dbConn, parentNo);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 新建文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addFileInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

			// 保存从文件柜、网络硬盘选择附件
			YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, "roll_manage");
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
			boolean docAttachmentFlag = false;
			String attachmentId = "";
			String attachmentName = "";
			String docAttachmentId = "";
			String docAttachmentName = "";
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(date);
			String separator = File.separator;
			String filePath = YHSysProps.getAttachPath() + separator + "roll_manage" + separator + currDate;

			while (iKeys.hasNext()) {
				String fieldName = iKeys.next();
				String fileName = fileForm.getFileName(fieldName);

				if (YHUtility.isNullorEmpty(fileName)) {
					continue;
				}
				YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
				String rand = emul.getRandom();

				if (!YHUtility.isNullorEmpty(fieldName) && "ATTACHMENT_DOC".equals(fieldName.trim())) {
					docAttachmentId += currDate + "_" + rand + ",";
					docAttachmentName += fileName + "*";
					docAttachmentFlag = true;
				} else {
					attachmentId += currDate + "_" + rand + ",";
					attachmentName += fileName + "*";
					uploadFlag = true;
				}

				fileName = rand + "_" + fileName;
				fileForm.saveFile(fieldName, filePath + File.separator + fileName);
			}

			Map<Object, Object> map = new HashMap<Object, Object>();
			
			map.put("fromFolderFlag", fromFolderFlag);
			map.put("uploadFlag", uploadFlag);
			map.put("docAttachmentFlag", docAttachmentFlag);
			
			map.put("newAttchId", newAttchId);
			map.put("attachmentId", attachmentId);
			map.put("docAttachmentId", docAttachmentId);
			
			map.put("newAttchName", newAttchName);
			map.put("attachmentName", attachmentName);			
			map.put("docAttachmentName", docAttachmentName);			

			this.logic.setRmsFileValue(dbConn, fileForm, person, map);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/subsys/oa/rollmanage/rollfile/newFileWarn.jsp";
	}
	
	/**
	 * add by zyy 张银友
	 * 从工作流中归档文件
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addFileFromFlow(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

			// 保存从文件柜、网络硬盘选择附件
			YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, "roll_manage");
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
			boolean docAttachmentFlag = false;
			String attachmentId = "";
			String attachmentName = "";
			String docAttachmentId = "";
			String docAttachmentName = "";
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(date);
			String separator = File.separator;
			String filePath = YHSysProps.getAttachPath() + separator + "roll_manage" + separator + currDate;

			while (iKeys.hasNext()) {
				String fieldName = iKeys.next();
				String fileName = fileForm.getFileName(fieldName);

				if (YHUtility.isNullorEmpty(fileName)) {
					continue;
				}
				YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
				String rand = emul.getRandom();

				if (!YHUtility.isNullorEmpty(fieldName) && "ATTACHMENT_DOC".equals(fieldName.trim())) {
					docAttachmentId += currDate + "_" + rand + ",";
					docAttachmentName += fileName + "*";
					docAttachmentFlag = true;
				} else {
					attachmentId += currDate + "_" + rand + ",";
					attachmentName += fileName + "*";
				}

				fileName = rand + "_" + fileName;
				fileForm.saveFile(fieldName, filePath + File.separator + fileName);
			}
			
			//处理从工作流转来的附件
			String attids=fileForm.getParameter("attachmentId");
			String attnames=fileForm.getParameter("attachmentName");
			YHFlowUtil util=new YHFlowUtil();
			if(attids==null || "".equals(attids)){
			}else{
				util.copyAttachFlowToWhere(attnames, attids, "roll_manage");
			}
			//将工作流表单
		
				
			Map<Object, Object> map = new HashMap<Object, Object>();
			
			map.put("fromFolderFlag", fromFolderFlag);
			map.put("uploadFlag", true);
			map.put("docAttachmentFlag", docAttachmentFlag);
			
			map.put("newAttchId", newAttchId);
			map.put("attachmentId", attachmentId+attids);
			map.put("docAttachmentId", docAttachmentId);
			
			map.put("newAttchName", newAttchName);
			map.put("attachmentName", attachmentName+attnames);			
			map.put("docAttachmentName", docAttachmentName);			

			this.logic.setRmsFileValue(dbConn, fileForm, person, map);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/subsys/oa/rollmanage/rollfile/newFileFromFlowWarn.jsp";
	}

	/**
	 * 取得文件列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getRmsFileJosn(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getRmsFileJosn(dbConn, request.getParameterMap(), person);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();

		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return null;
	}
	
	/**
	 * 档案查询中，根据查询条件取得文件列表
	 * add by jzk
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getRmsFilesJosn(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		String sendDate = request.getParameter("sendDate");
		String rollId = request.getParameter("rollId");
		String deadlineId = request.getParameter("deadlineId");
		if(sendDate != null && sendDate !=""){
			map.put("SEND_DATE", request.getParameter("sendDate"));
		}
		if(rollId != null && rollId != ""){
			map.put("ROLL_ID", request.getParameter("rollId"));
		}
		if(deadlineId != null && deadlineId != ""){
			map.put("DEADLINE", request.getParameter("deadlineId"));
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.queryRmsFiles(dbConn, request.getParameterMap(), person,map);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();

		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return null;
	}

	/**
	 * 档案查询中，根据查询条件取得文件列表
	 * add by jzk
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	 public String getdeadline(HttpServletRequest request,
		      HttpServletResponse response) throws Exception{
		    Connection dbConn = null;
		    try{
		      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      StringBuffer sb = new StringBuffer("[");
		      String sql = "SELECT SEQ_ID,CLASS_DESC FROM oa_kind_dict_item	WHERE CLASS_NO='DEADLINE'";
		      PreparedStatement ps = null;
		      ResultSet rs = null;
		      try {
		        ps = dbConn.prepareStatement(sql);
		        rs = ps.executeQuery() ;
		        while (rs.next()) {
		          int seqId = rs.getInt("SEQ_ID");
		          String class_desc =  YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("CLASS_DESC")));
		          sb.append("{");
		          sb.append("seqId:\"" + seqId + "\"");
		          sb.append(",class_desc:\"" + class_desc + "\"");
		          sb.append("},");
		        }
		      } catch (Exception e) {
		        throw e;
		      } finally {
		        YHDBUtility.close(ps, rs, null);
		      }
		      if(sb.length() > 1){
		        sb.deleteCharAt(sb.length() - 1); 
		      }
		      sb.append("]");
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
		      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		    }catch(Exception ex) {
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		      throw ex;
		    }
		    return "/core/inc/rtjson.jsp";
		  } 
	
	/**
	 * 根据seqId取出RmsFile信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getRmsFileDetailById(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String seqIdStr = request.getParameter("seqId");

		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			// YHRmsFileLogic logic = new YHRmsFileLogic();

			YHRmsFile rmsFile = this.logic.getRmsFileDetailById(dbConn, seqId);

			if (rmsFile == null) {
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				request.setAttribute(YHActionKeys.RET_MSRG, "文件不存在");
				return "/core/inc/rtjson.jsp";
			}

			StringBuffer data = YHFOM.toJson(rmsFile);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
			request.setAttribute(YHActionKeys.RET_DATA, data.toString());

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}
	/**
   * 获取案卷下拉列表值

   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRmsRollSelectOption2(HttpServletRequest request, HttpServletResponse response) throws Exception {

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getRmsRollSelectOption2(dbConn , loginUser.getDeptId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
	/**
	 * 获取案卷下拉列表值
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getRmsRollSelectOption(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			String data = this.logic.getRmsRollSelectOption(dbConn);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
			request.setAttribute(YHActionKeys.RET_DATA, data);

		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 修改文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateRmsFileById(HttpServletRequest request, HttpServletResponse response) throws Exception {

		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String seqIdStr = (String) fileForm.getParameter("seqId");
		String fileCode = (String) fileForm.getParameter("fileCode");
		String fileSubject = (String) fileForm.getParameter("fileSubject");
		String fileTitle = (String) fileForm.getParameter("fileTitle");

		String fileTitleo = (String) fileForm.getParameter("fileTitleo");
		String sendUnit = (String) fileForm.getParameter("sendUnit");
		String sendDate = (String) fileForm.getParameter("sendDate");
		String secret = (String) fileForm.getParameter("secret");
		String urgency = (String) fileForm.getParameter("urgency");
		String fileType = (String) fileForm.getParameter("fileType");
		String fileKind = (String) fileForm.getParameter("fileKind");
		String filePage = (String) fileForm.getParameter("filePage");
		String printPage = (String) fileForm.getParameter("printPage");
		String remark = (String) fileForm.getParameter("remark");
		String rollIdStr = (String) fileForm.getParameter("rollId");
		String downloadYnStr = (String) fileForm.getParameter("downloadYn");

		String fileYear = (String) fileForm.getParameter("fileYear");
        String fileWord = (String) fileForm.getParameter("fileWord");
        String issueNum = (String) fileForm.getParameter("issueNum");
        String deadline =(String) fileForm.getParameter("deadline");
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}

		int rollId = 0;
		int downloadYn = 0;
		if (!YHUtility.isNullorEmpty(rollIdStr)) {
			rollId = Integer.parseInt(rollIdStr);
		}
		if (!YHUtility.isNullorEmpty(downloadYnStr)) {
			downloadYn = Integer.parseInt(downloadYnStr);
		}

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHRmsFile rmsFile = this.logic.getRmsFileDetailById(dbConn, seqId);
			String dbAttchId = YHUtility.null2Empty(rmsFile.getAttachmentId());
			String dbAttchName = YHUtility.null2Empty(rmsFile.getAttachmentName());

			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

			// 保存从文件柜、网络硬盘选择附件
			YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, "roll_manage");
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
			String filePath = YHSysProps.getAttachPath() + separator + "roll_manage" + separator + currDate;

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

				fileName = rand + "_" + fileName;
				fileForm.saveFile(fieldName, filePath + File.separator +fileName);

				uploadFlag = true;
			}

			if (fromFolderFlag && uploadFlag) {

				rmsFile.setAttachmentId(dbAttchId + newAttchId + attachmentId);
				rmsFile.setAttachmentName(dbAttchName + newAttchName + attachmentName);

			} else if (fromFolderFlag) {
				rmsFile.setAttachmentId(dbAttchId + newAttchId);
				rmsFile.setAttachmentName(dbAttchName + newAttchName);
			} else if (uploadFlag) {
				rmsFile.setAttachmentId(dbAttchId + attachmentId);
				rmsFile.setAttachmentName(dbAttchName + attachmentName);

			}

			rmsFile.setModUser(String.valueOf(person.getSeqId()));
			rmsFile.setModTime(new Date());
			rmsFile.setFileCode(fileCode);
			rmsFile.setFileTitle(fileTitle);
			rmsFile.setFileTitleo(fileTitleo);
			rmsFile.setFileSubject(fileSubject);
			rmsFile.setSendUnit(sendUnit);
			rmsFile.setSendDate(YHUtility.parseDate(sendDate));
			rmsFile.setSecret(secret);
			rmsFile.setUrgency(urgency);
			rmsFile.setFileKind(fileKind);
			rmsFile.setFileType(fileType);
			rmsFile.setFilePage(filePage);
			rmsFile.setPrintPage(printPage);
			rmsFile.setRemark(remark);
			rmsFile.setDownloadYn(downloadYn);
			rmsFile.setRollId(rollId);
			rmsFile.setDeadline(Integer.parseInt(deadline));
			rmsFile.setFileWord(fileWord);
			rmsFile.setFileYear(fileYear);
			rmsFile.setIssueNum(issueNum);
			this.logic.updateRmsFileByObj(dbConn, rmsFile);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		String contextPath = request.getContextPath();
		response.sendRedirect(contextPath + "/subsys/oa/rollmanage/rollfile/fileManage.jsp");

		return null;
	}

	/**
	 * 文件销毁人
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String destroySingleFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (YHUtility.isNullorEmpty(seqIdStr)) {
			// seqId=Integer.parseInt(seqIdStr);
			seqIdStr = "0";
		}

		YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			this.logic.updateRmsFileById(dbConn, String.valueOf(person.getSeqId()), seqIdStr);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 浮动菜单文件删除
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delFloatFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String seqIdStr = request.getParameter("seqId");
		String attachId = request.getParameter("attachId");
		String attachName = request.getParameter("attachName");

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			boolean updateFlag = this.logic.updateFloadFile(dbConn, seqIdStr, YHUtility.null2Empty(attachId), YHUtility.null2Empty(attachName));

			int returnFlag = 0;
			if (updateFlag) {
				returnFlag = 1;
			}

			String data = "{updateFlag:\"" + returnFlag + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, data);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;

		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 查询文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryRmsFileJosn(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("fileCode", request.getParameter("fileCode"));
		map.put("fileSubject", request.getParameter("fileSubject"));
		map.put("fileTitle", request.getParameter("fileTitle"));
		map.put("fileTitleo", request.getParameter("fileTitleo"));
		map.put("sendUnit", request.getParameter("sendUnit"));

		map.put("sendTimeMin", request.getParameter("sendTimeMin"));
		map.put("sendTimeMax", request.getParameter("sendTimeMax"));
		map.put("secret", request.getParameter("secret"));
		map.put("urgency", request.getParameter("urgency"));
		map.put("fileType", request.getParameter("fileType"));
		map.put("fileKind", request.getParameter("fileKind"));

		map.put("filePage1", request.getParameter("filePage1"));
		map.put("filePage2", request.getParameter("filePage2"));
		map.put("printPage1", request.getParameter("printPage1"));
		map.put("printPage2", request.getParameter("printPage2"));
		map.put("remark", request.getParameter("remark"));
		map.put("handlerTime", request.getParameter("handlerTime"));
		map.put("fileWord", request.getParameter("fileWord"));
    map.put("fileYear", request.getParameter("fileYear"));
    map.put("issueNum", request.getParameter("issueNum"));
		Connection dbConn;
		try {

			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

			String data = this.logic.queryRmsFileLogic(dbConn, request.getParameterMap(), person, map);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return null;
	}

	/**
	 * 组卷至
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String changeRoll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqIdStr");
		String rollIdStr = request.getParameter("rollId");
		if (YHUtility.isNullorEmpty(seqIdStr)) {
			seqIdStr = "0";
		}
		int rollId = 0;
		if (!YHUtility.isNullorEmpty(rollIdStr)) {
			rollId = Integer.parseInt(rollIdStr);
		}

		Connection dbConn;
		try {

			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			this.logic.changeRollLogic(dbConn, seqIdStr, rollId);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 取得已销毁文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getDestroyRmsFileJosn(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

			String data = this.logic.getDestroyFileLogic(dbConn, request.getParameterMap(), person);

			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return null;
	}

	/**
	 * 还原文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String recoverFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		if (YHUtility.isNullorEmpty(seqIdStr)) {
			seqIdStr = "0";
		}

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			this.logic.updateDestroyFileById(dbConn, seqIdStr);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 删除文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String deleteFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			String filePath = YHSysProps.getAttachPath() + File.separator + "roll_manage" + File.separator;

			this.logic.deleteFileLogic(dbConn, seqIdStr, filePath);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 导出到csv
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String exportFileToCsv(HttpServletRequest request, HttpServletResponse response) throws Exception {
	  response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
	  Connection conn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			conn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
			String seqIdStr = request.getParameter("seqIdStr");
			String fileName = URLEncoder.encode("文件档案.csv", "UTF-8");
			fileName = fileName.replaceAll("\\+", "%20");
			response.setHeader("Cache-control", "private");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
			ArrayList<YHDbRecord> dbL = this.logic.toExportRmsFileData(conn, seqIdStr);
			YHCSVUtil.CVSWrite(response.getWriter(), dbL);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return null;
	}
	/**
	 * 删除文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getDeadlineID(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int seqIdStr = Integer.parseInt(request.getParameter("seqId"));
        String sb="";
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
            String sql="select CLASS_DESC from oa_kind_dict_item where SEQ_ID="+seqIdStr+"";
            Statement stm1 = dbConn.createStatement();
            ResultSet rs = stm1.executeQuery(sql);
            String deadline_desc=null;
            while (rs.next()){
            	 deadline_desc=rs.getString("CLASS_DESC");
            }
             sb="{\"deadline_desc\":\""+deadline_desc+"\"}";
            
			request.setAttribute(YHActionKeys.RET_DATA,sb);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
}
