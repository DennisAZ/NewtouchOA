	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>借书登记</title>
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
    var now = new Date();  
    var nowDate = "";
    if(myBrowser() == "FF"){
    	nowDate = now.getYear()+1900+"-"+parseMonth(now.getMonth()+1)+"-"+now.getDate();
    }else{
    	nowDate = now.getYear()+"-"+parseMonth(now.getMonth()+1)+"-"+now.getDate();
    }
    $("borrowDate").value = nowDate;
    var milliDate = now.getTime() + 30 * 24 * 60 * 60 *1000;
    var lastDate = new Date(milliDate);
    var returnDate = lastDate.getYear()+"-"+parseMonth(lastDate.getMonth()+1)+"-"+lastDate.getDate();
    if(myBrowser() == "FF"){
    	returnDate = lastDate.getYear()+1900+"-"+parseMonth(lastDate.getMonth()+1)+"-"+lastDate.getDate();
    }else{
    	returnDate = lastDate.getYear()+"-"+parseMonth(lastDate.getMonth()+1)+"-"+lastDate.getDate();
    }
    $("returnDate").value = returnDate;
  }

  function parseMonth(month){
    if(month < 10){
      return "0"+month;
    }
    return month;
  }

  function myBrowser(){
	  var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	  var isOpera = userAgent.indexOf("Opera") > -1;

	  if (isOpera){return "Opera"}; //判断是否Opera浏览器
	  if (userAgent.indexOf("Firefox") > -1){return "FF";} //判断是否Firefox浏览器
	  if (userAgent.indexOf("Safari") > -1){return "Safari";} //判断是否Safari浏览器
	  if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera){return "IE";} ; //判断是否IE浏览器
	  } //myBrowser() end
	  
function check(){
  var name = $("toName").value;
  var bookNo = $("bookNo").value;
  var begin = $("borrowDate").value;
  var end = $("returnDate").value;
  if(!name){
    alert("借书人不能为空");
    return false;
  }else if(!bookNo){
    alert("图书编号不能为空");
    return false;
  }
  if(begin){
	  if(!isValidDateStr(begin)){
	    alert("借书日期格式不正确,如1999-10-10");
	    document.getElementById("borrowDate").focus();
	    return false;
	  }
  }
  if(end){
	  if(!isValidDateStr(end)){
	    alert("还书日期格式不正确,如1999-10-10");
	    document.getElementById("returnDate").focus();
	    return false;
	  }
  }
  if(end && begin){
	  if(end < begin){
	    alert("还书日期不能小于借书日期！");
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

function LoadWindow2(event)
{
  var userId = $("toName").value;
  if(!userId){
    alert("请先选择借书人");
    return false;
  }
  event = event || window.event;
  var URL= contextPath + "/subsys/oa/book/query/queryindex.jsp?userId=" + userId;
  loc_x=document.body.scrollLeft+event.clientX-event.offsetX-100;
  loc_y=document.body.scrollTop+event.clientY-event.offsetY+170;
  window.showModalDialog(URL,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:245px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
}

function goBack(){
  var url = contextPath + "/yh/subsys/oa/book/act/YHBookRuleAct/index.act";
  window.location.href = url;
}

function getSingleUser(array){
  $("bookNo").value = "";
  selectSingleUser(array);
}
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 借书登记</span>
    </td>
  </tr>
</table>
<br>
<br>
<form action="<%=contextPath%>/yh/subsys/oa/book/act/YHBookRuleAct/regBorrowBook.act"  method="post" name="form1" id="form1">
<table class="TableBlock"  width="400" align="center" >
   <tr>
    <td nowrap class="TableData" width="120">借书人：</td>
    <td nowrap class="TableData">
      <input type="hidden" name="toId" id="toId" value=""> 	
      <input type="text" name="toName" id="toName" size="13" class="BigStatic" value="" readonly>&nbsp;
      <a href="javascript:;" class="orgAdd" onClick="getSingleUser(['toId' , 'toName']); return false;" title="指定借书人">指定</a>      
      <br>
     </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">图书编号：</td>
    <td class="TableData">
      <input type="text" name="bookNo" id="bookNo" class="BigStatic" size="13" maxlength="100" readonly value="">&nbsp;
      <input type="button" value="选 择" class="SmallButton" onclick="LoadWindow2(this)" title="选择图书编号" name="button">
    </td> 
   </tr>
   <tr>
    <td nowrap class="TableData">借书日期：</td>

    <td nowrap class="TableData">                 
    <input type="text" id="borrowDate" onfocus="this.select();" name="borrowDate" class="BigInput" value="" style="width:80px;" />
	  <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" />
	       为空为当前日期
    </td> 
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">归还日期：</td>
    <td nowrap class="TableData">
      <input type="text" id="returnDate" onfocus="this.select();" name="returnDate" class="BigInput" value="" style="width:80px;" />
	    <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" />
	                   为空为从借书之日起30天的日期
    </td> 
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">备注：</td>
    <td nowrap class="TableData">
      <textarea name="remark" id="remark" class="BigInput" cols="35" rows="3"></textarea>
    </td>
   </tr>

   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="button" value="确定" class="BigButton" title="保存借书信息" name="button1" onclick="checkForm();">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="goBack();return false;">
    </td>
   </tr>
</table>
</form>
</body>

</html>
