package yh.subsys.oa.hr.manage.staff_contract.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.logic.YHInnerEMailLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.book.data.YHBookInfo;
import yh.subsys.oa.book.data.YHBookType;
import yh.subsys.oa.book.data.YHPage;
import yh.subsys.oa.book.logic.YHBookQueryLogic;
import yh.subsys.oa.book.logic.YHBookTypeLogic;
import yh.subsys.oa.hr.manage.hrIdtransName.hrPublicIdTransName;
import yh.subsys.oa.hr.manage.staff_contract.data.YHHrStaffContract;
import yh.subsys.oa.hr.manage.staff_contract.logic.YHNewContractInfoLogic;

public class YHNewContractInfoAct {
	public static final String attachmentFolder = "contract";
	hrPublicIdTransName workTrans = new hrPublicIdTransName();
	/**
   * 附件上传
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String fileLoad(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PrintWriter pw = null;
    request.setCharacterEncoding(YHConst.DEFAULT_CODE);
    response.setCharacterEncoding(YHConst.DEFAULT_CODE);
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      YHNewContractInfoLogic ieml = new YHNewContractInfoLogic();
      StringBuffer sb = ieml.uploadMsrg2Json(fileForm);
      String data = "{'state':'0','data':" + sb.toString() + "}";
      pw = response.getWriter();
      pw.println(data.trim());
      pw.flush();
    }catch(Exception e){
      pw = response.getWriter();
      pw.println("{'state':'1'}".trim());
      pw.flush();
    } finally {
      pw.close();
    }
    return null;
  }
	public String addContractInfo(HttpServletRequest request,  HttpServletResponse response) throws Exception{
		
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String contexPath = request.getContextPath();
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		YHPerson user = (YHPerson) request.getSession().getAttribute(
				"LOGIN_USER");// 获得登陆用户
		Connection dbConn = null;
		 try{ 
		  dbConn = requestDbConn.getSysDbConn();
		  YHNewContractInfoLogic contractLogic = new YHNewContractInfoLogic();
		  contractLogic.setNewContractInfoValueLogic(dbConn, fileForm, user , request.getContextPath());
	     /*int ok = contractLogic.newContractInfo(dbConn, user, Contract);
	     if(ok!=0){
	    	 return "/subsys/oa/hr/manage/staff_contract/addOK.jsp";
	     } */
		 }catch(Exception e){
			 request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		     request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
		     request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
		     throw e;
		 }
		 
		 response.sendRedirect(contexPath + "/subsys/oa/hr/manage/staff_contract/addOK.jsp");
		return null;
	}
	/**
	 * 修改合同信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateContractInfo(HttpServletRequest request,  HttpServletResponse response) throws Exception{
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String contexPath = request.getContextPath();
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		YHPerson user = (YHPerson) request.getSession().getAttribute(
				"LOGIN_USER");// 获得登陆用户
		Connection dbConn = null;
		
		 try{ 
		  dbConn = requestDbConn.getSysDbConn();
		  YHNewContractInfoLogic contractLogic = new YHNewContractInfoLogic();
		  contractLogic.setUpContractInfoValueLogic(dbConn, fileForm, user , request.getContextPath());
	    /* int ok = contractLogic.upContractInfo(dbConn, user, Contract);
	     if(ok!=0){
	    	 return "/yh/subsys/oa/hr/manage/staff_contract/act/YHNewContractInfoAct/findContractInfo.act";
	     } */
	     
		 }catch(Exception e){
			 request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		     request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
		     request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
		     throw e;
		 }
		 response.sendRedirect(contexPath + "/yh/subsys/oa/hr/manage/staff_contract/act/YHNewContractInfoAct/findContractInfo.act");
			return null;
	//	return "/yh/subsys/oa/hr/manage/staff_contract/act/YHNewContractInfoAct/findContractInfo.act";
	}
	
	/*
	 * 查询合同信息带分页的
	 */
	public String findContractInfo(HttpServletRequest request,  HttpServletResponse response) throws Exception{
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
	    Connection dbConn = null; 
		try{ 
			 dbConn = requestDbConn.getSysDbConn();
			 YHNewContractInfoLogic findContract = new YHNewContractInfoLogic();
		      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
		     // String currNo = request.getParameter("currNo");
		      
		      int total = findContract.count(dbConn, user);
		      String currNo = request.getParameter("currNo");
		      int curruntNo = 1;
		      if(YHUtility.isNullorEmpty(currNo)){
		        curruntNo = 1;
		      }else{
		        curruntNo = Integer.parseInt(currNo);
		      }
		      YHPage page = new YHPage(5, total, curruntNo);
		      List<YHHrStaffContract> findContracts = findContract.findContracts(dbConn, user, page);
		      request.setAttribute("contractInfoList",findContracts);
		      request.setAttribute("page",page);
		}catch(Exception e){
			 request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		     request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
		     request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
		     throw e;
		 }
		return "/subsys/oa/hr/manage/staff_contract/index1.jsp";
	}
	/**
	 * 查询合同的详细信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String contractXxInfo(HttpServletRequest request,  HttpServletResponse response) throws Exception{
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
	    Connection dbConn = null; 
	    YHNewContractInfoLogic delContract = new YHNewContractInfoLogic();
		try{ 
			 dbConn = requestDbConn.getSysDbConn();
			 YHNewContractInfoLogic findContract = new YHNewContractInfoLogic();
		      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
		      String ContractSeqId =  request.getParameter("ContractSeqId");
		      
		      List<YHHrStaffContract> findContracts = findContract.contractXxInfo(dbConn, user,Integer.parseInt(ContractSeqId));
		      String seqId ="";
				for(int i=0; i<findContracts.size(); i++){
				   seqId = findContracts.get(i).getStaffName();
				}
				if(!YHUtility.isNullorEmpty(seqId)){
				    String userName =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
				    request.setAttribute("userName", userName);
				}
		      request.setAttribute("contractXXInfoList",findContracts);
		      
		}catch(Exception e){
			 request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		     request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
		     request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
		     throw e;
		 }
		return "/subsys/oa/hr/manage/staff_contract/contractXxInfo.jsp";
	}
	
	/**
	 * 删除合同的基本信息
	 * @param request
	 * @param response 
	 * @throws Exception
	 */
	  public String delContractInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
          YHNewContractInfoLogic delContract = new YHNewContractInfoLogic();
          Connection dbConn = null;
          YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
         try{
          dbConn = requestDbConn.getSysDbConn();
          YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
          String noHiddenId =  request.getParameter("HiddenId");
          int ok = delContract.delContractInfo(dbConn, person ,Integer.parseInt(noHiddenId));
          if(ok!=0){
        	  return "/yh/subsys/oa/hr/manage/staff_contract/act/YHNewContractInfoAct/findContractInfo.act";
          }
          //request.setAttribute("booktype", booktype);
         }catch(Exception e){
			 request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		     request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
		     request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
		     throw e;
		 }
         return "/yh/subsys/oa/hr/manage/staff_contract/act/YHNewContractInfoAct/findContractInfo.act";
	 }
	  
	  public String upContractInfo(HttpServletRequest request,  HttpServletResponse response) throws Exception{
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
		    Connection dbConn = null; 
			try{ 
				 dbConn = requestDbConn.getSysDbConn();
				 YHNewContractInfoLogic findContract = new YHNewContractInfoLogic();
		      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
		      String ContractSeqId =  request.getParameter("contractId");
		      
		      List<YHHrStaffContract> findContracts = findContract.contractXxInfo(dbConn, user,Integer.parseInt(ContractSeqId));
		      String seqId ="";
					for(int i=0; i<findContracts.size(); i++){
					   seqId = findContracts.get(i).getStaffName();
					}
					if(!YHUtility.isNullorEmpty(seqId)){
				    String userName =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
				    request.setAttribute("userName", userName);
					}
		      request.setAttribute("contractXXInfoList",findContracts);
			}catch(Exception e){
				 request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			     request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			     request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
			     throw e;
			 }
			return "/subsys/oa/hr/manage/staff_contract/updateContract.jsp";
		}
	  /**
	   * 删除所选择的合同信息
	   */
	  public String deleteContractInfo(HttpServletRequest request,
		      HttpServletResponse response)throws Exception{
		      Connection dbConn = null;
		      YHNewContractInfoLogic delContract = new YHNewContractInfoLogic();
		      try{
		        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		        dbConn = requestDbConn.getSysDbConn();
		         YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
		         String deleteStr = request.getParameter("deleteStr");
		         
		          delContract.deleteContractInfo(dbConn, deleteStr);
		          
		          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);//RET_STATE返回状态  RETURN_OK正确返回
		         request.setAttribute(YHActionKeys.RET_MSRG, "合同信息删除成功");//RET_MSRG 返回消息
		        // request.setAttribute(YHActionKeys.RET_DATA, str);//RET_DATA 返回数据
		           }catch(Exception ex){
		        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		        throw ex;
		      }
		     return "/core/inc/rtjson.jsp";
		  }
	  /**
	   * 查询合同信息（带时间条件）
	   * @param request
	   * @param response
	   * @return
	   * @throws Exception
	   */
	  public String queryContractInfo(HttpServletRequest request,  HttpServletResponse response) throws Exception{
		    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
		    Connection dbConn = null; 
		    YHNewContractInfoLogic queryContract = new YHNewContractInfoLogic();
		    String userId = request.getParameter("userId");
            String userName = request.getParameter("userName");
		    String staffContractNo = request.getParameter("STAFF_CONTRACT_NO");
		    String contractType = request.getParameter("contractType");
		    String contractState = request.getParameter("contractState"); 
		    String makeContract1 = request.getParameter("MAKE_CONTRACT1");
		    String makeContract2 = request.getParameter("MAKE_CONTRACT2");
		    String trailOverTime1 = request.getParameter("TRAIL_OVER_TIME1");
		    String trailOverTime2 = request.getParameter("TRAIL_OVER_TIME2");
		    String contractEndTime1 = request.getParameter("CONTRACT_END_TIME1");
		    String contractEndTime2 = request.getParameter("CONTRACT_END_TIME2");
		    
		    YHHrStaffContract contract = new YHHrStaffContract();
		    contract.setStaffName(userId);
		    contract.setStaffContractNo(staffContractNo);
		    contract.setContractType(contractType);
		    contract.setStatus(contractState);
		   
		    try{
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
		      int total = queryContract.count1(dbConn, user, contract,makeContract1,makeContract2,trailOverTime1,trailOverTime2,contractEndTime1,contractEndTime2);
			     
		      String currNo = request.getParameter("currNo");
		      int curruntNo = 1;
		      if(YHUtility.isNullorEmpty(currNo)){
		        curruntNo = 1;
		      }else{
		        curruntNo = Integer.parseInt(currNo);
		      }
		      YHPage page = new YHPage(5, total, curruntNo);
		      
		      List<YHHrStaffContract> findContracts = queryContract.queryContractInfo(dbConn,user,page,contract,makeContract1,makeContract2,trailOverTime1,trailOverTime2,contractEndTime1,contractEndTime2);
		      request.setAttribute("contractInfoList",findContracts);
		      request.setAttribute("contracts",contract);
		      
		      request.setAttribute("makeC1",makeContract1);
		      request.setAttribute("makeC2",makeContract2);
		      request.setAttribute("trailO1",trailOverTime1);
		      request.setAttribute("trailO2",trailOverTime2);
		      request.setAttribute("contractEnd1",contractEndTime1);
		      request.setAttribute("contractEnd2",contractEndTime2);
		      request.setAttribute("page",page);
		  
		    } catch (SQLException e){   
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
		      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
		      throw e;
		    }    
		    return "/subsys/oa/hr/manage/staff_contract/index2.jsp";
		  }
	  
	  /**
	   * 浮动菜单文件删除
	   * @param request
	   * @param response
	   * @return
	   * @throws Exception
	   */
	  public String delFloatFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String seqIdStr = request.getParameter("seqId");
	    String attachId = request.getParameter("delAttachId");
	    Connection dbConn = null;
	    try {
	      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      YHNewContractInfoLogic ieml = new YHNewContractInfoLogic();
	      boolean updateFlag = ieml.updateFloadFile(dbConn, seqIdStr, YHUtility.null2Empty(attachId));
	      int returnFlag = 0;
	      if (updateFlag) {
	        returnFlag = 1;
	      }
	      String data = "{updateFlag:\"" + returnFlag + "\"}";
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_DATA, data);
	    } catch (Exception e) {
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
	      throw e;
	    }
	    return "/core/inc/rtjson.jsp";
	  }
	
}
