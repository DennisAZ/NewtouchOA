<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>劳动技能信息查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/notify/js/openWin.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/setting/codeJs/hrCodeJs.js"></script>
<script type="text/javascript">
function doOnload(){
    var parameters = {
	      inputId:'ISSUE_DATE1',
	      property:{isHaveTime:false}
	      ,bindToBtn:'date1'
	  };
	  new Calendar(parameters);
	  var parameters = {
	      inputId:'ISSUE_DATE2',
	      property:{isHaveTime:false}
	      ,bindToBtn:'date2'
	  };
	  new Calendar(parameters);
	  var parameters = {
		      inputId:'EXPIRE_DATE1',
		      property:{isHaveTime:false}
		      ,bindToBtn:'date3'
		  };
		  new Calendar(parameters);
		  var parameters = {
			      inputId:'EXPIRE_DATE2',
			      property:{isHaveTime:false}
			      ,bindToBtn:'date4'
			  };
			  new Calendar(parameters);
}			  
function CheckForm(){
	if(document.form1.ISSUE_DATE1.value!="" && document.form1.ISSUE_DATE2.value!="" && document.form1.ISSUE_DATE1.value > document.form1.ISSUE_DATE2.value)
	   { 
	      alert("发证日期 前者不能大于后者！");
	      return (false);
	   }
	
	 if(document.form1.EXPIRE_DATE1.value!="" &&document.form1.EXPIRE_DATE2!="" && document.form1.EXPIRE_DATE1.value > document.form1.EXPIRE_DATE2.value)
	   { 
	      alert("到期日期 前者不能大于后者！");
	      return (false);
	   }
	 var src = contextPath+"/yh/subsys/oa/hr/manage/staffLaborSkills/act/YHNewLaborSkillsAct/queryLaborSkillInfo.act";
	  document.form1.action = src;
	  document.form1.submit();
	  return true;
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doOnload();">

<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 劳动技能信息查询</span></td>
  </tr>
</table>
<br>
<form action="#"  method="post" name="form1" onsubmit="return CheckForm();" >
 <table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData">单位员工：</td>
      <td class="TableData">
        <input type="text" name="userName" id="userName" size="12" class="BigStatic" readonly value="">&nbsp;
        <INPUT type="hidden" name="userId" id="userId">
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['userId', 'userName'],null,null,1)">选择</a>  
        <a href="javascript:;" class="orgClear" onClick="$('userId').value='';$('userName').value='';">清空</a>
      </td>
    </tr>
  
    <tr>
      <td nowrap class="TableData"> 发证日期：</td>
      <td class="TableData">
        <input type="text" name="ISSUE_DATE1" id="ISSUE_DATE1" size="10" maxlength="10" class="BigInput" readonly value="" />
        <img src="<%=imgPath%>/calendar.gif" id="date1" name="date1" align="absMiddle" border="0" style="cursor:pointer">
        至
        <input type="text" name="ISSUE_DATE2" id="ISSUE_DATE2" size="10" maxlength="10" class="BigInput" readonly value="" />
        <img src="<%=imgPath%>/calendar.gif" id="date2" name="date2" align="absMiddle" border="0" style="cursor:pointer">       
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 到期日期：</td>
      <td class="TableData">
        <input type="text" name="EXPIRE_DATE1" id="EXPIRE_DATE1" size="10" maxlength="10" class="BigInput" readonly value=""/>
        <img src="<%=imgPath%>/calendar.gif" id="date3" name="date3" align="absMiddle" border="0" style="cursor:pointer">
        至
        <input type="text" name="EXPIRE_DATE2" id="EXPIRE_DATE2" size="10" maxlength="10" class="BigInput" readonly value=""/>
        <img src="<%=imgPath%>/calendar.gif" id="date4" name="date4" align="absMiddle" border="0" style="cursor:pointer">        
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 技能名称：</td>
      <td class="TableData">
        <input type="text" name="ABILITY_NAME" id="ABILITY_NAME" size="25" maxlength="200" class="BigInput" value="">
      </td>
   </tr>
    <tr>
      <td nowrap class="TableData">发证机关/单位：</td>
      <td class="TableData">
         <input type="text" name="ISSUING_AUTHORITY" id="ISSUING_AUTHORITY" size="25" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" onclick="javascript:CheckForm(); return false;" value="查询" name="button"  class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
 </table>
  </form>
</body>
</html>