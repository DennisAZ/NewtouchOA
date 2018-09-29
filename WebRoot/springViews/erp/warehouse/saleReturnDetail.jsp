<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String flag=request.getParameter("flag")==null?"":request.getParameter("flag");
	String typeId=request.getParameter("typeId")==null?"":request.getParameter("typeId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查看退货单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">

<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script  type="text/javascript"  src="map.js"></script>
<script type="text/javascript">
var typeId="<%=typeId%>";
function doInit(){
	//首先查看该typeId是否有退货记录
	initTime();
	checkData();
	getPCode();
	var f = '<%=flag%>';
	if(f == '1'){
		jQuery("#type").val("销售");
	}
	else{
		jQuery("#type").val("采购");
	}
	jQuery("#type").attr("disabled",true);
}

function initTime(){
  var beginTimePara = {
      inputId:'return_date',
      property:{isHaveTime:false},
      bindToBtn:'return_dateImg'
  };
  new Calendar(beginTimePara);
}

function getAutoCode(url,id){
	jQuery.ajax({
		type:'POST',
		url:url,
		success:function(data){
			jQuery('#'+id).val(data);
		}
	});
}

function checkData(){
	var url = '<%=contextPath%>/SpringR/warehouse/checkData?typeId=<%=typeId%>&flag=<%=flag%>';
	jQuery.ajax({
		type:'POST',
		url:url,
		async:false,
		success:function(jsonData){
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			jQuery('#code').val(json.code);
	        jQuery('#type').val(json.type);
	        jQuery('#reason').val(json.reason);
	        jQuery('#return_date').val(json.return_date);
	        jQuery('#remark').val(json.remark);
	        jQuery('#retId').val(json.id);
			getReturnDBMsg();
		}
	});
}
function getReturnDBMsg(){
	var retId=jQuery("#retId").val();
	var url = "<%=contextPath %>/SpringR/warehouse/getReturn?retId="+retId;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					var tds = "<tr><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					tds += "<td align='center' nowrap>"+item.pro_type+"</td>";//产品规格
					tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//产品单位
					tds += "<td align='center' nowrap>"+item.num+"</td>";//退货数量
			 		var add="add_"+item.pro_id;	
			 		var arrWhName="<span id='"+add+"'>";
					var url1 = "<%=contextPath %>/SpringR/warehouse/getReturnDb?retId="+retId+"&proId="+item.pro_id+"&flag=<%=flag%>";
					jQuery.ajax({
	 					  type : 'POST',
	   					  url : url1,
	  					  async:false,
	  					  success : function(jsonData1){   
						  var data1 = JSON.stringify(jsonData1);
						  var json1 = eval("(" + data1 + ")");
						  if(json1.length > 0){
						  jQuery(json1).each(function(j,item1){
							arrWhName+="<a id='"+item1.dbId+"'>"+item1.whName+"("+item1.batch+")["+item1.number+"]</a>";
							});
							arrWhName+="</span>"
						  }		
	  				 	}
					 });
					tds += "<td align='center' nowrap>"+arrWhName+"</td></tr>";				
					jQuery('#pro_table').append(tds);
				});
	  		}
	  	}
	  });
}

function getPCode(){
var url = '<%=contextPath%>/SpringR/warehouse/getPCode?typeId='+typeId+'&flag=<%=flag%>';
jQuery.ajax({
		type:'POST',
		url:url,
		success:function(jsonData){
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			var flag = '<%=flag%>';
			var str = '';
			if(flag == '1'){
			 str = "<a href='<%=contextPath%>/springViews/erp/warehouse/showDetials.jsp?pod_id="+json.id+"' target='view_window'>"+json.pod_code+"</a>";
			}
			else{
			 str = "<a href='<%=contextPath%>/springViews/erp/warehouse/detailPur.jsp?pDeId="+json.id+"' target='view_window'>"+json.code+"</a>";
			}
			jQuery('#PCode').append(str);
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
	新增退货单
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="retId" name="retId">
<table class="TableBlock" width="80%" align="center">
	<tr>
	      <td nowrap class="TableData">退货单编号：</td>
	      <td class="TableData" >
	       <input id="code" name="code" type="text" value="">
	      </td>
	      
	    </tr>
	    <tr>
	    	<td nowrap class="TableData">退货来源：</td>
	      <td class="TableData">
	      	<select id="type" name="type">
	 			 <option value="销售">销售</option>
				<option value="采购">采购</option>
				<option value="其他">其他</option>
				</select>
	      </td>
	    </tr>
	    <%
	    	if(typeId != null && !"".equals(typeId)){
	    %>
	    <tr>
	     <%
	    	if("1".equals(typeId)){
	    %>
	    	<td nowrap class="TableData">对应发货单：</td>
	    <%
	    	}else{
	    %>
	    	<td nowrap class="TableData">对应收货单：</td>
	    <%
	    	}
	    %>
	      <td class="TableData">
	      	<span id="PCode"></span>
	      </td>
	    </tr>
	    <%
	    	}
	    %>
    <tr>
      <td nowrap class="TableData">退货原因：</td>
      <td class="TableData" >
       <textarea id="reason" name="reason" ></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">退货时间：</td>
      <td class="TableData" >
        <input type="text" id="return_date" name="return_date" size="19" maxlength="19" class="BigInput" value="" readOnly>
        <img src="<%=imgPath%>/calendar.gif" id="return_dateImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan="3">
		<textarea id="remark" name="remark"></textarea>
      </td>
    </tr>
      <tr>
      <td colspan="6" align="center">
		       <table id="pro_table" align="center" width="100%" >
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>退货数量</td>
	 			<td align='center' nowrap>仓库名称(批次)[退货数量]</td>
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