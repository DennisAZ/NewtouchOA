package yh.core.oaknow.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import yh.core.util.YHOut;

/**
 * oa知道访问计数
 * @author qwx110
 *
 */
public class YHCountUtil{
  private static int  count = 0;
  public static final String FILE_PATH ="count.txt";
  private static final YHCountUtil cu= new YHCountUtil();
  
  public synchronized int readCount(HttpServletRequest request) throws IOException{
    //String path = this.getClass().getClassLoader().getResource("").getPath();
    String sp = System.getProperty("file.separator");
    String path = request.getSession().getServletContext().getRealPath(sp)+ "core" + sp + "oaknow" +sp;   
    File file = new File(path + FILE_PATH);
    if(!file.exists()){
       file.createNewFile();
       file.setReadable(true);
       file.setWritable(true);
    }else{
       if(!file.canRead()){
          file.setReadable(true);
       }else if(!file.canWrite()){
          file.setWritable(true);
       }
    }
    FileInputStream fis =  new FileInputStream(path+FILE_PATH);
    byte[] b = new byte[20];
    int i = fis.read(b);
    fis.close();
    String temp = "";
    if(i == -1){
      temp = "0";
    }else{
     temp = new String(b,0,i);
    }
    //YHOut.println(temp);
    count = Integer.parseInt(temp) +1;
    FileOutputStream fos = new FileOutputStream(path+FILE_PATH);
    fos.write(String.valueOf(count).getBytes());
    fos.close();
    return count;
  }
  
  public static YHCountUtil getInstance(){
    return cu;
  }
  
}
