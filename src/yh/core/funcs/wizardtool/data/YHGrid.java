package yh.core.funcs.wizardtool.data;

import java.util.Map;

public class YHGrid {
  public void loadToHtml(String id , String parameters
      , StringBuffer sb ,Map libMap){
    libMap.put("YHGrid", "/rad/grid/grid.css,/rad/grid/grid.js");
    sb.append("\nvar " + id + " = new YHGrid(); \n" + id + ".create(" + parameters + ");"); 
  }
}
