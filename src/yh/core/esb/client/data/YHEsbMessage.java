package yh.core.esb.client.data;

import java.io.Serializable;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yh.core.esb.client.logic.YHEsbClientUtility;
import yh.core.esb.client.logic.YHObjectUtility;
import yh.core.util.YHUtility;

public class YHEsbMessage implements Serializable {
  public String message;
  public String type;
  public String data;
  
  public static final String contentKey = "content";
  public static final String typeKey = "type";
  public static final String dataKey = "data";
  public static final String messageKey = "message";
  public static String KEY_MESSAGE_FILE = "message.xml";
  
  
  public String toXml() {
    String ms = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      + "<"+messageKey+"><"+contentKey+">"+YHUtility.null2Empty(message)+"</"+contentKey+">"
      + "<"+typeKey+">" +YHUtility.null2Empty(type)+"</"+typeKey+">"
      + "<"+dataKey+">" +YHUtility.null2Empty(data)+"</"+dataKey+"></"+messageKey+">";
    return ms;
  }
  public static YHEsbMessage xmlToObj(String xml) throws Exception {
    YHEsbMessage m = null; 
    if (!YHUtility.isNullorEmpty(xml) ) {
      SAXReader saxReader = new SAXReader(); 
      StringReader rs = new StringReader(xml);
      Document document = saxReader.read(rs);
      Element root = document.getRootElement();  
      if (root.getName().equals(messageKey)) {
        m = new YHEsbMessage();
        List<Element> elements = root.elements();
        for (Element el : elements) {
          String els = (String) el.getData();
          if (el.getName().equals(contentKey)) {
            m.setMessage(els);
          }
          if (el.getName().equals(typeKey)) {
            m.setType(els);
          }
          if (el.getName().equals(dataKey)) {
            m.setData(els);
          }
        }
      }
      }
      
    return m;
  }
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
  public static void main(String[] args) throws Exception {
//    YHEsbMessage me = new YHEsbMessage();
//    me.setData("aaaa");
//    me.setMessage("eeee");
//    me.setType("2222");
//    String mes = me.toXml();
//    YHEsbMessage me2 = xmlToObj("<?xml version=\"1.0\" encoding=\"UTF-8\"?><message><content>sysDept</content><type></type><data><dept><deptNo>333</deptNo><deptName>3333</deptName><deptDesc>eeeee</deptDesc><esbUser>3333</esbUser><deptParent>0</deptParent><deptId>d9dd7b0c658bec87da6e8f9f975f8782</deptId></dept><dept><deptNo>656</deptNo><deptName>6464</deptName><deptDesc>ddddd</deptDesc><esbUser>6464</esbUser><deptParent>d9dd7b0c658bec87da6e8f9f975f8782</deptParent><deptId>0b59073e679026ca718fc37186f9c2e6</deptId></dept><dept><deptNo>777</deptNo><deptName>664daaa</deptName><deptDesc>dafdadafdasfdasfdsa</deptDesc><esbUser>346464aaa</esbUser><deptParent>d9dd7b0c658bec87da6e8f9f975f8782</deptParent><deptId>53bdeb19145c488f3788a96404be200d</deptId></dept><dept><deptNo>888</deptNo><deptName>888</deptName><deptDesc>646</deptDesc><esbUser>857u575</esbUser><deptParent>0</deptParent><deptId>6f73508977b6ed589e5a9934217a4b3b</deptId></dept></data></message>");
//    System.out.println(me2.getData());
    
    YHExtDept ed = new YHExtDept( "2222" ,  "44444",  "client","444322",  "刘涵" , "1" , "0", "", "", "", "","");
   ed.setDeptId("9999");
  // System.out.println(ed.writeObject(ed));
   YHExtDept ed2 = (YHExtDept)YHObjectUtility.readObject(YHObjectUtility.writeObject(ed));
   System.out.println(ed2.getDeptDesc());
  }
}
