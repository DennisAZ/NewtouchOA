package yh.subsys.oa.finance.act;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.codeclass.data.YHCodeItem;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.attendance.manage.logic.YHManageOutLogic;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.finance.data.YHBudgetApply;
import yh.subsys.oa.finance.data.YHBudgetDeptTotal;
import yh.subsys.oa.finance.logic.YHBudgetApplyLogic;
import yh.subsys.oa.finance.logic.YHBudgetDeptTotalLogic;
import yh.subsys.oa.finance.logic.YHChargeExpenseLogic;
import yh.subsys.oa.finance.logic.YHFinanceApplyRecordLogic;
import yh.subsys.oa.giftProduct.instock.logic.YHGiftInstockLogic;

public class YHBudgetApplyAct {
  /**
   * 新建预算
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addBudgetApply(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyMM");
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      Calendar cl = Calendar.getInstance();
      int curYear = cl.get(Calendar.YEAR);
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      String budgetYear = fileForm.getParameter("budgetYear");
      if(budgetYear==null||!YHUtility.isInteger(budgetYear)){
        budgetYear = curYear + "";
      }
      // 保存从文件柜、网络硬盘选择附件
      YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, "finance");
      String attIdStr = sel.getAttachIdToString(",");
      String attNameStr = sel.getAttachNameToString("*");
      
      String budgetProposer = fileForm.getParameter("budgetProposer");
      String budgetItem = fileForm.getParameter("budgetItem");
      String deptId = fileForm.getParameter("deptId");
      String budgetAvailabein = fileForm.getParameter("budgetAvailabein");
      String budgetMoney = fileForm.getParameter("budgetMoney");
      if(budgetMoney!=null){
        budgetMoney = budgetMoney.replace(",", "");
      }
      String budgetInMoney = fileForm.getParameter("budgetInMoney");
      if(budgetInMoney!=null){
        budgetInMoney = budgetInMoney.replace(",", "");
      }
      String notAffair = fileForm.getParameter("notAffair");
      String useArea = fileForm.getParameter("useArea");
      String memo = fileForm.getParameter("memo");
      String ATTACHMENT_ID_OLD = fileForm.getParameter("ATTACHMENT_ID_OLD");
      String ATTACHMENT_NAME_OLD = fileForm.getParameter("ATTACHMENT_NAME_OLD");
      String detailContent = fileForm.getParameter("detailContent");
      String detailContentIn = fileForm.getParameter("detailContentIn");
      String deptAuditDirector = fileForm.getParameter("deptAuditDirector");
      String deptAuditDate = fileForm.getParameter("deptAuditDate");
      String deptAuditContent = fileForm.getParameter("deptAuditConent");
      String seqId = fileForm.getParameter("seqId");
      String clon = request.getParameter("clon");
      YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
      YHBudgetApply budgetApply = new YHBudgetApply();
      String attIdStrTemp  = "";
      String attNameStrTemp = "";
      if(!YHUtility.isNullorEmpty(seqId)&&YHUtility.isInteger(seqId)&&clon!=null&&clon.equals("1")){
        YHBudgetApply budgetApplyTemp  = tbal.selectBudgetApplyById(dbConn, seqId);  
        if(budgetApplyTemp!=null){
          attIdStrTemp = budgetApplyTemp.getAttachmentId();
          attNameStrTemp = budgetApplyTemp.getAttachmentName(); 
        }
      }

      budgetApply.setBudgetProposer(budgetProposer);
      budgetApply.setBudgetItem(budgetItem);
      budgetApply.setDeptId(deptId);
      budgetApply.setBudgetYear(Integer.parseInt(budgetYear));
      budgetApply.setDeptId(deptId);
      budgetApply.setSettleFlag("0");
      budgetApply.setBudgetDate(new Date());
      if (budgetAvailabein != null && !budgetAvailabein.trim().equals("")) {
        budgetApply.setBudgetAvailablein(dateFormat.parse(budgetAvailabein));
      }
      if (budgetMoney != null && YHUtility.isNumber(budgetMoney)) {
        budgetApply.setBudgetMoney(Double.parseDouble(budgetMoney));
      }
      if (budgetInMoney != null && YHUtility.isNumber(budgetInMoney)) {
        budgetApply.setBudgetInMoney(Double.parseDouble(budgetInMoney));
      }
      budgetApply.setNotAffair(notAffair);
      budgetApply.setUseArea(useArea);
      budgetApply.setMemo(memo);
      budgetApply.setAttachmentId(ATTACHMENT_ID_OLD);
      budgetApply.setAttachmentName(ATTACHMENT_NAME_OLD);
      budgetApply.setDetailContent(detailContent);
      budgetApply.setDetailContentIn(detailContentIn);
      budgetApply.setDeptAuditDirector(deptAuditDirector);
      if (deptAuditDate != null && YHUtility.isDay(deptAuditDate)) {
        budgetApply.setDeptAuditDate(dateFormat.parse(deptAuditDate));
      }
      budgetApply.setDeptAuditContent(deptAuditContent);
      
      
      
      Iterator<String> iKeys = fileForm.iterateFileFields();
      String filePath = YHSysProps.getAttachPath()  + File.separator + "finance" + File.separator + dateFormat2.format(new Date()); // YHSysProps.getAttachPath()
      String attachmentId = "";
      String attachmentName = "";
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        String regName = fileName;

        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
        String rand = emul.getRandom();
        attachmentId =  dateFormat2.format(new Date()) + "_" + attachmentId + rand+",";
        attachmentName = attachmentName + fileName+"*";
        fileName = rand + "_" + fileName;
        fileForm.saveFile(fieldName, filePath + File.separator + fileName);
      }
      attachmentId = attachmentId + attIdStr;
      attachmentName = attachmentName + attNameStr;
      if(!YHUtility.isNullorEmpty(attIdStrTemp)){
        attachmentId = attachmentId + "," + attIdStrTemp;
      }
      if(!YHUtility.isNullorEmpty(attachmentName)){
        attachmentName = attachmentName + "*" + attNameStrTemp;
      }
      budgetApply.setAttachmentId(attachmentId);
      budgetApply.setAttachmentName(attachmentName);
      
      tbal.addBudgetApply(dbConn, budgetApply);
   
  
      String path = request.getContextPath();
      
      if(clon!=null&&clon.equals("1")){
        response.sendRedirect(path
            + "/subsys/oa/finance/budget/plan/clon_edit.jsp?type=1");
      }else{
        response.sendRedirect(path
            + "/subsys/oa/finance/budget/plan/addbudget.jsp?type=1");
      }
    
      /*
       * //短信smsType, content, remindUrl, toId, fromId YHSmsBack sb = new
       * YHSmsBack(); sb.setSmsType("5"); sb.setContent("请查看日程安排！内容："+content);
       * sb.setRemindUrl("/yh/core/funcs/calendar/mynote.jsp?seqId="+maxSeqId+
       * "&openFlag=1&openWidth=300&openHeight=250");
       * sb.setToId(String.valueOf(userId)); sb.setFromId(userId);
       * YHSmsUtil.smsBack(dbConn, sb);
       */
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "");

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "";
  }
  /**
   * 更新预算
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateBudgetApply(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyMM");
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      Calendar cl = Calendar.getInstance();
      int year = cl.get(Calendar.YEAR);
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      // 保存从文件柜、网络硬盘选择附件
      YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, "finance");
      String attIdStr = sel.getAttachIdToString(",");
      String attNameStr = sel.getAttachNameToString("*");
     
      String budgetProposer = fileForm.getParameter("budgetProposer");
      String budgetItem = fileForm.getParameter("budgetItem");
      String deptId = fileForm.getParameter("deptId");
      String budgetAvailabein = fileForm.getParameter("budgetAvailabein");
      String budgetMoney = fileForm.getParameter("budgetMoney");
      if(budgetMoney!=null){
        budgetMoney = budgetMoney.replace(",", "");
      }
      String budgetInMoney = fileForm.getParameter("budgetInMoney");
      if(budgetInMoney!=null){
        budgetInMoney = budgetInMoney.replace(",", "");
      }
      String notAffair = fileForm.getParameter("notAffair");
      String useArea = fileForm.getParameter("useArea");
      String memo = fileForm.getParameter("memo");
      String ATTACHMENT_ID_OLD = fileForm.getParameter("ATTACHMENT_ID_OLD");
      String ATTACHMENT_NAME_OLD = fileForm.getParameter("ATTACHMENT_NAME_OLD");
      String detailContent = fileForm.getParameter("detailContent");
      String detailContentIn = fileForm.getParameter("detailContentIn");
      String deptAuditDirector = fileForm.getParameter("deptAuditDirector");
      String deptAuditDate = fileForm.getParameter("deptAuditDate");
      String deptAuditContent = fileForm.getParameter("deptAuditConent");
      String budgetYear = fileForm.getParameter("budgetYear");
      if(budgetYear==null||!YHUtility.isInteger(budgetYear)){
        budgetYear = year + "";
      }
      String seqId = fileForm.getParameter("seqId");
      if(seqId!=null&&YHUtility.isInteger(seqId)){
        YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
        YHBudgetApply budgetApply = new YHBudgetApply();
        //先查出数据库的附件，然后加上
        String attIdStrTemp = "";
        String attNameStrTemp = "";
        YHBudgetApply budgetApplyTemp  = tbal.selectBudgetApplyById(dbConn, seqId);  
        if(budgetApplyTemp!=null){
          attIdStrTemp  = budgetApplyTemp.getAttachmentId();
          attNameStrTemp = budgetApplyTemp.getAttachmentName();
        }

        
        budgetApply.setSeqId(Integer.parseInt(seqId));
        budgetApply.setBudgetProposer(budgetProposer);
        budgetApply.setBudgetItem(budgetItem);
        budgetApply.setDeptId(deptId);
        budgetApply.setBudgetYear(Integer.parseInt(budgetYear));
        budgetApply.setDeptId(deptId);
        budgetApply.setBudgetDate(new Date());
        if (budgetAvailabein != null && !budgetAvailabein.trim().equals("")) {
          budgetApply.setBudgetAvailablein(dateFormat.parse(budgetAvailabein));
        }
        if (budgetMoney != null && YHUtility.isNumber(budgetMoney)) {
          budgetApply.setBudgetMoney(Double.parseDouble(budgetMoney));
        }
        if (budgetInMoney != null && YHUtility.isNumber(budgetInMoney)) {
          budgetApply.setBudgetInMoney(Double.parseDouble(budgetInMoney));
        }
        budgetApply.setNotAffair(notAffair);
        budgetApply.setUseArea(useArea);
        budgetApply.setMemo(memo);
        budgetApply.setAttachmentId(ATTACHMENT_ID_OLD);
        budgetApply.setAttachmentName(ATTACHMENT_NAME_OLD);
        budgetApply.setDetailContent(detailContent);
        budgetApply.setDetailContentIn(detailContentIn);
        budgetApply.setDeptAuditDirector(deptAuditDirector);
        if (deptAuditDate != null && YHUtility.isDay(deptAuditDate)) {
          budgetApply.setDeptAuditDate(dateFormat.parse(deptAuditDate));
        }
        budgetApply.setDeptAuditContent(deptAuditContent);
        
        String attachmentId = "";
        String attachmentName = "";
        
        Iterator<String> iKeys = fileForm.iterateFileFields();
        String filePath = YHSysProps.getAttachPath()  + File.separator + "finance" + File.separator + dateFormat2.format(new Date()); // YHSysProps.getAttachPath()
        while (iKeys.hasNext()) {
          String fieldName = iKeys.next();
          String fileName = fileForm.getFileName(fieldName);
          String regName = fileName;

          if (YHUtility.isNullorEmpty(fileName)) {
            continue;
          }
          YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
          String rand = emul.getRandom();
          attachmentId =  dateFormat2.format(new Date()) + "_" + attachmentId + rand+",";
          attachmentName = attachmentName + fileName+"*";
          fileName = rand + "_" + fileName;
          fileForm.saveFile(fieldName, filePath + File.separator + fileName);
        }
        
        attachmentId = attachmentId + attIdStr;
        attachmentName = attachmentName + attNameStr;
        if(attIdStrTemp!=null&&!attIdStrTemp.equals("")){
          attachmentId = attIdStrTemp  + "," + attachmentId;
        }
        if(attNameStrTemp!=null&&!attNameStrTemp.equals("")){
          attachmentName = attNameStrTemp  + "*" + attachmentName;
        }

        budgetApply.setAttachmentId(attachmentId);
        budgetApply.setAttachmentName(attachmentName);
        tbal.updateBudgetApply(dbConn, budgetApply);
      }
      

  
      String path = request.getContextPath();
      response.sendRedirect(path
          + "/subsys/oa/finance/budget/plan/clon_edit.jsp?type=1");
      /*
       * //短信smsType, content, remindUrl, toId, fromId YHSmsBack sb = new
       * YHSmsBack(); sb.setSmsType("5"); sb.setContent("请查看日程安排！内容："+content);
       * sb.setRemindUrl("/yh/core/funcs/calendar/mynote.jsp?seqId="+maxSeqId+
       * "&openFlag=1&openWidth=300&openHeight=250");
       * sb.setToId(String.valueOf(userId)); sb.setFromId(userId);
       * YHSmsUtil.smsBack(dbConn, sb);
       */
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "");

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "";
  }

  /**
   * 查询预算情况
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectBudgetApply(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();

      /*
       * //短信smsType, content, remindUrl, toId, fromId YHSmsBack sb = new
       * YHSmsBack(); sb.setSmsType("5"); sb.setContent("请查看日程安排！内容："+content);
       * sb.setRemindUrl("/yh/core/funcs/calendar/mynote.jsp?seqId="+maxSeqId+
       * "&openFlag=1&openWidth=300&openHeight=250");
       * sb.setToId(String.valueOf(userId)); sb.setFromId(userId);
       * YHSmsUtil.smsBack(dbConn, sb);
       */
      YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
      YHManageOutLogic tmol = new YHManageOutLogic();
      YHPersonLogic tpl = new YHPersonLogic();
      List<YHBudgetApply> applyList = new ArrayList<YHBudgetApply>();
      String data = "[";
      if (user.getUserPriv() != null && user.getUserPriv().equals("1")
          && user.getUserId().equals("admin")) {
        String[] str = {};
        applyList = tbal.selectBudgetApply(dbConn, str);
      } else {
        String[] str = { "DEPT_ID = " + user.getDeptId() };
        applyList = tbal.selectBudgetApply(dbConn, str);
      }
      for (int i = 0; i < applyList.size(); i++) {
        YHBudgetApply ba = applyList.get(i);
        String applyName = "";
        String deptName = "";
        if (ba.getDeptId() != null) {
          deptName = YHUtility.encodeSpecial(tmol.selectByUserIdDept(dbConn, ba
              .getDeptId()));
        }
        if (ba.getBudgetProposer() != null) {
          applyName = YHUtility.encodeSpecial(tpl.getNameBySeqIdStr(ba
              .getBudgetProposer(), dbConn));
        }
        data = data
            + YHFOM.toJson(ba).substring(0, YHFOM.toJson(ba).length() - 1)
            + ",applyName:\"" + applyName + "\",deptName:\"" + deptName
            + "\"},";
      }
      if (applyList.size() > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 删除预算byID
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String  deleteBudgetById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
      if(seqId!=null&&YHUtility.isInteger(seqId)){
        tbal.delBudgetApplyById(dbConn, seqId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "{}");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 查询预算总金额情况
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectBudget(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String year = request.getParameter("year");
      Calendar cl = Calendar.getInstance();
      int curYear = cl.get(Calendar.YEAR);
      if(year==null||year.equals("")){
        year = curYear + "";
      }
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
  
      YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
      YHBudgetDeptTotalLogic tbatl = new YHBudgetDeptTotalLogic();
      YHPersonLogic tpl = new YHPersonLogic();
      String[] str = { "DEPT_ID='" + user.getDeptId() + "'", "CUR_YEAR=" + year };
      List<YHBudgetDeptTotal> totalList = tbatl.selectBudgetTotal(dbConn, str);

      String total = "0";
      String isDpetTotal = "1";
      if (totalList.size() > 0) {
        total = totalList.get(0).getTotal() +"";
        total = YHUtility.getFormatedStr(total, 2);
        isDpetTotal = "0";
      } else {
        total = tbal.selectTotal(dbConn, String.valueOf(user.getDeptId()), year);
        total = YHUtility.getFormatedStr(total, 2);
      }
      String data = "{year:" + year + ",deptTotal:\"" + total +"\",isDpetTotal:"+isDpetTotal+"}";
      request.setAttribute("isDpetTotal",isDpetTotal);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
/**
 * 分页
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
  public String queryDeptBudget(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String year = request.getParameter("year");
      Calendar cl = Calendar.getInstance();
      int curYear = cl.get(Calendar.YEAR);
      if(year==null||year.equals("")){
        year = curYear + "";
      }
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
      String data = tbal.toSearchData(dbConn, request.getParameterMap(), String
          .valueOf(user.getDeptId()), year);
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
  /**
   * 分页
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
    public String queryBudgetList(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
        String data = tbal.toSearchData3(dbConn, request.getParameterMap());
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
    /**
     * 分页业务管理系统
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
      public String queryBudgetListToProject(HttpServletRequest request,
          HttpServletResponse response) throws Exception {
        Connection dbConn = null;
        try {
          YHRequestDbConn requestDbConn = (YHRequestDbConn) request
              .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
          YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
          String budgetItem = request.getParameter("budgetItem");
          String budgetPropose = request.getParameter("budgetPropose");
          String data = tbal.toSearchData3(dbConn, request.getParameterMap(),budgetItem,budgetPropose);
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
  /**
   * 费用管理分页
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
    public String queryBudget(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        String year = request.getParameter("year");
        String notAffair = request.getParameter("notAffair");
        if(notAffair!=null&&notAffair.equals("")){
          notAffair = "0";
        }
        Calendar cl = Calendar.getInstance();
        YHPerson user = (YHPerson) request.getSession().getAttribute(
            YHConst.LOGIN_USER);
        int userId = user.getSeqId();
        YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
        String data = tbal.toSearchData2(dbConn, request.getParameterMap(), String
            .valueOf(user.getDeptId()), notAffair,userId);
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
  /**
   * 得到有效的费用预算金额=财务人员确定额度-本部门本年的预算金额的总额
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectAvailable(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptId = request.getParameter("deptId");
      String year = request.getParameter("year");

      Calendar cl = Calendar.getInstance();
      int curYear = cl.get(Calendar.YEAR);
      if (year == null&&year.equals("")) {
        year = curYear + "";
      }
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
      YHBudgetDeptTotalLogic tbatl = new YHBudgetDeptTotalLogic();
      YHPersonLogic tpl = new YHPersonLogic();
      String[] str = { "DEPT_ID='" + user.getDeptId() + "'",
          "CUR_YEAR=" + year };
      List<YHBudgetDeptTotal> totalList = tbatl.selectBudgetTotal(dbConn, str);

      String DeptTotal = "0";
      String type = "1";// 可以新增预算
      double available = 0;
      if (totalList.size() > 0) {
        type = "2";
        DeptTotal = totalList.get(0).getTotal() + "";
        String budgetTotal = tbal.selectTotal(dbConn, String.valueOf(user
            .getDeptId()), year);
        available = Double.parseDouble(DeptTotal)
            - Double.parseDouble(budgetTotal);
      }
      String data = "{type:" + type + ",availableMoney:" + available + "}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 得到预算项目的所有类型
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCodeItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      // String seqId = request.getParameter("seqId");
      String GIFT_PROTYPE = request.getParameter("GIFT_PROTYPE");
      // 根据seqId（codeClass） 得到所有的codeItem

      YHGiftInstockLogic giftLogic = new YHGiftInstockLogic();
      String data = "[";
      List<YHCodeItem> itemList = new ArrayList<YHCodeItem>();
      if (GIFT_PROTYPE != null && !GIFT_PROTYPE.equals("")) {
        // YHCodeClass codeClass = giftLogic.getCodeClass(dbConn,
        // Integer.parseInt(seqId));
        // if(codeClass!=null&&codeClass.getClassNo()!=null&&!codeClass.getClassNo().trim().equals("")){
        // String[] str = {"CLASS_NO = '" + codeClass.getClassNo() +
        // "' order by SORT_NO"};
        itemList = giftLogic.getCodeItem(dbConn, GIFT_PROTYPE);
        for (int i = 0; i < itemList.size(); i++) {
          YHCodeItem item = itemList.get(i);
          data = data + YHFOM.toJson(item) + ",";
        }

      }

      if (itemList.size() > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查询预算ById
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectBudgetById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String seqId = request.getParameter("seqId");
    String type = request.getParameter("type");//1为查看详情2为克隆3为编辑，4为打印预览
    if(type==null){
      type = "";
    }
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      String data = "{";
      YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
      YHPersonLogic personLogic = new YHPersonLogic();
      YHBudgetApply tba = null;
      if(seqId!=null&&!seqId.equals("")){
       tba = tbal.selectBudgetApplyById(dbConn, seqId);
      }
      request.setAttribute("budgetApply", tba);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    if(type.equals("1")){
      return "/subsys/oa/finance/budget/plan/querybudget.jsp";
    }
    if(type.equals("2")){
      return "/subsys/oa/finance/budget/plan/clon_edit.jsp?clon_edit="+type;
    }
    if(type.equals("3")){
      return "/subsys/oa/finance/budget/plan/clon_edit.jsp?clon_edit="+type;
    }
    if(type.equals("4")){
      return "/subsys/oa/finance/budget/plan/printbudget.jsp";
    }
    
    return "";
  }
  /***
   * 根据budgetId得到领用和报销总金额

   * @return
   * @throws Exception 
   */
  public String chequeTotalByBudgetId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String budgetId = request.getParameter("budgetId");
    String type = request.getParameter("type");
    if(budgetId==null){
      budgetId = "";
    }
    if(type==null){
      type = "";
    }
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFinanceApplyRecordLogic tfarl = new YHFinanceApplyRecordLogic();
      YHChargeExpenseLogic tcel = new YHChargeExpenseLogic();
      String total = "0.00";
       if(type.equals("2")){//已报销总金额
         double  expenseTotal= tcel.sunMoneyByBudgetId(dbConn, budgetId);
         total = expenseTotal + "";//YHUtility.getFormatedStr(expenseTotal,2);
       }else{
         total  = tfarl.chequeTotalByBudgetId(dbConn, budgetId);
         //total = YHUtility.getFormatedStr(chequeTotal, 2);
       }
      String data = "{total:\""+total+"\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /***
   * 更新预算的附件ById

   * @return
   * @throws Exception 
   */
  public String deleleFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String attachId = request.getParameter("attachId");
      String attachName = request.getParameter("attachName");
      if(seqId==null){
        seqId = "";
      }
      if(attachId==null){
        attachId = "";
      }
      if(attachName==null){
        attachName = "";
      }
      YHBudgetApply tba = null;
      YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
      String updateFlag = "0";
      if(seqId!=null&&!seqId.equals("")){
       tba = tbal.selectBudgetApplyById(dbConn, seqId);
       if(tba!=null){
         String attachmentId = tba.getAttachmentId();
         String attachmentName = tba.getAttachmentName();
         if(attachmentId==null){
           attachmentId = "";
         }
         if(attachmentName==null){
           attachmentName = "";
         }
         String[] attachmentIdArray = attachmentId.split(",");
         String[] attachmentNameArray = attachmentName.split("\\*");
         String newAttachmentId = "";
         String newAttachmentName = "";
         for (int i = 0; i < attachmentIdArray.length; i++) {
           if(!attachmentIdArray[i].equals(attachId)){
             newAttachmentId = newAttachmentId +attachmentIdArray[i] + ",";
           }
         }
         for (int i = 0; i < attachmentNameArray.length; i++) {
           if(!attachmentNameArray[i].equals(attachName)){
             newAttachmentName = newAttachmentName +attachmentNameArray[i] + "*";
           }
         }
         
         tbal.updateFile(dbConn, newAttachmentId, newAttachmentName, seqId);
         updateFlag = "1";
       }
      }
      String data = "{updateFlag:"+updateFlag+"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * //得到每个部门的所有年份的详细预算信息
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectAllYearBudgetByDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String deptId = request.getParameter("deptId");//得到部门id如果为空则为当前用户的部门 ID
      if(YHUtility.isNullorEmpty(deptId)){
        deptId = user.getDeptId() + "";
      }
      //得到部门申请过预算的所有年份
      List yearList = new ArrayList();
      yearList = tbal.selectAllYearByDept(dbConn, deptId);
      String data = "[";
      //循环所有的年份
      for (int i = 0; i < yearList.size(); i++) {
        String year = (String) yearList.get(i);
        data = data + "{year:" + year + "},";
      }
      if(yearList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
