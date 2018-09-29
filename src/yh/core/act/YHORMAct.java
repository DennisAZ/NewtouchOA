package yh.core.act;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHORMAct {
  /**
   * log
   */
  private static Logger log = Logger
                                .getLogger("yh.core.act.YHORMAct");

  public String loadData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    //System.out.println(YHFOM.buildList(request.getParameterMap()));
    return null;
  }

  public String update(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map paramMap = new HashMap(request.getParameterMap());
      String mainTable = ((String[]) paramMap.get("mainTable"))[0];
      paramMap.remove("mainTable");
      Map formInfo = YHFOM.buildMap(paramMap);
      YHORM orm = new YHORM();
      orm.updateComplex(dbConn, mainTable, formInfo);
      String ms = "";
      for (Object obj : paramMap.keySet()) {
        String[] values = (String[]) paramMap.get(obj);
        ms += obj + " = " + values[0] + "  ";
      }
      //System.out.println("maps :" + ms);
      //System.out.println("mm : " + formInfo);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载代码失败" + ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String delete(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map paramMap = request.getParameterMap();
      String mainTable = ((String[]) paramMap.get("mainTable"))[0];
      paramMap.remove("mainTable");
      Map formInfo = YHFOM.buildMap(paramMap);
      YHORM orm = new YHORM();
      orm.deleteComplex(dbConn, mainTable, formInfo);
      String ms = "";
      for (Object obj : paramMap.keySet()) {
        String[] values = (String[]) paramMap.get(obj);
        ms += obj + " = " + values[0] + "  ";
      }
      //System.out.println("maps :" + ms);
      //System.out.println("mm : " + formInfo);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载代码失败" + ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public String add(HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      Map paramMap = new HashMap(request.getParameterMap());
      String mainTable = ((String[]) paramMap.get("mainTable"))[0];
      paramMap.remove("mainTable");
      Map fieldInfo = YHFOM.buildMap(paramMap);
      //System.out.println("map ========================================= "+fieldInfo);
      YHORM orm = new YHORM();
      orm.saveComplex(dbConn, mainTable, fieldInfo);
      String ms = "";
      for (Object obj : paramMap.keySet()) {
        String[] values = (String[]) paramMap.get(obj);
        ms += obj + " = " + values[0] + "  ";
      }
      //System.out.println("maps :" + ms);
      //System.out.println("mm : " + fieldInfo);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载代码失败" + ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
