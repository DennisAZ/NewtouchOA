package yh.subsys.inforesouce.act;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.subsys.inforesouce.data.YHMateShow;
import yh.subsys.inforesouce.data.YHMateType;
import yh.subsys.inforesouce.logic.YHMateShowLogic;
import yh.subsys.inforesouce.util.YHStringUtil;

/**
 * 控制元数据树的类
 * @author qwx110
 *
 */
public class YHMateShowAct{
  private YHMateShowLogic show = new YHMateShowLogic();
  /**
   * 跳转到managemate.jsp
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String toManage(HttpServletRequest request, HttpServletResponse response) throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");      //获得登陆用户
      String ftype = request.getParameter("ftype");
      if(YHUtility.isNullorEmpty(ftype)){
        ftype = "1";
      }
      List<YHMateType> types = show.findMyMenus(dbConn, user, ftype); // 此方法查询所有的父元素，子元素，值域
      YHMateShow idString = show.findMyShow(dbConn, user, ftype);    // 用户第二次登陆显示 上次定义好的元素
      String saveOk = request.getParameter("saveOk");
      if(YHStringUtil.isEmpty(saveOk)){
        saveOk = "";
      }
      request.setAttribute("ftype", ftype);
      request.setAttribute("saveOk", saveOk);
      request.setAttribute("types", types);
      request.setAttribute("idStr", idString);
    } catch (Exception e){ 
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;      
    } 
    return "/subsys/inforesource/managemate.jsp";
  } 
  
  /**
   * 保存用户所选的menu
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String saveAjax(HttpServletRequest request, HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    String saveOk = "fail";
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");      //获得登陆用户
      String showId = request.getParameter("show");                                   //获得用户选择的所有的顶级父节点，通过顶级父节点获得选择的项
      String newIds = newString(showId, request);
      String typeId = request.getParameter("ftype");
      int k = show.saveOrUpdate(dbConn, user, showId, newIds, typeId);
      if(k != 0){
        saveOk = "ok";
      }
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/yh/subsys/inforesouce/act/YHMateShowAct/toManage.act?saveOk=" + saveOk;
  }
  
  /**
   * 工具方法
   * @param showId
   * @param request
   * @return
   */
  public String newString(String showId,HttpServletRequest request){
    String newIds = "";
    if(YHStringUtil.isNotEmpty(showId)){
      String[] pIds = showId.split(",");//把所有的父元素区分
      if(pIds.length != 0){        
        for(int i=0; i<pIds.length; i++){
         String[] ids = request.getParameterValues(pIds[i]);//每次获得父元素一个节点（包括子元素和值域 他们的name都是相同的（相当于key），获得他们的名字就能获得不同的值）
         String str = YHStringUtil.array2AString(ids);
         if(YHStringUtil.isNotEmpty(str)){
           newIds += str +",|";
         }       
        }
        //YHOut.println(newIds);
        newIds = newIds.substring(0, newIds.lastIndexOf("|")==-1?0:newIds.lastIndexOf("|"));
      }
    }
    return newIds;
  }
  
}
