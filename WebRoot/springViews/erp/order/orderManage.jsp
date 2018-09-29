<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>

<%
	String status=request.getParameter("status");
	if("".equals(status)||status==null){
		status="0";
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
<title>销售订单通知单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
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
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script> 
var pageMgr = null;
var  status="<%=status%>";
var  beginTime="<%=beginTime%>";
var  endTime="<%=endTime%>";
function doInit(){
  initTime();
	 var url = "<%=contextPath %>/SpringR/saleOrder/orderList?status="+status+"&beginTime="+beginTime+"&endTime="+endTime;
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
     	   sortIndex: 2,
      		sortDirect: "desc",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"hidden", name:"po_id", text:"id"},
	         {type:"data", name:"orderCode",  width: '20%', text:"销售编号",render:recordCenterFunc},    
	         {type:"data", name:"orderTitle",  width: '20%', text:"销售单主题",render:recordCenterFunc},
	         {type:"data", name:"salesperson",  width: '10%', text:"销售员",render:recordCenterFunc},    
	         {type:"data", name:"signDate",  width: '10%', text:"日期",render:recordCenterFunc},
	         {type:"data", name:"orderStatus",  width: '10%', text:"销售状态",render:selectStatusFunc},    
	         {type:"hidden", name:"remark",  width: '10%', text:"备注",render:recordCenterFunc},
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
     var selects = document.getElementById("pod_status");
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
 var statuss=jQuery('#pod_status').val();
 var beginTime=jQuery('#beginTime').val();
 var endTime=jQuery('#endTime').val();
 window.location = "orderManage.jsp?status="+statuss+"&beginTime="+beginTime+"&endTime="+endTime;
 }
	
	
	 
}
function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}

function opts(cellData, recordIndex, columIndex){
	var order_id = this.getCellData(recordIndex, "id");
	var po_id = this.getCellData(recordIndex, "po_id");
	var order_title = this.getCellData(recordIndex, "orderTitle");
	var order_code = this.getCellData(recordIndex, "orderCode");
	var status = this.getCellData(recordIndex, "orderStatus");
	return selectOPTByStatus(status,order_id,order_title,order_code,po_id);
}

function selectOPTByStatus(status,order_id,order_title,order_code,po_id){
	if(status==0){
		return "<center><a href=\"javascript:void(0)\" onclick=\"createFlowCheck('" + order_id + "','" + order_title + "','" + order_code + "');\" >提交审核</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"detail('" + order_id + "');\" >查看</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + order_id + "');\" >编辑</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" + order_id+"','"+po_id + "');\" >删除</a></center>";
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
	window.location.href='orderupdate.jsp?orderId='+id;
}
function detail(id){
	window.location.href='detail.jsp?orderId='+id;
}
function deletes(id,po_id){
	var url = "<%=contextPath %>/SpringR/saleOrder/deleteOrder?orderId="+id+"&po_id="+po_id;
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

function createFlowCheck(order_id,order_title,order_code){
	var para = "KEY="+order_id
				+"&ORDER_TITLE="+order_title
				+"&ORDER_CODE="+order_code;
	createNewWork(para,false);
}

function createNewWork(para , isNotOpenWindow){
  var url = contextPath + "/yh/subsys/oa/saleOrder/act/YHERPApplyAct/saleOrderApply.act";
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
function checkForm(){
  var beginTime = $F('beginTime');
  var endTime = $F('endTime');

 // if(endTime && !isValidDatetime(endTime)){
 //   alert("结束时间格式不对，应形如 1999-01-02 14:55:20");
 //   return false;
 // }

  //if(beginTime && !isValidDatetime(beginTime)){
  //  alert("起始时间格式不对，应形如 1999-01-02 14:55:20");
  //  return false;
 // }
  
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
function newCreate(){
	window.location.href='orderAdd.jsp?flag=1';
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
 	 <td class="TableData" >
 	状态:
			<select id="pod_status" name="pod_status" >
				<option value="all">全部</option>
				<option value="<%=oa.spring.util.StaticData.RUNNING%>">执行中</option>
				<option value="<%=oa.spring.util.StaticData.NEW_CREATE%>">新建</option>
				<option value="<%=oa.spring.util.StaticData.AUDITING%>">审核中</option>
				<option value="<%=oa.spring.util.StaticData.OVER%>">已完成</option>
			</select>
      </td>
      <td nowrap class="TableData">起始时间： 
        <input type="text" id="beginTime" name="beginTime" size="12" maxlength="12" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">截止时间：
        <input type="text" id="endTime" name="endTime" size="12" maxlength="12" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="endTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td> 
      <td><input type="button" value="搜索" onclick="showManage()"  class="BigButton" ></td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>


<div id="msrg">
</div>
</body>
</html>