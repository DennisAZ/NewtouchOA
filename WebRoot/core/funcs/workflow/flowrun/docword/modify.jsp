<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId=request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑文件字</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var seqId='<%=seqId%>';
function doInit(){
	getDocWord(seqId);
  
}


function doSubmit()
{
  
  if(CheckForm()){
    $("form1").submit();
  }
 
}

function CheckForm()
{ 
  if($('dwName').value=="")
   { 
      alert("名称不能为空！");
      return (false);
   }
  if($("indexStyle").value=="")
   { 
      alert("序号样式不能为空！");
      return (false);
   }
  
 return (true);
}

function getDocWord(seqId){
	var requestURLStr = contextPath + "/yh/core/funcs/workflow/act/YHDocWordAct";
	  var url = requestURLStr + "/getDocWordAct.act";
	  var rtJson = getJsonRs(url, "seqId=" + seqId );
	  if (rtJson.rtState == "0") {
	    var data=rtJson.rtData;
	    bindJson2Cntrl(data);
	    if (data.deptPrivId == "0" || data.deptPrivId == "ALL_DEPT") {
          $('deptPrivName').update("全体部门");
	    }
	  }else {
	   alert(rtJson.rtMsrg); 
	  }
	
}



</script>
</head>
<body onLoad="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 编辑文件字</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="" action="<%=contextPath%>/yh/core/funcs/workflow/act/YHDocWordAct/updateDocWordAct.act"  method="post" name="form1" id="form1" >
<table class="TableBlock" width="50%" align="center">
  <tr>
    <td nowrap class="TableContent"><span  style="color:#FF0000" >*</span>名称：</td>
      <td class="TableData">
      <INPUT type="hidden" name="seqId"  id="seqId" class=BigInput size="30" value=""/>
      <INPUT type="text" name="dwName"  id="dwName" class=BigInput size="30" value=""/>
    </td>   
  </tr>
  
   <tr>
    <td nowrap class="TableContent"><span  style="color:#FF0000" >*</span>序号样式：</td>
      <td class="TableData">
        <input type="text" name="indexStyle" id="indexStyle" class="BigInput" size="30" >
    </td>
     
  </tr>
  
  <tr>
    <td nowrap colspan="2" class="TableContent">文件字权限设置：</td>
  </tr>
  <tr>
  <td nowrap class="TableContent">部门权限：</td>
  <td class="TableData">
        <input type="hidden" name="deptPrivId" id="deptPrivId" value="" >
        <textarea name="deptPrivName" id="deptPrivName" class="BigInput" readonly></textArea>
        <a href="javascript:;" class="orgAdd" onClick="selectDept(['deptPrivId', 'deptPrivName']);">选择</a>
  </td>
  </tr>
    <tr>
  <td nowrap class="TableContent">角色权限：</td>
  <td class="TableData">
        <input type="hidden" name="rolePrivId" id="rolePrivId" value="" >
         <textarea name="rolePrivName" id="rolePrivName" class="BigInput" readonly></textArea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole(['rolePrivId', 'rolePrivName']);">选择</a>
  </td>
  </tr>
    <tr>
  <td nowrap class="TableContent">人员权限：</td>
  <td class="TableData">
        <input type="hidden" name="userPrivId" id="userPrivId" value="" >
           <textarea name="userPrivName" id="userPrivName" class="BigInput" readonly></textArea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['userPrivId', 'userPrivName']);">选择</a>
  </td>
  </tr>
  
  
  <tr align="center" class="TableControl">
    <td colspan=4 nowrap>
       <input  type="button"  onClick="doSubmit()" value="保存" class="BigButton">
       <input  type="button"  onClick="history.go(-1)" value="返回" class="BigButton">
     </td>
  </tr>
</table>
</form>

</body>
</html>