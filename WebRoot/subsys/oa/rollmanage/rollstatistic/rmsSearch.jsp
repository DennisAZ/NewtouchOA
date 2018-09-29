<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%
  boolean regist = YHUtility.isNullorEmpty(YHRegistProps.getProp(YHAuthKeys.REGIST_ORG + ".yh"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.core.global.YHRegistProps"%>
<%@page import="yh.core.data.YHAuthKeys"%>
<html>
<head>
<title>文件管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rollfilelogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
  setDate();
  var param = "sendDate=${param.sendDate}&rollId=${param.rollId}&deadlineId=${param.deadlineId}"
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsFileAct/getRmsFilesJosn.act?"+param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 2,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"fileCode",  width: '10%', text:"文件号", render:rmsFileCodeFunc, sortDef:{type: 0, direct:"desc"}},       
         {type:"data", name:"fileTitle",  width: '10%', text:"文件标题", render:rollFileFunc,sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"secret",  width: '10%', text:"密级", render:secretFunc}, 
         {type:"data", name:"sendUnit",  width: '10%', text:"发文单位", render:rollFileFunc,sortDef:{type: 0, direct:"desc"}}, 
         {type:"data", name:"sendDate",  width: '10%', text:"发文时间", render:sendDateFunc,sortDef:{type: 0, direct:"desc"}}, 
         {type:"data", name:"rollName",  width: '15%', text:"所属案卷", render:rmsRollNameFunc}, 
         {type:"data", name:"fileWord",  width: '5%', text:"公文字" ,render: fileWordFunc},
         {type:"data", name:"fileYear",  width: '5%', text:"公文年号",render: fileYearFunc},
         {type:"data", name:"fileNum",  width: '5%', text:"期号",render: issueNumFunc},
         {type:"data", name:"deadline",  width: '5%', text:"归档期限",render: deadlineFunc},
         {type:"selfdef", text:"操作", width: '10%',render:optsFile2}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      checkSelectRoll();
      checkSelectdeadline();
    }else{
      WarningMsrg('无文件', 'msrg');
    }
}

/**
 * 档案查询时加载案卷选择框
 * add by jzk
 * @return
 */
function checkSelectRoll(extValue){
  var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getRmsRollSelect2.act";
  var rtJson = getJsonRs(url);
  var selectObj =  $('rollId');
  var rollId='${param.rollId}';
  if(rtJson.rtState == "0"){
   for(var i = 0; i < rtJson.rtData.length; i++){
     var option = document.createElement("option");
     option.value = rtJson.rtData[i].seqId;
     option.innerHTML = rtJson.rtData[i].rollCode + " - " + rtJson.rtData[i].rollName;
     selectObj.appendChild(option);
     if(rollId && (rollId == rtJson.rtData[i].seqId)){
       option.selected = true;
     }
   }
  }
}

/**
 * 档案查询时加载归档期限的选择框
 * add by jzk
 * @return
 */
function checkSelectdeadline(extValue){
  var url = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsFileAct/getdeadline.act";
  var rtJson = getJsonRs(url);
  var selectObj =  $('deadlineId');
  var deadlineId='${param.deadlineId}';
  if(rtJson.rtState == "0"){
   for(var i = 0; i < rtJson.rtData.length; i++){
     var option = document.createElement("option");
     option.value = rtJson.rtData[i].seqId;
     option.innerHTML = rtJson.rtData[i].class_desc;
     selectObj.appendChild(option);
     if(deadlineId && (deadlineId == rtJson.rtData[i].seqId)){
       option.selected = true;
     }
   }
  }
}

function setDate(){
	//日期
	var date1Parameters = {
	   inputId:'inputsendDate',
	   property:{isHaveTime:false}
	   ,bindToBtn:'date1'
	};
	new Calendar(date1Parameters);
}

function queryRMSFiles()
{
	var sendDate = $("inputsendDate").value;
	var rollNameSelect = document.getElementById('rollId');
	var rollName = rollNameSelect.options[rollNameSelect.selectedIndex].value;
	var deadlineSelect = document.getElementById('deadlineId');
	var deadline = deadlineSelect.options[deadlineSelect.selectedIndex].value;
	if(checkDate() == true){
		var query="";
		if(sendDate !=null && sendDate !="")
		{
			query = query + "sendDate="+sendDate+"&";
		}
		if(rollName != "")
		{
			query = query + "rollId="+rollNameSelect.value+"&";
		}
		if(deadline != "")
		{
			query = query + "deadlineId="+deadlineSelect.value;
		}
		window.location = contextPath + "/subsys/oa/rollmanage/rollstatistic/rmsSearch.jsp?"+query;
	}
	
}

function checkDate(){
	var sendDate = document.getElementById("inputsendDate"); 
	if(sendDate.value !=null && sendDate.value !="")
	{
		if(!isValidDateStr(sendDate.value)){
			alert("日期格式不对，应形如 1999-01-01"); 
			sendDate.focus(); 
			sendDate.select(); 
			return false; 
		}
	}
	return true;
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="middle"><span class="big3"> 档案查询</span>&nbsp;
    </td>
    <td class="TableData">
        <input type="text" name="inputsendDate" id="inputsendDate" size="15" maxlength="19" class="BigInput" value="${param.sendDate}">
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >&nbsp;
      </td>
      <td>
	    <select id="rollId" name="rollId" value="${param.rollId}">
	        <option value="">所有案卷</option>
	     </select>
     </td>
     <td>
	    <select id="deadlineId" name="deadlineId" >
	        <option value="">归档期限</option>
	     </select>
     </td>
     <td>
	    <input type="button" value="查询" class="BigButton" onclick="queryRMSFiles();"/>
     </td>
  </tr>
</table>

<br>
<div id="listContainer" style="display:none;width:100%;"></div>
<div id="msrg"></div>
</body>
</html>