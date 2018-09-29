<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String planId = request.getParameter("planId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>入库请检</title>
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
<script  type="text/javascript"  src="map.js"></script>
<script type="text/javascript">
var planId="<%=planId%>";
function doInit(){
	getWHExamDetial();
}
var map = new Map();
function getWHExamDetial(){
	var url = "<%=contextPath %>/SpringR/produce/getWHExamDetial?planId="+planId;
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
 						jQuery('#exam_id').val(item.exam_id);
 						jQuery('#exam_code').val(item.exam_code);
 						jQuery('#remark').val(item.remark);
					}
					 var tds = "<tr id='"+i+"'><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					  tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					  tds += "<td align='center' nowrap>"+item.pro_type+"</td>";//产品型号
					  tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//计量单位
					  tds += "<td align='center' nowrap>"+getSelectStr(item.exam_id,item.pro_id,item.exam_way)+"</td>";//检查方式
					  tds += "<td align='center' nowrap>"+item.number+"</td>";//本次生产数量
					  tds += "<td align='center' nowrap>"+item.qualified_number+"</td>";
					  tds += "<td align='center' nowrap><span id='add_"+item.pro_id+"'></span></td></tr>";
					  jQuery('#pro_table').append(tds);
					  if(proids!=""){
					  	proids+=",";
					  }
					  proids+=item.pro_id;
					  map.put(item.pro_id,0);
			    });
			}
	  		jQuery('#products').val(proids);
	   }
	 });
	 
	 //填充产品对应仓库的信息
	 var exam_id = jQuery('#exam_id').val();
	 url = "<%=contextPath %>/SpringR/produce/getExamProDetial?exam_id="+exam_id;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					var para = item.db_id+","+item.wh_id+","+item.pro_id+","+exam_id+","+item.number;
					var arr=new Array();
					arr.push(para);
					dataArr.push(arr);
					var tds="<a id='"+item.db_id+"'>"+item.wh_name+"["+item.number+"]</a>";
					jQuery('#add_'+item.pro_id).append(tds);
			    });
			}
	   }
	 });
	 
}
function getSelectStr(exam_id,pro_id,exam_way){
	 var selStr = "<select id='"+pro_id+"' disabled=true><option value='抽检'>抽检</option><option value='全检'>全检</option></select>";
	 var selStr1 = "<select id='"+pro_id+"' disabled=true><option value='抽检'>抽检</option><option value='全检' selected>全检</option></select>";
	 if(exam_way == '全检'){
	 	return selStr1;
	 }
	 else{
	 	return selStr;
	 }
}

var dataArr=new Array();
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	      入库请检
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="exam_id" name="exam_id" value="-1">
 <input type="hidden" id="products" name="products" value="-1">
<table class="TableBlock" width="80%" align="center">
      <tr>
      <td nowrap class="TableData">入库请检编号：</td>
       <td class="TableData">
 		<input type="text" name="exam_code" id="exam_code"  class="BigInput" />
      </td>
     
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" >
 		<textarea name="remark" id="remark" class="BigInput" cols="40"></textarea>
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData" colspan="2">入库请检清单：</td>
    </tr>
      <tr>
      <td colspan="2" align="center">
		       <table id="pro_table" align="center" width="100%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>检查方式</td>
		    	<td align='center' nowrap>本次生产数量</td>
		    	<td align='center' nowrap>合格入库数量</td>
		    	<td align='center' nowrap>入库[仓库名称/出库数量]</td>
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