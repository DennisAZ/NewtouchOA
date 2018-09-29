package yh.core.funcs.dept.act;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.seclog.logic.YHSecLogUtil;
import yh.core.funcs.system.ispirit.n12.org.act.YHIsPiritOrgAct;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

public class YHDeptAct {
  private static Logger log = Logger.getLogger(YHDeptAct.class);

  public String insertDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String deptParentDesc = request.getParameter("deptParentDesc");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String deptNo = request.getParameter("deptNo");
      int newSeqId = Integer.parseInt(getDeptAddSeq(dbConn));
      String remark = getDeptLog(dbConn, newSeqId);
      
      YHDeptLogic dl = new YHDeptLogic();
//      if(dl.existsTableNo(dbConn, deptNo)){
//        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
//        request.setAttribute(YHActionKeys.RET_MSRG, "部门排序号以存在，请重新填写！");
//        return "/core/inc/rtjson.jsp";
//      }else{
//        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
//        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功");
//      }
      YHDepartment dpt = (YHDepartment) YHFOM.build(request.getParameterMap());
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, dpt);
      
      
      //生成org.xml文件
      YHIsPiritOrgAct.getOrgDataStream(dbConn);
      
      YHSysLogLogic.addSysLog(dbConn, YHLogConst.ADD_DEPT, remark, person.getSeqId(), request.getRemoteAddr());
      // add  seclog
      try{
      YHSecLogUtil.log(dbConn, person, request.getRemoteAddr(), "205", dpt,"1", "新建部门名称为'"+dpt.getDeptName()+"'");
      }catch(Exception e){}
      //dbConn.close();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
  
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDept(HttpServletRequest request, HttpServletResponse response)
      throws Exception{
    Connection dbConn = null;
    try{
      int treeId = Integer.parseInt(request.getParameter("treeId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      YHORM orm = new YHORM();
      Object obj = orm.loadObjSingle(dbConn, YHDepartment.class, treeId);
      data = YHFOM.toJson(obj).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String getGroupDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      int treeId = Integer.parseInt(request.getParameter("treeId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDepartment dpt = null;
      String data = null;
      YHORM orm = new YHORM();
      data = YHFOM.toJson(orm.loadObjSingle(dbConn, dpt.getClass(), 6)).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 系统日志：获取部门DEPT_ID,DEPT_NAME,DEPT_PARENT
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getDeptLog(Connection dbConn, int deptId) throws Exception {
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
  
  public String getDeptAddSeq(Connection dbConn) throws Exception {
    String data = "";
    YHDeptLogic dl = new YHDeptLogic();
    data = dl.getDeptAddSeqLogic(dbConn);
    return data;
  }
  
  public String updateDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      String managerDesc = request.getParameter("managerDesc");
      int treeId = Integer.parseInt(request.getParameter("treeId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String deptNo = request.getParameter("deptNo");
      String deptNoOld = request.getParameter("deptNoOld");
      YHDeptLogic dl = new YHDeptLogic();
      YHDepartment dpt = (YHDepartment) YHFOM.build(request.getParameterMap());
      dpt.setSeqId(treeId);
      String remark = getDeptLog(dbConn, treeId);
      
      YHDepartment dptOld = dl.getDepartmentById(dpt.getSeqId(), dbConn);
      
      YHORM orm = new YHORM();
      if((deptNoOld.trim()).equals(deptNo.trim())){
        orm.updateSingle(dbConn, dpt);
        YHSysLogLogic.addSysLog(dbConn, YHLogConst.EIDT_DEPT, remark.replace("添加","更新"), person.getSeqId(), request.getRemoteAddr());
        // add  seclog
        try{
         String dptOldName = "";
         if(dptOld != null){
           dptOldName = dptOld.getDeptName();
         }
        YHSecLogUtil.log(dbConn, person, request.getRemoteAddr(), "205",dpt,"1", "修改部门名称'" +dptOldName + "'改为'" + dpt.getDeptName() + "'" );
        }catch(Exception e){}
        
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功更改数据库的数据");
      }else{
        if(dl.existsTableNo(dbConn, deptNo)){
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "部门排序号已存在，请重新填写！");
          return "/core/inc/rtjson.jsp";
        }else{
          orm.updateSingle(dbConn, dpt);
          YHSysLogLogic.addSysLog(dbConn, YHLogConst.EIDT_DEPT, remark, person.getSeqId(), request.getRemoteAddr());
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "成功更改数据库的数据");
        }
      }

      //生成org.xml文件
      YHIsPiritOrgAct.getOrgDataStream(dbConn);
      
   
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 级联删除部门及部门下的人员，如果该部门下有系统管理员（不能删除），系统管理员着转到离职人员中
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try{
      int seqId = Integer.parseInt(request.getParameter("treeId"));
      String seqdd = request.getParameter("treeId");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDepartment dt = null;
      YHDepartment dtt = new YHDepartment();
      dtt.setSeqId(seqId);
      YHDepartment ment = null;
      YHORM orm = new YHORM();
      YHDeptLogic deptlogic = new YHDeptLogic();
      //deptlogic.deleteDeptMul(dbConn, seqId);
      List lista = new ArrayList();
      List lidd = deptlogic.deleteDeptMul(dbConn, seqId);
      
     
    
      
      //生成org.xml文件
      YHIsPiritOrgAct.getOrgDataStream(dbConn);
      
      
      dtt = deptlogic.getDepartmentById(seqId, dbConn);
      lidd.add(dtt);
      String remark = getDeptLog(dbConn, seqId);
      
      YHSysLogLogic.addSysLog(dbConn, YHLogConst.DELETE_DEPT, remark, person.getSeqId(), request.getRemoteAddr());
      // add  seclog

      String remarkStr = "";
      String userSeqIdStr = "seqId-->";
      String userNameStr = "";
      for(int i = 0; i < lidd.size(); i++){
       //ment =  (YHDepartment) deptlogic.deleteDeptMul(dbConn, seqId).get(i);
        //dt = (YHDepartment)orm.loadObjComplex(dbConn, YHDepartment.class, deptent.getSeqId());
        //dt.setSeqId(deptent.getSeqId());
        YHDepartment deptent = (YHDepartment) lidd.get(i);
        remarkStr = remarkStr + deptent.getDeptName() + ",";
        String[] filters = new String[]{"DEPT_ID = " + deptent.getSeqId() + ""};
        List<YHPerson> listPer = orm.loadListSingle(dbConn, YHPerson.class, filters);
  
        for(int x = 0; x < listPer.size(); x++){//删除部门中的用户
          YHPerson per = (YHPerson) listPer.get(x);
   
          if(per.isAdmin()){//admin用户则移植到第一级
            per.setDeptId(0);
            orm.updateSingle(dbConn, per);
            continue;
          }else{
            userSeqIdStr = userSeqIdStr + per.getSeqId() +",";
            userNameStr = userNameStr + per.getUserName() + ",";
            deptlogic.deleteDepPerson(dbConn, deptent.getSeqId());
          }
        }
        deptlogic.deleteDept(dbConn, deptent.getSeqId());
        //orm.deleteComplex(dbConn, dt);
      }
      
      try{
      YHSecLogUtil.log(dbConn, person, request.getRemoteAddr(), "205", remark,"1", "删除部门名称为'" +remarkStr + "'");
      
      if(!YHUtility.isNullorEmpty(userNameStr)){//如果有人员
        YHSecLogUtil.log(dbConn, person, request.getRemoteAddr(), "205", userSeqIdStr,"1", "级联删除部门中的用户名称为'" +userNameStr + "'");
        
      }
      }catch(Exception e){}
     // YHDepartment dt = (YHDepartment)orm.loadObjComplex(dbConn, YHDepartment.class, seqId);
      //YHDepartment dpt = (YHDepartment) YHFOM.build(request.getParameterMap());
      //dt.setSeqId(seqId);
      //orm.deleteComplex(dbConn, dt);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功删除数据库的数据");
      
      //生成org.xml文件
      YHIsPiritOrgAct.getOrgDataStream(dbConn);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getTree(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
    String idStr = request.getParameter("id");
    int id = 0;
    if(idStr != null && !"".equals(idStr.trim())){
      id = Integer.parseInt(idStr);
    }
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/xml");
    response.setHeader("Cache-Control", "no-cache");
    PrintWriter out = response.getWriter();
    out.print("<?xml version=\'1.0\' encoding=\'utf-8'?>");
    out.print("<menus>");
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("deptId", id);
      List<YHPerson> list = orm.loadListSingle(dbConn, YHPerson.class, map);
      for(YHPerson d : list){
        out.print("<menu>");
        out.print("<id>" + d.getSeqId() + "</id>");
        out.print("<name>" + d.getUserId() + "</name>");
        out.print("<parentId>" + d.getDeptId() + "</parentId>");
        out.print("<isHaveChild>0</isHaveChild>");
        out.print("</menu>");
      }
      map.remove("deptId");
      map.put("deptParent", id);
      List<YHDepartment> deptList = orm.loadListSingle(dbConn, YHDepartment.class, map);
      for(YHDepartment t : deptList) {
        out.print("<menu>");
        out.print("<id>" + t.getSeqId() + "</id>");
        out.print("<name>" + t.getDeptName() + "</name>");
        out.print("<parentId>" + t.getDeptParent() + "</parentId>");
        out.print("<isHaveChild>" + IsHaveChild(request, response, String.valueOf(t.getSeqId())) + "</isHaveChild>");
        out.print("</menu>");      
      }    
      out.print("<parentNodeId>" + id + "</parentNodeId>");
      out.print("<count>" + (list.size()+deptList.size()) + "</count>");
      out.print("</menus>");
      out.flush();
      out.close();
      //dbConn.close();
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  public int IsHaveChild(HttpServletRequest request,
      HttpServletResponse response,String id) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("DEPT_PARENT", id);
      List<YHDepartment> list = orm.loadListSingle(dbConn, YHDepartment.class, map);
      if(list.size() != -1){
        return 1;
      }else{
        return 0;
      }
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
  }
  
  public String selectDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    response.setContentType("text/html;charset=UTF-8");
    request.setCharacterEncoding("UTF-8");
    String treeId = request.getParameter("treeId");
    String TO_ID = request.getParameter("TO_ID");
    String TO_NAME = request.getParameter("TO_NAME");
    //String deptLocal = new String(request.getParameter("deptLocal").getBytes("ISO-8859-1"),"UTF-8");
    String deptLocal = request.getParameter("deptLocal");
    String managerList = request.getParameter("deptList");
    try{
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      ArrayList<YHPerson> personList = null;
      Map map = new HashMap();
      map.put("DEPT_ID", treeId);
      //map.put("manager", managerList);
      personList = (ArrayList<YHPerson>)orm.loadListSingle(dbConn, YHPerson.class, map);
      request.setAttribute("personList", personList);
      //dbConn.close();
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    return "/core/funcs/dept/user.jsp?TO_ID=" + TO_ID + "&TO_NAME=" + TO_NAME + "&deptLocal=" + deptLocal;
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
  
  public String selectDeptToAttendance(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHDeptLogic deptLogic = new YHDeptLogic();
      String postpriv = user.getPostPriv();// 管理范围
      String postDept = user.getPostDept();// 管理范围指定部门
      String deptId = String.valueOf(user.getDeptId());
      int deptIdTmp = user.getDeptId();
      String postDeptTmp = user.getPostDept();
      String deptParent = request.getParameter("deptParent");
      String parentId = request.getParameter("parentId");
      int postDeptId = 0;
      String postDeptStr = "";
      int userDeptId = user.getDeptId();
      int userDeptIdFunc = Integer.parseInt(request.getParameter("userDeptId"));
      String userDeptIdStr = request.getParameter("userDeptId");

      String data = "";
      if (YHUtility.isNullorEmpty(postpriv)) {
        String[] postDeptArray = { String.valueOf(userDeptId) };
        data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray) + "]";
      } else {
        if (postpriv.equals("0")) {
          if (findId(deptId, userDeptIdStr)) {
            data = "[{text:'" + deptParent + "'" + ", value: '" + parentId + "'}]";
          } else {
            String deptLog = deptLogic.getChildDeptId(dbConn, deptIdTmp);
            if (deptLog.length() > 0) {
              deptId += "," + deptLog.substring(0, deptLog.length() - 1);
            }
            String[] deptArray = deptId.split(",");
            data = "["
                + deptLogic.getDeptTreeJsonSelf(0, dbConn, deptArray,
                    userDeptIdFunc) + "]";
            // String[] postDeptArray1 = {String.valueOf(userDeptId)};
            // data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray1)
            // + "]";
          }
        }
        if (postpriv.equals("1")) {
          data = deptLogic.getDeptTreeJson1(0, dbConn, userDeptIdFunc);
        }
        if (postpriv.equals("2")) {
          if (postDept == null || postDept.equals("")) {
            data = "[]";
          } else {
            String[] postDeptFunc = postDept.split(",");
            for (int i = 0; i < postDeptFunc.length; i++) {
              postDeptId = Integer.parseInt(postDeptFunc[i]);
              postDeptStr += deptLogic.getChildDeptId(dbConn, postDeptId);
            }
            postDept += "," + postDeptStr;
            String[] postDeptArray = postDept.split(",");
            if (findId(postDeptTmp, userDeptIdStr)) {
              data = "[{text:'" + deptParent + "'" + ", value: '" + parentId + "'}]";
            } else {
              // getDeptTreeJson
              data = "["
                  + deptLogic.getDeptTreeJsonSelf(0, dbConn, postDeptArray,
                      userDeptIdFunc) + "]";
            }
          }
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, String.valueOf(userDeptId)
          + "," + postpriv);
      request.setAttribute(YHActionKeys.RET_DATA, data);
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
    OutputStream ops = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      String fileName = URLEncoder.encode("OA部门.csv", "UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      YHDeptLogic ieml = new YHDeptLogic();
      ArrayList<YHDbRecord > dbL = ieml.toExportDeptData(conn);
      YHCSVUtil.CVSWrite(response.getWriter(), dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
    }
    return null;
  }
  
  public String importDept(HttpServletRequest request,HttpServletResponse response) throws Exception{
    InputStream is = null;
    Connection conn = null;
    String data = null;
    int isCount = 0;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      is = fileForm.getInputStream();
      ArrayList<YHDbRecord> drl = YHCSVUtil.CVSReader(is, YHConst.CSV_FILE_CODE);
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      StringBuffer sb = new StringBuffer("[");
      YHDeptLogic dl = new YHDeptLogic();
      String deptName = "";
      String deptParent = "";
      String deptNo = "";
      String infoStr= "";
      String telNo = "";
      String faxNo = "";
      String deptFunc = "";
      String color = "red";
      int deptParentNo = 0;
      
      String remark = "成功导入部门：";
      boolean hasSucess = false;
      YHDeptLogic logic = new YHDeptLogic();
      for(int i = 0; i < drl.size(); i++){
        deptName = (String) drl.get(i).getValueByName("部门名称");
        if(YHUtility.isNullorEmpty(deptName)){
          continue;
        }
        deptName = getOutOf(deptName);
        deptParent = getOutOf((String) drl.get(i).getValueByName("上级部门"));
        deptNo = getOutOf((String) drl.get(i).getValueByName("部门排序号"));
        telNo = getOutOf((String) drl.get(i).getValueByName("部门电话"));
        faxNo = getOutOf((String) drl.get(i).getValueByName("部门传真"));
        deptFunc = getOutOf((String) drl.get(i).getValueByName("部门职能"));
        
        infoStr = "导入失败,部门 " + deptName + " 已经存在";
        if(dl.existsDeptName(conn, deptName)){
          color = "red";
          infoStr = "导入失败,部门 " + deptName + " 已经存在";
          sb.append("{");
          sb.append("deptName:\"" + (deptName == null ? "" : deptName)+ "\"");
          sb.append(",deptNo:\"" + (deptNo == null ? "" : deptNo) + "\"");
          sb.append(",deptParent:\"" + (deptParent == null ? "" : deptParent) + "\"");
          sb.append(",info:\"" + (infoStr == null ? "" : infoStr) + "\"");
          sb.append(",color:\"" + (color == null ? "" : color) + "\"");
          sb.append("},");
        }else{
          isCount++;
          infoStr = "成功";
          color = "black";
          if (!YHUtility.isInteger(telNo)) {
            telNo = "";
          }
          if (!YHUtility.isInteger(faxNo)) {
            faxNo = "";
          }
          if (!YHUtility.isNumber(deptNo)) {
            deptNo = "0";
          }
          sb.append("{");
          sb.append("deptName:\"" + (deptName == null ? "" : deptName) + "\"");
          sb.append(",deptNo:\"" + (deptNo == null ? "" : deptNo) + "\"");
          sb.append(",deptParent:\"" + (deptParent == null ? "" : deptParent) + "\"");
          sb.append(",info:\"" + (infoStr == null ? "" : infoStr) + "\"");
          sb.append(",color:\"" + (color == null ? "" : color) + "\"");
          sb.append("},");
          if(YHUtility.isNullorEmpty(deptParent)){
            deptParentNo = 0;
          }else{
            deptParentNo = dl.getDeptIdLogic(conn, deptParent);
          }
          logic.saveDept(conn, deptName, deptParentNo, deptNo, telNo, faxNo, deptFunc);
          //YHORM orm = new YHORM();
          //orm.saveSingle(conn , "department" , map);
          remark += deptName + ",";
          hasSucess = true;
        }
      }
      if (sb.charAt(sb.length() - 1) == ','){
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      data = sb.toString();
      if (hasSucess) {
        YHSysLogLogic.addSysLog(conn, YHLogConst.ADD_DEPT, remark, person.getSeqId(), request.getRemoteAddr());
      }
      
      //生成org.xml文件
      YHIsPiritOrgAct.getOrgDataStream(conn);
      
      request.setAttribute("contentList", data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    } 
    return "/core/funcs/dept/importDept.jsp?data="+data+"&isCount="+isCount;
  }
  
  public String getDeptSelectFunc(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int deptId = Integer.parseInt(request.getParameter("deptId"));
      int userId = user.getSeqId();
      String userPriv = user.getUserPriv();// 角色
      String postpriv = user.getPostPriv();// 管理范围
      String postDept = user.getPostDept();// 管理范围指定部门
      int userDeptId = user.getDeptId();
      YHDeptLogic deptLogic = new YHDeptLogic();
      boolean isAdminRole = user.isAdminRole();
      boolean isAdmin = user.isAdmin();
      String userDeptName = deptLogic.getNameByIdStr(
          String.valueOf(userDeptId), dbConn);
      String data = "";
      if (YHUtility.isNullorEmpty(postpriv)) {
        if (isAdminRole && !isAdmin) {
          if (userDeptId != deptId) {
            String deptName = deptLogic.getDeptName(dbConn, deptId);
            data = "[{text:'" + deptName + "',value:" + deptId + "}]";
          } else {
            String[] postDeptArray = { String.valueOf(userDeptId) };
            data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
                + "]";
          }

        } else {
          String[] postDeptArray = { String.valueOf(userDeptId) };
          data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
              + "]";
        }
      } else {
        if (postpriv.equals("0")) {
          // data = "[{text:\"" + userDeptName + "\",value:" + userDeptId +
          // "}]";
          if (isAdminRole && !isAdmin) {
            if (userDeptId != deptId) {
              String deptName = deptLogic.getDeptName(dbConn, deptId);
              data = "[{text:'" + deptName + "',value:" + deptId + "}]";
            } else {
              String[] postDeptArray = { String.valueOf(userDeptId) };
              data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
                  + "]";
            }

          } else {
            String[] postDeptArray = { String.valueOf(userDeptId) };
            data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
                + "]";
          }
        }
        if (postpriv.equals("1")) {
          data = deptLogic.getDeptTreeJson(0, dbConn);
        }
        if (postpriv.equals("2")) {
          if (isAdminRole && !isAdmin) {
            if (!findId(postDept, String.valueOf(deptId))) {
              String deptName = deptLogic.getDeptName(dbConn, deptId);
              data = "[{text:'" + deptName + "',value:" + deptId + "}]";
            } else {
              String[] postDeptArray = postDept.split(",");
              data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
                  + "]";

            }
          } else {
            if (postDept == null || postDept.equals("")) {
              data = "[]";
            } else {
              String[] postDeptArray = postDept.split(",");
              data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
                  + "]";

            }
          }
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, String.valueOf(userDeptId)
          + "," + postpriv);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 根据用户的管理权限得到所有部门

   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectDeptToPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      // int deptId = Integer.parseInt(request.getParameter("deptId"));
      int userId = user.getSeqId();
      String userPriv = user.getUserPriv();// 角色
      String postpriv = user.getPostPriv();// 管理范围
      String postDept = user.getPostDept();// 管理范围指定部门
      int userDeptId = user.getDeptId();
      YHDeptLogic deptLogic = new YHDeptLogic();
      boolean isAdminRole = user.isAdminRole();
      boolean isAdmin = user.isAdmin();
      String userDeptName = deptLogic.getNameByIdStr(
          String.valueOf(userDeptId), dbConn);
      String data = "";
      if (YHUtility.isNullorEmpty(postpriv)) {
        String[] postDeptArray = { String.valueOf(userDeptId) };
        data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray) + "]";
      } else {
        if (postpriv.equals("0")) {
          // data = "[{text:\"" + userDeptName + "\",value:" + userDeptId +
          // "}]";
          String[] postDeptArray = { String.valueOf(userDeptId) };
          data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
              + "]";
        }
        if (postpriv.equals("1")) {
          data = deptLogic.getDeptTreeJson(0, dbConn);
        }
        if (postpriv.equals("2")) {
          if (postDept == null || postDept.equals("")) {
            data = "[]";
          } else {
            String[] postDeptArray = postDept.split(",");
            data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
                + "]";

          }
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, String.valueOf(userDeptId)
          + "," + postpriv);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getOutOf(String str) {
    if (str != null) {
      str = str.replace("'", "");
      str = str.replace("\"", "");
      str = str.replace("\\", "");
      str = str.replaceAll("\n", "");
      str = str.replaceAll("\r", "");
    }
    return str;
  }
}
