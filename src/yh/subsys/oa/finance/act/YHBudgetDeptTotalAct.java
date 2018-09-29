package yh.subsys.oa.finance.act;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.calendar.act.YHCalendarAct;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.finance.data.YHBudgetApply;
import yh.subsys.oa.finance.data.YHBudgetDeptTotal;
import yh.subsys.oa.finance.logic.YHBudgetApplyLogic;
import yh.subsys.oa.finance.logic.YHBudgetDeptTotalLogic;
import yh.subsys.oa.finance.logic.YHChargeExpenseLogic;

public class YHBudgetDeptTotalAct {
  /**
   * 新建预算年份
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addBudgetTotal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      
    /*    //短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("5");
        sb.setContent("请查看日程安排！内容："+content);
        sb.setRemindUrl("/yh/core/funcs/calendar/mynote.jsp?seqId="+maxSeqId+"&openFlag=1&openWidth=300&openHeight=250");
        sb.setToId(String.valueOf(userId));
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);*/
      YHBudgetDeptTotalLogic tbal = new YHBudgetDeptTotalLogic();
      YHBudgetDeptTotal deptTotal = new YHBudgetDeptTotal();
      tbal.addBudgetTotal(dbConn, deptTotal);
      String data = "{}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
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
  public String selectTotal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String year = request.getParameter("year");
      String deptId = request.getParameter("deptId");
      Calendar cl = Calendar.getInstance();
      int curYear = cl.get(Calendar.YEAR);
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      if(year==null||year.equals("")){
        year = curYear + "";
      }
      if(deptId==null||deptId.equals("")){
        deptId = user.getDeptId()+"";
      }

      YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
      YHBudgetDeptTotalLogic tbatl = new YHBudgetDeptTotalLogic();
      YHPersonLogic tpl = new YHPersonLogic();
      String[] str = { "DEPT_ID='" + deptId + "'", "CUR_YEAR=" + year };
      List<YHBudgetDeptTotal> totalList = tbatl.selectBudgetTotal(dbConn, str);

      String total  = "0.00";
      String isDpetTotal = "1";
      DecimalFormat numFormatG = new DecimalFormat("#,##0.00");
      if (totalList.size() > 0) {
       // double totalMoney = totalList.get(0).getTotal();
        total = totalList.get(0).getTotal() +"";//YHUtility.getFormatedStr(totalMoney, 2);
        isDpetTotal = "0";
      } else {
        total = tbal.selectTotal(dbConn, deptId, year);
       // double doubleTotal = 0;
        //if(YHUtility.isNumber(total)){
         // doubleTotal = Double.parseDouble(total);
        //}
        //total = numFormatG.format(doubleTotal);
      }
      YHChargeExpenseLogic tcel = new YHChargeExpenseLogic();
      double useMoney =  tcel.sunMoney(dbConn, deptId, Integer.parseInt(year));
      String data = "{year:" + year + ",deptTotal:\"" + total +"\",isDpetTotal:\""+ isDpetTotal+"\",useMoney:\""+useMoney +"\"}";
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
   * 确认额度
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setBudget(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String year = request.getParameter("year");
      String deptId = request.getParameter("deptId");
      String total = request.getParameter("total");
      YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
      YHBudgetDeptTotalLogic tbatl = new YHBudgetDeptTotalLogic();
      if(year!=null&&!year.equals("")&&deptId!=null&&!deptId.equals("")&&total!=null&&!total.equals("")){
        String[] str = { "DEPT_ID='" + deptId + "'", "CUR_YEAR=" + year };
        List<YHBudgetDeptTotal> totalList = tbatl.selectBudgetTotal(dbConn, str);
        if (totalList.size() > 0) {

        } else {
          YHBudgetDeptTotal tbdt = new YHBudgetDeptTotal();
          tbdt.setCurYear(Integer.parseInt(year));
          tbdt.setDeptId(deptId);
          total = total.replace(",", "");
          if(YHUtility.isNumber(total)){
            tbdt.setTotal(Double.parseDouble(total));
          }
         
          tbatl.addBudgetTotal(dbConn, tbdt);
        }
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
      int year = cl.get(Calendar.YEAR);
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      String budgetProposer = fileForm.getParameter("budgetProposer");
      String budgetItem = fileForm.getParameter("budgetItem");
      String deptId = fileForm.getParameter("deptId");
      String budgetAvailabein = fileForm.getParameter("budgetAvailabein");
      String budgetMoney = fileForm.getParameter("budgetMoney");
      String budgetInMoney = fileForm.getParameter("budgetInMoney");
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
      String clon = fileForm.getParameter("clon");
      YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
      YHBudgetDeptTotalLogic tbdtl = new YHBudgetDeptTotalLogic();
      YHBudgetApply budgetApply = new YHBudgetApply();
      budgetApply.setBudgetProposer(budgetProposer);
      budgetApply.setBudgetItem(budgetItem);
      if(deptId==null||deptId.equals("")){
        deptId = user.getDeptId()+"";
      }
      budgetApply.setDeptId(deptId);
      budgetApply.setBudgetDate(new Date());
      if(budgetYear==null||budgetYear.equals("")){
        budgetYear = String.valueOf(year);
      }
      budgetApply.setBudgetYear(Integer.parseInt(budgetYear));
      if (budgetAvailabein != null && !budgetAvailabein.trim().equals("")) {
        budgetApply.setBudgetAvailablein(dateFormat.parse(budgetAvailabein));
      }  
      budgetMoney = budgetMoney.replace(",", "");
      if (budgetMoney != null && YHUtility.isNumber(budgetMoney)) {
        budgetApply.setBudgetMoney(Double.parseDouble(budgetMoney));
      }
      budgetInMoney = budgetInMoney.replace(",", "");
      if (budgetInMoney != null && YHUtility.isNumber(budgetInMoney)) {
        budgetApply.setBudgetInMoney(Double.parseDouble(budgetInMoney));
      }
      budgetApply.setNotAffair(notAffair);
      budgetApply.setUseArea(useArea);
      budgetApply.setMemo(memo);
      budgetApply.setSettleFlag("0");
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
        attachmentId = attachmentId + rand+",";
        attachmentName = attachmentName + fileName+"*";
        fileName = rand + "_" + fileName;
        fileForm.saveFile(fieldName, filePath + File.separator + fileName);

      }
      budgetApply.setAttachmentId(attachmentId);
      budgetApply.setAttachmentName(attachmentName);
      tbal.addBudgetApply(dbConn, budgetApply);
      
      //查询某个部门和年份是不是已确认
      String[] str = {"DEPT_ID='" + deptId + "'", "CUR_YEAR = "+budgetYear };
      List<YHBudgetDeptTotal> totalList =  tbdtl.selectBudgetTotal(dbConn, str);
     //更新确认额度total总金额
      if(totalList.size()>0){
        tbdtl.updateTotal(dbConn, deptId, budgetYear, budgetMoney);
      }

      String path = request.getContextPath();
      
      if(clon!=null&&clon.equals("1")){
       // response.sendRedirect(path
           // + "/subsys/oa/finance/budget/plan/clon_edit.jsp?type=1");
      }else{
        response.sendRedirect(path
            + "/subsys/oa/finance/budget/addBudget.jsp?type=1");
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
        YHPerson user = (YHPerson) request.getSession().getAttribute(
            YHConst.LOGIN_USER);
        int userId = user.getSeqId();
        dbConn = requestDbConn.getSysDbConn();
        String year = request.getParameter("year");
        String deptId = request.getParameter("deptId");
        Calendar cl = Calendar.getInstance();
        
        int curYear = cl.get(Calendar.YEAR);
        if(year==null||year.equals("")){
          year = curYear + "";
        }
        if(deptId==null||deptId.equals("")){
          deptId = user.getDeptId() + "";
        }
  
        YHBudgetApplyLogic tbal = new YHBudgetApplyLogic();
        String data = tbal.toSearchData(dbConn, request.getParameterMap(), deptId, year);
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
     *查询所有(分页)通用列表显示数据
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String expenseSelectByBudgetId(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        String budgetId = request.getParameter("budgetId");
        if(budgetId==null){
          budgetId = "";
        }
        YHBudgetDeptTotalLogic gift = new YHBudgetDeptTotalLogic();
        List<Map<String,String>> expenseList = gift.expenseSelectByBudgetId(dbConn,request.getParameterMap(),budgetId);
        String data = YHCalendarAct.getJson(expenseList);
  /*      String data = gift.expenseSelectByBudgetId(dbConn,request.getParameterMap(),budgetId);
        PrintWriter pw = response.getWriter();
        pw.println(data);
        pw.flush();*/
       // request.setAttribute("expenseList", expenseList);
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
