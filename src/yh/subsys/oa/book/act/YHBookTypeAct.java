package yh.subsys.oa.book.act;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.book.data.YHBookInfo;
import yh.subsys.oa.book.data.YHBookType;
import yh.subsys.oa.book.data.YHPage;
import yh.subsys.oa.book.logic.YHBookQueryLogic;
import yh.subsys.oa.book.logic.YHBookTypeLogic;

public class YHBookTypeAct { 
  private static Logger log = Logger.getLogger(YHBookTypeAct.class);
  public String addBookType(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    Map<String, String> paramMap = fileForm.getParamMap();
    String typeName = paramMap.get("typeName");
    
        //String typeName = request.getParameter("typeName");
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        List<YHBookType> booktype  = YHBookTypeLogic.selectBook(dbConn, person, typeName);
        request.setAttribute("booktype", booktype);
        String book = "";
        for(int i = 0; i<booktype.size(); i++){
         book = String.valueOf(booktype.get(i));
        }
        if(book.equals("null"))
          return "/subsys/oa/book/type/add.jsp?typeName="+typeName;
        else
          return "/subsys/oa/book/type/index.jsp";
          
  }
  /**
   * 图书类型查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findBookType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
          YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
          YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
          List<YHBookType> booktype  = YHBookTypeLogic.findBookType(dbConn, person);
          //System.out.println(booktype);
          request.setAttribute("booktype", booktype);
          return "/subsys/oa/book/type/index.jsp";     
    }
  /**
   * (查出图书类型进行修改)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String editBookType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      String bookId = request.getParameter("bookId");
          YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
          YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
          YHBookType bookType = YHBookTypeLogic.editBookType(dbConn, person,bookId);
          request.setAttribute("bookType", bookType);
          return "/subsys/oa/book/type/edit.jsp";     
    }
  /**
   * 修改图书类型
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateBookType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      String typeId = request.getParameter("typeId");
      String typeName = request.getParameter("typeName");
          YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
          YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
          int bookType = YHBookTypeLogic.updateBookType(dbConn, person,Integer.parseInt(typeId),typeName);
         // request.setAttribute("bookType", bookType);
         // if(bookType == 0)
            //return "/subsys/oa/book/type/add.jsp?typeName="+typeName;
         // else
           return "/yh/subsys/oa/book/act/YHBookTypeAct/findBookType.act";     
    }
  /**
   * 删除图书类型名称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteBookType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      String bookId = request.getParameter("bookId");
          YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
          YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
          int flag = YHBookTypeLogic.deleteBookType(dbConn, person,Integer.parseInt(bookId));
         // request.setAttribute("bookType", bookType);
        return "/yh/subsys/oa/book/act/YHBookTypeAct/findBookType.act";
    }
  /**
   * 用jsons 查询图书类别
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectBookType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
          YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
          YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
          String loginName = person.getUserName();
          List<YHBookType> booktype  = YHBookTypeLogic.selectBookType(dbConn, person);
          request.setAttribute("booktype", booktype);
          request.setAttribute("loginName", loginName);
          return "/subsys/oa/book/manage/newbook.jsp";     
    }
  
   /**
    * 图书录入 查询图书类型
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
  public String jinruBookType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
          YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
          YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
          List<YHBookType> booktype  = YHBookTypeLogic.findBookType(dbConn, person);
          //System.out.println(booktype);
          request.setAttribute("booktype", booktype);
          return "/subsys/oa/book/manage/index.jsp";     
    }
  /**
   * 图书录入查询（带分页的）
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
     if(YHUtility.isNullorEmpty(orderflag)){
       orderflag="DEPT";
     }
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
      YHPage page = new YHPage(15, total, curruntNo);//此js方法只要填写每页显示的条数，总数据，当前显示的页数，就可以显示，首页上一页，下一页，尾页，转到第几页。  
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
    return "/subsys/oa/book/manage/list.jsp";
  }
  /**修改图书的基本信息(book_info)
   * 修改之前先进行查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectBookTypeId(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
      Connection dbConn = null;
      String bookSeqId = request.getParameter("bookSeqId");
      //bookSeqId.equalsIgnoreCase(arg0)
      try{
        String str = "";
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
         YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
         List<YHBookInfo> bookinfo = YHBookTypeLogic.selectBookTypeId(dbConn, person,Integer.parseInt(bookSeqId));
         List<YHBookType> booktype  = YHBookTypeLogic.selectBookType(dbConn, person);
         request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);//RET_STATE返回状态  RETURN_OK正确返回
         request.setAttribute(YHActionKeys.RET_MSRG, "图书模糊查询成功");//RET_MSRG 返回消息
         request.setAttribute(YHActionKeys.RET_DATA, str);//RET_DATA 返回数据
         request.setAttribute("bookinfo", bookinfo);
         request.setAttribute("booktype", booktype);
      }catch(Exception ex){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
    return "/subsys/oa/book/manage/edit.jsp";
  } 
  
  /**
   * 编辑 图书类型查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String editFindBookType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
          YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
          YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
          List<YHBookType> booktype  = YHBookTypeLogic.findBookType(dbConn, person);
          //System.out.println(booktype);
          request.setAttribute("booktype", booktype);
          return "/subsys/oa/book/manage/edit.jsp";     
    }
  
  public String deleteBookInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
          YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
          YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
          String noHiddenId =  request.getParameter("HiddenId");
         int ok = YHBookTypeLogic.deleteBookInfo(dbConn, person ,Integer.parseInt(noHiddenId));
          //System.out.println(booktype);
          //request.setAttribute("booktype", booktype);
         String url = "typeId="+"0"+"&lend="+""+"&bookName="+""+"&bookNo="+""+"&author="+""+"&isbn="+""+"&pub_house="+""+"&area="+"";
         if(ok!=0){
            return "/yh/subsys/oa/book/act/YHBookTypeAct/findBooks.act?"+url;
          }
         return "/yh/subsys/oa/book/act/YHBookTypeAct/findBooks.act?"+url;
    }
  /**
   *图书导出
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String SysExport(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    response.setCharacterEncoding(YHConst.CSV_FILE_CODE);  
    OutputStream ops = null;
    Connection conn = null;
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
    try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        YHPerson personLogin = (YHPerson) request.getSession().getAttribute(
        "LOGIN_USER");
        conn = requestDbConn.getSysDbConn();
        String fileName = URLEncoder.encode("图书信息导出.csv", "UTF-8");
        
        fileName = fileName.replaceAll("\\+", "%20");
        response.setHeader("Cache-control", "private");
        response.setContentType("text/text; charset=GBK");
        response.setHeader("Cache-Control","maxage=3600");
        response.setHeader("Pragma","public");
        response.setHeader("Content-disposition", "attachment; filename=\""
            + fileName + "\"");
        YHBookTypeLogic exportlog = new YHBookTypeLogic();
        ArrayList<YHDbRecord> dbL = exportlog.bookExportlog(book, orderflag, conn, personLogin);
         YHCSVUtil.CVSWrite(response.getWriter(), dbL);
    } catch (Exception ex) {
      throw ex;
    }
    return null;
}
  /**
   * 图书导入
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String importBookTypeinfo (HttpServletRequest request,HttpServletResponse response) throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
    .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    OutputStream ops = null;
    Connection conn = null;
    try{
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      List<YHBookInfo> bookinfo = YHBookTypeLogic.importBookTypeInfo(conn,person,request);
      
      request.setAttribute("bookinfo", bookinfo);
    } catch (Exception ex) {
    	 request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
         request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
         request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw ex;
    }
    return "/subsys/oa/book/manage/importOkOn.jsp";
   //return "/subsys/oa/asset/manage/mgs.jsp?num=" + num + "&numOne=" + numOne;
  } 
  /**
   * 导出图书模板
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
public String templetImport(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
  response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
  Connection conn = null;
  try {
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
    .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    YHPerson personLogin = (YHPerson) request.getSession().getAttribute(
    "LOGIN_USER");
    conn = requestDbConn.getSysDbConn();
    String fileName = URLEncoder.encode("图书信息导出.csv", "UTF-8");
    fileName = fileName.replaceAll("\\+", "%20");
    response.setHeader("Cache-control", "private");
    response.setCharacterEncoding("GBK");
    response.setContentType("text/text; charset=GBK");
    response.setHeader("Cache-Control","maxage=3600");
    response.setHeader("Pragma","public");
    response.setHeader("Content-disposition", "attachment; filename=\""
        + fileName + "\"");
    YHBookTypeLogic exportlog = new YHBookTypeLogic();
    ArrayList<YHDbRecord> dbL = exportlog.templetbookExportlog(conn, personLogin);
    YHCSVUtil.CVSWrite(response.getWriter(), dbL);
  } catch (Exception ex) {
    throw ex;
  }
  return null;
}
  /**
   * 删除图书所选择的图书
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteSelectBook(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
      Connection dbConn = null;
      try{
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
         YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
         String deleteStr = request.getParameter("deleteStr");
         YHBookTypeLogic deleteBook = new YHBookTypeLogic();
         int str =  deleteBook.deleteSelectBook(dbConn,deleteStr);
         request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);//RET_STATE返回状态  RETURN_OK正确返回
         request.setAttribute(YHActionKeys.RET_MSRG, "图书删除成功");//RET_MSRG 返回消息
        // request.setAttribute(YHActionKeys.RET_DATA, str);//RET_DATA 返回数据
           }catch(Exception ex){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
     return "/core/inc/rtjson.jsp";
  }
  
}
