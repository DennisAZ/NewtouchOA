<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.inforesouce.data.*"%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<!-- 
<link rel="stylesheet" type="text/css" href="/yh/rad/dsdef/css/tableList.css"/>
 -->
<%
List<YHMateType> matas = (List<YHMateType>)request.getAttribute("mates");
request.setAttribute("mata",matas);
%>
<script>
function delete_board(BOARD_ID)
{
 msg='确认要删除该讨论区吗？这将删除该讨论区中的所有文章且不可恢复！';
 if(window.confirm(msg))
 {
    URL="delete.php?BOARD_ID="+BOARD_ID;
    window.location=URL;
 }
}

function gotojsp(){
  window.location.href = contextPath + "/subsys/inforesource/addelement.jsp";
}
function deleteBookType(seqId){
  var msg ="确定要删除该类别吗？";
  if(!window.confirm(msg)){
  }else{
    if(seqId && seqId!="null"){
     // document.form1.action = contextPath + "/yh/subsys/inforesouce/act/YHMateTypeAct/deleteSubMata.act";  
     // document.form1.submit(); 
      window.location.href = contextPath + "/yh/subsys/inforesouce/act/YHMateTypeAct/deleteSubMata.act?seqid="+ seqId;
      return true;
    }
  }
}
</script>
</head>

<body class="bodycolor" topmargin="5">

<!--<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 新建元数据</span><br>
    </td>
  </tr>
</table>

<div align="center">
<input type="button"  value="新建元数据" class="BigButton" onClick="gotojsp();return false;" title="新建">
</div>
--><br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3"> 管理</span><br>
    </td>
  </tr>
</table>
<br>
<div align="center">
 <form action="#" id="form1" method="get" name="form1" >
  <table class="TableList" width="85%">
 
    <tr class="TableHeader">
    	<td nowrap align="center">编号</td>
      <td nowrap align="center">中文名称</td>
      <td nowrap align="center">英文名称</td>
      <td nowrap align="center">定义</td>
      <td nowrap align="center">目的</td>
      <td nowrap align="center">约束性</td>
      <td nowrap align="center">可重复性</td>
	  <td nowrap align="center">元素类型</td>
	  <td nowrap align="center">数据类型</td>
	  <td nowrap align="center">操作</td>
    </tr>
   
   <%
   	if(matas!=null && matas.size()>0){
   	   for(int i=0; i<matas.size(); i++){
   	  //   System.out.println(matas.get(i).getConstraint());
   	  %>
   	  	<tr class="TableLine1">
		    <td align="center"><%=matas.get(i).getNumberId() %></td>
	      <td align="center"><%=matas.get(i).getcNname() %></td>
	      <td align="left"><%=matas.get(i).geteNname()==null||"null".equals(matas.get(i).geteNname())?"":matas.get(i).geteNname() %></td>
	      <td align="left"><%=matas.get(i).getDefine()==null||"null".equals(matas.get(i).getDefine())?"":matas.get(i).getDefine() %></td>
	      <td align="left"><%=matas.get(i).getAim()==null||matas.get(i).getAim()==""||"null".equals(matas.get(i).getAim())?"":matas.get(i).getAim() %></td>
		    <td align="left"><% if("1".equalsIgnoreCase(matas.get(i).getConstraint())){
				      %>
				        必须选
				      <%
				    }else if("2".equalsIgnoreCase(matas.get(i).getConstraint())){%>
				    	条件选
				    <%}else if("3".equalsIgnoreCase(matas.get(i).getConstraint())){
				      %>
				      可选
				      <%
				    }else{%>
				      --
				    <%}%>
		    
		    </td>
		    <td align="left">
		    	 <%if("1".equalsIgnoreCase(matas.get(i).getRepeat())){
				      %>
				               不可重复
				      <%
				    }else if("2".equalsIgnoreCase(matas.get(i).getRepeat())){%>
				    	可重复
				    <%}else{%>
				      --
				    <%}%>
		    </td>
		    <td align="left">
		    	 <%if("1".equalsIgnoreCase(matas.get(i).getElementId())){
				      %>
				               简单型
				      <%
				    }else if("2".equalsIgnoreCase(matas.get(i).getElementId())){%>
				    	容器型
				    <%}else if("3".equalsIgnoreCase(matas.get(i).getElementId())){
				      %>
				               符合型				      <%
				    }else{%>
				      --
				    <%}%>		    
		    </td>
		    <td align="left">
		    	<%if("1".equalsIgnoreCase(matas.get(i).getTypeId())){
				      %>
				               数字型				      <%
				    }else if("2".equalsIgnoreCase(matas.get(i).getTypeId())){%>
				    	字符型				    <%}else if("3".equalsIgnoreCase(matas.get(i).getTypeId())){%>
				                日期时间型
				    <%}else{
				      %>
				      --
				      <%
				    }%>
		    </td>  
	      <td nowrap align="center">
	      	<%//contextPath + "/core/inforesource
	      		if("1".equalsIgnoreCase(matas.get(i).getElementId())){  
	      	%>
	      		  <a href="<%=contextPath%>/yh/subsys/inforesouce/act/YHMateElementAct/selectvalue.act?seqid=<%=matas.get(i).getSeqId()%>&&number=<%=matas.get(i).getNumberId()%>">值域定义</a>	      		
	      		<%}else if("2".equalsIgnoreCase(matas.get(i).getElementId())){%>
	      		  <a href="<%=contextPath%>/yh/subsys/inforesouce/act/YHMateTypeAct/findSubMata.act?seqid=<%=matas.get(i).getSeqId()%>">子元素定义</a>	   
	      		<%}else if("3".equalsIgnoreCase(matas.get(i).getElementId())){%>
	      		  <a href="<%=contextPath%>/yh/subsys/inforesouce/act/YHMateElementAct/selectvalue.act?seqid=<%=matas.get(i).getSeqId()%>&&number=<%=matas.get(i).getNumberId()%>">值域定义</a> 	      		  
	      		  <a href="<%=contextPath%>/yh/subsys/inforesouce/act/YHMateTypeAct/findSubMata.act?seqid=<%=matas.get(i).getSeqId()%>">子元素定义</a>
	      		<%}%>
	      		<!--<a href="<%=contextPath%>/yh/subsys/inforesouce/act/YHMateTypeAct/deleteSubMata.act?seqid=<%=matas.get(i).getSeqId()%>">删除</a>	   
	      	 
	      	 --><a href="javascript:void(0);" onclick="deleteBookType(<%=matas.get(i).getSeqId()%>); return false;">删除</a>	
	      	 <a href="<%=contextPath%>/yh/subsys/inforesouce/act/YHMateTypeAct/editAMeta.act?seqid=<%=matas.get(i).getSeqId()%>">编辑</a>	 	          	         
	         <input type="hidden" id = "seqId" name ="seqid" value ="<%=matas.get(i).getSeqId()%>"></input>
	      </td> 
		    </tr>
   	  <%
   	}
   	}
   %>
 </table>
 </form>
</div>
</body>
</html>