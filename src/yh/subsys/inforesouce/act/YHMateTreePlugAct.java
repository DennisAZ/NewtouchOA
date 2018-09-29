package yh.subsys.inforesouce.act;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.subsys.inforesouce.data.YHMateType;
import yh.subsys.inforesouce.logic.YHMateTreePlugLogic;
import yh.subsys.inforesouce.util.YHStringUtil;

/**
 * 元数据树形插件
 * @author qwx110
 *
 */
public class YHMateTreePlugAct{
  private YHMateTreePlugLogic plugLogic = new YHMateTreePlugLogic();
  
  /**
   * 取得元数据树状列表数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findMates(HttpServletRequest request, HttpServletResponse response)throws  Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    String tree = "";
    try{
      dbConn = requestDbConn.getSysDbConn();
      String contextPath = request.getContextPath();
      int id = 0;
      String idStr = request.getParameter("id");
      if(YHStringUtil.isNotEmpty(idStr)){
        id = Integer.parseInt(idStr);
      }
      tree = plugLogic.findMateTree(dbConn, id, contextPath);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, tree);
    } catch (Exception e){     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 点击节点后，右侧显示的内容
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getMateIndent(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String id = request.getParameter("id");
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    try{
      Connection dbConn = null;
      dbConn = requestDbConn.getSysDbConn();
      List<YHMateType> mates = plugLogic.findMateList(dbConn);
      StringBuffer sb = new StringBuffer();
      if (id == null || "".equals(id)) {
        sb = plugLogic.getMateJson(mates, 0);
      } else {
        sb = plugLogic.getMateJson(mates, Integer.parseInt(id));
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "get Success");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 默认显示的树右侧
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findDefMate(HttpServletRequest request, HttpServletResponse response)throws Exception {
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    StringBuffer sb = new StringBuffer();
    try{
      dbConn = requestDbConn.getSysDbConn();
      List<YHMateType> mates = plugLogic.findMateList(dbConn);    
      sb = plugLogic.getMateJson(mates, 0);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "get Success");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
}
