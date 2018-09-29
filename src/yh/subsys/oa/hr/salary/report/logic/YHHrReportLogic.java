package yh.subsys.oa.hr.salary.report.logic;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.attendance.manage.logic.YHManageAttendLogic;
import yh.core.funcs.attendance.manage.logic.YHManageOutLogic;
import yh.core.funcs.attendance.manage.logic.YHManageQueryLogic;
import yh.core.funcs.attendance.personal.data.YHAttendDuty;
import yh.core.funcs.attendance.personal.logic.YHAttendDutyLogic;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.attendance.data.YHAttendConfig;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHSalItem;
import yh.subsys.oa.hr.salary.submit.data.YHSalPerson;
import yh.subsys.oa.hr.score.data.YHScoreFlow;

public class YHHrReportLogic {
  private static Logger log = Logger.getLogger(YHHrReportLogic.class);
   
  /**
   * 工资上报待办流程  通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getReportJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      String sql = null;
      String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        sql = " select s1.SEQ_ID,p1.USER_NAME,s1.BEGIN_DATE,s1.END_DATE,s1.SAL_YEAR + '年' + s1.SAL_MONTH + '月',s1.CONTENT "
          + " from oa_sal_flow s1 "
          + " join person p1 on s1.SAL_CREATER = p1.SEQ_ID "
          + " where s1.issend = 0 "
          + " ORDER BY s1.SEQ_ID desc ";
      }else if(dbms.equals(YHConst.DBMS_ORACLE)){
        sql = " select s1.SEQ_ID,p1.USER_NAME,s1.BEGIN_DATE,s1.END_DATE,s1.SAL_YEAR || '年' || s1.SAL_MONTH || '月',s1.CONTENT "
          + " from oa_sal_flow s1 "
          + " join person p1 on s1.SAL_CREATER = p1.SEQ_ID "
          + " where s1.issend = 0 "
          + " ORDER BY s1.SEQ_ID desc ";
      }else {
        sql = " select s1.SEQ_ID,p1.USER_NAME,s1.BEGIN_DATE,s1.END_DATE,concat(s1.SAL_YEAR , '年' , s1.SAL_MONTH , '月'),s1.CONTENT "
          + " from oa_sal_flow s1 "
          + " join person p1 on s1.SAL_CREATER = p1.SEQ_ID "
          + " where s1.issend = 0 "
          + " ORDER BY s1.SEQ_ID desc ";
      }
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 获取工资项的seq_id 并且ISCOMPUTER !=1的

   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String getSalItemIdLogic(Connection dbConn, YHPerson user) throws Exception {
    String sql = " SELECT SEQ_ID,ITEM_NAME from oa_sal_item where ISCOMPUTER = 0 and ISREPORT = 1 order by SLAITEM_ID ";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String seqIds = "";
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while(rs.next()){
       seqIds += rs.getString("SEQ_ID")+",";
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return seqIds;
  } 
  
  /**
   * 获得薪酬项目中的Id和Name
   * @param dbConn
   * @param user
   * @param seqID
   * @return
   * @throws Exception
   */
  public YHSalItem getSalItemIdAndNameLogic(Connection dbConn, YHPerson user,int seqID) throws Exception {
    String sql = " select SEQ_ID,ITEM_NAME,SLAITEM_ID from oa_sal_item where SEQ_ID ="+seqID;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    YHSalItem salItem = new YHSalItem();
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      if(rs.next()){
        salItem.setSeqId(rs.getInt("SEQ_ID"));
        salItem.setItemName(rs.getString("ITEM_NAME"));
        salItem.setSlaitemId(rs.getInt("SLAITEM_ID"));
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return salItem;
  }
  
  /**
   * 根据部门id查找所有属于这个部门的人员
   * @param dbConn
   * @param user
   * @param seqId
   * @return
   * @throws Exception
   */
  public List<YHSalPerson> getDeptPersonNameLogic(Connection dbConn, YHPerson user,String deptId, String flowId) throws Exception {
    
    String sql = " select SLAITEM_ID SID from oa_sal_item "
               + " where ISCOMPUTER = 0 and ISREPORT = 1 "
               + " order by SLAITEM_ID ";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ResultSet rsTemp = null;
    List<String> sid = new ArrayList<String>();
    List<YHSalPerson> listPerson = new ArrayList<YHSalPerson>();
    try{
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while(rs.next()){
        sid.add("S"+rs.getString("SID"));
      } 
      
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < sid.size(); i++){
        sb.append("(select " + sid.get(i) + " from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") " + sid.get(i) + " ,");
      }
      
      String str = "";
      if(sid.size() != 0){
        str = "," + sb.substring(0, sb.length()-1);
      }
      
      sql = " select p1.SEQ_ID USER_ID, p1.USER_NAME ,(select SEQ_ID from oa_sal_data h1 where h1.USER_ID=p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") SD_ID " + str
          + " from PERSON p1 "
          + " join USER_PRIV u1 on u1.SEQ_ID = p1.USER_PRIV "
          + " where  DEPT_ID = " + deptId + " or "+ YHDBUtility.findInSet(String.valueOf(deptId), "DEPT_ID_OTHER")
          + " order by u1.PRIV_NO ";
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();      
      
      while(rs.next()){
        YHSalPerson salPerson = new YHSalPerson();
        salPerson.setSdId(rs.getInt("SD_ID"));
        salPerson.setUserId(rs.getInt("USER_ID"));
        salPerson.setUserName(rs.getString("USER_NAME"));
        
        if(salPerson.getSdId() == 0){
          String sqlTemp = " select (select SEQ_ID from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) HSD_ID " + str.replace("oa_sal_data", "oa_pm_salary_data").replace("and h1.FLOW_ID=" + flowId, "")
                         + " from PERSON p1 "
                         + " join USER_PRIV u1 on u1.SEQ_ID = p1.USER_PRIV "
                         + " where p1.SEQ_ID = " + salPerson.getUserId() + " and DEPT_ID = " + deptId + " or "+ YHDBUtility.findInSet(String.valueOf(deptId), "DEPT_ID_OTHER")
                         + " order by u1.PRIV_NO ";
          stmt = dbConn.prepareStatement(sqlTemp);
          rsTemp = stmt.executeQuery();   
          if(rsTemp.next()){
            salPerson.setHsdId(rsTemp.getInt("HSD_ID"));
            Map<String,Double> smap = new HashMap<String,Double>();
            List<String> slist = new ArrayList<String>();
            for(int i = 0; i < sid.size(); i++){
              smap.put(sid.get(i), rsTemp.getDouble(sid.get(i)));
              slist.add(i, (String)sid.get(i));
            }
            salPerson.setSmap(smap);
            salPerson.setSlist(slist);
            listPerson.add(salPerson);
          }
        }
        else{
          Map<String,Double> smap = new HashMap<String,Double>();
          List<String> slist = new ArrayList<String>();
          for(int i = 0; i < sid.size(); i++){
            smap.put(sid.get(i), rs.getDouble(sid.get(i)));
            slist.add(i, (String)sid.get(i));
          }
          salPerson.setSmap(smap);
          salPerson.setSlist(slist);
          listPerson.add(salPerson);
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return listPerson;
  }
  
  /**
   * 批量设置人员财务工资
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void setReportInfo(Connection dbConn, Map<String,String> request, YHPerson person) throws Exception {
    
    String flowId = request.get("flowId");
    String totalStr = request.get("total");
    String titleStr = request.get("title");
    if (!YHUtility.isNullorEmpty(titleStr)) {
      titleStr = titleStr.substring(0, titleStr.length()-1);
    }
    String titleList[] = titleStr.split(",");
    
    int total = 0;
    if (!YHUtility.isNullorEmpty(totalStr)) {
      total = Integer.parseInt(totalStr);
    }
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      for(int i = 0; i < total; i++){
        String sdIdStr = request.get("sdId_" + i);
        int sdId = 0;
        if (!YHUtility.isNullorEmpty(sdIdStr)) {
          sdId = Integer.parseInt(sdIdStr);
        }
        String sql = " select SEQ_ID from oa_sal_data WHERE SEQ_ID = " + sdId;
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();
        
        List<String> Slist = new ArrayList<String>();
        for(int j = 0; j < titleList.length; j++){
        	if(request.get(titleList[j]+"_"+i) !=null){
        		Slist.add(request.get(titleList[j]+"_"+i));
        	}
        }
        if(rs.next()){
          sql = " update oa_sal_data set ";
          for(int j = 0; j < titleList.length; j++){
            sql += titleList[j] + "=" + Slist.get(j) + ",";
          }
          sql = sql.substring(0, sql.length()-1) + " where SEQ_ID=" + rs.getString("SEQ_ID");
        }
        else{
          String userId = request.get("userId_" + i);
          if("".equals(titleStr)){
        	  
        	  sql = " insert into OA_SAL_DATA(FLOW_ID,USER_ID,INSURANCE_OTHER) values(" + flowId + "," + userId + ",1) ";
          }
          else{
        	  sql = " insert into OA_SAL_DATA(FLOW_ID,USER_ID,INSURANCE_OTHER," + titleStr + ") values(" + flowId + "," + userId + ",1," + Slist.toString().substring(1, Slist.toString().length()-1) + ") ";
          }
        }
        ps = dbConn.prepareStatement(sql);
        ps.executeUpdate();     
      }
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 获取工资项的seq_id 并且ISCOMPUTER !=1的

   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String getSalItemIdLogic2(Connection dbConn, YHPerson user) throws Exception {
    String sql = " SELECT SEQ_ID,ITEM_NAME from oa_sal_item order by SLAITEM_ID ";//where ISCOMPUTER = 0 and ISREPORT = 1 
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String seqIds = "";
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while(rs.next()){
       seqIds += rs.getString("SEQ_ID")+",";
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return seqIds;
  } 
  
  /**
   * 获得薪酬项目中的Id和Name
   * @param dbConn
   * @param user
   * @param seqID
   * @return
   * @throws Exception
   */
  public YHSalItem getSalItemIdAndNameLogic2(Connection dbConn, YHPerson user,int seqID) throws Exception {
    String sql = " select SEQ_ID,ITEM_NAME,SLAITEM_ID,ISCOMPUTER,ISREPORT,FORMULA,FORMULANAME from oa_sal_item where SEQ_ID ="+seqID;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    YHSalItem salItem = new YHSalItem();
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      if(rs.next()){
        salItem.setSeqId(rs.getInt("SEQ_ID"));
        salItem.setItemName(rs.getString("ITEM_NAME"));
        salItem.setSlaitemId(rs.getInt("SLAITEM_ID"));
        salItem.setIscomputer(rs.getString("ISCOMPUTER"));
        salItem.setIsreport(rs.getString("ISREPORT"));
        salItem.setFormula(rs.getString("FORMULA"));
        salItem.setFormulaname(rs.getString("FORMULANAME"));
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return salItem;
  }
  
  /**
   * 根据部门id查找所有属于这个部门的人员
   * @param dbConn
   * @param user
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHSalPerson getPersonNameLogic(Connection dbConn, YHPerson user,String userId, String flowId, String history) throws Exception {
    YHSalPerson salPerson = new YHSalPerson();
    String sql = " select SLAITEM_ID SID from oa_sal_item "
               ///+ " where ISCOMPUTER = 0 and ISREPORT = 1 "
               + " order by SLAITEM_ID ";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ResultSet rsTemp = null;
    List<String> sid = new ArrayList<String>();
    List<YHSalPerson> listPerson = new ArrayList<YHSalPerson>();
    try{
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while(rs.next()){
        sid.add("S"+rs.getString("SID"));
      } 
      
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < sid.size(); i++){
        sb.append("(select " + sid.get(i) + " from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") " + sid.get(i) + " ,");
      }
      String salData = "(select MEMO from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") MEMO ";
      
      sql = " select p1.SEQ_ID USER_ID, p1.USER_NAME ,(select SEQ_ID from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") SD_ID ," + sb +salData
          + " from PERSON p1 "
          + " join USER_PRIV u1 on u1.SEQ_ID = p1.USER_PRIV "
          + " where  p1.SEQ_ID = " + userId 
          + " order by u1.PRIV_NO ";
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();      
      
      if(rs.next()){
        salPerson.setSdId(rs.getInt("SD_ID"));
        salPerson.setUserId(rs.getInt("USER_ID"));
        salPerson.setUserName(rs.getString("USER_NAME"));
        
        if(salPerson.getSdId() == 0 && history == null){
          String tmp = sb.toString();
          if (tmp != null) {
            if (tmp.endsWith(",")) {
              tmp = tmp.substring(0, tmp.length() - 1);
            }
            tmp = tmp.replace("oa_sal_data", "oa_pm_salary_data").replace("and h1.FLOW_ID=" + flowId, "");
            if (!"".equals(tmp)) {
              tmp = "," + tmp;
            }
          } 
          String sqlTemp = " select (select SEQ_ID from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) HSD_ID " + tmp
                         + "  from PERSON p1 "
                         + " join USER_PRIV u1 on u1.SEQ_ID = p1.USER_PRIV "
                         + " where p1.SEQ_ID = " + userId 
                         + " order by u1.PRIV_NO ";
          stmt = dbConn.prepareStatement(sqlTemp);
          rsTemp = stmt.executeQuery();   
          if(rsTemp.next()){
            salPerson.setHsdId(rsTemp.getInt("HSD_ID"));
            Map<String,Double> smap = new HashMap<String,Double>();
            List<String> slist = new ArrayList<String>();
            for(int i = 0; i < sid.size(); i++){
              smap.put(sid.get(i), rsTemp.getDouble(sid.get(i)));
              slist.add(i, (String)sid.get(i));
            }
            salPerson.setSmap(smap);
            salPerson.setSlist(slist);
            salPerson.setMemo("");
          }
        }
        else{
          Map<String,Double> smap = new HashMap<String,Double>();
          List<String> slist = new ArrayList<String>();
          for(int i = 0; i < sid.size(); i++){
            smap.put(sid.get(i), rs.getDouble(sid.get(i)));
            slist.add(i, (String)sid.get(i));
          }
          salPerson.setSmap(smap);
          salPerson.setSlist(slist);
          salPerson.setMemo(rs.getString("MEMO"));
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return salPerson;
  }
  
  /**
   * 查询HR_INSURANCE_PARA中的yesOther 是否为1 为1显示保险系数
   * @param dbConn
   * @param user
   * @return
   * @throws Exception
   */
  public String getYesOtherLogic(Connection dbConn, YHPerson user) throws Exception {
    String sql = "SELECT YES_OTHER from oa_pm_insurance";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      if(rs.next()){
       return rs.getString("YES_OTHER");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return null;
  }
  
  /**
   * 指定设置人员财务工资
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void setReportInfoUser(Connection dbConn, Map<String,String> request, YHPerson person) throws Exception {
    String userId = request.get("userId");
    String flowId = request.get("flowId");
    String titleStr = request.get("title");
    if (!YHUtility.isNullorEmpty(titleStr)) {
      titleStr = titleStr.substring(0, titleStr.length()-1);
    }
    String titleList[] = titleStr.split(",");
    
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = " select SEQ_ID from oa_sal_data WHERE USER_ID = " + userId +" and FLOW_ID = " + flowId;
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      List<String> Slist = new ArrayList<String>();
      for(int j = 0; j < titleList.length; j++){
        Slist.add(request.get(titleList[j]));
      }
      if(rs.next()){
        sql = " update oa_sal_data set ";
        for(int j = 0; j < titleList.length; j++){
          sql += titleList[j] + "=" + Slist.get(j) + ",";
        }
        sql += " MEMO = ?"
             + " where SEQ_ID=" + rs.getString("SEQ_ID");
      }
      else{
        sql = " insert into oa_sal_data (" 
            + " FLOW_ID" 
            + ",USER_ID" 
            + "," + titleStr 
            + ",ALL_BASE" 
            + ",PENSION_BASE" 
            + ",PENSION_U" 
            + ",PENSION_P" 
            + ",MEDICAL_BASE" 
            + ",MEDICAL_U" 
            + ",MEDICAL_P" 
            + ",FERTILITY_BASE" 
            + ",FERTILITY_U" 
            + ",UNEMPLOYMENT_BASE" 
            + ",UNEMPLOYMENT_U" 
            + ",UNEMPLOYMENT_P" 
            + ",INJURIES_BASE" 
            + ",INJURIES_U" 
            + ",HOUSING_BASE" 
            + ",HOUSING_U" 
            + ",HOUSING_P" 
            + ",INSURANCE_OTHER" 
            + ",MEMO) " 
            + "(select " + flowId + "," + userId + "," + Slist.toString().substring(1, Slist.toString().length()-1)
            + ",ALL_BASE"
            + ",PENSION_BASE" 
            + ",PENSION_U" 
            + ",PENSION_P" 
            + ",MEDICAL_BASE" 
            + ",MEDICAL_U" 
            + ",MEDICAL_P" 
            + ",FERTILITY_BASE" 
            + ",FERTILITY_U" 
            + ",UNEMPLOYMENT_BASE" 
            + ",UNEMPLOYMENT_U" 
            + ",UNEMPLOYMENT_P" 
            + ",INJURIES_BASE" 
            + ",INJURIES_U" 
            + ",HOUSING_BASE" 
            + ",HOUSING_U" 
            + ",HOUSING_P" 
            + ",1" 
            + ",? from oa_pm_salary_data where USER_ID = " + userId + ") ";
      }
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, request.get("memo"));
      ps.executeUpdate();     
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 工资代办流程  通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getReportHistoryListJson(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      String sql = null;
      String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        sql = " select s1.SEQ_ID,p1.USER_NAME,s1.BEGIN_DATE,s1.END_DATE,s1.SAL_YEAR + '年' + s1.SAL_MONTH + '月',s1.CONTENT "
          + " from oa_sal_flow s1 "
          + " join person p1 on s1.SAL_CREATER = p1.SEQ_ID "
          + " where s1.issend = 1 "
          + " ORDER BY s1.SEQ_ID desc ";
      }else if(dbms.equals(YHConst.DBMS_ORACLE)){
        sql = " select s1.SEQ_ID,p1.USER_NAME,s1.BEGIN_DATE,s1.END_DATE,s1.SAL_YEAR || '年' || s1.SAL_MONTH || '月',s1.CONTENT "
          + " from oa_sal_flow s1 "
          + " join person p1 on s1.SAL_CREATER = p1.SEQ_ID "
          + " where s1.issend = 1 "
          + " ORDER BY s1.SEQ_ID desc ";
      }else {
        sql = " select s1.SEQ_ID,p1.USER_NAME,s1.BEGIN_DATE,s1.END_DATE,concat(s1.SAL_YEAR , '年' , s1.SAL_MONTH , '月'),s1.CONTENT "
          + " from oa_sal_flow s1 "
          + " join person p1 on s1.SAL_CREATER = p1.SEQ_ID "
          + " where s1.issend = 1 "
          + " ORDER BY s1.SEQ_ID desc ";
      }
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  
  
  
  
  /**
   *获取考核数据 
   * @param dbConn
   * @param userId
   * @param person
   * */
  @SuppressWarnings("unchecked")
  public String getAttendanceLogic(Connection dbConn, String userId,String flowId,YHPerson person) throws Exception {
    Statement stmt = null;
    ResultSet rs = null; 
    Statement stmt1 = null;
    ResultSet rs1 = null; 
    String lateCount="";   //迟到次数
    String ealyCount="";   //早退次数
    String outCount="";    //外出次数
    String leaveCount="";   //请假次数
    String evectionCount="";   //出差次数
    String overtimeCount="";  // 加班次数
    String awardCount="";    // 奖励次数
    String punishCount="";  //惩罚次数
    String calCount="";      //日程次数
    String calCount1="";    //日程完成次数
    String diaryCount="";   //日志次数
    String checkCount="";  // 共计次数
    int attendCount = 6;
    
    String sql="";
    stmt=dbConn.createStatement();
    stmt1=dbConn.createStatement();
    SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM-dd");  
    SimpleDateFormat curTime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar   cDay1   =   Calendar.getInstance(); 
    Calendar   cDay2   =   Calendar.getInstance(); 
    
    sql="select SAL_YEAR,SAL_MONTH from oa_sal_flow where SEQ_ID='"+flowId+"'";
    rs=stmt.executeQuery(sql);
    String currentdate="";
    if(rs.next()){
      String str  = rs.getString(1) + "-" + rs.getString(2) + "-";
      currentdate=str+"01";
    }
 
    cDay1.setTime(curTime.parse(currentdate));   
    int lastDay   =   cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);       
    Date   lastDate   = curTime.parse(currentdate);   
    lastDate.setDate(lastDay);   
  
    String date=curTime.format(lastDate);
    
    
    
    String startDate=date.substring(0,7)+"-01 00:00:00";
    String endDate=date+" 23:59:59";
 
    
    //获得迟到次数    //获得早退次数
    int late=0,ealry=0;
    String dutyTime1="";
    String dutyTime2="";
    String dutyTime3="";
    String dutyTime4="";
    String dutyTime5="";
    String dutyTime6="";
    String dutyType1="";
    String dutyType2="";
    String dutyType3="";
    String dutyType4="";
    String dutyType5="";
    String dutyType6="";
    String general="";
    String dutyType="dutyType";
    String dutyTime="dutyTime";
    HashMap<String,String> timeMap=new HashMap<String,String>();
    
   // sql="select * from attend_config where duty_name='正常班'";
    sql="select * from oa_attendance_conf where SEQ_ID in (select duty_type from PERSON where SEQ_ID='"+userId+"')";

    rs=stmt.executeQuery(sql);
    if(rs.next()){
//      timeMap.put("dutyTime1",rs.getString("DUTY_TIME1"));
//      timeMap.put("dutyTime2",rs.getString("DUTY_TIME2"));
//      timeMap.put("dutyTime3",rs.getString("DUTY_TIME3"));
//      timeMap.put("dutyTime4",rs.getString("DUTY_TIME4"));
//      timeMap.put("dutyTime5",rs.getString("DUTY_TIME5"));
//      timeMap.put("dutyTime6",rs.getString("DUTY_TIME6"));
//      timeMap.put("dutyType1",rs.getString("DUTY_TYPE1"));
//      timeMap.put("dutyType2",rs.getString("DUTY_TYPE2"));
//      timeMap.put("dutyType3",rs.getString("DUTY_TYPE3"));
//      timeMap.put("dutyType4",rs.getString("DUTY_TYPE4"));
//      timeMap.put("dutyType5",rs.getString("DUTY_TYPE5"));
//      timeMap.put("dutyType6",rs.getString("DUTY_TYPE6"));
//      general=rs.getString("GENERAL");
      
      //计算总登记次数
//      for(int i = 1; i < 7; i++){
//        if(YHUtility.isNullorEmpty(rs.getString("DUTY_TIME"+i))){
//          attendCount--;
//        }
//      }
//      attendCount = attendCount*Integer.parseInt(date.substring(8, 10));
      
      
      
      
      Map map = new HashMap();
      map.put("userId", userId);
      map.put("dutyType", rs.getString("SEQ_ID"));
      map.put("startDate", date.substring(0,7)+"-01");
      if(Integer.parseInt(date.substring(5, 7)) ==  (cDay2.get(Calendar.MONTH) + 1)){
        int day = cDay1.DATE;
        String dayStr = day > 9 ? String.valueOf(day) : "0"+day;
        map.put("endDate", date.substring(0,7)+"-"+dayStr);
      }
      else{
        map.put("endDate", date);
      }
      List list = getDeptDuty(dbConn,map);
      Integer lateCount1 = 0;
      Integer earlyCount1 = 0;
      if (list.size() > 0) {
        lateCount1 =  (Integer) ((Map)list.get(0)).get("lateCount");
        earlyCount1 = (Integer) ((Map)list.get(0)).get("earlyCount");
      }
      attendCount = lateCount1 + earlyCount1;
    }
 
    //查询本月登记次数
//    sql = " select count(1) from oa_attendance_duty where USER_ID='"+userId+"' and "+YHDBUtility.getDateFilter("REGISTER_TIME", startDate, ">=")+" and "+YHDBUtility.getDateFilter("REGISTER_TIME", endDate, "<=");
//    rs=stmt.executeQuery(sql);
//    if(rs.next()){
//      
//      //计算本月未登记次数//
//      attendCount = attendCount - rs.getInt(1);
//    }

    sql="select REGISTER_TYPE,REGISTER_TIME from oa_attendance_duty where USER_ID='"+userId+"' and "+YHDBUtility.getDateFilter("REGISTER_TIME", startDate, ">=")+" and "+YHDBUtility.getDateFilter("REGISTER_TIME", endDate, "<=");
   
    rs=stmt.executeQuery(sql);
    while(rs.next()){
      String type=rs.getString(1);
      String time=rs.getString(2); 
      time=time.substring(0,19);

    //排除双休日，节假日，出差，请假，外出
      String status="0";
    /*  //是否节假日

      sql="select * from oa_attendance_festival where "+YHDBUtility.getDateFilter("BEGIN_DATE",time, "<=")+" and "+YHDBUtility.getDateFilter("END_DATE",time, ">=");
      rs1=stmt1.executeQuery(sql);
      if(rs1.next()){
        status="1";
      }
      //是否双休日

      cDay1.setTime(curTime1.parse(time));
      int week=cDay1.get(Calendar.DAY_OF_WEEK)-1;
     if(general!=null && general.indexOf(week+"")!=-1){
       status="1";
     }          */
    
      String time1=time;
      time=time.substring(11,19);
  
      dutyType="dutyType";
      dutyTime="dutyTime";
      
      dutyTime+=type;
      dutyType+=type;
      String dutyTimes=timeMap.get(dutyTime);
      String dutyTypes=timeMap.get(dutyType);//获取对应的考勤时间点

      //是否为外出

      if(dutyTimes!=null){
        
        //是否为出差

        sql="select * from oa_attendance_trip where USER_ID='"+userId+"' and ALLOW='1' and "+YHDBUtility.getDateFilter("EVECTION_DATE1",time1.substring(0,10)+" "+dutyTimes, "<=")+" and "+YHDBUtility.getDateFilter("EVECTION_DATE2",time1.substring(0,10)+" "+dutyTimes, ">=");
        rs1=stmt1.executeQuery(sql);
        if(rs1.next()){
          status="1";
        }
        //是否为请假

        sql="select * from oa_attendance_off where USER_ID='"+userId+"' and ALLOW in ('1','3') and "+YHDBUtility.getDateFilter("LEAVE_DATE1",time1.substring(0,10)+" "+dutyTimes, "<=")+" and "+YHDBUtility.getDateFilter("LEAVE_DATE2",time1.substring(0,10)+" "+dutyTimes, ">=");
        rs1=stmt1.executeQuery(sql);
        if(rs1.next()){
          status="1";
        }

      sql="select * from oa_attendance_out where USER_ID='"+userId+"' and ALLOW in ('1') and "+YHDBUtility.getDateFilter("SUBMIT_TIME",time1.substring(0,10)+" 00:00:00", ">=") + " and  "+YHDBUtility.getDateFilter("SUBMIT_TIME",time1.substring(0,10)+" 23:59:59", "<=") + " and OUT_TIME1<='"+dutyTimes.substring(0,5)+"' and OUT_TIME2>='"+dutyTimes.substring(0,5)+"'";
      rs1=stmt1.executeQuery(sql);
       if(rs1.next()){       
        status="1";
        }    
      }
    
       if(status.equals("0")){
        if(dutyTimes!=null && dutyType!=null){
        if(dutyTypes.equals("1")){
            if(time.compareTo(dutyTimes)>0){ late++;  
           
            }
        }
        else if(dutyTypes.equals("2")){
            if(time.compareTo(dutyTimes)<0){ ealry++;
          
            }
           }          
          }
         }
       rs1.close();
   }
  
     lateCount=""+late;
     ealyCount=""+ealry;
  
     
    //获取USER的出差次数

     sql="select count(*) from oa_attendance_trip where USER_ID='"+userId+"' and ALLOW='1' AND (("+YHDBUtility.getDateFilter("EVECTION_DATE1", startDate, ">=")+" and "+YHDBUtility.getDateFilter("EVECTION_DATE1", endDate, "<=")+") or ("+YHDBUtility.getDateFilter("EVECTION_DATE2", startDate, ">=")+" and "+YHDBUtility.getDateFilter("EVECTION_DATE2", endDate, "<=")+") or ("+YHDBUtility.getDateFilter("EVECTION_DATE2", endDate, "<=")+" and "+YHDBUtility.getDateFilter("EVECTION_DATE1", startDate, ">=")+"))"; 
   
     rs=stmt.executeQuery(sql);
    if(rs.next()){
      evectionCount=rs.getString(1);
     
    }

    
    //获取USER的外出次数

    sql="select count(*) from oa_attendance_out where USER_ID='"+userId+"' and allow='1' and "+YHDBUtility.getDateFilter("SUBMIT_TIME", startDate, ">=")+" and "+YHDBUtility.getDateFilter("submit_time", endDate, "<=");
    rs=stmt.executeQuery(sql);
    if(rs.next()){
      outCount=rs.getString(1);
    }

    //获取请假次数
    sql="select count(*) from oa_attendance_off where USER_ID='"+userId+"' and allow in('1','3') and (("+YHDBUtility.getDateFilter("leave_date1", startDate, ">=")+"  and "+YHDBUtility.getDateFilter("leave_date1", endDate, "<=")+") or ("+YHDBUtility.getDateFilter("leave_date2", startDate, ">=")+"  and "+YHDBUtility.getDateFilter("leave_date2", endDate, "<=")+") or ("+YHDBUtility.getDateFilter("leave_date1", startDate, ">=")+"  and "+YHDBUtility.getDateFilter("leave_date2", endDate, "<=")+"))";
  
    rs=stmt.executeQuery(sql);
    if(rs.next()){
      leaveCount=rs.getString(1);
    }

    //获取加班次数
    sql="select count(*) from oa_timeout_record where USER_ID='"+userId+"' and status='1' and "+YHDBUtility.getDateFilter("BEGIN_TIME", startDate, ">=")+" and "+YHDBUtility.getDateFilter("BEGIN_TIME", endDate, "<=");
    rs=stmt.executeQuery(sql);
    if(rs.next()){
      overtimeCount=rs.getString(1);
    }

    //获取奖励次数
    sql="select count(*) from oa_pm_employee_encouragement where 1=1 and "+YHDBUtility.findInSet(userId,"STAFF_NAME")+" and incentive_type='1' and "+YHDBUtility.getDateFilter("incentive_time", startDate, ">=")+" and "+YHDBUtility.getDateFilter("incentive_time", endDate, "<=");
   
    rs=stmt.executeQuery(sql);
    if(rs.next()){
      awardCount=rs.getString(1);
    }

    //获取惩罚次数
    sql="select count(*) from oa_pm_employee_encouragement where 1=1 and "+YHDBUtility.findInSet(userId,"STAFF_NAME")+" and "+YHDBUtility.getDateFilter("incentive_time", startDate, ">=")+" and "+YHDBUtility.getDateFilter("incentive_time", endDate, "<=")+" and incentive_type='2'";
    rs=stmt.executeQuery(sql);
    if(rs.next()){
      punishCount=rs.getString(1);
    }

    //获取日程次数
    sql="select count(*) from oa_schedule where USER_ID='"+userId+"' and  CAL_TYPE!='2' and "+YHDBUtility.getDateFilter("cal_time", startDate, ">=")+" and "+YHDBUtility.getDateFilter("cal_time", endDate, "<=");
   
    rs=stmt.executeQuery(sql);
    if(rs.next()){
      calCount=rs.getString(1);
    }

    
    //获取已完成日程次数

    sql="select count(*) from oa_schedule where USER_ID='"+userId+"' and CAL_TYPE!='2' and  "+YHDBUtility.getDateFilter("cal_time", startDate, ">=")+" and "+YHDBUtility.getDateFilter("cal_time", endDate, "<=")+" and over_status='1'";
    rs=stmt.executeQuery(sql);
    if(rs.next()){
      calCount1=rs.getString(1);
    }

    
    //获取日志次数
    sql="select count(*) from oa_journal where USER_ID='"+userId+"' and "+YHDBUtility.getDateFilter("dia_date", startDate, ">=")+" and "+YHDBUtility.getDateFilter("dia_date", endDate, "<=")+"and DIA_TYPE!='2'";
  
    rs=stmt.executeQuery(sql);
    if(rs.next()){
      diaryCount=rs.getString(1);
    }

    
    //获得考核次数
    sql = "SELECT count(*) from oa_score_fl sf,oa_score_data sd where sf.SEQ_ID=sd.FLOW_ID and "+YHDBUtility.findInSet(userId,"sf.PARTICIPANT")+" and "+YHDBUtility.getDateFilter("sf.END_DATE", startDate, ">=")+" and "+YHDBUtility.getDateFilter("sf.END_DATE", endDate, "<=")+" and  "+YHDBUtility.findInSet(userId,"sd.PARTICIPANT");
    rs=stmt.executeQuery(sql);
    if(rs.next())
    {
      checkCount=rs.getString(1);
    }
    stmt1.close(); 
    rs.close();
    stmt.close(); 
    
    return "{attendCount:"+attendCount+",lateCount:"+lateCount+",ealyCount:"+ealyCount+",outCount:"+outCount+",leaveCount:"+leaveCount+",evectionCount:"+evectionCount+",overtimeCount:"+overtimeCount+",awardCount:"+awardCount+",punishCount:"+punishCount+",calCount:"+calCount+",calCount1:"+calCount1+",diaryCount:"+diaryCount+",checkCount:"+checkCount+",startDate:'"+startDate+"',endDate:'"+endDate+"'}";

  }
  
  
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public static String selectList(Connection dbConn,Map request,YHScoreFlow flow, String beginDate1,String endDate1,String cd,String userId) throws Exception {
    String sql = "select ex.SEQ_ID,son.SEQ_ID,ex.FLOW_TITLE,ex.RANKMAN,ex.PARTICIPANT,son.GROUP_NAME,ex.ANONYMITY"
      + ",ex.BEGIN_DATE,ex.END_DATE,ex.FLOW_DESC,ex.FLOW_FLAG,ex.SEND_TIME FROM oa_score_data da,oa_score_fl ex "
      + " left outer join oa_score_team son on son.seq_id = ex.GROUP_ID "
      + " WHERE 1=1 and ex.SEQ_ID=da.FLOW_ID and "+YHDBUtility.findInSet(userId,"da.PARTICIPANT")+" and "+YHDBUtility.findInSet(userId,"ex.PARTICIPANT");
   
    
    
    if (!YHUtility.isNullorEmpty(flow.getFlowTitle())) {
      sql += " and ex.FLOW_TITLE like '%" + YHDBUtility.escapeLike(flow.getFlowTitle()) + "%' " + YHDBUtility.escapeLike();
    }
    if (flow.getGroupId() > 0) {
      sql += " and ex.GROUP_ID=" + flow.getGroupId();
    }
    if (!YHUtility.isNullorEmpty(flow.getRankman())) {
      sql += " and " + YHDBUtility.findInSet(flow.getRankman(),"ex.RANKMAN");
    }
    if (!YHUtility.isNullorEmpty(flow.getParticipant())) {
      sql += " and " + YHDBUtility.findInSet(flow.getParticipant(),"ex.PARTICIPANT");
    }
    if (flow.getBeginDate() != null) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.BEGIN_DATE", YHUtility.getDateTimeStr(flow.getBeginDate()), ">=");
    }
    if (!YHUtility.isNullorEmpty(beginDate1)) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.BEGIN_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(beginDate1)), "<=");
    }
    if (flow.getEndDate() != null) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.END_DATE", YHUtility.getDateTimeStr(flow.getEndDate()), ">=");
    }
    if (!YHUtility.isNullorEmpty(endDate1)) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.END_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(endDate1)), "<=");
    }
    if (cd.equals("2")) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.END_DATE", YHUtility.getDateTimeStr(new java.util.Date()), "<=") + " and  ex.END_DATE is not null ";
    }
    sql += " order by ex.SEND_TIME desc ";
    
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /**
	 * 下载CSV模板
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<YHDbRecord> downCSVTempletLogic(Connection dbConn) throws Exception {
		ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
		YHDbRecord record = new YHDbRecord();

		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT SLAITEM_ID,ITEM_NAME from oa_sal_item ORDER BY SLAITEM_ID";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			record.addField("用户名", "");
			while (rs.next()) {
				String itemName = YHUtility.null2Empty(rs.getString("ITEM_NAME"));
				record.addField(itemName, "");
			}
			result.add(record);
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return result;
	}
	public int getSalDataCountByFlowIdLogic(Connection dbConn, int flowId) throws Exception {
		int count = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT count(SEQ_ID) from oa_sal_data where FLOW_ID=" + flowId;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return count;
	}
	
	/**
	 * 导入CSV工资数据
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @param buffer
	 * @return
	 * @throws Exception
	 */
	public Map<Object, Object> impReportInfoByCsvLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person, StringBuffer buffer)
			throws Exception {
		Map<Object, Object> returnMap = new HashMap<Object, Object>();
		int isCount = 0;
		int updateCount = 0;
		PreparedStatement stmt = null;
		String sql = "";
		try {
			Map<Object, Object> bufferMap = new HashMap<Object, Object>();
			String infoStr = "";
			InputStream is = fileForm.getInputStream();
			String flowIdStr = fileForm.getParameter("flowId");
			String c1Str = fileForm.getParameter("c1");
			int flowId = 0;
			if (YHUtility.isNumber(flowIdStr)) {
				flowId = Integer.parseInt(flowIdStr);
			}
			Map<String,Integer> fieldMap = this.getSalItemId(dbConn);
			
			ArrayList<YHDbRecord> dbRecords = YHCSVUtil.CVSReader(is, YHConst.CSV_FILE_CODE);
			
			int operate = 0;
			if ("2".equalsIgnoreCase(c1Str)) {
				operate = 1;
			}else {
			// 清除本次流程之前导入的数据
				this.delSalDataByFlowId(dbConn, flowId);
			}
			if (operate==0) {
				for (YHDbRecord record : dbRecords) {
					int fieldCount = record.getFieldCnt();
					String userId = "";
					String keyStr = "";
					String valueStr = "";
					int usefulColumn = 0;
					if (fieldCount > 0) {
						for (int i = 0; i < fieldCount; i++) {
							String keyName = record.getNameByIndex(i);
							String value = (String) record.getValueByIndex(i);
							if (!"用户名".equals(keyName.trim()) &&fieldMap.containsKey(keyName.trim())) {
								usefulColumn++;
								keyStr += "S" + fieldMap.get(keyName.trim()) + ",";
								valueStr += "'" + value + "',";
							}else if ("用户名".equals(keyName.trim())) {
								userId = value;
							}
						}
						if (keyStr.endsWith(",")) {
							keyStr = keyStr.substring(0, keyStr.length() - 1);
						}
						if (valueStr.endsWith(",")) {
							valueStr = valueStr.substring(0, valueStr.length() - 1);
						}
						Map<Object, Object> map = this.getPersonCountByUserName(dbConn, userId);
						boolean isHave = (Boolean) map.get("isHave");
						int personSeqId = (Integer) map.get("personId");
						if (!isHave) {
							String nameStr = "";
							if (!YHUtility.isNullorEmpty(userId)) {
								nameStr = userId;
							}
							infoStr = "员工" + nameStr + "尚未在OA系统中注册！！";
							bufferMap.put("userId", userId);
							bufferMap.put("infoStr", infoStr);
							bufferMap.put("color", "red");
							this.sbStrJson(buffer, bufferMap);
							continue;
						}
					// csv文件需包含有效的工资项目
						if (usefulColumn > 0) {
							if (flowId != 0) {
								sql = "insert into oa_sal_data(FLOW_ID,USER_ID," + keyStr.trim() + " ) values (" + flowId + ", " + personSeqId + ", " + valueStr + " )";
								stmt = dbConn.prepareStatement(sql);
								stmt.executeUpdate();
								infoStr = "员工" + userId + "的工资导入完成！！";
								bufferMap.put("userId", userId);
								bufferMap.put("infoStr", infoStr);
								bufferMap.put("color", "green");
								this.sbStrJson(buffer, bufferMap);
								isCount++;
							}else {
								bufferMap.put("userId", userId);
								infoStr = "flowId不能为0!";
								bufferMap.put("infoStr", infoStr);
								bufferMap.put("color", "red");
								this.sbStrJson(buffer, bufferMap);
								break;
							}
						}else {
							bufferMap.put("userId", userId);
							infoStr = "工资项目没有定义!";
							bufferMap.put("infoStr", infoStr);
							bufferMap.put("color", "red");
							this.sbStrJson(buffer, bufferMap);
							break;
						}
					}
				}
			}else {
				for (YHDbRecord record : dbRecords) {
					int fieldCount = record.getFieldCnt();
					String userId = "";
					String updateStr = "";
					int usefulColumn = 0;
					if (fieldCount > 0) {
						for (int i = 0; i < fieldCount; i++) {
							String keyName = record.getNameByIndex(i);
							String value = (String) record.getValueByIndex(i);
							if (!"用户名".equals(keyName.trim()) &&fieldMap.containsKey(keyName.trim())) {
								usefulColumn++;
								updateStr += "S" + fieldMap.get(keyName.trim()) + "='" + value + "',";
							}else if ("用户名".equals(keyName.trim())) {
								userId = value;
							}
						}
						if (updateStr.endsWith(",")) {
							updateStr = updateStr.substring(0, updateStr.length() - 1);
						}
						Map<Object, Object> map = this.getPersonCountByUserName(dbConn, userId);
						boolean isHave = (Boolean) map.get("isHave");
						int personSeqId = (Integer) map.get("personId");
						if (!isHave) {
							String nameStr = "";
							if (!YHUtility.isNullorEmpty(userId)) {
								nameStr = userId;
							}
							infoStr = "员工" + nameStr + "尚未在OA系统中注册！！";
							bufferMap.put("userId", userId);
							bufferMap.put("infoStr", infoStr);
							bufferMap.put("color", "red");
							this.sbStrJson(buffer, bufferMap);
							continue;
						}
					// csv文件需包含有效的工资项目
						if (usefulColumn > 0) {
							if (flowId != 0) {
								sql = "update oa_sal_data set " + updateStr.trim() + " where FLOW_ID='" + flowId + "' and USER_ID='" + personSeqId + "'";
								stmt = dbConn.prepareStatement(sql);
								stmt.executeUpdate();
								infoStr = "员工" + userId + "的工资修改完成！！";
								bufferMap.put("userId", userId);
								bufferMap.put("infoStr", infoStr);
								bufferMap.put("color", "green");
								this.sbStrJson(buffer, bufferMap);
								updateCount++;
							}else {
								bufferMap.put("userId", userId);
								infoStr = "flowId不能为0!";
								bufferMap.put("infoStr", infoStr);
								bufferMap.put("color", "red");
								this.sbStrJson(buffer, bufferMap);
								break;
							}
						}else {
							bufferMap.put("userId", userId);
							infoStr = "工资项目没有定义!";
							bufferMap.put("infoStr", infoStr);
							bufferMap.put("color", "red");
							this.sbStrJson(buffer, bufferMap);
							break;
						}
					}
					
				}
			}
			returnMap.put("isCount", isCount);
			returnMap.put("updateCount", updateCount);
			return returnMap;
			
		} catch (Exception e) {
			throw e;
		}
	}
	public Map<Object, Object> getPersonCountByUserName(Connection dbConn, String userName) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		boolean flag = false;
		int dbSeqId = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT SEQ_ID,USER_ID,USER_NAME from  PERSON where USER_ID=?";
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, YHUtility.null2Empty(userName));
			rs = stmt.executeQuery();
			if (rs.next()) {
				dbSeqId = rs.getInt("SEQ_ID");
				flag = true;
			}
			map.put("isHave", flag);
			map.put("personId", dbSeqId);
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return map;
	}
	
	public String sbStrJson(StringBuffer sb, Map<Object, Object> map) throws Exception {
		String userId = (String) map.get("userId");
		String infoStr = (String) map.get("infoStr");
		String color = (String) map.get("color");
		try {
			sb.append("{");
			sb.append("userId:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(userId)) + "\"");
			sb.append(",infoStr:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(infoStr)) + "\"");
			sb.append(",color:\"" + YHUtility.null2Empty(color) + "\"");
			sb.append("},");
			return sb.toString();
		} catch (Exception e) {
			throw e;
		}
	}
	public void delSalDataByFlowId(Connection dbConn, int flowId) throws Exception {
		PreparedStatement stmt = null;
		String sql = "DELETE FROM oa_sal_data WHERE FLOW_ID =" + flowId;
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, null, log);
		}
	}
	public Map<String,Integer> getSalItemId(Connection dbConn) throws Exception {
		Map<String,Integer> fieldMap = new HashMap<String, Integer>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT SLAITEM_ID,ITEM_NAME from oa_sal_item ORDER BY SLAITEM_ID";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int itemId =rs.getInt("SLAITEM_ID");
				String itemName = YHUtility.null2Empty(rs.getString("ITEM_NAME"));
				fieldMap.put(itemName, itemId);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return fieldMap;
	}
	 /**
   * 查询在一段时间内得到考勤登记信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserDutyInfoLogic(Connection dbConn,HttpServletRequest request,
      HttpServletResponse response,String userId,String days)throws Exception {
    
    String[] dayArray = days.split(",");
    String data = "{trTemp:\"";
    YHManageOutLogic yhaol = new YHManageOutLogic();
    //根据用户得到相应的排班类型

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    //根据userId得到DUTY_TYPE对应的是attend_config表中的SEQ_ID;
    YHPersonLogic personLogic  = new YHPersonLogic();
    YHPerson person = personLogic.getPerson(dbConn, userId);
    int configSeqId = person.getDutyType();
    YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
    YHAttendConfig config = yhadl.selectConfigById(dbConn, String.valueOf(configSeqId));
    String dutyName = config.getDutyName();
    String dutyTime1 = config.getDutyTime1();
    String dutyTime2 = config.getDutyTime2();
    String dutyTime3 = config.getDutyTime3();
    String dutyTime4 = config.getDutyTime4();
    String dutyTime5 = config.getDutyTime5();
    String dutyTime6 = config.getDutyTime6();
    String dutyType1 = config.getDutyType1();
    String dutyType2 = config.getDutyType2();
    String dutyType3 = config.getDutyType3();
    String dutyType4 = config.getDutyType4();
    String dutyType5 = config.getDutyType5();
    String dutyType6 = config.getDutyType6();
    String general = "";
  
    if(config.getGeneral()!=null&& !config.getGeneral().trim().equals("")){
      general = config.getGeneral();
    }
    String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
    //对日期循环


    for(int i = 0; i < dayArray.length; i++){
      Date dateTemp = YHUtility.parseDate(dayArray[i]); 
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(dateTemp);
      int week = calendar.get(Calendar.DAY_OF_WEEK);
      String weekStr = weeks[week-1];
      String trClass = "TableData";
      //判断当天是否是节假日.公休日。


    
 
      data = data + "<tr align='center' class='"+ trClass+"'>" ;
      data = data + "<td align='center' nowrap>" + dayArray[i]+ " (" + weekStr + ")</td>";
      //对排版类型的6循环
      for(int j = 1;j<=6;j++){
        
        String holidayStr = "未登记";
        
        String registerTimeStr="";
        String registerIp="";
        String dutyTime = "";
        String dutyType = "";
        if(j==1){
          dutyTime = dutyTime1;
          dutyType = dutyType1;
        }
        if(j==2){
          dutyTime = dutyTime2;
          dutyType = dutyType2;
        }
        if(j==3){
          dutyTime = dutyTime3;
          dutyType = dutyType3;
        }
        if(j==4){
          dutyTime = dutyTime4;
          dutyType = dutyType4;
        }
        if(j==5){
          dutyTime = dutyTime5;
          dutyType = dutyType5;
        }
        if(j==6){
          dutyTime = dutyTime6;
          dutyType = dutyType6;
        }

        if(dutyTime!=null&&!dutyTime.trim().equals("")){       
    
          //查出当天有没有登记 记录
          List<YHAttendDuty> dutyList =getAttendDuty(request,response,dayArray[i],String.valueOf(j),userId,config);
          String td = "" ;
          if(dutyList.size()>0){
            YHAttendDuty duty = dutyList.get(0);
            Date registerTime = duty.getRegisterTime();
             registerTimeStr = dateFormat.format(registerTime);          
             registerIp = duty.getRegisterIp();
             registerIp="("+registerIp+")";
            int seqId = duty.getSeqId();
            String remark = duty.getRemark();
            
         
            long dutyTimeInt = getLongByDutyTime(dutyTime);
            long registerTimeInt = getLongByDutyTime(registerTimeStr);
            if(dutyType.equals("1")){
              if(dutyTimeInt<registerTimeInt){
                holidayStr = "<span class=big4>迟到</span><br>";
              }
            }else{
              if(dutyTimeInt>registerTimeInt){
                holidayStr = "<span class=big4>早退</span>";
              }
            }
 
          }
            //判断是否外出
            if(isOutTemp(dbConn,request, response, dayArray[i]+" "+dutyTime, userId).equals("1")){
                 holidayStr = "<font color='#00CC33'>外出</font>";
                          
             }
            //判断是否请假
            if(isLeaveTemp(dbConn,request, response, dayArray[i] + " " + dutyTime, userId).equals("1")){
              holidayStr = "<font color='#00CC33'>请假</font>";
            
            }
            //判断是否出差
            if(isEvectionTemp(dbConn,request, response, dayArray[i] + " " + dutyTime, userId).equals("1")){
              holidayStr = "<font color='#00CC33'>出差</font>";
                    
            }
            if(holidayStr.equals("未登记")){
              holidayStr= "<font color='#CCCC66'>未登录</font>";
              if(!"".equals(registerIp)){    
                holidayStr="";
              }
            }
            data = data + "<td align='center' >"+registerTimeStr +registerIp+ holidayStr + "</td>";
          
        }
      }
      data = data + "</tr>";
    } 
    data = data + "\"}";
    
   return data;
  }
 
 
  /**
   * 
   * @param request
   * @param response
   * @param date 时间字符串


   * @param userId
   * @return
   * @throws Exception
   */
  public String isLeaveTemp(Connection dbConn,HttpServletRequest request,
      HttpServletResponse response,String date,String userId) throws Exception {
   
    Statement stmt;
    ResultSet rs;
    String isLeave = "0";
    try {
    
      stmt=dbConn.createStatement();
      String sql="select * from oa_attendance_off where USER_ID='"+userId+"' and ALLOW in ('1','3') and "+YHDBUtility.getDateFilter("LEAVE_DATE1",date, "<=")+" and "+YHDBUtility.getDateFilter("LEAVE_DATE2",date, ">=");
    
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        isLeave="1";
      }
      rs.close();
      stmt.close();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return isLeave;
  }

  
 
  public String isOutTemp(Connection dbConn,HttpServletRequest request,
      HttpServletResponse response,String date,String userId) throws Exception {
   
    String isOut = "0";
    try {
      Statement stmt=dbConn.createStatement();
      ResultSet rs;
      
      //是否为外出    
      String  sql="select * from oa_attendance_out where USER_ID='"+userId+"' and ALLOW in ('1') and "+YHDBUtility.getDateFilter("SUBMIT_TIME",date.substring(0,10)+" 00:00:00", ">=") + " and  "+YHDBUtility.getDateFilter("SUBMIT_TIME",date.substring(0,10)+" 23:59:59", "<=") + " and OUT_TIME1<='"+date.substring(11,16)+"' and OUT_TIME2>='"+date.substring(11,16)+"'";
      rs=stmt.executeQuery(sql);   
    
      if(rs.next()){
        isOut="1";
       
      }
      rs.close();
      stmt.close();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return isOut;
  }
 
  /**
   * 
   * @param request
   * @param response
   * @param date 时间 String 类型
   * @param userId 
   * @return
   * @throws Exception
   */
    public String isEvectionTemp(Connection dbConn,HttpServletRequest request,
        HttpServletResponse response,String date,String userId) throws Exception {
     
      Statement stmt;
      ResultSet rs;
      String isEvection = "0";
      try {
       
        stmt=dbConn.createStatement();
        //是否为出差

       String sql="select * from oa_attendance_trip where USER_ID='"+userId+"' and ALLOW='1' and "+YHDBUtility.getDateFilter("EVECTION_DATE1",date, "<=")+" and "+YHDBUtility.getDateFilter("EVECTION_DATE2",date, ">=");
        rs=stmt.executeQuery(sql);
        if(rs.next()){
          isEvection="1";
        }
        rs.close();
        stmt.close();
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return isEvection;
    }
    /**
     * 得到一天的登记情况
     * @param request
     * @param response
     * @param date 时间字符串


     * @param registerType 登记类型(1-6)
     * @param userId 登记人SeqId
     * @param config 排版类型 
     * @return
     * @throws Exception
     */
    public List<YHAttendDuty> getAttendDuty(HttpServletRequest request,
        HttpServletResponse response,String date,String registerType,String userId,YHAttendConfig config) throws Exception {
      Connection dbConn = null;
      List<YHAttendDuty> dutyList = new ArrayList<YHAttendDuty>();
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        //得到指定当天登记的记录


        YHDBUtility yhdbu = new YHDBUtility();
        YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
        String date1 = date + " 00:00:00";
        String date2 = date + " 23:59:59";
        String DBStr =  yhdbu.curDayFilter("REGISTER_TIME");
        date1 = yhdbu.getDateFilter("REGISTER_TIME", date1, ">=");
        date2 = yhdbu.getDateFilter("REGISTER_TIME", date2, "<=");
        //System.out.println(userId);
        dutyList = yhadl.selectDuty(dbConn, String.valueOf(userId), date1,date2,registerType);
        YHManageAttendLogic attendLogic = new YHManageAttendLogic();
        String dutyName = config.getDutyName();
        String dutyTime1 = config.getDutyTime1();
        String dutyTime2 = config.getDutyTime2();
        String dutyTime3 = config.getDutyTime3();
        String dutyTime4 = config.getDutyTime4();
        String dutyTime5 = config.getDutyTime5();
        String dutyTime6 = config.getDutyTime6();
        String dutyType1 = config.getDutyType1();
        String dutyType2 = config.getDutyType2();
        String dutyType3 = config.getDutyType3();
        String dutyType4 = config.getDutyType4();
        String dutyType5 = config.getDutyType5();
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return dutyList;
    }
    public long getLongByDutyTime(String dutyTime){
      long time = 0;
      String times[] = dutyTime.split(":");
      int length = times.length;
      for (int i = 0; i < times.length; i++) {
        time = time + Long.parseLong(times[i])* (long)(Math.pow(60, length-1-i)) ;
      }
      return time;
    }
	
    /**
     * 日志分页列表
     * @param conn
     * @param request
     * @return
     * @throws Exception
     */
    public String toSearchData(Connection conn,Map request,int userId) throws Exception{
      String sql =  "select SEQ_ID,DIA_TIME,SUBJECT,ATTACHMENT_NAME,ATTACHMENT_ID from oa_journal where 1=1 ";
      //String filters = toSearchWhere(request,userId);
      String filters="";
      String startDateStr = request.get("startDate") != null ? ((String[])request.get("startDate"))[0] : null;
      String endDateStr = request.get("endDate") != null ? ((String[])request.get("endDate"))[0] : null;
      
      filters=" and USER_ID='"+userId+"' and DIA_TYPE!='2' and "+ YHDBUtility.getDateFilter("DIA_DATE",startDateStr,">=")+" and "+YHDBUtility.getDateFilter("DIA_DATE",endDateStr,"<=");
      String query = " order by DIA_DATE desc,DIA_TIME DESC ";
      if(!"".equals(filters)){
        sql += filters;
      }
      sql += query;
  //    System.out.println(sql);
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      
      return pageDataList.toJson();
    }
	
    
    
    //改自舒友林考勤统计方法
    public List getDeptDuty(Connection conn,Map request) throws Exception{
      YHManageQueryLogic logic = new YHManageQueryLogic();
      String query = "SELECT PARA_VALUE from SYS_PARA where PARA_NAME='NO_DUTY_USER'";
      Statement stmt2=null;
      ResultSet rs2=null;
      String paraValue = "";
      try {
        stmt2 = conn.createStatement();
        rs2 = stmt2.executeQuery(query);
        if (rs2.next()) {
          paraValue = rs2.getString("PARA_VALUE");
        }
      }catch(Exception ex){
        throw ex;
      }finally{
        YHDBUtility.close(stmt2, rs2, null);
      } 
      
      
      //StringBuffer sb = new StringBuffer("[");
      List list = new ArrayList();
      String userId = request.get("userId") == null ? null : ((String) request.get("userId"));
      String dutyType = request.get("dutyType") == null ? null : ((String) request.get("dutyType"));
      String startDate = request.get("startDate") == null ? null : ((String) request.get("startDate"));
      String endDate = request.get("endDate") == null ? null : ((String) request.get("endDate"));
      Date start = null;
      Date end = null;
      if (startDate != null) {
        start = YHUtility.parseDate(startDate);
      }
      if (endDate != null) {
        end = YHUtility.parseDate(endDate);
      }
      //---- 取规定上下班时间 -----
       String query2  = "SELECT * from oa_attendance_conf";
       Statement stmt3=null;
       ResultSet rs3=null;
       Map attendConfig = new HashMap();
       Map regCount = new HashMap();
       try {
         stmt3 = conn.createStatement();
         rs3 = stmt3.executeQuery(query2);
         while (rs3.next()) {
           int seqId = rs3.getInt("SEQ_ID");
           String dutyName = rs3.getString("DUTY_NAME");
           String general = rs3.getString("GENERAL");
           
           Map map =new HashMap();
           map.put("SEQ_ID", seqId);
           map.put("DUTY_NAME", dutyName);
           map.put("GENERAL", general);
           map.put("DUTY_TIME1",rs3.getString("DUTY_TIME1"));
           map.put("DUTY_TIME2",rs3.getString("DUTY_TIME2"));
           map.put("DUTY_TIME3",rs3.getString("DUTY_TIME3"));
           map.put("DUTY_TIME4",rs3.getString("DUTY_TIME4"));
           map.put("DUTY_TIME5",rs3.getString("DUTY_TIME5"));
           map.put("DUTY_TIME6",rs3.getString("DUTY_TIME6"));

           map.put("DUTY_TYPE1",rs3.getString("DUTY_TYPE1"));
           map.put("DUTY_TYPE2",rs3.getString("DUTY_TYPE2"));
           map.put("DUTY_TYPE3",rs3.getString("DUTY_TYPE3"));
           map.put("DUTY_TYPE4",rs3.getString("DUTY_TYPE4"));
           map.put("DUTY_TYPE5",rs3.getString("DUTY_TYPE5"));
           map.put("DUTY_TYPE6",rs3.getString("DUTY_TYPE6"));
           
           int c = 0;
           int dutyOnTimes = 0;
           int dutyOffTimes = 0;
           for (int i = 1 ;i <= 6 ;i++) {
             String dutyTimeI = rs3.getString("DUTY_TIME" + i);
             String dutyTypeI = rs3.getString("DUTY_TYPE" + i);
             if (YHUtility.isNullorEmpty(dutyTimeI)) {
               continue;
             }
             c++;
             if ("1".equals(dutyTypeI)) {
               dutyOnTimes++;
             } else {
               dutyOffTimes++;
             }
           }
           regCount.put(seqId + "", c);
           map.put("DUTY_ON_TIMES", dutyOnTimes);
           map.put("DUTY_OFF_TIMES", dutyOffTimes);
           attendConfig.put(seqId + "", map);
         }
       }catch(Exception ex){
         throw ex;
       }finally{
         YHDBUtility.close(stmt3, rs3, null);
       } 
       
       String query3 = "SELECT * from PERSON,USER_PRIV,oa_department where PERSON.NOT_LOGIN='0' and "+ YHDBUtility.findNoInSet(paraValue, "PERSON.SEQ_ID") +" and oa_department.SEQ_ID=PERSON.DEPT_ID ";
//       String deptChildId= "";
//       if(!"ALL_DEPT".equals(dept) 
//           && !YHUtility.isNullorEmpty(dept)){
//         deptChildId=logic.getDeptChildId(conn, dept);
//         if (!"".equals(deptChildId)) {
//           if (deptChildId.endsWith(",")) {
//             deptChildId += dept + ""; 
//           } else {
//             deptChildId = deptChildId + "," + dept ;
//           }
//         } else {
//           deptChildId = dept ;
//         }
//         query3 += " and DEPARTMENT.SEQ_ID in ("+ deptChildId +") ";
//       }
//       if (!"ALL_TYPE".equals(dutyType)) {
//         query3 += " AND DUTY_TYPE = '" + dutyType + "'";
//       }
       if(!YHUtility.isNullorEmpty(userId)){
         query3 += " AND PERSON.SEQ_ID = " + userId;
       }
       query3 += " and PERSON.USER_PRIV=USER_PRIV.SEQ_ID order by DEPT_NO,PRIV_NO,USER_NO,USER_NAME";
       int lineCount = 0;
       
       Statement stmt5=null;
       ResultSet rs5=null;
       int count = 0 ;
       try {
         stmt5 = conn.createStatement();
         rs5 = stmt5.executeQuery(query3);
         Map dutyMap = new HashMap();
         
         while (rs5.next() ) {
           int allHours = 0;
           int allMinites = 0;
           
           
           int seqId = rs5.getInt("SEQ_ID");
           String userName = rs5.getString("USER_NAME");
           String dutyTypeTmp = dutyType = rs5.getString("DUTY_TYPE");
           String userDeptName = rs5.getString("DEPT_NAME");
           
           Map duty = (Map)attendConfig.get(dutyTypeTmp);
           if (duty == null) {
             continue;
           }
           String general = (String)duty.get("GENERAL");
           
           int dutyOnTimes = (Integer)duty.get("DUTY_ON_TIMES");
           int dutyOffTimes =  (Integer)duty.get("DUTY_OFF_TIMES");
           
           lineCount++;
           
           int prefectCount = 0;
           int earlyCount = 0;
           int lateCount = 0 ;
           int dutyOnCount = 0 ;
           int dutyOffCount = 0;
           int dutyOnTotal = 0 ;
           int dutyOffTotal = 0 ;
           int overOnCount = 0 ;
           int overOffCount = 0 ;
           String allHoursMinites = "";
           
           for (Date j = start ;  end.compareTo(j) >= 0 ;  j = new Date(j.getTime() + 24 * 3600 * 1000)) {
             String jj = YHUtility.getDateTimeStr(j);
             int week = j.getDay();
             int holiday = 0 ;
             int holiday1 = 0 ;
             
             if (YHWorkFlowUtility.findId(general, week + "")) {
               holiday = 1;
             }
             if (holiday == 0) {
               String query5 = "select count(*)  from oa_attendance_festival where  "+YHDBUtility.getDateFilter("BEGIN_DATE", jj,"<=") +" and " + YHDBUtility.getDateFilter("END_DATE", jj,">=");
               holiday = logic.getCount( conn , query5);
               if (holiday > 0 ) {
                 holiday1 = 1;
               }
             }
             if (holiday == 0) {
               String query5 = "select count(*) from oa_attendance_trip where   USER_ID='"
                 +seqId+"' and ALLOW='1' and "
                 +  YHDBUtility.getDateFilter("EVECTION_DATE1", jj,"<=") +" and " + YHDBUtility.getDateFilter("EVECTION_DATE2", jj,">=");
               
               query5 = "select count(*) from oa_attendance_trip where USER_ID='"
                 +seqId+"' and ALLOW='1' and "+YHDBUtility.getDateFilter("EVECTION_DATE1", jj,"<=")+" and "+YHDBUtility.getDateFilter("EVECTION_DATE2", jj,">=");
               holiday = logic.getCount( conn , query5);
               if (holiday > 0 ) {
                 holiday1 = 1;
               }
             }
             
             if (holiday == 0) {
               String query5 = "select count(*) from oa_attendance_off where   USER_ID='"
                 +seqId+"' and ALLOW='1' and "
                 +  YHDBUtility.getDateFilter("LEAVE_DATE1", jj,"<=") +" and " + YHDBUtility.getDateFilter("LEAVE_DATE2", jj,">=");
               query5 = "select count(*) from oa_attendance_off where USER_ID='"
                 +seqId+"' and ALLOW='1' and "+YHDBUtility.getSqlSubstr("LEAVE_DATE1", jj,0, 10,"<=")+" and "+YHDBUtility.getSqlSubstr("LEAVE_DATE2", jj,0, 10,">=");
               holiday = logic.getCount( conn , query5);
               if (holiday > 0 ) {
                 holiday1 = 1;
               }
             }
             if (holiday == 0) {
               dutyOnTotal += dutyOnTimes;
               dutyOffTotal += dutyOffTimes;
             }
             int perfectFlag = 0 ;
             String query6 = "SELECT * from oa_attendance_duty where USER_ID='"+seqId+"' and "+YHDBUtility.getDateFilter("REGISTER_TIME", jj,"=")+" order by REGISTER_TIME";
             //query6 = "SELECT * from oa_attendance_duty where USER_ID='"+seqId+"' and to_days(REGISTER_TIME)=to_days('"+jj+"') order by REGISTER_TIME";
             
             int oneDayCount = 0 ;
             Statement stmyh=null;
             ResultSet rs9=null;
             Map oneDayReg = new HashMap();
             try {
               stmyh = conn.createStatement();
               rs9 = stmyh.executeQuery(query6);
               while (rs9.next()) {
                 oneDayCount++;
                 Date d = rs9.getTimestamp("REGISTER_TIME");
                 oneDayReg.put(oneDayCount, d);
                 int dutyTypeCount =(Integer) regCount.get(dutyTypeTmp);
                 if (oneDayCount == dutyTypeCount 
                     && dutyTypeCount %2 == 0 
                     && oneDayCount % 2 == 0
                     && dutyTypeCount > 1
                     && oneDayCount > 1
                   ) {
                   int cc = (Integer)regCount.get(dutyTypeTmp);
                   Date dd = (Date)oneDayReg.get(cc);
                   Date dd2 = (Date)oneDayReg.get(cc - 1);
                   long cha5 = dd.getTime();
                   long cha4 = dd2.getTime();
                   long cha1 = cha5 - cha4;
                   long cha2 = 0 ;
                   long cha3 = 0 ;
                  
                   if ( (Integer)regCount.get(dutyTypeTmp) - 2 > 1) {
                     dd = (Date)oneDayReg.get(cc - 2);
                     dd2 = (Date)oneDayReg.get(cc - 3 );
                     long cha7 = dd.getTime();
                     long cha8 = dd2.getTime();
                     cha2 = cha7 - cha8;
                   } 
                   if ( (Integer)regCount.get(dutyTypeTmp) - 4 > 1) {
                     dd = (Date)oneDayReg.get(cc - 4);
                     dd2 = (Date)oneDayReg.get(cc - 5 );
                     long cha7 = dd.getTime();
                     long cha8 = dd2.getTime();
                     cha3 = cha7 - cha8;
                   } 
                   allMinites += cha1 + cha2 + cha3;
                 }
                 String registerType = rs9.getString("REGISTER_TYPE");
                 String dutyTime = (String)duty.get("DUTY_TIME" + registerType);
                 String dutyType11 = (String)duty.get("DUTY_TYPE" + registerType);
                 if ("".equals(dutyTime)) {
                   continue;
                 }
                 if ("1".equals(dutyType11)) {
                   if (logic.compareTime(d, dutyTime) < 1) {
                     perfectFlag++;
                   }
                   
                   if (holiday > 0 && holiday1 != 1) {
                     overOnCount++;
                     continue;
                   }
                   
                   dutyOnCount++;
                   if  (logic.compareTime(d, dutyTime) == 1) {
                     lateCount++;
                   }
                 }
                 if ("2".equals(dutyType11)) {
                   if (logic.compareTime(d, dutyTime) > -1) {
                     perfectFlag++;
                   }
                   if (holiday > 0 && holiday != 1) {
                     overOffCount++;
                     continue;
                   }
                   dutyOffCount++;
                   if (logic.compareTime(d, dutyTime) == -1) {
                     earlyCount++;
                   }
                 }
               }
             }catch(Exception ex){
               throw ex;
             }finally{
               YHDBUtility.close(stmyh, rs9, null);
             } 
             if (perfectFlag >= dutyOnTimes + dutyOffTimes) {
               prefectCount++;
             }
           }
           
           allMinites = allMinites / 1000;
           allHours = allMinites / 3600 ;
           int hour1 = allMinites % 3600;
           int minite = hour1 / 60;
           if (allHours != 0 || minite != 0) {
             allHoursMinites = allHours + "时" + minite + "分";
           } else {
             allHoursMinites = "0";
           }
           Map mapReturn = new HashMap();
           mapReturn.put("userId", seqId);
           int tmp = dutyOnTotal - dutyOnCount ;
           mapReturn.put("lateCount",  ((tmp < 0 ) ? "0" : tmp));
           tmp = dutyOffTotal - dutyOffCount;
           mapReturn.put("earlyCount", ((tmp < 0 ) ? "0" : tmp));
           list.add(mapReturn);
//           sb.append("{");
//           sb.append("userName:\"" + YHUtility.encodeSpecial(userName) + "\"");
//           sb.append(",deptName:\"" + YHUtility.encodeSpecial(userDeptName) + "\"");
//           sb.append(",perfectCount:\"" + prefectCount+ "\"");
//           sb.append(",allHoursMinites:\"" + allHoursMinites  + "\"");
//           sb.append(",lateCount:\"" + lateCount  + "\"");
//           int tmp = dutyOnTotal - dutyOnCount ;
//           sb.append(",on:" + ((tmp < 0 ) ? "0" : tmp) );
//           sb.append(",earlyCount:" + earlyCount );
//           tmp = dutyOffTotal - dutyOffCount;
//           sb.append(",off:"  + ((tmp < 0 ) ? "0" : tmp) );
//           sb.append(",overOnCount:"  + overOnCount);
//           sb.append(",overOffCount:"  + overOffCount);
//           sb.append(",userId:"  + seqId);
//           sb.append(",date1:\"" + startDate + "\"");
//           sb.append(",date2:\"" + startDate + "\"");
//           sb.append("},");
           count++;
         }
       }catch(Exception ex){
         throw ex;
       }finally{
         YHDBUtility.close(stmt5, rs5, null);
       } 
//       if (count > 0 ) {
//         sb.deleteCharAt(sb.length() - 1);
//       }
//       sb.append("]");
//       return sb.toString();
       return list;
    }
}
