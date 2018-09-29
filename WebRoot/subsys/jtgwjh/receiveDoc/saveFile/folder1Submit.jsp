<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("checkId");
	String attachId=request.getParameter("attachId");
	String attachName=request.getParameter("attachName");
	String subject=request.getParameter("subject");
	String module=request.getParameter("module");
	String docReceiveSeqIds=request.getParameter("docReceiveSeqIds");//公文交互
	
	if(module==null){
		module="";
	}
	
	if(seqId==null){
	  seqId="0";
	}
	if(subject==null){
	  subject="";
	}

	
	String serviceName = request.getServerName();
	int port = request.getLocalPort();
	String filePath = YHSysProps.getAttachPath() + "/docReceive" ;
	filePath = filePath.replace("\\" ,"\\\\");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件归档</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/jtgwjh/receiveDoc/js/designer.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/jtgwjh/receiveDoc/js/jhDocReceive.js"></script>
<script type="text/javascript">
var docReceiveSeqIds = "<%=docReceiveSeqIds%>";
var requestURL="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct";
	//alert("seqId:"+'<%=seqId%>' +" attachId:"+'<%=attachId%>' + "  attachName:"+'<%=attachName%>' +"  module:"+'<%=module%>');
function doInit(){
  $("sendDiving").style.display ="";
  getReceiveByseqIds();

	updateHandStatus();
	window.opener.location.reload();
}


/**
 * 根据需要转存公文的seqId字符串获取所有公文的附件包括正文
 */
function getReceiveByseqIds(){
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/selectByIds.act?seqIds=" +docReceiveSeqIds ;
	var rtJson=getJsonRs(url);
	//alert("rsText>>:"+rsText);
	if(rtJson.rtState == '1'){
		alert(rtJson.rtMsrg);
		return;
	}

  
  var prcs = rtJson.rtData;
  var unloadPath = rtJson.rtMsrg;
  for(var i = 0;i<prcs.length;i++){
    var prc= prcs[i];
    $("attachId").value = prc.attachId;
    $("attachName").value = prc.attachName;
    var isMain = prc.isMain;//1:是正文
    if(isMain == '1' ){
      var obj = $("HWPostil1");
      if(!obj){
      	loadAIPObject($("tempAIP"),0,0);
       }
       obj = $("HWPostil1");
       obj.SilentMode =1 ;//1：安静模式。安静模式下不显示下载提示对话框、文档转化提示对话框和部份提示信息
      var attachmentId = prc.attachId;
      var attachmentName = prc.attachName;
      var attachmentIdArra = attachmentId.split("_");
      var attachmentIdDate = attachmentIdArra[0];
      var fileName = attachmentName;
      var filePath = "<%=filePath%>";
      var attachmentIdDate = attachmentId.substr(0,4);
      var fileName = attachmentIdArra[1] + "_" + attachmentName;
      filePath = filePath +"/" +attachmentIdDate + "/" + fileName;
      obj.LoadFile("http://<%=serviceName%>:<%=port%><%=contextPath %>/getFile?uploadFileNameServer=" + encodeURIComponent(filePath) );
      //文件变灰
      obj.grayData(31);
      //去掉章
      getAllUserNoteInfo(obj);
     
      //脱密的文件
      updateAipToService(unloadPath,filePath,attachmentName);

      obj.CloseDoc(0);
       
    }
    
    //安全日志记录
     addSelLog(prc.seqId,"3");
    
    
    var url2 = requestURL + "/transferFolder.act";
  	//alert(url);
  	var pars = $('form1').serialize();
  	var json=getJsonRs(url2,pars);
  	//alert("rsText>>:"+rsText);
  	if(json.rtState == '1'){
  		alert(json.rtMsrg);
  		return ;				
  	}
  	$("listDiv").show();
  
  	/*var prcsJson=json.rtData;
  	var flag=prcsJson.flag;
  	if(flag=="true"){
  		$("listDiv").show();
  	}else{
  		$("failDiv").show();
  	}*/
  	

  }  
  
  $("sendDiving").style.display ="none";
}

/*
 * 把aip文件上传到服务器去
 */
function updateAipToService(unloadPath,filePath,attachmentName) {
  try{   
    var webObj=document.getElementById("HWPostil1");
        webObj.HttpInit();
        webObj.HttpAddPostCurrFile("FileBlody");  
        var port = '<%=port%>';
        returnValue = webObj.HttpPost("http://<%=serviceName%>:" +port+ "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/logic/YHAipToJNI/ZhuanCunsaveAIP.act?filePath=" + encodeURIComponent(filePath) + "&fileName=" + encodeURIComponent(attachmentName) + "&unloadPath=" + encodeURIComponent(unloadPath));

        if("failed" == returnValue){
          alert("上传失败！");
        }else{
           var attacheArr = returnValue.split("`~");
           if(attacheArr.length == 2){
             $("attachId").value = attacheArr[0];
             $("attachName").value = attacheArr[1];
            }
        }
        //window.location.href  = "index.jsp" ;
  }catch(e){
    alert(e);
  }
}



//归档
function updateHandStatus(){
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/updateHandStatus.act?seqIds=" + docReceiveSeqIds + "&handstatus=1" ;
  var json = getJsonRs(url);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
}
</script>
</head>
<body onload="doInit();">
<!--  <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/save_file.gif" align="middle"><span class="big3"> 文件转存 — <%=attachName%></span>
    </td>
  </tr>
</table>
-->
<div id="listDiv" style="display: none">

<table class="MessageBox" align="center" width="310">
  <tr>
    <td class="msg blank">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">文件转存归档文件柜成功</div>
    </td>
  </tr>
</table>
 
<div class="big1" align="center">
<input type="button"  value="关闭" class="SmallButtonW" onClick="window.close();">
</div>
</div>

<div id="failDiv" style="display: none">
<table class="MessageBox" align="center" width="310">
  <tr>
    <td class="msg blank">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">文件转存至公共文件柜失败，可能原文件不存在！</div>
    </td>
  </tr>
</table>
 
<div class="big1" align="center">
<input type="button"  value="关闭" class="SmallButtonW" onClick="window.close();">
</div>

</div>
<div id="sendDiving" style="">
<table class="MessageBox" align="center" width="310">
  <tr>
    <td class="msg blank">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">文件正在转存中，请稍后...！</div>
    </td>
  </tr>
</table>
</div>


<form name="form1" id="form1" action="" method="post">
	<input type="hidden" name="attachId" id="attachId" value="<%=attachId %>">
	<input type="hidden" name="attachName" id="attachName" value="<%=attachName %>">
	<input type="hidden" name="module" value="<%=module.trim() %>">
	<input type="hidden" name="subject" value="<%=subject %>">
	<input type="hidden" name="checkId" id="checkId" value="<%=seqId %>">
</form>

<div id="tempAIP"></div>
</body>
</html>