<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String pDeId = request.getParameter("pDeId");
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
	getPud();
	getPurPro();
}



function getPud(){
		var url = "<%=contextPath %>/SpringR/warehouse/getPod?pdId=<%=pDeId%>";
		jQuery.ajax({
			type:'POST',
			url:url,
			async:false,
			success:function(jsonData){
				var data=JSON.stringify(jsonData);
				var json=eval("("+data+")");
				jQuery('#purCode').val(json.ppo_code);
				jQuery('#purTitle').val(json.ppo_title);
				jQuery('#supPerson').val(json.sup_name);
				jQuery('#supAddress').val(json.address);
				jQuery('#supPhone').val(json.phone);
				jQuery('#supMobile').val(json.mobile);
				jQuery('#supZipCode').val(json.zip_code);
				jQuery('#supId').val(json.supId);
				jQuery('#ppoId').val(json.ppo_id);
				jQuery('#ppoCode').val(json.ppidCode);
				jQuery('#purDate').val(json.ppidDate);
				jQuery('#ppoRemark').val(json.ppidRemark);
			}
		})
}

var arrWhName="";
function getPurPro(){
var productOut="";
var orderNum=0;
var k=0;
		var url = "<%=contextPath %>/SpringR/warehouse/getPodPro?pdId=<%=pDeId%>";
		jQuery.ajax({
			type:'POST',
			url:url,
			async:false,
			success:function(jsonData){
				var data=JSON.stringify(jsonData);
				var json=eval("("+data+")");
				if(json.length>0){
					jQuery(json).each(function(i,item){
					var batch="";
						var url1 = "<%=contextPath %>/SpringR/warehouse/getPudNumbers?pdId=<%=pDeId%>&proId="+item.pro_id;
					jQuery.ajax({
	 					  type : 'POST',
	   					  url : url1,
	  					  async:false,
	  					  success : function(jsonData1){   
						  var data1 = JSON.stringify(jsonData1);
						  var json1 = eval("(" + data1 + ")");
						  if(json1.length > 0){
						  jQuery(json1).each(function(j,item1){
							var parm=item.pro_id+j;		
							arrWhName+="<a id='"+j+"' style='cursor:pointer' title='[批次号："+item1.batch+"]--[有效时间："+item1.invalid_time+"]' >"+item1.whName+"["+item1.number+"]</a><span id='"+parm+"'></span>";
							k++;
							orderNum+=parseInt(item1.number);
							
								});
							}		
	  				 	}
					 });
									var tds = "<tr ><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
									tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
									tds += "<td align='center' nowrap>"+item.purchase_num+"</td>";//订单数量
									tds += "<td align='center' nowrap>"+item.already_purchase_num+"</td>";//已发数量
									tds += "<td align='center' nowrap><input type='text' value='"+orderNum+"'  class='BigInput' size='10' readOnly maxlength='10'></td>";//已发数量
									tds += "<td align='center' nowrap id='"+item.pro_id+"'>"+arrWhName+"</td></tr>";//仓库
									jQuery('#pro_table').append(tds);
									arrWhName="";
							 		orderNum=0;
					})
				}
			}
		})
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
		    	<td align='center' nowrap>库存[仓库名称/入库数量]</td>
		    	</tr>
		    </table>
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