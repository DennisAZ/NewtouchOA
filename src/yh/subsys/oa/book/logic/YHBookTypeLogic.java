package yh.subsys.oa.book.logic;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import yh.core.data.YHDbRecord;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.book.data.YHBookInfo;
import yh.subsys.oa.book.data.YHBookType;
/**
 * 添加类型名称 并 查出类型名称
 * @author Administrator
 *
 */
public class YHBookTypeLogic {
  public static  List<YHBookType> selectBook(Connection conn, YHPerson person, String typeName) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    Statement stmt1 = null;
    ResultSet rs1 = null;
    Statement stmt2 = null;
    ResultSet rs2 = null;
    List<YHBookType> booktype = new ArrayList<YHBookType>(); 
    String sql = "select type_name from oa_literature_type where type_name = '"+YHUtility.encodeLike(typeName)+"'";
    String insertSql = "insert into oa_literature_type (type_name) values ('"+YHUtility.encodeLike(typeName)+"')";
    String findSql = "select seq_id, type_name from oa_literature_type order by seq_id";
    try{
      stmt = conn.createStatement();
      stmt2 = conn.createStatement();
      rs = stmt.executeQuery(sql);
      //System.out.println(sql);
      YHBookType book = new YHBookType();
      if(rs.next()){//如果名称相同，就返回null, 返回null 就不可以添加
        book.setTypeName(rs.getString("type_name"));
        book = null;
        booktype.add(book);
        return booktype;
      }else{//否侧可以添加，添加之后在查询出所有图书的类型
        stmt1 = conn.createStatement();
        stmt1.executeUpdate(insertSql);
        rs2 = stmt2.executeQuery(findSql);
        while(rs2.next()){
          YHBookType book1 = new YHBookType();
          book1.setSeqId(rs2.getInt("seq_id"));
          book1.setTypeName(rs2.getString("type_name"));
          booktype.add(book1);
        }
        return booktype;
      }
    }catch (Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(stmt, rs, null);
    }
  }
  /**
   * 查询图书类型名称
   * @param conn
   * @param person
   * @return
   * @throws Exception
   */
  public static  List<YHBookType> findBookType(Connection conn, YHPerson person) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;   
    String sql = "select seq_id, type_name from oa_literature_type order by seq_id desc";
    List<YHBookType> book = new ArrayList<YHBookType>();
    try{
       ps = conn.prepareStatement(sql);
       rs = ps.executeQuery();
       while(rs.next()){
         YHBookType bt = new YHBookType();
         bt.setSeqId(rs.getInt("seq_id"));
         bt.setTypeName(rs.getString("type_name"));
         book.add(bt);
       }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    
    return book;
  }
  /**
   * 编辑图书类型之前 进行查询图书类型
   * @param conn
   * @param person
   * @param bookId
   * @return
   * @throws Exception
   */
  public static YHBookType editBookType(Connection conn, YHPerson person,String bookId) throws Exception {
   PreparedStatement ps = null;
   ResultSet rs = null;
   String sql = "select seq_id, type_name from oa_literature_type where seq_id = "+bookId;
   try{
     ps = conn.prepareStatement(sql);
     rs = ps.executeQuery();
     YHBookType bt = new YHBookType();
     if(rs.next()){
       String name = rs.getString("type_name");
       if (!YHUtility.isNullorEmpty(name)) {
         name = name.trim();
       }
       else {
         name = "";
       }
        bt.setTypeName(tsziFu(name));
        bt.setSeqId(rs.getInt("seq_id"));
        return bt;
     }
   }catch(Exception ex){
     throw ex;
   }finally{
     YHDBUtility.close(ps, rs, null);
   }
    return null;
  }
  
  public static int updateBookType(Connection conn, YHPerson person,int typeId,String typeName) throws Exception {
   PreparedStatement ps = null;
   ResultSet rs = null;
   PreparedStatement ps1 = null;
   ResultSet rs1 = null;
   int ok = 0;
   String sql = "select type_name from oa_literature_type where type_name = '"+typeName+"'"; 
   String updateSql = "update oa_literature_type set type_name = ? where seq_id = ?";
   List<YHBookType> btype = new ArrayList<YHBookType>();
   try{
    ps = conn.prepareStatement(sql);
    rs = ps.executeQuery();
    if(rs.next()){
      /*YHBookType bt = new YHBookType();
      bt = null;
      btype.add(bt);*/
      return ok;
    }else{
      ps1 = conn.prepareStatement(updateSql);
      //System.out.println(updateSql);
      ps1.setString(1, typeName);
      ps1.setInt(2, typeId);
      ok = ps1.executeUpdate();
      return ok;
    }
   }catch(Exception ex){
     throw ex;
   }finally{
     YHDBUtility.close(ps, null, null);
   }
  }
  /**
   * 删除图书类型名称
   * @param conn
   * @param person
   * @param typeId
   * @param typeName
   * @return
   * @throws Exception
   */
  public static int deleteBookType(Connection conn, YHPerson person,int typeId) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    int flag = 0;
    String deleteSql = "delete from oa_literature_type where seq_id = "+typeId;
    try{
      ps = conn.prepareStatement(deleteSql);
      flag = ps.executeUpdate(); 
    }catch(Exception ex){
      throw ex;
    }finally{
       YHDBUtility.close(ps, rs, null);
    }
    return flag;
  }
  public static  List<YHBookType> selectBookType(Connection conn, YHPerson person) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;   
    String sql = "select seq_id, type_name from oa_literature_type";
    List<YHBookType> book = new ArrayList<YHBookType>();
    try{
       ps = conn.prepareStatement(sql);
       rs = ps.executeQuery();
       while(rs.next()){
         YHBookType bt = new YHBookType();
         bt.setSeqId(rs.getInt("seq_id"));
         bt.setTypeName(rs.getString("type_name"));
         book.add(bt);
       }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    
    return book;
  }
  /**修改图书的基本信息
   * 修改之前先进行查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public static  List<YHBookInfo> selectBookTypeId(Connection conn, YHPerson person, int seqId) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;   
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = "select seq_id, dept,book_name,book_no,type_id,author,isbn,pub_house,pub_date,area,amt,price,brief,[open],lend,borr_person,memo, attachment_id,attachment_name from oa_literature_info where seq_id ="+seqId;
    }else {
      sql = "select seq_id, dept,book_name,book_no,type_id,author,isbn,pub_house,pub_date,area,amt,price,brief,open,lend,borr_person,memo, attachment_id,attachment_name from oa_literature_info where seq_id ="+seqId;
    }
    List<YHBookInfo> book = new ArrayList<YHBookInfo>();
    try{
       ps = conn.prepareStatement(sql);
       rs = ps.executeQuery();
       while(rs.next()){
         YHBookInfo ttf = new YHBookInfo();
         ttf.setSeqId(rs.getInt("seq_id"));
         ttf.setDept(rs.getInt("dept"));
         ttf.setBookName(tsziFu(rs.getString("book_name")));
         ttf.setBookNo(tsziFu(rs.getString("book_no")));
         ttf.setTypeId(rs.getInt("type_id"));
         ttf.setAuthor(tsziFu(rs.getString("author")));
         ttf.setIsbn(rs.getString("isbn"));
         ttf.setPubHouse(tsziFu(rs.getString("pub_house")));
         ttf.setPubDate(tsziFu(rs.getString("pub_date")));
         ttf.setArea(tsziFu(rs.getString("area")));
         ttf.setAmt(rs.getInt("amt"));
         ttf.setPrice(rs.getDouble("price"));
         ttf.setBrief(rs.getString("brief"));
         ttf.setOpen(tsziFu(rs.getString("open")));
         
         ttf.setOpenNames(findDeptNames(conn,rs.getString("open")));
         
         
         ttf.setLend(tsziFu(rs.getString("lend")));
         ttf.setBorrPerson(tsziFu(rs.getString("borr_person")));
         ttf.setMemo(tsziFu(rs.getString("memo")));
         ttf.setAttachmentId(rs.getString("attachment_id"));
         ttf.setAttachmentName(rs.getString("attachment_name"));
         book.add(ttf);
       }
      // System.out.println(sql);
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    
    return book;
  }
  /**
   * 转换特殊字符
   * @param zf
   * @return
   * @throws Exception
   */
  public static String tsziFu(String zf) throws Exception {
    String newStr ="";
    if(!YHUtility.isNullorEmpty(zf)){
       newStr = zf.replaceAll("\"", "&quot;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
    return newStr;
  }
  /** 
  * 返回部门名字串 
  * @param dbConn 
  * @param deptIds 
  * @return 
  * @throws Exception 
  */ 
  public static String findDeptNames(Connection dbConn, String deptIds) throws Exception{ 
  PreparedStatement ps = null; 
  ResultSet rs = null; 
  String sql = "select DEPT_NAME from oa_department where SEQ_ID in (" + deptIds +")"; 
  if(YHUtility.isNullorEmpty(deptIds)){ 
    deptIds = "-1"; 
  } 
  String dept = ""; 
  if("0".equalsIgnoreCase(deptIds) || "ALL_DEPT".equalsIgnoreCase(deptIds)){ 
    dept = "全体部门"; 
  return dept; 
  } 
  try{ 
    ps = dbConn.prepareStatement(sql); 
    rs = ps.executeQuery(); 
  while(rs.next()){ 
    dept += rs.getString("DEPT_NAME") +","; 
  } 
  } catch (Exception e){ 
      throw e; 
  }finally{ 
    YHDBUtility.close(ps, rs, null); 
  } 
    dept = dept.substring(0, dept.lastIndexOf(",")==-1?0:dept.lastIndexOf(",")); 
    return dept; 
  }
  /**
   * 删除基本信息
   * @param conn
   * @param person
   * @return
   * @throws Exception
   */
  public static int deleteBookInfo(Connection conn, YHPerson person ,int noHiddenId ) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;   
    int ok =0;
    String deleteSql = "delete from oa_literature_info where seq_id ="+noHiddenId;
    
    try{
       ps = conn.prepareStatement(deleteSql);
       ok = ps.executeUpdate();
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    
    return ok;
  }
  /**
   * 图书导出
   * @param type
   * @throws Exception
   */
  public ArrayList<YHDbRecord> bookExportlog(YHBookInfo book, String orderflag, Connection conn, YHPerson user)
  throws Exception {
  StringBuffer sb = new StringBuffer();
  Statement stmt = null;
  ResultSet rs = null;
  String ExportSql = null;
  String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
  if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
    ExportSql="select info.seq_id, info.dept,info.book_name,info.book_no,info.type_id,info.author,info.isbn,info.pub_house,info.pub_date,info.area,info.amt,info.price,info.brief,info.[open],info.lend,info.borr_person,info.memo, info.attachment_id,info.attachment_name from oa_literature_info info where 1=1";
  }else {
    ExportSql="select info.seq_id, info.dept,info.book_name,info.book_no,info.type_id,info.author,info.isbn,info.pub_house,info.pub_date,info.area,info.amt,info.price,info.brief,info.open,info.lend,info.borr_person,info.memo, info.attachment_id,info.attachment_name from oa_literature_info info where 1=1";
  }
  if(book.getTypeId() != 0){
    ExportSql += " and info.type_id=" + book.getTypeId();
  }
  if(!YHUtility.isNullorEmpty(book.getLend())){
    ExportSql += " and info.lend =" + book.getLend();
  } 
  if(!YHUtility.isNullorEmpty(book.getBookName().trim())){
    ExportSql += " and info.book_name like '%" + YHDBUtility.escapeLike(book.getBookName().trim()) +"%'" + YHDBUtility.escapeLike();
  }
  if(!YHUtility.isNullorEmpty(book.getBookNo().trim())){
    ExportSql += " and info.book_no='"+book.getBookNo()+"'";
  }
  if(!YHUtility.isNullorEmpty(book.getAuthor().trim())){
    ExportSql += " and info.author like '%" + YHDBUtility.escapeLike(book.getAuthor().trim())+"%'" + YHDBUtility.escapeLike();
  }
  if(!YHUtility.isNullorEmpty(book.getIsbn().trim())){
    ExportSql += " and info.isbn like '%" + YHDBUtility.escapeLike(book.getIsbn().trim())+"%'" + YHDBUtility.escapeLike();
  }
  if(!YHUtility.isNullorEmpty(book.getPubHouse().trim())){
    ExportSql += " and info.pub_house like '%" + YHDBUtility.escapeLike(book.getPubHouse().trim())+"%'" + YHDBUtility.escapeLike();
  }
  if(!YHUtility.isNullorEmpty(book.getArea().trim())){
    ExportSql += " and info.area like '%" + YHDBUtility.escapeLike(book.getArea().trim())+"%'" + YHDBUtility.escapeLike();
  }  
  if(!user.isAdmin()){
	  if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
	    ExportSql += " and ('" + user.getDeptId() +"' in ( info.[OPEN]) or info.[OPEN] = '0')";
	  }else {
	    ExportSql += " and ('" + user.getDeptId() +"' in ( info.OPEN ) or info.OPEN = '0')";
	  }
  }
  ExportSql += " order by " + orderflag +" desc";
  //System.out.println("daochu:::"+ExportSql);
  try{
      stmt = conn.createStatement();
      rs = stmt.executeQuery(ExportSql);
      ArrayList<YHDbRecord>  dbl = new ArrayList<YHDbRecord>();
      int num = 0;
      while (rs.next() && ++num<=300) {
           int seqId = rs.getInt("seq_id");
           int dept = rs.getInt("dept");
           String depts = findDeptNames(conn,String.valueOf(dept));
           String bookName = rs.getString("book_name");
           String bookNo = rs.getString("book_no");
           int typeId = rs.getInt("type_id");
           YHBookType tt = editBookType(conn,user,String.valueOf(typeId));
           String bookTypeName = "";
           if(tt!=null){
             bookTypeName = tt.getTypeName();
           }else{
             bookTypeName = "";
           }
           String author = rs.getString("author");
           String isbn = rs.getString("isbn");
           String pubHouse = rs.getString("pub_house");
           String pubDate = rs.getString("pub_date");
           String area = rs.getString("area");
           int amt = rs.getInt("amt");
           Double price = rs.getDouble("price");
           String brief = rs.getString("brief");
           //String open = rs.getString("open");
           String lend = rs.getString("lend");
           String open = findDeptNames(conn,rs.getString("open"));
           
           String borrPerson = rs.getString("borr_person");
           String memo = rs.getString("memo");
           YHDbRecord dbr = new YHDbRecord();
           dbr.addField("部门",depts);
           dbr.addField("书名", bookName);
           dbr.addField("作者", author);
           dbr.addField("图书编号",bookNo );
           dbr.addField("图书类别", bookTypeName);
           dbr.addField("ISBN号",isbn);
           dbr.addField("出版社", pubHouse);
           dbr.addField("出版日期", pubDate);
           dbr.addField("存放地点",area );
           dbr.addField("数量", amt);
           
           dbr.addField("价格", price);
           dbr.addField("内容简介", brief);
           dbr.addField("借阅状态",lend );
           dbr.addField("借阅范围", open);
           
           dbr.addField("录入人",borrPerson );
           dbr.addField("备注", memo);
           dbl.add(dbr);
     }
         return dbl;
 }catch(Exception ex){
        throw ex;
 }finally {
         YHDBUtility.close(stmt, rs, null);
  }  
}
  /**
   * 图书模块导出
   * @param conn
   * @param user
   * @return
   * @throws Exception
   */
  public ArrayList<YHDbRecord> templetbookExportlog(Connection conn, YHPerson user)
  throws Exception {
  StringBuffer sb = new StringBuffer();
  Statement stmt = null;
  ResultSet rs = null;
  String ExportSql="";

  try{
      ArrayList<YHDbRecord>  dbl = new ArrayList<YHDbRecord>();
      int num = 0;
           YHDbRecord dbr = new YHDbRecord();
           dbr.addField("部门","");
           dbr.addField("书名", "");
           dbr.addField("作者", "");
           dbr.addField("图书编号","" );
           dbr.addField("图书类别", "");
           dbr.addField("ISBN号","");
           dbr.addField("出版社", "");
           dbr.addField("出版日期", "");
           dbr.addField("存放地点","" );
           dbr.addField("数量", "");
           
           dbr.addField("价格", "");
           dbr.addField("内容简介", "");
           dbr.addField("借阅状态","" );
           dbr.addField("借阅范围", "");
           
           dbr.addField("录入人","" );
           dbr.addField("备注", "");
           
           
           dbl.add(dbr);
     
         return dbl;
 }catch(Exception ex){
        throw ex;
 }finally {
         YHDBUtility.close(stmt, rs, null);
  }  
}
  
 /**
  * 图书导入信息
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
  public static List<YHBookInfo> importBookTypeInfo (Connection conn, YHPerson user,HttpServletRequest request) throws Exception{
    PreparedStatement ps = null;
    InputStream is = null;
    int ok = 0;
    int numOne = 0;
    List<YHBookInfo> list = new ArrayList();
    try {
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      is = fileForm.getInputStream();
      ArrayList<YHDbRecord> drl = YHCSVUtil.CVSReader(is, YHConst.CSV_FILE_CODE);
      numOne = drl.size();
      for (int i = 0; i < drl.size(); i++) {
        YHDbRecord rd = new YHDbRecord();
        rd = drl.get(i);
        String depts = (String)rd.getValueByName("部门");
        int dept = -1;
        if(depts!=null){
           dept = findDeptNameId(conn,depts);
        }
        
        String bookName = (String)rd.getValueByName("书名");
        String author = (String)rd.getValueByName("作者");
        String bookNo = String.valueOf(rd.getValueByName("图书编号"));
        
        String bookTypeName = (String)rd.getValueByName("图书类别");
        int bookTypeId = -1;
        if(bookTypeName!=null){
           bookTypeId = findBookTypeId(conn,bookTypeName);
        }
        String isbn = (String)rd.getValueByName("ISBN号版社");
        String pubDate = (String)rd.getValueByName("出版日期");
        String area = (String)rd.getValueByName("存放地点");
        String pubHouse = (String)rd.getValueByName("出版社");
        String amts = (String)rd.getValueByName("数量");
        int amt =0;
        if(YHUtility.isInteger(amts)){
           amt = Integer.parseInt(amts);
        }
        String prices = (String)rd.getValueByName("价格");
        Double price=0.0;
        if(YHUtility.isNumber(prices)){
          price = Double.parseDouble(prices);
        }
        String brief = (String)rd.getValueByName("内容简介");
        String lend = (String)rd.getValueByName("借阅状态");
        String opens = (String)rd.getValueByName("借阅范围");
        int open = -1;
        if(opens!=null){
          open = findDeptNameId(conn,opens);
        }        
        String borrPerson = (String)rd.getValueByName("录入人");
        String memo = (String)rd.getValueByName("备注");
        YHBookInfo bi = new YHBookInfo();
        bi.setDept(dept);
        bi.setBookName(bookName);
        bi.setAuthor(author);
        bi.setBookNo(bookNo);
        bi.setTypeId(bookTypeId);
        bi.setIsbn(isbn);
        bi.setPubDate(pubDate);
        bi.setArea(area);
        bi.setPubHouse(pubHouse);
        bi.setAmt(amt);
        bi.setPrice(price);
        bi.setBrief(brief);
        bi.setLend(lend);
        bi.setOpen(String.valueOf(open));
        bi.setBorrPerson(borrPerson);
        bi.setMemo(memo);
        List<YHBookInfo> ttf =  chaBookTypeId(conn,user,bi.getBookNo());
        if(ttf.size()>0){
          bi.setSaveInfo("保存失败！编号已存在"); 
        }else{
          ok = importBookInfo(conn,user,bi);
          if(ok==1){
            bi.setSaveInfo("保存成功！");
            String sql = " select max(SEQ_ID) SEQ_ID from oa_literature_info ";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
              bi.setSeqId(rs.getInt("SEQ_ID"));
            }
          }else{
            bi.setSaveInfo("保存失败！");
          }
        }
        String idIsDeptName="";
       // idIsDeptName = YHBookQueryLogic.findADeptName(conn,bi.getDept());
        bi.setDeptName(depts);
        list.add(bi);
      }
      
    } catch (Exception e) {
      throw e;
    }finally{
      YHDBUtility.close(ps, null, null);
    }
    return list;
  }
  /**
   * 插入导入信息
   * @param conn
   * @param person
   * @param noHiddenId
   * @return
   * @throws Exception
   */
  public static int importBookInfo(Connection conn, YHPerson person, YHBookInfo bi) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null; 
    int ok =0;
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = "insert into oa_literature_info (dept,book_name,author,book_no,type_id,isbn,pub_house,pub_date,area,amt,price,brief,[open],lend,borr_person,memo)"
        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }else {
      sql = "insert into oa_literature_info (dept,book_name,author,book_no,type_id,isbn,pub_house,pub_date,area,amt,price,brief,open,lend,borr_person,memo)"
        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
    try{
    ps = conn.prepareStatement(sql);
    
    ps.setInt(1, bi.getDept());
    ps.setString(2,bi.getBookName());
    ps.setString(3,bi.getAuthor());
    ps.setString(4,bi.getBookNo());
    ps.setInt(5,bi.getTypeId());
    ps.setString(6,bi.getIsbn());
    ps.setString(7,bi.getPubHouse());
    ps.setString(8,bi.getPubDate());
    ps.setString(9,bi.getArea());
    ps.setInt(10,bi.getAmt());
    ps.setDouble(11,bi.getPrice());
    ps.setString(12,bi.getBrief());
    ps.setString(13,bi.getOpen());
    if("0".equals(bi.getLend()) || "1".equals(bi.getLend())){
      ps.setString(14,bi.getLend());
    }else{
      ps.setString(14,"-1");
    }
    ps.setString(15,bi.getBorrPerson());
    ps.setString(16,bi.getMemo());
    ok = ps.executeUpdate();
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    
    return ok; 
  }
  /** 
   * 返回部门名Id
   * @param dbConn 
   * @param deptIds 
   * @return 
   * @throws Exception 
   */ 
   public static int findDeptNameId(Connection dbConn, String deptName) throws Exception{ 
   PreparedStatement ps = null; 
   ResultSet rs = null; 
   int deptId = -1;
   String sql = "select SEQ_ID, DEPT_NAME from oa_department where DEPT_NAME='"+deptName.trim()+"'"; 
   
   //System.out.println(sql);
   try{ 
     if(!deptName.trim().equalsIgnoreCase("全体部门")){
         ps = dbConn.prepareStatement(sql); 
         rs = ps.executeQuery(); 
         if(rs.next()){ 
           deptId = rs.getInt("SEQ_ID"); 
         } 
     }else{
       deptId =0;
     }
   } catch (Exception e){ 
   throw e; 
   }finally{ 
   YHDBUtility.close(ps, rs, null); 
   } 
   return deptId; 
   }
   
   /** 
    * 通过图书类型查询 Id
    * @param dbConn 
    * @param deptIds 
    * @return 
    * @throws Exception 
    */ 
    public static int findBookTypeId(Connection dbConn, String typeName) throws Exception{ 
    PreparedStatement ps = null; 
    ResultSet rs = null; 
    int seqId = 0;
    String sql = "select seq_id, type_name from oa_literature_type where type_name='"+typeName.trim()+"'"; 
    //System.out.println("sql::::"+sql);
    try{ 
    ps = dbConn.prepareStatement(sql); 
    rs = ps.executeQuery(); 
    while(rs.next()){ 
      seqId = rs.getInt("seq_id"); 
    } 
    } catch (Exception e){ 
    throw e; 
    }finally{ 
    YHDBUtility.close(ps, rs, null); 
    } 
    return seqId; 
    }
    /**
     * 查询图书基本信息（用于判断导入图书标号是否重复，和图书名称是否为空）
     * @param conn
     * @param person
     * @param seqId
     * @return
     * @throws Exception
     */
    public static  List<YHBookInfo> chaBookTypeId(Connection conn, YHPerson person, String bookNo) throws Exception {
      PreparedStatement ps = null;
      ResultSet rs = null;   
      String sql = null;
      String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        sql = "select seq_id, dept,book_name,book_no,type_id,author,isbn,pub_house,pub_date,area,amt,price,brief,[open],lend,borr_person,memo, attachment_id,attachment_name from oa_literature_info where book_no='"+bookNo+"'";
      }else {
        sql = "select seq_id, dept,book_name,book_no,type_id,author,isbn,pub_house,pub_date,area,amt,price,brief,open,lend,borr_person,memo, attachment_id,attachment_name from oa_literature_info where book_no='"+bookNo+"'";
      }
      List<YHBookInfo> book = new ArrayList<YHBookInfo>();
      try{
         ps = conn.prepareStatement(sql);
         rs = ps.executeQuery();
         while(rs.next()){
           YHBookInfo ttf = new YHBookInfo();
           ttf.setSeqId(rs.getInt("seq_id"));
           ttf.setDept(rs.getInt("dept"));
           ttf.setBookName(rs.getString("book_name"));
           ttf.setBookNo(rs.getString("book_no"));
           ttf.setTypeId(rs.getInt("type_id"));
           ttf.setAuthor(rs.getString("author"));
           ttf.setIsbn(rs.getString("isbn"));
           ttf.setPubHouse(rs.getString("pub_house"));
           ttf.setPubDate(rs.getString("pub_date"));
           ttf.setArea(rs.getString("area"));
           ttf.setAmt(rs.getInt("amt"));
           ttf.setPrice(rs.getDouble("price"));
           ttf.setBrief("brief");
           ttf.setOpen(rs.getString("open"));
           System.out.println(rs.getString("open"));
           ttf.setOpenNames(findDeptNames(conn,rs.getString("open")));
           ttf.setLend(rs.getString("lend"));
           ttf.setBorrPerson(rs.getString("borr_person"));
           ttf.setMemo(rs.getString("memo"));
           ttf.setAttachmentId(rs.getString("attachment_id"));
           ttf.setAttachmentName(rs.getString("attachment_name"));
           book.add(ttf);
         }
         //System.out.println(sql);
      }catch(Exception ex){
        throw ex;
      }finally{
        YHDBUtility.close(ps, rs, null);
      }
      
      return book;
    }
    /**
     * 删除图书所选图书信息
     * @param dbConn
     * @param loginperson
     * @param deleteStr
     * @return
     * @throws IOException
     * @throws Exception
     */
    public int deleteSelectBook(Connection dbConn,String deleteStr) throws IOException, Exception {
      String deletenewsCommentSql = "";
      String deletenewsSql = "";
      Statement stmt = null;
      ResultSet rs = null;
        
      if(!YHUtility.isNullorEmpty(deleteStr)){
          String[] deleteStrs = deleteStr.split(",");
          deleteStr = "";
          for(int i = 0 ;i < deleteStrs.length ; i++){
             deleteStr +=   deleteStrs[i]  + ",";
          }
             deleteStr = deleteStr.substring(0, deleteStr.length() - 1);
      }
      int success = 0;
      try {
          stmt = dbConn.createStatement();
          deletenewsCommentSql = "delete  from oa_literature_info  where seq_id in  ("+ deleteStr + ")";
          success = stmt.executeUpdate(deletenewsCommentSql);
      } catch (Exception e) {
          e.printStackTrace();
      }finally {
           YHDBUtility.close(stmt, rs, null);
      }
       return success;
    }
    
 }
  


