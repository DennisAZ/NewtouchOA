<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>志愿者报名</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css"/>
<style type="text/stylesheet">

</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<style type="text/css">
<!--
.table {
  padding: 0px;
  width: 675px;
  margin-top: 4px;
}
.table form {
  vertical-align: top;
  margin-top: 10px;
}
-->
</style>
<script type="text/javascript">
function submitForm(){
  var reg = /^[0-9]*$/;
  if (!$F('name')){
    alert('团体名称不能为空');
    $('name').select();
    return;
  }

  if ($('introduce').value.length <= 20){
    alert('志愿者团体介绍至少为20字');
    $('introduce').select();
    return;
  }

  if ($('introduce').value.length > 1500){
    alert('志愿者团体介绍最多为1500字');
    $('introduce').focus();
    $('introduce').select();
    return;
  }
  
  if (!$F('principal')){
    alert('团体负责人姓名不能为空');
    $('principal').select();
    return;
  }

  if (!isNumber($F('principalTel1'))){
    alert('联系电话必须为数字');
    $('principalTel1').focus();
    $('principalTel1').select();
    return;
  }

  if(!reg.test($('principalTel2').value)){
  	alert("联系电话必须为数字");
  	$('principalTel2').value = '';
    $('principalTel2').select();
  	return false;
  }
  
  if (!$F('principalContact')){
    alert('团体负责人紧急联系方式不能为空');
    $('principalContact').select();
    return;
  }

  if (!isNumber($F('principalContact'))){
    alert('紧急联系方式必须为数字');
    $('principalContact').value = '';
    $('principalContact').select();
    return;
  }
  
  if (!$('services1').checked && !$('services2').checked && !$('services3').checked && !$('services4').checked ){
    alert('能提供的服务项目不能为空');
    return;
  }
  else{
    $('services').value = '';
    ['services1','services2','services3','services4'].each(function(e, i){
      if ($(e).checked){
        $('services').value += (( i + 1 ) + ',');
      }
    });
  }

  if($("availTime1").checked){
    $("availTime1").value = "1";
  }else{
    $("availTime1").value = "0";
  }
  if($("availTime2").checked){
    $("availTime2").value = "1";
  }else{
    $("availTime2").value = "0";
  }
  

  var temp = document.getElementsByName("a1");
  for (var i = 0; i < temp.length; i++){
    if(temp[i].checked){
      $("at1Vlaue").value = temp[i].value;
    }
  }

  var temp2 = document.getElementsByName("a2");
  for (var i = 0; i < temp2.length; i++){
    if(temp2[i].checked){
      $("at2Vlaue").value = temp[i].value;
    }
  }

  var pars = Form.serialize($('form1'));
  
  var url = "<%=contextPath %>/yh/subsys/infomgr/bilingual/act/YHPostulantAct/modify.act";
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    window.location.href = "<%=contextPath %>/subsys/infomgr/postulant/update.jsp?flag=${param.flag}";
  }else{
    alert("修改失败");
  }
}

function doInit(){
  var url = "<%=contextPath %>/yh/subsys/infomgr/bilingual/act/YHPostulantAct/detail.act?seqId=${param.seqId}";
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    var data = json.rtData;
    $('seqId').value = data.seqId;
    $('postulantId').value = data.postulantId;
    $('name').value = data.name;
    $('flag').value = data.flag;
    $('principal').value = data.principal;
    $('principalUnit').value = data.principalUnit;
    $('principalDuty').value = data.principalDuty;
    var principalTel = data.principalTel;
    if(principalTel == "" || principalTel == null){
      $('principalTel1').value = "010";
    }else{
      $('principalTel1').value = principalTel.substr(0, principalTel.indexOf('-'));
    }

    if(principalTel == "" || principalTel == null){
      $('principalTel2').value = "";
    }else{
      $('principalTel2').value = principalTel.substr(principalTel.indexOf('-')+1, principalTel.length);
    }
    $('principalAdd').value = data.principalAdd;
    $('principalContact').value = data.principalContact;
    $('servicesOther').value = data.servicesOther;
    $('recordSource').value = data.recordSource;
    $("introduce").value = data.introduce;
    $('services').value = data.services;
    
    var serveTimeWeekday = data.serveTimeWeekday || "";
    var serveTimeWeekend = data.serveTimeWeekend || "";
    
    
    if(serveTimeWeekend != ""){
      if(serveTimeWeekend.substr(0, serveTimeWeekend.length-1) == "1"){
        $("availTime2").checked = true;
        if(serveTimeWeekend.substr(1, serveTimeWeekend.length) == "0"){
          $("at4").checked = true;
        }else if(serveTimeWeekend.substr(1, serveTimeWeekend.length) == "1"){
          $("at5").checked = true;
        }else{
          $("at6").checked = true;
        }
      }
    }

    if(serveTimeWeekday != ""){
      if(serveTimeWeekday.substr(0, serveTimeWeekday.length-1) == "1"){
        $("availTime1").checked = true;
        if(serveTimeWeekday.substr(1, serveTimeWeekday.length) == "0"){
          $("at1").checked = true;
        }else if(serveTimeWeekday.substr(1, serveTimeWeekday.length) == "1"){
          $("at2").checked = true;
        }else{
          $("at3").checked = true;
        }
      }
    }
    
    
    if (data.services){
      data.services.split(",").each(function(e, i){
        $('services' + e).checked = true;

        if (e == '4'){
          $('servicesOther').show();
        }
      });
    }
    
  }else{
    alert("初始化菜单失败");
  }
  detailInfo();
}

function detailInfo(){
  var total = 0;
  var str = "";
  var url = "<%=contextPath %>/yh/subsys/infomgr/bilingual/act/YHPostulantAct/detailInfo.act?seqId=${param.seqId}";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    for(var i = 0; i < rtJson.rtData.length; i++){
      var seqId = rtJson.rtData[i].seqId;
      var amount = rtJson.rtData[i].amount;
      var languageKind = rtJson.rtData[i].languageKind;
      var languageLevel = rtJson.rtData[i].languageLevel;

      str += languageKind + "(" + languageLevel + ")志愿者: ";
      str += amount + "人<br />";
      total += amount;
    }
    $('totalCount').innerHTML = "报名人数总计：" + total;
    $("dispMsg").innerHTML = str;
        
  }else{
    alert("初始化菜单失败");
  }  
}

function check() {
  var content;
  content = document.form1.introduce.value;
  if(content.length < 20)
   $("tip").innerHTML = "文字介绍至少为<font color=red>20</font>字";
  else if(content.length <=1500)
    $("tip").innerHTML = "还可以填写<font color=red>" + (1500-content.length) + "</font>字";
  else
    $("tip").innerHTML = "已超出<font color=red>" + (content.length-1500) + "</font>字";
}

function showServiceOther(t){
  if (t.checked){
    $('servicesOther').show();
    $('servicesOther').select();
  }
  else {
    $('servicesOther').hide();
    $('servicesOther').value = '';
  }
}
</script>
</head>
<body onload="doInit()">
	<div class="table">
		<form name="form1" id="form1" method="post" action="">
			<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
			<tr>
			  <td class="TableData">志愿者团体名称</td>
			  <td width="539" class="TableData"><input name="name" id="name" type="text" class="BigInput" size="37" /></td>
			</tr>
			<tr>
              <td class="TableData">报名情况</td>
              <td class="TableData"><div id="dispMsg"></div>
              <input type="hidden" name="group" size="50" value="[]" />
              <input type="hidden" name="langstr" value="" />
              <input type="hidden" name="gradestr" value="" />
              <input type="hidden" name="numberstr" value="" />
              <input type="hidden" name="strValue" id="strValue" value="" />
              </td>
             </tr>
             <tr>
              <td class="TableData">人数总计</td>
              <td class="TableData">
              <div id="totalCount"></div>
              </td>
             </tr>
			<tr>
			  <td class="TableData">志愿者团体介绍</td>
			  <td class="TableData">
			  <textarea name="introduce" id="introduce" cols="60" rows="5" wrap="virtual" class="BigInput" onkeyup="check()"></textarea> 
              <div id="tip">（可填写描述文字1500字以内）</div>
              </td>
			</tr>
			<tr>
			  <td class="TableData">团体负责人单位</td>
			  <td class="TableData"><input name="principalUnit" id="principalUnit" type="text" class="BigInput" size="37" />（请填写单位名称的全称）</td>
			</tr>
            <tr>
              <td class="TableData">团体负责人姓名</td>
              <td class="TableData"><input name="principal" id="principal" type="text" class="BigInput" size="10" /></td>
            </tr>
			<tr>
			  <td class="TableData">团体负责人职务</td>
			  <td class="TableData"><input name="principalDuty" id="principalDuty" type="text" class="BigInput" size="37" /></td>
			</tr>
			<tr>
			  <td class="TableData">联系电话</td>
			  <td class="TableData">
                <input name="principalTel1" id="principalTel1" type="text" class="BigInput" value="" size="5" />-<input name="principalTel2" id="principalTel2" type="text" class="BigInput" size="10" /></
              </td>
			</tr>
			<tr>
			  <td class="TableData">通讯地址</td>
			  <td class="TableData"><input name="principalAdd" id="principalAdd" type="text" class="BigInput" size="37" /></td>
			</tr>
			<tr>
			  <td class="TableData">紧急联系方式</td>
			  <td class="TableData"><input name="principalContact" id="principalContact" type="text" class="BigInput" size="15" />（请填写手机联系方式）</td>
			</tr>
			<tr>
			  <td class="TableData">能提供的服务项目</td>
			  <td class="TableData">
			  <label for="services1"><input type="checkbox" id="services1" />
			  1:公共英语标识纠错</label>
			  <label for="services2"><input type="checkbox" id="services2" />
			  2:行业/社区外语服务</label>
			  <label for="services3"><input type="checkbox" id="services3" />
			  3:全市大型外语活动</label>
			  <p><label for="services4">
			  <input type="checkbox" onclick="showServiceOther(this)" id="services4" />
			  4:其他请说明</label>
			  <input name="services" id="services" type="hidden"/>
			  <input name="servicesOther" id="servicesOther" style="display:none;" type="text" class="BigInput" size="21" />
			  </p></td>
			</tr>
			<tr>
			  <td class="TableData">服务时间</td>
			  <td class="TableData">
               <label for="availTime1">
               <input type="checkbox" name="availTime1" id="availTime1" />周一至周五</label>
               <label for="at1"><input type="radio" id="at1" name="a1" value="0" />9:00-12:00</label>
               <label for="at2"><input type="radio" id="at2" name="a1" value="1" />14:00-17:00</label>
               <label for="at3"><input type="radio" id="at3" name="a1" value="2" />9:00-17:00</label>
			   <br/>
               <label for="availTime2">
               <input type="checkbox" name="availTime2" id="availTime2"  />周末及公共假日</label>
               <label for="at4"><input type="radio" id="at4" name="a2" value="0" />9:00-12:00</label>
               <label for="at5"><input type="radio" id="at5" name="a2" value="1" />14:00-17:00</label>
               <label for="at6"><input type="radio" id="at6" name="a2" value="2" />9:00-17:00</label>
			  </td>
			</tr>
			<tr>
			<td colspan="2" class="TableData" align="center">
			<input type="hidden" id="recordSource" name="recordSource" value="YH"/>
			<input type="hidden" id="seqId" name="seqId" value=""/>
            <input type="hidden" id="postulantId" name="postulantId" value=""/>
            <input type="hidden" value="" id="at1Vlaue" name="at1Vlaue"/>
            <input type="hidden" value="" id="at2Vlaue" name="at2Vlaue"/>
			<input type="hidden" id="flag" name="flag"/>
			<input type="button" class="BigButton" value="返回" onclick="window.location.href='<%=contextPath %>/subsys/infomgr/postulant/manage.jsp?flag=${param.flag}';"/>
			<input type="button" value="提交" class="BigButton" onclick="submitForm()" />
			</td>
			</tr>
			</table>
		</form>
	</div>
</body>
</html>