<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>收藏夹</title>
<head>
<script type="text/javascript">
Ext.onReady(function (){
  var loader = new Ext.tree.TreeLoader({
    preloadChildren: true,
    clearOnLoad: false,
    dataUrl: '<%=contextPath%>/yh/core/funcs/setdescktop/fav/act/YHFavAct/list.act'
  });

  loader.on("load",function(t,n,c){
  });
  
  var root = new Ext.tree.AsyncTreeNode({
    expanded: true,
    iconCls: 'icon-url'
  });
  
  var favTree = new Ext.tree.TreePanel({
    id: 'favTree',
    renderTo: 'fav-div',
    border: false,
    lines: false,
    rootVisible: false,
    root: root,
    loader: loader
  });

  favTree.on("click",function(node,e){
    if (node.attributes.openType == '1'){
      window.open(node.attributes.url);
    }
    else {
      dispParts(node.attributes.url);
    }
  });
});
</script>
</head>
<body>
  <div id="fav-div"></div>
  <a style="position:relative;left:150px" href="javascript:dispParts('<%=contextPath %>/core/funcs/setdescktop/control.jsp?path=fav');">设置</a>
</body>
</html>