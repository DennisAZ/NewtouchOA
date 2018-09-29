<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String treeId = request.getParameter("treeId");
  if(treeId == null){
    treeId = "";
  }
  String userName = request.getParameter("userName");
  if (userName == null){
    userName = "";
  }
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门管理</title>
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/dtree1.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript">
var userName = encodeURI("<%=userName%>");
var seqId = "<%=seqId%>";
var tree = null;
function doInit(){
  var flag = getMyManageArea(seqId);  //有大的管辖区域
  showTree('dtreeId'); //展示详细的管辖区域
  if(!flag){
  	alert('该人员还未有管辖区域');
  	return false; //没有这是return
  }
}

var d = null;
function showTree(type)
{
	var	url = contextPath + '/yh/core/funcs/person/act/YHUserPrivAct/getManageAreas1.act?userId=' + seqId;
    d = new dTree('d');
    
	 jQuery.ajax({
	   type : 'GET',
	   url : url,
	   success : function(json_data){ 
	   var data2 = eval("(" + json_data + ")");
	   if(data2.length > 0){
	    	jQuery(data2).each(function(i,item){  
			 var NodeId = item.nodeId;
			 var Parent_id = item.parentId;
			 var area_name = item.name;
			 var isChecked = item.isChecked;
			 d.add(NodeId,Parent_id,area_name,"","","","","",false,isChecked);
 			});
	   }else{
	   		alert("当前树还没有形成，当前添加的默认为根节点，请先添加树根节点名称！");
	   		return ;
	   }
 		document.getElementById(type).innerHTML = d;
 		disappear(type); //调用显示这棵树
	   }
	 });
}


//获取该人员管辖的区域
function getMyManageArea(seqId){
	var reFlag = false;
	var	url = contextPath + '/yh/core/funcs/person/act/YHUserPrivAct/getMyManageArea.act?seqId=' + seqId;
    
	 jQuery.ajax({
	   type : 'GET',
	   url : url,
	   async:false,
	   success : function(json_data){ 
	   if(json_data == '' || json_data == null){
	   	 return reFlag;
	   }
	    var data2 = eval("(" + json_data + ")");
		    if(data2.length > 0){
		    	jQuery(data2).each(function(i,item){  
	   				jQuery("#management_area").val(item.management_area);
	   				jQuery("#remark").val(item.remark);
	 			});
	 			reFlag = true;
		   }else{
		   		alert("该人员还未有管辖区域！");
		   		reFlag = false;
		   }
	   }
	 });
	 return reFlag;
}

  function  display(type){
   	document.getElementById(type).style.display = "block";
   }
   
   function  disappear(type){
   	document.getElementById(type).style.display = "none";
   }
   
var checkboxIds = new Array();
function doSubmit(){
	jQuery("input[type=checkbox]").each(function(i,item){
		  	if(this.checked == true){
		  		checkboxIds.push(item.value);
		  }
 	});
 	
  if(jQuery("#management_area").val() == ""){ 
    alert("请填写管辖区域名称！");
    jQuery("#management_area").focus();
    jQuery("#management_area").select();
    return false;
  }
  var userId = jQuery("#userId").val();
  var userName = jQuery("#userName").val();
  var management_area = jQuery("#management_area").val();
  var remark = jQuery("#remark").val();
  var param = "";
  param = "checkboxIds="+checkboxIds+"&userId="+userId+"&userName="+encodeURI(userName)+"&management_area="+encodeURI(management_area)+"&remark="+encodeURI(remark);
 	if(checkboxIds != null && checkboxIds != ''){
 		
 		var url = contextPath + "/yh/core/funcs/person/act/YHUserPrivAct/saveManageAreaInfo.act?"+param;
 		
 		jQuery.ajax({
		   type : 'GET',
		   url : url,
		   success : function(json_data){ 
		   var data2 = eval("(" + json_data + ")");
		   if(data2.state == '0'){
			    window.location.href = contextPath + "/crm/customer/manageAreaSet/submit.jsp";
		   }else{
			   	alert('设置失败');
			    return ;
		   }
		   }
		  });
 	}else{
 	    alert("请选择管辖的详细区域！");
 		return false;
 	}
}
window.onscroll = window.onresize = function(){
  var opBtn = document.getElementById("OP_BTN");
  if(!opBtn){
    return false;
  } else {
    opBtn.style.left = (document.documentElement.clientWidth + document.documentElement.scrollLeft - 150) + 'px';
    opBtn.style.top = (document.documentElement.scrollTop + 25) + 'px';
  }
};

</script>
</head>
<body onload="doInit()">


<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;设置人员管辖区域－(<%=userName%>)</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="55%" align="center">
   <tr>
      <td nowrap class="TableData">管辖区域名称：<font style='color:red'>*</font></td>
      <td class="TableData" colspan=3>
       <input type="text" name="management_area" id="management_area" size="20" class="BigInput" >
        <a href="javascript:;" class="orgAdd" onClick="display('dtreeId');">添加详细区域</a>
      </td>
    </tr>
     <tr>
    <td nowrap class="TableData">备注：</td>
    <td class="TableData" colspan=3>
      <textarea name="remark" id="remark" cols="62" rows="3" class="BigInput" value=""></textarea>
    </td>
  </tr>
    
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
      	<input type="hidden" name="userId" id="userId" value="<%=seqId %>">
      	<input type="hidden" name="userName" id="userName" value="<%=userName %>">
        <input type="button" value="保存" class="BigButton" onclick="doSubmit();">
      </td>
    </tr>
  </table>
</form>

 <div id="dtreeId" class="dtree" style="overflow-x:auto;overflow-y:auto;overflow: auto; display: none;"></div>
 
</body>
</html>