<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String vId = request.getParameter("vId");
  String type = request.getParameter("type");
  if(vId == null){
    vId = "";
  }
  if(type==null){
    type = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>添加维护提醒</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script Language="JavaScript"> 
function CheckForm(){

  var remindTime = document.getElementById("vmRequestDate");
  var remindTimeArray  = remindTime.value.trim().split(" ");
  var type1 =   "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　;
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2);
  if(remindTime.value==''){
    alert("请指定维护时间！");
    remindTime.focus();
    remindTime.select();
    return false;
  }
  if(remindTime.value!=""&&remindTimeArray.length!=2){
    alert("维护时间应为日期时间型，如：1999-01-01 10:20:10");
    remindTime.focus();
    remindTime.select();
    return false;
  }else{
    if(remindTime.value!=""&&(!isValidDateStr(remindTimeArray[0])||remindTimeArray[1].match(re1) == null ||remindTimeArray[1].match(re2) != null)){
      alert("维护时间应为日期时间型，如：1999-01-01 10:20:10");
      remindTime.focus();
      remindTime.select();
      return false;
    }
  }
  var IsInt = "^[0-9]*[1-9][0-9]*$";//正整数　
  var IsInt1 = "^[0-9][0-9]*$";//0和正整数　
  var IsInt2 =   "^-?\\d+$";//整数"^\\d+$"　
  var IsInt3 = "^[0-9]*\\.?[0-9]*$";//0以上的数
  var re = new RegExp(IsInt1);
  if($("beforeDay").value!=''&&$("beforeDay").value.match(re)==null){
     alert("提前的天数应为整数！");
     $("beforeDay").focus();
     $("beforeDay").select();
     return false;
  }
  if($("beforeHour").value!=''&&$("beforeHour").value.match(re)==null){
    alert("提前的小时应为整数！");
    $("beforeHour").focus();
    $("beforeHour").select();
    return false;
  }
  if($("beforeMin").value!=''&&$("beforeMin").value.match(re)==null){
    alert("提前的分钟应为整数！");
    $("beforeMin").focus();
    $("beforeMin").select();
    return false;
  }
}
var type = '<%=type%>';
function doOnload(){
  if(type!=1){
  //短信
    var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=9";
    var rtJson = getJsonRs(requestUrl);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    var prc = rtJson.rtData;
    var allowRemind = prc.allowRemind;;//是否允许显示
  //手机
    var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=9"; 
    var rtJson = getJsonRs(requestUrl); 
    if(rtJson.rtState == "1"){ 
      alert(rtJson.rtMsrg); 
      return ; 
    } 
    var prc = rtJson.rtData; 
    var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 

    if(allowRemind=='2'&&(moblieRemindFlag!='2'&&moblieRemindFlag!='1')){
       $("remindMain").style.display = "none";
       $("NoRemind").style.display = "";
    }else{
      //短信
      var defaultRemind = prc.defaultRemind;;//是否默认选中
      var mobileRemind = prc.mobileRemind;//手机默认选中
      if(allowRemind=='2'){
        $("smsRemindDiv").style.display = 'none';
      }else{
        if(defaultRemind=='1'){
          $("smsRemind").checked = true;
        }
      }
      //手机
      if(moblieRemindFlag == '2'){ 
        $("moblieSmsRemindDiv").style.display = ''; 
        $("moblieSmsRemind").checked = true; 
      }else if(moblieRemindFlag == '1'){ 
         $("moblieSmsRemindDiv").style.display = ''; 
         $("moblieSmsRemind").checked = false; 
      }else{ 
        $("moblieSmsRemindDiv").style.display = 'none'; 
      } 
    }
    var date1Parameters = {
        inputId:'vmRequestDate',
        property:{isHaveTime:true}
        ,bindToBtn:'date'
    };
    new Calendar(date1Parameters);
    doOperator();
  }
  
}

//调度人员名称
function doOperator() {
  var requestURLs = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleOperatorAct/selectManagerPerson.act"; 
  var rtJson = getJsonRs(requestURLs); 
  if(rtJson.rtState == '1') {
    alert(rtJson.rtMsrg); 
    return ; 
  }
  var prcsJsons = rtJson.rtData; 
  var selects = document.getElementById("vuOperator"); 
  for(var i = 0;i < prcsJsons.length; i++){ 
    var option = document.createElement("option"); 
    option.value = prcsJsons[i].seqId; 
    option.text = prcsJsons[i].userName;
    var selectObj = $('vuOperator');
    selectObj.options.add(option,selectObj.options ? selectObj.options.length : 0); 
  }
}
</script>
</head>
<body topmargin="5" onload="doOnload();">
<%if(!type.equals("1")){ %>
<div id="remindMain">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif"><span class="big3">添加维护提醒</span>
    </td>
  </tr>
</table>
 <form action="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/remiend.act" method="post" name="form1" onsubmit="return CheckForm();">
<table class="TableBlock" align="center">
    <tr>
      <td nowrap class="TableContent"> 维护时间：</td>
      <td class="TableData">
        <input type="text" id="vmRequestDate" name="vmRequestDate" size="20" maxlength="19" class="BigInput" value=""">
            <img id="date" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer"  align="absMiddle">
       
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 维护类型：</td>
      <td class="TableData">
        <SELECT name="vmType" >
   <option value="0" >维修</option>
   <option value="1" >加油</option>
   <option value="2" >洗车</option>
   <option value="3" >年检</option>
   <option value="4" >其它</option>
        </SELECT>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" > 维护原因：</td>
      <td class="TableData" colspan="3">
        <textarea name="vmReason" class="BigInput" cols="45" rows="3"></textarea>
      </td>
    </tr>
    <tr>
   <td nowrap class="TableContent" width="110"> 提醒调度人员：</td>
      <td class="TableData" colspan="3">
        <select name="vuOperator" id="vuOperator">
        </select>
      </td>   
   </tr>
    <tr>
    <td nowrap class="TableContent" width="80">提醒设置： </td>
    <td class="TableData" colspan="3">
   <span id="smsRemindDiv">
      <input type="checkbox" id="smsRemind" name="smsRemind" ><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;
   </span>  
   <span id="moblieSmsRemindDiv">
     <input type="checkbox" id="moblieSmsRemind" name="moblieSmsRemind" ><label for="smsRemind">使用手机短信提醒</label>&nbsp;&nbsp;
   </span>    
  </td>
  </tr>
    <tr>
      <td nowrap class="TableContent" > 提醒设置：</td>
      <td class="TableData" colspan="3">提前
 <input type="text"id="beforeDay" name="beforeDay" size="2" class="BigInput" value="" maxlength="5"> 天

        <input type="text" id="beforeHour" name="beforeHour" size="2" class="BigInput" value="" maxlength="5"> 小时
        <input type="text" id="beforeMin" name="beforeMin" size="2" class="BigInput" value="10" maxlength="5"> 分钟提醒
 
      </td>
    </tr>
    <tr class="TableControl">
      <td nowrap colspan="4" align="center">
        <input type="hidden" name="vId" value="<%=vId %>">
        <input type="submit" value="保存" class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="window.close();">
      </td>
    </tr>
    </table>
</form>
 </div>
 <div id="NoRemind" style="display:none">
<table class="MessageBox" align="center" width="220">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">未设置该模块为提醒</div>
    </td>
  </tr>
</table>
  <center><input type="button" value="关闭" class="BigButton" onClick="window.close();"></center>
 
 </div>
 <%}else{ %>
 
<table class="MessageBox" align="center" width="220">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">保存成功</div>
    </td>
  </tr>
</table>
  <center><input type="button" value="关闭" class="BigButton" onClick="window.close();"></center>
 <%} %>

</body>
</html>
