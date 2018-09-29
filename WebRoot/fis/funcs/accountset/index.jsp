<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>帐套</title>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-patch.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/jquery.jqGrid.src.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqGrid/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/zTree/zTreeStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<script type="text/javascript" src="<%=jsPath%>/ui/uni-form/uni-form-validation.jquery.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/uni-form/css/uni-form.css" media="screen" rel="stylesheet"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/uni-form/css/default.uni-form.css" title="Default Style" media="screen" rel="stylesheet"/>
<script type="text/javascript">
$(function() {
  $("#list").jqGrid({
    url: contextPath + '/uifrm/data/accountset.js',
    datatype: "json",
    height: "100%",
    colNames:['帐套编号', '帐套名称', '会计年度', '财务年月'],
    colModel:[
      {name:'',index:'invdate', width: 100},
      {name:'',index:'name', width: 150},
      {name:'',index:'amount', width: 100, align:"right"},
      {name:'',index:'tax', width: 100, align: "center"}  
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
    <h2>帐套</h2>
  </div>
  <div>
    <div class="top-toolbar">
      <a href="javascript: void(0)"><span>新增</span></a>
      <a href="javascript: void(0)"><span>复制</span></a>
      <a href="javascript: void(0)"><span>导出</span></a>
      <a href="javascript: void(0)"><span>新增导入</span></a>
      <a href="javascript: void(0)"><span>覆盖导入</span></a>
      <a href="javascript: void(0)"><span>设置权限</span></a>
      <a href="javascript: void(0)"><span>删除</span></a>
    </div>
    <table id="list"></table>
    <div id="pager"></div>
  </div>
</body>
</html>