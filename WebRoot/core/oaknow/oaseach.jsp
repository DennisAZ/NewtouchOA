<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="yh.core.oaknow.data.*"%>
<%@ page  import="yh.core.oaknow.util.*"%>
<html>
<head>
<title>OA知道</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/wiki.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<style type="text/css">
  span.highlight{background-color:yellow}
  .on{background-color:white}
</style>
<% 
  YHPageUtil pu = (YHPageUtil)request.getAttribute("page");
  List<YHOAAsk> askList =(List<YHOAAsk>) request.getAttribute("askList");
  String key  = (String)request.getAttribute("flag");
  String askName = (String)request.getAttribute("askName"); 
%>
<script type="text/javascript">
//	function  pager(flag, askName, currNo){
//		var param ="question="+encodeURI(encodeURI(askName))+"&currNo="+currNo+"&flag="+flag;		
//		window.location.href = "<%=contextPath%>/yh/core/oaknow/act/YHOASeachAct/findResolveStatus.act?" + param;
//	}
 
	function  pager(flag, askName, currNo){
		var val = $("textValue").value;
   	if(val == ""|| val==null){ 
   		alert("搜索内容不能为空！");
   		document.getElementById("textValue").focus();
   		return;
   	}else{
			$("flag").value = flag;	    
			$("currNo").value = currNo;
			document.getElementById("form1").submit();
   	}
	}

</script>
<script type="text/javascript">
   function gotoIndex(){
		window.location.href = contextPath + "/yh/core/oaknow/act/YHOAKnowAct/OAKnowIndex.act";
  }

   function checkVal(){
   	var val = $("textValue").value;
   	if(val == ""|| val==null){ 
   		alert("搜索内容不能为空！");
   		document.getElementById("textValue").focus();
   		return;
   	}else{
   		document.getElementById("form1").submit();
   	}
    } 
</script>
</head>
<body topmargin="5" onload="">
<div class="bodydiv">
<br />
<div class="searchbody">
	<!--search begin  -->  
   <form name="form1" id="form1" action='<%=contextPath%>/yh/core/oaknow/act/YHOASeachAct/findResolveStatus.act' method='post'>
	 <div class="searchcss">
	 	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size:14px;">
    <tr>
	      <td><a onclick="javascript:gotoIndex();" class="logo">${oaName}</a>
	      	<input id="textValue" type="text" onmouseover="this.focus()" onfocus="this.select()" name="question" class="" size="35" value="<%=askName%>"/>
	 	      <input type="button" value="搜 索"  onclick="checkVal();" class="BigButton" />
	 	      <input type="button" value="提 问" onclick=location.href="<%=contextPath%>/core/oaknow/ask.jsp" class="BigButton" />
                                     &nbsp;&nbsp;<a href="<%=contextPath%>/yh/core/oaknow/act/YHOAKnowPanelAct/mainPanel.act">管理面板</a>
          <input type="hidden" value="" name="currNo" id="currNo"/>
          <input type="hidden" value="resolve" name="flag" id="flag"/>
	     </td>
	  </tr>
	  </table>
	 </DIV>
	</form>
	<!--search end  -->
	<div class="navbar">
		<a href="<%=contextPath%>/yh/core/oaknow/act/YHOAKnowAct/OAKnowIndex.act">${oaName}</a>&nbsp;&raquo;&nbsp;搜索结果
	</div>
  <div>
     <DIV id=line></DIV>
     <%if(key.equals("'resolve'")){%>
	     <DIV id=sub>
	     	  <SPAN class=tbk1>&nbsp;</SPAN>
	     	  <SPAN>已解决问题</SPAN>
	     	  <SPAN class=tbk3>&nbsp;</SPAN>
	     	  <A id="noresolve" href="javascript:pager('noresolve','','');">待解决问题</A>
	     	  <SPAN class=tbk2>&nbsp;</SPAN>
	     </DIV>       
     <%} else if(key.equals("'noresolve'")){%>
       <DIV id=sub>
     	  <A id="resolve" href="javascript:pager('resolve','','');">已解决问题</A>
     	  <SPAN class=tbk2>&nbsp;</SPAN>
     	  <SPAN class=tbk1>&nbsp;</SPAN>
     	  <SPAN>待解决问题</SPAN>
     	  <SPAN class=tbk3>&nbsp;</SPAN>
     </DIV>       
     <%}%>
    <!--   <DIV id=sub>
     	  <SPAN class=tbk1>&nbsp;</SPAN>
     	  <A id="resolve" href="javascript:pager('resolve','${askName}','');">已解决问题</A>
     	  <SPAN class=tbk3>&nbsp;</SPAN>
     	  <A id="noresolve" href="javascript:pager('noresolve','${askName}','');">待解决问题</A>
     	  <SPAN class=tbk2>&nbsp;</SPAN>
     </DIV>-->     
     <DIV style="margin-top:6px;">
     <%
     	if(askList == null || askList.size() == 0){
     %>
                抱歉，没有找到与“<span class="highlight">${askName}</span>” 相关的资料。
     <%  
     	}else{
     	  for(int i=0; i<askList.size(); i++){
     %>
     		<TABLE cellSpacing=0 cellPadding=0 border=0>
     		<TR>
     			 <TD class="searchtitle">
     			 		<a href="<%=contextPath%>/yh/core/oaknow/act/YHOAAskReferenceAct/findAskRef.act?askId=<%=askList.get(i).getSeqId()%>" font-size="20px"><%=askList.get(i).getAsk() %></a>
     			 		<span class="bodyspan">
     			 		     <%
     			 		     		if(YHStringUtil.isNotEmpty(askList.get(i).getAskComment())){
     			 		     	%>
     			 		     		<br><%=askList.get(i).getAskComment()%>
     			 		     	<%	  
     			 		     		}if(YHStringUtil.isNotEmpty(askList.get(i).getAnswer())){
     			 		     		  %>
     			 		     		   <br><%=askList.get(i).getAnswer()%>
     			 		     		  <%
     			 		     		}
     			 		     %>     			 		    
     			 		</span>
     			 </TD>
     		</TR>
     		</TABLE>
     <%	    
     	  }
     	}
     %>
     </DIV>
     <div class="pagebar"><!-- 分页 -->
   <%
   	 if(pu.getCurrentPage()>1){
   %>
   	<a href=javascript:pager(${flag},'',1)>首页</a>&nbsp;&nbsp;<a href=javascript:pager(${flag},'',<%=pu.getCurrentPage()-1%>)>上一页&nbsp;&nbsp;</a>
   <%
   	 }
     if(pu.getCurrentPage() -4 >0){
       for(int no = pu.getCurrentPage()-4; no<pu.getCurrentPage(); no++){
         %>
         	<a href=javascript:pager(${flag},'',<%=no%>)><%=no%></a>&nbsp;&nbsp;
         <%
       }       
     }else{
       for(int no=1; no<pu.getCurrentPage(); no++){
         %>
        	<a href=javascript:pager(${flag},'',<%=no%>)><%=no%></a>&nbsp;&nbsp;
        <%
       }
     } 
     if(pu.getPagesCount()>1){
     %>
     		<a href=javascript:pager(${flag},'',<%=pu.getCurrentPage()%>)>[<%=pu.getCurrentPage()%>]</a>
     <%   
     }
     if(pu.getCurrentPage()+5 < pu.getPagesCount()){
       for(int no2= pu.getCurrentPage()+1; no2<pu.getCurrentPage()+5; no2++){
         %>
         	<a href="javascript:pager('${flag}','',<%=no2%>)；"><%=no2%></a>&nbsp;&nbsp;
         <%
       }
     }else{
       for(int no2=pu.getCurrentPage()+1; no2<=pu.getPagesCount(); no2++){
         %>
        	<a href=javascript:pager(${flag},'',<%=no2%>)><%=no2%></a>&nbsp;&nbsp;
        <%
       }
    }
     if(pu.getCurrentPage() < pu.getPagesCount()){
       %>
       <a href=javascript:pager(${flag},'',<%=pu.getCurrentPage()+1 %>)>下一页</a>&nbsp;
       <a href=javascript:pager(${flag},'',<%=pu.getPagesCount() %>)>末页</a>
       <%
     }
   %>
  </div>
  </div>

</div>
</div>
<input type="hidden" name="REMINDACT" id="REMINDACT" value="" />
<div id="overlay"></div>
<div id="p" class="loginbox" style="width:402px;height:250;"></div>
</body>
</html>