package yh.subsys.oa.confidentialFile.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.confidentialFile.data.YHConfidentialContent;
import yh.subsys.oa.confidentialFile.data.YHConfidentialSort;

public class YHSetConfidentialSortLogic {
	private static Logger log = Logger.getLogger(YHSetConfidentialSortLogic.class);

	/**
	 * 向数据库添加YHConfidentialSort对象
	 * 
	 * @param dbConn
	 * @param confidentialSort
	 * @throws Exception
	 */
	public void addConfidentialSortLogic(Connection dbConn, YHConfidentialSort confidentialSort) throws Exception {
		try {
			YHORM orm = new YHORM();
			orm.saveSingle(dbConn, confidentialSort);
		} catch (Exception ex) {
			throw ex;
		}

	}

	/**
	 * 检查数据库是否已经有该值
	 * 
	 * @param dbConn
	 * @param checkPlanNo
	 * @param seqIdStr
	 * @return
	 * @throws Exception
	 */
	public int checkSortNameLogic(Connection dbConn, String checkName, String seqIdStr) throws Exception {
		int num = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		if (YHUtility.isNullorEmpty(checkName)) {
			checkName = "";
		}

		try {
			String queryStr = "select count(*) from oa_secret_sort where SORT_NAME='" + YHDBUtility.escapeLike(checkName) + "'";
			if (!"".equals(seqIdStr) && seqId != 0) {
				queryStr = "select count(*) from oa_secret_sort where SORT_NAME='" + YHDBUtility.escapeLike(checkName) + "' and SEQ_ID !=" + seqId;
			}
			stmt = dbConn.prepareStatement(queryStr);
			rs = stmt.executeQuery();
			if (rs.next()) {
				num = rs.getInt(1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return num;
	}
	

	/**
	 * 获取目录列表
	 * @param dbConn
	 * @return
	 * @throws Exception
	 */
	public String getFileSortListLogic(Connection dbConn) throws Exception {
		StringBuffer sb = new StringBuffer("[");
		YHORM orm = new YHORM();
		boolean isHave = false;
		try {
			String[] filters = { " SORT_PARENT=0 order by SORT_NO,SORT_NAME " };
			List<YHConfidentialSort> confidentialSorts = orm.loadListSingle(dbConn, YHConfidentialSort.class, filters);
			if (confidentialSorts != null && confidentialSorts.size() > 0) {
				for (YHConfidentialSort sort : confidentialSorts) {
					int dbSeqId = sort.getSeqId();
					int sortParent = sort.getSortParent();
					String sortNo = YHUtility.null2Empty(sort.getSortNo());
					String sortName = YHUtility.null2Empty(sort.getSortName());

					sb.append("{");
					sb.append("sqlId:\"" + dbSeqId + "\"");
					sb.append(",sortParent:\"" + sortParent + "\"");
					sb.append(",sortNo:\"" + YHUtility.encodeSpecial(sortNo) + "\"");
					sb.append(",sortName:\"" + YHUtility.encodeSpecial(sortName) + "\"");
					sb.append("},");
					isHave = true;
				}
				if (isHave) {
					sb.deleteCharAt(sb.length() - 1);
				}
				sb.append("]");
			} else {
				sb.append("]");
			}

		} catch (Exception e) {
			throw e;
		}
		return sb.toString();
	}

	/**
	 * 根据seqId获取YHConfidentialSort对象
	 * 
	 * @param conn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public YHConfidentialSort getfileSortById(Connection dbConn, int seqId) throws Exception {
		try {
			YHORM orm = new YHORM();
			return (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, seqId);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 更新YHConfidentialSort对象
	 * 
	 * @param dbConn
	 * @param equipment
	 * @throws Exception
	 */
	public void updateFileSortByIdLogic(Connection dbConn, YHConfidentialSort confidentialSort) throws Exception {
		YHORM orm = new YHORM();
		try {
			orm.updateSingle(dbConn, confidentialSort);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据seqId删除YHConfidentialSort对象
	 * 
	 * @param conn
	 * @param seqId
	 * @throws Exception
	 */
	public void deleteSingleLogic(Connection conn, int seqId) throws Exception {
		try {
			YHORM orm = new YHORM();
			orm.deleteSingle(conn, YHConfidentialSort.class, seqId);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 递归删除文件夹及下的所有文件信息
	 * 
	 * 
	 * @param dbConn
	 * @param fileSort
	 * @throws Exception
	 */
	public void delFileSortInfoById(Connection dbConn, YHConfidentialSort fileSort, int loginUserSeqId, String ipStr) throws Exception {
		YHConfidentialContentLogic contentLogic = new YHConfidentialContentLogic();
		String separator = File.separator;
		String filePath = YHSysProps.getAttachPath() + separator + "confidential" + separator;

		String seqIdStrs = "";
		YHORM orm = new YHORM();
		Map map = new HashMap();
		map.put("SORT_PARENT", fileSort.getSeqId());
		List<YHConfidentialSort> fileSortList = orm.loadListComplex(dbConn, YHConfidentialSort.class, map);

		Map contentMap = new HashMap();
		contentMap.put("SORT_ID", fileSort.getSeqId());
		List<YHConfidentialContent> fileContents = new ArrayList<YHConfidentialContent>();
		fileContents = contentLogic.getFileContentsInfo(dbConn, contentMap);
		if (fileContents != null && fileContents.size() > 0) {
			for (int i = 0; i < fileContents.size(); i++) {
				YHConfidentialContent content = fileContents.get(i);
				seqIdStrs += content.getSeqId() + ",";
			}
			if (seqIdStrs.endsWith(",")) {
				seqIdStrs = seqIdStrs.trim().substring(0, seqIdStrs.trim().length() - 1);
			}
			contentLogic.delFile(dbConn, seqIdStrs, filePath, loginUserSeqId, ipStr, "", "");
		}

		orm.deleteSingle(dbConn, fileSort);
		for (int i = 0; i < fileSortList.size(); i++) {
			delFileSortInfoById(dbConn, fileSortList.get(i), loginUserSeqId, ipStr);
		}
	}

	/**
	 * 获取树形结构信息,用于权限设置用，不考虑权限。
	 * 
	 * @param dbConn
	 * @param idStr
	 * @param sortIdStr
	 * @return
	 * @throws Exception
	 */
	public String getSetTreeLogic(Connection dbConn, String idStr, String sortIdStr) throws Exception {
		int id = 0;
		if (!YHUtility.isNullorEmpty(idStr)) {
			id = Integer.parseInt(idStr);
		}
		StringBuffer sb = new StringBuffer("[");
		try {

			if (!YHUtility.isNullorEmpty(sortIdStr) && id == 0) {
				YHConfidentialSort fileSort = this.getfileSortById(dbConn, Integer.parseInt(sortIdStr));
				if (fileSort != null) {
					int seqId = fileSort.getSeqId();
					String sortName = YHUtility.null2Empty(fileSort.getSortName());
					sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
					int isHaveChild = this.isHaveChild(dbConn, fileSort.getSeqId());
					String extData = "";
					sb.append("{");
					sb.append("nodeId:\"" + seqId + "\"");
					sb.append(",name:\"" + YHUtility.encodeSpecial(sortName) + "\"");
					sb.append(",isHaveChild:" + isHaveChild + "");
					sb.append(",extData:\"" + extData + "\"");
					sb.append("},");
					sb.deleteCharAt(sb.length() - 1);
				}

			} else {
				String[] condition = { " SORT_PARENT=" + id + " order by SORT_NO,SORT_NAME" };
				List<YHConfidentialSort> list = this.getFileFilderInfo(dbConn, condition);
				if (list.size() > 0) {
					for (YHConfidentialSort fileSort : list) {
						int seqId = fileSort.getSeqId();
						String sortName = YHUtility.null2Empty(fileSort.getSortName());
						sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
						int isHaveChild = this.isHaveChild(dbConn, fileSort.getSeqId());
						String extData = "";
						sb.append("{");
						sb.append("nodeId:\"" + seqId + "\"");
						sb.append(",name:\"" + YHUtility.encodeSpecial(sortName) + "\"");
						sb.append(",isHaveChild:" + isHaveChild + "");
						sb.append(",extData:\"" + extData + "\"");
						sb.append("},");
					}
					sb.deleteCharAt(sb.length() - 1);
				}
			}
			sb.append("]");
		} catch (Exception e) {
			throw e;
		}

		return sb.toString();
	}

	/**
	 * 判断是否有子级文件夹,考虑权限。
	 * 
	 * @param dbConn
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int isHaveChild(Connection dbConn, int id) throws Exception {
		YHORM orm = new YHORM();
		Map map = new HashMap();
		map.put("SORT_PARENT", id);
		List<YHConfidentialSort> list = orm.loadListSingle(dbConn, YHConfidentialSort.class, map);
		if (list.size() > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	public List<YHConfidentialSort> getFileFilderInfo(Connection dbConn, String[] condition) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHConfidentialSort.class, condition);
	}

	public List<YHConfidentialSort> getFileSorts(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		return (List<YHConfidentialSort>) orm.loadListSingle(dbConn, YHConfidentialSort.class, map);
	}

	/**
	 * 递归重置所有下级子文件夹的权限
	 * 
	 * @param dbConn
	 * @param seqId
	 * @param setIdStr
	 * @throws Exception
	 */
	public void updateVisitOverride(Connection dbConn, YHConfidentialSort fileSort, String setIdStr, String action) throws Exception {
		this.getChildFolder(dbConn, fileSort, setIdStr, action);
	}

	public void getChildFolder(Connection dbConn, YHConfidentialSort fileSort, String setIdStr, String action) throws Exception {

		YHORM orm = new YHORM();
		int seqId = fileSort.getSeqId();
		if ("USER_ID".equals(action)) {
			fileSort.setUserId(setIdStr);
		}
		if ("MANAGE_USER".equals(action)) {
			fileSort.setManageUser(setIdStr);
		}
		if ("NEW_USER".equals(action)) {
			fileSort.setNewUser(setIdStr);
		}
		if ("DOWN_USER".equals(action)) {
			fileSort.setDownUser(setIdStr);
		}
		if ("OWNER".equals(action)) {
			fileSort.setOwner(setIdStr);
		}
		orm.updateSingle(dbConn, fileSort);
		Map map = new HashMap();
		map.put("SORT_PARENT", seqId);
		List<YHConfidentialSort> list = getFileSorts(dbConn, map);

		for (YHConfidentialSort sort : list) {
			getChildFolder(dbConn, sort, setIdStr, action);
		}

	}

	// /**
	// * 根据id设置访问权限
	// *
	// * @param dbConn
	// * @param sortId
	// * @param override
	// * @param userId
	// * @throws Exception
	// */
	// public void setVisitByIdLogic(Connection dbConn, String sortId, String
	// override, String userId) throws Exception {
	// try {
	// String action = "USER_ID";
	// int seqId = 0;
	// if (!YHUtility.isNullorEmpty(sortId)) {
	// seqId = Integer.parseInt(sortId);
	// }
	// if (userId.replaceAll("|", "").length() == 0) {
	// userId = "";
	// }
	// YHConfidentialSort confidentialSort = this.getfileSortById(dbConn, seqId);
	// if ("override".equals(override)) {
	// this.updateVisitOverride(dbConn, confidentialSort, userId, action);
	// } else {
	// confidentialSort.setUserId(userId);
	// this.updateFileSortByIdLogic(dbConn, confidentialSort);
	// }
	//
	// } catch (Exception e) {
	// throw e;
	// }
	// }
	// /**
	// * 根据id设置管理权限
	// *
	// * @param dbConn
	// * @param sortId
	// * @param override
	// * @param userId
	// * @throws Exception
	// */
	// public void setManageUserByIdLogic(Connection dbConn, String sortId, String
	// override, String manageUser) throws Exception {
	// try {
	// String action = "MANAGE_USER";
	// int seqId = 0;
	// if (!YHUtility.isNullorEmpty(sortId)) {
	// seqId = Integer.parseInt(sortId);
	// }
	// if (manageUser.replaceAll("|", "").length() == 0) {
	// manageUser = "";
	// }
	// YHConfidentialSort confidentialSort = this.getfileSortById(dbConn, seqId);
	// if ("override".equals(override)) {
	// this.updateVisitOverride(dbConn, confidentialSort, manageUser, action);
	// } else {
	// confidentialSort.setManageUser(manageUser);
	// this.updateFileSortByIdLogic(dbConn, confidentialSort);
	// }
	// } catch (Exception e) {
	// throw e;
	// }
	// }
	/**
	 * 根据id设置管理权限
	 * 
	 * @param dbConn
	 * @param sortId
	 * @param override
	 * @param userId
	 * @throws Exception
	 */
	public void setPrivLogic(Connection dbConn, String sortId, String override, String setIdStr, String action) throws Exception {
		try {
			int seqId = 0;
			if (!YHUtility.isNullorEmpty(sortId)) {
				seqId = Integer.parseInt(sortId);
			}
			if (setIdStr.replaceAll("|", "").length() == 0) {
				setIdStr = "";
			}
			YHConfidentialSort confidentialSort = this.getfileSortById(dbConn, seqId);
			if ("override".equals(override)) {
				this.updateVisitOverride(dbConn, confidentialSort, setIdStr, action);
			} else {
				if ("USER_ID".equals(action)) {
					confidentialSort.setUserId(setIdStr);
				}
				if ("MANAGE_USER".equals(action)) {
					confidentialSort.setManageUser(setIdStr);
				}
				if ("NEW_USER".equals(action)) {
					confidentialSort.setNewUser(setIdStr);
				}
				if ("DOWN_USER".equals(action)) {
					confidentialSort.setDownUser(setIdStr);
				}
				if ("OWNER".equals(action)) {
					confidentialSort.setOwner(setIdStr);
				}
				this.updateFileSortByIdLogic(dbConn, confidentialSort);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 分离出人员的id字符串
	 * 
	 * @param dbConn
	 * @param fileSort
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String selectManagerIds(Connection dbConn, YHConfidentialSort fileSort, String action) throws Exception {
		String ids = "";
		if (fileSort != null) {
			if ("USER_ID".equals(action)) {
				if (fileSort.getUserId() != null) {
					String idString = YHUtility.null2Empty(fileSort.getUserId());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 2) {
							ids = "";
						} else if (idsStrings.length == 1) {
							ids = "";

						} else {
							ids = idsStrings[2];
						}

					}
				}
			} else if ("MANAGE_USER".equals(action)) {
				if (fileSort.getManageUser() != null) {
					String idString = YHUtility.null2Empty(fileSort.getManageUser());
					String[] idsStrings = idString.split("\\|");
					if (idsStrings.length != 0) {
						if (idsStrings.length == 2) {
							ids = "";
						} else if (idsStrings.length == 1) {
							ids = "";

						} else {
							ids = idsStrings[2];
						}

					}
				}
			} else if ("NEW_USER".equals(action)) {
				if (fileSort.getNewUser() != null) {
					String idString = YHUtility.null2Empty(fileSort.getNewUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 2) {
							ids = "";
						} else if (idsStrings.length == 1) {
							ids = "";

						} else {
							ids = idsStrings[2];
						}
					}

				}
			} else if ("DOWN_USER".equals(action)) {
				if (fileSort.getDownUser() != null) {
					String idString = YHUtility.null2Empty(fileSort.getDownUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 2) {
							ids = "";
						} else if (idsStrings.length == 1) {
							ids = "";

						} else {
							ids = idsStrings[2];
						}
					}

				}
			} else if ("OWNER".equals(action)) {
				if (fileSort.getOwner() != null) {
					String idString = YHUtility.null2Empty(fileSort.getOwner());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 2) {
							ids = "";
						} else if (idsStrings.length == 1) {
							ids = "";
						} else {
							ids = idsStrings[2];
						}

					}

				}
			}

		}
		return ids;
	}

	/**
	 * 根据人员id字符串得到name字符串
	 * 
	 * @param dbConn
	 * @param ids
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String getNamesByIds(Connection dbConn, String ids, String action) throws Exception {
		String names = "";
		YHPersonLogic tpl = new YHPersonLogic();
		names = tpl.getNameBySeqIdStr(ids, dbConn);
		return names;
	}

	/**
	 * 获取人员id名字串
	 * 
	 * @param dbConn
	 * @param seqIdStr
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String getPersonNameStrLogic(Connection dbConn, String seqIdStr, String action) throws Exception {
		String data = "";
		try {
			int seqId = 0;
			if (!YHUtility.isNullorEmpty(seqIdStr)) {
				seqId = Integer.parseInt(seqIdStr);
			}
			YHConfidentialSort fileSort = this.getfileSortById(dbConn, seqId);
			String ids = this.selectManagerIds(dbConn, fileSort, action);
			String names = "";
			if (!YHUtility.isNullorEmpty(ids)) {
				names = this.getNamesByIds(dbConn, ids, action);
			}
			data = "{user:\"" + ids + "\",userDesc:\"" + YHUtility.encodeSpecial(names) + "\"}";
		} catch (Exception e) {
			throw e;
		}
		return data;
	}

	/**
	 * 分离出部门人员的id字符串
	 * 
	 * @param dbConn
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getDeptIds(Connection dbConn, YHConfidentialSort fileSort, String action) throws Exception {
		YHORM orm = new YHORM();
		String ids = "";
		if (fileSort != null) {
			if ("USER_ID".equals(action)) {
				if (fileSort.getUserId() != null) {
					String idString = YHUtility.null2Empty(fileSort.getUserId());
					String[] idsStrings = idString.split("\\|");

					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}
				}
			} else if ("MANAGE_USER".equals(action)) {
				if (fileSort.getManageUser() != null) {
					String idString = YHUtility.null2Empty(fileSort.getManageUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}
				}
			} else if ("NEW_USER".equals(action)) {
				if (fileSort.getNewUser() != null) {
					String idString = YHUtility.null2Empty(fileSort.getNewUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}
				}
			} else if ("DOWN_USER".equals(action)) {
				if (fileSort.getDownUser() != null) {
					String idString = YHUtility.null2Empty(fileSort.getDownUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}
				}
			} else if ("OWNER".equals(action)) {
				if (fileSort.getOwner() != null) {
					String idString = YHUtility.null2Empty(fileSort.getOwner());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}
				}
			}

		}
		return ids;
	}

	/**
	 * 根据seqId串返回部门名字串
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public String getDeptNameBySeqIdStr(Connection dbConn, String ids) throws Exception, Exception {
		String names = "";
		if ("ALL_DEPT".equals(ids)) {
			return names;
		}
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp.trim()) && !"ALL_DEPT".equals(tmp.trim())) {
					YHDepartment deptName = this.getDeptById(dbConn, Integer.parseInt(tmp.trim()));
					if (deptName != null) {
						names += deptName.getDeptName() + ",";
					}
				}
			}
		}
		if (names.endsWith(",")) {
			names = names.substring(0, names.length() - 1);
		}
		return names;
	}

	/**
	 * 根据seqId查询部门信息
	 * 
	 * @param dbConn
	 * @param seqId
	 * @return YHDepartment对象
	 * @throws Exception
	 */
	public YHDepartment getDeptById(Connection dbConn, int seqId) throws Exception {
		YHORM orm = new YHORM();
		YHDepartment department = (YHDepartment) orm.loadObjSingle(dbConn, YHDepartment.class, seqId);
		return department;
	}

	/**
	 * 获取部门名字串
	 * 
	 * @param dbConn
	 * @param seqIdStr
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String getDeptNameStrLogic(Connection dbConn, String seqIdStr, String action) throws Exception {
		String data = "";
		try {
			int seqId = 0;
			if (!YHUtility.isNullorEmpty(seqIdStr)) {
				seqId = Integer.parseInt(seqIdStr);
			}
			YHConfidentialSort fileSort = this.getfileSortById(dbConn, seqId);
			String ids = this.getDeptIds(dbConn, fileSort, action);
			String names = "";
			if (!YHUtility.isNullorEmpty(ids)) {
				names = this.getDeptNameBySeqIdStr(dbConn, ids);
			}
			data = "{dept:\"" + ids + "\",deptDesc:\"" + YHUtility.encodeSpecial(names) + "\"}";
		} catch (Exception e) {
			throw e;
		}
		return data;
	}

	/**
	 * 分离出角色人员的id字符串
	 * 
	 * @param dbConn
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getRoleIds(Connection dbConn, YHConfidentialSort fileSort, String action) throws Exception {
		String ids = "";
		if (fileSort != null) {
			if ("USER_ID".equals(action)) {
				if (fileSort.getUserId() != null) {
					String idString = YHUtility.null2Empty(fileSort.getUserId());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 1) {
							ids = "";
						} else {
							ids = idsStrings[1];
						}
					}
				}
			} else if ("MANAGE_USER".equals(action)) {
				if (fileSort.getManageUser() != null) {
					String idString = fileSort.getManageUser();
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 1) {
							ids = "";
						} else {
							ids = idsStrings[1];
						}
					}
				}
			} else if ("NEW_USER".equals(action)) {
				if (fileSort.getNewUser() != null) {
					String idString = YHUtility.null2Empty(fileSort.getNewUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 1) {
							ids = "";
						} else {
							ids = idsStrings[1];
						}
					}
				}
			} else if ("DOWN_USER".equals(action)) {
				if (fileSort.getDownUser() != null) {
					String idString = YHUtility.null2Empty(fileSort.getDownUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 1) {
							ids = "";
						} else {
							ids = idsStrings[1];
						}
					}
				}
			} else if ("OWNER".equals(action)) {
				if (fileSort.getOwner() != null) {
					String idString = YHUtility.null2Empty(fileSort.getOwner());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 1) {
							ids = "";
						} else {
							ids = idsStrings[1];
						}
					}
				}
			}

		}
		return ids;
	}

	/**
	 * 根据seqId串返回角色名字串
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public String getRoleNameBySeqIdStr(Connection dbConn, String ids) throws Exception, Exception {
		String names = "";
		if (!YHUtility.isNullorEmpty(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp)) {
					YHUserPriv privName = this.getPersonById(dbConn, Integer.parseInt(tmp));
					if (privName != null) {
						names += privName.getPrivName() + ",";
					}
				}
			}
		}
		if (names.endsWith(",")) {
			names = names.substring(0, names.length() - 1);
		}
		return names;
	}

	/**
	 * 根据seqId查询角色信息
	 * 
	 * @param dbConn
	 * @param seqId
	 * @return YHUserPriv对象
	 * @throws Exception
	 */
	public YHUserPriv getPersonById(Connection dbConn, int seqId) throws Exception {
		YHORM orm = new YHORM();
		YHUserPriv userPriv = (YHUserPriv) orm.loadObjSingle(dbConn, YHUserPriv.class, seqId);
		return userPriv;
	}

	/**
	 * 获取人员id名字串
	 * 
	 * @param dbConn
	 * @param seqIdStr
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String getRoleNameStrLogic(Connection dbConn, String seqIdStr, String action) throws Exception {
		String data = "";
		try {
			int seqId = 0;
			if (!YHUtility.isNullorEmpty(seqIdStr)) {
				seqId = Integer.parseInt(seqIdStr);
			}
			YHConfidentialSort fileSort = this.getfileSortById(dbConn, seqId);
			String ids = this.getRoleIds(dbConn, fileSort, action);
			String names = "";
			if (!YHUtility.isNullorEmpty(ids)) {
				names = this.getRoleNameBySeqIdStr(dbConn, ids);
			}
			data = "{role:\"" + ids + "\",roleDesc:\"" + YHUtility.encodeSpecial(names) + "\"}";
		} catch (Exception e) {
			throw e;
		}
		return data;
	}

	/**
	 * 批量设置权限（添加权限）
	 * 
	 * @param dbConn
	 * @param seqId
	 * @param setIdStr
	 * @throws Exception
	 */
	public void updateVisitOverrideAdd(Connection dbConn, int seqId, String setIdStr, String action) throws Exception {
		YHORM orm = new YHORM();
		YHConfidentialSort fileSort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, seqId);
		getChildFolderAdd(dbConn, fileSort, setIdStr, action);

	}

	public void getChildFolderAdd(Connection dbConn, YHConfidentialSort fileSort, String setIdStr, String action) throws Exception {
		YHConfidentialSort fileSort2 = new YHConfidentialSort();

		YHORM orm = new YHORM();
		int seqId = fileSort.getSeqId();
		String deptString = "";
		String roleString = "";
		String personString = "";
		if ("USER_ID".equals(action)) {
			fileSort2.setUserId(setIdStr);
			String deptIdStrs = this.getDeptIds(dbConn, fileSort2, action);
			String roleIdStrs = this.getRoleIds(dbConn, fileSort2, action);
			String personIdStrs = this.selectManagerIds(dbConn, fileSort2, action);

			String dbDeptIdStrs = this.getDeptIds(dbConn, fileSort, action);
			String dbRoleIdStrs = this.getRoleIds(dbConn, fileSort, action);
			String dbPersonIdStrs = this.selectManagerIds(dbConn, fileSort, action);

			String deptTem = "";
			String roleTem = "";
			String personTem = "";
			if (!"".equals(dbDeptIdStrs.trim()) && !"".equals(deptIdStrs.trim())) {
				deptTem = ",";
			}
			if (!"".equals(dbRoleIdStrs.trim()) && !"".equals(roleIdStrs.trim())) {
				roleTem = ",";
			}
			if (!"".equals(dbPersonIdStrs.trim()) && !"".equals(personIdStrs.trim())) {
				personTem = ",";
			}

			String alldeptStr = "";

			if (!"0".equals(dbDeptIdStrs.trim()) && !"0".equals(deptIdStrs.trim())) {
				deptString = dbDeptIdStrs + deptTem + deptIdStrs;
				alldeptStr = this.delReIdStr(deptString);
			} else {
				alldeptStr = "0";
			}

			roleString = dbRoleIdStrs + roleTem + roleIdStrs;
			personString = dbPersonIdStrs + personTem + personIdStrs;

			String allRoleStr = this.delReIdStr(roleString);
			String personStr = this.delReIdStr(personString);

			String idStrArry = alldeptStr + "|" + allRoleStr + "|" + personStr;

			fileSort.setUserId(idStrArry);
		}
		if ("MANAGE_USER".equals(action)) {
			fileSort2.setManageUser(setIdStr);
			String deptIdStrs = this.getDeptIds(dbConn, fileSort2, action);
			String roleIdStrs = this.getRoleIds(dbConn, fileSort2, action);
			String personIdStrs = this.selectManagerIds(dbConn, fileSort2, action);

			String dbDeptIdStrs = this.getDeptIds(dbConn, fileSort, action);
			String dbRoleIdStrs = this.getRoleIds(dbConn, fileSort, action);
			String dbPersonIdStrs = this.selectManagerIds(dbConn, fileSort, action);

			String deptTem = "";
			String roleTem = "";
			String personTem = "";
			if (!"".equals(dbDeptIdStrs.trim()) && !"".equals(deptIdStrs.trim())) {
				deptTem = ",";
			}
			if (!"".equals(dbRoleIdStrs.trim()) && !"".equals(roleIdStrs.trim())) {
				roleTem = ",";
			}
			if (!"".equals(dbPersonIdStrs.trim()) && !"".equals(personIdStrs.trim())) {
				personTem = ",";
			}

			String alldeptStr = "";

			if (!"0".equals(dbDeptIdStrs.trim()) && !"0".equals(deptIdStrs.trim())) {
				deptString = dbDeptIdStrs + deptTem + deptIdStrs;
				alldeptStr = this.delReIdStr(deptString);
			} else {
				alldeptStr = "0";
			}

			roleString = dbRoleIdStrs + roleTem + roleIdStrs;
			personString = dbPersonIdStrs + personTem + personIdStrs;
			String allRoleStr = this.delReIdStr(roleString);
			String personStr = this.delReIdStr(personString);
			String idStrArry = alldeptStr + "|" + allRoleStr + "|" + personStr;

			fileSort.setManageUser(idStrArry);
		}
		if ("NEW_USER".equals(action)) {
			fileSort2.setNewUser(setIdStr);
			String deptIdStrs = this.getDeptIds(dbConn, fileSort2, action);
			String roleIdStrs = this.getRoleIds(dbConn, fileSort2, action);
			String personIdStrs = this.selectManagerIds(dbConn, fileSort2, action);

			String dbDeptIdStrs = this.getDeptIds(dbConn, fileSort, action);
			String dbRoleIdStrs = this.getRoleIds(dbConn, fileSort, action);
			String dbPersonIdStrs = this.selectManagerIds(dbConn, fileSort, action);

			String deptTem = "";
			String roleTem = "";
			String personTem = "";
			if (!"".equals(dbDeptIdStrs.trim()) && !"".equals(deptIdStrs.trim())) {
				deptTem = ",";
			}
			if (!"".equals(dbRoleIdStrs.trim()) && !"".equals(roleIdStrs.trim())) {
				roleTem = ",";
			}
			if (!"".equals(dbPersonIdStrs.trim()) && !"".equals(personIdStrs.trim())) {
				personTem = ",";
			}

			String alldeptStr = "";

			if (!"0".equals(dbDeptIdStrs.trim()) && !"0".equals(deptIdStrs.trim())) {
				deptString = dbDeptIdStrs + deptTem + deptIdStrs;
				alldeptStr = this.delReIdStr(deptString);
			} else {
				alldeptStr = "0";
			}

			roleString = dbRoleIdStrs + roleTem + roleIdStrs;
			personString = dbPersonIdStrs + personTem + personIdStrs;

			String allRoleStr = this.delReIdStr(roleString);
			String personStr = this.delReIdStr(personString);

			String idStrArry = alldeptStr + "|" + allRoleStr + "|" + personStr;

			// fileSort.setOwner(idStrArry);
			fileSort.setNewUser(idStrArry);
		}
		if ("DOWN_USER".equals(action)) {
			fileSort2.setDownUser(setIdStr);
			String deptIdStrs = this.getDeptIds(dbConn, fileSort2, action);
			String roleIdStrs = this.getRoleIds(dbConn, fileSort2, action);
			String personIdStrs = this.selectManagerIds(dbConn, fileSort2, action);

			String dbDeptIdStrs = this.getDeptIds(dbConn, fileSort, action);
			String dbRoleIdStrs = this.getRoleIds(dbConn, fileSort, action);
			String dbPersonIdStrs = this.selectManagerIds(dbConn, fileSort, action);

			String deptTem = "";
			String roleTem = "";
			String personTem = "";
			if (!"".equals(dbDeptIdStrs.trim()) && !"".equals(deptIdStrs.trim())) {
				deptTem = ",";
			}
			if (!"".equals(dbRoleIdStrs.trim()) && !"".equals(roleIdStrs.trim())) {
				roleTem = ",";
			}
			if (!"".equals(dbPersonIdStrs.trim()) && !"".equals(personIdStrs.trim())) {
				personTem = ",";
			}

			String alldeptStr = "";

			if (!"0".equals(dbDeptIdStrs.trim()) && !"0".equals(deptIdStrs.trim())) {
				deptString = dbDeptIdStrs + deptTem + deptIdStrs;
				alldeptStr = this.delReIdStr(deptString);
			} else {
				alldeptStr = "0";
			}

			roleString = dbRoleIdStrs + roleTem + roleIdStrs;
			personString = dbPersonIdStrs + personTem + personIdStrs;

			String allRoleStr = this.delReIdStr(roleString);
			String personStr = this.delReIdStr(personString);

			String idStrArry = alldeptStr + "|" + allRoleStr + "|" + personStr;

			fileSort.setDownUser(idStrArry);
		}
		if ("OWNER".equals(action)) {
			fileSort2.setOwner(setIdStr);
			String deptIdStrs = this.getDeptIds(dbConn, fileSort2, action);
			String roleIdStrs = this.getRoleIds(dbConn, fileSort2, action);
			String personIdStrs = this.selectManagerIds(dbConn, fileSort2, action);

			String dbDeptIdStrs = this.getDeptIds(dbConn, fileSort, action);
			String dbRoleIdStrs = this.getRoleIds(dbConn, fileSort, action);
			String dbPersonIdStrs = this.selectManagerIds(dbConn, fileSort, action);

			String deptTem = "";
			String roleTem = "";
			String personTem = "";
			if (!"".equals(dbDeptIdStrs.trim()) && !"".equals(deptIdStrs.trim())) {
				deptTem = ",";
			}
			if (!"".equals(dbRoleIdStrs.trim()) && !"".equals(roleIdStrs.trim())) {
				roleTem = ",";
			}
			if (!"".equals(dbPersonIdStrs.trim()) && !"".equals(personIdStrs.trim())) {
				personTem = ",";
			}

			String alldeptStr = "";

			if (!"0".equals(dbDeptIdStrs.trim()) && !"0".equals(deptIdStrs.trim())) {
				deptString = dbDeptIdStrs + deptTem + deptIdStrs;
				alldeptStr = this.delReIdStr(deptString);
			} else {
				alldeptStr = "0";
			}

			roleString = dbRoleIdStrs + roleTem + roleIdStrs;
			personString = dbPersonIdStrs + personTem + personIdStrs;

			String allRoleStr = this.delReIdStr(roleString);
			String personStr = this.delReIdStr(personString);

			String idStrArry = alldeptStr + "|" + allRoleStr + "|" + personStr;

			fileSort.setOwner(idStrArry);
		}
		orm.updateSingle(dbConn, fileSort);
		Map map = new HashMap();
		map.put("SORT_PARENT", seqId);
		List<YHConfidentialSort> list = getFileSorts(dbConn, map);

		for (YHConfidentialSort sort : list) {
			getChildFolder(dbConn, sort, setIdStr, action);
		}

	}

	/**
	 * 去掉重复id
	 * 
	 * @param idString
	 * @return
	 * @throws Exception
	 */
	public String delReIdStr(String idString) throws Exception {
		String data = "";
		try {
			String[] stringArry = idString.split(",");
			ArrayList arrayList = new ArrayList();
			if (stringArry != null && stringArry.length != 0) {
				for (int i = 0; i < stringArry.length; i++) {
					if (arrayList.contains(stringArry[i]) == false) {
						arrayList.add(stringArry[i]);
						data += stringArry[i] + ",";
					}
				}
			}
			if (data.lastIndexOf(",") != -1) {
				data = data.substring(0, data.lastIndexOf(","));
			}
		} catch (Exception e) {
			throw e;
		}
		return data;
	}

	/**
	 * 批量设置权限（删除权限）
	 * 
	 * @param dbConn
	 * @param seqId
	 * @param setIdStr
	 * @throws Exception
	 */
	public void updateVisitOverrideDel(Connection dbConn, int seqId, String setIdStr, String action) throws Exception {
		YHORM orm = new YHORM();
		YHConfidentialSort fileSort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, seqId);
		getChildFolderDel(dbConn, fileSort, setIdStr, action);
	}

	public void getChildFolderDel(Connection dbConn, YHConfidentialSort fileSort, String setIdStr, String action) throws Exception {
		YHConfidentialSort fileSort2 = new YHConfidentialSort();

		YHORM orm = new YHORM();
		int seqId = fileSort.getSeqId();
		if ("USER_ID".equals(action)) {
			fileSort2.setUserId(setIdStr);
			String deptIdStrs = this.getDeptIds(dbConn, fileSort2, action);
			String roleIdStrs = this.getRoleIds(dbConn, fileSort2, action);
			String personIdStrs = this.selectManagerIds(dbConn, fileSort2, action);

			String dbDeptIdStrs = this.getDeptIds(dbConn, fileSort, action);
			String dbRoleIdStrs = this.getRoleIds(dbConn, fileSort, action);
			String dbPersonIdStrs = this.selectManagerIds(dbConn, fileSort, action);

			String alldeptStr = "";

			if (!"0".equals(dbDeptIdStrs.trim()) && !"0".equals(deptIdStrs.trim())) {
				alldeptStr = this.getDelIdStrs(deptIdStrs, dbDeptIdStrs);
			} else if (!"0".equals(deptIdStrs.trim())) {
				dbDeptIdStrs = this.getAlldept(dbConn);
				alldeptStr = this.getDelIdStrs(deptIdStrs, dbDeptIdStrs);
			}

			String allRoleStr = this.getDelIdStrs(roleIdStrs, dbRoleIdStrs);
			String personStr = this.getDelIdStrs(personIdStrs, dbPersonIdStrs);

			String idStrArry = alldeptStr + "|" + allRoleStr + "|" + personStr;
			fileSort.setUserId(idStrArry);
		}
		if ("MANAGE_USER".equals(action)) {
			fileSort2.setManageUser(setIdStr);
			String deptIdStrs = this.getDeptIds(dbConn, fileSort2, action);
			String roleIdStrs = this.getRoleIds(dbConn, fileSort2, action);
			String personIdStrs = this.selectManagerIds(dbConn, fileSort2, action);

			String dbDeptIdStrs = this.getDeptIds(dbConn, fileSort, action);
			String dbRoleIdStrs = this.getRoleIds(dbConn, fileSort, action);
			String dbPersonIdStrs = this.selectManagerIds(dbConn, fileSort, action);

			String alldeptStr = "";

			if (!"0".equals(dbDeptIdStrs.trim()) && !"0".equals(deptIdStrs.trim())) {
				alldeptStr = this.getDelIdStrs(deptIdStrs, dbDeptIdStrs);
			} else if (!"0".equals(deptIdStrs.trim()) && !"0".equals(deptIdStrs.trim())) {
				dbDeptIdStrs = this.getAlldept(dbConn);
				alldeptStr = this.getDelIdStrs(deptIdStrs, dbDeptIdStrs);
			}

			String allRoleStr = this.getDelIdStrs(roleIdStrs, dbRoleIdStrs);
			String personStr = this.getDelIdStrs(personIdStrs, dbPersonIdStrs);

			String idStrArry = alldeptStr + "|" + allRoleStr + "|" + personStr;
			fileSort.setManageUser(idStrArry);
		}
		if ("NEW_USER".equals(action)) {
			fileSort2.setNewUser(setIdStr);
			String deptIdStrs = this.getDeptIds(dbConn, fileSort2, action);
			String roleIdStrs = this.getRoleIds(dbConn, fileSort2, action);
			String personIdStrs = this.selectManagerIds(dbConn, fileSort2, action);

			String dbDeptIdStrs = this.getDeptIds(dbConn, fileSort, action);
			String dbRoleIdStrs = this.getRoleIds(dbConn, fileSort, action);
			String dbPersonIdStrs = this.selectManagerIds(dbConn, fileSort, action);

			String alldeptStr = "";

			if (!"0".equals(dbDeptIdStrs.trim()) && !"0".equals(deptIdStrs.trim())) {
				alldeptStr = this.getDelIdStrs(deptIdStrs, dbDeptIdStrs);
			} else if (!"0".equals(deptIdStrs.trim())) {
				dbDeptIdStrs = this.getAlldept(dbConn);
				alldeptStr = this.getDelIdStrs(deptIdStrs, dbDeptIdStrs);
			}

			String allRoleStr = this.getDelIdStrs(roleIdStrs, dbRoleIdStrs);
			String personStr = this.getDelIdStrs(personIdStrs, dbPersonIdStrs);

			String idStrArry = alldeptStr + "|" + allRoleStr + "|" + personStr;

			fileSort.setNewUser(idStrArry);
		}
		if ("DOWN_USER".equals(action)) {
			fileSort2.setDownUser(setIdStr);
			String deptIdStrs = this.getDeptIds(dbConn, fileSort2, action);
			String roleIdStrs = this.getRoleIds(dbConn, fileSort2, action);
			String personIdStrs = this.selectManagerIds(dbConn, fileSort2, action);

			String dbDeptIdStrs = this.getDeptIds(dbConn, fileSort, action);
			String dbRoleIdStrs = this.getRoleIds(dbConn, fileSort, action);
			String dbPersonIdStrs = this.selectManagerIds(dbConn, fileSort, action);

			String alldeptStr = "";

			if (!"0".equals(dbDeptIdStrs.trim()) && !"0".equals(deptIdStrs.trim())) {
				alldeptStr = this.getDelIdStrs(deptIdStrs, dbDeptIdStrs);
			} else if (!"0".equals(deptIdStrs.trim())) {
				dbDeptIdStrs = this.getAlldept(dbConn);
				alldeptStr = this.getDelIdStrs(deptIdStrs, dbDeptIdStrs);
			}

			String allRoleStr = this.getDelIdStrs(roleIdStrs, dbRoleIdStrs);
			String personStr = this.getDelIdStrs(personIdStrs, dbPersonIdStrs);

			String idStrArry = alldeptStr + "|" + allRoleStr + "|" + personStr;

			fileSort.setDownUser(idStrArry);
		}
		if ("OWNER".equals(action)) {
			// String deptString = "";
			// String roleString = "";
			// String personString = "";
			fileSort2.setOwner(setIdStr);
			String deptIdStrs = this.getDeptIds(dbConn, fileSort2, action);
			String roleIdStrs = this.getRoleIds(dbConn, fileSort2, action);
			String personIdStrs = this.selectManagerIds(dbConn, fileSort2, action);

			String dbDeptIdStrs = this.getDeptIds(dbConn, fileSort, action);
			String dbRoleIdStrs = this.getRoleIds(dbConn, fileSort, action);
			String dbPersonIdStrs = this.selectManagerIds(dbConn, fileSort, action);

			String alldeptStr = "";

			String allDeptIdStrs = "";
			if (!"0".equals(dbDeptIdStrs.trim()) && !"0".equals(deptIdStrs.trim())) {
				alldeptStr = this.getDelIdStrs(deptIdStrs, dbDeptIdStrs);
			} else if (!"0".equals(deptIdStrs.trim())) {
				dbDeptIdStrs = this.getAlldept(dbConn);
				alldeptStr = this.getDelIdStrs(deptIdStrs, dbDeptIdStrs);
			}

			String allRoleStr = this.getDelIdStrs(roleIdStrs, dbRoleIdStrs);
			String personStr = this.getDelIdStrs(personIdStrs, dbPersonIdStrs);

			String idStrArry = alldeptStr + "|" + allRoleStr + "|" + personStr;

			fileSort.setOwner(idStrArry);
		}
		orm.updateSingle(dbConn, fileSort);
		Map map = new HashMap();
		map.put("SORT_PARENT", seqId);
		List<YHConfidentialSort> list = getFileSorts(dbConn, map);

		for (YHConfidentialSort sort : list) {
			getChildFolderDel(dbConn, sort, setIdStr, action);
		}

	}

	/**
	 * 删除id权限
	 * 
	 * @param fromIdStr
	 * @return
	 * @throws Exception
	 */
	public String getDelIdStrs(String fromIdStr, String dbIdStrs) throws Exception {
		String data = "";
		try {
			if (!"".equals(dbIdStrs.trim()) && dbIdStrs != null && !"".equals(fromIdStr.trim())) {
				// String[] dbIdstrArry = dbIdStrs.split(",");
				String[] fromIdStrArry = fromIdStr.split(",");
				if (dbIdStrs != null && !"".equals(dbIdStrs.trim())) {
					for (int i = 0; i < fromIdStrArry.length; i++) {
						dbIdStrs = this.returnIdStr(fromIdStrArry[i], dbIdStrs);
					}
					data = dbIdStrs;
				}
				if (data.lastIndexOf(",") != -1) {
					data = data.substring(0, data.lastIndexOf(","));
				}
			} else {
				data = dbIdStrs;
			}
		} catch (Exception e) {
			throw e;
		}
		return data;
	}

	public String returnIdStr(String fromIdStr, String idStrs) throws Exception {
		String data = "";
		try {
			if (idStrs != null && !"".equals(idStrs.trim())) {
				String temArry[] = idStrs.split(",");
				if (temArry != null && temArry.length != 0) {
					for (String idString : temArry) {
						if (idString.equals(fromIdStr.trim())) {
							continue;
						}
						data += idString + ",";
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return data;
	}

	/**
	 * 当deptId等于0时调用此方法取得所有deptId
	 * 
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public String getAlldept(Connection conn) throws Exception {
		String result = "";
		String sql = "select SEQ_ID FROM oa_department";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				int deptId = rs.getInt(1);
				if (!"".equals(result)) {
					result += ",";
				}
				result += deptId;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		if (result.endsWith(",")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	/**
	 * 批量设置权限
	 * 
	 * @param dbConn
	 * @param seqString
	 * @param setIdStr
	 * @param checks
	 * @param opt
	 * @throws Exception
	 */
	public void setBatchPrivLogic(Connection dbConn, String seqString, String setIdStr, String checks, String opt) throws Exception {
		if (checks.endsWith(",")) {
			checks = checks.substring(0, checks.length() - 1);
		}
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqString)) {
			seqId = Integer.parseInt(seqString);
		}
		try {
			String[] checkStrs = checks.split(",");
			if ("addPriv".equals(opt)) {
				if (checks != "") {
					for (int i = 0; i < checkStrs.length; i++) {
						if ("USER_ID".equals(checkStrs[i])) {
							this.updateVisitOverrideAdd(dbConn, seqId, setIdStr, "USER_ID");
						}
						if ("MANAGE_USER".equals(checkStrs[i])) {
							this.updateVisitOverrideAdd(dbConn, seqId, setIdStr, "MANAGE_USER");
						}
						if ("NEW_USER".equals(checkStrs[i])) {
							this.updateVisitOverrideAdd(dbConn, seqId, setIdStr, "NEW_USER");
						}
						if ("DOWN_USER".equals(checkStrs[i])) {
							this.updateVisitOverrideAdd(dbConn, seqId, setIdStr, "DOWN_USER");
						}
						if ("OWNER".equals(checkStrs[i])) {
							this.updateVisitOverrideAdd(dbConn, seqId, setIdStr, "OWNER");
						}
					}
				}

			} else if ("delPriv".equals(opt)) {
				if (checks != "") {
					// String[] checkStrs = checks.split(",");
					for (int i = 0; i < checkStrs.length; i++) {
						if ("USER_ID".equals(checkStrs[i])) {
							this.updateVisitOverrideDel(dbConn, seqId, setIdStr, "USER_ID");
						}
						if ("MANAGE_USER".equals(checkStrs[i])) {
							this.updateVisitOverrideDel(dbConn, seqId, setIdStr, "MANAGE_USER");
						}
						if ("NEW_USER".equals(checkStrs[i])) {
							this.updateVisitOverrideDel(dbConn, seqId, setIdStr, "NEW_USER");
						}
						if ("DOWN_USER".equals(checkStrs[i])) {
							this.updateVisitOverrideDel(dbConn, seqId, setIdStr, "DOWN_USER");
						}
						if ("OWNER".equals(checkStrs[i])) {
							this.updateVisitOverrideDel(dbConn, seqId, setIdStr, "OWNER");
						}
					}
				}
			}

		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 得到有权限的部门id串
	 * @param dbConn
	 * @param loginUserDeptId
	 * @param ids
	 * @return
	 */
	public String getPrivDeptIdStr(Connection dbConn, int loginUserDeptId, String ids) {
		String idstr = "";
		if (!YHUtility.isNullorEmpty(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp.trim()) && !"0".equals(tmp.trim())) {
					if (Integer.parseInt(tmp) == loginUserDeptId) {
						idstr += tmp + ",";
					}
				} else if (!"".equals(tmp.trim()) && "0".equals(tmp.trim())) {
					idstr += tmp + ",";
				}
			}
		}
		return idstr.trim();
	}
	/**
	 * 得到有权限的角色id串
	 * @param dbConn
	 * @param loginUserDeptId
	 * @param ids
	 * @return
	 */
	public String getPrivRoleIdStr(Connection dbConn, int loginUserRoleId, String ids) {
		String idstr = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (YHUtility.isNumber(tmp)) {
					if (Integer.parseInt(tmp) == loginUserRoleId) {
						idstr += tmp;
					}
				}
			}
		}
		return idstr.trim();
	}

	/**
	 * 得到该部门下的全体人员id串
	 * @param dbConn
	 * @param deptIdStr
	 * @return
	 * @throws Exception
	 */
	public String getDeptPersonIdStr(int userSeqId, String ids, Connection dbConn) throws Exception {
		String idStr = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp.trim()) && !"0".equals(tmp.trim())) {
					if (Integer.parseInt(tmp) == userSeqId) {
						Map deptIdmMap = new HashMap();
						deptIdmMap.put("DEPT_ID", tmp);
						List<YHPerson> list = this.getPersonsByDeptId(dbConn, deptIdmMap);
						if (list != null && list.size() != 0) {
							for (YHPerson person : list) {
								idStr += person.getSeqId() + ",";
							}
						}
					}
				} else if (!"".equals(tmp.trim()) && "0".equals(tmp.trim())) {
					String[] filters = { "dept_id!=0 and not_login=0" };
					List<YHPerson> list = this.getPersonsByDeptIdStr(dbConn, filters);
					if (list != null && list.size() != 0) {
						for (YHPerson person : list) {
							idStr += person.getSeqId() + ",";
						}
					}
				}
			}
		}
		return idStr.trim();
	}
	public List<YHPerson> getPersonsByDeptId(Connection dbConn, Map deptIdmMap) throws Exception {
		YHORM orm = new YHORM();
		try {
			List list = new ArrayList();
			return orm.loadListComplex(dbConn, YHPerson.class, deptIdmMap);
		} catch (Exception e) {
			throw e;
		}
	}
	public List<YHPerson> getPersonsByDeptIdStr(Connection dbConn, String[] filters) throws Exception {
		YHORM orm = new YHORM();
		try {
			return orm.loadListSingle(dbConn, YHPerson.class, filters);
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 得到该角色下的全体人员id串
	 * @param dbConn
	 * @param deptIdStr
	 * @return
	 * @throws Exception
	 */
	public String getRolePersonIdStr(int userSeqId, String ids, Connection dbConn) throws Exception {
		String idStr = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp)) {
					if (Integer.parseInt(tmp) == userSeqId) {
						Map deptIdmMap = new HashMap();
						deptIdmMap.put("USER_PRIV  ", tmp);
						List<YHPerson> list = this.getPersonsByDeptId(dbConn, deptIdmMap);
						if (list != null && list.size() != 0) {
							for (YHPerson person : list) {
								idStr += person.getSeqId() + ",";
							}
						}
					}
				}
			}
		}
		return idStr.trim();
	}
	
	/**
	 * 从父结点递归到子结点，
	 * @param dbConn
	 * @param seqId
	 * @return String
	 * @throws Exception
	 */
	public String getSortName(Connection dbConn, int seqId) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		YHORM orm = new YHORM();
		YHConfidentialSort fileSort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, seqId);
		getChild(dbConn, fileSort, sb, 0);
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]");
		return sb.toString();
	}
	public void getChild(Connection dbConn, YHConfidentialSort fileSort, StringBuffer sb, int flag) throws Exception {
		int seqId = fileSort.getSeqId();
		String sortName = YHUtility.null2Empty(fileSort.getSortName());
		sortName = sortName.replaceAll("[\n-\r]", "<br>");
		sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
		sortName = sortName.replace("\"", "\\\"");
		for (int i = 0; i < flag; i++) {
			sortName = "&nbsp;&nbsp;&nbsp;" + sortName;
		}
		String nameString = getAllPersonIdName(dbConn, seqId);
		sb.append("{seqId:\"" + seqId + "\",sortName:\"" + sortName + nameString + "\"},");
		Map map = new HashMap();
		map.put("SORT_PARENT", seqId);
		List<YHConfidentialSort> list = getFileSorts(dbConn, map);
		flag++;
		for (YHConfidentialSort sort : list) {
			getChild(dbConn, sort, sb, flag);
		}
	}
	
	public String getAllPersonIdName(Connection dbConn, int seqId) throws Exception {
		Map map = new HashMap();
		map.put("SEQ_ID", seqId);
		YHConfidentialSort fileSort = this.getFileSortInfoById(dbConn, map);
		String visiId = "";
		String manageId = "";
		String newUserId = "";
		String downUserId = "";
		String ownerId = "";

		String visiName = "";
		String manageName = "";
		String newUserName = "";
		String downUserName = "";
		String ownerName = "";
		String nameString = "";
		String actions[] = new String[] { "OWNER", "USER_ID", "MANAGE_USER", "NEW_USER", "DOWN_USER" };
		YHPersonLogic personLogic = new YHPersonLogic();
		for (int i = 0; i < actions.length; i++) {
			if ("OWNER".equals(actions[i])) {
				String visiUserId = this.selectManagerIds(dbConn, fileSort, "OWNER");
				String visiRoseId = this.getRoleIds(dbConn, fileSort, "OWNER");
				String visiDeptId = this.getDeptIds(dbConn, fileSort, "OWNER");
				String visiUserRoseId = "";
				String visiUserDeptId = "";
				if (!"".equals(visiRoseId.trim())) {
					visiUserRoseId = this.getUserIdsByRole(dbConn, visiRoseId);
				}
				if (!"".equals(visiDeptId)) {
					if (visiDeptId.equals("0") || "ALL_DEPT".equals(visiDeptId.trim())) {
						visiUserDeptId = "0";
					} else {
						visiUserDeptId = this.getUserIdsByDept(dbConn, visiDeptId);
					}
				}
				ownerId = this.getUserIds(visiUserId, visiUserRoseId, visiUserDeptId);
				if (!"".equals(ownerId.trim())) {
					if ("0".equals(ownerId.trim()) || "ALL_DEPT".equals(ownerId.trim())) {
						ownerName = "所有人员";
					} else {
						ownerName = personLogic.getNameBySeqIdStr(ownerId, dbConn);
					}
				}
			}
			if ("USER_ID".equals(actions[i])) {
				String visiUserId = this.selectManagerIds(dbConn, fileSort, "USER_ID");
				String visiRoseId = this.getRoleIds(dbConn, fileSort, "USER_ID");
				String visiDeptId = this.getDeptIds(dbConn, fileSort, "USER_ID");
				String visiUserRoseId = ""; // 人员的id
				String visiUserDeptId = "";
				if (!"".equals(visiRoseId.trim())) {
					visiUserRoseId = getUserIdsByRole(dbConn, visiRoseId);
				}
				if (!"".equals(visiDeptId)) {
					if ("0".equals(visiDeptId.trim()) || "ALL_DEPT".equals(visiDeptId.trim())) {
						visiUserDeptId = "0";
					} else {
						visiUserDeptId = this.getUserIdsByDept(dbConn, visiDeptId);
					}
				}
				visiId = getUserIds(visiUserId, visiUserRoseId, visiUserDeptId);
				// 与所有者的并集
				visiId = getUserId(visiId, ownerId);
				if (!visiId.equals("")) {
					if (visiId.equals("0") || "ALL_DEPT".equals(visiId.trim())) {
						visiName = "所有人员";
					} else {
						visiName = personLogic.getNameBySeqIdStr(visiId, dbConn);
					}
				}
			}
			if ("MANAGE_USER".equals(actions[i])) {
				String visiUserId = this.selectManagerIds(dbConn, fileSort, "MANAGE_USER");
				String visiRoseId = this.getRoleIds(dbConn, fileSort, "MANAGE_USER");
				String visiDeptId = this.getDeptIds(dbConn, fileSort, "MANAGE_USER");
				String visiUserRoseId = "";
				String visiUserDeptId = "";
				if (!"".equals(visiRoseId.trim())) {
					visiUserRoseId = getUserIdsByRole(dbConn, visiRoseId);
				}
				if (!visiDeptId.equals("")) {
					if ("0".equals(visiDeptId.trim()) || "ALL_DEPT".equals(visiDeptId.trim())) {
						visiUserDeptId = "0";
					} else {
						visiUserDeptId = this.getUserIdsByDept(dbConn, visiDeptId);
					}
				}
				manageId = getUserIds(visiUserId, visiUserRoseId, visiUserDeptId);
				// 与访问权限交集
				manageId = this.getUserIds(manageId, visiId);
				if (!manageId.equals("")) {
					if (manageId.equals("0") || "ALL_DEPT".equals(manageId.trim())) {
						manageName = "所有人员";
					} else {
						manageName = personLogic.getNameBySeqIdStr(manageId, dbConn);
					}
				}
			}
			if ("NEW_USER".equals(actions[i])) {
				String visiUserId = this.selectManagerIds(dbConn, fileSort, "NEW_USER");
				String visiRoseId = this.getRoleIds(dbConn, fileSort, "NEW_USER");
				String visiDeptId = this.getDeptIds(dbConn, fileSort, "NEW_USER");
				String visiUserRoseId = "";
				String visiUserDeptId = "";
				if (!visiRoseId.equals("")) {
					visiUserRoseId = getUserIdsByRole(dbConn, visiRoseId);
				}
				if (!visiDeptId.equals("")) {
					if (visiDeptId.equals("0") || "ALL_DEPT".equals(visiDeptId.trim())) {
						visiUserDeptId = "0";
					} else {
						visiUserDeptId = this.getUserIdsByDept(dbConn, visiDeptId);
					}
				}
				newUserId = getUserIds(visiUserId, visiUserRoseId, visiUserDeptId);
				// 与访问权限交集
				newUserId = getUserIds(newUserId, visiId);
				if (!newUserId.equals("")) {
					if (newUserId.equals("0") || "ALL_DEPT".equals(newUserId.trim())) {
						newUserName = "所有人员";
					} else {
						newUserName = personLogic.getNameBySeqIdStr(newUserId, dbConn);
					}
				}
			}
			if ("DOWN_USER".equals(actions[i])) {
				String visiUserId = this.selectManagerIds(dbConn, fileSort, "DOWN_USER");
				String visiRoseId = this.getRoleIds(dbConn, fileSort, "DOWN_USER");
				String visiDeptId = this.getDeptIds(dbConn, fileSort, "DOWN_USER");
				String visiUserRoseId = "";
				String visiUserDeptId = "";
				if (!visiRoseId.equals("")) {
					visiUserRoseId = this.getUserIdsByRole(dbConn, visiRoseId);
				}
				if (!visiDeptId.equals("")) {
					if (visiDeptId.equals("0") || "ALL_DEPT".equals(visiDeptId.trim())) {
						visiUserDeptId = "0";
					} else {
						visiUserDeptId = this.getUserIdsByDept(dbConn, visiDeptId);
					}
				}
				downUserId = this.getUserIds(visiUserId, visiUserRoseId, visiUserDeptId);
				// 与访问权限交集
				downUserId = this.getUserIds(downUserId, visiId);
				if (!downUserId.equals("")) {
					if (downUserId.equals("0") || "ALL_DEPT".equals(downUserId.trim())) {
						downUserName = "所有人员";
					} else {
						downUserName = personLogic.getNameBySeqIdStr(downUserId, dbConn);
					}
				}

			}

		}
		return nameString = "\",visiName:\"" + visiName + "\",manageName:\"" + manageName + "\",newUserName:\"" + newUserName + "\",downUserName:\""
				+ downUserName + "\",ownerName:\"" + ownerName;
	}
	public YHConfidentialSort getFileSortInfoById(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		return (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, map);

	}
	/**
	 * 根据角色Ids得到人员 Ids
	 * @param dbConn
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public String getUserIdsByRole(Connection dbConn, String roleId) throws Exception {
		YHORM orm = new YHORM();
		String personIds = "";
		try {
			if (!YHUtility.isNullorEmpty(roleId)) {
				Map map = new HashMap();
				String[] str = { "USER_PRIV in(" + roleId + ")" };
				List<YHPerson> personList = orm.loadListSingle(dbConn, YHPerson.class, str);
				for (int i = 0; i < personList.size(); i++) {
					YHPerson person = personList.get(i);
					personIds = personIds + person.getSeqId() + ",";
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return personIds;
	}
	/**
	 * 根据部门Ids得到人员 Ids
	 * @param dbConn
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public String getUserIdsByDept(Connection dbConn, String deptId) throws Exception {
		YHORM orm = new YHORM();
		String deptTmp = "";
		String personIds = "";
		if (!YHUtility.isNullorEmpty(deptId) && !"ALL_DEPT".equals(deptId.trim())) {
			String deptArry[] = deptId.split(",");
			if (deptArry.length != 0) {
				for (String tmp : deptArry) {
					if (!"ALL_DEPT".equals(tmp.trim())) {
						deptTmp += tmp.trim() + ",";
					}
				}
			}
			if (!YHUtility.isNullorEmpty(deptTmp)) {
				if (deptTmp.endsWith(",")) {
					deptTmp = deptTmp.substring(0, deptTmp.length()-1);
				}
				String[] str = { "DEPT_ID in(" + deptTmp + ")" };
				List<YHPerson> personList = orm.loadListSingle(dbConn, YHPerson.class, str);
				for (int i = 0; i < personList.size(); i++) {
					YHPerson person = personList.get(i);
					personIds = personIds + person.getSeqId() + ",";
				}
			}
		}
		return personIds;
	}
	/**
	 * 根据人员Ids由角色得到 的 Ids 由部门得到的Ids 得到他们的 并集
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
	public String getUserIds(String userIds, String roleIds, String deptIds) throws Exception {
		String[] userIdArray = {};
		String[] roleIdArray = {};
		String[] deptIdArray = {};
		String userId = "";
		List list1 = new ArrayList();
		List list2 = new ArrayList();
		List list3 = new ArrayList();
		List listTemp = new ArrayList();
		// 判断是否为全体部门userId为0
		if (deptIds != null && (deptIds.equals("0") || "ALL_DEPT".equals(deptIds.trim()))) {
			userId = "0";
		} else {
			if (!userIds.equals("")) {
				userIdArray = userIds.split(",");
				for (int i = 0; i < userIdArray.length; i++) {
					list1.add(userIdArray[i]);
				}
			}
			if (!roleIds.equals("")) {
				roleIdArray = roleIds.split(",");
				for (int i = 0; i < roleIdArray.length; i++) {
					list2.add(roleIdArray[i]);
				}
			}
			if (!deptIds.equals("")) {
				deptIdArray = deptIds.split(",");
				for (int i = 0; i < deptIdArray.length; i++) {
					list3.add(deptIdArray[i]);
				}
			}
			if (!userIds.equals("")) {
				for (int i = 0; i < list1.size(); i++) {
					String temp1 = (String) list1.get(i);
					if (!roleIds.equals("")) {
						for (int j = 0; j < list2.size(); j++) {
							String temp2 = (String) list2.get(j);
							if (temp1.equals(temp2)) {
								break;
							} else {
								if (!temp1.equals(temp2) && j == list2.size() - 1) {
									list2.add(list1.get(i));
									break;
								}
							}
						}
					} else {
						list2 = list1;
					}
				}
			}
			if (!deptIds.equals("")) {
				for (int i = 0; i < list3.size(); i++) {
					String temp1 = (String) list3.get(i);
					if (list2.size() > 0) {
						for (int j = 0; j < list2.size(); j++) {
							String temp2 = (String) list2.get(j);
							if (temp1.equals(temp2)) {
								break;
							} else {
								if (!temp1.equals(temp2) && j == list2.size() - 1) {
									list2.add(list3.get(i));
									break;
								}
							}
						}
					} else {
						list2 = list3;
					}
				}
			}
			for (int i = 0; i < list2.size(); i++) {
				userId = userId + list2.get(i) + ",";
			}
		}
		return userId;
	}
	/**
	 * 根据访问权限的人员和所有者权限的人员Ids 得到他们的 并集
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
	public String getUserId(String userIds, String deptIds) throws Exception {
		String[] userIdArray = {};
		String[] roleIdArray = {};
		String[] deptIdArray = {};
		String userId = "";
		List list1 = new ArrayList();
		List list2 = new ArrayList();
		List listTemp = new ArrayList();
		// 判断是否为全体部门userId为0
		if (userIds != null && !userIds.equals("")) {
			if (userIds.equals("0") || "ALL_DEPT".equals(userId.trim())) {
				userId = "0";
				return userId;
			} else {
				userIdArray = userIds.split(",");
				for (int i = 0; i < userIdArray.length; i++) {
					list1.add(userIdArray[i]);
				}
			}
		}
		if (deptIds != null && !deptIds.equals("")) {
			if (deptIds.equals("0") || "ALL_DEPT".equals(deptIds.trim())) {
				userId = "0";
				return userId;
			} else {
				deptIdArray = deptIds.split(",");
				for (int i = 0; i < deptIdArray.length; i++) {
					list2.add(deptIdArray[i]);
				}
			}
		}
		for (int i = 0; i < list2.size(); i++) {
			String temp1 = (String) list2.get(i);
			if (list1.size() > 0) {
				for (int j = 0; j < list1.size(); j++) {
					String temp2 = (String) list1.get(j);
					if (temp1.equals(temp2)) {
						break;
					} else {
						if (!temp1.equals(temp2) && j == list1.size() - 1) {
							list1.add(list2.get(i));
							break;
						}
					}
				}
			} else {
				list1 = list2;
			}
		}
		for (int i = 0; i < list1.size(); i++) {
			userId = userId + list1.get(i) + ",";
		}
		return userId;
	}
	/**
	 * 根据人员Ids由角色得到 的 Ids 由部门得到的Ids 得到他们的 并集后在得到访问权限的交集
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
	public String getUserIds(String userIds, String visiIds) throws Exception {
		String[] userIdArray = {};
		String[] visiIdArray = {};
		String userId = "";
		List list1 = new ArrayList();
		List list2 = new ArrayList();
		List list3 = new ArrayList();
		if (userIds.equals("0") && visiIds.equals("0")) {
			return "0";
		}
		if (userIds.equals("0") && !visiIds.equals("0")) {
			return visiIds;
		}
		if (!userIds.equals("0") && visiIds.equals("0")) {
			return userIds;
		}
		if (!userIds.equals("")) {
			userIdArray = userIds.split(",");
			for (int i = 0; i < userIdArray.length; i++) {
				list1.add(userIdArray[i]);
			}
		}
		if (!visiIds.equals("")) {
			visiIdArray = visiIds.split(",");
			for (int i = 0; i < visiIdArray.length; i++) {
				list2.add(visiIdArray[i]);
			}
		}
		for (int i = 0; i < list1.size(); i++) {
			String temp1 = (String) list1.get(i);
			for (int j = 0; j < list2.size(); j++) {
				String temp2 = (String) list2.get(j);
				if (temp1.equals(temp2)) {
					list3.add(list1.get(i));
				}
			}
		}
		for (int i = 0; i < list3.size(); i++) {
			userId = userId + list3.get(i) + ",";
		}
		return userId;
	}

}
