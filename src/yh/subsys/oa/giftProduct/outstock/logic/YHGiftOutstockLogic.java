package yh.subsys.oa.giftProduct.outstock.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.calendar.logic.YHCalendarLogic;
import yh.core.load.YHPageLoader;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.giftProduct.outstock.data.YHGiftOutstock;

public class YHGiftOutstockLogic {
  private static Logger log = Logger.getLogger(YHGiftOutstockLogic.class);
  public int addGiftOutstock(Connection dbConn,YHGiftOutstock giftOutstock) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, giftOutstock);
    return YHCalendarLogic.getMaSeqId(dbConn, "GIFT_OUTSTOCK");
  }
  public void updateGiftOutstock(Connection dbConn,YHGiftOutstock giftOutstock) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, giftOutstock);
  }
  public void delOutstockById(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHGiftOutstock.class, seqId);
  }
  public void delOutstock(Connection dbConn,String giftId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String giftIdArray[] = giftId.split(",");
    String newGiftId = "";
    for (int i = 0; i < giftIdArray.length; i++) {
      newGiftId = newGiftId + "'" + giftIdArray[i] + "',";
    }
    if(giftIdArray.length>0){
      newGiftId = newGiftId.substring(0, newGiftId.length()-1);
    }
    String sql = "delete from oa_present_outstock where GIFT_ID in (" + newGiftId + ")";
   try{
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }

  }
  public YHGiftOutstock selectOutstockById(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    YHGiftOutstock outLogic = (YHGiftOutstock) orm.loadObjSingle(dbConn, YHGiftOutstock.class, seqId);
    return outLogic;
  }
  public  Map<String ,String> selectGiftOutstockById(Connection dbConn,int seqId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null; 
    Map<String ,String> map = null;
    String sql = "select go.SEQ_ID as SEQ_ID,go.TRANS_USER as TRANS_USER,p.USER_NAME as TRANS_NAME , go.TRANS_QTY as TRANS_QTY,go.TRANS_FLAG " 
        +"as TRANS_FLAG,ci.CLASS_DESC as GIFT_TYPE ,gi.GIFT_NAME as GIFT_NAME,go.trans_uses as TRANS_USES,go.TRANS_MEMO as TRANS_MEMO " 
        +"from oa_present_outstock go left outer join oa_present_instock gi on go.gift_id = gi.seq_id "  
        +"left outer join person p on go.TRANS_USER = p.seq_id left join oa_kind_dict_item ci on gi.GIFT_TYPE=ci.seq_id where go.SEQ_ID="+seqId;
    try{
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
        map = new HashMap();
        map.put("seqId", rs.getString("SEQ_ID"));
        map.put("transUser", rs.getString("TRANS_USER"));
        map.put("giftType", rs.getString("GIFT_TYPE"));
        map.put("transUses", rs.getString("TRANS_USES"));
        map.put("transQty", rs.getString("TRANS_QTY"));
        map.put("transFlag", rs.getString("TRANS_FLAG"));
        map.put("transMemo", rs.getString("TRANS_MEMO"));      
        map.put("operator", rs.getString("TRANS_USER"));
        map.put("giftName", rs.getString("GIFT_NAME"));
        map.put("transUserName", rs.getString("TRANS_NAME"));
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return map;
  }
  public List<Map<String ,String>> getGiftOutstockToday(Connection dbConn,String today,int userId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null; 
    YHORM orm = new YHORM();
    String begin = YHDBUtility.getDateFilter("go.TRANS_DATE", today, ">=");
    String end = YHDBUtility.getDateFilter("go.TRANS_DATE", today + " 23:59:59", "<=");
    List<Map<String ,String>>   outList = new ArrayList<Map<String ,String>>();
    String queryStr = "select goip.SEQ_ID as SEQ_ID,goip.GIFT_ID as GIFT_ID,goip.gift_name as GIFT_NAME,p2.USER_NAME as TRANS_USER ,goip.GIFT_QTY as GIFT_QTY, "
      +"goip.TRANS_QTY as TRANS_QTY,goip.trans_flag as TRANS_FLAG,go2.use_gift_qty as USE_GIFT_QTY, goip.USE_TRANS as USE_TRANS,"
      +"goip.TRANS_USES as TRANS_USES, goip.gift_price as GIFT_PRICE,goip.TRANS_DATE as TRANS_DATE,goip.OPERATOR as OPERATOR,goip.RUN_ID as RUN_ID from"
      +"(select go.SEQ_ID as SEQ_ID,gi.gift_name as GIFT_NAME,go.TRANS_USER as TRANS_USER,gi.GIFT_QTY as GIFT_QTY, "
      +"go.TRANS_QTY as TRANS_QTY,go.trans_flag as TRANS_FLAG,go.gift_id as GIFT_ID, go.TRANS_QTY-go.trans_flag as USE_TRANS,"
      +"go.TRANS_USES as TRANS_USES, gi.gift_price as GIFT_PRICE,go.TRANS_DATE as TRANS_DATE,p.USER_NAME as OPERATOR ,go.RUN_ID as RUN_ID from "
      +"oa_present_outstock go left outer join oa_present_instock gi on go.gift_id=gi.seq_Id left outer join person p on go.operator=p.SEQ_ID where "
      + begin + " and " + end + " and go.OPERATOR='" + userId + "') goip left outer join person p2 on goip.TRANS_USER = p2.SEQ_ID left outer join "
      +"(select sum(trans_qty- trans_flag) as use_gift_qty,gift_id as gift_id  FROM oa_present_outstock   group by gift_id) go2 on goip.GIFT_ID = go2.gift_id";
    //System.out.println(queryStr);
    try{
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while(rs.next()){
        Map<String ,String> map = new HashMap();
        map.put("seqId", rs.getString("SEQ_ID"));
        map.put("giftName", rs.getString("GIFT_NAME"));
        map.put("transUser", rs.getString("TRANS_USER"));
        map.put("giftQty", rs.getString("GIFT_QTY"));
        map.put("transQty", rs.getString("TRANS_QTY"));
        map.put("transFlag", rs.getString("TRANS_FLAG"));
        map.put("useTrans", rs.getString("USE_TRANS"));      
        map.put("transUses", rs.getString("TRANS_USES"));
        map.put("useGiftQty", rs.getString("USE_GIFT_QTY"));
        map.put("giftPrice", rs.getString("GIFT_PRICE"));
        map.put("transDate", rs.getString("TRANS_DATE"));
        map.put("operator", rs.getString("OPERATOR"));
        map.put("runId", rs.getString("RUN_ID"));
        outList.add(map);
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
   
    return  outList;
  }
  public List<Map<String ,String>> getGiftOutstockByUserId(Connection dbConn,String userId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null; 
    YHORM orm = new YHORM();
    List<Map<String ,String>>   outList = new ArrayList<Map<String ,String>>();
    String queryStr = "select goip.SEQ_ID as SEQ_ID,goip.gift_name as GIFT_NAME,p2.USER_NAME as TRANS_USER ,goip.GIFT_QTY as GIFT_QTY, "
      +"goip.TRANS_QTY as TRANS_QTY,goip.trans_flag as TRANS_FLAG, goip.USE_TRANS as USE_TRANS,"
      +"goip.TRANS_USES as TRANS_USES, goip.gift_price as GIFT_PRICE,goip.TRANS_DATE as TRANS_DATE,goip.OPERATOR as OPERATOR,goip.TRANS_MEMO as TRANS_MEMO,goip.RUN_ID as RUN_ID from"
      +"(select go.SEQ_ID as SEQ_ID,gi.gift_name as GIFT_NAME,go.TRANS_USER as TRANS_USER,gi.GIFT_QTY as GIFT_QTY, "
      +"go.TRANS_QTY as TRANS_QTY,go.trans_flag as TRANS_FLAG, go.TRANS_QTY-go.trans_flag as USE_TRANS,"
      +"go.TRANS_USES as TRANS_USES, gi.gift_price as GIFT_PRICE,go.TRANS_DATE as TRANS_DATE,p.USER_NAME as OPERATOR ,go.TRANS_MEMO as TRANS_MEMO,go.RUN_ID as RUN_ID from "
      +"GIFT_OUTSTOCK go left outer join oa_present_instock gi on go.gift_id=gi.seq_Id left outer join person p on go.operator=p.SEQ_ID where go.TRANS_USER='" + userId + "') goip left outer join person p2 on goip.TRANS_USER = p2.SEQ_ID";
    //System.out.println(queryStr);
    try{
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while(rs.next()){
        Map<String ,String> map = new HashMap();
        map.put("seqId", rs.getString("SEQ_ID"));
        map.put("giftName", rs.getString("GIFT_NAME"));
        map.put("transUser", rs.getString("TRANS_USER"));
        map.put("giftQty", rs.getString("GIFT_QTY"));
        map.put("transQty", rs.getString("TRANS_QTY"));
        map.put("transFlag", rs.getString("TRANS_FLAG"));
        map.put("useTrans", rs.getString("USE_TRANS"));      
        map.put("transUses", rs.getString("TRANS_USES"));
        map.put("giftPrice", rs.getString("GIFT_PRICE"));
        map.put("transDate", rs.getString("TRANS_DATE"));
        map.put("operator", rs.getString("OPERATOR"));
        map.put("transMemo", rs.getString("TRANS_MEMO"));
        map.put("runId", rs.getString("RUN_ID"));
        outList.add(map);
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
   
    return  outList;
  }
  public Map<String,String> getOutstockByIdBack(Connection dbConn,int seqId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null; 
    Map<String ,String> map = null;
    String sql = "select goc.SEQ_ID as SEQ_ID,goc.OPERATOR_ID as OPERATOR_ID ,goc.OPERATOR as OPERATOR , goc.TRANS_QTY as TRANS_QTY,"
        +"goc.TRANS_FLAG as TRANS_FLAG, ci.CLASS_DESC as GIFT_TYPE,goc.GIFT_NAME as GIFT_NAME,goc.TRANS_MEMO as TRANS_MEMO,goc.TRANS_USES as TRANS_USES " 
        +"from (select go.SEQ_ID as SEQ_ID,go.operator as OPERATOR_ID,p.USER_NAME as OPERATOR , go.TRANS_QTY as TRANS_QTY,go.TRANS_FLAG " 
        +"as TRANS_FLAG,gi.gift_type as GIFT_TYPE ,gi.GIFT_NAME as GIFT_NAME,go.trans_uses as TRANS_USES,go.TRANS_MEMO as TRANS_MEMO " 
        +"from oa_present_outstock go left outer join oa_present_instock gi on go.gift_id = gi.seq_id "  
        +"left outer join person p on go.operator = p.seq_id where go.SEQ_ID="+seqId+") goc left outer join oa_kind_dict_item ci on goc.GIFT_TYPE=ci.SEQ_ID";
    try{
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if(rs.next()){
        map = new HashMap();
        map.put("seqId", rs.getString("SEQ_ID"));
        map.put("giftType", rs.getString("GIFT_TYPE"));
        map.put("transUses", rs.getString("TRANS_USES"));
        map.put("transQty", rs.getString("TRANS_QTY"));
        map.put("transFlag", rs.getString("TRANS_FLAG"));
        map.put("transMemo", rs.getString("TRANS_MEMO"));      
        map.put("operator", rs.getString("OPERATOR"));
        map.put("giftName", rs.getString("GIFT_NAME"));
        map.put("operatorId", rs.getString("OPERATOR_ID"));
      } 
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return map;
  }
  public void updateGiftBack(Connection dbConn,int seqId,String transFlag) throws Exception {
    Statement stmt = null;
    ResultSet rs = null; 
    String sql = "update oa_present_outstock set TRANS_FLAG = TRANS_FLAG + " + transFlag + " where SEQ_ID=" + seqId;
    try{
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  /**
   * 分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String toSearchData(Connection conn,Map request,String giftType,String giftId,String giftName,String fromDate,String toDate) throws Exception{
    giftName = giftName.replaceAll("'", "''");
   String sql = "select goip.SEQ_ID as SEQ_ID,goip.gift_name as GIFT_NAME,p2.USER_NAME as TRANS_USER ,goip.GIFT_QTY as GIFT_QTY, "
    +"goip.TRANS_QTY as TRANS_QTY,goip.trans_flag as TRANS_FLAG, goip.USE_TRANS as USE_TRANS,"
    +"goip.TRANS_USES as TRANS_USES, goip.gift_price as GIFT_PRICE,goip.TRANS_DATE as TRANS_DATE,goip.OPERATOR as OPERATOR,goip.TRANS_MEMO as TRANS_MEMO,goip.RUN_ID as RUN_ID from"
    +"(select go.SEQ_ID as SEQ_ID,gi.gift_name as GIFT_NAME,go.TRANS_USER as TRANS_USER,gi.GIFT_QTY as GIFT_QTY, "
    +"go.TRANS_QTY as TRANS_QTY,go.trans_flag as TRANS_FLAG, go.TRANS_QTY-go.trans_flag as USE_TRANS,"
    +"go.TRANS_USES as TRANS_USES, gi.gift_price as GIFT_PRICE,go.TRANS_DATE as TRANS_DATE,p.USER_NAME as OPERATOR ,go.TRANS_MEMO as TRANS_MEMO,go.RUN_ID as RUN_ID from "
    +"oa_present_outstock go left outer join oa_present_instock gi on go.gift_id=gi.seq_Id left outer join person p on go.operator=p.SEQ_ID where 1=1 ";
    
    
    if(!giftName.equals("")){
      sql = sql + " and gi.GIFT_NAME like '%"+giftName+"%'";
    }
    if(!giftId.equals("")){
      sql = sql + " and gi.SEQ_ID = "+giftId;
    }
    if(!giftType.equals("")){
      sql = sql + " and gi.GIFT_TYPE = '" + giftType + "'";
    }
    if(!fromDate.equals("")){
      sql = sql + " and " + YHDBUtility.getDateFilter("go.TRANS_DATE",fromDate, ">=");
    }
    if(!fromDate.equals("")){
      sql = sql + " and " + YHDBUtility.getDateFilter("go.TRANS_DATE",fromDate, ">=");
    }
    if(!toDate.equals("")){
      sql = sql + " and " + YHDBUtility.getDateFilter("go.TRANS_DATE",toDate + " 23:59:59", "<=");
    }
    sql = sql  +" order by TRANS_DATE) goip left outer join person p2 on goip.TRANS_USER = p2.SEQ_ID";
   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      /*String sql = "select gpd.SEQ_ID as SEQ_ID, gpd.GIFT_NAME as GIFT_NAME , c.class_desc as GIFT_TYPE, gpd.GIFT_PRICE as GIFT_PRICE,"
            +"gpd.GIFT_SUPPLIER as GIFT_SUPPLIER,gpd.GIFT_QTY as GIFT_QTY, p2.user_name as GIFT_KEEPER, gpd.DEPT_ID as DEPT_ID,"
            +"gpd.GIFT_CREATOR as GIFT_CREATOR, gpd.CREATE_DATE as CREATE_DATE,gpd.GIFT_MEMO as GIFT_MEMO from oa_kind_dict_item c right outer join (select g.SEQ_ID as SEQ_ID,g.GIFT_NAME as GIFT_NAME ,g.GIFT_TYPE as GIFT_TYPE,g.GIFT_PRICE as GIFT_PRICE,g.GIFT_SUPPLIER as GIFT_SUPPLIER,"
            +"g.GIFT_QTY as GIFT_QTY, g.gift_keeper as GIFT_KEEPER,d.DEPT_NAME as DEPT_ID,p.USER_NAME as GIFT_CREATOR,g.CREATE_DATE as CREATE_DATE,"
            +"g.GIFT_MEMO as GIFT_MEMO from PERSON p right outer join  GIFT_INSTOCK g on g.GIFT_CREATOR =p.SEQ_ID left outer join DEPARTMENT d " 
            +"on g.DEPT_ID =d.SEQ_ID g.DEPT_ID =d.SEQ_ID  where g.GIFT_TYPE = '"+giftType+"' and g.GIFT_NAME like '%"+giftName+"%' and g.GIFT_CODE='"+giftCode+"' and g.gift_desc like '%"+giftDesc+"%'  ) gpd on c.seq_id=gpd.GIFT_TYPE left OUTER join person p2 ON gpd.GIFT_KEEPER=p2.seq_id  order by gpd.GIFT_NAME";
 */     String dateStr = dateFormat.format(new Date());
      //System.out.println(sql);
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    
    return pageDataList.toJson();
  }
  public List<Map<String,String>> selectReport(Connection dbConn,String giftType,String giftId,String fromDate,String toDate) throws Exception{
    String sql ="select gi.SEQ_ID as SEQ_ID,gi.gift_name as GIFT_NAME,gi.GIFT_QTY as GIFT_QTY"
     +",ci.CLASS_DESC as CLASS_DESC ,gi.create_date as CREATE_DATE,gi.GIFT_QTY*gi.GIFT_PRICE as GIFT_TOTAL"
     +" ,gi.gift_price as GIFT_PRICE,p.USER_NAME as GIFT_CREATOR, gi.GIFT_MEMO as GIFT_MEMO from "
     +"oa_present_instock gi  left outer join person p on gi.gift_creator=p.SEQ_ID left outer join oa_kind_dict_item ci on gi.GIFT_TYPE=ci.seq_id where 1=1 ";
     if(!giftId.equals("")){
       sql = sql + " and gi.SEQ_ID = "+giftId;
     }
     if(!giftType.equals("")){
       sql = sql + " and gi.GIFT_TYPE = '" + giftType + "'";
     }
     if(!fromDate.equals("")){
       sql = sql + " and " + YHDBUtility.getDateFilter("gi.CREATE_DATE",fromDate, ">=");
     }
     if(!toDate.equals("")){
       sql = sql + " and " + YHDBUtility.getDateFilter("gi.CREATE_DATE",toDate, "<=");
     }
     sql = sql  +" order by gi.gift_name";
     Statement stmt = null;
     ResultSet rs = null; 
     List<Map<String,String>> list = new ArrayList<Map<String,String>>();
     try{
       stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map = new HashMap<String,String>();
        map.put("seqId", rs.getString("SEQ_ID"));
        map.put("giftName", rs.getString("GIFT_NAME"));
        map.put("classDesc", rs.getString("CLASS_DESC"));
        map.put("createDate", rs.getString("CREATE_DATE"));
        map.put("giftCreator", rs.getString("GIFT_CREATOR"));
        map.put("giftTotal", rs.getString("GIFT_TOTAL"));
        map.put("giftQty", rs.getString("GIFT_QTY"));
        map.put("giftMemo", rs.getString("GIFT_MEMO"));
        map.put("giftPrice", rs.getString("GIFT_PRICE"));
        list.add(map);
      }
     }catch(Exception ex) {
       throw ex;
     }finally {
       YHDBUtility.close(stmt, rs, log);
     }
     return list;
   }
  
  public List<Map<String,String>> selectProductInfo(Connection dbConn,String giftType,String giftId,String fromDate,String toDate) throws Exception{
    String sql ="select gi.SEQ_ID as SEQ_ID, gi.GIFT_NAME  as GIFT_NAME ,gi.GIFT_QTY as GIFT_QTY,gi.GIFT_UNIT as GIFT_UNIT,go.use_gift_qty as USE_GIFT_QTY from oa_present_instock gi left outer join "
           +"(select sum(trans_qty- trans_flag) as use_gift_qty,gift_id as gift_id  FROM oa_present_outstock   group by gift_id) go on gi.seq_id = go.gift_id where 1=1 "; 
     if(!giftId.equals("")){
       sql = sql + " and gi.SEQ_ID = "+giftId;
     }
     if(!giftType.equals("")){
       sql = sql + " and gi.GIFT_TYPE = '" + giftType + "'";
     }
     if(!fromDate.equals("")){
       sql = sql + " and " + YHDBUtility.getDateFilter("gi.CREATE_DATE",fromDate, ">=");
     }
     if(!toDate.equals("")){
       sql = sql + " and " + YHDBUtility.getDateFilter("gi.CREATE_DATE",toDate + " 23:59:59", "<=");
     }
     sql = sql  +" order by gi.gift_name";
     Statement stmt = null;
     ResultSet rs = null; 
     List<Map<String,String>> list = new ArrayList<Map<String,String>>();
     try{
       stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map = new HashMap<String,String>();
        map.put("seqId", rs.getString("SEQ_ID"));
        map.put("giftName", rs.getString("GIFT_NAME"));
        map.put("giftUnit", rs.getString("GIFT_UNIT"));
        map.put("useGiftQty", rs.getString("USE_GIFT_QTY"));
        map.put("giftQty", rs.getString("GIFT_QTY"));
        list.add(map);
      }
     }catch(Exception ex) {
       throw ex;
     }finally {
       YHDBUtility.close(stmt, rs, log);
     }
     return list;
   }
  
  
  public List<Map<String,String>> getGiftOutByDeptId(Connection dbConn,String giftType,String giftId,String fromDate,String toDate,String deptId) throws Exception{
    String sql ="select gi.SEQ_ID as SEQ_ID,gi.gift_name as GIFT_NAME,p.USER_NAME as TRANS_USER,go.TRANS_QTY as TRANS_QTY, "
    +"go.trans_flag as TRANS_FLAG, go.TRANS_QTY-go.trans_flag as USE_TRANS,"
    +"go.TRANS_QTY*gi.GIFT_PRICE TRANS_TOTAL, gi.gift_price as GIFT_PRICE,go.TRANS_DATE as TRANS_DATE,p.USER_NAME as OPERATOR from "
    +"oa_present_outstock go left outer join oa_present_instock gi on go.gift_id=gi.seq_Id left outer join person p on go.TRANS_USER=p.SEQ_ID where p.dept_id=" + deptId;
     if(!giftId.equals("")){
       sql = sql + " and gi.SEQ_ID = "+giftId;
     }
     if(!giftType.equals("")){
       sql = sql + " and gi.GIFT_TYPE = '" + giftType + "'";
     }
     if(!fromDate.equals("")){
       sql = sql + " and " + YHDBUtility.getDateFilter("go.TRANS_DATE",fromDate, ">=");
     }
     if(!toDate.equals("")){
       sql = sql + " and " + YHDBUtility.getDateFilter("go.TRANS_DATE",toDate + " 23:59:59", "<=");
     }
     sql = sql  +" order by go.TRANS_DATE";
     Statement stmt = null;
     ResultSet rs = null; 
     List<Map<String,String>> list = new ArrayList<Map<String,String>>();
     try{
       stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map = new HashMap<String,String>();
        map.put("seqId", rs.getString("SEQ_ID"));
        map.put("giftName", rs.getString("GIFT_NAME"));
        map.put("transUser", rs.getString("TRANS_USER"));
        map.put("transTotal", rs.getString("TRANS_TOTAL"));
        map.put("giftPrice", rs.getString("GIFT_PRICE"));
        map.put("transDate", rs.getString("TRANS_DATE"));
        map.put("transQty", rs.getString("TRANS_QTY"));
        map.put("useTrans", rs.getString("USE_TRANS"));
        list.add(map);
      }
     }catch(Exception ex) {
       throw ex;
     }finally {
       YHDBUtility.close(stmt, rs, log);
     }
     return list;
   }
}
