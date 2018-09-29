package yh.core.funcs.workflow.act;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowSort;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.logic.YHFlowSortLogic;
import yh.core.funcs.workflow.logic.YHFlowTypeLogic;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHFlowSortAct {
  private static Logger log = Logger.getLogger(YHFlowSortAct.class);
  public String getSortTreeJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFlowSortLogic flowSort = new YHFlowSortLogic();
      String data = flowSort.getSortTreeJson(dbConn, loginUser, 0 ,  false);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出流程分类数据！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getSortName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFlowSortLogic flowSort = new YHFlowSortLogic();
      String data = flowSort.getSortTreeJson(dbConn, loginUser, 0 , false);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出流程分类数据！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String listFlowSort(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFlowSortLogic flowSort = new YHFlowSortLogic();
      String data = flowSort.getSortTreeJson(dbConn, loginUser, 0 , true);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出流程分类数据！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getFlowSort(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String seqId = request.getParameter("seqId");
    String data = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
  
      YHFlowSort flowSort = null;
     
      YHORM orm = new YHORM();
      flowSort = (YHFlowSort)orm.loadObjSingle(dbConn, YHFlowSort.class, Integer.parseInt(seqId));
      if(flowSort == null) {
        flowSort = new YHFlowSort();
      }

      StringBuffer sb = new StringBuffer("{");
      sb.append("seqId:\"" + flowSort.getSeqId() + "\"");
      sb.append(",sortNo:\"" + flowSort.getSortNo() + "\"");
      sb.append(",sortName:\"" + flowSort.getSortName() + "\"");
      sb.append(",deptId:\"" + flowSort.getDeptId() + "\"");
      sb.append("}");
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
  
  public String addFlowSort(HttpServletRequest request,
      HttpServletResponse response) throws Exception  {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      
      YHORM orm = new YHORM();
      String sortNo = request.getParameter("sortNo");
      String sortParent = request.getParameter("sortParent");
      String sortName = request.getParameter("sortName");
      String deptId = request.getParameter("deptId");
      
      String haveChild = "0";
      
      if(!sortParent.equals("0")) {
        String no = "1";
        Map map = new HashMap();
        map.put("seqId", sortParent);
        map.put("haveChild", no);
        orm.updateSingle(dbConn, "flowSort", map);
      }
      Map map = new HashMap();
      map.put("sortNo", sortNo);
      map.put("sortParent", sortParent);
      map.put("sortName", sortName);
      map.put("deptId", deptId);
      map.put("haveChild", haveChild);
      
      orm.saveSingle(dbConn, "flowSort", map);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功添加流程分类");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateFlowSort(HttpServletRequest request,
      HttpServletResponse response) throws Exception  {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      
      YHFlowSort flowSort = (YHFlowSort)YHFOM.build(request.getParameterMap());
      
      YHORM orm = new YHORM(); 
      orm.updateSingle(dbConn, flowSort);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"流程分类修改成功!");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String deleteFlowSort(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");  
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("seqId", seqId);
      orm.deleteSingle(dbConn, "flowSort", map);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除流程分类！");
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
    if (idStr != null && !"".equals(idStr)) {
      id = Integer.parseInt(idStr);
    }
    
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
          
      YHORM orm = new YHORM();
      HashMap map = new HashMap();
      map.put("DEPT_PARENT", id);
      StringBuffer sb = new StringBuffer("[");
      
      ArrayList<YHDepartment> deptList = null;
      deptList = (ArrayList<YHDepartment>)orm.loadListSingle(dbConn, YHDepartment.class, map);
      
      for(int i = 0; i < deptList.size(); i++) {
        YHDepartment dept = deptList.get(i);
        sb.append("{");
        sb.append("nodeId:\"" + dept.getSeqId() + "\"");
        sb.append(",name:\"" + dept.getDeptName() + "\"");
        sb.append(",isHaveChild:" + IsHaveSon(request, response, String.valueOf(dept.getSeqId())) + "");
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
  /**
   * 主要是流程管理时，右边的滑动菜单
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSortList(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String idStr = request.getParameter("id");
    int id = 0;
    if(idStr!=null && !"".equals(idStr)){
      id = Integer.parseInt(idStr);
    }
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       stmt = dbConn.createStatement();
       YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
       String deptIds = user.getDeptId() + ",0";
       String deptOther = user.getDeptIdOther();
       if (!YHUtility.isNullorEmpty(deptOther) ) {
         deptIds = deptIds + "," + deptOther;
         deptIds = YHWorkFlowUtility.getOutOfTail(deptIds);
       }
       int count = 0 ;
       StringBuffer sb = new StringBuffer("[");
       YHFlowTypeLogic ft = new YHFlowTypeLogic();
       YHWorkFlowUtility w = new YHWorkFlowUtility();
       if (id != 0 ) {
         List<YHFlowType> typeList = ft.getFlowTypeList(id , dbConn);
         for(YHFlowType ftTmp : typeList){
           int deptId = ftTmp.getDeptId();
           if (!w.isHaveRight(deptId, user, dbConn)) {
             continue;
           }
           count++;
           int nodeId = ftTmp.getSeqId();
           String name = YHWorkFlowUtility.encodeSpecial(ftTmp.getFlowName());
           sb.append("{");
           sb.append("nodeId:\"T" + nodeId + "\"");
           sb.append(",name:\"" + name + "\"");
           sb.append(",isHaveChild:0");
           sb.append(",imgAddress:\""+ request.getContextPath() +"/core/funcs/workflow/flowtype/img/workflow.gif\"");
           sb.append("},");
         }
       }
       String queryStr = "select SEQ_ID , SORT_NAME , SORT_NO  from oa_fl_sort where SORT_PARENT =" + id ;
       if (!user.isAdminRole()) {
         queryStr += " AND  DEPT_ID IN (" + deptIds + ") " ;
       }
       queryStr += " order by SORT_NO";
       rs = stmt.executeQuery(queryStr);
       while(rs.next()){
          int nodeId = rs.getInt("SEQ_ID");
          
          boolean flag =  IsHaveChild( dbConn ,  nodeId ,  user , ft ,   w ,  deptIds);
          String name = rs.getString("SORT_NAME");
          if (flag) {
            sb.append("{");
            sb.append("nodeId:\"" + nodeId + "\"");
            sb.append(",name:\"" + name + "\"");
            sb.append(",isHaveChild:1");
            sb.append(",imgAddress:\""+ request.getContextPath() +"/core/funcs/workflow/flowtype/img/folder.gif\"");
            sb.append("},");
            count++;
          }
        }
       if (count > 0) {
         sb.deleteCharAt(sb.length() - 1);
       }
        sb.append("]");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
        request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }finally {
        YHDBUtility.close(stmt, rs, null);
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 新建工作时，左边的列表，主要是考虑了权限的问题
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSortListR(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String idStr = request.getParameter("id");
    int id = 0;
    if(idStr!=null && !"".equals(idStr)){
      id = Integer.parseInt(idStr);
    }
    Statement stmt = null;
    ResultSet rs = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      sb.append("[");
      int count = 0;
      if (id != 0) {
        YHFlowTypeLogic ft = new YHFlowTypeLogic();
        List<YHFlowType> typeList = ft.getFlowTypeList(id , dbConn);
        YHPrcsRoleUtility tru = new YHPrcsRoleUtility();
        for(YHFlowType ftTmp : typeList){
          //如果是自由流程，只判断新建权限，如果是固定流程需判断第一步的经办权限， 如果第一步经办权限没设或没有第一步，只不会显示
          boolean flag = false;
          flag = tru.prcsRole(ftTmp, 0, user, dbConn);
          if(flag){
            int nodeId = ftTmp.getSeqId();
            String name = ftTmp.getFlowName();
            sb.append("{");
            sb.append("nodeId:\"F" + nodeId + "\"");
            sb.append(",name:\"" + name + "\"");
            sb.append(",isHaveChild:0");
            sb.append(",imgAddress:\""+ request.getContextPath() +"/core/funcs/workflow/flowtype/img/workflow.gif\"");
            sb.append("},");
            count++;
          }
        }
      }
      String queryStr = "select SEQ_ID , SORT_NAME , SORT_NO  from oa_fl_sort where SORT_PARENT =" + id ;
      queryStr += " order by SORT_NO";
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while(rs.next()){
         int nodeId = rs.getInt("SEQ_ID");
         String name = rs.getString("SORT_NAME");
         if (this.isHaveFlow(dbConn, nodeId , user)) {
           sb.append("{");
           sb.append("nodeId:\"" + nodeId + "\"");
           sb.append(",name:\"" + name + "\"");
           sb.append(",isHaveChild:1");
           sb.append(",imgAddress:\""+ request.getContextPath() +"/core/funcs/workflow/flowtype/img/folder.gif\"");
           sb.append("},");
           count++;
         }
       }
      if(count > 0){
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }finally {
        YHDBUtility.close(stmt, rs, null);
    }
    return "/core/inc/rtjson.jsp";
  }
  public boolean isHaveFlow(Connection conn  , int sortId , YHPerson user ) throws Exception {
    YHFlowTypeLogic ft = new YHFlowTypeLogic();
    YHPrcsRoleUtility tru = new YHPrcsRoleUtility();
    List<YHFlowType> typeList = ft.getFlowTypeList(String.valueOf(sortId) , conn);
    boolean flag = false;
    for(YHFlowType ftTmp : typeList){
      flag = tru.prcsRole(ftTmp, 0, user, conn);
      if (flag) {
        return flag;
      }
    }
    List<Integer> seqIds = new ArrayList<Integer>();
    String queryStr = "select SEQ_ID  from oa_fl_sort where SORT_PARENT =" + sortId ;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while(rs.next()){
         int nodeId = rs.getInt("SEQ_ID");
         seqIds.add(nodeId);
       }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    for (int id : seqIds) {
      flag = this.isHaveFlow(conn, id, user);
       if (flag) {
          return flag;
       }
    }
    return flag;
  }
  /**
   * 新建工作时，左边的列表，主要是考虑了权限的问题
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getFlowType(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String sSortId = request.getParameter("sortId");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer();
      YHFlowSortLogic fs = new YHFlowSortLogic();
      YHFlowTypeLogic ft = new YHFlowTypeLogic();
      List<YHFlowSort> list  = new ArrayList();
      if (sSortId == null || "".equals(sSortId)) {
        list = fs.getFlowSort(dbConn);
      } else {
        sSortId = YHWorkFlowUtility.getInStr(sSortId);
        list =  fs.getFlowSortByIds(dbConn , sSortId);
      }
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      sb.append("[");
      int count = 0;
      long date1 = System.currentTimeMillis();
      for(YHFlowSort tmp : list){
        List<YHFlowType> typeList = ft.getFlowTypeList(tmp.getSeqId()+ "" , dbConn);
        YHPrcsRoleUtility tru = new YHPrcsRoleUtility();
        for(YHFlowType ftTmp : typeList){
          //如果是自由流程，只判断新建权限，如果是固定流程需判断第一步的经办权限， 如果第一步经办权限没设或没有第一步，只不会显示          boolean flag = false;
          flag = tru.prcsRole(ftTmp, 0, user, dbConn);
          if(flag){
            sb.append("{");
            sb.append("flowName:'" + ftTmp.getFlowName() + "'");
            sb.append(",flowId:'" + ftTmp.getSeqId() +"'}," );
            count++;
          }
        }
      }
      long date2 = System.currentTimeMillis();
      long date3 = date2 - date1;
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
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
  public String getFlowBySortId(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String sortId = request.getParameter("sortId");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer();
      YHFlowSortLogic fs = new YHFlowSortLogic();
      YHPerson u = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      sb.append("[");
      YHFlowTypeLogic ft = new YHFlowTypeLogic();
      List<YHFlowType> typeList = ft.getFlowTypeList(Integer.parseInt(sortId) , dbConn);
      int count = 0 ;
      YHWorkFlowUtility w = new YHWorkFlowUtility();
      for(YHFlowType ftTmp : typeList){
        int deptId = ftTmp.getDeptId();
        if (!w.isHaveRight(deptId, u, dbConn)) {
          continue;
        }
        count ++;
        if(ftTmp.getFlowName() == null){
          sb.append("{title:' '");
        }else{ 
          sb.append("{title:'" + ftTmp.getFlowName() + "'");
        }
       // if(hasIcon){
      sb.append(",icon:imgPath + '/edit.gif'");
        //}
      sb.append(",action:actionFuntion");
      sb.append(",iconAction:iconActionFuntion");
      sb.append(",extData:'" + ftTmp.getSeqId() +":"+ ftTmp.getFlowType() +"'}," );
    }
    if(count > 0){
      sb.deleteCharAt(sb.length() - 1);
    }
      sb.append("]");
     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, sortId);
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 表单管理
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getFromSortList(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String idStr = request.getParameter("id");
    int id = 0;
    if(idStr!=null && !"".equals(idStr)){
      id = Integer.parseInt(idStr);
    }
    Statement stmt = null;
    ResultSet rs = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer();
      int count = 0;
      YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      sb.append("[");
      if (id != 0) {
        YHFlowSortLogic fs = new YHFlowSortLogic();
        int id2 = id;
        if (id2 == -1) {
          id2 = 0;
        } 
        String tb = fs.getSortJson(id2, dbConn , user , request.getContextPath());
        if (!"".equals(tb)) {
          count++;
        }
        sb.append(tb);
      }
      String queryStr = "select SEQ_ID , SORT_NAME , SORT_NO  from oa_fl_sort where SORT_PARENT =" + id ;
      if (!user.isAdminRole()) {
        String deptIds = user.getDeptId() + ",0";
        String deptOther = user.getDeptIdOther();
        if (!YHUtility.isNullorEmpty(deptOther) ) {
          deptIds = deptIds + "," + deptOther;
          deptIds = YHWorkFlowUtility.getOutOfTail(deptIds);
        }
        queryStr += " AND  DEPT_ID IN (" + deptIds + ") " ;
      }
      queryStr += " order by SORT_NO";
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while(rs.next()){
         int nodeId = rs.getInt("SEQ_ID");
         String name = rs.getString("SORT_NAME");
         sb.append("{");
         sb.append("nodeId:\"" + nodeId + "\"");
         sb.append(",name:\"" + name + "\"");
         sb.append(",isHaveChild:1");
         sb.append(",imgAddress:\""+ request.getContextPath() +"/core/funcs/workflow/flowtype/img/folder.gif\"");
         sb.append("},");
         count++;
      }
      if (id == 0) {
        sb.append("{");
        sb.append("nodeId:\"-1\"");
        sb.append(",name:\"未分类\"");
        sb.append(",isHaveChild:1");
        sb.append(",imgAddress:\""+ request.getContextPath() +"/core/funcs/workflow/flowtype/img/folder.gif\"");
        sb.append("},");
        count++;
      }
      if(count > 0){
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    } finally {
        YHDBUtility.close(stmt, rs, null);
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getSortId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String sortName = request.getParameter("sortName");
    String sql = "select seq_id from oa_fl_sort where sort_name = '" + sortName + "'"; 
    int result = 0;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = dbConn.createStatement();
        rs = stm.executeQuery(sql);
        if (rs.next()) {
          result = rs.getInt("seq_id");
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm , rs , null);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "");
      request.setAttribute(YHActionKeys.RET_DATA, "'" +result + "'");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    } 
    return "/core/inc/rtjson.jsp";
  }
  public String getSortIds(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String sortName = request.getParameter("sortName");
    String sortNamesNew = "";
    if (!YHUtility.isNullorEmpty(sortName)) {
      String[] news = sortName.split(",");
      for (String tmp : news) {
        if (!YHUtility.isNullorEmpty(tmp)) {
          sortNamesNew += "'" + tmp + "',";
        }
      }
    }
    if (sortNamesNew.endsWith(",")) {
      sortNamesNew = sortNamesNew.substring(0, sortNamesNew.length() - 1);
    }
    String result = "";
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      if (!"".equals(sortNamesNew)) {
        String sql = "select seq_id from oa_fl_sort where sort_name in (" + sortNamesNew + ")"; 
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = dbConn.createStatement();
          rs = stm.executeQuery(sql);
          while (rs.next()) {
            result += rs.getInt("seq_id") + ",";
          }
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm , rs , null);
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "");
      request.setAttribute(YHActionKeys.RET_DATA, "'" +result + "'");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    } 
    return "/core/inc/rtjson.jsp";
  }
  public boolean IsHaveChild(Connection conn , int nodeId , YHPerson user ,YHFlowTypeLogic ft ,  YHWorkFlowUtility w , String deptIds) throws Exception {
    List<YHFlowType> typeList = ft.getFlowTypeList(nodeId , conn);
    boolean flag = false;
    for(YHFlowType ftTmp : typeList){
      int deptId = ftTmp.getDeptId();
      if (w.isHaveRight(deptId, user, conn)) {
        flag = true;
        return flag;
      }
    }
    String queryStr2 = "select 1  from oa_fl_sort where SORT_PARENT =" + nodeId ;
    if (!user.isAdminRole()) {
      queryStr2 += " AND  DEPT_ID IN (" + deptIds + ") " ;
    }
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(queryStr2);
      if (rs.next()) {
        flag = true;
        return flag;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return flag;
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
  public String getDeptList(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHPerson u = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDeptLogic deptLogic = new YHDeptLogic();
      String deptList =  deptLogic.getDeptTreeJson(u , dbConn);
      request.setAttribute(YHActionKeys.RET_DATA, deptList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public YHFlowSort mapToFlowSort (Map mapListNext){
    YHFlowSort sort = new YHFlowSort();
    sort.setSeqId((Integer)mapListNext.get("seqId"));
    sort.setSortNo((Integer)mapListNext.get("sortNo"));
    sort.setSortName((String)mapListNext.get("sortName"));
    sort.setSortParent((Integer)mapListNext.get("sortParent"));
    sort.setHaveChild((String)mapListNext.get("haveChild"));
    sort.setDeptId((Integer)mapListNext.get("deptId"));
    return sort;
  }
}
