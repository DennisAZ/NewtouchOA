package yh.core.funcs.mysqldb.logic;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Test;


public class YHMySqlDBLogicTest {

  @Test
  public void testGetTableInfo() {
      YHMySqlDBLogic my = new YHMySqlDBLogic();
      try {
       // Connection conn = TestDbUtil.getConnection(false, "yh");
       // my.getTableInfo(conn);
      } catch (Exception e) {
        e.printStackTrace();
      }
     
  }

}
