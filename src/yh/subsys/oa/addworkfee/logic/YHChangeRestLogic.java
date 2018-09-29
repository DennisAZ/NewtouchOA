package yh.subsys.oa.addworkfee.logic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.subsys.oa.addworkfee.data.YHChangeRest;
import yh.subsys.oa.addworkfee.data.YHFestival;



/**
 * 调休
 * @author Administrator
 *
 */
public class YHChangeRestLogic{
  
  
  public int addYHChangeRest(Connection conn, YHChangeRest rest) throws Exception{
    String sql = " insert into oa_add_modify(YEAR_FLAG, FROM_DATE , TO_DATE, TYPE_ID ) values(?, ?, ?, ?)";
    PreparedStatement ps = null;
    int k = 0;
    try {
        ps = conn.prepareStatement(sql);
        ps.setInt(1, rest.getYear());
        ps.setDate(2, new Date(rest.getBeginDate().getTime()));
        ps.setDate(3, new Date(rest.getEndDate().getTime()));
        ps.setInt(4, rest.getType());
        k = ps.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    } finally {
       YHDBUtility.close(ps, null, null);
    }
    return k;
  }
  
  /**
   * 某年的调休表
   * @param conn
   * @param date
   * @return
   * @throws SQLException
   */
  public List<YHChangeRest> findChangeRestList(Connection conn, String date) throws SQLException{
    String sql = "select SEQ_ID, YEAR_FLAG, FROM_DATE , TO_DATE, TYPE_ID  from oa_add_modify where year_flag=?" ;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHChangeRest> dosc = new ArrayList<YHChangeRest>();
    String year = YHUtility.getCurDateTimeStr("yyyy");
    try{
      if(date != null ){
        year = date;
      }
      ps = conn.prepareStatement(sql);
      ps.setInt(1, Integer.parseInt(year));
      rs = ps.executeQuery();
      while(rs.next()){
        YHChangeRest fest = new YHChangeRest();
        fest.setSeqId(rs.getInt("SEQ_ID"));
        fest.setYear(rs.getInt("YEAR_FLAG"));
        fest.setBeginDate(rs.getDate("FROM_DATE"));
        fest.setEndDate(rs.getDate("TO_DATE"));
        fest.setType(rs.getInt("TYPE_ID"));
        dosc.add(fest);
      }
    } catch (SQLException e){
      throw e;
    }
    return dosc;
  }
  
  /**
   * 查找某一调休
   * @param conn
   * @param doc
   * @param user
   * @throws SQLException 
   */
  public YHChangeRest findYHChangeRest(Connection conn, int date) throws SQLException{
    String sql = "select SEQ_ID, YEAR_FLAG, FROM_DATE , TO_DATE, TYPE_ID  from oa_add_modify where SEQ_ID=?" ;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
     
      ps = conn.prepareStatement(sql);
      ps.setInt(1, date);
      rs = ps.executeQuery();
      if(rs.next()){
        YHChangeRest fest = new YHChangeRest();
        fest.setSeqId(rs.getInt("SEQ_ID"));
        fest.setYear(rs.getInt("YEAR_FLAG"));
        fest.setBeginDate((java.util.Date)rs.getObject("FROM_DATE"));
        fest.setEndDate((java.util.Date)rs.getObject("TO_DATE"));
        fest.setType(rs.getInt("TYPE_ID"));
        return fest;
      }
    } catch (SQLException e){
      throw e;
    }
    return null;
  }
  
  /**
   * 返回所有的年份
   * @param conn
   * @return
   * @throws SQLException
   */
  public List<Integer> findYearList(Connection conn)throws SQLException{
    String sql = "select DISTINCT YEAR_FLAG from oa_add_modify order by YEAR_FLAG desc" ;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<Integer> years = new ArrayList<Integer>();
    try{
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        years.add(rs.getInt("YEAR_FLAG"));
      }
    } catch (SQLException e){
      throw e;
    }
    return years;
  }
  
  /**
   * 更新
   * @param conn
   * @return
   * @throws SQLException
   */
  public int updateChangeRest(Connection conn,  YHChangeRest fest)throws SQLException{
    String sql = "update oa_add_modify set YEAR_FLAG=?,  FROM_DATE=?, TO_DATE=?, TYPE_ID=? where SEQ_ID=?" ;
    PreparedStatement ps = null;
    int k =0;
    try{
      ps = conn.prepareStatement(sql);
      ps.setInt(1, fest.getYear());
      ps.setDate(2, new java.sql.Date(fest.getBeginDate().getTime()));
      ps.setDate(3, new java.sql.Date(fest.getEndDate().getTime()));
      ps.setInt(4, fest.getType());
      ps.setInt(5, fest.getSeqId());
       k = ps.executeUpdate();
    } catch (SQLException e){
      throw e;
    }
    return k;
  }
  
  /**
   * 删除一个日期
   * @param conn
   * @param doc
   * @param user
   * @throws Exception 
   */
  public int delChangeRest(Connection conn, int seqId) throws Exception{
    String sql = "delete from oa_add_modify where seq_id=" + seqId ;
    PreparedStatement ps = null;
    int ok =0;
    try{
      ps = conn.prepareStatement(sql);
      ok = ps.executeUpdate();
    } catch (Exception e){
      throw e;
    }
    return ok;
  }

  /**
   * 返回所有的年份
   * @param conn
   * @return
   * @throws SQLException
   */
  public int updateFestival(Connection conn,  YHChangeRest fest)throws SQLException{
    String sql = "update oa_add_modify set YEAR_FLAG=?, TYPE_ID=?, FROM_DATE=?, TO_DATE=? where SEQ_ID=?" ;
    PreparedStatement ps = null;
    int k =0;
    try{
      ps = conn.prepareStatement(sql);
      ps.setInt(1, fest.getYear());
      ps.setInt(2, fest.getType());
      ps.setDate(3, new java.sql.Date(fest.getBeginDate().getTime()));
      ps.setDate(4, new java.sql.Date(fest.getEndDate().getTime()));
      ps.setInt(5, fest.getSeqId());
      k = ps.executeUpdate();
    } catch (SQLException e){
      throw e;
    }
    return k;
  }
}
