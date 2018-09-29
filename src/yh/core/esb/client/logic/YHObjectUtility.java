package yh.core.esb.client.logic;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.util.ByteArrayBuffer;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class YHObjectUtility {
  public static String writeObject(Object o) throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(o);
    oos.flush();
    oos.close();
    bos.close();
    return new BASE64Encoder().encode(bos.toByteArray());
   }
  public static Object readObject(String object) throws Exception{
    ByteArrayInputStream bis = new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(object));
       ObjectInputStream ois = new ObjectInputStream(bis);
       Object o = ois.readObject();
       bis.close();
       ois.close();
    return o;
  }
}
