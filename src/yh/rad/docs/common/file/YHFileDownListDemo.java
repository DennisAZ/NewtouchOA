package yh.rad.docs.common.file;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;


public class YHFileDownListDemo {

  public String fileDownListJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String attachmentId = "";
      String attachmentName = "";
      String data = "{attachmentId:\"" + attachmentId + "\",attachmentName:\"" + attachmentName + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载代码失败" + ex.getMessage());
      throw ex;
    } 
    return "/core/inc/rtjson.jsp";
  }
}
