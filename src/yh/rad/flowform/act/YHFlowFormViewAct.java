package yh.rad.flowform.act;

import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.rad.flowform.logic.YHFlowFormLogic;
import yh.rad.flowform.praser.YHFormPraser;

public class YHFlowFormViewAct{
  
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
}
