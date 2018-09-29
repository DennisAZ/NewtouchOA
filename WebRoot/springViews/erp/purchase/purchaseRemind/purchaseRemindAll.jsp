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
<title>采购订单管理</title>
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
     var url = "<%=contextPath %>/SpringR/purchase/getPurchaseListByPerson?status="+status+"&beginTime="+beginTime+"&endTime="+endTime;
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"purOutId", text:"id"},
	         {type:"hidden", name:"purId", text:"id"},
	         {type:"data", name:"code",  width: '10%', text:"采购编号",render:recordCenterFunc},    
	         {type:"data", name:"title",  width: '20%', text:"采购单主题",render:recordCenterFunc},
	         {type:"data", name:"person",  width: '10%', text:"采购员",render:recordCenterFunc},    
	         {type:"data", name:"signDate",  width: '10%', text:"日期",render:recordCenterFunc},
	         {type:"data", name:"status",  width: '10%', text:"采购状态",render:options},    
	          {type:"data", name:"remark",  width: '10%', text:"备注",render:recordCenterFunc},
	         {type:"selfdef", text:"操作", width: '20%',render:opts}]
	    };
	    pageMgr = new YHJsPage(cfgs);
	    pageMgr.show();
	    var total = pageMgr.pageInfo.totalRecord;
	    if(total){
	      showCntrl('listContainer');
	      var mrs = " 共 " + total + " 条记录 ！";
	    }else{
	      WarningMsrg('无符合条件的采购信息', 'msrg');
	    }
	    
	    selectOption(); //查询条件参数的传递
	    
	    toQueryTime(); //查询时间
}

function selectOption(){
     var selects = document.getElementById("pur_status");
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
 var statuss=jQuery('#pur_status').val();
   if(statuss==-4){
 	alert("请先选择状态！");
 	return false;
 }
 var beginTime=jQuery('#beginTime').val();
 var endTime=jQuery('#endTime').val();
 window.location.href = contextPath + "/springViews/erp/purchase/purchaseRemind/purchaseRemindAll.jsp?status="+statuss+"&beginTime="+beginTime+"&endTime="+endTime;
	 }
 }
	
function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}

function opts(cellData, recordIndex, columIndex){
	var purOutId = this.getCellData(recordIndex, "purOutId");
	var orderStatus = this.getCellData(recordIndex, "status");
	var purId = this.getCellData(recordIndex, "purId");
	var code = this.getCellData(recordIndex, "code");
	var title = this.getCellData(recordIndex, "title");
	var person = this.getCellData(recordIndex, "person");
	if(orderStatus==0){
		return "<center><a href=\"javascript:void(0)\" onclick=\"createFlowCheck('" + purId +"','"+code+"','"+title+ "','"+person+"');\" >提交审核</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"detail('" + purId + "');\" >查看</a></center>";
	}else{
		return "<center><a href=\"javascript:void(0)\" onclick=\"detail('" + purId + "');\" >查看</a>";
	}
}
function options(cellData, recordIndex, columIndex){
	var status = this.getCellData(recordIndex, "status");
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

function detail(id){
	window.location.href = contextPath + '/springViews/erp/purchase/purchaseRemind/detail.jsp?purchaseId='+id;
}

function createFlowCheck(purId,code,title,person){
	var para = "KEY="+purId+"&CODE="+code+"&TITLE="+title+"&PERSON="+person;
	createNewWork(para,false);
}

function createNewWork(para , isNotOpenWindow){
  var url = contextPath + "/yh/subsys/oa/saleOrder/act/YHERPApplyAct/purchaseApply.act";
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
function deletes(id,purOutId){
	var url = "<%=contextPath %>/SpringR/purchase/deletePurchase?purchaseId="+id+"&purOutId="+purOutId;
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
function newCreate1(){
	window.location.href="purchaseAdd.jsp?reqId=0"
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
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;采购信息管理</span>
   </td>
 </tr>
 <tr>
 	 <td class="TableData" >
 	状态:
			<select id="pur_status" name="pur_status" >
				<option value="-4">请先选择</option>
				<option value="-3">全部</option>
				<option value="<%=oa.spring.util.StaticData.RUNNING%>">执行中</option>
				<option value="<%=oa.spring.util.StaticData.NEW_CREATE%>">新建</option>
				<option value="<%=oa.spring.util.StaticData.AUDITING%>">审核中</option>
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
       <td class="Big">
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