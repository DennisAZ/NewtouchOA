package yh.subsys.inforesouce.act;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.oaknow.util.YHAjaxUtil;
import yh.core.util.YHUtility;
import yh.subsys.inforesouce.data.YHImageManage;
import yh.subsys.inforesouce.data.YHKengine;
import yh.subsys.inforesouce.logic.YHManageImgLogic;
import yh.subsys.inforesouce.logic.YHMateTypeLogic;
import yh.subsys.inforesouce.util.YHFileMateConstUtil;
import yh.subsys.inforesouce.util.YHPageUtil;

/**
 * 图片管理中心<br>
 * 调用的类：YHManageImgLogic, YHMateTypeLogic
 * @see yh.subsys.inforesouce.logic.YHManageImgLogic
 * @see yh.subsys.inforesouce.logic.YHMateTypeLogic
 * @author qwx110
 * 
 */
public class YHManageImgAct{
  
  /**
   * 通过分页查找所有的图片新闻<br>
   * 调用YHManageImgLogic的findAllImage方法进行查询，返回当前页的图片新闻<br>
   * 通过ajax调用返回结果
   * @see yh.subsys.inforesouce.logic.YHManageImgLogic#findAllImage(Connection, YHPageUtil, HttpServletRequest)
   * @see yh.subsys.inforesouce.util.YHPageUtil
   * @see yh.core.oaknow.util.YHAjaxUtil#ajax(String, HttpServletResponse)
   * @param request
   * @param response
   * @return null
   * @throws Exception
   */
  public String findAllImages(HttpServletRequest request, HttpServletResponse response)throws Exception{
    
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHManageImgLogic imgLogic = new YHManageImgLogic();
      YHPageUtil page = new YHPageUtil();
      String crrNo = request.getParameter("currNo").trim();
      int imgAmount = imgLogic.findImageAmount(dbConn);
      if(YHUtility.isNullorEmpty(crrNo)){
        crrNo = "1";
      }
      page.setCurrentPage(Integer.parseInt(crrNo));
      page.setPageSize(3);
      page.setElementsCount(imgAmount); 
      List<YHImageManage> imges = imgLogic.findAllImage(dbConn, page, request);
      String images = toJson(imges, page);     
      YHAjaxUtil.ajax(images, response);      
      request.setAttribute(YHActionKeys.RET_DATA,  images);
    } catch (Exception ex){
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return null;
  }
  
  /**
   * 把一个List转化为json格式
   * @param imges
   * @return
   * @throws Exception 
   */
  public String toJson( List<YHImageManage> imges, YHPageUtil pu) throws Exception{
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    sb.append("data:[");
    if(imges != null && imges.size() >0){
      int size = imges.size();
      for(int i=0; i<size; i++){
        sb.append(imges.get(i).toJson());
        if(i < size-1){
          sb.append(",");
        }
      }
    }
    sb.append("]");
    if(pu != null){
      sb.append(",");
      sb.append("\"currNo\":").append(pu.getCurrentPage()).append(",");
      sb.append("\"totalNo\":").append(pu.getPagesCount()).append(",");
      sb.append("\"pageSize\":").append(pu.getPageSize());
    }    
    sb.append("}");
    //YHOut.println(sb.toString());
    return sb.toString();
  }
  
  /**
   * 查找文件的主题词，组织机构名，人名，地名等<br>
   * 调用YHMateTypeLogic的findName返回文件的主题词，组织机构名，人名，地名等的编号<br>
   * 调用YHManageImgLogic的findMate返回主题词，组织机构名，人名，地名
   * @see yh.subsys.inforesouce.logic.YHMateTypeLogic#findName(Connection, String)
   * @see yh.subsys.inforesouce.logic.YHManageImgLogic#findMate(Connection, String, String, String, String)
   * @param request
   * @param response
   * @return "/core/inc/rtjson.jsp"
   * @throws Exception
   */
  public String findAmate(HttpServletRequest request, HttpServletResponse response) throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    
    Connection dbConn = null;
    String userName = null ;
    String areaName = null;
    String org = null;
    
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHMateTypeLogic logic = new YHMateTypeLogic();
      YHManageImgLogic alogic = new YHManageImgLogic();
      String newSeqId = request.getParameter("newSeqId");  
      userName = logic.findName(dbConn, YHFileMateConstUtil.userName);
      areaName = logic.findName(dbConn, YHFileMateConstUtil.areaName);
      org = logic.findName(dbConn, YHFileMateConstUtil.Org);
      YHKengine ki = alogic.findMate(dbConn, newSeqId+"",userName,areaName,org);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回内容");
      request.setAttribute(YHActionKeys.RET_DATA,  mate2Json(ki) );
    } catch (Exception ex){
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    } 
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 把YHKengine 转化为json
   * @param engine
   * @return
   */
  public String mate2Json(YHKengine engine){
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    if(engine != null){
       if(!YHUtility.isNullorEmpty(engine.getUserName())){
         sb.append("name:").append("\"").append(YHUtility.encodeSpecial(strSplit(engine.getUserName()))).append("\"").append(",");
       }else{
         sb.append("name:").append("''").append(",");
       }
       if(!YHUtility.isNullorEmpty(engine.getAreaName())){
         sb.append("area:").append("\"").append(YHUtility.encodeSpecial(strSplit(engine.getAreaName()))).append("\"").append(",");
       }else{
         sb.append("area:").append("''").append(",");
       }
       if(!YHUtility.isNullorEmpty(engine.getOrgName())){
         sb.append("orge:").append("\"").append(YHUtility.encodeSpecial(strSplit(engine.getOrgName()))).append("\"");
       }else{
         sb.append("orge:").append("''");
       }
    }
    sb.append("}");
    return sb.toString();
  }
  
  public String strSplit(String str){
    String[] fix = str.split(" ");
    int len = fix.length >2 ? 2 :fix.length;
    String nStr = "";
    for(int i=0; i<len; i++){
      nStr += fix[i] + " ";
    }
    return nStr;
  }
  
  /**
   * 返回最多10张图片（热点图片）<br>
   * 调用YHManageImgLogic的findTenImage返回10篇图片新闻，生成json数据<br>
   * 调用ajax返回到前台
   * @see yh.subsys.inforesouce.logic.YHManageImgLogic#findTenImage(Connection, HttpServletRequest)
   * @param request
   * @param response
   * @return null
   * @throws Exception
   */
  public String findTenImages(HttpServletRequest request, HttpServletResponse response)throws Exception{
    
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHManageImgLogic imgLogic = new YHManageImgLogic();
    
      List<YHImageManage> imges = imgLogic.findTenImage(dbConn, request);
      String images = toJson(imges, null);     
      YHAjaxUtil.ajax(images, response);      
      request.setAttribute(YHActionKeys.RET_DATA,  images);
    } catch (Exception ex){
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return null;
  }
}
