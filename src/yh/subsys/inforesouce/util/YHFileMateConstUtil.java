package yh.subsys.inforesouce.util;

public class YHFileMateConstUtil {
  public static final String userName = "人名";
  public static final String areaName = "地名";
  public static final String Org = "机构名";
  public static final String  subJect= "主题词";
  public static final String keyWord = "关键词";
  
  /**
   * 截取M
   * @param fileNumberId
   * @return
   */
  public static int checkString(String fileNumberId){
    int fileNumbers = 0;
    if(fileNumberId!=null){
      fileNumbers = Integer.parseInt(fileNumberId.replace("M", "").replace("MEX", ""));
      
    }else{
      return 0;
    }
    return fileNumbers;
  }
  
}

