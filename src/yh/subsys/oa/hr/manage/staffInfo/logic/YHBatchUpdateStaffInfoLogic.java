package yh.subsys.oa.hr.manage.staffInfo.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.hr.manage.staffInfo.data.YHHrStaffInfo;

public class YHBatchUpdateStaffInfoLogic {
	private static Logger log = Logger.getLogger(YHBatchUpdateStaffInfoLogic.class);

	public void batchUpdateInfoLogic(Connection dbConn, Map<Object, Object> map) throws Exception {
		String userIds = (String) map.get(("staffName"));
		String leaveType = (String) map.get(("leaveType"));
		String workStatus = (String) map.get(("workStatus"));
		String mode = (String) map.get(("mode"));
		String selectitem = (String) map.get(("selectitem"));
		String tContext = (String) map.get(("tContext"));
		YHORM orm = new YHORM();
		try {
			String userIdArry[] = userIds.split(",");
			if (userIdArry != null && userIdArry.length > 0) {
				for (String userIdStr : userIdArry) {

					String userId = this.getUserIdStr(dbConn, Integer.parseInt(userIdStr));
					boolean isHaveFlag = this.checkUser(dbConn, userId);
					if (isHaveFlag) {
						Map<Object, Object> filters = new HashMap<Object, Object>();
						filters.put("USER_ID", userId);
						YHHrStaffInfo staffInfo = (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, filters);
						if (staffInfo != null) {
							if (!YHUtility.isNullorEmpty(leaveType) && YHUtility.isNumber(leaveType)) {
								staffInfo.setLeaveType(Integer.parseInt(leaveType));
							}
							if (!YHUtility.isNullorEmpty(workStatus)) {
								staffInfo.setWorkStatus(workStatus);
							}
							if (!"-1".equals(selectitem) && !YHUtility.isNullorEmpty(tContext)) {
								if ("overwrite".equals(mode)) {
									if ("certificate".equals(selectitem)) {
										staffInfo.setCertificate(tContext);
									}
									if ("surety".equals(selectitem)) {
										staffInfo.setSurety(tContext);
									}
									if ("insure".equals(selectitem)) {
										staffInfo.setInsure(tContext);
									}
									if ("bodyExamim".equals(selectitem)) {
										staffInfo.setBodyExamim(tContext);
									}
									if ("remark".equals(selectitem)) {
										staffInfo.setRemark(tContext);
									}

								} else {
									String concatStr = "";
									if ("certificate".equals(selectitem)) {
										if (!YHUtility.isNullorEmpty(staffInfo.getCertificate())) {
											concatStr = staffInfo.getCertificate() + "\n" + tContext;
										} else {
											concatStr = staffInfo.getCertificate();
										}
										staffInfo.setCertificate(concatStr);
									}
									if ("surety".equals(selectitem)) {
										if (!YHUtility.isNullorEmpty(staffInfo.getSurety())) {
											concatStr = staffInfo.getSurety() + "\n" + tContext;
										} else {
											concatStr = staffInfo.getSurety();
										}
										staffInfo.setSurety(concatStr);
									}
									if ("insure".equals(selectitem)) {
										if (!YHUtility.isNullorEmpty(staffInfo.getInsure())) {
											concatStr = staffInfo.getInsure() + "\n" + tContext;
										} else {
											concatStr = staffInfo.getInsure();
										}
										staffInfo.setInsure(concatStr);
									}
									if ("bodyExamim".equals(selectitem)) {
										if (!YHUtility.isNullorEmpty(staffInfo.getBodyExamim())) {
											concatStr = staffInfo.getBodyExamim() + "\n" + tContext;
										} else {
											concatStr = staffInfo.getBodyExamim();
										}
										staffInfo.setBodyExamim(concatStr);
									}
									if ("remark".equals(selectitem)) {
										if (!YHUtility.isNullorEmpty(staffInfo.getRemark())) {
											concatStr = staffInfo.getRemark() + "\n" + tContext;
										} else {
											concatStr = staffInfo.getRemark();
										}
										staffInfo.setRemark(concatStr);
									}
								}
							}
							orm.updateSingle(dbConn, staffInfo);
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public String getUserIdStr(Connection dbConn, int seqId) throws Exception {
		String data = "";
		YHORM orm = new YHORM();
		try {
			YHPerson person = (YHPerson) orm.loadObjSingle(dbConn, YHPerson.class, seqId);
			if (person != null) {
				data = YHUtility.null2Empty(person.getUserId());
			}
		} catch (Exception e) {
			throw e;
		}
		return data;
	}

	public boolean checkUser(Connection dbConn, String userId) throws Exception {
		boolean flag = false;
		int count = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select count(HR_STAFF_INFO.SEQ_ID) from oa_pm_employee_info as HR_STAFF_INFO,PERSON where HR_STAFF_INFO.USER_ID=PERSON.USER_ID and HR_STAFF_INFO.USER_ID=?";
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, userId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
			if (count == 1) {
				flag = true;
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

}
