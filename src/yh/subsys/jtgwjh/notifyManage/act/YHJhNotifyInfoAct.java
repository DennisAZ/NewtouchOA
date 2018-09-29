package yh.subsys.jtgwjh.notifyManage.act;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.news.logic.YHNewsManageLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendInfo;
import yh.subsys.jtgwjh.docSend.logic.YHDocSendLogic;
import yh.subsys.jtgwjh.notifyManage.logic.YHJhNotifyInfoLogic;
import yh.subsys.jtgwjh.notifyManage.data.YHJhNotify;
public class YHJhNotifyInfoAct{
	 YHJhNotifyInfoLogic logic = new YHJhNotifyInfoLogic();
	public static final String attachmentFolder = "notify";
	
	  public String fileLoad(HttpServletRequest request,   HttpServletResponse response) throws Exception{
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
		      YHJhNotifyInfoLogic ieml = new YHJhNotifyInfoLogic();
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
			/*
			 *增加公告信息
			 */
			
			public String addNotifyInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
				YHFileUploadForm fileForm = new YHFileUploadForm();
				fileForm.parseUploadRequest(request);
				String contexPath = request.getContextPath();
				Connection dbConn = null;
				try {
					YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
					YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
					dbConn = requestDbConn.getSysDbConn();
					this.logic.setNotifyValueLogic(dbConn, fileForm, person);
					request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
					request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
				} catch (Exception ex) {
					request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
					request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
					throw ex;
				}
				response.sendRedirect(contexPath + "/subsys/jtgwjh/notifyManage/newRemind.jsp?type=1");
				return null;
			}
 			
			//保存并发送公告
			public String addAndSendNotifyInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
				YHFileUploadForm fileForm = new YHFileUploadForm();
				fileForm.parseUploadRequest(request);
				String contexPath = request.getContextPath();
				Connection dbConn = null;
				try {
					YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
					YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
					dbConn = requestDbConn.getSysDbConn();
					this.logic.setNotifyValueLogic(dbConn, fileForm, person);
					int jhNotifyId=this.logic.getMaxSeqId(dbConn);
					this.logic.sendNotify(dbConn, jhNotifyId, request, null);
					request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
					request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
				} catch (Exception ex) {
					request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
					request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
					throw ex;
				}
				response.sendRedirect(contexPath + "/subsys/jtgwjh/notifyManage/newRemind.jsp?type=2");
				return null;
			}
			//删除浮动菜单文件
			  public String delFloatFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
				    String attachId = request.getParameter("attachId");
				    String attachName = request.getParameter("attachName");
				    String sSeqId = request.getParameter("seqId");
				    //YHOut.println(sSeqId);
				    if (attachId == null) {
				      attachId = "";
				    }
				    if (attachName == null) {
				      attachName = "";
				    }
				    int seqId = 0 ;
				    if (sSeqId != null && !"".equals(sSeqId)) {
				      seqId = Integer.parseInt(sSeqId);
				    }
				    Connection dbConn = null;
				    try {
				      YHRequestDbConn requesttDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
				      dbConn = requesttDbConn.getSysDbConn();

				      YHJhNotifyInfoLogic logic = new YHJhNotifyInfoLogic();

				      boolean updateFlag = logic.delFloatFile(dbConn, attachId, attachName , seqId);
				     
				      String isDel="";
				      if (updateFlag) {
				        isDel ="isDel"; 

				      }
				      String data = "{updateFlag:\"" + isDel + "\"}";
				      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
				      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功!");
				      request.setAttribute(YHActionKeys.RET_DATA, data);
				    } catch (Exception e) {
				      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
				      throw e;
				    }

				    return "/core/inc/rtjson.jsp";
				  }
			  
			  /**
			   * 发送公告信息

			   * @param request
			   * @param response
			   * @return
			   * @throws Exception
			   */
			  public String sendNotifyInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
			    
			    String notifysendInfoId = request.getParameter("jhNotifyId");
			    Connection dbConn = null;
			    try {
			      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			      dbConn = requestDbConn.getSysDbConn();
			      
			      YHJhNotifyInfoLogic logic = new YHJhNotifyInfoLogic();
			      
			      //发送公告信息
			      logic.sendNotify(dbConn, Integer.parseInt(notifysendInfoId), request, null);
			      
			      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
			    } catch (Exception ex) {
			      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			      throw ex;
			    }
			    return "/core/inc/rtjson.jsp";
			  }
			  
			  public String sendNotifyInfoTasks(HttpServletRequest request, HttpServletResponse response) throws Exception {
				    
				    String guid = request.getParameter("guid");
				    String toId = request.getParameter("toId");
				    Connection dbConn = null;
				    try {
				      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
				      dbConn = requestDbConn.getSysDbConn();
				      
				      YHJhNotifyInfoLogic logic = new YHJhNotifyInfoLogic();
				      
				      //直接发送公告信息
				      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
				      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
				    } catch (Exception ex) {
				      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
				      throw ex;
				    }
				    return "/core/inc/rtjson.jsp";
				  }
				  
			  //获取公告列表信息
			  public String getListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
				    Connection dbConn = null;
				    String publish = request.getParameter("publish");
				    try {
				      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
				      dbConn = requestDbConn.getSysDbConn();
				      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
				      YHJhNotifyInfoLogic logic = new YHJhNotifyInfoLogic();
				      String data = logic.getJsonLogic(dbConn, request.getParameterMap(), person, request,publish);
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
			 
			  //删除公告信息
			  public String deleteFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
				    String seqIdStr = request.getParameter("seqId");
				    Connection dbConn = null;
				    try {
				      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
				      dbConn = requestDbConn.getSysDbConn();
				      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

				      YHJhNotifyInfoLogic logic = new YHJhNotifyInfoLogic();
				      String filePath = YHSysProps.getAttachPath() + File.separator + this.attachmentFolder + File.separator;
				      String temp = logic.deleteFileLogic(dbConn, seqIdStr, filePath);
				      
				      //系统日志
				      YHSysLogLogic.addSysLog(dbConn, "60", "删除发文标题为" + temp + "的公告信息。"  ,person.getSeqId(), request.getRemoteAddr());
				      
				      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
				    } catch (Exception e) {
				      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
				      throw e;
				    }
				    return "/core/inc/rtjson.jsp";
				  }
			  
			  /**
			   * 获取详情
			   * 
			   * @param request
			   * @param response
			   * @return
			   * @throws Exception
			   */
			  public String getDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
			    String seqId = request.getParameter("seqId");
			    if (YHUtility.isNullorEmpty(seqId)) {
			      seqId = "0";
			    }
			    Connection dbConn = null;
			    try {
			      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			      dbConn = requestDbConn.getSysDbConn();
			      YHJhNotifyInfoLogic logic = new YHJhNotifyInfoLogic();
			      
			      //主表json串
			      YHJhNotify notifyInfo = (YHJhNotify) logic.getDetailLogic(dbConn, Integer.parseInt(seqId));
			      if (notifyInfo == null) {
			        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			        request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
			        return "/core/inc/rtjson.jsp";
			      }
			      StringBuffer data1 = YHFOM.toJson(notifyInfo);
			      data1.deleteCharAt(0);
			      data1.deleteCharAt(data1.length() - 1);
			      String data = "{" +data1 +"}";
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
			   * 修改公告信息

			   * @param request
			   * @param response
			   * @return
			   * @throws Exception
			   */
			  public String updateNotifyInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
			    
			    YHFileUploadForm fileForm = new YHFileUploadForm();
			    fileForm.parseUploadRequest(request);
			    String contexPath = request.getContextPath();
			    String seqId = request.getParameter("seqId");
			    Connection dbConn = null;
			    try {
			      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			      dbConn = requestDbConn.getSysDbConn();
			      
			      YHJhNotifyInfoLogic logic = new YHJhNotifyInfoLogic();
			      YHJhNotify  notifyInfo = logic.updateNotifyInfo(dbConn, fileForm, person, Integer.parseInt(seqId));
			      
			      //系统日志
			      YHSysLogLogic.addSysLog(dbConn, "60", "修改公告信息：" + notifyInfo.toString() ,person.getSeqId(), request.getRemoteAddr());
			      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
			    } catch (Exception ex) {
			      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			      throw ex;
			    }
				response.sendRedirect(contexPath + "/subsys/jtgwjh/notifyManage/updateSuccess.jsp");
			    return null;
			  }	  
			  
			  
			  /*
			   * 公告签收
			   */
			  public String queryGroupManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
				    Connection dbConn = null;
				    try {
				      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
				      dbConn = requestDbConn.getSysDbConn();
				      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
				      int userId = user.getSeqId();
				      YHJhNotifyInfoLogic logic = new YHJhNotifyInfoLogic();
				      String data = logic.toSearchData(dbConn, request.getParameterMap(),request);
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
}
