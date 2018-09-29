<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);

%>
<html>
<head>
<title>消息盒子</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="./style/smsBox1.css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="./js/smsBox1.js"></script>

<script type="text/javascript">
var bSmsPriv = true;
var loginUser = {seqId:<%=person.getSeqId()%>, userName:"<%=person.getUserName()%>"};
try {
	if(window.external && typeof(window.external.OA_SMS) != 'undefined') {
	  window.external.OA_SMS(485, 412, "SET_SIZE");
	  window.external.OA_SMS(document.title, "", "NAV_TITLE");
	}
} catch (e) {
  
} finally {
  
}
function markRead(seqId){
  jQuery.ajax({
    async: false,
    type: 'POST',
    url: contextPath + '/yh/core/funcs/sms/act/YHSmsAct/resetFlag.act?',
    data: 'seqId= ' + seqId,
    success: function(data){
     // alert(1);
    }
 });
  
}
</script>
<title>事务提醒</title>
</head>
<body>
<div class="center">
 <div class="center-left">
 <div class="center-group">
 <a href="javascript:;" id="group_by_name" class="active" hidefocus="hidefocus">按姓名分组</a>
 <a href="javascript:;" id="group_by_type" hidefocus="hidefocus">按类型分组</a>
 </div>
 <div id="smsbox_scroll_up"></div>
 <div id="smsbox_list">
 <div id="smsbox_list_container" class="list-container"></div>
 </div>
 <div id="smsbox_scroll_down"></div>
 <div id="smsbox_op_all">
 <a href="javascript:;" id="smsbox_read_all" hidefocus="hidefocus">全部已阅</a>
 <a href="javascript:;" id="smsbox_detail_all" hidefocus="hidefocus">全部详情</a>
 <a href="javascript:;" id="smsbox_delete_all" hidefocus="hidefocus">全部删除</a>
 </div>
 </div>
 <div class="center-right">
 <div class="center-toolbar">
 <a href="javascript:;" id="smsbox_toolbar_read" hidefocus="hidefocus" title="已阅以下提醒">已阅</a>
 <a href="javascript:;" id="smsbox_toolbar_detail" hidefocus="hidefocus" title="查看以下提醒详情">详情</a>
 <a href="javascript:;" id="smsbox_toolbar_delete" hidefocus="hidefocus" title="删除以下提醒">删除</a>
 </div>
 <div id="smsbox_msg_container" class="center-chat"></div>
 <div class="center-reply" id="reply">
 <textarea id="smsbox_textarea"></textarea>
 <a id="smsbox_send_msg" href="javascript:;" hidefocus="hidefocus">发送</a>
 </div>
 </div>
 <div id="smsbox_tips" class="center-tips"></div>
 <div id="no_sms">
 <div class="no-msg">
 <div class="close-tips">本窗口 <span id="smsbox_close_countdown">5</span> 秒后自动关闭</div>
<!--  <a class="btn-white-big" href="javascript:;" onclick="send_sms('', '');" hidefocus="hidefocus">发微讯</a>&nbsp;&nbsp; -->
 <a class="btn-white-big" href="javascript:;" onclick="close_window();" hidefocus="hidefocus">关闭</a>
 </div>
</div>
</div>
</body> 
</html>