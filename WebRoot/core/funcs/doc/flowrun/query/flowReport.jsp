<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
      <%@ include file="/core/inc/header.jsp" %>
      <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
    <%@ page import="java.sql.Timestamp,java.util.Set,java.util.HashMap,java.util.List,yh.core.funcs.doc.util.YHWorkFlowUtility,java.util.Map" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="java.net.URLEncoder" %>
<%
String flowName = request.getParameter("flowName");
if (flowName == null ) {
  flowName = "";
}

String op = request.getParameter("OP");
String error = (String)request.getAttribute("error");
if (error != null) {
  out.println("<div align=center>");
  out.println(error);
  out.println("</div>");
  return ;
}
List<Map> list = (List<Map>)request.getAttribute("result");
Map titles = (Map)request.getAttribute("title");
String[] sums = request.getParameterValues("disselected");
if (list.size()== 0 ) {
  String msg =  YHWorkFlowUtility.Message("无符合条件的记录", 0);
  out.println("<link rel=\"stylesheet\" href =\""+cssPath + "/style.css\"><div align=center>");
  out.println(msg);
  out.println("</div>");
  return ;
}
//html
if ("1".equals(op)) {
  %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作流分组统计报表 - <%=flowName %></title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
</head>
  <%
} else {
  response.setContentType("application/msexcel");
  flowName = URLEncoder.encode("工作流分组统计报表-" + flowName, "UTF-8");
  response.setHeader("Content-Disposition", "attachment;filename="+flowName+".xls");
%>
<html>
<head>
<title>工作流分组统计报表 - <%=flowName %></title>
</head>
<%}%>
<body>
<table cellspacing="0" width="100%" style="border-collapse:collapse" border=1 cellspacing=0 cellpadding=3 bordercolor='#000000' class="small">
<%if ("1".equals(op)) {
  %>
  <tr class="TableHeader">
   <%
  } else {
  %>
  <tr style="BACKGROUND: #D3E5FA; color: #000000; font-weight: bold;">
  <%}%> 
  
   <td  nowrap align="center"><b>分组：流水号&nbsp;</b></td>
   <td  nowrap align="center"><b>名称/文号&nbsp;</b></td>
   <td  nowrap align="center"><b>流程状态&nbsp;</b></td>
   <td  nowrap align="center"><b>流程开始日期&nbsp;</b></td>
    <td  nowrap align="center"><b>流程开始时间&nbsp;</b></td>
  <%
  String itemIds = "runId,runName,runStatus,runDate,runTime,";
  Set<String> key = titles.keySet();
  for (String s : key) {
    String t = (String)titles.get(s);
    if (!"".equals(s) ) {
      itemIds += s + ",";
      out.println("<td  nowrap align=\"center\"><b>"+t+"&nbsp;</b></td>");
    }
  }
  %>
</tr>
<%
int rowSpan = 1;
if (sums != null && sums.length > 0) {
  rowSpan = 2;
}
String[] s = itemIds.split(",");
Map<String , Float> countData = new HashMap();
for (Map map : list) {
  %>
  <tr class="TableData">
  <% 
  for (String tmp : s) {
    if (!"".equals(s)) {
      if ("runId".equals(tmp)) {
        String runId = (String)map.get("runId");
        %>
        <td rowspan="<%=rowSpan %>"><%=runId %></td>
        <% 
      } else {
        String value = (String)map.get(tmp);
        %>
        <td width="100%" nowrap ><%=value%></td>
        <% 
      }
    }
  }
  %>
  </tr>
  <% 
  if (rowSpan == 2) {
    %>
    <tr class="TableData">
    <td>小计</td>
    <% 
    for (String tmp : s) {
      if (!"".equals(tmp) && !"runId".equals(tmp) && !"runName".equals(tmp)) {
        boolean has = false;
        for (String tmp2 : sums) {
          if (tmp.equals("DATA_" + tmp2)) {
            has = true;
            break;
          }
        }
        String value = (String)map.get(tmp);
        if (has) {
          if (YHUtility.isNumber(value) && value != null && !"".equals(value)) {
            Float f = Float.parseFloat(value);
            Float fCount = (Float)countData.get(tmp);
            if (fCount == null ) {
              fCount = new Float(0);
            }
            fCount += f;
            countData.put(tmp, fCount);
          } else {
            value = "0";
          }
        } else {
          value = "";
        }
        
        %>
        <td><%=value %></td>
        <% 
      }
    }
  %>
    </tr>
    <%
  }
}

if (rowSpan == 2) {
  %>
  <tr>
  <td>合计</td>
  <% 
    for (String tmp : s) {
      if (!"".equals(tmp)  && !"runId".equals(tmp)) {
        boolean has = false;
        for (String tmp2 : sums) {
          if (tmp.equals("DATA_" + tmp2)) {
            has = true;
            break;
          }
        }
        String value = "";
        Float f = countData.get(tmp);
        if (has) {
          value = String.valueOf(f);
          if (f == null) {
            value = "0";
          }
        }
        
        %>
        <td><%=value %></td>
        <% 
      }
    }
  %>
  
  </tr>
  <%
}
%>

</table>
</body>
</html>