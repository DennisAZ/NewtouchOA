package yh.core.funcs.system.filefolder.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.filefolder.data.YHFileContent;
import yh.core.funcs.filefolder.logic.YHFileContentLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.filefolder.data.YHFileSort;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;

public class YHFileSortLogic {
	private static Logger log = Logger.getLogger(YHFileSortLogic.class);

	public void saveFileSortInfo(Connection dbConn, YHFileSort fileSort) {
		YHORM orm = new YHORM();
		try {
			orm.saveSingle(dbConn, fileSort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取最大的SeqId值
	 * 
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @return
	 */
	public YHFileSort getMaxSeqId(Connection dbConn) {
		// String sql="select MAX(SEQ_ID) from file_sort";
		String sql = "select SEQ_ID,SORT_NAME from oa_file_sort where SEQ_ID=(select MAX(SEQ_ID) from oa_file_sort ) ";
		YHFileSort fileSort = null;
		int seqId = 0;
		String sortName = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				fileSort = new YHFileSort();
				seqId = rs.getInt("SEQ_ID");
				sortName = rs.getString("SORT_NAME");
				fileSort.setSeqId(seqId);
				fileSort.setSortName(sortName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		return fileSort;
	}

	public List<YHFileSort> getFileSortsInfo(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHFileSort.class, map);

	}

	public List<YHFileSort> getFileFilderInfo(Connection dbConn, String[] condition) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHFileSort.class, condition);
	}

	public YHFileSort getFileSortInfoById(Connection dbConn, String seqId) throws NumberFormatException, Exception {
		YHORM orm = new YHORM();

		return (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, Integer.parseInt(seqId));
	}

	public YHFileSort getFileSortInfoById(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		return (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, map);

	}

	/**
	 * 递归获取文件夹名路径
	 * 
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public void getSortNamePath(Connection dbConn, int seqId, StringBuffer buffer) throws Exception {
		YHFileSort fileSort = getSortNameById(dbConn, seqId);

		int sortParent = 0;
		String sortName = "";
		if (fileSort != null) {

			sortParent = fileSort.getSortParent();
			sortName = fileSort.getSortName();
		}

		// 处理特殊字符
		sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
		sortName = sortName.replaceAll("[\n-\r]", "<br>");
		sortName = sortName.replace("\"", "\\\"");

		buffer.append(sortName + "/,");
		boolean flag = isHaveSortParent(dbConn, sortParent);
		if (flag) {
			getSortNamePath(dbConn, sortParent, buffer);
		}
	}

	/**
	 * 查询文件夹信息
	 * 
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public YHFileSort getSortNameById(Connection dbConn, int seqId) throws Exception {
		YHORM orm = new YHORM();
		return (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, seqId);
	}

	/**
	 * 判断是否还有子级文件夹
	 * 
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param sortParent
	 * @return
	 * @throws Exception
	 */
	public boolean isHaveSortParent(Connection dbConn, int sortParent) throws Exception {
		Boolean flag = false;
		YHORM orm = new YHORM();
		Map map = new HashMap();
		map.put("SEQ_ID", sortParent);
		List<YHFileSort> list = orm.loadListSingle(dbConn, YHFileSort.class, map);
		if (list.size() > 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 递归获取单个文件夹名称
	 * 
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public void getSortName(Connection dbConn, int seqId, StringBuffer bufferSeqId, StringBuffer bufferSortName) throws Exception {
		YHFileSort fileSort = getSortNameById(dbConn, seqId);
		int sortParent = fileSort.getSortParent();
		int folderId = fileSort.getSeqId();
		String sortName = YHUtility.null2Empty(fileSort.getSortName());
		bufferSeqId.append(folderId + ",");
		bufferSortName.append(sortName + "|");
		boolean flag = isHaveSortParent(dbConn, sortParent);
		if (flag) {
			getSortName(dbConn, sortParent, bufferSeqId, bufferSortName);
		}
	}

	/**
	 * 从父结点递归到子结点，
	 * 
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param seqId
	 * @return String
	 * @throws Exception
	 */
	public String getSortName(Connection dbConn, int seqId) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		YHORM orm = new YHORM();
		YHFileSort fileSort = (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, seqId);
		getChild(dbConn, fileSort, sb, 0);
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]");

		return sb.toString();
	}

	public void getChild(Connection dbConn, YHFileSort fileSort, StringBuffer sb, int flag) throws Exception {
		int seqId = fileSort.getSeqId();
		String sortName = YHUtility.null2Empty(fileSort.getSortName());
		// sortName.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r",
		// "").replace("\n", "");
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
		List<YHFileSort> list = getFileSorts(dbConn, map);
		flag++;
		for (YHFileSort sort : list) {
			getChild(dbConn, sort, sb, flag);
		}

	}

	public List<YHFileSort> getSortParentId(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHFileSort.class, map);
	}

	public String getAllPersonIdName(Connection dbConn, int seqId) throws Exception {
		Map map = new HashMap();
		map.put("SEQ_ID", seqId);
		YHFileSort fileSort = this.getFileSortInfoById(dbConn, map);
		String visiId = "";
		String manageId = "";
		String delId = "";
		String newUserId = "";
		String downUserId = "";
		String ownerId = "";

		String visiName = "";
		String manageName = "";
		String newUserName = "";
		String downUserName = "";
		String ownerName = "";
		String nameString = "";
		String delName = "";
		String actions[] = new String[] { "OWNER", "USER_ID", "MANAGE_USER", "DEL_USER", "NEW_USER", "DOWN_USER" };
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

				// ownerId = this.selectManagerIds(dbConn, fileSort, "OWNER");
				// if (!"".equals(ownerId)) {
				// ownerName = this.getNamesByIds(dbConn, map, "OWNER");
				// }

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

				// visiId = this.selectManagerIds(dbConn, fileSort, "USER_ID");
				// if (!visiId.equals("")) {
				// visiName = this.getNamesByIds(dbConn, map, "USER_ID");
				// }
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
				manageId = getUserIds(manageId, visiId);
				if (!manageId.equals("")) {
					if (manageId.equals("0") || "ALL_DEPT".equals(manageId.trim())) {
						manageName = "所有人员";
					} else {
						manageName = personLogic.getNameBySeqIdStr(manageId, dbConn);
					}
				}

				// manageId = selectManagerIds(dbConn, fileSort, "MANAGE_USER");
				// if (!manageId.equals("")) {
				// manageName = this.getNamesByIds(dbConn, map, "MANAGE_USER");
				// }
			}
			if ("DEL_USER".equals(actions[i])) {
        String visiUserId = this.selectManagerIds(dbConn, fileSort, "DEL_USER");
        String visiRoseId = this.getRoleIds(dbConn, fileSort, "DEL_USER");
        String visiDeptId = this.getDeptIds(dbConn, fileSort, "DEL_USER");
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
        delId = getUserIds(visiUserId, visiUserRoseId, visiUserDeptId);
        // 与访问权限交集

        delId = getUserIds(delId, visiId);
        if (!delId.equals("")) {
          if (delId.equals("0") || "ALL_DEPT".equals(delId.trim())) {
            delName = "所有人员";
          } else {
            delName = personLogic.getNameBySeqIdStr(delId, dbConn);
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

				// newUserId = this.selectManagerIds(dbConn, fileSort, "NEW_USER");
				// if (!newUserId.equals("")) {
				// newUserName = this.getNamesByIds(dbConn, map, "NEW_USER");
				// }
			}
			if ("DOWN_USER".equals(actions[i])) {
				String visiUserId = this.selectManagerIds(dbConn, fileSort, "DOWN_USER");
				String visiRoseId = this.getRoleIds(dbConn, fileSort, "DOWN_USER");
				String visiDeptId = this.getDeptIds(dbConn, fileSort, "DOWN_USER");
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
				downUserId = getUserIds(visiUserId, visiUserRoseId, visiUserDeptId);
				// 与访问权限交集
				downUserId = getUserIds(downUserId, visiId);
				if (!downUserId.equals("")) {
					if (downUserId.equals("0") || "ALL_DEPT".equals(downUserId.trim())) {
						downUserName = "所有人员";
					} else {
						downUserName = personLogic.getNameBySeqIdStr(downUserId, dbConn);
					}
				}

				// downUserId = this.selectManagerIds(dbConn, fileSort, "DOWN_USER");
				// if (!downUserId.equals("")) {
				// downUserName = this.getNamesByIds(dbConn, map, "DOWN_USER");
				// }
			}

		}
		return nameString = "\",visiName:\"" + visiName + "\",manageName:\"" + manageName + "\",newUserName:\"" + newUserName + "\",downUserName:\""
				+ downUserName + "\",ownerName:\"" + ownerName+ "\",delName:\"" + delName ;
	}

	/**
	 * 根据角色Ids得到人员 Ids
	 * 
	 * @param dbConn
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public String getUserIdsByRole(Connection dbConn, String roleId) throws Exception {
		YHORM orm = new YHORM();

		String personIds = "";
		Map map = new HashMap();
		String[] str = { "USER_PRIV in(" + roleId + ")" };
		List<YHPerson> personList = orm.loadListSingle(dbConn, YHPerson.class, str);
		for (int i = 0; i < personList.size(); i++) {
			YHPerson person = personList.get(i);
			personIds = personIds + person.getSeqId() + ",";
		}
		return personIds;
	}

	/**
	 * 根据部门Ids得到人员 Ids
	 * 
	 * @param dbConn
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public String getUserIdsByDept(Connection dbConn, String deptId) throws Exception {
		YHORM orm = new YHORM();
		String deptTmp = "";
		String personIds = "";
		Map map = new HashMap();
		if (!"".equals(deptId.trim()) && !"ALL_DEPT".equals(deptId.trim())) {

			// String[] str = {"DEPT_ID in(" + deptId + ")"};
			String deptArry[] = deptId.split(",");
			if (deptArry.length != 0) {
				for (String tmp : deptArry) {
					if (!"ALL_DEPT".equals(tmp.trim())) {
						deptTmp += tmp.trim() + ",";
					}

				}

			}
			if (!"".equals(deptTmp.trim())) {
				String deptString = deptTmp.substring(0, deptTmp.lastIndexOf(","));
				String[] str = { "DEPT_ID in(" + deptString + ")" };
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
	 * 
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
	 * 根据seqid串返回一个名字串
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public String getNameBySeqIdStr(String ids, Connection conn) throws Exception, Exception {
		String names = "";
		if (ids != null && !"".equals(ids.trim())) {
			if (ids.endsWith(",")) {
				ids = ids.substring(0, ids.length() - 1);
			}
			String query = "select USER_NAME from PERSON where SEQ_ID in (" + ids + ")";
			Statement stm = null;
			ResultSet rs = null;
			try {
				stm = conn.createStatement();
				rs = stm.executeQuery(query);
				while (rs.next()) {
					names += rs.getString("USER_NAME") + ",";
				}
			} catch (Exception ex) {
				throw ex;
			} finally {
				YHDBUtility.close(stm, rs, null);
			}
		}
		if (names.endsWith(",")) {
			names = names.substring(0, names.length() - 1);
		}
		return names;
	}

	/**
	 * 根据访问权限的人员和所有者权限的人员Ids 得到他们的 并集
	 * 
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
	 * 
	 * 
	 * 
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

	/**
	 * 递归重置所有下级子文件夹的权限
	 * 
	 * @param dbConn
	 * @param seqId
	 * @param setIdStr
	 * @throws Exception
	 */
	public void updateVisitOverride(Connection dbConn, int seqId, String setIdStr, String action) throws Exception {
		YHORM orm = new YHORM();
		YHFileSort fileSort = (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, seqId);
		getChildFolder(dbConn, fileSort, setIdStr, action);

	}

	public void getChildFolder(Connection dbConn, YHFileSort fileSort, String setIdStr, String action) throws Exception {

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
		if ("DEL_USER".equals(action)) {
      fileSort.setDelUser(setIdStr);
    }
		orm.updateSingle(dbConn, fileSort);
		Map map = new HashMap();
		map.put("SORT_PARENT", seqId);
		List<YHFileSort> list = getFileSorts(dbConn, map);

		for (YHFileSort sort : list) {
			getChildFolder(dbConn, sort, setIdStr, action);
		}

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
		YHFileSort fileSort = (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, seqId);
		getChildFolderAdd(dbConn, fileSort, setIdStr, action);

	}

	public void getChildFolderAdd(Connection dbConn, YHFileSort fileSort, String setIdStr, String action) throws Exception {
		YHFileSort fileSort2 = new YHFileSort();

		YHORM orm = new YHORM();
		int seqId = fileSort.getSeqId();
		if ("USER_ID".equals(action)) {
			String deptString = "";
			String roleString = "";
			String personString = "";
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

			// fileSort.setOwner(idStrArry);

			fileSort.setUserId(idStrArry);
		}
		if ("MANAGE_USER".equals(action)) {
			String deptString = "";
			String roleString = "";
			String personString = "";
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

			// fileSort.setOwner(idStrArry);

			fileSort.setManageUser(idStrArry);
		}
		if ("DEL_USER".equals(action)) {
      String deptString = "";
      String roleString = "";
      String personString = "";
      fileSort2.setDelUser(setIdStr);
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
      fileSort.setDelUser(idStrArry);
    }
		if ("NEW_USER".equals(action)) {
			String deptString = "";
			String roleString = "";
			String personString = "";
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
			String deptString = "";
			String roleString = "";
			String personString = "";
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

			// fileSort.setOwner(idStrArry);

			fileSort.setDownUser(idStrArry);
		}
		if ("OWNER".equals(action)) {
			String deptString = "";
			String roleString = "";
			String personString = "";
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
		List<YHFileSort> list = getFileSorts(dbConn, map);

		for (YHFileSort sort : list) {
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
		YHFileSort fileSort = (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, seqId);
		getChildFolderDel(dbConn, fileSort, setIdStr, action);

	}

	public void getChildFolderDel(Connection dbConn, YHFileSort fileSort, String setIdStr, String action) throws Exception {
		YHFileSort fileSort2 = new YHFileSort();

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
		if ("DEL_USER".equals(action)) {
      fileSort2.setDelUser(setIdStr);
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
      fileSort.setDelUser(idStrArry);
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
		List<YHFileSort> list = getFileSorts(dbConn, map);

		for (YHFileSort sort : list) {
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
		return result;
	}

	/**
	 * 更新访问权限信息
	 */
	public void updateVisitById(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		// orm.updateSingle(dbConn, fileContent);
		// orm.updateSingle(dbConn, "FILE_CONTENT", map);
		PreparedStatement ps = null;
		String sql = "UPDATE oa_file_sort SET USER_ID=? where SEQ_ID=? ";
		ps = dbConn.prepareStatement(sql);
		ps.setString(1, (String) map.get("userId"));
		ps.setInt(2, (Integer) map.get("seqId"));
		ps.executeUpdate();
		YHDBUtility.close(ps, null, log);

	}

	/**
	 * 剪贴更新上级节点信息
	 */
	public void updateFolderInfoById(Connection dbConn, int parentId, int seqId,Map<Object,Object> nodeNameMap) throws Exception {
		YHORM orm = new YHORM();
		PreparedStatement ps = null;
		String sql = "UPDATE oa_file_sort SET SORT_PARENT=?,SORT_NAME=? where SEQ_ID=? ";
		try {
			YHFileSort fileSort2= (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, seqId);
			String newSortName = "";
			String fileSort2Name = "";
			if (fileSort2!=null) {
				fileSort2Name = YHUtility.null2Empty(fileSort2.getSortName());
			}
			boolean haveFlag = this.isExistFile(dbConn, parentId, fileSort2Name);
			if (haveFlag) {
				StringBuffer buffer = new StringBuffer();
				this.copyExistFile(dbConn, buffer, parentId, fileSort2Name);
				newSortName = buffer.toString().trim();
			}else {
				newSortName = YHUtility.null2Empty(fileSort2Name);
			}
			ps = dbConn.prepareStatement(sql);
			ps.setInt(1, parentId);
			ps.setString(2, newSortName);
			ps.setInt(3, seqId);
			ps.executeUpdate();
			nodeNameMap.put("sortName", newSortName);
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(ps, null, log);
		}
	}

	/**
	 * 更新管理权限信息
	 * 
	 * @param dbConn
	 * @param map
	 * @throws Exception
	 */
	public void updateManageUserById(Connection dbConn, Map map) {
		YHORM orm = new YHORM();
		// orm.updateSingle(dbConn, fileContent);
		// orm.updateSingle(dbConn, "FILE_CONTENT", map);
		PreparedStatement ps = null;
		String sql = "UPDATE oa_file_sort SET MANAGE_USER=? where SEQ_ID=? ";
		try {
			ps = dbConn.prepareStatement(sql);
			ps.setString(1, (String) map.get("manageUser"));
			ps.setInt(2, (Integer) map.get("seqId"));
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			YHDBUtility.close(ps, null, log);
		}

	}
	/**
   * 更新管理权限信息
   * 
   * @param dbConn
   * @param map
   * @throws Exception
   */
  public void updateDelUserById(Connection dbConn, Map map) {
    YHORM orm = new YHORM();
    // orm.updateSingle(dbConn, fileContent);
    // orm.updateSingle(dbConn, "FILE_CONTENT", map);
    PreparedStatement ps = null;
    String sql = "UPDATE oa_file_sort SET DEL_USER=? where SEQ_ID=? ";
    try {
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, (String) map.get("manageUser"));
      ps.setInt(2, (Integer) map.get("seqId"));
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {

      YHDBUtility.close(ps, null, log);
    }

  }
	/**
	 * 更新新建权限信息
	 * 
	 * @param dbConn
	 * @param map
	 * @throws Exception
	 */
	public void updateNewUserById(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		// orm.updateSingle(dbConn, fileContent);
		// orm.updateSingle(dbConn, "FILE_CONTENT", map);
		PreparedStatement ps = null;
		String sql = "UPDATE oa_file_sort SET NEW_USER=? where SEQ_ID=? ";
		ps = dbConn.prepareStatement(sql);
		ps.setString(1, (String) map.get("newUser"));
		ps.setInt(2, (Integer) map.get("seqId"));
		ps.executeUpdate();
		YHDBUtility.close(ps, null, log);

	}

	/**
	 * 更新下载权限信息
	 * 
	 * @param dbConn
	 * @param map
	 * @throws Exception
	 */
	public void updateDownLoadById(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		// orm.updateSingle(dbConn, fileContent);
		// orm.updateSingle(dbConn, "FILE_CONTENT", map);
		PreparedStatement ps = null;
		String sql = "UPDATE oa_file_sort SET DOWN_USER=? where SEQ_ID=? ";
		ps = dbConn.prepareStatement(sql);
		ps.setString(1, (String) map.get("downUser"));
		ps.setInt(2, (Integer) map.get("seqId"));
		ps.executeUpdate();
		YHDBUtility.close(ps, null, log);

	}

	/**
	 * 更新下载权限信息
	 * 
	 * @param dbConn
	 * @param map
	 * @throws Exception
	 */
	public void updateOwnerById(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		// orm.updateSingle(dbConn, fileContent);
		// orm.updateSingle(dbConn, "FILE_CONTENT", map);
		PreparedStatement ps = null;
		String sql = "UPDATE oa_file_sort SET OWNER=? where SEQ_ID=? ";
		ps = dbConn.prepareStatement(sql);
		ps.setString(1, (String) map.get("owner"));
		ps.setInt(2, (Integer) map.get("seqId"));
		ps.executeUpdate();
		YHDBUtility.close(ps, null, log);

	}

	// /**
	// * 更新编辑文件夹信息
	// *
	// *
	// * @param dbConn
	// * @param map
	// * @throws Exception
	// */
	// public void updateEditFileSort(Connection dbConn, Map map) throws Exception
	// {
	// YHORM orm = new YHORM();
	// PreparedStatement ps = null;
	// String sql = "UPDATE FILE_SORT SET SORT_NO=?,SORT_NAME=? where SEQ_ID=? ";
	// ps = dbConn.prepareStatement(sql);
	// ps.setString(1, (String) map.get("sortNo"));
	// ps.setString(2, (String) map.get("sortName"));
	// ps.setInt(3, (Integer) map.get("seqId"));
	// ps.executeUpdate();
	// YHDBUtility.close(ps, null, log);
	//
	// }

	/**
	 * 删除件夹及其下的子文件和文件信息
	 * 
	 * @param dbConn
	 * @param fileSort
	 * @throws Exception
	 */
	// public void delFileSortInfoById(Connection dbConn, YHFileSort fileSort)
	// throws Exception {
	// YHORM orm = new YHORM();
	// Map map = new HashMap();
	// map.put("SORT_PARENT", fileSort.getSeqId());
	// List<YHFileSort> fileSortList = orm.loadListComplex(dbConn,
	// YHFileSort.class,map );
	// orm.deleteSingle(dbConn, fileSort);
	// for (int i = 0; i < fileSortList.size(); i++) {
	// delFileSortInfoById(dbConn,fileSortList.get(i));
	// }
	// }

	public List<YHFileSort> getFileSorts(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		return (List<YHFileSort>) orm.loadListSingle(dbConn, YHFileSort.class, map);
	}

	/**
	 * 判断是否有子级文件夹，不需要考虑权限
	 * 
	 * @param dbConn
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int isHaveChild(Connection dbConn, int id, int userSeqId, String loginUserRoleId, int loginUserDeptId) throws Exception {
		YHORM orm = new YHORM();
		Map map = new HashMap();
		Map map2 = new HashMap();
		map.put("SORT_PARENT", id);
		boolean userFlag = false;
		boolean roleFlag = false;
		boolean deptFlag = false;

		boolean ownerUserFlag = false;
		boolean ownerRoleFlag = false;
		boolean ownerDeptFlag = false;
		int count = 0;

		List<YHFileSort> list = orm.loadListSingle(dbConn, YHFileSort.class, map);
		if (list.size() > 0) {
			for (YHFileSort fileSort : list) {
				String idss = fileSort.getUserId();
				map2.put("SEQ_ID", fileSort.getSeqId());
				YHFileSort fileSort2 = this.getFileSortInfoById(dbConn, map2);
				String userPrivsIds = this.selectManagerIds(dbConn, fileSort2, "USER_ID");
				String rolePrivs = this.getRoleIds(dbConn, fileSort2, "USER_ID");
				String deptPrivs = this.getDeptIds(dbConn, fileSort2, "USER_ID");

				userFlag = this.getUserIdStr(userSeqId, userPrivsIds, dbConn);
				roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
				deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);

				String ownerUserPrivs = this.selectManagerIds(dbConn, fileSort2, "OWNER");
				String ownerRolePrivs = this.getRoleIds(dbConn, fileSort2, "OWNER");
				String ownerDeptPrivs = this.getDeptIds(dbConn, fileSort2, "OWNER");

				ownerUserFlag = this.getUserIdStr(userSeqId, ownerUserPrivs, dbConn);
				ownerRoleFlag = this.getRoleIdStr(loginUserRoleId, ownerRolePrivs, dbConn);
				ownerDeptFlag = this.getDeptIdStr(loginUserDeptId, ownerDeptPrivs, dbConn);

				if (ownerUserFlag || ownerRoleFlag || ownerDeptFlag) {
					return 1;
				} else if (userFlag == true || roleFlag == true || deptFlag == true) {
					return 1;
				}
			}
			return 0;
		} else {
			return 0;
		}
	}

	/**
	 * 判断是否有子级文件夹,考虑权限。
	 * 
	 * 
	 * 
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
		List<YHFileSort> list = orm.loadListSingle(dbConn, YHFileSort.class, map);
		if (list.size() > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/*
	 * 得到人员的id字符串
	 */
	public String selectManagerIds(Connection dbConn, YHFileSort fileSort, String action) throws Exception {
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
					// System.out.println(idsStrings);
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
			}  else if ("DEL_USER".equals(action)) {
        if (fileSort.getDelUser() != null) {
          String idString = YHUtility.null2Empty(fileSort.getDelUser());
          String[] idsStrings = idString.split("\\|");
          // System.out.println(idsStrings);
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
					// System.out.println(idsStrings);
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
					// System.out.println(idsStrings);
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
					// System.out.println(idsStrings);

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
		// System.out.println("ids=====" + ids);
		return ids;
	}

	/*
	 * 根据人员id字符串得到name字符串
	 */
	public String getNamesByIds(Connection dbConn, Map map, String action) throws Exception {
		String names = "";
		YHFileSort fileSort = this.getFileSortInfoById(dbConn, map);
		YHPersonLogic tpl = new YHPersonLogic();
		String ids = selectManagerIds(dbConn, fileSort, action);
		// System.out.println(ids);
		names = tpl.getNameBySeqIdStr(ids, dbConn);
		return names;
	}

	/**
	 * 得到角色人员的id字符串
	 * 
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getRoleIds(Connection dbConn, YHFileSort fileSort, String action) throws Exception {
		YHORM orm = new YHORM();
		String ids = "";
		// ArrayList<YHFileSort> managerList = (ArrayList<YHFileSort>)
		// orm.loadListSingle(dbConn, YHFileSort.class, map);
		if (fileSort != null) {
			// YHFileSort manager = managerList.get(0);
			if ("USER_ID".equals(action)) {
				if (fileSort.getUserId() != null) {
					String idString = YHUtility.null2Empty(fileSort.getUserId());
					// System.out.println(idString);
					String[] idsStrings = idString.split("\\|");
					// System.out.println("角色idsStrings:" + idsStrings.length);
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
					// System.out.println(idString);
					String[] idsStrings = idString.split("\\|");
					// System.out.println("角色idsStrings:" + idsStrings.length);
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 1) {
							ids = "";
						} else {
							ids = idsStrings[1];
						}
					}
				}
			}  else if ("DEL_USER".equals(action)) {
        if (fileSort.getDelUser() != null) {
          String idString = fileSort.getDelUser();
          // System.out.println(idString);
          String[] idsStrings = idString.split("\\|");
          // System.out.println("角色idsStrings:" + idsStrings.length);
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
					// System.out.println(idString);
					String[] idsStrings = idString.split("\\|");
					// System.out.println("角色idsStrings:" + idsStrings.length);
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
					// System.out.println(idString);
					String[] idsStrings = idString.split("\\|");
					// System.out.println("角色idsStrings:" + idsStrings.length);
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
					// System.out.println(idString);
					String[] idsStrings = idString.split("\\|");
					// System.out.println("角色idsStrings:" + idsStrings.length);
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
		// System.out.println("ids=====" + ids);
		return ids;
	}

	/**
	 * 根据角色人员id字符串得到name字符串
	 * 
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getRoleNamesByIds(Connection dbConn, Map map, String action) throws Exception {
		String names = "";
		YHFileSortLogic tpl = new YHFileSortLogic();
		YHFileSort fileSort = this.getFileSortInfoById(dbConn, map);
		String ids = getRoleIds(dbConn, fileSort, action);
		// System.out.println(ids);
		names = tpl.getRoleNameBySeqIdStr(dbConn, ids);
		return names;
	}

	/**
	 * 根据userId中的角色Id串返回与登录的角色Id比较判断是否相等，返回boolean类型。
	 * 
	 * 
	 * 
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public boolean getRoleIdStr(String loginUserRoleId, String ids, Connection dbConn) throws Exception, Exception {
		boolean flag = false;
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp)) {
					if (Integer.parseInt(tmp) == Integer.parseInt(loginUserRoleId)) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 根据seqId串返回一个角色名字串
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public String getRoleNameBySeqIdStr(Connection dbConn, String ids) throws Exception, Exception {
		String names = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp)) {
					// YHPerson person = this.getPersonById(Integer.parseInt(tmp));
					YHUserPriv privName = this.getPersonById(dbConn, Integer.parseInt(tmp));
					if (privName != null) {
						names += privName.getPrivName() + ",";
					}
				}
			}
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
		;
		return userPriv;
	}

	/**
	 * 得到部门人员的id字符串
	 * 
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getDeptIds(Connection dbConn, YHFileSort fileSort, String action) throws Exception {
		YHORM orm = new YHORM();
		String ids = "";
		// ArrayList<YHFileSort> managerList = (ArrayList<YHFileSort>)
		// orm.loadListSingle(dbConn, YHFileSort.class, map);
		if (fileSort != null) {
			// YHFileSort manager = managerList.get(0);
			if ("USER_ID".equals(action)) {
				if (fileSort.getUserId() != null) {
					String idString = YHUtility.null2Empty(fileSort.getUserId());
					// System.out.println(idString);
					String[] idsStrings = idString.split("\\|");
					// System.out.println(idsStrings);

					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}
				}
			} else if ("MANAGE_USER".equals(action)) {
				if (fileSort.getManageUser() != null) {
					String idString = YHUtility.null2Empty(fileSort.getManageUser());
					// System.out.println(idString);
					String[] idsStrings = idString.split("\\|");
					// System.out.println(idsStrings);
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}
				}
			} else if ("DEL_USER".equals(action)) {
        if (fileSort.getDelUser() != null) {
          String idString = YHUtility.null2Empty(fileSort.getDelUser());
          // System.out.println(idString);
          String[] idsStrings = idString.split("\\|");
          // System.out.println(idsStrings);
          if (!"".equals(idString.trim()) && idsStrings.length != 0) {
            ids = idsStrings[0];
          }
        }
      } else if ("NEW_USER".equals(action)) {
				if (fileSort.getNewUser() != null) {
					String idString = YHUtility.null2Empty(fileSort.getNewUser());
					// System.out.println(idString);
					String[] idsStrings = idString.split("\\|");
					// System.out.println(idsStrings);
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}
				}
			} else if ("DOWN_USER".equals(action)) {
				if (fileSort.getDownUser() != null) {
					String idString = YHUtility.null2Empty(fileSort.getDownUser());
					// System.out.println(idString);
					String[] idsStrings = idString.split("\\|");
					// System.out.println(idsStrings);
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}
				}
			} else if ("OWNER".equals(action)) {
				if (fileSort.getOwner() != null) {
					String idString = YHUtility.null2Empty(fileSort.getOwner());
					// System.out.println(idString);
					String[] idsStrings = idString.split("\\|");
					// System.out.println(idsStrings);
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}
				}
			}

		}
		// System.out.println("ids=====" + ids);
		return ids;
	}

	/**
	 * 根据部门id字符串得到name字符串
	 * 
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getDeptByIds(Connection dbConn, Map map, String action) throws Exception {
		String names = "";
		YHFileSortLogic tpl = new YHFileSortLogic();
		YHFileSort fileSort = this.getFileSortInfoById(dbConn, map);
		String ids = getDeptIds(dbConn, fileSort, action);
		// System.out.println(ids);
		if (!"ALL_DEPT".equals(ids.trim())) {
			names = tpl.getDeptNameBySeqIdStr(dbConn, ids);
		}
		return names;
	}

	/**
	 * 根据seqId串返回一个部门名字串
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public String getDeptNameBySeqIdStr(Connection dbConn, String ids) throws Exception, Exception {
		String names = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp.trim()) && !"ALL_DEPT".equals(tmp.trim())) {
					YHDepartment deptName = getDeptById(dbConn, Integer.parseInt(tmp.trim()));
					if (deptName != null) {
						names += deptName.getDeptName() + ",";
					}
				}
			}
		}
		return names;
	}

	/**
	 * 根据登录用户的部门Id与权限中的部门Id对比返回boolean
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public boolean getDeptIdStr(int loginUserDeptId, String ids, Connection dbConn) throws Exception, Exception {
		boolean flag = false;
		if (ids != null && !"".equals(ids)) {
			if ("0".equals(ids.trim()) || "ALL_DEPT".equals(ids.trim())) {
				return flag = true;
			}
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp.trim())) {
					if (loginUserDeptId == Integer.parseInt(tmp)) {
						flag = true;
					}
				}
			}
		}
		return flag;
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
		;
		return department;
	}

	/**
	 * 得到该部门下的全体人员id串(有权限)
	 * 
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
					// Map deptIdmMap = new HashMap();
					// deptIdmMap.put("DEPT_ID", tmp);
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

	/**
	 * 得到该部门下的全体人员id串(无权限)
	 * 
	 * @param dbConn
	 * @param deptIdStr
	 * @return
	 * @throws Exception
	 */
	public String getDeptPersonIds(String ids, Connection dbConn) throws Exception {
		String idStr = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!YHUtility.isNullorEmpty(tmp.trim()) && !"0".equals(tmp.trim())) {
					Map deptIdmMap = new HashMap();
					deptIdmMap.put("DEPT_ID", tmp);
					List<YHPerson> list = this.getPersonsByDeptId(dbConn, deptIdmMap);
					if (list != null && list.size() != 0) {
						for (YHPerson person : list) {
							idStr += person.getSeqId() + ",";
						}
					}
				} else if (!YHUtility.isNullorEmpty(tmp.trim()) && "0".equals(tmp.trim())) {
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

	public List<YHPerson> getPersonsByDeptIdStr(Connection dbConn, String[] filters) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHPerson.class, filters);
	}

	/**
	 * 得到有权限的部门id串
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param loginUserDeptId
	 * @param ids
	 * @return
	 */
	public String getPrivDeptIdStr(Connection dbConn, int loginUserDeptId, String ids) {
		String idstr = "";
		if (ids != null && !"".equals(ids)) {
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
	 * 得到该角色下的全体人员id串(有权限)
	 * 
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
	 * 得到该角色下的全体人员id串(无权限)
	 * 
	 * @param dbConn
	 * @param deptIdStr
	 * @return
	 * @throws Exception
	 */
	public String getRolePersonIds(String ids, Connection dbConn) throws Exception {
		String idStr = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp)) {
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
		return idStr.trim();
	}

	/**
	 * 得到有权限的角色id串
	 * 
	 * 
	 * 
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
				if (!"".equals(tmp)) {
					if (Integer.parseInt(tmp) == loginUserRoleId) {
						idstr += tmp;
					}
				}
			}
		}

		return idstr.trim();
	}

	public List<YHPerson> getPersonsByDeptId(Connection dbConn, Map deptIdmMap) throws Exception {
		YHORM orm = new YHORM();
		List list = new ArrayList();

		return orm.loadListComplex(dbConn, YHPerson.class, deptIdmMap);

		// return list;
	}

	/**
	 * 获取访问权限：根据ids串返回与登录的seqId比较判断是否相等，返回boolean类型。
	 * 
	 * 
	 * 
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public boolean getUserIdStr(int userSeqId, String ids, Connection dbConn) throws Exception, Exception {
		boolean flag = false;
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp)) {
					if (Integer.parseInt(tmp) == userSeqId) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 获取访问权限
	 * 
	 * @param dbConn
	 * @param map
	 * @param loginUserSeqId
	 * @param loginUserDeptId
	 * @param loginUserRoleId
	 * @return
	 * @throws Exception
	 */
	public String getVisiPriv(Connection dbConn, Map map, int loginUserSeqId, int loginUserDeptId, String loginUserRoleId) throws Exception {
		StringBuffer sb = new StringBuffer("[");
		int visiPrivFlag = 0;
		int managePrivFlag = 0;
		int delPrivFlag = 0;
		int newPrivFlag = 0;
		int downPrivFlag = 0;
		int ownerPrivFlag = 0;
		int downUserPrivFlag = 0;
		int sortSeqId = 0;
		YHORM orm = new YHORM();
		YHFileSort fileSort = (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, map);
		int sortParent = 0;
		if (fileSort != null) {
			sortParent = fileSort.getSortParent();

		}
		String[] actions = new String[] { "USER_ID", "MANAGE_USER", "DEL_USER", "NEW_USER", "DOWN_USER", "OWNER" };
		for (int i = 0; i < actions.length; i++) {
			if ("USER_ID".equals(actions[i])) {
				String userPrivs = this.selectManagerIds(dbConn, fileSort, "USER_ID");
				String rolePrivs = this.getRoleIds(dbConn, fileSort, "USER_ID");
				String deptPrivs = this.getDeptIds(dbConn, fileSort, "USER_ID");

				boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
				boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
				boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
				if (userFlag || deptFlag || roleFlag) {
					visiPrivFlag = 1;
				}
			}
			if ("MANAGE_USER".equals(actions[i])) {
				String userPrivs = this.selectManagerIds(dbConn, fileSort, "MANAGE_USER");
				String rolePrivs = this.getRoleIds(dbConn, fileSort, "MANAGE_USER");
				String deptPrivs = this.getDeptIds(dbConn, fileSort, "MANAGE_USER");

				boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
				boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
				boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
				if (userFlag || deptFlag || roleFlag) {
					managePrivFlag = 1;
				}
			}
			if ("DEL_USER".equals(actions[i])) {
        String userPrivs = this.selectManagerIds(dbConn, fileSort, "DEL_USER");
        String rolePrivs = this.getRoleIds(dbConn, fileSort, "DEL_USER");
        String deptPrivs = this.getDeptIds(dbConn, fileSort, "DEL_USER");

        boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
        boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
        boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
        if (userFlag || deptFlag || roleFlag) {
          delPrivFlag = 1;
        }
      }
			if ("NEW_USER".equals(actions[i])) {
				String userPrivs = this.selectManagerIds(dbConn, fileSort, "NEW_USER");
				String rolePrivs = this.getRoleIds(dbConn, fileSort, "NEW_USER");
				String deptPrivs = this.getDeptIds(dbConn, fileSort, "NEW_USER");

				boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
				boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
				boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
				if (userFlag || deptFlag || roleFlag) {
					newPrivFlag = 1;
				}
			}
			if ("DOWN_USER".equals(actions[i])) {
				String userPrivs = this.selectManagerIds(dbConn, fileSort, "DOWN_USER");
				String rolePrivs = this.getRoleIds(dbConn, fileSort, "DOWN_USER");
				String deptPrivs = this.getDeptIds(dbConn, fileSort, "DOWN_USER");

				boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
				boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
				boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
				if (userFlag || deptFlag || roleFlag) {
					downPrivFlag = 1;
				}
			}
			if ("OWNER".equals(actions[i])) {
				String userPrivs = this.selectManagerIds(dbConn, fileSort, "OWNER");
				String rolePrivs = this.getRoleIds(dbConn, fileSort, "OWNER");
				String deptPrivs = this.getDeptIds(dbConn, fileSort, "OWNER");

				boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
				boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
				boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
				if (userFlag || deptFlag || roleFlag) {
					ownerPrivFlag = 1;
				}
			}
			if ("DOWN_USER".equals(actions[i])) {
				String userPrivs = this.selectManagerIds(dbConn, fileSort, "DOWN_USER");
				String rolePrivs = this.getRoleIds(dbConn, fileSort, "DOWN_USER");
				String deptPrivs = this.getDeptIds(dbConn, fileSort, "DOWN_USER");

				boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
				boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
				boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
				if (userFlag || deptFlag || roleFlag) {
					downUserPrivFlag = 1;
				}
			}
		}

		if (fileSort != null) {
			sortSeqId = fileSort.getSeqId();
		}

		sb.append("{");
		sb.append("visiPriv:\"" + visiPrivFlag + "\"");
		sb.append(",managePriv:\"" + managePrivFlag + "\"");
		sb.append(",delPriv:\"" + delPrivFlag + "\"");
		sb.append(",newPriv:\"" + newPrivFlag + "\"");
		sb.append(",downPriv:\"" + downPrivFlag + "\"");
		sb.append(",ownerPriv:\"" + ownerPrivFlag + "\"");
		sb.append(",downUserPriv:\"" + downUserPrivFlag + "\"");
		sb.append(",sortParent:\"" + sortParent + "\"");
		sb.append(",seqId:\"" + sortSeqId + "\"");
		sb.append("}");
		sb.append("]");

		return sb.toString();
	}

	/**
	 * 取得访问权限（有userId或owner权限之一）
	 * 
	 * 
	 * @param dbConn
	 * @param loginUser
	 * @param fileSort
	 * @return
	 * @throws Exception
	 */
	public boolean getAccessPriv(Connection dbConn, YHPerson loginUser, YHFileSort fileSort) throws Exception {
		boolean returnFlag = false;

		// 获取登录用户信息
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		int visiPrivFlag = 0;
		int ownerPrivFlag = 0;

		String[] actions = new String[] { "USER_ID", "OWNER" };

		try {
			for (int i = 0; i < actions.length; i++) {
				if ("USER_ID".equals(actions[i])) {
					String userPrivs = this.selectManagerIds(dbConn, fileSort, "USER_ID");
					String rolePrivs = this.getRoleIds(dbConn, fileSort, "USER_ID");
					String deptPrivs = this.getDeptIds(dbConn, fileSort, "USER_ID");

					boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					if (userFlag || deptFlag || roleFlag) {
						visiPrivFlag = 1;
					}
				}
				if ("OWNER".equals(actions[i])) {
					String userPrivs = this.selectManagerIds(dbConn, fileSort, "OWNER");
					String rolePrivs = this.getRoleIds(dbConn, fileSort, "OWNER");
					String deptPrivs = this.getDeptIds(dbConn, fileSort, "OWNER");

					boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					if (userFlag || deptFlag || roleFlag) {
						ownerPrivFlag = 1;
					}
				}

			}

			if (visiPrivFlag == 1 || ownerPrivFlag == 1) {
				returnFlag = true;
			}

		} catch (Exception e) {
			throw e;
		}

		return returnFlag;
	}

	/**
	 * 取得访问权限（只取userId下的权限）
	 * 
	 * 
	 * @param dbConn
	 * @param loginUser
	 * @param fileSort
	 * @return
	 * @throws Exception
	 */
	public boolean getUserIdAccessPriv(Connection dbConn, YHPerson loginUser, YHFileSort fileSort) throws Exception {
		boolean returnFlag = false;

		// 获取登录用户信息
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		String[] actions = new String[] { "USER_ID" };

		try {
			for (int i = 0; i < actions.length; i++) {
				if ("USER_ID".equals(actions[i])) {
					String userPrivs = this.selectManagerIds(dbConn, fileSort, "USER_ID");
					String rolePrivs = this.getRoleIds(dbConn, fileSort, "USER_ID");
					String deptPrivs = this.getDeptIds(dbConn, fileSort, "USER_ID");

					boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					if (userFlag || deptFlag || roleFlag) {
						returnFlag = true;
					}
				}

			}

		} catch (Exception e) {
			throw e;
		}

		return returnFlag;
	}

	/**
	 * 取得管理权限（只取MANAGE_USER下的权限）
	 * 
	 * 
	 * @param dbConn
	 * @param loginUser
	 * @param fileSort
	 * @return
	 * @throws Exception
	 */
	public boolean getManageAccessPriv(Connection dbConn, YHPerson loginUser, YHFileSort fileSort) throws Exception {
		boolean returnFlag = false;

		// 获取登录用户信息
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		String[] actions = new String[] { "MANAGE_USER" };

		try {
			for (int i = 0; i < actions.length; i++) {
				if ("MANAGE_USER".equals(actions[i])) {
					String userPrivs = this.selectManagerIds(dbConn, fileSort, "MANAGE_USER");
					String rolePrivs = this.getRoleIds(dbConn, fileSort, "MANAGE_USER");
					String deptPrivs = this.getDeptIds(dbConn, fileSort, "MANAGE_USER");

					boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					if (userFlag || deptFlag || roleFlag) {
						returnFlag = true;
					}
				}

			}

		} catch (Exception e) {
			throw e;
		}

		return returnFlag;
	}

	/**
	 * 取得下载权限（只取DOWN_USER下的权限）
	 * 
	 * 
	 * @param dbConn
	 * @param loginUser
	 * @param fileSort
	 * @return
	 * @throws Exception
	 */
	public boolean getDownAccessPriv(Connection dbConn, YHPerson loginUser, YHFileSort fileSort) throws Exception {
		boolean returnFlag = false;

		// 获取登录用户信息
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		String[] actions = new String[] { "DOWN_USER" };

		try {
			for (int i = 0; i < actions.length; i++) {
				if ("DOWN_USER".equals(actions[i])) {
					String userPrivs = this.selectManagerIds(dbConn, fileSort, "DOWN_USER");
					String rolePrivs = this.getRoleIds(dbConn, fileSort, "DOWN_USER");
					String deptPrivs = this.getDeptIds(dbConn, fileSort, "DOWN_USER");

					boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					if (userFlag || deptFlag || roleFlag) {
						returnFlag = true;
					}
				}

			}

		} catch (Exception e) {
			throw e;
		}

		return returnFlag;
	}

	/**
	 * 取得新建权限（只取MANAGE_USER下的权限）
	 * 
	 * 
	 * @param dbConn
	 * @param loginUser
	 * @param fileSort
	 * @return
	 * @throws Exception
	 */
	public boolean getNewUserAccessPriv(Connection dbConn, YHPerson loginUser, YHFileSort fileSort) throws Exception {
		boolean returnFlag = false;

		// 获取登录用户信息
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		String[] actions = new String[] { "NEW_USER" };

		try {
			for (int i = 0; i < actions.length; i++) {
				if ("NEW_USER".equals(actions[i])) {
					String userPrivs = this.selectManagerIds(dbConn, fileSort, "NEW_USER");
					String rolePrivs = this.getRoleIds(dbConn, fileSort, "NEW_USER");
					String deptPrivs = this.getDeptIds(dbConn, fileSort, "NEW_USER");

					boolean userFlag = this.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = this.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = this.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					if (userFlag || deptFlag || roleFlag) {
						returnFlag = true;
					}
				}

			}

		} catch (Exception e) {
			throw e;
		}

		return returnFlag;
	}

	/**
	 * 得到本级以及其所有子文件夹的对象以及每个文件夹属于第几级
	 * 
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public List getAllFolderList(Connection dbConn, int seqId, int parentId, List listTemp,Map<Object,Object> nodeNameMap, int maxSeqId) throws Exception {
		String seqIdString = "";
		YHORM orm = new YHORM();
		Map map = new HashMap();
		try {
			map.put("SORT_PARENT", seqId);
			List<YHFileSort> list = new ArrayList<YHFileSort>();
			list = orm.loadListSingle(dbConn, YHFileSort.class, map);
			if (seqId > maxSeqId) {
				return list;
			}
			int newParent = getChildFolder(dbConn, seqId, parentId, listTemp,nodeNameMap); // 返回新文件夹的seqId，

			YHFileContentLogic contentLogic = new YHFileContentLogic();
			Map contentMap = new HashMap();
			contentMap.put("SORT_ID", seqId);
			List<YHFileContent> fileContents = contentLogic.getFileContentsInfo(dbConn, contentMap);
			if (fileContents != null && fileContents.size() > 0) {
				for (int i = 0; i < fileContents.size(); i++) {
					YHFileContent content = fileContents.get(i);
					seqIdString += content.getSeqId() + ",";
				}
				copyAllFile(dbConn, newParent, seqIdString.substring(0, seqIdString.length() - 1));
			}
			for (int i = 0; i < list.size(); i++) {
				YHFileSort dimension = list.get(i);
				getAllFolderList(dbConn, dimension.getSeqId(), newParent, listTemp,nodeNameMap, maxSeqId);
			}
			return listTemp;
		} catch (Exception e) {
			throw e;
		}
		
	}

	/**
	 * 以点击粘贴文件夹的信息为准，创建一个新的文件夹
	 * 
	 * @param dbConn
	 * @param seqId
	 * @param parentId
	 * @param listTemp
	 * @return
	 * @throws Exception
	 */
	public int getChildFolder(Connection dbConn, int seqId, int parentId, List listTemp,Map<Object,Object> nodeNameMap) throws Exception {
		YHORM orm = new YHORM();
		YHFileSort fileSort = (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, parentId); // 获取点击粘贴时的文件夹对象信息		YHFileSort fileSort2 = (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, seqId); // 获取点击复制时的文件夹对象信息
		try {
			String newSortName = "";
			String fileSort2Name = "";
			if (fileSort2!=null) {
				fileSort2Name = YHUtility.null2Empty(fileSort2.getSortName());
			}
				
			boolean haveFlag = this.isExistFile(dbConn, parentId, fileSort2Name);
			if (haveFlag) {
				StringBuffer buffer = new StringBuffer();
				this.copyExistFile(dbConn, buffer, parentId, fileSort2Name);
				newSortName = buffer.toString().trim();
			}else {
				newSortName = YHUtility.null2Empty(fileSort2Name);
			}
			
		// fileSort.setDeptId(fileSort.getDeptId());
			// fileSort.setDownUser(fileSort.getDownUser());
			// fileSort.setManageUser(fileSort.getManageUser());
			// fileSort.setNewUser(fileSort.getNewUser());
			// fileSort.setOwner(fileSort.getOwner());
			// fileSort.setShareUser(fileSort.getShareUser());
			// fileSort.setSortNo(fileSort.getSortNo());
			// fileSort.setUserId(fileSort.getUserId());
			// fileSort.setSortType(fileSort.getSortType());
			fileSort.setSortParent(parentId);
			fileSort.setSortName(newSortName);
			orm.saveSingle(dbConn, fileSort);
			YHFileSort maxfiSort = getMaxSeqId(dbConn);
			listTemp.add(maxfiSort.getSeqId());
			nodeNameMap.put("sortName", newSortName);
			return maxfiSort.getSeqId();
			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 级联复制文件
	 * 
	 * @param dbConn
	 * @param seqIdStrs
	 * @param filePath
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public void copyAllFile(Connection dbConn, int sortId, String seqIdStrs) throws NumberFormatException, Exception {
		String separator = File.separator;
		String filePath = YHSysProps.getAttachPath() + separator + "file_folder" + separator;

		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(date);
		// String newAttaName = "";
		// String newAttaId = "";

		YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();
		String rand = emut.getRandom();

		YHORM orm = new YHORM();
		YHFileContentLogic contentLogic = new YHFileContentLogic();
		String[] seqIdStr = seqIdStrs.split(",");
		if (!"".equals(seqIdStrs) && seqIdStrs.split(",").length > 0) {
			for (String seqId : seqIdStr) {
				String newAttaId = "";
				String newAttaName = "";
				boolean isHave = false;

				YHFileContent fileContent = contentLogic.getFileContentInfoById(dbConn, Integer.parseInt(seqId));
				String attachmentId = YHUtility.null2Empty(fileContent.getAttachmentId());
				String attachmentName = YHUtility.null2Empty(fileContent.getAttachmentName());
				String[] attIdArray = attachmentId.split(",");
				String[] attNameArray = attachmentName.split("\\*");
				for (int i = 0; i < attIdArray.length; i++) {
					Map<String, String> map = contentLogic.getFileName(attIdArray[i], attNameArray[i]);
					if (map.size() != 0) {
						Set<String> set = map.keySet();
						// 遍历Set集合
						for (String keySet : set) {
							String key = keySet;
							String keyValue = map.get(keySet);
							String attaIdStr = contentLogic.getAttaId(keySet);
							String newAttName = rand + "_" + keyValue;

							String fileNameValue = attaIdStr + "_" + keyValue;
							String fileFolder = contentLogic.getFilePathFolder(key);

							String oldFileNameValue = attaIdStr + "." + keyValue;

							// File file = new File(filePath + File.separator + fileFolder);
							File file = new File(filePath + File.separator + fileFolder + File.separator + fileNameValue);
							File oldFile = new File(filePath + File.separator + fileFolder + File.separator + oldFileNameValue);

							if (file.exists()) {
								YHFileUtility.copyFile(file.getAbsolutePath(), filePath + File.separator + currDate + File.separator + newAttName);
								newAttaId += currDate + "_" + rand + ",";
								newAttaName += keyValue + "*";
								isHave = true;
							} else if (oldFile.exists()) {
								YHFileUtility.copyFile(oldFile.getAbsolutePath(), filePath + File.separator + currDate + File.separator + newAttName);
								newAttaId += currDate + "_" + rand + ",";
								newAttaName += keyValue + "*";
								isHave = true;
							}
						}
					}
				}
				if (isHave) {
					YHFileContent content = new YHFileContent();
					content.setAttachmentId(newAttaId.trim());
					content.setAttachmentName(newAttaName.trim());
					content.setSendTime(YHUtility.parseTimeStamp());
					content.setSortId(sortId);
					content.setSubject(fileContent.getSubject());
					orm.saveSingle(dbConn, content);
				} else {
					// YHFileContent content = new YHFileContent();
					fileContent.setSortId(sortId);
					fileContent.setSendTime(YHUtility.parseTimeStamp());
					orm.saveSingle(dbConn, fileContent);
				}
			}
		}
	}

	/**
	 * 返回人员的部门Id串
	 * 
	 * 
	 * 
	 * @param userSeqId
	 * @param ids
	 * @param dbConn
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public String getUserDeptIdStr(int userSeqId, String ids, Connection dbConn) throws Exception, Exception {
		String deptIdstr = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp)) {
					if (Integer.parseInt(tmp) == userSeqId) {
						// 返回有权限人员的部门id串,用到以下两个表
						// YHPerson
						// YHDepartment

						deptIdstr += tmp + ",";
					}
				}
			}
		}
		return deptIdstr.trim();
	}

	public List<YHFileSort> getFileSorts(Connection dbConn, String[] filters) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHFileSort.class, filters);
	}

	/**
	 * 验证文件夹名是否存在（新建）
	 * 
	 * @param dbConn
	 * @param seqId
	 * @param loginUserSeqId
	 * @param folderName
	 * @return
	 */
	public boolean checkFolderName(Connection dbConn, int seqId, String folderName) {
		boolean isHave = false;
		// String sortType = "4";
		// boolean userFlag=false;

		try {

			if (seqId == 0) {
				String[] filters = { "SORT_PARENT=" + seqId + " AND (SORT_TYPE !='4' or SORT_TYPE is null)" };
				List<YHFileSort> parentList = this.getFileSorts(dbConn, filters);
				if (parentList != null && parentList.size() > 0) {
					for (YHFileSort fileFolder : parentList) {
						String sortNameString = YHUtility.null2Empty(fileFolder.getSortName());
						if (folderName.trim().equals(sortNameString.trim())) {
							isHave = true;
							break;
						}
					}

				}

			} else {
				String[] filters = { "SORT_PARENT=" + seqId };
				List<YHFileSort> parentList = this.getFileSorts(dbConn, filters);
				if (parentList != null && parentList.size() > 0) {
					for (YHFileSort fileFolder : parentList) {
						String sortNameString = YHUtility.null2Empty(fileFolder.getSortName());
						if (folderName.trim().equals(sortNameString.trim())) {
							isHave = true;
							break;
						}

					}

				}

			}

		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}

		return isHave;

	}

	/**
	 * 验证文件夹名是否存在（编辑）
	 * 
	 * @param dbConn
	 * @param seqId
	 * @param loginUserSeqId
	 * @param folderName
	 * @return
	 * @throws Exception 
	 */
	public boolean checkEditFolder(Connection dbConn, int seqId, String folderName) throws Exception {
		boolean isHave = false;
		// String sortType = "4";
		// boolean userFlag=false;

		try {

			YHFileSort fileSort = this.getFileSortInfoById(dbConn, String.valueOf(seqId));
			int sortParentId = fileSort.getSortParent();

			if (sortParentId == 0) {
				String[] filters = { "SORT_PARENT=" + sortParentId + " AND (SORT_TYPE !='4' or SORT_TYPE is null)" };
				List<YHFileSort> parentList = this.getFileSorts(dbConn, filters);
				if (parentList != null && parentList.size() > 0) {
					for (YHFileSort fileFolder : parentList) {
						int sortId = fileFolder.getSeqId();
						String sortNameString = YHUtility.null2Empty(fileFolder.getSortName());
						if (sortId != seqId) {
							if (folderName.trim().equals(sortNameString.trim())) {
								isHave = true;
								break;
							}
						}

					}

				}

			} else {
				String[] filters = { "SORT_PARENT=" + sortParentId };
				List<YHFileSort> parentList = this.getFileSorts(dbConn, filters);
				if (parentList != null && parentList.size() > 0) {
					for (YHFileSort fileFolder : parentList) {
						int sortId = fileFolder.getSeqId();
						String sortNameString = YHUtility.null2Empty(fileFolder.getSortName());
						if (sortId != seqId) {
							if (folderName.trim().equals(sortNameString.trim())) {
								isHave = true;
								break;
							}
						}
					}

				}

			}

		} catch (Exception e) {
			throw e;
		}

		return isHave;

	}

	public YHFileSort getFolderInfoById(Connection dbConn, int seqId) throws Exception {
		YHORM orm = new YHORM();
		return (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, seqId);
	}

	public void updateSingleObj(Connection dbConn, YHFileSort fileSort) throws Exception {
		YHORM orm = new YHORM();
		orm.updateSingle(dbConn, fileSort);
	}
	
	/**
	 * 判断库是否已有文件夹
	 * 
	 * @param dbConn
	 * @param sortId
	 * @param subject
	 * @return
	 * @throws Exception
	 */
	public boolean isExistFile(Connection dbConn, int sortParentId, String sortName) throws Exception {
		boolean flag = false;
		int counter = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select count(SEQ_ID) from oa_file_sort where SORT_PARENT =? and SORT_NAME=?";
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setInt(1, sortParentId);
			stmt.setString(2, sortName);
			rs = stmt.executeQuery();
			if (rs.next()) {
				counter = rs.getInt(1);
			}
			if (counter > 0) {
				flag = true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return flag;
	}
	/**
	 * 文件夹里已存在的处理方法
	 * 
	 * @param dbConn
	 * @param buffer
	 * @param sortId
	 * @param subject
	 * @throws Exception
	 */
	public void copyExistFile(Connection dbConn, StringBuffer buffer, int sortParentId, String sortName) throws Exception {
		try {
			String newFileName = sortName + " - 复件";
			boolean isHave = this.isExistFile(dbConn, sortParentId, newFileName);
			if (!isHave) {
				buffer.append(newFileName);
			} else {
				copyExistFile(dbConn, buffer, sortParentId, newFileName);
			}
		} catch (Exception e) {
			throw e;
		}
	}

  public boolean getRoleIdOtherStr(String userPrivOther, String ids) {
    // TODO Auto-generated method stub
    String ss = YHWorkFlowUtility.checkId(ids, userPrivOther, true);
    if ("".equals(ss)) {
      return false;
    } else {
      return true;
    }
  }
	
  public boolean getDeptOtherStr(String deptOther, String ids) {
    // TODO Auto-generated method stub
    String ss = YHWorkFlowUtility.checkId(ids, deptOther, true);
    if ("".equals(ss)) {
      return false;
    } else {
      return true;
    }
  }


}
