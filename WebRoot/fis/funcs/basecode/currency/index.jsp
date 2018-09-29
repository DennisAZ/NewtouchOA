<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>币别</title>
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
	  url: contextPath + '/uifrm/data/currency.js',
	  datatype: "json",
	  height: "100%",
	  colNames:['','代码', '名称', '汇率', '是否本位'],
	  colModel:[
	    {name:'',index:'id', hidden: true},
	    {name:'',index:'invdate', width: 100},
	    {name:'',index:'name', width: 150},
	    {name:'',index:'amount', width: 100, align:"right"},
	    {name:'',index:'tax', width: 100, align: "center", 
	      formatter: function (cellvalue, options, rowObject) {
	        return {
	          "0": "否",
	          "1": "是"
	        } [cellvalue] || "";
	      }
	    }  
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
	
	$("#operate").dialog({
	  autoOpen: false,
    width: 400,
    height: 320,
    buttons: {
      "取消": function() {
        $("#operate").dialog("close");
      },
      "保存": function() {
      }
    },
    title: "新增"
  });
});

function add() {
  $("#operate").dialog("option", {title: "新增"}).dialog("open");
}

function edit() {
  $("#operate").dialog("option", {title: "修改"}).dialog("open");
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
  <div class="page-header">
    <h2>币别</h2>
  </div>
  <div class="top-toolbar">
    <a href="javascript: void(0)" onclick="add()"><span>新增</span></a>
    <a href="javascript: void(0)" onclick="edit()"><span>修改</span></a>
    <a href="javascript: void(0)" onclick="del()"><span>批量删除</span></a>
    <a href="javascript: void(0)" onclick="exp()"><span>导出</span></a>
  </div>
  <div id="pager"></div>
  <table id="list"></table></body>
  <div class="hidden">
    <div id="operate">
      <form class="uniForm">
	      <fieldset class="inlineLabels">
		      <div class="ctrlHolder">
		        <label for="code"><em>*</em> 代码</label>
		        <input name="code" id="code" size="35" maxlength="50" type="text" class="textInput required validateInteger"/>
		        <p class="formHint">请输入数字</p>
		      </div>
		      
		      <div class="ctrlHolder">
		        <label for="name"><em>*</em> 名称</label>
		        <input name="name" id="name" data-default-value="" size="35" maxlength="50" type="text" class="textInput required"/>
		        <p class="formHint"></p>
		      </div>
		    
		      <div class="ctrlHolder">
		        <label for="name"><em>*</em> 汇率</label>
		        <input name="name" id="name" data-default-value="" size="35" maxlength="50" type="text" class="textInput required"/>
		        <p class="formHint"></p>
		      </div>
        </fieldset>
      </form>
    </div>
  </div>
</body>
</html>