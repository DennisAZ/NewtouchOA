package yh.core.funcs.workflow.act;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.data.YHFlowFormReglex;
import yh.core.funcs.workflow.data.YHFlowFormType;
import yh.core.funcs.workflow.logic.YHFlowFormLogic;
import yh.core.funcs.workflow.logic.YHFormVersionLogic;
import yh.core.funcs.workflow.logic.YHWorkflowSave2DataTableLogic;
import yh.core.funcs.workflow.praser.YHFormPraser;
import yh.core.funcs.workflow.util.YHFlowFormUtility;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;

public class YHFormVersionAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.workflow.act.YHFormVersionAct");
  public String getVersionByFlow(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String flowId = request.getParameter("flowId");
      int id = Integer.parseInt(flowId);
      
      YHFlowRunUtility ut = new YHFlowRunUtility();
      int formId = ut.getFormId(dbConn, id);
      
      YHFormVersionLogic logic = new YHFormVersionLogic();
      String versions =  logic.getVersionNoByForm(dbConn, formId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + versions + "\"");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 采用新版本占新号
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String genFormVersion(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      int id = Integer.parseInt(seqId);
      
      YHORM orm = new YHORM();
      YHFlowFormType form = (YHFlowFormType) orm.loadObjSingle(dbConn, YHFlowFormType.class, id);
      
      String printModel = request.getParameter("printModel");
      String itemMax = request.getParameter("itemMax");
      printModel = printModel.replaceAll("\"", "\\\\\"");
      printModel = printModel.replaceAll("\r\n", "");
      YHFlowFormType form2 = new YHFlowFormType();
      form2.setFormName(form.getFormName());
      form2.setItemMax(Integer.parseInt(itemMax));
      form2.setPrintModel(printModel);
      form2.setDeptId(form2.getDeptId());
      
      String printModelNew = "";
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      HashMap hm = (HashMap) YHFormPraser.praserHTML2Dom(printModel);
      Map<String, Map> m1 = YHFormPraser.praserHTML2Arr(hm);
      printModelNew = YHFormPraser.toShortString(m1, printModel, YHFlowFormReglex.CONTENT);
      form2.setPrintModelShort(printModelNew);
      form2.setFormId(0);
      form2.setVersionNo(form.getVersionNo() + 1);
      form2.setVersionTime(new Date());
      //创建新的数据
      orm.saveSingle(dbConn, form2);
      YHFormVersionLogic logic =new YHFormVersionLogic();
      int newId = logic.getMaxFormId(dbConn);
      YHFlowFormUtility ffu = new YHFlowFormUtility();
      ffu.cacheForm(newId, dbConn);
      
      YHWorkflowSave2DataTableLogic logic4 = new YHWorkflowSave2DataTableLogic();
      
      //创建表结构
      String flowTypes = logic4.getFlowTypeByFormId(dbConn, seqId + "");
      logic4.createFlowFormTable(dbConn, newId, flowTypes);
      //更新flow关联
      logic4.updateFlowTypeByFormId(dbConn, seqId, newId);
      //修改formId
      logic4.updateFormTypeByFormId(dbConn, seqId, newId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, newId + "");
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 采用老版本占新号
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String genFormVersion1(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      int id = Integer.parseInt(seqId);
      
      YHORM orm = new YHORM();
      YHFlowFormType form = (YHFlowFormType) orm.loadObjSingle(dbConn, YHFlowFormType.class, id);
      
      YHFlowFormType form2 = new YHFlowFormType();
      form2.setCss(form.getCss());
      form2.setScript(form.getScript());
      form2.setFormName(form.getFormName());
      form2.setItemMax(form.getItemMax());
      form2.setPrintModel(form.getPrintModel());
      form2.setPrintModelShort(form.getPrintModelShort());
      form2.setFormId(Integer.parseInt(seqId));
      form2.setVersionNo(form.getVersionNo());
      form2.setVersionTime(form.getVersionTime());
      orm.saveSingle(dbConn, form2);
      
      YHFormVersionLogic logic =new YHFormVersionLogic();
      int newId = logic.getMaxFormId(dbConn);
      
      //logic.updateFormId(dbConn, newId, id);
      logic.updateFormItem(dbConn , id , newId);
      logic.changeTableName(dbConn, id, newId);
      
      String printModel = request.getParameter("printModel");
      String itemMax = request.getParameter("itemMax");
      YHFlowFormLogic logic2 =new YHFlowFormLogic();
      printModel = printModel.replaceAll("\"", "\\\\\"");
      printModel = printModel.replaceAll("\r\n", "");
      logic2.updateForm(dbConn, id , printModel , itemMax , false);
      
      logic.updateFormVersion(dbConn, id, 0, form.getVersionNo() + 1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String replay1(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String formId = request.getParameter("formId");
      int id = Integer.parseInt(formId);
      
      YHORM orm = new YHORM();
      YHFlowFormType form3 = (YHFlowFormType) orm.loadObjSingle(dbConn, YHFlowFormType.class, Integer.parseInt(seqId));
      YHFlowFormType form = (YHFlowFormType) orm.loadObjSingle(dbConn, YHFlowFormType.class, id);
      YHFlowFormType form2 = new YHFlowFormType();
      form2.setCss(form.getCss());
      form2.setScript(form.getScript());
      form2.setFormName(form.getFormName());
      form2.setItemMax(form.getItemMax());
      form2.setPrintModel(form.getPrintModel());
      form2.setPrintModelShort(form.getPrintModelShort());
      form2.setFormId(id);
      form2.setVersionNo(form.getVersionNo());
      form2.setVersionTime(form.getVersionTime());
      orm.saveSingle(dbConn, form2);
      
      YHFormVersionLogic logic =new YHFormVersionLogic();
      int newId = logic.getMaxFormId(dbConn);
      
      //logic.updateFormId(dbConn, newId, id);
      logic.updateFormItem(dbConn , id , newId);
      logic.changeTableName(dbConn, id, newId);
      
      String printModel = form3.getPrintModel();
      String itemMax = form3.getItemMax() + "";
      YHFlowFormLogic logic2 =new YHFlowFormLogic();
      logic2.updateForm(dbConn, id , printModel , itemMax , false);
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      ffl.updateFlowForm(dbConn, id, new String[]{"CSS","SCRIPT"},  new String[]{form3.getCss() , form3.getScript()});
      logic.updateFormVersion(dbConn, id, 0, form.getVersionNo() + 1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String replay(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String formId = request.getParameter("formId");
      int id = Integer.parseInt(formId);
      
      YHORM orm = new YHORM();
      YHFlowFormType form3 = (YHFlowFormType) orm.loadObjSingle(dbConn, YHFlowFormType.class, Integer.parseInt(seqId));
      YHFlowFormType form = (YHFlowFormType) orm.loadObjSingle(dbConn, YHFlowFormType.class, id);
      YHFlowFormType form2 = new YHFlowFormType();
      form2.setCss(form3.getCss());
      form2.setScript(form3.getScript());
      form2.setFormName(form3.getFormName());
      form2.setItemMax(form3.getItemMax());
      form2.setPrintModel(form3.getPrintModel());
      form2.setPrintModelShort(form3.getPrintModelShort());
      form2.setFormId(0);
      form2.setDeptId(form3.getDeptId());
      form2.setVersionNo(form.getVersionNo() + 1);
      form2.setVersionTime(new Date());
      orm.saveSingle(dbConn, form2);
      
      YHFormVersionLogic logic =new YHFormVersionLogic();
      int newId = logic.getMaxFormId(dbConn);
      YHFlowFormUtility ffu = new YHFlowFormUtility();
      ffu.cacheForm(newId, dbConn);
      
      YHWorkflowSave2DataTableLogic logic4 = new YHWorkflowSave2DataTableLogic();
      //创建表结构
      String flowTypes = logic4.getFlowTypeByFormId(dbConn, formId + "");
      logic4.createFlowFormTable(dbConn, newId, flowTypes);
      //更新flow关联
      logic4.updateFlowTypeByFormId(dbConn, formId, newId);
      //修改formId
      logic4.updateFormTypeByFormId(dbConn, formId, newId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
      request.setAttribute(YHActionKeys.RET_DATA, newId +"");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
