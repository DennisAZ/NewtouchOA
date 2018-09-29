package yh.subsys.oa.hr.setting.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.hr.setting.data.YHHrManager;

public class YHHrManagerLogic {
	private static Logger log = Logger.getLogger(YHHrManagerLogic.class);

	public void setBatchValueLogic(Connection dbConn, String operation, String userStr, String deptStr) throws Exception {
		if (YHUtility.isNullorEmpty(deptStr)) {
			deptStr = "";
		}
		YHORM orm = new YHORM();
		try {
			String deptArry[] = deptStr.split(",");
			if ("0".equals(operation)) {
				if ("0".equals(deptStr.trim())) {
					List<YHHrManager> hrManagers = orm.loadListSingle(dbConn, YHHrManager.class, new HashMap());
					if (hrManagers != null && hrManagers.size() > 0) {
						for (YHHrManager hrManager : hrManagers) {
							String dbDeptHrManager = YHUtility.null2Empty(hrManager.getDeptHrManager());
							String deptTem = "";
							if (!YHUtility.isNullorEmpty(dbDeptHrManager) && !YHUtility.isNullorEmpty(userStr)) {
								deptTem = ",";
							}
							String deptManagerString = dbDeptHrManager + deptTem + userStr;
							String deptHrManager = this.delReIdStr(deptManagerString);
							hrManager.setDeptHrManager(deptHrManager);
							orm.updateSingle(dbConn, hrManager);
						}
					}
				} else {
					for (String deptId : deptArry) {
						Map<Object, Object> map = this.findInSet(dbConn, deptId);
						boolean flag = (Boolean) map.get("flag");
						int dbSeqId = (Integer) map.get("seqId");
						int dbDeptId = (Integer) map.get("deptId");
						String dbDeptHrManager = (String) map.get("deptManagerStr");
						if (flag) {
							String deptTem = "";
							if (!YHUtility.isNullorEmpty(dbDeptHrManager) && !YHUtility.isNullorEmpty(userStr)) {
								deptTem = ",";
							}
							String deptManagerString = dbDeptHrManager + deptTem + userStr;
							String deptHrManager = this.delReIdStr(deptManagerString);
							YHHrManager manager = new YHHrManager();
							manager.setSeqId(dbSeqId);
							manager.setDeptId(dbDeptId);
							manager.setDeptHrManager(deptHrManager);
							orm.updateSingle(dbConn, manager);
						} else {
							// YHHrManager manager = new YHHrManager();
							// manager.setDeptId(Integer.parseInt(deptId));
							// manager.setDeptHrManager(userStr);
							// orm.saveSingle(dbConn, manager);
						}
					}
				}
			} else if ("1".equals(operation.trim())) {
				if ("0".equals(deptStr.trim())) {
					List<YHHrManager> hrManagers = orm.loadListSingle(dbConn, YHHrManager.class, new HashMap());
					if (hrManagers != null && hrManagers.size() > 0) {
						for (YHHrManager hrManager : hrManagers) {
							String dbDeptHrManager = YHUtility.null2Empty(hrManager.getDeptHrManager());
							String deptHrManager = this.getDelIdStrs(userStr, dbDeptHrManager);
							hrManager.setDeptHrManager(deptHrManager);
							orm.updateSingle(dbConn, hrManager);
						}
					}
				} else {
					for (String deptId : deptArry) {
						Map<Object, Object> map = this.findInSet(dbConn, deptId);
						boolean flag = (Boolean) map.get("flag");
						int dbSeqId = (Integer) map.get("seqId");
						int dbDeptId = (Integer) map.get("deptId");
						String dbDeptHrManager = (String) map.get("deptManagerStr");
						if (flag) {
							String deptHrManager = this.getDelIdStrs(userStr, dbDeptHrManager);
							YHHrManager manager = new YHHrManager();
							manager.setSeqId(dbSeqId);
							manager.setDeptId(dbDeptId);
							manager.setDeptHrManager(deptHrManager);
							orm.updateSingle(dbConn, manager);
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
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

	public Map<Object, Object> findInSet(Connection dbConn, String str) throws Exception {
		boolean flag = false;
		Map<Object, Object> map = new HashMap<Object, Object>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select SEQ_ID,DEPT_HR_MANAGER,DEPT_ID from oa_pm_manager where " + YHDBUtility.findInSet(str, "DEPT_ID");
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			int seqId = 0;
			int deptId = 0;
			String deptManagerStr = "";
			if (rs.next()) {
				seqId = rs.getInt("SEQ_ID");
				deptId = rs.getInt("DEPT_ID");
				deptManagerStr = YHUtility.null2Empty(rs.getString("DEPT_HR_MANAGER"));
				flag = true;
			}
			map.put("seqId", seqId);
			map.put("deptId", deptId);
			map.put("deptId", deptId);
			map.put("deptManagerStr", deptManagerStr);
			map.put("flag", flag);
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return map;
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
	 * 获取人力资源管理员名称
	 * 
	 * @param dbConn
	 * @param deptIdStr
	 * @return
	 * @throws Exception
	 */
	public String getHrManagerLogic(Connection dbConn, String deptIdStr) throws Exception {
		String data = "";
		int deptId = 0;
		if (YHUtility.isNullorEmpty(deptIdStr)) {
			deptIdStr = "-1";
		}
		if ("ALL_DEPT".equals(deptIdStr)) {
			deptIdStr = "0";
		}
		if (YHUtility.isNumber(deptIdStr)) {
			deptId = Integer.parseInt(deptIdStr);
		}
		YHORM orm = new YHORM();
		try {
			Map map = new HashMap();
			map.put("DEPT_ID", deptId);
			YHHrManager manager = (YHHrManager) orm.loadObjSingle(dbConn, YHHrManager.class, map);
			String deptHrManager = "";
			String userName = "";
			if (manager != null) {
				deptHrManager = YHUtility.null2Empty(manager.getDeptHrManager());
				if (!YHUtility.isNullorEmpty(deptHrManager)) {
					userName = this.getUserNameLogic(dbConn, deptHrManager);
				}
			} else {
				YHHrManager hrManager = new YHHrManager();
				hrManager.setDeptId(deptId);
				orm.saveSingle(dbConn, hrManager);
			}
			data = "{userName:\"" + YHUtility.encodeSpecial(userName) + "\"}";
		} catch (Exception e) {
			throw e;
		}
		return data;

	}

	/**
	 * 获取人力资源管理员Id串
	 * 
	 * @param dbConn
	 * @param deptIdStr
	 * @return
	 * @throws Exception
	 */
	public String getHrManagerIdStrLogic(Connection dbConn, String deptIdStr) throws Exception {
		int deptId = 0;
		if (YHUtility.isNullorEmpty(deptIdStr)) {
			deptIdStr = "-1";
		}
		if ("ALL_DEPT".equals(deptIdStr)) {
			deptIdStr = "0";
		}
		if (YHUtility.isNumber(deptIdStr)) {
			deptId = Integer.parseInt(deptIdStr);
		}
		YHORM orm = new YHORM();
		try {
			Map map = new HashMap();
			map.put("DEPT_ID", deptId);
			YHHrManager manager = (YHHrManager) orm.loadObjSingle(dbConn, YHHrManager.class, map);
			String deptHrManager = "";
			if (manager != null) {
				deptHrManager = YHUtility.null2Empty(manager.getDeptHrManager());
			}
			String data = "{deptHrManager:\"" + YHUtility.encodeSpecial(deptHrManager) + "\"}";
			return data;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 编辑人力资源管理员Id串
	 * 
	 * @param dbConn
	 * @param deptIdStr
	 * @return
	 * @throws Exception
	 */
	public void editHrManagerLogic(Connection dbConn, String deptIdStr, String deptHrManagerStr) throws Exception {
		int deptId = 0;
		if (YHUtility.isNullorEmpty(deptIdStr)) {
			deptIdStr = "-1";
		}
		if ("ALL_DEPT".equals(deptIdStr)) {
			deptIdStr = "0";
		}
		if (YHUtility.isNumber(deptIdStr)) {
			deptId = Integer.parseInt(deptIdStr);
		}
		YHORM orm = new YHORM();
		try {
			Map map = new HashMap();
			map.put("DEPT_ID", deptId);
			YHHrManager manager = (YHHrManager) orm.loadObjSingle(dbConn, YHHrManager.class, map);
			if (manager != null) {
				manager.setDeptHrManager(deptHrManagerStr);
				orm.updateSingle(dbConn, manager);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 取得人员名称
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getUserNameLogic(Connection dbConn, String seqIdStr) throws Exception {
		String result = "";
		if (YHUtility.isNullorEmpty(seqIdStr)) {
			return result;
		}
		if (seqIdStr.endsWith(",")) {
			seqIdStr = seqIdStr.substring(0, seqIdStr.length() - 1);
		}
		String sql = " select USER_NAME from PERSON where SEQ_ID in(" + seqIdStr + ")";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String toId = rs.getString(1);
				if (toId != null) {
					result += toId + ",";
				}
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

}
