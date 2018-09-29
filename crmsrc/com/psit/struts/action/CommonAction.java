package com.psit.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * 公共跳转action <br>
 * @author rabbit
 */
public class CommonAction extends BaseDispatchAction{
	/**
	 * 跳转到删除确认页面 <br>
	 * @param request 
	 * 		para > id:删除实体的id  delType:删除类型（序号） isIfrm:删除后是否刷新iframe
	 * @return ActionForward <br>
	 * 		attribute > d:删除实体的id  delType:删除类型（序号） isIfrm:删除后是否刷新iframe
	 */
	public ActionForward toDelConfirm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String delType = request.getParameter("delType");
		String isIfrm = request.getParameter("isIfrm");
		
		request.setAttribute("isIfrm", isIfrm);
		request.setAttribute("id", id);
		request.setAttribute("delType", delType);
		return mapping.findForward("delConfirm");
	}
	
	/**
	 * 跳转到类似删除功能的确认页面
	 * @param request 
	 * 		para > id:删除实体的id  confirmType:删除类型（序号）
	 * @return ActionForward <br>
	 * 		attribute > d:删除实体的id  confirmType:删除类型（序号）
	 */
	
	public ActionForward toConfirm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String confirmType = request.getParameter("confirmType");
		request.setAttribute("id", id);
		request.setAttribute("confirmType", confirmType);
		return mapping.findForward("comConfirm");
	}
	
	/**
	 * 跳转到撤销确认页面 <br>
	 * @param request 
	 * 		para > id:撤销实体的id  delType:撤销类型（序号） isIfrm:撤销后是否刷新iframe
	 * @return ActionForward <br>
	 * 		attribute > d:撤销实体的id  delType:撤销类型（序号） isIfrm:撤销后是否刷新iframe
	 */
	public ActionForward toCancleConfirm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String delType = request.getParameter("canType");
		String isIfrm = request.getParameter("isIfrm");
		
		request.setAttribute("isIfrm", isIfrm);
		request.setAttribute("id", id);
		request.setAttribute("canType", delType);
		return mapping.findForward("cancleConfirm");
	}
}
