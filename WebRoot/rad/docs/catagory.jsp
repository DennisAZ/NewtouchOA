<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="yh.core.util.file.YHFileUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="yh.core.servlet.YHServletUtility"%>

<head>
<title>yh</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script language="JavaScript" type="text/javascript">
var selectedId = "";
function doClick(obj) {
  if (selectedId == obj.id) {
    return;
  }
  if (selectedId) {
    document.getElementById(selectedId).style.backgroundColor = "#FFFFFF";
  }
  selectedId = obj.id;
  document.getElementById(selectedId).style.backgroundColor = "#CCDADF";
}
</script>
</head>
<body>
<table width="100%" cellspacing="0" cellpadding="3">
<%
String catagory = request.getParameter("path");
String path = null;
if (!YHUtility.isNullorEmpty(catagory)) {
  path = YHServletUtility.getWebAppDir(this.getServletContext()) + "rad\\docs\\" + catagory;
  File pathFile = new File(path);
  String[] dirArray = pathFile.list();
  int i = 0;
  for (String fileName : dirArray) {
    String cataPath = path + "\\" +  fileName;
    File cataFile = new File(cataPath);
    if (!cataFile.isDirectory()) {
      continue;
    }
    String infoPath = path + "\\" +  fileName + "\\" + "info.text";
    File infoFile = new File(infoPath);
    if (!infoFile.exists()) {
      continue;
    }
    List entryList = new ArrayList();
    YHFileUtility.loadLine2Array(infoPath, 0, 100, entryList, "UTF-8");
    if (entryList.size() < 1) {
      continue;
    }
    String entryDesc =  entryList.get(0).toString();
    if (entryDesc.indexOf("=") > 0) {
      entryDesc = entryDesc.split("=")[1];
    }
    String author = entryList.size() > 1 ? entryList.get(1).toString() : "";
    String date = entryList.size() > 2 ? entryList.get(2).toString(): "";
%>
  <tr>
    <td id="link_<%=i %>" onclick="doClick(this);">
      <a href="<%=contextPath + "/rad/docs/" + catagory + "/" + fileName + "/index.htm"%>" target="clientCmp"><%=entryDesc %></a>
    </td>
  </tr>
<%
    i++;
  }
}
%>
</table>
</body>
</html>