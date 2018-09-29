package yh.subsys.oa.book.act;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
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
import yh.subsys.oa.book.data.YHBookInfo;
import yh.subsys.oa.book.data.YHBookManage;
import yh.subsys.oa.book.data.YHBookType;
import yh.subsys.oa.book.data.YHPage;
import yh.subsys.oa.book.logic.YHBookQueryLogic;
import yh.subsys.oa.book.logic.YHBookSmsLogic;
/**
 * 图书查询
 * @author qwx110
 *
 */
public class YHBookQueryAct{

  
  /**
   * 模糊查找
   * @param request
   * @param response
   * @return
   * @throws Exception
   * @throws SQLException
   */
  public String findBookNos(HttpServletRequest request,  HttpServletResponse response) throws Exception, SQLException{    
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;   
    try{
      dbConn = requestDbConn.getSysDbConn();
      String search = request.getParameter("condition");
        search = YHUtility.decodeURL(search); //解码
      if(YHUtility.isNullorEmpty(search)){
        search = "";
      }
      String userId = request.getParameter("userId");
      YHPerson user = null;
      if(!YHUtility.isNullorEmpty(userId)){
        user = new YHPerson();
        user.setSeqId(Integer.parseInt(userId));//从页面中传过来的用户信息
      }else{
        user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      }
      YHBookQueryLogic queryLogic = new YHBookQueryLogic();           
      List<YHBookInfo> books = queryLogic.findBookNos(dbConn, user, search);
      String jsons = toJsons2(books);
      request.setAttribute(YHActionKeys.RET_DATA, jsons);    
      request.setAttribute(YHActionKeys.RET_STATE, "0");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 图书类别
   * @param request
   * @param response
   * @return
   * @throws Exception
   * @throws SQLException
   */
  public String findBookTypes(HttpServletRequest request,  HttpServletResponse response)throws Exception, SQLException{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;   
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHBookQueryLogic queryLogic = new YHBookQueryLogic();
      List<YHBookType> types = queryLogic.findBookTypes(dbConn);
      String typeJson = toJsons(types);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, typeJson);      
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查询图书
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String findBooks(HttpServletRequest request,  HttpServletResponse response) throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null; 
    String typeId = request.getParameter("typeId");
    String lend = request.getParameter("lend");
    String bookName = request.getParameter("bookName");
    String bookNo = request.getParameter("bookNo");
    String author = request.getParameter("author");
    String isbn = request.getParameter("isbn");
    String pubHouse = request.getParameter("pub_house");
    String area = request.getParameter("area");
    String orderflag = request.getParameter("orderflag");
    YHBookInfo book = new YHBookInfo();
    if("all".equalsIgnoreCase(typeId)){
      typeId = "0";
    }
    book.setTypeId(Integer.parseInt(typeId));
    book.setLend(lend);
    book.setBookName(bookName);
    book.setBookNo(bookNo);
    book.setAuthor(author);
    book.setIsbn(isbn);
    book.setPubHouse(pubHouse);
    book.setArea(area);
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHBookQueryLogic queryLogic = new YHBookQueryLogic();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      int total = queryLogic.count(dbConn, book, orderflag, user);
      
      
      String currNo = request.getParameter("currNo");
      int curruntNo = 1;
      if(YHUtility.isNullorEmpty(currNo)){
        curruntNo = 1;
      }else{
        curruntNo = Integer.parseInt(currNo);
      }
      YHPage page = new YHPage(15, total, curruntNo);    
      List<YHBookInfo> findBooks = queryLogic.findBooks(dbConn, book, orderflag, user, page);
      request.setAttribute("books",findBooks);
      request.setAttribute("page",page);
      request.setAttribute("conditon",book);
      request.setAttribute("orderflag",orderflag);
    } catch (SQLException e){   
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }    
    return "/subsys/oa/book/query/list.jsp";
  }
  
  /**
   * 查询图书
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String findBooks2(HttpServletRequest request,  HttpServletResponse response) throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null; 
    String typeId = request.getParameter("typeId");
    String lend = request.getParameter("lend");
    String bookName = request.getParameter("bookName");
    bookName = URLDecoder.decode(bookName, YHConst.DEFAULT_CODE);
    String bookNo = request.getParameter("bookNo");
    bookNo = YHUtility.decodeURL(bookNo);
    String author = request.getParameter("author");
    author = YHUtility.decodeURL(author);
    String isbn = request.getParameter("isbn");
    isbn = YHUtility.decodeURL(isbn);
    String pubHouse = request.getParameter("pub_house");
    pubHouse = YHUtility.decodeURL(pubHouse);
    String area = request.getParameter("area");
    area = YHUtility.decodeURL(area);
    String orderflag = request.getParameter("orderflag");
    YHBookInfo book = new YHBookInfo();
    if("all".equalsIgnoreCase(typeId)){
      typeId = "0";
    }
    book.setTypeId(Integer.parseInt(typeId));
    book.setLend(lend);
    book.setBookName(bookName);
    book.setBookNo(bookNo);
    book.setAuthor(author);
    book.setIsbn(isbn);
    book.setPubHouse(pubHouse);
    book.setArea(area);
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHBookQueryLogic queryLogic = new YHBookQueryLogic();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      int total = queryLogic.count(dbConn, book, orderflag, user);
      
      
      String currNo = request.getParameter("currNo");
      int curruntNo = 1;
      if(YHUtility.isNullorEmpty(currNo)){
        curruntNo = 1;
      }else{
        curruntNo = Integer.parseInt(currNo);
      }
      YHPage page = new YHPage(15, total, curruntNo);    
      List<YHBookInfo> findBooks = queryLogic.findBooks(dbConn, book, orderflag, user, page);
      request.setAttribute("books",findBooks);
      request.setAttribute("page",page);
      request.setAttribute("conditon",book);
      request.setAttribute("orderflag",orderflag);
    } catch (SQLException e){   
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }    
    return "/subsys/oa/book/query/list.jsp";
  }
  
  
  /**
   * 图书类型
   * @param types
   * @return
   */
  public String toJsons(List<YHBookType> types){
    StringBuffer sb = new StringBuffer();
    sb.append("[");
      if(types.size()>0){
         for(int i=0; i<types.size(); i++){
            sb.append(types.get(i).toJson());
            if(i < types.size()-1){
              sb.append(",");
            }
         }
      }
    sb.append("]");
    return sb.toString();
  }
  
  /**
   * 图书编码
   * @param books
   * @return
   */
  public String toJsons2(List<YHBookInfo> books){
    StringBuffer sb = new StringBuffer();
    sb.append("[");
      if(books.size()>0){
         for(int i=0; i<books.size(); i++){
            sb.append(books.get(i).toJson());
            if(i < books.size()-1){
              sb.append(",");
            }
         }
      }
    sb.append("]");
    return sb.toString();
  }
  
  /**
   * 点击借阅
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String toRead(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null;   
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHBookQueryLogic queryLogic = new YHBookQueryLogic();      
      String buserId = request.getParameter("toId");//借书人id
      String bookNo = request.getParameter("bookNo");
      String borrowDate = request.getParameter("borrowDate");
      String returnDate = request.getParameter("returnDate");
      String bRemark = request.getParameter("bRemark");   
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      YHBookManage manage = new YHBookManage();
      manage.setBookNo(bookNo);
      manage.setBuserId(buserId);
      manage.setBorrowRemark(bRemark);
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
        manage.setBorrowDate(YHUtility.parseDate("yyyy-MM-dd",borrowDate));
        Date browDate = YHUtility.parseDate("yyyy-MM-dd",borrowDate);
        String boDate = YHUtility.getDateTimeStr(browDate).split(" ")[0];
        if(YHUtility.isNullorEmpty(returnDate)){
          Date afterDate = YHUtility.getDayAfter(boDate, 30);
          String afDate = YHUtility.getDateTimeStr(afterDate).split(" ")[0];       
          manage.setReturnDate(YHUtility.parseDate("yyyy-MM-dd",afDate));
        }else{
          manage.setReturnDate(YHUtility.parseDate("yyyy-MM-dd",returnDate));
        } 
      }
      int k =  queryLogic.toReadStatus(dbConn,  manage, user);
      if(k!=0 && k != -10){        
        String toId = queryLogic.findManagerIds(dbConn, bookNo);       
        String content = user.getUserName()+"提交了借书申请，请审批！";
        String url = request.getContextPath() + "/subsys/oa/book/act/YHBookRuleAct/index.act";
        YHBookSmsLogic.sendSms(user, dbConn, content, url, toId, null);
        request.setAttribute("message", "保存成功");
      } 
      if(k == -10){
        request.setAttribute("message", "此书已借出");
      }
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }     
    return "/subsys/oa/book/query/message.jsp";
  }
  
  /**
   * 待批借阅
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String toAllow(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null; 
    try{
      dbConn = requestDbConn.getSysDbConn();
      String status = request.getParameter("stauts");
      if(YHUtility.isNullorEmpty(status)){
        status = "0";
      }
      YHBookQueryLogic queryLogic = new YHBookQueryLogic();    
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      List<YHBookManage> manages = queryLogic.findBooksNoAllow(dbConn, user, status);
      request.setAttribute("manages", manages);
      request.setAttribute("status", status);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/subsys/oa/book/query/status.jsp";
  }
  
  /**
   * 借阅待批，借阅已准 删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteManage(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null; 
    String status = "0";
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHBookQueryLogic queryLogic = new YHBookQueryLogic();    
      String seqId = request.getParameter("seqId");
      status = request.getParameter("status");
      int k = queryLogic.deleteManage(dbConn, Integer.parseInt(seqId));      
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
    return "/yh/subsys/oa/book/act/YHBookQueryAct/toAllow.act?stauts=" + status;
  }
  
  /**
   * 点击还书(还书审批),给借书审批人发短信
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String returnBooks(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null; 
    String status = "1";
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHBookQueryLogic queryLogic = new YHBookQueryLogic();    
      String seqId = request.getParameter("seqId");
      status = request.getParameter("status");
      int ok = queryLogic.returnBook(dbConn, Integer.parseInt(seqId));
      if(ok != 0){
        YHBookManage aManage = queryLogic.findRUserIds(dbConn, Integer.parseInt(seqId));
        YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        String content = user.getUserName()+"归还了所借的图书，编号为:"+ aManage.getBookNo();
        String url = "";
        YHBookSmsLogic.sendSms(user, dbConn, content, url, aManage.getRuserId(), null);
      }
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/yh/subsys/oa/book/act/YHBookQueryAct/toAllow.act?stauts=" + status;
  }
  
  /**
   * 还书批准删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteFlag(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null; 
    String status = "1";
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHBookQueryLogic queryLogic = new YHBookQueryLogic();    
      String seqId = request.getParameter("seqId");
      status = request.getParameter("status");  
      String flag = request.getParameter("delFlag");
      int ok = queryLogic.deleteFlagByFlag(dbConn, Integer.parseInt(seqId), flag);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/yh/subsys/oa/book/act/YHBookQueryAct/toAllow.act?stauts=" + status;
  } 
  
  /**
   * 点击详情
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String detail(HttpServletRequest request,  HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
    Connection dbConn = null; 
    try{
      dbConn = requestDbConn.getSysDbConn();    
      String bookId = request.getParameter("bookId");
      YHBookQueryLogic queryLogic = new YHBookQueryLogic();    
      YHBookInfo aBook = queryLogic.findABook(dbConn, Integer.parseInt(bookId));
      List<YHBookManage> daipi =  queryLogic.findBookConditionByBookId(dbConn, aBook.getBookNo(), 0);//待批
      List<YHBookManage> weihuan =  queryLogic.findBookConditionByBookId(dbConn, aBook.getBookNo(), 1);//未还
      request.setAttribute("aBook", aBook);
      request.setAttribute("daipi", daipi);
      request.setAttribute("weihuan",weihuan);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }
    return "/subsys/oa/book/query/detail.jsp";
  }
}
