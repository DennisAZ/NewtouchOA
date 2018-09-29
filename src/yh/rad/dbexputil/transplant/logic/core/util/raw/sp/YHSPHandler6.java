package yh.rad.dbexputil.transplant.logic.core.util.raw.sp;

import java.util.HashMap;

import yh.rad.dbexputil.transplant.logic.core.processor.YHSpecialHandler;

public class YHSPHandler6 implements YHSpecialHandler{

  public String cutHtml(){
    return null;
  }
  @Override
  public Object excute(Object value, HashMap<String, String> params)
      throws Exception {
    return cutHtml();
  }
}
