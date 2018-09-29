package yh.subsys.oa.hr.score.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.hr.score.data.YHScoreData;
import yh.subsys.oa.hr.score.data.YHScoreShow;

public class YHScoreShowLogic {
  private static Logger log = Logger.getLogger(YHScoreShowLogic.class);

  /**
   *  查询
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
  */
  public static List<YHScoreShow> selectData2(Connection dbConn,String[] str) throws Exception {
    YHORM orm = new YHORM();
    List<YHScoreShow> dataList = new ArrayList<YHScoreShow>();
    dataList = orm.loadListSingle(dbConn, YHScoreShow.class, str);
    return dataList;
  }
  
  /**
   * 新建
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static int addData2(Connection dbConn,YHScoreShow data) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, data);
    return getMaSeqId(dbConn, "oa_score_show");
  }
  
  public static void updateDate2(Connection dbConn, String seqId, String score, String answer, String memo, String checkEnd) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    if(!YHUtility.isNullorEmpty(memo)){
      memo = memo.replace("'", "''");     
    }else{
      memo = "";
    }
    String sql = "update oa_score_show set SCORE = '" + score + "' ,ANSWER = '" + answer + "' , MEMO = '" + memo + "', CHECK_END = '" + checkEnd + "'  where seq_id = " + seqId ;
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
   }

  public static int getMaSeqId(Connection dbConn,String tableName)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    int maxSeqId = 0;
    String sql = "select max(SEQ_ID) as SEQ_ID from " + tableName;
    try{
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     if(rs.next()){
       maxSeqId = rs.getInt("SEQ_ID");
     }
      
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return maxSeqId;
  }
  
  public static YHPerson showPerson(Connection dbConn,String participant) throws Exception {
    String sql = "select son.SEQ_ID as seqId,son.USER_NAME as userName,dep.DEPT_NAME as deptName "
      + ",priv.PRIV_NAME as privName FROM PERSON son "
      + " left outer join oa_department dep on dep.SEQ_ID = son.DEPT_ID "
      + " left outer join USER_PRIV priv on priv.SEQ_ID = son.USER_PRIV "
      + " WHERE son.SEQ_ID =" + participant;
    YHPerson  person = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        person = new YHPerson();
        person.setSeqId(rs.getInt("seqId"));
        person.setUserName(rs.getString("userName"));
        person.setUserId(rs.getString("deptName"));
        person.setUserPriv(rs.getString("privName"));
      }
    }catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs,log);
    }
    return person;
  }
  
  /**
   * 添加手动填写考核数据
   * @param dbConn
   * @param flowId
   * @param loginUserId
   * @param participant
   * @param score
   * @param memo
   * @throws Exception
   */
  public void addScoreData(Connection dbConn, int groupId, String loginUserId, String participant, String score, String memo, String checkFlag, String year, String month) throws Exception {
//    String sql = "insert into oa_score_data (FLOW_ID,RANKMAN,PARTICIPANT,SCORE,RANK_DATE,MEMO,CHECK_FLAG) values("+flowId+","+loginUserId+",'"+participant+"','"+score+"',?,'"+memo+"','"+checkFlag+"')";
    String ymd = year + "-" + month;
    if(YHUtility.isNullorEmpty(ymd)){
      ymd = "";
    }
    String sql = "insert into oa_score_show (GROUP_ID,RANKMAN,PARTICIPANT,SCORE,RANK_DATE,MEMO,CHECK_FLAG,SCORE_TIME) values(?,?,?,?,?,?,?,?)";
    PreparedStatement stmt = null ; 
    ResultSet rs = null;
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setInt(1, groupId);
      stmt.setString(2, loginUserId);
      stmt.setString(3, participant);
      stmt.setString(4, score);
      stmt.setDate(5, YHUtility.parseSqlDate(YHUtility.getCurDateTimeStr()));
      stmt.setString(6, memo);
      stmt.setString(7, checkFlag);
      stmt.setString(8, ymd);
      stmt.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 修改手动填写考核数据
   * @param dbConn
   * @param flowId
   * @param score
   * @param memo
   * @param participant
   * @param person
   * @throws Exception
   */
  public static void updateScoreDate(Connection dbConn,int groupId, String score, String memo, String participant, YHPerson person, String checkFlag) throws Exception {
    if (YHUtility.isNullorEmpty(memo)) {
      memo = "";
    }
    PreparedStatement stmt = null ; 
    ResultSet rs = null;
//    String sql = "update oa_score_data set SCORE = '" + score + "' ,RANK_DATE = ? , MEMO = '" + memo + "', CHECK_FLAG = '" + checkFlag + "'  where FLOW_ID = " + flowId + " and RANKMAN =" +person.getSeqId()+" and PARTICIPANT = "+participant+"";
    String sql = "update oa_score_show set SCORE = ?, RANK_DATE = ? , MEMO = ?, CHECK_FLAG = ?  where GROUP_ID = ? and RANKMAN = ? and PARTICIPANT = ?";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1, score);
      stmt.setDate(2, YHUtility.parseSqlDate(YHUtility.getCurDateTimeStr()));
      stmt.setString(3, memo);
      stmt.setString(4, checkFlag);
      stmt.setInt(5, groupId);
      stmt.setString(6, String.valueOf(person.getSeqId()));
      stmt.setString(7, participant);
      stmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 获取查看工作日志、工作安排--wyw
   * @param dbConn
   * @param groupId
   * @return
   * @throws Exception
   */
  public String getGroupReferLogic(Connection dbConn,int groupId) throws Exception{
     String sql = "select GROUP_REFER from oa_score_team where SEQ_ID =" + groupId;
     PreparedStatement stmt = null;
     ResultSet rs = null;
     String groupReferStr = "";
     String data = "";
     String returnValue = "";
     boolean diaryFlag = false;
     boolean calendarFlag = false;
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      if(rs.next()){
        groupReferStr = YHUtility.null2Empty(rs.getString("GROUP_REFER"));
      }
      String [] groupReferArry = groupReferStr.split(",");
      if(groupReferArry !=null && groupReferArry.length>0){
        for(String tmp:groupReferArry){
          if ("DIARY".equals(tmp.trim())) {
            diaryFlag = true;
          }
          if ("CALENDAR".equals(tmp.trim())) {
            calendarFlag = true;
          }
        }
      }
      if (diaryFlag && calendarFlag) {
        returnValue = "both";
      }else if (diaryFlag) {
        returnValue = "diary";
      }else if(calendarFlag){
        returnValue = "calendar";
      }
      data = "{groupRefer:\"" +  returnValue  + "\"}";
    } catch (Exception e) {
      throw e;
    }finally{
      YHDBUtility.close(stmt, rs, log);
    }
    return data;
  }
  
  public String getScoreGroupSelect(Connection dbConn, String userIdStr) throws Exception {
    String data = ""; 
    StringBuffer sb = new StringBuffer("["); 
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      boolean isHave = false; 
      String sql = "select SEQ_ID, GROUP_NAME from oa_score_team where "
          + YHDBUtility.findInSet(userIdStr, "USER_PRIV");
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        int flowId = rs.getInt(1);
        String groupName = rs.getString("GROUP_NAME");
        sb.append("{"); 
        sb.append("seqId:\"" + flowId + "\""); 
        sb.append(",text:\"" + YHUtility.encodeSpecial(groupName) + "\""); 
        sb.append("},"); 
        isHave = true; 
      }
      if (isHave) { 
        sb.deleteCharAt(sb.length() - 1); 
        } 
        sb.append("]"); 
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    data = sb.toString(); 
    return data;
  }
  
  /**
   * 获取groupId
   * @param conn
   * @param roleId
   * @return
   * @throws Exception
   */
  public String getGroupId(Connection conn, String roleId) throws Exception {
    String result = "";
    String sql = "select SEQ_ID from oa_score_team where "
      + YHDBUtility.findInSet(roleId, "USER_PRIV");
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 获取是选择考核还是手动填写考核项目 1-选择，0-手动填写
   * @param conn
   * @param groupId
   * @return
   * @throws Exception
   */
  public String getGroupFlag(Connection conn, int groupId) throws Exception {
    String result = "";
    String sql = "select GROUP_FLAG from oa_score_team where SEQ_ID = " + groupId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String groupFlag = rs.getString(1);
        if (groupFlag != null) {
          result = groupFlag;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 判断考核是否结束 1-结束，0-未结束
   * @param conn
   * @param groupId
   * @return
   * @throws Exception
   */
  public String getCheckEnd(Connection conn, int groupId, String userId, String year, String month) throws Exception {
    String result = "0";
    String ymd = year + "-" + month;
    String sql = "select CHECK_END from oa_score_show where GROUP_ID = " + groupId + " and PARTICIPANT ='" + userId + "' and SCORE_TIME = '"+ymd+"'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String checkEnd = rs.getString(1);
        if (checkEnd != null) {
          result = checkEnd;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public String getScoreShowStr(Connection conn, String year, String month,
      String userId) throws Exception {
    String result = "";
    String ymd = year + "-" + month;
    String sql = " select SCORE from oa_score_show where PARTICIPANT='" + userId + "' and SCORE_TIME = '"+ymd+"'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result += toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public double getScoreShow(Connection conn, String year, String month,
      String userId) throws Exception {
    double result = 0;
    String data = getScoreShowStr(conn, year, month, userId);
    try {
      String dataStr[] = data.split(",");
      for (int i = 0; i < dataStr.length; i++) {
        if (!YHUtility.isNullorEmpty(dataStr[i])) {
          double val = Double.parseDouble(dataStr[i]);
          result = result + val;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      // YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 查询是否已录入过数据
   * @param dbConn
   * @param person
   * @param userId
   * @param flowId
   * @return
   * @throws Exception
   */
  public boolean getOperationFlag(Connection dbConn, YHPerson person, String userId, int groupId, String year, String month)
  throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String ymd = year + "-" + month;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) from oa_score_show where GROUP_ID = " + groupId + " and PARTICIPANT = '" + userId+ "' and SCORE_TIME = '" + ymd + "'";
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

  /**
   * 获取考核指标集标题
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String getScoreGroupName(Connection conn, int groupId) throws Exception {
    String result = "";
    String sql = " select GROUP_NAME from oa_score_team where SEQ_ID = " + groupId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
}
