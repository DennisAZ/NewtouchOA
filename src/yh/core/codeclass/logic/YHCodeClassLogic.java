package yh.core.codeclass.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.codeclass.data.YHCodeClass;
import yh.core.codeclass.data.YHCodeItem;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHCodeClassLogic {
  private static Logger log = Logger.getLogger(YHCodeClassLogic.class);
  public YHCodeClass selectCodeClassById(Connection dbConn,String seqId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null; 
    YHCodeClass codeClass = null;
    try {
      String queryStr = "select SEQ_ID, CLASS_NO, SORT_NO, CLASS_DESC, CLASS_LEVEL from oa_kind_dict where SEQ_ID= " + seqId;
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
     
      if(rs.next()){
        codeClass = new  YHCodeClass();
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
  /**
   * 得到所有的CODE_ITEM 根据CODE_CALSS的CLASS_NO
   * @param dbConn
   * @param classNo
   * @return
   * @throws Exception
   */
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
}
