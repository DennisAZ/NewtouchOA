<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.oa.officeProduct.officeType.data.*"%> 
<html>
<head>
<%

  List<YHOfficeDepository> officeDep = (List<YHOfficeDepository>)request.getAttribute("findOfficeDepS");
  
  
%>
<title>办公用品库设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
 //增加办公用品库
function addNew(){
	window.location.href= contextPath + "/subsys/oa/officeProduct/officeType/new.jsp";
}
//删除单个办公用品库设置
function deleteDepository(seqId){
	
		   var msg ="确定要删除该类别吗？";
		   if(!window.confirm(msg)){
		   }else{
		     //var noHiddenId = document.getElementById("noHiddenId").value;
		     if(seqId && seqId!="null"){
		       window.location.href = contextPath + "/yh/subsys/oa/officeProduct/officeType/act/YHOfficeDepositoryAct/delOfficeDepositoryInfo.act?HiddenId="+seqId;  
		       return true;
		      }
		   }  
}
//修改单个办公用品库设置
function updateDepository(seqId){
	 var url  = contextPath + "/yh/subsys/oa/officeProduct/officeType/act/YHOfficeDepositoryAct/upOfficeDepositoryInfo.act?officeSeId="+ seqId;
	  window.location.href=url;
}

</script>
</head>
<body topmargin="5" class="bodycolor">
<table width="100%" cellspacing="0" cellpadding="3" border="0" class="small">
  <tbody><tr>
    <td class="Big"><img align="absmiddle" src="<%=imgPath %>/vote.gif"><span class="big3"> 办公用品库设置 </span>
    </td>
  </tr>
</tbody></table>
<table width="50%" align="center" name="1" class="TableList">
  <tbody><tr class="TableHeader">
      <td nowrap="" align="center">办公用品库</td>
      <td nowrap="" align="center">办公用品类别</td>
      <td nowrap="" align="center">所属部门</td>
      <td nowrap="" align="center">仓库管理员</td>
      <td nowrap="" align="center">物品调度员</td>
      <td nowrap="" align="center">操作</td>
    </tr>
  <%
 	for(int i=0; i<officeDep.size()&&officeDep.size()>0; i++){
 	  String deptName =	officeDep.get(i).getDeptId();
 	  String manager = officeDep.get(i).getManager();
 	  String proKeeper = officeDep.get(i).getProKeeper();
 	  if(deptName.length()>30){
 		  deptName = deptName.substring(0,30); 
 		  deptName = deptName+"...";
 	  }else{
 		  deptName = deptName.substring(0,deptName.length());
 	  }
 	 if(manager.length()>20){
 		  manager = manager.substring(0,20); 
		  manager = manager+"...";
	  }else{
		  manager = manager.substring(0,manager.length());
	  }
 	if(proKeeper.length()>20){
 		proKeeper = proKeeper.substring(0,20); 
 		proKeeper = proKeeper+"...";
	  }else{
		  proKeeper = proKeeper.substring(0,proKeeper.length());
	  }
 	  
 	%>
    <tr class="TableLine2">
      <td nowrap="" align="center"><%=officeDep.get(i).getDepositoryName() %></td>
      <td nowrap="" align="center"><%=officeDep.get(i).getOfficeTypeId()==null?"":officeDep.get(i).getOfficeTypeId() %></td>
      <td nowrap="" width="" align="center" title="<%=officeDep.get(i).getDeptId()==null?"":officeDep.get(i).getDeptId() %>"><%=deptName==null?"":deptName %></td>
      <td nowrap="" align="center" title="<%=officeDep.get(i).getManager()==null?"":officeDep.get(i).getManager() %>"><%=manager==null?"":manager %></td>
      <td nowrap="" align="center" title="<%=officeDep.get(i).getProKeeper()==null?"":officeDep.get(i).getProKeeper() %>"><%= proKeeper==null?"":proKeeper %></td>
       <td nowrap="" align="center">
      	<a href="javascript:void(0);" onclick="updateDepository(<%=officeDep.get(i).getSeqId() %>);">修改</a>
      	<a href="javascript:void(0);" onclick="deleteDepository(<%=officeDep.get(i).getSeqId() %>);">删除 </a>	
      </td>
    </tr>
  <%} %>
<tr>
	<td align="center" colspan="6" class="TableControl">
    <input type="button" value="增加办公用品库" onclick="addNew();" class="BigButtonC">
  </td>
</tr>
</tbody></table>
</body>
</body>
</html>