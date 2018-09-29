<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用印查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function doInit(){
	setDate();
}

function doSubmit(){
	if(checkForm()){
		var query = $("form1").serialize(); 
		location.href = "<%=contextPath %>/subsys/jtgwjh/search/stampsSearch.jsp?" + query
	}
}

function checkForm(){
  var sendDate1 = $("sendDate1").value;
  if(sendDate1){
    if(!isValidDateStr(sendDate1)){
      alert("调动日期格式不对，应形如 2010-01-02");
      $("sendDate1").focus();
      $("sendDate1").select();
      return false;
    }
  }
  var sendDate2 = $("sendDate2").value;
  if(sendDate2){
    if(!isValidDateStr(sendDate2)){
      alert("调动日期格式不对，应形如 2010-01-02");
      $("sendDate2").focus();
      $("sendDate2").select();
      return false;
    }
  }
  return true;
}

//日期
function setDate(){
  var date1Parameters = {
    inputId:'sendDate1',
    property:{isHaveTime:false}
    ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
	  inputId:'sendDate2',
	  property:{isHaveTime:false}
	  ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
  
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/infofind.gif"><span class="big3"> 用印查询</span></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" >
<table class="TableBlock" align="center" >
  <tr>
    <td nowrap class="TableContent">用印类型： </td>
    <td class="TableData">
      <select id="stampType" name="stampType" style="width:80px;">
        <option value=''></option>
        <option value='0'>主办盖章</option>
        <option value='1'>协办盖章</option>
      </select>
    </td>
  <tr>

  <tr>
    <td nowrap class="TableContent">用印人员： </td>
    <td class="TableData">
      <input type="hidden" id="stampUser" name="stampUser" value="">
      <input type="text" id="stampUserDesc" name="stampUserDesc" style="width:60%" class="BigStatic" readonly>
      <a href="javascript:void(0);" class="orgAdd" onClick="selectUser(['stampUser', 'stampUserDesc']);">添加</a>
      <a href="javascript:void(0);" class="orgClear" onClick="$('stampUser').value='';$('stampUserDesc').value='';">清空</a>
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableContent">用印日期： </td>
    <td nowrap class="TableData" >
      <input type="text" name="sendDate1" id="sendDate1" class="BigInput" size="8" maxlength="10" value="" >&nbsp;
      <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    &nbsp;至&nbsp;
      <input type="text" name="sendDate2" id="sendDate2" class="BigInput" size="8" maxlength="10" value="" >&nbsp;
      <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    </td>
  </tr>
    <tr align="center" class="TableControl">
      <td colspan="6" nowrap>
        <input type="button" value="查询" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
 </table>
</form>
</body>
</html>