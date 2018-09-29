<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="yh.core.oaknow.data.*"%>
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
<%
  int count = (Integer)request.getAttribute("count");
  List<YHOAKnowUser> users = (List<YHOAKnowUser>)request.getAttribute("users");
  int hadResolvedCont = (Integer)request.getAttribute("hadResolvedCont");//已经解决的个数
  int hadNoResolvedCont = (Integer)request.getAttribute("hadNoResolvedCont");//待解决的问题
  List<YHCategoriesType>  types = (List<YHCategoriesType>)request.getAttribute("types");//问题分类
  List<YHOAAsk> askList  = (List<YHOAAsk>)request.getAttribute("askList");              //精彩问题推荐  
  List<YHOAAsk> noResolveAskList  = (List<YHOAAsk>)request.getAttribute("noResolvedList");     //为解决的问题
  List<YHOAAsk> resolvedList = (List<YHOAAsk>)request.getAttribute("resolvedList");
  List<YHOAAsk> myAsk  = (List<YHOAAsk>)request.getAttribute("myAsk");
%>
<script type="text/javascript">
function jump(selfId, parentId){
  var url = "<%=contextPath%>/yh/core/oaknow/act/YHOAKnowTypeAct/findType.act?typeId=";
	window.location.href = url+ selfId + "&parentId="+parentId;			
}
</script>
</head>
<body>
<table width="800">
	<tr align="center">
		<td colspan="2">
	<!--search begin  -->
	 <%@ include file="indexhead.jsp" %> 
	<!--search end  -->	
		</td>
  </tr>
	<tr align="center">
	<td  valign="top" width=210>		
  <!--left begin  -->
  <DIV id="left"> 
  <!--right begin  -->
  <DIV class="rarea">
    <DIV class="by bai" id=masterBox>
      <DIV class=ry1></DIV>
      <DIV class=ry2></DIV>
      <DIV class=t1><A class=lbk><span class="jifen">&nbsp;&nbsp;&nbsp;&nbsp;积分榜</span></A></DIV>
      <DIV class="bc4 agry">
         <DIV class="info" style="PADDING-LEFT: 12px;PADDING-RIGHT: 12px;PADDING-TOP: 6px;COLOR: #008800;">注册用户数：<%=count%></DIV>
         <HR style="MARGIN-LEFT: 4px;MARGIN-RIGHT: 4px;COLOR: #008800;BORDER-BOTTOM: #d1eaf6 1px solid;height:1px;" />
         <DIV class=zmr>         
           
             <% if(users!=null && users.size()!=0){%>
                <% for(int i=0; i<(users.size()>10?10:users.size()); i++){%> 
              <div>
              <table>              
             		<tr>
             				<td class="zmrtd2 dot"><%=users.get(i).getName()%></td>
             				<td class="zmrtd1 dot">积分</td>
             				<td class="zmrtd3 dot"><%=users.get(i).getScore()%></td>
             		</tr>             			
             	</table>
		          </div>
             	<%} %>
             <%} else{%>             
              <div>
               <table>
             		<tr>
             				<td colspan="3" class="zmrtd2 dot">无积分用户</td>
             		</tr>
             	 </table>
		          </div>	
             <%}%>		      
		    </div>
      </div>
    </div>
  </div>
  <!--right begin  -->
     <div class=rarea>
       <div class=tj>
         <div class=tjl></div>
         <div class=tjr></div>
       </div>
       <div class=bb>
         <div class=title>问题分类</DIV>
  			<div class=bgc id=leftarea>
  				<div class=info>已解决问题数：<%= hadResolvedCont%><BR />待解决问题数：<%=hadNoResolvedCont %>&nbsp;&nbsp;</DIV>
  			  <div style="PADDING-LEFT: 5px; PADDING-TOP: 5px">
  			     <%
  			        if(types!=null && types.size()!=0){
  			           for(int i=0; i<types.size(); i++){%>
  			              <span class=f14B><a href="javascript:void(0)" onclick="javascript:jump('<%=types.get(i).getSeqId()%>','<%=types.get(i).getPearentId()%>'); return false;"><%=types.get(i).getName()%></A></SPAN><BR>
  			              <%
  			                if(types.get(i).getList()!= null && types.get(i).getList().size()!=0){
  			              		for(int j=0; j<types.get(i).getList().size(); j++){ %> 			              		  
  			              		  <a href="javascript:void(0)" onclick="jump('<%=types.get(i).getList().get(j).getSeqId()%>','<%=types.get(i).getList().get(j).getPearentId()%>'); return false;"><%=types.get(i).getList().get(j).getName()%></A>&nbsp;&nbsp;  			              		  
  			              	<%}
  			                }
  			              %>
  			              <br>
  			           <%}
  			        }else{%>
  			                        无分类
  			     <%
  			       }
  			     %> 
  			    <br class=brh>  			  
  			  </div>
  			</div>
       </div>
       <div class=bai >
          <div class=lj color="blue">
             <div class=lj1></div>
             <div class=rj1></div>
          </div>
       </div>
     </div>  
</div>   
</td>
<td  valign=top width=570>
  <!--center begin  -->
  <div class="carea">
  	<div>
     	<div class="bg">
     		<div class="rg1"></div>
     		<div class="rg2"></div>
     		<div class="t1"><span class="hedicon3">&nbsp;&nbsp;&nbsp;&nbsp;</span><a class="lbk">&nbsp;&nbsp;精彩问题推荐</a></div>
     		<div class="bc">
     			<div class="bai">     
     			  <ul style="margin:2px;">
			      <%
			      	if(askList.size() !=0 ){
			      	  for(int i=0; i< (askList.size()>10?10:askList.size()) ; i++){
			      %>
			          
			          		<li class="showli1">&#8226;&nbsp;<a href="<%=contextPath%>/yh/core/oaknow/act/YHOAAskReferenceAct/findAskRef.act?askId=<%=askList.get(i).getSeqId()%>"><%=askList.get(i).getAsk()%></a>&nbsp;&nbsp;
			          		[<a class="lgy" href="javascript:void(0)" onclick="jump('<%=askList.get(i).getTypeId() %>','<%=askList.get(i).getParentId() %>'); return false;"><%=askList.get(i).getCategoryName() %></a>]</li>
			          
			      <%	     
			      	  }			      	  
			      	}else{
			      %>
			         无推荐问题
			      <%	  			      	  
			      	}
			      %>	
			      </ul>			      
			    </div>
		    </div>
		    <div class="bai">
			    <div class="rg3"></div>
			    <div class="rg4"></div>
		    </div>
	    </div>
  	</div>
 
  	<div>
     	<div class="bg">
     		<div class="rg1"></div>
     		<div class="rg2"></div>
     		<div class="t1"><span class="hedicon2">&nbsp;&nbsp;&nbsp;&nbsp;</span><a class="lbk">&nbsp;&nbsp;我的问题</a></div>
     		<div class="bc">
     			<div class="bai">
			       <ul style="margin:2px;">
			        <%
			      	if(myAsk.size() !=0 ){
			      	  for(int i=0; i< (myAsk.size()>5?5:myAsk.size()); i++){
			      	    
			      %>
			          		<li class="showli1">&#8226;&nbsp;<a href="<%=contextPath%>/yh/core/oaknow/act/YHOAAskReferenceAct/findAskRef.act?askId=<%=myAsk.get(i).getSeqId()%>" ><%=myAsk.get(i).getAsk()%></a>
			          		    <% if(myAsk.get(i).getAskCount()!=0){%>(<%=myAsk.get(i).getAskCount() %>)<%} %>&nbsp;&nbsp;
			          		    [<a class="lgy" href="javascript:void(0)" onclick="jump('<%=myAsk.get(i).getTypeId() %>','<%=myAsk.get(i).getParentId() %>'); return false;"><%=myAsk.get(i).getCategoryName() %></a>]</li>
			          
			      <%	     
			      	  }			      	  
			      	}else{
			      %>
			         无问题
			      <%	  			      	  
			      	}
			      %>
			     </ul>
			    </div>
		    </div>
		    <div class="bai">
			    <div class="rg3"></div>
			    <div class="rg4"></div>
		    </div>
	    </div>
  	</div>
 
  	<div>
     	<div class="bg">
     		<div class="rg1"></div>
     		<div class="rg2"></div>
     		<div class="t1"><span class="hedicon2">&nbsp;&nbsp;&nbsp;&nbsp;</span><a class="lbk">&nbsp;&nbsp;待解决的问题</a></div>
     		<div class="bc">
     			<div class="bai">
     			  <ul style="margin:2px;">
			      <%
			      	if(noResolveAskList.size() !=0 ){
			      	  for(int i=0; i< (noResolveAskList.size() >5?5:noResolveAskList.size()); i++){
			      	   
			      %>
			          
			          		<li class="showli1">&#8226;&nbsp;<a href="<%=contextPath%>/yh/core/oaknow/act/YHOAAskReferenceAct/findAskRef.act?askId=<%=noResolveAskList.get(i).getSeqId()%>" >
			          		           <%=noResolveAskList.get(i).getAsk()%></a>
			          		           <% if(noResolveAskList.get(i).getAskCount()!=0){%>(<%= noResolveAskList.get(i).getAskCount()%>)<%} %>&nbsp;&nbsp;
			          		           [<A class="lgy" href="javascript:void(0)" onclick="jump('<%=noResolveAskList.get(i).getTypeId() %>','<%=noResolveAskList.get(i).getParentId() %>'); return false;"><%=noResolveAskList.get(i).getCategoryName() %></A>]</li>
			          
			      <%	     
			      	  }			      	  
			      	}else{
			      %>
			         无待解决问题
			      <%	  			      	  
			      	}
			      %>
			     </ul>
			    </div>
		    </div>
		    <div class="bai">
			    <div class="rg3"></div>
			    <div class="rg4"></div>
		    </div>
	    </div>
  	</div>
 
  	<div>
     	<div class="bg">
     		<div class="rg1"></div>
     		<div class="rg2"></div>
     		<div class="t1"><span class="hedicon1">&nbsp;&nbsp;&nbsp;&nbsp;</span><a class="lbk">&nbsp;&nbsp;最近解决问题</a></div>
     		<div class="bc">
     			<div class="bai">
     			  <ul style="margin:2px;">
			         <%
			      	if(resolvedList.size() !=0 ){
			      	  for(int i=0; i< (resolvedList.size() >15?15:resolvedList.size()); i++){
			      	   
			      %>
			          
			          		<li class="showli1">&#8226;&nbsp;<a href="<%=contextPath%>/yh/core/oaknow/act/YHOAAskReferenceAct/findAskRef.act?askId=<%=resolvedList.get(i).getSeqId()%>"><%=resolvedList.get(i).getAsk()%></a>&nbsp;&nbsp;
			          		[<a class="lgy" href="javascript:void(0)" onclick="jump('<%=resolvedList.get(i).getTypeId() %>','<%=resolvedList.get(i).getParentId() %>'); return false;"><%=resolvedList.get(i).getCategoryName() %></a>]</li>
			          
			      <%	     
			      	  }			      	  
			      	}else{
			      %>
			         无已解决问题
			      <%	  			      	  
			      	}
			      %>
			     </ul> 
			    </div>
		    </div>
		    <div class="bai">
			    <div class="rg3"></div>
			    <div class="rg4"></div>
		    </div>
	    </div>
  	</div>
  </div>
  <!--center end  -->
		</td>
  </tr>
</table>
</div>
<input type="hidden" name="REMINDACT" id="REMINDACT" value="" />
<div id="overlay"></div>
<div id="p" class="loginbox" style="width:402px;height:250;"></div>
</body>
</html>
