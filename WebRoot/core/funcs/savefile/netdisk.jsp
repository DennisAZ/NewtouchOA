<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String attachName=(String)request.getAttribute("attachName");
	if(attachName==null){
	  attachName="";
	}
	String attachId=(String)request.getAttribute("attachId");
	if(attachId==null){
	  attachId="";
	}
	String module=(String)request.getAttribute("module");
	if(module==null){
		module="";
	}
	
	String subjectValue="";
	if(attachName!=null && attachName.lastIndexOf(".")!=-1 ){
		subjectValue=attachName.substring(0,attachName.lastIndexOf("."));
	}

%>


<%
	List<Map<String, String>> list=(List<Map<String, String>>)request.getAttribute("diskList");
	int start  = 1;
	int sizeStr  = 0;
	if(list!=null){
	  start = 0;
	  sizeStr=list.size();
	}
	
	int seqId=(Integer)request.getAttribute("seqId");
	String diskPath=(String)request.getAttribute("diskPath");
	String parentPath=(String)request.getAttribute("parentPath");
	if(diskPath==null){
	  diskPath="";
	}
	if(parentPath==null){
	  parentPath="";
	}else{
		parentPath = parentPath.replace("'","\\\'");
	}
	
	

%>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="yh.core.funcs.system.netdisk.data.YHNetdisk"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>网络硬盘</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
//alert('<%=attachId%>');
//alert('<%=attachName%>');

var attNameStr="<%=attachName%>";
var attachNameEncode=encodeURIComponent(attNameStr);
//alert("attachNameEncode>>>"+attachNameEncode);

var parentPathStr="<%=parentPath%>";
//alert("parentPathStr>>>"+parentPathStr);
var parentPathStrEncode=encodeURIComponent(parentPathStr);
//alert("attachNameEncode>>>"+parentPathStrEncode);


var requestURL="<%=contextPath%>/yh/core/funcs/system/netdisk/act/YHNetdiskAct";

function checkSend(){
	var radioArry=document.getElementsByName('radioStr');
	var radioStr="";
	for(var i=0;i<radioArry.length;i++){
		if(radioArry[i].checked){
			//alert(radioArry[i].value);
			radioStr = radioArry[i].value;
		}
	}
	return radioStr;
}

function send(){
	var subject = $("subject").value;
	//alert(subject);
	var radioStr=checkSend();
	if(radioStr!=""){

		$("checkId").value=radioStr;
		$("form1").submit();
		
		//alert(radioStr);
		//var url=contextPath + '/core/funcs/savefile/netdiskSubmit.jsp?module=<%=module%>&attachId=<%=attachId%>&attachName=<%=attachName%>&diskPath=' + radioStr + "&subject=" + subject;
		//location.href=encodeURI(url);
	}else{
		alert("请选择要转存至的目录");
	}
	
}

function comeBack(){
	var returnPath='<%=parentPath%>';
	//alert("returnPath>>"+returnPath);
  if(returnPath!=""){
	  history.back();
  }else{
		location.href="<%=contextPath %>/core/funcs/savefile/index.jsp?module=<%=module%>&attachId=<%=attachId%>&attachName=" +attachNameEncode;
  }    

}

function nextTest(seqId,diskPath,attachId ,parentPath,module){
	//alert(diskPath);
	window.location.href=requestURL + "/getNetDiskList.act?seqId="+seqId+"&diskPath=" + encodeURIComponent(diskPath) + "&attachId=" + attachId +"&attachName="+ attachNameEncode + "&module="+module;
}
</script>
</head>
<body onload="">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/save_file.gif" align="middle"><span class="big3">文件转存 — <%=attachName %></span>
    </td>
    </tr>
</table>

<form id="form1" name="form1" action="<%=contextPath%>/core/funcs/savefile/netdiskSubmit.jsp" method="post" >
<table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableHeader">
      <img src="<%=imgPath%>/green_arrow.gif" align="middle" WIDTH="20" HEIGHT="18"> 请选择转存目录：

      </td>
    </tr>
    <tr class="TableData" height="60">
      <td>
      <%
      	if(!"".equals(parentPath)){      	
      %>
      		<a href="javascript:location.href=requestURL + '/getNetDiskList.act?seqId=<%=seqId%>&diskPath=' + parentPathStrEncode + '&returnFlag=back&attachId=<%=attachId %>&attachName='+ attachNameEncode + '&module=<%=module %> ' "><img src="<%=imgPath%>/parent.gif" align="middle" border="0"> 返回上级目录</a><br>
      
      <%}if(list!=null && list.size()!= 0){
      	  for(Map<String,String> diskMap:list){      	            
      	  	String dirPathStr = diskMap.get("diskPath");
      	  	dirPathStr = dirPathStr.replace("'","\\\'");
      	  	
      %>     
      <script>
      //alert('<%=diskMap.get("diskPath") %>');
      //var dirPathStr="<%=diskMap.get("diskPath")%>";
     // var dirPathStrEncode=encodeURIComponent(dirPathStr);
      //alert(dirPathStrEncode);
      </script>
      
          <input type="radio" name="radioStr" id="DISK_<%=diskMap.get("seqId") %>" value="<%=diskMap.get("diskPath") %>"><label for="DISK_<%=diskMap.get("seqId") %>" title="将文件转移至【<%=diskMap.get("diskName") %>】"><%=diskMap.get("diskName") %></label> 
          <a href="javascript:" onclick="nextTest(<%=diskMap.get("seqId") %>,'<%=dirPathStr %>','<%=attachId %>','<%=parentPath%>','<%=module %>');" title="进入【<%=diskMap.get("diskName") %>】">下一级</a><br>
      <%
      
      	  }
      	  
      	}	  
      	      
      %>       
        
    	</td>
    </tr>
    <tr class="TableData">
      <td>转存文件名：<input type="text" name="subject" id="subject" value="<%=subjectValue %>" class="SmallInput" size="40">
      </td>
    </tr>
    <tr>
      <td nowrap align="center" class="TableControl">
        <input type="hidden" name="attachId" value="<%=attachId %>">
        <input type="hidden" name="attachName" value="<%=attachName %>">
        <input type="button"  value="上一步" class="SmallButtonW" onClick="comeBack();">&nbsp;&nbsp;
        <%
        	if(sizeStr!=0){        	        
        %>
        <script type="text/javascript">
	      	var radioArry=document.getElementsByName('radioStr');
	      	radioArry[0].checked=true;
        </script>
        <input type="button"  value=" 转存 " id="tranfer" onclick="send();" class="SmallButtonW">&nbsp;&nbsp;
        <%
        	}
        %>
        
        <input type="hidden" name="attachId" value="<%=attachId %>">
        <input type="hidden" name="attachName" value="<%=attachName %>">
        <input type="hidden" name="module" value="<%=module %>">
        <input type="hidden" name="checkId" id="checkId" value="">
        
        
        <input type="button"  value=" 关闭 " class="SmallButtonW" onClick="window.close();">
      </td>   
    </tr>
</table>
</form>

</body>
</html>