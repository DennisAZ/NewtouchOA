package oa.spring.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import oa.spring.util.StaticDataUtil;
import yh.core.funcs.system.info.act.YHInfoAct;

public class OpenAccount {

	
	public static void  init(String path) {
		
	    StaticDataUtil.LOCK_FILE_PATH1 = path + StaticDataUtil.LOCK_FILE_PATH;
	    File file = new File(StaticDataUtil.LOCK_FILE_PATH1);
	    File[] files = file.listFiles();
	    boolean licFlag=false; //判断开帐文件是否存在
	    for(int i = 0;i<files.length;i++){
	    	 if(files[i].isFile()){
	    		  String fileName = files[i].getName();
	      		  if(fileName.contains(".lock")){
	      			StaticDataUtil.sysInitDataFlag = 1;
	      			    licFlag=true;
	      			    break;
	      		  }
	    	 }
	    }
	  	 
	    if(licFlag){
	    	  StaticDataUtil.sysInitDataFlag = 1;
//	    	StaticDataUtil.lockIsExist(lockName); //文件存在
	    }else{
	    	StaticDataUtil.sysInitDataFlag=0;
	    }
}
	
	/**
	 * 开帐
	 * 
	 * @return 成功或则失败的信息
	 */
  public  String openAccount(){
	  String rtData = "";
	  StaticDataUtil.sysInitDataFlag = 1; //1、先将其系统变量置位
 	  boolean boo = false;
 	  StaticDataUtil.TOMCAT_PATH = StaticDataUtil.LOCK_FILE_PATH1;
	  String createFileName = StaticDataUtil.flagFileName;
	  File myFile = new File(StaticDataUtil.LOCK_FILE_PATH1+createFileName);
	    try {
			boo = myFile.createNewFile();
			if(boo){
		   		  FileWriter resultFile = new FileWriter(myFile);
		   		  PrintWriter myNewFile = new PrintWriter(resultFile);
		   		  myNewFile.println(StaticDataUtil.sysInitDataFlag);
				  if(resultFile != null){
					  resultFile.close();
				  }
				  if(myNewFile != null){
					  myNewFile.close();
				  }
				  rtData = "{rtState:'0'}";
		   	  }else{
		   		 rtData = "{rtState:'1'}";
		   	  }
		} catch (IOException e) {
			 rtData = "{rtState:'1'}";
		}
		return rtData;
  }
  
  /**
   * 判断是否开帐成功
   * 
   * @return
   */
  public static boolean isOpenAccount(){
	boolean isOpenAccount = false;
	if(StaticDataUtil.sysInitDataFlag == 1){
		isOpenAccount = true;
	}else if(StaticDataUtil.sysInitDataFlag == 0){
		isOpenAccount = false;
	}
	return isOpenAccount;
  }
}
