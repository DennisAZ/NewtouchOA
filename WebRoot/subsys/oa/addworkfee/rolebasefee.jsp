	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<%@ include file="/core/inc/header.jsp" %>
	<%@ page import="java.util.List, yh.subsys.oa.addworkfee.data.*" %>
<html>
<%
  List<YHRoleBaseFee> fees = ( List<YHRoleBaseFee>)request.getAttribute("fees");
  YHRoleBaseFee fee = (YHRoleBaseFee)request.getAttribute("fee");
  String seqId = "";
  String base = "";
  String noradd = "";
  String festadd = "";
  String weekadd = "";
  String userId = "";
  String name = "";
  if(fee != null){
    seqId = fee.getSeqId() + "";
    base = fee.getBaseAdd()+"";
    noradd = fee.getNormalAdd()+"";
    festadd = fee.getFestivalAdd()+"";
    weekadd = fee.getWeekAdd() + "";
    userId = fee.getRoleId() + "";
    name = fee.getName();
  }
%>
<head>
<title></title>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"> 

var seqId = "<%=seqId%>";
var base =  "<%=base%>";
var noradd = "<%=noradd%>";
var festadd = "<%=festadd%>";
var weekadd = "<%=weekadd%>";
var userId = "<%=userId%>";
var name = "<%=name%>";
   function doInit(){
     if(seqId){
       $("seqId").value = seqId;
       $("form1").action = contextPath + "/yh/subsys/oa/addworkfee/act/YHRoleBaseFeeAct/updatRoleBaseFee.act";
     }
     if(base){
       $("baseAdd").value = base;
     }
     if(noradd){
       $("normaladd").value = noradd;
     }
     if(festadd){
       $("festivaladd").value = festadd;
     }
     if(weekadd){
       $("weekadd").value = weekadd;
     }
     if(userId){
       $("role").value = userId;
     }
     if(name){
       $("roleDesc").value = name;
     }
   }

   function clearUser(){
     $("role").value = "";
     $("roleDesc").value = "";
   }

   function doSubmit(){
     if(checkVal()){
       $("form1").submit();
       return ;
     }
   }

   function checkVal(){
    var role = $("role").value;
    var normaladd = $("normaladd").value;
    var festivaladd = $("festivaladd").value;
    var weekadd = $("weekadd").value;
    var base = $("baseAdd").value;
    var flag = true;
    if(role=="" || role==null || role.replaceAll(" ","")==""){
      $("roleMsg").innerHTML = "*角色不能为空";
      flag = false;
     }else{
       $("roleMsg").innerHTML = "";
     }
    
    if(base=="" || base==null || base.replaceAll(" ","")==""){
      $("baseAddMsg").innerHTML = "*加班费基数必填";
      flag = false;
    }else if(!isNumber(base)){
      $("baseAddMsg").innerHTML = "*加班费基数必须位数字";
      flag = false;
    }else if((base)<=0){
      $("baseAddMsg").innerHTML = "*加班费基数必须大于0";
      flag = false;
    }else{
      $("baseAddMsg").innerHTML = "";
    }
    
    if(normaladd=="" || normaladd==null || normaladd.replaceAll(" ","")==""){
      $("normaladdMsg").innerHTML = "*平时加班费为必填";
      flag = false;
    }else if(!isNumber(normaladd)){
      $("normaladdMsg").innerHTML = "*平时加班费必须位数字";
      flag = false;
    }else if((normaladd)<=0){
      $("normaladdMsg").innerHTML = "*平时加班费必须大于0";
      flag = false;
    }else{
      $("normaladdMsg").innerHTML = "";
    }
    if(weekadd=="" || weekadd==null || weekadd.replaceAll(" ","")==""){
      $("weekaddMsg").innerHTML = "*节假日加班费为必填";
      flag = false;
    }else if(!isNumber(weekadd)){
      $("weekaddMsg").innerHTML = "*节假日加班费必须位数字";
      flag = false;
    }else if((weekadd)<=0){
      $("weekaddMsg").innerHTML = "*节假日加班费必须大于0";
      flag = false;
    }else{
      $("weekaddMsg").innerHTML = "";
    }

    if(festivaladd=="" || festivaladd==null || festivaladd.replaceAll(" ","")==""){
      $("festivaladdMsg").innerHTML = "*节假日加班费为必填";
      flag = false;
    }else if(!isNumber(festivaladd)){
      $("festivaladdMsg").innerHTML = "*节假日加班费必须位数字";
      flag = false;
    }else if((festivaladd)<=0){
      $("festivaladdMsg").innerHTML = "*节假日加班费必须大于0";
      flag = false;
    }else{
      $("festivaladdMsg").innerHTML = "";
    }
    return flag;
   } 

   function editOPt(seqId){
     
 		  window.location.href = contextPath + "/yh/subsys/oa/addworkfee/act/YHRoleBaseFeeAct/editRoleBaseFee.act?seqId="+seqId;
 		
     return false;
   }

   function delOPt(seqId){
     if(confirm("确实要删除此项么?")){
 		   window.location.href = contextPath + "/yh/subsys/oa/addworkfee/act/YHRoleBaseFeeAct/delRoleBaseFee.act?seqId="+seqId;
 		   return false;
 		 }
  }
   
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/styles/style3/img/infofind.gif" align="absmiddle"><span class="big3">加班费基数设置</span>
    </td>
  </tr>
</table>
<br>
<form action="<%=contextPath%>/yh/subsys/oa/addworkfee/act/YHRoleBaseFeeAct/delRoleBaseFee.act" id="form2" name="form2">
  <input type="hidden" name="seqIdDel" id="seqIdDel" value=""></input>
</form>
<form action="<%=contextPath%>/yh/subsys/oa/addworkfee/act/YHRoleBaseFeeAct/addRoleBaseFee.act" id="form1" name="form1">
<table class="TableBlock" align="center" width="35%;">
   <tr >
      <td nowrap class="TableContent">角色：<font style="color:red">*</font>&nbsp;</td>
      <td class="TableData">
        <input type="hidden" id="role" name="roleId" value="">
        <textarea cols=25 id="roleDesc" name="roleName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectRole('', 6);return false;">添加</a>
        <a href="javascript:;" class="orgClear" onclick="clearUser();return false;">清空</a><br>
        <span id="roleMsg" style="color:red"></span>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableContent">加班基数：<font style="color:red">*</font> &nbsp;</td>
      <td class="TableData">
        <input type="text" name="baseAdd" id="baseAdd" value="">&nbsp;元/小时<span id="baseAddMsg" style="color:red"></span>&nbsp;
      </td>
    </tr>
   <tr>
<tr>
      <td nowrap class="TableContent">平时加班：<font style="color:red">*</font> &nbsp;</td>
      <td class="TableData">
        <input type="text" name="normaladd" id="normaladd" value="1.5"><span id="normaladdMsg" style="color:red"></span>&nbsp;
      </td>
    </tr>
   <tr>
      <td nowrap class="TableContent">节假日加班：<font style="color:red">*</font> &nbsp;</td>
      <td class="TableData">
        <input type="text" name="festivaladd" id="festivaladd" value="3"></input><span id="festivaladdMsg" style="color:red"></span>
      </td>
    </tr>
      <tr>
      <td nowrap class="TableContent">周末加班：<font style="color:red">*</font>&nbsp;</td>
      <td class="TableData">
        <input type="text" name="weekadd" id="weekadd" value="2"><span id="weekaddMsg" style="color:red"></span>
      </td>
    </tr>
    <tr class="TableControl">
    <td nowrap align="center" colspan="2">
     <input type="button" value="保存" class="BigButton" title="保存" onClick="doSubmit();return false;">&nbsp;&nbsp;
    </td>
   </tr> 
    <input type="hidden" name="seqId" id="seqId" value="">
</table>
</form>
<br>
<%
  if(fees != null && fees.size() > 0){
%>
 <table width="50%" border="0" class="TableList pgTable" id="dataTable" align="center">
  <tbody>
  <tr class="TableTr" id="dataHeader">
	  <td width="6%" align="center"  class="TableHeader">角色名&nbsp;</td>
	  <td width="6%" align="center"  class="TableHeader">平时加班&nbsp;</td>
	  <td width="5%" align="center"  class="TableHeader">节假日加班&nbsp;</td>
	  <td width="4%" align="center"  class="TableHeader">周末加班&nbsp; </td>
	  <td width="5%" align="center"  class="TableHeader">操作&nbsp;</td>
 </tr>
 </tbody>
  <%
    for(int i=0; i< fees.size(); i++){
      YHRoleBaseFee currFee = fees.get(i);
  %>
    <tr class="TableLine2">
		  <td align="center"  nowrap class="TableContent">
		            <%=currFee.getName() %>&nbsp;
		  </td>
			<td align="center" style="word-wrap: break-word;">
			    <%=currFee.getBaseAdd() * currFee.getNormalAdd() %>
			</td>
			<td align="center" style="word-wrap: break-word;">
			   <%=currFee.getBaseAdd() * currFee.getFestivalAdd() %>
			</td>
			<td align="center" style="word-wrap: break-word;">
        <%=currFee.getBaseAdd() * currFee.getWeekAdd() %>
      </td>
			<td align="center" style="word-wrap: break-word;"><a href="javascript:void(0);" onclick="editOPt('<%=currFee.getSeqId()%>')">修改</a>&nbsp;&nbsp;<a href="javascript:delOPt('<%=currFee.getSeqId()%>');">删除</a></td>
	  </tr>
  <%}%>
</table>
 <%}%>
<div id="projectDiv"></div>
<div id="returnNull"></div>
</body>
</html>
