package yh.setup.fis.acset.util;


import java.sql.Statement;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;


public class TDPeriodSave {
  /**
   * log
   */
  private static Logger log = Logger.getLogger(TDPeriodSave.class);
  
  
  /**
   * 插入会计期间
   * @param 
   * @param 
   */
  public static void saveAccoutPeriod(Statement stmt,
      String periodCntrl,
      String acctYear,
      String firstYearMonth,
      String dbName) throws Exception {
    //更新会计期间
    String sql = "";
    sql = "delete from " + dbName + ".DBO.ACCOUNTPERIOD";
    stmt.executeUpdate(sql);
    GregorianCalendar cal = new GregorianCalendar();
    
    if (periodCntrl.equals("1")) {
      sql = "update " + dbName + ".DBO.ACPARAM set PARA_VALUE='1'"
      + " where PARA_NAME='P20020'"
      + " and ACCT_YEAR='" + acctYear + "'";
      stmt.executeUpdate(sql);
    
      for (int i = 0; i < 12; i++) {
        cal.set(Integer.parseInt(acctYear), i, 1);
        int lastDay = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        sql = "insert into " + dbName + ".DBO.ACCOUNTPERIOD"
          + "(PERIOD_YEAR, PERIOD_NUM"
          + ", START_DATE, END_DATE)"
          + "values ('" + acctYear + "'"
          + ", " + (i + 1)
          + ", '" + acctYear + "-" + (i + 1) + "-" +  1 + "'"
          + ", '" + acctYear + "-" + (i + 1) + "-" +  lastDay + "')";
        stmt.executeUpdate(sql);
      }
    }else if (periodCntrl.equals("2")){
      //更新帐务参数中的会计期间控制参数
      sql = "update " + dbName + ".DBO.ACPARAM set PARA_VALUE='2'"
        + " where PARA_NAME='P20020'"
        + " and ACCT_YEAR='" + acctYear + "'";
      stmt.executeUpdate(sql);
      
      int firstYear = Integer.parseInt(firstYearMonth.substring(0, 4));
      int firstMonth = Integer.parseInt(firstYearMonth.substring(5, 7));
      for (int i = 0; i < 12; i++) {
        int month = firstMonth + i;
        int year = firstYear;
        if (month > 12) {
          month = month % 12;
          year++;
        }
        cal.set(year, month - 1, 1);
        int lastDay = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        sql = "insert into " + dbName + ".DBO.ACCOUNTPERIOD"
          + "(PERIOD_YEAR, PERIOD_NUM"
          + ", START_DATE, END_DATE)"
          + "values ('" + acctYear + "'"
          + ", " + (i + 1)
          + ", '" + year + "-" + month + "-" +  1 + "'"
          + ", '" + year + "-" + month + "-" +  lastDay + "')";
        stmt.executeUpdate(sql);
      }
    } else if (periodCntrl.equals("3")) {
      
      sql = "update " + dbName + ".DBO.ACPARAM set PARA_VALUE='3'"
      + " where PARA_NAME='P20020'"
      + " and ACCT_YEAR='" + acctYear + "'";
      
      stmt.executeUpdate(sql);
    
      for (int i = 0; i < 4; i=i+1 ) {
        cal.set(Integer.parseInt(acctYear), i*3+2, 1);
        int lastDay = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        sql = "insert into " + dbName + ".DBO.ACCOUNTPERIOD"
          + "(PERIOD_YEAR, PERIOD_NUM"
          + ", START_DATE, END_DATE)"
          + "values ('" + acctYear + "'"
          + ", " + (i + 1)
          + ", '" + acctYear + "-" + (i*3 + 1) + "-" +  1 + "'"
          + ", '" + acctYear + "-" + (i*3 + 3) + "-" +  lastDay + "')"; 
        stmt.executeUpdate(sql);
      }     
    }
  }
}
