<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门树</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/javascript">
  var tree =  null;
  function doInit(){ 
    tree = new DTree({bindToContainerId:'deplist'
      , requestUrl:'<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/getDeptTree.act?id='
      , isOnceLoad:false
      , treeStructure:{isNoTree:false}
      , linkPara:{clickFunc:getChild}
    });
   tree.show(); 
  }

  function getChild(id) {
    parent.user.location = "<%=contextPath%>/core/funcs/sms/user.jsp?id="+ id;
  }
</script>
</head>
<body onload="doInit()">
<table cellscpacing="1" cellpadding="3" width="250">
  <tr class="TableLine1">
    <td>按部门选择</td>   
  </tr>
  <tr class="TableLine2">
    <td>
      <div id="deplist"></div>
    </td>
  </tr>
</table>
</body>
</html>