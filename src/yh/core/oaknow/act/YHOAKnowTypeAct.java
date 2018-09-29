package yh.core.oaknow.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.oaknow.data.YHCategoriesType;
import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.logic.YHOAKnowMyPanelLogic;
import yh.core.oaknow.logic.YHOAKnowTypeLogic;
import yh.core.oaknow.util.YHPageUtil;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.util.YHOut;

/**
 * 分类查找
 * 
 * @author qwx110
 * 
 */
public class YHOAKnowTypeAct{
  private static Logger     log   = Logger.getLogger("yh.core.act.YHOAKnowAct");
  private YHOAKnowTypeLogic logic = new YHOAKnowTypeLogic();
  private YHOAKnowMyPanelLogic panelLogic = new YHOAKnowMyPanelLogic();
  private  YHPageUtil pu = new YHPageUtil();
  public String findTypeAjax(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      dbConn = requestDbConn.getSysDbConn();
      int typeId = Integer.parseInt(request.getParameter("typeId").trim());
      int flag = Integer.parseInt(request.getParameter("flag").trim());        //区分；：1 全部，2 已解决，0 未解决       
      int count = logic.findAllCount(dbConn, typeId, flag);
      String no  = request.getParameter("currNo");
      int crrNo = 1;
      if(YHStringUtil.isNotEmpty(no)){
        crrNo = Integer.parseInt(no);
      }
      pu.setElementsCount(count);
      pu.setPageSize(10);
      pu.setCurrentPage(crrNo);
      String data = logic.findAskByType(dbConn, typeId, flag, pu);     
      //request.setAttribute(YHActionKeys.RET_DATA, data);
      PrintWriter pw = response.getWriter();
      String rtData = "{rtData:"+data+", currNo:"+ crrNo+", totalNo:"+ pu.getPagesCount() +"}";
      //YHOut.println(rtData+"&&&&&&&&&&&&&&&&&&&&");
      pw.println(rtData);
      pw.flush();
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;   
    }
    return null;
  }
  /**
   * 根据分类id查找
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String findType(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    YHCategoriesType type = new YHCategoriesType();
    try{
      dbConn = requestDbConn.getSysDbConn();
      int typeId = Integer.parseInt(request.getParameter("typeId").trim());
      int parentId = Integer.parseInt(request.getParameter("parentId").trim());
      //YHOut.println("typeId="+typeId+"---------------"+"parentId="+parentId);
      type = logic.findTypeByTypeId(dbConn, typeId, parentId);// 取分类
     // List<YHOAAsk> askList = logic.findAllByTypeId(dbConn, typeId, 1); 
     // YHOut.println("=============================="+askList.size());
      request.setAttribute("aType", type); // 存分类
     // request.setAttribute("askList", askList);  //某个分类下的所有的问题 
      String showFlag = request.getParameter("showFlag");
      if(YHStringUtil.isNotEmpty(showFlag)){
        request.setAttribute("showFlag", showFlag);
      }
      request.setAttribute("selfId", typeId);
      String oaName = panelLogic.findOAName(dbConn).trim();        
      request.setAttribute("oaName", oaName);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/core/oaknow/oaknowshowType.jsp";
  }
}
