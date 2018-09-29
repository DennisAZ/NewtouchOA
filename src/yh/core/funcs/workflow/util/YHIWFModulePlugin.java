package yh.core.funcs.workflow.util;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 工作流业务组件插件接口
 * @author lh
 *
 */
public interface YHIWFModulePlugin {
  /**
   * 编辑页面--返回编辑页面的地址
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String edit(HttpServletRequest request, HttpServletResponse response) throws Exception;
  /**
   * 返回打印页面的地址
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String print(HttpServletRequest request, HttpServletResponse response) throws Exception;
  /**
   * 保存或者修改
   * @param request
   * @param conn
   * @param moduleId
   * @return
   * @throws Exception
   */
  public String saveOrUpdate(HttpServletRequest request , Connection conn , String moduleId) throws Exception;
}
