<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
  
  String pageNo = request.getParameter("pageNo");
  if(pageNo == null) {
    pageNo = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建用户</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
  var seqId = "<%=seqId%>";
  var pageNo = "<%=pageNo%>";
  function doInit() {
	if(seqId){
	  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getOffDutyPerson.act";
	  var rtJson = getJsonRs(url, "seqId=" + seqId) ;
	  if (rtJson.rtState == "0") {
	    bindJson2Cntrl(rtJson.rtData);
	    bindDesc([{cntrlId:"postPriv", dsDef:"USER_PRIV,SEQ_ID,PRIV_NAME"}
	      ,{cntrlId:"deptId", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}
	      ,{cntrlId:"postDept", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
	  }else {
	    alert(rtJson.rtMsrg); 
	  }
	}
  } 
	
  function check() {
    var userId = document.getElementById("userId").value;
    var url = contextPath + "/yh/core/funcs/person/act/YHPersonAct/selectPerson.act";
    var rtJson = getJsonRs(url, "userId=" + userId) ;
    if (rtJson.rtState == "0") {
      var divNode = document.getElementById("userid");
      if(rtJson.rtData[0].num == '1') {
        divNode.innerHTML = "用户名已存在！";
      }else if(rtJson.rtData[0].num == '0') {
        divNode.innerHTML = "congratulations, 该用户名可注册！";
      }
    }
  }
	
  function selectPriv() {
	var priv = document.getElementById("priv");
	if(priv.style.display=="none"){
	  priv.style.display="";
	}else{
	  priv.style.display="none";
	}	
  }
	
  function selectDeptOther() {
	var deptOther = document.getElementById("deptOther");
	if (deptOther.style.display=="none"){
	  deptOther.style.display='';
	}else{
   	  deptOther.style.display="none";
	}
  }

  function selectOwnerDept() {
	var URL="<%=contextPath %>/core/funcs/person/deptselecttreelist.jsp";
	openDialog(URL,'300', '350');
  }

  function clearOwnerDept() {
	document.getElementById("postDeptDesc").value = "";
  }

  function addDept() {
	var URL="<%=contextPath %>/core/funcs/person/persontreelist.jsp";
	openDialog(URL,'300', '350');
  }
	
  function clearDept() {
	document.getElementById("deptIdDesc").value = "";
  }

  function addPriv() {
	var URL="<%=contextPath %>/core/funcs/person/personlist.jsp";
	openDialog(URL,'300', '350');
  }

  function clearPriv() {
    document.getElementById("postPrivDesc").value = "";
  }

  function selectCommunicateBoundary() {
  	var URL="<%=contextPath %>/core/funcs/person/communicatelist.jsp";
  	openDialog(URL,'300', '350');
  }
    
  function commitItem() {
	var url = null;
	var rtJson = null;
	
	if (seqId) {
	  url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/updateOffDutyPerson.act";
	  rtJson = getJsonRs(url, mergeQueryString($("userInfoForm")));
	  if (rtJson.rtState == "0") {
	    alert(rtJson.rtMsrg); 
	  } else {
        alert(rtJson.rtMsrg); 
      }  
	}else{
	  url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/addOffDutyPerson.act";
	  rtJson = getJsonRs(url, mergeQueryString($("userInfoForm")));
	  if (rtJson.rtState == "0") {
	    alert(rtJson.rtMsrg); 
	    $("userInfoForm").reset();
	  } else {
        alert(rtJson.rtMsrg); 
      } 
	}
	
	var openerWindow = getOpenerWindow();
	openerWindow.location.reload();
  }

  function folder(name) {
	var n = document.getElementById(name);
	if(n.style.display=='none'){
	  n.style.display='block';
	}else {
	  n.style.display='none';
	}
  }

  function selectPriv(){
    var priv = document.getElementById("priv");
    if(priv.style.display=="none"){
      priv.style.display="";
    }else{
      priv.style.display="none";
    }	
  }

  function goBack() {
    window.location.href = "<%=contextPath %>/core/funcs/person/offdutypersonlist.jsp?pageNo=" + pageNo;
  }
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">
      <img src="<%=contextPath %>/core/styles/imgs/topplus.gif"></img><span class="big3"> 新建用户 （离职人员/外部人员）</span>
    </td>
  </tr>
</table>
<form name="userInfoForm" id="userInfoForm" method="post">
<%
  if("".equals(seqId)) {
%>
    <h2><img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"></img>添加代码</h2> 
<%
  }else {
%>
    <h2><img src="<%=contextPath %>/core/styles/imgs/edit.gif"></img>修改代码</h2>
<%
  }
%> 
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.person.data.YHPerson"/>
  <input type="hidden" id="seqId" name="seqId" value=""/>
<div><img src="<%=contextPath %>/core/styles/imgs/green_arrow.gif" align="absMiddle"> 用户基本信息</div>
<table class="TableBlock" width="95%" align="center">
  <tr>
    <td nowrap class="TableContent" width="120">用户名：</td>
    <td nowrap class="TableContent">
        <input type="text" id="userId" name="userId" class="BigInput" size="10" maxlength="20" value="" onblur="check()">&nbsp;
        <span id="userid"></span>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent">真实姓名：</td>
    <td nowrap class="TableContent">
        <input type="text" name="userName" class="BigInput" size="10" maxlength="30">&nbsp;
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent">主角色：</td>
    <td nowrap class="TableContent">
        <select name="userPriv" class="BigSelect">
          <option value="5">职员</option>
          <option value="3">财务主管</option>
          <option value="1">OA 管理员</option>
          <option value="4">部门经理</option>
          <option value="2">总经理</option>
      </select>&nbsp;&nbsp;<a href="javascript:selectPriv()">指定辅助角色</a>
    </td>
  </tr>
  <tr id="priv" style="display:none;">
    <td nowrap class="TableData">辅助角色：</td>
    <td class="TableData">
      <input type="hidden" id="postPriv" name="postPriv" value="">
      <textarea cols=30 id="postPrivDesc" name="postPrivDesc" rows=2 class="BigStatic" wrap="yes" readonly ></textarea>
      
      <a href="javascript:;" class="orgAdd" onClick="addPriv()">添加</a>
      <a href="javascript:;" class="orgClear" onClick="clearPriv()">清空</a>
      <br/>辅助角色仅用于扩展主角色的模块权限
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableContent">部门：</td>
    <td nowrap class="TableContent">
      <textarea cols="40" id="deptIdDesc" name="deptIdDesc"  rows="1" class="SmallStatic" readonly></textarea>
      <input type="hidden" id="deptId" name="deptId" class="BigInput" size="25" maxlength="25" value="">
      <a href="javascript:addDept()">添加部门</a>
      <a href="javascript:;" onClick="clearDept()">清空部门</a>
      <h3><a href="javascript:selectDeptOther()"><font color="red">指定其它所属部门</font></a></h3>
    </td>
  </tr>
  
  <tr id="deptOther" style="display:none;">
    <td nowrap class="TableData">所属部门：</td>
    <td class="TableData">
      <textarea cols=30 id="postDeptDesc" name="postDeptDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <input type="hidden" id="postDept" name="postDept" class="BigInput" size="25" maxlength="25" value="">
      <a href="javascript:;" class="orgAdd" onClick="selectOwnerDept()">选择</a>
      <a href="javascript:;" class="orgClear" onClick="clearOwnerDept()">清空</a>
    </td>
  </tr> 
  <tr>
    <td nowrap class="TableContent">用户排序号：</td>
    <td nowrap class="TableContent">
        <input type="text" name="userNo" class="BigInput" size="10" value="10" value="">&nbsp;
        用于同角色用户的排序
    </td>
  </tr>
</table>

<div><img src="<%=contextPath %>/core/styles/imgs/green_arrow.gif" align="absMiddle"> 用户权限信息</div>
<table>
  <tr>
    <td nowrap class="TableData" width="120">管理范围：</td>
    <td nowrap class="TableData">
        <select name="managerBoundary" class="BigSelect" OnChange="select_dept()">
          <option value="0">本部门</option>
          <option value="1">全体</option>
          <option value="2">指定部门</option>
        </select>
        在管理型模块中起约束作用
    </td>
  </tr>
  
  <tr id="dept" style="display:none;">
      <td nowrap class="TableData">管理范围（部门）：</td>
      <td class="TableData">
        <input type="hidden" name="toId" value="">
        <textarea cols=30 name="toName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        &nbsp;<input type="button" value="选 择" class="SmallButton" onClick="SelectDept('','','','1')" title="选择部门" name="button">
        &nbsp;<input type="button" value="清 空" class="SmallButton" onClick="ClearUser()" title="清空部门" name="button">
      </td>
  </tr>

  <tr>
    <td nowrap class="TableData">访问控制：</td>
    <td nowrap class="TableData">
    	  <input type="checkbox" name="notLogin" id="NOT_LOGIN" ><label for="NOT_LOGIN">禁止登录OA系统</label>&nbsp;
    	  <input type="checkbox" name="notViewUser" id="NOT_VIEW_USER" ><label for="NOT_VIEW_USER">禁止查看用户列表</label>&nbsp;
    	  <input type="checkbox" name="notViewTable" id="NOT_VIEW_TABLE" ><label for="NOT_VIEW_TABLE">禁止显示桌面</label>
        <input type="checkbox" name="useingKey" id="USEING_KEY" ><label for="USEING_KEY">使用USB KEY登录</label>
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableData" width="120">IMA广播权限：</td>
    <td nowrap class="TableData">
        <select name="canbroadcast" class="BigSelect">
          <option value="0">无</option>
          <option value="1">有</option>
        </select>
    </td>
  </tr>
 
  <tr>
      <td nowrap class="TableData">通讯范围：</td>
      <td class="TableData">
        <input type="hidden" name="privId1" value="">
        <textarea cols=30 id="privName1" name="privName1" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        
        <a href="javascript:;" class="orgAdd" onClick="selectCommunicateBoundary()">添加</a>
        <a href="javascript:;" class="orgClear" onClick="clearCommunicateBoundary()">清空</a>
        <br>所选角色的人员可以给该用户发送邮件和短消息，为空则不限制
      </td>
  </tr>
</table>

<div onclick="folder('option1')" title="点击展开/收缩选项" style="cursor:pointer;"><img src="<%=contextPath %>/core/styles/imgs/green_arrow.gif" align="absMiddle"> 其它选项</div>
<table id="option1" style="display:none">
  <tr>
    <td nowrap class="TableData">考勤排班类型：</td>
    <td nowrap class="TableData">
        <select name="dutyType" class="BigSelect">
          <option value="1">正常班</option>
          <option value="2">全日班</option>
        </select>
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableData">内部邮箱容量：</td>
    <td nowrap class="TableData">
        <input type="text" name="emailCapacity" class="BigInput" size="5" maxlength="11" value="100">&nbsp;MB
        为空则表示不限制大小
    </td>
  </tr>
  
  <tr >
    <td nowrap class="TableData">个人文件柜容量：</td>
    <td nowrap class="TableData">
        <input type="text" name="folderCapacity" class="BigInput" size="5" maxlength="11" value="100">&nbsp;MB
        为空则表示不限制大小
    </td>
  </tr>
  
  <tr  style="display:none">
    <td nowrap class="TableData">Internet邮箱数量：</td>
    <td nowrap class="TableData">
        <input type="text" name="webmailNum" class="BigInput" size="5" maxlength="11" value="">&nbsp;个
        为空则表示不限制数量
    </td>
  </tr>
  
  <tr  style="display:none">
    <td nowrap class="TableData">每个Internet邮箱容量：</td>
    <td nowrap class="TableData">
        <input type="text" name="webmailCapacity" class="BigInput" size="5" maxlength="11" value="">&nbsp;MB
        为空则表示不限制大小
    </td>
  </tr>

  <tr>
    <td nowrap class="TableData">绑定IP地址：</td>
    <td nowrap class="TableData">
      <textarea name="bindIp" class="BigInput" cols="50" rows="2"></textarea><br>
        为空则该用户不绑定固定的IP地址，绑定多个IP地址用英文逗号(,)隔开<br>也可以绑定IP段，如“192.168.0.60,192.168.0.100-192.168.0.200”表示<br>192.168.0.60或192.168.0.100到192.168.0.200这个范围内都可以登录
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableData">备注：</td>
    <td nowrap class="TableData">
      <textarea name="remark" class="BigInput" cols="50" rows="2"></textarea>
    </td>
  </tr>
</table>

<div onclick="folder('option2')" title="点击展开/收缩选项"  style="cursor:pointer;"><img src="<%=contextPath %>/core/styles/imgs/green_arrow.gif" align="absMiddle"> 用户可自定义选项</div>
<table id="option2" style="display:none;">
  <tr>
    <td nowrap class="TableData" width="120">别名：</td>
    <td nowrap class="TableData">
        <input type="text" name="byname" class="BigInput" size="10" maxlength="20">
        用户可用此别名登录系统，别名不能与其他用户的别名或用户名相同
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableData">密码：</td>
    <td nowrap class="TableData">
        <input type="password" name="password" class="BigInput" size="20" maxlength="20"> 8-20位，必须同时包含字母和数字    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableData">性别：</td>
    <td nowrap class="TableData">
        <select name="sex" class="BigSelect">
        <option value="0">男</option>
        <option value="1">女</option>
        </select>
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableData">生日：</td>
    <td nowrap class="TableData">
        <input type="text" name="birthday" value="2009-01-01" size="10" maxlength="10" class="BigInput">
    </td>
  </tr>
  
  <tr>
      <td nowrap class="TableData">界面主题：</td>
      <td class="TableData">
        <select name="theme" class="BigSelect">
		  <option value="1" >儒雅深沉(2008默认主题)</option>
		  <option value="2" >清新怡然</option>		
		  <option value="3" >简洁明亮</option>
		  <option value="4" >粉红浪漫</option>
		  <option value="5" >水晶葡萄</option>
		  <option value="6" >Vista风格</option>
		  <option value="7" >我心飞翔</option>
		  <option value="8" >儒雅深沉(大字体)</option>	
		  <option value="9" selected>生命与自然之美(2009默认主题)</option>
        </select>
      </td>
  </tr>
  
  <tr>
      <td nowrap class="TableData"> 手机：</td>
      <td class="TableData">
        <input type="text" name="mobilNo" size="16" maxlength="23" class="BigInput" value="">
        <input type="checkbox" name="mobilNoHidden" id="mobilNoHidden"><label for="mobilNoHidden">手机号码不公开</label><br>
        填写后可接收OA系统发送的手机短信，手机号码不公开仍可接收短信
      </td>
  </tr>
  
  <tr>
      <td nowrap class="TableData"> 电子邮件：</td>
      <td class="TableData">
        <input type="text" name="email" size="25" maxlength="50" class="BigInput" value="">
      </td>
  </tr>
  <tr>
      <td nowrap class="TableData"> 工作电话：</td>
      <td class="TableData">
        <input type="text" name="telNoDept" size="16" maxlength="23" class="BigInput" value="">
      </td>
  </tr>
</table>
</form>

<%
  if(seqId.equals("")) {
%>
<div align="center">
   <input type="button" value="新 建" class="BigButton" title="新建用户" onclick="commitItem()">&nbsp;&nbsp;
</div>
<%
  }else {
%>
<div align="center">
   <input type="button" value="确认" class="BigButton" title="确认用户" onclick="commitItem()">&nbsp;&nbsp;
   <input type="button" value="返回" class="BigButton" title="返回窗口" onclick="goBack()">
</div>
<%
  }
%>
</body>
</html>