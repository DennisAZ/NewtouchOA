package yh.core.module.org_select.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.util.db.YHDBUtility;

public class YHOrgSelectModuleLogic {
  private static Logger log = Logger
  .getLogger("yh.core.module.org_select.logic.YHOrgSelectModuleLogic");
  
  public ArrayList<YHDepartment> getChildDept(Connection conn,int deptId) throws Exception{
    ArrayList<YHDepartment> depts = new ArrayList<YHDepartment>();
    String sql = "select " + "  SEQ_ID ,DEPT_NAME" + " from " + " oa_department "
    + " where " + " DEPT_PARENT = " + deptId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHDepartment dept = new YHDepartment();
        int deptSeqId = rs.getInt(1);
        String deptName = rs.getString(2);
        dept.setSeqId(deptSeqId);
        dept.setDeptName(deptName);
        depts.add(dept);
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return depts;
  }
  /**
   * 取得当前部门下所有符合当前登录用户管理范围的用户
   * @param conn
   * @param deptId
   * @param mp
   * @param loginUser
   * @return
   * @throws Exception
   */
  public ArrayList<YHPerson> getChildDeptPerson(Connection conn,int deptId,YHMyPriv mp ,YHPerson loginUser) throws Exception{
    ArrayList<YHPerson> persons = new ArrayList<YHPerson>();
    String sql = "SELECT SEQ_ID , USER_NAME FROM PERSON WHERE SEQ_ID != " + loginUser.getSeqId() + " AND DEPT_ID = " + deptId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        int seqId = rs.getInt(1);
        if(YHPrivUtil.isUserPriv(conn, seqId, mp, loginUser.getPostPriv(), loginUser.getPostDept(), loginUser.getSeqId(), loginUser.getDeptId())){
          continue;
        }
        YHPerson person = new YHPerson();
        
        String userName = rs.getString(2);
        person.setSeqId(seqId);
        person.setUserName(userName);
        persons.add(person);
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return persons;
  }
}
