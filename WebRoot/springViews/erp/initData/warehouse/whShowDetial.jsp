<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>发货单明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
<script> 
var pageMgr = null;
function doInit(){
     var url = "<%=contextPath %>/SpringR/warehouse/whShowDetial";
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"hidden", name:"order_id", text:"订单id"},
	         {type:"hidden", name:"po_id", text:"货单id"},
	         {type:"data", name:"order_code",  width: '20%', text:"订单编号",render:recordCenterFunc},    
	         {type:"data", name:"po_code", width: '20%', text:"订单出货单编号",render:recordCenterFunc}, 
	         {type:"data", name:"pod_code", width: '20%', text:"仓库发货单编号",render:recordCenterFunc}, 
	         {type:"data", name:"pod_status",  width: '20%', text:"仓库发货单状态",render:selectStatusFunc},
	         {type:"selfdef", text:"操作", width: '20%',render:opts}]
	    };
	    pageMgr = new YHJsPage(cfgs);
	    pageMgr.show();
	    var total = pageMgr.pageInfo.totalRecord;
	    if(total){
	      showCntrl('listContainer');
	      var mrs = " 共 " + total + " 条记录 ！";
	      //showCntrl('delOpt');
	    }else{
	      WarningMsrg('无信息展示', 'msrg');
	    }
}

function selectStatusFunc(cellData){
	var rtStr="";
	var url = "<%=contextPath %>/SpringR/warehouse/getStatusName?status="+cellData;
	jQuery.ajax({
	   type : 'POST',
	   async:false,
	   url : url,
	   success : function(jsonData){  
				rtStr = "<center>"+jsonData+"</center>";
	   }
	 });
	return rtStr;
}
function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}

function opts(cellData, recordIndex, columIndex){
	var order_id = this.getCellData(recordIndex, "order_id");//销售订单id
	var po_id = this.getCellData(recordIndex, "po_id");//销售货单id
	var pod_id = this.getCellData(recordIndex, "id");
	var pod_status = this.getCellData(recordIndex, "pod_status");
	var pod_code = this.getCellData(recordIndex, "pod_code");
	return selectOPTByStatus(pod_status,order_id,po_id,pod_id,pod_code);
}

function selectOPTByStatus(status,order_id,po_id,pod_id,pod_code){
	if(status==0){
		return "<center><a href=\"javascript:void(0)\" onclick=\"openAccount('" + pod_id + "');\" >开账</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"showDetials('" + pod_id + "');\" >发货单详情</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + po_id + "','"+order_id+"','" + pod_id + "');\" >编辑</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" + pod_id+"');\" >删除</a></center>";
	}else{
		return "<center><a href=\"javascript:void(0)\" onclick=\"showDetials('" + pod_id + "');\" >发货单详情</a></center>";
	}
}

function showDetials(pod_id){
	window.location.href='showDetials.jsp?pod_id='+pod_id;
}
function edit(po_id,order_id,pod_id){
	window.location.href='updateWhShowDetial.jsp?po_id='+po_id+'&order_id='+order_id+'&pod_id='+pod_id;
}
function deletes(pod_id){
	var url = "<%=contextPath %>/SpringR/warehouse/deletePOD?pod_id="+pod_id;
	jQuery.ajax({
	   type : 'POST',
	   async:false,
	   url : url,
	   success : function(jsonData){  
			if(jsonData == '0'){
				alert("删除成功！");
				window.location.reload();
			}
			else{
				alert("删除失败！");
			}
	   }
	 });
}



function openAccount(pod_id){
 var url = "<%=contextPath %>/SpringR/warehouse/openAccount?pod_id="+pod_id;
	jQuery.ajax({
	   type : 'POST',
	   async:false,
	   url : url,
	   success : function(jsonData){  
			if(jsonData == '0'){
				alert("操作成功！");
				window.location.reload();
			}
			else{
				alert("操作失败！");
			}
	   }
	 });
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;发货单明细</span>
   </td>
    <td class="Big">
   <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
      <td colspan="19">
         <input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;
         <a href="javascript:deleteAll();" title="删除所选信息"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选信息</a>&nbsp;
      </td>
 </tr>
</table>
</div>

<div id="msrg">
</div>
</body>
</html>