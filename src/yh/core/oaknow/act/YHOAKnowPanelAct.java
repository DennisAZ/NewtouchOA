package yh.core.oaknow.act;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.oaknow.data.YHCategoriesType;
import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.logic.YHOAKnowAnswerLogic;
import yh.core.oaknow.logic.YHOAKnowLogic;
import yh.core.oaknow.logic.YHOAKnowMyPanelLogic;
import yh.core.oaknow.util.YHAjaxUtil;
import yh.core.oaknow.util.YHPageUtil;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.oaknow.util.YHOAToJsonUtil;
import yh.core.util.YHOut;

/**
 * oa知道管理面板
 * @author qwx110
 *
 */
public class YHOAKnowPanelAct{
  
  private YHOAKnowMyPanelLogic panelLogic = new YHOAKnowMyPanelLogic();
  private YHPageUtil pu = new YHPageUtil();
  private YHOAKnowAnswerLogic anLogic = new YHOAKnowAnswerLogic();
  private  YHOAKnowLogic oaLogicIndex = new YHOAKnowLogic();
  private YHSysLogLogic logLogic = new YHSysLogLogic();
  /**
   * oa知道管理面板左边的frame
   * @param request
   * @param response
   * @return
   */
  public String oAKonwLeftPanel(HttpServletRequest request,
      HttpServletResponse response){    
    YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    request.setAttribute("user", user);
    return "/core/oaknow/panel/oaleftpanel.jsp";
  }
  
  /**
   * 跳转到管理面板
   * @param request
   * @param response
   * @return
   */
  public String mainPanel(HttpServletRequest request,HttpServletResponse response){   
    return "/core/oaknow/panel/oaknowpanel.jsp";
  }
  
  /**
   * oa知道管理面板顶部的frame
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String topPanel(HttpServletRequest request,HttpServletResponse response) throws Exception{
    YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    request.setAttribute("user", user);
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      dbConn = requestDbConn.getSysDbConn();
      String oAName = panelLogic.findOAName(dbConn);
      //YHOut.println("--------"+oAName);
      request.setAttribute("oAName", oAName);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }   
    return "/core/oaknow/panel/oaknowtopbar.jsp";
  }
  
  /**
   * oa知道管理面板右边的我的问题管理
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String leftPanel(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
    return findMyAsk(request, response);
  }
  
  /**
   * oa知道管理面板右边的我的问题管理
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String findMyAsk(HttpServletRequest request,HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String crrNo = request.getParameter("currNo");//当前的页码
      int currNo = 1;
      if(YHStringUtil.isEmpty(crrNo)){
        currNo = 1;
      }else{
        currNo = Integer.parseInt(crrNo);
      }      
      int total = panelLogic.findMyAskCount(dbConn, user);
      pu.setCurrentPage(currNo);
      pu.setElementsCount(total);
      pu.setPageSize(10);
      List<YHOAAsk> asks = panelLogic.findMyAsks(dbConn, user, pu);
      request.setAttribute("asks", asks);
      request.setAttribute("page", pu);
    }catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }    
    return "/core/oaknow/panel/oamyask.jsp";
  }
  
  /**
   * oa管理面板的编辑我的问题
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String editMyAsk(HttpServletRequest request,HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHOAAsk ask = new YHOAAsk();
      String askName = request.getParameter("ask");
      String typeId = request.getParameter("categorieid");
      String content = request.getParameter("content");
      String tab = request.getParameter("tab");
      ask.setAsk(askName);
      ask.setAskComment(content);
      ask.setTypeId(Integer.parseInt(typeId));
      ask.setReplyKeyWord(tab);
      ask.setSeqId(Integer.parseInt(request.getParameter("seqId")));
      ask.setCommend(Integer.parseInt(request.getParameter("commend")));
      int flag = anLogic.changeAsk(dbConn, ask);
      if(flag != 0){
        return findMyAsk(request, response);
      }
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return null;
  }
  
  /**
   * 跳转到编辑页面
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String goToEditPage (HttpServletRequest request,HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      dbConn = requestDbConn.getSysDbConn();
      String askId = request.getParameter("askId");
      YHOAAsk ask = anLogic.findAskStatus(dbConn, Integer.parseInt(askId));
      List<YHCategoriesType>  types = oaLogicIndex.findKind(dbConn);
      
      request.setAttribute("toJson", YHOAToJsonUtil.toJsonTwo((types)));
      request.setAttribute("ask", ask);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/core/oaknow/panel/oapaneledit.jsp";
  }
  /**
   * 删除我的问题
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteAsk (HttpServletRequest request,HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      dbConn = requestDbConn.getSysDbConn();
      String askId = request.getParameter("askId");
      int flag = anLogic.deleteMyAsk(dbConn, Integer.parseInt(askId));
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return findMyAsk(request, response);
  }
  
  /**
   * 我参与过的问题
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findMyReferenceAsks (HttpServletRequest request,HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String crrNo = request.getParameter("currNo");//当前的页码
      int currNo = 1;
      if(YHStringUtil.isEmpty(crrNo)){
        currNo = 1;
      }else{
        currNo = Integer.parseInt(crrNo);
      }      
      int total = panelLogic.findMyReferenceAsksCount(dbConn, user);
      pu.setCurrentPage(currNo);
      pu.setElementsCount(total);
      pu.setPageSize(10);
      List<YHOAAsk> askList = panelLogic.findMyReferenceAsks(dbConn, user, pu);
      request.setAttribute("asks", askList);
      request.setAttribute("page", pu);
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/core/oaknow/panel/myreference.jsp";
  }
  /**
   * 跳转到系统设置
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findOAName(HttpServletRequest request,HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      dbConn = requestDbConn.getSysDbConn();
      String oaName = panelLogic.findOAName(dbConn).trim();
      request.setAttribute("oaName", oaName);
      request.setAttribute("flag", "0");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      e.printStackTrace();
    }
    return "/core/oaknow/panel/oachangename.jsp";
  }
  
  /**
   * 保存修改的oa名字
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String saveOaName(HttpServletRequest request,HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      dbConn = requestDbConn.getSysDbConn();
      String oaName = request.getParameter("oaName").trim();
      int flag = panelLogic.updateOrSave(dbConn, oaName);
      if(flag != 0){
        request.setAttribute("flag", "1");
        request.setAttribute("oaName", oaName);
      }
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      e.printStackTrace();
    }
    return "/core/oaknow/panel/oachangename.jsp";
  }
  
  /**
   * 用户管理
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String userManage(HttpServletRequest request,HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String userKey = request.getParameter("userKey");
      int count = panelLogic.findPersonsCount(dbConn, userKey);
      pu.setElementsCount(count);
      pu.setPageSize(10);     
      int currNo = 1;
      if(YHStringUtil.isEmpty(request.getParameter("currNo"))){
        currNo = 1;
      }else{
        currNo = Integer.parseInt(request.getParameter("currNo"));
      }    
      pu.setCurrentPage(currNo);     
      List<YHPerson> users = panelLogic.findPersons(dbConn, userKey, pu);
      request.setAttribute("users", users);
      request.setAttribute("page", pu);
      request.setAttribute("user", user);
      request.setAttribute("userKey", userKey);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/core/oaknow/panel/oamamageuser.jsp";
  }
  
  public String findPerson(HttpServletRequest request,HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      YHPerson user = panelLogic.findPerson(dbConn, Integer.parseInt(userId));
      request.setAttribute("user", user);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/core/oaknow/panel/useredit.jsp";
  }
  
  /**
   * 更新用户
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updatePerson(HttpServletRequest request,HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    YHPerson user = new YHPerson();
    try{
      dbConn = requestDbConn.getSysDbConn();
      String username = request.getParameter("username");
      String score = request.getParameter("score");
      String tderflag = request.getParameter("tderflag");
      String userId = request.getParameter("userId");
      user.setSeqId(Integer.parseInt(userId));
      user.setUserName(username);
      user.setScore(Integer.parseInt(score));
      user.setTderFlag(tderflag);      
      int flag = panelLogic.updatePerson(dbConn, user);
      if(flag !=0 ){
        YHPerson p = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        YHSysLogLogic.addSysLog(dbConn, "8", p.getUserName()+"更新了"+user.getUserName()+"的用户的类型或分数", Integer.parseInt(userId), logLogic.getIpAddr(request));
        YHAjaxUtil.ajax(flag, response);
      }
   
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      e.printStackTrace();
    }
    return null ;
  }
  /**
   * 删除用户
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteUserByUserId(HttpServletRequest request,HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    try {
    dbConn = requestDbConn.getSysDbConn();
    YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    String userId = request.getParameter("userId");
    panelLogic.deleteUserReference(dbConn, Integer.parseInt(userId));
    YHSysLogLogic.addSysLog(dbConn, "8", user.getUserName()+"的用户删除"+userId+"的用户", Integer.parseInt(userId), logLogic.getIpAddr(request));
  } catch (Exception e) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      e.printStackTrace();
  }
  return userManage(request, response);
  }
}
