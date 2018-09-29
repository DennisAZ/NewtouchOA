package yh.cms.column.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import yh.cms.content.data.YHCmsContent;
import yh.cms.station.data.YHCmsStation;
import yh.cms.template.data.YHCmsTemplate;

public class YHCmsColumn {

  private int seqId;
  private String columnName;
  private String columnTitle;
  private int stationId;
  private int parentId;
  private String columnPath;
  private int archive;
  private int templateIndexId;
  private int templateArticleId;
  private int createId;
  private Date createTime;
  private int columnIndex;
  private int paging;
  private int maxIndexPage;
  private int pagingNumber;
  private String url;
  private int showMain;
  private String visitUser;
  private String editUser;
  private String newUser;
  private String delUser;
  private String relUser;
  private String editUserContent;
  private String approvalUserContent;
  private String releaseUserContent;
  private String recevieUserContent;
  private String orderContent;
  private List<YHCmsContent> contentList;
  public String getEditUserContent() {
    return editUserContent;
  }
  public void setEditUserContent(String editUserContent) {
    this.editUserContent = editUserContent;
  }
  public String getApprovalUserContent() {
    return approvalUserContent;
  }
  public void setApprovalUserContent(String approvalUserContent) {
    this.approvalUserContent = approvalUserContent;
  }
  public String getReleaseUserContent() {
    return releaseUserContent;
  }
  public void setReleaseUserContent(String releaseUserContent) {
    this.releaseUserContent = releaseUserContent;
  }
  public String getRecevieUserContent() {
    return recevieUserContent;
  }
  public void setRecevieUserContent(String recevieUserContent) {
    this.recevieUserContent = recevieUserContent;
  }
  public String getOrderContent() {
    return orderContent;
  }
  public void setOrderContent(String orderContent) {
    this.orderContent = orderContent;
  }
  public String getVisitUser() {
    return visitUser;
  }
  public void setVisitUser(String visitUser) {
    this.visitUser = visitUser;
  }
  public String getEditUser() {
    return editUser;
  }
  public void setEditUser(String editUser) {
    this.editUser = editUser;
  }
  public String getNewUser() {
    return newUser;
  }
  public void setNewUser(String newUser) {
    this.newUser = newUser;
  }
  public String getDelUser() {
    return delUser;
  }
  public void setDelUser(String delUser) {
    this.delUser = delUser;
  }
  public String getRelUser() {
    return relUser;
  }
  public void setRelUser(String relUser) {
    this.relUser = relUser;
  }
  public Iterator<YHCmsContent> itContent(){
    Iterator<YHCmsContent> iterator = new Iterator<YHCmsContent>() {
      
      @Override
      public void remove() {
        // TODO Auto-generated method stub
        
      }
      
      @Override
      public YHCmsContent next() {
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public boolean hasNext() {
        // TODO Auto-generated method stub
        return false;
      }
    };
    return iterator;
  }
  public int getShowMain() {
    return showMain;
  }
  public void setShowMain(int showMain) {
    this.showMain = showMain;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getColumnName() {
    return columnName;
  }
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
  public String getColumnTitle() {
    return columnTitle;
  }
  public void setColumnTitle(String columnTitle) {
    this.columnTitle = columnTitle;
  }
  public int getStationId() {
    return stationId;
  }
  public void setStationId(int stationId) {
    this.stationId = stationId;
  }
  public int getParentId() {
    return parentId;
  }
  public void setParentId(int parentId) {
    this.parentId = parentId;
  }
  public String getColumnPath() {
    return columnPath;
  }
  public void setColumnPath(String columnPath) {
    this.columnPath = columnPath;
  }
  public int getTemplateIndexId() {
    return templateIndexId;
  }
  public void setTemplateIndexId(int templateIndexId) {
    this.templateIndexId = templateIndexId;
  }
  public int getTemplateArticleId() {
    return templateArticleId;
  }
  public void setTemplateArticleId(int templateArticleId) {
    this.templateArticleId = templateArticleId;
  }
  public int getCreateId() {
    return createId;
  }
  public void setCreateId(int createId) {
    this.createId = createId;
  }
  public Date getCreateTime() {
    return createTime;
  }
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  public int getColumnIndex() {
    return columnIndex;
  }
  public void setColumnIndex(int columnIndex) {
    this.columnIndex = columnIndex;
  }
  public int getArchive() {
    return archive;
  }
  public void setArchive(int archive) {
    this.archive = archive;
  }
  public int getPaging() {
    return paging;
  }
  public void setPaging(int paging) {
    this.paging = paging;
  }
  public int getMaxIndexPage() {
    return maxIndexPage;
  }
  public void setMaxIndexPage(int maxIndexPage) {
    this.maxIndexPage = maxIndexPage;
  }
  public int getPagingNumber() {
    return pagingNumber;
  }
  public void setPagingNumber(int pagingNumber) {
    this.pagingNumber = pagingNumber;
  }
  public List<YHCmsContent> getContentList() {
    return contentList;
  }
  public void setContentList(List<YHCmsContent> contentList) {
    this.contentList = contentList;
  }
  public List<YHCmsContent> getContents(int count){
    if(count == 999){
      return contentList;
    }
    List<YHCmsContent> contents = new ArrayList<YHCmsContent>();
    for(YHCmsContent content : contentList){
      if(count == 0){
        break;
      }
      contents.add(content);
      count--;
    }
    return contents;
  }
  public String getPage(YHCmsColumn column, int contentSize, YHCmsStation station, YHCmsTemplate template, int i){
    if(column.getPaging() == 1){
      int maxIndexPage = column.getMaxIndexPage();
      int pagingNumber = column.getPagingNumber(); 
      
      int total = contentSize;
      
      int page = total/pagingNumber + (total%pagingNumber > 0 ? 1 : 0);
      if(page > maxIndexPage){
        total = maxIndexPage * pagingNumber;
      }
      page = page > maxIndexPage ? maxIndexPage : page;
      
      String fileName = template.getTemplateFileName();
      String prv = "";
      String next = "";
      if(i - 1 <= 0){
        prv = fileName+"."+station.getExtendName();
      }
      else{
        prv = fileName+(i - 1)+"."+station.getExtendName();
      }
      if(i + 1 >= page){
        next = fileName+(page-1 == 0 ? "" : page-1)+"."+station.getExtendName();
      }
      else{
        next = fileName+(i+1)+"."+station.getExtendName();
      }
      
      String tempStr =  "<table width=\"100%\" border=\"0\" cellspacing=\"5\" cellpadding=\"0\">\n " 
                      + "<tr>\n " 
                      + "<td align=\"center\" class=\"dahei\"><table> " 
                      + "共"+ total +"条新闻，分"+page+"页，当前第<font color=red>" + (i+1) + "</font>页&nbsp;&nbsp;"
                      + "<a href=\""+(fileName+"."+station.getExtendName())+"\">最前页</a> " 
                      + "<a href=\""+prv+"\">上一页</a> " 
                      + "<a href=\""+next+"\">下一页</a> " 
                      + "<a href=\""+(fileName+(page-1)+"."+station.getExtendName())+"\">最后页</a> " 
                      + "</table> "
                      + "</td>"
                      + "</tr>"
                      + "</table>";
//      String pageStr = "";
//      for(int j = 0; j < page; j++){
//        fileName = template.getTemplateFileName();
//        if(j != 0){
//          fileName = fileName + j;
//        }
//        if(j == i){
//          pageStr = pageStr + "<span class=\"current\">"+(j+1)+"</span>";
//        }
//        else{
//          pageStr = pageStr + "<a href="+fileName+"."+station.getExtendName()+"><span>"+(j+1)+"</span></a>";
//        }
//      }
//        
//      String tempStr =  "<a href=\""+prv+"\"><span class=\"prev\">&nbsp;</span></a>" 
//                      +	pageStr
//                      + "<a href=\""+next+"\"><span class=\"next\">&nbsp;</span></a>";
      return tempStr;
    }
    return "";
  }
}
