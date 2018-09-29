package yh.rad.dbexputil.transplant.logic.core.processor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 * 所有处理器应该实现的接口
 * @author Think
 *
 */
public interface YHProcI {
  
  public  Object dbDataHandler(String o , int newDbType) throws Exception ;
  
  public  String xmlDataHandler(Object o , int newDbType,int oldDbType) throws Exception ;

  public  Object sqlParam2JavaParam(ResultSet rs, int index, int dbType)
  throws Exception ;
  
  public PreparedStatement java2sqlParam(Object pa , PreparedStatement ps , int i) throws Exception;
}
