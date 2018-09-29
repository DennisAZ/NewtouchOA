<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String contextPath2 = request.getContextPath();
%>
<form name="form1" action='<%=contextPath2%>/yh/core/oaknow/act/YHOASeachAct/findResolveStatus.act' method='post'>
	 <div class="searchcss">
	 	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size:14px;">
    <tr>
	      <td><a onclick="gotoIndex();" class="logo">${oaName}</a>
	      	<input type="text" onmouseover="this.focus()" onfocus="this.select()" name="question" class="" size="35" value="${askName}"/>
	 	      <input type="submit" value="搜 索"  class="BigButton" />
          <input type="hidden" value="" name="currNo" id="currNo"/>
          <input type="hidden" value="resolve" name="flag" id="flag"/>
	     </td>
	  </tr>
	  </table>
	 </DIV>
	</form>