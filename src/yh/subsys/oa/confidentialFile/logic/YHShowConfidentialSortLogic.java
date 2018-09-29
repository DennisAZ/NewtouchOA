package yh.subsys.oa.confidentialFile.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;
import yh.subsys.oa.confidentialFile.act.YHConfidentialContentAct;
import yh.subsys.oa.confidentialFile.data.YHConfidentialContent;
import yh.subsys.oa.confidentialFile.data.YHConfidentialSort;

public class YHShowConfidentialSortLogic {
	private static Logger log = Logger.getLogger(YHShowConfidentialSortLogic.class);

	public String getPrivTreeLogic(Connection dbConn, YHPerson person, String idStr, String sortIdStr) throws Exception {
		int id = 0;
		if (idStr != null && !"".equals(idStr)) {
			id = Integer.parseInt(idStr);
		}
		if (sortIdStr == null || "".equals(sortIdStr.trim())) {
			sortIdStr = "0";
		}
		// 获取登录用户信息
		int loginUserSeqId = person.getSeqId();
		int loginUserDeptId = person.getDeptId();
		String loginUserRoleId = person.getUserPriv();
		YHSetConfidentialSortLogic fileSortLogic = new YHSetConfidentialSortLogic();
		StringBuffer sb = new StringBuffer();
		try {
			if (sortIdStr != null && !"0".equals(sortIdStr.trim()) && id == 0) {

				YHConfidentialSort fileSort = fileSortLogic.getfileSortById(dbConn, Integer.parseInt(sortIdStr));
				if (fileSort != null) {
					sb.append("[");
					int seqId = fileSort.getSeqId();
					String sortName = YHUtility.null2Empty(fileSort.getSortName());

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
				String[] condition = { " SORT_PARENT=" + id + " order by SORT_NO,SORT_NAME" };
				List<YHConfidentialSort> list = fileSortLogic.getFileFilderInfo(dbConn, condition);

				boolean userFlag = false;
				boolean roleFlag = false;
				boolean deptFlag = false;

				boolean ownerUserFlag = false;
				boolean ownerRoleFlag = false;
				boolean ownerDeptFlag = false;

				if (list.size() > 0) {
					sb.append("[");
					boolean isHave = false;
					for (YHConfidentialSort fileSort : list) {
						int seqId = fileSort.getSeqId();
						YHConfidentialSort fileSort2 = fileSortLogic.getfileSortById(dbConn, seqId);

						String userPrivs = fileSortLogic.selectManagerIds(dbConn, fileSort2, "USER_ID");
						String rolePrivs = fileSortLogic.getRoleIds(dbConn, fileSort2, "USER_ID");
						String deptPrivs = fileSortLogic.getDeptIds(dbConn, fileSort2, "USER_ID");

						String ownerUserPrivs = fileSortLogic.selectManagerIds(dbConn, fileSort2, "OWNER");
						String ownerRolePrivs = fileSortLogic.getRoleIds(dbConn, fileSort2, "OWNER");
						String ownerDeptPrivs = fileSortLogic.getDeptIds(dbConn, fileSort2, "OWNER");

						userFlag = this.checkUserIdPriv(loginUserSeqId, userPrivs);
						roleFlag = this.checkUserIdPriv(Integer.parseInt(loginUserRoleId), rolePrivs);
						deptFlag = this.chekDeptIdPriv(loginUserDeptId, deptPrivs);

						ownerUserFlag = this.checkUserIdPriv(loginUserSeqId, ownerUserPrivs);
						ownerRoleFlag = this.checkUserIdPriv(Integer.parseInt(loginUserRoleId), ownerRolePrivs);
						ownerDeptFlag = this.chekDeptIdPriv(loginUserDeptId, ownerDeptPrivs);

						if (ownerUserFlag == true || ownerRoleFlag == true || ownerDeptFlag == true) {
							String sortName = YHUtility.null2Empty(fileSort.getSortName());
							sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
							sortName = sortName.replaceAll("[\n-\r]", "<br>");
							sortName = sortName.replace("\"", "\\\"");

							String extData = "";
							sb.append("{");
							sb.append("nodeId:\"" + seqId + "\"");
							sb.append(",name:\"" + sortName + "\"");
							sb.append(",isHaveChild:" + 1 + "");
							sb.append(",extData:\"" + extData + "\"");
							sb.append("},");
							isHave = true;
						} else if (userFlag == true || roleFlag == true || deptFlag == true) {
							String sortName = YHUtility.null2Empty(fileSort.getSortName());
							sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
							sortName = sortName.replaceAll("[\n-\r]", "<br>");
							sortName = sortName.replace("\"", "\\\"");

							String extData = "";
							sb.append("{");
							sb.append("nodeId:\"" + seqId + "\"");
							sb.append(",name:\"" + sortName + "\"");
							sb.append(",isHaveChild:" + 1 + "");
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

		} catch (Exception e) {
			throw e;
		}
		return sb.toString();
	}

	/**
	 * 获取访问权限：根据ids串返回与登录的seqId比较判断是否相等，返回boolean类型。
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public boolean checkUserIdPriv(int userSeqId, String ids) throws Exception, Exception {
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
	 * 根据登录用户的部门Id与权限中的部门Id对比返回boolean
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public boolean chekDeptIdPriv(int loginUserDeptId, String ids) throws Exception, Exception {
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
	 * 获取目录路径
	 * 
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public String getFolderPathByIdLogic(Connection dbConn, int seqId) throws Exception {
		YHORM orm = new YHORM();
		StringBuffer buffer = new StringBuffer();
		try {
			YHConfidentialSort fileSort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, seqId);
			int sortParent = 0;
			if (fileSort != null) {
				sortParent = fileSort.getSortParent();
			}
			this.getSortNamePath(dbConn, seqId, buffer);
			String sortName = buffer.toString();
			String sortNames[] = sortName.split(",");
			StringBuffer sb = new StringBuffer();
			for (int i = sortNames.length - 1; i >= 0; i--) {
				sb.append(sortNames[i]);
			}
			sb.deleteCharAt(sb.length() - 1);
			String data = "{folderPath:\"" + sb.toString() + "\",sortParentId:" + sortParent + "}";
			return data;
		} catch (Exception e) {
			throw e;
		}
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
		YHORM orm = new YHORM();
		try {
			YHConfidentialSort fileSort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, seqId);
			int sortParent = 0;
			String sortName = "";
			if (fileSort != null) {
				sortParent = fileSort.getSortParent();
				sortName = YHUtility.null2Empty(fileSort.getSortName());
			}
			// 处理特殊字符
			sortName = sortName.replaceAll("[\\\\/:*?\"<>|]", "");
			sortName = sortName.replaceAll("[\n-\r]", "<br>");
			sortName = sortName.replace("\"", "\\\"");

			buffer.append(YHUtility.encodeSpecial(sortName) + "/,");
			boolean flag = this.isHaveSortParent(dbConn, sortParent);
			if (flag) {
				getSortNamePath(dbConn, sortParent, buffer);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 判断是否还有子级文件夹
	 * 
	 * @param dbConn
	 * @param sortParent
	 * @return
	 * @throws Exception
	 */
	public boolean isHaveSortParent(Connection dbConn, int sortParent) throws Exception {
		boolean flag = false;
		YHORM orm = new YHORM();
		try {
			Map map = new HashMap();
			map.put("SEQ_ID", sortParent);
			List<YHConfidentialSort> list = orm.loadListSingle(dbConn, YHConfidentialSort.class, map);
			if (list.size() > 0) {
				flag = true;
			}
			return flag;
		} catch (Exception e) {
			throw e;
		}
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
		try {
			Map map = new HashMap();
			map.put("SORT_PARENT", id);
			List<YHConfidentialSort> list = orm.loadListSingle(dbConn, YHConfidentialSort.class, map);
			if (list.size() > 0) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 新建子文件夹
	 * 
	 * @param dbConn
	 * @param fileSort
	 * @param sortParentIdStr
	 * @param folderName
	 * @return
	 * @throws Exception
	 */
	public String addSubFolderLogic(Connection dbConn, YHConfidentialSort fileSort, String sortParentIdStr, String folderName) throws Exception {
		YHORM orm = new YHORM();
		int isHaveFlag = 0;
		int nodeId = 0;
		String sortName = "";
		int sortParentId = 0;
		if (!YHUtility.isNullorEmpty(sortParentIdStr)) {
			sortParentId = Integer.parseInt(sortParentIdStr);
		}
		try {
			// YHSetConfidentialSortLogic setLogic = new YHSetConfidentialSortLogic();
			// int counter = setLogic.checkSortNameLogic(dbConn, folderName,
			// sortParentIdStr);
			boolean isHave = this.checkAddFolderName(dbConn, sortParentId, folderName);
			if (isHave) {
				isHaveFlag = 1;
			} else {
				YHConfidentialSort sort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, sortParentId);
				fileSort.setSortParent(sortParentId);
				fileSort.setNewUser(sort.getNewUser());
				fileSort.setUserId(sort.getUserId());
				fileSort.setManageUser(sort.getManageUser());
				fileSort.setDownUser(sort.getDownUser());
				fileSort.setShareUser(sort.getShareUser());
				fileSort.setOwner(sort.getOwner());
				this.saveFileSortObj(dbConn, fileSort);
				YHConfidentialSort fileSort2 = this.getSortMaxSeqId(dbConn);
				if (fileSort2 != null) {
					nodeId = fileSort2.getSeqId();
					sortName = YHUtility.null2Empty(fileSort2.getSortName());
				}
			}
			String date = "{nodeId:\"" + nodeId + "\",sortName:\"" + YHUtility.encodeSpecial(sortName) + "\",isHaveFlag:\"" + isHaveFlag + "\" }";
			return date;
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 获取最大的SeqId值
	 * 
	 * @param dbConn
	 * @return
	 */
	public YHConfidentialSort getSortMaxSeqId(Connection dbConn) {
		String sql = "select SEQ_ID,SORT_NAME from oa_secret_sort where SEQ_ID=(select MAX(SEQ_ID) from oa_secret_sort )";
		int seqId = 0;
		String sortName = "";
		YHConfidentialSort fileSort = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				fileSort = new YHConfidentialSort();
				seqId = rs.getInt("SEQ_ID");
				sortName = YHUtility.null2Empty(rs.getString("SORT_NAME"));
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

	/**
	 * 保存对象
	 * 
	 * @param dbConn
	 * @param fileSort
	 */
	public void saveFileSortObj(Connection dbConn, YHConfidentialSort fileSort) {
		YHORM orm = new YHORM();
		try {
			orm.saveSingle(dbConn, fileSort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 验证文件夹名是否存在（新建）
	 * 
	 * @param dbConn
	 * @param seqId
	 * @param loginUserSeqId
	 * @param folderName
	 * @return
	 * @throws Exception
	 */
	public boolean checkAddFolderName(Connection dbConn, int seqId, String folderName) throws Exception {
		YHORM orm = new YHORM();
		boolean isHave = false;
		try {
			String[] filters = { "SORT_PARENT=" + seqId };
			List<YHConfidentialSort> parentList = orm.loadListSingle(dbConn, YHConfidentialSort.class, filters);
			if (parentList != null && parentList.size() > 0) {
				for (YHConfidentialSort fileFolder : parentList) {
					String sortNameString = YHUtility.null2Empty(fileFolder.getSortName());
					if (folderName.trim().equals(sortNameString.trim())) {
						isHave = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return isHave;
	}
	/**
	 *  通过id获取该文件夹的“所有者权限”
	 * @param dbConn
	 * @param person
	 * @param seqIdStr
	 * @return
	 * @throws Exception
	 */
	public String getOwnerPrivLogic(Connection dbConn, YHPerson person, String seqIdStr) throws Exception {
		YHSetConfidentialSortLogic setLogic = new YHSetConfidentialSortLogic();
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		YHORM orm = new YHORM();
		int ownerPrivFlag = 0;
		int sortSeqId = 0;
		try {
			YHConfidentialSort fileSort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, seqId);
			if (fileSort != null) {
				sortSeqId = fileSort.getSeqId();
				String userPrivs = setLogic.selectManagerIds(dbConn, fileSort, "OWNER");
				String rolePrivs = setLogic.getRoleIds(dbConn, fileSort, "OWNER");
				String deptPrivs = setLogic.getDeptIds(dbConn, fileSort, "OWNER");

				boolean userFlag = this.checkUserIdPriv(person.getSeqId(), userPrivs);
				boolean roleFlag = this.checkUserIdPriv(Integer.parseInt(person.getUserPriv()), rolePrivs);
				boolean deptFlag = this.chekDeptIdPriv(person.getDeptId(), deptPrivs);
				if (userFlag || deptFlag || roleFlag) {
					ownerPrivFlag = 1;
				}
			}
			String data = "{ownerPriv:\"" + ownerPrivFlag + "\",seqId:" + sortSeqId + "}";
			return data;
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 通过id获取该文件夹的“所有者权限”与“访问权限”
	 * @param dbConn
	 * @param person
	 * @param seqIdStr
	 * @return
	 * @throws Exception
	 */
	public String getVisitOrOwnerPrivLogic(Connection dbConn, YHPerson person, String seqIdStr) throws Exception {
		YHSetConfidentialSortLogic setLogic = new YHSetConfidentialSortLogic();
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		YHORM orm = new YHORM();
		int ownerPrivFlag = 0;
		int visitPrivFlag = 0;
		int privacy = 0;
		try {
			YHConfidentialSort fileSort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, seqId);
			if (fileSort != null) {
				String visitUserPrivs = setLogic.selectManagerIds(dbConn, fileSort, "USER_ID");
				String visitRolePrivs = setLogic.getRoleIds(dbConn, fileSort, "USER_ID");
				String visitDeptPrivs = setLogic.getDeptIds(dbConn, fileSort, "USER_ID");
				
				String ownerUserPrivs = setLogic.selectManagerIds(dbConn, fileSort, "OWNER");
				String ownerRolePrivs = setLogic.getRoleIds(dbConn, fileSort, "OWNER");
				String ownerDeptPrivs = setLogic.getDeptIds(dbConn, fileSort, "OWNER");
				
				boolean ownerUserFlag = this.checkUserIdPriv(person.getSeqId(), ownerUserPrivs);
				boolean ownerRoleFlag = this.checkUserIdPriv(Integer.parseInt(person.getUserPriv()), ownerRolePrivs);
				boolean ownerDeptFlag = this.chekDeptIdPriv(person.getDeptId(), ownerDeptPrivs);
				
				boolean visitUserFlag = this.checkUserIdPriv(person.getSeqId(), visitUserPrivs);
				boolean visitRoleFlag = this.checkUserIdPriv(Integer.parseInt(person.getUserPriv()), visitRolePrivs);
				boolean visitDeptFlag = this.chekDeptIdPriv(person.getDeptId(), visitDeptPrivs);
				if (ownerUserFlag || ownerRoleFlag || ownerDeptFlag) {
					ownerPrivFlag = 1;
				}
				if (visitUserFlag || visitRoleFlag || visitDeptFlag) {
					visitPrivFlag = 1;
				}
				if (visitPrivFlag == 1 || ownerPrivFlag == 1) {
					privacy = 1;
				}
			}
			String data = "{privacy:\"" + privacy + "\"}";
			return data;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 通过id获取该文件夹的所有权限信息
	 * @param dbConn
	 * @param person
	 * @param seqId
	 * @throws Exception
	 */
	public String getAllPrivteByIdLogic(Connection dbConn,YHPerson person,int seqId) throws Exception{
		StringBuffer sb = new StringBuffer();
		int visiPrivFlag = 0;
		int managePrivFlag = 0;
		int newPrivFlag = 0;
		int downPrivFlag = 0;
		int ownerPrivFlag = 0;
		int sortSeqId = 0;
		int sortParent = 0;
		try {
			YHORM orm = new YHORM();
			YHConfidentialSort fileSort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, seqId);
			YHSetConfidentialSortLogic setLogic = new YHSetConfidentialSortLogic();
			if (fileSort !=null) {
				sortSeqId = fileSort.getSeqId();
				sortParent = fileSort.getSortParent();
				String userIdPrivs = setLogic.selectManagerIds(dbConn, fileSort, "USER_ID");
				String userRolePrivs = setLogic.getRoleIds(dbConn, fileSort, "USER_ID");
				String userDeptPrivs = setLogic.getDeptIds(dbConn, fileSort, "USER_ID");
				boolean userIdFlag = this.checkUserIdPriv(person.getSeqId(), userIdPrivs);
				boolean userRoleFlag = this.checkUserIdPriv(Integer.parseInt(person.getUserPriv()), userRolePrivs);
				boolean userDeptFlag = this.chekDeptIdPriv(person.getDeptId(), userDeptPrivs);
				if (userIdFlag || userRoleFlag || userDeptFlag) {
					visiPrivFlag = 1;
				}
				
				String newUserIdPrivs = setLogic.selectManagerIds(dbConn, fileSort, "NEW_USER");
				String newUserRolePrivs = setLogic.getRoleIds(dbConn, fileSort, "NEW_USER");
				String newUserDeptPrivs = setLogic.getDeptIds(dbConn, fileSort, "NEW_USER");
				boolean newUserIdFlag = this.checkUserIdPriv(person.getSeqId(), newUserIdPrivs);
				boolean newUserRoleFlag = this.checkUserIdPriv(Integer.parseInt(person.getUserPriv()), newUserRolePrivs);
				boolean newUserDeptFlag = this.chekDeptIdPriv(person.getDeptId(), newUserDeptPrivs);
				if (newUserIdFlag || newUserRoleFlag || newUserDeptFlag) {
					newPrivFlag = 1;
				}
				
				String manageUserIdPrivs = setLogic.selectManagerIds(dbConn, fileSort, "MANAGE_USER");
				String manageUserRolePrivs = setLogic.getRoleIds(dbConn, fileSort, "MANAGE_USER");
				String manageUserDeptPrivs = setLogic.getDeptIds(dbConn, fileSort, "MANAGE_USER");
				boolean manageUserIdFlag = this.checkUserIdPriv(person.getSeqId(), manageUserIdPrivs);
				boolean manageUserRoleFlag = this.checkUserIdPriv(Integer.parseInt(person.getUserPriv()), manageUserRolePrivs);
				boolean manageUserDeptFlag = this.chekDeptIdPriv(person.getDeptId(), manageUserDeptPrivs);
				if (manageUserIdFlag || manageUserRoleFlag || manageUserDeptFlag) {
					managePrivFlag = 1;
				}
				
				String downUserIdPrivs = setLogic.selectManagerIds(dbConn, fileSort, "DOWN_USER");
				String downUserRolePrivs = setLogic.getRoleIds(dbConn, fileSort, "DOWN_USER");
				String downUserDeptPrivs = setLogic.getDeptIds(dbConn, fileSort, "DOWN_USER");
				boolean downUserIdFlag = this.checkUserIdPriv(person.getSeqId(), downUserIdPrivs);
				boolean downUserRoleFlag = this.checkUserIdPriv(Integer.parseInt(person.getUserPriv()), downUserRolePrivs);
				boolean downUserDeptFlag = this.chekDeptIdPriv(person.getDeptId(), downUserDeptPrivs);
				if (downUserIdFlag || downUserRoleFlag || downUserDeptFlag) {
					downPrivFlag = 1;
				}
				String ownerUserIdPrivs = setLogic.selectManagerIds(dbConn, fileSort, "OWNER");
				String ownerUserRolePrivs = setLogic.getRoleIds(dbConn, fileSort, "OWNER");
				String ownerUserDeptPrivs = setLogic.getDeptIds(dbConn, fileSort, "OWNER");
				boolean ownerUserIdFlag = this.checkUserIdPriv(person.getSeqId(), ownerUserIdPrivs);
				boolean ownerUserRoleFlag = this.checkUserIdPriv(Integer.parseInt(person.getUserPriv()), ownerUserRolePrivs);
				boolean ownerUserDeptFlag = this.chekDeptIdPriv(person.getDeptId(), ownerUserDeptPrivs);
				if (ownerUserIdFlag || ownerUserRoleFlag || ownerUserDeptFlag) {
					ownerPrivFlag = 1;
				}
			}
			sb.append("{");
			sb.append("visiPriv:\"" + visiPrivFlag + "\"");
			sb.append(",managePriv:\"" + managePrivFlag + "\"");
			sb.append(",newPriv:\"" + newPrivFlag + "\"");
			sb.append(",downPriv:\"" + downPrivFlag + "\"");
			sb.append(",ownerPriv:\"" + ownerPrivFlag + "\"");
			sb.append(",sortParent:\"" + sortParent + "\"");
			sb.append(",seqId:\"" + sortSeqId + "\"");
			sb.append("}");
			return sb.toString();
			
		} catch (Exception e) {
			throw e;
		}
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
	 * 验证文件夹名是否存在（编辑）
	 * 
	 * @param dbConn
	 * @param seqId
	 * @param folderName
	 * @return
	 * @throws Exception
	 */
	public boolean checkEditFolderName(Connection dbConn, int seqId, String folderName) throws Exception {
		YHORM orm = new YHORM();
		boolean isHave = false;
		try {
			YHConfidentialSort fileSort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, seqId);
			int sortParentId = 0;
			if (fileSort != null) {
				sortParentId = fileSort.getSortParent();
			}
			String[] filters = { "SORT_PARENT=" + sortParentId };
			List<YHConfidentialSort> parentList = orm.loadListSingle(dbConn, YHConfidentialSort.class, filters);
			if (parentList != null && parentList.size() > 0) {
				for (YHConfidentialSort fileFolder : parentList) {
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

		} catch (Exception e) {
			throw e;
		}
		return isHave;
	}

	/**
	 * 编辑子文件夹
	 * 
	 * @param dbConn
	 * @param fileSort
	 * @param sortParentIdStr
	 * @param folderName
	 * @return
	 * @throws Exception
	 */
	public String updateSubFolderLogic(Connection dbConn, YHConfidentialSort fileSort, String sortParentIdStr, String folderName) throws Exception {
		YHORM orm = new YHORM();
		int isHaveFlag = 0;
		int nodeId = 0;
		String sortName = "";
		int sortParentId = 0;
		if (!YHUtility.isNullorEmpty(sortParentIdStr)) {
			sortParentId = Integer.parseInt(sortParentIdStr);
		}
		try {
			boolean isHave = this.checkEditFolderName(dbConn, sortParentId, folderName);
			if (isHave) {
				isHaveFlag = 1;
			} else {
				YHConfidentialSort sort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, sortParentId);
				if (sort != null) {
					sort.setSortNo(fileSort.getSortNo());
					sort.setSortName(fileSort.getSortName());
					orm.updateSingle(dbConn, sort);
					nodeId = sort.getSeqId();
					sortName = YHUtility.null2Empty(sort.getSortName());
				}
			}
			String date = "{nodeId:\"" + nodeId + "\",sortName:\"" + YHUtility.encodeSpecial(sortName) + "\",isHaveFlag:\"" + isHaveFlag + "\" }";
			return date;
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 剪贴(更新上级节点信息)
	 * 
	 * @param dbConn
	 * @param parentId
	 * @param seqId
	 * @throws Exception
	 */
	public void updateFolderInfoById(Connection dbConn, int parentId, int seqId) throws Exception {
		PreparedStatement ps = null;
		try {
			String sql = "UPDATE oa_secret_sort SET SORT_PARENT=? where SEQ_ID=? ";
			ps = dbConn.prepareStatement(sql);
			ps.setInt(1, parentId);
			ps.setInt(2, seqId);
			ps.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, null, log);
		}
	}

	/**
	 * 得到本级以及其所有子文件夹的对象以及每个文件夹属于第几级
	 * 
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public List getAllFolderList(Connection dbConn, int seqId, int parentId, List listTemp, int maxSeqId) throws Exception {
		String seqIdString = "";
		YHORM orm = new YHORM();
		Map map = new HashMap();
		map.put("SORT_PARENT", seqId);
		List<YHConfidentialSort> list = orm.loadListSingle(dbConn, YHConfidentialSort.class, map);
		if (seqId > maxSeqId) {
			return list;
		}

		int newParent = this.getChildFolder(dbConn, seqId, parentId, listTemp); // 返回新文件夹的seqId，
		Map contentMap = new HashMap();
		contentMap.put("SORT_ID", seqId);
//		String[] filters = {" SORT_ID=" + seqId};
		List<YHConfidentialContent> fileContents = this.getFileContentsInfo(dbConn, contentMap);
//		List<YHConfidentialContent> fileContents = orm.loadListSingle(dbConn, YHConfidentialContent.class, filters);
		if (fileContents != null && fileContents.size() > 0) {
			for (int i = 0; i < fileContents.size(); i++) {
				YHConfidentialContent content = fileContents.get(i);
				seqIdString += content.getSeqId() + ",";
			}
			copyAllFile(dbConn, newParent, seqIdString.substring(0, seqIdString.length() - 1));
		}

		for (int i = 0; i < list.size(); i++) {
			YHConfidentialSort dimension = list.get(i);
			getAllFolderList(dbConn, dimension.getSeqId(), newParent, listTemp, maxSeqId);
		}
		return listTemp;
	}
	
	public List<YHConfidentialContent> getFileContentsInfo(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		try {
			return orm.loadListSingle(dbConn, YHConfidentialContent.class, map);
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
	public int getChildFolder(Connection dbConn, int seqId, int parentId, List listTemp) throws Exception {
		YHORM orm = new YHORM();
		int maxSeqId = 0;
		try {
			YHConfidentialSort fileSort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, parentId); // 获取点击粘贴时的文件夹对象信息
			YHConfidentialSort fileSort2 = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, seqId); // 获取点击复制时的文件夹对象信息
			String sortName = "";
			if (fileSort2 != null) {
				sortName = YHUtility.null2Empty(fileSort2.getSortName());
			}
			if (fileSort != null) {
				fileSort.setSortParent(parentId);
				fileSort.setSortName(sortName);
				orm.saveSingle(dbConn, fileSort);
				YHConfidentialSort maxfiSort = getSortMaxSeqId(dbConn);
				listTemp.add(maxfiSort.getSeqId());
				maxSeqId = maxfiSort.getSeqId();
			}
		} catch (Exception e) {
			throw e;
		}
		return maxSeqId;
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
		String filePath = YHSysProps.getAttachPath() + separator + YHConfidentialContentAct.attachmentFolder + separator;

		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(date);

		YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();
		String rand = emut.getRandom();

		YHORM orm = new YHORM();
		YHConfidentialContentLogic contentLogic = new YHConfidentialContentLogic();
		String[] seqIdStr = seqIdStrs.split(",");
		if (!"".equals(seqIdStrs) && seqIdStrs.split(",").length > 0) {
			for (String seqId : seqIdStr) {
				String newAttaId = "";
				String newAttaName = "";
				boolean isHave = false;

				YHConfidentialContent fileContent = contentLogic.getFileContentInfoById(dbConn, Integer.parseInt(seqId));
				if (fileContent != null) {
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
				}
				if (isHave) {
					YHConfidentialContent content = new YHConfidentialContent();
					content.setAttachmentId(newAttaId.trim());
					content.setAttachmentName(newAttaName.trim());
					content.setSendTime(YHUtility.parseTimeStamp());
					content.setSortId(sortId);
					content.setSubject(fileContent.getSubject());
					orm.saveSingle(dbConn, content);
				} else {
					fileContent.setSortId(sortId);
					fileContent.setSendTime(YHUtility.parseTimeStamp());
					orm.saveSingle(dbConn, fileContent);
				}
			}
		}
	}
	
	/**
	 * 取得文件夹名
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public String getFolderNameLogic(Connection dbConn,int seqId) throws Exception{
		String sortName = "";
		try {
			YHConfidentialSort fileSort = this.getfileSortById(dbConn, seqId);
			if (fileSort != null) {
				sortName = YHUtility.encodeSpecial(YHUtility.null2Empty(fileSort.getSortName()));
			}
			String data = "{folderName:\"" + sortName + "\"}";
			return data;
		} catch (Exception e) {
			throw e;
		}
	}
	
}
