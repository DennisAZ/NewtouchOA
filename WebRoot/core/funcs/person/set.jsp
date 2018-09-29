<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>批量设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/person/js/personUtil.js"></script>
<script Language="JavaScript">
var flag = 1;  //标记能否添加部门  1可以，0不可以


function doInit(){
  theme();
  deptFunc();
  var url =  contextPath + "/yh/core/funcs/person/act/YHPersonAct/getSetLogList.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"userId",width: 90, text:"用户", render:getUserName},   
       {type:"data", name:"time",width: 130, text:"时间" ,render:timeFunc},       
       {type:"data", name:"ip", width: 90, text:"IP地址", render:ipFunc},
       {type:"data", name:"type",width: 90, text:"类型", render:typeRender}, 
       {type:"data", name:"remark", width: 150, text:"备注"}]
  };
  var pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  //$('numUser').innerHTML = total;
  if(total){
    showCntrl('listContainer');
    //var mrs = " 共 "+ total + " 个符合条件且可管理的用户";
    //WarningMsrg(mrs, 'msrg');
  }else{
    WarningMsrg('无日志记录', 'msrg');
  }
  var mgr = new SelectMgr();
  mgr.addSelect({cntrlId: "userPriv"
           , tableName: "USER_PRIV"
           , codeField: "SEQ_ID"
           , nameField: "PRIV_NAME"
           , value: "0"
           , isMustFill: "0"
           , filterField: "USER_ID"
           , filterValue: ""
           , order: "PRIV_NO"
           , reloadBy: "desc"
           , actionUrl: ""
           ,extData:""
  });
  mgr.loadData();
  mgr.bindData2Cntrl();

  var mgrs = new SelectMgr();
  mgrs.addSelect({cntrlId: "dutyType"
           , tableName: "oa_attendance_conf"
           , codeField: "SEQ_ID"
           , nameField: "DUTY_NAME"
           , value: "0"
           , isMustFill: "0"
           , filterField: "DUTY_TYPE"
           , filterValue: ""
           , order: ""
           , reloadBy: ""
           , actionUrl: ""
           ,extData:""
  });
  mgrs.loadData();
  mgrs.bindData2Cntrl();

  //设置密码显示长度 min-max  显示是否出现"必须同时包含字母和数字" 如果字段SEC_PASS_SAFE=1 就出现

  var setPassMinStr = "";
  var setPassMaxStr = "";
  var setPassSafeStr = "";
  var paraNameStr = "";
  var paraValueStr = "";
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getPasswordLength.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    for(var i = 0; i < rtJson.rtData.length; i++){
      paraNameStr = rtJson.rtData[i].paraName || "";
      paraValueStr = rtJson.rtData[i].paraValue || "";
      if(paraNameStr == "SEC_PASS_MIN"){
        if(paraValueStr == "null" || paraValueStr == ""){
          setPassMinStr = "8";
        }else{
          setPassMinStr = paraValueStr;
        }
      }else if(paraNameStr == "SEC_PASS_MAX"){
        if(paraValueStr == "null" || paraValueStr == ""){
          setPassMaxStr = "20";
        }else{
          setPassMaxStr = paraValueStr;
        }
      }else if(paraNameStr == "SEC_PASS_SAFE"){
        setPassSafeStr = paraValueStr;
      }
    }
  }else{
  	alert(rtJson.rtMsrg); 
  }
  $('pass1Min').innerHTML = setPassMinStr;
  $('pass1Max').innerHTML = setPassMaxStr;
  $('pass2Min').innerHTML = setPassMinStr;
  $('pass2Max').innerHTML = setPassMaxStr;
  if(setPassSafeStr == "1"){
    $('pass1Desc').innerHTML = "，必须同时包含字母和数字 ";
    $('pass2Desc').innerHTML = "，必须同时包含字母和数字 ";
  }
}

function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"280\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  if(msrgDom != "" || msrgDom!= null){
    $(cntrlId).innerHTML = msrgDom;
  }
  
}

function getUserName(cellData, recordIndex, columIndex) {
  var userId = this.getCellData(recordIndex,"seqId");
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getUserName.act?userId="+cellData;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == '0'){
    return "<center>"+rtJson.rtData+"</center>";
  }
}

function addDept() {
  var URL="<%=contextPath %>/core/funcs/person/persontreelist.jsp";
   if(flag == 1){
     openDialog(URL,'300', '350');
   }
}
 
//function clearDept() {
  //document.getElementById("deptIdDesc").value = "";
  //document.getElementById("deptId").value = "";
//}
/**
 * 离职人员/外部人员标识  标记能否添加部门  1可以，0不可以

 */
//function flagDept(){
  //if(document.getElementById("separations").checked == true){
  //  flag = 0;
  //  clearDept();
  //}else{
  //  flag = 1;
  //}
//}

/**
 * 验证密码
 */
function checkPassword(){
  var reg = /^[a-zA-Z0-9]*$/;
  var setPassMin = 0;
  var setPassMax = 0;
  var setPassSafe = "";
  var paraNames = "";
  var paraValues = "";
  var pass1 = $('pass1').value;
  var pass2 = $('pass2').value;
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getPasswordLength.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    for(var i = 0; i < rtJson.rtData.length; i++){
      paraNames = rtJson.rtData[i].paraName;
      paraValues = rtJson.rtData[i].paraValue;
      if(paraNames == "SEC_PASS_MIN"){
        if(paraValues == "null" || paraValues == ""){
          setPassMin = "8";
        }else{
          setPassMin = paraValues;
        }
      }else if(paraNames == "SEC_PASS_MAX"){
        if(paraValues == "null" || paraValues == ""){
          setPassMax = "20";
        }else{
          setPassMax = paraValues;
        }
      }else if(paraNames == "SEC_PASS_SAFE"){
        setPassSafe = paraValues;
      }
    }
  }else{
  	alert(rtJson.rtMsrg); 
  }

  if(pass1.length < setPassMin || pass1.length > setPassMax){
     alert("错误,密码长度应"+setPassMin+"-"+setPassMax+"位!");
     $('pass1').focus();
     $('pass1').select();
     return false;
  }

  if(pass2.length < setPassMin || pass2.length > setPassMax){
    alert("错误,密码长度应"+setPassMin+"-"+setPassMax+"位!");
    $('pass2').focus();
    $('pass2').select();
    return false;
  }
  
  if(pass1!=pass2){
    alert("错误,输入的密码不一致！");
    $('pass1').focus();
    $('pass1').select();
    return false;
  }

  if(setPassSafe == "1" && !reg.test(pass1)){
  	alert("错误,密码必须同时包含字母和数字!");
  	$('pass1').focus();
    $('pass1').select();
  	return false;
  }
  return true;
}

function commit(){
  if(!CheckForm()){
    return;
  }
  if($('pass1').value != ""){
    if(!checkPassword()){
      return;
    }
  }
  var reg1 = /[^\d]/g;
  str = $('emailCapacity').value;
  if (str && str.match(reg1)) {
    alert("内部邮箱容量只能为数字！");
    return ;
  }
  str = $('folderCapacity').value;
  if (str && str.match(reg1)) {
    alert("个人文件柜容量只能为数字！");
    return ;
  }
  str = $('webmailNum').value;
  if (str && str.match(reg1)) {
    alert("Internet邮箱数量只能为数字！");
    return ;
  }
  str = $('webmailCapacity').value;
  if (str && str.match(reg1)) {
    alert("每个Internet邮箱容量只能为数字！");
    return ;
  }
  var query = "";
  query = $("form1").serialize();
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/addSet.act?flag="+flag;
  var rtJson = getJsonRs(url,query);
  if(rtJson.rtState == '0'){
    location = "<%=contextPath%>/core/funcs/person/setupdate.jsp";
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function CheckForm(){
  if($('dept').value == "" && $('role').value == "" && $('user').value==""){ 
    alert("请指定人员范围！");
    return false;
  }
  if($('myTableLeft').value == "" && $('myTableRight').value == "" && $('shortcut').value == "" 
    && $('privId1').value == "" && $('userPriv').value == "" && $('deptId').value == "" 
    && $('theme').value == "" && $('bkground').value == "" 
    && $('menuType').value == "" && $('smsOn').value == "" && $('callSound').value == "" 
    && $('panel').value == "" && $('dutyType').value == "" && $('pass1').value == ""
    && $('emailCapacity').value == "" && $('folderCapacity').value == "" && $('webmailNum').value == "" 
    && $('webmailCapacity').value == "" && $('webmailCapacity').value == ""){
    alert("请选择要修改的选项设置！");
    return false;
  }
  return true;
}
/**
 * 空密码用户

 */
function noPassUser(){
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getNoPassUser.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == '0'){
    $('user').value = rtJson.rtData[0].userId;
    $('userDesc').value = rtJson.rtData[0].userName;
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function select_sound(){
 
}

function bkChange(value){
  if(value == "" || value == "0"){
    document.getElementById('bkPreview').style.display = 'none';
  }else{
    //document.getElementById('bkPreview').href = '/attachment/background/'+value;
    //document.getElementById('bkPreview').style.display = '';
  }
}

var mytable = null;
var mytableDesc = null;
/**
 * 桌面模块(右侧)
 */
function rightMytable(){
  mytable = $('myTableRight');
  mytableDesc = $('myTableRightDesc');
  
  var url = '<%=contextPath%>/core/funcs/setdescktop/setports/mytableconfig.jsp?pos=r&selected=' + $('myTableRight').value;
  window.showModalDialog(url, window, "dialogWidth:600px;dialogHeight:400px;");
}
/**
 * 桌面模块(左侧)
 */
function leftMytable(){
  mytable = $('myTableLeft');
  mytableDesc = $('myTableLeftDesc');
  
  var url = '<%=contextPath%>/core/funcs/setdescktop/setports/mytableconfig.jsp?pos=l&selected=' + $('myTableLeft').value;
  window.showModalDialog(url, window, "dialogWidth:600px;dialogHeight:400px");
}
/**
 * 菜单快捷组
 */
function selectShortcut(){
  window.shortcut = $('shortcut');
  window.shortcutDesc = $('shortcutDesc');
  
  var url = '<%=contextPath%>/core/funcs/setdescktop/shortcut/shortcut.jsp?selected=' + $('shortcut').value;
  window.showModalDialog(url, window, "dialogWidth:600px;dialogHeight:400px");
}

/**
 * 默认界面主题
 * @return
 */

function theme(){
  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getTheme.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var select = document.getElementById("theme");
    //select.value = "0";
    for(var i = 0; i < rtJson.rtData.length; i++) {
      var option = document.createElement("option");
      option.value = rtJson.rtData[i].value;
      option.innerHTML = rtJson.rtData[i].text;
      select.appendChild(option);
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function getMyTheme(){
  //alert('开发中。。。');
  //&nbsp;<a href="javascript:" onClick="getMyTheme();">设置为我的界面主题</a>
}

function deptFunc(){
  var url = "<%=contextPath%>/yh/core/funcs/system/sealmanage/act/YHSealAct/selectDeptToAttendance.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("deptId");
  for(var i = 0; i < prcs.length; i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  var option = document.createElement("option"); 
  option.value = "0";
  option.innerHTML = "离职人员/外部人员"; 
  selects.appendChild(option);
  return userId;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/sys_config.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;批量用户个性设置</span>
    </td>
  </tr>
</table>
<br/>
<form method="post" name="form1" id="form1">
  <table class="TableBlock" width="100%" align="center">
   <tr>
      <td nowrap class="TableData" width="150">范围(部门)：</td>
      <td class="TableData">
        <input type="hidden" name="dept" id="dept" value="">
        <textarea cols=30 name="deptDesc" id="deptDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectDept()">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">范围(角色)：</td>
      <td class="TableData">
        <input type="hidden" name="role" id="role" value="">
        <textarea cols=30 name="roleDesc" id="roleDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableData">范围(人员)：</td>
      <td class="TableData">
        <input type="hidden" name="user" id="user" value="">
        <textarea cols=30 name="userDesc" id="userDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
        <br><a href="javascript:noPassUser();">空密码用户</a>&nbsp;
        <a href="javascript:;" class="orgAdd" onClick="selectUserExternalSelect(['user','userDesc']);">添加离职人员</a>
      </td>
   </tr>
    <tr>
      <td nowrap class="TableData">桌面模块(左侧)：</td>
      <td class="TableData">
        <input type="hidden" name="myTableLeft" id="myTableLeft" value="">
        <textarea cols=30 name="myTableLeftDesc" id="myTableLeftDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="leftMytable()">选择</a>
        <a href="javascript:;" class="orgClear" onClick="$('myTableLeft').value='';$('myTableLeftDesc').value='';">清空</a>
      </td>
   </tr>
    <tr>
      <td nowrap class="TableData">桌面模块(右侧)：</td>
      <td class="TableData">
        <input type="hidden" name="myTableRight" id="myTableRight" value="">
        <textarea cols=30 name="myTableRightDesc" id="myTableRightDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="rightMytable()">选择</a>
        <a href="javascript:;" class="orgClear" onClick="$('myTableRight').value='';$('myTableRightDesc').value='';">清空</a>
      </td>
   </tr>
    <tr>
      <td nowrap class="TableData">菜单快捷组：</td>
      <td class="TableData">
        <input type="hidden" name="shortcut" id="shortcut" value="">
        <textarea  cols=30 name="shortcutDesc" id="shortcutDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectShortcut()">选择</a>
        <a href="javascript:;" class="orgClear" onClick="$('shortcut').value='';$('shortcutDesc').value='';">清空</a>
      </td>
   </tr>
   <tr style="display:none;">
      <td nowrap class="TableData">通讯范围：</td>
      <td class="TableData">
        <input type="hidden" name="privId1" id="privId1" value="">
        <textarea  cols=30 name="privId1Desc" id="privId1Desc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole(['privId1','privId1Desc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('privId1').value='';$('privId1Desc').value='';">清空</a>
        <br>所选角色的人员可以给上边选择范围内的用户发送邮件和短消息

      </td>
   </tr>
   <tr>
    <td nowrap class="TableData">主角色：</td>
    <td nowrap class="TableData">
        <select name="userPriv" id="userPriv" class="BigSelect">
      </select>
    </td>
   </tr>
   
   <tr>
    <td nowrap class="TableData">部门：</td>
    <td nowrap class="TableData">
      <select id="deptId" name="deptId" style="height:22px;FONT-SIZE: 12pt;">
      <option value=""></option>
        </select>
      &nbsp;如设置为离职人员/外部人员，将对其他用户不可见
    </td>
  </tr>

    <tr>
      <td nowrap class="TableData">界面主题：</td>
      <td class="TableData">
        <select name="theme" id="theme" class="BigSelect" style="width:80px">
          <option value=""></option>
        </select>&nbsp;需重新登录才能生效
      </td>
    </tr>

    <tr style="display:none">
      <td nowrap class="TableData">桌面背景图片：</td>
      <td class="TableData">
         <select name="bkground" id="bkground" class="BigSelect" onchange="bkChange(this.value)">
            <option value=""></option>
            <option value="0">开发中...</option>
         </select>&nbsp;
      	<a id="bkPreview" href="" target="_blank" style="display:none;">预览</a>
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData">登录模式：</td>
      <td class="TableData">
        <select name="menuType" id="menuType" class="BigSelect">
          <option value=""></option>
          <option value="1">在本窗口打开OA</option>
          <option value="2">在新窗口打开OA，显示工具栏</option>
          <option value="3">在新窗口打开OA，无工具栏</option>
        </select> 需重新登录才能生效
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">短信息提醒窗口弹出方式：</td>
      <td class="TableData">
        <select name="smsOn" id="smsOn" class="BigSelect">
          <option value=""></option>
          <option value="1">自动</option>
          <option value="0">手动</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">短信息提示音：</td>
      <td class="TableData">
        <select name="callSound" id="callSound" class="BigSelect" onchange="select_sound()">
          <option value=""></option>
          <option value="9">长语音</option>
          <option value="8">短语音</option>
          <option value="2">激光</option>
          <option value="3">水滴</option>
          <option value="4">手机</option>
          <option value="5">电话</option>
          <option value="6">鸡叫</option>
          <option value="7">OICQ</option>
          <option selected="selected" value="0">无</option>
        </select>
        <div align="right" id="sms_sound"></div>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">登录后显示的左侧面板：</td>
      <td class="TableData">
        <select name="panel" id="panel" class="BigSelect">
          <option value=""></option>
          <option value="1">导航</option>
          <option value="2">组织</option>
          <option value="3">短信箱</option>
          <option value="4">搜索</option>
        </select>
        需重新登录才能生效
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">考勤排班类型：</td>
      <td class="TableData">
        <select name="dutyType" id="dutyType" class="BigSelect">
        </select>
      </td>
    </tr>
   <tr>
    <td nowrap class="TableData" width="60" >密码：</td>
    <td nowrap class="TableData">
        <input type="password" name="pass1" id="pass1" class="BigInput" size="20" maxlength="" value=""> <span id="pass1Min"></span>-<span id="pass1Max"></span>位<span id="pass1Desc"></span>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="60">确认密码：</td>
    <td nowrap class="TableData">
        <input type="password" name="pass2" id="pass2" class="BigInput" size="20" maxlength="" value=""> <span id="pass2Min"></span>-<span id="pass2Max"></span>位<span id="pass2Desc"></span>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">内部邮箱容量：</td>
    <td nowrap class="TableData">
        <input type="text" name="emailCapacity" id="emailCapacity" class="BigInput" size="5" maxlength="11" value="">&nbsp;MB
        0表示不限制大小

    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">个人文件柜容量：</td>
    <td nowrap class="TableData">
        <input type="text" name="folderCapacity" id="folderCapacity" class="BigInput" size="5" maxlength="11" value="">&nbsp;MB
        0表示不限制大小

    </td>
   </tr>
   <tr style="display:none">
    <td nowrap class="TableData">Internet邮箱数量：</td>
    <td nowrap class="TableData">
        <input type="text" name="webmailNum" id="webmailNum" class="BigInput" size="5" maxlength="11" value="">&nbsp;个

        0表示不限制数量

    </td>
   </tr>
   <tr  style="display:none">
    <td nowrap class="TableData">每个Internet邮箱容量：</td>
    <td nowrap class="TableData">
        <input type="text" name="webmailCapacity" id="webmailCapacity" class="BigInput" size="5" maxlength="11" value="">&nbsp;MB
        0表示不限制大小

    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="button" value="批量设置" class="BigButton" onclick="commit();">
    </td>
   </tr>
  </table>
</form>
<br/>
<br/>
<table border="0" width="95%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/green_arrow.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;最近10次批量个性设置日志</span><br>
    </td>
  </tr>
</table>
<br/>
<div id="listContainer" style="display:none">
</div>
<div id="msrg"></div>
</body>
</html>