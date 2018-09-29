<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
 <%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
  String isManage = request.getParameter("isManage");
%>
<html>
<head>
<title>查看公告通知</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function doInit(){
	var seqId = "<%=seqId%>";
	var isManage = "<%=isManage%>";
   var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyShowAct/showObject.act?seqId='+seqId+'&isManage='+isManage;
   var json = getJsonRs(url);
	 if(json.rtState == "0"){
		var rtData = json.rtData;
    $("subject").innerHTML = rtData.subject;
    $("subject").setStyle(rtData.subjectFont);
		var deptName = rtData.deptName;
		var fromName = rtData.fromName;
		var beginDate = rtData.beginDate;
		var ym = rtData.ym;
		var attachFile = rtData.attachFile;
		var isHasNotifyFunc = rtData.isHasNotifyFunc;
		var content = rtData.content;
		var format = rtData.format;
		var notifyAuditingSingle = rtData.notifyAuditingSingle;
		var auditer = rtData.auditer;
		var auditerName = rtData.auditerName;
		var auditerDeptName = rtData.auditerDeptName;
		var download = rtData.download;
		var print = rtData.print;
		var attachmentId = rtData.attachmentId;
		var title = "发布部门：<u title='部门："+deptName+"' style='cursor:pointer'>"+deptName
                  +"</u>&nbsp;&nbsp; 发布人：<u title='部门："+deptName+"' style='cursor:pointer'>"+fromName
                  +"</u>&nbsp;&nbsp;发布于：<i>"+beginDate.substring(0,19)+"</i>";
		if(notifyAuditingSingle=="1"&&auditer!=""){
	           title = title + "&nbsp;&nbsp;审批人：<u title='部门："+auditerDeptName+"' style='cursor:pointer'>"
	                   +auditerName+"</u>";
		    }
		$("title").update(title);
		if(format == "0"){
		  $("content").update(content);
		}
    if(format == "1"){
      var attachFrame ="";
      if(isHaveMht(attachFile, attachmentId)){ 
		    attachFrame = "<iframe id=mhtFrame src='<%=contextPath%>/getFile?module=notify&ym="+ym+"&attachFile=" + encodeURIComponent(findMht(attachFile, attachmentId)) + "'  width='100%' height='100%' style='overflow:auto'></iframe>";
      }else{
        attachFrame = "<iframe  width='100%' height='100%' style='overflow:auto'></iframe>";
      }
      $("content").style.height = '600';
      $("content").update(attachFrame);
    }
    if (format=="2") {
      var urlStr = rtData.content.trim();
      if (!urlStr) {
        window.location.href = contextPath + "/core/inc/empty.html";
      }else if (urlStr.indexOf("http://") == 0) {
        window.location.href = urlStr;
      }else {
        window.location.href = "http://" + urlStr;
      }
      return;
    }
    var attrIds = rtData.attachmentId;
    var attrNames = rtData.attachmentName;
    $('attachmentName').value = attrNames;
    $('attachmentId').value = attrIds;
    if(download == '1' && print == '1'){
      attachMenuUtil("showAtt","notify",null,$('attachmentName').value ,$('attachmentId').value,true,"","","1",true,5);//可以下载,可以打印
    }
    if(download != '1' && print != '1'){
      attachMenuUtil("showAtt","notify",null,$('attachmentName').value ,$('attachmentId').value,true,"","","",false);//禁止下载，禁止打印    }
    if(download == '1' && print != '1'){
      attachMenuUtil("showAtt","notify",null,$('attachmentName').value ,$('attachmentId').value,true,"","","",true);//可以下载,不可以打印    }
    if(download != '1' && print == '1'){   
      attachMenuUtil("showAtt","notify",null,$('attachmentName').value ,$('attachmentId').value,true,"","","1",false,5);//禁止下载，可以打印    }
    if(isManage == 1 || isHasNotifyFunc == 0) {
      $("send").style.display = "none";
    }
  }else{
     $('hasData').style.display = "none";
     $('noData').style.display = "";
     $('msgInfo').update(json.rtMsrg);
  }
}
/*
function findMht(attachFile){
   var profix = attachFile.split("_")[0].split(",");
   var lastfix = attachFile.split("_")[1];
   var fileNames = lastfix.split("*");   
   for(var i=0; i<fileNames.length; i++){
     if(fileNames[i].toLowerCase().indexOf(".mht")!=-1){      
				return profix[i]+"_"+fileNames[i];
     }
   }
}

function isHaveMht(attachFile){
  var flag = findMht(attachFile);
  if(flag){
     return true;
  }
  return false;
}*/
function findMht(attachFile, attachFileIds){
	   var profix = attachFile.split("_")[0].split(",");
	   var lastfix = attachFile.split("_")[1];
	   var fileNames = lastfix.split("*");  
	 
	   var attId = attachFileIds.split(",");
	   for(var i=0; i<fileNames.length; i++){
	     if(fileNames[i].toLowerCase().indexOf(".mht")!=-1){   
	    	 if(attId[i]){
	           ym = attId[i].substring(0, attId[i].indexOf("_"));
	         }
				return profix[i]+"_"+fileNames[i];
	     }
	   }
	}

	function isHaveMht(attachFile, attachFileIds){
	  var flag = findMht(attachFile, attachFileIds);
	  if(flag){
	     return true;
	  }
	  return false;
	}

function fw_notify(seqId)
{
	 if(window.confirm("确认要转发该公告通知吗？")){
	   window.location = contextPath+"/core/funcs/notify/manage/notifyAdd.jsp?isFw=1&seqId="+seqId;
	 }
}

function zhuanfa_notify(seqId,isFw)
{
 URL= contextPath + "/core/funcs/notify/show/readNotify.jsp?isManage=1&seqId="+seqId;
 myleft=(screen.availWidth-780)/2;
 mytop=100
 mywidth=780;
 myheight=500;
 if(format=="1")
 {
    myleft=0;
    mytop=0
    mywidth=screen.availWidth-15;
    myheight=screen.availHeight-60;
 }
 
 window.open(URL,"read_news","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function SetCwinHeight(){   
	     var H=$("#myiframe").contents().find("meta").attr("content");   
	     alert(H);   
	     $("#myiframe2").height(H);  
}    
</script>

</head>
<body onload="doInit();" style="overflow:auto;">
 <div id="hasData">
 <table class="TableBlock" width="100%">
    <tr height="45px;">
      <td class="TableHeader" align="center"><span id="subject" style=""></span></td>
    </tr>
    <tr>
      <td class="TableContent" align="right" id="title">
      </td>
    </tr>
    <tr>
      <td class="TableData Content" height="150" id="content">
    
      </td>
    </tr>
    <tr align="left">
     <td class="TableData" id="showAttachment">
        <input type="hidden" id="attachmentId" name="attachmentId">
        <input type="hidden" id="attachmentName" name="attachmentName">
        <input type="hidden" id="ensize" name="ensize">
         <input type="hidden" id="moduel" name="moduel" value="notify">
        <span id="showAtt">
        </span>
      </td>
     </tr>
    <tr align="center" class="TableControl">
      <td>
        <span id="send">
        <input type="button" class="BigButton" value="转发" onClick="fw_notify('<%=seqId%>');">
        </span>
        <span id="close">
        <input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
        </span>
         <span id="return" style="display:none">
        <input type="button" value="返回" class="BigButton" onClick="window.location.href='/yh/core/funcs/notify/show/index.jsp'">
        </span>
      </td>
    </tr>
  </table>
  </div>
  <div id="noData" align=center style="display:none">
   <table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="msgInfo" class="msg info">该公告已删除。
            </td>
        </tr>
    </tbody>
</table>
</div>
</body>
</html>