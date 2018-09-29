package yh.core.funcs.workflow.act;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowRun;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.logic.YHConfigLogic;
import yh.core.funcs.workflow.logic.YHFeedbackLogic;
import yh.core.funcs.workflow.logic.YHFlowRunLogic;
import yh.core.funcs.workflow.logic.YHFlowTypeLogic;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.funcs.workflow.util.YHFlowUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.db.YHDBUtility;
import yh.subsys.oa.rollmanage.logic.YHRmsFileLogic;
import yh.oa.tools.StaticData;
/**
 * 描述：工作流归档转存
 * 
 * @author 张银友 zhangyinyou@126.com 2012-11-29 上午11:02:29
 * 
 */
public class YHFlowSaveFileAct {
	
	
	public String checkRoll(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
				YHConst.LOGIN_USER);
		String priv=loginUser.getUserPriv();
		String privo=loginUser.getUserPrivOther();
		if(!"".equals(privo)){
			if(privo.endsWith(","))
			{
				//对于数据库中出现类似以逗号结尾的数据如：“40,”，做如下适配
				privo = privo.substring(0,privo.length()-1);
			}
			priv=priv+","+privo;
		}
		Connection con = null;
		Statement stmt = null;
	    ResultSet rs = null;
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		try {
			con = requestDbConn.getSysDbConn();
	                YHConfigLogic logic=new YHConfigLogic();
			String flowAction = logic.getSysPar("FLOW_ACTION", con);
			if(flowAction==null){
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				return null;
			}
			stmt=con.createStatement();
			String sql="SELECT FUNC_ID_STR  FROM USER_PRIV WHERE seq_id IN("+priv+")";
			System.out.println(sql);
			rs=stmt.executeQuery(sql);
			List<String> list=new ArrayList<String>();
			while(rs.next()){
				list.add(rs.getString(1));
			}
			String pris=StringUtils.join(list, ",");//用户所拥有所有的功能ID
			String arr[] = pris.split(",");
			list=Arrays.asList(arr);
	                StringBuffer sb=new StringBuffer();

			if(list.contains(StaticData.ROLLFILE) && flowAction.contains("4")){
				 sb.append("4");
				 
			}
			if(list.contains(StaticData.NOTIFY) && flowAction.contains("1")){	
				 sb.append("1"); 
			}
			if(flowAction.contains("2")){	
				 sb.append("2"); 
			}
			if(flowAction.contains("3")){	
				 sb.append("3"); 
			}
			if(!sb.toString().equals("")){
				 request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
				 request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
			}
			else{
				 request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			}


		} catch (SQLException e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}finally {
		      YHDBUtility.close(stmt, rs, null); 
	        }
		 return "/core/inc/rtjson.jsp";
	}
		public String checkFlowSysSet(HttpServletRequest request,
			HttpServletResponse response){
		try {
	    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
	      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		YHConfigLogic logic=new YHConfigLogic();
		Connection dbConn = null;
		dbConn = requestDbConn.getSysDbConn();
		String flowAction = logic.getSysPar("FLOW_ACTION", dbConn);
		 return flowAction;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
 public String getRunName(HttpServletRequest request){
    	String runId= request.getParameter("runId");
    	if(null==runId || "".equals(runId))
    		return null;
    	YHFlowRunLogic frl = new YHFlowRunLogic();
    	Connection dbConn = null;
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		try {
			dbConn = requestDbConn.getSysDbConn();
	    	YHFlowRun flowRun = frl.getFlowRunByRunId(Integer.parseInt(runId), dbConn);
	    	//System.out.println(flowRun.getRunName());
	    	return flowRun.getRunName();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}

    	return null;

    }
	public String toSaveFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int runId = Integer.parseInt(request.getParameter("runId"));
		int prcsId = Integer.parseInt(request.getParameter("prcsId"));
		int flowId = Integer.parseInt(request.getParameter("flowId"));
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
				YHConst.LOGIN_USER);
		String imgPath = YHWorkFlowUtility.getImgPath(request);
		YHFeedbackLogic feedbackLogic = new YHFeedbackLogic();
		YHFlowUtil util = new YHFlowUtil();
		Connection dbConn = null;
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHFlowRunLogic frl = new YHFlowRunLogic();
			YHFlowRun flowRun = frl.getFlowRunByRunId(runId, dbConn);
			YHFlowTypeLogic ftl = new YHFlowTypeLogic();
			YHFlowType ft = ftl.getFlowTypeById(flowId, dbConn);
			String runName = flowRun.getRunName();
			// 获取form表单内容
			Map result = frl.getPrintForm(loginUser, flowRun, ft, true, dbConn,
					imgPath);
			String form = (String) result.get("form");
			form = form.replaceAll("\\\\\"", "\"");
			StringBuilder sb = new StringBuilder();
			sb.append("<div id=\"form\" style=\"margin-top:5px;margic-bottom:5px\">");
			sb.append(form).append("</div>");
			// 获取会签内容
			String feedbacks = feedbackLogic.getFeedbacksHtml(loginUser,
					flowRun.getFlowId(), flowRun.getRunId(), dbConn);
			sb.append(feedbacks);

			List<String> zipfiles = new ArrayList<String>();
			// 将表单和会签的内容生成html文件
			String formHtmlPath = util.writeHtmlFile(sb.toString(), runName);
			zipfiles.add(formHtmlPath);

			// 获取流程附件

			YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
			// 验证是否有权限,并取出权限字符串
			String roleStr = roleUtility.runRole(runId, flowId, prcsId,
					loginUser, dbConn);
			if ("".equals(roleStr)) {// 没有权限
			} else {
				String attIds = flowRun.getAttachmentId();
				String attNames = flowRun.getAttachmentName();
				if ("".equals(attIds) || attIds == null) {

				} else {// 有附件的情况下 把附件打包
					String[] ids = attIds.substring(0, attIds.length() - 1)
							.split(",");
					String[] names = attNames.substring(0,
							attNames.length() - 1).split("\\*");
					for (int i = 0; i < ids.length; i++) {
						String floder = ids[i].substring(0, 4);
						String fileName = ids[i].substring(5) + "_" + names[i];
						String attPath = YHSysProps.getAttachPath()
								+ File.separator + "workflow" + File.separator
								+ floder + File.separator + fileName;
						System.out.println(attPath);
						zipfiles.add(attPath);
					}
				}
			}
			Map<String, String> map = util.zip(zipfiles, runName);
			return "/core/funcs/savefile/index.jsp?attachId="
					+ map.get("attid") + "&attachName=" + map.get("attname")
					+ "&module=workflow";

		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

	}
	
	
	public String toSaveRoll(HttpServletRequest request,HttpServletResponse response) throws Exception{
		int runId = Integer.parseInt(request.getParameter("runId"));
		int prcsId = Integer.parseInt(request.getParameter("prcsId"));
		int flowId = Integer.parseInt(request.getParameter("flowId"));
		Connection dbConn;
		try{
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			
			YHFlowRunLogic frl = new YHFlowRunLogic();
			YHFeedbackLogic feedbackLogic = new YHFeedbackLogic();
			YHFlowRun flowRun = frl.getFlowRunByRunId(runId, dbConn);
			YHFlowTypeLogic ftl = new YHFlowTypeLogic();
			YHFlowType ft = ftl.getFlowTypeById(flowId, dbConn);
			String runName = flowRun.getRunName();
			// 获取form表单内容
			String imgPath = YHWorkFlowUtility.getImgPath(request);
			Map result = frl.getPrintForm(person, flowRun, ft, true, dbConn,
					imgPath);
			String form = (String) result.get("form");
			form = form.replaceAll("\\\\\"", "\"");
			StringBuilder sb = new StringBuilder();
			sb.append("<div id=\"form\" style=\"margin-top:5px;margic-bottom:5px\">");
			sb.append(form).append("</div>");
			// 获取会签内容
			String feedbacks = feedbackLogic.getFeedbacksHtml(person,
					flowRun.getFlowId(), flowRun.getRunId(), dbConn);
			sb.append(feedbacks);
			// 将表单和会签的内容生成html文件
			YHFlowUtil util=new YHFlowUtil();
			Map<String,Object>  attMap= util.writeHtmlFile(sb.toString(), runName,"workflow");
			request.setAttribute("attMap", attMap);
			//add by jzk,判断是否已经归档过----start
			YHRmsFileLogic yhrmsfilelogic = new YHRmsFileLogic();
			int count = yhrmsfilelogic.getRmsNumByRunId(dbConn, runId);
			request.setAttribute("FlowRMSFileNum", count+"");
			//add by jzk,判断是否已经归档过----end
		}catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/subsys/oa/rollmanage/rollfile/newFileFromFlow.jsp";
	}
}
