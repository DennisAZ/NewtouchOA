<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
  //String oaSsoPort  = YHSysProps.getProp("OA_SSO_PORT");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<style>
.PageHeader{
   height:32px !important;
   height:79px;
   padding-top:47px;
   color:#666666;
}
.PageHeader .title{
   float:left;
   margin-top:8px;
   margin-right:20px;
   font-size:14px;
   font-weight:bold;
}
.PageHeader .header-left{
   float:left;
}
.PageHeader .header-right{
   float:right;
}
a.ToolBtn{
   display:inline-block;
   padding-left:5px;
   text-decoration:none;
   background:url("tool_btn.png") left 0px no-repeat;
}
a.ToolBtn span{
   display:inline-block;
   padding:0px 13px 0px 8px;
   height:25px;
   line-height:25px;
   color:#373737 !important;
   background:url("tool_btn.png") right -50px no-repeat;
   cursor:pointer;
}
a.ToolBtn:hover,
a.ToolBtn-active{
   background-position:left -25px;
}
a.ToolBtn:hover span,
a.ToolBtn-active span{
   color:#fff !important;
   background-position:right -75px;
}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/sso.js"></script>
<script> 
var oaSsoPort = "";
function delete_mailbox(emailId){
 msg='确认要删除该邮箱吗？所有存储的邮件都将被删除！';
 if(window.confirm(msg)){
   var url = contextPath + "/yh/core/funcs/email/act/YHWebmailAct/deletWebmailInfo.act?seqId=" + emailId;
   var rtJson = getJsonRs(url);
   if(rtJson.rtState == "0"){
	   if(oaSsoPort == ''){
		   location.reload();
	   }
	   else{
       var url = location.href;
       var httpIp = url.split(contextPath)[0];
       rsSSO(httpIp+':'+oaSsoPort+'/general/crm/apps/crm/modules/SendEmail/aysnWebMail.php?seqId='+emailId+'&flag=1');
	   }
   }else{
     alert(rtJson.rtMsrg);
   }
 }
}

function listWebInfo(){
  var url = contextPath + "/yh/core/funcs/email/act/YHWebmailAct/listWebmailInfo.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    for(var i = 0 ; i < rtJson.rtData.length; i ++){
      var eamilcontent = rtJson.rtData[i].email;
      if(rtJson.rtData[i].isDefault == "1"){
        eamilcontent += " [默认邮箱]";
      }
      var html = "<tr class=\"TableData\"><td align=\"center\">" +  eamilcontent + "</a></td>"
        + "<td nowrap align=\"center\"><a href=\"edit.jsp?seqId=" + rtJson.rtData[i].seqId + "\">配置</a>&nbsp;&nbsp;"
        + "<a href=\"javascript:delete_mailbox(" + rtJson.rtData[i].seqId + ");\">删除</a></td></tr>";
      $("content").insert(html,"bottom");
    }
  }else{
    alert(rtJson.rtMsrg);
  }
}
function doInit(){
  listWebInfo();
}

function locationTo(){
	location.reload();
}
</script>
</head>
<body class="bodycolor" onload="doInit()">
<div class="PageHeader">
   <div class="title">Internet 邮箱</div>
   <div class="header-left">
      <a class="ToolBtn" href="add.jsp"><span>新建邮箱</span></a>
   </div>
</div>
<table class="TableList" width="90%" align="center">
     <tr class="TableHeader">
      <td nowrap align="center" width="300">电子邮件地址</td>
      <td nowrap align="center" width="140">操作</td>
    </tr>
    <tbody id="content">
    </tbody>
</table>
</body>
</html>