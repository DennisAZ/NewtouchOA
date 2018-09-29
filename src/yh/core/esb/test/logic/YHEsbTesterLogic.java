package yh.core.esb.test.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.UUID;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;

import org.apache.log4j.Logger;

import yh.core.esb.frontend.YHEsbFrontend;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHEsbTesterLogic {

  private static Logger log = Logger.getLogger(YHEsbTesterLogic.class);


  /**
   * 清空esbTransferStatus表
   * @param dbConn
   * @throws Exception
   */
  public void deleteTransferStatus(Connection dbConn) throws Exception {
    Statement st = null;
    try {
      String sql = "delete from ESB_TRANSFER_STATUS";
      st = dbConn.createStatement();
      st.executeUpdate(sql);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(st, null, log);
    }
  }
  
  /**
   * 清空esbTransfer表
   * @param dbConn
   * @throws Exception
   */
  public void deleteTransfer(Connection dbConn) throws Exception {
    Statement st = null;
    try {
      String sql = "delete from ESB_TRANSFER";
      st = dbConn.createStatement();
      st.executeUpdate(sql);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(st, null, log);
    }
  }
  
  /**
   * 清空esbSysMsg表
   * @param dbConn
   * @throws Exception
   */
  public void deleteSysMsg(Connection dbConn) throws Exception {
    Statement st = null;
    try {
      String sql = "delete from ESB_SYS_MSG";
      st = dbConn.createStatement();
      st.executeUpdate(sql);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(st, null, log);
    }
  }
  
  /**
   * 上传文件数
   * @param dbConn
   * @throws Exception
   */
  public int getUploadNo(Connection dbConn) throws Exception {
    Statement st = null;
    ResultSet rs = null;
    try {
      String sql = "select count(1) as AMOUNT from ESB_TRANSFER";
      st = dbConn.createStatement();
      rs = st.executeQuery(sql);
      if (rs.next()) {
        return rs.getInt("AMOUNT");
      }
      return -1;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(st, null, log);
    }
  }
  
  /**
   * 上传成功数
   * @param dbConn
   * @throws Exception
   */
  public int getUploadSuccessfulNo(Connection dbConn) throws Exception {
    Statement st = null;
    ResultSet rs = null;
    try {
      String sql = "select count(1) as AMOUNT from ESB_TRANSFER where STATUS in ('2', '3')";
      st = dbConn.createStatement();
      rs = st.executeQuery(sql);
      if (rs.next()) {
        return rs.getInt("AMOUNT");
      }
      return -1;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(st, null, log);
    }
  }
  
  /**
   * 下载文件数
   * @param dbConn
   * @throws Exception
   */
  public int getDownloadNo(Connection dbConn) throws Exception {
    Statement st = null;
    ResultSet rs = null;
    try {
      String sql = "select count(1) as AMOUNT from ESB_TRANSFER_STATUS where STATUS != '0'";
      st = dbConn.createStatement();
      rs = st.executeQuery(sql);
      if (rs.next()) {
        return rs.getInt("AMOUNT");
      }
      return -1;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(st, null, log);
    }
  }
  
  /**
   * 上传成功数
   * @param dbConn
   * @throws Exception
   */
  public int getDownloadSuccessfulNo(Connection dbConn) throws Exception {
    Statement st = null;
    ResultSet rs = null;
    try {
      String sql = "select count(1) as AMOUNT from ESB_TRANSFER_STATUS where STATUS = '2'";
      st = dbConn.createStatement();
      rs = st.executeQuery(sql);
      if (rs.next()) {
        return rs.getInt("AMOUNT");
      }
      return -1;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(st, null, log);
    }
  }
  
  public void clearDB(Connection dbConn) throws Exception {
    this.deleteSysMsg(dbConn);
    this.deleteTransfer(dbConn);
    this.deleteTransferStatus(dbConn);
  }
  
  /**
   * 测试,并把结果记录到log文件中
   * @param log
   * @param testFolder
   * @param toId
   * @param time
   * @throws Exception 
   */
  public void test(Connection dbConn, File log, File testFolder, String toId, int toCount, long time) throws Exception {
    //this.clearDB(dbConn);
    
    long start = System.currentTimeMillis();
    this.test(testFolder, toId, time);
    long uEnd = System.currentTimeMillis();
    
    
    
    while (!YHEsbFrontend.isCompleted() || !isUploadFinished(dbConn)) {
      Thread.sleep(1000);
    }
    stat(dbConn, log, start, uEnd);
  }
  
  public void stat(Connection dbConn, File log, long start, long uEnd) throws Exception {
    int us = this.getUploadSuccessfulNo(dbConn);
    int d = this.getDownloadNo(dbConn);
    int remain = us - d;
    while (remain > 0) {
      Thread.sleep(1000);
      d = this.getDownloadNo(dbConn);
      remain = us - d;
    }
    
    long dEnd =  System.currentTimeMillis();
    
    long uploadTimes = uEnd - start;
    long downloadTimes = dEnd - start;
    
    
    
    int u = this.getUploadNo(dbConn);
    int ds = this.getDownloadSuccessfulNo(dbConn);
    writeLog(log, start, uEnd, dEnd, u, us, d, ds, getRate(dbConn));
  }
  
  private int[] getTransferInfo(Connection dbConn) {
    return new int[] {};
  }
  
  private boolean isUploadFinished(Connection dbConn) throws Exception {
    Statement st = null;
    ResultSet rs = null;
    try {
      String sql = "select count(1) as AMOUNT" +
      		" from ESB_TRANSFER" +
      		" where STATUS not in ('2', '3', '4')";
      st = dbConn.createStatement();
      rs = st.executeQuery(sql);
      if (rs.next()) {
        return rs.getInt("AMOUNT") == 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(st, null, log);
    }
    return false;
  }
  
  private double getRate(Connection dbConn) throws Exception {
    Statement st = null;
    ResultSet rs = null;
    try {
      String sql = "select (select count(1) from ESB_TRANSFER) as C1," +
      		"(select count(1) from ESB_TRANSFER where STATUS = '3') as C2";
      st = dbConn.createStatement();
      rs = st.executeQuery(sql);
      if (rs.next()) {
        int c1 = rs.getInt("C1");
        int c2 = rs.getInt("C2");
        return (double)c2 / c1;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(st, null, log);
    }
    return 0;
  }
  
  private void writeLog(File log, long start, long uEnd, long dEnd, int u, int us, int d, int ds, double rate) throws IOException {
    try {
      FileWriter fw = new FileWriter(log);
      BufferedWriter br = new BufferedWriter(fw);
      
      NumberFormat nf = NumberFormat.getPercentInstance();
      nf.setMinimumFractionDigits(2);
      
      String startStr = YHUtility.getDateTimeStr(new Date(start));
      String uEndStr = YHUtility.getDateTimeStr(new Date(uEnd));
      String dEndStr = YHUtility.getDateTimeStr(new Date(dEnd));
      
      br.write(String.format("测试开始时间:%s\n上传结束时间:%s\n下载结束时间:%s\n", startStr, uEndStr, dEndStr));
      br.write(String.format("发送文件数:%d\n发送到服务器成功数:%d\n接收文件数:%d\n成功接收数:%d\n", u, us, d, ds));
      
      br.write(String.format("发送成功率:%s\n接收成功率:%s\n传输成功率:%s\n", nf.format((double)us / u),nf.format((double)ds / d), nf.format(rate)));
      br.flush();
      fw.flush();
      br.close();
      fw.close();
    } catch (IOException e) {
      //
      throw e;
    }
  }
  
  public void test(final File testFolder, String toId, long time) throws InterruptedException {
    if (YHUtility.isNullorEmpty(toId)) {
      toId = "ALL_USERS";
    }
    final String to = toId;
    if (YHEsbFrontend.login() == 0) {
      Thread t = new Thread(new Runnable() {
        public void run() {
          boolean flag = true;
          while (flag) {
            try {
              //System.out.println("发送测试文件");
              for (File f : testFolder.listFiles()) {
                if (!flag) {
                  break;
                }
                if (f.isFile()) {
                  final String guid = UUID.randomUUID().toString();
                  int code = YHEsbFrontend.send(f, to  ,guid  ,"", "");
                  while (code != 0 && flag) {
                    Thread.sleep(1000);
                    code = YHEsbFrontend.send(f, to , guid  ,"", "");
                  }
                }
              }
            } catch (InterruptedException e) {
              flag = false;
            }
          }
        }
      });
      t.start();
      Thread.sleep(time);
      t.interrupt();
    }
  }
}
