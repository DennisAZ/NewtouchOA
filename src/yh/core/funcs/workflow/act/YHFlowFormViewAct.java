package yh.core.funcs.workflow.act;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowFormItem;
import yh.core.funcs.workflow.logic.YHFlowFormLogic;
import yh.core.funcs.workflow.praser.YHFormPraser;
import yh.core.funcs.workflow.praser.YHPraseData2FormView;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;

public class YHFlowFormViewAct{
  
  private static Logger log = Logger
    .getLogger("yh.core.funcs.workflow.act.YHFlowFormViewAct");
  public String showFormView(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      String seqIdStr = request.getParameter("seqId");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      int seqId = Integer.parseInt(seqIdStr);
      Map map = ffl.selectFlowForm(dbConn, seqId, new String[]{"PRINT_MODEL","PRINT_MODEL_SHORT","FORM_NAME"});
      String html = (String) map.get("PRINT_MODEL");
      String shortModel = (String) map.get("PRINT_MODEL_SHORT");
      String formName = (String) map.get("FORM_NAME");
      if(shortModel == null){
        shortModel = "";
      }
      if(html == null){
        html = "";
      }
      StringBuffer data = YHFormPraser.toJson(html);
      StringBuffer newdata = new StringBuffer();
      newdata.append("{'seqId':").append(seqId).append(",")
        .append("'printModel' :").append(data).append(",")
        .append("'printModelShort':\"").append(shortModel).append("\"").append(",")
        .append("'formName':\"").append(formName).append("\"").append("}");
      //System.out.println(newdata.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取得数据");
      request.setAttribute(YHActionKeys.RET_DATA, newdata.toString());
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getIpAdd(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String ip = request.getRemoteAddr();
    if("".equals(ip) || ip == null){
      ip = "127.0.0.1";
    }
    String data = "{'ip':" + "\"" + ip +"\"}";
    //System.out.println(data);
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, "成功取得数据");
    request.setAttribute(YHActionKeys.RET_DATA, data);
    return "/core/inc/rtjson.jsp";
  }
  public String getFormView(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      String seqIdStr = request.getParameter("seqId");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      int seqId = Integer.parseInt(seqIdStr);
      Map map = ffl.selectFlowForm(dbConn, seqId, new String[]{"PRINT_MODEL","PRINT_MODEL_SHORT","FORM_NAME" , "SCRIPT" , "CSS"});
      String html = (String) map.get("PRINT_MODEL");
      String shortModel = (String) map.get("PRINT_MODEL_SHORT");
      String formName = (String) map.get("FORM_NAME");
      String js = (String)map.get("SCRIPT");
      String css = (String)map.get("CSS");
      if(shortModel == null){
        shortModel = "";
      }
      if (js == null) {
        js = "";
      }
      if (css == null) {
        css = "";
      }
      if(html == null){
        html = "";
      }
      StringBuffer sb = new StringBuffer();
      Map formItemQuery = new HashMap();
      formItemQuery.put("FORM_ID", seqId);
      YHORM orm = new YHORM();
      List<YHFlowFormItem> list = orm.loadListSingle(dbConn, YHFlowFormItem.class , formItemQuery);
      YHPraseData2FormView pf = new YHPraseData2FormView();
      String form =  pf.parseForm(loginUser, shortModel, list ,request.getRemoteAddr(), dbConn);
      form = form.replaceAll("\'", "\\\\'");
      form = form.replaceAll("\\\n", "");
      js = js.replaceAll("\'", "\\\\'");
      js = js.replaceAll("[\n-\r]", "");
      css = css.replaceAll("\'", "\\\\'");
      css = css.replaceAll("[\n-\r]", "");
      
      sb.append("{js:'"+ js +"'");
      sb.append(",css:'" + css + "'");
      sb.append(",form:'"+ form +"'");
      sb.append("}");
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取得数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getAllSeal(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFlowFormLogic logic = new YHFlowFormLogic();
      String result = logic.getSeals(loginUser.getSeqId(), dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取得数据");
      request.setAttribute(YHActionKeys.RET_DATA, result);
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getSeal(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String sId = request.getParameter("id");
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowFormLogic logic = new YHFlowFormLogic();
      byte[] data = logic.getSealData(Integer.parseInt(sId), dbConn);
      //response.setCharacterEncoding("GB2312");
      OutputStream out = null;
      try {
        out = response.getOutputStream();
        out.write(data, 0, data.length);
        out.flush();
      }catch(Exception ex2) {
        throw ex2;
      }finally {
        if (out != null) {
          out.close();
        }
      }
    }catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
}
