package yh.subsys.jtgwjh.notifyManage.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.seclog.logic.YHSecLogUtil;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.subsys.jtgwjh.notifyManage.data.YHJhNotify;
import yh.user.api.core.db.YHDbconnWrap;

import com.agile.zip.CZipInputStream;
import com.agile.zip.ZipEntry;


public class YHNotifyZipRarFile {
  private static String unrarCmd = "C:\\Program Files\\WinRAR\\UnRar x "; // 默认安装路径
  /**
   * 只支持ZIP附件
   * 
   * @param filePath ： 附件路径
   * @param fromUnit : 发送单位
   *  fromUnit:发送单位 type:1解压普通附件 0：解压XML文件
   *          guid:ESB数据交互平台发送的唯一表示
   * @param webrootPath
   *          文件保存目录
   * @throws Exception
   */
  public static void unZipFileXml(String filePath, String fromUnit,String guid) throws Exception {
    String savePath = YHSysProps.getAttachPath()  + "/notifyRecive";//接收附件存放路径
    Connection dbConn = null;
    YHDbconnWrap dbUtil = new YHDbconnWrap();   
    dbConn = dbUtil.getSysDbConn();
    File file = new File(filePath);
    InputStream rarFile = new FileInputStream(file);
    if (rarFile == null) {
      return ;
    }
    //解压普通附件
    String[] attachStr = unZipFile( filePath,  savePath,  dbConn, fromUnit, guid) ;
    
    CZipInputStream zip = new CZipInputStream(rarFile);// 支持中文目录
    ZipEntry entry;
    String xmlFile = "";//公告XML文件名称
    String upXMLPath = filePath;//
    while ((entry = zip.getNextEntry()) != null) {// 循环zip下的所有文件和目录
      String attName = entry.getName();
      if (YHUtility.isNullorEmpty(attName)) {
        continue;
      }
      File outFile = new File(savePath + "/" + attName);
      File outPath = outFile.getParentFile();
      if (!outPath.exists()) {
        outPath.mkdirs();
      }
      if (attName.startsWith("JHNOTIFY_")) {// 公文类型的XML
        xmlFile = attName;
        try {
          //
          InputStream in = null;
          //解压ZIP的公文XMl文件，并存放在ZIP的同一级路径
          File filePathP = new File(filePath);
          File PTem = filePathP.getParentFile();
          String outPathStr = PTem.getPath();
          upXMLPath = outPathStr + "/" + attName;
          outFile = new File(upXMLPath);
          outFile.createNewFile();
          FileOutputStream out = new FileOutputStream(outFile);
          int len = 0;
          byte[] buff = new byte[4096];
          while ((len = zip.read(buff)) != -1) {
            out.write(buff, 0, len);
          }
          zip.closeEntry();
          out.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      }
    }
    if (!YHUtility.isNullorEmpty(xmlFile)) {//解析xml文件
       parseXML(upXMLPath, dbConn, fromUnit, guid,attachStr);
    }
    System.out.println(xmlFile);
  }

  /**
   * 只支持ZIP附件
   * 
   * @param filePath:公文发送附件ZIP
   * @param savePath ： 将解压的附件保存路径
   *          文件元素名称
        fromUnit:发送单位 guid:ESB数据交互平台发送的唯一表示
   * @param attachStrArrys ： 公文XML返回的数据数组
   *         guid ： ESB 唯一标识
   * @throws Exception
   */
  public static String[] unZipFile(String filePath, String savePath, Connection dbConn,String fromUnit,String guid) throws Exception{
	  String[] attachStr = new String[2];
    File file = new File(filePath);   
    InputStream rarFile = new FileInputStream(file);
    if(rarFile == null){//如果没有附件了跳出
      return attachStr;
    }
    CZipInputStream zip=new CZipInputStream(rarFile);//支持中文目录
    ZipEntry entry;
    String upXMLPath = filePath;
     String attachIds = "";
     String attachNames = "";
    while ((entry = zip.getNextEntry()) != null) {//循环zip下的所有文件和目录
      String attName=entry.getName(); 
      if(YHUtility.isNullorEmpty(attName)){
        continue;
      }
      
      if (attName.startsWith("JHNOTIFY_")) {// 公告类型的XML
    	  continue;
      }
      
      if(attName.length()>33)
      {
    	  attName=attName.substring(33, attName.length());
      }
      File outFile = new File(savePath  + "/" + attName);
      File outPath = outFile.getParentFile();
      if (!outPath.exists()) {
        outPath.mkdirs();
      }
      String datePath = "";
      String attachmentName = attName;//isExist(list,attName);
      if(!YHUtility.isNullorEmpty(attachmentName) ){//如果存在
        //日期
        Calendar cld = Calendar.getInstance();
        int year = cld.get(Calendar.YEAR) % 100;
        int month = cld.get(Calendar.MONTH) + 1;
        String mon = month >= 10 ? month + "" : "0" + month;
        String hard = year + mon;
        String newFilePath = savePath  + "/" ;
        String attachId = "";
        String attachName = "";
        try {

          String rand = YHDiaryUtil.getRondom();
          String fileName = rand + "_" + attachmentName;
          while (YHDiaryUtil.getExist(newFilePath + hard, fileName)) {
            rand = YHDiaryUtil.getRondom();
            fileName = rand + "_" + attachmentName;
          }
          attachId = hard + "_" + rand;
          attachName = attachmentName;
          newFilePath = newFilePath + hard + "/" + fileName;
          outFile = new File(newFilePath);
          File outPathTemp = outFile.getParentFile();
          if (!outPathTemp.exists()) {
            outPathTemp.mkdirs();
          }
          outFile.createNewFile();
          InputStream in = null;
          FileOutputStream out =new FileOutputStream( outFile); 
            int len=0; 
            byte[] buff=new byte[4096]; 
            while((len = zip.read(buff))!=-1){ 
              out.write(buff, 0, len); 
            } 
            zip.closeEntry(); 
            out.close(); 
        } catch (IOException e) {
          e.printStackTrace();
        }
        if(!YHUtility.isNullorEmpty(attachIds)){
          attachIds =attachIds + "," + attachId ;
        }else{
          attachIds = attachId ;
        }
        if(!YHUtility.isNullorEmpty(attachNames)){
          attachNames = attachNames  + "*" + attachName ;
        }else{
          attachNames =   attachName ;
        }
      }
    }
    
    attachStr[0] = attachIds;
    attachStr[1] = attachNames;
    return attachStr;
}

  /**
   * 
   *  解析公告XML、并新建收文记录
   * upXMLPath: 临时公文XML存放路径
   * @param savePath
   *          :保存文件路径
   * @param fromUnit
   *          ：发送单位
   * @param dbConn
   *          ：数据库 fromUnit：发送单位 * guid:ESB数据交互平台发送的唯一标识
   * @throws Exception
   */
  public static String[] parseXML(String upXMLPath, Connection dbConn, String fromUnit, String guid, String[] attachStr) throws Exception {
    SAXReader saxReader = new SAXReader();
    File XMLFile = new File(upXMLPath);
    Document document = saxReader.read(XMLFile);
    Element root = document.getRootElement();
    YHJhNotify  receive = null;
    String attachIdStr = "";//附件Id
    String attachNameStr = "";//附件名称
    if (!YHUtility.isNullorEmpty(root.getName()) && root.getName().equals("body")) {
      receive = new YHJhNotify();
      List<Element> elements = root.elements();
      for (Element el : elements) {
        String elName = el.getName();
        String elData = (String) el.getData() == null ? "" : (String) el.getData();
        elData = elData.trim();
        if (elName.equalsIgnoreCase("title")) {
          receive.setTitle(elData);
        }
        else if (elName.equalsIgnoreCase("sendDatetime") && YHUtility.isDayTime(elData)) {
          receive.setSendDateTime(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",elData));
        }
        else if (elName.equalsIgnoreCase("createDate") && YHUtility.isDayTime(elData)) {
          receive.setSendDateTime(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",elData));
        }  
        else if (elName.equalsIgnoreCase("sendDeptName")) {
          receive.setSendDeptName(elData);
        }
        else if (elName.equalsIgnoreCase("content")) {
          receive.setContent(elData);
        }
        else if(elName.equalsIgnoreCase("sendDept")){
        	receive.setSendDept(elData);
        }
        else if(elName.equalsIgnoreCase("sendDeptName")){
        	receive.setSendDeptName(elData);
        }
        else if(elName.equalsIgnoreCase("receiveDept")){
        	receive.setReceiveDept(elData);
        }
        else if(elName.equalsIgnoreCase("receiveDeptName")){
        	receive.setReceiveDeptName(elData);
        }
        else if(elName.equalsIgnoreCase("userName")){
        	receive.setUserName(elData);
        }
        else if(elName.equalsIgnoreCase("userId")){
        	receive.setUserId(elData);
        }
      }
    }
    if (receive != null) {
      receive.setSendDeptName(fromUnit);
      receive.setGuid(guid);
      receive.setAttachmentId(attachStr[0]);
      receive.setAttachmentName(attachStr[1]);
      receive.setCreateDate(YHUtility.parseDate(YHUtility.getCurDateTimeStr()));
      receive.setPublish("2");
      //新建收文记录
      YHJhNotifyInfoLogic logic = new YHJhNotifyInfoLogic();
       logic.add(dbConn, receive);
       //获取request
       HttpServletRequest request = getWebserviceHttp();
       String IP = "";
       if(request != null ){
         IP = request.getRemoteAddr();
       }
       YHPerson user = new YHPerson();
       user.setSeqId(0);
       user.setUserName("系统");
       //系统日志
       YHSysLogLogic.addSysLog(dbConn, "62", "系统成功接收日志：" + receive.toString() ,0, IP);
    }
    attachStr[0] = attachIdStr;
    attachStr[1] = attachNameStr;
    return attachStr;
  }

  /*
   * 将数组转化为List
   * attachs：公文XML解析出的部分数据
   * 索引0：所有附件的Id字符串，已都好分割
   * 索引：所有附件的Name字符串
   * 普通附件转换
   */
  public static List<Map<String, String>> getparseStr(String[] attachs) {
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    String attachIds = attachs[0];
    String attachNames = attachs[1];
    if (!YHUtility.isNullorEmpty(attachIds) && !YHUtility.isNullorEmpty(attachNames)) {
      String[] attachIdArry = attachIds.split(",");
      String[] attachNameArry = attachNames.split("\\*");
      if (attachIdArry.length == attachNameArry.length) {
        for (int i = 0; i < attachNameArry.length; i++) {
          if(!YHUtility.isNullorEmpty(attachIdArry[i]) && !YHUtility.isNullorEmpty(attachNameArry[i])) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("attachId", attachIdArry[i]);
            map.put("attachName", attachNameArry[i]);
            list.add(map);
          }
         
        }
      }
    }
    return list;
  }

  /*
   * 将数组转化为List
   * 正文转换
   */
  public static Map<String,String> getparseMainStr(String[] attachs) {
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    String attachIds = attachs[2];
    String attachNames = attachs[3];
    Map<String, String> map = new HashMap<String, String>();
    if (!YHUtility.isNullorEmpty(attachIds)
        && !YHUtility.isNullorEmpty(attachNames)) {
      String[] attachIdArry = attachIds.split(",");
      String[] attachNameArry = attachNames.split("\\*");
      if (attachIdArry.length == attachNameArry.length) {
        for (int i = 0; i < attachNameArry.length; i++) {
          if(!YHUtility.isNullorEmpty(attachIdArry[i]) && !YHUtility.isNullorEmpty(attachNameArry[i])) {
           
            map.put("attachId", attachIdArry[i]);
            map.put("attachName", attachNameArry[i]);
            return map;
          }
         
        }
      }
    }
    return map;
  }


  /**
   * 利用webservice 获取request
   * @return
   */
  public static HttpServletRequest getWebserviceHttp(){
    MessageContext mc =  (MessageContext) org.apache.axis.MessageContext.getCurrentContext();;  
    HttpServletRequest request = (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);  
    return request;
  }
}