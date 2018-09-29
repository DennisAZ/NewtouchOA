<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编号管理</title>
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
     var url = "<%=contextPath %>/SpringR/system/getCodeMsg/";
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"code_type",  width: '20%', text:"编号类型",render:options},    
	         {type:"data", name:"value",  width: '20%', text:"表达式的值",render:recordCenterFunc},    
	          {type:"data", name:"remark",  width: '20%', text:"备注",render:recordCenterFunc},
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
}

function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}

function opts(cellData, recordIndex, columIndex){
	var id = this.getCellData(recordIndex, "id");
	return "<center><a href=\"javascript:void(0)\" onclick=\"detail('" + id + "');\" >查看</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + id + "');\" >编辑</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" +id + "');\" >删除</a></center>";
	
}
function options(cellData, recordIndex, columIndex){
	var code_type = this.getCellData(recordIndex, "code_type");
	var value = "";
	var url = "<%=contextPath %>/SpringR/system/getCodeType?code_type="+code_type;
	jQuery.ajax({
		type:"POST",
		url:url,
		async:false,
		success:function(data){
			value = "<center>"+data+"</center>";
		}
	});
	return value;
}

function edit(id){
	window.location.href='editCode.jsp?id='+id;
}
function detail(id){
	window.location.href='codeDetail.jsp?id='+id;
}
function deletes(id){
	var url = "<%=contextPath %>/SpringR/system/deleteCodeById?id="+id;
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
function newCreate(){
	window.location.href='codeAdd.jsp';
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;编号表达式管理</span>
   </td>
   <td class="Big">
   <input type="button" value="新建" class="BigButton" onclick="newCreate();">
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