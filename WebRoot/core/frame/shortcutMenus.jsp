<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>收藏夹</title>
<head>
<script type="text/javascript">
Ext.onReady(function (){
  var stLoader = new Ext.tree.TreeLoader({
    preloadChildren: true,
    clearOnLoad: false,
    dataUrl: '<%=contextPath %>/yh/core/funcs/setdescktop/shortcut/act/YHShortcutAct/listShortCut.act'
  });

  var stRoot = new Ext.tree.AsyncTreeNode({
    expanded: true,
    iconCls: 'icon-url'
  });
  
  var shortCut = new Ext.tree.TreePanel({
    id: 'shortcutTree',
    renderTo: 'shortcut-div',
    border: false,
    lines: false,
    rootVisible: false,
    root: stRoot,
    loader: stLoader
  });

  shortCut.on("click",function(node,e){
    dispParts(node.attributes.url);
  });

});
</script>
</head>
<body>
  <div id="shortcut-div"></div>
  <a style="position:relative;left:150px" href="javascript:dispParts('<%=contextPath %>/core/funcs/setdescktop/control.jsp?path=shortcut');">设置</a>
</body>
</html>