<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.global.YHSysProps" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
  String deptId = request.getParameter("deptId");
  if (deptId == null) {
    deptId = "";
  }
  //System.out.println("Encoding>>" + request.getCharacterEncoding());
  String userName = request.getParameter("userName");
  //userName = new String(userName.getBytes("iso-8859-1"), "utf-8");
  if (userName == null) {
    userName = "";
  }
  String userId = request.getParameter("userId");
  if (userId == null) {
    userId = "";
  }
  
  String bindUserOther = YHSysProps.getString("BIND_USERS_OTHERS");
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  boolean isAdmin = loginUser.isAdmin();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/person/js/personUtil.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var deptId = "<%=deptId%>";
var userIdStr = "<%=userId%>";
var userNameStr = "<%=userName%>";
var postPriv = <%=loginUser.getPostPriv()%>;
var notLogin = <%=loginUser.getNotLogin()%>;
var isAdmins = <%=isAdmin%>;
var jso = [];
var userInfo = null;
var bindIp = null;
var remark = null;
var deptIdList = null;
var idFlag = null;
var flag = 1;  //标记能否添加部门  1可以，0不可以

var notLoginFlag = null; //标记读取数据时是否禁止登录OA： 0 不可以， 1可以


//是否显示绑定用户模块
var bindUserOther = "<%=bindUserOther%>";
  
function checkUserId() {
  var userIdOld = document.getElementById("userIdOld").value;
  var userId = document.getElementById("userId").value;
  var url = contextPath + "/yh/core/funcs/person/act/YHPersonAct/selectPerson.act";
  if (seqId) {
    if (userIdOld != userId) {
      var rtJson = getJsonRs(url, "userId=" + userId);
      if (rtJson.rtState == "0") {
        var divNode = document.getElementById("userIdSpan");
        if (rtJson.rtData[0].num == 1) {
          divNode.innerHTML = "<img src='<%=imgPath%>/dtree/error.gif' />该用户名已存在";
        } else if (rtJson.rtData[0].num == 0) {
          divNode.innerHTML = "<img src='<%=imgPath%>/dtree/correct.gif' />";
        }
      }
    }
  } else {
    var rtJson = getJsonRs(url, "userId=" + userId) ;
    if (rtJson.rtState == "0") {
      var divNode = document.getElementById("userIdSpan");
      if (rtJson.rtData[0].num >= 1) {
        divNode.innerHTML = "<img src='<%=imgPath%>/dtree/error.gif' />该用户名已存在";
      } else if (rtJson.rtData[0].num == 0 && userId != "") {
        divNode.innerHTML = "<img src='<%=imgPath%>/dtree/correct.gif' />";
      }
    }
  }
}

//function addPriv() {
//  var URL="<%=contextPath %>/core/funcs/person/personlist.jsp";
//  openDialog(URL,'300', '350');
//}

//function clearPriv() {
//  document.getElementById("postPrivDesc").value = "";
//  document.getElementById("postPriv").value = "";
//}

//function selectOwnerDept() {
//  var URL="<%=contextPath %>/core/funcs/person/deptselecttreelist.jsp";
//  openDialog(URL,'300', '350');
//}

//function clearOwnerDept() {
//  document.getElementById("postDeptDesc").value = "";
//}

function addDept() {
  var URL="<%=contextPath %>/core/funcs/person/deptIframe.jsp";
  if (flag == 1) {
    openDialog(URL,'300', '300');
  }
}
  	
function clearDept() {
  //document.getElementById("deptIdDesc").value = "";
  //document.getElementById("deptId").value = "";
}

function selectDeptOther() {
  var deptOther = document.getElementById("deptOther");
  if (deptOther.style.display == "none") {
    deptOther.style.display = '';
  } else {
    deptOther.style.display = "none";
  }
}

function selectPriv() {
  var priv = document.getElementById("privs");
  if (priv.style.display == "none") {
    priv.style.display = "";
  } else {
    priv.style.display = "none";
  } 
}

function manageDept(idVal) {
  var manageDept = document.getElementById("manageDept");
  if (idVal == "0") {
    manageDept.style.display = "none";
  } else if (idVal == "1") {
    manageDept.style.display = "none";
  } else if (idVal == "2") {
    manageDept.style.display = '';
  }
}

function goBack() {
  window.location.href = location = "<%=contextPath%>/core/funcs/person/role/usernew.jsp?deptId="+deptId;
}

function deptFunc(){
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptAct/getDeptSelectFunc.act";
  var rtJson = getJsonRs(url, "deptId="+deptId);
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
    if(prc.value == deptId){
      option.selected = true;
    }
    selects.appendChild(option);
  }
  if(postPriv == "1"){
    var option = document.createElement("option"); 
    option.value = "0";
    option.innerHTML = "离职人员/外部人员"; 
    if(deptId == "0"){
      option.selected = true;
    }
    selects.appendChild(option);
  }
  return userId;
}

function doInit() {
	 //isSecPriv();
  jso = [
      {title:"用户基本信息", onload:showUserInfo.bind(window, "baseInfo"), useTextContent:true, contentUrl:"<%=contextPath %>/core/funcs/person/role/offdutypersonbasicinfo.jsp?userIdStr="+userIdStr+"&postPriv="+postPriv, useIframe:false}
        ]; 

  if (bindUserOther == '1'){
    var bindTab = {title:"绑定其他系统", onload:showUserInfo.bind(window, "bindUsers"), useTextContent:true, contentUrl:"<%=contextPath %>/core/funcs/person/bindUsers.jsp", useIframe:false};
    jso.push(bindTab);
  }
    
  loadData();
  buildTab(jso, 'contentDiv');
  $("contentDiv").setStyle({
    overflowY: "auto",
    overflowX: "hidden"
  });
  
  if(userIdStr != "admin" ||userIdStr != "system"){
    var url = contextPath + "/yh/core/funcs/person/act/YHPersonAct/getUserPrivByNo.act";
    var json = getJsonRs(url);
    var userPriv = document.getElementById("userPriv");
    if (json.rtState == '0') {
      var options = json.rtData;
      for (var i = 0; i< options.length; i++) {
        var option = document.createElement("option");
        option.value = options[i].privNo;
        option.innerHTML = options[i].privName;
        userPriv.appendChild(option);
      }
      if (seqId) {
        userPriv.value = userInfo.userPriv;
      }

    }
  }else{
      userPriv.disabled=true;
  }
  if (seqId) {
    //$('userId').readOnly = true;
    $('userId').disabled = true;
    if(userIdStr != "admin"){
      $("modulePrivDiv").style.display = "";
    }
    if($("role") && $("role").value.trim()){
      bindDesc([{cntrlId:"role", dsDef:"USER_PRIV,SEQ_ID,PRIV_NAME"}]);
    }
    if($("deptId") && $("deptId").value.trim() && $("deptId").value != 0){
      bindDesc([{cntrlId:"deptId", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
    }
    if($("dept") && $("dept").value.trim() && $("dept").value != "0" && $("dept").value != "ALL_DEPT"){
      bindDesc([{cntrlId:"dept", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
    }else if($("dept") && ($("dept").value == "0" || $("dept").value == "ALL_DEPT")){
      $("dept").value = "0";
      $("deptDesc").value = "全体部门";
    }
    
    if($("postDeptId") && $("postDeptId").value.trim()){
      bindDesc([{cntrlId:"postDeptId", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
    }
    if($("postDept") && $("postDept").value.trim()){
      bindDesc([{cntrlId:"postDept", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
    }
  } 
  if($("dept").value.trim()){
    $("deptOther").style.display = '';
  }else{
    $("deptOther").style.display = "none";
  }
  if($("role").value.trim()){
    $("privs").style.display = '';
  }else{
    $("privs").style.display = "none";
  }
  deptFunc();
}

function loadData() {    
  if (seqId) {
    var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getOffDutyPerson.act";
    var rtJson = getJsonRs(url, "seqId=" + seqId);
    if (rtJson.rtState == "0") {
      deptIdList = rtJson.rtData.deptId;
      document.getElementById("userIdOld").value = rtJson.rtData.userId;
      document.getElementById("bynameOld").value = rtJson.rtData.byname;
      userInfo = rtJson.rtData;
      //userInfo.birthday = userInfo.birthday.substring(0,10);
    } else {
      alert(rtJson.rtMsrg); 
    }   
  }
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

function modulePriv(){
  var width=Math.round((window.screen.width-400)/2);
  var height=Math.round((window.screen.height-200)/2);
  var userNameFunc = $("userName").value;
  window.open('<%=contextPath%>/core/funcs/modulepriv/index.jsp?uid='+seqId+'&userName='+encodeURIComponent(userNameFunc) , 'newwindow', 
      'height=300, width=600, top='+height+', left='+width+', toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
}

function showUserInfo() {
  var id = arguments[0]; 
  if (!userInfo) {     //新建（没有数据）
    var mgrs = new SelectMgr();
    mgrs.addSelect({cntrlId: "dutyType"
             , tableName: "oa_attendance_conf"
             , codeField: "SEQ_ID"
             , nameField: "DUTY_NAME"
             , value: "5"
             , isMustFill: "1"
             , filterField: "DUTY_TYPE"
             , filterValue: ""
             , order: ""
             , reloadBy: ""
             , actionUrl: ""
             ,extData:""
    });
    mgrs.loadData();
    mgrs.bindData2Cntrl();
    if (id == 'baseInfo') { 
      $('openedAutoSelect').value = 1;
      if (deptId == 0) {
        //$('separation').checked = true;
        //flagDept();
        $('userDept').innerHTML = "离职人员/外部人员";
      } else {
        //$('deptId').value = deptId;
        //if($("deptId") && $("deptId").value.trim() && $("deptId").value != 0){
        //  bindDesc([{cntrlId:"deptId", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
        //}
        var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getDeptName.act";
        var rtJsons = getJsonRs(urls , "deptId=" +  deptId);
        if(rtJsons.rtState == '0'){
          $('userDept').innerHTML = rtJsons.rtData;
        }
      }
    } 
    if (id == 'privInfo') { 
      $('openedFlowDispatch').value = 1;
    } 
    if (id == 'otherInfo') { 
      $('openedWarnDispatch').value = 1;
    } 
    if (id == 'selfDef') { 
      theme();
      idFlag = "1";
      $('openedOtherDispatch').value = 1;
      var beginParameters = {
        inputId:'birthday',
        property:{isHaveTime:false}
        ,bindToBtn:'beginDateImg'
      };
      new Calendar(beginParameters);
      
    } 
    return;
  }
    //编辑
  var mgr = new SelectMgr();
    mgr.addSelect({cntrlId: "dutyType"
             , tableName: "oa_attendance_conf"
             , codeField: "SEQ_ID"
             , nameField: "DUTY_NAME"
             , value: "5"
             , isMustFill: "1"
             , filterField: "DUTY_TYPE"
             , filterValue: ""
             , order: ""
             , reloadBy: ""
             , actionUrl: ""
             ,extData:""
    });
  mgr.loadData();
  mgr.bindData2Cntrl();
  //根据标签页设置智能选人 
  var id = arguments[0]; 
  if(id == 'baseInfo'){ 
    $('openedAutoSelect').value = 1;
    setBaseinfo(); 
  } 
  if(id == 'privInfo'){ 
    $('openedFlowDispatch').value = 1;
    setPrivInfo(); 
  } 
  if(id == 'otherInfo'){ 
    $('openedWarnDispatch').value = 1;
    setOtherInfo(); 
  } 
  if(id == 'selfDef'){
    theme();
    idFlag = "selfDef"; 
    $('openedOtherDispatch').value = 1;
    $('passwordDiv').style.display = "none";
    setSelfDef(); 
    var beginParameters = {
        inputId:'birthday',
        property:{isHaveTime:false}
        ,bindToBtn:'beginDateImg'
    };
    new Calendar(beginParameters);
  }
  if (id == 'bindUsers'){
    setBindInfo();
  } 
}

function flagDept() {
  if (document.getElementById("separation").checked == true) {
    flag = 0;
    clearDept();
  } else {
    flag = 1;
  }
}

function setBaseinfo(){
  var deptOthers = document.getElementById("deptOther");
  var manageDept = document.getElementById("manageDept");
  var privs = document.getElementById("privs");
  $('userId').value = userInfo.userId;
  $('userName').value = userInfo.userName;
  if(userIdStr != "admin"){
    $('userPriv').value = userInfo.userPriv;
  }
  
  $('role').value = userInfo.userPrivOther;
  if($('role').value){
    privs.style.display = '';
  }
  $('deptId').value = userInfo.deptId;
  deptId = userInfo.deptId;
  $('dept').value = userInfo.deptIdOther;
  if($('dept').value){
    deptOthers.style.display = "";
  }
  if(userInfo.deptId == "0"){
    //document.getElementById("separation").checked = true;
    flag = 0;
  }
  if(userIdStr != "admin"){
    if(userInfo.postPriv == "0"){
      $('postPriv').value = userInfo.postPriv;
    }else if(userInfo.postPriv == "1"){
      $('postPriv').value = userInfo.postPriv;
    }else if(userInfo.postPriv == "2"){
      $('postPriv').value = userInfo.postPriv;
      manageDept.style.display = '';
    }else{
      $('postPriv').value = "0";
    }
  }
  $('postDeptId').value = userInfo.postDept;

  $('dutyType').value = userInfo.dutyType;
}
  
function setPrivInfo(){
  $('userNo').value = userInfo.userNo;
  //$('canbroadcast').value = userInfo.canbroadcast;
  notLoginFlag = userInfo.notLogin;
  if(userInfo.notLogin == "1"){
    $('notLogin').checked = true;
  }
  if(userInfo.notViewUser == "1"){
    $('notViewUser').checked = true;
  }
  if(userInfo.notViewTable == "1"){
    $('notViewTable').checked = true;
  }
  if(userInfo.useingKey == 1){
    $('useingKey').checked = true;
  }
 // $('imRange').value = userInfo.imRange;
 //alert(userInfo.imRange);
  $('canbroadcast').value = userInfo.canbroadcast;
  /*
  var ops = $('imRange').getElementsByTagName("option");
  for (var i = 0 ;i < ops.length ; i++) {
    var op = ops[i];
    if (op.value == userInfo.imRange) {
       op.selected = true;
    }
  }*/
  $('imRange').value = userInfo.imRange;
  //setTimeout("setImRange("+userInfo.imRange+")" , 1000);
}

function setImRange(value) {
  var ops = $('imRange').getElementsByTagName("option");
  for (var i = 0 ;i < ops.length ; i++) {
    var op = ops[i];
    if (op.value == value) {
       op.selected = true;
    }
  }
}
function setOtherInfo(){
  $('emailCapacity').value = userInfo.emailCapacity;
  $('folderCapacity').value = userInfo.folderCapacity;
  if(userInfo.webmailNum == 0){
    $('webmailNum').value = "";
  }else{
    $('webmailNum').value = userInfo.webmailNum;
  }
  if(userInfo.webmailCapacity == 0){
    $('webmailCapacity').value = "";
  }else{
    $('webmailCapacity').value =userInfo.webmailCapacity;
  }
  $('bindIp').value = userInfo.bindIp;
  $('remark').value = userInfo.remark;
}

function setSelfDef(){
  $('byname').value = userInfo.byname;
  $('sex').value = userInfo.sex;
  $('birthday').value = userInfo.birthday.substr(0,10);
  $('theme').value = userInfo.theme;
  $('mobilNo').value = userInfo.mobilNo;
  $('email').value = userInfo.email;
  $('telNoDept').value = userInfo.telNoDept;
  if(userInfo.mobilNoHidden == "1"){
    $('mobilNoHidden').checked = true;
  }
}

function checkBasicInfo() {
  var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])$/;
  var emailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
  //  var bindIp = document.getElementById("bindIp");
//  if (!reg.test(bindIp.value)) {
//  alert("起始IP格式不合法");
//  bindIp.select();
//  bindIp.focus();
//  return false;
//  }

  var cntrl = document.getElementById("userId");
  if (!cntrl.value) {
    alert("用户名不能为空！");
    selectLast(cntrl);
   //cntrl.focus();
    return false;
  }
  cntrl = document.getElementById("userName"); 
  if (!cntrl.value) {
    alert("真实姓名不能为空！");
    selectLast(cntrl);
    return false;
  }
  
  cntrl = document.getElementById("postPriv"); 
  if (cntrl && !cntrl.value) {
    alert("管理范围不能为空！");
    selectLast(cntrl);
    return false;
  }

  if(idFlag == "1" || idFlag == "selfDef"){ 
    if($("birthday").value){
      var birthdays = $("birthday").value;
      if(!isValidDateStr(birthdays)){
        alert("错误,生日时间格式不对，应形如 1999-01-02");
        //selectLast($("birthday"));
        return false;
      }
    }
  } 
  //if($("email").value){
  //  if(!emailReg.exec($("email").value)){
  //	  alert("请输入有效的E-mail地址！");
  //	  selectLast($("email"));
  //	  return false;
  //  }
  //}
  return true;
} 

function checkByname() {
  var bynameOld = document.getElementById("bynameOld").value;
  var byname = document.getElementById("byname").value;
  var divNode = document.getElementById("bynameSpan");
  if (!byname.trim()) {
    divNode.innerHTML = "";
    return;
  }
  var url = contextPath + "/yh/core/funcs/person/act/YHPersonAct/selectPersonName.act";
  if (seqId) {
    if (bynameOld != byname) {
      var rtJson = getJsonRs(url, "byname=" + byname);
      if (rtJson.rtState == "0") {
        if (rtJson.rtData[0].num == 1) {
          divNode.innerHTML = "<img src='<%=imgPath%>/dtree/error.gif' />该用户名或别名已存";
        } else if (rtJson.rtData[0].num == 0) {
          divNode.innerHTML = "<img src='<%=imgPath%>/dtree/correct.gif' />";
        }
        if ($("byname").value.trim() == $("userId").value.trim()){
          divNode.innerHTML = "<img src='<%=imgPath%>/dtree/error.gif' />该用户名和别名相同";
        }
      }
    }
  } else {
    var rtJson = getJsonRs(url, "byname=" + byname);
    if (rtJson.rtState == "0") {
      if (rtJson.rtData[0].num == 1) {
        divNode.innerHTML = "<img src='<%=imgPath%>/dtree/error.gif' />该用户名或别名已存在";
      } else if (rtJson.rtData[0].num == 0 && byname != "") {
        divNode.innerHTML = "<img src='<%=imgPath%>/dtree/correct.gif' />";
      }
      if ($("byname").value.trim() == $("userId").value.trim()){
        divNode.innerHTML = "<img src='<%=imgPath%>/dtree/error.gif' />该用户名和别名相同";
      }
    }
  }   
}

//验证用户别名是否通过
function byNameLogic(){
  var bynameCntrl = document.getElementById("byname");
  if (!bynameCntrl) {
    return true;
  }
  var byname = bynameCntrl.value;
  var divNode = document.getElementById("bynameSpan");
  if (!byname.trim()) {
    divNode.innerHTML = "";
    return true;
  }
  var url = contextPath + "/yh/core/funcs/person/act/YHPersonAct/selectPersonName.act";
  var rtJsons = getJsonRs(url, "byname=" + byname);
  if (rtJsons.rtState == "0") {
    if (rtJsons.rtData[0].num == 1) {
      divNode.innerHTML = "<img src='<%=imgPath%>/dtree/error.gif' />该用户名或别名已存在";
      alert("用户别名已存在,请重新填写");
      selectLast($("byname"));
      return false;
    }else if ($("byname").value.trim() == $("userId").value.trim()){
      alert("用户名和别名相同,请重新填写");
      selectLast($("byname"));
      return false;
    }else if (rtJsons.rtData[0].num == 0) {
      divNode.innerHTML = "<img src='<%=imgPath%>/dtree/correct.gif' />";
      return true;
    }else {
      divNode.innerHTML = "";
      return true;
    }
  }
  return true;
}

function commitPerson(){
    //parent.dialogArguments.location.reload();
  if (!checkBasicInfo()) {
    return;
  }
  if (seqId) {
    //if ($('separation').checked == true) {
    //  deptId = 0;
    //}
    var deptIdStr = $("deptId").value;
//    if($("birthday").value){
//      var birthday = $("birthday").value;
//      if(!isValidDateStr(birthday)){
//        alert("错误,生日时间格式不对，应形如 1999-01-02");
//        return false;
 //     }
//    }
   if ($("bynameOld") && $("byname")) {
      if($("bynameOld").value.trim() != $("byname").value.trim()){
        if (!byNameLogic()) {
          return;
        }  
      }
    }
    var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/updatePerson.act?seqId="+seqId+"&deptId="+deptId+"&deptIdStr="+deptIdStr+"&notLoginFlag="+notLoginFlag;
    var rtJson = getJsonRs(url, mergeQueryString($("userInfoForm")));
    if (rtJson.rtState == "0") {
      location = "<%=contextPath%>/core/funcs/person/role/usernew.jsp?deptId="+deptId;
      if(deptIdStr == "" && deptId == 0){
        parent.navigateFrame.location.reload();
      }
      if(deptId != deptIdStr){
        parent.navigateFrame.location.reload();
      }
    } else {
      alert(rtJson.rtMsrg); 
    }  
  } else {
    if (!byNameLogic()) {
      return;
    }  
    var userId = document.getElementById("userId").value;
    var url = contextPath + "/yh/core/funcs/person/act/YHPersonAct/selectPerson.act";
    var rtJson = getJsonRs(url, "userId=" + userId);
    if (rtJson.rtState == "0") {
      var divNode = document.getElementById("userIdSpan");
      if (rtJson.rtData[0].num >= 1) {
        alert("该用户名已存在,请重新填写！");
        selectLast($("userId"));
        //$("userId").select();
        return false;
      } else {
        var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/addPerson.act?deptIdLoca="+deptId;
        var rtJson = getJsonRs(url, mergeQueryString($("userInfoForm")));
        if (rtJson.rtState == "0") {
          location = "<%=contextPath%>/core/funcs/person/add.jsp?deptId="+deptId;
          dialogArguments.reloadFunc();
        } else {
          alert(rtJson.rtMsrg); 
        }
      }
    }
  }
}

//window.onunload = function() {
//  var parentWindow = window.dialogArguments;
//  parentWindow.refreshBoth();
//}



/**
 * 初始化绑定用户的信息
 */
function setBindInfo(){
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHBindUsersAct/bindInfo.act?userId=" + seqId;
  var json = getJsonRs(url);
  if (json.rtState == "0"){
    if (json.rtData != 0){
      $('bindUserDiv').setStyle({'display': 'none'});
      $('dispBindUserId').innerHTML = json.rtData.id;
      $('dispBindUserDesc').innerHTML = json.rtData.desc;
      $('dispBindUserDiv').setStyle({'display': 'block'});
    }
    else{
      $('dispBindUserDiv').setStyle({'display': 'none'});
      $('bindUserDiv').setStyle({'display': 'block'});
    }
  }
}


/**
 * 弹出绑定用户窗口函数
 */
function showBindWindow(){
  var url = contextPath + '/core/funcs/person/bindUsersWin.jsp';
  var paras = '?userId=' + seqId;
  window.showModalDialog(url + paras, window, "dialogWidth:300px;dialogHeight:400px");
}

/**
 * 弹出更换绑定用户窗口函数
 */
function showUpdateWindow(){
  var url = contextPath + '/core/funcs/person/bindUsersWin.jsp';
  var paras = '?userId=' + seqId;
  window.showModalDialog(url + paras, window, "dialogWidth:300px;dialogHeight:400px");
}

/**
 * 解除绑定函数
 */
function removeBind(){
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHBindUsersAct/removeBind.act?userId=" + seqId;
  var json = getJsonRs(url);
  
  if (json.rtState == "0"){
    
    $('dispBindUserDiv').setStyle({'display': 'none'});
    $('bindUserDiv').setStyle({'display': 'block'});
  }else{
    alert("解除失败!");
  }
}
</script>
</head>
<body onload="doInit();">
<%-- 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">
      <img src="<%=contextPath %>/core/styles/imgs/topplus.gif"></img><span class="big3"> 新建用户 （在职人员）</span>
    </td>
  </tr>
</table>
--%>
<form name="userInfoForm" id="userInfoForm" method="post">
<input type="hidden" value="0" name="openedAutoSelect" id="openedAutoSelect">
<input type="hidden" value="0" name="openedFlowDispatch" id="openedFlowDispatch">
<input type="hidden" value="0" name="openedWarnDispatch" id="openedWarnDispatch">
<input type="hidden" value="0" name="openedOtherDispatch" id="openedOtherDispatch">
<%
  if("".equals(seqId)) {
%>
    <span class="big1"><img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"></img>&nbsp;新建用户（<span id="userDept"></span>）</span>
<%
  }else {
%>
    <span class="big1"><img src="<%=contextPath %>/core/styles/imgs/edit.gif"></img>&nbsp;用户权限管理</span>
<%
  }
%> 
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.person.data.YHPerson"/>
  <input type="hidden" id="seqId" name="seqId" value="<%=seqId%>"/>
  <input type="hidden" id="userIdOld" name="userIdOld" value=""/>
  <input type="hidden" id="bynameOld" name="bynameOld" value=""/>
<%
  if(seqId.equals("")) {
%>
<span>
  &nbsp;<input type="button" value="新 建" class="BigButton" title="新建" onclick="commitPerson()">&nbsp;&nbsp;
  <input type="button" value="关 闭" class="BigButton" title="关闭窗口" onclick="window.close();">
</span>
<%
  }else {
%>
<span align="center">
  &nbsp;<input type="button" value="确认" class="BigButton" title="确认用户" onclick="commitPerson()">&nbsp;&nbsp;
  <input type="button" value="返回用户管理" class="BigButtonC" title="返回窗口" onclick="goBack()">
</span>
<%
  }
%>
<div id="contentDiv" style="position:absolute;left:0px;top:60px;">
</div>
</form>
</body>
</html>