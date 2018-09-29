package yh.rad.dbexputil.transplant.logic.core.cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import yh.core.util.db.YHDBUtility;
import yh.rad.dbexputil.transplant.logic.core.data.YHColumnInfo;
import yh.rad.dbexputil.transplant.logic.core.data.YHSpecialHandFun;
import yh.rad.dbexputil.transplant.logic.core.praser.YHDBExcute;
import yh.rad.dbexputil.transplant.logic.core.praser.YHXmlReader;
import yh.rad.dbexputil.transplant.logic.core.util.db.YHTransplantUtil;

/***
 * 生成配置文件
 * 
 * @author Think
 * 
 */
public class YHTableMappingCfg {
  /**
   * 
   * @param oldSchName
   * @param newSchName
   * @param oldDbType
   * @param newDbType
   * @param tableMapping
   * @param path
   * @throws Exception
   */
  public static void createTableCfg(String oldSchName, String newSchName,
      String oldDbType, String newDbType, Map<String, String> tableMapping,
      String path) throws Exception {
    int intReturn = 0;
    Document document = DocumentHelper.createDocument();
    Element schemaElement = document.addElement("schema");
    schemaElement.addAttribute("old_name", oldSchName);
    schemaElement.addAttribute("old_db_type", oldDbType);
    schemaElement.addAttribute("new_name", newSchName);
    schemaElement.addAttribute("new_db_type", newDbType);

    Element dbConfigElement = schemaElement.addElement("db_config");
    Element newDbConfigElement = dbConfigElement.addElement("new_db_config");
    Element oldDbConfigElement = dbConfigElement.addElement("old_db_config");

    Element tablesElement = schemaElement.addElement("tables");

    Set<String> oldTbales = tableMapping.keySet();
    for (String oldTable : oldTbales) {
      String newTable = tableMapping.get(oldTable);
      Element mappingElement = tablesElement.addElement("mapping");
      mappingElement.addAttribute("old_name", oldTable);
      mappingElement.addAttribute("new_name", newTable);
      Element mappingFile = tablesElement.addElement("mapping_file");
      String mappingFileName = newTable + ".xml";
      mappingFile.setText(mappingFileName);
    }
    String tablesCfgPath = path + "/tableMappingCfg.xml";
    saveXmlFile(document, tablesCfgPath, "utf-8");
  }

  /**
   * String tablesCfgPath = path + "/tableCfg.xml"; SAXReader reader = new
   * SAXReader(); Document document = reader.read(new File(tablesCfgPath));
   * Element root= document.getRootElement(); ArrayList<Element> tablesElems =
   * getElementsByName(root, "tables"); Element tables = null;
   * if(tablesElems.size() > 0){ tables = tablesElems.get(0); } if(tables !=
   * null){ ArrayList<Element> mappings = getElementsByName(tables, "mapping");
   * for (Element mapping : mappings) { ArrayList<Element> mappingFiles =
   * getElementsByName(mapping, "mapping_file"); Element mappingFile = null;
   * if(mappingFiles.size() > 0){ mappingFile = mappingFiles.get(0); }
   * if(mappingFile != null){ //创建配置文件
   * 
   * } }
   * 
   * }
   * 
   * @param tableCfgXml
   * @param path
   * @throws Exception
   */
  public static void createMappingCfg(String oldTableName, String oldSch,
      int oldDbType, String newTableName, String newSch, int newDbTye,
      Map<String, String> colunmns, String mappingFileName,
      ArrayList<YHSpecialHandFun> refers, String path) throws Exception {
    System.out.println("开始生成：" + oldTableName + "表   >>> " + newTableName
        + "表的映射关系!");
    Connection oldConn = YHTransplantUtil.getDBConn(false, oldSch, oldDbType);
    Connection newConn = YHTransplantUtil.getDBConn(false, newSch, newDbTye);
    try {
      Set<String> oldColumns = colunmns.keySet(); //
      Document document = DocumentHelper.createDocument();
      Element schemaElement = document.addElement("schema");
      Element dbConfigElement = schemaElement.addElement("db_config");
      Element newDbConfigElement = dbConfigElement.addElement("new_db_config");
      Element oldDbConfigElement = dbConfigElement.addElement("old_db_config");
      schemaElement.addAttribute("old_name", oldSch);
      schemaElement.addAttribute("old_db_type", String.valueOf(oldDbType));
      schemaElement.addAttribute("new_name", newSch);
      schemaElement.addAttribute("new_db_type", String.valueOf(newDbTye));

      Element tablesElement = schemaElement.addElement("tables");
      Element tableElement = tablesElement.addElement("table");
      tableElement.addAttribute("old_name", oldTableName);
      tableElement.addAttribute("new_name", newTableName);
      String fkrefers = YHDBExcute.getRefers(newConn, newTableName, newSch);
      tableElement.addAttribute("fkrefers", fkrefers);

      // Element columnsElement = tableElement.addElement("columns");

      for (String oldColuName : oldColumns) {
        String newColuName = colunmns.get(oldColuName);
        YHColumnInfo oldClos = YHDBExcute.getColumnInfoByName(oldConn
            .getMetaData(), oldConn.getCatalog(), oldSch, oldTableName,
            oldColuName);
        YHColumnInfo newClos = YHDBExcute.getColumnInfoByName(newConn
            .getMetaData(), newConn.getCatalog(), newSch, newTableName,
            newColuName);

        Element columnElement = tableElement.addElement("column");
        columnElement.addAttribute("old_name", oldClos == null ? oldColuName
            : oldClos.getColumnName());
        columnElement.addAttribute("old_type", oldClos == null ? "0" : String
            .valueOf(oldClos.getDataType()));
        columnElement.addAttribute("new_name", newClos == null ? newColuName
            : newClos.getColumnName());
        columnElement.addAttribute("new_type", newClos == null ? "0" : String
            .valueOf(newClos.getDataType()));
        String textName = (oldClos == null) ? null : oldClos.getColumnName();
        YHSpecialHandFun sphf = isHasSpecial(refers, textName, oldTableName);
        if (refers != null && sphf != null) {
          Element sphanderElement = columnElement.addElement("sphandler");
          sphanderElement.addAttribute("type", sphf.getRefersType());
          HashMap<String, String> params = sphf.getParams();
          if (params != null && params.size() > 0) {
            Element paramElement = sphanderElement.addElement("param");
            Set<String> paramTypes = params.keySet();
            for (String paramType : paramTypes) {
              String paramValue = params.get(paramType);
              paramElement.addAttribute("name", paramType);
              paramElement.addAttribute("value", paramValue);
            }
          }
        }
      }
      File file = new File(path);
      if (!file.exists()) {
        file.mkdirs();
      }
      String tablesCfgPath = path + "/" + newTableName + ".xml";
      saveXmlFile(document, tablesCfgPath, "utf-8");
      System.out.println("生成成功：" + oldTableName + "表   >>> " + newTableName
          + "表的映射关系!");
    } catch (Exception e) {
      throw e;
    } finally {
      oldConn.commit();
      newConn.commit();
      YHDBUtility.closeDbConn(oldConn, null);
      YHDBUtility.closeDbConn(newConn, null);
    }
    
  }

  /**
   * 数据处理
   * 
   * @throws Exception
   */
  public static void parserXml2Data(int oldDbType, int newDbTye)
      throws Exception {
    Connection oldConn = YHTransplantUtil.getDBConn(false, "", oldDbType);
    Connection newConn = YHTransplantUtil.getDBConn(false, "", newDbTye);

  }

  /**
   * 判断此字段是否要做特殊处理
   * 
   * @param refers
   * @param oldName
   * @return
   */
  public static YHSpecialHandFun isHasSpecial(
      ArrayList<YHSpecialHandFun> refers, String oldName, String tabName) {
    for (YHSpecialHandFun sphf : refers) {
      if (oldName != null && tabName != null
          && oldName.equalsIgnoreCase(sphf.getColumnName())
          && tabName.equalsIgnoreCase(sphf.getTabName())) {
        return sphf;
      }
    }
    return null;
  }

  /**
   * 获取某一节点的属性值；
   * 
   * @param inputxml
   *          xml文件；
   * @param XPath
   * @return
   * @throws Exception
   */
  public static String[] getNodeAttributeValue(File inputxml, String XPath)
      throws Exception {
    String nodeAttri = "";// 储存节点属性值;
    if (XPath.indexOf("@") < 0) {
      return null;
    }
    SAXReader saxReader = new SAXReader();
    Document document = null;
    try {
      document = saxReader.read(inputxml);
      List list = document.selectNodes(XPath);
      Iterator it = list.iterator();
      while (it.hasNext()) {
        Attribute attri = (Attribute) it.next();
        nodeAttri += attri.getValue() + ",";
      }
    } catch (Exception e) {
      throw e;
    }
    if (nodeAttri.length() > 0) {
      nodeAttri = nodeAttri.substring(0, nodeAttri.length() - 1);
    }
    return nodeAttri.split(",");
  }

  /**
   * 
   * 修改指定节点的文本值；
   * 
   * @param inputXml
   * @param XPath
   *          要修改节点属性的表达式；如："//article/@level" 则表示article节点下的所有level节点的文本；
   * @param newText
   *          新的文本值；
   * 
   * 
   * @throws DocumentException
   */
  public static Document modifyXMLNodeTextByName(File inputXml, String XPath,
      String newText) throws Exception {
    if (XPath.indexOf("@") >= 0) {
      System.out.println("参数XPath无效!");
      return null;
    }
    SAXReader saxReader = new SAXReader();
    Document document = null;
    try {
      document = saxReader.read(inputXml);
      List list = document.selectNodes(XPath);
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
        Element elementText = (Element) iter.next();
        elementText.setText(newText);
      }
    } catch (DocumentException e) {
      throw e;
    }
    return document;
  }

  /**
   * 替换指定节点文本的值。
   * 
   * @param inputXml
   *          xml文件流
   * @param XPath
   *          要修改节点属性的表达式；如："//article/level" 则表示article节点下的所有level节点的文本；
   * @param oldText
   *          原文本
   * @param newText
   *          新文本；
   * 
   * 
   * @throws Exception
   */
  public static Document modifyXMLNodeTextByName(File inputXml, String XPath,
      String oldText, String newText) throws Exception {
    if (XPath.indexOf("@") >= 0) {
      System.out.println("参数XPath无效!");
      return null;
    }
    SAXReader saxReader = new SAXReader();
    Document document = null;
    try {
      document = saxReader.read(inputXml);
      List list = document.selectNodes(XPath);
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
        Element elementText = (Element) iter.next();
        if (elementText.getText().equals(oldText))
          elementText.setText(newText);
      }
    } catch (DocumentException e) {
      throw e;
    }
    return document;
  }

  /**
   *获取某一节点的文本内容；
   * 
   * @param inputxml
   *          xml文件；
   * @param XPath
   * @return
   * 
   * @throws Exception
   */
  public static String[] getNodeTextValue(File inputxml, String XPath)
      throws Exception {
    String nodeTextValue = "";// 储存节点属性值;
    if (XPath.indexOf("@") >= 0) {
      return null;
    }
    SAXReader saxReader = new SAXReader();
    Document document = null;
    try {
      document = saxReader.read(inputxml);
      List list = document.selectNodes(XPath);
      Iterator it = list.iterator();
      while (it.hasNext()) {
        Element text = (Element) it.next();
        nodeTextValue += text.getText() + ",";
      }
    } catch (Exception e) {
      throw e;
    }
    if (nodeTextValue.length() > 0) {
      nodeTextValue = nodeTextValue.substring(0, nodeTextValue.length() - 1);
    }
    return nodeTextValue.split(",");
  }

  /**
   * 保存xml文件;
   * 
   * @param document
   *          xml文件流;
   * @param filePath
   *          文件存储的全路径(包括文件名)
   * @code 储存的编码;
   * 
   * @throws Exception
   */
  public static void saveXmlFile(Document document, String filePath, String code)
      throws Exception {
    if (document == null) {
      return;
    }
    XMLWriter output;
    try {
      OutputFormat format = new OutputFormat();
      format.setEncoding(code);
      format.setNewlines(true);
      format.setPadText(true);
      format.setTrimText(true);
      File file = new File(filePath);
      /*
       * if(!file.exists()){ file.createNewFile(); }
       */
      output = new XMLWriter(new FileWriter(file), format);
      output.write(document);
      output.close();
    } catch (IOException e) {
      throw e;
    }
  }
/**
 * 
 * @param document
 * @param filePath
 * @param code
 * @throws Exception
 */
  public static void saveXmlFile(Element element, String filePath, String code)
      throws Exception {
    if (element == null) {
      return;
    }
    XMLWriter output;
    try {
      OutputFormat format = new OutputFormat();
      format.setEncoding(code);
      format.setNewlines(true);
      format.setPadText(true);
      format.setTrimText(true);
      File file = new File(filePath);
      /*
       * if(!file.exists()){ file.createNewFile(); }
       */
      output = new XMLWriter(new FileWriter(file,true), format);
      output.write(element);
      output.close();
    } catch (IOException e) {
      throw e;
    }
  }

  /**
   * 修改指定节点的属性值；
   * 
   * @param inputXml
   * @param XPath
   * @param attributeValue
   * @return
   */
  public Document modifyXMLNodeAttributeByName(File inputXml, String XPath,
      String attributeValue) throws Exception {
    if (XPath.indexOf("@") < 0) {
      System.out.println("参数XPath无效,请在要修改的属性前加入'@'");
      return null;
    }
    SAXReader saxReader = new SAXReader();
    Document document = null;
    try {
      document = saxReader.read(inputXml);
      List list = document.selectNodes(XPath);
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
        Attribute attribute = (Attribute) iter.next();
        // 把原属性修改为新的属性；
        attribute.setValue(attributeValue);
      }

    } catch (DocumentException e) {
      e.printStackTrace();
    }
    return document;
  }

  /**
   * 修改指定节点的属性值；
   * 
   * @param inputXml
   *          xml文件流
   * @param XPath
   *          要修改节点属性的表达式；如："//article/@level" 则表示修改节点level(父节点为article)的属性
   * @param attributeValue
   *          属性新值；
   */
  public Document modifyXMLNodeAttributeByName(File inputXml, String XPath,
      String oldAttributeValue, String attributeValue) throws Exception{
    if (XPath.indexOf("@") < 0) {
      System.out.println("参数XPath无效,请在要修改的属性前加入'@'");
      return null;
    }
    SAXReader saxReader = new SAXReader();
    Document document = null;
    try {
      document = saxReader.read(inputXml);
      List list = document.selectNodes(XPath);
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
        Attribute attribute = (Attribute) iter.next();
        if (attribute.getValue().equals(oldAttributeValue))// 把原属性修改为新的属性；
          attribute.setValue(attributeValue);
      }

    } catch (DocumentException e) {
      e.printStackTrace();
    }
    return document;

  }

  /**
   * 取得指定的节点
   * 
   * @param parent
   * @param name
   * @return
   */
  public static ArrayList<Element> getElementsByName(Element parent, String name) {
    ArrayList<Element> result = new ArrayList<Element>();
    if (parent.getName().equalsIgnoreCase(name)) {
      result.add(parent);
    }
    Iterator<Element> iter = parent.elementIterator();
    if (!iter.hasNext()) {
      return result;
    } else {
      while (iter.hasNext()) {
        Element sub = iter.next();
        result.addAll(getElementsByName(sub, name));
      }
    }
    return result;
  }
  /**
   * 
   * @param xmlFile
   * @return
   * @throws Exception
   */
  public static Document getDocument(File xmlFile)throws Exception {
    YHXmlReader handler = new YHXmlReader();
    SAXReader reader = new SAXReader();
    reader.setDefaultHandler(handler);
    InputStream ins = new FileInputStream(xmlFile);
    return reader.read(ins);
  }
}
