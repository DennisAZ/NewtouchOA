<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>总账</title>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery.ux.borderlayout.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-patch.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/jquery.jqGrid.src.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqGrid/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/zTree/zTreeStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<script type="text/javascript" src="<%=jsPath %>/ui/zTree/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript">
$(function() {
	$("body").layout({
	  north: {
	    size: 230
	  },
	  center: {
	    overflow: "auto"
	  }
	});
	
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
    multiselect: true
  });
	
	$("#subjectFromBtn").click(function() {
	  selectSubject({
	    callback: function(e,treeId, treeNode) {
	      $("#subjectFrom").val(treeNode.name);
	    }
	  });
	  return false;
	});
	$("#subjectToBtn").click(function() {
	  selectSubject({
	    callback:function(e,treeId, treeNode) {
	      $("#subjectTo").val(treeNode.name);
	    }
	  });
	  return false;
	});
});
</script>
</head>
<body>
	<div class="ui-layout-north">
		<div class="page-header">
	    <h2>明细账</h2>
	  </div>
	  <div class="form">
	    <form>
	      <fieldset>
	        <table>
	          <tr>
	            <td>
	              会计期间:
	            </td>
	            <td>
	            <input class="small" type="text"/>年<input class="small" type="text"/>期
	              至:
	              <input class="small" type="text"/>年<input class="small" type="text"/>期
	            </td>
	          </tr>
	          <tr>
	            <td>
	              会计科目:
	            </td>
	            <td>
	            <input class="big" id="subjectFrom" type="text"/><button id="subjectFromBtn">科目选择</button>
	              至:
	              <input class="big" id="subjectTo" type="text"/><button id="subjectToBtn">科目选择</button>
	            </td>
	          </tr>
	          <tr>
	            <td>
	              科目级次:
	            </td>
	            <td>
	            <input class="small" type="text"/>至<input class="small" type="text"/>
	              币别:<select class="small">
	                <option>人民币</option>
	                <option>美元</option>
	              </select>
	            </td>
	          </tr>
	          <tr>
	            <td>
	            </td>
	            <td>
	              <label for=""><input type="checkbox">显示辅助核算</label>
	            </td>
	          </tr>
	        </table>
	      </fieldset>
	      <fieldset class="action">
	        <button>查询</button>
	        <button>打印</button>
	        <button>分页打印</button>
	        <button>引出</button>
	      </fieldset>
	    </form>
	  </div>
	</div>
	<div class="ui-layout-center">
	  <div id="pager"></div>
	  <table id="list"></table></body>
	</div>
	
</html>