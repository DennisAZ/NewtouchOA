package yh.subsys.inforesouce.act;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHOut;
import yh.subsys.inforesouce.data.YHMateValue;
import yh.subsys.inforesouce.logic.YHMateElementLogic;


public class YHMateElementAct {
  private static Logger log = Logger.getLogger(YHMateElementAct.class);
  
  public String addelement(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    //System.out.println("ss");
    
    
     String boardNo = request.getParameter("BOARD_NO"); //编号
     int num = Integer.parseInt(boardNo);
     if(num >= 100){
       boardNo = "MEX"+boardNo;
     }else{
       boardNo = "M"+boardNo;
     }
    // int boardNo = Integer.parseInt(board);
     String cnName = request.getParameter("cn_NAME");
     String enName = request.getParameter("en_NAME");
     String defineText = request.getParameter("define_TEXT");
     String aimText = request.getParameter("aim_TEXT"); 
     String constraint = request.getParameter("constraint");//约束性
     String repeat = request.getParameter("repeat");
     String element_type = request.getParameter("element_type");
     String typeId = request.getParameter("typeId");
     String pd = request.getParameter("pId");
     String []eleType = request.getParameterValues("eleType");
     String elementType = "";
     for(int i = 0; i<eleType.length; i++){
          elementType += eleType[i]+",";
     }
     
     int pid = Integer.parseInt(pd);
     //System.out.println(boardNo+"::"+cnName+"::"+enName+"::"+defineText+"::"+aimText+"::"+constraint+"::"+repeat+"::"+element_type+"::"+typeId);
    Connection dbConn = null;
    try {
     //System.out.println("ddddd");
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHMateElementLogic element= new YHMateElementLogic();
      element.addelement(boardNo,cnName,enName,defineText,aimText,constraint,repeat,element_type,typeId,pid,elementType,dbConn,person);
     /* YHSysLogSaveLogic save = new YHSysLogSaveLogic();
      String OkandSory="";
      if(person.isAdmin()){
       OkandSory =  save.getSaveLog(dbConn, person);
      }
  */
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw ex;
    }
    return "/yh/subsys/inforesouce/act/YHMateTypeAct/findMata.act?defalutType=1";
  }
  // 增加自定义值域
  
  public String addvalue(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    //System.out.println("ss");
  String  valueId = request.getParameter("value_id");
 
  String valueName =  request.getParameter("value_name");  
 
  Connection dbConn = null;
  String seqId = request.getParameter("seqId");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHMateElementLogic element= new YHMateElementLogic();
        element.addValueRange(dbConn,Integer.parseInt(seqId),valueId, valueName, person);
        
       /* YHSysLogSaveLogic save = new YHSysLogSaveLogic();
      String OkandSory="";
      if(person.isAdmin()){
       OkandSory =  save.getSaveLog(dbConn, person);
      }
  */
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw ex;
    }
    // return null;/yh/core/inforesource/define.jsp
    return "/yh/subsys/inforesouce/act/YHMateElementAct/selectvalue.act?seqid="+seqId+"&&number="+valueId;
  }
  
  /**
   * 查询值域 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
 
  public String selectvalue(HttpServletRequest request,
      HttpServletResponse response) throws Exception {   
      Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHMateElementLogic element= new YHMateElementLogic();
        String seqId = request.getParameter("seqid");
        String number = request.getParameter("number");
        
      List<YHMateValue> va  =   element.selectvalue(Integer.parseInt(seqId),dbConn, person);
      request.setAttribute("va", va);
       /* YHSysLogSaveLogic save = new YHSysLogSaveLogic();
      String OkandSory="";
      if(person.isAdmin()){
       OkandSory =  save.getSaveLog(dbConn, person);
      }
  */
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute("seqId", seqId);
      request.setAttribute("number", number);
     }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw ex;
    }
     return "/subsys/inforesource/define.jsp";
  }
  
  
  public String updatevalue(HttpServletRequest request,
      HttpServletResponse response) throws Exception {   
      Connection dbConn = null;
      String pId="";
      String number="";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHMateElementLogic element= new YHMateElementLogic();
        number = request.getParameter("valueId");
      //  YHOut.println(valueId+"uuuuuuuuuuuu");
        String valueName = request.getParameter("valueName");
        String aid = request.getParameter("aid");
         pId = request.getParameter("pId");
       element.updatevalue(Integer.parseInt(aid), number,valueName, dbConn, person);
     // request.setAttribute("va", va);
       /* YHSysLogSaveLogic save = new YHSysLogSaveLogic();
      String OkandSory="";
      if(person.isAdmin()){
       OkandSory =  save.getSaveLog(dbConn, person);
      }
  */
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute("seqId", number);
     }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw ex;
    }
     return "/yh/subsys/inforesouce/act/YHMateElementAct/selectvalue.act?seqid="+pId+"&&number="+number;
  }
  
  /**
   * 判断编号是否存在
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String isExitNos(HttpServletRequest request, HttpServletResponse response)throws Exception{
    
    YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHMateElementLogic element= new YHMateElementLogic();
      String nos = request.getParameter("nos");
      String seqId = request.getParameter("seqId");
      boolean isExit = element.isExitNos(dbConn, nos, seqId);
      if(isExit){//存在编码
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);        
        request.setAttribute(YHActionKeys.RET_DATA, "1");
      }else{   //不存在编码
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);        
        request.setAttribute(YHActionKeys.RET_DATA, "0");
      }
    } catch (Exception e){
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
}
