<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%

String rt_state=(String)request.getAttribute("rt_state");
String rt_msg=(String)request.getAttribute("rt_msg");
String attrId=(String)request.getAttribute("attrId");
String attrName=(String)request.getAttribute("attrName");
String DEST_UID=(String)request.getAttribute("DEST_UID");
String file_size=(String)request.getAttribute("file_size");

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
var rtState="<%=rt_state%>";
var rtMsg="<%=rt_msg%>";
var attrId="<%=attrId%>";
var destUid="<%=DEST_UID%>";
var fileSize="<%=file_size%>";
var attrName="<%=attrName%>";
	if(rtState=="0"){//上传成功
		
		
	}else{//上传失败
	  if("0"==destUid){
		  $("content").innHTML="接收方ID无效";
	  }else{
		  $("content").innHTML="文件上传失败";
	  }
		
		
	}

	function insertOffline(destUid,attrId,attrName,fileSize){
		  var url = "<%=contextPath%>/yh/core/funcs/system/ispirit/n12/file/act/YHImOffLineAct/uploadFile.act";
		  var param="destUid="+destUid+"&attrId="+attrId+"&attrName="+attrName+"&fileSize="+fileSize;
		    var rtJson = getJsonRs(url,param);
		    if (rtJson.rtState != "0") {
		    	$("content").innHTML="数据库操作失败！";
		    }
		
	}

</script>


      <div align="center" class="content" id="content" style="font-size:12pt"></div>


