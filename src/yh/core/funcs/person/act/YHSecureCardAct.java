package yh.core.funcs.person.act;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHSecureKey;
import yh.core.funcs.person.logic.YHSecureCardLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import seamoonotp.seamoonapi;

public class YHSecureCardAct {
  YHSecureCardLogic logic = new YHSecureCardLogic();

  public String importSecureCard(HttpServletRequest request, HttpServletResponse response) throws Exception {
    InputStream is = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      is = fileForm.getInputStream();
      
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      int count = 0;
      String temp;
      while(!YHUtility.isNullorEmpty(temp = br.readLine())){
        String secureKeyStr[] = temp.split(",");
      
        YHORM orm = new YHORM();
        YHSecureKey secureCard = new YHSecureKey();
        secureCard.setKeySn(secureKeyStr[0]);
        secureCard.setKeyInfo(secureKeyStr[1]);
        
        if(!this.logic.isExist(dbConn, secureCard.getKeySn())){
          orm.saveComplex(dbConn, secureCard);
          count++;
        }
      }
      return "/core/funcs/person/importSecureCard.jsp?flag="+count;
    }
    catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      // ex.printStackTrace();
      throw ex;
    }
  }
  
  
  /**
   * 动态密保卡 通用列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSecureCard(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getSecureCard(dbConn, request.getParameterMap(), person);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  public String bindUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userId = request.getParameter("userId");
    String keySn = request.getParameter("keySn");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.bindUser(dbConn, userId, keySn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteSecureCard(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String seqIdStrs = request.getParameter("seqIds");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.deleteSecureCard(dbConn, seqIdStrs);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String unBindSecureCard(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String seqIdStrs = request.getParameter("seqIds");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.unBindSecureCard(dbConn, seqIdStrs);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String passwordsyn(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String seqId = request.getParameter("seqId");
    String password = request.getParameter("password");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sninfo = this.logic.getKeySn(dbConn, seqId);
      seamoonapi sc=new seamoonapi();
      String keyInfo = sc.passwordsyn(sninfo, password);
      String data = "";
      if(keyInfo.length() > 3){
        data = "同步成功";
        YHORM orm = new YHORM();
        YHSecureKey secureCard = new YHSecureKey();
        secureCard.setSeqId(Integer.parseInt(seqId));
        secureCard.setKeyInfo(keyInfo);
        orm.updateSingle(dbConn, secureCard);
      }
      else if("0".equals(keyInfo)){
        data = "动态密码错误";
      }
      else if("-1".equals(keyInfo)){
        data = "未知内部错误";
      }
      else if("-2".equals(keyInfo)){
        data = "动态加密字符串有错";
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 下载CSV模板
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String downCSVTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception {
    response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      String fileName = URLEncoder.encode("批量绑定动态密码卡模板.csv", "UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
      ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
      YHDbRecord record = new YHDbRecord();
      record.addField("用户名", "");
      record.addField("卡号", "");
      dbL.add(record);
      YHCSVUtil.CVSWrite(response.getWriter(), dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return null;
  }
  
  /**
   * 导入CSV批量绑定密码卡
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String impSecureCardInfoToCsv(HttpServletRequest request, HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
      int count = this.logic.impSecureCardInfoToCsv(dbConn, fileForm, person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "导入数据成功！");
      return "/core/funcs/person/importBindCard.jsp?flag=1&count="+count;
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "导入数据失败");
      throw e;
    }
  }
}
