<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.core.oaknow.data.*"%>
<%@ page  import="yh.core.oaknow.util.*"%>
<html>
<head>
<title>我的问题</title>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/wiki.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript">
function  pager(currNo){
	var param ="currNo="+currNo;		
	window.location.href = "<%=contextPath%>/yh/core/oaknow/act/YHOAKnowPanelAct/leftPanel.act?" + param;
}

function deleteAsk(askId){
  var msg='确认要删除该问题吗?';
  if(window.confirm(msg)){
    var param = "askId="+ askId;
    var url = "<%=contextPath %>/yh/core/oaknow/act/YHOAKnowPanelAct/deleteAsk.act?"+param;
    window.location.href = url;
  }
}
</script>
    <%
    List<YHOAAsk> asks = (List<YHOAAsk>)request.getAttribute("asks");
    YHPageUtil pu = (YHPageUtil)request.getAttribute("page");
    %>   
</head>
<body class="mbodycolor" topmargin="5">
<div class="gt">我的问题管理</div>
<div style="text-align: center;">
       <%
       	 if(asks != null && asks.size() != 0){
       	   
       	     %>
   <TABLE class="tlists">
       <TR class="header">

         <TD width="55%">问题</TD>
         <TD width="20%">提问时间</TD>
         <TD width="10%">状态</TD>
         <TD width="15%">操作</TD>   
       </TR>
           <%for(int i =0; i< asks.size(); i++){ %>
       	     	<TR>
					       <TD class="tctd tctd1">&nbsp;<a href="<%=contextPath%>/yh/core/oaknow/act/YHOAAskReferenceAct/findAskRef.act?askId=<%=asks.get(i).getSeqId()%>" target="_blank"><%=asks.get(i).getAsk() %></a></TD>
					       <TD class="tctd"><%=asks.get(i).getCreateDateStr()%></TD>
					       
					       		<%if(asks.get(i).getStatus()==0){%>					       		 
					       		  <TD class="tctd"><img src="<%=contextPath%>/core/styles/oaknow/images/no.gif" title="问题未解决" /> </TD>
					       		  <TD class="tctd">
								       	 <a href="<%=contextPath%>/yh/core/oaknow/act/YHOAKnowPanelAct/goToEditPage.act?askId=<%=asks.get(i).getSeqId()%>">编辑</a>
								         <a href="javascript:void(0);" onclick="javascript:deleteAsk(<%=asks.get(i).getSeqId()%>); return false;">删除</a>
					            </TD>
					       		 <% 
					       		}else if(asks.get(i).getStatus() == 1){
					       		  %>
					       		   <TD class="tctd"><img src="<%=contextPath%>/core/styles/oaknow/images/ok.gif" title="问题已解决" /></TD>
					       		   <TD class="tctd">无操作 </TD>
					       		  <%
					       		}
					       		%> 
             </TR>
       	     <%
       	   }       	         
       %>
   </TABLE>
   <%}else{
     %>
     	<table align="center" width="340" class="MessageBox">
         <tbody>
          <tr>
				    <td class="msg info">
				      <h4 class="title" align="left">提示</h4>
				      <div style="font-size: 12pt;" class="content">没有问题！</div>
				    </td>
          </tr>
        </tbody>
      </table>  
     <%
     
   } %>
</div>
<div class="pagebar"><!-- 分页 -->
   <%
   	 if(pu.getCurrentPage()>1){
   %>
   	<a href=javascript:pager(1)>首页</a>&nbsp;&nbsp;<a href=javascript:pager(<%=pu.getCurrentPage()-1%>)>上一页&nbsp;&nbsp;</a>
   <%
   	 }
     if(pu.getCurrentPage() -4 >0){
       for(int no = pu.getCurrentPage()-4; no<pu.getCurrentPage(); no++){
         %>
         	<a href=javascript:pager(<%=no%>)><%=no%></a>&nbsp;&nbsp;
         <%
       }       
     }else{
       for(int no=1; no<pu.getCurrentPage(); no++){
         %>
        	<a href=javascript:pager(<%=no%>)><%=no%></a>&nbsp;&nbsp;
        <%
       }
     } 
     if(pu.getPagesCount()>1){
     %>
     		<a href=javascript:pager(<%=pu.getCurrentPage()%>)>[<%=pu.getCurrentPage()%>]</a>
     <%   
     }
     if(pu.getCurrentPage()+5 < pu.getPagesCount()){
       for(int no2= pu.getCurrentPage()+1; no2<pu.getCurrentPage()+5; no2++){
         %>
         	<a href=javascript:pager(<%=no2%>)><%=no2%></a>&nbsp;&nbsp;
         <%
       }
     }else{
       for(int no2=pu.getCurrentPage()+1; no2<=pu.getPagesCount(); no2++){
         %>
        	<a href=javascript:pager(<%=no2%>)><%=no2%></a>&nbsp;&nbsp;
        <%
       }
    }
     if(pu.getCurrentPage() < pu.getPagesCount()){
       %>
       <a href=javascript:pager(<%=pu.getCurrentPage()+1 %>)>下一页</a>&nbsp;
       <a href=javascript:pager(<%=pu.getPagesCount() %>)>末页</a>
       <%
     }
   %>
  </div>
</body>
</html>