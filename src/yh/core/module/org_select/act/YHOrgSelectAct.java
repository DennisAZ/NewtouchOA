package yh.core.module.org_select.act;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.tree.DefaultMutableTreeNode;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.modulepriv.data.YHModulePriv;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserOnline;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.security.data.YHSecurity;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.module.org_select.data.YHDeptCoparator;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHOrgSelectAct {

  

  public String getTree(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Connection dbConn = null;
    String idStr = request.getParameter("id");
    YHOrgSelectLogic logic = new YHOrgSelectLogic();
    int id = 0;
    if (idStr != null && !"".equals(idStr)) {
      id = Integer.parseInt(idStr);
    }
    String MODULE_ID = request.getParameter("MODULE_ID");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String USER_DEPT = String.valueOf(person.getDeptId());
      String USER_SEQ_ID = String.valueOf(person.getSeqId());
      String loginUserPriv = person.getUserPriv();

      YHORM orm = new YHORM();
      String[] query = { "MODULE_ID=" + MODULE_ID, "USER_SEQ_ID=" + USER_SEQ_ID };
      ArrayList<YHModulePriv> modulePriv = (ArrayList<YHModulePriv>) orm
          .loadListSingle(dbConn, YHModulePriv.class, query);
      StringBuffer buf = null;
      String deptName = "";
      String roleName = "";
      if (modulePriv.size() != 0) { // 按模块进行权限分配        YHModulePriv tmp = (YHModulePriv) modulePriv.get(0);
        String deptPriv = tmp.getDeptPriv();
        query[0] = "DEPT_PARENT=" + id;
        if (deptPriv.equals("0")) {
          query[1] = "SEQ_ID=" + USER_DEPT;
        } else if (deptPriv.equals("1")) {
          query[1] = "1=1";
        } else if (deptPriv.equals("2")) {
          query[1] = "SEQ_ID IN (" + tmp.getDeptId() + ")";
        } else {
          query[1] = "1=1";
        }
        ArrayList<YHDepartment> deptList = (ArrayList<YHDepartment>) orm
            .loadListSingle(dbConn, YHDepartment.class, query);
        buf = new StringBuffer("[");
        query[0] = "(DEPT_ID=" + id + " or "
            + YHDBUtility.findInSet(String.valueOf(id), "DEPT_ID_OTHER") + ")";

        if (deptPriv.equals("0")) {
          if (USER_DEPT.equals(id)) {
            query[1] = "1=1";
          } else {
            query[1] = "1=0";
          }
        } else if (deptPriv.equals("1")) {
          query[1] = "1=1";
        } else if (deptPriv.equals("2")) {
          query[1] = "DEPT_ID IN (" + tmp.getDeptId() + ")";
        } else if (deptPriv.equals("3")) {
          query[1] = "USER_ID IN ('" + tmp.getUserId() + "')";
        } else {
          query[1] = "1=1";
        }
        //System.out.println(query[0] + "," + query[1] + "MNNMMN");
        ArrayList<YHPerson> personList = (ArrayList<YHPerson>) orm
            .loadListSingle(dbConn, YHPerson.class, query);
        for (YHPerson m : personList) {
          int seqId = m.getSeqId();
          int deptId = m.getDeptId();
          int roleId = Integer.parseInt(m.getUserPriv());
          deptName = getDeptName(dbConn, deptId);
          roleName = getRoleName(dbConn, roleId);
          String telNoDept = m.getTelNoDept();
          String email = m.getEmail();
          if (YHUtility.isNullorEmpty(email)) {
            email = "";
          }
          String oicq = m.getOicq();
          if (YHUtility.isNullorEmpty(oicq)) {
            oicq = "";
          }
          buf.append("{");
          buf.append("nodeId:\"r" + m.getSeqId() + "\"");
          buf.append(",name:\"" + m.getUserName() + "\"");
          buf.append(",isHaveChild:" + 0);
          buf.append(",imgAddress:\"" + request.getContextPath()
              + "/core/styles/style1/img/dtree/0-1.gif\"");
          buf.append(",title:\"部门:" + deptName + "\\n角色:" + roleName
              + "\\n工作电话:" + telNoDept + "\\nemail:" + email + "\\nQQ:" + oicq
              + "\"");
          buf.append("},");
        }
        for (int i = 0; i < deptList.size(); i++) {
          YHDepartment dept = deptList.get(i);
          buf.append("{");
          buf.append("nodeId:\"" + dept.getSeqId() + "\"");
          buf.append(",name:\"" + dept.getDeptName() + "\"");
          buf
              .append(",isHaveChild:"
                  + IsHaveChild(request, response, String.valueOf(dept
                      .getSeqId())));
          buf.append(",imgAddress:\"" + request.getContextPath()
              + "/core/styles/style1/img/dtree/node_dept.gif\"");
          buf.append(",title:\"" + dept.getDeptName() + "\"");
          buf.append("},");
        }
        if (buf.length() > 1) {
          buf.deleteCharAt(buf.length() - 1);
        }
      } else { // 没按模块进行权限分配
        int privNo = Integer.parseInt(YHPersonLogic.getPrivNoStr(dbConn, loginUserPriv));
        String[] querys = { "SEQ_ID=" + USER_SEQ_ID };
        ArrayList<YHPerson> personPriv = (ArrayList<YHPerson>) orm
            .loadListSingle(dbConn, YHPerson.class, querys);
        YHPerson tmps = (YHPerson) personPriv.get(0);
        String postPriv = tmps.getPostPriv();
        String postDept = tmps.getPostDept();
        ArrayList<YHDepartment> deptList = new ArrayList();
        
        if (postPriv.equals("0")) {    
          if (id == 0 && !person.isAdminRole() ) {
            YHDepartment dept = logic.getDeptParentId(dbConn, person.getDeptId()); 
            if (dept != null) {
              deptList.add(dept);
            }
          } else {
            querys[0] ="DEPT_PARENT=" + id;
            deptList = (ArrayList<YHDepartment>) orm
            .loadListSingle(dbConn, YHDepartment.class, querys);
          }
        }else {
          YHDepartment dept = logic.getDeptParentId(dbConn, person.getDeptId());
          if(id == 0){
            querys[0] = " DEPT_PARENT = " + dept.getDeptParent();
          }else{
            querys[0] ="DEPT_PARENT=" + id;
          }
          deptList = (ArrayList<YHDepartment>) orm
          .loadListSingle(dbConn, YHDepartment.class, querys);
        }
        String userPrivStr = "";
        String whereStr = "";
        buf = new StringBuffer("[");
         whereStr = "(DEPT_ID=" + id + " or "
           + YHDBUtility.findInSet(String.valueOf(id), "DEPT_ID_OTHER") + ")";
        if (postPriv.equals("0")) {
          if (USER_DEPT.equals(id)) {
            whereStr += " and 1=1";
          }
        }
        if (id == 0) {
          whereStr += " and not DEPT_ID = 0";
        }else if(postPriv.equals("0")){
          whereStr += " and PERSON.USER_PRIV = USER_PRIV.SEQ_ID and USER_PRIV.PRIV_NO >" + privNo + " and not USER_PRIV.SEQ_ID = 1 and DEPT_ID =" + person.getDeptId();
          userPrivStr = ", USER_PRIV ";
        } else if (postPriv.equals("1")) {
          if(person.isAdminRole()){
            whereStr += " and 1=1";
          }else{
            whereStr += " and PERSON.USER_PRIV = USER_PRIV.SEQ_ID and USER_PRIV.PRIV_NO >"+ privNo +" and not USER_PRIV.SEQ_ID = 1";
            userPrivStr = ", USER_PRIV ";
          }
        } else if (postPriv.equals("2")) {
          String[] postDeptStr = postDept.split(",");
          if(!strValue(postDeptStr, USER_DEPT)){    //用于判断登陆者的DEPT_ID是否在POST_DEPT中            postDept += "," + USER_DEPT + ""; 
          }else{
            postDept = postDept;
          }
          whereStr += " and DEPT_ID IN (" + postDept + ") and PERSON.USER_PRIV = USER_PRIV.SEQ_ID and USER_PRIV.PRIV_NO >"+ privNo +" and not USER_PRIV.SEQ_ID = 1"; 
          userPrivStr = ", USER_PRIV ";
        } else if (postPriv.equals("3")) {
          querys[0] += " and USER_ID IN ('" + tmps.getUserId() + "')";
        } else {
          whereStr += " and 1=1";
        }
        YHOrgSelectLogic osl = new YHOrgSelectLogic();
        ArrayList<YHPerson> personList = osl.getPersonPrivList(dbConn, whereStr, userPrivStr, loginUserPriv);
        for (YHPerson m : personList) {
          int seqId = m.getSeqId();
          int deptId = m.getDeptId();
          int roleId = Integer.parseInt(m.getUserPriv());
          String deptOther = m.getDeptIdOther();
          String sex = m.getSex();
          deptName = getDeptName(dbConn, deptId);
          roleName = getRoleName(dbConn, roleId);
          String telNoDept = m.getTelNoDept();
          if(YHUtility.isNullorEmpty(telNoDept)){
            telNoDept = "";
          }
          String email = m.getEmail();
          if (YHUtility.isNullorEmpty(email)) {
            email = "";
          }
          String oicq = m.getOicq();
          if (YHUtility.isNullorEmpty(oicq)) {
            oicq = "";
          }
          buf.append("{");
          buf.append("nodeId:\"r" + m.getSeqId() + "\"");
          buf.append(",name:\"" + m.getUserName() + "\"");
          buf.append(",isHaveChild:" + 0);
          buf.append(",imgAddress:\"" + request.getContextPath()
              + userStateImg(request, response, m.getSeqId(), dbConn, sex) );
          buf.append(",title:\"部门:" + deptName + "\\n角色:" + roleName
              + "\\n工作电话:" + telNoDept + "\\nemail:" + email + "\\nQQ:" + oicq
              + "\"");
          buf.append("},");
        }
        for (int i = 0; i < deptList.size(); i++) {
          YHDepartment dept = deptList.get(i);
          buf.append("{");
          buf.append("nodeId:\"" + dept.getSeqId() + "\"");
          buf.append(",name:\"" + dept.getDeptName() + "\"");
          buf
              .append(",isHaveChild:"
                  + IsHaveChild(request, response, String.valueOf(dept
                      .getSeqId())));
          buf.append(",imgAddress:\"" + request.getContextPath()
              + "/core/styles/style1/img/dtree/node_dept.gif\"");
          buf.append(",title:\"" + dept.getDeptName() + "\"");
          buf.append("},");
        }
        if (buf.length() > 1) {
          buf.deleteCharAt(buf.length() - 1);
        }
      }
      buf.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, buf.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
}

  /**
   * 获取是否在线人员图片
   * @param request
   * @param response
   * @param userIdStr
   * @param dbConn
   * @return
   * @throws Exception
   */
  
  public String userStateImg(HttpServletRequest request,
      HttpServletResponse response, int userIdStr, Connection dbConn, String sex) throws Exception {
    String url = "\"";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHOrgSelectLogic osl = new YHOrgSelectLogic();
      String userState = osl.getUserStateImg(dbConn, userIdStr);
      
      if("0".equals(sex)){
        if("1".equals(userState)){
          url = "/core/styles/style1/img/dtree/0-1.gif\"";
        }else if("2".equals(userState)){
          url = "/core/styles/style1/img/dtree/U02.gif\"";
        }else if("3".equals(userState)){
          url = "/core/styles/style1/img/dtree/U03.gif\"";
        }else{
          url = "/core/styles/style1/img/dtree/0-2.gif\"";
        }
      }else {
        if("1".equals(userState)){
          url = "/core/styles/style1/img/dtree/1-1.gif\"";
        }else if("2".equals(userState)){
          url = "/core/styles/style1/img/dtree/U12.gif\"";
        }else if("3".equals(userState)){
          url = "/core/styles/style1/img/dtree/U13.gif\"";
        }else{
          url = "/core/styles/style1/img/dtree/1-2.gif\"";
        }
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return url;
  }

  // 判断是否有子部门和本部门是否有人:1为有子部门或者本部门有人0为都不是
  public int IsHaveChild(HttpServletRequest request,
      HttpServletResponse response, String id) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("DEPT_PARENT", id);
      // 判断是否有子部门
      YHOrgSelectLogic osl = new YHOrgSelectLogic();
      ArrayList<YHDepartment> list = osl.getDepartmentList(dbConn, Integer.parseInt(id));
      
      //List<YHDepartment> list = orm.loadListSingle(dbConn, YHDepartment.class, map);
      // 判断本部门是否有人
      //System.out.println(list.size()+"=FGHJT");
      String[] str = { "DEPT_ID =" + id };
      String whereStr = "DEPT_ID =" + Integer.parseInt(id) + " or " + YHDBUtility.findInSet(id, "DEPT_ID_OTHER");
      //List<YHPerson> personList = orm.loadListSingle(dbConn, YHPerson.class,str);
      List<YHPerson> personList = osl.getPersonList(dbConn, whereStr);
      if (list.size() > 0 || personList.size() > 0) {
        return 1;
      } else {
        return 0;
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
  }
  
  /**
   * 用于判断是否显示人员
   * @param request
   * @param response
   * @param id
   * @return
   * @throws Exception
   */
  
  public int IsHaveChildPriv(HttpServletRequest request,
      HttpServletResponse response, String id, String postDept) throws Exception {
    Connection dbConn = null;
    YHPerson person = (YHPerson) request.getSession().getAttribute(
        YHConst.LOGIN_USER);
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptIdLogin = String.valueOf(person.getDeptId());
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("DEPT_PARENT", id);
      // 判断是否有子部门
      YHOrgSelectLogic osl = new YHOrgSelectLogic();
      ArrayList<YHDepartment> list = osl.getDepartmentList(dbConn, Integer.parseInt(id));
      //List<YHDepartment> list = orm.loadListSingle(dbConn, YHDepartment.class, map);
      // 判断本部门是否有人
      String[] str = { "DEPT_ID =" + id };
      String whereStr = "DEPT_ID =" + Integer.parseInt(id);
      //List<YHPerson> personList = orm.loadListSingle(dbConn, YHPerson.class,str);
      List<YHPerson> personList = osl.getPersonList(dbConn, whereStr);
      if (list.size() > 0 || personList.size() > 0) {
        for(int i = 0 ; i < personList.size(); i++){
          String deptId = String.valueOf(personList.get(i).getDeptId());
          if(deptIdLogin.equals(deptId)){
            return 1;
          }else{
            return 0;
          }
        }
        return 1;
      } else {
        return 0;
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
  }
  
  /**
   * 获取不带权限的树(包含部门和人员，在线和不在线)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAllTree(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String idStr = request.getParameter("id");
    int id = 0;
    if (idStr != null && !"".equals(idStr)) {
      id = Integer.parseInt(idStr);
    }
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHORM orm = new YHORM();
      StringBuffer buf = null;
      String deptName = "";
      String roleName = "";
      String moduleId = request.getParameter("MODULE_ID");
      String privNoFlagStr = request.getParameter("privNoFlag");
      int privNoFlag = 0;
      if (!YHUtility.isNullorEmpty(privNoFlagStr)) {
        privNoFlag = Integer.parseInt(privNoFlagStr);
      }
      YHModulePriv priv = YHPrivUtil.getMyPrivByModel(dbConn,  person.getSeqId(), moduleId);
      YHMyPriv mp = new YHMyPriv();
      mp = YHPrivUtil.getMyPriv(dbConn, person, moduleId, privNoFlag);
        HashMap map = new HashMap();
        map.put("DEPT_PARENT", id);
        String[] filterDept = new String[]{"DEPT_PARENT = " + id + " order by DEPT_NO ASC , DEPT_NAME ASC"};
        ArrayList<YHDepartment> deptList = (ArrayList<YHDepartment>) orm
            .loadListSingle(dbConn, YHDepartment.class, filterDept);
        buf = new StringBuffer("[");
        //String[] filters = new String[]{"DEPT_ID = " + id +" and not DEPT_ID = '0'"};
        ArrayList<YHPerson> personList = this.getPersons(dbConn, id);
        String ipFlag = getIpState(request, response, dbConn);
        YHOrgSelectLogic osl = new YHOrgSelectLogic();
        String manager = YHPrivUtil.getDeptManager( dbConn ,priv, person, moduleId, privNoFlag) ;
        for (YHPerson m : personList) {
          int seqId = m.getSeqId();
          if(!YHPrivUtil.isUserPriv(dbConn, seqId, mp, person)
              && !YHWorkFlowUtility.findId(manager, seqId + "")){
            continue;
          }
          int deptId = m.getDeptId();
          int roleId = Integer.parseInt(m.getUserPriv());
          String sex = m.getSex();
          deptName = YHUtility.encodeSpecial(getDeptName(dbConn, deptId));
          roleName = YHUtility.encodeSpecial(getRoleName(dbConn, roleId));
          String telNoDept = m.getTelNoDept();
          if(YHUtility.isNullorEmpty(telNoDept)){
            telNoDept = "";
          }
          String email = m.getEmail();
          if(YHUtility.isNullorEmpty(email)){
            email = "";
          }
          String oicq = m.getOicq();
          if(YHUtility.isNullorEmpty(oicq)){
            oicq = "";
          }
          String myStatus = m.getMyStatus();
          String myState = "";
          if(!YHUtility.isNullorEmpty(myStatus)){
            myState = "\\n人员状态:" + YHUtility.encodeSpecial(myStatus) +"" ;
          }
          String showIp = "";
          if("1".equals(ipFlag)){
            if(person.isAdminRole()){
              String ip = osl.getShowIp(dbConn, YHLogConst.LOGIN, seqId);
              ip = YHUtility.encodeSpecial(ip);
              showIp = "\\n最后登录IP:" + ip +"";
            }
          }else if("2".equals(ipFlag)){
            String ip = osl.getShowIp(dbConn, YHLogConst.LOGIN,seqId);
            ip = YHUtility.encodeSpecial(ip);
            showIp = "\\n最后登录IP:" + ip +"";
          }
          buf.append("{");
          buf.append("nodeId:\"r" + m.getSeqId() + "\"");
          buf.append(",name:\"" + YHUtility.encodeSpecial(m.getUserName()) + "\"");
          buf.append(",isHaveChild:" + 0);
          buf.append(",imgAddress:\"" + request.getContextPath()
              + userStateImg(request, response, m.getSeqId(), dbConn, sex));
          buf.append(",title:\"部门:" + deptName + "\\n角色:" + roleName + "\\n工作电话:" + YHUtility.encodeSpecial(telNoDept) + "\\nemail:" + YHUtility.encodeSpecial(email) + "\\nQQ:" + YHUtility.encodeSpecial(oicq) +  myState + showIp +"\"");
          buf.append(",appendHtml:\"<img  id='append-r"+m.getSeqId()+"' src='\" + imgPath + \"/msg.png' style='visibility:hidden;padding-left:2px;cursor: pointer;' onclick=smsFunc('"+m.getSeqId()+"') title='发短信'/><img id='append1-r"+m.getSeqId()+"' src='\" + imgPath + \"/email.png' style='visibility:hidden;cursor: pointer;' onclick=emailFunc('"+m.getSeqId()+"')  title='发邮件'/>\"");
          buf.append("},");
        }
        String deptss = "";
        if (priv != null && "2".equals(priv.getDeptPriv()) 
            && !YHUtility.isNullorEmpty(priv.getDeptId())) {
          String deptStr = priv.getDeptId();
          deptss = deptStr;
        }
        if ("".equals(deptss) || deptss.endsWith(",")) {
          deptss += person.getDeptId();
        }else{
          deptss += "," + person.getDeptId();
        } 
        for (int i = 0; i < deptList.size(); i++) {
          YHDepartment dept = deptList.get(i);
          if (priv != null 
              && ("2".equals(priv.getDeptPriv())
              || "0".equals(priv.getDeptPriv())) && !YHOrgSelectLogic.isShow(dbConn, dept.getSeqId(), deptss) ) {
            continue;
          }
          buf.append("{");
          buf.append("nodeId:\"" + dept.getSeqId() + "\"");
          buf.append(",name:\"" + YHUtility.encodeSpecial(dept.getDeptName()) + "\"");
          buf.append(",isHaveChild:"
                  + IsHaveChild(request, response, String.valueOf(dept.getSeqId())));
          buf.append(",title:\"部门:" + YHUtility.encodeSpecial(dept.getDeptName()) + "\"");
          buf.append(",imgAddress:\"" + request.getContextPath()
              + "/core/styles/style1/img/dtree/node_dept.gif\"");
          buf.append("},");
        }
        if (buf.length() > 1) {
          buf.deleteCharAt(buf.length() - 1);
        }
      buf.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, buf.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDeptName(Connection dbConn, int deptId){
    YHOrgSelectLogic deptNameLogic = new YHOrgSelectLogic();
    String deptNameStr = "";
    try {
      deptNameStr = deptNameLogic.getDeptNameLogic(dbConn, deptId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return deptNameStr;
    
  }
  
  public String getRoleName(Connection dbConn, int roleId){
    YHOrgSelectLogic deptNameLogic = new YHOrgSelectLogic();
    String roleNameStr = "";
    try {
      roleNameStr = deptNameLogic.getRoleNameLogic(dbConn, roleId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return roleNameStr;
    
  }
  
  public String getParallelTree(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHORM orm = new YHORM();
      YHOrgSelectLogic osl = new YHOrgSelectLogic();
      List<YHUserOnline> onLineList = osl.getUserOnlineList(dbConn);
      String ipFlag = getIpState(request, response, dbConn);
      String[] perFilter = null;
      StringBuffer sb = new StringBuffer("[");
      String tmp = "";
      String deptName = "";
      String roleName = "";
      String userIds = osl.getUserOnlineUserId(dbConn);
      if (!YHUtility.isNullorEmpty(userIds)) {
        perFilter = new String[]{"SEQ_ID IN (" + userIds +") and not DEPT_ID = '0' order by DEPT_ID ASC"};
      } else {
        perFilter = new String[]{" 1 <>1 and not DEPT_ID = '0' order by DEPT_ID ASC"};
      }
      //for(int i = 0; i < onLineList.size(); i++){
        //int userIds = onLineList.get(i).getUserId();
        //String userStates = onLineList.get(i).getUserState();
        
        List<YHPerson> personList = orm.loadListSingle(dbConn, YHPerson.class, perFilter);
        for(int x = 0; x < personList.size(); x++){
          int seqId = personList.get(x).getSeqId();
//          if(!YHPrivUtil.isUserPriv(dbConn, seqId, mp, person)){
//            continue;
//          }
          String userIdStr = personList.get(x).getUserId();
          String userPriv = personList.get(x).getUserPriv();
          int deptId = personList.get(x).getDeptId();
          int roleId = Integer.parseInt(personList.get(x).getUserPriv());
          String sex = personList.get(x).getSex();
          String userState = personList.get(x).getOnStatus();
          String userStates = osl.getUserStatesLogic(dbConn, seqId);
          
          String userStateImg = "";
          //<img src="<%=contextPath%>/core/styles/style1/img/notify_new.gif" align="absmiddle">
          if (sex.equals("1")) {
            if(userStates.equals("1")){
              userStateImg = "<img src='"+request.getContextPath()+"/core/styles/style1/img/dtree/1-1.gif' align='absmiddle'>";
            }else if(userStates.equals("2")){
              userStateImg = "<img src='"+request.getContextPath()+"/core/styles/style1/img/dtree/U12.gif' align='absmiddle'>";
            }else if(userStates.equals("3")){
              userStateImg = "<img src='"+request.getContextPath()+"/core/styles/style1/img/dtree/U13.gif' align='absmiddle'>";
            }else {
              userStateImg = "<img src='"+request.getContextPath()+"/core/styles/style1/img/dtree/1-1.gif' align='absmiddle'>";
            }
          } else {
            if (userStates.equals("1")){
              userStateImg = "<img src='"+request.getContextPath()+"/core/styles/style1/img/dtree/0-1.gif' align='absmiddle'>";
            }else if (userStates.equals("2")){
              userStateImg = "<img src='"+request.getContextPath()+"/core/styles/style1/img/dtree/U02.gif' align='absmiddle'>";
            }else if (userStates.equals("3")){
              userStateImg = "<img src='"+request.getContextPath()+"/core/styles/style1/img/dtree/U03.gif' align='absmiddle'>";
            }else {
              userStateImg = "<img src='"+request.getContextPath()+"/core/styles/style1/img/dtree/0-1.gif' align='absmiddle'>";
            }
          }
          
          deptName = YHUtility.encodeSpecial(getDeptName(dbConn, deptId));
          roleName = YHUtility.encodeSpecial(getRoleName(dbConn, roleId));
          
          String telNoDept = personList.get(x).getTelNoDept();
          if(YHUtility.isNullorEmpty(telNoDept)){
            telNoDept = "";
          }
          String email = personList.get(x).getEmail();
          if(YHUtility.isNullorEmpty(email)){
            email = "";
          }
          String oicq = personList.get(x).getOicq();
          if(YHUtility.isNullorEmpty(oicq)){
            oicq = "";
          }
          String myStatus = personList.get(x).getMyStatus();
          String myState = "";
          if(YHUtility.isNullorEmpty(myStatus)){
            myState = "";
          }else{
            myStatus = YHUtility.encodeSpecial(myStatus);
            myState = "\\n人员状态:" + myStatus +"" ;
          }
          String showIp = "";
          if("1".equals(ipFlag)){
            if(person.isAdminRole()){
              String ip = osl.getShowIp(dbConn, YHLogConst.LOGIN, seqId);
              ip = YHUtility.encodeSpecial(ip);
              showIp = "\\n最后登录IP:" + ip +"";
            }
          }else if("2".equals(ipFlag)){
            String ip = osl.getShowIp(dbConn, YHLogConst.LOGIN, seqId);
            ip = YHUtility.encodeSpecial(ip);
            showIp = "\\n最后登录IP:" + ip +"";
          }else {
            showIp = "";
          }
            sb.append("{");
            sb.append("name:\"" + personList.get(x).getUserName() + "\"");
            sb.append(",imgPath:\"" + userStateImg + "\"");
            sb.append(",deptName:\"" + deptName + "\"");
            sb.append(",userId:\"" + seqId + "\"");
            sb.append(",title:\"部门:" + deptName + "\\n角色:" + roleName + "\\n工作电话:" + YHUtility.encodeSpecial(telNoDept) + "\\nemail:" + YHUtility.encodeSpecial(email) + "\\nQQ:" + YHUtility.encodeSpecial(oicq) + myState + showIp + "\"");
            sb.append(",deptId:\"" + deptId + "\"");
            sb.append("},");
          List<YHDepartment> deptList = osl.searchDeptparent(dbConn, deptId);
//          for (int z = 0; z < deptList.size(); z++) {
//            String[] str = tmp.split(",");
//            String deptIdStr = String.valueOf(deptList.get(z).getSeqId());
//            if (!strValue(str, deptIdStr)) {
//              sb.append("{");
//              sb.append("deptName:\"" + deptList.get(z).getDeptName() + "\"");
//              sb.append("},");
//              tmp += String.valueOf(deptList.get(z).getSeqId()) + ",";
//            }
//          }
        }
      //}
      if (sb.length() > 1) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      //System.out.println(sb);
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
  
  public boolean strValue(String[] str, String deptIdStr){
    for(int y = 0; y < str.length; y++){
      if(str[y].equals(deptIdStr)){
        return true;
      }else{
        continue;
      }
    }
    return false;
  }
  
  /**
   * 获取在线状态图片
   * @param request
   * @param response
   * @param userIdStr
   * @param dbConn
   * @return
   * @throws Exception
   */
  
  public String userState(HttpServletRequest request,
      HttpServletResponse response, int userIdStr, Connection dbConn) throws Exception {
    String url = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHOrgSelectLogic osl = new YHOrgSelectLogic();
      String userState = osl.getUserStateImg(dbConn, userIdStr);
      if("1".equals(userState)){
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
  
  public boolean IsHaveChild1(HttpServletRequest request,
      HttpServletResponse response, String id) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("DEPT_PARENT", id);

      String[] str = { "DEPT_ID =" + id };
      YHOrgSelectLogic pls = new YHOrgSelectLogic();
      List<YHPerson> personList = orm.loadListSingle(dbConn, YHPerson.class, str);
      for(int i = 0; i < personList.size(); i++){
        int userId = personList.get(i).getSeqId();
        //System.out.println(userId+"BNMNNBBVB");
        if(pls.getUserState(dbConn, userId)){
          return true;
        }else{
          return false;
        }
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return false;
  }
  /**
   * 判段id是不是在str里面
   * @param str
   * @param id
   * @return
   */
  public static boolean findId(String str, String id) {
    if(str == null || id == null || "".equals(str) || "".equals(id)){
      return false;
    }
    String[] aStr = str.split(",");
    for(String tmp : aStr){
      if(tmp.equals(id)){
        return true;
      }
    }
    return false;
  }
  public ArrayList<YHPerson> getPersons(Connection conn , int deptId) throws Exception {
    String query = "SELECT PERSON.SEQ_ID " 
      + ", USER_NAME  " 
      + ", USER_PRIV  " 
      + ", ON_STATUS  " 
      + ", SEX  " 
      + ", DEPT_ID  " 
      + ", EMAIL  " 
      + ", OICQ  " 
      + ", ICQ"
      + ", TEL_NO_DEPT"
      + ", MY_STATUS FROM PERSON , USER_PRIV WHERE (" +
      		YHDBUtility.findInSet(String.valueOf(deptId), "DEPT_ID_OTHER") + " or "
      + " DEPT_ID =  " + deptId 
      + ") and not DEPT_ID = '0' "
      + " AND USER_PRIV.SEQ_ID = PERSON.USER_PRIV "
      + " order by USER_PRIV.PRIV_NO , PERSON.USER_NO DESC  ,PERSON.SEQ_ID";
    ArrayList<YHPerson> persons = new ArrayList();
    Statement stm4 = null;
    ResultSet rs4 = null;
    Set set = new HashSet();
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()) {
        YHPerson person = new YHPerson();
        int seqId = rs4.getInt("SEQ_ID");
        if (!set.contains(seqId)) {
          person.setSeqId(seqId);
          person.setOnStatus(rs4.getString("ON_STATUS"));
          person.setUserName(rs4.getString("USER_NAME"));
          person.setDeptId(rs4.getInt("DEPT_ID"));
          person.setUserPriv(rs4.getString("USER_PRIV"));
          person.setTelNoDept(rs4.getString("TEL_NO_DEPT"));
          person.setEmail(rs4.getString("EMAIL"));
          person.setIcq(rs4.getString("ICQ"));
          person.setOicq(rs4.getString("OICQ"));
          person.setSex(rs4.getString("SEX"));
          person.setMyStatus(rs4.getString("MY_STATUS"));
          persons.add(person);
          set.add(seqId);
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stm4 , rs4 , null);
    }
    return persons;
  }
  public String getIpState(HttpServletRequest request,
      HttpServletResponse response, Connection dbConn) throws Exception {
    String showIpFlag = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHOrgSelectLogic osl = new YHOrgSelectLogic();
      showIpFlag = osl.getSecrityShowIp(dbConn);
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return showIpFlag;
  }
  
  /**
   * 构造在线用户树
   * @param root
   * @param list
   */
  private void buildOnlineTree(DefaultMutableTreeNode root, List<YHPerson> list) { 
    Enumeration<DefaultMutableTreeNode> em = root.depthFirstEnumeration();
    //生成list操作是为了避免操作树的时候使枚举失效
    List<DefaultMutableTreeNode> l = new ArrayList<DefaultMutableTreeNode>(Collections.list(em));
    for (DefaultMutableTreeNode n : l) {
      addUserNode(n, list);
    }
  }
  
  /**
   * 向一个节点中添加用户,如果没有用户和下级节点则删除这个部门
   * @param node
   * @param list
   */
  private void addUserNode(DefaultMutableTreeNode node, List<YHPerson> list) {
    Object o = node.getUserObject();
    if (o instanceof YHDepartment) {
      YHDepartment d = (YHDepartment)o;
      for (YHPerson p : list) {
        if (this.inDept(p, d.getSeqId())) {
          //插入新节点到左边
          node.insert(new DefaultMutableTreeNode(p), 0);
        }
      }
      if (node.getChildCount() == 0) {
        ((DefaultMutableTreeNode)node.getParent()).remove(node);
      }
    }
  }
  
  /**
   * 判断人员是否在该部门,考虑了辅助部门
   * @param p
   * @param deptId
   * @return
   */
  private boolean inDept(YHPerson p, int deptId) {
    if (p.getDeptId() == deptId) {
      return true;
    }
    String other = "," + p.getDeptIdOther() + ",";
    return other.contains("," + deptId + ",");
  }
  
  /**
   * 把树转为json串
   * @param root
   * @return
   * @throws Exception 
   */
  private StringBuffer breadthFirst(Connection dbConn, DefaultMutableTreeNode root) throws Exception {
    StringBuffer sb = new StringBuffer("[");
    Enumeration<DefaultMutableTreeNode> e = root.breadthFirstEnumeration();
    while (e.hasMoreElements()) {
      DefaultMutableTreeNode o = e.nextElement();
      Object uo = o.getUserObject();
      if (uo != null && uo instanceof YHDepartment) {
        this.deptToString((YHDepartment)uo, sb, o.getChildCount());
        sb.append(",");
      }
      else if (uo != null && uo instanceof YHPerson) {
        Object tmp = ((DefaultMutableTreeNode)o.getParent()).getUserObject();
        if (tmp instanceof YHDepartment) {
          this.userToString(dbConn, (YHPerson)uo, sb, ((YHDepartment)tmp).getSeqId());
          sb.append(",");
        }
      }
    }
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb;
  }
  
  /**
   * 在线人员树的人员节点转成json串
   * @param dbConn
   * @param p
   * @param sb
   * @param parentId
   * @throws Exception 
   */
  private void userToString(Connection dbConn, YHPerson p, StringBuffer sb, int parentId) throws Exception {
    sb.append("{\"nodeId\": \"r");
    sb.append(p.getSeqId());
    sb.append("\",\"name\": \"");
    sb.append(p.getUserName());
    sb.append("\",\"parentId\": \"");
    sb.append(parentId);
    sb.append("\",\"isHaveChild\": 0");
    String sex = p.getSex();
    String userStates = p.getOnStatus();
    String userStateImg;
    if(sex != null && sex.equals("1")){
      if(userStates.equals("1")){
        userStateImg = "/dtree/1-1.gif";
      }else if(userStates.equals("2")){
        userStateImg = "/dtree/U12.gif";
      }else if(userStates.equals("3")){
        userStateImg = "/dtree/U13.gif";
      }else {
        userStateImg = "/dtree/1-1.gif";
      }
    }else {
      if(userStates.equals("1")){
        userStateImg = "/dtree/0-1.gif";
      }else if(userStates.equals("2")){
        userStateImg = "/dtree/U02.gif";
      }else if(userStates.equals("3")){
        userStateImg = "/dtree/U03.gif";
      }else {
        userStateImg = "/dtree/0-1.gif";
      }
    }
    YHOrgSelectLogic logic = new YHOrgSelectLogic();
    String deptName = YHUtility.encodeSpecial(logic.getDeptNameLogic(dbConn, p.getDeptId()));
    String roleName; 
    try {
      roleName = YHUtility.encodeSpecial(logic.getRoleNameLogic(dbConn, Integer.parseInt(p.getUserPriv())));
    } catch (NumberFormatException e) {
      roleName = "";
    }
    
    String showIpFlag = logic.getSecrityShowIp(dbConn);
    String showIp = "";
    if("1".equals(showIpFlag)){
      if (p.isAdminRole()) {
        String ip = logic.getShowIp(dbConn, YHLogConst.LOGIN, p.getSeqId());
        ip = YHUtility.encodeSpecial(ip);
        showIp = "\\n最后登录IP:" + ip;
      }
    }
    else if("2".equals(showIpFlag)) {
      String ip = logic.getShowIp(dbConn, YHLogConst.LOGIN, p.getSeqId());
      ip = YHUtility.encodeSpecial(ip);
      showIp = "\\n最后登录IP:" + ip;
    }
    
    sb.append(",\"title\": \"部门:");
    sb.append(deptName);
    sb.append("\\n角色:");
    sb.append(roleName);
    sb.append("\\n工作电话:");
    sb.append(YHUtility.encodeSpecial(p.getTelNoDept()));
    sb.append("\\nemail:");
    sb.append(YHUtility.encodeSpecial(p.getEmail()));
    sb.append("\\nQQ:");
    sb.append(YHUtility.encodeSpecial(p.getOicq()));
    sb.append("\\n人员状态:");
    sb.append(YHUtility.encodeSpecial(p.getMyStatus()));
    sb.append(showIp);
    sb.append("\",\"imgAddress\": imgPath + \"");
    sb.append(userStateImg);
    sb.append("\"}");
    
  }
  
  /**
   * 在线人员树的部门节点转成json串
   * @param d
   * @param sb
   * @param childCount
   */
  private void deptToString(YHDepartment d, StringBuffer sb, int childCount) {
    sb.append("{\"nodeId\": \"");
    sb.append(d.getSeqId());
    sb.append("\",\"name\": \"");
    sb.append(d.getDeptName());
    sb.append("\",\"parentId\": \"");
    sb.append(d.getDeptParent());
    sb.append("\",\"isHaveChild\": ");
    sb.append(childCount > 0 ? 1 : 0);
    sb.append(",\"title\": \"部门:");
    sb.append(d.getDeptName());
    sb.append("\",\"imgAddress\": imgPath + \"/dtree/node_dept.gif\"}");
  }
  
  /**
   * 按照角色对在线人员列表排序
   * @param dbConn
   * @param userList
   * @param sort          排序方向: sort > 0 - asc, sort < 0 - desc
   * @throws Exception
   */
  private void sortUserListByRole(final Connection dbConn, List<YHPerson> userList, final int sort) throws Exception {
    Collections.sort(userList, new Comparator<YHPerson>() {
      YHOrgSelectLogic logic = new YHOrgSelectLogic();
      public int compare(YHPerson p1, YHPerson p2) {
        try {
          int r = logic.getUserPrivNo(dbConn, p1.getUserPriv()).compareTo(logic.getUserPrivNo(dbConn, p2.getUserPriv()));
          if (r == 0) {
            r = p2.getUserNo() - p1.getUserNo();
          }
          if (r == 0) {
//            Collator cmp = Collator.getInstance(java.util.Locale.CHINA); 
//            r = cmp.compare(p1.getUserName(), p2.getUserName());
            r = p1.getUserId().compareTo(p2.getUserId());
          }
          return sort > 0 ? r : -r;
        } catch (Exception e) {
          return 0;
        }
      }
    });
  }
  
  /**
   * 在线人员树
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getOnLineTree(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHOrgSelectLogic logic = new YHOrgSelectLogic();
      DefaultMutableTreeNode root = logic.buildDeptTree(dbConn);
      //获取登陆人员
      List<YHPerson> onlineUsers = logic.getOnlineUsers(dbConn);
      
      //反向排序(因为人员插入到树的左侧)
      this.sortUserListByRole(dbConn, onlineUsers, -1);
      this.buildOnlineTree(root, onlineUsers);
      
      StringBuffer sb = breadthFirst(dbConn, root);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
