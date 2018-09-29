<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>动态题目</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script>
function hide_tree()
{
   var frame2 = parent.document.getElementById('frame2');
   if(frame2.cols=='0,*')
   {
      frame2.cols = '200,*';
      document.getElementById('btn').innerHTML='<<隐藏目录树';
   }
   else
   {
      frame2.cols = '0,*';
      document.getElementById('btn').innerHTML='显示目录树>>';
   }
}
</script>
</head>
<body>
<body class="bodycolor" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr class="TableHeader">
    <td class="Big" valign="bottom"><img src="/yh/core/funcs/netdisk/images/netdisk.gif" WIDTH="22" HEIGHT="20" align="absmiddle"> <b>网络硬盘</b>&nbsp;&nbsp;&nbsp;&nbsp;
      <a id="btn" href="javascript:hide_tree();" onfocus="this.blur();" class="A1 small" style="text-decoration: none;font-weight:normal;">隐藏目录树</a>
    </td>
  </tr>
</table>
</body>
</body>
</html>