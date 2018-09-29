package yh.subsys.jtgwjh.docReceive.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUtility;
import yh.subsys.jtgwjh.docReceive.data.YHJhDocrecvInfo;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendFiles;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendInfo;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendTasks;
import yh.subsys.jtgwjh.docSend.logic.YHDocSendLogic;
import yh.user.api.core.db.YHDbconnWrap;

import com.agile.zip.CZipInputStream;
import com.agile.zip.ZipEntry;

public class YHUnZipRarFile {
  private static String unrarCmd = "C:\\Program Files\\WinRAR\\UnRar x "; // 默认安装路径

  /*
   * static { try { // 此处很重要：不能出现.dll后缀名，需要将chilkat.dll存放 // 在D:\Program
   * Files\Genuitec\Common\binary //
   * \com.sun.java.jdk.win32.x86_1.6.0.013\bin等目录下。 //
   * 不然报"no chilkat in java.library.path"错误 //System.loadLibrary("chilkat"); }
   * catch (UnsatisfiedLinkError e) { //
   * System.err.println("Native code library failed to load.\n" + e); //
   * System.exit(1); } }
   */

  /**
   * 只支持ZIP附件
   * 
   * @param filePath
   *          ： 附件路径
   * @param fromUnit
   *          : 发送单位 fromUnit:发送单位 type:1解压普通附件 0：解压XML文件 guid:ESB数据交互平台发送的唯一表示
   * @param webrootPath
   *          文件保存目录
   * @throws Exception
   */
  public static void unZipFileXml(String filePath, String fromUnit, String guid)
      throws Exception {
    String savePath = YHSysProps.getAttachPath() + "/docReceive";// 接收附件存放路径
    Connection dbConn = null;
    YHDbconnWrap dbUtil = new YHDbconnWrap();
    dbConn = dbUtil.getSysDbConn();
    File file = new File(filePath);
    InputStream rarFile = new FileInputStream(file);
    if (rarFile == null) {
      return;
    }
    CZipInputStream zip = new CZipInputStream(rarFile);// 支持中文目录
    ZipEntry entry;
    String xmlFile = "";// 公文XML文件名称
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
      if (attName.startsWith("JHDATA_")) {// 公文类型的XML
        xmlFile = attName;
        try {
          //
          InputStream in = null;
          // 解压ZIP的公文XMl文件，并存放在ZIP的同一级路径
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
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        break;
      }
    }
    if (!YHUtility.isNullorEmpty(xmlFile)) {// 如果存在公文XML
      String[] attachStr = parseXML(upXMLPath, savePath, dbConn, fromUnit, guid);
      unZipFile(filePath, savePath, dbConn, fromUnit, attachStr, guid);
    }
    // System.out.println(xmlFile);
  }

  /**
   * 只支持ZIP附件
   * 
   * @param filePath
   *          :公文发送附件ZIP
   * @param savePath
   *          ： 将解压的附件保存路径 文件元素名称 fromUnit:发送单位 guid:ESB数据交互平台发送的唯一表示
   * @param attachStrArrys
   *          ： 公文XML返回的数据数组 guid ： ESB 唯一标识
   * @throws Exception
   */
  public static void unZipFile(String filePath, String savePath,
      Connection dbConn, String fromUnit, String[] attachStrArrys, String guid)
      throws Exception {
    File file = new File(filePath);
    InputStream rarFile = new FileInputStream(file);
    if (rarFile == null) {// 如果没有附件了跳出
      return;
    }
    CZipInputStream zip = new CZipInputStream(rarFile);// 支持中文目录
    ZipEntry entry;
    String upXMLPath = filePath;
    List<Map<String, String>> list = getparseStr(attachStrArrys);// 普通附件list，包括正文
    Map<String, String> mainMap = getparseMainStr(attachStrArrys);// 正文Map

    String attachIds = "";
    String attachNames = "";
    String mainDocIdNew = "";
    String mainDocNameNew = "";
    String attachSizes = "";
    String seqIdMax = attachStrArrys[7];// 最大seqId
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
      String datePath = "";
      /*
       * if(list.size() > 0){//获取附件日期 for(int i = 0 ; i< list.size() ;
       * i++){//考虑附件不是在同一个日期内 Map<String,String> map = list.get(i); String
       * attaId = map.get("attachId") == null ? "" : map.get("attachId"); String
       * attaName = map.get("attachName"); if(!YHUtility.isNullorEmpty(attaId)
       * && attaId.split("_").length == 2 &&
       * attName.contains(attaId.split("_")[1])){ datePath =
       * attaId.split("_")[0]; break; } } }
       */
      String attachmentName = isExist(list, attName);
      if (!YHUtility.isNullorEmpty(attachmentName)) {// 如果存在
        // 日期
        Calendar cld = Calendar.getInstance();
        int year = cld.get(Calendar.YEAR) % 100;
        int month = cld.get(Calendar.MONTH) + 1;
        String mon = month >= 10 ? month + "" : "0" + month;
        String hard = year + mon;
        String newFilePath = savePath + "/";
        String attachId = "";
        String attachName = "";
        
        // 判断是否是正文
        /*
         * 利用点聚JNI控制打印份数、编号等
         */
        String attaMainId = mainMap.get("attachId");
        String attaMainName = mainMap.get("attachName");
        String dateMainPath = "";
        if (!YHUtility.isNullorEmpty(attaMainId) && !YHUtility.isNullorEmpty(attaMainName)) {
          dateMainPath = attaMainId.split("_")[0];
          if (attaMainId.split("_")[1].equals(attName.substring(0, 32))) {
            if (attaMainId.split("_")[1].equals(attName.substring(0, 32))) {
              int index = attachmentName.lastIndexOf(".");
              if(attachmentName.substring(index+1).equalsIgnoreCase("doc") || attachmentName.substring(index+1).equalsIgnoreCase("docx")){
                attachmentName = attachmentName.substring(0, index) + ".aip";
              }
              
              mainDocNameNew = attachmentName;
            }
          }
        }
        
        
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
          FileOutputStream out = new FileOutputStream(outFile);
          int len = 0;
          byte[] buff = new byte[4096];
          while ((len = zip.read(buff)) != -1) {
            out.write(buff, 0, len);
          }
        
          long tempLength =  outFile.length();
          if(YHUtility.isNullorEmpty(attachSizes)){
            attachSizes = tempLength + "";
          }else{
            attachSizes = attachSizes + "," + tempLength;
          }
          zip.closeEntry();
          out.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        if (!YHUtility.isNullorEmpty(attachIds)) {
          attachIds = attachIds + "," + attachId;
        } else {
          attachIds = attachId;
        }
        if (!YHUtility.isNullorEmpty(attachNames)) {
          attachNames = attachNames + "*" + attachName;
        } else {
          attachNames = attachName;
        }

        // 判断是否是正文
        /*
         * 利用点聚JNI控制打印份数、编号等
         */
     //   String attaMainId = mainMap.get("attachId");
       // String attaMainName = mainMap.get("attachName");
        String mainFilePath = savePath + "\\";
      //  String dateMainPath = "";
        if (!YHUtility.isNullorEmpty(attaMainId)
            && !YHUtility.isNullorEmpty(attaMainName)) {
          dateMainPath = attaMainId.split("_")[0];
          if (attaMainId.split("_")[1].equals(attName.substring(0, 32))) {
            mainDocIdNew = attachId;
            YHAipToJNI aipJni = new YHAipToJNI();
            aipJni.AipToJNI(newFilePath, attachStrArrys[4], attachStrArrys[5],
                attachStrArrys[6],attachStrArrys[8]);
          }
          // mainFilePath = mainFilePath + dateMainPath + "\\" +
          // attaMainId.split("_")[1] + "_" + attaMainName;

        }
      }

    }
    // 更新附件路径
    YHJhDocrecvInfo info = YHJhDocrecvInfoLogic.getById(dbConn, seqIdMax);
    info.setSeqId(Integer.parseInt(seqIdMax));
    info.setAttachmentId(attachIds);
    info.setAttachmentName(attachNames);
    info.setMainDocId(mainDocIdNew);
    info.setMainDocName(mainDocNameNew);
    info.setAttachmentSize(attachSizes);
    YHJhDocrecvInfoLogic.updateNation(dbConn, info);
  }

  /**
   * 
   * 解析公文XML、并新建收文记录 upXMLPath: 临时公文XML存放路径
   * 
   * @param savePath
   *          :保存文件路径
   * @param fromUnit
   *          ：发送单位
   * @param dbConn
   *          ：数据库 fromUnit：发送单位 * guid:ESB数据交互平台发送的唯一标识
   * @throws Exception
   */
  public static String[] parseXML(String upXMLPath, String savePath,
      Connection dbConn, String fromUnit, String guid) throws Exception {
    SAXReader saxReader = new SAXReader();
    File XMLFile = new File(upXMLPath);
    Document document = saxReader.read(XMLFile);
    Element root = document.getRootElement();

    YHJhDocrecvInfo receive = null;
    String attachIdStr = "";// 普通附件Id
    String attachNameStr = "";// 普通附件名称
    String mDocId = "";// 正文Id
    String mDocName = "";// 正文附件、
    String printNum = "0";// 打印份数
    String startNo = "";// 开始编号
    String endNo = "";// 结束编号
    String isSign = "1";// 是否打印控制，0：没控制；1：已控制
    int seqIdMax = 0;
    String[] attachStr = new String[9];
    String taskId = "0";// 发送单位任务从表的seqId
    if (!YHUtility.isNullorEmpty(root.getName())
        && root.getName().equals("body")) {
      receive = new YHJhDocrecvInfo();
      List<Element> elements = root.elements();
      for (Element el : elements) {
        String elName = el.getName();
        String elData = (String) el.getData() == null ? "" : (String) el
            .getData();
        elData = elData.trim();
        if (elName.equalsIgnoreCase("seqId") && YHUtility.isInteger(elData)) {
          receive.setDocsendId(Integer.parseInt(elData));
        } else if (elName.equalsIgnoreCase("docTitle")) {
          receive.setDocTitle(elData);
        } else if (elName.equalsIgnoreCase("docType")) {
          receive.setDocType(elData);
        } else if (elName.equalsIgnoreCase("docKind")) {
          receive.setDocKind(elData);
        } else if (elName.equalsIgnoreCase("securityLevel")) {
          receive.setSecurityLevel(elData);
        } else if (elName.equalsIgnoreCase("securityTime")) {
          receive.setSecurityTime(elData);
        } else if (elName.equalsIgnoreCase("pageCount")
            && YHUtility.isInteger(elData)) {
          receive.setPageCount(Integer.parseInt(elData));
        } else if (elName.equalsIgnoreCase("docNo")) {
          receive.setDocNo(elData);
        } else if (elName.equalsIgnoreCase("attachCount")
            && YHUtility.isInteger(elData)) {
          receive.setAttachCount(Integer.parseInt(elData));
        } else if (elName.equalsIgnoreCase("remark")) {
          receive.setRemark(elData);
        } else if (elName.equalsIgnoreCase("sendDatetime")
            && YHUtility.isDayTime(elData)) {
          receive.setSendDatetime(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",
              elData));
        } else if (elName.equalsIgnoreCase("mainDocId")) {// 正文Id
          attachIdStr = attachIdStr + elData + ",";
        } else if (elName.equalsIgnoreCase("mainDocName")) {// 正文名称
          attachNameStr = attachNameStr + elData + "*";
        } else if (elName.equalsIgnoreCase("isSign")) {// 是否设置打印份数
          if (elData.endsWith("0")) {
            isSign = "0";
          }
        }else if(elName.equalsIgnoreCase("oaMainSend")){//主送
          receive.setOaMainSend(elData);
        }else if(elName.equalsIgnoreCase("oaCopySend")){//抄送
          receive.setOaCopySend(elData);
        }else if(elName.equalsIgnoreCase("paperPrintCount")){//纸制打印份数
          if(YHUtility.isInteger(elData)){
            receive.setPaperPrintCount(Integer.parseInt(elData));
          }
        }else if(elName.equalsIgnoreCase("totalPrintCount")){//总份数
          if(YHUtility.isInteger(elData)){
            receive.setTotalPrintCount(Integer.parseInt(elData));
          }
        }else if(elName.equalsIgnoreCase("sendDatetimeShow")){//主送
          if(YHUtility.isDayTime(elData)){
            receive.setSendDatetimeShow(YHUtility.parseDate("yyyy-mm-dd HH:ss:mm", elData));
          }
        }
        else if (elName.equalsIgnoreCase("tasks")) {// 发文任务表
          int sign = 0;
          for (Iterator j = el.elementIterator(); j.hasNext();) {// 任务列表//第二级
            Element node = (Element) j.next();
            String TaskName = node.element("reciveDept").getName() == null ? ""
                : node.element("reciveDept").getName();
            String TaskValue = node.element("reciveDept").getText() == null ? ""
                : node.element("reciveDept").getText();

            String reciveDeptDesc = node.element("reciveDeptDesc").getName() == null ? ""
                : node.element("reciveDeptDesc").getName();
            String reciveDeptDescValue = node.element("reciveDeptDesc")
                .getText() == null ? "" : node.element("reciveDeptDesc")
                .getText();

            // 获取esb本地配置单位
            YHEsbClientConfig config = YHEsbClientConfig.builder(YHSysProps
                .getWebPath() + YHEsbConst.CONFIG_PATH);
            YHDocSendLogic dsl = new YHDocSendLogic();
            String reciveDeptDescClient = dsl.getEsbUser(dbConn, TaskValue);
            if (reciveDeptDescClient.equalsIgnoreCase(config.getUserId())) {// 发送单位
              String seqId = node.element("seqId").getText() == null ? ""
                  : node.element("seqId").getText();
              taskId = seqId;
              // receive.setSendDept(TaskValue);
              // receive.setSendDeptName(reciveDeptDescValue);
              String mainDocId = node.element("mainDocId").getText() == null ? ""
                  : node.element("mainDocId").getText();
              receive.setMainDocId(mainDocId);
              mDocId = mainDocId;
              String mainDocName = node.element("mainDocName").getText() == null ? ""
                  : node.element("mainDocName").getText();
              receive.setMainDocName(mainDocName);
              mDocName = mainDocName;
              String attachmentId = node.element("attachmentId").getText() == null ? ""
                  : node.element("attachmentId").getText();
              receive.setAttachmentId(attachmentId);

              attachIdStr = attachIdStr + attachmentId;
              String attachmentName = node.element("attachmentName").getText() == null ? ""
                  : node.element("attachmentName").getText();
              receive.setAttachmentName(attachmentName);

              attachNameStr = attachNameStr + attachmentName;
              String printCount = node.element("printCount").getText() == null ? ""
                  : node.element("printCount").getText();
              if (YHUtility.isInteger(printCount)) {
                receive.setPrintCount(Integer.parseInt(printCount));
                printNum = printCount;
              }

              String printNoStart = node.element("printNoStart").getText() == null ? ""
                  : node.element("printNoStart").getText();
              startNo = printNoStart;
              receive.setPrintNoStart(printNoStart);
              String printNoEnd = node.element("printNoEnd").getText() == null ? ""
                  : node.element("printNoEnd").getText();
              receive.setPrintNoEnd(printNoEnd);
              endNo = printNoEnd;

              String attachmentSize = node.element("attachmentSize").getText() == null ? ""
                  : node.element("attachmentSize").getText();
              receive.setAttachmentSize(attachmentSize);

            }
          }
        }

      }
    }
    if (receive != null) {
      // receive.setSendDept(fromUnit);
      // receive.setSendDeptName(fromUnit);
      YHDeptTreeLogic dtl = new YHDeptTreeLogic();
      YHExtDept ed = dtl.getDeptByEsbUser(dbConn, fromUnit);// 根据发送部门，查询外部组织机构
      if (ed != null) {
        receive.setSendDept(ed.getDeptId()+"");// 部门GUID
        receive.setSendDeptName(ed.getDeptOfficialName());// 部门名称
      } else {
        // receive.setSendDept(fromUnit);
        // receive.setSendDeptName(fromUnit);
      }
      receive.setStatus("0");
      receive.setGuid(guid);
      if (isSign.equals("0")) {
        receive.setPrintCount(0);
        printNum = "0";
        receive.setPrintNoStart("000");
        startNo = "000";
        receive.setPrintNoEnd("000");
        endNo = "000";
      }
      receive.setIsSign(isSign);
      // 新建收文记录
      YHJhDocrecvInfoLogic logic = new YHJhDocrecvInfoLogic();
      seqIdMax = logic.add(dbConn, receive);

      // 获取request
//      HttpServletRequest request = getWebserviceHttp();
      String IP = "127.0.0.1";
//      if (request != null) {
//        IP = request.getRemoteAddr();
//      }
      YHPerson user = new YHPerson();
      user.setSeqId(0);
      user.setUserName("系统");
      // 系统日志
      // YHSysLogLogic.addSysLog(dbConn, "62", "系统成功接收日志：" + receive.toString()
      // ,0, IP);
      // 安全日志
      // YHSecLogUtil.log(dbConn, user, IP, "310",receive,"1", "系统成功接收数据");

    }
    attachStr[0] = attachIdStr;
    attachStr[1] = attachNameStr;
    attachStr[2] = mDocId;
    attachStr[3] = mDocName;
    attachStr[4] = printNum;
    attachStr[5] = startNo;
    attachStr[6] = endNo;
    attachStr[7] = seqIdMax + "";
    attachStr[8] = isSign;
    
    
    return attachStr;
  }

  /*
   * 将数组转化为List attachs：公文XML解析出的部分数据 索引0：所有附件的Id字符串，已都好分割 索引：所有附件的Name字符串
   * 普通附件转换
   */
  public static List<Map<String, String>> getparseStr(String[] attachs) {
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    String attachIds = attachs[0];
    String attachNames = attachs[1];
    if (!YHUtility.isNullorEmpty(attachIds)
        && !YHUtility.isNullorEmpty(attachNames)) {
      String[] attachIdArry = attachIds.split(",");
      String[] attachNameArry = attachNames.split("\\*");
      if (attachIdArry.length == attachNameArry.length) {
        for (int i = 0; i < attachNameArry.length; i++) {
          if (!YHUtility.isNullorEmpty(attachIdArry[i])
              && !YHUtility.isNullorEmpty(attachNameArry[i])) {
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
   * 将数组转化为List 正文转换
   */
  public static Map<String, String> getparseMainStr(String[] attachs) {
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
          if (!YHUtility.isNullorEmpty(attachIdArry[i])
              && !YHUtility.isNullorEmpty(attachNameArry[i])) {

            map.put("attachId", attachIdArry[i]);
            map.put("attachName", attachNameArry[i]);
            return map;
          }

        }
      }
    }
    return map;
  }

  /*
   * 判断是否存在此附件
   */
  public static String isExist(List<Map<String, String>> list,
      String attachIdStr) {
    for (int i = 0; i < list.size(); i++) {
      Map<String, String> map = list.get(i);
      String attaId = map.get("attachId");
      String attaName = map.get("attachName");
      if (!YHUtility.isNullorEmpty(attaId)
          && !YHUtility.isNullorEmpty(attaName)) {
        String[] temp = attaId.split("_");
        // 6a162779b0bad76d59197438f153f1f3_太极政务云项目案例介绍.docx
        if (temp.length == 2) {
          String tempName = temp[1] + "_" + attaName;
          if (attachIdStr.equals(tempName)) {
            return attaName;
          }

        }

      }
    }
    return "";
  }

  /**
   * 利用webservice 获取request
   * 
   * @return
   */
  public static HttpServletRequest getWebserviceHttp() {
    MessageContext mc = (MessageContext) org.apache.axis.MessageContext
        .getCurrentContext();
    ;
    HttpServletRequest request = (HttpServletRequest) mc
        .getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
    return request;
  }

    
  /**
   * 只支持ZIP附件
   * 
   * @param filePath
   *          ： 附件路径
   * @param fromUnit
   *          : 发送单位 fromUnit:发送单位 type:1解压普通附件 0：解压XML文件 guid:ESB数据交互平台发送的唯一表示
   * @param webrootPath
   *          文件保存目录
   * @throws NoSuchAlgorithmException
   * @throws IOException
   * @throws Exception
   */
  
    public static  Map<String ,String>  unNetZipFileXml(String filePath) throws Exception {
   String savePath = YHSysProps.getAttachPath() + "/jtgw/";//接收附件存放路径
      //String savePath = "D:/project/yhcoreGW/attach/jtgw/";//接收附件存放路径

    Connection dbConn = null; 

    YHDbconnWrap dbUtil = new YHDbconnWrap();
    dbConn = dbUtil.getSysDbConn(); 
    File file = new File(filePath);
    Map<String ,String> map = new HashMap<String,String>();
    InputStream rarFile = new FileInputStream(file);
    if (rarFile == null) { return map; }
    CZipInputStream zip = new CZipInputStream(rarFile);// 支持中文目录 
    ZipEntry entry; 
    String xmlFile = "";//公文XML文件名称
    String upXMLPath = filePath;//
    while((entry = zip.getNextEntry()) != null) {// 循环zip下的所有文件和目录 
      String attName =entry.getName(); 
      if (YHUtility.isNullorEmpty(attName)) { continue; }
      File outFile = new File(filePath );
      String outPath =outFile.getParent(); 
      if (!outFile.exists()) {
        outFile.mkdirs(); 
        }
       outFile = new File(outPath + "/" +   attName);
      if(attName.startsWith("JHNET_DATA")) {// 公文类型的XML
        outFile.createNewFile();
        InputStream in = null;
        FileOutputStream out = new FileOutputStream(outFile);
        int len = 0;
        byte[] buff = new byte[4096];
        while ((len = zip.read(buff)) != -1) {
          out.write(buff, 0, len);
        }
        zip.closeEntry();
        out.close();
        upXMLPath = outPath + "/" +   attName;
      }else{
        continue;
      }
    }
    if(!YHUtility.isNullorEmpty(upXMLPath)){//如果存在XMl文件
      map = parseNetXML( upXMLPath,  dbConn , filePath,savePath) ;
      
    
    }
    return map;
  }
   
  /**
   * 公文系统传输
   * @param filePath  压缩包路径
   * @param savePath  将文件解压保存到路径
   * @return mainDoc 正文附件Id和名称
   */
  public static String[] parseAttach(String filePath, String savePath,String mainDoc) {
    File file = new File(filePath);
    String[] attachArr = new String[5];
    
    String attachIds = "";//所有附件Id字符串
    String attachNames = "";//名称
    String mainDocId = "";
    String mainDocName = "";
    String fileSizeStr = "";//附件大小
    
    String OADocId = "";//xml数据中正文Id
    String OADocName = "";//名称
    if(!YHUtility.isNullorEmpty(mainDoc) && mainDoc.split("\\*").length == 2){
      OADocId = mainDoc.split("\\*")[0];
      OADocName = mainDoc.split("\\*")[1];
    }
    try {
      InputStream rarFile = new FileInputStream(file);
      if (rarFile == null) {// 如果没有附件了跳出
        return attachArr;
      }
      ZipEntry entry;
      CZipInputStream zip = new CZipInputStream(rarFile);// 支持中文目录
      while ((entry = zip.getNextEntry()) != null) {// 循环zip下的所有文件和目录
        String attName = entry.getName();
        if (YHUtility.isNullorEmpty(attName)) {
          continue;
        }
        if (attName.startsWith("JHNET_DATA") && attName.endsWith("xml")) {// 如果是数据xml
          continue;
        }

        // 日期
        Calendar cld = Calendar.getInstance();
        int year = cld.get(Calendar.YEAR) % 100;
        int month = cld.get(Calendar.MONTH) + 1;
        String mon = month >= 10 ? month + "" : "0" + month;
        String hard = year + mon;
        String newSavePath = savePath + hard + "/";

        // 判断是否存在
        String rand = YHDiaryUtil.getRondom();
        String fileName = rand + "_" + attName;
        while (YHDiaryUtil.getExist(newSavePath, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + attName;
        }
        File outFile = new File(newSavePath + fileName);
        File outPath = outFile.getParentFile();
        if (!outPath.exists()) {
          outPath.mkdirs();
        }
        // 附件Id
        String attachId = hard + "_" + rand;
        String attachName = attName;
        if(OADocName.equals(attName)){//如果是正文
          OADocName = attachName;
          OADocId = attachId;
        }
        outFile = new File(newSavePath + fileName);
        outFile.createNewFile();
        InputStream in = null;
        FileOutputStream out = new FileOutputStream(outFile);
        int len = 0;
        byte[] buff = new byte[4096];
        while ((len = zip.read(buff)) != -1) {
          out.write(buff, 0, len);
        }
        zip.closeEntry();
        out.close();
        long fileSize = outFile.length();
        if(YHUtility.isNullorEmpty(fileSizeStr)){
          fileSizeStr = fileSize + "";
        }else {
          fileSizeStr = fileSizeStr + "," + fileSize;
        }
        if (!YHUtility.isNullorEmpty(attachIds)) {
          attachIds = attachIds + "," + attachId;
        } else {
          attachIds = attachId;
        }
        if (!YHUtility.isNullorEmpty(attachNames)) {
          attachNames = attachNames + "*" + attachName;
        } else {
          attachNames = attachName;
        }
      }

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    attachArr[0] = attachIds;
    attachArr[1] = attachNames;
    attachArr[2] = OADocId;
    attachArr[3] = OADocName;
    attachArr[4] = fileSizeStr;
    return attachArr;
  }
  
  /**
   * 
   * 解析公文XML、并新建收文记录 upXMLPath: 临时公文XML存放路径
   * @param dbConn
   *          ：数据库 
   *          attachArr: 附件Id、附件名称数组
   * @throws Exception
   */
  public static Map<String,String> parseNetXML(String upXMLPath, Connection dbConn ,String filePath,String savePath) throws Exception {
    SAXReader saxReader = new SAXReader();
    File XMLFile = new File(upXMLPath);
    Document document = saxReader.read(XMLFile);
    Element root = document.getRootElement();
    String taskId = "0";// 发送单位任务从表的seqId
    Map<String ,String> map = null;

    YHDeptTreeLogic dtl = new YHDeptTreeLogic();
    if (!YHUtility.isNullorEmpty(root.getName()) && root.getName().equals("root")) {
      List<Element> elements = root.elements();
      map = new HashMap<String,String> ();
      Element node = root;
      String title = node.element("title").getName() == null ? "" : node.element("title").getName();
      String titleValue = node.element("title").getText() == null ? "": node.element("title").getText();
      String runId = node.element("runId").getText() == null ? "0": node.element("runId").getText();
      String attachmentId = node.element("attachmentId").getText() == null ? "": node.element("attachmentId").getText();
      String attachmentName = node.element("attachmentName").getText() == null ? "": node.element("attachmentName").getText();
      String docNum = node.element("docNum").getText() == null ? "": node.element("docNum").getText();
      String hurry = node.element("hurry").getText() == null ? "": node.element("hurry").getText();
      String secret = node.element("secret").getText() == null ? "": node.element("secret").getText();
      String sendDept = node.element("sendDept").getText() == null ? "": node.element("sendDept").getText();
      String sendTime = node.element("sendTime").getText() == null ? "": node.element("sendTime").getText();
      String DocType = node.element("DocType").getText() == null ? "": node.element("DocType").getText();
      //文件类型，0：普通，1：公文
      String counts = node.element("counts").getText() == null ? "": node.element("counts").getText();
      String attachCounts = node.element("attachCounts").getText() == null ? "": node.element("attachCounts").getText();
      String keyWords = node.element("keyWords").getText() == null ? "": node.element("keyWords").getText();
      //String sendRange = node.element("sendRange").getText() == null ? "": node.element("sendRange").getText();
      String MainSendDept = node.element("MainSendDept").getText() == null ? "": node.element("MainSendDept").getText();
      String CopySendDept = node.element("CopySendDept").getText() == null ? "": node.element("CopySendDept").getText();
      
      String mainDoc = node.element("mainDoc").getText() == null ? "": node.element("mainDoc").getText();
      YHJhDocsendInfo send = new YHJhDocsendInfo();
      send.setCreateUser(0);
      send.setRunId(Integer.parseInt(runId));
      send.setDocTitle(titleValue);
      send.setDocNo(docNum);
      String docType = "1";
      if(DocType.equals("普通")){
        docType = "0";
        send.setIsSign("0");//是否打印
      }else{
        send.setIsSign("1");//是否打印
      }

      send.setDocType(docType);
      if(secret.equals("普通")){//密级
        send.setSecurityLevel("0");
      }else if(secret.equals("内部")){
        send.setSecurityLevel("1");
      }else if(secret.equals("秘密")){
        send.setSecurityLevel("2");
      }else if(secret.equals("机密")){
        send.setSecurityLevel("3");
      }
      
      if(hurry.equals("普通")){//紧急程度
        send.setUrgentType("0");
      }else if(hurry.equals("紧急")){
        send.setUrgentType("1");
      }else if(hurry.equals("特急")){
        send.setUrgentType("2");
      }
      int printAllCount = 0;
      if(YHUtility.isInteger(counts)){
        printAllCount = Integer.parseInt(counts);
      }
      //send.setPrintCount(printCount);
      send.setTotalPrintCount(printAllCount);//手动填入的
      int attachCount = 0;
      if(YHUtility.isInteger(attachCounts)){
        attachCount = Integer.parseInt(attachCounts);
      }
      send.setAttachCount(attachCount);
      //解析普通附件
      String[] attachArr = parseAttach( filePath,  savePath, mainDoc);//解压附件，并重新返回附件Id和名称字符串
      send.setAttachmentId(attachArr[0]);
      send.setAttachmentName(attachArr[1]);
      send.setMainDocId(attachArr[2]);
      send.setMainDocName(attachArr[3]);
      send.setCreateDatetime(new Date());
   
      send.setSecurityTime("0");//保密期限
      send.setOaMainSend(MainSendDept);
      send.setOaCopySend(CopySendDept);
      //新建正文主表
      
    //  send.setHandReciveDept(sendRange);//接收单位
      send.setIsStamp("0");//是否盖章
      

      //获取发送范围
      int printCount = 0;
      Element UseDeptS = node.element("UseDept");
      List<Element>  UseDept = UseDeptS.elements("UseDept");//解析密码网单位和打印份数
     
      String deptCodeStr = "";
      String deptCodeNameStr = "";
      String deptCodeFlagStr = "";
      List<Map<String,String>> list = new ArrayList<Map<String,String>>();
      for (Iterator j = UseDeptS.elementIterator(); j.hasNext();) {// 任务列表//第二级
        Element el = (Element) j.next();
        String copy = el.element("copy").getText() == null ? "": el.element("copy").getText();
        String deptCode = el.element("deptCode").getText() == null ? "": el.element("deptCode").getText();
       
       // String[] str = {"DEPT_CODE='" +  deptCode+ "'"};//根据单位Id查询对象

        String[] str = {"DEPT_ID='" +  deptCode+ "'"};//根据单位Id查询对象
        List<YHExtDept>  listDept = YHDeptTreeLogic.select(dbConn, str);
        if(listDept.size()>0){
          YHExtDept dept = listDept.get(0);//取第一个
          Map<String,String> map2 = new HashMap<String,String>();
          map2.put("code", deptCode);
          map2.put("copy", copy);
          map2.put("codeName", dept.getDeptName());
          String DEPT_TEL_LINE = dept.getDeptTelLine() == null ? "" : dept.getDeptTelLine() ;
          list.add(map2);
          if(YHUtility.isNullorEmpty(deptCodeStr)){
            deptCodeStr = deptCode;
            deptCodeNameStr = dept.getDeptName();
            deptCodeFlagStr = deptCode;
          }else{
            deptCodeStr = deptCodeStr + "," +  deptCode;
            deptCodeNameStr = deptCodeNameStr + "," +  dept.getDeptName();
            deptCodeFlagStr = deptCodeFlagStr +","+ deptCode;
          }
          if(DEPT_TEL_LINE.equals("1")){//如果是拨号单位
            deptCodeFlagStr = deptCodeFlagStr + "*";
          }
          if(YHUtility.isInteger(copy)){
            printCount = printCount + Integer.parseInt(copy);
          }
        }
        
        
      }
      send.setReciveDept(deptCodeStr);
      send.setReciveDeptDesc(deptCodeNameStr);
      send.setPrintCount(printCount);//网络打印份数
      send.setReciveDeptFlag(deptCodeFlagStr);
      int max = YHDocSendLogic.addDoc(dbConn, send);
      /*<UseDept> 
      <item> 
      <copy>1</copy> 
      <deptCode>1234</deptCode> 
      </item> 
      </UseDept>*/


      //附件
      String attachId = attachArr[0];
      String attachName = attachArr[1];
      String attachFileSize = attachArr[4];//附件大小字符串
      if(!YHUtility.isNullorEmpty(attachId) && !YHUtility.isNullorEmpty(attachName)){
        String[] attachIdArr = attachId.split(",");
        String[] attachNameArr = attachName.split("\\*");
        String[] attachSizeArr = attachFileSize.split(",");
        for (int i = 0; i < attachNameArr.length; i++) {
          YHJhDocsendFiles file = new YHJhDocsendFiles();
          file.setDocsendInfoId(max);
          file.setFileId(attachIdArr[i]);
          file.setFileName(attachNameArr[i]);
          System.out.println(attachIdArr[i] +":"+attachArr[2] );
          if(!YHUtility.isNullorEmpty(attachIdArr[i]) && attachIdArr[i].equals(attachArr[2])){//如果是正文
            file.setIsMainDoc(1);
          }
          file.setReciveDept(deptCodeStr);
          file.setReciveDeptDesc(deptCodeNameStr);
          file.setFileSize(attachSizeArr[i]);
          YHDocSendLogic.addDocFile(dbConn, file);
        }
      }

      
      //新建发送任务
      int startNum = 1;
      for (int i = 0; i < list.size(); i++) {
        Map<String,String> mapTemp = list.get(i);
        String recvDept = mapTemp.get("code");//单位代码
        String recvDeptName = mapTemp.get("codeName");//单位代码名称
        String copyNum = mapTemp.get("copy") == null ? "0" : mapTemp.get("copy");//打印份数；
        
        int copyNumInt = 0;
        if(YHUtility.isInteger(copyNum)){
          copyNumInt= Integer.parseInt(copyNum);
        }
        YHJhDocsendTasks task = new YHJhDocsendTasks();
        task.setReciveDept(recvDept);
        task.setReciveDeptDesc(recvDeptName);
        task.setAttachmentId(attachId);
        task.setAttachmentName(attachName);
        task.setPrintCount(copyNumInt);
        task.setStatus("");
        task.setDocsendInfoId(max);
        task.setMainDocId(attachArr[2]);
        task.setMainDocName(attachArr[3]);
        
        String[] printNo = getPrintNo(copyNumInt,startNum);
        startNum =  startNum + copyNumInt;
        task.setPrintNoStart(printNo[0]);
        task.setPrintNoEnd(printNo[1]);
        YHDocSendLogic.addDocTask(dbConn, task);
      }
      
      map.put("seqId", max + "");
      map.put("title", titleValue);
      map.put("runId", runId);
      map.put("attachmentId", attachArr[0]);
      map.put("attachmentName", attachArr[1]);
      map.put("hurry", hurry);
      map.put("secret", secret);
      map.put("sendDept", sendDept);
      map.put("docNum", docNum);
      map.put("sendTime", sendTime);
      map.put("counts", counts);
      map.put("attachCounts", attachCounts);
      map.put("keyWords", keyWords);
      map.put("sendTime", sendTime);
      map.put("mainDoc", mainDoc);
    }
    return map;
  }
  
  public static String[] getPrintNo(int pringCount,int startNum){
    String[] printNo = new String[2];
    String startNo=  "" + startNum;
     String endNo = (pringCount + startNum -1) + "" ;
     for (int i = startNo.length(); i < 3; i++) {
       startNo = "0" + startNo;
     }
    for (int i = endNo.length(); i < 3; i++) {
      endNo = "0" + endNo;
    }
    printNo[0] = startNo;
    printNo[1] = endNo;
    return printNo;
  }

public static void main(String[] args) {
  try {
    unNetZipFileXml("D:\\project\\yhcoreGW\\attach\\NETFILE\\8aa5469da31ec9d6cac9331b84b9638a_abc.zip");
  } catch (Exception e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
}
}