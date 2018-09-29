package yh.cms.velocity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;


public class YHvelocityUtil{

  public static void velocity(Map request, String url, String templateName,
      String templateUrl){
    // 获取模板引擎
    VelocityEngine ve = new VelocityEngine();
    // 模板文件所在的路径
    // String path = System.getProperty("user.dir") + SUB_PATH;
    // String path = System.getProperty("user.dir") + SUB_PATH;
    //templateUrl = "D:\\project\\yh\\templates\\db";
    // 设置参数
    //System.out.println(templateUrl);
    ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templateUrl);
    // 处理中文问题
    ve.setProperty(Velocity.ENCODING_DEFAULT, "utf-8");
    ve.setProperty(Velocity.INPUT_ENCODING, "utf-8");
    ve.setProperty(Velocity.OUTPUT_ENCODING, "utf-8");
    Writer wt = null;
    try{
      // 初始化模板

      ve.init();
      // 获取模板(template.vm)
      Template template = ve.getTemplate(templateName);
      // 获取上下文

      VelocityContext root = new VelocityContext();
      // 把数据填入上下文
      // String className = ((String[])request.get("className"))[0];
      
      Iterator<String> keyIter = request.keySet().iterator();
      while (keyIter.hasNext()){
        String key = keyIter.next();
        Object value = null;
        try {
          value = ((String[])request.get(key))[0];
        }catch (Exception e){
            value = request.get(key);
        }
        root.put(key, value);
      }
      // 输出路径
      String outpath = url + "\\" + request.get("fileName");
      File dir = new File(url);
      if (!dir.exists()){
        dir.mkdirs();
      }
      File file = new File(outpath);
      if (!file.exists()){
        file.createNewFile();
      }
      FileOutputStream fs = new FileOutputStream(file);
      wt = new PrintWriter(new OutputStreamWriter(fs, "UTF-8"));
      template.merge(root, wt);
      wt.flush();
    } catch (Exception e){
      e.printStackTrace();
    } finally{
      if (wt != null){
        try{
          wt.close();
        } catch (IOException e){
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }
}
