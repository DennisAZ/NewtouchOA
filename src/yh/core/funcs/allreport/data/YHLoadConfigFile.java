package yh.core.funcs.allreport.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yh.core.util.YHOut;

public class YHLoadConfigFile {
   private String file; 
   private List<YHField> fieldList;
   private List<YHJionCondition>  conditionList;

   public YHLoadConfigFile(String file){
     this.conditionList=new ArrayList();
     this.fieldList=new ArrayList();
     this.file=file;
     this.loadConfigFile(this.file);
   }

   public YHLoadConfigFile(){
     this.conditionList=new ArrayList();
     this.fieldList=new ArrayList();
   }

 
  
  public String getFile() {
    return file;
  }




  public void setFile(String file) {
    this.file = file;
  }




  public List<YHField> getFieldList() {
    return fieldList;
  }




  public void setFieldList(List<YHField> fieldList) {
    this.fieldList = fieldList;
  }




  public List<YHJionCondition> getConditionList() {
    return conditionList;
  }




  public void setConditionList(List<YHJionCondition> conditionList) {
    this.conditionList = conditionList;
  }




  @SuppressWarnings("unchecked")
  public void loadConfigFile(String file){
        SAXReader reader = new SAXReader();
        Document document;
        try {
          document = reader.read(new InputStreamReader(new FileInputStream(new File(file)), "UTF-8"));
          Element root = document.getRootElement();
          Iterator e = root.elementIterator();
          if(e.hasNext()){
            Element fields=(Element)e.next();

          List list= fields.elements("field");
          for ( int i=0;i<list.size();i++ ) {
            YHField record=new YHField();
            Element field = (Element) list.get(i);
            record.setFromTable(field.attributeValue("fromTable"));
            record.setItem(field.attributeValue("item"));
            record.setJavaType(field.attributeValue("javaType"));
            record.setItemDesc(field.attributeValue("itemDesc"));
            record.setForignKey(field.attributeValue("forignKey"));
            record.setDataType(field.attributeValue("dataType"));
            record.setSelectItem(field.attributeValue("selectItem"));
            record.setSelectData(field.attributeValue("selectData"));
            
            
            String forignKey=field.attributeValue("forignKey");
            
            if("1".equals(forignKey)){
              for(Iterator j=field.elementIterator();j.hasNext();){
                Element forign = (Element) j.next();
                record.setTable(forign.attributeValue("table"));
                record.setKey(forign.attributeValue("key"));
                record.setDescKey(forign.attributeValue("descKey"));
                record.setForignKeyDataType(forign.attributeValue("forignKeyDataType"));
              }
            }
          this.fieldList.add(record);     
           }
          }
        if(e.hasNext()){
        Element cEelement=(Element)e.next();
        List  list= cEelement.elements("jionCondition");
        for (int i=0;i<list.size();i++ ) {
          YHJionCondition condition=new YHJionCondition();
          Element jionCondition = (Element)list.get(i);
          condition.setTables(jionCondition.attributeValue("tables"));
          condition.setItem1(jionCondition.attributeValue("item1"));
          condition.setItem2(jionCondition.attributeValue("item2"));
          this.conditionList.add(condition);
          }    
         }
        } catch (UnsupportedEncodingException e) {
          // TODO Auto-generated catch block
         System.out.println(e.toString()) ;
        } catch (FileNotFoundException e) {
          // TODO Auto-generated catch block
          System.out.println(e.toString()) ;
        } catch (DocumentException e) {
          // TODO Auto-generated catch block
          System.out.println(e.toString()) ;
        }
        
        
  }
   
   
}
