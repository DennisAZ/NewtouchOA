package yh.core.util.auth;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import yh.core.data.YHAuthKeys;
import yh.core.global.YHConst;
import yh.core.global.YHRegistProps;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUtility;

/**
 * 注册相关类
 * @author jpt
 *
 */
public class YHRegistUtility {
  /**
   * log
   */
  private static final Logger log = Logger.getLogger("yzq.yh.core.util.auth.YHRegistUtility");
  
  public static String getMchineCode() throws Exception {
    if (YHSysProps.isWindows()) {
//      return TDHardWareReader.getMachineCode(YHSysProps.getRootPath().substring(0, 3));
    }else if (YHSysProps.isLinux()) {
//      return YHLinuxUtility.getMachineCode();
    }else {
      throw new Exception("Unsurported OS");
    }
    return "00000";
  }
  /**
   * 加载注册文件
   * @param fileName
   * @return
   */
  public static Map<String, String> loadRegistFromPath(String fileName, String installDisk, String softId, String registOrg) {
    Map registMap = new HashMap<String, String>();
    File file = new File(fileName);
    if (!file.exists()) {
      return registMap;
    }
    if (file.isDirectory()) {
      String[] fileList = file.list();
      for (int i = 0; i < fileList.length; i++) {
        String currFileName = fileList[i];
        if (!currFileName.endsWith(".regist") && !currFileName.endsWith(".install")) {
          continue;
        }
        String filePath = fileName + File.separator + currFileName;
        Map tmpMap = loadRegist(filePath, installDisk, softId, registOrg);
        YHUtility.copyMap(tmpMap, registMap);
      }      
    }
    return registMap;
  }
  /**
   * 加载注册文件
   * @param fileName
   * @return
   */
  public static Map loadRegist(String fileName, String installDisk, String softId, String registOrg) {
    Map registMap = new HashMap();
    File file = new File(fileName);
    if (!file.exists() || !file.isFile()) {
      return registMap;
    }
    try {
      String machineCode = YHRegistUtility.getMchineCode();
      //加载注册属性
      byte[] buf = YHFileUtility.loadFile2Bytes(fileName);
      if (buf == null) {
        return registMap;
      }
      String propStr = YHAuthenticator.ciphDecryptBytes(buf);
      YHUtility.str2Map(propStr, registMap);
      //删除不合法的注册属性
      List<String> keyList = new ArrayList<String>();
      Iterator<String> iKeys = registMap.keySet().iterator();
      while (iKeys.hasNext()) {
        String key = (String)iKeys.next();
        if (key.endsWith(YHAuthKeys.REGIST_PROPKEY_DIGIST_POSTFIX)) {
          continue;
        }
        String value = (String)registMap.get(key);
        String didgistKey = key + YHAuthKeys.REGIST_PROPKEY_DIGIST_POSTFIX;
        String didgist = (String)registMap.get(didgistKey);
        if (didgist == null) {
          keyList.add(key);
          continue;
        }
        try {
          if (!YHAuthenticator.isValidRegist(YHAuthKeys.getMD5SaltLength(null), value + machineCode + softId + registOrg, didgist)) {
            keyList.add(key);
            keyList.add(didgistKey);
          }else {
            keyList.add(didgistKey);
          }
        }catch(Exception ex) {
          keyList.add(key);
        }
      }
      for (int i = 0; i < keyList.size(); i++) {
        String key = (String)keyList.get(i);
        registMap.remove(key);
      }
    }catch(Exception ex) {
      log.debug(ex.getMessage(), ex);
    }
    return registMap;
  }
  
  /**
   * 加载注册文件
   * @param fileName
   * @return
   */
  public static Map loadRegist(String fileName) {
    Map registMap = new HashMap();
    File file = new File(fileName);
    if (!file.exists() || !file.isFile()) {
      return registMap;
    }
    try {
      //加载注册属性
      byte[] buf = YHFileUtility.loadFile2Bytes(fileName);
      if (buf == null) {
        return registMap;
      }
      String propStr = YHAuthenticator.ciphDecryptBytes(buf);
      YHUtility.str2Map(propStr, registMap);
      //删除不合法的注册属性
      List<String> keyList = new ArrayList<String>();
      Iterator<String> iKeys = registMap.keySet().iterator();
      while (iKeys.hasNext()) {
        String key = (String)iKeys.next();
        if (key.endsWith(YHAuthKeys.REGIST_PROPKEY_DIGIST_POSTFIX)) {
          keyList.add(key);
        }
      }
      for (int i = 0; i < keyList.size(); i++) {
        String key = (String)keyList.get(i);
        registMap.remove(key);
      }
    }catch(Exception ex) {
      log.debug(ex.getMessage(), ex);
    }
    return registMap;
  }
  
  /**
   * 软件是否已经注册
   * @return
   */
  public static boolean hasRegisted() {
    return !YHUtility.isNullorEmpty(YHRegistProps.getString(YHAuthKeys.REGIST_ORG + ".yh"));
  }
  
  /**
   * 软件是否已经过期
   * @return
   */
  public static boolean isExpired() {
    return remainDays() < 1;
  }
  
  /**
   * 软件是否已经过期
   * @return
   */
  public static int remainDays() {
    String installTimeStr = YHSysProps.getString(YHSysPropKeys.INSTALL_TIME);
    String installTimePassStr = YHSysProps.getString(YHSysPropKeys.INSTALL_TIME_PASS);
    if (YHUtility.isNullorEmpty(installTimeStr) || YHUtility.isNullorEmpty(installTimePassStr)) {
      return 0;
    }
    try {
      if (!YHAuthenticator.isValidRegist(YHAuthKeys.getMD5SaltLength(null), installTimeStr, installTimePassStr)) {
        return 0;
      }
      long installTime = Long.parseLong(installTimeStr);
      long currTime = System.currentTimeMillis();
      String evalueDaysStr = YHSysProps.getString("evalueDays");
      String evalueDaysPass = YHSysProps.getString("evalueDaysPass");
      
      int evalueDate = 30;
      if (!YHUtility.isNullorEmpty(evalueDaysStr)
          && YHAuthenticator.isValidRegist(YHAuthKeys.getMD5SaltLength(null),
              evalueDaysStr + YHRegistUtility.getMchineCode(),
              evalueDaysPass)) {
        evalueDate = Integer.parseInt(evalueDaysStr);
      }
      
      if (((currTime - installTime) / YHConst.DT_D) > evalueDate) {
        return 0;
      }
      return evalueDate - (int)((currTime - installTime) / YHConst.DT_D);
    }catch(Exception ex) {
      return 0;
    }
  }
  
  /**
   * 取得授权用户数
   * @return
   */
  public static int getUserCnt() {
	  int maxUserCnt = 100000;//YHRegistProps.getInt(YHAuthKeys.USER_CNT + ".yh");
    if (maxUserCnt > 0) {
      return maxUserCnt;
    }
    int defaultCnt = 30;
    String userCntStr = YHSysProps.getString("maxUserCnt");
    String userCntPassStr = YHSysProps.getString("maxUserCntPass");
    if (YHUtility.isNullorEmpty(userCntStr) || YHUtility.isNullorEmpty(userCntPassStr)) {
      return defaultCnt;
    }
    try {
      if (!YHAuthenticator.isValidRegist(YHAuthKeys.getMD5SaltLength(null),
          userCntStr + YHRegistUtility.getMchineCode(),
          userCntPassStr)) {
        return defaultCnt;
      }
      int userCnt = Integer.parseInt(userCntStr);
      return userCnt;
    }catch(Exception ex) {
      return defaultCnt;
    }
  }
}
