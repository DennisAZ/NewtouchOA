<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String notifyId = request.getParameter("notifyId");
	String flag = request.getParameter("flag");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查看生产通知单</title>
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
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script type="text/javascript">
var notifyId="<%=notifyId%>";
function doInit(){
	getWareHouse();
	getNotifyPro();
}
function getWareHouse(){
	var url = "<%=contextPath %>/SpringR/produce/getNotifyById?notifyId="+notifyId;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			jQuery('#code').val(json.CODE);
			jQuery('#type').val(json.TYPE);
			jQuery('#beginTime').val(json.produce_time);
			jQuery('#createTime').val(json.create_time);
			jQuery('#remark').val(json.remark);
	   }
	 });
	
}
function getNotifyPro(){
		var url = "<%=contextPath %>/SpringR/produce/getNotifyProById?notifyId="+notifyId;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					   var tds = "<tr id='"+i+"'><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					  tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					  tds += "<td align='center' nowrap>"+item.ptName+"</td>";//产品型号
					  tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//计量单位
					  tds += "<td align='center' nowrap><input type='text' id='"+item.pro_id+"Number' value='"+item.number+"' class='BigInput' size='10' maxlength='10' ></td>";//数量
					  tds += "<td align='center' nowrap><input type='text' id='"+item.pro_id+"beginTime' name='"+item.pro_id+"beginTime' value='"+item.end_time+"' size='19' maxlength='19' class='BigInput' value=''><img src='<%=imgPath%>/calendar.gif' id='"+item.pro_id+"beginTimeImg' align='absMiddle' border='0' style='cursor:pointer' > </td>";
					  tds += "<td align='center' nowrap><a href='#' style='color:red' onclick=\"showCrafts('"+item.craftsId+"','"+item.pro_name+"')\">"+item.crafts_version+"</a></td>";//工艺版本
					  tds += "<td align='center' nowrap><a href='#' style='color:red' onclick=\"showDrawings('"+item.drawingId+"')\">"+item.drawing_version+"</a></td></tr>";//图纸版本
					  jQuery('#pro_table').append(tds);
				});
			}
	   }
	 });
}

 function showCrafts(craftsId,pro_name){
	    var url="processManage3.jsp?craftsId="+craftsId+'&pro_name='+encodeURIComponent(pro_name);
	 	openDialogResize(url,800, 500);
 	}
 	 function showDrawings(drawingId){
	    var url="drawingShow.jsp?drawingId="+drawingId;
	 	openDialogResize(url,800, 500);
 	}
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       查看生产通知单
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="products" name="products">
<table class="TableBlock" width="80%" align="center">
       <tr>
      <td nowrap class="TableData">通知单编号：</td>
       <td class="TableData">
 		<input type="text" name="code" id="code" readOnly  class="BigInput" />
      </td>
     
    </tr>
      <tr>
      <td nowrap class="TableData">通知单来源：</td>
       <td class="TableData">
 		<input type="text" name="type" id="type"  readOnly class="BigInput" />
      </td>
     
    </tr>
      <tr>
      <td nowrap class="TableData">生产时间：</td>
        <td class="TableData" >
   		<input type="text" id="beginTime"  readOnly name="beginTime" size="19" maxlength="19" 

class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif"  disabled id="beginTimeImg" align="absMiddle" border="0" 

style="cursor:pointer" >
      </td>
     
    </tr>
     <tr style="display:none;">
      <td nowrap class="TableData">创建时间：</td>
       <td class="TableData" >
   		<input type="text" id="createTime" readOnly name="createTime" size="19" 

maxlength="19" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif" id="createTimeImg" disabled align="absMiddle" border="0" 

style="cursor:pointer" >
      </td>
     
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" >
 		<textarea name="remark" id="remark" class="BigInput" readOnly cols="40"></textarea>
      </td>
    </tr>
    <tr>
       <td class="TableData" colspan="2">
 		<input type="button" disabled  value="+添加产品" class="BigButton" onclick="chooseProduct(['products'],0);">
      </td>
    </tr>
      <tr>
      <td colspan="2" align="center">
		       <table id="pro_table" align="center" width="100%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>计划生产数量</td>
		    	<td align='center' nowrap>交货时间</td>
		    	<td align='center' nowrap>工艺版本号</td>
		    	<td align='center' nowrap>图纸版本号</td>
		    	</tr>
		    </table>
    </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
      <%
      	if("1".equals(flag)){
      %>
        <input type="button" value="关闭" class="BigButton" onclick="javascript:window.close();">
        <%
        }else{
        %>
        <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1)">
        <%
        	}
        %>
      </td>
    </tr>
  </table>
</form>
</body>
</html>