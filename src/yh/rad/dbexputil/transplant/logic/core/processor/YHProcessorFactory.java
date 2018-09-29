package yh.rad.dbexputil.transplant.logic.core.processor;

import yh.core.global.YHSysProps;

/**
 * 实体类的工厂方法
 * @author Think
 *
 */
public class YHProcessorFactory {
  /**
   * 得到数据处理的实体类对象
   * @param dbType
   * @return
   * @throws Exception
   */
  public static YHProcI getProcessorInstance(int dbType) throws Exception{
    YHProcI result = null;
    //从配置文件得到当前数据文件的数据类型
    String className = "";
    if(dbType == 1){
      className = YHSysProps.getProp("db.jdbc.olddbms.proc");
    }else if(dbType == 2){
      className = YHSysProps.getProp("db.jdbc.newdbms.proc");
    }
    result = (YHProcI) (Class.forName(className)).newInstance();
    return result;
  }
}
