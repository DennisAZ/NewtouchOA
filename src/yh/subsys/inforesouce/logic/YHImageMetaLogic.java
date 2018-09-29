package yh.subsys.inforesouce.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import yh.core.util.YHRegexpUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.subsys.inforesouce.data.YHMateType;
import yh.subsys.inforesouce.db.YHMetaDbHelper;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectory;
public class YHImageMetaLogic {
  private static String metaType = "2";
  private static Map<String , String> attaMap =  new HashMap();
  private static String profix = System.getProperty("file.separator");
  private static final String BASE_PATH = "D:"+ profix +"project"+ profix +"yh"+ profix +"attach"+ profix ;
  public Map<String , String> readImageMeta(Connection conn, String imagePath) throws Exception {
    File imageFile = new File(imagePath);
    if (!imageFile.exists()) {
      return null;
    }
    Map<String , String> map = new HashMap();
    Metadata metadata  = JpegMetadataReader.readMetadata(imageFile);
    Directory exif = metadata.getDirectory(ExifDirectory.class);
    List<YHMateType> list = this.getMetaProperty(conn, 2);
    this.defAttaMap();
    for (YHMateType metaType : list) {
      String tagKey = attaMap.get(metaType.getcNname());
      if (tagKey != null) {
        String model = exif.getString(Integer.parseInt(tagKey)); 
        String numberId =  metaType.getNumberId();
        if (!numberId.startsWith("MEX") && numberId.startsWith("M")) {
          String numStr = numberId.substring(1);
          if (YHUtility.isInteger(numStr)) {
            int n = Integer.parseInt(numStr);
            if (n > 100) {
              numberId = "MEX" + numStr;
            }
          }
        }
        map.put(numberId, model);
      }
    }
    String model = exif.getString(ExifDirectory.TAG_FOCAL_LENGTH);      
    System.out.println("焦距" + model);
    return map;
  }
  public void saveImageMeta(Connection conn , String attachmentId , String attachmentName , String module) throws Exception {
    String imagePath = this.getImagePath(attachmentId, attachmentName, module);
    Map map = this.readImageMeta(conn, imagePath);
    YHMetaDbHelper helper = new YHMetaDbHelper();
    helper.updateMetadata(conn, attachmentId, imagePath, map, null, metaType, 0);
  }
  public String getImagePath(String attachmentId , String attachmentName , String module){
    String attId = attachmentId.replace("_", profix);
    String absolutePath = BASE_PATH  + module + profix + attId +"_" + attachmentName;
    return absolutePath;
  }
  public List<YHMateType> getMetaProperty(Connection conn , int typeId) throws Exception {
    List<YHMateType> list = new ArrayList<YHMateType>();
    String query = "SELECT * FROM oa_mate_kind WHERE PARENT_ID=0 and " +YHDBUtility.findInSet(""+ typeId, "ELEMENT_TYP");
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while(rs.next()) {
        String propNumberId = rs.getString("NUMBER_ID");
        String propChName = rs.getString("CHNAME");
        String propEnName = rs.getString("ENNAME");
        YHMateType meta = new YHMateType();
        meta.setNumberId(propNumberId);
        meta.setcNname(propChName);
        meta.seteNname(propEnName);
        list.add(meta);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return list;
  }
  public void defAttaMap() throws Exception {
    String defStr = "{相机厂商:"+ ExifDirectory.TAG_MAKE 
      + ",相机型号:" + ExifDirectory.TAG_MODEL
      + ",焦距:" + ExifDirectory.TAG_FOCAL_LENGTH
      +",白平衡:"+ExifDirectory.TAG_WHITE_BALANCE+"}";
    attaMap = json2Map(defStr);
  }
  public  Map json2Map(String json) throws Exception {
    Map propMap = new HashMap<String, String>();
    List<String> tmpArray = YHRegexpUtility.splitJson(json);
    for (String propStr : tmpArray) {
      propStr = propStr.trim();
      String[] tmpArray2 = propStr.split(":");
      if (tmpArray2.length < 2) {
        continue;
      }
      tmpArray2[0] = tmpArray2[0].trim();
      tmpArray2[1] = tmpArray2[1].trim();
      if (tmpArray2[0].charAt(0) == '\"' || tmpArray2[0].charAt(0) == '\'') {
        tmpArray2[0] = tmpArray2[0].substring(1, tmpArray2[0].length() - 1);
      }
      if (tmpArray2[1].charAt(0) == '\"' || tmpArray2[1].charAt(0) == '\'') {
        tmpArray2[1] = tmpArray2[1].substring(1, tmpArray2[1].length() - 1);
      }
      propMap.put(tmpArray2[0], tmpArray2[1]);
    }
    return propMap;
  }
  public String getImageMeta(Connection conn , String fileId) throws Exception {
    List<YHMateType> list = this.getMetaProperty(conn, 2);
    Map<String , String> map = new HashMap();
    String typeQuery = "";
    Map<String , String> map2 = new HashMap();
    for (YHMateType type : list) {
      String numberId = type.getNumberId();
      String numStr  = "";
      if (numberId.startsWith("MEX")) {
        numStr = numberId.substring(3);
      } else if (numberId.startsWith("M")) {
        numStr = numberId.substring(1);
        if (numStr.length() == 1) {
          numStr = "00" + numStr;
        } else if (numStr.length() == 2) {
          numStr = "0" + numStr;
        }
      }
      if ("".equals(numStr)) {
        continue;
      }
      numStr = "ATTR_" + numStr;
      typeQuery += numStr + ",";
      map.put(numStr, type.getcNname());
      map2.put(type.getcNname(), numberId);
    }
    if (typeQuery.endsWith(",")) {
      typeQuery = typeQuery.substring(0, typeQuery.length() - 1);
    }
    Map<String , String> valueMap = new HashMap();
    if (!YHUtility.isNullorEmpty(typeQuery)) {
      String query = "select " + typeQuery + " from oa_file_attrs02 where FILE_SEQ_ID = (SELECT SEQ_ID FROM oa_seal_attach WHERE FILE_ID = '" + fileId + "')";
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = conn.createStatement();
        rs = stm.executeQuery(query);
        if (rs.next()) {
          Set<String> keySet = map.keySet();
          for (String key : keySet) {
            String value = rs.getString(key);
            String keyStr = map.get(key);
            valueMap.put(keyStr, value);
          }
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null);
      }
    }
    return this.mapToXml(conn , valueMap , map2);
  }
  public String mapToXml(Connection conn , Map<String , String> valueMap , Map<String , String> map) throws Exception {
    StringBuffer sb = new StringBuffer();
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><nodes>");
    Set<String> keySet = valueMap.keySet();
    for (String key : keySet) {
      String value = valueMap.get(key);
        String tmp = key;
        if (!YHUtility.isNullorEmpty(value)) {
          tmp = tmp + ":" + value;
        }
        String numberId = map.get(key);
        sb.append("<node numberId=\""+numberId+"\"  name=\""+ key +"\" label=\""+ tmp + "\">");
        String query = "select value_name from oa_mate_value where type_number='" + numberId + "'";
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(query);
          while (rs.next()) {
            String valueName = rs.getString("value_name");
            if (!YHUtility.isNullorEmpty(valueName)) {
              if (valueName.equals(value)) {
                sb.append("<node idChild=\"true\" label=\""+valueName+"\" selected=\"true\"/>");
              } else {
                sb.append("<node  idChild=\"true\" label=\""+valueName+"\" selected=\"false\"/>");
              }
              
            }
          }
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null);
        }
        sb.append("</node>");
     // }
    }
    sb.append("</nodes>");
    return sb.toString();
  }
  public static void main(String[] args) {
    try{
//      YHImageMetaLogic logic = new YHImageMetaLogic();
//      Connection conn = TestDbUtil.getConnection(false, "TD_OA2");
//      String query = "select attachment_id , attachment_name from news where seq_id=1381";
//      Statement stm = null;
//      ResultSet rs = null;
//      stm = conn.createStatement();
//      rs = stm.executeQuery(query);
//      if (rs.next()) {
//        String attachmentId = rs.getString("attachment_id");
//        String attachmentName = rs.getString("attachment_name");
//        String[] attrName = attachmentName.split("[*]");
//        String[] attrId = attachmentId.split(",");
//        for(int i=0; i<attrId.length; i++){
//          logic.saveImageMeta(conn, attrId[i] , attrName[i] , "news");
//          conn.commit();
//        }
//      }
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}
//String model = exif.getString(ExifDirectory.TAG_MAKE);
//System.out.println("设备制造商 " + model);
//
//model = exif.getString(ExifDirectory.TAG_MODEL);      
//System.out.println("相机的型号 " + model);
//
//model = exif.getString(ExifDirectory.TAG_X_RESOLUTION);      
//System.out.println("水平分辨率 " + model);
//
//model = exif.getString(ExifDirectory.TAG_Y_RESOLUTION);      
//System.out.println("垂直分辨率 " + model);
//
//model = exif.getString(ExifDirectory.TAG_DATETIME);      
//System.out.println("拍摄日期时间 " + model);
//
//model = exif.getString(ExifDirectory.TAG_EXPOSURE_TIME);      
//System.out.println("曝光时间" + model);
//
//model = exif.getString(ExifDirectory.TAG_FNUMBER);      
//System.out.println("光圈" + model);
//
//model = exif.getString(ExifDirectory.TAG_ISO_EQUIVALENT);      
//System.out.println("ISO值" + model);
//
//model = exif.getString(ExifDirectory.TAG_EXIF_VERSION);      
//System.out.println("Exif版本" + model);
//
//model = exif.getString(ExifDirectory.TAG_FLASH);      
//System.out.println("闪光灯" + model);
//
//model = exif.getString(ExifDirectory.TAG_FOCAL_LENGTH);      
//System.out.println("焦距" + model);
//
//model = exif.getString(ExifDirectory.TAG_EXIF_IMAGE_WIDTH);      
//System.out.println("宽度" + model);
//
//model = exif.getString(ExifDirectory.TAG_EXIF_IMAGE_HEIGHT);      
//System.out.println("高度" + model);
