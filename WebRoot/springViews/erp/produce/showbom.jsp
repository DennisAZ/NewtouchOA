<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String planId = request.getParameter("planId");
	String type = request.getParameter("type");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>生产领料</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">

<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">jQuery.noConflict();</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script type="text/javascript">
var planId="<%=planId%>";
var type="<%=type%>";
function doInit(){
	getPlanCodeAndInsertBom();
}

function getPlanCodeAndInsertBom(){
	var url = "<%=contextPath %>/SpringR/produce/getPlanCodeAndInsertBom?planId="+planId+"&type="+type;
	var proids="";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					if(i==0){
						var str = "<a href='#' style='color:green;' onclick='planDetail(\""+planId+"\")'>"+item.plan_code+"</a>";
 						$('code').innerHTML=str;
 						jQuery('#bom_id').val(item.bom_id);
 						jQuery('#bomCode').val(item.code);
 						jQuery('#remark').val(item.remark);
					}
				
					 var tds = "<tr id='"+i+"'><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					  tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					  tds += "<td align='center' nowrap>"+item.pro_type+"</td>";//产品型号
					  tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//计量单位
					  tds += "<td align='center' nowrap>"+item.number+"</td>";//需求数量
					  jQuery('#pro_table').append(tds);
					  if(proids!=""){
					  	proids+=",";
					  }
					  proids+=item.pro_id;
					  
			    });
			}
	  		jQuery('#products').val(proids);
	   }
	 });
}

function planDetail(id){
	window.location.href='planDetail.jsp?planId='+id+'&flag=1';
}
   
  

</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       生产领料
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="bom_id" name="bom_id" value="-1">
 <input type="hidden" id="products" name="products" value="-1">
<table class="TableBlock" width="80%" align="center">
       <tr>
      <td nowrap class="TableData">生产计划编号：</td>
       <td class="TableData">
      	 <span name="code" id="code"></span>
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">生产领料编号：</td>
       <td class="TableData">
 		<input type="text" name="bomCode" id="bomCode"  class="BigInput" />
      </td>
     
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" >
 		<textarea name="remark" id="remark" class="BigInput" cols="40"></textarea>
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData" colspan="2">BOM清单：</td>
    </tr>
      <tr>
      <td colspan="2" align="center">
		       <table id="pro_table" align="center" width="100%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>需求数量</td>
		    	</tr>
		    </table>
    </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="关闭" class="BigButton" onclick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>