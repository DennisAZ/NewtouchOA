package yh.core.oaknow.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class YHAjaxUtil{
  
  public static void ajax(int id,  HttpServletResponse response) throws Exception{
    PrintWriter pw = response.getWriter();
    if(id != 0){
      String rtData = "{rtState:'0'}";
      pw.println(rtData);       
    }else{
      String rtData = "{rtState:'1'}";
      pw.println(rtData);
    }
    pw.flush();
  }
  
  public static void ajax(String str, HttpServletResponse response) throws Exception{
    PrintWriter pw = response.getWriter();    
    String rtData = "{rtData:"+str+"}";
    pw.println(rtData);    
    pw.flush();
  }
}
