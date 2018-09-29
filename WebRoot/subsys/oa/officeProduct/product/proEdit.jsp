<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
String typeId = request.getParameter("typeId");
String storeId = request.getParameter("storeId");
if(YHUtility.isNullorEmpty(storeId)){
	storeId = "0";
}
if(YHUtility.isNullorEmpty(typeId)){
	typeId = "0";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>办公用品类别</title>
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
	var extData = "<%=typeId%>";
	var storeId = "<%=storeId%>";
	getproEditDepositoryNames("officeDepository",extData);
	//depositoryOfType(extData,extData)
	getTypeNamesByStoreId2(storeId,extData);
}

/**
 * 获取办公用品类别(根据库的id)
 * @param typeId
 * @return
 */
function getTypeNamesByStoreId2(storeId,extData){
	//alert(storeId);
	var selects = document.getElementById("officeProtype");
	selects.length =0;
	if(storeId){
		var urlStr = contextPath + "/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
		var url = urlStr + "/getTypeNamesByStoreId.act";
		var rtJson = getJsonRs(url,"storeId=" + storeId);
		//alert(rsText);
		if (rtJson.rtState == "1") {
			alert(rtJson.rtMsrg);
			return;
		}
		var prcs = rtJson.rtData;
		for ( var i = 0; i < prcs.length; i++) {
			var prc = prcs[i];
			var option = document.createElement("option");
			option.value = prc.typeId;
			option.innerHTML = prc.name;
			selects.appendChild(option);
			if (extData && (extData == prc.typeId)) {
				option.selected = true;
			}
		}
	}else{
		//selects.length =1;
	}
}



function checkForm(){
	var proName = $("proName").value.trim();
	var proUnit = $("proUnit").value.trim();
	if(proName == ""){
		alert("办公用品名称不能为空！");
		$("proName").focus();
		$("proName").select();
		return false;
	}
	if(proUnit == ""){
		alert("计量单位不能为空！");
		$("proUnit").focus();
		$("proUnit").select();
		return false;
	}
	
	return true;
}

function doSubmit(){
	if(checkForm()){
		var requestURI = "<%=contextPath%>/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
		var pars = Form.serialize($('form1'));
		var url = requestURI + "/addOfficeProducts.act";
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
				var curTree = parent.frames["file_tree"].tree;
				var curNode = curTree.getCurrNode();
					//alert("curTree:"+curNode);
					var nodeId = prcs.maxSeqId;
					var curNodeId = "<%=typeId%>" + ",type,produce";
					var nodeName = prcs.proName;
					//alert("nodeId :"+nodeId);
					//alert("nodeName :"+nodeName);
					var imgAddress = "<%=imgPath%>/4[1].gif";
					var node = {
						parentId:curNodeId,
						nodeId:nodeId,
						name:nodeName,
						isHaveChild:0,
						extData:'',
						imgAddress:imgAddress
					}
					curTree.addNode(node);
				$("showFormDiv").hide();
				$("remindInfo").innerHTML = "办公用品添加成功！"
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
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 新建办公用品</span>
    </td>
  </tr>
</table>
<form action=""  method="post" name="form1" id="form1">
<table width="70%" class="TableBlock" align="center" >
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
      <select name="officeDepository" id="officeDepository" disabled="disabled" onchange = "depositoryOfType(this.value);">
	 		 <option value="">请选择</option>
      </select>
    </td>
   </tr>
    <tr>
    <td nowrap class="TableData">办公用品类别：</td>
    <td nowrap class="TableData"  id = "OFFICE_TYPE">
		<select name="officeProtype" id="officeProtype" >
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
      <input type="hidden" name="proCreator" id="proCreator" value="<%=person.getSeqId() %>" >
      <input type="text" name="proCreatorname" id="proCreatorname" class="BigStatic" readonly size="10" maxlength="100" value="<%=person.getUserName() %>">&nbsp;&nbsp;创建人可以修改自己创建的办公用品信息。
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
        <input type="button" value="新建办公用品" class="BigButtonC" title="新建办公用品" onclick="doSubmit();">&nbsp;
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
</body>
</html>