package yh.subsys.portal.guoyan.module.act;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.HashMap;
//import java.util.zip.CRC32;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.office.ntko.data.YHNtkoStream;
import yh.core.funcs.office.ntko.logic.YHNtkoLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
//import yh.core.util.form.YHFOM;
import yh.subsys.portal.guoyan.module.logic.YHPortalGridModuleLogic;
/**
 * 如果你使用过Servlet这个层就相当于action层
 * act层只是对Servlet的ACTION层进行了封装
 * get/post通过请求到这层，地址类似于:/yh/subsys/portal/guoyan/module/YHPortalGridModule/loadGridData.act.
 * 分解为 1.类名称：yh.subsys.portal.guoyan.module.act.YHPortalGridModule
 *       2.方法名称：loadGridData
 *       3.后缀：.act
 * 返回的数据分两种情况
 *    1.通过PrintWriter pw = response.getWriter();
          pw.write(data);
          pw.flush();
                      直接输出
      2. return "/core/inc/rtjson.jsp";
                     通过这样输出到页面的话就是
             {rtState:"0", rtMsrg:"返回的提示信息", rtData:json数据}
 * @author tulaike
 *
 */
public class YHPortalGridModule {
  /**
   * 加载列表数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String loadGridData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPortalGridModuleLogic lal = new YHPortalGridModuleLogic();
      String data = lal.loadGridDataLogic(dbConn, request.getParameterMap()).toString();
      /**
       *    页面得到的数据
       *    
       *    js代码 ： 
       *       var url = contextPacth + "/yh/subsys/portal/guoyan/module/YHPortalGridModule/loadGridData.act";
       *       var rtJson = getJsonRs(url);
       *    此处的 实际数据是 rtJson;
       *    即rtJson为 上面的 字符串 data
       */
      PrintWriter pw = response.getWriter();
      pw.write(data);
      pw.flush();
    }catch(Exception ex) {
      throw ex;
    }
    return null;
  }
  /**
   * 加载单条数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String loadOneData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String newsId = request.getParameter("newsId");
      YHPortalGridModuleLogic lal = new YHPortalGridModuleLogic();
      String data = lal.loadOneData(dbConn,Integer.valueOf(newsId)).toString();
      /****
       * {rtState:"0", rtMsrg:"返回的提示信息", rtData:json数据} 注意:这道页面了也是一个JSON数据，取Data的话
       *    js代码 ： var rtJson = getJsonRs(url);
       *    此处的 实际数据应该是 rtJson.rtData;
       * YHActionKeys.RET_STATE 即上面rtState的值，0(YHConst.RETURN_OK)表示正常，1(YHConst.RETURN_ERROR)表示错误
       * YHActionKeys.RET_MSRG  即上面rtMsrg的值
       * YHActionKeys.RET_DATA  即上面rtData的值
       * ******/
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 加载带分页的数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String loadDataPage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String pageSize = request.getParameter("pageSize");
      String pageIndex = request.getParameter("pageIndex");
      String newsType = request.getParameter("newsType");

      YHPortalGridModuleLogic lal = new YHPortalGridModuleLogic();
      String data = lal.loadDataPage(dbConn, Integer.valueOf(pageSize), Integer.valueOf(pageIndex), newsType).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 根据用户名称取得用户Id
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserIdByName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userName = request.getParameter("userName");
      YHPortalGridModuleLogic lal = new YHPortalGridModuleLogic();
      String data = lal.getUserIdByName(dbConn, userName);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 根据用户Id取得用户名称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserNameById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      YHPortalGridModuleLogic lal = new YHPortalGridModuleLogic();
      String data = lal.getUserNameById(dbConn, userId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 根据部门名称取得部门ID
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDeptIdByName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userName = request.getParameter("deptName");
      YHPortalGridModuleLogic lal = new YHPortalGridModuleLogic();
      String data = lal.getDeptIdByName(dbConn, userName);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 下载图片
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String downloadImage(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    OutputStream ops = null;
    InputStream is = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      //YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String directView = "1";
      YHNtkoLogic nl = new YHNtkoLogic();
      YHNtkoStream ns = new YHNtkoStream();
      String attachmentIdstr = request.getParameter("ATTACHMENT_ID");
      String ym = request.getParameter("YM");
      String attachmentName = request.getParameter("ATTACHMENT_NAME");
      String module = request.getParameter("MODULE");
      /*java.util.zip.CRC32 crc = new CRC32();
      crc.update(attachmentName.getBytes());
      long attachLong = Long.valueOf(attachmentIdstr)^crc.getValue();*/
      String attachmentId = ym + "_" + attachmentIdstr;
      ns.setAttachmentId(attachmentId);
      ns.setAttachmentName(attachmentName);
      ns.setModule(module);
      is = ns.getFileStream();
      //nl.saveOcLog(conn, person.getSeqId(), request.getRemoteAddr(), ns.getAttachmentId(), ns.getAttachmentName(), 1);
      HashMap<String, String> contentTypeMap = (HashMap<String, String>) nl.getAttachHeard(ns.getAttachmentName(), directView);
      String contentType = contentTypeMap.get("contentType");
      String contentTypeDesc = contentTypeMap.get("contentTypeDesc");
      //设置html 头信息
      String fileName = URLEncoder.encode(ns.getAttachmentName(),"UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      if(contentTypeDesc != null){
        response.setContentType(contentTypeDesc);
      }else {
        response.setContentType("application/octet-stream");
      }
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Accept-Length",String.valueOf(ns.getFileSize()));
      response.setHeader("Content-Length",String.valueOf(ns.getFileSize()));
      if("1".equals(contentType)){
        response.setHeader("Content-disposition","filename=" + fileName);
      } else {
        response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      }
      ops = response.getOutputStream();
      if(is != null){
        byte[] buff = new byte[8192];
        int byteread = 0;
        while( (byteread = is.read(buff)) != -1){
          ops.write(buff,0,byteread);
          ops.flush();
        }
      }
      //System.out.println(ns.toString());
      //System.out.println(response.getContentType());
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      if (is != null) {
        is.close();
      }
    }
    return null;
  }
}
