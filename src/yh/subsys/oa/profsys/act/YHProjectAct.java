package yh.subsys.oa.profsys.act;

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
import yh.core.codeclass.logic.YHCodeClassLogic;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.finance.data.YHBudgetApply;
import yh.subsys.oa.finance.logic.YHBudgetApplyLogic;
import yh.subsys.oa.profsys.data.YHProject;
import yh.subsys.oa.profsys.data.YHProjectCalendar;
import yh.subsys.oa.profsys.logic.YHProjectCalendarLogic;
import yh.subsys.oa.profsys.logic.YHProjectLogic;
import yh.subsys.oa.profsys.logic.in.YHInProjectLogic;

public class YHProjectAct {
  /**
   * 新建更新项目
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String addUpdateProject(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHProject project = (YHProject)YHFOM.build(request.getParameterMap());
      YHProjectLogic projectLogic = new YHProjectLogic();

      //文件柜中上传附件
      String attachmentName = request.getParameter("attachmentName");
      String attachmentId = request.getParameter("attachmentId");
      YHSelAttachUtil sel = new YHSelAttachUtil(request,"profsys");
      String attachNewId = sel.getAttachIdToString(",");
      String attachNewName = sel.getAttachNameToString("*");
      if(!"".equals(attachNewId) && !"".equals(attachmentId) &&  !attachmentId.trim().endsWith(",")){
        attachmentId += ",";
      }
      attachmentId += attachNewId;
      if(!"".equals(attachNewName) && !"".equals(attachmentName)  && !attachmentName.trim().endsWith("*")){
        attachmentName += "*";
      }
      attachmentName += attachNewName;
      project.setAttachmentId(attachmentId);
      project.setAttachmentName(attachmentName);
      int seqId = 0;
      if(project !=null){
        project.setProjCreator(user.getSeqId() + "");
        project.setDeptId(user.getDeptId());
        project.setProjDate(new Date());
        if(project.getSeqId()>0){
          seqId = project.getSeqId();
          projectLogic.updateProject(dbConn, project);
        }else{
          project.setProjStatus("0");
          project.setPrintStatus("0");
          seqId = projectLogic.addProject(dbConn, project);
        }
      }
      String data = "{seqId:" + seqId + "}";
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
   * 新建项目
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addUpdateProjectCopy(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
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
      // 保存从文件柜、网络硬盘选择附件
      YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, "profsys");
      String attIdStr = sel.getAttachIdToString(",");
      String attNameStr = sel.getAttachNameToString("*");
      
      
      YHProject project = new YHProject();
      String seqId = fileForm.getParameter("seqId");
      project.setProjCreator(userId + "");
      project.setDeptId(user.getDeptId());
      project.setProjType(YHUtility.isNullorEmpty(fileForm.getParameter("projType")) ? "" : fileForm.getParameter("projType"));
      project.setProjNum(YHUtility.isNullorEmpty(fileForm.getParameter("projNum")) ? "" : fileForm.getParameter("projNum"));
      project.setProjGroupName(YHUtility.isNullorEmpty(fileForm.getParameter("projGroupName")) ? "" : fileForm.getParameter("projGroupName"));
      project.setProjVisitType (YHUtility.isNullorEmpty(fileForm.getParameter("projVisitType")) ? "" : fileForm.getParameter("projVisitType"));
      project.setProjActiveType(YHUtility.isNullorEmpty(fileForm.getParameter("projActiveType")) ? "" : fileForm.getParameter("projActiveType"));
      project.setProjLeader(YHUtility.isNullorEmpty(fileForm.getParameter("projLeader")) ? "" : fileForm.getParameter("projLeader"));
      project.setProjManager(YHUtility.isNullorEmpty(fileForm.getParameter("projManager")) ? "" : fileForm.getParameter("projManager"));
      project.setProjArriveTime(YHUtility.isNullorEmpty(fileForm.getParameter("projArriveTime")) ? null : dateFormat.parse(fileForm.getParameter("projArriveTime")));
      project.setProjLeaveTime(YHUtility.isNullorEmpty(fileForm.getParameter("projLeaveTime")) ? null : dateFormat.parse(fileForm.getParameter("projLeaveTime")));
      project.setProjStartTime(YHUtility.isNullorEmpty(fileForm.getParameter("projStartTime")) ? null : dateFormat.parse(fileForm.getParameter("projStartTime")));
      project.setProjEndTime(YHUtility.isNullorEmpty(fileForm.getParameter("projEndTime")) ? null : dateFormat.parse(fileForm.getParameter("projEndTime")));
      project.setProjList(YHUtility.isNullorEmpty(fileForm.getParameter("projList")) ? "" : fileForm.getParameter("projList"));
      project.setProjOrganizer(YHUtility.isNullorEmpty(fileForm.getParameter("projOrganizer")) ? "" : fileForm.getParameter("projOrganizer"));
      project.setProjOperator(YHUtility.isNullorEmpty(fileForm.getParameter("projOperator")) ? "" : fileForm.getParameter("projOperator"));
      project.setProjSponsor(YHUtility.isNullorEmpty(fileForm.getParameter("projSponsor")) ? "" : fileForm.getParameter("projSponsor"));
      project.setProjDept(YHUtility.isNullorEmpty(fileForm.getParameter("projDept")) ? "" : fileForm.getParameter("projDept"));
      project.setProjViwer(YHUtility.isNullorEmpty(fileForm.getParameter("projViwer")) ? "" : fileForm.getParameter("projViwer"));
      project.setProjLeaderDescription(YHUtility.isNullorEmpty(fileForm.getParameter("projLeaderDescription")) ? "" : fileForm.getParameter("projLeaderDescription"));
      project.setProjUnitDescription(YHUtility.isNullorEmpty(fileForm.getParameter("projUnitDescription")) ? "" : fileForm.getParameter("projUnitDescription"));
      project.setProjNote(YHUtility.isNullorEmpty(fileForm.getParameter("projNote")) ? "" : fileForm.getParameter("projNote"));
      project.setProjStatus(YHUtility.isNullorEmpty(fileForm.getParameter("projStatus")) ? "" : fileForm.getParameter("projStatus"));
      project.setPTotal(YHUtility.isInteger(fileForm.getParameter("pTotal")) ? Integer.parseInt(fileForm.getParameter("pTotal")) : 0);
      project.setPYx(YHUtility.isInteger(fileForm.getParameter("pYx")) ? Integer.parseInt(fileForm.getParameter("pYx")) : 0);
      project.setPCouncil(YHUtility.isInteger(fileForm.getParameter("pCouncil")) ? Integer.parseInt(fileForm.getParameter("pCouncil")) : 0);
      project.setPGuest(YHUtility.isInteger(fileForm.getParameter("pGuest")) ?  Integer.parseInt(fileForm.getParameter("pGuest")):0);
      project.setPurposeCountry(YHUtility.isNullorEmpty(fileForm.getParameter("purposeCountry")) ? "" : fileForm.getParameter("purposeCountry"));
      project.setCountryTotal(YHUtility.isInteger(fileForm.getParameter("countryTotal")) ? Integer.parseInt(fileForm.getParameter("countryTotal")) : 0);
      project.setBudgetId(YHUtility.isInteger(fileForm.getParameter("budgetId")) ? Integer.parseInt(fileForm.getParameter("budgetId")) : 0);
      project.setProjDate(new Date());
    
      Iterator<String> iKeys = fileForm.iterateFileFields();
      String filePath = YHSysProps.getAttachPath()  + File.separator  + "profsys" + File.separator  + dateFormat2.format(new Date()); // YHSysProps.getAttachPath()
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
        fileForm.saveFile(fieldName, filePath + File.separator  + fileName);
      }
      attachmentId = attachmentId + attIdStr;
      attachmentName = attachmentName + attNameStr;

 
      
      YHProjectLogic projectLogic = new YHProjectLogic();
      if(YHUtility.isInteger(seqId)){//更新
        project.setSeqId(Integer.parseInt(seqId));
        
        //先查出数据库的附件，然后加上
        String attIdStrTemp = "";
        String attNameStrTemp = "";
        YHProject projectTemp  = projectLogic.getProjectById(dbConn, seqId)  ;
        if(projectTemp!=null){
          attIdStrTemp  = projectTemp.getAttachmentId();
          attNameStrTemp = projectTemp.getAttachmentName();
        }
        if(!YHUtility.isNullorEmpty(attIdStrTemp)){
          attachmentId = attachmentId + "," + attIdStrTemp;
        }
        if(!YHUtility.isNullorEmpty(attachmentName)){
          attachmentName = attachmentName + "*" + attNameStrTemp;
        }
        project.setAttachmentId(attachmentId);
        project.setAttachmentName(attachmentName);
        projectLogic.updateProject(dbConn, project);
      }else{//新增
        project.setAttachmentId(attachmentId);
        project.setAttachmentName(attachmentName);
        int seqIdInt = projectLogic.addProject(dbConn, project);
        seqId = seqIdInt + "";

      }
      String path = request.getContextPath();
      response.sendRedirect(path+ "/subsys/oa/profsys/in/baseinfo/news/base.jsp?seqId=" + seqId);
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
      YHProject project = projectLogic.getProjectById(dbConn, seqId);
      if(project!=null){
        data = YHFOM.toJson(project).toString();
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
   * 从系统代码设置得到所有类型

   * 根据seqId（codeClass） 得到所有的codeItem
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
      String classNo = request.getParameter("classNo");
      if(classNo==null){
        classNo = "";
      }
      YHCodeClassLogic codeLogic = new YHCodeClassLogic();
      String data = "[";
      List<YHCodeItem> itemList = new ArrayList<YHCodeItem>();
      itemList = codeLogic.getCodeItem(dbConn, classNo);
      for (int i = 0; i < itemList.size(); i++) {
        YHCodeItem item = itemList.get(i);
        data = data + YHFOM.toJson(item) + ",";
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
   * 取出国家
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCountry(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String moduleId = request.getParameter("moduleId");
    String privOp = request.getParameter("privOp");
    String privNoFlagStr = request.getParameter("privNoFlag");
    int privNoFlag = 0;
    if (!YHUtility.isNullorEmpty(privNoFlagStr)) {
      privNoFlag = Integer.parseInt(privNoFlagStr);
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHUserPrivLogic logic = new YHUserPrivLogic();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      List<YHCodeItem> itemList = new ArrayList<YHCodeItem>();
      YHCodeClassLogic codeLogic = new YHCodeClassLogic();
      itemList = codeLogic.getCodeItem(dbConn, "NATION");
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < itemList.size(); i++) {
        YHCodeItem item = itemList.get(i);
        String str = "{";
        str += "privNo:" + item.getSqlId() + ",";  
        str += "privName:\"" + YHUtility.encodeSpecial(item.getClassDesc()) + "\"";  
        str += "},";
        sb.append(str);
      }
      if (itemList.size() > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "get Success");
      request.setAttribute(YHActionKeys.RET_DATA, "[" + sb.toString() + "]");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /***
   * 更新的附件ById
     删除一个附件
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
      YHProjectLogic projectLogic = new YHProjectLogic();
      YHProject project  = null ;
      String updateFlag = "0";
      if(seqId!=null&&!seqId.equals("")){
        project = projectLogic.getProjectById(dbConn, seqId)  ;
       if(project!=null){
         String attachmentId = project.getAttachmentId();
         String attachmentName = project.getAttachmentName();
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
         
         projectLogic.updateFile(dbConn,"oa_project", newAttachmentId, newAttachmentName, seqId);
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
   * 根据用户的管理权限得到所有部门（考勤统计）

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectDeptToAttendance(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String userPriv = user.getUserPriv();//角色
      String postpriv = user.getPostPriv();//管理范围
      String postDept = user.getPostDept();//管理范围指定部门
      int userDeptId = user.getDeptId();
      YHDeptLogic deptLogic = new YHDeptLogic();
      String userDeptName = deptLogic.getNameByIdStr(String.valueOf(userDeptId), dbConn);
      String data = "";
      if(userPriv!=null&&userPriv.equals("1")&&user.getUserId().trim().equals("admin")){//假如是系统管理员的都快要看得到.而且是ADMIN用户
        data =  deptLogic.getDeptTreeJson(0,dbConn) ;
        
      }else{
        if(postpriv.equals("0")){
         // data = "[{text:\"" + userDeptName + "\",value:" + userDeptId + "}]";
          String[] postDeptArray = {String.valueOf(userDeptId)};
          data =  "[" + deptLogic.getDeptTreeJson(0,dbConn,postDeptArray)+ "]";
        }
        if(postpriv.equals("1")){
          data =  deptLogic.getDeptTreeJson(0,dbConn) ;
        }
        if(postpriv.equals("2")){
          if(postDept==null||postDept.equals("")){
            data = "[]";
          }else{
             String[] postDeptArray = postDept.split(",");
             data =  "[" + deptLogic.getDeptTreeJson(0,dbConn,postDeptArray)+ "]";

          }
        }
      }
      if(data.equals("")){
        data = "[]";
      }
      data = data.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r\n", "").replace("\n", "").replace("\r", "");
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, String.valueOf(userDeptId)+","+postpriv);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  
  /**
   * 检索项目BY projType
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

        String projType = request.getParameter("projType");
        if(YHUtility.isNullorEmpty(projType)){
          projType = "0";
        }
        YHProjectLogic tbal = new YHProjectLogic();
        String data = tbal.toSearchData(dbConn, request.getParameterMap(),projType);
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
