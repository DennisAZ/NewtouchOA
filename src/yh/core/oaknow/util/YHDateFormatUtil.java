package yh.core.oaknow.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import yh.core.util.YHOut;

public class YHDateFormatUtil{
  public static String dateFormat(Date date){
    if(date != null){
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String ds = sdf.format(date);
      //YHOut.println(ds.toString());
      return ds.toString();
    }else{
      return "";
    }    
  }
}
