package yh.core.funcs.person.act;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.psit.struts.entity.CrmManagementArea;
import com.psit.struts.entity.CusArea;
import com.psit.struts.entity.CusCity;
import com.psit.struts.entity.CusProvince;
import com.psit.struts.util.JSONConvert;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.menu.data.YHSysMenu;
import yh.core.funcs.notify.data.YHNotify;
import yh.core.funcs.org.data.YHOrganization;
import yh.core.funcs.orgselect.logic.YHDeptSelectLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.funcs.seclog.logic.YHSecLogUtil;
import yh.core.funcs.system.ispirit.n12.org.act.YHIsPiritOrgAct;
import yh.core.funcs.workflow.util.YHFlowFormLogicUtil;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.global.YHSysProps;
import yh.core.module.org_select.logic.YHOrgSelect2Logic;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.abroad.data.YHHrAbroadRecord;
import yh.subsys.oa.fillRegister.logic.YHAttendFillLogic;

public class YHUserPrivAct {
  private static Logger log = Logger.getLogger(YHUserPrivAct.class);

  public String insertUserPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      // if(dl.existsTableNo(dbConn, privNo)){
      // request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      // request.setAttribute(YHActionKeys.RET_MSRG, "角色排序号以存在，请重新填写！");
      // return "/core/inc/rtjson.jsp";
      // }else{
      // request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      // request.setAttribute(YHActionKeys.RET_MSRG, "添加成功");
      // }
      YHUserPriv dpt = (YHUserPriv) YHFOM.build(request.getParameterMap());
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, dpt);
      userPrivNum(request, response);
      
      
      // add  seclog
      try{
      YHSecLogUtil.log(dbConn, person, request.getRemoteAddr(), "215", dpt,"1","新建角色，名称为'" + dpt.getPrivName() + "'");
      }catch(Exception e){}
      
      
      //dbConn.close();
      //request.setAttribute("desc", deptParentDesc);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
//      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/funcs/dept/deptinput.jsp";
    //?deptParentDesc=+deptParentDesc
    return "/core/inc/rtjson.jsp";
  }
  
  public String insertPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHPerson login = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      String funcIdStr = request.getParameter("funcIdStr");
      String sSeqId = request.getParameter("seqId");
      int seqId = 0;
      if(sSeqId != null && !"".equals(sSeqId)){
        seqId = Integer.parseInt(sSeqId);
      }
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      YHUserPriv dpt = (YHUserPriv) orm.loadObjSingle(dbConn, YHUserPriv.class, seqId);
      
      YHUserPrivLogic privLogic = new YHUserPrivLogic();
      String menuNameStr = privLogic.selectMemuNameByMenuId(dbConn,funcIdStr);
      String menuFunNameStr = privLogic.getChildMenuName(dbConn,funcIdStr);
      
      String oldMenuId = dpt.getFuncIdStr();
      String oldMenuNameStr = privLogic.selectMemuNameByMenuId(dbConn,oldMenuId);
      String oldMenuFunNameStr = privLogic.getChildMenuName(dbConn,oldMenuId);
      String oldFuncId  = dpt.getFuncIdStr();
      dpt.setSeqId(seqId);
      dpt.setFuncIdStr(funcIdStr);
      orm.updateSingle(dbConn, dpt);
      
      // add  seclog
      try{
      YHSecLogUtil.log(dbConn, login, request.getRemoteAddr(), "215", dpt,"1", "角色 \"" +dpt.getPrivName() + "\"修改菜单权限:\"" + oldMenuNameStr +","+oldMenuFunNameStr + "\"  改为  \"" + menuNameStr +"," + menuFunNameStr + "\"");
      }catch(Exception e){}
      //dbConn.close();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
      
      //生成org.xml文件
      YHIsPiritOrgAct.getOrgDataStream(dbConn);
      
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getUserPriv(HttpServletRequest request, HttpServletResponse response)
      throws Exception{
    Connection dbConn = null;
    try{
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      YHORM orm = new YHORM();
      Object obj = orm.loadObjSingle(dbConn, YHUserPriv.class, seqId);
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

  public String updateUserPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHPerson login = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHUserPriv dpt = (YHUserPriv) YHFOM.build(request.getParameterMap());
      dpt.setSeqId(seqId);
      YHUserPrivLogic logic = new YHUserPrivLogic();
      YHUserPriv userPriv = logic.getRoleById(seqId, dbConn);
      YHORM orm = new YHORM();
      //if((privNoOld).equals(privNo)){
      orm.updateSingle(dbConn, dpt);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功更改数据库的数据");
      // add  seclog
      try{
        //系统安全日志
      String oldPrivName = "";
      if(userPriv != null ){
        oldPrivName = userPriv.getPrivName();
      }
      YHSecLogUtil.log(dbConn, login, request.getRemoteAddr(), "215",dpt,"1", "将角色名称为'" + oldPrivName + "' 修改成'" + dpt.getPrivName() + "'");
      }catch(Exception e){}
      
      //}else{
       // if(userpriv.existsTableNo(dbConn, privNo)){
       //   request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
       //   request.setAttribute(YHActionKeys.RET_MSRG, "角色排序号以存在，请重新填写！");
       //   return "/core/inc/rtjson.jsp";
       // }else{
       //   orm.updateSingle(dbConn, dpt);
       //request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
       //request.setAttribute(YHActionKeys.RET_MSRG, "成功更改数据库的数据");
       // }
       // }
      
      //生成org.xml文件
      YHIsPiritOrgAct.getOrgDataStream(dbConn);
      
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteUserPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHORM orm = new YHORM();
      YHUserPriv dt = (YHUserPriv)orm.loadObjSingle(dbConn, YHUserPriv.class, seqId);
      //YHDeptPosition dpt = (YHDeptPosition) YHFOM.build(request.getParameterMap());
      dt.setSeqId(seqId);
      orm.deleteComplex(dbConn, dt);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功删除数据库的数据");
      
      //生成org.xml文件
      YHIsPiritOrgAct.getOrgDataStream(dbConn);
      
      // add  seclog
      try{
      YHSecLogUtil.log(dbConn, person, request.getRemoteAddr(), "215", dt,"1", "删除角色名称为'" + dt.getPrivName() + "'");
      }catch(Exception e){}
      
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String userPrivNum(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHUserPrivLogic userPrivLogic = new YHUserPrivLogic(); 
      for(int i = 0; i < userPrivLogic.selectUserPriv(dbConn).size(); i++){
        YHUserPriv up = (YHUserPriv) userPrivLogic.selectUserPriv(dbConn).get(i);
        //YHPerson upp = (YHPerson) userPrivLogic.selectPerson(dbConn,up.getPrivNo());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功删除数据库的数据");
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
    if(idStr != null && !"".equals(idStr)){
      id = Integer.parseInt(idStr);
    }
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/xml");
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Cache-Control","maxage=3600");
    response.setHeader("Pragma","public");
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
      List funcList = new ArrayList();
      funcList.add("sysFunction");
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      String length = null;
      
      if (dbms.equals("sqlserver")) {
        length = "len";
      }else if (dbms.equals("mysql")) {
        length = "length";
      }else if (dbms.equals("oracle")) {
        length = "length";
      }else {
        throw new SQLException("not accepted dbms");
      }
      
      String[] filters = new String[]{"MENU_ID like '" + id + "%'"," " + length + "(MENU_ID) > " + id.length() + ""};
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
  
  /**
   * 是否有子的区域  省份
   * 
   * @param request
   * @param response
   * @param id
   * @return
   * @throws Exception
   */
  public int IsHaveChildProvince(HttpServletRequest request,
		  HttpServletResponse response,String id) throws Exception{
	  Connection dbConn = null;
	  try{
		  YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		  .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  dbConn = requestDbConn.getSysDbConn();
		  YHORM orm = new YHORM();
		  Map map = new HashMap();
          List cusProvinceList = new ArrayList();
		  
		  YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
		  cusProvinceList = userPrivLogic.selectUserManageProvinces(dbConn, Integer.parseInt(id));
		  if(cusProvinceList.size() > 0){
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
  
  /**
   * 是否有子的区域  城市
   * 
   * @param request
   * @param response
   * @param id
   * @return
   * @throws Exception
   */
  public int IsHaveChildCity(HttpServletRequest request,
		  HttpServletResponse response,String id) throws Exception{
	  Connection dbConn = null;
	  try{
		  YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		  .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  dbConn = requestDbConn.getSysDbConn();
		  YHORM orm = new YHORM();
		  Map map = new HashMap();
		  List cusCityList = new ArrayList();
		  
		  YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
		  cusCityList = userPrivLogic.selectUserManageCitys(dbConn,Integer.parseInt(id));
		  if(cusCityList.size() > 0){
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
      String[] filterMenuId = new String[]{" 1=1 order by MENU_ID"};
      menuList = (ArrayList<YHSysMenu>)orm.loadListSingle(dbConn, YHSysMenu.class, filterMenuId);
      for(int i = 0; i < menuList.size(); i++){
        YHSysMenu menu = menuList.get(i);
        sb.append("{");
        sb.append("nodeId:\"" + menu.getMenuId() + "\"");
        sb.append(",name:\"" + menu.getMenuName() + "\"");
        sb.append(",isHaveChild:" + IsHaveChild(request, response, String.valueOf(menu.getMenuId())));
        sb.append(",isChecked:" + isChecked(privArray,(String) menu.getMenuId()));
        sb.append(",imgAddress:\"" + request.getContextPath() + "/core/styles/imgs/menuIcon/" + menu.getImage() + "\"");
        sb.append("},");
      }       
      List funcList = new ArrayList();
      funcList.add("sysFunction");
      String[] filters = new String[]{};
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      List<Map> list = (List<Map>) map.get("OA_SYS_FUNC");
      String contextPath = request.getContextPath();
      for(Map m : list){
        String funcAddress = null;
        String imageAddress = null;
        String fun = (String) m.get("funcCode");
        if (fun == null) {
          continue;
        }
        imageAddress = contextPath + "/core/funcs/display/img/org.gif";
        if (fun.startsWith("/")) {
          funcAddress = contextPath + fun;
        }else {
          funcAddress = contextPath + "/core/funcs/" + fun + "/";
        }
        sb.append("{");
        sb.append("nodeId:\"" + m.get("menuId") + "\"");
        sb.append(",name:\"" + m.get("funcName") + "\"");
        sb.append(",isChecked:" + isChecked(privArray,(String) m.get("menuId")));
        sb.append(",isHaveChild:" + IsHaveChild(request, response, String.valueOf(m.get("menuId"))));
        sb.append(",imgAddress:\"" + imageAddress + "\"");
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
  
  
  
  /**
   * 判断当前登录人是否有管理该区域的权限
   * 
   * @param request
   * 
   * @param response
   * 
   * @return 返回拼接的字符串
   * 
   * @throws Exception
   */
  
  public String isOrNotManageAreaPriv(HttpServletRequest request,
		  HttpServletResponse response) throws Exception {
	  Connection dbConn = null;
	  try {
		  YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		  .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  dbConn = requestDbConn.getSysDbConn();
		  YHPerson loginPerson = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		  
		  //获取当前登录人所有的管辖区域
		  List<Map<String,String>> manageAreaList = getManageAreasByUserId(dbConn,String.valueOf(loginPerson.getSeqId()));
		  
		  String areaId = request.getParameter("areaId"); //当前登录人  选择的区域标识
		  
		  int countryIndex = -1;
		  int provinceIndex = -1;
		  int cityIndex = -1;
		  if(areaId != null && !"".equals(areaId)){
			  countryIndex = areaId.indexOf("c");   //查询国家的下标
			  provinceIndex = areaId.indexOf("p");  //查询省份的下标
			  cityIndex = areaId.indexOf("t");      //查询城市的下标
		  }
		  
		  boolean isOrNotPriv = false; //是否有权限访问该菜单
		  
		  boolean isOrNotCountry_priv = false; //是否有负责该国家的权限
		  boolean isOrNotProvince_priv = false; //是否有负责该国家下的某个省份的权限
		  boolean isOrNotCity_priv = false; //是否有负责该国家下的某个省份下的某个城市的权限
		  if(countryIndex > -1 && provinceIndex == -1 && cityIndex == -1){ //选中了最大的范围  例如：中国
				String country_id = areaId.substring(countryIndex+1, areaId.length());
				isOrNotCountry_priv = isChecked(manageAreaList,country_id,"country");
				if(isOrNotCountry_priv){
					isOrNotPriv = true;
				}
		  }else if(countryIndex > -1 && provinceIndex > -1 && cityIndex == -1){ //选中了最大的范围  例如：中国下的江苏省
			  String country_id = areaId.substring(countryIndex+1, provinceIndex);
			  String province_id = areaId.substring(provinceIndex+1, areaId.length());
			  isOrNotCountry_priv = isChecked(manageAreaList,country_id,"country");
			  isOrNotProvince_priv = isChecked(manageAreaList,province_id,"province");
			  
			  if(isOrNotCountry_priv && isOrNotProvince_priv){
					isOrNotPriv = true;
			  }
		  }else{ //选中了最大的范围  例如：中国下的江苏省下的南京市
			  String country_id = areaId.substring(countryIndex+1, provinceIndex);
			  String province_id = areaId.substring(provinceIndex+1, cityIndex);
			  String city_id = areaId.substring(cityIndex+1);
			  isOrNotCountry_priv = isChecked(manageAreaList,country_id,"country");
			  isOrNotProvince_priv = isChecked(manageAreaList,province_id,"province");
			  isOrNotCity_priv = isChecked(manageAreaList,city_id,"city");
			  
			  if(isOrNotCountry_priv && isOrNotProvince_priv && isOrNotCity_priv){
					isOrNotPriv = true;
			  }
		  }
			
		  StringBuffer sb = new StringBuffer("[");
		  sb.append("{");
		  sb.append("isOrNotPriv:" + isOrNotPriv); //是否有权限 返回 true 返回 false
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
  
  
  private List<Map<String,String>> getManageAreasByUserId(Connection dbConn,String userId)throws Exception{
	  List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	  String sql = "select r.country_id,r.province_id,r.city_id from crm_management_area a,crm_management_area_range r where a.id=r.ma_id and a.person_id='"+userId+"'";
	  Statement st = dbConn.createStatement();
	  ResultSet rs = st.executeQuery(sql);
	  while(rs.next()){
		  Map<String,String> map = new HashMap<String,String>();
		  map.put("country_id", rs.getString("country_id"));
		  map.put("province_id", rs.getString("province_id"));
		  map.put("city_id", rs.getString("city_id"));
		  list.add(map);
	  }
	  return list;
  }
  private boolean isChecked(List<Map<String,String>> list,String value,String flag){
	  if(list !=null && list.size() >0){
		  for(int i=0;i<list.size();i++){
			  Map<String,String> map = list.get(i);
			  if("country".equals(flag)){
				  if(map.get("country_id").equals(value)){
					  return true;
				  }
			  }
			  if("province".equals(flag)){
				  if(map.get("province_id").equals(value)){
					  return true;
				  }
			  }
			  if("city".equals(flag)){
				  if(map.get("city_id").equals(value)){
					  return true;
				  }
			  }
		  }
	  }
	  return false;
  }
  /**
   * 获取人员的管辖区域
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getManageAreas(HttpServletRequest request,
		  HttpServletResponse response) throws Exception{
	  Connection dbConn = null;
	  try{
		  YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  dbConn = requestDbConn.getSysDbConn();
//		  String nodeId = request.getParameter("userId");
//		  int userId = Integer.parseInt(nodeId);;
//		  if(nodeId.indexOf("c") ==0){
//			  //该节点是国家
//			  userId = Integer.parseInt(nodeId.substring(0));
//		  }
//		  else if(nodeId.indexOf("p") == 0){
//			//该节点是省份
//			  userId = Integer.parseInt(nodeId.substring(0));
//		  }
//		  else if(nodeId.indexOf("t") == 0){
//				//该节点是城市
//				  userId = Integer.parseInt(nodeId.substring(0));
//			  }
		   
		  
//		  String sPriv = getPrivString(dbConn,userId);
		  
		  //"10,1077,107710,107733";
//		  String[] privArray = sPriv.split(",");
		  YHORM orm = new YHORM();
		  Map map = new HashMap();
		  StringBuffer sb = new StringBuffer("[");
		  List<CusArea> cusAreaList = null;
		  List<CusProvince> cusProvinceList = null;
		  List<CusCity> cusCityList = null;
		  ArrayList<CrmManagementArea> menuList = null;
		  String[] filterAreaId = new String[]{" 1=1 order by are_id"};
		  String[] filterProvinceId = new String[]{" 1=1 order by prv_id"};
		  String[] filterCityId = new String[]{" 1=1 order by city_id"};
		  String[] filterMenuId = new String[]{" 1=1 order by Id"};
		  YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
		  cusAreaList = userPrivLogic.selectUserManageAreas(dbConn);
		 if(cusAreaList != null && cusAreaList.size() > 0){
			 for(int i = 0; i < cusAreaList.size(); i++){
				  CusArea menuArea = cusAreaList.get(i);
				  if(!menuArea.getAreName().equals("请选择")){
					  sb.append("{");
					  String countryId= String.valueOf(menuArea.getAreId());
					  if(menuArea.getAreId() < 10){
						  countryId = "0"+countryId;
					  }
					  sb.append("nodeId:\"" + String.valueOf(countryId) + "\"");
					  sb.append(",name:\"" + menuArea.getAreName() + "\"");
					  sb.append(",isHaveChild:" + IsHaveChildProvince(request, response, String.valueOf(menuArea.getAreId())));
//					  sb.append(",isChecked:" + isChecked(privArray,String.valueOf(menuArea.getAreId())));
					  sb.append(",imgAddress:\"" + request.getContextPath() + "/core/funcs/display/img/org.gif\"");
					  sb.append("},");
					  if(IsHaveChildProvince(request, response, String.valueOf(menuArea.getAreId())) == 1){
						  cusProvinceList = userPrivLogic.selectUserManageProvinces(dbConn,menuArea.getAreId());
						  if(cusProvinceList!=null && cusProvinceList.size()>0){
							  for (int j = 0; j < cusProvinceList.size(); j++) {
								      CusProvince cusProvince= cusProvinceList.get(j);
								      String contextPath = request.getContextPath();
									  String imageAddress = null;
									  if (cusProvince == null) {
										  continue;
									  }
									  imageAddress = contextPath + "/core/funcs/display/img/org.gif";
									  sb.append("{");
									  String provinceId = String.valueOf(cusProvince.getPrvId());
									  if(cusProvince.getPrvId() < 10){
										  provinceId = "0"+provinceId;
									  }
									  sb.append("nodeId:\""+ countryId + provinceId + "\"");
									  sb.append(",name:\"" + cusProvince.getPrvName() + "\"");
									  if(cusProvince.getPrvName().equals("北京市")){
										  System.out.println();
									  }
//									  sb.append(",isChecked:" + isChecked(privArray,String.valueOf(cusProvince.getPrvId())));
									  sb.append(",isHaveChild:" + IsHaveChildCity(request, response, String.valueOf(cusProvince.getPrvId())));
									  sb.append(",imgAddress:\"" + imageAddress + "\"");
									  sb.append("},");
									  if(IsHaveChildCity(request, response, String.valueOf(cusProvince.getPrvId())) == 1){
										  cusCityList = userPrivLogic.selectUserManageCitys(dbConn,cusProvince.getPrvId());
										  if(cusCityList!=null && cusCityList.size()>0){
											  for (int k = 0; k < cusCityList.size(); k++) {
												      CusCity cusCity= cusCityList.get(k);
													  if (cusCity == null) {
														  continue;
													  }
													  imageAddress = contextPath + "/core/funcs/display/img/org.gif";
													  sb.append("{");
													  String cityId = String.valueOf(cusCity.getCityId());
													  if(cusCity.getCityId() < 10){
														  cityId = "0"+cityId;
													  }
													  sb.append("nodeId:\""+countryId+provinceId+cityId +"\"");
													  sb.append(",name:\"" + cusCity.getCityName() + "\"");
//													  sb.append(",isChecked:" + isChecked(privArray,String.valueOf(cusCity.getCityId())));
													  sb.append(",isHaveChild:0");
													  sb.append(",imgAddress:\"" + imageAddress + "\"");
													  sb.append("},");
											}
										  }
									  }
							}
						  }
					  }
				  }
			  } 
		 }       
//		  List funcList = new ArrayList();
//		  funcList.add("sysFunction");
//		  String[] filters = new String[]{};
//		  map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
//		  List<Map> list = (List<Map>) map.get("OA_SYS_FUNC");
//		  String contextPath = request.getContextPath();
//		  for(Map m : list){
//			  String funcAddress = null;
//			  String imageAddress = null;
//			  String fun = (String) m.get("funcCode");
//			  if (fun == null) {
//				  continue;
//			  }
//			  imageAddress = contextPath + "/core/funcs/display/img/org.gif";
//			  if (fun.startsWith("/")) {
//				  funcAddress = contextPath + fun;
//			  }else {
//				  funcAddress = contextPath + "/core/funcs/" + fun + "/";
//			  }
//			  sb.append("{");
//			  sb.append("nodeId:\"" + m.get("menuId") + "\"");
//			  sb.append(",name:\"" + m.get("funcName") + "\"");
//			  sb.append(",isChecked:" + isChecked(privArray,(String) m.get("menuId")));
//			  sb.append(",isHaveChild:" + IsHaveChild(request, response, String.valueOf(m.get("menuId"))));
//			  sb.append(",imgAddress:\"" + imageAddress + "\"");
//			  sb.append("},");
//		  } 
		  sb.deleteCharAt(sb.length() - 1);       
		  sb.append("]");
		  System.out.println(sb);
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
  /**
   * 获取人员的管辖区域
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getManageAreas1(HttpServletRequest request,
		  HttpServletResponse response) throws Exception{
	  Connection dbConn = null;
	  try{
		  YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  dbConn = requestDbConn.getSysDbConn();
		  String userId = request.getParameter("userId");
		  List<Map<String,String>> manageAreaList = getManageAreasByUserId(dbConn,userId);
		  
		  YHORM orm = new YHORM();
		  Map map = new HashMap();
		  StringBuffer sb = new StringBuffer("[");
		  List<CusArea> cusAreaList = null;
		  List<CusProvince> cusProvinceList = null;
		  List<CusCity> cusCityList = null;
		  ArrayList<CrmManagementArea> menuList = null;
		  String[] filterAreaId = new String[]{" 1=1 order by are_id"};
		  String[] filterProvinceId = new String[]{" 1=1 order by prv_id"};
		  String[] filterCityId = new String[]{" 1=1 order by city_id"};
		  String[] filterMenuId = new String[]{" 1=1 order by Id"};
		  YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
		  cusAreaList = userPrivLogic.selectUserManageAreas(dbConn);
		  if(cusAreaList != null && cusAreaList.size() > 0){
			  for(int i = 0; i < cusAreaList.size(); i++){
				  CusArea menuArea = cusAreaList.get(i);
				  if(!menuArea.getAreName().equals("请选择")){
					  sb.append("{");
					  sb.append("nodeId:\"c" + menuArea.getAreId() + "\"");
					  sb.append(",name:\"" + menuArea.getAreName() + "\"");
					  sb.append(",parentId:\"-1\"");
					  sb.append(",isHaveChild:" + IsHaveChildProvince(request, response, String.valueOf(menuArea.getAreId())));
					  sb.append(",isChecked:" + isChecked(manageAreaList,String.valueOf(menuArea.getAreId()),"country"));
					  sb.append(",imgAddress:\"" + request.getContextPath() + "/core/funcs/display/img/org.gif\"");
					  sb.append("},");
					  if(IsHaveChildProvince(request, response, String.valueOf(menuArea.getAreId())) == 1){
						  cusProvinceList = userPrivLogic.selectUserManageProvinces(dbConn,menuArea.getAreId());
						  if(cusProvinceList!=null && cusProvinceList.size()>0){
							  for (int j = 0; j < cusProvinceList.size(); j++) {
								  CusProvince cusProvince= cusProvinceList.get(j);
								  String contextPath = request.getContextPath();
								  String imageAddress = null;
								  if (cusProvince == null) {
									  continue;
								  }
								  imageAddress = contextPath + "/core/funcs/display/img/org.gif";
								  sb.append("{");
								  sb.append("nodeId:\"c" + menuArea.getAreId() + "p"+ cusProvince.getPrvId() + "\"");
								  sb.append(",name:\"" + cusProvince.getPrvName() + "\"");
								  sb.append(",parentId:\"c" + menuArea.getAreId() + "\"");
								  sb.append(",isChecked:" + isChecked(manageAreaList,String.valueOf(cusProvince.getPrvId()),"province"));
								  sb.append(",isHaveChild:" + IsHaveChildCity(request, response, String.valueOf(cusProvince.getPrvId())));
								  sb.append(",imgAddress:\"" + imageAddress + "\"");
								  sb.append("},");
								  if(IsHaveChildCity(request, response, String.valueOf(cusProvince.getPrvId())) == 1){
									  cusCityList = userPrivLogic.selectUserManageCitys(dbConn,cusProvince.getPrvId());
									  if(cusCityList!=null && cusCityList.size()>0){
										  for (int k = 0; k < cusCityList.size(); k++) {
											  CusCity cusCity= cusCityList.get(k);
											  if (cusCity == null) {
												  continue;
											  }
											  imageAddress = contextPath + "/core/funcs/display/img/org.gif";
											  sb.append("{");
											  sb.append("nodeId:\"c" + menuArea.getAreId() + "p"+ cusProvince.getPrvId() + "t"+cusCity.getCityId() +"\"");
											  sb.append(",name:\"" + cusCity.getCityName() + "\"");
											  sb.append(",parentId:\"c" + menuArea.getAreId() + "p"+ cusProvince.getPrvId() +"\"");
											  sb.append(",isChecked:" + isChecked(manageAreaList,String.valueOf(cusCity.getCityId()),"city"));
											  sb.append(",isHaveChild:0");
											  sb.append(",imgAddress:\"" + imageAddress + "\"");
											  sb.append("},");
										  }
									  }
								  }
							  }
						  }
					  }
				  }
			  } 
		  }       
		  sb.deleteCharAt(sb.length() - 1);       
		  sb.append("]");
			PrintWriter out = null;
			try {
				out = response.getWriter();
				out.print(sb);
				out.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				out.close();
			}
	  }catch(Exception ex){
		  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		  request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		  throw ex;
	  }
	  return null;
  }
  /**
   * 获取区域树
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAllAreas(HttpServletRequest request,
		  HttpServletResponse response) throws Exception{
	  Connection dbConn = null;
	  try{
		  YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  dbConn = requestDbConn.getSysDbConn();
		  
		  StringBuffer sb = new StringBuffer("[");
		  List<CusArea> cusAreaList = new ArrayList<CusArea>();
		  List<CusProvince> cusProvinceList = new ArrayList<CusProvince>();
		  List<CusCity> cusCityList = new ArrayList<CusCity>();
		  YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
		  cusAreaList = userPrivLogic.selectUserManageAreas(dbConn);
		  if(cusAreaList != null && cusAreaList.size() > 0){
			  for(int i = 0; i < cusAreaList.size(); i++){
				  CusArea menuArea = cusAreaList.get(i);
				  if(!menuArea.getAreName().equals("请选择")){
					  sb.append("{");
					  sb.append("nodeId:\"c" + menuArea.getAreId() + "\"");
					  sb.append(",name:\"" + menuArea.getAreName() + "\"");
					  sb.append(",parentId:\"-1\"");
					  sb.append(",isHaveChild:" + IsHaveChildProvince(request, response, String.valueOf(menuArea.getAreId())));
					  sb.append(",isChecked:false");
					  sb.append(",imgAddress:\"" + request.getContextPath() + "/core/funcs/display/img/org.gif\"");
					  sb.append("},");
					  if(IsHaveChildProvince(request, response, String.valueOf(menuArea.getAreId())) == 1){
						  cusProvinceList = userPrivLogic.selectUserManageProvinces(dbConn,menuArea.getAreId());
						  if(cusProvinceList!=null && cusProvinceList.size()>0){
							  for (int j = 0; j < cusProvinceList.size(); j++) {
								  CusProvince cusProvince= cusProvinceList.get(j);
								  String contextPath = request.getContextPath();
								  String imageAddress = null;
								  if (cusProvince == null) {
									  continue;
								  }
								  imageAddress = contextPath + "/core/funcs/display/img/org.gif";
								  sb.append("{");
								  sb.append("nodeId:\"c" + menuArea.getAreId() + "p"+ cusProvince.getPrvId() + "\"");
								  sb.append(",name:\"" + cusProvince.getPrvName() + "\"");
								  sb.append(",parentId:\"c" + menuArea.getAreId() + "\"");
								  sb.append(",isChecked:false");
								  sb.append(",isHaveChild:" + IsHaveChildCity(request, response, String.valueOf(cusProvince.getPrvId())));
								  sb.append(",imgAddress:\"" + imageAddress + "\"");
								  sb.append("},");
								  if(IsHaveChildCity(request, response, String.valueOf(cusProvince.getPrvId())) == 1){
									  cusCityList = userPrivLogic.selectUserManageCitys(dbConn,cusProvince.getPrvId());
									  if(cusCityList!=null && cusCityList.size()>0){
										  for (int k = 0; k < cusCityList.size(); k++) {
											  CusCity cusCity= cusCityList.get(k);
											  if (cusCity == null) {
												  continue;
											  }
											  imageAddress = contextPath + "/core/funcs/display/img/org.gif";
											  sb.append("{");
											  sb.append("nodeId:\"c" + menuArea.getAreId() + "p"+ cusProvince.getPrvId() + "t"+cusCity.getCityId() +"\"");
											  sb.append(",name:\"" + cusCity.getCityName() + "\"");
											  sb.append(",parentId:\"c" + menuArea.getAreId() + "p"+ cusProvince.getPrvId() +"\"");
											  sb.append(",isChecked:false");

											  sb.append(",isHaveChild:0");
											  sb.append(",imgAddress:\"" + imageAddress + "\"");
											  sb.append("},");
										  }
									  }
								  }
							  }
						  }
					  }
				  }
			  } 
		  }       
		  sb.deleteCharAt(sb.length() - 1);       
		  sb.append("]");
		  PrintWriter out = null;
		  try {
			  out = response.getWriter();
			  out.print(sb);
			  out.flush();
		  } catch (IOException e1) {
			  e1.printStackTrace();
		  } finally {
			  out.close();
		  }
	  }catch(Exception ex){
		  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		  request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		  throw ex;
	  }
	  return null;
  }
  
  /**
   * 查询所有的客户信息资料根据区域，展示树形菜单
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAllByOnlyAreas(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {

	  Connection dbConn = null;
	  try{
		  YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  dbConn = requestDbConn.getSysDbConn();
		  String idStr = request.getParameter("id");
		  String countryId = "organizationNodeId";
		  StringBuffer record = new StringBuffer();
		  StringBuffer sb = new StringBuffer();
		  List<CusArea> cusAreaList = new ArrayList<CusArea>();
		  List<CusProvince> cusProvinceList = new ArrayList<CusProvince>();
		  List<CusCity> cusCityList = new ArrayList<CusCity>();
		  YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
		  cusAreaList = userPrivLogic.selectUserManageAreas(dbConn);
		  if(cusAreaList != null && cusAreaList.size() > 0){
			  int countryIndex = idStr.indexOf("c");
				int provinceIndex = idStr.indexOf("p");
				int cityIndex = idStr.indexOf("t");
				
			  if ((idStr == null || "".equals(idStr) || "0".equals(idStr))
			          && !countryId.equals(idStr)) {
				  if(countryIndex == -1 && provinceIndex == -1 && cityIndex == -1){
					  for(int i = 0; i < cusAreaList.size(); i++){
						  CusArea menuArea = cusAreaList.get(i);
						  if(!menuArea.getAreName().equals("请选择")){
							  
								  record.append("{");
								  record.append("nodeId:\"c" + menuArea.getAreId() + "\"");
								  record.append(",name:\"" + menuArea.getAreName() + "\"");
								  record.append(",isHaveChild:" + IsHaveChildProvince(request, response, String.valueOf(menuArea.getAreId())));
								  record.append(",imgAddress:\"" + request.getContextPath() + "/core/funcs/display/img/org.gif\"");
								  record.append(",title:\"" + YHUtility.encodeSpecial(menuArea.getAreName()) + "\"");
								  record.append("},");
							     
						  }
					  } 
				  }
			  }
			  else{
				  String contextPath = request.getContextPath();
				  String imageAddress = null;
				  
					String country_id = "";
					String province_id = "";
					String city_id = "";
					if(countryIndex > -1 && provinceIndex == -1 && cityIndex == -1){
						
						 String areaId = idStr.substring(1, idStr.length());
						  if(IsHaveChildProvince(request, response, areaId) == 1){
							  cusProvinceList = userPrivLogic.selectUserManageProvinces(dbConn,Long.parseLong(areaId));
							  if(cusProvinceList!=null && cusProvinceList.size()>0){
								  for (int j = 0; j < cusProvinceList.size(); j++) {
									  CusProvince cusProvince= cusProvinceList.get(j);
									 
									  if (cusProvince == null) {
										  continue;
									  }
									  imageAddress = contextPath + "/core/funcs/display/img/org.gif";
									  record.append("{");
									  record.append("nodeId:\"c" + areaId + "p"+ cusProvince.getPrvId() + "\"");
									  record.append(",name:\"" + cusProvince.getPrvName() + "\"");
									  record.append(",parentId:\"c" + areaId + "\"");
									  record.append(",title:\"" + YHUtility.encodeSpecial(cusProvince.getPrvName()) + "\"");
									  record.append(",extData:true");
									  record.append(",isHaveChild:" + IsHaveChildCity(request, response, String.valueOf(cusProvince.getPrvId())));
									  record.append(",imgAddress:\"" + imageAddress + "\"");
									  record.append("},");
								  }
							  }
						  }
					}
					
				 else{
						if(countryIndex > -1 && provinceIndex > -1 && cityIndex == -1){
							country_id = idStr.substring(countryIndex+1, provinceIndex);
							province_id = idStr.substring(provinceIndex+1, idStr.length());
						}
						
					  if(IsHaveChildCity(request, response, province_id) == 1){
						  cusCityList = userPrivLogic.selectUserManageCitys(dbConn,Long.parseLong(province_id));
						  if(cusCityList!=null && cusCityList.size()>0){
							  for (int k = 0; k < cusCityList.size(); k++) {
								  CusCity cusCity= cusCityList.get(k);
								  if (cusCity == null) {
									  continue;
								  }
								  imageAddress = contextPath + "/core/funcs/display/img/org.gif";
								  record.append("{");
								  record.append("nodeId:\"c" + Long.parseLong(country_id) + "p"+ Long.parseLong(province_id) + "t"+cusCity.getCityId() +"\"");
								  record.append(",name:\"" + cusCity.getCityName() + "\"");
								  record.append(",parentId:\"c" + Long.parseLong(country_id) + "p"+ Long.parseLong(province_id) +"\"");
								  record.append(",title:\"" + YHUtility.encodeSpecial(cusCity.getCityName()) + "\"");
								  record.append(",extData:true");

								  record.append(",isHaveChild:0");
								  record.append(",imgAddress:\"" + imageAddress + "\"");
								  record.append("},");
							  }
						  }
					  }
				  }
			
			}
		  }
					  
			
	      sb.append("[").append(record).append("]");
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

  
  /**
   * 根据区域查询该区域下的客户
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCustomersByArea(HttpServletRequest request,
		  HttpServletResponse response) throws Exception{
	  Connection dbConn = null;
	  try{
		  YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  dbConn = requestDbConn.getSysDbConn();
		  String[] areaIds = request.getParameterValues("str");
//		  List<Map<String,String>> manageAreaList = getManageAreasByUserId(dbConn,"");
//		  
//		  YHORM orm = new YHORM();
//		  Map map = new HashMap();
//		  StringBuffer sb = new StringBuffer("[");
//		  List<CusArea> cusAreaList = null;
//		  List<CusProvince> cusProvinceList = null;
//		  List<CusCity> cusCityList = null;
//		  ArrayList<CrmManagementArea> menuList = null;
//		  String[] filterAreaId = new String[]{" 1=1 order by are_id"};
//		  String[] filterProvinceId = new String[]{" 1=1 order by prv_id"};
//		  String[] filterCityId = new String[]{" 1=1 order by city_id"};
//		  String[] filterMenuId = new String[]{" 1=1 order by Id"};
//		  YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
//		  cusAreaList = userPrivLogic.selectUserManageAreas(dbConn);
//		  if(cusAreaList != null && cusAreaList.size() > 0){
//			  for(int i = 0; i < cusAreaList.size(); i++){
//				  CusArea menuArea = cusAreaList.get(i);
//				  if(!menuArea.getAreName().equals("请选择")){
//					  sb.append("{");
//					  sb.append("nodeId:\"c" + menuArea.getAreId() + "\"");
//					  sb.append(",name:\"" + menuArea.getAreName() + "\"");
//					  sb.append(",parentId:\"-1\"");
//					  sb.append(",isHaveChild:" + IsHaveChildProvince(request, response, String.valueOf(menuArea.getAreId())));
//					  sb.append(",isChecked:" + isChecked(manageAreaList,String.valueOf(menuArea.getAreId()),"country"));
//					  sb.append(",imgAddress:\"" + request.getContextPath() + "/core/funcs/display/img/org.gif\"");
//					  sb.append("},");
//					  if(IsHaveChildProvince(request, response, String.valueOf(menuArea.getAreId())) == 1){
//						  cusProvinceList = userPrivLogic.selectUserManageProvinces(dbConn,menuArea.getAreId());
//						  if(cusProvinceList!=null && cusProvinceList.size()>0){
//							  for (int j = 0; j < cusProvinceList.size(); j++) {
//								  CusProvince cusProvince= cusProvinceList.get(j);
//								  String contextPath = request.getContextPath();
//								  String imageAddress = null;
//								  if (cusProvince == null) {
//									  continue;
//								  }
//								  imageAddress = contextPath + "/core/funcs/display/img/org.gif";
//								  sb.append("{");
//								  sb.append("nodeId:\"c" + menuArea.getAreId() + "p"+ cusProvince.getPrvId() + "\"");
//								  sb.append(",name:\"" + cusProvince.getPrvName() + "\"");
//								  sb.append(",parentId:\"c" + menuArea.getAreId() + "\"");
//								  sb.append(",isChecked:" + isChecked(manageAreaList,String.valueOf(cusProvince.getPrvId()),"province"));
//								  sb.append(",isHaveChild:" + IsHaveChildCity(request, response, String.valueOf(cusProvince.getPrvId())));
//								  sb.append(",imgAddress:\"" + imageAddress + "\"");
//								  sb.append("},");
//								  if(IsHaveChildCity(request, response, String.valueOf(cusProvince.getPrvId())) == 1){
//									  cusCityList = userPrivLogic.selectUserManageCitys(dbConn,cusProvince.getPrvId());
//									  if(cusCityList!=null && cusCityList.size()>0){
//										  for (int k = 0; k < cusCityList.size(); k++) {
//											  CusCity cusCity= cusCityList.get(k);
//											  if (cusCity == null) {
//												  continue;
//											  }
//											  imageAddress = contextPath + "/core/funcs/display/img/org.gif";
//											  sb.append("{");
//											  sb.append("nodeId:\"c" + menuArea.getAreId() + "p"+ cusProvince.getPrvId() + "t"+cusCity.getCityId() +"\"");
//											  sb.append(",name:\"" + cusCity.getCityName() + "\"");
//											  sb.append(",parentId:\"c" + menuArea.getAreId() + "p"+ cusProvince.getPrvId() +"\"");
//											  sb.append(",isChecked:" + isChecked(manageAreaList,String.valueOf(cusCity.getCityId()),"city"));
//											  sb.append(",isHaveChild:0");
//											  sb.append(",imgAddress:\"" + imageAddress + "\"");
//											  sb.append("},");
//										  }
//									  }
//								  }
//							  }
//						  }
//					  }
//				  }
//			  } 
//		  }       
//		  sb.deleteCharAt(sb.length() - 1);       
//		  sb.append("]");
		  PrintWriter out = null;
		  try {
			  out = response.getWriter();
			  out.print("=================shenrm_test");
			  out.flush();
		  } catch (IOException e1) {
			  e1.printStackTrace();
		  } finally {
			  out.close();
		  }
	  }catch(Exception ex){
		  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		  request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		  throw ex;
	  }
	  return null;
  }
  
  public String getMyManageArea(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
	  Connection dbConn = null;
	  YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	  dbConn = requestDbConn.getSysDbConn();
	  List<CrmManagementArea> list = new ArrayList<CrmManagementArea>();
	  String person_id = request.getParameter("seqId");  //人员标识
	  YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
	  list = userPrivLogic.getCrmManagementArea(dbConn,person_id);
	  if(list != null && list.size() > 0){
		  CrmManagementArea cmar = list.get(0);
		  StringBuffer sb = new StringBuffer("[");
		  sb.append("{");
		  sb.append("Id:\"" + cmar.getId() + "\"");
		  sb.append(",person_id:\"" + cmar.getPerson_id() + "\"");
		  sb.append(",person:\"" + cmar.getPerson() + "\"");
		  sb.append(",management_area:\"" + cmar.getManagement_area() + "\"");
		  sb.append(",remark:\"" + cmar.getRemark() + "\"");
		  sb.append("},");
		  sb.deleteCharAt(sb.length() - 1);       
		  sb.append("]");
		  PrintWriter out = null;
			try {
				out = response.getWriter();
				out.print(sb);
				out.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				out.close();
			}
	  }
	  return null;
  }
  
  
  public String saveManageAreaInfo(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
	  Connection dbConn = null;
	  long ma_id = 0;
	  try {
		  
		  YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  dbConn = requestDbConn.getSysDbConn();
		  YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
		  
		  String userId = request.getParameter("userId");
		  String userName = request.getParameter("userName");
		  String management_area = request.getParameter("management_area");
		  String remark = request.getParameter("remark");
		  CrmManagementArea cma = new CrmManagementArea();
		  cma.setPerson_id(userId);
		  cma.setPerson(userName);
		  cma.setManagement_area(management_area);
		  cma.setRemark(remark);
		  
		  //先对原来数据进行删除
		  userPrivLogic.deleteUserManageAreas(dbConn, userId);
		  //先添加管辖区域
		  userPrivLogic.addUserManageAreas(dbConn,cma);
		
		  ma_id = userPrivLogic.getMaxId(dbConn);
		  
		  String str = request.getParameter("checkboxIds");
		  String[] strs = {};
		  if(str != null && str.length() >0){
			  strs = str.split(",");
			  for (int i = 0; i < strs.length; i++) {
				String checkBoxId = strs[i];
				if(!"".equals(checkBoxId)){
					int countryIndex = checkBoxId.indexOf("c");
					int provinceIndex = checkBoxId.indexOf("p");
					int cityIndex = checkBoxId.indexOf("t");
					if(countryIndex > -1 && provinceIndex > -1 && cityIndex > -1){
						String country_id = checkBoxId.substring(countryIndex+1, provinceIndex);
						String province_id = checkBoxId.substring(provinceIndex+1, cityIndex);
						String city_id = checkBoxId.substring(cityIndex+1);
						userPrivLogic.addUserManageCity(dbConn,ma_id,country_id,province_id,city_id);
					}
				}
			}
		  }
		  
		  PrintWriter out = null;
			try {
				out = response.getWriter();
				String message = "{'state':'0','message':'设置成功'}";
				out.print(message);
				out.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				out.close();
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	dbConn.rollback();
	    }
	    return null;
  }
  
  public String getPrivString(Connection dbConn, int userId) throws Exception{
    List<YHUserPriv> positionPrivList = null;
    List funcList = new ArrayList();
    YHORM orm = new YHORM();
    String positionPriv = "";
    Map map1 = new HashMap();
    map1.put("SEQ_ID", userId);
    positionPrivList = orm.loadListSingle(dbConn, YHUserPriv.class, map1);
    for(int j = 0; j < positionPrivList.size(); j++){
      YHUserPriv poPriv = positionPrivList.get(j);
      int positionSeqIDD = poPriv.getSeqId();
      positionPriv =  poPriv.getFuncIdStr();
      if(positionPriv == null){
        positionPriv = "";
      }
    }
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
  
  public String getPrivTreeData(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
    Connection dbConn = null;
    try{
      int privNo = Integer.parseInt(request.getParameter("userId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("SEQ_ID", privNo);
      Object obj = orm.loadObjSingle(dbConn, YHUserPriv.class, map);
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
  
  /**
   * 获取管辖区域树数据
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getManageAreasTreeData(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
	  Connection dbConn = null;
	  try{
		  int privNo = Integer.parseInt(request.getParameter("userId"));
		  YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		  .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  dbConn = requestDbConn.getSysDbConn();
		  String data = null;
		  YHORM orm = new YHORM();
		  Map map = new HashMap();
		  map.put("SEQ_ID", privNo);
		  Object obj = orm.loadObjSingle(dbConn, CrmManagementArea.class, map);
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
  
  public String getSelectData(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //int userId = Integer.parseInt(request.getParameter("userId"));
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      ArrayList<YHUserPriv> perList = null;
      String[] filters = new String[]{"1 = 1 order by PRIV_NO, PRIV_NAME ASC"};
      //perList = (ArrayList<YHUserPriv>)orm.loadListSingle(dbConn, YHUserPriv.class, null);
      List funcList = new ArrayList();
      funcList.add("userPriv");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      List<Map> list = (List<Map>) map.get("USER_PRIV");
      for(Map m : list) {
      //for(int i = 0; i < perList.size(); i++){
      //YHUserPriv menu = perList.get(i);
        sb.append("{");
        sb.append("seqId:\"" + m.get("seqId") + "\"");
        sb.append(",privNo:\"" + m.get("privNo") + "\"");
        sb.append(",privName:\"" + (m.get("privName") == null ? "" : YHUtility.encodeSpecial(String.valueOf(m.get("privName")))) + "\"");
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
  
  public String updateSelectPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String priv_str = request.getParameter("privSeqId");
      String no_str = request.getParameter("privNo");
      String[] privSeqId = priv_str.split(",");
      String[] privNo = no_str.split(",");
      YHORM orm = new YHORM();
      Map m = new HashMap();
      String privNO = "";
      String seqId = "";
      for(int x = 0; x < privSeqId.length; x++){
        seqId = privSeqId[x];
        privNO = privNo[x];
        m.put("seqId", seqId);
        m.put("privNo", privNO);
        orm.updateSingle(dbConn, "userPriv", m);
        
        
        // add  seclog
        try{
          YHUserPriv up = (YHUserPriv)orm.loadObjSingle(dbConn, YHUserPriv.class, m);
          YHSecLogUtil.log(dbConn, person, request.getRemoteAddr(), "215",up.getPrivName(),"1", "调整角色排序成功");
        }catch(Exception e){}
     
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "调整角色排序成功");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getUserName(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //int userId = Integer.parseInt(request.getParameter("userId"));
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      ArrayList<YHUserPriv> perList = null;
      String[] filters = new String[]{"1 = 1 order by PRIV_NO ASC"};
      //perList = (ArrayList<YHUserPriv>)orm.loadListSingle(dbConn, YHUserPriv.class, null);
      List funcList = new ArrayList();
      funcList.add("userPriv");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      List<Map> list = (List<Map>) map.get("USER_PRIV");
      for(Map m : list) {
      //for(int i = 0; i < perList.size(); i++){
      //YHUserPriv menu = perList.get(i);
        sb.append("{");
        sb.append("seqId:\"" + m.get("seqId") + "\"");
        sb.append(",privNo:\"" + m.get("privNo") + "\"");
        sb.append(",privName:\"" + (m.get("privName") == null ? "" : YHUtility.encodeSpecial(String.valueOf(m.get("privName")))) + "\"");
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
  
  public String getPrivTree(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //int userId = Integer.parseInt(request.getParameter("userId"));
      //String sPriv = getPrivString(dbConn,userId);
      //"10,1077,107710,107733";
      //String[] privArray = sPriv.split(",");
      YHORM orm = new YHORM();
      Map map = new HashMap();
      StringBuffer sb = new StringBuffer("[");
      ArrayList<YHSysMenu> menuList = null;
      String[] filterMenuId = new String[]{" 1=1 order by MENU_ID"};
      menuList = (ArrayList<YHSysMenu>)orm.loadListSingle(dbConn, YHSysMenu.class, filterMenuId);
      for(int i = 0; i < menuList.size(); i++){
        YHSysMenu menu = menuList.get(i);
        sb.append("{");
        sb.append("nodeId:\"" + menu.getMenuId() + "\"");
        sb.append(",name:\"" + menu.getMenuName() + "\"");
        sb.append(",isHaveChild:" + IsHaveChild(request, response, String.valueOf(menu.getMenuId())));
        //sb.append(",isChecked:" + isChecked(privArray,(String) menu.getMenuId()));
        sb.append(",imgAddress:\"" + request.getContextPath() + "/core/styles/imgs/menuIcon/" + menu.getImage() + "\"");
        sb.append("},");
      }       
      List funcList = new ArrayList();
      funcList.add("sysFunction");
      String[] filters = new String[]{};
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      List<Map> list = (List<Map>) map.get("OA_SYS_FUNC");
      String contextPath = request.getContextPath();
      for(Map m : list){
        String funcAddress = null;
        String imageAddress = null;
        String fun = (String) m.get("funcCode");
        if (fun == null) {
          continue;
        }
        imageAddress = contextPath + "/core/funcs/display/img/org.gif";
        if (fun.startsWith("/")) {
          funcAddress = contextPath + fun;
        }else {
          funcAddress = contextPath + "/core/funcs/" + fun + "/";
        }
        sb.append("{");
        sb.append("nodeId:\"" + m.get("menuId") + "\"");
        sb.append(",name:\"" + m.get("funcName") + "\"");
        //sb.append(",isChecked:" + isChecked(privArray,(String) m.get("menuId")));
        sb.append(",isHaveChild:" + IsHaveChild(request, response, String.valueOf(m.get("menuId"))));
        sb.append(",imgAddress:\"" + imageAddress  + "\"");
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
  
  public String usertAddDeleltPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson login = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      //int seqId = Integer.parseInt(request.getParameter("treeId"));
      int user_op = Integer.parseInt(request.getParameter("user_op"));
      String treeNo = request.getParameter("treeNo");
      String userPrivSeqId = request.getParameter("userPrivSeqId");
      String funcIdStr = "";
      String userPrivSeqID = "";
      //ArrayStack prayArrId = null;
      boolean isHave = false;
      YHORM orm = new YHORM();
      Map m = new HashMap();
      String treeNO[] = treeNo.split(",");
      String userSeqId[] = userPrivSeqId.split(",");
      YHUserPrivLogic upl = new YHUserPrivLogic();
      String str = "";
      if(user_op == 0){
        for(int x = 0; x < userSeqId.length; x++){
          userPrivSeqID = userSeqId[x];
          str = upl.selectUserPrivFun(dbConn, userPrivSeqID);
          if(str == null||str.equals("")){
           // for(int i = 0; i < treeNO.length; i++){
             // funcIdStr = treeNO[i];
            //  if(str != null && str != ""){
            //    str += ",";
             // }
            str = treeNo;
           // }
            m.put("seqId", userPrivSeqID);
            m.put("funcIdStr", str);
            orm.updateSingle(dbConn, "userPriv", m);
          }else{
            String func[] = str.split(",");
            for(int i = 0; i < treeNO.length; i++){
              funcIdStr = treeNO[i];
              for(int y = 0; y < func.length; y++){
                String funcStr = func[y];
                if(funcStr.equals(funcIdStr) && !YHUtility.isNullorEmpty(funcStr)){
                  isHave = true;
                  break;
                }
              }
              if(!isHave){
                if(!YHUtility.isNullorEmpty(str)){
                  str += ",";
                }
                str += funcIdStr;
              }
              isHave = false;
            }
            m.put("seqId", userPrivSeqID);
            m.put("funcIdStr", str);
            orm.updateSingle(dbConn, "userPriv", m);
          }
        }
      }else{
        for(int x = 0; x < userSeqId.length; x++){
          String newStr = "";
          userPrivSeqID = userSeqId[x];
          str = upl.selectUserPrivFun(dbConn, userPrivSeqID);
          if(YHUtility.isNullorEmpty(str)){
            
          }else{
            String func[] = str.split(",");
            for(int i = 0; i < treeNO.length; i++){
              funcIdStr = treeNO[i];
              List<String> tmp = new ArrayList<String>();
              for(int y = 0; y < func.length; y++){
                String funcStr = func[y];
                if(funcIdStr.equals(func[y]) == false){
                  tmp.add(func[y]);
                }
              }
              func = new String[tmp.size()];
              int j = 0 ;
              for(String t : tmp){
                func[j] = t;
                j++;
              }
              tmp = null;
            }
            String funcStr = "";
            for(int i = 0 ; i < func.length ; i++){
              if(!YHUtility.isNullorEmpty(funcStr)){
                funcStr += ",";
              }
              funcStr += func[i];
            }
            m.put("seqId", userPrivSeqID);
            m.put("funcIdStr", funcStr);
            orm.updateSingle(dbConn, "userPriv", m);
          }
        }
      }
      
      /**
       * 安全日志 ---舒友林、
       */
      String privNames = upl.getNameByIdStr(userPrivSeqId, dbConn);//角色名称字符串
      
      //操作菜单名称
      String menuNameStr = upl.selectMemuNameByMenuId(dbConn,treeNo);
      String menuFunNameStr = upl.getChildMenuName(dbConn,treeNo);
      String optType = "添加";
      if(user_op != 0){
        optType = "删除";
      }
      YHSecLogUtil.log(dbConn, login, request.getRemoteAddr(), "215",privNames,"1", "将角色 为\"" +privNames + "\"" + optType + "菜单权限: \"" + menuNameStr +"," + menuFunNameStr + "\"");
      
      
      //for(int i = 0; i < lidd.size(); i++){
       //ment =  (YHDepartment) deptlogic.deleteDeptMul(dbConn, seqId).get(i);
        //String a = (String) lidd.get(i);
       //YHDepartment deptent = (YHDepartment) lidd.get(i);
        //String a = (String) lidd.get(i);
        //System.out.println(deptent.getSeqId()+"xxxxxxxxxxxxxxxxxxxxxx");
       // dt = (YHDepartment)orm.loadObjComplex(dbConn, YHDepartment.class, deptent.getSeqId());
       // System.out.println("YHDepartment : " + dt);
       // dt.setSeqId(deptent.getSeqId());
        //orm.deleteComplex(dbConn, dt);
      //}
     // YHDepartment dt = (YHDepartment)orm.loadObjComplex(dbConn, YHDepartment.class, seqId);
      //YHDepartment dpt = (YHDepartment) YHFOM.build(request.getParameterMap());
      //dt.setSeqId(seqId);
      //orm.deleteComplex(dbConn, dt);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功数据库的数据");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  public String otherPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int user_op = Integer.parseInt(request.getParameter("user_op"));
      String userPrivs = YHUtility.null2Empty(request.getParameter("treeNo"));
      String users = YHUtility.null2Empty(request.getParameter("userPrivSeqId"));
      YHUserPrivLogic logic = new YHUserPrivLogic();
      if(user_op == 0){
        logic.addUserPriv(dbConn, users, userPrivs);
      }else{
        logic.getOutPriv(dbConn, users, userPrivs);
      }
      /**
       * 安全日志 ---舒友林、
       */
      YHPersonLogic pl = new YHPersonLogic();
      String userNames = pl.getNameBySeqIdStr(users, dbConn);//人员名称
      YHUserPrivLogic upl = new YHUserPrivLogic();
      
      String privNames = upl.getNameByIdStr(userPrivs, dbConn);//角色名称字符串
  
      String optType = "添加";
      if(user_op != 0){
        optType = "删除";
      }
      YHPerson login = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHSecLogUtil.log(dbConn, login, request.getRemoteAddr(), "215","人员seqId[" +users + "]" ,"1", "给用户\"" +userNames + "\"" + optType + "辅助角色: \"" + privNames + "\"");
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getAutoData(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHORM orm = new YHORM();
      HashMap map = null;
      YHFlowFormLogicUtil dtt = new YHFlowFormLogicUtil();
      int seqID = dtt.deleteDeptMul(dbConn, seqId);
      String func = "";
      String dd = dtt.deleteDept(dbConn, seqID);
      String[]str = dd.split(",");
      StringBuffer sb = null;
      List<Map> list = new ArrayList();
      boolean ma = true;
      ArrayList<YHUserPriv> perList = null;
      sb = new StringBuffer("[");
      for(int i = 0; i < str.length; i++){
        func = str[i];
        String[] filters = new String[]{"SEQ_ID="+func};
        List funcList = new ArrayList();
        funcList.add("person");
        map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
        list.addAll((List<Map>) map.get("PERSON"));
      }
        for(Map m : list){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userName:\"" + m.get("userName") + "\"");
          sb.append(",userPriv:\"" + m.get("userPriv") + "\"");
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
  
  /**
   * 权限列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getUserPrivList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHUserPrivLogic upl = new YHUserPrivLogic();
      String data = upl.getUserPrivList(dbConn,request.getParameterMap());
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
  
  public String showReader(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    YHNotify notify = null;
    String seqId = request.getParameter("seqId");
    String displayAll = request.getParameter("displayAll");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHUserPrivLogic notifyShowLogic = new YHUserPrivLogic();
      String data = notifyShowLogic.showReader(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getAllUsers(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHUserPrivLogic dl = new YHUserPrivLogic();
      String data = String.valueOf(dl.allUsers(dbConn, seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getOtherUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHUserPrivLogic dl = new YHUserPrivLogic();
      String data = String.valueOf(dl.otherUser(dbConn, seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getNotLoginUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHUserPrivLogic dl = new YHUserPrivLogic();
      String data = String.valueOf(dl.notLoginUser(dbConn, seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getUserPrivNo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userPriv = request.getParameter("seqId");
      YHUserPrivLogic dl = new YHUserPrivLogic();
      String data = String.valueOf(dl.userPrivNo(dbConn, userPriv));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
