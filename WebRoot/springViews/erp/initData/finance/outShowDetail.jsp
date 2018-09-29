<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String fId = request.getParameter("fId");
if(fId==null|fId==""){
	fId="0";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>应付单详情</title>
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
var fId="<%=fId%>";
var pageMgr = null;
function doInit(){
     var url = "<%=contextPath %>/SpringR/finance/outShowDetail?fId="+fId;
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"hidden", name:"fId", text:"fId"},
	         {type:"data", name:"fi_code",  width: '15%', text:"应付单编号",render:recordCenterFunc},    
	         {type:"data", name:"code",  width: '15%', text:"应付明细编号",render:recordCenterFunc},    
	         {type:"data", name:"status",  width: '20%', text:"应付明细状态",render:selectStatusFunc},
	         {type:"selfdef", text:"操作", width: '20%',render:opts}]
	    };
	    pageMgr = new YHJsPage(cfgs);
	    pageMgr.show();
	    var total = pageMgr.pageInfo.totalRecord;
	    if(total){
	      showCntrl('listContainer');
	      var mrs = " 共 " + total + " 条记录 ！";
	    }else{
	      WarningMsrg('无明细信息', 'msrg');
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
	var fdId = this.getCellData(recordIndex, "id");
	var fId = this.getCellData(recordIndex, "fId");
	var status = this.getCellData(recordIndex, "status");
	var code = this.getCellData(recordIndex, "code");
	if(status=='0'){
	return "<center><a href=\"javascript:void(0)\" onclick=\"openAccount('"+ fdId+"');\" >开账</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"showDetials('" + fdId+"','"+fId + "');\" >查看详情</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" + fdId+"');\" >删除</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + fdId+"','"+fId+"');\" >编辑</a></center>";
	}else{
		return "<center><a href=\"javascript:void(0)\" onclick=\"showDetials('" + fdId+"','"+fId + "');\" >查看详情</a>&nbsp</center>";
	}
}


function openAccount(fdId){
 var url = "<%=contextPath %>/SpringR/finance/openAccountFinanceOutDetial?fdId="+fdId;
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
function deletes(fdId,fId){
	var url = "<%=contextPath %>/SpringR/finance/deleteOutDetail?fdId="+fdId+"&fId="+fId;
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
function showDetials(fdId,fId){
		window.location.href='outShowDetails.jsp?fdId='+fdId+"&fId="+fId;
}
function edit(fdId,fId){
		window.location.href='editOutDetail.jsp?fdId='+fdId+"&fId="+fId;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;应付单管理</span>
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