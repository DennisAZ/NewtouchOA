<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<% request.setCharacterEncoding("utf-8"); %>
<% 
String  useInfoResSubsys = YHSysProps.getProp("useInfoResSubsys");
String sortId = request.getParameter("sortId");
%>

<%@page import="yh.core.funcs.workflow.util.YHWorkFlowUtility"%>
<HTML>
<HEAD>
<TITLE>表单智能设计器</TITLE>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>   
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<SCRIPT language=JavaScript>
var itemMax = 0 ;
var formId = 0 ;
//var flag = "true"; 
var isSave2DataTable = <%=YHWorkFlowUtility.isSave2DataTable() %>;
self.moveTo(0,0);
self.resizeTo(screen.availWidth,screen.availHeight);
self.focus();
var cssPath = "<%=cssPath%>/style.css";

var seqId = "<%=request.getParameter("seqId")%>";
var loadDataDom = "";
var useInfoResSubsys = '<%=useInfoResSubsys%>';
function CheckForm()
{
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;

  if(FORM_HTML == "")
  {
  	alert("表单内容不能为空！");
    return (false);
  }
  return (true);
}

function send(flag)
{
  if(CheckForm())
  {
    var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
 	  var FORM_MODE = FCK.EditingArea.Mode;
   
    //获取编辑区域的常量——源文件模式
    var editingAreaFrame = document.getElementById('FORM_CONTENT___Frame');
    var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js

    if(FORM_MODE == editModeSourceConst)
    {
    	FCK.Commands.GetCommand( 'Source' ).Execute();
    } 
   // $("formName").value = formName;
   var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
   var textStr = FORM_HTML;
   /// var url = "";
    //alert(mergeQueryString($("form1")));
    if (!flag) {
      if(seqId != null && seqId != ""){
        url = "<%=contextPath%>/yh/core/funcs/workflow/act/YHFlowFormAct/updateForm.act";
      }else{
        url = "<%=contextPath%>/yh/core/funcs/workflow/act/YHFlowFormAct/insertForm.act";
      }
    } else {
      url = "<%=contextPath%>/yh/core/funcs/workflow/act/YHFormVersionAct/genFormVersion.act";
    }
    
    //var textStr = FCK.GetXHTML( true );
    textStr = textStr.replace(/\n/g,"");
    document.form1.printModel.value=textStr;
    document.form1.itemMax.value=itemMax;
    var par = mergeQueryString($("form1"));
    var rtJson = getJsonRs(url, par);
    if(rtJson.rtState == "0"){
      alert(rtJson.rtMsrg);
      if (flag) {
        try{
          if (window.opener) {
            window.opener.parent.sort.location.reload();
            window.opener.location.href = contextPath + "/core/funcs/workflow/flowform/edit.jsp?seqId=" + rtJson.rtData + "&sortId=<%=sortId %>";
          }
        }catch(e) {}
        location.href = 'main2.jsp?seqId=' + rtJson.rtData  + "&sortId=<%=sortId %>";
      }
    }else{
   	  alert(rtJson.rtMsrg);
     }
  }
}

function myclose(){
  msg='关闭表单设计器前，保存对表单的修改？';
  if(window.confirm(msg)){
    document.form1.CLOSE_FLAG.value="1";
    send();
  }
  window.close();
}
//var fun = test;
function Load_Do()
{
}
function loadData(){
  var url = "<%=contextPath%>/yh/core/funcs/workflow/act/YHFlowFormAct/getFormView.act?";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if(rtJson.rtState == "0"){
    itemMax  = rtJson.rtData.ITEM_MAX;
    formId =  rtJson.rtData.FORM_ID;
    //flag =  rtJson.rtData.flag;
    if(rtJson.rtData.PRINT_MODEL == "null"){
      return "";
    }else{
      return rtJson.rtData.PRINT_MODEL;
    }
  }else{
 	 alert(rtJson.rtMsrg);
  }
  
}
var size={
	textfield_w:320,textfield_h:160,
	textarea_w:320,textarea_h:190,
	listmenu_w:320,listmenu_h:300,
	checkbox_w:320,checkbox_h:120,
	calendar_w:320,calendar_h:120,
	auto_w:320,auto_h:230,
	calc_w:420,calc_h:200,
	listview_w:420,listview_h:293,
	user_w:330,user_h:150,
	sign_w:330,sign_h:170,
	data_w:420,data_h:290,
	fetch_w:420,fetch_h:290
};

//--- 单行输入框（新） ---
function td_textfield() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
  FCK.Focus();
  FCK.Commands.GetCommand("NTextField").Execute(); //仿照fcktoolbarbutton.js第71行的写法 by dq 090521
}
//--- 宏控件（新） ---
function td_auto() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
  FCK.Focus();
  FCK.Commands.GetCommand("NAuto").Execute(); //仿照fcktoolbarbutton.js第71行的写法 by dq 090521
}
//--- 计算控件（新） ---
function td_calcu() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
  FCK.Focus();
  FCK.Commands.GetCommand("NCalcu").Execute(); //仿照fcktoolbarbutton.js第71行的写法 by dq 090521
}
//--- 列表控件（新） ---
function td_listview() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT');
  FCK.Focus();
  FCK.Commands.GetCommand("NListView").Execute(); 
}
//--- 日历控件（新） ---
function td_calendar() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT');
  FCK.Focus();
  FCK.Commands.GetCommand("NCalendar").Execute(); 
}
//--- 部门人员控件（新） ---
function td_user() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT');
  FCK.Focus();
  FCK.Commands.GetCommand("NUser").Execute(); 
}
//--- 签章控件（新） ---
function td_sign() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT');
  FCK.Focus();
  FCK.Commands.GetCommand("NSign").Execute(); 
}

//--- 业务组件（新） ---
function module_select() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT');
  FCK.Focus();
  FCK.Commands.GetCommand("NModule").Execute(); 
}
//--- 下拉列表控件（新） ---
function td_listmenu() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT');
  FCK.Focus();
  FCK.Commands.GetCommand("NListMenu").Execute(); 
}
//--- 选择框控件（新） ---
function td_checkbox() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT');
  FCK.Focus();
  FCK.Commands.GetCommand("NCheckBox").Execute(); 
}
//--- 单选框 ---
function td_radio() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT');
  FCK.Focus();
  FCK.Commands.GetCommand("NRadio").Execute(); 
}

//--- 多行输入框控件（新） ---
function td_textarea() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT');
  FCK.Focus();
  FCK.Commands.GetCommand("NTextArea").Execute(); 
}
//--- 数据选择控件（新） ---
function td_data_select() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT');
  FCK.Focus();
  FCK.Commands.GetCommand("NDataSelect").Execute(); 
}
//--- 数据获取控件（新） ---
function td_data_fetch() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT');
  FCK.Focus();
  FCK.Commands.GetCommand("NDataFetch").Execute(); 
}
//--- 流程数据获取控件 ---
function td_flow_data_fetch() {
  var FCK = FCKeditorAPI.GetInstance('FORM_CONTENT');
  FCK.Focus();
  FCK.Commands.GetCommand("NFlowDataSelect").Execute(); 
}

function checkClose()
{   
  if(event.clientX>document.body.clientWidth-20 && event.clientY<0||event.altKey)
    window.event.returnValue='您确定退出表单设计器吗';   
}
function createFck() {
  var oFCKeditor = new FCKeditor('FORM_CONTENT'); 
  oFCKeditor.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/workflow/flowform/editor/fckconfig.js"; 
  oFCKeditor.BasePath = contextPath + "/core/js/cmp/fck/fckeditor/"; 
  oFCKeditor.Height = document.viewport.getDimensions().height;
  oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
  oFCKeditor.Config["PluginsPath"] =   contextPath +  "/core/funcs/workflow/flowform/editor/plugins/";
  oFCKeditor.ToolbarSet="FlowForm";
  loadDataDom = loadData();
  if(loadDataDom){
    oFCKeditor.Value = loadDataDom;
  }
  oFCKeditor.Create();
}
function doShow() {
  if (formId == "0" 
    //&& flag == "false"
      ) {
    $("generateButton").show();
  }
}
function genFormVersion() {
  send(true);
}
</SCRIPT>
<META content="MSHTML 6.00.2900.3640" name=GENERATOR></HEAD>
<BODY onload="doShow()" leftMargin=0 topMargin=0 onbeforeunload="checkClose();" >
<TABLE class=TableBlock height="100%" width="100%" align=center>
  <TBODY>
  <TR bgColor=#dddddd>
    <TD class=TableHeader colSpan=2 height=20>&nbsp;<IMG 
      src="image/dot3.gif" align='absMiddle' /> 
      表单智能设计器：首先，将网页设计工具或Word编辑好的表格框架粘贴到表单设计区。然后，创建表单控件。 </TD></TR>
  <TR bgColor=#dddddd>
    <TD width="100%" height="100%"  bgColor=#dddddd>
    <script language="JavaScript">  
      createFck();
    </script>
<%
  Thread.sleep(200);
%>
    </TD>
    <TD vAlign=top align=middle>
      <TABLE class=TableBlock width=120 align=center border=0>
        <TBODY>
          <TR class=TableHeader>
            <TD align=middle>表单控件</TD></TR>
          <TR class=TableData>
            <TD align=middle>
              <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_textfield()>
                <IMG height=20 src="image/textfield.gif" width=20  align=absMiddle>单行输入框</BUTTON><BR>
              <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_textarea()>
                <IMG height=20 src="image/textarea.gif" width=20 align=absMiddle>多行输入框</BUTTON><BR>
              <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_listmenu()>
                <IMG height=20 src="image/listmenu.gif" width=20 align=absMiddle>下拉菜单</BUTTON><BR>
              <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_radio()>
               <IMG height=20 src="image/radio.gif" width=20 align=absMiddle>单选框</BUTTON><BR>
              <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_checkbox()>
               <IMG height=20 src="image/checkbox.gif" width=20 align=absMiddle>复选框</BUTTON><BR>
              <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_listview()>
               <IMG height=20 src="image/listview.gif" width=20 align=absMiddle>列表控件</BUTTON><BR>
              <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_auto()>
               <IMG height=20 src="image/auto.gif" width=20 align=absMiddle>宏控件</BUTTON><BR>
              <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_calendar()>
               <IMG height=20 src="image/calendar.gif" width=20 align=absMiddle>日历控件</BUTTON><BR>
              <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_calcu()>
               <IMG height=20 src="image/calc.gif" width=20 align=absMiddle>计算控件</BUTTON><BR>
              <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_user()>
               <IMG height=20 src="image/user.gif" width=20 align=absMiddle>部门人员控件</BUTTON><BR>
              <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_sign()>
               <IMG height=20 src="image/sign.gif" width=20 align=absMiddle>签章控件</BUTTON><BR>
            <!--  <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=module_select()>
               <IMG height=20 src="image/data.gif" width=20 align=absMiddle>业务组件</BUTTON><BR> -->
               <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_data_select()>
               <IMG height=20 src="image/data.gif" width=20 align=absMiddle>数据选择控件</BUTTON><BR>
               <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_data_fetch()>
               <IMG height=20 src="image/data.gif" width=20 align=absMiddle>表单数据控件</BUTTON><BR>
              <BUTTON style="WIDTH: 120px; TEXT-ALIGN: left" onclick=td_flow_data_fetch()>
               <IMG height=20 src="image/data.gif" width=20 align=absMiddle>流程数据控件</BUTTON> 
             </TD>
           </TR>
         </TBODY>
       </TABLE><BR>
       <TABLE class=TableBlock width=120 align=center border=0>
         <TBODY>
           <TR class=TableHeader>
             <TD align=middle>保存与退出</TD></TR>
           <TR class=TableData>
            <TD align=middle>
              <BUTTON style="WIDTH: 120px; HEIGHT: 30px; TEXT-ALIGN: center" onclick=send()>
                <B>保存表单</B></BUTTON><BR>
              <BUTTON style="WIDTH: 120px; HEIGHT: 30px; TEXT-ALIGN: center" onclick='viewForm(<%=request.getParameter("seqId")%>)'>
                <B>预览表单</B></BUTTON><BR>
                <div id="generateButton" style="display:none">
                <BUTTON style="WIDTH: 120px; HEIGHT: 30px; TEXT-ALIGN: center" onclick='genFormVersion(<%=request.getParameter("seqId")%>)'>
                <B>生成版本</B></BUTTON><BR></div>
             <BUTTON style="WIDTH: 120px; HEIGHT: 30px; TEXT-ALIGN: center" onclick=myclose()>
              <B>关闭设计器</B></BUTTON>
             </TD>
           </TR>
           <TR class=TableData>
              <TD align=middle></TD>
           </TR>
          </TBODY>
        </TABLE>
      </TD>
     </TR>
  </TBODY>
 </TABLE>
        <FORM id="form1" name='form1' >
          <input type="hidden" id="dtoClass" name="dtoClass" value="yh.rad.flowform.data.YHFlowFormType"/>
          <INPUT type=hidden name='printModel' id='printModel'> 
          <INPUT type=hidden name='itemMax' id='itemMax'>  
          <INPUT type=hidden name=CLOSE_FLAG> 
          <INPUT type=hidden name='seqId' value="<%=request.getParameter("seqId")%>">
      </FORM>
 </BODY></HTML>
