package yh.core.funcs.attendance.manage.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.attendance.personal.data.YHAttendEvection;
import yh.core.funcs.attendance.personal.data.YHAttendLeave;
import yh.core.funcs.attendance.personal.data.YHAttendOut;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.fillRegister.data.YHAttendFill;

public class YHManageAttendLogic {
  private static Logger log = Logger.getLogger(YHManageAttendLogic.class);
  public long getLongByDutyTime(String dutyTime){
    long time = 0;
    String times[] = dutyTime.split(":");
    int length = times.length;
    for (int i = 0; i < times.length; i++) {
      time = time + Long.parseLong(times[i])* (long)(Math.pow(60, length-1-i)) ;
    }
    return time;
  }
  public List<YHAttendLeave> selectLeave(Connection dbConn,String str[])throws Exception {
    YHORM orm = new YHORM();
    List<YHAttendLeave> leaveList = (List<YHAttendLeave>) orm.loadListSingle(dbConn, YHAttendLeave.class, str);
    return leaveList;
  }  
  public List<YHAttendEvection> selectEvection(Connection dbConn,String str[])throws Exception {
    YHORM orm = new YHORM();
    List<YHAttendEvection> evectonList = (List<YHAttendEvection>) orm.loadListSingle(dbConn, YHAttendEvection.class, str);
    return evectonList;
  }
  public List<YHAttendOut> selectOut(Connection dbConn,String str[])throws Exception {
    YHORM orm = new YHORM();
    List<YHAttendOut> outList = (List<YHAttendOut>) orm.loadListSingle(dbConn, YHAttendOut.class, str);
    return outList;
  }
  
  public List<YHAttendFill> getFillRegister(Connection dbConn,String str[])throws Exception {
    YHORM orm = new YHORM();
    List<YHAttendFill> evectonList = (List<YHAttendFill>) orm.loadListSingle(dbConn, YHAttendFill.class, str);
    return evectonList;
  }
  //根据排班类型得到所有人员
  public List<YHPerson> selectPerson(Connection dbConn,String[] str) throws Exception{
    List<YHPerson> personList = new ArrayList<YHPerson>();
    YHORM orm = new YHORM();
    personList = orm.loadListSingle(dbConn, YHPerson.class, str);
    return personList;
  }
  //根据部门得到所有人员Id
  public String selectUserIds(int deptId,Connection dbConn) throws Exception{
    String userIds = "";
    Statement stmt = null;
    ResultSet rs = null;
    String deptIds = this.getDeptTreeSeqIds(deptId, dbConn);
    if(!deptIds.equals("")){
      String sql = "select p.SEQ_ID as SEQ_ID from PERSON p,oa_department d where p.DEPT_ID = d.SEQ_ID and d.SEQ_ID in(" + deptIds + ")";
      //System.out.println(sql);
        try {
          stmt = dbConn.createStatement();
          rs = stmt.executeQuery(sql);
          while(rs.next()){
            userIds = userIds +rs.getString("SEQ_ID")+",";
          }
          if(!userIds.equals("")){
            userIds = userIds.substring(0, userIds.length()-1);
          }
        }catch(Exception ex) {
          throw ex;
       }finally {
         YHDBUtility.close(stmt, rs, log);
     }      
    }   
    return userIds;
  }
  //根据部门得到子部门Id和本部门Id
  public  String getDeptTreeSeqIds(int deptId , Connection conn) throws Exception{
    StringBuffer sb = new StringBuffer();
    this.getDeptTree(deptId, sb, 0 , conn);
    //System.out.println(sb.length()+":"+(sb==null));
    if(sb.length()>0){
      sb.deleteCharAt(sb.length() - 1);
    }
    if(deptId!=0&&sb.length()>0){
      sb.append(","+deptId);  
    }else if(deptId!=0&&sb.length()<=0){
      sb.append(deptId);  
    }
    return sb.toString();
  }
  public void getDeptTree(int deptId , StringBuffer sb , int level , Connection conn) throws Exception{
    //首选分级，然后记录级数，是否为最后一个。。。

    List<YHDepartment> list = this.getDeptByParentId(deptId , conn);
    
    for(int i = 0 ;i < list.size() ;i ++){
      YHDepartment dp = list.get(i);
      sb.append(dp.getSeqId());
      sb.append(",");
      this.getDeptTree(dp.getSeqId(), sb, level + 1 , conn);
    }
   
  }
  //部门
  public List<YHDepartment> getDeptByParentId(int deptId ,Connection conn) throws Exception {
    // TODO Auto-generated method stub
    YHORM orm = new YHORM();
    List<YHDepartment> list = new ArrayList();
    Map filters = new HashMap();
    filters.put("DEPT_PARENT", deptId);
    list  = orm.loadListSingle(conn ,YHDepartment.class , filters);
    return list;
    
  }
}
