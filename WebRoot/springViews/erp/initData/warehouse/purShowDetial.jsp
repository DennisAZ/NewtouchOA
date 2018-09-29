<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String purId=request.getParameter("purId");
	if(purId==null||purId==""){
		purId="0";
	}
 %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title> 收货单管理</title>
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
var purId="<%=purId%>";
function doInit(){
 var url="";
      url = "<%=contextPath %>/SpringR/warehouse/queryPur?purId="+purId;
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"ppoId", text:"id"},
	         {type:"hidden", name:"purId", text:"id"},
	         {type:"hidden", name:"pdId", text:"id"},
	         {type:"data", name:"purCode",  width: '15%', text:"订单编号",render:recordCenterFunc},    
	         {type:"data", name:"ppoCode",  width: '15%', text:"订单出货单编号",render:recordCenterFunc},    
	         {type:"data", name:"pdCode",  width: '15%', text:"仓库收货单编号",render:recordCenterFunc},    
	         {type:"data", name:"pdStatus", width: '20%', text:"仓库收货单状态",render:selectStatusFunc}, 
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
	var pDeId = this.getCellData(recordIndex, "pdId");//出货单id
	var status = this.getCellData(recordIndex, "pdStatus");
	var purId = this.getCellData(recordIndex, "purId");//销售订单id
	var pdCode = this.getCellData(recordIndex, "pdCode");//销售订单id
		if(status==0){
		return "<center><a href=\"javascript:void(0)\" onclick=\"openAccount('"+pDeId + "');\" >开账</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"showDetial('" + purId + "');\" >收货单详情</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + purId + "','"+pDeId+"');\" >编辑</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" + purId+"');\" >删除</a></center>";
	}else{
		return "<center><a href=\"javascript:void(0)\" onclick=\"showDetials('" + purId + "');\" >收货单详情</a></center>";
	}
}


function openAccount(pDeId){
 var url = "<%=contextPath %>/SpringR/warehouse/openAccountPurchaseDetial?pDeId="+pDeId;
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

function checkHasValue(purId){
	var flag = '0';
	var url = "<%=contextPath %>/SpringR/warehouse/checkHasValue?purId="+purId;
	jQuery.ajax({
	   type : 'POST',
	   async:false,
	   url : url,
	   success : function(jsonData){  
			if(jsonData == '0'){
				//表示某个订单，仓库有发货记录
				flag = '1';
			}
	   }
	 });
	return flag;
}
function deletes(purId){
	var url = "<%=contextPath %>/SpringR/warehouse/deletesPur?purId="+purId;
	jQuery.ajax({
	   type : 'POST',
	   async:false,
	   url : url,
	   success : function(jsonData){  
			if(jsonData == '0'){
			alert("删除成功！");
			window.location.reload();
			}else{
			alert("删除失败！");
			}
	   }
	 });
	return flag;
}

function showDetial(purId){
	window.location.href='detailPur.jsp?purId='+purId;
}
function edit(purId,pDeId){
	window.location.href='editPur.jsp?purId='+purId+"&pDeId="+pDeId;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;采购收货管理</span>
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