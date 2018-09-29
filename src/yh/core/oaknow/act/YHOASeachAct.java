package yh.core.oaknow.act;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.logic.YHOAKnowMyPanelLogic;
import yh.core.oaknow.logic.YHOASeachLogic;
import yh.core.oaknow.util.YHPageUtil;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.oaknow.util.YHEscape;
/**
 * oa知道搜索
 * @author qwx110
 *
 */

public class YHOASeachAct{
  
  private YHOASeachLogic seachLogic = new YHOASeachLogic();
  private YHOAKnowMyPanelLogic panelLogic = new YHOAKnowMyPanelLogic();
  private YHPageUtil pu = new YHPageUtil();
 /**
  * 搜索与name相关问题并分页
  * @param request
  * @param response
  * @return
 * @throws Exception 
  */
 @SuppressWarnings("deprecation")
public String findResolveStatus(HttpServletRequest request, HttpServletResponse response) throws Exception{  
     Connection dbConn = null;
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
     List<YHOAAsk> askList = null;
     int currNo = 1;
     try{
      dbConn = requestDbConn.getSysDbConn(); 
      String crrNo = request.getParameter("currNo").trim();
      if(YHStringUtil.isEmpty(crrNo)){
        currNo = 1;
      }else{
        currNo = Integer.parseInt(crrNo);
      }
      String askName = request.getParameter("question");//搜索题目
     // String askName = java.net.URLDecoder.decode(ask, "utf-8");
      String flag = request.getParameter("flag").trim();
      //askName = StringUtil.toChange(askName);
      int total = 1;
      if("resolve".equals(flag)){
        total = seachLogic.findAllAskResolvedCount(dbConn, askName);//解决的问题        
      }else if("noresolve".equals(flag)){
        total = seachLogic.findAskNoResolvedCount(dbConn, askName);        
      }     
      pu.setCurrentPage(currNo);
      pu.setPageSize(10);
      pu.setElementsCount(total); 
      if("resolve".equals(flag)){
        askList = seachLogic.findAllAskResolved(dbConn, askName, pu);
      }else if("noresolve".equals(flag)){
        askList = seachLogic.findAllAskNoResolved(dbConn, askName, pu);        
      } 
      String oaName = panelLogic.findOAName(dbConn).trim();        
      request.setAttribute("oaName", oaName);
      request.setAttribute("askList", askList);
      request.setAttribute("page", pu);
      request.setAttribute("askName", YHStringUtil.toChange(askName));
      request.setAttribute("flag", "'"+flag+"'");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }   
    return "/core/oaknow/oaseach.jsp";
 }
}
