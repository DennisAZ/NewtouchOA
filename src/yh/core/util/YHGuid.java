package yh.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 生成GUID类
 * @author jpt
 * @date 2008-06-21
 */
public class YHGuid {
  public static final int BEFORE_MD5 = 1;
  public static final int AFTER_M5 = 2;
  public static final int FORMAT_STRING = 3;
  
  private static Random myRand = null;
  private static SecureRandom mySecureRand = null;
  private static String s_id = null;
  
  private String seedingString = "";
  private String rawGUID = "";
 

  static {
    mySecureRand = new SecureRandom();
    long secureInitializer = mySecureRand.nextLong();
    myRand = new Random(secureInitializer);
    try {
      s_id = InetAddress.getLocalHost().toString();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }
  /*
   * Method to generate the random GUID
   */
  private void getRandomGUID(boolean secure) throws NoSuchAlgorithmException {
    MessageDigest md5 = null;
    StringBuffer sbValueBeforeMD5 = new StringBuffer();

    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException ex) {
      throw ex;
    }

    try {
      long time = System.currentTimeMillis();
      long rand = 0;

      if (secure) {
        rand = mySecureRand.nextLong();
      } else {
        rand = myRand.nextLong();
      }

      // This StringBuffer can be a long as you need; the MD5
      // hash will always return 128 bits. You can change
      // the seed to include anything you want here.
      // You could even stream a file through the MD5 making
      // the odds of guessing it at least as great as that
      // of guessing the contents of the file!
      sbValueBeforeMD5.append(s_id);
      sbValueBeforeMD5.append(":");
      sbValueBeforeMD5.append(Long.toString(time));
      sbValueBeforeMD5.append(":");
      sbValueBeforeMD5.append(Long.toString(rand));

      seedingString = sbValueBeforeMD5.toString();
      md5.update(seedingString.getBytes());

      byte[] array = md5.digest();
      StringBuffer sb = new StringBuffer();
      for (int j = 0; j < array.length; ++j) {
        int b = array[j] & 0xFF;
        if (b < 0X10) {
          sb.append('0');
        }
        sb.append(Integer.toHexString(b));
      }

      rawGUID = sb.toString();

    } catch (Exception e) {
      //System.out.println("Error:" + e);
    }
  }

  public static String getGuid(int nFormatType, boolean secure) throws NoSuchAlgorithmException {
    YHGuid builder = new YHGuid();
    builder.getRandomGUID(secure);
    String sGuid = "";
    if (BEFORE_MD5 == nFormatType) {
      sGuid = builder.getSeedingString();
    } else if (AFTER_M5 == nFormatType) {
      sGuid = builder.getRawGUID();
    } else {
      sGuid = builder.toString();
    }
    return sGuid;
  }

  public static String getGuid(int nFormatType) throws NoSuchAlgorithmException {
    return getGuid(nFormatType, false);
  }
  
  public static String getGuid() throws NoSuchAlgorithmException {
    return getGuid(FORMAT_STRING, false);
  }
  
  public static String getRawGuid() throws NoSuchAlgorithmException {
    return getGuid(AFTER_M5, false);
  }

  /*
   * Convert to the standard format for GUID (Useful for SQL Server
   * UniqueIdentifiers, etc.) Example: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
   */
  public String toString() {
    //System.out.println(rawGUID);
    String raw = rawGUID.toUpperCase();
    StringBuffer sb = new StringBuffer();
    sb.append(raw.substring(0, 8));
    sb.append("-");
    sb.append(raw.substring(8, 12));
    sb.append("-");
    sb.append(raw.substring(12, 16));
    sb.append("-");
    sb.append(raw.substring(16, 20));
    sb.append("-");
    sb.append(raw.substring(20));

    return sb.toString();
  }

  public static void main(String args[]) throws NoSuchAlgorithmException {
    for (int i = 0; i < 100; i++) {
      YHGuid myGUID = new YHGuid();
      //System.out.println("Seeding String="
//          + myGUID.getGuid(BEFORE_MD5));
      //System.out.println("rawGUID="
//          + myGUID.getGuid(AFTER_M5));
      //System.out.println("RandomGUID="
//          + myGUID.getGuid(FORMAT_STRING));
    }
  }

  public String getSeedingString() {
    return seedingString;
  }

  public String getRawGUID() {
    return rawGUID;
  }
}
