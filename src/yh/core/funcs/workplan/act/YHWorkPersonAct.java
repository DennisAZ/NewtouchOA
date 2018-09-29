package yh.core.funcs.workplan.act;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.funcs.workplan.data.YHWorkPerson;
import yh.core.funcs.workplan.data.YHWorkPlan;
import yh.core.funcs.workplan.data.YHWorkPlanCont;
import yh.core.funcs.workplan.logic.YHWorkLogic;
import yh.core.funcs.workplan.logic.YHWorkPersonLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;

public class YHWorkPersonAct {
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public String selectPerson(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkLogic logic = new YHWorkLogic();
      YHWorkPersonLogic personLogic = new YHWorkPersonLogic();
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String name = request.getParameter("name");
      
      List<YHWorkPerson> list = personLogic.selectPerson(dbConn,seqId,name);
      YHWorkPlan plan = logic.selectId(dbConn, seqId);
      request.setAttribute("plan", plan);
      request.setAttribute("name", name);
      request.setAttribute("person", list);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/core/funcs/workplan/show/resource.jsp";
  }
  
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public String person(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkLogic logic = new YHWorkLogic();
      YHWorkPersonLogic personLogic = new YHWorkPersonLogic();
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String name = request.getParameter("name");
      
      List<YHWorkPerson> list = personLogic.selectPerson(dbConn,seqId,name);
      YHWorkPlan plan = logic.selectId(dbConn, seqId);
      request.setAttribute("plan", plan);
      request.setAttribute("name", name);
      request.setAttribute("person", list);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/core/funcs/workplan/show/resource_diary.jsp";
  }
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public String selectPerson2(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkPersonLogic personLogic = new YHWorkPersonLogic();
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String name = request.getParameter("name");
      List<YHWorkPerson> list = personLogic.selectPerson(dbConn,seqId,name);
      
      //定义数组将数据保存到Json中
      String data = "[";
      YHWorkPerson person = new YHWorkPerson(); 
      for (int i = 0; i < list.size(); i++) {
        person = list.get(i);
        data = data + YHFOM.toJson(person).toString()+",";
      }
      if(list.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data.replaceAll("\\n", "");
      data = data.replaceAll("\\r", "");
      data = data + "]";
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
     //request.setAttribute("person", list); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/core/inc/rtjson.jsp";
  }
  /***
   * 根据ID添加数据
   * @return
   * @throws Exception 
   */
  public String addPerson(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkPersonLogic personLogic = new YHWorkPersonLogic();
      YHWorkPerson person = new YHWorkPerson();
      YHPerson persons = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //使用request.getParameter接受页面的值
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      String smsflag3 = request.getParameter("sms2flag");
      YHSelAttachUtil sel = new YHSelAttachUtil(request, YHWorkPlanCont.MODULE);
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
      
      String sqlId = String.valueOf(request.getParameter("sqlId"));
      String sqlName = String.valueOf(request.getParameter("sqlName"));
      
      String smsflag = request.getParameter("smsflag");
      if (smsflag.equals("true")) {
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("12");
        sb.setContent("有新的计划任务增加，请查看.");
        sb.setSendDate(new java.util.Date());
        sb.setFromId(persons.getSeqId());
        sb.setToId(sqlName);
        sb.setRemindUrl("/yh/core/funcs/workplan/act/YHWorkPersonAct/person.act?seqId=" + sqlId + "&name=" + sqlName + "&openFlag=1&openWidth=800&openHeight=550");
        YHSmsUtil.smsBack(dbConn, sb);
      }
      if (smsflag3.equals("true")) {
        YHMobileSms2Logic sb = new YHMobileSms2Logic();
        sb.remindByMobileSms(dbConn,sqlName,person.getSeqId(),"有新的计划任务增加，请查看:",new java.util.Date());
      }
      
      person.setPlanId(Integer.parseInt(request.getParameter("PLAN_ID")));
      person.setPuserId(request.getParameter("PUSER_ID"));
      person.setAttachmentId(attachmentId);
      person.setAttachmentName(attachmentName);
      person.setPplanContent(request.getParameter("PPLAN_CONTENT"));
      person.setPuseResource(request.getParameter("PUSE_RESOURCE"));
      person.setPbegeiDate(Date.valueOf(request.getParameter("statrTime")));
      person.setPendDate(Date.valueOf(request.getParameter("endTime")));
      
      personLogic.addPerson(dbConn, person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据失败");
      throw e;
    } finally {
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /***
   * 根据ID删除数据
   * @return
   * @throws Exception 
   */
  public String deletePerson(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    int planId;
    String name;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkPersonLogic personLogic = new YHWorkPersonLogic();
      //使用request.getParameter接受页面的值
      planId = Integer.parseInt(request.getParameter("planId"));
      name = request.getParameter("name");
      
      personLogic.deletePerson(dbConn, Integer.parseInt(request.getParameter("seqId")));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据失败");
      throw e;
    } finally {
    }
    return "/yh/core/funcs/workplan/act/YHWorkPersonAct/selectPerson.act?seqId=" + planId + "&name=" + name;
  }
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public String selectId(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkPersonLogic personLogic = new YHWorkPersonLogic();
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String name = String.valueOf(request.getParameter("name"));
      
      YHWorkPerson  person = personLogic.selectId(dbConn,seqId);
      request.setAttribute("person",person);
      request.setAttribute("name",name);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/core/funcs/workplan/show/modify_resource.jsp";
  }
  public String updatePerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String name;
    int planId;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkPersonLogic personLogic = new YHWorkPersonLogic();
      YHWorkPerson person = new YHWorkPerson();
      //使用request.getParameter接受页面的值
      name = request.getParameter("PUSER_ID");
      planId = Integer.parseInt(request.getParameter("PLAN_ID"));
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
//      if (YHUtility.isNullorEmpty(attachmentName)) {
//        attachmentName = request.getParameter("attachmentName2");
//      }
//      if (YHUtility.isNullorEmpty(attachmentId)) {
//        attachmentId = request.getParameter("attachmentId2");
//      }
      
      YHSelAttachUtil sel = new YHSelAttachUtil(request, YHWorkPlanCont.MODULE);
      String attachNewId = sel.getAttachIdToString(",");
      String attachNewName = sel.getAttachNameToString("*");
      if(!"".equals(attachNewId) && !YHUtility.isNullorEmpty(attachmentId) && !attachmentId.trim().endsWith(",")){
        attachmentId += ",";
      }
      attachmentId += attachNewId;
      if(!"".equals(attachNewName) && !YHUtility.isNullorEmpty(attachmentName) && !attachmentName.trim().endsWith("*")){
        attachmentName += "*";
      }
      attachmentName += attachNewName;
      
      person.setAttachmentName(attachmentName);
      person.setAttachmentId(attachmentId);
      person.setPplanContent(request.getParameter("PPLAN_CONTENT"));
      person.setPuseResource(request.getParameter("PUSE_RESOURCE"));
      person.setPbegeiDate(Date.valueOf(request.getParameter("statrTime")));
      person.setPendDate(Date.valueOf(request.getParameter("endTime")));
      person.setSeqId(Integer.parseInt(request.getParameter("seqId2")));
      
      personLogic.updatePerson(dbConn, person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    } finally {
    }
    return "/yh/core/funcs/workplan/act/YHWorkPersonAct/selectPerson.act?seqId=" + planId + "&name=" + name;
    
  }
  /***
   * 根据ID查询,修改,附件删除数据
   * @return
   * @throws Exception 
   */
  public String updatePerson2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String name;
    int planId;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkPersonLogic personLogic = new YHWorkPersonLogic();
      YHWorkPerson person = new YHWorkPerson();
      //使用request.getParameter接受页面的值
      name = request.getParameter("PUSER_ID");
      planId = Integer.parseInt(request.getParameter("PLAN_ID"));
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
//      if (YHUtility.isNullorEmpty(attachmentName)) {
//        attachmentName = request.getParameter("attachmentName2");
//      }
//      if (YHUtility.isNullorEmpty(attachmentId)) {
//        attachmentId = request.getParameter("attachmentId2");
//      }
      
      YHSelAttachUtil sel = new YHSelAttachUtil(request, YHWorkPlanCont.MODULE);
      String attachNewId = sel.getAttachIdToString(",");
      String attachNewName = sel.getAttachNameToString("*");
      if(!"".equals(attachNewId) && !YHUtility.isNullorEmpty(attachmentId) && !attachmentId.trim().endsWith(",")){
        attachmentId += ",";
      }
      attachmentId += attachNewId;
      if(!"".equals(attachNewName) && !YHUtility.isNullorEmpty(attachmentName) && !attachmentName.trim().endsWith("*")){
        attachmentName += "*";
      }
      attachmentName += attachNewName;
      
      person.setAttachmentName(attachmentName);
      person.setAttachmentId(attachmentId);
      person.setPplanContent(request.getParameter("PPLAN_CONTENT"));
      person.setPuseResource(request.getParameter("PUSE_RESOURCE"));
      person.setPbegeiDate(Date.valueOf(request.getParameter("statrTime")));
      person.setPendDate(Date.valueOf(request.getParameter("endTime")));
      person.setSeqId(Integer.parseInt(request.getParameter("seqId2")));
      
      personLogic.updatePerson(dbConn, person);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    } finally {
    }
    return "/core/inc/rtjson.jsp";
  }
}
