<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>  
    <%
    String seqId = request.getParameter("seqId");
    String userName = request.getParameter("userName");
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人资源状况查询</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
  var seqId = "<%=seqId %>";
  function doInit() {
    var url = contextPath + "/yh/core/funcs/system/resManage/act/YHResManageAct/getUserRes.act";
    var json = getJsonRs(url , "userId=" + seqId);
    if (json.rtState == "0") {
      var user = json.rtData;
      if (!user)  {
        return ;
      }
      emailCapacity = json.rtData.emailCapacity;
      if (emailCapacity > 0) {
        emailCapacity = emailCapacity + "M";
      }else {
        emailCapacity = "无限制"
      }
      $("emailCapacitySpan").update(emailCapacity);
      folderCapacity = json.rtData.folderCapacity;
      if (folderCapacity > 0) {
        folderCapacity = folderCapacity + "M";
      }else {
        folderCapacity = "无限制"
      }
      $("folderCapacitySpan").update(folderCapacity);
      inboxCount = user.inboxCount;
      $('inboxCountTd').update(inboxCount);
      $('emailSize1MTd').update(user.emailSize1M );
      $('emailSize1Td').update(user.emailSize1 +"字节");

      $('outboxCountTd').update(user.outboxCount);
      $('emailSize2MTd').update(user.emailSize2M );
      $('emailSize2Td').update(user.emailSize2 +"字节");
      
      $('sentboxCountTd').update(user.sentboxCount);
      
      $('emailSize3MTd').update(user.emailSize3M );
      $('emailSize3Td').update(user.emailSize3 +"字节");
      $('boxCountTd').update(user.boxCount);
      $('emailSize4MTd').update(user.emailSize4M );
      $('emailSize4Td').update(user.emailSize4 +"字节");

      $('sortCountTd').update(user.sortCount);
      $('sortSizeMTd').update(user.sortSizeM );
      $('sortSizeTd').update(user.sortSize +"字节");
      $('allSizeSpan').update(user.allSize);
    }
  }
</script>
</head>
<body onload="doInit()">

<table align="center">
<tr>
<td>

<table class="TableList" width="500">
    <tr class="TableHeader">
      <td nowrap align="center" width=150><b>内部邮件</b></td>
      <td nowrap align="center" width=50>邮件数</td>
      <td nowrap align="center" colspan=2>大小(<span id="emailCapacitySpan"></span>)</td>
    </tr>
    <tr class="TableData">
      <td nowrap>收件箱(包括自建邮箱)</td>
      <td align="center" id="inboxCountTd"></td>
      <td align="right" id="emailSize1MTd"></td>
      <td align="right" id="emailSize1Td"></td>
    </tr>
    <tr class="TableData">
      <td nowrap>发件箱</td>
      <td align="center" id="outboxCountTd"></td>
      <td align="right" id="emailSize2MTd"></td>
      <td align="right" id="emailSize2Td"></td>
    </tr>
    <tr class="TableData">
      <td nowrap>已发送邮件箱</td>
      <td align="center" id="sentboxCountTd"></td>
      <td align="right" id="emailSize3MTd"></td>
      <td align="right" id="emailSize3Td"></td>
    </tr>
    <tr class="TableControl">
      <td nowrap><b>合计</b></td>
      <td align="center" id="boxCountTd"></td>
      <td align="right" id="emailSize4MTd"></td>
      <td align="right" id="emailSize4Td"></td>
    </tr>
</table>

<br>
<table class="TableList" width="500">
    <tr class="TableHeader">
      <td nowrap align="center" width="150"><b>个人文件柜</b></td>
      <td nowrap align="center" width="50">文件数</td>
      <td nowrap align="center" colspan=2>大小(<span id="folderCapacitySpan"></span>)</td>
    </tr>
    <tr class="TableControl">
      <td nowrap><b>合计</b></td>
      <td align="center" id="sortCountTd"></td>
      <td align="right" id="sortSizeMTd"></td>
      <td align="right" id="sortSizeTd"></td>
    </tr>
</table>
</td>
</tr>

<thead>
<tr>
<td>
<div id="noData" align=center >
   <table class="MessageBox" width="400">
    <tbody>
        <tr>
            <td id="msgInfo" class="msg info">
            总计<span id="allSizeSpan"></span>，用户：<%=userName %>
            </td>
        </tr>
    </tbody>
</table>
</div>
</td>
</tr>
</thead>
</table>
</body>
</html>