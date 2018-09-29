package yh.rad.docs.menu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx4j.log.Log;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHDBUtility;

public class YHMenuAct {
  /**
   * log                                               
   */
  private Log logc = null;
  public String getData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
       StringBuffer sb = new StringBuffer();
       sb.append("[{name:'新增',action:add,icon:imgPath + '/cmp/rightmenu/addStep.gif' , extData:\"menu1\"}"
                      + ",{name:'修改',action:update,icon:imgPath + '/cmp/rightmenu/addStep.gif', extData:\"menu1\"}"
                      + ",{name:'删除',action:del,icon:imgPath + '/cmp/rightmenu/addStep.gif', extData:\"menu1\"}]");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
        request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
