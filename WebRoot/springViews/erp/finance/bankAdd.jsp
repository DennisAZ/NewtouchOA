<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
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

   function doSubmitForm(){
   var bankName=jQuery('#bankName').val();
   var bankAccount=jQuery('#bankAccount').val();
   var bankMoney=jQuery('#bankMoney').val();
   var bankRemark=jQuery('#bankRemark').val();
   var openName=jQuery('#openName').val();
   var param="bankName="+encodeURIComponent(bankName)+
              "&bankAccount="+encodeURIComponent(bankAccount)+
              "&openName="+encodeURIComponent(openName)+
              "&bankMoney="+encodeURIComponent(bankMoney)+
              "&bankRemark="+encodeURIComponent(bankRemark);
      var  url = "<%=contextPath %>/SpringR/finance/addBank?"+param;
   	jQuery.ajax({
   		type:'POST',
   		url:url,
   		success:function(data){
   			if(data==0){
   				alert("添加成功!");
   				window.location.href="bank.jsp";
   			}else{
   				alert("添加失败！");
   			}
   		}
   	})
   }
</script>
</head>
<body topmargin="5" >
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
 		<input type="text" name="bankName" id="bankName"  class="BigInput" />
      </td>
     
    </tr>
     <tr>
      <td nowrap class="TableData">现金银行开户名称：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="openName" id="openName"  class="BigInput" />
      </td>
     
    </tr>
    <tr>
      <td nowrap class="TableData">现金银行卡号：</td>
       <td class="TableData"  colspan="2">
 		<input type="text" name="bankAccount" id="bankAccount"  class="BigInput"/>
      </td>
    </tr>
    <tr>
    	 <td nowrap class="TableData">现金银行资产：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="bankMoney" id="bankMoney"  class="BigInput" />
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="2">
 		<textarea name="bankRemark" id="bankRemark" class="BigInput" cols="70"></textarea>
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
       <input type="button" value="添加" class="BigButton" onclick="doSubmitForm()">
      </td>
    </tr>
  </table>
</form>
</body>
</html>