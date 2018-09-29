package yh.core.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页数据存储
 * @author jpt
 *
 */
public class YHPageDataListNew {
  //全部记录条数
  private int totalRecord = 0;
  //当前显示页
  private int page = 0;
  //当前显示行
  private int total = 0;
  //数据记录列表
  private List<YHDbRecord> recordList = new ArrayList<YHDbRecord>();
  
  /**
   * 转换成Json字符串
   * @return
   */
  public String toJson() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\"records\":");
    sb.append(totalRecord);
    sb.append(",\"page\":");
    sb.append(page);
    sb.append(",\"total\":");
    sb.append(total);
    
    sb.append(",\"rows\":[");
    int recordCnt = this.getRecordCnt();
    for (int i = 0; i < recordCnt; i++) {
      YHDbRecord record = this.getRecord(i);
      sb.append("{\"id\":");
      sb.append(i);
      sb.append(",\"cell\":[");
      for (int j = 0; j < record.getFieldCnt(); j++) {
        sb.append("\"");
        sb.append(record.getValueByIndex(j) == null ? "" : record.getValueByIndex(j));
        sb.append("\"");
        if (j < record.getFieldCnt() - 1) {
          sb.append(",");
        }
      }
      sb.append("]}");
      if (i < recordCnt - 1) {
        sb.append(",");
      }
    }
    sb.append("]");
    sb.append("}");
    return sb.toString();
  }
  
  /**
   * 取得记录的条数
   * @return
   */
  public int getRecordCnt() {
    return this.recordList.size();
  }
  /**
   * 取得记录
   * @param index
   * @return
   */
  public YHDbRecord getRecord(int index) {
    return this.recordList.get(index);
  }
  /**
   * 添加记录
   * @param record
   */
  public void addRecord(YHDbRecord record) {
    this.recordList.add(record);
  }
  
  public int getTotalRecord() {
    return totalRecord;
  }
  public void setTotalRecord(int totalRecord) {
    this.totalRecord = totalRecord;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  
}
