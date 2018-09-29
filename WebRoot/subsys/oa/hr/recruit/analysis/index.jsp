<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String module = request.getParameter("module") == null ? "" :  request.getParameter("module");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>统计分析</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var module = "<%=module%>";
function doInit(){
    getSecretFlag("POOL_POSITION","position");
}

function doSubmit(){
  var chose = "";
  var sumField = "";
 var position="";
  var mapTypeStr = document.getElementsByName("MAP_TYPE");
  for(var i = 0; i < mapTypeStr.length; i++){
    if(mapTypeStr[i].checked)
      chose = mapTypeStr[i].value;
  } 

  var sumFieldStr = document.getElementsByName("SUMFIELD");
  for(var i = 0; i < sumFieldStr.length; i++){
    if(sumFieldStr[i].checked)
      sumField = sumFieldStr[i].value;
  }   
  
  position=$('position').value; 

  var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/analysis/act/YHHrRecruitAnalysisAct/getAnalysis.act?sumField="+sumField+"&position="+position;
  if(sumField == 2){
	  url = url + "&ageRange="+$('ageRange').value;
  }

  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ; 
  }
  var data = rtJson.rtData.data;

  location = "<%=contextPath%>/subsys/oa/hr/recruit/analysis/analysis.jsp?chose="+chose+"&sumField="+sumField+"&data="+data;
}

function sel_change(){
  if($("SUMFIELD2").checked){
    document.all("AGE").style.display = "";
  }else{
    document.all("AGE").style.display = "none";
  }
}
//
function getSecretFlag(parentNo,optDiv,extValue){
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectChildCode.act?parentNo=" + parentNo;
	var rtJson = getJsonRs(requestURLStr);
	if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ;
	}
	var prcs = rtJson.rtData;
	var selects = document.getElementById(optDiv);
	for(var i=0;i<prcs.length;i++){
	  var prc = prcs[i];
	  var option = document.createElement("option"); 
	  option.value = prc.seqId; 
	  option.innerHTML = prc.codeName; 
	  selects.appendChild(option);
	  if(extValue && (extValue == prc.value)){
      option.selected = true;
    }
	}
}


</script>
</head>
<body class="bodycolor" topmargin="5"  onLoad="doInit()" >
<form action="" method="post" name="form1" id="form1" >
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/finance1.gif" align="absMiddle"><span class="big3">&nbsp;人才统计分析 </span>
   </td>
 </tr>
</table>
<br>
<table align="center" width="50%" class="TableBlock">
    <tr>
      <td nowrap  class="TableContent" width="80" >基本信息：</td>
      <td class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" onClick="sel_change()"><label for="SUMFIELD1">学历</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD2' value="2" onClick="sel_change()"><label for="SUMFIELD2">年龄</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD3' value="3" onClick="sel_change()"><label for="SUMFIELD3">性别</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD4' value="4" onClick="sel_change()"><label for="SUMFIELD4">专业</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD9' value="5" onClick="sel_change()"><label for="SUMFIELD9">籍贯</label>&nbsp;      
         <input type="radio" name='SUMFIELD' id='SUMFIELD6' value="6" checked onClick="sel_change()"><label for="SUMFIELD6">期望工作性质</label>&nbsp;
   
      </td>
    </tr>
    <tr id="AGE" style="display:none">
	<td nowrap class="TableContent" width="80">年龄范围：</td>
    <td class="TableData" nowrap  colspan="3">
	<input type="text" name="ageRange" id="ageRange" size="50" maxlength="100" class="BigInput" value="0-25,26-30,31-35,36-40,41-45,46-50,51-55,56-60">
	</td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80" >统计图：</td>
      <td class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='MAP_TYPE' id='MAP_TYPE' value="1" checked><label for="SUMFIELD1">饼图</label>&nbsp;
         <input type="radio" name='MAP_TYPE' id='MAP_TYPE' value="2" ><label for="SUMFIELD2">柱状图</label>&nbsp;
        
      </td>
    </tr>



    <tr>
      <td nowrap class="TableContent" width="80" >应聘岗位：</td>
      <td class="TableData" nowrap   colspan="3">
          <select name="position" id="position"  title="应聘岗位可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">应聘岗位&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
      </td>
    </tr>

    <tr>
      <td nowrap class="TableContent"  colspan="4" >
	  <div align="center">
	  <input type="button"  class="BigButton" value="确定" onClick="doSubmit()" /><input class="BigButton" type="reset"  value="清空" onClick="location='index.jsp'">
	  </div>
	  </td>
     
    </tr>

    </table>
</form>
</body>
</html>
