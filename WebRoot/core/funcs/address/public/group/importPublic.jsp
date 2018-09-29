<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
  String flag = request.getParameter("flag");
  if(flag == null) {
    flag = "";
  }
  String groupId = request.getParameter("groupId");
  if(groupId == null) {
    groupId = "";
  }

  String isCount = request.getParameter("isCount");
  if(isCount == null) {
    isCount = "";
  }
  String maxSize = request.getParameter("maxSize");
  if(maxSize == null) {
    maxSize = "";
  }
%>
<head>
<title>分组管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/address/public/js/publicUtil.js"></script>
<script type="text/javascript">
var isCount = "<%=isCount%>";
var flag = "<%=flag%>";
var groupIds = "<%=groupId%>";
var maxSize = "<%=maxSize %>";
function doInit(){
  $("groupId").value = groupIds;
  if(isCount){
    $("cionDiv").style.display = 'none';
  }else{
    //$("turnDiv").style.display = 'none';
  }
  if(flag != "1"){
    showCntrl('listContainer');
    var mrs = '';
    if (maxSize) {
      mrs = " 导入失败，导入文件超出了最大允许导入文件的大小!";
    } else {
      mrs = " 共 "+ isCount + " 条数据导入!";
    }
    WarningMsrg(mrs, 'msrg');
  }else{

  }
}

</script>
</head>
<body topmargin="5" onload="doInit();">
<div id="cionDiv">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
      <td class="Big"><img src="<%=imgPath%>/sys_config.gif" align="absmiddle"><span class="big3"> 导入CSV通讯簿</span><br>
      </td>
    </tr>
  </table>

  <br>
  <br>

  <div align="center" class="Big1">
  <b>请指定用于导入的CSV文件：</b>
  <form name="form1" id="form1" action="<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/importAddressPublic.act" method="post" enctype="multipart/form-data">
    <input type="file" id="csvFile" name="csvFile" class="BigInput" size="30">
    <input type="hidden" id="FILE_NAME" name="FILE_NAME">
    <input type="hidden" id="groupId" name="groupId" value="">
    <input type="button" value="导入" class="BigButton" onClick="importAddressPublic2();">
  </form>
  <br>
  </div>
  </div>
  <div id="listDiv" align="center"></div>
  <br>
  <div id="listContainer" style="display:none">
</div>
<div id="msrg"></div>
  <br>
  <div align="center" id="turnDiv">
   <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/core/funcs/address/public/group/index.jsp';" title="返回">
</div>
</body>
</html>