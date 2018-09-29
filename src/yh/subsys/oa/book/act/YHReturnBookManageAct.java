package yh.subsys.oa.book.act;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.subsys.oa.book.data.YHBookManage;
import yh.subsys.oa.book.logic.YHBookRuleLogic;
import yh.subsys.oa.book.logic.YHBookSmsLogic;
import yh.subsys.oa.book.logic.YHReturnBookSelectLogic;


/**
 *
 */
public class YHReturnBookManageAct{
 /**
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String regBorrowBook(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null; 
    try{
      dbConn = requestDbConn.getSysDbConn();
      String toId = request.getParameter("toId");
      String bookNo = request.getParameter("bookNo");
      String borrowDate = request.getParameter("borrowDate"); //借书日期
      String returnDate = request.getParameter("returnDate"); //还书日期
      String remark = request.getParameter("remark");
      YHBookManage manage = new YHBookManage();
      manage.setBookNo(bookNo);
      manage.setBorrowRemark(remark);
      manage.setBuserId(toId);
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      if(YHUtility.isNullorEmpty(borrowDate)){
        Date now = new Date();
        String boDate = YHUtility.getDateTimeStr(now).split(" ")[0];
        manage.setBorrowDate(YHUtility.parseDate("yyyy-MM-dd",boDate));
        if(YHUtility.isNullorEmpty(returnDate)){
          Date afterDate = YHUtility.getDayAfter(boDate, 30);
          String afDate = YHUtility.getDateTimeStr(afterDate).split(" ")[0];       
          manage.setReturnDate(YHUtility.parseDate("yyyy-MM-dd",afDate));
        }else{
          manage.setReturnDate(YHUtility.parseDate("yyyy-MM-dd",returnDate));
        }        
      }else{        
        Date browDate = YHUtility.parseDate("yyyy-MM-dd",borrowDate);   
        manage.setBorrowDate(browDate);
        String boDate = YHUtility.getDateTimeStr(browDate).split(" ")[0];
        if(YHUtility.isNullorEmpty(returnDate)){
          Date afterDate = YHUtility.getDayAfter(boDate, 30);
          String afDate = YHUtility.getDateTimeStr(afterDate).split(" ")[0];       
          manage.setReturnDate(YHUtility.parseDate("yyyy-MM-dd",afDate));
        }else{
          manage.setReturnDate(YHUtility.parseDate("yyyy-MM-dd",returnDate));
        } 
      }
      YHBookRuleLogic ruleLogic = new YHBookRuleLogic();
      ruleLogic.regBookByAdmin(dbConn, user, manage);         //存数据库
      String content = user.getUserName() + "同意了你的借书申请， 图书编号："+bookNo;   
      YHBookSmsLogic.sendSms(user, dbConn, content, "", toId, null); //立即发送给用户       
      Date remindDate = YHUtility.getDayBefore(manage.getReturnDate(), 2);  
       request.setAttribute("message", "保存成功");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
    return "/subsys/oa/book/borrow_manage/return/msg.jsp";
  }
  /**
   * 还书管理查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String returnBookManage(HttpServletRequest request,  HttpServletResponse response)throws Exception{
       Connection dbConn = null; 
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String toId = request.getParameter("toId");
      String bookNo = request.getParameter("bookNo");
      String beginDate = request.getParameter("borrowDate");
      String endDate = request.getParameter("returnDate");
      String bookStatus = request.getParameter("bookStatus");
      YHBookManage manage = new YHBookManage();
      manage.setBuserId(toId);//借书人ID
      manage.setBookNo(bookNo);
      manage.setBookStatus(bookStatus);
      YHReturnBookSelectLogic returnbook = new YHReturnBookSelectLogic();
      List<YHBookManage> manages = returnbook.returnBookSelect(dbConn,person,beginDate,endDate, manage);
      request.setAttribute("manage", manages);
      request.setAttribute("toId", toId);
      request.setAttribute("bookNo", bookNo);
      request.setAttribute("startDate", beginDate);
      request.setAttribute("endDate", endDate);
      request.setAttribute("status", bookStatus);
    }catch (Exception e){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
        request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
        throw e;
      }
    return "/subsys/oa/book/borrow_manage/borrow/rebooksearch.jsp";
  }
  /**
   * 点击还书 图书信息状态改变
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateBookManage(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    Connection dbConn = null; 
    String url = "";
    try{
   YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
   dbConn = requestDbConn.getSysDbConn();
   YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
   int okManage = 0;
   int okInfo = 0;
   String toId = request.getParameter("userId");
   String bookNo = request.getParameter("bookNo");
   String beginDate = request.getParameter("startDate");
   String endDate = request.getParameter("endDate");
   String bookStatus = request.getParameter("status");
   String seqId = request.getParameter("seqId");
   String bookNo1 = request.getParameter("bookNo1");
   bookNo1 = URLDecoder.decode(bookNo1, "UTF-8");
   YHReturnBookSelectLogic updatebook = new YHReturnBookSelectLogic();
   okManage = updatebook.updateBookManage(dbConn, seqId);
   okInfo = updatebook.updateBookInfo(dbConn, bookNo1);
   url = "toId="+toId+"&bookNo="+bookNo +"&beginDate="+beginDate+"&endDate="+endDate+"&bookStatus="+bookStatus;
   if(okManage!=0 && okInfo!=0)
      return "/yh/subsys/oa/book/act/YHReturnBookManageAct/returnBookManage.act?"+url;
   
   /*YHBookManage manage = new YHBookManage();
   manage.setBuserId(toId);//借书人ID
   manage.setBookNo(bookNo);
   manage.setBookStatus(bookStatus);
   YHReturnBookSelectLogic returnbook = new YHReturnBookSelectLogic();
   List<YHBookManage> manages = returnbook.returnBookSelect(dbConn,person,beginDate,endDate, manage);
   request.setAttribute("manage", manages);
   request.setAttribute("toId", toId);
   request.setAttribute("bookNo", bookNo);
   request.setAttribute("startDate", beginDate);
   request.setAttribute("endDate", endDate);
   request.setAttribute("status", bookStatus);*/
 }catch (Exception e){
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
     request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
     throw e;
   }
 return "/yh/subsys/oa/book/act/YHReturnBookManageAct/returnBookManage.act?"+url;
}
  /**
   * 删除并保存历史记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteSaveBook(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    Connection dbConn = null; 
    String url ="";
    try{
   YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
   dbConn = requestDbConn.getSysDbConn();
   YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
   int okdelSave = 0;
   int okInfo = 0;
   String toId = request.getParameter("userId");
   String bookNo = request.getParameter("bookNo");
   String beginDate = request.getParameter("startDate");
   String endDate = request.getParameter("endDate");
   String bookStatus = request.getParameter("status");
   String seqId = request.getParameter("seqId");
   String bookNo2 = request.getParameter("bookNo2");
   bookNo2 = YHUtility.decodeURL(bookNo2);
   //System.out.println(seqId);
   YHReturnBookSelectLogic deleteSavebook = new YHReturnBookSelectLogic();
   okdelSave = deleteSavebook.deleteSaveBook(dbConn, seqId);
  
    url = "toId="+toId+"&bookNo="+bookNo +"&beginDate="+beginDate+"&endDate="+endDate+"&bookStatus="+bookStatus;
   if(okdelSave!=0)
      return "/yh/subsys/oa/book/act/YHReturnBookManageAct/returnBookManage.act?"+url;
   
 
 }catch (Exception e){
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
     request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
     request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
     throw e;
   }
 return "/yh/subsys/oa/book/act/YHReturnBookManageAct/returnBookManage.act?"+url;
}
}
