<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String purId = request.getParameter("purId");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增仓库收货单信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">

<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">

function doInit(){
	getCusMsg();
	
}



function getCusMsg(){
	var url = "<%=contextPath %>/SpringR/warehouse/getPur?purId=<%=purId%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
				if(json.length > 0){
				jQuery(json).each(function(i,item){
				jQuery('#purCode').val(item.code);
				jQuery('#purTitle').val(item.purTitle);
				jQuery('#supPerson').val(item.supName);
				jQuery('#supAddress').val(item.supAddress);
				jQuery('#supPhone').val(item.supPhone);
				jQuery('#supMobile').val(item.supMobile);
				jQuery('#supZipCode').val(item.supZipCode);
				jQuery('#supId').val(item.supId);
				jQuery('#ppoId').val(item.ppoId);
				jQuery('#purCode').val(item.purCode);
				jQuery('#ppoCode').val(item.pdCode);
				jQuery('#purDate').val(item.pdDate);
				jQuery('#ppoRemark').val(item.pdRemark);
				var tds = "<tr><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					tds += "<td align='center' nowrap>"+item.purchase_num+"</td>";//订单数量
					tds += "<td align='center' nowrap>"+item.already_purchase_num+"</td>";//已发数量
					tds += "<td align='center' nowrap>"+item.pur_num+"</td></tr>";//已发数量
					jQuery('#pro_table').append(tds);
	   				
	  
			 });
	 	 }
	  	}
	  });
}


function returnPage(){
	window.location.href="purShowDetial.jsp"
}

</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       新增仓库收货单信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="supId" name="supId">
 <input type="hidden" id="ppoId" name="ppoId">
<table class="TableBlock" width="80%" align="center">
    <tr>
      <td nowrap class="TableData">对应订单编号：</td>
      <td class="TableData" >
       <input id="purCode" name="purCode" type="text" value="" readOnly class="BigInput">
      </td>
      <td nowrap class="TableData">对应订单主题：</td>
      <td class="TableData" >
       <input id="purTitle" name="purTitle" type="text" value="" readOnly class="BigInput">
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">发货单编号：</td>
      <td class="TableData" >
       <input id="ppoCode" name="ppoCode" type="text" value="" readOnly class="BigInput"> 
      </td>
      <td nowrap class="TableData" style="display: none">收货单状态：</td>
      <td class="TableData" style="display: none">
			<select id="ppoStatus" name="ppoStatus">
				<option value="<%=oa.spring.util.StaticData.NEW_CREATE%>">新建</option>
				<option value="<%=oa.spring.util.StaticData.AUDITING%>">审核中</option>
				<option value="<%=oa.spring.util.StaticData.PASSING%>">审核通过</option>
				<option value="<%=oa.spring.util.StaticData.NO_PASSING%>">审核没通过</option>
				<option value="<%=oa.spring.util.StaticData.RUNNING%>">执行中</option>
				<option value="<%=oa.spring.util.StaticData.OVER%>">已完成</option>
			</select>
      </td>
    </tr>
    
     <tr>
      <td nowrap class="TableData">发货方：</td>
      <td class="TableData" >
       <input id="supPerson" name="supPerson" type="text" value="" readOnly class="BigInput">
      </td>
       <td nowrap class="TableData">发货地址：</td>
      <td class="TableData" >
       <input id="supAddress" name="supAddress" type="text" value="" readOnly class="BigInput">
      </td>
    </tr>
    
     <tr>
     <td nowrap class="TableData">联系电话：</td>
      <td class="TableData" >
       <input id="supPhone" name="supPhone" type="text" value="" readOnly class="BigInput">
      </td>
       <td nowrap class="TableData">联系手机：</td>
      <td class="TableData" >
       <input id="supMobile" name="supMobile" type="text" value="" readOnly class="BigInput">
      </td>
     
    </tr>
     
    
    <tr>
     <td nowrap class="TableData">邮编：</td>
	      <td class="TableData" >
	       <input id="supZipCode" name="supZipCode" type="text" value="" readOnly class="BigInput">
	      </td>
      <td nowrap class="TableData">收货时间：</td>
       <td class="TableData"  colspan="2">
        <input type="text" id="purDate" name="purDate" size="19" readOnly maxlength="19" class="BigInput" value="" readOnly>
        <img src="<%=imgPath%>/calendar.gif" id="purDateImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan="3">
		<textarea id="ppoRemark" name="ppoRemark" readOnly class="BigInput"></textarea>
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData" colspan="4">发货明细：</td>
    </tr>
      <tr>
      <td colspan="4" align="center">
		       <table id="pro_table" align="center" width="100%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>订单数量</td>
		    	<td align='center' nowrap>已收数量</td>
		    	<td align='center' nowrap>收货数量</td>
		    	</tr>
		    </table>
    </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="返回" class="BigButton" onclick="returnPage()">
      </td>
    </tr>
  </table>
</form>
</body>
</html>