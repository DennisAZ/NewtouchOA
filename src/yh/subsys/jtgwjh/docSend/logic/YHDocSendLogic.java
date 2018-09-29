package yh.subsys.jtgwjh.docSend.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.esb.frontend.services.YHEsbService;
import yh.core.funcs.calendar.logic.YHCalendarLogic;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.office.ntko.data.YHNtkoCont;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.core.util.file.YHZipFileTool;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.docReceive.data.YHJhDocrecvInfo;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendFiles;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendInfo;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendStamps;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendTasks;
import yh.subsys.jtgwjh.util.YHDocUtil;
import yh.user.api.core.db.YHDbconnWrap;

public class YHDocSendLogic {
  private static Logger log = Logger.getLogger(YHDocSendLogic.class);
  private static final String attachmentFolder = "jtgw";
  public static String filePath = YHSysProps.getAttachPath() + File.separator  + "jtgw";
  public static String COPYPATH = File.separator  + "subsys"+ File.separator  + "jtgwjh"+ File.separator  + "ntko";

  /**
   * 处理上传附件，返回附件id，附件名称
   * 
   * @param fileForm
   * @return
   * @throws Exception
   */
  public Map<Object, Object> fileUploadLogic(YHFileUploadForm fileForm, String attachmentFolder) throws Exception {
    Map<Object, Object> result = new HashMap<Object, Object>();
    try {
      // 保存从文件柜、网络硬盘选择附件
      YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, attachmentFolder);
      String attIdStr = sel.getAttachIdToString(",");
      String attNameStr = sel.getAttachNameToString("*");
      boolean fromFolderFlag = false;
      String forlderAttchId = "";
      String forlderAttchName = "";
      if (!"".equals(attIdStr) && !"".equals(attNameStr)) {
        forlderAttchId = attIdStr + ",";
        forlderAttchName = attNameStr + "*";
        fromFolderFlag = true;
      }
      Iterator<String> iKeys = fileForm.iterateFileFields();
      boolean uploadFlag = false;
      String uploadAttchId = "";
      String uploadAttchName = "";
      Date date = new Date();
      SimpleDateFormat format = new SimpleDateFormat("yyMM");
      String currDate = format.format(date);
      String separator = File.separator;
      String filePath = YHSysProps.getAttachPath() + separator + attachmentFolder + separator + currDate;

      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        String rand = YHGuid.getRawGuid();
        uploadAttchId += currDate + "_" + rand + ",";
        uploadAttchName += fileName + "*";
        uploadFlag = true;

        fileName = rand + "_" + fileName;
        fileForm.saveFile(fieldName, filePath + File.separator + fileName);
      }
      boolean attachFlag = false;
      String attachmentIds = "";
      String attachmentNames = "";
      if (fromFolderFlag && uploadFlag) {
        attachmentIds = forlderAttchId + uploadAttchId;
        attachmentNames = forlderAttchName + uploadAttchName;
        attachFlag = true;
      } else if (fromFolderFlag) {
        attachmentIds = forlderAttchId;
        attachmentNames = forlderAttchName;
        attachFlag = true;
      } else if (uploadFlag) {
        attachmentIds = uploadAttchId;
        attachmentNames = uploadAttchName;
        attachFlag = true;
      }
      result.put("attachFlag", attachFlag);
      result.put("attachmentIds", attachmentIds);
      result.put("attachmentNames", attachmentNames);
    } catch (Exception e) {
      throw e;
    }
    return result;
  }

  /**
   * 新建发文登记
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public YHJhDocsendInfo addDocInfo(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
    YHORM orm = new YHORM();
    
    //获取页面信息
    String forwordId = fileForm.getParameter("forwordId") ;
    String docTitle = fileForm.getParameter("docTitle");
    String docType = fileForm.getParameter("docType");
    String docKind = fileForm.getParameter("docKind");
    String urgentType = fileForm.getParameter("urgentType");
    String securityLevel = fileForm.getParameter("securityLevel");
    String totalPrintCount = YHUtility.isNullorEmpty(fileForm.getParameter("totalPrintCount")) ? "0" : fileForm.getParameter("totalPrintCount");
    String printCount = YHUtility.isNullorEmpty(fileForm.getParameter("printCount")) ? "0" : fileForm.getParameter("printCount");
    String paperPrintCount = YHUtility.isNullorEmpty(fileForm.getParameter("paperPrintCount")) ? "0" : fileForm.getParameter("paperPrintCount");
    
    String docNo = fileForm.getParameter("docNo");
    String pageCount = YHUtility.isNullorEmpty(fileForm.getParameter("pageCount")) ? "0" : fileForm.getParameter("pageCount");
    String attachCount = YHUtility.isNullorEmpty(fileForm.getParameter("attachCount")) ? "0" : fileForm.getParameter("attachCount");
    
    String oaMainSend = fileForm.getParameter("oaMainSend");
    String oaCopySend = fileForm.getParameter("oaCopySend");
    
    String remark = fileForm.getParameter("remark");
    String handReciveDept = fileForm.getParameter("handReciveDept");
    String reciveDept = fileForm.getParameter("reciveDept");
    String reciveDeptDesc = fileForm.getParameter("reciveDeptDesc");
    String reciveDeptFlag = fileForm.getParameter("reciveDeptFlag");
    String attachmentId = fileForm.getParameter("attachmentId");
    String attachmentName = fileForm.getParameter("attachmentName");
    String attachmentReciveDeptId = fileForm.getParameter("attachmentReciveDeptId");
    String attachmentReciveDeptDesc = fileForm.getParameter("attachmentReciveDeptDesc");
    String attachmentUploadNum = fileForm.getParameter("attachmentUploadNum");
    String mainDocId = fileForm.getParameter("mainDocId");
    String mainDocName = fileForm.getParameter("mainDocName");
    if(!YHUtility.isNullorEmpty(mainDocId)){
      Map<String, String> map = new HashMap<String, String>();
      map.put(mainDocId, mainDocName);
      long sizeMain = this.getSize(map, "0".equals(forwordId) ? attachmentFolder : "docReceive");
      if(attachmentId.endsWith(",") || YHUtility.isNullorEmpty(attachmentId)){
        attachmentId += mainDocId + ",";
        attachmentName += mainDocName + "*";
        attachmentReciveDeptId += "0";
        attachmentReciveDeptDesc += "已选网络接收单位";
        attachmentUploadNum += sizeMain;
      }
      else{
        attachmentId += "," + mainDocId + ",";
        attachmentName += "*" + mainDocName + "*";
        attachmentReciveDeptId += "*0";
        attachmentReciveDeptDesc += "*已选网络接收单位";
        attachmentUploadNum += "," + sizeMain;
      }
    }
    String isSign = fileForm.getParameter("isSign");
    if(YHUtility.isNullorEmpty(mainDocId)){
      isSign = "0";
    }
    String isStamp = fileForm.getParameter("isStamp");
    String seqId = fileForm.getParameter("seqId");
    
    String yearShow = fileForm.getParameter("yearShow");
    String mouthShow = fileForm.getParameter("mouthShow");
    String dayShow = fileForm.getParameter("dayShow");
    String sendDatatimeShow = yearShow+"-"+mouthShow+"-"+dayShow;
    
    try{
      
      //发文登记主表保存功能
      YHJhDocsendInfo docSendInfo = new YHJhDocsendInfo();
      docSendInfo.setForwordId(Integer.parseInt(forwordId));
      docSendInfo.setDocTitle(docTitle);
      docSendInfo.setDocType(docType);
      docSendInfo.setDocKind(docKind);
      docSendInfo.setUrgentType(urgentType);
      docSendInfo.setSecurityLevel(securityLevel);
      docSendInfo.setTotalPrintCount(Integer.parseInt(totalPrintCount));
      docSendInfo.setPrintCount(Integer.parseInt(printCount));
      docSendInfo.setPaperPrintCount(Integer.parseInt(paperPrintCount));
      docSendInfo.setDocNo(docNo);
      docSendInfo.setPageCount(Integer.parseInt(pageCount));
      docSendInfo.setAttachCount(Integer.parseInt(attachCount));
      docSendInfo.setOaMainSend(oaMainSend);
      docSendInfo.setOaCopySend(oaCopySend);
      docSendInfo.setRemark(remark);
      docSendInfo.setHandReciveDept(handReciveDept);
      docSendInfo.setReciveDept(reciveDept);
      docSendInfo.setReciveDeptDesc(reciveDeptDesc);
      docSendInfo.setReciveDeptFlag(reciveDeptFlag);
      docSendInfo.setCreateUser(person.getSeqId());
      docSendInfo.setCreateUserName(person.getUserName());
      docSendInfo.setCreateDept(person.getDeptId());
      docSendInfo.setCreateDeptName(getUserDeptName(dbConn, person.getDeptId()));
      docSendInfo.setCreateDatetime(YHUtility.parseTimeStamp());
      docSendInfo.setMainDocId(mainDocId);
      docSendInfo.setMainDocName(mainDocName);
      docSendInfo.setAttachmentId(attachmentId);
      docSendInfo.setAttachmentName(attachmentName);
      docSendInfo.setIsSign(isSign);
      docSendInfo.setIsStamp(isStamp);
      
      if(YHUtility.isDay(sendDatatimeShow)){
        docSendInfo.setSendDatetimeShow(YHUtility.parseTimeStamp(sendDatatimeShow));
      }
      else{
        docSendInfo.setSendDatetimeShow(YHUtility.parseTimeStamp());
      }
      
      if(YHUtility.isInteger(seqId)){
        docSendInfo.setSeqId(Integer.parseInt(seqId));
        orm.updateSingle(dbConn, docSendInfo);
      }
      else{
        orm.saveSingle(dbConn, docSendInfo);
      }
      
      //获取主表最大seqId
      int docsendInfoId = 0;
      if(YHUtility.isInteger(seqId)){
        docsendInfoId = Integer.parseInt(seqId);
      }
      else{
        docsendInfoId = getMaxSeqId(dbConn);
        docSendInfo.setSeqId(docsendInfoId);
      }
      
      //发文登记附件从表保存功能
      Map<String, String> attachMap = new HashMap<String, String>();
      String attachmentIdArray[] = attachmentId.split(",");
      String attachmentNameArray[] = attachmentName.split("\\*");
      String attachmentReciveDeptIdArray[] = attachmentReciveDeptId.split("\\*");
      String attachmentReciveDeptDescArray[] = attachmentReciveDeptDesc.split("\\*");
      String attachmentUploadNumArray[] = attachmentUploadNum.split(",");
      for(int i = 0; i < attachmentIdArray.length ;i++){
        if(!YHUtility.isNullorEmpty(attachmentIdArray[i])){
          YHJhDocsendFiles docSendAttach = new YHJhDocsendFiles();
          docSendAttach.setDocsendInfoId(docsendInfoId);
          docSendAttach.setFileId(attachmentIdArray[i]);
          docSendAttach.setFileName(attachmentNameArray[i]);
          
          String attachmentReciveDeptIdTemp = "";
          String attachmentReciveDeptNameTemp = "";
          if("0".equals(attachmentReciveDeptIdArray[i])){
            attachmentReciveDeptIdTemp = reciveDept;
            attachmentReciveDeptNameTemp = reciveDeptDesc;
          }
          else{
            attachmentReciveDeptIdTemp = attachmentReciveDeptIdArray[i];
            attachmentReciveDeptNameTemp = attachmentReciveDeptDescArray[i];;
          }
          docSendAttach.setReciveDept(attachmentReciveDeptIdTemp);
          docSendAttach.setReciveDeptDesc(attachmentReciveDeptNameTemp);
          if(attachmentIdArray[i].equals(mainDocId)){
            docSendAttach.setIsMainDoc(1);
            docSendAttach.setFileSize(attachmentUploadNumArray[i]); 
          }
          else{
            docSendAttach.setIsMainDoc(0);
            docSendAttach.setFileSize(fileForm.getParameter("attach_Size_"+attachmentUploadNumArray[i])); 
          }
          orm.saveSingle(dbConn, docSendAttach);
          attachMap.put(attachmentIdArray[i], attachmentReciveDeptIdTemp);
        }
        else{
          attachMap.put(attachmentIdArray[i], reciveDept);
        }
      }
      
      //map转换，从以附件为主的一对多关系转变为，变为以部门id为主的一对多关系
      //遍历map 发文登记任务队列从表保存功能
      Map <String, String> deptMap = YHDocUtil.attach2Dept(attachMap);
      Set<String> key = deptMap.keySet();
      Iterator<String> iKeys =  key.iterator();
      while(iKeys.hasNext()){
        String keyStr = (String) iKeys.next();
        String valueStr = (String) deptMap.get(keyStr);
        
        YHJhDocsendTasks docsendTasks = new YHJhDocsendTasks();
        docsendTasks.setDocsendInfoId(docsendInfoId);
        docsendTasks.setReciveDept(keyStr);
        docsendTasks.setReciveDeptDesc(getOutUserDeptName(dbConn, keyStr));
        docsendTasks.setAttachmentId(valueStr);
        StringBuffer fileName = new StringBuffer();
        StringBuffer fileSize = new StringBuffer();
        String valueArray[] = valueStr.split(",");
        for(int i = 0; i < valueArray.length ;i++){
          for(int j = 0; j < attachmentIdArray.length ; j++){
            if(valueArray[i].equals(mainDocId)){
              docsendTasks.setMainDocId(mainDocId);
              docsendTasks.setMainDocName(mainDocName);
            }
            if(valueArray[i].equals(attachmentIdArray[j])){
              fileName.append(attachmentNameArray[j] + "*");
              if(valueArray[i].equals(mainDocId)){
                fileSize.append(attachmentUploadNumArray[j] + ",");
              }
              else{
                fileSize.append(fileForm.getParameter("attach_Size_"+attachmentUploadNumArray[j]) + ",");
              }
              break;
            }
          }
        }
        if(fileName.length() > 1 && fileName.toString().endsWith("*")){
          fileName = fileName.deleteCharAt(fileName.length() - 1);
          fileSize = fileSize.deleteCharAt(fileSize.length() - 1);
        }
        docsendTasks.setAttachmentName(fileName.toString());
        docsendTasks.setAttachmentSize(fileSize.toString());
        
        String reciveDeptArray[] = reciveDept.split(",");
        for(int i = 0; i < reciveDeptArray.length ; i++){
          if(reciveDeptArray[i].equals(keyStr)){
            String printCountStr = fileForm.getParameter("print_count_"+i);
            String printNoStart = fileForm.getParameter("print_no_start_"+i);
            String printNoEnd = fileForm.getParameter("print_no_end_"+i);
            if(YHUtility.isInteger(printCountStr)){
              docsendTasks.setPrintCount(Integer.parseInt(printCountStr));
            }
            else{
              docsendTasks.setPrintCount(0);
            }
            docsendTasks.setPrintNoStart(printNoStart);
            docsendTasks.setPrintNoEnd(printNoEnd);
            break;
          }
        }
        docsendTasks.setStatus("0");//草稿
        docsendTasks.setProcessTime(YHUtility.parseTimeStamp());
        orm.saveSingle(dbConn, docsendTasks);
      }
      
      //如果是收文转发文，需要把收文的正文和附件 从docRecive中复制到jtgw目录中
      if(!YHUtility.isNullorEmpty(forwordId)){
        for(int j = 0; j < attachmentIdArray.length; j++){
          String attachPath = getAttachPath(attachmentIdArray[j], attachmentNameArray[j], "docReceive");
          
          String pathPx = YHSysProps.getAttachPath();
          String filePath = pathPx + File.separator +  "jtgw";
          int index = attachmentIdArray[j].indexOf("_");
          String hard = "";
          String str = "";
          if (index > 0) {
            hard = attachmentIdArray[j].substring(0, index);
            str = attachmentIdArray[j].substring(index + 1);
          } else {
            hard = "all";
            str = attachmentIdArray[j];
          }
          String path = filePath + File.separator +  hard + File.separator +  str + "_" + attachmentNameArray[j];
          
          YHFileUtility.copyFile(attachPath, path);
        }
      }
      
      return docSendInfo;
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 发送发文登记
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public String sendDoc(Connection dbConn, int docsendInfoId, HttpServletRequest request, String forward) throws Exception{
    
    YHORM orm = new YHORM();
    YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    
    //循环发送单位
    YHJhDocsendInfo docsendInfo = (YHJhDocsendInfo) orm.loadObjSingle(dbConn, YHJhDocsendInfo.class, docsendInfoId);
    docsendInfo.setSendDatetime(YHUtility.parseTimeStamp());
    orm.updateSingle(dbConn, docsendInfo);

    //此次打包任务名称
    String taskName = "JHDATA_" + YHGuid.getRawGuid();
    
    //生成xml文件
    Map<String, String> filters = new HashMap<String, String>();
    filters.put("DOCSEND_INFO_ID", docsendInfoId+"");
    List<YHJhDocsendTasks> docsendTasksList = orm.loadListSingle(dbConn, YHJhDocsendTasks.class, filters);
    
    String xml = docsendInfo.toXML(docsendTasksList);
    String path = YHSysProps.getAttachPath() + File.separator + "uploadJTGW" + File.separator + taskName;
    String FileName = "JHDATA_" + YHGuid.getRawGuid() + ".xml";
    while (YHDocUtil.getExist(path, FileName)) {
      FileName = "JHDATA_" + YHGuid.getRawGuid() + ".xml";
    }
    File file = new File(path + File.separator + FileName);
    if(!file.exists()){
      File file2 = new File(path);
      if(!file2.exists()){
        file2.mkdirs();
      }
      file.createNewFile();
    }
    FileOutputStream out = new FileOutputStream(file);
    out.write(xml.getBytes("utf-8"));
    out.close();
    
    //复制附件到指定目录
    String attachmentId = docsendInfo.getAttachmentId();
    String attachmentName = docsendInfo.getAttachmentName();
    String attachmentIdArray[] = attachmentId.split(",");
    String attachmentNameArray[] = attachmentName.split("\\*");
    for(int j = 0; j < attachmentIdArray.length; j++){
      String attachPath = getAttachPath(attachmentIdArray[j], attachmentNameArray[j], attachmentFolder);
      File fileTemp = new File(attachPath);
      YHFileUtility.copyFile(attachPath, path + File.separator + fileTemp.getName());
    }
    //生成zip压缩包
    YHZipFileTool.doZip(path, path + ".zip");
    
    //连接esb服务器发送任务
    String reciveDeptDesc = getEsbUser(dbConn, docsendInfo.getReciveDept());
    
    YHEsbClientConfig config = YHEsbClientConfig.builder(request.getRealPath("/") + YHEsbConst.CONFIG_PATH) ;
    YHEsbService esbService = new YHEsbService();
    String ret = esbService.send(path + ".zip", reciveDeptDesc, config.getToken(), "JHdoc" ,docsendInfo.getDocTitle());
    
    //ret json串中code值为0时 改为发送中
    Map map = YHFOM.json2Map(ret);
    String code = (String) map.get("code");
    String msg = (String) map.get("msg");
    if("0".equals(code)){
      
      //修改task任务为发送中状态
      for(YHJhDocsendTasks docsendTasks : docsendTasksList){
        docsendTasks.setStatus("2");//发送中
        docsendTasks.setGuid((String)map.get("guid"));
        docsendTasks.setProcessTime(YHUtility.parseTimeStamp());
        orm.updateSingle(dbConn, docsendTasks);
      }
      //系统日志
      YHSysLogLogic.addSysLog(dbConn, "60", "发送发文成功：" + docsendInfo.toString() ,person.getSeqId(), request.getRemoteAddr());
    }
    else{
      //修改task任务为本地发送失败状态
      for(YHJhDocsendTasks docsendTasks : docsendTasksList){
        docsendTasks.setStatus("-2");//本地失败
        docsendTasks.setProcessTime(YHUtility.parseTimeStamp());
        orm.updateSingle(dbConn, docsendTasks);
      }
      //系统日志
      YHSysLogLogic.addSysLog(dbConn, "60", "发送发文失败：" + docsendInfo.toString() ,person.getSeqId(), request.getRemoteAddr());
    }
    return msg;
  }
  
  
  /**
   * esb用户查询
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public String getEsbUser(Connection dbConn, String reciveDeptDesc) throws Exception{
    
    StringBuffer reStr = new StringBuffer();
    StringBuffer sb = new StringBuffer();
    String reciveDeptDescArray[] = reciveDeptDesc.split(",");
    for(int i = 0; i < reciveDeptDescArray.length; i++){
      sb.append("'"+reciveDeptDescArray[i]+"',");
    }
    if(sb.length() > 1){
      sb = sb.deleteCharAt(sb.length() - 1);
    }
    String sql = " select ESB_USER from oa_dept_ext where DEPT_ID IN (" + sb.toString() + ") ";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        reStr.append(rs.getString("ESB_USER") + ",");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    if(reStr.length() > 1){
      reStr = reStr.deleteCharAt(reStr.length() - 1);
    }
    return reStr.toString();
  }
  
  /**
   * 重发发文登记中的一个任务
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public void sendDocTasks(Connection dbConn, String guid, String toId, HttpServletRequest request) throws Exception{
    
    YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    
    //连接esb服务器发送任务
    String esbUserId = getEsbUser(dbConn, toId);
    
    YHEsbClientConfig config = YHEsbClientConfig.builder(request.getRealPath("/") + YHEsbConst.CONFIG_PATH) ;
    YHEsbService esbService = new YHEsbService();
    String ret = esbService.resend(guid, esbUserId, config.getToken());
    Map map = YHFOM.json2Map(ret);
    String code = (String) map.get("code");
    /*{code:'0',msg:'重发成功！'}
      {code:'-1',msg:'重发失败！'} 
      {code:'-2',msg:'连接超时！'} 
      {code:'-3',msg:'程序出错！'} 
      {code:'-4',msg:'未返回消息！'} 
      {code:'-5',msg:'本地配置不对'}*/
    YHORM orm = new YHORM();
    Map<String, String> filters = new HashMap<String, String>();
    filters.put("guid", guid);
    filters.put("RECIVE_DEPT", toId);
    YHJhDocsendTasks docsendTask = (YHJhDocsendTasks) orm.loadObjSingle(dbConn, YHJhDocsendTasks.class, filters);
    YHJhDocsendInfo YHJhDocsendInfo = (YHJhDocsendInfo) orm.loadObjSingle(dbConn, YHJhDocsendInfo.class, docsendTask.getDocsendInfoId());
    if("0".equals(code)){
      //修改task任务为发送中状态
      docsendTask.setStatus("5");//发送中
      docsendTask.setProcessTime(YHUtility.parseTimeStamp());
      orm.updateSingle(dbConn, docsendTask);
      
      //系统日志
      YHSysLogLogic.addSysLog(dbConn, "60", "重新发送发文成功：" + YHJhDocsendInfo.toString() + ",toId:"+toId ,person.getSeqId(), request.getRemoteAddr());
    }
    else{
      //修改task任务为本地发送失败状态
      docsendTask.setStatus("-2");//本地失败
      docsendTask.setProcessTime(YHUtility.parseTimeStamp());
      orm.updateSingle(dbConn, docsendTask);
      
    //系统日志
      YHSysLogLogic.addSysLog(dbConn, "60", "重新发送发文失败：" + YHJhDocsendInfo.toString() + ",toId:"+toId ,person.getSeqId(), request.getRemoteAddr());
    }
  }
  
  public static String getAttachPath(String aId , String aName  , String moduleDesc) throws Exception {
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
  
  /**
   * 附件批量上传页面处理
   * 
   * @return
   * @throws Exception
   */
  public StringBuffer uploadMsrg2Json(YHFileUploadForm fileForm)
      throws Exception {
    StringBuffer sb = new StringBuffer();
    Map<String, String> attr = null;
    String attachmentId = "";
    String attachmentName = "";
    try {
      attr = this.fileUploadLogic1(fileForm);
      Set<String> attrKeys = attr.keySet();
      for (String key : attrKeys) {
        String fileName = attr.get(key);
        attachmentId += key + ",";
        attachmentName += fileName + "*";
      }
      long size = this.getSize(attr, attachmentFolder);
      sb.append("{");
      sb.append("'attachmentId':").append("\"").append(attachmentId).append("\",");
      sb.append("'attachmentName':").append("\"").append(attachmentName).append("\",");
      sb.append("'size':").append("").append(size);
      sb.append("}");
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return sb;
  }
  
  /**
   * 处理上传附件，返回附件id，附件名称
   * 
   * @param request
   *          HttpServletRequest
   * @param
   * @return Map<String, String> ==> {id = 文件名}
   * @throws Exception
   */
  public Map<String, String> fileUploadLogic1(YHFileUploadForm fileForm) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    String filePath = YHSysProps.getAttachPath();
    // fileForm.saveFileAll(filePath);
    Calendar cld = Calendar.getInstance();
    int year = cld.get(Calendar.YEAR) % 100;
    int month = cld.get(Calendar.MONTH) + 1;
    String mon = month >= 10 ? month + "" : "0" + month;
    String hard = year + mon;
    Iterator<String> iKeys = fileForm.iterateFileFields();
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      String fileName = fileForm.getFileName(fieldName);
      String trusFileName = "";
      if (YHUtility.isNullorEmpty(fileName)) {
        continue;
      }
      
      String rand = YHGuid.getRawGuid();
      trusFileName = rand + "_" + fileName;
      while (YHDocUtil.getExist(filePath + File.separator + hard, trusFileName)) {
        rand = YHGuid.getRawGuid();
        trusFileName = rand + "_" + fileName;
      }
      result.put(hard + "_" + rand, fileName);
      fileForm.saveFile(fieldName, filePath + File.separator + attachmentFolder + File.separator + hard + File.separator + trusFileName);
    }
    YHSelAttachUtil selA = new YHSelAttachUtil(fileForm, attachmentFolder);
    result.putAll(selA.getAttachInFo());
    return result;
  }
  
  /**
   * 
   * @param attr
   * @param pathPx
   * @return
   * @throws Exception
   */
  public long getSize(Map<String, String> attr, String module) throws Exception {
    long result = 0l;
    Set<String> attrKeys = attr.keySet();
    String fileName = "";
    String path = "";
    for (String attachmentId : attrKeys) {
      String attachmentName = attr.get(attachmentId);
      if(attachmentId != null && !"".equals(attachmentId)){
        if(attachmentId.indexOf("_") > 0){
          String attIds[] = attachmentId.split("_");
          fileName = attIds[1] + "." + attachmentName;
          path = YHSysProps.getAttachPath()+ File.separator + module + File.separator + attIds[0] + File.separator  + fileName;
        }else{
          fileName = attachmentId + "." + attachmentName;
          path = YHSysProps.getAttachPath() + File.separator + module + File.separator  + fileName;
        }
        
        File file = new File(path);
        if(!file.exists()){
          if(attachmentId.indexOf("_") > 0){
            String attIds[] = attachmentId.split("_");
            fileName = attIds[1] + "_" + attachmentName;
            path = YHNtkoCont.ATTA_PATH + File.separator + module + File.separator + attIds[0] + File.separator  + fileName;
          }else{
            fileName = attachmentId + "_" + attachmentName;
            path = YHNtkoCont.ATTA_PATH + File.separator + module + File.separator  + fileName;
          }
          file = new File(path);
        }
        if(!file.exists()){
          continue;
        }
        //this.fileName = fileName;
        result += file.length();
      }
    }
    return result;
  }
  
  /**
   * 获取最大的SeqId值   * 
   * @param dbConn
   * @return
   */
  public int getMaxSeqId(Connection dbConn) {
    String sql = "select MAX(SEQ_ID) SEQ_ID from jh_docsend_info";
    int seqId = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        seqId = rs.getInt("SEQ_ID");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return seqId;
  }
  
  /**
   * 获取员工部门名称
   * 
   * @param conn
   * @param userIdStr
   * @return
   * @throws Exception
   */
  public String getUserDeptName(Connection conn, int deptId) throws Exception {
    String result = "";
    String sql = " select DEPT_NAME from oa_department where SEQ_ID =" + deptId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        result = rs.getString(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 获取外部员工部门名称
   * 
   * @param conn
   * @param userIdStr
   * @return
   * @throws Exception
   */
  public String getOutUserDeptName(Connection conn, String deptId) throws Exception {
    String result = "";
    String sql = " select DEPT_NAME from oa_dept_ext where DEPT_ID ='" + deptId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        result = rs.getString(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }

  /**
   * 发文登记列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getJsonLogic(Connection dbConn, Map request1, YHPerson person,HttpServletRequest request) throws Exception {
    try {
      String docTitle = request.getParameter("docTitle");
      String docNo = request.getParameter("docNo");
      String docType = request.getParameter("docType");
      String sendDate = request.getParameter("sendDate");
      String sendDate2 = request.getParameter("sendDate2");
      String urgentType = request.getParameter("urgentType");
      String securityLevel = request.getParameter("securityLevel");
      
      String whereStr = "";
      if (!YHUtility.isNullorEmpty(docTitle)){
        whereStr += " and c1.DOC_TITLE like '%" + YHDBUtility.escapeLike(docTitle) + "%'";
      }
      if (!YHUtility.isNullorEmpty(docNo)){
        whereStr += " and c1.DOC_NO like '%" + YHDBUtility.escapeLike(docNo) + "%'";
      }
      if (!YHUtility.isNullorEmpty(docType)){
        whereStr += " and c1.DOC_TYPE ='" + YHDBUtility.escapeLike(docType) + "'";
      }
      if (!YHUtility.isNullorEmpty(urgentType)){
        whereStr += " and c1.URGENT_TYPE ='" + YHDBUtility.escapeLike(urgentType) + "'";
      }
      if (!YHUtility.isNullorEmpty(securityLevel)){
        whereStr += " and c1.SECURITY_LEVEL ='" + YHDBUtility.escapeLike(securityLevel) + "'";
      }
      if (!YHUtility.isNullorEmpty(sendDate)) {
        whereStr += " and " + YHDBUtility.getDateFilter("c1.CREATE_DATETIME", sendDate, ">=");
      }
      if (!YHUtility.isNullorEmpty(sendDate2)) {
        whereStr += " and " + YHDBUtility.getDateFilter("c1.CREATE_DATETIME", sendDate2 + " 23:59:59", "<=");
      }
      String sql = " SELECT SEQ_ID, DOC_TITLE, DOC_TYPE, URGENT_TYPE, SECURITY_LEVEL, DOC_NO, "
      		       + " RECIVE_DEPT, RECIVE_DEPT_DESC, CREATE_DATETIME, IS_STAMP, STAMP_COMPLETE, '0' STATUS, MAIN_DOC_ID, MAIN_DOC_NAME "
      		       + " FROM jh_docsend_info c1 "
                 + " where SEND_DATETIME is null " + whereStr
                 + " order by SEQ_ID desc ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request1);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 已发发文列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getJsonCompleteLogic(Connection dbConn, Map request1, YHPerson person, HttpServletRequest request) throws Exception {
    String docTitle = request.getParameter("docTitle");
    String docNo = request.getParameter("docNo");
    String docType = request.getParameter("docType");
    String sendDate = request.getParameter("sendDate");
    String sendDate2 = request.getParameter("sendDate2");
    String urgentType = request.getParameter("urgentType");
    String securityLevel = request.getParameter("securityLevel");
    
    String whereStr = "";
    if (!YHUtility.isNullorEmpty(docTitle)){
      whereStr += " and c1.DOC_TITLE like '%" + YHDBUtility.escapeLike(docTitle) + "%'";
    }
    if (!YHUtility.isNullorEmpty(docNo)){
      whereStr += " and c1.DOC_NO like '%" + YHDBUtility.escapeLike(docNo) + "%'";
    }
    if (!YHUtility.isNullorEmpty(docType)){
      whereStr += " and c1.DOC_TYPE ='" + YHDBUtility.escapeLike(docType) + "'";
    }
    if (!YHUtility.isNullorEmpty(urgentType)){
      whereStr += " and c1.URGENT_TYPE ='" + YHDBUtility.escapeLike(urgentType) + "'";
    }
    if (!YHUtility.isNullorEmpty(securityLevel)){
      whereStr += " and c1.SECURITY_LEVEL ='" + YHDBUtility.escapeLike(securityLevel) + "'";
    }
    if (!YHUtility.isNullorEmpty(sendDate)) {
      whereStr += " and " + YHDBUtility.getDateFilter("c1.SEND_DATETIME", sendDate, ">=");
    }
    if (!YHUtility.isNullorEmpty(sendDate2)) {
      whereStr += " and " + YHDBUtility.getDateFilter("c1.SEND_DATETIME", sendDate2 + " 23:59:59", "<=");
    }
    try {
      String sql = " SELECT SEQ_ID, DOC_TITLE, DOC_TYPE, URGENT_TYPE, SECURITY_LEVEL, DOC_NO, "
                 + " RECIVE_DEPT, RECIVE_DEPT_DESC, CREATE_DATETIME, SEND_DATETIME, '0' STATUS "
                 + " ,(select count(1) from jh_docsend_tasks where DOCSEND_INFO_ID = c1.SEQ_ID ) tasks "
                 + " ,(select count(1) from jh_docsend_tasks where DOCSEND_INFO_ID = c1.SEQ_ID and (status = '3' or status = '4' or status = '6')) sendComplete "
                 + " ,(select count(1) from jh_docsend_tasks where DOCSEND_INFO_ID = c1.SEQ_ID and (status = '4' or status = '6')) complete "
                 + " FROM jh_docsend_info c1 "
                 + " where SEND_DATETIME is not null " + whereStr
                 + " order by SEQ_ID desc ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request1);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 删除文件
   * 
   * @param dbConn
   * @param seqIdStr
   * @throws Exception
   */
  public String deleteFileLogic(Connection dbConn, String seqIdStr, String filePath) throws Exception {
    YHORM orm = new YHORM();
    if (YHUtility.isNullorEmpty(seqIdStr)) {
      seqIdStr = "";
    }
    String tempReturn = "";
    try {
      String seqIdArry[] = seqIdStr.split(",");
      if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
        for (String seqId : seqIdArry) {
          StringBuffer attIdBuffer = new StringBuffer();
          StringBuffer attNameBuffer = new StringBuffer();
          YHJhDocsendInfo docsendInfo = (YHJhDocsendInfo) orm.loadObjSingle(dbConn, YHJhDocsendInfo.class, Integer.parseInt(seqId));
          String attachmentId = YHUtility.null2Empty(docsendInfo.getAttachmentId());
          String attachmentName = YHUtility.null2Empty(docsendInfo.getAttachmentName());
          attIdBuffer.append(attachmentId.trim());
          attNameBuffer.append(attachmentName.trim());
          String[] attIdArray = {};
          String[] attNameArray = {};
          if (!YHUtility.isNullorEmpty(attIdBuffer.toString()) && !YHUtility.isNullorEmpty(attNameBuffer.toString()) && attIdBuffer.length() > 0) {
            attIdArray = attIdBuffer.toString().trim().split(",");
            attNameArray = attNameBuffer.toString().trim().split("\\*");
          }
          if (attIdArray != null && attIdArray.length > 0) {
            for (int i = 0; i < attIdArray.length; i++) {
              Map<String, String> map = this.getFileName(attIdArray[i], attNameArray[i]);
              if (map.size() != 0) {
                Set<String> set = map.keySet();
                // 遍历Set集合
                for (String keySet : set) {
                  String key = keySet;
                  String keyValue = map.get(keySet);
                  String attaIdStr = this.getAttaId(keySet);
                  String fileNameValue = attaIdStr + "_" + keyValue;
                  String fileFolder = this.getFilePathFolder(key);
                  String oldFileNameValue = attaIdStr + "." + keyValue;
                  File file = new File(filePath + File.separator + fileFolder + File.separator + fileNameValue);
                  File oldFile = new File(filePath + File.separator + fileFolder + File.separator + oldFileNameValue);
                  if (file.exists()) {
                    YHFileUtility.deleteAll(file.getAbsoluteFile());
                  } else if (oldFile.exists()) {
                    YHFileUtility.deleteAll(oldFile.getAbsoluteFile());
                  }
                }
              }
            }
          }

          //删除从表中的数据
          String sql = " delete from jh_docsend_files where DOCSEND_INFO_ID="+seqId;
          String sql1 = " delete from jh_docsend_tasks where DOCSEND_INFO_ID="+seqId;
          String sql2 = " delete from jh_docsend_stamps where DOC_SEND_INFO_ID="+seqId;
          PreparedStatement ps = null;
          PreparedStatement ps1 = null;
          PreparedStatement ps2 = null;
          try {
            ps = dbConn.prepareStatement(sql);
            ps.executeUpdate();
            ps1 = dbConn.prepareStatement(sql1);
            ps1.executeUpdate();
            ps2 = dbConn.prepareStatement(sql2);
            ps2.executeUpdate();
          } catch (Exception e) {
            throw e;
          } finally {
            YHDBUtility.close(ps, null, log);
            YHDBUtility.close(ps1, null, log);
            YHDBUtility.close(ps2, null, log);
          }
          
          // 删除数据库信息
          orm.deleteSingle(dbConn, docsendInfo);
          
          tempReturn += docsendInfo.getDocTitle()+",";
        }
      }
      if(tempReturn.length() > 1 && tempReturn.endsWith(",")){
        tempReturn = tempReturn.substring(0, tempReturn.length() - 1);
      }
      return tempReturn;
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 获取详情
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHJhDocsendInfo getDetailLogic(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHJhDocsendInfo) orm.loadObjSingle(conn, YHJhDocsendInfo.class, seqId);
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 获取详情
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public List<YHJhDocsendFiles> getFilesDetailLogic(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      String filters[] = {"DOCSEND_INFO_ID="+seqId};
      return (List<YHJhDocsendFiles>) orm.loadListSingle(conn, YHJhDocsendFiles.class, filters);
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 获取详情
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public List<YHJhDocsendTasks> getFilesTasksLogic(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      String filters[] = {"DOCSEND_INFO_ID="+seqId};
      return (List<YHJhDocsendTasks>) orm.loadListSingle(conn, YHJhDocsendTasks.class, filters);
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 获取详情
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public List<YHJhDocsendStamps> getStampsLogic(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      String filters[] = {"DOC_SEND_INFO_ID="+seqId+" order by STAMP_TYPE asc "};
      return (List<YHJhDocsendStamps>) orm.loadListSingle(conn, YHJhDocsendStamps.class, filters);
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 更新发文登记
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public YHJhDocsendInfo updateDocInfo(Connection dbConn, YHFileUploadForm fileForm, YHPerson person, int seqId) throws Exception {
    YHORM orm = new YHORM();
    
    //获取页面信息
    String docTitle = fileForm.getParameter("docTitle");
    String docType = fileForm.getParameter("docType");
    String docKind = fileForm.getParameter("docKind");
    String urgentType = fileForm.getParameter("urgentType");
    String securityLevel = fileForm.getParameter("securityLevel");
    String totalPrintCount = YHUtility.isNullorEmpty(fileForm.getParameter("totalPrintCount")) ? "0" : fileForm.getParameter("totalPrintCount");
    String printCount = YHUtility.isNullorEmpty(fileForm.getParameter("printCount")) ? "0" : fileForm.getParameter("printCount");
    String paperPrintCount = YHUtility.isNullorEmpty(fileForm.getParameter("paperPrintCount")) ? "0" : fileForm.getParameter("paperPrintCount");
    
    String docNo = fileForm.getParameter("docNo");
    String pageCount = YHUtility.isNullorEmpty(fileForm.getParameter("pageCount")) ? "0" : fileForm.getParameter("pageCount");
    String attachCount = YHUtility.isNullorEmpty(fileForm.getParameter("attachCount")) ? "0" : fileForm.getParameter("attachCount");
    
    String oaMainSend = fileForm.getParameter("oaMainSend");
    String oaCopySend = fileForm.getParameter("oaCopySend");
    
    String remark = fileForm.getParameter("remark");
    String handReciveDept = fileForm.getParameter("handReciveDept");
    String reciveDept = fileForm.getParameter("reciveDept");
    String reciveDeptDesc = fileForm.getParameter("reciveDeptDesc");
    String reciveDeptFlag = fileForm.getParameter("reciveDeptFlag");
    String attachmentId = fileForm.getParameter("attachmentId");
    String attachmentName = fileForm.getParameter("attachmentName");
    String attachmentReciveDeptId = fileForm.getParameter("attachmentReciveDeptId");
    String attachmentReciveDeptDesc = fileForm.getParameter("attachmentReciveDeptDesc");
    String attachmentUploadNum = fileForm.getParameter("attachmentUploadNum");
    String mainDocId = fileForm.getParameter("mainDocId");
    String mainDocName = fileForm.getParameter("mainDocName");
    if(!YHUtility.isNullorEmpty(mainDocId)){
      Map<String, String> map = new HashMap<String, String>();
      map.put(mainDocId, mainDocName);
      long sizeMain = this.getSize(map, attachmentFolder);
      if(attachmentId.endsWith(",")){
        attachmentId += mainDocId + ",";
        attachmentName += mainDocName + "*";
        attachmentReciveDeptId += "0";
        attachmentReciveDeptDesc += "已选网络接收单位";
        attachmentUploadNum += sizeMain;
      }
      else{
        attachmentId += "," + mainDocId + ",";
        attachmentName += "*" + mainDocName + "*";
        attachmentReciveDeptId += "*0";
        attachmentReciveDeptDesc += "*已选网络接收单位";
        attachmentUploadNum += "," + sizeMain;
      }
    }
    String isSign = fileForm.getParameter("isSign");
    String isStamp = fileForm.getParameter("isStamp");
    
    String yearShow = fileForm.getParameter("yearShow");
    String mouthShow = fileForm.getParameter("mouthShow");
    String dayShow = fileForm.getParameter("dayShow");
    String sendDatatimeShow = yearShow+"-"+mouthShow+"-"+dayShow;

    try{
      
      //发文登记主表保存功能
      YHJhDocsendInfo docSendInfo = (YHJhDocsendInfo) orm.loadObjSingle(dbConn, YHJhDocsendInfo.class, seqId);
      docSendInfo.setDocTitle(docTitle);
      docSendInfo.setDocType(docType);
      docSendInfo.setDocKind(docKind);
      docSendInfo.setUrgentType(urgentType);
      docSendInfo.setSecurityLevel(securityLevel);
      docSendInfo.setTotalPrintCount(Integer.parseInt(totalPrintCount));
      docSendInfo.setPrintCount(Integer.parseInt(printCount));
      docSendInfo.setPaperPrintCount(Integer.parseInt(paperPrintCount));
      docSendInfo.setDocNo(docNo);
      docSendInfo.setPageCount(Integer.parseInt(pageCount));
      docSendInfo.setAttachCount(Integer.parseInt(attachCount));
      docSendInfo.setOaMainSend(oaMainSend);
      docSendInfo.setOaCopySend(oaCopySend);
      docSendInfo.setRemark(remark);
      docSendInfo.setHandReciveDept(handReciveDept);
      docSendInfo.setReciveDept(reciveDept);
      docSendInfo.setReciveDeptDesc(reciveDeptDesc);
      docSendInfo.setReciveDeptFlag(reciveDeptFlag);
      docSendInfo.setMainDocId(mainDocId);
      docSendInfo.setMainDocName(mainDocName);
      docSendInfo.setAttachmentId(attachmentId);
      docSendInfo.setAttachmentName(attachmentName);
      docSendInfo.setIsSign(isSign);
      docSendInfo.setIsStamp(isStamp);
      
      if(YHUtility.isDay(sendDatatimeShow)){
        docSendInfo.setSendDatetimeShow(YHUtility.parseTimeStamp(sendDatatimeShow));
      }
      else{
        docSendInfo.setSendDatetimeShow(YHUtility.parseTimeStamp());
      }
      orm.updateSingle(dbConn, docSendInfo);
      
      //删除从表中对应的信息
      String sql = " delete from jh_docsend_files where DOCSEND_INFO_ID="+seqId;
      String sql1 = " delete from jh_docsend_tasks where DOCSEND_INFO_ID="+seqId;
      PreparedStatement ps = null;
      PreparedStatement ps1 = null;
      try {
        ps = dbConn.prepareStatement(sql);
        ps.executeUpdate();
        ps1 = dbConn.prepareStatement(sql1);
        ps1.executeUpdate();
      } catch (Exception e) {
        throw e;
      } finally {
        YHDBUtility.close(ps, null, log);
        YHDBUtility.close(ps1, null, log);
      }
      
      
      //发文登记附件从表保存功能
      Map<String, String> attachMap = new HashMap<String, String>();
      String attachmentIdArray[] = attachmentId.split(",");
      String attachmentNameArray[] = attachmentName.split("\\*");
      String attachmentReciveDeptIdArray[] = attachmentReciveDeptId.split("\\*");
      String attachmentReciveDeptDescArray[] = attachmentReciveDeptDesc.split("\\*");
      String attachmentUploadNumArray[] = attachmentUploadNum.split(",");
      for(int i = 0; i < attachmentIdArray.length ;i++){
        if(!YHUtility.isNullorEmpty(attachmentIdArray[i])){
          YHJhDocsendFiles docSendAttach = new YHJhDocsendFiles();
          docSendAttach.setDocsendInfoId(seqId);
          docSendAttach.setFileId(attachmentIdArray[i]);
          docSendAttach.setFileName(attachmentNameArray[i]);
          
          String attachmentReciveDeptIdTemp = "";
          String attachmentReciveDeptNameTemp = "";
          if("0".equals(attachmentReciveDeptIdArray[i])){
            attachmentReciveDeptIdTemp = reciveDept;
            attachmentReciveDeptNameTemp = reciveDeptDesc;
          }
          else{
            attachmentReciveDeptIdTemp = attachmentReciveDeptIdArray[i];
            attachmentReciveDeptNameTemp = attachmentReciveDeptDescArray[i];;
          }
          docSendAttach.setReciveDept(attachmentReciveDeptIdTemp);
          docSendAttach.setReciveDeptDesc(attachmentReciveDeptNameTemp);
          if(attachmentIdArray[i].equals(mainDocId)){
            docSendAttach.setIsMainDoc(1);
            docSendAttach.setFileSize(attachmentUploadNumArray[i]); 
          }
          else{
            docSendAttach.setIsMainDoc(0);
            docSendAttach.setFileSize(fileForm.getParameter("attach_Size_"+attachmentUploadNumArray[i])); 
          }
          orm.saveSingle(dbConn, docSendAttach);
          attachMap.put(attachmentIdArray[i], attachmentReciveDeptIdTemp);
        }
        else{
          attachMap.put(attachmentIdArray[i], reciveDept);
        }
        
      }
      
      //map转换，从以附件为主的一对多关系转变为，以部门id为主的一对多关系
      //遍历map 发文登记任务队列从表保存功能
      Map <String, String> deptMap = YHDocUtil.attach2Dept(attachMap);
      Set<String> key = deptMap.keySet();
      Iterator<String> iKeys =  key.iterator();
      while(iKeys.hasNext()){
        String keyStr = (String) iKeys.next();
        String valueStr = (String) deptMap.get(keyStr);
        
        YHJhDocsendTasks docsendTasks = new YHJhDocsendTasks();
        docsendTasks.setDocsendInfoId(seqId);
        docsendTasks.setReciveDept(keyStr);
        docsendTasks.setReciveDeptDesc(getOutUserDeptName(dbConn, keyStr));
        docsendTasks.setAttachmentId(valueStr);
        StringBuffer fileName = new StringBuffer();
        StringBuffer fileSize = new StringBuffer();
        String valueArray[] = valueStr.split(",");
        for(int i = 0; i < valueArray.length ;i++){
          for(int j = 0; j < attachmentIdArray.length ; j++){
            if(valueArray[i].equals(mainDocId)){
              docsendTasks.setMainDocId(mainDocId);
              docsendTasks.setMainDocName(mainDocName);
            }
            if(valueArray[i].equals(attachmentIdArray[j])){
              fileName.append(attachmentNameArray[j] + "*");
              if(valueArray[i].equals(mainDocId)){
                fileSize.append(attachmentUploadNumArray[j] + ",");
              }
              else{
                fileSize.append(fileForm.getParameter("attach_Size_"+attachmentUploadNumArray[j]) + ",");
              }
              break;
            }
          }
        }
        if(fileName.length() > 1 && fileName.toString().endsWith("*")){
          fileName = fileName.deleteCharAt(fileName.length() - 1);
          fileSize = fileSize.deleteCharAt(fileSize.length() - 1);
        }
        docsendTasks.setAttachmentName(fileName.toString());
        docsendTasks.setAttachmentSize(fileSize.toString());
        
        String reciveDeptArray[] = reciveDept.split(",");
        for(int i = 0; i < reciveDeptArray.length ; i++){
          if(reciveDeptArray[i].equals(keyStr)){
            String printCountStr = fileForm.getParameter("print_count_"+i);
            String printNoStart = fileForm.getParameter("print_no_start_"+i);
            String printNoEnd = fileForm.getParameter("print_no_end_"+i);
            if(YHUtility.isInteger(printCountStr)){
              docsendTasks.setPrintCount(Integer.parseInt(printCountStr));
            }
            else{
              docsendTasks.setPrintCount(0);
            }
            docsendTasks.setPrintNoStart(printNoStart);
            docsendTasks.setPrintNoEnd(printNoEnd);
            break;
          }
        }
        docsendTasks.setStatus("0");//草稿
        docsendTasks.setProcessTime(YHUtility.parseTimeStamp());
        orm.saveSingle(dbConn, docsendTasks);
      }
      return docSendInfo;
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 新建文档
   * @param runId
   * @param newType
   * @param newName
   * @throws Exception 
   */
  public String createAttachment(String newName, Connection conn, String webrootPath, YHPerson person, String seqId) throws Exception {
    // TODO Auto-generated method stub
    String fileName = newName + ".doc";
    Calendar cld = Calendar.getInstance();
    int year =  cld.get(Calendar.YEAR)%100;
    int month = cld.get(Calendar.MONTH) + 1;
    String mon = month >= 10?month+"":"0"+month;
    String hard = year + mon ;
    String attachmentId = YHGuid.getRawGuid(); 
    
    String fileName2 = attachmentId + "_" + fileName;
    String tmp = filePath + File.separator + hard + File.separator + fileName2;
    
    File catalog = new File(filePath + File.separator +hard);
    if(!catalog.exists()){
      catalog.mkdirs();
    }
    boolean success = false;
    String srcFile = webrootPath + this.COPYPATH + File.separator + "copy.doc";
    YHFileUtility.copyFile(srcFile, tmp);
    success = true;
    if (success) {
      
      int docsendInfoId = setDocMainInfo(conn, hard + "_" + attachmentId, fileName, person, seqId);
      
      return "{seqId:\""+docsendInfoId+"\", attachmentName:\""+ YHUtility.encodeSpecial(fileName) +"\", attachmentId:'"+ hard + "_" + attachmentId +"'}";
    } else {
      return "''";
    }
  }
  
  /**
   * 处理上传附件，返回附件id，附件名称


   * 处理单文件上传的
   * @param request
   *          HttpServletRequest
   * @param
   * @return Map<String, String> ==> {id = 文件名}
   * @throws Exception
   */
  public Map<String, String> fileUploadLogic(YHFileUploadForm fileForm) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    try {
      Calendar cld = Calendar.getInstance();
      int year = cld.get(Calendar.YEAR) % 100;
      int month = cld.get(Calendar.MONTH) + 1;
      String mon = month >= 10 ? month + "" : "0" + month;
      String hard = year + mon;
      Iterator<String> iKeys = fileForm.iterateFileFields();
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        String fileNameV = fileName;
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        String rand = YHDiaryUtil.getRondom();
        fileName = rand + "_" + fileName;
        
        while (YHDiaryUtil.getExist(YHSysProps.getAttachPath() + File.separator + hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + fileName;
        }
        result.put(hard + "_" + rand, fileNameV);
        fileForm.saveFile(fieldName, YHSysProps.getAttachPath() + File.separator + attachmentFolder + File.separator + hard + File.separator + fileName);
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  
  /**
   * 新建正文/上传正文 保存发文登记信息
   * @param conn
   * @param attrId
   * @param attrName
   * @param person
   * @throws Exception 
   */
  public int setDocMainInfo(Connection conn, String attrId, String attrName, YHPerson person, String seqId) throws Exception {
    boolean flag = true;
    YHORM orm = new YHORM();
    //发文登记主表保存功能
    if(!YHUtility.isInteger(seqId)){
      seqId = "0";
    }
    YHJhDocsendInfo docSendInfo = (YHJhDocsendInfo)orm.loadObjSingle(conn, YHJhDocsendInfo.class, Integer.parseInt(seqId));
    if(docSendInfo == null){
      docSendInfo = new YHJhDocsendInfo();
      docSendInfo.setAttachmentId(attrId);
      docSendInfo.setAttachmentName(attrName);
      docSendInfo.setDocTitle("无标题");
      docSendInfo.setIsStamp("0");
      flag = false;
    }
    else{
      docSendInfo.setAttachmentId(docSendInfo.getAttachmentId()+attrId);
      docSendInfo.setAttachmentName(docSendInfo.getAttachmentName()+attrName);
      
    }
    
    docSendInfo.setMainDocId(attrId);
    docSendInfo.setMainDocName(attrName);
    docSendInfo.setCreateUser(person.getSeqId());
    docSendInfo.setCreateUserName(person.getUserName());
    docSendInfo.setCreateDept(person.getDeptId());
    docSendInfo.setCreateDeptName(getUserDeptName(conn, person.getDeptId()));
    docSendInfo.setCreateDatetime(YHUtility.parseTimeStamp());
    if(flag){
      orm.updateSingle(conn, docSendInfo);
    }
    else{
      orm.saveSingle(conn, docSendInfo);
    }
    int docsendInfoId = 0;
    if(flag){
      docsendInfoId = Integer.parseInt(seqId);
    }
    else{
      docsendInfoId = getMaxSeqId(conn);
    }
    return docsendInfoId;
  }
  
  /**
   * 是否有盖章辅助角色
   * @param request
   * @throws Exception 
   */
  public static boolean isStamp(HttpServletRequest request) throws Exception{
    
    YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    Connection dbConn = null;
    YHORM orm = new YHORM();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      person = (YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, person.getSeqId());
      String userPrivOther = person.getUserPrivOther();
      if(!YHUtility.isNullorEmpty(userPrivOther)){
        String userPrivOtherArray[] = userPrivOther.split(",");
        for(int i = 0; i <userPrivOtherArray.length; i++){
          if(YHUtility.isInteger(userPrivOtherArray[i])){
            YHUserPriv userPriv = (YHUserPriv)orm.loadObjSingle(dbConn, YHUserPriv.class, Integer.parseInt(userPrivOtherArray[i]));
            if(userPriv != null && ("机要A角".equals(userPriv.getPrivName()) || "机要B角".equals(userPriv.getPrivName()) || "盖章权限".equals(userPriv.getPrivName())) && getPasscard(dbConn)){
              return true;
            }
          }
        }
      }
      return false;
    }catch (Exception ex) {
      throw ex;
    }
  }
  
  public static boolean getPasscard(Connection dbConn) throws Exception{
    // 获取esb本地配置单位
    YHEsbClientConfig config = YHEsbClientConfig.builder(YHSysProps.getWebPath() + YHEsbConst.CONFIG_PATH);
    YHORM orm = new YHORM();
    Map<String, String> filters = new HashMap<String, String>();
    filters.put("ESB_USER", config.getUserId());
    YHExtDept extDept = (YHExtDept)orm.loadObjSingle(dbConn, YHExtDept.class, filters);
    return "1".equals(extDept.getDeptPasscard());
  }
  
  /**
   * 是否有发送辅助角色
   * @param request
   * @throws Exception 
   */
  public static boolean isSend(HttpServletRequest request) throws Exception{
    
    YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    Connection dbConn = null;
    YHORM orm = new YHORM();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      person = (YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, person.getSeqId());
      String userPrivOther = person.getUserPrivOther();
      if(!YHUtility.isNullorEmpty(userPrivOther)){
        String userPrivOtherArray[] = userPrivOther.split(",");
        for(int i = 0; i <userPrivOtherArray.length; i++){
          if(YHUtility.isInteger(userPrivOtherArray[i])){
            YHUserPriv userPriv = (YHUserPriv)orm.loadObjSingle(dbConn, YHUserPriv.class, Integer.parseInt(userPrivOtherArray[i]));
            if(userPriv != null && ("机要A角".equals(userPriv.getPrivName()) || "机要B角".equals(userPriv.getPrivName()) || "发送权限".equals(userPriv.getPrivName()))){
              return true;
            }
          }
        }
      }
      return false;
    }catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 收文数量
   * @param request
   * @throws Exception 
   */
  public static int getReceiveCount(HttpServletRequest request) throws Exception{
    
    Connection dbConn = null;
    YHORM orm = new YHORM();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String[] filters = {"status=0"};
      List<YHJhDocrecvInfo> docsendInfoList = orm.loadListSingle(dbConn, YHJhDocrecvInfo.class, filters);
      int count = docsendInfoList.size();
      return count;
    }catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 待发文数量
   * @param request
   * @throws Exception 
   */
  public static int getWaitSendCount(HttpServletRequest request) throws Exception{
    
    Connection dbConn = null;
    YHORM orm = new YHORM();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String[] filters = {"SEND_DATETIME is null"};
      List<YHJhDocsendInfo> docsendInfoList = orm.loadListSingle(dbConn, YHJhDocsendInfo.class, filters);
      int count = docsendInfoList.size();
      return count;
    }catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 更新公文发送状态
   * @param request
   * @throws Exception 
   */
  public static String updateSendDocStatus(Connection dbConn, String guid, int state, String to) throws Exception{
    YHORM orm = new YHORM();
    Map<String, String> filters = new HashMap<String, String>();
    filters.put("GUID", guid);
    YHJhDocsendTasks docsendTasks = null;
    List<YHJhDocsendTasks> docsendTasksList = null;
    if(YHUtility.isNullorEmpty(to)){
      docsendTasksList =  orm.loadListSingle(dbConn, YHJhDocsendTasks.class, filters);
    }
    else{
      
      YHDeptTreeLogic dtl = new YHDeptTreeLogic();
      YHExtDept ed = dtl.getDeptByEsbUser(dbConn, to);//根据发送部门，查询外部组织机构
      if(ed != null){
        filters.put("RECIVE_DEPT", ed.getDeptId()+"");
        docsendTasks = (YHJhDocsendTasks) orm.loadObjSingle(dbConn, YHJhDocsendTasks.class, filters);
      }
    }
    
    if(docsendTasks != null || docsendTasksList !=null){
      switch(state){
        case 1 : {
          //修改task任务为发送完毕
          for(YHJhDocsendTasks docsendTasksTemp : docsendTasksList){
            docsendTasksTemp.setStatus("3");//文件已发送到服务器
            docsendTasksTemp.setProcessTime(YHUtility.parseTimeStamp());
            orm.updateSingle(dbConn, docsendTasksTemp);
          }
          break;
        }
        case 2 : {
          //修改task任务为对方已接收
          docsendTasks.setStatus("4");//对方已接收
          docsendTasks.setProcessTime(YHUtility.parseTimeStamp());
          orm.updateSingle(dbConn, docsendTasks);
          break;
        }
        case -3 : {
          //修改task任务为对方接收失败
          docsendTasks.setStatus("-3");//对方接收失败
          docsendTasks.setProcessTime(YHUtility.parseTimeStamp());
          orm.updateSingle(dbConn, docsendTasks);
          break;
        }
        default : {
          if(docsendTasks != null){
            //修改task任务为发送失败
            docsendTasks.setStatus("-1");//发送失败
            docsendTasks.setProcessTime(YHUtility.parseTimeStamp());
            orm.updateSingle(dbConn, docsendTasks);
          }
          else{
            for(YHJhDocsendTasks docsendTasksTemp : docsendTasksList){
              docsendTasksTemp.setStatus("-1");//发送失败
              docsendTasksTemp.setProcessTime(YHUtility.parseTimeStamp());
              orm.updateSingle(dbConn, docsendTasksTemp);
            }
          }
          break;
        }
      }
      return "1";
    }
    else{
      return "";
    }
  }
  
  /**
   * 已发送成功公文签收回执
   * @param request
   * @throws Exception 
   */
  public static void parseXML(String upXMLPath,Connection dbConn, String fromUnit, String guid) throws Exception {
    SAXReader saxReader = new SAXReader();
    File XMLFile = new File(upXMLPath);
    Document document = saxReader.read(XMLFile);
    Element root = document.getRootElement();
    if(root.getName().equals("body")){
      YHJhDocsendTasks docsendTasks = new YHJhDocsendTasks();
      List<Element> elements = root.elements();
      for (Element el : elements) {
        String elName = el.getName();
        String elData = (String) el.getData() == null ? "" : (String) el.getData();
        elData = elData.trim();
        
        if (elName.equalsIgnoreCase("guid")) {
          docsendTasks.setGuid(elData);
        } 
        else if (elName.equalsIgnoreCase("sendDept")) {
          docsendTasks.setReciveDept(elData);
        }
      }
      
      try {
        YHDbconnWrap dbUtil = new YHDbconnWrap();
        dbConn = dbUtil.getSysDbConn();
        YHORM orm = new YHORM();
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("GUID", docsendTasks.getGuid());
        YHDeptTreeLogic dtl = new YHDeptTreeLogic();
        YHExtDept ed = dtl.getDeptByEsbUser(dbConn, fromUnit);//根据发送部门，查询外部组织机构
        if(ed != null){
          filters.put("RECIVE_DEPT", ed.getDeptId()+"");
          YHJhDocsendTasks docsendTasksTemp = (YHJhDocsendTasks) orm.loadObjSingle(dbConn, YHJhDocsendTasks.class, filters);
          if(docsendTasksTemp != null){
            docsendTasksTemp.setStatus("6");//对方已签收
            docsendTasksTemp.setProcessTime(YHUtility.parseTimeStamp());
            orm.updateSingle(dbConn, docsendTasksTemp);
            dbConn.commit();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        YHDBUtility.closeDbConn(dbConn, null);
      }
    }
  
  }
  
  
  
  /**
   * 签发
   * 
   * @param dbConn
   * @param fileForm
   * @param person
   * @throws Exception
   */
  public YHJhDocsendInfo updateDocInfoSign(Connection dbConn, YHFileUploadForm fileForm, YHPerson person, int seqId) throws Exception {
    YHORM orm = new YHORM();
    
    //获取页面信息
    String reciveDept = fileForm.getParameter("reciveDept");

    try{
      //发文登记主表保存功能  修改签发状态
      YHJhDocsendInfo docSendInfo = (YHJhDocsendInfo) orm.loadObjSingle(dbConn, YHJhDocsendInfo.class, seqId);
      docSendInfo.setIsSign("1");//签发状态
      orm.updateSingle(dbConn, docSendInfo);
      
      //发文登记任务队列从表修改功能
      String reciveDeptArray[] = reciveDept.split(","); 
      for(int i = 0; i < reciveDeptArray.length; i++){
        String taskId = fileForm.getParameter("taskId_"+i);
        YHJhDocsendTasks docsendTasks = (YHJhDocsendTasks) orm.loadObjSingle(dbConn, YHJhDocsendTasks.class, Integer.parseInt(taskId));
        docsendTasks.setPrintCount(Integer.parseInt(fileForm.getParameter("print_count_"+i)));
        docsendTasks.setPrintNoStart(fileForm.getParameter("print_no_start_"+i));
        docsendTasks.setPrintNoEnd(fileForm.getParameter("print_no_end_"+i));
        docsendTasks.setProcessTime(YHUtility.parseTimeStamp());
        orm.updateSingle(dbConn, docsendTasks);
      }
      return docSendInfo;
    } catch (Exception e) {
      throw e;
    }
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
   * 拼接附件Id与附件名
   * 
   * @param attachmentId
   * @param attachmentName
   * @return
   */
  public Map<String, String> getFileName(String attachmentId, String attachmentName) {
    Map<String, String> map = new HashMap<String, String>();
    if (YHUtility.isNullorEmpty(attachmentId) || YHUtility.isNullorEmpty(attachmentName)) {
      return map;
    }
    if (!"".equals(attachmentId.trim()) && !"".equals(attachmentName.trim())) {
      String attachmentIds[] = attachmentId.split(",");
      String attachmentNames[] = attachmentName.split("\\*");
      if (attachmentIds.length != 0 && attachmentNames.length != 0) {
        for (int i = 0; i < attachmentIds.length; i++) {
          map.put(attachmentIds[i], attachmentNames[i]);
        }
      }
    }
    return map;
  }

  /**
   * 得到附件的Id 兼老数据
   * 
   * @param keyId
   * @return
   */
  public String getAttaId(String keyId) {
    String attaId = "";
    if (keyId != null && !"".equals(keyId)) {
      if (keyId.indexOf('_') != -1) {
        String[] ids = keyId.split("_");
        if (ids.length > 0) {
          attaId = ids[1];
        }

      } else {
        attaId = keyId;
      }
    }
    return attaId;
  }

  /**
   * 得到该文件的文件夹名
   * 
   * @param key
   * @return
   */
  public String getFilePathFolder(String key) {
    String folder = "";
    if (key != null && !"".equals(key)) {
      if (key.indexOf('_') != -1) {
        String[] str = key.split("_");
        for (int i = 0; i < str.length; i++) {
          folder = str[0];
        }
      } else {
        folder = "all";
      }
    }
    return folder;
  }
  
  /**
   *  新建公文  syl
   * @param dbConn
   * @param doc
   * @return
   * @throws Exception
   */
  public static int addDoc(Connection dbConn, YHJhDocsendInfo doc) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, doc);
    return YHCalendarLogic.getMaSeqId(dbConn, "JH_DOCSEND_INFO");
  }
  /*
   * 新建公文附件 --syl
   */
  public static int addDocFile(Connection dbConn, YHJhDocsendFiles docFile) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, docFile);
    return 0;
   // return YHCalendarLogic.getMaSeqId(dbConn, "JH_DOC_SEND_INFO");
  }
  
  /*
   * 新建公文任务 --syl
   */
  public static int addDocTask(Connection dbConn, YHJhDocsendTasks docTask) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, docTask);
    return 0;
   // return YHCalendarLogic.getMaSeqId(dbConn, "JH_DOC_SEND_INFO");
  }
}
