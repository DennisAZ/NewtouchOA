package yh.subsys.oa.confidentialFile.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.confidentialFile.data.YHConfidentialSort;
import yh.subsys.oa.confidentialFile.logic.YHConfidentialContentLogic;
import yh.subsys.oa.confidentialFile.logic.YHShowConfidentialSortLogic;

public class YHShowConfidentialSortAct {
	private static Logger log = Logger.getLogger(YHShowConfidentialSortAct.class);
	private YHShowConfidentialSortLogic logic = new YHShowConfidentialSortLogic();

	/**
	 * 获取文件夹树形结构信息，考虑是否有权限，有权限才能显示
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPrivTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idStr = request.getParameter("id");
		String sortIdStr = request.getParameter("seqId");

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getPrivTreeLogic(dbConn, loginUser, idStr, sortIdStr);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 通过id递归获取文件夹名
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSortNameById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getFolderPathByIdLogic(dbConn, seqId);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "返回数据成功");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 添加子文件夹
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addSubFolderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sortParent = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			Map<String, String[]> map = request.getParameterMap();
			YHConfidentialSort fileSort = (YHConfidentialSort) YHFOM.build(map, YHConfidentialSort.class, "");
			String folderName = YHUtility.null2Empty(fileSort.getSortName());
			String data = this.logic.addSubFolderLogic(dbConn, fileSort, sortParent, folderName);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 通过id获取该文件夹的“所有者权限”
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPrivteById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqId = request.getParameter("seqId");
		if (YHUtility.isNullorEmpty(seqId)) {
			seqId = "0";
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getOwnerPrivLogic(dbConn, person, seqId);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 * 通过id获取该文件夹的“所有者权限”与“访问权限”
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getVisitOrOwnerPrivteById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqId = request.getParameter("seqId");
		if (YHUtility.isNullorEmpty(seqId)) {
			seqId = "0";
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getVisitOrOwnerPrivLogic(dbConn, person, seqId);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 根据seqId获取YHConfidentialSort对象
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getFileSortInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHConfidentialSort confidentialSort = this.logic.getfileSortById(dbConn, seqId);
			if (confidentialSort == null) {
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				request.setAttribute(YHActionKeys.RET_MSRG, "会议设备信息不存在");
				return "/core/inc/rtjson.jsp";
			}
			StringBuffer data = YHFOM.toJson(confidentialSort);
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
	 * 更新编辑子文件夹信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateFileSortInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sortParent = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			Map<String, String[]> map = request.getParameterMap();
			YHConfidentialSort fileSort = (YHConfidentialSort) YHFOM.build(map, YHConfidentialSort.class, "");
			String folderName = YHUtility.null2Empty(fileSort.getSortName());
			String data = this.logic.updateSubFolderLogic(dbConn, fileSort, sortParent, folderName);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 复制文件夹
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String copyFolderById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqId = request.getParameter("folderId");
		String action = request.getParameter("action");
		try {
			Cookie sortSeqIdCookie = new Cookie("folderSeqIdCookie", seqId);
			Cookie sortActionCookie = new Cookie("confidentialAction", action);
			response.addCookie(sortSeqIdCookie);
			response.addCookie(sortActionCookie);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功设置数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 粘贴文件夹
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String pasteFolder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sortParentStr = request.getParameter("sortParent"); // 点击粘贴时的文件夹seqId作为父级id
		int sortParent = 0;
		if (!YHUtility.isNullorEmpty(sortParentStr)) {
			sortParent = Integer.parseInt(sortParentStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHConfidentialContentLogic contentLogic = new YHConfidentialContentLogic();

			String seqIdStr = contentLogic.getCookieValue(request, "folderSeqIdCookie");
			String action = contentLogic.getCookieValue(request, "confidentialAction");
			int seqId = 0;
			if (!YHUtility.isNullorEmpty(seqIdStr)) {
				seqId = Integer.parseInt(seqIdStr);
			}
			YHConfidentialSort fileSort3 = this.logic.getfileSortById(dbConn, seqId); // 获取补复制文件夹信息
			String data = "";
			int nodeId = 0;
			int isHaveChild = 0;
			String sortName = "";
			if (fileSort3 != null) {
				sortName = YHUtility.null2Empty(fileSort3.getSortName());
				if ("cut".equals(action)) {
					this.logic.updateFolderInfoById(dbConn, sortParent, seqId);
					isHaveChild = this.logic.isHaveChild(dbConn, seqId);
					nodeId = fileSort3.getSeqId();
				} else if ("copy".equals(action)) {
					// 级联查询本文件夹及其所有的子文件夹信息
					List listTemp = new ArrayList();
					YHConfidentialSort maxFileSort = this.logic.getSortMaxSeqId(dbConn);
					int maxSeqId = maxFileSort.getSeqId();
					List folderList = this.logic.getAllFolderList(dbConn, seqId, sortParent, listTemp, maxSeqId);
					isHaveChild = this.logic.isHaveChild(dbConn, seqId);
					nodeId = (Integer) folderList.get(0);
				}
				Cookie sortCookie = contentLogic.getCookie(request, "folderSeqIdCookie");
				Cookie actionCookie = contentLogic.getCookie(request, "confidentialAction");
				if (sortCookie != null) {
					sortCookie.setMaxAge(0);
					response.addCookie(sortCookie);
				}
				if (actionCookie != null) {
					actionCookie.setMaxAge(0);
					response.addCookie(actionCookie);
				}
			}
			data = "[{nodeId:\"" + nodeId + "\",isHaveChild:\"" + isHaveChild + "\",sortName:\"" + YHUtility.encodeSpecial(sortName) + "\",seqId:\""
					+ seqId + "\",action:\"" + action + "\" }]";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功粘贴数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 通过id获取该文件夹的所有权限信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getAllPrivteById(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getAllPrivteByIdLogic(dbConn,person,seqId);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 取得文件夹名
	 * @return
	 * @throws Exception
	 */
	public String getFolderName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getFolderNameLogic(dbConn, seqId);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	

}
