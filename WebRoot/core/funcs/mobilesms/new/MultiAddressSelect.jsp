<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>地址选择</title>
<base target="_self">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/orgselect.css">
<link rel="stylesheet" href = "<%=cssPath %>/menu_left.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/javascript" src="MultiAddressSelect.js"></script>
<script type="text/javascript">
var loginUserId = "<%=loginUser.getSeqId()%>";
</script>
</head>
<body onload="doInit()" style="padding-right:0px">
<div id="left">
</div>
<div id="right" class="list" style="display:none;">
	<table class="TableTop" width="100%">
	  <tr>
	    <td class="left">
	    </td>
	    <td class="center">
	      <span id="title" class="clickable">&nbsp;</span>
	    </td>
	    <td class="right">
	    </td>
	  </tr>
	</table>
	<div class="op">
	  <input type="button" class="BigButtonB" value="全部添加" onclick="selectedAll()" id="addAll">
	  <input type="button" class="BigButtonB" value="全部删除" onclick="disSelectedAll()" id="disAll">
	</div>
  <div id="deptsDiv" align="left">
  </div>
</div>
<div id="deptsDiv" align="center">
</div>
<div id="noGroup" style="display:none;">
  <table class="TableTop" width="100%">
    <tr>
      <td class="left">
      </td>
      <td class="center">
        &nbsp;
      </td>
      <td class="right">
      </td>
    </tr>
  </table>
  <table class="TableBlock no-top-border" align="center" width="100%"> 
  <tr> 
    <td class="TableData"> 
      该分组尚无记录
    </td> 
  </tr> 
</table>
</div>
<div style="text-align: center; padding-top: 10px; clear: both;">
  <input type=button class="SmallButtonW" value="确定" onclick="window.close()"/>
</div>
</body>
</html>