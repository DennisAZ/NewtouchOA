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
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/const.js'></script>
<script type="text/javascript">
var attNameStr="<%=attachName%>";
var attachNameEncode=encodeURIComponent(attNameStr);
var parentPathStr="<%=parentPath%>";
var parentPathStrEncode=encodeURIComponent(parentPathStr);
function checkSend(){
	var radioArry=document.getElementsByName('radioStr');
	var radioStr="";
	for(var i=0;i<radioArry.length;i++){
		if(radioArry[i].checked){
			radioStr = radioArry[i].value;
		}
	}
	return radioStr;
}

function send(){
	var radioStr=checkSend();
	if(radioStr!=""){
		$("checkId").value=radioStr;
		$("form1").submit();
	}else{
		alert("请选择要转存至的目录");
	}
	
}

function comeBack(){
	var returnPath='<%=parentPath%>';
  if(returnPath!=""){
	  history.back();
  }else{
    location.href= contextPath + "/subsys/inforesource/savefile/index.jsp?module=<%=module%>&attachId=<%=attachId%>&attachName=" +attachNameEncode;
  }    

}

</script>
</head>
<body onload="">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/save_file.gif" align="middle"><span class="big3">网络硬盘—批量转存</span>
    </td>
    </tr>
</table>

<form id="form1" name="form1" action="<%=contextPath%>/subsys/inforesource/savefile/netdiskSubmit.jsp" method="post" >
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
      %>     
      <script>
      var dirPathStr="<%=diskMap.get("diskPath")%>";
      var dirPathStrEncode=encodeURIComponent(dirPathStr);
      </script>
      
          <input type="radio" name="radioStr" id="DISK_<%=diskMap.get("seqId") %>" value="<%=diskMap.get("diskPath") %>"><label for="DISK_<%=diskMap.get("seqId") %>" title="将文件转移至【<%=diskMap.get("diskName") %>】"><%=diskMap.get("diskName") %></label> 
          <a href="javascript:location.href=requestURL + '/getNetDiskList.act?seqId=<%=diskMap.get("seqId") %>&diskPath=' + dirPathStrEncode + '&attachId=<%=attachId %>&attachName='+ attachNameEncode + '&module=<%=module %> ' " title="进入【<%=diskMap.get("diskName") %>】">下一级</a><br>
      <%
      
      	  }
      	  
      	}	  
      	      
      %>       
        
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
        
        
        <input type="button"  value="关闭" class="SmallButtonW" onClick="window.close();">
      </td>   
    </tr>
</table>
</form>

</body>
</html>