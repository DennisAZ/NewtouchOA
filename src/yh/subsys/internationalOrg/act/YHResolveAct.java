package yh.subsys.internationalOrg.act;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import  yh.subsys.internationalOrg.logic.YHResolveLogic;
public class YHResolveAct {
public String getProjectById(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
  request.setCharacterEncoding("utf-8");
  Connection dbConn = null;
  YHResolveLogic logic =  new YHResolveLogic();
  try {
    YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
//   String pars = request.getParameter("pars");
//    String pathss = pars.replace("\\", "/");
    File input = new File("D:/模板/wqs-a.html");
    Document doc = Jsoup.parse(input, "gb2312", "http://example.com/");
    String title = doc.title(); 
    Elements newsHeadlines = doc.select("a");
    Elements div = doc.select("[bgcolor=#CCCCCC] div");
    System.out.println(div.html());
    Elements div1 = doc.select(".text-1");
   for(int i=0;i<2;i++){
     if(4*i+3<div1.size()){
       System.out.println(div1.get(4*i+1).text());
       System.out.println(div1.get(4*i+2).text());
       System.out.println(div1.get(4*i+3).text());
       String hrefpath = div1.get(4*i+3).select("a").attr("href").substring(0,4);
       //解析二级菜单
        File input1 = new File("D:/模板/"+div1.get(4*i+3).select("a").attr("href"));
        Document doc1 = Jsoup.parse(input1, "gb2312", "http://example.com/");
        Elements aHref = doc1.select("strong a");
        //解析三级菜单
        if(aHref.size()>0){
        File input2 = new File("D:/模板/"+hrefpath+"/"+aHref.get(0).select("a").attr("href"));
        Document doc2 = Jsoup.parse(input2, "gb2312", "http://example.com/");
        String src = doc2.select("img").attr("src");
        String embed = doc2.select("embed").attr("src");
        String pText = doc2.select("P").html().replace("<br />","\n");
        if(!YHUtility.isNullorEmpty(src)){
          String[] doc3Id = logic.getNewAttachPath(src, "concierge");
          String path =  doc3Id[1];
          System.out.println(path);
          byte b[] = src.getBytes();  
          ByteArrayInputStream bais = new ByteArrayInputStream(b);  
          POIFSFileSystem fs = new POIFSFileSystem();  
          DirectoryEntry directory = fs.getRoot();  
          //生成word   
          directory.createDocument("WordDocument", bais);  
          FileOutputStream ostream = new FileOutputStream(path);  
          fs.writeFilesystem(ostream);  
          bais.close();  
          ostream.close(); 
          System.out.println(src);
        }
        else if(!YHUtility.isNullorEmpty(src)){
          System.out.println(embed);
        }
        else {
          System.out.println(pText);
          }
        }
        if(aHref.size()>1){
          File input3 = new File("D:/模板/"+hrefpath+"/"+aHref.get(1).select("a").attr("href"));
          Document doc3 = Jsoup.parse(input3, "gb2312", "http://example.com/");
          String pText = doc3.select("p").html().replace("<br />","\n");
          String embed = doc3.select("embed").attr("src");
       String[] doc3Id = logic.getNewAttachPath(doc3.title()+".doc", "concierge");
          String path =  "E:/"+div1.get(4*i+3).text()+doc3.title()+".doc";
          if(!YHUtility.isNullorEmpty(embed)){
            System.out.println(embed);
          }
          else {
            //写入doc
            byte b[] = pText.getBytes();  
            ByteArrayInputStream bais = new ByteArrayInputStream(b);  
            POIFSFileSystem fs = new POIFSFileSystem();  
            DirectoryEntry directory = fs.getRoot();  
            //生成word   
            directory.createDocument("WordDocument", bais);  
            FileOutputStream ostream = new FileOutputStream(path);  
            fs.writeFilesystem(ostream);  
            bais.close();  
            ostream.close(); 
            }
        }
         if(aHref.size()>2){
          File input4 = new File("D:/模板/"+hrefpath+"/"+aHref.get(2).select("a").attr("href"));
          Document doc4 = Jsoup.parse(input4, "gb2312", "http://example.com/");
          String pText = doc4.select("p").html().replace("<br />","\n");
          String path =  "E:/"+div1.get(4*i+3).text()+doc4.title()+".doc";
          byte b[] = pText.getBytes();  
          ByteArrayInputStream bais = new ByteArrayInputStream(b);  
          POIFSFileSystem fs = new POIFSFileSystem();  
          DirectoryEntry directory = fs.getRoot();  
          //生成word   
          directory.createDocument("WordDocument", bais);  
          FileOutputStream ostream = new FileOutputStream(path);  
          fs.writeFilesystem(ostream);  
          bais.close();  
          ostream.close(); 
        }
//         if(aHref.size()>3){
//          File input5 = new File("D:/模板/"+aHref.get(3).select("a").attr("href"));
//          Document doc5 = Jsoup.parse(input5, "gb2312", "http://example.com/");
//          String pText = doc5.select("p").html().replace("<br />","\n");
//          String path =  "E:/"+div1.get(4*i+3).text()+doc5.title()+".doc";
//          byte b[] = pText.getBytes();  
//          ByteArrayInputStream bais = new ByteArrayInputStream(b);  
//          POIFSFileSystem fs = new POIFSFileSystem();  
//          DirectoryEntry directory = fs.getRoot();  
//          //生成word   
//          directory.createDocument("WordDocument", bais);  
//          FileOutputStream ostream = new FileOutputStream(path);  
//          fs.writeFilesystem(ostream);  
//          bais.close();  
//          ostream.close(); 
//        }
//         if(aHref.size()>4){
//          File input6 = new File("D:/模板/"+aHref.get(4).select("a").attr("href"));
//          Document doc6 = Jsoup.parse(input6, "gb2312", "http://example.com/");
//          String pText = doc6.select("p").html().replace("<br />","\n");
//          String path =  "E:/"+div1.get(4*i+3).text()+doc6.title()+".doc";
//          byte b[] = pText.getBytes();  
//          ByteArrayInputStream bais = new ByteArrayInputStream(b);  
//          POIFSFileSystem fs = new POIFSFileSystem();  
//          DirectoryEntry directory = fs.getRoot();  
//          //生成word   
//          directory.createDocument("WordDocument", bais);  
//          FileOutputStream ostream = new FileOutputStream(path);  
//          fs.writeFilesystem(ostream);  
//          bais.close();  
//          ostream.close(); 
//        }
//         if(aHref.size()>5){
//          File input7 = new File("D:/模板/"+aHref.get(5).select("a").attr("href"));
//          Document doc7 = Jsoup.parse(input7, "gb2312", "http://example.com/");
//          String pText = doc7.select("p").html().replace("<br />","\n");
//          String path =  "E:/"+div1.get(4*i+3).text()+doc7.title()+".doc";
//          byte b[] = pText.getBytes();  
//          ByteArrayInputStream bais = new ByteArrayInputStream(b);  
//          POIFSFileSystem fs = new POIFSFileSystem();  
//          DirectoryEntry directory = fs.getRoot();  
//          //生成word   
//          directory.createDocument("WordDocument", bais);  
//          FileOutputStream ostream = new FileOutputStream(path);  
//          fs.writeFilesystem(ostream);  
//          bais.close();  
//          ostream.close(); 
//        }
//         if(aHref.size()>6){
//          File input8 = new File("D:/模板/"+aHref.get(6).select("a").attr("href"));
//          Document doc8 = Jsoup.parse(input8, "gb2312", "http://example.com/");
//          String pText = doc8.select("p").html().replace("<br />","\n");
//          String path =  "E:/"+div1.get(4*i+3).text()+doc8.title()+".doc";
//          byte b[] = pText.getBytes();  
//          ByteArrayInputStream bais = new ByteArrayInputStream(b);  
//          POIFSFileSystem fs = new POIFSFileSystem();  
//          DirectoryEntry directory = fs.getRoot();  
//          //生成word   
//          directory.createDocument("WordDocument", bais);  
//          FileOutputStream ostream = new FileOutputStream(path);  
//          fs.writeFilesystem(ostream);  
//          bais.close();  
//          ostream.close(); 
//        }
//         if(aHref.size()>7){
//          File inpuyh = new File("D:/模板/"+aHref.get(7).select("a").attr("href"));
//          Document doc9 = Jsoup.parse(inpuyh, "gb2312", "http://example.com/");
//          String pText = doc9.select("p").html().replace("<br />","\n");
//          String path =  "E:/"+doc9.title()+".doc";
//          byte b[] = pText.getBytes();  
//          ByteArrayInputStream bais = new ByteArrayInputStream(b);  
//          POIFSFileSystem fs = new POIFSFileSystem();  
//          DirectoryEntry directory = fs.getRoot();  
//          //生成word   
//          directory.createDocument("WordDocument", bais);  
//          FileOutputStream ostream = new FileOutputStream(path);  
//          fs.writeFilesystem(ostream);  
//          bais.close();  
//          ostream.close(); 
//        }
//        if(aHref.size()>8){
//          File input10 = new File("D:/模板/"+aHref.get(8).select("a").attr("href"));
//          Document doc10 = Jsoup.parse(input10, "gb2312", "http://example.com/");
//          String pText = doc10.select("p").html().replace("<br />","\n");
//          String path =  "E:/"+div1.get(4*i+3).text()+doc10.title()+".doc";
//          byte b[] = pText.getBytes();  
//          ByteArrayInputStream bais = new ByteArrayInputStream(b);  
//          POIFSFileSystem fs = new POIFSFileSystem();  
//          DirectoryEntry directory = fs.getRoot();  
//          //生成word   
//          directory.createDocument("WordDocument", bais);  
//          FileOutputStream ostream = new FileOutputStream(path);  
//          fs.writeFilesystem(ostream);  
//          bais.close();  
//          ostream.close(); 
//        }
       // }
   }
   }
    // }
  // }
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
  }catch(Exception ex) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
    throw ex;
  }
  return "/core/inc/rtjson.jsp";
}
}