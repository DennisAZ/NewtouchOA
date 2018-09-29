package yh.core.funcs.youhua.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.youhua.logic.YHYouhuaLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHYouHuaAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.youhua.act.YHYouHuaAct");
  public String createIndex(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHYouhuaLogic logic = new YHYouhuaLogic();
      logic.createIndex(dbConn, request.getRealPath("/"));
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
