package yh.core.funcs.workplan.act;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workplan.data.YHWorkPlan;
import yh.core.funcs.workplan.logic.YHWorkLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHPlanWorkTwoAct {
  /**
   * 根据条件查询数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectRes(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层YHWorkLogic
      YHWorkLogic workLogic = new YHWorkLogic();
      //实列实体层plan
      YHWorkPlan plan = new YHWorkPlan();
      //接受对应页面值      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      String name = request.getParameter("NAME");
      String content = request.getParameter("CONTENT");
      String deptParentDesc = request.getParameter("deptParent");
      String managerDesc = request.getParameter("manager");
      String leader1Desc = request.getParameter("leader1");
      String leader2Desc = request.getParameter("leader2");
      String type = request.getParameter("WORK_TYPE");
      String REMARK = request.getParameter("REMARK");
      String leader3Desc = request.getParameter("leader3");
      String statrTime = request.getParameter("statrTime");
      String endTime = request.getParameter("endTime");

      if (!YHUtility.isNullorEmpty(statrTime)) {
        plan.setStatrTime(Date.valueOf(statrTime));
      }
      if (!YHUtility.isNullorEmpty(endTime)) {
        plan.setEndTime(Date.valueOf(endTime));
      }

      //将接收的数据进行封装到实体中
      plan.setContent(content);
      plan.setName(name);
      plan.setDeptParentDesc(deptParentDesc);
      plan.setManagerDesc(managerDesc);
      plan.setLeader1Desc(leader1Desc);
      plan.setLeader2Desc(leader2Desc);
      plan.setLeader3Desc(leader3Desc);
      plan.setType(type);
      plan.setRemark(REMARK);

      //调用worklist方法，返回LIST集合
      List<YHWorkPlan> worklist = workLogic.selectRes(dbConn, plan ,loginUser);
      //      //定义数组将数据保存到Json中
      //      String data = "[";
      //
      //      YHWorkPlan workData = new YHWorkPlan(); 
      //      for (int i = 0; i < worklist.size(); i++) {
      //        workData = worklist.get(i);
      //        data = data + YHFOM.toJson(workData).toString()+",";
      //      }
      //      if(worklist.size()>0){
      //        data = data.substring(0, data.length()-1);
      //      }
      //      data = data.replaceAll("\\n", "");
      //      data = data.replaceAll("\\r", "");
      //      data = data + "]";
      //将结果数据保存到data中
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
      request.setAttribute("worklist", worklist);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/funcs/workplan/manage/arrange_work/query3.jsp";
  }
  /*****
   * 详情查询有数据

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String workSelect2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //接受对应页面值

      int seqId = Integer.parseInt(request.getParameter("seqId"));
      //实列化业务层YHWorkLogic
      YHWorkLogic workLogic = new YHWorkLogic();
      //调用业务层selectId方法根据条件SEQID查询数据
      YHWorkPlan data = workLogic.selectId(dbConn, seqId);
      //保存查询数据是否成功，保存date
      request.setAttribute("planData", data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "数据成功！");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "数据失败");
      throw e;
    }
    return "/core/funcs/workplan/manage/detail.jsp";
  }
  /**
   *查询所有(分页)通用列表显示数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String applySelect(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String type = request.getParameter("type");
      String create = request.getParameter("create");
      String createTime = request.getParameter("createTime");
      String seqId = String.valueOf(person.getSeqId());
      String deptId = String.valueOf(person.getDeptId());
      
      YHWorkLogic gift = new YHWorkLogic();
      String data = gift.applySelect(dbConn,request.getParameterMap(),createTime,type,create,seqId,deptId);
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
   *查询所有ID串名字
   */
  public String userName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHWorkLogic gift = new YHWorkLogic();
      String data = gift.userName(dbConn,seqId);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
