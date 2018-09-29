<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>采购计划通知管理</title>
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
     var url = "<%=contextPath %>/SpringR/purchase/requestList/"+new Date().getTime();
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"reqCode",  width: '10%', text:"申请单编号",render:recordCenterFunc},  
	         {type:"data", name:"reqName",  width: '10%', text:"申请单名称",render:recordCenterFunc},    
	         {type:"data", name:"reqPerson",  width: '20%', text:"申请人",render:recordCenterFunc},
	         {type:"data", name:"signDate",  width: '10%', text:"日期",render:recordCenterFunc},
	         {type:"data", name:"reqStatus",  width: '10%', text:"申请状态",render:options},    
	          {type:"data", name:"reqRemark",  width: '10%', text:"备注",render:recordCenterFunc},
	         {type:"selfdef", text:"操作", width: '20%',render:opts}]
	    };
	    pageMgr = new YHJsPage(cfgs);
	    pageMgr.show();
	    
	    var total = pageMgr.pageInfo.totalRecord;
	    if(total){
	      showCntrl('listContainer');
	      var mrs = " 共 " + total + " 条记录 ！";
	    }else{
	      WarningMsrg('无符合条件的采购申请单信息', 'msrg');
	    }
}

function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}

function opts(cellData, recordIndex, columIndex){
	var reqId = this.getCellData(recordIndex, "id");
	var reqName = this.getCellData(recordIndex, "reqName");
	var reqCode = this.getCellData(recordIndex, "reqCode");
	var orderStatus = this.getCellData(recordIndex, "reqStatus");
	if(orderStatus==0){
	return "<center><a href=\"javascript:void(0)\" onclick=\"createFlowCheck('" + reqId + "','" + reqName + "','" + reqCode + "');\" >提交审核</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"detail('" + reqId + "');\" >查看</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + reqId + "');\" >编辑</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" +reqId + "');\" >删除</a></center>";
		
	}else if(orderStatus=="1"||orderStatus==1){
	return "<center><a href=\"javascript:void(0)\" onclick=\"detail('" + reqId + "');\" >查看</a>";
	}else{
	return "<center><a href=\"javascript:void(0)\" onclick=\"detail('" + reqId + "');\" >查看</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"newAdd('" + reqId + "');\" >新建采购单</a>";
	
	}
}

function createFlowCheck(fi_id,reqName,code){
	var para = "KEY="+fid_id+"&NAME="+reqName+"&CODE="+code;
	createNewWork(para,false);
}

function createNewWork(para , isNotOpenWindow){
  var url = contextPath + "/yh/subsys/oa/saleOrder/act/YHERPApplyAct/financeInDetialApply.act";
  var json = getJsonRs(url ,  para);
  if(json.rtState == "0"){
    var url2 =  json.rtData.url;
    if (isNotOpenWindow) {
      location.href = url2;
    } else {
      window.open(url2);
    }
  }else{
    alert(json.rtMsrg);
  }
}


function options(cellData, recordIndex, columIndex){
	var status = this.getCellData(recordIndex, "reqStatus");
	if(status==0){
	return "<center>新建状态</center>";
	}else if(status==1){
		return "<center>审核中</center>";
	}else if(status==2){
		return "<center>审核通过</center>";
	}else if(status==3){
		return "<center>审核没通过</center>";
	}else if(status==4){
		return "<center>执行中</center>";
	}else{
		return "<center>已完成</center>";
	}
}

function edit(id){
	window.location.href='requestEdit.jsp?reqId='+id;
}
function detail(id){
	window.location.href='requestDetail.jsp?reqId='+id;
}
function deletes(id,purOutId){
	var url = "<%=contextPath %>/SpringR/purchase/deletePResult?reqId="+id;
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
function newAdd(reqId){
	window.location.href="purchaseAdd.jsp?reqId="+reqId;
}
function createFlowCheck(reqId,reqName,reqCode){
	var para = "KEY="+reqId
				+"&NAME="+reqName
				+"&CODE="+reqCode;
	createNewWork(para,false);
}

function createNewWork(para , isNotOpenWindow){
  var url = contextPath + "/yh/subsys/oa/saleOrder/act/YHERPApplyAct/purchaseRequestApply.act";
  var json = getJsonRs(url ,  para);
  if(json.rtState == "0"){
    var url2 =  json.rtData.url;
    if (isNotOpenWindow) {
      location.href = url2;
    } else {
      window.open(url2);
    }
  }else{
    alert(json.rtMsrg);
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<br>
<div id="listContainer" style="display:none;width:100;">
</div>


<div id="msrg">
</div>
</body>
</html>