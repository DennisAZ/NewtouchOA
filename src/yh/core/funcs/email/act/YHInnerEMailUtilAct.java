package yh.core.funcs.email.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 主要处理内部邮件相关的特殊功能
 * @author TTlang
 *
 */
public class YHInnerEMailUtilAct{
  
  /**
   * 下载附件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String download(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    response.setContentType("application/octet-stream");
    return "/core/funcs/email/att/5.rar";
  }
}
