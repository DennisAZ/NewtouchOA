package yh.core.funcs.filefolder.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.filefolder.data.YHFileSort;
import yh.core.funcs.filefolder.logic.YHFileSortLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHFileSortAct {

	/**
	 * 新建文件夹
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addFileSortInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHFileSortLogic fileSortLogic = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			fileSortLogic = new YHFileSortLogic();
			YHFileSort fileSort = (YHFileSort) YHFOM.build(request.getParameterMap());
			fileSortLogic.saveFileSortInfo(dbConn, fileSort);
			dbConn.close();
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
	 * 取得文件夹所有信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getFileSortInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHFileSortLogic fileSortLogic = new YHFileSortLogic();
		StringBuffer sb = new StringBuffer("[");
		List<YHFileSort> fileSorts = new ArrayList<YHFileSort>();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			fileSorts = fileSortLogic.getFileSortsInfo(dbConn);
			for (int i = 0; i < fileSorts.size(); i++) {
				YHFileSort fileSort = fileSorts.get(i);
				String sortNo = fileSort.getSortNo() == null ? "" : fileSort.getSortNo();
				sb.append("{");
				sb.append("sqlId:\"" + fileSort.getSeqId() + "\"");
				sb.append(",sortParent:\"" + fileSort.getSortParent() + "\"");
				sb.append(",sortNo:\"" + sortNo + "\"");
				String fileSortNameStr = fileSort.getSortName() == null ? "" : fileSort.getSortName();
				fileSortNameStr = fileSortNameStr.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
				sb.append(",sortName:\"" + fileSortNameStr + "\"");
				sb.append(",sortType:\"" + fileSort.getSortType() + "\"");
				sb.append(",deptId:\"" + fileSort.getDeptId() + "\"");
				sb.append(",userId:\"" + fileSort.getUserId() + "\"");
				sb.append(",newUser:\"" + fileSort.getNewUser() + "\"");
				sb.append(",manageUser:\"" + fileSort.getManageUser() + "\"");
				sb.append(",DownUser:\"" + fileSort.getDownUser() + "\"");
				sb.append(",shareUser:\"" + fileSort.getShareUser() + "\"");
				sb.append(",owner:\"" + fileSort.getOwner() + "\"");
				sb.append("},");
			}
			sb.deleteCharAt(sb.length() - 1);
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
	 * 通过id获取信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getFileSortInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHFileSortLogic fileSortLogic = new YHFileSortLogic();
		StringBuffer sb = new StringBuffer("[");
		String seqId = request.getParameter("seqId");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHFileSort fileSort = fileSortLogic.getFileSortInfoById(dbConn, seqId);
			String sortNo = fileSort.getSortNo() == null ? "" : fileSort.getSortNo();
			String sortName = fileSort.getSortName();
			sortName = sortName.replaceAll("[\n-\r]", "<br>");
			sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
			sortName = sortName.replace("\"", "\\\"");

			sb.append("{");
			sb.append("sqlId:\"" + fileSort.getSeqId() + "\"");
			sb.append(",sortParent:\"" + fileSort.getSortParent() + "\"");
			sb.append(",sortNo:\"" + sortNo + "\"");
			sb.append(",sortName:\"" + sortName + "\"");
			sb.append(",sortType:\"" + fileSort.getSortType() + "\"");
			sb.append(",deptId:\"" + fileSort.getDeptId() + "\"");
			sb.append(",userId:\"" + fileSort.getUserId() + "\"");
			sb.append(",newUser:\"" + fileSort.getNewUser() + "\"");
			sb.append(",manageUser:\"" + fileSort.getManageUser() + "\"");
			sb.append(",DownUser:\"" + fileSort.getDownUser() + "\"");
			sb.append(",shareUser:\"" + fileSort.getShareUser() + "\"");
			sb.append(",owner:\"" + fileSort.getOwner() + "\"");
			sb.append("},");
			sb.append("]");
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
			dbConn.close();
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	public String updateFileSortInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHFileSortLogic fileSortLogicc = new YHFileSortLogic();
		int seqId = Integer.parseInt(request.getParameter("seqId"));
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHFileSort fileSort = (YHFileSort) YHFOM.build(request.getParameterMap());
			fileSort.setSeqId(seqId);
			fileSortLogicc.updateFileSortInfoById(dbConn, fileSort);
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
	 * 递归删除文件夹及下的所有文件信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delFileSortInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int seqId = Integer.parseInt(request.getParameter("seqId"));
		YHFileSort fileSort = null;
		YHFileSortLogic fileSortLogic = new YHFileSortLogic();
		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		// 获取ip
		String ipStr = request.getRemoteAddr();
		YHORM orm = new YHORM();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHFileSort addLogFileSort = (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, seqId);
		  String nameStr = "";
      if (addLogFileSort!=null) {
        nameStr = YHUtility.null2Empty(addLogFileSort.getSortName());
      }
		      
			fileSort = new YHFileSort();
			fileSort.setSeqId(seqId);
			fileSortLogic.delFileSortInfoById(dbConn, fileSort, loginUserSeqId, ipStr);
		// 写入系统日志
      String remark = "删除目录，名称： " + nameStr ;
      YHSysLogLogic.addSysLog(dbConn, YHLogConst.FILE_FOLDER, remark, loginUserSeqId, request.getRemoteAddr());
			
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
	 * 签阅情况
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String showReader(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sortIdStr = request.getParameter("sortId");
		String contentIdStr = request.getParameter("contentId");
		int sortId = 0;
		int contentId = 0;
		if (sortIdStr != null) {
			sortId = Integer.parseInt(sortIdStr);
		}
		if (contentIdStr != null) {
			contentId = Integer.parseInt(contentIdStr);
		}
		YHFileSortLogic logic = new YHFileSortLogic();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHFileSort fileSort = logic.getFileSortInfoById(dbConn, String.valueOf(sortId));
			String userIdStrs = "";
			if (fileSort != null) {
				userIdStrs = fileSort.getUserId();
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "数据取出成功！");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 取得文件夹名
	 * @return
	 * @throws Exception
	 */
	public String getFolderName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqId = request.getParameter("seqId");
		YHFileSortLogic logic = new YHFileSortLogic();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sortName = "";
			if ("0".equals(seqId)) {
				sortName = "根目录";
			} else {
				YHFileSort fileSort = logic.getFileSortInfoById(dbConn, seqId);
				if (fileSort != null) {
					sortName = YHUtility.null2Empty(fileSort.getSortName());
				}
			}
			String data = "{folderName:\"" + YHUtility.encodeSpecial(sortName) + "\"}";
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
