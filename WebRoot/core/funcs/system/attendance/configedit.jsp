<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑上下班时间</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript">
function CheckForm(){
  if(document.getElementById("dutyName").value ==""){ 
    alert("说明不能为空！");
    document.getElementById("dutyName").focus();
    document.getElementById("dutyName").select();
    return (false);
  }
  var type1 =   "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　;
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2);
  if(document.getElementById("dutyTime1").value != ""){
    if(document.getElementById("dutyTime1").value.match(re1) == null || document.getElementById("dutyTime1").value.match(re2) != null) { 
      alert( "第1次登记的时间格式不正确,应形如 8:00:00"); 
      document.getElementById("dutyTime1").focus();
      document.getElementById("dutyTime1").select();
      return false;
    }
  }
  if(document.getElementById("dutyTime2").value != ""){
    if(document.getElementById("dutyTime2").value.match(re1) == null || document.getElementById("dutyTime2").value.match(re2) != null) { 
      alert( "第2次登记的时间格式不正确,应形如 8:00:00"); 
      document.getElementById("dutyTime2").focus();
      document.getElementById("dutyTime2").select();
      return false;
    } 
  }
  if(document.getElementById("dutyTime3").value != ""){
    if(document.getElementById("dutyTime3").value.match(re1) == null || document.getElementById("dutyTime3").value.match(re2) != null) { 
      alert( "第3次登记的时间格式不正确,应形如 8:00:00"); 
      document.getElementById("dutyTime3").focus();
      document.getElementById("dutyTime3").select();
      return false;
    }
  }
  if(document.getElementById("dutyTime4").value != ""){
    if(document.getElementById("dutyTime4").value.match(re1) == null || document.getElementById("dutyTime4").value.match(re2) != null) { 
      alert( "第4次登记的时间格式不正确,应形如 8:00:00"); 
      document.getElementById("dutyTime4").focus();
      document.getElementById("dutyTime4").select();
      return false;
    }
  }
  if(document.getElementById("dutyTime5").value != ""){
    if(document.getElementById("dutyTime5").value.match(re1)== null || document.getElementById("dutyTime5").value.match(re2) != null) { 
      alert( "第5次登记的时间格式不正确,应形如 8:00:00"); 
      document.getElementById("dutyTime5").focus();
      document.getElementById("dutyTime5").select();
      return false;
    }
  } 
  if(document.getElementById("dutyTime6").value != ""){
    if(document.getElementById("dutyTime6").value.match(re1) == null || document.getElementById("dutyTime6").value.match(re2) != null) { 
      alert( "第6次登记的时间格式不正确,应形如 8:00:00"); 
      document.getElementById("dutyTime6").focus();
      document.getElementById("dutyTime6").select();
      return false;
    }
  }
  return (true);
}
function Init(){
  if(CheckForm()){
    var requestURL; 
    var prcsJson; 
    requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendConfigAct/updateConfigById.act"; 
    var json = getJsonRs(requestURL,mergeQueryString($("form1"))); 
    //alert(rsText);
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    history.go(-1);
  }
}
function doOnLoad(){
  var seqId = '<%=request.getParameter("seqId")%>';
  if(seqId != "undefined"){
    var requestURL; 
    var prcs; 
    requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendConfigAct/selectConfigById.act?seqId=" + seqId; 
    var json = getJsonRs(requestURL); 
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    prcs = json.rtData; 
    if(prcs != "undefined"){
      var seqId = prcs.seqId;
      var dutyName = prcs.dutyName;
      var dutyTime1 = prcs.dutyTime1; 
      var dutyTime2 = prcs.dutyTime2; 
      var dutyTime3 = prcs.dutyTime3; 
      var dutyTime4 = prcs.dutyTime4; 
      var dutyTime5 = prcs.dutyTime5; 
      var dutyTime6 = prcs.dutyTime6;
      var dutyType1 = prcs.dutyType1; 
      var dutyType2 = prcs.dutyType2; 
      var dutyType3 = prcs.dutyType3; 
      var dutyType4 = prcs.dutyType4; 
      var dutyType5 = prcs.dutyType5; 
      var dutyType6 = prcs.dutyType6;  
      document.getElementById("seqId").value = seqId;
      document.getElementById("dutyName").value = dutyName;
      document.getElementById("dutyTime1").value = dutyTime1.trim();
      document.getElementById("dutyTime2").value = dutyTime2.trim();
      document.getElementById("dutyTime3").value = dutyTime3.trim();
      document.getElementById("dutyTime4").value = dutyTime4.trim();
      document.getElementById("dutyTime5").value = dutyTime5.trim();
      document.getElementById("dutyTime6").value = dutyTime6.trim();
      document.getElementById("dutyType1").value = dutyType1;
      document.getElementById("dutyType2").value = dutyType2;
      document.getElementById("dutyType3").value = dutyType3;
      document.getElementById("dutyType4").value = dutyType4;
      document.getElementById("dutyType5").value = dutyType5;
      document.getElementById("dutyType6").value = dutyType6;
    }
  } 
}
</script>
</head>
<body class="" topmargin="5" onload="doOnLoad();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/form.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;编辑上下班时间</span>
    </td>
  </tr>
</table>
<br>
<table class="MessageBox" align="center" width="500">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">说明：上下班登记时间范围在 00:00:00 至 23:59:59 之间，请按时间顺序指定</div>
    </td>
  </tr>
</table>
<br>
<form action="config_insert.php"  method="post" id = "form1" name="form1" >
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.attendance.data.YHAttendConfig"/>
<table class="TableBlock" width="450" align="center">
  <tr>
    <td nowrap class="TableData"> 排班类型说明：</td>
    <td class="TableData">
       <input type="text" id= "dutyName" name="dutyName" size="25" maxlength="100" class="BigInput" value="">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData"> 第1次登记：</td>
    <td class="TableData">
       <input type="text" id = "dutyTime1"  name="dutyTime1" class="BigInput" size="8" maxlength="8" value="">&nbsp;
          <select id = "dutyType1" name="dutyType1" class="BigSelect">
            <option value="1" >上班</option>
            <option value="2" >下班</option>
          </select>
     </td>
   </tr>
   <tr>
     <td nowrap class="TableData"> 第2次登记：</td>
     <td class="TableData">
       <input type="text" id = "dutyTime2" name="dutyTime2" class="BigInput" size="8" maxlength="8" value="">&nbsp;
         <select id = "dutyType2" name="dutyType2" class="BigSelect">
           <option value="1" >上班</option>
           <option value="2" >下班</option>
         </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 第3次登记：</td>
      <td class="TableData">
        <input type="text" id ="dutyTime3" name="dutyTime3" class="BigInput" size="8" maxlength="8" value="">&nbsp;
          <select id = "dutyType3" name="dutyType3" class="BigSelect">
            <option value="1" >上班</option>
            <option value="2" >下班</option>
          </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 第4次登记：</td>
      <td class="TableData">
          <input type="text" id = "dutyTime4" name="dutyTime4" class="BigInput" size="8" maxlength="8" value="">&nbsp;
          <select id = "dutyType4" name="dutyType4" class="BigSelect">
            <option value="1" >上班</option>
            <option value="2" >下班</option>
          </select>
       </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 第5次登记：</td>
      <td class="TableData">
        <input type="text" id = "dutyTime5" name="dutyTime5" class="BigInput" size="8" maxlength="8" value="">&nbsp;
        <select id = "dutyType5" name="dutyType5" class="BigSelect">
          <option value="1" >上班</option>
          <option value="2" >下班</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 第6次登记：</td>
      <td class="TableData">
          <input type="text" id = "dutyTime6" name="dutyTime6" class="BigInput" size="8" maxlength="8" value="">&nbsp;
          <select id = "dutyType6" name="dutyType6" class="BigSelect">
            <option value="1" >上班</option>
            <option value="2" >下班</option>
          </select>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" value="" id = "seqId" name="seqId">
        <input type="button" value="确定" class="BigButton" onclick = "Init();">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="history.go(-1);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>