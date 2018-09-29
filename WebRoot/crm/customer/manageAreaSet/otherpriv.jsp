<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String treeId = request.getParameter("treeId");
  if(treeId == null){
    treeId = "";
  }
  String userId = request.getParameter("userId");
  if(userId == null){
    userId = "";
  }
  String privNo = request.getParameter("privNo");
  if (privNo == null){
    privNo = "";
  }
  String privName = request.getParameter("privName");
  if (privName == null){
    privName = "";
  }
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加/删除权限</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"><!--
var userId = "<%=userId%>";
var privNo = "<%=privNo%>";
var privName = "<%=privName%>";
var treeId = "<%=treeId%>";
var seqId = "<%=seqId%>";
var tree = null;
var check_all_var = true;

function doInit(){

  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/getUserName.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    //bindJson2Cntrl(rtJson.rtData);
    //alert(rsText);
	var td = document.getElementById("input");
	for(var i = 0; i < rtJson.rtData.length; i++){
	  var input = document.createElement("input");
	  input.setAttribute("type", "checkbox");
	  input.setAttribute("id", "USER_PRIV" + rtJson.rtData[i].seqId);
	  input.setAttribute("name", "USER_PRIV");
	  input.setAttribute("value", rtJson.rtData[i].seqId);
	  
	  var label = document.createElement("label");
	  label.setAttribute("for", "USER_PRIV" + rtJson.rtData[i].seqId);
	  label.innerHTML = rtJson.rtData[i].privName;
      
	  td.appendChild(input);
	  td.appendChild(label);
	}
  }else{
	alert(rtJson.rtMsrg); 
  }
} 

function select_all(){
  var cb = document.getElementsByTagName("input");
  if(!cb || cb.length == 0){
    return;
  }
  for(var i = 0;i < cb.length; i++){
    if (cb[i].type == 'checkbox'){
      if(check_all_var){
        cb[i].checked = true;
      }else{
        cb[i].checked = false;
      }
    }
  }
  check_all_var =! check_all_var;
}

function test(){
  var id = document.getElementById("manager").value;
  var user_priv_str = "";
  var user_op_str = "";
  
  var user_priv = document.getElementsByTagName("INPUT");
  var user_op = document.getElementsByTagName("INPUT");
  
  for(var x = 0; x < user_op.length; x++){
	if(user_op[x].type== 'radio' && user_op[x].checked){
	  user_op_str = user_op[x].value;
	}
  }
  for(var i = 0; i < user_priv.length; i++){
     if(user_priv[i].type == 'checkbox' && user_priv[i].checked){
       user_priv_str += user_priv[i].value + ",";
     }
  }
  if($("manager").value == ""){
    alert("请选择人员");
    return false;
  }
  if(user_priv_str == ""){
    alert("请选择角色");
    return false;
  }
  //alert(id);        //树的编号
  //alert(user_op); //是否添加或者是删除 添加是0  删除是1
  //alert(user_priv_str);   //角色seqId
  var formDiv = document.getElementById("formDiv");
  var show = document.getElementById("show");
  var dtoClass = "yh.core.funcs.dept.data.YHUserPriv";
  var positionSeqId = userId;
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/otherPriv.act";
  var rtJson = getJsonRs(url, "treeNo=" + user_priv_str + "&dtoClass=" + dtoClass + "&userPrivSeqId=" + id + "&user_op=" + user_op_str);
  if(rtJson.rtState == "0"){
    alert("操作成功！");
    //location.href ='<%=contextPath %>/core/funcs/userpriv/manage.jsp';
  }else{
	alert(rtJson.rtMsrg);
  }
}

function commit(){
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptPositionAct/insertPriv.act?treeId=" + treeId;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if(rtJson.rtState == "0"){
    alert(rtJson.rtMsrg);
  }else{
	alert(rtJson.rtMsrg);
  }
}

function ClearUser(TO_ID, TO_NAME){
  if(TO_ID == "" || TO_ID == "undefined" || TO_ID == null){
    TO_ID = "TO_ID";
	TO_NAME = "TO_NAME";
  }
  document.getElementsByName(TO_ID)[0].value = "";
  document.getElementsByName(TO_NAME)[0].value = "";
}

function SelectUser(TO_ID, TO_NAME){
  var treId = treeId;
  URL = "/yh/core/funcs/userpriv/userselect.jsp?treeId=" + treId + "&TO_ID=" + TO_ID + "&TO_NAME=" + TO_NAME;
  openDialog(URL,'400', '350');
}

--></script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 添加/删除辅助角色</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>

<form method="post" name="form1" id="form1">
  <table class="TableBlock" width="600" align="center">
   <tr class="TableData">
    <td nowrap width="80">操作：</td>
    <td>
        <input type="radio" name="OPERATION" value="0" id="OPERATION0" checked><label for="OPERATION0">添加权限</label>
        <input type="radio" name="OPERATION" value="1" id="OPERATION1"><label for="OPERATION1">删除权限</label>
    </td>
   </tr>
   <tr>
      <td nowrap class="TableData">人员：</td>
      <td class="TableData">
        <input type="hidden" name="manager" id="manager" value="">
        <textarea cols=45 name="managerDesc" id="managerDesc" rows=4 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['manager', 'managerDesc'])">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('manager', 'managerDesc')">清空</a>
      </td>
   </tr>
   <tr class="TableData">
    <td nowrap>角色：<a href="javascript:select_all();"><u>全选</u></a></td>
    <td id="input">
    </td>
   </tr>
   <tr class="TableControl">
    <td colspan="2" align="center">
        <input type="button" value="确定" class="BigButton" onclick="test();">
    </td>
   </tr>
   <input type="hidden" name="USER_PRIV_STR" value="">
  </form>
</table>
<table class="MessageBox" align="center" width="500">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">为所选择的人员添加或删除指定的辅助角色</div>

    </td>
  </tr>
</table>


</body>
</html>