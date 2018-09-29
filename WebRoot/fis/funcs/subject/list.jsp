<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>科目</title>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/jquery.jqGrid.src.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/uni-form/uni-form-validation.jquery.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqGrid/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/uni-form/css/uni-form.css" media="screen" rel="stylesheet"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/uni-form/css/default.uni-form.css" title="Default Style" media="screen" rel="stylesheet"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<script type="text/javascript">
$(function() {
  $("#list").jqGrid({
    url:'data.js',
    datatype: "json",
    height: "100%",
    colNames:['代码','名称', '类别', '余额方向','外币核算','期末调汇','辅助核算', '状态'],
    colModel:[
      {name:'代码',index:'id', width:55},
      {name:'名称',index:'invdate', width:90},
      {name:'类别',index:'name', width:100},
      {name:'余额方向',index:'amount', width:80, align:"right"},
      {name:'外币核算',index:'tax', width:80, align:"right"},    
      {name:'期末调汇',index:'total', width:80,align:"right"},   
      {name:'辅助核算',index:'note', width:150, sortable:false}, 
      {name:'辅助核算',index:'note', width:150, sortable:false} 
    ],
    rowNum:10,
    rowList:[10,20,30],
    pager: '#pager',
    sortname: 'id',
    viewrecords: true,
    sortorder: "desc",
    multiselect: true,
    caption: "资产"
  });
  
  $(".top-toolbar a").button()
});

function add() {
  $('<iframe src="new.jsp"></iframe>').dialog({
    title: "新建科目",
    height: 450,
    width: 500,
    modal: true
  }).css({
    width: "100%"
  });
}

function del() {
  var gr = jQuery("#list").jqGrid('getGridParam','selrow');
  if( gr != null ) {
    jQuery("#list").jqGrid('delGridRow',gr,{reloadAfterSubmit:false});
  }
  else {
    alertMsg("请至少选择一行数据!");
  }
}
</script>
</head>
<body>
  <div class="top-toolbar">
    <a href="javascript:void(0)" onclick="add()"><span>新增</span></a>
    <a href="javascript:void(0)"><span>批量设置辅助核算</span></a>
    <a href="javascript:void(0)" onclick="del()"><span>批量删除</span></a>
    <a href="javascript:void(0)"><span>批量引入</span></a>
    <a href="javascript:void(0)"><span>引出 </span></a>
  </div>
  <div id="pager"></div>
  <table id="list">
  </table>
</body>
</html>