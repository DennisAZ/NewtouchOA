package yh.subsys.portal.guoyan.module.data;

public class YHPortalDataRule {
  /**
   * 表名称
   */
  private String tableName;
  private String seqFieldName;
  /**
   * 非分页显示条数限制
   */
  private int limit;
  /**
   * 分页显示条数限制
   */
  private int pagingLimit;
  /**
   * 数据库字段名
   */
  private String dbFieldNames;
  /**
   * 对应显示名称
   */
  private String names;
  /**
   * 非分页查询条件
   */
  private String filter;
  /**
   * 分页查询条件
   */
  private String filterPaging;
/**
 * 排序字段
 */
  private String orderBy;
  
  private String serach;
  /**
   * 排序字段desc/asc
   */
  private String orderBySort;
  public String getSeqFieldName() {
    return seqFieldName;
  }

  public void setSeqFieldName(String seqFieldName) {
    this.seqFieldName = seqFieldName;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public String getFilterPaging() {
    return filterPaging;
  }

  public void setFilterPaging(String filterPaging) {
    this.filterPaging = filterPaging;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public int getPagingLimit() {
    return pagingLimit;
  }

  public void setPagingLimit(int pagingLimit) {
    this.pagingLimit = pagingLimit;
  }

  public String getDbFieldNames() {
    return dbFieldNames;
  }

  public void setDbFieldNames(String dbFieldNames) {
    this.dbFieldNames = dbFieldNames;
  }

  public String getNames() {
    return names;
  }

  public void setNames(String names) {
    this.names = names;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public String getOrderBySort() {
    return orderBySort;
  }

  public void setOrderBySort(String orderBySort) {
    this.orderBySort = orderBySort;
  }

  public String getSerach() {
    return serach;
  }

  public void setSerach(String serach) {
    this.serach = serach;
  }

  @Override
  public String toString() {
    return "YHPoralDataRule [dbFieldNames=" + dbFieldNames + ", filter="
        + filter + ", filterPaging=" + filterPaging + ", limit=" + limit
        + ", names=" + names + ", pagingLimit=" + pagingLimit + ", tableName="
        + tableName + "]";
  }
  
}
