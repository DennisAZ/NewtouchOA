package yh.subsys.oa.hr.setting.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.hr.manage.staffInfo.data.YHHrStaffInfo;

public class YHHrSetOtherLogic {
	private static Logger log = Logger.getLogger(YHHrSetOtherLogic.class);

	public String getHrSetUserLogin(Connection conn) throws Exception {
		String result = "";
		String sql = " select PARA_VALUE from SYS_PARA where PARA_NAME = 'HR_SET_USER_LOGIN'";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				String toId = rs.getString(1);
				if (toId != null) {
					result = toId;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		return result;
	}

	public String getHrRetireAge(Connection conn) throws Exception {
		String result = "";
		String sql = " select PARA_VALUE from SYS_PARA where PARA_NAME = 'RETIRE_AGE'";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				String toId = rs.getString(1);
				if (toId != null) {
					result = toId;
				}
			}
			String manAge = "";
			String womenAge = "";
			if (!YHUtility.isNullorEmpty(result)) {
				String resultArry[] = result.split(",");
				if (resultArry.length > 1) {
					manAge = resultArry[0];
					womenAge = resultArry[1];
				}else if(resultArry.length == 1) {
					manAge = resultArry[0];
				}
			
			}
			String data = "{manAge:\"" + YHUtility.encodeSpecial(manAge) + "\",womenAge:\"" + YHUtility.encodeSpecial(womenAge) + "\"}";
			return data;
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
	}
	
	/**
	 * 设置值
	 * @param dbConn
	 * @param yesOther
	 * @param manAge
	 * @param womenAge
	 * @throws Exception
	 */
	public void setOtherValueLogic(Connection dbConn,String yesOther,String manAge,String womenAge) throws Exception{
		if (YHUtility.isNullorEmpty(manAge)) {
			manAge = "";
		}
		if (YHUtility.isNullorEmpty(womenAge)) {
			womenAge = "";
		}
		String ageStr = manAge + "," + womenAge;
		try {
			
			this.updateYesOther(dbConn, yesOther);
			this.updateAgeValue(dbConn, ageStr);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void updateYesOther(Connection dbConn,String yesOther) throws Exception{
		String sql = "update SYS_PARA set PARA_VALUE=? where PARA_NAME='HR_SET_USER_LOGIN'";
		PreparedStatement stmt = null;
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, yesOther);
			stmt.executeUpdate();
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, null, log);
		}
	}
	public void updateAgeValue(Connection dbConn,String ageStr) throws Exception{
		String sql = "update SYS_PARA set PARA_VALUE=? where PARA_NAME='RETIRE_AGE'";
		PreparedStatement stmt = null;
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, ageStr);
			stmt.executeUpdate();
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, null, log);
		}
	}
	
	/**
	 * 根据hr表的seqId获取信息
	 * 2011-4-14
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public int getPersongCountLogic1(Connection dbConn, int seqId) throws Exception {
		String sql = "SELECT count(SEQ_ID) from PERSON where USER_ID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int personSeqId = 0;
		YHORM orm = new YHORM();
		try {
			String userId = "";
			YHHrStaffInfo staffInfo = (YHHrStaffInfo) orm.loadObjSingle(dbConn, YHHrStaffInfo.class, seqId);
			if (staffInfo != null) {
				userId = YHUtility.null2Empty(staffInfo.getUserId());
			}else {
				userId =String.valueOf(seqId);
			}
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, userId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				personSeqId = rs.getInt(1);
			}
			return personSeqId;
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
	}
	
	/**
	 * 根据person表的userId获取信息
	 * 2011-4-14
	 * @param dbConn
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public int getPersongCountLogic2(Connection dbConn, String userId) throws Exception {
		String sql = "SELECT count(SEQ_ID) from PERSON where USER_ID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int personSeqId = 0;
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, userId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				personSeqId = rs.getInt(1);
			}
			return personSeqId;
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
	}

}
