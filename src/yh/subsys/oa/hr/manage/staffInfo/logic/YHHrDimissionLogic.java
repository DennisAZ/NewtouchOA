package yh.subsys.oa.hr.manage.staffInfo.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.modulepriv.data.YHModulePriv;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHHrDimissionLogic {
	private static Logger log = Logger.getLogger(YHHrDimissionLogic.class);
	
	/**
	 * 离职人员信息列表
	 * @param dbConn
	 * @param request
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getDimissionListLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
		try {
			String sql = "select " 
				+ " h.SEQ_ID" 
				+ ", h.DEPT_ID" 
				+ ", p.USER_NAME" 
				+ ", h.STAFF_SEX" 
				+ ", h.STAFF_BIRTH"
				+ ", p.SEQ_ID as userId" 
				+ " from oa_pm_employee_info h,PERSON p"
				+ " where p.USER_ID=h.USER_ID AND p.DEPT_ID=0";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String getDimissionCauseLogic(Connection dbConn,String userIdStr) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select QUIT_REASON from oa_pm_employee_leave where SEQ_ID=(select MAX(SEQ_ID) from oa_pm_employee_leave where LEAVE_PERSON=?)";
		String dataStr = "";
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, userIdStr);
			rs = stmt.executeQuery();
			if (rs.next()) {
				dataStr = YHUtility.null2Empty(rs.getString(1));
			}
		} catch (Exception e) {
			throw e;
		}
		return dataStr;
	}
	
	/**
	 * 末记录所属部门
	 * @param dbConn
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getNotRecordDeptListLogic(Connection dbConn,YHPerson person) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT PERSON.USER_ID as userId," +
				" PERSON.SEQ_ID as personSeqId," +
				" USER_NAME," +
				" PRIV_NAME " +
				" from USER_PRIV,PERSON " +
				" left join oa_pm_employee_info as HR_STAFF_INFO on PERSON.USER_ID=HR_STAFF_INFO.USER_ID " +
				" where PERSON.DEPT_ID=0  and PERSON.USER_PRIV=USER_PRIV.SEQ_ID ";
		try {
			int privNo = this.getPrivNo(dbConn, person);
			String conditionStr = "";
			if (!person.isAdminRole()) {
				YHModulePriv modulePriv = this.getModulePriv(dbConn, 9, person.getSeqId());
				if (modulePriv!=null) {
					String rolePriv = YHUtility.null2Empty(modulePriv.getRolePriv());
					String privIdStr = YHUtility.null2Empty(modulePriv.getPrivId());
					String deptPrivStr = YHUtility.null2Empty(modulePriv.getDeptPriv());
					String userPrivStr = YHUtility.null2Empty(modulePriv.getUserId());
					
					if (privIdStr.endsWith(",")) {
						privIdStr.substring(0, privIdStr.length()-1);
					}
					if ("0".equals(rolePriv)) {
						conditionStr += " and USER_PRIV.PRIV_NO>"+ privNo;
					}else if ("1".equals(rolePriv)) {
						conditionStr += " and USER_PRIV.PRIV_NO>"+ privNo;
					}else if ("3".equals(rolePriv)) {
						if (!YHUtility.isNullorEmpty(privIdStr)) {
							conditionStr += " and PERSON.USER_PRIV in ("+ privIdStr + ")";
						}
					}
					if ("3".equals(deptPrivStr)) {
						if (!YHUtility.isNullorEmpty(userPrivStr)) {
							conditionStr += " and PERSON.USER_ID in ("+ userPrivStr + ")";
						}
					}
				}
			}
			String orderByStr = " order by PRIV_NO,USER_NO,USER_NAME";
			sql = sql+ conditionStr +orderByStr;  
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			StringBuffer buffer = new StringBuffer();
			boolean isHave = false;
			buffer.append("[");
			while(rs.next()){
				String dbUserId = YHUtility.null2Empty(rs.getString("userId"));
				String dbUserName = YHUtility.null2Empty(rs.getString("USER_NAME"));
				String dbPrivName = YHUtility.null2Empty(rs.getString("PRIV_NAME"));
				String dbPersonSeqId = YHUtility.null2Empty(rs.getString("personSeqId"));
				
				buffer.append("{");
				buffer.append("userId:\"" + YHUtility.encodeSpecial(dbUserId) + "\"");
				buffer.append(",userName:\"" + YHUtility.encodeSpecial(dbUserName) + "\"");
				buffer.append(",privName:\"" +  YHUtility.encodeSpecial(dbPrivName) + "\"");
				buffer.append(",personId:\"" +  YHUtility.encodeSpecial(dbPersonSeqId) + "\"");
				buffer.append("},");
				isHave = true;
			}
			if (isHave) {
				buffer.deleteCharAt(buffer.length()-1);
			}
			buffer.append("]");
			return buffer.toString();
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
	}
	
	public int  getPrivNo(Connection dbConn,YHPerson person) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT PRIV_NO from USER_PRIV where SEQ_ID=?";
		int priv = 0;
		int privNo = 0;
		if (YHUtility.isNumber(person.getUserPriv())) {
			priv = Integer.parseInt(person.getUserPriv());
		}
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setInt(1, priv);
			rs = stmt.executeQuery();
			if (rs.next()) {
				privNo = rs.getInt("PRIV_NO");
			}
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		return privNo;
		
	}
	
	
	public YHModulePriv getModulePriv(Connection dbConn,int moduleId,int loginUserId) throws Exception{
		YHModulePriv modulePriv = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select SEQ_ID,MODULE_ID,DEPT_PRIV,ROLE_PRIV,DEPT_ID,PRIV_ID,USER_ID,USER_SEQ_ID from oa_function_priv WHERE MODULE_ID =? AND USER_SEQ_ID=?";
		
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setInt(1, moduleId);
			stmt.setInt(2, loginUserId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				modulePriv = new YHModulePriv();
				modulePriv.setSeqId(rs.getInt("SEQ_ID"));
				modulePriv.setModuleId(rs.getInt("MODULE_ID"));
				modulePriv.setDeptPriv(rs.getString("DEPT_PRIV"));
				modulePriv.setRolePriv(rs.getString("ROLE_PRIV"));
				modulePriv.setDeptId(rs.getString("DEPT_ID"));
				modulePriv.setPrivId(rs.getString("PRIV_ID"));
				modulePriv.setUserId(rs.getString("USER_ID"));
				modulePriv.setUserSeqId(rs.getInt("USER_SEQ_ID"));
			}
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		return modulePriv;
	}
	
	
	
	
}
