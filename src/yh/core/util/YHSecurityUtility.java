package yh.core.util;

import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import yh.core.data.YHAuthKeys;

public class YHSecurityUtility {
  /**
   * 取得默认的密码保护的加密/解密对象
   * @return
   * @throws Exception
   */
  public static Cipher getPassWordCipher(int mode) throws Exception {
    return getPassWordCipher(
        YHAuthKeys.getPassword(null),
        YHAuthKeys.getSalt(null),
        YHAuthKeys.getItCnt(null),
        mode);
  }
      
  /**
   * 取得密码保护的加密/解密对象
   * @param passWord       密码
   * @param salt           干扰字节码
   * @param itCnt          迭代次数
   * @param mode           加密/解密
   * @return
   */
  public static Cipher getPassWordCipher(
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode) throws Exception {
    
    PBEKeySpec pbeKeySpec = null;
    PBEParameterSpec pbeParamSpec = null;
    SecretKeyFactory keyFac = null;
    
    pbeParamSpec = new PBEParameterSpec(salt, itCnt);
    pbeKeySpec = new PBEKeySpec(passWord);
    keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
    Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
    pbeCipher.init(mode, pbeKey, pbeParamSpec);

    return pbeCipher;
  }
  
  /**
   * 取得具有加密/解密的输出流对象
   * @param passWord    密码
   * @param salt        干扰字节
   * @param itCnt       迭代次数
   * @param mode        模式，加密/解密
   * @param os          输入流
   * @return
   * @throws Exception
   */
  public static OutputStream buildPassWordOutputStream(
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode,
      OutputStream os) throws Exception {
    
    Cipher cipher = getPassWordCipher(passWord, salt, itCnt, mode);
    CipherOutputStream rtOs = new CipherOutputStream(os, cipher);
    
    return rtOs;
  }
  
  /**
   * 取得具有加密/解密的输入流对象
   * @param passWord    密码
   * @param salt        干扰字节
   * @param itCnt       迭代次数
   * @param mode        模式，加密/解密
   * @param is          输入流
   * @return
   * @throws Exception
   */
  public static InputStream buildPassWordInputStream(
      Cipher cipher,
      InputStream is) throws Exception {

    CipherInputStream rtIs = new CipherInputStream(is, cipher);
    
    return rtIs;
  }
  
  /**
   * 取得具有加密/解密的输入流对象
   * @param passWord    密码
   * @param salt        干扰字节
   * @param itCnt       迭代次数
   * @param mode        模式，加密/解密
   * @param is          输入流
   * @return
   * @throws Exception
   */
  public static InputStream buildPassWordInputStream(
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode,
      InputStream is) throws Exception {
    
    Cipher cipher = getPassWordCipher(passWord, salt, itCnt, mode);
    CipherInputStream rtIs = new CipherInputStream(is, cipher);
    
    return rtIs;
  }
}
