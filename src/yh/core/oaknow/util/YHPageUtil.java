package yh.core.oaknow.util;


public class YHPageUtil {
  private int elementsCount;// 总的元素数
  private int pageSize;     // 页面大小
  private int pagesCount;   // 总的页面数
  private int currentPage =1;  // 当前是第几页
  
  public int getElementsCount() {
    return elementsCount;
  }
  public void setElementsCount(int elementsCount) {
    this.elementsCount = elementsCount;
  }
  public int getPageSize() {
    return pageSize;
  }
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public void setPagesCount(int pagesCount) {
    this.pagesCount = pagesCount;
  }
  public int getCurrentPage() {
    return currentPage;
  }
  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }
  public int getPagesCount() {
    pagesCount = (getElementsCount() % getPageSize() == 0) ? (getElementsCount() / getPageSize())
        : (getElementsCount() / getPageSize() + 1);
    return pagesCount;
  }
}
