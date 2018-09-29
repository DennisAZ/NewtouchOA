package yh.core.funcs.search.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHDsType;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.workflow.util.YHFlowWorkAdSearchLogic;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

/**
 * 
 * @author Think
 *
 */
public class YHFrameSearchLogic {
  private static Logger log = Logger.getLogger(YHFrameSearchLogic.class);
  /**
   * 得到用户的信息
   * @param conn
   * @param keyWord
   * @return
   * @throws Exception
   */
  public String getUserInfo(Connection conn,String keyWord,int pageStart,int pageNum) throws Exception{
    keyWord = YHDBUtility.escapeLike(keyWord);
    StringBuffer result = new StringBuffer();
    String sql = "SELECT " +
        "PERSON.SEQ_ID," +
    		"BP_NO," +
    		"SEX," +
    		"USER_ID," +
    		"USER_NAME," +
    		"oa_department.DEPT_NAME," +
    		"BIRTHDAY," +
    		"PERSON.USER_PRIV," +
    		"PRIV_NAME," +
    		"TEL_NO_DEPT," +
    		"TEL_NO_HOME," +
    		"EMAIL," +
    		"OICQ," +
    		"MOBIL_NO," +
    		"MOBIL_NO_HIDDEN," +
    		"REMARK " +
    		"from PERSON,USER_PRIV,oa_department where DEPT_ID!=0 AND oa_department.SEQ_ID = PERSON.DEPT_ID and (PERSON.USER_ID like '%" + keyWord + "%' or PERSON.BYNAME like '%" + keyWord + "%' or PERSON.USER_NAME like '%" + keyWord + "%' or (MOBIL_NO like '%" + keyWord + "%' and MOBIL_NO_HIDDEN='0') or TEL_NO_DEPT like '%" + keyWord + "%' or TEL_NO_HOME like '%" + keyWord + "%' or REMARK like '%" + keyWord + "%')  and  PERSON.USER_PRIV=USER_PRIV.SEQ_ID order by PRIV_NO,USER_NO,USER_NAME ";
    String[] names = {"seqId","bpNo","sex","userId","userName","deptId","birthday"
        ,"userPriv","privName","telNoDept","telNoHome","email","oicq","mobilNo","mobilNoHidden","remark"};
    result = praserData(conn, sql, names, "user", pageStart, pageNum);
    return result.toString();
  }
  /**
   * 
   * @param conn
   * @param keyWord
   * @param pageStart
   * @param pageNum
   * @return
   * @throws Exception
   */
  public String getEmailInfo(Connection conn,String keyWord,int pageStart,int pageNum,int userId) throws Exception{
    keyWord = YHDBUtility.escapeLike(keyWord);
    StringBuffer result = new StringBuffer();
    String sql = "SELECT" +
    		" oa_email_body.SUBJECT" +
    		",oa_email.SEQ_ID" +
    		",oa_email.BODY_ID" +
    		",oa_email.BOX_ID" +
    		",oa_email_body.SEND_TIME" +
    		",oa_email_body.CONTENT " +
    		" from " +
    		"oa_email" +
    		",oa_email_body  " +
    		"where (SUBJECT like '%" + keyWord + "%' "  + YHDBUtility.escapeLike() +  "  or CONTENT like '%" + keyWord + "%'"  + YHDBUtility.escapeLike() +  " ) and  oa_email.BODY_ID=oa_email_body.SEQ_ID and oa_email.TO_ID='" + userId + "' and (DELETE_FLAG='' or  DELETE_FLAG='0' or DELETE_FLAG='2') order by SEND_TIME desc ";
    String[] names = {"subject","emailId","bodyId","boxId","sendTime","content"};
    result = praserData(conn, sql, names, "email", pageStart, pageNum);
    return result.toString();
  }
  /**
   * 搜索公告通知
   * @param conn
   * @param keyWord
   * @param pageStart
   * @param pageNum
   * @param userId
   * @return
   * @throws Exception
   */
  public String getNotifyInfo(Connection conn,String keyWord,int pageStart,int pageNum,YHPerson loginUser) throws Exception{
    keyWord = YHDBUtility.escapeLike(keyWord);
    StringBuffer result = new StringBuffer();
    String sql = "SELECT" +
    		" NOTIFY.SEQ_ID" +
    		",SUBJECT" +
    		",CONTENT" +
    		",PERSON.USER_NAME" +
    		",TYPE_ID" +
    		",BEGIN_DATE" +
    		",FORMAT" +
    		" from " +
    		"oa_notify as NOTIFY " +
        ",PERSON " +
    		"where " +
    		" PERSON.SEQ_ID = NOTIFY.FROM_ID AND (" + YHDBUtility.findInSet("ALL_DEPT", "TO_ID") + "  or " + YHDBUtility.findInSet(String.valueOf(0), "TO_ID") + "  or " + YHDBUtility.findInSet(String.valueOf(loginUser.getDeptId()), "TO_ID") + " or " + YHDBUtility.findInSet(loginUser.getUserPriv(), "PRIV_ID") + " or " + YHDBUtility.findInSet(String.valueOf(loginUser.getSeqId()), "NOTIFY.USER_ID") + ") and " + YHDBUtility .getDateFilter("BEGIN_DATE", YHUtility.getCurDateTimeStr(), "<=")+ " and (" + YHDBUtility .getDateFilter("END_DATE", YHUtility.getCurDateTimeStr(), ">=")+ " or END_DATE is null) and PUBLISH='1' and SUBJECT like '%" + keyWord + "%' "  + YHDBUtility.escapeLike() +  "  order by BEGIN_DATE desc ";
    String[] names = {"notifyId","subject","content","fromId","typeId","beginDate","format"};
    result = praserData(conn, sql, names, "notify", pageStart, pageNum);
    return result.toString();
  }
  /**
   * 查询通讯簿
   * @param conn
   * @param keyWord
   * @param pageStart
   * @param pageNum
   * @param loginUser
   * @return
   * @throws Exception
   */
  public String getAddressInfo(Connection conn,String keyWord,int pageStart,int pageNum,YHPerson loginUser) throws Exception{
    keyWord = YHDBUtility.escapeLike(keyWord);
    StringBuffer result = new StringBuffer();
    String sql = toAddSqlStr(conn, loginUser, keyWord);
    String[] names = {"addId","userName","addDept","psName","sex","telNoDept","deptName","telNoHome","mobilNo","email","nickName"};
    result = praserData(conn, sql, names, "address", pageStart, pageNum);
    return result.toString();
  }
  /**
   * 
   * @param conn
   * @param loginUser
   * @param keyWord
   * @return
   * @throws Exception
   */
  private String toAddSqlStr(Connection conn,YHPerson loginUser,String keyWord) throws Exception{
    String sql = "SELECT " +
    "ADDRESS.SEQ_ID" +
    ",ADDRESS.USER_ID" +
    ",ADDRESS.ADD_DEPT" +
    ",ADDRESS.PSN_NAME" +
    ",ADDRESS.SEX" +
    ",ADDRESS.TEL_NO_DEPT" +
    ",ADDRESS.DEPT_NAME" +
    ",ADDRESS.TEL_NO_HOME" +
    ",ADDRESS.MOBIL_NO" +
    ",ADDRESS.EMAIL" +
    ",ADDRESS.NICK_NAME ";
    YHUserPriv userPriv = getUserRole(conn, Integer.valueOf(loginUser.getUserPriv()));
    boolean hasSelfRole = false;
    boolean hasComfRole = false;
    hasSelfRole = findId(userPriv.getFuncIdStr(), 232);
    hasComfRole = findId(userPriv.getFuncIdStr(), 557);
    if(hasSelfRole && hasComfRole){//有个人通讯簿模块权限
      sql += " from " +
        "oa_address as ADDRESS " +
        ",oa_address_team " +
        " where ((ADDRESS.USER_ID='" + loginUser.getSeqId() + "'  and oa_address_team.USER_ID=ADDRESS.USER_ID  and oa_address_team.SEQ_ID=ADDRESS.GROUP_ID)or( (ADDRESS.USER_ID='' or ADDRESS.USER_ID is null ) and oa_address_team.SEQ_ID=ADDRESS.GROUP_ID and(" + YHDBUtility.findInSet("ALL_DEPT", "PRIV_DEPT") + " or " + YHDBUtility.findInSet(String.valueOf(loginUser.getDeptId()), "PRIV_DEPT") + " or " + YHDBUtility.findInSet(loginUser.getUserPriv(), "PRIV_ROLE") + ")))";
    }else if(hasSelfRole){
      sql += " from " +
        "oa_address as ADDRESS " +
        " where  ADDRESS.USER_ID='" + loginUser.getSeqId() + "' and oa_address_team.USER_ID=ADDRESS.USER_ID  and oa_address_team.SEQ_ID=ADDRESS.GROUP_ID";
    }else if(hasComfRole){//有公共通讯簿权限
      sql += " from " +
        "oa_address as ADDRESS " +
        ",oa_address_team " +
        " where (ADDRESS.USER_ID='' or ADDRESS.USER_ID is null ) and oa_address_team.SEQ_ID=ADDRESS.GROUP_ID and(" + YHDBUtility.findInSet("ALL_DEPT", "PRIV_DEPT") + " or " + YHDBUtility.findInSet(String.valueOf(loginUser.getDeptId()), "PRIV_DEPT") + " or " + YHDBUtility.findInSet(loginUser.getUserPriv(), "PRIV_ROLE") + ")";
    }else{
      sql += " from " +
      "oa_address as ADDRESS " +
      " where  1=2";
    }
    sql += " and PSN_NAME like '%" + keyWord + "%'"  + YHDBUtility.escapeLike() ;
    return sql;
  }
  /**
   * 
   * @param conn
   * @param keyWord
   * @param pageStart
   * @param pageNum
   * @param loginUser
   * @return
   * @throws Exception
   */
  public String getFileFloderInfo(Connection conn,String keyWord,int pageStart,int pageNum,YHPerson loginUser) throws Exception{
    keyWord = YHDBUtility.escapeLike(keyWord);
    StringBuffer result = new StringBuffer();
    ArrayList<Integer> seqIdArray = getFileFloderSeqIds(conn, loginUser, keyWord);
    int recordCnt = -1;
    ArrayList<Integer> temp = new ArrayList<Integer>();
    if(seqIdArray.size() >= 1000){
      recordCnt = seqIdArray.size();
      int pageCnt = recordCnt / pageNum;
      if (recordCnt % pageNum != 0) {
        pageCnt++;
      }
      if (pageStart < 0) {
        pageStart = 0;
      }
      if (pageStart > pageCnt - 1) {
        pageStart = pageCnt - 1;
      }
      int toIndex = (pageStart * pageNum + pageNum) > recordCnt ? recordCnt : (pageStart * pageNum + pageNum);
      temp.addAll(seqIdArray.subList(pageStart * pageNum + 1, toIndex));
    }else {
      temp = seqIdArray;
    }
    String seqIds = "";
    for (int i = 0; i < temp.size(); i++) {
      if(!"".equals(seqIds)){
        seqIds += ",";
      }
      seqIds += temp.get(i);
    }
    if("".equals(seqIds)){
      seqIds += "-1";
    }
    String sql = toFileFloderSqlStr(conn, loginUser, keyWord,seqIds);
    String[] names = {"sortId","contentId","subject","userId","content","sendTime","attachmentName"};
    result = praserData(conn, sql, names, "fileFloder", 0, pageNum,recordCnt);
    return result.toString();
  }
  /**
   * 
   * @param conn
   * @param loginUser
   * @param keyWord
   * @param seqIds
   * @return
   * @throws Exception
   */
  private String toFileFloderSqlStr(Connection conn, YHPerson loginUser,String keyWord,String seqIds) throws Exception{
    String result = "SELECT " +
    		"SORT_ID" +
    		",SEQ_ID" +
    		",SUBJECT" +
    		",USER_ID" +
    		",CONTENT" +
    		",SEND_TIME" +
    		",ATTACHMENT_NAME " +
    		" from " +
    		" oa_file_content " +
    		" where " + 
    		" SEQ_ID IN(" + seqIds + ")";
    return result;
  }
  /**
   * 
   * @param conn
   * @param loginUser
   * @param keyWord
   * @return
   * @throws Exception
   */
  private ArrayList<Integer> getFileFloderSeqIds(Connection conn, YHPerson loginUser,String keyWord) throws Exception{
    ArrayList<Integer> result = new ArrayList<Integer>();
    String sql = "SELECT " +
    		" SORT_ID" +
    		",SEQ_ID" +
    		",USER_ID" +
    		",CONTENT" +
    		",SEND_TIME" +
    		",ATTACHMENT_NAME " +
    		" from oa_file_content where (SUBJECT like '%" + keyWord + "%') order by SEND_TIME desc";
    YHUserPriv userPriv = getUserRole(conn, Integer.valueOf(loginUser.getUserPriv()));
    boolean hasSelfRole = false;
    boolean hasComfRole = false;
    hasSelfRole = findId(userPriv.getFuncIdStr(), 250);//个人文件柜    hasComfRole = findId(userPriv.getFuncIdStr(), 916);//公共文件柜    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while( rs.next() ){
        int seqId = rs.getInt(2);
        int sortId = rs.getInt(1);
        String userId = rs.getString(3);
        if(userId != null && !"".equals(userId)){//个人文件柜          if(!hasSelfRole){
            continue;
          }
          if(!userId.equals(String.valueOf(loginUser.getSeqId()))){
            continue;
          }
        }else {
          if(!isComRole(conn, sortId, loginUser, hasSelfRole, hasComfRole)){
            continue;
          }
        }
        result.add(seqId);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  /**
   * 
   * @param conn
   * @param sortId
   * @param loginUser
   * @param hasSelfRole
   * @param hasComfRole
   * @return
   * @throws Exception
   */
  private boolean isComRole(Connection conn,int sortId,YHPerson loginUser,boolean hasSelfRole,boolean hasComfRole) throws Exception{
    String sql = "SELECT USER_ID,SORT_PARENT,SHARE_USER from oa_file_sort where SEQ_ID=" + sortId;
    boolean result = false;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if( rs.next()){
        String userId = rs.getString(1);
        int sortParent = rs.getInt(2);
        String shareUser = rs.getString(3);
        String shareUser1 = shareUserStr(conn, sortParent) + "," + shareUser;
        if("".equals(shareUser1)) {
          if(hasSelfRole && findId(shareUser1, loginUser.getSeqId())){
            result = true;
          }
        } else {
          String deptRole = "";
          String userPriv = "";
          String userRole = "";
          int index = userId.indexOf("|");
          if( index >= 0){
            String[] ids = userId.split("\\|");
            if (ids.length > 0) {
              deptRole = ids[0];
              if (ids.length > 1) {
                userPriv = ids[1];
                if (ids.length > 2) {
                  userRole = ids[2];
                }
              }
            }
//            int start = 0;
//            int end = 0;
//            end = userId.indexOf("|", 0);
//            deptRole = userId.substring(start,end);
//            start = end + 1;
//            end = userId.indexOf("|", start);
//            userPriv = userId.substring(start,end);
//            start = end +1;
//            end = userId.indexOf("|", start);
//            try{
//              userRole =  userId.substring(start,end);
//            }catch(Exception e){
//              userRole = "";
//            }
          } 
          if(hasComfRole && (userId.equals(loginUser.getSeqId()) 
              || findId(deptRole, loginUser.getDeptId()) 
              || findId(userPriv, Integer.valueOf(loginUser.getUserPriv()))
              || findId(userRole, loginUser.getSeqId()))){
            result = true;
          }
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
  * 取得共享用户的用户Id
  * @param conn
  * @param sortId
  * @return
  * @throws Exception
  */
  private String shareUserStr(Connection conn,int sortId) throws Exception{
    String sql = "SELECT SHARE_USER,SORT_PARENT from oa_file_sort where SEQ_ID=" + sortId;
    String result = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    int sortParent  = 0;
    String shareUser = "";
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if ( rs.next() ) {
        shareUser = rs.getString(1);
        sortParent = rs.getInt(2);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    if(sortParent == 0){
      result =  shareUser;
    }else{
      result = shareUser + "," + shareUserStr(conn, sortParent);
    }
    return result;
  }
  /**
   * 
   * @param conn
   * @param keyWord
   * @param pageStart
   * @param pageNum
   * @param loginUser
   * @return
   * @throws Exception
   */
  public String getWorkFlowInfo(Connection conn,String keyWord,int pageStart,int pageNum,YHPerson loginUser) throws Exception{
    StringBuffer result = new StringBuffer();
    YHFlowWorkAdSearchLogic fwsl = new YHFlowWorkAdSearchLogic();
    String myRunId = fwsl.getMyFlowRun(conn,loginUser.getSeqId());
    String myDeptstr = fwsl.getMyDept(conn,loginUser.getDeptId());

    String queryStr = "";
    if("".equals(myRunId)){
      myRunId = "0";
    }
    if(!loginUser.isAdmin() && !loginUser.isAdminRole()){
      queryStr = " and (FLOW_RUN.RUN_ID in (" + myRunId + ") or ((" +
      		"FLOW_TYPE.MANAGE_USER like '%" + loginUser.getSeqId()+ "%' or FLOW_TYPE.QUERY_USER like '%" + loginUser.getSeqId()+ "%'" +
      				" or FLOW_TYPE.MANAGE_USER like '%|0|%'  or FLOW_TYPE.MANAGE_USER like '%|ALL_DEPT|%'  or  FLOW_TYPE.MANAGE_USER like '%" + loginUser.getDeptId()+ "%' or FLOW_TYPE.QUERY_USER like '%" + loginUser.getDeptId() + "%' or FLOW_TYPE.QUERY_USER like '%|0|%' or FLOW_TYPE.QUERY_USER like '%|ALL_DEPT|%'" +
              " or FLOW_TYPE.MANAGE_USER like '%" + loginUser.getUserPriv()+ "%' or FLOW_TYPE.QUERY_USER like '%" +loginUser.getUserPriv() + "%'" +
      				") and ( " + fwsl.getMyManageSql(myDeptstr, myRunId, loginUser.getUserPriv(), loginUser.getSeqId(), String.valueOf(loginUser.getDeptId())) + ")))";
    }
    String sql = "";
    String query = "";
    try {
      sql = "select" 
        + " FLOW_RUN.RUN_ID " 
        + " ,RUN_NAME " 
        + " ,BEGIN_TIME " 
        + " ,END_TIME "
        + " ,ATTACHMENT_ID " 
        + " ,ATTACHMENT_NAME "
        + " ,FLOW_TYPE.SEQ_ID "
        + " ,LIST_FLDS_STR " 
        + " ,FLOW_NAME " 
        + " ,FREE_OTHER "
        + " ,FLOW_TYPE " 
        + " from oa_fl_type as FLOW_TYPE,oa_fl_run as FLOW_RUN,PERSON " 
        + " WHERE  " 
        + " FLOW_RUN.BEGIN_USER=PERSON.SEQ_ID  " 
        + " and FLOW_TYPE.SEQ_ID=FLOW_RUN.FLOW_ID  "
        + " and FLOW_RUN.DEL_FLAG=0 " 
        + " and ( FLOW_RUN.RUN_NAME like '%" + keyWord + "%') " ;
      if(!"".equals(queryStr)){
        sql += queryStr;
      }
      query = " order by FLOW_RUN.RUN_ID desc"; // 按照流水号（实例ID）倒排序
      sql += query;
      String names = "runId,runName,beginTime,endTime,attachmentId,attachmentName,flowTypeId,listFldsStr,flowName,flowType";
      YHPageQueryParam queryParam = new YHPageQueryParam();
      queryParam.setNameStr(names);
      queryParam.setPageIndex(pageStart);
      queryParam.setPageSize(pageNum);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
        YHDbRecord record = pageDataList.getRecord(i);
//        String runName = (String)record.getValueByName("runName");
//        runName = runName.replaceAll("\\\\", "\\\\\\\\");
//        runName = runName.replaceAll("\"", "\\\\\"");
//        record.updateField("runName", runName);
        fwsl.getRunPrcs(record, conn,  loginUser.getSeqId());
        fwsl.getCommentPriv(record, conn, loginUser);
        fwsl.getEditPriv(record, conn, loginUser);
        fwsl.getFocusPriv(record, conn, loginUser);
        fwsl.getAttach(record, conn, loginUser);
      }
      result.append(pageDataList.toJson());
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return result.toString();
  }
  /**
   * 取得当前用户的权限
   * @param conn
   * @param userPriv
   * @return
   * @throws Exception
   */
  private YHUserPriv getUserRole(Connection conn,int userPriv) throws Exception{
    YHUserPriv result = null;
    YHORM orm = new YHORM();
    result = (YHUserPriv) orm.loadObjSingle(conn, YHUserPriv.class, userPriv);
    return result;
  }
  /**
   * 从指定的数字串[12,23,34,45...] 找出指定的数字
   * @param statement
   * @param userId
   * @return
   */
  public boolean findId(String statement,int ss){
    boolean result = false;
    String[] ids = statement.split(",");
    for (String id : ids) {
      if("".equals(id.trim())){
        continue;
      }
      int comId = Integer.parseInt(id.trim());
      if (comId == ss) {
        return true;
      }
    }
    return result;
  }
  /**
   * 组装成json数据
   * @param conn
   * @param sql
   * @param names
   * @param pageStart
   * @param pageNum
   * @return
   * @throws Exception
   */
  private StringBuffer praserData(Connection conn,String sql,String[] names,String tyInfo,int pageIndex,int pageSize,int recordCnt2) throws Exception{
    StringBuffer result = new StringBuffer();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs  = ps.executeQuery();
      rs.last();
      int recordCnt = rs.getRow();
      if(recordCnt2 <= 0){
        recordCnt2 = recordCnt;
      }
      result.append("{recordTotal:").append(recordCnt).append(",")
        .append("typeInfo:\"").append(tyInfo).append("\",records:[");
      if (recordCnt == 0) {
        return result.append("]}");
      }
      int pageCnt = recordCnt / pageSize;
      if (recordCnt % pageSize != 0) {
        pageCnt++;
      }
      if (pageIndex < 0) {
        pageIndex = 0;
      }
      if (pageIndex > pageCnt - 1) {
        pageIndex = pageCnt - 1;
      }
      rs.absolute(pageIndex * pageSize + 1);
      int fieldCnt = names.length;
      
      ResultSetMetaData meta = rs.getMetaData();
      int[] typeArray = new int[fieldCnt];
      for (int i = 0; i < fieldCnt; i++) {
        typeArray[i] = meta.getColumnType(i + 1);
      }
      //记录取出记录的条数
      StringBuffer record = new StringBuffer();

      for (int i = 0; i < pageSize && !rs.isAfterLast(); i++) {
        StringBuffer normalField = new StringBuffer();
        for (int j = 0; j < fieldCnt; j++) {
          String name = names[j];
          String value = "";
          int typeInt = typeArray[j];
          if (YHDsType.isDecimalType(typeInt)) {
            value = String.valueOf(rs.getDouble(j + 1));
          }else if (YHDsType.isIntType(typeInt)) {
            value = String.valueOf(rs.getInt(j + 1));
          }else if (YHDsType.isLongType(typeInt)) {
            value = String.valueOf(rs.getLong(j + 1));
          }else if (YHDsType.isDateType(typeInt)) {
            try{
              value = YHUtility.getDateTimeStr(rs.getTimestamp(j + 1));
            }catch(Exception e){
              
            }
          }else {
            value = rs.getString(j + 1);
          }
          if(value == null ){
            value = "";
          }
          if(!"".equals(normalField.toString())){
              normalField.append(",");
            }
            normalField.append(name).append(":\"").append(YHUtility.encodeSpecial(value.trim())).append("\"");
          }
        if(!"".equals(record.toString())){
          record.append(",");
        }
        record.append("{").append(normalField).append("}");
        rs.next();
      }
      result.append(record).append("]}");
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  /**
   * 组装数据
   * @param conn
   * @param sql
   * @param names
   * @param tyInfo
   * @param pageIndex
   * @param pageSize
   * @return
   * @throws Exception
   */
  private StringBuffer praserData(Connection conn,String sql,String[] names,String tyInfo,int pageIndex,int pageSize) throws Exception{
    return praserData(conn, sql, names, tyInfo, pageIndex, pageSize,-1);
  }

}
