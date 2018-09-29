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
<title>销售发货通知管理</title>
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
     var url = "<%=contextPath %>/SpringR/warehouse/sendFormManage?status="+status+"&beginTime="+beginTime+"&endTime="+endTime;
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"order_code",  width: '20%', text:"订单编号",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}},    
	         {type:"data", name:"po_code",  width: '20%', text:"订单出货单编号",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}},    
	         {type:"data", name:"po_title", width: '15%', text:"订单出货单主题",render:recordCenterFunc}, 
	         {type:"hidden", name:"order_id", text:"order_id"},
	         {type:"data", name:"po_status",  width: '15%', text:"订单出货单状态",render:selectStatusFunc},
<%--	         {type:"data", name:"remark",  width: '10%', text:"备注",render:recordCenterFunc},--%>
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
 window.location = "sendManage.jsp?status="+statuss+"&beginTime="+beginTime+"&endTime="+endTime;
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
	var po_id = this.getCellData(recordIndex, "id");//出货单id
	var po_status = this.getCellData(recordIndex, "po_status");
	var order_id = this.getCellData(recordIndex, "order_id");//销售订单id
	var orderCode = this.getCellData(recordIndex, "order_code");//销售订单id
	if(po_status == '5'){
		return "<center><a href=\"javascript:void(0)\" onclick=\"showDetial('" + po_id + "','"+order_id+"');\" >查看明细</a></center>";
	}
	else if(checkHasValue(order_id) == '1'){
		return "<center><a href=\"javascript:void(0)\" onclick=\"createNotify('" + order_id + "','"+orderCode+"');\" >新建生产通知单</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"newCreate('" + po_id + "','"+order_id+"');\" >新建发货单</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"showDetial('" + po_id + "','"+order_id+"');\" >查看明细</a></center>";
	}else{
		return "<center><a href=\"javascript:void(0)\" onclick=\"createNotify('" + order_id + "','"+orderCode+"');\" >新建生产通知单</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"newCreate('" + po_id + "','"+order_id+"');\" >新建发货单</a></center>";
	}
}

function checkHasValue(order_id){
	var flag = '0';
	var url = "<%=contextPath %>/SpringR/warehouse/checkHasValue?order_id="+order_id;
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

function showDetial(po_id,order_id){
	window.location.href='showDetial.jsp?po_id='+po_id+'&order_id='+order_id;
}
function newCreate(po_id,order_id){
	window.location.href='newCreate.jsp?po_id='+po_id+'&order_id='+order_id;
}
function createNotify(order_id,orderCode){
	window.location.href='<%=contextPath%>/springViews/erp/produce/notifyAdd1.jsp?order_id='+order_id+'&orderCode='+orderCode;
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
				<option value="<%=oa.spring.util.StaticData.RUNNING%>">执行中</option>
				<option value="<%=oa.spring.util.StaticData.OVER%>">已完成</option>
			</select>
      </td>
      <td nowrap class="TableData">起始时间： 
        <input type="text" id="beginTime" name="beginTime" size="19" maxlength="19" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">截止时间：
        <input type="text" id="endTime" name="endTime" size="19" maxlength="19" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="endTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td> 
      <td><input type="button" value="搜索" onclick="showManage()"  class="BigButton" ></td>
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