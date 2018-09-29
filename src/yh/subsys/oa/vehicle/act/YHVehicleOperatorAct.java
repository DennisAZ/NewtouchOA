package yh.subsys.oa.vehicle.act;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.vehicle.data.YHVehicleOperator;
import yh.subsys.oa.vehicle.logic.YHVehicleOperatorLogic;

public class YHVehicleOperatorAct {
  /**
   * 添加或更新调度人员
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addUpdateOperator(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleOperator vcOperator = new YHVehicleOperator();
      YHVehicleOperatorLogic tvo = new YHVehicleOperatorLogic();
      String operatorName = request.getParameter("operatorName");
      String operatorId = request.getParameter("operatorId");
      String seqId = request.getParameter("seqId");
      YHORM orm = new YHORM();
      //查询调度人员
      Map map = null;
      ArrayList<YHVehicleOperator> operatorList =  tvo.selectOperator(dbConn, map);
      vcOperator.setOperatorId(operatorId);
      vcOperator.setOperatorName(operatorName);
      if(operatorList.size()>0){//更新
         tvo.updateOperator(dbConn, operatorId,operatorName);
      }else{//新建
        tvo.addOperator(dbConn, vcOperator);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询调度人员
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectOperator(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String data =  "{";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVehicleOperator vcOperator = new YHVehicleOperator();
      YHVehicleOperatorLogic tvo = new YHVehicleOperatorLogic();
      Map map = null;
      ArrayList<YHVehicleOperator> operatorList =  tvo.selectOperator(dbConn, map);
      YHPersonLogic personLogic = new YHPersonLogic();
      for (int i = 0; i < operatorList.size(); i++) {
        YHVehicleOperator operator = operatorList.get(0);
        String operatorName = personLogic.getNameBySeqIdStr(operator.getOperatorId(), dbConn);
        data = data + "operatorId:\"" + operator.getOperatorId() + "\",operatorName:\"" + YHUtility.encodeSpecial(operatorName) + "\"";
      }
      data = data + "}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出主菜单和子菜单项的数据");
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
   * 查询调度 人员 
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
      YHVehicleOperatorLogic logic = new YHVehicleOperatorLogic();
      Map map = null;
      List<YHPerson> personList = new ArrayList<YHPerson>();
      personList = logic.getPersonByIds(dbConn, map);
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
  /**
   * 在线调度人员 -lz
   * 
   * */
  public String selectPerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = YHVehicleOperatorLogic.selectPerson(dbConn);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
