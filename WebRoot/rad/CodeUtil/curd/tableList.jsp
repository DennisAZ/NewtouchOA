<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/core/inc/t6.jsp"%>
<%
String categoryNo = request.getParameter("categoryNo");
%>
<title>列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=jsPath%>/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jquery.ux.borderlayout.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jquery-ui-patch.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/jquery.jqGrid.src.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqGrid/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/zTree/zTreeStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<script type="text/javascript" src="<%=jsPath%>/ui/zTree/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript">
var categoryNo = "<%=categoryNo %>";
function doInit(){
	$("#divieeeeee").jqGrid({
    url:"/yh/yh/rad/grid/act/YHGridNomalAct/jsonTest.act?dtoClass=yh.core.data.YHPageQueryParamNew&nameStr=seqId,tableNo,tableName,tableDesc,className,classDesc,categoryNo&flag=1&categoryNo="+categoryNo,
    datatype: "json",
    height: "230px",
    colNames:['seqId', '表编码', '表名称', '表描述', '类名称', '表类型', 'categoryNo', '操作'],
    colModel:[
      {name:'seqId'				,index:'seqId'					,width:80		,hidden: true},    
      {name:'tableNo'			,index:'tableNo'				,width:60 },
      {name:'tableName'		,index:'tableName'			,width:220},
      {name:'tableDesc'		,index:'tableDesc'			,width:150},
      {name:'className'		,index:'className'			,width:180	,hidden: true},
      {name:'classDesc'		,index:'classDesc'			,width:80 	,hidden: true},
      {name:'categoryNo'	,index:'categoryNo'			,width:80 	,hidden: true},
      {name:'act'					,index:'act'						,width:70}
    ],
    rowNum:10,
    rowList:[10,20,30],
    pager: '#pager',
    gridComplete: opts,
    //sortname: 'id',
    viewrecords: true,
    sortorder: "desc",
    ondblClickRow:click
  });
	$("#divieeeeee").jqGrid('navGrid','#pager',{add:false, edit:false, del:false, search:true, refresh:true});
}

function opts(){
	var ids = $("#divieeeeee").jqGrid('getDataIDs');
	for(var i = 0; i < ids.length; i++){
		var rowId = ids[i];
		var str = "<center>"
				 		+ "<a href=javascript:click(" + rowId + ");><font color='blue'>选择</font></a> "
						+ "</center>";
		$("#divieeeeee").jqGrid('setRowData',ids[i],{act:str});
	}	
}

function click(index) {
  var record = $("#divieeeeee").jqGrid('getRowData',index);
  window.returnValue = record;
  window.close();
}
</script>
</head>
<body onload="doInit()">
	<div id="divi" class="ui-layout-center">
	  <table id="divieeeeee"></table>
	  <div id="pager"></div>
	</div>
</body>
</html>
