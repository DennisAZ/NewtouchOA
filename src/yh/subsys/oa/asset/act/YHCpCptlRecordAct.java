package yh.subsys.oa.asset.act;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.subsys.oa.asset.data.YHCpCptlInfo;
import yh.subsys.oa.asset.data.YHCpCptlRecord;
import yh.subsys.oa.asset.data.YHCpcptl;
import yh.subsys.oa.asset.logic.YHCpAssetTypeLogic;
import yh.subsys.oa.asset.logic.YHCpCptlInfoLogic;
import yh.subsys.oa.asset.logic.YHCpCptlRecordLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHCpCptlRecordAct {
  /**
   * 查询分类代码
   * @param request
   * @param response
   * @throws Exception
   */
  public String cpTypeId(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //无条件，返回list
      List<YHCpCptlInfo> list = YHCpCptlRecordLogic.typeList(dbConn);
      //遍历返回的list，将数据保存到Json中
      String data = "[";

      YHCpCptlInfo type = new YHCpCptlInfo(); 
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
      //System.out.println(data);

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
   * 查询名称代码
   * @param request
   * @param response
   * @throws Exception
   */
  public String nameList(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //无条件，返回list
      String useFlag = request.getParameter("useFlag");
      List<YHCpCptlInfo> list = YHCpCptlInfoLogic.nameList(dbConn, useFlag); 
      //遍历返回的list，将数据保存到Json中
      String data = "[";

      YHCpCptlInfo type = new YHCpCptlInfo(); 
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
      //System.out.println(data);

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
   * 查询分类代码
   * @param request
   * @param response
   * @throws Exception
   */
  public String useStateList(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //无条件，返回list
      List<YHCpCptlInfo> list = YHCpCptlRecordLogic.useStateList(dbConn);
      //遍历返回的list，将数据保存到Json中
      String data = "[";

      YHCpCptlInfo type = new YHCpCptlInfo(); 
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
      //System.out.println(data);

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
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      //YHORM orm = new YHORM();orm映射数据库
      YHCpCptlInfo cptlInfo = new YHCpCptlInfo();
      YHCpCptlRecord cptlRecord = new YHCpCptlRecord();

      String cpreFlag = request.getParameter("CPRE_FLAG");
      String cptlName = request.getParameter("CPTL_NAME");
      String cptlSpec = request.getParameter("CPTL_SPEC");
      String deptId2 = request.getParameter("DEPT_ID");
      String typeId = request.getParameter("TYPE_ID");
      String useFor = request.getParameter("USE_FOR"); 
      String useState = request.getParameter("USE_STATE");
      String useUser = request.getParameter("USE_USER");

      //      param = "CPRE_FLAG=" + cpreFlag + "&CPTL_NAME=" + cptlName 
      //      + "&CPTL_SPEC=" + cptlSpec + "&DEPT_ID=" + deptId2 + "&TYPE_ID=" + typeId
      //      + "&USE_FOR=" + useFor + "&USE_STATE=" + useState + "&USE_USER=" + useUser;

      int deptId = 0;
      if (!YHUtility.isNullorEmpty(deptId2)) {
        deptId = Integer.parseInt(deptId2);
        cptlRecord.setDeptId(deptId);
      }
      cptlRecord.setCpreFlag(cpreFlag);
      cptlInfo.setCptlName(cptlName);
      cptlInfo.setCptlSpec(cptlSpec);
      cptlInfo.setTypeId(typeId);
      cptlInfo.setUseFor(useFor);
      cptlInfo.setUseState(useState);
      cptlInfo.setUseUser(useUser);
      YHCpCptlRecordLogic cpcp = new YHCpCptlRecordLogic();
      String data = cpcp.listSelect(dbConn,request.getParameterMap(),cptlInfo,cptlRecord);
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
      //无条件，返回list
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHORM orm = new YHORM();//orm映射数据库
      YHCpCptlRecord record = (YHCpCptlRecord) YHFOM.build(request.getParameterMap());
      orm.saveSingle(dbConn,record);

      YHCpCptlRecordLogic cpre = new YHCpCptlRecordLogic();
      YHCpCptlInfo info = new YHCpCptlInfo();
      if (record.getCpreFlag().equals("1")) {
        info.setUseState("3");
        info.setUseDept(request.getParameter("deptId"));
        info.setUseUser(String.valueOf(person.getSeqId()));
      }
      if (record.getCpreFlag().equals("2")) {
        info.setUseState("1");
        info.setUseDept("");
        info.setUseUser("");
      }
      info.setSeqId(record.getCptlId());
      cpre.getUpdate(dbConn,info);

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
      YHCpCptlRecord cp = new YHCpCptlRecord();
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
   * 查询级联数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String getSelectID(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int seqId = Integer.parseInt(String.valueOf(request.getParameter("seqId")));
      YHCpCptlRecordLogic recordLogic = new YHCpCptlRecordLogic();

      List<YHCpcptl> list = recordLogic.getCpreFlag1(dbConn,seqId);
      List<YHCpcptl> list2 = recordLogic.getCpreFlag2(dbConn,seqId);
      String infoName = recordLogic.getName(dbConn, seqId);

      request.setAttribute("list",list);
      request.setAttribute("list2",list2);
      request.setAttribute("infoName",infoName);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, userJson);
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    }
    return "/subsys/oa/asset/manage/cpreDetai.jsp";
  }

  /**
   * 查询数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String getAssetId(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int seqId = Integer.parseInt(String.valueOf(request.getParameter("seqId")));
      YHCpCptlInfo type = YHCpAssetTypeLogic.getAsset(dbConn,seqId);
      request.setAttribute("type", type);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, userJson);
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    }
    return "/subsys/oa/asset/manage/record/updateQuery.jsp";
  }
  /**
   *修改数据
   * @throws Exception 
   *
   **/
  public String editAsset(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    //数据库的连接
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
    .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try {
      dbConn = requestDbConn.getSysDbConn();
      YHCpCptlRecord record = new YHCpCptlRecord();
      record.setSeqId(Integer.parseInt(request.getParameter("seqId")));
      record.setDeptId(Integer.parseInt(request.getParameter("deptId")));
      record.setCpreMemo(request.getParameter("cpreMemo"));
      record.setCpreReason(request.getParameter("cpreReason"));
      record.setCprePlace(request.getParameter("cprePlace"));
      YHCpCptlRecordLogic.editAsset(dbConn,record);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, userJson);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";

  }
}
