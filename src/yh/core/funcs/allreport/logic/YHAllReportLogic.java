package yh.core.funcs.allreport.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import yh.core.data.YHDbRecord;
import yh.core.funcs.allreport.data.YHAllReport;
import yh.core.funcs.allreport.data.YHField;
import yh.core.funcs.allreport.data.YHLoadConfigFile;
import yh.core.funcs.person.data.YHPerson;
//import yh.core.funcs.workflow.data.YHFlowReport;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;

public class YHAllReportLogic {
   
  /**
   * 获取左边菜单
   * 
   * */
  public String getMenuList(Connection conn,String actionUrl)throws Exception{
    String data="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" select * from all_report_type  order by seq_id ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      while(rs.next()){
        int seqId=rs.getInt("seq_id");
        String moduleName=rs.getString("module_name");
        data+="{";
        data+="title:'"+moduleName+"',action:getList,extData:'"+seqId+"',data:[],actionUrl:'"+actionUrl+seqId+"'";
        data+="}";
        data+=",";
      }
       
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(data.endsWith(",")){
      data=data.substring(0, data.length()-1);
    }
    return data;
  }
    
  public String getSelectOptionLogic(Connection conn,String rId)  throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    String data="";
    try{
     String sql="select * from all_report_type where seq_id='"+rId+"'";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     String config="";
     if(rs.next()){
       config=rs.getString("config");
     }
     String configFile=YHSysProps.getString("allreportconfig");
     configFile+=config;
     YHLoadConfigFile loadconfigfile=new YHLoadConfigFile(configFile);
     List<YHField> fieldList= loadconfigfile.getFieldList();
     for(int i=0;i<fieldList.size();i++){
       YHField field=fieldList.get(i);
       data+="{";
       data+="value:'"+field.getItem()+"',text:'"+field.getItemDesc()+"'";
       data+="},";
     }
     
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(data.endsWith(",")){
      data=data.substring(0,data.length()-1);
    }
    return data;
  }

  public String getSelectCalOptionLogic(Connection conn,String rId)  throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    String data="";
    try{
     String sql="select * from all_report_type where seq_id='"+rId+"'";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     String config="";
     if(rs.next()){
       config=rs.getString("config");
     }
     String configFile=YHSysProps.getString("allreportconfig");
     configFile+=config;
     YHLoadConfigFile loadconfigfile=new YHLoadConfigFile(configFile);
     List<YHField> fieldList= loadconfigfile.getFieldList();
     for(int i=0;i<fieldList.size();i++){
       YHField field=fieldList.get(i);
      if("CAL".equals(field.getDataType().trim())){
       data+="{";
       data+="value:'"+field.getItem()+"',text:'"+field.getItemDesc()+"'";
       data+="},";
      }
     }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(data.endsWith(",")){
      data=data.substring(0,data.length()-1);
    }
    return data;
  }

  
  
  /**
   * 获取左边子菜单
   * 
   * */
  public String getReportsById(Connection conn,String mId)throws Exception{
    String data="";
    StringBuffer sb=new StringBuffer();
    Statement stmt=null;
    ResultSet rs=null;
    try{
      String sql=" select * from all_report where module_id='"+mId+"' order by seq_id ";
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      while(rs.next()){
        int seqId=rs.getInt("seq_id");
        String rName=rs.getString("r_name");
      sb.append("{title:'" + rName + "'");
      sb.append(",icon:imgPath + '/edit.gif'");
      sb.append(",action:actionFuntion");
      sb.append(",iconAction:iconActionFuntion");
      sb.append(",extData:'" + seqId +":"+mId+"'}," );
      }
       
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    data=sb.toString();
    if(data.endsWith(",")){
      data=data.substring(0, data.length()-1);
    }
    return data;
  }
  
  public void addReportLogic(Connection conn,YHFileUploadForm fileForm,YHPerson person)  throws Exception{

    String flowId = fileForm.getParameter("flow_id");
    String rName = fileForm.getParameter("r_name");
    String listItem = fileForm.getParameter("list_item");
    String queryItem = fileForm.getParameter("query_item");
    String groupType = fileForm.getParameter("group_type");
    String groupField = fileForm.getParameter("group_field");
    try{
      YHAllReport report=new YHAllReport();
      report.setModuleId(Integer.parseInt(flowId));
   
      if(!YHUtility.isNullorEmpty(rName)){
        report.setRName(rName);
      }
      if(!YHUtility.isNullorEmpty(listItem)){
        report.setListItem(listItem);
      }
      if(!YHUtility.isNullorEmpty(queryItem)){
        report.setQueryItem(queryItem);
      }
      if(!YHUtility.isNullorEmpty(groupType)){
        report.setGroupType(groupType);
      }
      if(!YHUtility.isNullorEmpty(groupField)){
        report.setGroupField(groupField);
      }
      report.setCreateUser(person.getSeqId()+"");
      report.setCreateDate(YHUtility.parseDate(YHUtility.getCurDateTimeStr()));
      YHORM orm=new YHORM();
      orm.saveSingle(conn, report);
    }catch(Exception e){
      throw e;
    }
    
  }
  
  public String editReportLogic(Connection conn,String rId)  throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    String data="";
    try{
      String sql="select * from all_report where seq_id="+rId;
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);      
      if(rs.next()){
        int seq_id = rs.getInt("seq_id");
        int module_id = rs.getInt("module_id");
        String r_name   =rs.getString("r_name");
        String list_item = rs.getString("list_item");
        String query_item  = rs.getString("query_item");
        String group_type    = rs.getString("group_type");
        String group_field   = rs.getString("group_field");
        if(!YHUtility.isNullorEmpty(list_item)){
        list_item=hangdleItem(list_item);
        }
        if(!YHUtility.isNullorEmpty(query_item)){
        query_item=hangdleItem(query_item);
        }else {
          query_item="";
        }
        list_item = YHUtility.null2Empty(list_item);
        query_item = YHUtility.null2Empty(query_item);
        
        data+="{rid:'"+seq_id+"',flow_id:'"+module_id+"',r_name:'"+r_name+"',group_field:'"+group_field+"',group_type:'"+group_type+"',listItem:["+list_item+"],queryItem:["+query_item+"]}";
        data+=",";
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(data.endsWith(",")){
      data=data.substring(0,data.length()-1);
    }
    return data;
  }
  
  public String hangdleItem(String item) throws Exception{
    String reStr="";

    String fieldName[]={"field","disp_name","calc_type","formulate","weight"};
    String itemOrg[]=item.split(",");
    for(int i=0;i<itemOrg.length;i++){
      String org[]=itemOrg[i].split("\\`",-1);
      reStr+="{";
      for(int j=0;j<org.length;j++){
        reStr+=fieldName[j]+":'"+org[j]+"'";
        reStr+=",";
      }
      if(reStr.endsWith(",")){
        reStr=reStr.substring(0, reStr.length()-1);
      }
      reStr+="},";     
    }
    if(reStr.endsWith(",")){
      reStr=reStr.substring(0, reStr.length()-1);
    }
    return reStr;
  }  
  
  public void updateReportLogic(Connection conn,YHFileUploadForm fileForm,YHPerson person)  throws Exception{

    String rid = fileForm.getParameter("rid");
    String rName = fileForm.getParameter("r_name");
    String listItem = fileForm.getParameter("list_item");
    String queryItem = fileForm.getParameter("query_item");
    String groupType = fileForm.getParameter("group_type");
    String groupField = fileForm.getParameter("group_field");
    try{
      YHORM orm=new YHORM();
      YHAllReport report=new YHAllReport();
      report =(YHAllReport)orm.loadObjComplex(conn, YHAllReport.class,Integer.parseInt(rid));

      if(!YHUtility.isNullorEmpty(rName)){
        report.setRName(rName);
      }
      if(!YHUtility.isNullorEmpty(listItem)){
        report.setListItem(listItem);
      }
    
        report.setQueryItem(queryItem);

      if(!YHUtility.isNullorEmpty(groupType)){
        report.setGroupType(groupType);
      }
      if(!YHUtility.isNullorEmpty(groupField)){
        report.setGroupField(groupField);
      }
      report.setCreateUser(person.getSeqId()+"");
      report.setCreateDate(YHUtility.parseDate(YHUtility.getCurDateTimeStr()));
     
      orm.updateSingle(conn, report);
    }catch(Exception e){
      throw e;
    }
    
  }
  
  public void delReportByIdLogic(Connection conn,String rid) throws Exception{
    Statement stmt=null;
    try {
      String query = "delete from ALL_REPORT_PRIV WHERE R_ID = " + rid;
      stmt = conn.createStatement();
      stmt.executeUpdate(query);
      YHORM orm = new YHORM();
      YHAllReport report = (YHAllReport) orm.loadObjSingle(conn,YHAllReport.class, Integer.parseInt(rid));
      orm.deleteSingle(conn,report);
   } catch (SQLException e) {
     // TODO Auto-generated catch block
     throw e;
   } finally {
     YHDBUtility.close(stmt, null, null);
   }
  }
  

  
  public String getPrivDept(Connection conn,YHPerson person,String rid) throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    String data="";
    try{
      String sql="select * from oa_fl_report_priv where rid="+rid+" and "+YHDBUtility.findInSet(person.getSeqId()+"", "user_str");
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      String dd="";
        while(rs.next()){
          dd=rs.getString("dept_str");
          if(dd .equals("ALL_DEPT") )
          {
            data = "0";
            break;
          }
          else if(dd .equals("DEPT") )
            data += person.getDeptId()+",";
          else{
            if(!data.endsWith(",")){
              data+=",";
            }
            data += dd;
          }      
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(data.startsWith(",")){
      data=data.substring(1, data.length());
    }
    if(data.endsWith(",")){
      data=data.substring(0, data.length()-1);
    }
    return data;
  }
  
 
}
