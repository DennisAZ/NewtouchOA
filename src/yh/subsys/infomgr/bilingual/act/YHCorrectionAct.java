package yh.subsys.infomgr.bilingual.act;

import java.io.PrintWriter;
import javax.mail.MessagingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.picture.act.YHImageUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHGuid;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.core.util.mail.YHMailSenderInfo;
import yh.core.util.mail.YHSimpleMailSender;
import yh.subsys.infomgr.bilingual.data.YHBilingualCorrection;
import yh.subsys.infomgr.bilingual.logic.YHCorrectionLogic;

public class YHCorrectionAct {
  public static final String CORRECTION_PATH = "\\bilingual\\correction";
  
  private YHCorrectionLogic logic = new YHCorrectionLogic();
   
  /**
   * 增加记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addCorrection(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    YHFileUploadForm fileForm = new YHFileUploadForm();
    
    fileForm.parseUploadRequest(request);
    
    Iterator<String> iKeys = fileForm.iterateFileFields();
    
    YHImageUtil iu = new YHImageUtil();
    String fileName = "";
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      fileName = fileForm.getFileName(fieldName);
      if (YHUtility.isNullorEmpty(fileName)) {
        continue;
      }
      String sp = System.getProperty("file.separator");
      String filePath = request.getSession().getServletContext().getRealPath(sp) + YHCorrectionAct.CORRECTION_PATH + "\\";
      fileName = YHGuid.getRawGuid() + "_" + fileName;
      fileForm.saveFile(fieldName, filePath + fileName);
      iu.saveImageAsJpg(filePath + fileName, filePath + "thumb-" + fileName, 100, 100);
    }
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map map = fileForm.getParamMap();
      YHBilingualCorrection bi = (YHBilingualCorrection) YHFOM.build(map, YHBilingualCorrection.class, "");
      bi.setPicture(fileName);
      bi.setFlag("0");
      this.logic.addCorrection(dbConn, bi);
      
      request.setAttribute("msg", "添加成功");
    }catch(Exception ex) {
      request.setAttribute("msg", "添加未成功");
      throw ex;
    }finally{
      
    }
    return "/subsys/infomgr/bilingual/success.jsp";
  }
  
  /**
   * 增加记录网站使用
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String add4website(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    return addCorrection(request, response);
    /*
    YHFileUploadForm fileForm = new YHFileUploadForm();
    
    fileForm.parseUploadRequest(request);
    
    Iterator<String> iKeys = fileForm.iterateFileFields();
    
    YHImageUtil iu = new YHImageUtil();
    String fileName = "";
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      fileName = fileForm.getFileName(fieldName);
      if (YHUtility.isNullorEmpty(fileName)) {
        continue;
      }
      String sp = System.getProperty("file.separator");
      String filePath = request.getSession().getServletContext().getRealPath(sp) + YHCorrectionAct.CORRECTION_PATH + "\\";
      fileName = YHGuid.getRawGuid() + "_" + fileName;
      fileForm.saveFile(fieldName, filePath + fileName);
      iu.saveImageAsJpg(filePath + fileName, filePath + "thumb-" + fileName, 100, 100);
    }
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map map = fileForm.getParamMap();
      YHBilingualCorrection bi = (YHBilingualCorrection) YHFOM.build(map, YHBilingualCorrection.class, "");
      bi.setFlag("0");
      bi.setPicture(fileName);
      this.logic.addCorrection(dbConn, bi);
      request.setAttribute("msg", "感谢您的参与!");
    }catch(Exception ex) {
      request.setAttribute("msg", "添加未成功");
      throw ex;
    }finally{
      return "/subsys/infomgr/correction/success.jsp";
    }*/
  }
  
  /**
   * 修改一条记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String modifyCorrection(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map map = request.getParameterMap();
      YHBilingualCorrection bi = (YHBilingualCorrection) YHFOM.build(map, YHBilingualCorrection.class, "");
      
      this.logic.modifyCorrection(dbConn, bi);
      request.setAttribute("msg", "修改成功");
    }catch(Exception ex) {
      request.setAttribute("msg", "修改未成功");
      ex.printStackTrace();
      throw ex;
    }finally{
      return "/subsys/infomgr/bilingual/success.jsp";
    }
  }
  
  /** 
  * 取得双语标示的信息,并分页  * @param request 
  * @param response 
  * @return 
  * @throws Exception 
  */ 
  public String getPage(HttpServletRequest request, 
  HttpServletResponse response) throws Exception { 

    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
    
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap()); 
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, 
      queryParam, 
      "select SEQ_ID" +
      ",CONTENT" +
      ",PICTURE" +
      ",CHANGES" +
      ",TYPE" +
      ",LOCATION" +
      ",CORRECT_DATE" +
      ",CORRECTER" +
      ",WORKPLACE" +
      ",EMAIL" +
      ",ADDRESS" +
      ",TEL" +
      ",FLAG" +
      " from BILINGUAL_CORRECTION" +
      " order by CORRECT_DATE desc"); 
    
      PrintWriter pw = response.getWriter(); 
      pw.println(pageDataList.toJson()); 
      pw.flush(); 
  
      return null; 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
  }
  
  /** 
   * 取得双语标示的信息,并分页   * @param request 
   * @param response 
   * @return 
   * @throws Exception 
   */ 
  public String getPage4WebSite(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 
    
    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
      
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap()); 
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, 
          queryParam, 
          "select SEQ_ID" +
          ",CONTENT" +
          ",PICTURE" +
          ",ADDRESS" +
          ",CHANGES" +
          " from BILINGUAL_CORRECTION" +
          " where FLAG = '1'" +
      " order by CORRECT_DATE desc"); 
      
      PrintWriter pw = response.getWriter(); 
      pw.println(pageDataList.toJson()); 
      pw.flush(); 
      
      return null; 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
  }
  
  /** 
   * 取得双语标示未确认的信息,并分页
   * @param request 
   * @param response 
   * @return 
   * @throws Exception 
   */ 
  public String getPageNotConfirm(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 
    
    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
      
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap()); 
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, 
          queryParam, 
          "select SEQ_ID" +
          ",CONTENT" +
          ",PICTURE" +
          ",CHANGES" +
          ",TYPE" +
          ",LOCATION" +
          ",CORRECT_DATE" +
          ",CORRECTER" +
          ",WORKPLACE" +
          ",EMAIL" +
          ",ADDRESS" +
          ",TEL" +
          ",FLAG" +
          " from BILINGUAL_CORRECTION" +
          " where FLAG = '0'" +
          " order by CORRECT_DATE desc"); 
      
      PrintWriter pw = response.getWriter(); 
      pw.println(pageDataList.toJson()); 
      pw.flush(); 
      
      return null; 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
  }
  
  public String getNotConfirm(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 
    Connection dbConn = null; 
    YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
    List<YHBilingualCorrection> corrs = logic.getNotConfirmSqServer(dbConn);
    StringBuffer sb = new StringBuffer();
    int len = corrs.size();
    sb.append("[");
    for(int i=0 ; i<len; i++){
      if(i < len-1){
       sb.append("{ id:").append(corrs.get(i).getSeqId()).append(", location:'").append(YHUtility.encodeSpecial(corrs.get(i).getLocation())).append("', picture: '").append(YHUtility.encodeSpecial(corrs.get(i).getPicture())).append("', content:'").append(YHUtility.encodeSpecial(corrs.get(i).getContent())).append("'},");
      }else{
       sb.append("{ id:").append(corrs.get(len-1).getSeqId()).append(", location:'").append(YHUtility.encodeSpecial(corrs.get(len-1).getLocation())).append("', picture:'").append(YHUtility.encodeSpecial(corrs.get(len-1).getPicture())).append("', content:'").append(YHUtility.encodeSpecial(corrs.get(len-1).getContent())).append("'}");         
      }
    }
    sb.append("]");
    //YHOut.println(sb.toString());
    PrintWriter pw = response.getWriter();    
    String rtData = "{rtState:'0',rtData:"+sb.toString()+"}";
    pw.println(rtData);    
    pw.flush(); 
    return null;
  }
  
  /**
   * 通过条件查找记录,并分页
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String searchPage(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 
    
    String location = request.getParameter("location");
    location = location == null ? "" : location;
    
    String correctDate = request.getParameter("correctDate");
    
    if (correctDate == null || "".equals(correctDate)){
      correctDate = "";
    }
    else {
      correctDate = " and CORRECT_DATE = '" + correctDate + "'";
    }
    
    String type = request.getParameter("type");
    type = type == null ? "" : type;
    
    String tel = request.getParameter("tel");
    tel = tel == null ? "" : tel;
    
    String content = request.getParameter("content");
    content = content == null ? "" : content;
    content = java.net.URLDecoder.decode(content, "utf-8");
    
    String address = request.getParameter("address");
    address = address == null ? "" : address;
    address = java.net.URLDecoder.decode(address, "utf-8");
    
    String correcter = request.getParameter("correcter");
    correcter = correcter == null ? "" : correcter;
    correcter = java.net.URLDecoder.decode(correcter, "utf-8");
    
    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
      String sql = "select SEQ_ID" +
      ",CONTENT" +
      ",PICTURE" +
      ",CHANGES" +
      ",TYPE" +
      ",LOCATION" +
      ",CORRECT_DATE" +
      ",CORRECTER" +
      ",WORKPLACE" +
      ",EMAIL" +
      ",ADDRESS" +
      ",TEL" +
      ",FLAG" +
      " from BILINGUAL_CORRECTION" +
      " where LOCATION like '%" + YHDBUtility.escapeLike(location) +
      "%'" +
      correctDate +
      " and TEL like '%" + YHDBUtility.escapeLike(tel) +
      "%'" +
      " and CONTENT like '%" + YHDBUtility.escapeLike(content) +
      "%'" +
      " and ADDRESS like '%" + YHDBUtility.escapeLike(address) +
      "%'" +
      " and CORRECTER like '%" + YHDBUtility.escapeLike(correcter) +
      "%'" +
      " and TYPE like '%" + YHDBUtility.escapeLike(type) +
      "%'" +
      " order by CORRECT_DATE desc";
      
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      
      //System.out.println(sql);
      
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap()); 
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, 
          queryParam, 
          sql); 
      
      PrintWriter pw = response.getWriter(); 
      pw.println(pageDataList.toJson()); 
      pw.flush(); 
      
      return null; 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
  }
  
  /**
   * 删除记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteRecord(HttpServletRequest request,
          HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      this.logic.deleteRecord(dbConn, Integer.parseInt(seqId));
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除记录");
      
    }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 确定记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String confirmRecord(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      this.logic.confirmRecord(dbConn, Integer.parseInt(seqId));
      
      YHBilingualCorrection bi = this.logic.queryRecord(dbConn, Integer.parseInt(seqId));
      String toAddress = bi.getEmail();
      if (toAddress != null) {
        if (checkEmail(toAddress)) {
          sendMail(toAddress);
        }
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除记录");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 按id查询单条记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryRecord(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHBilingualCorrection bi = (YHBilingualCorrection)this.logic.queryRecord(dbConn, Integer.parseInt(seqId));
      
      request.setAttribute(YHActionKeys.RET_DATA, YHFOM.toJson(bi).toString()); 
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功"); 
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage()); 
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  private void sendMail(String toAddress, String content, String subject) throws Exception {
    // 这个类主要是设置邮件
    YHMailSenderInfo mailInfo = YHMailSenderInfo.build("mailConfBiligual");
    
    if (mailInfo == null) {
      //System.out.println("请检查邮件配置信息是否正确！");
      return;
    }
    
    mailInfo.setToAddress(toAddress);
    mailInfo.setSubject(content);
    mailInfo.setContent(subject);
    
    // 这个类主要来发送邮件
    YHSimpleMailSender sms = new YHSimpleMailSender();
    sms.sendTextMail(mailInfo);// 发送文体格式

    //System.out.println("发送邮件成功！");
  }
  
  private static void sendMail(String toAddress) throws Exception {
    // 这个类主要是设置邮件
    YHMailSenderInfo mailInfo = YHMailSenderInfo.build("mailConfBiligual");
    
    if (mailInfo == null) {
      //System.out.println("请检查邮件配置信息是否正确！");
      return;
    }
    
    mailInfo.setToAddress(toAddress);
    
    // 这个类主要来发送邮件
    YHSimpleMailSender sms = new YHSimpleMailSender();
    sms.sendTextMail(mailInfo);// 发送文体格式
    
    //System.out.println("发送邮件成功！");
  }
  
  private boolean checkEmail(String email) {
    
    Pattern pattern = Pattern.compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$");
    Matcher matcher = pattern.matcher(email);
    if (matcher.matches()) {
        return true;
    }
    return false;
  }
}
