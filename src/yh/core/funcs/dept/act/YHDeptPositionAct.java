package yh.core.funcs.dept.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.data.YHDeptPosition;
import yh.core.funcs.dept.data.YHPositionPerson;
import yh.core.funcs.dept.data.YHPositionPriv;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.menu.data.YHSysMenu;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHDeptPositionAct{
  private static Logger log = Logger.getLogger(YHDeptPositionAct.class);

  public String insertDp(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptSeqId = request.getParameter("deptSeqId");
      String positionNo = request.getParameter("positionNo");
      //System.out.println(deptSeqId+"sssssssssss");
      YHDeptLogic dl = new YHDeptLogic();
      if(dl.existsDeptPosition(dbConn, positionNo)){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "岗位编码以存在，请重新填写！");
        return "/core/inc/rtjson.jsp";
      }else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功");
      }
      YHDeptPosition dp = (YHDeptPosition) YHFOM.build(request.getParameterMap());
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, dp);
//      dbConn.close();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String insertPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      String positionPriv = request.getParameter("positionPriv");
      int positionSeqId = Integer.parseInt(request.getParameter("positionSeqId"));
      //int userId = Integer.parseInt(request.getParameter("userId"));
      String sSeqId = request.getParameter("seqId");
      int seqId = 0;
      if(sSeqId != null && !"".equals(sSeqId)){
        seqId = Integer.parseInt(sSeqId);
      }
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPositionPriv priv = new YHPositionPriv(); 
      //(YHPositionPriv) YHFOM.build(request.getParameterMap());
      priv.setPositionPriv(positionPriv);
      priv.setPositionSeqId(positionSeqId);
      priv.setSeqId(seqId);
      YHORM orm = new YHORM();
      orm.deleteSingle(dbConn, priv);
      orm.saveSingle(dbConn, priv);
//      dbConn.close();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    }catch (Exception ex){
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
      int positionSeqId = Integer.parseInt(request.getParameter("treeId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("SEQ_ID", positionSeqId);
      Object obj = orm.loadObjSingle(dbConn, YHDeptPosition.class, map);
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
  
  public String getUserPosition(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int deptSeqId = Integer.parseInt(request.getParameter("deptSeqId"));
      String data = null;
      List<YHDeptPosition> userPositionList = null;
      StringBuffer sb = new StringBuffer("[");
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("DEPT_SEQ_ID", deptSeqId);
      userPositionList = orm.loadListSingle(dbConn, YHDeptPosition.class, map);
      if(userPositionList.size() > 0){
        for(int i = 0; i < userPositionList.size(); i++){
          YHDeptPosition userPriv = userPositionList.get(i);
          sb.append("{");
          sb.append("seqId:\"" + userPriv.getSeqId() + "\"");
          //sb.append("deptSeqId:\"" + userPriv.getDeptSeqId() + "\"");
          sb.append(",positionName:\"" + userPriv.getPositionName() + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        data = sb.toString();
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
        request.setAttribute(YHActionKeys.RET_DATA, data);
      }else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未定义岗位");
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  
  public String getPersonPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      int treeId = Integer.parseInt(request.getParameter("treeId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      List<YHPerson> personPrivList = null;
      StringBuffer sb = new StringBuffer("[");
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("DEPT_ID", treeId);
      personPrivList = orm.loadListSingle(dbConn, YHPerson.class, map);
      if(personPrivList.size() > 0) {
        for(int i = 0; i < personPrivList.size(); i++){
          YHPerson userPriv = personPrivList.get(i);
          sb.append("{");
          //sb.append("seqId:\"" + userPriv.getSeqId() + "\"");
          sb.append("seqId:\"" + userPriv.getSeqId() + "\"");
          sb.append(",userName:\"" + userPriv.getUserName() + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        data = sb.toString();
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
        request.setAttribute(YHActionKeys.RET_DATA, data);
      }else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未定义岗位权限");
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  
  public String getPerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      List<YHDepartment> personList = null;
      StringBuffer sb = new StringBuffer("[");
      YHORM orm = new YHORM();
      personList = orm.loadListSingle(dbConn, YHDepartment.class, new HashMap());
      for(int i = 0; i < personList.size(); i++) {
        YHDepartment userPriv = personList.get(i);
        sb.append("{");
        //sb.append("seqId:\"" + userPriv.getSeqId() + "\"");
        sb.append("seqId:\"" + userPriv.getSeqId() + "\"");
        sb.append(",deptName:\"" + userPriv.getDeptName() + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append("]");
      data = sb.toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 

  public String getTree(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
    Connection dbConn = null;
    try{
      int menuId = Integer.parseInt(request.getParameter("menuId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("POSITION_SEQ_ID", menuId);
      Object obj = orm.loadObjSingle(dbConn, YHPositionPriv.class, map);
      data = YHFOM.toJson(obj).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateDp(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      String positionFlag = request.getParameter("positionFlag");
      int treeId = Integer.parseInt(request.getParameter("treeId"));
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String positionNo = request.getParameter("positionNo");
      String positionNoOld = request.getParameter("positionNoOld");
      YHDeptLogic dl = new YHDeptLogic();
      YHDeptPosition dpt = (YHDeptPosition) YHFOM.build(request.getParameterMap());
      //dpt.setDeptSeqId(treeId);
      dpt.setSeqId(seqId);
      String data = null;
      YHORM orm = new YHORM();
      if((positionNoOld).equals(positionNo)){
        orm.updateSingle(dbConn, dpt);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功更改数据库的数据");
      }else{
        if(dl.existsDeptPosition(dbConn, positionNo)){
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "岗位编码以存在，请重新填写！");
          return "/core/inc/rtjson.jsp";
        }else{
          orm.updateSingle(dbConn, dpt);
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "成功更改数据库的数据");
        }
      }
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deletePosition(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      YHDeptPosition dt = (YHDeptPosition)orm.loadObjComplex(dbConn, YHDeptPosition.class, seqId);
      //YHDeptPosition dpt = (YHDeptPosition) YHFOM.build(request.getParameterMap());
      dt.setSeqId(seqId);
      orm.deleteComplex(dbConn, dt);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功删除数据库的数据");
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
//  public int IsHaveChild(HttpServletRequest request,
//      HttpServletResponse response,String id) throws Exception {
//    Connection dbConn = null;
//    try {
//      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
//          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
//      dbConn = requestDbConn.getSysDbConn();
//      YHORM orm = new YHORM();
//      Map map = new HashMap();
//      map.put("deptParent", id);
//      List<YHDeptPosition> list = orm.loadListSingle(dbConn, YHDeptPosition.class, map);
//      if(list.size()!=-1){
//        return 1;
//      }else{
//        return 0;
//      }
//    } catch (Exception ex) {
//      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
//      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
//      throw ex;
//    }
//  }
  
  public String selectDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String treeId = request.getParameter("treeId");
    String TO_ID = request.getParameter("TO_ID");
    String TO_NAME = request.getParameter("TO_NAME");
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
      personList = (ArrayList<YHPerson>)orm.loadListSingle(dbConn, YHPerson.class, map);
      request.setAttribute("personList", personList);
//      dbConn.close();
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    return "/core/funcs/dept/user.jsp?TO_ID="+TO_ID+"&TO_NAME="+TO_NAME+"&deptLocalg="+deptLocal;
  }
  
  public String getNoTreeOnce(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int userId = Integer.parseInt(request.getParameter("userId"));
      String sPriv = getPrivString(dbConn,userId);
      //"10,1077,107710,107733";
      String[] privArray = sPriv.split(",");
      YHORM orm = new YHORM();
      Map map = new HashMap();
      StringBuffer sb = new StringBuffer("[");
      ArrayList<YHSysMenu> menuList = null;
      Map m1 = null;
      menuList = (ArrayList<YHSysMenu>)orm.loadListSingle(dbConn, YHSysMenu.class, m1);
      for(int i = 0; i < menuList.size(); i++){
        YHSysMenu menu = menuList.get(i);
        sb.append("{");
        sb.append("nodeId:\"" + menu.getMenuId() + "\"");
        sb.append(",name:\"" + menu.getMenuName() + "\"");
        sb.append(",isHaveChild:" + 1);
        sb.append(",isChecked:" + isChecked(privArray,(String) menu.getMenuId()));
        sb.append(",imgAddress:\"" + request.getContextPath() + "/core/styles/imgs/" + menu.getImage() + "\"");
        sb.append("},");
      }       
      List funcList = new ArrayList();
      funcList.add("sysFunction");
      String[] filters = new String[]{};
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      List<Map> list = (List<Map>) map.get("OA_SYS_FUNC");
      for(Map m : list){
        sb.append("{");
        sb.append("nodeId:\"" + m.get("menuId") + "\"");
        sb.append(",name:\"" + m.get("funcName") + "\"");
        sb.append(",isChecked:" + isChecked(privArray,(String) m.get("menuId")));
        sb.append(",isHaveChild:" + IsHaveChild(request, response, String.valueOf(m.get("menuId"))));
        sb.append(",imgAddress:\"" + request.getContextPath() + "/core/styles/imgs/" + m.get("funcCode") + "\"");
        sb.append("},");
      } 
      sb.deleteCharAt(sb.length() - 1);       
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getSelestData(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //int userId = Integer.parseInt(request.getParameter("userId"));
      YHORM orm = new YHORM();
      Map map = new HashMap();
      StringBuffer sb = new StringBuffer("[");
      ArrayList<YHPerson> perList = null;
      Map m = null;
      perList = (ArrayList<YHPerson>)orm.loadListSingle(dbConn, YHPerson.class, m);
      for(int i = 0; i < perList.size(); i++){
        YHPerson menu = perList.get(i);
        sb.append("{");
        sb.append("value:\"" + menu.getSeqId() + "\"");
        sb.append(",name:\"" + menu.getUserName() + "\"");
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
  
  public String getDeptSelect(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
    Connection dbConn = null;
    try{
      int positionSeqId = Integer.parseInt(request.getParameter("treeId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("POSITION_SEQ_ID", positionSeqId);
      Object obj = orm.loadObjSingle(dbConn, YHPositionPerson.class, map);
      data = YHFOM.toJson(obj).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getPrivTreeData(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
    Connection dbConn = null;
    try{
      int positionSeqId = Integer.parseInt(request.getParameter("userId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("POSITION_SEQ_ID", positionSeqId);
      Object obj = orm.loadObjSingle(dbConn, YHPositionPriv.class, map);
      data = YHFOM.toJson(obj).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updatePersonDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      int personSeqId = Integer.parseInt(request.getParameter("personSeqId"));
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptNo = request.getParameter("deptNo");
      String deptNoOld = request.getParameter("deptNoOld");
      YHPositionPerson dpt = (YHPositionPerson) YHFOM.build(request.getParameterMap());
      YHORM orm = new YHORM();
      if(seqId == 0){
        dpt.setSeqId(personSeqId);
        orm.saveSingle(dbConn, dpt);
      }else{
        dpt.setSeqId(seqId);
        orm.deleteSingle(dbConn, dpt);
        orm.saveSingle(dbConn, dpt);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功更改数据库的数据");
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getComPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      int userId = Integer.parseInt(request.getParameter("userId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map map = new HashMap();
      String data = null;
      List<YHPositionPerson> userPrivList = null;
      List<YHPositionPriv> positionPrivList = null;
      List funcList = new ArrayList();
      YHORM orm = new YHORM();
      //String[] filters = new String[]{"POSITION_USERS = '" + userId + "'"};
      //map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      map.put("POSITION_USERS", userId);
      userPrivList = orm.loadListSingle(dbConn, YHPositionPerson.class, map);
      int positionSeqId = 0;
      String positionPriv = "";
      for(int i = 0; i < userPrivList.size(); i++){
        YHPositionPerson userPriv = userPrivList.get(i);
        int seqId = userPriv.getSeqId();
        positionSeqId = userPriv.getPositionSeqId();
        // String positionUsers = userPriv.getPositionUsers();
        map.put("POSITION_SEQ_ID", positionSeqId);
        positionPrivList = orm.loadListSingle(dbConn, YHPositionPriv.class, map);
        for(int j = 0; j < userPrivList.size(); j++){
          YHPositionPriv poPriv = positionPrivList.get(i);
          int positionSeqIDD = poPriv.getPositionSeqId();
          positionPriv +=  poPriv.getPositionPriv() + ",";
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
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
      List funcList = new ArrayList();
      funcList.add("sysFunction");
      String[] filters = new String[]{"MENU_ID like '" + id + "%'"," length(MENU_ID) > " + id.length() + ""};
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      //map.put("menuId", id);
      List<Map> list = (List<Map>) map.get("OA_SYS_FUNC");
      //List<YHSysFunction> list = orm.loadListSingle(dbConn, YHSysFunction.class, map);
      if(list.size() > 0){
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
  
  public String getPrivString(Connection dbConn, int userId) throws Exception{
    //String data = null;
    List<YHPositionPerson> userPrivList = null;
    List<YHPositionPriv> positionPrivList = null;
    List funcList = new ArrayList();
    YHORM orm = new YHORM();
    //String[] filters = new String[]{"POSITION_USERS = '" + userId + "'"};
    //map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
    //Map map = new HashMap();
    //map.put("POSITION_USERS", String.valueOf(userId));
    //userPrivList = orm.loadListSingle(dbConn, YHPositionPerson.class, map);
    //int positionSeqId=0;
    String positionPriv = "";
    //for(int i = 0; i < userPrivList.size(); i++) {
    //YHPositionPerson userPriv = userPrivList.get(i);
    //int seqId = userPriv.getSeqId();
    //positionSeqId = userPriv.getPositionSeqId();
    //String positionUsers = userPriv.getPositionUsers();
    Map map1 = new HashMap();
    map1.put("POSITION_SEQ_ID", userId);
    positionPrivList = orm.loadListSingle(dbConn, YHPositionPriv.class, map1);
    for(int j = 0; j < positionPrivList.size(); j++){
      YHPositionPriv poPriv = positionPrivList.get(j);
      int positionSeqIDD = poPriv.getPositionSeqId();
     //positionPriv +=  poPriv.getPositionPriv() + ",";
      positionPriv =  poPriv.getPositionPriv();
      if(positionPriv == null){
        positionPriv = "";
      }
    }
    //}
    return positionPriv;
  }
  
  public boolean isChecked(String[] privArray,String id){
    for(int i = 0 ;i < privArray.length; i++){
      if(id.equals(privArray[i])){
        return true;
      }
    }
    return false;
  }
}