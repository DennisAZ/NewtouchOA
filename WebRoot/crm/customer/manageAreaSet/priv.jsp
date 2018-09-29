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
<script type="text/javascript">
var userId = "<%=userId%>";
var privNo = "<%=privNo%>";
var privName = "<%=privName%>";
var seqId = "<%=seqId%>";
var tree = null;
var check_all_var = true;

function doInit(){
  var p = {bindToContainerId:'di'
         ,isOnceLoad:true
		 ,requestUrl:contextPath + '/yh/core/funcs/person/act/YHUserPrivAct/getPrivTree.act'
		 ,checkboxPara:{isHaveCheckbox:true}
		 ,treeStructure:{isNoTree:true,regular:'2,2,2'}
		 ,isFolder:true	
  };
  tree =  new DTree(p);
  tree.show();

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
      //input.appendChild(label);
      
	  td.appendChild(input);
	  td.appendChild(label);
	}
  }else{
	alert(rtJson.rtMsrg); 
  }
} 

function select_all(){
  var cb = $('input').getElementsByTagName("input");
  if(!cb || cb.length == 0){
    return;
  }
  for(var i = 0;i < cb.length; i++){
    if(check_all_var){
      cb[i].checked = true;
    }else{
      cb[i].checked = false;
    }
  }
  check_all_var =! check_all_var;
}

function doSubmit(){
  var id = tree.getCheckedList();
  var user_priv_str = "";
  var user_priv = $('input').getElementsByTagName("input");
  var user_op = $('operate').getElementsByTagName("input");
  for(var x = 0; x < user_op.length; x++){
	if(user_op[x].checked){
	  user_op = user_op[x].value;
	}
  }
  for(var i = 0; i < user_priv.length; i++){
     if(user_priv[i].checked){
       user_priv_str += user_priv[i].value + ",";
     }
  }
  if(user_priv_str==""){
    alert("请选择角色");
    return;
  }
  //alert(id);        //树的编号
  // alert(user_op); //是否添加或者是删除 添加是0  删除是1
  //alert(user_priv_str);   //角色seqId
  
  var formDiv = document.getElementById("formDiv");
  var show = document.getElementById("show");
  //var seqId = 0;
  //document.getElementById("seqId").value;
  var dtoClass = "yh.core.funcs.dept.data.YHUserPriv";
  var positionSeqId = userId;
  //var seqId = document.getElementById("seqId").value;
  //var id = tree.getCheckedList();
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/usertAddDeleltPriv.act?user_op=" + user_op;
  var rtJson = getJsonRs(url, "treeNo=" + id + "&dtoClass=" + dtoClass + "&userPrivSeqId=" + user_priv_str);
  if(rtJson.rtState == "0"){
    //$("form1").reset();
    //bindJson2Cntrl(rtJson.rtData);dtoClass
    //formDiv.style.display="none";
    //show.style.display="";
    //alert(rtJson.rtMsrg);
    location.href ='<%=contextPath %>/core/funcs/userpriv/manage.jsp';
  }else{
	alert(rtJson.rtMsrg);
  }
}
function test2(id){  
}
function test1(id){
  var obj = tree.getNode(id);
  var parentId = obj.parentId;
  var deptParent = '无';
  if(parentId != '0'){
  	deptParent = tree.getNode(parentId).name;
  }
  window.parent.deptinput.location = "<%=contextPath%>/core/funcs/dept/mulindex.jsp?treeId=" + id + "&deptParent=" + deptParent + "&deptLocal=" + obj.name; 
}

function commit(){
  //document.getElementById("positionFlag") = document.getElementById("pre4");
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptPositionAct/insertPriv.act?treeId=" + treeId;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if(rtJson.rtState == "0"){
    //$("form1").reset();
    //bindJson2Cntrl(rtJson.rtData);
    alert(rtJson.rtMsrg);
  }else{
	alert(rtJson.rtMsrg);
  }
}

window.onscroll = window.onresize = function(){
   var opBtn = document.getElementById("OP_BTN");
   if(!opBtn){
     return false;
   } else {
     opBtn.style.left = (document.documentElement.clientWidth + document.documentElement.scrollLeft - 150) + 'px';
     opBtn.style.top = (document.documentElement.scrollTop + 25) + 'px';
   }
};
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 添加/删除权限</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>

<table class="TableBlock" width="100%" align="center">
  <form method="post" name="form1" id="form1">
   <tr class="TableData">
    <td nowrap width="80"><b>操作：</b></td>
    <td id="operate">
        <input type="radio" name="OPERATION" value="0" id="OPERATION0" checked><label for="OPERATION0">添加权限</label>
        <input type="radio" name="OPERATION" value="1" id="OPERATION1"><label for="OPERATION1">删除权限</label>
    </td>
   </tr>
   <br></br>
   <tr class="TableData">
    <td nowrap><b>角色：</b><a href="javascript:select_all();"><u>全选</u></a></td>
    <td id="input">
    </td>
   </tr>
  </form>
</table>
 <div id="deplist"></div>
 <div id="formDiv">
 <form name="form1" id="form1">
 <input type="hidden" name="seqId" id="seqId" value="">
  <input type="hidden" name="positionSeqId" id="positionSeqId" value="">
 <table id="treeSave">
 <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="hidden" name="DEPT_ID" value="1">
    </td>
    <tr/>
 </table>
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">&nbsp;&nbsp;
     <div id="OP_BTN" style="width:150px;top:5px;right:20px;position:absolute;">
       <input type="button" value="保存" class="BigButtonA" title="保存" name="button" onclick="doSubmit();">&nbsp;&nbsp;
       <input type="button" value="返回" class="BigButtonA" onclick="location='manage.jsp'">
    </div>
    </td>
  </tr>
 </table>
 </form>
 </div>
 <div align="center" style="display:none" id="show">
 <table class="MessageBox" align="center" width="410">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">岗位人员信息已保存修改</div>
    </td>
  </tr>
 </table>
 </div>
</body>
</html>