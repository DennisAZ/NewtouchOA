package yh.core.funcs.workplan.act;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workplan.data.YHWorkPlan;
import yh.core.funcs.workplan.logic.YHExportLogic;
import yh.core.funcs.workplan.logic.YHWorkLogic;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHExport {
  public String exportCsv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String fileName = URLEncoder.encode("工作计划.csv","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      YHWorkLogic workLogic = new YHWorkLogic();
      //实列实体层plan
      YHWorkPlan plan = new YHWorkPlan();
      //接受对应页面值
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
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //调用worklist方法，返回LIST集合
      List<YHWorkPlan> worklist = workLogic.selectRes(dbConn,plan ,loginUser); 
      YHExportLogic expl = new YHExportLogic();
      ArrayList<YHDbRecord > dbL = expl.getDbRecord(worklist,dbConn);
      YHCSVUtil.CVSWrite(response.getWriter(), dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return null;
  }
}
