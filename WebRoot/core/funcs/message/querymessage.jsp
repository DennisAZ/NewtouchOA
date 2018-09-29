<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信查询</title>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/message/js/messageutil.js"></script>
<script type="text/javascript">
function send(){
  var opType = $('operationType').value;
  if(checkDateTime("startDate") == false){
    $("startDate").focus();
    $("startDate").select();
    alert("日期格式不对，请输入形如：2010-10-10 12:12:12");
    return;
  }
  if(checkDateTime("endDate") == false){
    $("endDate").focus();
    $("endDate").select();
    alert("日期格式不对，请输入形如：2010-10-10 12:12:12");
    return;
  }
  if( !opType ||  opType == 1 ){
      //查询
      doSearch('form1');
   }else if(opType == 2){
     doExport('form1');
   } else if(opType == 3){
     alert('删除功能正在完善中！');
     return;
   }
}
/**
 * 查询
 */
function doSearch(form){
  var userType = $('userType').value;
  var param = "";
  var url = null;
  if(form){
    param = $(form).serialize();
  }

    url = "<%=contextPath %>/yh/core/funcs/message/act/YHMessageAct/queryMessageList.act?pageNo=0&pageSize=100&queryType=1&";
  location = url + param;
}
function doExport(form){
  var userType = $('userType').value;
  var param = "";
  if(form){
    param = $(form).serialize();
  }
  var  url = contextPath + "/yh/core/funcs/sms/act/YHSmsTestAct/exportExcel.act?";
  location = url + param;
}
function setOpType(type){
  $('operationType').value = type;
}
function doInit(){
  showCalendar("startDate", true, "startDateImg");
  showCalendar("endDate", true, "endDateImg");
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">
      <img src="<%=imgPath%>/infofind.gif" WIDTH="16" HEIGHT="16" align="absmiddle"><span class="big3"> 微讯查询</span>
    </td>
  </tr>
</table>
 <form name="form1" id="form1">
  <table class="TableBlock" width="400" align="center">
   <input type="hidden" value="1"  name="userType" id="userType" />
     <input type="hidden" name="userId" id="userId" value="">
 <!--    <tr>
   
    <td nowrap class="TableData">
         <select id="userType" name="userType">
            <option value="1">发送人</option>
            <option value="2">收信人</option>
         </select>
      </td> 
      <td class="TableData">
        <input type="hidden" name="userId" id="userId" value="">
        <textarea cols=23 name="userIdDesc" id="userIdDesc" rows="3" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['userId', 'userIdDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="Clear('userId','userIdDesc')">清空</a>
      </td>
    </tr> -->
  
    <tr>
      <td nowrap class="TableData">起始日期：</td>
      <td class="TableData"><input type="text" name="startDate" id="startDate" size="20" maxlength="20" class="BigInput" value="">
          <img src="<%=imgPath%>/calendar.gif" id="startDateImg" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">截止日期：</td>
      <td class="TableData"><input type="text" name="endDate" id="endDate" size="20" maxlength="20" class="BigInput" value="">
          <img src="<%=imgPath%>/calendar.gif" id="endDateImg" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">内容：</td>
      <td class="TableData">
        <textarea cols=33 name="content" rows="2" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">排序字段：</td>
      <td class="TableData">
        <select name="orderBy">
          
            <option value="SEND_TIME" selected>发送时间</option>
         </select>
         <select name="orderBySeq">
            <option value="DESC">降序</option>
            <option value="ASC">升序</option>
         </select>
      </td>
    </tr>
   
    <tr >
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="button" value="确定" class="BigButton" onclick="send()">
            <input type="hidden" value="" id="operationType" value="1"></input>
      </td>
    </tr>
  </table>
 </form>
</body>
</html>