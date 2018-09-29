package yh.subsys.oa.hr.score.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.score.data.YHScoreFlow;


public class YHScoreFlowLogic {
  private static Logger log = Logger.getLogger(YHScoreFlowLogic.class);

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页--lz
   * @return
   * @throws Exception 
   */
  public static String selectFlow(Connection dbConn,Map request) throws Exception {
    String sql = "select ex.SEQ_ID,son.SEQ_ID,son.GROUP_FLAG,ex.FLOW_TITLE,ex.RANKMAN,ex.PARTICIPANT,son.GROUP_NAME,ex.ANONYMITY"
      + ",ex.BEGIN_DATE,ex.END_DATE,ex.FLOW_DESC,ex.FLOW_FLAG,ex.SEND_TIME FROM oa_score_fl ex "
      + " left outer join oa_score_team son on son.seq_id = ex.GROUP_ID ";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页--lz
   * @return
   * @throws Exception 
   */
  public static String selectFlow2(Connection dbConn,Map request,String dayTime,String seqId) throws Exception {
    String sql = "select ex.SEQ_ID as SEQ_ID,son.SEQ_ID as SEQ_IDS,son.GROUP_FLAG,ex.FLOW_TITLE,ex.RANKMAN,ex.PARTICIPANT,son.GROUP_NAME,ex.ANONYMITY"
      + ",ex.BEGIN_DATE,ex.END_DATE,ex.FLOW_DESC,ex.FLOW_FLAG,ex.SEND_TIME FROM oa_score_fl ex "
      + " left outer join oa_score_team son on son.seq_id = ex.GROUP_ID "
      + " WHERE 1=1 ";
    sql += " and " + YHDBUtility.findInSet(seqId,"ex.RANKMAN");
    sql += " and " + YHDBUtility.getDateFilter("ex.BEGIN_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(dayTime)), "<=");
    sql += " and (" + YHDBUtility.getDateFilter("ex.END_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(dayTime)), ">")
    + "or ex.END_DATE is null)  order by ex.SEQ_ID desc ";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public static String selectList(Connection dbConn,Map request,YHScoreFlow flow, String beginDate1,String endDate1,String cd) throws Exception {
    String sql = "select ex.SEQ_ID as SEQ_ID,son.SEQ_ID as SEQ_IDS,ex.FLOW_TITLE,ex.RANKMAN,ex.PARTICIPANT,son.GROUP_NAME,ex.ANONYMITY"
      + ",ex.BEGIN_DATE,ex.END_DATE,ex.FLOW_DESC,ex.FLOW_FLAG,ex.SEND_TIME FROM oa_score_fl ex "
      + " left outer join oa_score_team son on son.seq_id = ex.GROUP_ID "
      + " WHERE 1=1 ";
    if (!YHUtility.isNullorEmpty(flow.getFlowTitle())) {
      sql += " and ex.FLOW_TITLE like '%" + YHDBUtility.escapeLike(flow.getFlowTitle()) + "%' " + YHDBUtility.escapeLike();
    }
    if (flow.getGroupId() > 0) {
      sql += " and ex.GROUP_ID=" + flow.getGroupId();
    }
    if (!YHUtility.isNullorEmpty(flow.getRankman())) {
      sql += " and " + YHDBUtility.findInSet(flow.getRankman(),"ex.RANKMAN");
    }
    if (!YHUtility.isNullorEmpty(flow.getParticipant())) {
      sql += " and " + YHDBUtility.findInSet(flow.getParticipant(),"ex.PARTICIPANT");
    }
    if (flow.getBeginDate() != null) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.BEGIN_DATE", YHUtility.getDateTimeStr(flow.getBeginDate()), ">=");
    }
    if (!YHUtility.isNullorEmpty(beginDate1)) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.BEGIN_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(beginDate1)), "<=");
    }
    if (flow.getEndDate() != null) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.END_DATE", YHUtility.getDateTimeStr(flow.getEndDate()), ">=");
    }
    if (!YHUtility.isNullorEmpty(endDate1)) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.END_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(endDate1)), "<=");
    }
    if (cd.equals("2")) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.END_DATE", YHUtility.getDateTimeStr(new java.util.Date()), "<=") + " and  ex.END_DATE is not null ";
    }
    sql += " order by ex.SEND_TIME desc ";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 根据条件查询数据--lz
   * @return
   * @throws Exception 
   */
  public static List<YHScoreFlow> expList(Connection dbConn,String seqId) throws Exception {
    String sql = "select ex.SEQ_ID,ex.anonymity as anonymity,son.seq_id as groupId,item.item_name as itemName"
      + ",sd.RANKMAN as RANKMAN,sd.PARTICIPANT as PARTICIPANT,sd.score as score, sd.memo as memo FROM oa_score_fl ex"
      + " left outer join oa_score_team son on son.seq_id = ex.GROUP_ID"
      + " left outer join oa_score_item item on item.group_id = son.seq_id"
      + " left outer join oa_score_data sd on sd.flow_id = ex.SEQ_ID"
      + " where ex.seq_id='" + seqId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHScoreFlow> list = new ArrayList<YHScoreFlow>();
    YHScoreFlow flow = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        flow = new YHScoreFlow();
        flow.setRankman(rs.getString("RANKMAN"));
        flow.setAnonymity(rs.getString("anonymity"));
        flow.setParticipant(rs.getString("PARTICIPANT"));
        flow.setFlowTitle(rs.getString("memo"));
        flow.setFlowDesc(rs.getString("itemName"));
        flow.setFlowFlag(rs.getString("score"));
        list.add(flow);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return list;
  }
  
  public static List<YHScoreFlow> expScoreDataList(Connection dbConn,String seqId, String groupId) throws Exception {
    String sql = "select PARTICIPANT, SCORE, MEMO from oa_score_data where FLOW_ID='" + seqId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<YHScoreFlow> list = new ArrayList<YHScoreFlow>();
    YHScoreFlow flow = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        flow = new YHScoreFlow();
        flow.setParticipant(rs.getString(1)); //PARTICIPANT
        flow.setFlowFlag(rs.getString(2));    //SCORE
        flow.setFlowTitle(rs.getString(3));
        list.add(flow);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return list;
  }
  
  public static List<YHScoreFlow> expScoreItemList(Connection dbConn,String seqId, String groupId) throws Exception {
    String sql = "select ITEM_NAME from oa_score_item where GROUP_ID='" + groupId + "'";
    String sql2 = "select ITEM_NAME from oa_score_answer where GROUP_ID='" + groupId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    PreparedStatement ps2 = null;
    ResultSet rs2 = null;
    List<YHScoreFlow> list = new ArrayList<YHScoreFlow>();
    YHScoreFlow flow = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        flow = new YHScoreFlow();
        flow.setFlowDesc(rs.getString(1)); //ITEM_NAME
        list.add(flow);
      }
      ps2 = dbConn.prepareStatement(sql2);
      rs2 = ps2.executeQuery();
      while (rs2.next()) {
        flow = new YHScoreFlow();
        flow.setFlowDesc(rs2.getString(1)); //ITEM_NAME
        list.add(flow);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, rs, log);
      YHDBUtility.close(ps2, rs2, log);
    }
    return list;
  }
  
  /**
   * 导出分数明细
   * @param dbConn
   * @param scoreDataList
   * @param scoreItemlist
   * @return
   * @throws Exception
   */
  public static ArrayList<YHDbRecord> getDbRecord(Connection dbConn,List<YHScoreFlow> scoreDataList, List<YHScoreFlow> scoreItemlist) throws Exception{
    ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
    YHDbRecord dbrec = null;
    YHScoreFlow scoreFlow = new YHScoreFlow();
    YHScoreFlow scoreFlow2 = new YHScoreFlow();
    YHScoreFlow scoreFlow3 = new YHScoreFlow();
    if (scoreItemlist.size() <= 0) {
      dbrec = new YHDbRecord();
      dbrec.addField("部门","");
      dbrec.addField("姓名","");
      dbrec.addField("角色","");
      dbL.add(dbrec);
    } else {
      for (int i = 0; i < scoreDataList.size(); i++) {
        scoreFlow = scoreDataList.get(i);
        dbrec = new YHDbRecord();
        YHPerson person = showPerson(dbConn,scoreFlow.getParticipant());
        dbrec.addField("部门",person.getUserId());
        dbrec.addField("姓名",person.getUserName());
        dbrec.addField("角色",person.getUserPriv());  
        
        scoreFlow2 = scoreDataList.get(i);
        String[] scoreStr = scoreFlow2.getFlowFlag().split(",");
        String[] memoStr = scoreFlow2.getFlowTitle().split(",");
        for(int h = 0; h < scoreItemlist.size(); h++){
          scoreFlow3 = scoreItemlist.get(h);
          String score = "";
          if(h > scoreStr.length-1){
            score = "";
          }else{
            score = scoreStr[h];
          }
          if(h > memoStr.length-1){
            score += "";
          }else{
            score += "(批注:"+memoStr[h]+")";
          }
          dbrec.addField(scoreFlow3.getFlowDesc(),score);  
        }
        dbL.add(dbrec);
      }
    }
    return dbL;
  }
  
  /**
   * 导出总分
   * @param dbConn
   * @param scoreDataList
   * @param scoreItemlist
   * @return
   * @throws Exception
   */
  public static ArrayList<YHDbRecord> getDbRecord2(Connection dbConn,List<YHScoreFlow> scoreDataList, List<YHScoreFlow> scoreItemlist) throws Exception{
    ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
    YHDbRecord dbrec = null;
    YHScoreFlow scoreFlow = new YHScoreFlow();
    YHScoreFlow scoreFlow2 = new YHScoreFlow();
    YHScoreFlow scoreFlow3 = new YHScoreFlow();
    int count = 0;
    if (scoreItemlist.size() <= 0) {
      dbrec = new YHDbRecord();
      dbrec.addField("部门","");
      dbrec.addField("姓名","");
      dbrec.addField("角色","");
      dbL.add(dbrec);
    } else {
      for (int i = 0; i < scoreDataList.size(); i++) {
        scoreFlow = scoreDataList.get(i);
        dbrec = new YHDbRecord();
        YHPerson person = showPerson(dbConn,scoreFlow.getParticipant());
        dbrec.addField("部门",person.getUserId());
        dbrec.addField("姓名",person.getUserName());
        dbrec.addField("角色",person.getUserPriv());  
        
        scoreFlow2 = scoreDataList.get(i);
        String[] scoreStr = scoreFlow2.getFlowFlag().split(",");
        String[] memoStr = scoreFlow2.getFlowTitle().split(",");
        for(int h = 0; h < scoreItemlist.size(); h++){
          scoreFlow3 = scoreItemlist.get(h);
          String score = "";
          if(h > scoreStr.length - 1){
            score = "";
          }else{
            score = scoreStr[h];
            String scoreFlag = "0";
            if(!YHUtility.isNullorEmpty(scoreStr[h])){
              scoreFlag = scoreStr[h];
            }
            count = count + Integer.parseInt(scoreFlag);
          }
          dbrec.addField(scoreFlow3.getFlowDesc(),score);  
        }
        dbrec.addField("总分",count);  
        count = 0;
        dbL.add(dbrec);
      }
    }
    return dbL;
  }
  
  public List getItemName(Connection dbConn, String groupId) throws Exception {
    List list = new ArrayList();
    Statement stmt = null;
    ResultSet rs = null;
    list.add("部门");
    list.add("名称");
    list.add("角色");
    String sql = "SELECT ITEM_NAME from oa_score_item where GROUP_ID= '" + groupId + "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String itemName = rs.getString(1);
        list.add(itemName);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }

  /**
   * 人员查询
   * 
   * @return
   * @throws Exception
   */
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
   * 单个查询byId
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHScoreFlow getFlowById(Connection dbConn,String seqId)throws Exception {
    YHORM orm = new YHORM();
    YHScoreFlow flow = (YHScoreFlow) orm.loadObjSingle(dbConn, YHScoreFlow.class, Integer.parseInt(seqId));
    return flow;
  }

  public void addScoreFlow(Connection dbConn, YHScoreFlow scoreFlow) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, scoreFlow);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }

  /**
   * 删除考核任务一条记录--cc
   * @param conn
   * @param seqId
   * @throws Exception
   */
  public void deleteSingle(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.deleteSingle(conn, YHScoreFlow.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  public void deleteSingleScoreData(Connection conn, String flowId) throws Exception {
    String sql = "DELETE FROM oa_score_data WHERE FLOW_ID = '" + flowId + "'";
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
  
  public void deleteSingleScoreFlow(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.deleteSingle(conn, YHScoreFlow.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }

  public YHScoreFlow getScoreFlowDetail(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHScoreFlow) orm.loadObjSingle(conn, YHScoreFlow.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }

  public void updateScoreFlow(Connection conn, YHScoreFlow scoreFlow) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.updateSingle(conn, scoreFlow);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }

  /**
   * 立即生效,立即终止,恢复终止
   * @return
   * @throws Exception
   */
  public static void updateBeginDate(Connection dbConn,int seqId,String tdName,Date beginDate) throws Exception {
    String sql = "update oa_score_fl set " + tdName + "=? where SEQ_ID=?";
    PreparedStatement ps = null;
    try{
      ps = dbConn.prepareStatement(sql);
      ps.setDate(1,beginDate);
      ps.setInt(2,seqId);
      ps.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      //YHDBUtility.close(ps,null, log);
    }
  }

  public String getGroupDescLogic(Connection conn, int seqId) throws Exception {
    String result = "";
    String sql = " select GROUP_DESC from oa_score_team where SEQ_ID = " + seqId;
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

  public String getCheckFlagLogic(Connection conn, int seqId) throws Exception {
    String result = "";
    String sql = " select CHECK_FLAG from oa_score_fl where SEQ_ID = " + seqId;
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
   * 获取考核任务标题
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String getFlowTitleName(Connection conn, int seqId) throws Exception {
    String result = "";
    String sql = " select FLOW_TITLE from oa_score_fl where SEQ_ID = " + seqId;
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
   * 获取设定考核依据模块工作日志和日程
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String getGroupRefer(Connection conn, int seqId) throws Exception {
    String result = "";
    String sql = " select GROUP_REFER from oa_score_team where SEQ_ID = " + seqId;
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
  
  public static List<YHPerson> showMan(Connection dbConn,String participant) throws Exception {
    String sql = "select son.SEQ_ID as seqId,son.USER_NAME as userName,dep.DEPT_NAME as deptName "
      + ",priv.PRIV_NAME as privName FROM PERSON son "
      + " left outer join oa_department dep on dep.SEQ_ID = son.DEPT_ID "
      + " left outer join USER_PRIV priv on priv.SEQ_ID = son.USER_PRIV "
      + " WHERE son.SEQ_ID in (" + participant +")";
    List<YHPerson> list = new ArrayList<YHPerson>();
    YHPerson  person = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        person = new YHPerson();
        person.setUserName(rs.getString("userName"));
        person.setUserId(rs.getString("deptName"));
        person.setUserPriv(rs.getString("privName"));
        person.setSeqId(rs.getInt("seqId"));
        list.add(person);
      }
    }catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs,log);
    }
    return list;
  }
  
  /**
   * 判断打分状态是否完成
   * @param dbConn
   * @param userId
   * @param flowId
   * @return
   * @throws Exception
   */
  public boolean getFinishFlag(Connection dbConn, String userId, int flowId)
  throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String[] participantCountStr = getParticipantCount(dbConn, flowId).split(",");
    long participantCount = participantCountStr.length;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) from oa_score_data where FLOW_ID = " + flowId + " and RANKMAN = '" + userId + "'";
      rs = stmt.executeQuery(sql);
      long count = 0;
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count == participantCount) {
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
   * 判断考核指标集是否已经被应用
   * @param dbConn
   * @param groupId
   * @return
   * @throws Exception
   */
  public boolean getScoreFlowFlag(Connection dbConn, String groupId)
  throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) from oa_score_fl where GROUP_ID = " + groupId ;
      rs = stmt.executeQuery(sql);
      long count = 0;
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count > 0) {
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
  
  public String getParticipantCount(Connection conn, int seqId) throws Exception {
    String result = "";
    String sql = " select PARTICIPANT from oa_score_fl where SEQ_ID = " + seqId;
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
