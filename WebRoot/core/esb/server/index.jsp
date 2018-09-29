<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.core.global.YHRegistProps" %>
<%@ page import="yh.core.util.auth.YHRegistUtility" %>
<%
String sessionToken = (String)session.getAttribute("sessionToken");
YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
String userName = "";
if (person != null) {
  userName = person.getUserName();
}
int remainDays = YHRegistUtility.remainDays();
%>
<html>
<head>
<title><%=StaticData.SOFTTITLE_SHORT%>数据交换平台</title>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/index1.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/extResources/css/jq-yhtheme.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.droppable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.resizable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.selectable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
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
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.borderlayout.js"></script>
<script type="text/javascript" src="js/index.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
  <div class="ui-layout-north" id="northContainer" style="min-width:700px;">
	  <div class="banner_bg">
		  <div class="" style="float:left;">
		    <img id="logoImg" src="style/images/logo.jpg"/>
		  </div>
	  </div>
  </div>
  <div class="ui-layout-south" id="southContainer">
  <table cellspacing="0" cellpadding="0" border="0" width="100%" class="status-content">
  <tr>
    <td width="209" align="center" class="statusbar-usercount" id="usercount">
    </td>
    <td>
      <div id="status" style="text-align:center;line-height:21px;height:21px;overflow:hidden;">
        <div id="statusContent" style="position:relative;top:0px;"><%=StaticData.SOFTCOMPANY_SHORTNAME%></div>
      </div>
    </td>
    <%if (!YHRegistUtility.hasRegisted()) { %>
    <!-- 
    <td width="350">
      <font color="red"> 本软件尚未注册, 免费试用剩余&nbsp;<%=remainDays %>&nbsp;天</font>&nbsp;&nbsp;
      <a href="javascript:void(0)" style="color:blue;" onclick="dispParts('<%=contextPath %>/core/esb/server/sysinfo/index.jsp');return false;">注册</a>
    </td>
     -->
    <%} %>
  </tr>
</table>
  </div>
  <div class="ui-layout-west" id="westContainer">
    <div class="leftmenu-north-lv1 ui-layout-north">
      <div class="leftmenu-north-lv1-menu" id="menuExplorer">
      </div>
      <div style="position:absolute;left:28px;top:0px;">
         <span style="line-height: 35px; color: white;">欢迎&nbsp;<%=userName %></span>
       </div>
      <a href="javascript:void();" class="left-callLeft" onclick="collapseLeft();return false;"></a>
    </div>
    <div id=leftPanel class="ui-layout-center">
      <div id="tabpanelLv1" class="ui-layout-center leftContent">
        <div id="navigation">
          <div class="leftmenu-north-lv2" style="height: 10px;">
          </div>
          <div class="leftmenu-body" style="top: 12px;">
            <div id="menu"></div>
          </div>
        </div>
      </div>
      <div class="ui-layout-west leftmenu-west">
      </div>
      <div class="ui-layout-east leftmenu-east">
      </div>
      <div class="ui-layout-south leftmenu-south">
      </div>
    </div>
  </div>
  <div class="ui-layout-center" id="mainContainer">
  <div id="rightHeaderBar" class="ui-layout-north right-funcsbar">
       <div class="right-callright">
         <a id="callRight" href="javascript:void(0)" onclick="collapseLeft();return false;"></a>
       </div>
       
       <div class="right-logout">
         <a href="javascript:doLogoutMsg()" onclick=""></a>
       </div>
       <!-- 
       <div class="right-desktop" style="right: 120px;">
         <a   href="javascript:void(0)" onclick="dispDesk();"></a>
       </div>
        -->
       <div class="rightbar-up">
         <div style="cursor:pointer;"><img onclick="collapseUp()" id="rightBarUp" src="<%=imgPath%>/mainframe/call_up.jpg"/></div>
       </div>
       
       <div class="right-timeinfo" style="right: 100px;">
         <div style="float:left;"><img id="" style="" src="<%=imgPath%>/mainframe/clock.jpg"/></div>
         <div style="float:left;margin:8px 0px;">&nbsp;<span id="date"></span><span id="localtime"></span>&nbsp;</div>
         <div style="float:left;"><img id="" style="" src="<%=imgPath%>/mainframe/header_sp.jpg"/></div>
       </div>
     </div>
   <div class="ui-layout-center">
     <iframe id="main-body-desktop" name="main-body-desktop" src="about:blank" frameborder="0" style="width:100%;height:100%;position:absolute;left: 0; top: 0;">
     </iframe>
     <iframe src="<%=contextPath %>/core/esb/server/taskstatus/index.jsp" id="main-body-parts" name="main-body-parts" frameborder="0" style="border:none;display:;width:100%;height:100%;position:absolute;">
     </iframe>
   </div>
  </div>
</body>
</html>