<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson,yh.core.funcs.doc.util.YHWorkFlowUtility" %>
<%
  String seqId = request.getParameter("seqId");
  String sortId = request.getParameter("sortId");

  if (seqId == null){
    seqId = "";
  }
  if (sortId == null){
    sortId = "0";
  }
  String num = request.getParameter("num");
  if (num == null){
    num = "";
  }
  String treeId = request.getParameter("treeId");
  if (treeId == null){
    treeId = "";
  }
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  String userPrivOther  = loginUser.getUserPrivOther();
  boolean isAdmin = false;
  if (loginUser.isAdminRole() 
      || YHWorkFlowUtility.findId(userPrivOther , "1")){
    isAdmin = true;
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑表单</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var treeId = "<%=treeId%>";
var sortId = "<%=sortId%>";
var isAdmin = <%=isAdmin%>;
var deptId = <%=loginUser.getDeptId()%>;
var flowFormUrl = contextPath + "<%=moduleSrcPath %>/act/YHFlowFormAct";
var formViewUrl = contextPath + "<%=moduleSrcPath %>/act/YHFlowFormViewAct";

function doInit(){
  if (isAdmin) {
    var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowSortAct/getDeptList.act";
    var rtJson = getJsonRs(url);
    if (rtJson.rtState == "0") {
      setOptions($('deptId') , rtJson.rtData);
    }
  }
  var div = document.getElementById("operation");
  div.style.display = "none";
  var divNew = document.getElementById("newID");
  var divEdit = document.getElementById("editID");
  if(seqId != null&&seqId != ""){
    divEdit.style.display = "";
    divNew.style.display = "none";
  }else{
    divEdit.style.display = "none";
    divNew.style.display = "";
  }
  if(seqId != null&&seqId != ""){
    var div = document.getElementById("operation");
    div.style.display = "";
    var url = flowFormUrl + "/getFlowForm.act?seqId=" + seqId;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      //表单下有工作
      if (!rtJson.rtData.noDelete) {
		$("delButton").show();
      }
      bindJson2Cntrl(rtJson.rtData.formData);
    }else{
	  alert(rtJson.rtMsrg); 
    }
  } else {
    if (!isAdmin) {
      $('deptId').value = deptId;
    }
  }
  selectLast($('formName'));
}
function setOptions(el,aOption,value){
  for(var i = 0 ;i< aOption.length ;i++){
    var option = new Element('option').update(aOption[i].text);
    option.value = aOption[i].value;
    el.appendChild(option);
  }
  if(value){
    el.value = value;
  }  
}
function Submit(){
  var deptId = document.getElementById("deptId").value;
  var formDiv = document.getElementById("formDiv");
  var show = document.getElementById("show");
  var formName = document.form1.formName;
  var reg = /['"]/g;
  //onkeyup="value=value.replace(reg,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(reg,''))" 
  var str = formName.value;
  if(!str){ 
    alert("表单名称不能为空！");
    $('formName').focus();
	return (false);
  }
  if (str.match(reg)) {
    alert("表单名称不能有\"'\"和\"\"\"字符！");
    $('formName').focus();
  	return (false);
  }
  if(seqId != null 
      && seqId != ""){
    var url = flowFormUrl + "/updateFlowForm.act";
  }else{
    var url = flowFormUrl + "/insertFlowForm.act";
  }
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if(rtJson.rtState == "0"){
    location.href ='<%=contextPath%><%=moduleContextPath %>/flowform/list.jsp?sortId=' + sortId;
    parent.sort.location.reload();
  }else{
	  alert(rtJson.rtMsrg);
  }
}

function confirmDel() {
  if(confirm("确认要删除该表单吗？这将删除表单描述与字段设置且不可恢复！")){
    return true;
  }else{
    return false;
  }
}

function deleteForm(){
  if(!confirmDel()){
    return ;
  }
  var url = flowFormUrl + "/deleteForm.act";
  var rtJson = getJsonRs(url, $('form1').serialize());
  if(rtJson.rtState == "0"){
    location.href ='<%=contextPath%><%=moduleContextPath %>/flowform/list.jsp?sortId=' + sortId;

    parent.sort.location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function design(){
  openFormDesign(seqId);
}

function ClearUser(TO_ID, TO_NAME){
  if(TO_ID == "" || TO_ID == "undefined" || TO_ID == null){
    TO_ID = "TO_ID";
	TO_NAME = "TO_NAME";
  }
  document.getElementsByName(TO_ID)[0].value = "";
  document.getElementsByName(TO_NAME)[0].value = "";
}
function showView(seqId){
  var url = "<%=contextPath %><%=moduleContextPath %>/flowform/formView.jsp?seqId=" + seqId ;
  showWindow(url,'预览表单',800,600);
}
function exportForm(formId) {
  window.open(contextPath + '<%=moduleSrcPath %>/act/YHFlowExportAct/exportForm.act?formId=' + formId); 
}

</script>
</head>
<body class="" topmargin="5" onload="doInit()">
<div id="newID">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big" ><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 新建表单</span><br>
    </td>
  </tr>
</table>
</div>
<div id="editID">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big" ><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 编辑表单名称</span><br>
    </td>
  </tr>
</table>
</div>
<br>
  <form action=""  method="post" name="form1" id="form1">
  <div align=center>

   <table width="450" align="center" class="TableList">
   <input type="hidden" name="seqId" id="seqId" value="">
   <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.doc.data.YHDocFlowFormType"/>
    <tr>
      <td nowrap class="TableContent">表单名称：</td>
      <td class="TableData" align=left>
        <input type="text" name="formName"    id="formName" size="30" maxlength="100" class="BigInput" value="">
        <font style='color:red'>*</font>
      </td>
    </tr>
<% 
if (isAdmin) { 
%>
    <tr>
      <td nowrap class="TableContent">所属部门：</td>
     <td nowrap class="TableData"  align=left>
        <select id='deptId' name="deptId">
       <option value="0">系统管理员</option>
      </select>
    </td>
    </tr>
    <%} %>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
      <% if (!isAdmin) { %>
      <input type='hidden' value="" name="deptId" id="deptId">
      <%} %>
        <input type="button"  value="保存" class="BigButton" name="button" onclick="Submit();">&nbsp;&nbsp;
        <input type="button"  value="返回" class="BigButton" name="button" onclick="window.history.back()">
      </td>
    </tr>
    </table>
    </div>
 
  </form>
  <br>
  <div  align=center id="operation">  

  <table width="450" height="28" align="center" class="TableList">
    <tr>
      <td class="TableContent" nowrap align="center" width=100 ><b>相关操作：</b></td>
      <td class="TableData" nowrap align=left>
          &nbsp;
          <a href="javascript:design();">表单智能设计器</a>&nbsp;&nbsp;
          <a href="javascript:viewForm(<%=seqId %>);" title="表单预览">预览</a>&nbsp;&nbsp;
          <a href="javascript:location='import.jsp?formId=<%=seqId %>'" title="可将一些备份的表单或优秀的表单导入">导入</a>&nbsp;&nbsp;
          <a href="javascript:exportForm(<%=seqId %>);" title="可以将表单导出后，与其他OA用户分享">导出</a>&nbsp;&nbsp;
          <a href="javascript:deleteForm();" id="delButton" style="display:none">删除</a>&nbsp;
      </td>
    </tr>
  </table>

</div>
  
<br>
<br>
<div class="small1" align=center>

不明白如何设计表单和设计流程？请参考官方网站下载中心的<a href="<%=StaticData.YH_DOWN_SITE1%>">《工作流使用详解》</a>。

</div>
</body>
</html>