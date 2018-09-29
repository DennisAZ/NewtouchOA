package yh.subsys.inforesouce.data;

/**
 * 元数据过滤器配置
 * @author jpt
 *
 */
public class YHMetaFilterConf {
  //支持的编码
  private String typeNos = null;
  //过滤器
  private String filter = null;

  public String getTypeNos() {
    return typeNos;
  }
  public void setTypeNos(String typeNos) {
    this.typeNos = typeNos;
  }
  public String getFilter() {
    return filter;
  }
  public void setFilter(String filter) {
    this.filter = filter;
  }
}
