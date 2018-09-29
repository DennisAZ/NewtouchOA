package yh.subsys.oa.officeProduct.report.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHDsType;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.generics.YHSQLParamHepler;
import yh.core.util.form.YHFOM;

public class YHReportAnalysisLogic {
  private static Logger log = Logger.getLogger(YHReportAnalysisLogic.class);

  public String getAnalysis(Connection dbConn, Map map, Map request, YHPerson person) throws Exception{
    
    String careDate1 = (String)map.get("careDate1");
    String careDate2 = (String)map.get("careDate2");
    String officeDepository = (String)map.get("officeDepository");
    String officeProtype = (String)map.get("officeProtype");
    String product = (String)map.get("product");
    String module = (String)map.get("module");
    String mapType = (String)map.get("mapType");
    String conditionStr = "";
    String conditionStr1 = "";

    if (!YHUtility.isNullorEmpty(careDate1)) {
      conditionStr1 += " and " + YHDBUtility.getDateFilter("t1.TRANS_DATE", careDate1, ">=");
    }
    if (!YHUtility.isNullorEmpty(careDate2)) {
      conditionStr1 += " and " + YHDBUtility.getDateFilter("t1.TRANS_DATE", careDate2, "<=");
    }
    if (!YHUtility.isNullorEmpty(officeDepository)) {
      conditionStr = " and d1.SEQ_ID ='" + YHDBUtility.escapeLike(officeDepository) + "'";
    }
    if (!YHUtility.isNullorEmpty(officeProtype)) {
      conditionStr = " and ty1.SEQ_ID ='" + YHDBUtility.escapeLike(officeProtype) + "'";
    }
    if (!YHUtility.isNullorEmpty(product)) {
      conditionStr = " and p1.SEQ_ID ='" + YHDBUtility.escapeLike(product) + "'";
    }
    String sql = "";
    Statement stmt = null;
    ResultSet rs = null;
    
    try {
      if(module.equals("OFFICE_WPZB")){
        sql = "  SELECT p1.SEQ_ID,p1.PRO_NAME,p1.PRO_UNIT,p1.PRO_STOCK, " 
            + " (SELECT sum(t1.TRANS_QTY) "
            + " FROM oa_office_goods p3 "
            + " join oa_office_trans_records t1 on t1.PRO_ID = p3.SEQ_ID and t1.TRANS_FLAG = 0 "
            + " left join oa_office_kind ty1 on p3.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " where 1=1 and p3.seq_id = p1.seq_id "+ conditionStr1
            + " ORDER BY p3.SEQ_ID DESC) BUG_NUM, "
            + " -(SELECT sum(t1.TRANS_QTY) "
            + " FROM oa_office_goods p3 "
            + " join oa_office_trans_records t1 on t1.PRO_ID = p3.SEQ_ID and t1.TRANS_FLAG = 1 and t1.trans_state = 1 "
            + " left join oa_office_kind ty1 on p3.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " where 1=1 and p3.seq_id = p1.seq_id "+ conditionStr1
            + " ORDER BY p3.SEQ_ID DESC) GET_NUM, "
            + " -(SELECT sum(t1.TRANS_QTY) "
            + " FROM oa_office_goods p3 "
            + " join oa_office_trans_records t1 on t1.PRO_ID = p3.SEQ_ID and t1.TRANS_FLAG = 2 and t1.trans_state = 1 "
            + " left join oa_office_kind ty1 on p3.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " where 1=1 and p3.seq_id = p1.seq_id "+ conditionStr1
            + " ORDER BY p3.SEQ_ID DESC) BORROW_NUM, "
            + " (SELECT sum(t1.TRANS_QTY) "
            + " FROM oa_office_goods p3 "
            + " join oa_office_trans_records t1 on t1.PRO_ID = p3.SEQ_ID and t1.TRANS_FLAG = 3 and t1.trans_state = 1 "
            + " left join oa_office_kind ty1 on p3.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " where 1=1 and p3.seq_id = p1.seq_id "+ conditionStr1
            + " ORDER BY p3.SEQ_ID DESC) RETURN_NUM, "
            + " '' UNRETURN_NUM, "
            + " -(SELECT sum(t1.TRANS_QTY) "
            + " FROM oa_office_goods p3 "
            + " join oa_office_trans_records t1 on t1.PRO_ID = p3.SEQ_ID and t1.TRANS_FLAG = 4 "
            + " left join oa_office_kind ty1 on p3.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " where 1=1 and p3.seq_id = p1.seq_id "+ conditionStr1
            + " ORDER BY p3.SEQ_ID DESC) dumping "
            + " FROM oa_office_goods p1 "
            + " left join oa_office_kind ty1 on p1.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " left join oa_office_repertory d1 on ty1.TYPE_DEPOSITORY = d1.SEQ_ID "
            + " where 1=1 " + conditionStr
            + " group by p1.SEQ_ID,p1.PRO_NAME,p1.PRO_UNIT,p1.PRO_STOCK "
            + " ORDER BY p1.SEQ_ID DESC ";
      }
      else if(module.equals("OFFICE_LYWP")){
        if(!YHUtility.isNullorEmpty(mapType)){
          Map<String, Integer> LYmap = new HashMap();
          List list = new ArrayList();
          sql = " select DEPT_NAME "
            + " from oa_department d1 "
            + " where d1.DEPT_PARENT = 0 ";
          stmt = dbConn.createStatement();
          rs = stmt.executeQuery(sql);         
          while(rs.next()){
            String deptName = rs.getString("DEPT_NAME");
            LYmap.put(deptName, 0);
            list.add(deptName);
          }         
          
          String data = "<graph caption='部门领用汇总' showNames='1'>";
          
          sql = " select p2.DEPT_ID,sum(t1.TRANS_QTY) SUM "
              + " from oa_office_goods p1 "
              + " join oa_office_trans_records t1 on p1.SEQ_ID = t1.PRO_ID and t1.TRANS_FLAG = 1 " + conditionStr1
              + " join PERSON p2 on t1.BORROWER = p2.SEQ_ID "
              + " left join oa_office_kind ty1 on p1.OFFICE_PROTYPE = ty1.SEQ_ID "
              + " left join oa_office_repertory d1 on ty1.TYPE_DEPOSITORY = d1.SEQ_ID "
              + " where 1=1 " + conditionStr
              + " group by p2.DEPT_ID ";
          stmt = dbConn.createStatement();
          rs = stmt.executeQuery(sql);
          while(rs.next()){
            String deptId = rs.getString("DEPT_ID");
            String sum = rs.getString("SUM");
            
            YHOrgSelectLogic orgSelectLogic = new YHOrgSelectLogic();
            YHDepartment Department = orgSelectLogic.getDeptParentId(dbConn,Integer.parseInt(deptId));
            int total = LYmap.get(Department.getDeptName());
            total = total + Integer.parseInt(sum);
            LYmap.put(Department.getDeptName(), total);
          }
          
          for(int i = 0; i < list.size(); i++){
            data = data +  "<set name='" + list.get(i) + "' value='" + LYmap.get(list.get(i)) + "'/>";
            
          }
          return data + "</graph>";
        }
        else{
          String deptId = (String)map.get("deptId");
          StringBuffer sb = new StringBuffer("[");
          boolean flag = false;
          sql = " select p2.USER_NAME,p1.PRO_NAME,-t1.TRANS_QTY TRANS_QTY,t1.TRANS_DATE,t1.PRICE "
              + " from oa_office_goods p1 "
              + " join oa_office_trans_records t1 on p1.SEQ_ID = t1.PRO_ID and t1.TRANS_FLAG = 1 " + conditionStr1
              + " join PERSON p2 on t1.BORROWER = p2.SEQ_ID and p2.DEPT_ID = " + deptId
              + " left join oa_office_kind ty1 on p1.OFFICE_PROTYPE = ty1.SEQ_ID  "
              + " left join oa_office_repertory d1 on ty1.TYPE_DEPOSITORY = d1.SEQ_ID "
              + " where 1=1 " + conditionStr
              + " ORDER BY p2.USER_NAME DESC ";
          
          stmt = dbConn.createStatement();
          rs = stmt.executeQuery(sql);        
          while(rs.next()){
            flag = true;
            sb.append("{userName:\""+rs.getString("USER_NAME")+"\",");
            sb.append("proName:\""+rs.getString("PRO_NAME")+"\",");
            sb.append("transQty:\""+rs.getString("TRANS_QTY")+"\",");
            sb.append("transDate:\""+rs.getString("TRANS_DATE")+"\",");
            sb.append("price:\""+rs.getString("PRICE")+"\"},");
          }
          if(flag)
            sb.deleteCharAt(sb.length()-1); 
          sb.append("]");
          return sb.toString();
        }
      }
      else if(module.equals("OFFICE_WHJL")){
        sql = " select p1.SEQ_ID,p1.PRO_NAME,t1.TRANS_DATE,pe2.USER_NAME OPERATOR,t1.REMARK "
            + " from oa_office_goods p1 "
            + " join oa_office_trans_records t1 on p1.SEQ_ID = t1.PRO_ID and t1.TRANS_FLAG = 5 " + conditionStr1
            + " join PERSON pe2 on t1.OPERATOR = pe2.SEQ_ID "
            + " join oa_office_kind ty1 on p1.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " join oa_office_repertory d1 on ty1.TYPE_DEPOSITORY = d1.SEQ_ID "
            + " where 1=1 " + conditionStr
            + " ORDER BY p1.SEQ_ID DESC ";
      }
      else if(module.equals("OFFICE_WGHWP")){
        sql = " select p1.SEQ_ID,p1.PRO_NAME,'' NORETURN_NUM, "
            + " -(SELECT sum(t1.TRANS_QTY) "
            + " FROM oa_office_goods p3 "
            + " join oa_office_trans_records t1 on t1.PRO_ID = p3.SEQ_ID and t1.TRANS_FLAG = 2 and t1.trans_state = 1 "
            + " left join oa_office_kind ty1 on p3.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " where 1=1 and p3.seq_id = p1.seq_id and t1.BORROWER = pe1.SEQ_ID " + conditionStr1
            + " ORDER BY p3.SEQ_ID DESC) BORROW_NUM, "
            + " -(SELECT sum(t1.TRANS_QTY) "
            + " FROM oa_office_goods p3 "
            + " join oa_office_trans_records t1 on t1.PRO_ID = p3.SEQ_ID and t1.TRANS_FLAG = 3 and t1.trans_state = 1 "
            + " left join oa_office_kind ty1 on p3.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " where 1=1 and p3.seq_id = p1.seq_id and t1.BORROWER = pe1.SEQ_ID " + conditionStr1
            + " ORDER BY p1.SEQ_ID DESC) RETURN_NUM, "
            + " pe1.USER_NAME,min(t1.TRANS_DATE) TRANS_DATE "
            + " from oa_office_goods p1 "
            + " join oa_office_trans_records t1 on p1.SEQ_ID = t1.PRO_ID and t1.TRANS_FLAG = 2 and t1.trans_state = 1 "
            + " join PERSON pe1 on t1.BORROWER = pe1.SEQ_ID "
            + " join oa_office_kind ty1 on p1.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " join oa_office_repertory d1 on ty1.TYPE_DEPOSITORY = d1.SEQ_ID "
            + " where 1=1 " + conditionStr
            + " group by  p1.SEQ_ID,p1.PRO_NAME,pe1.USER_NAME "
            + " ORDER BY p1.SEQ_ID DESC ";
      }     
      else if(module.equals("OFFICE_TZ")){
        sql = " select t1.TRANS_DATE,p1.PRO_NAME,t1.TRANS_FLAG,t1.TRANS_QTY NUM,p1.PRO_UNIT,p1.PRO_PRICE,pe1.USER_NAME OPERATOR "
            + " from oa_office_trans_records t1 "
            + " join oa_office_goods p1 on t1.PRO_ID = p1.SEQ_ID "
            + " join PERSON pe1 on t1.OPERATOR = pe1.SEQ_ID "
            + " join oa_office_kind ty1 on p1.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " join oa_office_repertory d1 on ty1.TYPE_DEPOSITORY = d1.SEQ_ID "
            + " where 1=1 and t1.TRANS_FLAG != 6 " + conditionStr + conditionStr1
            + " ORDER BY p1.SEQ_ID DESC ";
      } 
      else {
        String transFlag = "";
        String temp = "";
        String sign = "";
        if(module.equals("OFFICE_CGWP")){
          if(!YHUtility.isNullorEmpty(mapType)){
            String data = "<graph caption='采购物品' showNames='1'>";
            sql = " select p1.SEQ_ID,p1.PRO_NAME,sum(t1.TRANS_QTY) TOTAL_COUNT "
                + " from oa_office_goods p1 "
                + " join oa_office_trans_records t1 on p1.SEQ_ID = t1.PRO_ID and t1.TRANS_FLAG = 0 " + conditionStr1
                + " join oa_office_kind ty1 on p1.OFFICE_PROTYPE = ty1.SEQ_ID "
                + " join oa_office_repertory d1 on ty1.TYPE_DEPOSITORY = d1.SEQ_ID "
                + " where 1=1 " + conditionStr
                + " group by p1.SEQ_ID,p1.PRO_NAME "
                + " ORDER BY p1.SEQ_ID DESC ";
            stmt = dbConn.createStatement();
            rs = stmt.executeQuery(sql);
            String sumName = "";
            String totalCount = "";
            while(rs.next()){
              sumName = rs.getString("PRO_NAME");
              totalCount = rs.getString("TOTAL_COUNT");
              data = data +  "<set name='" + sumName + "' value='" + totalCount + "'/>";
            }
            return data + "</graph>";
          }
          transFlag = "0";
          temp = "t1.PRICE,";
        }
        else if(module.equals("OFFICE_JYWP")){
          sign = "-";
          transFlag = "2";
          temp = "pe1.USER_NAME USERNAME,";
        }
        else if(module.equals("OFFICE_GHWP")){
          sign = "";
          transFlag = "3";
          temp = "pe1.USER_NAME USERNAME,";
        }
        else if(module.equals("OFFICE_BFWP")){
          sign = "-";
          transFlag = "4";
          temp = "t1.PRICE,";
        }
        sql = " select p1.SEQ_ID,p1.PRO_NAME,t1.TRANS_FLAG,"+sign+"t1.TRANS_QTY TRANS_QTY," + temp + "t1.TRANS_DATE,pe2.USER_NAME OPERATOR,t1.REMARK "
            + " from oa_office_goods p1 "
            + " join oa_office_trans_records t1 on p1.SEQ_ID = t1.PRO_ID and t1.TRANS_FLAG = "+ transFlag +" " + conditionStr1
            + " left join PERSON pe1 on t1.BORROWER = pe1.SEQ_ID "
            + " join PERSON pe2 on t1.OPERATOR = pe2.SEQ_ID "
            + " join oa_office_kind ty1 on p1.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " join oa_office_repertory d1 on ty1.TYPE_DEPOSITORY = d1.SEQ_ID "
            + " where 1=1 " + conditionStr
            + " ORDER BY p1.SEQ_ID DESC ";
      }
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = null;
      
      int pageSize = queryParam.getPageSize();
      int pageIndex = queryParam.getPageIndex();
      String[] nameList = queryParam.getNameStr().split(",");
      YHPageDataList rtList = new YHPageDataList();

      stmt = dbConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = stmt.executeQuery(sql);
      
      //总记录数
      rs.last();
      int recordCnt = rs.getRow();
      if (recordCnt == 0) {
        pageDataList = rtList;
      }
      else{
        rtList.setTotalRecord(recordCnt);
        
        int pageCnt = recordCnt / pageSize;
        if (recordCnt % pageSize != 0) {
          pageCnt++;
        }
        if (pageIndex < 0) {
          pageIndex = 0;
        }
        if (pageIndex > pageCnt - 1) {
          pageIndex = pageCnt - 1;
        }
        rs.absolute(pageIndex * pageSize + 1);
        
        int fieldCnt = nameList.length;
        
        ResultSetMetaData meta = rs.getMetaData();
        int[] typeArray = new int[fieldCnt];
        for (int i = 0; i < fieldCnt; i++) {
          typeArray[i] = meta.getColumnType(i + 1);
        }
        //记录取出记录的条数
        int rtListCount = -1;
        Double sum = 0.0;
        Double total = 0.0;
        do{
          YHDbRecord record = new YHDbRecord();
          for (int j = 0; j < fieldCnt; j++) {
            String name = nameList[j];
            Object value = null;
            int typeInt = typeArray[j];
            if (YHDsType.isDecimalType(typeInt)) {
              value = new Double(rs.getDouble(j + 1));
            }else if (YHDsType.isIntType(typeInt)) {
              value = new Integer(rs.getInt(j + 1));
            }else if (YHDsType.isLongType(typeInt)) {
              value = new Long(rs.getLong(j + 1));
            }else if (YHDsType.isDateType(typeInt)) {
              value = rs.getTimestamp(j + 1);
            }else if (typeInt == Types.CLOB) {
              value = YHSQLParamHepler.clobToString(rs.getClob(j + 1));
            }else {
              value = rs.getString(j + 1);
            }
            record.addField(name, value);
          }
          if((module.equals("OFFICE_CGWP") || module.equals("OFFICE_BFWP")) && YHUtility.isNullorEmpty(mapType) && !rs.isLast()){
            if( rtListCount > -1 ){
              Double agoSeqId = (Double)rtList.getRecord(rtListCount).getValueByName("seqId");
              Double nowSeqId = (Double)record.getValueByName("seqId");
              if(nowSeqId.equals(agoSeqId)){
                Double nowTransQty = (Double)record.getValueByName("transQty");
                Double nowPrice = (Double)record.getValueByName("username");
                sum = sum + nowTransQty*nowPrice;
              }
              else{
                YHDbRecord record1 = new YHDbRecord();
                rtList.addRecord(record1);
                rtListCount++;
                for (int j = 0; j < fieldCnt; j++) {
                  String name = nameList[j];
                  if(j+1 >= fieldCnt){
                    record1.addField(name, "<div align='right'><B>合计："+sum+"元</B></div>");
                    total = total + sum;
                    Double nowTransQty = (Double)record.getValueByName("transQty");
                    Double nowPrice = (Double)record.getValueByName("username");
                    sum = nowTransQty*nowPrice;
                  }
                  else{
                    record1.addField(name, "");
                  }
                }
              }
            }
            else{
              Double nowTransQty = Double.parseDouble(record.getValueByName("transQty")+"");
              Double nowPrice = (Double)record.getValueByName("username");
              sum = sum + nowTransQty*nowPrice;
            }
          }
          rtList.addRecord(record);
          rtListCount++;
          if(rs.isLast() && (module.equals("OFFICE_CGWP") || module.equals("OFFICE_BFWP")) && YHUtility.isNullorEmpty(mapType) && rtListCount > -1){
            YHDbRecord record1 = new YHDbRecord();
            YHDbRecord record2 = new YHDbRecord();
            rtList.addRecord(record1);
            rtListCount++;
            rtList.addRecord(record2);
            rtListCount++;
            Double nowTransQty = Double.parseDouble(record.getValueByName("transQty")+"");
            Double nowPrice = (Double)record.getValueByName("username");
            sum = sum + nowTransQty*nowPrice;
            for (int j = 0; j < fieldCnt; j++) {
              String name = nameList[j];
              if(j+1 >= fieldCnt){
                record1.addField(name, "<div align='right'><B>合计："+sum+"元</B></div>");
                total = total + sum;
                record2.addField(name, "<div align='right'><B>共计："+total+"元</B></div>");
              }
              else{
                record1.addField(name, "");
                record2.addField(name, "");
              }
            }
          }
        }while(rs.next());
        pageDataList = rtList;
      }
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  public ArrayList<YHDbRecord> printExcel(Connection dbConn, Map map, Map request, YHPerson person) throws Exception{
    
    String careDate1 = (String)map.get("careDate1");
    String careDate2 = (String)map.get("careDate2");
    String officeDepository = (String)map.get("officeDepository");
    String officeProtype = (String)map.get("officeProtype");
    String product = (String)map.get("product");
    String module = (String)map.get("module");
    String title = (String)map.get("title");
    String conditionStr = "";
    String conditionStr1 = "";
    String conditionStr2 = "";
    String conditionStr3 = "";
    String conditionStr4 = "";
    String conditionStr5 = "";

    if (!YHUtility.isNullorEmpty(careDate1)) {
      conditionStr1 += " and " + YHDBUtility.getDateFilter("t1.TRANS_DATE", careDate1, ">=");
      conditionStr2 += " and " + YHDBUtility.getDateFilter("t2.TRANS_DATE", careDate1, ">=");
      conditionStr3 += " and " + YHDBUtility.getDateFilter("t3.TRANS_DATE", careDate1, ">=");
      conditionStr4 += " and " + YHDBUtility.getDateFilter("t4.TRANS_DATE", careDate1, ">=");
      conditionStr5 += " and " + YHDBUtility.getDateFilter("t5.TRANS_DATE", careDate1, ">=");
    }
    if (!YHUtility.isNullorEmpty(careDate2)) {
      conditionStr1 += " and " + YHDBUtility.getDateFilter("t1.TRANS_DATE", careDate2, "<=");
      conditionStr2 += " and " + YHDBUtility.getDateFilter("t2.TRANS_DATE", careDate2, "<=");
      conditionStr3 += " and " + YHDBUtility.getDateFilter("t3.TRANS_DATE", careDate2, "<=");
      conditionStr4 += " and " + YHDBUtility.getDateFilter("t4.TRANS_DATE", careDate2, "<=");
      conditionStr5 += " and " + YHDBUtility.getDateFilter("t5.TRANS_DATE", careDate2, "<=");
    }
    if (!YHUtility.isNullorEmpty(officeDepository)) {
      conditionStr = " and d1.SEQ_ID ='" + YHDBUtility.escapeLike(officeDepository) + "'";
    }
    if (!YHUtility.isNullorEmpty(officeProtype)) {
      conditionStr = " and ty1.SEQ_ID ='" + YHDBUtility.escapeLike(officeProtype) + "'";
    }
    if (!YHUtility.isNullorEmpty(product)) {
      conditionStr = " and p1.SEQ_ID ='" + YHDBUtility.escapeLike(product) + "'";
    }
    String sql = "";
    Statement stmt = null;
    ResultSet rs = null;
    
    try {
      if(module.equals("OFFICE_WPZB")){
        sql = " SELECT p1.PRO_NAME,p1.PRO_UNIT,p1.PRO_STOCK "
            + " ,sum(t1.TRANS_QTY) BUG_NUM,sum(t2.TRANS_QTY) GET_NUM,sum(t3.TRANS_QTY) BORROW_NUM "
            + " ,sum(t4.TRANS_QTY) RETURN_NUM,'' UNRETURN_NUM,sum(t5.TRANS_QTY) dumping "
            + " FROM oa_office_goods p1 "
            + " left join oa_office_trans_records t1 on p1.SEQ_ID = t1.PRO_ID and t1.TRANS_FLAG = 0 " + conditionStr1
            + " left join oa_office_trans_records t2 on p1.SEQ_ID = t2.PRO_ID and t2.TRANS_FLAG = 1 " + conditionStr2
            + " left join oa_office_trans_records t3 on p1.SEQ_ID = t3.PRO_ID and t3.TRANS_FLAG = 2 " + conditionStr3
            + " left join oa_office_trans_records t4 on p1.SEQ_ID = t4.PRO_ID and t4.TRANS_FLAG = 3 " + conditionStr4
            + " left join oa_office_trans_records t5 on p1.SEQ_ID = t5.PRO_ID and t5.TRANS_FLAG = 4 " + conditionStr5
            + " left join oa_office_kind ty1 on p1.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " left join oa_office_repertory d1 on ty1.TYPE_DEPOSITORY = d1.SEQ_ID "
            + " where 1=1 " + conditionStr
            + " group by p1.SEQ_ID,p1.PRO_NAME,p1.PRO_UNIT,p1.PRO_STOCK,t1.TRANS_FLAG "
            + " ORDER BY p1.SEQ_ID DESC ";
      }
      else if(module.equals("OFFICE_LYWP")){
        
      }
      else if(module.equals("OFFICE_WHJL")){
        sql = " select p1.PRO_NAME,t1.TRANS_DATE,pe2.USER_NAME OPERATOR,t1.REMARK "
            + " from oa_office_goods p1 "
            + " join oa_office_trans_records t1 on p1.SEQ_ID = t1.PRO_ID and t1.TRANS_FLAG = 5 " + conditionStr1
            + " join PERSON pe2 on t1.OPERATOR = pe2.SEQ_ID "
            + " join oa_office_kind ty1 on p1.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " join oa_office_repertory d1 on ty1.TYPE_DEPOSITORY = d1.SEQ_ID "
            + " where 1=1 " + conditionStr
            + " ORDER BY p1.SEQ_ID DESC ";
      }
      else if(module.equals("OFFICE_WGHWP")){
        sql = " select p1.PRO_NAME,'' NORETURN_NUM,sum(t1.TRANS_QTY) BORROW_NUM,sum(t2.TRANS_QTY) RETURN_NUM,pe1.USER_NAME,min(t1.TRANS_DATE) TRANS_DATE "
            + " from oa_office_goods p1  "
            + " join oa_office_trans_records t1 on p1.SEQ_ID = t1.PRO_ID and t1.TRANS_FLAG = 2 " + conditionStr1
            + " left join oa_office_trans_records t2 on p1.SEQ_ID = t2.PRO_ID and t2.TRANS_FLAG = 3 " + conditionStr2
            + " join PERSON pe1 on t1.BORROWER = pe1.SEQ_ID  "
            + " join oa_office_kind ty1 on p1.OFFICE_PROTYPE = ty1.SEQ_ID  "
            + " join oa_office_repertory d1 on ty1.TYPE_DEPOSITORY = d1.SEQ_ID  "
            + " where 1=1 " + conditionStr
            + " group by  p1.SEQ_ID,p1.PRO_NAME,pe1.USER_NAME "
            + " ORDER BY p1.SEQ_ID DESC ";
      }     
      else if(module.equals("OFFICE_TZ")){
        sql = " select t1.TRANS_DATE,p1.PRO_NAME,t1.TRANS_FLAG,t1.TRANS_QTY NUM,p1.PRO_UNIT,p1.PRO_PRICE,pe1.USER_NAME OPERATOR "
            + " from oa_office_trans_records t1 "
            + " join oa_office_goods p1 on t1.PRO_ID = p1.SEQ_ID "
            + " join PERSON pe1 on t1.OPERATOR = pe1.SEQ_ID "
            + " join oa_office_kind ty1 on p1.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " join oa_office_repertory d1 on ty1.TYPE_DEPOSITORY = d1.SEQ_ID "
            + " where 1=1 " + conditionStr + conditionStr1
            + " ORDER BY p1.SEQ_ID DESC ";
      } 
      else {
        String transFlag = "";
        String temp = "";
        if(module.equals("OFFICE_CGWP")){
          transFlag = "0";
          temp = "t1.PRICE,";
        }
        else if(module.equals("OFFICE_JYWP")){
          transFlag = "2";
          temp = "pe1.USER_NAME USERNAME,";
        }
        else if(module.equals("OFFICE_GHWP")){
          transFlag = "3";
          temp = "pe1.USER_NAME USERNAME,";
        }
        else if(module.equals("OFFICE_BFWP")){
          transFlag = "4";
          temp = "t1.PRICE,";
        }
        sql = " select p1.PRO_NAME,t1.TRANS_FLAG,t1.TRANS_QTY," + temp + "t1.TRANS_DATE,pe2.USER_NAME OPERATOR,t1.REMARK "
            + " from oa_office_goods p1 "
            + " join oa_office_trans_records t1 on p1.SEQ_ID = t1.PRO_ID and t1.TRANS_FLAG = "+ transFlag + " " + conditionStr1
            + " left join PERSON pe1 on t1.BORROWER = pe1.SEQ_ID "
            + " join PERSON pe2 on t1.OPERATOR = pe2.SEQ_ID "
            + " join oa_office_kind ty1 on p1.OFFICE_PROTYPE = ty1.SEQ_ID "
            + " join oa_office_repertory d1 on ty1.TYPE_DEPOSITORY = d1.SEQ_ID "
            + " where 1=1 " + conditionStr
            + " ORDER BY p1.SEQ_ID DESC ";
      }
      
      ArrayList<YHDbRecord>  dbl = new ArrayList<YHDbRecord>();
      stmt = dbConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = stmt.executeQuery(sql);
      
      //总记录数
      rs.last();
      int recordCnt = rs.getRow();
      if (recordCnt == 0) {
        dbl = null;
      }
      else{
        rs.absolute(1);
        ResultSetMetaData meta = rs.getMetaData(); //获得列集
        int col = meta.getColumnCount();   //获得列的个数
        
        int[] typeArray = new int[col];
        for (int i = 0; i < col; i++) {
          typeArray[i] = meta.getColumnType(i + 1);
        }
        
        String[] titleArr = title.split(",");
        //记录取出记录的条数
        do{
          YHDbRecord record = new YHDbRecord();
          for (int j = 0; j < titleArr.length; j++) {
            Object value = null;
            int typeInt = typeArray[j];
            if (YHDsType.isDecimalType(typeInt)) {
              value = new Double(rs.getDouble(j + 1));
            }else if (YHDsType.isIntType(typeInt)) {
              value = new Integer(rs.getInt(j + 1));
            }else if (YHDsType.isLongType(typeInt)) {
              value = new Long(rs.getLong(j + 1));
            }else if (YHDsType.isDateType(typeInt)) {
              value = rs.getTimestamp(j + 1);
            }else if (typeInt == Types.CLOB) {
              value = YHSQLParamHepler.clobToString(rs.getClob(j + 1));
            }else {
              value = rs.getString(j + 1);
            }
            if(meta.getColumnName(j + 1).equals("TRANS_FLAG")){
              switch(Integer.parseInt((String)value)){
                case 0 : value = "采购";break;
                case 1 : value = "领用";break;
                case 2 : value = "借用";break;
                case 3 : value = "归还";break;
                case 4 : value = "报废";break;
                case 5 : value = "维护";break;
              }
              record.addField(titleArr[j], value);
            }
            else if(meta.getColumnName(j + 1).equals("TRANS_DATE")){
              if(value.toString().length() > 11){
                value = value.toString().substring(0, 10);
              }
              record.addField(titleArr[j], value);
            }
            else if(meta.getColumnName(j + 1).equals("UNRETURN_NUM")){
              value = (Double)record.getValueByIndex(j-2) - (Double)record.getValueByIndex(j-1);
              record.addField(titleArr[j], value);
            }
            else
              record.addField(titleArr[j], value);
          }
          dbl.add(record);
        }while(rs.next());
      }
      return dbl;
    } catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  
  public String getOfficeDepository(Connection dbConn)throws Exception{
    StringBuffer data = new StringBuffer();
    data.append("[");
    PreparedStatement ps = null;
    ResultSet rs = null; 
    String sql = " select SEQ_ID,DEPOSITORY_NAME from oa_office_repertory ";
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        String value = rs.getString("SEQ_ID");
        String text = rs.getString("DEPOSITORY_NAME");
        data.append("{value:"+value+",text:\""+text+"\"},");
      }
      if(data.charAt(data.length() - 1) == ','){
        data.deleteCharAt(data.length() - 1);
      }
      data.append("]");
    } catch (SQLException e) {
      throw(e);
    }
    return  data.toString();
  }
}
