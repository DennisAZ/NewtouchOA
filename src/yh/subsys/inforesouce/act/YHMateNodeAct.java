package yh.subsys.inforesouce.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.exps.YHInvalidParamException;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.subsys.inforesouce.data.YHMateNode;
import yh.subsys.inforesouce.logic.YHMateNodeLogic;
import yh.subsys.inforesouce.util.YHAjaxUtil;
/**
 * 
 * @author qwx110
 *
 */

public class YHMateNodeAct{
  
  private YHMateNodeLogic nodeLogic = new YHMateNodeLogic();
  
  /**
   * 保存选择的树中选择的要显示的节点
   * @param request
   * @param response
   * @return
   * @throws Exception 
   * @throws YHInvalidParamException 
   */
  public String saveNode(HttpServletRequest request, HttpServletResponse response) throws YHInvalidParamException, Exception{
    String tagname = request.getParameter("tagname");
    String nodes = request.getParameter("nodes");
    String nodeType = request.getParameter("nodeType");
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER"); 
      YHMateNode node = new YHMateNode();
      node.setNodes(nodes);
      node.setTagName(tagname);
      node.setUserId(user.getSeqId());
      int ok = nodeLogic.saveAjax(dbConn, node, nodeType);
      YHAjaxUtil.ajax(ok, response);
    } catch (Exception ex){
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;   
    }    
    return null;
  }
  
  /**
   * 查询出某个用户下的所有的自定义标签
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findTagNameAjax(HttpServletRequest request, HttpServletResponse response) throws  Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;    
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER"); 
      String nodes = nodeLogic.tagName(dbConn, user, "1");
      //YHOut.println(nodes);
      YHAjaxUtil.ajax(nodes, response);
    } catch (Exception ex){
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return null;
  }
  
  /**
   * 删除标签
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteTagName(HttpServletRequest request, HttpServletResponse response)throws  Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;    
    try{
      dbConn = requestDbConn.getSysDbConn();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      int ok  = nodeLogic.deleteTagName(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"删除成功！");       
    } catch (Exception ex){
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
}
