package yh.core.logic;

import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import yh.core.dto.YHCodeLoadParam;
import yh.core.dto.YHCodeLoadParamSet;
import yh.core.load.YHCodeLoader;
import yh.core.util.YHUtility;

public class YHCodeSelectLogic {
  /**
   * 加载选择框数据
   * @param dbConn
   * @param paramSet
   * @return
   * @throws Exception
   */
  public String loadSelectData(Connection dbConn,
      YHCodeLoadParamSet paramSet) throws Exception {
    
    Map<String, List> dataMap = loadSelectDataMap(dbConn, paramSet);
    //返回Json字符串
    StringBuffer rtBuf = new StringBuffer("{");
    Iterator<YHCodeLoadParam> iParam = paramSet.itParams();
    int iCnt = 0;
    while (iParam.hasNext()) {
      YHCodeLoadParam param = iParam.next();
      String cntrlId = param.getCntrlId();
      List<String[]> dataList = dataMap.get(cntrlId);
      int codeCnt = dataList.size();
      if (iCnt > 0) {
        rtBuf.append(",");
      }
      rtBuf.append(cntrlId);
      rtBuf.append(":");
      rtBuf.append("{value:");
      rtBuf.append("\"");
      rtBuf.append(YHUtility.null2Empty(param.getValue()));
      rtBuf.append("\",data:[");
      String isMustFill = param.getIsMustFill();
      boolean insertEmpty = false;
      if (!YHUtility.isNullorEmpty(isMustFill)
           && isMustFill.equals("0")) {
        rtBuf.append("{code:\"\",desc:\"\"}");
        insertEmpty = true;
      }
      for (int i = 0; i < codeCnt; i++) {
        String[] codeRecord = dataList.get(i);
        if (insertEmpty || i > 0) {
          rtBuf.append(",");
        }
        rtBuf.append("{code:\"");
        rtBuf.append(YHUtility.encodeSpecial(codeRecord[0]));
        rtBuf.append("\",desc:\"");
        rtBuf.append(YHUtility.encodeSpecial(codeRecord[1]));
        rtBuf.append("\"}");
      }
      rtBuf.append("]}");
      iCnt++;
    }
    rtBuf.append("}");
    return rtBuf.toString();
  }
  /**
   * 加载选择框数据
   * @param dbConn
   * @param paramSet
   * @return
   * @throws Exception
   */
  public Map loadSelectDataMap(Connection dbConn,
      YHCodeLoadParamSet paramSet) throws Exception {
    Map<String, List> dataMap = new LinkedHashMap<String, List>();
    //首先处理不联动重新加载的数据
    Iterator<YHCodeLoadParam> iParams = paramSet.itParams();
    while (iParams.hasNext()) {
      YHCodeLoadParam param = iParams.next();
      if (!YHUtility.isNullorEmpty(param.getReloadBy())) {
        continue;
      }
      List<String[]> dataList = YHCodeLoader.loadData(dbConn, param);
      dataMap.put(param.getCntrlId(), dataList);
      if (YHUtility.isNullorEmpty(param.getValue())) {
        if (dataList.size() > 0) {
          param.setValue(dataList.get(0)[0]);
        }
      }
    }
    //然后处理联动重新加载的数据
    iParams = paramSet.itParams();
    while (iParams.hasNext()) {
      YHCodeLoadParam param = iParams.next();
      if (YHUtility.isNullorEmpty(param.getReloadBy())) {
        continue;
      }
      String reloadBy = param.getReloadBy();
      YHCodeLoadParam reloadParam = paramSet.getParam(reloadBy);
      if (reloadParam != null) {
        param.setFilterValue(reloadParam.getValue());
      }      
      List<String[]> dataList = YHCodeLoader.loadData(dbConn, param);
      dataMap.put(param.getCntrlId(), dataList);
      if (YHUtility.isNullorEmpty(param.getValue())) {
        if (dataList.size() > 0) {
          param.setValue(dataList.get(0)[0]);
        }
      }
    }
    return dataMap;
  }
}
