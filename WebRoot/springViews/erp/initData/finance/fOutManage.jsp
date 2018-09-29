<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
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
var pageMgr = null;
function doInit(){
     var url = "<%=contextPath %>/SpringR/finance/fidManage";
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"hidden", name:"fi_id", text:"fi_id"},
	         {type:"data", name:"fi_code",  width: '15%', text:"应收单编号",render:recordCenterFunc},    
	         {type:"data", name:"code",  width: '15%', text:"应收明细编号",render:recordCenterFunc},    
	         {type:"data", name:"status",  width: '20%', text:"应收明细状态",render:selectStatusFunc},
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
	var fid_id = this.getCellData(recordIndex, "id");
	var fi_id = this.getCellData(recordIndex, "fi_id");
	var code = this.getCellData(recordIndex, "code");
	var status = this.getCellData(recordIndex, "status");
	return selectOPTByStatus(fi_id,fid_id,code,status);
}

function selectOPTByStatus(fi_id,fid_id,code,status){
	if(status==0){
		return "<center><a href=\"javascript:void(0)\" onclick=\"createFlowCheck('" + fi_id + "','" + fid_id + "','" + code + "');\" >提交审核</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"showDetials('" + fid_id + "');\" >查看详情</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + fid_id + "');\" >编辑</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" + fid_id+"');\" >删除</a></center>";
	}else{
		return "<center><a href=\"javascript:void(0)\" onclick=\"showDetials('" + fid_id + "');\" >查看详情</a></center>";
	}
}

function showDetials(fid_id){
		window.location.href='fidDetials.jsp?fid_id='+fid_id;
}
function edit(fid_id){
	window.location.href='fidEdit.jsp?fid_id='+fid_id;
}
function deletes(fid_id){
	var url = "<%=contextPath %>/SpringR/finance/deleteFID?fid_id="+fid_id;
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


function createFlowCheck(fi_id,fid_id,code){
	var para = "KEY="+fid_id
				+"&CODE="+code;
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

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;应付明细管理</span>
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