<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文章信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function doInit(){
  var url = "<%=contextPath%>/yh/cms/content/act/YHContentAct/getContentDetail.act?seqId=<%=seqId%>";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
    if(data.contentDate){
      $("contentDate").value = data.contentDate.substr(0,19);
    }
    fckContentStr = data.content;
  }else{
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 文章信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent">文章标题：</td>
    <td align="left" class="TableData" width="180" colspan="3"><div id="contentName"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">文章副标题：</td>
    <td align="left" class="TableData" width="180" colspan="3"><div id="contentTitle"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">摘要：</td>
    <td align="left" class="TableData" width="180" colspan="3"><div id="contentAbstract"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">关键字：</td>
    <td align="left" class="TableData" width="180"><div id="keyword"></div> </td>
    <td align="left" width="120" class="TableContent">来源：</td>
    <td align="left" class="TableData" width="180"><div id="contentSource"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">文件名：</td>
    <td align="left" class="TableData" width="180"><div id="contentFileName"></div> </td>
    <td align="left" width="120" class="TableContent">作者：</td>
    <td align="left" class="TableData" width="180"><div id="contentAuthor"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">发布日期：</td>
    <td align="left" class="TableData" width="180"><div id="contentDate"></div> </td>
    <td align="left" width="120" class="TableContent">所属栏目：</td>
    <td align="left" class="TableData" width="180"><div id="columnName"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent" colspan="4">文章内容：</td>
  </tr>
  <tr>
    <td align="left" class="TableData" colspan="4"><div id="content"></div></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>
</body>
</html>