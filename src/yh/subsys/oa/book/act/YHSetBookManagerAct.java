package yh.subsys.oa.book.act;

import java.sql.Connection;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.subsys.oa.book.data.YHBookManager;
import yh.subsys.oa.book.logic.YHSetBookManagerLogic;

/**
 * 设置管理员
 * @author qwx110
 *
 */
public class YHSetBookManagerAct{
   
  /**
   * 跳转到增加管理员index页面
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String index(HttpServletRequest request,  HttpServletResponse response) throws Exception{   
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();    
      YHSetBookManagerLogic  mLogic = new YHSetBookManagerLogic();
      List<YHBookManager> managers = mLogic.findAllManager(dbConn);
      request.setAttribute("managers", managers);
    }catch(Exception e){      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;      
    }
    return "/subsys/oa/book/setmanager/index.jsp";
  }
  
  /**
   * 增加新的管理员
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String addManager(HttpServletRequest request,  HttpServletResponse response) throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHSetBookManagerLogic  mLogic = new YHSetBookManagerLogic();
      YHBookManager aManager = new YHBookManager();
      String managerIds = request.getParameter("manage");
      String deptIds = request.getParameter("dept");
      aManager.setManagerId(managerIds);
      aManager.setManageDeptId(deptIds);
      int k =  mLogic.newManager(dbConn,aManager);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;    
    } 
    request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
    return "/yh/subsys/oa/book/act/YHSetBookManagerAct/index.act";
  }
  
  /**
   * 编辑管理员
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String editManager(HttpServletRequest request,  HttpServletResponse response) throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHSetBookManagerLogic  mLogic = new YHSetBookManagerLogic();
      YHBookManager aManager = mLogic.editManager(dbConn, Integer.parseInt(seqId));
      request.setAttribute("manager", aManager);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;    
    } 
   
    
    return "/subsys/oa/book/setmanager/addManager.jsp";
  }
  
  /**
   * 更新管理员
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String updateManager(HttpServletRequest request,  HttpServletResponse response) throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHSetBookManagerLogic  mLogic = new YHSetBookManagerLogic();
      YHBookManager aManager = new YHBookManager();
      String managerIds = request.getParameter("manage");
      String deptIds = request.getParameter("dept");
      String seqId = request.getParameter("seqId");
      aManager.setManagerId(managerIds);
      aManager.setManageDeptId(deptIds);
      aManager.setSeqId(Integer.parseInt(seqId));
      int k =  mLogic.updateManager(dbConn,aManager);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;    
    } 
    request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
    return "/yh/subsys/oa/book/act/YHSetBookManagerAct/index.act";    
  }
  
  /**
   * 删除管理员
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String delManager(HttpServletRequest request,  HttpServletResponse response) throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHSetBookManagerLogic  mLogic = new YHSetBookManagerLogic();
      String seqId = request.getParameter("seqId");
      int k = mLogic.delManager(dbConn, Integer.parseInt(seqId));
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    } 
    return "/yh/subsys/oa/book/act/YHSetBookManagerAct/index.act";  
  }
}
