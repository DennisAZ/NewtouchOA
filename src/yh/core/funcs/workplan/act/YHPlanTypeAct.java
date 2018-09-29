package yh.core.funcs.workplan.act;
import java.sql.Connection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workplan.data.YHPlanType;
import yh.core.funcs.workplan.logic.YHPlanTypeLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;

public class YHPlanTypeAct {
  /**
   * 查询数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String planType(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化YHPlanTypeLogic
      YHPlanTypeLogic pLogic = new YHPlanTypeLogic();
      //调用selectType无条件，返回list
      List<YHPlanType> list = pLogic.selectType(dbConn);
      //遍历返回的list，将数据保存到Json中
      String data = "[";

      YHPlanType type = new YHPlanType(); 
      for (int i = 0; i < list.size(); i++) {
        type = list.get(i);
        data = data + YHFOM.toJson(type).toString()+",";
      }
      if(list.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data.replaceAll("\\n", "");
      data = data.replaceAll("\\r", "");
      data = data + "]";
      //将遍历数据保存到request
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 添加数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String addType(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化YHPlanTypeLogic
      YHPlanTypeLogic pLogic = new YHPlanTypeLogic();
      YHPlanType type = new YHPlanType();
      type.setTypeName(request.getParameter("TYPE_NAME").replaceAll("\"","\\\""));
      type.setTypeNO(Integer.parseInt(request.getParameter("TYPE_NO")));
      pLogic.addType(dbConn,type);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 删除数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String deleteType(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化YHPlanTypeLogic
      YHPlanTypeLogic pLogic = new YHPlanTypeLogic();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      pLogic.deleteType(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据失败");
      throw e;
    }
    return "/core/funcs/workplan/type/index.jsp";
  }
  /**
   * 删除所有数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String deleteTypeAll(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化YHPlanTypeLogic
      YHPlanTypeLogic pLogic = new YHPlanTypeLogic();
      pLogic.deleteTypeAll(dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据失败");
      throw e;
    }
    return "/core/funcs/workplan/type/index.jsp";
  }
  
  /**
   * 修改数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String updateType(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化YHPlanTypeLogic
      YHPlanTypeLogic pLogic = new YHPlanTypeLogic();
      YHPlanType type = new YHPlanType();
      type.setSeqId(Integer.parseInt(request.getParameter("seqId")));
      type.setTypeName(request.getParameter("TYPE_NAME"));
      type.setTypeNO(Integer.parseInt(request.getParameter("TYPE_NO")));
      pLogic.updateType(dbConn, type);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    }
    return "/core/funcs/workplan/type/index.jsp";
  }
  /**
   * 根据ID查询数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String selectId(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化YHPlanTypeLogic
      YHPlanTypeLogic pLogic = new YHPlanTypeLogic();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHPlanType type = pLogic.selectId(dbConn, seqId);
      request.setAttribute("type",type);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    }
    return "/core/funcs/workplan/type/editor.jsp";
  }
  /**
   * 根据typeName查询数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String selectTypeName(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化YHPlanTypeLogic
      YHPlanTypeLogic pLogic = new YHPlanTypeLogic();
      String typeName = request.getParameter("typeName");
      YHPlanType planType = pLogic.selectTypeName(dbConn, typeName);
    //定义数组将数据保存到Json中
      String data = "[";
      if(planType != null) {
        data = data + YHFOM.toJson(planType);
        //data = data.substring(0, data.length()-1);
        data = data.replaceAll("\\n", "");
        data = data.replaceAll("\\r", "");
        //System.out.println(data);
      }
      data = data + "]";
      //保存查询数据是否成功，保存date
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
}
