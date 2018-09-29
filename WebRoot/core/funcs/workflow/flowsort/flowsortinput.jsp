<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson,yh.core.funcs.workflow.util.YHWorkFlowUtility" %>
<%
String seqId = request.getParameter("seqId");
if(seqId == null) {
  seqId = "";
}

String sortParent = request.getParameter("sortParent");
if(sortParent == null) {
  sortParent = "";
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
<title>流程分类添加</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var sortParent = "<%=sortParent%>";
var isAdmin = <%=isAdmin%>;
var deptId = <%=loginUser.getDeptId()%>;
var sortParentSel = null;
var reg = /['"]/g;
function loadSelect() {
  var url = "<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowSortAct/getSortTreeJson.act";
  var rtJson = getJsonRs(url); 
  if (rtJson.rtState == "0") {
   sortParentSel = document.getElementById("sortParent");
    sortParentSel.options.length = 0;
    var option = document.createElement("option");
    option.value = "0";
    option.text = "";
    sortParentSel.appendChild(option);
    for(var i=0; i<rtJson.rtData.length; i++){
      var option = document.createElement("option");
      option.value = rtJson.rtData[i].value;
      option.innerHTML = rtJson.rtData[i].text;
      sortParentSel.appendChild(option);
    }
  }else {
   // alert(rtJson.rtMsrg); 
  }
}

function doInit() {
   loadSelect();
   if (isAdmin) {
     var url = "<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowSortAct/getDeptList.act";
     var rtJson = getJsonRs(url);
     if (rtJson.rtState == "0") {
       setOptions($('deptId') , rtJson.rtData);
     }else {
       alert(rtJson.rtMsrg); 
     }
   }
   if(seqId) {
    loadSelect(); 
    for(var i=0; i<sortParentSel.options.length; i++) {
      if(sortParentSel.options[i].value == sortParent) {
        sortParentSel.options[i].selected = true;
      }
    }
    var url = "<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowSortAct/getFlowSort.act";
    var rtJson = getJsonRs(url, "seqId=" + seqId);
    if (rtJson.rtState == "0") {
      bindJson2Cntrl(rtJson.rtData);
    }else {
      alert(rtJson.rtMsrg); 
    }
  } else {
    if (!isAdmin) {
      $('deptId').value = deptId;
    }
  }
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
function addDept() {
  var URL="<%=contextPath %>/core/funcs/workflow/flowsort/deptlist.jsp";
  openDialog(URL,'300', '350');
}

function clearDept() {
  document.getElementById("deptIdDesc").value = "";
  document.getElementById("deptId").value = "";
}

function check() {
  var cntrl = document.getElementById("sortNo")
  if(!cntrl.value) {
	alert("流程分类排序号不能为空！");
	cntrl.focus();
	return false;
  }
  var reg1 = /[^\d]/g;
  if (cntrl.value.match(reg1)) {
    alert("流程分类排序号只能为数字！");
    cntrl.focus();
  	return false;
  }  
  
  cntrl = document.getElementById("sortName");
  if(!cntrl.value) {
  	alert("流程分类名称不能为空！");
  	cntrl.focus();
  	return false;
  }
  if (cntrl.value.match(reg)) {
    alert("流程分类名称不能有\"'\"和\"\"\"字符！");
    cntrl.focus();
  	return false;
  }  
  if (seqId) {
    if ($('sortParent').value == seqId) {
      alert("父分类不能是分类本身！");
      return false;
    }
  }
  cntrl = document.getElementById("deptId");
  if(!cntrl.value) {
  	alert("所属部门不能为空！");
  	//cntrl.focus();
  	return false;
  }
  return true;
}

function commitItem() {
  if(!check()) {
    return;
  }
  if(seqId) {
    var url = "<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowSortAct/updateFlowSort.act";
    var rtJson = getJsonRs(url, mergeQueryString($("sortflowInfoForm")));
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg);
      window.location = "<%=contextPath %>/core/funcs/workflow/flowsort/flowsortlist.jsp";
    }else {
      alert(rtJson.rtMsrg); 
    }    
  }else {
    var url = "<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowSortAct/addFlowSort.act";
    var rtJson = getJsonRs(url, mergeQueryString($("sortflowInfoForm")));
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg);
      loadSelect();
      $("sortflowInfoForm").reset();
    }
  }
}
function changeSortParent(){
  
}

</script>
</head>
<body onload="doInit()">
<form id="sortflowInfoForm" name="sortflowInfoForm" method="post">
  <%
    if(seqId.equals("")) {
  %>   
   <img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"/> <span class="big3"> &nbsp;添加流程分类</span>
  <%
    }else {
  %>    
     <img src="<%=imgPath %>/edit.gif"/><span class="big3">&nbsp;修改流程分类</span>
  <%
    }
  %>
  <input type="hidden" id="seqId" name="seqId" value="" />
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.workflow.data.YHFlowSort"/>
  <div align=center>
  <table  cellpadding="3" width="400px" class=TableList>
    <tr class="TableLine1" >
      <td class="TableContent" align="left">流程父分类：</td>
      <td class="TableData" align=left>
        <select id="sortParent" name="sortParent"  style="width:150px"  onchange="changeSortParent()">
          <option value="0">流程分类</option>
        </select>（为空为一级分类）
      </td>
    </tr>  
    
  <tr class="TableLine2">
    <td class="TableContent" align="left">流程分类排序号：</td>
    <td class="TableData"  align=left>
      <font style="color:red">*</font>
      <input type="text" id="sortNo" name="sortNo" size="4" maxlength="100" class="BigInput" value=""
         onkeyup="value=value.replace(/[^\d]/g,'')"/>
  	</td>
  </tr>
  
  <tr class="TableLine1">
    <td class="TableContent" align="left">流程分类名称：</td>
    <td class="TableData"  align=left>
      <font style="color:red">*</font>
      <input type="text"   id="sortName" name="sortName" size="30" maxlength="100" class="BigInput" value=""/>
  	</td>
  </tr>
  <% 
if (isAdmin) { 
%>
  <tr class="TableLine2">
    <td class="TableContent" align="left">所属部门：</td>
    <td class="TableData"  align=left><font style="color:red"></font>
      <select id="deptId" name="deptId">
      <option value="0">所有部门</option>
      
      </select>
    </td>
  </tr> 
 <%} %>
  <tr class="TableControl">
    <td colspan="2" align="center">
     <% if (!isAdmin) { %>
      <input type='hidden' value="" name="deptId" id="deptId">
      <%} %>
      <input type="button" value="提交" class="SmallButtonW" onclick="commitItem()">
      <input type="button" value="返回" class="SmallButtonW" onclick="location='<%=contextPath %>/core/funcs/workflow/flowsort/flowsortlist.jsp'">
    </td>
  </tr>

  </table>
  </div>
</form>
</body>
</html>