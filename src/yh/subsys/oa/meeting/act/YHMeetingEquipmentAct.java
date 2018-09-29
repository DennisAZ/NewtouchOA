package yh.subsys.oa.meeting.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.address.data.YHAddress;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.meeting.data.YHMeetingEquipment;
import yh.subsys.oa.meeting.logic.YHMeetingEquipmentLogic;

public class YHMeetingEquipmentAct {

	private YHMeetingEquipmentLogic logic = new YHMeetingEquipmentLogic();
	
	/**
	 * 设备管理列表--cc
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getMeetingEquipmentList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;

    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String cycleNo = request.getParameter("cycleNo");
      String flag = request.getParameter("flag");
      String data = this.logic.getMeetingEquipmentList(dbConn, request.getParameterMap(), cycleNo);
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
   * 获取小编码表内容--同类设备名称--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCodeName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String classCode = request.getParameter("classCode");
      String data = this.logic.getCodeNameLogic(dbConn, classCode);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 删除单个设备管理--cc
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteSingle(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.deleteSingle(dbConn, Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 会议查询 -cc
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSearchMeetingEquipment(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    ArrayList<YHAddress> addressList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String equipmentNo = YHDBUtility.escapeLike(request.getParameter("equipmentNo"));
      String equipmentName = YHDBUtility.escapeLike(request.getParameter("equipmentName"));
      String equipmentStatus = YHDBUtility.escapeLike(request.getParameter("equipmentStatus"));
      String mrId = YHDBUtility.escapeLike(request.getParameter("mrId"));
      String remark = YHDBUtility.escapeLike(request.getParameter("remark"));
      String data = "";
      data = this.logic.getMeetingEquiomentSearchJson(dbConn, request.getParameterMap(), equipmentNo, equipmentName, equipmentStatus, mrId, remark);
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
	 * 获取所属会议室
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getMRoomName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			String data = this.logic.getMRoomNameLogic(dbConn);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 添加设备信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addEquipment(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			Map<String, String[]> map = request.getParameterMap();
			YHMeetingEquipment equipment = (YHMeetingEquipment) YHFOM.build(map, YHMeetingEquipment.class, "");

			this.logic.addEquipmentLogic(dbConn, equipment);

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}
	
	
	/**
	 * 根据会议室seqId获取设备信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getEquipmentById(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String mRoomStr = request.getParameter("mRoom");
		int mRoom=0;
		if (!YHUtility.isNullorEmpty(mRoomStr)) {
			mRoom=Integer.parseInt(mRoomStr);
		}
		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data=this.logic.getEquipmentByIdLogic(dbConn, mRoom);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
		
	/**
	 * 获取下拉列表值	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSelectOption(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String parentNo = request.getParameter("parentNo");
		String optionType = request.getParameter("optionType");
		if (YHUtility.isNullorEmpty(parentNo)) {
			parentNo = "";
		}
		if (YHUtility.isNullorEmpty(optionType)) {
			optionType = "";
		}
		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getSelectOption(dbConn, parentNo);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	
	/**
	 * 根据seqId获取设备信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getEquipmentInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHMeetingEquipment equipment=this.logic.getEquipmentLogic(dbConn, seqId);
			if (equipment == null) {
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				request.setAttribute(YHActionKeys.RET_MSRG, "会议设备信息不存在");
				return "/core/inc/rtjson.jsp";
			}
			StringBuffer data = YHFOM.toJson(equipment);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
			request.setAttribute(YHActionKeys.RET_DATA, data.toString());
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	
	/**
	 * 更新设备信息--wyw
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateEquipmentById(HttpServletRequest request,HttpServletResponse response) throws Exception{
			Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			Map<String, String[]> map = request.getParameterMap();
			YHMeetingEquipment equipment = (YHMeetingEquipment) YHFOM.build(map, YHMeetingEquipment.class, "");
			this.logic.updateEquipmentLogic(dbConn,equipment);
			
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
		}catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
}
