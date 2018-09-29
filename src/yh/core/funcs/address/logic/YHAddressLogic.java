package yh.core.funcs.address.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

//import sun.io.CharToByteConverter;
import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.address.data.YHAddress;
import yh.core.funcs.address.data.YHAddressGroup;
import yh.core.funcs.address.data.YHAddressMb;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.picture.data.YHPicture;
import yh.core.funcs.system.censorcheck.data.YHCensorCheck;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;

import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHAddressLogic {
  private static Logger log = Logger.getLogger(YHAddressLogic.class);

  /**
   * 将中文字符串转换成Ascii(不能用)
   * @param s
   * @return
   */
  public static String ChineseStringToAscii(String s) {
    //if (s == null)
      return s;
    /*try {
      //CharToByteConverter toByte = CharToByteConverter.getConverter("gb2312");
      byte[] orig = toByte.convertAll(s.toCharArray());
      char[] dest = new char[orig.length];
      for (int i = 0; i < orig.length; i++)
        dest[i] = (char) (orig[i] & 0xFF);
      return new String(dest);
    } catch (Exception e) {
      //System.out.println(e);
      return s;
    }*/
  }
  
  /**
   * 按条件查出数据，把对应的数据组装成字符串，中文，英文，其它
   */
  
  public String getAddressMb(Connection dbConn, int loginSeqId)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String psnName = "";
    String firstName = "";
    String capital = "";
    String capitalStr = "";
    String firstBigLetter = "";
    String leSeqId = "";
    String mbStrs = "";
    String leNameSeqId = "";
    String otherSeqId = "";
    String sbToString = "";
    Map m = null;
    int count = 0;
    YHAddressMb amb = new YHAddressMb();
    try {
      String queryStr = "select SEQ_ID, PSN_NAME, GROUP_ID from oa_address where USER_ID = '" + loginSeqId + "' order by PSN_NAME";
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {
        psnName = rs.getString("PSN_NAME");
        int seqId = rs.getInt("SEQ_ID");
        m = amb.getMap();
        Iterator it = m.entrySet().iterator();
        firstName = psnName.substring(0, 1);
        char[]chars = firstName.toCharArray();                               //把字符中转换为字符数组 
        for(int i = 0; i < chars.length; i++){                                   //输出结果
          //System.out.println(" "+chars[i]+" "+(int)chars[i]);
          if((int)chars[i] >= 128){                                       //姓名第一个字如果是中文
            while (it.hasNext()) {
              Map.Entry pairs = (Map.Entry) it.next();
              if (String.valueOf(pairs.getKey()).indexOf(firstName) != -1) {
                capital = String.valueOf(pairs.getValue());
                capitalStr = capital.toUpperCase();  
                //找到姓氏对应的英文字母（转大写），作为索引值                //System.out.println(capitalStr+"IUUI");                     //大写字母（索引）
                break;
              }
            } 
            leNameSeqId += capitalStr + firstName + seqId + ",";
          }else if((int)(firstName.toUpperCase()).charAt(0) >= 65 && (int)(firstName.toUpperCase()).charAt(0) <= 90){                                                         //如果是姓名第一个字节是英文字母
            firstBigLetter = firstName.toUpperCase();
            if((int)(firstBigLetter).charAt(0) >= 65 && (int)(firstBigLetter).charAt(0) <= 90){  //A-65   Z-90
              leSeqId += firstBigLetter + "+" + seqId + ",";
            }
          }else{                                                           //其它（不是中文和英文）            Set<String> keySet = m.keySet();
            boolean name = false;
            for (String mapStr : keySet) {
              name = mapStr.contains(psnName);
            } 
            if(!name){
              otherSeqId += psnName + "+" + seqId + ",";
            }
          }
        }
      }
      mbStrs = leNameSeqId + leSeqId + otherSeqId;
      if(!YHUtility.isNullorEmpty(mbStrs)){
        sbToString = getResultMb(mbStrs, leNameSeqId, leSeqId, otherSeqId);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return sbToString;
  }
  
  /**
   * mysql findInSet 处理
   * @param str
   * @param dbFieldName
   * @return
   * @throws SQLException
   */
  public String findInSet(String str,String dbFieldName) throws SQLException{
    return YHDBUtility.findInSet(str, dbFieldName);
  }
  
  /** 
  * 判段id是不是在str里面 
  * @param str 
  * @param id 
  * @return 
  */ 
  public static boolean findId(String str, String id) {
    if (str == null || id == null || "".equals(str) || "".equals(id)) {
      return false;
    }
    String[] aStr = str.split(",");
    for (String tmp : aStr) {
      if (tmp.equals(id)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * 按条件查出数据，把对应的数据组装成字符串，中文，英文，其它
   */
  
  public String getAddressPublicMb(Connection dbConn, int loginSeqId, String groupIdStr)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String psnName = "";
    String firstName = "";
    String capital = "";
    String capitalStr = "";
    String firstBigLetter = "";
    String leSeqId = "";
    String mbStrs = "";
    String leNameSeqId = "";
    String otherSeqId = "";
    String sbToString = "";
    Map m = null;
    int count = 0;
    YHAddressMb amb = new YHAddressMb();
    try {
      String queryStr = "select SEQ_ID, PSN_NAME, GROUP_ID from oa_address where USER_ID is null order by PSN_NAME";
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while (rs.next()) {
        psnName = rs.getString("PSN_NAME");
        int seqId = rs.getInt("SEQ_ID");
        int groupId = rs.getInt("GROUP_ID");
        if(!findId(groupIdStr, String.valueOf(groupId))){
          continue;
        }
        m = amb.getMap();
        Iterator it = m.entrySet().iterator();
        
        firstName = psnName.substring(0, 1);
        char[]chars = firstName.toCharArray();                               //把字符中转换为字符数组 
        for(int i = 0; i < chars.length; i++){                                   //输出结果
          //System.out.println(" "+chars[i]+" "+(int)chars[i]);
          if((int)chars[i] >= 128){                                       //姓名第一个字如果是中文
            while (it.hasNext()) {
              Map.Entry pairs = (Map.Entry) it.next();
              if (String.valueOf(pairs.getKey()).indexOf(firstName) != -1) {
                capital = String.valueOf(pairs.getValue());
                capitalStr = capital.toUpperCase();  
                //找到姓氏对应的英文字母（转大写），作为索引值                //System.out.println(capitalStr+"IUUI");                     //大写字母（索引）
                break;
              }
            } 
            leNameSeqId += capitalStr + firstName + seqId + ",";
          }else if((int)(firstName.toUpperCase()).charAt(0) >= 65 && (int)(firstName.toUpperCase()).charAt(0) <= 90){                                                         //如果是姓名第一个字节是英文字母
            firstBigLetter = firstName.toUpperCase();
            if((int)(firstBigLetter).charAt(0) >= 65 && (int)(firstBigLetter).charAt(0) <= 90){  //A-65   Z-90
              leSeqId += firstBigLetter + "+" + seqId + ",";
            }
          }else{                                                           //其它（不是中文和英文）
            Set<String> keySet = m.keySet();
            boolean name = false;
            for (String mapStr : keySet) {
              name = mapStr.contains(psnName);
            } 
            if(!name){
              otherSeqId += psnName + "+" + seqId + ",";
            }
          }
        }
      }
      mbStrs = leNameSeqId + leSeqId + otherSeqId;
      if(!YHUtility.isNullorEmpty(mbStrs)){
        sbToString = getResultMb(mbStrs, leNameSeqId, leSeqId, otherSeqId);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return sbToString;
  }
  
  /**
   * 对按姓氏索引的串进行解析 串的形式：中文|英文||其它
   * @param mbStrs  所有的串（中文，英文，其它）
   * @param leNameSeqId    中文
   * @param leSeqId        英文
   * @param otherSeqId     其它
   * @return
   * @throws Exception
   */
  
  public String getResultMb(String mbStrs, String leNameSeqId, String leSeqId, String otherSeqId) throws Exception {
//    System.out.println(mbStrs);
//    String indexStrs = mbStrs.substring(0, mbStrs.indexOf("|"));
//    System.out.println(mbStrs.substring(mbStrs.indexOf("|")+1, mbStrs.indexOf("||"))+"=HIU");
//    String middleStrs = mbStrs.substring(mbStrs.indexOf("|")+1, mbStrs.indexOf("||"));
//    String otherStrs = mbStrs.substring(mbStrs.indexOf("||")+2, mbStrs.length());
//    System.out.println(otherStrs+"=MN");
    String[] indexs = null;
    String[] middleIndex = null;
    String[] othersIndex = null;
    if(!YHUtility.isNullorEmpty(leNameSeqId)){
      indexs = leNameSeqId.split(",");
    }
    if(!YHUtility.isNullorEmpty(leSeqId)){
      middleIndex =  leSeqId.split(",");
    }
    if(!YHUtility.isNullorEmpty(otherSeqId)){
      othersIndex = otherSeqId.split(",");
    }
    String letter[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","W","X","Y","Z"};
    StringBuffer sb = new StringBuffer("[");
    for(int i = 0; i < letter.length; i++){
      String strs = "";
      String seqIdStr = "";
      int count = 0;
      if(!YHUtility.isNullorEmpty(leSeqId)){
        for(int y = 0; y < middleIndex.length; y++){
          if(middleIndex[y].startsWith(letter[i])){
            count ++;
            if(count > 0){
              if(strs.indexOf(middleIndex[y].substring(0,1)) == -1){
                strs += middleIndex[y].substring(0,1) + "," + " ";
              }
              seqIdStr += middleIndex[y].substring(2,middleIndex[y].length()) + ",";
            }
          }
        }
      }
      if(!YHUtility.isNullorEmpty(leNameSeqId)){
        for(int x = 0; x < indexs.length; x++){
          if(indexs[x].startsWith(letter[i])){
            count ++;
            if(count > 0){
              if(strs.indexOf(indexs[x].substring(1,2)) == -1){
                strs += indexs[x].substring(1,2) + "," + " ";
              }
              seqIdStr += indexs[x].substring(2,indexs[x].length()) + "," ;
            }
          }
        }
      }
      if(count > 0){
        String strw = letter[i] + "(" + count + ")" + " " + "－" + " " + strs;
        String seqIds = seqIdStr;
        sb.append("{");
        sb.append("nameStrs:\"" + strw + "\"");
        sb.append(",seqId:\"" + seqIds + "\"");
        sb.append("},");
      }
    }
    String otherStr = "";
    int sum = 0;
    String seqIdOther = "";
    if(!YHUtility.isNullorEmpty(otherSeqId)){
      for(int n = 0; n < othersIndex.length; n++){
        sum++;
        if(otherStr.indexOf(othersIndex[n].substring(0, 1)) == -1){
          otherStr += othersIndex[n].substring(0, 1) + " " + "," + " ";
        }
        seqIdOther += othersIndex[n].substring(othersIndex[n].indexOf("+")+1, othersIndex[n].length()) + ",";
      }
      String othersStr = "其它" + "(" +sum + ")" + " " + "－" + otherStr;
      sb.append("{");
      sb.append("nameStrs:\"" + othersStr + "\"");
      sb.append(",seqId:\"" + seqIdOther + "\"");
      sb.append("}");
    }else{
      sb.deleteCharAt(sb.length() - 1); 
    }
    sb.append("]");
    return sb.toString();
  }
  
  public String getNameIndexJson(Connection dbConn, Map request, String seqId) throws Exception {
      String sql = "select "
          + "SEQ_ID"
          + ",GROUP_ID"
          + ",PSN_NO"
          + ",PSN_NAME"
          + ",SEX"
          + ",DEPT_NAME"
          + ",TEL_NO_DEPT"
          //+ ",TEL_NO_HOME"
          + ",MOBIL_NO"
          + ",EMAIL"
          + " from oa_address where SEQ_ID IN (" + seqId + ") order by PSN_NO asc, DEPT_NAME asc, PSN_NAME asc";
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
      return pageDataList.toJson();
  }
  
  public ArrayList<YHAddress> getNameIndexJson1(Connection dbConn, String seqId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHAddress address = null;
    List list = new ArrayList();
    ArrayList<YHAddress> addressList = new ArrayList<YHAddress>();
    try {
      stmt = dbConn.createStatement();
      String sql = "select "
          + "SEQ_ID"
          + ",USER_ID"
          + ",GROUP_ID"
          + ",PSN_NAME"
          + ",SEX"
          + ",NICK_NAME"
          + ",BIRTHDAY"
          + ",MINISTRATION"
          + ",MATE"
          + ",CHILD"
          + ",DEPT_NAME"
          + ",ADD_DEPT"
          + ",POST_NO_DEPT"
          + ",TEL_NO_DEPT"
          + ",FAX_NO_DEPT"
          + ",ADD_HOME"
          + ",POST_NO_HOME"
          + ",TEL_NO_HOME"
          + ",MOBIL_NO"
          + ",BP_NO"
          + ",EMAIL"
          + ",OICQ_NO"
          + ",ICQ_NO"
          + ",NOTES"
          + ",PSN_NO"
          + ",SMS_FLAG"
          + " from oa_address where SEQ_ID IN (" + seqId + ") order by PSN_NO asc, DEPT_NAME asc, PSN_NAME asc ";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        address = new YHAddress();
        address.setSeqId(rs.getInt("SEQ_ID"));
        address.setUserId(rs.getString("USER_ID"));
        address.setGroupId(rs.getInt("GROUP_ID"));
        address.setPsnName(rs.getString("PSN_NAME"));
        address.setSex(rs.getString("SEX"));
        address.setNickName(rs.getString("NICK_NAME"));
        address.setBirthday(rs.getDate("BIRTHDAY"));
        address.setMinistration(rs.getString("MINISTRATION"));
        address.setMate(rs.getString("MATE"));
        address.setChild(rs.getString("CHILD"));
        address.setDeptName(rs.getString("DEPT_NAME"));
        address.setAddDept(rs.getString("ADD_DEPT"));
        address.setPostNoDept(rs.getString("POST_NO_DEPT"));
        address.setTelNoDept(rs.getString("TEL_NO_DEPT"));
        address.setFaxNoDept(rs.getString("FAX_NO_DEPT"));
        address.setAddHome(rs.getString("ADD_HOME"));
        address.setPostNoHome(rs.getString("POST_NO_HOME"));
        address.setTelNoHome(rs.getString("TEL_NO_HOME"));
        address.setMobilNo(rs.getString("MOBIL_NO"));
        address.setBpNo(rs.getString("BP_NO"));
        address.setEmail(rs.getString("EMAIL"));
        address.setOicqNo(rs.getString("OICQ_NO"));
        address.setIcqNo(rs.getString("ICQ_NO"));
        address.setNotes(rs.getString("NOTES"));
        address.setPsnNo(rs.getInt("PSN_NO"));
        address.setSmsFlag(rs.getString("SMS_FLAG"));
        addressList.add(address);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return addressList;
  }
  
  /**
   * 判断是否有重复的分组名称  
   * @param dbConn
   * @param groupName  分组名称
   * @return
   * @throws Exception
   */
  public boolean existsGroupName(Connection dbConn, String groupName)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) FROM oa_address_team WHERE GROUP_NAME = '"
          + YHDBUtility.escapeLike(groupName) + "'";
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
   * 查询结果 （按条件查询）
   * @param dbConn
   * @param userId
   * @param psnName
   * @param sex
   * @param nickNames
   * @param deptName
   * @param telNoDept
   * @param addDept
   * @param telNoHome
   * @param addHome
   * @param notes
   * @return
   * @throws Exception
   */
  
  public String getAddressSearchJson(Connection dbConn, Map request,
      int userId, String psnName, String sex, String nickNames, String deptName, String telNoDept, 
      String addDept, String telNoHome, String addHome, String notes, String groupId, String beginDate, String endDate, String mobileNo) throws Exception {
    String whereStr = "";
    String groupIdStr = "";
    String birthDay = "";
    if(groupId.trim().equals("ALL")){
      groupIdStr = "";
    }else if(groupId.trim().equals("0")){
      groupIdStr = " and GROUP_ID = 0";
    }else if(groupId != "ALL" || groupId != "0"){
      groupIdStr = " and GROUP_ID=" + groupId;
    }
    if(sex.trim().equals("All")){
      whereStr = "";
    }else if(sex.trim().equals("0")){
      whereStr = " and SEX=" + sex;
    }else if(sex.trim().equals("1")){
      whereStr = " and SEX=" + sex;
    }
    String sql = "select "
        + "SEQ_ID"
        + ", USER_ID"
        + ", PSN_NAME"
        + ", SEX"
        + ", DEPT_NAME"
        + ", TEL_NO_DEPT"
        + ", MOBIL_NO"
        + ", EMAIL"
        + ", GROUP_ID"
        + " from oa_address where (USER_ID ='" + userId + "' or USER_ID is null)" + whereStr + "" + groupIdStr+"";

    if(!YHUtility.isNullorEmpty(telNoDept)){ 
      sql = sql + " and TEL_NO_DEPT like '%" + telNoDept + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(psnName)){ 
      sql = sql + " and PSN_NAME like '%" + psnName + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(nickNames)){ 
      sql = sql + " and NICK_NAME like '%" + nickNames + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(deptName)){ 
      sql = sql + " and DEPT_NAME like '%" + deptName + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(addDept)){ 
      sql = sql + " and ADD_DEPT like '%" + addDept + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(telNoHome)){ 
      sql = sql + " and TEL_NO_HOME like '%" + telNoHome + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(addHome)){ 
      sql = sql + " and ADD_HOME like '%" + addHome + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(mobileNo)){ 
      sql = sql + " and MOBIL_NO like '%" + mobileNo + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(notes)){ 
      sql = sql + " and NOTES like '%" + notes + "%'" + YHDBUtility.escapeLike(); 
    } 
    if(!YHUtility.isNullorEmpty(beginDate)){ 
      sql = sql + " and "+ YHDBUtility.getDateFilter("BIRTHDAY", beginDate, ">=");
    } 
    if(!YHUtility.isNullorEmpty(endDate)){ 
      sql = sql + " and "+ YHDBUtility.getDateFilter("BIRTHDAY", endDate, "<=");
    } 
    sql = sql + " order by PSN_NO asc, DEPT_NAME asc, PSN_NAME asc";
    
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
     
    return pageDataList.toJson();
  }
  
  /**
   * 管理联系人查询
   * @param dbConn
   * @param groupId
   * @param userId
   * @return
   * @throws Exception
   */
  
  public String getManageContactJson(Connection dbConn, Map request,
      String groupId, String userId) throws Exception {
    String userIdFun = "";
    if(YHUtility.isNullorEmpty(userId)){
      userIdFun += " and USER_ID is null";
    }else {
      userIdFun += " and (USER_ID ='" + userId + "' or USER_ID is null)";
    }
    String sql = "select "
        + "SEQ_ID"
        + ",USER_ID"
        + ",GROUP_ID"
        + ",PSN_NO"
        + ",PSN_NAME"
        + ",SEX"
        + ",DEPT_NAME"
        + ",TEL_NO_DEPT"
        //+ ",TEL_NO_HOME"
        + ",MOBIL_NO"
        + ",EMAIL"
        + " from oa_address where GROUP_ID='" + groupId + "'" + userIdFun + " order by PSN_NO ASC, DEPT_NAME ASC, PSN_NAME ASC";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }

  
  public ArrayList<YHAddress> getManageContactJson1(Connection dbConn,
      String groupId, String userId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHAddress address = null;
    List list = new ArrayList();
    ArrayList<YHAddress> addressList = new ArrayList<YHAddress>();
    String userIdFun = "";
    if(YHUtility.isNullorEmpty(userId)){
      userIdFun += " and USER_ID is null";
    }else {
      userIdFun += " and USER_ID ='" + userId + "'";
    }
    try {
      stmt = dbConn.createStatement();
      String sql = "select "
          + "SEQ_ID"
          + ",USER_ID"
          + ",GROUP_ID"
          + ",PSN_NAME"
          + ",SEX"
          + ",NICK_NAME"
          + ",BIRTHDAY"
          + ",MINISTRATION"
          + ",MATE"
          + ",CHILD"
          + ",DEPT_NAME"
          + ",ADD_DEPT"
          + ",POST_NO_DEPT"
          + ",TEL_NO_DEPT"
          + ",FAX_NO_DEPT"
          + ",ADD_HOME"
          + ",POST_NO_HOME"
          + ",TEL_NO_HOME"
          + ",MOBIL_NO"
          + ",BP_NO"
          + ",EMAIL"
          + ",OICQ_NO"
          + ",ICQ_NO"
          + ",NOTES"
          + ",PSN_NO"
          + ",SMS_FLAG"
          + " from oa_address where GROUP_ID='" + groupId + "'" + userIdFun;
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        address = new YHAddress();
        address.setSeqId(rs.getInt("SEQ_ID"));
        address.setUserId(rs.getString("USER_ID"));
        address.setGroupId(rs.getInt("GROUP_ID"));
        address.setPsnName(rs.getString("PSN_NAME"));
        address.setSex(rs.getString("SEX"));
        address.setNickName(rs.getString("NICK_NAME"));
        address.setBirthday(rs.getDate("BIRTHDAY"));
        address.setMinistration(rs.getString("MINISTRATION"));
        address.setMate(rs.getString("MATE"));
        address.setChild(rs.getString("CHILD"));
        address.setDeptName(rs.getString("DEPT_NAME"));
        address.setAddDept(rs.getString("ADD_DEPT"));
        address.setPostNoDept(rs.getString("POST_NO_DEPT"));
        address.setTelNoDept(rs.getString("TEL_NO_DEPT"));
        address.setFaxNoDept(rs.getString("FAX_NO_DEPT"));
        address.setAddHome(rs.getString("ADD_HOME"));
        address.setPostNoHome(rs.getString("POST_NO_HOME"));
        address.setTelNoHome(rs.getString("TEL_NO_HOME"));
        address.setMobilNo(rs.getString("MOBIL_NO"));
        address.setBpNo(rs.getString("BP_NO"));
        address.setEmail(rs.getString("EMAIL"));
        address.setOicqNo(rs.getString("OICQ_NO"));
        address.setIcqNo(rs.getString("ICQ_NO"));
        address.setNotes(rs.getString("NOTES"));
        address.setPsnNo(rs.getInt("PSN_NO"));
        address.setSmsFlag(rs.getString("SMS_FLAG"));
        addressList.add(address);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return addressList;
  }
  /**
   * 管理联系人：删除
   * @param conn
   * @param seqIds   ADDRESS表中的seqId
   * @throws Exception
   */
  
  public void deleteAll(Connection conn, String seqIds) throws Exception {
    String sql = "DELETE FROM oa_address WHERE SEQ_ID IN(" + seqIds + ")";
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
  
 /**
  * 管理分组 ：删除 （该组下的联系人转入到默认分组中）
  * @param dbConn
  * @param seqId
  * @throws Exception
  */
  
  public void updateManageGroup(Connection dbConn, int seqId) throws Exception {
    String sql = "update oa_address set GROUP_ID = 0 WHERE GROUP_ID = '" + seqId +"'";
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
  
  /**
   * 个人管理分组：清空该组内的联系人
   * @param dbConn
   * @param seqId    ADDRESS表中的groupId
   * @throws Exception
   */
  
  public void deletePrivateClearContact(Connection dbConn, int seqId, int loginUserId) throws Exception {

    String sql = "DELETE FROM oa_address WHERE GROUP_ID=" + seqId +" and USER_ID=" + loginUserId;
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
  
  /**
   * 公共管理分组：清空该组内的联系人
   * @param dbConn
   * @param seqId    ADDRESS表中的groupId
   * @throws Exception
   */
  
  public void deletePublicClearContact(Connection dbConn, int seqId) throws Exception {
    String userId = null;
    String sql = "DELETE FROM oa_address WHERE GROUP_ID=" + seqId +" and USER_ID is null";
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

  /**
   * 公共通讯簿（公共事务）：查询结果 
   * @param dbConn
   * @param userId
   * @param psnName
   * @param sex
   * @param nickNames
   * @param deptName
   * @param telNoDept
   * @param addDept
   * @param telNoHome
   * @param addHome
   * @param notes
   * @return
   * @throws Exception
   */
  
  public String getPublicAddressSearchJson(Connection dbConn,
      Map request, YHPerson person, String psnName, String sex, String nickNames, String deptName, String telNoDept, 
      String addDept, String telNoHome, String addHome, String notes, String groupId, String beginDate, String endDate, String mobileNo) throws Exception {
    String whereStr = "";
    String groupIdStr = "";
    YHPageDataList pageDataList = null;
    if(groupId.trim().equals("ALL")){
      groupIdStr = "";
    }else if(groupId.trim().equals("0")){
      groupIdStr = " and GROUP_ID = 0";
    }else if(groupId != "ALL" || groupId != "0"){
      groupIdStr = " and GROUP_ID=" + groupId;
    }
    
    if(sex.trim().equals("All")){
      whereStr = "";
    }else if(sex.trim().equals("0")){
      whereStr = " and SEX = '" + sex + "'";
    }else if(sex.trim().equals("1")){
      whereStr = " and SEX = '" + sex + "'";
    }
    String sql = "select "
        + "oa_address.SEQ_ID"
        + ",PSN_NAME"
        + ",SEX"
        + ",DEPT_NAME"
        + ",TEL_NO_DEPT"
        + ",TEL_NO_HOME"
        + ",MOBIL_NO"
        + ",EMAIL"
        + ",GROUP_ID"
        + " from oa_address left outer join oa_address_team on  oa_address.GROUP_ID = oa_address_team.SEQ_ID where oa_address.USER_ID is null ";
       if (!person.isAdminRole()) {
          sql += " and ( "
          + findInSet(String.valueOf(person.getSeqId()),"oa_address_team.PRIV_USER")
          + " or "+ findInSet(String.valueOf(person.getDeptId()),"oa_address_team.PRIV_DEPT")
          + " or "+ findInSet(person.getUserPriv(),"oa_address_team.PRIV_ROLE")
          + " or oa_address_team.PRIV_DEPT = 'ALL_DEPT'"
          + " OR oa_address_team.PRIV_DEPT = '0'"
          + ") " ;
        }
        sql += whereStr + "" + groupIdStr + "";
      if(!YHUtility.isNullorEmpty(telNoDept)){ 
        sql = sql + " and oa_address.TEL_NO_DEPT like '%" + telNoDept + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(psnName)){ 
        sql = sql + " and oa_address.PSN_NAME like '%" + psnName + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(nickNames)){ 
        sql = sql + " and oa_address.NICK_NAME like '%" + nickNames + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(deptName)){ 
        sql = sql + " and oa_address.DEPT_NAME like '%" + deptName + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(addDept)){ 
        sql = sql + " and oa_address.ADD_DEPT like '%" + addDept + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(telNoHome)){ 
        sql = sql + " and oa_address.TEL_NO_HOME like '%" + telNoHome + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(addHome)){ 
        sql = sql + " and oa_address.ADD_HOME like '%" + addHome + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(notes)){ 
        sql = sql + " and oa_address.NOTES like '%" + notes + "%'" + YHDBUtility.escapeLike(); 
      } 
//      if(!YHUtility.isNullorEmpty(notes)){ 
//        sql=sql+" and NOTES like '%" + notes + "%'"; 
//      } 
      if(!YHUtility.isNullorEmpty(beginDate)){ 
        sql = sql + " and "+ YHDBUtility.getDateFilter("oa_address.BIRTHDAY", beginDate, ">=");
      } 
      if(!YHUtility.isNullorEmpty(endDate)){ 
        sql = sql + " and "+ YHDBUtility.getDateFilter("oa_address.BIRTHDAY", endDate, "<=");
      } 
      if(!YHUtility.isNullorEmpty(mobileNo)){ 
        sql = sql + " and oa_address.MOBIL_NO like '%" + mobileNo + "%'" + YHDBUtility.escapeLike(); 
      } 
      sql = sql + " order by oa_address.PSN_NO asc, oa_address.DEPT_NAME asc, oa_address.PSN_NAME asc";
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
      pageDataList = YHPageLoader.loadPageList(dbConn, queryParam,sql);
      return pageDataList.toJson();
  }
  
  public ArrayList<YHAddress> getPublicAddressSearchJson1(Connection dbConn,
      YHPerson person, String psnName, String sex, String nickNames, String deptName, String telNoDept, 
      String addDept, String telNoHome, String addHome, String notes, String groupId, String beginDate, String endDate) throws Exception {
    
    String whereStr = "";
    String groupIdStr = "";
    String birthDay = "";
    if(groupId.trim().equals("ALL")){
      groupIdStr = "";
    }else if(groupId.trim().equals("0")){
      groupIdStr = " and GROUP_ID = 0";
    }else if(groupId != "ALL" || groupId != "0"){
      groupIdStr = " and GROUP_ID=" + groupId;
    }
    
    if(sex.trim().equals("All")){
      whereStr = "";
    }else if(sex.trim().equals("0")){
      whereStr = " and SEX=" + sex;
    }else if(sex.trim().equals("1")){
      whereStr = " and SEX=" + sex;
    }
    Statement stmt = null;
    ResultSet rs = null;
    YHAddress address = null;
    List list = new ArrayList();
    ArrayList<YHAddress> addressList = new ArrayList<YHAddress>();
    try {
      stmt = dbConn.createStatement();
      String sql = "select "
          + "SEQ_ID"
          + ",USER_ID"
          + ",GROUP_ID"
          + ",PSN_NAME"
          + ",SEX"
          + ",NICK_NAME"
          + ",BIRTHDAY"
          + ",MINISTRATION"
          + ",MATE"
          + ",CHILD"
          + ",DEPT_NAME"
          + ",ADD_DEPT"
          + ",POST_NO_DEPT"
          + ",TEL_NO_DEPT"
          + ",FAX_NO_DEPT"
          + ",ADD_HOME"
          + ",POST_NO_HOME"
          + ",TEL_NO_HOME"
          + ",MOBIL_NO"
          + ",BP_NO"
          + ",EMAIL"
          + ",OICQ_NO"
          + ",ICQ_NO"
          + ",NOTES"
          + ",PSN_NO"
          + ",SMS_FLAG"
          + " from oa_address ,oa_address_team where USER_ID is null " 
          + " AND oa_address.GROUP_ID = oa_address_team.SEQ_ID and ( "
          + findInSet(String.valueOf(person.getSeqId()),"oa_address_team.PRIV_USER")
          + " or "+ findInSet(String.valueOf(person.getDeptId()),"oa_address_team.PRIV_DEPT")
          + " or "+ findInSet(person.getUserPriv(),"oa_address_team.PRIV_ROLE")
          + ") " + whereStr + "" + groupIdStr + "";
      if(!YHUtility.isNullorEmpty(telNoDept)){ 
        sql = sql + " and TEL_NO_DEPT like '%" + telNoDept + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(psnName)){ 
        sql = sql + " and PSN_NAME like '%" + psnName + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(nickNames)){ 
        sql = sql + " and NICK_NAME like '%" + nickNames + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(deptName)){ 
        sql = sql + " and DEPT_NAME like '%" + deptName + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(addDept)){ 
        sql = sql + " and ADD_DEPT like '%" + addDept + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(telNoHome)){ 
        sql = sql + " and TEL_NO_HOME like '%" + telNoHome + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(addHome)){ 
        sql = sql + " and ADD_HOME like '%" + addHome + "%'" + YHDBUtility.escapeLike(); 
      } 
      if(!YHUtility.isNullorEmpty(notes)){ 
        sql = sql + " and NOTES like '%" + notes + "%'" + YHDBUtility.escapeLike(); 
      } 
//      if(!YHUtility.isNullorEmpty(notes)){ 
//        sql=sql+" and NOTES like '%" + notes + "%'" + YHDBUtility.escapeLike(); 
//      } 
      if(!YHUtility.isNullorEmpty(beginDate)){ 
        sql = sql + " and " + YHDBUtility.getDateFilter("BIRTHDAY", beginDate, ">=");
      } 
      if(!YHUtility.isNullorEmpty(endDate)){ 
        sql = sql + " and " + YHDBUtility.getDateFilter("BIRTHDAY", endDate, "<=");
      } 
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        address = new YHAddress();
        address.setSeqId(rs.getInt("SEQ_ID"));
        address.setUserId(rs.getString("USER_ID"));
        address.setGroupId(rs.getInt("GROUP_ID"));
        address.setPsnName(rs.getString("PSN_NAME"));
        address.setSex(rs.getString("SEX"));
        address.setNickName(rs.getString("NICK_NAME"));
        if(rs.getTimestamp("BIRTHDAY") != null){
          birthDay = YHUtility.getDateTimeStr(rs.getTimestamp("BIRTHDAY"));
        }
        if (!YHUtility.isNullorEmpty(birthDay)) {
          birthDay = birthDay.substring(0, 10);
          address.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthDay));
        }
        address.setMinistration(rs.getString("MINISTRATION"));
        address.setMate(rs.getString("MATE"));
        address.setChild(rs.getString("CHILD"));
        address.setDeptName(rs.getString("DEPT_NAME"));
        address.setAddDept(rs.getString("ADD_DEPT"));
        address.setPostNoDept(rs.getString("POST_NO_DEPT"));
        address.setTelNoDept(rs.getString("TEL_NO_DEPT"));
        address.setFaxNoDept(rs.getString("FAX_NO_DEPT"));
        address.setAddHome(rs.getString("ADD_HOME"));
        address.setPostNoHome(rs.getString("POST_NO_HOME"));
        address.setTelNoHome(rs.getString("TEL_NO_HOME"));
        address.setMobilNo(rs.getString("MOBIL_NO"));
        address.setBpNo(rs.getString("BP_NO"));
        address.setEmail(rs.getString("EMAIL"));
        address.setOicqNo(rs.getString("OICQ_NO"));
        address.setIcqNo(rs.getString("ICQ_NO"));
        address.setNotes(rs.getString("NOTES"));
        address.setPsnNo(rs.getInt("PSN_NO"));
        address.setSmsFlag(rs.getString("SMS_FLAG"));
//        if((psnName != "" && rs.getString("PSN_NAME").toLowerCase().indexOf(psnName.toLowerCase()) == -1)
//            ||(nickNames !="" && rs.getString("NICK_NAME").toLowerCase().indexOf(nickNames.toLowerCase()) == -1)
//            ||(deptName !="" && rs.getString("DEPT_NAME").toLowerCase().indexOf(deptName.toLowerCase()) == -1)
//            ||(telNoDept !="" && rs.getString("TEL_NO_DEPT").toLowerCase().indexOf(telNoDept.toLowerCase()) == -1)
//            ||(addDept !="" && rs.getString("ADD_DEPT").toLowerCase().indexOf(addDept.toLowerCase()) == -1)
//            ||(telNoHome !="" && rs.getString("TEL_NO_HOME").toLowerCase().indexOf(telNoHome.toLowerCase()) == -1)
//            ||(addHome !="" && rs.getString("ADD_HOME").toLowerCase().indexOf(addHome.toLowerCase()) == -1)
//            ||(notes !="" && rs.getString("NOTES").toLowerCase().indexOf(notes.toLowerCase()) == -1)
//            ){
//          continue;
//        }
        addressList.add(address);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return addressList;
  }
  
  /**
   * （公共通讯簿）管理联系人查询
   * @param dbConn
   * @param groupId
   * @param userId
   * @return
   * @throws Exception
   */
  
  
  public String getPublicManageContactJson(Connection dbConn, Map request,
      String groupId, String userId) throws Exception {
    String userIdStr = "";
    if(userId.trim().equals("")){
      userIdStr = " and USER_ID is null";
    }else{
      userIdStr = "";
    }
    String sql = "select "
        + "SEQ_ID"
        + ",GROUP_ID"
        + ",PSN_NO"
        + ",PSN_NAME"
        + ",SEX"
        + ",DEPT_NAME"
        + ",TEL_NO_DEPT"
        //+ ",TEL_NO_HOME"
        + ",MOBIL_NO"
        + ",EMAIL"
        + " from oa_address where GROUP_ID='" + groupId + "'" + userIdStr + " order by PSN_NO ASC, DEPT_NAME ASC, PSN_NAME ASC";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    
    return pageDataList.toJson();
  }
  
  public ArrayList<YHAddress> getPublicManageContactJson1(Connection dbConn,
      String groupId, String userId) throws Exception {
    String userIdStr = "";
    if(userId.trim().equals("")){
      userIdStr = " and USER_ID is null";
    }else{
      userIdStr = "";
    }
    Statement stmt = null;
    ResultSet rs = null;
    YHAddress address = null;
    List list = new ArrayList();
    ArrayList<YHAddress> addressList = new ArrayList<YHAddress>();
    try {
      stmt = dbConn.createStatement();
      String sql = "select "
          + "SEQ_ID"
          + ",USER_ID"
          + ",GROUP_ID"
          + ",PSN_NAME"
          + ",SEX"
          + ",NICK_NAME"
          + ",BIRTHDAY"
          + ",MINISTRATION"
          + ",MATE"
          + ",CHILD"
          + ",DEPT_NAME"
          + ",ADD_DEPT"
          + ",POST_NO_DEPT"
          + ",TEL_NO_DEPT"
          + ",FAX_NO_DEPT"
          + ",ADD_HOME"
          + ",POST_NO_HOME"
          + ",TEL_NO_HOME"
          + ",MOBIL_NO"
          + ",BP_NO"
          + ",EMAIL"
          + ",OICQ_NO"
          + ",ICQ_NO"
          + ",NOTES"
          + ",PSN_NO"
          + ",SMS_FLAG"
          + " from oa_address where GROUP_ID='" + groupId + "'" + userIdStr + "";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        address = new YHAddress();
        address.setSeqId(rs.getInt("SEQ_ID"));
        address.setUserId(rs.getString("USER_ID"));
        address.setGroupId(rs.getInt("GROUP_ID"));
        address.setPsnName(rs.getString("PSN_NAME"));
        address.setSex(rs.getString("SEX"));
        address.setNickName(rs.getString("NICK_NAME"));
        address.setBirthday(rs.getDate("BIRTHDAY"));
        address.setMinistration(rs.getString("MINISTRATION"));
        address.setMate(rs.getString("MATE"));
        address.setChild(rs.getString("CHILD"));
        address.setDeptName(rs.getString("DEPT_NAME"));
        address.setAddDept(rs.getString("ADD_DEPT"));
        address.setPostNoDept(rs.getString("POST_NO_DEPT"));
        address.setTelNoDept(rs.getString("TEL_NO_DEPT"));
        address.setFaxNoDept(rs.getString("FAX_NO_DEPT"));
        address.setAddHome(rs.getString("ADD_HOME"));
        address.setPostNoHome(rs.getString("POST_NO_HOME"));
        address.setTelNoHome(rs.getString("TEL_NO_HOME"));
        address.setMobilNo(rs.getString("MOBIL_NO"));
        address.setBpNo(rs.getString("BP_NO"));
        address.setEmail(rs.getString("EMAIL"));
        address.setOicqNo(rs.getString("OICQ_NO"));
        address.setIcqNo(rs.getString("ICQ_NO"));
        address.setNotes(rs.getString("NOTES"));
        address.setPsnNo(rs.getInt("PSN_NO"));
        address.setSmsFlag(rs.getString("SMS_FLAG"));
        addressList.add(address);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return addressList;
  }
  
  /**
   * 查询表ADDRESS_GROUP的信息
   * @param dbConn
   * @param moduleCode
   * @return
   * @throws Exception
   */
  
  public ArrayList<YHAddressGroup> getAddressGroup(Connection dbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHAddressGroup addressGroup = null;
    List list = new ArrayList();
    ArrayList<YHAddressGroup> groupList = new ArrayList<YHAddressGroup>();
    try {
      stmt = dbConn.createStatement();
      String sql = "select SEQ_ID, PRIV_DEPT, PRIV_ROLE, PRIV_USER, SUPPORT_DEPT， SUPPORT_USER from oa_address_team";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        addressGroup = new YHAddressGroup();
        addressGroup.setSeqId(rs.getInt("SEQ_ID"));
        addressGroup.setPrivDept(rs.getString("PRIV_DEPT"));
        addressGroup.setPrivRole(rs.getString("PRIV_ROLE"));
        addressGroup.setPrivUser(rs.getString("PRIV_USER"));
        addressGroup.setSupportDept(rs.getString("SUPPORT_DEPT"));
        addressGroup.setSupportUser(rs.getString("SUPPORT_USER"));
        groupList.add(addressGroup);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return groupList;
  }
  
  public void changePublicGroupLogic(Connection conn,int groupId,String seqStr,String groupIdOld) throws Exception{
    String sql = "update oa_address SET GROUP_ID = " + groupId + " WHERE GROUP_ID = " + groupIdOld + " and SEQ_ID IN(" + seqStr + ")";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  
  public void changePrivateGroupLogic(Connection conn,int groupId,String seqStr,String groupIdOld) throws Exception{
    String sql = "update oa_address SET GROUP_ID = " + groupId + " WHERE GROUP_ID = " + groupIdOld + " and SEQ_ID IN(" + seqStr + ")";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  
  public void changePublicCopyGroupLogic(Connection conn,int groupId,String seqStr,String groupIdOld) throws Exception{
    String sql = "select SEQ_ID, PRIV_DEPT, PRIV_ROLE, PRIV_USER, SUPPORT_DEPT， SUPPORT_USER from oa_address where SEQ_ID IN(" + seqStr + ")";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  
  public int getSupportPriv(Connection conn, String groupId, String loginDeptId, String loginSeqId , String loginUserPriv)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      String queryStr = "select count(*) from oa_address_team where " 
        + " SEQ_ID=" + Integer.parseInt(groupId) 
        + " and ("
        + findInSet(loginSeqId,"PRIV_USER")
        + " or "+ YHDBUtility.findInSet(loginDeptId,"PRIV_DEPT")
        + " or "+ YHDBUtility.findInSet(loginUserPriv,"PRIV_ROLE")
        + " or PRIV_DEPT = 'ALL_DEPT'"
        + " OR PRIV_DEPT = '0'"
        + ") "
        + "  and (" +YHDBUtility.findInSet(loginDeptId, "SUPPORT_DEPT") 
        + " or (SUPPORT_DEPT like 0 or SUPPORT_DEPT like 'ALL_DEPT')  " 
        + " or "+ YHDBUtility.findInSet(loginSeqId, "SUPPORT_USER")+")"; 
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      int num = 0;
      if(rs.next()){      
        num = rs.getInt(1);
      }
      return num;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  public int getSupportPriv1(Connection conn, String groupId, String loginDeptId, String loginSeqId , String loginUserPriv)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      String queryStr = "select count(*) from oa_address_team where " 
        + " SEQ_ID=" + Integer.parseInt(groupId) 
        + " and (("
        + findInSet(loginSeqId,"PRIV_USER")
        + " or "+ findInSet(loginDeptId,"PRIV_DEPT")
        + " or "+ findInSet(loginUserPriv,"PRIV_ROLE")
        + " or PRIV_DEPT = 'ALL_DEPT'"
        + " OR PRIV_DEPT = '0'"
        + ") "
        + "  OR (" +YHDBUtility.findInSet(loginDeptId, "SUPPORT_DEPT") 
        + " or (SUPPORT_DEPT like 0 or SUPPORT_DEPT like 'ALL_DEPT')  " 
        + " or "+ YHDBUtility.findInSet(loginSeqId, "SUPPORT_USER")+"))"; 
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      int num = 0;
      if(rs.next()){      
        num = rs.getInt(1);
      }
      return num;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  public static void main(String args[]) throws Exception {
    YHAddressLogic logic = new YHAddressLogic();
    String mbStrs = "C成42,F范18,L刘29,Z张19,Z张17,|Z+35,Z+34,||1+38,";
    //String result = logic.getResultMb(mbStrs);
  }
  
  public List<YHAddressGroup> getAddressInfo(Connection dbConn, int loginSeqId) throws Exception {
    YHORM orm = new YHORM();
    String[] filters = new String[]{"USER_ID is null order by USER_ID asc, ORDER_NO asc, GROUP_NAME desc"};
    return orm.loadListSingle(dbConn, YHAddressGroup.class, filters);
  }
  
  public List<YHAddressGroup> getAddressPresenceInfo(Connection dbConn, int loginSeqId) throws Exception {
    YHORM orm = new YHORM();
    String[] filters = new String[]{"USER_ID = '" 
        + loginSeqId + "' order by USER_ID asc, ORDER_NO asc, GROUP_NAME desc"};
    return orm.loadListSingle(dbConn, YHAddressGroup.class, filters);
  }
  
  public ArrayList<YHDbRecord> toExportPersonData(Connection conn, int seqId ,int userId) throws Exception{
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
    String sql = "SELECT PSN_NAME"
          + ",MINISTRATION "
          + ",NICK_NAME "
          + ",EMAIL "
          + ",MOBIL_NO "
          + ",BP_NO "
          + ",OICQ_NO "
          + ",ICQ_NO "
          + ",SEX "
          + ",BIRTHDAY "
          + ",MATE "
          + ",CHILD "
          + ",POST_NO_HOME "
          + ",ADD_HOME "
          + ",TEL_NO_HOME "
          + ",DEPT_NAME"
          + ",POST_NO_DEPT"
          + ",ADD_DEPT"
          + ",TEL_NO_DEPT"
          + ",FAX_NO_DEPT"
          + ",NOTES"
          + " from oa_address where GROUP_ID ="+seqId ;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery() ;
      while (rs.next()) {
        YHDbRecord record = new YHDbRecord();
        String psnName = rs.getString(1);
        String ministration = rs.getString(2);
        String nickName = rs.getString(3);
        String email = rs.getString(4);
        String mobilNo = rs.getString(5);
        String bpNo = rs.getString(7);
        String oicqNo = rs.getString(7);
        String icqNo = rs.getString(8);
        String sex = rs.getString(9);
        Date birthday = rs.getTimestamp(10);
        String mate = rs.getString(11);
        String child = rs.getString(12);
        String postNoHome = rs.getString(13);
        String addHome = rs.getString(14);
        String telNoHome = rs.getString(15);
        String deptName = rs.getString(16);
        String postNoDept = rs.getString(17);
        String addDept = rs.getString(18);
        String telNoDept = rs.getString(19);
        String faxNoDept = rs.getString(20);
        String notes = rs.getString(21);
        record.addField("姓名", psnName);
        record.addField("职位", ministration);
        record.addField("昵称", nickName);
        record.addField("电子邮件地址", email);
        record.addField("手机",mobilNo);
        record.addField("传呼机",bpNo);
        record.addField("QQ",oicqNo);
        record.addField("MSN",icqNo);
        record.addField("性别",getSexFunc(conn, sex).toString());
        record.addField("生日",YHUtility.getDateTimeStrCn(birthday));
        record.addField("配偶",mate);
        record.addField("子女",child);
        record.addField("家庭所在地邮政编码",postNoHome);
        record.addField("家庭所在街道",addHome);
        record.addField("家庭电话1",telNoHome);
        record.addField("公司",deptName);
        record.addField("公司所在地邮政编码",postNoDept);
        record.addField("公司所在街道",addDept);
        record.addField("办公电话1",telNoDept);
        record.addField("公司传真",faxNoDept);
        record.addField("附注",notes);
        result.add(record);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  
  public ArrayList<YHDbRecord> toExportPublicAdressData(Connection conn, int seqId, int loginSeqId) throws Exception{
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
    String sql = "SELECT PSN_NAME"
            + ",MINISTRATION "
            + ",NICK_NAME "
            + ",EMAIL "
            + ",MOBIL_NO "
            + ",BP_NO "
            + ",OICQ_NO "
            + ",ICQ_NO "
            + ",SEX "
            + ",BIRTHDAY "
            + ",MATE "
            + ",CHILD "
            + ",POST_NO_HOME "
            + ",ADD_HOME "
            + ",TEL_NO_HOME "
            + ",DEPT_NAME"
            + ",POST_NO_DEPT"
            + ",ADD_DEPT"
            + ",TEL_NO_DEPT"
            + ",FAX_NO_DEPT"
            + ",NOTES"
            + " from oa_address where GROUP_ID =" + seqId + " and USER_ID is null  ";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery() ;
      while (rs.next()) {
        YHDbRecord record = new YHDbRecord();
        String psnName = rs.getString(1);
        String ministration = rs.getString(2);
        String nickName = rs.getString(3);
        String email = rs.getString(4);
        String mobilNo = rs.getString(5);
        String bpNo = rs.getString(7);
        String oicqNo = rs.getString(7);
        String icqNo = rs.getString(8);
        String sex = rs.getString(9);
        Date birthday = rs.getTimestamp(10);
        String mate = rs.getString(11);
        String child = rs.getString(12);
        String postNoHome = rs.getString(13);
        String addHome = rs.getString(14);
        String telNoHome = rs.getString(15);
        String deptName = rs.getString(16);
        String postNoDept = rs.getString(17);
        String addDept = rs.getString(18);
        String telNoDept = rs.getString(19);
        String faxNoDept = rs.getString(20);
        String notes = rs.getString(21);
        
        record.addField("姓名", psnName);
        record.addField("职位", ministration);
        record.addField("昵称", nickName);
        record.addField("电子邮件地址", email);
        record.addField("手机",mobilNo);
        record.addField("传呼机",bpNo);
        record.addField("QQ",oicqNo);
        record.addField("MSN",icqNo);
        record.addField("性别",getSexFunc(conn, sex).toString());
        record.addField("生日",YHUtility.getDateTimeStrCn(birthday));
        record.addField("配偶",mate);
        record.addField("子女",child);
        record.addField("家庭所在地邮政编码",postNoHome);
        record.addField("家庭所在街道",addHome);
        record.addField("家庭电话1",telNoHome);
        record.addField("公司",deptName);
        record.addField("公司所在地邮政编码",postNoDept);
        record.addField("公司所在街道",addDept);
        record.addField("办公电话1",telNoDept);
        record.addField("公司传真",faxNoDept);
        record.addField("附注",notes);
        result.add(record);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  
  /**
   * 取得性别

   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public String getSexFunc(Connection conn , String sex) throws Exception{
    String result = "";
    if("0".equals(sex)){
      result = "男";
    }else if("1".equals(sex)){
      result = "女";
    }
    return result;
  }
  
  public boolean existsGroupId(Connection dbConn, int groupId, String psnName)
  throws Exception {
    long count = 0;
    String sql = "SELECT count(*) FROM oa_address WHERE GROUP_ID = " + groupId + " AND PSN_NAME = '" +psnName+"'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count >= 1) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  
  public int getGroupSeqIdLogic(Connection conn , int groupId, String psnName) throws Exception{
    int seqId = 0;
    String sql = "SELECT SEQ_ID FROM oa_address WHERE GROUP_ID = " + groupId + " AND PSN_NAME = '" +psnName+"'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        seqId = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return seqId;
  }
  
  public ArrayList<YHAddressGroup> getSelectGroup(Connection dbConn) throws Exception {
    ArrayList<YHAddressGroup> groupList = new ArrayList<YHAddressGroup>();
    Statement stmt = null;
    ResultSet rs = null;
    YHAddressGroup addressGroup = null;
    String sql = "SELECT SEQ_ID, GROUP_NAME FROM oa_address_team WHERE USER_ID is NULL";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        addressGroup = new YHAddressGroup();
        addressGroup.setSeqId(rs.getInt("SEQ_ID"));
        addressGroup.setGroupName(rs.getString("GROUP_NAME"));
        groupList.add(addressGroup);
      }
    } catch (Exception ex) {
       throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return groupList;
  }
}
