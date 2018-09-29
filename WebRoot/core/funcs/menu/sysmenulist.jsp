<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.util.*,yh.core.funcs.menu.data.YHSysMenu,yh.core.funcs.menu.data.YHSysFunction" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示菜单主分类的信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript">
function doInit(){
  //iframeFun();
  //设置父页面iframe高度
  var iframe = parent.document.getElementById("iframeD");
  var h = $("list").getDimensions().height;
  if (isNaN(h)) {
    h = 200;
  }
  iframe.style.height = h + "px";
}
//function loadData(){
 // var url = "<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/listSysMenu.act";
 // location = url;
//}

function confirmDel() {
  if(confirm("确认删除！")) {
    return true;
  }else {
    return false;
  }
}

function delSysMenu(seqId, menuId) {
  var url = "<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/deleteSysMenu.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId + "&menuId=" + menuId);
  if (rtJson.rtState == "0") {
    window.location.reload();
    window.parent.parent.contentFrame.location = "<%=contextPath%>/core/funcs/menu/delete.jsp";
  }else {
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body onload="doInit()" style="margin:0px;padding:0px">
  <table id="list" cellpadding="3" width="100%" class="TableList no-border">
    <%
      ArrayList<YHSysMenu> menuList = (ArrayList<YHSysMenu>)request.getAttribute("menuList");
      for(int i = 0; i < menuList.size(); i++) {
        YHSysMenu menu = menuList.get(i);
    %> 
    <tr class="<%= (i % 2 == 0) ? "TableLine1" : "TableLine2" %>">
      <td><img style="width:18px;height:18px;" src="<%=contextPath %>/core/styles/imgs/menuIcon/<%=menu.getImage() %>"/></td>
      <td width="20px"><%=menu.getMenuId() %></td>
      <td width="75px"><%=menu.getMenuName() %></td>
      <td>
        <a href="<%=contextPath %>/core/funcs/menu/sysmenuinput.jsp?seqId=<%=menu.getSeqId() %>"  target="contentFrame">编辑</a> 
      </td>
      <td>
        <a href="<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/listSysFunction.act?menuId=<%=menu.getMenuId() %>"  target="contentFrame">下一级</a>
      </td>
      <td>
        <a href="javascript:delSysMenu('<%=menu.getSeqId() %>', '<%=menu.getMenuId() %>');" onclick="return confirmDel()">删除</a>
      </td>
    </tr>
    <%
      }
    %>
  </table>
</body>
</html>