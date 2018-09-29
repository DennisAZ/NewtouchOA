package yh.rad.dbexputil.transplant.logic.core.processor;

import java.util.HashMap;

public interface YHSpecialHandler {
/**
 * 外部处理程序
 * @param conn
 * @param value
 * @param params
 * @return
 * @throws Exception
 */
  public Object excute(Object value,HashMap<String, String> params)throws Exception;
}
