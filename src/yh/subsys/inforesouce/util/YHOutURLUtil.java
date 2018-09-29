package yh.subsys.inforesouce.util;

/**
 * 访问其他网络
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author qwx
 *
 */
public class YHOutURLUtil {
  
  /**
   * 返回给定的网站的返回值
   * @param url
   * @return
   * @throws Exception
   */
  public static String getContent(String url) throws Exception{
	  StringBuffer result = new StringBuffer();
	  InputStream inputstream = null;
	  BufferedReader reader =  null;
	  HttpURLConnection connection = null;
	  try {
		  URL uRl = new URL(url);
		  connection = (HttpURLConnection) uRl.openConnection();
		  connection.setRequestMethod("GET");
		  connection.setRequestProperty("Content-type", "text/html");
		  connection.setRequestProperty("Accept-Charset", "UTF-8");
		  connection.setRequestProperty("contentType", "UTF-8");
		  connection.connect();
		  inputstream = connection.getInputStream();
		  reader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
		  String temp = "";
		  while((temp =reader.readLine()) != null){
			  result.append(temp);
		  }		 
	} catch (IOException e) {
		throw e;
	}finally{
		if(reader != null){
		 reader.close();	
		}
		if(inputstream != null){
		 inputstream.close();		  
		}		 
		if(connection != null ){
		  connection.disconnect();
		}
	}
	return result==null?null:result.toString();
}
  //test
  public static void main(String[] args){
	  String url = "http://192.168.0.126:9000/BjfaoWeb/TitleSign/GetKeyword?KeyIDs=10,11,10,9";
	  String content;
	try {
		content = YHOutURLUtil.getContent(url);
		//System.out.println(content);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
  }
}
