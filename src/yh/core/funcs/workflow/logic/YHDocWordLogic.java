package yh.core.funcs.workflow.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHDocWord;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHDocWordLogic {
 public void addDocWordLogic(Connection conn,String dwName,String indexStyle,String deptPriv,String rolePriv,String userPriv)throws Exception{
   YHORM orm=new YHORM();
   YHDocWord dw=new YHDocWord();
   dw.setDwName(dwName);
   dw.setIndexStyle(indexStyle);
   dw.setDepartPriv(deptPriv);
   dw.setRolePriv(rolePriv);
   dw.setUserPriv(userPriv);
   orm.saveSingle(conn, dw);
   
 }
 
 
 /**
  *   通用列表
  * 
  * @param dbConn
  * @param request
  * @param person
  * @return
  * @throws Exception
  */
 public String getDocWordListLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
   try {
     String sql = " select c1.SEQ_ID,c1.DW_NAME, c1.index_style, c1.depart_priv, c1.role_priv, c1.user_priv"
                + " from oa_officialdoc_word c1 "
                + " ORDER BY c1.seq_id desc ";
     YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
     YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
  
     return pageDataList.toJson();
     
   } catch (Exception e) {
     throw e;
   }
 }
 
 
 /**
  *   通用列表
  * 
  * @param dbConn
  * @param request
  * @param person
  * @return
  * @throws Exception
  */
 public String queryDocWordListLogic(Connection dbConn,Map request, String dwName, String indexS) throws Exception {
   try {
     String sql = " select c1.SEQ_ID,c1.DW_NAME, c1.index_style, c1.depart_priv, c1.role_priv, c1.user_priv"
                + " from oa_officialdoc_word c1  where 1=1 ";
               
     if(!YHUtility.isNullorEmpty(dwName)){
       sql+=" and c1.DW_name like '%"+dwName+"%' ";
     }
     if(!YHUtility.isNullorEmpty(indexS)){
       sql+=" and c1.index_style like '%"+indexS+"%' ";
     }
     
     YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
     YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
  
     return pageDataList.toJson();
     
   } catch (Exception e) {
     throw e;
   }
 }
  
 
 
 public String getDeptName(Connection conn, String deptId)throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   String data="";
   
   try{
     String depts[]=deptId.split(",");
     for(int i=0;i<depts.length;i++){
       if(!"".equals(depts[i])){
       String sql=" select dept_name from oa_department where seq_id='"+depts[i]+"' ";
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);
        if(rs.next()){
         data+=rs.getString("dept_name");
         data+=",";
        }
       }
     }
   }catch( Exception ex ){
     ex.printStackTrace();
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }
  if(data.endsWith(",")){
    data=data.substring(0, data.length()-1);
  }
   return data;
 }
 
 

 public String getRoleName(Connection conn, String roleId)throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   String data="";
   
   try{
     String roles[]=roleId.split(",");
     for(int i=0;i<roles.length;i++){
       if(!"".equals(roles[i])){
       String sql=" select priv_name from user_priv where seq_id='"+roles[i]+"' ";
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);
        if(rs.next()){
         data+=rs.getString("priv_name");
         data+=",";
        }
       }
     }
   }catch( Exception ex ){
     ex.printStackTrace();
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }
  if(data.endsWith(",")){
    data=data.substring(0, data.length()-1);
  }
   return data;
 }
 
 public String getUserName(Connection conn, String userId)throws Exception{
   Statement stmt=null;
   ResultSet rs=null;
   String data="";
   
   try{
     String users[]=userId.split(",");
     for(int i=0;i<users.length;i++){
       if(!"".equals(users[i])){
       String sql=" select user_name from person where seq_id='"+users[i]+"' ";
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);
        if(rs.next()){
         data+=rs.getString("user_name");
         data+=",";
        }
       }
     }
   }catch( Exception ex ){
     ex.printStackTrace();
   }finally{
     YHDBUtility.close(stmt, rs, null);
   }
  if(data.endsWith(",")){
    data=data.substring(0, data.length()-1);
  }
   return data;
 }
 
 public void deleteDocWordLogic(Connection conn, String seqId)throws Exception{
   YHORM orm =new YHORM();
   try{
     if(!"".equals(seqId)){
       orm.deleteSingle(conn, YHDocWord.class, Integer.parseInt(seqId));
     }
   }catch( Exception ex ){
     ex.printStackTrace();
   }
 }
 
 public String getDocWordLogic(Connection conn, String seqId)throws Exception{
   YHORM orm =new YHORM();
   String data="";
   try{
     YHDocWord dw=new YHDocWord();
     if(!"".equals(seqId)){
       dw=(YHDocWord)orm.loadObjSingle(conn, YHDocWord.class, Integer.parseInt(seqId));
       String deptPriv=dw.getDepartPriv();
       String rolePriv=dw.getRolePriv();
       String userPriv=dw.getUserPriv();
       deptPriv=this.getDeptName(conn,deptPriv);
       rolePriv=this.getRoleName(conn, rolePriv);
       userPriv=this.getUserName(conn,userPriv);
     
     data="{" ;
     data+="seqId:'"+dw.getSeqId()+"',dwName:'"+dw.getDwName()+"',indexStyle:'"+dw.getIndexStyle()+"'";
     data+=",deptPrivId:'"+dw.getDepartPriv()+"',deptPrivName:'"+deptPriv+"'";
     data+=", rolePrivId:'"+dw.getRolePriv()+"',rolePrivName:'"+rolePriv+"' ";
     data+=", userPrivId:'"+dw.getUserPriv()+"',userPrivName:'"+userPriv+"' ";
     data+="}";
     }
   }catch( Exception ex ){
     ex.printStackTrace();
   }
   
  return data;
 }
 
 public void deleteAllDocWordLogic(Connection conn, String seqId)throws Exception{
   try{
     String ids[]=seqId.split(",");
     for(int i=0;i<ids.length;i++){
       if(!"".equals(seqId)){
        this.deleteDocWordLogic(conn, ids[i]);
       }
     }
   }catch( Exception ex ){
     ex.printStackTrace();
   }
 }
 
 public void updateDocWordLogic(Connection conn,String dwName,String indexStyle,String deptPriv,String rolePriv,String userPriv,String seqId)throws Exception{
   YHORM orm=new YHORM();
   YHDocWord dw=new YHDocWord();
   dw=(YHDocWord)orm.loadObjSingle(conn, YHDocWord.class, Integer.parseInt(seqId));
   dw.setDwName(dwName);
   dw.setIndexStyle(indexStyle);
   dw.setDepartPriv(deptPriv);
   dw.setRolePriv(rolePriv);
   dw.setUserPriv(userPriv);
   orm.updateSingle(conn, dw);
   
 }
 
 
}
