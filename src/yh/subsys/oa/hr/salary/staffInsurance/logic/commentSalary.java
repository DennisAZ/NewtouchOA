package yh.subsys.oa.hr.salary.staffInsurance.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;


public class commentSalary {
	/**
	 * 查找所属部门
	 * @param dbConn
	 * @param user
	 * @param depts
	 * @return
	 * @throws Exception
	 */
	public String findDept(Connection dbConn, YHPerson user,String depts) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String addDeptName="";
		if (!YHUtility.isNullorEmpty(depts)) {
			
		}
		List list = new ArrayList();
		String sql = "select DEPT_NAME from oa_department where SEQ_ID in ("+ depts + ")";
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
		   
			while (rs.next()) {
				 list.add(rs.getString("DEPT_NAME")); 
			}
			if(list.size()>0){
				for(int i=0; i<list.size(); i++){
					addDeptName	+= (String) list.get(i)+",";
				}
				addDeptName = addDeptName.substring(0, addDeptName.length() - 1);
			}
			/*if(deleteStr != null && !"".equals(deleteStr)){
		        String[] deleteStrs = deleteStr.split(",");
		        deleteStr = "";
		        for(int i = 0 ;i < deleteStrs.length ; i++){
		           deleteStr +=  "'" + deleteStrs[i] + "'" + ",";
		        }
		           deleteStr = deleteStr.substring(0, deleteStr.length() - 1);
		    }*/
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return addDeptName;
	}
	/**
	 * 查找仓库管理员
	 * @param dbConn
	 * @param user
	 * @param manager
	 * @return
	 * @throws Exception
	 */
	public String findManager(Connection dbConn, YHPerson user,String manager) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List list = new ArrayList();
		String managers="";
		String sql = "select USER_NAME from person where SEQ_ID in ("+ manager + ")";
		try {
			ps = dbConn.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
		
			while (rs.next()) {
				 list.add(rs.getString("USER_NAME")); 
			}
			
			if(list.size()>0){
				for(int i=0; i<list.size(); i++){
					managers	+= (String) list.get(i)+",";
				}
				managers = managers.substring(0, managers.length() - 1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return managers;
	}
	
	/**
	 * 查找部门下所属人员
	 * @param dbConn
	 * @param user
	 * @param proKeeper
	 * @return
	 * @throws Exception
	 */
	public String findDeptPerson(Connection dbConn, YHPerson user) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List list = new ArrayList();
		String deptPerson ="";
		String sql = "select SEQ_ID from PERSON";
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				 deptPerson	+= rs.getInt("SEQ_ID") + ",";
			}
			/*if(deptPerson.endsWith(",")){
				deptPerson = deptPerson.substring(0, deptPerson.length()-1);
			}*/
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return deptPerson;
	}
	/**
	 * 查找部门下所属人员1
	 * @param dbConn
	 * @param user
	 * @param proKeeper
	 * @return
	 * @throws Exception
	 */
	public String findDeptPerson1(Connection dbConn, YHPerson user,String personIds) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String deptPerson ="";
		//String sql = "select SEQ_ID from PERSON where "+YHDBUtility.findInSet(personIds, "DEPT_ID")+" ";
		String sql = "select SEQ_ID from PERSON where  dept_id in (" +personIds+")";
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
		
			while (rs.next()) {			
				 deptPerson	+= rs.getInt("SEQ_ID") + ",";
			}			
			/*if(deptPerson.endsWith(",")){
				deptPerson = deptPerson.substring(0, deptPerson.length()-1);
			}*/
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return deptPerson;
	}
	/**
	 * 通过人员id 查找人员
	 * @param dbConn
	 * @param user
	 * @param manager etProKeeper
	 * @return
	 * @throws Exception
	 */
	public String idqueryPerson(Connection dbConn, YHPerson user,String personId,String userIdStr) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String deptPerson ="";
		String seqId ="";
		//String sql = "select SEQ_ID from person where "+YHDBUtility.findInSet(personId, "SEQ_ID")+"";
		String sql = "select SEQ_ID from person where SEQ_ID in (" +personId+")";
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {			
				seqId = rs.getString("SEQ_ID") ;
				if(!userIdStr.contains(seqId)){
					userIdStr += seqId+",";
				} 
			}			
			/*if(deptPerson.endsWith(",")){
				deptPerson = deptPerson.substring(0, deptPerson.length()-1);
			}*/
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return userIdStr;
	}
	/**
	 * 通过设置角色查找人员
	 * @param dbConn
	 * @param user
	 * @param personId
	 * @param userIdStr
	 * @return
	 * @throws Exception
	 */
	public String roleQueryPerson(Connection dbConn, YHPerson user,String personId,String userIdStr) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String deptPerson ="";
		String seqId ="";
		//String sql = "select SEQ_ID from person where "+YHDBUtility.findInSet(personId, "USER_PRIV")+"";
		String sql = "select SEQ_ID from person where USER_PRIV in (" +personId+")";
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {			
				seqId = rs.getString("SEQ_ID") ;
				if(!userIdStr.contains(seqId)){
					userIdStr += seqId+",";
				} 
			}			
			/*if(deptPerson.endsWith(",")){
				deptPerson = deptPerson.substring(0, deptPerson.length()-1);
			}*/
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return userIdStr;
	}
}
