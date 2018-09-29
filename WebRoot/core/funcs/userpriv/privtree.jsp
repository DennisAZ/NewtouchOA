<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String treeId = request.getParameter("treeId");
  if(treeId == null){
    treeId = "";
  }
  String userId = request.getParameter("userId");
  if(userId == null){
    userId = "";
  }
  String privNo = request.getParameter("privNo");
  if (privNo == null){
    privNo = "";
  }
  String privName = request.getParameter("privName");
  if (privName == null){
    privName = "";
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
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/src/cmp/DTree1.0.js"></script>
<script type="text/javascript">
var userId = "<%=userId%>";
var privNo = "<%=privNo%>";
var privName = encodeURI("<%=privName%>");
var seqId = "<%=seqId%>";
var tree = null;
function doInit(){
  var p = {bindToContainerId:'di'
         ,isOnceLoad:true
		 ,requestUrl:contextPath + '/yh/core/funcs/person/act/YHUserPrivAct/getNoTreeOnce.act?userId=' + seqId
		 ,checkboxPara:{isHaveCheckbox:true}
		 ,treeStructure:{isNoTree:true,regular:'2,2,2'}
		 ,isFolder:true	
  };
  tree =  new DTree(p);
  tree.show();
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/getPrivTreeData.act?userId=" + seqId;
  var rtJson = getJsonRs(url, null);
  if(rtJson.rtState == "0"){
    bindJson2Cntrl(rtJson.rtData);
  }else{
	alert(rtJson.rtMsrg); 
  }
}

function doSubmit(){
  var formDiv = document.getElementById("formDiv");
  var show = document.getElementById("show");
  //var seqId = 0;
  //document.getElementById("seqId").value;
  var dtoClass = "yh.core.funcs.dept.data.YHUserPriv";
  var positionSeqId = userId;
  
  //var seqId = document.getElementById("seqId").value;
  var id = tree.getCheckedList();
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/insertPriv.act?privNo=" + privNo;
  var rtJson = getJsonRs(url,"funcIdStr=" + id + "&dtoClass=" + dtoClass + "&seqId=" + seqId+ "&privName=" + encodeURIComponent(privName));
  if(rtJson.rtState == "0"){
    //$("form1").reset();
    //bindJson2Cntrl(rtJson.rtData);dtoClass
    //formDiv.style.display="none";
    //show.style.display="";
    //alert(rtJson.rtMsrg);
    location.href = '<%=contextPath %>/core/funcs/userpriv/manage.jsp';
  }else{
	alert(rtJson.rtMsrg);
  }
}
function test2(id){  
}
function test1(id){
  var obj = tree.getNode(id);
  var parentId = obj.parentId;
  var deptParent = '无';
  if(parentId != '0'){
  	deptParent = tree.getNode(parentId).name;
  }
  window.parent.deptinput.location = "<%=contextPath%>/core/funcs/dept/mulindex.jsp?treeId=" + id + "&deptParent=" + deptParent + "&deptLocal=" + obj.name; 
}

function commit(){
  //document.getElementById("positionFlag") = document.getElementById("pre4");
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptPositionAct/insertPriv.act?treeId=" + treeId;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if(rtJson.rtState == "0"){
    //$("form1").reset();
    //bindJson2Cntrl(rtJson.rtData);
    alert(rtJson.rtMsrg);
  }else{
	alert(rtJson.rtMsrg);
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
 <div id="deplist"></div>
 <div id="formDiv">
 <form name="form1" id="form1">
 <input type="hidden" name="seqId" id="seqId" value="">
  <input type="hidden" name="positionSeqId" id="positionSeqId" value="">
 <table id="treeSave">
 <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="hidden" name="DEPT_ID" value="1">
    </td>
    <tr/>
 </table>
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" width="20" HEIGHT="22"><span class="big3"> 编辑角色权限－(<%=privName%>)</span>&nbsp;&nbsp;
     <div id="OP_BTN" style="width:150px;top:5px;right:20px;position:absolute;">
    <input type="button" value="保存" class="BigButtonA" title="保存" name="button" onclick="doSubmit();">&nbsp;&nbsp;
    <input type="button" value="返回" class="BigButtonA" onclick="location='manage.jsp'">
    </div>
    </td>
  </tr>
 </table>
 </form>
 </div>
 <div align="center" style="display:none" id="show">
 <table class="MessageBox" align="center" width="410">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">岗位人员信息已保存修改</div>
    </td>
  </tr>
 </table>
 </div>
</body>
</html>