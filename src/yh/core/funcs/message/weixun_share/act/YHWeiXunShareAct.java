package yh.core.funcs.message.weixun_share.act;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.message.weixun_share.data.PinYin;
import yh.core.funcs.message.weixun_share.data.YHWeixunShareTopic;
import yh.core.funcs.message.weixun_share.data.YHWeixunShare;
import yh.core.funcs.message.weixun_share.logic.YHWeiXunShareLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.ispirit.n12.group.logic.YHImGroupLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;



public class YHWeiXunShareAct{
	YHWeiXunShareLogic logic=new YHWeiXunShareLogic();
	
	public String addWeiXunShare(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Connection dbConn = null;
	    try {
	      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
	       String content=request.getParameter("wx_content");
	       String shareId=request.getParameter("shareId");
	       shareId=YHUtility.null2Empty(shareId);
	        YHORM orm =new YHORM();
	        String seqIdStr="";
	    //保存新主题
        String dataCont = this.getTopPic(content);
        String org[] = dataCont.split("\\|");
        for(int i=0;i<org.length;i++){
          String topic = org[i];
          if(!YHUtility.isNullorEmpty(topic)){
            if(!this.isExitTopic(dbConn, topic)){
              YHWeixunShareTopic topic_share =new YHWeixunShareTopic();
              topic_share.setTopicName(topic);
              orm.saveSingle(dbConn, topic_share);
              YHImGroupLogic Imp=new YHImGroupLogic();
              seqIdStr+= Imp.getMaxId(dbConn, " select max(seq_Id) from oa_shortmsg_share_topic");
              seqIdStr+=",";
            }
            
          }
        }
        
        //提取提到人员
       String mentionedIds=this.getMentionId(content);
       
	      YHWeixunShare weiXun=new YHWeixunShare();
	      weiXun.setContent(content);
	      weiXun.setTopics(seqIdStr);
	      weiXun.setBroadcastIds(shareId);
	      weiXun.setAddTime(YHUtility.getCurDateTimeStr());
	      weiXun.setUserId(user.getSeqId());
	      weiXun.setMentionedIds(YHUtility.null2Empty(mentionedIds));
	      orm.saveSingle(dbConn, weiXun);

	    }catch(Exception ex)
	    {
	       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	       request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
	       throw ex;
	    }
	    return "/core/inc/rtjson.jsp";
	}
	
	/**
	 *提取人员 
	 **/
	public String getMentionId(String content)throws Exception{
	  String userIdStr="";
	  String inputOrg[]=content.split("INPUT");
	  for(int i=0;i<inputOrg.length;i++){
	    String inputStr=inputOrg[i];
	    if(inputStr.indexOf("data_uid=")!=-1){
	      
	      String keyValue[]=inputStr.split(" ");
	      for(int j=0;j<keyValue.length;j++){
	        String keyStr=keyValue[j];
	        if(keyStr.indexOf("data_uid")!=-1){
	          keyStr=keyStr.replace("\"", "");
	          keyStr=keyStr.replace(",", "");
	          keyStr=keyStr.replace(">", "");
	          keyStr=keyStr.replace("data_uid=", "");
	          keyStr=keyStr.trim();
	          userIdStr+=keyStr;
	          userIdStr+=","; 
	          break;
	        }
	        
	       
	      }
	      
	        
	    }
	  }
	  
	  return userIdStr;
	}
	
	/**
	 *获取微讯分享 
	 **/
	public String getTopPic(String content){
	  if(YHUtility.isNullorEmpty(content) ||content.indexOf("#")==-1){
	    return "";
	  }
	  String contData="";
	  String tempCont="";
	  content=content.substring(content.indexOf("#")+1, content.length());
	  tempCont=content.substring(0,content.indexOf("#"));
	  if(!YHUtility.isNullorEmpty(tempCont)){
	    contData = tempCont;
	  }
	  content=content.substring(content.indexOf("#")+1,content.length());
	  if(content.indexOf("#")!=-1){
	    contData+="|";
	    contData+=this.getTopPic(content);
	  }
	  
	  return contData;
	}
	
	
	/**
   *获取微讯分享 
   **/
  public boolean isExitTopic(Connection conn,String content){
    boolean contData=false;
    Statement stmt =null;
    ResultSet rs= null;
    try{
     String sql=" select * from oa_shortmsg_share_topic where TOPIC_NAME='"+content+"' ";
     stmt=conn.createStatement();
     rs=stmt.executeQuery(sql);
     if(rs.next()){
       contData=true;
     }
      
    }catch(Exception e){
      e.printStackTrace();
    }
    return contData;
  }
  
  
  /**
   *查询人员keyword
   **/
  public String search_user(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn=null;
    String contData="";
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);  
      dbConn = requestDbConn.getSysDbConn();
      String content =request.getParameter("KWORD");
      Statement stmt =null;
      ResultSet rs= null;
    
     String sql=" SELECT SEQ_ID,USER_NAME,DEPT_ID,BYNAME,REMARK from PERSON where DEPT_ID!=0 order by USER_NO,USER_NAME";
     stmt=dbConn.createStatement();
     rs=stmt.executeQuery(sql);
     int SEARCH_COUNT=0;
     while(rs.next()){
       boolean is=false;
       PinYin py = new PinYin();
       String seqId=rs.getString("SEQ_ID");
       String deptId=rs.getString("DEPT_ID");
       String userName=rs.getString("user_name");
       String BYNAME = YHUtility.null2Empty(rs.getString("BYNAME"));
       String REMARK =YHUtility.null2Empty(rs.getString("REMARK"));
       String unPy=py.chinese2PinYin(userName);
       
       if(seqId.indexOf(content)!=-1){
         is=true;
       }else if(userName.indexOf(content)!=-1){
         is=true;
       }else if(unPy.startsWith(content)){
         is=true;
       }else if(BYNAME.indexOf(content)!=-1){
         is=true;
       }else if(REMARK.indexOf(content)!=-1){
         is=true;
       }
       String line="";
       if(is){
         SEARCH_COUNT++;
         String deptName=logic.getDeptName(dbConn, deptId);
         if(SEARCH_COUNT%2 == 0){
           line=" class=\"line2\" ";
         } 
         String el=userName+"("+deptName+")";
         
         contData+="<li "+line+" title=\""+deptName+"\" data_uid=\""+seqId+"\" data_username=\""+userName+"\">"+el+"</li>\n";
       }
     }
      
     response.setCharacterEncoding("UTF-8");
     response.setContentType("text/html;charset=UTF-8");
     response.setHeader("Cache-Control","private");
     //response.setHeader("Accept-Ranges","bytes");
     PrintWriter out = response.getWriter();
     out.print(contData);
     out.flush();
    
   }catch(Exception ex) {
     ex.printStackTrace();
   }
   return null;
  }
	
	  public String getWeiXun(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		    Connection dbConn = null;
		    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
		    int toId = person.getSeqId();  //用户seqId
		    String pageNoStr = request.getParameter("pageNo");
		    String pageSizeStr = request.getParameter("pageSize");
		   // System.out.println(pageSizeStr);
		    int sizeNo = 0;
		    int pageNo = 0;
		    int pageSize = 0;
		    try {
		      pageNo = Integer.parseInt(pageNoStr);
		      pageSize = Integer.parseInt(pageSizeStr);
		      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);  
		      dbConn = requestDbConn.getSysDbConn();
		     // messageLogic = new YHMessageLogic();
		      String data = logic.getWeiXunContent(dbConn,request.getParameterMap(), toId, pageNo, pageSize);
		      //System.out.println(data);
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
		    }catch(Exception ex) {
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		      throw ex;
		    }
		    return "/core/inc/rtjson.jsp";
		  }
	/**
	 *过去个人weixun
	 **/
	  public String getWeiXunPerson(HttpServletRequest request, HttpServletResponse response) throws Exception { 
      Connection dbConn = null;
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();  //用户seqId
      String pageNoStr = request.getParameter("pageNo");
      String pageSizeStr = request.getParameter("pageSize");
      String seqId = request.getParameter("seqId");
      int sizeNo = 0;
      int pageNo = 0;
      int pageSize = 0;
      try {
        pageNo = Integer.parseInt(pageNoStr);
        pageSize = Integer.parseInt(pageSizeStr);
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);  
        dbConn = requestDbConn.getSysDbConn();
        String data = logic.getWeiXunContentPerson(dbConn,request.getParameterMap(), toId, pageNo, pageSize);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_DATA, data.toString());
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
	  
	  
	  /**
	   *过去个人weixun
	   **/
	    public String getWeiXunMention(HttpServletRequest request, HttpServletResponse response) throws Exception { 
	      Connection dbConn = null;
	      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
	      int toId = person.getSeqId();  //用户seqId
	      String pageNoStr = request.getParameter("pageNo");
	      String pageSizeStr = request.getParameter("pageSize");
	      String seqId = request.getParameter("seqId");
	      int sizeNo = 0;
	      int pageNo = 0;
	      int pageSize = 0;
	      try {
	        pageNo = Integer.parseInt(pageNoStr);
	        pageSize = Integer.parseInt(pageSizeStr);
	        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);  
	        dbConn = requestDbConn.getSysDbConn();
	        String data = logic.getWeiXunContentMention(dbConn,request.getParameterMap(), toId, pageNo, pageSize);
	        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	        request.setAttribute(YHActionKeys.RET_DATA, data.toString());
	      }catch(Exception ex) {
	        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
	        throw ex;
	      }
	      return "/core/inc/rtjson.jsp";
	    }
	    
	    /**
	     *过去个人weixun
	     **/
	      public String getWeiXunTopic(HttpServletRequest request, HttpServletResponse response) throws Exception { 
	        Connection dbConn = null;
	        YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
	        int toId = person.getSeqId();  //用户seqId
	        String pageNoStr = request.getParameter("pageNo");
	        String pageSizeStr = request.getParameter("pageSize");
	        String topic = request.getParameter("topic");
	        int sizeNo = 0;
	        int pageNo = 0;
	        int pageSize = 0;
	        try {
	          pageNo = Integer.parseInt(pageNoStr);
	          pageSize = Integer.parseInt(pageSizeStr);
	          YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);  
	          dbConn = requestDbConn.getSysDbConn();
	          String data = logic.getWeiXunContentTopic(dbConn,request.getParameterMap(), topic, pageNo, pageSize);
	          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	          request.setAttribute(YHActionKeys.RET_DATA, data.toString());
	        }catch(Exception ex) {
	          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	          request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
	          ex.printStackTrace();
	        }
	        return "/core/inc/rtjson.jsp";
	      }
	    
	  
	  public String getWeiXunShare(HttpServletRequest request, HttpServletResponse response) throws Exception { 
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
      try {
        String data="";
        data=logic.getweiXunShare(dbConn);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_DATA, "["+data+"]");
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
	  
	   
    public String getWeiXunById(HttpServletRequest request, HttpServletResponse response) throws Exception { 
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
      try {
        String data="";
        String wxid = request.getParameter("wxid");
        data=logic.getweiXunById(dbConn,wxid);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_DATA, data);
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
	  
	  
	  
	   public String delWeiXun(HttpServletRequest request, HttpServletResponse response) throws Exception { 
	      Connection dbConn = null;
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	          dbConn = requestDbConn.getSysDbConn();
	      try {
	        String seqId=request.getParameter("seqId");
	        YHWeixunShare ws = new YHWeixunShare();
	       YHORM orm = new YHORM();
	       orm.deleteSingle(dbConn, YHWeixunShare.class, Integer.parseInt(seqId));
	        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      }catch(Exception ex) {
	        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
	        throw ex;
	      }
	      return "/core/inc/rtjson.jsp";
	    }
	  
	  
	   public String getTime(HttpServletRequest request, HttpServletResponse response) throws Exception { 
       Connection dbConn = null;
	   String data="";
	   String timeStr=request.getParameter("timeStr");
	try{
	   int date=YHUtility.getDaySpan(timeStr);
	   date=-date;
	   if(date>0){
	     if(date>3){
	       data=timeStr;
	     }else{
	        data=date+"天前";
	     }
	   }else{
	     Date date1 = this.parseDate("yyyy-MM-dd hh:mm:ss", timeStr);
	     Date date2 = this.parseDate("yyyy-MM-dd hh:mm:ss", YHUtility.getCurDateTimeStr());
	     long timeStmp =  date2.getTime()-date1.getTime();
	     int h=(int)timeStmp/(60*60*1000);
	     if(h>0){
	       data=h+"小时前";
	     }else{
	       int m = (int)timeStmp/(60*1000);
	       if(m<=0){
	         m=1;
	       }
	       data=m+"分钟前";
	     }
	     
	   }
	   
	   
	   request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_DATA, "{data:'"+data+"'}");
   }catch(Exception ex) {
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     throw ex;
   }
   return "/core/inc/rtjson.jsp";
	   }
	  
	   /**
	    *获取当前用户的图片 
	    **/
	   public String getPersonAvator(HttpServletRequest request, HttpServletResponse response) throws Exception { 
       Connection dbConn = null;
        try{  
          YHRequestDbConn requestDbConn = (YHRequestDbConn) request
              .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
              dbConn = requestDbConn.getSysDbConn();
          String seqId = request.getParameter("seqId");
          YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
          String webPath =YHSysProps.getWebPath();
          String uid=person.getSeqId()+"";
          if(!YHUtility.isNullorEmpty(seqId)){
            uid=seqId;
          }
          
          FileInputStream fio = logic.getUserAvatorStream(dbConn, uid+"", webPath);
          InputStream is = fio;
          OutputStream ops = null;
          ops = response.getOutputStream();
          response.setCharacterEncoding("UTF-8");
          response.setContentType("application/octet-stream");
          response.setHeader("Cache-Control","private");
          response.setHeader("Accept-Ranges","bytes");
          if(is != null){
            byte[] buff = new byte[8192];
            int byteread = 0;
            while( (byteread = is.read(buff)) != -1){
              ops.write(buff,0,byteread);
              ops.flush();
            }
          }
          
         }catch(Exception ex) {
          
           throw ex;
         }
         return null;
     }
	   
	   
	   public static Date parseDate(String formatStr, String dateStr) throws ParseException {
	     SimpleDateFormat format = new SimpleDateFormat(formatStr);
	     return format.parse(dateStr);
	   }
	   
	  
}