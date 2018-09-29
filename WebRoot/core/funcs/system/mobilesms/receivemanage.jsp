<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<head>
<title>修改界面设置</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/mobilesms/js/mobilesmsLogic.js"></script>
<script type="text/javascript">
function doInit(){
  var beginParameters = {
      inputId:'beginDate',
      property:{isHaveTime:true}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endDate',
      property:{isHaveTime:true}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);

  var date = new Date();
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  m = (m > 9) ? m : '0' + m;
  var d = date.getDate();
  d = (d > 9) ? d : '0' + d;
  var time = date.toLocaleTimeString();
  $('endDate').value = y + '-' + m + '-' + d + ' ' + time;
}

function doSearch(){
  var beginDate = $("beginDate").value;
  var endDate = $("endDate").value;
  if(!Test($("beginDate"),$("endDate"))){
    return;
  }
//  if($F('beginDate') && !(/^(\d{4})(-|\/)(\d{2})\2(\d{2}) (\d{1,2}):(\d{2}):(\d{2})$/).exec($F('beginDate'))){
//    alert("错误,起始时间格式不对，应形如 1999-01-02 14:55:20");
//    return false;
//  }
//  if($F('endDate') && !(/^(\d{4})(-|\/)(\d{2})\2(\d{2}) (\d{1,2}):(\d{2}):(\d{2})$/).exec($F('endDate'))){
//    alert("错误,截止时间格式不对，应形如 1999-01-02 14:55:20");
//    return false;
//  }

  var query = "";
  query = $("form1").serialize();
  location = "<%=contextPath%>/core/funcs/system/mobilesms/receivesearch.jsp?"+query;
}

function deleteSms(){
  if(!confirmDelSign()) {
    return ;
  } 
  var phone = $("phone").value;
  var content = $("content").value;
  var beginDate = $("beginDate").value;
  var endDate = $("endDate").value;
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/deleteReceiveManage.act";
  var rtJson = getJsonRs(url, "phone="+encodeURIComponent(phone)+"&content="+encodeURIComponent(content)+"&beginDate="+beginDate+"&endDate="+endDate);
  if (rtJson.rtState == "0") {
    location = "<%=contextPath%>/core/funcs/system/mobilesms/receivedelete.jsp";
  }else{
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;短信接收管理</span>
    	<span class="small1">&nbsp;查询手机向OA系统发送的短信，这些短信未指定接收人姓名或发送者手机号未在OA系统中登记</span>
    </td>
  </tr>
</table>
<br/>
 <form name="form1" id="form1">
  <table class="TableBlock" width="400" align="center">
 
    <tr>
      <td nowrap class="TableData">发信人手机号码：</td>
      <td class="TableData"><input type="text" name="phone" id="phone" size="20" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">内容：</td>
      <td class="TableData"><textarea cols=40 name="content" id="content" rows="3" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">起始时间：</td>
      <td class="TableData"><input type="text" name="beginDate" id="beginDate" size="20" maxlength="20" class="BigInput" value="">
          <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">截止时间：</td>
      <td class="TableData"><input type="text" name="endDate" id="endDate" size="20" maxlength="20" class="BigInput">
          <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
  <!--   <tr>
      <td nowrap class="TableData">查询显示条数：</td>
      <td class="TableData"><input type="text" name="limit" id="limit" size="4" class="BigInput" value="50">
      </td>
    </tr>
    <tr > -->
      <td nowrap class="TableControl" colspan="2" align="center">
      	  <input type="hidden" name="DELETE_FLAG" value="0">
          <input type="button" value="查询" class="BigButton" title="进行查询" name="button" onclick="doSearch();">&nbsp;&nbsp;&nbsp;
          <input type="button" value="导出Excel" class="BigButton" title="导出至Excel" name="button" onclick="exportDept();">&nbsp;&nbsp;&nbsp;
          <input type="button" value="删除" class="BigButton" title="删除指定范围内的短信记录" onclick="deleteSms();">
      </td>
    </tr>
   
  </table>
 </form>
</body>
</html>