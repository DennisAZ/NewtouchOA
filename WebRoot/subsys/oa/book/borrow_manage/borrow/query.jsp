	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<%@ include file="/core/inc/header.jsp" %>

<html>
<head>
<%
  String flag = request.getParameter("flag");
%>
<title>还书管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
	<link rel="stylesheet" href = "<%=cssPath%>/style.css">
	<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script Language="JavaScript">
var flag = <%=flag%>;
function doInit(){  
  var beginParameters = {
      inputId:'borrowDate',
      property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'
        };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'returnDate',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
        };
  new Calendar(endParameters); 
  if(flag == 0){
    $("form1").action = contextPath + "/yh/subsys/oa/book/act/YHBookRuleAct/findHistory.act";    
  }
}
function parseMonth(month){
  if(month < 10){
    return "0"+month;
  }
  return month;
}
function LoadWindow2(event)
{
  var userId = $("toId").value;
  event = event || window.event;
  var URL= contextPath + "/subsys/oa/book/query/queryindex.jsp?userId=" + userId; 
  loc_x=document.body.scrollLeft+event.clientX-event.offsetX-100;
  loc_y=document.body.scrollTop+event.clientY-event.offsetY+170;
  window.showModalDialog(URL,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:245px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
}
  function check(){
    var name = $("toId").value;
    var bookNo = $("bookNo").value;
    var begin = $("borrowDate").value;
    var end = $("returnDate").value;
    var bookStatus = $("bookStatus").value;
    //alert(name + "=="+bookNo+"=="+bookStatus+"=="+begin+"=="+end);
    if(begin!=""){
 	   if(!isValidDateStr(begin)){
 	     alert("开始日期格式不正确,如1999-10-10");
 	     document.getElementById("borrowDate").focus();
 	     return false;
 	   }
    }
    if(end!=""){ 
 	   if(!isValidDateStr(end)){
 	     alert("结束日期格式不正确,如1999-10-10");
 	     document.getElementById("returnDate").focus();
 	     return false;
 	   }
    } 
    if(begin!="" && end!=""){
 	   if(end < begin){
 	     alert("结束日期不能小于开始日期！");
 	     return false;
 	   }  	  
   }
    return true;
  }
 function checkForm(){
   if(check()){
    $("form1").submit();
  }
    return true;	  
 }


</script>
</head>

<body class="bodycolor" topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/book.gif" align="absmiddle"><span class="big3"> 还书管理 </span>
    </td>
  </tr>
</table>
<br>

<div align="center">
 <form action="<%=contextPath%>/yh/subsys/oa/book/act/YHReturnBookManageAct/returnBookManage.act"  method="post" id="form1" name="form1" >  
<table class="TableBlock"  width="40%" align="center" >

 
   <tr>
    <td nowrap class="TableData">借书人：</td>
    <td nowrap class="TableData" align="left">
      <input type="hidden" name="toId" id="toId" value=""> 	
      <input type="text" name="toName" id="toName" size="18" class="BigStatic" value="" readonly>&nbsp;
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['toId' , 'toName'])" title="指定借书人">指定</a>      
      <br>
     </td>
   </tr>

   <tr>
    <td nowrap class="TableData" width="120">图书编号：</td>
    <td class="TableData" align="left">
      <input type="text" name="bookNo" id="bookNo" class="BigStatic" size="13" maxlength="100" readonly value="">&nbsp;
      <input type="button" value="选 择" class="SmallButton" onclick="LoadWindow2(this)" title="选择图书编号" name="button">
    </td> 
   </tr>
   
   <tr>
    <td nowrap class="TableData">借书日期：</td>
    <td nowrap class="TableData" align="left">
     从      <input type="text" id="borrowDate" onfocus="this.select();" name="borrowDate" class="BigInput" value="" style="width:80px;" />
	    <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" />
    到       <input type="text" id="returnDate" onfocus="this.select();" name="returnDate" class="BigInput" value="" style="width:80px;" />
	    <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" />

    </td>
   </tr>   
   <tr>
    <td nowrap class="TableData" width="120">状态：</td>
    <td nowrap class="TableData" align="left">
       <select name="bookStatus" id="bookStatus" >
       	<option value="">选择</option>
       	<option value="1">已还</option>
       	<option value="0">未还</option>
       </select>
    </td>
   </tr>

   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="button" value="查询" class="BigButton" onClick="checkForm();" title="模糊查询" name="button">
    </td>
   </tr>
   
      
</table>
</form>
</div>

</body>
</html>