package yh.core.install;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import org.apache.tools.zip.ZipOutputStream;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipEntry;

public class YHZipManager
{
private String srcPath;
private String desPath;
private int buffer;

    public YHZipManager() 
    {
    srcPath = "";
    desPath = "";
    buffer = 1024;
    }
    
    public void setsrcPath(String srcPath)
    {
    this.srcPath = srcPath;
    } 
    
    public String getsrcPath()
    {
    return srcPath;
    } 
    
    public void setdesPath(String desPath)
    {
    this.desPath = desPath;
    }
    
    public String getdesPath()
    {
    return desPath;
    }
    
    private boolean isdesPathNull()
    {
    return (desPath.length() == 0);
    }
    
    private boolean issrcPathNull()
    {
    return (srcPath.length() == 0);
    }
       
    public void zip()
    {
    if(issrcPathNull())
    {
       System.out.println("请设置要压缩的文件或目录路径！");
       return;
    }
    if(isdesPathNull()) 
    {
       System.out.println("请设置压缩文件保存路径！");
       return;
    }
    try
    {
       System.out.println("压缩的文件或目录：" + srcPath);
       System.out.println("压缩到：" + desPath);
       FileOutputStream fos = new FileOutputStream(new File(desPath));
       ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(fos,buffer));
       File inputFile = new File(srcPath);
       zip(out, inputFile, "");
       out.close();
       System.out.println("压缩完毕！");
    }
    catch(Exception e)
    {
       System.out.println(e);
    }
    } 
    
    private void zip(ZipOutputStream out, File inputFile, String base)
    {
    try
    {
       if(inputFile.isDirectory())
       {
        File[] file = inputFile.listFiles();
        out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
        System.out.println("压缩路径：" + base + "/");
        base = (base.length() == 0) ? "" : (base + "/");
     for(int i = 0; i < file.length; i++)
     {
         zip(out, file[i], base + file[i].getName());
     }
       }
       else
       {
        out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
        byte[] data = new byte[buffer];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile), buffer);
        System.out.println("正在压缩文件：" + base);
        int cnt;
        while((cnt = bis.read(data, 0 , buffer)) != -1)
        {
         out.write(data, 0, buffer);
        }
        out.flush();
        bis.close();
       }
    }
    catch(Exception e)
    {
       System.out.println(e);
    }
    }
    
    public void unzip()
    {
    if(issrcPathNull())
    {
       System.out.println("请设置要解压的zip文件！");
       return;
    }
    if(isdesPathNull()) 
    {
       System.out.println("请设置文件解压的路径！");
       return;
    }
    File outFile = new File(desPath);
    if (!outFile.exists()) {
            outFile.mkdirs();
        }
    try
    {
       ZipFile zipFile = new ZipFile(srcPath);
       Enumeration en = zipFile.getEntries();
       ZipEntry zipEntry = null;
       createDirectory(desPath.replace("\\","/"),"");
       while (en.hasMoreElements())
       {
        zipEntry = (ZipEntry) en.nextElement();
        int index = zipEntry.getName().lastIndexOf("/");
        if(index == -1) index = 0;
        createDirectory(desPath.replace("\\","/"),zipEntry.getName().substring(0,index));
        if (zipEntry.isDirectory())
        {
         continue;
        }
        else
        {
         File file = new File(outFile.getPath() + "/" + zipEntry.getName());
         System.out.println("正在解压文件：" + zipEntry.getName());
         file.createNewFile();
         BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry),buffer);
         BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file),buffer);
         int cnt;
         byte[] data = new byte[buffer];
         while((cnt = bis.read(data, 0, buffer)) != -1)
         {
          out.write(data, 0, cnt);
         }
         bis.close();
         out.close();
         System.out.println(zipEntry.getName() + "解压完毕！");
        }
       }
       System.out.println("解压完毕！");
    }
    catch(Exception e)
    {
       System.out.println(e);
    }
    }
    
    private static void createDirectory(String directory, String subDirectory)
    {
        String[] dir;
        System.out.println("传过来的subDirectory: " + subDirectory);
        File file = new File(directory);
        try 
        {
            if ((subDirectory.length() == 0) && (!file.exists()))
                file.mkdirs();
            else if (subDirectory.length() != 0)
            {
            dir = subDirectory.replace("\\", "/").split("/");
            String temstr = directory;
            File temfile = null;
            for(int i = 0; i < dir.length; i++)
            {
               System.out.println("分离的目录名：" + dir[i]);
               temstr += "/" + dir[i]; 
               temfile = new File(temstr);
               if(!temfile.exists())
               {
                System.out.println("创建目录：" + temstr);
                boolean bl = temfile.mkdir();
                System.out.println("创建目录：" + bl);
               }
               else
               {
                System.out.println(temstr + "目录已存在！");
               }
            }
            }
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }
    }
    
    public void delete()
    {
    if(issrcPathNull())
    {
       System.out.println("请设置要删除的文件或目录！");
       return;
    }
    File file = new File(srcPath);
    if(!file.exists())
    {
       System.out.println("您要删除的文件或目录不存在！");
       return;
    }
    System.out.println("您要删除的文件或目录：" + file.getPath());
    delete(file);
    System.out.println("删除目录完毕！");
    }
    
    public void delete(File file)
    {
    try
    {
       if(file.isDirectory())
       {
        File[] f = file.listFiles();
        for(int i = 0; i < f.length; i++)
        {
         delete(f[i]);
        }
        file.delete();
        System.out.println(file.getName() + " 目录已删除！");
       }
       else
       {
        file.delete();
        System.out.println(file.getName() + " 文件已删除！");
       }
    }
    catch(Exception e)
    {
       System.out.println(e);
    }
    }
    
    public static void main (String[] args) 
    {
    try
    {
       YHZipManager dz = new YHZipManager();
       dz.setdesPath("d:" + "/" + "测试.zip");
       dz.setsrcPath("e:" + "/" + "测试");
       dz.zip();

       //dz.setsrcPath("d:" + "/" + "测试.zip");
      // dz.setdesPath("d:" + "/" + "测试");
      // dz.unzip();
      
       //dz.setsrcPath("d:" + "/" + "测试");
       //dz.delete();
    }
    catch(Exception e)
    {
       System.out.println(e);
    }
} 
}
