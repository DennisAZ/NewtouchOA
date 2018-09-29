package yh.core.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.dto.YHCodeLoadParamSet;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.logic.YHCodeSelectLogic;
import yh.core.util.form.YHFOM;

public class YHSelectDataAct {
  /**
   * log                                   
   */
  private static Logger log = Logger.getLogger(YHSelectDataAct.class);
  
  /**
   * 加载下拉框数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String loadData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHCodeLoadParamSet paramSet = (YHCodeLoadParamSet)YHFOM.build(request.getParameterMap());

      YHCodeSelectLogic logic = new YHCodeSelectLogic();
      String rtSt = logic.loadSelectData(dbConn, paramSet);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "");
      request.setAttribute(YHActionKeys.RET_DATA, rtSt);
      //然后处理联动加载的数据
      
//      StringBuffer rtBuf = new StringBuffer("{");
//      String dsDef = request.getParameter("dsDef");
//      String[] dsDefArray = dsDef.split(";");
//      for (int i = 0; i < dsDefArray.length; i++) {
//        String[] tableDefArray = dsDefArray[i].split(".");
//        String propName = tableDefArray[0];
//        String tableName = tableDefArray[1];
//        String codeField = tableDefArray[2];
//        String nameField = tableDefArray[3];
//        String filter = null;
//        if (tableDefArray.length > 4) {
//          filter = tableDefArray[4];
//        }
//        StringBuffer jsonBuf = loadDataTable(dbConn, tableName, codeField, nameField, filter);
//        if (i > 0) {
//          rtBuf.append(",");
//        }
//        rtBuf.append(propName);
//        rtBuf.append(":");
//        rtBuf.append(jsonBuf);
//      }
//      rtBuf.append("}");
//      request.setAttribute(YHActionKeys.RET_DATA, rtBuf.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载代码失败" + ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
