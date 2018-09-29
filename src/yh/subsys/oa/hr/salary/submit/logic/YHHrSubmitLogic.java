package yh.subsys.oa.hr.salary.submit.logic;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
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

public class YHHrSubmitLogic {
  private static Logger log = Logger.getLogger(YHHrSubmitLogic.class);
   
  /**
   * 工资代办流程  通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getSubmitJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
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
    String sql = " SELECT SEQ_ID,ITEM_NAME from oa_sal_item where ISCOMPUTER = 0 and ISREPORT = 0 order by SLAITEM_ID ";
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
   * 根据部门id查找所有属于这个部门的人员
   * @param dbConn
   * @param user
   * @param seqId
   * @return
   * @throws Exception
   */
  public List<YHSalPerson> getDeptPersonNameLogic(Connection dbConn, YHPerson user,String deptId, String flowId) throws Exception {
    
    String sql = " select SLAITEM_ID SID from oa_sal_item "
               + " where ISCOMPUTER = 0 and ISREPORT = 0 "
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
  public void setSubmitInfo(Connection dbConn, Map<String,String> request, YHPerson person) throws Exception {
    
    String flowId = request.get("flowId");
    String totalStr = request.get("total");
    String titleStr = request.get("title");
    if("".equals(titleStr)){
    	return;
    }
    titleStr = titleStr.substring(0, titleStr.length()-1);
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
          Slist.add(request.get(titleList[j]+"_"+i));
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
          sql = " insert into oa_sal_data(FLOW_ID,USER_ID,INSURANCE_OTHER," + titleStr + ") values(" + flowId + "," + userId + ",1," + Slist.toString().substring(1, Slist.toString().length()-1) + ") ";
        }
        ps = dbConn.prepareStatement(sql);
        ps.executeUpdate();     
      }
    } catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps, rs, null);
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
    String sql = " SELECT SEQ_ID,ITEM_NAME from oa_sal_item order by SLAITEM_ID ";//where ISCOMPUTER = 0 and ISREPORT = 0
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
              // + " where ISCOMPUTER = 0 and ISREPORT = 0 "
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
      sb.append("(select ALL_BASE from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") ALL_BASE ,");
      sb.append("(select PENSION_BASE from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") PENSION_BASE ,");
      sb.append("(select PENSION_U from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") PENSION_U ,");
      sb.append("(select PENSION_P from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") PENSION_P ,");
      sb.append("(select MEDICAL_BASE from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") MEDICAL_BASE ,");
      sb.append("(select MEDICAL_U from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") MEDICAL_U ,");
      sb.append("(select MEDICAL_P from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") MEDICAL_P ,");
      sb.append("(select FERTILITY_BASE from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") FERTILITY_BASE ,");
      sb.append("(select FERTILITY_U from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") FERTILITY_U ,");
      sb.append("(select UNEMPLOYMENT_BASE from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") UNEMPLOYMENT_BASE ,");
      sb.append("(select UNEMPLOYMENT_U from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") UNEMPLOYMENT_U ,");
      sb.append("(select UNEMPLOYMENT_P from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") UNEMPLOYMENT_P ,");
      sb.append("(select INJURIES_BASE from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") INJURIES_BASE ,");
      sb.append("(select INJURIES_U from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") INJURIES_U ,");
      sb.append("(select HOUSING_BASE from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") HOUSING_BASE ,");
      sb.append("(select HOUSING_U from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") HOUSING_U ,");
      sb.append("(select HOUSING_P from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") HOUSING_P ,");
      String salData = "(select INSURANCE_DATE from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") INSURANCE_DATE ,"
                     + "(select MEMO from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") MEMO ,"
                     + "(select INSURANCE_OTHER from oa_sal_data h1 where h1.USER_ID= p1.SEQ_ID and h1.FLOW_ID=" + flowId + ") INSURANCE_OTHER ";
      
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
          String sqlTemp = " select (select SEQ_ID from oa_pm_salary_data h1 where h1.USER_ID= p1.SEQ_ID) HSD_ID ," + sb.substring(0, sb.length()-1).replace("oa_sal_data", "oa_pm_salary_data").replace("and h1.FLOW_ID=" + flowId, "")
                         + " from PERSON p1 "
                         + " join USER_PRIV u1 on u1.SEQ_ID = p1.USER_PRIV "
                         + " where p1.SEQ_ID = " + userId 
                         + " order by u1.PRIV_NO ";
          stmt = dbConn.prepareStatement(sqlTemp);
          rsTemp = stmt.executeQuery();   
          if(rsTemp.next()){
            salPerson.setHsdId(rsTemp.getInt("HSD_ID"));
            salPerson.setAllBase(rsTemp.getDouble("ALL_BASE"));
            salPerson.setPensionBase(rsTemp.getDouble("PENSION_BASE"));
            salPerson.setPensionU(rsTemp.getDouble("PENSION_U"));
            salPerson.setPensionP(rsTemp.getDouble("PENSION_P"));
            salPerson.setMedicalBase(rsTemp.getDouble("MEDICAL_BASE"));
            salPerson.setMedicalU(rsTemp.getDouble("MEDICAL_U"));
            salPerson.setMedicalP(rsTemp.getDouble("MEDICAL_P"));
            salPerson.setFertilityBase(rsTemp.getDouble("FERTILITY_BASE"));
            salPerson.setFertilityU(rsTemp.getDouble("FERTILITY_U"));
            salPerson.setUnemploymentBase(rsTemp.getDouble("UNEMPLOYMENT_BASE"));
            salPerson.setUnemploymentU(rsTemp.getDouble("UNEMPLOYMENT_U"));
            salPerson.setUnemploymentP(rsTemp.getDouble("UNEMPLOYMENT_P"));
            salPerson.setInjuriesBase(rsTemp.getDouble("INJURIES_BASE"));
            salPerson.setInjuriesU(rsTemp.getDouble("INJURIES_U"));
            salPerson.setHousingBase(rsTemp.getDouble("HOUSING_BASE"));
            salPerson.setHousingU(rsTemp.getDouble("HOUSING_U"));
            salPerson.setHousingP(rsTemp.getDouble("HOUSING_P"));
            Map<String,Double> smap = new HashMap<String,Double>();
            List<String> slist = new ArrayList<String>();
            for(int i = 0; i < sid.size(); i++){
              smap.put(sid.get(i), rsTemp.getDouble(sid.get(i)));
              slist.add(i, (String)sid.get(i));
            }
            salPerson.setSmap(smap);
            salPerson.setSlist(slist);
            salPerson.setInsuranceDate(null);
            salPerson.setMemo("");
            salPerson.setInsuranceOther("1");
          }
        }
        else{
          salPerson.setAllBase(rs.getDouble("ALL_BASE"));
          salPerson.setPensionBase(rs.getDouble("PENSION_BASE"));
          salPerson.setPensionU(rs.getDouble("PENSION_U"));
          salPerson.setPensionP(rs.getDouble("PENSION_P"));
          salPerson.setMedicalBase(rs.getDouble("MEDICAL_BASE"));
          salPerson.setMedicalU(rs.getDouble("MEDICAL_U"));
          salPerson.setMedicalP(rs.getDouble("MEDICAL_P"));
          salPerson.setFertilityBase(rs.getDouble("FERTILITY_BASE"));
          salPerson.setFertilityU(rs.getDouble("FERTILITY_U"));
          salPerson.setUnemploymentBase(rs.getDouble("UNEMPLOYMENT_BASE"));
          salPerson.setUnemploymentU(rs.getDouble("UNEMPLOYMENT_U"));
          salPerson.setUnemploymentP(rs.getDouble("UNEMPLOYMENT_P"));
          salPerson.setInjuriesBase(rs.getDouble("INJURIES_BASE"));
          salPerson.setInjuriesU(rs.getDouble("INJURIES_U"));
          salPerson.setHousingBase(rs.getDouble("HOUSING_BASE"));
          salPerson.setHousingU(rs.getDouble("HOUSING_U"));
          salPerson.setHousingP(rs.getDouble("HOUSING_P"));
          Map<String,Double> smap = new HashMap<String,Double>();
          List<String> slist = new ArrayList<String>();
          for(int i = 0; i < sid.size(); i++){
            smap.put(sid.get(i), rs.getDouble(sid.get(i)));
            slist.add(i, (String)sid.get(i));
          }
          salPerson.setSmap(smap);
          salPerson.setSlist(slist);
          salPerson.setInsuranceDate(rs.getDate("INSURANCE_DATE"));
          salPerson.setMemo(rs.getString("MEMO"));
          salPerson.setInsuranceOther(rs.getString("INSURANCE_OTHER"));
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
   * 获取计算项公式

   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public String getFormula(Connection dbConn, String name) throws Exception {
    String data = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    name = name.substring(1, name.length());
    String sql = " select FORMULANAME from oa_sal_item where SLAITEM_ID =" + name;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery(); 
      if(rs.next()){
        data = rs.getString("FORMULANAME");
      }
    } catch (Exception e) {
      throw e;
    }
    return data;
  }
  
  /**
   * 指定设置人员财务工资
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void setSubmitInfoUser(Connection dbConn, Map<String,String> request, YHPerson person) throws Exception {
    int insuranceOther = 0;
    if(request.get("insuranceOther") != null){
      insuranceOther = 1;
    }
    String allBase = "0.00";
    String pensionBase = "0.00";
    String pensionU = "0.00";
    String pensionP = "0.00";
    String medicalBase = "0.00";
    String medicalU = "0.00";
    String medicalP = "0.00";
    String fertilityBase = "0.00";
    String fertilityU = "0.00";
    String unemploymentBase = "0.00";
    String unemploymentU = "0.00"; 
    String unemploymentP = "0.00";
    String injuriesBase = "0.00";
    String injuriesU = "0.00";
    String housingBase = "0.00"; 
    String housingU = "0.00"; 
    String housingP = "0.00"; 
    if(insuranceOther == 1){
      allBase = request.get("allBase");
      pensionBase = request.get("pensionBase");
      pensionU = request.get("pensionU");
      pensionP = request.get("pensionP");
      medicalBase = request.get("medicalBase");
      medicalU = request.get("medicalU");
      medicalP = request.get("medicalP");
      fertilityBase = request.get("fertilityBase");
      fertilityU = request.get("fertilityU");
      unemploymentBase = request.get("unemploymentBase");
      unemploymentU = request.get("unemploymentU");
      unemploymentP = request.get("unemploymentP");
      injuriesBase = request.get("injuriesBase");
      injuriesU = request.get("injuriesU");
      housingBase = request.get("housingBase");
      housingU = request.get("housingU");
      housingP = request.get("housingP");
    }

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
        sql += " ALL_BASE = " + allBase
        		 + ",PENSION_BASE = " + pensionBase
        		 + ",PENSION_U = " + pensionU
        		 + ",PENSION_P = " + pensionP
        		 + ",MEDICAL_BASE = " + medicalBase
        		 + ",MEDICAL_U = " + medicalU
        		 + ",MEDICAL_P = " + medicalP
        		 + ",FERTILITY_BASE = " + fertilityBase
        		 + ",FERTILITY_U = " + fertilityU
        		 + ",UNEMPLOYMENT_BASE = " + unemploymentBase
        		 + ",UNEMPLOYMENT_U = " + unemploymentU 
        		 + ",UNEMPLOYMENT_P = " + unemploymentP
        		 + ",INJURIES_BASE = " + injuriesBase
        		 + ",INJURIES_U = " + injuriesU
        		 + ",HOUSING_BASE = " + housingBase
        		 + ",HOUSING_U = " + housingU
        		 + ",HOUSING_P = " + housingP
        		 + ",INSURANCE_DATE = ? " 
        		 + ",INSURANCE_OTHER = " + insuranceOther
        		 + ",MEMO = ?"
             + " where SEQ_ID=" + rs.getString("SEQ_ID");
      }
      else{
        sql = " insert into oa_sal_data(" 
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
        		+ ",INSURANCE_DATE" 
        		+ ",INSURANCE_OTHER" 
        		+ ",MEMO) " 
        		+ " values(" + flowId + "," + userId + "," + Slist.toString().substring(1, Slist.toString().length()-1)
        		+ "," + allBase
            + "," + pensionBase
            + "," + pensionU
            + "," + pensionP
            + "," + medicalBase
            + "," + medicalU
            + "," + medicalP
            + "," + fertilityBase
            + "," + fertilityU
            + "," + unemploymentBase
            + "," + unemploymentU 
            + "," + unemploymentP
            + "," + injuriesBase
            + "," + injuriesU
            + "," + housingBase 
            + "," + housingU 
            + "," + housingP 
            + ",?"
            + "," + insuranceOther
            + ",?) ";
      }
      ps = dbConn.prepareStatement(sql);
      if(YHUtility.isNullorEmpty(request.get("insuranceDate")) || insuranceOther == 0){
        ps.setTimestamp(1, null);
      }
      else{
        ps.setTimestamp(1, YHUtility.parseTimeStamp(request.get("insuranceDate")));
      }
      ps.setString(2, request.get("memo"));
      ps.executeUpdate();     
    } catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps, rs, null);
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
  public String getSubmitHistoryListJson(Connection dbConn, Map request, YHPerson person) throws Exception {
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
	public Map<Object, Object> impSubmitInfoByCsvLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person, StringBuffer buffer)
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
			String computeStr = fileForm.getParameter("compute");
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
			//计算值
			if ("1".equalsIgnoreCase(computeStr)) {
				
				
				
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
	 * 计算值
	 * @param dbConn
	 * @param flowId
	 * @throws Exception
	 */
	public void computAllitem(Connection dbConn,int flowId) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT * from oa_sal_data where FLOW_ID='" + flowId + "'";
		try {
			Map<Object, Object> fornulaMap = this.getFormula(dbConn);
			
			
			
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
	}
	
	public Map<Object, Object> getFormula(Connection dbConn) throws Exception{
		Map<Object, Object> map = new HashMap<Object, Object>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT  FORMULA,ISCOMPUTER, SLAITEM_ID from oa_sal_item   order by SLAITEM_ID";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				String formula = YHUtility.null2Empty(rs.getString("FORMULA"));
				String iscomputer = YHUtility.null2Empty(rs.getString("ISCOMPUTER"));
				int slaitemId = rs.getInt("SLAITEM_ID");
				if ("1".equals(iscomputer)) {
					map.put(slaitemId, formula);
				}
			}
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		return map;
	}
	
	
	
	
	
	
}
