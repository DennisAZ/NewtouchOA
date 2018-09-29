<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String idstr = request.getParameter("diaId");
  String type = request.getParameter("type");
  if(type == null){
    type = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript">
var id = "<%=idstr %>";
var type = "<%=type %>";
function doInit(){
  shareDiaDetaile(id);
  var isShowOp = true;
  var isShowCommentOp = true;
  if(type == "1"){
    isShowOp = false;
    isShowCommentOp = false;
  }
  listComment(id,false,isShowOp,isShowCommentOp);
}
</script>
<title>查看</title>
</head>
<body onload="doInit()">
<div id="readbody">
  <div class="subject">
    <table border="0" width="100%">
	  <tr>
       <td style="font-size: 13px;"><img src="<%=imgPath%>/diary.gif" width="18" height="18" align="absmiddle"> <span id="subject"></span></td>
       <td width="40%" align="right" style="font-size: 13px;">
    	<input type="button" value="打印" class="SmallButtonW" onclick="document.execCommand('Print')">&nbsp;
        <input type="button" value="返回" class="SmallButtonW" onClick="location='../diaryBody.jsp'">
      </td>
    </tr>
  </table>
</div>

<div style="clear: both;border-bottom: 1px #9F9F9F solid;">
	<div class="diary_type"><span id="diaryType"></span> | 日志日期：<span id="diaDate"></span> | 最后修改：<span id="diaTime"></span>  | 日志作者：<span id="userName"></span></div>
    <div class="content" style="overflow-y:auto;overflow-x:auto;width=100%;height=100%"><span id="content"></span><br><br><div id="attrDiv" style="display:none"><b>附件文件:</b><br><span id="attr"></span></div></div>
    <div class="content" id="comment_<%=idstr %>">
    </div>
  </div>
</div>
<br><br>
</body>
</html>