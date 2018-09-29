package yh.cms.common.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.cms.column.data.YHCmsColumn;
import yh.cms.content.data.YHCmsContent;
import yh.cms.station.data.YHCmsStation;
import yh.cms.template.data.YHCmsTemplate;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;

public class YHCmsCommonLogic {
  public static final String attachmentFolder = "cms";
  /**
   * 读取文章模板文件，转换模板内容--文章
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public StringBuffer readFile(Connection conn, File templateFile, YHCmsContent content) throws Exception{
    YHORM orm = new YHORM();
    YHCmsStation station = (YHCmsStation) orm.loadObjSingle(conn, YHCmsStation.class, content.getStationId());
    YHCmsColumn column = (YHCmsColumn) orm.loadObjSingle(conn, YHCmsColumn.class, content.getColumnId());
    List<YHCmsContent> contentList = new ArrayList<YHCmsContent>();
    contentList.add(content);
    StringBuffer sb = resolve(conn, templateFile, station, column, contentList);
    return sb;
  }
  
  /**
   * 读取索引模板文件，转换模板内容--栏目
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public StringBuffer readFile(Connection conn, File templateFile, YHCmsStation station, YHCmsColumn column, List<YHCmsContent> contentList, int... i) throws Exception{
    StringBuffer sb = resolve(conn, templateFile, station, column, contentList, i);
    return sb;
  }
  
  /**
   * 读取索引模板文件，转换模板内容--站点
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public StringBuffer readFile(Connection conn, File templateFile, YHCmsStation station) throws Exception{
    StringBuffer sb = resolve(conn, templateFile, station, null, null);
    return sb;
  }
  
  public StringBuffer resolve(Connection conn, File templateFile, YHCmsStation station, YHCmsColumn column, List<YHCmsContent> contentList, int... i) throws Exception{
    StringBuffer sb = new StringBuffer("");
    BufferedReader reader = null;
    try{
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile),"utf-8"));
      String tempStr = "";
      String foreachStr = "";
      String columnParameters = "";
      boolean foreachBoolean = false;
      int line = 0;
      while((tempStr = reader.readLine()) != null){
        
        //计数器--记录行数
        line++;
        
        //解决读文件首字节出现特殊编码字符的问题--导致模板换行
        if(line == 1){
          String ttt = new String(tempStr.getBytes(),"utf-8");
          if(ttt.startsWith("?")){
            tempStr = ttt.substring(1, ttt.length());
          }
        }
        
        //包含模板
        if(tempStr.contains("#parse(")){
          YHORM orm = new YHORM();
          columnParameters = tempStr.substring(tempStr.indexOf("(")+1, tempStr.indexOf(")"));
          Map<String,String> map = new HashMap<String,String>();
          map.put("TEMPLATE_NAME", columnParameters);
          YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(conn, YHCmsTemplate.class, map);
          if(template != null){
            File templateFileContain = new File(YHSysProps.getAttachPath() + File.separator + attachmentFolder + File.separator + template.getAttachmentId() + "_" + template.getAttachmentName());
            tempStr = tempStr.replace("#parse("+columnParameters+")", readFile(conn, templateFileContain, station, column, contentList).toString());
            sb.append(tempStr);
          }
          columnParameters = "";
          continue;
        }
        
        //对循环处理--文章foreach循环
        if(tempStr.contains("#foreachContent")){
          if(tempStr.contains("#foreachContent(")){
            columnParameters = tempStr.substring(tempStr.indexOf("(")+1, tempStr.indexOf(")"));
            foreachStr = foreachStr + tempStr.substring(tempStr.indexOf("#foreachContent("), tempStr.length()).replace("#foreachContent("+columnParameters+")", "") + "\n";
          }
          else{
            foreachStr = foreachStr + tempStr.substring(tempStr.indexOf("#foreachContent"), tempStr.length()).replaceAll("#foreachContent", "") + "\n";
          }
          tempStr = tempStr.substring(0,tempStr.indexOf("#foreachContent"));
          foreachBoolean = true;
        }
         
        if(tempStr.contains("#endContent") && foreachBoolean){
          
          //判断是否有参数
          if(!YHUtility.isNullorEmpty(columnParameters)){
            foreachStr = foreachStr + tempStr.substring(0, tempStr.indexOf("#endContent")) + "\n";
            
            //获取出来的list 是有排序的
            YHORM orm = new YHORM();
            String[] parameter = columnParameters.split(",");
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("COLUMN_NAME", parameter[0]);   //parameter[0]栏目名称
            YHCmsColumn columnInfo = (YHCmsColumn) orm.loadObjSingle(conn, YHCmsColumn.class, map);
            String filters[] = {" COLUMN_ID =" + columnInfo.getSeqId() + " and CONTENT_STATUS = 5 order by CONTENT_TOP desc, CONTENT_INDEX desc "};
            List<YHCmsContent> contentListTemp = orm.loadListSingle(conn, YHCmsContent.class, filters);
            List<YHCmsContent> contentListInfo = new ArrayList<YHCmsContent>();
            int count = 0;
            if(contentListTemp.size() > Integer.parseInt(parameter[1])){    //parameter[1]显示条数
              for(YHCmsContent content : contentListTemp){
                contentListInfo.add(content);
                count++;
                if(count == Integer.parseInt(parameter[1])){
                  break;
                }
              }
            }
            else{
              contentListInfo = contentListTemp;
            }
            
            tempStr = getForeachContent(conn, foreachStr, station, contentListInfo) + tempStr.substring(tempStr.indexOf("#endContent"), tempStr.length()).replaceAll("#endContent", "");
          }
          else{
            
            YHORM orm = new YHORM();
            foreachStr = foreachStr + tempStr.substring(0, tempStr.indexOf("#endContent")) + "\n";
            tempStr = getForeachContent(conn, foreachStr, station, contentList) + tempStr.substring(tempStr.indexOf("#endContent"), tempStr.length()).replaceAll("#endContent", "");
            
            //是否分页，如果分页则显示分页信息
            if(column.getPaging() == 1){
              int maxIndexPage = column.getMaxIndexPage();
              int pagingNumber = column.getPagingNumber(); 
              
              String filters[] = {" COLUMN_ID =" + column.getSeqId() + " and CONTENT_STATUS = 5 order by CONTENT_TOP desc, CONTENT_INDEX desc "};
              List<YHCmsContent> contentListAll = orm.loadListSingle(conn, YHCmsContent.class, filters);
              int total = contentListAll.size();
              
              int page = total/pagingNumber + (total%pagingNumber > 0 ? 1 : 0);
              if(page > maxIndexPage){
                total = maxIndexPage * pagingNumber;
              }
              page = page > maxIndexPage ? maxIndexPage : page;
              
              YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(conn, YHCmsTemplate.class, column.getTemplateIndexId());
              String fileName = template.getTemplateFileName();
              String prv = "";
              String next = "";
              if(i[0] - 1 <= 0){
                prv = fileName+"."+station.getExtendName();
              }
              else{
                prv = fileName+(i[0] - 1)+"."+station.getExtendName();
              }
              if(i[0] + 1 >= page){
                next = fileName+(page-1)+"."+station.getExtendName();
              }
              else{
                next = fileName+(i[0]+1)+"."+station.getExtendName();
              }
              
              tempStr = tempStr +" <table width=\"100%\" border=\"0\" cellspacing=\"5\" cellpadding=\"0\">\n " 
                                + "<tr>\n " 
                                + "<td align=\"center\" class=\"dahei\"><table> " 
                                + "共"+ total +"条新闻，分"+page+"页，当前第<font color=red>" + (i[0]+1) + "</font>页&nbsp;&nbsp;"
                                +	"<a href=\""+(fileName+"."+station.getExtendName())+"\">最前页</a> " 
                                + "<a href=\""+prv+"\">上一页</a> " 
                                + "<a href=\""+next+"\">下一页</a> " 
                                + "<a href=\""+(fileName+(page-1)+"."+station.getExtendName())+"\">最后页</a> " 
                                + "</table> ";
            }
          }
          foreachStr = "";
          foreachBoolean = false;
        }

        //对循环处理
        if(tempStr.contains("#foreachColumn")){
          foreachStr = foreachStr + tempStr.substring(tempStr.indexOf("#foreachColumn"), tempStr.length()).replaceAll("#foreachColumn", "") + "\n";
          tempStr = tempStr.substring(0,tempStr.indexOf("#foreachColumn"));
          foreachBoolean = true;
        }
        if(tempStr.contains("#endColumn") && foreachBoolean){
          foreachStr = foreachStr + tempStr.substring(0, tempStr.indexOf("#endColumn")) + "\n";
          tempStr = getForeachColumn(conn, foreachStr, station) + tempStr.substring(tempStr.indexOf("#endColumn"), tempStr.length()).replaceAll("#endColumn", "");
          foreachStr = "";
          foreachBoolean = false;
        }
        if(foreachBoolean){
          foreachStr = foreachStr + tempStr + "\n";
          continue;
        }
        
        //替换站点名称
        if(tempStr.contains("$CMSstation.getStationName")){
          tempStr = tempStr.replaceAll("\\$CMSstation\\.getStationName", station.getStationName());
        }
        //替换站点首页url
        if(tempStr.contains("$CMSstation.getUrl")){
          YHORM orm = new YHORM();
          YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(conn, YHCmsTemplate.class, station.getTemplateId());
          String fileName = template.getTemplateFileName();
          tempStr = tempStr.replaceAll("\\$CMSstation\\.getUrl", "/yh/" + station.getStationPath() + "/" + fileName + "." + station.getExtendName());
        }
        //替换栏目名称
        if(tempStr.contains("$CMScolumn.getColumnName")){
          tempStr = tempStr.replaceAll("\\$CMScolumn\\.getColumnName", column.getColumnName());
        }
        //替换栏目url
        if(tempStr.contains("$CMScolumn.getUrl")){
          //替换指定栏目url 带参数
          if(tempStr.contains("$CMScolumn.getUrl(")){
            String columnName = tempStr.substring(tempStr.indexOf("(")+1, tempStr.indexOf(")"));
            YHORM orm = new YHORM();
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("COLUMN_NAME", columnName);
            YHCmsColumn columnInfo = (YHCmsColumn) orm.loadObjSingle(conn, YHCmsColumn.class, map);
            if(columnInfo != null){
              YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(conn, YHCmsTemplate.class, station.getTemplateId());
              String fileName = template.getTemplateFileName();
              String path = getColumnPath(conn, columnInfo.getSeqId());
              fileName = "/yh/" + station.getStationPath() + "/" + path + "/" + fileName + "." + station.getExtendName();
              tempStr = tempStr.replace("$CMScolumn.getUrl("+ columnName +")", fileName);
            }
            else{
              tempStr = tempStr.replace("$CMScolumn.getUrl("+ columnName +")", "");
            }
          }
        }
        //替换文章名称
        if(tempStr.contains("$CMScontent.getContentName")){
          tempStr = tempStr.replaceAll("\\$CMScontent\\.getContentName", contentList.get(0).getContentName());
        }
        //替换文章作者
        if(tempStr.contains("$CMScontent.getContentAuthor")){
          tempStr = tempStr.replaceAll("\\$CMScontent\\.getContentAuthor", contentList.get(0).getContentAuthor());
        }
        //替换文章来源
        if(tempStr.contains("$CMScontent.getContentSource")){
          tempStr = tempStr.replaceAll("\\$CMScontent\\.getContentSource", contentList.get(0).getContentSource());
        }
        //替换文章发布日期
        if(tempStr.contains("$CMScontent.getContentDate")){
          String date = "";
          if(contentList.get(0).getContentDate() != null){
            date = contentList.get(0).getContentDate().toString().substring(0,19);
          }
          tempStr = tempStr.replaceAll("\\$CMScontent\\.getContentDate", date);
        }
        //替换文章内容
        if(tempStr.contains("$CMScontent.getContent")){
          tempStr = tempStr.replaceAll("\\$CMScontent\\.getContent", contentList.get(0).getContent());
        }
        //替换所在位置
        if(tempStr.contains("#getLocation")){
          tempStr = tempStr.replaceAll("#getLocation", getLocation(conn, column, "../") + " >" + column.getColumnName());
//          System.out.println(getLocation(conn, column, "../") + " >> " + column.getColumnName());
        }
        sb.append(tempStr+"\n");
      }
    }catch(Exception e){
      throw e;
    }
    finally{
      reader.close();
    }
    return sb;
  }
  
  
  
  
  
  
  /**
   * 查询循环中的信息
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String getForeachContent(Connection conn, String foreachStr, YHCmsStation station, List<YHCmsContent> contentList) throws Exception{
    String data = "";
    try{
      for(YHCmsContent content : contentList){
        String tempStr = foreachStr;
        if(tempStr.contains("$CMScontent.getContentName")){
          tempStr = tempStr.replaceAll("\\$CMScontent\\.getContentName", content.getContentName());
        }
        if(tempStr.contains("$CMScontent.getContentDate")){
          String date = "";
          if(content.getContentDate() != null){
            date = content.getContentDate().toString().substring(0,19);
          }
          tempStr = tempStr.replaceAll("\\$CMScontent\\.getContentDate", date);
        }
        if(tempStr.contains("$CMScontent.getUrl")){
          String fileName = content.getContentFileName();
          if(YHUtility.isNullorEmpty(content.getContentFileName())){
            fileName = content.getSeqId()+"";
          }
          String path = getColumnPath(conn, content.getColumnId());
          fileName = "/yh/" + station.getStationPath() + "/" + path + "/" + fileName + "." + station.getExtendName();
          tempStr = tempStr.replaceAll("\\$CMScontent\\.getUrl", fileName);
        }
        data = data + tempStr;
      }
    }catch(Exception e){
      throw e;
    }
    return data;
  }
  
  /**
   * 查询循环中的信息
   * 
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public String getForeachColumn(Connection conn, String foreachStr, YHCmsStation station) throws Exception{
    String data = "";
    YHORM orm = new YHORM();
    
    //获取出来的list 是有排序的
    String filters[] = {" STATION_ID =" + station.getSeqId() + " and PARENT_ID =" + 0 + " order by COLUMN_INDEX desc "};
    List<YHCmsColumn> columnList = orm.loadListSingle(conn, YHCmsColumn.class, filters);
    try{
      for(YHCmsColumn column : columnList){
        String tempStr = foreachStr;
        if(tempStr.contains("$CMScolumn.getColumnName")){
          tempStr = tempStr.replaceAll("\\$CMScolumn\\.getColumnName", column.getColumnName());
        }
        if(tempStr.contains("CMScolumn.getUrl")){
          YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(conn, YHCmsTemplate.class, station.getTemplateId());
          String fileName = template.getTemplateFileName();
          String path = getColumnPath(conn, column.getSeqId());
          fileName = "/yh/" + station.getStationPath() + "/" + path + "/" + fileName + "." + station.getExtendName();
          tempStr = tempStr.replaceAll("\\$CMScolumn\\.getUrl", fileName);
        }
        data = data + tempStr;
      }
    }catch(Exception e){
      throw e;
    }
    return data;
  }
  
  /**
   * 递归获取目录结构
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public String getColumnPath(Connection dbConn, int columnId) throws Exception{
    YHORM orm = new YHORM();
    YHCmsColumn column = (YHCmsColumn) orm.loadObjSingle(dbConn, YHCmsColumn.class, columnId);
    String parentPath = column.getColumnPath();
    if(column.getParentId() > 0){
      parentPath = getColumnPath(dbConn, column.getParentId()) + "/" + parentPath;
    }
    return parentPath;
  }
  
  /**
   * 递归获取栏目地址
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public String getLocation(Connection dbConn, YHCmsColumn column, String temp) throws Exception{
    YHORM orm = new YHORM();
    YHCmsStation station = (YHCmsStation) orm.loadObjSingle(dbConn, YHCmsStation.class, column.getStationId());
    YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(dbConn, YHCmsTemplate.class, column.getTemplateIndexId());
    String location = "";
    if(column.getParentId() == 0){
      location = "<a href=\""+temp+template.getTemplateFileName()+"."+station.getExtendName()+"\">首页</a>";
    }
    else{
      YHCmsColumn columnParent = (YHCmsColumn) orm.loadObjSingle(dbConn, YHCmsColumn.class, column.getParentId());
      String abc = getLocation(dbConn, columnParent, temp+"../");
      location = abc + " ><a href=\""+temp+template.getTemplateFileName()+"."+station.getExtendName()+"\">" + columnParent.getColumnName() + "</a>";
    }
    return location;
  }
  
  public YHCmsStation getStationInfo(Connection conn, int stationId) throws Exception{
    YHORM orm = new YHORM();
    YHCmsStation station = (YHCmsStation) orm.loadObjSingle(conn, YHCmsStation.class, stationId);
    YHCmsTemplate template = (YHCmsTemplate) orm.loadObjSingle(conn, YHCmsTemplate.class, station.getTemplateId());
    //一级栏目，并设置栏目url
    //获取出来的list 是有排序的
    String filters[] = {" STATION_ID =" + stationId + " order by COLUMN_INDEX desc "};
    List<YHCmsColumn> columnList = orm.loadListSingle(conn, YHCmsColumn.class, filters);
    for(YHCmsColumn columnTemp : columnList){
      if(columnTemp != null){
        YHCmsTemplate columnTemplate = (YHCmsTemplate) orm.loadObjSingle(conn, YHCmsTemplate.class, station.getTemplateId());
        String columnFileName = columnTemplate.getTemplateFileName();
        String pathColumn = getColumnPath(conn, columnTemp.getSeqId());
        columnFileName = "/yh/" + station.getStationPath() + "/" + pathColumn + "/" + columnFileName + "." + station.getExtendName();
        columnTemp.setUrl(columnFileName);
        
        //当前栏目下的文章，并设置文章url
        //获取出来的list 是有排序的
        String filtersContent[] = {" COLUMN_ID =" + columnTemp.getSeqId() + " and CONTENT_STATUS = 5 order by CONTENT_TOP desc, CONTENT_INDEX desc "};
        List<YHCmsContent> contentList = orm.loadListSingle(conn, YHCmsContent.class, filtersContent);
        for(YHCmsContent content : contentList){
          String contentFileName = content.getContentFileName();
          if(YHUtility.isNullorEmpty(content.getContentFileName())){
            contentFileName = content.getSeqId()+"";
          }
          String pathContent = getColumnPath(conn, content.getColumnId());
          contentFileName = "/yh/" + station.getStationPath() + "/" + pathContent + "/" + contentFileName + "." + station.getExtendName();
          content.setUrl(contentFileName);
        }
        columnTemp.setContentList(contentList);
      }
    }
    
    station.setColumnList(columnList);
    //站点及站点url
    station.setUrl("/yh/" + station.getStationPath() + "/" + template.getTemplateFileName() + "." + station.getExtendName());
    return station;
  }
}
