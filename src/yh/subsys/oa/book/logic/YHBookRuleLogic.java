package yh.subsys.oa.book.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.generics.YHSQLParamHepler;
import yh.subsys.oa.book.data.YHBookInfo;
import yh.subsys.oa.book.data.YHBookManage;
import yh.subsys.oa.book.data.YHPage;


/**
 * 借还书管理
 * @author qwx110
 *
 */
public class YHBookRuleLogic{
  
  /**
   * 借书确认
   * 1.找出登陆用户的管理的部门id串
   * 2.找出在登陆用户管理范围内的图书的bookNO
   * 3.根据bookno找出管理信息
   * @param loginUser 登陆用户
   * @return
   * @throws Exception 
   */
    public List<YHBookManage> borrowConfirm(Connection dbConn, YHPerson loginUser) throws Exception{
      String deptIds = getManageDeptId(dbConn, loginUser);
      List<YHBookInfo> bookNos = getBookNos(dbConn, deptIds);
      List<YHBookManage> manages = getBookManage(dbConn, YHBookRuleLogic.list2String(bookNos),0);
      return manages;
    }
    
    /**
     * 还书确认
     * @param dbConn
     * @param loginUser 登陆用户
     * @return
     * @throws Exception 
     */
    public List<YHBookManage> returnConfirm(Connection dbConn, YHPerson loginUser) throws Exception{
      String deptIds = getManageDeptId(dbConn, loginUser);
      List<YHBookInfo> bookNos = getBookNos(dbConn, deptIds);
      List<YHBookManage> manages = getBookManage(dbConn, YHBookRuleLogic.list2String(bookNos), 1);
      return manages;   
    }
   
    /**
     * 借书管理，管理员直接登记
     * @param dbConn
     * @param loginUser 登陆用户
     * @return
     */
    public List<YHBookManage> borrowManageByAdmin(Connection dbConn, YHPerson loginUser)throws Exception{
        return null;
    }
    
    /**
     * 获得登陆用户所管理的所有的部门id
     * @param dbConn
     * @param user
     * @return
     * @throws Exception 
     */
    public String getManageDeptId(Connection dbConn, YHPerson loginUser) throws Exception{
      PreparedStatement ps = null;
      ResultSet rs = null; 
      String sql = "select MANAGE_DEPT_ID from oa_literature_manager where "+ YHDBUtility.findInSet(String.valueOf(loginUser.getSeqId()), "MANAGER_ID");
      String deptIds = ""; 
      try{
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();
        while(rs.next()){
          deptIds += rs.getString("MANAGE_DEPT_ID")+",";
        }
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, rs, null);
      }
      return YHBookQueryLogic.quQong(deptIds);
    }
    
    /**
     * 查找在部门deptIds范围内的图书
     * @param dbConn
     * @param deptIds
     * @return
     * @throws Exception 
     */
    public List<YHBookInfo> getBookNos(Connection dbConn, String  deptIds) throws Exception{
      PreparedStatement ps = null;
      ResultSet rs = null; 
      String sql = "select SEQ_ID, BOOK_NO, BOOK_NAME from oa_literature_info ";     
      if (!"0".equals(deptIds)) {
        sql += " where DEPT in (" + deptIds +")";
      }
      List<YHBookInfo> books = new ArrayList<YHBookInfo>();
      try{
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();
        while(rs.next()){
          YHBookInfo book = new YHBookInfo();
          book.setSeqId(rs.getInt("SEQ_ID"));
          book.setBookNo(rs.getString("BOOK_NO"));
          book.setBookName(rs.getString("BOOK_NAME"));
          books.add(book);
        }
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, rs, null);
      }
      return books;      
    }
    
    /**
     * 把bookList转化为booknos串
     * @param books
     * @return
     */
    public static String list2String(List<YHBookInfo> books){
      String bookNos = "";
      if(books!=null && books.size()>0){
        for(int i=0; i<books.size(); i++){
          bookNos += "'" +YHUtility.encodeLike(books.get(i).getBookNo())+"',";
        }
        bookNos = YHBookQueryLogic.quQong(bookNos);
      }else{
        bookNos ="'-1'";
      }
      return bookNos;
    }
    
    /**
     * 返回需要确认的借书列表
     * @param dbConn
     * @param bookNos
     * @return
     */
    public List<YHBookManage> getBookManage(Connection dbConn, String bookNos, int state)throws Exception{
      PreparedStatement ps = null;
      ResultSet rs = null; 
      String sql = "select SEQ_ID, BUSER_ID,BOOK_NO, BORROW_DATE, RETURN_DATE, RUSER_ID, BOOK_STATUS, STATUS,BORROW_REMARK from oa_literature_manage where BOOK_STATUS='"+ state +"' " +
      		         "and STATUS='0' and (REG_FLAG='0' or REG_FLAG is NULL) and BOOK_NO in(" +bookNos +")";
     
      List<YHBookManage> manages = new ArrayList<YHBookManage>();
      //YHOut.println(sql);
      try{
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();
        while(rs.next()){
          YHBookManage manage = new YHBookManage();
          manage.setSeqId(rs.getInt("SEQ_ID"));
          manage.setBuserId(rs.getString("BUSER_ID"));
          manage.setBookNo(rs.getString("BOOK_NO"));
          manage.setBorrowDate(rs.getDate("BORROW_DATE"));
          manage.setReturnDate(rs.getDate("RETURN_DATE"));
          manage.setRuserId(rs.getString("RUSER_ID"));
          manage.setBookStatus(rs.getString("BOOK_STATUS"));
          manage.setStatus(rs.getString("STATUS"));
          manage.setBookName(findBookName(dbConn, rs.getString("BOOK_NO")));
          manage.setBorPersonName(findUserNameBySeqId(dbConn, Integer.parseInt(rs.getString("BUSER_ID"))));
          String reUserId= rs.getString("RUSER_ID");
          if(YHUtility.isNullorEmpty(reUserId)){
            reUserId = "-222";
          }
          String reUserName = findUserNameBySeqId(dbConn, Integer.parseInt(reUserId));
          manage.setRegUserName(reUserName);
          manage.setBorrowRemark(rs.getString("BORROW_REMARK"));
          manages.add(manage);
        }
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, rs, null);
      }
      return manages;
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
     * 改变状态（点击同意，退回）
     * bookStatus=0， status=0 借书待批
     * bookStatus=0， status=1 借书同意
     * bookStatus=0， status=2 借书不同意（退回）
     * bookStatus=1， status=0 还书待批
     * bookStatus=1， status=1 还书同意
     * bookStatus=1， status=2 还书不同意（退回）
     * @param dbConn
     * @param seqId
     * @param bookStatus
     * @param status
     * @return
     * @throws Exception
     */
    public int agreeStatus(Connection dbConn, int  seqId, int bookStatus, int status)throws Exception{
      PreparedStatement ps = null;     
      String sql = "update oa_literature_manage set book_status='"+ bookStatus +"', status='"+ status +"' where SEQ_ID =" + seqId;
      try{
        ps = dbConn.prepareStatement(sql);
        int ok = ps.executeUpdate();
        return ok;
      } catch (Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, null, null);
      }   
    }
    
    /**
     * 同意借阅
     * 1.查看这本书的数量 amt
     * 2.查看这本书被借的数量 bamt
     * 3. 若bamt = amt, 则设置oa_literature_info.lend = 0, 更新oa_literature_manage.book_status和status
     *    若 bamt < amt  则 更新oa_literature_manage.book_status和status
     * @param dbConn
     * @param seqId
     * @return
     * @throws Exception
     */
    public int agreeToBorr(Connection dbConn, int  seqId, String bookNo)throws Exception{
      int amt = findBookCount(dbConn, bookNo);
      //int bamt = borrowCount(dbConn, bookNo);
      int bamt =0;
      YHBookQueryLogic qLogic = new YHBookQueryLogic();
      List<YHBookManage> list = qLogic.findBookConditionByBookId(dbConn,bookNo, 1);
      if(list!=null && list.size() >0){
    	  bamt = list.size();
      }
      if(amt == bamt+1){
        int ok = updateLend(dbConn,bookNo,1);
      }
      return agreeStatus(dbConn, seqId, 0, 1);
    }
    
    /**
     * 不同意借阅（退回）
     * @param dbConn
     * @param seqId
     * @param bookNo
     * @return
     * @throws Exception
     */
    public int notAgreeToBorr(Connection dbConn, int  seqId, String bookNo)throws Exception{
     int k = agreeStatus(dbConn, seqId, 0, 2);
     int o = updateLend(dbConn,bookNo,0);
     return k;
    }
    
    /**
     * 不同意借阅（退回）
     * @param dbConn
     * @param seqId
     * @return
     * @throws Exception
     */
    public int notAgreeToBorr(Connection dbConn, int  seqId)throws Exception{
      return agreeStatus(dbConn, seqId, 0, 2);
    }
    
    /**
     * 同意还书
     * @param dbConn
     * @param seqId
     * @param bookNo
     * @return
     * @throws Exception
     */
    public int agreeToReturn(Connection dbConn, int  seqId, String bookNo)throws Exception{
      int k = agreeStatus(dbConn, seqId, 1, 1);
      int o = updateLend(dbConn,bookNo,0);
      return k;
    }
    
    /**
     * 同意还书
     * @param dbConn
     * @param seqId
     * @return
     * @throws Exception
     */
    public int agreeToReturn(Connection dbConn, int  seqId)throws Exception{
      return agreeStatus(dbConn, seqId, 1, 1);
    }
    
    /**
     * 不同意还书
     * @param dbConn
     * @param seqId
     * @return
     * @throws Exception
     */
    public int notAgreeToReturn(Connection dbConn, int  seqId)throws Exception{
      return agreeStatus(dbConn, seqId, 1, 2);
    }
    
    /**
     * 查找这本书被借了多少本
     * @param dbConn
     * @param bookNo
     * @return
     * @throws Exception
     */
    public int borrowCount(Connection dbConn, String bookNo)throws Exception{
      PreparedStatement ps = null;
      ResultSet rs = null;    
      String sql = "select count(*) from oa_literature_manage where BOOK_NO = '"+ bookNo +"'"; 
      try{
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();
        if(rs.next()){
          return rs.getInt(1);
        }
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, rs, null);
      }
      return 0;
    }
    
    /**
     * 查找这本书的数量
     * @param dbConn
     * @param bookNo
     * @return
     * @throws Exception
     */
    public int findBookCount(Connection dbConn, String bookNo)throws Exception{
      PreparedStatement ps = null;
      ResultSet rs = null;    
      String sql = "select  AMT from oa_literature_info where BOOK_NO = '"+ bookNo +"'"; 
      try{
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();
        if(rs.next()){
          return rs.getInt(1);
        }
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, rs, null);
      }
      return 0;
    }
    
    /**
     * 更新图说的借阅状态
     * @param dbConn
     * @param bookNo
     * @param flag 0或1
     * @return
     * @throws Exception
     */
    public int updateLend(Connection dbConn, String bookNo, int flag)throws Exception{
      PreparedStatement ps = null;    
      String sql = "UPDATE oa_literature_info set LEND='"+ flag +"'  where BOOK_NO = '"+ bookNo+"'"; 
      try{
        ps = dbConn.prepareStatement(sql);
        int k = ps.executeUpdate();
        return k;
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, null, null);
      }   
    }
    
    /**
     * 查找还书时间
     * @param dbConn
     * @param borrowId
     * @return
     * @throws Exception
     */
    public Date getReturnDate(Connection dbConn, int borrowId)throws Exception{
      PreparedStatement ps = null;  
      ResultSet rs = null;    
      String sql = "select RETURN_DATE FROM oa_literature_manage where seq_id = " + borrowId; 
      try{
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();
        if(rs.next()){
          return rs.getDate("RETURN_DATE");
        }
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, rs, null);
      }  
      return null;
    }
    
    /**
     * 借书登记（管理员直接操作）
     * @param dbConn
     * @param user
     * @param borrowId
     * @throws Exception 
     */
    public int regBorrowBook(Connection dbConn, YHPerson user, YHBookManage manage) throws Exception{
      PreparedStatement ps = null; 
      String sql = " insert into oa_literature_manage(BUSER_ID, BOOK_NO, BORROW_DATE, BORROW_REMARK, RUSER_ID,RETURN_DATE, BOOK_STATUS, STATUS, REG_FLAG)" +
                   "values (?,?,?,?,?,?,?,?,?)"; 
      try{
        ps = dbConn.prepareStatement(sql);
        ps.setString(1, manage.getBuserId());
        ps.setString(2, manage.getBookNo());
        ps.setDate(3, new java.sql.Date(manage.getBorrowDate().getTime()));
        ps.setString(4, manage.getBorrowRemark());
        ps.setString(5, String.valueOf(user.getSeqId()));
        ps.setDate(6, new java.sql.Date(manage.getReturnDate().getTime()));
        ps.setString(7, "0");
        ps.setString(8, "1");
        ps.setString(9, "1");
        int k = ps.executeUpdate();
        return k;
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, null, null);
      }  
    }
   
   /**
    * 借书登记（管理员直接操作）
    * @param dbConn
    * @param user
    * @param manage
    * @throws Exception
    */
   public void regBookByAdmin(Connection dbConn, YHPerson user, YHBookManage manage)throws Exception{
     int ok = regBorrowBook(dbConn, user, manage);//插入借书信息
     int amt = findBookCount(dbConn, manage.getBookNo());
     int bamt = 0;
     if(ok != 0){
        bamt = borrowCount(dbConn, manage.getBookNo());
        if(bamt ==  amt){
          updateLend(dbConn, manage.getBookNo(), 1);
        }
     }
   }
     
    
    /**
     * 借书管理 
     * @param dbConn
     * @param user
     * @param page
     * @return
     * @throws Exception
     */
    public List<YHBookManage> findRegManages(Connection dbConn, YHPerson user, YHPage page)throws Exception{      
      PreparedStatement ps = null;  
      ResultSet rs = null;    
      String sql = "select SEQ_ID, BUSER_ID,BOOK_NO, BORROW_DATE, RETURN_DATE, RUSER_ID, BOOK_STATUS, STATUS,BORROW_REMARK " +
      		         " from oa_literature_manage where BOOK_STATUS='0' " +
      		         " and status='1' and REG_FLAG='1' and RUSER_ID ="+ user.getSeqId() +" order by RETURN_DATE desc"; 
      List<YHBookManage> manages = new ArrayList<YHBookManage>();
      try{
        ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
        
        ResultSetMetaData rsmd = null;
        ps.setMaxRows(page.getCurrentPageIndex() * page.getPageSize());
        rs = ps.executeQuery();
        rsmd = ps.getMetaData();
        rsmd.getColumnCount();// 得到多少列
        rs.first();   
        rs.relative((page.getCurrentPageIndex()-1) * page.getPageSize() -1);             
        while(rs.next()){
          YHBookManage manage = new YHBookManage();
          manage.setSeqId(rs.getInt("SEQ_ID"));
          manage.setBuserId(rs.getString("BUSER_ID"));
          manage.setBookNo(rs.getString("BOOK_NO"));
          manage.setBorrowDate(rs.getDate("BORROW_DATE"));
          manage.setReturnDate(rs.getDate("RETURN_DATE"));
          manage.setRuserId(rs.getString("RUSER_ID"));
          manage.setBookStatus(rs.getString("BOOK_STATUS"));
          manage.setStatus(rs.getString("STATUS"));
          manage.setBookName(findBookName(dbConn, rs.getString("BOOK_NO")));
          manage.setBorPersonName(findUserNameBySeqId(dbConn, Integer.parseInt(rs.getString("BUSER_ID"))));
          String reUserId= rs.getString("RUSER_ID");
          if(YHUtility.isNullorEmpty(reUserId)){
            reUserId = "-222";
          }
          String reUserName = findUserNameBySeqId(dbConn, Integer.parseInt(reUserId));
          manage.setRegUserName(reUserName);
          if(rsmd.getColumnType(9) == java.sql.Types.CLOB){
            manage.setBorrowRemark(YHSQLParamHepler.clobToString(rs.getClob(9)));
          }else  {
            manage.setBorrowRemark(rs.getString("BORROW_REMARK"));
          }
          manages.add(manage);
        }
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, rs, null);
      }  
      return manages;
    }
    
    /**
     * 查找总的有管理员直接登记的
     * @param dbConn
     * @param user
     * @return
     * @throws Exception
     */
    public int findRegCount(Connection dbConn, YHPerson user)throws Exception{
      PreparedStatement ps = null;  
      ResultSet rs = null;    
      String sql = "select count(*)" +
                   " from oa_literature_manage where BOOK_STATUS='0' " +
                   " and status='1' and REG_FLAG='1' and RUSER_ID ="+ user.getSeqId(); 
      try{
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();        
        if(rs.next()){
          return rs.getInt(1);
        }
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, rs, null);
      }  
      return 0;
    }
    
    /**
     * 管理员直接点击还书
     * @param dbConn
     * @param user
     * @throws Exception
     */
    public int returnBookByAdmin(Connection dbConn,  int seqId)throws Exception{
      PreparedStatement ps = null;    
      String sql = "update oa_literature_manage set BOOK_STATUS='1', STATUS='1',REAL_RETURN_TIME=? where seq_id= ?"; 
      try{
        ps = dbConn.prepareStatement(sql);
        ps.setDate(1, new java.sql.Date(new Date().getTime()));
        ps.setInt(2, seqId);
        int k = ps.executeUpdate();
        return k;
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, null, null);
      }   
    }
    
    /**
     * 管理员点击还书时的操作
     * @param dbConn
     * @param seqId
     * @param bookNo
     * @return
     * @throws Exception
     */
    public int returnBookByReg(Connection dbConn,  int seqId, String bookNo) throws Exception{
      int ok = returnBookByAdmin(dbConn, seqId);
      int two = 0;
      if(ok != 0){
        two = updateLend(dbConn, bookNo, 0);
      }
      return two;
    }
    
    /**
     * 查询历史记录
     * @param dbConn
     * @param manage
     * @return
     * @throws Exception
     */
    public List<YHBookManage> findHistory(Connection dbConn, String buserId, String bookNo, 
                                          String startDate, String endDate, String bookStatus, String bookNos, String loginUserId)throws Exception{
      PreparedStatement ps = null;  
      ResultSet rs = null;    
      String sql = "select SEQ_ID, BUSER_ID, BOOK_NO, BORROW_DATE, RETURN_DATE, " +
      		"          REAL_RETURN_TIME, RUSER_ID, BOOK_STATUS, STATUS, BORROW_REMARK from oa_literature_manage " +
      		"         where STATUS!='2' and BOOK_NO in (" + bookNos +")"; 
      if(!YHUtility.isNullorEmpty(buserId)){
          sql += " and BUSER_ID = " + buserId;
      }
      if(!YHUtility.isNullorEmpty(bookNo)){
        sql += " and BOOK_NO ='" + bookNo +"'";
      }
      if(!YHUtility.isNullorEmpty(startDate)){
        sql += " and " + YHDBUtility.getDateFilter("BORROW_DATE", startDate, ">=");
      }
      if(!YHUtility.isNullorEmpty(endDate)){
        sql += " and " + YHDBUtility.getDateFilter("BORROW_DATE", endDate, "<=");
      }
      if(!YHUtility.isNullorEmpty(bookStatus)){
         if("1".equalsIgnoreCase(bookStatus)){
           sql += " and BOOK_STATUS = '1' and STATUS = '1'";
         }else if("0".equalsIgnoreCase(bookStatus)){
           sql += " and ((BOOK_STATUS = '0' and STATUS = '1' ) or (BOOK_STATUS = '1' and STATUS = '0' )) ";
         }
      }else{
        sql += " and ((BOOK_STATUS='0' and STATUS='1') or (BOOK_STATUS='1' and STATUS='0') or (BOOK_STATUS='1' and STATUS='1'))";
      }
      sql += " order by RETURN_DATE DESC";
      List<YHBookManage> manages = new ArrayList<YHBookManage>();
      try{
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery(); 
       
        while(rs.next()){
          YHBookManage manage = new YHBookManage();
          manage.setSeqId(rs.getInt("SEQ_ID"));
          manage.setBuserId(rs.getString("BUSER_ID"));
          manage.setBookNo(rs.getString("BOOK_NO"));
          manage.setBookName(findBookName(dbConn, rs.getString("BOOK_NO")));
          manage.setBorrowDate(rs.getDate("BORROW_DATE"));
          manage.setReturnDate(rs.getDate("RETURN_DATE"));
          manage.setRealReturnTime(rs.getDate("REAL_RETURN_TIME"));
          manage.setRuserId(rs.getString("RUSER_ID"));
          manage.setStatus(rs.getString("STATUS"));
          manage.setBookStatus(rs.getString("BOOK_STATUS"));
          manage.setBorrowRemark(rs.getString("BORROW_REMARK"));
          manage.setBorPersonName(findUserNameBySeqId(dbConn, Integer.parseInt(rs.getString("BUSER_ID"))));
          int id = -11;
          if(!YHUtility.isNullorEmpty(rs.getString("RUSER_ID"))){
            id = Integer.parseInt(rs.getString("RUSER_ID"));
          }
          manage.setRegUserName(findUserNameBySeqId(dbConn, id));
          manages.add(manage);
        }
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, rs, null);
      }      
      return manages;
    }
    
    /**
     *查找历史记录
     *1.查出登陆用户所管理的部门id串
     *2.根据部门串查出在这些部门里德所有的图书的编号串
     *3.根据编号串，查出登陆用户所能查出的所有的借书信息
     * @param dbConn
     * @param userId
     * @param bookNo
     * @param startDate
     * @param endDate
     * @param status
     * @return
     * @throws Exception
     */
    public List<YHBookManage> findManagerByBookNo(Connection dbConn, String buserId, String bookNo, 
        String startDate, String endDate, String status, String myUserId)throws Exception{
      String manageDeptId = findManageDeptId(dbConn, myUserId);
      List<YHBookInfo> bookInfos = getBookNos(dbConn, manageDeptId);
      String bookNos = list2String(bookInfos);
      List<YHBookManage> bookManages = findHistory(dbConn, buserId, bookNo, startDate, endDate, status,bookNos, myUserId);      
      return  bookManages;
    }
    
    /**
     * 查找登陆用户所管理的部门
     * @param dbConn
     * @param userId
     * @return
     * @throws Exception
     */
    public String findManageDeptId(Connection dbConn, String userId)throws Exception{
      PreparedStatement ps = null;  
      ResultSet rs = null;    
      String sql = "select MANAGE_DEPT_ID  from oa_literature_manager where "+YHDBUtility.findInSet(userId, "MANAGER_ID"); 
      String deptIds = "";
      //YHOut.println(sql);
      try{
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery();        
        while(rs.next()){
          deptIds += rs.getString("MANAGE_DEPT_ID") +",";
        }
      }catch(Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, rs, null);
      }      
      return YHBookQueryLogic.quQong(deptIds);
    }   
    
    /**
     * 彻底删除历史记录
     * @param dbConn
     * @param borrowId
     * @return
     * @throws Exception
     */
    public int deleteManage(Connection dbConn, int  borrowId)throws Exception{
      PreparedStatement ps = null;  
      String sql = "delete from oa_literature_manage where SEQ_ID =?";
      try{
        ps = dbConn.prepareStatement(sql);
        ps.setInt(1, borrowId);
        int k = ps.executeUpdate();
        return k;
      } catch (Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps, null, null);
      }
    }
}
