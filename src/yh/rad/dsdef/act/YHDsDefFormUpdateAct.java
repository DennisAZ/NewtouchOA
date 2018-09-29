package yh.rad.dsdef.act;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import yh.core.data.YHDsTable;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHBeanKeys;
import yh.rad.dsdef.logic.YHDsDefLogic;

public class YHDsDefFormUpdateAct {
  private static Logger log = Logger.getLogger(YHDsDefFormUpdateAct.class);

  public Object build(HttpServletRequest request, String classTable) throws Exception {
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
    Class classTypeTable = Class.forName(classTable);
    String tableNo = request.getParameter("tableNoDiv");
    String tableNoNew = request.getParameter("tableNo");
   int idN = Integer.parseInt(request.getParameter("id"));
//    for(int x=1;x<=idN;x++){
//      //int seqId = x;
//      String dseqId = request.getParameter("seqid"+"_"+x);
//      System.out.println(dseqId+":"+"mmmmmmmmmmmmmmmmmmmmmmmm");
//    }
//    String tableNo1  = request.getParameter("tableNo");
//    YHDsDefFormMoreAct dm = new YHDsDefFormMoreAct();
//    String classField = (String) request.getParameter("YHDsField");
//    System.out.println(classField+"yyyyyyyyyyyyyyyyyyyy");
//    dm.build(request, classField, idN);
 //   System.out.println(tableNo+"++++++++");
    
    YHDsTable obj = (YHDsTable) classTypeTable.newInstance();
    Field[] fields = classTypeTable.getDeclaredFields();
    Object valueSet = null;
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      Object objo=null;
      String strg = "get";
      if (field.getType().equals(Boolean.TYPE)) {
        strg = "is";
      }
      String fieldN = field.getName();
      //System.out.println(fieldN+"lllllllllllllllllllllllll");
      String valueStr = request.getParameter(fieldN);
      //System.out.println(valueStr+"ddddddddddddddddddd");
      
      if (field.getType().equals(Integer.TYPE)) {
        if (valueStr.equals("")) {
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
      Method getMethod = classTypeTable.getMethod(getName);
      //System.out.println(getMethod);
      Method setMethod = classTypeTable.getMethod(setName, new Class[] { field
          .getType() });
      valueSet = setMethod.invoke(obj, new Object[] { objo });
      Object value = getMethod.invoke(obj);
      //System.out.println(fieldN + " : " + value);
    }
    YHDsDefLogic ddl = new YHDsDefLogic();
    ddl.DsDefUpdateTable(dbConn, tableNo, obj, tableNoNew);
    //ddl.DsDefInsert(dbConn, obj);
    //ddl.DsDefInsertZ(dbConn, tableNo, obj);
    return obj;
  }
}
