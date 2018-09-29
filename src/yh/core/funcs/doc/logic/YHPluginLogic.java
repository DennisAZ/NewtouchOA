package yh.core.funcs.doc.logic;

import java.sql.Connection;
import java.util.Date;
import java.util.Map;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHPluginLogic {
  public StringBuffer getSelectData( Connection conn, Map request , String findStr , String sql)
  throws Exception { 
StringBuffer resualt = new StringBuffer();
String query = "";
try {
  if (!YHUtility.isNullorEmpty(findStr)) {
    String[] items = findStr.split(",");
    for (String tmp : items) {
      if (!YHUtility.isNullorEmpty(tmp)){
        String[] value = (String[])request.get(tmp);
        if (value != null && value.length > 0){
          if (!YHUtility.isNullorEmpty(value[0])) {
            query += " and " + tmp + " like '%" + YHUtility.encodeLike(value[0]) + "%' " + YHDBUtility.escapeLike() ;
          }
        }
      }
    }
  }
  sql += query;
  YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
  YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
  for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
    YHDbRecord record = pageDataList.getRecord(i);
    String values = "";
    int fieldCnt = record.getFieldCnt();
    for (int j= 0 ;j < fieldCnt; j++) {
      Object colObj =  record.getValueByIndex(j);
      String val = "";
      if (colObj != null) {
        Class fieldType = colObj.getClass(); 
        if (Integer.class.equals(fieldType)) {        
          val = String.valueOf(((Integer)colObj).intValue());
        }else if (Long.class.equals(fieldType)) {        
          val = String.valueOf(((Long)colObj).longValue());
        }else if (Double.class.equals(fieldType)) {        
          val = YHUtility.getFormatedStr(((Double)colObj).doubleValue(), YHUtility.WITHOUTGROUP);
        }else if (Date.class.equals(fieldType)) {
          val =  YHUtility.getDateTimeStr((Date)colObj);
        }else {
          if (colObj == null) {
            val = "";
          }else {
            String tmpStr = YHUtility.null2Empty(colObj.toString());
            tmpStr = tmpStr.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "").replace("\'", "\\\'");
            val = tmpStr;
          }
        }
        values += val + "++";
      }
      record.addField("value", values);
    }
  }
  resualt.append(pageDataList.toJson());
} catch (Exception ex) {
  ex.printStackTrace();
  throw ex;
}
return resualt;
}
}
