package yh.core.funcs.email.act;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.data.YHEmailBox;
import yh.core.funcs.email.logic.YHEmailBoxLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHEmailBoxAct{
  /**
   * 新建自定义邮箱
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String saveBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    //System.out.println(person);
    int userId = person.getSeqId();
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHEmailBoxLogic embl = new YHEmailBoxLogic();
      embl.saveBox(dbConn, request.getParameterMap(), userId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱保存成功！");
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱保存失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 修改自定义邮箱
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHEmailBoxLogic embl = new YHEmailBoxLogic();
      embl.updateBox(dbConn, request.getParameterMap());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱修改成功！");
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮想修改失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 修改自定义邮箱
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getBoxById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    try{
      String boxIdStr = request.getParameter("boxId");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      YHEmailBox eb = (YHEmailBox) orm.loadObjSingle(dbConn, YHEmailBox.class, Integer.parseInt(boxIdStr));
      StringBuffer data = YHFOM.toJson(eb);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data.toString() );
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱修改成功！");
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮想修改失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 修改邮箱页数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setBoxPage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    try{
      String seqIdstr = request.getParameter("seqId");
      String pageSizestr = request.getParameter("pageSize");
      String boxName = request.getParameter("boxName");
      int seqId = -1;
      if(seqIdstr != null && !"".equals(seqIdstr)){
        try {
          seqId = Integer.parseInt(seqIdstr);
        } catch (Exception e) {
          seqId = -1;
        }
      }else{
        seqId = -1;
      }
      int pageSize = Integer.parseInt(pageSizestr);
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHEmailBoxLogic embl = new YHEmailBoxLogic();
      embl.setPageSize(dbConn, pageSize, seqId, boxName, person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱修改成功！");
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮想修改失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 删除自定义邮箱
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    String idstr = request.getParameter("boxId");
    try{
      int id = Integer.valueOf(idstr.trim());
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHEmailBoxLogic embl = new YHEmailBoxLogic();
      embl.deleteBox(dbConn, id);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱删除成功！");
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱删除失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询自定义邮箱
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    //System.out.println(person);
    int userId = person.getSeqId();
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHEmailBoxLogic embl = new YHEmailBoxLogic();
      ArrayList<YHEmailBox> list = embl.listBoxByUser(dbConn, userId, false);
      request.setAttribute("boxList", list);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱列表读取成功！");
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱列表读取失败: " + e.getMessage());
    }
    return "/core/funcs/email/mailbox/boxManager.jsp";
  }
  /**
   * 取得自定义邮箱的容量
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getBoxSize(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    String boxIdStr = request.getParameter("boxId");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHEmailBoxLogic embl = new YHEmailBoxLogic();
      int boxId = Integer.valueOf(boxIdStr.trim());
      long result = embl.getBoxSizeById(dbConn, boxId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱容量读取成功！");
      request.setAttribute(YHActionKeys.RET_DATA,"");
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱容量读取失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setDefaultCount(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    String boxIdStr = request.getParameter("boxId");
    String defCount = request.getParameter("defCount");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHEmailBoxLogic embl = new YHEmailBoxLogic();
      int boxId = Integer.valueOf(boxIdStr.trim());
      embl.setBoxMailPage(dbConn, boxId, defCount);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱默认每页显示邮件数设置成功！");
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱默认每页显示邮件数设置失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSelf(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHEmailBoxLogic embl = new YHEmailBoxLogic();
      StringBuffer data = embl.getBoxSelfLogic(dbConn, person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱默认每页显示邮件数设置失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSelfForLi(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHEmailBoxLogic embl = new YHEmailBoxLogic();
      StringBuffer data = embl.getBoxSelfForList(dbConn, person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱默认每页显示邮件数设置失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getBoxName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    String boxId = request.getParameter("boxId");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHEmailBoxLogic embl = new YHEmailBoxLogic();
      StringBuffer data = embl.getBoxName(dbConn, person.getSeqId(),boxId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱默认每页显示邮件数设置失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDefBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHEmailBoxLogic embl = new YHEmailBoxLogic();
      StringBuffer data = embl.getBoxDefLogic(dbConn, person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱默认每页显示邮件数设置失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String isBoxNameExist(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    try{
      String boxName = request.getParameter("boxName");
      String boxId = request.getParameter("boxId");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHEmailBoxLogic embl = new YHEmailBoxLogic();
      boolean isExit = embl.isNameExist(dbConn, person.getSeqId(), boxName,boxId);
      String data = "{isExist:" + isExit + "}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱默认每页显示邮件数设置失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
}
