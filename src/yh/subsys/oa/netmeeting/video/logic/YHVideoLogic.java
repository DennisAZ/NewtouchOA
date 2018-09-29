package yh.subsys.oa.netmeeting.video.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class YHVideoLogic {
  
  public static String send(String urlAddr, String sendData) throws Exception {    
    HttpURLConnection conn = null;     
    StringBuffer sb = new StringBuffer("");
    StringBuffer params = new StringBuffer();    
    params.append(sendData);
          
    try{    
      URL url = new URL(urlAddr);    
      conn = (HttpURLConnection)url.openConnection();    
 
      conn.setDoOutput(true);    
      conn.setRequestMethod("POST");    
      conn.setUseCaches(false);    
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");    
      conn.setRequestProperty("Content-Length", String.valueOf(params.length()));    
      conn.setDoInput(true);    
      conn.connect();    
 
      OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");    
      out.write(params.toString());    
      out.flush();    
      out.close();    
    
      int code = conn.getResponseCode();
      if (code != 200) {    
        System.out.println("ERROR===" + code);    
      } 
      else {    
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String s = "";
        while((s = br.readLine()) != null){     
          sb.append( s );    
        }
        br.close();
      }    
    }catch(Exception e){   
      throw e;  
    }finally{    
      conn.disconnect();    
    }    
    return sb.toString();  
  } 
}
