package yh.subsys.oa.asset.act;
import java.sql.Connection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.asset.data.YHCpAssetType;
import yh.subsys.oa.asset.logic.YHCpAssetTypeLogic;


public class YHCpAssetTypeAct {
  /**
   * 查询类别代码
   * @param request
   * @param response
   * @throws Exception
   */
  public String assetTypeId(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //无条件，返回list
      List<YHCpAssetType> list = YHCpAssetTypeLogic.specList(dbConn);
      
      //遍历返回的list，将数据保存到Json中
      String data = "[";

      YHCpAssetType cp = new YHCpAssetType(); 
      for (int i = 0; i < list.size(); i++) {
        cp = list.get(i);
        data = data + YHFOM.toJson(cp).toString()+",";
        System.out.println("1:"+data);
      }
      if(list.size()>0){
        data = data.substring(0, data.length()-1);
        System.out.println("2:"+data);
      }
      data = data.replaceAll("\\n", "");
      data = data.replaceAll("\\r", "");
      data = data + "]";
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
      YHORM orm = new YHORM();//orm映射数据库
      YHCpAssetType type = (YHCpAssetType) YHFOM.build(request.getParameterMap());
      //添加数据
      orm.saveSingle(dbConn,type);
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
      //将表单form1映射到YHTest实体类
      YHORM orm = new YHORM();//orm映射数据库
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHCpAssetType cp = new YHCpAssetType();
      cp.setSeqId(seqId);
      orm.deleteSingle(dbConn,cp);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
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
      YHCpAssetTypeLogic.deleteAll(dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
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
      YHORM orm = new YHORM();
      YHCpAssetType type = (YHCpAssetType) YHFOM.build(request.getParameterMap());
      orm.updateSingle(dbConn, type);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
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
      YHCpAssetTypeLogic pLogic = new YHCpAssetTypeLogic();
      String typeName = request.getParameter("typeName");
      YHCpAssetType planType = pLogic.selectTypeName(dbConn,typeName);
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
  
  /**
   * 删除所有数据
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
      //将表单form1映射到YHTest实体类
      YHORM orm = new YHORM();//orm映射数据库
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHCpAssetType cp = (YHCpAssetType)orm.loadObjSingle(dbConn, YHCpAssetType.class,seqId);
      request.setAttribute("cp",cp);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据失败");
      throw e;
    }
    return "/subsys/oa/asset/assetType/editor.jsp";
  }
}
