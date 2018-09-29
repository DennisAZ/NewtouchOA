package yh.core.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class YHDBMapUtil {
	public static String getMapDBName(String pDBName){
		if(pDBName!=null && !pDBName.equals("")){
			Object mapDb=YHDBMapExcelReader.GDBMap.get(pDBName);
			if(mapDb!=null)
				return mapDb.toString();
		}
		return pDBName;
	}
	public static String read(File src) {
		  StringBuffer res = new StringBuffer();
		  String line = null;
		  try {
		   BufferedReader reader = new BufferedReader(new FileReader(src));
		   while ((line = reader.readLine()) != null) {
		    res.append(line + "\n");
		   }
		   reader.close();
		  } catch (FileNotFoundException e) {
		   e.printStackTrace();
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		  return res.toString();
		 }

		 public static boolean write(String cont, File dist) {
		  try {
		   BufferedWriter writer = new BufferedWriter(new FileWriter(dist));
		   writer.write(cont);
		   writer.flush();
		   writer.close();
		   return true;
		  } catch (IOException e) {
		   e.printStackTrace();
		   return false;
		  }
		 }

	
	public static void main(String []args){
		try {
			replaceAllSql();
//			showAllFiles(new File("E:\\COA-Enterprise\\yh\\"));
//			System.out.println(r);
//			File a=new File("tt.xls");
//			System.out.println(a.getName().endsWith(".xls"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static int r=0;
	private static void replaceAllCode(File src){
	
		  String cont = YHDBMapUtil.read(src);
		  
		if(YHDBMapExcelReader.GDBMap.size()==0)
		{
			new YHDBMapExcelReader().initYHDBMapExcelReader();
		}
		Set<String> s=YHDBMapExcelReader.GDBMap.keySet();
		for(String k:s){
//			System.out.println(k);
//			if(k.trim().equalsIgnoreCase("oa_url"))oa_secure_key where
			cont = cont.replaceAll(YHDBMapExcelReader.GDBMap.get(k).toString()+"where",YHDBMapExcelReader.GDBMap.get(k)+" where");
		}
		  System.out.println(YHDBMapUtil.write(cont, src));
	}
	final static void showAllFiles(File dir) throws Exception{
		  File[] fs = dir.listFiles();
		  for(int i=0; i<fs.length; i++){
		   System.out.println(fs[i].getAbsolutePath());
		   r++;
		   if(fs[i].isDirectory()){
		    try{
		     showAllFiles(fs[i]);
		    }catch(Exception e){}
		   }else{
			   if(fs[i].getName().endsWith(".java") || fs[i].getName().endsWith(".jsp") || fs[i].getName().endsWith(".properties") || fs[i].getName().endsWith(".xml"))
//				   if(fs[i].getName().endsWith(".jsp"))
					   replaceAllCode(fs[i]); 
		   }
		  }
		 }
	private static void replaceAllSql(){
		 File src = new File("E:\\数据库命名改造\\20130510.sql");
		  String cont = YHDBMapUtil.read(src);
		new YHDBMapExcelReader().initYHDBMapExcelReader();
		Set<String> s=YHDBMapExcelReader.GDBMap.keySet();
		for(String k:s){
			System.out.println(k);
			cont = cont.replaceAll(k.trim(), YHDBMapExcelReader.GDBMap.get(k).toString().trim());
		}
		  System.out.println(YHDBMapUtil.write(cont, src));
	}
}
