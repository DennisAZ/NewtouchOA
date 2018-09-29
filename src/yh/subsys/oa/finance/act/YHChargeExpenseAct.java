package yh.subsys.oa.finance.act;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.finance.data.YHChargeExpense;
import yh.subsys.oa.finance.logic.YHChargeExpenseLogic;

public class YHChargeExpenseAct {
  /**
   *查询所有(分页)通用列表显示数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String expenseSelect(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String deptId = request.getParameter("deptId");
      String chargeUser = request.getParameter("chargeUser");
      String chargeDate = request.getParameter("chargeDate");
      String chargeDate2 = request.getParameter("chargeDate2");
      String chargeItem = request.getParameter("chargeItem");
      String financeAuditUser = request.getParameter("financeAuditUser");
      String momey = request.getParameter("chargeMoney");
      String expense = request.getParameter("expense");
      
      Date statrTime = null;
      Date endTime = null;
      if (!YHUtility.isNullorEmpty(chargeDate)) {
        statrTime = Date.valueOf(chargeDate);
      }
      if (!YHUtility.isNullorEmpty(chargeDate2)) {
        endTime = Date.valueOf(chargeDate2);
      }

      YHChargeExpenseLogic gift = new YHChargeExpenseLogic();
      String data = gift.expenseSelect(dbConn,request.getParameterMap(),deptId,chargeUser,statrTime,endTime,chargeItem,financeAuditUser,momey,expense);
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
  public String expenseSelect2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String deptId = request.getParameter("deptId");
      String chargeUser = request.getParameter("chargeUser");
      String chargeDate = request.getParameter("chargeDate");
      String chargeDate2 = request.getParameter("chargeDate2");
      String chargeItem = request.getParameter("chargeItem");
      String financeAuditUser = request.getParameter("financeAuditUser");
      String momey = request.getParameter("chargeMoney");
      String expense = request.getParameter("expense");
      String year2 = request.getParameter("year");
      Date statrTime = null;
      Date endTime = null;
      int year = 0;

      if (!YHUtility.isNullorEmpty(chargeDate)) {
        statrTime = Date.valueOf(chargeDate);
      }
      if (!YHUtility.isNullorEmpty(chargeDate2)) {
        endTime = Date.valueOf(chargeDate2);
      }
      if (!YHUtility.isNullorEmpty(year2)) {
        year = Integer.parseInt(year2);
      }

      YHChargeExpenseLogic gift = new YHChargeExpenseLogic();
      String data = gift.expenseSelect2(dbConn,request.getParameterMap(),deptId,chargeUser,statrTime,endTime,chargeItem,financeAuditUser,momey,expense,year);
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
   * 修改数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String expenseOut(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("printStr");
      YHChargeExpenseLogic expense = new YHChargeExpenseLogic();
      expense.updateExpense(dbConn,seqIdStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    }
    return "/subsys/oa/finance/expense/charge/news/noDone.jsp";
  }
  /**
   * 修改数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String makeWaste(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("printStr");
      YHChargeExpenseLogic expense = new YHChargeExpenseLogic();
      expense.makeWaste(dbConn,seqIdStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    }
    return "/subsys/oa/finance/expense/charge/news/done.jsp";
  }
  /**
   * 修改数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String updatePrint(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("printStr");
      YHChargeExpenseLogic expense = new YHChargeExpenseLogic();
      expense.updatePrint(dbConn,seqIdStr);
      List<YHChargeExpense> list = expense.printSeqId(dbConn, seqIdStr);
      request.setAttribute("list",list);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    }
    return "/subsys/oa/finance/expense/charge/news/print.jsp";
  }
  
  /**
   * 删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delCharge(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      YHORM orm = new YHORM();//orm映射数据库
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHChargeExpense expense = new YHChargeExpense();
      expense.setSeqId(seqId);
      orm.deleteSingle(dbConn,expense);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 编辑，查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectCharge(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      YHORM orm = new YHORM();//orm映射数据库
      int seqId = Integer.parseInt(request.getParameter("seqId")); 
      YHChargeExpense expense = (YHChargeExpense)orm.loadObjSingle(dbConn,YHChargeExpense.class,seqId);
      request.setAttribute("expense",expense);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    }
    return "/subsys/oa/finance/expense/charge/news/expense.jsp";
  }
  /**
   * 编辑，查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectMoney(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptId = request.getParameter("deptId");
      int year = Integer.parseInt(request.getParameter("year")); 
      YHChargeExpenseLogic expense = new YHChargeExpenseLogic();
      double money = expense.sunMoney(dbConn,deptId,year);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    }
    return "";
  }
  /**
   * 根据ID修改数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String editExpense(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      YHORM orm = new YHORM();//orm映射数据库
      YHChargeExpense expense = (YHChargeExpense)YHFOM.build(request.getParameterMap());
      orm.updateComplex(dbConn,expense);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 添加数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String addExpense(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      YHChargeExpense expense = (YHChargeExpense) YHFOM.build(request.getParameterMap());
      YHORM orm = new YHORM();//orm映射数据库
      orm.saveSingle(dbConn,expense);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 编辑，查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectExpense(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      YHORM orm = new YHORM();//orm映射数据库
      int seqId = Integer.parseInt(request.getParameter("seqId")); 
      YHChargeExpense expense = (YHChargeExpense)orm.loadObjSingle(dbConn,YHChargeExpense.class,seqId);
      request.setAttribute("expense",expense);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    }
    return "/subsys/oa/finance/expense/charge/news/chargeDetai.jsp";
  }
}
