<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>证照信息查询</title>
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
	licenseType();//查询码表相关信息(证照类型)
	licenseState();//证照状态
	var parameters = {
		      inputId:'EXPIRE_DATE1',
		      property:{isHaveTime:false}
		      ,bindToBtn:'date1'
		  };
		  new Calendar(parameters);
		  var parameters = {
		      inputId:'EXPIRE_DATE2',
		      property:{isHaveTime:false}
		      ,bindToBtn:'date2'
		  };
		  new Calendar(parameters);
}
//证照类型
function licenseType(){
	var codeObject = getChildCode("HR_STAFF_LICENSE1");
	var selectObj = $("LICENSE_TYPE");
  for(var i=0; i<codeObject.length; i++){
    var code = codeObject[i];
    var codeId = code.seqId;
    //alert(codeId);
    var codeNo = code.codeNo;
    var codeDesc = code.codeName;
    var myOption = document.createElement("option");
    
    myOption.value = codeId; //codeId是编号
   
    myOption.text = codeDesc;
   
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
    //selectObj.options.add(new Option(codeObject[i], codeObject[i]));
   // $("LICENSE_TYPE").value = myOption.text;
	}
}
//证照状态
function licenseState(){ 
  var codeObject = getChildCode("HR_STAFF_LICENSE2");
	var selectObj = $("STATUS");
	
	for(var i=0; i<codeObject.length; i++){
    var code = codeObject[i];
    var codeId = code.seqId;
    var codeNo = code.codeNo;
    var codeDesc = code.codeName;
    
    var myOption = document.createElement("option");
    myOption.value = codeId;
    myOption.text = codeDesc;
  
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
	}
}
function CheckForm(){
	 if(document.form1.EXPIRE_DATE1.value!="" && document.form1.EXPIRE_DATE2.value!="" && document.form1.EXPIRE_DATE1.value > document.form1.EXPIRE_DATE2.value)
	   { 
	      alert("注意  前者不能大于后者时间！");
	      return (false);
	   }
	 	var src = contextPath+"/yh/subsys/oa/hr/manage/staff_license/act/YHNewLicenseInfoAct/queryLicenseInfo.act";
	 	  document.form1.action = src;
	 	  document.form1.submit();
	 	  return true;   
	   
}
function getType(){
	var licenseType1 = $("LICENSE_TYPE").value;
	//alert(contractType);
	$("licenseType").value = licenseType1;
}
function getState(){
	var licenseState1 = $("STATUS").value;
	//alert(contractType);
	$("licenseState").value = licenseState1;
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doOnload();">

<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 证照信息查询</span></td>
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
      <td nowrap class="TableData" width="100"> 证照类型：</td>
      <td class="TableData">
        <select name="LICENSE_TYPE" id="LICENSE_TYPE" onchange="getType();" title="证照类型可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">证照类型&nbsp;</option>
        </select>
         <input type="hidden" name="licenseType" id="licenseType" ></input>
      </td>
   </tr>
   <tr>
    	<td nowrap class="TableData">证照编号：</td>
      <td class="TableData">
        <INPUT type="text"name="LICENSE_NO" id="LICENSE_NO"  class=BigInput size="15" value="">
      </td>
   </tr>
   <tr>
    	<td nowrap class="TableData">证照名称：</td>
      <td class="TableData">
       <INPUT type="text"name="LICENSE_NAME" id="LICENSE_NAME"  class=BigInput size="15" value="">
      </td>
    </tr>
    <tr>
    	<td nowrap class="TableData">状态：</td>
      <td class="TableData" colspan=3>
        <select name="STATUS" id="STATUS" onchange="getState();" title="状态可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">状态&nbsp;</option>
        </select>
        <input type="hidden" name="licenseState" id="licenseState" ></input>
      </td> 
    </tr>
    <tr>
      <td nowrap class="TableData"> 到期日期：</td>
      <td class="TableData">
        <input type="text" name="EXPIRE_DATE1" id="EXPIRE_DATE1"  size="10" maxlength="10" class="BigInput" readonly value=""/>
        <img src="<%=imgPath%>/calendar.gif" id="date1" name="date1" align="absMiddle" border="0" style="cursor:pointer">
        至
        <input type="text" name="EXPIRE_DATE2" id="EXPIRE_DATE2" size="10" maxlength="10" class="BigInput" readonly value="" /> 
        <img src="<%=imgPath%>/calendar.gif" id="date2" name="date2" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">发证机构：</td>
      <td class="TableData">
        <textarea type="text" name="NOTIFIED_BODY" id="NOTIFIED_BODY" size="25" class="BigInput" value=""></textarea>
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