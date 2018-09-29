package yh.core.funcs.workplan.act;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.funcs.workplan.data.YHWorkDetail;
import yh.core.funcs.workplan.data.YHWorkPlan;
import yh.core.funcs.workplan.data.YHWorkPlanCont;
import yh.core.funcs.workplan.logic.YHWorkDetailLogic;
import yh.core.funcs.workplan.logic.YHWorkLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
public class YHWorkDetailAct {
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public String selectDetai(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      YHWorkLogic logic = new YHWorkLogic();
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHWorkPlan plan = logic.selectId(dbConn, seqId);
      List<YHWorkDetail> list = detailLogic.selectDetail(dbConn, seqId);
      request.setAttribute("list", list);
      request.setAttribute("plan", plan);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/core/funcs/workplan/manage/add_opinion.jsp";
  }
  
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public String detai(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      YHWorkLogic logic = new YHWorkLogic();
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHWorkPlan plan = logic.selectId(dbConn, seqId);
      List<YHWorkDetail> list = detailLogic.selectDetail(dbConn, seqId);
      request.setAttribute("list", list);
      request.setAttribute("plan", plan);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/core/funcs/workplan/manage/opinion.jsp";
  }
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public String selectDetai2(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    int seqId;
    try {
      //数据库的连接
    //实列化业务层
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkLogic logic = new YHWorkLogic();
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      //使用request.getParameter接受页面的值
      seqId = Integer.parseInt(request.getParameter("seqId"));
      YHWorkPlan plan = logic.selectId(dbConn, seqId);
      List<YHWorkDetail> list = detailLogic.selectDetail(dbConn, seqId);
      List<YHWorkDetail> list2 = detailLogic.selectDetailId(dbConn, seqId);
      int sun = detailLogic.sunNum(dbConn, person.getSeqId(),seqId);
      int sun2 = detailLogic.maxSunNum(dbConn,seqId);
      request.setAttribute("list", list);
      request.setAttribute("list2", list2);
      request.setAttribute("plan", plan);
      request.setAttribute("sunWork", sun);
      request.setAttribute("sunWork2", sun2);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/core/funcs/workplan/manage/progress_map.jsp?seqid=" + seqId;
  }

  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public String selectDetaiId2(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHWorkDetail workDetail = detailLogic.selectId(dbConn, seqId);
      //定义数组将数据保存到Json中
      String data = "[";
      if(workDetail != null) {
        data = data + YHFOM.toJson(workDetail);
        //data = data.substring(0, data.length()-1);
        data = data.replaceAll("\\n", "");
        data = data.replaceAll("\\r", "");
        data = data + "]";
        //System.out.println(data);
      }
      //保存查询数据是否成功，保存date
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/core/inc/rtjson.jsp";
  }
  /***
   * 根据ID增加数据
   * @return
   * @throws Exception 
   */
  public String addDetai(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    String planId = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      YHWorkDetail detail = new YHWorkDetail();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //使用request.getParameter接受页面的值
      planId = request.getParameter("PLAN_ID");
      String smsflag = request.getParameter("smsflag");
      String progress = request.getParameter("PROGRESS");
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      String time =  request.getParameter("WRITE_TIME").substring(0,10);
      String leader1 = request.getParameter("leader1");
      String smsflag3 = request.getParameter("sms2flag");
      String sqlName = "0,";
      if (!leader1.trim().equals("null")) {
        sqlName = leader1 + ",";
      }
      Date witeTime = Date.valueOf(time);
      if (smsflag.equals("true")) {
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("12");
        sb.setContent("有新的领导批注增加，请查看: " + progress);
        sb.setSendDate(new java.util.Date());
        sb.setFromId(person.getSeqId());
        sb.setToId(sqlName);
        sb.setRemindUrl("/yh/core/funcs/workplan/act/YHWorkDetailAct/detai.act?seqId=" + planId + "&openFlag=1&openWidth=800&openHeight=550");
        YHSmsUtil.smsBack(dbConn, sb);
      }
      if (smsflag3.equals("true")) {
        YHMobileSms2Logic sb = new YHMobileSms2Logic();
        sb.remindByMobileSms(dbConn,sqlName,person.getSeqId(),"有新的领导批注增加，请查看: " + progress,new java.util.Date());
      }
      
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
      
      detail.setAttachmentId(attachmentId);
      detail.setPlanId(planId);
      detail.setWriter(String.valueOf(person.getSeqId()));
      detail.setProgress(progress);
      detail.setAttachmentName(attachmentName);
      detail.setWriteTime(witeTime);
      detail.setTypeFlag("1");
      detail.setPercent(0);
      detailLogic.addDetail(dbConn,detail);

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
  public String deleteDetai(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    String planId = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      planId = request.getParameter("planId");
      detailLogic.deteleDetail(dbConn,seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据失败");
      throw e;
    } finally {
    }
    return "/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetai.act?seqId=" + planId;
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
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHWorkDetail detail = detailLogic.selectId(dbConn,seqId);
      request.setAttribute("detailId",detail);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/core/funcs/workplan/manage/add_opinion3.jsp";
  }
  /***
   * 根据ID修改数据
   * @return
   * @throws Exception 
   */
  public String updateDetail(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    String planId;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      YHWorkDetail detail = new YHWorkDetail();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
//      String writer = request.getParameter("writer");
      String progress = request.getParameter("PROGRESS");
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      String time =  request.getParameter("WRITE_TIME").substring(0,10);
      Date witeTime = Date.valueOf(time);
      planId = request.getParameter("planId");
      
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

      detail.setAttachmentId(attachmentId);
      detail.setWriter(String.valueOf(person.getSeqId()));
      detail.setProgress(progress);
      detail.setAttachmentName(attachmentName);
      detail.setWriteTime(witeTime);
      detail.setSeqId(seqId);
      detailLogic.updateDetail(dbConn, detail);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetai.act?seqId=" + planId;
  }
  
  
  /***
   * 根据ID修改,删除附件数据
   * @return
   * @throws Exception 
   */
  public String updateDetail2(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    String planId;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      YHWorkDetail detail = new YHWorkDetail();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
//      String writer = request.getParameter("writer");
      String progress = request.getParameter("PROGRESS");
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      String time =  request.getParameter("WRITE_TIME").substring(0,10);
      Date witeTime = Date.valueOf(time);
      planId = request.getParameter("planId");
      
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

      detail.setAttachmentId(attachmentId);
      detail.setWriter(String.valueOf(person.getSeqId()));
      detail.setProgress(progress);
      detail.setAttachmentName(attachmentName);
      detail.setWriteTime(witeTime);
      detail.setSeqId(seqId);
      detailLogic.updateDetail(dbConn, detail);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/core/inc/rtjson.jsp";
  }

  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public String selectDetaiId(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      YHWorkLogic logic = new YHWorkLogic();
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHWorkPlan plan = logic.selectId(dbConn, seqId);
      List<YHWorkDetail> list = detailLogic.selectDetailId(dbConn, seqId);
      request.setAttribute("listId", list);
      request.setAttribute("plan", plan);
      int sun = detailLogic.sunNum2(dbConn, person.getSeqId(),seqId);
      request.setAttribute("sunWork", sun);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/core/funcs/workplan/show/add_diary.jsp";
  }
  
  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public String DetaiId(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      YHWorkLogic logic = new YHWorkLogic();
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));

      YHWorkPlan plan = logic.selectId(dbConn, seqId);
      List<YHWorkDetail> list = detailLogic.selectDetailId(dbConn, seqId);
      request.setAttribute("listId", list);
      request.setAttribute("plan", plan);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/core/funcs/workplan/show/diary.jsp";
  }
  /***
   * 根据ID增加数据
   * @return
   * @throws Exception 
   */
  public String addDetaiId(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    String planId = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      YHWorkDetail detail = new YHWorkDetail();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //使用request.getParameter接受页面的值
      String smsflag = request.getParameter("smsflag");
      planId = request.getParameter("PLAN_ID");
      int percent = Integer.parseInt(request.getParameter("PERCENT"));
//      String writer = request.getParameter("writer");
      String progress = request.getParameter("PROGRESS");
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      String smsflag3 = request.getParameter("sms2flag");
      String time =  request.getParameter("WRITE_TIME").substring(0,10);
      Date witeTime = Date.valueOf(time);
      if (YHUtility.isNullorEmpty(smsflag)) {
        smsflag = "false";
      } 
      
      String num = request.getParameter("chi");
      if (num.equals("true")) {
//        InetAddress inet = InetAddress.getLocalHost(); 
//        String ip = inet.getHostAddress(); 
//        yh.core.funcs.system.syslog.logic.YHSysLogLogic.addSysLog(dbConn,"1","添加进度日志",person.getSeqId(),ip);
        YHDiaryUtil diary = new YHDiaryUtil();
        diary.writerDiary(dbConn,person.getSeqId(), "工作计划进度", progress);
//        YHDiary diaryData = diary.getDiary(dbConn,Integer.parseInt(planId));
//        if (diaryData == null) {
//        }
//        else {
//          diary.updateDiary(dbConn,diaryData.getSeqId(),person.getSeqId(), "工作计划进度", diaryData.getContent() + "<br>" + progress + "(" + witeTime +")");
//        }
      }
      
      if (smsflag.equals("true")) {
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("12");
        sb.setContent("有新的工作计划进度日志，请查看!");
        sb.setSendDate(new java.util.Date());
        sb.setFromId(person.getSeqId());
        sb.setToId(String.valueOf(request.getParameter("sqlName")));
        sb.setRemindUrl("/yh/core/funcs/workplan/act/YHWorkDetailAct/DetaiId.act?seqId=" + planId + "&openFlag=1&openWidth=800&openHeight=550");
        YHSmsUtil.smsBack(dbConn, sb);
      }
      if (smsflag3.equals("true")) {
        YHMobileSms2Logic sb = new YHMobileSms2Logic();
        sb.remindByMobileSms(dbConn,String.valueOf(request.getParameter("sqlName")),person.getSeqId(),"有新的工作计划进度日志，请查看!",new java.util.Date());
      }
      
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
      
      detail.setPercent(percent);
      detail.setAttachmentId(attachmentId);
      detail.setPlanId(planId);
      detail.setWriter(String.valueOf(person.getSeqId()));
      detail.setProgress(progress);
      detail.setAttachmentName(attachmentName);
      detail.setWriteTime(witeTime);
      detail.setTypeFlag("0");
      detailLogic.addDetail(dbConn,detail);

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
  public String deleteDetaiId(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    String planId = null;
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      planId = request.getParameter("planId");
      detailLogic.deteleDetail(dbConn,seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据失败");
      throw e;
    } finally {
    }
    return "/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetaiId.act?seqId=" + planId;
  }

  /***
   * 根据ID修改数据
   * @return
   * @throws Exception 
   */
  public String updateDetaiId(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    String planId;
    String sqlName = request.getParameter("sqlName");
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      YHWorkDetail detail = new YHWorkDetail();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));  
//      String writer = request.getParameter("writer");
      String progress = request.getParameter("PROGRESS");
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      String time =  request.getParameter("WRITE_TIME").substring(0,10);
      Date witeTime = Date.valueOf(time);
      planId = request.getParameter("planId");
      String smsflag3 = request.getParameter("sms2flag");
      int percent = Integer.parseInt(request.getParameter("PERCENT"));
      //    int percent2 = Integer.parseInt(request.getParameter("PERCENT2"));
      String smsflag = request.getParameter("smsflag");
      
//      if (YHUtility.isNullorEmpty(attachmentName)) {
//        attachmentName = request.getParameter("attachmentName2");
//      }
      
      String num = request.getParameter("chi");
      if (num.trim().equals("true")) {
//        InetAddress inet = InetAddress.getLocalHost(); 
//        String ip = inet.getHostAddress(); 
//        yh.core.funcs.system.syslog.logic.YHSysLogLogic.addSysLog(dbConn,"1","修改进度日志",person.getSeqId(),ip);
        YHDiaryUtil diary = new YHDiaryUtil();
        diary.writerDiary(dbConn,person.getSeqId(), "工作计划进度", progress);
      }
      
      if (smsflag.trim().equals("true")) {
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("12");
        sb.setContent("修改工作计划进度日志，请查看!");
        sb.setSendDate(new java.util.Date());
        sb.setFromId(person.getSeqId());
        sb.setToId(sqlName);
        sb.setRemindUrl("/yh/core/funcs/workplan/act/YHWorkDetailAct/DetaiId.act?seqId=" + planId + "&openFlag=1&openWidth=800&openHeight=550");
        YHSmsUtil.smsBack(dbConn, sb);
      }
      if (smsflag3.equals("true")) {
        YHMobileSms2Logic sb = new YHMobileSms2Logic();
        sb.remindByMobileSms(dbConn,sqlName,person.getSeqId(),"修改工作计划进度日志，请查看!",new java.util.Date());
      }
      
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
      
      
      detail.setPercent(percent);
      detail.setAttachmentId(attachmentId);
      detail.setWriter(String.valueOf(person.getSeqId()));
      detail.setProgress(progress);
      detail.setAttachmentName(attachmentName);
      detail.setWriteTime(witeTime);
      detail.setSeqId(seqId);
      detailLogic.updateDetailId(dbConn, detail);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    } finally {
    }
    return "/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetaiId.act?seqId=" + planId;
  }
  
  
  /***
   * 根据ID修改,附件删除数据
   * @return
   * @throws Exception 
   */
  public String updateDetaiId2(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    String planId;
    String sqlName = request.getParameter("sqlName");
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      YHWorkDetail detail = new YHWorkDetail();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));  
//      String writer = request.getParameter("writer");
      String progress = request.getParameter("PROGRESS");
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      String time =  request.getParameter("WRITE_TIME").substring(0,10);
      Date witeTime = Date.valueOf(time);
      planId = request.getParameter("planId");
      int percent = Integer.parseInt(request.getParameter("PERCENT"));
      //    int percent2 = Integer.parseInt(request.getParameter("PERCENT2"));
      String smsflag = request.getParameter("smsflag");
      
//      if (YHUtility.isNullorEmpty(attachmentName)) {
//        attachmentName = request.getParameter("attachmentName2");
//      }
      
      String num = request.getParameter("chi");
      if (num.trim().equals("true")) {
//        InetAddress inet = InetAddress.getLocalHost(); 
//        String ip = inet.getHostAddress(); 
//        yh.core.funcs.system.syslog.logic.YHSysLogLogic.addSysLog(dbConn,"1","修改进度日志",person.getSeqId(),ip);
        YHDiaryUtil diary = new YHDiaryUtil();
        diary.writerDiary(dbConn,person.getSeqId(), "工作计划进度", progress);
      }
      
      if (smsflag.trim().equals("true")) {
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("12");
        sb.setContent("修改工作计划进度日志，请查看!");
        sb.setSendDate(new java.util.Date());
        sb.setFromId(person.getSeqId());
        sb.setToId(sqlName);
        sb.setRemindUrl("/yh/core/funcs/workplan/act/YHWorkDetailAct/DetaiId.act?seqId=" + planId + "&openFlag=1&openWidth=800&openHeight=550");
        YHSmsUtil.smsBack(dbConn, sb);
      }
      
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
      
      
      detail.setPercent(percent);
      detail.setAttachmentId(attachmentId);
      detail.setWriter(String.valueOf(person.getSeqId()));
      detail.setProgress(progress);
      detail.setAttachmentName(attachmentName);
      detail.setWriteTime(witeTime);
      detail.setSeqId(seqId);
      detailLogic.updateDetailId(dbConn, detail);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    } finally {
    }
    return "/core/inc/rtjson.jsp";
  }


  /***
   * 根据ID查询数据
   * @return
   * @throws Exception 
   */
  public String selectId2(HttpServletRequest request,
      HttpServletResponse response) throws  Exception {
    Connection dbConn = null;
    String sqlName = request.getParameter("sqlName");
    try {
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //实列化业务层
      YHWorkDetailLogic detailLogic = new YHWorkDetailLogic();
      //使用request.getParameter接受页面的值
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHWorkDetail detail = detailLogic.selectId(dbConn,seqId);
      request.setAttribute("detailId",detail);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
      //orm.saveSingle(dbConn, YHWork_Plan.class);//保存数据
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    } finally {
    }
    return "/core/funcs/workplan/show/edit_diary.jsp?sqlName=" + sqlName;
  }
}
