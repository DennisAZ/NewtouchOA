package yh.core.funcs.personfolder.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.personfolder.data.YHFileSort;
import yh.core.funcs.personfolder.logic.YHPersonFolderLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;

public class YHPersonFolderAct {

	/**
	 * 获取个人文件柜目录树
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
		if (idStr != null && !"".equals(idStr) && !"root".equals(idStr)) {
			id = Integer.parseInt(idStr);
		}
		String sortType = "4";

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		YHPersonFolderLogic logic = new YHPersonFolderLogic();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			Map map = new HashMap();
			StringBuffer sb = new StringBuffer("[");

			if (id == 0 && !"root".equals(idStr)) {

				sb.append("{");
				sb.append("nodeId:\"" + "root" + "\"");
				sb.append(",name:\"" + "根目录" + "\"");
				sb.append(",isHaveChild:" + 1 + "");
				sb.append("}");
				sb.append("]");

			} else {

				boolean userFlag = false;

				boolean isHave = false;

				// Map parentMap = new HashMap();
				// parentMap.put("SORT_PARENT", id);
				// parentMap.put("SORT_TYPE", sortType);

				String[] filters = { "SORT_PARENT=" + id + " and SORT_TYPE='" + sortType + "' order by SORT_NO,SORT_NAME" };
				List<YHFileSort> parentList = logic.getFileSorts(dbConn, filters);
				if (parentList != null && parentList.size() > 0) {
					// Map sortTypeMap = new HashMap();
					for (YHFileSort fileFolder : parentList) {
						String userPrivs = logic.getUserId(dbConn, fileFolder.getSeqId(), "USER_ID");
						userFlag = logic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
						if (userFlag) {
							//System.out.println(userFlag);
							boolean shareFlag = false;
							String sahreIdStr = YHUtility.null2Empty(fileFolder.getShareUser());
							if (!"".equals(sahreIdStr.trim())) {
								// shareFlag=logic.getUserIdStr(loginUserSeqId, sahreIdStr,
								// dbConn);
								shareFlag = true;
							}
							String imgAddress = "\"/yh/core/styles/style1/img/dtree/folder.gif\"";
							if (shareFlag) {
								imgAddress = "\"/yh/core/styles/style1/img/endnode_share.gif\"";
							}

							int isHaveChild = logic.isHaveChild(dbConn, fileFolder.getSeqId());

							String sortName = fileFolder.getSortName();
							sortName = sortName.replaceAll("[\n-\r]", "<br>");
							sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
							sortName = sortName.replace("\"", "\\\"");

							sb.append("{");
							sb.append("nodeId:\"" + fileFolder.getSeqId() + "\"");
							sb.append(",name:\"" + sortName + "\"");
							sb.append(",isHaveChild:" + isHaveChild + "");
							sb.append(",imgAddress:" + imgAddress + "");
							sb.append("},");
							isHave = true;

						}

						// int seqId = fileFolder.getSeqId();
						// sortTypeMap.put("SORT_TYPE", sortType);
						// List<YHFileSort> typeList=logic.getFileSorts(dbConn,
						// sortTypeMap);
						// if (typeList!=null &&typeList.size()>0) {
						// for(YHFileSort typeSort:typeList){
						// String userPrivs=logic.getUserId(dbConn, typeSort.getSeqId(),
						// "USER_ID");
						// userFlag=logic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
						// if (userFlag) {
						// System.out.println(userFlag);
						//									
						// int isHaveChild = logic.isHaveChild(dbConn, typeSort.getSeqId());
						// sb.append("{");
						// sb.append("nodeId:\"" + typeSort.getSeqId() + "\"");
						// sb.append(",name:\"" + typeSort.getSortName() + "\"");
						// sb.append(",isHaveChild:" + isHaveChild + "");
						// sb.append("},");
						//									
						//									
						// }
						//								
						// }
						//							
						// }

						// int nodeId = fileFolder.getSeqId();
						// String sortName = fileFolder.getSortName();
						// int isHaveChild = logic.isHaveChild(dbConn,
						// fileFolder.getSeqId());
						// sb.append("{");
						// sb.append("nodeId:\"" + nodeId + "\"");
						// sb.append(",name:\"" + sortName + "\"");
						// sb.append(",isHaveChild:" + isHaveChild + "");
						// sb.append("},");

						// isHave = true;

					}

				}
				if (isHave) {
					sb.deleteCharAt(sb.length() - 1);

				}
				sb.append("]");
			}

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 从个人文件柜选择附件目录树
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPersonTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idStr = request.getParameter("id");
		String sortIdStr = request.getParameter("seqId");
		int id = 0;
		if (idStr != null && !"".equals(idStr)) {
			id = Integer.parseInt(idStr);
		}
		String sortType = "4";

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		YHPersonFolderLogic logic = new YHPersonFolderLogic();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			Map map = new HashMap();
			StringBuffer sb = new StringBuffer("[");

			// if (id==0) {

			boolean userFlag = false;

			boolean isHave = false;

			// Map parentMap = new HashMap();
			// parentMap.put("SORT_PARENT", id);
			// parentMap.put("SORT_TYPE", sortType);

			String[] filters = { "SORT_PARENT=" + id + " and SORT_TYPE='" + sortType + "' order by SORT_NO,SORT_NAME" };
			List<YHFileSort> parentList = logic.getFileSorts(dbConn, filters);
			if (parentList != null && parentList.size() > 0) {
				// Map sortTypeMap = new HashMap();
				for (YHFileSort fileFolder : parentList) {
					String userId = logic.getUserId(dbConn, fileFolder.getSeqId(), "USER_ID");
					userFlag = logic.getUserIdStr(loginUserSeqId, userId, dbConn);
					if (userFlag) {
						//System.out.println(userFlag);
						// boolean shareFlag = false;
						// String sahreIdStr = fileFolder.getShareUser();
						// if (!"".equals(sahreIdStr.trim())) {
						// // shareFlag=logic.getUserIdStr(loginUserSeqId, sahreIdStr,
						// // dbConn);
						// shareFlag = true;
						// }
						String imgAddress = "\"/yh/core/styles/style1/img/dtree/folder.gif\"";
						// if (shareFlag) {
						// imgAddress = "\"/yh/core/styles/style1/img/endnode_share.gif\"";
						// }

						int isHaveChild = logic.isHaveChild(dbConn, fileFolder.getSeqId());
						String sortName = fileFolder.getSortName();
						sortName = sortName.replaceAll("[\n-\r]", "<br>");
						sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
						sortName = sortName.replace("\"", "\\\"");

						sb.append("{");
						sb.append("nodeId:\"" + fileFolder.getSeqId() + "\"");
						sb.append(",name:\"" + sortName + "\"");
						sb.append(",isHaveChild:" + isHaveChild + "");
						sb.append(",imgAddress:" + imgAddress + "");
						sb.append("},");
						isHave = true;

					}

				}

			}
			if (isHave) {
				sb.deleteCharAt(sb.length() - 1);

			}
			sb.append("]");

			// }else {
			//        
			//        
			//        
			// }

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 共享目录树
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getShareTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idStr = request.getParameter("id");
		String sortIdStr = request.getParameter("seqId");
		int id = 0;
		if (idStr != null && !"".equals(idStr)) {
			id = Integer.parseInt(idStr);
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();
		String sortType = "4";

		YHPersonFolderLogic logic = new YHPersonFolderLogic();

		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			StringBuffer sb = new StringBuffer("[");
			if (id == 0) {
				boolean userFlag = false;

				boolean isHave = false;

				Map parentMap = new HashMap();
				// parentMap.put("SORT_PARENT", id);
				parentMap.put("SORT_TYPE", sortType);
				List<YHFileSort> parentList = logic.getFileSorts(dbConn, parentMap);
				if (parentList != null && parentList.size() > 0) {
					Map sortTypeMap = new HashMap();
					for (YHFileSort fileFolder : parentList) {
						boolean shareFlag = false;
						// String userIdStr = logic.getUserId(dbConn, fileFolder.getSeqId(),
						// "USER_ID");
						// userFlag = logic.getUserIdStr(loginUserSeqId, userIdStr, dbConn);
						// //判断当前登录人是否为创建该文件夹的用户

						String sahreIdStr = YHUtility.null2Empty(fileFolder.getShareUser());

						if (sahreIdStr != null && !"".equals(sahreIdStr.trim())) {
							shareFlag = logic.getUserIdStr(loginUserSeqId, sahreIdStr, dbConn);
							// shareFlag = true;
						}

						if (shareFlag) {
							String shareUserNames = logic.getNameBySeqIdStr(YHUtility.null2Empty(fileFolder.getUserId()), dbConn);
							if (shareUserNames == null) {
								shareUserNames = "";
							}

							String sortName = fileFolder.getSortName();
							sortName = sortName.replaceAll("[\n-\r]", "<br>");
							sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
							sortName = sortName.replace("\"", "\\\"");

							String shareName = sortName + "(" + shareUserNames + ")";

							String imgAddress = "\"/yh/core/styles/style1/img/endnode_share.gif\"";
							int isHaveChild = logic.isHaveChild(dbConn, fileFolder.getSeqId());

							// String extData = "";
							sb.append("{");
							sb.append("nodeId:\"" + fileFolder.getSeqId() + "\"");
							sb.append(",name:\"" + shareName + "\"");
							sb.append(",isHaveChild:" + isHaveChild + "");
							sb.append(",imgAddress:" + imgAddress + "");
							sb.append(",extData:\"" + fileFolder.getSeqId() + "\"");
							sb.append("},");
							isHave = true;

						}
					}
				}
				if (isHave) {
					sb.deleteCharAt(sb.length() - 1);

				}
				sb.append("]");

			} else {

				boolean userFlag = false;

				boolean isHave = false;

				Map parentMap = new HashMap();
				parentMap.put("SORT_PARENT", id);
				parentMap.put("SORT_TYPE", sortType);
				List<YHFileSort> parentList = logic.getFileSorts(dbConn, parentMap);
				if (parentList != null && parentList.size() > 0) {
					Map sortTypeMap = new HashMap();
					for (YHFileSort fileFolder : parentList) {
						String userPrivs = logic.getUserId(dbConn, fileFolder.getSeqId(), "USER_ID");
						userFlag = logic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
						//System.out.println(userFlag);
						String imgAddress = "\"/yh/core/styles/style1/img/dtree/folder.gif\"";

						int isHaveChild = logic.isHaveChild(dbConn, fileFolder.getSeqId());
						String sortName = fileFolder.getSortName();
						sortName = sortName.replaceAll("[\n-\r]", "<br>");
						sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
						sortName = sortName.replace("\"", "\\\"");

						sb.append("{");
						sb.append("nodeId:\"" + fileFolder.getSeqId() + "\"");
						sb.append(",name:\"" + sortName + "\"");
						sb.append(",isHaveChild:" + isHaveChild + "");
						sb.append(",imgAddress:" + imgAddress + "");
						sb.append(",extData:\"" + fileFolder.getSeqId() + "\"");
						sb.append("},");
						isHave = true;

					}

				}
				if (isHave) {
					sb.deleteCharAt(sb.length() - 1);

				}
				sb.append("]");

			}

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
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
		YHPersonFolderLogic fileSortLogic = new YHPersonFolderLogic();
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
			int sortParnet = 0;
			if (fileSort != null) {
				sortParnet = fileSort.getSortParent();
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, String.valueOf(sortParnet));
			request.setAttribute(YHActionKeys.RET_DATA, data);

		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 取出共享文件夹路径名
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getShareFolderNameById(HttpServletRequest request, HttpServletResponse response) throws Exception {

		YHPersonFolderLogic logic = new YHPersonFolderLogic();
		int seqId = Integer.parseInt(request.getParameter("seqId"));
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHFileSort fileSort = logic.getSortNameById(dbConn, seqId);
			String folderName = YHUtility.null2Empty(fileSort.getSortName());
			if (folderName == null) {
				folderName = "";
			}

			// 处理特殊字符
			folderName = folderName.replaceAll("[\\\\/:*?\"<>|]", "");
			folderName = folderName.replaceAll("[\n-\r]", "<br>");
			folderName = folderName.replace("\"", "\\\"");

			String sortName = folderName + "(共享文件柜)";

			String data = "[{sortName:\"" + sortName + "\"}]";

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			// request.setAttribute(YHActionKeys.RET_MSRG,
			// String.valueOf(sortParnet));
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
		Connection dbConn = null;
		YHPersonFolderLogic logic = new YHPersonFolderLogic();
		String seqId = request.getParameter("seqId");

		String sortType = "4";
		int sortParent = 0;
		if (seqId != null) {
			sortParent = Integer.parseInt(seqId);
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHFileSort fileSort = (YHFileSort) YHFOM.build(request.getParameterMap());

			String folderName = fileSort.getSortName();

			int isHaveFlag = 0;
			boolean isHave = false;
			int nodeId = 0;
			String sortName = "";

			isHave = logic.checkFolderName(dbConn, sortParent, loginUserSeqId, folderName);

			if (!isHave) {

				fileSort.setSortParent(sortParent);
				fileSort.setUserId(String.valueOf(loginUserSeqId));
				fileSort.setSortType(sortType);
				logic.saveFileSortInfo(dbConn, fileSort);
				YHFileSort fileSort2 = new YHFileSort();
				fileSort2 = logic.getMaxSeqId(dbConn);
				nodeId = fileSort2.getSeqId();
				sortName = fileSort2.getSortName();
				
				sortName = sortName.replaceAll("[\n-\r]", "<br>");
				sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
				sortName = sortName.replace("\"", "\\\"");
				
			} else {
				isHaveFlag = 1;
			}

			String date = "[{nodeId:\"" + nodeId + "\",sortName:\"" + sortName + "\",isHaveFlag:\"" + isHaveFlag + "\" }]";
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
	 * 验证文件夹名是否存在
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String checkFolderName(HttpServletRequest request, HttpServletResponse response) throws Exception {

		YHPersonFolderLogic logic = new YHPersonFolderLogic();
		String seqId = request.getParameter("seqId");

		String sortType = "4";
		int sortParent = 0;
		if (seqId != null) {
			sortParent = Integer.parseInt(seqId);
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHFileSort fileSort = (YHFileSort) YHFOM.build(request.getParameterMap());
			String folderName = fileSort.getSortName();

			int isHaveFlag = 0;
			boolean isHave = false;
			int nodeId = 0;
			String sortName = "";

			if (sortParent == 0) {

			} else {
				String[] filters = { "SORT_PARENT=" + sortParent };
				List<YHFileSort> parentList = logic.getFileSorts(dbConn, filters);
				if (parentList != null && parentList.size() > 0) {
					for (YHFileSort fileFolder : parentList) {
						String sortNameString = YHUtility.null2Empty(fileFolder.getSortName());
						if (folderName.trim().equals(sortNameString.trim())) {
							isHave = true;
							isHaveFlag = 1;
							break;
						}

					}

				}

			}

			String date = "{nodeId:\"" + nodeId + "\",sortName:\"" + sortName + "\",isHaveFlag:\"" + isHaveFlag + "\" }";
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
	 * 通过id获取信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getFileSortInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHPersonFolderLogic fileSortLogic = new YHPersonFolderLogic();
		StringBuffer sb = new StringBuffer("[");
		String seqId = request.getParameter("seqId");
		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			Map map = new HashMap();
			map.put("SEQ_ID", seqId);
			// map.put("USER_ID", loginUserSeqId);

			// YHFileSort fileSort = fileSortLogic.getFileSortInfoById(dbConn, seqId);
			YHFileSort fileSort = fileSortLogic.getFileSortInfoById(dbConn, map);
			String sortNo = fileSort.getSortNo() == null ? "" : fileSort.getSortNo();
			String sortName = YHUtility.null2Empty(fileSort.getSortName());
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
	 * 更新编辑子文件夹信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateFileSortInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {

		YHPersonFolderLogic fileSortLogicc = new YHPersonFolderLogic();
		int seqId = Integer.parseInt(request.getParameter("seqId"));
		String sortNo = request.getParameter("sortNo");
		String sortName = request.getParameter("sortName");

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			boolean isHave = false;
			int isHaveFlag = 0;
			int nodeId = 0;
			String folderName = "";

			isHave = fileSortLogicc.checkEditFolder(dbConn, seqId, loginUserSeqId, sortName);

			if (isHave) {
				isHaveFlag = 1;
			} else {

				YHFileSort fileSort = fileSortLogicc.getFolderInfoById(dbConn, seqId);
				fileSort.setSortNo(sortNo);
				fileSort.setSortName(sortName);
				fileSortLogicc.updateSingleObj(dbConn, fileSort);
				nodeId = fileSort.getSeqId();
				folderName = fileSort.getSortName();
				
//			String sortName = fileFolder.getSortName();
				folderName = folderName.replaceAll("[\n-\r]", "<br>");
				folderName = folderName.replaceAll("[\\\\/:*?\"<>|]", "");
				folderName = folderName.replace("\"", "\\\"");
				
			}

			String date = "[{nodeId:\"" + nodeId + "\",sortName:\"" + folderName + "\",isHaveFlag:\"" + isHaveFlag + "\" }]";
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
	 * 设置共享、管理权限
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setSharePriv(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sortId = request.getParameter("sortId");
		String shareUserId = request.getParameter("shareUserId");
		String manageUserId = request.getParameter("manageUserId");

		int seqId = 0;
		if (sortId != null) {
			seqId = Integer.parseInt(sortId);
		}
		if (shareUserId == null) {
			shareUserId = "";
		}
		if (manageUserId == null) {
			manageUserId = "";
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		YHPersonFolderLogic logic = new YHPersonFolderLogic();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			String unloginUserIdStr = logic.getNoLoginIdStr(dbConn, loginUserSeqId, shareUserId);
			String unloginManaId = logic.getNoLoginIdStr(dbConn, loginUserSeqId, manageUserId);

			int shareFlag = 0;

			YHFileSort fileSort = logic.getFolderInfoById(dbConn, seqId);
			if (fileSort != null) {
				fileSort.setShareUser(unloginUserIdStr);
				fileSort.setManageUser(unloginManaId);
				logic.updateSingleObj(dbConn, fileSort);
				if (unloginUserIdStr != null && !"".equals(unloginUserIdStr)) {
					shareFlag = 1;
				}

			}

			String data = "{shareFlag:\"" + shareFlag + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "更新成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取共享人员名字串
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPrivUserName(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}

		YHPersonFolderLogic logic = new YHPersonFolderLogic();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			String shareIdStr = "";
			String manageUserIdStr = "";

			String shareUserNames = "";
			String manageUserNames = "";

			YHFileSort fileSort = logic.getFolderInfoById(dbConn, seqId);
			if (fileSort != null) {
				shareIdStr = fileSort.getShareUser();
				manageUserIdStr = fileSort.getManageUser();

				if (!"".equals(shareIdStr)) {
					shareUserNames = logic.getNameBySeqIdStr(shareIdStr, dbConn);
				}
				if (!"".equals(manageUserIdStr)) {
					manageUserNames = logic.getNameBySeqIdStr(manageUserIdStr, dbConn);
				}
			}

			String data = "{shareUser:\"" + shareIdStr + "\",shareUserDesc:\"" + shareUserNames + "\",manageUser:\"" + manageUserIdStr
					+ "\",manageUserDesc:\"" + manageUserNames + "\"}";

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
	 * 递归删除文件夹及下的所有文件信息
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delFileSortInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int seqId = Integer.parseInt(request.getParameter("seqId"));
		Connection dbConn = null;
		YHFileSort fileSort = null;
		YHPersonFolderLogic fileSortLogic = new YHPersonFolderLogic();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			//System.out.println("dbConn>>>>>>>>>>del>>>>" + dbConn);
			fileSort = new YHFileSort();
			fileSort.setSeqId(seqId);
			fileSortLogic.delFileSortInfoById(dbConn, fileSort);
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
	 * 获取管理权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getManagePrivteById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");

		int seqId = 0;
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		// int loginUserDeptId = loginUser.getDeptId();
		// String loginUserRoleId = loginUser.getUserPriv();

		YHPersonFolderLogic logic = new YHPersonFolderLogic();

		Connection dbConn = null;
		try {
			YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requesttDbConn.getSysDbConn();

			YHFileSort fileSort = logic.getFolderInfoById(dbConn, seqId);
			String manageIdStr = "";
			if (fileSort != null) {

				manageIdStr = fileSort.getManageUser();
			}

			boolean privFlag = logic.getUserIdStr(loginUserSeqId, manageIdStr, dbConn);

			int managePriv = 0;
			if (privFlag) {
				managePriv = 1;
			}

			String data = "{managePriv:\"" + managePriv + "\"}";

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
	 * 复制文件夹
	 * 
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

			request.getSession().setAttribute("perActionStr", action);
			request.getSession().setAttribute("perFolderSeqId", seqId);
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
	 * 粘贴文件夹
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String pasteFolder(HttpServletRequest request, HttpServletResponse response) throws Exception {

		YHPersonFolderLogic fileSortLogic = new YHPersonFolderLogic();
		String action = (String) request.getSession().getAttribute("perActionStr");
		String seqId = (String) request.getSession().getAttribute("perFolderSeqId"); // 点击复制时的文件夹seqId
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
//				sortName = YHUtility.null2Empty(fileSort3.getSortName());
				Map<Object, Object> nodeNameMap = new HashMap<Object, Object>();
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
				nullFlag=1;
			}
			data = "{nodeId:\"" + nodeId 
			+ "\",isHaveChild:\"" + isHaveChild 
			+ "\",sortName:\"" + YHUtility.encodeSpecial(sortName) 
			+ "\",seqId:\""	+ seqId 
			+ "\",nullFlag:\""	+ nullFlag 
			+ "\",action:\"" + action + "\" }";

			request.getSession().setAttribute("perActionStr", "");
			request.getSession().setAttribute("perFolderSeqId", "");

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
	 * 获取转存到个人文件柜的根目录树信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPersonFolderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
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

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();

		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		YHPersonFolderLogic logic = new YHPersonFolderLogic();
		String sortType = "4";

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			int inIt = 0;
			if (seqId != 0) {
				inIt = 1;
			}

			YHFileSort fileSort = logic.getFileSortInfoById(dbConn, String.valueOf(seqId));
			int parentIdStr = 0;
			if (fileSort != null) {
				parentIdStr = fileSort.getSortParent();
			}

			String[] filters = { "SORT_PARENT=" + seqId + " and SORT_TYPE='" + sortType + "' order by SORT_NO,SORT_NAME" };
			List<YHFileSort> parentList = logic.getFileSorts(dbConn, filters);
			if (seqId == 0) {
				Map<String, String> defaulMap = new HashMap<String, String>();
				defaulMap.put("seqId", String.valueOf(0));
				defaulMap.put("sortName", "根目录");
				returnList.add(defaulMap);

			}

			if (parentList != null && parentList.size() > 0) {
				boolean userFlag = false;
				for (YHFileSort fileFolder : parentList) {
					String userPrivs = logic.getUserId(dbConn, fileFolder.getSeqId(), "USER_ID");
					userFlag = logic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);

					if (userFlag) {
						Map<String, String> sortMap = new HashMap<String, String>();

						sortMap.put("seqId", String.valueOf(fileFolder.getSeqId()));
						sortMap.put("sortName", fileFolder.getSortName());
						sortMap.put("sortParent", String.valueOf(fileFolder.getSortParent()));
						returnList.add(sortMap);

					}

				}
			}

			request.setAttribute("attachId", attachId);
			request.setAttribute("attachName", attachName);
			request.setAttribute("module", module);
			request.setAttribute("inIt", inIt);

			request.setAttribute("seqId", seqId);
			request.setAttribute("parentId", parentIdStr);

			request.setAttribute("fileSortList", returnList);

		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/funcs/savefile/personFolderList.jsp";
	}

}
