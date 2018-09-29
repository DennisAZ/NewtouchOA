<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat" %>
<%
  String module = request.getParameter("module") == null ? "" :  request.getParameter("module");
%>
	<%
	  Date date=new Date();
	SimpleDateFormat matter=new SimpleDateFormat("yyyy-MM-dd");
	
	
	Calendar c = Calendar.getInstance();
	c.setFirstDayOfWeek(Calendar.MONDAY); // 如果以周一为一周第一天，默认周日
	c.setTime(date);
	c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 如果以周一为一周第一天

	Date d = c.getTime();
	String mon=matter.format(d);
	c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
	Date s=c.getTime();
	String sun=matter.format(s);
	String ton=matter.format(date);
	
	
	    //获取月的第一天，最后一天

	c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));      
	String monthLastDate=matter.format(c.getTime());
	String monthFirstDate=ton.substring(0,8)+"01";
	String month=ton.substring(6,7);    
	//获取年的第一天和最后一天

	c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));      
	String yearLastDate=matter.format(c.getTime());
	String yearFirstDate=ton.substring(0,5)+"01-01";
	     //获得去年的第一天和最后一天

	String yes=yearLastDate.substring(0,4);
	int y=Integer.parseInt(yes);
	     y-=1;
	String lastYearF=y+"-01-01";
	String lastYearL=y+"-12-31";
	      //获得上上年第一天和最后一天

	int yy=y-1;
	String llastYearF=yy+"-01-01";
	String llastYearL=yy+"-12-31";
	      //获取季度的第一天和最后一天

	String one=ton.substring(0,4)+"-01-01";
	String four=ton.substring(0,4)+"-04-01";
	String sewen=ton.substring(0,4)+"-07-01";
	String ten=ton.substring(0,4)+"-10-01";
  String thr=ton.substring(0,4)+"-03-31";
  String six=ton.substring(0,4)+"-06-30";
  String nine=ton.substring(0,4)+"-09-31";
  String twn=ton.substring(0,4)+"-12-31";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>统计分析</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/index.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/cmp/Calendar.css" />
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/customer.css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/javascript">
var module = "<%=module%>";
function doInit(){
	if(module!="HR_LZ1" && module!="HR_DD1" ){doSubmit();}
	else{
	setDate();
	setThisYear();
 }
}

//日期
function setDate(){
var date1Parameters = {
  inputId:'startDate',
  property:{isHaveTime:false}
  ,bindToBtn:'date1'
};
new Calendar(date1Parameters);

var date2Parameters = {
  inputId:'endDate',
  property:{isHaveTime:false}
  ,bindToBtn:'date2'
};
new Calendar(date2Parameters);
}



function doSubmit(){
  var chose = "";
  var sumField = "";
  var deptId = $("deptId").value;
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
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/analysis/act/YHHrStaffAnalysisAct/getAnalysis.act?module="+module+"&sumField="+sumField+"&deptId="+deptId;
  if(module == 'HR_INFO' && sumField == 2){
	  url = url + "&ageRange="+$('ageRange').value
  }
  
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ; 
  }
  var data = rtJson.rtData.data;
  parent.tumain.location = "<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/analysis.jsp?deptId="+deptId+"&chose="+chose+"&sumField="+sumField+"&data="+data;
}

function setThisYear(){
   $('startDate').value='<%=yearFirstDate%>';
	   $('endDate').value='<%=yearLastDate%>';
}
function setLastYear(){
	 $('startDate').value='<%=lastYearF%>';
   $('endDate').value='<%=lastYearL%>';
}
function setLLastYear(){
	   $('startDate').value='<%=llastYearF%>';
	   $('endDate').value='<%=llastYearL%>';
	}

function setThisMonth(){
    $('startDate').value='<%=monthFirstDate%>';
    $('endDate').value='<%=monthLastDate%>';
}
function setThisSeason(){
	var month='<%=month%>';
	var index=(parseFloat(month)-1)/3+1;
	index=parseInt(index);

  if(index==1){
	    $('startDate').value='<%=one%>';
	    $('endDate').value='<%=thr%>';
	  }
  else if(index==2){
      $('startDate').value='<%=four%>';
      $('endDate').value='<%=six%>';
    }
  else if(index==3){
      $('startDate').value='<%=sewen%>';
      $('endDate').value='<%=nine%>';
    }
  else if(index==4){
      $('startDate').value='<%=ten%>';
      $('endDate').value='<%=twn%>';
    }
}

function setTime(){
  var select=$("selectId").value;
	if(select=="thisYear"){
       setThisYear();
		}
	else if(select=="lastYear1"){
       setLastYear();
		}
	else if(select=="lastYear2"){
       setLLastYear();
		}
	else if(select=="thisseason"){
       setThisSeason();
		}
	else if(select=="thismonth"){
      setThisMonth();
		}
}



function doSubmit1(){
  var chose = "";
  var sumField = "";
  var deptId = $("deptId").value;
  var mapTypeStr = document.getElementsByName("MAP_TYPE");
  for(var i = 0; i < mapTypeStr.length; i++){
    if(mapTypeStr[i].checked)
      chose = mapTypeStr[i].value;
  } 

  var startDate = $("startDate").value;
  var endDate = $("endDate").value;

 if(doCheck()){
  if(chose=='0'){
	  parent.tumain.location = "<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/analysisList.jsp?deptId="+deptId+"&startDate="+startDate+"&endDate="+endDate+"&module="+module;

 }else {
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/analysis/act/YHHrStaffAnalysisAct/getAnalysisTwo.act?deptId="+deptId+"&startDate="+startDate+"&endDate="+endDate+"&module="+module;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ; 
  }
  var data = rtJson.rtData.data;
  parent.tumain.location = "<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/analysis.jsp?deptId="+deptId+"&chose="+chose+"&data="+data;
  }
 }
}
function doCheck(){
	  var startDate = $("startDate").value;
	  var endDate = $("endDate").value;
	  var deptId = $("deptId").value;
  if(deptId==""){
     alert("部门不能留空");
     return false;
	  }
	if(startDate==""){
		  alert("开始时间不能留空");
		     return false;
		}
	if(endDate==""){
		  alert("结束时间不能留空");
		     return false;
	}
	if (startDate>=endDate){
		  alert("开始时间必须小于结束时间");
		     return false;
		}
	return true;
}


function sel_change(){
  if($("SUMFIELD2").checked){
    document.all("AGE").style.display = "";
  }else{
    document.all("AGE").style.display = "none";
  }
}

</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit();">
<form action="" method="post" name="form1" id="form1" target="tu_main">
<table align="center" width="98%" class="TableBlock" style="margin-top: 3px;">
<%if("HR_INFO".equals(module)) {%>
    <tr>
      <td nowrap class="TableContent" width="80" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" checked onClick="sel_change()"><label for="SUMFIELD1">学历</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD2' value="2" onclick="sel_change()"><label for="SUMFIELD2">年龄</label>&nbsp;
         <span style="display:none" id="AGE">
         	<input type="text" name="ageRange" id="ageRange" size="10" maxlength="100" class="BigInput" value="0-25,26-30,31-35,36-40,41-45,46-50,51-55,56-60">
         </span>
         <input type="radio" name='SUMFIELD' id='SUMFIELD3' value="3" onClick="sel_change()"><label for="SUMFIELD3">性别</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD4' value="4" onClick="sel_change()"><label for="SUMFIELD4">政治面貌</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD9' value="5" onClick="sel_change()"><label for="SUMFIELD9">在职状态</label>&nbsp;<br>
         <input type="radio" name='SUMFIELD' id='SUMFIELD5' value="6" onClick="sel_change()"><label for="SUMFIELD5">籍贯</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD6' value="7" onClick="sel_change()"><label for="SUMFIELD6">职称</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD7' value="8" onClick="sel_change()"><label for="SUMFIELD7">员工类型</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD8' value="9" onClick="sel_change()"><label for="SUMFIELD8">加入本单位时间</label>&nbsp;
      </td>
    </tr>
<%} if("HR_HT".equals(module)) {%>

    <tr>
      <td nowrap class="TableContent" width="80" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" checked><label for="SUMFIELD1">合同类型</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD2' value="2" ><label for="SUMFIELD2">合同状态</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD3' value="3" ><label for="SUMFIELD3">合同属性</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD4' value="4" ><label for="SUMFIELD3">签订日期</label>&nbsp;
      </td>
    </tr>

<%} if("HR_JC".equals(module)) {%>

    <tr>
      <td nowrap class="TableContent" width="80" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" checked><label for="SUMFIELD1">奖惩项目</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD2' value="2" ><label for="SUMFIELD2">奖惩属性</label>&nbsp;
      </td>
    </tr>
<%} if("HR_ZZ".equals(module)) {%> 
    <tr>
      <td nowrap class="TableContent" width="80" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" checked><label for="SUMFIELD1">证照类型</label>&nbsp;
         <input type="radio" name='SUMFIELD' id='SUMFIELD2' value="2" ><label for="SUMFIELD2">证照状态</label>&nbsp;
      </td>
    </tr>

<%} if("HR_XX".equals(module)) {%> 
 
    <tr>
      <td nowrap class="TableContent" width="80" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" checked><label for="SUMFIELD1">所获学位</label>&nbsp;
      </td>
    </tr>

<%} if("HR_JL".equals(module)) {%> 
    <tr>
      <td nowrap class="TableContent" width="80" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" checked><label for="SUMFIELD1">所在部门</label>&nbsp;
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD2' value="2" ><label for="SUMFIELD2">担任职位</label>&nbsp;
      </td>
    </tr>

<%} if("HR_JN".equals(module)) {%>   
    <tr>
      <td nowrap class="TableContent" width="80" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" checked><label for="SUMFIELD1">技能名称</label>&nbsp;
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD2' value="2" ><label for="SUMFIELD2">技能级别</label>&nbsp;
      </td>
    </tr>

<%} if("HR_GX".equals(module)) {%>  
    <tr>
      <td nowrap class="TableContent" width="80" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" checked><label for="SUMFIELD1">与本人关系</label>&nbsp;
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD2' value="2" ><label for="SUMFIELD2">政治面貌</label>&nbsp;
      </td>
    </tr>

<%} if("HR_DD".equals(module)) {%> 
    <tr>
      <td nowrap class="TableContent" width="80" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" checked><label for="SUMFIELD1">调动类型</label>&nbsp;
      </td>
    </tr>

<%} if("HR_LZ".equals(module)) {%> 
    <tr>
      <td nowrap class="TableContent" width="80" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" checked><label for="SUMFIELD1">离职类型</label>&nbsp;
      </td>
    </tr>

<%} if("HR_FZ".equals(module)) {%>  
    <tr>
      <td nowrap class="TableContent" width="80" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" checked><label for="SUMFIELD1">复职类型</label>&nbsp;
      </td>
    </tr>

<%} if("HR_ZC".equals(module)) {%> 
    <tr>
      <td nowrap class="TableContent" width="80" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" checked><label for="SUMFIELD1">获取方式</label>&nbsp;
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD2' value="2" ><label for="SUMFIELD2">获取职称</label>&nbsp;
      </td>
    </tr>

<%} if("HR_GH".equals(module)) {%>
    <tr>
      <td nowrap class="TableContent" width="60" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
      	 <input type="radio" name='SUMFIELD' id='SUMFIELD1' value="1" checked><label for="SUMFIELD1">关怀类型</label>&nbsp;
      </td>
    </tr>
<%} if(!"HR_RZ1".equals(module) && !"HR_LZ1".equals(module) && !"HR_DD1".equals(module)) {%>

    <tr>
      <td nowrap class="TableContent" width="60"> 统计图：</td>
      <td nowrap class="TableData" width="130">
         <input type="radio" name='MAP_TYPE' id='MAP_TYPE1' value="1" checked><label for="MAP_TYPE1">饼图</label>&nbsp;<br>
         <input type="radio" name='MAP_TYPE' id='MAP_TYPE2' value="2"><label for="MAP_TYPE2">柱状图</label>&nbsp;
        <!-- <input type="radio" name='MAP_TYPE' id='MAP_TYPE3' value="3"><label for="MAP_TYPE3">线状图</label>&nbsp;      	-->
      </td>
      <td nowrap class="TableContent" width="80"> 所属部门：</td>
      <td nowrap class="TableData" >
         <input type="hidden" name="deptId" id="deptId" value="">
      <textarea cols=30 name="deptIdDesc" id="deptIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea><br>
      <a href="javascript:;" class="orgAdd" onClick="selectDept(['deptId', 'deptIdDesc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('deptId').value='';$('deptIdDesc').value='';">清空</a>
      	<input type="hidden" name="module" value="module">
        <input type="button" value="确定"  onClick="doSubmit()" class="BigButton">&nbsp;&nbsp;        
    </tr> 
    
<%} if("HR_RZ1".equals(module)) {%>
    <tr>
      <td nowrap class="TableContent" width="60" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
        <select class="BigButton" id="selectId">
        <option value="thisYear">本年度</option>
        <option value="lastYear1">上一年</option>
        <option value="lastYear2">上两年</option>
        <option value="thisseason">本季度</option>
        <option value="thismonth">本     月</option>
        </select>
    时间 ： <input type="text" name="startDate" id="startDate" size="10" maxlength="10" class="BigInput" value="" />
     <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      至：
      <input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" value="" />   
     <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
  
      </td>
    </tr>
       <tr>
      <td nowrap class="TableContent" width="60"> 统计图：</td>
      <td nowrap class="TableData" width="300">
          <input type="radio" name='MAP_TYPE' id='MAP_TYPE0' value="0" checked><label for="MAP_TYPE1">列表</label>&nbsp;
         <input type="radio" name='MAP_TYPE' id='MAP_TYPE1' value="1" ><label for="MAP_TYPE1">饼图</label>&nbsp;
         <input type="radio" name='MAP_TYPE' id='MAP_TYPE2' value="2"><label for="MAP_TYPE2">柱状图</label>&nbsp;
        <!-- <input type="radio" name='MAP_TYPE' id='MAP_TYPE3' value="3"><label for="MAP_TYPE3">线状图</label>&nbsp;        -->
      </td>
      <td nowrap class="TableContent" width="80"> 所属部门：</td>
      <td nowrap class="TableData" >
         <input type="hidden" name="deptId" id="deptId" value="">
      <textarea cols=30 name="deptIdDesc" id="deptIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectDept(['deptId', 'deptIdDesc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('deptId').value='';$('deptIdDesc').value='';">清空</a>
        
        <input type="hidden" name="module" value="module">
        <input type="button" value="确定"  onClick="doSubmit1()" class="BigButton">&nbsp;&nbsp;        
    </tr> 

<%} %>
<% if("HR_LZ1".equals(module)) {%>
    <tr>
      <td nowrap class="TableContent" width="60" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
        <select class="BigButton"  onChange="setTime();" id="selectId">
        <option value="thisYear">本年度</option>
        <option value="lastYear1">上一年</option>
        <option value="lastYear2">上二年</option>
        <option value="thisseason">本季度</option>
        <option value="thismonth">本     月</option>
        </select>
    时间 ： <input type="text" name="startDate" id="startDate" size="10" maxlength="10" class="BigInput" value="" />
     <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      至：
      <input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" value="" />   
     <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
  
      </td>
    </tr>
       <tr>
      <td nowrap class="TableContent" width="60"> 统计图：</td>
      <td nowrap class="TableData" width="300">
          <input type="radio" name='MAP_TYPE' id='MAP_TYPE0' value="0" checked><label for="MAP_TYPE1">列表</label>&nbsp;
         <input type="radio" name='MAP_TYPE' id='MAP_TYPE1' value="1" ><label for="MAP_TYPE1">饼图</label>&nbsp;
         <input type="radio" name='MAP_TYPE' id='MAP_TYPE2' value="2"><label for="MAP_TYPE2">柱状图</label>&nbsp;
        <!-- <input type="radio" name='MAP_TYPE' id='MAP_TYPE3' value="3"><label for="MAP_TYPE3">线状图</label>&nbsp;        -->
      </td>
      <td nowrap class="TableContent" width="80"> 所属部门：</td>
      <td nowrap class="TableData" >
         <input type="hidden" name="deptId" id="deptId" value="">
      <textarea cols=30 name="deptIdDesc" id="deptIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectDept(['deptId', 'deptIdDesc'],13);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('deptId').value='';$('deptIdDesc').value='';">清空</a>
        
        <input type="hidden" name="module" value="module">
        <input type="button" value="确定"  onClick="doSubmit1()" class="BigButton">&nbsp;&nbsp;        
    </tr> 

<%} %>
<% if("HR_DD1".equals(module)) {%>
    <tr>
      <td nowrap class="TableContent" width="60" >基本信息：</td>
      <td nowrap class="TableData" nowrap   colspan="3">
        <select class="BigButton" onChange="setTime();" id="selectId">
        <option value="thisYear">本年度</option>
        <option value="lastYear1">上一年</option>
        <option value="lastYear2">上二年</option>
        <option value="thisseason">本季度</option>
        <option value="thismonth">本     月</option>
        </select>
    时间 ： <input type="text" name="startDate" id="startDate" size="10" maxlength="10" class="BigInput" value="" />
     <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      至：
      <input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" value="" />   
     <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
  
      </td>
    </tr>
       <tr>
      <td nowrap class="TableContent" width="60"> 统计图：</td>
      <td nowrap class="TableData" width="300">
          <input type="radio" name='MAP_TYPE' id='MAP_TYPE0' value="0" checked><label for="MAP_TYPE1">列表</label>&nbsp;<br>
         <input type="radio" name='MAP_TYPE' id='MAP_TYPE1' value="1" ><label for="MAP_TYPE1">饼图</label>&nbsp;<br>
         <input type="radio" name='MAP_TYPE' id='MAP_TYPE2' value="2"><label for="MAP_TYPE2">柱状图</label>&nbsp;<br>
        <!-- <input type="radio" name='MAP_TYPE' id='MAP_TYPE3' value="3"><label for="MAP_TYPE3">线状图</label>&nbsp;        -->
      </td>
      <td nowrap class="TableContent" width="80"> 所属部门：</td>
      <td nowrap class="TableData" >
         <input type="hidden" name="deptId" id="deptId" value="">
      <textarea cols=30 name="deptIdDesc" id="deptIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea><br>
      <a href="javascript:;" class="orgAdd" onClick="selectDept(['deptId', 'deptIdDesc'],13);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('deptId').value='';$('deptIdDesc').value='';">清空</a>
        <input type="hidden" name="module" value="module">
        <input type="button" value="确定"  onClick="doSubmit1()" class="BigButton">&nbsp;&nbsp;        
    </tr> 

<%} %>
    </table>
</form>
</body>
</html>
