package yh.core.funcs.doc.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.doc.data.YHDocFlowFormType;
import yh.core.funcs.doc.data.YHDocFlowProcess;
import yh.core.funcs.doc.data.YHDocFlowRunFeedback;
import yh.core.funcs.doc.data.YHDocFlowSort;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.data.YHDocRun;
import yh.core.funcs.doc.receive.logic.YHDocRegisterLogic;
import yh.core.funcs.doc.send.data.YHDocFlowRun;
import yh.core.funcs.doc.send.logic.YHDocLogic;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.doc.util.sort.YHFlowTypeComparator;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHFlowTypeLogic {
  public ArrayList<YHDocFlowSort> getFlowSortList(Connection conn) throws Exception{
    YHORM orm = new YHORM();
    HashMap map = null;
    HashMap flowSortMap = null;
    ArrayList<YHDocFlowSort> flowSortList = flowSortList = new ArrayList<YHDocFlowSort>();
         
    List sortList = new ArrayList();
    sortList.add("flowSort");
    String[] filters = new String[]{"SORT_PARENT = 0 order by SORT_NO"};
    map = (HashMap)orm.loadDataSingle(conn, sortList, filters);
    List<HashMap> list = (List<HashMap>) map.get("FLOW_SORT");
    
    for(int i = 0; i < list.size(); i++) {
      HashMap mapList = list.get(i);
      YHDocFlowSort sort = null;
      if(mapList.get("haveChild").equals("1")) {
        sort = new YHDocFlowSort();
        sort.setSeqId((Integer)mapList.get("seqId"));
        sort.setSortNo((Integer)mapList.get("sortNo"));
        sort.setSortName((String)mapList.get("sortName"));
        sort.setSortParent((Integer)mapList.get("sortParent"));
        sort.setHaveChild((String)mapList.get("haveChild"));
        sort.setDeptId((Integer)mapList.get("deptId"));
        flowSortList.add(sort);
               
        int seqId = (Integer)mapList.get("seqId");
        String[] rules = new String[]{"SORT_PARENT = " + seqId + " order by SORT_NO"};
        
        List sortListNext = new ArrayList();
        sortListNext.add("flowSort");
        
        flowSortMap = (HashMap)orm.loadDataSingle(conn, sortListNext, rules);
        List<HashMap> sortMapList = (List<HashMap>) flowSortMap.get("FLOW_SORT");
        for(int j = 0; j < sortMapList.size(); j++) {
          HashMap mapListNext = sortMapList.get(j);
          sort = new YHDocFlowSort();
          sort.setSeqId((Integer)mapListNext.get("seqId"));
          sort.setSortNo((Integer)mapListNext.get("sortNo"));
          sort.setSortName((String)mapListNext.get("sortName"));
          sort.setSortParent((Integer)mapListNext.get("sortParent"));
          sort.setHaveChild((String)mapListNext.get("haveChild"));
          sort.setDeptId((Integer)mapListNext.get("deptId"));
          flowSortList.add(sort);
        }         
      }else {
        sort = new YHDocFlowSort();
        sort.setSeqId((Integer)mapList.get("seqId"));
        sort.setSortNo((Integer)mapList.get("sortNo"));
        sort.setSortName((String)mapList.get("sortName"));
        sort.setSortParent((Integer)mapList.get("sortParent"));
        sort.setHaveChild((String)mapList.get("haveChild"));
        sort.setDeptId((Integer)mapList.get("deptId"));
        flowSortList.add(sort);
      }
    }
    return  flowSortList;
  }
  
  public ArrayList<YHDocFlowFormType> getFlowFormTypeList(Connection conn) throws Exception{
    YHORM orm = new YHORM();
    ArrayList<YHDocFlowFormType> typeList = null;
    Map m = null;
    typeList = (ArrayList<YHDocFlowFormType>)orm.loadListSingle(conn, YHDocFlowFormType.class, m);
    return typeList;
  }
  
  public void saveFlowType(Connection conn, Map map) throws Exception{
    YHORM orm = new YHORM();
    orm.saveSingle(conn, "flowType", map);
  }
  public YHDocFlowType getFlowTypeById(int flowTypeId , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    YHDocFlowType ft = (YHDocFlowType) orm.loadObjSingle(conn, YHDocFlowType.class, flowTypeId);
    return ft;
  }
  public List<YHDocFlowType> getFlowTypeList(Connection conn) throws Exception{
    Map m = null;
    YHORM orm = new YHORM();
    List<YHDocFlowType> list = orm.loadListSingle(conn, YHDocFlowType.class, m);
    return list;
  }
  public List<Map> getFlowTypeListByType(Connection conn , int type ) throws Exception{
    String query  = "select SEQ_ID , flow_name from  "+ YHWorkFlowConst.FLOW_TYPE +" WHERE FLOW_TYPE = '"+ type +"' order by flow_no";
    Statement stm = null;
    ResultSet rs = null;
    List<Map> list = new ArrayList();
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        String flowName = rs.getString("flow_name");
        Map ft = new HashMap();
        ft.put("seqId", seqId);
        ft.put("flowName", flowName);
        list.add(ft);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return list;
  }
  
  public List<YHDocFlowType> getFlowTypeList(int sortId , Connection conn) throws Exception{
    Map map = new HashMap();
    map.put("FLOW_SORT", sortId);
    YHORM orm = new YHORM();
    List<YHDocFlowType> list = orm.loadListSingle(conn, YHDocFlowType.class, map);
    Collections.sort(list,new YHFlowTypeComparator());
    return list;
  }
  public List<YHDocFlowType> getFlowTypeList(String sortId , Connection conn) throws Exception {
    List<YHDocFlowType> list =  new ArrayList();
    sortId = YHWorkFlowUtility.getOutOfTail(sortId);
    String tmp = " flow_SORT in (" + sortId + ") ";
    if (sortId.indexOf(",") == -1) {
      tmp = " flow_sort = " + sortId;
    } 
    String query  = "select SEQ_ID ,FLOW_NO, flow_name , flow_Type , NEW_USER ,query_User,query_User_Dept,manage_User,manage_User_Dept from "+ YHWorkFlowConst.FLOW_TYPE +" WHERE "+ tmp +" order by flow_no";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        YHDocFlowType ft = new YHDocFlowType();
        int seqId = rs.getInt("SEQ_ID");
        String flowName = rs.getString("flow_name");
        String flowType = rs.getString("flow_Type");
        String newUser = rs.getString("NEW_USER");
        String queryUser = rs.getString("query_User");
        String queryUserDept = rs.getString("query_User_Dept");
        String manageUser = rs.getString("manage_User");
        String manageUserDept = rs.getString("manage_User_Dept");
        ft.setFlowType(flowType);
        ft.setFlowName(flowName);
        ft.setSeqId(seqId);
        ft.setNewUser(newUser);
        ft.setQueryUser(queryUser);
        ft.setQueryUserDept(queryUserDept);
        ft.setManageUser(manageUser);
        ft.setManageUserDept(manageUserDept);
        list.add(ft);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return list;
  }
  public List<YHDocFlowType> getFlowTypeListByDeptId(int sortId , YHPerson user , Connection conn) throws Exception{
    Map map = new HashMap();
    map.put("FLOW_SORT", sortId);
    YHORM orm = new YHORM();
    List<YHDocFlowType> list = orm.loadListSingle(conn, YHDocFlowType.class, map);
    Collections.sort(list,new YHFlowTypeComparator());
    return list;
  }
  public void saveFlowType(YHDocFlowType ft , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    orm.saveSingle(conn, ft);
  }
  public void updateFlowType(YHDocFlowType ft , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    orm.updateSingle(conn, ft);
  }
  public void delFlowType(YHDocFlowType ft , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    this.empty(ft.getSeqId(), conn);
    //删除委托规则
    //this.deleteTable("FLOW_RULE", "FLOW_ID=" + , conn)
    //要删 除其它表
    YHFlowProcessLogic fp = new YHFlowProcessLogic();
    fp.delFlowProcessByFlowId(ft.getSeqId() , conn);
    orm.deleteSingle(conn, ft);
  }
  public int getWorkCountByFlowId(int flowId , Connection conn) throws Exception{
    String query = "SELECT COUNT(*) count FROM "+ YHWorkFlowConst.FLOW_RUN +" WHERE FLOW_ID = " + flowId;
    int count = this.getIntBySeq(query, conn) ;
    return count ;
  }
  public int getIntBySeq(String sql , Connection conn) throws Exception{
    int count = 0 ;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(sql);
      if(rs.next()){
        count = rs.getInt(1);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return count ;
  }
  public int getDelWorkCountByFlowId(int flowId , Connection conn) throws Exception{
    String query = "SELECT COUNT(*) count FROM "+ YHWorkFlowConst.FLOW_RUN +" WHERE FLOW_ID = " + flowId +" and DEL_FLAG ='1'" ;
    int count = this.getIntBySeq(query, conn) ;
    return count ;
  }
  public String getNewPriv(int flowId , Connection conn) throws Exception {
    String query = "select "
      + " NEW_USER"
      + " FROM "+ YHWorkFlowConst.FLOW_TYPE +" WHERE"
      + " SEQ_ID=" + flowId;
    String newUser = "||";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next() 
          && rs.getString("NEW_USER") != null) {
        newUser = rs.getString("NEW_USER");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    
    String[] priv = newUser.split("\\|");
    
    String userId = "";
    String dept = "";
    String role = "";
    if (priv.length > 0) {
      userId = priv[0] ;
    }
    if (priv.length > 1) {
      dept = priv[1] ;
    }
    if (priv.length > 2) {
      role = priv[2] ;
    }
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    sb.append("userId:'" + userId + "'");
    sb.append(",dept:'" + dept + "'");
    sb.append(",role:'" + role + "'");
    String deptName = "";
    if ("0".equals(dept)) {
      deptName = "全体部门";
    } else {
      YHDeptLogic deptLogic = new YHDeptLogic();
      deptName = deptLogic.getNameByIdStr(dept, conn);
    }
    YHPersonLogic personLogic = new YHPersonLogic();
    
    YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
    sb.append(",userDesc:'" + personLogic.getNameBySeqIdStr( userId, conn) + "'");
    sb.append(",deptDesc:'" +  deptName + "'");
    sb.append(",roleDesc:'" + userPrivLogic.getNameByIdStr(role , conn) + "'");
    sb.append("}");
    return sb.toString();
  }
  public void updateNewPriv(int flowId , String userId , String dept , String role , Connection conn ) throws Exception {
    String newPriv = userId + "|" + dept + "|" + role;
    String query = "update "+ YHWorkFlowConst.FLOW_TYPE +" set new_user='" + newPriv + "' where seq_id=" + flowId; 
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate(query);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, null, null); 
    }
  }
 
  public void empty(int flowId , Connection conn) throws Exception {
    YHORM orm = new YHORM();
    Map map = new HashMap();
    map.put("FLOW_ID", flowId);
    List<YHDocRun> flowRunList = orm.loadListSingle(conn, YHDocRun.class, map);
    YHAttachmentLogic logic = new YHAttachmentLogic();
    //删除流程实例 
    for (YHDocRun tmp : flowRunList) {
      String attachmentId = tmp.getAttachmentId();
      String attachmentName = tmp.getAttachmentName();
      logic.deleteAttachments(attachmentId, attachmentName);
       //删除会签意见
      Map mapFeedback = new HashMap();
      mapFeedback.put("RUN_ID", tmp.getRunId());
      List<YHDocFlowRunFeedback> feedbackList = orm.loadListSingle(conn, YHDocFlowRunFeedback.class, mapFeedback);
      for (YHDocFlowRunFeedback tmpF : feedbackList) {
        String attachmentId2 = tmp.getAttachmentId();
        String attachmentName2 = tmp.getAttachmentName();
        logic.deleteAttachments(attachmentId2, attachmentName2);
        orm.deleteSingle(conn, tmpF);
      }
      List<YHDocFlowRun> list = orm.loadListSingle(conn, YHDocFlowRun.class, mapFeedback);
      for (YHDocFlowRun tmpF : list) {
        String attachmentId2 = tmp.getAttachmentId();
        String attachmentName2 = tmp.getAttachmentName();
        logic.deleteAttachments(attachmentId2, attachmentName2);
        orm.deleteSingle(conn, tmpF);
      }
       //删除流程数据
      YHWorkFlowUtility.deleteTable(YHWorkFlowConst.FLOW_RUN_DATA, "RUN_ID=" + tmp.getRunId(), conn);
      //删除流程步骤
      YHWorkFlowUtility.deleteTable(YHWorkFlowConst.FLOW_RUN_PRCS, "RUN_ID=" + tmp.getRunId(), conn);
    //删除流程日志
      YHWorkFlowUtility.deleteTable(YHWorkFlowConst.FLOW_RUN_LOG, "RUN_ID=" + tmp.getRunId(), conn);
      YHDocLogic doc = new YHDocLogic();
      doc.delDoc(tmp.getRunId(), conn);
      //清空收文流程相关
      YHDocRegisterLogic rec = new YHDocRegisterLogic();
      rec.delRun(conn, tmp.getRunId());
      orm.deleteSingle(conn, tmp);
    }
  }
  
  public static void main(String[] args) throws Exception {
//    Connection dbConn = null;
//    dbConn = TestDbUtil.getConnection(false, "TD_OA");
//    String user = "jw";
//    YHFlowTypeLogic f = new YHFlowTypeLogic();
//    String value = f.getUserSeqId(user, dbConn);
//    //System.out.println(value);
  }
 /**
  * 校验
  * @param flowId
  * @param conn
  * @return [{isError:true , id:'prcsUserCheck',desc:['ddddd','ddddd','ddddd']}
  ,{isError:true , id:'prcsToCheck',desc:['ddddd','ddddd','ddddd']}
  ,{isError:false , id:'writableFieldCheck'}
  ,{isError:true , id:'condFormulaCheck',desc:['ddddd','ddddd','ddddd']}]
 * @throws Exception 
  */
  public String checkFlowType(int flowId , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    sb.append(this.checkPrcsUser(flowId, conn) + ",");
    
    sb.append("]");
    return sb.toString();
  }
  /**
   * 校验经办人
   * @param flowId
   * @param conn
   * @return
   * @throws Exception 
   */
  public String checkPrcsUser(int flowId , Connection conn) throws Exception {
    StringBuffer sb = new StringBuffer();
    String noPrcsUserStr = "";
    String query = "select PRCS_ID " 
      + " , PRCS_NAME " 
      + "  from "+ YHWorkFlowConst.FLOW_PROCESS +" where  " 
      + " FLOW_SEQ_ID= " + flowId
      + " and (PRCS_USER='' or PRCS_USER = NULL ) " 
      + " and (PRCS_PRIV=''  or PRCS_PRIV = NULL )" 
      + " and (PRCS_DEPT=''  or PRCS_DEPT = NULL ) " 
      + " order by PRCS_ID";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()){
        int prcsId = rs.getInt("PRCS_ID");
        String prcsName = rs.getString("PRCS_NAME");
        noPrcsUserStr += "第" + prcsId + "步[" + prcsName +"],";
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    sb.append("{");
    sb.append("id:'prcsUserCheck'");
    if ("".equals(noPrcsUserStr)) {
      sb.append(",isError:false");
    } else {
      sb.append(",isError:true");
      sb.append(",desc:'" + noPrcsUserStr + "'");
    }
    sb.append("}");
    return sb.toString();
  }
  /**
   * 检查流程的每个步骤，查看下一步指定方面是否存在问题
   * @param flowId
   * @param conn
   * @throws Exception
   */
  public String checkNodePrcsTo(int flowId , Connection conn) throws Exception {
    StringBuffer sb = new StringBuffer();
  
    sb.append("}");
    return sb.toString();
  }
  public String checkWritableField(int flowId , Connection conn) throws Exception {
    StringBuffer sb = new StringBuffer();
    
    sb.append("}");
    return sb.toString();
  }
  public String getCloneMsg (int flowId , Connection conn) throws Exception {
    StringBuffer sb = new StringBuffer();
    String query = "select "
      + " FLOW_NO"
      + ",FLOW_NAME"
      + " from "+ YHWorkFlowConst.FLOW_TYPE +" where" 
      + " SEQ_ID=" + flowId ;
    
    String flowName = "";
    String flowNo = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()){
        flowName = rs.getString("FLOW_NAME");
        flowNo = rs.getString("FLOW_NO");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    sb.append("{");
    sb.append("flowName:'" + flowName + "'");
    sb.append(",flowNo:'" + flowNo + "'");
    sb.append("}");
    return sb.toString();
  }
  public void clone (int flowId , String flowName , String sFlowNo , Connection conn) throws Exception {
    YHORM orm = new YHORM();
    YHDocFlowType flowType = (YHDocFlowType) orm.loadObjSingle(conn, YHDocFlowType.class, flowId);
    flowType.setSeqId(0);
    flowType.setFlowName(flowName);
    int flowNo = 0 ;
    if (sFlowNo != null && !"".equals(sFlowNo)) {
      flowNo = Integer.parseInt(sFlowNo);
    }
    flowType.setFlowNo(flowNo);
    orm.saveSingle(conn, flowType);
    
    if ("1".equals(flowType.getFlowType())) {
      int flowSeqId = 0;
      String query = "select max(SEQ_ID) as max from "+ YHWorkFlowConst.FLOW_TYPE;
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = conn.createStatement();
        rs = stm.executeQuery(query);
        if (rs.next()){
          flowSeqId = rs.getInt("max");
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null); 
      }
      YHFlowProcessLogic logic = new YHFlowProcessLogic();
      List<YHDocFlowProcess> list = logic.getFlowProcessByFlowId(flowId, conn);
      for (YHDocFlowProcess fp : list) {
        fp.setSeqId(0);
        fp.setFlowSeqId(flowSeqId);
        orm.saveSingle(conn, fp);
      }
    }
  }
  /**
   * 取得对应formId的表单名
   * @param formId
   * @param conn
   * @return
   * @throws Exception
   */
  public String getFormName(int formId , Connection conn) throws Exception {
    String formName = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      String query = "select FORM_NAME FROM "+ YHWorkFlowConst.FLOW_FORM_TYPE +" WHERE SEQ_ID=" + formId;
      rs = stm.executeQuery(query);
      if (rs.next()){
        formName = rs.getString("FORM_NAME");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return formName;
  }
  /**
   * 取得对应FlowType的名
   * @param formId
   * @param conn
   * @return
   * @throws Exception
   */
  public String getFlowTypeName(int flowId , Connection conn) throws Exception {
    String flowName = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      String query = "select FLOW_NAME FROM "+ YHWorkFlowConst.FLOW_TYPE +" WHERE SEQ_ID=" + flowId;
      rs = stm.executeQuery(query);
      if (rs.next()){
        flowName = rs.getString("FLOW_NAME");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return flowName;
  }
  /**
   * 取得流程相关信息，返回一个xml形式的字符串
   * @param flowId
   * @param conn
   * @return
   * @throws Exception
   */
  public String getFlowTypeMsg(int flowId , Connection conn) throws Exception {
    StringBuffer sb = new StringBuffer();
    String query = "select * from "+ YHWorkFlowConst.FLOW_TYPE +" where SEQ_ID=" + flowId;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()){
        sb.append("<BaseInfo>\r\n");
        ResultSetMetaData rsmd =  rs.getMetaData();
        for (int i = 1 ; i <= rsmd.getColumnCount() ; i ++ ) {
          String value = rs.getString(i);
          if (value == null) {
            value = ""; 
          }
          String colName = rsmd.getColumnName(i);
          if ("SEQ_ID".equals(colName)) {
            colName = "FLOW_ID";
          }
          if ("FORM_SEQ_ID".equals(colName)) {
            colName = "FORM_ID";
          }
          sb.append("<");
          sb.append(colName);
          sb.append(">");
          sb.append("<![CDATA[");
          sb.append(value);
          sb.append("]]></");
          sb.append(colName);
          sb.append(">\r\n");
        }
        sb.append("</BaseInfo>\r\n");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return sb.toString();
  }
  /**
   * 取得相关步骤的信息
   * @param flowId
   * @param conn
   * @return
   * @throws Exception
   */
  public String getFlowProcMsg(int flowId , Connection conn) throws Exception {
    StringBuffer sb = new StringBuffer();
    String query = "select * from "+ YHWorkFlowConst.FLOW_PROCESS +" where FLOW_SEQ_ID=" + flowId + " order by PRCS_ID";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()){
        sb.append("<Process>\r\n");
        ResultSetMetaData rsmd =  rs.getMetaData();
        for (int i = 1 ; i <= rsmd.getColumnCount() ; i ++ ) {
          String value = rs.getString(i);
          if (value == null) {
            value = ""; 
          }
          String colName = rsmd.getColumnName(i);
          if ("SEQ_ID".equals(colName)) {
            colName = "ID";
          }
          if ("FLOW_SEQ_ID".equals(colName)) {
            colName = "FLOW_ID";
          }
          sb.append("<");
          sb.append(colName);
          sb.append(">");
          sb.append("<![CDATA[");
          sb.append(value);
          sb.append("]]></");
          sb.append(colName);
          sb.append(">\r\n");
        }
        sb.append("</Process>\r\n");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return sb.toString();
  }
  public void importFlow(Element root , int flowId , boolean isUserOn , Connection conn) throws Exception {
    Element flowMsg = root.element("BaseInfo");
    String userStr = "MANAGE_USER,QUERY_USER,NEW_USER,MANAGE_USER_DEPT,QUERY_USER_DEPT,EDIT_PRIV,DEPT_ID,PRCS_USER,PRCS_DEPT,PRCS_PRIV,AUTO_USER_OP,AUTO_USER,MAIL_TO";
    String useUserStr = "PRCS_USER,AUTO_USER_OP,AUTO_USER,MAIL_TO";
    String privStr = "MANAGE_USER,QUERY_USER,NEW_USER,MANAGE_USER_DEPT,QUERY_USER_DEPT,EDIT_PRIV";
    List<Element> iterator = flowMsg.elements();
    String query = "update oa_fl_type set ";
    for (Element el : iterator){
      String name = el.getName();
      if (name.equals("FLOW_ID") 
          || name.equals("FLOW_NAME")
          || name.equals("FLOW_SORT")
          || name.equals("FORM_ID")
          || name.equals("FORCE_PRE_SET")
          || name.equals("ATTACHMENT_ID")
          || name.equals("VIEW_USER")
          || name.equals("ATTACHMENT_NAME")
      ) {
        continue; 
      }
      if (!isUserOn && YHWorkFlowUtility.findId(userStr, name)) {
        continue;
      }
      if (el.getText()== null || "".equals(el.getText().trim())) {
        query += " " + el.getName() + "=null,";
      } else {
        String value = el.getText();
        if (isUserOn && YHWorkFlowUtility.findId(privStr, name)) {
          value = this.getUserSeqId2(value, conn);
        }
        query += " " + el.getName() + "='" + value + "',";
      }
    }
    query = query.substring(0, query.length() - 1);
    query += " where SEQ_ID =" + flowId;
    
    this.upateBySql(query, conn);
    query = "delete from "+ YHWorkFlowConst.FLOW_PROCESS +" where FLOW_SEQ_ID=" + flowId ;
    this.upateBySql(query, conn);
    
    List<Element> rowList = root.elements("Process");
    for (Element node : rowList) {
      List<Element> child = node.elements();
      String nameStr = "";
      String valueStr = "";
      for (Element el : child){
        String name = el.getName();
        String value = el.getText();
        if (name.equals("TIME_OUT_MODIFY")
            || name.equals("TIME_OUT_ATTEND")
            || name.equals("RELATION_IN") 
            || name.equals("RELATION_OUT")
        ) {
          continue;
        }
        if (!isUserOn && YHWorkFlowUtility.findId(userStr, name)) {
          continue;
        }
        if ("ATTACH_EDIT_PRIV".equals(name)) {
          continue;
        }
        if ("DOC_ATTACH_PRIV".equals(name)) {
          continue;
        }
        if ("FLOW_ID".equals(name)) {
          value = String.valueOf(flowId);
          name = "FLOW_SEQ_ID";
        }
        if ("ID".equals(name)) {
          continue;
        }
        nameStr += name + ",";
        if (value== null || "".equals(value.trim())) {
          valueStr += "null,";
        } else {
          if (isUserOn && YHWorkFlowUtility.findId(useUserStr, name)) {
            value = this.getUserSeqId(value, conn);
          }
          if ("PRCS_DEPT".equals(name) && "ALL_DEPT".equals(value)) {
            value = "0";
          }
          value = value.replace("'", "''");
          valueStr += "'" + value + "',";
        }
      }
      valueStr = valueStr.substring(0, valueStr.length() - 1);
      nameStr = nameStr.substring(0, nameStr.length() - 1);
      String tmp = "insert into "+ YHWorkFlowConst.FLOW_PROCESS +" (" + nameStr +") values ("+ valueStr +")";
//      System.out.println(tmp);
      this.upateBySql(tmp, conn);
    }
  }
  public String getUserSeqId(String userIds , Connection conn) throws Exception {
    String str = "";
    String[] persons = userIds.split(",");
    for (String tmp : persons) {
      if (YHUtility.isInteger(tmp)) {
        String query = "select 1 from PERSON where seq_ID = " + tmp;
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(query);
          if (rs.next()) {
            str += tmp + ",";
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
      } else {
        String query = "select SEQ_ID from PERSON where USER_ID = '" + tmp + "'";
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(query);
          if (rs.next()) {
            int seqId = rs.getInt("SEQ_ID");
            str += seqId + ",";
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
      }
    }
    if (str.endsWith(",")) {
      str = str.substring(0 , str.length() - 1);
    }
    return str;
  }
  public  String getUserSeqId2(String privStr , Connection conn) throws Exception{
    String result = "";
    if (privStr == null || "".equals(privStr)){
      return privStr;
    }
    String [] arra = privStr.split("\\|");
    String user = "";
    String priv = "";
    String dept = "";
    if (arra.length == 1 ) {
      user = arra[0];
    } else if (arra.length >= 2 ) {
      user = arra[0];
      dept = arra[1];
      if (arra.length == 3) {
        priv = arra[2];
      }
      if ("ALL_DEPT".equals(dept)) {
        dept = "0";
      }
    } 
    user = this.getUserSeqId(user, conn);
    result = user + "|" + dept + "|" + priv;
    return result;
  }
  public void upateBySql(String sql , Connection conn) throws Exception {
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate(sql);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, null, null); 
    }
  }
  public void trans(Connection conn ,String flowStr , String toId , String userId , String beginRun , String endRun) throws Exception {
    if (flowStr.endsWith(",")) {
      flowStr = flowStr.substring(0, flowStr.length() - 1);
    }
    String query = "select RUN_ID FROM "+ YHWorkFlowConst.FLOW_RUN +" WHERE FLOW_ID IN ("+flowStr+") ";
    if (!YHUtility.isNullorEmpty(beginRun)) {
      query += " and RUN_ID >= " + beginRun ;
    }
    if (!YHUtility.isNullorEmpty(endRun)) {
      query += " and RUN_ID <= " + endRun ;
    }
    
    String runIds = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        String runId = String.valueOf(rs.getInt("RUN_ID"));
        if (!YHWorkFlowUtility.findId(runIds, runId)) {
          runIds += runId + ",";
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
     
    if (!YHUtility.isNullorEmpty(runIds)) {
      if (runIds.endsWith(",")) {
        runIds = runIds.substring(0, runIds.length() - 1);
      }
      String update = "UPDATE "+ YHWorkFlowConst.FLOW_RUN_PRCS +" set FROM_USER = USER_ID,USER_ID='"+toId+"' WHERE USER_ID='"+userId+"' AND RUN_ID IN(" + runIds +")";
      this.upateBySql(update, conn);
    }
  }

  public List<YHDocFlowType> getFlowTypeList(String sortId, Connection conn,
      String flowOther) throws Exception {
    // TODO Auto-generated method stub
    List<YHDocFlowType> list =  new ArrayList();
    sortId = YHWorkFlowUtility.getOutOfTail(sortId);
    String query  = "select * from "+ YHWorkFlowConst.FLOW_TYPE +" WHERE flow_SORT in (" + sortId + ") and FREE_OTHER = '"+ flowOther  +"' order by flow_no";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        YHDocFlowType ft = new YHDocFlowType();
        int seqId = rs.getInt("SEQ_ID");
        String flowName = rs.getString("flow_name");
        String flowType = rs.getString("flow_Type");
        String newUser = rs.getString("NEW_USER");
        String queryUser = rs.getString("query_User");
        String queryUserDept = rs.getString("query_User_Dept");
        String manageUser = rs.getString("manage_User");
        String manageUserDept = rs.getString("manage_User_Dept");
        ft.setFlowType(flowType);
        ft.setFlowName(flowName);
        ft.setSeqId(seqId);
        ft.setNewUser(newUser);
        ft.setQueryUser(queryUser);
        ft.setQueryUserDept(queryUserDept);
        ft.setManageUser(manageUser);
        ft.setManageUserDept(manageUserDept);
        list.add(ft);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return list;
  }
  public String getQueryItem(String flowId , Connection conn ) throws Exception {
    StringBuffer sb = new StringBuffer("{");
    String query = "select * from "+ YHWorkFlowConst.FLOW_TYPE +" where SEQ_ID=" + flowId;
    int formId = 0;
    int sortId = 0;
    String queryItem = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        formId = rs.getInt("FORM_SEQ_ID");
        sortId = rs.getInt("FLOW_SORT");
        queryItem = rs.getString("QUERY_ITEM");
        if (queryItem == null) {
          queryItem = "";
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    YHFlowFormLogic ffLogic = new YHFlowFormLogic();
    String formItem = ffLogic.getTitle(conn, formId);
    formItem += ",[B@]";
    sb.append("sortId:" + sortId);
    sb.append(",queryItem:'" + queryItem + "'");
    sb.append(",formItem:'" + formItem + "'");
    sb.append("}");
    return sb.toString();
  }

  public void setQueryItem(String flowId, String queryItem, Connection conn) throws Exception {
    // TODO Auto-generated method stub
    if (queryItem == null) {
      queryItem = "";
    }
    String query = "update "+ YHWorkFlowConst.FLOW_TYPE +" set query_item='" +queryItem +"' where SEQ_ID=" + flowId;
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate(query);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, null, null);
    }
  }
}

