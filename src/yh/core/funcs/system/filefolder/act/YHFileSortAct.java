package yh.core.funcs.system.filefolder.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.filefolder.data.YHFileSort;
import yh.core.funcs.system.filefolder.logic.YHFileSortLogic;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;

public class YHFileSortAct {
	private static Logger log = Logger.getLogger(YHFileSortAct.class);

	/**
	 * 新建文件夹
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addFileSortInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileSortLogic fileSortLogic = null;

		int sortParent = 0;

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			fileSortLogic = new YHFileSortLogic();

			YHFileSort fileSort = (YHFileSort) YHFOM.build(request.getParameterMap());

			String folderName = YHUtility.null2Empty(fileSort.getSortName());

			boolean isHave = false;
			int isHaveFlag = 0;
			int nodeId = 0;
			String sortName = "";

			isHave = fileSortLogic.checkFolderName(dbConn, sortParent, folderName);

			if (isHave) {
				isHaveFlag = 1;
			} else {

				fileSortLogic.saveFileSortInfo(dbConn, fileSort);
			}

			String date = "{isHaveFlag:\"" + isHaveFlag + "\" }";

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
			request.setAttribute(YHActionKeys.RET_DATA, date);

		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 取得文件夹所有信息
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getFileSortInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		int sortParent = 0;
		YHFileSortLogic fileSortLogic = new YHFileSortLogic();

		StringBuffer sb = new StringBuffer();
		List<YHFileSort> fileSorts = new ArrayList<YHFileSort>();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			//System.out.println("dbConn>>>>>>>>>>>>>>..." + dbConn);
			Map map = new HashMap();
			map.put("SORT_PARENT", sortParent);
			String[] condition = { " SORT_PARENT=" + sortParent + " AND (SORT_TYPE !='4' or SORT_TYPE is null) order by SORT_NO,SORT_NAME " };

			// fileSorts = fileSortLogic.getFileSortsInfo(dbConn, map);
			fileSorts = fileSortLogic.getFileFilderInfo(dbConn, condition);

			if (fileSorts.size() > 0) {
				sb.append("[");
				for (int i = 0; i < fileSorts.size(); i++) {
					YHFileSort fileSort = fileSorts.get(i);
					String sortNo = fileSort.getSortNo() == null ? "" : fileSort.getSortNo();
					
					String sortNoString=sortNo.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
					
					String fileSortNameStr = fileSort.getSortName() == null ? "" : fileSort.getSortName();
					fileSortNameStr = fileSortNameStr.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");

					sb.append("{");
					sb.append("sqlId:\"" + fileSort.getSeqId() + "\"");
					sb.append(",sortParent:\"" + fileSort.getSortParent() + "\"");
					sb.append(",sortNo:\"" + sortNoString + "\"");
					sb.append(",sortName:\"" + fileSortNameStr + "\"");
					sb.append(",sortType:\"" + YHUtility.null2Empty(fileSort.getSortType()) + "\"");
					sb.append(",deptId:\"" + fileSort.getDeptId() + "\"");
					sb.append(",userId:\"" + YHUtility.null2Empty(fileSort.getUserId()) + "\"");
					sb.append(",newUser:\"" + YHUtility.null2Empty(fileSort.getNewUser()) + "\"");
					sb.append(",manageUser:\"" + YHUtility.null2Empty(fileSort.getManageUser()) + "\"");
					sb.append(",DownUser:\"" + YHUtility.null2Empty(fileSort.getDownUser()) + "\"");
					sb.append(",shareUser:\"" + YHUtility.null2Empty(fileSort.getShareUser()) + "\"");
					sb.append(",owner:\"" + YHUtility.null2Empty(fileSort.getOwner()) + "\"");
					sb.append("},");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append("]");
			} else {
				sb.append("[]");
			}

			// data=YHFOM.toJson(mettingRoom).toString();
			//System.out.println("sb>>>>>>>>>>" + sb);
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
	 * 
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
			//System.out.println("dbConn>>>>>>>>>>>>>>..." + dbConn + "    seqId>>>>" + seqId);
			YHFileSort fileSort = fileSortLogic.getFileSortInfoById(dbConn, seqId);
			String sortNo = fileSort.getSortNo() == null ? "" : fileSort.getSortNo();
			String sortNoString=sortNo.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
			
			String sortName = YHUtility.null2Empty(fileSort.getSortName());
			sortName = sortName.replaceAll("[\n-\r]", "<br>");
			sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
			sortName = sortName.replace("\"", "\\\"");

			sb.append("{");
			sb.append("sqlId:\"" + fileSort.getSeqId() + "\"");
			sb.append(",sortParent:\"" + fileSort.getSortParent() + "\"");
			sb.append(",sortNo:\"" + sortNoString + "\"");
			sb.append(",sortName:\"" + sortName + "\"");
			sb.append(",sortType:\"" + YHUtility.null2Empty(fileSort.getSortType()) + "\"");
			sb.append(",deptId:\"" + fileSort.getDeptId() + "\"");
			sb.append(",userId:\"" + YHUtility.null2Empty(fileSort.getUserId()) + "\"");
			sb.append(",newUser:\"" + YHUtility.null2Empty(fileSort.getNewUser()) + "\"");
			sb.append(",manageUser:\"" + YHUtility.null2Empty(fileSort.getManageUser()) + "\"");
			sb.append(",DownUser:\"" + YHUtility.null2Empty(fileSort.getDownUser()) + "\"");
			sb.append(",shareUser:\"" + YHUtility.null2Empty(fileSort.getShareUser()) + "\"");
			sb.append(",owner:\"" + YHUtility.null2Empty(fileSort.getOwner()) + "\"");
			sb.append("},");
			sb.append("]");

			// String data=YHFOM.toJson(fileSort).toString();
			//System.out.println("YHFOM.toJson(mettingRoom).toString()>>>>>>>>>>" + sb);
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
	 * 通过id获取该文件夹的所有权限信息
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPrivteById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileSortLogic fileSortLogic = new YHFileSortLogic();
		StringBuffer sb = new StringBuffer("[");
		String seqId = request.getParameter("seqId");
		if (seqId == null || "".equals(seqId)) {
			seqId = "0";
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		String RET_MSRG="成功取出数据";
		
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			 YHFileSort fileSort = fileSortLogic.getFileSortInfoById(dbConn, seqId);
			 
			Map map = new HashMap();
			map.put("SEQ_ID", Integer.parseInt(seqId));
			String data = fileSortLogic.getVisiPriv(dbConn, map, loginUserSeqId, loginUserDeptId, loginUserRoleId);
			
			if (fileSort==null ) {
				String aaString="";
				RET_MSRG="1";
			}

			// String data=YHFOM.toJson(fileSort).toString();
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, RET_MSRG);
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
		Connection dbConn = null;
		YHFileSortLogic fileSortLogic = new YHFileSortLogic();
		int seqId = Integer.parseInt(request.getParameter("seqId"));
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			//System.out.println("dbConn>>>>>>>>>>>>>>..." + dbConn + "    seqId>>>>" + seqId);
			StringBuffer buffer = new StringBuffer();
			fileSortLogic.getSortNamePath(dbConn, seqId, buffer);
			String sortName = buffer.toString();
			String sortNames[] = sortName.split(",");
			StringBuffer sb = new StringBuffer();
			for (int i = sortNames.length - 1; i >= 0; i--) {
				sb.append(sortNames[i]);
			}
			sb.deleteCharAt(sb.length() - 1);

			String data = "[{sortName:\"" + sb.toString() + "\"}]";
			// System.out.println("data>>>>:"+data);
			YHFileSort fileSort = fileSortLogic.getSortNameById(dbConn, seqId);
			int sortParent=0;
			if (fileSort!=null) {
				sortParent=fileSort.getSortParent();
			}
			
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, String.valueOf(sortParent));
			request.setAttribute(YHActionKeys.RET_DATA, data);

		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
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
		Connection dbConn = null;
		YHFileSortLogic fileSortLogicc = new YHFileSortLogic();
		int seqId = Integer.parseInt(request.getParameter("seqId"));
		//System.out.println("Integer.parseInt(request.getParameter()>>>>>" + seqId);
		String sortNo = request.getParameter("sortNo");
		String sortName = request.getParameter("sortName");

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

			boolean isHave = false;
			int isHaveFlag = 0;
			int nodeId = 0;
			String folderName = "";
			isHave = fileSortLogicc.checkEditFolder(dbConn, seqId, sortName);
			if (isHave) {
				isHaveFlag = 1;
			} else {
				YHFileSort fileSort = fileSortLogicc.getFolderInfoById(dbConn, seqId);
				String nameStr = YHUtility.null2Empty(fileSort.getSortName());
				fileSort.setSortNo(sortNo);
				fileSort.setSortName(sortName);
				fileSortLogicc.updateSingleObj(dbConn, fileSort);

				nodeId = fileSort.getSeqId();
				folderName = fileSort.getSortName();
			// 写入系统日志
        String remark = "重命名子文件夹 " + nameStr + " ，命名为：" + folderName;
        YHSysLogLogic.addSysLog(dbConn, YHLogConst.FILE_FOLDER, remark, person.getSeqId(), request.getRemoteAddr());
			}

			String date = "[{nodeId:\"" + nodeId + "\",sortName:\"" + YHUtility.encodeSpecial(folderName) + "\",isHaveFlag:\"" + isHaveFlag + "\" }]";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "更新成功");
			request.setAttribute(YHActionKeys.RET_DATA, date);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 删除件夹及其下的子文件和文件信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	// public String delFileSortInfoById(HttpServletRequest request,
	// HttpServletResponse response) throws Exception {
	// int seqId = Integer.parseInt(request.getParameter("seqId"));
	// Connection dbConn = null;
	// YHFileSort fileSort = null;
	// YHFileSortLogic fileSortLogic = new YHFileSortLogic();
	// try {
	// YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	// .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	// dbConn = requestDbConn.getSysDbConn();
	// System.out.println("dbConn>>>>>>>>>>del>>>>" + dbConn);
	// fileSort = new YHFileSort();
	// fileSort.setSeqId(seqId);
	// fileSortLogic.delFileSortInfoById(dbConn, fileSort);
	// request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	// request.setAttribute(YHActionKeys.RET_MSRG, "数据删除成功！");
	// } catch (Exception ex) {
	// request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	// request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
	// throw ex;
	// }
	// return "/core/inc/rtjson.jsp";
	// }

	/**
	 * 获取树形结构信息,用于权限设置用，不考虑权限。
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idStr = request.getParameter("id");
		String sortIdStr = request.getParameter("seqId");
		int id = 0;
		if (idStr != null && !"".equals(idStr)) {
			id = Integer.parseInt(idStr);
		}

		Connection dbConn = null;
		YHFileSortLogic fileSortLogic = new YHFileSortLogic();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			Map map = new HashMap();
			StringBuffer sb = new StringBuffer("[");
			if (sortIdStr != null && !"".equals(sortIdStr) && id == 0) {
				YHFileSort fileSort = fileSortLogic.getFileSortInfoById(dbConn, sortIdStr);
				if (fileSort != null) {
					int seqId = fileSort.getSeqId();
					String sortName = YHUtility.null2Empty(fileSort.getSortName());
					sortName = sortName.replaceAll("[\n-\r]", "<br>");
					sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
					sortName = sortName.replace("\"", "\\\"");

					int isHaveChild = fileSortLogic.isHaveChild(dbConn, fileSort.getSeqId());
					String extData = "";
					// String imgAddress =
					// "/yh/core/styles/style1/img/dtree/node_dept.gif";
					sb.append("{");
					sb.append("nodeId:\"" + seqId + "\"");

					sb.append(",name:\"" + sortName + "\"");
					sb.append(",isHaveChild:" + isHaveChild + "");
					sb.append(",extData:\"" + extData + "\"");
					// sb.append(",imgAddress:\"" + imgAddress + "\"");
					sb.append("},");
					sb.deleteCharAt(sb.length() - 1);
				}

			} else {
				// map.put("SORT_PARENT", id);
				// List<YHFileSort> list = fileSortLogic.getFileSorts(dbConn, map);

				String[] condition = { " SORT_PARENT=" + id + " order by SORT_NO,SORT_NAME" };
				List<YHFileSort> list = fileSortLogic.getFileFilderInfo(dbConn, condition);

				if (list.size() > 0) {
					for (YHFileSort fileSort : list) {
						int seqId = fileSort.getSeqId();
						String sortName = YHUtility.null2Empty(fileSort.getSortName());
						sortName = sortName.replaceAll("[\n-\r]", "<br>");
						sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
						sortName = sortName.replace("\"", "\\\"");
						int isHaveChild = fileSortLogic.isHaveChild(dbConn, fileSort.getSeqId());
						String extData = "";
						// String imgAddress =
						// "/yh/core/styles/style1/img/dtree/node_dept.gif";
						sb.append("{");
						sb.append("nodeId:\"" + seqId + "\"");
						sb.append(",name:\"" + sortName + "\"");
						sb.append(",isHaveChild:" + isHaveChild + "");
						sb.append(",extData:\"" + extData + "\"");
						// sb.append(",imgAddress:\"" + imgAddress + "\"");
						sb.append("},");

					}
					sb.deleteCharAt(sb.length() - 1);
				}
			}

			sb.append("]");
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取文件夹树形结构信息，考虑是否有权限，有权限才能显示
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPrivTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idStr = request.getParameter("id");
		String sortIdStr = request.getParameter("seqId");
		int id = 0;
		if (idStr != null && !"".equals(idStr)) {
			id = Integer.parseInt(idStr);
		}
		if (sortIdStr == null || "".equals(sortIdStr.trim())) {
			sortIdStr = "0";
		}
		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		Connection dbConn = null;
		YHFileSortLogic fileSortLogic = new YHFileSortLogic();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			// Map map = new HashMap();
			StringBuffer sb = new StringBuffer();

			if (sortIdStr != null && !"0".equals(sortIdStr.trim()) && id == 0) {

				// String[] condition={" SORT_PARENT=" + sortIdStr
				// +" AND (SORT_TYPE !='4' or SORT_TYPE is null) " };
				// fileSorts=fileSortLogic.getFileFilderInfo(dbConn, condition);

				YHFileSort fileSort = fileSortLogic.getFileSortInfoById(dbConn, sortIdStr);
				if (fileSort != null) {
					sb.append("[");
					int seqId = fileSort.getSeqId();
					String sortName = YHUtility.null2Empty(fileSort.getSortName());

					// String contentStr = content.getContent();
					sortName = sortName.replaceAll("[\n-\r]", "<br>");
					sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
					sortName = sortName.replace("\"", "\\\"");

					int isHaveChild = fileSortLogic.isHaveChild(dbConn, fileSort.getSeqId());
					String extData = "";
					sb.append("{");
					sb.append("nodeId:\"" + seqId + "\"");
					sb.append(",name:\"" + sortName + "\"");
					sb.append(",isHaveChild:" + isHaveChild + "");
					sb.append(",extData:\"" + extData + "\"");
					sb.append("},");
					sb.deleteCharAt(sb.length() - 1);
					sb.append("]");
				} else if (fileSort == null) {
					sb.append("[]");
				}

			} else {
				String[] condition = { " SORT_PARENT=" + id + " AND (SORT_TYPE !='4' or SORT_TYPE is null)  order by SORT_NO,SORT_NAME" };
				List<YHFileSort> list = fileSortLogic.getFileFilderInfo(dbConn, condition);

				// map.put("SORT_PARENT", id);
				// List<YHFileSort> list = fileSortLogic.getFileSorts(dbConn, map);
				boolean userFlag = false;
				boolean roleFlag = false;
				boolean deptFlag = false;

				boolean ownerUserFlag = false;
				boolean ownerRoleFlag = false;
				boolean ownerDeptFlag = false;

				if (list.size() > 0) {
					sb.append("[");
					Map map2 = new HashMap();
					boolean isHave = false;
					for (YHFileSort fileSort : list) {
						int seqId = fileSort.getSeqId();
						map2.put("SEQ_ID", seqId);
						YHFileSort fileSort2 = fileSortLogic.getFileSortInfoById(dbConn, map2);

						String userPrivs = fileSortLogic.selectManagerIds(dbConn, fileSort2, "USER_ID");
						String rolePrivs = fileSortLogic.getRoleIds(dbConn, fileSort2, "USER_ID");
						String deptPrivs = fileSortLogic.getDeptIds(dbConn, fileSort2, "USER_ID");

						String ownerUserPrivs = fileSortLogic.selectManagerIds(dbConn, fileSort2, "OWNER");
						String ownerRolePrivs = fileSortLogic.getRoleIds(dbConn, fileSort2, "OWNER");
						String ownerDeptPrivs = fileSortLogic.getDeptIds(dbConn, fileSort2, "OWNER");

						userFlag = fileSortLogic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
						roleFlag = fileSortLogic.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
						deptFlag = fileSortLogic.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);

						ownerUserFlag = fileSortLogic.getUserIdStr(loginUserSeqId, ownerUserPrivs, dbConn);
						ownerRoleFlag = fileSortLogic.getRoleIdStr(loginUserRoleId, ownerRolePrivs, dbConn);
						ownerDeptFlag = fileSortLogic.getDeptIdStr(loginUserDeptId, ownerDeptPrivs, dbConn);
						
						
		        String[] conditionChild = { " SORT_PARENT=" + seqId + " AND (SORT_TYPE !='4' or SORT_TYPE is null)  order by SORT_NO,SORT_NAME" };
		        List<YHFileSort> listChild = fileSortLogic.getFileFilderInfo(dbConn, conditionChild);
		        

						if (ownerUserFlag == true || ownerRoleFlag == true || ownerDeptFlag == true) {
							String sortName = YHUtility.null2Empty(fileSort.getSortName());
							// int isHaveChild = fileSortLogic.isHaveChild(dbConn,
							// fileSort.getSeqId(),loginUserSeqId,loginUserRoleId,loginUserDeptId);
							sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
							sortName = sortName.replaceAll("[\n-\r]", "<br>");
							sortName = sortName.replace("\"", "\\\"");

							String extData = "";
							sb.append("{");
							sb.append("nodeId:\"" + seqId + "\"");
							sb.append(",name:\"" + sortName + "\"");
							sb.append(",isHaveChild:" + (listChild.size() > 0 ? 1 : 0) + "");
							sb.append(",extData:\"" + extData + "\"");
							sb.append("},");
							isHave = true;
						} else if (userFlag == true || roleFlag == true || deptFlag == true) {
							String sortName = YHUtility.null2Empty(fileSort.getSortName());
							// int isHaveChild = fileSortLogic.isHaveChild(dbConn,
							// fileSort.getSeqId(),loginUserSeqId,loginUserRoleId,loginUserDeptId);
							sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
							sortName = sortName.replaceAll("[\n-\r]", "<br>");
							sortName = sortName.replace("\"", "\\\"");

							String extData = "";
							sb.append("{");
							sb.append("nodeId:\"" + seqId + "\"");
							sb.append(",name:\"" + sortName + "\"");
							sb.append(",isHaveChild:" + (listChild.size() > 0 ? 1 : 0) + "");
							sb.append(",extData:\"" + extData + "\"");
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
			}

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
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
		Connection dbConn = null;
		YHFileSortLogic fileSortLogic = null;
		String seqId = request.getParameter("seqId");
		//System.out.println("seqId>>>>>>>>>" + seqId);
		int sortParent = 0;
		if (seqId != null) {
			sortParent = Integer.parseInt(seqId);
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			fileSortLogic = new YHFileSortLogic();
			YHFileSort fileSort = (YHFileSort) YHFOM.build(request.getParameterMap());
			String folderName = YHUtility.null2Empty(fileSort.getSortName());

			boolean isHave = false;
			int isHaveFlag = 0;
			int nodeId = 0;
			String sortName = "";

			isHave = fileSortLogic.checkFolderName(dbConn, sortParent, folderName);

			if (isHave) {
				isHaveFlag = 1;

			} else {
				YHFileSort sort = fileSortLogic.getFileSortInfoById(dbConn, seqId);
				fileSort.setSortParent(sortParent);
				fileSort.setNewUser(sort.getNewUser());
				fileSort.setUserId(sort.getUserId());
				fileSort.setManageUser(sort.getManageUser());
				fileSort.setDownUser(sort.getDownUser());
				fileSort.setShareUser(sort.getShareUser());
				fileSort.setOwner(sort.getOwner());

				fileSortLogic.saveFileSortInfo(dbConn, fileSort);
				YHFileSort fileSort2 = new YHFileSort();
				fileSort2 = fileSortLogic.getMaxSeqId(dbConn);
				nodeId = fileSort2.getSeqId();
				sortName = YHUtility.null2Empty(fileSort2.getSortName());
				
				String remark = "新建子文件夹，名称：" + sortName ;
        YHSysLogLogic.addSysLog(dbConn, YHLogConst.FILE_FOLDER, remark, person.getSeqId(), request.getRemoteAddr());
			}
			String date = "[{nodeId:\"" + nodeId + "\",sortName:\"" + YHUtility.encodeSpecial(sortName) + "\",isHaveFlag:\"" + isHaveFlag + "\" }]";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
			request.setAttribute(YHActionKeys.RET_DATA, date);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 根据id设置访问权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setVisitById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHFileSortLogic fileSortLogicc = new YHFileSortLogic();
		String sortId = request.getParameter("seqId");
		String override = request.getParameter("override");
		String action = "USER_ID";
		int seqId = 0;
		if (sortId != null) {
			seqId = Integer.parseInt(sortId);
		}
		//System.out.println("FileSort__seqId>>>>>>>>>>>>>" + seqId);
		String userId = request.getParameter("userId");
		if (userId.replaceAll("|", "").length() == 0) {
			userId = "";
		}
		//System.out.println("userId>>>>>" + userId);

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			Map map = new HashMap();
			map.put("userId", userId);
			map.put("seqId", seqId);

			if ("override".equals(override)) {
				fileSortLogicc.updateVisitOverride(dbConn, seqId, userId, action);
			} else {
				fileSortLogicc.updateVisitById(dbConn, map);
			}

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
	 * 根据id设置管理权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setManageUserById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHFileSortLogic fileSortLogicc = new YHFileSortLogic();
		String sortId = request.getParameter("seqId");
		String override = request.getParameter("override");
		String action = "MANAGE_USER";
		int seqId = 0;
		if (sortId != null) {
			seqId = Integer.parseInt(sortId);
		}
		String manageUser = request.getParameter("manageUser");
		if (manageUser.replaceAll("|", "").length() == 0) {
			manageUser = "";
		}
		//System.out.println("manageUser>>>>>" + manageUser);

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			Map map = new HashMap();
			map.put("seqId", seqId);
			map.put("manageUser", manageUser);

			if ("override".equals(override)) {
				fileSortLogicc.updateVisitOverride(dbConn, seqId, manageUser, action);
			} else {
				fileSortLogicc.updateManageUserById(dbConn, map);
			}

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
   * 根据id设置管理权限
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setDelUserById(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    YHFileSortLogic fileSortLogicc = new YHFileSortLogic();
    String sortId = request.getParameter("seqId");
    String override = request.getParameter("override");
    String action = "DEL_USER";
    int seqId = 0;
    if (sortId != null) {
      seqId = Integer.parseInt(sortId);
    }
    String manageUser = request.getParameter("manageUser");
    if (manageUser.replaceAll("|", "").length() == 0) {
      manageUser = "";
    }
    //System.out.println("manageUser>>>>>" + manageUser);

    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map map = new HashMap();
      map.put("seqId", seqId);
      map.put("manageUser", manageUser);

      if ("override".equals(override)) {
        fileSortLogicc.updateVisitOverride(dbConn, seqId, manageUser, action);
      } else {
        fileSortLogicc.updateDelUserById(dbConn, map);
      }

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
	 * 根据id设置新建权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setNewUserById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHFileSortLogic fileSortLogicc = new YHFileSortLogic();
		String sortId = request.getParameter("seqId");
		String override = request.getParameter("override");
		String action = "NEW_USER";
		int seqId = 0;
		if (sortId != null) {
			seqId = Integer.parseInt(sortId);
		}
		String newUser = request.getParameter("createId");
		if (newUser.replaceAll("|", "").length() == 0) {
			newUser = "";
		}
		//System.out.println("newUser>>>>>" + newUser);

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			Map map = new HashMap();
			map.put("seqId", seqId);
			map.put("newUser", newUser);

			if ("override".equals(override)) {
				fileSortLogicc.updateVisitOverride(dbConn, seqId, newUser, action);
			} else {
				fileSortLogicc.updateNewUserById(dbConn, map);
			}

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
	 * 根据id设置下载权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setDownLoadById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHFileSortLogic fileSortLogicc = new YHFileSortLogic();
		String sortId = request.getParameter("seqId");
		String override = request.getParameter("override");
		String action = "DOWN_USER";
		int seqId = 0;
		if (sortId != null) {
			seqId = Integer.parseInt(sortId);
		}
		String downUser = request.getParameter("downLoadId");
		if (downUser.replaceAll("|", "").length() == 0) {
			downUser = "";
		}
		//System.out.println("downUser>>>>>" + downUser);

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			Map map = new HashMap();
			map.put("seqId", seqId);
			map.put("downUser", downUser);
			if ("override".equals(override)) {
				fileSortLogicc.updateVisitOverride(dbConn, seqId, downUser, action);
			} else {
				fileSortLogicc.updateDownLoadById(dbConn, map);
			}

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
	 * 根据id设置所有者权限
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setOwnerById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHFileSortLogic fileSortLogicc = new YHFileSortLogic();
		String sortId = request.getParameter("seqId");
		String override = request.getParameter("override");
		String action = "OWNER";
		int seqId = 0;
		if (sortId != null) {
			seqId = Integer.parseInt(sortId);
		}
		String owner = request.getParameter("ownerId");
		if (owner.replaceAll("|", "").length() == 0) {
			owner = "";
		}
		//System.out.println("downUser>>>>>" + owner);

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			Map map = new HashMap();
			map.put("seqId", seqId);
			map.put("owner", owner);

			if ("override".equals(override)) {
				fileSortLogicc.updateVisitOverride(dbConn, seqId, owner, action);
			} else {
				fileSortLogicc.updateOwnerById(dbConn, map);
			}

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
	 * 获取人员id名字串
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPersonIdStr(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn;
		String seqIdStr = request.getParameter("seqId");
		String action = request.getParameter("action");
		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHFileSortLogic sortLogic = new YHFileSortLogic();
			Map map = new HashMap();
			map.put("SEQ_ID", seqId);
			YHFileSort fileSort = sortLogic.getFileSortInfoById(dbConn, map);
			String ids = sortLogic.selectManagerIds(dbConn, fileSort, action);
			String names = "";
			//System.out.println(ids);
			if (!YHUtility.isNullorEmpty(ids)) {
				names = sortLogic.getNamesByIds(dbConn, map, action);
			}
			String data = "{user:\"" + ids + "\",userDesc:\"" + YHUtility.encodeSpecial(names) + "\"}";
			//System.out.println(data);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取所有人员的id名字串
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getAllPersonIdStr(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn;
		String seqIdStr = request.getParameter("seqId");
		// String action = request.getParameter("action");
		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHFileSortLogic sortLogic = new YHFileSortLogic();
			String data = sortLogic.getSortName(dbConn, seqId);

			//System.out.println(data);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取角色人员id名字串
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getRoleIdStr(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn;
		String seqIdStr = request.getParameter("seqId");
		String action = request.getParameter("action");
		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHFileSortLogic sortLogic = new YHFileSortLogic();
			Map map = new HashMap();
			map.put("SEQ_ID", seqId);
			YHFileSort fileSort = sortLogic.getFileSortInfoById(dbConn, map);
			String ids = sortLogic.getRoleIds(dbConn, fileSort, action);
			String names = "";
			//System.out.println(ids);
			if (!YHUtility.isNullorEmpty(ids)) {
				names = sortLogic.getRoleNamesByIds(dbConn, map, action);
			}
			String data = "{role:\"" + ids + "\",roleDesc:\"" + YHUtility.encodeSpecial(names) + "\"}";
			//System.out.println(data);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取部门人员id名字串
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getDeptIdStr(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn;
		String seqIdStr = request.getParameter("seqId");
		String action = request.getParameter("action");
		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHFileSortLogic sortLogic = new YHFileSortLogic();
			Map map = new HashMap();
			map.put("SEQ_ID", seqId);
			YHFileSort fileSort = sortLogic.getFileSortInfoById(dbConn, map);
			String ids = sortLogic.getDeptIds(dbConn, fileSort, action);
			String names = "";
			//System.out.println(ids);
			if (!YHUtility.isNullorEmpty(ids)) {
				names = sortLogic.getDeptByIds(dbConn, map, action);
			}
			String data = "{dept:\"" + ids + "\",deptDesc:\"" + YHUtility.encodeSpecial(names) + "\"}";
			//System.out.println(data);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
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
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String copyFolderById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		String seqId = request.getParameter("folderId");
		String action = request.getParameter("action");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			//System.out.println("dbConn>>>>>>>>>>>>>>..." + dbConn + "    seqId>>>>" + seqId);

			request.getSession().setAttribute("folderActionStr", action);
			request.getSession().setAttribute("folderSeqId", seqId);
			// System.out.println(request.getSession().getAttribute("actionStr"));
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 粘贴文件夹	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String pasteFolder(HttpServletRequest request, HttpServletResponse response) throws Exception {

		YHFileSortLogic fileSortLogic = new YHFileSortLogic();
		String action = (String) request.getSession().getAttribute("folderActionStr");
		String seqId = (String) request.getSession().getAttribute("folderSeqId"); // 点击复制时的文件夹seqId
		String sortParent = request.getParameter("sortParent"); // 点击粘贴时的文件夹seqId作为父级id
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			// YHFileSort fileSort1 = fileSortLogic.getFileSortInfoById(dbConn,
			// sortParent); //获取粘贴文件夹信息
			YHFileSort fileSort3 = fileSortLogic.getFileSortInfoById(dbConn, seqId); // 获取补复制文件夹信息
			String data = "";
			int nodeId = 0;
			int isHaveChild =0;
			int nullFlag =0;
			String sortName ="";
			if (fileSort3!=null) {
				Map<Object, Object> nodeNameMap = new HashMap<Object, Object>();
//				sortName = YHUtility.null2Empty(fileSort3.getSortName());
				if ("cut".equals(action)) {
					YHFileSort fileSort2 = new YHFileSort();
					fileSort2.setSeqId(Integer.parseInt(seqId));
					fileSortLogic.updateFolderInfoById(dbConn, Integer.parseInt(sortParent), Integer.parseInt(seqId),nodeNameMap);
					isHaveChild = fileSortLogic.isHaveChild(dbConn, Integer.parseInt(seqId));
					nodeId = fileSort3.getSeqId();
					sortName = (String)nodeNameMap.get("sortName");
				} else if ("copy".equals(action)) {
					// 级联查询本文件夹及其所有的子文件夹信息
					List listTemp = new ArrayList();
					YHFileSort maxFileSort = fileSortLogic.getMaxSeqId(dbConn);
					int maxSeqId = maxFileSort.getSeqId();
					List folderList = fileSortLogic.getAllFolderList(dbConn, Integer.parseInt(seqId), Integer.parseInt(sortParent), listTemp,nodeNameMap, maxSeqId);
					isHaveChild = fileSortLogic.isHaveChild(dbConn, Integer.parseInt(seqId));
					nodeId = (Integer)folderList.get(0);
					sortName = (String)nodeNameMap.get("sortName");
				}
			}else {
				nullFlag = 1;
			}
			data = "{nodeId:\"" + nodeId 
					+ "\",isHaveChild:\"" + isHaveChild 
					+ "\",sortName:\"" + YHUtility.encodeSpecial(sortName) 
					+ "\",seqId:\""	+ seqId 
					+ "\",nullFlag:\""	+ nullFlag 
					+ "\",action:\"" + action + "\" }";
			request.getSession().setAttribute("folderActionStr", "");
			request.getSession().setAttribute("folderSeqId", "");

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
	 * 批量设置权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setBatchPriv(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		String seqString = request.getParameter("seqId");		//文件夹seqId	12
		String setIdStr = request.getParameter("idStr");		//设置的id串	16||
		String check = request.getParameter("check");				//要设置的选项   OWNER,
		String opt = request.getParameter("opt");						//添加或删除操作  addPriv

		String checks = check.substring(0, check.length() - 1);
		int seqId = 0;
		if (seqString != "") {
			seqId = Integer.parseInt(seqString);
		}

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHFileSortLogic logic = new YHFileSortLogic();
			if ("addPriv".equals(opt)) {
				if (checks != "") {
					String[] checkStrs = checks.split(",");
					for (int i = 0; i < checkStrs.length; i++) {
						if ("USER_ID".equals(checkStrs[i])) {
//							logic.updateVisitOverride(dbConn, seqId, setIdStr, "USER_ID");
							logic.updateVisitOverrideAdd(dbConn, seqId, setIdStr, "USER_ID");
						}
						if ("MANAGE_USER".equals(checkStrs[i])) {
							logic.updateVisitOverrideAdd(dbConn, seqId, setIdStr, "MANAGE_USER");
						}
						if ("DEL_USER".equals(checkStrs[i])) {
              logic.updateVisitOverrideAdd(dbConn, seqId, setIdStr, "DEL_USER");
            }
						if ("NEW_USER".equals(checkStrs[i])) {
							logic.updateVisitOverrideAdd(dbConn, seqId, setIdStr, "NEW_USER");
						}
						if ("DOWN_USER".equals(checkStrs[i])) {
							logic.updateVisitOverrideAdd(dbConn, seqId, setIdStr, "DOWN_USER");
						}
						if ("OWNER".equals(checkStrs[i])) {
							logic.updateVisitOverrideAdd(dbConn, seqId, setIdStr, "OWNER");
						}
					}
				}

			} else if ("delPriv".equals(opt)) {
				if (checks != "") {
					String[] checkStrs = checks.split(",");
					for (int i = 0; i < checkStrs.length; i++) {
						if ("USER_ID".equals(checkStrs[i])) {
							logic.updateVisitOverrideDel(dbConn, seqId, setIdStr, "USER_ID");
						}
						if ("MANAGE_USER".equals(checkStrs[i])) {
							logic.updateVisitOverrideDel(dbConn, seqId, setIdStr, "MANAGE_USER");
						}
						if ("DEL_USER".equals(checkStrs[i])) {
              logic.updateVisitOverrideDel(dbConn, seqId, setIdStr, "DEL_USER");
            }
						if ("NEW_USER".equals(checkStrs[i])) {
							logic.updateVisitOverrideDel(dbConn, seqId, setIdStr, "NEW_USER");
						}
						if ("DOWN_USER".equals(checkStrs[i])) {
							logic.updateVisitOverrideDel(dbConn, seqId, setIdStr, "DOWN_USER");
						}
						if ("OWNER".equals(checkStrs[i])) {
							logic.updateVisitOverrideDel(dbConn, seqId, setIdStr, "OWNER");
						}
					}
				}
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "批量设置权限成功");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取转存到公共文件柜的根目录信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getFolderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		String parentIdStr = request.getParameter("parentId");
		String attachId = request.getParameter("attachId");
		String attachName = request.getParameter("attachName");
		String backFlag = request.getParameter("backFlag");
		String module = request.getParameter("module");

		int parentId = 0;
		int seqId = 0;
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}
		if (parentIdStr != null) {
			parentId = Integer.parseInt(parentIdStr);
		}
		if (backFlag == null) {
			backFlag = "";
		}
		if (module == null) {
			module = "";
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		boolean userFlag = false;
		boolean roleFlag = false;
		boolean deptFlag = false;

		boolean newUserFlag = false;
		boolean newRoleFlag = false;
		boolean newDeptFlag = false;

		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		Map map = new HashMap();
		YHFileSortLogic fileSortLogic = new YHFileSortLogic();
		YHFileSort fileSort = new YHFileSort();
		List<YHFileSort> list = new ArrayList<YHFileSort>();

		int inIt = 0;
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			int sortPar = 0;

			if ("back".equals(backFlag)) {
				if (parentId == 0 && seqId == 0) {
					parentId = 0;
					seqId = 0;
				} else {
					map.put("SORT_PARENT", parentId);
					list = fileSortLogic.getFileSorts(dbConn, map);
					fileSort = fileSortLogic.getFileSortInfoById(dbConn, parentIdStr);
					if (fileSort != null) {
						inIt = 1;
						sortPar = fileSort.getSeqId();
						if (fileSort.getSortParent() == 0) {
							parentId = 0;
							seqId = 0;
						} else {
							parentId = fileSort.getSortParent();
							seqId = fileSort.getSeqId();
						}
					} else {
						seqId = 0;
						inIt = 1;
					}
				}
			}
			//System.out.println(seqId == 0 && sortPar == 0);

			if (seqId == 0 && sortPar == 0) {
				
//				map.put("SORT_PARENT", parentId);
//				list = fileSortLogic.getFileSorts(dbConn, map);
				
				String[] condition = { " SORT_PARENT=" + parentId + " AND (SORT_TYPE !='4' or SORT_TYPE is null)  order by SORT_NO,SORT_NAME" };
				list = fileSortLogic.getFileFilderInfo(dbConn, condition);
				
			} else if (seqId != 0) {
				map.put("SORT_PARENT", seqId);
				list = fileSortLogic.getFileSorts(dbConn, map);
				fileSort = fileSortLogic.getFileSortInfoById(dbConn, String.valueOf(seqId));
				parentId = fileSort.getSortParent();
				seqId = fileSort.getSeqId();
			}

			if (list.size() != 0) {
				for (YHFileSort sort : list) {
					map.put("SEQ_ID", sort.getSeqId());
					YHFileSort fileSort2 = fileSortLogic.getFileSortInfoById(dbConn, map);

					String userPrivs = fileSortLogic.selectManagerIds(dbConn, fileSort2, "USER_ID");
					String rolePrivs = fileSortLogic.getRoleIds(dbConn, fileSort2, "USER_ID");
					String deptPrivs = fileSortLogic.getDeptIds(dbConn, fileSort2, "USER_ID");

					String newUserPrivs = fileSortLogic.selectManagerIds(dbConn, fileSort2, "NEW_USER");
					String newRolePrivs = fileSortLogic.getRoleIds(dbConn, fileSort2, "NEW_USER");
					String newDeptPrivs = fileSortLogic.getDeptIds(dbConn, fileSort2, "NEW_USER");

					userFlag = fileSortLogic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					roleFlag = fileSortLogic.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					deptFlag = fileSortLogic.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);

					newUserFlag = fileSortLogic.getUserIdStr(loginUserSeqId, newUserPrivs, dbConn);
					newRoleFlag = fileSortLogic.getRoleIdStr(loginUserRoleId, newRolePrivs, dbConn);
					newDeptFlag = fileSortLogic.getDeptIdStr(loginUserDeptId, newDeptPrivs, dbConn);

					int visitFlag = 0;
					int newFlag = 0;
					if (userFlag || roleFlag || deptFlag) {
						visitFlag = 1;
					}
					if (newUserFlag || newRoleFlag || newDeptFlag) {
						newFlag = 1;
					}
					if (visitFlag==1 ) {
						
						
						Map<String, String> sortMap = new HashMap<String, String>();
						sortMap.put("seqId", String.valueOf(sort.getSeqId()));
						sortMap.put("sortName", sort.getSortName());
						sortMap.put("sortParent", String.valueOf(sort.getSortParent()));
						sortMap.put("visitFlag", String.valueOf(visitFlag));
						sortMap.put("newFlag", String.valueOf(newFlag));
						returnList.add(sortMap);
					}

				}

			}
			
			
			request.setAttribute("attachId", attachId);
			request.setAttribute("attachName", attachName);
			request.setAttribute("module", module);
			
			request.setAttribute("seqId", seqId);
			request.setAttribute("parentId", parentId);
			request.setAttribute("inIt", inIt);

			request.setAttribute("fileSortList", returnList);
			
			
			// seqId=sortPar;
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/funcs/savefile/folder1.jsp";
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

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

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
			// String userIdStrs="";
			// if (fileSort!=null) {
			// userIdStrs=fileSort.getUserId();
			// }

			String deptIdStrs = logic.getDeptIds(dbConn, fileSort, "USER_ID");
			String roleIdStrs = logic.getRoleIds(dbConn, fileSort, "USER_ID");
			String userIdStrs = logic.selectManagerIds(dbConn, fileSort, "USER_ID");
			;

			// 返回有权限人员的部门id串,用到以下两个表
			// YHPerson
			// YHDepartment
			String userDeptIdStr = logic.getUserDeptIdStr(loginUserSeqId, userIdStrs, dbConn);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "数据取出成功！");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

	public static void main(String[] args) {
		// String string = "aaa,|&!}sdfs";
		// String[] a = string.split("\\|");
		// System.out.println(a.length);
		// String actions[] = new String[] { "USER_ID", "MANAGE_USER", "NEW_USER",
		// "DOWN_USER" };
		// for (int i = 0; i < actions.length; i++) {
		// // System.out.println("actions["+i+"]"+actions[i]);
		// if ("USER_ID".equals(actions[i])) {
		// System.out.println(actions[i]);
		// }
		//
		// }
		String s = "\"\\/:*?<>|";

		s = s.replaceAll("[\\\\/:*?\"<>|]", "");

		//System.out.println(s);
	}

}
