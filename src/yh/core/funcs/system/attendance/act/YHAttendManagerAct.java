package yh.core.funcs.system.attendance.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.attendance.data.YHAttendManager;
import yh.core.funcs.system.attendance.logic.YHAttendManagerLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHAttendManagerAct {
  /**
   * 
   * 添加/更新审批管理 人员 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String add_updateManager(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendManager manager = new YHAttendManager();
      String userIds = "";
      userIds = request.getParameter("user"); 
      //System.out.println(userIds);
      if(userIds == null){
        userIds = "";
      }
      manager.setManagers(userIds);
      YHAttendManagerLogic yhaml = new YHAttendManagerLogic();
      Map map = null;
      yhaml.add_updateManager(dbConn, manager, map);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/system/attendance/index.jsp";
  }
  /**
   * 
   * 查询审批管理 人员 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectManager(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHAttendManager manager = new YHAttendManager();
      YHAttendManagerLogic yhaml = new YHAttendManagerLogic();
      Map map = null;
      String ids = yhaml.selectManagerIds(dbConn, map);
      String names = "";
      //ids = ids.replaceAll(String.valueOf(userId)+",", "");
      if(!ids.equals("")){
        names = yhaml.getNamesByIds(dbConn, map);
      }
      //System.out.println(ids);
      String data = "{user:\"" + ids + "\",userDesc:\"" + names+ "\"}";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * 查询审批管理 人员 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectManagerPerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHAttendManager manager = new YHAttendManager();
      YHAttendManagerLogic yhaml = new YHAttendManagerLogic();
      Map map = null;
      List<YHPerson> personList = new ArrayList<YHPerson>();
      personList = yhaml.getPersonByIds(dbConn, map);
      String data = "[";
      for (int i = 0; i < personList.size(); i++) {
        YHPerson person = personList.get(i);
        data = data + "{seqId:" + person.getSeqId() + ",userName:\"" + person.getUserName() + "\"},";
      }
      if(personList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

}
