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

  if (!$F('amount')){
    alert('可参加志愿者数量不能为空');
    $('amount').select();
    return;
  }
  
  if (!isNumber($F('amount'))){
    alert('数量必须为数字');
    $('amount').value = '';
    $('amount').select();
    return;
  }
  
  if ($('introduce').value.length < 20){
    alert('志愿者团体介绍至少为20字');
    $('introduce').focus();
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
    $('principalTel1').value = '';
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
  }else{
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

 // if ((!$('weekdayStart').value && !$('weekendStart').value) || (!$('weekdayEnd').value && !$('weekendEnd').value)){
  //  alert('填写工作日服务时间段或者周末服务时间段');
  //  return;
 // }

  //if ($('weekendEnd').value && $('weekendStart').value){
  //  $('serveTimeWeekend').value = $('weekendStart').value + ',' + $('weekendEnd').value;
  //}
  
  //if ($('weekdayStart').value && $('weekdayEnd').value) {
  //  $('serveTimeWeekday').value = $('weekdayStart').value + ',' + $('weekdayEnd').value;
  //}
  
  var pars = Form.serialize($('form1'));
  
  var url = "<%=contextPath %>/yh/subsys/infomgr/bilingual/act/YHPostulantAct/add.act";
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    window.location.href = "<%=contextPath %>/subsys/infomgr/postulant/success.jsp";
  }else{
    alert("修改失败");
  }
}

function userdef(obj) {
 if(obj.selectedIndex==8)  //手填的语言
  $("user_lang").style.display = "";
 else
  $("user_lang").style.display = "none";
}

function addPerson() {
  var data = "";
  data = document.form1.group.value;
  var strLang = document.form1.langstr.value;
  var strGrade = document.form1.gradestr.value;
  var strNumber = document.form1.numberstr.value;
  var data = data.substring(1,data.length-1);
  if(data.length > 0)  {  //已报名编组为空
    data += ',';
    strLang += ',';
    strGrade += ',';
    strNumber += ',';
  }
 
  if(document.form1.lang.selectedIndex == 8) { //手填的语言
    strLang +=  document.form1.language.value;
    data += '{"language":"' + document.form1.language.value + '",';
  }
  else {
    strLang += document.form1.lang.options[document.form1.lang.selectedIndex].value;
    data += '{"language":"' + document.form1.lang.options[document.form1.lang.selectedIndex].value + '",';
  }
  
  data +='"languageLevel":' + document.form1.languageLevel.value + ',';
  strGrade += document.form1.languageLevel.value;
  if( isNaN(parseInt(document.form1.amount.value)) || parseInt(document.form1.amount.value)<=0 ) {
    document.form1.amount.focus();
    document.form1.amount.select();
    //document.form1.amount.value = 0;
    return;
  }
  data +='"amount":' + document.form1.amount.value + '}';
  strNumber += document.form1.amount.value;
  
  document.form1.group.value='[' + data + ']';
  document.form1.langstr.value = strLang;
  document.form1.gradestr.value = strGrade;
  document.form1.numberstr.value = strNumber;
  
  var str = "";
  var strFunc = "";
  var total = 0;
  grade=["精通","良好","一般"];
  var arr = eval(document.form1.group.value);
  for(i=0; i<arr.length; i++){
    str += arr[i].language + "," + grade[arr[i].languageLevel] + "," + arr[i].amount +"=";
  }
  strFunc = str.substr(0, str.length - 1);
  $("strValue").value = strFunc;
}
  
function dispMessage() {
  var str = "";
  var total = 0;
  grade=["精通","良好","一般"];
  var arr = eval(document.form1.group.value);
  for(i=0; i<arr.length; i++){
    str += arr[i].language + "(" + grade[arr[i].languageLevel] + ")志愿者: ";
    str += arr[i].amount + "人<br />";
    total += arr[i].amount;
  }
  $("dispMsg").innerHTML = str;
  $("totalCount").innerHTML = "报名人数总计："+ total;
}

function resetForm() {
  $("dispMsg").innerHTML = "";
  $("tip").innerHTML = "（可填写描述文字1500字以内）";
  //document.form1.group.value = "";
  var mobile = /(\S)+[@]{1}(\S)+[.]{1}(\w)+/;
  alert(mobile.test(document.form1.email.value));
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

function checkForm() {
  var mail=/(\S)+[@]{1}(\S)+[.]{1}(\w)+/;
  return false;
  var mobile=/\d{11}/;
  if(document.form1.intro.value.length<20 || document.form1.intro.value.lenght>1500 
     || !mail.test(document.form1.email.value)  || !mobile.test(document.form1.tel.value) ){
    return false;
  }
  return true;
}

function doInit(){
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
             <td class="TableData">外语语种</td>
             <td class="TableData">
              <select name="lang" onchange="userdef(this)" name="languageKind" id="languageKind">
                <option value="英语">英语</option>
                <option value="法语">法语</option>
                <option value="西班牙语">西班牙语</option>
                <option value="阿拉伯语">阿拉伯语</option>
                <option value="俄语">俄语</option>
                <option value="德语">德语</option>
                <option value="日语">日语</option>
                <option value="韩语">韩语</option>
                <option value="其他">其他</option>
               </select>
             <span id="user_lang" style="display: none">
            <input name="language" id="language" type="text" class="style-3" size="10" value="请输入语种" onclick="this.value=''"/>
            </span>
             </td>
            </tr>
             <tr>
             <td class="TableData">外语水平</td>
                <td class="TableData">
                  <select name="languageLevel" id="languageLevel">
                  <option value="0">精通</option>
                  <option value="1" selected="selected">良好</option>
                  <option value="2">一般</option>
                  </select>
                </td>
            </tr>
			<tr>
			  <td class="TableData">可参加志愿者数量</td>
			  <td class="TableData"><input name="amount" id="amount" type="text" class="BigInput" size="5" />
                 <input type="button" value=" 增加 " onclick="addPerson();dispMessage();" />
             </td>
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
              <div id="totalCount">&nbsp;</div>
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
                <input name="principalTel1" id="principalTel1" type="text" class="BigInput" value="010" size="5" />-<input name="principalTel2" id="principalTel2" type="text" class="BigInput" size="10" /></
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
            <input type="hidden" value="" id="at1Vlaue" name="at1Vlaue"/>
            <input type="hidden" value="" id="at2Vlaue" name="at2Vlaue"/>
			<input type="hidden" value="YH" id="recordSource" name="recordSource"/>
			<input type="reset" value="重置" class="BigButton" />
			<input type="button" value="提交" class="BigButton" onclick="submitForm()" />
			</td>
			</tr>
			</table>
		</form>
	</div>
</body>
</html>