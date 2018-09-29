package yh.core.funcs.person.act;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.psit.struts.entity.LimUser;



import yh.core.data.YHAuthKeys;
import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.exps.YHInvalidParamException;
import yh.core.funcs.calendar.data.YHCalendar;
import yh.core.funcs.calendar.logic.YHCalendarLogic;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHSecureKey;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.person.logic.YHSecureCardLogic;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.funcs.seclog.logic.YHSecLogUtil;
import yh.core.funcs.setdescktop.mypriv.logic.YHMyprivLogic;
import yh.core.funcs.system.act.YHSystemAct;
import yh.core.funcs.system.act.adapter.YHLoginAdapter;
import yh.core.funcs.system.act.filters.YHBindIpValidator;
import yh.core.funcs.system.act.filters.YHExistUserValidator;
import yh.core.funcs.system.act.filters.YHForbidLoginValidator;
import yh.core.funcs.system.act.filters.YHInitialPwValidator;
import yh.core.funcs.system.act.filters.YHIpRuleValidator;
import yh.core.funcs.system.act.filters.YHPasswordValidator;
import yh.core.funcs.system.act.filters.YHPwExpiredValidator;
import yh.core.funcs.system.act.filters.YHRepeatLoginValidator;
import yh.core.funcs.system.act.filters.YHRetryLoginValidator;
import yh.core.funcs.system.act.filters.YHUsbkeyValidator;
import yh.core.funcs.system.act.filters.YHVerificationCodeValidator;
import yh.core.funcs.system.ispirit.communication.YHMsgPusher;
import yh.core.funcs.system.ispirit.n12.org.act.YHIsPiritOrgAct;
import yh.core.funcs.system.logic.YHSystemLogic;
import yh.core.funcs.system.security.data.YHSecurity;
import yh.core.funcs.system.security.logic.YHSecurityLogic;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.global.YHRegistProps;
import yh.core.global.YHSysProps;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHGuid;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHPassEncrypt;
import yh.core.util.auth.YHRegistUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

public class YHPersonAct {
  YHPersonLogic personLogic = new YHPersonLogic();
 
  /**
   * 系统日志：获取部门DEPT_ID,DEPT_NAME,DEPT_PARENT
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPersonLog(Connection dbConn, int deptId) throws Exception {
    String data = "";
    YHDeptLogic dl = new YHDeptLogic();
    data = dl.getDeptNameLogic(dbConn, deptId);
    return data;
  }
  
  /**
   * 获取新建部门SEQ_ID（用于系统日志）
   * @param dbConn
   * @param deptId
   * @return
   * @throws Exception
   */
  
  public String getPersonAddSeq(Connection dbConn) throws Exception {
    String data = "";
    YHDeptLogic dl = new YHDeptLogic();
    data = dl.getDeptAddSeqLogic(dbConn);
    return data;
  }
  
 /**
  * 判断是否注册
  * @param request
  * @param response
  * @return
  * @throws Exception
  * data = 1 注册， 0 未注册
  */
  
  public String getRegistOrg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String data = "";
      if(YHUtility.isNullorEmpty(YHRegistProps.getProp(YHAuthKeys.REGIST_ORG + ".yh"))){
        data = "0";
      }else{
        data = "1";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取系统用户数量，并判断是否能新建用户
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRegistNum(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPersonLogic pl = new YHPersonLogic();
      String data = "";
      int permitUserCnt = YHRegistUtility.getUserCnt();
      if(pl.getNotLoginNum(dbConn) < permitUserCnt){
        data = "1";
      }else{
        data = "0";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 非注册版，控制30用户
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getNoRegistNum(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPersonLogic pl = new YHPersonLogic();
      String data = "";
      int permitUserCnt = YHRegistUtility.getUserCnt();
      
      if(pl.getNotLoginNum(dbConn) < permitUserCnt){
        data = "1";
      }else{
        data = "0";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * CMS注册用户
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String regist(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
	  Connection dbConn = null;
	  PreparedStatement ps = null;
	  ResultSet rs = null;
	  String user_id=request.getParameter("user_id");
	  String password=request.getParameter("password");
	  String user_name=request.getParameter("username");
	  String duty_type=request.getParameter("duty_type");
	  String dept_id=request.getParameter("dept_id");
	  String POST_PRIV=request.getParameter("POST_PRIV");
	  String USER_PRIV=request.getParameter("USER_PRIV");
	  password = YHPassEncrypt.encryptPass(password);
	  try{
		  YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
		  dbConn = requestDbConn.getSysDbConn();
	      String sql="insert into person(user_id ,user_name,duty_type,dept_id,POST_PRIV,PASSWORD,USER_PRIV) values(?,?,?,?,?,?,?)";
	      ps = dbConn.prepareStatement(sql);
			ps.setString(1, user_id);
			ps.setString(2, user_name);
			ps.setString(3, duty_type);
			ps.setString(4, dept_id);
			ps.setString(5, POST_PRIV);
			ps.setString(6, password);
			ps.setString(7, USER_PRIV);
			ps.execute();
	  }catch(Exception e){
		  e.printStackTrace();
	  }finally {
			YHDBUtility.close(ps, rs, null);
		}
	  YHSystemAct sa=new YHSystemAct();
	  sa.doLoginIn2(request,response,user_id,password);
	  request.setAttribute(YHActionKeys.RET_MSRG, "1");
	  return "/core/inc/rtjson.jsp";
  }
  /**
   * 新建用户
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String addPerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    int num = 0;
    Map map = new HashMap();
    String userId = "";
    YHPerson person=new YHPerson();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      String sql = "insert into person (";
      String values = " (";
      person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String openedAutoSelect = request.getParameter("openedAutoSelect");
      int deptId = 0;
      String userName = "";
      String password = request.getParameter("password");
      if(YHUtility.isNullorEmpty(password)){
        password = "";
      }
      String sex = request.getParameter("sex");
      if(YHUtility.isNullorEmpty(sex)){
        sex = "0";
      }
      String emailCapacitys = request.getParameter("emailCapacity");
      int emailCapacity = 0;
      
      if(YHUtility.isNullorEmpty(emailCapacitys)){
        emailCapacity = 100;
      }else if (YHUtility.isInteger(request.getParameter("emailCapacity"))) {
        emailCapacity = Integer.parseInt(request.getParameter("emailCapacity"));
      }
      String folderCapacity = request.getParameter("folderCapacity");
      if(!YHUtility.isInteger(folderCapacity)){
        folderCapacity = "100";
      }
      if(openedAutoSelect.trim().equals("1")){
        userId = request.getParameter("userId");
        userId = userId.replace("\\", "").replace("\"", "").replace("\'", "").replace("\r", "").replace("\n", "");
        userName = request.getParameter("userName");
        userName = userName.replace("\\", "").replace("\"", "").replace("\'", "").replace("\r", "").replace("\n", "");
        String userPriv = request.getParameter("userPriv");
        String userPrivOther = request.getParameter("role");
        String postPriv = request.getParameter("postPriv");
        String postDept = request.getParameter("postDeptId");
        String deptIdOther = request.getParameter("dept");
        int dutyType = 0;
        try {
          dutyType = Integer.parseInt(request.getParameter("dutyType"));
        }catch(Exception ex) {          
        }
        int deptIdLoca = 0;
        try {
          deptIdLoca = Integer.parseInt(request.getParameter("deptIdLoca"));
        }catch(Exception ex) {          
        }
        
        if(request.getParameter("deptId").equals("")){
          deptId = deptIdLoca;
        }else{
          deptId = Integer.parseInt(request.getParameter("deptId"));
        }
        num = personLogic.selectPerson(dbConn, userId);
        if(num >= 1) {
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "用户名重复, 用户名不能重复");
          return "/core/inc/rtjson.jsp";
        }
        if(userPriv==null || Integer.valueOf(userPriv)<1)
        userPriv=personLogic.getNoPriv(dbConn);
        map.put("userId" , userId);
        map.put("userName" , userName);
        map.put("userPriv" , userPriv);
        map.put("postPriv" , postPriv);
        map.put("deptId" , deptId);
        map.put("postDept" , postDept);
        map.put("userPrivOther" , userPrivOther);
        map.put("deptIdOther" , deptIdOther);
        map.put("dutyType" , dutyType);
      }
      String openedFlowDispatch = request.getParameter("openedFlowDispatch");
      if(openedFlowDispatch.trim().equals("1")){
        String userNoStr = request.getParameter("userNo");
        int userNo = 10;
        if(YHUtility.isNullorEmpty(userNoStr)){
          userNo = 10;
        }else if (YHUtility.isInteger(userNoStr)) {
          userNo = Integer.parseInt(request.getParameter("userNo"));
        }
        //int canbroadcast = Integer.parseInt(request.getParameter("canbroadcast"));
        String notLogin = request.getParameter("notLogin");
        if (YHUtility.isNullorEmpty(notLogin)){
          notLogin = "0";
        }else{
          notLogin = "1";
        }
        String notViewUser = request.getParameter("notViewUser");
        if(YHUtility.isNullorEmpty(notViewUser)){
          notViewUser = "0";
        }else{
          notViewUser = "1";
        }
        String notViewTable = request.getParameter("notViewTable");
        if(YHUtility.isNullorEmpty(notViewTable)){
          notViewTable = "0";
        }else{
          notViewTable = "1";
        }
        String useingKey = request.getParameter("useingKey");
        if(YHUtility.isNullorEmpty(useingKey)){
          useingKey = "0";
        }else{
          useingKey = "1";
        }
        map.put("notLogin", notLogin);
        map.put("notViewUser", notViewUser);
        map.put("notViewTable", notViewTable);
        map.put("useingKey", useingKey);
        map.put("userNo" , userNo);
        int		canbroadcast=0;
        if(request.getParameter("canbroadcast")!=null && !request.getParameter("canbroadcast").equals(""))
        {
         canbroadcast = Integer.parseInt(request.getParameter("canbroadcast"));
        }
        map.put("canbroadcast" , canbroadcast);
        int imRange=0;
        if(request.getParameter("imRange")!=null && !request.getParameter("imRange").equals(""))
        { imRange = Integer.parseInt(request.getParameter("imRange"));}
        map.put("imRange" , imRange);
      }else{
        map.put("notLogin", "0");
        map.put("notViewUser", "0");
        map.put("notViewTable", "0");
        map.put("useingKey", "0");
      }
      
      String openedWarnDispatch = request.getParameter("openedWarnDispatch");
      if(openedWarnDispatch.trim().equals("1")){
        String webmailNums = request.getParameter("webmailNum");
        int webmailNum = 0;
        if(YHUtility.isNullorEmpty(webmailNums)){
          webmailNum = 0;
        }else if (YHUtility.isInteger(webmailNums)) {
          webmailNum = Integer.parseInt(request.getParameter("webmailNum"));
        }
        String webmailCapacitys = request.getParameter("webmailCapacity");
        int webmailCapacity = 0;
        if(YHUtility.isNullorEmpty(webmailCapacitys)){
          webmailCapacity = 0;
        }else if (YHUtility.isInteger(webmailCapacitys)) {
          webmailCapacity = Integer.parseInt(request.getParameter("webmailCapacity"));
        }
        String bindIp = request.getParameter("bindIp");
        String remark = request.getParameter("remark");
        
        map.put("webmailNum" , webmailNum);
        map.put("webmailCapacity", webmailCapacity);
        map.put("bindIp" , bindIp);
        map.put("remark" , remark);
      }
      
      String openedOtherDispatch = request.getParameter("openedOtherDispatch");
      if(openedOtherDispatch.trim().equals("1")){
        String byname = request.getParameter("byname");
        if(!YHUtility.isNullorEmpty(byname)){
          byname = byname.replace("\\", "\\\\").replace("\"", "").replace("\'", "").replace("\r", "").replace("\n", "");
        }
        String birthday = request.getParameter("birthday");   
        String theme = request.getParameter("theme");
        String mobilNo = request.getParameter("mobilNo");
        String mobilNoHidden = request.getParameter("mobilNoHidden");
        if(YHUtility.isNullorEmpty(mobilNoHidden)){
          mobilNoHidden = "0";
        }else{
          mobilNoHidden = "1";
        }
        String email = request.getParameter("email");
        String telNoDept = request.getParameter("telNoDept");
        
        map.put("byname" , byname);
        map.put("birthday" ,birthday);   
        map.put("theme" , theme);
        map.put("mobilNo", mobilNo);
        map.put("mobilNoHidden", mobilNoHidden);
        map.put("email", email);
        map.put("telNoDept", telNoDept);
      }
      map.put("sex" , sex);
      map.put("password" , YHPassEncrypt.encryptPass(password));
      map.put("emailCapacity" , emailCapacity);
      map.put("folderCapacity" , folderCapacity);
      map.put("auatar", "default.gif");
      map.put("shortcut", "0204,020802,0216,0217,0224,0228,0232,0220,");
      YHORM orm = new YHORM();
	    try {
		   orm.saveSingle(dbConn , "person" , map);

	    }catch (Exception ex) {
	    	ex.printStackTrace();
	    }
      
      //orm.saveSingle(dbConn , "person" , map);
      
      int maxSeqId = YHCalendarLogic.getMaSeqId(dbConn, "PERSON");
      YHPerson per=(YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, maxSeqId);
      String deptName = personLogic.getDeptNameLogic(dbConn, per.getDeptId());
      String remark = "{deptId:'"+deptName+"',userName：'"+per.getUserName()+"',userId:'"+per.getUserId() + "',userPriv:" + per.getUserPriv() + "}";
      YHSecLogUtil.log(dbConn, person,request.getRemoteAddr(), "210",per.toJsonLog(),"1", "添加人员姓名为" + per.getUserName() + ",部门为" + deptName +",用户Id为" + per.getUserId());


      //String deptName = personLogic.getDeptNameLogic(dbConn, deptId);
      //String remark = "["+deptName+"]"+userName+",USER_ID="+userId;
      YHSysLogLogic.addSysLog(dbConn, YHLogConst.ADD_USER, remark, person.getSeqId(), request.getRemoteAddr());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功添加人员");

      //生成org.xml文件
      YHIsPiritOrgAct.getOrgDataStream(dbConn);
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      // add  seclog
      try{
        YHORM orm = new YHORM();
        YHPerson pe=(YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, map);
       YHSecLogUtil.log(dbConn, person,request.getRemoteAddr(), "210", map,"0", ex.getMessage());
      }catch(Exception e){
        
      }
    
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  

  /**
   * 编辑用户
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String updatePerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null; 
    try {
      YHPerson login = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      Map map = new HashMap();
      String userId = "";
      String userName = "";
      String userPriv ="";
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      int deptId = 0;
      String openedAutoSelect = request.getParameter("openedAutoSelect");
      if(openedAutoSelect.trim().equals("1")){
        //userId = request.getParameter("userId");
        //userId = userId.replace("\\", "").replace("\"", "").replace("\'", "").replace("\r", "").replace("\n", "");
        userName = request.getParameter("userName");
        userName = userName.replace("\\", "").replace("\"", "").replace("\'", "").replace("\r", "").replace("\n", "");
        userPriv = request.getParameter("userPriv");
        String userPrivOther = request.getParameter("role");
        String postPriv = request.getParameter("postPriv");
        String deptIdStr = request.getParameter("deptIdStr");
        if(request.getParameter("deptId").equals(deptIdStr.trim())){
          deptId = Integer.parseInt(request.getParameter("deptId"));
        }else{
          if("".equals(deptIdStr.trim())){
            //deptId = 0;
          }else{
            deptId = Integer.parseInt(deptIdStr);
          }
        }
        //System.out.println(deptId+"UIUIIUIUIUIU");
        String postDept = request.getParameter("postDeptId");
        //System.out.println(postDept);
        String deptIdOther = request.getParameter("dept");
        int dutyType = 0;
        if(YHUtility.isNullorEmpty(request.getParameter("dutyType"))){
          dutyType = 0;
        }else{
          dutyType = Integer.parseInt(request.getParameter("dutyType"));
        }
        //map.put("userId" , YHDBUtility.escapeLike(userId));
        map.put("userName" , userName);
        map.put("userPriv" , userPriv);
        map.put("postPriv" , postPriv);
        map.put("deptId" , deptId);
        map.put("postDept" , postDept);
        map.put("userPrivOther" , userPrivOther);
        map.put("deptIdOther" , deptIdOther);
        map.put("dutyType" , dutyType);
      }
      String openedFlowDispatch = request.getParameter("openedFlowDispatch");
      if(openedFlowDispatch.trim().equals("1")){
        //System.out.println(request.getParameter("userNo")+"TTTTTTT1111");
        int userNo = 10;
        if (YHUtility.isInteger(request.getParameter("userNo"))) {
          userNo = Integer.parseInt(request.getParameter("userNo"));
        }
        //int canbroadcast = Integer.parseInt(request.getParameter("canbroadcast"));
        String notLogin = request.getParameter("notLogin");
        String notLoginFlag = request.getParameter("notLoginFlag");
        if(YHUtility.isNullorEmpty(notLogin)){
          YHPersonLogic pl = new YHPersonLogic();
          if(YHUtility.isNullorEmpty(YHRegistProps.getProp(YHAuthKeys.REGIST_ORG + ".yh"))){
            int permitUserCntNo = YHRegistUtility.getUserCnt();
            if(pl.getNotLoginNum(dbConn) < permitUserCntNo){
              notLogin = "0";
            }else{
              if("0".equals(notLoginFlag)){
                notLogin = "0";
              }else{
                request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
                request.setAttribute(YHActionKeys.RET_MSRG, "已经达到系统的最大授权用户数(30)，不能再增加允许登录OA用户");
                return "/core/inc/rtjson.jsp";
              }
            }
          }else{
            int permitUserCnt = YHRegistUtility.getUserCnt();
            if(pl.getNotLoginNum(dbConn) < permitUserCnt){
              notLogin = "0";
            }else{
              request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
              request.setAttribute(YHActionKeys.RET_MSRG, "已经达到系统的最大授权用户数(" + YHRegistUtility.getUserCnt() +")，不能再增加允许登录OA用户");
              return "/core/inc/rtjson.jsp";
            }
          }
          
        }else{
          notLogin = "1";
        }
        
        String notViewUser = request.getParameter("notViewUser");
        if(YHUtility.isNullorEmpty(notViewUser)){
          notViewUser = "0";
        }else{
          notViewUser = "1";
        }
        String notViewTable = request.getParameter("notViewTable");
        if(YHUtility.isNullorEmpty(notViewTable)){
          notViewTable = "0";
        }else{
          notViewTable = "1";
        }
        String useingKey = request.getParameter("useingKey");
        if(YHUtility.isNullorEmpty(useingKey)){
          useingKey = "0";
        }else{
          useingKey = "1";
        }
        map.put("notLogin", notLogin);
        map.put("notViewUser", notViewUser);
        map.put("notViewTable", notViewTable);
        map.put("useingKey", useingKey);
        map.put("userNo" , userNo);
        int		canbroadcast=0;
        if(request.getParameter("canbroadcast")!=null && !request.getParameter("canbroadcast").equals(""))
        {
         canbroadcast = Integer.parseInt(request.getParameter("canbroadcast"));
        }
        map.put("canbroadcast" , canbroadcast);
        int imRange=0;
        if(request.getParameter("imRange")!=null && !request.getParameter("imRange").equals(""))
        { imRange = Integer.parseInt(request.getParameter("imRange"));}
        map.put("imRange" , imRange);
      }
      
      String openedWarnDispatch = request.getParameter("openedWarnDispatch");
      if(openedWarnDispatch.trim().equals("1")){
        int emailCapacity = 0;
        if (YHUtility.isInteger(request.getParameter("emailCapacity"))) {
          emailCapacity = Integer.parseInt(request.getParameter("emailCapacity"));
        }
        String folderCapacity = request.getParameter("folderCapacity");
        int webmailNum = 0;
        if(YHUtility.isNullorEmpty(request.getParameter("webmailNum"))){
          webmailNum = 0;
        }else if (YHUtility.isInteger(request.getParameter("webmailNum"))) {
          webmailNum = Integer.parseInt(request.getParameter("webmailNum"));
        }
        int webmailCapacity = 0;
        if(YHUtility.isNullorEmpty(request.getParameter("webmailCapacity"))){
          webmailCapacity = 0;
        }else if (YHUtility.isInteger(request.getParameter("webmailCapacity"))) {
          webmailCapacity = Integer.parseInt(request.getParameter("webmailCapacity"));
        }
        String bindIp = request.getParameter("bindIp");
        String remark = request.getParameter("remark");
        
        map.put("emailCapacity" , emailCapacity);
        map.put("folderCapacity" , folderCapacity);
        map.put("webmailNum" , webmailNum);
        map.put("webmailCapacity", webmailCapacity);
        map.put("bindIp" , bindIp);
        map.put("remark" , remark);
      }
      
      String openedOtherDispatch = request.getParameter("openedOtherDispatch");
      if(openedOtherDispatch.trim().equals("1")){
        String byname = request.getParameter("byname");
        if(!YHUtility.isNullorEmpty(byname)){
          byname = byname.replace("\\", "\\\\").replace("\"", "").replace("\'", "").replace("\r", "").replace("\n", "");
        }
        String sex = request.getParameter("sex");
        String birthday = request.getParameter("birthday");   
        String theme = request.getParameter("theme");
        String mobilNo = request.getParameter("mobilNo");
        String mobilNoHidden = request.getParameter("mobilNoHidden");
        if(YHUtility.isNullorEmpty(mobilNoHidden)){
          mobilNoHidden = "0";
        }else{
          mobilNoHidden = "1";
        }
        String email = request.getParameter("email");
        String telNoDept = request.getParameter("telNoDept");
        
        map.put("byname" , byname);
        //map.put("password" , password);
        map.put("sex" , sex);
        map.put("birthday" ,birthday);   
        map.put("theme" , theme);
        map.put("mobilNo", mobilNo);
        map.put("mobilNoHidden", mobilNoHidden);
        map.put("email", email);
        map.put("telNoDept", telNoDept);
      }
      map.put("seqId" , seqId);
      YHORM orm = new YHORM();
//      if(userId.trim().equals(userIdOld.trim())) {
        //orm.updateSingle(dbConn, person);
      //修改之前
      YHPerson OldPer=(YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, seqId);
      String oldDeptName = personLogic.getDeptNameLogic(dbConn, OldPer.getDeptId());//部门名称
      YHUserPrivLogic tupl = new YHUserPrivLogic();
      String userP = OldPer.getUserPriv();//主角色
      String oldPrivName = "";
      String otherPrivNameOld = tupl.getNameByIdStr(OldPer.getUserPrivOther(),dbConn);//辅助角色
      if(YHUtility.isInteger(userP)){
         oldPrivName = tupl.getNameById(Integer.parseInt(userP), dbConn);
      }
     
      
      orm.updateSingle(dbConn, "person", map);
      
      // add  seclog
      try{
      YHPerson per=(YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, seqId);
      String deptName = personLogic.getDeptNameLogic(dbConn, per.getDeptId());
      String remark = "["+deptName+"]"+per.getUserName()+",USER_ID="+per.getUserId();
      
      
      String userPri = per.getUserPriv();
      String userPriName = "";//主角色
      if(YHUtility.isInteger(userPri)){
        userPriName = tupl.getNameById(Integer.parseInt(userPri), dbConn);
      }
      String otherPrivName = tupl.getNameByIdStr(per.getUserPrivOther(),dbConn);//辅助角色
      
     // YHSecLogUtil.log(dbConn, login, request.getRemoteAddr(), "210",userName+"(userPriv："+per.getUserPriv()+")","1",remark);
     
      YHSecLogUtil.log(dbConn, login,request.getRemoteAddr(), "210",per.toJsonLog(),"1", "修改人员姓名'" + OldPer.getUserName() + "'为'" + per.getUserName() + "', 部门\"" + oldDeptName + "\" 为 \"" + deptName +"\"," +
      		"主角色\"" + oldPrivName+ "\" 为 \"" +userPriName + "\"，辅助角色\" " + otherPrivNameOld + "\" 为  \"" + otherPrivName + "\"");

      }catch(Exception e){e.printStackTrace();}

      /*
      if(openedFlowDispatch.trim().equals("1")){
        Statement stm2 = null;
        int imRange = Integer.parseInt(request.getParameter("imRange"));
        String update = "update person set IM_RANGE = '" + imRange + "' where seq_id=" + seqId;
        try {
          stm2 = dbConn.createStatement();
          stm2.executeUpdate(update);
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm2, null, null); 
        }
      }*/
      updateDeptInHR(dbConn, seqId, String.valueOf(deptId));
      
      String deptNameStr = personLogic.getDeptNameLogic(dbConn, deptId);
      String remarks = "[" + deptNameStr + "]" + userName + ",USER_ID="
          + userId;
      YHSysLogLogic.addSysLog(dbConn, YHLogConst.EIDT_USER, remarks, login
          .getSeqId(), request.getRemoteAddr());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功修改人员");
        //return "/core/inc/rtjson.jsp";
//      }
//      num = personLogic.selectPerson(dbConn, userId);
//      if(num >= 1) {
//        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
//        request.setAttribute(YHActionKeys.RET_MSRG, "用户名重复, 用户名不能重复");
//        return "/core/inc/rtjson.jsp";
//      }
      //orm.updateSingle(dbConn, person);
      //orm.updateSingle(dbConn , "person" , map);
      //String deptName = personLogic.getDeptNameLogic(dbConn, deptId);
      //String remark = "["+deptName+"]"+userName+",USER_ID="+userId;
      //YHSysLogLogic.addSysLog(dbConn, YHLogConst.EIDT_USER, remark, person.getSeqId(), request.getRemoteAddr());
      //request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      //request.setAttribute(YHActionKeys.RET_MSRG,"成功修改人员");
      
    //生成org.xml文件
      YHIsPiritOrgAct.getOrgDataStream(dbConn);
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
     
      throw ex;
    }
    return "/core/inc/rtjson.jsp";    
  }
  
  private void updateDeptInHR(Connection dbConn, int seqId, String deptId) {
    try {
      Class<?> classObj = Class.forName("yh.subsys.oa.hr.manage.staffInfo.logic.YHHrStaffInfoLogic");
      Class<?>[] paramTypeArray = new Class[]{Connection.class, int.class, String.class};
      Method methodObj = classObj.getMethod("updateDeptId", paramTypeArray);
      methodObj.invoke(classObj.newInstance(), new Object[]{dbConn, seqId, deptId});
      
    } catch (ClassNotFoundException e){
      
    } catch (Exception e){
      
    }
  }
  
  private void syncDept(Connection dbConn) {
    try {
      Class<?> classObj = Class.forName("yh.subsys.oa.hr.manage.staffInfo.logic.YHHrStaffInfoLogic");
      Class<?>[] paramTypeArray = new Class[]{Connection.class};
      Method methodObj = classObj.getMethod("syncDept", paramTypeArray);
      methodObj.invoke(classObj.newInstance(), new Object[]{dbConn});
      
    } catch (ClassNotFoundException e){
      
    } catch (Exception e){
      
    }
  }
  
  public String listInDutyPerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String id = request.getParameter("id");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     
      dbConn = requestDbConn.getSysDbConn();
      ArrayList<YHPerson> personList = null;
      Map map = new HashMap();
      map.put("DEPT_ID", id);
      YHORM orm = new YHORM();
      personList = (ArrayList<YHPerson>)orm.loadListSingle(dbConn, YHPerson.class, map);
      request.setAttribute("personList", personList);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/person/indutypersonlist.jsp";
  }
  
  public String getOffDutyPerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String seqId = request.getParameter("seqId");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = null;
      StringBuffer data = null;
      YHORM orm = new YHORM();
      person = (YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, Integer.parseInt(seqId));
      if (person == null) {
        person = new YHPerson();
      }
      //System.out.println(seqId);
      data =YHFOM.toJson(person);
      //System.out.println(data.toString()+"XXXXXXXXXXXXXXXXXXXXXXXXXXXx");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  
  public String selectPerson(HttpServletRequest request, 
      HttpServletResponse response)throws Exception {
    String userId = request.getParameter("userId");
    Connection dbConn = null;
    int num = 0;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      String data = null;    
      num = personLogic.selectPerson(dbConn, userId);  
      //System.out.println("++++++++++++++++++===============num:" + num);
      if(num >= 1) {
        StringBuffer sb = new StringBuffer("[");
        sb.append("{");
        sb.append("num:\"" + num + "\"");
        sb.append("}");
        sb.append("]");
        data = sb.toString();
      }else if(num == 0) {
        StringBuffer sb = new StringBuffer("[");
        sb.append("{");
        sb.append("num:\"" + num + "\"");
        sb.append("}"); 
        sb.append("]");
        data = sb.toString();
      } 
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }   
    return "/core/inc/rtjson.jsp";
  }
  
  public String selectPersonName(HttpServletRequest request, 
      HttpServletResponse response)throws Exception {
    String byName = request.getParameter("byname");
    Connection dbConn = null;
    int num = 0;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      String data = null;    
      YHMyprivLogic logic = new YHMyprivLogic();
      //num = personLogic.selectPerson(dbConn, byName);   
      if(user.getUserId().equals(byName)){
        num = 1;
      }
      if(logic.checkByName(dbConn, byName) > 0){
        num = 1;
      }
      if(num == 1) {
        StringBuffer sb = new StringBuffer("[");
        sb.append("{");
        sb.append("num:\"" + num + "\"");
        sb.append("}");
        sb.append("]");
        data = sb.toString();
      }else if(num == 0) {
        StringBuffer sb = new StringBuffer("[");
        sb.append("{");
        sb.append("num:\"" + num + "\"");
        sb.append("}"); 
        sb.append("]");
        data = sb.toString();
      } 
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }   
    return "/core/inc/rtjson.jsp";
  }
  public String getTree(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    String idStr = request.getParameter("id");
    int id = 0;
    if (idStr != null && !"".equals(idStr.trim())) {
      id = Integer.parseInt(idStr);
    }
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      HashMap map = new HashMap();
      map.put("DEPT_PARENT", id);
      YHORM orm = new YHORM();
      StringBuffer sb = new StringBuffer("[");
      List<YHDepartment> deptlist = orm.loadListSingle(dbConn, YHDepartment.class, map);
      for(int i = 0; i < deptlist.size(); i++) {
        YHDepartment dept = deptlist.get(i);
        sb.append("{");
        sb.append("nodeId:\"" + dept.getSeqId() + "\"");
        sb.append(",name:\"" + dept.getDeptName() + "\"");
        sb.append(",isHaveChild:" + IsHaveChild(request, response, String.valueOf(dept.getSeqId())));
        sb.append(",imgAddress:\""+ request.getContextPath() +"/core/styles/style1/img/dtree/node_dept.gif\"");
        sb.append("},");
      }
      List personList = new ArrayList();
      personList.add("person");
      String[] filters = new String[]{"DEPT_ID = " + id};
      map = (HashMap)orm.loadDataSingle(dbConn, personList, filters);
      
      List<Map> list = (List<Map>) map.get("PERSON");
      for(Map m : list){
        sb.append("{");
        sb.append("nodeId:\"r" + m.get("seqId") + "\"");
        sb.append(",name:\"" + m.get("userName") + "\"");
        sb.append(",isHaveChild:" + 0);
        sb.append(",imgAddress:\""+ request.getContextPath() + "/core/styles/style1/img/dtree/0-1.gif\"");
        sb.append("},");
      } 
      sb.deleteCharAt(sb.length() - 1);
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());       
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String userState(HttpServletRequest request,
      HttpServletResponse response, int userId) throws Exception {
    Connection dbConn = null;
    String url = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPersonLogic pl = new YHPersonLogic();
      String userState = pl.getUserStateImg(dbConn, userId);
      if(userState.trim().equals("1")){
        url = "/core/styles/style1/img/dtree/0-1.gif\"";
      }else{
        url = "/core/styles/style1/img/dtree/0-2.gif\"";
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return url;
  }
  
  public int IsHaveChild(HttpServletRequest request,
      HttpServletResponse response,String id) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("DEPT_PARENT", id);
   // 判断是否有子部门
      List<YHDepartment> list = orm.loadListSingle(dbConn, YHDepartment.class,
          map);
      // 判断本部门是否有人
      //System.out.println(list.size() + "=FGHJT");
      String[] str = { "DEPT_ID =" + id };
      List<YHPerson> personList = orm.loadListSingle(dbConn, YHPerson.class,
          str);
      if (list.size() > 0 || personList.size() > 0) {
        return 1;
      } else {
        return 0;
      }
//      List<YHDepartment> list = orm.loadListSingle(dbConn, YHDepartment.class, map);
//      if(list.size() > 0){
//        return 1;
//      }else{
//        return 0;
//      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
  }
  public String getUserPrivByNo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String loginUserPriv = person.getUserPriv();
      String data = null;
      List<YHUserPriv> userPrivList = null;
      StringBuffer sb = new StringBuffer("[");
      YHORM orm = new YHORM();
      int privNo = Integer.parseInt(YHPersonLogic.getPrivNoStr(dbConn, loginUserPriv));
      String[] filters = null;
      if(!person.isAdminRole(dbConn)){
        filters = new String[]{"PRIV_NO > " + privNo +" and not SEQ_ID = 1 order by PRIV_NO ASC"};
      }else{
        filters = new String[]{"1=1 order by PRIV_NO ASC"};
      }
      userPrivList = orm.loadListSingle(dbConn, YHUserPriv.class, filters);
      for(int i = 0; i < userPrivList.size(); i++) {
        YHUserPriv userPriv = userPrivList.get(i);
        sb.append("{");
        sb.append("privNo:\"" + userPriv.getSeqId() + "\"");
        sb.append(",privName:\"" + userPriv.getPrivName() + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append("]");
      data = sb.toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  
  public String getUserPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      List<YHUserPriv> userPrivList = null;
      StringBuffer sb = new StringBuffer("[");
      YHORM orm = new YHORM();
      userPrivList = orm.loadListSingle(dbConn, YHUserPriv.class, new HashMap());
      for(int i = 0; i < userPrivList.size(); i++) {
        YHUserPriv userPriv = userPrivList.get(i);
        sb.append("{");
        sb.append("seqId:\"" + userPriv.getSeqId() + "\"");
        sb.append(",privName:\"" + userPriv.getPrivName() + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append("]");
      data = sb.toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  
  public String getGradeTree(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String idStr = request.getParameter("id");
    int id = 0;
    if (idStr != null && !"".equals(idStr.trim())) {
      id = Integer.parseInt(idStr);
    }
    YHOrgSelectLogic logic = new YHOrgSelectLogic();
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      StringBuffer sb = new StringBuffer("[");
      YHORM orm = new YHORM();
      HashMap map = new HashMap();
      map.put("DEPT_PARENT", id);
      String[] querys = new String[2];
      int login = person.getDeptId();
      ArrayList<YHDepartment> deptList = new ArrayList();
      if (person.getPostPriv().equals("0")) {    
        querys = new String[]{"SEQ_ID = "+login};
        deptList = (ArrayList<YHDepartment>) orm
        .loadListSingle(dbConn, YHDepartment.class, querys);
      } else {
        YHDepartment dept = logic.getDeptParentId(dbConn, person.getDeptId());
        if(id == 0){
          querys[0] = " DEPT_PARENT = " + dept.getDeptParent();
        }else{
          querys[0] ="DEPT_PARENT=" + id;
        }
        deptList = (ArrayList<YHDepartment>) orm
        .loadListSingle(dbConn, YHDepartment.class, querys);
      }
      //ArrayList<YHDepartment> deptListStr = null;
      //deptList = (ArrayList<YHDepartment>)orm.loadListSingle(dbConn, YHDepartment.class, querys);
      for(int i = 0; i < deptList.size(); i++) {
        YHDepartment depts = deptList.get(i);
        sb.append("{");
        sb.append("nodeId:\"" + depts.getSeqId() + "\"");
        sb.append(",name:\"" + depts.getDeptName() + "\"");
        sb.append(",isHaveChild:" + IsHaveSon(request, response, String.valueOf(depts.getSeqId())) + "");
        sb.append(",imgAddress:\""+ request.getContextPath() +"/core/styles/style1/img/dtree/node_dept.gif\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public int IsHaveSon(HttpServletRequest request,
      HttpServletResponse response,String id) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("DEPT_PARENT", id);
      List<YHDepartment> list = orm.loadListSingle(dbConn, YHDepartment.class, map);
      if(list.size() > 0){
        return 1;
      }else{
        return 0;
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
  }
  
  /**
   * 闲置状态：是否登陆过
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getLoginUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
    try{
      YHPerson person = (YHPerson)request.getSession().getAttribute("LONGIN_USER");
      //person = new YHPerson();
     // person.setUserName("ss");
     /// person.setUserPriv("xitong");
     // person.setUserId("1");
      if(person != null){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
        request.setAttribute(YHActionKeys.RET_DATA, person.toJSON()); 
      }else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未登陆");
      }
           
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "未登陆");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getUserByDept(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String sDeptId = request.getParameter("deptId"); 
    Connection dbConn = null;
    try {
      YHPersonLogic pl = new YHPersonLogic();
      List<YHPerson> list =  pl.getPersonByDept(Integer.parseInt(sDeptId) , dbConn);
      StringBuffer sb = new StringBuffer("[");
      if(list.size() > 0){
        for(YHPerson tmp : list){
          sb.append(tmp.toJsonSimple());
        }
        sb.deleteCharAt( sb.length() - 1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString()); 
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "未登陆");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 管理用户列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getManagePersonList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String deptId = request.getParameter("deptId");
      YHPersonLogic dl = new YHPersonLogic();
      String data = "";
    /*  if(person.isAdminRole() || postPriv.equals("1")){
        data = dl.getManagePersonList(dbConn,request.getParameterMap(), deptId, deptIdOther, loginUserPriv, isOaAdmin, deptIdLogin, postDept, postPriv);
      }else{
        if(dl.findId(postDept, deptId) || dl.exitDeptId(deptIdLogin, deptId)){
          data = dl.getManagePersonList(dbConn,request.getParameterMap(), deptId, deptIdOther, loginUserPriv, isOaAdmin, deptIdLogin, postDept, postPriv);
        }else{
          data = "{totalRecord:0,pageData:[]}";
        }
      }*/
      data = dl.getManagePersonList(dbConn,request.getParameterMap(),deptId, person);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  /**
   * 获取部门名称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getDeptName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptIdStr = request.getParameter("deptId");
      int deptId = Integer.parseInt(deptIdStr);
      YHPersonLogic dl = new YHPersonLogic();
      String data = dl.getDeptNameLogic(dbConn, deptId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取部门名称(带权限)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getPostPrivDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String postPriv = person.getPostPriv();
      int loginDeptId = person.getDeptId();
      int deptId = Integer.parseInt(request.getParameter("deptId"));
      String postDept = person.getPostDept();
      StringBuffer sb = new StringBuffer("[");
      YHMyPriv mp = YHPrivUtil.getMyPriv(dbConn, person , null,  2);
      boolean isPriv = YHPrivUtil.isDeptPriv(dbConn, deptId, mp, person);

      
      sb.append("{");
      sb.append("loginDeptId:\"" + loginDeptId + "\"");
      sb.append(",postPriv:\"" + postPriv + "\"");
      sb.append(",isPriv:" + isPriv);
      sb.append(",postDept:\"" + postDept + "\"");
      sb.append("}");
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 获取用户名称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getUserName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userIdStr = request.getParameter("userId");
      int userId = Integer.parseInt(userIdStr);
      YHPersonLogic dl = new YHPersonLogic();
      String data = dl.getUserNameLogic(dbConn, userId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取角色名称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getRoleName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String roleIdStr = request.getParameter("roleId");
      int roleId = Integer.parseInt(roleIdStr);
      YHPersonLogic dl = new YHPersonLogic();
      String data = dl.getRoleNameLogic(dbConn, roleId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 考勤排班类型
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getDutyType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String dutyIdStr = request.getParameter("dutyId");
      int dutyId = Integer.parseInt(dutyIdStr);
      YHPersonLogic dl = new YHPersonLogic();
      String data = dl.getDutyTypeLogic(dbConn, dutyId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String loginUserId = person.getUserId();
      String sumStrs = request.getParameter("sumStrs");
      String remark = "";
      YHSysLogLogic.addSysLog(dbConn, YHLogConst.DELETE_USER, remark, person.getSeqId(), request.getRemoteAddr());
      YHPersonLogic pl = new YHPersonLogic();
      // add  seclog
      try{
       remark=pl.getUserNamesLogic(dbConn, sumStrs);
      YHSecLogUtil.log(dbConn, person, request.getRemoteAddr(), "210", remark+"(seqId->"+sumStrs+")","1", "删除人员(" + remark + ")");
      }catch(Exception ex){
        
      }
    
      
    
      pl.deleteAll(dbConn, sumStrs, loginUserId);
      
     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    
      //生成org.xml文件
      YHIsPiritOrgAct.getOrgDataStream(dbConn);
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 清空密码
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String clesrUserPassword(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String remark = "";
      String seqIdStrs = request.getParameter("seqIdStrs");
      YHPersonLogic pl = new YHPersonLogic();
      pl.clearPassword(dbConn, seqIdStrs);
      String[] seqIds = seqIdStrs.split(",");
      for(int i = 0; i < seqIds.length; i++){
        String seqIdStr = seqIds[i];
        String deptId = personLogic.getDeptIdLogic(dbConn, Integer.parseInt(seqIdStr));
        String deptName = personLogic.getDeptNameLogic(dbConn, Integer.parseInt(deptId));
        String userName = personLogic.getUserNameLogic(dbConn, Integer.parseInt(seqIdStr));
        String userId = personLogic.getUserIdLogic(dbConn, Integer.parseInt(seqIdStr));
        remark += "["+YHUtility.encodeSpecial(deptName)+"]" + userName + ",USER_ID=" + userId + "<br>";
      }
      YHSysLogLogic.addSysLog(dbConn, YHLogConst.REMOVE_PASSWORD_BY_ADMIN, remark, person.getSeqId(), request.getRemoteAddr());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 清空在线时长
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String clearVisitTime(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStrs = request.getParameter("seqIdStrs");
      YHPersonLogic pl = new YHPersonLogic();
      pl.clearVisitTime(dbConn, seqIdStrs);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 判断用户是否有密码
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getPassword(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptIdStr = request.getParameter("deptId");
      int deptId = Integer.parseInt(deptIdStr);
      YHPersonLogic dl = new YHPersonLogic();
      String data = dl.getPassword(dbConn, deptId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 判断用户是否登录过
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getNotLogin(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptIdStr = request.getParameter("deptId");
      int deptId = Integer.parseInt(deptIdStr);
      YHPersonLogic dl = new YHPersonLogic();
      String data = dl.getNotLogin(dbConn, deptId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getUserIdCol(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptIdStr = request.getParameter("deptId");
      int seqId = Integer.parseInt(deptIdStr);
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"SEQ_ID="+seqId};
      List funcList = new ArrayList();
      funcList.add("person");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("PERSON"));
      if(list.size() > 1){
        for(Map ms : list){
          sb.append("{");
          sb.append("userId:\"" + ms.get("userId") + "\"");
          sb.append(",password:\"" + (ms.get("password") == null ? "" : ms.get("password")) + "\"");
          sb.append(",notLogin:\"" + (ms.get("notLogin") == null ? "" : ms.get("notLogin")) + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map ms : list){
          sb.append("{");
          sb.append("userId:\"" + ms.get("userId") + "\"");
          sb.append(",password:\"" + (ms.get("password") == null ? "" : ms.get("password")) + "\"");
          sb.append(",notLogin:\"" + (ms.get("notLogin") == null ? "" : ms.get("notLogin")) + "\"");
          sb.append("}");
        }
      }    
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取密码为空用户显示为红色，禁止登录用户显示为灰色 的判断条件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getMenuPara(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptIdStr = request.getParameter("deptId");
      int seqId = Integer.parseInt(deptIdStr);
      YHPersonLogic orgLogic = new YHPersonLogic();
      YHPerson org = null;
      String data = null;
      org = orgLogic.getMenuPara(dbConn,seqId);
      if (org == null) {
        org = new YHPerson();
      }
      data = YHFOM.toJson(org).toString();
      //System.out.println(data+"klklkl");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查询结果列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getSearchPersonList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    ArrayList<YHPerson> personList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      String loginUserPriv = person.getUserPriv();
      String postDepts = person.getPostDept();
      String loginpostPriv = person.getPostPriv();
      //int LoginUserId = Integer.parseInt(person.getUserId());
      int loginUserDept = person.getDeptId();
      int LoginUserId = person.getSeqId();
      StringBuffer sb = new StringBuffer("[");
      //String deptId = request.getParameter("deptId");
      boolean isAdminRole = person.isAdminRole();
      String userId = request.getParameter("userId");
      userId = YHDBUtility.escapeLike(userId);
      String userName = request.getParameter("userName");
      userName = YHDBUtility.escapeLike(userName);
      String byname = request.getParameter("byname");
      byname = YHDBUtility.escapeLike(byname);
      String sex = request.getParameter("sex");
      String deptId = request.getParameter("deptId");
      String userPriv = request.getParameter("userPriv");
      String postPriv = request.getParameter("postPriv");
      String notLogin = request.getParameter("notLogin");
      String notViewUser = request.getParameter("notViewUser");
      String notViewTable = request.getParameter("notViewTable");
      String dutyType = request.getParameter("dutyType");
      String lastVisitTime = request.getParameter("lastVisitTime");
      YHPersonLogic dl = new YHPersonLogic();
      //boolean a = YHPrivUtil.isDeptPriv(dbConn, deptId, postPriv, postDept, LoginUserId, loginUserDept);
      personList = dl.getSearchPersonList(dbConn,request.getParameterMap(), userId, userName, byname, sex, deptId, userPriv, postPriv, notLogin, notViewUser, notViewTable, dutyType, lastVisitTime, loginUserPriv, isAdminRole);
      int flag = 0;
      String isAdmin = "";
      for(int i = 0; i < personList.size(); i++){
        YHPerson address = personList.get(i);
        if(!YHPrivUtil.isDeptPriv(dbConn, address.getDeptId(), loginpostPriv, postDepts, LoginUserId, loginUserDept)){
          flag++;
          continue;
        }
        String passwordStr = "password";
        if(address.getPassword() == null){
          passwordStr = "";
        }else if(YHPassEncrypt.isValidPas("", address.getPassword())){
          passwordStr = "";
        }
        sb.append("{");
        sb.append("seqId:\"" + address.getSeqId() + "\"");
        sb.append(",userId:\"" + address.getUserId() + "\"");
        sb.append(",deptId:\"" + address.getDeptId() + "\"");
        sb.append(",password:\"" + passwordStr + "\"");
        if(address.isAdmin()){
          isAdmin = "isAdmin";
          sb.append(",isAdmin:\"" + isAdmin + "\"");
        }else{
          sb.append(",isAdmin:\"" + isAdmin + "\"");
        }
        //YHPassEncrypt.isValidPas(pwd, person.getPassword().trim());
        sb.append(",notLogin:\"" + address.getNotLogin() + "\"");
        sb.append(",userName:\"" + (address.getUserName() == null ? "" : address.getUserName()) + "\"");
        sb.append(",userPriv:\"" + (address.getUserPriv() == null ? "" : address.getUserPriv()) + "\"");
        sb.append(",postPriv:\"" + (address.getPostPriv() == null ? "" : address.getPostPriv()) + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      if (personList.size() == 0 || flag == personList.size()) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取管理范围
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getPostPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String loginUserId = person.getUserId();
      YHPersonLogic pl = new YHPersonLogic();
      String data = pl.getPostPrivLogic(dbConn, loginUserId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getPostPrivOther(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int loginDeptId =  person.getDeptId();
      YHPersonLogic pl = new YHPersonLogic();
      String data = pl.getPostPrivOtherLogic(dbConn, loginDeptId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 用户批量设置
   */
  public String addSet(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String type = "19";
      String roleDesc = request.getParameter("roleDesc");
      String deptDesc = request.getParameter("deptDesc");
      String userDesc = request.getParameter("userDesc");
      String role = request.getParameter("role");
      String dept = request.getParameter("dept");
      String user = request.getParameter("user");
      
      String myTableLeft = request.getParameter("myTableLeft");
      String myTableRight = request.getParameter("myTableRight");
      String shortcut = request.getParameter("shortcut");
      //String privId1 = request.getParameter("privId1");
      String privId1 = "";
      String userPriv = request.getParameter("userPriv");
      String deptId = request.getParameter("deptId");
      String theme = request.getParameter("theme");
      String bkground = request.getParameter("bkground");
      String menuType = request.getParameter("menuType");
      String smsOn = request.getParameter("smsOn");
      String callSound = request.getParameter("callSound");
      String panel = request.getParameter("panel");
      String dutyType = request.getParameter("dutyType");
      String pass1 = request.getParameter("pass1");
      String emailCapacity = request.getParameter("emailCapacity");
      String folderCapacity = request.getParameter("folderCapacity");
      String webmailNum = request.getParameter("webmailNum");
      String webmailCapacity = request.getParameter("webmailCapacity");
      String remark = "";
      String setStr = "";
      if(!"".equals(deptDesc.trim())){
        remark += "部门："+ deptDesc +"<br>";
      }
      if(!"".equals(roleDesc.trim())){
        remark += "角色："+ roleDesc +"<br>";
      }
      if(!"".equals(userDesc.trim())){
        remark += "人员："+ userDesc +"<br>";
      }
      
      if(!myTableLeft.trim().equals("")){
        setStr += "MYTABLE_LEFT='" + myTableLeft + "',";
      }
      if(!myTableRight.trim().equals("")){
        setStr += "MYTABLE_RIGHT='" + myTableRight + "',";
      }
      if(!shortcut.trim().equals("")){
        setStr += "SHORTCUT='" + shortcut + "',";
      }
      if(!userPriv.trim().equals("")){
        setStr += "USER_PRIV='" + userPriv + "',";
      }
      if(!deptId.trim().equals("")){
        setStr += "DEPT_ID='" + deptId + "',";
      }
      if(!theme.trim().equals("")){
        setStr += "THEME='" + theme + "',";
      }
      if(!bkground.trim().equals("")){
        if(bkground.trim().equals("0")){
          bkground = "";
        }
        setStr += "BKGROUND='" + bkground + "',";
      }
      if(!menuType.trim().equals("")){
        setStr += "MENU_TYPE='" + menuType + "',";
      }
      if(!smsOn.trim().equals("")){
        setStr += "SMS_ON='" + smsOn + "',";
      }
      if(!callSound.trim().equals("")){
        setStr += "CALL_SOUND='" + callSound + "',";
      }
      if(!panel.trim().equals("")){
        setStr += "PANEL='" + panel + "',";
      }
      if(!dutyType.trim().equals("")){
        setStr += "DUTY_TYPE='" + dutyType + "',";
      }
      if(!emailCapacity.trim().equals("")){
        setStr += "EMAIL_CAPACITY='" + emailCapacity + "',";
      }
      if(!folderCapacity.trim().equals("")){
        setStr += "FOLDER_CAPACITY='" + folderCapacity + "',";
      }
      if(!webmailNum.trim().equals("")){
        setStr += "WEBMAIL_NUM='" + webmailNum + "',";
      }
      if(!webmailCapacity.trim().equals("")){
        setStr += "WEBMAIL_CAPACITY='" + webmailCapacity + "',";
      }
      if(!pass1.trim().equals("")){
        
        setStr += "PASSWORD='" + YHPassEncrypt.encryptPass(pass1) + "',";
      }
      YHSysLogLogic.addSysLog(dbConn, type, remark, person.getSeqId(), request.getRemoteAddr());
      YHPersonLogic pl = new YHPersonLogic();
      pl.getSearchSet(dbConn, dept, role, user, setStr, privId1);
      syncDept(dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      //request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    
    //生成org.xml文件
      YHIsPiritOrgAct.getOrgDataStream(dbConn);
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 批量个性设置日志列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getSetLogList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int loginUserId = person.getSeqId();
      YHPersonLogic dl = new YHPersonLogic();
      String data = dl.getSetLogList(dbConn,request.getParameterMap(), loginUserId);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  /**
   * 获得密码长度范围
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getPasswordLength(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"PARA_NAME='SEC_PASS_MIN' or PARA_NAME='SEC_PASS_MAX' or PARA_NAME='SEC_PASS_SAFE'"};
      List funcList = new ArrayList();
      funcList.add("sysPara");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("SYS_PARA"));
      if(list.size() > 1){
        for(Map ms : list){
          sb.append("{");
          sb.append("paraName:\"" + ms.get("paraName") + "\"");
          sb.append(",paraValue:\"" + ms.get("paraValue") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map ms : list){
          sb.append("{");
          sb.append("paraName:\"" + ms.get("paraName") + "\"");
          sb.append(",paraValue:\"" + ms.get("paraValue") + "\"");
          sb.append("}");
        }
      }    
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出密码长度范围");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取空密码用户
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getNoPassUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPersonLogic pl = new YHPersonLogic();
      String userIdStr = pl.getNoPassUserId(dbConn);
      String userNameStr = pl.getNoPassUserName(dbConn);
      StringBuffer sb = new StringBuffer("[");
      sb.append("{");
      sb.append("userId:\"" + userIdStr.substring(0, userIdStr.length() - 1) + "\"");
      sb.append(",userName:\"" + userNameStr.substring(0, userNameStr.length() - 1) + "\"");
      sb.append("}");
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功空密码用户");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 是否启用UserKey
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getUserKey(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("seqId");
      int seqId = Integer.parseInt(seqIdStr);
      YHPersonLogic pl = new YHPersonLogic();
      String data = pl.getUserKey(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   *  update PERSON表中的keySn
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String updateUserKey(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String keySn = request.getParameter("keySn");
      Map m =new HashMap();
      m.put("seqId", seqId);
      m.put("keySn", keySn);
      YHORM orm = new YHORM();
      orm.updateSingle(dbConn, "person", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"keySn修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getUserInformation(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("seqId");
      int seqId = Integer.parseInt(seqIdStr);
      YHPersonLogic pl = new YHPersonLogic();
      String data = pl.getUserInformation(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public void addUsbKeyLog(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
    .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     try {
      dbConn = requestDbConn.getSysDbConn();
      String remark = "";
      YHSysLogLogic.addSysLog(dbConn, YHLogConst.USER_KEY_AUTHFAILURES, remark, person.getSeqId(), request.getRemoteAddr());
    } catch (YHInvalidParamException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  
  public String getMaxPrivNo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPersonLogic pl = new YHPersonLogic();
      String maxPrivNo = pl.getMaxPrivNoLogic(dbConn);
      String data = pl.getMaxUserPrivLogic(dbConn, maxPrivNo);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 选择角色
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getRoles(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String privNoFlagStr = request.getParameter("privNoFlag");
    if (!YHUtility.isNullorEmpty(privNoFlagStr)) {
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPersonLogic logic = new YHPersonLogic();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String loginUserPriv = loginUser.getUserPriv();
      int privNo = Integer.parseInt(YHPersonLogic.getPrivNoStr(dbConn, loginUserPriv));
      List<YHUserPriv> list = null;
      list = logic.getRoleList(dbConn, privNo, loginUser);
      
      StringBuffer sb = new StringBuffer();
      for (YHUserPriv up : list) {
        sb.append(up.getJsonSimple());
      }
      if (list.size() > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "get Success");
      request.setAttribute(YHActionKeys.RET_DATA, "[" + sb.toString() + "]");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 导出到EXCEL表格中

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportToExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
    response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      String loginUserPriv = person.getUserPriv();
      //String deptId = request.getParameter("deptId");
      boolean isAdminRole = person.isAdminRole();
      String userId = request.getParameter("userId");
      userId = YHDBUtility.escapeLike(userId);
      String userName = request.getParameter("userName");
      userName = YHDBUtility.escapeLike(userName);
      String byname = request.getParameter("byname");
      byname = YHDBUtility.escapeLike(byname);
      String sex = request.getParameter("sex");
      String deptId = request.getParameter("deptId");
      String userPriv = request.getParameter("userPriv");
      String postPriv = request.getParameter("postPriv");
      String notLogin = request.getParameter("notLogin");
      String notViewUser = request.getParameter("notViewUser");
      String notViewTable = request.getParameter("notViewTable");
      String dutyType = request.getParameter("dutyType");
      String lastVisitTime = request.getParameter("lastVisitTime");
      
      String fileName = URLEncoder.encode("OA用户.csv","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      YHPersonLogic ieml = new YHPersonLogic();
      ArrayList<YHDbRecord > dbL = ieml.toExportPersonData(conn,request.getParameterMap(), userId, userName, byname, sex, deptId, userPriv, postPriv, notLogin, notViewUser, notViewTable, dutyType, lastVisitTime, loginUserPriv, isAdminRole);;
      YHCSVUtil.CVSWrite(response.getWriter(), dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return null;
  }
  
  public String importPerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    InputStream is = null;
    Connection dbConn = null;
    String data = null;
    int isCount = 0;
    int updateCount = 0;
    String message = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      is = fileForm.getInputStream();
      ArrayList<YHDbRecord> drl = YHCSVUtil.CVSReader(is, YHConst.CSV_FILE_CODE);

      StringBuffer sb = new StringBuffer("[");
      YHPersonLogic dl = new YHPersonLogic();
      int count = dl.getUserConut(dbConn);
      int availableCnt = YHRegistUtility.getUserCnt() - count;
      if (count < 0) {
        data = "获取用户数失败!";
      } else if (drl.size() > availableCnt) {
        data = "导入失败: 剩余可用用户数" + availableCnt + ", 导入用户超出用户数限制!";
      } else {
        String deptName = "";
        String userId = "";
        String userName = "";
        String role = "";
        String userNo = "";
        String password = "";
        String postPriv = "";
        String postPrivStr = "";
        String infoStr = "";
        String sex = "";
        String brithday = "";
        String byName = "";
        String mobilNo = "";
        String bindIp = "";
        String telNoDept = "";
        String faxNoDept = "";
        String addHome = "";
        String postNoHome = "";
        String telNoHome = "";
        String email = "";
        String oicq = "";
        String msn = "";
        String seqIdStr = "";
        String color = "red";
        int seqId = 0;
        String remark = "成功导入人员：";
        boolean hasSucess = false;
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        Map map = new HashMap();
        for (int i = 0; i < drl.size(); i++) {
          deptName = (String) drl.get(i).getValueByName("部门");
          userId = (String) drl.get(i).getValueByName("用户名");
          if (YHUtility.isNullorEmpty(userId)) {
            continue;
          }
          userName = (String) drl.get(i).getValueByName("姓名");
          if (YHUtility.isNullorEmpty(userName)) {
            continue;
          }
          role = (String) drl.get(i).getValueByName("角色");
          role="";
          userNo = (String) drl.get(i).getValueByName("用户排序号");
          postPriv = (String) drl.get(i).getValueByName("管理范围");
          postPriv="";
          sex = (String) drl.get(i).getValueByName("性别");
          brithday = (String) drl.get(i).getValueByName("生日");
          byName = (String) drl.get(i).getValueByName("别名");
          mobilNo = (String) drl.get(i).getValueByName("手机");
          bindIp = (String) drl.get(i).getValueByName("IP");
          telNoDept = (String) drl.get(i).getValueByName("工作电话");
          faxNoDept = (String) drl.get(i).getValueByName("工作传真");
          addHome = (String) drl.get(i).getValueByName("家庭地址");
          postNoHome = (String) drl.get(i).getValueByName("邮编");
          telNoHome = (String) drl.get(i).getValueByName("家庭电话");
          email = (String) drl.get(i).getValueByName("E-mail");
          oicq = (String) drl.get(i).getValueByName("QQ");
          msn = (String) drl.get(i).getValueByName("MSN");
          password = (String) drl.get(i).getValueByName("PASSWORD");
          
          seqIdStr = (String) drl.get(i).getValueByName("ID");
          if(!YHUtility.isNullorEmpty(seqIdStr)){
            seqId = Integer.parseInt(seqIdStr);
          }
          if (YHUtility.isNullorEmpty(userId)) {
            color = "red";
            infoStr = "导入失败,用户名为空";
            sbStrJson(sb, deptName, userId, userName, role, userNo, postPriv,
                infoStr, color);
            continue;
          }
          if (dl.existsDeptNameIsMultiple(dbConn, deptName)) {
            color = "red";
            infoStr = "导入失败,部门名称 " + deptName + " 在系统中存在多个";
            sbStrJson(sb, deptName, userId, userName, role, userNo, postPriv,
                infoStr, color);
            continue;
          } else if (dl.existsDepartment(dbConn, deptName)) {
            color = "red";
            if(YHUtility.isNullorEmpty(deptName)){
              deptName = "";
            }
            infoStr = "导入失败,部门名称 " + deptName + " 在系统中不存在";
            sbStrJson(sb, deptName, userId, userName, role, userNo, postPriv,
                infoStr, color);
            continue;
          }
          if (dl.existsUserId(dbConn, userId, seqId)) {
            color = "red";
            infoStr = "导入失败,用户ID重复";
            sbStrJson(sb, deptName, userId, userName, role, userNo, postPriv,
                infoStr, color);
            continue;
          }
          if (dl.existsRole(dbConn, role)) {
            color = "red";
            if(YHUtility.isNullorEmpty(role)){
              role = "";
            }
            infoStr = "导入失败，角色 " + role + " 不存在";
            sbStrJson(sb, deptName, userId, userName, role, userNo, postPriv,
                infoStr, color);
            continue;
          }
          if ("女".equals(sex)) {
            sex = "1";
          } else {
            sex = "0";
          }
          if ("全体".equals(postPriv)) {
            postPrivStr = "1";
          } else if ("指定部门".equals(postPriv)) {
            postPrivStr = "2";
          } else {
            postPrivStr = "0";
          }
          int deptId = dl.getDeptIdLogic(dbConn, deptName);
          role =  String.valueOf(dl.getUserPrivIdLogic(dbConn, role));
          String userPriv=personLogic.getNoPriv(dbConn);
          map.put("deptId", deptId);
          map.put("userId", userId);
          map.put("userName", userName);
          map.put("userPriv", userPriv);
          map.put("userNo", userNo);
          map.put("sex", sex);
          map.put("postPriv", "");
          map.put("birthday", brithday);
          map.put("byname", byName);
          map.put("mobilNo", mobilNo);
          map.put("bindIp", bindIp);
          map.put("telNoDept", telNoDept);
          map.put("faxNoDept", faxNoDept);
          map.put("addHome", addHome);
          map.put("postNoHome", postNoHome);
          map.put("telNoHome", telNoHome);
          map.put("email", email);
          map.put("oicq", oicq);
          map.put("msn", msn);

          // map.put("postDept" , postDept);
          // map.put("userPrivOther" , userPrivOther);
          // map.put("deptIdOther" , deptIdOther);

          // map.put("notLogin", notLogin);
          // map.put("notViewUser", notViewUser);
          // map.put("notViewTable", notViewTable);
          // map.put("useingKey", useingKey);

          // map.put("webmailNum" , webmailNum);
          // map.put("webmailCapacity", webmailCapacity);
          // map.put("remark" , remark);
          // map.put("mobilNoHidden", mobilNoHidden);

          String auatar = "1";
          map.put("auatar", auatar);
          String callSound = "1";
          map.put("callSound", callSound);
          int dutyType = 1;
          map.put("dutyType", dutyType);
          String smsOn = "1";
          map.put("smsOn", smsOn);
          String theme = "2";
          map.put("theme", theme);
          if (YHUtility.isNullorEmpty(password)) {
            password = "";
            map.put("password", YHPassEncrypt.encryptPass(password));
          } else {
            map.put("password", YHPassEncrypt.encryptPass(password));
          }
          int emailCapacity = 100;
          int folderCapacity = 100;
          map.put("emailCapacity", emailCapacity);
          map.put("folderCapacity", folderCapacity);

          YHORM orm = new YHORM();
          if (dl.existsUserId2(dbConn, userId)) {
            updateCount++;
            infoStr = "用户名" + userName + " 已存在，其信息得到更新";
            color = "red";
            sbStrJson(sb, deptName, userId, userName, role, userNo, postPriv,
                infoStr, color);
            YHPerson persons = dl.getPersonById(userId, dbConn);
            map.put("seqId", persons.getSeqId());
            orm.updateSingle(dbConn, "person", map);
          } else {
            isCount++;
            infoStr = "成功";
            color = "black";
            sbStrJson(sb, deptName, userId, userName, role, userNo, postPriv,
                infoStr, color);
            orm.saveSingle(dbConn, "person", map);
            remark += userName + ",";
            hasSucess = true;
          }
        }
        if (sb.charAt(sb.length() - 1) == ',') {
          sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        data = sb.toString();
        request.setAttribute("contentList", data);
        if (hasSucess) {
          YHSysLogLogic.addSysLog(dbConn, "6", remark, person.getSeqId(), request.getRemoteAddr());
        }
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
        request.setAttribute(YHActionKeys.RET_DATA, data);
        
      //生成org.xml文件
        YHIsPiritOrgAct.getOrgDataStream(dbConn);
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      message =ex.getMessage();
      // ex.printStackTrace();
      throw ex;
    }
    return "/core/funcs/person/importPerson.jsp?data=" + message + "&isCount="
        + isCount + "&updateCount=" + updateCount;
  }
  public String add(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
        Statement stm2 = null;
        ResultSet rs2 = null;
        String updatePwd = "SELECT USER_ID from PERSON WHERE USER_ID <> 'admin'";
        try {
          stm2 = dbConn.createStatement();
          rs2 =  stm2.executeQuery(updatePwd);
          while(rs2.next()) {
            String userId = rs2.getString("USER_ID");
            update(dbConn ,  userId);
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm2, rs2, null); 
        }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public void update(Connection conn , String userId) throws Exception {
    Statement stm2 = null;
    ResultSet rs2 = null;
    String userId1 = userId;
    if (userId.length() == 1) {
      userId1 = "000" + userId;
    }else if (userId.length() == 2) {
      userId1 = "00" + userId;
    }else if (userId.length() == 3) {
      userId1 = "0" + userId;
    }else if (userId.length() == 4) {
      return;
    }
    String updatePwd = "update  PERSON set USER_ID = '"+userId1+"' WHERE USER_ID = '"+userId+"'";
    try {
      stm2 = conn.createStatement();
       stm2.executeUpdate(updatePwd);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
  }
  public String importPersonPwd(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    InputStream is = null;
    Connection dbConn = null;
    String data = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      is = fileForm.getInputStream();
      ArrayList<YHDbRecord> drl = YHCSVUtil.CVSReader(is, YHConst.CSV_FILE_CODE);

      StringBuffer sb = new StringBuffer("[");
      String remark = "成功导入人员pwd：";
      for (int i = 0; i < drl.size(); i++) {
        String pwd =  YHUtility.null2Empty((String) drl.get(i).getValueByName("登录密码")).trim();
        String userId  = YHUtility.null2Empty((String) drl.get(i).getValueByName("行员号(登录用户名)")).trim();
        Statement stm2 = null;
        ResultSet rs2 = null;
        String updatePwd = "update PERSON SET PASSWORD = '" + pwd + "' where USER_ID='" + userId + "'";
        try {
          stm2 = dbConn.createStatement();
          int ii =  stm2.executeUpdate(updatePwd);
          if (ii == 1) {
            data += userId + "<br/>";
          } else {
            data += "<span style='color:red'>" + userId + "</span><br/>";
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm2, rs2, null); 
        }
      }
      request.setAttribute("data", data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      // ex.printStackTrace();
      throw ex;
    }
    return "/core/funcs/person/importPersonPwd.jsp";
  }
  
  public String sbStrJson(StringBuffer sb, String deptName, String userId, String userName, String role, String userNo, String postPriv, String infoStr, String color){
    sb.append("{");
    sb.append("deptName:\"" + (deptName == null ? "" : deptName) + "\"");
    sb.append(",userId:\"" + (userId == null ? "" : userId) + "\"");
    sb.append(",userName:\"" + (userName == null ? "" : userName) + "\"");
    sb.append(",role:\"" + (role == null ? "" : role) + "\"");
    sb.append(",userNo:\"" + (userNo == null ? "" : userNo) + "\"");
    sb.append(",postPriv:\"" + (postPriv == null ? "" : postPriv) + "\"");
    sb.append(",info:\"" + (infoStr == null ? "" : infoStr) + "\"");
    sb.append(",color:\"" + (color == null ? "" : color) + "\"");
    sb.append("},");
    return sb.toString();
  }
  
  /**
   * 获取当前用户，是否是安全员
   * */
  public String isSecPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    InputStream is = null;
    Connection dbConn = null;
    String data = "0";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String privId=person.getUserPriv();
      YHORM orm =new YHORM();
      YHUserPriv up=(YHUserPriv)orm.loadObjSingle(dbConn, YHUserPriv.class, Integer.parseInt(privId));
      String privName=up.getPrivName();
      if("OA 安全员".equals(privName)){
        data="1";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      ex.printStackTrace();
    }
    return "/core/inc/rtjson.jsp";
 }
  
  
  /**
   * 获取当前用户，是否是审计员
   * */
  public String isAudPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    InputStream is = null;
    Connection dbConn = null;
    String data = "0";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String privId=person.getUserPriv();
      YHORM orm =new YHORM();
      YHUserPriv up=(YHUserPriv)orm.loadObjSingle(dbConn, YHUserPriv.class, Integer.parseInt(privId));
      String privName=up.getPrivName();
      if("OA 安全员".equals(privName)){
        data="1";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      ex.printStackTrace();
    }
    return "/core/inc/rtjson.jsp";
 }
}
