<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>凭证模板</title>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-patch.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/jquery.jqGrid.src.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqGrid/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/zTree/zTreeStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<script type="text/javascript">
$(function() {
  $("#list").jqGrid({
    url: contextPath + '/uifrm/data/currency.js',
    datatype: "json",
    height: "100%",
    colNames:['类别','模板名称', '摘要', '科目'],
    colModel:[
      {name:'',index:'id', width: 100},
      {name:'',index:'invdate', width: 100},
      {name:'',index:'name', width: 150},
      {name:'',index:'amount', width: 300, align:"right"}
    ],
    rowNum:10,
    rowList:[10,20,30],
    pager: '#pager',
    sortname: 'id',
    viewrecords: true,
    sortorder: "desc",
    multiselect: true
  });
  
  $(".top-toolbar a").button();
  
});
</script>
</head>
<body>
  <div class="page-header">
    <h2>凭证模板</h2>
  </div>
  <div class="top-toolbar">
    <a href="javascript: void(0)" onclick="del()"><span>批量删除</span></a>
    <a href="javascript: void(0)" onclick="exp()"><span>导出</span></a>
  </div>
  <div id="pager"></div>
  <table id="list"></table></body>
</body>
</html>