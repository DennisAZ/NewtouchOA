<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
  String ID=request.getParameter("ID");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/Menu.js" ></script>
<script type="text/Javascript">
  
var ID="<%=ID%>";
if(ID=="0"){
	$("content").innerHTML="无效的文件ID";
	
}

//获取文件路径
function getFileAttach(ID){
	  var url = "<%=contextPath%>/yh/core/funcs/system/ispirit/n12/file/act/YHImOffLineAct/downFile.act";
      var param="ID="+ID;
        var rtJson = getJsonRs(url,param);
        if (rtJson.rtState == "0") {
           var filePath=rtJson.rtData.filePath;
           if(filePath==""){
        	   $("content").innHTML="无效附件ID";
           }else{
        	  window.loaction.href=filePath;
           }
        }else{
        	$("content").innHTML="后台发生错误！";
        }
	
}

</script>


   <div align="center" class="content" id="content" style="font-size:12pt"></div>