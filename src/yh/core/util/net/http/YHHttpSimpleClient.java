package yh.core.util.net.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 简单Http代理类，本类没有考虑获取效率和编码重定向等复杂的问题，适用于不是很频繁地少量获取其他服务器资源场合
 * @author jpt
 * @date 2012-02-19
 */
public class YHHttpSimpleClient {
  //主机地址
  private String host = null;
  //端口
  private int port = 80;
  
  /**
   * 缺省构造方法
   */
  public YHHttpSimpleClient() {
  }
  /**
   * 构造方法
   * @param host  主机地址
   */
  public YHHttpSimpleClient(String host) {
    this.host = host;
  }
  /**
   * 构造方法
   * @param host  主机地址
   * @param port  服务端口
   */
  public YHHttpSimpleClient(String host, int port) {
    this.host = host;
    this.port = port;
  }
  
  /**
   * Get方法
   * @param url  资源地址，如果是"http://"打头，则认为是绝对地址，否则是相对地址
   * @param outResponse  输出响应
   * @throws Exception
   */
  public void get(String url, HttpServletResponse outResponse) throws Exception  {
    execute(url, false, null, outResponse);
  }
  
  /**
   * Post方法
   * @param url  资源地址，如果是"http://"打头，则认为是绝对地址，否则是相对地址
   * @param outResponse  输出响应
   * @throws Exception 
   */
  public void post(String url, HttpServletResponse outResponse) throws Exception  {
    execute(url, true, null, outResponse);
  }
  
  /**
   * Post方法
   * @param url  资源地址，如果是"http://"打头，则认为是绝对地址，否则是相对地址
   * @param nvps  其他参数
   * @param outResponse  输出响应
   * @throws Exception
   */
  public void post(String url, List<NameValuePair> nvps, HttpServletResponse outResponse) throws Exception  {
    execute(url, true, nvps, outResponse);
  }
  
  /**
   * 通用方法
   * @param path  资源地址，如果是"http://"打头，则认为是绝对地址，否则是相对地址
   * @param isPost  是否是Post方法
   * @param nvps  其他参数
   * @param outResponse  输出响应
   * @throws Exception
   */
  private void execute(String path, boolean isPost, List<NameValuePair> nvps, HttpServletResponse outResponse) throws Exception {
    String url = null;
    if (path.indexOf("http://") == 0) {
      url = path;
    }
    if (url == null) { 
      url = "http://" + host;
      if (port != 80) {
        url += ":" + port;
      }
      if (path.charAt(0) == '/') {
        url += path;
      }else {
        url += "/" + path;
      }
    }
    DefaultHttpClient httpClient = new DefaultHttpClient();
    try {
      HttpResponse response = null;
      if (isPost) {
        HttpPost httppost = new HttpPost(url);
        if (nvps != null) {
          httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        }
        System.out.println("executing request" + httppost.getRequestLine());
        response = httpClient.execute(httppost);
      }else {
        HttpGet httpget = new HttpGet(url);
        System.out.println("executing request" + httpget.getRequestLine());
        response = httpClient.execute(httpget);
      }
      HttpEntity entity = response.getEntity();
      Header[] headers = response.getAllHeaders();
      if (headers != null) {
        for (int i = 0; i < headers.length; i++) {
          Header header = headers[i];
          outResponse.setHeader(header.getName(), header.getValue());
        }
      }

      System.out.println("----------------------------------------");
      System.out.println(response.getStatusLine());
      if (entity != null) {
        System.out.println("Response content length: "
            + entity.getContentLength());
        outResponse.setContentLength((int)entity.getContentLength());
        Header encoding = entity.getContentEncoding();
        System.out.println(encoding);
        if (encoding != null) {
          outResponse.setCharacterEncoding(encoding.getValue());
        }
        String contentType = entity.getContentType().getValue();
        System.out.println(contentType);
        if (contentType != null && contentType.length() > 0) {
          outResponse.setContentType(contentType);
        }
        byte[] buff = new byte[1024];
        int readLen = 0;
        InputStream in = null;
        OutputStream out = null;
        try {
          in = entity.getContent();
          out = outResponse.getOutputStream();
          while ((readLen = in.read(buff)) > 0) {
            out.write(buff, 0, readLen);
          }
          out.flush();
        }catch(Exception ex) {
        }finally {
          if (in != null) {
            try {
              in.close();
            }catch(Exception ex) {          
            }
          }
          if (out != null) {
            try {
              out.close();
            }catch(Exception ex) {          
            }
          }
        }
      }
      EntityUtils.consume(entity);
    } finally {
      httpClient.getConnectionManager().shutdown();
    }
  }
}
