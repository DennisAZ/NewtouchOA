package yh.subsys.oa.book.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.subsys.oa.book.data.YHBookManage;


/**
 * 还书管理查询
 * @author Administrator
 *
 */
public class YHReturnBookSelectLogic{
  public List<YHBookManage> returnBookSelect(Connection dbConn, YHPerson user, String beginTime, String endTime, YHBookManage bookmanage) throws Exception{ 
  PreparedStatement ps = null;
  ResultSet rs = null;
  String selectSql = "select SEQ_ID, BUSER_ID, BOOK_NO, BORROW_DATE, RETURN_DATE, RUSER_ID, BOOK_STATUS,STATUS,BORROW_REMARK from oa_literature_manage where 1=1 ";
  if(!YHUtility.isNullorEmpty(bookmanage.getBuserId())){
    selectSql += " and BUSER_ID ="+ bookmanage.getBuserId();
  }
  if(!YHUtility.isNullorEmpty(bookmanage.getBookNo())){
    selectSql += " and BOOK_NO ='"+bookmanage.getBookNo()+"'";
  }
  if(!YHUtility.isNullorEmpty(beginTime)){
    selectSql += " and " + YHDBUtility.getDateFilter("BORROW_DATE", beginTime, ">=");
  }
  if(!YHUtility.isNullorEmpty(endTime)){
    selectSql += " and " + YHDBUtility.getDateFilter("BORROW_DATE", endTime, "<=");
  }
  if(!YHUtility.isNullorEmpty(bookmanage.getBookStatus())){
    if("1".equalsIgnoreCase(bookmanage.getBookStatus())){ // 还书状态
      selectSql += " and BOOK_STATUS = '1' and STATUS = '1'";
    }else if("0".equalsIgnoreCase(bookmanage.getBookStatus())){//未还状态
      selectSql += " and ((BOOK_STATUS = '0' and STATUS = '1' ) or (BOOK_STATUS = '1' and STATUS = '0' )) ";
    }
 }else{
   selectSql += " and ((BOOK_STATUS='0' and STATUS='1') or (BOOK_STATUS='1' and STATUS='0') or (BOOK_STATUS='1' and STATUS='1'))";
 }
  selectSql += " and (DELETE_FLAG !='1' or DELETE_FLAG is null) order by RETURN_DATE DESC";
 // System.out.println(selectSql);
  List<YHBookManage> manages = new ArrayList<YHBookManage>();
  try{
    ps = dbConn.prepareStatement(selectSql);
    rs = ps.executeQuery();
    while(rs.next()){
      YHBookManage manage = new YHBookManage();
      manage.setSeqId(rs.getInt("SEQ_ID"));
      manage.setBuserId(rs.getString("BUSER_ID"));
      manage.setBookNo(rs.getString("BOOK_NO"));
      manage.setBookName(findBookName(dbConn, rs.getString("BOOK_NO")));
      manage.setBorrowDate(rs.getDate("BORROW_DATE"));
      manage.setReturnDate(rs.getDate("RETURN_DATE"));
      manage.setRuserId(rs.getString("RUSER_ID"));
      manage.setBookStatus(rs.getString("BOOK_STATUS"));
      manage.setStatus(rs.getString("STATUS"));
      manage.setBorrowRemark(rs.getString("BORROW_REMARK"));
      manage.setBorPersonName(findUserNameBySeqId(dbConn, Integer.parseInt(rs.getString("BUSER_ID"))));
      String reUserId= rs.getString("RUSER_ID");
      if(YHUtility.isNullorEmpty(reUserId)){
        reUserId = "-222";
      }
      String reUserName = findUserNameBySeqId(dbConn, Integer.parseInt(reUserId));
      manage.setRegUserName(reUserName);
      manages.add(manage);
    }
  } catch (SQLException e){
    throw e;
  }finally{
    YHDBUtility.close(ps, rs, null);
  } 
  return manages;
  }
  /**
   * 查找用户名

   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String findUserNameBySeqId(Connection dbConn, int  seqId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;    
    String sql = "select USER_NAME from person where SEQ_ID =" + seqId;
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return rs.getString("USER_NAME");
      }
    } catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return null;
  }
  /**
   * 查找书的名字
   * @param dbConn
   * @param bookNo
   * @return
   * @throws Exception
   */
  public String findBookName(Connection dbConn, String bookNo)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String name = "";
    String sql = "select BOOK_NAME from oa_literature_info where BOOK_NO = '"+ bookNo +"'"; 
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        name = rs.getString("BOOK_NAME");
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return name;
  }
  /**
   * 点击还书 更新图书的状态信息(book_manage)
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public int updateBookManage(Connection dbConn, String seqId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String name = "";
    int ok=0;
    String datetime = YHUtility.getCurDateTimeStr();
    String Datetime = datetime.substring(0,10);
    Date time = YHUtility.parseDate(Datetime);
    
    String sql = "update oa_literature_manage set book_status ='1', status='1', real_return_time=? where seq_id = "+seqId; 
    //System.out.println(sql);
    try{
      ps = dbConn.prepareStatement(sql);
      ps.setDate(1, new java.sql.Date(time.getTime()));
      ok = ps.executeUpdate();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return ok;
  }
  /**
   * 点击还书 更新图书 oa_literature_info借阅状态信息（lend）
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public int updateBookInfo(Connection dbConn, String bookNo)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String name = "";
    int okInfo=0;
    String datetime = YHUtility.getCurDateTimeStr();
    String Datetime = datetime.substring(0,10);
    
    String sql = "update oa_literature_info set lend ='0' where book_no = '"+bookNo+"'"; 
    try{
      ps = dbConn.prepareStatement(sql);
      okInfo = ps.executeUpdate();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return okInfo;
  }
  /**
   * 删除并保存历史记录
   * @param dbConn
   * @param bookNo
   * @return
   * @throws Exception
   */
  public int deleteSaveBook(Connection dbConn, String seqId)throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String name = "";
    int okInfo=0;
    /*String datetime = YHUtility.getCurDateTimeStr();
    String Datetime = datetime.substring(0,10);*/
    String sql = "update oa_literature_manage set DELETE_FLAG='1' where seq_id = '"+seqId+"'"; 
    try{
      ps = dbConn.prepareStatement(sql);
      okInfo = ps.executeUpdate();
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return okInfo;
  }
}
