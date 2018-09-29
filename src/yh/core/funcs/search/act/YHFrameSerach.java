package yh.core.funcs.search.act;

import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.search.logic.YHFrameSearchLogic;
import yh.core.funcs.system.url.data.YHUrl;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;

/**
 * 
 * @author Think
 * 搜索
 */
public class YHFrameSerach {
  /**
   * 搜索用户信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String searchUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      String keyWord = request.getParameter("keyWord");
      String pageStartStr = request.getParameter("pageStart");
      String pageNumStr = request.getParameter("pageNum");
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFrameSearchLogic fsl = new YHFrameSearchLogic();
      String data = fsl.getUserInfo(dbConn, keyWord, Integer.valueOf(pageStartStr), Integer.valueOf(pageNumStr));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取得数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 搜索email数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String searchEmail(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      String keyWord = request.getParameter("keyWord");
      String pageStartStr = request.getParameter("pageStart");
      String pageNumStr = request.getParameter("pageNum");
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFrameSearchLogic fsl = new YHFrameSearchLogic();
      String data = fsl.getEmailInfo(dbConn, keyWord, Integer.valueOf(pageStartStr), Integer.valueOf(pageNumStr),person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取得数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 搜索公告通知
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String searchNotify(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      String keyWord = request.getParameter("keyWord");
      String pageStartStr = request.getParameter("pageStart");
      String pageNumStr = request.getParameter("pageNum");
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFrameSearchLogic fsl = new YHFrameSearchLogic();
      String data = fsl.getNotifyInfo(dbConn, keyWord, Integer.valueOf(pageStartStr), Integer.valueOf(pageNumStr),person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取得数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 通讯薄
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String searchAddress(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      String keyWord = request.getParameter("keyWord");
      String pageStartStr = request.getParameter("pageStart");
      String pageNumStr = request.getParameter("pageNum");
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFrameSearchLogic fsl = new YHFrameSearchLogic();
      String data = fsl.getAddressInfo(dbConn, keyWord, Integer.valueOf(pageStartStr), Integer.valueOf(pageNumStr),person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取得数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 文件柜
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String searchFileFolder(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      String keyWord = request.getParameter("keyWord");
      String pageStartStr = request.getParameter("pageStart");
      String pageNumStr = request.getParameter("pageNum");
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFrameSearchLogic fsl = new YHFrameSearchLogic();
      String data = fsl.getFileFloderInfo(dbConn, keyWord, Integer.valueOf(pageStartStr), Integer.valueOf(pageNumStr),person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取得数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 工作流搜索
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String searchWorkFlow(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      String keyWord = request.getParameter("keyWord");
      String pageStartStr = request.getParameter("pageStart");
      String pageNumStr = request.getParameter("pageNum");
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFrameSearchLogic fsl = new YHFrameSearchLogic();
      String data = fsl.getWorkFlowInfo(dbConn, keyWord, Integer.valueOf(pageStartStr), Integer.valueOf(pageNumStr),person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取得数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
