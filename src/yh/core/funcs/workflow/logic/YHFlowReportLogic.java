package yh.core.funcs.workflow.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;

import yh.core.data.YHDbRecord;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowReport;
import yh.core.funcs.workflow.data.YHFlowReportPriv;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;

public class YHFlowReportLogic {
   public String getReportLogic(Connection conn,String fId)  throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String data="";
     try{
       String sql="select seq_id,r_name,createuser,createdate,group_type  from oa_fl_report  where flow_id="+fId+" order by seq_id desc";
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);
       while(rs.next()){
         int rId=rs.getInt("seq_id");
         String rName=rs.getString("r_name");
         String createUser=rs.getString("createuser");
         String createDate=rs.getString("createdate");
         String groupType=rs.getString("group_type");
         data+="{rId:'"+rId+"',rName:'"+rName+"',createUser:'"+getUserNameById(conn,createUser)+"',createDate:'"+createDate.substring(0, 10)+"',groupType:'"+groupType+"'}";
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
   
   public String getUserNameById(Connection conn,String id) throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String name="";
     try {
       String sql="select user_name from person where seq_id="+id;
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        name=rs.getString("user_name");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }

     return name;
   }

   public void delReportByIdLogic(Connection conn,String rid) throws Exception{
     Statement stmt=null;
     try {
       String query = "delete from oa_fl_report_priv WHERE RID = " + rid;
       stmt = conn.createStatement();
       stmt.executeUpdate(query);
       YHORM orm = new YHORM();
       YHFlowReport report = (YHFlowReport) orm.loadObjSingle(conn,YHFlowReport.class, Integer.parseInt(rid));
       orm.deleteSingle(conn,report);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, null);
    }
   }
   
   public void delReportPrivByIdLogic(Connection conn,String pid) throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     try {
       YHORM orm = new YHORM();
       YHFlowReportPriv report = (YHFlowReportPriv) orm.loadObjSingle(conn,YHFlowReportPriv.class, Integer.parseInt(pid));
       orm.deleteSingle(conn,report);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }

   }
   
   public String getListItemLogic(Connection conn,String fId)  throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String data="";
     try{
       String sql="select form_seq_id from oa_fl_type where seq_id="+fId;
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);
       int form_id=0;
       if(rs.next()){
         form_id=rs.getInt("form_seq_id");
       }
       sql="select title,name from oa_fl_form_item where form_id="+form_id+" order by item_id asc";
       rs=stmt.executeQuery(sql);
       while(rs.next()){
         String text=rs.getString("title");
         String value=rs.getString("name");      
         data+="{text:'"+text+"',value:'"+value+"'}";
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

   public String getFidByRidLogic(Connection conn,String rId)  throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String data="";
     try{
       String sql="select flow_id from oa_fl_report where seq_id="+rId;
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);
       int form_id=0;
       if(rs.next()){
         form_id=rs.getInt("flow_id");
       }
       data=form_id+"";
     }catch(Exception e){
       throw e;
     }finally{
       YHDBUtility.close(stmt, rs, null);
     }
     return data;
   }

   public String getReportPrivByRidLogic(Connection conn,String rId)  throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String data="";
     try{
       String sql="select * from oa_fl_report_priv where rid="+rId;
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);
       while(rs.next()){
        int seqId=rs.getInt("seq_id"); 
        String userStr=rs.getString("user_str");
        String deptStr=rs.getString("dept_str");
        userStr=this.getUserNamesbyIds(conn,userStr);
        deptStr=this.getDeptNamesbyIds(conn,deptStr);
       data+="{pId:'"+seqId+"',userStr:'"+userStr+"',deptStr:'"+deptStr+"'}";
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
   
   public String getReportPrivByPidLogic(Connection conn,String id)  throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String data="";
     try{
       String sql="select * from oa_fl_report_priv where seq_id="+id;
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);
       while(rs.next()){
        int seqId=rs.getInt("seq_id"); 
        String userStr=rs.getString("user_str");
        String deptStr=rs.getString("dept_str");
       data+="{pId:'"+seqId+"',userIds:'"+userStr+"',userStr:'"+this.getUserNamesbyIds(conn,userStr)+"',dept:'"+deptStr+"',deptStr:'"+this.getDeptNamesbyIds(conn,deptStr)+"'}";
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
   
   public String getReportByRidLogic(Connection conn,String id,YHPerson person)  throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String data="";
     String dept="";
     String query="";
     String dd="";
     try{
      String sql="select * from oa_fl_report_priv where rid="+id+" and "+YHDBUtility.findInSet(person.getSeqId()+"", "user_str");

      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      while(rs.next()){
        dd=rs.getString("dept_str");
        if(dd .equals("ALL_DEPT") )
        {
          dept = "ALL_DEPT";
          break;
        }
        else if(dd .equals("DEPT") )
          dept += person.getDeptId()+",";
        else{
          if(!dept.endsWith(",")){
            dept+=",";
          }
          dept += dd;
        }
      }
      YHFlowReport report=new YHFlowReport();
      YHORM orm=new YHORM();
      report=(YHFlowReport)orm.loadObjSingle(conn,YHFlowReport.class,Integer.parseInt(id));
      query = report.getQueryItem();
     
      String item="";
      String name="";
      String data1="";
      if(!YHUtility.isNullorEmpty(query)){
      String org[]=query.split(",");
      for(int i=0;i<org.length;i++){
        item="";
        name="";
        if(!"".equals(org[i])){
          item=org[i].substring(0,org[i].indexOf("`"));
          name=org[i].substring(org[i].indexOf("`")+1,org[i].length());
        }
        data1+="{item:'"+item+"',name:'"+name+"'}";
        data1+=",";
       }
      }
      if(data1.endsWith(",")){
        data1=data1.substring(0, data1.length()-1);
      }
      data="{dept:'"+dept+"',query:["+data1+"]}";
     }catch(Exception e){
       throw e;
     }finally{
       YHDBUtility.close(stmt, rs, null);
     }
     return data;
   }
   
   public String getUserNamesbyIds(Connection conn,String ids) throws Exception{
     String names="";
     String id[]=ids.split(",");
     for(int i=0;i<id.length;i++){
       if(!"".equals(id[i])){
       names+=this.getUserNameById(conn,id[i]);
       names+=",";
     }
    }
     if(names.endsWith(",")){
       names=names.substring(0,names.length()-1);
     }
     return names;
   }
   
   public String getDeptNamesbyIds(Connection conn, String ids){
     String names="";
     if("ALL_DEPT".equals(ids)) {
       return "全体部门";
     }else if("DEPT".equals(ids)){
       return "本部门";
     }
     else {
      String dept[]=ids.split(",");
      for(int i=0;i<dept.length;i++){
        if(!"".equals(dept[i])){
        names+=getDeptNameById(conn,dept[i]);
        names+=",";
       }
      } 
    }
     if(names.endsWith(",")){
       names=names.substring(0,names.length()-1);
     }
     return names;
   }
   
   public String getDeptNameById(Connection conn,String id){
     Statement stmt=null;
     ResultSet rs=null;
     String name="";
     try {
       String sql="select dept_name from oa_department where seq_id="+id;
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        name=rs.getString("dept_name");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }

     return name;
   }
   
   public String editReportLogic(Connection conn,String rId)  throws Exception{
     Statement stmt=null;
     ResultSet rs=null;
     String data="";
     try{
       String sql="select * from oa_fl_report where seq_id="+rId;
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);      
       if(rs.next()){
         int seq_id = rs.getInt("seq_id");
         int flow_id = rs.getInt("flow_id");
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
         
         data+="{rid:'"+seq_id+"',flow_id:'"+flow_id+"',r_name:'"+r_name+"',group_field:'"+group_field+"',group_type:'"+group_type+"',listItem:["+list_item+"],queryItem:["+query_item+"]}";
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
 
 public void addReportLogic(Connection conn,YHFileUploadForm fileForm,YHPerson person)  throws Exception{

     String flowId = fileForm.getParameter("flow_id");
     String tid = fileForm.getParameter("tid");
     String formId = fileForm.getParameter("form_id");
     String rName = fileForm.getParameter("r_name");
     String listItem = fileForm.getParameter("list_item");
     String queryItem = fileForm.getParameter("query_item");
     String groupType = fileForm.getParameter("group_type");
     String groupField = fileForm.getParameter("group_field");
     try{
       YHFlowReport report=new YHFlowReport();
       report.setFlowId(Integer.parseInt(flowId));
       if(!YHUtility.isNullorEmpty(tid)){
       report.setTid(Integer.parseInt(tid));
       }
       if(!YHUtility.isNullorEmpty(formId)){
         report.setFlowId(Integer.parseInt(flowId));
       }
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
       report.setCreateuser(person.getSeqId()+"");
       report.setCreatedate(YHUtility.parseDate(YHUtility.getCurDateTimeStr()));
       YHORM orm=new YHORM();
       orm.saveSingle(conn, report);
     }catch(Exception e){
       throw e;
     }
     
   }
   
 public void addReportPrivLogic(Connection conn,YHFileUploadForm fileForm,YHPerson person)  throws Exception{

   String rid = fileForm.getParameter("rid");
   String user_str = fileForm.getParameter("userId");
   String dept_str = fileForm.getParameter("deptvalue");

   try{
     YHFlowReportPriv priv=new YHFlowReportPriv();
     priv.setRid(Integer.parseInt(rid));
     priv.setUserStr(user_str);
     priv.setDeptStr(dept_str);
     YHORM orm=new YHORM();
     orm.saveSingle(conn, priv);
   }catch(Exception e){
     throw e;
   }
   
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
     YHFlowReport report=new YHFlowReport();
     report =(YHFlowReport)orm.loadObjComplex(conn, YHFlowReport.class,Integer.parseInt(rid));

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
     report.setCreateuser(person.getSeqId()+"");
     report.setCreatedate(YHUtility.parseDate(YHUtility.getCurDateTimeStr()));
    
     orm.updateSingle(conn, report);
   }catch(Exception e){
     throw e;
   }
   
 }
 public void updateReportPrivLogic(Connection conn,YHFileUploadForm fileForm,YHPerson person)  throws Exception{

   String pid = fileForm.getParameter("pid");
   String user_str = fileForm.getParameter("userId");
   String dept_str = fileForm.getParameter("deptvalue");
   try{
     YHORM orm=new YHORM();
     YHFlowReportPriv priv=new YHFlowReportPriv();
     priv =(YHFlowReportPriv)orm.loadObjComplex(conn, YHFlowReportPriv.class,Integer.parseInt(pid));
     priv.setUserStr(user_str);
     priv.setDeptStr(dept_str);
     orm.updateSingle(conn, priv);
   }catch(Exception e){
     throw e;
   }
   
 }
 
 public String getReportListLogic(Connection conn,YHPerson user)  throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   String data="";
   try{
     String sql="select seq_id ,sort_name from oa_fl_sort";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     while(rs.next()){
     int seq_id=rs.getInt("seq_id");
     String sort_name=rs.getString("sort_name");
     String list="";
     list=this.getListById(conn,seq_id);
     data+="{seqId:'"+seq_id+"',sortName:'"+sort_name+"',list:["+list+"]}";
     data+=",";
     }
   
   }catch(Exception e){
     throw e;
   } finally{
     YHDBUtility.close(stmt, rs, null);
   }
   if(data.endsWith(",")){
     data=data.substring(0,data.length()-1);
   }
   return data;
 }
 
 public  String getTableListLogic(Connection conn,YHPerson user,Map request, int versionNo)  throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   List<List<String>>  querylist=new ArrayList();
   LinkedList<String> datalist=new LinkedList<String>();
   String data="";
   String startDate = request.get("startDate") == null ? null : ((String[]) request.get("startDate"))[0];
   String endDate = request.get("endDate") == null ? null : ((String[]) request.get("endDate"))[0];
   String dept = request.get("dept") == null ? null : ((String[]) request.get("dept"))[0];
   String query = request.get("query") == null ? null : ((String[]) request.get("query"))[0];
   String rid = request.get("rid") == null ? null : ((String[]) request.get("rid"))[0];

   try{
     String sql="select * from oa_fl_report where seq_id="+rid;
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     int flowId=0;
     int rId=0;
     String list_item="";
     String query_item="";
     String group_type="";
     String group_field="";
   
     if(rs.next()){
       flowId=rs.getInt("flow_id");
       rId           = rs.getInt("seq_id");
       list_item     = rs.getString("list_item");
       query_item    = rs.getString("query_item");
       group_type    = rs.getString("group_type");
       group_field   = rs.getString("group_field");   
     }
     datalist=this.getFieldNameByRidForExcel(conn, group_field, flowId+"", list_item,sql,rid , versionNo);  
     querylist.add(datalist);
     
     String deptStr="";
    if("".equals(dept) || dept==null){
       dept=getPrivDept(conn,user,rid);
     }
     if(!"".equals(dept) && !"0".equals(dept)){
       deptStr=" and begin_user in ( select seq_id from person where 1=1 and dept_id in ("+dept+"))";
     }         
     sql="select run_id  from oa_fl_run  where 1=1 "+deptStr+" AND FORM_VERSION = "+ versionNo +" and flow_id="+flowId+" and "+YHDBUtility.getDateFilter("begin_time",startDate+" 00:00:00",">=") +" and "+YHDBUtility.getDateFilter("begin_time", endDate+" 23:59:59", "<=");    
     sql+=" order by seq_id asc";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     String itemData="";
     String flag="0";
     while(rs.next()){
      int run_id=rs.getInt("run_id");
        if(this.getQueryStr(conn,run_id+"",rid,query, request , flowId))
        {
          datalist=this.getItemDataList(conn,list_item,run_id+"",group_field , flowId);
          querylist.add(datalist);
        }
     }

    querylist=querylist.subList(1, querylist.size());
    
    if("0".equals(group_type)){
      querylist=this.getGroupCalListLogic(list_item,querylist);
     }else if("1".equals(group_type)){
       querylist=this.getGroupListLogic(querylist);
     }
    querylist=this.queryDistinctGroupField(querylist);
    
    List<String> calLine=new ArrayList();
    if("0".equals(group_type)){
     calLine = this.getCalLine(querylist,list_item);
     querylist.add(calLine);
    }
    
 
    
    itemData=getTableStr(querylist,group_type);
    String ItemName=this.getFieldNameByRid(conn, group_field, flowId+"", list_item,rid,group_type,querylist);   
    data="<table border='0' width='85%' class='TableList'  align='center'>"+ItemName+itemData+"</table>";
    data=YHUtility.encodeSpecial(data);
    if(!"".equals(itemData)){
      flag="1";
    }
    data="{table:'"+data+"',flag:'"+flag+"'}";
   }catch(Exception e){
     throw e;
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }

   return data;
 }
 
 /**
  * 去掉重复统计
  * */
 
 public List<List<String>> queryDistinctGroupField(List<List<String>> dataList) throws Exception{
   
   
   for(int i=0;i<dataList.size();i++){
     List<String> list = dataList.get(i);
     String groupStr=list.get(0);
     for(int j=i+1;j<dataList.size();j++){
       List<String> list1=dataList.get(j);
       String groupStr1=list1.get(0);
       if(groupStr.equals(groupStr1)){
         dataList.remove(j);
       }
     }
     
   }
   
  
   return dataList;
 }
 public List<List<String>> getGroupCalListLogic(String list_item,List<List<String>> list)throws Exception{
   list_item = YHUtility.null2Empty(list_item);
   List<List<String>> resultList=new ArrayList();
   
   for(int i=0 ;i<list.size();i++){
     List<String> everyList=list.get(i);
     String groupItem=everyList.get(0);
     if(!YHUtility.isNullorEmpty(groupItem)){
       List<String> personList= getCalList(list_item,list,groupItem);
       resultList.add(personList);
     }
   }
   
   return resultList;
 }
 
 public List<String> getCalList(String list_item,List<List<String>> list,String groupItem)throws Exception{
   List<String> resultList=new ArrayList();//返回的list
   resultList.add(groupItem);
   String calType[]=list_item.split(",");
   for(int i=0;i<calType.length;i++){
     String item=calType[i];
     String info[]=item.split("`");
     String ct=info[2];
     
     List<String> num=new ArrayList();
        double number=0;
     for(int j=0;j<list.size();j++){
       List<String> eachList=list.get(j);
       String groupValue=eachList.get(0);
       if(groupItem.equals(groupValue)){
         num.add(eachList.get(i+1));
       }
     } 
     if("0".equals(ct)){//求和
       number=this.getSum(num);
     }else if("2".equals(ct)){//求平均
       number=this.getAvg(num);
     } 
     resultList.add(number+"");
     
     }
   return resultList;   

 }
 
 public int getColCount(List<List<String>> dataList,String name)throws Exception{
   int sum=0;
  if(YHUtility.isNullorEmpty(name)){
    
    for(int i=0;i<dataList.size();i++){
      List<String> list=dataList.get(i);
      String itemName="";
      itemName=list.get(0);
      if( null==itemName){
        sum=1;
        for(int j=i+1;j<dataList.size();j++){
          List<String> countList=dataList.get(j);
          if("".equals(countList.get(0))){
            sum++;
          }
          else {
            break;
          }
          
        }
        if(sum>0){
          break;
        }
      
      }
    }
    
    
    
  
   }else{
   for(int i=0;i<dataList.size();i++){
     List<String> list=dataList.get(i);
     String itemName="";
     itemName=list.get(0);
     if(  name.equals(itemName)){
       sum=1;
       for(int j=i+1;j<dataList.size();j++){
         List<String> countList=dataList.get(j);
         if("".equals(countList.get(0))){
           sum++;
         }
         else {
           break;
         }
         
       }
       if(sum>0){
         break;
       }
     
     }
   }
   }
  
  
   return sum;
 }
 
 public String getTableStr(List<List<String>> dataList,String type)throws Exception{
    String tableStr="";
   for(int i=0;i<dataList.size()-1;i++){
     List<String> itemList=dataList.get(i);
     tableStr+="<tr>";
     int flag=0;
     for(int j=0;j<itemList.size();j++){
       String colspan=""; 
       String itemData="";
       itemData=itemList.get(j);
       if(!"".equals(itemData) && flag==0){  
           int num=getColCount(dataList,itemData);
           colspan= " rowspan='"+num+"'";
        }
       if(!"".equals(itemData) || flag!=0){
       tableStr+="<td "+colspan+"  class='TableData'>"+itemData+"</td>";
       }
        flag=1;
     }
     tableStr+="</tr>";
   }
   
   if("0".equals(type) && dataList.size()>0){
     List<String> lastList=dataList.get(dataList.size()-1);
     tableStr+="<tr>";
      tableStr+="<td class='TableContent'>合计：</td>";
     for(int j=1;j<lastList.size();j++){
       String itemData=lastList.get(j);
       tableStr+="<td class='TableData'>"+itemData+"</td>";
     }
     tableStr+="</tr>";
    
   }else if(dataList.size()>0){
     List<String> lastList=dataList.get(dataList.size()-1);
     int f=0;
     for(int j=0;j<lastList.size();j++){
       String itemData=lastList.get(j);
       if(!"".equals(itemData) || f!=0){
       tableStr+="<td class='TableData'>"+itemData+"</td>";
       }
       f=1;
     }
     tableStr+="</tr>";
   }
   
   
   return tableStr;
 }
 
 
/* 

 public String getTableListLogic(Connection conn,YHPerson user,Map request)  throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   String data="";
   String startDate = request.get("startDate") == null ? null : ((String[]) request.get("startDate"))[0];
   String endDate = request.get("endDate") == null ? null : ((String[]) request.get("endDate"))[0];
   String dept = request.get("dept") == null ? null : ((String[]) request.get("dept"))[0];
   String query = request.get("query") == null ? null : ((String[]) request.get("query"))[0];
   String rid = request.get("rid") == null ? null : ((String[]) request.get("rid"))[0];

   try{
     String sql="select * from flow_report where seq_id="+rid;
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     int flowId=0;
     int rId=0;
     String list_item="";
     String query_item="";
     String group_type="";
     String group_field="";
     if(rs.next()){
       flowId=rs.getInt("flow_id");
       rId           = rs.getInt("seq_id");
       list_item     = rs.getString("list_item");
       query_item    = rs.getString("query_item");
       group_type    = rs.getString("group_type");
       group_field   = rs.getString("group_field");   
     }
     String deptStr="";
    if("".equals(dept) || dept==null){
       dept=getPrivDept(conn,user,rid);
     }
     if(!"".equals(dept) && !"0".equals(dept)){
       deptStr=" and begin_user in ( select seq_id from person where 1=1 and dept_id in ("+dept+"))";
     }         
     sql="select run_id  from flow_run  where 1=1 "+deptStr+" and flow_id="+flowId+" and "+YHDBUtility.getDateFilter("begin_time",startDate+" 00:00:00",">=") +" and "+YHDBUtility.getDateFilter("begin_time", endDate+" 23:59:59", "<=");    
     sql+=" order by seq_id asc";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     String itemData="";
     String flag="0";
     while(rs.next()){
      int run_id=rs.getInt("run_id");
        if(this.getQueryStr(conn,run_id+"",rid,query, request))
        {
          itemData+=this.getItemData(conn,list_item,run_id+"",group_field);
        }
     }
                                  
     String calLine="";
     if("0".equals(group_type)){
     this.getCalLine(conn,list_item,sql,flowId+"",rid,query,request);
     }
     String ItemName=this.getFieldNameByRid(conn, group_field, flowId+"", list_item,sql,rid);   
     data="<table border='0' width='85%' class='TableList'  align='center'>"+ItemName+itemData+calLine+"</table>";
     if(!"".equals(itemData)){
       flag="1";
     }
     data=YHUtility.encodeSpecial(data);
   data="{table:'"+data+"',flag:'"+flag+"'}";
   }catch(Exception e){
     throw e;
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }

   return data;
 }*/
 
 public List<String> getCalLine(List<List<String>> list,String list_item) throws Exception{
   List<String> reList=new ArrayList();
   reList.add("合计");
   String data="";
   String calType[]=list_item.split(",");
   for(int i=0;i<calType.length;i++){
     String item=calType[i];
     String info[]=item.split("`");
     String ct=info[2];
     
     List<String> num=new ArrayList();
     double number=0;
     for(int j=0;j<list.size();j++){
       List<String> eachList=list.get(j);
       num.add(eachList.get(i+1));
     }
       
     if("0".equals(ct)){//求和
       number=this.getSum(num);
     }else if("2".equals(ct)){//求平均
       number=this.getAvg(num);
     } 
     
     reList.add(number+"");
     }  
  
   return reList;
 }
 
 public float getFormulate(Connection conn,String field,String query,String flow_id,String rid,String que,Map request) throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   float sum=0;
   int run_id=0;
   LinkedList<String> titlelist=new LinkedList<String>();
   LinkedList<String> namelist=new LinkedList<String>();
   try{

     String Item[]=field.split("`",-1);
     String formulate=Item[3];
     String sql="select title,name from oa_fl_form_item where form_id in (select form_seq_id from oa_fl_type where seq_id='"+flow_id+"')";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     while(rs.next()){
       String title=rs.getString("title");
       String name=rs.getString("name");
       titlelist.add(title);
       namelist.add(name);
     }
     titlelist.add("流水号");
     namelist.add("run_id");
    rs=stmt.executeQuery(query);
    while (rs.next()){
      run_id=rs.getInt("run_id");
      if(this.getQueryStr(conn, run_id+"", rid, que, request , Integer.parseInt(flow_id))){
      Iterator iterTitle=titlelist.iterator();
      Iterator iterName=namelist.iterator();
      while(iterTitle.hasNext()){
        String title=(String)iterTitle.next();
        String name=(String)iterName.next();
        String value=this.getDataByFidAndItem(conn, run_id+"", name , Integer.parseInt(flow_id));
        if(!YHUtility.isNumber(value)){
          value="0";
        }
        formulate=formulate.replace(title, value);
        
      }
       sum+=this.getFormulateResult(formulate);
      }
    }
   }catch(Exception e){
     throw e;
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }
   
   return sum;
 }
 
 public double getFormulateResult(String formulate) throws Exception { 
   double result =0; 
   try { 
   Expression e = ExpressionFactory.createExpression(formulate+"+0.0"); 
   JexlContext jc = JexlHelper.createContext(); 
   result = (Double) e.evaluate(jc); 
   } catch (Exception ex) { 
     throw ex;
   } 
  return result; 
   
   }
 
 
 public float getSum(List<String> num) throws Exception{
   float sum=0;
   for(int i=0;i<num.size();i++){
     String value=num.get(i);
     if(YHUtility.isNumber(value)){
       sum+=Float.parseFloat(value);
     }
     
   }
   return sum;
 }
 
 public float getAvg(List<String> number) throws Exception{
   float sum=0;
   int num=0;
   for(int i=0;i<number.size();i++){
     String value=number.get(i);
     if(YHUtility.isNumber(value)){
       sum+=Float.parseFloat(value);
     }
     num++;
   }
   float result=0;
   if(num!=0){
     result= sum/num;
   }
   return result;
 }
 
 public float getAvgWeight(Connection conn,String field,String query,String rid,String que,Map request , int flowId) throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   float sumWeight=0;
   float sum=0;
   int run_id=0;
   
   String Item=field.substring(0,field.indexOf("`"));
   String weigth=field.substring(field.indexOf("`")+1,field.length());
   try{
     stmt=conn.createStatement();
     rs=stmt.executeQuery(query);
     while(rs.next()){
       run_id =rs.getInt("run_id");
       if(this.getQueryStr(conn, run_id+"", rid, que, request , flowId)){
       String itemData=this.getDataByFidAndItem(conn, run_id+"", Item  , flowId);
       String weigthData=this.getDataByFidAndItem(conn, run_id+"", weigth  , flowId);
       if(YHUtility.isNumber(itemData) && YHUtility.isNumber(weigthData)){
         float Idata=Float.parseFloat(itemData);
         float Wdata=Float.parseFloat(weigthData);
         sumWeight+=Wdata;
         if (sumWeight != 0 ) {
           sum=((Idata * Wdata) + (sumWeight - Wdata) * sum)/sumWeight;
         }
        }
       }
     }
   }catch(Exception e){
     throw e;
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }
   return sum;
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
 
 
 public String getItemData(Connection conn,String listItem,String run_id,String groupItem , int flowId) throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   String data="";
 
     String fieldvalue=this.getDataByFidAndItem(conn,run_id,groupItem , flowId);
     data="<td class='TableData'>"+fieldvalue+"</td>";
     listItem = YHUtility.null2Empty(listItem);
   String list[]=listItem.split(",");
   for(int i=0;i<list.length;i++){
     if(!"".equals(list[i])){
      String itemName[]=list[i].split("`");
      String itemValue=this.getDataByFidAndItem(conn, run_id,itemName[0] , flowId) ;
      if(YHUtility.isNullorEmpty(itemValue)){
        itemValue="";
      }
      data+="<td class='TableData'>"+itemValue+"</td>"; 
     }
   }
   data="<tr>"+data+"</tr>";
   
   return data;
 }
 
 public LinkedList<String> getItemDataList(Connection conn,String listItem,String run_id,String groupItem , int flowId) throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   LinkedList<String> datalist=new LinkedList<String>();
   listItem = YHUtility.null2Empty(listItem);
     String fieldvalue=this.getDataByFidAndItem(conn,run_id,groupItem , flowId);
     datalist.add(fieldvalue);
     String list[]=listItem.split(",");
   for(int i=0;i<list.length;i++){
     if(!"".equals(list[i])){
      String itemName[]=list[i].split("`");
      String itemValue=this.getDataByFidAndItem(conn, run_id,itemName[0] , flowId) ;
      if(YHUtility.isNullorEmpty(itemValue)){
        itemValue="";
      }
      datalist.add(itemValue); 
     }
   }
   return datalist;
 }
 
 public String getDataByFidAndItem(Connection conn,String fId,String Item , int flowId) throws Exception{
   String data="";
   
   if("run_id".equals(Item) || "RUN_ID".equals(Item)){
     return fId;
   }
   YHFlowRunUtility util = new YHFlowRunUtility();
   int itemId = Integer.parseInt(Item.substring(Item.indexOf("_")+1,Item.length()));
   YHFlowRunData runData = util.getFlowRunData(conn, Integer.parseInt(fId), itemId, flowId);
   if (runData != null) {
     data = runData.getItemData();
   }
   return data;
 }
 
 public LinkedList<String> getFieldNameByRidForExcel(Connection conn,String groupItem,String flow_id,String listItem,String sql1,String rid, int versionNo) throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   String data="";
   LinkedList<String> namelist=new LinkedList<String>();
   YHFormVersionLogic lo = new YHFormVersionLogic();
   
   
   try{
     String form_seq_id=this.getFormId(conn,flow_id);
     int formId = lo.getFormSeqId(conn, versionNo, Integer.parseInt(form_seq_id));
     String sql="select title from oa_fl_form_item where form_id="+ formId +" and name='"+groupItem+"'";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     String groupName="";
     if(rs.next()){
       groupName=rs.getString("title");
       data=groupName;
     }
     if("run_id".equals(groupItem) || "RUN_ID".equals(groupItem) ){
       data="流水号";
     }
     namelist.add(data);
     String list[]=listItem.split(",");
     for(int i=0;i<list.length;i++){
       String item[]=list[i].split("`");
       String tmp = "&nbsp;";
       if (item.length > 1) {
         tmp = item[1];
       }
       namelist.add(tmp);  
     }
  
   }catch(Exception e){
     throw e;
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }
   return namelist;
 }
 public String getFieldNameByRid(Connection conn,String groupItem,String flow_id,String listItem,String rid,String group_type,List<List<String>> queryList) throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   String data="";
   //sql1=sql1.replace("(","*");
   //sql1=sql1.replace(")","%");
   try{
     String form_seq_id=this.getFormId(conn,flow_id);
     String sql="select title from oa_fl_form_item where form_id="+form_seq_id+" and name='"+groupItem+"'";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     String groupName="";
     if(rs.next()){
       groupName=rs.getString("title");
     }
     if("run_id".equals(groupItem) || "RUN_ID".equals(groupItem) ){
       groupName="流水号";
     }
     data="<td class='TableContent'>"+groupName+"</td>";
     listItem = YHUtility.null2Empty(listItem);
     String list[]=listItem.split(",");
     for(int i=0;i<list.length;i++){
       String item[]=list[i].split("`");
       String tmp = "&nbsp;";
       String onclick="";
       if (item.length > 1) {
         tmp = item[1];
       }
       if("0".equals(group_type)){
         String title="按"+groupName+"分组统计"+tmp;
         String ChartStr=getChartStr(queryList,i,title);
         ChartStr=YHUtility.encodeSpecial(ChartStr);
         onclick="title='点击转换到饼状分析图' style='cursor:hand' onclick=\"Chart('"+ChartStr+"')\"";
       }
       data+="<td "+onclick+" class='TableContent'>"+tmp+"</td>";  
     }
    data="<tr>"+data+"</tr>";
   }catch(Exception e){
     throw e;
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }
   return data;
 }

 public String getChartStr(List<List<String>> queryList,int index,String title)throws Exception{
   String data="";
   data="<graph caption='"+title+"' showNames='1'>";
   for(int i=0;i<queryList.size()-1;i++){
   List<String> eachList=queryList.get(i);
   String fieldValue=eachList.get(0);
   String itemValue=eachList.get(index+1);
   data+=" <set name='" + fieldValue + "' value='" + itemValue + "'/>";
   }
   data+="</graph>";
   return data;
 }
 
 
 public String getFormId(Connection conn,String id) throws Exception {
   Statement stmt=null;
   ResultSet rs=null;
   int seq_id=0;
  try{
   String sql="select form_seq_id from oa_fl_type where seq_id="+id;
   stmt=conn.createStatement();
   rs=stmt.executeQuery(sql);
   
   if(rs.next()){
     seq_id=rs.getInt("form_seq_id");
   }
  }catch(Exception e){
    throw e;
  }finally{
    YHDBUtility.close(stmt, rs, null);
  }
   return seq_id+"";
   
 }
 
 
 public boolean getQueryStr(Connection conn,String run_id,String rid,String query,Map request , int flowId) throws Exception{
   boolean result=true;
   boolean result1=false;
   String que[]=query.split(",");
   for(int i=0;i<que.length;i++){
     if(!"".equals(que[i])){
       String item=que[i];
       String value = request.get(item) == null ? null : ((String[]) request.get(item))[0];
      try {
       if(!YHUtility.isNullorEmpty(value)){
         String valueData=this.getDataByFidAndItem(conn, run_id, item , flowId);
        if(valueData==null || valueData.indexOf(value)==-1){
           result=false;
           break;
         }
       }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        throw e;
      }     
     }
   }
   if(result){
     result1=true;
   }
   return result1;
 }
 
 public String getItemNameByRid(Connection conn,String rId,String item)throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
    String itemName="";
   try{
     String sql=" select query_item from oa_fl_report where seq_id="+rId;
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     String query_field="";
     if(rs.next()){
       query_field=rs.getString("query_item");
     }
     String field[]=query_field.split(",");
     for(int i=0;i<field.length;i++){
       String itemOrg[]=field[i].split("`");
       if(itemOrg[1].equals(item)){
         itemName=itemOrg[0];
         break;
       }
     }
     
     
   }catch(Exception e){
     throw e;
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }
   
   
   return itemName;
 }
 
 public String getListById(Connection conn,int seq_id)  throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   String data="";
   try{
     String sql="select r.seq_id,r.r_name,t.form_seq_id,t.SEQ_ID AS FLOW_ID from oa_fl_report r,oa_fl_type t where t.seq_id=r.flow_id and t.flow_sort="+seq_id;
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     YHFormVersionLogic logic = new YHFormVersionLogic();
     while(rs.next()){
       int id=rs.getInt("seq_id");
       String name=rs.getString("r_name");
       int formSeqId = rs.getInt("form_seq_id");
       int flowId = rs.getInt("FLOW_ID");
       data+="{seqId:'"+id+"',name:'"+name+"', hasFormVersion:"+ logic.hasFormVersion(conn, formSeqId) +" , flowId:"+ flowId +"}";
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
   
 public String getChartLogic(Connection conn,Map request) throws Exception{
   String data="";
   Statement stmt=null;
   ResultSet rs=null;
   String groupItem = request.get("groupItem") == null ? null : ((String[]) request.get("groupItem"))[0];
   String item = request.get("item") == null ? null : ((String[]) request.get("item"))[0];
   String sql1 = request.get("sql") == null ? null : ((String[]) request.get("sql"))[0];
   String rid = request.get("rid") == null ? null : ((String[]) request.get("rid"))[0];
   String query = request.get("query") == null ? null : ((String[]) request.get("query"))[0];
   String title=getChartTitle(conn,rid, item);

   try{
     
     String sql="select * from oa_fl_report where seq_id="+rid;
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     int flowId=0;
     int rId=0;
     String list_item="";
     String query_item="";
     String group_type="";
     String group_field="";
     if(rs.next()){
       flowId=rs.getInt("flow_id");
       list_item     = rs.getString("list_item");
       group_field   = rs.getString("group_field");   
     }
     
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql1);
     while(rs.next()){
        int  run_id=rs.getInt("run_id");
        if(this.getQueryStr(conn,run_id+"",rid,query, request , flowId)){
        data+=this.getChartData(conn, list_item,run_id+"", group_field,item , flowId);        
     }     
    }
     data = title + data;
   }catch(Exception e){
     throw e;
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }
 return data + "</graph>";

 }
 
 public String getChartData(Connection conn,String listItem,String run_id,String groupItem,String item , int flowId) throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   String data="";
 
     String fieldvalue=this.getDataByFidAndItem(conn,run_id,groupItem , flowId);
     listItem =  YHUtility.null2Empty(listItem);
   String list[]=listItem.split(",");

      String itemName[]=list[Integer.parseInt(item)].split("`");
      String itemValue=this.getDataByFidAndItem(conn, run_id,itemName[0] , flowId) ;
      if(YHUtility.isNullorEmpty(itemValue)){
        itemValue="";
      }
    
     data="<set name='" + fieldvalue + "' value='" + itemValue + "'/>";
   
   return data;
 }
 
 public String getChartTitle(Connection conn,String rid,String item) throws Exception{
   String title="";
   Statement stmt=null;
   ResultSet rs=null;
   String groupItem="";
   String items="";
   int flow_id=0;
   String groupName="";
   String itemName="";
   try{
   String sql="select * from oa_fl_report where seq_id="+rid;
   stmt=conn.createStatement();
   rs=stmt.executeQuery(sql);
   if(rs.next()){
     flow_id=rs.getInt("flow_id");
     groupItem=rs.getString("group_field");
     items=rs.getString("list_item");
   }
   String form_seq_id=this.getFormId(conn,flow_id+"");
   sql="select title from oa_fl_form_item where form_id="+form_seq_id+" and name='"+groupItem+"'";
   stmt=conn.createStatement();
   rs=stmt.executeQuery(sql);
   if(rs.next()){
     groupName=rs.getString("title");
   }
   if("run_id".equals(groupItem) || "RUN_ID".equals(groupItem) ){
     groupName="流水号";
   }
   String itemsOrg[]=items.split(",");
   String itemOrg[]= itemsOrg[Integer.parseInt(item)].split("`");
   itemName=itemOrg[1];
   title = "<graph caption=\"按"+groupName+"统计"+itemName+"\" showNames='1'>";
   }catch(Exception e){
     throw e;
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }
   return title;
 }
 
 public  List<LinkedList<String>> toExcel(Connection conn,YHPerson user,Map request , int versionNo)  throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   List<LinkedList<String>>  querylist=new ArrayList();
   LinkedList<String> datalist=new LinkedList<String>();
   String data="";
   String startDate = request.get("startDate") == null ? null : ((String[]) request.get("startDate"))[0];
   String endDate = request.get("endDate") == null ? null : ((String[]) request.get("endDate"))[0];
   String dept = request.get("dept") == null ? null : ((String[]) request.get("dept"))[0];
   String query = request.get("query") == null ? null : ((String[]) request.get("query"))[0];
   String rid = request.get("rid") == null ? null : ((String[]) request.get("rid"))[0];

   try{
     String sql="select * from oa_fl_report where seq_id="+rid;
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     int flowId=0;
     int rId=0;
     String list_item="";
     String query_item="";
     String group_type="";
     String group_field="";
   
     if(rs.next()){
       flowId=rs.getInt("flow_id");
       rId           = rs.getInt("seq_id");
       list_item     = rs.getString("list_item");
       query_item    = rs.getString("query_item");
       group_type    = rs.getString("group_type");
       group_field   = rs.getString("group_field");   
     }
     datalist=this.getFieldNameByRidForExcel(conn, group_field, flowId+"", list_item,sql,rid  ,versionNo);  
     querylist.add(datalist);
     
     String deptStr="";
    if("".equals(dept) || dept==null){
       dept=getPrivDept(conn,user,rid);
     }
     if(!"".equals(dept) && !"0".equals(dept)){
       deptStr=" and begin_user in ( select seq_id from person where 1=1 and dept_id in ("+dept+"))";
     }         
     sql="select run_id  from oa_fl_run  where 1=1 "+deptStr+" and FORM_VERSION = "+versionNo + " and flow_id="+flowId+" and "+YHDBUtility.getDateFilter("begin_time",startDate+" 00:00:00",">=") +" and "+YHDBUtility.getDateFilter("begin_time", endDate+" 23:59:59", "<=");    
     sql+=" order by seq_id asc";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     String itemData="";
     String flag="0";
     while(rs.next()){
      int run_id=rs.getInt("run_id");
        if(this.getQueryStr(conn,run_id+"",rid,query, request , flowId))
        {
          datalist=this.getItemDataList(conn,list_item,run_id+"",group_field , flowId);
          querylist.add(datalist);
        }
     }
     
   }catch(Exception e){
     throw e;
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }

   return querylist;
 }
 
 public ArrayList<YHDbRecord> convertList( List<LinkedList<String>> list){
   ArrayList<YHDbRecord > dbL = new ArrayList<YHDbRecord>();
   if(list != null && list.size() >0){
     LinkedList<String> namelist=list.get(0);
     int length=namelist.size();
     for(int i=1;i<list.size();i++){
       LinkedList<String> datalist=list.get(i);
       YHDbRecord dbrec=new YHDbRecord();
       for(int j=0;j<length;j++){       
         dbrec.addField(namelist.get(j),datalist.get(j));        
       }
        dbL.add(dbrec);
     }

   }     
   return dbL;    
 }
 
 public List<List<String>> getGroupListLogic(List<List<String>> dataList) throws Exception{
   List<List<String>> seqList=new ArrayList();
   for(int i=0;i<dataList.size();i++){
     List<String> itemList=dataList.get(i);
     String groupItem=itemList.get(0);
     
    if(!this.getIsExit(seqList, groupItem) && !"".equals(groupItem)){
     seqList.add(itemList);

     for(int j=i+1;j<dataList.size();j++){
       List<String> compList=dataList.get(j);
       String compItem=compList.get(0);
       if(YHUtility.isNullorEmpty(groupItem)){
         groupItem="";
       }
       if(YHUtility.isNullorEmpty(compItem)){
         compItem="";
       }
       if(groupItem.trim().equals(compItem.trim())){
       
          compList.set(0, "");
          seqList.add(compList);

       }
     }
    }
   }
   return seqList;
 }
 
 public boolean getIsExit(List<List<String>> seqList,String name){
   boolean result=false;
   if(YHUtility.isNullorEmpty(name)){
     name="";
   }
   for(int i=0;i<seqList.size();i++){
     List<String> rr=seqList.get(i);
     String Tname= "";
      Tname= rr.get(0);
     
     if(name.equals(Tname)){
       result=true;
       break;
     }
     
   }
   return result;
 }
 
 
}
