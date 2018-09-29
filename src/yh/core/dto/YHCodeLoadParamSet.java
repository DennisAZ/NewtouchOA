package yh.core.dto;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 下拉框数据加载参数集合
 * @author jpt
 *
 */
public class YHCodeLoadParamSet {
  //参数哈希表
  Map<String, YHCodeLoadParam> paramMap = new LinkedHashMap<String, YHCodeLoadParam>();
  /**
   * 新添加参数
   * @param param
   */
  public void addParam(YHCodeLoadParam param) {
    paramMap.put(param.getCntrlId(), param);
  }
  /**
   * 取得迭代器
   * @return
   */
  public Iterator<YHCodeLoadParam> itParams() {
    return paramMap.values().iterator();
  }
  /**
   * 取得参数
   * @param cntrlId
   * @return
   */
  public YHCodeLoadParam getParam(String cntrlId) {
    return paramMap.get(cntrlId);
  }
  
  /**
   * 取得参数
   * @param cntrlId
   * @return
   */
  public YHCodeLoadParam getParam() {
    Iterator<YHCodeLoadParam> iParam = itParams();
    if (iParam.hasNext()) {
      return iParam.next();
    }
    return null;
  }
  
  public String toString() {
    StringBuffer rtBuf = new StringBuffer();
    rtBuf.append("{");
    Iterator<YHCodeLoadParam> itParams = itParams();
    while (itParams.hasNext()) {
      YHCodeLoadParam param = itParams.next();
      rtBuf.append(param.getCntrlId() + ":");
      rtBuf.append(param.toString());
      rtBuf.append(",");
    }
    rtBuf.append("}");
    return rtBuf.toString();
  }
}
