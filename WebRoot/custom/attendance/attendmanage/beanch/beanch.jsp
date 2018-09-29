	
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html> 
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
  function clearUser(){
    $("userId").value = "";
    $("userDesc").value = "";
  }

  function setDate(){
    var date3Parameters = {
     		inputId:'changeDate',
     		property:{isHaveTime:false},
     		bindToBtn:'changeDateImg'
     	};
     	new Calendar(date3Parameters);
     	
  }

  function doSubmit(){
    var userId = $("userId").value;
    if(!userId){
      alert("请选择人员");
      return;
    }

    var annualDays = $("annualDays").value;
    if(!annualDays){
      alert("请输入天数");
      $("annualDays").focus();
      return;
    }else if(!isNumber(annualDays)){
      alert("天数应该为整数");
      $("annualDays").focus();
      return;
    }else if(isNumber(annualDays)){
        if(annualDays < 0){
          alert("天数应该大于0");
          $("annualDays").focus();
          return;
         }
    }
    
    doButton();   
  }
  function doInit(){
    setDate();
  }

  function doButton(){
    var url = contextPath + "/yh/custom/attendance/act/YHPersonAnnualBeanchAct/insertBeanch.act";
    var param = mergeQueryString($("form1"));
    
    var rtJson = getJsonRs(url, param);
    if(rtJson.rtState == "0"){
      alert(rtJson.rtMsrg);
      return;
    }
    if (rtJson.rtState == "1") {
     alert(rtJson.rtMsrg);
     return;
    }
  }
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/infofind.gif" align="absmiddle"><span class="big3">年休假批量设置</span>
    </td>
  </tr>
</table>

<form  id="form1" name="form1">
<table class="TableBlock" align="center" width="50%">
   <tr >
      <td nowrap class="TableContent">选择人员：</td>
      <td class="TableData">
        <input type="hidden" id="userId" name="userId" value="" />
        <textarea cols=30 id="userDesc" name="userDesc" rows=5 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectUser(['userId', 'userDesc']);return false;">添加</a>
        <a href="javascript:;" class="orgClear" onclick="clearUser();return false;">清空</a><br>
        <span id="roleMsg" style="color:red"></span>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableContent">天数：</td>
      <td class="TableData">
        <input type="text" name="annualDays" id="annualDays" value="" />
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">下次调整时间：</td>
      <td class="TableData">
        <input type="text" name="changeDate" id="changeDate" size="10" maxlength="10" class="BigInput" value="" readonly/>
        <img id="changeDateImg" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
		<td nowrap class=TableData align="center" colspan="2">
		<input type="button" value="确定" class="BigButton" onclick="doSubmit();"></input>
		</td>
	</tr>
    
</table>
</form>
</body>
</html>

