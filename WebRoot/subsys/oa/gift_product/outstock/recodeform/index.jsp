 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>礼品登记报表</title>
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
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="/yh/core/js/orgselect.js" ></script>

<script type="text/javascript"> 
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
function Sum_Report(flag){
  if(checkForm()){
    var giftType = $("giftType").value;
    var giftId = $("giftId").value;
    var fromDate = $("fromDate").value;
    var toDate = $("toDate").value;
    if(flag==0){
    	 URL="report.jsp?fromDate="+fromDate+"&toDate="+toDate+"&giftId="+giftId+"&giftType="+giftType;
      myleft=(screen.availWidth-500)/2;
      window.open(URL,"report","height=500,width=720,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
    }
    if(flag==1){
    	 URL="dept_sum.jsp?fromDate="+fromDate+"&toDate="+toDate+"&giftId="+giftId+"&giftType="+giftType;
      myleft=(screen.availWidth-500)/2;
      window.open(URL,"dept_Sum","height=500,width=680,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
    }
    if(flag==6){
    	 URL="product_info.jsp?fromDate="+fromDate+"&toDate="+toDate+"&giftId="+giftId+"&giftType="+giftType;
      myleft=(screen.availWidth-500)/2;
      window.open(URL,"discard","height=500,width=550,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
    }
  }
  
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
    myOption.text = giftName+"/数量" +giftQty;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}
</script>
</head>
<body topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/new_email[1].gif" WIDTH="18" HEIGHT="18"  align="absmiddle"><span class="big3"> 礼品登记报表</span>
    </td>
  </tr>
</table>
<br>
<div align="center">
 <form method="post" name="form1" id="form1">
<table class="TableBlock" align="center" >

 	<tr align="left">
    <td nowrap class="TableContent">礼品类别：</td>
     <td class="TableData"  > 
        <select id="giftType" name="giftType" class="" onChange="getInstock();">
              <option value=""></option>
         </select>
       </td>
    </tr>
  	<tr  align="left">
      <td nowrap class="TableContent">礼品：</td>
      <td class="TableData">
        <select id="giftId" name="giftId" class="">
           <option value=""></option>
 
                      </select>
       </td>
   </tr>
   <tr align="left">
    <td nowrap class="TableContent">日期： </td>
    <td nowrap class="TableData">
        <input type="text" id="fromDate" name="fromDate" class="BigInput" size="15" maxlength="10" value="">&nbsp;
        <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
    &nbsp;至&nbsp;
       <input type="text" id="toDate" name="toDate" class="BigInput" size="15" maxlength="10" value="">&nbsp;
       <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableHeader" colspan="2" align="left">
     报表类型
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableLine1" colspan="2" align="left">
      <a href="javascript:Sum_Report('6');">物品总表</a>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableLine1" colspan="2" align="left">
      <a href="javascript:Sum_Report('0');">采购物品报表</a>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableLine2" colspan="2" align="left">
      <a href="javascript:Sum_Report('1');">部门、人员领用物品报表</a>
    </td>
   </tr>

</table>
</form>
</div>
</body>
</html>