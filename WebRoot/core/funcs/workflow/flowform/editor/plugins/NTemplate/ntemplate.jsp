<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.InputStreamReader" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/flowform.css">
<link rel="stylesheet" href = "<%=cssPath%>/views.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script src="<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>模板</title>
<SCRIPT> 
var menu_enter = "";
function borderize_on(e){
  var color="#708DDF";
  var source3 = event.srcElement;
  if(source3.className == "menulines" && source3 != menu_enter)
    source3.style.backgroundColor=color;
}
 
function borderize_on1(e){
  /*
  for(var i = 0; i < document.all.length; i++){ 
    document.getElementById(i).style.borderColor = "";
    document.getElementById(i).style.backgroundColor = "";
    document.getElementById(i).style.color = "";
    document.getElementById(i).style.fontWeight = "";
  }*/
  var color = "#003FBF";
  var source3 = event.srcElement; 
  if(source3.className == "menulines"){ 
    source3.style.borderColor = "black";
    source3.style.backgroundColor = color;
    source3.style.color = "white";
    source3.style.fontWeight = "bold";
  }
  var menu_enter = source3;
}
 
function borderize_off(e){
  var source4 = event.srcElement;
  if(source4.className == "menulines" && source4 != menu_enter){
    source4.style.backgroundColor = "";
    source4.style.borderColor = "";
  }
}

</SCRIPT>
<script type="text/javascript">
var dialog = window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement();
var parent_window = oEditor;
var FCK = oEditor.FCK ;
function click_model(ID){
  var targetelement = document.getElementById(ID);
  parent_window.FCKUndo.SaveUndoStep();
  FCK.EditingArea.Focus();
  if (targetelement.innerHTML) {
    FCK.InsertHtml(targetelement.innerHTML);
  }
  dialog.CloseDialog();
}

window.onload = function(){
  dialog.SetAutoSize(true);
}

</script>
</head>
<body topmargin="5">
<table border="1" cellspacing="0" width="100%" class="small" cellpadding="3"  bordercolorlight="#000000" bordercolordark="#FFFFFF"  onMouseover="borderize_on(event)" onMouseout="borderize_off(event)" onclick="borderize_on1(event)">
<tr class="TableHeader">
  <td bgcolor="#d6e7ef" align="center"><b>选择模版</b></td>
</tr>
<% 
char[] chrBuffer = new char[10]; //缓冲
File f = new File(YHSysProps.getWebPath()+"/core/funcs/workflow/workflowUtility/model/");
File[] fArray = f.listFiles();
for(int i = 0; i<fArray.length; i++) {
  if(fArray[i].getName().endsWith(".html")){
    FileInputStream file1 = new FileInputStream(fArray[i]);
    InputStreamReader isr = new InputStreamReader(file1, "UTF-8"); 
    BufferedReader reader = new BufferedReader(isr);
    StringBuffer sb = new StringBuffer();
    String b = reader.readLine();
    while(b != null) {
      sb.append(b);
      b = reader.readLine();
    }
    %>
    <tr class="TableLine1">
      <td class="menulines" align="center" onclick=javascript:click_model('CONTENT_<%=i %>') style="cursor:pointer">
      <%=fArray[i].getName() %><div style="display:none" id="CONTENT_<%=i %>" name="CONTENT_<%=i %>"><%=sb.toString() %></div>
      </td>
    </tr>
    <%
    file1.close();
    reader.close();
    isr.close();
  }
}
%>
</table>
</body>
</html>