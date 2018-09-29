<%@ page language="java" import="yh.core.funcs.person.data.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<%
  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
  int userSeqId = user.getSeqId();
  String userPriv = user.getUserPriv();
  String type = request.getParameter("type");
  String isExNotUser =  request.getParameter("isExNotUser") == null ? "" :  request.getParameter("isExNotUser");//导入不成功的人员
  if(type==null){
    type="";
  }
  String error = "导入成功！";
  if(type!=null&&type.equals("2")){
    error = "导入出错,可能文件数据不正确！";
    //error = "导入成功！";
  }else{
    if(!isExNotUser.equals("")){
      error = "导入成功，但" + isExNotUser + "等人未成功导入,请确认人员是否存在！";
    }
  }
  
  String dateStr = YHUtility.getCurDateTimeStr();
  int year = Integer.parseInt(dateStr.substring(0,4));
  int month = Integer.parseInt(dateStr.substring(5,7));
  int day = Integer.parseInt(dateStr.substring(8,10));
  int minYear = 2008;
  int maxYear = 2050;
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>导入和导出</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">

var file_name;
function CheckForm(){
  if(document.form2.xmlFile.value==""){ 
     alert("请选择要导入的文件！");
     return (false);
  }
  if(document.form2.xmlFile.value!="") {
    var file_temp=document.form2.xmlFile.value;
    $("xmlFileName").value=file_temp;
    var Pos;
    Pos=file_temp.lastIndexOf("\\");
    file_name=file_temp.substring(Pos+1,file_temp.length);
    document.form2.fileName.value=file_name;
 
  }
   return (true);
}
function my_import(){
  //alert($("xmlFile").value);
  if(CheckForm()){
    document.form2.submit();
  }
}
function improt(){
  var fileName = $("fileName").value;
  var file=fileName.lastIndexOf("\.");
  fileName=fileName.substring(file+1,fileName.length);
  
  if((fileName!='csv')&&(fileName!='xls')){
    alert("只能导入CSV或者XLS文件!");
    return;
  }
  if(fileName=='xls'){
    var impType = 2;
    $("impType").value = impType;
  }else{
    var impType = 1;
    $("impType").value = impType;
  }
  
  document.form2.submit();   
}

function returnFun(){
  window.location.href = "<%=contextPath%>/subsys/jtgwjh/setting/import.jsp";
}


function doOnload(){
  var type='<%=type%>';
  if(type=='1'||type=='2'){
    $("bodyDiv").style.display = "none";
    $("returnDiv").style.display = "";
  }
}
</script>
</head>

<body class="" topmargin="5" onload="doOnload()">
<div id="bodyDiv">



<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/inout.gif" align="absmiddle"><span class="big3"> &nbsp;单位数据导入</span>
    </td>
  </tr>
</table>
<br>

<form enctype="multipart/form-data"  action="<%=contextPath%>/yh/subsys/jtgwjh/util/YHNetFileAct/importData.act"  method="post" id="form2" name="form2" onsubmit="return CheckForm();">
<table class="TableBlock" width="550" align="center">
 <tr>

  <tr height="25">
    <td nowrap class="TableContent">选择要导入单位的文件：</td>
    <td class="TableData" colspan="3">
       <input type="file" id="xmlFile" value="" name="xmlFile" class="BigInput" size="35">
       <input type="hidden" id="fileName" name="fileName">
         <input type="hidden" id="xmlFileName" name="xmlFileName">
         <input type="hidden" id="impType" name="impType" value=""></input>
         
        </td>
  </tr>
   
  <tr align="center" class="TableControl">
   <td colspan="4" nowrap>
     <input type="button" value="导入" class="BigButton" onClick="my_import();">
   </td>
 </tr>
</table>
</form>
</div>
<div id="returnDiv" style="display:none">
<table class="MessageBox" align="center" width="260">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt"><%=error %></div>
    </td>
  </tr>
</table>
<center>
	<input type="button" value="返回" class="BigButton" onClick="returnFun();">
		
</center>

</div>
</body>
</html>