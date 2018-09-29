package yh.subsys.oa.hr.setting.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.setting.data.YHHrCode;
import yh.subsys.oa.hr.setting.logic.YHHrCodeLogic;

public class YHHrCodeAct {
  /**
   * 新建
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String addCode(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String codeName = request.getParameter("codeName");
      String codeNo = request.getParameter("codeNo");
      String codeOrder = request.getParameter("codeOrder");
      String codeFlag = request.getParameter("codeFlag");
      String parentNo = request.getParameter("parentNo");//seqId
      YHHrCodeLogic codeLogic = new YHHrCodeLogic();
      if(YHUtility.isInteger(parentNo)){
        YHHrCode code = codeLogic.getCodeById(dbConn, parentNo) ;
        if(code != null && !YHUtility.isNullorEmpty(code.getCodeNo())){
          parentNo = code.getCodeNo();
        }else{
          parentNo = "";
        }
      }else{
        parentNo = "";
      }
      if(YHUtility.isNullorEmpty(codeFlag)){
        codeFlag = "0";
      }
  

      String errer = "";
      if(codeLogic.checkCodeNo(dbConn, codeNo,"")){
        errer = "1";
      }else{
        YHHrCode code = new YHHrCode();
        code.setCodeName(codeName);
        code.setCodeNo(codeNo);
        code.setCodeOrder(codeOrder);
        code.setParentNo(parentNo);
        code.setCodeFlag(codeFlag);
        codeLogic.addCode(dbConn, code);
      }
      String data = "{errer:\"" + errer + "\"}";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 新建下一级code
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String addChildCode(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String codeName = request.getParameter("codeName");
      String codeNo = request.getParameter("codeNo");
      String codeOrder = request.getParameter("codeOrder");
      String codeFlag = request.getParameter("codeFlag");
      String parentNo = request.getParameter("parentNo");//seqId
      YHHrCodeLogic codeLogic = new YHHrCodeLogic();
      if(YHUtility.isInteger(parentNo)){
        YHHrCode code = codeLogic.getCodeById(dbConn, parentNo) ;
        if(code != null && !YHUtility.isNullorEmpty(code.getCodeNo())){
          parentNo = code.getCodeNo();
        }else{
          parentNo = "";
        }
      }else{
        parentNo = "";
      }
      if(YHUtility.isNullorEmpty(codeFlag)){
        codeFlag = "";
      }else{
        codeFlag = "1";
      }
      String errer = "";
      if(codeLogic.checkCodeNo(dbConn, codeNo,parentNo,"")){
        errer = "1";
      }else{
        YHHrCode code = new YHHrCode();
        code.setCodeName(codeName);
        code.setCodeNo(codeNo);
        code.setCodeOrder(codeOrder);
        code.setParentNo(parentNo);
        code.setCodeFlag(codeFlag);
        codeLogic.addCode(dbConn, code);
      }
      String data = "{errer:\"" + errer + "\"}";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 更新
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateCode(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String seqId = request.getParameter("seqId");
      String codeName = request.getParameter("codeName");
      String codeNo = request.getParameter("codeNo");
      String codeOrder = request.getParameter("codeOrder");
      String codeFlag = request.getParameter("codeFlag");
      String parentNo = request.getParameter("parentNo");
      if(YHUtility.isNullorEmpty(parentNo)){
        parentNo = "";
      }
      if(YHUtility.isNullorEmpty(codeFlag)){
        codeFlag = "0";
      }
      YHHrCodeLogic codeLogic = new YHHrCodeLogic();

      String errer = "";
      if(codeLogic.checkCodeNo(dbConn, codeNo,seqId)){
        errer = "1";
      }else{
        if(YHUtility.isInteger(seqId)){
   /*       YHHrCode code = new YHHrCode();
          code.setSeqId(Integer.parseInt(seqId));
          code.setCodeName(codeName);
          code.setCodeNo(codeNo);
          code.setCodeOrder(codeOrder);
          code.setParentNo(parentNo);
          code.setCodeFlag(codeFlag);*/
          YHHrCode code = codeLogic.getCodeById(dbConn, seqId);
          String oldCodeNo = "";
          if(code != null && !YHUtility.isNullorEmpty(code.getCodeNo())){
            oldCodeNo = code.getCodeNo();
          }
          codeLogic.updateCode(dbConn, seqId,codeNo, codeName, codeOrder, codeFlag);
          codeLogic.updateChildCode(dbConn, oldCodeNo, codeNo);//更新子代码类别
        }
      }
      String data = "{errer:\"" + errer + "\"}";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 更新
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateChildCode(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String seqId = request.getParameter("seqId");
      String codeName = request.getParameter("codeName");
      String codeNo = request.getParameter("codeNo");
      String codeOrder = request.getParameter("codeOrder");
      String codeFlag = request.getParameter("codeFlag");
      String parentNo = request.getParameter("parentNo");
      YHHrCodeLogic codeLogic = new YHHrCodeLogic();
      if(YHUtility.isInteger(parentNo)){
        YHHrCode code = codeLogic.getCodeById(dbConn, parentNo) ;
        if(code != null && !YHUtility.isNullorEmpty(code.getCodeNo())){
          parentNo = code.getCodeNo();
        }else{
          parentNo = "";
        }
      }else{
        parentNo = "";
      }
      if(YHUtility.isNullorEmpty(codeFlag)){
        codeFlag = "0";
      }


      String errer = "";
      if(codeLogic.checkCodeNo(dbConn, codeNo,parentNo,seqId)){
        errer = "1";
      }else{
        if(YHUtility.isInteger(seqId)){
          YHHrCode code = codeLogic.getCodeById(dbConn, seqId);
          String oldCodeNo = "";
          if(code != null && !YHUtility.isNullorEmpty(code.getCodeNo())){
            oldCodeNo = code.getCodeNo();
          }
          codeLogic.updateCode(dbConn, seqId,codeNo, codeName, codeOrder, codeFlag);
        }
      }
      String data = "{errer:\"" + errer + "\"}";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到ById
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String selectCodeById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String seqId = request.getParameter("seqId");
      YHHrCodeLogic codeLogic = new YHHrCodeLogic();
      String data = "";
      if(YHUtility.isInteger(seqId)){
        YHHrCode code = codeLogic.getCodeById(dbConn, seqId);
        if(code != null){
          data = YHFOM.toJson(code).toString();
        }
      }
      if(data.equals("")){
        data = "{}";
      }
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到所有的父级代码表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String selectCode(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String[] str = {"PARENT_NO = '' or PARENT_NO is null"};
      String seqId = request.getParameter("seqId");
      YHHrCodeLogic codeLogic = new YHHrCodeLogic();
      String data = "[";
      List<YHHrCode> codeList = new ArrayList<YHHrCode>();
      codeList = codeLogic.getCode(dbConn, str);
      for (int i = 0; i < codeList.size(); i++) {
        YHHrCode code = codeList.get(i);
        data = data + YHFOM.toJson(code) + ",";
      }
      if(codeList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 根据父级得到子集代码表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String selectChildCode(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String parentNo = request.getParameter("parentNo");
      if(YHUtility.isNullorEmpty(parentNo)){
        parentNo = "";
      }
      String seqId = request.getParameter("seqId");
      YHHrCodeLogic codeLogic = new YHHrCodeLogic();
      String data = "[";
      List<YHHrCode> codeList = new ArrayList<YHHrCode>();
      codeList = codeLogic.getChildCode(dbConn, parentNo);
      for (int i = 0; i < codeList.size(); i++) {
        YHHrCode code = codeList.get(i);
        data = data + YHFOM.toJson(code) + ",";
      }
      if(codeList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 删除
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String deleteCode(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String seqId = request.getParameter("seqId");
      YHHrCodeLogic codeLogic = new YHHrCodeLogic();
      if(YHUtility.isInteger(seqId)){
        codeLogic.delCodeById(dbConn, seqId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
