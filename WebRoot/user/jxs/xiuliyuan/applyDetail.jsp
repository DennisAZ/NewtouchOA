<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>申领记录</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
</head>
<body>
<table  width="100%" class="TableList">
    <tr class="TableHeader">
      <td>
      申领物品
      </td>
       <td>
         申领数量
      </td>

      <td>
      申领时间
      </td>
      <td>
      申领状态
      </td>
      <td>
      操作
      </td>
    </tr>
    <tr class="TableLine1">
     <td>
      嵌子
      </td>
       <td>
         1
      </td>

      <td>
      2011-03-15
      </td>
      <td>
     正在审批中
      </td>
      <td>
      <input value="提醒审批人" type="button" class="SmallButtonC" onclick=""/>
      <input value="查看详情" type="button" class="SmallButtonC" onclick=""/>
      </td>
    </tr>
    <tr class="TableLine2">
     <td>
      嵌子
      </td>
       <td>
         1
      </td>

      <td>
      2011-03-15
      </td>
      <td>
     审批未通过
      </td>
      <td>
      <input value="修改" type="button" class="SmallButtonC" onclick=""/>
       <input value="查看详情" type="button" class="SmallButtonC" onclick=""/>
       <input value="删除" type="button" class="SmallButtonC" onclick=""/>
      </td>
    </tr>
    <tr class="TableLine1">
     <td>
      嵌子
      </td>
       <td>
         1
      </td>

      <td>
      2011-03-15
      </td>
      <td>
     审批通过
      </td>
      <td>输入物品单号：<input type="text">
      <input value="签收" type="button" class="SmallButtonC" onclick=""/>
       <input value="查看详情" type="button" class="SmallButtonC" onclick=""/>
      </td>
    </tr>
    <tr class="TableLine1">
     <td>
      嵌子
      </td>
       <td>
         1
      </td>

      <td>
      2011-03-15
      </td>
      <td>
     已签收
      </td>
      <td>
       <input value="查看详情" type="button" class="SmallButtonC" onclick=""/>
      </td>
    </tr>
     <tr class="TableControl" align="right">
      <td  colspan="5">
      <input value="关闭" type="button" class="SmallButtonC" onclick="window.close()"/></td>
    </tr>
    </table>
</body>
</html>