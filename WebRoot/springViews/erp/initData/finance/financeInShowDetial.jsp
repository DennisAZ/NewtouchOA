<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>应收单</title>
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
     var url = "<%=contextPath %>/SpringR/finance/fiShowDetial";
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"fi_id", text:"fi_id"},
	         {type:"data", name:"type",  width: '15%', text:"应收来源",render:recordCenterFunc},    
	         {type:"data", name:"customer",  width: '15%', text:"客户信息",render:recordCenterFunc},    
	         {type:"data", name:"code", width: '20%', text:"应收单编号",render:recordCenterFunc}, 
	         {type:"data", name:"status",  width: '20%', text:"应收单状态",render:selectStatusFunc},
	         {type:"hidden", name:"typeId",  width: '20%', text:"订单id",render:selectStatusFunc},
	         {type:"selfdef", text:"操作", width: '20%',render:opts}]
	    };
	    pageMgr = new YHJsPage(cfgs);
	    pageMgr.show();
	    var total = pageMgr.pageInfo.totalRecord;
	    if(total){
	      showCntrl('listContainer');
	      var mrs = " 共 " + total + " 条记录 ！";
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
	var fi_id = this.getCellData(recordIndex, "fi_id");
	var orderId = this.getCellData(recordIndex, "typeId");
	var code = this.getCellData(recordIndex, "code");
	var status = this.getCellData(recordIndex, "status");
	return selectOPTByStatus(orderId,fi_id,code,status);
}

function selectOPTByStatus(orderId,fi_id,code,status){
	if(status==0){
		if(orderId!='0'||orderId!=0){
		return "<center><a href=\"javascript:void(0)\" onclick=\"openAccount('" + fi_id + "');\" >开账</a>" +
			"&nbsp<a href=\"javascript:void(0)\" onclick=\"look('" + fi_id + "');\" >查看</a>";
		}else{
		return "<center><a href=\"javascript:void(0)\" onclick=\"openAccount('" + fi_id + "');\" >开账</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + fi_id + "');\" >编辑</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"look('" + fi_id + "');\" >查看</a>"+
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" + fi_id+"');\" >删除</a></center>";
	
	}
	}else if(status==4)
	{
		return "<center><a href=\"javascript:void(0)\" onclick=\"createNewFID('" + fi_id + "');\" >新建明细</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"look('" + fi_id + "');\" >查看详情</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"showDetials('" + fi_id + "');\" >查看明细</a></center>";
	}
	else{
		return "<center><a href=\"javascript:void(0)\" onclick=\"look('" + fi_id + "');\" >查看详情</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"showDetials('" + fi_id + "');\" >查看明细</a></center>";
	}
}

function createNewFID(fi_id){
	window.location.href='fidNewCreate.jsp?fi_id='+fi_id;
}
function showDetials(fi_id){
		window.location.href='fidShowDetial.jsp?fi_id='+fi_id;
}
function showDetial(fi_id){
		window.location.href='fidShowDetial.jsp?fi_id='+fi_id;
}
function edit(fi_id){
	window.location.href='fiEdit.jsp?fi_id='+fi_id;
}
function look(fi_id){
	window.location.href='fiLook.jsp?fi_id='+fi_id;
}
function deletes(fi_id){
	var url = "<%=contextPath %>/SpringR/finance/deletePPD?ppd_id="+fi_id;
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

function openAccount(fi_id){
 var url = "<%=contextPath %>/SpringR/finance/openAccount?fi_id="+fi_id;
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
	window.location.href='fiNewCreate.jsp';
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;应收单管理</span>
   </td>
    <td class="Big">
   <input type="button" value="新建" class="BigButton" onclick="newCreate();">
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