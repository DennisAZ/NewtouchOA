package yh.subsys.oa.confidentialFile.act;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.office.ntko.logic.YHNtkoLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.confidentialFile.data.YHConfidentialContent;
import yh.subsys.oa.confidentialFile.logic.YHConfidentialContentLogic;

public class YHConfidentialContentAct {
	private YHConfidentialContentLogic logic = new YHConfidentialContentLogic();
	public static final String attachmentFolder = "confidential";

	/**
	 *	获取文件夹下的文列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getContentFileListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String seqId = request.getParameter("seqId");
			String data = this.logic.getContentFileListJson(dbConn, request.getParameterMap(), seqId, person);
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
	 * 新建文件信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addNewFileInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		String type = request.getParameter("fileType");
		String subject = request.getParameter("subject");
		String contentNo = request.getParameter("contentNo");
		String content = request.getParameter("content");
		String attachmentName = request.getParameter("attachmentName");
		String attachmentDesc = request.getParameter("attachmentDesc");
		String contentIdStr = request.getParameter("contentId");
		String smsPerson = request.getParameter("smsPerson");
		String mobileSmsPerson = request.getParameter("mobileSmsPerson");

		String folderPath = request.getParameter("folderPath");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

			String ipStr = request.getRemoteAddr();
			String realPath = request.getRealPath("/");

			// 保存从文件柜、网络硬盘选择附件
			YHSelAttachUtil sel = new YHSelAttachUtil(request, this.attachmentFolder);
			String attIdStr = sel.getAttachIdToString(",");
			String attNameStr = sel.getAttachNameToString("*");

			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("seqIdStr", seqIdStr);
			map.put("type", type);
			map.put("subject", subject);
			map.put("contentNo", contentNo);
			map.put("content", content);
			map.put("attachmentName", attachmentName);
			map.put("attachmentDesc", attachmentDesc);
			map.put("contentIdStr", contentIdStr);
			map.put("smsPerson", smsPerson);
			map.put("mobileSmsPerson", mobileSmsPerson);
			map.put("folderPath", folderPath);

			map.put("attIdStr", attIdStr);
			map.put("attNameStr", attNameStr);
			map.put("ipStr", ipStr);
			map.put("realPath", realPath);
			this.logic.addNewFileInfoLogic(dbConn, map, person);
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
	 * 新建文件单个文件上传
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String newFileSingleUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);

		String seqIdStr = request.getParameter("seqId"); // 文件夹的seqId
		int sortId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			sortId = Integer.parseInt(seqIdStr);
		}
		int contentId = 0;
		String contentStr = request.getParameter("contentId");
		if (!YHUtility.isNullorEmpty(contentStr)) {
			contentId = Integer.parseInt(contentStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

			String actionFlag = (String) fileForm.getParameter("actionFlag");
			String retrunFlag = fileForm.getParameter("retrunFlag"); // 返回页面returnFolderFlag
			String returnFolderFlag = fileForm.getParameter("returnFolderFlag");

			if (YHUtility.isNullorEmpty(returnFolderFlag)) {
				returnFolderFlag = "";
			}
			if (!YHUtility.isNullorEmpty(returnFolderFlag)) {
				retrunFlag = returnFolderFlag;
			}
			if (actionFlag == null || "".equals(actionFlag)) {
				actionFlag = "new";
			}
			Map<Object, Object> map = this.logic.fileUploadLogic(fileForm, attachmentFolder);
			map.put("sortId", sortId);
			map.put("contentId", contentId);
			contentId = this.logic.setNewFileValueLogic(dbConn, fileForm, person, map);
			actionFlag = "edit";

			String contextPath = request.getContextPath();
			if ("returnNew".equals(retrunFlag.trim())) {
				response.sendRedirect(contextPath + "/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/new/newFile.jsp?actionFlag=" + actionFlag
						+ "&contentId=" + contentId + "&seqId=" + sortId);
			} else if ("returnEdit".equals(retrunFlag.trim())) {
				response.sendRedirect(contextPath + "/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/edit.jsp?seqId=" + sortId + "&contentId=" + contentId);
			} else if ("returnQueryEdit".equals(retrunFlag.trim())) {
				response.sendRedirect(contextPath + "/subsys/oa/confidentialFile/showConfidentialFile/queryFile/edit.jsp?seqId=" + sortId + "&contentId=" + contentId);
			}else if ("returnFolder".equals(retrunFlag.trim())) {
				response.sendRedirect(contextPath + "/core/funcs/filefolder/folder.jsp?seqId=" + sortId);
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return null;
	}

	/**
	 * 根据contentId获得文件夹下的内容信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getFileContentInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String contentIdStr = request.getParameter("contentId");
		int contentId = 0;
		if (contentIdStr != null && !"".equals(contentIdStr)) {
			contentId = Integer.parseInt(contentIdStr);
		}

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHConfidentialContent content = this.logic.getFileContentByIdLogic(dbConn, contentId);
			if (content == null) {
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				request.setAttribute(YHActionKeys.RET_MSRG, "文件信息不存在");
				return "/core/inc/rtjson.jsp";
			}
			StringBuffer data = YHFOM.toJson(content);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "取出文件信息成功");
			request.setAttribute(YHActionKeys.RET_DATA, data.toString());
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
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
		request.setCharacterEncoding("UTF-8");
		String seqIdStr = request.getParameter("seqId");
		String contentIdStr = request.getParameter("contentId");
		String fileType = request.getParameter("newFileType");
		String attachmentName = request.getParameter("newAttachmentName");
		String subject = request.getParameter("newSubject");
		String contentNo = request.getParameter("newContentNo");
		String content = request.getParameter("newContent");
		String attDesc = request.getParameter("newAtttDesc");

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String realPath = request.getRealPath("/");
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("seqIdStr", seqIdStr);
			map.put("contentIdStr", contentIdStr);
			map.put("fileType", fileType);
			map.put("attachmentName", attachmentName);
			map.put("subject", subject);
			map.put("contentNo", contentNo);
			map.put("content", content);
			map.put("attDesc", attDesc);
			map.put("realPath", realPath);

			Map<Object, Object> returnMap = this.logic.setCreateFileLogic(dbConn, person, map);
			int reContentId = (Integer) returnMap.get("contentId");
			String actionFlag = (String) returnMap.get("actionFlag");
			String newAttachIdStr = (String) returnMap.get("newAttachIdStr");
			String newAttachNameStr = (String) returnMap.get("newAttachNameStr");

			request.setAttribute("newAttachIdStr", newAttachIdStr);
			request.setAttribute("newAttachNameStr", newAttachNameStr);
			return "/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/new/newFile.jsp?actionFlag=" + actionFlag + "&contentId=" + reContentId;
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
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
		String attachId = request.getParameter("attachId");
		String attachName = request.getParameter("attachName");
		String contentIdStr = request.getParameter("contentId");
		int contentId = 0;
		if (!YHUtility.isNullorEmpty(contentIdStr)) {
			contentId = Integer.parseInt(contentIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();
			boolean updateFlag = this.logic.delFloatFile(dbConn, attachId, attachName, contentId);
			String isDel = "";
			if (updateFlag) {
				isDel = "isDel";
			}
			String data = "{updateFlag:\"" + isDel + "\"}";
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
		String filePath = YHSysProps.getAttachPath() + separator + this.attachmentFolder + separator; // YHSysProps.getAttachPath()得到
		String recyclePath = YHSysProps.getAttachPath() + separator + "recycle" + separator + this.attachmentFolder; // 文件回收站的路径

		String recycle = YHSysProps.getString("$MYOA_IS_RECYCLE");
		if (recycle == null) {
			recycle = "";
		}
		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		// 获取ip
		String ipStr = request.getRemoteAddr();
		if (seqIdStr.trim().endsWith(",")) {
			seqIdStr = seqIdStr.trim().substring(0, seqIdStr.trim().length() - 1);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();
			this.logic.delFile(dbConn, seqIdStr, filePath, loginUserSeqId, ipStr, recycle, recyclePath);
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
	 * 批量文件上传
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String uploadBatchFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.uploadFileLogic(dbConn, request);
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
	 *复制文件信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String copyFileByIds(HttpServletRequest request, HttpServletResponse response) {
		String seqIdStrs = request.getParameter("seqIdStrs");
		String action = request.getParameter("action");
		String folderSeqId = request.getParameter("folderSeqId");
		if (seqIdStrs.endsWith(",")) {
			seqIdStrs = seqIdStrs.substring(0, seqIdStrs.length() - 1);
		}
		try {
			Cookie seqIdStrsCookie = new Cookie("confidentialContentId", seqIdStrs);
			Cookie actionCookie = new Cookie("confidentialAction", action);
			Cookie folderSeqIdCookie = new Cookie("folderSeqIdCookie", folderSeqId);
			response.addCookie(seqIdStrsCookie);
			response.addCookie(actionCookie);
			response.addCookie(folderSeqIdCookie);
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
		String sortIdStr = (String) request.getParameter("sortId");
		String separator = File.separator;
		String filePath = YHSysProps.getAttachPath() + separator + attachmentFolder + separator;

		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();
			String seqIdStrs = this.logic.getCookieValue(request, "confidentialContentId");
			String action = this.logic.getCookieValue(request, "confidentialAction");
			boolean optFlag = false;
			if ("copyFile".equals(action)) {
				this.logic.copyFile(dbConn, seqIdStrs, sortIdStr, filePath);
				optFlag = true;
			} else if ("cutFile".equals(action)) {
				this.logic.cutFile(dbConn, seqIdStrs, sortIdStr, filePath);
				optFlag = true;
			}
			if (optFlag) {
				Cookie seqIdStrsCookie = this.logic.getCookie(request, "confidentialContentId");
				Cookie actionCookie = this.logic.getCookie(request, "confidentialAction");
				Cookie folderSeqIdCookie = this.logic.getCookie(request, "folderSeqIdCookie");
				if (seqIdStrsCookie != null) {
					seqIdStrsCookie.setMaxAge(0);
					response.addCookie(seqIdStrsCookie);
				}
				if (actionCookie != null) {
					actionCookie.setMaxAge(0);
					response.addCookie(actionCookie);
				}
				if (folderSeqIdCookie != null) {
					folderSeqIdCookie.setMaxAge(0);
					response.addCookie(folderSeqIdCookie);
				}
			}
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
	 * 批量下载
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String batchDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String contentIdStr = request.getParameter("contentIdStr");
		String seqIdStr = request.getParameter("sortId");
		if (contentIdStr == null) {
			contentIdStr = "-1";
		}
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		OutputStream ops = null;
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String module = request.getParameter("module") == null ? attachmentFolder : request.getParameter("module");
			String name = request.getParameter("name");
			if (name == null || "".equals(name)) {
				name = "附件打包下载";
			}
			String fileName = URLEncoder.encode(name + ".zip", "UTF-8");
			fileName = fileName.replaceAll("\\+", "%20");
			response.setHeader("Cache-control", "private");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
			ops = response.getOutputStream();
			YHNtkoLogic nl = new YHNtkoLogic();
			Map<String, InputStream> map = this.logic.toZipInfoMapFile(dbConn, seqId, contentIdStr, module, loginUser);
			nl.zip(map, ops);
			ops.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			ops.close();
		}
		return null;
	}

	/**
	 * 更新文件信息(编辑)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateFileInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("sortId");
		String type = request.getParameter("fileType");
		String subject = request.getParameter("subject");
		String contentNo = request.getParameter("contentNo");
		String content = request.getParameter("content");
		String attachmentName = request.getParameter("attachmentName");
		String attachmentDesc = request.getParameter("attachmentDesc");
		String contentIdStr = request.getParameter("contentId");
		String smsPerson = request.getParameter("smsPerson");
		String mobileSmsPerson = request.getParameter("mobileSmsPerson");

		String folderPath = request.getParameter("folderPath");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

			String ipStr = request.getRemoteAddr();
			String realPath = request.getRealPath("/");
			// 保存从文件柜、网络硬盘选择附件
			YHSelAttachUtil sel = new YHSelAttachUtil(request, attachmentFolder);
			String attIdStr = sel.getAttachIdToString(",");
			String attNameStr = sel.getAttachNameToString("*");
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("seqIdStr", seqIdStr);
			map.put("type", type);
			map.put("subject", subject);
			map.put("contentNo", contentNo);
			map.put("content", content);
			map.put("attachmentName", attachmentName);
			map.put("attachmentDesc", attachmentDesc);
			map.put("contentIdStr", contentIdStr);
			map.put("smsPerson", smsPerson);
			map.put("mobileSmsPerson", mobileSmsPerson);
			map.put("folderPath", folderPath);

			map.put("attIdStr", attIdStr);
			map.put("attNameStr", attNameStr);
			map.put("ipStr", ipStr);
			map.put("realPath", realPath);
			this.logic.updateFileInfoLogic(dbConn,map,person);
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
	 * 编辑文件中的"新建附件"
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String newAttrachFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		String contentIdStr = request.getParameter("contentId");
		String attachmentName = request.getParameter("newAttachmentName");
		String fileType = request.getParameter("newFileType");
		String subject = request.getParameter("newSubject");
		String contentNo = request.getParameter("newContentNo");
		String content = request.getParameter("newContent");
		String atttDesc = request.getParameter("newAtttDesc");
		String retrunFlag = request.getParameter("retrunFlag");
		int contentId = 0;
		if (!YHUtility.isNullorEmpty(contentIdStr)) {
			contentId = Integer.parseInt(contentIdStr);
		}
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		if (YHUtility.isNullorEmpty(retrunFlag)) {
			retrunFlag = "";
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String realPath = request.getRealPath("/");
			
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("seqId", seqId);
			map.put("contentId", contentId);
			map.put("attachmentName", attachmentName);
			map.put("fileType", fileType);
			map.put("subject", subject);
			map.put("contentNo", contentNo);
			map.put("content", content);
			map.put("atttDesc", atttDesc);
			map.put("realPath", realPath);
			Map<Object, Object> objMap = this.logic.newAttrachFileLogic(dbConn,map);
			String newAttachIdStr = (String)objMap.get("newAttachIdStr");
			String newAttachNameStr = (String)objMap.get("newAttachNameStr");
			request.setAttribute("newAttachIdStr", newAttachIdStr);
			request.setAttribute("newAttachNameStr", newAttachNameStr);
		} catch (Exception e) {
			throw e;
		}
		String returnStr = "";
		if ("returnQueryEdit".equals(retrunFlag)) {
			returnStr = "/subsys/oa/confidentialFile/showConfidentialFile/queryFile/edit.jsp?seqId=" + seqId + "&contentId=" + contentId;
		}else {
			returnStr = "/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/edit.jsp?seqId=" + seqId + "&contentId=" + contentId;
		}
		return returnStr;
	}
	
	/**
	 * 文件报送
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String fileSend(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String contentIdStr = request.getParameter("contentId");
		if (contentIdStr == null) {
			contentIdStr = "";
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			if (contentIdStr.endsWith(",")) {
				contentIdStr = contentIdStr.substring(0, contentIdStr.length() - 1);
			}
			this.logic.updateFileSendLogic(dbConn,contentIdStr,person);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "文件报送成功");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 文件撤回
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getBack(HttpServletRequest request, HttpServletResponse response) throws Exception{
	  String contentIdStr = request.getParameter("contentId");
	  if (contentIdStr == null) {
	    contentIdStr = "";
	  }
	  Connection dbConn = null;
	  try {
	    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	    dbConn = requestDbConn.getSysDbConn();
	    YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
	    if (contentIdStr.endsWith(",")) {
	      contentIdStr = contentIdStr.substring(0, contentIdStr.length() - 1);
	    }
	    this.logic.updateFileSendLogic(dbConn, contentIdStr, person, "0");
	    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	    request.setAttribute(YHActionKeys.RET_MSRG, "文件报送成功");
	  } catch (Exception e) {
	    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
	    throw e;
	  }
	  return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 查询文件列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryFileByIdJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("subject", request.getParameter("subject"));
		map.put("contentNo", request.getParameter("contentNo"));
		map.put("key1", request.getParameter("key1"));
		map.put("key2", request.getParameter("key2"));
		map.put("key3", request.getParameter("key3"));

		map.put("attachmentDesc", request.getParameter("attachmentDesc"));
		map.put("attachmentName", request.getParameter("attachmentName"));
		map.put("attachmentData", request.getParameter("attachmentData"));
		map.put("sendTimeMin", request.getParameter("sendTimeMin"));
		map.put("sendTimeMax", request.getParameter("sendTimeMax"));
		map.put("seqId", request.getParameter("seqId"));

		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String filePath = YHSysProps.getAttachPath() + File.separator + this.attachmentFolder;

			String data = this.logic.queryFileByIdJsonLogic(dbConn, request.getParameterMap(), person, map,filePath);
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
	 * 获取未报送文件列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getFileNotSendListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String fileSendFlag = request.getParameter("fileSend");
			String data = this.logic.getFileNotSendListJson(dbConn, request.getParameterMap(),person,fileSendFlag);
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
	 * 全局搜索文件列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getGlobalFileListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("subject", request.getParameter("subject"));
		map.put("contentNo", request.getParameter("contentNo"));
		map.put("key1", request.getParameter("key1"));
		map.put("key2", request.getParameter("key2"));
		map.put("key3", request.getParameter("key3"));

		map.put("attachmentDesc", request.getParameter("attachmentDesc"));
		map.put("attachmentName", request.getParameter("attachmentName"));
		map.put("attachmentData", request.getParameter("attachmentData"));
		map.put("sendTimeMin", request.getParameter("sendTimeMin"));
		map.put("sendTimeMax", request.getParameter("sendTimeMax"));
		map.put("seqId", request.getParameter("seqId"));

		Connection dbConn;
		try {

			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String filePath = YHSysProps.getAttachPath() + File.separator + YHConfidentialContentAct.attachmentFolder;
			
			String data = this.logic.getGlobalFileJsonLogic(dbConn, request.getParameterMap(), person, map,filePath);
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
	 * 获取创建人名称
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getCreaterNameById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String createrId = request.getParameter("createId");
		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getCreaterNameByIdLogic(dbConn,createrId);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "数据取出成功");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	
	
	
	
	
}
