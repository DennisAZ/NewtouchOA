package yh.core.funcs.workflow.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowProcess;
import yh.core.funcs.workflow.util.sort.YHFlowProcessComparator;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHFlowProcessLogic {
  public void saveFlowProcess(YHFlowProcess flowProcess , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    orm.saveSingle(conn, flowProcess);
  }
  public String getDisAip(int flowId , Connection conn) throws Exception {
    StringBuffer sb = new StringBuffer();
    String query = "select SEQ_ID , T_NAME from oa_fl_print_tpl where FLOW_ID = '"+flowId+"' AND T_TYPE='2'";
    Statement stm = null;
    ResultSet rs = null;
    sb.append("[");
    int count = 0 ;
    try{
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        String tName = rs.getString("T_NAME");
        sb.append("{").append("value:'" + seqId  + "'").append(",").append("text:'" + tName + "'},");
        count++ ;
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stm, rs, null);
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public List<YHFlowProcess> getFlowProcessByFlowId(int flowId , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    List<YHFlowProcess> list = new ArrayList();
    Map filters = new HashMap();
    filters.put("FLOW_SEQ_ID", flowId);
    list  = orm.loadListSingle(conn ,YHFlowProcess.class , filters);
    return  list;
  }
  public List<Map> getFlowPrcsByFlowId(int flowId , Connection conn) throws Exception{
    List<Map> list = new ArrayList<Map>();
    String query = "select SEQ_ID , PRCS_ID , PRCS_NAME from oa_fl_process WHERE FLOW_SEQ_ID =" + flowId + " order by prcs_id";
    Statement stm = null;
    ResultSet rs = null;
    try{
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        String prcsName = rs.getString("PRCS_NAME");
        int seqId = rs.getInt("SEQ_ID");
        int prcsId = rs.getInt("PRCS_ID");
        Map map = new HashMap();
        map.put("prcsName", prcsName);
        map.put("seqId", seqId);
        map.put("prcsId", prcsId);
        list.add(map);
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stm, rs, null);
    }
    return  list;
  }
  public List<YHFlowProcess> getFlowPrrocessByFlowId1(int flowId , Connection conn) throws Exception{
    String query = "select " 
      + " PRCS_ID"
      + " , PRCS_USER"
      + " , PRCS_DEPT"
      + " , PRCS_PRIV"
      + " from oa_fl_process where FLOW_SEQ_ID=" + flowId + " and PRCS_ID=1";
    
    List<YHFlowProcess> flowProcessList  = new ArrayList();
    Statement stm = null;
    ResultSet rs = null;
    try{
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        String prcsUser = rs.getString("PRCS_USER");
        String prcsDept = rs.getString("PRCS_DEPT");
        String prcsPriv = rs.getString("PRCS_PRIV");
        int prcsId = rs.getInt("PRCS_ID");
        YHFlowProcess fp = new YHFlowProcess();
        fp.setPrcsId(prcsId);
        fp.setPrcsUser(prcsUser);
        fp.setPrcsDept(prcsDept);
        fp.setPrcsPriv(prcsPriv);
        flowProcessList.add(fp);
      }
    }catch(Exception e){
      e.printStackTrace();
      throw e;
    }finally{
      YHDBUtility.close(stm, rs, null);
    }
    return flowProcessList;
  }
  public List<YHFlowProcess> getFlowPrrocessByFlowId2(int flowId , Connection conn) throws Exception{
    String query = "select " 
      + " PRCS_ID"
      + " , PRCS_USER"
      + " , PRCS_DEPT"
      + " , PRCS_PRIV"
      + " from oa_fl_process where FLOW_SEQ_ID=" + flowId;
    
    List<YHFlowProcess> flowProcessList  = new ArrayList();
    Statement stm = null;
    ResultSet rs = null;
    try{
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        String prcsUser = rs.getString("PRCS_USER");
        String prcsDept = rs.getString("PRCS_DEPT");
        String prcsPriv = rs.getString("PRCS_PRIV");
        int prcsId = rs.getInt("PRCS_ID");
        YHFlowProcess fp = new YHFlowProcess();
        fp.setPrcsId(prcsId);
        fp.setPrcsUser(prcsUser);
        fp.setPrcsDept(prcsDept);
        fp.setPrcsPriv(prcsPriv);
        flowProcessList.add(fp);
      }
    }catch(Exception e){
      e.printStackTrace();
      throw e;
    }finally{
      YHDBUtility.close(stm, rs, null);
    }
    return flowProcessList;
  }
  public YHFlowProcess getFlowProcessById(int flowId , String prcsId , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    YHFlowProcess flowProcess = null;
    Map filters = new HashMap();
    filters.put("FLOW_SEQ_ID", flowId);
    filters.put("PRCS_ID", prcsId);
    flowProcess  = (YHFlowProcess) orm.loadObjSingle(conn, YHFlowProcess.class, filters);
    return flowProcess;
  }
 
  public YHFlowProcess getFlowProcessById(int processId , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    YHFlowProcess flowProcess = new YHFlowProcess();
    flowProcess = (YHFlowProcess) orm.loadObjSingle(conn, YHFlowProcess.class, processId);
    return flowProcess;
  }
  public void delFlowProcess(int processId , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    YHFlowProcess fp = this.getFlowProcessById(processId , conn);
    int prcsId = fp.getPrcsId();
    int flowId = fp.getFlowSeqId();
    List<YHFlowProcess> list = this.getFlowProcessByFlowId(flowId , conn);
    boolean isChange = false;
    for(YHFlowProcess tmp : list){
      String prcsTo = tmp.getPrcsTo();
      String prcsToTmp = this.getOutofStr(prcsTo, String.valueOf(prcsId));
      if(prcsTo != null && !prcsTo.equals(prcsToTmp)){
        isChange = true;
        tmp.setPrcsTo(prcsToTmp);
      }
      int autoBaseUser = tmp.getAutoBaseUser();
      if(autoBaseUser == prcsId){
        isChange = true;
        tmp.setAutoBaseUser(0);
      }
      if(isChange){
        this.updateFlowProcess(tmp , conn);
      }
    }
    orm.deleteSingle(conn,YHFlowProcess.class, processId);
  }
  public String getOutofStr(String str,String s){
    if(str != null){
      String[] aStr = str.split(",");
      String strTmp = "";
      int j = 0 ;//控制重名
      for(int i = 0 ;i < aStr.length ; i++){
        if(!"".equals(aStr[i]) && (aStr[i] != s || j != 0)){
            strTmp += aStr[i] + ',';
        }else{
          if(aStr[i] == s){
            j = 1;
          }  
        }
      }
      //处理逗号
      if(!str.endsWith(",") && !"".equals(strTmp)){
        strTmp = strTmp.substring(0, strTmp.length() -1);
      }
      return strTmp;
    }else{
      return null;
    }
    
  }
  public void delFlowProcessByFlowId(int flowId , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    List<YHFlowProcess> list = this.getFlowProcessByFlowId(flowId , conn);
    for(YHFlowProcess fp : list){
      orm.deleteSingle(conn, fp);
    }
  }
  public void updateFlowProcess(YHFlowProcess flowProcess , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    orm.updateSingle(conn, flowProcess);
  }
  public int getMaxProcessId(int flowId , Connection conn) throws Exception{
    int max = 0 ;
    List<YHFlowProcess> list  = this.getFlowProcessByFlowId(flowId , conn) ;
    for(YHFlowProcess t : list){
      int prcsId = t.getPrcsId();
      if(prcsId > max){
        max = prcsId;
      }
    }
    return max;
  }
  public int getMinProcessId(int flowId , Connection conn) throws Exception{
    List<YHFlowProcess> list  = this.getFlowProcessByFlowId(flowId , conn);
    if(list.size() > 0){
      int min = list.get(0).getPrcsId() ;
      for(YHFlowProcess t : list){
        int prcsId = t.getPrcsId();
        if(prcsId < min){
          min = prcsId;
        }
      }
      return min;
    }else{
      return 0;
    }
  }
  public List<YHPerson> getPersonsByDept(int deptId , Connection conn) throws Exception{
    List<YHPerson> list = new ArrayList();
    Map filters = new HashMap();
    filters.put("DEPT_ID", deptId);
    YHORM orm = new YHORM();
    list  = orm.loadListSingle(conn ,YHPerson.class , filters);
    return  list;
  }
  public StringBuffer getDeptJson(List depts ,int procId , Connection conn) throws Exception{
    return this.getDeptJson(depts, procId, 0 , conn);
  }
  public StringBuffer getDeptJson(List depts , int procId , int deptId , Connection conn) throws Exception{
    YHFlowProcess proc = this.getFlowProcessById(procId , conn);
    String sSelectedDept  = YHOrgSelectLogic.changeDept(conn, proc.getPrcsDept()); 
    if(sSelectedDept == null){
      sSelectedDept = "";
    }
    String[] aSelectedDept = sSelectedDept.split(",");
    StringBuffer sb = new StringBuffer("[");
    YHDepartment dept = new YHDepartment();
    if(deptId != 0){
      for(int i = 0 ;i < depts.size();i ++){
        dept = (YHDepartment) depts.get(i);
        if(dept.getSeqId() == deptId){
          break;
        }
      }
      this.setDeptSingle(dept, depts, aSelectedDept, sb , 0);
    }else{
      for(int i = 0 ;i < depts.size();i ++){
        dept = (YHDepartment) depts.get(i);
        if(dept.getDeptParent() == 0){
          this.setDeptSingle(dept, depts, aSelectedDept, sb , 0);
        }
      }
    }
    
    sb.deleteCharAt(sb.length() - 1);
    
    sb.append("]");
    return sb;
  }
  public void setDeptSingle(YHDepartment dept , List depts , String[] aSelectedDept , StringBuffer sb ,int level){
    String deptName = dept.getDeptName();
    int deptId = dept.getSeqId();
    boolean isChecked = false;
    for(String tmp : aSelectedDept){
      if(!"".equals(tmp) && deptId == Integer.parseInt(tmp)){
        isChecked = true;
      }
    }
    String nbsp = "├";
    for(int i = 0 ;i < level;i++){
      nbsp = "&nbsp&nbsp" + nbsp;
    }
    sb.append("{");
    sb.append("deptName:'" + nbsp + deptName + "',");
    sb.append("deptId:'" + deptId + "',");
    sb.append("isChecked:" + isChecked) ;
    sb.append("},");
    //depts.remove(dept);
    
    level++;
    for(int i = 0 ;i < depts.size() ; i++){
      YHDepartment  deptTmp = (YHDepartment) depts.get(i);
      if(deptTmp.getDeptParent() == deptId){
        setDeptSingle(deptTmp , depts , aSelectedDept , sb , level);
      }
    }
  }
  public List<YHFlowProcess> getNextProcess(YHFlowProcess prcs , Connection conn) throws Exception{
    List<YHFlowProcess> list = this.getFlowProcessByFlowId(prcs.getFlowSeqId() , conn);
    Collections.sort(list,new YHFlowProcessComparator());
    List<YHFlowProcess> result = new ArrayList();
    String prcsTo = prcs.getPrcsTo();
    if(prcsTo == null || "".equals(prcsTo)){
      for(int i = 0 ; i< list.size() ; i++){
        YHFlowProcess prcsTmp = list.get(i);
        if(prcsTmp.getSeqId() == prcs.getSeqId()){
          if(i + 1 == list.size()){
            prcsTmp = new YHFlowProcess();
            prcsTmp.setPrcsId(0);
            prcsTmp.setSeqId(0);
            prcsTmp.setPrcsName("结束");
          }else{
            prcsTmp = list.get(i + 1);
          }
          result.add(prcsTmp);
        }
      }
    }else{
      String[] aPrcsTo = prcsTo.split(",");
      for(String tmp : aPrcsTo){
        if(tmp.equals("0")){
          YHFlowProcess prcsTmp = new YHFlowProcess();
          prcsTmp.setPrcsId(0);
          prcsTmp.setSeqId(0);
          prcsTmp.setPrcsName("结束");
          result.add(prcsTmp);
          continue;
        }
        for(YHFlowProcess prcsTmp : list){
          int prcsId = Integer.parseInt(tmp);
          if(prcsId == prcsTmp.getPrcsId()){
            result.add(prcsTmp);
          }
        }
      }
    }
    
    return result;
  }
  /**
   * 保存位置
   * @param strSql
   * @throws Exception
   */
  public void savePosition(String strSql , Connection conn) throws Exception{
    if(strSql == null){
      return ;
    }
    String[] aStrSql = strSql.split(";");//分解成数组
    String query = "UPDATE oa_fl_process SET ";
    for(String tmp : aStrSql){
      if(tmp != null && !"".equals(tmp)){
        tmp = query + tmp;
        Statement stm = null;
        try {
          stm = conn.createStatement();
          stm.executeUpdate(tmp);
          //conn.commit();
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, null, null); 
        }
      }
    }
  }
  /**
   * 取得插件
   * @param prcsId
   * @param flowId
   * @param conn
   * @return
   * @throws Exception
   */
  public String getPluginStr(int prcsId , int flowId , Connection conn) throws Exception{
    String query = "select  PLUGIN from oa_fl_process  where " 
      + "  PRCS_ID= " + prcsId 
      + " and FLOW_SEQ_ID=" + flowId;
    String plugin = null;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        plugin = rs.getString("PLUGIN");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return plugin;
  }
  /**
   * 保存对提取元数据的字段
   * @param prcsId
   * @param flowId
   * @param conn
   * @return
   * @throws Exception
   */
  public void setMatadataItem(String items , int seqId , Connection conn) throws Exception{
    String query = "update oa_fl_process set metadata_item='" + items + "' where seq_id=" + seqId;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate(query);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
  }
  
}
