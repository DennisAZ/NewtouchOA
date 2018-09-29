package yh.core.funcs.system.censorwords.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.funcs.system.censorwords.data.YHCensorWords;
import yh.core.util.db.YHDBUtility;

public class YHCensorWordsLogic {
  private static Logger log = Logger.getLogger(YHCensorWordsLogic.class);

  public boolean existsCensorWords(Connection dbConn, String find)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) FROM oa_examine_words WHERE FIND = '" + find
          + "'";
      rs = stmt.executeQuery(sql);
      long count = 0;
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count == 1) {
        return true;
      } else {
        return false;
      }

    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  public long existsCount(Connection dbConn, int userId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    long count = 0;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) FROM oa_person_ext WHERE SYS_USER='0'";
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        count = rs.getLong(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return count;
  }

  /**
   * 获取词语过滤过滤信息
   * @param dbConn
   * @return
   * @throws Exception
   */
  public ArrayList<YHCensorWords> getCensorWords(Connection dbConn)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHCensorWords censorWords = null;
    List list = new ArrayList();
    ArrayList<YHCensorWords> wordList = new ArrayList<YHCensorWords>();
    try {
      stmt = dbConn.createStatement();
      String sql = "select SEQ_ID, USER_ID, FIND, REPLACEMENT from oa_examine_words order by FIND";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        censorWords = new YHCensorWords();
        censorWords.setSeqId(rs.getInt("SEQ_ID"));
        censorWords.setUserId(rs.getInt("USER_ID"));
        censorWords.setFind(rs.getString("FIND"));
        censorWords.setReplacement(rs.getString("REPLACEMENT"));
        wordList.add(censorWords);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return wordList;
  }

  /**
   * 词语过滤查询
   * @param dbConn
   * @param userId
   * @param find
   * @param replacement
   * @return
   * @throws Exception
   */
  public ArrayList<YHCensorWords> getCensorWordsSearch(Connection dbConn,
      int userId, String find, String replacement) throws Exception {

    Statement stmt = null;
    ResultSet rs = null;
    YHCensorWords censorWords = null;
    List list = new ArrayList();
    ArrayList<YHCensorWords> wordList = new ArrayList<YHCensorWords>();
    ArrayList<YHCensorWords> result = new ArrayList<YHCensorWords>();
    try {
      stmt = dbConn.createStatement();

      String sql = "select SEQ_ID, USER_ID, FIND, REPLACEMENT from oa_examine_words where USER_ID="
          + userId;
//      if (userId == 1) {
//        sql += " where USER_ID=" + userId;
//      }
      if (!YHUtility.isNullorEmpty(find)) {
        sql += " and FIND like '%" + find + "%'"  + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(replacement)) {
        sql += " and REPLACEMENT like '%" + replacement + "%'" + YHDBUtility.escapeLike();
      }
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        censorWords = new YHCensorWords();
        censorWords.setSeqId(rs.getInt("SEQ_ID"));
        censorWords.setUserId(rs.getInt("USER_ID"));
        censorWords.setFind(rs.getString("FIND"));
        censorWords.setReplacement(rs.getString("REPLACEMENT"));

        // 处理content，符合条件的加到wordlist里        wordList.add(censorWords);
      }
      int num = 200;
      if (wordList.size() > num) {
        result.addAll(wordList.subList(0, num));
      } else {
        result = wordList;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return result;
  }

  public void deleteAll(Connection dbConn, String seqId) throws Exception {

    String sql = "DELETE FROM oa_examine_words WHERE SEQ_ID IN(" + seqId + ")";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      //System.out.println(sql);
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  /**
   * 词语过滤查询中的删除
   * @param dbConn
   * @param userId
   * @param find
   * @param replacement
   * @throws Exception
   */
  public void deleteSearch(Connection dbConn, int userId, String find,
      String replacement) throws Exception {
    String LOGIN_USER_PRIV = "1";
    String sql = "DELETE FROM oa_examine_words WHERE 1=1";
    if (LOGIN_USER_PRIV != "1") {
      sql += " and USER_ID=" + userId;
    }
    if (find != "") {
      sql += " and FIND like '%" + find + "%'" + YHDBUtility.escapeLike();
    }
    if (replacement != "") {
      sql += " and REPLACEMENT like '%" + replacement + "%'" + YHDBUtility.escapeLike();
    }
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  public void updateSingleWords(Connection dbConn, String find,
      String replacement) throws Exception {

    String sql = "update oa_examine_words set REPLACEMENT='" + replacement
        + "' WHERE FIND='" + find + "'";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  public void deleteAllFast(Connection dbConn, int userId) throws Exception {
    String sql = "DELETE FROM oa_examine_words WHERE USER_ID=" + userId;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  public void deleteAllWords(Connection dbConn) throws Exception {
    String sql = "DELETE FROM oa_examine_words";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  public ArrayList<YHCensorWords> getCensorWordsTxtList(Connection dbConn,
      int userId, String find, String replacement) throws Exception {

    Statement stmt = null;
    ResultSet rs = null;
    YHCensorWords censorWords = null;
    List list = new ArrayList();
    ArrayList<YHCensorWords> wordList = new ArrayList<YHCensorWords>();
    try {
      stmt = dbConn.createStatement();
      String sql = "select SEQ_ID, USER_ID, FIND, REPLACEMENT from oa_examine_words where USER_ID="
          + userId;
      if (!YHUtility.isNullorEmpty(find)) {
        sql += " and FIND like '%" + find + "%'" + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(replacement)) {
        sql += " and REPLACEMENT like '%" + replacement + "%'" + YHDBUtility.escapeLike();
      }
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        censorWords = new YHCensorWords();
        censorWords.setSeqId(rs.getInt("SEQ_ID"));
        censorWords.setUserId(rs.getInt("USER_ID"));
        censorWords.setFind(rs.getString("FIND"));
        censorWords.setReplacement(rs.getString("REPLACEMENT"));

        // 处理content，符合条件的加到wordlist里
        wordList.add(censorWords);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return wordList;
  }
}
