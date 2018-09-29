<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String bankId=request.getParameter("bankId");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建现金银行信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
		<link rel="stylesheet" href="<%=cssPath%>/page.css">
		<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript">
var bankId="<%=bankId%>";
function doInit(){
	var url="<%=contextPath %>/SpringR/finance/detailBank?bankId="+bankId;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){ 
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					  jQuery('#bankName').val(item.name);
					  jQuery('#openName').val(item.open_name);
					  jQuery('#bankAccount').val(item.account);
					  jQuery('#bankMoney').val(item.money);
					  jQuery('#bankRemark').val(item.remark);
				});
			}
	   }
	 });


}
function returnPage(){
	window.location.href="bank.jsp";
}	 
	
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="50%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	新建现金银行
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="50%" align="center">
      <tr>
      <td nowrap class="TableData">现金银行名称：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="bankName" id="bankName"  class="BigInput" readonly="readonly"/>
      </td>
     
    </tr>
     <tr>
      <td nowrap class="TableData">现金银行开户名称：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="openName" id="openName"  class="BigInput" readonly="readonly"/>
      </td>
     
    </tr>
    <tr>
      <td nowrap class="TableData">现金银行卡号：</td>
       <td class="TableData"  colspan="2">
 		<input type="text" name="bankAccount" id="bankAccount"  class="BigInput" readonly/>
      </td>
    </tr>
    <tr>
    	 <td nowrap class="TableData">现金银行资产：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="bankMoney" id="bankMoney"  class="BigInput" readonly/>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="2">
 		<textarea name="bankRemark" id="bankRemark" class="BigInput" cols="70" readonly></textarea>
      </td>
    </tr>
    
    
  </table>
  <hr/>

<br>
	<div id="listContainer" style="display:none;width:100;">
</div>
   <table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
    <tr align="center" class="TableControl">
      <td colspan=6 nowrap>
       <input type="button" value="返回" class="BigButton" onclick="returnPage()">
      </td>
    </tr>
  </table>
</form>
</body>
</html>