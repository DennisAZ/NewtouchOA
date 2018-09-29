<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String webosCssPath = "";
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int seq=person.getSeqId();
  int reflash=(seq%10)*60+300+(seq%60);
%>
<html>
<head>
<meta http-equiv="refresh"content="10;url=location.href">
<title></title>

<link rel="stylesheet" type="text/css" href="styles/style1/css/index.css"/>
<link rel="stylesheet" type="text/css" href="styles/style1/cmp/css/cmp-all.css" />
<link rel="stylesheet" type="text/css" href="styles/style1/cmp/css/customer.css" />
<style>
  li {
    line-height: 20px;
  }
  
  .jq-Container-tabheader a {
    font-size: 12px;
  }
  
</style>
<style>
</style>
</head>
<body>
  <div id="desktop" class="container">
	  <div class="north">
	    <div class="background">
		    <div class="center">
			    <div class="right">
			      <div class="user-info">
		          <div class="user-info-top">
		            <div>
		              <img id="avatar"></img>
		            </div>
		            <div id="userInfo">
		            </div>
		            <div id="onlineStatus">
		            </div>
		            <div style="clear: both;"></div>
		          </div>
		          <div class="user-info-bottom">
		            <div id="userStatus">
		              <button></button>
		              <input type="text"/>
		            </div>
		            <div id="onlineAmount">
		              <button onclick="showOrg()"></button>
		            </div>
		            <div style="clear: both;"></div>
		          </div>
		        </div>
			    </div>
			    <div class="left">
			      <div class="logo">
			        
			      </div> 
			    </div>
		    </div>
	    <div class="bottom">
	    </div>
	    </div>
	  </div>
	  <div class="south" id="">
	    <div class="right">
	    </div>
	    <div class="left">
	    </div>
	  </div>
  </div>
  <div id="assist" class="assist">
    <div class="assist-tr"></div>
    <div class="assist-br"></div>
  </div>
  <div id="flashPlayer"></div>
  <script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.droppable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.resizable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.selectable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.touch-punch.js"></script>
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
<script type="text/javascript" src="<%=contextPath %>/core/frame/webos/js/common.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/frame/webos/js/index.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/frame/common/sms/sms.js"></script>
</body>
</html>