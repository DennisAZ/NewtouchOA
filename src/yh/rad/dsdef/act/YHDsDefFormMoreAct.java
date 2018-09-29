package yh.rad.dsdef.act;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import yh.core.data.YHDsField;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHBeanKeys;
import yh.core.util.YHUtility;
import yh.rad.dsdef.logic.YHDsDefLogic;

public class YHDsDefFormMoreAct {
  private static Logger log = Logger.getLogger(YHDsDefFormMoreAct.class);

  public Object build(HttpServletRequest request, String classField,int idName)
      throws Exception {

    Class classTypeField = Class.forName(classField);
    String tableNo = request.getParameter("tableNo");
    
    YHDsField obj = (YHDsField) classTypeField.newInstance();
    Field[] fields = classTypeField.getDeclaredFields();
    Object valueSet = null;
    for (int j = 0; j < idName; j++) {
      for (int i = 0; i < fields.length; i++) {
        Field field = fields[i];
        Object objo;
        String strg = "get";
        if (field.getType().equals(Boolean.TYPE)) {
          strg = "is";
        }
        String valueStr = "";
        String fieldN = field.getName();
        //System.out.println(fieldN+"ooooooooo");
        valueStr = request.getParameter(fieldN + "_" + j);
        //System.out.println(fieldN + "_" + j);
        //System.out.println(valueStr+"ooooooooo");
        if (field.getType().equals(Integer.TYPE)) {
          if (valueStr==null||valueStr.equals("")) {
            objo = new Integer(0);
          } else {
            objo = Integer.valueOf(valueStr);
          }
        } else if (field.getType().equals(Float.TYPE)) {
          objo = Float.valueOf(valueStr);
        } else if (field.getType().equals(Double.TYPE)) {
          objo = Double.valueOf(valueStr);
        } else {
          objo = valueStr;
        }
        String stringLetter = fieldN.substring(0, 1).toUpperCase();
        String getName = strg + stringLetter + fieldN.substring(1);
        String setName = "set" + stringLetter + fieldN.substring(1);

        Method getMethod = classTypeField.getMethod(getName);
        Method setMethod = classTypeField.getMethod(setName, new Class[] { field.getType() });
        valueSet = setMethod.invoke(obj, new Object[] { objo });
        Object value = getMethod.invoke(obj);
        //System.out.println(fieldN + " : " + value);
      }
      YHDsDefLogic ddl = new YHDsDefLogic();
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      if(!YHUtility.isNullorEmpty(obj.getFieldNo())){
        ddl.DsDefInsertZ(dbConn, tableNo, obj);
      }
    }
    return obj;
  }
  // public static void main(String args[]) throws Exception{
  // Class clzz = Class.forName("yh.core.data.YHDsField");
  // Method setMethod = clzz.getMethod("setFieldPrecision", new
  // Class[]{Integer.class});
  // Object obj = clzz.newInstance();
  // setMethod.invoke(obj, new Object[] { null });
  // YHDsField t = (YHDsField)obj;
  // if(t.getDefaultValue()==null){
  // System.out.print("dd");
  // }
  //   
  // }
}
