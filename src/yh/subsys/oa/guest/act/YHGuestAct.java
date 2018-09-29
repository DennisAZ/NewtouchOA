package yh.subsys.oa.guest.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.codeclass.data.YHCodeItem;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.act.YHDeptAct;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.guest.data.YHGuest;
import yh.subsys.oa.guest.logic.YHGuestLogic;
import yh.subsys.oa.profsys.logic.in.YHInProjectLogic;
import yh.subsys.oa.profsys.source.org.data.YHSourceOrg;
import yh.subsys.oa.profsys.source.org.logic.YHSourceOrgLogic;

public class YHGuestAct {
  /**
   * 新建更新
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String addUpdateGuest(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHGuest guest = (YHGuest)YHFOM.build(request.getParameterMap());
      YHGuestLogic guestLogic = new YHGuestLogic();

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
      guest.setAttachmentId(attachmentId);
      guest.setAttachmentName(attachmentName);
      int seqId = 0;
      if(guest !=null){

        if(guest.getSeqId()>0){
          seqId = guest.getSeqId();
          guestLogic.updateGuest(dbConn, guest);
        }else{
          seqId = guestLogic.addGuest(dbConn, guest);
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
   * 克隆ById

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String clonGuest(HttpServletRequest request,HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String guestNum = request.getParameter("guestNum");
      String guestName = request.getParameter("guestName");
      YHGuestLogic guestLogic = new YHGuestLogic();
      if(YHUtility.isInteger(seqId)){
        YHGuest guest = YHGuestLogic.selectGuestById(dbConn, seqId);
        if(guest != null){
          guest.setGuestName(guestName);
          guest.setGuestNum(guestNum);
          guestLogic.addGuest(dbConn, guest);
        }
        
      } 

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询所有
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String queryGuest(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
    //通用查询数据
      String data = YHGuestLogic.queryGuest(dbConn,request.getParameterMap());
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
   * 按条件查询
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String queryGuestTerm(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String guestNum = request.getParameter("guestNum") == null ? "" : request.getParameter("guestNum");
      String guestType = request.getParameter("guestType") == null ? "" : request.getParameter("guestType");
      String guestName = request.getParameter("guestName") == null ? "" : request.getParameter("guestName");
      String guestDiner = request.getParameter("guestDiner") == null ? "" : request.getParameter("guestDiner");
      String guestUnit = request.getParameter("guestUnit") == null ? "" : request.getParameter("guestUnit");
      String guestPhone = request.getParameter("guestPhone") == null ? "" : request.getParameter("guestPhone");
      String guestAttendTime = request.getParameter("guestAttendTime") == null ? "" : request.getParameter("guestAttendTime");
      String guestAttendTime1 = request.getParameter("guestAttendTime1") == null ? "" : request.getParameter("guestAttendTime1");
      String guestLeaveTime = request.getParameter("guestLeaveTime") == null ? "" : request.getParameter("guestLeaveTime");
      String guestLeaveTime1 = request.getParameter("guestLeaveTime1") == null ? "" : request.getParameter("guestLeaveTime1");
      String guestCreator = request.getParameter("guestCreator") == null ? "" : request.getParameter("guestCreator");
      String guestDept = request.getParameter("guestDept") == null ? "" : request.getParameter("guestDept");
      String guestNote = request.getParameter("guestNote") == null ? "" : request.getParameter("guestNote");
    //通用查询数据
      String data = YHGuestLogic.queryGuestTrem(dbConn,request.getParameterMap(),guestNum,guestType,guestName,guestDiner,guestUnit
          ,guestPhone,guestAttendTime,guestAttendTime1,guestLeaveTime,guestLeaveTime1,guestCreator,guestDept,guestNote);
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
   * 根据ID字符串取得部门名称

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDeptNameBySeqIds(HttpServletRequest request,HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptId = request.getParameter("deptId");
      String data = "";
      if(!YHUtility.isNullorEmpty(deptId)){
        YHDeptLogic d = new  YHDeptLogic();
        String deptName = YHUtility.encodeSpecial(d.getNameByIdStr(deptId, dbConn));
        data = "{deptName:\"" + deptName +"\"}";
      }
      if(data.equals("")){
        data = "{}";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 删除

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delGuest(HttpServletRequest request,HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      if(YHUtility.isInteger(seqId)){
        YHGuestLogic.delGuest(dbConn, seqId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");

    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询ById

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getGuestById(HttpServletRequest request,HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String data = "";
      if(YHUtility.isInteger(seqId)){
        YHGuest guest = YHGuestLogic.selectGuestById(dbConn, seqId);
        if(guest != null){
          String deptName = "";
          if(!YHUtility.isNullorEmpty(guest.getGuestDept())){
            YHDeptLogic d = new  YHDeptLogic();
            deptName = YHUtility.encodeSpecial(d.getNameByIdStr(guest.getGuestDept(), dbConn));
          }
          String guestTypeDesc = "";
          if(!YHUtility.isNullorEmpty(guest.getGuestType())){
            YHCodeItem  codeItem = YHInProjectLogic.getCodeItem(dbConn, guest.getGuestType());
            if(codeItem != null){
              guestTypeDesc = YHUtility.encodeSpecial(codeItem.getClassDesc());
            }
          }
          String guestCreatorName = "";
          if(!YHUtility.isNullorEmpty(guest.getGuestCreator())){
            YHPersonLogic personLogic = new YHPersonLogic();
            guestCreatorName = YHUtility.encodeSpecial(personLogic.getNameBySeqIdStr(guest.getGuestCreator(), dbConn));
          }
          data = data + YHFOM.toJson(guest).toString().substring(0, YHFOM.toJson(guest).toString().length()-1) + ",deptName:\"" +deptName + "\",guestCreatorName:\"" + guestCreatorName + "\",guestTypeDesc:\"" + guestTypeDesc + "\"}";
        }
      } 
      if(data.equals("")){
        data = "{}";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 单文件附件上传

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String uploadFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    Map<String, String> attr = null;
    String attrId = (fileForm.getParameter("attachmentId")== null )? "":fileForm.getParameter("attachmentId");
    String attrName = (fileForm.getParameter("attachmentName")== null )? "":fileForm.getParameter("attachmentName");
    String data = "";
    try{
      YHGuestLogic projectLogic = new YHGuestLogic();
      attr = projectLogic.fileUploadLogic(fileForm);
      Set<String> keys = attr.keySet();
      for (String key : keys){
        String value = attr.get(key);
        if(attrId != null && !"".equals(attrId)){
          if(!(attrId.trim()).endsWith(",")){
            attrId += ",";
          }
          if(!(attrName.trim()).endsWith("*")){
            attrName += "*";
          }
        }
        attrId += key + ",";
        attrName += value + "*";
      }
      data = "{attrId:\"" + attrId + "\",attrName:\"" + attrName + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);

    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
      throw e;
    }
    return "/core/inc/rtuploadfile.jsp";
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
      YHGuestLogic orgLogic = new YHGuestLogic();
      YHGuest guest  = null ;
      String updateFlag = "0";
      if(seqId!=null&&!seqId.equals("")){
        guest = orgLogic.selectGuestById(dbConn, seqId)  ;
       if(guest!=null){
         String attachmentId = guest.getAttachmentId();
         String attachmentName = guest.getAttachmentName();
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
         
         orgLogic.updateFile(dbConn,"oa_host", newAttachmentId, newAttachmentName, seqId);
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
}
