package yh.core.funcs.modulepriv.act;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.modulepriv.data.YHModulePriv;
import yh.core.funcs.modulepriv.logic.YHModuleprivLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.seclog.logic.YHSecLogUtil;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;

public class YHModuleprivAct {
  private static Logger log = Logger.getLogger(YHModuleprivAct.class);
  
  YHModuleprivLogic moduleprivLogic = new YHModuleprivLogic();
  
  public String beforepriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    Connection dbConn = null;
    String id = null;
    String uid = null;
    String userName= null;
    try {
       id = request.getParameter("id");
       uid = request.getParameter("uid");
       userName = request.getParameter("userName");
      //int userId = Integer.parseInt(request.getParameter("userId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
//      request.setAttribute("modulePriv", modulePriv);
//      data = YHFOM.toJson(obj).toString();
//      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
//      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
//      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/modulepriv/priv.jsp?id="+id+"&uid="+uid+"&userName="+userName;
  }
  
  public String updateModulepriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
     String apply_to_module = request.getParameter("apply_to_module");//应用到其他模块
     String apply_to_dept = request.getParameter("apply_to_dept");//应用到其他用户所在部门的限制
     String apply_to_priv = request.getParameter("apply_to_priv");//应用到其他用户所属角色的限制
     Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map map = request.getParameterMap();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHModulePriv modulepriv = (YHModulePriv)YHFOM.build(request.getParameterMap());  
      int moduleId = modulepriv.getModuleId();
//      String deptPriv = request.getParameter("deptPriv");
//      String rolePriv = request.getParameter("rolePriv");
//      String deptId = request.getParameter("deptId");
//      String privId = request.getParameter("privId");
//      String userId = request.getParameter("userId");
//      String userSeqId = request.getParameter("userSeqId");
//      String moduleId = request.getParameter("moduleId");
//     
//      YHModulePriv modulepriv = new YHModulePriv();
//      modulepriv.setDeptPriv(deptPriv);
//      modulepriv.setRolePriv(rolePriv);
//      modulepriv.setDeptId(deptId);
//      modulepriv.setPrivId(privId);
//      modulepriv.setUserId(userId);
//      modulepriv.setUserSeqId(Integer.parseInt(userSeqId));
//      modulepriv.setModuleId(moduleId);
      if(apply_to_module==null) {
        apply_to_module = "";
      }
      apply_to_module = apply_to_module + moduleId;
      moduleprivLogic.queryNeedSetMoudle(dbConn, modulepriv, apply_to_priv, apply_to_dept, apply_to_module);
      try{
        YHSecLogUtil.log(dbConn, person, request.getRemoteAddr(), "215","管理范围变动："+apply_to_priv+apply_to_dept+apply_to_module,"1", "管理范围变动");
        }catch(Exception e){}
        
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"按模块设置管理范围信息已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  public String getJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    YHModulePriv modulePriv = null;
    StringBuffer sb = new StringBuffer();
    String data = "";
    String id = request.getParameter("id");
    String uid = request.getParameter("uid");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Statement st = null;
      ResultSet rs = null;
//      YHORM orm = new YHORM();
//      Map map = new HashMap();
//      map.put("USER_SEQ_ID", uid);
//      map.put("MODULE_ID", id);
//      modulePriv = (YHModulePriv)orm.loadObjSingle(dbConn, YHModulePriv.class, map);
//      
//      if (modulePriv == null) {
//        modulePriv = new YHModulePriv();
//      }
//      
//      data = modulePriv.toJSON();
      String sql = "select * from oa_function_priv where MODULE_ID='"+ id + "' and USER_SEQ_ID='"+ uid + "'";
      st = dbConn.createStatement();
      rs = st.executeQuery(sql);
      if(rs.next()) {
       int seqId =  rs.getInt("SEQ_ID");
       String deptPriiv = rs.getString("DEPT_PRIV");
       String rolePriv = rs.getString("ROLE_PRIV");
       
       String deptId = rs.getString("DEPT_ID");
       if("null".equals(deptId)||deptId==null){
         deptId = "";
       }
       String privId = rs.getString("PRIV_ID");
       if("null".equals(privId)||privId==null){
         privId = "";
       }
       String userId = rs.getString("USER_ID");
       if("null".equals(userId)||userId==null){
         userId = "";
       }
       sb.append("{");
       sb.append("seqId:\"" + seqId  + "\"");
       sb.append(",deptPriv:\"" + deptPriiv  + "\"");
       sb.append(",rolePriv:\"" + rolePriv  + "\"");
       sb.append(",moduleId:\"" + id  + "\"");
       sb.append(",userSeqId:\"" + uid  + "\"");
       sb.append(",deptId:\"" + deptId  + "\"");
       sb.append(",privId:\"" + privId  + "\"");
       sb.append(",userId:\"" + userId  + "\"");
       sb.append("}");
      }else{
         modulePriv = new YHModulePriv();
        sb =  new StringBuffer(modulePriv.toJSON());
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
}
