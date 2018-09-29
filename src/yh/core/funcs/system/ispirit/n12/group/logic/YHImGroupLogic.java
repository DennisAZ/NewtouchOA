package yh.core.funcs.system.ispirit.n12.group.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.ispirit.communication.YHMsgPusher;
import yh.core.funcs.system.ispirit.n12.group.data.YHImGroup;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.salary.welfare_manager.data.YHHrWelfareManage;

public class YHImGroupLogic {
  /**
   * 查询群聊历史数据
   **/ 
  public String getGorupMsgHistory(Connection conn,YHPerson person,String gid,String filterStr,String start)throws Exception{
    Statement stmt=null;
    ResultSet rs=null; 
    String reContent="";
    String pageCount="";
    String recordCount="";
     //判断是否在该群内
    if(!YHUtility.isNullorEmpty(filterStr)){
      filterStr=" and msg_content like '%"+filterStr+"%' ";
    }
      //翻页
     try {
       String sql=" SELECT  MSG_ID,MSG_CONTENT,MSG_TIME,MSG_GROUP_ID,ATTACHMENT_ID,ATTACHMENT_NAME,MSG_USER_NAME,MSG_TIME as MSG_TIME from oa_im_group_msg where MSG_GROUP_ID='"+gid+"' "+filterStr+" order by MSG_ID desc ";
       String dbms = YHSysProps.getProp("db.jdbc.dbms");
       String result = "";
       if (dbms.equals("mysql")) {
         stmt=conn.createStatement();
       }else{
         stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);  
       }
     
       rs=stmt.executeQuery(sql);
       rs.last();
       pageCount= ((rs.getRow() + 10 - 1)/10)+"";
       recordCount= rs.getRow()+"";
       rs.beforeFirst();
       rs.absolute((Integer.parseInt(start)-1)*10+1);
       int count=0;
       while(Integer.parseInt(recordCount)>0 && !rs.isAfterLast()){
         String msgId = rs.getString("MSG_id");
         String msgContent = rs.getString("msg_content");
         String msgTime = rs.getString("msg_time");
         String msgGroupId = rs.getString("msg_group_id");
         String attachmentId = rs.getString("attachment_id");
         String attachmentName = rs.getString("attachment_name");
         String msgUserName = rs.getString("msg_user_name");
         count++;
         if(count>=10){
           break;
         }
         rs.next();
         msgContent=YHUtility.null2Empty(msgContent);
         if(!this.is_image(attachmentId)){
           msgContent+=this.getFilePath(attachmentName, attachmentId, "im");
         }else{
           msgContent+=this.getImagePath(attachmentName, attachmentId, "im");
         }
      
         
         
         reContent+="{msgId:'"+msgId+"'" +
         		",msgContent:'"+YHUtility.encodeSpecial(msgContent)+"'" +
         				",msgTime:'"+msgTime+"'" +
         						",msgGroupId:'"+msgGroupId+"'" +
         								",attachmentId:'"+attachmentId+"'" +
         										",attachmentName:'"+attachmentName+"'" +
         												",msgUserName:'"+msgUserName+"'},";
       }
      /* reContent+="{msgId:'1'" +
           ",msgContent:'你好'" +
               ",msgTime:'2011-01-01 12:02:22'" +
                   ",msgGroupId:'2'" +
                       ",attachmentId:''" +
                           ",attachmentName:''" +
                               ",msgUserName:'admin'},";*/
       
     }catch(Exception e){
       e.printStackTrace();
     }
     if(reContent.endsWith(",")){
       reContent=reContent.substring(0, reContent.length()-1);
     }
     String data="{pageCount:\""+pageCount+"\",data:["+reContent+"]}";
     
     return data;
   }
  /**
   *获取最大ID
   */
  public String getMaxId(Connection conn ,String sql)throws Exception{
    String id="";
    Statement stmt=null;
    ResultSet rs=null;
    try{
      stmt=conn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        id=rs.getString(1);
      }
      
    }catch(Exception e){
      e.printStackTrace();
    }
    
    
    return id;
  }
  
  
  /**
   * 群管理  通用列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getGroupJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
    try {
      String whereStr="  where GROUP_CREATOR='"+person.getSeqId()+"' ";
      if("1".equals(person.getSeqId()+"")){
        whereStr="";
      }
      String sql = " select c1.GROUP_ID,c1.group_Name, c1.group_Subject, c1.group_Create_Time, c1.group_Creator, c1.group_Active"
                 + " from oa_im_group c1 "
                 + whereStr
                 + " ORDER BY c1.ORDER_NO desc ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
   
      return pageDataList.toJson();
      
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 获取名称
   * 
   * @param dbConn
   * @param seqIdStr
   * @throws Exception
   */
  public String getUserNameLogic(Connection dbConn, String seqIdStr) throws Exception {
    String data="";
    if (YHUtility.isNullorEmpty(seqIdStr)) {
      seqIdStr = "";
    }
    try {
      if(seqIdStr.endsWith(",")){
        seqIdStr=seqIdStr.substring(0, seqIdStr.length());
      }
      
      String sql=" select user_name from person where seq_id in ("+seqIdStr+") ";
      Statement stmt=dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while(rs.next()){
       data+=rs.getString("user_name");
       data+=",";  
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if(data.endsWith(",")){
      data=data.substring(0, data.length());
    }
    return data;
  }
  
  
  /**
   * 获取名称
   * 
   * @param dbConn
   * @param seqIdStr
   * @throws Exception
   */
  public YHImGroup getImGroupLogic(Connection dbConn, String seqIdStr) throws Exception {
      YHImGroup imGroup = new YHImGroup();
     try{
      String sql=" select GROUP_NAME,GROUP_UID,ORDER_NO,REMARK,GROUP_INTRODUCTION,GROUP_SUBJECT from oa_im_group where group_id ='"+seqIdStr+"' ";
      Statement stmt=dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while(rs.next()){
        String GROUP_NAME = rs.getString("GROUP_NAME");
        String GROUP_UID = rs.getString("GROUP_UID");
        String ORDER_NO = rs.getString("ORDER_NO");
        String REMARK = rs.getString("REMARK");
        String GROUP_INTRODUCTION = rs.getString("GROUP_INTRODUCTION");
        String GROUP_SUBJECT = rs.getString("GROUP_SUBJECT");
  
        imGroup.setGroupName(GROUP_NAME);
        imGroup.setGroupUid(GROUP_UID);
        imGroup.setOrderNo(ORDER_NO);
        imGroup.setGroupSubject(GROUP_SUBJECT);
        imGroup.setGroupIntroduction(GROUP_INTRODUCTION);
        imGroup.setRemark(REMARK);
        
      }
     }catch(Exception e){
       e.printStackTrace();
     }
    return imGroup;
  }
  
  
  
  /**
   * 删除文件--wyw
   * 
   * @param dbConn
   * @param seqIdStr
   * @throws Exception
   */
  public void deleteGroupLogic(Connection dbConn, String seqIdStr) throws Exception {
    YHORM orm = new YHORM();
    if (YHUtility.isNullorEmpty(seqIdStr)) {
      seqIdStr = "";
    }
    try {
      if(seqIdStr.endsWith(",")){
        seqIdStr=seqIdStr.substring(0, seqIdStr.length());
      }
      
      String sql=" delete from oa_im_group where group_id in ("+seqIdStr+") ";
      Statement stmt=dbConn.createStatement();
      stmt.execute(sql);
      
      //通知精灵服务器端
      String seq[] = seqIdStr.split(",");
      for( int i=0;i<seq.length;i++){
        String id=seq[i];
        YHMsgPusher.push("S^d^2^"+id);
      }
      
     
      
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 获取已发送信息
   **/ 
  public String getGorupMsgSent(Connection conn,YHPerson person,String gid)throws Exception{
    Statement stmt=null;
    ResultSet rs=null; 
    String reContent="";
     //判断是否在该群内
     
     //翻页
     try {
       String sql=" SELECT MSG_ID,MSG_CONTENT,MSG_TIME,MSG_GROUP_ID,ATTACHMENT_ID,ATTACHMENT_NAME,MSG_USER_NAME,FROM_UNIXTIME(MSG_TIME) as MSG_TIME from oa_im_group_msg where MSG_GROUP_ID='"+gid+"' order by MSG_ID desc ";
       stmt=conn.createStatement();
       rs=stmt.executeQuery(sql);
       int count=0;
       while(rs.next()){
         String msgId = rs.getString("MSG_id");
         String msgContent = rs.getString("msg_content");
         String msgTime = rs.getString("msg_time");
         String msgGroupId = rs.getString("msg_group_id");
         String attachmentId = rs.getString("attachment_id");
         String attachmentName = rs.getString("attachment_name");
         String msgUserName = rs.getString("msg_user_name");
         count++;
         if(count>=20){
           break;
         }
         
         reContent+="{msgId:'"+msgId+"'" +
            ",msgContent:'"+YHUtility.encodeSpecial(msgContent)+"'" +
                ",msgTime:'"+msgTime+"'" +
                    ",msgGroupId:'"+msgGroupId+"'" +
                        ",attachmentId:'"+attachmentId+"'" +
                            ",attachmentName:'"+attachmentName+"'" +
                                ",msgUserName:'"+msgUserName+"'},";
       }
     /*  reContent+="{msgId:'1'" +
           ",msgContent:'你好'" +
               ",msgTime:'2011-01-01 12:02:22'" +
                   ",msgGroupId:'2'" +
                       ",attachmentId:''" +
                           ",attachmentName:''" +
                               ",msgUserName:'admin'},";*/
       
     }catch(Exception e){
       e.printStackTrace();
     }
     if(reContent.endsWith(",")){
       reContent=reContent.substring(0, reContent.length()-1);
     }
     
     
     return reContent;
   }
  
  public boolean is_image(String attName){
    if(YHUtility.isNullorEmpty(attName)){
      return false;
    }
    boolean is=false;
    attName=attName.replace("*", "");
     attName=attName.substring(attName.lastIndexOf(".")+1, attName.length()).toLowerCase();
    if("bmp".equals(attName) || "jpg".equals(attName) || "png".equals(attName) || "gif".equals(attName) ){
      is=true;
    }
    
    return is;
  }
  
  
  public String getFilePath(String attName,String attId,String module){
    if(YHUtility.isNullorEmpty(attName) || YHUtility.isNullorEmpty(attId) || YHUtility.isNullorEmpty(module) ){
      return "";
    }
    attName=attName.replace("*", "");
    attId=attId.replace(",", "");
    
    String filePath="/yh/yh/core/funcs/office/ntko/act/YHNtkoAct/downFile.act?attachmentName="+attName+"&attachmentId="+attId+"&module="+module+"&directView=0";
    return "<font color='red'>  共享文件：</font><a title='点击下载' href=\""+filePath+"\">"+attName+"</a>";
  }
  
  
  public String getImagePath(String attName,String attId,String module){
    if(YHUtility.isNullorEmpty(attName) || YHUtility.isNullorEmpty(attId) || YHUtility.isNullorEmpty(module) ){
      return "";
    }
    attName=attName.replace("*", "");
    attId=attId.replace(",", "");
    
    
    String filePath="/yh/yh/core/funcs/office/ntko/act/YHNtkoAct/downFile.act?attachmentName="+attName+"&attachmentId="+attId+"&module="+module+"&directView=0";

    return "<font color='red'>  附件：</font><a title='点击下载' href='"+filePath+"'>"+attName+"</a>";

   
  }
  
}
