package yh.rad.velocity.act;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHDsTable;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.db.YHStringFormat;
import yh.core.util.form.YHFOM;
import yh.rad.velocity.YHCode2DbUtil;
import yh.rad.velocity.YHCodeUtil;
import yh.rad.velocity.YHvelocityUtil;
import yh.rad.velocity.createtable.YHCreateTableUtil;
import yh.rad.velocity.createtable.YHDBDialectUtil;
import yh.rad.velocity.metadata.YHField;
import yh.rad.velocity.metadata.YHGridField;

public class YHCodeUtilAct {
  public String loginCheck(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    String url = "D:\\project\\yh\\src\\yh\\core\\act";
    String templateName = "ActTemplate.vm";
    YHvelocityUtil.velocity(request.getParameterMap(), url, templateName, "");
    return "/core/inc/rtjson.jsp";
  }
  
  public String create(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tableNos = request.getParameter("tableNos");
      String outp = request.getParameter("outpath");
      String templateUrl = request.getParameter("templateUrl").trim();
      String[] dialects = request.getParameterValues("dialect");
      String templateName = "";
      if(templateUrl.endsWith("\\")){
        String str = templateUrl.substring(0, templateUrl.length()-1);
        if(str.endsWith(".vm")){
          int index = str.lastIndexOf("\\");
          templateUrl = str.substring(0,index + 1);
        }
      }
      String[] tableNoArr = tableNos.split(",");
        for (String dia : dialects) {
          String outpath = outp + dia + "\\";
          templateName = "createtable" + dia + ".vm";
          YHvelocityUtil.velocity(
              YHCreateTableUtil.createTableById(dbConn,tableNoArr,dia)
              , outpath, templateName, templateUrl);
        }
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "代码生成成功！");
    } catch(Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "代码生成失败！");
      e.printStackTrace();
    }
    return "/core/inc/rtjson.jsp";
  }

  public String showField(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tabNo = request.getParameter("tableNo").trim();
      //System.out.println(tabNo);
      String data = YHCode2DbUtil.getFields(dbConn, tabNo);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "chenggong！");
      request.setAttribute(YHActionKeys.RET_DATA,data);
      //System.out.println(data);
    } catch(Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "代码生成失败！");
      e.printStackTrace();
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String code2java(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tabNo = request.getParameter("tableNo").trim();
      String filedsVal = request.getParameter("filedsVal").trim();
      String[] selectFilter = request.getParameterValues("selectFilter");
      String[] radioFilter = request.getParameterValues("radioFilter");
      ArrayList< YHGridField> gridFields = new ArrayList<YHGridField>();
      ArrayList< YHField> fields = new ArrayList<YHField>();
      String[] fieldsVals = filedsVal.split("/");
      for (String fieldValue : fieldsVals){
        String[] fie = fieldValue.split(",");
        String header = fie[1].trim(); 
        String realName = fie[0].trim(); 
        String name = YHStringFormat.unformat(fie[0].trim()); 
        String hidden = ""; 
        if("0".equals(fie[3].trim())){
          hidden = "true";
        } else {
          hidden = "false";
        }
        String with = fie[2].trim();
        
        String fkTableName = "";
        String fkRelaFieldName = "";
        String fkNameFieldName = "";
        String fkTableNo = "";
        
        String fkTableName2 = "";
        String fkFilterName = "";
        String codeClass = "";
        String fkNameFieldName2 = "";
        String sql = " SELECT d1.TABLE_NAME FK_TABLE_NAME, d2.FIELD_NAME FK_RELA_FIELD_NAME, d3.FIELD_NAME FK_NAME_FIELD_NAME, d1.TABLE_NO "
                   + "      , d4.TABLE_NAME FK_TABLE_NAME2, d5.FIELD_NAME FK_FILTER_NAME, d.CODE_CLASS, d6.FIELD_NAME FK_NAME_FIELD_NAME2 "
                   + " FROM oa_field_dicts d "
                   + " left join oa_table_dicts d1 on d.FK_TABLE_NO = d1.TABLE_NO "
                   + " left join oa_field_dicts d2 on d.FK_RELA_FIELD_NO = d2.FIELD_NO "
                   + " left join oa_field_dicts d3 on d.FK_NAME_FIELD_NO = d3.FIELD_NO "
                   + " left join oa_table_dicts d4 on d.FK_TABLE_NO2 = d4.TABLE_NO "
                   + " left join oa_field_dicts d5 on d.FK_FILTER = d5.FIELD_NO "
                   + " left join oa_field_dicts d6 on d.FK_NAME_FIELD_NO2 = d6.FIELD_NO "
                   + " where d.TABLE_NO = '"+ tabNo +"' and d.FIELD_NAME = '"+ realName +"'";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
          ps = dbConn.prepareStatement(sql);
          rs = ps.executeQuery();
          while (rs.next()) {
            fkTableName = rs.getString("FK_TABLE_NAME");
            fkRelaFieldName = rs.getString("FK_RELA_FIELD_NAME");
            fkNameFieldName = rs.getString("FK_NAME_FIELD_NAME");
            fkTableNo = rs.getString("TABLE_NO");
            
            fkTableName2 = rs.getString("FK_TABLE_NAME2");
            fkFilterName = rs.getString("FK_FILTER_NAME");
            codeClass = rs.getString("code_Class");
            fkNameFieldName2 = rs.getString("FK_NAME_FIELD_NAME2");
          }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          YHDBUtility.close(ps, rs, null);
        }
        
        YHGridField gf = new YHGridField(header, name, realName, hidden, with, fkTableName, fkRelaFieldName, fkNameFieldName);
        if(!YHUtility.isNullorEmpty(fkTableName2)){
          gf = new YHGridField(header, name, realName, hidden, with, fkTableName2, "SEQ_ID", fkNameFieldName2);
        }
        gridFields.add(gf);
        
        boolean isMust = false;
        if("1".equals(fie[4].trim())){
          isMust = true;
        }
        YHField f = new YHField(name,header,isMust,fie[5].trim());
        if(!YHUtility.isNullorEmpty(fkTableName)){
          f.setHidden(true);
        }
        if(!YHUtility.isNullorEmpty(fkTableName2)){
          f.setFkTableName2(fkTableName2);
          f.setFkFilterName(YHStringFormat.unformat(fkFilterName));
          f.setCodeClass(codeClass);
          f.setFkNameFieldName2(YHStringFormat.unformat(fkNameFieldName2));
        }
        fields.add(f);
        if(!YHUtility.isNullorEmpty(fkTableName)){
          YHField f0 = new YHField(name + "Desc",header,isMust,fie[5].trim()
              , fkTableName, YHStringFormat.unformat(fkRelaFieldName), YHStringFormat.unformat(fkNameFieldName), fkTableNo
              , fkTableName2, YHStringFormat.unformat(fkFilterName), codeClass, YHStringFormat.unformat(fkNameFieldName2));
          fields.add(f0);
        }
      }
      Map m = new HashMap();
      Map req = request.getParameterMap();
      Set<String> keys = req.keySet();
      for (String key : keys){
        String value = ((String[]) req.get(key))[0];
        m.put(key, value);
      }
      ArrayList<String> sf = new ArrayList<String>();
      if(selectFilter != null){
        for (String string : selectFilter){
          sf.add(string);
        }
      }
      ArrayList<String> rf = new ArrayList<String>();
      if(radioFilter != null){
        for (String string : radioFilter){
          rf.add(string);
        }
      }
      m.put("rf", rf);
      m.put("sf", sf);
      m.put("listFields", gridFields);
      m.put("inputFields", fields);
      YHCodeUtil.autoCode(dbConn, tabNo,m );
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "代码生成成功！");
    } catch(Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "代码生成失败,请检查数据字典是否配置正确！");
      e.printStackTrace();
    }
    return "/core/inc/rtjson.jsp";
  }
  public String test(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String fieldsVal = request.getParameter("filedsVal").trim();
    //System.out.println(fieldsVal);
    return "/core/inc/rtjson.jsp";
  }
}
