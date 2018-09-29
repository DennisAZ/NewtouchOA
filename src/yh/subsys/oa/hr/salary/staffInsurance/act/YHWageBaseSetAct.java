package yh.subsys.oa.hr.salary.staffInsurance.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHHrInsurancePara;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHSalItem;
import yh.subsys.oa.hr.salary.staffInsurance.logic.YHWageBaseSetLogic;
import yh.subsys.oa.hr.salary.submit.data.YHSalPerson;

public class YHWageBaseSetAct {
	YHWageBaseSetLogic wageBase = new YHWageBaseSetLogic();
	/**
	 * 获取本部门共有多少人
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String wageBaseSetAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
	   
		String seqIdStr = request.getParameter("deptId");
		int seqId = 0;
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = "";
			if (!YHUtility.isNullorEmpty(seqIdStr)) {
				seqId = Integer.parseInt(seqIdStr);
				data = wageBase.getWageBaseSetLogic(dbConn, seqId);
			} 
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回数据");
			request.setAttribute(YHActionKeys.RET_DATA, "{data:"+ data+"}");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	/**
	 *求部门id属于哪个部门的
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String whereDeptIdAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String seqIdStr = request.getParameter("deptId");
			int seqId = 0;
			Connection dbConn = null;
			try {
				YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
				dbConn = requestDbConn.getSysDbConn();
				String data = "";
				if (!YHUtility.isNullorEmpty(seqIdStr)) {
					seqId = Integer.parseInt(seqIdStr);
					data = wageBase.getWhereDeptIdLogic(dbConn, seqId);
				} 
				//System.out.println(data);
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
				request.setAttribute(YHActionKeys.RET_MSRG, "成功返回数据");
				request.setAttribute(YHActionKeys.RET_DATA, "{data:'"+ data +"'}");
			} catch (Exception ex) {
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
				throw ex;
			}
			return "/core/inc/rtjson.jsp";
		}
	public String getSalItemIdAct(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;

		String yesOtherId ="0";
		List<YHSalPerson> listPerson = null;
		String deptId = request.getParameter("deptId");
		List<YHSalItem> listSalItem = new ArrayList<YHSalItem>();
		List<Map> listSalItemData = new ArrayList<Map>();
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
		  String slaItemId = wageBase.getSalItemIdLogic(dbConn, user);
		  String[] slaItemIds = slaItemId.split(",");
		  int ArrayNum = slaItemIds.length;
		  if( slaItemIds[ArrayNum-1]==""){
			  ArrayNum --;
		  }
		  if(ArrayNum!=0 && ArrayNum > 0){//定义的薪酬项目
		        for(int i = 0 ;i < ArrayNum ; i++){
		        	YHSalItem salItem = wageBase.getSalItemIdAndNameLogic(dbConn, user, Integer.valueOf(slaItemIds[i]));
		        	listSalItem.add(salItem);
		        }
		  }
		   yesOtherId = wageBase.getYesOtherLogic(dbConn, user);
		   if(!YHUtility.isNullorEmpty(yesOtherId)){
			   request.setAttribute("yesOtherId", Integer.valueOf(yesOtherId));//查询HR_INSURANCE_PARA中的yesOther 是否为1 为1显示保险系数
		   } else {
		     request.setAttribute("yesOtherId", 0);
		   }
		  List<YHHrInsurancePara> insurancePara = wageBase.getYesOtherLogic1(dbConn, user);
		   if(!YHUtility.isNullorEmpty(deptId)){ //获取部门下的所有人
			   listPerson = wageBase.getDeptPersonNameLogic1(dbConn, user, deptId);
		   }
		  /* if(listPerson!=null && listPerson.size()>0){
	        	for(int j=0; j<listPerson.size(); j++){
	        	   int userId =	listPerson.get(j).getSeqId();
	        	   String SID="";
	        	   if(ArrayNum!=0 && ArrayNum > 0){//定义薪酬项目的值
	   		        for(int i = 0 ;i < ArrayNum ; i++){
	   		        	
	   		        	Integer seqId[]= new Integer[ArrayNum];
	   		        	YHSalItem salItem = wageBase.getSalItemIdAndNameLogic(dbConn, user, Integer.valueOf(slaItemIds[i]));
	   		        	seqId[i] = salItem.getSlaitemId();
	   		        	SID += "S"+seqId[i]+",";
	   		        	//listSalItemData.add(salItem);
	   		        }
	   		    }
  		        	Map map = wageBase.getHrSalDateLogic(dbConn,user,userId,SID);
  		        	
  		        	listSalItemData.add(map);
	         }
	      }*/   
		   /*if(ArrayNum!=0 && ArrayNum > 0){//定义薪酬项目的值
		        for(int i = 0 ;i < ArrayNum ; i++){
		        	String SID;
		        	Integer seqId[]= new Integer[ArrayNum];
		        	YHSalItem salItem = wageBase.getSalItemIdAndNameLogic(dbConn, user, Integer.valueOf(slaItemIds[i]));
		        	seqId[i] = salItem.getSlaitemId();
		        	SID = "S"+seqId[i];
		        	listSalItemData.add(salItem);
		        }
		  }*/
			request.setAttribute("deptId", deptId);//部门Id
			request.setAttribute("listSalItem", listSalItem);//显示多少薪酬项目标题
			request.setAttribute("listPerson", listPerson);//根据部门id查找所有属于这个部门的人员
			//request.setAttribute("listSalItemData", listSalItemData);
			request.setAttribute("slaItemId", slaItemId);
			request.setAttribute("hrInsurancePara", insurancePara);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/hr/salary/staffInsurance/wageList.jsp";
	}
	/**
	 * 增加薪酬基数
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addWageBaseAct(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String contexPath = request.getContextPath();
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		YHPerson user = (YHPerson) request.getSession().getAttribute(
				"LOGIN_USER");// 获得登陆用户
		Connection dbConn = null;
		
		try {
			dbConn = requestDbConn.getSysDbConn();
			wageBase.addWageBaseLogic(dbConn, fileForm, user);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		response.sendRedirect(contexPath + "/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/getSalaryItemJson.act");
		return null;
	}
	/**
	 * 批量增加或更新 部门下所有员工的 薪酬项目和保险系数
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setSubmitInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
	    String contexPath = request.getContextPath();
	    Connection dbConn = null;
	   //  String ta = request.getParameter("total");
	    // String ti = request.getParameter("title");
	     
	    try {
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
	      wageBase.setSubmitInfo(dbConn,YHFOM.buildMap(request.getParameterMap()),person);
	      //wageBase.setSubmitInfo(dbConn,request.getParameterMap(),person);
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
	      throw e;
	    }
	    return "/core/inc/rtjson.jsp";
	  }
	/**
	 * 设置单个员工的 薪酬项目和保险系数
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String setOnePersonSubmitInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
	    String contexPath = request.getContextPath();
	    Connection dbConn = null;
	    // String ta = request.getParameter("total");
	    // String ti = request.getParameter("title");
	    try {
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
	      wageBase.setOnePersonSubmitLogic(dbConn,YHFOM.buildMap(request.getParameterMap()),person);
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
	      throw e;
	    }
	    return "/core/inc/rtjson.jsp";
	  }
	/**
	 * 获取单个人员的薪酬项目和保险基数
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	 public String getSalItemIdUserAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		    Connection dbConn = null;
		    YHSalPerson person = null;
		    String yesOtherId="0";
		    String userId = request.getParameter("personId");
		    String userName = request.getParameter("userName");
		    userName = YHUtility.decodeURL(userName);
		    
		    List<YHSalItem> listSalItem = new ArrayList<YHSalItem>();
		    try {
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		    //求sal_item中的SLAITEM_ID字段 这个字段是设置的薪酬项目数最多不超过50个   其中这个字段（动态的）和HR_SAL_DATA 中的S1-S5对应 
		      String slaItemId = wageBase.getSalItemIdLogic(dbConn, user);
		      String[] slaItemIds = slaItemId.split(",");
		      int ArrayNum = slaItemIds.length;
		      if(slaItemIds[ArrayNum-1] == ""){
		        ArrayNum--;
		      }
		    if(ArrayNum!=0 && ArrayNum > 0){
		        for(int i = 0 ;i < ArrayNum ; i++){
		          YHSalItem salItem =wageBase.getSalItemIdAndNameLogic(dbConn, user, Integer.valueOf(slaItemIds[i]));
		          listSalItem.add(salItem);//取出薪酬项目名称保存集合中
		        }
		      }
		    List<YHHrInsurancePara> insurancePara = wageBase.getYesOtherLogic1(dbConn, user);
		     if(!YHUtility.isNullorEmpty(userId)){
		    	 //取出属于哪个部门下的人员所对应的  薪酬项目以及保险基数的数据
		        person = wageBase.getPersonNameLogic(dbConn, user, userId);
		      }
		       yesOtherId = wageBase.getYesOtherLogic(dbConn, user);
		      if(!YHUtility.isNullorEmpty(yesOtherId)){
		        request.setAttribute("yesOtherId", Integer.valueOf(yesOtherId));//查询HR_INSURANCE_PARA中的yesOther 是否为1 为1显示保险系数
		      }else {
		         request.setAttribute("yesOtherId", 0);
		       }
		      request.setAttribute("userId", userId);//人员Id
		      request.setAttribute("listSalItem", listSalItem);//显示薪酬项目中的输入项 和录入项
		      request.setAttribute("person", person);//获取人员下的所有数据
		      request.setAttribute("userName", userName);
		      request.setAttribute("hrInsurancePara", insurancePara);
		    } catch (Exception e) {
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
		      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
		      throw e;
		    }
		    return "/subsys/oa/hr/salary/staffInsurance/wageListUser.jsp";
		  }
	 /**
	  * 员工薪酬基数批量设置
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public String setAllSalBaseAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		    Connection dbConn = null;
		    String yesOtherId ="0";
		   // YHSalPerson person = null;
		    List<YHSalPerson> listPerson = null;
		    List<YHSalItem> listSalItem = new ArrayList<YHSalItem>();
		    try {
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		    //求sal_item中的SLAITEM_ID字段 这个字段是设置的薪酬项目数最多不超过50个   其中这个字段（动态的）和HR_SAL_DATA 中的S1-S5对应 
		      String slaItemId = wageBase.getSalItemIdLogic(dbConn, user);
		      String[] slaItemIds = slaItemId.split(",");
		      int ArrayNum = slaItemIds.length;
		      if(slaItemIds[ArrayNum-1] == ""){
		        ArrayNum--;
		      }
		    if(ArrayNum!=0 && ArrayNum > 0){
		        for(int i = 0 ;i < ArrayNum ; i++){
		          YHSalItem salItem =wageBase.getSalItemIdAndNameLogic(dbConn, user, Integer.valueOf(slaItemIds[i]));
		          listSalItem.add(salItem);//取出薪酬项目名称保存集合中
		        }
		      }
		    List<YHHrInsurancePara> insurancePara = wageBase.getYesOtherLogic1(dbConn, user);
		        yesOtherId = wageBase.getYesOtherLogic(dbConn, user);
		      if(!YHUtility.isNullorEmpty(yesOtherId)){
		        request.setAttribute("yesOtherId", Integer.valueOf(yesOtherId));//查询HR_INSURANCE_PARA中的yesOther 是否为1 为1显示保险系数
		      }else {
		         request.setAttribute("yesOtherId", 0);
		       }
	    	 //取出属于哪个部门下的人员所对应的  薪酬项目以及保险基数的数据
		      listPerson = wageBase.setPLPersonNameLogic(dbConn, user);
	  
		      
		      request.setAttribute("listSalItem", listSalItem);//显示薪酬项目中的输入项 和录入项
		      request.setAttribute("hrInsurancePara", insurancePara);
		      request.setAttribute("listPerson", listPerson);
		    } catch (Exception e) {
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
		      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
		      throw e;
		    }
		    return "/subsys/oa/hr/salary/staffInsurance/setAllSalBase.jsp";
		  }
	 
	 public String setPLPersonSubmitInfo(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			YHFileUploadForm fileForm = new YHFileUploadForm();
			//fileForm.parseUploadRequest(request); //没有上传文件把这句话去掉  不然会报错
			String contexPath = request.getContextPath();
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			Connection dbConn = null;
				try {
				dbConn = requestDbConn.getSysDbConn();
				//wageBase.setPLPersonSubmitLogic(dbConn,YHFOM.buildMap(request.getParameterMap()),user,putName,reportName);
				wageBase.setPLPersonSubmitLogic(dbConn,YHFOM.buildMap(request.getParameterMap()),user);
				} catch (Exception e) {
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
				request.setAttribute(YHActionKeys.FORWARD_PATH,
						"/core/inc/error.jsp");
				throw e;
			}
			//response.sendRedirect(contexPath + "/subsys/oa/officeProduct/officeType/addOK.jsp");
			return "/core/inc/rtjson.jsp";
		}
	
}
