<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.core.oaknow.data.*"%>
<html>
<head>
<title>OA知道</title>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/wiki.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<%
	YHCategoriesType aType = (YHCategoriesType)request.getAttribute("aType");//分类列表
	int selfId = (Integer)(request.getAttribute("selfId"));
	String cont = (String)request.getAttribute("showFlag");
%>
<script type="text/javascript">
function gotoDiv(typeId, flag,crrNo){
  var param = "typeId="+typeId+"&&"+"flag="+flag +"&currNo="+crrNo;
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/oaknow/act/YHOAKnowTypeAct/findTypeAjax.act",param);
  var jsonParse = rtJson["rtData"].length; 
  var temp = "";  
  if(jsonParse != 0){
     for(i=0; i<jsonParse; i++){
      var url="<%=contextPath%>/yh/core/oaknow/act/YHOAAskReferenceAct/findAskRef.act?askId=";
      var em = url+rtJson.rtData[i].seqId;
			temp += "<li class = \"showli\">•&nbsp;"; 
			temp += "<a href=\""+em+"\">";
			temp += rtJson["rtData"][i]["ask"];
			temp += "</a>";
			temp += "</li>"
     }
     var totalNo = rtJson["totalNo"];
	   var currNo = rtJson["currNo"];
	   if(totalNo >= 1){
	     createPage(currNo, totalNo, typeId,flag);
	   }
   }else{
			temp += "<li class = \"showli\">•&nbsp;无记录</li>";
			$('pagebar').innerHTML = "";
   } 
   $('show').innerHTML = temp;  
}

function clickDiv(tag1, tag2, tag3, typeId, flag){
  var name = "'"+tag1+"'";
	var name2 = "'"+tag2+"'";
  var name3 = "'"+tag3+"'";
	gotoDiv(typeId, flag, 1);    //flag=1全部，2已解决，0未解决 ，1表示第一页
	$(tag1).removeClassName('top top_off');	
	$(tag2).removeClassName($(tag2).className);	
	$(tag3).removeClassName($(tag3).className);	
	$(tag1).addClassName('top top_on');              
	 
  $(tag2).addClassName('top top_off');             
	$(tag3).addClassName('top top_off');  
}

function jump(selfId, parentId){
	window.location.href = "/yh/yh/core/oaknow/act/YHOAKnowTypeAct/findType.act?typeId="+ selfId + "&parentId="+parentId;			
}

//分页
function createPage(currNo, totalNo, typeId, flaga){
  var pageNo="";
  var flag = 1;
  if(flaga === "" || flaga === "undefined"){
    flag = $("flagid").value;
  }else{
	  flag = flaga;
  }
  if(currNo > 1){
		pageNo += "<a href=javascript:gotoDiv('"+ typeId+"',"+ flag +",1);>首页</a>&nbsp;&nbsp;";
		pageNo += "<a href=javascript:gotoDiv('"+ typeId+"',"+ flag+","+(currNo-1)+")>上一页</a>&nbsp;&nbsp;";
  }
  if(currNo - 4 >0){
	  for(var no = currNo - 4; no< currNo; no++){    //当前页前面的页数
	   pageNo += "<a href=javascript:gotoDiv('" + typeId +"',"+ flag+","+no+");>"+ no + "</a>"+"&nbsp;&nbsp;";
	  }
  }else{
    for(var no =1; no< currNo; no++){    //当前页前面的页数
 	   pageNo += "<a href=javascript:gotoDiv('"+ typeId +"',"+ flag +","+no+");>"+ no + "</a>"+"&nbsp;&nbsp;";
 	  }
  }
  if(totalNo >1){
   pageNo += "<a href=\"javascript:gotoDiv('"+ typeId +"',"+flag +","+ currNo +");\">"+"【"+ currNo +"】"+ "</a>&nbsp;&nbsp;";
  }
  if(currNo+5 < totalNo){
	  for(var no2 = currNo +1; no2< currNo+5; no2++){
	   pageNo += "<a href=javascript:gotoDiv('"+ typeId +"',"+ flag+","+no2+");>"+ no2 + "</a>&nbsp;&nbsp;";
	  }
  }else{
    for(var no2 = currNo +1; no2<= totalNo; no2++){
 	   pageNo += "<a href=javascript:gotoDiv('"+ typeId +"',"+ flag+","+no2+");>"+ no2 + "</a>&nbsp;&nbsp;";
 	  }
  }
  if(currNo < totalNo){
    pageNo += "<a href=javascript:gotoDiv('"+ typeId +"',"+ flag+","+(currNo+1)+");>下一页</a>&nbsp;&nbsp;";
		pageNo += "<a href=javascript:gotoDiv('"+ typeId +"',"+ flag+","+ totalNo + ");>末页</a>";
  }
  $('pagebar').innerHTML = pageNo;
}

function getBack(){
	var showFlag='<%=cont%>'; 
	if(showFlag == 'CONTENT'){	
		top.dispDesk();
	}else{
	  history.back();
	}
	
}

</script>

</head>
<body  topmargin="5" onload="gotoDiv(<%=selfId%>, 1, 1)">
<div class="bodydiv">
<br /><div class="askbody">
	<!--search begin  -->
	 <%@ include file="head2.jsp" %> 
	<!--search end  -->
	<div class="navbar">
		<a onclick="javascript:gotoIndex();" href="#">${oaName}</a>
			<%
			  if(selfId == aType.getSeqId()){
			   %>
			   &nbsp;&raquo;&nbsp;<%=aType.getName()%>
			   <% 
			  }else	if(aType.getList()!=null && aType.getList().size() >0){
				  %>
				  &nbsp;&raquo;&nbsp;<a href="javascript:void(0);" onclick="javascript:jump('<%=aType.getSeqId() %>','<%=aType.getPearentId() %>');return false;"><%=aType.getName() %></a>
				  <%
				  for(int i=0; i<aType.getList().size(); i++){
				    if(selfId == aType.getList().get(i).getSeqId()){
				      %>
				      &nbsp;&raquo;&nbsp;<%=aType.getList().get(i).getName()%>
				      <%
				    }
				  }
				}
			%>	
		</div>
  <div class="showclass">
		<div class="bg">
		  <div class="rg1"></div>
		  <div class="rg2"></div>
		   <div class="t1"><a class="lbk"><%=aType.getName()%></a></div><!-- 父分类 -->
		   <div class="bc">
		   <div class="bai" style="padding-bottom:2px;">
		    <%
		     if(aType.getList()!=null && aType.getList().size() >0){
		    	for(int i=0; i<aType.getList().size(); i++){
		    %>		    	
		       &#8226;&nbsp;<a href="javascript:void(0)" style="font-size:14px;" onclick="javascript:jump('<%=aType.getList().get(i).getSeqId()%>','<%=aType.getList().get(i).getPearentId()%>');return false;"><%=aType.getList().get(i).getName()%></a>&nbsp;&nbsp;&nbsp;&nbsp;	<!-- 加链接 -->	   	
		    <%
		    }		    	
		     }else{
		       %>没有分类<%
		     }
		    %>
		    </div>
		  </div>
		  <div class="bai">
			   <div class="rg3"></div>
			   <div class="rg4"></div>
		   </div>
	  </div>
	</div>
  <div class="showclass">
  	 <div class="leave" style="height:410px;">
				<div class="leave1">
				   <div class="top top_on" id="allctrl" onclick="clickDiv('allctrl','kedctrl','wilctrl',<%=selfId %>,1);"><a><span>全部问题</span></a></div>
				   <div class="top top_off" id="kedctrl" onclick="clickDiv('kedctrl','allctrl','wilctrl',<%=selfId %>,2);"><a><span>已解决</span></a></div>
				   <div class="top top_off" id="wilctrl" onclick="clickDiv('wilctrl','allctrl','kedctrl',<%=selfId %>,0);"><a><span>未解决</a></span></div>
				   <div class="top1 top_off" id="blankctrl"></div>
				</div>
				<div class="box"  style="display:'';height:370px;">
				
						<ul id="show">												
							
						</ul>
				</div>
				<div class="pagebar" id="pagebar" ></div>
		 </div>
  </div>
 </div>
</div>
<input type="hidden" name="flagid" id="flagid" value="1" />
<div id="overlay"></div>
<div id="p" class="loginbox" style="width:402px;height:250;"></div>
<br><center><input type="button" class="BigButton" value="返回" onclick="javascript:getBack();return false;"></center>
</body>
</html>
