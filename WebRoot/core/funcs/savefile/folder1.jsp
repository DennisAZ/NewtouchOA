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

%>

<%
	int sortId=(Integer)request.getAttribute("seqId");
	int inIt=(Integer)request.getAttribute("inIt");
	int parentId=(Integer)request.getAttribute("parentId");

	List<Map<String, String>> list=(List<Map<String, String>>)request.getAttribute("fileSortList");
	int start  = 1;
	int sizeStr  = 0;
 	if(list!=null){
 	 	start = 0;
  	sizeStr=list.size();
 	}


%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="yh.core.funcs.system.filefolder.data.YHFileSort"%>
<%@page import="yh.core.funcs.system.filefolder.act.YHFileSortAct"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.Map"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公共文件柜</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
var requestURL="<%=contextPath%>/yh/core/funcs/system/filefolder/act/YHFileSortAct";

var attNameStr="<%=attachName%>";
var attachNameEncode=encodeURIComponent(attNameStr);
//alert("attachNameEncode>>>"+attachNameEncode);



function checkSend(){
	var radioArry=document.getElementsByName('radioStr');
	var radioStr=0;
	for(var i=0;i<radioArry.length;i++){
		if(radioArry[i].checked){
			radioStr = radioArry[i].value;
		}
	}
	return radioStr;
}

function send(){
	var subject = $("subject").value;
	var radioStr=checkSend();
	if(radioStr!=0){
		$("checkId").value=radioStr;
		$("form1").submit();
	}else{
		alert("请选择要转存至的目录");
	}
	
}

function comeBack(){
	var returnSortId='<%=sortId%>';
	//alert("returnInit>>"+returnInit +"  returnSortId>>"+returnSortId);
  if(returnSortId!=0){
	  history.back();
  }else{
		location.href="<%=contextPath %>/core/funcs/savefile/index.jsp?module=<%=module%>&attachId=<%=attachId%>&attachName=" +attachNameEncode;
  }    

}


</script>
</head>
<body onload="" topmargin="5" leftmargin="0">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/save_file.gif" align="middle"><span class="big3"> 文件转存 — <%=attachName%></span>
    </td>
    </tr>
</table>

<form id="form1" name="form1" action="<%=contextPath%>/core/funcs/savefile/folder1Submit.jsp">
<table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableHeader">
      <img src="<%=imgPath%>/green_arrow.gif" align="middle" WIDTH="20" HEIGHT="18"> 请选择转存目录：
      </td>
    </tr>
    <tr class="TableData" height="60">
      <td>   
      <%      	
      	if(sortId!=0 || inIt==1){       	    
      %>  
       	<a href="javascript:location.href=requestURL + '/getFolderInfo.act?module=<%=module %>&backFlag=back&seqId=<%=sortId%>&parentId=<%=parentId%>&attachName=' +attachNameEncode + '&attachId=<%=attachId %> ' "><img src="<%=imgPath%>/parent.gif" align="middle" border="0">返回上级目录</a><br>
	      	
	    <%}	if(list!=null && list.size()>0){
	 		  		int temp = 0;
	      		for(int i = 0 ;i<list.size();i++){
	      		  Map<String,String> sortMap = list.get(i);
	     
	      		  if("1".equals(sortMap.get("visitFlag")) && "1".equals(sortMap.get("newFlag")) ){  
	      		    String check = "";
	      		    temp  = temp + 1;
	      		    if(temp==1){
	      		      check = " checked";
	      		    }	      		   
      %>        
  		
	        <input type="radio" name="radioStr"  id="SORT_<%=sortMap.get("seqId") %>"  value="<%=sortMap.get("seqId") %>" <%=check %>>
	        <label for="SORT_<%=sortMap.get("seqId")%>" title="将文件转移至【<%=sortMap.get("sortName") %>】"><%=sortMap.get("sortName") %></label> <a href="javascript:location.href=requestURL + '/getFolderInfo.act?module=<%=module %>&seqId=<%=sortMap.get("seqId")%>&parentId=<%=sortMap.get("sortParent")%>&attachName=' + attachNameEncode + '&attachId=<%=attachId %> ' " title="进入【<%=sortMap.get("sortName") %>】">下一级</a><br>
	    	 	
	    <%   
	      		  } else if("1".equals(sortMap.get("visitFlag")) && !"1".equals(sortMap.get("newFlag"))){
	      		    
	    %>
	      		     
		    	<input type="radio" name="radioStr"  id="SORT_<%=sortMap.get("seqId") %>" value="<%=sortMap.get("seqId") %>" disabled="disabled">
	        <label for="SORT_<%=sortMap.get("seqId")%>" title="将文件转移至【<%=sortMap.get("sortName") %>】"><%=sortMap.get("sortName") %></label> <a href="javascript:location.href=requestURL + '/getFolderInfo.act?module=<%=module %>&seqId=<%=sortMap.get("seqId")%>&parentId=<%=sortMap.get("sortParent")%>&attachName=' +attachNameEncode + '&attachId=<%=attachId %> ' " title="进入【<%=sortMap.get("sortName") %>】">下一级</a><br>
	      		    
	   <%
	      		    
	      		  } 
 					}    	  
      	}else{
			
			%>
			<div>
				<table class="MessageBox" align="center" width="190">
  				<tr>
  					<td class="msg blank">
      			<div class="content" style="font-size:12pt">无可访问目录</div>
    				</td>
  				</tr>
				</table>			
			</div>
		<%
      	}  
		%>
      </td>    
      
    </tr>
    <tr class="TableData">
      <td>转存文件名：<input type="text" name="subject" id="subject" value="<%=attachName.substring(0,attachName.lastIndexOf(".")) %>" class="SmallInput" size="40">
      </td>
    </tr>
    <tr>
      <td nowrap align="center" class="TableControl">
        <input type="button"  value="上一步" class="SmallButtonW" onClick="comeBack();">&nbsp;&nbsp;
				
				<%
					if(sizeStr!=0){
				%>
			
        <input type="button"  value=" 转存 " onclick="send();" class="SmallButtonW">&nbsp;&nbsp;
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