<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>地址簿</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
</head>
<body topmargin="5">
<ul>
   <li><a href="javascript:;" onclick="clickMenu('1');" target="address_main" title="联系人分组" id="link_1" class="active"><span>联系人分组</span></a></li>

   <div id="module_1" class="moduleContainer" style="display:;"><table class="TableBlock trHover" width="100%" align="center">
   <tr class="TableData" align="center">
     <td nowrap onclick="parent.address_main.location='address?GROUP_ID=0&PUBLIC_FLAG=0'" style="cursor:pointer;">默认</td>
   </tr><tr class="TableData" align="center"><td nowrap onclick="parent.address_main.location='address?GROUP_ID=1&PUBLIC_FLAG=0'" style="cursor:pointer;">客户</td></tr><tr class="TableData" align="center"><td nowrap onclick="parent.address_main.location='address?GROUP_ID=2&PUBLIC_FLAG=0'" style="cursor:pointer;">朋友</td></tr><tr class="TableData" align="center"><td nowrap onclick="parent.address_main.location='address?GROUP_ID=9&PUBLIC_FLAG=0'" style="cursor:pointer;">啊</td></tr><tr class="TableData" align="center"><td nowrap onclick="parent.address_main.location='address?GROUP_ID=3&PUBLIC_FLAG=1'" style="cursor:pointer;">客户 (公共)</td></tr><tr class="TableData" align="center"><td nowrap onclick="parent.address_main.location='address?GROUP_ID=5&PUBLIC_FLAG=1'" style="cursor:pointer;">JAVA (公共)</td></tr></table></div>
   <li><a href="javascript:;" onclick="clickMenu('2');" target="address_main" title="索引(按姓氏)" id="link_2"><span>索引(按姓氏)</span></a></li>

   <div id="module_2" class="moduleContainer" style="display:none;"><table class="TableBlock trHover" width="100%" align="center"><tr class="TableData" align="left"><td onclick="parent.address_main.location='address/idx_search.php?ID_STR=2,&TABLE_STR=<b>C</b>(1) - C'" style="cursor:pointer;"><b>C</b>(1) - C</td></tr><tr class="TableData" align="left"><td onclick="parent.address_main.location='address/idx_search.php?ID_STR=4,&TABLE_STR=<b>L</b>(1) - 刘'" style="cursor:pointer;"><b>L</b>(1) - 刘</td></tr><tr class="TableData" align="left"><td onclick="parent.address_main.location='address/idx_search.php?ID_STR=3,&TABLE_STR=<b>S</b>(1) - 士'" style="cursor:pointer;"><b>S</b>(1) - 士</td></tr></table></div>
   <li><a href="address/search.php" onclick="" target="address_main" title="查找(关键字)" id="link_3"><span>查找(关键字)</span></a></li>
   <li><a href="group" onclick="" target="address_main" title="管理分组" id="link_4"><span>管理分组</span></a></li>
</ul>


</body>
</html>