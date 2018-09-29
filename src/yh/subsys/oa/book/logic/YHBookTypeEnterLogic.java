package yh.subsys.oa.book.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.book.data.YHBookInfo;

public class YHBookTypeEnterLogic { 
  private static Logger log = Logger.getLogger(YHBookTypeEnterLogic.class);
  public static int addBookTypeEnter(Connection conn, YHPerson person,YHBookInfo bi) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    int ok =0;
    String addSql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      addSql = "insert into oa_literature_info(dept,book_name,book_no,type_id,author,isbn,pub_house,pub_date,area,amt,price,brief,[open],lend,borr_person,memo, attachment_id,attachment_name) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }else {
      addSql = "insert into oa_literature_info(dept,book_name,book_no,type_id,author,isbn,pub_house,pub_date,area,amt,price,brief,open,lend,borr_person,memo, attachment_id,attachment_name) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
    try{
      ps = conn.prepareStatement(addSql);
       ps.setInt(1, bi.getDept());
       ps.setString(2, bi.getBookName());
       ps.setString(3, bi.getBookNo());
       ps.setInt(4, bi.getTypeId());
       ps.setString(5, bi.getAuthor());
       ps.setString(6, bi.getIsbn());
       ps.setString(7, bi.getPubHouse());
       ps.setString(8, bi.getPubDate());
       ps.setString(9, bi.getArea());
       ps.setInt(10, bi.getAmt());
       ps.setDouble(11, bi.getPrice());
       ps.setString(12, bi.getBrief());
       ps.setString(13, bi.getOpen());
       ps.setString(14, bi.getLend());
       ps.setString(15, bi.getBorrPerson());
       ps.setString(16, bi.getMemo());
       ps.setString(17, bi.getAttachmentId());
       ps.setString(18, bi.getAttachmentName());
       ok = ps.executeUpdate();
      // System.out.println(addSql);
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
     return ok;
   }
  /**
   * 查看图书名称是否重复
   */
  public static List<YHBookInfo> isBookNameRepeat(Connection conn, YHPerson person,String bookName) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    int ok =0;
    String selSql = null;
    List<YHBookInfo> info = new ArrayList<YHBookInfo>();
    selSql = "select book_name from oa_literature_info where book_name='"+YHUtility.encodeLike(bookName)+"'";
    try{
      ps = conn.prepareStatement(selSql);
      rs = ps.executeQuery();
      YHBookInfo binfo = new YHBookInfo();
      if(rs.next()){
        binfo.setBookName(rs.getString("book_name"));
        info.add(binfo);
        return info;
      }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
     return info;
   }
  /**
   * 查看图书编号是否重复
   */
  public static List<YHBookInfo> isBookNoRepeat(Connection conn, YHPerson person,String bookNo) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    int ok =0;
    String selSql = null;
    List<YHBookInfo> info = new ArrayList<YHBookInfo>();
    selSql = "select book_no from oa_literature_info where book_no='"+YHUtility.encodeLike(bookNo)+"'";
    try{
      ps = conn.prepareStatement(selSql);
      rs = ps.executeQuery();
      YHBookInfo binfo = new YHBookInfo();
      if(rs.next()){
        binfo.setBookName(rs.getString("book_no"));
        info.add(binfo);
        return info;
      }
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
     return info;
   }
  
  
  /**
   * 编辑图书信息
   * @param conn
   * @param person
   * @param bi
   * @return
   * @throws Exception
   */
  public static int editBookTypeInfo(Connection conn, YHPerson person,YHBookInfo bi) throws Exception {
    PreparedStatement ps = null;
    ResultSet rs = null;
    int ok =0;
    //dept,book_name,book_no,type_id,author,isbn,pub_house,pub_date,area,amt,
    //price,brief,open,lend,borr_person,memo, attachment_id,attachment_name
    String addSql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      addSql = "update oa_literature_info set dept =?,book_name=?,book_no=?,type_id=?,author=?,isbn=?,pub_house=?,pub_date=?,area=?,amt=?" +
      ",price=?,brief=?,[open]=?,lend=?,borr_person=?,memo=?,attachment_id=?,attachment_name=? where seq_id =?";
    }else {
      addSql = "update oa_literature_info set dept =?,book_name=?,book_no=?,type_id=?,author=?,isbn=?,pub_house=?,pub_date=?,area=?,amt=?" +
      ",price=?,brief=?,open=?,lend=?,borr_person=?,memo=?,attachment_id=?,attachment_name=? where seq_id =?";
    }
    //System.out.println(addSql);
    try{
      ps = conn.prepareStatement(addSql);
       ps.setInt(1, bi.getDept());
       ps.setString(2, bi.getBookName());
       ps.setString(3, bi.getBookNo());
       ps.setInt(4, bi.getTypeId());
       ps.setString(5, bi.getAuthor());
       ps.setString(6, bi.getIsbn());
       ps.setString(7, bi.getPubHouse());
       ps.setString(8, bi.getPubDate());
       ps.setString(9, bi.getArea());
       ps.setInt(10, bi.getAmt());
       ps.setDouble(11, bi.getPrice());
       ps.setString(12, bi.getBrief());
       ps.setString(13, bi.getOpen());
       ps.setString(14, bi.getLend());
       ps.setString(15, bi.getBorrPerson());
       ps.setString(16, bi.getMemo());
       ps.setString(17, bi.getAttachmentId());
       ps.setString(18, bi.getAttachmentName());
       ps.setInt(19, bi.getSeqId());
       ok = ps.executeUpdate();
       //System.out.println(addSql);
       //System.out.println(ok);
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
     return ok;
   }
  /**
   * 处理上传附件，返回附件id，附件名称
  //点击单文件上传时调用的方法

   * @param request  HttpServletRequest
   * @param 
   * @return Map<String, String> ==> {id = 文件名}
   * @throws Exception 
   */
  public Map<String, String> fileUploadLogic(YHFileUploadForm fileForm,
      String pathPx) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    String filePath = pathPx;
    try {
      Calendar cld = Calendar.getInstance();
      int year = cld.get(Calendar.YEAR) % 100;
      int month = cld.get(Calendar.MONTH) + 1;
      String mon = month >= 10 ? month + "" : "0" + month;
      String hard = year + mon;
      Iterator<String> iKeys = fileForm.iterateFileFields();
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        String fileNameV = fileName;
        //YHOut.println(fileName+"*************"+fileNameV);
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        String rand = YHDiaryUtil.getRondom();
        fileName = rand + "_" + fileName;
        
        while (YHDiaryUtil.getExist(filePath + File.separator + hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + fileName;
        }
        result.put(hard + "_" + rand, fileNameV);
        fileForm.saveFile(fieldName, filePath + File.separator + "book" + File.separator + hard + File.separator + fileName);
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  
  
  public static String findBookType(Connection conn,YHPerson person) throws Exception{
    StringBuffer sb = new StringBuffer();
    PreparedStatement ps = null;
    ResultSet rs = null;   
    String findSql="select book_no,book_name from oa_literature_info";
    List<YHBookInfo> bi = new ArrayList<YHBookInfo>();
    try{
      sb.append("{");
      sb.append("listData:[");
      ps = conn.prepareStatement(findSql);
      rs = ps.executeQuery();
      int rowCnt = 0;
      while(rs.next()){
        YHBookInfo bt = new YHBookInfo();
        bt.setBookNo(rs.getString("book_no"));
        bt.setBookName(rs.getString("book_name"));
        bi.add(bt);
        if(++rowCnt>50){
          break;
        }
      }
      for(int i = 0; i<bi.size(); i++){
        sb.append("{");
        sb.append("bookNo:" + YHUtility.encodeSpecial(bi.get(i).getBookNo()));
        sb.append(",bookName:\""+YHUtility.encodeSpecial(bi.get(i).getBookName()) +"\"");
        sb.append("},");
      }
      if(bi.size()>0)
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        sb.append("}");
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    
    return sb.toString();
  }
  public static String blurFindBookType(Connection conn,YHPerson person, String bookNo) throws Exception{
    StringBuffer sb = new StringBuffer();
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String blurSql = "select seq_id, book_no, book_name from oa_literature_info where book_no like "+
    " '%" +YHDBUtility.escapeLike(bookNo)+ "%'" + YHDBUtility.escapeLike() +" or book_name like "+
    " '%" +YHDBUtility.escapeLike(bookNo) + "%'" +YHDBUtility.escapeLike();
    List<YHBookInfo> bi = new ArrayList<YHBookInfo>();
    try{
      sb.append("{");
      sb.append("listData:[");
      ps = conn.prepareStatement(blurSql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHBookInfo bt = new YHBookInfo();
        bt.setSeqId(rs.getInt("seq_id"));
        bt.setBookNo(rs.getString("book_no"));
        bt.setBookName(rs.getString("book_name"));
        bi.add(bt);
      }
     for(int i=0; i<bi.size(); i++){
       sb.append("{");
       sb.append("seqId:" + bi.get(i).getSeqId());
       sb.append(",bookNo:\"" +YHUtility.encodeSpecial(bi.get(i).getBookNo())+ "\"");
       sb.append(",bookName:\""+YHUtility.encodeSpecial(bi.get(i).getBookName()) +"\"");
       sb.append("},");
     }
     if(bi.size()>0)
       sb.deleteCharAt(sb.length() - 1);
       sb.append("]");
       sb.append("}");
    }catch(Exception ex){
      throw ex;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return sb.toString();
  }
}
