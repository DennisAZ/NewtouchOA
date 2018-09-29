package yh.subsys.oa.netmeeting.video.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.netmeeting.video.data.YHVideoMeetingManager;

public class YHVideoMeetingLogic {
  private static Logger log = Logger.getLogger(YHVideoMeetingLogic.class);

  public String checkUser(Connection dbConn, YHPerson person, String contexPathAll) throws Exception  {
    
    String userName = "";
    String password = "";
    String sql = " select RED_USERNAME,RED_PASSWORD "
               + " from oa_conference_manager "
               + " where user_ID = " + person.getSeqId()
               + " order by SEQ_ID ";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()) {
        userName = rs.getString("RED_USERNAME");
        password = rs.getString("RED_PASSWORD");
        
        Map ipAndPort = getIpAndPort(contexPathAll);
        String ip = (String)ipAndPort.get("ip");
        String port = (String)ipAndPort.get("port");
        String url = "http://" + ip + ":" + port + "/integration/xml";
        String strXML = "<?xml version='1.0' encoding='UTF-8'?>"
                      + "<Message><header>"
                      + "<action>" + "listSummaryMeeting" + "</action>"
                      + "<service>" + "meeting" + "</service>"
                      + "<type>" + "xml" + "</type>"
                      + "<userName>" + userName + "</userName>"
                      + "<password>" + password + "</password>"
                      + "</header>"
                      + "<body>"
                      + "<timeZoneId>" + "21" + "</timeZoneId>"
                      + "</body>"
                      + "</Message>";
        
        String reXML = YHVideoLogic.send(url,strXML);
        Document doc = DocumentHelper.parseText(reXML);
        Element root = doc.getRootElement();
        Element header = root.element("header");
        Element exceptionID = header.element("exceptionID");
        if(exceptionID != null && exceptionID.getText().equals("0x0000008")){
          return  "{state:\"2\"}";
        }
      }
      if(YHUtility.isNullorEmpty(userName) || YHUtility.isNullorEmpty(password)){
        return "{state:\"0\"}";
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return "{state:\"1\"}";
  }
  
  public String getMeetingList(Connection dbConn, Map request, YHPerson person, String contexPathAll) throws Exception  {
    
    Map map = getUsernameAndPassword(dbConn, person);
    String userName = (String)map.get("userName");
    
    StringBuffer sb = null;
    try{
      Map ipAndPort = getIpAndPort(contexPathAll);
      String ip = (String)ipAndPort.get("ip");
      String port = (String)ipAndPort.get("port");
      String url = "http://" + ip + ":" + port + "/integration/xml";
      String strXML = "<?xml version='1.0' encoding='UTF-8'?>"
                    + "<Message><header>"
                    + "<action>" + "listSummaryMeeting" + "</action>"
                    + "<service>" + "meeting" + "</service>"
                    + "<type>" + "xml" + "</type>"
                    + "<userName>" + "admin" + "</userName>"
                    + "<password>" + "admin" + "</password>"
                    + "</header>"
                    + "<body>"
                    + "<confKey>33401712</confKey>"
                    + "<timeZoneId>" + "21" + "</timeZoneId>"
                    + "</body>"
                    + "</Message>";
      
      String reXML = YHVideoLogic.send(url,strXML);

      Document doc = DocumentHelper.parseText(reXML);
      Element root = doc.getRootElement();
      Element header = root.element("header");
      Element exceptionID = header.element("exceptionID");
      if(exceptionID != null && exceptionID.getText().equals("0x0000008")){
        return  "error";
      }
      Element result = header.element("result");
      if(result.getText().equals("SUCCESS")){
        Element body = root.element("body");
        if(body == null){
          sb = new StringBuffer("{totalRecord:0,pageData:[]}");
        }
        else{
          Element meetings = body.element("meetings");
          Iterator iterator = meetings.elementIterator();
          List list = meetings.elements();
          
          sb = new StringBuffer("{totalRecord:");
          sb.append(list.size());
          sb.append(",pageData:[");
          int count = 0;
          while(iterator.hasNext()){
            Element meeting = (Element)iterator.next();
            if(meeting.getName().equals("meeting")){ 
              if(count != 0) {
                sb.append(",");
              }
              sb.append("{confKey:\"" + meeting.element("confKey").getText() + "\"");
              sb.append(",confType:\"" + meeting.element("confType").getText() + "\"");
              sb.append(",duringTime:\"" + meeting.element("duringTime").getText() + "\"");
              String hostName = meeting.element("hostName").getText();
              sb.append(",hostName:\"" + hostName + "\"");
              sb.append(",startTime:\"" + meeting.element("startTime").getText() + "\"");
              sb.append(",endTime:\"" + meeting.element("endTime").getText() + "\"");
              sb.append(",status:\"" + meeting.element("status").getText() + "\"");
              sb.append(",subject:\"" + meeting.element("subject").getText() + "\"");
              if(hostName.equals(userName))
                sb.append(",state:\"0\"");
              else
                sb.append(",state:\"1\"");
              sb.append("}");
              count++;
            }
          }
          sb.append("]}");
        }
      }
      else{
        throw new Exception();
      }
    } catch(Exception e){
      throw e;
    }
    return sb.toString().trim();
  }
 
  public String getVideoMeetingListInfo(Map request, Connection dbConn, YHPerson person, String contexPathAll) throws Exception  {
    
    Map map = getUsernameAndPassword(dbConn, person);
    String userName = (String)map.get("userName");
    StringBuffer sb = null;
    try{
      Map ipAndPort = getIpAndPort(contexPathAll);
      String ip = (String)ipAndPort.get("ip");
      String port = (String)ipAndPort.get("port");
      String url = "http://" + ip + ":" + port + "/integration/xml";
      String strXML = "<?xml version='1.0' encoding='UTF-8'?>"
                    + "<Message><header>"
                    + "<action>" + "listSummaryMeeting" + "</action>"
                    + "<service>" + "meeting" + "</service>"
                    + "<type>" + "xml" + "</type>"
                    + "<userName>" + "admin" + "</userName>"
                    + "<password>" + "admin" + "</password>"
                    + "</header>"
                    + "<body>"
                    + "<timeZoneId>" + "21" + "</timeZoneId>"
                    + "</body>"
                    + "</Message>";
      
      String reXML = YHVideoLogic.send(url,strXML);

      Document doc = DocumentHelper.parseText(reXML);
      Element root = doc.getRootElement();
      Element header = root.element("header");
      Element result = header.element("result");
      if(result.getText().equals("SUCCESS")){
        Element body = root.element("body");
        if(body == null){
          sb = new StringBuffer("{totalRecord:0,pageData:[]}");
        }
        else{
          Element meetings = body.element("meetings");
          Iterator iterator = meetings.elementIterator();
          List list = meetings.elements();
          
          sb = new StringBuffer("{totalRecord:");
          sb.append(list.size());
          sb.append(",pageData:[");
          int count = 0;
          while(iterator.hasNext()){
            Element meeting = (Element)iterator.next();
            if(meeting.getName().equals("meeting")){ 
              String confKey = meeting.element("confKey").getText();
              
              strXML = "<?xml version='1.0' encoding='UTF-8'?>"
                     + "<Message>"
                     + "<header>"
                     + "<action>listSummaryAttendee</action>"
                     + "<service>meeting</service>"
                     + "<type>xml</type>"
                     + "<userName>admin</userName>"
                     + "<password>admin</password>"
                     + "</header>"
                     + "<body>"
                     + "<startFrom>0</startFrom>"
                     + "<maximumNum>10</maximumNum>"
                     + "<confKey>" + confKey + "</confKey>"
                     + "</body>"
                     + "</Message>";
              
              
              reXML = YHVideoLogic.send(url,strXML);
              Document doc1 = DocumentHelper.parseText(reXML);
              Element root1 = doc1.getRootElement();
              Element body1 = root1.element("body");
              Element attendees = body1.element("attendees");
              Iterator iterator1 = attendees.elementIterator();
              String attendeesSeqId = "";
              while(iterator1.hasNext()){
                Element attendee = (Element)iterator1.next();
                if(attendee.element("name").getName().equals("name")){ 
                  attendeesSeqId = attendeesSeqId + attendee.element("name").getText() + ",";
                }
              }
              String temp = ","+attendeesSeqId;
              if(temp.indexOf("," + person.getSeqId() + ",") == -1 && (YHUtility.isNullorEmpty(userName) || !userName.equals(meeting.element("hostName").getText()))){
                continue;
              }
              
              if(count != 0) {
                sb.append(",");
              }
              sb.append("{confKey:\"" + meeting.element("confKey").getText() + "\"");
              sb.append(",confType:\"" + meeting.element("confType").getText() + "\"");
              sb.append(",duringTime:\"" + meeting.element("duringTime").getText() + "\"");
              sb.append(",hostName:\"" + meeting.element("hostName").getText() + "\"");
              sb.append(",startTime:\"" + meeting.element("startTime").getText() + "\"");
              sb.append(",endTime:\"" + meeting.element("endTime").getText() + "\"");
              sb.append(",status:\"" + meeting.element("status").getText() + "\"");
              sb.append(",subject:\"" + meeting.element("subject").getText() + "\"");
              sb.append(",attendees:\"" + attendeesSeqId + "\"");
              sb.append("}");
              count++;
            }
          }
          sb.append("]}");
          if(count == 0) {return "{totalRecord:0,pageData:[]}";}
        }
      }
      else{
        throw new Exception();
      }
    } catch(Exception e){
      throw e;
    }
    return sb.toString().trim();
  }
  
  public String doStartVideoMeeting(String confKey, String meetingPwd, Connection dbConn, YHPerson person, String contexPathAll) throws Exception  {
    Map map = getUsernameAndPassword(dbConn, person);
    String userName = (String)map.get("userName");
    String password = (String)map.get("password");
    
    if(YHUtility.isNullorEmpty(userName) || YHUtility.isNullorEmpty(password)){
      return "{data:\"empty\"}";
    }
    
    StringBuffer sb = new StringBuffer();
    try{
      Map ipAndPort = getIpAndPort(contexPathAll);
      String ip = (String)ipAndPort.get("ip");
      String port = (String)ipAndPort.get("port");
      String url = "http://" + ip + ":" + port + "/integration/xml";
      String strXML = "<?xml version='1.0' encoding='UTF-8'?>"
                    + "<Message><header>"
                    + "<action>" + "startMeeting" + "</action>"
                    + "<service>" + "meeting" + "</service>"
                    + "<type>" + "xml" + "</type>"
                    + "<userName>" + userName + "</userName>"
                    + "<password>" + password + "</password>"
                    + "</header>"
                    + "<body>"
                    + "<hostName>" + userName + "</hostName>"
                    + "<confKey>" + confKey + "</confKey>"
                    + "<meetingPwd>" + meetingPwd + "</meetingPwd>"
                    + "<email>satrs111@foxmail.com</email>"
                    + "<webBaseUrl>" + "http://" + ip + ":" + port + "</webBaseUrl>"
                    + "</body>"
                    + "</Message>";
      
      String reXML = YHVideoLogic.send(url,strXML);
      Document doc = DocumentHelper.parseText(reXML);
      Element root = doc.getRootElement();
      Element header = root.element("header");
      Element result = header.element("result");
      Element exceptionID = header.element("exceptionID");
      if(result.getText().equals("SUCCESS")){
        Element body = root.element("body");
        Element ciURL = body.element("ciURL");
        Element token = body.element("token");
        sb.append("{ciURL:\"" + ciURL.getText() + "\",token:\"" + token.getText() + "\"}");       
        
        strXML = "<?xml version='1.0' encoding='UTF-8'?>"
               + "<Message>"
               + "<header>"
               + "<action>" + "listSummaryAttendee" + "</action>"
               + "<service>" + "meeting" + "</service>"
               + "<type>" + "xml" + "</type>"
               + "<userName>" + userName + "</userName>"
               + "<password>" + password + "</password>"
               + "</header>"
               + "<body>"
               + "<confKey>" + confKey + "</confKey>"
               + "</body>"
               + "</Message>";
         reXML = YHVideoLogic.send(url,strXML);
         doc = DocumentHelper.parseText(reXML);
         root = doc.getRootElement();
         header = root.element("header");
         result = header.element("result");
         exceptionID = header.element("exceptionID");
         body = root.element("body");
         Element attendees = body.element("attendees");
         Iterator iterator = attendees.elementIterator();
         String attendeesSeqId = "";
         while(iterator.hasNext()){
           Element attendee = (Element)iterator.next();
           if(attendee.element("name").getName().equals("name") && !YHUtility.isNullorEmpty(attendee.element("name").getText())){ 
             attendeesSeqId = attendeesSeqId + attendee.element("name").getText() + ",";
           }
         }
        String remindUrl = "/subsys/oa/netmeeting/video/index.jsp";
        String smsContent = "请加入视频会议！会议号：" + confKey + "，密码：" + meetingPwd;
        // 短信提醒
        this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), attendeesSeqId, "99", remindUrl, new Date());
      }
      else if(exceptionID != null && exceptionID.getText().equals("0x0604006")){
        sb.append("{error:\"密码错误！\"}");       
      }
      else if(exceptionID != null && exceptionID.getText().equals("0x0000008")){
        return "{data:\"error\"}";   
      }
      else if(exceptionID != null && exceptionID.getText().equals("0x0604005")){
        return "{error:\"会议必须由创建者启动！\"}";   
      }
      else{
        throw new Exception();
      }
    } catch(Exception e){
      throw e;
    }
    return sb.toString().trim();
  }
  
  /**
   * 新建视频会议
   * 
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public String setNewVideoMeetingValueLogic(Connection dbConn, Map map, YHPerson person, String contexPathAll) throws Exception {
    Map map1 = getUsernameAndPassword(dbConn, person);
    String userName = (String)map1.get("userName");
    String password = (String)map1.get("password");
    
    if(YHUtility.isNullorEmpty(userName) || YHUtility.isNullorEmpty(password)){
      return "empty";
    }
    String subject = ((String[])map.get("subject"))[0];
    String conferencePattern = ((String[])map.get("conferencePattern"))[0];
    String passwd = ((String[])map.get("password"))[0];
    String startDateStr = ((String[])map.get("startDate"))[0];
    String endDateStr = ((String[])map.get("endDate"))[0];
    String hostId = ((String[])map.get("hostId"))[0];
    String attendees = ((String[])map.get("attendees"))[0];
    String agenda = "";
    if(map.get("agenda") != null)
      agenda = ((String[])map.get("agenda"))[0];

    startDateStr = startDateStr.replace(" ", "T") + ":00";
    endDateStr = endDateStr.replace(" ", "T") + ":00";
    try{
      String attendee[] = attendees.split(",");
      String attendeeXml = "";
      for(String seqId : attendee){
        attendeeXml = attendeeXml + "  <attendee>"
                                  + "    <name>" + seqId + "</name>"
                                  + "  </attendee>";
      }
      
      Map ipAndPort = getIpAndPort(contexPathAll);
      String ip = (String)ipAndPort.get("ip");
      String port = (String)ipAndPort.get("port");
      String url = "http://" + ip + ":" + port + "/integration/xml";
      String strXML = "<?xml version='1.0' encoding='UTF-8'?>"
                    + "<Message>"
                    + "<header>"
                    + "<action>" + "createReserveMeeting" + "</action>"
                    + "<service>" + "meeting" + "</service>"
                    + "<type>" + "xml" + "</type>"
                    + "<userName>" + userName + "</userName>"
                    + "<password>" + password + "</password>"
                    + "</header>"
                    + "<body>"
                    + "<subject>" + subject + "</subject>"
                    + "<startTime>" + startDateStr + "</startTime>"
                    + "<endTime>" + endDateStr + "</endTime>"  
                    + "<timeZoneId>45</timeZoneId>"
                    + "<attendeeAmount>" + attendee.length + "</attendeeAmount>"
                    + "<hostName>" + userName + "</hostName>"   
                    + "<creator>" + userName + "</creator>"
                    + "<openType>true</openType> "  
                    + "<passwd>" + passwd + "</passwd>"
                    + "<conferencePattern>" + conferencePattern + "</conferencePattern>" 
                    + "<agenda>" + agenda + "</agenda>"   
                    + "<mailTemplateLocal>zh_CN</mailTemplateLocal>"
                    + "<webBaseUrl>" + "http://" + ip + ":" + port + "</webBaseUrl>"
                    + "<attendees>"
                    + attendeeXml
                    + "</attendees>"
                    + "</body>"
                    + "</Message>";
      
      String reXML = YHVideoLogic.send(url,strXML);
      Document doc = DocumentHelper.parseText(reXML);
      Element root = doc.getRootElement();
      Element header = root.element("header");
      Element exceptionID = header.element("exceptionID");
      if(exceptionID != null && exceptionID.getText().equals("0x0000008")){
        return  "error";
      }
      Element result = header.element("result");
      if(result == null || !result.getText().equals("SUCCESS")){
        throw new Exception();
      }
    } catch (Exception e) {
      throw e;
    }
    return null;
  }
  
  /**
   * 取消会议（删除会议）
   * 
   * @param dbConn
   * @param seqIdStr
   * @throws Exception
   */
  public String deleteMeetingLogic(String confKey, Connection dbConn, YHPerson person, String contexPathAll) throws Exception {
    Map map1 = getUsernameAndPassword(dbConn, person);
    String userName = (String)map1.get("userName");
    String password = (String)map1.get("password");
    
    if(YHUtility.isNullorEmpty(userName) || YHUtility.isNullorEmpty(password)){
      return "{data:\"empty\"}";
    }
    Map ipAndPort = getIpAndPort(contexPathAll);
    String ip = (String)ipAndPort.get("ip");
    String port = (String)ipAndPort.get("port");
    String url = "http://" + ip + ":" + port + "/integration/xml";
    String strXML = "<?xml version='1.0' encoding='UTF-8'?>"
                  + "<Message>"
                  + "<header>"
                  + "<action>" + "deleteMeeting" + "</action>"
                  + "<service>" + "meeting" + "</service>"
                  + "<type>" + "xml" + "</type>"
                  + "<userName>" + userName + "</userName>"
                  + "<password>" + password + "</password>"
                  + "</header>"
                  + "<body>"
                  + "<confKey>" + confKey + "</confKey>"
                  + "<webBaseUrl>" + "http://" + ip + ":" + port +"</webBaseUrl>"
                  + "</body>"
                  + "</Message>";
    String reXML = YHVideoLogic.send(url,strXML);
    Document doc = DocumentHelper.parseText(reXML);
    Element root = doc.getRootElement();
    Element header = root.element("header");
    Element exceptionID = header.element("exceptionID");
    if(exceptionID != null && exceptionID.getText().equals("0x0000008")){
      return  "{data:\"error\"}";
    }
    Element result = header.element("result");
    if(result == null || !result.getText().equals("SUCCESS")){
      throw new Exception();
    }
    return "{data:\"\"}";
  }
  
  public String getVideoMeetingInfo(String confKey, Connection dbConn, YHPerson person, String contexPathAll) throws Exception{
    Map map1 = getUsernameAndPassword(dbConn, person);
    String userName = (String)map1.get("userName");
    String password = (String)map1.get("password");
    
    if(YHUtility.isNullorEmpty(userName) || YHUtility.isNullorEmpty(password)){
      return "{data:\"empty\"}";
    }
    Map ipAndPort = getIpAndPort(contexPathAll);
    String ip = (String)ipAndPort.get("ip");
    String port = (String)ipAndPort.get("port");
    String url = "http://" + ip + ":" + port + "/integration/xml";
    String strXML = "<?xml version='1.0' encoding='UTF-8'?>"
                  + "<Message>"
                  + "<header>"
                  + "<action>" + "readMeeting" + "</action>"
                  + "<service>" + "meeting" + "</service>"
                  + "<type>" + "xml" + "</type>"
                  + "<userName>" + userName + "</userName>"
                  + "<password>" + password + "</password>"
                  + "</header>"
                  + "<body>"
                  + "<confKey>" + confKey + "</confKey>"
                  + "</body>"
                  + "</Message>";
    String reXML = YHVideoLogic.send(url,strXML);
    Document doc = DocumentHelper.parseText(reXML);
    Element root = doc.getRootElement();
    Element header = root.element("header");
    Element result = header.element("result");
    Element exceptionID = header.element("exceptionID");
    if(exceptionID != null && exceptionID.getText().equals("0x0000008")){
      return  "{data:\"error\"}";
    }
    if(result == null || !result.getText().equals("SUCCESS")){
      throw new Exception();
    }
    Element body = root.element("body");
    StringBuffer sb = new StringBuffer();
    sb.append("{agenda:\"" + body.element("agenda").getText() + "\"");
    sb.append(",attendeeAmount:\"" + body.element("attendeeAmount").getText() + "\"");
    sb.append(",confKey:\"" + body.element("confKey").getText() + "\"");
    sb.append(",conferencePattern:\"" + body.element("conferencePattern").getText() + "\"");
    sb.append(",conferenceType:\"" + body.element("conferenceType").getText() + "\"");
    sb.append(",duration:\"" + body.element("duration").getText() + "\"");
    sb.append(",effectiveTime:\"" + body.element("effectiveTime").getText() + "\"");
    sb.append(",endHourMinute:\"" + body.element("endHourMinute").getText() + "\"");
    sb.append(",endTime:\"" + body.element("endTime").getText() + "\"");
    sb.append(",hostName:\"" + body.element("hostName").getText() + "\"");
    sb.append(",openType:\"" + body.element("openType").getText() + "\"");
    sb.append(",startHourMinute:\"" + body.element("startHourMinute").getText() + "\"");
    sb.append(",startTime:\"" + body.element("startTime").getText() + "\"");
    sb.append(",subject:\"" + body.element("subject").getText() + "\"");
    
    strXML = "<?xml version='1.0' encoding='UTF-8'?>"
           + "<Message>"
           + "<header>"
           + "<action>" + "listSummaryAttendee" + "</action>"
           + "<service>" + "meeting" + "</service>"
           + "<type>" + "xml" + "</type>"
           + "<userName>" + userName + "</userName>"
           + "<password>" + password + "</password>"
           + "</header>"
           + "<body>"
           + "<confKey>" + confKey + "</confKey>"
           + "</body>"
           + "</Message>";
    reXML = YHVideoLogic.send(url,strXML);
    doc = DocumentHelper.parseText(reXML);
    root = doc.getRootElement();
    header = root.element("header");
    result = header.element("result");
    exceptionID = header.element("exceptionID");
    if(exceptionID != null && exceptionID.getText().equals("0x0000008")){
      return  "{data:\"error\"}";
    }
    if(result == null || !result.getText().equals("SUCCESS")){
      throw new Exception();
    }
    body = root.element("body");
    Element attendees = body.element("attendees");
    Iterator iterator = attendees.elementIterator();
    String attendeesSeqId = "";
    while(iterator.hasNext()){
      Element attendee = (Element)iterator.next();
      if(attendee.element("name").getName().equals("name") && !YHUtility.isNullorEmpty(attendee.element("name").getText())){ 
        attendeesSeqId = attendeesSeqId + attendee.element("name").getText() + ",";
      }
    }
    sb.append(",attendees:\"" + attendeesSeqId.substring(0, attendeesSeqId.length()-1) + "\"}");
    return sb.toString();
  }
  
  @SuppressWarnings("static-access")
  public String updateTransferInfoLogic(Connection dbConn, Map map, YHPerson person, String contexPathAll) throws Exception {
    
    Map map1 = getUsernameAndPassword(dbConn, person);
    String userName = (String)map1.get("userName");
    String password = (String)map1.get("password");
    
    if(YHUtility.isNullorEmpty(userName) || YHUtility.isNullorEmpty(password)){
      return "empty";
    }
    String confKey = ((String[])map.get("confKey"))[0];
    String subject = ((String[])map.get("subject"))[0];
    String conferencePattern = ((String[])map.get("conferencePattern"))[0];
    String passwd = ((String[])map.get("password"))[0];
    String startDateStr = ((String[])map.get("startDate"))[0];
    String endDateStr = ((String[])map.get("endDate"))[0];
    String hostId = ((String[])map.get("hostId"))[0];
    String attendees = ((String[])map.get("attendees"))[0];
    String agenda = "";
    if(map.get("agenda") != null)
      agenda = ((String[])map.get("agenda"))[0];
    
    startDateStr = startDateStr.replace(" ", "T") + ":00";
    endDateStr = endDateStr.replace(" ", "T") + ":00";
    try{
      String attendee[] = attendees.split(",");
      String attendeeXml = "";
      for(String seqId : attendee){
        if(!YHUtility.isNullorEmpty(seqId))
          attendeeXml = attendeeXml + "  <attendee>"
                                    + "    <name>" + seqId + "</name>"
                                    + "  </attendee>";
      }
      Map ipAndPort = getIpAndPort(contexPathAll);
      String ip = (String)ipAndPort.get("ip");
      String port = (String)ipAndPort.get("port");
      String url = "http://" + ip + ":" + port + "/integration/xml";
      String strXML = "<?xml version='1.0' encoding='UTF-8'?>"
                    + "<Message>"
                    + "<header>"
                    + "<action>" + "updateReserveMeeting" + "</action>"
                    + "<service>" + "meeting" + "</service>"
                    + "<type>" + "xml" + "</type>"
                    + "<userName>" + userName + "</userName>"
                    + "<password>" + password + "</password>"
                    + "</header>"
                    + "<body>"
                    + "<confKey>" + confKey + "</confKey>"
                    + "<subject>" + subject + "</subject>"
                    + "<startTime>" + startDateStr + "</startTime>"
                    + "<endTime>" + endDateStr + "</endTime>"  
                    + "<timeZoneId>45</timeZoneId>"
                    + "<attendeeAmount>" + attendee.length + "</attendeeAmount>"
                    + "<hostName>" + userName + "</hostName>"   
                    + "<creator>" + userName + "</creator>"
                    + "<openType>true</openType> "  
                    + "<passwd>" + passwd + "</passwd>"
                    + "<conferencePattern>" + conferencePattern + "</conferencePattern>" 
                    + "<agenda>" + agenda + "</agenda>"   
                    + "<mailTemplateLocal>zh_CN</mailTemplateLocal>"
                    + "<webBaseUrl>" + "http://" + ip + ":" + port + "</webBaseUrl>"
                    + "<attendees>"
                    + attendeeXml
                    + "</attendees>"
                    + "</body>"
                    + "</Message>";
      
      String reXML = YHVideoLogic.send(url,strXML);
      Document doc = DocumentHelper.parseText(reXML);
      Element root = doc.getRootElement();
      Element header = root.element("header");
      Element result = header.element("result");
      Element exceptionID = header.element("exceptionID");
      if(exceptionID != null && exceptionID.getText().equals("0x0000008")){
        return  "error";
      }
      if(result == null || !result.getText().equals("SUCCESS")){
        throw new Exception();
      }
    } catch (Exception e) {
      throw e;
    }
    return null;
  }
  
  
  public String doJoinVideoMeeting(String confKey, String password, YHPerson person, String contexPathAll) throws Exception  {
    StringBuffer sb = new StringBuffer();
    try{
      Map ipAndPort = getIpAndPort(contexPathAll);
      String ip = (String)ipAndPort.get("ip");
      String port = (String)ipAndPort.get("port");
      String url = "http://" + ip + ":" + port + "/integration/xml";
      String strXML = "<?xml version='1.0' encoding='UTF-8'?>"
                    + "<Message><header>"
                    + "<action>" + "joinMeeting" + "</action>"
                    + "<service>" + "meeting" + "</service>"
                    + "<type>" + "xml" + "</type>"
                    + "<userName>" + "admin" + "</userName>"
                    + "<password>" + "admin" + "</password>"
                    + "</header>"
                    + "<body>"
                    + "<attendeeName>" + person.getUserName() + "</attendeeName>"
                    + "<confKey>" + confKey + "</confKey>"
                    + "<meetingPwd>" + password + "</meetingPwd>"
                    + "<email>satrs111@foxmail.com</email>"
                    + "<webBaseUrl>" + "http://" + ip + ":" + port + "</webBaseUrl>"
                    + "</body>"
                    + "</Message>";
      String reXML = YHVideoLogic.send(url,strXML);
      Document doc = DocumentHelper.parseText(reXML);
      Element root = doc.getRootElement();
      Element header = root.element("header");
      Element result = header.element("result");
      Element exceptionID = header.element("exceptionID");
      if(result.getText().equals("SUCCESS")){
        Element body = root.element("body");
        Element ciURL = body.element("ciURL");
        Element token = body.element("token");
        sb.append("{ciURL:\"" + ciURL.getText() + "\",token:\"" + token.getText() + "\"}");       
      }
      else if(exceptionID.getText().equals("0x0604006")){
        sb.append("{error:\"密码错误！\"}");       
      }
      else{
        throw new Exception();
      } 
    } catch(Exception e){
      throw e;
    }
    return sb.toString().trim();
  }
  
  public Map getUsernameAndPassword(Connection dbConn, YHPerson person) throws Exception  {
    String userName = "";
    String password = "";
    Map map = new HashMap();
    String sql = " select RED_USERNAME,RED_PASSWORD "
               + " from oa_conference_manager "
               + " where user_ID = " + person.getSeqId();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()) {
        userName = rs.getString("RED_USERNAME");
        password = rs.getString("RED_PASSWORD");
        map.put("userName", userName);
        map.put("password", password);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return map;
  }
  
  public String queryMeetingList(Connection dbConn, Map request, YHPerson person, String contexPathAll) throws Exception  {
    
    Map map = getUsernameAndPassword(dbConn, person);
    String userName = (String)map.get("userName");
    String password = (String)map.get("password");
    
    String startDate = ((String[])request.get("startDate"))[0];
    startDate = startDate.replace(" ", "T") + ":00";
    String endDate = ((String[])request.get("endDate"))[0];
    endDate = endDate.replace(" ", "T") + ":00";
    
    StringBuffer sb = null;
    try{
      Map ipAndPort = getIpAndPort(contexPathAll);
      String ip = (String)ipAndPort.get("ip");
      String port = (String)ipAndPort.get("port");
      String url = "http://" + ip + ":" + port + "/integration/xml";
      String strXML = "<?xml version='1.0' encoding='UTF-8'?>"
                    + "<Message><header>"
                    + "<action>" + "listSummaryMeeting41" + "</action>"
                    + "<service>" + "meeting" + "</service>"
                    + "<type>" + "xml" + "</type>"
                    + "<userName>" + userName + "</userName>"
                    + "<password>" + password + "</password>"
                    + "</header>"
                    + "<body>"
                    + "<startFrom>0</startFrom>"
                    + "<maximumNum>10</maximumNum>"
                    + "<openType>true</openType>"
                    + "<startTime>" + startDate + "</startTime>"
                    + "<endTime>" + endDate + "</endTime>"
                    + "<timeZoneId>" + "21" + "</timeZoneId>"
                    + "</body>"
                    + "</Message>";

      String reXML = YHVideoLogic.send(url,strXML);

      Document doc = DocumentHelper.parseText(reXML);
      Element root = doc.getRootElement();
      Element header = root.element("header");
      Element exceptionID = header.element("exceptionID");
      if(exceptionID != null && exceptionID.getText().equals("0x0000008")){
        return  "error";
      }
      Element result = header.element("result");
      if(result.getText().equals("SUCCESS")){
        Element body = root.element("body");
        if(body == null){
          sb = new StringBuffer("{totalRecord:0,pageData:[]}");
        }
        else{
          Element meetings = body.element("meetings");
          Iterator iterator = meetings.elementIterator();
          List list = meetings.elements();
          
          sb = new StringBuffer("{totalRecord:");
          sb.append(list.size());
          sb.append(",pageData:[");
          int count = 0;
          while(iterator.hasNext()){
            Element meeting = (Element)iterator.next();
            if(meeting.getName().equals("meeting")){ 
              if(count != 0) {
                sb.append(",");
              }
              sb.append("{confKey:\"" + meeting.element("confKey").getText() + "\"");
              sb.append(",confType:\"" + meeting.element("confType").getText() + "\"");
              sb.append(",duringTime:\"" + meeting.element("duringTime").getText() + "\"");
              String hostName = meeting.element("hostName").getText();
              sb.append(",hostName:\"" + hostName + "\"");
              sb.append(",startTime:\"" + meeting.element("startTime").getText() + "\"");
              sb.append(",endTime:\"" + meeting.element("endTime").getText() + "\"");
              sb.append(",status:\"" + meeting.element("status").getText() + "\"");
              sb.append(",subject:\"" + meeting.element("subject").getText() + "\"");
              if(hostName.equals(userName))
                sb.append(",state:\"0\"");
              else
                sb.append(",state:\"1\"");
              sb.append("}");
              count++;
            }
          }
          sb.append("]}");
        }
      }
      else{
        throw new Exception();
      }
    } catch(Exception e){
      throw e;
    }
    return sb.toString().trim();
  }
  
  public void addManager(Connection dbConn,YHVideoMeetingManager manager) throws Exception{
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, manager);
  }
  
  public void updateManager(Connection dbConn,YHVideoMeetingManager manager) throws Exception{
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, manager);
  }
  
  public void deleteManager(Connection dbConn,String seqId) throws Exception{
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHVideoMeetingManager.class, Integer.parseInt(seqId));
  }
 
  public String getParameters(String contexPath) throws Exception{
    BufferedReader reader = new BufferedReader(new FileReader(contexPath + "subsys"+ File.separator  +"oa"+ File.separator  +"netmeeting"+ File.separator  +"video"+ File.separator  +"set"+ File.separator  +"config.properties"));
    String temp = null;
    String ip = "";
    String port = "";
    int count = 1;
    while((temp = reader.readLine()) != null){
      if(count == 1){
        ip = temp.substring(3, temp.length());
        count++;
        continue;
      }
      if(count == 2){
        port = temp.substring(5, temp.length());
        count++;
        break;
      }
    }
    reader.close();
    return "{ip:\"" + ip + "\",port:\"" + port + "\"}";
  }
  
  public void setParameters(String ip, String port, String contexPathAll) throws Exception{
    byte[] msg = ("ip=" + ip + "\nport=" + port).getBytes();
    storBytes2File(contexPathAll + "subsys"+ File.separator  +"oa"+ File.separator  +"netmeeting"+ File.separator  +"video"+ File.separator  +"set"+ File.separator  +"config.properties", msg, 0, msg.length, false);
  }
  
  public Map getIpAndPort(String contexPathAll) throws Exception{
    BufferedReader reader = new BufferedReader(new FileReader(contexPathAll + "subsys"+ File.separator  +"oa"+ File.separator  +"netmeeting"+ File.separator  +"video"+ File.separator  +"set"+ File.separator  +"config.properties"));
    String temp = null;
    String ip = "";
    String port = "";
    int count = 1;
    while((temp = reader.readLine()) != null){
      if(count == 1){
        ip = temp.substring(3, temp.length());
        count++;
        continue;
      }
      if(count == 2){
        port = temp.substring(5, temp.length());
        count++;
        break;
      }
    }
    reader.close();
    Map map = new HashMap();
    map.put("ip", ip);
    map.put("port", port);
    return map;
  }
  
  /**
   * 获取单位员工用户名称
   * 
   * @param conn
   * @param userIdStr
   * @return
   * @throws Exception
   */
  public String getUserNameLogic(Connection conn, String userIdStr) throws Exception {
    if (YHUtility.isNullorEmpty(userIdStr)) {
      userIdStr = "-1";
    }
    if (userIdStr.endsWith(",")) {
      userIdStr = userIdStr.substring(0, userIdStr.length() - 1);
    }
    String result = "";
    String sql = " select USER_NAME from PERSON where SEQ_ID IN (" + userIdStr + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        String toId = rs.getString(1);
        if (!"".equals(result)) {
          result += ",";
        }
        result += toId;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 短信提醒(带时间)
   * 
   * @param conn
   * @param content
   * @param fromId
   * @param toId
   * @param type
   * @param remindUrl
   * @param sendDate
   * @throws Exception
   */
  public static void doSmsBackTime(Connection conn, String content, int fromId, String toId, String type, String remindUrl, Date sendDate)
      throws Exception {
    YHSmsBack sb = new YHSmsBack();
    sb.setContent(content);
    sb.setFromId(fromId);
    sb.setToId(toId);
    sb.setSmsType(type);
    sb.setRemindUrl(remindUrl);
    sb.setSendDate(sendDate);
    YHSmsUtil.smsBack(conn, sb);
  }
  
  /**
   *  把字节数组保存到文件
   * @param fileName
   * @param srcBuf
   * @throws Exception
   */
  public static void storBytes2File(String fileName,
      byte[] srcBuf, int offset, int length, boolean endWrite) throws Exception {
    OutputStream outs = null;
    try {
      File outFile = new File(fileName);
      File outDir = outFile.getParentFile();
      if (!outDir.exists()) {
        outDir.mkdirs();
      }
      if (!outFile.exists()) {
        outFile.createNewFile();
      }
      if (!outFile.canWrite()) {
        outFile.setWritable(true);
      }
      outs = new FileOutputStream(outFile,endWrite);
      
      outs.write(srcBuf, offset, length);
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (outs != null) {
          outs.close();
        }
      }catch(Exception ex) {
        //System.out.println(ex.getMessage());
      }      
    }
  }
}
