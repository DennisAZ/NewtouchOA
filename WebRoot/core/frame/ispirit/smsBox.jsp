<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>事务提醒</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=contextPath%>/core/frame/ispirit/style/smsBox.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath%>/core/js/jquery/yh/core/js/jquery/jquery.min1.6.2.js"></script>

<script type="text/javascript" src="<%=contextPath%>/core/frame/ispirit/js/smsBox.js"></script>

<script type="text/javascript">
var bSmsPriv=true;
if(typeof(window.external.OA_SMS) != 'undefined')
{
   window.external.OA_SMS(445, 412, "SET_SIZE");
   window.external.OA_SMS(document.title, "", "NAV_TITLE");
}
</script>
<body scroll="no">
<div class="center">
   <div id="new_noc_panel">
    <div id="new_noc_title">
        <span class="noc_iterm_num">共<span style="color:red" id='number'> 0 </span>条提醒</span>
            <span class="noc_iterm_history"><a id="check_remind_histroy" href="javascript:;" hidefocus="hidefocus">查看历史记录</a></span>
    </div> 
    <div id="nocbox_tips"></div>
    <div id="new_noc_list"></div>
    <div class="button">
         <a id="ViewAllNoc" class="btn-white-big" href="javascript:;" hidefocus="hidefocus">全部已阅</a>
         <a id="ViewDetail" class="btn-white-big" href="javascript:;" hidefocus="hidefocus">全部详情</a>
         <a id="CloseBtn" class="btn-white-big" href="javascript:;" hidefocus="hidefocus">关闭</a>
      </div>          
   </div>
</div>
</body>
</html>