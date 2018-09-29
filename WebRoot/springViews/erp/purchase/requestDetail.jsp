<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String reqId=request.getParameter("reqId");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建采购申请单信息</title>
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
var reqId="<%=reqId%>";

function doInit(){
	var url="<%=contextPath%>/SpringR/purchase/getRequestById?reqId="+reqId;
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
					  jQuery('#reqName').val(item.name);
					  jQuery('#reqCode').val(item.code);
					  jQuery('#reqRemark').val(item.remark);
					}
					
					var tds = "<tr><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					tds += "<td align='center' nowrap>"+item.pro_type+"</td>";//规格型号
					tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//计量单位
					
					var supStr = "";
					var url1="<%=contextPath%>/SpringR/purchase/getRequestSupById?proId="+item.pro_id+"&reqId="+reqId;
					jQuery.ajax({
	   					type : 'POST',
	   					url : url1,
	   					async:false,
	   					success : function(jd){ 
							var d1 = JSON.stringify(jd);
							var d2 = eval("(" + d1 + ")");
							if(d2.length > 0){
								jQuery(d2).each(function(j,item1){
									supStr+="<a id='"+item1.sup_id+"'>"+item1.sup_name+"["+item1.sp_price+"]</a>";
								});
							}
						}
					});
						
					tds	+= "<td align='center' nowrap><span id='"+item.pro_id+"'>"+supStr+"</span></td></tr>";//供应商选择
					jQuery('#pro_table').append(tds);
				});
			}
	   }
	 });
}
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	 采购申请单基础信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="90%" align="center">
      <tr>
      <td nowrap class="TableData">申请单名称：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="reqName" id="reqName"  class="BigInput" />
      </td>
      <td nowrap class="TableData">申请单编号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="reqCode" id="reqCode"  class="BigInput" />
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="6">
 		<textarea name="reqRemark" id="reqRemark" class="BigInput" cols="70"></textarea>
      </td>
    </tr>
      <tr>
      <td colspan="7" align="center">
		       <table id="pro_table" align="center" width="100%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>供货商[名称/价格]</td>
				</tr>
				</table>
		</td>
	  </tr>
	<tr align="center" class="TableControl">
      <td colspan="7" nowrap>
       <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      </td>
    </tr>
</table>
</form>
</body>
</html>