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
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
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

  var beginParameters1 = {
      inputId:'beginDate1',
      property:{isHaveTime:true}
      ,bindToBtn:'beginDateImg1'
  };
  new Calendar(beginParameters1);
  var endParameters1 = {
      inputId:'endDate1',
      property:{isHaveTime:true}
      ,bindToBtn:'endDateImg1'
  };
  new Calendar(endParameters1);

  var date = new Date();
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  var m = (m > 9) ? m : '0' + m;
  var d = date.getDate();
  d = (d > 9) ? d : '0' + d;
  var time = date.toLocaleTimeString();
  $('endDate').value = y + '-' + m + '-' + d + ' ' + time;

  var date = new Date();
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  m = (m > 9) ? m : '0' + m;
  var d = date.getDate();
  d = (d > 9) ? d : '0' + d;
  var time = date.toLocaleTimeString();
  $('endDate1').value = y + '-' + m + '-' + d + ' ' + time;
}

function doSearch(){
  var beginDate = $("beginDate").value; 
  var endDate = $("endDate").value; 
  
  //var te = Test($("beginDate"),$("endDate"));
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
  location = "<%=contextPath%>/core/funcs/system/mobilesms/sendsearch.jsp?"+query;
}

function deleteSms(){
  if(!confirmDelSign()) {
    return ;
  } 
  var user = $("user").value;
  var phone = $("phone").value;
  var content = $("content").value;
  var beginDate = $("beginDate").value;
  var endDate = $("endDate").value;
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/deleteSendManage.act";
  var rtJson = getJsonRs(url, "phone="+encodeURIComponent(phone)+"&content="+encodeURIComponent(content)+"&beginDate="+beginDate+"&endDate="+endDate+"&user="+user);
  if (rtJson.rtState == "0") {
    location = "<%=contextPath%>/core/funcs/system/mobilesms/senddelete.jsp";
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function reportSubmit(){
  var beginDate = $("beginDate1").value; 
  var endDate = $("endDate1").value; 
  var radio0 = $("stat0");
  var radio1 = $("stat1");
  var radio2 = $("view0");
  var radio3 = $("view1");
  
  if(!Test($("beginDate1"),$("endDate1"))){
    return;
  }

  if(radio2.checked == true){
    if(radio0.checked == true){
      location = "<%=contextPath%>/core/funcs/system/mobilesms/report.jsp?beginDate="+beginDate+"&endDate="+endDate;
    }else if(radio1.checked == true){
      location = "<%=contextPath%>/core/funcs/system/mobilesms/reportDept.jsp?beginDate="+beginDate+"&endDate="+endDate;
    }
  }else if(radio3.checked == true){
    if(radio0.checked == true){
      location = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/exportToExcelSmsPerson.act?beginDate="+beginDate+"&endDate="+endDate;
    }else if(radio1.checked == true){
      location = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/exportToExcelSmsDept.act?beginDate="+beginDate+"&endDate="+endDate;
    }
  }
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;短信发送管理</span>
    </td>
  </tr>
</table>
  <form name="form1" id="form1">
  <table class="TableBlock" width="400" align="center">

    <tr>
      <td nowrap class="TableData">短信发送状态：</td>
      <td class="TableData">
        <select name="sendFlag" id="sendFlag" class="BigSelect">
          <option value="ALL">所有</option>
          <option value="0">待发送</option>
          <option value="3">发送中...</option>
          <option value="1">发送成功</option>
          <option value="2">发送超时</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">发送人：</td>
      <td class="TableData">
        <input type="hidden" name="user" id="user" value="">
        <textarea cols=23 name="userDesc" id="userDesc" rows="3" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">收信人号码：</td>
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
      <td class="TableData"><input type="text" name="endDate" id="endDate" size="20" maxlength="20" class="BigInput" value="">
         <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
  <!--    <tr>
      <td nowrap class="TableData">查询显示条数：</td>
      <td class="TableData"><input type="text" name="limit" id="limit" size="4" class="BigInput" value="100">
      </td>
    </tr>
    <tr >-->
      <td nowrap class="TableControl" colspan="2" align="center">
      	  <input type="hidden" name="DELETE_FLAG" value="0">
          <input type="button" value="查询" class="BigButton" title="进行查询" onclick="doSearch();">&nbsp;&nbsp;&nbsp;
          <input type="button" value="删除" class="BigButton" title="删除指定范围内的短信记录" onclick="deleteSms();">
      </td>
    </tr>

  </table>
      </form>
<br>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp; 短信发送统计</span>
    </td>
  </tr>
</table>

  <table class="TableBlock" width="400" align="center">
  <form name="form2" id="form2">
    <tr>
      <td nowrap class="TableData">起始时间：</td>
      <td class="TableData"><input type="text" name="beginDate1" id="beginDate1" size="20" maxlength="20" class="BigInput" value="">
         <img id="beginDateImg1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">截止时间：</td>
      <td class="TableData"><input type="text" name="endDate1" id="endDate1" size="20" maxlength="20" class="BigInput" value="">
          <img id="endDateImg1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">统计方式：</td>
      <td nowrap class="TableData" align="left">
        <INPUT type="radio" name="radio0" id="stat0" CHECKED><label for="stat0">按人员统计</label>
        <INPUT type="radio" name="radio0" id="stat1"><label for="stat1">按部门统计</label>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">查看方式：</td>
      <td nowrap class="TableData" align="left">
        <INPUT type="radio" name="radio1" id="view0" CHECKED><label for="view0">网页方式</label>
        <INPUT type="radio" name="radio1" id="view1"><label for="view1">EXCEL方式</label>
      </td>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" colspan="2" align="center">
         <input type="button" name="button1" value="统计报表" class="BigButton" title="按人员统计报表" onclick="reportSubmit();">&nbsp;&nbsp;
      </td>
    </tr>
    </form>
  </table>
</body>
</html>