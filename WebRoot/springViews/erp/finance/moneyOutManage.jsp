<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String status=request.getParameter("status");
	if("".equals(status)||status==null){
		status="0";
	}
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>出款单管理</title>
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
function doInit(){
     var url = "<%=contextPath %>/SpringR/finance/moneyOutManage?status="+status;
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"fId", text:"id"},
	         {type:"hidden", name:"fTypeId", text:"typeId"},
	         {type:"data", name:"fType",  width: '15%', text:"出款去处",render:recordCenterFunc},    
	         {type:"data", name:"fCode",  width: '15%', text:"出款单编号",render:recordCenterFunc},    
	         {type:"data", name:"fPayee", width: '20%', text:"收款人",render:recordCenterFunc}, 
	         {type:"data", name:"fStatus",  width: '20%', text:"出款单状态",render:selectStatusFunc},
	         {type:"selfdef", text:"操作", width: '30%',render:opts}]
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

function showManage(){


 var statuss=jQuery('#status').val();
 if(statuss==-4){
 	alert("请先选择状态！");
 	return false;
 }

 window.location = "moneyOutManage.jsp?status="+statuss;
	
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
	var fId = this.getCellData(recordIndex, "fId");//回款单id
	var fTypeId = this.getCellData(recordIndex, "fTypeId");//销售订单id
	var fStatus = this.getCellData(recordIndex, "fStatus");
	var fCode = this.getCellData(recordIndex, "fCode");
	if(fStatus == '4'){
		return "<center><a href=\"javascript:void(0)\" onclick=\"newCreate('" +fId+"');\" >新建明细</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"showDetial1('" + fId +"');\" >查看详细</a>"
		+"&nbsp<a href=\"javascript:void(0)\" onclick=\"showDetial('" + fId +"');\" >查看明细</a></center>";
	}
	if(fStatus!='0'){
	return "<center><a href=\"javascript:void(0)\" onclick=\"showDetial1('" + fId +"');\" >查看详细</a>"+
			"&nbsp<a href=\"javascript:void(0)\" onclick=\"showDetial('" + fId +"');\" >查看记录</a></center>";
	}
	if(fTypeId!='0'&fStatus=='-1'){
	return "<center><a href=\"javascript:void(0)\" onclick=\"createFlowCheck('" +fId+"','"+fCode+"');\" >提交审核</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"showDetial1('" + fId +"');\" >查看详细</a>"+
			"&nbsp<a href=\"javascript:void(0)\" onclick=\"newCreate('" + fId +"');\" >新建明细</a></center>";
	}else{
	return "<center><a href=\"javascript:void(0)\" onclick=\"createFlowCheck('" +fId+"','"+fCode+"');\" >提交审核</a>" +
	"&nbsp<a href=\"javascript:void(0)\" onclick=\"showDetial1('" + fId +"');\" >查看详细</a>"+
	"&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + fId +"');\" >编辑</a>"+
	"&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" +fId+"');\" >删除</a>" +
			"&nbsp<a href=\"javascript:void(0)\" onclick=\"newCreate('" + fId +"');\" >新建明细</a></center>";
	}
}

function createFlowCheck(fId,code){
	var para = "KEY="+fId+"&CODE="+code;
	createNewWork(para,false);
}

function createNewWork(para , isNotOpenWindow){
  var url = contextPath + "/yh/subsys/oa/saleOrder/act/YHERPApplyAct/financeOutApply.act";
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

function showDetial(fId){
	window.location.href='outShowDetail.jsp?fId='+fId;
}
function showDetial1(fId,typeId){
	window.location.href='outShowDetail1.jsp?fId='+fId+'&typeId='+typeId;
}
function newCreate(fId){
	window.location.href='outNewCreate.jsp?fId='+fId;
}
function edit(fId){
	window.location.href='outEdit.jsp?fId='+fId;
}
function deletes(fId){
	var url = "<%=contextPath %>/SpringR/finance/deleteOut?fId="+fId;
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
	window.location.href='addOut.jsp';
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
 	 <td class="TableData" >
 	状态:
			<select id="status" name="status"  onchange="showManage()">
				<option value="all">全部</option>
				<option value="<%=oa.spring.util.StaticData.NEW_CREATE%>">新建</option>
				<option value="<%=oa.spring.util.StaticData.RUNNING%>">执行中</option>
				<option value="<%=oa.spring.util.StaticData.OVER%>">已完成</option>
			</select>
      </td>
        <td class="Big">
   <input type="button" value="新建" class="BigButton" onclick="newCreate1();">
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