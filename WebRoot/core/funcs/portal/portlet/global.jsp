<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  XHTML  1.0  Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=utf-8">
<title>桌面模块设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script  type="text/javascript"  src="<%=contextPath  %>/core/js/datastructs.js"></script>
<script  type="text/javascript"  src="<%=contextPath  %>/core/js/sys.js"></script>
<script  type="text/javascript"  src="<%=contextPath  %>/core/js/prototype.js"></script>
<script  type="text/javascript"  src="<%=contextPath  %>/core/js/smartclient.js"></script>
<script type="text/javascript" src = "<%=contextPath  %>/core/js/cmp/grid.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath %>/cmp/grid.css"/>
<script type="text/javascript" src="/yh/core/js/orgselect.js"></script>
<style>

div.YH_Grid_data_opratePanel{
  font-size:14px;
}
</style>
<script type="text/javascript">
function doInit(){
	//初始化桌面参数设置列表  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/setports/act/YHDesktopDefineAct/getDesktopProperties.act");

	if (rtJson.rtState == "0") {
		$('DESKTOP_WIDTH').checked = rtJson.rtData.WIDTH == "1" ? true:false;
		$('DESKTOP_POS').checked = rtJson.rtData.POS == "1" ? true:false;
		$('DESKTOP_LINES').checked = rtJson.rtData.LINES == "1" ? true:false;
		$('DESKTOP_SCROLL').checked = rtJson.rtData.SCROLL == "1" ? true:false;
		$('DESKTOP_EXPAND').checked = rtJson.rtData.EXPAND == "1" ? true:false;
		
		Form.Element.setValue($('DESKTOP_LEFT_WIDTH'), rtJson.rtData.LEFT_WIDTH);
	}else {
    alert(rtJson.rtMsrg);
	}
}

function check(e) {
  if(!isPositivInteger(e.value)){
    alert("请输入正整数!");
    e.select();
  }
}

function setMineToOthers() {
  showDialog("optionBlock");
}

function showDialog(id,vTopOffset) {
  if (typeof arguments[1] == "undefined") {
    vTopOffset = 90;
  }
  var bb=(document.compatMode && document.compatMode!="BackCompat") ? document.documentElement : document.body;
  $("overlay").style.width = Math.max(parseInt(bb.scrollWidth),parseInt(bb.offsetWidth))+"px";
  $("overlay").style.height = Math.max(parseInt(bb.scrollHeight),parseInt(bb.offsetHeight))+"px";

  $("overlay").style.display = 'block';
  $(id).style.display = 'block';

  $(id).style.left = ((bb.offsetWidth - $(id).offsetWidth)/2)+"px";
  $(id).style.top  = (vTopOffset + bb.scrollTop)+"px";
}


function hideDialog(id) {
   $("overlay").style.display = 'none';
   $(id).style.display = 'none';
}

function clearUser(id, desc) {
  $(id).value = '';
  $(desc).innerHTML = '';
}

function checkForm() {
  if (!$('userId').value) {
    alert('请选择用户!');
    return false;
  }
}

function submitForm() {
  var url = '<%=contextPath %>/yh/core/funcs/setdescktop/setports/act/YHDesktopDefineAct/setMineToOthers.act';
  var pars = Form.serialize($('form2'));
  var json = getJsonRs(url,pars);
  if (json.rtState == "0"){
    hideDialog('optionBlock')
    alert('设置成功!');
  } else{
    hideDialog('optionBlock')
    alert('设置失败!');
  }
}
</script>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="absmiddle"><span class="big3"> 桌面参数设置</span>
    </td>
  </tr>
</table>
<form action="<%=contextPath %>/yh/core/funcs/setdescktop/setports/act/YHDesktopDefineAct/setDesktopProperties.act"  method="post" name="form1">
  <table class="TableBlock" width="700" align="center">
   <tr class="TableHeader">
    <td colspan="2">全局设置</td>
   </tr>
   <tr class="TableData">
    <td>桌面设置：</td>
    <td>
      <input type="checkbox" name="DESKTOP_POS" id="DESKTOP_POS" /><label for="DESKTOP_POS">允许用户调整各桌面模块位置</label><br>
      <input type="checkbox" name="DESKTOP_WIDTH" id="DESKTOP_WIDTH" /><label for="DESKTOP_WIDTH">允许用户调整左右栏目宽度</label><br>
      <input type="checkbox" name="DESKTOP_LINES" id="DESKTOP_LINES" /><label for="DESKTOP_LINES">允许用户调整各桌面模块显示条数</label><br>
      <input type="checkbox" name="DESKTOP_SCROLL" id="DESKTOP_SCROLL" /><label for="DESKTOP_SCROLL">允许用户设置列表上下滚动显示</label><br>
      <input type="checkbox" name="DESKTOP_EXPAND" id="DESKTOP_EXPAND" /><label for="DESKTOP_EXPAND">允许用户展开/折叠桌面模块</label><br>
    </td>
   </tr>
   <tr class="TableData">
    <td>左侧栏目宽度：</td>
    <td><input type="text" name="DESKTOP_LEFT_WIDTH" id="DESKTOP_LEFT_WIDTH" class="BigInput" size="5" value="50" onblur="javascript:check($('DESKTOP_LEFT_WIDTH'))">%</td>
   </tr>
   <tr class="TableControl" >
    <td nowrap colspan="2" align="center">
       <input type="submit" value="确定" class="BigButton">&nbsp;
    <% 
       YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
       if ("admin".equals(user.getUserId())) {
    %>
           <a href="javascript:void(0);" onClick="setMineToOthers();return false;">用我的设置更新其他用户(与我当前桌面看到的一致)</a>
     <%
       } 
     %>
     </td>
   </tr>
  </table>
</form>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath %>/dian1.gif" width="100%"></td>
 </tr>
</table>

<div id="overlay"></div>
<div id="optionBlock" class="ModalDialog" style="display:none;width:550px;">
  <div class="header"><span class="title">用我的设置更新其他用户 </span></div>
  <div id="msgCommand" class="body">
    <form action="" method="post" name="form2" id="form2">
      <table class="TableBlock" align="center">
       <tr>
        <td nowrap class="TableContent">选择待更新的用户：</td>
        <td nowrap class="TableData">
          <input type="hidden" name="userId" id="userId" value="">
          <textarea cols="38" name="userIdDesc" id="userIdDesc" rows="2" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
          <a href="javascript:;" class="orgAdd" onClick="selectUser(['userId', 'userIdDesc'])">添加</a>
          <a href="javascript:;" class="orgClear" onClick="clearUser('userId', 'userIdDesc')">清空</a>
        </td>
       </tr>
       <tr class="TableControl">
        <td colspan="2" align="center">
            <input type="button" onclick="submitForm()" value="确定" class="BigButton" title="用我的设置更新选中的用户" name="button">&nbsp;&nbsp;
            <input type="button" value="关闭" class="BigButton" title="关闭提交窗口" name="button" onclick="javascript:hideDialog('optionBlock');">
        </td>
       </tr>
      </table>
    </form>
  </div>
</div>
</body>
</html>