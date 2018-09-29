package yh.cms.station.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import yh.cms.column.data.YHCmsColumn;

public class YHCmsStation {

  private int seqId;
  private String stationName;
  private String stationDomainName;
  private int templateId;
  private String stationFileName;
  private String extendName;
  private String articleExtendName;
  private int createId;
  private Date createTime;
  private String stationPath;
  private String url;
  private String visitUser;
  private String editUser;
  private String newUser;
  private String delUser;
  private String relUser;
  private List<YHCmsColumn>  columnList;
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
  public Iterator<YHCmsColumn> itColumn(){
    Iterator<YHCmsColumn> iterator = new Iterator<YHCmsColumn>() {
      
      @Override
      public void remove() {
        // TODO Auto-generated method stub
        
      }
      
      @Override
      public YHCmsColumn next() {
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
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public String getStationPath() {
    return stationPath;
  }
  public void setStationPath(String stationPath) {
    this.stationPath = stationPath;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getStationName() {
    return stationName;
  }
  public void setStationName(String stationName) {
    this.stationName = stationName;
  }
  public String getStationDomainName() {
    return stationDomainName;
  }
  public void setStationDomainName(String stationDomainName) {
    this.stationDomainName = stationDomainName;
  }
  public int getTemplateId() {
    return templateId;
  }
  public void setTemplateId(int templateId) {
    this.templateId = templateId;
  }
  public String getStationFileName() {
    return stationFileName;
  }
  public void setStationFileName(String stationFileName) {
    this.stationFileName = stationFileName;
  }
  public String getExtendName() {
    return extendName;
  }
  public void setExtendName(String extendName) {
    this.extendName = extendName;
  }
  public String getArticleExtendName() {
    return articleExtendName;
  }
  public void setArticleExtendName(String articleExtendName) {
    this.articleExtendName = articleExtendName;
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
  public List<YHCmsColumn> getColumnList() {
    return columnList;
  }
  public void setColumnList(List<YHCmsColumn> columnList) {
    this.columnList = columnList;
  }
  public YHCmsColumn getColumn(String columnName){
    for(YHCmsColumn columnTemp : columnList){
      if(columnName.equals(columnTemp.getColumnName())){
        return columnTemp;
      }
    }
    return null;
  }
  public List<YHCmsColumn> getChildrenColumns(String columnName){
    List<YHCmsColumn>  columnChildrenList = new ArrayList<YHCmsColumn>();
    int columnId = 0;
    for(YHCmsColumn columnTemp : columnList){
      if(columnName.equals(columnTemp.getColumnName())){
        columnId = columnTemp.getSeqId();
        break;
      }
    }
    for(YHCmsColumn columnTemp : columnList){
      if(columnId == columnTemp.getParentId()){
        columnChildrenList.add(columnTemp);
      }
    }
    return columnChildrenList;
  }
}
