package yh.core.oaknow.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.logic.YHOAKnowAnswerLogic;
import yh.core.oaknow.logic.YHOAKnowManageLogic;
import yh.core.oaknow.util.YHAjaxUtil;
import yh.core.oaknow.util.YHPageUtil;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.util.YHUtility;

/**
 * 知道管理
 * @author qwx110
 *
 */
public class YHOAKnowManageAct{
  private YHOAKnowManageLogic knowLogic = new YHOAKnowManageLogic();
  private YHPageUtil pu = new YHPageUtil();
  private YHOAKnowAnswerLogic oaLogic = new YHOAKnowAnswerLogic();
  /**
   * 跳转到知道管理页面
   * @param request
   * @param response
   * @throws Exception
   */
  public String gotoManage(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
    try{
      dbConn = requestDbConn.getSysDbConn();
      String ask = request.getParameter("ask");
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String status = request.getParameter("status");
      String crrNo = request.getParameter("currNo");//当前的页码
      int currNo = 1;
      if(YHStringUtil.isEmpty(crrNo)){
        currNo = 1;
      }else{
        currNo = Integer.parseInt(crrNo);
      }      
      int count = knowLogic.getCount(dbConn, status, startTime, endTime, ask);
      pu.setElementsCount(count);
      pu.setPageSize(10);
      pu.setCurrentPage(currNo);
      List<YHOAAsk> askList  = new ArrayList<YHOAAsk>();
      askList =  knowLogic.getAsks(dbConn, pu, status, startTime, endTime, ask);
      request.setAttribute("askList", askList);
      request.setAttribute("page", pu);
      request.setAttribute("ask", ask);
      request.setAttribute("startTime", startTime);
      request.setAttribute("endTime", endTime);
      if(YHStringUtil.isEmpty(status)){
        request.setAttribute("status", "");
      }else{
        request.setAttribute("status", status);
      }
      
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/core/oaknow/panel/allask.jsp";
  }
  /**
   * 推荐状态和非推荐状态
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String toTuiJian(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    try{
      dbConn = requestDbConn.getSysDbConn();
      int askId = Integer.parseInt(request.getParameter("askId"));
      int flag  = Integer.parseInt(request.getParameter("flag"));
      int id = 0;
      if(flag == 1){
        id = oaLogic.tuiJianStatus(dbConn, askId, 1);  //推荐状态
      }else if(flag == 0){
        id = oaLogic.tuiJianStatus(dbConn, askId, 0);  //取消推荐 状态  
      }   
      YHAjaxUtil.ajax(id, response);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return null;
  }
 /**
  * 删除答案 
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
  public String deleteAsk(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    try{
      dbConn = requestDbConn.getSysDbConn();
      String askId = request.getParameter("askId");
      String status = request.getParameter("status");
      int id = knowLogic.deleteAsk(dbConn, Integer.parseInt(askId), Integer.parseInt(status));
      YHAjaxUtil.ajax(id, response);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return null;
  }
}
