package yh.core.util.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.exps.YHInvalidParamException;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHLinuxUtility {
  private static String machineCode = null;
  
  public static String getMachineCode() throws Exception {
    if (machineCode == null) {
      YHLinuxUtility util = new YHLinuxUtility();
      machineCode = util.getMachineCodeInner();
    }
    return machineCode;
  }
  private String getMachineCodeInner() throws Exception {
    String rtStr = null;
    
    Map<String, Object> diskInfoMap = parseCmdRs(exeCmd("fdisk -l"));
    if (diskInfoMap.size() > 0) {
      List<Map<String, String>> partList = (List<Map<String, String>>)diskInfoMap.get("partList");
      if (partList.size() > 0) {
        Map<String, String> partMap = partList.get(0);
        StringBuffer rtBuf = new StringBuffer("");
        rtBuf.append("{");
          String name = YHUtility.null2Empty((String)partMap.get("name"));
          String boot = YHUtility.null2Empty((String)partMap.get("boot"));
          String start = YHUtility.null2Empty((String)partMap.get("start"));
          String end = YHUtility.null2Empty((String)partMap.get("end"));
          String block = YHUtility.null2Empty((String)partMap.get("block"));
          String id = YHUtility.null2Empty((String)partMap.get("id"));
          String system = YHUtility.null2Empty((String)partMap.get("system"));
          
          rtBuf.append("name:\"" + name + "\"");
          rtBuf.append(", boot:\"" + boot + "\"");
          rtBuf.append(", start:\"" + start + "\"");
          rtBuf.append(", end:\"" + end + "\"");
          rtBuf.append(", block:\"" + block + "\"");
          rtBuf.append(", id:\"" + id + "\"");
          rtBuf.append(", system:\"" + system + "\"");
          
          rtBuf.append("}");
          rtStr = rtBuf.toString();
      }
    }
    if (!YHUtility.isNullorEmpty(rtStr)) {
      String tmpStr = YHDigestUtility.md5Hex(rtStr.getBytes(YHConst.DEFAULT_CODE));
      rtStr = tmpStr.substring(1, 3)
         + tmpStr.substring(7, 9)
         + tmpStr.substring(20, 22)
         + tmpStr.substring(29, 31);
    }else {
      throw new YHInvalidParamException("");
    }
    return rtStr.toUpperCase();
  }
  
  /**
   * 拼接磁盘信息
   * @return
   */
  private String appendDisInfo(Map<String, Object> diskInfoMap) {
    StringBuffer rtBuf = new StringBuffer("");
    String diskType = YHUtility.null2Empty((String)diskInfoMap.get("diskType"));
    String size = YHUtility.null2Empty((String)diskInfoMap.get("size"));
    String headers = YHUtility.null2Empty((String)diskInfoMap.get("headers"));
    String sectors = YHUtility.null2Empty((String)diskInfoMap.get("sectors"));
    String cylinders = YHUtility.null2Empty((String)diskInfoMap.get("cylinders"));
    String DiskIdentyfier = YHUtility.null2Empty((String)diskInfoMap.get("DiskIdentyfier"));
    
    rtBuf.append("diskType:\"" + diskType + "\"");
    rtBuf.append(", size:\"" + size + "\"");
    rtBuf.append(", headers:\"" + headers + "\"");
    rtBuf.append(", sectors:\"" + sectors + "\"");
    rtBuf.append(", cylinders:\"" + cylinders + "\"");
    rtBuf.append(", DiskIdentyfier:\"" + DiskIdentyfier + "\"");
    
    rtBuf.append(", partList:[");
    List<Map<String, String>> partList = (List<Map<String, String>>)diskInfoMap.get("partList");
    if (partList != null) {
      for (int i = 0; i < partList.size(); i++) {
        Map<String, String> partMap = partList.get(i);
        
        if (i > 0) {
          rtBuf.append(", ");
        }
        rtBuf.append("{");
        String name = YHUtility.null2Empty((String)partMap.get("name"));
        String boot = YHUtility.null2Empty((String)partMap.get("boot"));
        String start = YHUtility.null2Empty((String)partMap.get("start"));
        String end = YHUtility.null2Empty((String)partMap.get("end"));
        String block = YHUtility.null2Empty((String)partMap.get("block"));
        String id = YHUtility.null2Empty((String)partMap.get("id"));
        String system = YHUtility.null2Empty((String)partMap.get("system"));
        
        rtBuf.append("name:\"" + name + "\"");
        rtBuf.append(", boot:\"" + boot + "\"");
        rtBuf.append(", start:\"" + start + "\"");
        rtBuf.append(", end:\"" + end + "\"");
        rtBuf.append(", block:\"" + block + "\"");
        rtBuf.append(", id:\"" + id + "\"");
        rtBuf.append(", system:\"" + system + "\"");
        
        rtBuf.append("}");
      }
      rtBuf.append("]");
    }
    
    return rtBuf.toString();
  }
  /**
   * 解析命令的输出，把引导盘作为产生序列号的目标，解析该盘的参数
   * @param cmdRs
   * @return 异常情况输出 null
   *     正常情况输出 {
   *       diskType: 磁盘类型(ide|sisc),
   *       size: 整个磁盘大小,
   *       headers: 磁头数,
   *       sectors: 扇区数,
   *       cylinders: 柱面数,
   *       DiskIdentyfier: 磁盘序列号，与系统有关，未必能够得到
   *       partList: 分区情况[{
   *         name: 名字,
   *         boot: 是否是引导盘(*|empty),
   *         start: 起始单位号,
   *         end: 截止单位号,
   *         block: 块数,
   *         id: 分区类别ID号,
   *         system: 系统名
   *       }]
   *     }
   */
  private Map<String, Object> parseCmdRs(List<String> cmdRs) {
    if (cmdRs == null || cmdRs.size() < 1) {
      return null;
    }
    Map<String, Object> rtMap = new HashMap<String, Object>();
    int startIndex = findNextNextIndex(0, cmdRs, "Disk /dev/");
    while (startIndex >= 0) {
      String lineSize = cmdRs.get(startIndex);
      String lineParam = cmdRs.get(startIndex + 1);
      int diskIdIndex = findNextNextIndex(startIndex + 2, cmdRs, "Disk identifier: ");
      int partIndex = findNextNextIndex(startIndex + 2, cmdRs, "Device Boot");
      //该磁盘创建了分区
      if (partIndex > startIndex) {
        partIndex = parseDiskPart(partIndex + 1, rtMap, cmdRs);
        if (rtMap.size() > 0) {
          parseDiskSize(lineSize, rtMap);
          parseDiskParam(lineParam, rtMap);
          if (diskIdIndex > startIndex) {
            String lineId = cmdRs.get(diskIdIndex);
            int tmpIndex = lineId.indexOf(":");
            if (tmpIndex > 0) {
              rtMap.put("DiskIdentyfier", lineId.substring(tmpIndex + 1).trim());
            }else {
              rtMap.put("DiskIdentyfier", "");
            }
          }
          break;
        }
      }
      startIndex = findNextNextIndex(partIndex + 1, cmdRs, "Disk /dev/");
    }
    return rtMap;
  }
  
  private int findNextNextIndex(int currIndex, List<String> cmdRs, String token) {
    if (currIndex < 0 || currIndex > cmdRs.size() - 1) {
      return -1;
    }
    String line = cmdRs.get(currIndex);
    while (currIndex < cmdRs.size()) {
      if (line.indexOf(token) == 0) {
        return currIndex;
      }
      if (currIndex == cmdRs.size() - 1) {
        break;
      }
      line = cmdRs.get(++currIndex);
    }
    return -1;
  }
  
  /**
   * 解析磁盘参数
   * @param lineParam
   * @param rtMap
   */
  private void parseDiskParam(String lineParam, Map<String, Object> rtMap) {
    String[] paramArray = lineParam.split(", ");
    if (paramArray != null && paramArray.length == 3) {
      rtMap.put("headers", paramArray[0].split(" ")[0]);
      rtMap.put("sectors", paramArray[1].split(" ")[0]);
      rtMap.put("cylinders", paramArray[2].split(" ")[0]);
    }
  }
  /**
   * 解析磁盘整个大小
   * @param lineSize
   * @param rtMap
   */
  private void parseDiskSize(String lineSize, Map<String, Object> rtMap) {
    int devIndex = lineSize.indexOf("/dev/");
    int colonIndex = lineSize.indexOf(":");
    int splitIndex = lineSize.indexOf(",");
    int bytesIndex = lineSize.indexOf("bytes");
    if (colonIndex > 0 && splitIndex > 0 && bytesIndex > 0) {
      rtMap.put("diskType", lineSize.substring(devIndex + 5, colonIndex));
      rtMap.put("size", lineSize.substring(splitIndex + 1, bytesIndex - 1));
    }
  }
  /**
   * 解析分区数据
   * @param index 分区数据起始索引
   * @param rtMap 
   * @param cmdRs 
   * @return 把分区数据填写到返回的Map之中，返回最后一个分区索引值
   */
  private int parseDiskPart(int index, Map<String, Object>rtMap, List<String>cmdRs) {
    if (index < 0 || index > cmdRs.size() - 1) {
      return index;
    }
    String partLine = cmdRs.get(index);
    List<Map<String, String>> partList = new ArrayList<Map<String, String>>();
    boolean existsBoot = false;
    while (partLine.indexOf("/dev/") == 0) {
      Map<String, String> partMap = new HashMap<String, String>();
      String[] wordList = partLine.split("\\s+");
      int firstIndex = 1;
      boolean isBoot = false;
      if (wordList.length >= 7 && wordList[1].equals("*")) {
        isBoot = true;
        existsBoot = true;
        firstIndex++;
      }
      partMap.put("name", (String)wordList[0]);
      partMap.put("boot", isBoot ? "true" : "false");
      partMap.put("start", (String)wordList[firstIndex++]);
      partMap.put("end", (String)wordList[firstIndex++]);
      partMap.put("block", (String)wordList[firstIndex++]);
      partMap.put("id", (String)wordList[firstIndex++]);
      partMap.put("system", (String)wordList[firstIndex++]);
      if (isBoot) {
        partList.add(partMap);
      }
      
      index++;
      if (index > cmdRs.size() - 1) {
        break;
      }
      partLine = cmdRs.get(index);
    }
    if (existsBoot) {
      rtMap.put("partList", partList);
    }
    return index;
  }
  /**
   * 执行命令
   * @return 返回命令的执行结果输出
   */
  private List<String> exeCmd(String cmd) {
    List<String> rtList = new ArrayList<String>();
    BufferedReader br = null;
    try {
      Process p;
      p = Runtime.getRuntime().exec(cmd);
      br = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;
      while ((line = br.readLine()) != null) {
        line = line.trim();
        rtList.add(line);
      }
    } catch (IOException e) {
    }finally {
      if (br != null) {
        try {
          br.close();
        }catch(Exception ex) {
        }
      }
    }
    return rtList;
  }
}
