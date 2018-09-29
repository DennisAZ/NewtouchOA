<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String processId = request.getParameter("processId");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>生产加工类型明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">

<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">

function doInit(){
getType();
getPro();
}
  
 
function getType(){
	var url = "<%=contextPath %>/SpringR/produce/getProcessByIds?id=<%=processId%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");

			jQuery('#name').val(json.name);
			jQuery('#machine_type').val(json.machine_name);
			jQuery('#process_index').val(json.process_index);
			jQuery('#bad_rate').val(json.bad_rate);
			jQuery('#process_time').val(json.process_time);
			jQuery('#remark').val(json.remark);
			
  		} 
	 });
}
function getPro(){
	var url = "<%=contextPath %>/SpringR/produce/getProcessPro?processId=<%=processId%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
				if(json.length > 0){
								jQuery(json).each(function(i,item){

			var tds = "<tr  ><td align='center' nowrap>"+item.pro_code+"</td>";
									tds += "<td align='center' nowrap>"+item.pro_name+"</td>";
									tds += "<td align='center' nowrap>"+item.pt_name+"</td>";
									tds += "<td align='center' nowrap>"+item.unit_name+"</td>";
									tds += "<td align='center' nowrap><input type='text'  value='"+item.number+"'  class='BigInput' size='10' readOnly maxlength='10'></td></tr>";
									
									jQuery('#pro_table').append(tds);
									
							});
									
					}
			
  		} 
	 });


}


</script>
</head><body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <table class="TableBlock" width="80%" align="center">
  
      <td nowrap class="TableData">工序名称：</td>
       <td class="TableData">
 		<input type="text" name="name" id="name"  class="BigInput" readOnly />
      </td>
    </tr>
        <tr>
      <td nowrap class="TableData">加工类型：</td>
      <td class="TableData" >
       <input type="text" id="machine_type" name="machine_type" readOnly>
       </select>
      </td>
     
    </tr>
    <tr>
      </td>
    </tr>
    <tr>
    	 <td nowrap class="TableData">加工顺序：</td>
       <td class="TableData" >
 		<input type="text" name="process_index"   id="process_index"  

class="BigInput"  readOnly />
      </td>
    </tr>
        <tr>
      <td nowrap class="TableData">不良率：</td>
       <td class="TableData">
 		<input type="text" name="bad_rate" id="bad_rate"  class="BigInput"  readOnly/>（单位：%）
      </td>
    </tr>
        <tr>
      <td nowrap class="TableData">工时：</td>
       <td class="TableData">
 		<input type="text" name="process_time" id="process_time"  class="BigInput"  readOnly/>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" >
 		<textarea name="remark" id="remark" ></textarea>
      </td>
    </tr>
    <tr>
       <td class="TableData" colspan="2">
 		<input type="button" value="+工序损耗" disabled class="BigButton" onclick="chooseProduct(['products'],0);">
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
		    	<td align='center' nowrap>数量</td>
		    	</tr>
		    </table>
    </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan=6 nowrap>
       <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>