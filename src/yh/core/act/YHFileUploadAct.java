package yh.core.act;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.core.util.YHGuid;
import yh.core.util.file.YHFileUploadForm;

public class YHFileUploadAct {
  /**
   * log                                               
   */
  private static Logger log = Logger.getLogger(YHFileUploadAct.class);
  
  
  public String doFileUpload(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    String rtUrl = "/core/inc/rtuploadfile.jsp";
    try {
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      String uploadDir = fileForm.getParameter("uploadPath");
      YHGuid guidBuilder = new YHGuid();
      String relaPath = uploadDir + File.separator + guidBuilder.getRawGuid() + "." + fileForm.getFileExt();
      String filePath = (String)request.getAttribute(YHActionKeys.ACT_CTX_PATH) + relaPath;
      fileForm.saveFile(filePath);
      
      request.setAttribute(YHActionKeys.RET_DATA,
          "{actionFrom: \"upload\", fileNameServer: \"" + relaPath + "\"}");

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.FORWARD_PATH, rtUrl);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败" + ex.getMessage());
      throw ex;
    }
    return rtUrl;
  }
}
