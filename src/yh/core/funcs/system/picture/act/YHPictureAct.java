/**
 * 
 */
package yh.core.funcs.system.picture.act;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.system.picture.data.YHPicture;
import yh.core.funcs.system.picture.logic.YHPictureLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;

public class YHPictureAct {

	/**
	 * 新建图片目录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addPicSortInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHPictureLogic pictureLogic = new YHPictureLogic();
		YHPicture picture = new YHPicture();
		String toDeptId = request.getParameter("dept");
		String toPrivId = request.getParameter("role");
		String toUserId = request.getParameter("user");
		String picName = request.getParameter("picName");
		String picPath = request.getParameter("picPath");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String isPath = "";
			if (picPath != null && picPath.trim().length() > 0) {
				String path = picPath.replace("\\", "/");
				File file = new File(picPath.trim());
				if (file.exists()) {
					picture.setToDeptId(toDeptId);
					picture.setToPrivId(toPrivId);
					picture.setToUserId(toUserId);
					picture.setPicName(picName);
					picture.setPicPath(path);
					pictureLogic.savePicSortInfo(dbConn, picture);
				} else {
					isPath = "isNone";
				}
			} else {
				isPath = "isNone";
			}

			String data = "{isPath:\"" + isPath + "\"}";

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
	 * 功能：获取图片目录信息
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPicSortInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		StringBuffer sb = new StringBuffer("[");
		List<YHPicture> pictures = new ArrayList<YHPicture>();
		YHPictureLogic pictureLogic = new YHPictureLogic();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			//System.out.println("dbConn>>>>>>>>>>>>>>..." + dbConn);
			Map map = new HashMap();
			pictures = pictureLogic.getPicSortInfo(dbConn);
			StringBuffer buffer = new StringBuffer();
			String ids = "";
			String userName = "";
			String roleName = "";
			String deptName = "";
			if (pictures.size() > 0) {
				for (int i = 0; i < pictures.size(); i++) {
					YHPicture picture = pictures.get(i);
					String picName = picture.getPicName() == null ? "" : picture.getPicName();
					picName = picName.replaceAll("[\n-\r]", "<br>");
					picName = picName.replaceAll("[\\\\/:*?\"<>|]", "");
					picName = picName.replace("\"", "\\\"");
					
					
					String picPath = picture.getPicPath() == null ? "" : picture.getPicPath();
					String toDeptId = picture.getToDeptId() == null ? "" : picture.getToDeptId();
					String[] actions = new String[] { "userId", "roleId", "deptId" };

					for (int j = 0; j < actions.length; j++) {
						map.put("SEQ_ID", picture.getSeqId());
						if ("userId".equals(actions[0])) {
							ids = picture.getToUserId();
							if (ids == null) {
								ids = "";
							}
							userName = pictureLogic.getNamesByIds(dbConn, map, "userId");
						}
						if ("roleId".equals(actions[1])) {
							ids = picture.getToPrivId();
							if (ids == null) {
								ids = "";
							}
							roleName = pictureLogic.getNamesByIds(dbConn, map, "roleId");
						}
						if ("deptId".equals(actions[2])) {
							ids = picture.getToDeptId();
							if (ids == null) {
								ids = "";
							}
							if ("0".equals(ids.trim()) || "ALL_DEPT".equals(ids.trim())) {
								deptName = "全体部门";
							} else {
								deptName = pictureLogic.getNamesByIds(dbConn, map, "deptId");
							}
						}

					}
					// deptName="<font color=#0000FF><b>部门:</b></font>"+deptName+"<br>";
					// roleName="<font color=#0000FF><b>角色:</b></font>"+roleName+"<br>";
					// userName="<font color=#0000FF><b>人员:</b></font>"+userName;

					sb.append("{");
					sb.append("sqlId:\"" + picture.getSeqId() + "\"");
					sb.append(",picName:\"" + picName + "\"");
					sb.append(",picPath:\"" + picPath + "\"");
					sb.append(",deptName:\"" + deptName + "\"");
					sb.append(",roleName:\"" + roleName + "\"");
					sb.append(",userName:\"" + userName + "\"");
					sb.append("},");
				}

				sb.deleteCharAt(sb.length() - 1);
				sb.append("]");
			} else {
				sb.append("]");
			}

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

	// getPicSortInfoById

	/**
	 * 获取人员、部门、角色id串名字，不需要以"|"分离
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
			YHPictureLogic pictureLogic = new YHPictureLogic();
			Map map = new HashMap();
			map.put("SEQ_ID", seqId);
			String names = "";
			String ids = "";
			YHPicture picture = pictureLogic.selectManagerIds(dbConn, map);
			if ("userId".equals(action)) {
				ids = picture.getToUserId();
				if (ids == null) {
					ids = "";
				}
				names = pictureLogic.getNamesByIds(dbConn, map, action);
			}
			if ("roleId".equals(action)) {
				ids = picture.getToPrivId();
				if (ids == null) {
					ids = "";
				}
				names = pictureLogic.getNamesByIds(dbConn, map, action);
			}
			if ("deptId".equals(action)) {
				ids = picture.getToDeptId();
				if (ids == null) {
					ids = "";
				}
				if (!"ALL_DEPT".equals(ids.trim())) {
					names = pictureLogic.getNamesByIds(dbConn, map, action);
				}
			}
			String picName = picture.getPicName() == null ? "" : picture.getPicName();
			picName = picName.replaceAll("[\n-\r]", "<br>");
			picName = picName.replaceAll("[\\\\/:*?\"<>|]", "");
			picName = picName.replace("\"", "\\\"");
			
			
			String picPath = picture.getPicPath() == null ? "" : picture.getPicPath();
			String data = "{ids:\"" + ids + "\",names:\"" + names + "\",picName:\"" + picName + "\",picPath:\"" + YHUtility.encodeSpecial(picPath) + "\"}";
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
	 * 更新编辑图片目录信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updatePicSortInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHPictureLogic pictureLogic = new YHPictureLogic();
		int seqId = Integer.parseInt(request.getParameter("seqId"));

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPicture picture = (YHPicture) YHFOM.build(request.getParameterMap());
			String picPath = YHUtility.null2Empty(picture.getPicPath());
			Map map = new HashMap();
			String isPath = "";
			if (picPath != null && picPath.trim().length() > 0) {
				String path = picPath.replace("\\", "/");
				File file = new File(picPath.trim());
				if (file.exists()) {
					map.put("seqId", seqId);
					map.put("toUserId", request.getParameter("user"));
					map.put("toPrivId", request.getParameter("role"));
					map.put("toDeptId", request.getParameter("dept"));
					map.put("picName", picture.getPicName());
					map.put("picPath", path);
					pictureLogic.updatePicSort(dbConn, map);
				} else {
					isPath = "isNone";
				}
			} else {
				isPath = "isNone";
			}

			String data = "{isPath:\"" + isPath + "\"}";

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
	 * 根据id设置权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setPrivateById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHPictureLogic pictureLogic = new YHPictureLogic();
		String sortId = request.getParameter("seqId");
		String action = request.getParameter("action");
		String idStr = request.getParameter("idStr");
		int seqId = 0;
		if (sortId != null) {
			seqId = Integer.parseInt(sortId);
		}
		if (idStr.replaceAll("|", "").length() == 0) {
			idStr = "";
		}

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			Map map = new HashMap();
			map.put("seqId", seqId);
			if ("UPLOAD_USER".equals(action)) {
				map.put("uploadUser", idStr);
				pictureLogic.updatePrivateById(dbConn, map, action);

			} else if ("MANAGE_USER".equals(action)) {
				map.put("manageUser", idStr);
				pictureLogic.updatePrivateById(dbConn, map, action);
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
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPersonNameStr(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
			YHPictureLogic pictureLogic = new YHPictureLogic();
			Map map = new HashMap();
			map.put("SEQ_ID", seqId);
			String ids = "";
			String names = "";
			ids = pictureLogic.selectManagerIds(dbConn, map, action);
			if (!ids.equals("")) {
				names = pictureLogic.getNamesById(dbConn, map, action);
			}

			String data = "{user:\"" + ids + "\",userDesc:\"" + names + "\"}";
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
			YHPictureLogic pictureLogic = new YHPictureLogic();
			Map map = new HashMap();
			map.put("SEQ_ID", seqId);
			String ids = "";
			String names = "";
			ids = pictureLogic.getRoleIds(dbConn, map, action);

			//System.out.println(ids);
			if (!ids.equals("")) {
				names = pictureLogic.getRoleNamesByIds(dbConn, map, action);
			}
			String data = "{role:\"" + ids + "\",roleDesc:\"" + names + "\"}";
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
			YHPictureLogic pictureLogic = new YHPictureLogic();
			Map map = new HashMap();
			map.put("SEQ_ID", seqId);
			String ids = pictureLogic.getDeptIds(dbConn, map, action);
			String names = "";
			//System.out.println(ids);
			if (!ids.equals("")) {
				names = pictureLogic.getDeptByIds(dbConn, map, action);
			}
			String data = "{dept:\"" + ids + "\",deptDesc:\"" + names + "\"}";
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
	 * 删除信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delPicFolderById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int seqId = Integer.parseInt(request.getParameter("seqId"));
		Connection dbConn = null;
		YHPicture picture = new YHPicture();
		YHPictureLogic pictureLogic = new YHPictureLogic();
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			picture.setSeqId(seqId);
			pictureLogic.delPicFolderInfoById(dbConn, picture);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "数据删除成功！");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

}
