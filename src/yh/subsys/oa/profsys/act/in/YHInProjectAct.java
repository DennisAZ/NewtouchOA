package yh.subsys.oa.profsys.act.in;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.codeclass.data.YHCodeItem;
import yh.core.codeclass.logic.YHCodeClassLogic;
import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.data.YHProject;
import yh.subsys.oa.profsys.data.YHProjectComm;
import yh.subsys.oa.profsys.data.YHProjectFile;
import yh.subsys.oa.profsys.logic.YHProjectLogic;
import yh.subsys.oa.profsys.logic.in.YHInProjectLogic;


public class YHInProjectAct {
  /**
   * 根据seqId
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getProjectById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
  
      YHCodeClassLogic codeLogic = new YHCodeClassLogic();
      String data = "";
      YHProjectLogic projectLogic = new YHProjectLogic();
      YHPersonLogic personLogic = new YHPersonLogic();
      if(YHUtility.isInteger(seqId)){
        YHProject project = projectLogic.getProjectById(dbConn, seqId);
        if(project!=null){
          String projLeaderName = "";
          if(!YHUtility.isNullorEmpty(project.getProjLeader())){
            projLeaderName = YHUtility.encodeSpecial(personLogic.getNameBySeqIdStr(project.getProjLeader(),dbConn));
          }
          String projManagerName = ""; 
          if(!YHUtility.isNullorEmpty(project.getProjManager())){
            projManagerName = YHUtility.encodeSpecial(personLogic.getNameBySeqIdStr(project.getProjManager(),dbConn));
          }
          String projDeptName = "";
          if(!YHUtility.isNullorEmpty(project.getProjDept())){
            projDeptName = YHUtility.encodeSpecial(YHProjectLogic.deptStr(dbConn,project.getProjDept()));
          }
          String projViwerName = "";
          if(!YHUtility.isNullorEmpty(project.getProjViwer())){
            projViwerName = YHUtility.encodeSpecial(personLogic.getNameBySeqIdStr(project.getProjViwer(),dbConn));
          }
          String projVisitTypeName = "";
          if(!YHUtility.isNullorEmpty(project.getProjVisitType())){
            YHCodeItem  codeItem = YHInProjectLogic.getCodeItem(dbConn, project.getProjVisitType());
            if(codeItem != null){
              projVisitTypeName = YHUtility.encodeSpecial(codeItem.getClassDesc());
            }
          }
          String projActiveTypeName = "";
          if(!YHUtility.isNullorEmpty(project.getProjActiveType())){
            YHCodeItem  codeItem = YHInProjectLogic.getCodeItem(dbConn, project.getProjActiveType());
            if(codeItem != null){
              projActiveTypeName = YHUtility.encodeSpecial(codeItem.getClassDesc());
            }
          }
          String budgetItem = "";
          if(project.getBudgetId()>0){
            budgetItem = YHUtility.encodeSpecial(YHInProjectLogic.getBudgetApplyById(dbConn, project.getBudgetId()+"" ));
          }
          
          data = YHFOM.toJson(project).toString().substring(0, YHFOM.toJson(project).toString().length()-1) + ",projLeaderName:\"" +projLeaderName + "\","
                 + "projManagerName:\"" + projManagerName + "\",projDeptName:\"" + projDeptName + "\","
                 + "projViwerName:\"" + projViwerName + "\",projVisitTypeName:\"" + projVisitTypeName + "\","
                 + "projActiveTypeName:\"" + projActiveTypeName + "\",budgetItem:\"" + budgetItem + "\"}";
        }
      }
      if(data.equals("")){
        data = "{}";
      }
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
   * 来访项目
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
    public String queryInProject(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
        int userId = user.getSeqId();
        String userPriv = user.getUserPriv();//角色
        String postpriv = user.getPostPriv();//管理范围
        String postDept = user.getPostDept();//管理范围指定部门
        if(YHUtility.isNullorEmpty(userPriv)){
          userPriv = "";
        }
        if(YHUtility.isNullorEmpty(postpriv)){
          postpriv = "";
        }
        if(YHUtility.isNullorEmpty(postDept)){
          postDept = "";
        }
        String managerStr = "";
        if(!userPriv.equals("1")){
          if(postpriv.equals("0")){
            managerStr = " = " + user.getDeptId();
          }
          if(postpriv.equals("2")){
            managerStr = " in(" + postDept + ")";
          }
        }
       
        String projType = request.getParameter("projType");
        String projStatus = request.getParameter("projStatus");
        if(YHUtility.isNullorEmpty(projType)){
          projType = "0";//默认为来访
        }
        if(YHUtility.isNullorEmpty(projStatus)){
          projStatus = "0";
        }
        String projNum = request.getParameter("projNum");
        String projActiveType = request.getParameter("projActiveType");
        
        String projStartTime1 = request.getParameter("projStartTime1");
        String projStartTime2 = request.getParameter("projStartTime2");
        String projGropName = request.getParameter("projGropName");
        
        String projVisitType = request.getParameter("projVisitType");
        String projEndTime1 = request.getParameter("projEndTime1");
        String projEndTime2 = request.getParameter("projEndTime2");
        String projLeader = request.getParameter("projLeader");
        
        String deptId = request.getParameter("deptId");

        YHInProjectLogic tbal = new YHInProjectLogic();
        String data = tbal.toSearchData(dbConn, request.getParameterMap(),projType,projNum,projActiveType,projStartTime1,projStartTime2,projGropName,projVisitType,projEndTime1,projEndTime2,projLeader,deptId,managerStr,projStatus,userId);
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
 * 导出
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
    public String exportXls(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      OutputStream ops = null;
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        String fileName = URLEncoder.encode("来访信息.xls","UTF-8");
        fileName = fileName.replaceAll("\\+", "%20");
        response.setHeader("Cache-control","private");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Accept-Ranges","bytes");
        response.setHeader("Cache-Control","maxage=3600");
        response.setHeader("Pragma","public");
        response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
        ops = response.getOutputStream();
        //接受参数
        String seqId = request.getParameter("printStr");
        ArrayList<YHDbRecord > dbL = new ArrayList<YHDbRecord >();
        if(!YHUtility.isNullorEmpty(seqId)){
          dbL = YHInProjectLogic.toInExp(dbConn, seqId);
        }

        YHJExcelUtil.writeExc(ops, dbL);
        //YHCSVUtil.CVSWrite(ops, dbL);
      } catch (Exception ex) {
        ex.printStackTrace();
        throw ex;
      } finally {
        ops.close();
      }
      return null;
    }
    /**
     * 打印
     * 
     * */
    public String printIn(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        String printStr = request.getParameter("printStr");
        YHProjectLogic projectLogic = new YHProjectLogic();
        if (!YHUtility.isNullorEmpty(printStr)) {
          printStr = printStr.substring(0,printStr.length() - 1);
          YHProjectLogic.printOut(dbConn,printStr);
          String[] str = {"SEQ_ID in (" + printStr + ")"};
          List<YHProject> project = YHProjectLogic.queryProject(dbConn, str);
          request.setAttribute("printStr", printStr);
          request.setAttribute("project", project);
        }
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "打印成功！");
      } catch (Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/subsys/oa/profsys/in/baseinfo/news/print.jsp";
    }
    
    /**
     *按条件查询所有(分页)通用列表显示数据
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String queryProject(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();

        String projNum = request.getParameter("projNum");
        String projActiveType = request.getParameter("projActiveType");
        String projStartTime = request.getParameter("projStartTime");
        String projStartTime1 = request.getParameter("projStartTime1");
        String budgetId = request.getParameter("budgetId");
        String projVisitType = request.getParameter("projVisitType");
        String projEndTime = request.getParameter("projEndTime");
        String projEndTime1 = request.getParameter("projEndTime1");
        String projLeader = request.getParameter("projLeader");
        String projDept = request.getParameter("projDept");
        String projManager = request.getParameter("projManager");
        //实体类封装


        YHProject project = new YHProject();
        project.setProjNum(projNum);
        project.setProjActiveType(projActiveType);
        if (!YHUtility.isNullorEmpty(budgetId)) {
          project.setBudgetId(Integer.parseInt(budgetId));
        }
        project.setProjLeader(projLeader);
        project.setProjVisitType(projVisitType);
        project.setProjDept(projDept);
        project.setProjManager(projManager);
        if (!YHUtility.isNullorEmpty(projStartTime)) {
          project.setProjArriveTime(Date.valueOf(projStartTime));
        }
        if (!YHUtility.isNullorEmpty(projEndTime)) {
          project.setProjLeaveTime(Date.valueOf(projEndTime));
        }
        
        Date statrTime = null;
        Date endTime = null;
        if (!YHUtility.isNullorEmpty(projStartTime1)) {
          statrTime = Date.valueOf(projStartTime1);
        }
        if (!YHUtility.isNullorEmpty(projEndTime1)) {
          endTime = Date.valueOf(projEndTime1);
        }

        String data = YHInProjectLogic.queryProject(dbConn,request.getParameterMap(),project,statrTime,endTime);
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
     * 根基项目人员查询项目
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String queryProjectByMem(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        String memNum = request.getParameter("memNum");
        String memPosition = request.getParameter("memPosition");
        String memName = request.getParameter("memName");
        String memSex = request.getParameter("memSex");
        String memBirth = request.getParameter("memBirth");
        String memIdNum = request.getParameter("memIdNum");
        String projMemType = request.getParameter("projMemType");

       // String projId = YHProjectMemLogic.queryMemToProjId(dbConn,memNum,memPosition,memName,memSex,memBirth,memIdNum,"0");
      //通用查询数据
        String data = YHInProjectLogic.queryProjectMem(dbConn,request.getParameterMap(),"0",memNum,memPosition,memName,memSex,memBirth,memIdNum,"0");
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
     * 根据日程查询项目
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String queryProjectByCalendar(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        String activeType = request.getParameter("activeType");
        String activeContent = request.getParameter("activeContent");
        String activeLeader = request.getParameter("activeLeader");
        String activePartner = request.getParameter("activePartner");
        String startTime = request.getParameter("startTime");
        String startTime1 = request.getParameter("startTime1");
        String endTime = request.getParameter("endTime");
        String endTime1 = request.getParameter("endTime1");
        String projCalendarType = request.getParameter("projCalendarType");

        //先查出projId
       // String projId = YHProjectCalendarLogic.queryCalendarToProjId(dbConn,activeType,activeContent,activeLeader,
          //  activePartner,startTime,startTime1,endTime,endTime1,"0");
        //通用查询数据
        String data = YHInProjectLogic.queryProjectCalendar(dbConn,request.getParameterMap(),"0",activeType,activeContent,activeLeader,
            activePartner,startTime,startTime1,endTime,endTime1,"0");
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
     * 根据纪要查询项目
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String queryProjectByComm(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHProjectComm comm = new YHProjectComm();
        String commNum = request.getParameter("commNum");
        String commMemCn = request.getParameter("commMemCn");
        String commMemFn = request.getParameter("commMemFn");
        String commName = request.getParameter("commName");
        String commTime = request.getParameter("commTime");
        String commPlace = request.getParameter("commPlace");
        //String projId = YHProjectCommLogic.queryCommToProjId(dbConn,commNum,commMemCn,commMemFn,commName,commTime,commPlace,"0");
        //通用查询数据
        String data = YHInProjectLogic.queryProjectComm(dbConn,request.getParameterMap(),"0",commNum,commMemCn,commMemFn,commName,commTime,commPlace,"0");
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
     * 根据文档查询项目
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String queryProjectByFile(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHProjectFile file = new YHProjectFile();
        String fileNum = request.getParameter("fileNum");
        String fileName = request.getParameter("fileName");
        String fileType = request.getParameter("fileType");
        String projCreator = request.getParameter("projCreator");
        String fileTitle = request.getParameter("fileTitle");
        String projFileType = request.getParameter("projFileType");

       // String projId = YHProjectFileLogic.queryCommToProjId(dbConn,fileNum,fileName,fileType,projCreator,fileTitle,"0");
      //通用查询数据
        String data = YHInProjectLogic.queryProjectFile(dbConn,request.getParameterMap(),"0",fileNum,fileName,fileType,projCreator,fileTitle,"0");
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
}
