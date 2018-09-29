package yh.core.funcs.workflow.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowFormType;
import yh.core.funcs.workflow.data.YHFlowPrintTpl;
import yh.core.funcs.workflow.data.YHFlowRun;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.util.YHFlowHookUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;

public class YHFlowPrintLogic {
  /**
   * 解析宏标记


   * @param flowRun
   * @param ft
   * @param form
   * @param conn
   * @return
   * @throws Exception
   */
  public String analysisAutoFlag (int runId , int flowId , String modelShort , Connection conn ) throws Exception {
    YHFlowRunLogic frl = new YHFlowRunLogic();
    YHFlowRun flowRun = frl.getFlowRunByRunId(runId , conn);
    YHFlowTypeLogic ftl = new YHFlowTypeLogic();
    YHFlowType ft = ftl.getFlowTypeById(flowId, conn);
    Date beginTime = flowRun.getBeginTime();
    String date = new SimpleDateFormat("yyyy-MM-dd").format(beginTime);
    String time = new SimpleDateFormat("HH:mm:ss").format(beginTime);
    String runName = flowRun.getRunName();
    runName = YHWorkFlowUtility.getRunName(runName);
    runName = runName.replace("$", "\\$");
    modelShort = modelShort.replaceAll("#\\[文号\\]", runName);
    modelShort = modelShort.replaceAll("#\\[时间\\]", "日期：" + date);
    modelShort = modelShort.replaceAll("#\\[流水号\\]", flowRun.getRunId() + "");
    modelShort = modelShort.replaceAll("#\\[文号计数器\\]", ft.getAutoNum() + "");
    //---------附件链接宏标记------------------
    YHWorkFlowUtility ut = new YHWorkFlowUtility();
    if ( modelShort.indexOf("#[会签意见") != -1) {
      modelShort = ut.getSignInfo(modelShort, flowRun, conn);
    }
    return modelShort;
  }  
  public String savePrintTpl(Connection conn,String tName,String content,String tType,String flowId) throws Exception{
     String seq_id="";
    try{    
      YHFlowPrintTpl tpl=new  YHFlowPrintTpl();
      tpl.setFlowId(Integer.parseInt(flowId));
      tpl.setTName(tName);
      tpl.setContent(content);
      tpl.setTType(tType);
      
      YHORM orm=new YHORM();
      orm.saveSingle(conn, tpl);
      YHFlowHookUtility ut = new YHFlowHookUtility();
      int attendEvectionId = ut.getMax(conn, " select max(SEQ_ID) FROM oa_fl_print_tpl ");
      seq_id=attendEvectionId+"";
      
    }catch( Exception ex){  
      throw ex;
      }
      return seq_id;
  }
  
  
  public void updatePrintTpl(Connection conn,String seqId,String prcsStr,String tName,String content,String tType,String flowId) throws Exception{
    YHORM orm=new YHORM();
    try{    
      
      
      YHFlowPrintTpl tpl=new  YHFlowPrintTpl();
      tpl=(YHFlowPrintTpl)orm.loadObjSingle(conn,YHFlowPrintTpl.class , Integer.parseInt(seqId));
   
      tpl.setTName(tName);
      tpl.setContent(content);
      tpl.setTType(tType);
      tpl.setFlowPrcs(prcsStr);
      orm.updateSingle(conn, tpl);

    }catch( Exception ex){  
      throw ex;
      }finally{
        //YHDBUtility.close(stmt, rs, log)
      }
    
  }
  
  
  
    public void delTplLogic(Connection conn,String seq_id) throws Exception{
         
       try{
         YHORM orm =new YHORM();
        orm.deleteSingle(conn, YHFlowPrintTpl.class, Integer.parseInt(seq_id));
       }catch(Exception ex){
         throw ex;
       }finally{
       //  YHDBUtility.close(stmt, rs, null);
       }
     
    }
  

    public String loadAip(Connection conn,String seq_id) throws Exception{
        YHFlowPrintTpl tpl=new  YHFlowPrintTpl();
     try{
       YHORM orm =new YHORM();
   
       tpl=(YHFlowPrintTpl)orm.loadObjSingle(conn, YHFlowPrintTpl.class, Integer.parseInt(seq_id));
     }catch(Exception ex){
       throw ex;
     }finally{
     //  YHDBUtility.close(stmt, rs, null);
     }
    return YHFOM.toJson(tpl).toString();
  }

public void updateAip(Connection conn,String seqId , String runId , String attachmentId) throws Exception{
  Statement stmt=null;
  ResultSet rs=null;
  String sql = "select AIP_FILES from  oa_fl_run WHERE RUN_ID='"+runId+"'";
  String aipFiles = "";
  try{
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     if(rs.next()){   
       aipFiles = rs.getString("AIP_FILES");
     }
  }catch(Exception ex){
    throw ex;
  }finally{
    YHDBUtility.close(stmt, rs, null);
  }
  aipFiles += seqId + ":"+attachmentId+"\n";
  Statement stmt1=null;
  String sql2 = "update  oa_fl_run set AIP_FILES = '"+ aipFiles +"' WHERE RUN_ID='"+runId+"'";
  try{
     stmt1=conn.createStatement();
     stmt1.executeUpdate(sql2);
  }catch(Exception ex){
    throw ex;
  }finally{
    YHDBUtility.close(stmt1, null, null);
  }
}

    
    public String getTplList(Connection conn,String flow_id) throws Exception{
         Statement stmt=null;
         ResultSet rs=null;
         String data="";
       try{
          String sql=" select * from  oa_fl_print_tpl where flow_id='"+flow_id+"'";
          stmt=conn.createStatement();
          rs=stmt.executeQuery(sql);
          while(rs.next()){
            int seqId=rs.getInt("seq_id");
            String  tName=rs.getString("t_name");
            String tType=rs.getString("t_type");
            String flowPrcs=rs.getString("flow_prcs");
            flowPrcs=getPrcs(conn,flowPrcs,flow_id);
          data+="{seqId:'"+seqId+"',tName:'"+tName+"',tType:'"+tType+"',flowPrcs:'"+flowPrcs+"'}";
          data+=",";
          }
       
       }catch(Exception ex){
         throw ex;
       }finally{
         YHDBUtility.close(stmt, rs, null);
       }
       if(data.endsWith(",")){
         data=data.substring(0,data.length()-1);
       }
       
      return data;
    }
  
    public String getPrcs(Connection conn,String flowPrcs,String flow_id) throws Exception{
      Statement stmt=null;
      ResultSet rs=null;
      String data="";
      if(YHUtility.isNullorEmpty(flowPrcs)){
        return "";
      }
      String prcs[]=flowPrcs.split(",");
    try{
      for(int i=0;i<prcs.length;i++){
         String flowPcs=prcs[i];
         if(!YHUtility.isNullorEmpty(flowPcs)){
         String sql=" select * from  oa_fl_process where flow_seq_id='"+flow_id+"' and prcs_id='"+flowPcs+"' order by prcs_id asc";
         stmt=conn.createStatement();
         rs=stmt.executeQuery(sql);
         if(rs.next()){         
           data+=rs.getString("prcs_name");
           data+=",";
         }
        }
      }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    
    if(data.endsWith(",")){
      data=data.substring(0,data.length()-1);
    }
   return data;
 }
    
    public String getDisSelectByFlowIdLogic(Connection conn,String flow_id) throws Exception{
      Statement stmt=null;
      ResultSet rs=null;
      String data="";
    try{
       String sql=" select * from  oa_fl_process where flow_seq_id='"+flow_id+"' order by prcs_id asc";
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);
       while(rs.next()){
         int prcsId=rs.getInt("prcs_id");
         String  prcsName=rs.getString("prcs_name");
        
       data+="{value:'"+prcsId+"',text:'"+prcsName+"'}";
       data+=",";
       }
    
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(data.endsWith(",")){
      data=data.substring(0,data.length()-1);
    }
    
   return data;
 }
    
    
    public String getSelectByFlowIdLogic(Connection conn,String seq_id) throws Exception{
      Statement stmt=null;
      ResultSet rs=null;
      String data="";
    try{
       String sql=" select * from  oa_fl_print_tpl where seq_id='"+seq_id+"' ";
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);
       if(rs.next()){
         int flow_id=rs.getInt("flow_id");
         String  prcsId=rs.getString("flow_prcs");
         if(!YHUtility.isNullorEmpty(prcsId)){
         String prcs[]=prcsId.split(",");
         for(int i=0;i<prcs.length;i++){
           if(!"".equals(prcs[i])){
            data+="{value:'"+prcs[i]+"',text:'"+this.getPrcs(conn,prcs[i] ,flow_id+"" )+"'}";
            data+=",";
         }
         }
       }
      }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(data.endsWith(",")){
      data=data.substring(0,data.length()-1);
    }
    
   return data;
 }
    
    public String getTempOptionLogic(Connection conn,YHPerson person,String flowId,String runId) throws Exception{
      Statement stmt=null;
      ResultSet rs=null;
      Statement stmt1=null;
      ResultSet rs1=null;
      String data="";
    try{
      String sql = "select SEQ_ID,T_NAME,FLOW_PRCS FROM oa_fl_print_tpl WHERE FLOW_ID='"+flowId+"' and T_TYPE = '1'";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
      while(rs.next())
      {
          String flowPrcs = rs.getString("FLOW_PRCS");
          int seqId=rs.getInt("seq_id");
          String tName=rs.getString("t_name");
          if(YHUtility.isNullorEmpty(flowPrcs)){
            flowPrcs = "0";
          }
           if(flowPrcs.endsWith(",")){
             flowPrcs=flowPrcs.substring(0, flowPrcs.length()-1);
           }
         sql = "select * from oa_fl_run_prcs WHERE RUN_ID='"+runId+"' and USER_ID='"+person.getSeqId()+"' and FLOW_PRCS IN ("+flowPrcs+")";
          stmt1=conn.createStatement();
          rs1=stmt1.executeQuery(sql);
          if(rs1.next()){
            data+="{seqId:'"+seqId+"',tName:'"+tName+"'}";
            data+=",";
          }
            
      }     
    }catch(Exception ex){
      ex.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
      YHDBUtility.close(stmt1, rs1, null);
    }
    if(data.endsWith(",")){
      data=data.substring(0,data.length()-1);
    }
    
   return data;
 }
   
    
    public String getFlowItemData(Connection conn,String flowId,String runId) throws Exception{
      Statement stmt=null;
      ResultSet rs=null;
      String data="";
    try{
     String sql = "select f.title,f.name  FROM oa_fl_type t,oa_fl_form_item f WHERE t.form_seq_id=f.form_id and t.seq_id='"+flowId+"'";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
      while(rs.next())
      {
          String title = YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("title")));
          String name= YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("name")));
          name = this.getDataByFidAndItem(conn, runId, name);
          if(YHUtility.isNullorEmpty(name)){
            name="";
          }
          data+="{name:\""+title+"\",data:\""+name+"\"}";
          data+=",";
      }     
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
    if(data.endsWith(",")){
      data=data.substring(0,data.length()-1);
    }
    
   return data;
 }
    
    public String getDataByFidAndItem(Connection conn,String rId,String Item) throws Exception{
      String data="";
      Statement stmt=null;
      ResultSet rs=null;
      if("run_id".equals(Item) || "RUN_ID".equals(Item)){
        return rId;
      }
      try{
        String sql="select * from oa_fl_run_data where run_id="+rId+" and item_id="+Integer.parseInt(Item.substring(Item.indexOf("_")+1,Item.length()));
        stmt=conn.createStatement();
        rs=stmt.executeQuery(sql);
        if(rs.next()){
          data=YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("item_data")));
        }
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(stmt, rs, null);
      }
      
      return data;
    }
    
    
}
