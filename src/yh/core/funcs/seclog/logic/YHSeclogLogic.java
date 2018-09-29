package yh.core.funcs.seclog.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.setting.data.YHHrCode;

public class YHSeclogLogic {

  /**
   * 员工福利  通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getLogLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    
    Map map =this.getParameterMap(request);
    
    String startDate=(String)map.get("startDate");
    String endDate=(String)map.get("endDate");
    String optDesc=(String)map.get("optDesc");
    String userId=(String)map.get("userId");
    String opType=(String)map.get("opType");
    
    try {
      String whereStr="";
      if(!YHUtility.isNullorEmpty(startDate)){
        whereStr+=" and  "+YHDBUtility.getDateFilter("op_time", startDate+" 00:00:00", ">=");
      }
      if(!YHUtility.isNullorEmpty(endDate)){
        whereStr+=" and  "+YHDBUtility.getDateFilter("op_time", endDate+" 23:59:59", "<=");
      }
      if(!YHUtility.isNullorEmpty(optDesc)){
        whereStr+=" and  op_result='"+optDesc+"' ";
      }
      if(!YHUtility.isNullorEmpty(userId)){
        String[] array = userId.split(",");
        String newStr = "";
        for (int i = 0; i < array.length; i++) {
          newStr = newStr + "'" +array[i] + "',";
        }
        if(!newStr.equals("")){
          newStr = newStr.substring(0, newStr.length()-1);
        }
      //  whereStr+=" and  "+YHDBUtility.findInSet(userId, "USER_SEQ_ID");
        whereStr+=" and  USER_SEQ_ID in("+ newStr +  ")";
      }
      if(!YHUtility.isNullorEmpty(opType)){
        whereStr+=" and  op_Type='"+opType+"' ";
      }
      
      
      
      String sql = " select c1.SEQ_ID,c1.USER_SEQ_ID, c1.op_time, c1.client_Ip, c1.op_Type, c1.op_Object,c1.op_result,op_Desc"
                 + " from seclog c1 "
                 + " where 1=1 "+whereStr
                 + " ORDER BY seq_id desc ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
   
      return pageDataList.toJson();
      
    } catch (Exception e) {
      throw e;
    }
  }
  
  
  public ArrayList<YHDbRecord> doExportLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    Statement stmt=null;
    ResultSet rs=null;
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
    Map map =this.getParameterMap(request);
    String startDate=(String)map.get("startDate");
    String endDate=(String)map.get("endDate");
    String optDesc=(String)map.get("optDesc");
    String userId=(String)map.get("userId");
    String opType=(String)map.get("opType");
    
    try {
      String whereStr="";
      if(!YHUtility.isNullorEmpty(startDate)){
        whereStr+=" and  "+YHDBUtility.getDateFilter("op_time", startDate+" 00:00:00", ">=");
      }
      if(!YHUtility.isNullorEmpty(endDate)){
        whereStr+=" and  "+YHDBUtility.getDateFilter("op_time", endDate+" 23:59:59", "<=");
      }
      if(!YHUtility.isNullorEmpty(optDesc)){
        whereStr+=" and  op_result='"+optDesc+"' ";
      }
      if(!YHUtility.isNullorEmpty(userId)){
        whereStr+=" and  "+YHDBUtility.findInSet(userId, "USER_SEQ_ID");
      }
      if(!YHUtility.isNullorEmpty(opType)){
        whereStr+=" and  op_Type='"+opType+"' ";
      }
      String sql = " select c1.SEQ_ID,c1.USER_SEQ_ID,user_name, c1.op_time, c1.client_Ip, c1.op_Type, c1.op_Object,c1.op_result,op_Desc"
                 + " from seclog c1 "
                 + " where 1=1 "+whereStr
                 + " ORDER BY seq_id desc ";
      stmt= dbConn.createStatement();
      rs=stmt.executeQuery(sql);
      while (rs.next()) {
        YHDbRecord record = new YHDbRecord();
        record.addField("管理员",rs.getString("user_name"));
        record.addField("触发时间",rs.getString("op_time"));
        record.addField("操作主机",rs.getString("client_Ip"));
        String opTypeName=getSecLogType(dbConn,rs.getString("op_Type"));
        record.addField("操作类型",opTypeName);
        record.addField("操作实体",rs.getString("op_Object"));
        String opResult = rs.getString("op_result");
        if(opResult.equals("1")){
          opResult="成功";
        }else{
          opResult="失败";
        }
        record.addField("操作结果",opResult);
        record.addField("日志描述",rs.getString("op_Desc"));
        result.add(record);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
    
  }
  
  
  
  public  Map getParameterMap(Map properties) {
    // 返回值Map
    Map returnMap = new HashMap();
    Iterator entries = properties.entrySet().iterator();
    Map.Entry entry;
    String name = "";
    String value = "";
    while (entries.hasNext()) {
      entry = (Map.Entry) entries.next();
      name = (String) entry.getKey();
      Object valueObj = entry.getValue();
      if(null == valueObj){
        value = "";
      }else if(valueObj instanceof String[]){
        String[] values = (String[])valueObj;
        for(int i=0;i<values.length;i++){
          value = values[i] + ",";
        }
        value = value.substring(0, value.length()-1);
      }else{
        value = valueObj.toString();
      }
      returnMap.put(name, value);
    }
    return returnMap;
  }
  
  
  
  /**
   * 查询下一级

   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public  List<YHHrCode> getChildCode(Connection dbConn, String parentNo)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    List<YHHrCode> codeList = new ArrayList<YHHrCode>();
    if (YHUtility.isNullorEmpty(parentNo)) {
      parentNo = "";
    }
    parentNo = parentNo.replaceAll("'", "''");
    String sql = "select * from oa_kind_dict_item where class_no = '"+ parentNo + "' order by sort_no asc";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        YHHrCode code = new YHHrCode();
        code.setSeqId(rs.getInt("SEQ_ID"));
        code.setCodeNo(rs.getString("CLASS_CODE"));
        String proStr=proStrSet(rs.getString("CLASS_CODE"));
        code.setCodeName(proStr+rs.getString("CLASS_DESC"));
        code.setCodeOrder(rs.getString("sort_no"));
        code.setCodeFlag("0");
        codeList.add(code);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return codeList;
  }
  
  public String proStrSet(String desc)throws Exception{
    String str="";
    int len=desc.length()/3;
    for(int i=0;i<len-1;i++){
      str+="－";
      
      
    }
    if(!YHUtility.isNullorEmpty(str)){
      str="〡"+str;
    }
    return str;
  }
  
  /**
   *获取日志类型
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getSecLogType(Connection dbConn, String logType) throws Exception {
     String data="";
     Statement stmt=null;
     ResultSet rs=null;
    try {
      String sql = " select class_desc from oa_kind_dict_item where class_no='seclog' and class_code='"+logType+"' ";
      stmt=dbConn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        data=rs.getString("class_desc");
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return data;
  }

  
  
}
