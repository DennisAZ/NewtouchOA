<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Custom Layouts and Containers - Portal Example</title>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/extResources/css/jq-yhtheme.css" />
<style>
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
  }
  body {
    overflow-x:hidden;
  }
  ul {
    margin:0;
    padding:0;
  }
  li {
    line-height: 20px;
  }
  
  .jq-Container-tabheader a {
    font-size: 12px;
  }
  .module {
    border: none;
  }
  
  .yh-imgbox {
    position:relative;
    height: 100px;
    overflow:hidden;
  }
  .yh-imgbox-container {
    position:absolute;
    height: 100px;
    overflow:hidden;
    width:100%;
  }
  
  .yh-imgbox-imgs {
    position:absolute;
  }
  
  .yh-imgbox-texts {
    position:absolute;
    top:0px;
    left:180px;
  }
  
  .yh-imgbox-imgs img {
    height: 100px;
    width: 150px;
    display:block;
  }
  
  .yh-imgbox-texts span {
    cursor:pointer;
    display:block;
    line-height: 20px;
  }
  
  #container {
    width:100%;
    height:100%;
    background-color:white;
  }
  
  #config {
    display:none;
    position:absolute;
    top: -100px;
    height: 100px;
    width: 100%;
    background-color:#EbEbEb;
  }
  
</style>
<!-- resizable的样式 -->
<style>
  .ui-resizable { 
    position: relative;
  }
  .ui-resizable-handle {
    position: absolute;
    font-size: 0.1px;
    z-index: 99999; 
    display: block;
  }
  .ui-resizable-disabled .ui-resizable-handle, .ui-resizable-autohide .ui-resizable-handle { 
    display: none; 
  }
  .ui-resizable-n { 
    cursor: n-resize;
    height: 7px; 
    width: 100%; 
    top: -5px; 
    left: 0; 
  }
  .ui-resizable-s {
    cursor: s-resize; 
    height: 7px; 
    width: 100%; 
    bottom: -5px; 
    left: 0; 
  }
  .ui-resizable-e { 
    cursor: e-resize; 
    width: 7px; 
    right: -5px; 
    top: 0; 
    height: 100%; 
  }
  .ui-resizable-w { 
    cursor: w-resize; 
    width: 7px; 
    left: -5px; 
    top: 0; 
    height: 100%;
  }
  .ui-resizable-se { 
    cursor: se-resize; 
    width: 12px; 
    height: 12px; 
    right: 1px; 
    bottom: 1px;
  }
  .ui-resizable-sw { 
    cursor: sw-resize; 
    width: 9px; 
    height: 9px; 
    left: -5px; 
    bottom: -5px; 
  }
  .ui-resizable-nw { 
    cursor: nw-resize; 
    width: 9px; 
    height: 9px; 
    left: -5px; 
    top: -5px; 
  }
  .ui-resizable-ne { 
    cursor: ne-resize; 
    width: 9px; 
    height: 9px; 
    right: -5px; 
    top: -5px;
  }
  table {
    border: 1px solid #D3D3D3;
  }
  td.ui-selecting { background: #FECA40; }
  td.ui-selected { background: #F39814; color: white; }
  #selectable { list-style-type: none; margin: 0; padding: 0; }
  td { boder:1px soild black; width:80px;height:80px;  border: 1px solid #D3D3D3;}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.droppable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.resizable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.selectable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.yh.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.tableoperation.js"></script>
<script type="text/javascript">
$(document).ready(function() {
  window.tableOp = new YH.TableOperation('table');
});

</script>
</head>
<body>
  <table class="resizable" width="100%">
    <tr>
	    <td class="ui-state-default">
	    </td>
	    <td class="ui-state-default">
	    </td>
	    <td class="ui-state-default">
	    </td>
    </tr>
    <tr>
	    <td class="ui-state-default">
	    </td>
	    <td class="ui-state-default">
	    </td>
	    <td class="ui-state-default">
	    </td>
	  </tr>
	  <tr>
	    <td class="ui-state-default">
	    </td>
	    <td class="ui-state-default">
	    </td>
	    <td class="ui-state-default">
	    </td>
	  </tr>
	  <tr>
	    <td class="ui-state-default">
	    </td>
	    <td class="ui-state-default">
	    </td>
	    <td class="ui-state-default">
	    </td>
	  </tr>
  </table>
</body>
</html>