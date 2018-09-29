<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<title>编辑个人资料</title>
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script  type="text/javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script  type="text/javascript">

var initSelectState = function(e,value){
  e.childElements().each(function(e,i){
    if(e.value == value){
      e.selected = true;
    }
  });
}

function doInit(){
  var beginParameters = {
      inputId:'birthday',
      property:{isHaveTime:false},
      bindToBtn:'birthdayImg'
  };
  new Calendar(beginParameters);
  
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/setports/act/YHPersonInfoAct/getPersonInfo.act");
  if (rtJson.rtState == "0"){
    $('userName').value = rtJson.rtData.userName;
    $('telNoDept').value = rtJson.rtData.telNoDept;
    $('faxNoDept').value = rtJson.rtData.faxNoDept;
    $('mobilNo').value = rtJson.rtData.mobilNo;
    $('bpNo').value = rtJson.rtData.bpNo;
    $('email').value = rtJson.rtData.email;
    $('oicq').value = rtJson.rtData.oicq;
    $('icq').value = rtJson.rtData.icq;
    $('sex').value = rtJson.rtData.sex;
    $('msn').value = rtJson.rtData.msn;
    $('addHome').value = rtJson.rtData.addHome;
    $('postNoHome').value = rtJson.rtData.postNoHome;
    $('telNoHome').value = rtJson.rtData.telNoHome;
    $('seqId').value = rtJson.rtData.seqId;
    $('birthday').value = rtJson.rtData.birthday.substring(0,10);
    $('mobilNoHidden').checked = rtJson.rtData.mobilNoHidden == '1';
  }
}

function submitForm(){
  if ($F('telNoDept') && !/^(\d{3,4}-)?\d{7,8}$/.exec($F('telNoDept'))) {
    alert('请输入正确的工作电话!');
    $('telNoDept').select();
    return;
  }

  if ($F('birthday') && !isValidDateStr($F('birthday'))) {
    alert("请输入正确的生日!");
    $('birthday').select();
    return;
  }
  
  if ($F('faxNoDept') && !/^(\d{3,4}-)?\d{7,8}$/.exec($F('faxNoDept'))) {
    alert('请输入正确的传真号码!');
    $('faxNoDept').select();
    return;
  }
  
  if ($F('bpNo') && !/^(\d{3,4}-)?\d{7,8}$/.exec($F('bpNo'))) {
    alert('请输入正确的工作电话2!');
    $('bpNo').select();
    return;
  }
  
  if ($F('telNoHome') && !/^(\d{3,4}-)?\d{7,8}$/.exec($F('telNoHome'))) {
    alert('请输入正确的家庭!');
    $('telNoHome').select();
    return;
  }

  if ($F('mobilNo') && !/^[0-9]{11}$/.exec($F('mobilNo'))) {
    alert('请输入正确的手机号码!');
    $('mobilNo').select();
    return;
  }
  
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/setports/act/YHPersonInfoAct/updatePersonInfo.act";

  var json = getJsonRs(url,pars);
  
  if(json.rtState == "0"){
    window.location = "<%=contextPath %>/core/funcs/setdescktop/info/update.jsp";
  }else{
    alert("信息修改失败");
  }
}

</script>
</head>

<body onload="doInit()">
<div class="PageHeader"> 
<table border="0" width="50%">
  <tr>
    <td class="Big">
      <img src="<%=imgPath%>/node_user.gif" WIDTH="16" HEIGHT="16"/>
      <span class="big3"> 个人资料</span>
      <br>
    </td>
  </tr>
</table>
</div>
<form action=""  method="post" name="form1" id="form1">
  <input type="hidden" value="yh.core.funcs.setdescktop.setports.data.YHPerson" name="dtoClass" id="dtoClass">
  <input type="hidden" id="seqId" name="seqId"/>
  <input type="hidden" id="userId" name="userId"/>
  <table class="TableTop" style="width: 80%" align="center">
  <tr>
    <td class="left">
    </td>
    <td class="center">
    基本信息
    </td>
    <td class="right">
    </td>
  </tr>
</table>
  <table class="TableBlock no-top-border" width="80%" align="center">

    <tr>
      <td nowrap class="TableData"> 姓名：</td>
      <td class="TableData">
        <input type="text" name="userName" id="userName" size="10" maxlength="10" class="BigStatic" readonly>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 性别：</td>
      <td class="TableData">
        <select name="sex" id="sex" class="BigSelect">
          <option value="0" >男</option>
          <option value="1" >女</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">生日：</td>
      <td class="TableData">
        <input type="text" id="birthday" name="birthday"/>
        <img border="0" align="absMiddle" style="" src="<%=imgPath%>/calendar.gif" id="birthdayImg" name="birthdayImg">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableHeader" colspan="2"><b>&nbsp;联系方式</b></td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 工作电话：</td>
      <td class="TableData">
        <input type="text" name="telNoDept" id="telNoDept" size="25" maxlength="25" class="BigInput">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 工作传真：</td>
      <td class="TableData">
        <input type="text" name="faxNoDept" id="faxNoDept" size="25" maxlength="25" class="BigInput" >
      </td>
    </tr>
   <tr>
      <td nowrap class="TableData"> 手机：</td>
      <td class="TableData">
        <input type="text" name="mobilNo" id="mobilNo" size="23" maxlength="23" class="BigInput">
        <input type="checkbox" name="mobilNoHidden" id="mobilNoHidden" ><label for="mobilNoHidden">手机号码不公开</label><br>
        填写后可接收OA系统发送的手机短信<br>
        手机号码不公开仍可接收短信<br>
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 工作电话2：</td>
      <td class="TableData">
        <input type="text" name="bpNo" id="bpNo" size="25" maxlength="25" class="BigInput" >
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 电子邮件：</td>
      <td class="TableData">
        <input type="text" name="email" id="email" size="25" maxlength="50" class="BigInput" >
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> QQ号码：</td>
      <td class="TableData">
        <input type="text" name="oicq" id="oicq" size="25" maxlength="25" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> msn：</td>
      <td class="TableData">
        <input type="text" name="msn" id="msn" size="25" maxlength="50" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> ICQ号码：</td>
      <td class="TableData">
        <input type="text" name="icq" id="icq" size="25" maxlength="25" class="BigInput" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableHeader" colspan="2"><b>&nbsp;家庭信息</b></td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 家庭住址：</td>
      <td class="TableData">
        <input type="text" name="addHome" id="addHome" size="40" maxlength="100" class="BigInput">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 家庭邮编：</td>
      <td class="TableData">
        <input type="text" name="postNoHome" id="postNoHome" size="25" maxlength="25" class="BigInput" >
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 家庭电话：</td>
      <td class="TableData">
        <input type="text" name="telNoHome" id="telNoHome" size="25" maxlength="25" class="BigInput">
      </td>
    </tr>


    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="保存修改" class="BigButton" onclick="submitForm()">
      </td>
    </tr>
  </table>

</form>

</body>
</html>