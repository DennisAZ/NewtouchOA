package yh.subsys.oa.officeProduct.officeType.act;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.officeProduct.commentOffice;
import yh.subsys.oa.officeProduct.officeType.data.YHOfficeDepository;
import yh.subsys.oa.officeProduct.officeType.data.YHOfficeType;
import yh.subsys.oa.officeProduct.officeType.logic.YHOfficeDepositoryLogic;

/**
 * 增加办公用品库
 * @author Administrator
 *
 */
public class YHOfficeDepositoryAct {
	YHOfficeDepositoryLogic officeDepository = new YHOfficeDepositoryLogic();
	commentOffice comment = new commentOffice();
	public String addOfficeDepository(HttpServletRequest request,
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
			officeDepository.setOfficeInfoValueLogic(dbConn, fileForm, user);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		response.sendRedirect(contexPath + "/subsys/oa/officeProduct/officeType/addOK.jsp");
		return null;
	}
   /**
    * 查询办公用品库
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
	public String findOfficeDepositoryInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			
			List<YHOfficeDepository> findOfficeDep = officeDepository.findOfficeDepositorySet(dbConn,
					user);
			
			request.setAttribute("findOfficeDepS", findOfficeDep);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/officeProduct/officeType/index.jsp";
	}
	/**
	 * 删除办公用品库设置
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delOfficeDepositoryInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");
			String noHiddenId = request.getParameter("HiddenId");

			int ok = officeDepository.delOfficeDepository(dbConn, person, Integer
					.parseInt(noHiddenId));
			if (ok != 0) {
				return "/yh/subsys/oa/officeProduct/officeType/act/YHOfficeDepositoryAct/findOfficeDepositoryInfo.act";
			}
			// request.setAttribute("booktype", booktype);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/yh/subsys/oa/officeProduct/officeType/act/YHOfficeDepositoryAct/findOfficeDepositoryInfo.act";
	}
	/**
	 * 修改办公用品库之前先进行查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String upOfficeDepositoryInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		Connection dbConn = null;
		String deptName ="";
		String managerName ="";
		String keeperName ="";
		
		try {
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			String officeId = request.getParameter("officeSeId");

			YHOfficeDepository findOffice = officeDepository.officeDepositoryInfo(dbConn,
					user, Integer.parseInt(officeId));
			List<YHOfficeType> officeType = findOfficeType(dbConn, user,findOffice.getSeqId());//通过办公用品库表的seq_id 获取办公用品类型type_nam
			if(!YHUtility.isNullorEmpty(findOffice.getDeptId())){
			    deptName = comment.findDept(dbConn, user, findOffice.getDeptId());
			}
			if(!YHUtility.isNullorEmpty(findOffice.getManager())){
			    managerName = comment.findManager(dbConn, user, findOffice.getManager());
			}
			if(!YHUtility.isNullorEmpty(findOffice.getManager())){
			    keeperName = comment.findProKeeper(dbConn, user, findOffice.getProKeeper());
			}
			//String deptId = findOffice.getDeptId();
			//System.out.println(deptId);
			/*String seqId ="";
			for(int i=0; i<findlicenses.size(); i++){
			   seqId = findlicenses.get(i).getStaffName();
			}
			if(!YHUtility.isNullorEmpty(seqId)){
			    String userName =	workTrans.getUserName(dbConn,Integer.valueOf(seqId));
			    request.setAttribute("userName", userName);
			}*/
			request.setAttribute("officeDepositoryInfo", findOffice);
			request.setAttribute("deptNames", deptName);
			request.setAttribute("managerNames", managerName);
			request.setAttribute("keeperNames", keeperName);
			request.setAttribute("officeType", officeType);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		return "/subsys/oa/officeProduct/officeType/updateOfficeDepository.jsp";
	}
	/**
	 * 通过办公用品库查找办公用品类型名称
	 * @param dbConn
	 * @param user
	 * @param manager
	 * @return
	 * @throws Exception
	 */
	public List<YHOfficeType> findOfficeType(Connection dbConn, YHPerson user,int seqId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List list = new ArrayList();
		String managers="";
		String sql = "select SEQ_ID,TYPE_NAME from oa_office_kind where type_depository="+ seqId;
		List<YHOfficeType> officeTypes = new ArrayList<YHOfficeType>();
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				YHOfficeType officeType = new YHOfficeType();
				officeType.setSeqId(rs.getInt("SEQ_ID"));
				officeType.setTypeName(rs.getString("TYPE_NAME")); 
				officeTypes.add(officeType);
			}

			
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return officeTypes;
	}
	
	/**
	 * 修改办公用品库设置
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateOfficeDepositoryInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		YHPerson user = (YHPerson) request.getSession().getAttribute(
				"LOGIN_USER");// 获得登陆用户
		Connection dbConn = null;
		String seqid = request.getParameter("seqid");
		String depositoryName = request.getParameter("DEPOSITORY_NAME"); // 库名称
		
		String deptId = request.getParameter("dept");// 所属部门
		String userManager = request.getParameter("user"); // 库管理员
		String user1 = request.getParameter("user1");// 物品调度员
		String[] typeName = request.getParameterValues("typeName");//类别名称
		String typeNameStr="";
		if(typeName!=null && typeName.length > 0){
	        for(int i = 0 ;i < typeName.length ; i++){
	        	typeNameStr +=  typeName[i] + ",";
	        }
	        typeNameStr = typeNameStr.substring(0, typeNameStr.length() - 1);
		}
		YHOfficeDepository office = new YHOfficeDepository();
		office.setSeqId(Integer.valueOf(seqid));
		 office.setDepositoryName(depositoryName);
		 office.setDeptId(deptId);
		 office.setManager(userManager);
		 office.setProKeeper(user1);
		 if(!YHUtility.isNullorEmpty(typeNameStr)){
		  office.setOfficeTypeId(typeNameStr);
		 }
		try {
			dbConn = requestDbConn.getSysDbConn();
			//officeDepository.setUpOfficeInfoLogic(dbConn, fileForm, user);
		 int ok =	officeDepository.updateLicenseInfo(dbConn, user,office);
		 if (ok != 0) { 
				return "/yh/subsys/oa/officeProduct/officeType/act/YHOfficeDepositoryAct/findOfficeDepositoryInfo.act";
			}
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			request.setAttribute(YHActionKeys.FORWARD_PATH,
					"/core/inc/error.jsp");
			throw e;
		}
		//response.sendRedirect(contexPath + "/yh/subsys/oa/officeProduct/act/YHOfficeDepositoryAct/findOfficeDepositoryInfo.act");
		return "/yh/subsys/oa/officeProduct/officeType/act/YHOfficeDepositoryAct/findOfficeDepositoryInfo.act";
	}
	
}
