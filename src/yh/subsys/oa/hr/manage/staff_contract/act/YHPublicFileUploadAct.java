package yh.subsys.oa.hr.manage.staff_contract.act;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.hr.manage.staff_contract.logic.YHPublicFileUploadLogic;

//yh/subsys/oa/hr/hrManager/act/YHPublicFileUploadAct
//import yh.subsys.inforesouce.docmgr.data.YHDocReceive;
//import yh.subsys.inforesouce.docmgr.logic.YHDocReceiveLogic;
//import yh.subsys.inforesouce.docmgr.logic.YHDocSmsLogic;
public class YHPublicFileUploadAct {
	 /**
	   * 单文件附件上传
	   * @param request
	   * @param response
	   * @return
	   * @throws Exception
	   */
	  public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    YHFileUploadForm fileForm = new YHFileUploadForm();
	    fileForm.parseUploadRequest(request);
	    Map<String, String> attr = null;
	    String attrId = (fileForm.getParameter("attachmentId")== null )? "":fileForm.getParameter("attachmentId");
	    String attrName = (fileForm.getParameter("attachmentName")== null )? "":fileForm.getParameter("attachmentName");
	    String data = "";
	    try{
	    	YHPublicFileUploadLogic  docLogic = new YHPublicFileUploadLogic();
	      attr = docLogic.fileUploadLogic(fileForm, YHSysProps.getAttachPath());
	      Set<String> keys = attr.keySet();
	      for (String key : keys){
	        String value = attr.get(key);
	        if(attrId != null && !"".equals(attrId)){
	          if(!(attrId.trim()).endsWith(",")){
	            attrId += ",";
	          }
	          if(!(attrName.trim()).endsWith("*")){
	            attrName += "*";
	          }
	        }
	        attrId += key + ",";
	        attrName += value + "*";
	      }
	      data = "{attrId:\"" + attrId + "\",attrName:\"" + attrName + "\"}";
	      //YHOut.println(data);
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
	      request.setAttribute(YHActionKeys.RET_DATA, data);

	    } catch (Exception e){
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
	      throw e;
	    }
	    return "/core/inc/rtuploadfile.jsp";
	  }
}
