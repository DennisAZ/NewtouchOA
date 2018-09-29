<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String fi_id = request.getParameter("fi_id");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>更新财务应收单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">

<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function doInit(){
	getFinanceInMsg();
}

 function getFinanceInMsg(){
	var url = "<%=contextPath %>/SpringR/finance/getFinanceInsMsg?fi_id=<%=fi_id%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			jQuery('#code').val(json.code);
			jQuery('#type').val(json.type);
			jQuery('#customer').val(json.customer);
			jQuery('#total').val(json.total);
			jQuery('#already_total').val(json.already_total);
			jQuery('#remark').val(json.remark);
	   }
	 });
}
 
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       更新财务应收单
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="80%" align="center">
	<tr>
	      <td nowrap class="TableData">应收单编号：</td>
	      <td class="TableData" >
	       <input id="code" name="code" type="text" value="" readonly>
	      </td>
	      <td nowrap class="TableData">应收来源：</td>
	      <td class="TableData">
	      	<select id="type" name="type" disabled="disabled">
					<option value="销售">销售</option>
					<option value="其他">其他</option>
				</select>
	      </td>
	    </tr>
	     <tr>
      <td nowrap class="TableData">应收客户：</td>
      <td class="TableData" colspan='3'>
       <input id="customer" name="customer" type="text" value="" readonly>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">应收金额：</td>
      <td class="TableData" >
       <input id="total" name="total" type="text" value="" onblur="checkFloat('total');" readonly>
      </td>
      <td nowrap class="TableData">已收金额：</td>
      <td class="TableData" >
       <input id="already_total" name="already_total" type="text" value="" onblur="checkFloat('already_total');" readonly>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan="3">
		<textarea id="remark" name="remark" readonly></textarea>
      </td>
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input id="backButton" type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>