<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>礼品退库</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/workflow/workflowUtility/utility.js" ></script>

<script type="text/javascript"> 
function CheckForm(){
  if(document.form1.user.value==""&&document.form1.userDesc.value==""){      
  	 alert( "领用人不能为空！");
  	document.form1.userDesc.focus();
  	document.form1.userDesc.select();
    return (false);
  }
 if(document.form1.transQty.value=="") { 
    alert("数量不能为空！");
    form1.transQty.focus();
    form1.transQty.select();
    return (false);
 }
 var IsInt = "^[0-9]*[1-9][0-9]*$";//正整数　
 var IsInt2 =   "^-?\\d+$";//整数
 var re2 = new RegExp(IsInt2);
 if(document.getElementById("transQty").value.match(re2) == null ){
   alert("数量应为整数!");
   document.getElementById("transQty").focus();
   document.getElementById("transQty").select();
   return false;
 }
  return true;
}
function doOnload(){
  var seqId = '<%=seqId%>';
  var giftId = getOutstockBack(seqId);
}
function getOutstockBack(seqId){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/outstock/act/YHGiftOutstockAct/getOutstockById.act?seqId="+seqId;
    var json = getJsonRs(requestURL); 
      //alert(rsText);
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    var prc = json.rtData;
    var giftId = "";
    if(prc.seqId){
     var seqId = prc.seqId;
     var transQty = prc.transQty;
     var transFlag = prc.transFlag;
     $("seqId").value = seqId;
     $("user").value = prc.transUser;
     $("transQty").value = prc.transQty;
     $("giftName").value = prc.giftName;
     $("giftType").value = prc.giftType;
     $("userDesc").value = prc.transUserName;
     $("transFlag").innerHTML = "不能小于本次退回数量:" + prc.transFlag;
     giftId = prc.giftId;
     var useTrans = 0;
     if(transQty==''){
       transQty = 0;
     }
     if(transFlag==''){
       transFlag = 0;
     }
     $("transUses").value = prc.transUses;
     $("transMemo").value = prc.transMemo;;
    }
    return giftId;
}
function Init(){
  if(CheckForm()){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/outstock/act/YHGiftOutstockAct/updateGiftstockInfo.act";
    var json = getJsonRs(requestURL,mergeQueryString($("form1"))); 
      //alert(rsText);
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    var prc = json.rtData;
    var type = prc.type;
    var giftQty = prc.giftQty;
    if(type==2){
      alert("领用数量不能小于退回数量！");
      form1.transQty.focus();
      form1.transQty.select();
    }else if(type==3){
      alert("领用数量不能大于库存数量:"+giftQty+"！");
      form1.transQty.focus();
      form1.transQty.select();
    }else{
      alert("保存成功！");
    }
  }
}

function returnOutstock(){
  history.go(-1);//window.location.href = "<%=contextPath%>/subsys/oa/gift_product/outstock/recode.jsp";
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<div id="bodyDiv" >
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;新建领用登记</span>
    </td>
  </tr>
</table>
<br>
  <form enctype="multipart/form-data" action="#"  method="post" id="form1" name="form1">
    <input type="hidden" id="dtoClass" name="dtoClass" value="yh.subsys.oa.giftProduct.outstock.data.YHGiftOutstock"/>
<table width="70%" align="center" class="TableBlock">

    <tr>
      <td nowrap  class="TableData" style="">领用人：</td>
      <td class="TableData">
       <input type="hidden" name="user" id="user" value="" />
      <textarea name="userDesc" id="userDesc" class="BigStatic" rows="1" cols="10" disabled ></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['user','userDesc']);">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
	  </td>
    </tr>
   <tr>
      <td nowrap class="TableData">礼品类别：</td>
      <td class="TableData">
          <input type="text" name="giftType" id="giftType" class="BigStatic" value="" disabled></input> 
       </td>
   </tr>
  	<tr>
      <td nowrap class="TableData">礼品：</td>
      <td class="TableData">
         <input type="text" name="giftName" id="giftName" class="BigStatic" value="" disabled></input> 
    </td>
      </tr>
   <tr>
      <td nowrap class="TableData">数量：</td>
      <td class="TableData">
        <input type="text" name="transQty" id="transQty" size="10" maxlength="20" class="BigInput" value="" >
		<font color="red" id="transFlag"></font>
		<font color=red></font>				
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">礼品用途：</td>
      <td class="TableData">
        <textarea id="transUses" name="transUses" cols="45" rows="3" class="BigInput"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData">
        <textarea id="transMemo" name="transMemo" cols="45" rows="5" class="BigInput"></textarea>
      </td>
    </tr>
    <tfoot align="center" class="TableFooter">
      <td colspan="2" nowrap>
        <input type="hidden" id="seqId" name="seqId" value="">
        <input type="button" value="确认" class="BigButton" onClick="Init();">&nbsp;&nbsp;
         <input type="button"  value="返回" class="BigButton" onClick="returnOutstock();">
      </td>
    </tfoot>
  </table>
</form>
</div>

</body>
</html>
