package yh.subsys.inforesouce.act;



import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHOut;
import yh.subsys.inforesouce.logic.YHMateValueLogic;

/**
 * 值域
 * @author qwx110
 *
 */
public class YHMateValueAct{
  private YHMateValueLogic mvlogic = new YHMateValueLogic();
/**
 * 删除值域
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
  public String deleteMateValue(HttpServletRequest request, HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    dbConn = requestDbConn.getSysDbConn();
    String seqIda = request.getParameter("seqIda");
    String seqIdb = request.getParameter("seqIdb");
    String number = request.getParameter("number");
    try{
      int falg = mvlogic.updateMate(dbConn, Integer.parseInt(seqIda), Integer.parseInt(seqIdb));
      //YHOut.println(falg);
    } catch (Exception e){   
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/yh/subsys/inforesouce/act/YHMateElementAct/selectvalue.act?seqid="+seqIdb+"&&number="+number;
  }  
}
