<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String status=request.getParameter("status");
	if("".equals(status)||status==null){
		status="-2";
	}
	String beginTime=request.getParameter("beginTime");
	if("".equals(beginTime)||beginTime==null){
		beginTime="";
	}
	String endTime=request.getParameter("endTime");
	if("".equals(endTime)||endTime==null){
		endTime="";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>发货单明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
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
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script> 
var pageMgr = null;
var  status="<%=status%>";
var  beginTime="<%=beginTime%>";
var  endTime="<%=endTime%>";
function doInit(){
initTime();
     var url = "<%=contextPath %>/SpringR/warehouse/whShowDetial?status="+status+"&beginTime="+beginTime+"&endTime="+endTime;
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"hidden", name:"order_id", text:"订单id"},
	         {type:"hidden", name:"po_id", text:"货单id"},
	         {type:"data", name:"order_code",  width: '24%', text:"订单编号",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}},    
	         {type:"data", name:"po_code", width: '24%', text:"订单出货单编号",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}}, 
	         {type:"data", name:"pod_code", width: '24%', text:"仓库发货单编号",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}}, 
	         {type:"data", name:"pod_status",  width: '8%', text:"状态",render:selectStatusFunc},
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
	    
	    selectOption(); //查询条件参数的传递
	    
	    toQueryTime(); //查询时间
}

function selectOption(){
     var selects = document.getElementById("status");
	 for(var i = 0; i < selects.length; i++){
    var prc = selects[i];
    if(status == prc.value){
      prc.selected = true;
    }
  }
}

function toQueryTime(){
	jQuery('#beginTime').val(beginTime);
    jQuery('#endTime').val(endTime);
    return ;
}
function showManage(){
if (checkForm()){

 var statuss=jQuery('#status').val();
 if(statuss==-4){
 	alert("请先选择状态！");
 	return false;
 }
 var beginTime=jQuery('#beginTime').val();
 var endTime=jQuery('#endTime').val();
 window.location = "whShowDetial.jsp?status="+statuss+"&beginTime="+beginTime+"&endTime="+endTime;
	 }
 }
	function checkForm(){
  var beginTime = $F('beginTime');
  var endTime = $F('endTime');


  if (endTime && beginTime && beginTime > endTime){ 
    alert("结束时间不能小于起始时间！");
    return false;
  }
  
  return true;
}
function initTime(){
  var beginTimePara = {
      inputId:'beginTime',
      property:{isHaveTime:false},
      bindToBtn:'beginTimeImg'
  };
  new Calendar(beginTimePara);
  
  var endTimePara = {
      inputId:'endTime',
      property:{isHaveTime:false},
      bindToBtn:'endTimeImg'
  };
  
  new Calendar(endTimePara);

  var date = new Date();
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
		return "<center><a href=\"javascript:void(0)\" onclick=\"createFlowCheck('" + order_id + "','" + po_id + "','" + pod_id + "','" + pod_code + "');\" >提交审核</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"showDetials('" + pod_id + "');\" >发货单详情</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + po_id + "','"+order_id+"','" + pod_id + "');\" >编辑</a>" +
		"&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" + pod_id+"');\" >删除</a></center>";
	}else if(status == 7){
		return "<center><a href=\"javascript:void(0)\" onclick=\"showDetials1('" + pod_id + "');\" >查看退货情况</a>&nbsp;<a href=\"javascript:void(0)\" onclick=\"showDetials('" + pod_id + "');\" >发货单详情</a></center>";
	}else{
		return "<center><a href=\"javascript:void(0)\" onclick=\"showDetials('" + pod_id + "');\" >发货单详情</a></center>";
	}
}

function showDetials(pod_id){
	window.location.href='showDetials.jsp?pod_id='+pod_id;
}
function showDetials1(pod_id){
	window.location.href='saleReturnDetail.jsp?typeId='+pod_id+'&flag=1';
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


function createFlowCheck(order_id,po_id,pod_id,pod_code){
	var para = "KEY="+pod_id
				+"&POD_CODE="+pod_code+"&ORDER_ID="+order_id+"&PO_ID="+po_id;
	createNewWork(para,false);
}

function createNewWork(para , isNotOpenWindow){
  var url = contextPath + "/yh/subsys/oa/saleOrder/act/YHERPApplyAct/whPODApply.act";
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
 	 <td class="TableData" >
 	状态:
			<select id="status" name="status" >
				<option value="-4">请选择</option>
				<option value="-3">全部</option>
				<option value="<%=oa.spring.util.StaticData.NEW_CREATE%>">新建</option>
				<option value="<%=oa.spring.util.StaticData.AUDITING%>">审核中</option>
				<option value="<%=oa.spring.util.StaticData.ALREADY_SEND%>">已发货，客户还没验收</option>
				<option value="<%=oa.spring.util.StaticData.CUSTOMER_NO_AGREE%>">客户验收不通过，有退货</option>
				<option value="<%=oa.spring.util.StaticData.OVER%>">已完成</option>
			</select>
      </td>
      <td nowrap class="TableData">起始时间： 
        <input type="text" id="beginTime" name="beginTime" size="14" maxlength="14" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">截止时间：
        <input type="text" id="endTime" name="endTime" size="14" maxlength="14" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="endTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td> 
      <td><input type="button" value="搜索" onclick="showManage()"  class="BigButton" ></td>
        <td class="Big">
   <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.go(-1)">
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