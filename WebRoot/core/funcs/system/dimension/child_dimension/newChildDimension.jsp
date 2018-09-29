<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	if(seqId==null){
  	seqId="";
	}
	//out.print(seqId+":dddd");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建子维度</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>

<script Language="JavaScript"> 
var requestURL="<%=contextPath%>/yh/core/funcs/system/dimension/act/YHDimensionAct";
//alert('newFolder:<%=seqId%>');
function checkForm(){
  if(document.form1.sortName.value==""){
    alert("维度名称不能为空！");
    return (false);
  }
  return (true);
}

function sendForm(){
	var seqId='<%=seqId%>';
	var url=requestURL + "/addSubFolderInfo.act?seqId="+seqId;
	if(checkForm()){
		//alert("checkForm");
		var rtJson = getJsonRs(url,mergeQueryString($("form1")));
		//alert(rsText);
  	    if(rtJson.rtState == '0'){
    	  bindJson2Cntrl(rtJson.rtData);
    	  alert("添加成功！");
    	  //alert(rsText);
    	  //parent.location='../index.jsp';    	
    	  prcsJson = rtJson.rtData;
    	  //alert("rtJson.rtData:"+rtJson.rtData[0].nodeId+"  sortName:"+rtJson.rtData[0].sortName);
    	
    	  var curTree = parent.frames["file_tree"].tree;
    	  //alert("curTree:"+curTree);
    	    
    	  var curNode = curTree.getCurrNode();
    	 // alert("curTree:"+curNode);
    	  var nodeId = prcsJson[0].nodeId;
    	  var curNodeId = '';
          window.location.href = "newChildDimension.jsp?seqId="+seqId;
    	  curNodeId = seqId;
    	  var nodeName = prcsJson[0].sortName;
    	  //alert("nodeId :"+nodeId);
    	  //alert("nodeName :"+nodeName);
    	  //var opt = "";
    	  var imgAddress = "<%=imgPath%>/dtree/folder.gif";
    	  var node = {
    			parentId:curNodeId,
    			nodeId:nodeId,
    			name:nodeName,
    			isHaveChild:0,
    			extData:'',
    			imgAddress:imgAddress
    	  }
    	  curTree.addNode(node);   
    	
       }else{
  	 	  alert(rtJson.rtMsrg); 
  	   }	
   }
	//history.back(); 
}
function doInit(){
//alert(parent.frames["file_tree"].tree);
  var seqId = '<%=seqId%>';
 getDimensionName(seqId);
}
function getDimensionName(seqId){

  var url=requestURL + "/getSortNameById.act?seqId="+seqId;
  var json=getJsonRs(url);
  if(json.rtState == '1'){
	alert(json.rtMsrg);
	return ;				
  }

  prcsJson=json.rtData;
  var sortParent = json.rtMsrg;//父节点
  //document.getElementById("sortParent").value = sortParent;
  //是父节点就隐藏删除按钮
  if(sortParent=='0'){
    $("del").hide();
  }
  if(prcsJson.length>0){
	var prcs=prcsJson[0];
    $("sortParentName").value=prcs.sortName;
  }
  document.getElementById("seqId").value= seqId;
}
function delDimension(){
     var seqId = document.getElementById("seqId").value;
	 var curTree = parent.frames["file_tree"].tree;
	 //var obj = curTree.getFirstNode();
	 var parentId = "";
    var curNode = curTree.getCurrNode();
  	if(curNode==null){
        curNode = curTree.getNode(seqId);
      
    	}
    parentId =  curNode.parentId;
	 if(parentId==seqId){
       alert("父节点不能删除！");
       return;
     }

 	msg='确认要删除该维度吗？这将删除该维度中的所有子维度且不可恢复！';
 	if(window.confirm(msg)) {
 	  var url=requestURL + "/delDimensionInfoById.act?seqId="+seqId;
 	  var json=getJsonRs(url);
 	  if(json.rtState == '1'){
 		alert(json.rtMsrg);
 		return ;				
 	  }		


	 curTree.removeNode(seqId);
	 
	 //getDimensionName(parentId);
	 //删除后返回上级节点的id 
	 window.location.href="newChildDimension.jsp?seqId="+parentId;
   }
}
</script>
</head> 
<body class="" topmargin="5" onload="doInit();" > 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/filefolder/images/folder_new.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 新建子维度</span>
    </td>
  </tr>
</table>
 
<br>
<form action=""  method="post" name="form1" id="form1" onsubmit="">
<input type="hidden" value="yh.core.funcs.system.dimension.data.YHDimension" name="dtoClass">
<table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData"> 排序号：</td>
      <td class="TableData">
        <input type="text" name="sortNo" id="sortNo" size="20" maxlength="20" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 维度名称：</td>
      <td class="TableData">
        <input type="text" name="sortName" id="sortName" size="25" maxlength="100" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 上级维度名称：</td>
      <td class="TableData">
        <input type="text" name="sortParentName" id="sortParentName" size="25" maxlength="100" class="BigStatic" readonly >
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" name="seqId" id="seqId" value="<%=seqId %>"></input>
        <input type="button" value="确定" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
        <input type="button" id="del" value="删除" onclick="delDimension();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="parent.location='../index.jsp'">
      </td>
    </tr>
  </table>
</form>
 
</body>

</html>