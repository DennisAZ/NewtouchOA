package yh.core.util.auth;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class YHDigestUtility {
  private static final char[] HEX_ARRAY = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  //取得摘要的实例
  private static MessageDigest md = null;
  static {
    try {
      md = MessageDigest.getInstance("MD5");
    }catch(NoSuchAlgorithmException ex) {
    }
  }
  /**
   * 获取MD5摘要
   * @param inputData
   * @return
   */
  synchronized public static byte[] md5(byte[] inputData) {
    if (md == null) {
      return null;
    }
    md.reset();
    md.update(inputData);
    return md.digest();
  }
  /**
   * 生成MD5摘要
   * @param srcBytes
   * @return
   */
  synchronized public static String md5Hex(byte[] srcBytes) {
    byte[] md5Array = md5(srcBytes);
    StringBuffer rtStr = new StringBuffer();
    for (int i = 0; i < md5Array.length; i++) {
      byte high = (byte)((md5Array[i] >> 4) & 0x0F);
      byte low = (byte)(md5Array[i] & 0x0F);
      rtStr.append(HEX_ARRAY[high]);
      rtStr.append(HEX_ARRAY[low]);
    }
    return rtStr.toString();
  }
  /**
   * 校验是否有效的MD5值
   * @param currData
   * @param digest
   * @return
   */
  synchronized public static boolean isMatch(byte[] currData, String digest) {
    if (currData == null || digest == null) {
      return false;
    }
    String realDigest = md5Hex(currData);    
    return digest.equals(realDigest);
  }
  
  /**
   * 生成MD5摘要
   * @param srcBytes
   * @return
   */
  synchronized public static String md5File(String filePath) {
    if (md == null) {
      return null;
    }
    md.reset();
    InputStream in = null;
    try {
      File file = new File(filePath);
      if (!file.exists() || !file.isFile()) {
        return null;
      }
      in = new BufferedInputStream(new FileInputStream(file));
      byte[] buf = new byte[1024 * 10];
      int readLen = 0;
      while ((readLen = in.read(buf)) > 0) {
        md.update(buf, 0, readLen);
      }
    }catch(Exception ex) {
      return null;
    }finally {
      try {
        if (in != null) {
          in.close();
        }
      }catch(Exception ex) {        
      }
    }
    
    byte[] md5Array = md.digest();
    StringBuffer rtStr = new StringBuffer();
    for (int i = 0; i < md5Array.length; i++) {
      byte high = (byte)((md5Array[i] >> 4) & 0x0F);
      byte low = (byte)(md5Array[i] & 0x0F);
      rtStr.append(HEX_ARRAY[high]);
      rtStr.append(HEX_ARRAY[low]);
    }
    return rtStr.toString();
  }
  
  /**
   * 校验是否有效的MD5值
   * @param currData
   * @param digest
   * @return
   */
  synchronized public static boolean isFileMatch(String filePath, String digest) {
    if (digest == null) {
      return false;
    }
    String realDigest = md5File(filePath);    
    return digest.equals(realDigest);
  }
}
