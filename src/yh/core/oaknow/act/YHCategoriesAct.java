package yh.core.oaknow.act;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.oaknow.data.YHCategoriesType;
import yh.core.oaknow.logic.YHCategoriesLogic;
import yh.core.oaknow.logic.YHOAKnowLogic;
import yh.core.oaknow.util.YHAjaxUtil;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.oaknow.util.YHOAToJsonUtil;
import yh.core.util.YHOut;

public class YHCategoriesAct{
  private  YHOAKnowLogic oaLogicIndex = new YHOAKnowLogic();
  private YHCategoriesLogic typeLogic = new YHCategoriesLogic();
  /**
   * 挑传到新建分类页面
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String goToCategoty(HttpServletRequest request,HttpServletResponse response) throws Exception{  
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
    YHCategoriesType type = new YHCategoriesType();;
    try{
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      int flag = 0;
      if(YHStringUtil.isNotEmpty(seqId)){
        type = typeLogic.findATypeById(dbConn, Integer.parseInt(seqId));       
        flag = 1;
     }   
      request.setAttribute("flag", flag);
      request.setAttribute("type", type);
      if(YHStringUtil.isNotEmpty(type.getName())){
        request.setAttribute("quot", YHStringUtil.toChange(type.getName()));
      }
      List<YHCategoriesType>  types = oaLogicIndex.findKind(dbConn);
      request.setAttribute("toJson", YHOAToJsonUtil.toJsonTwo(types));    
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
  
    return "/core/oaknow/panel/categories.jsp";
  }
  /**
   * 保存新的类型
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findCategory(HttpServletRequest request,HttpServletResponse response) throws Exception{  
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHCategoriesType type = new YHCategoriesType();
      String oderId = request.getParameter("oderId");
      String typeName = request.getParameter("typeName");
      String pearentid = request.getParameter("pearentid");
      String managernames = request.getParameter("manage");
      String seqId = request.getParameter("seqId");
      if(YHStringUtil.isNotEmpty(seqId)){
        type.setSeqId(Integer.parseInt(seqId));
      }else{
        type.setSeqId(0);
      }
      
      type.setOrderId(Integer.parseInt(oderId));
      type.setName(typeName);
      type.setPearentId((Integer.parseInt(pearentid)-Integer.parseInt(seqId)==0)?0:Integer.parseInt(pearentid));
      type.setManagers(managernames);
     // int id = typeLogic.saveCategoty(dbConn, type);
      int id = typeLogic.saveOrUpdateCategoty(dbConn, type);
      YHAjaxUtil.ajax(id, response);
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return null;
  }
  
  /**
   * 删除一个类型
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteType(HttpServletRequest request,HttpServletResponse response) throws Exception{  
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
    dbConn = requestDbConn.getSysDbConn();
    try{
      String seqId = request.getParameter("seqId");
      int id = typeLogic.deleteType(dbConn, Integer.parseInt(seqId)); 
      YHAjaxUtil.ajax(id, response);
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return null;
  }
}
