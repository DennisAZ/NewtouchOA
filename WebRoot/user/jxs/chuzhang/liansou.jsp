<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
    <%
    String approve =  request.getParameter("approve");
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>验收方案</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/workflow.js"></script>
<script type="text/javascript">
function openProject() {
  formView(68 , 570);
}
function approve() {
  location.href = contextPath + "/core/funcs/workflow/flowrun/list/inputform/index.jsp?runId=68&flowId=570&prcsId=2&flowPrcs=2&isNew=&sortId=&skin=";
}
function clearPrcs1() {
  clearPrcs(570 , 68 , 2 , 2);
}
</script>
</head>
<body>
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td height=24><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><b>
     <% if ("1".equals(approve)) {
   out.print("待审");
    } else {out.print("已审");}  %>验收方案</b></td>
  </tr>
</table>

<table  width="100%" class="TableList">
    <tr class="TableHeader">
      <td>
      送修通知单号
      </td>
       <td>
          送修时间
      </td>
      <td>
      装备名称
      </td>
      <td>
      承修部门
      </td>
        <td>
      修理等级
      </td>
      <td>
      操作
      </td>
    </tr>
    <tr class="TableLine1">
      <td>
      20110321001
      </td>
      <td>
      2011年03月21日

      </td>
      <td>
      东风汽车
      </td>
      <td>部门3</td>
      <td>中修</td>
      <td>
     <% if ("1".equals(approve)) { %>
      <a href="javascript:void(0)" onclick="approve()">审核</a>
      <% } %>
      <a href="javascript:void(0)" onclick="openProject()">查看验收方案</a>
      </td>
    </tr>
    <tr class="TableLine2">
      <td>
      20110317002
      </td>
      <td>
      2011年03月17日

      </td>
      <td>
       坦克
      </td>
      <td>
     部门2
      </td>
       <td>中修</td>
      <td>
       <% if ("1".equals(approve)) { %>
      <a href="javascript:void(0)" onclick="approve()">审核</a>
      <% } %>
      <a href="javascript:void(0)" onclick="openProject()">查看验收方案</a>
      </td>
    </tr>
     <tr class="TableLine1">
      <td>
      20110316001
      </td>
      <td>
      2011年03月16日

      </td>
      <td>
      战车
      </td>
      <td> 部门1
      </td>
       <td>小修</td>
      <td>
      <% if ("1".equals(approve)) { %>
      <a href="javascript:void(0)" onclick="approve()">审核</a>
      <% } %>
      <a href="javascript:void(0)" onclick="openProject()">查看验收方案</a>
      </td>
    </tr>
    </table>
</body>
</html>