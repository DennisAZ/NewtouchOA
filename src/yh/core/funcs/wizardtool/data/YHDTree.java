package yh.core.funcs.wizardtool.data;

import java.util.Map;

public class YHDTree {
  public void loadToHtml(String id , String parameters
                                  , StringBuffer sb ,Map libMap){
    libMap.put("DTree", "/core/js/cmp/DTree1.0.js");
    sb.append("\nvar " + id + " = new DTree(" + parameters + ");");
    sb.append("\n" + id + ".show();");
  }
}
