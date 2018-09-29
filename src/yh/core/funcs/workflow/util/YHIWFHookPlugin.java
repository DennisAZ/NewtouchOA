package yh.core.funcs.workflow.util;

import java.sql.Connection;
import java.util.Map;

/**
 * 工作流业务引擎插件接口
 * @author jpt
 *
 */
public interface YHIWFHookPlugin {
  /**
   * 工作流结束时执行
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String execute(Connection conn , int runId  , Map arrayHandler , Map formData , boolean  agree ) throws Exception;
}
