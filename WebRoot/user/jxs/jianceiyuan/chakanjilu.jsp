<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检测记录</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
</head>
<body>
<table  width="100%" class="TableList">
    <tr class="TableHeader">
      <td>
      检测项
      </td>
       <td>
          标准值


      </td>

      <td>
      实际值


      </td>
      <td>
   是否合格
      </td>
    </tr>
    <tr class="TableLine1">
      <td>
      刹车
      </td>
      <td>
     灵


      </td>
      <td>
      <input value="" type="text">
      </td>
	  <td>
       <input name="1" type="radio" value="">是  <input name="1" type="radio" value="">否

	   </td>
    </tr>
    <tr class="TableLine2">
      <td>
      最高车速(km/h)：
 </td>
      <td>
      250
      </td>
      <td>
       <input value="140" type="text">
      </td>
      <td>
       <input name="2" type="radio" value="" >是  <input name="2" type="radio" value="" checked>否

	   </td>
    </tr>
     <tr class="TableLine1">
      <td>
  油耗

      </td>
      <td>
      8.4-9.0L
      </td>
      <td>
       <input value="" type="text">
      </td>
         <td>
       <input name="3" type="radio" value="">是  <input name="3" type="radio" value="">否

	   </td>
    </tr>
     <tr class="TableControl" align="right">
      <td  colspan="4">
      <input value="保存" type="button" class="SmallButtonC"/>
     </td>
    </tr>
    </table>
</body>
</html>