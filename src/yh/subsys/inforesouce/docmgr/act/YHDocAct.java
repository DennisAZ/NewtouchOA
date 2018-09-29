package yh.subsys.inforesouce.docmgr.act;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.funcs.doc.data.YHDocFlowFormItem;
import yh.core.funcs.doc.data.YHDocFlowRunData;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.logic.YHFlowProcessLogic;
import yh.core.funcs.doc.logic.YHFlowRunLogic;
import yh.core.funcs.doc.logic.YHFlowTypeLogic;
import yh.core.funcs.doc.util.YHFlowRunUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;
import yh.subsys.inforesouce.docmgr.data.YHDocFlowRun;
import yh.subsys.inforesouce.docmgr.logic.YHDocLogic;
import yh.subsys.oa.rollmanage.data.YHRmsFile;
import yh.subsys.oa.rollmanage.logic.YHRmsFileLogic;

/**
 * ddddd
 * @author liuhan
 *
 */
public class YHDocAct {
  
  private YHRmsFileLogic logic = new YHRmsFileLogic();
  
  
  private static Logger log = Logger
    .getLogger("yh.subsys.inforesouce.docmgr.act.YHDocAct");
  public String createDoc(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String realPath = request.getRealPath("/");
      int runId = Integer.parseInt(request.getParameter("runId"));
      String docName = request.getParameter("docName");
      YHDocLogic logic = new YHDocLogic();
      String docId = logic.createAttachment(runId, docName, dbConn , realPath);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功 ");
      request.setAttribute(YHActionKeys.RET_DATA, docId);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String saveDocCreateTime(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String seqIdStr = request.getParameter("seqId");
    String sRunId = request.getParameter("runId");
    int runId = 0 ;
    if (YHUtility.isInteger(sRunId)) {
      runId = Integer.parseInt(sRunId);
    }
    int prcsId = 0;
    String sPrcsId = request.getParameter("prcsId");
    if (YHUtility.isInteger(sPrcsId)) {
      prcsId = Integer.parseInt(sPrcsId);
    }
    int flowId = 0;
    String sFlowId = request.getParameter("flowId");
    if (YHUtility.isInteger(sFlowId)) {
      flowId = Integer.parseInt(sFlowId);
    }
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDocLogic logic = new YHDocLogic();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      if (YHUtility.isNullorEmpty(seqIdStr)) {
        seqIdStr = logic.getFeedback(dbConn, runId, flowId, prcsId, user.getSeqId()) + "";
      } 
      logic.saveCreateTime(Integer.parseInt(seqIdStr), dbConn, runId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功 ");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getDoc(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      int runId = Integer.parseInt(request.getParameter("runId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      int flowPrcs = Integer.parseInt(request.getParameter("flowPrcs"));
      
      YHDocLogic logic = new YHDocLogic();
      String doc = logic.getDoc(runId, flowPrcs , flowId, dbConn);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, doc);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getDocModule(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDocLogic logic = new YHDocLogic();
      String root = request.getRealPath("/");
      String docStyle = logic.getDocStyle(root);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, docStyle);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getBookmark(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int runId = Integer.parseInt(request.getParameter("runId"));
      YHDocLogic logic = new YHDocLogic();
      String root = request.getRealPath("/");
      String docStyle = logic.getDocStyle1(root , runId , dbConn);
      
      String content = logic.getContentStyle(root);
      String style = logic.getStyle(runId ,dbConn );
      String str = "{docStyle:"+ docStyle +",style:"+ style +",content:"+content+"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getFlowType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDocLogic logic = new YHDocLogic();
      String root = request.getRealPath("/");
      String sb = "[";
      String query = "select flow_type.seq_Id , flow_type.flow_name from oa_fl_type as flow_type ,oa_fl_sort as flow_sort " 
        + " where  " 
        + " flow_sort.SEQ_ID = FLOW_TYPE.FLOW_SORT  " 
        + " AND FLOW_SORT.SORT_NAME='发文'";
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = dbConn.createStatement();
        rs = stm.executeQuery(query);
        while (rs.next()) {
          int seqId = rs.getInt("SEQ_ID");
          String flowName = rs.getString("FLOW_NAME");
          sb += "{seqId:'"+seqId+"' , flowName:'"+ flowName +"'},";
        } 
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null);
      }
      sb = YHWorkFlowUtility.getOutOfTail(sb);
      sb += "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, sb);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getRunData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int runId = Integer.parseInt(request.getParameter("runId"));
      YHDocLogic logic = new YHDocLogic();
      String root = request.getRealPath("/");
      String doc = logic.getBookmark(runId, dbConn);
      String str = "{runData:" + doc + "}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String delDoc(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int runId = Integer.parseInt(request.getParameter("runId"));
      YHDocLogic logic = new YHDocLogic();
      logic.delDoc(runId, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功 ");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delDoc1(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int runId = Integer.parseInt(request.getParameter("runId"));
      YHDocLogic logic = new YHDocLogic();
      logic.delDoc1(runId, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功 ");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String saveDocStyle(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int runId = Integer.parseInt(request.getParameter("runId"));
      String docStyle = request.getParameter("docStyle");
      YHDocLogic logic = new YHDocLogic();
      logic.saveDocStyle(runId , docStyle , dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功 ");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String loadPigeonholeData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int runId = Integer.parseInt(request.getParameter("runId"));
      int prcsId = Integer.parseInt(request.getParameter("prcsId"));
      //生成附件
      YHDocLogic logic = new YHDocLogic();
      String doc = logic.getPigeonholeData(runId, dbConn);
      String docStr = logic.getDoc(runId, dbConn , true);
      String imgPath = YHWorkFlowUtility.getImgPath(request);
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String attachmentStr = logic.pigeonholeAttachment(runId , dbConn ,loginUser, imgPath);
      String handler = logic.getHandlerTime(runId , prcsId , dbConn) ;
      String str = "{"
        + "runData:" + doc 
        + ",doc:" + docStr 
        + ",attachment:" + attachmentStr 
        + ",handlerTime:'"+ handler +"'}";
      
      //加载流程数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }  
  /**
   * 取得归档的状态
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getState(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sRunId = request.getParameter("runId");
      String sFlowId = request.getParameter("flowId");
      String sPrcsId = request.getParameter("flowPrcs");
      int prcsId = 0 ;
      int flowId = 0 ;
      int runId = 0 ;
      if (YHUtility.isInteger(sRunId)) {
        runId = Integer.parseInt(sRunId);
      }
      if (YHUtility.isInteger(sFlowId)) {
        flowId = Integer.parseInt(sFlowId);
      }
      if (YHUtility.isInteger(sPrcsId)) {
        prcsId = Integer.parseInt(sPrcsId);
      }
      YHDocLogic logic = new YHDocLogic();
      boolean flag = logic.getState(runId, prcsId, flowId, dbConn);
      //加载流程数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, flag + "");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  public String pigeonhole(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int runId = Integer.parseInt(request.getParameter("runId"));
      String  docName  = request.getParameter("docName");
      String docId = request.getParameter("docId");
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      

      String fileCode = (String) request.getParameter("fileCode");
      String fileSubject = (String) request.getParameter("fileSubject");
      String fileTitle = (String) request.getParameter("fileTitle");

      String fileTitleo = (String) request.getParameter("fileTitleo");
      String sendUnit = (String) request.getParameter("sendUnit");
      String sendDate = (String) request.getParameter("sendDate");
      String secret = (String) request.getParameter("secret");
      String urgency = (String) request.getParameter("urgency");
      String fileType = (String) request.getParameter("fileType");
      String fileKind = (String) request.getParameter("fileKind");
      String filePage = (String) request.getParameter("filePage");
      String printPage = (String) request.getParameter("printPage");
      String remark = (String) request.getParameter("remark");
      String rollIdStr = (String) request.getParameter("rollId");
      String downloadYnStr = (String) request.getParameter("downloadYn");
      String handlerTime = (String)request.getParameter("handlerTime");
      String turnCount = (String)request.getParameter("turnCount");

      int rollId = 0;
      int downloadYn = 0;
      if (!YHUtility.isNullorEmpty(rollIdStr)) {
        rollId = Integer.parseInt(rollIdStr);
      }
      if (!YHUtility.isNullorEmpty(downloadYnStr)) {
        downloadYn = Integer.parseInt(downloadYnStr);
      }


      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

      // 保存从文件柜、网络硬盘选择附件
      YHSelAttachUtil sel = new YHSelAttachUtil(request, "roll_manage");
      String attIdStr = sel.getAttachIdToString(",");
      String attNameStr = sel.getAttachNameToString("*");

      boolean fromFolderFlag = false;
      String newAttchId = "";
      String newAttchName = "";
      if (!"".equals(attIdStr) && !"".equals(attNameStr)) {
        newAttchId = attIdStr + ",";
        newAttchName = attNameStr + "*";
        fromFolderFlag = true;

      }
      YHRmsFile rmsFile = new YHRmsFile();
      rmsFile.setAttachmentId(attachmentId);
      rmsFile.setAttachmentName(attachmentName);
      rmsFile.setDocAttachmentId(docId);
      rmsFile.setDocAttachmentName(docName);

      rmsFile.setAddUser(String.valueOf(person.getSeqId()));
      rmsFile.setAddTime(YHUtility.parseTimeStamp());
      rmsFile.setFileCode(fileCode);

      rmsFile.setFileTitle(fileTitle);
      rmsFile.setFileTitleo(fileTitleo);
      rmsFile.setFileSubject(fileSubject);

      rmsFile.setSendUnit(sendUnit);
      rmsFile.setSendDate(YHUtility.parseDate(sendDate));
      rmsFile.setSecret(secret);
      rmsFile.setUrgency(urgency);
      rmsFile.setFileKind(fileKind);

      rmsFile.setFileType(fileType);
      rmsFile.setFilePage(filePage);
      rmsFile.setPrintPage(printPage);
      rmsFile.setRemark(remark);
      rmsFile.setRollId(rollId);
      rmsFile.setDownloadYn(downloadYn);
      rmsFile.setHandlerTime(handlerTime);
      rmsFile.setTurnCount(turnCount);
      
      this.logic.addRmsFileInfo(dbConn, rmsFile);
      YHDocLogic logic = new YHDocLogic();
      logic.updateFlowRun(runId, dbConn);
      //加载流程数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }  
  public String getDocType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      YHDocLogic logic = new YHDocLogic();
      String data = logic.getDocType(person, dbConn);
      //加载流程数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  public String getDocWordByType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      YHDocLogic logic = new YHDocLogic();
      String type = request.getParameter("type");
      String data = logic.getDocWordByType(person, dbConn, type);
      //加载流程数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  public String getWord(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String wordKey = request.getParameter("wordKey");
      if (wordKey == null) {
        wordKey = "";
      }
      String realPath = request.getRealPath("/");
      String path = realPath + "/subsys/inforesource/docmgr/sendManage/selectWord/js/keyword.txt";
      List<String> list = new ArrayList();
      YHFileUtility.loadLine2Array(path, list);
      StringBuffer sb = new StringBuffer();
      sb.append("[");
      int count = 0 ;
      for (String s : list) {
        if ("".equals(wordKey) || s.indexOf(wordKey) != -1 ) {
          sb.append("\"" + YHUtility.encodeSpecial(s) + "\",");
          count++;
        }
      }
      if (count > 0 ) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      //加载流程数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  public String getDocNum(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int runId = Integer.parseInt(request.getParameter("runId"));
      YHDocLogic logic = new YHDocLogic();
      String str = logic.getDocNum(dbConn,runId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getNum(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String docWord1 = request.getParameter("docWord");
      int docWord = Integer.parseInt(docWord1);
      String year = request.getParameter("docYear");
      YHDocLogic logic = new YHDocLogic();
      int str = logic.getNum(dbConn, year, docWord);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, "" + str);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String sendNum(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String docWord1 = request.getParameter("docWordSeqId");
      int docWord = Integer.parseInt(docWord1);
      String doc = request.getParameter("doc");
      String year = request.getParameter("docYear");
      int runId = Integer.parseInt(request.getParameter("runId"));
      int docNum = Integer.parseInt(request.getParameter("docNum"));
      
      YHDocLogic logic = new YHDocLogic();
      logic.sendNum(dbConn, year, docWord , doc , runId , docNum);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 新建一个工作
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String createWorkFlow(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
   String flowName = request.getParameter("flowName");
   Connection dbConn = null;
    try {
      YHORM orm = new YHORM();
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowProcessLogic fpl = new YHFlowProcessLogic();
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      YHFlowRunUtility fru = new YHFlowRunUtility();
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      String docWord = request.getParameter("word");
      int wordId = Integer.parseInt(request.getParameter("wordId"));
      int docType = Integer.parseInt(request.getParameter("docType"));
      
      String docStyle = request.getParameter("docStyle");
      String flowRunName = docStyle.replace("${文件字}", docWord);
      String year = YHUtility.getCurDateTimeStr().split("-")[0];
      flowRunName = flowRunName.replace("${年号}", year);
     // flowRunName = flowRunName.replace("${文号}", "${文号}");
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDocFlowType flowType = flowTypeLogic.getFlowTypeById(flowId , dbConn);      
      //重名
      YHFlowRunLogic frl = new YHFlowRunLogic();
      int runId = frl.createNewWork(loginUser, flowType, flowRunName , dbConn );
      //flowRunName = flowRunName.replace("${文号}", "${预分配文号"+runId+"}");
      
      String query = "delete from oa_officialdoc_fl_run where RUN_ID='" + runId + "'";
      YHWorkFlowUtility.updateTableBySql(query, dbConn);
      YHDocFlowRun doc = new YHDocFlowRun(runId, flowRunName,year, wordId,
          new Date(),  docType);
      orm.saveSingle(dbConn, doc);
      
      frl.updateRunName(flowRunName, runId, dbConn);
      Map queryItem = new HashMap();
      queryItem.put("FORM_ID", flowType.getFormSeqId());
      
      List<YHDocFlowFormItem> list = orm.loadListSingle(dbConn, YHDocFlowFormItem.class, queryItem);
      for(YHDocFlowFormItem tmp : list){
        int itemId = tmp.getItemId();
        String itemData =  (String)request.getParameter(tmp.getTitle());
        if ("文件字".equals(tmp.getTitle())) {
          itemData = docWord;
        }
        if (itemData != null) {
          Map queryMap = new HashMap();
          queryMap.put("RUN_ID", runId);
          queryMap.put("ITEM_ID", itemId);
          YHDocFlowRunData flowRunData = (YHDocFlowRunData) orm.loadObjSingle(dbConn, YHDocFlowRunData.class, queryMap);
          if(flowRunData != null){
            flowRunData.setItemData((itemData == null ? "" : itemData));
            orm.updateSingle(dbConn, flowRunData);
          }else{
            flowRunData =  new YHDocFlowRunData();
            flowRunData.setItemId(itemId);
            flowRunData.setRunId(runId);
            flowRunData.setItemData((itemData == null ? "" : itemData));
            orm.saveSingle(dbConn, flowRunData);
          }
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "新建成功!");
      request.setAttribute(YHActionKeys.RET_DATA, "{runId:" + runId + ",flowId:" + flowId + "}");
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
