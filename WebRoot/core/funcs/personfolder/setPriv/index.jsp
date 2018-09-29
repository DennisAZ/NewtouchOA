<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 

<%
	String seqId=request.getParameter("seqId");
	if(seqId==null){
	  seqId="";
	}	

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>共享设置</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
var requestURL = "<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFolderAct";
//alert("seqId>>"+'<%=seqId%>');
var seqId='<%=seqId%>';
function sendForm(){
  var shareUserId = document.getElementById("user").value;
  var manageUserId = document.getElementById("user1").value;  
  //alert("shareUserId>>"+shareUserId +" manageUserId>>"+manageUserId);
  var url = requestURL + "/setSharePriv.act?sortId=<%=seqId%>&shareUserId=" + shareUserId + "&manageUserId=" + manageUserId;
	var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    alert('设置权限成功！');

    prcsJson = rtJson.rtData;
    var shareFlag=prcsJson.shareFlag;

    //alert("shareFlag>>"+shareFlag);
   	var curTree = parent.frames["file_tree"].tree;  		
   	var curNode = curTree.getNode(seqId); 
   	//alert(curNode.nodeId);
    if(shareFlag == 1){
    	curNode.imgAddress="<%=imgPath%>/endnode_share.gif";
    	//curNode.name = $F('sortName');
    	/*
    	var nodeId = prcsJson[0].nodeId;
    	var nodeName = prcsJson[0].sortName;
    	
    	var imgAddress = "<%=imgPath%>/dtree/folder.gif";
    	var node = {
    			parentId:curNode.parentId,
    			nodeId:nodeId,
    			name:nodeName,
    			isHaveChild:curNode.isHaveChild,
    			extData:'',
    			imgAddress:imgAddress
    	}*/
    	//curTree.updateNode(curNode.nodeId, curNode);  
    	
    	curTree.updateNode(curNode.nodeId, curNode);    

    	//location='<%=contextPath%>/core/funcs/personfolder/setPriv/result.jsp';

    	//alert(curNode.nodeId +"  curNode>>"+curNode );
    }else{
      curNode.imgAddress="<%=imgPath%>/dtree/folder.gif";
      //alert(curNode.nodeId +"  curNode>>"+curNode );
      curTree.updateNode(curNode.nodeId, curNode);   
    }
    //window.location.reload();	

  
    //window.location.href="<%=contextPath%>/core/funcs/personfolder/setPriv/result.jsp";
    //location="<%=contextPath%>/core/funcs/personfolder/setPriv/result.jsp";
    
  }else{
		alert(rtJson.rtMsrg);
  }		
  
}


function doInit(){
  var url = requestURL + "/getPrivUserName.act?seqId=<%=seqId%>";
	var json = getJsonRs(url);
	//alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  prcsJson = json.rtData;
  //alert("prcsJson>>"+prcsJson);
  if(prcsJson.shareUser != "null"){
    document.getElementById("user").value = prcsJson.shareUser;
    document.getElementById("userDesc").value = prcsJson.shareUserDesc;
  }
  if(prcsJson.manageUser != "null"){
    document.getElementById("user1").value = prcsJson.manageUser;
    document.getElementById("userDesc1").value = prcsJson.manageUserDesc;
  }
  
}



</script>
</head>
<body onload="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big3"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle">共享设置</span>
    </td>
  </tr>
</table>
<form action=""  method="post" name="form1">
 <table class="TableBlock" width="100%" align="center">
    <tr>
      <td nowrap class="TableData"> 共享范围：</td>
	   	 <td class="TableData">
	      <input type="hidden" name="user" id="user" value="">
	      <textarea cols=40 name="userDesc" id="userDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
	      <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
	      <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
	   </td>      
    </tr>
    <tr>
      <td nowrap class="TableData"> 修改权限：</td>
      <td class="TableData">
			  <input type="hidden" name="user1" id="user1" value="">
	      <textarea cols=40 name="userDesc1" id="userDesc1" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
	      <a href="javascript:;" class="orgAdd" onClick="selectUser(['user1', 'userDesc1']);">添加</a>
	      <a href="javascript:;" class="orgClear" onClick="$('user1').value='';$('userDesc1').value='';">清空</a>
	   	</td> 
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap >
        <input type="hidden" name="FILE_SORT" value="2">
        <input type="hidden" name="SORT_ID" value="52">
        <input type="submit" value="确定" class="BigButton" onclick="sendForm();">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/core/funcs/personfolder/folder.jsp?seqId=<%=seqId %>'">
      </td>
    </tr>
  </table>
</form>




</body>
</html>