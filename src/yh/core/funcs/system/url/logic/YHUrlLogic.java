package yh.core.funcs.system.url.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import yh.core.util.db.YHORM;
import yh.core.data.YHDsTable;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.util.db.YHDBUtility;

public class YHUrlLogic {
  private static Logger log = Logger.getLogger(YHUrlLogic.class);
  private YHORM orm = new YHORM();
  private Connection conn ;
  public YHUrlLogic(){
  }
  public YHUrlLogic(Connection conn){
    this.conn = conn;
  }
  public List deleteDeptMul(Connection dbConn, int seqId) {
    List list = new ArrayList();
    int seqID = 0;
    YHDepartment de = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT SEQ_ID FROM oa_department WHERE DEPT_PARENT = '" + seqId + "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        de = new YHDepartment();
        de.setSeqId(rs.getInt("SEQ_ID"));
        list.add(de);
      }
      for(Iterator it = list.iterator(); it.hasNext();){
//        for(int x = 0; x<list.size(); x++){
//          YHDepartment 
//          
//        }
        YHDepartment der = (YHDepartment)(it.next());
        List srclist = deleteDeptMul(dbConn,der.getSeqId());
        list.addAll(srclist);
      }
    } catch (Exception ex) {
      // throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  
  public List deleteSelf(Connection dbConn, int seqId) {
    //System.out.println(seqId+"reeeeeeeeee");
    List list1 = new ArrayList();
    YHDepartment de = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT SEQ_ID FROM oa_department WHERE DEPT_PARENT = '" + seqId + "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        de = new YHDepartment();
        de.setSeqId(rs.getInt("SEQ_ID"));
        list1.add(de);
      }
      for(Iterator it = list1.iterator(); it.hasNext();){
        YHDepartment dd = (YHDepartment)(it.next());
        //System.out.println(dd.getSeqId()+"rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
        //YHORM orm = new YHORM();
       // YHDepartment dt = (YHDepartment)orm.loadObjComplex(dbConn, YHDepartment.class, dd.getSeqId());
        //dt.setSeqId(seqId);
      //  orm.deleteComplex(dbConn, dt);
        
        deleteSelf(dbConn,dd.getSeqId());
      }
      
    } catch (Exception ex) {
      // throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list1;
  }
  public boolean existsTableNo(Connection dbConn, String deptNo)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) FROM oa_department WHERE DEPT_NO = '" + deptNo
          + "'";
      rs = stmt.executeQuery(sql);
      //System.out.println(sql);
      long count = 0;
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count == 1) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  public boolean existsDeptPosition(Connection dbConn, String positionNo)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) FROM oa_dept_role WHERE POSITION_NO = '"
          + positionNo + "'";
      rs = stmt.executeQuery(sql);
      //System.out.println(sql);
      long count = 0;
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count == 1) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  public List selectDept(Connection dbConn, String total) throws Exception {
    List list = new ArrayList();
    YHDsTable dt = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT * FROM oa_table_dicts";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        dt = new YHDsTable();
        dt.setSeqId(rs.getInt("SEQ_ID"));
        dt.setTableNo(rs.getString("table_No"));
        dt.setTableName(rs.getString("table_Name"));
        dt.setTableDesc(rs.getString("table_Desc"));
        dt.setClassName(rs.getString("class_Name"));
        dt.setCategoryNo(rs.getString("category_No"));
        dt.setDbNo(rs.getString("db_No"));
        list.add(dt);
      }

    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  public List getDeptList() throws Exception{
    List list = new ArrayList();
    Map filters = new HashMap();
    list  = orm.loadListSingle(this.conn ,YHDepartment.class , filters);
    return list;
  }
  
  public YHDepartment getDepartmentById(int deptId) throws Exception{
    YHDepartment department = new YHDepartment();
    department = (YHDepartment) this.orm.loadObjSingle(this.conn, YHDepartment.class, deptId);
    return department;
  }
  //根据id串取得名字串
  public String getNameByIdStr(String ids) throws Exception, Exception{
    String names = "";
    String[] aId = ids.split(",");
    for(String tmp : aId){
      if(!"".equals(tmp)){
        YHDepartment department = this.getDepartmentById(Integer.parseInt(tmp));
        names += department.getDeptName() + ",";
      }
    }
    return names;
  }
  public String getDeptTreeJson(int deptId) throws Exception{
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    this.getDeptTree(deptId, sb, 0);
    if(sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    
    return sb.toString();
  }
  public void getDeptTree(int deptId , StringBuffer sb , int level) throws Exception{
    //首选分级，然后记录级数，是否为最后一个。。。
    List<YHDepartment> list = this.getDeptByParentId(deptId);
    
    for(int i = 0 ;i < list.size() ;i ++){
      String flag = "├";
      if(i == list.size() - 1 ){
        flag = "└";
      }
      String tmp = "";
      for(int j = 0 ;j < level ; j++){
        tmp += "｜";
      }
      flag = tmp + flag;
      
      YHDepartment dp = list.get(i);
      sb.append("{");
      sb.append("text:'" + flag + dp.getDeptName() + "',");
      sb.append("value:" + dp.getSeqId() );
      sb.append("},");
    
      this.getDeptTree(dp.getSeqId(), sb, level + 1);
    }
   
  }
  public List<YHDepartment> getDeptByParentId(int deptId) throws Exception {
    // TODO Auto-generated method stub
    List<YHDepartment> list = new ArrayList();
    Map filters = new HashMap();
    filters.put("DEPT_PARENT", deptId);
    list  = orm.loadListSingle(this.conn ,YHDepartment.class , filters);
    return list;
    
  }
}
