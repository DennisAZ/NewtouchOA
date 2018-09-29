package yh.core.funcs.filefolder.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHSignReaderLogic {
	private int readCount = 0;
	private int unReadCount = 0;
	private int isHavePriv = 0;
	private Map userIdMap = null;
	public String getSignReader(int sortId, int contentId,YHPerson person, Connection conn) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		Map map = this.getUserId(sortId, conn);
		userIdMap = map;
		String toId = (String) map.get("toId");
		String privId = (String) map.get("privId");
		String userIdTo = (String) map.get("userIdTo");
		boolean userFlag = this.checkUserIdPriv(person.getSeqId(),userIdTo);
		boolean roleFlag = this.checkUserIdPriv(Integer.parseInt(person.getUserPriv()), privId);
		boolean deptFlag = this.chekDeptIdPriv(person.getDeptId(), toId);
		if (userFlag || roleFlag || deptFlag) {
			isHavePriv = 1;
		}

		// 不是全体部门
		if (!"0".equals(toId)) {
			String[] aStr = privId.split(",");
			for (String tmp : aStr) {
				if (YHUtility.isInteger(tmp)) {
					String sql = "SELECT DEPT_ID from PERSON where USER_PRIV=" + tmp + " and NOT_LOGIN='0'";
					toId = this.getToId(sql, conn, toId);
				}
			}
		}

		// 不是全体部门
		if (!"0".equals(toId)) {
			String[] aStr = userIdTo.split(",");
			for (String tmp : aStr) {
				if (YHUtility.isInteger(tmp)) {
					String sql = "SELECT DEPT_ID from PERSON where SEQ_ID=" + tmp;
					toId = this.getToId(sql, conn, toId);
				}
			}
		}

		Map fileContent = this.getFileContent(contentId, conn);
		String subject = (String) fileContent.get("subject");
		String readers = (String) fileContent.get("readers");
		sb.append("subject:'" + YHUtility.encodeSpecial(subject) + "'");
		sb.append(",detpList:" + this.getDeptTreeJson(0, conn, toId, readers));
		sb.append(",readCount:" + readCount);
		sb.append(",unReadCount:" + unReadCount);
		sb.append(",isHavePriv:" + isHavePriv);
		sb.append("}");
		return sb.toString();
	}

	public Map getFileContent(int contentId, Connection conn) throws Exception {
		Map map = new HashMap();
		String sql = "SELECT SUBJECT,READERS from oa_file_content where SEQ_ID=" + contentId;
		Statement stm1 = null;
		ResultSet rs1 = null;
		String subject = "";
		String readers = "";
		try {
			stm1 = conn.createStatement();
			rs1 = stm1.executeQuery(sql);
			if (rs1.next()) {
				subject = rs1.getString("SUBJECT");
				readers = rs1.getString("READERS");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stm1, rs1, null);
		}
		// 需转换一下
		map.put("subject", subject);
		map.put("readers", readers);
		return map;
	}

	public String getToId(String sql, Connection conn, String toId) throws Exception {
		Statement stm1 = null;
		ResultSet rs1 = null;
		if (toId != null && !toId.endsWith(",")) {
			toId += ",";
		}
		try {
			stm1 = conn.createStatement();
			rs1 = stm1.executeQuery(sql);
			while (rs1.next()) {
				int deptId = rs1.getInt("DEPT_ID");
				if (!findId(toId, String.valueOf(deptId))) {
					toId += deptId + ",";
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stm1, rs1, null);
		}
		return toId;
	}

	/**
	 * 判段id是不是在str里面
	 * 
	 * @param str
	 * @param id
	 * @return
	 */
	public static boolean findId(String str, String id) {
		if (str == null || id == null || "".equals(str) || "".equals(id)) {
			return false;
		}
		String[] aStr = str.split(",");
		for (String tmp : aStr) {
			if (tmp.equals(id)) {
				return true;
			}
		}
		return false;
	}

	public Map getUserId(int sortId, Connection conn) throws Exception {
		Map map = new HashMap();
		String query = "SELECT USER_ID from oa_file_sort where SEQ_ID =" + sortId;

		String userId = "";
		Statement stm1 = null;
		ResultSet rs1 = null;
		try {
			stm1 = conn.createStatement();
			rs1 = stm1.executeQuery(query);
			if (rs1.next()) {
				userId = rs1.getString("USER_ID");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stm1, rs1, null);
		}
		String[] aUserId = userId.split("\\|");

		String toId = "";
		String privId = "";
		String userIdTo = "";
		if (aUserId.length > 0) {
			toId = aUserId[0];
		}
		if (aUserId.length > 1) {
			privId = aUserId[1];
		}
		if (aUserId.length > 2) {
			userIdTo = aUserId[2];
		}
		map.put("toId", toId);
		map.put("privId", privId);
		map.put("userIdTo", userIdTo);
		return map;
	}

	public String getDeptTreeJson(int deptId, Connection conn, String toId, String readers) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		this.getDeptTree(deptId, sb, 0, conn, toId, readers);
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]");
		return sb.toString();
	}

	public void getDeptTree(int deptId, StringBuffer sb, int level, Connection conn, String toId, String readers) throws Exception {
		// 首选分级，然后记录级数，是否为最后一个。。。
		List<YHDepartment> list = this.getDeptByParentId(deptId, conn, toId);
		for (int i = 0; i < list.size(); i++) {
			String flag = "├";
			String tmp = "";
			for (int j = 0; j < level; j++) {
				tmp += "　";
			}
			flag = tmp + flag;

			YHDepartment dp = list.get(i);
			sb.append("{");
			sb.append("deptName:'" + flag + dp.getDeptName() + "',");
			this.getUserById(conn, dp.getSeqId(), sb, toId, readers);
			sb.append("},");
			this.getDeptTree(dp.getSeqId(), sb, level + 1, conn, toId, readers);
		}

	}

	public void getUserById(Connection conn, int deptId, StringBuffer sb, String toId, String readers) throws Exception {
		String unReaderName = "";
		String readerName = "";
		String sDeptId = String.valueOf(deptId);
		String toIdReal = (String) userIdMap.get("toId");
		String privId = (String) userIdMap.get("privId");
		String userIdTo = (String) userIdMap.get("userIdTo");

		if ("0".equals(toId) || "ALL_DEPT".equals(toId) || this.findId(toId, String.valueOf(deptId))) {
			String query = "select SEQ_ID " + ",USER_PRIV " + ",USER_NAME  " + "from PERSON where  " + "DEPT_ID= " + deptId + " and NOT_LOGIN='0'  "
					+ "order by USER_NO,USER_NAME";

			Statement stm1 = null;
			ResultSet rs1 = null;
			try {
				stm1 = conn.createStatement();
				rs1 = stm1.executeQuery(query);
				while (rs1.next()) {
					int userId = rs1.getInt("SEQ_ID");

					String userPriv = rs1.getString("USER_PRIV");
					String userName = rs1.getString("USER_NAME");
					if (this.findId(readers, String.valueOf(userId))) {
						readerName += userName + ",";
						readCount++;
					} else {
						if ("0".equals(toId) || "ALL_DEPT".equals(toId) || this.findId(toIdReal, sDeptId) || this.childToId(conn, toIdReal, sDeptId)
								|| this.findId(privId, userPriv) || this.findId(userIdTo, String.valueOf(userId))) {
							unReaderName += userName + ",";
							unReadCount++;
						}
					}
				}
			} catch (Exception ex) {
				throw ex;
			} finally {
				YHDBUtility.close(stm1, rs1, null);
			}
		}
		if (readerName.endsWith(",")) {
			readerName = readerName.substring(0, readerName.length() - 1);
		}
		if (unReaderName.endsWith(",")) {
			unReaderName = unReaderName.substring(0, unReaderName.length() - 1);
		}
		sb.append("hasReader:'" + readerName + "',");
		sb.append("noReader:'" + unReaderName + "'");
	}

	public List<YHDepartment> getDeptByParentId(int deptId, Connection conn, String toId) throws Exception {
		// TODO Auto-generated method stub
		YHORM orm = new YHORM();
		List<YHDepartment> list = new ArrayList();
		List<YHDepartment> listNew = new ArrayList();
		list = this.getDeptByParentId(conn, deptId);
		if (!"0".equals(toId) && !"ALL_DEPT".equals(toId)) {// 判断是不是全体部门
			for (int i = 0; i < list.size(); i++) {
				boolean temp = false;
				YHDepartment dept = list.get(i);
				if (toId != null && !"".equals(toId)) {
					String[] toIds = toId.split(",");
					for (int j = 0; j < toIds.length; j++) {
						String toIdTemp = toIds[j];
						if (toIdTemp.equals(Integer.toString(dept.getSeqId())) || childToId(conn, toId, Integer.toString(dept.getSeqId()))) {
							temp = true;
							break;
						}
					}
				}
				if (temp == true) {
					listNew.add(dept);
				}
			}
		} else {
			listNew = list;
		}
		return listNew;

	}

	public List<YHDepartment> getDeptByParentId(Connection conn, int deptId) throws Exception {
		List<YHDepartment> list = new ArrayList();
		String query = "select SEQ_ID , DEPT_NAME from oa_department where dept_parent=" + deptId;
		Statement stm1 = null;
		ResultSet rs1 = null;
		try {
			stm1 = conn.createStatement();
			rs1 = stm1.executeQuery(query);
			while (rs1.next()) {
				YHDepartment dept = new YHDepartment();
				int seqId = rs1.getInt("SEQ_ID");
				String deptName = rs1.getString("DEPT_NAME");
				dept.setSeqId(seqId);
				dept.setDeptName(deptName);
				list.add(dept);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stm1, rs1, null);
		}
		return list;
	}

	public boolean childToId(Connection conn, String toId, String deptId) throws Exception {
		YHORM orm = new YHORM();
		List<YHDepartment> list = new ArrayList();
		Map filters = new HashMap();
		filters.put("DEPT_PARENT", deptId);
		list = orm.loadListSingle(conn, YHDepartment.class, filters);
		boolean result = false;
		for (int i = 0; i < list.size(); i++) {
			boolean temp = false;
			YHDepartment dept = list.get(i);
			if (toId != null && !"".equals(toId)) {
				String[] toIds = toId.split(",");
				for (int j = 0; j < toIds.length; j++) {
					String toIdTemp = toIds[j];
					if (toIdTemp.equals(Integer.toString(dept.getSeqId()))) {
						temp = true;
						break;
					}
				}
			}
			boolean temp2 = false;
			temp2 = childToId(conn, toId, Integer.toString(dept.getSeqId()));
			if (temp == true || temp2 == true) {
				result = true;
			}
		}
		return result;
	}

	public void delSignReader(Connection conn, int contentId, YHPerson user) throws Exception {
		boolean accessPriv = false;
		boolean managePriv = false;

		String query = "select SORT_ID from oa_file_content where SEQ_ID=" + contentId;
		Statement stm1 = null;
		ResultSet rs1 = null;
		int sortId = 0;
		try {
			stm1 = conn.createStatement();
			rs1 = stm1.executeQuery(query);
			if (rs1.next()) {
				sortId = rs1.getInt("SORT_ID");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stm1, rs1, null);
		}
		String queryUser = "select USER_ID,MANAGE_USER,OWNER from oa_file_sort where SEQ_ID=" + sortId;
		Statement stm2 = null;
		ResultSet rs2 = null;
		String userId = "";
		String manageUser = "";
		String owner = "";
		try {
			stm2 = conn.createStatement();
			rs2 = stm2.executeQuery(queryUser);
			if (rs2.next()) {
				userId = rs2.getString("USER_ID");
				manageUser = rs2.getString("MANAGE_USER");
				owner = rs2.getString("OWNER");

				if (userId == null) {
					userId = "";
				}
				if (manageUser == null) {
					manageUser = "";
				}
				if (owner == null) {
					owner = "";
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stm2, rs2, null);
		}
		boolean ownPriv = this.checkPriv(user, owner);
		boolean userIdPriv = this.checkPriv(user, userId);
		boolean manageUserPriv = this.checkPriv(user, manageUser);
		String seqId = String.valueOf(user.getSeqId());
		boolean isUser = userId.equals(seqId);
		accessPriv = isUser || userIdPriv || ownPriv;
		managePriv = isUser || manageUserPriv;

		if (!accessPriv || !managePriv) {
			return;
		}
		String del = "update oa_file_content set READERS='' where SEQ_ID=" + contentId;
		Statement stm3 = null;
		try {
			stm3 = conn.createStatement();
			stm3.executeUpdate(del);
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stm3, null, null);
		}
	}

	public boolean checkPriv(YHPerson user, String privStr) {
		if (privStr == null || user == null) {
			return false;
		}
		String[] aPriv = privStr.split("\\|");
		String privUser = "";
		String privRole = "";
		String privDept = "";
		if (aPriv.length > 0) {
			privDept = aPriv[0];
		}
		if (aPriv.length > 1) {
			privRole = aPriv[1];
		}
		if (aPriv.length > 2) {
			privUser = aPriv[2];
		}
		if ("0".equals(privDept) || "ALL_DEPT".equals(privDept) || this.findId(privUser, String.valueOf(user.getSeqId()))
				|| this.findId(privDept, String.valueOf(user.getDeptId())) || this.findId(privRole, user.getUserPriv())
				|| !this.checkId(privRole, user.getUserPrivOther(), true).equals("") || !this.checkId(privDept, user.getDeptIdOther(), true).equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 交集字符串,非交集
	 * 
	 * 
	 * @param str
	 *          字符串以,分,分割
	 * @param flag
	 *          true-取两字符串的交集,false-取非交集
	 * @return
	 */
	public static String checkId(String str, String ids, boolean flag) {
		if (ids == null) {
			ids = "";
		}
		String[] aStr = ids.split(",");
		String idStr = "";
		for (String tmp : aStr) {
			if (flag) {
				if (findId(str, tmp)) {
					idStr += tmp + ",";
				}
			} else {
				if (!findId(str, tmp)) {
					idStr += tmp + ",";
				}
			}
		}
		return idStr;
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
	
	
	
}
