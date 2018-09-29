 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/menu_left.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="/yh/core/js/prototype.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="/yh/core/js/datastructs.js"></script>
<script type="text/javascript" src="/yh/core/js/sys.js"></script>
<script type="text/javascript" src="/yh/core/js/smartclient.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/Menu.js"></script>
<script language="JavaScript"> 
function checkForm(){
  var date1 = $("fromDate");
  var date2 = $("toDate");
  if(date1.value!=""){
    if(!isValidDateStr(date1.value)){
      alert("起始日期格式不正确,应形如 2010-02-01");
      date1.focus();
      date1.select();
      return false;
      }
  }
  if(date2.value!=""){
    if(!isValidDateStr(date2.value)){
      alert("截止日期格式不正确,应形如 2010-02-01");
      date2.focus();
      date2.select();
      return false;
     }
  }

  var beginInt;
  var endInt;
  var beginArray = date1.value.split("-");
  var endArray = date2.value.split("-");
  for(var i = 0 ; i<beginArray.length; i++){
    beginInt = parseInt(" " + beginArray[i]+ "",10);  
    endInt = parseInt(" " + endArray[i]+ "",10);
    if((beginInt - endInt) > 0){
      alert("起始日期不能大于截止日期!");
      date2.focus();
      date2.select();
      return false;
    }else if(beginInt - endInt<0){
      return true;
    }  
  }
   return (true);
}
function doOnload(){
  giftType();
  getInstock();

  var date1Parameters = {
      inputId:'fromDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'toDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
}
function giftType(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/getCodeItem.act?GIFT_PROTYPE=GIFT_PROTYPE"; 
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcsJson = json.rtData;
  var selectObj = document.getElementById("giftType");
  //selectObj.options.length=1;
  //document.all.selectObj.options.length = 0;  
  for(var i = 0;i<prcsJson.length;i++){
    var prc = prcsJson[i];
    var seqId = prc.sqlId;
    var classNO = prc.classNo;
    var calssDesc = prc.classDesc;
    var myOption = document.createElement("option");
    myOption.value = seqId;
    myOption.text = calssDesc;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}
function getInstock(){
  var giftType = "";
  giftType = $("giftType").value;
  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/getInstockByGiftType.act?giftType="+giftType;
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcsJson = json.rtData;
  var selectObj = document.getElementById("giftId");
  selectObj.options.length=1;
  for(var i = 0;i<prcsJson.length;i++){
    var prc = prcsJson[i];
    var seqId = prc.seqId;
    var giftName = prc.giftName;
    var giftQty = prc.giftQty;
    var myOption = document.createElement("option");
    myOption.value = seqId;//+","+giftQty;
    myOption.text = giftName+"/现存" +giftQty;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}
function Init(){
  if(checkForm()){
    var giftType = $("giftType").value;
    var giftId = $("giftId").value ;
    var giftName = $("giftName").value;
    var fromDate = $("fromDate").value;
    var toDate = $("toDate").value;
    var requestURL = "<%=contextPath%>/subsys/oa/gift_product/outstock/recodequery/showquery.jsp?giftType="+giftType+"&giftId="+giftId+"&giftName="+encodeURIComponent(giftName)+"&fromDate="+fromDate+"&toDate="+toDate;
    parent.location.href = requestURL;   
  }
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif"  align="absmiddle"><span class="big3"> 礼品登记查询</span>
    </td>
  </tr>
</table>
<br>
<div align="center">
<form method="post" id="from1" name="form1">
<table class="TableBlock" align="center" >

    <!--tr>
      <td nowrap  class="TableData" style="">领用人：</td>
      <td class="TableData">
        <input type="hidden" name="TO_ID" value="">
        <input type="text" name="TO_NAME" size="20" class="BigInput" maxlength="20"  value="" readonly>
        &nbsp;<input type="button" value="选 择" class="SmallButton" onClick="SelectUserSingle('','TO_ID','TO_NAME')" title="选择" name="button">
        &nbsp;<input type="button" value="清 空" class="SmallButton" onClick="ClearUser()" title="清空" name="button">
      </td>
    </tr-->
      <tr>
      <td nowrap class="TableData" align="left">礼品类别：</td>
      <td class="TableData" align="left">
        <select name="giftType"  id="giftType" class="" onChange="getInstock();">
           <option value=""></option>
         </select>
       </td>
   </tr>
  	<tr>
      <td nowrap class="TableData" align="left">礼品：</td>
      <td class="TableData" align ="left">
        <select name="giftId" id="giftId" class="">
           <option value=""></option>
         </select> &nbsp;
       </td>
      </tr>
 
    <tr>
    <td nowrap class="TableContent" align ="left">礼品名称(模糊)：</td>
    <td nowrap class="TableData" align ="left">
      <input type="text" name="giftName" id="giftName" class="BigInput" size="15" maxlength="10">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent" align ="left">日期： </td>
    <td nowrap class="TableData" align ="left">
        <input type="text" id="fromDate" name="formDate" class="BigInput" size="15" maxlength="10" value="">&nbsp;
        <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
    &nbsp;至&nbsp;
       <input type="text" id="toDate" name="toDate" class="BigInput" size="15" maxlength="10" value="">&nbsp;
       <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
    </td>
   </tr>
   <tfoot align="center" class="TableFooter">
    <td colspan="2" align="center">
        <input value="查询" class="BigButton" title="模糊查询" type="button" onclick="Init();">
        &nbsp;<input type="hidden" value="导出" class="BigButton" title="导出礼品信息" name="button" onClick="excel_export()">
    </td>
   </tfoot>

</table>
  </form>
</div>
</body>
</html>