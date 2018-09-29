<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>销售订单管理</title>
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
     var url = "<%=contextPath %>/SpringR/saleOrder/orderList/"+new Date().getTime();
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"hidden", name:"proId", text:"id"},
	         {type:"data", name:"orderCode",  width: '10%', text:"销售编号",render:recordCenterFunc},    
	         {type:"data", name:"orderTitle",  width: '20%', text:"销售单主题",render:recordCenterFunc},
	         {type:"data", name:"salesperson",  width: '10%', text:"销售员",render:recordCenterFunc},    
	         {type:"data", name:"signDate",  width: '10%', text:"日期",render:recordCenterFunc},
	         {type:"data", name:"orderStatus",  width: '10%', text:"销售状态",render:selectStatusFunc},    
	          {type:"data", name:"remark",  width: '10%', text:"备注",render:recordCenterFunc},
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
	      WarningMsrg('无符合条件的销售信息', 'msrg');
	    }
}

function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}

function opts(cellData, recordIndex, columIndex){
	var order_id = this.getCellData(recordIndex, "id");
	var proId = this.getCellData(recordIndex, "proId");
	var order_title = this.getCellData(recordIndex, "orderTitle");
	var order_code = this.getCellData(recordIndex, "orderCode");
	var status = this.getCellData(recordIndex, "orderStatus");
	return selectOPTByStatus(status,order_id,order_title,order_code,proId);
}

function selectOPTByStatus(status,order_id,order_title,order_code,proId){
	if(status==0){
		return "<center><a href=\"javascript:void(0)\" onclick=\"openAccount('" + order_id + "');\" >开账</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"detail('" + order_id + "');\" >查看</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + order_id + "');\" >编辑</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" + order_id+"','"+proId + "');\" >删除</a></center>";
	}else{
		return "<center><a href=\"javascript:void(0)\" onclick=\"detail('" + order_id + "');\" >查看</a></center>";
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
function edit(id){
	window.location.href='orderAdd.jsp?orderId='+id+"&flag=0";
}
function detail(id){
	window.location.href='detail.jsp?orderId='+id;
}
function deletes(id,proId){
	var url = "<%=contextPath %>/SpringR/saleOrder/deleteOrder?orderId="+id+"&proId="+proId;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("删除成功");
		    window.location.reload();
		  }
		  else{
			   alert("删除失败"); 
		  }
	   }
	 });
}

function openAccount(order_id){
 var url = "<%=contextPath %>/SpringR/saleOrder/openAccount?orderId="+order_id;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("操作成功");
		    window.location.reload();
		  }
		  else{
			   alert("操作失败"); 
		  }
	   }
	 });
}
function newCreate(){
	window.location.href='orderAdd.jsp?flag=1';
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;销售信息管理</span>
   </td>
    <td class="Big">
   <input type="button" value="新建" class="BigButton" onclick="newCreate();">
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>


<div id="msrg">
</div>
</body>
</html>