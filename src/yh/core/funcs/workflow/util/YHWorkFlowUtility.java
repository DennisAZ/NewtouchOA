package yh.core.funcs.workflow.util;

import java.io.File;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.funcs.workflow.data.YHFlowProcess;
import yh.core.funcs.workflow.data.YHFlowRun;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;

public class YHWorkFlowUtility {
  //public static Map departmentMap = new HashMap();
  public static String getFileName(String fileName) {
    fileName = fileName.replace(":", "");
    fileName = fileName.replace("\\", "");
    fileName = fileName.replace("/", "");
    fileName = fileName.replace("*", "");
    fileName = fileName.replace("?", "");
    fileName = fileName.replace("|", "");
    fileName = fileName.replace("<", "");
    fileName = fileName.replace(">", "");
    fileName = fileName.replace("\"", "");
    return fileName;
  }
  public static String getRunName(String runName){
    runName = runName.replaceAll("\\\\", "\\\\\\\\");
    runName = runName.replaceAll("'", "\\\\'");
    return runName;
  }
  public static String upperCaseWord(String str) {
    if (YHUtility.isNullorEmpty(str)) {
      return str;
    }
    str = str.substring(0, 1).toUpperCase() + str.substring(1);
    return str;
  }
  /**
   * 去除特殊字符
   * @param value
   * @return
   */
  public static String getOutSpecialChar(String realValue) {
    realValue = realValue.replaceAll("\"", "&quot;");
    realValue = realValue.replaceAll("\\$", "&#" + (int)('$') + ";");
    realValue = realValue.replaceAll("<", "&lt;");
    realValue = realValue.replaceAll(">", "&gt;");
    realValue = realValue.replaceAll("\n", "&#10;"); 
    realValue = realValue.replaceAll("\r", "&#13;"); 
    return realValue;
  }
  public static String encodeSpecial(String srcStr) {
    return YHUtility.encodeSpecial(YHUtility.null2Empty(srcStr));
  }
  /**
   * 判段id是不是在str里面
   * @param str
   * @param id
   * @return
   */
  public static boolean findId(String str, String id) {
    if(str == null || id == null || "".equals(str) || "".equals(id)){
      return false;
    }
    String[] aStr = str.split(",");
    for(String tmp : aStr){
      if(tmp.equals(id)){
        return true;
      }
    }
    return false;
  }
  /**
   * 交集字符串,非交集
   * @param str 字符串以,分,分割
   * @param flag true-取两字符串的交集,false-取非交集
   * @return
   */
  public static String checkId(String str, String ids , boolean flag) {
    if(ids == null){
      ids = "";
    }
    String[] aStr = ids.split(",");
    String idStr = "";
    for(String tmp : aStr){
      if(flag){
        if(findId(str , tmp)){
          idStr += tmp + ",";
        }
      }else{
        if(!findId(str , tmp)){
          idStr += tmp + ",";
        }
      }
    }
    return idStr;
  }
  /**
   *取得字符串in形式，如在数据库用in语句查询的时候需要用单引号．．
   * @param str
   * @return
   */
  public static String getInStr(String str) {
    if (str == null || "".equals(str)) {
      return "";
    }
    String[] strs = str.split(",");
    String newStr = "";
    for (String tmp : strs) {
      
      if (tmp != null && !"".equals(tmp)) {
        if (tmp.startsWith("'") && tmp.endsWith("'")) {
          newStr += "" + tmp + ",";
        } else {
          newStr += "'" + tmp + "',";
        }
      } 
    }
    newStr = getOutOfTail(newStr);
    return newStr;
  }
  /**
   * 去掉最后一个逗号
   * @param str
   * @return
   */
  public static String getOutOfTail(String str) {
    return getOutOfTail(str , ",");
  }
  /**
   * 去掉最后一个逗号
   * @param str
   * @return
   */
  public static String getOutOfTail(String str , String split) {
    if (str == null) {
      return str ;
    }
    if (str.endsWith(split) ) {
      str = str.substring(0, str.length() - split.length());
    }
    return str;
  }
  /**
   * 
   * @param privStr
   * @param privOther
   * @return
   */
  public static int privOther(String privStr , String privOther){
    int privOtherFlag = 0 ;
    if(privOther != null && !"".equals(privOther)){
      String[] aPriv = privOther.split(",");
      for(String tmp : aPriv){
        if(!"".equals(tmp) && findId(privStr , tmp)){
          privOtherFlag = 1 ;
        }
      }
    }
    return privOtherFlag;
  }
  /**
   * 匹配数字
   * @param value
   * @return
   */
  public static boolean isNumeric(String value){
    //如查找到一个非数字..
    if (value != null) {
      Pattern p = Pattern.compile("\\\\D+");
      Matcher m = p.matcher(value);
      boolean b = m.matches();
      if(b){
        return false;
      }else{
        return true;
      }
    } else {
      return false;
    }
    
  }
  /**
   * 匹配小数.或整数
   * @param value
   * @return
   */
  public static boolean isFloat(String value){
    if (value == null ) {
      return false;
    }
    Pattern p = Pattern.compile("\\d+\\.?\\d+");
    Matcher m = p.matcher(value);
    boolean b = m.matches();
    if(b){
      return true;
    }else{
      return false;
    }
  }
  /**
   * 
   * @param content 内容
   * @param type 0-提示信息,1-错误信息,2-警告信息,3-禁止信息,4-停止信息,5-空白信息
   * @return 
   */
  public static String Message(String content,int type){
    StringBuffer body = new StringBuffer();
    body.append("<div align=center><table width=\"300\" class=\"MessageBox\"><tbody><tr>");
    if(type == 0){
      body.append("<td class=\"msg info\" style='font-size:11pt'>");
    }
    if(type == 1){
      body.append("<td class=\"msg error\" style='font-size:11pt'>");
    }
    if(type == 2){
      body.append("<td class=\"msg warning\" style='font-size:11pt'>");
    }
    if(type == 3){
      body.append("<td class=\"msg forbidden\">");
    }
    if(type == 4){
      body.append("<td class=\"msg stop\">");
    }
    if(type == 5){
      body.append("<td class=\"msg blank\">");
    }
    body.append(content);
    body.append("</td></tr></tbody></table></div>");
    return body.toString();
  }
  /**
   * 构建类似find_in_set的sql语句 返回结果形如  findField = 'ddd' or findField = 'daaa'
   */
  public static String createFindSql(String findField , String str ){
    String query = "";
    String[] aStr = str.split(",");
    int j = 0 ;
    for(int i = 0 ;i < aStr.length ;i ++){
      if(!"".equals(aStr[i].trim())){
        if(j == 0){
          query = " " + findField + " = '" + aStr[i] + "'";
        }else{
          query += " OR " + findField + " = '" + aStr[i] + "'";
        }
        j ++;
      }
    }
    return query;
  }
  /**
   * 根据字符串，取得提醒标记
   * @param sSmsRemindNext
   * @param sSms2RemindNext
   * @param sWebMailRemindNext
   * @param sSmsRemindStart
   * @param sSms2RemindStart
   * @param sWebMailRemindStart
   * @param sSmsRemindAll
   * @param sSms2RemindAll
   * @param sWebMailRemindAll
   * @return
   */
  public static int getRemindFlag(String sSmsRemindNext , String sSms2RemindNext , String sWebMailRemindNext
      , String sSmsRemindStart, String sSms2RemindStart, String sWebMailRemindStart 
      , String sSmsRemindAll , String sSms2RemindAll , String sWebMailRemindAll){
    int remindFlag = 0;
    int needRemind = 0x200;
    int smsRemindNext = 0x100;
    int sms2RemindNext = 0x80;
    int mailRemindNext = 0x40;

    int smsRemindStart = 0x20;
    int sms2RemindStart = 0x10;
    int mailRemindStart = 8;

    int smsRemindAll = 4;
    int sms2RemindAll = 2;
    int mailRemindAll = 1;
    remindFlag += needRemind;
    if (sSmsRemindNext != null && sSmsRemindNext.equals("on")) {
      remindFlag += smsRemindNext;
    }
    if (sSms2RemindNext != null && sSms2RemindNext.equals("on")) {
      remindFlag += sms2RemindNext;
    }
    if (sWebMailRemindNext != null && sWebMailRemindNext.equals("on")) {
      remindFlag += mailRemindNext;
    }
    if (sSmsRemindStart != null && sSmsRemindStart.equals("on")) {
      remindFlag += smsRemindStart;
    }
    if (sSms2RemindStart != null && sSms2RemindStart.equals("on")) {
      remindFlag += sms2RemindStart;
    }
    if (sWebMailRemindStart != null && sWebMailRemindStart.equals("on")) {
      remindFlag += mailRemindStart;
    }
    if (sSmsRemindAll != null && sSmsRemindAll.equals("on")) {
      remindFlag += smsRemindAll;
    }
    if (sSms2RemindAll != null && sSms2RemindAll.equals("on")) {
      remindFlag += sms2RemindAll;
    }
    if (sWebMailRemindAll != null && sWebMailRemindAll.equals("on")) {
      remindFlag += mailRemindAll;
    }
    return remindFlag;
  }
  public static String addId(String content , String name , String tag) {
     String reg = "\\s[iI][dD]=([\"']?)" + name + "\\1\\s"; 
     Pattern pattern = Pattern.compile(reg);  
     Matcher matcher = pattern.matcher(content);  
     if (!matcher.find()) {
       content = content.trim();
       String tmp = "<" + tag;
       String id = tmp + " id=\"" + name + "\" ";
       content = content.replaceFirst("<" + tag , id);
     }
     return content;
   }
  
  /**
   * 权限验证
   * @param flowType
   * @param list
   * @param loginUser
   * @return
   * @throws Exception 
   */
  public static boolean checkPriv(YHFlowType flowType , List<YHFlowProcess> list , YHPerson loginUser ,Connection conn) throws Exception {
    //取得第一步
    YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
    boolean flag = false;
    //2是自由流程
    if ("1".equals(flowType.getFlowType())) {
      YHFlowProcess firstPrc = null;
      for(YHFlowProcess tmp : list){
        if(tmp.getPrcsId() == 1){
          firstPrc = tmp;
        }
      }
      flag = (firstPrc == null || !roleUtility.prcsRole(flowType , firstPrc , 0 , loginUser , conn));
    } else {
      flag = !roleUtility.prcsRole(flowType, 0, loginUser , conn);
    }
    return flag;
  }
  /**
   * 根据条件删除指定表中的数据
   * @param tableName 表名
   * @param condition 条件
   * @param conn
   * @throws Exception 
   */
  public static void deleteTable(String tableName , String condition , Connection conn) throws Exception{
    String delSql = "delete from " + tableName + " where " + condition;
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate(delSql);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, null, null); 
    }
  }
  /**
   * 根据条件更新指定表中的数据
   * @param tableName 表名
   * @param updateData setValue
   * @param condition 条件
   * @param conn
   * @throws Exception 
   */
  public static void updateTable(String tableName , String updateData , String condition , Connection conn) throws Exception{
    String delSql = "update " + tableName + " set " + updateData  + " where " + condition;
    updateTableBySql(delSql , conn);
  }
  public static void updateTableBySql(String sql , Connection conn) throws Exception{
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate(sql);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, null, null); 
    }
  }
  public static int getInsertSeqId(String table , Connection conn) throws Exception{
    Statement stm = null;
    ResultSet rs = null;
    String sql = "select max(seq_id)  from " + table ;
    int seqId = 0;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(sql);
      if (rs.next()) {
        seqId = rs.getInt(1);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return seqId;
  }
  /**
   * 从str字符串中去掉s
   * @param str
   * @param s
   * @return
   */
  public static String getOutOf(String str , String s) {
    String[] aStr = str.split(",");
    String strTmp = "";
    for(String tmp : aStr){
      if (tmp != null && !tmp.equals(s)) {
          strTmp += tmp + ',';
      } else if (tmp != null && tmp.equals(s)){
        continue;
      }
    }
    return strTmp;
  }
  /**
   * 会签意见替换宏标记
   * @param modelShort
   * @param fr
   * @param conn
   * @return
   * @throws Exception
   */
  public String getSignInfo(String modelShort , YHFlowRun fr , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    String query = "select PRCS_ID "  
      + " , FLOW_PRCS from oa_fl_run_prcs where "
      + "  RUN_ID = " + fr.getRunId();
    Map map = new HashMap();
    Statement stm2 = null;
    ResultSet rs2 = null ;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query);
      while (rs2.next()) {
        int prcsId = rs2.getInt("PRCS_ID");
        int flowPrcs = rs2.getInt("FLOW_PRCS");
        
        String query1 = "select PRCS_NAME from oa_fl_process where "
          + " FLOW_SEQ_ID = " + fr.getFlowId() 
          + " and PRCS_ID =" + flowPrcs;
        String prcsName = "";
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(query1);
          if (rs.next()) {
            prcsName = rs.getString("PRCS_NAME");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
        String tmp = (String) map.get(String.valueOf(prcsId));
        if ("".equals(tmp) || tmp == null) {
          map.put(String.valueOf(prcsId), prcsName );
        } else if (!prcsName.equals(tmp)) {
          map.put(String.valueOf(prcsId), tmp + "," + prcsName );
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    int tmpPos = modelShort.indexOf("#[会签意见", 0);
    List<String[]> signMacro = new ArrayList<String[]>();
    List<String[]> signMacroPrcs = new ArrayList<String[]>();
    while (tmpPos >= 0) {
      int endPos =  modelShort.indexOf("]", tmpPos);
      String signMacroName = modelShort.substring(tmpPos, endPos + 1);
      String signMacroNum = signMacroName.substring(signMacroName.indexOf("见") + 1 , signMacroName.length() - 1);
      tmpPos = endPos + 1;
      //去会签意见样式
      String macroStyle = "";
      int endPosNew ;
      if ("[".equals(modelShort.substring(endPos + 1, endPos + 2))) {
        endPosNew = modelShort.indexOf("]" , endPos + 1);
        macroStyle = modelShort.substring(endPos + 2, endPosNew );
        signMacroName += "[" + macroStyle + "]";
      } else {
        endPosNew = endPos;
      }
      if (signMacroNum.endsWith("*")) {
        String[] tmp = {signMacroName , signMacroNum.substring(0, signMacroNum.length() - 1), macroStyle};
        signMacroPrcs.add(tmp);
      } else {
        String[] tmp = {signMacroName , signMacroNum, macroStyle};
        signMacro.add(tmp);
      }
      tmpPos = modelShort.indexOf("#[会签意见", endPosNew + 1);
    }
    //按流程设计号取会签意见
    if (signMacroPrcs.size() > 0) {
      modelShort = this.signReplace( signMacroPrcs , map, fr, 1 , modelShort , conn);
    } else {
      modelShort = this.signReplace( signMacro , map , fr , 0 , modelShort, conn);
    }
    return modelShort;
  }
  /**
   * 会签意见替换宏标记
   * @param signMacro
   * @param prcsIdArray
   * @param fr
   * @param type
   * @param modelShort
   * @param conn
   * @return
   * @throws Exception
   */
  public String signReplace(List<String[]> signMacro , Map prcsIdArray , YHFlowRun fr, int type , String modelShort, Connection conn) throws Exception {
    int count = 0 ;
    for (String[] tmp : signMacro) {
      String text = tmp[0];
      //注意
      String signMacroNum = tmp[1];
      String signMacroStyle = tmp[2];
      String signContentTpl =  signMacroStyle;
      String query = "";
      if ( signMacroStyle == null 
          || "".equals(signMacroStyle)) {
        signContentTpl = "{P} {U}({SD}):{C} {Y}年{M}月{D}日 {H}:{I}:{S}";
      }
      if (type == 1) {
        query = "select * from oa_fl_run_feedback a left join oa_fl_run_prcs b on " 
          + " a.RUN_ID = b.RUN_ID  " 
          + " and a.PRCS_ID = b.PRCS_ID " 
          + " and a.USER_ID = b.USER_ID "
          + " where a.RUN_ID = " + fr.getRunId();
        if (!"".equals(signMacroNum)) {
          query += " and b.FLOW_PRCS=" + signMacroNum;
        }
        query += " order by a.PRCS_ID,a.EDIT_TIME";
      } else {
        query += "SELECT * from oa_fl_run_feedback where RUN_ID=" + fr.getRunId();
        
        if (!"".equals(signMacroNum)) {
          query += " and PRCS_ID=" + signMacroNum;
        }
        query += "  order by PRCS_ID,EDIT_TIME";
      }
      int count1 = 0 ;
      String outputContent = "";
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = conn.createStatement();
        rs = stm.executeQuery(query);
        while (rs.next()) {
          count ++ ;
          count1 ++ ;
          int userId = rs.getInt("USER_ID");
          int prcsId1 = rs.getInt("PRCS_ID");;
          String content = rs.getString("CONTENT");
          
          if (content == null) {
            content = "";
          }
          Date editTime = rs.getTimestamp("EDIT_TIME");
          String signData = YHWorkFlowUtility.clob2String(rs.getClob("SIGN_DATA"));
          String prcsName = "";
          String deptName = "";
          String longName = "";
          String privName = "";
          String userName = "";
          
          String query1 = "select "
            + " USER_NAME"
            + " , DEPT_ID"
            + " , USER_PRIV"
            + " from PERSON where "
            + " SEQ_ID=" + userId;
          Statement stm2 = null;
          ResultSet rs2 = null;
          try {
            stm2 = conn.createStatement();
            rs2 = stm2.executeQuery(query1);
            if (rs2.next()) {
              userName = rs2.getString("USER_NAME");
              int deptId = rs2.getInt("DEPT_ID");
              int userPriv = rs2.getInt("USER_PRIV");
              YHDeptLogic deptLogic = new YHDeptLogic();
              YHUserPrivLogic privLogic =  new YHUserPrivLogic();
              deptName = deptLogic.getNameById(deptId, conn);
              StringBuffer sb = new StringBuffer();
              deptLogic.getDeptNameLong(conn, deptId, sb);
              longName = sb.toString();
              if (longName.endsWith("/")) {
                longName = longName.substring(0, longName.length() - 1);
              }
              privName = privLogic.getNameById(userPriv, conn);
            }
          } catch(Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(stm2, rs2, null); 
          }
          if (prcsId1 != 0 
              //&& !"".equals(signMacroNum)
              ) {
            prcsName = "第" + prcsId1 + "步" + prcsIdArray.get(String.valueOf(prcsId1));
          }
          SimpleDateFormat df = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String dateStr = df.format(editTime);
          String[] aDate = dateStr.split(" ");

          String curYear = aDate[0].split("-")[0];
          String curMon =  aDate[0].split("-")[1];
          String curDay =  aDate[0].split("-")[2];
          String curHour = aDate[1].split(":")[0];
          String curMinite = aDate[1].split(":")[1];
          String curSecond = aDate[1].split(":")[2];
          String signContent =signContentTpl;
          if (signContent == null) {
            signContent = "";
          }
          if (signContent.indexOf("{Y}") != -1) {
            signContent = signContent.replaceAll("\\{Y\\}", curYear);
          }
          if (signContent.indexOf("{M}") != -1) {
            signContent = signContent.replaceAll("\\{M\\}", curMon);
          }
          if (signContent.indexOf("{D}") != -1) {
            signContent = signContent.replaceAll("\\{D\\}", curDay);
          }
          if (signContent.indexOf("{H}") != -1) {
            signContent = signContent.replaceAll("\\{H\\}", curHour);
          }
          if (signContent.indexOf("{I}") != -1) {
            signContent = signContent.replaceAll("\\{I\\}", curMinite);
          }
          if (signContent.indexOf("{S}") != -1) {
            signContent = signContent.replaceAll("\\{S\\}", curSecond);
          }
          //------
          if (signContent.indexOf("{P}") != -1) {
            signContent = signContent.replaceAll("\\{P\\}", prcsName);
          }
          if (signContent.indexOf("{C}") != -1) {
            signContent = signContent.replaceAll("\\{C\\}", content);
          }
          
          if (signContent.indexOf("{U}") != -1) {
            signContent = signContent.replaceAll("\\{U\\}", userName);
          }
          if (signContent.indexOf("{SD}") != -1) {
            signContent = signContent.replaceAll("\\{SD\\}",deptName);
          }
          if (signContent.indexOf("{LD}") != -1) {
            signContent = signContent.replaceAll("\\{LD\\}", longName);
          }
          if (signContent.indexOf("{R}") != -1) {
            signContent = signContent.replaceAll("\\{R\\}", privName);
          }
          if (signContent.indexOf("{SH}") != -1) {
            String signInfo = "";
            if(!"".equals(signData)) {
              String guid = YHGuid.getRawGuid();
              signInfo = "<div id=\"personal_sign" + count + "\"><script>LoadSignDataSign(\""+ signData +"\",\""+ count +"\",\""+ guid +"\",\"0\");</script></div>";
            }
            signContent = signContent.replaceAll("\\{SH\\}", signInfo);
          }
          outputContent += signContent + "<br>";
        }
        
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null); 
      }
      tmp[0] = tmp[0].replaceAll("\\[", "\\\\[");
      tmp[0] = tmp[0].replaceAll("\\]", "\\\\]");
      tmp[0] = tmp[0].replaceAll("\\{", "\\\\{");
      tmp[0] = tmp[0].replaceAll("\\}", "\\\\}");
      tmp[0] = tmp[0].replaceAll("\\*", "\\\\*");
      if (count1 > 0) {
        modelShort = modelShort.replaceAll(tmp[0], outputContent);
      } else {
        modelShort = modelShort.replaceAll(tmp[0], "");
      }
    }
    return modelShort;
  }
  public String getAttach(String modelShort , YHFlowRun fr , Connection conn , String imgPath) {
  // TODO Auto-generated method stub
    if (fr.getAttachmentId() == null 
        || "".equals(fr.getAttachmentId().trim())) {
      return modelShort;
    }
    String[] attachIdArray = fr.getAttachmentId().split(",");
    String names = fr.getAttachmentName();
    names = names.replace("'", "&#39;");
    names = names.replace("'", "&#34;");
    String[] attachNameArray = fr.getAttachmentName().split("\\*");
    
    int tmpPos = modelShort.indexOf("#[附件", 0);
    Map<String , String> attachMacro = new HashMap();
    while (tmpPos >= 0) {
      int endPos =  modelShort.indexOf("]", tmpPos);
      //30-25+1 6 25,6
      String attachMacroName = modelShort.substring(tmpPos, endPos  + 1);
      String attachMacroNum = attachMacroName.substring(attachMacroName.indexOf("件") + 1 , attachMacroName.length() - 1);
      attachMacro.put(attachMacroName, attachMacroNum);
      tmpPos = modelShort.indexOf("#[附件", endPos + 1);
    }
    for (String key : attachMacro.keySet()) {
      String attachMacroNum = attachMacro.get(key);
      if (attachMacroNum != null 
          && !"".equals(attachMacroNum)) {
        int index = Integer.parseInt(attachMacroNum);
        key = key.replaceAll("\\[", "\\\\[");
        key = key.replaceAll("\\]", "\\\\]");
        if (index <= attachIdArray.length) {
          if (!"".equals(attachIdArray[index - 1]) 
              && !"".equals(attachNameArray[index - 1])) {
            String attachInfo = this.getAttachMacro(attachIdArray[index - 1],  attachNameArray[index - 1], imgPath); 
            modelShort = modelShort.replaceAll(key, attachInfo);
          }
        } else {
          modelShort = modelShort.replaceAll(key, "");
        }
      }
    }
    if (modelShort.indexOf("#[附件]") != -1) {
      String attachAll = "";
      for (int i = 0 ;i < attachIdArray.length ; i ++) {
        String v = attachIdArray[i];
        if (!"".equals(v.trim())) {
          attachAll += this.getAttachMacro(v, attachNameArray[i], imgPath);
        }
      }
      modelShort = modelShort.replaceAll("#\\[附件\\]", attachAll);
    }
    return modelShort;
  }
  public String getAttachMacro(String id , String name , String imgPath) { 
    String src = this.getAttachImage(name, imgPath);
    String attachInfo = "<div><img src=\"" + src + "\">&nbsp;<a href=\"javascript:void(0)\" onclick=\"downLoadFile('"+name+"' ,  '"+id+"', \'workFlow\');\">" + name + "</a></div>";
    attachInfo = attachInfo.replace("$", "\\$");
    return attachInfo ;
  }
  public boolean isDeptParent(int childDept , int parentDept , Connection conn) throws Exception {
    String query = "select DEPT_PARENT from oa_department where SEQ_ID =" + childDept;
    Statement stmt = null;
    ResultSet rs = null;
    int parentId = 0;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      if (rs.next()) {
        parentId = rs.getInt("DEPT_PARENT");
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt , rs , null);
    }
    if (parentId == 0) {
      return false;
    } else if (parentId == parentDept) {
      return true;
    } else {
      return isDeptParent(parentId , parentDept , conn);
    }
  }
  
  /**
   * 附件显示时的图标
   * @param ext
   * @return
   */
  public String getAttachImage(String name , String imgPath) {
    String imgSrc = "";
    if(name.endsWith("gif")  
        || name.endsWith("jpg")){
      imgSrc = imgPath + "/gif.gif";
    }else if(name.endsWith("doc")
        || name.endsWith("docx")){
      imgSrc = imgPath + "/doc.gif";
    }else if(name.endsWith("xls")
        || name.endsWith("xlsx")){
      imgSrc = imgPath + "/xls.gif";
    }else if(name.endsWith("ppt")
        || name.endsWith("pptx")){
      imgSrc = imgPath + "/ppt.gif";
    }else if(name.endsWith("ext")){
      imgSrc = imgPath + "/exe.gif";
    }else if(name.endsWith("chm")){
      imgSrc = imgPath + "/chm.gif";
    }else if(name.endsWith("txt")){
      imgSrc = imgPath + "/txt.gif";
    }else if (name.endsWith("rar")
        || name.endsWith("zip")) {
      imgSrc = imgPath + "/rar.gif";
    } else{
      imgSrc = imgPath + "/defaut.gif";
    }
    return imgSrc;
  }
  /**
   * 获取步骤办理超时时间
   * @param timeOut 限时单位小时
   * @param beginTime 开始时间
   * @param endTime 结束时间
   * @param format 返回格式形如 "dhms"为xx天xx小时xx分钟xx秒
   * @param except 是否排除双修日，两位代码 00 不排除
   * @return
   * @throws ParseException 
   */
  public static String getTimeOut (String sTimeOut , Date beginTime , Date endTime , String format , String except) throws ParseException {
    String timeOutDesc = "";
    if (endTime == null ) {
      endTime = new Date();
    }
    if (beginTime == null) {
      return "";
    }
    long timeUsed = endTime.getTime() - beginTime.getTime();
    long difTime = 0 ;
    if (except == null) {
      except = "00";
    }
    
    if (!"00".equals(except)) {
      String saturday = except.substring(0,1);
      String sunday = except.substring(1);
      Calendar beginTimeCal = Calendar.getInstance();
      Calendar endTimeCal = Calendar.getInstance();
      beginTimeCal.setTime(beginTime);
      endTimeCal.setTime(endTime);
      int beginWeek = beginTimeCal.DAY_OF_WEEK;
      int endWeek = endTimeCal.DAY_OF_WEEK;
      
      int nextMonday = beginTimeCal.MONDAY;
      int lastFriday = endTimeCal.FRIDAY;
      //同一周
      if (isSameWeekDates(beginTime , endTime)) {
        if ("1".equals(sunday) && beginWeek == 0) {
          if (endWeek >0) {
            SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
            String str = sp.format(beginTime);
            Date date2 = sp.parse(str);
            difTime = beginTime.getTime() - date2.getTime();
          } else {
            difTime = endTime.getTime() - beginTime.getTime();
          }
        }
        if ("1".equals(saturday) && endWeek == 0) {
          if (beginWeek < 6) {
            SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
            String str = sp.format(beginTime);
            Date date2 = sp.parse(str);
            difTime = endTime.getTime() - date2.getTime();
          } else {
            difTime = endTime.getTime() - beginTime.getTime();
          }
        }
      } else {
        int difDay = (Math.abs(lastFriday - nextMonday) /86400000) + 1;
        difDay += (7 - beginWeek - 1) + endWeek;
        int weeks = difDay/7;
        if ("1".equals(sunday)) {
          difTime += weeks * 86400 * 1000;
        } 
        if ("1".equals(saturday)) {
          difTime += weeks * 86400 * 1000;
        }
      }
      timeUsed -= difTime;
    }
    //-- 超时计算 --
    if (!"".equals(sTimeOut) && sTimeOut != null ) {
      float timeOut = Float.parseFloat(sTimeOut);
      if (timeUsed > timeOut * 3600 *1000) { 
        timeUsed = timeUsed - (long)(timeOut * 3600 *1000);
        String dayStr = "";
        String hourStr = "";
        String minStr = "";
        String secStr = "";
        long day = timeUsed/(24*60*60*1000); 
        long hour = (timeUsed/(60*60*1000)-day*24); 
        long min = ((timeUsed/(60*1000))-day*24*60-hour*60); 
        long s = (timeUsed/1000-day*24*60*60-hour*60*60-min*60);
        if(day > 0){
          dayStr = day + "天";
        }
        if(hour>0){
          hourStr +=hour + "时";
        }
        if(min>0){
          minStr +=min + "分";
        }
        if(s>0){
          secStr +=s + "秒";
        }
        timeOutDesc= format;
        timeOutDesc = timeOutDesc.replaceAll("d", dayStr);
        timeOutDesc = timeOutDesc.replaceAll("h", hourStr);
        timeOutDesc = timeOutDesc.replaceAll("m", minStr);
        timeOutDesc = timeOutDesc.replaceAll("s", secStr);
        timeOutDesc = "超时：" + timeOutDesc;
      }
    }
    return timeOutDesc;
  }
  /**
   * 判断二个时间是否在同一个周
   * 
   * @param date1
   * @param date2
   * @return
   */
  public static boolean isSameWeekDates(Date date1, Date date2) {
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    cal1.setTime(date1);
    cal2.setTime(date2);
    int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
    if (0 == subYear) {
        if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
          return true;
        }
    } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
        // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
      if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
        return true;
      }
    } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
      if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
        return true;
      }
    }
    return false;
  }
 /**
  *将CLOB转成String ,静态方法
  * @param clob 字段
  * @return 内容字串，如果出现错误，返回
 * @throws Exception 
  */
 public final static String clob2String(Clob clob) throws Exception {
   if (clob == null) {
     return "";
   }
   StringBuffer sb = new StringBuffer(65535);//64K
   Reader clobStream = null;
   try {
     clobStream = clob.getCharacterStream();
     char[] b = new char[60000];//每次获取60K
     int i = 0;
     while((i = clobStream.read(b)) != -1) {
       sb.append(b,0,i);
     }
   } catch(Exception ex) {
     throw ex;
   } finally {
     try {
       if (clobStream != null) {
         clobStream.close();
       }
     } catch (Exception e) {
       throw e;
     }
   }
   if (sb == null)
     return "";
   else
     return sb.toString();
  }
  public static String getImgPath(HttpServletRequest request) {
    String contextPath = request.getContextPath();
    if (contextPath.equals("")) {
      contextPath = "/yh";
    }
    int styleIndex = 1;
    String stylePath = contextPath + "/core/styles/style" + styleIndex;
    String imgPath = stylePath + "/img";
    return imgPath;
  }
  /**
   * 主要是否有流程,表单管理权限
   * @param w
   * @param DeptId
   * @param u
   * @param conn
   * @return
   * @throws Exception 
   */
  public boolean isHaveRight(int deptId , YHPerson u , Connection conn) throws Exception {
    String userPrivOther = u.getUserPrivOther();
    if (!u.isAdminRole() 
        && !YHWorkFlowUtility.findId(userPrivOther, "1")
        &&  deptId != u.getDeptId()
        && !this.isDeptParent(u.getDeptId(), deptId , conn)) {
      return false;
    }
    return true;
  }
  public String getUserNameById(int seqId , Connection conn) throws Exception {
    String query = "select USER_NAME from PERSON where SEQ_ID = " + seqId; 
    Statement stm = null; 
    ResultSet rs = null; 
    String userName = "";
    try { 
      stm = conn.createStatement(); 
      rs = stm.executeQuery(query); 
      if (rs.next()){ 
        userName = rs.getString("USER_NAME");
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, rs, null); 
    } 
    return userName;
  }
  /**
   * 主要是否有分类管理权限
   * @param w
   * @param DeptId
   * @param u
   * @param conn
   * @return
   * @throws Exception 
   */
  public boolean isHaveSortRight(int deptId , YHPerson u , Connection conn) throws Exception {
    String userPrivOther = u.getUserPrivOther();
    if (!u.isAdminRole() 
        && !YHWorkFlowUtility.findId(userPrivOther, "1")
        && deptId != 0
        &&  deptId != u.getDeptId()
        && !this.isDeptParent(u.getDeptId(), deptId , conn)) {
      return false;
    }
    return true;
  }
  public String getNotDisplaySort(Connection conn) throws Exception{
    String sortIds = YHSysProps.getString("FLOWSORT_NEED_NOT_DISPLAY");
    if (YHUtility.isNullorEmpty(sortIds)) {
      return null;
    }
    sortIds = getInStr(sortIds);
    String query = "select SEQ_ID from oa_fl_sort WHERE SORT_NAME in (" + sortIds + ")";
    Statement stm = null; 
    ResultSet rs = null; 
    sortIds = "";
    try { 
      stm = conn.createStatement(); 
      rs = stm.executeQuery(query); 
      while (rs.next()){ 
        sortIds  += rs.getInt("SEQ_ID") + ",";
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, rs, null); 
    } 
    sortIds = getOutOfTail(sortIds);
    return sortIds;
  }
  public String[] getNewAttachPath(String fileName ,String module) throws Exception {
    String pathPx = YHSysProps.getAttachPath();
    String filePath = pathPx + File.separator + module;
    Calendar cld = Calendar.getInstance();
    int year = cld.get(Calendar.YEAR) % 100;
    int month = cld.get(Calendar.MONTH) + 1;
    String mon = month >= 10 ? month + "" : "0" + month;
    String hard = year + mon;
    String trusFileName = "";
    String attachmentId = YHGuid.getRawGuid();
    trusFileName = attachmentId + "_" + fileName;
    String trusPath = filePath + File.separator +  hard + File.separator + trusFileName;
    File storeDir = new File(filePath + File.separator +  hard);
    if (!storeDir.exists()) {
      storeDir.mkdirs();
    }
    String attId =  hard + "_" + attachmentId;
    String[] atts = {attId , trusPath};
    return atts;
  }
  public String getAttachPath(String aId , String aName  , String moduleDesc) throws Exception {
    String pathPx = YHSysProps.getAttachPath();
    String filePath = pathPx + File.separator +  moduleDesc;
    int index = aId.indexOf("_");
    String hard = "";
    String str = "";
    if (index > 0) {
      hard = aId.substring(0, index);
      str = aId.substring(index + 1);
    } else {
      hard = "all";
      str = aId;
    }
    String path = filePath + File.separator +  hard + File.separator +  str + "_" + aName;
    return path;
  }
  public static String copyAttachSingle(String attId , String attName , String moduleSrc , String moduleDesc ) throws Exception {
    String pathPx = YHSysProps.getAttachPath();
    String filePath = pathPx + File.separator +  moduleSrc;
    String filePath2 = pathPx + File.separator + moduleDesc;
    int index = attId.indexOf("_");
    String hard = "";
    String str = "";
    if (index > 0) {
      hard = attId.substring(0, index);
      str = attId.substring(index + 1);
    } else {
      hard = "all";
      str = attId;
    }
    String path = filePath + File.separator +  hard + File.separator +  str + "_" + attName;
    String attachmentId = YHGuid.getRawGuid();
    Calendar cld = Calendar.getInstance();
    int year =  cld.get(Calendar.YEAR)%100;
    int month = cld.get(Calendar.MONTH) + 1;
    String mon = month >= 10?month+"":"0"+month;
    String hard2 = year + mon ;
    String fileName2 = attachmentId + "_" + attName;
    File catalog = new File(filePath + File.separator + hard2);
    if(!catalog.exists()){
      catalog.mkdirs();
    }
    String tmp = filePath2 + File.separator + hard2 + File.separator +  fileName2;
    YHFileUtility.copyFile(path, tmp);
    return hard2 + "_" + attachmentId;
  }
  public static String copyAttach(String attId , String attName , String moduleSrc , String moduleDesc) throws Exception {
    String newAttId = "";
    String[] attIds = attId.split(",");
    String[] attNames = attName.split("\\*");
    for(int i = 0 ;i < attIds.length ;i ++){
      String tmp = attIds[i];
      if ("".equals(tmp)) {
        continue;
      }
      String attN = attNames[i];
      String newId = copyAttachSingle(tmp, attN , moduleSrc ,  moduleDesc);
      newAttId += newId + ",";
    }
    return newAttId;
  }
  public String getRoleJson(Connection conn) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    StringBuffer sb = new StringBuffer();
    int count  = 0 ;
    sb.append("[");
    try {
      stmt = conn.createStatement();
      String sql = "SELECT SEQ_ID , PRIV_NAME FROM USER_PRIV  order by PRIV_NO asc";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        sb.append("{").append("seqId:\"").append(rs.getInt("SEQ_ID")).append("\"").append(",roleName:\"").append(YHUtility.encodeSpecial(rs.getString("PRIV_NAME"))).append("\"").append("},");
        count++;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public static boolean isSave2DataTable() {
    String workflowSave2DataTable  = YHSysProps.getProp("WorkflowSave2DataTable");
    if ("1".equals(workflowSave2DataTable)) {
      return true;
    }
    return false;
  }
  public void saveFormDataToTable(Connection conn ,int runId , Map dataMap , int formId) throws Exception {
    String tableName = "" +YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE + formId;
    String del = "delete from " + tableName + " where RUN_ID = " + runId;
    this.updateTableBySql(del, conn);
    String field = "RUN_ID ,";
    String value = runId + ",";
    Set<Integer>  keys = dataMap.keySet();
    for (Integer key : keys) {
      field += "DATA_" + key + ",";
      String v = (String) dataMap.get(key);
      if (v != null ) {
        value +=  "'" + v.replace("'", "''") + "',";
      } else {
        value +=  "'',";
      }
    }
    field = this.getOutOfTail(field);
    value = this.getOutOfTail(value);
    String insertData = "insert into " + tableName + "("+ field +" ) values ( "+ value  +")";
    this.updateTableBySql(insertData, conn);
  }
  /**
   * 同步表和表单数据
   * @param conn
   * @param runId
   * @param formId
   * @throws Exception 
   */
  public void refresh(Connection conn ,int runId , int formId) throws Exception {
    String tableName = "" +YHWorkflowSave2DataTableLogic.FORM_DATA_TABLE_PRE + formId;
    String query = "select * from " + tableName + " where RUN_ID =" + runId;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      if (rs.next()) {
        ResultSetMetaData   rsm   =   rs.getMetaData(); 
        int   colNum   =   0; 
        colNum   =   rsm.getColumnCount(); 
        for(int   i = 1;i   <=   colNum;i++){ 
          String   name   =   rsm.getColumnName(i); 
          if (!"RUN_ID".equalsIgnoreCase(name) 
              && !"SEQ_ID".equalsIgnoreCase(name)) {
            String value = rs.getString(name);
            String itemId = "";
            if (name.startsWith("DATA_")) {
              itemId = name.replace("DATA_", "");
              if (value != null ) {
                value =  value.replace("'", "''"); 
              } else {
                value =  "";
              }
              String update = "update oa_fl_run_data set ITEM_DATA = '" + value + "' WHERE RUN_ID = " + runId + " and ITEM_ID = '"  + itemId + "'" ;
              this.updateTableBySql(update, conn);
            }
          }
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
}
