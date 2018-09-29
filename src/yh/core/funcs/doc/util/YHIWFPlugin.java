package yh.core.funcs.doc.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 工作流插件接口
 * @author jpt
 *
 */
public interface YHIWFPlugin {
  /**
   * 节点执行前执行
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String before(HttpServletRequest request, HttpServletResponse response) throws Exception;
  /**
   * 节点执行完毕执行
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String after(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
