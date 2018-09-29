<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>zTree</title>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../../js/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/zTree/jquery.ztree.all-3.1.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-patch.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/zTree/zTreeStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
<script type="text/javascript">
var setting = {
    view: {
      dblClickExpand: false,
      showLine: false
    },
    data: {
      simpleData: {
        enable: true
      }
    },
    callback: {
      onClick: onClick
    }
  };

  var zNodes =[
    { id:1, pId:0, name:"资产", open:true},
    { id:11, pId:1, name:"1001_库存现金", open:true},
    { id:12, pId:1, name:"1002_银行存款", open:true},
    { id:121, pId:12, name:"100201_招商银行金色支行"},
    { id:122, pId:12, name:"100202_工商银行XX支行"},
    { id:13, pId:1, name:"1012_其他货币资金", open:true},
    { id:131, pId:13, name:"101201_外埠存款"},
    { id:132, pId:13, name:"101202_银行本票存款"}
  ];

  function onClick(e,treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.expandNode(treeNode);
  }

  $(document).ready(function(){
    $("#tabs").tabbedDialog();
    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
  });
  </script>
</head>
<body>
	<div id="tabs">
  <ul>
    <li><a href="#treeDemo">资产</a></li>
    <li><a href="#tabs-2">负债</a></li>
    <li><a href="#tabs-3">共同</a></li>
  </ul>
  <div id="treeDemo"  class="ztree">
  </div>
  <div id="tabs-2">
  </div>
  <div id="tabs-3">
  </div>
  </div>
</body>
</html>