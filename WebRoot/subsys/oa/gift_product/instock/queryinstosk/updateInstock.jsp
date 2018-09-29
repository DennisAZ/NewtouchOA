<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId==null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>礼品信息编辑</title>
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
  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/getInstockById.act?seqId="+seqId; 
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prc = json.rtData;
  var giftTypeStr = "";
  var deptIdStr = "";
  if(prc.seqId){
    giftTypeStr = prc.giftType;
    deptIdStr = prc.deptId;
    $("giftName").value=prc.giftName;
    $("giftDesc").value=prc.giftDesc;
    $("giftCode").value=prc.giftCode;
    $("giftUnit").value=prc.giftUnit;
    $("giftPrice").value=prc.giftPrice;
    $("giftQty").value=prc.giftQty;
    $("seqId").value = prc.seqId;
    $("giftCreator").value=prc.giftCreator;
    $("giftCreatorName").value=prc.giftCreatorName;
    $("giftKeeper").value=prc.giftKeeper;
    $("giftSupplier").value = prc.giftSupplier;
    $("giftKeeperName").value=prc.giftKeeperName;
    $("giftMemo").value = prc.giftMemo
  }
  //礼品类别
  giftType(giftTypeStr);
  //所有部门;
  getDept(deptIdStr);
}
function giftType(giftTypeStr){
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
    if(giftTypeStr==seqId){
      myOption.selected = true;
    }
  }
}
function  getDept(deptIdStr){
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
    if(deptIdStr==prc.value){
      option.selected = true;
    }
  }
  if(user!=""){
    $("giftCreator").value = user.split(",")[0];
    $("giftCreatorName").value = user.split(",")[1];
  }

}
function CheckForm(){
   if(document.form1.giftName.value=="")
   { alert("礼品名称不能为空！");
   	 form1.giftName.focus();
  	 form1.giftName.select();
     return (false);
   }
 
    if(document.form1.giftUnit.value=="")
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
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/upateGiftInstock.act";
    var rtJson = getJsonRs(requestUrl,mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    //alert("礼品编辑成功！");
    $("form1").style.display = "none";
    $("returnDiv").style.display = '';
  }
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
</script>
</head>
 
<body class="" topmargin="5" onload="doOnload();">
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
        <input type="text" id="giftCode" name="giftCode" class="BigInput" size="33" maxlength="100" value="20100612193602">
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
        <input type="text" id="giftPrice" name="giftPrice" class="BigInput" size="30" maxlength="16" value="">(元)&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">数量：  </td>
    <td nowrap class="TableData">
        <input type="text" name="giftQty" id="giftQty" class="BigInput" size="33" maxlength="25" value="">
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
        &nbsp;       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['giftKeeper','giftKeeperName']);">添加</a>
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
     <input type="hidden" value="确定" class="BigButton" name="seqId" id="seqId"></input>
        <input type="button" value="确定" class="BigButton" name="button" onclick="Init();" ></input>
                <input type="button" value="返回" class="BigButton" name="button" onclick="history.go(-1);" ></input>
    </td>
   </tfoot>

</table>
  </form>
  <div id="returnDiv" style="display:none" align="center">
<table class="MessageBox" align="center" width="290">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">编辑成功！</div>
    </td>
  </tr>
</table>
<div align="center">
	<input type="button" value="返回" class="BigButton" onClick="history.go(-1);">
</div>

  </div>
</body>
</html>
