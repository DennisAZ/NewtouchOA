<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>zTree</title>
<script type="text/javascript" src="<%=jsPath%>/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/jquery.jqGrid.src.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqGrid/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
<script type="text/javascript">
$(function() {
	$("#list2").jqGrid({
	  url:'data.json',
	  datatype: "json",
	  colNames:['Inv No','Date', 'Client', 'Amount','Tax','Total','Notes'],
	  colModel:[
	    {name:'id',index:'id', width:55},
	    {name:'invdate',index:'invdate', width:90},
	    {name:'name',index:'name asc, invdate', width:100},
	    {name:'amount',index:'amount', width:80, align:"right"},
	    {name:'tax',index:'tax', width:80, align:"right"},    
	    {name:'total',index:'total', width:80,align:"right"},   
	    {name:'note',index:'note', width:150, sortable:false}   
	  ],
	  rowNum:10,
	  rowList:[10,20,30],
	  pager: '#pager2',
	  sortname: 'id',
	  viewrecords: true,
	  sortorder: "desc",
	  caption:"JSON Example"
	});
});
</script>
</head>
<body>
  <div id="pager2"></div>
  <table id="list2">
  </table>
</body>
</html>