<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>国务院发展研究中心</title>
<link href="style/css/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/portal.js"></script>

<script type="text/JavaScript">
function getPF(height2){
  document.getElementById('mainframe').style.height = height2 + 'px';
}
</script>
</head>

<body style="overflow-x: hidden">
<div id="container">
   <div id="top"><!--头从这里开始-->
     <div id="Header">
	   <div id="logo"></div>
	   <div id="ben-r">
	     <div class="link-ess">
		   <img src="images/login-oa.gif" />
		   <a href="#">登入OA</a>&nbsp;&nbsp;
		   <img src="images/email-icon.gif" />
		   <a href="email.jsp">发送邮件</a>&nbsp;&nbsp;
		   <img src="images/bbs-icon.gif" />
		   <a href="#">内部论坛</a>
		 </div>
	   </div>
	 </div>
	 <div id="nvigation">
       <div id="nvi-box">
		   <ul class="left-ul">
		     <li><a href="#" target="_top">首页</a></li>
		     <li><a href="zxjj.jsp" target="mainframe">中心简介</a></li>
			 <li><a href="bgzl.jsp" target="mainframe">办公专栏</a></li>
		     <li><a href="modules/kygl/kygl-dybg-list.jsp" target="mainframe">科研管理</a></li>
			 <li><a href="modules/gjhz/gjhz-list.jsp" target="mainframe">国际合作</a></li>
		     <li><a href="xsqk.jsp" target="mainframe">学术期刊</a></li>
			 <li><a href="modules/djgz/djgz-list.jsp?typeId=1"  target="mainframe">党建工作</a></li>
			 <li><a href="modules/bmgz/bmgz-list.jsp" target="mainframe">保密工作</a></li>
			 <li><a href="modules/gwgl/gwgl-list.jsp" target="mainframe">公文管理</a></li>
		     <li><a href="modules/wxzl/wxzl-list.jsp?typeId=1" target="mainframe">文献资料</a></li>
			 <li><a href="#" target="mainframe">国研网</a></li>
		   </ul></div>
	 </div>
   </div><!--头从这里结束-->

   <div id="content">
	   <!--这里是框架了哦-->
    <div id="frame-c" sytle="scrolling:no">
    <iframe id="mainframe" src="index-content.jsp" name="mainframe"
                        frameborder="0" scrolling="no"
                        style="width: 100%;background-color: #ffffff;" 
                        marginheight="0" marginwidth="0" onload="autoHeight2(this.id)"></iframe>
		</div>
	 </div>
   <div id="footer">
         <div class="f-txt">版权所有：国务院发展研究中心信息中心 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
   </div>
</div>
</body>
</html>