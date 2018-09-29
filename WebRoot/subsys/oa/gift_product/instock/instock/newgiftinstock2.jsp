<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  Date date = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
  String seqId = request.getParameter("seqId");
  if(seqId==null){
    seqId = "";
  }
  String curDateStr = dateFormat.format(date);
  //out.print(dateFormat.format(date));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建礼品</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function doOnload(){
  var seqId = '<%=seqId%>';
  getCodeItem(seqId);
  //礼品类别
  giftType(seqId);
  //所有部门;
  getDept();
}
function getCodeItem(seqId){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/getCodeItemById.act?seqId="+seqId; 
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcsJson = json.rtData;
  if(prcsJson.sqlId){
     var seqId = prcsJson.sqlId;
     $("seqId").value = seqId;
     $("codeNO").value = prcsJson.classCode;
     $("codeOrder").value = prcsJson.sortNo;
     $("codeName").value = prcsJson.classDesc;
  }
}
function giftType(seqIdStr){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/getCodeItem.act?GIFT_PROTYPE=GIFT_PROTYPE"; 
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcsJson = json.rtData;
 
  var selectObj = document.getElementById("giftType");
  for(var i = 0;i<prcsJson.length;i++){
    var prc = prcsJson[i];
    var seqId = prc.sqlId;
    var classNO = prc.classNo;
    var calssDesc = prc.classDesc;
    var myOption = document.createElement("option");
    myOption.value = seqId;
    myOption.text = calssDesc;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
    if(seqIdStr==seqId){
      myOption.selected = true;
    }
  }
}
function  getDept(){
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/selectDeptToGift.act?";
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var user = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("deptId");
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  if(user!=""){
    $("giftCreator").value = user.split(",")[0];
    $("giftCreatorName").value = user.split(",")[1];
  }

}
function CheckForm(){
   if(document.form1.giftName.value.trim()=="")
   { alert("礼品名称不能为空！");
   	 form1.giftName.focus();
  	 form1.giftName.select();
     return (false);
   }
 
    if(document.form1.giftUnit.value.trim()=="")
    { alert("计量单位不能为空！");
   	 form1.giftUnit.focus();
  	 form1.giftUnit.select();
     return (false);
   }
    if($("giftPrice").value!=''){
      if(!isNumber($("giftPrice").value)){
        alert("礼品单价不是有效的数字！");
     	 form1.giftPrice.focus();
    	 form1.giftPrice.select();
    	 return false;
      }  
    }
    var IsInt = "^[0-9]*[1-9][0-9]*$";//正整数　
    var IsInt2 =   "^-?\\d+$";//整数
    var re2 = new RegExp(IsInt2);
    if(document.getElementById("giftQty").value.match(re2) == null ){
      alert("数量应为整数!");
      document.getElementById("giftQty").focus();
      document.getElementById("giftQty").select();
      return false;
    }
    return true;
}
function Init(){
  if(CheckForm()){
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/addGiftInstock.act?";
    var rtJson = getJsonRs(requestUrl,mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    var giftType = $("giftType").value;
    var prc = rtJson.rtData;
    var seqId = prc.seqId;
    var giftName = prc.giftName;
    var curTree = parent.frames["file_tree"].tree;
	  //alert("curTree:"+curTree);
	    
	 var curNode = curTree.getCurrNode();
	 // alert("curTree:"+curNode);
	  var nodeId = seqId+",gift";
	  var curNodeId = '';
      //window.location.href = "newChildDimension.jsp?seqId="+seqId;
	  curNodeId = giftType;
	  var nodeName = giftName;
	  //alert("nodeId :"+nodeId);
	  //alert("nodeName :"+nodeName);
	  //var opt = "";
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
    $("bodyDiv").style.display = "none";
    $("returnDiv").style.display = '';
  }
}
function CheckFormType(){
  var codeOrder = $("codeOrder");
  var codeName = $("codeName");

  if(codeOrder.value.trim()==''){
    alert("排序号不能为空！");
    codeOrder.focus();
    codeOrder.select();
    return false;
  }
  if(codeName.value.trim()==''){
    alert("类别名称不能为空！");
    codeName.focus();
    codeName.select();
    return false;
  }

  return true;
}
function isNumber(aValue) {
  var digitSrc = "0123456789";

  if (aValue == null || ("" + aValue).length == 0) {
    return false;
  }
  aValue = "" + aValue;
  if (aValue.substr(0, 1) == "-") {
    aValue = aValue.substr(1, aValue.length - 1);
  }
  var strArray = aValue.split(".");
  // 含有多个“.”

  if (strArray.length > 2) {
    return false;
  }
  var tmpStr = "";
  for (var i = 0; i < strArray.length; i++) {
    tmpStr += strArray[i];
  }

  for (var i = 0; i < tmpStr.length; i++) {
    var tmpIndex = digitSrc.indexOf(tmpStr.charAt(i));
    if (tmpIndex < 0) {
      // 有字符不是数字

      return false;
    }
  }
  // 是数字

  return true;
}
function codeInit(){
  if(CheckFormType()){
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/updateCodeItemById.act";
    var rtJson = getJsonRs(requestUrl,mergeQueryString($("typeForm")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    parent.file_tree.location.reload();
    $("bodyDiv").style.display = "none";
    $("returnType").style.display = '';
  }
}
</script>
</head>
 
<body class="" topmargin="5" onload="doOnload();">
<div id="bodyDiv">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;&nbsp;礼品类别编辑 </span><font color="red">(礼品类别的删除在系统管理的系统代码设置中)</font>
    </td>
  </tr>
</table>
  <form action="#"  id="typeForm" method="post" name="TypeForm" onSubmit="return CheckTypeForm();">
<table width="70%" class="TableBlock" align="center" >

  	<tr>
    <td nowrap class="TableContent">编号： </td>
    <td nowrap class="TableData">
        <input type="text"  readonly="true" id="codeNO" name="codeNo" class="BigInput" size="33" maxlength="40" value="">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">排序号： </td>
    <td nowrap class="TableData">
		<input type="text" name="codeOrder" id="codeOrder" class="BigInput" size="33" maxlength="40" value="">&nbsp;
    </td>
   </tr>
    <tr>
    <td nowrap class="TableContent">类别名称： </td>
    <td nowrap class="TableData">
        <input type="text" name="codeName" id="codeName" class="BigInput" size="33" maxlength="100" value="">&nbsp;
    </td>
   </tr>
   <tfoot align="center" class="TableFooter">
    <td colspan="2" align="center">
		<input type="hidden" name="seqId" id="seqId" value="">
        <input type="hidden" name="OLD_ORDER" value="01">
        <input type="button" value="确定" class="BigButton" onclick="codeInit();">&nbsp;&nbsp;
    </td>
	</tfoot>

</table>
  </form>
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;&nbsp;礼品新建 </span>
    </td>
  </tr>
</table>
  <form action="#" id="form1" method="post" name="form1" >
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.subsys.oa.giftProduct.instock.data.YHGiftInstock"/>
<table width="70%" class="TableBlock" align="center" >

   <tr>
    <td nowrap class="TableContent">礼品名称： </td>
    <td nowrap class="TableData" >
        <input type="text" name="giftName" id="giftName" class="BigInput" size="33" maxlength="100" value="">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">上缴部门： </td>
    <td nowrap class="TableData">
	<select name="deptId" id="deptId" class="">
    </select>
	</td>
   </tr>
   <tr>
    <td nowrap class="TableContent">礼品来源： </td>
    <td nowrap class="TableData">
    	   <textarea cols=37 name="giftDesc" id="giftDesc" rows="2" class="BigInput" wrap="yes"></textarea>
    </td>
   </tr>
    <tr>
    <td nowrap class="TableContent">礼品类别： </td>
    <td nowrap class="TableData">
      <select name="giftType" id="giftType" class="">
   </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">礼品编码： </td>
    <td nowrap class="TableData">
        <input type="text" id="giftCode" name="giftCode" class="BigInput" size="33" maxlength="100" value="<%=curDateStr %>">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">计量单位： </td>
    <td nowrap class="TableData">
        <input type="text" name="giftUnit"  id="giftUnit"class="BigInput" size="33" maxlength="100" value="">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">单价： </td>
    <td nowrap class="TableData">
        <input type="text" id="giftPrice" name="giftPrice" class="BigInput" size="30" maxlength="14" value="">(元)&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">数量：  </td>
    <td nowrap class="TableData">
        <input type="text" name="giftQty" id="giftQty" class="BigInput" size="33" maxlength="9" value="">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">供应商： </td>
    <td nowrap class="TableData">
        <input type="text" name="giftSupplier" id="giftSupplier" class="BigInput" size="33" maxlength="25" value="">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">经手人：</td>
    <td nowrap class="TableData">
      <input type="hidden" name="giftCreator" id="giftCreator" value="admin" >
      <input type="text" name="giftCreatorName" id="giftCreatorName" class="BigStatic" readonly size="10" maxlength="100" value="">&nbsp;&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">保管员：</td>
      <td class="TableData">
        <input type="hidden" id="giftKeeper" name="giftKeeper" value="">
        <input type="text" name="giftKeeperName" id="giftKeeperName" size="20" class="BigStatic" maxlength="20"  value="" readonly>
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['giftKeeper','giftKeeperName']);">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('giftKeeperName').value='';$('giftKeeper').value='';">清空</a>
	  </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">礼品备注： </td>
    <td nowrap class="TableData">
    	   <textarea cols=57 name="giftMemo" id="giftMemo" rows="3" class="BigInput" wrap="yes"></textarea>
    </td>
   </tr>
   <tfoot align="center" class="TableFooter">
    <td colspan="2" align="center">
        <input type="button" value="新建礼品" class="BigButton" name="button" onclick="Init();" ></input>
    </td>
   </tfoot>

</table>
  </form>
  </div>
    <div id="returnType" style="display:none" align="center">
    <table class="MessageBox" align="center" width="470">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">礼品类别信息修改成功！</div>
    </td>
  </tr>
</table>
	<div align="center">
		<input type="button" value="返回" class="BigButton" onClick="window.location.reload();">
	</div>
    
    </div>
  <div id="returnDiv" style="display:none" align="center">
  <table class="MessageBox" align="center" width="350">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">礼品添加成功！</div>
    </td>
  </tr>
</table>
 <br>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="window.location.reload();">
</div>
  </div>
</body>
</html>
