	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<%@ include file="/core/inc/header.jsp" %>
	<%@ page import="java.util.List, yh.subsys.oa.addworkfee.data.*" %>
<html>
<%
  List<YHFestival> fets = (List<YHFestival>)request.getAttribute("fets");
  int year = (Integer)request.getAttribute("year");
  YHFestival fest = (YHFestival)request.getAttribute("fest");
  String pYear = "";
  String pName = "";
  String begin = "";
  String end = "";
  String seqId = "";
  if(fest != null){
    pYear = fest.getYear()+"";
    pName = fest.getFeName();
    begin = fest.getBeginDateStr();
    end = fest.getEndDateStr();
    seqId = fest.getSeqId()+"";
  }
  String yearsList = (String)request.getAttribute("yearList");
  if(yearsList==null || yearsList.length()<1){
    yearsList = "[]";
  }
  String edit = (String)request.getAttribute("edit");
%>
<head>
<title>节假日设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css"/>

<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>

<script type="text/javascript"> 
  var pYear = "<%=pYear%>";
  var pName = "<%=pName%>";
  var begin = "<%=begin%>";
  var end = "<%=end%>";
  var yearList = "<%=yearsList%>";
  var yeartemp = "<%=year%>";
  var eSeqId = "<%=seqId%>";
  function parseList(yearList){
    var temp = "";   
    var sel = document.getElementById("selYears");
     if(yearList.length > 0 && sel){
       for(var i=0; i<yearList.length; i++ ){         
         sel.options.add(new Option(yearList[i], yearList[i]));
       }
       $("selYears").value = yeartemp;
     }
  }
  
	function doInit(){
	  var beginParameters = {
	      inputId:'startTime',
	      property:{isHaveTime:false}
	      ,bindToBtn:'beginDateImg'
	      };
	  new Calendar(beginParameters);
	  
	  var endParameters = {
	      inputId:'endTime',
	      property:{isHaveTime:false}
	      ,bindToBtn:'endDateImg'
	      };
	  new Calendar(endParameters);
	  updateOpt();
    var yearObj = eval("(" + yearList +")");
    if(yearList){
      parseList(yearObj);
    }
    if(!pYear){
      $("year").value = "<%=year%>";
    }
	}

	function updateOpt(){
	  if(pYear){
     $("year").value=pYear;
     $("form1").action = contextPath + "/yh/subsys/oa/addworkfee/act/YHFestivalAct/updateFestival.act";
		}
	  if(pName){
     $("festName").value = pName;
		}
	  if(begin){
	    $("startTime").value = begin;
		}
	  if(end){
	    $("endTime").value = end;
		}
		if(eSeqId){
		  $("seqId").value = eSeqId;
		}
   
	}
	
	function doSubmit(){
    if(doCheckInval()){
      $("form1").submit();
      return;
    }
	}

	function doCheckInval(){
    var flag = true;
    var year = $("year").value;
    var fName = $("festName").value;
    var startTime = $("startTime").value;
    var endTime = $("endTime").value;

    if(year == "" || year == null || year.replaceAll(" ","")==""){
      $("yearMsg").innerHTML = "*年份为必填";
      flag = false;
    }else if(!isInteger(year)){
      $("yearMsg").innerHTML = "*年份必须为整数";
      flag = false;
    }else if(year<=0){
      $("yearMsg").innerHTML = "*年份必须大于0";
      flag = false;
    }else{
      $("yearMsg").innerHTML = "";
    }

    if(fName == "" || fName == null || fName.replaceAll(" ","")==""){
      $("festivaladdMsg").innerHTML = "*节假日名称为必填";
      flag = false;
    }else{
      $("festivaladdMsg").innerHTML = "";
    }

    if(startTime == "" || startTime == null || startTime.replaceAll(" ","")==""){
      $("startTimeMsg").innerHTML = "*开始日期不能为空";
      flag = false;
    }else if(!isValidDateStr(startTime)){
      $("startTimeMsg").innerHTML = "*时间格式不正确,应如1999-10-10";
      flag = false;
    }else{
      $("startTimeMsg").innerHTML = "";
    }

    if(endTime == "" || endTime == null || endTime.replaceAll(" ","")==""){
      $("endTimeMsg").innerHTML = "*结束日期不能为空";
      flag = false;
    }else if(!isValidDateStr(endTime)){
      $("endTimeMsg").innerHTML = "*时间格式不正确,应如1999-10-10";
      flag = false;
    }else if(endTime < startTime){
      $("endTimeMsg").innerHTML = "*结束日期不能小于开始日期";
      flag = false;
    }else{
      $("endTimeMsg").innerHTML = "";
    }

    if(startTime &&　year){
      var start = startTime.substring(0, startTime.indexOf("-"));
      var end = startTime.substring(startTime.indexOf("-"));
      if(year != start){
        $("startTimeMsg").innerHTML = "*年份不一致应为:"+year + end ;
        flag = false;
      }else{
        $("startTimeMsg").innerHTML = "" ;
      }
    }

    if(endTime　&& year){
      var start = endTime.substring(0, startTime.indexOf("-"));
      var end = endTime.substring(startTime.indexOf("-"));
      if(year != start){
        $("endTimeMsg").innerHTML = "*年份不一致应为:"+year + end ;
        flag = false;
      }else{
        $("endTimeMsg").innerHTML = "" ;
      }
    }

    return flag;    
	}

	function delOpt(seqId, year){
	  if(confirm("确实要删除此项么?")){
		  window.location.href = contextPath + "/yh/subsys/oa/addworkfee/act/YHFestivalAct/delFestvial.act?seqId="+seqId+"&year="+year;
	  }
    return false;
	}
	
	function editOpt(seqId){
		
		  window.location.href = contextPath + "/yh/subsys/oa/addworkfee/act/YHFestivalAct/editFestival.act?seqId="+seqId;
		
    return false;
	}	

	function onSelChange(){
    var year = $("selYears").value;
    window.location.href = contextPath + "/yh/subsys/oa/addworkfee/act/YHFestivalAct/findFestvialList.act?year="+year;
    return false;
  }
</script>
</head>
<body class="bodycolor" topmargin="5px" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3"><%=year%>节假日设置</span>
    </td>
  </tr>
</table>
<br>
<form action="<%=contextPath%>/yh/subsys/oa/addworkfee/act/YHFestivalAct/addFestival.act" id="form1" name="form1">
<table class="TableBlock" align="center"  width="35%;">
      <td nowrap class="TableContent">年份：<font style="color:red">*</font> &nbsp;</td>
      <td class="TableData">
        <input type="text" name="year" id="year" value="">&nbsp;<span id="yearMsg" style="color:red"></span>
      </td>
    </tr>
   <tr>
      <td nowrap class="TableContent">名称：<font style="color:red">*</font> &nbsp;</td>
      <td class="TableData">
        <input type="text" name="festName" id="festName" value=""></input>&nbsp;<span id="festivaladdMsg" style="color:red"></span>
      </td>
    </tr>
  <tr>
		    <td nowrap class="TableContent">开始日期：<font style="color:red">*</font>&nbsp;</td>
		    <td nowrap class="TableData">
			    <input type="text" id="startTime" onfocus="this.select();" name="startTime" class="BigInput" style="width:80px;" />
	        <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" />
	        <span id="startTimeMsg" style="color:red"></span> 
		    </td>
		     
	</tr>
  <tr>
		    <td nowrap class="TableContent">结束日期：<font style="color:red">*</font>&nbsp;</td>
		    <td nowrap class="TableData">
			    <input type="text" id="endTime" onfocus="this.select();" name="endTime" class="BigInput"  style="width:80px;" />
	        <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" />
	         <span id="endTimeMsg" style="color:red"></span> 
		    </td>
		   <input type="hidden" id="seqId" onfocus="this.select();" name="seqId" class="BigInput"  style="width:80px;" />
	</tr>
    <tr class="TableControl">
    <td nowrap align="center" colspan="2">
     <input type="button" value="保存" class="BigButton" title="保存" onClick="doSubmit();return false;">&nbsp;&nbsp;
    </td>
   </tr> 
</table>
</form>
<br>
<%
    if(fets != null && fets.size() > 0){%>
<table width="50%" border="0" class="TableList pgTable" id="dataTable" align="center">
  <tbody>
 
      <tr class="TableTr" id="dataHeader">
	      <td colspan="4" align="center"  class="TableHeader">
	      <select id="selYears" name="selYears" onchange="onSelChange();">
	        
	      </select>年份的节假日&nbsp;</td>
     </tr>
   
 </tbody>
 
    <tr class="TableLine2" >
		  <td width="6%" align="center"  class="TableContent">名称</td>
		  <td width="6%" align="center"  class="TableContent">开始时间&nbsp;</td>
		  <td width="5%" align="center"  class="TableContent">结束时间&nbsp;</td>
		  <td width="5%" align="center"  class="TableContent">操作&nbsp;</td>
    </tr>
    <%
      for(int i=0; i<fets.size(); i++){%>
      	<tr class="TableLine2">
    	  <td align="center"  nowrap class="TableContent">
    	            <%=fets.get(i).getFeName() %>&nbsp;
    	  </td>
    		<td align="center" style="word-wrap: break-word;">
    		    <%=fets.get(i).getBeginDateStr() %>
    		</td>
    		<td align="center" style="word-wrap: break-word;"> <%=fets.get(i).getEndDateStr() %></td>
    		<td align="center" style="word-wrap: break-word;"><a href="javascript:void(0);" onclick="editOpt('<%=fets.get(i).getSeqId() %>');">修改</a>  <a href="javascript:void(0);" onclick="delOpt('<%=fets.get(i).getSeqId() %>','<%=fets.get(i).getYear() %>')">删除</a></td>
    	</tr>
     <% }
    %>
 <%}%>
 <!--
 
 
  <tr class="TableLine2" >
	  <td width="6%" align="center"  class="TableContent">名称</td>
	  <td width="6%" align="center"  class="TableContent">开始时间&nbsp;</td>
	  <td width="5%" align="center"  class="TableContent">结束时间&nbsp;</td>
	  <td width="5%" align="center"  class="TableContent">操作&nbsp;</td>
 </tr>
	<tr class="TableLine2">
	  <td align="center"  nowrap class="TableContent">
	            国庆节&nbsp;
	  </td>
		<td align="center" style="word-wrap: break-word;">
		    2010-10-01
		</td>
		<td align="center" style="word-wrap: break-word;"> 2010-10-07</td>
		<td align="center" style="word-wrap: break-word;">修改  删除</td>
	</tr>
	<tr class="TableLine2">
	  <td align="center"  nowrap class="TableContent">
	            中秋&nbsp;
	  </td>
		<td align="center" style="word-wrap: break-word;">
		    2010-10-01
		</td>
		<td align="center" style="word-wrap: break-word;"> 2010-10-07</td>
		<td align="center" style="word-wrap: break-word;">修改  删除</td>
	</tr>
-->
</table>

<div id="projectDiv"></div>
<div id="returnNull"></div>
</body>
</html>
