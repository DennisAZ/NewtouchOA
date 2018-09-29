package yh.core.funcs.workflow.praser;

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
import java.util.regex.Pattern;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.funcs.workflow.data.YHFlowFormItem;
import yh.core.funcs.workflow.data.YHFlowProcess;
import yh.core.funcs.workflow.data.YHFlowRunPrcs;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.util.YHConfigLogic;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHRegexpUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
/**
 * 表单预览的解析
 * @author Think
 *
 */
public class YHPraseData2FormView {
 private String itemValueText = "";
 private String signObject = "";
 private boolean isHaveSign = false;
 public String parseForm(YHPerson user , String modelShort ,
      List<YHFlowFormItem> itemList ,String ip , Connection conn ) throws Exception {
    for (YHFlowFormItem item : itemList) {
      String tag = item.getTag();
      String value = item.getValue();
      int itemId = item.getItemId();
      String content = item.getContent();
      String clazz = item.getClazz();
      String name = item.getName();
      String tag1 = tag.toLowerCase();
      if (content != null)
        content = content.replace("<" + tag1, "<" + tag);
      item.setContent(content);
      if ("RADIO".equals(clazz)
          && "IMG".equals(tag)) {
        content = this.getRadio(item, itemList);
      }
      if ("INPUT".equals(tag)){
        if (content.indexOf("type=checkbox") == -1) {
          //隐藏属性           
            String hidden = item.getHidden();
            String hiddenStr = "";
            if("1".equals(hidden))
              hiddenStr = " type=\"hidden\" ";
            content = content.replaceAll("<" + tag, "<" + tag +  hiddenStr );
        }
      }
      if ("INPUT".equals(tag) 
          || "TEXTAREA".equals(tag)
          || "SELECT".equals(tag)) {
        content = YHWorkFlowUtility.addId(content, "DATA_" + itemId  , tag);
      } 
      if ("DATE".equals(clazz)) {
        content = this.getDate(item, itemList);
      }
      if ("USER".equals(clazz)) {
        content = this.getUserAndDept(item, itemList,conn);
        // 计算控件
      } else if ("SELECT".equals(tag) && !"AUTO".equals(clazz)) {
        content = this.getSelect(item, itemList);
      } else if ("CALC".equals(clazz)) {
        content = this.getCalc(value, itemList, itemId , content);
      } else if ("AUTO".equals(clazz)) {
        content = this.getAuto(item, itemList, user  , content ,ip ,conn);
      } else if ("LIST_VIEW".equals(clazz)) {
        content = this.getListView(item, itemList, user);
      } else if ("SIGN".equals(clazz)) {
        content = this.getSign(item, itemList);
      } else if ("DATA".equals(clazz)) {
        content = this.getData(item, itemList);
      } else if ("MODULE".equals(clazz)) {
        content = this.getModule(item);
      } else if ("FETCH".equals(clazz)) {
        content = this.getFetch(item, itemList);
      }else if ("FLOWFETCH".equals(clazz)) {
        content = this.getFlowFetch(item, itemList);
      }
      content = content.replace("$", "\\$");
     
      modelShort = modelShort.replaceAll("\\{" + name + "\\}", content);
    }
    //处理签章控件
    if ( isHaveSign) {
      modelShort += "<script>";
      modelShort += "sign_str = \"" + signObject + "\";";
      YHConfigLogic logic = new YHConfigLogic();
      String sealForm = logic.getSysPar("SEAL_FROM", conn);
      if (sealForm == null  || "".equals(sealForm)) {
        sealForm = "1";
      }
      modelShort += "sealForm = " + sealForm + ";"; 
      modelShort += "</script>";
    }
    return modelShort;
  }
  private String getRadio(YHFlowFormItem item, List<YHFlowFormItem> itemList) {
  // TODO Auto-generated method stub
    String radioField = YHUtility.null2Empty(item.getRadioField());
    String radioCheck = YHUtility.null2Empty(item.getRadioCheck());
    String[] radioArray = radioField.split("`");
    String name = item.getName();
    String elOut = "";
    for (String s : radioArray) {
      String checked = "";
      if (s.equals(radioCheck)) {
        checked = "checked";
      }
      elOut += "<input type=\"radio\" name=\""+name+"\" value=\""+s+"\" "+checked+"><label>"+ s +"</label>&nbsp;";
    }
    return elOut;
}
  private String getFetch(YHFlowFormItem item, List<YHFlowFormItem> itemList) {
  // TODO Auto-generated method stub
    String dataControl = YHUtility.null2Empty(item.getDataControl());
    String myArray[] = dataControl.split("`");
    String itemStr = "";
    for (String ss : myArray) {
      int itemId1 = 0;
      int findFlag = 0 ;
      for (YHFlowFormItem tmp : itemList) {
    	  /*ljs 20121224 修改逻辑为getName*/
//        String title = YHUtility.null2Empty(tmp.getTitle());
    	  String title = YHUtility.null2Empty(tmp.getName());
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
   content = content.replace("<" + tag, "<INPUT type=text size=10 id=\""+name+"\" value=\"输入流水号..\" onclick=\"javascript:this.value=''\"><" + tag + " type=\"button\" onclick=data_fetch(this,document.getElementById(\""+name+"\").value,\""+ itemStr +"\") ");
   String tag1 = tag.toLowerCase();
   content = content.replace("<" + tag1, "<INPUT type=text size=10 id=\""+name+"\" value=\"输入流水号..\" onclick=\"javascript:this.value=''\"><" + tag + " type=\"button\" onclick=data_fetch(this,document.getElementById(\""+name+"\").value,\""+ itemStr +"\") ");
   return content;
}
  private String getFlowFetch(YHFlowFormItem item, List<YHFlowFormItem> itemList) {
    // TODO Auto-generated method stub
    String dataControl = YHUtility.null2Empty(item.getDataControl());
    String myArray[] = dataControl.split("`");
    String itemStr = "";
    for (String ss : myArray) {
      int itemId1 = 0;
      int findFlag = 0 ;
      for (YHFlowFormItem tmp : itemList) {
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
    }
    String content = item.getContent();
    String tag = item.getTag();
    String tag1 = tag.toLowerCase();
    content = content.replace("<" + tag1, "<" + tag);
    content = content.replace("<" + tag, "<" + tag + " type=\"button\" onclick=flow_data_picker(this,\""+ itemStr +"\") ");
    return content;
  }
  private String getModule(YHFlowFormItem item) {
  // TODO Auto-generated method stub
    String module = item.getValue();
    String content = item.getContent();
    int itemId = item.getItemId();
    String divId = "module-" + module  + "-DATA_"+ itemId;
    content = "<div id=\""+ divId +"\">"+ content +"</div>";
    content += "<script>";
    content += "editModuleContent(\""+ module +"\" , \""+ divId +"\")";
    content += "</script>";
    return content;
  }
  //设置只读
  public String setReadOnly(YHFlowFormItem item  , String content , YHFlowProcess fp,
      YHFlowRunPrcs frp, YHFlowType ft  , String realValue) {
    // TODO Auto-generated method stub
    String title = item.getTitle();
    String clazz = item.getClazz();
    int itemId = item.getItemId();
    String tag  = item.getTag();
    //注意
    String readOnlyStr = "";
    // 不允许在不可写情况下自动赋值的宏控件，且是计算控件。
    boolean flag = true;
    if (fp != null) {
      flag = !YHWorkFlowUtility.findId(fp.getPrcsItemAuto(), title);
    }
    if ( flag && "CALC".equals(clazz)) {
      readOnlyStr += itemId + ",";
    }
    //是checkbox
    if (content.indexOf("type=checkbox") != -1
        || content.indexOf("type=\"checkbox\"") != -1
        || content.indexOf("type=\\\"checkbox\\\"") != -1    
    ) {
      if (content.indexOf(" CHECKED") != -1) {//是选中
        content = content.replaceAll("<" + tag, "<" + tag
            + " readonly onclick=\"this.checked=1\"; class=BigStatic");
      } else {//不是选中
        content = content.replaceAll("<" + tag, "<" + tag
            + " readonly onclick=\"this.checked=0\"; class=BigStatic");
      }
    }else if (!"LIST_VIEW".equals(clazz) && !"SIGN".equals(clazz)) {
      content = content.replaceAll("<" + tag, "<" + tag
          + " readOnly class=BigStatic ");
    }
    //如果是select
    if ("SELECT".equals(tag)) {
      if (!"AUTO".equals(clazz)) {
        // 注意
        content= content.substring(0, content.indexOf(">") + 1) + "<OPTION value="+ realValue+">"+  realValue +"</OPTION></SELECT>"; 
      }else{
        content = content.substring(0, content.indexOf(">") + 1) + "<OPTION value="+ realValue+">"+ itemValueText +"</OPTION></SELECT>";
      }
    } else {
      //可输入项，突出输入颜色      if (("SELECT".equals(tag) || "INPUT".equals(tag) || "TEXTAREA"
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
  public String getData(YHFlowFormItem item, List<YHFlowFormItem> itemList){
    // TODO Auto-generated method stub
    String dataControl = YHUtility.null2Empty(item.getDataControl());
    String dataType = YHUtility.null2Empty(item.getDataType());
    String myArray[] = dataControl.split("`");
    String itemStr = "";
    for (String ss : myArray) {
      int itemId1 = 0;
      int findFlag = 0 ;
      for (YHFlowFormItem tmp : itemList) {
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
  }

  public String getInput(YHFlowFormItem item,
      String realValue) {
    // <input title="ddd" align="center" style="text-align: center"
    // name="DATA_1" value="dddda" type="text" />
    String content = item.getContent();
    String tag = item.getTag();
    int id = item.getItemId();
    //原来的默认的值
    String value = item.getValue() == null ? "": item.getValue();
    if("{宏控件}".equals(value)){
      value = "\\{宏控件\\}";//加上转义符...为后面的replaceAll
    }
    realValue = realValue == null ? "": realValue;
    //如果不是checkbox
    if (content.indexOf("type=checkbox") == -1
        && content.indexOf("type=\"checkbox\"") == -1
        && content.indexOf("type=\\\"checkbox\\\"") == -1    
    ) {
    //隐藏属性           
      String hidden = item.getHidden();
      String hiddenStr = "";
      if("1".equals(hidden))
        hiddenStr = " type=\"hidden\" ";
      content = content.replaceAll("value=" + value, "");
      content = content.replaceAll("<" + tag, "<" + tag + " value=\""
          + realValue + "\" " + hiddenStr );
      content = content.replaceAll("<" + tag, "<" + tag + " id=\"DATA_"
          + id + "\"");
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
    content = YHWorkFlowUtility.addId(content, "DATA_" + id , tag);
    return content;
  }
  
  public String getTextArea(YHFlowFormItem item,
      String realValue) {
    String content = item.getContent();
    String tag = item.getTag();
    int id = item.getItemId();
    String value = item.getValue() == null ? "" : item.getValue();
    content = content.replaceAll(">" + value + "<", ">" + realValue + "<");
    content = content.replaceAll("<" + tag, "<" + tag + " id=\"DATA_"
        + id + "\"");
    return content;
  }

  public String getSelect(YHFlowFormItem item, List<YHFlowFormItem> itemList) {
    String content = item.getContent();
    int itemId = item.getItemId();
    String tag = item.getTag();
    
    String child = item.getChild();
   
    String itemStr = "";
    // 有子select,,child 不为空,先不考虑
    if (child != null && !"".equals(child)) {
      int count = 0 ;
      for (YHFlowFormItem tmp : itemList) {
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
      content = content.replaceAll("<" + tag.toLowerCase(),
          "<" + tag.toLowerCase() + " onchange=\"selectChange(this.value,'" + itemStr + "')\"");
      content = content.replaceAll("<" + tag,
          "<" + tag + " onchange=\"selectChange(this.value,'" + itemStr + "')\"");
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
    content = YHWorkFlowUtility.addId(content, "DATA_" + itemId  , tag);
    return content;
  }

  public String getDate(YHFlowFormItem item, List<YHFlowFormItem> itemList ) {
    String content = item.getContent();
    String itemStr = "";
    String itemId = "OTHER_" + item.getItemId();
    String realValue = item.getValue();
    for (YHFlowFormItem tmp : itemList) {
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
    String dateFormat = YHUtility.null2Empty(item.getDateFormat());
    content = "<IMG class=DATE align=absmiddle title=日期控件："
          + realValue
          + " id="+ itemId +" style=\"CURSOR: hand;cursor:pointer\" src=\"image/calendar.gif\" border=0>";
    content += "<script>";
    content += "try{new Calendar({inputId:\""
          + itemStr + "\",bindToBtn:\""+ itemId +"\",property:{isHaveTime:true,format:\""+dateFormat+"\"}});}catch(e){var inputDom = document.getElementsByName(\""
          + itemStr + "\")[0];;var bindToDom = document.getElementsByName(\""+ itemId +"\")[0];new Calendar({inputId:inputDom,bindToBtn:bindToDom,property:{isHaveTime:true,format:\""+dateFormat+"\"}});}";
    content += "</script>";
    return content;
  }
  public String getUserAndDept(YHFlowFormItem item, List<YHFlowFormItem> itemList  ,Connection conn) throws Exception {
    String content = item.getContent();
    String realValue = item.getValue();
    String type = item.getType();
    String itemStr = "";
    int itemId2 = 0;
    for (YHFlowFormItem tmp : itemList) {
      String title2 = tmp.getTitle();
      String clazz2 = tmp.getClazz();
      itemId2 = tmp.getItemId();

      if ("DATE".equals(clazz2) || "USER".equals(clazz2)) {
        continue;
      }//注意。。。
      if (title2.equals(realValue)) {
        itemStr = "DATA_" + itemId2;
        break;
      }
    }
    //选 择人员
    if ( type == null || "".equals(type) || "0".equals(type)) {
      content = "<input type=\"hidden\" id=\"USER_" + itemId2 
        + "\" name=\"USER_"+ itemId2 
        +"\" value=\"\"><IMG class=USER align=absmiddle title=部门人员控件："
        +  realValue +" style=\"CURSOR: hand\" src=\"image/user.gif\" border=0 onclick=\"SelectUser('USER_"+ itemId2 +"','"+ itemStr +"')\">";
        
    } else if (type.equals("1")) {
      content = "<input type=\"hidden\" id=\"DEPT_" + itemId2 
        + "\" name=\"DEPT_"+ itemId2 
        +"\" value=\"\"><IMG class=USER align=absmiddle title=部门人员控件："
        +  realValue +" style=\"CURSOR: hand\" src=\"image/user.gif\" border=0 onclick=\"SelectDept('DEPT_"+ itemId2 +"','"+ itemStr +"')\">";
    }
    return content;
  }
  public String getAuto(YHFlowFormItem item
      , List<YHFlowFormItem> itemList
      ,YHPerson user , String content , String ip,Connection conn) throws Exception{
    String tag = item.getTag();
    String datafild = item.getDatafld();
    String autoValue = "";
    Date date = new Date();
    
    String value = item.getValue();
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
      } else if ("SYS_USERID".equals(datafild)) {
        autoValue = String.valueOf(user.getSeqId());
      }else if ("SYS_USERNAME".equals(datafild)) {
        autoValue = user.getUserName();
      } else if ("SYS_YEAR_DEPT".equals(datafild)) {
    	  int deptId = user.getDeptId();
          YHDeptLogic deptLogic = new YHDeptLogic();
          String deptName = deptLogic.getNameById(deptId , conn);
          autoValue =  new SimpleDateFormat("yyyy").format(date) + deptName;
      } else if ("SYS_YEAR_DEPT_AUTONUM".equals(datafild)) {
    	  int deptId = user.getDeptId();
          YHDeptLogic deptLogic = new YHDeptLogic();
          String deptName = deptLogic.getNameById(deptId , conn);
          autoValue =  new SimpleDateFormat("yyyy").format(date) + deptName + "文号";
      } else if ("SYS_USERPRIV".equals(datafild)) {
        // 取得用户的权限
        String userPriv = user.getUserPriv();
        YHUserPrivLogic logic = new YHUserPrivLogic();
        autoValue = logic.getNameById(Integer.parseInt(userPriv) , conn);
      } else if ("SYS_USERNAME_DATE".equals(datafild)) {
        // 用户时间
        String userName = user.getUserName();
        String sDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        autoValue = userName + " " + sDate;
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
        String query = "select FORM_NAME from oa_fl_form_type where SEQ_ID=" + item.getFormId();
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
      } else if ("SYS_IP".equals(datafild)) {
        autoValue = ip;
      } else if ("SYS_SQL".equals(datafild)) {
        // 查询数据库        String dataStr = item.getDatasrc();
        if (dataStr != null) {
          dataStr = YHPraseData2FormUtility.replaceSql(conn, user, dataStr);
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
      if("{宏控件}".equals(value)){
        value = "\\{宏控件\\}";//加上转义符...为后面的replaceAll
      }
      content = content.replace("value=\\\"{宏控件}\\\"" , "");
      content = content.replaceAll("value=" + value, "");
      content = content.replaceAll("value='" + value + "'", "");
      content = content.replaceAll("value=''" + value, "");
      autoValue = autoValue.replace("$", "\\$");
      String tag1 = tag.toLowerCase();
      content = content.replaceAll("<" + tag, "<" + tag
        + " value=\"" + autoValue + "\"");
      content = content.replaceAll("<" + tag1, "<" + tag1
          + " value=\"" + autoValue + "\"");
    } else {
      content = this.getAutoSelect(item, itemList , user , conn);
    }
    
    return content;
  }
  public String getAutoSelect(YHFlowFormItem item
      , List<YHFlowFormItem> itemList 
      ,YHPerson user
      , Connection conn) throws Exception{
    String datafild = item.getDatafld();
    String content = item.getContent();
    
    String autoValue = "<option value=\"\"";
    autoValue += " selected></option>";
    itemValueText = "";
    if ("SYS_LIST_DEPT".equals(datafild)) {
      StringBuffer sb = new StringBuffer();
      this.getDeptTree(0, sb, 0 , "", conn);
      autoValue += sb.toString();
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
          autoValue += "<option value ='" + userName + "' ";
          autoValue += ">" + userName + "</option>";
        }
      } catch(Exception ex) {
        autoValue +="<option value =''>无</option>";
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
          autoValue += "<option value ='" + privsName + "' ";
          autoValue += ">" + privsName + "</option>";
        }
      } catch(Exception ex) {
        autoValue +="<option value =''>无</option>";
      } finally {
        YHDBUtility.close(stm, rs, null); 
      }
    } else if ("SYS_LIST_SQL".equals(datafild)) {
      String dataStr = item.getDatasrc();
      if (dataStr != null) {
        content = content.replaceAll(dataStr , "");
        dataStr = YHPraseData2FormUtility.replaceSql(conn, user, dataStr);
        Statement stm2 = null;
        ResultSet rs2 = null ;
        try {
          stm2 = conn.createStatement();
          rs2 = stm2.executeQuery(dataStr);
          while (rs2.next()) {
            String autoValueSql = rs2.getString(1);
            autoValue += "<option value ='" + autoValueSql + "' ";
            autoValue += ">" + autoValueSql + "</option>";
          }
        } catch(Exception ex) {
          autoValue +="<option value =''>无</option>";
        } finally {
          YHDBUtility.close(stm2, rs2, null); 
        }
      }
    } else if ("SYS_LIST_MANAGER1".equals(datafild)) {
      int tmpDeptId = user.getDeptId();
      autoValue += this.sysListManager(tmpDeptId, user.getSeqId(), "", conn);
    } else if ("SYS_LIST_MANAGER2".equals(datafild)) {
      YHPrcsRoleUtility pu = new YHPrcsRoleUtility();
      YHORM orm = new YHORM();
      YHDepartment loginDept = (YHDepartment) orm.loadObjSingle(conn, YHDepartment.class, user.getDeptId());
      YHDepartment  department = pu.deptParent(loginDept, 1, conn);
      autoValue += this.sysListManager(department.getSeqId(), user.getSeqId(), "", conn);
    } else if ("SYS_LIST_MANAGER3".equals(datafild)) {
      YHPrcsRoleUtility pu = new YHPrcsRoleUtility();
      YHORM orm = new YHORM();
      YHDepartment loginDept = (YHDepartment) orm.loadObjSingle(conn, YHDepartment.class, user.getDeptId());
      YHDepartment  department = pu.deptParent(loginDept, 0, conn);
      autoValue += this.sysListManager(department.getSeqId(), user.getSeqId(), "", conn);
    }
    // $ELEMENT_OUT=substr($ELEMENT_OUT,0,strpos($ELEMENT_OUT,">")+1).$AUTO_VALUE."</SELECT>";
    content = content.substring(0, content.indexOf(">") + 1)
        + autoValue + "</SELECT>";
    return content;
  }
  public String getListView(YHFlowFormItem item, List<YHFlowFormItem> itemList ,YHPerson user){
    String content = item.getContent();
    int itemId = item.getItemId();
    String lvTbId = "LV_" + itemId;
    String lvTitle = item.getLvTitle();
    String lvAlign = item.getLvAlign();
    if ( lvAlign == null ) {
      lvAlign = "";
    }
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
    
    //isReadOnly = false;
    content = "<TABLE id='"
        + lvTbId
        + "' class='LIST_VIEW' style='border-collapse:collapse' border=1 cellspacing=0 cellpadding=2 FormData='"
        + lvSize
        + "'><TR "
        + "style='font-weight:bold;font-size:14px;' class='LIST_VIEW_HEADER'>\n";
    String[] myArray = lvTitle.split("`");
    String[] alignArray = lvAlign.split("`");
    for (int b = 0 ;b < myArray.length ; b++) {
      String tmp = myArray[b];
      String align = "";
      if (alignArray.length > b) {
        align = alignArray[b];
      }
      if ("".equals(align) || align == null) {
        align = "left";
      }
      content += "<TD nowrap align=\""+ align +"\">" + tmp + "</TD>\n";
    }
    content += "<TD>操作</TD></TR></TABLE>\n";
    
    content += "<input type=button class='SmallButtonW' value=新增  onclick=\"tbAddNew('"
        + lvTbId + "',0,'','" + lvSum + "','" + lvCal
        + "','" + lvAlign
        + "')\">\n";
    content += "<input type=button class='SmallButtonW' value=计算  onclick=\"tbCal('"
        + lvTbId + "','" + lvCal + "')\">\n";
    return content;
  }
  public String getSign(YHFlowFormItem item, List<YHFlowFormItem> itemList){
    // TODO Auto-generated method stub
    String content = "";
    int itemId = item.getItemId();
    String signColor = item.getSignColor();
    String signType = item.getSignType();
    if (YHUtility.isNullorEmpty(signType)) {
      signType = "1,1,";
    }
    if (signColor == null) {
      signColor = "";
    }
    String[] signTypes = signType.split(",");
    String signId = "DATA_" + itemId;
    signObject += signId + ",";
    
    String signCheck = "";
    if (item.getDatafld() != null) {
      signCheck =  item.getDatafld();
    }
    if (!signCheck.endsWith(",")) {
      signCheck += ",";
    }
    if ("1".equals(signTypes[0])) {
      content += "<input type=button class='SmallButtonW' value=盖章 onclick=\"addSeal('"
        + signId + "')\">";
    }
    if ("1".equals(signTypes[1])) {
      content += "<input type=button class='SmallButtonW' value=手写 onclick=\"handWrite('"
        + signId + "' , '"+ signColor +"')\">";
    }
    content += "<div class='websign' id=SIGN_POS_" + signId + ">&nbsp;";
    content += "</div>";
    content += "<input type=hidden  id="+signId+" name=" + signId + " value=''>";
    content += "<script>";
    content += "TO_VAL = \"" + signCheck + "\"";
    content += "</script>";
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
          
          autoValue += "<option value='" + userName + "'";
          if (userName.equals(selectValue)) {
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
          autoValue += "<option value='" + userName + "'";
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
  public String getCalc(String value , List<YHFlowFormItem> itemList , int itemId , String content){
    String eCalc = this.calculate(value, itemList);
    content += "<script>"
      + " window.calc_"+ itemId +" = function(){"
      + "  var myvalue= eval(\""+ eCalc +"\");"
      + "  if (myvalue==Infinity) {"
      + "    try{"
      + "      document.getElementById('DATA_"+ itemId +"').value=\"无效结果\";"
      + "    } catch(e) {document.getElementsByName('DATA_"+ itemId +"')[0].value=\"无效结果\";}"
      + "   } else if(!isNaN(myvalue)) {"
      + "       var prec = '';try{ prec = document.getElementById('DATA_"+ itemId +"').getAttribute('prec');} "
      + "          catch(e) {prec = document.getElementsByName('DATA_"+ itemId +"')[0].getAttribute('prec');}"
      + "     var vPrec;"
      + "     if(!prec) {"
      + "       vPrec=10000;"
      + "     } else {"
      + "       vPrec=Math.pow(10,prec);"
      + "     }"
      + "     var result = new Number(parseFloat(Math.round(myvalue*vPrec)/vPrec));"
      + "    try{"
      + "      document.getElementById('DATA_"+ itemId +"').value=result.toFixed(prec);"
      + "    } catch(e) {document.getElementsByName('DATA_"+ itemId +"')[0].value=result.toFixed(prec);}"
      + "   }else {"
      + "    try{"
      + "      document.getElementById('DATA_"+ itemId +"').value=myvalue;"
      + "    } catch(e) {document.getElementsByName('DATA_"+ itemId +"')[0].value=myvalue;}"
      + "   } "
      + "   setTimeout(\"window.calc_"+ itemId +"()\",1000);"
      + " };"
      + " setTimeout(\"window.calc_"+ itemId +"()\",3000);"
      + " </script>";
    if ("RMB(小写)".equals(value)) {
      //System.out.println(value);
    }
    return content;
  }
  
  public String calculate (String value  , List<YHFlowFormItem> itemList ) {
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
    //没有运算公式
    if (!Pattern.matches(".+[\\+|\\-|\\*|\\/|,].+", value)) {
      flag = true;
    }
    Map<String , String> formatMap = new HashMap(); 
    for (YHFlowFormItem tmp : itemList) {
      String title2 = tmp.getTitle();
      String clazz2 = tmp.getClazz();
      int itemId2 = tmp.getItemId();

      if ("DATE".equals(clazz2)) {
        formatMap.put(tmp.getValue(), YHUtility.null2Empty(tmp.getDateFormat()));
      }
    }
    //--- 替换控件名称 ---
    for (YHFlowFormItem tmp : itemList) {
      String title2 = tmp.getTitle();
      int itemId2 = tmp.getItemId();
      String format = YHUtility.null2Empty(formatMap.get(title2));
      //没有运算公式
      if (flag  && value.equals(title2)) {
        value = "calcGetVal('DATA_" + itemId2 + "' , '"+ format +"')" ;
        break ;
      } else {
        if(title2.indexOf("/") != -1){
          title2 = title2.replaceAll("/", "\\\\/");
        }
        value = YHRegexpUtility.replaceTitle(value, title2, "calcGetVal('DATA_" + itemId2 + "', '"+ format +"')");
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
      //String dept = String.valueOf(dp.getSeqId());
      sb.append("<option value='" + dp.getDeptName()  + "' " );
      if (dp.getDeptName().equals(value)) {
        sb.append(" selected ");
      }
      sb.append(">");
      sb.append(flag + dp.getDeptName());
      sb.append("</option>");
      this.getDeptTree(dp.getSeqId(), sb, level + 1 , value , conn);
    }
  }
  public Timestamp getBeginTime(int runId , Connection conn) throws Exception {
    String query = "select BEGIN_TIME from oa_fl_run where RUN_ID=" + runId;
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
    String content = "<SELECT id=DATA_5 title=子菜单 name=DATA_5 src=\"/yh/core/funcs/workflow/flowForm/editor/plugins/NListMenu/listmenu.gif\">\n <OPTION selected value=子1|选项1>子1|选项1</OPTION> \n<OPTION value=子2|选项1>子2|选项1</OPTION> \n<OPTION value=子3|选项1>子3|选项1</OPTION> \n<OPTION value=子4|选项2>子4|选项2</OPTION>\n <OPTION value=子5|选项2>子5|选项2</OPTION> \n<OPTION value=子6|选项2>子6|选项2</OPTION> \n<OPTION value=子7|选项3>子7|选项3</OPTION> \n<OPTION value=子8|选项3>子8|选项3</OPTION>\n <OPTION value=子9|选项3>子9|选项3</OPTION>\n <OPTION value=子10|选项4>子10|选项4</OPTION>\n <OPTION value=子11|选项4>子11|选项4</OPTION>\n <OPTION value=子12|选项4>子12|选项4</OPTION>\n</SELECT>";
    String realValue = "子9|选项3";
    content = content.replaceAll(" selected", "");
    String tmp = realValue.replaceAll("\\|", "\\\\|");
    content = content.replaceAll("<OPTION value=" + tmp + ">",
        "<OPTION selected value=\"" + realValue + "\">");
    content = content.replaceAll("<OPTION value=\"" + tmp + "\">",
        "<OPTION selected value=\"" + realValue + "\">");
    //System.out.println(content);
  }
}
