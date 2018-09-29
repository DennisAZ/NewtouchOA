package yh.subsys.oa.asset.act;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.subsys.oa.asset.data.YHCpCptlInfo;
import yh.subsys.oa.asset.logic.YHCpCptlInfoLogic;
import yh.subsys.oa.asset.logic.YHCpCptlRecordLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHCpCptlInfoAct {
 
  /**
   *查询所有(分页)通用列表显示数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listSelect(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
    //将表单form1映射到YHTest实体类
      //YHORM orm = new YHORM();orm映射数据库
      YHCpCptlInfo cptlInfo = new YHCpCptlInfo();

      String cptlNo = request.getParameter("cptlNo");
      String valMax = request.getParameter("cptlValMax");
      String cptlName = request.getParameter("cptlName");
      String cptlSpec = request.getParameter("cptlSpec");
      String val = request.getParameter("cptlVal");
      String listDate = request.getParameter("listDate");
      String getDate = request.getParameter("getDate");
      String keeper = request.getParameter("keeper");
      String safekeeping = request.getParameter("safekeeping");
      String remark = request.getParameter("remark");
      
      double cptMax = 0 ;
      if (!YHUtility.isNullorEmpty(val)) {
        double cptlVal = Double.parseDouble(val);
        cptlInfo.setCptlVal(cptlVal);
      }
      if (!YHUtility.isNullorEmpty(valMax)) {
        cptMax = Double.parseDouble(valMax);
      }
      if (!YHUtility.isNullorEmpty(listDate)) {
        cptlInfo.setListDate(Date.valueOf(listDate));
        
      }
      if (!YHUtility.isNullorEmpty(getDate)) {
        cptlInfo.setGetDate(Date.valueOf(getDate));
      }

      cptlInfo.setCptlName(cptlName);
      cptlInfo.setCptlNo(cptlNo);
      cptlInfo.setCptlSpec(cptlSpec);
      cptlInfo.setKeeper(keeper);
      cptlInfo.setSafekeeping(safekeeping);
      cptlInfo.setRemark(remark);
      
      YHCpCptlInfoLogic gift = new YHCpCptlInfoLogic();
      String data = gift.listSelect(dbConn,request.getParameterMap(),cptlInfo,cptMax);
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
  /**
   * 删除数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String getDelete(HttpServletRequest request,
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
      YHCpCptlInfo cp = new YHCpCptlInfo();
      YHCpCptlRecordLogic record = new YHCpCptlRecordLogic();
      cp.setSeqId(seqId);
      orm.deleteSingle(dbConn,cp);
      record.getDelete(dbConn,seqId);
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
   * 查找数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String getSelect(HttpServletRequest request,
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
      YHCpCptlInfo cp = new YHCpCptlInfo();
      cp.setSeqId(seqId);
      YHCpCptlInfo cpcp = (YHCpCptlInfo)orm.loadObjSingle(dbConn,YHCpCptlInfo.class,seqId);
      
      request.setAttribute("cpcp",cpcp);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    }
    return "/subsys/oa/asset/manage/detai.jsp";
  }
  
  /**
   *查询所有(分页)通用列表显示数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String assetSelect(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      //YHORM orm = new YHORM();orm映射数据库
      YHCpCptlInfo cptlInfo = new YHCpCptlInfo();
      String seqId2 = request.getParameter("SEQ_ID");
      String cptlNo = request.getParameter("CPTL_NO");
      String cptlName = request.getParameter("CPTL_NAME");
      String cptlSpec = request.getParameter("CPTL_SPEC");
      String typeId = request.getParameter("TYPE_ID");
      String useState = request.getParameter("USE_STATE");
      String useFor = request.getParameter("USE_FOR");
      String cpreFlag = request.getParameter("cpreFlag");

      int seqId = 0 ;
      if (!YHUtility.isNullorEmpty(seqId2)) {
        seqId = Integer.parseInt(seqId2);
        cptlInfo.setSeqId(seqId);
      } 
      cptlInfo.setCptlName(cptlName);
      cptlInfo.setCptlNo(cptlNo);
      cptlInfo.setCptlSpec(cptlSpec);
      cptlInfo.setTypeId(typeId);
      cptlInfo.setUseState(useState);
      cptlInfo.setUseFor(useFor);

      YHCpCptlInfoLogic gift = new YHCpCptlInfoLogic();
      String data = gift.assetSelect(dbConn,request.getParameterMap(),cptlInfo,cpreFlag);
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
  
  /**
   * 查询数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String querySelect(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHCpCptlInfo cptlInfo = new YHCpCptlInfo();

      String cptlNo = request.getParameter("cptlNo");
      String valMax = request.getParameter("cptlValMax");
      String cptlName = request.getParameter("cptlName");
      String cptlSpec = request.getParameter("cptlSpec");
      String val = request.getParameter("cptlVal");
      String listDate = request.getParameter("listDate");
      String getDate = request.getParameter("getDate");
      String keeper = request.getParameter("keeper");
      String safekeeping = request.getParameter("safekeeping");
      String remark = request.getParameter("remark");

      double cptMax = 0 ;
      if (!YHUtility.isNullorEmpty(val)) {
        double cptlVal = Double.parseDouble(val);
        cptlInfo.setCptlVal(cptlVal);
      }
      if (!YHUtility.isNullorEmpty(valMax)) {
        cptMax= Double.parseDouble(valMax);
      }
      if (!YHUtility.isNullorEmpty(listDate)) {
        cptlInfo.setListDate(Date.valueOf(listDate));
        
      }
      if (!YHUtility.isNullorEmpty(getDate)) {
        cptlInfo.setGetDate(Date.valueOf(getDate));
      }
      cptlInfo.setCptlName(cptlName);
      cptlInfo.setCptlNo(cptlNo);
      cptlInfo.setCptlSpec(cptlSpec);
      cptlInfo.setKeeper(keeper);
      cptlInfo.setSafekeeping(safekeeping);
      cptlInfo.setRemark(remark);
      
      YHCpCptlInfoLogic gift = new YHCpCptlInfoLogic();
      String data = gift.querySelect(dbConn,request.getParameterMap(),cptlInfo,cptMax);
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

  /**
   * 查询数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String applySelect(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHCpCptlInfoLogic gift = new YHCpCptlInfoLogic();
      String data = gift.applySelect(dbConn,request.getParameterMap());
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
  
  /**
   *查询所有(分页)通用列表显示数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String cpcpSelect(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String name = String.valueOf(person.getSeqId());
      YHCpCptlInfoLogic gift = new YHCpCptlInfoLogic();
      String data = gift.cpcpSelect(dbConn,request.getParameterMap(),name);
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
  /**
   *查询所有ID串名字
   */
  public String userName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHCpCptlInfoLogic gift = new YHCpCptlInfoLogic();
      String data = gift.getName(dbConn,seqId);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   *查询所有部门串名字
   */
  public String userDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHCpCptlInfoLogic gift = new YHCpCptlInfoLogic();
      String data = gift.getDept(dbConn,seqId);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 添加数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String add(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();//orm映射数据库
      YHCpCptlInfo cpCptlInfo = (YHCpCptlInfo)YHFOM.build(request.getParameterMap());
      orm.saveSingle(dbConn, cpCptlInfo);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "增加成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "增加失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查找数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String getSelect2(HttpServletRequest request,
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
      YHCpCptlInfo cp = new YHCpCptlInfo();
      cp.setSeqId(seqId);
      YHCpCptlInfo cpcp = (YHCpCptlInfo)orm.loadObjSingle(dbConn,YHCpCptlInfo.class,seqId);
      request.setAttribute("cpcp",cpcp);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    }
    return "/subsys/oa/asset/manage/updateAsset.jsp";
  }
  /**
   * 根据ID修改数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String editAsset(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      YHORM orm = new YHORM();//orm映射数据库
      YHCpCptlInfo cp = (YHCpCptlInfo)YHFOM.build(request.getParameterMap());
      orm.updateSingle(dbConn,cp);
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
   * 根据ID修改数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String selectID(HttpServletRequest request,
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
      YHCpCptlInfo cp = (YHCpCptlInfo)orm.loadObjSingle(dbConn, YHCpCptlInfo.class, seqId);
      request.setAttribute("cp",cp);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    }
    return "/subsys/oa/asset/query/apply.jsp";
  }
}
