<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.util.*, yh.core.funcs.menu.data.*, yh.core.funcs.menu.logic.*" %>
<%@ page import="yh.core.data.YHRequestDbConn" %>
<%@ page import="yh.core.global.YHBeanKeys" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>子菜单项设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<%
  String menuId = request.getParameter("menuId");
  String image = (String)request.getAttribute("image");
  String menuName = (String)request.getAttribute("menuName");
  String menuFlag = "2";
%>
<script>

function delSysFunction(menuId) {
  if(!confirm("确认删除！")) {
    return ;
  }
  
  var url = "<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/deleteSysFunction.act";
  var rtJson = getJsonRs(url, "menuId=" + menuId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body topmargin="5">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="big"><img src="<%=imgPath%>/system.gif" align="absmiddle"></img>
  <span class="big">&nbsp;子菜单项设置</span>
    </td>
  </tr>
</table>
  <br/>
  <table cellscpacing="1" cellpadding="3" width="300" align = "center" class="TableList">
    <tr class="TableHeader">
      <th colspan="3" align="left">
        <img src="<%=contextPath %>/core/styles/imgs/menuIcon/<%=image %>"/>&nbsp;<%=menuName %>
      </th>
    </tr>
    <tr class="TableControl">
      <td colspan="3" align="center">
        <a class="ToolBtn2" href="<%=contextPath %>/core/funcs/menu/sysfunctioninput.jsp?menuId=<%=menuId %>&menuName=<%=menuName %>&menuFlag=<%=menuFlag %>"" target="contentFrame">
          <span><img align="absmiddle" src="<%=contextPath %>/core/styles/imgs/green_plus.gif"></img>添加子菜单项</span>
        </a>   
      </td>
    </tr>
  <%
    ArrayList<YHSysFunction> functionList = (ArrayList<YHSysFunction>)request.getAttribute("functionList");
    for(int i = 0; i < functionList.size(); i++) {
      YHSysFunction function = functionList.get(i);
  %>
    <tr class="<%= ((i % 2 == 0) ? "TableLine1" : "TableLine2") %>">
      <% 
        if(function.getMenuId().length() == 4){
          
          String imageAddress = null;
          String funcCode = function.getFuncCode();
          if(funcCode == null){
            funcCode = "";
          }
          imageAddress = contextPath + "/core/funcs/display/img/org.gif";
      %>
      <td><img src="<%= imageAddress%>"/>
        <%=function.getMenuId().substring(2) %>
        <%=function.getFuncName() %>
      </td>
      <td>
        <a href="<%=contextPath %>/core/funcs/menu/sysfunctioninput.jsp?menuIdStr=<%=function.getMenuId() %>&seqId=<%=function.getSeqId() %>&menuId=<%=menuId %>&menuName=<%=menuName %>" target="contentFrame">编辑</a>
      </td>
      <td>
        <a href="javascript:delSysFunction('<%=function.getMenuId() %>');">删除</a>
      </td>
      <% 
        }else if(function.getMenuId().length() == 6){ 
         String funcName = "";
      %>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;
        <img src="<%=contextPath %>/core/funcs/display/img/person.gif"/>
        <%=function.getMenuId().substring(4) %>
        <%=function.getFuncName() %>
        <%
          YHSysMenuLogic menuLogic = new YHSysMenuLogic();
          funcName = menuLogic.getSysfunctionByMenuId(((YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR)).getSysDbConn(), function.getMenuId());
        %>
      <td>
        <a href="<%=contextPath %>/core/funcs/menu/sysfunctioninput.jsp?menuIdStr=<%=function.getMenuId() %>&seqId=<%=function.getSeqId() %>&menuId=<%=menuId %>&menuName=<%=funcName %>" target="contentFrame">编辑</a>
      </td>
      <td>
        <a href="javascript:delSysFunction('<%=function.getMenuId() %>');">删除</a>
      </td>
      <%
        }
      %>
    </tr>
  <%
    }
  %>
  </table>
  <br/>
<div align="center">
  <input type="button" value="返回" class="BigButton" onclick="location='<%=contextPath %>/core/funcs/menu/blank.jsp'">
</div>
  
</body>
</html>