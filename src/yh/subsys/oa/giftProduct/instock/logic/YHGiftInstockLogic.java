package yh.subsys.oa.giftProduct.instock.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import yh.core.codeclass.data.YHCodeClass;
import yh.core.codeclass.data.YHCodeItem;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.calendar.logic.YHCalendarLogic;
import yh.subsys.oa.giftProduct.instock.data.YHGiftInstock;
import yh.core.load.YHPageLoader;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHGiftInstockLogic {
  private static Logger log = Logger.getLogger(YHGiftInstockLogic.class);
  public int addGiftInstock(Connection dbConn,YHGiftInstock giftInstock) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, giftInstock);
    return YHCalendarLogic.getMaSeqId(dbConn, "GIFT_INSTOCK");
  }
  public void updateGiftInstock(Connection dbConn,YHGiftInstock giftInstock) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, giftInstock);
  }
  public YHGiftInstock selectGiftInstockById(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    YHGiftInstock giftInstock =  (YHGiftInstock) orm.loadObjSingle(dbConn, YHGiftInstock.class, seqId);
    return giftInstock;
  }
  public List<YHGiftInstock> selectGiftInstock(Connection dbConn,String[] str ) throws Exception {
    YHORM orm = new YHORM();
    List<YHGiftInstock> giftList = new ArrayList<YHGiftInstock>();
    giftList =  orm.loadListSingle(dbConn, YHGiftInstock.class, str);
    return giftList;
  }
  public List<YHGiftInstock> selectGiftInstock(Connection dbConn,String giftType ) throws Exception {
    YHORM orm = new YHORM();
    List<YHGiftInstock> giftList = new ArrayList<YHGiftInstock>();
    Statement stmt = null;
    ResultSet rs = null; 
    //sql = "select gi.SEQ_ID, gi.GIFT_NAME,gi.GIFT_QTY,go.use_gift_qty as USE_GIFT_QTY from gift_instock gi left outer join 
    //"(select sum(trans_qty- trans_flag) as use_gift_qty,gift_id as gift_id  FROM gift_outstock   group by gift_id) go on gi.seq_id = go.gift_id
    try {
      String queryStr = "select SEQ_ID, GIFT_NAME,GIFT_QTY from oa_present_instock"  ;
      if(!giftType.equals("")){
        queryStr = queryStr + " where GIFT_TYPE = '" + giftType + "' order by GIFT_NAME";
      }
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while(rs.next()){
        YHGiftInstock instock = new YHGiftInstock();
        instock.setSeqId(rs.getInt(1));
        instock.setGiftName(rs.getString(2));
        //System.out.println(rs.getString(2));
        instock.setGiftQty(rs.getInt(3));
        giftList.add(instock);
      }
    }catch(Exception ex) {
      throw ex;
   }finally {
     YHDBUtility.close(stmt, rs, log);
   }
    return giftList;
  }
  public int selectGiftQty(Connection dbConn,int  seqId ) throws Exception {
    YHORM orm = new YHORM();
    List<YHGiftInstock> giftList = new ArrayList<YHGiftInstock>();
    Statement stmt = null;
    ResultSet rs = null; 
    int giftQty = 0;
    try {
      String queryStr = "select sum(trans_qty- trans_flag) as gift_qty FROM oa_present_outstock  WHERE gift_id = " + seqId  ;
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      if(rs.next()){
        giftQty = rs.getInt("gift_qty");
      }
    }catch(Exception ex) {
      throw ex;
   }finally {
     YHDBUtility.close(stmt, rs, log);
   }
    return giftQty;
  }
  public void delGiftInstockById(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHGiftInstock.class, seqId);
  }
  public void delGiftInstock(Connection dbConn,String seqIds) throws Exception {
    Statement stmt = null;
    ResultSet rs = null; 
    if(seqIds.endsWith(",")){
      seqIds = seqIds.substring(0, seqIds.length()-1);
    }
    try {
      String sql = "delete from oa_present_instock where SEQ_ID in(" + seqIds +")";
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
   }finally {
     YHDBUtility.close(stmt, rs, log);
   }
  }
  public YHCodeClass getCodeClass(Connection dbConn,int seqId) throws Exception {
    YHCodeClass codeClass = null;
    Statement stmt = null;
    ResultSet rs = null; 
    
    try {
      String queryStr = "select SEQ_ID, CLASS_NO, SORT_NO, CLASS_DESC, CLASS_LEVEL from oa_kind_dict where SEQ_ID= " + seqId;

      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
     
      if(rs.next()){
        codeClass = new YHCodeClass();
        codeClass.setSqlId(rs.getInt(1));
        codeClass.setClassNo(rs.getString(2));
        codeClass.setSortNo(rs.getString(3));
        codeClass.setClassDesc(rs.getString(4));
        codeClass.setClassLevel(rs.getString(5));
      }
    }catch(Exception ex) {
      throw ex;
   }finally {
     YHDBUtility.close(stmt, rs, log);
   }
    return codeClass;
  }
  public YHCodeItem getCodeItemById(Connection dbConn,String seqId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null; 
    YHORM orm = new YHORM();
    String queryStr = "select SEQ_ID, CLASS_NO, SORT_NO, CLASS_DESC, CLASS_CODE from oa_kind_dict_item where SEQ_ID ="+seqId;

    YHCodeItem codeItem = null;
    try{
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while(rs.next()){
        codeItem = new YHCodeItem();
        codeItem.setSqlId(rs.getInt("SEQ_ID"));
        codeItem.setClassNo(rs.getString("CLASS_NO"));
        codeItem.setSortNo(rs.getString("SORT_NO"));
        codeItem.setClassDesc(rs.getString("CLASS_DESC"));
        codeItem.setClassCode(rs.getString("CLASS_CODE"));
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
   
    return   codeItem;
  }

public void updateCodeItemById(Connection dbConn,String seqId,String classCode,String sortNo,String classDesc) throws Exception {
  PreparedStatement pstmt = null;
  //Statement pstmt = null;
  ResultSet rs = null; 
  YHORM orm = new YHORM();
/*  classCode = classCode.replace("'", "''");
  sortNo = sortNo.replace("'", "''");
  classDesc = classDesc.replace("'", "''");*/
  //String sql = "update oa_kind_dict_item set CLASS_CODE='"+classCode+"',SORT_NO='"+sortNo+"',CLASS_DESC='"+classDesc+"' where SEQ_ID =" + seqId;
  String sql = "update oa_kind_dict_item set CLASS_CODE=?,SORT_NO=?,CLASS_DESC=? where SEQ_ID =?" ;


  YHCodeItem codeItem = null;
  try{
    //pstmt = dbConn.createStatement();
    pstmt = dbConn.prepareStatement(sql);
   // rs = stmt.executeQuery(queryStr);
    pstmt.setString(1, classCode);
    pstmt.setString(2, sortNo);
    pstmt.setString(3, classDesc);
    pstmt.setString(4, seqId);
   pstmt.executeUpdate();
  }catch(Exception ex) {
    throw ex;
  }finally {
    YHDBUtility.close(pstmt, rs, log);
  }
}
  public List<YHCodeItem> getCodeItem(Connection dbConn,String classNo) throws Exception {
    Statement stmt = null;
    ResultSet rs = null; 
    YHORM orm = new YHORM();
    List<YHCodeItem>   codeList = new ArrayList<YHCodeItem>();
    String queryStr = "select SEQ_ID, CLASS_NO, SORT_NO, CLASS_DESC, CLASS_CODE from oa_kind_dict_item where CLASS_NO = (select CLASS_NO from oa_kind_dict where CLASS_NO = '"+classNo+"') order by SORT_NO";

    YHCodeItem codeItem = null;
    try{
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while(rs.next()){
        codeItem = new YHCodeItem();
        codeItem.setSqlId(rs.getInt("SEQ_ID"));
        codeItem.setClassNo(rs.getString("CLASS_NO"));
        codeItem.setSortNo(rs.getString("SORT_NO"));
        codeItem.setClassDesc(rs.getString("CLASS_DESC"));
        codeItem.setClassCode(rs.getString("CLASS_CODE"));
        codeList.add(codeItem);
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
   
    return   codeList;
  }
  public boolean checkCodeClass(Connection dbConn,String calssDesc) throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rs = null; 
    YHORM orm = new YHORM();
    calssDesc = calssDesc.replace("'", "''");
    List<YHCodeItem>   codeList = new ArrayList<YHCodeItem>();
    String queryStr = "select * from oa_kind_dict_item where CLASS_DESC = '" + calssDesc + "'";

    YHCodeItem codeItem = null;
    try{
      pstmt = dbConn.prepareStatement(queryStr);
      rs = pstmt.executeQuery(queryStr);
      while(rs.next()){
        return false;
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, rs, log);
    }
   
    return   true;
  }
  public int addCodeItem(Connection dbConn,String classCode,String classNo,String sortNo,String classDesc) throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rs = null; 
    YHORM orm = new YHORM();
    List<YHCodeItem>   codeList = new ArrayList<YHCodeItem>();
   YHCodeItem codeItem = null;
    try{
      String queryStr = "insert into oa_kind_dict_item(CLASS_NO, CLASS_CODE, SORT_NO, CLASS_DESC) values(?, ?, ?, ?)";
      pstmt = dbConn.prepareStatement(queryStr);
      pstmt.setString(1, classNo);
      pstmt.setString(2, classCode);
      pstmt.setString(3, sortNo);
      pstmt.setString(4, classDesc);
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, rs, log);
    }
    return YHCalendarLogic.getMaSeqId(dbConn, "OA_KIND_DICT_ITEM");
  }
  /**
   * 分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String toSearchData(Connection conn,Map request,String giftName,String giftDesc,String giftCode,String giftType) throws Exception{
    giftName = YHDBUtility.escapeLike(giftName);
    giftDesc =YHDBUtility.escapeLike(giftDesc);
    giftCode = YHDBUtility.escapeLike(giftCode);
    String sql = "select gpd.SEQ_ID as SEQ_ID, gpd.GIFT_NAME as GIFT_NAME , c.class_desc as GIFT_TYPE, gpd.GIFT_PRICE as GIFT_PRICE,"
            +"gpd.GIFT_SUPPLIER as GIFT_SUPPLIER,gpd.GIFT_QTY as GIFT_QTY, p2.user_name as GIFT_KEEPER, gpd.DEPT_ID as DEPT_ID,gpd.DEPT_ID_DESC as DEPT_ID_DESC,"
            +"gpd.GIFT_CREATOR as GIFT_CREATOR, gpd.CREATE_DATE as CREATE_DATE,gpd.GIFT_MEMO as GIFT_MEMO from(select g.SEQ_ID as SEQ_ID,g.GIFT_NAME as GIFT_NAME ,g.GIFT_TYPE as GIFT_TYPE,g.GIFT_PRICE as GIFT_PRICE,g.GIFT_SUPPLIER as GIFT_SUPPLIER,"
            +"g.GIFT_QTY as GIFT_QTY, g.gift_keeper as GIFT_KEEPER,g.DEPT_ID as DEPT_ID,d.DEPT_NAME as DEPT_ID_DESC,p.USER_NAME as GIFT_CREATOR,g.CREATE_DATE as CREATE_DATE,"
            +"g.GIFT_MEMO as GIFT_MEMO from oa_present_instock g left outer join  PERSON p  on g.GIFT_CREATOR =p.SEQ_ID left outer join oa_department d " 
            +"on g.DEPT_ID =d.SEQ_ID  where 1=1";
    if(!giftName.equals("")){
      sql = sql + " and g.GIFT_NAME like '%"+giftName+"%' "+ YHDBUtility.escapeLike();
    }
    if(!giftType.equals("")){
      sql = sql + " and g.GIFT_TYPE = '"+giftType+"'";
    }
    if(!giftDesc.equals("")){
      sql = sql + " and g.GIFT_DESC like '%"+giftDesc+"%' "+ YHDBUtility.escapeLike();
    }
    if(!giftCode.equals("")){
      sql = sql + " and g.GIFT_CODE like '%"+giftCode+"%' "+ YHDBUtility.escapeLike();
    }
    sql = sql + ") gpd  left outer join oa_kind_dict_item c  on c.seq_id=gpd.GIFT_TYPE left OUTER join person p2 ON gpd.GIFT_KEEPER=p2.seq_id  order by gpd.GIFT_NAME";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      /*String sql = "select gpd.SEQ_ID as SEQ_ID, gpd.GIFT_NAME as GIFT_NAME , c.class_desc as GIFT_TYPE, gpd.GIFT_PRICE as GIFT_PRICE,"
            +"gpd.GIFT_SUPPLIER as GIFT_SUPPLIER,gpd.GIFT_QTY as GIFT_QTY, p2.user_name as GIFT_KEEPER, gpd.DEPT_ID as DEPT_ID,"
            +"gpd.GIFT_CREATOR as GIFT_CREATOR, gpd.CREATE_DATE as CREATE_DATE,gpd.GIFT_MEMO as GIFT_MEMO from oa_kind_dict_item c right outer join (select g.SEQ_ID as SEQ_ID,g.GIFT_NAME as GIFT_NAME ,g.GIFT_TYPE as GIFT_TYPE,g.GIFT_PRICE as GIFT_PRICE,g.GIFT_SUPPLIER as GIFT_SUPPLIER,"
            +"g.GIFT_QTY as GIFT_QTY, g.gift_keeper as GIFT_KEEPER,d.DEPT_NAME as DEPT_ID,p.USER_NAME as GIFT_CREATOR,g.CREATE_DATE as CREATE_DATE,"
            +"g.GIFT_MEMO as GIFT_MEMO from PERSON p right outer join  GIFT_INSTOCK g on g.GIFT_CREATOR =p.SEQ_ID left outer join oa_department d " 
            +"on g.DEPT_ID =d.SEQ_ID g.DEPT_ID =d.SEQ_ID  where g.GIFT_TYPE = '"+giftType+"' and g.GIFT_NAME like '%"+giftName+"%' and g.GIFT_CODE='"+giftCode+"' and g.gift_desc like '%"+giftDesc+"%'  ) gpd on c.seq_id=gpd.GIFT_TYPE left OUTER join person p2 ON gpd.GIFT_KEEPER=p2.seq_id  order by gpd.GIFT_NAME";
 */     String dateStr = dateFormat.format(new Date());
      //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    
    return pageDataList.toJson();
  }
  public void updateInstockById(Connection dbConn,String seqId,int qty) throws Exception {
    Statement stmt = null;
    ResultSet rs = null; 
    YHORM orm = new YHORM();
    String sql = "update  oa_present_instock set GIFT_QTY = GIFT_QTY-"+qty+"  where SEQ_ID ="+seqId;
    YHCodeItem codeItem = null;
    try{
      stmt = dbConn.createStatement();
     // rs = stmt.executeQuery(queryStr);
     stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  public void updateInstockByIdBack(Connection dbConn,int seqId,int qty) throws Exception {
    Statement stmt = null;
    ResultSet rs = null; 
    YHORM orm = new YHORM();
    String sql = "update  oa_present_instock set GIFT_QTY = GIFT_QTY+"+qty+"  where SEQ_ID ="+seqId;
    YHCodeItem codeItem = null;
    try{
      stmt = dbConn.createStatement();
     // rs = stmt.executeQuery(queryStr);
     stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  public List<YHGiftInstock> selectGiftInstockByName(Connection dbConn,String giftName ) throws Exception {
    YHORM orm = new YHORM();
    List<YHGiftInstock> giftList = new ArrayList<YHGiftInstock>();
    Statement stmt = null;
    ResultSet rs = null; 
    giftName = YHDBUtility.escapeLike(giftName);
    try {
      String queryStr = "select SEQ_ID, GIFT_NAME,GIFT_QTY from oa_present_instock"  ;
      if(!giftName.equals("")){
        queryStr = queryStr + " where GIFT_NAME like '%" + giftName + "%' " + YHDBUtility.escapeLike() + " order by GIFT_NAME";
      }
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while(rs.next()){
        YHGiftInstock instock = new YHGiftInstock();
        instock.setSeqId(rs.getInt(1));
        instock.setGiftName(rs.getString(2));
        //System.out.println(rs.getString(2));
        instock.setGiftQty(rs.getInt(3));
        giftList.add(instock);
      }
    }catch(Exception ex) {
      throw ex;
   }finally {
     YHDBUtility.close(stmt, rs, log);
   }
    return giftList;
  }
}
