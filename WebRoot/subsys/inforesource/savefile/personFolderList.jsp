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
	List<Map<String, String>> list=(List<Map<String, String>>)request.getAttribute("fileSortList");
	int sizeStr  = 0;
	if(list!=null){
	  sizeStr=list.size();
	  //System.out.print(sizeStr);
	}
	
	
	int inIt=(Integer)request.getAttribute("inIt");
	
	int parentId=(Integer)request.getAttribute("parentId");
	int seqId=(Integer)request.getAttribute("seqId");
	
	



%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.net.URLEncoder"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人文件柜</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/const.js'></script>
<script type="text/javascript">
var attNameStr="<%=attachName%>";
var attachNameEncode=encodeURIComponent(attNameStr);
function sendForm(){
	//var subject = $("subject").value;
	//alert(subject);
	var radioStr=checkSend();
	if(radioStr){
		$("checkId").value=radioStr;
		//alert("checkId>>>"+radioStr);
		$("form1").submit();
	}else{
		alert("请选择要转存至的目录");
	}
	
}


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

function comeBack(){
	var returnInit='<%=inIt%>';
  if(returnInit!=0){
	  history.back();
  }else{
		location.href="<%=contextPath %>/subsys/inforesource/savefile/index.jsp?module=<%=module%>&attachId=<%=attachId%>&attachName=" +attachNameEncode;
  }    

}
</script>
</head>
<body  topmargin="5" leftmargin="0">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/save_file.gif" align="middle"><span class="big3">个人文件柜-批量转存 </span>
    </td>
    </tr>
</table>

<form name="form1" id="form1" action="<%=contextPath%>/subsys/inforesource/savefile/personFolderSubmit.jsp">
<table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableHeader">
      <img src="<%=imgPath%>/green_arrow.gif" align="middle" WIDTH="20" HEIGHT="18"> 请选择转存目录：
      </td>
    </tr>
    <tr class="TableData" height="60">
      <td> 
      <%
      	if(inIt!=0){
      %>  
    	<%--<a href="javascript:location.href=encodeURI(requestURL + '/getPersonFolderInfo.act?module=<%=module %>&backFlag=back&seqId=<%=parentId %>&parentId=&attachId=<%=attachId %>&attachName=<%=attachName %> ') "><img src="<%=imgPath%>/parent.gif" align="middle" border="0">返回上级目录</a><br> --%>
    	<a href="javascript:location.href=requestURL + '/getPersonFolderInfo.act?module=<%=module %>&seqId=<%=parentId %>&attachName='+attachNameEncode + '&attachId=<%=attachId %> ' "><img src="<%=imgPath%>/parent.gif" align="middle" border="0">返回上级目录</a><br>
    	
	    <%	}	if(list!=null && list.size()>0){
	 		  		int temp = 0;
	      		for(int i = 0 ;i<list.size();i++){
	      		  Map<String,String> sortMap = list.get(i);
      		    String check = "";
      		    temp  = temp + 1;
      		    if(inIt==0&&temp==2){
      		      check = " checked";
      		    }else if(temp==1){
      		      check = " checked";
      		    }
      		    String hrefStr="";
      		    
  	        	if(!"0".equals(sortMap.get("seqId"))){
  	        	  //hrefStr="<a href=\"javascript:location.href=encodeURI(requestURL + '/getPersonFolderInfo.act?seqId=" + sortMap.get("seqId")+"&attachId="+attachId +"&attachName="+attachName +"&module="+module + " ')\" title=\"将文件转移至【" + sortMap.get("sortName")  +"】\" > 下一级 </a>";
  	        	  hrefStr="<a href=\"javascript:location.href=requestURL + '/getPersonFolderInfo.act?seqId=" + sortMap.get("seqId")+"&attachId="+attachId +"&attachName='+attachNameEncode+'&module="+module + "'\" title='将文件转移至【" + sortMap.get("sortName")  +"】' > 下一级 </a>";
  	        	}
	  	        
	  	        
      %>        
  		
	        <input type="radio" name="radioStr"  id="SORT_<%=sortMap.get("seqId") %>"  value="<%=sortMap.get("seqId") %>" <%=check %>>
	        <label for="SORT_<%=sortMap.get("seqId")%>" title="将文件转移至【<%=sortMap.get("sortName") %>】"><%=sortMap.get("sortName") %>	        
	        </label>  <%=hrefStr %>   <br>
	   <%
	      		    
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
   <!--<tr class="TableData">
    <%
    	String attachNameStr="";
    	if(attachName.lastIndexOf(".")!=-1){
    	  attachNameStr=attachName.substring(0,attachName.lastIndexOf("."));
    	  
    	}
    
    %>
    
      <td>转存文件名：<input type="text" name="subject" id="subject" value="<%=attachNameStr %>" class="SmallInput" size="40">
      </td>
    </tr>  --> 
    <tr>
      <td nowrap align="center" class="TableControl">
        <input type="button"  value="上一步" class="SmallButtonW" onClick="comeBack();">&nbsp;&nbsp;
				
				<%
					if(sizeStr!=0){
				%>
			
        <input type="button"  value=" 转存 " onclick="sendForm();" class="SmallButtonW">&nbsp;&nbsp;
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