package yh.subsys.oa.profsys.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.calendar.logic.YHCalendarLogic;
import yh.core.load.YHPageLoader;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.data.YHProject;

public class YHProjectLogic {
  private static Logger log = Logger.getLogger(YHProjectLogic.class);
  public int addProject(Connection dbConn, YHProject project) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, project);
    return YHCalendarLogic.getMaSeqId(dbConn, "oa_project");
  }
  public void updateProject(Connection dbConn, YHProject project) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, project);
    //return YHCalendarLogic.getMaSeqId(dbConn, "oa_project");
  }
  public YHProject getProjectById(Connection dbConn, String seqId) throws Exception {
    YHORM orm = new YHORM();
    YHProject project = (YHProject) orm.loadObjSingle(dbConn, YHProject.class, Integer.parseInt(seqId));
    return project;
  }
  /**
   * 修改打印状态

   * 
   * @return
   * @throws Exception
   */
  public static void printOut(Connection dbConn,String printStr) throws Exception {
    String sql = "update oa_project set print_status='1' WHERE SEQ_ID in (" + printStr + ")";
    PreparedStatement ps = null;
    try {
      ps = dbConn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps,null, log);
    }
  }
  public static List<YHProject> queryProject(Connection dbConn,String[] str) throws Exception{
    YHORM orm = new YHORM();
    List<YHProject> project = new ArrayList<YHProject>();
    project = orm.loadListSingle(dbConn,YHProject.class,str);
    return project;
  }
  /**
   * 更新数据库中的文件

   * @param dbConn
   * @param attachmentId
   * @param attachmentName
   * @param seqId
   * @throws Exception
   */
  public void updateFile(Connection dbConn,String tableName,String attachmentId,String attachmentName,String seqId) throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rs = null; 
    try {
      String sql = "update " + tableName + " set ATTACHMENT_ID = ? ,ATTACHMENT_NAME = ? where SEQ_ID=?"   ;
      pstmt = dbConn.prepareStatement(sql);
      pstmt.setString(1, attachmentId);
      pstmt.setString(2,attachmentName);
      pstmt.setString(3, seqId);
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, rs, log);
    }
  }
  /**
   * 分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String toSearchData(Connection conn,Map request,String projType) throws Exception{
     String sql = "select p.SEQ_ID,p.PROJ_NUM,ba.BUDGET_ITEM,p.DEPT_ID,dep.DEPT_NAME, c.CLASS_DESC"
      + ",pn.USER_NAME,ci.CLASS_DESC,p.PROJ_ARRIVE_TIME,p.PROJ_LEAVE_TIME,p.P_YX,p.P_TOTAL,p.PRINT_STATUS"
      + " from oa_project p left outer join oa_department dep on p.DEPT_ID = dep.SEQ_ID"
      + " left outer join oa_kind_dict_item c on p.PROJ_VISIT_TYPE = c.SEQ_ID"
      + " left outer join oa_kind_dict_item ci on p.PROJ_ACTIVE_TYPE = ci.SEQ_ID "
      + " left outer join oa_ration_apply ba on p.BUDGET_ID = ba.SEQ_ID "
      + " left outer join PERSON pn on p.PROJ_LEADER = pn.SEQ_ID where p.PROJ_TYPE = '" + projType + "'";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList.toJson();
  }
  /** 
  * deptId串转换成dept_name串 
  * @return 
  * @throws Exception 
  */ 
  public static String deptStr(Connection dbConn,String deptId) throws Exception { 
  String strString = "全体部门";//传入得deptId串必须是：1,2,4 
  PreparedStatement ps = null; 
  ResultSet rs = null; 
  if (!deptId.equals("0") && !deptId.equals("ALL_DEPT") ) { 
    if(deptId.endsWith(",")){
      deptId = deptId.substring(0, deptId.length()-1);
    }
    String sql = "select dept_name from oa_department where seq_id in (" + deptId + ")"; 
    strString = ""; 
    try{ 
      ps = dbConn.prepareStatement(sql); 
      rs = ps.executeQuery(); 
      while (rs.next()) { 
        strString += rs.getString("dept_name") + ","; 
      } 
      if (strString.length() > 0) { 
        strString = strString.substring(0,strString.length()-1); 
      } 
    }catch (Exception e) { 
      throw e; 
    }finally { 
      YHDBUtility.close(ps, rs, log); 
    } 
  } 
  return strString; 
  }
}
