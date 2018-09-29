package yh.core.data;

public class YHPageQueryParamNew {
  //每页显示的条数
  private int pageSize = 0;
  //请求页面索引，从0开始计数
  private int pageIndex = 0;
  //页面名称串，格式以逗号分隔的字符串
  
  private String sortColumn;
  
  private int page;
  private int rows;
  private String sort;
  private String sidx;
  
  public int getPage() {
    return page;
  }
  public void setPage(int page) {
    this.pageIndex = page - 1;
    this.page = page;
  }
  public int getRows() {
    return rows;
  }
  public void setRows(int rows) {
    this.pageSize = rows;
    this.rows = rows;
  }
  public String getSort() {
    return sort;
  }
  public void setSort(String sort) {
    this.direct = sort;
    this.sort = sort;
  }
  public String getSidx() {
    return sidx;
  }
  public void setSidx(String sidx) {
    this.sortColumn = sidx;
    this.sidx = sidx;
  }
  private String direct = "asc";
  
  private String nameStr = null;
  
  public String getSortColumn() {
    return sortColumn;
  }
  public void setSortColumn(String sortColumn) {
    this.sortColumn = sortColumn;
  }
  public String getDirect() {
    return direct;
  }
  public void setDirect(String direct) {
    this.direct = direct;
  }
  public int getPageSize() {
    return pageSize;
  }
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
  public int getPageIndex() {
    return pageIndex;
  }
  public void setPageIndex(int pageIndex) {
    this.pageIndex = pageIndex;
  }
  public String getNameStr() {
    return nameStr;
  }
  public void setNameStr(String nameStr) {
    this.nameStr = nameStr;
    if (this.nameStr != null && this.nameStr.endsWith(",")) {
      this.nameStr = this.nameStr.substring(0, this.nameStr.length() - 1);
    }
  }
}
