package yh.subsys.oa.rollmanage.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.attendance.logic.YHSysParaLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.rollmanage.data.YHRmsLend;
import yh.subsys.oa.rollmanage.logic.YHRmsLendLogic;

public class YHRmsLendAct {
	private YHRmsLendLogic logic = new YHRmsLendLogic();

	/**
	 * 借阅查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryLendFileJosn(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("roomName", request.getParameter("roomName"));
		map.put("rollName", request.getParameter("rollName"));
		map.put("fileCode", request.getParameter("fileCode"));
		map.put("fileSubject", request.getParameter("fileSubject"));
		map.put("fileTitle", request.getParameter("fileTitle"));
		map.put("fileTitleo", request.getParameter("fileTitleo"));
		map.put("sendUnit", request.getParameter("sendUnit"));
		map.put("remark", request.getParameter("remark"));
		map.put("sendTimeMin", request.getParameter("sendTimeMin"));
    map.put("sendTimeMax", request.getParameter("sendTimeMax"));
    map.put("fileWord", request.getParameter("fileWord"));
    map.put("fileYear", request.getParameter("fileYear"));
    map.put("issueNum", request.getParameter("issueNum"));
    
		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

			String data = this.logic.queryLendFileLogic(dbConn, request.getParameterMap(), person, map);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return null;
	}

	/**
	 * 借阅
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String rmsLendRoll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			int rollId = Integer.parseInt(request.getParameter("rollId"));
			int fileId = Integer.parseInt(request.getParameter("fileId"));
			String manage = this.logic.getRmsLendManage(dbConn, rollId);
			String type = "37";  //37为档案管理
			Map m = new HashMap();
			m.put("fileId", fileId);
			m.put("userId", String.valueOf(person.getSeqId()));
			m.put("addTime", YHUtility.getCurDateTimeStr());
//			System.out.println(YHUtility.getCurDateTimeStr());
			m.put("approve", manage);
			m.put("allow", "0");
			YHORM orm = new YHORM();
      orm.saveSingle(dbConn, "rmsLend", m);
			YHSysParaLogic yhpl = new YHSysParaLogic();
			String sysRemind = yhpl.selectPara(dbConn, "SMS_REMIND");
			String allowRemind = "2";// 1允许短信提醒2为不允许
      String defaultRemind = "2";// 1为默认提醒2为不默认
      String mobileRemind = "2";// 1为默认提醒2为不默认
      if (sysRemind != null) {
        String[] sysRemindArray = sysRemind.split("\\|");
        if (sysRemindArray.length == 1) {
          String temp = sysRemindArray[0];
          String[] tempArray = temp.split(",");
          for (int i = 0; i < tempArray.length; i++) {
            if (tempArray[i].equals(type)) {
              defaultRemind = "1";
              break;
            }
          }

        }
        if (sysRemindArray.length == 2) {
          String temp1 = sysRemindArray[0];
          String[] tempArray1 = temp1.split(",");
          for (int i = 0; i < tempArray1.length; i++) {
            if (tempArray1[i].equals(type)) {
              defaultRemind = "1";
              break;
            }
          }
          String temp2 = sysRemindArray[1];
          String[] tempArray2 = temp2.split(",");
          for (int i = 0; i < tempArray2.length; i++) {
            if (tempArray2[i].equals(type)) {
              mobileRemind = "1";
              break;
            }
          }
        }
        if (sysRemindArray.length == 3) {
          String temp1 = sysRemindArray[0];
          String[] tempArray1 = temp1.split(",");
          for (int i = 0; i < tempArray1.length; i++) {
            if (tempArray1[i].equals(type)) {
              defaultRemind = "1";
              break;
            }
          }
          String temp2 = sysRemindArray[2];
          String[] tempArray2 = temp2.split(",");
          for (int i = 0; i < tempArray2.length; i++) {
            if (tempArray2[i].equals(type)) {
              allowRemind = "1";
              break;
            }
          }
          String temp3 = sysRemindArray[1];
          String[] tempArray3 = temp3.split(",");
          for (int i = 0; i < tempArray3.length; i++) {
            if (tempArray3[i].equals(type)) {
              mobileRemind = "1";
              break;
            }
          }
        }
      }
			if(("1".equals(allowRemind) || "1".equals(defaultRemind))&& !YHUtility.isNullorEmpty(manage)){
			  String content = "用户向您借阅档案，请审批";
			  String remindUrl = "/subsys/oa/rollmanage/rolllend/confirmManage.jsp";
			  this.logic.doSmsBack(dbConn, content, person.getSeqId(), manage, type, remindUrl);
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "借阅成功");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	

	/**
	 * 批量借阅
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String rmsLendAllRoll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String rollIdStr = request.getParameter("rollIdStr");
			String[] rollStr = rollIdStr.split(",");
			Map m = new HashMap();
			YHORM orm = new YHORM();
			for (int i = 0; i < rollStr.length; i++) {
				String rollId = rollStr[i];
				m.put("fileId", rollId);
				m.put("userId", String.valueOf(person.getSeqId()));
				m.put("addTime", YHUtility.getCurDateTimeStr());
				m.put("allow", "0");
				orm.saveSingle(dbConn, "rmsLend", m);
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "借阅成功");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	
/**
 * 借阅记录通用方法
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
	public String getApprovalToBorrow(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String allow = request.getParameter("allow");
      if(YHUtility.isNullorEmpty(allow)){
        allow = "0";
      }
      String data = this.logic.getApprovalToBorrow(dbConn, person, allow).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
	
	/**
	 * 借阅审批通用方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	 public String getApprovaledLend(HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
	    Connection dbConn = null;
	    String userName = "";
	    try {
	      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
	      String allow = request.getParameter("allow");
	      String data = this.logic.getApprovaledLend(dbConn, person, allow).toString();
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
	      request.setAttribute(YHActionKeys.RET_DATA, data);
	    }catch(Exception ex) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
	      throw ex;
	    }
	    return "/core/inc/rtjson.jsp";
	  }
	 
 /**
  * 撤销
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
  public String revocationLend(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //int seqId = person.getSeqId();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHORM t = new YHORM();
      t.deleteSingle(dbConn,YHRmsLend.class, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
	  
  /**
   * 归还
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String returnLend(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqIdUser = person.getSeqId();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String allow = request.getParameter("allow");
      
      Map m =new HashMap();
      m.put("seqId", seqId);
      m.put("allow", allow);
      if("3".equals(allow)){
        m.put("returnTime", YHUtility.getCurDateTimeStr());
      }else{
        m.put("allowTime", YHUtility.getCurDateTimeStr());
      }
      YHORM t = new YHORM();
      t.updateSingle(dbConn, "oaArchivesLend", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"添加成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
