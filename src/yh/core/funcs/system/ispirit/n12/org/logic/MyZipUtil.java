package yh.core.funcs.system.ispirit.n12.org.logic;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import java.io.*;
import java.util.zip.ZipFile;




import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;


public class MyZipUtil {
  
  /**
   * 设置缓冲值
   */
  static final int BUFFER = 8192;
  
  private static final String ALGORITHM = "PBEWithMD5AndDES";

  public static void zip(String zipFileName, String inputFile,String pwd) throws Exception {
    zip(zipFileName, new File(inputFile), pwd);
  }
  /**
   * 功能描述：压缩指定路径下的所有文件
   * @param zipFileName 压缩文件名(带有路径)
   * @param inputFile   指定压缩文件夹
   * @return
   * @throws Exception  
   */
  public static void zip(String zipFileName, String inputFile) throws Exception {
    zip(zipFileName, new File(inputFile), null);
  }

  
  /**
   * 功能描述：压缩文件对象
   * @param zipFileName 压缩文件名(带有路径)
   * @param inputFile   文件对象
   * @return
   * @throws Exception
   */
  public static void zip(String zipFileName, File inputFile,String pwd) throws Exception {
    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
    zip(out, inputFile, "",pwd);
    out.close();
  }

  /**
   * 
   * @param out  压缩输出流对象
   * @param file 
   * @param base
   * @throws Exception
   */
  public static void zip(ZipOutputStream outputStream, File file, String base,String pwd) throws Exception {
    if (file.isDirectory()) {
      File[] fl = file.listFiles();
      outputStream.putNextEntry(new ZipEntry(base + "/"));
      base = base.length() == 0 ? "" : base + "/";
      for (int i = 0; i < fl.length; i++) {
        zip(outputStream, fl[i], base + fl[i].getName(), pwd);
      }
    } 
    else {
      outputStream.putNextEntry(new ZipEntry(base));
      FileInputStream inputStream = new FileInputStream(file);
      //普通压缩文件
      if(pwd == null || pwd.trim().equals("")){
        int b;
        while ((b = inputStream.read()) != -1){
          outputStream.write(b);
        }
        inputStream.close();
      }
      //给压缩文件加密
      else{
        PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray());
          SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
          SecretKey passwordKey = keyFactory.generateSecret(keySpec);
          byte[] salt = new byte[8];
          Random rnd = new Random();
          rnd.nextBytes(salt);
          int iterations = 100;
          PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);
          Cipher cipher = Cipher.getInstance(ALGORITHM);
          cipher.init(Cipher.ENCRYPT_MODE, passwordKey, parameterSpec);
          outputStream.write(salt);
          byte[] input = new byte[64];
          int bytesRead;
          while ((bytesRead = inputStream.read(input)) != -1) {
            byte[] output = cipher.update(input, 0, bytesRead);
            if (output != null){
              outputStream.write(output);
            }
          }
          byte[] output = cipher.doFinal();
          if (output != null){
            outputStream.write(output);
          }
          inputStream.close();
          outputStream.flush();
          outputStream.close();
      }
    }
  //  file.delete();
  }
  /**
   *压缩Zip文件 
   */
  public void ZipFile(String ZipFile,String file)throws Exception{
    try {
      ZipFile zipFile = new ZipFile(file);
      Enumeration enumeration = zipFile.entries();


      while (enumeration.hasMoreElements()) {
          ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
          System.out.println("解压中...: " + zipEntry.getName());


          BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));


          int size;
          byte[] buffer = new byte[2048];


          FileOutputStream fos = new FileOutputStream(zipEntry.getName());
          BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);


          while ((size = bis.read(buffer, 0, buffer.length)) != -1) {
              bos.write(buffer, 0, size);
          }


          bos.flush();
          bos.close();
          fos.close();


          bis.close();
      }
  } catch (IOException e) {
      e.printStackTrace();
  }


  }
  
  /** 
   * 功能：把 sourceDir 目录下的所有文件进行 zip 格式的压缩，保存为指定 zip 文件
   * @param sourceDir 如果是目录，eg：D:\\MyEclipse\\first\\testFile，则压缩目录下所有文件；
   *      如果是文件，eg：D:\\MyEclipse\\first\\testFile\\aa.zip，则只压缩本文件
   * @param zipFile 最后压缩的文件路径和名称,eg:D:\\MyEclipse\\first\\testFile\\aa.zip
   */
  public File doZip(String sourceDir, String zipFilePath) 
  throws IOException {
   
   File file = new File(sourceDir);
   File zipFile = new File(zipFilePath);
   ZipOutputStream zos = null;
   try {
    // 创建写出流操作
    OutputStream os = new FileOutputStream(zipFile);
    BufferedOutputStream bos = new BufferedOutputStream(os);
    zos = new ZipOutputStream(bos);
    
    String basePath = null;
    
    // 获取目录
    if(file.isDirectory()) {
     basePath = file.getPath();
    }else {
     basePath = file.getParent();
    }
    
    zipFile(file, basePath, zos);
   }finally {
    if(zos != null) {
     zos.closeEntry();
     zos.close();
    }
   }
   
   return zipFile;
  }


  
  
  
  /** 
   * @param source 源文件
   * @param basePath 
   * @param zos 
   */
  private void zipFile(File source, String basePath, ZipOutputStream zos) 
  throws IOException {
   File[] files = null;
   if (source.isDirectory()) {
    files = source.listFiles();
   } else {
    files = new File[1];
    files[0] = source;
   }
   
   InputStream is = null;
   String pathName;
   byte[] buf = new byte[1024];
   int length = 0;
   try{
    for(File file : files) {
     if(file.isDirectory()) {
      pathName = file.getPath().substring(basePath.length() + 1) + "/";
      zos.putNextEntry(new ZipEntry(pathName));
      zipFile(file, basePath, zos);
     }else {
      pathName = file.getPath().substring(basePath.length() + 1);
      is = new FileInputStream(file);
      BufferedInputStream bis = new BufferedInputStream(is);
      zos.putNextEntry(new ZipEntry(pathName));
      while ((length = bis.read(buf)) > 0) {
       zos.write(buf, 0, length);
      }
     }
    }
   }finally {
    if(is != null) {
     is.close();
    }
   }
  }

  
  public static void unzip(String zipFileName, String outputDirectory)throws Exception {
    ZipInputStream inputStream = new ZipInputStream(new FileInputStream(zipFileName));
    unzip(inputStream, outputDirectory, null);
  }
  
  
  /**
   * 功能描述：将压缩文件解压到指定的文件目录下
   * @param zipFileName      压缩文件名称(带路径)
   * @param outputDirectory  指定解压目录
   * @return
   * @throws Exception
   */
  public static void unzip(String zipFileName, String outputDirectory, String pwd)throws Exception {
    ZipInputStream inputStream = new ZipInputStream(new FileInputStream(zipFileName));
    unzip(inputStream, outputDirectory, pwd);
  }
  
  
  public static void unzip(File zipFile, String outputDirectory, String pwd)throws Exception {
    ZipInputStream inputStream = new ZipInputStream(new FileInputStream(zipFile));
    unzip(inputStream, outputDirectory,pwd);
  }
  
  
  public static void unzip(ZipInputStream inputStream, String outputDirectory, String pwd) throws Exception{
    ZipEntry zipEntry = null;
    FileOutputStream outputStream = null;
    try{
      while ((zipEntry = inputStream.getNextEntry()) != null) {
        if (zipEntry.isDirectory()) {
          String name = zipEntry.getName();
          name = name.substring(0, name.length() - 1);
          File file = new File(outputDirectory + File.separator + name);
          file.mkdir();
        } 
        else {
          File file = new File(outputDirectory + File.separator + zipEntry.getName());
          file.createNewFile();
          outputStream = new FileOutputStream(file);
          //普通解压缩文件
          if(pwd == null || pwd.trim().equals("")){
            int b;
            while ((b = inputStream.read()) != -1){
              outputStream.write(b);
            }
            outputStream.close();
          }
          //解压缩加密文件
          else{
            PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray());
              SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
              SecretKey passwordKey = keyFactory.generateSecret(keySpec);
              byte[] salt = new byte[8];
              inputStream.read(salt);
              int iterations = 100;
              PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);
              Cipher cipher = Cipher.getInstance(ALGORITHM);
              cipher.init(Cipher.DECRYPT_MODE, passwordKey, parameterSpec);
              byte[] input = new byte[64];
              int bytesRead;
              while ((bytesRead = inputStream.read(input)) != -1) {
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null){
                  outputStream.write(output);
                }
              }
              byte[] output = cipher.doFinal();
              if (output != null){
                outputStream.write(output);
              }
              outputStream.flush();
              outputStream.close();
          }
        }
      }
      inputStream.close();
    }
    catch(IOException ex){
      throw new Exception("解压读取文件失败");
    }
    catch(Exception ex){
      throw new Exception("解压文件密码不正确");
    }
    finally{
      inputStream.close();
      outputStream.flush();
        outputStream.close();
    }
  }
  
  

  }
