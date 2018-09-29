package yh.core.funcs.setdescktop.log.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.util.form.YHFOM;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.setdescktop.fav.logic.YHFavLogic;
import yh.core.funcs.system.url.data.YHUrl;
import yh.core.funcs.system.url.logic.YHUrlLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.menu.data.YHSysMenu;

public class YHSelfLogAct {
  private YHFavLogic logic = new YHFavLogic();
  
  public String getPage(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 

    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      String sql = "";
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("sqlserver")) {
        sql = "select top 20 SEQ_ID" +
        ",(select USER_NAME from PERSON where SEQ_ID = S.USER_ID)" +
        ",TIME" +
        ",IP" +
        ",TYPE" +
        ",REMARK" +
        " from oa_sys_log S" +
        " where USER_ID =" + user.getSeqId() +
        " order by TIME desc";
      }
      else if (dbms.equals("mysql")){
        sql = "select SEQ_ID" +
        ",(select USER_NAME from PERSON where SEQ_ID = S.USER_ID)" +
        ",TIME" +
        ",IP" +
        ",TYPE" +
        ",REMARK" +
        " from oa_sys_log S" +
        " where USER_ID =" + user.getSeqId() +
        " order by TIME desc" +
        " limit 20";
      }else if (dbms.equals("oracle")){
        sql = "select SEQ_ID" +
        ",(select USER_NAME from PERSON where SEQ_ID = S.USER_ID)" +
        ",TIME" +
        ",IP" +
        ",TYPE" +
        ",REMARK" +
        " from oa_sys_log S" +
        " where USER_ID =" + user.getSeqId() +
        " order by TIME desc";
      }
      
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap()); 
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, 
      queryParam, 
      sql);
      
      PrintWriter pw = response.getWriter(); 
      pw.println(pageDataList.toJson()); 
      pw.flush(); 
  
      return null; 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
  }
}