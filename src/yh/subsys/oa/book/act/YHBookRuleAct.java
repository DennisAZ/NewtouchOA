package yh.subsys.oa.book.act;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.subsys.oa.book.data.YHBookManage;
import yh.subsys.oa.book.data.YHPage;
import yh.subsys.oa.book.logic.YHBookRuleLogic;
import yh.subsys.oa.book.logic.YHBookSmsLogic;


/**
 * 借还书管理
 * @author qwx110
 *
 */
public class YHBookRuleAct{

  /**
   * 跳转到借书管理页面
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String index(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;  
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHBookRuleLogic ruleLogic = new YHBookRuleLogic();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      List<YHBookManage> bmanages = ruleLogic.borrowConfirm(dbConn, user);       //借书确认
      List<YHBookManage> rmanages = ruleLogic.returnConfirm(dbConn, user);       //还书确认
      int total = ruleLogic.findRegCount(dbConn, user);
      String currNo = request.getParameter("currNo");
      int curruntNo = 1;
      if(YHUtility.isNullorEmpty(currNo)){
        curruntNo = 1;
      }else{
        curruntNo = Integer.parseInt(currNo);
      }      
      YHPage page = new YHPage(15, total, curruntNo);  
      List<YHBookManage> regManages = ruleLogic.findRegManages(dbConn, user, page);
      request.setAttribute("bmanages", bmanages);
      request.setAttribute("rmanages", rmanages);
      request.setAttribute("regManages", regManages);
      request.setAttribute("page", page);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/subsys/oa/book/borrow_manage/borrow/index.jsp";
  }
  
  /**
   * 同意或者退回借书申请
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String agreeBorrOrNot(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;  
    try{
      dbConn = requestDbConn.getSysDbConn();
      String flag = request.getParameter("flag");//1.同意, 0.不同意
      String seqId = request.getParameter("seqId");  //book_manage的seqId
      String bookNo = request.getParameter("bookNo");//图书编号
      bookNo = YHUtility.decodeURL(bookNo);
      String toId = request.getParameter("toId");
      YHBookRuleLogic ruleLogic = new YHBookRuleLogic();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      if("1".equalsIgnoreCase(flag)){          //同意
        ruleLogic.agreeToBorr(dbConn, Integer.parseInt(seqId), bookNo);
        String content = user.getUserName() + "同意了你的借书申请， 图书编号："+bookNo;      
        YHBookSmsLogic.sendSms(user, dbConn, content, "", toId, null); //立即发送给用户
        Date returnDate = ruleLogic.getReturnDate(dbConn, Integer.parseInt(seqId));//获得还书时间
        Date remindDate = YHUtility.getDayBefore(returnDate, 2);   //提前2天提醒
        String remind = "你借的图书(编号：" + bookNo +") 于" + returnDate +"到期， 请按时归还！";      
        YHBookSmsLogic.sendSms(user, dbConn, remind, "", toId, remindDate);
      }else if("0".equalsIgnoreCase(flag)){    //不同意
        ruleLogic.notAgreeToBorr(dbConn, Integer.parseInt(seqId), bookNo);
        String content = user.getUserName() + "拒绝了你的借书申请， 图书编号："+bookNo;   
        YHBookSmsLogic.sendSms(user, dbConn, content, "", toId, null);
      }
    } catch (Exception e){     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
    return "/yh/subsys/oa/book/act/YHBookRuleAct/index.act";
  }
  
  /**
   * 同意或者退回还书申请
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String agreeReturnOrNot(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;  
    try{
      dbConn = requestDbConn.getSysDbConn();
      String flag = request.getParameter("flag");//1.同意, 0.不同意
      String seqId = request.getParameter("seqId");  //book_manage的seqId
      String bookNo = request.getParameter("bookNo");//图书编号
      bookNo = YHUtility.decodeURL(bookNo);
      String toId = request.getParameter("toId");
      YHBookRuleLogic ruleLogic = new YHBookRuleLogic();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      if("1".equalsIgnoreCase(flag)){          //同意
        ruleLogic.agreeToReturn(dbConn, Integer.parseInt(seqId), bookNo);
        String content = user.getUserName() + "同意了你的还书申请， 图书编号："+bookNo;      
        YHBookSmsLogic.sendSms(user, dbConn, content, "", toId, null); //立即发送给用户       
      }else if("0".equalsIgnoreCase(flag)){    //不同意
        ruleLogic.notAgreeToBorr(dbConn, Integer.parseInt(seqId));
        String content = user.getUserName() + "拒绝了你的还书申请， 图书编号："+bookNo;   
        YHBookSmsLogic.sendSms(user, dbConn, content, "", toId, null);
      }      
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
    return "/yh/subsys/oa/book/act/YHBookRuleAct/index.act";
  }
  
  /**
   * 借书登记(有管理员直接登记)
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
      
      //发内部短信提醒
      //String content = user.getUserName() + "同意了你的借书申请， 图书编号："+bookNo;   
      //YHBookSmsLogic.sendSms(user, dbConn, content, "", toId, null); //立即发送给用户       
      Date remindDate = YHUtility.getDayBefore(manage.getReturnDate(), 2);  
      //YHOut.println(dateFormat(manage.getReturnDate()));
      String remind = "你借的图书(编号:"+bookNo +")将在"+ dateFormat(manage.getReturnDate())+"到期，请及时归还！"; 
      YHBookSmsLogic.sendSms(user, dbConn, remind, "", toId, remindDate); //还书日期前2天提醒 
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
   * 管理员直接点击还书
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String returnBookByAdmin(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null; 
    try{
      dbConn = requestDbConn.getSysDbConn();
      String bookNo = request.getParameter("bookNo");//图书编号
      bookNo = YHUtility.decodeURL(bookNo);
      String borrowId = request.getParameter("borrowId");// 借书id
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      YHBookRuleLogic ruleLogic = new YHBookRuleLogic();
      int  ok = ruleLogic.returnBookByReg(dbConn, Integer.parseInt(borrowId), bookNo);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
    return "/yh/subsys/oa/book/act/YHBookRuleAct/index.act";
  }
  
  /**
   * 历史记录查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findHistory(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null; 
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      YHBookRuleLogic ruleLogic = new YHBookRuleLogic();
      String userId = request.getParameter("toId");
      String bookNo = request.getParameter("bookNo");
      String startDate = request.getParameter("borrowDate");
      String endDate = request.getParameter("returnDate");
      String status = request.getParameter("bookStatus");     
      List<YHBookManage>  manages = 
        ruleLogic.findManagerByBookNo(dbConn, userId, bookNo, startDate, endDate, status, String.valueOf(user.getSeqId()));
      request.setAttribute("manages", manages);
      request.setAttribute("toId", userId);
      request.setAttribute("bookNo", bookNo);
      request.setAttribute("startDate", startDate);
      request.setAttribute("endDate", endDate);
      request.setAttribute("status", status);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }    
    return "/subsys/oa/book/borrow_manage/borrow/result.jsp";
  }
  
  /**
   * 彻底删除历史记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteHistory(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHBookRuleLogic ruleLogic = new YHBookRuleLogic();
      int k = ruleLogic.deleteManage(dbConn, Integer.parseInt(seqId));
      if(k!=0){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"删除成功");
      }else{
        request.setAttribute(YHActionKeys.RET_STATE,1);
        request.setAttribute(YHActionKeys.RET_MSRG,"删除失败");
      } 
    } catch (Exception ex){
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public static String dateFormat(Date date){
    if(date != null){
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String ds = sdf.format(date);     
      return ds.toString();
    }else{
      return "";
    }    
  }
}
