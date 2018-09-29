package yh.subsys.jtgwjh.task.act;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.esb.client.service.YHWSCaller;
import yh.core.esb.frontend.services.YHEsbService;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.sealmanage.data.YHJhSeal;
import yh.subsys.jtgwjh.sealmanage.logic.YHJhSealLogic;
import yh.subsys.jtgwjh.task.data.YHJhTaskLog;
import yh.subsys.jtgwjh.task.logic.YHJhTaskLogLogic;
import yh.subsys.jtgwjh.util.YHDocUtil;

public class YHJhTaskLogAct {
  YHJhTaskLogLogic logic = new YHJhTaskLogLogic();
  /**
   * 同步发送  一个任务
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String sendSynSeal(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
     // YHJhDocrecvInfo group = (YHJhDocrecvInfo) YHFOM.build(request.getParameterMap());
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      //获取所有印章
      String[]  fileter = {};
      List<YHJhSeal>  list = YHJhSealLogic.selectSeal(dbConn,  fileter);
      String XmlStr = "<tasks>";
      for (int i = 0; i < list.size(); i++) {
         XmlStr = XmlStr + "<task>"+ YHJhTaskLogLogic.toXML(list.get(i)) + "</task>";
      }
      XmlStr = XmlStr + "</tasks>";
      
      XmlStr = "<?xml version='1.0' encoding='UTF-8'?>"  + XmlStr;
       //此次打包任务名称
        String taskName = YHGuid.getRawGuid();
        String path = YHSysProps.getAttachPath() + File.separator + "uploadJTGW"
            + File.separator + taskName ;
        String FileName = "JH_SYN_SEAL_" + YHGuid.getRawGuid() + ".xml";
        while (YHDocUtil.getExist(path, FileName)) {
          FileName = "JH_SYN_SEAL_" + YHGuid.getRawGuid() + ".xml";
        }
        //获取本地ESB
        YHEsbClientConfig config = YHEsbClientConfig.builder(request.getRealPath("/")+ YHEsbConst.CONFIG_PATH) ;
        YHWSCaller caller = new YHWSCaller();
        String fileName  =path + File.separator + FileName;
        YHFileUtility.storeString2File(fileName, XmlStr);
          caller.setWS_PATH(config.getWS_PATH());
        //YHEsbMessage ms = new YHEsbMessage();
       // ms.setData(data);
       // ms.setMessage("sysDept");
        String sendDept =  "client2";
      // String ret = caller.send (fileName, sendDept, config.getToken());
        //ret json串中code值为0时 改为发送中
       // Map map = YHFOM.json2Map(ret);
        
        /**
         * 同步所有的单位，广播消息
         */
       String ret= caller.broadcast(fileName, config.getToken());
         Map map = YHFOM.json2Map(ret);
       // System.out.println(ret);
        //添加任务日志信息
        
        YHDeptTreeLogic dtl = new YHDeptTreeLogic();
        YHExtDept ed = dtl.getDeptByEsbUser(dbConn,(config.getUserId()));//根据发送部门，查询外部组织机构
        String fromDept = "";
        String fromDeptName = "";
        
        if(ed != null){
          fromDept = ed.getDeptId()+"";//发送部门GUID
          fromDeptName = ed.getDeptName();//发送部门名称
        }
        
        /*
         * 获取所有接收单位，从外部组织机构上查询
         */
        String[] str = {"ESB_USER <> ''" ,"ESB_USER is not null"};
        List<YHExtDept>  listDept = YHDeptTreeLogic.select(dbConn, str);
        String msg = "";
        if(!YHUtility.isNullorEmpty(sendDept)){
          String[] sendDeptA = sendDept.split(",");
          String code  = "";
          String guId = "";
          if(map != null){
             code = (String) map.get("code") == null ? "" : (String) map.get("code") ;
             guId = ((String)map.get("guid")) == null ? "" : ((String)map.get("guid"));
          }
          
       /*   //{"code": "-7", "msg": "令牌不对！"}
          {code:'-3',msg:'发送的文件不存在！'} 
          {code:'0',msg:'文件正在发送中......'} 
          {code:'-2',msg:'获取主机配置不正确 ！'} 
          {code: "-7", msg: "令牌不对！"} 
          {msg:'未知错误!'}*/
          
          if(code.equals("-7")){
            msg  = "令牌不对！";
          }else if(code.equals("-3")){
            msg  = "发送的文件不存在！";
          }else if(code.equals("-2")){
            msg  = "获取主机配置不正确 ！";
          }else if(code.equals("0")){
            
          }else{
            msg  = "未知错误!";
          }
          if(!YHUtility.isNullorEmpty(msg)){
            request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
            request.setAttribute(YHActionKeys.RET_MSRG, msg);
            return "/core/inc/rtjson.jsp";
          }
          Date data = new Date();
          for (int i = 0; i < listDept.size(); i++) {
            YHJhTaskLog task = new YHJhTaskLog();
            YHExtDept toDeptObj  = listDept.get(i);
            //YHExtDept toDeptObj = dtl.getDeptByEsbUser(dbConn,sendDeptA[i]);//根据接收部门，查询外部组织机构
            String toDept = "";
            String toDeptName = "";
            if(toDeptObj != null){
              toDept = toDeptObj.getDeptId()+"";//部门GUID
              toDeptName = toDeptObj.getDeptName();//部门名称
            }
            
            if(fromDept.equals(toDept)){//如果是本单位不需要发送
              continue;
            }
            task.setUserId(user.getSeqId() + "");
            task.setUserName(user.getUserName());
            task.setType("2");//1：公章
            task.setStatus("0");//发送
            task.setOptTime(data);
            task.setGuid(guId);
            task.setFromDept(fromDept);//发送部门
            task.setFromDeptName(fromDeptName);//发送部门名称
            task.setToDept(toDept);//接收部门
            task.setToDeptName(toDeptName);//
            YHJhTaskLogLogic.add(dbConn, task);
          }
          //系统日志
          YHSysLogLogic.addSysLog(dbConn, "62", "同步全体单位公章" ,user , request.getRemoteAddr());
          
          
        }
        

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
      
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  
  /**
   * 同步，单个任务发送
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String sendSynSealOnly(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
     // YHJhDocrecvInfo group = (YHJhDocrecvInfo) YHFOM.build(request.getParameterMap());
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      String  Type = request.getParameter("type") == null ? "" : request.getParameter("type");
      String synDept = request.getParameter("reciveDept") == null ? "" :  request.getParameter("reciveDept") ;
      String synDeptName = request.getParameter("reciveDeptName") == null ? "" :  request.getParameter("reciveDeptName") ;
      
      //获取本地ESB
      YHEsbClientConfig config = YHEsbClientConfig.builder(request.getRealPath("/")+ YHEsbConst.CONFIG_PATH) ;
      YHWSCaller caller = new YHWSCaller();
      YHEsbService esbService = new YHEsbService();
      YHDeptTreeLogic dtl = new YHDeptTreeLogic();
      YHExtDept ed = dtl.getDeptByEsbUser(dbConn,(config.getUserId()));//根据发送部门，查询外部组织机构
      String fromDept = "";
      String fromDeptName = "";
      
      if(ed != null){
        fromDept = ed.getDeptId()+"";//发送部门GUID
        fromDeptName = ed.getDeptName();//发送部门名称
      }
      
      if(Type.equals("1")){//所有同步发送
        
        /*
         * 获取所有接收单位，从外部组织机构上查询
         */
        String[] str = {"ESB_USER <> ''" ,"ESB_USER is not null"};
        List<YHExtDept>  listDept = YHDeptTreeLogic.select(dbConn, str);
        
        Date date = new Date();
        for (int i = 0; i < listDept.size(); i++) {
          YHExtDept dept = listDept.get(i);
          String deptCode = dept.getDeptCode();
          String esbUser = dept.getEsbUser();
           String toDept  = dept.getDeptId()+"";//部门GUID
          if(fromDept.equals(toDept)){//如果是本单位不需要发送
            continue;
          }
          
         String msg = sendSealOnly( dbConn, esbService, user,
                dept, config, fromDept, fromDeptName, date) ;
          if(!YHUtility.isNullorEmpty(msg)){
            request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
            request.setAttribute(YHActionKeys.RET_MSRG, msg);
            return "/core/inc/rtjson.jsp";
          }
       
        }
        
      }else{//选择发送
        if(!YHUtility.isNullorEmpty(synDept)){
          String[] str = {"DEPT_ID in(" + synDept + ")"};
          List<YHExtDept>  listDept = YHDeptTreeLogic.select(dbConn, str);
          Date date = new Date();
          for (int i = 0; i < listDept.size(); i++) {
            YHExtDept dept = listDept.get(i);
            String deptCode = dept.getDeptCode();
            String esbUser = dept.getEsbUser();
             String toDept  = dept.getDeptId()+"";//部门GUID
            if(fromDept.equals(toDept)){//如果是本单位不需要发送
              continue;
            }
           String msg = sendSealOnly( dbConn, esbService, user,
                  dept, config, fromDept, fromDeptName, date) ;
            if(!YHUtility.isNullorEmpty(msg)){
              request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
              request.setAttribute(YHActionKeys.RET_MSRG, msg);
              return "/core/inc/rtjson.jsp";
            }
          }
        }
      }
      //系统日志
      YHSysLogLogic.addSysLog(dbConn, "62", "同步公章" ,user , request.getRemoteAddr());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
      
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 发送   
   * 
   * @param dbConn
   * @param esbService  
   * @param filePath 文件路径
   * @param reciveDeptDescClient  发送客户端
   * @param config  配置文件
   *    dept : 外部组织机构
   * @return
   * @throws Exception 
   */
  public String sendSealOnly(Connection dbConn,YHEsbService esbService,YHPerson user,
      YHExtDept  dept,YHEsbClientConfig config,String fromDept,String fromDeptName,Date date) throws Exception{
   // String ret = esbService.send(filePath, reciveDeptDescClient,config.getToken(), "JHdoc");

    String msg = "";
      String deptCode = dept.getDeptCode();
      String esbUser = dept.getEsbUser();
      String deptName = dept.getDeptName();
      //YHExtDept toDeptObj = dtl.getDeptByEsbUser(dbConn,sendDeptA[i]);//根据接收部门，查询外部组织机构
      String toDept = "";
      String toDeptName = "";
      if(dept != null){
        toDept = dept.getDeptId()+"";//部门GUID
        toDeptName = dept.getDeptName();//部门名称
      }

    //获取所有印章
      String[]  fileter = {"OUT_DEPT_ID='" + deptCode + "'"};
      List<YHJhSeal>  list = YHJhSealLogic.selectSeal(dbConn,  fileter);
      String fileName = createXml( list ) ;
      String ret = esbService.send(fileName, esbUser,config.getToken(), "JHdoc", "");
      String code  = "";
      String guId = "";

      Map map = YHFOM.json2Map(ret);
      if(map != null){
         code = (String) map.get("code") == null ? "" : (String) map.get("code") ;
         guId = ((String)map.get("guid")) == null ? "" : ((String)map.get("guid"));
      }
      
      if(code.equals("-7")){
        msg  = "令牌不对！";
      }else if(code.equals("-3")){
        msg  = "发送的文件不存在！";
      }else if(code.equals("-2")){
        msg  = "获取主机配置不正确 ！";
      }else if(code.equals("0")){
        
      }else{
        msg  = "未知错误!";
      }

      YHJhTaskLog task = new YHJhTaskLog();
      task.setUserId(user.getSeqId() + "");
      task.setUserName(user.getUserName());
      task.setType("2");//1：公章
      task.setStatus("0");//发送
      task.setOptTime(date);
      task.setGuid(guId);
      task.setFromDept(fromDept);//发送部门
      task.setFromDeptName(fromDeptName);//发送部门名称
      task.setToDept(toDept);//接收部门
      task.setToDeptName(toDeptName);//
      YHJhTaskLogLogic.add(dbConn, task);

    return msg;
  }
  
  
  public static String createXml(List<YHJhSeal>  list ) throws Exception, IOException{
    String XmlStr = "<tasks>";
    for (int j = 0; j < list.size(); j++) {
       XmlStr = XmlStr + "<task>"+ YHJhTaskLogLogic.toXML(list.get(j)) + "</task>";
    }
    XmlStr = XmlStr + "</tasks>";
    
    XmlStr = "<?xml version='1.0' encoding='UTF-8'?>"  + XmlStr;
     //此次打包任务名称
    String taskName = YHGuid.getRawGuid();
    String path = YHSysProps.getAttachPath() + File.separator + "uploadJTGW"
        + File.separator + taskName ;
    String FileName = "JH_SYN_SEAL_" + YHGuid.getRawGuid() + ".xml";
    while (YHDocUtil.getExist(path, FileName)) {
      FileName = "JH_SYN_SEAL_" + YHGuid.getRawGuid() + ".xml";
    }
    String fileName  =path + File.separator + FileName;
    YHFileUtility.storeString2File(fileName, XmlStr);
    return fileName;
  }
  /**
   * 获取最近同步记录
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getLastTask(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
     // YHJhDocrecvInfo group = (YHJhDocrecvInfo) YHFOM.build(request.getParameterMap());
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String sendType = request.getParameter("sendType") == null ? "" : request.getParameter("sendType") ;
       //获取所有印章
      List<YHJhTaskLog>  list = logic.getSynTaskInfo(dbConn, sendType, 10);
      String data = "[";
      for (int i = 0; i < list.size(); i++) {
        data = data + YHFOM.toJson(list.get(i)) + ",";
      }
      if(list.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data +"]";
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  
   /**
   * 根据GUID获取所有的发送任务详情
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getTaskByGuid(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
     // YHJhDocrecvInfo group = (YHJhDocrecvInfo) YHFOM.build(request.getParameterMap());
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String guid = request.getParameter("guid") == null ? "" : request.getParameter("guid");
      String optTime = request.getParameter("optTime") == null ? "" : request.getParameter("optTime");
      
      //获取所有印章
      List<YHJhTaskLog>  list = new ArrayList<YHJhTaskLog>();
     
      
      if(YHUtility.isNullorEmpty(optTime)){
        String[] fileter = {"guid ='"+ guid+ "'"};
        list = logic.select(dbConn, fileter);
      }else{
        String[]  fileter = {YHDBUtility.getDateFilter("OPT_TIME", optTime, "=")};
        list = logic.select(dbConn, fileter);
      }
      
      String data = "[";
      for (int i = 0; i < list.size(); i++) {
        data = data + YHFOM.toJson(list.get(i)) + ",";
      }
      if(list.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data +"]";
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
  * 根据GUID获取所有任务数
  * 
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String   getCountByGuid(HttpServletRequest request, HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     dbConn = requestDbConn.getSysDbConn();
    // YHJhDocrecvInfo group = (YHJhDocrecvInfo) YHFOM.build(request.getParameterMap());
     YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
     String guid = request.getParameter("guid") == null ? "" : request.getParameter("guid");
     
     String optTime = request.getParameter("optTime") == null ? "" : request.getParameter("optTime");
     //获取所有所有记录数
     String countAll = logic.getCountByGuid(dbConn, guid,optTime,"");//所有的
     String count = logic.getCountByGuid(dbConn, guid,optTime,"2");//对方接收成功
    
     request.setAttribute(YHActionKeys.RET_DATA, "{esbCountAll:\"" + countAll + "\",esbCount:\"" + count + "\"}");
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
   } catch (Exception e) {
     e.printStackTrace();
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
   }
   return "/core/inc/rtjson.jsp";
 }
  

}
