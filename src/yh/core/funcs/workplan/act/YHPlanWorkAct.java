package yh.core.funcs.workplan.act;
import java.io.PrintWriter;
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
import yh.core.funcs.workplan.data.YHWorkPlan;
import yh.core.funcs.workplan.data.YHWorkPlanCont;
import yh.core.funcs.workplan.logic.YHWorkLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHPlanWorkAct {
	  /**
	   * 添加数据
	   * @param request
	   * @param response
	   * @throws Exception
	   */
	  public String add(HttpServletRequest request,
	      HttpServletResponse response) throws  Exception {
	    Connection dbConn = null;
	    try {
	      //数据库的连接
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      //实列化业务层
	      YHWorkLogic workLogic = new YHWorkLogic();
	      //使用request.getParameter接受页面的值

	      String name = request.getParameter("name");
	      String publish = request.getParameter("PUBLISH");
	      String deptParentDesc = request.getParameter("deptParent");
	      String managerDesc = request.getParameter("manager");
	      String leader1Desc = request.getParameter("leader1");
	      String leader2Desc = request.getParameter("leader2");
	      String leader3Desc = request.getParameter("leader3");
	      String smsflag = request.getParameter("sms");
	      String smsflag2 = request.getParameter("smsflag");
	      String smsflag3 = request.getParameter("sms2flag");
	      String type = request.getParameter("WORK_TYPE");
	      String remark = request.getParameter("remark");
	      String attachmentcomment = request.getParameter("ATTACHMENT_COMMENT");
	      String content = request.getParameter("DIARY_CONTENT");
	      String attachmentid = request.getParameter("attachmentId");
	      String attachmentname = request.getParameter("attachmentName");

	      YHSelAttachUtil sel = new YHSelAttachUtil(request, YHWorkPlanCont.MODULE);
	      String attachNewId = sel.getAttachIdToString(",");
	      String attachNewName = sel.getAttachNameToString("*");
	      if(!"".equals(attachNewId) && !"".equals(attachmentid) &&  !attachmentid.trim().endsWith(",")){
	        attachmentid += ",";
	      }
	      attachmentid += attachNewId;
	      if(!"".equals(attachNewName) && !"".equals(attachmentname)  && !attachmentname.trim().endsWith("*")){
	        attachmentname += "*";
	      }
	      attachmentname += attachNewName;

	      String dateTime=request.getParameter("CREATE_DATE");
	      
	      java.util.Date createdate = YHUtility.parseDate(dateTime);
	      java.sql.Date date=new java.sql.Date(createdate.getTime());
	      
	      Date statrTime = Date.valueOf(request.getParameter("statrTime"));
	      Date endTime = Date.valueOf(request.getParameter("endTime"));
	      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
	      
	      //实列化实体层
	      YHWorkPlan workPlan = new YHWorkPlan();
	      workPlan.setStatrTime(statrTime);
	      workPlan.setEndTime(endTime);
	      workPlan.setCreatedate(date);
	      workPlan.setAttachmentname(attachmentname);
	      workPlan.setAttachmentid(attachmentid);
	      workPlan.setContent(content);
	      workPlan.setAttachmentcomment(attachmentcomment);
	      workPlan.setRemark(remark);
	      workPlan.setType(type);
	      workPlan.setPublish(publish);
	      workPlan.setSmsflag(smsflag);
	      workPlan.setName(name);
	      workPlan.setDeptParentDesc(deptParentDesc);
	      workPlan.setManagerDesc(managerDesc);
	      workPlan.setLeader1Desc(leader1Desc);
	      workPlan.setLeader2Desc(leader2Desc);
	      workPlan.setLeader3Desc(leader3Desc);
	      workPlan.setCreator(String.valueOf(person.getSeqId()));
	      //调用业务层add方法
	      workLogic.add(dbConn, workPlan);
	      
	      String sqlName = "0,";
	      if (!YHUtility.isNullorEmpty(leader1Desc)) {
	        sqlName += leader1Desc + ",";
	      }
	      if (!YHUtility.isNullorEmpty(leader2Desc)) {
	        sqlName += leader2Desc + ",";
	      }
	      if (!YHUtility.isNullorEmpty(leader3Desc)) {
	        sqlName += leader3Desc + ",";
	      }
	      if (!YHUtility.isNullorEmpty(managerDesc)) {
	        sqlName += managerDesc + ",";
	      }
	      int getMaxSeqId = workLogic.getMaxSeqId(dbConn);

	      if (smsflag2.equals("true") && !YHUtility.isNullorEmpty(smsflag2)) {
	        YHSmsBack sb = new YHSmsBack();
	        sb.setSmsType("12");
	        sb.setContent(person.getUserName() + "新建新的工作计划,请注意查看:" + name);
	        sb.setSendDate(new java.util.Date());
	        sb.setFromId(person.getSeqId());
	        sb.setToId(sqlName);
	        sb.setRemindUrl("/yh/core/funcs/workplan/act/YHPlanWorkAct/workSelect2.act?seqId=" + getMaxSeqId + "&openFlag=1&openWidth=700&openHeight=650");
	        YHSmsUtil.smsBack(dbConn, sb);
	      }
	      if (smsflag3.equals("true")) {
	        YHMobileSms2Logic sb = new YHMobileSms2Logic();
	        sb.remindByMobileSms(dbConn,sqlName,person.getSeqId(),person.getUserName() + "新建新的工作计划,请注意查看:" + name,new java.util.Date());
	      }
	      
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");

	      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "添加失败");
	      throw e;
	    } finally {
	    }
	    return "/core/inc/rtjson.jsp";
	  }
	  /**
	   * 修改状态数据


	   * @param request
	   * @param response
	   * @throws Exception 
	   */
	  public String updatePlu(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    try {
	      //数据库的连接
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      //接受对应页面值

	      int seqId = Integer.parseInt(request.getParameter("seqId"));
	      String pub = request.getParameter("pub");
	      //实列化业务层
	      YHWorkLogic workLogic = new YHWorkLogic();
	      //调用业务层updatePub方法修改数据
	      workLogic.updatePub(dbConn,seqId,pub);
	      //保存修改数据是否成功
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
	      throw e;
	    }
	    return "/core/inc/rtjson.jsp";
	  }
	  /**
	   * 修改状态数据


	   * @param request
	   * @param response
	   * @throws Exception 
	   */
	  public String updatePlu4(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    try {
	      //数据库的连接
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      //接受对应页面值

	      int seqId = Integer.parseInt(request.getParameter("seqId"));    
	      String pub = request.getParameter("pub");
	      String time = request.getParameter("time");
	      Date endTime = null;
	      if (!YHUtility.isNullorEmpty(time)) {
	        endTime = Date.valueOf(time);
	      }
	      //实列化业务层
	      YHWorkLogic workLogic = new YHWorkLogic();
	      //调用业务层updatePub方法修改数据
	      workLogic.updatePub4(dbConn,seqId,pub,endTime);
	      //保存修改数据是否成功
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
	      throw e;
	    }
	    return "/core/inc/rtjson.jsp";
	  }
	  
	  
	  /**
	   * 修改状态数据


	   * @param request
	   * @param response
	   * @throws Exception 
	   */
	  public String updatePlu8(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    try {
	      //数据库的连接
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      //接受对应页面值

	      int seqId = Integer.parseInt(request.getParameter("seqId"));
	      String pub = request.getParameter("pub");
	      String time = request.getParameter("time");
	      Date endTime = null;
	      if (!YHUtility.isNullorEmpty(time)) {
	        endTime = Date.valueOf(time);
	      }
	      //实列化业务层
	      YHWorkLogic workLogic = new YHWorkLogic();
	      //调用业务层updatePub方法修改数据
	      workLogic.updatePub8(dbConn, seqId, pub,endTime);
	      //保存修改数据是否成功
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
	      throw e;
	    }
	    return "";
	  }
	  /*****
	   * 删除数据
	   * @param request
	   * @param response
	   * @return
	   * @throws Exception
	   */
	  public String deleteWork(HttpServletRequest request,
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
	      //调用业务层deleteWork方法删除数据
	      workLogic.deleteWork(dbConn, seqId);
	      //保存删除数据是否成功
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据失败");
	      throw e;
	    }
	    return "/core/inc/rtjson.jsp";
	  }

	  /*****
	   * 删除数据
	   * @param request
	   * @param response
	   * @return
	   * @throws Exception
	   */
	  public String deleteQuery3(HttpServletRequest request,
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
	      //调用业务层deleteWork方法删除数据
	      workLogic.deleteWork(dbConn, seqId);
	      //保存删除数据是否成功
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据失败");
	      throw e;
	    }
	    return "/core/inc/rtjson.jsp";
	  }

	  /*****
	   * 删除所有数据

	   * @param request
	   * @param response
	   * @return
	   * @throws Exception
	   */
	  public String deleteWorkAll(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    try {
	      //数据库的连接
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      //实列化业务层YHWorkLogic
	      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
	      YHWorkLogic workLogic = new YHWorkLogic();
	      //调用业务层deleteWorkAll方法删除数据
	      workLogic.deleteWorkAll(dbConn,String.valueOf(person.getSeqId()));
	      //保存删除数据是否成功
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据失败");
	      throw e;
	    }
	    return "/core/inc/rtjson.jsp";
	  }
	  /*****
	   * 查询详细数据
	   * @param request
	   * @param response
	   * @return
	   * @throws Exception
	   */
	  public String updatePlu2(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    int seqId ;
	    try {
	      //实列实体层YHWorkPlan
	      YHWorkPlan plan = new YHWorkPlan();
	      //实列化业务层YHWorkLogic
	      YHWorkLogic workLogic = new YHWorkLogic();
	      //数据库的连接
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      //接受对应页面值

	      seqId = Integer.parseInt(request.getParameter("seqId"));
	      //调用selectId根据seqId查询对应数据
	      plan = workLogic.selectId(dbConn, seqId);
	      //将返回的数据保存到request中

	      request.setAttribute("plan", plan);
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "数据失败");
	      throw e;
	    }
	    return "/core/funcs/workplan/manage/updateWork.jsp";
	  }
	  /***
	   * 修改,删除附件数据
	   * @param request
	   * @param response
	   * @return
	   * @throws Exception 
	   */
	  public String updateWork3(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    int seqId ;
	    try {
	      //数据库的连接
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      //实列化业务层YHWorkLogic
	      YHWorkLogic workLogic = new YHWorkLogic();
	      //接受对应页面值

	      seqId = Integer.parseInt(request.getParameter("seqId"));
	      String name = request.getParameter("name");
	      String deptParentDesc = request.getParameter("deptParent");
	      String managerDesc = request.getParameter("manager");
	      String leader1Desc = request.getParameter("leader1");
	      String leader2Desc = request.getParameter("leader2");
	      String leader3Desc = request.getParameter("leader3");
	      String smsflag = request.getParameter("sms");
	      String smsflag2 = request.getParameter("smsflag");
	      String type = request.getParameter("WORK_TYPE");
	      String remark = request.getParameter("remark");
	      String attachmentcomment = request.getParameter("ATTACHMENT_COMMENT");
	      String content = request.getParameter("DIARY_CONTENT");
	      String attachmentid = request.getParameter("attachmentId");
	      String attachmentname = request.getParameter("attachmentName");
	      //      if (YHUtility.isNullorEmpty(attachmentname)) {
	      //        attachmentname = request.getParameter("attachmentName2");
	      //      }
	      //      if (YHUtility.isNullorEmpty(attachmentid)) {
	      //        attachmentid = request.getParameter("attachmentId2");
	      //      }

	      YHSelAttachUtil sel = new YHSelAttachUtil(request, YHWorkPlanCont.MODULE);
	      String attachNewId = sel.getAttachIdToString(",");
	      String attachNewName = sel.getAttachNameToString("*");
	      if(!"".equals(attachNewId) && !YHUtility.isNullorEmpty(attachmentid) && !attachmentid.trim().endsWith(",")){
	        attachmentid += ",";
	      }
	      attachmentid += attachNewId;
	      if(!"".equals(attachNewName) && !YHUtility.isNullorEmpty(attachmentname) && !attachmentname.trim().endsWith("*")){
	        attachmentname += "*";
	      }
	      attachmentname += attachNewName;

	      Date createdate = Date.valueOf(request.getParameter("CREATE_DATE"));
	      Date statrTime = Date.valueOf(request.getParameter("statrTime"));
	      Date endTime = Date.valueOf(request.getParameter("endTime"));
	      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
	      

	      //实列实体层YHWorkPlan
	      YHWorkPlan work = new YHWorkPlan();
	      //将接收的数据进行封装到实体中
	      work.setSeqId(seqId);
	      work.setStatrTime(statrTime);
	      work.setEndTime(endTime);
	      work.setCreatedate(createdate);
	      work.setAttachmentname(attachmentname);
	      work.setAttachmentid(attachmentid);
	      work.setContent(content);
	      work.setAttachmentcomment(attachmentcomment);
	      work.setRemark(remark);
	      work.setType(type);
	      work.setPublish("1");
	      work.setSmsflag(smsflag);
	      work.setName(name);
	      work.setDeptParentDesc(deptParentDesc);
	      work.setManagerDesc(managerDesc);
	      work.setLeader1Desc(leader1Desc);
	      work.setLeader2Desc(leader2Desc);
	      work.setLeader3Desc(leader3Desc);
	      work.setCreator(String.valueOf(person.getSeqId()));
	      //调用updateWork2方法传入work对象修改相应数据
	      workLogic.updateWork2(dbConn, work);
	      
	      String sqlName = "0,";
	      if (!YHUtility.isNullorEmpty(leader1Desc)) {
	        sqlName += leader1Desc + ",";
	      }
	      if (!YHUtility.isNullorEmpty(leader2Desc)) {
	        sqlName += leader2Desc + ",";
	      }
	      if (!YHUtility.isNullorEmpty(leader3Desc)) {
	        sqlName += leader3Desc + ",";
	      }
	      if (!YHUtility.isNullorEmpty(managerDesc)) {
	        sqlName += managerDesc + ",";
	      }
	      
	      if (smsflag2.equals("true") && !YHUtility.isNullorEmpty(smsflag2)) {
	        YHSmsBack sb = new YHSmsBack();
	        sb.setSmsType("12");
	        sb.setContent(person.getUserName() + "修改工作计划,请注意查看:" + name);
	        sb.setSendDate(new java.util.Date());
	        sb.setFromId(person.getSeqId());
	        sb.setToId(sqlName);
	        sb.setRemindUrl("/yh/core/funcs/workplan/act/YHPlanWorkAct/workSelect2.act?seqId=" + seqId + "&openFlag=1&openWidth=700&openHeight=650");
	        YHSmsUtil.smsBack(dbConn, sb);
	      }
	      
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功！");
	    }catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "数据失败");
	      throw e;
	    }
	    return "/core/inc/rtjson.jsp";
	  }  



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
	    String param = null;
	    try {
	      //数据库的连接
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      //实列化业务层YHWorkLogic
	      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
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
	      
	      param = "NAME=" + name + "&CONTENT=" + content + "&deptParent=" + deptParentDesc
	      + "&manager=" + managerDesc + "&leader1=" + leader1Desc + "&leader2=" + leader2Desc
	      + "&WORK_TYPE=" + type + "&REMARK=" + REMARK + "&leader3=" + leader3Desc;

	      if (!YHUtility.isNullorEmpty(statrTime)) {
	        plan.setStatrTime(Date.valueOf(statrTime));
	        param += "&statrTime=" + statrTime;
	      }
	      if (!YHUtility.isNullorEmpty(endTime)) {
	        plan.setEndTime(Date.valueOf(endTime));
	        param += "&endTime=" + endTime;
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
	      List<YHWorkPlan> worklist = workLogic.selectRes(dbConn, plan  ,    loginUser );
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
	      request.setAttribute("paramValue", param);
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
	      throw e;
	    }
	    return "/core/funcs/workplan/manage/query3.jsp";
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
	   * 修改状态数据

	   * @param request
	   * @param response
	   * @throws Exception 
	   */
	  public String updateQuery3(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    try {
	      //数据库的连接
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      //接受对应页面值

	      int seqId = Integer.parseInt(request.getParameter("seqId"));
	      String pub = request.getParameter("pub");
	      //实列化业务层
	      YHWorkLogic workLogic = new YHWorkLogic();
	      //调用业务层updatePub方法修改数据
	      workLogic.updatePub(dbConn, seqId, pub);
	      //保存修改数据是否成功
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
	      throw e;
	    }
	    return "/core/inc/rtjson.jsp";
	  }
	  /**
	   * 修改状态数据

	   * @param request
	   * @param response
	   * @throws Exception 
	   */
	  public String updateQuery(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    try {
	      //数据库的连接
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      //接受对应页面值

	      int seqId = Integer.parseInt(request.getParameter("seqId"));
	      String pub = request.getParameter("pub");
	      String time = request.getParameter("time");
	      Date endTime = null;
	      if (!YHUtility.isNullorEmpty(time)) {
	        endTime = Date.valueOf(time);
	      }
	      //实列化业务层
	      YHWorkLogic workLogic = new YHWorkLogic();
	      //调用业务层updatePub方法修改数据
	      workLogic.updatePub4(dbConn, seqId, pub,endTime);
	      //保存修改数据是否成功
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
	      throw e;
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
	  public String workSelect(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    try {
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
	      String type = request.getParameter("type");
	      String status = request.getParameter("status");
	      String seqId = String.valueOf(person.getSeqId());
	      YHWorkLogic gift = new YHWorkLogic();
	      String data = gift.workSelect(dbConn,request.getParameterMap(),type,status,seqId);
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
	      String data = gift.getName(dbConn,seqId);
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
	  /**
	   * 修改状态数据

	   * @param request
	   * @param response
	   * @throws Exception 
	   */
	  public String updateShengX(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    try {
	      //数据库的连接
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      //接受对应页面值

	      int seqId = Integer.parseInt(request.getParameter("seqId"));
	      String pub = request.getParameter("pub");
	      String time = request.getParameter("time");
	      Date endTime = null;
	      if (!YHUtility.isNullorEmpty(time)) {
	        endTime = Date.valueOf(time);
	      }
	      //实列化业务层
	      YHWorkLogic workLogic = new YHWorkLogic();
	      //调用业务层updatePub方法修改数据
	      workLogic.updatePub8(dbConn, seqId, pub,endTime);
	      //保存修改数据是否成功
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
	      throw e;
	    }
	    return "/core/inc/rtjson.jsp";
	  }
	}