package yh.rad.dbexputil.transplant.logic.core.processor;

import yh.core.global.YHSysProps;

public class YHSpecialHandlerFactory {
  public static YHSpecialHandler getSpecialHandlerInstance(String spType) throws Exception{
    YHSpecialHandler result = null;
    //从配置文件得到当前数据文件的数据类型
    String className = "";
    className = YHSysProps.getProp("db.jdbc.sp_" + spType);
    result = (YHSpecialHandler) (Class.forName(className)).newInstance();
    return result;
  }
}
