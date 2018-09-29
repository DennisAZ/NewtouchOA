<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>凭证查询</title>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.borderlayout.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-patch.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/jquery.jqGrid.src.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqGrid/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<script type="text/javascript">
$(function() {
  var dates = $( "#from, #to" ).datepicker({
    defaultDate: "+1w",
    changeMonth: true,
    onSelect: function( selectedDate ) {
      var option = this.id == "from" ? "minDate" : "maxDate",
        instance = $( this ).data( "datepicker" ),
        date = $.datepicker.parseDate(
          instance.settings.dateFormat ||
          $.datepicker._defaults.dateFormat,
          selectedDate, instance.settings );
      dates.not( this ).datepicker( "option", option, date );
    }
  });
  
  $("#list").jqGrid({
    url:'data.js',
    datatype: "json",
    height: "100%",
    autowidth: true,
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
    toolbar: [true, "top"]
  });
  
  var toolbar = $("<div></div>").toolbar({
    btns: [{
      text: "新增",
      icon:'',
      handler: function() {
        
      }
    }, {
      text: "批量删除",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "审核",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "反审核",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "全部审核",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "重整凭证号",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "插入凭证",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "打印凭证",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "打印列表",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "引入",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "引出",
      icon:'',
      handler: function(e, t, a) {
      }
    }]
  });
  $("#t_list").append(toolbar);
});
</script>
</head>
<body>
  <div class="page-header">
    <h2>凭证查询</h2>
  </div>
  <div class="form">
    <form>
	    <fieldset>
		    <table>
			    <tr>
				    <td>
			        <label for="date"><input type="radio" checked="checked" id="date" name="filter">日期:</label>
			      </td>
			      <td>
				      <label for="from"></label>
							<input class="small" type="text" id="from" name="from"/>
							<label for="to">至</label>
							<input class="small" type="text" id="to" name="to"/>
						</td>
			    </tr>
			    <tr>
				    <td>
			        <label for="number"><input type="radio" id="number" name="filter">凭证期号:</label>
			      </td>
			      <td>
			        <input class="small" type="text"/>年<input class="small" type="text"/>期
			        至:
			        <input class="small" type="text"/>年<input class="small" type="text"/>期
			        <select>
			          <option>所有</option>
			          <option>(记)</option>
			        </select>
			        <input class="small" type="text"/>
			      </td>
		      </tr>
	      </table>
	    </fieldset>
	    <fieldset class="action">
	      <button>查询</button>
	      <button>高级</button>
	    </fieldset>
    </form>
  </div>
  <div id="pager"></div>
  <table id="list"></table>
</body>
</html>