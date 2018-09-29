package yh.subsys.oa.hr.recruit.analysis.act;
import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.subsys.oa.hr.recruit.analysis.logic.YHHrRecruitAnalysisLogic;

public class YHHrRecruitAnalysisAct {
  private YHHrRecruitAnalysisLogic logic = new YHHrRecruitAnalysisLogic();
  public static final String attachmentFolder = "hr";
 
  
  /**获取分析数据
   * @param request;
   * @param response;
   * 
   * */
  public String getAnalysis(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String sumField = request.getParameter("sumField");
      String position = request.getParameter("position");
      String ageRange = request.getParameter("ageRange");
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getAnalysisLogic(dbConn, sumField, position, ageRange, person);
      data = "{\"data\":\""+data+"\"}";
     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  
  
}
