<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>人员显示</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<script type="text/javascript">
</script>
</head>
<frameset rows="*"  cols="200,*" frameborder="yes" border="0" framespacing="0" id="frame1">
  <frame name="userlist" scrolling="yes" frameborder="no"  src="<%=contextPath %>/subsys/oa/gift_product/outstock/recodequery/userlist.jsp"/>
  <frame name="queryoutstock" scrolling="yes"  src="<%=contextPath %>/subsys/oa/gift_product/outstock/recodequery/blank.jsp"/>
</frameset>
</html>