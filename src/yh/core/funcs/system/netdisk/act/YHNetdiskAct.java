package yh.core.funcs.system.netdisk.act;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.netdisk.data.YHNetdisk;
import yh.core.funcs.system.netdisk.logic.YHNetdiskLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;

public class YHNetdiskAct {

	/**
	 * 新建共享目录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addNetdiskFolderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHNetdiskLogic logic = new YHNetdiskLogic();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHNetdisk netdisk = (YHNetdisk) YHFOM.build(request.getParameterMap());
			String data = "";
			int createFlag = 0;
			int sameNameFlag = 1;
			// int spaceLimit=2147483647;
			String path = YHUtility.null2Empty(netdisk.getDiskPath()).trim();
			// int spaceSize=netdisk.getSpaceLimit();
			// if (spaceSize>spaceLimit) {
			// spaceSize=spaceLimit;
			// }
			// netdisk.setSpaceLimit(spaceSize);

//			List<YHNetdisk> list = logic.getNetdiskFolderInfo(dbConn);
			boolean isHaveFlag = logic.checkDiskPathLogic(dbConn, 0, path);
			if (isHaveFlag) {
				sameNameFlag =0;
			}
			if (sameNameFlag != 0) {
				File file = new File(path);
				if (file!=null) {
					String filePath = file.getPath().trim();
					String diskPath = filePath.trim().replace("\\", "/");
					
					if (!YHUtility.isNullorEmpty(path.trim()) && file!=null) {
						boolean flag = logic.createFolder(diskPath.trim());
						if (flag) {
							netdisk.setDiskPath(diskPath.trim() + "/");
							logic.saveNetFolderInfo(dbConn, netdisk);
							createFlag = 1;
						}
					}
				}
			}
			data = "[{createFlag:\"" + createFlag + "\",sameNameFlag:\"" + sameNameFlag + "\"}]";
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
	 * 功能：获取置共享目录信息
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getNetdiskFolderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		StringBuffer sb = new StringBuffer("[");
		List<YHNetdisk> netdisks = new ArrayList<YHNetdisk>();
		YHNetdiskLogic logic = new YHNetdiskLogic();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			// System.out.println("dbConn>>>>>>>>>>>>>>..." + dbConn);

			String[] filters = { "SEQ_ID !=0 order by DISK_NO" };

			netdisks = logic.getNetdiskFolderList(dbConn, filters);
			if (netdisks.size() > 0) {
				for (int i = 0; i < netdisks.size(); i++) {
					YHNetdisk netdisk = netdisks.get(i);
					String diskName = netdisk.getDiskName() == null ? "" : netdisk.getDiskName();
					diskName = diskName.replaceAll("[\n-\r]", "<br>");
					diskName = diskName.replaceAll("[\\\\/:*?\"<>|]", "");
					diskName = diskName.replace("\"", "\\\"");

					// String sortNo =netdisk.getd == null ? "" : netdisk.getDiskNo();
					// String sortNoString=sortNo.replace("\\", "\\\\").replace("\"",
					// "\\\"").replace("\r", "").replace("\n", "");

					String diskPath = netdisk.getDiskPath() == null ? "" : netdisk.getDiskPath();
					Object sapceLimit = netdisk.getSpaceLimit() == 0 ? "不限" : netdisk.getSpaceLimit();
					String orderBy = YHUtility.null2Empty(netdisk.getOrderBy());
					String ascDesc = YHUtility.null2Empty(netdisk.getAscDesc());
					String orderByAndAscDesc = "";
					if ("nom".equals(orderBy)) {
						orderBy = "名称";
					} else if ("taille".equals(orderBy)) {
						orderBy = "大小";
					} else if ("type".equals(orderBy)) {
						orderBy = "类型";
					} else if ("mod".equals(orderBy)) {
						orderBy = "最后修改时间";
					}
					if ("0".equals(ascDesc)) {
						ascDesc = "(升序)";
					} else if ("1".equals(ascDesc)) {
						ascDesc = "(降序)";
					}
					orderByAndAscDesc = orderBy + ascDesc;

					sb.append("{");
					sb.append("sqlId:\"" + netdisk.getSeqId() + "\"");
					sb.append(",diskNo:\"" + netdisk.getDiskNo() + "\"");
					sb.append(",diskName:\"" + diskName + "\"");
					sb.append(",diskPath:\"" + diskPath + "\"");
					sb.append(",spaceLimit:\"" + sapceLimit + "\"");
					sb.append(",orderBy:\"" + orderByAndAscDesc + "\"");
					sb.append("},");
				}

				sb.deleteCharAt(sb.length() - 1);
				sb.append("]");
			} else {
				sb.append("]");
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
	 * 通过id获取信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getNetdiskFolderInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHNetdiskLogic diskLogic = new YHNetdiskLogic();
		StringBuffer sb = new StringBuffer("[");
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (seqIdStr != "") {
			seqId = Integer.parseInt(seqIdStr);
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			// System.out.println("dbConn>>>>>>>>>>>>>>..." + dbConn + "    seqId>>>>"
			// + seqId);
			YHNetdisk netdisk = diskLogic.getNetdiskInfoById(dbConn, seqId);
			String diskName = netdisk.getDiskName() == null ? "" : netdisk.getDiskName();
			diskName = diskName.replaceAll("[\n-\r]", "<br>");
			diskName = diskName.replaceAll("[\\\\/:*?\"<>|]", "");
			diskName = diskName.replace("\"", "\\\"");

			String diskPath = netdisk.getDiskPath() == null ? "" : netdisk.getDiskPath();
			String orderBy = netdisk.getOrderBy() == null ? "" : netdisk.getOrderBy();
			sb.append("{");
			sb.append("sqlId:\"" + netdisk.getSeqId() + "\"");
			sb.append(",diskName:\"" + diskName + "\"");
			sb.append(",diskPath:\"" + diskPath + "\"");
			sb.append(",orderBy:\"" + orderBy + "\"");
			sb.append(",diskNo:\"" + netdisk.getDiskNo() + "\"");
			sb.append(",spaceLimit:\"" + netdisk.getSpaceLimit() + "\"");
			sb.append(",ascDesc:\"" + YHUtility.null2Empty(netdisk.getAscDesc()) + "\"");
			sb.append("}");
			sb.append("]");

			// String data=YHFOM.toJson(fileSort).toString();
			// System.out.println("YHFOM.toJson(mettingRoom).toString()>>>>>>>>>>" +
			// sb);
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
	 * 更新编辑网络硬盘共享目录信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateNetdisFolderById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHNetdiskLogic diskLogic = new YHNetdiskLogic();
		int seqId = Integer.parseInt(request.getParameter("seqId"));
		int isHaveName = 1;
		int createFlag = 0;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			// 先得到在数据库不的文件夹路径
			// YHNetdisk netdiskStr=diskLogic.getNetdiskInfoById(dbConn, seqId);
			// String diskPathStr=netdiskStr.getDiskPath();
			// File file1=null;
			// if(diskPathStr!=null && !"".equals(diskPathStr)){
			// file1=new File(diskPathStr);
			// }

			YHNetdisk netdisk = (YHNetdisk) YHFOM.build(request.getParameterMap());

			String path = YHUtility.null2Empty(netdisk.getDiskPath()).trim();
//			List<YHNetdisk> list = diskLogic.getNetdisksInfo(dbConn, seqId);
//			if (list.size() > 0) {
//				for (YHNetdisk disk : list) {
//					if (disk.getDiskName().equals(netdisk.getDiskName())) {
//						isHaveName = 0;
//						break;
//					}
//				}
//			}
			boolean isHaveFlag = diskLogic.checkDiskPathLogic(dbConn, seqId, path);
			if (isHaveFlag) {
				isHaveName =0;
			}
			
			if (isHaveName != 0) {

				File file = new File(path);
				String filePath = file.getPath().trim();
				String diskPath = filePath.trim().replace("\\", "/");
				if (!YHUtility.isNullorEmpty(path.trim()) && file!=null) {

					boolean flag = diskLogic.createFolder(diskPath);
					if (flag == true) {
						netdisk.setDiskPath(diskPath + "/");
						createFlag = 1;
						Map map = new HashMap();
						map.put("seqId", seqId);
						map.put("diskName", netdisk.getDiskName());
						map.put("diskPath", netdisk.getDiskPath());
						map.put("diskNo", netdisk.getDiskNo());
						map.put("spaceLimit", netdisk.getSpaceLimit());
						map.put("orderBy", netdisk.getOrderBy());
						map.put("ascDesc", netdisk.getAscDesc());
						diskLogic.updateNetdiskSort(dbConn, map);
					}
				}

			}

			String data = "{isHaveName:\"" + isHaveName + "\",createFlag:\"" + createFlag + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "更新成功");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 删除信息,取消该共享目录
	 * 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delNetdiskFolderById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int seqId = Integer.parseInt(request.getParameter("seqId"));
		Connection dbConn = null;
		YHNetdisk netdisk = new YHNetdisk();
		YHNetdiskLogic disklLogic = new YHNetdiskLogic();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			netdisk.setSeqId(seqId);
			disklLogic.delNetdiskFolderInfoById(dbConn, netdisk);
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
	 * 根据id设置权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setPrivateById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHNetdiskLogic diskLogic = new YHNetdiskLogic();
		String sortId = request.getParameter("seqId");
		String action = request.getParameter("action");
		String idStr = request.getParameter("idStr");
		int seqId = 0;
		if (sortId != null) {
			seqId = Integer.parseInt(sortId);
		}
		if (idStr.replace("|", "").length() == 0) {
			idStr = "";
		}

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			Map map = new HashMap();
			map.put("SEQ_ID", seqId);
			if ("USER_ID".equals(action)) {
				// map.put("userId", idStr);
				diskLogic.updatePrivateById(dbConn, map, action, idStr);

			} else if ("MANAGE_USER".equals(action)) {
				// map.put("manageUser", idStr);
				diskLogic.updatePrivateById(dbConn, map, action, idStr);
			} else if ("NEW_USER".equals(action)) {
				// map.put("newUser", idStr);
				diskLogic.updatePrivateById(dbConn, map, action, idStr);
			} else if ("DOWN_USER".equals(action)) {
				// map.put("downUser", idStr);
				diskLogic.updatePrivateById(dbConn, map, action, idStr);
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
	 * 获取人员权限id串名字
	 * 
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
			YHNetdiskLogic diskLogic = new YHNetdiskLogic();
			Map map = new HashMap();
			map.put("SEQ_ID", seqId);
			String ids = "";
			String names = "";
			ids = diskLogic.selectManagerIds(dbConn, map, action);
			if (!ids.equals("")) {
				names = diskLogic.getNamesByIds(dbConn, map, action);
			}

			String data = "{user:\"" + ids + "\",userDesc:\"" + names + "\"}";
			// System.out.println(data);
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
			YHNetdiskLogic diskLogic = new YHNetdiskLogic();
			Map map = new HashMap();
			map.put("SEQ_ID", seqId);
			String ids = "";
			String names = "";
			ids = diskLogic.getRoleIds(dbConn, map, action);

			// System.out.println(ids);
			if (!ids.equals("")) {
				names = diskLogic.getRoleNamesByIds(dbConn, map, action);
			}
			String data = "{role:\"" + ids + "\",roleDesc:\"" + names + "\"}";
			// System.out.println(data);
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
			YHNetdiskLogic netdiskLogic = new YHNetdiskLogic();
			Map map = new HashMap();
			map.put("SEQ_ID", seqId);
			String ids = netdiskLogic.getDeptIds(dbConn, map, action);
			String names = "";
			// System.out.println(ids);
			if (!ids.equals("")) {
				names = netdiskLogic.getDeptByIds(dbConn, map, action);
			}
			String data = "{dept:\"" + ids + "\",deptDesc:\"" + names + "\"}";
			// System.out.println(data);
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
	 * 获取转存到网络硬盘的根目录信息
	 * 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getNetDiskList(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String seqIdStr = request.getParameter("seqId");
		String diskPath = request.getParameter("diskPath");
		String parentPath = request.getParameter("parentPath");
		String attachId = request.getParameter("attachId");
		String attachName = request.getParameter("attachName");
		String returnFlag = request.getParameter("returnFlag");
		String module = request.getParameter("module");

		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}

		if (diskPath == null) {
			diskPath = "";
		}
		if (parentPath == null) {
			parentPath = "";
		}
		if (returnFlag == null) {
			returnFlag = "";
		}
		if (module == null) {
			module = "";
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		boolean visitUserFlag = false;
		boolean visitRoleFlag = false;
		boolean visitDeptFlag = false;

		boolean newUserFlag = false;
		boolean newRoleFlag = false;
		boolean newDeptFlag = false;

		YHNetdiskLogic logic = new YHNetdiskLogic();
		List list = new ArrayList();
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			if ("back".equals(returnFlag.trim())) {
				YHNetdisk netdisk = logic.getNetdiskInfoById(dbConn, seqId);
				String filePath = YHUtility.null2Empty(netdisk.getDiskPath());
				File file = new File(filePath);
				File file2 = new File(diskPath);
				if (file.exists() && file2.exists()) {
					if (file.getAbsolutePath().equals(file2.getAbsolutePath())) {
						diskPath = "";
					} else {
						String parentPathStr = diskPath.replace('\\', '/');
						int pathStr = parentPathStr.lastIndexOf('/') - 1;
						if (pathStr != -1) {
							diskPath = parentPathStr.substring(0, parentPathStr.lastIndexOf('/'));
							diskPath = diskPath + "/";
						}
					}
				}
			}

			if ("".equals(diskPath)) {
				list = logic.getNetdiskFolderInfo(dbConn);
				if (list != null && list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						YHNetdisk netdisk = (YHNetdisk) list.get(i);
						Map diskMap = new HashMap();
						diskMap.put("SEQ_ID", netdisk.getSeqId());

						String visitUserStr = logic.selectManagerIds(dbConn, diskMap, "USER_ID");
						String visitRoleStr = logic.getRoleIds(dbConn, diskMap, "USER_ID");
						String visitDeptStr = logic.getDeptIds(dbConn, diskMap, "USER_ID");

						String newUserStr = logic.selectManagerIds(dbConn, diskMap, "NEW_USER");
						String newRoleStr = logic.getRoleIds(dbConn, diskMap, "NEW_USER");
						String newDeptStr = logic.getDeptIds(dbConn, diskMap, "NEW_USER");

						visitUserFlag = logic.getUserIdStr(loginUserSeqId, visitUserStr, dbConn);
						visitRoleFlag = logic.getRoleIdStr(loginUserRoleId, visitRoleStr, dbConn);
						visitDeptFlag = logic.getDeptIdStr(loginUserDeptId, visitDeptStr, dbConn);

						newUserFlag = logic.getUserIdStr(loginUserSeqId, newUserStr, dbConn);
						newRoleFlag = logic.getRoleIdStr(loginUserRoleId, newRoleStr, dbConn);
						newDeptFlag = logic.getDeptIdStr(loginUserDeptId, newDeptStr, dbConn);

						int visitFlag = 0;
						int newFlag = 0;
						if (visitUserFlag || visitRoleFlag || visitDeptFlag) {
							visitFlag = 1;
						}
						if (newUserFlag || newRoleFlag || newDeptFlag) {
							newFlag = 1;
						}

						if (visitFlag == 1 && newFlag == 1) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("seqId", String.valueOf(netdisk.getSeqId()));
							map.put("diskName", YHUtility.encodeSpecial(YHUtility.null2Empty(netdisk.getDiskName())));
							map.put("diskPath", YHUtility.null2Empty(netdisk.getDiskPath()));
							map.put("newUser", netdisk.getNewUser());
							map.put("managerUser", YHUtility.encodeSpecial(YHUtility.null2Empty(netdisk.getManageUser())));
							map.put("userId", netdisk.getUserId());
							map.put("diskNo", String.valueOf(netdisk.getDiskNo()));
							map.put("spaceLimit", String.valueOf(netdisk.getSpaceLimit()));
							map.put("orderBy", netdisk.getOrderBy());
							map.put("ascDesc", netdisk.getAscDesc());
							map.put("downUser", netdisk.getDownUser());
							returnList.add(map);

						}

					}
				}
			} else {
				File file = new File(diskPath);
				if (file.exists()) {
					parentPath = file.getAbsolutePath().replace('\\', '/');
					File[] files = file.listFiles();
					if (files!=null) {
						for (int i = 0; i < files.length; i++) {
							File f = files[i];
							if (f.isDirectory() && !"tdoa_cache".equals(f.getName().trim())) {
								Map<String, String> map = new HashMap<String, String>();
								map.put("seqId", String.valueOf(seqId));
								map.put("diskName", f.getName());
								map.put("diskPath", f.getAbsolutePath().replace('\\', '/'));
								returnList.add(map);
							}
						}
					}
				}
			}
			request.setAttribute("attachId", attachId);
			request.setAttribute("attachName", attachName);
			request.setAttribute("module", module);

			request.setAttribute("seqId", seqId);
			request.setAttribute("diskPath", diskPath);
			request.setAttribute("parentPath", parentPath);

			request.setAttribute("diskList", returnList);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/funcs/savefile/netdisk.jsp";
	}

	/**
	 * 网络硬盘转存
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String transferNetdisk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String diskPath = request.getParameter("diskPath"); // d:/bjfaoitc/
		String attachId = request.getParameter("attachId"); // 1007_a4af7859d19ce575b052dda815fa1d3f
		String attachName = request.getParameter("attachName"); // 新建文档sd^.ppt
		String subject = request.getParameter("subject"); // 新建文档sd^
		String module = request.getParameter("module"); // file_folder

		if (module == null) {
			module = "";
		}
		if (diskPath == null) {
			diskPath = "";
		}
		if (subject == null) {
			subject = "";
		}

		String separator = File.separator;
		String filePath = YHSysProps.getAttachPath() + separator + module + separator; // D:\project\yh\attach\file_folder\
		YHNetdiskLogic logic = new YHNetdiskLogic();

		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();
			String data = logic.transferNetdisk(dbConn, diskPath, attachId, attachName, subject, filePath);

			request.setAttribute(YHActionKeys.RET_MSRG, "文件转存完毕！");
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
	 * 列出文件夹及文件信息（从网络硬盘中选择附件）
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String selectFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String diskPath = request.getParameter("diskPath");
		String seqIdStr = request.getParameter("seqId");
		String returnFlag = request.getParameter("returnFlag");
		String headName = request.getParameter("headName");

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

		String parentPath = "";
		String headerTitle = "";

		if (diskPath == null) {
			diskPath = "";
		}
		if (returnFlag == null) {
			returnFlag = "";
		}
		if (headName == null) {
			headName = "";
		}
		int seqId = 0;
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}

		YHNetdiskLogic logic = new YHNetdiskLogic();
		List list = new ArrayList();

		String dirFlag = "";
		boolean flag = false;
		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		boolean visitUserFlag = false;
		boolean visitRoleFlag = false;
		boolean visitDeptFlag = false;

		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();
			if ("back".equals(returnFlag)) {
				YHNetdisk netdisk = logic.getNetdiskInfoById(dbConn, seqId);
				String filePath = YHUtility.null2Empty(netdisk.getDiskPath());
				File file = new File(filePath);
				File file2 = new File(diskPath);
				if (file.exists() && file2.exists()) {
					if (file.getAbsolutePath().equals(file2.getAbsolutePath())) {
						diskPath = "";
					} else {
						String parentPathStr = diskPath.replace('\\', '/');
						int pathStr = parentPathStr.lastIndexOf('/') - 1;
						if (pathStr != -1) {
							diskPath = parentPathStr.substring(0, parentPathStr.lastIndexOf('/'));
						}
					}
				}
			}
			StringBuffer sb = new StringBuffer();
			if ("".equals(diskPath)) {
				list = logic.getNetdiskFolderInfo(dbConn);
				if (list != null && list.size() != 0) {
					sb.append("[");
					for (int i = 0; i < list.size(); i++) {
						YHNetdisk netdisk = (YHNetdisk) list.get(i);
						Map diskMap = new HashMap();
						diskMap.put("SEQ_ID", netdisk.getSeqId());

						String visitUserStr = logic.selectManagerIds(dbConn, diskMap, "USER_ID");
						String visitRoleStr = logic.getRoleIds(dbConn, diskMap, "USER_ID");
						String visitDeptStr = logic.getDeptIds(dbConn, diskMap, "USER_ID");

						visitUserFlag = logic.getUserIdStr(loginUserSeqId, visitUserStr, dbConn);
						visitRoleFlag = logic.getRoleIdStr(loginUserRoleId, visitRoleStr, dbConn);
						visitDeptFlag = logic.getDeptIdStr(loginUserDeptId, visitDeptStr, dbConn);
						int visitFlag = 0;
						if (visitUserFlag || visitRoleFlag || visitDeptFlag) {
							visitFlag = 1;
						}
						if (visitFlag == 1) {
							String diskName = netdisk.getDiskName() == null ? "" : netdisk.getDiskName();
							String diskPathStr = netdisk.getDiskPath() == null ? "" : netdisk.getDiskPath(); // D:/aa/
							String[] diskPathSplit = diskPathStr.split("/");
							dirFlag = "isDir";
							sb.append("{");
							sb.append("seqId:\"" + netdisk.getSeqId() + "\"");
							sb.append(",diskName:\"" + diskName + "\"");
							sb.append(",diskPath:\"" + diskPathStr + "\"");
							sb.append(",diskPathFlag:\"" + diskPath + "\"");
							sb.append(",headerTitle:\"" + "" + "\"");
							sb.append(",dirFlag:\"" + dirFlag + "\"");
							sb.append(",netFlag:\"" + "" + "\"");

							sb.append(",ext_filter:\"" + ext_filter + "\"");
							sb.append(",div_id:\"" + div_id + "\"");
							sb.append(",dir_field:\"" + dir_field + "\"");
							sb.append(",name_field:\"" + name_field + "\"");
							sb.append(",type_field:\"" + type_field + "\"");
							sb.append(",multi_select:\"" + multi_select + "\"");
							sb.append("},");
							flag = true;
						}
					}
					if (flag) {
						sb.deleteCharAt(sb.length() - 1);
					}
					sb.append("]");
				} else {
					sb.append("[]");
				}
			} else {
				String diskName = "";
				File file = new File(diskPath);
				if (file.exists()) {
					parentPath = file.getAbsolutePath().replace('\\', '/');
					if ("".equals(headName)) {
						YHNetdisk netdisk = logic.getNetdiskInfoById(dbConn, seqId);
						diskName = netdisk.getDiskName();
						headerTitle = diskName + "/" + file.getName();
					} else {
						headerTitle = headName + "/" + file.getName();
					}
					sb.append("[");
					if (file.isDirectory()) {
						File[] files = file.listFiles();
						if (files.length != 0) {
							for (int i = 0; i < files.length; i++) {
								File f = files[i];

								if (f.isDirectory()) {
									dirFlag = "isDir";
								} else {
									dirFlag = "isFile";
								}
								sb.append("{");
								sb.append("seqId:\"" + seqId + "\"");
								sb.append(",diskName:\"" + f.getName() + "\"");
								sb.append(",diskPath:\"" + f.getAbsolutePath().replace('\\', '/') + "\"");
								// sb.append(",diskPath:\"" + diskPath+file.getName() + "\"");
								// System.out.println(diskPath+f.getName());
								sb.append(",parentPath:\"" + parentPath + "\"");
								sb.append(",dirFlag:\"" + dirFlag + "\"");
								sb.append(",headerTitle:\"" + headerTitle + "\"");
								sb.append(",diskPathFlag:\"" + diskPath + "\"");
								sb.append(",netFlag:\"" + "next" + "\"");

								sb.append(",ext_filter:\"" + ext_filter + "\"");
								sb.append(",div_id:\"" + div_id + "\"");
								sb.append(",dir_field:\"" + dir_field + "\"");
								sb.append(",name_field:\"" + name_field + "\"");
								sb.append(",type_field:\"" + type_field + "\"");
								sb.append(",multi_select:\"" + multi_select + "\"");

								sb.append("},");
								flag = true;
							}
						} else {
							sb.append("{");
							sb.append("seqId:\"" + seqId + "\"");
							sb.append(",diskName:\"" + "" + "\"");
							sb.append(",diskPath:\"" + "" + "\"");
							sb.append(",parentPath:\"" + parentPath + "\"");
							sb.append(",dirFlag:\"" + "noFile" + "\"");
							sb.append(",headerTitle:\"" + headerTitle + "\"");
							sb.append(",netFlag:\"" + "next" + "\"");
							sb.append("},");
							flag = true;
						}
						if (flag) {
							sb.deleteCharAt(sb.length() - 1);
						}
						sb.append("]");
					}
				} else {
					sb.append("[]");
				}
			}
			request.setAttribute("headerTitle", headerTitle);
			request.setAttribute(YHActionKeys.RET_MSRG, "文件转存完毕！");
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 *判断路径是否已存在
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String checkDiskPath(HttpServletRequest request ,HttpServletResponse response) throws Exception{
		String seqIdStr = request.getParameter("seqId");
		String pathName = request.getParameter("diskPath");
		int isHaveFlag = 0;
		int seqId = 0;
		if (YHUtility.isNumber(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		if (YHUtility.isNullorEmpty(pathName)) {
			pathName = "";
		}
		YHNetdiskLogic logic = new YHNetdiskLogic();
		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();
			boolean isHave = logic.checkDiskPathLogic(dbConn,seqId,pathName);
			if (isHave) {
				isHaveFlag = 1;
			}
			String data = "{isHaveFlag:\"" + isHaveFlag + "\" }";
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
	
	
	

}
