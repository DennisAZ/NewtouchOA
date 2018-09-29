package yh.subsys.inforesouce.util;

import javax.servlet.http.HttpServletRequest;

import yh.core.global.YHConst;
import yh.core.util.file.YHFileUtility;


public class YHTempFileUtil{ 

  private static final YHTempFileUtil cu= new YHTempFileUtil();
  
  public synchronized String readContent(HttpServletRequest request, String FILE_PATH) throws Exception{    
    String sp = System.getProperty("file.separator");
    String path = request.getSession().getServletContext().getRealPath(sp)+ "subsys" + sp + "inforesource" +sp;   
    
    String temp = YHFileUtility.loadLine2Buff(path + FILE_PATH, YHConst.DEFAULT_CODE).toString();
   
    return temp;
  }
  
  public static YHTempFileUtil getInstance(){
    return cu;
  }
}
