package yh.core.util.net.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import yh.core.util.YHUtility;

public class YHHttpFileUtility {
  public static void main(String[] args) throws Exception {
//    YHHttpFileUtility util = new YHHttpFileUtility();
//    util.downloadFile("localhost",
//        80,
//        "/yh/test/core/act/YHTestFileUploadAct/downloadFile.act",
//        "D:\\tmp\\1.pdf",
//        new HashMap<String, String>());
    
    YHHttpFileUtility util = new YHHttpFileUtility();
    Map<String, String> paramMap = new Hashtable<String, String>();
    paramMap.put("uploadPath", "upload");
    util.uploadFile("localhost", 80,
        "/yh/yh/core/act/YHFileUploadAct/doFileUpload.act",
        "D:\\tmp\\1.pdf", paramMap);
  }
  /**
   * 上传文件
   * @param host           主机
   * @param portNum        端口号
   * @param url            文件接收服务器URL
   * @param inputFile      被上传的文件路径
   * @param paramMap       参数哈希表
   * @return
   * @throws ClientProtocolException
   * @throws IOException
   */
  public String uploadFile(String host,
      int portNum,
      String url,
      String inputFile,
      Map<String,String> paramMap) throws ClientProtocolException, IOException {
    
    //设定目标站点  web的默认端口80可以不写的 当然如果是其它端口就要标明                                                             
    HttpHost httpHost = new HttpHost(host, portNum);
    HttpClient httpclient = new DefaultHttpClient();  
    //请求处理页面  
    HttpPost httppost = new HttpPost(url);  
    //创建待处理的文件  
    FileBody file = new FileBody(new File(inputFile));  
    //对请求的表单域进行填充  
    MultipartEntity reqEntity = new MultipartEntity();  
    reqEntity.addPart("file", file);
    if (paramMap != null && !paramMap.isEmpty()) {
      Iterator<String> iKeys = paramMap.keySet().iterator();
      while (iKeys.hasNext()) {
        String key = iKeys.next();
        String value = YHUtility.null2Empty(paramMap.get(key));
        reqEntity.addPart(key, new StringBody(value));
      }
    }
    //设置请求  
    httppost.setEntity(reqEntity);  
    //执行  
    HttpResponse response = httpclient.execute(httpHost, httppost);  
    if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){  
        HttpEntity entity = response.getEntity();  
        //显示内容  
        if (entity != null) {  
           String rtStr = EntityUtils.toString(entity);
           EntityUtils.consume(entity);
           return rtStr;
        }else {
          return "-2";
        }
    }else { //非正常情况
      return "-1";
    }
  }
  /**
   * 下载文件
   * @param host             主机地址
   * @param portNum          端口号
   * @param url              响应请求的URL
   * @param outputFile       输出文件目录
   * @param paramMap         参数哈希表
   * @throws ClientProtocolException
   * @throws IOException
   */
  public void downloadFile(String host,
      int portNum,
      String url,
      String outputFile,
      Map<String,String> paramMap) throws ClientProtocolException, IOException {  
    
    //实例化一个HttpClient  
    HttpClient httpClient = new DefaultHttpClient();  
    //设定目标站点  web的默认端口80可以不写的 当然如果是其它端口就要标明                                                             
    HttpHost httpHost = new HttpHost(host, portNum);  
    //设置需要下载的文件  
    HttpPost httpPost = buildPost(url, paramMap);  
    //这里也可以直接使用httpGet的绝对地址，当然如果不是具体地址不要忘记/结尾       
    HttpResponse response = httpClient.execute(httpHost, httpPost);  
    if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){  
        //请求成功  
        //取得请求内容  
        HttpEntity entity = response.getEntity();           
        //显示内容  
        if (entity != null) {
            //设置本地保存的文件  
            File storeFile = new File(outputFile);
            FileOutputStream output = new FileOutputStream(storeFile);  
            //得到网络资源并写入文件  
            InputStream input = entity.getContent();  
            byte buff[] = new byte[1024];  
            int len = 0;  
            while((len = input.read(buff)) > 0){  
                output.write(buff, 0, len);  
            }  
            output.flush();  
            output.close();   
        }  
        if (entity != null) {
          EntityUtils.consume(entity);
        }  
    } 
  }
  
  private HttpPost buildPost(String url,
      Map<String, String> paramMap) throws UnsupportedEncodingException {
    //设置需要下载的文件  
    HttpPost httpPost = new HttpPost(url);  
    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
    if (paramMap != null && !paramMap.isEmpty()) {
      Iterator<String> iKeys = paramMap.keySet().iterator();
      while (iKeys.hasNext()) {
        String key = iKeys.next();
        String value = YHUtility.null2Empty(paramMap.get(key));
        nvps.add(new BasicNameValuePair(key, value));
      }
    }
    httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
    return httpPost;
  }
}
