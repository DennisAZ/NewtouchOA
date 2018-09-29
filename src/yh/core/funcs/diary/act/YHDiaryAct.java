package yh.core.funcs.diary.act;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.diary.data.YHDiary;
import yh.core.funcs.diary.data.YHDiaryCont;
import yh.core.funcs.diary.data.YHDiaryLock;
import yh.core.funcs.diary.logic.YHDiaryLogic;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
/**
 * 工作日志
 * @author TTlang
 *
 */
public class YHDiaryAct{
  private static Logger log = Logger.getLogger(YHDiaryAct.class);

  /**
   * 工作日志保存 ajax方式
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String saveByAjax(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int userId = person.getSeqId();
      
      YHDiaryLogic dl = new YHDiaryLogic();
      dl.saveLogic(dbConn, userId, request.getParameterMap());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 工作日志保存 ajax方式
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateByAjax(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int userId = person.getSeqId();
      
      YHDiaryLogic dl = new YHDiaryLogic();
      dl.updateLogic(dbConn, userId, request.getParameterMap());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 附件上传
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String uploadFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    YHFileUploadForm fileForm = new YHFileUploadForm();
    String data = "";
    try {
      fileForm.parseUploadRequest(request);
    } catch (Exception e) {
      data = "{type:1}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
      return "/core/inc/rtuploadfile.jsp";
    }
    
    Map<String, String> attr = null;
    String attrId = (fileForm.getParameter("attachmentId")== null )? "":fileForm.getParameter("attachmentId");
    String attrName = (fileForm.getParameter("attachmentName")== null )? "":fileForm.getParameter("attachmentName");
    try{
      YHDiaryLogic dl = new YHDiaryLogic();
      attr = dl.fileUploadLogic(fileForm, YHDiaryCont.ATT_PATCH);
      Set<String> keys = attr.keySet();
      for (String key : keys){
        String value = attr.get(key);
        if(attrId != null && !"".equals(attrId)){
          if(!(attrId.trim()).endsWith(",")){
            attrId += ",";
          }
          if(!(attrName.trim()).endsWith("*")){
            attrName += "*";
          }
        }
        attrId += key + ",";
        attrName += value + "*";
      }
      data = "{type:0,attrId:\"" + attrId + "\",attrName:\"" + attrName + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);

    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
      throw e;
    }
    return "/core/inc/rtuploadfile.jsp";
  }
  /**
   * 列出当前用户最新的十条工作日志
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String lastTen(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int userId = person.getSeqId();
      YHDiaryLogic dl = new YHDiaryLogic();
      List<YHDiary> diaryList = dl.getLastTenEntryByUserId(dbConn, userId,1);
      StringBuffer data = dl.toJson(dbConn, diaryList);
 
      //System.out.println("最近十条员工日志DIARY:" + data.toString());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
/**
 * 取得指定时间的日志
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
  public String listDiaryByDate(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int userId = person.getSeqId();
      String dateStr = request.getParameter("DiaDateDiary");
      YHDiaryLogic dl = new YHDiaryLogic();
      List<YHDiary> diaryList = dl.getLastByDate(dbConn, userId, YHUtility.parseDate(dateStr));
      StringBuffer data = dl.toJson(dbConn, diaryList);
 
      System.out.println("当前用户/当前时间DIARY:" + data.toString());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 取得指定ID日志信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
    public String getDiaDiaryById(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        String idStr = request.getParameter("diaId");
        YHDiaryLogic dl = new YHDiaryLogic();
        YHORM orm = new YHORM();
        YHDiary dia = (YHDiary) orm.loadObjSingle(dbConn, YHDiary.class, Integer.parseInt(idStr));
        StringBuffer dia2Json = YHFOM.toJson(dia);
   
        //System.out.println("ByIdDIARY:" + dia2Json.toString());
        
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_DATA, dia2Json.toString());
      } catch (Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
/**
 * 取得指定ID日志信息
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
  public String deleteDia(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String idStr = request.getParameter("diaIds");
      
      YHDiaryLogic dl = new YHDiaryLogic();
      dl.deleteDiaryLogic(dbConn, idStr);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
/**
 * 取得指定ID日志信息
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
  public String isLock(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String dateStr = request.getParameter("date");
      
      YHDiaryLogic dl = new YHDiaryLogic();
      YHDiaryLock diaLock = dl.getLock(dbConn);
      String data = "0";
      boolean islock = diaLock == null ? false : diaLock.isLock(YHUtility.parseDate(dateStr));
      if(islock){
        data = "1";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 列出指定用户最新的十条工作日志
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String lastTenByUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userIdStr = request.getParameter("userId");
      int userId = Integer.parseInt(userIdStr);
      YHDiaryLogic dl = new YHDiaryLogic();
      List<YHDiary> diaryList = dl.getLastTenEntryByUserId(dbConn, userId,2);
      StringBuffer data = dl.toJson(dbConn, diaryList);
 
      //System.out.println("指定用户最新的最近十条员工日志DIARY:" + data.toString());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 用户管理范围的十篇员工日志
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String lastTenByAllUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHDiaryLogic dl = new YHDiaryLogic();
      String modeulId = YHDiaryCont.MODUEL_ID;
      int privNoFlag = YHDiaryCont.PRIV_NO_FLAG;
      List<YHDiary> diaryList = dl.getLastTenEntryBySer(dbConn, person,modeulId , privNoFlag);
      StringBuffer data = dl.toJson(dbConn, diaryList);
 
      //System.out.println("用户管理范围的最近十条员工日志DIARY:" + data.toString());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 取得当前用户已被评论的日志
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listCommentedDiary(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int userId = person.getSeqId();
      YHDiaryLogic dl = new YHDiaryLogic();
      String data = dl.getCommentDiary(dbConn, userId);
 
      //System.out.println("用户管理范围的最近十条员工日志DIARY:" + data);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询日志
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String searchDiary(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
//      String userIdStr = request.getParameter("userId");
//      int userId = Integer.parseInt(userIdStr);
      
      YHDiaryLogic dl = new YHDiaryLogic();
      String data = dl.toSearchData(dbConn,request.getParameterMap());
      
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public String searchDiarySelf(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
//      String userIdStr = request.getParameter("userId");
//      int userId = Integer.parseInt(userIdStr);
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int userId = person.getSeqId();
      YHDiaryLogic dl = new YHDiaryLogic();
      //String data = dl.toSearchData(dbConn,request.getParameterMap(),userId);
      String data = dl.toSearchData1(dbConn,request,userId);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 查询（公共事务）员工日志查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String searchDiaryForInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");

//      String userIdStr = request.getParameter("userId");
//      int userId = Integer.parseInt(userIdStr);
      
      YHDiaryLogic dl = new YHDiaryLogic();
      String data = dl.toSearchDataForInfo(dbConn, request.getParameterMap(),person,YHDiaryCont.MODUEL_ID,YHDiaryCont.PRIV_NO_FLAG);
      
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 取得指定日志的共享范围
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getShare(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      String diaIdStr = request.getParameter("diaId");
      int diaId = Integer.parseInt(diaIdStr);
      YHDiaryLogic dl = new YHDiaryLogic();
      String data = dl.getShareLogic(dbConn, diaId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 设置指定日志的共享范围
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setShare(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      String diaIdStr = request.getParameter("diaId");
      String toId = request.getParameter("toId");
      int diaId = Integer.parseInt(diaIdStr);
      YHDiaryLogic dl = new YHDiaryLogic();
      dl.setShareLogic(dbConn, diaId, toId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "共享范围设定成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 取得指定日志的共享范围
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data  = "";
      String userIdStr = request.getParameter("userId");
      if(userIdStr != null && !"".equals(userIdStr)){
      int userId = Integer.parseInt(userIdStr);
      YHDiaryLogic dl = new YHDiaryLogic();
       data = dl.getUserNameLogic(dbConn, userId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getDeptName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      String deptIdStr = request.getParameter("deptId");
      int deptId = Integer.parseInt(deptIdStr);
      YHDiaryLogic dl = new YHDiaryLogic();
      String data = dl.getDeptNameLogic(dbConn, deptId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 是否被评论
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String isComment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      String diaIdStr = request.getParameter("diaId");
      int diaId = Integer.parseInt(diaIdStr);
      YHDiaryLogic dl = new YHDiaryLogic();
      int count =  dl.isCommentLogic(dbConn, diaId);
      String data = String.valueOf(count);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 列出所有的共享日志
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listShareDiary(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHDiaryLogic dl = new YHDiaryLogic();
      String data = dl.getShareDiary(dbConn, person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data );
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw  e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 保存阅读了此日志的人员
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String reader(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHDiaryLogic dl = new YHDiaryLogic();

      String diaIdStr = request.getParameter("diaId");
      int diaId = Integer.parseInt(diaIdStr);
      dl.setReader(dbConn,person.getSeqId(), diaId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw  e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 显示阅读人员
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String showReader(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHDiaryLogic dl = new YHDiaryLogic();

      String diaIdStr = request.getParameter("diaId");
      int diaId = Integer.parseInt(diaIdStr);
      String data = dl.showReader(dbConn, diaId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw  e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 显示阅读人员
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String loadDiaryById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDiaryLogic dl = new YHDiaryLogic();
      String diaIds = request.getParameter("diaId");
      String data = dl.getDiaryById(dbConn, diaIds);
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw  e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getUserInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHDiaryLogic dl = new YHDiaryLogic();
      String data = dl.getUserInFo(dbConn, person.getSeqId(), person.getUserName(), person.getUserPriv());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw  e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String exportExcel(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    OutputStream ops = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String fileName = URLEncoder.encode("工作日志.xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-execl");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      YHDiaryLogic dial = new YHDiaryLogic();
      ArrayList<YHDbRecord > dbL = dial.toExportDiaData(conn,request.getParameterMap(),person.getSeqId());
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSubject(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String diaIdStr = request.getParameter("diaId");
      int diaId = Integer.valueOf(diaIdStr);
      YHORM orm = new YHORM();
      YHDiary diary = (YHDiary) orm.loadObjSingle(dbConn, YHDiary.class, diaId);
      String data = YHUtility.encodeSpecial(diary.getSubject());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw  e;
    }
    return "/core/inc/rtjson.jsp";
  }
}
