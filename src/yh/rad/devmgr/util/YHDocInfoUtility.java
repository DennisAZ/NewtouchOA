package yh.rad.devmgr.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import yh.core.data.YHProps;
import yh.rad.devmgr.global.YHRadDevMgrConst;

public class YHDocInfoUtility {
  /**
   * 
   * @param filePath
   * @return
   * @throws Exception
   */
  public static List<YHProps> loadInfoList(String filePath) throws Exception {
    List<YHProps> rtList = new ArrayList<YHProps>();
    File moduleFile = new File(filePath);
    String[] fileArray = moduleFile.list();
    for (int i = 0; i < fileArray.length; i++) {
      String fileName = fileArray[i];
      String dirPath = filePath + "\\" + fileName;
      File dirFile = new File(dirPath);
      if (!dirFile.isDirectory()) {
        continue;
      }
      String infoPath = dirPath + "\\info.text";
      File infoFile = new File(infoPath);
      if (!infoFile.exists()) {
        continue;
      }
      YHProps infoProps = new YHProps();
      infoProps.loadProps(infoPath);
      infoProps.addProp(YHRadDevMgrConst.ENTRY_DIR, fileName);
      rtList.add(infoProps);
    }
    
    return rtList;
  }
  /**
   * 转换成Select下拉框用数据Json
   * @param infoList
   * @param id
   * @param value
   * @return
   */
  public static String toSelectJson(List<YHProps> infoList, String id, String value) {
    StringBuffer rtBuf = new StringBuffer("{");
    rtBuf.append(id);
    rtBuf.append(":{value:\"");
    rtBuf.append(value);
    rtBuf.append("\",data:[");
    for (int i = 0; i < infoList.size(); i++) {
      YHProps entry = infoList.get(i);
      String subsysDir = entry.get(YHRadDevMgrConst.ENTRY_DIR);
      String subsysDesc = entry.get(YHRadDevMgrConst.ENTRY_DESC);
      if (i > 0) {
        rtBuf.append(",");
      }
      rtBuf.append("{code:\"");
      rtBuf.append(subsysDir);
      rtBuf.append("\", desc:\"");
      rtBuf.append(subsysDesc);
      rtBuf.append("\"}");
    }
    rtBuf.append("]}}");
    return rtBuf.toString();
  }
}
