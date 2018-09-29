package yh.core.funcs.diary.act;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.diary.data.YHDiaryComment;
import yh.core.funcs.diary.data.YHDiaryCommentReply;
import yh.core.funcs.diary.logic.YHDiaryCommentLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHDiaryCommentAct {
  private static Logger log = Logger.getLogger(YHDiaryCommentAct.class);
  /**
   * 保存用户评论
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String saveComment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int userId = person.getSeqId();
      
      YHDiaryCommentLogic dcl = new YHDiaryCommentLogic();
      dcl.saveCommentLogic(dbConn, userId, request.getParameterMap(),request.getContextPath());
      
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
   * 保存用户评论
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listComment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int userId = person.getSeqId();
      String diaIdstr = request.getParameter("diaId");
      int diaId = Integer.parseInt(diaIdstr);
      YHDiaryCommentLogic dcl = new YHDiaryCommentLogic();
      ArrayList<YHDiaryComment> dclist = (ArrayList<YHDiaryComment>) dcl.listCommentLogic(dbConn, diaId);
      StringBuffer field = new StringBuffer();
      StringBuffer data = new StringBuffer();
      for (int i = 0 ; i < dclist.size(); i++) {
        YHDiaryComment dc = dclist.get(i);
        int isLoginUser = 0;
        ArrayList<YHDiaryCommentReply> dclistre = (ArrayList<YHDiaryCommentReply>) dcl.listCommentReplyLogic(dbConn, dc.getSeqId());
        StringBuffer dsbuff = YHFOM.toJson(dc);
        StringBuffer dscr = dcl.toJsonFCommentReply(dclistre);
        if(dc.getUserId() == userId){
          isLoginUser = 1;
        }
        if(!"".equals(field.toString())){
          field.append(",");
        }
        field.append("{")
          .append("comment:").append(dsbuff)
          .append(",commentReply:").append(dscr)
          .append(",isLoginUser:").append(isLoginUser)
          .append("}");
      }
      data.append("[").append(field).append("]");
      //System.out.println(data);
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
   * 删除评论
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteComment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String commentIdstr = request.getParameter("commentId");
      int commentId = Integer.parseInt(commentIdstr);
      
      YHDiaryCommentLogic dcl = new YHDiaryCommentLogic();
      dcl.deleteCommentLogic(dbConn, commentId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 评论标记为已读
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String commentReaded(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String commentIdstr = request.getParameter("commentId");
      int commentId = Integer.parseInt(commentIdstr);
      
      YHDiaryCommentLogic dcl = new YHDiaryCommentLogic();
      dcl.commentReadedLogic(dbConn, commentId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 保存当前用户的评论的回复
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String saveCommentReply(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int userId = person.getSeqId();
      
      YHDiaryCommentLogic dcl = new YHDiaryCommentLogic();
      dcl.saveCommentReplyLogic(dbConn, userId, request.getParameterMap(),request.getContextPath());
      
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
   * 删除评论回复
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteCommentReply(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String replyId = request.getParameter("replyId");
      
      YHDiaryCommentLogic dcl = new YHDiaryCommentLogic();
      dcl.deleteReplyLogic(dbConn, replyId);
      
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
  public String getDiaCommentDetaile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String commentId = request.getParameter("commentId");
      YHORM orm = new YHORM();
      YHDiaryComment dia = (YHDiaryComment) orm.loadObjSingle(dbConn, YHDiaryComment.class, Integer.parseInt(commentId));
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
 * 删除评论回复
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
  public String deleteReply(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String replyId = request.getParameter("replyId");
      YHORM orm = new YHORM();
      orm.deleteSingle(dbConn, YHDiaryCommentReply.class, Integer.valueOf(replyId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
/**
 * 删除评论回复
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
  public String getCommentReply(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String replyIdstr = request.getParameter("replyId");
      int replyId =  Integer.valueOf(replyIdstr);
      YHORM orm = new YHORM();
      YHDiaryCommentReply dcr = (YHDiaryCommentReply) orm.loadObjSingle(dbConn, YHDiaryCommentReply.class, replyId);
      StringBuffer data = YHFOM.toJson(dcr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA ,data.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 编辑日志回复
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateCommentReply(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDiaryCommentLogic dcl = new YHDiaryCommentLogic();
      dcl.updateCommentReplyLogic(dbConn, request.getParameterMap());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
