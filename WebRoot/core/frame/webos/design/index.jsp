<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>门户设计</title>
<link rel="stylesheet" type="text/css" href="../styles/style1/css/index.css"/>
<link rel="stylesheet" type="text/css" href="../styles/style1/css/design.css"/>
<link rel="stylesheet" type="text/css" href="../styles/style1/cmp/css/cmp-all.css" />
<link rel="stylesheet" type="text/css" href="../styles/style1/cmp/css/customer.css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.droppable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.resizable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.selectable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.yh.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.autolayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.fitlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.cardlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.freelayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.floatlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.columnlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.gridlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.container.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.module.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.panel.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.window.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.tip.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.button.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.borderlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/frame/webos/js/common.js"></script>
<script type="text/javascript">

//门户新增/修改参数化
var designType = '${param.type}';
var portalName = '${param.name}';
var portalId = '${param.id}';

var ctnInfo = {
  ctn: null,
  panels: [],
  id: portalId
};

$(document).ready(function() {
  $('input[name="layoutSel"]').click(function() {
    changeLayout(this.value);
  });

  var layoutCfg = {
    sortConnect: true,
    owner: null,
    designMode: true,
    droppable: {
      accept: '.portlet-source',
      greedy: true,
      dropStop: function(event, ui, cmp) {
        var opts = null;
        var el = ui.draggable;
        var id = el.attr('id').replace('btn', '');
        var sawdow = $('#sawdow' + id);
        sawdow.children('input').attr('checked', true);
        if (el.data('panel')) {
          addPanel(el.data('panel'), id, cmp, opts);
          el.draggable('disable');
          el.parent().addClass('port-sawdow-selected');
        }
        else if (el.data('layout')) {
          addLayout(el.data('layout'), id, cmp);
        }
      }
    }
  };

  /**
   * 定义布局 
   */
  var layout = {
    '0': {
      name: 'columnlayout',
      cfg: layoutCfg,
      handler: function(cfg) {
        cfg.layout = this.name;
        cfg.layoutCfg = this.cfg;
        cfg.layoutCfg.cellpadding = 3;
        cfg.items = [{
          columnWidth: '0.5',
          autoIdPrefix: 'portal'
        },{
          columnWidth: '0.5',
          autoIdPrefix: 'portal'
        }];
      }
    },
    '1': {
      name: 'columnlayout',
      cfg: layoutCfg,
      handler: function(cfg) {
        cfg.layout = this.name;
        cfg.layoutCfg = this.cfg;
        cfg.layoutCfg.cellpadding = 3;
        cfg.items = [{
          columnWidth: '0.3',
          autoIdPrefix: 'portal'
        },{
          columnWidth: '0.3',
          autoIdPrefix: 'portal'
        },{
          columnWidth: '0.4',
          autoIdPrefix: 'portal'
        }];
      }
    },
    '2': {
      name: 'freelayout',
      cfg: layoutCfg,
      handler: function(cfg) {
        cfg.layout = this.name;
        cfg.layoutCfg = this.cfg;
      }
    },
    '3': {
      name: 'gridlayout',
      cfg: layoutCfg,
      handler: function(cfg) {
	      cfg.layout = this.name;
	      cfg.layoutCfg = this.cfg;
	      cfg.layoutCfg.colsWidth = ['200px', '200px', '200px'];
	      cfg.layoutCfg.cols = 3;
	      cfg.layoutCfg.cellspacing = 4;
	      cfg.items = [{
          colSpan: 1,
          rowSpan: 1,
          autoIdPrefix: 'portal'
        },{
          colSpan: 1,
          rowSpan: 1,
          autoIdPrefix: 'portal'
        },{
          colSpan: 1,
          rowSpan: 1,
          autoIdPrefix: 'portal'
        },{
          colSpan: 1,
          rowSpan: 1,
          autoIdPrefix: 'portal'
        },{
          colSpan: 1,
          rowSpan: 1,
          autoIdPrefix: 'portal'
        },{
          colSpan: 1,
          rowSpan: 1,
          autoIdPrefix: 'portal'
        },{
          colSpan: 1,
          rowSpan: 1,
          autoIdPrefix: 'portal'
        },{
          colSpan: 1,
          rowSpan: 1,
          autoIdPrefix: 'portal'
        },{
          colSpan: 1,
          rowSpan: 1,
          autoIdPrefix: 'portal'
        },{
          colSpan: 1,
          rowSpan: 1,
          autoIdPrefix: 'portal'
        },{
          colSpan: 1,
          rowSpan: 1,
          autoIdPrefix: 'portal'
        },{
          colSpan: 1,
          rowSpan: 1,
          autoIdPrefix: 'portal'
        }];
      }
    }
  };

  /**
   * 更改界面布局
   */
  function changeLayout(no) {
    window.windowCfg = {
      cls: 'work-space',
      autoIdPrefix: 'portal',
      style: {
        'overflow-y': 'auto',
        'overflow-x': 'hidden'
      },
      height: 'auto',
      items: [
      ],
      location: true,
      renderTo: '#desktop'
    };
    
    layout[no].handler(windowCfg);
    window.ctnInfo.ctn && ctnInfo.ctn.destroy();
    window.ctnInfo.ctn = new YH.Container(windowCfg);

    ctnInfo.panels = [];
  }

	function listAllPorts() {
	  var url = contextPath + "/yh/core/funcs/portal/act/YHPortalAct/listAllPorts.act";
	  $.ajax({
	    type: "GET",
	    dataType: "text",
	    url: url,
	    success: function(text){
	      var json = YH.parseJson(text);
	      if (json.rtState == '0') {
	        var data = json.rtData;
	        $.each(data.records, function(i, e) {
	          if (e.file) {
	            var sawdow = $('<div class="port-sawdow"></div>');
	            sawdow.attr('id', 'sawdow' + e.id);
	            $('#config').append(sawdow);

              addTrayBtn(e.file, e.id, sawdow);
	            for (var j in ctnInfo.panels) {
	          	  if (e.id == ctnInfo.panels[j].id) {
		          	  sawdow.addClass('port-sawdow-selected');
	                var btn = $('#btn' + e.id);
	                btn.draggable('disable');
	                btn.parent().addClass('port-sawdow-selected');
		          	}
		          }
	          }
	        });
	      }
	    }
	  });
	}

/*
  //嵌套布局使用
	function multiLayouts() {
		var layouts = [{
		  name: '自由布局',
		  cfg: {
		    width: 300,
		    height: 200,
		    layout: 'freelayout',
		    layoutCfg: layoutCfg
		  },
		  file: 'freelayout.js'
	  }, {
      name: '二列布局',
      cfg: {
        width: 300,
        height: 200,
        layout: 'columnlayout',
        layoutCfg: layoutCfg,
        items: [{
          columnWidth: 0.5
        }, {
          columnWidth: 0.5
        }]
      },
      file: 'columnlayout.js'
    }];
	  $.each(layouts, function(i, e) {
	    var sawdow = $('<div style="float:left;"></div>');
	    sawdow.attr('id', 'sawdow' + e.id);
	    sawdow.css({
	      'height': '30px',
	      'width': '150px',
	      'border': '1px solid #606275'
	    });
	    sawdow.html('<span style="position:absolute;">' + e.name + '</span>');
	    $('#selLayouts').append(sawdow);

	     var btn = $('<div></div>');
	      btn.css({
	        'height': '30px',
	        'width': '100px',
	        'cursor': 'pointer',
	        'border': '1px solid #606275'
	      });
	      btn.addClass('portlet-source');
	      sawdow.append(btn);
	      btn.draggable({
	        revert: true
	      });
	      btn.html(e.name);
	      btn.data('layout', e.file);
		});
  }
	multiLayouts();
	*/
	/**
	 * 
	 */
	function addTrayBtn(file, id, sawdow) {
	  var btn = $('<div></div>');
    btn.addClass('portlet-source');

    //使用helper解决overflow:auto拖不出来的问题
    btn.draggable({
      revert: true,
      appendTo: 'body',
      helper: 'clone',
      containment: 'body'
    });
    //btn.attr('src', '<%= contextPath%>/core/frame/webos/styles/icons/comm.png');
    btn.attr('id', 'btn' + id)
    btn.data('panel', file);
    var span = $('<span></span>');
    span.html(file.replace('.js', ''));
    
    sawdow.append(btn.append(span));
  }

	if (designType) {
	  creator.loader.param.id = portalId;
		//设置回调函数
	  creator.units.portal.callback = function(w) {
		  w.designLayout();
		  $.each(ctnInfo.panels, function(i, e) {
			  YH.Event.add(e, 'bind', 'destroy', 'before', function(e, panel) {
          var btn = $('#btn' + panel.getId());
          btn.draggable('enable');
          
          btn.parent().removeClass('port-sawdow-selected');
          var index = $.inArray(panel, ctnInfo.panels);
          if (index >= 0) {
            ctnInfo.panels.splice(index, 1);
          }
        });
		  });
		  listAllPorts();
		};
	  creator.initialize(['portal', 'background']);
  }
	else {
	  listAllPorts();
		changeLayout(0);
  }
});



function addLayout(file, id, parentCmp) {
  $.post("<%= contextPath%>/yh/core/funcs/portal/act/YHPortalAct/viewLayout.act", {file:file}, function(d) {
    var cfg = YH.parseJson(d);
    parentCmp.addItems(cfg);
  }, 'text');
}

/**
 * 增加panel到桌面 */
function addPanel(file, id, parentCmp, opts) {
  $.post("<%= contextPath%>/yh/core/funcs/portal/act/YHPortalAct/viewPort.act", {
    file: file,
    id : id
  }, function(d) {
    var cfg = YH.parseJson(d);
    $.extend(cfg, opts);
    cfg.id = id;
    cfg.file = file;
    cfg.listeners = {
      'destroy': {
        before: function(e, panel) {
          var btn = $('#btn' + panel.getId());
          btn.draggable('enable');
          btn.parent().removeClass('port-sawdow-selected');
          var index = $.inArray(panel, ctnInfo.panels);
          if (index >= 0) {
            ctnInfo.panels.splice(index, 1);
          }
        }
      }
    };
    var tmp = new YH.Panel(cfg);
    tmp.addClass("port");
    ctnInfo.panels.push(tmp);
    parentCmp.addItems(tmp);
  }, 'text');
}

/**
 * 保存桌面模块位置
 */
function savePortal() {
  var url = contextPath + "/yh/core/funcs/portal/act/YHPortalAct/updateUserPortal.act";
  
  //规格化布局对象的模板
  var t = {
    id: null,
    cls: null,
    xtype: /container/,
    layout: null,
    layoutCfg: {
      cols: null,
      sortConnect: null,
      cellpadding: null,
      cellspacing: null,
      droppable: null,
      colsWidth: null,
      sortable: null
    },
    colSpan: null,
    rowSpan: null,
    draggable: '*',
    left: null,
    top: null,
    width: null,
    height: null,
    columnWidth: null,
    items: []
  };

  //保存布局前,首先对布局器的容器排序
  ctnInfo.ctn.sortItems(true);
  //把ctnInfo.ctn对象中的布局信息提取出来再转化成String
  var portalStr = YH.jsonToString(YH.formatJson(t, ctnInfo.ctn));

  $.ajax({
    type: "POST",
    dataType: "text",
    data: {
      portal: portalStr,
      name: portalName,
      id: portalId
    },
    url: url,
    success: function(text){
      var data = YH.parseJson(text);
      if (data.rtState == '0') {
        portalId = data.rtData.id;
        savePorts();
      }
      else {
        alert(data.rtMsrg || "保存未成功!");
      }
    }
  });
}

function savePorts() {
  var params = {};
  ctnInfo.ctn.sortItems(true);
  $.each(ctnInfo.panels, function(k, v) {
    var id = v.getId();
    var w = v.getWidth();
    var h = v.getHeight();
    var x = v.getPosX();
    var y = v.getPosY();
    var sort = v.sort || 0; 
    var pid = v.parentCmp && v.parentCmp.id;
    params[id] = [pid, sort, w, h, x, y];
  });

  params.id = portalId;
  
  var url = contextPath + "/yh/core/funcs/portal/act/YHPortalAct/updatePortalPorts.act";
  $.ajax({
    type: "POST",
    dataType: "text",
    data: params,
    url: url,
    success: function(text){
	    var data = YH.parseJson(text);
	    if (data.rtState == '0') {
	      alert("保存成功!");
	    }
	    else {
        alert(data.rtMsrg || "保存未成功!");
      }
    }
  });
}

</script>
<style>

.column-droppable,
.grid-droppable,
.free-droppable {
  background: url(../styles/style1/cmp/images/window_1/left-right-mc.png)
}

td.ui-selecting { 
  background: #FECA40; 
}


.jq-gridlayout td {
  border: 1px dashed white;
}

td.ui-selected {
  background: #F39814; 
  color: white; 
}
</style>
</head>
<body>
  <div id="desktop" class="container">
	  <div class="north">
	    <div class="background">
	      <div class="center">
				  <div class="left">
				    <div class="ml">
				      <div class="mr">
				        <div class="mc">
				          <div class="layout-sel">
		                <label for="radio0" class="layout-2">
		                </label>
		                <input type="radio" id="radio0" name="layoutSel" value="0">
		              </div>
		              <div class="layout-sel">
		                <label for="radio1" class="layout-3">
		                </label>
		                <input type="radio" name="layoutSel" id="radio1" value="1">
		              </div>
		              <div class="layout-sel">
		                <label for="radio2" class="layout-free">
		                </label>
		                <input type="radio" name="layoutSel" id="radio2" value="2">
		              </div>
		              <div class="layout-sel">
		                <label for="radio3" class="layout-grid">
		                </label>
		                <input type="radio" name="layoutSel" id="radio3" value="3">
		              </div>
				        </div>
				      </div>
				    </div>
				  </div>
				  <div class="right">
		        <div class="ml">
		          <div class="mr">
		            <div class="mc">
							    <div id="config">
								  </div>
		            </div>
		          </div>
		        </div>
				  </div>
				  <div class="btns">
				    <button class="close" onclick="confirm('确定关闭门户设计页面吗?') && window.close()"></button>
				    <button class="save" onclick="savePortal()"></button>
				  </div>
				</div>
				<div class="bottom">
				</div>
		  </div>
	  </div>
  </div>
  <div id="assist" class="assist">
    <div class="assist-tr"></div>
    <div class="assist-br"></div>
  </div>
</body>
</html>