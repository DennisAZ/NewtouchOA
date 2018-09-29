package yh.core.funcs.doc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.funcs.doc.data.YHDocFlowFormItem;
import yh.core.funcs.doc.data.YHDocFlowProcess;
import yh.core.funcs.doc.data.YHDocFlowRunData;
import yh.core.funcs.doc.data.YHDocFlowRunPrcs;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.logic.YHConfigLogic;
import yh.core.util.YHRegexpUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHPraseData2Form {
 private String itemValueText = "";
 private String signObject = "";
 private String signCheckStr = "";
 private boolean isHaveSign = false;
 private String readOnlyStr = "";
 public String parseForm(YHPerson user , String modelShort , YHDocFlowProcess fp,
      YHDocFlowRunPrcs frp, YHDocFlowType ft, List<YHDocFlowRunData> frdList,
      List<YHDocFlowFormItem> itemList , String ip , Connection conn) throws Exception {
    String hiddenStr = "";
    
    //取得隐藏字段
    String hidden = "";
    if (fp != null) {
      hidden = fp.getHiddenItem() == null ? "" : fp.getHiddenItem();
    }
    for (YHDocFlowFormItem item : itemList) {
      String realValue = this.getRealValue(frdList, item);
      String tag = item.getTag();
      String value = item.getValue();
      String title = item.getTitle();
      int itemId = item.getItemId();
      String content = item.getContent();
      String tag1 = tag.toLowerCase();
      if (content != null)
        content = content.replace("<" + tag1, "<" + tag);
      item.setContent(content);
      String clazz = item.getClazz();
      String name = item.getName();
      
      if (YHWorkFlowUtility.findId(hidden, title) 
          || (("DATE".equals(clazz) || "USER".equals(clazz))&& YHWorkFlowUtility.findId(hidden, value))) {
        // 替换为空
        modelShort = modelShort.replaceAll("\\{" + name + "\\}", "");
        hiddenStr += itemId + ",";
        continue;
      }
      String opFlag = frp.getOpFlag();
      String flowType = ft.getFlowType();
      String freeItem = frp.getFreeItem();
      //会签,或者(自由流程且FREE_ITEM不为空且该字段不在freeItem中)
      //或者(固定流程且不是可写字段)
      //且(不是日期控件和部门用户选择控件)
      boolean isReadOnly = "0".equals(opFlag)
          || ("2".equals(flowType) &&  freeItem!= null && !"".equals(freeItem) && !YHWorkFlowUtility.findId(freeItem, title))
          || (("1".equals(flowType) && !YHWorkFlowUtility.findId(fp.getPrcsItem(), title)))
          && !"DATE".equals(clazz) && !"USER".equals(clazz);
      realValue = YHWorkFlowUtility.getOutSpecialChar(realValue);
      if ("{宏控件}".equals(realValue)) {
        realValue = "";
      }
      if ("RADIO".equals(clazz)
          && "IMG".equals(tag)) {
        content = this.getRadio(item , isReadOnly, itemList , realValue);
      }
      if ("INPUT".equals(tag)) {
      //判断是否是保密字段
        content = this.getInput(item, realValue);
      } else if ("TEXTAREA".equals(tag)) {
        content = this.getTextArea(item, realValue);
      } else if ("SELECT".equals(tag) && !"AUTO".equals(clazz)) {
        content = this.getSelect(item, realValue, itemList);
      }
      if ("DATE".equals(clazz)) {
        boolean flag = (("1".equals(flowType)  && YHWorkFlowUtility.findId(fp.getPrcsItem() , value))
            ||("2".equals(flowType)&& ("".equals(freeItem) || YHWorkFlowUtility.findId(freeItem ,value))))&& "1".equals(opFlag);
        content = this.getDate(item, itemList , flag);
      }
      if ("USER".equals(clazz)) {
         boolean flag = (("1".equals(flowType)  && YHWorkFlowUtility.findId(fp.getPrcsItem() , value))
             ||("2".equals(flowType) && ("".equals(freeItem) || YHWorkFlowUtility.findId(freeItem, value))))
                 && "1".equals(opFlag);
        content = this.getUserAndDept(item, isReadOnly, itemList, frdList , flag,conn);
        // 计算控件
      } else if ("CALC".equals(clazz)) {
        content = this.getCalc(value, itemList, itemId , content);
      } else if ("AUTO".equals(clazz)) {
        content = this.getAuto(item, itemList, user  ,isReadOnly, ft, fp, frp ,realValue , content ,ip ,conn);
      } else if ("LIST_VIEW".equals(clazz)) {
        content = this.getListView(item, isReadOnly, realValue, itemList, user);
      } else if ("SIGN".equals(clazz)) {
        content = this.getSign(item, isReadOnly, realValue, itemList);
      } else if ("DATA".equals(clazz)) {
        content = this.getData(item, isReadOnly, itemList);
      } else if ("MODULE".equals(clazz)) {
        content = this.getModule(item , realValue, isReadOnly);
      }else if ("FETCH".equals(clazz)) {
        content = this.getFetch(item , isReadOnly, itemList);
      }else if ("WORDSELECT".equals(clazz)) {
        boolean flag = (("1".equals(flowType)  && YHWorkFlowUtility.findId(fp.getPrcsItem() , value))
            ||("2".equals(flowType)&& ("".equals(freeItem) || YHWorkFlowUtility.findId(freeItem ,value))))&& "1".equals(opFlag);
        content = getWordSelect(item, itemList, conn , flag);
      }
      if (isReadOnly) {
        content = this.setReadOnly(item, content, fp, frp, ft, realValue);
      }
      content = content.replace("$", "\\$");
      modelShort = modelShort.replaceAll("\\{" + name + "\\}", content);
    }
    //处理保密字段
    if (!"".equals(hiddenStr)) {
      modelShort += "<input value='" + hiddenStr + "' type='hidden' id='hiddenStr' name='hiddenStr'/>";
    }
    //处理只读字段
    if (!"".equals(readOnlyStr)) {
      modelShort += "<input value='" + readOnlyStr + "' type='hidden' id='readOnlyStr' name='readOnlyStr'/>";
    }
    //处理签章控件
    String sign  = "";
    sign += "<script>";
    if (isHaveSign) {
      if (signObject.endsWith(",")) {
        signObject = signObject.substring(0, signObject.length() -1 );
      }
      sign += "sign_str = \"" + signObject + "\";";
      sign += "sign_arr = sign_str.split(\",\");";
      
      if (signCheckStr.endsWith(",")) {
        signCheckStr = signCheckStr.substring(0, signCheckStr.length() -1 );
      }
      
      sign += "sign_check = {" + signCheckStr + "};";
      sign += "isHaveSign = true;LoadSignData();";
    }
    YHConfigLogic logic = new YHConfigLogic();
    String sealForm = logic.getSysPar("SEAL_FROM", conn);
    if (sealForm == null  || "".equals(sealForm)) {
      sealForm = "1";
    }
    sign += "sealForm = " + sealForm + ";"; 
    sign += "</script>";
    modelShort = sign + modelShort;
    return modelShort;
  }
 public String getWordSelect (YHDocFlowFormItem item, List<YHDocFlowFormItem> itemList  ,Connection conn ,boolean flag) throws Exception {
   String content = item.getContent();
   String realValue = item.getValue();
   String type = item.getType();
   String itemStr = "";
   int itemId2 = 0;
   for (YHDocFlowFormItem tmp : itemList) {
     String title2 = tmp.getTitle();
     String clazz2 = tmp.getClazz();
     itemId2 = tmp.getItemId();

     if ("DATE".equals(clazz2) 
         || "USER".equals(clazz2)  
         || "WORDSELECT".equals(clazz2)) {
       continue;
     }//注意。。。
     if (title2.equals(realValue)) {
       itemStr = "DATA_" + itemId2;
       break;
     }
   }
   if (flag){
     
     if ( type == null || "".equals(type) || "0".equals(type)) {
       content = "<IMG class=WORDSELECT align=absmiddle title=主题词选择控件："
         +  realValue +" style=\"CURSOR: hand\" src=\"img/word.gif\" border=0 onclick=\"selectWord('"+ itemStr +"'  , ',')\">";
     } else if (type.equals("1")) {
       content = "<IMG class=WORDSELECT align=absmiddle title=主题词选择控件："
         +  realValue +" style=\"CURSOR: hand\" src=\"img/word.gif\" border=0 onclick=\"selectWord('"+ itemStr +"'  , ' ')\">";
     }
   } else {
     content = "<IMG class=WORDSELECT align=absmiddle title=主题词选择控件："
         + realValue
         + " style=\"CURSOR: hand\" src=\"img/word.gif\" border=0>";
   }
   return content;
 }
 private String getModule(YHDocFlowFormItem item , String seqId , boolean isReadOnly) {
   // TODO Auto-generated method stub
     String module = item.getValue();
     String content = item.getContent();
     int itemId = item.getItemId();
     String divId = "module-" + module  + "-DATA_"+ itemId;
     content = "<div id=\""+ divId +"\">"+ content +"</div>";
     content += "<script>";
     content += "editModuleContent(\""+ module +"\" , \""+ divId +"\" ,  \""+ seqId +"\",  "+isReadOnly+")";
     content += "</script>";
     return content;
   }
  //设置只读
  public String setReadOnly(YHDocFlowFormItem item  , String content , YHDocFlowProcess fp,
      YHDocFlowRunPrcs frp, YHDocFlowType ft  , String realValue) {
    // TODO Auto-generated method stub
    String title = item.getTitle();
    String clazz = item.getClazz();
    int itemId = item.getItemId();
    String tag  = item.getTag();
    //注意
    //不允许在不可写情况下自动赋值的宏控件，且不是计算控件。    boolean flag = true;
    if (fp != null) {
      flag = !YHWorkFlowUtility.findId(fp.getPrcsItemAuto(), title);
    }
    if ( flag && !"CALC".equals(clazz)) {
      readOnlyStr += itemId + ",";
    }
    //是checkbox
    if (content.indexOf("type=checkbox") != -1
        || content.indexOf("type=\"checkbox\"") != -1    
        || content.indexOf("type=\\\"checkbox\\\"") != -1      
    ) {
      if (content.indexOf(" CHECKED") != -1) {//是选中
        String tag1 = tag.toLowerCase();
        content = content.replace("<" + tag1, "<" + tag);
        content = content.replaceAll("<" + tag, "<" + tag
            + " readonly onclick=\"this.checked=1\"; class=BigStatic");
      } else {//不是选中
        String tag1 = tag.toLowerCase();
        content = content.replace("<" + tag1, "<" + tag);
        content = content.replaceAll("<" + tag, "<" + tag
            + " readonly onclick=\"this.checked=0\"; class=BigStatic");
      }
    }else if (!"LIST_VIEW".equals(clazz) && !"SIGN".equals(clazz)) {
      content = content.replaceAll("<" + tag, "<" + tag
          + " readOnly class=BigStatic ");
    }
    //如果是select
    if ("SELECT".equals(tag)) {
      String value = item.getValue() == null ? "" : item.getValue();
      if (!"AUTO".equals(clazz)) {
        // 注意
        int index = realValue.indexOf("|");
        String text = realValue;
        if (index != -1) {
          text = text.substring(0 , index);
        }
        content= content.substring(0, content.indexOf(">") + 1) + "<OPTION value="+ realValue+">"+  text +"</OPTION></SELECT>"; 
      }else{
        content = content.substring(0, content.indexOf(">") + 1) + "<OPTION value="+ realValue+">"+ itemValueText +"</OPTION></SELECT>";
      }
    } else {
      //可输入项，突出输入颜色
      if (("SELECT".equals(tag) || "INPUT".equals(tag) || "TEXTAREA"
          .equals(tag))
          && !"AUTO".equals(clazz)
          && !"FETCH".equals(clazz)
          && !"LITTLE_SEAL_DIV".equals(clazz)) {
       // content = "<" + tag
       // + " class=BigInput onDblClick=\"quick_load(this,\\\""
      //  + frp.getRunId() + "\\\",\\\"" + ft.getSeqId()
      //  + "\\\")\" onkeypress=check_send(this) "
      //  + content.replaceAll("<" + tag, "");
      }
    }
    return content;
  }
  private String getFetch(YHDocFlowFormItem item , boolean isReadonly, List<YHDocFlowFormItem> itemList) {
    // TODO Auto-generated method stub
    if (!isReadonly) {
      String dataControl = YHUtility.null2Empty(item.getDataControl());
      String myArray[] = dataControl.split("`");
      String itemStr = "";
      for (String ss : myArray) {
        int itemId1 = 0;
        int findFlag = 0 ;
        for (YHDocFlowFormItem tmp : itemList) {
          String title = YHUtility.null2Empty(tmp.getTitle());
          String clazz = tmp.getClazz();
          itemId1 = tmp.getItemId();
          if ("DATE".equals(clazz) || "USER".equals(clazz)) {
            continue;
          }
          if (title.equals(ss)) {
            findFlag = 1;
            itemStr += "DATA_"+ itemId1 +",";
            break;
          }
        }
       // if(findFlag == 0)
         //itemStr += ",";
      }
     String content = item.getContent();
     String tag = item.getTag();
     String name = item.getName();
     String tag1 = tag.toLowerCase();
     content = content.replace("<" + tag1, "<" + tag);
     content = content.replace("<" + tag, "<INPUT type=text size=10 id=\""+name+"\" value=\"输入流水号..\" onclick=\"javascript:this.value=''\"><" + tag + " type=\"button\" onclick=data_fetch(this,document.getElementById(\""+name+"\").value,\""+ itemStr +"\") ");
     return content;
    }else {
      return "";
    }
  }
  private String getRadio(YHDocFlowFormItem item , boolean isReadonly, List<YHDocFlowFormItem> itemList , String itemValue) {
    // TODO Auto-generated method stub
      String radioField = YHUtility.null2Empty(item.getRadioField());
      String radioCheck = YHUtility.null2Empty(item.getRadioCheck());
      String[] radioArray = radioField.split("`");
      String name = item.getName();
      String elOut = "";
      String disabled = "";
      if (!YHUtility.isNullorEmpty(itemValue)) {
        radioCheck = itemValue;
      }
      if(isReadonly)
        disabled =  "disabled";
      for (String s : radioArray) {
        String checked = "";
        if (s.equals(radioCheck)) {
          checked = "checked";
        }
        elOut += "<input type=\"radio\" name=\""+name+"\" value=\""+s+"\" "+checked+" "+disabled+"><label>"+ s +"</label>&nbsp;";
      }
      return elOut;
  }
  public String getData(YHDocFlowFormItem item , boolean isReadonly, List<YHDocFlowFormItem> itemList){
    // TODO Auto-generated method stub
    if (!isReadonly) {
      String dataControl = YHUtility.null2Empty(item.getDataControl());
      String dataType = YHUtility.null2Empty(item.getDataType());
      String myArray[] = dataControl.split("`");
      String itemStr = "";
      for (String ss : myArray) {
        int itemId1 = 0;
        int findFlag = 0 ;
        for (YHDocFlowFormItem tmp : itemList) {
          String title = YHUtility.null2Empty(tmp.getTitle());
          String clazz = tmp.getClazz();
          itemId1 = tmp.getItemId();
          if ("DATE".equals(clazz) || "USER".equals(clazz)) {
            continue;
          }
          if (title.equals(ss)) {
            findFlag = 1;
            itemStr += "DATA_"+ itemId1 +",";
            break;
          }
        }
       // if(findFlag == 0)
         //itemStr += ",";
      }
      String content = item.getContent();
      String tag = item.getTag();
      String tag1 = tag.toLowerCase();
      content = content.replace("<" + tag1, "<" + tag);
      if ("".equals(dataType) || "0".equals(dataType)) {
        content = content.replace("<" + tag, "<" + tag + " type=\"button\" onclick=data_picker(this,\""+ itemStr +"\") ");
      } else {
        content = content.replace("<" + tag, "<" + tag + " type=\"button\"  style=\"display:none\" ");
        content +="<script></script>";
      }
      return content;
    } else {
      return "";
    }
  }
  
  public String getInput(YHDocFlowFormItem item,
      String realValue) throws Exception {
    String content = item.getContent();
    String tag = item.getTag();
    int id = item.getItemId();
    //原来的默认的值
    String value = item.getValue() == null ? "": item.getValue();
    if("{宏控件}".equals(value) || "\\\"{宏控件}\\\"".equals(value)){
      value = "\\{宏控件\\}";//加上转义符...为后面的replaceAll
    }
    realValue = realValue == null ? "": realValue;
    if ("\\{宏控件}\\".equals(realValue)) {
      realValue = "{宏控件}";
    }
    String tag1 = tag.toLowerCase();
    content = content.replace("<" + tag1, "<" + tag);
    if (content.indexOf("type=checkbox") == -1
        && content.indexOf("type=\"checkbox\"") == -1    
        && content.indexOf("type=\\\"checkbox\\\"") == -1    
    ) {
      content = content.replaceAll("value=" + value, "");
      String hidden = item.getHidden();
      String hiddenStr = "";
      if("1".equals(hidden))
        hiddenStr = " type=\"hidden\" ";
     
     content = content.replaceAll("<" + tag, "<" + tag +  hiddenStr );
      content = content.replace("<" + tag, "<" + tag + " value=\""
          + realValue + "\"");
      content = content.replace("<" + tag, "<" + tag + " id=\"DATA_"
          + id + "\"");
      content = content.replace("<" + tag, "<" + tag + " ondblclick=\"quickLoad(this)\"");
    } else {
      //去掉原来的值      
      content = content.replaceAll(" value=\"on\"", "");
      content = content.replaceAll(" value=\"\"", "");
      content = content.replaceAll(" CHECKED", "");
      content = content.replaceAll(" checked=\"checked\"", "");
      //加上现在的值      
      if ("on".equals(realValue)) {
        content = content.replaceAll("<" + tag, "<" + tag + " CHECKED");
      }
    }
    content = YHWorkFlowUtility.addId(content, "DATA_" + id  , tag);
    return content;
  }
  
  public String getTextArea(YHDocFlowFormItem item,
      String realValue) {
    String content = item.getContent();
    String tag = item.getTag();
    int id = item.getItemId();
    String value = item.getValue() == null ? "" : item.getValue();
    content = content.replaceAll(">" + value + "<", ">" + realValue + "<");
    content = content.replaceAll("<" + tag, "<" + tag + " id=\"DATA_"
        + id + "\"");
    content = content.replace("<" + tag, "<" + tag + " ondblclick=\"quickLoad(this)\" ");
    content = YHWorkFlowUtility.addId(content, "DATA_" + id , tag);
    return content;
  }

  public String getSelect(YHDocFlowFormItem item,
      String realValue, List<YHDocFlowFormItem> itemList) {
    String content = item.getContent();
    String child = item.getChild();
    String tag = item.getTag();
    int itemId = item.getItemId();
    String itemStr = "";
    // 有子select,,child 不为空,先不考虑
    if (child != null && !"".equals(child)) {
      int count = 0 ;
      for (YHDocFlowFormItem tmp : itemList) {
        String title2 = tmp.getTitle();
        String clazz2 = tmp.getClazz();
        int itemId2 = tmp.getItemId();
        if ("DATE".equals(clazz2) || "USER".equals(clazz2)) {
          continue;
        }
        if (title2.equals(child)) {
          itemStr += "DATA_" + itemId2 + ",";
          count ++ ;
        }
      }
      if (count > 0 ) {
        itemStr = itemStr.substring(0, itemStr.length() - 1);
      }
      content = content.replaceAll("<SELECT",
          "<SELECT onchange=\"selectChange(this.value,'" + itemStr + "')\"");
      content = content.replaceAll("<select",
          "<select onchange=\"selectChange(this.value,'" + itemStr + "')\"");
    }
    if (realValue !=null && !"".equals(realValue)) {
      content = content.replaceAll(" selected", "");
      String tmp = realValue.replaceAll("\\|", "\\\\|");
      content = content.replaceAll("<OPTION value=" + tmp + ">",
          "<OPTION selected value=\"" + realValue + "\">" );
      content = content.replaceAll("<OPTION value=\"" + tmp + "\">",
          "<OPTION selected value=\"" + realValue + "\">");
    }
    if (child != null && !"".equals(child)) {
      content += "<script>";
      String[] arrayValue = itemStr.split(",");
      for (int i = 0 ;i < arrayValue.length ;i ++) {
        content += "window.arr_"+ arrayValue[i] + " = new Array();";
      }
      
      content += "initSelect('"+ itemStr +"','DATA_"+ itemId +"'); ";
      content += "</script>";
    }
    content = YHWorkFlowUtility.addId(content, "DATA_" + itemId , tag);
    return content;
  }

  public String getDate(YHDocFlowFormItem item, List<YHDocFlowFormItem> itemList ,boolean flag) {
    String content = item.getContent();
    String itemStr = "";
    String realValue = item.getValue();
    for (YHDocFlowFormItem tmp : itemList) {
      String title2 = tmp.getTitle();
      String clazz2 = tmp.getClazz();
      int itemId2 = tmp.getItemId();
      if ("DATE".equals(clazz2) || "USER".equals(clazz2)) {
        continue;
      }//注意。。。
      if (title2.equals(realValue)) {
        itemStr = "DATA_" + itemId2;
        break;
      }
    }
    if (flag){
      String dateformat = YHUtility.null2Empty(item.getDateFormat());
      content = "<IMG class=DATE align=absmiddle title=日期控件："
          + realValue
          + " style=\"CURSOR: hand;cursor:pointer\" src=\"img/calendar.gif\" border=0 onclick='tdCalendar(\""
          + itemStr + "\",this,\""+dateformat+"\")'>";
    } else {
      content = "<IMG class=DATE align=absmiddle title=日期控件："
          + realValue
          + " style=\"CURSOR: hand\" src=\"img/calendar.gif\" border=0>";
    }
    return content;
  }
  public String getUserAndDept(YHDocFlowFormItem item, boolean isReadOnly, List<YHDocFlowFormItem> itemList , List<YHDocFlowRunData> frdList , boolean flag ,Connection conn) throws Exception {
    String content = item.getContent();
    String realValue = item.getValue();
    String type = item.getType();
    String itemStr = "";
    YHDocFlowFormItem flowFormItem = null;
    int itemId2 = 0;
    for (YHDocFlowFormItem tmp : itemList) {
      String title2 = tmp.getTitle();
      String clazz2 = tmp.getClazz();
      itemId2 = tmp.getItemId();

      if ("DATE".equals(clazz2) || "USER".equals(clazz2)) {
        continue;
      }//注意。。。
      if (title2.equals(realValue)) {
        itemStr = "DATA_" + itemId2;
        flowFormItem = tmp;
        break;
      }
    }
    if (flag) {
      // 注意type = null时
      //选 择人员
      if ( type == null || "".equals(type) || "0".equals(type)) {
        String userNameStr = "";
        if(flowFormItem != null){
          userNameStr = this.getRealValue(frdList, flowFormItem);
        }
        String userIdStr = "";
        if(!"".equals(userNameStr)){
          userNameStr = YHWorkFlowUtility.getInStr(userNameStr);
          String query = "SELECT SEQ_ID FROM PERSON WHERE  USER_NAME in ("
            + userNameStr + ") ";
          Statement stm = null;
          ResultSet rs = null;
          try {
            stm = conn.createStatement();
            rs = stm.executeQuery(query);
            while(rs.next()){
              userIdStr += rs.getString("SEQ_ID") + ",";
            }
          } catch(Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(stm, rs, null); 
          }
          userIdStr = YHWorkFlowUtility.getOutOfTail(userIdStr);
        }
        content = "<input type=\"hidden\" id=\"USER_" + itemId2 
          + "\" name=\"USER_"+ itemId2 
          +"\" value=\""+ userIdStr 
          +"\"><IMG class=USER align=absmiddle title=部门人员控件："
          +  realValue +" style=\"CURSOR: hand\" src=\"img/user.gif\" border=0 onclick=\"SelectUser('USER_"+ itemId2 +"','"+ itemStr +"')\">";
          
      } else if (type.equals("1")) {
        String deptNameStr = "";
        if(flowFormItem != null){
          deptNameStr = this.getRealValue(frdList, flowFormItem);
        }
        String deptIdStr = "";
        if(!"".equals(deptNameStr)){
          String query = "SELECT SEQ_ID FROM oa_department WHERE " + YHWorkFlowUtility.createFindSql("DEPT_NAME", deptNameStr);;
          Statement stm = null;
          ResultSet rs = null;
          try {
            stm = conn.createStatement();
            rs = stm.executeQuery(query);
            while(rs.next()){
              deptIdStr += rs.getInt("SEQ_ID") + ",";
            }
          } catch(Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(stm, rs, null); 
          }
        }
        content = "<input type=\"hidden\" id=\"DEPT_" + itemId2 
          + "\" name=\"DEPT_"+ itemId2 
          +"\" value=\""+ deptIdStr 
          +"\"><IMG class=USER align=absmiddle title=部门人员控件："
          +  realValue +" style=\"CURSOR: hand\" src=\"img/user.gif\" border=0 onclick=\"SelectDept('DEPT_"+ itemId2 +"','"+ itemStr +"')\">";
      }
      //选择部门
    } else {
      content = "<IMG class=USER align=absmiddle title=部门人员控件："
          + realValue
          + " style=\"CURSOR: hand\" src=\"img/user.gif\" border=0>";
    }
    return content;
  }
  public String getAuto(YHDocFlowFormItem item
      , List<YHDocFlowFormItem> itemList
      ,YHPerson user , boolean isReadOnly , YHDocFlowType ft  , YHDocFlowProcess fp , YHDocFlowRunPrcs frp , String realValue , String content , String ip,Connection conn) throws Exception{
    String tag = item.getTag();
    String datafild = item.getDatafld();
    String autoValue = "";
    Date date = new Date();
    
    String value = item.getValue();
    String title = item.getTitle();
    if ("INPUT".equals(tag)) {
      // 日期转化 不一样
      if ("SYS_DATE".equals(datafild)) {
        autoValue = new SimpleDateFormat("yyyy-MM-dd").format(date);
      } else if ("SYS_DATE_CN".equals(datafild)) {
        autoValue = new SimpleDateFormat("yyyy年M月d日").format(date);
      } else if ("SYS_DATE_CN_SHORT1".equals(datafild)) {
        autoValue = new SimpleDateFormat("yyyy年M月").format(date);
      } else if ("SYS_DATE_CN_SHORT2".equals(datafild)) {
        autoValue = new SimpleDateFormat("M月d日").format(date);
      } else if ("SYS_DATE_CN_SHORT3".equals(datafild)) {
        autoValue = new SimpleDateFormat("yyyy年").format(date);
      } else if ("SYS_DATE_CN_SHORT4".equals(datafild)) {
        autoValue = new SimpleDateFormat("yyyy").format(date);
      } else if ("SYS_TIME".equals(datafild)) {
        autoValue = new SimpleDateFormat("HH:mm:ss").format(date);
      } else if ("SYS_DATETIME".equals(datafild)) {
        autoValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
      } else if ("SYS_WEEK".equals(datafild)) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String[] strs = new String[]{"星期日","星期一","星期二","星期三","星期四","星期五","星期六"}; 
        autoValue = strs[cal.get(Calendar.DAY_OF_WEEK) - 1];
      } else if ("SYS_USERID".equals(datafild)) {
        autoValue = String.valueOf(user.getSeqId());
      } else if ("SYS_DEPTID".equals(datafild)) {
        autoValue = String.valueOf(user.getDeptId());
      } else if ("SYS_USERNAME".equals(datafild)) {
        autoValue = user.getUserName();
      } else if ("SYS_USERPRIV".equals(datafild)) {
        // 取得用户的权限
        String userPriv = user.getUserPriv();
        YHUserPrivLogic logic = new YHUserPrivLogic();
        autoValue = logic.getNameById(Integer.parseInt(userPriv) , conn);
      } else if ("SYS_USERNAME_DATE".equals(datafild)) {
        // 用户时间
        String userName = user.getUserName();
        String sDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        autoValue = userName + sDate;
      } else if ("SYS_YEAR_DEPT".equals(datafild)) {
    	  int deptId = user.getDeptId();
          YHDeptLogic deptLogic = new YHDeptLogic();
          String deptName = deptLogic.getNameById(deptId , conn);
          autoValue =  new SimpleDateFormat("yyyy").format(date) + deptName;
      } else if ("SYS_YEAR_DEPT_AUTONUM".equals(datafild)) {
    	  int deptId = user.getDeptId();
          YHDeptLogic deptLogic = new YHDeptLogic();
          String deptName = deptLogic.getNameById(deptId , conn);
          autoValue =  new SimpleDateFormat("yyyy").format(date) + deptName + String.valueOf(ft.getAutoNum());
      } else if ("SYS_USERNAME_DATETIME".equals(datafild)) {
        String userName = user.getUserName();
        String sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        autoValue = userName + sDate;
      } else if ("SYS_DEPTNAME".equals(datafild)) {
        int deptId = user.getDeptId();
        YHDeptLogic deptLogic = new YHDeptLogic();
        StringBuffer sb = new StringBuffer();
        deptLogic.getDeptNameLong(conn, deptId, sb);
        autoValue = sb.toString();
        if (autoValue.endsWith("/")) {
          autoValue = autoValue.substring(0, autoValue.length() - 1);
        }
      } else if ("SYS_DEPTNAME_SHORT".equals(datafild)) {
        int deptId = user.getDeptId();
        YHDeptLogic deptLogic = new YHDeptLogic();
        autoValue = deptLogic.getNameById(deptId , conn);
      } else if ("SYS_FORMNAME".equals(datafild)) {
        String query = "select FORM_NAME from "+ YHWorkFlowConst.FLOW_FORM_TYPE +" where SEQ_ID=" + ft.getFormSeqId();
        Statement stm = null;
        ResultSet rs = null ;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(query);
          if (rs.next()) {
            autoValue = rs.getString("FORM_NAME");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
      } else if ("SYS_RUNNAME".equals(datafild)) {
        String query = "select RUN_NAME from "+ YHWorkFlowConst.FLOW_RUN +" where RUN_ID=" + frp.getRunId();
        Statement stm = null;
        ResultSet rs = null ;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(query);
          if (rs.next()) {
            autoValue = rs.getString("RUN_NAME");
            autoValue = autoValue.replace("\"", "&quot;");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
      } else if ("SYS_RUNDATE".equals(datafild)) {
        Date beginTime = this.getBeginTime(frp.getRunId(), conn);
        if (beginTime == null) {
          autoValue = "";
        } else {
          autoValue = new SimpleDateFormat("yyyy-MM-dd").format(beginTime);
        }
      } else if ("SYS_RUNDATETIME".equals(datafild)) {
        Date beginTime = this.getBeginTime(frp.getRunId(), conn);
        if (beginTime == null) {
          autoValue = "";
        } else {
          autoValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beginTime);
        }
      } else if ("SYS_RUNID".equals(datafild)) {
        autoValue = String.valueOf(frp.getRunId());
      } else if ("SYS_AUTONUM".equals(datafild)) {
        autoValue = String.valueOf(ft.getAutoNum());
      } else if ("SYS_IP".equals(datafild)) {
        autoValue = ip;
      } else if ("SYS_SQL".equals(datafild)) {
        // 查询数据库
        String  query ="select PRIV_NO from USER_PRIV where SEQ_ID=" + user.getUserPriv();
        Statement stm = null;
        ResultSet rs = null ;
        int loginPrivNo = 0 ;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(query);
          if (rs.next()) {
            loginPrivNo = rs.getInt("PRIV_NO");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
        
        String dataStr = item.getDatasrc();
        if (dataStr != null) {
          dataStr  = dataStr.replaceAll("`", "'");
          dataStr  = dataStr.replaceAll("&#13;&#10;", " ");
          dataStr  = dataStr.replaceAll("\\[SYS_USER_ID\\]", String.valueOf(user.getSeqId()));
          dataStr  = dataStr.replaceAll("\\[SYS_DEPT_ID\\]", String.valueOf(user.getDeptId()));
          dataStr  = dataStr.replaceAll("\\[SYS_PRIV_ID\\]", String.valueOf(user.getUserPriv()));
          dataStr = dataStr.replaceAll("\\[SYS_PRIV_NO\\]",String.valueOf(loginPrivNo));
          dataStr  = dataStr.replaceAll("\\[SYS_RUN_ID\\]", String.valueOf(frp.getRunId()));
          itemValueText = value;
          Statement stm2 = null;
          ResultSet rs2 = null ;
          try {
            stm2 = conn.createStatement();
            rs2 = stm2.executeQuery(dataStr);
            if (rs2.next()) {
              autoValue = rs2.getString(1);
            }
          } catch(Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(stm2, rs2, null); 
          }
        }
      } else if ("SYS_MANAGER1".equals(datafild)) {
        autoValue = this.sysManager(user.getDeptId(), user.getSeqId(), conn);
      } else if ("SYS_MANAGER2".equals(datafild)) {
        YHPrcsRoleUtility pu = new YHPrcsRoleUtility();
        YHORM orm = new YHORM();
        YHDepartment loginDept = (YHDepartment) orm.loadObjSingle(conn, YHDepartment.class, user.getDeptId());
        YHDepartment  department = pu.deptParent(loginDept, 1, conn);
        autoValue = this.sysManager(department.getSeqId(),  user.getSeqId(), conn);
      } else if ("SYS_MANAGER3".equals(datafild)) {
        YHPrcsRoleUtility pu = new YHPrcsRoleUtility();
        YHORM orm = new YHORM();
        YHDepartment loginDept = (YHDepartment) orm.loadObjSingle(conn, YHDepartment.class, user.getDeptId());
        YHDepartment  department = pu.deptParent(loginDept, 0, conn);
        autoValue = this.sysManager(department.getSeqId(),  user.getSeqId(), conn);
      }
      //--- 宏控件单行输入框的自动赋值，数据库为空值且为可写字段时将自动取值，或者是设定为允许在非可写状态下赋值的宏控件(不管是否为空，都自动赋值) ---
      boolean flag = ((realValue == null ||"".equals(realValue)) && !isReadOnly)||(isReadOnly 
          && (fp != null && YHWorkFlowUtility.findId(fp.getPrcsItemAuto(), title)) && "1".equals(frp.getOpFlag()));
      if(flag) {
        if("{宏控件}".equals(value) || "\\\"{宏控件}\\\"".equals(value)){
          value = "\\{宏控件\\}";//加上转义符...为后面的replaceAll
        }
        content = content.replace("value=\\\"{宏控件}\\\"" , "");
        content = content.replaceAll("value=" + value, "");
        content = content.replaceAll("value='" + value + "'", "");
        content = content.replaceAll("value=''" + value, "");
        autoValue = YHUtility.null2Empty(autoValue);
        autoValue = autoValue.replace("$", "\\$");
        autoValue = autoValue.replace("\"", "&quot;");
        content = content.replaceAll("<" + tag, "<" + tag
            + " value=\"" + autoValue + "\"");
        String tag1 = tag.toLowerCase();
        content = content.replaceAll("<" + tag1, "<" + tag1
            + " value=\"" + autoValue + "\"");
      }
    } else {
      content = this.getAutoSelect(item, isReadOnly, itemList, ft, fp, frp.getRunId() , user , realValue, conn);
    }
    
    return content;
  }
  public String getAutoSelect(YHDocFlowFormItem item
      , boolean isReadOnly
      , List<YHDocFlowFormItem> itemList 
      , YHDocFlowType ft 
      , YHDocFlowProcess fp  
      , int runId 
      ,YHPerson user
      , String value
      , Connection conn) throws Exception{
    String datafild = item.getDatafld();
    String content = item.getContent();
    
    String autoValue = "<option value=\"\"";
    if (value == null || "".equals(value)) {
      value = "";
      autoValue += " selected";
    }
    autoValue += "></option>\n";
    itemValueText = "";
    if ("SYS_LIST_DEPT".equals(datafild)) {
      StringBuffer sb = new StringBuffer();
      this.getDeptTree(0, sb, 0 , value, conn);
      autoValue += sb.toString();
      if (value != null && !"".equals(value)) {
        String queryAuto = "select " 
          + " DEPT_NAME " 
          + " from  oa_department where "
          + " SEQ_ID = " + value;
        Statement stm = null;
        ResultSet rs = null ;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(queryAuto);
          if (rs.next()) {
            itemValueText = rs.getString("DEPT_NAME");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
      }
    } else if ("SYS_LIST_USER".equals(datafild)) {
      String queryAuto = "select " 
        + " PERSON.SEQ_ID " 
        + ", USER_NAME " 
        + " from  PERSON , USER_PRIV where "
        + " PERSON.USER_PRIV = USER_PRIV.SEQ_ID "
        + " order by PRIV_NO , USER_NO , USER_NAME ";
      Statement stm = null;
      ResultSet rs = null ;
      try {
        stm = conn.createStatement();
        rs = stm.executeQuery(queryAuto);
        while (rs.next()) {
          int userId = rs.getInt("SEQ_ID");
          String userName = rs.getString("USER_NAME");
          autoValue += "<option value ='" + userId + "' ";
          String sUserId = String.valueOf(userId);
          if (value.equals(sUserId)) {
            autoValue += " selected ";
            itemValueText = userName;
          }
          autoValue += ">" + userName + "</option>";
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null); 
      }
    } else if ("SYS_LIST_PRIV".equals(datafild)) {
      String queryAuto = "SELECT SEQ_ID " 
        + " ,PRIV_NAME " 
        + "  from USER_PRIV  " 
        + " order by PRIV_NO";
      Statement stm = null;
      ResultSet rs = null ;
      try {
        stm = conn.createStatement();
        rs = stm.executeQuery(queryAuto);
        while (rs.next()) {
          int userPriv = rs.getInt("SEQ_ID");
          String privsName = rs.getString("PRIV_NAME");
          autoValue += "<option value ='" + userPriv + "' ";
          String sUserId = String.valueOf(userPriv);
          if (value.equals(sUserId)) {
            autoValue += " selected ";
            itemValueText = privsName;
          }
          autoValue += ">" + privsName + "</option>";
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null); 
      }
    } else if ("SYS_LIST_PRCSUSER1".equals(datafild)) {
      String queryAuto  = "select PRCS_USER " 
        + " ,PRCS_DEPT " 
        + " ,PRCS_PRIV  " 
        + " from "+ YHWorkFlowConst.FLOW_PROCESS +" where  " 
        + " FLOW_SEQ_ID="+ ft.getSeqId() 
        + " order by PRCS_ID";
      String prcsUser = "";
      String prcsDept = "";
      String prcsPriv = "";
      String prcsDeptAll = "";
      Statement stm = null;
      ResultSet rs = null ;
      try {
        stm = conn.createStatement();
        rs = stm.executeQuery(queryAuto);
        while (rs.next()) {
          String prcsUserTmp = rs.getString("PRCS_USER");
          String prcsDeptTmp = rs.getString("PRCS_DEPT");
          String prcsPrivTmp = rs.getString("PRCS_PRIV");
          if (prcsUserTmp != null && !"".equals(prcsUserTmp)) {
            prcsUser += prcsUserTmp + ",";
          }
          if (prcsDeptTmp != null 
              && !"".equals(prcsDeptTmp)
              && !"ALL_DEPT".equals(prcsDept)) {
            prcsDept += prcsDeptTmp;
          } else if ("ALL_DEPT".equals(prcsDept)) {
            prcsDeptAll = "ALL_DEPT";
          }
          if (prcsPrivTmp != null && !"".equals(prcsPrivTmp)) {
            prcsPriv += prcsPrivTmp;
          }
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null); 
      }
      queryAuto = "SELECT PERSON.SEQ_ID " 
        + " ,USER_NAME  " 
        + " from PERSON ,USER_PRIV where  " 
        + " PERSON.USER_PRIV=USER_PRIV.SEQ_ID  " 
        + " AND NOT_LOGIN='0' AND ( ";
      if (!"".equals(prcsDept) 
          && !"ALL_DEPT".equals(prcsDeptAll))  {
        queryAuto += YHWorkFlowUtility.createFindSql("PERSON.DEPT_ID", prcsDept);
      } else if ("ALL_DEPT".equals(prcsDeptAll)) {
        queryAuto += "1=1";
      } else {
        queryAuto += "1=0";
      }
      if (!"".equals(prcsUser)) {
        queryAuto += " or " + YHWorkFlowUtility.createFindSql("PERSON.SEQ_ID", prcsUser);
      }
      if (!"".equals(prcsPriv)) {
        queryAuto += " or " + YHWorkFlowUtility.createFindSql("PERSON.USER_PRIV", prcsPriv);
      }
      queryAuto += ") order by PRIV_NO , USER_NO , USER_NAME";
      
      Statement stm2 = null;
      ResultSet rs2 = null ;
      try {
        stm2 = conn.createStatement();
        rs2 = stm2.executeQuery(queryAuto);
        while (rs2.next()) {
          int userId = rs2.getInt("SEQ_ID");
          String userName = rs2.getString("USER_NAME");
          autoValue += "<option value ='" + userId + "' ";
          String sUserId = String.valueOf(userId);
          if (value.equals(sUserId)) {
            autoValue += " selected ";
            itemValueText = userName;
          }
          autoValue += ">" + userName + "</option>";
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
    } else if ("SYS_LIST_PRCSUSER2".equals(datafild)) {
      String prcsUser = "";
      String prcsDept = "";
      String prcsPriv = "";
      
      if (fp != null ) {
        if (fp.getPrcsUser() != null) {
          prcsUser = fp.getPrcsUser();
        }
        if (fp.getPrcsDept() != null 
            && !"0".equals(fp.getPrcsDept())) {
          prcsDept = fp.getPrcsDept();
        } else if ("0".equals(fp.getPrcsDept())) {
          prcsDept = "ALL_DEPT";
        }
        if (fp.getPrcsPriv() != null 
            && !"".equals(fp.getPrcsPriv())) {
          prcsPriv += fp.getPrcsPriv();
        }
      }
      if (value != null 
          && !"".equals(value) ) {
        prcsUser += value;
      }
      
      String queryAuto = "SELECT PERSON.SEQ_ID " 
        + " ,USER_NAME  " 
        + " from PERSON ,USER_PRIV where  " 
        + " PERSON.USER_PRIV=USER_PRIV.SEQ_ID  " 
        + " AND NOT_LOGIN='0' AND ( ";
      if (!"".equals(prcsDept) 
          && !"ALL_DEPT".equals(prcsDept))  {
        queryAuto += YHWorkFlowUtility.createFindSql("PERSON.DEPT_ID", prcsDept);
      } else if ("ALL_DEPT".equals(prcsDept)) {
        queryAuto += " 1=1 ";
      } else {
        queryAuto += " 1=0 ";
      }
      if (!"".equals(prcsUser)) {
        queryAuto += " or " + YHWorkFlowUtility.createFindSql("PERSON.SEQ_ID", prcsUser);
      }
      if (!"".equals(prcsPriv)) {
        queryAuto += " or " + YHWorkFlowUtility.createFindSql("PERSON.USER_PRIV", prcsPriv);
      }
      queryAuto += ") order by PRIV_NO , USER_NO , USER_NAME";
      
      Statement stm2 = null;
      ResultSet rs2 = null ;
      try {
        stm2 = conn.createStatement();
        rs2 = stm2.executeQuery(queryAuto);
        while (rs2.next()) {
          int userId = rs2.getInt("SEQ_ID");
          String userName = rs2.getString("USER_NAME");
          autoValue += "<option value ='" + userId + "' ";
          String sUserId = String.valueOf(userId);
          if (value.equals(sUserId)) {
            autoValue += " selected ";
            itemValueText = userName;
          }
          autoValue += ">" + userName + "</option>";
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
    } else if ("SYS_LIST_SQL".equals(datafild)) {
      String dataStr = item.getDatasrc();
      content = content.replaceAll(dataStr , "");
      dataStr  = dataStr.replaceAll("`", "'");
      dataStr  = dataStr.replaceAll("&#13;&#10;", " ");
      dataStr  = dataStr.replaceAll("\\[SYS_USER_ID\\]", String.valueOf(user.getSeqId()));
      dataStr  = dataStr.replaceAll("\\[SYS_DEPT_ID\\]", String.valueOf(user.getDeptId()));
      dataStr  = dataStr.replaceAll("\\[SYS_PRIV_ID\\]", String.valueOf(user.getUserPriv()));
      //$EDATASRC=str_replace("[SYS_PRIV_NO]",$LOGIN_USER_PRIV_NO,$EDATASRC);
     // dataStr  = dataStr.replaceAll("[SYS_PRIV_NO]", String.valueOf(user.getUserPrivOther()));
      dataStr  = dataStr.replaceAll("\\[SYS_RUN_ID\\]", String.valueOf(runId));
      itemValueText = value;
      Statement stm2 = null;
      ResultSet rs2 = null ;
      try {
        stm2 = conn.createStatement();
        rs2 = stm2.executeQuery(dataStr);
        while (rs2.next()) {
          String autoValueSql = rs2.getString(1);
          autoValue += "<option value ='" + autoValueSql + "' ";
          if (value.equals(autoValueSql)) {
            autoValue += " selected ";
          }
          autoValue += ">" + autoValueSql + "</option>";
        }
      } catch(Exception ex) {
        autoValue +="<option value =''>" + ex.toString() + "</option>";
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
    } else if ("SYS_LIST_MANAGER1".equals(datafild)) {
      int tmpDeptId = user.getDeptId();
      autoValue += this.sysListManager(tmpDeptId, user.getSeqId(), value, conn);
    } else if ("SYS_LIST_MANAGER2".equals(datafild)) {
      YHPrcsRoleUtility pu = new YHPrcsRoleUtility();
      YHORM orm = new YHORM();
      YHDepartment loginDept = (YHDepartment) orm.loadObjSingle(conn, YHDepartment.class, user.getDeptId());
      YHDepartment  department = pu.deptParent(loginDept, 1, conn);
      autoValue += this.sysListManager(department.getSeqId(), user.getSeqId(), value, conn);
    } else if ("SYS_LIST_MANAGER3".equals(datafild)) {
      YHPrcsRoleUtility pu = new YHPrcsRoleUtility();
      YHORM orm = new YHORM();
      YHDepartment loginDept = (YHDepartment) orm.loadObjSingle(conn, YHDepartment.class, user.getDeptId());
      YHDepartment  department = pu.deptParent(loginDept, 0, conn);
      autoValue += this.sysListManager(department.getSeqId(), user.getSeqId(), value, conn);
    }
    // $ELEMENT_OUT=substr($ELEMENT_OUT,0,strpos($ELEMENT_OUT,">")+1).$AUTO_VALUE."</SELECT>";
    content = content.substring(0, content.indexOf(">") + 1)
        + autoValue + "</SELECT>";
    content = YHWorkFlowUtility.addId(content, "DATA_" + item.getItemId()  , item.getTag());
    return content;
  }
  public String getListView(YHDocFlowFormItem item, boolean isReadOnly, String realValue, List<YHDocFlowFormItem> itemList ,YHPerson user){
    String content = item.getContent();
    int itemId = item.getItemId();
    String lvId = "DATA_" + itemId;
    String lvTbId = "LV_" + itemId;
    String lvTitle = item.getLvTitle();
    if ( lvTitle == null ) {
      lvTitle = "";
    }
    String lvSize = item.getLvSize();
    if ( lvSize == null ) {
      lvSize = "";
    }
    String lvSum = item.getLvSum();
    if ( lvSum == null ) {
      lvSum = "";
    }
    String lvCal = item.getLvCal();
    if ( lvCal == null ) {
      lvCal = "";
    }
    
    content = "<TABLE id='"
        + lvTbId
        + "' class='LIST_VIEW' style='border-collapse:collapse' border=1 cellspacing=0 cellpadding=2 FormData='"
        + lvSize
        + "'><TR "
        + "style='font-weight:bold;font-size:14px;' class='LIST_VIEW_HEADER'>\n";
    String[] myArray = lvTitle.split("`");
    for (String tmp : myArray) {
      content += "<TD nowrap>" + tmp + "</TD>\n";
    }
    content += "<TD>操作</TD></TR></TABLE>\n";
    
    if (!isReadOnly) {
      int readOnly = 0;
      content += "<input type=button class='SmallButtonW' value=新增  onclick=\"tbAddNew('"
          + lvTbId + "'," + readOnly + ",'','" + lvSum + "','" + lvCal
          + "')\">\n";
      content += "<input type=button class='SmallButtonW' value=计算  onclick=\"tbCal('"
          + lvTbId + "','" + lvCal + "')\">\n";
    }
    content += "<input type=hidden name=" + lvId + " id=" + lvId +">\n";
    content += "<SCRIPT>\n";
    
    realValue = realValue.replace("&#13;", "");
    myArray = realValue.split("&#10;");
    for (String tmp : myArray) {
      // /注意
      if (!"".equals(tmp)) {
        content += "tbAddNew('"+ lvTbId + "'," + isReadOnly
        +",'"+ tmp +"','"+ lvSum +"','"+lvCal+"');";
      }
    }
    content += "setInterval(\"tbCal('" + lvTbId +  "','"+ lvCal  +"')\",1000);";
    content += "</SCRIPT>";
    return content;
  }
  public String getSign(YHDocFlowFormItem item, boolean isReadOnly, String realValue, List<YHDocFlowFormItem> itemList){
    // TODO Auto-generated method stub
    String content = "";
    int itemId = item.getItemId();
    String signId = "DATA_" + itemId;
    String itemCheck = "";
    String signCheck = "";
    if (item.getDatafld() != null) {
      signCheck =  item.getDatafld();
    }
    if (!signCheck.endsWith(",")) {
      signCheck += ",";
    }
    for (YHDocFlowFormItem item2 : itemList) {
      String title2 = item2.getTitle();
      String clazz2 = item2.getClazz();
      int itemId2 = item2.getItemId();
      if ("DATE".equals(clazz2) || "USER".equals(clazz2)) {
        continue;
      }
      if (YHWorkFlowUtility.findId(signCheck, title2)) {
        itemCheck += "DATA_" + itemId2 + ",";
      }
    }
    
    signCheckStr += "\"" + signId + "\":\"" + itemCheck + "\",";
    signObject += signId + ",";

    content += "<div class='websign' id=SIGN_POS_" + signId + ">&nbsp;";
    if (!isReadOnly) {
      content += "<input type=button class='SmallButtonW' value=盖章 onclick=\"addSeal('"
          + signId + "')\">";
      content += "<input type=button class='SmallButtonW' value=手写 onclick=\"handWrite('"
          + signId + "')\">";
    }
    content += "<input type=hidden name=" + signId + "  id=" + signId + " value='" + realValue
        + "'>";
    content += "</div>";
    isHaveSign = true;
    return content;
  }
  public String sysManager(int tmpDeptId , int loginUserId ,Connection conn) throws Exception{
    YHORM orm = new YHORM();
    YHDepartment dept = (YHDepartment) orm.loadObjSingle(conn, YHDepartment.class, tmpDeptId);
    String autoValue = "";
    String manager = "";
    if(dept != null){
      manager = dept.getManager();
    }
    if(manager != null && !"".equals(manager.trim())){
      String[] aManager = manager.split(",");
      if (YHUtility.isInteger(aManager[0])) {
        YHWorkFlowUtility ut = new YHWorkFlowUtility();
        autoValue = ut.getUserNameById(Integer.parseInt(aManager[0]), conn);
      }
    }else{
      String query = "SELECT PERSON.SEQ_ID,USER_NAME,USER_PRIV.SEQ_ID from PERSON,USER_PRIV where PERSON.USER_PRIV=USER_PRIV.SEQ_ID and DEPT_ID='"
        + tmpDeptId + "' and PERSON.SEQ_ID!=" + loginUserId + " order by PRIV_NO,USER_NO,USER_NAME";
      Statement stm2 = null;
      ResultSet rs2 = null ;
      try {
        stm2 = conn.createStatement();
        rs2 = stm2.executeQuery(query);
        if(rs2.next()){
          autoValue = rs2.getString("USER_NAME");
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
    }
    return autoValue;
  }
  public String sysListManager(int tmpDeptId , int loginUserId , String selectValue , Connection conn) throws Exception{
    YHORM orm = new  YHORM();
    YHDepartment dept = (YHDepartment) orm.loadObjSingle(conn, YHDepartment.class, tmpDeptId);
    String autoValue = "";
    String manager = "";
    if (dept != null) {
      manager = dept.getManager();
      if (manager == null) {
        manager = "";
      }
    }
    if (!"".equals(manager)) {
      String[] aManager = manager.split(",");
      String query = "SELECT SEQ_ID,USER_NAME from PERSON where 1<>1 ";
      for(int i = 0 ;i < aManager.length ;i ++){
        if(YHUtility.isInteger(aManager[i])){
           query += " OR SEQ_ID = " + aManager[i];
        }
      }
      Statement stm2 = null;
      ResultSet rs2 = null ;
      try {
        stm2 = conn.createStatement();
        rs2 = stm2.executeQuery(query);
        while (rs2.next()) {
          String userName = rs2.getString("USER_NAME");
          String userId = String.valueOf(rs2.getInt("SEQ_ID"));
          
          autoValue += "<option value='" + userId + "'";
          if (userId.equals(selectValue)) {
            itemValueText = userName;
            autoValue += " selected ";
          }
          autoValue += ">" + userName + "</option>\n";
        }
      } catch(Exception ex) {
        autoValue += "<option value=''></option>\n";
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
    }else{
      String query = "SELECT PERSON.SEQ_ID,USER_NAME from PERSON,USER_PRIV where PERSON.USER_PRIV=USER_PRIV.SEQ_ID and DEPT_ID='"
        + tmpDeptId + "' and PERSON.SEQ_ID != " + loginUserId + " order by PRIV_NO,USER_NO,USER_NAME";
      Statement stm2 = null;
      ResultSet rs2 = null ;
      try {
        stm2 = conn.createStatement();
        rs2 = stm2.executeQuery(query);
        while (rs2.next()) {
          String userName = rs2.getString("USER_NAME");
          int userId = rs2.getInt("SEQ_ID");
          autoValue += "<option value='" + userId + "'";
          if(selectValue != null && selectValue.equals(autoValue)){
            itemValueText = userName;
            autoValue += " selected";
          }
          autoValue += ">" + userName + "</option>\n";
        }
      } catch(Exception ex) {
        autoValue += "<option value=''></option>\n";
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
    }
    return autoValue;
  }
  public String getCalc(String value , List<YHDocFlowFormItem> itemList , int itemId , String content){
    String eCalc = this.calculate(value, itemList);
    content += "<script>"
      + " window.calc_"+ itemId +" = function(){"
      + "   var myvalue= eval(\""+ eCalc +"\");"
      + "   if (myvalue==Infinity) {"
      + "    try{"
      + "      document.getElementById('DATA_"+ itemId +"').value=\"无效结果\";"
      + "    } catch(e) {document.getElementsByName('DATA_"+ itemId +"')[0].value=\"无效结果\";}"
      + "   } else if(!isNaN(myvalue)) {"
      + "       var prec = '';try{ prec = document.getElementById('DATA_"+ itemId +"').getAttribute('prec');} "
      + "          catch(e) {prec = document.getElementsByName('DATA_"+ itemId +"')[0].getAttribute('prec');}"
      + "     var vPrec;"
      + "     if(!prec) "
      + "       vPrec=10000;"
      + "     else "
      + "       vPrec=Math.pow(10,prec);"
      + "     var result = new Number(parseFloat(Math.round(myvalue*vPrec)/vPrec));"
      + "    try{"
      + "      document.getElementById('DATA_"+ itemId +"').value=result.toFixed(prec);"
      + "    } catch(e) {document.getElementsByName('DATA_"+ itemId +"')[0].value=result.toFixed(prec);}"
      + "   }else {"
      + "    try{"
      + "      document.getElementById('DATA_"+ itemId +"').value=myvalue;"
      + "    } catch(e) {document.getElementsByName('DATA_"+ itemId +"')[0].value=myvalue;}"
      + "   } "
      + "   setTimeout(\"calc_"+ itemId +"()\",1000);"
      + " };"
      + " setTimeout(\"window.calc_"+ itemId +"()\",3000);"
      + " </script>";
    return content;
  }
  public String getRealValue(List<YHDocFlowRunData> frdList, YHDocFlowFormItem item){
    String realValue = "";
    for (YHDocFlowRunData flowRunData : frdList) {
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
  
  public String calculate (String value  , List<YHDocFlowFormItem> itemList ) {
    if ("".equals(value)) {
      return "";
    }
    Map<String , String> map = new HashMap();
    map.put("ABS\\(", "calcABS(");
    map.put("RMB\\(", "calcRMB(");
    map.put("MAX\\(", "calcMAX(");
    map.put("MIN\\(", "calcMIN(");
    map.put("DAY\\(", "calcDAY(");
    map.put("HOUR\\(", "calcHOUR(");
    map.put("AVG\\(", "calcAVG(");
    map.put("DATE\\(", "calcDATE(");
    //没有list
    //map.put("LIST(", "calc_abs(");
    for(String key : map.keySet()){
      String mapValue = map.get(key);
      value = value.replaceAll(key, mapValue);
    }
    //--- 兼容非运算公式情况 ---
    boolean flag = false;
  //--- 兼容非运算公式情况 ---
    //if(preg_match("/[\+|\-|\*|\/|,]+/",$VALUE)==0)
       //$flag = true;
    //没有运算公式
    if (!Pattern.matches(".+[\\+|\\-|\\*|\\/|,].+", value)) {
      flag = true;
    }
    //--- 替换控件名称 ---
    for (YHDocFlowFormItem tmp : itemList) {
      String title2 = tmp.getTitle();
      int itemId2 = tmp.getItemId();
      //没有运算公式
      if (flag  && value.equals(title2)) {
        value = "calcGetVal('DATA_" + itemId2 + "')" ;
        break ;
      } else {
        if(title2.indexOf("/") != -1){
          title2 = title2.replaceAll("/", "\\\\/");
        }
        value = YHRegexpUtility.replaceTitle(value, title2, "calcGetVal('DATA_" + itemId2 + "')");
      }
    }
    return value; 
  }
  public void getDeptTree(int deptId , StringBuffer sb , int level , String value, Connection conn) throws Exception{
    //首选分级，然后记录级数，是否为最后一个。。。
    YHDeptLogic logic = new YHDeptLogic();
    List<YHDepartment> list = logic.getDeptByParentId(deptId , conn);
    for(int i = 0 ;i < list.size() ;i ++){
      String flag = "├";
      if(i == list.size() - 1 ){
        flag = "└";
      }
      String tmp = "";
      for(int j = 0 ;j < level ; j++){
        tmp += "│";
      }
      flag = tmp + flag;
      YHDepartment dp = list.get(i);
      String dept = String.valueOf(dp.getSeqId());
      sb.append("<option value='" + dept  + "' " );
      if (dept.equals(value)) {
        sb.append(" selected ");
      }
      sb.append(">");
      sb.append(flag + dp.getDeptName());
      sb.append("</option>");
      this.getDeptTree(dp.getSeqId(), sb, level + 1 , value , conn);
    }
  }
  public Timestamp getBeginTime(int runId , Connection conn) throws Exception {
    String query = "select BEGIN_TIME from "+ YHWorkFlowConst.FLOW_RUN +" where RUN_ID=" + runId;
    Timestamp beginTime = null;
    Statement stm = null;
    ResultSet rs = null ;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        beginTime = rs.getTimestamp("BEGIN_TIME");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return beginTime;
  }
  
  public static void main(String[] args) throws Exception{
  }
}
