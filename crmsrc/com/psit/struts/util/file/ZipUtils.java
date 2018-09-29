package com.psit.struts.util.file;
import java.io.BufferedInputStream;   
import java.io.BufferedOutputStream;   
import java.io.File;   
import java.io.FileInputStream;   
import java.io.FileOutputStream;   
import java.util.Enumeration;   
   
import org.apache.tools.zip.ZipEntry;   
import org.apache.tools.zip.ZipFile;   
import org.apache.tools.zip.ZipOutputStream;   
 /**
  * 压缩文件操作类 <br>
  */
public class ZipUtils {   
    static final int BUFFER = 1024;   
    public static String encoding = "GBK";   
   
    public static void zip(String inputFilePath, String zipFilePath)   
            throws Exception {   
        File inputFile = new File(inputFilePath);   
        if(!inputFile.exists())return;   
        File zipFile = new File(zipFilePath);   
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));   
        out.setEncoding(encoding);   
        zip(out, inputFile, "");   
        out.close();   
    }   
   
    private static void zip(ZipOutputStream out, File f, String base)   
            throws Exception {   
        if (f.isDirectory()) {   
            File[] fl = f.listFiles();   
            out.putNextEntry(new ZipEntry(base + "/"));   
            base = base.length() == 0 ? "" : base + "/";   
            for (int i = 0; i < fl.length; i++) {   
                zip(out, fl[i], base + fl[i].getName());   
            }   
        } else {   
            out.putNextEntry(new ZipEntry(base));   
            FileInputStream in = new FileInputStream(f);   
            int len;   
            byte[] buf = new byte[BUFFER];   
            try {   
                while ((len = in.read(buf, 0, BUFFER)) != -1) {   
                    out.write(buf, 0, len);   
                }   
            } catch (Exception e) {   
                e.printStackTrace();   
            }finally{   
                in.close();   
            }   
               
        }   
    }   
   
    public static void unZip(String zipFilePath, String outPath) throws Exception{   
        ZipFile zipFile = new ZipFile(zipFilePath, encoding);   
        Enumeration<?> emu = zipFile.getEntries();   
        while (emu.hasMoreElements()) {   
            ZipEntry entry = (ZipEntry) emu.nextElement();   
            if (entry.isDirectory()) {   
                new File(outPath + entry.getName()).mkdirs();   
                continue;   
            }   
            File file = new File(outPath + entry.getName());   
            File parent = file.getParentFile();   
            if (parent != null && (!parent.exists())) {   
                parent.mkdirs();   
            }   
            BufferedInputStream bis = null;    
            FileOutputStream fos = null;   
            BufferedOutputStream bos = null;   
            try {   
                bis = new BufferedInputStream(zipFile   
                        .getInputStream(entry));   
                fos = new FileOutputStream(file);   
                bos = new BufferedOutputStream(fos, BUFFER);   
                byte[] buf = new byte[BUFFER];   
                int len = 0;   
                while ((len = bis.read(buf, 0, BUFFER)) != -1) {   
                    fos.write(buf, 0, len);   
                }   
                bos.flush();   
            } catch (Exception e) {   
                e.printStackTrace();   
            } finally{   
                if(bos!=null){   
                    bos.close();   
                }  
                if(fos!=null){
                	fos.close();
                }
                if(bis!=null){   
                    bis.close();   
                }   
            }   
        }   
        zipFile.close();   
    }   
   
    /*public static void main(String[] args) {   
        try {   
            ZipUtils.unZip("d:/1.zip", "d:/rar/");   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
    }   */
}  