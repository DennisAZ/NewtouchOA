package yh.subsys.inforesouce.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.subsys.inforesouce.data.YHMateValue;

public class YHMateElementLogic {
  private static Logger log = Logger.getLogger(YHMateElementLogic.class);
  
  /**
   * 添加资源类型数据
   * @param seqIds  1,2,3,4
   */
  public void addelement(String boardNo,String cnName,String enName,String defineText,String aimText,String constraint,String repeat,String element_type,String typeId,int pid,String elemntType, Connection conn,YHPerson person)
  throws Exception{
    Statement stmt = null;
    ResultSet rs = null;

    try {
      stmt = conn.createStatement();
      String sql ="insert into oa_mate_kind(NUMBER_ID,CHNAME,ENNAME,DEFINEE,AIM,CONSTRAINTT,REPEATE ,ELEM_ID,TYPE_ID,PARENT_ID,ELEMENT_TYP)" +
      " values('"+boardNo+"','"+cnName+"','"+enName+"','"+defineText+"','"+aimText+"','"+constraint+"','"+repeat+"','"+element_type+"','"+typeId+"','"+pid+"','"+elemntType+"')";
      //System.out.println(sql);
      int i = stmt.executeUpdate(sql);    
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, log);
    }
  // return result;
  }
  
  /**
   * 添加自定义值域,返回刚插入记录的seqId
   * @param seqIds  1,2,3,4
   */
  public int addvalue(String valueId,String valueName, Connection conn,YHPerson person)
  throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;

    try { 
      // String sql ="insert into mate_value(VALUE_ID,VALUE_NAME)values('"+valueId+"','"+valueName+"')";
      String sql ="insert into oa_mate_value(type_number,VALUE_NAME)values('"+valueId+"','"+valueName+"')";
      //YHOut.println(sql);
      String[] str = {"SEQ_ID"};
      ps = conn.prepareStatement(sql, str);
      int id = ps.executeUpdate();
      rs = ps.getGeneratedKeys();//可以返回刚刚插入的记录的自动增长的ID值
      if(id !=0){
        if(rs.next()){
          return  rs.getInt(1);
        } 
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
    return 0;
  }
  /**
   * 查询mate_type表中的value_range字段
   * 目的：获得mate_value表中的seq_id,追加到mate_type表中的value_range字段
   * @throws Exception 
   */

   public static String findvalueRange(Connection conn,int seqId)throws Exception{

    Statement stmt = null;
    ResultSet rs = null;
    String StringRange="";
    String sql="";
    try{
     stmt = conn.createStatement();
     sql="select VALUE_RANGE from oa_mate_kind where SEQ_ID="+seqId;
     rs = stmt.executeQuery(sql);
     if(rs.next()){
     StringRange = rs.getString("VALUE_RANGE");
     }
    }catch (Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stmt,rs, null);
    }
     return StringRange;
   }
  
  /**
   * 添加值域
   * @throws Exception 
   */
  public void addValueRange(Connection conn, int seqId,String valueId,String valueName,YHPerson person) throws Exception{
    PreparedStatement ps = null; 

    String vRange="";
    String value="";
    String sql = ""; 
    int k = addvalue(valueId, valueName,  conn, person);
    if(k!=0){//如果像值域表插入成功，才向类型表插入    
      if(checkValueRange(conn, seqId) != null){
      //update mate_type set VALUE_RANGE = VALUE_RANGE ||','||'406' 每次添加值域时 把值域表中的seq_id 更新到mate_type表
        vRange = findvalueRange(conn, seqId);
        value = vRange+","+k;
        sql = "update oa_mate_kind set VALUE_RANGE = '"+ value +"' where seq_id="+seqId;
       //sql = "update mate_type set VALUE_RANGE = VALUE_RANGE ||','||'" +k +"' where seq_id="+seqId;        
      }else{
        sql = "update oa_mate_kind set VALUE_RANGE ='" +k +"' where seq_id="+seqId; 
      }
      //YHOut.println(sql);
      try{
        ps = conn.prepareStatement(sql);
        ps.executeUpdate();
      } catch (Exception e){
        throw e;
      }finally{
        YHDBUtility.close(ps,null, null);
      }
    }    
  }
  
  /**
   * 判断值域是否为空
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String checkValueRange(Connection conn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "select VALUE_RANGE from oa_mate_kind where seq_id =" + seqId;
    //YHOut.println(sql);
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        String ids = rs.getString(1);
        if(ids !=null && ids.length()>0&& ids.trim().length()>0){
          return rs.getString(1);
        }
        return null;
      }
    } catch (SQLException e) {
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return null;
  }
 
  /**
   *  查询值域
   */
  public String selectva(int seqId, Connection conn,YHPerson person)
  throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    String num="";
    try {
      stmt = conn.createStatement();
      //String sql ="insert into mate_value(VALUE_ID,VALUE_NAME)values('"+valueId+"','"+valueName+"')";
      String sql ="select value_range from oa_mate_kind where SEQ_ID="+seqId;  
      rs = stmt.executeQuery(sql);
      //System.out.println(sql);
      while(rs.next()){
      num = rs.getString(1);
      }
     return num; 
     // rs = ps.executeQuery();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  /**
   *  查询值域
   */
  public List<YHMateValue> selectvalue(int seqId, Connection conn,YHPerson person)
  throws Exception{

    Statement stmt = null;
    ResultSet rs = null;
  String mumvalue  = selectva(seqId, conn,person);
  if(YHUtility.isNullorEmpty(mumvalue)){
    mumvalue = "0";
  }
    try {
      stmt = conn.createStatement();
       
      String sql ="select seq_id, TYPE_NUMBER,VALUE_NAME from oa_mate_value where SEQ_ID in (" +mumvalue+")";  
      //System.out.println(sql);
      rs = stmt.executeQuery(sql);
     
      List<YHMateValue> va = new ArrayList<YHMateValue>();
      while(rs.next()){
        YHMateValue mv = new YHMateValue();
        mv.setSeqId(rs.getInt("seq_id"));
        mv.setTypeNumber(rs.getString("TYPE_NUMBER"));
        mv.setValueName(rs.getString("VALUE_NAME"));
        va.add(mv);
      }
     return va; 
     // rs = ps.executeQuery();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  /**
   * 更新值域
   */
  public void updatevalue(int aid, String valueId, String valueName, Connection conn,YHPerson person)
  throws Exception{
    Statement stmt = null;
    String num="";
    try {
      stmt = conn.createStatement();
      //String sql ="insert into mate_value(VALUE_ID,VALUE_NAME)values('"+valueId+"','"+valueName+"')";
      String sql ="update oa_mate_value set type_number = '"+ valueId +"', value_name ='"+valueName+"' where seq_id="+aid;
      //System.out.println(sql);
      stmt.executeUpdate(sql);
      
      // rs = ps.executeQuery();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  /**
   * 判断存在这个编号了么
   * @param conn
   * @param nos
   * @return
   * @throws Exception
   */
  public boolean isExitNos(Connection conn, String nos, String seqId)  throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "select * from oa_mate_kind where number_Id = '" +  merginNos(nos)+"'";
    if(!YHUtility.isNullorEmpty(seqId)){
      sql += " and seq_id !=" + seqId;
    }
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        return true;
      }
    } catch (SQLException e) {
      throw e;
    }finally{
      YHDBUtility.close(ps, rs, null);
    }
    return false;
  }
  
  public String merginNos(String nos){
    int number = Integer.parseInt(nos);
    if(number > 100){
       return "MEX" + number;
    }
    return "M" + number;
  }
}
