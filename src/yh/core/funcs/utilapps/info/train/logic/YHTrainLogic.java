package yh.core.funcs.utilapps.info.train.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;

public class YHTrainLogic {
    
  public YHPageDataList queryPrice(Connection conn, YHPageDataList pageDataList) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      int length = pageDataList.getRecordCnt();
      for(int i = 0 ; i < length ; i++){
        YHDbRecord record = pageDataList.getRecord(i);
        String pdistance = record.getValueByName("pdistance").toString();
        String typePrice = record.getValueByName("typePrice").toString();
        
        if(typePrice.isEmpty()){
          record.updateField("price", "");
          continue;
        }
        String sql = " select " + typePrice + " from oa_train_price where distance = ? ";
        
        ps = conn.prepareStatement(sql);
        ps.setString(1, pdistance);
        rs = ps.executeQuery();
        if(rs.next()){
          String price = rs.getString(typePrice);
          record.updateField("price", price.split(",")[0]);
        }
      }
      return pageDataList;
    }catch(Exception ex) {
      throw ex;
    }
  }
  
  public YHPageDataList queryAddress(Connection conn, YHPageDataList pageDataList) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      int length = pageDataList.getRecordCnt();
      for(int i = 0 ; i < length ; i++){
        YHDbRecord record = pageDataList.getRecord(i);
        String pdistance = record.getValueByName("pdistance").toString();
        String typePrice = record.getValueByName("typePrice").toString();
        
        if(typePrice.isEmpty()){
          record.updateField("price", "");
          continue;
        }
        String sql = " select " + typePrice + " from oa_train_price where distance = ? ";
        
        ps = conn.prepareStatement(sql);
        ps.setString(1, pdistance);
        rs = ps.executeQuery();
        if(rs.next()){
          String price = rs.getString(typePrice);
          record.updateField("price", price.split(",")[0]);
        }
      }
      return pageDataList;
    }catch(Exception ex) {
      throw ex;
    }
  }
  
  public String countStation(Connection conn, Map<String,String> map) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;  
    try{
      String sql = " select count(1) count_station from TRAIN_PASS p where p.trainid = ? "
                 + " and p.distance >= (select p.distance from TRAIN_PASS p "
                 + " join oa_train_station s on p.station = s.id "
                 + " where p.trainid = ? and s.station = ?) "
                 + " and p.distance <= (select p.distance from TRAIN_PASS p "
                 + " join oa_train_station s on p.station = s.id "
                 + " where p.trainid = ? and s.station = ?) ";
      ps = conn.prepareStatement(sql);
      
      String start = map.get("pstart") == null ? map.get("start") : map.get("pstart");
      String end = map.get("pend") == null ? map.get("end") : map.get("pend");
      String seqId = map.get("seqId");
      ps.setString(1, seqId);
      ps.setString(2, seqId);
      ps.setString(3, start);
      ps.setString(4, seqId);
      ps.setString(5, end);
      rs = ps.executeQuery();
      String countStation = "";
      if(rs.next()){
        countStation = rs.getString("count_station");
      }
      return countStation;
    }catch(Exception ex) {
      throw ex;
    }
  }
  
  public String allPrice(Connection conn, Map<String,String> map) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;  
    String allPrice = "";
    try{
      if(map.get("typePrice").isEmpty()){
        return allPrice = "无,无,无,无,无,无,无";
      }
      String sql = " select " + map.get("typePrice") + "," + map.get("typePrice").replace("z", "w") + " from oa_train_price p "
                 + "where p.distance = ? ";
      ps = conn.prepareStatement(sql);
      
      String pdistance = map.get("pdistance");
      
      ps.setString(1, pdistance);
      rs = ps.executeQuery();
      
      if(rs.next()){
        allPrice = rs.getString(map.get("typePrice"));
        allPrice += ","+rs.getString(map.get("typePrice").replace("z", "w"));
      }
      return allPrice;
    }catch(Exception ex) {
      throw ex;
    }
  }
}
