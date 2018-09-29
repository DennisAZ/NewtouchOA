package yh.core.funcs.workflow.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;

import yh.core.data.YHDbRecord;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowFormItem;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.praser.YHPraseData2FormUtility;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.funcs.workflow.util.sort.YHFlowComparator;
import yh.core.funcs.workflow.util.sort.YHFlowItemComparator;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;

/**
 * 高级查询
 * @author Think
 *
 */
public class YHFlowWorkAdSearchLogic extends YHFlowWorkSearchLogic{
  public String checkFormula(String condFormula , String flowId , String sortId  , String skin , String contextPath) {
    String result  = null;
    if (!YHUtility.isNullorEmpty(condFormula)) {
      String[] tmpA1 = condFormula.split("\\(");
      String[] tmpA2 = condFormula.split("\\)");
      int t = tmpA2.length;
      if (condFormula.endsWith(")")) {
        t += 1;
      }
      
      if (tmpA1.length != tmpA2.length) {
        String msg = YHWorkFlowUtility.Message("条件公式的左右括号 ( 和 ) 个数不一致！", 0);
        msg += "<br><div align=\"center\"><input type=\"button\" value=\"返回\" class=\"BigButton\" onClick=\"location='"+contextPath+"/core/funcs/workflow/flowrun/query/query.jsp?flowId="+flowId+"&sortId="+sortId+"&skin="+skin+"'\"><div>";
        return msg;
      }
      tmpA1 = condFormula.split("\\[");
      tmpA2 = condFormula.split("\\]");
      t = tmpA2.length;
      if (condFormula.endsWith("]")) {
        t += 1;
      }
      
      if (tmpA1.length != t) {
        String msg = YHWorkFlowUtility.Message("条件公式的左右中括号 [ 和 ] 个数不一致！", 0);
        msg += "<br><div align=\"center\"><input type=\"button\" value=\"返回\" class=\"BigButton\" onClick=\"location='"+contextPath+"/core/funcs/workflow/flowrun/query/query.jsp?flowId="+flowId+"&sortId="+sortId+"&skin="+skin+"'\"><div>";
        return msg;
      }
      tmpA1 = condFormula.split("\\{");
      tmpA2 = condFormula.split("\\}");
      t = tmpA2.length;
      if (condFormula.endsWith("}")) {
        t += 1;
      }
      if (tmpA1.length != tmpA2.length) {
        String msg = YHWorkFlowUtility.Message("条件公式的左右大括号 { 和 } 个数不一致！", 0);
        msg += "<br><div align=\"center\"><input type=\"button\" value=\"返回\" class=\"BigButton\" onClick=\"location='"+contextPath+"/core/funcs/workflow/flowrun/query/query.jsp?flowId="+flowId+"&sortId="+sortId+"&skin="+skin+"'\"><div>";
        return msg;
      }
    }
    return result;
  }
  public Map flowReport(int flowId, HttpServletRequest request,
      Connection conn, YHPerson user, String listFldsStr , int versionNo) throws Exception {
    // TODO Auto-generated method stub
    List<Map> mapList = new ArrayList<Map>();
    Map titles = new LinkedHashMap();
    String condFormula = request.getParameter("condFormula");
    YHFlowRunUtility fu = new YHFlowRunUtility();
    int formId = fu.getFormId(conn, flowId);
    YHORM orm = new YHORM();
    Map formItemQuery = new HashMap();
    YHFormVersionLogic lo =new YHFormVersionLogic();
    int formSeqId = lo.getFormSeqId(conn, versionNo, formId);
    formItemQuery.put("FORM_ID", formSeqId);
    List<YHFlowFormItem> list = orm.loadListSingle(conn, YHFlowFormItem.class , formItemQuery);
    Collections.sort(list , new YHFlowItemComparator());
    String sql = getQuerySql(flowId,  request , conn , user , versionNo);
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(sql);
      while (rs.next()) {
        
        int runId = rs.getInt("RUN_ID");
        String runName = rs.getString("run_Name");
        Timestamp beginTime = rs.getTimestamp("begin_Time");
        Timestamp endTime = rs.getTimestamp("END_TIME");
        
        Map foundFlagArray = new HashMap();
        boolean notFound = false;
        
        Map map2 = new HashMap();
        map2.put("run_Id", runId);
        List<YHFlowRunData> frdList = orm.loadListSingle(conn, YHFlowRunData.class, map2);
        if (YHWorkFlowUtility.isSave2DataTable()){
          YHPraseData2FormUtility util = new YHPraseData2FormUtility();
          frdList = util.tableData2FlowRunData(conn ,  flowId,  runId  , list );
        }
        YHFlowRunUtility util = new YHFlowRunUtility();
        for (YHFlowFormItem item : list) {
          String value = request.getParameter(item.getName());
          int id = item.getItemId();
          String ids = String.valueOf(id);
          String rTmp = "relation_" + id;
          String relation = request.getParameter(rTmp);
          if ((!YHUtility.isNullorEmpty(value) 
              && !"SELECT_ALL_VALUE".equals(value))
              || "10".equals(relation)) {
            if ("CHECKBOX_ON".equals(value)) {
              value = "on";
            } else if ("CHECKBOX_OFF".equals(value)) {
              value = "";
            }
            YHFlowRunData rd = util.getFlowRunData(conn, runId, id, flowId);
            if (rd != null) {
              boolean check = this.checkCondition(relation, YHUtility.null2Empty(rd.getItemData()), value);
              if (check) {
                foundFlagArray.put(ids, true);
              } else {
                foundFlagArray.put(ids, false);
                if (YHUtility.isNullorEmpty(condFormula)) {
                  notFound = true;
                  break;
                }
              }
            }
          } else {
            foundFlagArray.put(ids, true);
          }
        }
        String tmp1 = this.checkCondFormula(condFormula, foundFlagArray);
        if (!"ok".equals(tmp1)) {
          notFound = true;
        } 
        if (notFound) {
          continue;
        }
        
        Map map = new HashMap();
        map.put("runId", String.valueOf(runId));
        map.put("runName", runName);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        String date = format2.format(beginTime);
        String beginTimes = format.format(beginTime);
        map.put("runTime", beginTimes);
        map.put("runDate", date);
        
       //流程状态显示        String status = "";
        if(endTime == null) {
          status = "<font color=red>执行中</font>";
        } else {
          status = "已结束";
        }
        map.put("runStatus", status);
        String hidden = this.getHiddenItem(conn, user, flowId, runId);
        
        for (YHFlowFormItem item : list) {
          String realValue = "";
          String tag = item.getTag();
          String title = item.getTitle();
          int itemId = item.getItemId();
          String content = item.getContent();
          String clazz = item.getClazz();
          //判断是否是保密字段
          if ("DATE".equals(clazz) || "USER".equals(clazz)) {
            continue;
          }
          //不显示的列
          if (YHWorkFlowUtility.findId(listFldsStr, String.valueOf(itemId))) {
            continue;
          }
          if (!YHWorkFlowUtility.findId(hidden, title)) {
            realValue = this.getRealValue(frdList, item);
          } 
          if("LIST_VIEW".equals(clazz)) {
            realValue = realValue.replaceAll("\r", " ");
            //realValue = realValue.replaceAll("\n", " ");
            //realValue = realValue.replaceAll("\r", " ");
          }
          
          //--- 用值替换控件 ----
        //  && "ETAG".equals(name)
          if("AUTO".equals(clazz) && "SELECT".equals(tag) && !"".equals(realValue)) //--- 列表型宏控件 ---
          {
            //String datafld = item.getDatafld();
          } else if("LIST_VIEW".equals(clazz)){
            int sumFlag = 0 ;
            String lvTitle = item.getLvTitle();
            String lvSize = item.getLvSize();
            String lvSum = item.getLvSum();
            
            if ( lvTitle == null ) {
              lvTitle = "";
            }
            if ( lvSize == null ) {
              lvSize = "";
            }
            if ( lvSum == null ) {
              lvSum = "";
            }
            String lvCal = item.getLvCal();
            if ( lvCal == null ) {
              lvCal = "";
            }
            
            String[] lvSumArray = lvSum.split("`");
            if (lvSum.indexOf("1") != -1) {
              sumFlag = 1;
            }
            String[] myArraySize = lvSize.split("`");
            String lvValue = realValue;
            realValue = "<TABLE class='LIST_VIEW' style='border-collapse:collapse' border=1 cellspacing=0 cellpadding=2><TR style='font-weight:bold;font-size:14px;' class='LIST_VIEW_HEADER'>";
            String[] myArray = lvTitle.split("`");
            int arrayCountTitle = myArray.length;
            for (String tmp : myArray) {
              realValue += "<TD nowrap>" + tmp + "</TD>";
            }
            realValue += "</TR>";
            myArray = lvValue.split("\n");
            int arrayCount = myArray.length;
            Float[] sumData = new Float[arrayCountTitle];
            for (String tmp : myArray) {
              String[] myArray1 = tmp.split("`");
              if (!"".equals(tmp) && myArray1.length > 0) {
                realValue += "<tr>";
                for (int j = 0 ;j < arrayCountTitle; j++) {
                  if ("1".equals(lvSumArray[j]) 
                      && myArray1[j] != null
                      && YHUtility.isNumber(myArray1[j])) {
                    if (sumData[j] == null) {
                      sumData[j] = new Float(0) ;
                    }
                    sumData[j] += Float.parseFloat(myArray1[j]);
                  }
                  String tdData = "";
                  if (j < myArray1.length && !"".equals(myArray1[j]) ) {
                    tdData = myArray1[j];
                  } else {
                    tdData = "&nbsp;";
                  }
                  int l = Integer.parseInt(myArraySize[j]);
                  realValue += "<td width=" + (l * 9) +">"+ tdData +"</td>";
                }
                realValue += "</tr>";
              }
            }
            if (sumFlag == 1 && arrayCount > 0 ) {
              realValue += "<tr style='font-weight:bold;'>";
              for (int j= 0 ; j < arrayCountTitle ; j++) {
                String sumValue = "";
                if ("".equals(sumData[j]) || sumData[j] == null) {
                  sumValue = "&nbsp;";
                } else {
                  sumValue = "合计：" + sumData[j];
                }
                int l = Integer.parseInt(myArraySize[j]);
                realValue += "<td align=right width="+ (l * 9) +">" + sumValue +"</td>";
              }
              realValue += "<td>";
            }
            realValue +="</TABLE>";
          } else if("SIGN".equals(clazz)){
            realValue = "";
            continue;
          } else {
            if("AUTO".equals(clazz) && "{宏控件}".equals(realValue)){
              realValue = "";
            }
            realValue = realValue.replaceAll("<", "&lt;");
            realValue = realValue.replaceAll(">", "&gt;");
            realValue = realValue.replaceAll("  ", "&nbsp;&nbsp;");
            realValue = realValue.replaceAll("\r\n", "<br>");
            realValue = realValue.replaceAll("  ", "&nbsp;&nbsp;");
            if("INPUT".equals(tag) && content.indexOf("type=checkbox") != -1){
              if("on".equals(realValue)){
                realValue = "是";
              }else{
                realValue = "否";
              }
            }
          }
          titles.put("DATA_" + itemId, title);
          map.put("DATA_" + itemId, realValue);
        }
        mapList.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    this.sortList(request, mapList);
    Map n = new HashMap();
    n.put("title", titles);
    n.put("result", mapList);
    return n;
  }
  public List<Map> doQuery(int flowId, HttpServletRequest request,
      Connection conn, YHPerson user, int versionNo) throws Exception {
    // TODO Auto-generated method stub
    List<Map> mapList = new ArrayList<Map>();
    String condFormula = request.getParameter("condFormula");
    YHFlowRunUtility fu = new YHFlowRunUtility();
    int formId = fu.getFormId(conn, flowId);
    YHORM orm = new YHORM();
    Map formItemQuery = new HashMap();
    YHFormVersionLogic lo = new YHFormVersionLogic();
    int formSeqId = lo.getFormSeqId(conn, versionNo, formId);
    formItemQuery.put("FORM_ID", formSeqId);
    List<YHFlowFormItem> list = orm.loadListSingle(conn, YHFlowFormItem.class , formItemQuery);
    String sql = getQuerySql(flowId,  request , conn , user , versionNo);
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(sql);
      while (rs.next()) {
        int runId = rs.getInt("RUN_ID");
        String runName = rs.getString("run_Name");
        Timestamp beginTime = rs.getTimestamp("begin_Time");
        String flowType = rs.getString("flow_type");
        String manageUser = rs.getString("MANAGE_USER");
        String queryUser = rs.getString("query_user");
        String attachId = rs.getString("ATTACHMENT_ID");
        String attachName = rs.getString("ATTACHMENT_NAME");
        String commentPriv = rs.getString("COMMENT_PRIV");
        String focusUser = rs.getString("FOCUS_USER");
        Timestamp endTime = rs.getTimestamp("END_TIME");
        String editPriv = rs.getString("EDIT_PRIV");
        String queryUserDept = rs.getString("QUERY_USER_DEPT");
        String manageUserDept = rs.getString("MANAGE_USER_DEPT");
        String freeOther = rs.getString("FREE_OTHER");
        String flowName = rs.getString("FLOW_NAME");
        
        Map foundFlagArray = new HashMap();
        boolean notFound = false;
        
        YHFlowRunUtility util = new YHFlowRunUtility();
        for (YHFlowFormItem item : list) {
          String value = request.getParameter(item.getName());
          int id = item.getItemId();
          String ids = String.valueOf(id);
          String rTmp = "relation_" + id;
         
          String relation = request.getParameter(rTmp);
          if ((!YHUtility.isNullorEmpty(value) 
              && !"SELECT_ALL_VALUE".equals(value))
              || "10".equals(relation)) {
            if ("CHECKBOX_ON".equals(value)) {
              value = "on";
            } else if ("CHECKBOX_OFF".equals(value)) {
              value = "";
            }
            YHFlowRunData rd = util.getFlowRunData(conn, runId, id, flowId);
            if (rd != null) {
              boolean check = this.checkCondition(relation, YHUtility.null2Empty(rd.getItemData()), value);
              if (check) {
                foundFlagArray.put(ids, true);
              } else {
                foundFlagArray.put(ids, false);
                if (YHUtility.isNullorEmpty(condFormula)) {
                  notFound = true;
                  break;
                }
              }
            }
          } else {
            foundFlagArray.put(ids, true);
          }
        }
        String tmp = this.checkCondFormula(condFormula, foundFlagArray);
        if (!"ok".equals(tmp)) {
          notFound = true;
        } 
        if (notFound) {
          continue;
        }
        Map map = new HashMap();
        map.put("runId", String.valueOf(runId));
        map.put("runName", runName);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTimes = format.format(beginTime);
        map.put("beginTime", beginTimes);
        map.put("flowName", flowName);
        
       //流程状态显示
        String status = "";
        if(endTime == null) {
          status = "<font color=red>执行中</font>";
        } else {
          status = "已结束";
        }
        map.put("status", status);
        map.put("endTime", endTime);
        map.put("flowType", flowType);
        this.setFocusPriv(map, endTime, focusUser, user);
        this.setEditPriv(map, endTime, editPriv, conn, user);
        this.setCommentPriv(map, commentPriv, manageUser, queryUser, queryUserDept, manageUserDept, conn, user);
        this.setRunPrcs(map, runId, freeOther, conn, user.getSeqId());
        this.setAttach(map, attachId, attachName, flowType, runId, flowId, conn, user);
        mapList.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return mapList;
  }
  public void setCommentPriv(Map map , String commentPriv , String manageUser , String queryUser , String queryUserDept , String manageUserDept , Connection conn , YHPerson user) throws Exception {
    //显示“点评”超链接 begin
    if (manageUser == null) {
      manageUser = "";
    }
    if (queryUser == null) {
      queryUser = "";
    }
    if (manageUserDept == null) {
      manageUserDept = "";
    }
    if (queryUserDept == null) {
      queryUserDept = "";
    }
    YHPrcsRoleUtility pu = new YHPrcsRoleUtility();
    
    boolean mUserPriv = pu.checkPriv(user, manageUser) ;
    boolean qUserPriv = pu.checkPriv(user, queryUser) ;
    boolean qUserDeptPriv = pu.checkPriv(user, queryUserDept) ;
    boolean mUserDeptPriv = pu.checkPriv(user, manageUserDept) ;
    
    if (("3".equals(commentPriv) && (mUserPriv || qUserPriv || qUserDeptPriv|| mUserDeptPriv ))
        || ("2".equals(commentPriv) && (qUserPriv || qUserDeptPriv))
        || ("3".equals(commentPriv) && (mUserPriv || mUserDeptPriv))) {
      map.put("hasComPriv", true);
    } else {
      map.put("hasComPriv", false);
    }
  }
  public void setAttach(Map map
      , String attachmentId
      , String attachmentName
      , String flowType
      , int runId
      , int flowId
      , Connection conn 
      , YHPerson user) throws Exception{
    if (attachmentName == null || "".equals(attachmentName.trim())) {
      return ;
    }
    if (attachmentId == null || "".equals(attachmentId.trim())) {
      return ;
    }
    String priv = "";
    if ("1".equals(flowType)) {
      YHAttachmentLogic logic = new YHAttachmentLogic();
      priv = logic.getDownPrintPriv((int)runId, (int)flowId, user.getSeqId(), conn);
    } else {
      priv = "1,1";
    }
    
    String[] attachsName = attachmentName.split("\\*");
    String[] attachsId = attachmentId.split(",");
    StringBuffer sb = new StringBuffer("[");
    for ( int i = 0 ;i < attachsId.length ;i ++ ) {
      String tmp = attachsId[i];
      String name = attachsName[i];    
      sb.append("{attachmentName:\"" + YHUtility.encodeSpecial(name) + "\"");
      sb.append(",attachmentId:'" + tmp + "'" +
          ",ext:'" +  YHFileUtility.getFileExtName(name) + "',priv:'"+ priv +"'},");
    }
    if ( attachsId.length > 0 ) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    map.put("attach", sb.toString());
  }
  public void setRunPrcs(Map map , int runId , String freeOther , Connection conn , int userId) throws Exception {
    String opFlag = "";//主办人标识，用于显示委托、收回超链接
    String query = "select  PRCS_ID , PRCS_FLAG , FLOW_PRCS , OP_FLAG from  oa_fl_run_prcs where RUN_ID=" + runId  + " and USER_ID=" + userId + " and PRCS_FLAG <>'4'  order by PRCS_FLAG " ;//“limit 1”的作用？针对并发或者子流程？

    Statement stm1 = null;
    ResultSet rs1 = null;
    int prcsId = 0 ;
    int flowPrcs = 0 ;
    String prcsFlag = "";
    try {
      stm1 = conn.createStatement();
      rs1 = stm1.executeQuery(query);
      if ( rs1.next()) {
        prcsId = rs1.getInt("PRCS_ID");//实际步骤顺序号

        prcsFlag= rs1.getString("PRCS_FLAG");//处理标识（1-未接收；2-办理中；3-转交下一步，下一步经办人无人接收；4-已办结）
        flowPrcs = rs1.getInt("FLOW_PRCS");//步骤定义ID
        opFlag = rs1.getString("OP_FLAG");//主办人标识
      } 
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm1, rs1, null); 
    }
    map.put("prcsId", String.valueOf(prcsId));
    map.put("opFlag", opFlag);
    map.put("flowPrcs", String.valueOf(flowPrcs));
    map.put("prcsFlag", prcsFlag);
    
    if ("1".equals(prcsFlag) || "2".equals(prcsFlag)) {
      map.put("hasHandler", true);
      if (("1".equals(freeOther) && "1".equals(opFlag))
          || "2".equals(freeOther)
          || "3".equals(freeOther)) {
        map.put("hasOther", true);
      } else {
        map.put("hasOther", false);
      }
      map.put("hasCallback", false);
    } else if ("3".equals(prcsFlag) && "1".equals(opFlag)) {//转交下一步，下一步经办人无人接收 并且当前用户是主办人
      map.put("hasCallback", true);
      map.put("hasHandler", false);
      map.put("hasOther", false);
    } else {
      map.put("hasCallback", false);
      map.put("hasHandler", false);
      map.put("hasOther", false);
    }
  }
  public void setEditPriv(Map map ,Timestamp endTime , String privStr , Connection conn , YHPerson user) throws Exception{
    if (endTime != null) {
      if(privStr == null || user == null){
        map.put("hasEdit", false);
        return ;
      }
      String[] aPriv = privStr.split("\\|");
      String privUser = "";
      if (aPriv.length > 0 ) {
        privUser = aPriv[0];
      }
      String privDept = "";
      if (aPriv.length > 1 ) {
        privDept = aPriv[1];
      }
      String privRole = "";
      if (aPriv.length > 2 ) {
        privRole = aPriv[2];
      }
      privDept = YHOrgSelectLogic.changeDept(conn, privDept); 
      if (user.isAdminRole() 
          || YHWorkFlowUtility.findId(privUser,String.valueOf(user.getSeqId())) 
          || YHWorkFlowUtility.findId(privDept,String.valueOf(user.getDeptId())) 
          || YHWorkFlowUtility.findId(privRole,user.getUserPriv())) {
        map.put("hasEdit", true);
      } else {
        map.put("hasEdit", false);
      }
    } else {
      map.put("hasEdit", false);
    }
  }
  public void setFocusPriv(Map map ,Timestamp endTime , String focusUser , YHPerson user) {
    if (endTime == null && !YHWorkFlowUtility.findId(focusUser , String.valueOf(user.getSeqId()))) {
      map.put("hasFocus", true);
      map.put("hasCalFocus", false);
    } else if (YHWorkFlowUtility.findId(focusUser , String.valueOf(user.getSeqId()))) {
      map.put("hasFocus", false);
      map.put("hasCalFocus", true);
    } else {
      map.put("hasFocus", false);
      map.put("hasCalFocus", false);
    }
  }
  private String checkCondFormula(String condFormula , Map fondFlagArray) {
    String result = "ok";
    Set<String> set = fondFlagArray.keySet();
    if (!YHUtility.isNullorEmpty(condFormula)) {
      for (String tmp : set) {
        boolean value = (Boolean)fondFlagArray.get(tmp);
        condFormula = condFormula.replaceAll("\\["+tmp+"\\]", String.valueOf(value));
      }
      try {
        Expression e = ExpressionFactory.createExpression(condFormula);    
        JexlContext jc = JexlHelper.createContext();    
        boolean flag = (Boolean) e.evaluate(jc); 
        if (flag) {
          return result ;
        } else {
          return "表单条件公式不满足！";
        }
      } catch (Exception ex) {
        return "条件公式错误";
      }
    }
    return result;
  }
  private boolean checkCondition(String relation ,String itemData , String value) {
    int iItemData = 0;
    int iValue = 0 ;
    if (YHUtility.isInteger(itemData)) {
      iItemData = Integer.parseInt(itemData);
    }
    if (YHUtility.isInteger(value)) {
      iValue = Integer.parseInt(value);
    }
    if ("1".equals(relation)) {
      if (itemData.equals(value)) {
        return true;
      }
    } else if ("2".equals(relation)) {
      if (iItemData > iValue) {
        return true;
      }
    } else if ("3".equals(relation)) {
      if (iItemData < iValue) {
        return true;
      }
    } else if ("4".equals(relation)) {
      if (iItemData >= iValue) {
        return true;
      }
    } else if ("5".equals(relation)) {
      if (iItemData <= iValue) {
        return true;
      }
    } else if ("6".equals(relation)) {
      if (!itemData.equals(value)) {
        return true;
      }
    } else if ("7".equals(relation)) {
      if (value == null) {
        value = "";
      }
      if (itemData.startsWith(value)) {
        return true;
      }
    } else if ("8".equals(relation)) {
      if (value == null) {
        value = "";
      }
      if (itemData.contains(value)) {
        return true;
      }
    } else if ("9".equals(relation)) {
      if (value == null) {
        value = "";
      }
      if (itemData.endsWith(value)) {
        return true;
      }
    } else if ("10".equals(relation)) {
      if (value == null) {
        value = "";
      }
      if (itemData.equals(value)) {
        return true;
      }
    } else {
      if (value == null) {
        value = "";
      }
      if (itemData.equals(value)) {
        return true;
      }
    }
    return false;
  }
  private String getQuerySql(int flowId, HttpServletRequest request ,Connection conn , YHPerson user, int versionNo) throws Exception {
    // TODO Auto-generated method stub
    String query = "SELECT * from oa_fl_type as FLOW_TYPE,oa_fl_run as FLOW_RUN,PERSON"
      + " WHERE  "
      + " FLOW_RUN.BEGIN_USER=PERSON.SEQ_ID  "
      + " AND FLOW_TYPE.SEQ_ID=FLOW_RUN.FLOW_ID  "
      + " and FLOW_RUN.DEL_FLAG=0 "
      + " and FLOW_TYPE.SEQ_ID=" + flowId
      + " and FORM_VERSION=" + versionNo;
    //--- 文号条件 ---
    String runName = request.getParameter("runName");
    if(!YHUtility.isNullorEmpty(runName)) {
      String rr = request.getParameter("runNameRelation");
      runName = runName.replace("'", "''");
      query += this.getRunNameCondition(runName, rr);
    }
    //--- 公共附件条件 ---
    String attachmentName = request.getParameter("attachmentName");
    if(!YHUtility.isNullorEmpty(attachmentName)) {
      query += " and FLOW_RUN.ATTACHMENT_NAME like '%" + YHUtility.encodeLike(attachmentName) + "%' "  + YHDBUtility.escapeLike();
    }
    //--- 日期范围 ---
    String prcsDate1 = request.getParameter("prcsDate1");
    if(!YHUtility.isNullorEmpty(prcsDate1)){
      prcsDate1 +=  " 00:00:00";
      String dbDateF = YHDBUtility.getDateFilter("BEGIN_TIME", prcsDate1, " >= ");
      query += " and " + dbDateF;
    }
       
    String prcsDate2 = request.getParameter("prcsDate2");
    if(!YHUtility.isNullorEmpty(prcsDate2)){
      prcsDate2 +=  " 23:59:59";
      String dbDateF = YHDBUtility.getDateFilter("BEGIN_TIME", prcsDate2, " <= ");
      query += " and " + dbDateF;
    }
    
    //--- 日期范围 ---
    String endDate1 = request.getParameter("endDate1");
    if(!YHUtility.isNullorEmpty(endDate1)){
      endDate1 +=  " 00:00:00";
      String dbDateF = YHDBUtility.getDateFilter("FLOW_RUN.END_TIME", endDate1, " >= ");
      query += " and " + dbDateF;
    }
       
    String endDate2 = request.getParameter("endDate2");
    if(!YHUtility.isNullorEmpty(endDate2)){
      endDate2 +=  " 23:59:59";
      String dbDateF = YHDBUtility.getDateFilter("FLOW_RUN.END_TIME", endDate2, " <= ");
      query += " and " + dbDateF;
    }
    //查询范围
    String where = this.fqt2WhereString(conn, request.getParameter("flowQueryType"), user, 0);
    query += where;
    //--- 流程状态条件检查 ---
    String flowStatus = request.getParameter("flowStatus");
    if (!"ALL".equals(flowStatus)) {
      if ("0".equals(flowStatus)) {
        query += " and FLOW_RUN.END_TIME is null";
      } else {
        query += " and FLOW_RUN.END_TIME is not null";
      }
    }
    String beginUser = request.getParameter("beginUser");
    if (YHUtility.isInteger(beginUser)) {
      query += " and BEGIN_USER=" + beginUser;
    }
    //--- 开始进行查询 ---
    query +=" order by FLOW_RUN.RUN_ID desc";
    return query;
  }
  public String getRunNameCondition(String runName , String rr) throws Exception{
    String whereRun = " and FLOW_RUN.RUN_NAME";
    if ("1".equals(rr)) {
      whereRun += "='" + runName + "'";
    } else if ("2".equals(rr)) {
      whereRun += ">'" + runName + "'";
    } else if ("3".equals(rr)) {
      whereRun += "<'" + runName + "'";
    } else if ("4".equals(rr)) {
      whereRun += ">='" + runName + "'";
    } else if ("5".equals(rr)) {
      whereRun += "<='" + runName + "'";
    } else if ("6".equals(rr)) {
      whereRun += "!='" + runName + "'";
    } else if ("7".equals(rr)) {
      whereRun += " like '" + YHUtility.encodeLike(runName) + "%' " + YHDBUtility.escapeLike();
    } else if ("8".equals(rr)) {
      whereRun += " like '%" + YHUtility.encodeLike(runName) + "%' " + YHDBUtility.escapeLike();
    } else if ("9".equals(rr)) {
      whereRun += " like '%" + YHUtility.encodeLike(runName) + "' " + YHDBUtility.escapeLike();
    } else {
      whereRun += " like '%" + YHUtility.encodeLike(runName) + "%' " + YHDBUtility.escapeLike();
    }
    return whereRun;
  }
  public String getRealValue(List<YHFlowRunData> frdList, YHFlowFormItem item){
    String realValue = "";
    for (YHFlowRunData flowRunData : frdList) {
      if (item.getItemId() == flowRunData.getItemId()) {
        //$ITEM_VALUE = str_replace(array('"','<','>'),array("&quot;","&lt;","&gt;"),$ITEM_VALUE);
        //可能会进行以上语句的处理
        if(flowRunData.getItemData() != null){
          realValue = flowRunData.getItemData();
        }
      }
    }
    return realValue;
  }
  public String getHiddenItem(Connection conn , YHPerson loginUser , int flowId , int runId) throws Exception {
    String sql = "select HIDDEN_ITEM from oa_fl_process,oa_fl_run_prcs as FLOW_RUN_PRCS where oa_fl_process.FLOW_SEQ_ID="+flowId+" and FLOW_RUN_PRCS.RUN_ID="+ runId +" and FLOW_RUN_PRCS.USER_ID="+loginUser.getSeqId()+" and oa_fl_process.PRCS_ID=FLOW_RUN_PRCS.FLOW_PRCS";
    Statement stm2 = null;
    ResultSet rs2 = null;
    String hidden = "";
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(sql);
      while (rs2.next()) {
        String tmp = rs2.getString("HIDDEN_ITEM");
        if (tmp != null) {
          hidden += tmp;
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null);
    }
    return hidden;
  }
  public ArrayList<YHDbRecord> getExcelData(int flowId, String runIds,
      Connection conn, String contextPath, YHPerson loginUser) throws Exception {
    // TODO Auto-generated method stub
    YHFlowRunUtility fu = new YHFlowRunUtility();
    int formId = fu.getFormId(conn, flowId);
    YHORM orm = new YHORM();
    Map formItemQuery = new HashMap();
    formItemQuery.put("FORM_ID", formId);
    List<YHFlowFormItem> itemList = orm.loadListSingle(conn, YHFlowFormItem.class , formItemQuery);
    ArrayList<YHDbRecord> list = new ArrayList<YHDbRecord>(); 
    runIds = YHWorkFlowUtility.getOutOfTail(runIds);
    String query = "select run_id , run_NAME , END_TIME , BEGIN_TIME from oa_fl_run where run_id in (" + runIds + ") order by run_id";
    Statement stm = null;
    ResultSet rs = null;
    try{
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        YHDbRecord dr = new YHDbRecord();
        int runId = rs.getInt("RUN_ID");
        dr.addField("流水号", runId);
        String runName =rs.getString("RUN_NAME");
        dr.addField("工作名称/文号", runName);
        Timestamp endTime = rs.getTimestamp("END_TIME");
        Timestamp beginTime = rs.getTimestamp("BEGIN_TIME");
        String status = "执行中";
        if (endTime != null) {
          status = "已结束";
        }
        dr.addField("流程状态", status);
        String sBeginTime = YHUtility.getDateTimeStr(beginTime);
        dr.addField("流程开始时间", sBeginTime);
        Map map = new HashMap();
        map.put("RUN_ID", runId);
        List<YHFlowRunData> frdList = orm.loadListSingle(conn, YHFlowRunData.class, map);
        String hidden = this.getHiddenItem(conn, loginUser, flowId, runId);
        
        for (YHFlowFormItem item : itemList) {
          String realValue = "";
          String tag = item.getTag();
          String value = item.getValue();
          String title = item.getTitle();
          int itemId = item.getItemId();
          String content = item.getContent();
          String clazz = item.getClazz();
          String name = item.getName();
          //判断是否是保密字段
          if ("DATE".equals(clazz) || "USER".equals(clazz)) {
            continue;
          }
          if (!YHWorkFlowUtility.findId(hidden, title)) {
            realValue = this.getRealValue(frdList, item);
          } 
          
          if("LIST_VIEW".equals(clazz)) {
            realValue = realValue.replaceAll("\r\n", " ");
          }
          //--- 用值替换控件 ----
        //  && "ETAG".equals(name)
          if("AUTO".equals(clazz) && "SELECT".equals(tag) && !"".equals(realValue)) //--- 列表型宏控件 ---
          {
            String datafld = item.getDatafld();
            if("SYS_LIST_DEPT".equals(datafld)){
              String queryAuto = "select " 
                + " DEPT_NAME " 
                + " from  oa_department where "
                + " SEQ_ID = " + realValue;
              Statement stm2 = null;
              ResultSet rs2 = null ;
              try {
                stm2 = conn.createStatement();
                rs2 = stm2.executeQuery(queryAuto);
                if (rs2.next()) {
                  realValue = rs2.getString("DEPT_NAME");
                }
              } catch(Exception ex) {
                throw ex;
              } finally {
                YHDBUtility.close(stm2, rs2, null); 
              }
            }else if("SYS_LIST_PRIV".equals(datafld)){
              String queryAuto = "select " 
                + " PRIV_NAME " 
                + " from  USER_PRIV where "
                + " SEQ_ID = " + realValue;
              Statement stm2 = null;
              ResultSet rs2 = null ;
              try {
                stm2 = conn.createStatement();
                rs2 = stm2.executeQuery(queryAuto);
                if (rs2.next()) {
                  realValue = rs2.getString("PRIV_NAME");
                }
              } catch(Exception ex) {
                throw ex;
              } finally {
                YHDBUtility.close(stm2, rs2, null); 
              }
            }
          } else if("LIST_VIEW".equals(clazz)){
            int sumFlag = 0 ;
            String lvTitle = item.getLvTitle();
            String lvSize = item.getLvSize();
            String lvSum = item.getLvSum();
            if ( lvTitle == null ) {
              lvTitle = "";
            }
            if ( lvSize == null ) {
              lvSize = "";
            }
            if ( lvSum == null ) {
              lvSum = "";
            }
            String lvCal = item.getLvCal();
            if ( lvCal == null ) {
              lvCal = "";
            }
            
            String[] lvSumArray = lvSum.split("`");
            if (lvSum.indexOf("1") != -1) {
              sumFlag = 1;
            }
            String[] myArraySize = lvSize.split("`");
            String lvValue = realValue;
            realValue = "<TABLE class='LIST_VIEW' style='border-collapse:collapse' border=1 cellspacing=0 cellpadding=2><TR style='font-weight:bold;font-size:14px;' class='LIST_VIEW_HEADER'>";
            String[] myArray = lvTitle.split("`");
            int arrayCountTitle = myArray.length;
            for (String tmp : myArray) {
              realValue += "<TD nowrap>" + tmp + "</TD>";
            }
            realValue += "</TR>";
            myArray = lvValue.split("\r\n");
            int arrayCount = myArray.length;
            Float[] sumData = new Float[arrayCountTitle];
            for (String tmp : myArray) {
              String[] myArray1 = tmp.split("`");
              if (!"".equals(tmp) && myArray1.length > 0) {
                realValue += "<tr>";
                for (int j = 0 ;j < arrayCountTitle; j++) {
                  if ("1".equals(lvSumArray[j]) 
                      && myArray1[j] != null
                      && YHUtility.isNumber(myArray1[j])) {
                    if (sumData[j] == null) {
                      sumData[j] = new Float(0) ;
                    }
                    sumData[j] += Float.parseFloat(myArray1[j]);
                  }
                  String tdData = "";
                  if (j < myArray1.length && !"".equals(myArray1[j]) ) {
                    tdData = myArray1[j];
                  } else {
                    tdData = "&nbsp;";
                  }
                  int l = Integer.parseInt(myArraySize[j]);
                  realValue += "<td width=" + (l * 9) +">"+ tdData +"</td>";
                }
                realValue += "</tr>";
              }
            }
            if (sumFlag == 1 && arrayCount > 0 ) {
              realValue += "<tr style='font-weight:bold;'>";
              for (int j= 0 ; j < arrayCountTitle ; j++) {
                String sumValue = "";
                if ("".equals(sumData[j]) || sumData[j] == null) {
                  sumValue = "&nbsp;";
                } else {
                  sumValue = "合计：" + sumData[j];
                }
                int l = Integer.parseInt(myArraySize[j]);
                realValue += "<td align=right width="+ (l * 9) +">" + sumValue +"</td>";
              }
              realValue += "<td>";
            }
            realValue +="</TABLE>";
          } else if("SIGN".equals(clazz)){
            realValue = "";
          } else {
            if("AUTO".equals(clazz) && "{宏控件}".equals(realValue)){
              realValue = "";
            }
          
            realValue = realValue.replaceAll("<", "&lt;");
            realValue = realValue.replaceAll(">", "&gt;");
            realValue = realValue.replaceAll("  ", "&nbsp;&nbsp;");
            realValue = realValue.replaceAll("\r\n", "<br>");
            realValue = realValue.replaceAll("  ", "&nbsp;&nbsp;");
            if("INPUT".equals(tag) && content.indexOf("type=checkbox") != -1){
              if("on".equals(realValue)){
                realValue = "是";
              }else{
                realValue = "否";
              }
            }
          }
          dr.addField(title, realValue);
        }
        list.add(dr);
      }
    }catch(Exception e){
      throw e;
    }finally{
      YHDBUtility.close(stm, rs, null);
    }
    return list;
  }
  public void sortList(HttpServletRequest request,List<Map> list) {
    String groupFld = request.getParameter("groupFld");
    String groupSort = request.getParameter("groupSort");
    Collections.sort(list, new YHFlowComparator(groupFld , groupSort));
  }
}
