package yh.rad.dsdef.logic;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
public class YHDsDefJsonlogic {
  /**
   * log
   */
  private static Logger log = Logger.getLogger(YHDsDefJsonlogic.class);
  
  public StringBuffer toJson(Object obj) throws Exception {
    Class cla = obj.getClass();
    Field[] fields = cla.getDeclaredFields();
    StringBuffer json = new StringBuffer(" {");
    //System.out.println("fields.length------"+fields.length);
    for (int i = 0; i < fields.length; i++) {
      //System.out.println("3333333344: "+i);
      Field field = fields[i];
      String fieldName = field.getName();
      String stringLetter = fieldName.substring(0, 1).toUpperCase();
      String getName = "get" + stringLetter + fieldName.substring(1);
      Method getMethod = cla.getMethod(getName);
      Object value = getMethod.invoke(obj);
      if (obj == null) {
        json.append("null");
      }
      json.append("\"").append(fieldName).append("\"").append(":");
      //System.out.println(value+"-----value----"+i);
      if (value == null||value.equals("")) {
        json.append("\"").append("").append("\"");
      } else {
        json.append("\"").append(value).append("\"");
      }
      //json.append("\"").append(value).append("\"");
      if(i < fields.length-1){
        json.append(",");
      }
    }
    json.append("}");
    return json;
  }
}
