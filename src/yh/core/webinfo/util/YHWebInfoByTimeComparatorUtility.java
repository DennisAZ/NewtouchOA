package yh.core.webinfo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import yh.core.webinfo.dto.YHWebInfo;

public class YHWebInfoByTimeComparatorUtility implements Comparator{

  @Override
  public int compare(Object arg0, Object arg1) {
    // TODO Auto-generated method stub
    Date date1 = null;
    Date date2 = null;
    try {
      YHWebInfo webInfo1 = (YHWebInfo)arg0;
      YHWebInfo webInfo2 = (YHWebInfo)arg1;
      DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
      date1 = f.parse(webInfo1.getWebInfoDate());
      date2 = f.parse(webInfo2.getWebInfoDate());
      if(date1.after(date2)){
        return 0;
      }
    } catch (ParseException e) {
      // TODO Auto-generated catch block
       e.printStackTrace();
    } 
    return 1;
  }
  
}
