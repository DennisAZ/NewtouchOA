<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String id = request.getParameter("id");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查看报损单信息</title>
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



var arrWhName="";
var orderNum=0;
function getCusMsg(){
var  k=0;
	var url = "<%=contextPath %>/SpringR/warehouse/getLoss?lossId=<%=id%>";

	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
				if(json.length > 0){
				jQuery(json).each(function(i,item){
					var arrPro=new Array();
				jQuery('#code').val(item.code);
				jQuery('#reason').val(item.reason);
				jQuery('#remark').val(item.remark);
				jQuery('#beginTime').val(item.loss_date);
				var url1 = "<%=contextPath %>/SpringR/warehouse/getLossDb?lossId=<%=id%>&proId="+item.proId;
				jQuery.ajax({
					   type : 'POST',
					   url : url1,
					   async:false,
					   success : function(jsonData1){   
							var data1 = JSON.stringify(jsonData1);
							var json1 = eval("(" + data1 + ")");
								if(json1.length > 0){
								jQuery(json1).each(function(j,item1){
							
							arrWhName+="<a id='"+j+"'>"+item1.whName+"["+item1.number+"]</a>";
							k++;
							orderNum+=parseInt(item1.number);
								
	   					  });
				 	    }
				  	  }
				  });
									var tds = "<tr ><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
									tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
									tds += "<td align='center' nowrap>"+item.ptName+"</td>";//订单数量
									tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//已发数量
									tds += "<td align='center' nowrap><input type='text' value='"+orderNum+"'  class='BigInput' size='10' readOnly maxlength='10'></td>";//已发数量
									tds += "<td align='center' nowrap id='"+item.pro_id+"'>"+arrWhName+"</td></tr>";//仓库
									
									jQuery('#pro_table').append(tds);
									arrWhName="";
									orderNum=0;
			 });
	 	 }
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
	查看报损单明细
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="customerId" name="customerId">
<table class="TableBlock" width="80%" align="center">
	<tr>
	      <td nowrap class="TableData">报损单编号：</td>
	      <td class="TableData" >
	       <input id="code" name="code" type="text" value="" readOnly>
	      </td>
	      
	    </tr>
	   
    <tr>
      <td nowrap class="TableData">报损原因：</td>
      <td class="TableData" >
       <textarea id="reason" name="reason" readOnly></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">报损时间：</td>
      <td class="TableData" >
       <input type="text" id="beginTime" name="beginTime" readOnly></textarea>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan="3">
		<textarea id="remark" name="remark" readOnly></textarea>
      </td>
    </tr>
      <tr>
      <td colspan="6" align="center">
		       <table id="pro_table" align="center" width="80%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>已经报损数量</td>
		    	<td align='center' nowrap>库存（仓库名称/报损数量）</td>
		    	</tr>
		    </table>
    </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>