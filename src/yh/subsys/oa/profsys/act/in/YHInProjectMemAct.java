package yh.subsys.oa.profsys.act.in;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.data.YHProjectMem;
import yh.subsys.oa.profsys.logic.YHProjectMemLogic;
import yh.subsys.oa.profsys.logic.in.YHInProjectMemLogic;
import yh.subsys.oa.profsys.logic.out.YHOutProjectMemLogic;

public class YHInProjectMemAct {
  /**
   * 新建项目——人员
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addUpdateMem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHProjectMem mem = (YHProjectMem)YHFOM.build(request.getParameterMap());
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
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
      mem.setAttachmentId(attachmentId);
      mem.setAttachmentName(attachmentName);
      int userId = user.getSeqId();
      mem.setProjDate(new Date());
      mem.setProjCreator(userId + "");
      int seqId = 0 ;
        YHProjectMemLogic memLogic = new YHProjectMemLogic();
        if(mem.getSeqId()>0){//更新
          seqId = mem.getSeqId();
          memLogic.updateMem(dbConn, mem);
        }else{//新增
          int seqIdInt = memLogic.addMem(dbConn, mem);
          seqId = seqIdInt;
      }
      String data = "{seqId:" + seqId + "}";
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
   * 新建项目——人员
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addUpdateMemCopy(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      String projId = fileForm.getParameter("projId");//项目ID
      String seqId = fileForm.getParameter("seqId");
      if(YHUtility.isInteger(projId)){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyMM");
        YHPerson user = (YHPerson) request.getSession().getAttribute(
            YHConst.LOGIN_USER);
        int userId = user.getSeqId();
        Calendar cl = Calendar.getInstance();
        int curYear = cl.get(Calendar.YEAR);
      
        // 保存从文件柜、网络硬盘选择附件
        YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, "profsys");
        String attIdStr = sel.getAttachIdToString(",");
        String attNameStr = sel.getAttachNameToString("*");
        
        
        YHProjectMem mem = new YHProjectMem();
        mem.setProjId(Integer.parseInt(projId));
        mem.setProjCreator(userId + "");
        mem.setProjDate(new Date());
        mem.setProjMemType("0");
        mem.setMemNum(YHUtility.isNullorEmpty(fileForm.getParameter("memNum")) ? "" : fileForm.getParameter("memNum"));
        mem.setMemRole(YHUtility.isNullorEmpty(fileForm.getParameter("memRole")) ? "" : fileForm.getParameter("memRole"));
        mem.setMemName (YHUtility.isNullorEmpty(fileForm.getParameter("memName")) ? "" : fileForm.getParameter("memName"));
        mem.setMemSex(YHUtility.isNullorEmpty(fileForm.getParameter("memSex")) ? "" : fileForm.getParameter("memSex"));
        mem.setMemPosition(YHUtility.isNullorEmpty(fileForm.getParameter("memPositionId")) ? "" : fileForm.getParameter("memPositionId"));
        mem.setMemNation(YHUtility.isNullorEmpty(fileForm.getParameter("memNation")) ? "" : fileForm.getParameter("memNation"));
        mem.setMemNativePlace(YHUtility.isNullorEmpty(fileForm.getParameter("memNativePlace")) ? "" : fileForm.getParameter("memNativePlace"));
        mem.setMemBirthplace(YHUtility.isNullorEmpty(fileForm.getParameter("memBirthplace")) ? "" : fileForm.getParameter("memBirthplace"));
        mem.setMemIdNum(YHUtility.isNullorEmpty(fileForm.getParameter("memIdNum")) ? "" : fileForm.getParameter("memIdNum"));
        mem.setMemPhone(YHUtility.isNullorEmpty(fileForm.getParameter("memPhone")) ? "" : fileForm.getParameter("memPhone"));
        mem.setMemMail(YHUtility.isNullorEmpty(fileForm.getParameter("memMail")) ? "" : fileForm.getParameter("memMail"));
        mem.setMemFax(YHUtility.isNullorEmpty(fileForm.getParameter("memFax")) ? "" : fileForm.getParameter("memFax"));
        mem.setMemAddress(YHUtility.isNullorEmpty(fileForm.getParameter("memAddress")) ? "" : fileForm.getParameter("memAddress"));
        mem.setMemNote(YHUtility.isNullorEmpty(fileForm.getParameter("memNote")) ? "" : fileForm.getParameter("memNote"));      
        mem.setMemBirth(YHUtility.isNullorEmpty(fileForm.getParameter("memBirth")) ? null : dateFormat.parse(fileForm.getParameter("memBirth")));
      
        Iterator<String> iKeys = fileForm.iterateFileFields();
        String filePath = YHSysProps.getAttachPath()  + File.separator  +"profsys"  + File.separator  + dateFormat2.format(new Date()); // YHSysProps.getAttachPath()
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

   
        
        YHProjectMemLogic memLogic = new YHProjectMemLogic();
        if(YHUtility.isInteger(seqId)){//更新
          mem.setSeqId(Integer.parseInt(seqId));
          
          //先查出数据库的附件，然后加上
          String attIdStrTemp = "";
          String attNameStrTemp = "";
          YHProjectMem memTemp  = memLogic.getMemById(dbConn, seqId)  ;
          if(memTemp!=null){
            attIdStrTemp  = memTemp.getAttachmentId();
            attNameStrTemp = memTemp.getAttachmentName();
          }
          if(!YHUtility.isNullorEmpty(attIdStrTemp)){
            attachmentId = attachmentId + "," + attIdStrTemp;
          }
          if(!YHUtility.isNullorEmpty(attachmentName)){
            attachmentName = attachmentName + "*" + attNameStrTemp;
          }
          mem.setAttachmentId(attachmentId);
          mem.setAttachmentName(attachmentName);
          memLogic.updateMem(dbConn, mem);
        }else{//新增
          mem.setAttachmentId(attachmentId);
          mem.setAttachmentName(attachmentName);
          int seqIdInt = memLogic.addMem(dbConn, mem);
          seqId = seqIdInt + "";

        }
      }
     
      String path = request.getContextPath();
      response.sendRedirect(path+ "/subsys/oa/profsys/in/baseinfo/news/user.jsp?seqId=" + seqId +"&projId=" + projId);
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
   * 来访项目人员By ProjId
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
    public String queryInMemByProjId(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
        int userId = user.getSeqId(); 
        String projId = request.getParameter("projId");
        if(YHUtility.isNullorEmpty(projId)){
          projId = "0";
        }
        YHInProjectMemLogic tbal = new YHInProjectMemLogic();
        String data = tbal.toSearchData(dbConn, request.getParameterMap(),projId,"0");
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
   * 来访项目人员By ProjId
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getMemById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String data = "";
      if (YHUtility.isInteger(seqId)) {
        YHProjectMemLogic memLogic = new YHProjectMemLogic();
        YHProjectMem mem = memLogic.getMemById(dbConn, seqId);
        if(mem != null){
          String privName = YHOutProjectMemLogic.userName(dbConn,mem.getMemPosition()); 
          data = YHFOM.toJson(mem).toString().substring(0, YHFOM.toJson(mem).toString().length()-1) + ",memPositionName:\"" + YHUtility.encodeSpecial(privName) + "\"}";
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
}
