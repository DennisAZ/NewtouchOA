<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>生产通知单管理</title>
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
     var url = "<%=contextPath %>/SpringR/produce/getNotify/"+new Date().getTime();
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"code",  width: '15%', text:"通知单编号",render:recordCenterFunc},  
	         {type:"data", name:"type",  width: '10%', text:"来源",render:recordCenterFunc},    
	         {type:"hidden", name:"type_id",  width: '1%', text:"销售单id",render:recordCenterFunc},
	         {type:"hidden", name:"produce_time",  width: '1%', text:"生产时间",render:recordCenterFunc},
	         {type:"hidden", name:"person_id",  width: '1%', text:"创建人id",render:recordCenterFunc},    
	         {type:"data", name:"person",  width: '5%', text:"创建人",render:recordCenterFunc},
	         {type:"data", name:"create_time",  width: '10%', text:"创建时间",render:recordCenterFunc},
	         {type:"data", name:"status",  width: '10%', text:"状态",render:selectStatusFunc},
	         {type:"hidden", name:"remark",  width: '1%', text:"备注",render:recordCenterFunc},
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
	      WarningMsrg('无符合条件的生产工艺信息', 'msrg');
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
	var notifyId = this.getCellData(recordIndex, "id");
	var type_id = this.getCellData(recordIndex, "type_id");
	var code = this.getCellData(recordIndex, "code");
	var status = this.getCellData(recordIndex, "status");
	if(status=="4"){
		return "<center>&nbsp<a href=\"javascript:void(0)\" onclick=\"addPlan('" + notifyId + "');\" >新建计划</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"notifyDetail('" + notifyId + "');\" >查看</a></center>";
	}else if(status=="1" || status=="5"){
		return "<center><a href=\"javascript:void(0)\" onclick=\"notifyDetail('" + notifyId + "');\" >查看</a></center>";
	}else{
		if(type_id != "-1"){
			//如果来自销售单
			return "<center><a href=\"javascript:void(0)\" onclick=\"createFlowCheck('" + notifyId + "','"+code+"');\" >提交审核</a></center>";
		}
	  return "<center><a href=\"javascript:void(0)\" onclick=\"createFlowCheck('" + notifyId + "','"+code+"');\" >提交审核</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"notifyDetail('" + notifyId + "');\" >查看</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + notifyId + "');\" >编辑</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" + notifyId + "');\" >删除</a></center>";
	}
}

function edit(id){
	window.location.href='notifyEdit.jsp?notifyId='+id;
}
function notifyDetail(notifyId){
	window.location.href='notifyDetail.jsp?notifyId='+notifyId;
}
function addPlan(id){
	window.location.href='planAdd.jsp?notifyId='+id;
}
function deletes(id){
	var url = "<%=contextPath %>/SpringR/produce/deleteNotify?notifyId="+id;
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

function createFlowCheck(id,code){
	var para = "KEY="+id+"&CODE="+code;
	createNewWork(para,false);
}

function createNewWork(para , isNotOpenWindow){
  var url = contextPath + "/yh/subsys/oa/saleOrder/act/YHERPApplyAct/notifyApply.act";
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
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;生产通知单管理</span>
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