<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
String proSeqId = request.getParameter("proSeqId");
String typeId = request.getParameter("typeId");
String storeId = request.getParameter("storeId");
String undefindTypeFlag = request.getParameter("undefindTypeFlag");
if(YHUtility.isNullorEmpty(proSeqId)){
	proSeqId = "0";
}
if(YHUtility.isNullorEmpty(typeId)){
	typeId = "0";
}
if(YHUtility.isNullorEmpty(storeId)){
	storeId = "0";
}
if(YHUtility.isNullorEmpty(undefindTypeFlag)){
	undefindTypeFlag = "0";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>办公用品编辑</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/officeProduct/js/productLogic.js"></script>
<script type="text/javascript">
function doInit(){
	
	var typeId = "<%=typeId%>";
	getproEditDepositoryNames("officeDepository",typeId);
	depositoryOfType(typeId,typeId);

	var requestURI = "<%=contextPath%>/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
	var url = requestURI + "/getOfficeProductsById.act";
	var rtJson = getJsonRs(url,"proSeqId=<%=proSeqId%>");
	if(rtJson.rtState == "0"){
		var prcs = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		getPersonName("proCreator");
		getPersonName("proAuditer");
		getPersonName("proManager");
		getDeptName("proDept");
		
	}else{
		alert(rtJson.rtMsrg);
	}
}

function getPersonName(personIdDiv){
	if($(personIdDiv) && $(personIdDiv).value.trim()){
		bindDesc([{cntrlId:personIdDiv, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
	}
}
function getDeptName(deptIdDiv){
	if($(deptIdDiv) && $(deptIdDiv).value.trim() && $(deptIdDiv).value != "0" && $(deptIdDiv).value != "ALL_DEPT"){
		bindDesc([{cntrlId:deptIdDiv, dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
	}else if($(deptIdDiv) && ($(deptIdDiv).value == "0" || $(deptIdDiv).value == "ALL_DEPT")){
		$(deptIdDiv).value = "0";
		$(deptIdDiv+"Desc").innerHTML = "全体部门";
	}
}

function deleteVote(){
	var msg = "确认删除该用品吗？删除后该用品的全部信息将被清除！";
	if(window.confirm(msg)){
		var requestURI = "<%=contextPath%>/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
		var url = requestURI + "/delOfficeProductsById.act";
		var rtJson = getJsonRs(url,"proSeqId=<%=proSeqId%>");
		if(rtJson.rtState == "0"){
			var prcs = rtJson.rtData;
			parent.file_tree.location.reload();
			//var curTree = parent.frames["file_tree"].tree;
			$("showFormDiv").style.display = "none";
			$("returnDiv").style.display = '';
		}else{
			alert(rtJson.rtMsrg);
		}
	}
}

function checkForm(){
	var proName = $("proName").value.trim();
	var proUnit = $("proUnit").value.trim();
	var officeDepository = $("officeDepository").value.trim();
	var officeProtype = $("officeProtype").value.trim();
	var proStock = $("proStock").value.trim();
	if(proName == ""){
		alert("办公用品名称不能为空！");
		$("proName").focus();
		$("proName").select();
		return false;
	}
	if(officeDepository == ""){
		alert("办公用品库不能为空！");
		$("officeDepository").focus();
		$("officeDepository").select();
		return false;
	}
	if(officeProtype == "" || officeProtype == "-1"){
		alert("办公用品类别不能为空！");
		$("officeProtype").focus();
		$("officeProtype").select();
		return false;
	}
	if(proUnit == ""){
		alert("计量单位不能为空！");
		$("proUnit").focus();
		$("proUnit").select();
		return false;
	}
	if(proStock && (!isPositivInteger(proStock) && proStock != "0")){
		alert("当前库存应为正整数！");
		$("proStock").focus();
		$("proStock").select();
		return false;
	}
	return true;
}

function doSubmit(){
	if(checkForm()){
		var requestURI = "<%=contextPath%>/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
		var pars = Form.serialize($('form1'));
		var url = requestURI + "/updateOfficeProductsById.act?proSeqId=<%=proSeqId%>";
		var rtJson = getJsonRs(url,pars);
		if(rtJson.rtState == "0"){
			var prcs = rtJson.rtData;
			var isHave = prcs.isHave;
			if(isHave=="1"){
				$("showFormDiv").hide();
				$("remindInfo").innerHTML = "同类别下已有相同名称的办公用品！"
				$("remindDiv").show();
			}else{
			//重新加载树
				var undefindTypeFlag = "<%=undefindTypeFlag%>";
				//alert(undefindTypeFlag);
				if(undefindTypeFlag == "1"){
					parent.frames["file_tree"].location.reload();
				}else{
					//parent.file_tree.location.reload();
					var curTree = parent.frames["file_tree"].tree;
					var curNode = curTree.getNode('<%=proSeqId%>');  
					if(curNode){
						curNode.name = prcs.proName;
						curTree.updateNode(curNode.nodeId, curNode);
					}else{
						parent.frames["file_tree"].location.reload();
					}
				}
				$("showFormDiv").hide();
				$("remindInfo").innerHTML = "办公用品修改成功！"
				$("remindDiv").show();
			}
		}else{
			alert(rtJson.rtMsrg); 
		}
	}
}

function showForm(){
	window.location.reload();
}

</script>
</head>
<body onload="doInit();">
<div id="showFormDiv">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 办公用品信息编辑 </span>
    </td>
  </tr>
</table>
<form action=""  method="post" name="form1" id="form1">
<table width="500" class="TableBlock" align="center" >
   <tr>
    <td nowrap class="TableData">办公用品名称：<font color="red">*</font> </td>
    <td nowrap class="TableData">
        <input type="text" name="proName" id="proName" class="BigInput" size="33" maxlength="100" value="">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">办公用品描述： </td>
    <td nowrap class="TableData">
    	   <textarea cols=37 name="proDesc" id="proDesc" rows="2" class="BigInput" wrap="yes"></textarea></td>
   </tr>
  <tr>
    <td nowrap class="TableData">办公用品库： </td>
    <td nowrap class="TableData">
      <select name="officeDepository" id="officeDepository"  onchange = "depositoryOfType(this.value);">
	 		 <option value="">请选择</option>
      </select>
    </td>
   </tr>
    <tr>
    <td nowrap class="TableData">办公用品类别：</td>
    <td nowrap class="TableData"  id = "OFFICE_TYPE">
		<select name="officeProtype" id="officeProtype" >
			<option value="-1">请选择</option>
		</select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">办公用品编码： </td>
    <td nowrap class="TableData">
        <input type="text" name="proCode" id="proCode" class="BigInput" size="33" maxlength="100" value="">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">计量单位：<font color="red">*</font> </td>
    <td nowrap class="TableData">
        <input type="text" name="proUnit" id="proUnit" class="BigInput" size="33" maxlength="50" value="">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">单价： </td>
    <td nowrap class="TableData">
        <input type="text" name="proPrice" id="proPrice" class="BigInput" size="30" maxlength="100" value="">(元)&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">供应商： </td>
    <td nowrap class="TableData">
        <input type="text" name="proSupplier" id="proSupplier" class="BigInput" size="33" maxlength="25" value="">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">最低警戒库存： </td>
    <td nowrap class="TableData">
        <input type="text" name="proLowstock" id="proLowstock" class="BigInput" size="33" maxlength="25" value="">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">最高警戒库存： </td>
    <td nowrap class="TableData">
        <input type="text" name="proMaxstock" id="proMaxstock" class="BigInput" size="33" maxlength="25" value="">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">当前库存： </td>
    <td nowrap class="TableData">
        <input type="text" name="proStock" id="proStock" class="BigInput" size="33" maxlength="8" value="">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">创建人：</td>
    <td nowrap class="TableData">
      <input type="hidden" name="proCreator" id="proCreator" value="" >
      <input type="text" name="proCreatorDesc" id="proCreatorDesc" class="BigStatic" readonly size="10" maxlength="100" value="">&nbsp;&nbsp;创建人可以修改自己创建的办公用品信息。
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">审批权限（用户）：</td>
    <td nowrap class="TableData">
      <input type="hidden" name="proAuditer" id="proAuditer" value="">
      <textarea cols=37 name="proAuditerDesc" id="proAuditerDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectUser(['proAuditer', 'proAuditerDesc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('proAuditer').value='';$('proAuditerDesc').value='';">清空</a>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">登记权限（用户）：</td>
    <td nowrap class="TableData">
      <input type="hidden" name="proManager" id="proManager" value="">
      <textarea cols=37 name="proManagerDesc" id="proManagerDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectUser(['proManager', 'proManagerDesc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('proManager').value='';$('proManagerDesc').value='';">清空</a>
    </td>
   </tr>
   <tr>
     <td nowrap class="TableData">登记权限（部门）：</td>
     <td class="TableData" >
       <input type="hidden" name="proDept" id="proDept" value="">
      <textarea cols=37 name="proDeptDesc" id="proDeptDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectDept(['proDept', 'proDeptDesc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('proDept').value='';$('proDeptDesc').value='';">清空</a>
       <br>有登记权限的用户或部门，可以申请领用、借用该办公用品，都不填写，表示所有部门均有权限。
     </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="button" value="确定" class="BigButton" title="" onclick="doSubmit();">&nbsp;
        <input type="button" value="删除" class="BigButton" title="删除办公用品" onclick="deleteVote();">&nbsp;
    </td>
   </tr>
</table>
</form>
</div>
<div id="remindDiv" style="display: none">
<table class="MessageBox" align="center" width="370">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt"><span id="remindInfo"></span></div>
    </td>
  </tr>
</table>
<br><center>
	 <input type="button" class="BigButton" value="返回" onclick="showForm();">
</center>
</div>
  <div id="returnDiv" style="display:none" align="center">
<table class="MessageBox" align="center" width="290">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">删除成功！</div>
    </td>
  </tr>
</table>
<div align="center">
	<input type="button" value="返回" class="BigButton" onClick="window.location.href='<%=contextPath %>/subsys/oa/officeProduct/product/new.jsp';">
</div>
</div>

</body>
</html>