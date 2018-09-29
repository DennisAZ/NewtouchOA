<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId == null) {
	seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加或修改菜单主分类</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
  var seqId = "<%=seqId%>";
  function doInit() {
    if(seqId){
      var url = "<%=contextPath%>/yh/core/funcs/menu/act/YHSysMenuAct/getSysMenu.act";
      var rtJson = getJsonRs(url, "seqId=" + seqId);
      if (rtJson.rtState == "0") {
        bindJson2Cntrl(rtJson.rtData);
        document.getElementById("menuIdOld").value = rtJson.rtData.menuId ;
      }else {
        alert(rtJson.rtMsrg); 
      }   
    }
  }

  function checkStr(str){ 
    var re=/[\\"']/; 
    return str.match(re); 
  }

  function check() {
    var cntrl = document.getElementById("menuId");
    if (!cntrl.value) {
  	  alert("主菜单分类代码不能为空！");
  	  cntrl.focus();
  	  return false;
    }
    if (!/[0-9]{2}/.test(cntrl.value)) {
  	  alert("主菜单分类代码必须为2位数字！");
  	  cntrl.focus();
  	  return false;
    }
    cntrl = document.getElementById("menuName"); 
    if (!cntrl.value.trim()) {
  	  alert("菜单名称不能为空！");
  	  cntrl.focus();
  	  return false;
    }

    if(checkStr($("menuName").value)){
      alert("您输入的部门名称含有'双引号'、'单引号 '或者 '\\' 请从新填写");
      $('menuName').focus();
      $('menuName').select();
      return false;
    }

    cntrl = document.getElementById("image"); 
    if (!cntrl.value.trim()) {
  	  alert("图片名不能为空！");
  	  cntrl.focus();
  	  return false;
    }
    
    if(checkStr($("image").value)){
      alert("您输入的部门名称含有'双引号'、'单引号 '或者 '\\' 请从新填写");
      $('image').focus();
      $('image').select();
      return false;
    }
    return true;
  }

  function goBack() {
    window.location.href = "<%=contextPath %>/core/funcs/menu/sysmenuinput.jsp";
  }
  
  function commitItem(){
    if(!check()) {
      return;
    }

    var url = null;
    var rtJson = null;
    if (seqId) {
      url = "<%=contextPath%>/yh/core/funcs/menu/act/YHSysMenuAct/updateSysMenu.act";
      rtJson = getJsonRs(url, mergeQueryString($("menuInfoForm")));
      
      if (rtJson.rtState == "0") {
        //alert(rtJson.rtMsrg);
        if(rtJson.rtMsrg == "成功修改主菜单分类") {
          document.getElementById("menuIdOld").value = document.getElementById("menuId").value ;
        }
        location = "<%=contextPath%>/core/funcs/menu/update.jsp";
        parent.navigateFrame.location.reload();
      }else {
        alert(rtJson.rtMsrg); 
      }     
    }else {
      url = "<%=contextPath%>/yh/core/funcs/menu/act/YHSysMenuAct/addSysMenu.act";
      rtJson = getJsonRs(url, mergeQueryString($("menuInfoForm")));
      if (rtJson.rtState == "0") {
        //parent.navigateFrame.location.reload();
        location = "<%=contextPath%>/core/funcs/menu/insert.jsp";
        window.parent.navigateFrame.location.reload();
        //$("menuInfoForm").reset();
        //document.getElementById("menuId").focus();
      }else{
        alert(rtJson.rtMsrg); 
      }
    }
  }
</script>
</head>
<body onload="doInit()">

<form name="menuInfoForm" id="menuInfoForm" method="post">
  <table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
    <tr>
      <td class="Big">
  <%
    if(seqId.equals("")) {
  %>   
    <img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"></img><span class="big3">&nbsp;添加菜单主分类</span> 
  <%
    }else {
  %>    
    <img src="<%=contextPath %>/core/styles/imgs/edit.gif"></img><span class="big3">&nbsp;修改菜单主分类</span>
  <%
    }
  %>
	    </td>
	  </tr>
  </table>
   <input type="hidden" name="seqId" id="seqId" value="" />
    <input type="hidden" id="menuIdOld" name="menuIdOld" value=""/>
    <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.menu.data.YHSysMenu"/>
    
  <table cellscpacing="1" cellpadding="3" width="400" align="center" class="TableList">
    <tr class="TableLine1">
      <td>主菜单分类代码:</td>
      <td><font style="color:red">*</font>&nbsp;<input type="text" id="menuId" name="menuId" class="BigInput" size="2" maxlength="2" value=""/>代码请尽量间隔开，2位数字</td>
    </tr>
    <tr class="TableLine2">
      <td>菜单名称:</td>
      <td><font style="color:red">*</font>&nbsp;<input type="text" id="menuName" name="menuName" class="SmallInput" size="20" maxlength="50" value=""/></td>
    </tr>
    <tr class="TableLine1">
      <td>图片名:</td>
      <td><font style="color:red">*</font>&nbsp;<input type="text" id="image" name="image" class="SmallInput" size="20" maxlength="50" value=""/></td>
    </tr>
  
  <%
    if(seqId.equals("")) {
  %>   
    <tr class="TableLine2">
      <td colspan="2" align="center">
        <input type="button" value="提交" class="SmallButton" onclick="commitItem()">
      </td>
    </tr>
  <%
    }else {
  %>    
    <tr class="TableLine2">
      <td colspan="2" align="center">
        <input type="button" value="提交" class="SmallButton" onclick="commitItem()">
        <input type="button" value="返回" class="SmallButton" onclick="location='<%=contextPath %>/core/funcs/menu/blank.jsp'"/>
      </td>
    </tr>
  <%
    }
  %>

  </table>
</form>

</body>
</html>