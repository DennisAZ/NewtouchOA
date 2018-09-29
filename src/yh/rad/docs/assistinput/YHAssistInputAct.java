package yh.rad.docs.assistinput;

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

public class YHAssistInputAct {
  /**
   * 
   * log                                               
   */
  private Log log = null;
  private String[] ss = {"测试","测试222","测试2sss","bbbb","测试22bbbb","测试2ddddd22","rrreeee","测试rrrrr","eee333","eeeeee","测试bbbbbbb"};
  public String getData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      StringBuffer sb = new StringBuffer();
      String w = request.getParameter("w");
      
      sb.append(",lis:[");
      int count = 0;
      for (String s : ss) {
        if (s.contains(w)) {
          sb.append("{string:'"+s+"'},");
          count++;
        }
      }
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]}");
      sb.insert(0, "{count:" + count);
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
