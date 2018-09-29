package yh.subsys.oa.profsys.source.person.act;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.source.person.data.YHSourcePerson;
import yh.subsys.oa.profsys.source.person.logic.YHSourcePersonLogic;

public class YHSourcePersonAct {
  /**
   * 新建项目
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addPerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSourcePerson person = (YHSourcePerson)YHFOM.build(request.getParameterMap());

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

      person.setAttachmentId(attachmentId);
      person.setAttachmentName(attachmentName);

      YHSourcePersonLogic.addPerson(dbConn, person);
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
   *查询所有(分页)通用列表显示数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectPerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      YHSourcePerson person = new YHSourcePerson();
      String perNum = request.getParameter("perNum");
      String perNation = request.getParameter("perNation");
      String perPosition = request.getParameter("perPosition");
      String perVocation = request.getParameter("perVocation");
      String perName = request.getParameter("perName");
      String perSex = request.getParameter("perSex");
      String perBirthday = request.getParameter("perBirthday");
      String perBirthday1 = request.getParameter("perBirthday1");

      person.setPerNum(perNum);
      person.setPerNation(perNation);
      person.setPerVocation(perVocation);
      person.setPerName(perName);
      person.setPerSex(perSex);
      person.setPerPosition(perPosition);
      if (!YHUtility.isNullorEmpty(perBirthday)) {
        person.setPerBirthday(Date.valueOf(perBirthday));
      }
      Date birthDay = null;
      if (!YHUtility.isNullorEmpty(perBirthday1)) {
        birthDay = Date.valueOf(perBirthday1);
      }
      String data = YHSourcePersonLogic.selectPerson(dbConn,request.getParameterMap(),person,birthDay);
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
   *查询详情信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String showDetail(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHSourcePerson person = new YHSourcePerson();
      if (!YHUtility.isNullorEmpty(seqId)) {
        person = YHSourcePersonLogic.showDetail(dbConn,Integer.parseInt(seqId));
      }
      //定义数组将数据保存到Json中
      String data = "";
      if(person != null) {
        data = data + YHFOM.toJson(person);
        data = data.replaceAll("\\n", "");
        data = data.replaceAll("\\r", "");
      }
      data = data + "";
      if(data.equals("")){
        data = "{}";
      }
      //保存查询数据是否成功，保存date
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   *删除信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      if (!YHUtility.isNullorEmpty(seqId)) {
        YHSourcePersonLogic.deleteInfo(dbConn,Integer.parseInt(seqId));
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   *删除信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updatePerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHSourcePerson person = (YHSourcePerson)YHFOM.build(request.getParameterMap());

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

      person.setAttachmentId(attachmentId);
      person.setAttachmentName(attachmentName);

      YHSourcePersonLogic.updatePerson(dbConn,person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
