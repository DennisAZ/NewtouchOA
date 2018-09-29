<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=StaticData.SOFTTITLE%></title>
<link rel="stylesheet"  href="<%=cssPath%>/style.css">
<script type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript">
function submitForm() {
  if (!$("USER_READ").checked) {
    alert("您必须同意软件使用条款，才能进行软件注册!");
    return false;
  }
  
  if (!/properties$/.test($("REGISTER_FILE").value)) {
		alert("授权文件格式不正确!");
		return false;
  }
  $('form1').submit();
}

</script>
</head>

<body>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">增加试用人数延长试用期限</span>
    </td>
  </tr>
</table>
<form name="form1" id="form1" method="post" action="<%=contextPath %>/yh/core/funcs/system/info/act/YHInfoAct/triaReg.act" enctype="multipart/form-data">
<table class="TableTop" style="width: 600px" align="center">
  <tr>
    <td class="left">
    </td>
    <td class="center" align="center">
   <%=StaticData.SOFTTITLE%>增加试用人数延长试用期限
    </td>
    <td class="right">
    </td>
  </tr>
</table>
<table width="600" align="center" class="TableBlock no-top-border">
  <tbody>
    <tr>
      <td width="100" nowrap="" class="TableData"><b>&nbsp;授权文件：</b></td>
      <td nowrap="" class="TableData">
        <input type="file" class="BigInput" size="30" id="REGISTER_FILE" name="REGISTER_FILE"><br>
        试用用户，请选择<%=StaticData.SOFTCOMPANY_SHORTNAME%>站获取的授权文件，进行操作      </td>
    </tr>
    <tr>
      <td colspan="2" class="TableData">
      	<b>&nbsp;软件使用条款：</b>
      	<font color="red">以下条款极为重要，请务必认真阅读，如您不接受以下条款，则请不要进行该操作</font><br><br>
        1、如果您已通过正规渠道购买了本软件，请勿进行此操作。<br>
        2、请确认您通过正规销售渠道获取了授权文件，如果您确认没有问题，可以略过以下条款。<br>
        3、当使用未经授权的方式操作时，可能对系统特定功能进行自动锁定。<br>
        4、您不得以任何目的，对软件进行破解、反编译、反向工程、代码抄袭、修改软件版权信息等操作。<br>
        5、如使用未经授权的方式注册，或进行软件破解操作，由此产生的一切直接、间接、可能或必然的损失，均由使用者自行承担，并同时承担版权侵权的法律责任。<br>
        <br>
        <input type="checkbox" id="USER_READ" class="BigInput" size="20" name="USER_READ"> <label style="cursor:pointer" for="USER_READ"><u><b>我已经阅读以上条款，并且完全理解和同意以上条款</b></u></label>
      </td>
    </tr>
    <tr>
      <td align="center" nowrap="" colspan="2" class="TableControl">
       <input type="button" onclick="submitForm()" class="BigButtonA" value="确定">&nbsp;
       <input type="button" onclick="location.href = contextPath + '/core/funcs/system/info/index.jsp'" class="BigButtonA" value="返回">
      </td>
    </tr>
 </tbody>
</table>
</form>
</body>
</html>