<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>时钟</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
 <script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script language=javascript>
var is_moz = (navigator.product == 'Gecko') && userAgent.substr(userAgent.indexOf('firefox') + 8, 3);
function getOpenner()
{
   if(is_moz)
      return parent.opener;
   else
      return parent.dialogArguments;
}
var parent_window = getOpenner();
var timeField = null;

function check_form(){ 
  var cur_time = $("hour").value + ":" + $("minite").value + ":" + $("second").value;

  if(timeField.value == ""){
    timeField.value = cur_time;
  } else {
     var len = timeField.value.indexOf(" ");
     if( len < 0 ) {
       len = timeField.value.length;
     }
     timeField.value = timeField.value.substr(0,len)+" "+cur_time;
  }
  if (parent_window.callbackFun) {
    parent_window.callbackFun();
  }
  window.close();
}
window.onload=function (){
   var now =new Date();
   createSelect('hour' , 24 , now.getHours());
   createSelect('minite' , 60 , now.getMinutes());
   createSelect('second' , 60 , now.getSeconds());
   
   timeField = parent_window.$(parent_window.timeField);
   var date_time = timeField.value;
   var len=date_time.indexOf(" ");
   if(len < 0) return;
   var time=date_time.substr(len+1);
   var time_array = time.split(":");
   if(time_array[0] != "" && !isNaN(parseInt(time_array[0])))
      $("hour").options[parseInt(time_array[0])].selected=" selected";
   if(time_array[1]!="" && !isNaN(parseInt(time_array[1])))
     $("minite").options[parseInt(time_array[1])].selected=" selected";
   if(time_array[2]!="" && !isNaN(parseInt(time_array[2])))
     $("second").options[parseInt(time_array[2])].selected=" selected";
};
function createSelect(selecte , max , value ) {
  var node = $(selecte);
  for (var i = 0 ;i < max ;i++ ) {
    var operate = new Element("option");
    if (i == value ) {
      operate.selected = true;
    }
    var tmp = i;
    if (i < 10 ) {
      tmp = "0" + i;
    }
    operate.value = tmp;
    operate.innerHTML = tmp;
    node.appendChild(operate);
  }
}
</script>
</head>


<body class="bodycolor" topmargin="2">
<div class="big1" align="center">
<b>
时间：<select name=hour id=hour class=BigSelect style="width:50px">
</select>
：<select name=minite id=minite class=BigSelect style="width:50px">
</select>
：<select name=second id=second class=BigSelect style="width:50px">
</select>

<br>
<br>
<input type=button  onClick="check_form()" value=" 确 定 " class="BigButton">
</b>
</div>

</body>
</html>
