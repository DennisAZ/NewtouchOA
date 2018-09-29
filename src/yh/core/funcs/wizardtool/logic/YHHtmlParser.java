package yh.core.funcs.wizardtool.logic;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class YHHtmlParser {
  private String dataPackageName = "yh.core.funcs.wizardtool.data.YH";
  private StringBuffer onloadJs = new StringBuffer("function doInit(){");
  private Map<String,String> libMap = new HashMap();
  public String parseToHtml(String html,String contextPath,Map data) throws Exception{
    StringReader rs = new StringReader("<body onload='doInit()'>"+ html + "</body>");
    SAXReader saxReader = new SAXReader();   
    StringBuffer htmlSb = new StringBuffer("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
    htmlSb.append("\n<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title></title>");
    try {
      
      Document document = saxReader.read(rs);
      
      Element root = document.getRootElement();   
      this.getElement(root,data);
      this.setSystemLib(htmlSb, contextPath);
      for(String libs : libMap.values()){
        String[] libStrs = libs.split(","); 
        for(String libStr : libStrs){
          if(libStr.endsWith(".js")){
            htmlSb.append("\n<script type='text/javascript' src='" + contextPath + libStr + "'></script>");
          }else if(libStr.endsWith(".css")){
            htmlSb.append("\n<link rel='stylesheet' href ='" + contextPath + libStr + "'>");
          }
        }
      }
      htmlSb.append("\n<script type='text/javascript'>");
      htmlSb.append(this.onloadJs);
      htmlSb.append("\n}</script>\n</head>");
      String body = this.docToString(document).toString();
      body = body.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".length());
      htmlSb.append(body);
      htmlSb.append("</html>");
      
      html = htmlSb.toString().replaceAll("&amp;", "&");
     
    } catch (DocumentException e) {   
      throw e;  
    }   
    return html;
  }
  public ByteArrayOutputStream docToString(Document document) throws Exception{
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try{
      OutputFormat format = new OutputFormat(" ",true,"UTF-8");
      XMLWriter writer = new XMLWriter(out,format);
      writer.write(document);
      
    }catch(Exception ex){
      throw ex;
    }
    return out;
  }
  public void getElement(Element element,Map data) throws Exception, Exception, Exception {
    Attribute a = element.attribute("widgettype");
    if(a != null){
      String widgetType = a.getValue();
      String id = element.attributeValue("id");
      if(data.containsKey(id + "Data")){
        String parameters = (String) data.get(id + "Data");
        try{
          Object obj = Class.forName(dataPackageName  + widgetType).newInstance();
          Method m = obj.getClass().getMethod("loadToHtml", new Class[]{String.class,String.class,StringBuffer.class,Map.class});
          m.invoke(obj, new Object[]{id,parameters, this.onloadJs, this.libMap});
        }catch(ClassNotFoundException ex){
        }
      }
      Element parent = element.getParent();
      parent.remove(element);
      return ;
    }
    List elements = element.elements();
    if (elements.size() == 0) {
        return ;
    } else {
        for (Iterator it = elements.iterator(); it.hasNext();) {
            Element elem = (Element) it.next();
            getElement(elem,data);
        }
    }
  }
  public void setSystemLib(StringBuffer sb,String contextPath){
    sb.append("\n<meta http-equiv='Content-Type' content='text/html'; charset='UTF-8'>");
    sb.append("\n<script type='text/javascript'>");
    sb.append("\nvar TDJSCONST = {YES: 1,NO: 0};");
    sb.append("\nvar contextPath = '" + contextPath + "';");
    sb.append("\nvar imgPath = '" + contextPath + "/core/styles/style1/img';");
    sb.append("\n</script>");
    sb.append("\n<link rel='stylesheet' href ='" + contextPath +"/core/styles/style1/css/style.css'>");
    sb.append("\n<script type='text/javascript' src='"+ contextPath +"/core/js/datastructs.js'></script>");
    sb.append("\n<script type='text/Javascript' src='"+ contextPath +"/core/js/sys.js' ></script>");  
    sb.append("\n<script type='text/Javascript' src='"+ contextPath +"/core/js/prototype.js' ></script>"); 
    sb.append("\n<script type='text/Javascript' src='"+ contextPath +"/core/js/smartclient.js' ></script>"); 
  }
  public static void main(String[] args){
    YHHtmlParser htmlParser = new YHHtmlParser();
    Map libMap = new HashMap();
    libMap.put("ffData", "{treeId:'ff',bindToContainerId:'',requestUrl:'',isOnceLoad:false,treeStructure:{isNoTree:false}}");
    String html = "<p>&amp;nbsp;,<img style='width: 20px; height: 17px;' src='/yh/raw/lh/fckeditor/editor/plugins/DtreeWidget/dtree.jpg' id='ff' widgettype='DTree' alt='' /></p>";
    try {
      htmlParser.parseToHtml(html,"/yh", libMap);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
   
  }
}
