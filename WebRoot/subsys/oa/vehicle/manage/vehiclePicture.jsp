<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
  String attachmentId = request.getParameter("attachmentId");
  String attachmentName = request.getParameter("attachmentName");
  String seqId = request.getParameter("seqId");
  String filePath = request.getParameter("filePath");
  if(attachmentId == null){
    attachmentId = "";
  }
  if(attachmentName == null){
    attachmentName = "";
  }
  if(seqId == null){
    seqId = "";
  }
  if(filePath == null){
    filePath = "";
  }
%>
<head>
<title>车辆信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">

function doOnload(){
  var attachmentName = '<%=attachmentName%>';
  var attachmentId = '<%=attachmentId%>';
  var seqId = '<%=seqId%>';
  var filePath = '<%=filePath%>';
  var attr = "暂无照片";
  var pictureStr = ["png","gif","jpg","bmp","PNG","GIF","JPG","BMP"];
  var attachmentIdDate = attachmentId.substr(0,4);
  var fileName =attachmentName.lastIndexOf("\.");
  fileName = attachmentName.substring(fileName+1,attachmentName.length);
  var pType = "2";//不是图片
  for(var i =0 ; i< pictureStr.length ; i++){
    if(pictureStr[i]==fileName){
      pType = "1";
      break;
    }
  }
  if(pType=='1'){
    attr = "<img src=<%=contextPath %>/getFile?uploadFileNameServer="+filePath+"/"+attachmentId + "_" + encodeURIComponent(attachmentName) + "></img>";
  } else{

    downLoadFile(attachmentName,"1008_"+attachmentId,'vehicle');
 }
  $("attr").innerHTML= attr; 
}
</script>
</head>
<body  onload = "doOnload();">
<center id="attr"></center>	

</body>
</html>
