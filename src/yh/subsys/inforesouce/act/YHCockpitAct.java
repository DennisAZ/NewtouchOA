package yh.subsys.inforesouce.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.form.YHFOM;
import yh.subsys.inforesouce.util.YHAjaxUtil;
import yh.subsys.inforesouce.util.YHOutURLUtil;

/**
 * 驾驶仓相关代码
 * @author lh
 *
 */
public class YHCockpitAct{
  
  /**
   * 取得趋势图数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getLineData(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String keyId = request.getParameter("keyId");
    String type = request.getParameter("type");
    try{
      String basePath = YHSysProps.getString("signFileServiceUrl");
      String url = basePath + "/TagIt/GetHotPersonHistoryByMonth?keyId="+ keyId;
      if (("address").equals(type)) {
        url = basePath + "/TagIt/GetHotAddressHistoryByMonth?KeyID="+ keyId;
      } else if (("org").equals(type)) {
        url = basePath + "/TagIt/GetHotOrganizationHistoryByMonth?KeyID="+ keyId;
      } else if (("subject").equals(type)) {
        url = basePath + "/TagIt/GetHotKeywordHistoryByMonth?KeyID="+ keyId;
      }
      String files = YHOutURLUtil.getContent(url);
     String pop = "\"rotateNames\":\"0\",\"shownames\":\"0\",\"showLabels\":\"1\",\"showColumnShadow\":\"1\",\"animation\":\"1\",\"showAlternateHGridColor\":\"1\" ,\"AlternateHGridColor\":\"ff5904\",\"divLineColor\":\"ff5904\",  \"divLineAlpha\":\"20\" ,\"alternateHGridAlpha\":\"5\" ,\"canvasBorderColor\":\"666666\", \"baseFontColor\":\"666666\" ,\"lineColor\":\"FF5904\",  \"lineAlpha\":\"85\" , \"showValues\":\"1\" , \"rotateValues\":\"1\" ,\"valuePosition\":\"auto\" , \"paleteThemeColor\":\"6699FF\""; 
     files = "{\"chart\": {"+pop+"}, \"data\" :" + files + "}";
      YHAjaxUtil.ajax(files, response);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }  
    return null;
  }
  /**
   * 取得柱状图数据

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getColumnData(HttpServletRequest request, HttpServletResponse response) throws Exception{
    try{
      String basePath = YHSysProps.getString("signFileServiceUrl");
      String url = basePath + "/TagIt/GetHotPersonByMonth?nMaxMonths=12";
      String type = request.getParameter("type");
      if (("address").equals(type)) {
        url = basePath + "/TagIt/GetHotAddressByMonth?nMaxMonths=12";
      } else if (("org").equals(type)) {
        url = basePath + "/TagIt/GetHotOrganizationByMonth?nMaxMonths=12";
      } else if (("subject").equals(type)) {
        url = basePath + "/TagIt/GetHotKeywordByMonth?nMaxMonths=12";
      }
      String files = YHOutURLUtil.getContent(url);
      String prop = "\"unescapeLinks\":\"0\",\"bgColor\":\"FFFFFF,FFFFFF\" ,\"showBorder\":\"0\",\"useroundedges\":\"1\",    \"showborder\":\"0\",    \"exportenabled\":\"1\",    \"exportshowmenuitem\":\"0\"";
       files = "{\"chart\": {"+prop+"}, \"data\" :" + files + "}";
        YHAjaxUtil.ajax(files, response);
      } catch (Exception e){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
        throw e;
      }  
      return null;
    }
  /**
   * 文件管理中心

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getChartData(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String data = request.getParameter("data");
    String type = request.getParameter("type");
    try{
      String basePath = YHSysProps.getString("signFileServiceUrl");
      String url = basePath + "/TagIt/GetHotPersonHistoryByMonth?keyId="+ data;
      String propType = "line"; 
      if (("1").equals(type)) {
        propType = "column";
        //url = basePath + "/TagIt/GetHotAddressHistoryByMonth?KeyID="+ keyId;
      } else if (("2").equals(type)) {
        //url = basePath + "/TagIt/GetHotOrganizationHistoryByMonth?KeyID="+ keyId;
      } else if (("3").equals(type)) {
        //url = basePath + "/TagIt/GetHotKeywordHistoryByMonth?KeyID="+ keyId;
      } else if (("4").equals(type)) {
       // url = basePath + "/TagIt/GetHotKeywordHistoryByMonth?KeyID="+ keyId;
      }else if (("5").equals(type)) {
       // url = basePath + "/TagIt/GetHotKeywordHistoryByMonth?KeyID="+ keyId;
      }
      String files = YHOutURLUtil.getContent(url);
      String pop = this.getProp(propType) ;
      files = "{\"chart\": {"+pop+"}, \"data\" :" + files + "}";
      YHAjaxUtil.ajax(files, response);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }  
    return null;
  }
  /**
   * 取得图的属性
   * @param type
   * @return
   */
  public String getProp(String type) {
    String prop = "";
    if ("line".equals(type)) {
      prop = "\"rotateNames\":\"0\",\"shownames\":\"0\",\"showLabels\":\"1\",\"showColumnShadow\":\"1\",\"animation\":\"1\",\"showAlternateHGridColor\":\"1\" ,\"AlternateHGridColor\":\"ff5904\",\"divLineColor\":\"ff5904\",  \"divLineAlpha\":\"20\" ,\"alternateHGridAlpha\":\"5\" ,\"canvasBorderColor\":\"666666\", \"baseFontColor\":\"666666\" ,\"lineColor\":\"FF5904\",  \"lineAlpha\":\"85\" , \"showValues\":\"1\" , \"rotateValues\":\"1\" ,\"valuePosition\":\"auto\" , \"paleteThemeColor\":\"6699FF\"";
    } else {
      prop = "\"unescapeLinks\":\"0\",\"bgColor\":\"FFFFFF,FFFFFF\" ,\"showBorder\":\"0\",\"useroundedges\":\"1\",    \"showborder\":\"0\",    \"exportenabled\":\"1\",    \"exportshowmenuitem\":\"0\"";
    }
    return prop;
  }
}
