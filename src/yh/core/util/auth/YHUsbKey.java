package yh.core.util.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Usbkey认证程序
 * @author jpt
 *
 */
public class YHUsbKey {
  private static final char[] HEX_ARRAY = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  /**
   * 生成MD5摘要
   * @param srcBytes
   * @return
   */
  public static byte[] md5(byte[] srcBytes) throws Exception {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("MD5");
    }catch(NoSuchAlgorithmException ex) {
      throw ex;
    }
    md.update(srcBytes);
    return md.digest();
  }
  /**
   * 生成MD5摘要
   * @param srcBytes
   * @return
   */
  public static String md5Hex(byte[] srcBytes) throws Exception {
    byte[] md5Array = md5(srcBytes);
    String rtStr = "";
    for (int i = 0; i < md5Array.length; i++) {
      byte high = (byte)((md5Array[i] >> 4) & 0x0F);
      byte low = (byte)(md5Array[i] & 0x0F);
      rtStr += HEX_ARRAY[high];
      rtStr += HEX_ARRAY[low];
    }
    return rtStr;
  }
  public static byte[] str2Bytes(String srcStr) {
    int len = srcStr.length();
    byte[] rtArray = new byte[len];
    for (int i = 0; i < len; i++) {
      rtArray[i] = (byte)(srcStr.charAt(i) & 0x00FF);
    }
    return rtArray;
  }
  /**
   * 生成MD5摘要
   * @param srcBytes
   * @return
   */
  public static String md5Hex(String srcStr) throws Exception {
    return md5Hex(str2Bytes(srcStr));
  }
  
  /**
   * 加密串校验
   * @param clientDigest
   * @param randomData
   * @param passWord
   * @return
   */
  public static boolean digestComp(String clientDigest,
      String randomData, String passWord) throws Exception {
    String iPad = "";
    for (int i = 0; i < 64; i++) {
      iPad += "6";
    }
    String oPad = "";
    for (int i = 0; i < 64; i++) {
      oPad += "\\";
    }
    int keyLen = passWord.length();
    int randomLength = randomData.length();
    byte[] iResult = new byte[64 + randomLength];
    for (int i = 0; i < 64; i++) {
      if (i < keyLen) {
        iResult[i] = (byte)((iPad.charAt(i) & 0x00FF) ^ (passWord.charAt(i) & 0x00FF));
      }else {
        iResult[i] = (byte)(iPad.charAt(i) & 0x00FF);
      }
    }
    for (int i = 0; i < randomLength; i++) {
      iResult[64 + i] = (byte)(randomData.charAt(i) & 0x00FF);
    }
    iResult = md5(iResult);

    byte[] oResult = new byte[64 + iResult.length];
    for (int i = 0; i < 64; i++) {
      if (i < keyLen) {
        oResult[i] = (byte)((oPad.charAt(i) & 0x00FF) ^ (passWord.charAt(i) & 0x00FF));
      }else {
        oResult[i] = (byte)(oPad.charAt(i) & 0x00FF);
      }
    }
    for (int i = 0; i < iResult.length; i++) {
      oResult[64 + i] = iResult[i];
    }
    String result = md5Hex(oResult);
    result = result.toUpperCase();

    if (result.equals(clientDigest)) {
      return true;
    }else {
      return false;
    }
  }
}