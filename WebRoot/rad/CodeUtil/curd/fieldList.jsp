<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	String tableNo = request.getParameter("tableNo");
%>
<%@ include file="/core/inc/t6.jsp"%>
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
var tableNo = "<%=tableNo %>";
function doInit(){
  $("#divieeeeee").jqGrid({
    url: "/yh/yh/rad/dsdef/act/YHDsDefMoreAct/testMethod2.act?dtoClass=yh.core.data.YHPageQueryParamNew&nameStr=seqId, tableNo, fieldNo, fieldName, propName, fieldDesc, fkTableNo, fkTableNo2, fkRelaFieldNo, fkNameFieldNo, fkFilter, codeClass, defaultValue, formatMode, formatRule, errorMsrg, fieldPrecision, fieldScale, dataType, isIdentity, displayLen, isMustFill, isPrimaryKey&tableNo=" + tableNo,
    datatype: "json",
    height: "230px",
    colNames:['seqId', 'tableNo', 'fieldNo', 'fieldName', 'propName', 'fieldDesc', 'fkTableNo', 'fkTableNo2', 'fkRelaFieldNo'
              , 'fkNameFieldNo', 'fkFilter', 'codeClass', 'defaultValue', 'formatMode', 'formatRule', 'errorMsrg', 'fieldPrecision'
              , 'fieldScale', 'dataType', 'isIdentity' , 'displayLen', 'isMustFill', 'isPrimaryKey', '操作'],
    colModel:[
      {name:'seqId'         ,index:'seqId'          ,width:80    ,hidden: true},    
      {name:'tableNo'       ,index:'tableNo'        ,width:100   ,hidden: true},
      {name:'fieldNo'       ,index:'fieldNo'        ,width:100   ,hidden: false   ,editable: true},
      {name:'fieldName'     ,index:'fieldName'      ,width:150   ,hidden: false   ,editable: true},
      {name:'propName'      ,index:'propName'       ,width:100   ,hidden: true},
      {name:'fieldDesc'     ,index:'fieldDesc'      ,width:150   ,hidden: false   ,editable: true},
      {name:'fkTableNo'     ,index:'fkTableNo'      ,width:100   ,hidden: true},
      {name:'fkTableNo2'    ,index:'fkTableNo2'     ,width:100   ,hidden: true},
      {name:'fkRelaFieldNo' ,index:'fkRelaFieldNo'  ,width:100   ,hidden: true},
      {name:'fkNameFieldNo' ,index:'fkNameFieldNo'  ,width:100   ,hidden: true},
      {name:'fkFilter'      ,index:'fkFilter'       ,width:100   ,hidden: true},
      {name:'codeClass'     ,index:'codeClass'      ,width:100   ,hidden: true},
      {name:'defaultValue'  ,index:'defaultValue'   ,width:100   ,hidden: true},
      {name:'formatMode'    ,index:'formatMode'     ,width:100   ,hidden: true},
      {name:'formatRule'    ,index:'formatRule'     ,width:100   ,hidden: true},
      {name:'errorMsrg'     ,index:'errorMsrg'      ,width:100   ,hidden: true},
      {name:'fieldPrecision',index:'fieldPrecision' ,width:100   ,hidden: true},
      {name:'fieldScale'    ,index:'fieldScale'     ,width:100   ,hidden: true},
      {name:'dataType'      ,index:'dataType'       ,width:100   ,hidden: true},
      {name:'isIdentity'    ,index:'isIdentity'     ,width:100   ,hidden: true},
      {name:'displayLen'    ,index:'displayLen'     ,width:100   ,hidden: true},
      {name:'isMustFill'    ,index:'isMustFill'     ,width:100   ,hidden: true},
      {name:'isPrimaryKey'  ,index:'isPrimaryKey'   ,width:100   ,hidden: true},
      {name:'act'           ,index:'act'            ,width:100},
    ],
    rowNum:10,
    rowList:[10,20,30],
    height: '100%',
    pager: '#pager',
    //sortname: 'id',
    viewrecords: true,
    sortorder: "desc",
    gridComplete: opts,
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
