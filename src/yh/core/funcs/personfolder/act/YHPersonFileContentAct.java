package yh.core.funcs.personfolder.act;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.filefolder.logic.YHFileContentLogic;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.personfolder.data.YHFileContent;
import yh.core.funcs.personfolder.data.YHPage;
import yh.core.funcs.personfolder.logic.YHPersonFileContentLogic;
import yh.core.funcs.personfolder.logic.YHPersonFolderLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;

public class YHPersonFileContentAct {

	/**
	 * 获得文件夹下的所有内容信息
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getFileContentInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqId = request.getParameter("seqId");
		String shareFlag = request.getParameter("shareFlag");

		String pageNoStr = request.getParameter("pageNo");
		String pageSizeStr = request.getParameter("pageSize");

		String orderBy = request.getParameter("field");
		String ascDesc = request.getParameter("ascDescFlag");

		if (orderBy == null) {
			orderBy = "";
		}
		if (ascDesc == null) {
			ascDesc = "";
		}

		int sortId = 0;
		if (seqId != null && !"".equals(seqId)) {
			sortId = Integer.parseInt(seqId);
		}

		int currNo = 1;
		if (YHUtility.isNullorEmpty(pageNoStr)) {
			currNo = 1;
		} else {
			currNo = Integer.parseInt(pageNoStr);
		}

		if (shareFlag == null) {
			shareFlag = "";
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		// Map map = new HashMap();
		// map.put("SORT_ID", sortId);

		// if (sortId == 0) {
		// map.put("USER_ID", String.valueOf(loginUserSeqId));
		// }

		// if (!"share".equals(shareFlag.trim())) {
		// map.put("USER_ID", String.valueOf(loginUserSeqId));
		//
		// }

		String condition = "SORT_ID=" + sortId;

		if (sortId == 0) {
			// map.put("USER_ID", String.valueOf(loginUserSeqId));
			condition += " and USER_ID='" + String.valueOf(loginUserSeqId) + "'";
		}

		if ("NAME".equals(orderBy.trim())) {

			if ("1".equals(ascDesc.trim())) {
				condition += " order by SUBJECT desc ";
			} else {
				condition += " order by SUBJECT asc ";
			}

		} else if ("SENDTIME".equals(orderBy.trim())) {

			if ("1".equals(ascDesc.trim())) {
				condition += " order by SEND_TIME desc ";
			} else {
				condition += " order by SEND_TIME asc ";
			}

		} else if ("CONTENTNO".equals(orderBy.trim())) {

			if ("1".equals(ascDesc.trim())) {
				condition += " order by CONTENT_NO desc ";
			} else {
				condition += " order by CONTENT_NO asc ";
			}

		}

		String[] filters = { condition };

		Connection dbConn = null;
		YHPersonFileContentLogic contentLogic = new YHPersonFileContentLogic();
		StringBuffer sb = new StringBuffer();
		List<YHFileContent> fileContents = new ArrayList<YHFileContent>();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			// fileContents = contentLogic.getFileContentsInfo(dbConn, map);
			fileContents = contentLogic.getFileContentsByFilters(dbConn, filters);

			long count = fileContents.size();
			int pageSize = 20;// 一个页面显示的数目
			if (pageSizeStr != null) {
				pageSize = Integer.parseInt(pageSizeStr);
			}

			YHPage page = new YHPage(pageSize, count, (currNo + 1));
			long first = page.getFirstResult();
			long last = page.getLastResult();

			if (fileContents.size() > 0) {
				sb.append("[");
				for (int i = (int) first; i < last; i++) {
					YHFileContent content = fileContents.get(i);
					String subject = content.getSubject() == null ? "" : content.getSubject();
					String attachmentName = content.getAttachmentName() == null ? "" : content.getAttachmentName();
					String contentNo = content.getContentNo() == null ? "" : content.getContentNo();

					String attchmentDesc = content.getAttachmentDesc() == null ? "" : content.getAttachmentDesc();

					String date = "";
					if (content.getSendTime() != null) {
						date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(content.getSendTime());
					}
					String sendTime = date == null ? "" : date;

					String YM = contentLogic.getFilePathFolder(YHUtility.null2Empty(content.getAttachmentId()));
					// String attachmentNameStr = content.getAttachmentName();

//					subject = subject.replaceAll("[\n-\r]", "<br>");
//					subject = subject.replaceAll("[\\\\/:*?\"<>|]", "");
//					subject = subject.replace("\"", "\\\"");

//					contentNo = contentNo.replaceAll("[\n-\r]", "<br>");
//					contentNo = contentNo.replaceAll("[\\\\/:*?\"<>|]", "");
//					contentNo = contentNo.replace("\"", "\\\"");

//					attchmentDesc = attchmentDesc.replaceAll("[\n-\r]", "<br>");
//					attchmentDesc = attchmentDesc.replaceAll("[\\\\/:*?\"<>|]", "");
//					attchmentDesc = attchmentDesc.replace("\"", "\\\"");

					sb.append("{");
					sb.append("contentId:\"" + content.getSeqId() + "\"");
					sb.append(",sortId:\"" + content.getSortId() + "\"");
					sb.append(",subject:\"" + YHUtility.encodeSpecial(subject) + "\"");

					String contentStr = YHUtility.null2Empty(content.getContent());

//					contentStr = contentStr.replaceAll("[\n-\r]", "<br>");
//					contentStr = contentStr.replace("\"", "\\\"");

					sb.append(",content:\"" + YHUtility.encodeSpecial(contentStr) + "\"");

					sb.append(",sendTime:\"" + sendTime + "\"");
					sb.append(",attachmentId:\"" + content.getAttachmentId() + "\"");
					sb.append(",attachmentName:\"" + YHUtility.encodeSpecial(attachmentName) + "\"");
					sb.append(",attachmentDesc:\"" + YHUtility.encodeSpecial(attchmentDesc) + "\"");
					sb.append(",userId:\"" + YHUtility.null2Empty(content.getUserId()) + "\"");
					sb.append(",contentNo:\"" + YHUtility.encodeSpecial(contentNo) + "\"");
					sb.append(",newPerson:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(content.getNewPerson())) + "\"");
					sb.append(",readers:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(content.getReaders())) + "\"");
					sb.append(",creater:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(content.getCreater())) + "\"");

					String logStr = YHUtility.null2Empty(content.getLogs());
					logStr.replaceAll("[\n-\r]", "<br>");
					logStr = logStr.replace("\r\n", "<br>");
					logStr = logStr.replace("\n", "<br>");

					sb.append(",logs:\"" + logStr + "\"");
					sb.append(",YM:\"" + YM + "\"");

					sb.append(",ascDesc:\"" + ascDesc + "\"");
					sb.append(",orderBy:\"" + orderBy + "\"");

					sb.append(",totalRecord:\"" + count + "\"");
					sb.append(",pageNo:\"" + currNo + "\"");
					sb.append(",pageSize:\"" + pageSize + "\"");

					sb.append("},");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append("]");
			} else {
				sb.append("[]");
			}

			// System.out.println("sb>>>>>>>>>>" + sb);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获得文件夹下的所有内容信息
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getShareFileContentInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqId = request.getParameter("seqId");
		String shareFlag = request.getParameter("shareFlag");

		String pageNoStr = request.getParameter("pageNo");
		String pageSizeStr = request.getParameter("pageSize");

		int sortId = 0;
		if (seqId != null && !"".equals(seqId)) {
			sortId = Integer.parseInt(seqId);
		}

		int currNo = 1;
		if (YHUtility.isNullorEmpty(pageNoStr)) {
			currNo = 1;
		} else {
			currNo = Integer.parseInt(pageNoStr);
		}

		if (shareFlag == null) {
			shareFlag = "";
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		Map map = new HashMap();
		map.put("SORT_ID", sortId);

		if (sortId == 0) {
			map.put("USER_ID", String.valueOf(loginUserSeqId));
		}

		// if (!"share".equals(shareFlag.trim())) {
		// map.put("USER_ID", String.valueOf(loginUserSeqId));
		//
		// }

		Connection dbConn = null;
		YHPersonFileContentLogic contentLogic = new YHPersonFileContentLogic();
		StringBuffer sb = new StringBuffer();
		List<YHFileContent> fileContents = new ArrayList<YHFileContent>();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			fileContents = contentLogic.getFileContentsInfo(dbConn, map);

			long count = fileContents.size();
			int pageSize = 20;// 一个页面显示的数目
			if (pageSizeStr != null) {
				pageSize = Integer.parseInt(pageSizeStr);
			}

			YHPage page = new YHPage(pageSize, count, (currNo + 1));
			long first = page.getFirstResult();
			long last = page.getLastResult();

			if (fileContents.size() > 0) {
				sb.append("[");
				for (int i = (int) first; i < last; i++) {
					YHFileContent content = fileContents.get(i);
					String subject = content.getSubject() == null ? "" : content.getSubject();
					String attachmentName = content.getAttachmentName() == null ? "" : content.getAttachmentName();
					String contentNo = content.getContentNo() == null ? "" : content.getContentNo();
					String attachmentDesc = content.getAttachmentDesc() == null ? "" : content.getAttachmentDesc();
					String date = "";
					if (content.getSendTime() != null) {
						date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(content.getSendTime());
					}
					String sendTime = date == null ? "" : date;

					String YM = contentLogic.getFilePathFolder(YHUtility.null2Empty(content.getAttachmentId()));
					String attachmentNameStr = YHUtility.null2Empty(content.getAttachmentName());

					subject = subject.replaceAll("[\n-\r]", "<br>");
					subject = subject.replaceAll("[\\\\/:*?\"<>|]", "");
					subject = subject.replace("\"", "\\\"");

					contentNo = contentNo.replaceAll("[\n-\r]", "<br>");
					contentNo = contentNo.replaceAll("[\\\\/:*?\"<>|]", "");
					contentNo = contentNo.replace("\"", "\\\"");

					attachmentDesc = attachmentDesc.replaceAll("[\n-\r]", "<br>");
					attachmentDesc = attachmentDesc.replaceAll("[\\\\/:*?\"<>|]", "");
					attachmentDesc = attachmentDesc.replace("\"", "\\\"");

					sb.append("{");
					sb.append("contentId:\"" + content.getSeqId() + "\"");
					sb.append(",sortId:\"" + content.getSortId() + "\"");
					sb.append(",subject:\"" + subject + "\"");

					String contentStr = YHUtility.null2Empty(content.getContent());

					contentStr = contentStr.replaceAll("[\n-\r]", "<br>");
					contentStr = contentStr.replace("\"", "\\\"");

					sb.append(",content:\"" + contentStr + "\"");

					sb.append(",sendTime:\"" + sendTime + "\"");
					sb.append(",attachmentId:\"" + content.getAttachmentId() + "\"");
					sb.append(",attachmentName:\"" + attachmentName + "\"");
					sb.append(",attachmentDesc:\"" + attachmentDesc + "\"");
					sb.append(",userId:\"" + content.getUserId() + "\"");
					sb.append(",contentNo:\"" + contentNo + "\"");
					sb.append(",newPerson:\"" + content.getNewPerson() + "\"");
					sb.append(",readers:\"" + content.getReaders() + "\"");
					sb.append(",creater:\"" + content.getCreater() + "\"");
					sb.append(",logs:\"" + content.getLogs() + "\"");
					sb.append(",YM:\"" + YM + "\"");

					sb.append(",totalRecord:\"" + count + "\"");
					sb.append(",pageNo:\"" + currNo + "\"");
					sb.append(",pageSize:\"" + pageSize + "\"");

					sb.append("},");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append("]");
			} else {
				sb.append("[]");
			}

			// System.out.println("sb>>>>>>>>>>" + sb);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 从个人文件柜选择附件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPersonContentsById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqId = request.getParameter("seqId");
		int sortId = 0;
		if (seqId != null) {
			sortId = Integer.parseInt(seqId);
		}

		String ext_filter = request.getParameter("EXT_FILTER");
		if (ext_filter == null) {
			ext_filter = "";
		}

		String div_id = request.getParameter("DIV_ID");
		if (div_id == null) {
			div_id = "";
		}
		String dir_field = request.getParameter("DIR_FIELD");
		if (dir_field == null) {
			dir_field = "";
		}
		String name_field = request.getParameter("NAME_FIELD");
		if (name_field == null) {
			name_field = "";
		}
		String type_field = request.getParameter("TYPE_FIELD");
		if (type_field == null) {
			type_field = "";
		}
		String multi_select = request.getParameter("MULTI_SELECT");
		if (multi_select == null) {
			multi_select = "";
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		// YHFileContentLogic contentLogic = new YHFileContentLogic();
		YHPersonFileContentLogic contentLogic = new YHPersonFileContentLogic();
		StringBuffer sb = new StringBuffer();

		Map map = new HashMap();
		map.put("SORT_ID", sortId);
		map.put("USER_ID", String.valueOf(loginUserSeqId));
		boolean isHave = false;

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			// List<YHFileContent> contentList =
			// contentLogic.getFileContentsInfo(dbConn, map);

			List<YHFileContent> fileContents = contentLogic.getFileContentsInfo(dbConn, map);

			if (fileContents != null && fileContents.size() > 0) {
				sb.append("[");
				for (int i = 0; i < fileContents.size(); i++) {
					YHFileContent content = fileContents.get(i);
					String subject = content.getSubject() == null ? "" : content.getSubject();
					String attachmentId = content.getAttachmentName() == null ? "" : content.getAttachmentName();
					String attachmentName = content.getAttachmentName() == null ? "" : content.getAttachmentName();

					if (attachmentId != null && !"".equals(attachmentId)) {
						subject = subject.replaceAll("[\n-\r]", "<br>");
						subject = subject.replaceAll("[\\\\/:*?\"<>|]", "");
						subject = subject.replace("\"", "\\\"");

						sb.append("{");
						sb.append("contentId:\"" + content.getSeqId() + "\"");
						sb.append(",sortId:\"" + content.getSortId() + "\"");
						sb.append(",subject:\"" + subject + "\"");
						sb.append(",attachmentId:\"" + YHUtility.null2Empty(content.getAttachmentId()) + "\"");
						sb.append(",attachmentName:\"" + attachmentName + "\"");

						sb.append(",ext_filter:\"" + ext_filter + "\"");
						sb.append(",div_id:\"" + div_id + "\"");
						sb.append(",dir_field:\"" + dir_field + "\"");
						sb.append(",name_field:\"" + name_field + "\"");
						sb.append(",type_field:\"" + type_field + "\"");
						sb.append(",multi_select:\"" + multi_select + "\"");

						sb.append("},");

						isHave = true;

					}

				}
				if (isHave) {

					sb.deleteCharAt(sb.length() - 1);

				}
				sb.append("]");
			} else {
				sb.append("[]");
			}

			// System.out.println("sb>>>>>>>>>>" + sb);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());

		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 判断附件是否存在本地硬盘上
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String isHaveFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String attIdStr = request.getParameter("attIdStr");
		String attNameStr = request.getParameter("attNameStr");

		if (attIdStr == null) {
			attIdStr = "";
		}
		if (attNameStr == null) {
			attNameStr = "";
		}

		YHPersonFileContentLogic logic = new YHPersonFileContentLogic();

		try {

			String data = logic.isHaveFileInDisk(attIdStr, attNameStr);
			;

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "文件签阅成功");
			request.setAttribute(YHActionKeys.RET_DATA, data);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 文件批量上传
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// System.out.println(request.getCharacterEncoding());
		YHPersonFileContentLogic contentLogic = new YHPersonFileContentLogic();
		YHFileContent content = new YHFileContent();
		String seqId = request.getParameter("seqId");
		String remoteAddr = request.getRemoteAddr();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(date);
			String separator = File.separator;
			String filePath = YHSysProps.getAttachPath() + separator + "file_folder" + separator + currDate; 
			
			YHFileUploadForm fileForm = new YHFileUploadForm();
			fileForm.parseUploadRequest(request);
			String fileExists = fileForm.getExists(filePath);
			if (fileExists != null) {
				response.setCharacterEncoding(YHConst.DEFAULT_CODE);
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter pw = response.getWriter();
				pw.println("-ERR 文件\"" + fileExists + "\"已经存在！");
				pw.flush();
				return null;
			}
			
			
			contentLogic.uploadFileLogic(dbConn, content, fileForm,loginUser,seqId,filePath);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";

	}

	/**
	 * 根据contentId获得内容信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getFileContentInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// String seqId = request.getParameter("seqId");
		String contentIdStr = request.getParameter("contentId");
		int contentId = 0;
		if (contentIdStr != null) {
			contentId = Integer.parseInt(contentIdStr);
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		YHPersonFileContentLogic contentLogic = new YHPersonFileContentLogic();
		StringBuffer sb = new StringBuffer("[");
		YHFileContent fileContent = new YHFileContent();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			Map map = new HashMap();
			map.put("SEQ_ID", contentId);
			// map.put("USER_ID", loginUserSeqId);

			// fileContent = contentLogic.getFileContentInfoById(dbConn, contentId);
			fileContent = contentLogic.getFileInfoByMap(dbConn, map);

			String subject = fileContent.getSubject() == null ? "" : fileContent.getSubject();
			String content = fileContent.getContent() == null ? "" : fileContent.getContent();
			String attachmentDesc = fileContent.getAttachmentDesc() == null ? "" : fileContent.getAttachmentDesc();

			String attachmentName = fileContent.getAttachmentName() == null ? "" : fileContent.getAttachmentName();
			String contentNo = fileContent.getContentNo() == null ? "" : fileContent.getContentNo();
			String date = "";
			if (fileContent.getSendTime() != null) {
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fileContent.getSendTime());
			}
			String sendTime = date == null ? "" : date;

			YHFileContentLogic logic = new YHFileContentLogic();
			boolean isRead = logic.isHaveReaderId(loginUserSeqId, fileContent.getReaders());
			int reader = 0;
			if (isRead) {
				reader = 1;
			}

			String createName = logic.getPersonNamesByIds(dbConn, YHUtility.null2Empty(fileContent.getCreater()));

//			subject = subject.replaceAll("[\n-\r]", "<br>");
//			subject = subject.replaceAll("[\\\\/:*?\"<>|]", "");
//			subject = subject.replace("\"", "\\\"");

//			contentNo = contentNo.replaceAll("[\n-\r]", "<br>");
//			contentNo = contentNo.replaceAll("[\\\\/:*?\"<>|]", "");
//			contentNo = contentNo.replace("\"", "\\\"");

//			attachmentDesc = attachmentDesc.replaceAll("[\n-\r]", "<br>");
//			attachmentDesc = attachmentDesc.replaceAll("[\\\\/:*?\"<>|]", "");
//			attachmentDesc = attachmentDesc.replace("\"", "\\\"");

			sb.append("{");
			sb.append("contentId:\"" + fileContent.getSeqId() + "\"");
			sb.append(",sortId:\"" + fileContent.getSortId() + "\"");
			sb.append(",subject:\"" + YHUtility.encodeSpecial(subject) + "\"");
//			String contentStr = content;
//			contentStr = contentStr.replaceAll("[\n-\r]", "<br>");
//			contentStr = contentStr.replace("\"", "\\\"");

			sb.append(",content:\"" + YHUtility.encodeSpecial(content) + "\"");
			sb.append(",sendTime:\"" + sendTime + "\"");
			sb.append(",attachmentId:\"" + YHUtility.null2Empty(fileContent.getAttachmentId()).trim() + "\"");
			sb.append(",attachmentName:\"" + YHUtility.encodeSpecial(attachmentName.trim()) + "\"");
			sb.append(",attachmentDesc:\"" + YHUtility.encodeSpecial(attachmentDesc) + "\"");
			sb.append(",contentNo:\"" + YHUtility.encodeSpecial(contentNo) + "\"");
			// sb.append(",userId:\"" + fileContent.getUserId() + "\"");
			// sb.append(",newPerson:\"" + fileContent.getNewPerson() + "\"");
			sb.append(",readers:\"" + reader + "\"");
			sb.append(",creater:\"" + createName + "\"");

			String logStr = YHUtility.null2Empty(fileContent.getLogs());
			logStr.replaceAll("[\n-\r]", "<br>");
			logStr = logStr.replace("\r\n", "<br>");
			logStr = logStr.replace("\n", "<br>");

			sb.append(",logs:\"" + logStr + "\"");
			sb.append("}");
			sb.append("]");

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 编辑文件中的新建文件(附件)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @return
	 * @throws Exception
	 */
	public String newFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String seqIdStr = request.getParameter("seqId");
		String contentIdStr = request.getParameter("contentId");
		String attachmentName = request.getParameter("newAttachmentName");
		String fileType = request.getParameter("newFileType");
		String subject = request.getParameter("newSubject");

		String contentNo = request.getParameter("newContentNo");
		String content = request.getParameter("newContent");
		String atttDesc = request.getParameter("newAtttDesc");
		String returnFlag = request.getParameter("returnFlag");

		String managerPriSeqId = request.getParameter("managerPriSeqId");

		int contentId = 0;
		if (contentIdStr != null) {
			contentId = Integer.parseInt(contentIdStr);
		}
		int seqId = 0;
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}
		if (returnFlag == null) {
			returnFlag = "";
		}
		if ("".equals(returnFlag.trim())) {
			returnFlag = "returnEdit";
		}
		if (managerPriSeqId == null) {
			managerPriSeqId = "0";
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		YHPersonFileContentLogic logic = new YHPersonFileContentLogic();
		StringBuffer buffer = new StringBuffer();
		Map map = new HashMap();
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(new Date());

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			Map map2 = new HashMap();
			map2.put("SEQ_ID", contentId);
			// map2.put("USER_ID", loginUserSeqId);
			YHFileContent fileContent = logic.getFileInfoByMap(dbConn, map2);
			// String attachmentDesc = fileContent.getAttachmentDesc();
			String attachId = fileContent.getAttachmentId() == null ? "" : fileContent.getAttachmentId();
			String attachName = fileContent.getAttachmentName() == null ? "" : fileContent.getAttachmentName();

			String attachmentIds = attachId;
			String attachmentNames = attachName;
			String newAttachIdStr = "";
			String newAttachNameStr = "";
			String realPath = request.getRealPath("/");
			String rand = logic.createFile(fileType, attachmentName, realPath);
			if (!rand.equals("0")) {
				attachmentIds = attachId + currDate + "_" + String.valueOf(rand) + ",";
				attachmentNames = attachName + attachmentName + "." + fileType + "*";

				newAttachIdStr = currDate + "_" + String.valueOf(rand);
				newAttachNameStr = attachmentName + "." + fileType;
			}

			fileContent.setSubject(subject);
			fileContent.setAttachmentId(attachmentIds);
			fileContent.setAttachmentName(attachmentNames);
			fileContent.setContentNo(contentNo);
			fileContent.setContent(content);
			fileContent.setAttachmentDesc(atttDesc);
			logic.updateSingleObj(dbConn, fileContent);

			request.setAttribute("newAttachIdStr", newAttachIdStr);
			request.setAttribute("newAttachNameStr", newAttachNameStr);

		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		if ("returnEdit".equals(returnFlag.trim())) {
			return "/core/funcs/personfolder/edit.jsp?seqId=" + seqId + "&contentId=" + contentId;

		} else if ("shareEdit".equals(returnFlag.trim())) {
			return "/core/funcs/personfolder/shareEdit.jsp?seqId=" + seqId + "&contentId=" + contentId + "&managerPriSeqId=" + managerPriSeqId;
		}

		return "";
	}

	/**
	 * 根据id更新文件数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateFileInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHPersonFileContentLogic fileContentLogic = new YHPersonFileContentLogic();
		String contentIdStr = request.getParameter("contentId");
		String type = request.getParameter("fileType");
		String sortIdStr = request.getParameter("sortId");
		int sortId = 0;
		int contentId = 0;
		if (sortIdStr != null) {
			sortId = Integer.parseInt(sortIdStr);
		}
		if (contentIdStr != null) {
			contentId = Integer.parseInt(contentIdStr);
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		// 保存从文件柜、网络硬盘选择附件
		YHSelAttachUtil sel = new YHSelAttachUtil(request, "file_folder");
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

		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(new Date());
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			// YHFileContent content = fileContentLogic.getFileContentInfoById(dbConn,
			// contentId);
			Map map2 = new HashMap();
			map2.put("SEQ_ID", contentId);
			// map2.put("USER_ID", loginUserSeqId);
			YHFileContent content = fileContentLogic.getFileInfoByMap(dbConn, map2);

			String idStr = YHUtility.null2Empty(content.getAttachmentId());
			String nameStr = YHUtility.null2Empty(content.getAttachmentName());

			YHFileContent fileContent = (YHFileContent) YHFOM.build(request.getParameterMap());
			String attachmentName = YHUtility.null2Empty(fileContent.getAttachmentName()).trim();

			boolean newTypeFlag = false;
			String attIdString = "";
			String realPath = request.getRealPath("/");

			if (!YHUtility.isNullorEmpty(type) && !YHUtility.isNullorEmpty(attachmentName)) {
				String rand = fileContentLogic.createFile(type, attachmentName, realPath);
				if (!rand.equals("0")) {
					attIdString = currDate + "_" + String.valueOf(rand) + ",";
					attachmentName = attachmentName + "." + type + "*";
					newTypeFlag = true;
				}
			}

			if (newTypeFlag && fromFolderFlag) {
				content.setAttachmentId(idStr.trim() + newAttchId.trim() + attIdString.trim());
				content.setAttachmentName(nameStr.trim() + newAttchName.trim() + attachmentName.trim());

			} else if (newTypeFlag) {
				content.setAttachmentId(idStr.trim() + attIdString.trim());
				content.setAttachmentName(nameStr.trim() + attachmentName.trim());

			} else if (fromFolderFlag) {
				content.setAttachmentId(idStr.trim() + newAttchId.trim());
				content.setAttachmentName(nameStr.trim() + newAttchName.trim());
			} else {
				content.setAttachmentId(idStr.trim());
				content.setAttachmentName(nameStr.trim());
			}

			content.setContentNo(fileContent.getContentNo());
			content.setSubject(fileContent.getSubject());
			content.setContent(fileContent.getContent());
			content.setAttachmentDesc(fileContent.getAttachmentDesc());

			fileContentLogic.updateSingleObj(dbConn, content);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 新建文件信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addNewFileInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 保存从文件柜、网络硬盘选择附件
		YHSelAttachUtil sel = new YHSelAttachUtil(request, "file_folder");
		String attIdStr = sel.getAttachIdToString(",");
		String attNameStr = sel.getAttachNameToString("*");

		String attachAction = (String) request.getParameter("attachAction");

		String seqIdStr = request.getParameter("seqId");
		String type = request.getParameter("fileType");
		String subject = request.getParameter("subject");
		String contentNo = request.getParameter("contentNo");
		String content = request.getParameter("content");
		String attachmentName = request.getParameter("attachmentName");
		String attachmentDesc = request.getParameter("attachmentDesc");

		String contentIdStr = request.getParameter("contentId");
		String smsPerson = request.getParameter("smsPerson");
		String folderPath = request.getParameter("folderPath");

		int contentId = 0;
		int sortId = 0;
		if (seqIdStr != null) {
			sortId = Integer.parseInt(seqIdStr);
		}
		if (contentIdStr != null) {
			contentId = Integer.parseInt(contentIdStr);
		}
		if (smsPerson == null) {
			smsPerson = "";
		}
		if (attachAction == null) {
			attachAction = "";
		}
		if (YHUtility.isNullorEmpty(attachmentName)) {
			attachmentName = "";
		}else {
			attachmentName = attachmentName.trim();
		}

		// // 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		YHPersonFileContentLogic contentLogic = new YHPersonFileContentLogic();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			boolean fromFolderFlag = false;
			String newAttchId = "";
			String newAttchName = "";
			if (!"".equals(attIdStr) && !"".equals(attNameStr)) {
				newAttchId = attIdStr + ",";
				newAttchName = attNameStr + "*";
				fromFolderFlag = true;

			}

			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(new Date());
			YHFileContent fileContent = new YHFileContent();

			if (contentId != 0) {
				// 还得考虑当type有值时，
				YHFileContent fileContentStr = contentLogic.getFileContentInfoById(dbConn, contentId);
				String attIdString = YHUtility.null2Empty(fileContentStr.getAttachmentId());
				String attNameString = YHUtility.null2Empty(fileContentStr.getAttachmentName());
				fileContentStr.setAttachmentId(attIdString + newAttchId.trim());
				fileContentStr.setAttachmentName(attNameString + newAttchName.trim());
				fileContentStr.setContentNo(contentNo);
				fileContentStr.setSubject(subject);
				fileContentStr.setContent(content);
				fileContentStr.setAttachmentDesc(attachmentDesc);
				fileContentStr.setSendTime(YHUtility.parseTimeStamp());
				contentLogic.updateSingleObj(dbConn, fileContentStr);

			} else {
				boolean newTypeFlag = false;
				String attIdString = "";
				String attNameString = "";
				String realPath = request.getRealPath("/");

				if (!YHUtility.isNullorEmpty(type) && !YHUtility.isNullorEmpty(attachmentName)) {
					String rand = contentLogic.createFile(type, attachmentName, realPath);
					if (!rand.equals("0")) {
						attIdString = currDate + "_" + String.valueOf(rand) + ",";
						attNameString = attachmentName + "." + type.trim() + "*";
						newTypeFlag = true;

					}
				}

				if (newTypeFlag && fromFolderFlag) {
					fileContent.setAttachmentId(newAttchId.trim() + attIdString.trim());
					fileContent.setAttachmentName(newAttchName.trim() + attachmentName.trim());

				} else if (newTypeFlag) {
					fileContent.setAttachmentId(attIdString.trim());
					fileContent.setAttachmentName(attNameString.trim());
				} else if (fromFolderFlag) {
					fileContent.setAttachmentId(newAttchId.trim());
					fileContent.setAttachmentName(newAttchName.trim());
				}

				fileContent.setSortId(sortId);
				fileContent.setContentNo(contentNo);
				fileContent.setSubject(subject);
				fileContent.setContent(content);
				fileContent.setAttachmentDesc(attachmentDesc);
				fileContent.setSendTime(YHUtility.parseTimeStamp());
				fileContent.setUserId(String.valueOf(loginUserSeqId));
				fileContent.setCreater(String.valueOf(loginUserSeqId));
				contentLogic.saveSingleObj(dbConn, fileContent);

			}

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		if ("newAttach".equals(attachAction.trim())) {
			// return "/core/funcs/personfolder/folder.jsp?seqId=" + sortId ;
			String contextPath = request.getContextPath();
			response.sendRedirect(contextPath + "/core/funcs/personfolder/folder.jsp?seqId=" + sortId);
			return "";

		} else {
			return "/core/inc/rtjson.jsp";

		}
	}

	/**
	 * 删除所选择的seqId信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delCheckedFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqIdStr");

		String separator = File.separator;
		String filePath = YHSysProps.getAttachPath() + separator + "file_folder" + separator; // YHSysProps.getAttachPath()得到

		YHPersonFileContentLogic contentLogic = new YHPersonFileContentLogic();
		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();

			if (seqIdStr.trim().endsWith(",")) {
				seqIdStr = seqIdStr.trim().substring(0, seqIdStr.trim().length() - 1);
			}

			contentLogic.delFile(dbConn, seqIdStr, filePath);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "数据删除成功！");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 *复制文件信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String copyFileByIds(HttpServletRequest request, HttpServletResponse response) {
		String seqIdStrs = request.getParameter("seqIdStrs");
		String action = request.getParameter("action");
		String perFolderSeqId = request.getParameter("perFolderSeqId");
		if (seqIdStrs.endsWith(",")) {
			seqIdStrs = seqIdStrs.substring(0, seqIdStrs.length() - 1);
		}

		// Connection dbConn=null;
		try {
			// YHRequestDbConn requestDbConn=(YHRequestDbConn)
			// request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			// dbConn=requestDbConn.getSysDbConn();
			request.getSession().setAttribute("perFolderSeqId", perFolderSeqId);
			request.getSession().setAttribute("perContentIdStrs", seqIdStrs);
			request.getSession().setAttribute("perActionStr", action);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "完成复制!");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 粘贴文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String pasteFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStrs = (String) request.getSession().getAttribute("perContentIdStrs");
		String action = (String) request.getSession().getAttribute("perActionStr");
		String sortIdStr = (String) request.getParameter("sortId");

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		String separator = File.separator;
		String filePath = YHSysProps.getAttachPath() + separator + "file_folder" + separator; // YHSysProps.getAttachPath()得到
		// D:\\YH\\attach

		YHPersonFileContentLogic contentLogic = new YHPersonFileContentLogic();
		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();
			if ("copyFile".equals(action)) {
				contentLogic.copyFile(dbConn, seqIdStrs, sortIdStr, filePath, loginUserSeqId);

			} else if ("cutFile".equals(action)) {
				contentLogic.cutFile(dbConn, seqIdStrs, sortIdStr, filePath, loginUserSeqId);
			}
			request.getSession().setAttribute("perFolderSeqId", "");
			request.getSession().setAttribute("perContentIdStrs", "");
			request.getSession().setAttribute("perActionStr", "");

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功粘贴数据");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 新建文件单个文件上传
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String newFileSingleUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		String contextPath = request.getContextPath();
    String failseFlag = request.getParameter("failseFlag");
    String contentStr = request.getParameter("contentId");
    String seqIdStr = request.getParameter("seqId"); // 文件夹的seqId
    String managerPriSeqId = request.getParameter("managerPriSeqId"); // 文件夹的seqId
    int contentId = 0;
    if (!YHUtility.isNullorEmpty(contentStr)) {
      contentId = Integer.parseInt(contentStr);
    }
    int sortId = 0;
    if (!YHUtility.isNullorEmpty( seqIdStr)) {
      sortId = Integer.parseInt(seqIdStr);
    }
    if (YHUtility.isNullorEmpty(managerPriSeqId)) {
      managerPriSeqId= "0";
    }
    
    try {
      fileForm.parseUploadRequest(request);
    } catch (Exception e) {
      String actionStr = "";
      if ("returnNew".equalsIgnoreCase(failseFlag)) {
        actionStr = "new";
      }else if ("returnEdit".equalsIgnoreCase(failseFlag)) {
        actionStr = "edit";
      }else if ("returnFolder".equalsIgnoreCase(failseFlag)) {
        actionStr = "folder";
      }else if ("shareEdit".equalsIgnoreCase(failseFlag)) {
        actionStr = "shareEdit";
      }else if ("returnShareFolder".equalsIgnoreCase(failseFlag)) {
        actionStr = "shareFolder";
      }
      response.sendRedirect(contextPath + "/core/funcs/personfolder/uploadFailse.jsp?actionFlag=" + actionStr + "&seqId=" + sortId + "&contentId=" + contentId + "&managerPriSeqId=" + managerPriSeqId);
      return null;
    }
		
		// 保存从文件柜、网络硬盘选择附件
		YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, "file_folder");
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

		// 注意这里的
		String actionFlag = (String) fileForm.getParameter("actionFlag");
		String retrunFlag = fileForm.getParameter("retrunFlag"); // 返回页面returnFolderFlag
		String returnFolderFlag = fileForm.getParameter("returnFolderFlag");

		String subject1 = fileForm.getParameter("subject1");
		String contentNo1 = fileForm.getParameter("contentNo1");
		String content1 = fileForm.getParameter("content1");
		String attachmentName1 = fileForm.getParameter("attachmentName1");
		String fileType1 = fileForm.getParameter("fileType1");
		String attachmentDesc1 = fileForm.getParameter("attachmentDesc1");
		String newManagerPriSeqId = fileForm.getParameter("newManagerPriSeqId");

		if (actionFlag == null) {
			actionFlag = "";
		}
		if (newManagerPriSeqId == null) {
			newManagerPriSeqId = "0";
		}
		if (actionFlag == null || "".equals(actionFlag)) {
			actionFlag = "new";
		}
		if (returnFolderFlag == null) {
			returnFolderFlag = "";
		}
		if (subject1 == null) {
			subject1 = "";
		}
		if (contentNo1 == null) {
			contentNo1 = "";
		}
		if (content1 == null) {
			content1 = "";
		}
		if (attachmentName1 == null) {
			attachmentName1 = "";
		}
		if (fileType1 == null) {
			fileType1 = "";
		}
		if (attachmentDesc1 == null) {
			attachmentDesc1 = "";
		}

		if (!"".equals(returnFolderFlag.trim())) {
			retrunFlag = returnFolderFlag;

		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		YHPersonFileContentLogic logic = new YHPersonFileContentLogic();
		YHFileContent fileContent = new YHFileContent();

		// String content1="";

		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();

			boolean flag = false;
			String attachmentId = "";
			String attachmentName = "";

			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(date);
			String separator = File.separator;
			String filePath = YHSysProps.getAttachPath() + separator + "file_folder" + separator + currDate; // YHSysProps.getAttachPath()得到

			Iterator<String> iKeys = fileForm.iterateFileFields();
			while (iKeys.hasNext()) {
				String fieldName = iKeys.next();
				String fileName = fileForm.getFileName(fieldName);
				// System.out.println("fieldName>>" + fieldName);
				String regName = fileName;
				if (YHUtility.isNullorEmpty(fileName)) {
					continue;
				}
				// System.out.println("fieldName>>" + fieldName);
				YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
				String rand = emul.getRandom();

				attachmentId += currDate + "_" + rand + ",";
				attachmentName += fileName + "*";

				fileName = rand + "_" + fileName;
				fileForm.saveFile(fieldName, filePath +File.separator+ fileName);

				String regAttId = currDate + "_" + rand;
				String contentFilePath = request.getContextPath() + "/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?attachmentName=" + regName
						+ "&amp;attachmentId=" + regAttId + "&amp;module=file_folder&amp;directView=1";

				String isImage = "^.*?(\\.(png|gif|jpg|bmp|PNG|GIF|JPG|BMP))$";

				if (fileName.matches(isImage)) {
					String regex = "\\ssrc=\"file://?[^\"']+/" + regName + "\"\\s";
					content1 = content1.replaceAll(regex, " src=\"" + contentFilePath + "\" ");

				}

				flag = true;
			}

			if (flag) {

				if ("edit".equals(actionFlag.trim())) {
					YHFileContent content = logic.getFileContentInfoById(dbConn, contentId);
					String attIdString = content.getAttachmentId();
					String attNameString = content.getAttachmentName();

					content.setAttachmentId(YHUtility.null2Empty(attIdString).trim() + YHUtility.null2Empty(attachmentId).trim() + YHUtility.null2Empty(newAttchId).trim());
					content.setAttachmentName(YHUtility.null2Empty(attNameString).trim() + YHUtility.null2Empty(attachmentName).trim() + YHUtility.null2Empty(newAttchName).trim());
					content.setSendTime(YHUtility.parseTimeStamp());
					content.setSubject(subject1);
					content.setContent(content1);
					content.setAttachmentDesc(attachmentDesc1);
					content.setContentNo(contentNo1);
					logic.updateSingleObj(dbConn, content);

				} else if ("new".equals(actionFlag.trim())) {
					fileContent.setSortId(sortId);
					fileContent.setSubject(subject1);
					fileContent.setContent(content1);
					fileContent.setSendTime(YHUtility.parseTimeStamp());
					fileContent.setAttachmentId(YHUtility.null2Empty(attachmentId).trim() + YHUtility.null2Empty(newAttchId).trim());
					fileContent.setAttachmentName(YHUtility.null2Empty(attachmentName).trim() + YHUtility.null2Empty(newAttchName).trim());
					fileContent.setAttachmentDesc(attachmentDesc1);
					fileContent.setContentNo(contentNo1);
					fileContent.setUserId(String.valueOf(loginUserSeqId));
					fileContent.setCreater(String.valueOf(loginUserSeqId));
					logic.saveSingleObj(dbConn, fileContent);

					// 得到最大id
					fileContent = logic.getMaxSeqId(dbConn);
					contentId = fileContent.getSeqId();

					request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
					request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
				}
			} else {
				request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
			}
			actionFlag = "edit";

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		if ("returnNew".equals(retrunFlag.trim())) {

			response.sendRedirect(contextPath + "/core/funcs/personfolder/new/newFile.jsp?actionFlag=" + actionFlag + "&contentId=" + contentId + "&seqId="
					+ sortId);

		} else if ("returnEdit".equals(retrunFlag.trim())) {
			response.sendRedirect(contextPath + "/core/funcs/personfolder/edit.jsp?seqId=" + sortId + "&contentId=" + contentId);

		} else if ("returnFolder".equals(retrunFlag.trim())) {

			response.sendRedirect(contextPath + "/core/funcs/personfolder/folder.jsp?seqId=" + sortId);
		} else if ("shareEdit".equals(retrunFlag.trim())) {
			response.sendRedirect(contextPath + "/core/funcs/personfolder/shareEdit.jsp?seqId=" + sortId + "&contentId=" + contentId + "&managerPriSeqId="
					+ newManagerPriSeqId);

		} else if ("returnShareFolder".equals(retrunFlag.trim())) {
			response.sendRedirect(contextPath + "/core/funcs/personfolder/shareFolder.jsp?seqId=" + sortId);

		}
		return "";

		// if ("newAttach".equals(attachAction.trim())) {
		// String contextPath = request.getContextPath();
		// response.sendRedirect(contextPath +
		// "/core/funcs/personfolder/folder.jsp?seqId=" + sortId);
		//      
		// return ""; //"/core/funcs/personfolder/folder.jsp?seqId=" + sortId ;
		//      
		// }else if("editAttach".equals(attachAction.trim())) {
		//      
		// String contextPath = request.getContextPath();
		// response.sendRedirect(contextPath +
		// "/core/funcs/personfolder/edit.jsp?actionFlag=" + actionFlag +
		// "&contentId=" + contentId+"&seqId="+sortId);
		// return "";
		//      
		//      
		// }else {
		//
		// //return "/core/funcs/personfolder/new/newFile.jsp?actionFlag=" +
		// actionFlag + "&contentId=" + contentId;
		// String contextPath = request.getContextPath();
		// response.sendRedirect(contextPath +
		// "/core/funcs/personfolder/new/newFile.jsp?actionFlag=" + actionFlag +
		// "&contentId=" + contentId+"&seqId="+sortId);
		// return "";
		// }

	}

	/**
	 * 共享文件编辑中的单个文件上传
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String shareEdiSingleUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		// 注意这里的
		fileForm.parseUploadRequest(request);
		String shareFlagStr = (String) fileForm.getParameter("shareFlag");

		String seqIdStr = request.getParameter("seqId"); // 文件夹的seqId
		String subject1 = fileForm.getParameter("subject1");
		String contentNo1 = fileForm.getParameter("contentNo1");
		String content1 = fileForm.getParameter("content1");
		String attachmentName1 = fileForm.getParameter("attachmentName1");
		String fileType1 = fileForm.getParameter("fileType1");
		String attachmentDesc1 = fileForm.getParameter("attachmentDesc1");

		int contentId = 0;
		String contentStr = request.getParameter("contentId");
		if (contentStr != null) {
			contentId = Integer.parseInt(contentStr);
		}

		int shareFlag = 0;

		if (shareFlagStr != null) {
			shareFlag = Integer.parseInt(shareFlagStr);
		}

		int sortId = 0;
		if (seqIdStr != null) {
			sortId = Integer.parseInt(seqIdStr);
		}
		if (subject1 == null) {
			subject1 = "";
		}
		if (contentNo1 == null) {
			contentNo1 = "";
		}
		if (content1 == null) {
			content1 = "";
		}
		if (attachmentName1 == null) {
			attachmentName1 = "";
		}
		if (fileType1 == null) {
			fileType1 = "";
		}
		if (attachmentDesc1 == null) {
			attachmentDesc1 = "";
		}

		YHPersonFileContentLogic logic = new YHPersonFileContentLogic();

		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();

			boolean flag = false;
			String attachmentId = "";
			String attachmentName = "";

			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(date);
			String separator = File.separator;
			String filePath = YHSysProps.getAttachPath() + separator + "file_folder" + separator + currDate; // YHSysProps.getAttachPath()得到

			Iterator<String> iKeys = fileForm.iterateFileFields();
			while (iKeys.hasNext()) {
				String fieldName = iKeys.next();
				String fileName = fileForm.getFileName(fieldName);
				// System.out.println("fieldName>>" + fieldName);
				if (YHUtility.isNullorEmpty(fileName)) {
					continue;
				}
				// System.out.println("fieldName>>" + fieldName);
				YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
				String rand = emul.getRandom();

				attachmentId = currDate + "_" + rand + ",";
				attachmentName = fileName + "*";
				// returnAttId = currDate + "_" + rand;
				// returnAttName = fileName;

				fileName = rand + "_" + fileName;
				fileForm.saveFile(fieldName, filePath +File.separator+ fileName);
				flag = true;
			}

			if (flag) {

				YHFileContent content = logic.getFileContentInfoById(dbConn, contentId);
				String attIdString = YHUtility.null2Empty(content.getAttachmentId());
				String attNameString = YHUtility.null2Empty(content.getAttachmentName());

				content.setAttachmentId(attIdString.trim() + YHUtility.null2Empty(attachmentId).trim());
				content.setAttachmentName(attNameString.trim() + YHUtility.null2Empty(attachmentName).trim());
				content.setSendTime(YHUtility.parseTimeStamp());
				content.setSubject(subject1);
				content.setContent(content1);
				content.setAttachmentDesc(attachmentDesc1);
				content.setContentNo(contentNo1);
				logic.updateSingleObj(dbConn, content);

			}

			// actionFlag = "edit";

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		String returnString = "/core/funcs/personfolder/edit.jsp?shareFlag=0&seqId=" + sortId + "&contentId=" + contentId;
		if (shareFlag == 1) {
			returnString = "/core/funcs/personfolder/edit.jsp?shareFlag=1&seqId=" + sortId + "&contentId=" + contentId;
		}
		return returnString;
	}

	/**
	 * 新建文件中的"新建附件"
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String createFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String actionFlag = ""; // request.getParameter("actionFlag");
		String seqIdStr = request.getParameter("seqId");
		String contentIdStr = request.getParameter("contentId");
		String fileType = request.getParameter("newFileType");

		String attachmentName = request.getParameter("newAttachmentName");
		String subject = request.getParameter("newSubject");
		String contentNo = request.getParameter("newContentNo");
		String content = request.getParameter("newContent");
		String attDesc = request.getParameter("newAtttDesc");

		int seqId = 0;
		int contentId = 0;
		if (contentIdStr != null) {
			contentId = Integer.parseInt(contentIdStr);
		}
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}

		if (actionFlag == null) {
			actionFlag = "";
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		YHPersonFileContentLogic logic = new YHPersonFileContentLogic();
		YHFileContent fileContent = new YHFileContent();

		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(new Date());

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			String attachmentIds = "";
			String attachmentNames = "";

			String newAttachIdStr = "";
			String newAttachNameStr = "";
			String realPath = request.getRealPath("/");
			if (!"".equals(fileType) && fileType != null && !"".equals(attachmentName) && attachmentName != null) {

				String rand = logic.createFile(fileType, attachmentName, realPath);
				if (!rand.equals("0")) {
					attachmentIds = currDate + "_" + String.valueOf(rand) + ",";
					attachmentNames = attachmentName + "." + fileType + "*";

					newAttachIdStr = currDate + "_" + String.valueOf(rand);
					newAttachNameStr = attachmentName + "." + fileType;
				}

				if (contentId != 0) {
					YHFileContent eitContent = logic.getFileContentInfoById(dbConn, contentId);
					String attIdString = YHUtility.null2Empty(eitContent.getAttachmentId());
					String attNameString = YHUtility.null2Empty(eitContent.getAttachmentName());

					eitContent.setAttachmentId(attIdString + attachmentIds);
					eitContent.setAttachmentName(attNameString + attachmentNames);
					eitContent.setSendTime(YHUtility.parseTimeStamp());
					eitContent.setSubject(subject);
					eitContent.setContent(content);
					eitContent.setAttachmentDesc(attDesc);
					eitContent.setContentNo(contentNo);
					logic.updateSingleObj(dbConn, eitContent);

				} else {

					fileContent.setSortId(seqId);
					fileContent.setAttachmentId(attachmentIds);
					fileContent.setAttachmentName(attachmentNames);
					fileContent.setAttachmentDesc(attDesc);

					fileContent.setContent(content);
					fileContent.setContentNo(contentNo);
					fileContent.setSubject(subject);
					fileContent.setSendTime(YHUtility.parseTimeStamp());
					fileContent.setUserId(String.valueOf(loginUserSeqId));

					fileContent.setCreater(String.valueOf(loginUserSeqId));
					logic.saveSingleObj(dbConn, fileContent);

					// 得到最大id
					fileContent = logic.getMaxSeqId(dbConn);
					contentId = fileContent.getSeqId();

				}

			}

			actionFlag = "edit";

			request.setAttribute("newAttachIdStr", newAttachIdStr);
			request.setAttribute("newAttachNameStr", newAttachNameStr);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功存取数据！");

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/funcs/personfolder/new/newFile.jsp?actionFlag=" + actionFlag + "&contentId=" + contentId;
	}

	/**
	 * 更新浮动菜单的删除文件信息
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateAttchNameById(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String contentIdStr = request.getParameter("contentId");
		String attIdStr = request.getParameter("attId");
		String attNameStr = request.getParameter("attName");

		int contentId = 0;
		if (contentIdStr != null) {
			contentId = Integer.parseInt(contentIdStr);
		}
		if (attIdStr == null) {
			attIdStr = "";
		}
		if (attNameStr == null) {
			attNameStr = "";
		}

		boolean updateFlag = false;

		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();

			YHPersonFileContentLogic logic = new YHPersonFileContentLogic();

			YHFileContent content = logic.getFileContentInfoById(dbConn, contentId);

			String attachIdOld = content.getAttachmentId();
			String attachNameOld = content.getAttachmentName();

			String[] attachIdArrays = attachIdOld.split(",");
			String[] attachNameArrays = attachNameOld.split("\\*");

			String attaId = "";
			String attaName = "";

			for (int i = 0; i < attachIdArrays.length; i++) {
				if (attIdStr.equals(attachIdArrays[i])) {
					continue;
				}
				attaId += attachIdArrays[i] + ",";
				attaName += attachNameArrays[i] + "*";
			}

			content.setAttachmentId(attaId.trim());
			content.setAttachmentName(attaName.trim());

			updateFlag = logic.updateSingle(dbConn, content);

			// boolean updateFlag = logic.delFloatFile(dbConn, attachId, attachName,
			// contentId);

			String data = "";
			if (updateFlag) {
				data = "{updateFlag:\"" + "isDel" + "\"}";

			}

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "删除成功!");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 转发文件到指定人员
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String resendTo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String contentIdStr = request.getParameter("contentIdStr");
		String userIdStrs = request.getParameter("userIdStrs");
		String smsResend = request.getParameter("smsResend");
		String mobileResend = request.getParameter("mobileResend");

		if (contentIdStr == null) {
			contentIdStr = "";
		}
		if (userIdStrs == null) {
			userIdStrs = "";
		}
		if (smsResend == null) {
			smsResend = "";
		}
		if (mobileResend == null) {
			mobileResend = "";
		}

		// // 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();

		String separator = File.separator;
		String filePath = YHSysProps.getAttachPath() + separator + "file_folder" + separator;

		YHPersonFileContentLogic logic = new YHPersonFileContentLogic();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(date);
		YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();

		int sentFlag = 0;

		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();
			YHFileContent userContent = new YHFileContent();

			String loginName = logic.getNameBySeqIdStr(String.valueOf(loginUserSeqId), dbConn);

			String[] seqIdArry = contentIdStr.substring(0, contentIdStr.length() - 1).split(",");
			String[] userIdArry = userIdStrs.split(",");
			if (seqIdArry.length != 0) {
				for (String seqIdStr : seqIdArry) {
					YHFileContent content = logic.getFileContentInfoById(dbConn, Integer.parseInt(seqIdStr));
					if (content!=null) {
						String newSubject = "";
						String subject = YHUtility.null2Empty(content.getSubject())+ "由(" + loginName + "转发)";
						boolean haveFile = logic.isExistFile(dbConn, 0, subject);
						if (haveFile) {
							StringBuffer buffer = new StringBuffer();
							logic.copyExistFile(dbConn, buffer, 0, subject);
							newSubject = buffer.toString().trim();
						}else {
							newSubject = subject;
						}
						int maxSeqId = 0;
						// String attIdStr=content.getAttachmentId();
						// String attName=content.getAttachmentName();
						if (userIdArry.length != 0) {
							for (String userIdStr : userIdArry) {
								String[] attIdArray = {};
								String[] attNameArray = {};
								String attachmentId = YHUtility.null2Empty(content.getAttachmentId());
								String attachmentName = YHUtility.null2Empty(content.getAttachmentName());
								if (!"".equals(attachmentId.trim()) && !"".equals(attachmentName.trim()) && attachmentId != null && attachmentName != null) {
									attIdArray = attachmentId.trim().split(",");
									attNameArray = attachmentName.trim().split("\\*");
								}
								String attachmentIdStr = "";
								String attachmentNameStr = "";

								for (int i = 0; i < attIdArray.length; i++) {
									Map<String, String> map = logic.getFileName(attIdArray[i], attNameArray[i]);
									if (map.size() != 0) {
										Set<String> set = map.keySet();
										for (String keySet : set) {
											String rand = emut.getRandom(); // 171c3e9cdc2b95f50670df252e89ab19
											String key = keySet; // 1005_22dfb4211c393766b3be53e04e07489c
											String keyValue = map.get(keySet); // 22222111.txt
											String attaIdStr = logic.getAttaId(keySet); // 0055555011
											String newAttaName = rand + "_" + keyValue; // 171c3e9cdc2b95f50670df252e89ab19_22222111.txt
											String fileNameValue = attaIdStr + "_" + keyValue; // 22dfb4211c393766b3be53e04e07489c_22222111.txt
											String fileFolder = logic.getFilePathFolder(key); // 1005
											// String newFilePath = filePath + fileFolder;
											String fileName = fileNameValue;
											String oldFileName = attaIdStr + "." + keyValue;

											File file = new File(filePath + fileFolder + "/" + fileName);
											File oldFile = new File(filePath + fileFolder + "/" + oldFileName);

											boolean isHave = false;

											if (file.exists()) {
												YHFileUtility.copyFile(file.getAbsolutePath(), filePath + currDate + File.separator + newAttaName);
												isHave = true;
											} else if (oldFile.exists()) {
												YHFileUtility.copyFile(oldFile.getAbsolutePath(), filePath + currDate + File.separator + newAttaName);
												isHave = true;
											}
											if (isHave) {
												attachmentIdStr += currDate + "_" + rand + ",";
												attachmentNameStr += keyValue + "*";
											}
										}
									}
								}
								userContent.setAttachmentId(attachmentIdStr.trim());
								userContent.setAttachmentName(attachmentNameStr.trim());
								userContent.setSortId(0);
								userContent.setSubject(newSubject);
								userContent.setContent(content.getContent());
								userContent.setSendTime(YHUtility.parseTimeStamp());
								userContent.setAttachmentDesc(content.getAttachmentDesc());
								userContent.setAttachmentDesc(content.getAttachmentDesc());
								userContent.setUserId(userIdStr);
								userContent.setNewPerson(String.valueOf(loginUserSeqId));
								userContent.setReaders(content.getReaders());
								logic.saveSingleObj(dbConn, userContent);

								YHFileContent fileContent = logic.getMaxSeqId(dbConn);
								maxSeqId = fileContent.getSeqId();
								sentFlag = 1;
							}
							String smsContent = loginName + "给你转发文件:" + YHUtility.null2Empty(newSubject);
							// 短信提醒
							if ("smsResend".equals(smsResend.trim())) {

								YHPersonFolderLogic filFolderLogic = new YHPersonFolderLogic();
								YHFileContent newContent = logic.getFileContentInfoById(dbConn, maxSeqId);

								YHSmsBack sms = new YHSmsBack();
								String remindUrl = "/core/funcs/personfolder/personRead.jsp?&contentId=" + maxSeqId + "&sortId=" + newContent.getSortId()
										+ "&resendFlag=1&openFlag=1";
								sms.setFromId(loginUserSeqId);
								sms.setToId(userIdStrs);
								sms.setContent(smsContent);
								sms.setSendDate(YHUtility.parseTimeStamp());
								sms.setSmsType(YHLogConst.PERSONAL_FOLD);
								sms.setRemindUrl(remindUrl);
								YHSmsUtil.smsBack(dbConn, sms);
							}
							// 手机短信提醒
							if ("smsMobile".equals(mobileResend.trim())) {
								YHMobileSms2Logic mobileSms = new YHMobileSms2Logic();
								mobileSms.remindByMobileSms(dbConn, userIdStrs, loginUserSeqId, smsContent, null);
							}
						}
					}
				}
			}
			String data = "{sentFlag:\"" + sentFlag + "\"}";
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 个人文件柜转存
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String transferFolder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("checkId");
		String attachId = request.getParameter("attachId");
		String attachName = request.getParameter("attachName");
		String subject = request.getParameter("subject");
		String module = request.getParameter("module");
		int seqId = 0;
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}
		if (subject == null) {
			subject = "";
		}
		if (module == null) {
			module = "";
		}
		YHPersonFileContentLogic logic = new YHPersonFileContentLogic();
		String separator = File.separator;
		String filePath = YHSysProps.getAttachPath() + separator + module + separator;
		String folderPath = YHSysProps.getAttachPath() + separator + "file_folder";

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();

			boolean flag = logic.transferFolder(dbConn, loginUserSeqId, seqId, attachId, attachName, subject, filePath.trim(), folderPath);
			if (flag) {
				request.setAttribute(YHActionKeys.RET_MSRG, "文件转存成功！");
			} else {
				request.setAttribute(YHActionKeys.RET_MSRG, "文件转存失败！");
			}

			String data = "{flag:\"" + flag + "\"}";
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
	 * 判断新建文件名是否已存在
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String checkSubjectName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		String subjectName = request.getParameter("subject");
		String contentIdStr = request.getParameter("contentId");

		int seqId = 0; // 文件夹的id
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}

		int contentId = 0;
		if (contentIdStr != null) {
			contentId = Integer.parseInt(contentIdStr);
		}

		if (subjectName == null) {
			subjectName = "";
		}

		YHPersonFileContentLogic logic = new YHPersonFileContentLogic();

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();
			String data = logic.checkSubjectName(dbConn, loginUserSeqId, seqId, contentId, subjectName);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 判断编辑文件名是否已存在
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String checkEditSubjectName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		String subjectName = request.getParameter("subject");
		String contentIdStr = request.getParameter("contentId");

		int seqId = 0; // 文件夹的id
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}

		int contentId = 0;
		if (contentIdStr != null) {
			contentId = Integer.parseInt(contentIdStr);
		}

		if (subjectName == null) {
			subjectName = "";
		}

		YHPersonFileContentLogic logic = new YHPersonFileContentLogic();

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();
			String data = logic.checkEditSubjectName(dbConn, loginUserSeqId, seqId, contentId, subjectName);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 返回下载文件所需要的值
	 * 
	 * 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String downFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String contentIdStr = request.getParameter("contentId");
		if (contentIdStr == null) {
			contentIdStr = "";
		}

		YHPersonFileContentLogic logic = new YHPersonFileContentLogic();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			String data = logic.getDownFileInfo(dbConn, contentIdStr.substring(0, contentIdStr.length() - 1));

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 重命名附件
	 * 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String renameFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String contentIdStr = request.getParameter("contentId");
		int contentId = 0;
		if (contentIdStr != null) {
			contentId = Integer.parseInt(contentIdStr);
		}

		String attachName = request.getParameter("attachName");
		if (attachName == null) {
			attachName = "";
		}
		String attachId = request.getParameter("attachId");
		if (attachId == null) {
			attachId = "";
		}
		String module = request.getParameter("module");
		if (module == null) {
			module = "";
		}

		String renameStr = request.getParameter("newName");
		if (renameStr == null) {
			renameStr = "";
		}

		// YHFileContentLogic logic = new YHFileContentLogic();

		YHPersonFileContentLogic logic = new YHPersonFileContentLogic();

		String filePath = YHSysProps.getAttachPath() + "/" + module;

		int successFlag = 0;

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			// 更改文件名
			boolean success = logic.reNameOndisk(attachName, attachId, renameStr, filePath);

			// 对数据库操作
			if (success) {
				successFlag = 1;
				logic.updateAttachName(dbConn, contentId, attachName, attachId, renameStr, filePath);
			}

			String data = "{successFlag:\"" + successFlag + "\"}";

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功更新数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

}
