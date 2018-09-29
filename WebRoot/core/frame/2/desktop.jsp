<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Random"%>
<%@ include file="/core/inc/header.jsp" %>

<%
  //用户禁止查看桌面
  String userId = "";

  YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
  
  if (person != null){
    userId = person.getUserId();
    String viewFlag = person.getNotViewTable();
    
    if (viewFlag == null || "".equals(viewFlag.trim())){
      viewFlag = "0";
    }
    
    if ("1".equals(viewFlag.trim())){
      //转向一个空桌面页面
      response.sendRedirect(contextPath + "/core/ext/frame/portal/noneDesktop.jsp");
    }
  }
  int seq=person.getSeqId();
  int number = new Random().nextInt(10) + 1;
  int reflash=number*60+300+(seq%60);
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <!--  
    <meta http-equiv="refresh"content="<%=reflash%>;url=<%=contextPath %>/core/frame/2/desktop.jsp">
    -->
    <title>Custom Layouts and Containers - Portal Example</title>
    <!-- page specific -->
    <link rel="stylesheet" type="text/css" href="<%=cssPath %>/mytable.css" />
    <link rel="stylesheet" type="text/css" href="<%=contextPath %>/core/frame/webos/styles/style1/cmp/css/cmp-all.css" />
    <link rel="stylesheet" type="text/css" href="<%=cssPath%>/extResources/css/jq-yhtheme.css" />
    <script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.droppable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.resizable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.selectable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.yh.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.autolayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.fitlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.cardlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.freelayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.floatlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.columnlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.gridlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.module.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.container.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.panel.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.window.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.tip.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.button.js"></script>
<style>
  .jq-columnlayout table {
    height: auto;
  }
	.ui-sortable-placeholder { 
	  border: 1px dotted black;
	  background:#EEEEEE;
	  visibility: visible !important;
	}
  .ui-sortable-placeholder * { 
    visibility: hidden;
  }
  ul,ol {
    margin:0;
    padding:0;
  }
  .column {
    height: 100%;
  }
</style>
<script type="text/javascript">

function openUrl(node) {
  top.dispParts(node.url);  
}

var userId = '<%=userId%>';
var module = new Object();

function addModule( key, value){
  module[key] = value;
}

var idMap = {};

var gearTool = {
  id: 'gear',
  handler: function(e, target, panel){
    if (!window.gearWin) {
      window.gearWin = new YH.Window({
        'title' : '设置',
        'draggable': true,
        'width': 350,
        'height': 200,
        //'renderTo': 'body',
        'tbar': [{
          id: 'close',
          preventDefault: true,
          handler: function(e, target, win){
            win.hide();
	        }
        }],
        'content': jQuery('#setModules')
      });
    }

    if (pros.SCROLL != '1') {
      jQuery('#moduleScrollTr').hide();
    }
    else {
      var scl = jQuery.cookie(userId + '-lns-' + panel.id);

      if (scl != 0 && scl != 1) {
        jQuery('#moduleScroll').val(panel.param.scroll ? 1 : 0);
      }
      else {
        jQuery('#moduleScroll').val(scl);
      }
    }

    if (pros.LINES != '1') {
      jQuery('#moduleLinesTr').hide();
    }
    else {
	    jQuery('#moduleLines').val(jQuery.cookie(userId + '-lns-' + panel.id) || panel.param.lines);
    }
    
    if (pros.WIDTH != '1') {
      jQuery('#moduleWidthTr').hide();
    }
    else {
      jQuery('#moduleWidth').val(jQuery.cookie(userId + '-wid') * 100 || pros.LEFT_WIDTH);
    }

    gearWin.show();

    jQuery('#submitCookie').unbind('click');
    jQuery('#submitCookie').bind('click', function() {

      var width = jQuery('#moduleWidth').val();

      if (width < 100 && width > 0) {
	      columnCtn.items[0].setColumnWidth(width / 100);
	      columnCtn.items[1].setColumnWidth(1 - width / 100);
	      jQuery.cookie(userId + '-wid', width / 100, {expires: 30, path: '/'});
	      //jQuery(window).trigger('panelResize');
      }

      var scl = jQuery('#moduleScroll').val();
      if ((scl == 1) != panel.param.scroll) {
        jQuery.cookie(userId + '-scl-' + panel.id, scl, {expires: 30, path: '/'});
      }
      
      panel.param.scroll = (scl == 1);
      
      var lines = jQuery('#moduleLines').val();

      if (!isNaN(lines) && lines > 0) {
	      //设置cookie
	      if (lines != panel.param.lines) {
	        jQuery.cookie(userId + '-lns-' + panel.id, lines, {expires: 30, path: '/'});
	      }
	      panel.param.lines = jQuery('#moduleLines').val();
	      var lns = panel.param.lines;
      }
      window.location.reload();
      gearWin.hide();
    });
  }
};

function hideWin() {
  if (window.gearWin) {
    gearWin.hide();
  }
}

var toggleTool = {
  id: 'toggle'
};

var closeTool = {
  id:'close',
  preventDefault: true,
  handler: function(e, target, panel){
    if (confirm('确认不显示该模块么?')){
      panel.close();
      saveModules();
    }
  }
}

var panels = {};

jQuery(document).ready(function(){
  var url = contextPath + "/yh/core/funcs/setdescktop/setports/act/YHMytableAct/getDesktopPorts.act";

  jQuery.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    success: function(text){
		  var json = YH.parseJson(text);
		  if (json.rtState == '0') {
		    var data = json.rtData;
		    window.pros = json.rtData.properties;
		    var leftItems = [];
		    var rightItems = [];
		    var layoutCfg = {
          listeners: {
	          sortStop: function(e, ui) {
	            saveModules();
	          }
	        },
	        cellspacing: '8px',
	        sortConnect: true
	      };
		    //模块拖动
        if (pros.POS != '1') {
          layoutCfg.sortable = false;
        }
        
		    var leftWidth = jQuery.cookie(userId + '-wid') || (pros.LEFT_WIDTH || 50 ) / 100;
		
		    //模块是否有geartool
		    var isGear = pros.WIDTH | pros.LINES | pros.SCROLL;
		
		    //模块是否可以展开
		    var isExpand = pros.EXPAND == '1';
		    window.columnCtn = new YH.Container({
          renderTo: '#container',
          layout: 'columnlayout',
          layoutCfg: layoutCfg,
          items: [{
            id: 'leftContainer',
            columnWidth: leftWidth,
            items: []
          },{
            id: 'rightContainer',
            columnWidth: 1 - leftWidth,
            items: []
          }],
          width: '100%',
          height: '100%'
        });
		    jQuery.each(data.records, function(i, e) {
		      var tools = [];
		      var file = e.fileName;
		      var id = e.seqId;
		      var title = e.fileName.replace(/.js/, "");
		      
		      var el = jQuery('<div></div>');
		      el.attr('id', id);
		      el.data('seqId', e.seqId);
		       
		      idMap[title] = id;
		      
		      if (e.viewType == '1') {
		        tools.push(closeTool);
		      }
		
		      if (isGear){
		        tools.push(gearTool);
		      }
		      
		      if (isExpand){
		        tools.push(toggleTool);
		      }
		      //cookie中获取模块的属性
		      var lines = jQuery.cookie(userId + '-lns-' + id) || e.moduleLines;
		      var scl = jQuery.cookie(userId + '-scl-' + id);
		      
		      var scroll;
		      if (scl != 1 && scl != 0) {
		        scroll = !!(e.moduleScroll * 1);
		      }
		      else {
		        scroll = (scl == 1);
		      }
		      
		      try {
			      var ctn = new YH.Container({
			        height: "auto"
				    });
		        if (e.modulePos == 'l') {
		          YH.getCmp('leftContainer').addItems(ctn);
	          } else if (e.modulePos == 'r') {
	            YH.getCmp('rightContainer').addItems(ctn);
	          }
	          var param = {
              file: file,
              lines: lines,
              scroll: scroll
            };
            
		        //生成模块
		        $.post(contextPath + '/yh/core/funcs/setdescktop/setports/act/YHMytableAct/viewDesktopPortJquery.act', 
				        param, function(r) {
              var cfg = YH.parseJson(r);
              adaptClassic(cfg, id, tools, lines, param);
              panels[id] = new YH.Panel(cfg);
              ctn.addItems(panels[id]);
              if (cfg.param.scroll) {
                marquee(cfg.id);
              }
            }, "text");
		      } catch (e) {
		      }
		    });
		   }
     }
  });
});

/**
 * 使桌面模块的设置适应经典界面
 */
function adaptClassic(cfg, id, tools, lines, param) {
  function doMaxRecords(cfg, lines) {
    
    cfg.items && $.each(cfg.items, function(i, e) {
      if (e.xtype == 'grid' || e.xtype == 'imgbox') {
        e.maxRecords = lines;
      }
      else if (e.items) {
        doModuleHeight(e, lines);
      }
    });
  }
  
  cfg.id = id;
  cfg.cmpCls = "jq-panel";
  cfg.param = param;
  doMaxRecords(cfg, lines);

  $.each(cfg.tbar || [], function(i, e) {
    if (e.id == 'more') {
      tools.push(e);
      return;
    }
  });
  
  cfg.tbar = tools;
  cfg.width = "100%";
  cfg.height = lines * 20;
}


/**
 * 保存桌面模块位置
 */
function saveModules() {
  var leftModules = '';
  $.each($('#leftContainer .extend-container'), function(i, e) {
    leftModules += e.id + ',';
  });
  
  var rightModules = '';
  $.each($('#rightContainer .extend-container'), function(i, e) {
    rightModules += e.id + ',';
  });

  var url = contextPath + "/yh/core/funcs/setdescktop/setports/act/YHDesktopDefineAct/setDesktopPortal.act";
  $.ajax({
    type: "POST",
    data: {
      'tableLeft': leftModules,
      'tableRight': rightModules
    },
    dataType: "text",
    url: url
  });
}
function marquee(id) {
  var el = $('#' + id + " .jq-panel-content").children();
  el.parent().css({
    overflow: "hidden"
  });
  el.css({
    'position': 'static',
    overflow: "hidden"
  });
  el.wrapAll('<marquee border="0" height="100%" width="100%" onmouseout="this.start()" onmouseover="this.stop()" scrolldelay="100" scrollamount="2" behavior="scroll" direction="up"></marquee>');
}
</script>
</head>
<body id="container">
	
	<div id="setModules" style="display:none;height:200px;">
	  <fieldset>
	    <legend><b>模块选项</b></legend>
	    <table>
	      <tr id="moduleLinesTr">
	        <td>
	          显示行数:
	        </td>
	        <td>
	          <input type="text" id="moduleLines">
	        </td>
	      </tr>
	      <tr id="moduleScrollTr">
          <td>
            列表上下滚动显示:
          </td>
          <td>
          <select id="moduleScroll">
            <option value="0">否</option>
            <option value="1">是</option>
          </select>
          </td>
        </tr>
	    </table>
	  </fieldset>
	  <br>
	  <fieldset>
      <legend><b>全局选项</b></legend>
      <table>
        <tr id="moduleWidthTr">
          <td>
            左侧栏目宽度(%):
          </td>
          <td>
          <input type="text" id="moduleWidth">
          </td>
        </tr>
      </table>
    </fieldset>
    <div style="text-align:right;margin:10px 0px;">
	    <input type="button" value="确定" id="submitCookie">
	    <input type="button" value="取消" onclick="hideWin();">
    </div>
	</div>
</body>
</html>