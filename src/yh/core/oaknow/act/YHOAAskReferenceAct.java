package yh.core.oaknow.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.oaknow.data.YHAskAnswer;
import yh.core.oaknow.data.YHCategoriesType;
import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.data.YHOAComment;
import yh.core.oaknow.logic.YHOAKnowAnswerLogic;
import yh.core.oaknow.logic.YHOAKnowLogic;
import yh.core.oaknow.logic.YHOAKnowMyPanelLogic;
import yh.core.oaknow.logic.YHOAKnowTypeLogic;
import yh.core.oaknow.util.YHAjaxUtil;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.util.YHOut;

/**
 * 与问题相关
 * @author qwx110
 *
 */
public class YHOAAskReferenceAct{

  private  YHOAKnowAnswerLogic oaLogic = new YHOAKnowAnswerLogic();
  private  YHOAKnowLogic oaLogicIndex = new YHOAKnowLogic();
  private YHOAKnowTypeLogic typeLogic = new YHOAKnowTypeLogic();
  private YHOAKnowMyPanelLogic panelLogic = new YHOAKnowMyPanelLogic();
  private static Logger log = Logger
  .getLogger("yh.core.act.YHOAAskReference");
  
  /**
   * 问题状态，最佳答案，相关问题，最佳答案的评论
   * @return
   * @throws Exception 
   */
  public String findAskRef(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    int askId = Integer.parseInt(request.getParameter("askId"));
    try{
      
      dbConn = requestDbConn.getSysDbConn();
      YHOAAsk ask = oaLogic.findAskStatus(dbConn, askId);//查找问题状态
      YHAskAnswer goodAnswer = oaLogic.findBetterAnswer(dbConn, askId);//最佳答案
      List<YHAskAnswer> otherAnswers = oaLogic.findOtherAnswer(dbConn, askId);//其他答案
      List<YHOAComment> pinLun = oaLogic.findBetterAnswerPingLun(dbConn, goodAnswer.getAnswerId());//对最佳答案的评论      
      List<YHOAAsk> askList = oaLogic.findRefAsk(dbConn, askId);  //相关问题
      List<YHCategoriesType> types = typeLogic.findTypseUtil3(dbConn, askId);
      String oaName = panelLogic.findOAName(dbConn).trim();  
      String showFlag = request.getParameter("showFlag");
      if(YHStringUtil.isNotEmpty(showFlag)){
        request.setAttribute("showFlag", showFlag);
      }
      request.setAttribute("oaName", oaName);
      request.setAttribute("types", types);
      request.setAttribute("ask", ask);
      request.setAttribute("askList", askList);
      request.setAttribute("goodAnswer", goodAnswer);
      request.setAttribute("otherAnswers", otherAnswers);
      request.setAttribute("pinLun", pinLun);
      
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    
    return "/core/oaknow/oaknowask.jsp";
  }
  
  /**
   * 回答问题
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String toAnswerAjax(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHAskAnswer answer = new YHAskAnswer();
      int askId = Integer.parseInt(request.getParameter("askId"));
      String answerComment = request.getParameter("content");
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      answer.setAskId(askId);
      answer.setAnswerComment(answerComment);
      answer.setAnswerUserId(user.getSeqId()+"");
      answer.setAnswerTime(new Date());
      answer.setGoodAnswer(0);
      int id = oaLogic.insertAnswer(dbConn, answer);
      PrintWriter pw = response.getWriter();
      if(id !=0 ){
        String rtData = "{rtState:'0',rtMsrg:'提交答案成功'}";
        pw.println(rtData);       
      }else{
        String rtData = "{rtState:'1',rtMsrg:'提交答案失败'}";
        pw.println(rtData);        
      }
      pw.flush();
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    } 
    return null;
  }
 
  /**
   * 对最佳答案的评论输入
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
 public String goodAnsPingLun(HttpServletRequest request, HttpServletResponse response) throws Exception {
   Connection dbConn = null;
   YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
   try{
    dbConn = requestDbConn.getSysDbConn();
    String askId = request.getParameter("askId").trim();    
    YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
    int userId = user.getSeqId();
    //YHOut.println(askId+"--------------------------userId========" + userId);
    String comm = request.getParameter("comment");
    YHOAComment comment = new YHOAComment();
    comment.setAskId(Integer.parseInt(askId));
    comment.setComment(comm);
    comment.setMamber(userId+"");
    comment.setDateTime(new Date());
    
    int id = oaLogic.goodAnswerPingLun(dbConn, comment);
    PrintWriter pw = response.getWriter();
    if(id !=0 ){
      String rtData = "{rtState:'0',rtMsrg:'提交答案成功'}";
      pw.println(rtData);       
    }else{
      String rtData = "{rtState:'1',rtMsrg:'提交答案失败'}";
      pw.println(rtData);        
    }
    pw.flush();
  } catch (Exception e){
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    throw e;
  } 
  return null;
 }
 
/**
 * 采纳为答案
 * @param request
 * @param response
 * @return
 * @throws Exception 
 */
 public String changeToGoodAnswer(HttpServletRequest request, HttpServletResponse response) throws Exception{
   Connection dbConn = null;
   YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
   try{
    dbConn = requestDbConn.getSysDbConn();
    int askId = Integer.parseInt(request.getParameter("askId"));
    int answerId = Integer.parseInt(request.getParameter("answerId"));
    int userId = Integer.parseInt(request.getParameter("userId"));
    int status = oaLogic.changeToGoodAnswer(dbConn, askId, answerId, userId);
    
    PrintWriter pw = response.getWriter();
    if(status !=0 ){
      String rtData = "{rtState:'0',rtMsrg:'提交答案成功'}";
      pw.println(rtData);       
    }else{
      String rtData = "{rtState:'1',rtMsrg:'提交答案失败'}";
      pw.println(rtData);        
    }
    pw.flush();
  }  catch (Exception e){    
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    throw e;
  }
   return null;
 }
 
 /**
  * 推荐某个问题为推荐状态
  * @param request
  * @param response
  * @return
 * @throws Exception 
  */
 public String tuiJianStatus(HttpServletRequest request, HttpServletResponse response) throws Exception{
   Connection dbConn = null;
   YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
   int id =0;
   try{
     dbConn = requestDbConn.getSysDbConn();
     int askId = Integer.parseInt(request.getParameter("askId"));
     int flag  = Integer.parseInt(request.getParameter("flag"));
     if(flag == 1){
       id = oaLogic.tuiJianStatus(dbConn, askId, 1);  //推荐状态
     }else if(flag == 0){
       id = oaLogic.tuiJianStatus(dbConn, askId, 0);  //取消推荐 状态  
     }      
     YHAjaxUtil.ajax(id, response);
   }catch(Exception e){
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
     throw e;
   }
   return null;
 }
 
 /**
  * 编辑问题
  * @param request
  * @param response
  * @return
 * @throws Exception 
  */
 public String editAsk(HttpServletRequest request, HttpServletResponse response) throws Exception{
   Connection dbConn = null;
   YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
   int askId = Integer.parseInt(request.getParameter("askId"));
   try{
     
     dbConn = requestDbConn.getSysDbConn();
     YHOAAsk ask = oaLogic.findAskStatus(dbConn, askId);//查找问题状态
     YHAskAnswer goodAnswer = oaLogic.findBetterAnswer(dbConn, askId);//最佳答案
     List<YHAskAnswer> otherAnswers = oaLogic.findOtherAnswer(dbConn, askId);//其他答案
     List<YHOAComment> pinLun = oaLogic.findBetterAnswerPingLun(dbConn, goodAnswer.getAnswerId());//对最佳答案的评论      
     List<YHCategoriesType>  types = oaLogicIndex.findKind(dbConn);
     List<YHOAAsk> askList = oaLogic.findRefAsk(dbConn, askId);  //相关问题
     List<YHCategoriesType> kinds = typeLogic.findTypseUtil3(dbConn, askId);
     String oaName = panelLogic.findOAName(dbConn).trim();        
     request.setAttribute("oaName", oaName);
     request.setAttribute("kinds", kinds);
     request.setAttribute("askList", askList);
     request.setAttribute("toJson", toJson(types));
     request.setAttribute("ask", ask);
     request.setAttribute("goodAnswer", goodAnswer);
     request.setAttribute("otherAnswers", otherAnswers);
     request.setAttribute("pinLun", pinLun);
     
   }catch(Exception e){
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
     request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
     throw e;
   }
   return "/core/oaknow/oaknoweditask.jsp";
 }

 /**
  * 问题编辑页面的管理员删除答案问题
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String deleteAnswer(HttpServletRequest request, HttpServletResponse response) throws Exception{
   Connection dbConn = null;
   YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
   int answerId = Integer.parseInt(request.getParameter("answerId"));
   int flag = Integer.parseInt(request.getParameter("flag"));
   int userId = Integer.parseInt(request.getParameter("userId"));
   int askId = Integer.parseInt(request.getParameter("askId"));
   int id = 0;
   try{
    dbConn = requestDbConn.getSysDbConn();
     if( flag == 1){ //删除最佳答案
      id = oaLogic.deteteAnswerByFlag(dbConn, answerId, askId, 1, userId);
     }else if(flag == 0){ //删除一般答案
      id = oaLogic.deteteAnswerByFlag(dbConn, answerId, askId, 0, userId);
     }
     YHAjaxUtil.ajax(id, response);
  } catch (Exception e){  
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    throw e;
  }
   return null;
 }
/**
 * 问题编辑页面的采纳答案问题 
 * @param request
 * @param response
 * @return
 * @throws Exception 
 */
 public String agreeToGoodAnswer(HttpServletRequest request, HttpServletResponse response) throws Exception{
   Connection dbConn = null;
   YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
   int goodAnswerId = Integer.parseInt(request.getParameter("goodAnswerId")); //以前的最佳答案的id
   int newAnswerId  = Integer.parseInt(request.getParameter("newAnswerId"));  //现在采纳的最佳答案的id
   int oldUserId = Integer.parseInt(request.getParameter("oldUserId"));
   int newUserId = Integer.parseInt(request.getParameter("newUserId"));
   int oldAskId = Integer.parseInt(request.getParameter("oldAskId"));
   int newAskId = Integer.parseInt(request.getParameter("newAskId"));   
   int id= 0;
   try{
    dbConn = requestDbConn.getSysDbConn();   
    id = oaLogic.agreeToGoodAnswer(dbConn, goodAnswerId, newAnswerId, oldUserId, newUserId, oldAskId, newAskId);
    YHAjaxUtil.ajax(id, response);
  } catch (Exception e){    
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    throw e;
  }
   return null;
 } 
 /**
  * 问题编辑页面删除评论
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String deleteComment(HttpServletRequest request, HttpServletResponse response) throws Exception{
   Connection dbConn = null;
   YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
   int commentId = Integer.parseInt(request.getParameter("commentId"));
   int id = 0;
   try{
    dbConn = requestDbConn.getSysDbConn();
    id = oaLogic.deleteComment(dbConn, commentId);
    YHAjaxUtil.ajax(id, response);
  } catch (Exception e){
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    throw e;
  }
  return null; 
 }
 
 /**
  * 更改问题的答案
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String changeAnswers(HttpServletRequest request, HttpServletResponse response)throws Exception{
   Connection dbConn = null;
   YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
   int answerId = Integer.parseInt(request.getParameter("answerId").trim());
   String content = request.getParameter("content");
   try{
    dbConn = requestDbConn.getSysDbConn();
     int id = 0;
     id = oaLogic.changeAnswer(dbConn, answerId, content);
     YHAjaxUtil.ajax(id, response);
  } catch (Exception e){
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    throw e;
  }
  return null;   
 }
 /**
  * 更改问题
  * @param request
  * @param response
  * @return
  * @throws Exception
  */ 
 public String changeAsk(HttpServletRequest request, HttpServletResponse response)throws Exception{
   Connection dbConn = null;
   int id = 0;
   YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
   int askId = Integer.parseInt(request.getParameter("askId").trim());  //问题id
   int typeId = Integer.parseInt(request.getParameter("typeId").trim());//类型id
   String as = request.getParameter("ask");                             //问题题目
   String keyword = request.getParameter("keyword");                    //标签
   String content = request.getParameter("content");                    //问题内容
   YHOAAsk ask = new YHOAAsk();
   ask.setSeqId(askId);
   ask.setAsk(as);
   ask.setAskComment(content);
   ask.setReplyKeyWord(keyword);
   ask.setTypeId(typeId);
   try{
     dbConn = requestDbConn.getSysDbConn();
     id = oaLogic.changeAsk(dbConn, ask);
     YHAjaxUtil.ajax(id, response);
   }catch(Exception e){
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
     throw e;
   }
   return null;
 }
 
 /**
  * 生成json的工具类
  * @param list
  * @return
  */
 public String toJson(List<YHCategoriesType> list){
   StringBuffer sb = new StringBuffer();
   sb.append("[");
     if(list != null && list.size() != 0){
        for(int i=0; i < list.size(); i++){
           if(i < list.size()-1){
              sb.append(list.get(i).toString()).append(",");
           }else{
             sb.append(list.get(list.size()-1).toString());
           }
        }
     }
   sb.append("]");
   //YHOut.println(sb.toString()+"****************");
   return sb.toString();
 }
}
