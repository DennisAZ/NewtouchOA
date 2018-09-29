package yh.core.funcs.workstat.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHTimeCounter;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHWorkStatLogic {
  private static Logger log = Logger.getLogger(YHWorkStatLogic.class);

  /**
   * 取得部门下级级次树. String str = logic.getDeptTreeJson(0,conn);
   * 
   * @param deptId
   * @param conn
   * @return String [{value :'dd', text:'dds'},{value:'dd', text:'dd'}]
   * @throws Exception
   */
  public String getDeptTreeJson(int deptId, Connection conn, YHPerson user)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    stmt = conn.createStatement();
    // 通过DEPT_PRIV筛选管理范围
    String sql = "";
    String deptPriv = "";
    String rolePriv = "";
    String dept_id = "";
    String user_id = "";
    String userId = user.getSeqId() + "";
    int userDept = user.getDeptId();
    sql = "select * from oa_function_priv where module_id='8' and user_seq_id="
        + userId;
    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      deptPriv = rs.getString("dept_priv");
      rolePriv = rs.getString("role_priv");
      dept_id = rs.getString("dept_id");
      user_id = rs.getString("user_id");
    }
    // 0:本部门，1:全体，2:指定部门，3:指定人员
    if ("0".equals(deptPriv)) {
      this.getSelfDeptTree(userDept, sb, conn);
      this.getDeptTree(userDept, sb, 1, conn);
      if (sb.charAt(sb.length() - 1) == ',') {
        sb.deleteCharAt(sb.length() - 1);
      }
    } else if ("1".equals(deptPriv) || userId.equals("1")) {
      this.getDeptTree(0, sb, 0, conn);
      if (sb.charAt(sb.length() - 1) == ',') {
        sb.deleteCharAt(sb.length() - 1);
      }
    } else if ("2".equals(deptPriv)) {
      String dept[] = dept_id.split(",");
      for (int i = 0; i < dept.length; i++) {
        if (isNotChildDept(Integer.parseInt(dept[i]), dept, conn)) {
          this.getSelfDeptTree(Integer.parseInt(dept[i]), sb, conn);
          this.getDeptTree(Integer.parseInt(dept[i]), sb, 1, conn);
        }
      }
      if (sb.charAt(sb.length() - 1) == ',') {
        sb.deleteCharAt(sb.length() - 1);
      }
    } else if ("3".equals(deptPriv)) {

    }

    sb.append("]");

    return sb.toString();
  }

  public void getDeptTree(int deptId, StringBuffer sb, int level,
      Connection conn) throws Exception {
    // 首选分级，然后记录级数，是否为最后一个。。。
    List<Map> list = new ArrayList();
    String query = "select SEQ_ID , DEPT_NAME from oa_department where DEPT_PARENT="
        + deptId + " order by DEPT_NO, DEPT_NAME asc";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        String deptName = rs.getString("DEPT_NAME");
        int seqId = rs.getInt("SEQ_ID");
        Map map = new HashMap();
        map.put("deptName", deptName);
        map.put("seqId", seqId);
        list.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    for (int i = 0; i < list.size(); i++) {
      String flag = "&nbsp;├";
      if (i == list.size() - 1) {
        flag = "&nbsp;└";
      }
      String tmp = "";
      for (int j = 0; j < level; j++) {
        tmp += "&nbsp;│";
      }
      flag = tmp + flag;

      Map dp = list.get(i);
      int seqId = (Integer) dp.get("seqId");
      String deptName = (String) dp.get("deptName");
      sb.append("{");
      sb.append("text:'" + flag + YHUtility.encodeSpecial(deptName) + "',");
      sb.append("value:" + seqId);
      sb.append("},");

      this.getDeptTree(seqId, sb, level + 1, conn);
    }

  }

  /**
   * 选择下拉框内容
   * 
   * @param deptId
   * @param sb
   * @param conn
   * */

  public void getSelfDeptTree(int deptId, StringBuffer sb, Connection conn)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    stmt = conn.createStatement();

    String sql = "select SEQ_ID , DEPT_NAME from oa_department where SEQ_ID="
        + deptId + " order by DEPT_NO, DEPT_NAME asc";
    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      String deptName = rs.getString("DEPT_NAME");
      int seqId = rs.getInt("SEQ_ID");
      sb.append("{");
      sb
          .append("text:'" + "&nbsp;├" + YHUtility.encodeSpecial(deptName)
              + "',");
      sb.append("value:" + seqId);
      sb.append("},");

    }

  }

  // 判断deptId是否是dept[]的子部门
  public boolean isNotChildDept(int deptId, String dept[], Connection conn)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    stmt = conn.createStatement();

    String sql = "select DEPT_PARENT from oa_department where SEQ_ID=" + deptId;
    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      int deptParent = rs.getInt("DEPT_PARENT");

      for (int i = 0; i < dept.length; i++) {

        if (dept[i].equals(deptParent + "")) {
          return false;
        } else if (deptParent == 0) {
          return true;
        }

      }
      return this.isNotChildDept(deptParent, dept, conn);

    }

    return true;
  }

  public String getIsStaticLogic(Connection conn, int userId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    stmt = conn.createStatement();
    String deptPriv = "";
    String rolePriv = "";

    String sql = "select * from oa_function_priv where module_id='8' and user_seq_id="
        + userId;
    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      deptPriv = rs.getString("dept_priv");
    }

    return deptPriv;
  }

  /**
   * 获取统计报表数据
   * 
   * @param conn
   * @param userId
   * @param deptId
   * @param startDate
   * @param endDate
   * @param deptMore
   * 
   * */
  public String getDataLogic(Connection conn, YHPerson user, String deptId,
      String startDate, String endDate, String deptMore,String minNum,String maxNum) throws Exception {
    YHTimeCounter tc = new YHTimeCounter();
    Statement stmt = null;
    ResultSet rs = null;
    stmt = conn.createStatement();
    Statement stmt1 = null;
    ResultSet rs1 = null;
    stmt1 = conn.createStatement();
    StringBuffer data = new StringBuffer();

    String deptIdStr = "";
    String userIdStr = "";
    deptIdStr = deptId;

    String deptPriv = "";
    String rolePriv = "";
    String dept_id = "";
    String user_id = "";
    String priv_id = "";

    String sql = "select * from oa_function_priv where module_id='8' and user_seq_id="
        + user.getSeqId();

    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      deptPriv = rs.getString("dept_priv");
      rolePriv = rs.getString("role_priv");
      dept_id = rs.getString("dept_id");
      user_id = rs.getString("user_id");
      priv_id = rs.getString("priv_id");
    }
    /*if ("-1".equals(deptId) && !"1".equals(deptPriv) && !"3".equals(deptPriv)) {// 当用户不是全体权限，提示无权限。
      if (user.getSeqId() != 1) {
        return "1";
      }
    }*/

    // if(!"".equals(deptMore) && !isDeptPriv(conn,deptMore,user)){
    // //当所选的部门不属于本人管理范围时，提示无权限。
    // return "1";
    // }

    if (!"".equals(deptMore)) { // 部门id有下拉框值和更多部门的值
      deptId = deptId + "," + deptMore;
    }

    // 获取部门的子部门id
    if (',' == deptId.charAt(deptId.length() - 1)) {
      deptId = deptId.substring(0, deptId.length() - 1);
    }

    String deptOrg[] = deptId.split(",");
    for (int b = 0; b < deptOrg.length; b++) {
      deptId += ",";
      deptId += this.getDeptChildId(conn, deptOrg[b]);// //////////
    }

    if (',' == deptId.charAt(deptId.length() - 1)) {

      deptId = deptId.substring(0, deptId.length() - 1);
    }

    if (!"".equals(deptId)) { // 处理空的子23,,24
      String str = "";
      String Org[] = deptId.split(",");
      for (int x = 0; x < Org.length; x++) {
        if (!"".equals(Org[x])) {
          str += Org[x];
          str += ",";
        }
      }
      deptId = str.substring(0, str.length() - 1);

    }

    // 本人管理范围是：指定人员时
    if ("3".equals(deptPriv)) {
      sql = "select SEQ_ID,USER_PRIV from person where 1=1 and SEQ_ID in ("
          + user_id + ") order by DEPT_ID desc ";
      rs1 = stmt1.executeQuery(sql);
      while (rs1.next()) {
        int seqId = rs1.getInt("SEQ_ID");
        String userPriv = rs1.getString("USER_PRIV");
        if ("0".equals(rolePriv)) {
          if (userPriv.compareTo(user.getUserPriv()) > 0) {
            userIdStr += seqId;
            userIdStr += ",";
          }
        } else if ("1".equals(rolePriv)) {
          if (userPriv.compareTo(user.getUserPriv()) >= 0) {
            userIdStr += seqId;
            userIdStr += ",";
          }
        } else if ("3".equals(rolePriv)) {
          if (!"".equals(priv_id)) {
            String Org[] = priv_id.split(",");
            for (int x = 0; x < Org.length; x++) {
              if (userPriv.equals(Org[x])) {
                userIdStr += seqId;
                userIdStr += ",";
                break;
              }
            }
          }
        } else if ("2".equals(rolePriv) || user.getSeqId() == 1) {
          userIdStr += seqId;
          userIdStr += ",";
        }
      }

    } else {// 其他范围
      String whereStr = " and dept_id in (" + deptId + ") ";
      if ("-1".equals(deptId)) {
        whereStr = "";
      }
      sql = "select SEQ_ID,USER_PRIV from person where 1=1 " + whereStr
          + " order by DEPT_ID desc ";
      //System.out.print(sql);
      rs1 = stmt1.executeQuery(sql);
      while (rs1.next()) {
        int seqId = rs1.getInt("SEQ_ID");
        String userPriv = rs1.getString("USER_PRIV");
        if ("0".equals(rolePriv)) {
          if (userPriv.compareTo(user.getUserPriv()) > 0) {
            userIdStr += seqId;
            userIdStr += ",";
          }
        } else if ("1".equals(rolePriv)) {
          if (userPriv.compareTo(user.getUserPriv()) >= 0) {
            userIdStr += seqId;
            userIdStr += ",";
          }
        } else if ("3".equals(rolePriv)) {
          if (!"".equals(priv_id)) {
            String Org[] = priv_id.split(",");
            for (int x = 0; x < Org.length; x++) {
              if (userPriv.equals(Org[x])) {
                userIdStr += seqId;
                userIdStr += ",";
                break;
              }
            }
          }
        } else if ("2".equals(rolePriv) || user.getSeqId() == 1) {
          userIdStr += seqId;
          userIdStr += ",";
        }
      }

    }

    if (!"".equals(userIdStr)) {
      userIdStr = userIdStr.substring(0, userIdStr.length() - 1);
    }
  
    if (!"".equals(userIdStr)) {//System.out.println(userIdStr);
      userIdStr=this.getSplitUserIds(userIdStr,minNum,maxNum);
      if (!"".equals(userIdStr)){
         data = this.getDataList(conn, userIdStr, startDate, endDate);
      }
    }
    return data.toString();
  }

  
  
  /**
   * 获取统计报表数据
   * 
   * shenrm 2012-12-13 修改在自己最大角色以下的简报管理
   * 
   * @param conn
   * @param userId
   * @param deptId
   * @param startDate
   * @param endDate
   * @param deptMore
   * 
   * */
  public String getDataLogic1(Connection conn, YHPerson user, String deptId,
      String startDate, String endDate, String deptMore,String minNum,String maxNum) throws Exception {
	YHTimeCounter tc = new YHTimeCounter();
    Statement stmt = null;
    ResultSet rs = null;
    stmt = conn.createStatement();
    Statement stmt1 = null;
    ResultSet rs1 = null;
    stmt1 = conn.createStatement();
    
    Statement stmt2 = null;
    ResultSet rs2 = null;
    stmt2 = conn.createStatement();
    
    StringBuffer data = new StringBuffer();

    String deptIdStr = "";
    String userIdStr = "";
    deptIdStr = deptId;

    String deptPriv = "";
    String rolePriv = "";
    String dept_id = "";
    String user_id = "";
    String priv_id = "";

    String sql = "select * from oa_function_priv where module_id='8' and user_seq_id="
        + user.getSeqId();

    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      deptPriv = rs.getString("dept_priv");
      rolePriv = rs.getString("role_priv");
      dept_id = rs.getString("dept_id");
      user_id = rs.getString("user_id");
      priv_id = rs.getString("priv_id");
    }
    /*if ("-1".equals(deptId) && !"1".equals(deptPriv) && !"3".equals(deptPriv)) {// 当用户不是全体权限，提示无权限。

      if (user.getSeqId() != 1) {
        return "1";
      }
    }*/

    // if(!"".equals(deptMore) && !isDeptPriv(conn,deptMore,user)){
    // //当所选的部门不属于本人管理范围时，提示无权限。

    // return "1";
    // }

    if (!"".equals(deptMore)) { // 部门id有下拉框值和更多部门的值

      deptId = deptId + "," + deptMore;
    }

    // 获取部门的子部门id
    if (',' == deptId.charAt(deptId.length() - 1)) {
      deptId = deptId.substring(0, deptId.length() - 1);
    }

    String deptOrg[] = deptId.split(",");
    for (int b = 0; b < deptOrg.length; b++) {
      deptId += ",";
      deptId += this.getDeptChildId(conn, deptOrg[b]);// //////////
    }

    if (',' == deptId.charAt(deptId.length() - 1)) {

      deptId = deptId.substring(0, deptId.length() - 1);
    }

    if (!"".equals(deptId)) { // 处理空的子23,,24
      String str = "";
      String Org[] = deptId.split(",");
      for (int x = 0; x < Org.length; x++) {
        if (!"".equals(Org[x])) {
          str += Org[x];
          str += ",";
        }
      }
      deptId = str.substring(0, str.length() - 1);

    }

    // 本人管理范围是：指定人员时
    //shenrm 2012-12-13
    //if ("3".equals(deptPriv)) {
    if ("".equals(deptPriv) && !"".equals(user.getUserPriv())) {
     //查询sql优化
	  String sql2 = "SELECT MIN(PRIV_NO) AS PRIV_NO FROM USER_PRIV WHERE SEQ_ID="+user.getUserPriv()+"";
	  rs2 = stmt2.executeQuery(sql2);
	  int priv_no = 0;
	  while(rs2.next()){
		  priv_no = rs2.getInt("PRIV_NO");
	  }
      sql = "select SEQ_ID,USER_PRIV from person where 1=1 and SEQ_ID IN (SELECT SEQ_ID FROM PERSON WHERE USER_PRIV IN(SELECT SEQ_ID FROM USER_PRIV WHERE PRIV_NO IN(SELECT PRIV_NO FROM USER_PRIV WHERE PRIV_NO >="+ priv_no +"))) order by DEPT_ID desc ";
      rs1 = stmt1.executeQuery(sql);
      while (rs1.next()) {
        int seqId = rs1.getInt("SEQ_ID");
        String userPriv = rs1.getString("USER_PRIV");
        if ("0".equals(rolePriv)) {
          if (userPriv.compareTo(user.getUserPriv()) > 0) {
            userIdStr += seqId;
            userIdStr += ",";
          }
        } else if ("1".equals(rolePriv)) {
          if (userPriv.compareTo(user.getUserPriv()) >= 0) {
            userIdStr += seqId;
            userIdStr += ",";
          }
        } else if ("3".equals(rolePriv)) {
          if (!"".equals(priv_id)) {
            String Org[] = priv_id.split(",");
            for (int x = 0; x < Org.length; x++) {
              if (userPriv.equals(Org[x])) {
                userIdStr += seqId;
                userIdStr += ",";
                break;
              }
            }
          }
        } else if ("2".equals(rolePriv) || user.getSeqId() == 1) {
          userIdStr += seqId;
          userIdStr += ",";
        }else{//拼接用户标识 shenrm
        	 userIdStr += seqId;
             userIdStr += ",";
        }
      }

    } else {// 其他范围
      String whereStr = " and dept_id in (" + deptId + ") ";
      if ("-1".equals(deptId)) {
        whereStr = "";
      }
      sql = "select SEQ_ID,USER_PRIV from person where 1=1 " + whereStr
          + " order by DEPT_ID desc ";
      //System.out.print(sql);
      rs1 = stmt1.executeQuery(sql);
      while (rs1.next()) {
        int seqId = rs1.getInt("SEQ_ID");
        String userPriv = rs1.getString("USER_PRIV");
        if ("0".equals(rolePriv)) {
          if (userPriv.compareTo(user.getUserPriv()) > 0) {
            userIdStr += seqId;
            userIdStr += ",";
          }
        } else if ("1".equals(rolePriv)) {
          if (userPriv.compareTo(user.getUserPriv()) >= 0) {
            userIdStr += seqId;
            userIdStr += ",";
          }
        } else if ("3".equals(rolePriv)) {
          if (!"".equals(priv_id)) {
            String Org[] = priv_id.split(",");
            for (int x = 0; x < Org.length; x++) {
              if (userPriv.equals(Org[x])) {
                userIdStr += seqId;
                userIdStr += ",";
                break;
              }
            }
          }
        } else if ("2".equals(rolePriv) || user.getSeqId() == 1) {
          userIdStr += seqId;
          userIdStr += ",";
        }
      }

    }

    if (!"".equals(userIdStr)) {
      userIdStr = userIdStr.substring(0, userIdStr.length() - 1);
    }
  
    if (!"".equals(userIdStr)) {//System.out.println(userIdStr);
      userIdStr=this.getSplitUserIds(userIdStr,minNum,maxNum);
      if (!"".equals(userIdStr)){
         data = this.getDataList(conn, userIdStr, startDate, endDate);
      }
    }
    return data.toString();
  }
  
  
  public String getSplitUserIds(String userIdStr,String minNum,String maxNum){
    if("-1".equals(maxNum)){
      return userIdStr;
      
    }
    String userIds[]=userIdStr.split(",");
    String reUserIdStr="";
    for(int i=Integer.parseInt(minNum);i<Integer.parseInt(maxNum);i++){
      if(i>=userIds.length){
        break;
      }
      reUserIdStr+=userIds[i];
      reUserIdStr+=",";
    }
    if(reUserIdStr.endsWith(",")){
      reUserIdStr=reUserIdStr.substring(0,reUserIdStr.length()-1);
    }
    return reUserIdStr;
  }
  
  /**
   * 获取统计数据
   * 
   * @param conn
   * @param userIdStr
   * @param startDate
   * @param endDate
   * 
   * */
  public StringBuffer getDataList(Connection conn, String userIdStr,
      String startDate, String endDate) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    Statement stmt1 = null;
    ResultSet rs1 = null;
    stmt = conn.createStatement();
    stmt1 = conn.createStatement();
    StringBuffer data = new StringBuffer();
    Map<String,Map<String,String>> map = new HashMap<String,Map<String,String>>();

    startDate += " 00:00:00";
    endDate += " 23:59:59";
    String count = "0";
    String sql = "";
    String user[] = userIdStr.split(",");
    for (int i = 0; i < user.length; i++) {
      Map<String,String> dataMap=new HashMap<String,String>();
      dataMap.put("userName","");
      dataMap.put("userId",user[i]);   //用户Id
      dataMap.put("deptName","");
      dataMap.put("emailIn","0");
      dataMap.put("emailOut","0");
      dataMap.put("calendarFinish","0");
      dataMap.put("calendarAll","0");
      dataMap.put("diary","0");
      dataMap.put("workFlowDeelFinish","0");
      dataMap.put("workFlowDeelAll","0");
      dataMap.put("workFlowSignFinish","0");
      dataMap.put("workFlowSignAll","0");
      dataMap.put("notify","0");
      dataMap.put("news","0");
      
      map.put(user[i],dataMap);
      
      
    }
      Map<String,String> mmap=new HashMap<String,String>();//临时使用map
     
      //获取用户名称和部门名称
      sql = "select SEQ_ID, user_name,dept_id from person where SEQ_ID in ("+userIdStr+")"; 
      rs = stmt.executeQuery(sql);
      while (rs.next()) { 
        int seq=rs.getInt("SEQ_ID");
        mmap=map.get(seq+"");
        mmap.remove("userName");           //用户名称
        String userName = rs.getString("user_name");
        mmap.put("userName",userName);
        int deptId = rs.getInt("dept_id");
        sql = "select dept_name from oa_department where seq_id=" + deptId;
        rs1 = stmt1.executeQuery(sql);
        String deptName="";
        if (rs1.next()) {
          deptName = rs1.getString("dept_name");
          mmap.remove("deptName");
          mmap.put("deptName", deptName); // 部门名称
        }
       
        map.remove(seq+"");
        map.put(seq+"",mmap);
      }
      
      String userIdStrdd=userIdStr.replaceAll(",","','");
             userIdStrdd="'"+userIdStrdd+"'";
      // 内部邮件（收）
      sql = "select oa_email.TO_ID,count(oa_email.TO_ID) from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID and "
          + YHDBUtility.findNoInSet("", "oa_email.TO_ID")
          + " and DELETE_FLAG!='1' and "
          + " oa_email.TO_ID in ("+userIdStrdd+")"
          + " and "
          + YHDBUtility.getDateFilter("send_time", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("send_time", endDate, "<=")
          + " group by oa_email.TO_ID";

      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("emailIn");
        mmap.put("emailIn",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }
      // 内部邮件（发）
      sql = "select FROM_ID,count(FROM_ID) from oa_email_body where "
          + " FROM_ID in("+userIdStrdd+") and "
          + YHDBUtility.getDateFilter("send_time", startDate, ">=") + " and "
          + YHDBUtility.getDateFilter("send_time", endDate, "<=")
          + " and SEND_FLAG='1' group by FROM_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("emailOut");
        mmap.put("emailOut",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }
      // 日程安排（完成）
      sql = "select USER_ID,count(USER_ID) from oa_schedule where CAL_TYPE!='2' and "
          + YHDBUtility.findNoInSet("", "USER_ID")
          + " and "
          + "USER_ID in ("+userIdStrdd+")"
          + " and "
          + YHDBUtility.getDateFilter("cal_time", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("end_time", endDate, "<=")
          + " and OVER_STATUS='1' group by USER_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("calendarFinish");
        mmap.put("calendarFinish",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }
      // 日程安排（所有）

      sql = "select USER_ID,count(USER_ID) from oa_schedule where CAL_TYPE!='2' and "
          + YHDBUtility.findNoInSet("", "USER_ID")
          + " and "
          + "USER_ID in ("+userIdStrdd+")"
          + " and "
          + YHDBUtility.getDateFilter("cal_time", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("end_time", endDate, "<=")
          + " group by USER_ID";

      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("calendarAll");
        mmap.put("calendarAll",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }

      // 工作日志
      sql = "select USER_ID,count(USER_ID) from oa_journal where DIA_TYPE!='2' and "
          + YHDBUtility.findNoInSet("", "USER_ID") + " and "
          + " USER_ID in("+userIdStrdd+") and "
          + YHDBUtility.getDateFilter("DIA_TIME", startDate, ">=") + " and "
          + YHDBUtility.getDateFilter("DIA_TIME", endDate, "<=")
          + " group by USER_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("diary");
        mmap.put("diary",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }

      // 工作流主办（完成）
      sql = "select USER_ID,count(distinct(FLOW_RUN.RUN_ID)) from oa_fl_run as FLOW_RUN,oa_fl_run_prcs as FLOW_RUN_PRCS where FLOW_RUN.DEL_FLAG=0 AND FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID and "
          + "USER_ID in ("+userIdStrdd+")"
          + " and "
          + YHDBUtility.findNoInSet("", "USER_ID")
          + " and PRCS_FLAG=4 and OP_FLAG=1 and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", endDate, "<=")
          + " group by USER_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("workFlowDeelFinish");
        mmap.put("workFlowDeelFinish",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }
      
      // 工作流主办（所有）
      sql = "select USER_ID,count(distinct(FLOW_RUN.RUN_ID)) from oa_fl_run as FLOW_RUN,oa_fl_run_prcs as FLOW_RUN_PRCS where FLOW_RUN.DEL_FLAG=0 AND FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID and "
          + "USER_ID in ("+userIdStrdd+")"
          + " and "
          + YHDBUtility.findNoInSet("", "USER_ID")
          + " and OP_FLAG=1 and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", endDate, "<=")
          + " group by USER_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("workFlowDeelAll");
        mmap.put("workFlowDeelAll",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }

      // 工作流会签（完成）
      sql = "select USER_ID,count(distinct(FLOW_RUN.RUN_ID)) from oa_fl_run as FLOW_RUN,oa_fl_run_prcs as FLOW_RUN_PRCS where FLOW_RUN.DEL_FLAG=0 AND "
          + YHDBUtility.findNoInSet("", "USER_ID")
          + " AND FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID and "
          + " USER_ID in ("+userIdStrdd+")"
          + " and PRCS_FLAG=4 and OP_FLAG=0 and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", endDate, "<=")
          + " group by USER_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("workFlowSignFinish");
        mmap.put("workFlowSignFinish",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }

      // 工作流会签（所有）
      sql = "select USER_ID,count(distinct(FLOW_RUN.RUN_ID)) from oa_fl_run as FLOW_RUN,oa_fl_run_prcs as FLOW_RUN_PRCS where FLOW_RUN.DEL_FLAG=0 AND "
          + YHDBUtility.findNoInSet("", "USER_ID")
          + " AND FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID and "
          + " USER_ID in ("+userIdStrdd+")"
          + " and OP_FLAG=0 and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", endDate, "<=")
          + " group by USER_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("workFlowSignAll");
        mmap.put("workFlowSignAll",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }

      // 公告通知（发）
      sql = "select FROM_ID,count(FROM_ID) from oa_notify where "
          + " FROM_ID in("+userIdStrdd+") and "
          + YHDBUtility.findNoInSet("", "FROM_ID") + " and "
          + YHDBUtility.getDateFilter("SEND_TIME", startDate, ">=") + " and "
          + YHDBUtility.getDateFilter("SEND_TIME", endDate, "<=")
          + " group by FROM_ID";

      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("notyfy");
        mmap.put("notify",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }

      // 新闻（发）
      sql = "select PROVIDER,count(PROVIDER) from oa_news where "
          + " PROVIDER in ("+userIdStrdd+") and "
          + YHDBUtility.findNoInSet("", "PROVIDER") + " and "
          + YHDBUtility.getDateFilter("NEWS_TIME", startDate, ">=") + " and "
          + YHDBUtility.getDateFilter("NEWS_TIME", endDate, "<=")
          + " group by PROVIDER";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("news");
        mmap.put("news",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }



    for(int i=0;i<user.length;i++){
     mmap=map.get(user[i]);
     data.append(YHFOM.toJson(mmap)); 
     data.append(",");
     
    }
  
    data.deleteCharAt(data.length() - 1);

    return data;
  }

  /**
   * 判断deptmore是否在user的管理范围内
   * 
   * @param conn
   * @param deptMore
   * @param user
   * 
   * */
  public boolean isDeptPriv(Connection conn, String deptMore, YHPerson user)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    stmt = conn.createStatement();

    String deptPriv = "";
    String rolePriv = "";
    String dept_id = "";
    String user_id = "";
    String sql = "select * from oa_function_priv where module_id='8' and user_seq_id="
        + user.getSeqId();
    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      deptPriv = rs.getString("dept_priv");
      rolePriv = rs.getString("role_priv");
      dept_id = rs.getString("dept_id");
      user_id = rs.getString("user_id");

      String dept[] = deptMore.split(",");
      if ("0".equals(deptPriv)) {
        if (!this.isNotChildDept(user.getDeptId(), dept, conn)) {
          return true;
        } else {
          return false;
        }
      } else if ("1".equals(deptPriv) || 1 == user.getSeqId()) {
        return true;
      } else if ("2".equals(deptPriv)) {
        String depts[] = dept_id.split(",");
        String flag1 = "0";
        String flag2 = "0";
        for (int i = 0; i < dept.length; i++) {
          String dept1 = dept[i];
          flag1 = "0";
          for (int j = 0; j < depts.length; j++) {
            if (dept1.equals(depts[j])
                || !this.isNotChildDept(Integer.parseInt(dept1), dept, conn)) {
              flag1 = "1";
            }// //
          }
          if (flag1.equals("0")) {
            flag2 = "1";
            return false;
          }

        }
        if (flag2.equals("0")) {
          return true;
        }

      }

    }

    return true;
  }

  /**
   * 获取deptId的子部门
   * 
   * @param conn
   * @param deptId
   * 
   * */
  public String getDeptChildId(Connection conn, String deptId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    stmt = conn.createStatement();
    String sql = "";
    String deptIdStr = "";
    sql = "select SEQ_ID from oa_department where dept_parent=" + deptId;
    rs = stmt.executeQuery(sql);
    while (rs.next()) {
      deptIdStr += rs.getInt(1);
      deptIdStr += ",";
    }
    if (!"".equals(deptIdStr)) {

      String deptOrg[] = deptIdStr.split(",");
      for (int i = 0; i < deptOrg.length; i++) {
        deptIdStr += this.getDeptChildId(conn, deptOrg[i]);
      }
    }
    return deptIdStr;
  }

  /**
   * 导出execl
   * 
   * 
   * */
  public List<Map<String, String>> getDataToExeclLogic(Connection conn,
      YHPerson user, String deptId, String startDate, String endDate,
      String deptMore) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    stmt = conn.createStatement();
    Statement stmt1 = null;
    ResultSet rs1 = null;
    stmt1 = conn.createStatement();
    StringBuffer data = new StringBuffer();
    List<Map<String, String>> dataList = new ArrayList();

    String deptIdStr = "";
    String userIdStr = "";
    deptIdStr = deptId;

    String deptPriv = "";
    String rolePriv = "";
    String dept_id = "";
    String user_id = "";
    String priv_id = "";

    String sql = "select * from oa_function_priv where module_id='8' and user_seq_id="
        + user.getSeqId();

    rs = stmt.executeQuery(sql);
    if (rs.next()) {
      deptPriv = rs.getString("dept_priv");
      rolePriv = rs.getString("role_priv");
      dept_id = rs.getString("dept_id");
      user_id = rs.getString("user_id");
      priv_id = rs.getString("priv_id");
    }
    // if("-1".equals(deptId) && !"1".equals(deptPriv) &&
    // !"3".equals(deptPriv)){//当用户不是全体权限，提示无权限。
    // if(user.getSeqId()!=1){
    // return "1";
    // }
    // }

    // if(!"".equals(deptMore) && !isDeptPriv(conn,deptMore,user)){
    // //当所选的部门不属于本人管理范围时，提示无权限。
    // return "1";
    // }

    if (!"".equals(deptMore) && isDeptPriv(conn, deptMore, user)) { // 部门id有下拉框值和更多部门的值
      deptId = deptId + "," + deptMore;
    }

    // 获取部门的子部门id
    if (',' == deptId.charAt(deptId.length() - 1)) {
      deptId = deptId.substring(0, deptId.length() - 1);
    }

    String deptOrg[] = deptId.split(",");
    for (int b = 0; b < deptOrg.length; b++) {
      deptId += ",";
      deptId += this.getDeptChildId(conn, deptOrg[b]);
    }
    if (',' == deptId.charAt(deptId.length() - 1)) {

      deptId = deptId.substring(0, deptId.length() - 1);
    }

    if (!"".equals(deptId)) { // 处理空的子23,,24
      String str = "";
      String Org[] = deptId.split(",");
      for (int x = 0; x < Org.length; x++) {
        if (!"".equals(Org[x])) {
          str += Org[x];
          str += ",";
        }
      }
      deptId = str.substring(0, str.length() - 1);

    }

    // 本人管理范围是：指定人员时
    if ("3".equals(deptPriv)) {
      sql = "select SEQ_ID,USER_PRIV from person where 1=1 and SEQ_ID in ("
          + user_id + ") order by DEPT_ID desc ";
      rs1 = stmt1.executeQuery(sql);
      while (rs1.next()) {
        int seqId = rs1.getInt("SEQ_ID");
        String userPriv = rs1.getString("USER_PRIV");
        if ("0".equals(rolePriv)) {
          if (userPriv.compareTo(user.getUserPriv()) > 0) {
            userIdStr += seqId;
            userIdStr += ",";
          }
        } else if ("1".equals(rolePriv)) {
          if (userPriv.compareTo(user.getUserPriv()) >= 0) {
            userIdStr += seqId;
            userIdStr += ",";
          }
        } else if ("3".equals(rolePriv)) {
          if (!"".equals(priv_id)) {
            String Org[] = priv_id.split(",");
            for (int x = 0; x < Org.length; x++) {
              if (userPriv.equals(Org[x])) {
                userIdStr += seqId;
                userIdStr += ",";
                break;
              }
            }
          }
        } else if ("2".equals(rolePriv) || user.getSeqId() == 1) {
          userIdStr += seqId;
          userIdStr += ",";
        }
      }

    } else {// 其他范围
      String whereStr = " and dept_id in (" + deptId + ") ";
      if ("-1".equals(deptId)) {
        whereStr = "";
      }
      sql = "select SEQ_ID,USER_PRIV from person where 1=1 " + whereStr
          + " order by DEPT_ID desc ";

      rs1 = stmt1.executeQuery(sql);
      while (rs1.next()) {
        int seqId = rs1.getInt("SEQ_ID");
        String userPriv = rs1.getString("USER_PRIV");
        if ("0".equals(rolePriv)) {
          if (userPriv.compareTo(user.getUserPriv()) > 0) {
            userIdStr += seqId;
            userIdStr += ",";
          }
        } else if ("1".equals(rolePriv)) {
          if (userPriv.compareTo(user.getUserPriv()) >= 0) {
            userIdStr += seqId;
            userIdStr += ",";
          }
        } else if ("3".equals(rolePriv)) {
          if (!"".equals(priv_id)) {
            String Org[] = priv_id.split(",");
            for (int x = 0; x < Org.length; x++) {
              if (userPriv.equals(Org[x])) {
                userIdStr += seqId;
                userIdStr += ",";
                break;
              }
            }
          }
        } else if ("2".equals(rolePriv) || user.getSeqId() == 1) {
          userIdStr += seqId;
          userIdStr += ",";
        }
      }

    }

    if (!"".equals(userIdStr)) {
      userIdStr = userIdStr.substring(0, userIdStr.length() - 1);
    }
    if (!"".equals(userIdStr)) {
      dataList = this.getDataToExcelList(conn, userIdStr, startDate, endDate);
    }

    return dataList;
  }

  /**
   * 根据userIdStr获取dataList
   * */
  public List<Map<String, String>> getDataToExcelList(Connection conn,
      String userIdStr, String startDate, String endDate) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    Statement stmt1 = null;
    ResultSet rs1 = null;
    stmt = conn.createStatement();
    stmt1 = conn.createStatement();

    List<Map<String, String>> dataList = new ArrayList();

    Map<String,Map<String,String>> map = new HashMap<String,Map<String,String>>();

    startDate += " 00:00:00";
    endDate += " 23:59:59";
    String count = "0";
    String sql = "";
    String user[] = userIdStr.split(",");
    for (int i = 0; i < user.length; i++) {
      Map<String,String> dataMap=new HashMap<String,String>();
      dataMap.put("userName","");
      dataMap.put("userId",user[i]);   //用户Id
      dataMap.put("deptName","");
      dataMap.put("emailIn","0");
      dataMap.put("emailOut","0");
      dataMap.put("calendarFinish","0");
      dataMap.put("calendarAll","0");
      dataMap.put("diary","0");
      dataMap.put("workFlowDeelFinish","0");
      dataMap.put("workFlowDeelAll","0");
      dataMap.put("workFlowSignFinish","0");
      dataMap.put("workFlowSignAll","0");
      dataMap.put("notify","0");
      dataMap.put("news","0");
      
      map.put(user[i],dataMap);
      
      
    }
      Map<String,String> mmap=new HashMap<String,String>();//临时使用map
     
      //获取用户名称和部门名称
      sql = "select SEQ_ID, user_name,dept_id from person where SEQ_ID in ("+userIdStr+")"; 
      rs = stmt.executeQuery(sql);
      while (rs.next()) { 
        int seq=rs.getInt("SEQ_ID");
        mmap=map.get(seq+"");
        mmap.remove("userName");           //用户名称
        String userName = rs.getString("user_name");
        mmap.put("userName",userName);
        int deptId = rs.getInt("dept_id");
        sql = "select dept_name from oa_department where seq_id=" + deptId;
        rs1 = stmt1.executeQuery(sql);
        String deptName="";
        if (rs1.next()) {
          deptName = rs1.getString("dept_name");
          mmap.remove("deptName");
          mmap.put("deptName", deptName); // 部门名称
        }
       
        map.remove(seq+"");
        map.put(seq+"",mmap);
      }
      
      String userIdStrdd=userIdStr.replaceAll(",","','");
             userIdStrdd="'"+userIdStrdd+"'";
      // 内部邮件（收）
      sql = "select oa_email.TO_ID,count(oa_email.TO_ID) from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID and "
          + YHDBUtility.findNoInSet("", "oa_email.TO_ID")
          + " and DELETE_FLAG!='1' and "
          + " oa_email.TO_ID in ("+userIdStrdd+")"
          + " and "
          + YHDBUtility.getDateFilter("send_time", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("send_time", endDate, "<=")
          + " group by oa_email.TO_ID";

      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("emailIn");
        mmap.put("emailIn",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }
      // 内部邮件（发）
      sql = "select FROM_ID,count(FROM_ID) from oa_email_body where "
          + " FROM_ID in("+userIdStrdd+") and "
          + YHDBUtility.getDateFilter("send_time", startDate, ">=") + " and "
          + YHDBUtility.getDateFilter("send_time", endDate, "<=")
          + " and SEND_FLAG='1' group by FROM_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("emailOut");
        mmap.put("emailOut",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }
      // 日程安排（完成）
      sql = "select USER_ID,count(USER_ID) from oa_schedule where CAL_TYPE!='2' and "
          + YHDBUtility.findNoInSet("", "USER_ID")
          + " and "
          + "USER_ID in ("+userIdStrdd+")"
          + " and "
          + YHDBUtility.getDateFilter("cal_time", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("end_time", endDate, "<=")
          + " and OVER_STATUS='1' group by USER_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("calendarFinish");
        mmap.put("calendarFinish",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }
      // 日程安排（所有）

      sql = "select USER_ID,count(USER_ID) from oa_schedule where CAL_TYPE!='2' and "
          + YHDBUtility.findNoInSet("", "USER_ID")
          + " and "
          + "USER_ID in ("+userIdStrdd+")"
          + " and "
          + YHDBUtility.getDateFilter("cal_time", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("end_time", endDate, "<=")
          + " group by USER_ID";

      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("calendarAll");
        mmap.put("calendarAll",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }

      // 工作日志
      sql = "select USER_ID,count(USER_ID) from oa_journal where DIA_TYPE!='2' and "
          + YHDBUtility.findNoInSet("", "USER_ID") + " and "
          + " USER_ID in("+userIdStrdd+") and "
          + YHDBUtility.getDateFilter("DIA_TIME", startDate, ">=") + " and "
          + YHDBUtility.getDateFilter("DIA_TIME", endDate, "<=")
          + " group by USER_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("diary");
        mmap.put("diary",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }

      // 工作流主办（完成）
      sql = "select USER_ID,count(distinct(FLOW_RUN.RUN_ID)) from oa_fl_run as FLOW_RUN,oa_fl_run_prcs as FLOW_RUN_PRCS where FLOW_RUN.DEL_FLAG=0 AND FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID and "
          + "USER_ID in ("+userIdStrdd+")"
          + " and "
          + YHDBUtility.findNoInSet("", "USER_ID")
          + " and PRCS_FLAG=4 and OP_FLAG=1 and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", endDate, "<=")
          + " group by USER_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("workFlowDeelFinish");
        mmap.put("workFlowDeelFinish",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }
      
      // 工作流主办（所有）
      sql = "select USER_ID,count(distinct(FLOW_RUN.RUN_ID)) from oa_fl_run as FLOW_RUN,oa_fl_run_prcs as FLOW_RUN_PRCS where FLOW_RUN.DEL_FLAG=0 AND FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID and "
          + "USER_ID in ("+userIdStrdd+")"
          + " and "
          + YHDBUtility.findNoInSet("", "USER_ID")
          + " and OP_FLAG=1 and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", endDate, "<=")
          + " group by USER_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("workFlowDeelAll");
        mmap.put("workFlowDeelAll",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }

      // 工作流会签（完成）
      sql = "select USER_ID,count(distinct(FLOW_RUN.RUN_ID)) from oa_fl_run as FLOW_RUN,oa_fl_run_prcs as FLOW_RUN_PRCS where FLOW_RUN.DEL_FLAG=0 AND "
          + YHDBUtility.findNoInSet("", "USER_ID")
          + " AND FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID and "
          + " USER_ID in ("+userIdStrdd+")"
          + " and PRCS_FLAG=4 and OP_FLAG=0 and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", endDate, "<=")
          + " group by USER_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("workFlowSignFinish");
        mmap.put("workFlowSignFinish",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }

      // 工作流会签（所有）
      sql = "select USER_ID,count(distinct(FLOW_RUN.RUN_ID)) from oa_fl_run as FLOW_RUN,oa_fl_run_prcs as FLOW_RUN_PRCS where FLOW_RUN.DEL_FLAG=0 AND "
          + YHDBUtility.findNoInSet("", "USER_ID")
          + " AND FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID and "
          + " USER_ID in ("+userIdStrdd+")"
          + " and OP_FLAG=0 and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", endDate, "<=")
          + " group by USER_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("workFlowSignAll");
        mmap.put("workFlowSignAll",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }

      // 公告通知（发）
      sql = "select FROM_ID,count(FROM_ID) from oa_notify where "
          + " FROM_ID in("+userIdStrdd+") and "
          + YHDBUtility.findNoInSet("", "FROM_ID") + " and "
          + YHDBUtility.getDateFilter("SEND_TIME", startDate, ">=") + " and "
          + YHDBUtility.getDateFilter("SEND_TIME", endDate, "<=")
          + " group by FROM_ID";

      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("notyfy");
        mmap.put("notify",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }

      // 新闻（发）
      sql = "select PROVIDER,count(PROVIDER) from oa_news where "
          + " PROVIDER in ("+userIdStrdd+") and "
          + YHDBUtility.findNoInSet("", "PROVIDER") + " and "
          + YHDBUtility.getDateFilter("NEWS_TIME", startDate, ">=") + " and "
          + YHDBUtility.getDateFilter("NEWS_TIME", endDate, "<=")
          + " group by PROVIDER";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String userId=rs.getString(1);
        mmap=map.get(userId);
        mmap.remove("news");
        mmap.put("news",rs.getString(2));
        map.remove(userId);
        map.put(userId,mmap);
      }
    for(int i=0;i<user.length;i++){
      mmap=map.get(user[i]);
      dataList.add(mmap);
    }
    return dataList;
  }

  /**
   * 工具方法，把集合转化为
   * 
   * 
   * @param list
   * @return
   */
  public ArrayList<YHDbRecord> convertList(List<Map<String, String>> list) {
    ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
    if (list != null && list.size() > 0) {
      for (int i = 0; i < list.size(); i++) {
        YHDbRecord dbrec = new YHDbRecord();
        dbrec.addField("部门", list.get(i).get("deptName"));
        dbrec.addField("姓名", list.get(i).get("userName"));
        dbrec.addField("内部邮件(收)", list.get(i).get("emailIn"));
        dbrec.addField("内部邮件(发)", list.get(i).get("emailOut"));
        dbrec.addField("日程安排(完成)", list.get(i).get("calendarFinish"));
        dbrec.addField("日程安排(所有)", list.get(i).get("calendarAll"));
        dbrec.addField("工作日志", list.get(i).get("diary"));
        dbrec.addField("工作流主办(完成)", list.get(i).get("workFlowDeelFinish"));
        dbrec.addField("工作流主办(所有)", list.get(i).get("workFlowDeelAll"));
        dbrec.addField("工作流会签(完成)", list.get(i).get("workFlowSignFinish"));
        dbrec.addField("工作流会签(所有)", list.get(i).get("workFlowSignAll"));
        dbrec.addField("公告通知(发)", list.get(i).get("notify"));
        dbrec.addField("新闻(发)", list.get(i).get("workFlowSignAll"));

        dbL.add(dbrec);
      }
    }
    return dbL;
  }

  /**
   * 日程安排   通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getCalFinishLogic(Connection dbConn, Map<String, String> map)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    stmt = dbConn.createStatement();
    StringBuffer data = new StringBuffer();

    String userId = map.get("userId");
    String startDate = map.get("startDate");
    String endDate = map.get("endDate");
    String status = map.get("status");
    startDate = startDate + " 00:00:00";
    endDate = endDate + " 23:59:59";

    try {
      String sql = " select * from oa_schedule where CAL_TYPE!='2' ";
      sql += " and " + YHDBUtility.findInSet(userId, "USER_ID");
      sql += " and " + YHDBUtility.getDateFilter("CAL_TIME", startDate, ">=");
      sql += " and " + YHDBUtility.getDateFilter("END_TIME", endDate, "<=");
      if (status.equals("1")) {
        sql += " and OVER_STATUS='1' ";
      }

      sql += " order by SEQ_ID  ";

      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        data.append("{");
        data.append("startDate:'" + rs.getString("cal_time") + "',");
        data.append("endDate:'" + rs.getString("end_time") + "',");
        data.append("content:'" + YHUtility.encodeSpecial(rs.getString("content")) + "'");
        data.append("},");

      }

      data.deleteCharAt(data.length() - 1);
      return data.toString();

    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * 日志分页列表
   * 
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String toSearchData(Connection conn, Map request, int userId)
      throws Exception {
    String sql = "select SEQ_ID,DIA_TIME,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID from oa_journal where 1=1 ";
    // String filters = toSearchWhere(request,userId);
    String filters = "";
    String startDateStr = request.get("startDate") != null ? ((String[]) request
        .get("startDate"))[0]
        : null;
    String endDateStr = request.get("endDate") != null ? ((String[]) request
        .get("endDate"))[0] : null;

    filters = " and USER_ID='" + userId + "' and DIA_TYPE!='2' and "
        + YHDBUtility.getDateFilter("DIA_DATE", startDateStr, ">=") + " and "
        + YHDBUtility.getDateFilter("DIA_DATE", endDateStr, "<=");
    String query = " order by DIA_DATE desc,DIA_TIME DESC ";
    if (!"".equals(filters)) {
      sql += filters;
    }
    sql += query;
    // System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,
        sql);

    return pageDataList.toJson();
  }

  /**
   * 工作流 通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getWorkFlowLogic(Connection dbConn, Map request,
      Map<String, String> map) throws Exception {
    String userId = map.get("userId");
    String startDate = map.get("startDate");
    String str = map.get("flag");
    String endDate = map.get("endDate");
    startDate += " 00:00:00 ";
    endDate += " 23:59:59 ";
    try {
      String sql = " select distinct FLOW_RUN.SEQ_ID,FLOW_RUN.RUN_ID,FLOW_RUN.FLOW_ID,FLOW_RUN.RUN_NAME,FLOW_RUN.BEGIN_USER "
          + " from oa_fl_run as FLOW_RUN,FLOW_RUN_PRCS "
          + " where FLOW_RUN.DEL_FLAG=0 AND FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID and "
          + YHDBUtility.findInSet(userId, "USER_ID")
          + " and OP_FLAG=1 "
          + " and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", endDate, "<=");

      if ("1".equals(str.toString())) {
        sql += " and PRCS_FLAG=4 ";
      }

      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
          queryParam, sql);

      return pageDataList.toJson();

    } catch (Exception e) {
      throw e;
    }
  }

  public String getFlowNameLogic(Connection dbConn, String flowId)
      throws Exception {

    Statement stmt = null;
    ResultSet rs = null;
    stmt = dbConn.createStatement();
    try {
      String flowName = "";
      String sql = "select FLOW_NAME  from oa_fl_type where SEQ_ID=" + flowId;
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        flowName = rs.getString("flow_name");
      }

      return flowName;
    } catch (Exception e) {
      throw e;
    }

  }

  /**
   * 工作流会签 通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getFlowSignLogic(Connection dbConn, Map request,
      Map<String, String> map) throws Exception {
    String userId = map.get("userId");
    String startDate = map.get("startDate");
    String str = map.get("flag");
    String endDate = map.get("endDate");
    startDate += " 00:00:00 ";
    endDate += " 23:59:59 ";
    try {
      String sql = " select distinct FLOW_RUN.SEQ_ID,FLOW_RUN.RUN_ID,FLOW_RUN.FLOW_ID,FLOW_RUN.RUN_NAME,FLOW_RUN.BEGIN_USER "
          + " from oa_fl_run as FLOW_RUN,oa_fl_run_prcs as FLOW_RUN_PRCS "
          + " where FLOW_RUN.DEL_FLAG=0 AND FLOW_RUN.RUN_ID=FLOW_RUN_PRCS.RUN_ID and "
          + YHDBUtility.findInSet(userId, "USER_ID")
          + " and OP_FLAG=0 "
          + " and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", startDate, ">=")
          + " and "
          + YHDBUtility.getDateFilter("FLOW_RUN.BEGIN_TIME", endDate, "<=");

      if ("1".equals(str.toString())) {
        sql += " and PRCS_FLAG=4 ";
      }

      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
          queryParam, sql);

      return pageDataList.toJson();

    } catch (Exception e) {
      throw e;
    }
  }

}
