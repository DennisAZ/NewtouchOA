<%@ page language="java" contentType="text/html;  charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
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
     //获取年的第一天和最后一天
c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));      
String yearLastDate=matter.format(c.getTime());
String yearFirstDate=ton.substring(0,5)+"01-01";


%>


<html>
<head>
<title>工作统计</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<style type="text/css">
.Dialog { aborder: 1px #9F9F9F solid;  background: #FFF; padding: 0px;margin:100px 400px; clear: both; position: absolute;  z-index: 10000;display:none;}
#overlay {display: none; z-index: 10000; width:100%; height:100%;BACKGROUND: #000; filter: alpha(opacity=20);LEFT: 0px; POSITION: absolute; TOP: 0px; -moz-opacity:0.2;opacity:0.2;}
.TableBlock TD
{
   padding:0px;
}
</style>
</head>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript">
jQuery.noConflict(); 
</script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/funcs/workstat/js/workStat.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/funcs/workstat/js/utill.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/funcs/workstat/js/Jquery/divData.js"></script>
<script>
function doInit(){
	// showGif();
//部门    
	getDeptFunc("deptId");
	setDate();
	deptSelect("deptId");
    isStaticPerson("deptId");
	    $("startDate").value='<%=mon%>';
	    $("endDate").value='<%=sun%>';
	   
	     loadData();
	   // hideGif();
}

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

function dept_more()
{     
  var objSelect = document.getElementById("overlay");
		  objSelect.style.display="block";
 var objSelect = document.getElementById("optionBlock");
 objSelect.style.display="block";
	
}
function HideDialog()
{
  var objSelect = document.getElementById("overlay");
		  objSelect.style.display="none";
var objSelect = document.getElementById("optionBlock");
		  objSelect.style.display="none";
clearDept();
}
function clearDept(){
	$('requDept').value = "";
	$('requDeptDesc').value = "";
}

function HideDialog1(id)
{
   $("overlay").style.display = 'none';
   $(id).style.display = 'none';
}
</script>
</head>

<body class="bodycolor" topmargin="5" onLoad="doInit();">
<form name="form1" action=""  onSubmit="return CheckForm();">
<div class="small" style="clear:both;">
<table align="center" width="100%">
<tr>
   <td>
   <div style="float:left;">

   <select name="deptId" onChange="reSetNum();deptLoadData();"  id="deptId" class="SmallSelect" >
   <option value="-1">所有部门</option>
   
   </select>

    <input type="text"  name="startDate" id="startDate" size="6" maxlength="10"  class="BigInput" value="" readonly="true">
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
   至&nbsp;
    <input type="text"  name="endDate" id="endDate" size="6" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
  
   <input type="hidden" id="OPERATION" name="OPERATION" value="">
   <a href="javascript:dept_more()" title="选择更多部门进行统计">更多部门</a>
   <input type="button" value="统计" class="SmallButtonA" title="统计" onClick="reSetNum();deptLoadData();">
   <input type="button" value="导出" class="SmallButtonA" title="导出Excel文件" onClick="loadExcelData()">
 </div>
 <div style="float:right;">
   <input type="button" value="今天" class="SmallButtonA" title="今天" onClick="dateLoadData('<%=ton%>','<%=ton%>')">
   <input type="button" value="本周" class="SmallButtonA" title="本周" onClick="dateLoadData('<%=mon%>','<%=sun%>')">
   <input type="button" value="本月" class="SmallButtonA" title="本月" onClick="dateLoadData('<%=monthFirstDate%>','<%=monthLastDate%>')">
   <input type="button" value="今年" class="SmallButtonA" title="今年" onClick="dateLoadData('<%=yearFirstDate%>','<%=yearLastDate%>')">
   
 </div>
   </td>
</tr>
 </table>
</div>
<div id="overlay"></div>
<div id="optionBlock" class="Dialog" style="display:none;width:400px;">
   <h4 class="header">
      <span id="optionBlockTitle" class="title">更多部门</span>
      <a class="operation" href="javascript:HideDialog();"><img src="<%=imgPath%>/close.png"/></a>
  </h4>
  <center>
    <input type="hidden" name="requDept" id="requDept" value="" >
        <textarea name="requDeptDesc" id="requDeptDesc" cols="35" rows="4" class="BigStatic" value="" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectDept(['requDept', 'requDeptDesc'],8);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="clearDept()">清空</a>
	  <br />
	  <br />
	  <input type="hidden" name="more" id="more" value="" />
	  <input class="BigButton" onClick="reSetNum();moreLoadData();" type="button" value="确定"/>&nbsp;&nbsp;
     <input class="BigButton" onClick="HideDialog()" type="button" value="取消"/>
  </center>
</div>
</form>

<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312"></head>
<div align="center" id="showTableData">



</div>
<div align="center" style="display:none" id="showNoRight">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg forbidden"><div class='content' style='font-size:12pt'>您没有查看该部门的权限</div></td>
  </tr>
  </table>
</div>
<div align="center" style="display:none" id="showNoData">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>无可管理用户</div></td>
  </tr>
  </table>
				   
</div>
<div  style="display:none;"  style=" width:35px; height:35px; position:fixed; right:50px; bottom:30px;display:block;" id="getingData">
<img  src="/yh/core/styles/style1/img/loading.gif" width="35" height="35" />
</div>
 </body>
</html>
