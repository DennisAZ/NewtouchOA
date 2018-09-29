package yh.core.funcs.system.ispirit.n12.group.act;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.ispirit.communication.YHMsgPusher;
import yh.core.funcs.system.ispirit.n12.file.logic.YHImOffLineLogic;
import yh.core.funcs.system.ispirit.n12.group.data.YHImGroup;
import yh.core.funcs.system.ispirit.n12.group.data.YHImGroupMaxmsgid;
import yh.core.funcs.system.ispirit.n12.group.data.YHImGroupMsg;
import yh.core.funcs.system.ispirit.n12.group.logic.YHImGroupLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

public class YHImGroupAct {
  /**
   * 查看历史记录 
   * 
   **/
  public String getGroupMsgHistory(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
   try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String filterStr=request.getParameter("filterStr");
      String gid=request.getParameter("Gid");
      String start = request.getParameter("start");
      YHImGroupLogic logic= new YHImGroupLogic();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String data= logic.getGorupMsgHistory(dbConn, person, gid, filterStr,start);
  //    data="["+data+"]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
   return "/core/inc/rtjson.jsp";
   }
  
  
  /**
   * 
   * 添加群
   */
  public String addGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
   try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String groupName=request.getParameter("groupName");
      String groupNo=request.getParameter("groupNo");
      String groupSubject=request.getParameter("groupSubject");
      String groupUid=request.getParameter("groupUid");
      String groupIntroduction=request.getParameter("groupIntroduction");
      String groupRemark=request.getParameter("groupRemark");
      String seqId=request.getParameter("seqId");
      YHORM orm = new YHORM();
      YHImGroup ImGroup = new YHImGroup();
      ImGroup.setGroupName(YHUtility.encodeSpecial(groupName));
      ImGroup.setOrderNo(YHUtility.encodeSpecial(groupNo));
      ImGroup.setGroupSubject(YHUtility.encodeSpecial(groupSubject));
      ImGroup.setGroupUid(groupUid);
      ImGroup.setRemark(YHUtility.encodeSpecial(groupRemark));
      ImGroup.setGroupIntroduction(YHUtility.encodeSpecial(groupIntroduction));
      ImGroup.setGroupCreator(person.getSeqId());
      ImGroup.setGroupActive("1");
      Date date = new Date();
      ImGroup.setGroupCreateTime(YHUtility.getDateTimeStr(date));
      
      if(YHUtility.isNullorEmpty(seqId)){
        orm.saveSingle(dbConn, ImGroup);
      
        
        //通知精灵服务器端
        YHImGroupLogic logic = new YHImGroupLogic();
        String groupId=logic.getMaxId(dbConn, " select max(group_id) from oa_im_group ");
        YHMsgPusher.push("S^d^0^"+groupId);
        
      }else{
        ImGroup.setGroupId(Integer.parseInt(seqId));
        String sql=" update oa_im_group set GROUP_NAME='"+ImGroup.getGroupName()+"' ,GROUP_UID='"+ImGroup.getGroupUid()+"' ,ORDER_NO='"+ImGroup.getOrderNo()+"'  ,REMARK='"+ImGroup.getRemark()+"' ,GROUP_INTRODUCTION='"+ImGroup.getGroupIntroduction()+"' ,GROUP_SUBJECT='"+ImGroup.getGroupSubject()+"'  where group_id='"+seqId+"' ";
        Statement stmt=dbConn.createStatement();
        stmt.execute(sql);
        
        
        //通知精灵服务器端
        YHMsgPusher.push("S^d^1^"+seqId);
        
        
      }
         
  
      
      
    } catch (Exception ex) {
 
      throw ex;
    }
   return "/core/frame/ispirit/n12/group/msg.jsp";
   }
  
  
  /**
   * 
   * 添加群
   */
  public String startGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
   try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
       String seqId=request.getParameter("seqId");
      
        String sql=" select * from oa_im_group where group_id='"+seqId+"'  ";
        Statement stmt=dbConn.createStatement();
        ResultSet rs= stmt.executeQuery(sql);
        if(rs.next()){
          String active=rs.getString("GROUP_ACTIVE");
          String act="1";
          if("1".equals(active)){
            act="0";
          }
          sql=" update oa_im_group set GROUP_ACTIVE='"+act+"'   where group_id='"+seqId+"' ";
          stmt=dbConn.createStatement();
          stmt.execute(sql);
        }
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
       // request.setAttribute(YHActionKeys.RET_DATA, "{data:'"+data+"'}");
      } catch (Exception e) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
        e.printStackTrace();
      }
   return "/core/inc/rtjson.jsp";
   }
  
  /**
   * 
   * 发送群消息
   */
  public String groupMsgSend(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String msgGroupId="";
   try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHImOffLineLogic imOffLineLogic = new YHImOffLineLogic();
      String msgContent = (fileForm.getParameter("MSG_CONTENT")== null )? "":fileForm.getParameter("MSG_CONTENT");
      msgGroupId = (fileForm.getParameter("MSG_GROUP_ID")== null )? "":fileForm.getParameter("MSG_GROUP_ID");
      String attachmentId = (fileForm.getParameter("ATTACHMENT_ID")== null )? "":fileForm.getParameter("ATTACHMENT_ID");
      String attachmentName = (fileForm.getParameter("ATTACHMENT_NAME")== null )? "":fileForm.getParameter("ATTACHMENT_NAME");
      String msgContentCimple = (fileForm.getParameter("MSG_CONTENT_SIMPLE")== null )? "":fileForm.getParameter("MSG_CONTENT_SIMPLE");
       
      YHImGroupMsg groupMsg = new YHImGroupMsg();
      YHORM orm = new YHORM();
     
      groupMsg.setMsgUid(person.getSeqId());
      Date date = new Date();
      String   a   =   YHUtility.getDateTimeStr(date); 
      
      groupMsg.setMsgTime(a);
      groupMsg.setMsgGroupId(Integer.parseInt(msgGroupId));
      groupMsg.setAttachmentId(attachmentId);
      groupMsg.setAttachmentName(attachmentName);
      groupMsg.setMsgUserName(person.getUserName());
      groupMsg.setMsgContentSimple(msgContentCimple);
      groupMsg.setReaderUid(person.getSeqId()+"");
      groupMsg.setMsgContent(msgContent);
        
      orm.saveSingle(dbConn, groupMsg);
       
     //处理 IM_GROUP_MAXMSGID
      YHImGroupLogic logic= new YHImGroupLogic();
      String sql=" select max(msg_id) from oa_im_group_msg ";
      String maxId = logic.getMaxId(dbConn, sql);
  //    Map<String,String> map = new HashMap();
  //    map.put("GROUP_MEMBER_UID", person.getSeqId()+"");
   //   map.put("GROUP_ID", msgGroupId);
  //    YHImGroupMaxmsgid ImGroupMaxmsgid =(YHImGroupMaxmsgid) orm.loadObjSingle(dbConn,YHImGroupMaxmsgid.class, map);
     sql=" select * from oa_im_group_maxmsgid where GROUP_MEMBER_UID='"+person.getSeqId()+"' and GROUP_ID='"+msgGroupId+"'  ";
     Statement stmt= dbConn.createStatement();
     ResultSet rs=stmt.executeQuery(sql);
      if(rs.next()){
         String msg_id=rs.getString("msg_id");
         sql=" update oa_im_group_maxmsgid set max_msg_id='"+maxId+"' where msg_id='"+msg_id+"' ";
         stmt.executeUpdate(sql);
         
      }else{
        YHImGroupMaxmsgid  ImGroupMaxmsgid = new YHImGroupMaxmsgid();
        ImGroupMaxmsgid.setGroupId(msgGroupId);
        ImGroupMaxmsgid.setGroupMemberUid(person.getSeqId()+"");
        ImGroupMaxmsgid.setMaxMsgId(maxId);
        orm.saveSingle(dbConn, ImGroupMaxmsgid);
        
      }
      
    } catch (Exception ex) {
      throw ex;
    }
   //header("location: group_msg_list.php?MSG_GROUP_ID=$MSG_GROUP_ID");
 //  return "/core/frame/ispirit/n12/group/group_msg_list.jsp?Gid="+msgGroupId;
   return null;
   }
  
  /**
   * 
   * 发送群消息
   */
  public String getImGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
   try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHORM orm = new YHORM();
      YHImGroupLogic logic= new YHImGroupLogic();
      YHImGroup imGroup =logic.getImGroupLogic(dbConn, seqId);
      String data = YHFOM.toJson(imGroup).toString();
      Map<String,String> map = YHFOM.json2Map(data);
     
      String userName = logic.getUserNameLogic(dbConn, imGroup.getGroupUid());
      map.put("userName", userName);
      map.put("groupNo", imGroup.getOrderNo());
      map.put("groupRemark", imGroup.getRemark());
      data = YHFOM.toJson(map).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
     return "/core/inc/rtjson.jsp";
   }
    
  
  
  /**
   *刷新群消息 
   * 
   **/
  public String getMsgListServer(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
   try{
     String MSG_GROUP_ID= request.getParameter("MSG_GROUP_ID");
     YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
     //校验用户是否在群内
     
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
  
     
     //
     int RECORD_MAX_MSG_ID=0;
     int RECODE_EXIST=0;
     String query = "SELECT MAX_MSG_ID from oa_im_group_maxmsgid where GROUP_MEMBER_UID='"+person.getSeqId()+"' and GROUP_ID='"+MSG_GROUP_ID+"'";
     Statement stmt = dbConn.createStatement();
     ResultSet rs = stmt.executeQuery(query);
     if(rs.next())
     {
       RECORD_MAX_MSG_ID=rs.getInt("MAX_MSG_ID");
       RECODE_EXIST=1;
     }
     
     String returnData="{msg: [";
     
     String CUR_DATE=YHUtility.getCurDateTimeStr("yy-MM-dd");
     query = "SELECT MSG_ID,MSG_CONTENT,MSG_GROUP_ID,ATTACHMENT_ID,ATTACHMENT_NAME,MSG_USER_NAME,MSG_TIME as MSG_TIME from oa_im_group_msg where MSG_GROUP_ID='"+MSG_GROUP_ID+"' and  MSG_ID > '"+RECORD_MAX_MSG_ID+"' order by MSG_ID desc ";
     rs=stmt.executeQuery(query);
     
     int MSG_COUNT=0;
     int MAX_MSG_ID = RECORD_MAX_MSG_ID;

     while(rs.next()){
       MSG_COUNT++;
       int MSG_ID = rs.getInt("MSG_ID");
       if(MSG_ID > MAX_MSG_ID){
         MAX_MSG_ID = MSG_ID;
       }
       String MSG_CONTENT = rs.getString("MSG_CONTENT");
       String MSG_TIME = rs.getString("MSG_TIME");
       String ATTACHMENT_ID = rs.getString("ATTACHMENT_ID");
       String ATTACHMENT_NAME = rs.getString("ATTACHMENT_NAME");
       String MSG_USER_NAME = rs.getString("MSG_USER_NAME");
       String time="";
       if(MSG_TIME.substring(2, 10).equals(CUR_DATE))
         time+=MSG_TIME.substring(11, 19);
       else
         time+=MSG_TIME;
       
       MSG_CONTENT=YHUtility.null2Empty(MSG_CONTENT);
       if(!this.is_image(ATTACHMENT_NAME)){
         MSG_CONTENT+=this.getFilePath(ATTACHMENT_NAME, ATTACHMENT_ID, "im");
       }else{
         MSG_CONTENT+=this.getImagePath(ATTACHMENT_NAME, ATTACHMENT_ID, "im");
       }
       MSG_CONTENT = YHUtility.encodeSpecial(MSG_CONTENT);
       returnData+="{msgId:'"+MSG_ID+"',userName: '"+MSG_USER_NAME+"',time:'"+time+"',content:'"+MSG_CONTENT+"'},";

     }
     if(returnData.endsWith(",")){
       returnData=returnData.substring(0, returnData.length()-1);
     }
     if(!"".equals(returnData)){
       returnData+="]}";
     }

        String sql=" select * from oa_im_group_maxmsgid where GROUP_MEMBER_UID='"+person.getSeqId()+"' and GROUP_ID='"+MSG_GROUP_ID+"'  ";
         stmt= dbConn.createStatement();
         rs=stmt.executeQuery(sql);
         if(rs.next()){
            String msg_id=rs.getString("msg_id");
            sql=" update oa_im_group_maxmsgid set max_msg_id='"+MAX_MSG_ID+"' where msg_id='"+msg_id+"' ";
            stmt.executeUpdate(sql);
            
         }else{
        YHORM orm = new YHORM();   
       YHImGroupMaxmsgid ImGroupMaxmsgid = new YHImGroupMaxmsgid();
       ImGroupMaxmsgid.setGroupId(MSG_GROUP_ID);
       ImGroupMaxmsgid.setGroupMemberUid(person.getSeqId()+"");
       ImGroupMaxmsgid.setMaxMsgId(MAX_MSG_ID+"");
       orm.saveSingle(dbConn, ImGroupMaxmsgid);
       
     }
     
     //返回数据
     response.setCharacterEncoding("UTF-8");
     response.setContentType("text/html;charset=UTF-8");
     response.setHeader("Cache-Control","private");
     //response.setHeader("Accept-Ranges","bytes");
     PrintWriter out = response.getWriter();
     out.print(returnData);
     out.flush();     
     
   }catch (Exception ex) {
     
     ex.printStackTrace();
   }
  return null;
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
  
  /**
   * 查看历史记录 
   * 
   **/
  public String getGroupRemark(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
   try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String groupId = request.getParameter("GROUP_ID");
      //判断成员是否在该群
      
     String sql="select * from oa_im_group where GROUP_ID='"+groupId+"'";
      Statement stmt= dbConn.createStatement();
      ResultSet rs=stmt.executeQuery(sql);
      String REMARK="";
      if(rs.next()){
        String GROUP_UID=rs.getString("GROUP_UID");
        REMARK=rs.getString("REMARK");
      }
      String returnStr="+OK "+REMARK;
      
      //返回
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/html");
      response.setHeader("Cache-Control","private");
      //response.setHeader("Accept-Ranges","bytes");
      PrintWriter out = response.getWriter();
    
      out.print(returnStr);
      out.flush();
   
    } catch (Exception ex) {
      
      throw ex;
    }
   return null;
   }
  
  /**
   * 查看已发送内容 
   * 
   **/
  public String getGroupMsgSent(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
   try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHImOffLineLogic imOffLineLogic = new YHImOffLineLogic();
      String gid=request.getParameter("Gid");
      YHImGroupLogic logic= new YHImGroupLogic();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String data= logic.getGorupMsgSent(dbConn, person, gid);
      data="["+data+"]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
   return "/core/inc/rtjson.jsp";
   }
  
  /**
   * 群管理  通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getGroupListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHImGroupLogic logic= new YHImGroupLogic();
      String data = logic.getGroupJsonLogic(dbConn, request.getParameterMap(), person);
      PrintWriter pw = response.getWriter();
      //System.out.println(data);
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /***
   *删除群
   */
  public String deleteGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHImGroupLogic logic= new YHImGroupLogic();
      logic.deleteGroupLogic(dbConn, seqIdStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 单文件附件上传
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
public String uploadFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
  try{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    
    String gid="";

    Map<String,String> map = new HashMap();
    
  java.util.Enumeration params = request.getParameterNames(); 
  while(params.hasMoreElements()) { 
  String current_param = (String)params.nextElement(); 
  //System.out.print("参数名:" + current_param); 
  String[] values=request.getParameterValues(current_param); 
  for (int i=0;i<values.length;i++) 
    gid= values[i];
    map.put(current_param, gid);
  
  } 
    
    Map<String, String> attr = null;
    String attrId = "";
    String attrName = "";
  //  String msgContent = (fileForm.getParameter("MSG_CONTENT")== null )? "":fileForm.getParameter("MSG_CONTENT");
    String msgGroupId = (fileForm.getParameter("MSG_GROUP_ID")== null )? "":fileForm.getParameter("MSG_GROUP_ID");
    String mixFlag = (fileForm.getParameter("MIX_FLAG")== null )? "":fileForm.getParameter("MIX_FLAG");
    if(YHUtility.isNullorEmpty(msgGroupId)){
      msgGroupId=map.get("MSG_GROUP_ID");
    }
    if(YHUtility.isNullorEmpty(mixFlag)){
      mixFlag=map.get("MIX_FLAG");
    }
    
    long file_size=fileForm.getFileSize("ATTACHMENT");
    String data = "";
    YHImOffLineLogic imOffLineLogic = new YHImOffLineLogic();
    attr = imOffLineLogic.fileUploadLogic(fileForm, YHSysProps.getAttachPath());
    Set<String> keys = attr.keySet();
    for (String key : keys){
      String value = attr.get(key);
      if(attrId != null && !"".equals(attrId)){
        if(!(attrId.trim()).endsWith(",")){
          attrId += ",";
        }
        if(!(attrName.trim()).endsWith("*")){
          attrName += "*";
        }
      }
      attrId += key + ",";
      attrName += value + "*";
    }
    String msg="";
    if("2".equals(mixFlag)){//上传截图 返回连接地址
      String picURL="/yh/yh/core/funcs/office/ntko/act/YHNtkoAct/downFile.act?attachmentName="+attrName+"&attachmentId="+attrId+"&module=im&directView=1";
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/html;charset=UTF-8");
      response.setHeader("Cache-Control","private");
     // response.setHeader("Accept-Ranges","bytes");
      PrintWriter out = response.getWriter();
      out.print(picURL);
      out.flush();
    }else{  //上传其他文件  返回犯贱ID
      
      YHImGroupMsg groupMsg = new YHImGroupMsg();
      YHORM orm = new YHORM();
      groupMsg.setMsgUid(person.getSeqId());
      Date date = new Date();
      groupMsg.setMsgTime(YHUtility.getDateTimeStr(date));
      groupMsg.setMsgGroupId(Integer.parseInt(msgGroupId));
      groupMsg.setAttachmentId(attrId);
      groupMsg.setAttachmentName(attrName);
      groupMsg.setMsgUserName(person.getUserName());
    //  groupMsg.setMsgContentSimple(msgContentCimple);
      groupMsg.setReaderUid(person.getSeqId()+"");
    //  groupMsg.setMsgContent(msgContent);
      orm.saveSingle(dbConn, groupMsg);

      //处理 IM_GROUP_MAXMSGID
      YHImGroupLogic logic= new YHImGroupLogic();
      String sql=" select max(msg_id) from oa_im_group_msg ";
      String maxId = logic.getMaxId(dbConn, sql);
      
      map = new HashMap();
      String query=" select * from oa_im_group_maxmsgid where GROUP_MEMBER_UID='"+person.getSeqId()+"' and GROUP_ID='"+msgGroupId+"'  ";
      Statement stmt= dbConn.createStatement();
      ResultSet rs=stmt.executeQuery(query);
      if(rs.next()){
         String msg_id=rs.getString("msg_id");
         sql=" update oa_im_group_maxmsgid set max_msg_id='"+maxId+"' where msg_id='"+msg_id+"' ";
         stmt.executeUpdate(sql);
         
      }else{  
    YHImGroupMaxmsgid ImGroupMaxmsgid = new YHImGroupMaxmsgid();
    ImGroupMaxmsgid.setGroupId(msgGroupId);
    ImGroupMaxmsgid.setGroupMemberUid(person.getSeqId()+"");
    ImGroupMaxmsgid.setMaxMsgId(maxId+"");
    orm.saveSingle(dbConn, ImGroupMaxmsgid);
    
  }
      
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/html;charset=UTF-8");
      response.setHeader("Cache-Control","private");
      //response.setHeader("Accept-Ranges","bytes");
      PrintWriter out = response.getWriter();
      out.print("+OK"+maxId);
      out.flush();     
    }

  }catch (SizeLimitExceededException ex) {
  } catch (Exception e){
    e.printStackTrace();
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("Cache-Control","private");
    //response.setHeader("Accept-Ranges","bytes");
    PrintWriter out = response.getWriter();
    out.print("上传文件失败");
    out.flush();  
  
    
  }
  return null;
}
  
}
