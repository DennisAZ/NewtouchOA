<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ include file="/core/inc/header.jsp" %>
        <%
        String path = request.getParameter("path").replaceAll("\\\\", "\\\\\\\\");
        
        %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/javascript">
function reNameFile(){
  if($("rename").present()){
    var json = getJsonRs(window.opener.actionUrl + "/reName.act","path=<%=path%>&newName=" + $F("rename"));
    if(json.rtState == '0'){
      window.opener.folderTree.updateNode('<%=path%>', json.rtData);
      window.close();
    }else if(json.rtState == '1'){
	  alert(json.rtMsrg);
	}
  }else{
		alert("文件名不能为空！");
		$("rename").focus();
  } 
}


</script>
</head>
<body>
<input type="text" name="rename" id="rename" class="SmallInput">
<input type="button" value="重命名"  class="SmallButtonW "  onclick="reNameFile()"> 
</body>
</html>