<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>菜单分类设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
</head>
<body topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;菜单设置</span>
    </td>
  </tr>
</table>

<table class="MessageBox" align="center" width="480">
  <tr>
    <td class="msg info">
      <h4 class="title">菜单定义简明指南</h4>
      <div class="content" style="font-size:12pt">
1、通过灵活定义菜单，可以挂接外部的B/S、C/S或单机版系统，从而形成以 YH为核心的应用平台。<br><br>
2、菜单定义实际并不难，如果存在疑问，也可以通过咨询软件开发商获得详尽的帮助。<br><br>
3、菜单定义的关键是理解菜单项ID和菜单项代码的含义：子菜单项ID是菜单的唯一性标识，应避免重复；子菜单项代码用于表示子菜单项在其所属层次中的位置，子菜单项代码为两位数字。<br><br>
4、同一层次的菜单项代码建议保留一定间隔，以方便日后在中间插入菜单项。<br><br>
5、新增的菜单项，只有进行角色权限设置后才能看到。
</div>
    </td>
  </tr>
</table>
</body>
</html>