package com.psit.struts.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
/**
 * 
 * 文件加密类 <br>
 *
 * create_date: Aug 20, 2010,2:27:59 PM<br>
 * @author zjr
 */
public class EncryptGen {
	 static final byte[] KEYVALUE = "DDDAAA78DAS<99999"
		   .getBytes();
	 static final int BUFFERLEN = 512;//缓存大小
	 static final int RESLENGTH=27;//文件长度
	 public EncryptGen(){} 
	 /**
	  * 
	  * 文件写入 <br>
	  * create_date: Aug 20, 2010,2:29:53 PM <br>
	  * @param fileName 文件名称
	  * @param iniStr 写入内容
	  * @throws Exception 抛出异常<br>
	  */
	 public static void encryptFile(String fileName,String iniStr)
	   throws Exception {
		  File file = new File(fileName);
		  if (!file.exists())
		   file.createNewFile();
		  FileOutputStream out = new FileOutputStream(file);
		  int  pos, keylen;
		  pos = 0;
		  keylen = KEYVALUE.length;
		  byte buffer[]=iniStr.getBytes();
		   for (int i = 0; i < buffer.length; i++) {
		    buffer[i] ^= KEYVALUE[pos];
		    out.write(buffer[i]);
		    pos++;
		    if (pos == keylen)
		     pos = 0;
		   }
		  out.close();
	 }
	 /**
	  * 
	  * 生成写入文件内容 <br>
	  * create_date: Aug 20, 2010,2:31:28 PM <br>
	  * @param fileName 文件名称
	  * @return String[] 返回文件内容数组
	  * @throws Exception 抛出异常<br>
	  */
	 public static String[] decryptFile(String fileName)
	   throws Exception {
		  FileInputStream in = new FileInputStream(fileName);
		  int c, pos, keylen,n;//c为保存在缓冲区中实际元素个数
		  pos = 0;
		  n=0;
		  keylen = KEYVALUE.length;
		  byte buffer[] = new byte[BUFFERLEN];//创建缓冲区
		  byte strBuffer[]=new byte[RESLENGTH];
		  while ((c = in.read(buffer)) != -1) {
		   for (int i = 0; i < c; i++) {
				buffer[i] ^= KEYVALUE[pos];
			    strBuffer[n++]=buffer[i];
			    pos++;
			    if (pos == keylen)
			     pos = 0;
			   }
		  }
		  in.close();
		  if(n>0)
		  {
			  String str=new String(strBuffer);
			  String[] result=str.split(",");
			  return result;
		  }
		  else
		  {
			  return null;
		  }
	 }
}
