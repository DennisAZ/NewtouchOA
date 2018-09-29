<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据库切换</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>

<script type="text/javascript">
 function doInit(){
	  var url = contextPath + "/yh/core/funcs/connswitch/act/YHConnSwitchAct/getConnectingDbms.act";
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    var data=rtJson.rtData;
	    var dbms=data.dbms;
	    bindJson2Cntrl(data);
	  }else{
	    alert( rtJson.rtMsrg);
	  }
	  
	 }

function warm(){
	 var dbms="";
	  var chkObjs = document.getElementsByName("dbms");
      for(var i=0;i<chkObjs.length;i++){
          if(chkObjs[i].checked){
              dbms=chkObjs[i].value;
              break;
          }
      }

	
   if(window.confirm("请确认是否要切换到"+dbms+"数据库，请谨慎！")){
	    var url = contextPath + "/yh/core/funcs/connswitch/act/YHConnSwitchAct/getSwitchDbms.act";
	    var rtJson = getJsonRs(url,"dbms="+dbms);
	    if(rtJson.rtState == "0"){
		    if(window.confirm("成功切换到"+dbms+",是否进入登陆界面？")){
		    	 window.open('/yh/login.jsp') ;
			  }
	    }else{
	      alert( rtJson.rtMsrg);
	    }
	   }
	
}
 
</script>
</head>
<body onLoad="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 数据库切换</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<br>
<table class="TableBlock" width="60%" align="center">
	<tr>
		<td nowrap colspan="3" class="TableContent"> 请选择要切换到的数据库类型！</td>
	</tr>
  <tr>
    <td nowrap class="TableData"><input type="radio" class="BigInput" name="dbms" id="oracle" value="oracle"> Oracle数据库</td>
      <td class="TableData">
        <input type="radio" name="dbms" id="sqlserver" value="sqlserver" class="BigInput"  > Sqlserver数据库
      </td>
    <td nowrap class="TableData"> <input type="radio" name="dbms" id="mysql" value="mysql" class="BigInput"  > Mysql数据库</td>
     
  </tr>
  <tr>
  <td colspan="3" align="center" class="TableContent">
   <input type="button" onclick="warm()" value="切     换" class="BigButton"  >
  </td>
  </tr>
  
</table>
<br>
<br>
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 数据库连接配置</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<div align="center">
<iframe align="middle" frameborder="0" id="info" src="connInfo.jsp" width="60%" marginheight="0" marginwidth="0" height="240"  style="border:none"></iframe>
</div>
</body>
</html>