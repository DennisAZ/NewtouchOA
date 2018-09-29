<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>新建合同信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
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
<script type="text/Javascript"><!--
function doOnload(){
	contractType();//查询码表相关信息(合同类型)
	contractState();//合同状态
	  //时间(判断的事合同签订日期和合同生效日期)
	var parameters = {
		      inputId:'MAKE_CONTRACT1',
		      property:{isHaveTime:false}
		      ,bindToBtn:'date1'
		  };
		  new Calendar(parameters);
		  var parameters = {
		      inputId:'MAKE_CONTRACT2',
		      property:{isHaveTime:false}
		      ,bindToBtn:'date2'
		  };
		  new Calendar(parameters);
		  var parameters = {
			      inputId:'TRAIL_OVER_TIME1',
			      property:{isHaveTime:false}
			      ,bindToBtn:'date3'
			  };
			  new Calendar(parameters);
			  var parameters = {
				      inputId:'TRAIL_OVER_TIME2',
				      property:{isHaveTime:false}
				      ,bindToBtn:'date4'
				  };
				  new Calendar(parameters);
				  var parameters = {
				      inputId:'CONTRACT_END_TIME1',
				      property:{isHaveTime:false}
				      ,bindToBtn:'date5'
				  };
				  new Calendar(parameters);
				  var parameters = {
					      inputId:'CONTRACT_END_TIME2',
					      property:{isHaveTime:false}
					      ,bindToBtn:'date6'
					  };
					  new Calendar(parameters);
}
//合同类型
function contractType(){
	var codeObject = getChildCode("HR_STAFF_CONTRACT1");
	var selectObj = $("CONTRACT_TYPE");
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
    //$("CONTRACT_TYPE").value = myOption.text;
	}
}
//合同状态
function contractState(){ 
  var codeObject = getChildCode("HR_STAFF_CONTRACT2");
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
	 if(document.form1.MAKE_CONTRACT1.value!="" && document.form1.MAKE_CONTRACT2.value!="" && document.form1.MAKE_CONTRACT1.value > document.form1.MAKE_CONTRACT2.value)
	   { 
	      alert("合同签订日期 前者不能大于后者！");
	      return (false);
	   }
	
	 if(document.form1.TRAIL_OVER_TIME1.value!="" &&document.form1.TRAIL_OVER_TIME2!="" && document.form1.TRAIL_OVER_TIME1.value > document.form1.TRAIL_OVER_TIME2.value)
	   { 
	      alert("试用到期日期 前者不能大于后者！");
	      return (false);
	   }
	 	if(document.form1.CONTRACT_END_TIME1.value!="" && document.form1.CONTRACT_END_TIME2!=""  && document.form1.CONTRACT_END_TIME1.value > document.form1.CONTRACT_END_TIME2.value)
	   { 
	      alert("合同到期日期 前者不能大于后者！");
	      return (false);
	   }
	 	var src = contextPath+"/yh/subsys/oa/hr/manage/staff_contract/act/YHNewContractInfoAct/queryContractInfo.act";
	 	  document.form1.action = src;
	 	  document.form1.submit();
	 	  return true;   
	   
 }
 
function getType(){
	var contractType1 = $("CONTRACT_TYPE").value;
	//alert(contractType1);
	$("contractType").value = contractType1;
}
function getState(){
	var contractState1 = $("STATUS").value;
	//alert(contractType);
	$("contractState").value = contractState1;
}
function getSx(){
	var contractSX1 = $("CONTRACT_SPECIALIZATION").value;
	//alert(contractType);
	$("contractSX").value = contractSX1;
}
--></script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doOnload();">

<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img  src="<%=imgPath%>/infofind.gif"><span class="big3"> 合同信息查询</span></td>
  </tr>
</table>
<br>
 <form  action="#"  method="post" name="form1" >
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
    	<td nowrap class="TableData">合同编号：</td>
      <td class="TableData">
        <INPUT type="text" name="STAFF_CONTRACT_NO" id="STAFF_CONTRACT_NO" class=BigInput size="13" value="">
      </td>
   </tr>
   <tr>
   
      <td nowrap class="TableData" width="100"> 合同类型：</td>
      <td class="TableData">
        <select name="CONTRACT_TYPE" id="CONTRACT_TYPE" onchange="getType();" title="合同类型可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="" selected>请选择合同类型&nbsp;</option>
        </select>
        <input type="hidden" name="contractType" id="contractType" />
      </td>
   </tr>
    <tr>
    	<td nowrap class="TableData">合同状态：</td>
      <td class="TableData" colspan=3>
        <select name="STATUS" id="STATUS"  onchange="getState();" title="合同状态可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">请选择合同状态&nbsp;</option>
        </select>
        <input type="hidden" name="contractState" id="contractState" ></input>
      </td> 
    </tr>
    <tr>
      <td nowrap class="TableData"> 合同签订日期：</td>
      <td class="TableData">
        <input type="text" name="MAKE_CONTRACT1" id="MAKE_CONTRACT1" size="10" maxlength="10" class="BigInput" readonly value=""/>
        <img src="<%=imgPath%>/calendar.gif" id="date1" name="date1" align="absMiddle" border="0" style="cursor:pointer">
        至
        <input type="text" name="MAKE_CONTRACT2" id="MAKE_CONTRACT2" size="10" maxlength="10" class="BigInput" readonly value="" /> 
        <img src="<%=imgPath%>/calendar.gif" id="date2" name="date2" align="absMiddle" border="0" style="cursor:pointer">      
      </td>
    </tr>
        <tr>
      <td nowrap class="TableData"> 试用到期日期：</td>
      <td class="TableData">
        <input type="text" name="TRAIL_OVER_TIME1" id="TRAIL_OVER_TIME1" size="10" maxlength="10" class="BigInput" readonly value="" /> 
        <img src="<%=imgPath%>/calendar.gif" id="date3" name="date3" align="absMiddle" border="0" style="cursor:pointer">
        至
        <input type="text" name="TRAIL_OVER_TIME2" id="TRAIL_OVER_TIME2" size="10" maxlength="10" class="BigInput" readonly value="" /> 
        <img src="<%=imgPath%>/calendar.gif" id="date4" name="date4" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 合同到期日期：</td>
      <td class="TableData">
        <input type="text" name="CONTRACT_END_TIME1" id="CONTRACT_END_TIME1" size="10" maxlength="10" class="BigInput" readonly value=""/> 
        <img src="<%=imgPath%>/calendar.gif" id="date5" name="date5" align="absMiddle" border="0" style="cursor:pointer">
        至
        <input type="text" name="CONTRACT_END_TIME2" id="CONTRACT_END_TIME2" size="10" maxlength="10" class="BigInput" readonly value=""/>     
        <img src="<%=imgPath%>/calendar.gif" id="date6" name="date6" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
       <input type="button" onclick="javascript:CheckForm(); return false;" value="查询" name="button"  class="BigButton">
      <!--   <input type="submit" value="查询" class="BigButton">&nbsp;&nbsp;  -->
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>

 </table>
   </form>
</body>
</html>