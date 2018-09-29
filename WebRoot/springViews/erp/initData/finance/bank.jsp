<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>公司银行管理</title>
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
     var url = "<%=contextPath %>/SpringR/finance/bank/"+new Date().getTime();
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"name",  width: '10%', text:"银行名称",render:recordCenterFunc},    
	         {type:"data", name:"openName",  width: '10%', text:"开户名称",render:recordCenterFunc},    
	         {type:"data", name:"account",  width: '20%', text:"卡号",render:recordCenterFunc},
	         {type:"data", name:"money",  width: '10%', text:"资金",render:recordCenterFunc},
	          {type:"data", name:"reqRemark",  width: '10%', text:"备注",render:recordCenterFunc},
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
	      WarningMsrg('无符合条件的现金银行信息信息', 'msrg');
	    }
}

function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}

function opts(cellData, recordIndex, columIndex){
	var bankId = this.getCellData(recordIndex, "id");
	return "<center><a href=\"javascript:void(0)\" onclick=\"detail('" + bankId + "');\" >查看</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + bankId + "');\" >编辑</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" +bankId + "');\" >删除</a></center>";
	
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

function edit(bankId){
	window.location.href='editBank.jsp?bankId='+bankId;
}
function detail(bankId){
	window.location.href='detailBank.jsp?bankId='+bankId;
}
function deletes(bankId){
	var url = "<%=contextPath %>/SpringR/finance/deleteBank?bankId="+bankId;
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
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;公司银行管理</span>
   </td>
    <td class="Big">
   <input type="button" value="添加" class="BigButton" onclick="javascript:window.location.href='bankAdd.jsp'">
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