package yh.rad.docs.act;


import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class YHTestPageAction {
  private static Logger log = Logger.getLogger(YHTestPageAction.class);
  
  /**
   * 取得页面数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap());
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
          queryParam,
          //"select SEQ_ID,FROM_ID,SEND_TIME,ATTACHMENT_NAME from EMAIL_BODY where SEQ_ID=0");
          "select SEQ_ID,FROM_ID,SEND_TIME,ATTACHMENT_NAME , ATTACHMENT_ID from oa_email_body");
      
      PrintWriter pw = response.getWriter();
      pw.println(pageDataList.toJson());
      pw.flush();
      
      return null;
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
  }
}
