<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.person.data.YHPerson" %>
<%@ include file="/core/inc/header.jsp" %>
<%
  String deptIdStr = request.getParameter("deptId");
  if (deptIdStr == null) {
    deptIdStr = "";
  }
  String userId = request.getParameter("userId") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("userId"));
  String userName = request.getParameter("userName") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("userName"));
  String byname = request.getParameter("byname") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("byname"));
  String sex = request.getParameter("sex");
  if (sex == null) {
    sex = "";
  }
  String deptId = request.getParameter("deptId");
  if (deptId == null) {
    deptId = "";
  }
  String userPriv = request.getParameter("userPriv");
  if (userPriv == null) {
    userPriv = "";
  }
  String postPriv = request.getParameter("postPriv");
  if (postPriv == null) {
    postPriv = "";
  }
  String notLogin = request.getParameter("notLogin");
  if (notLogin == null) {
    notLogin = "";
  }
  String notViewUser = request.getParameter("notViewUser");
  if (notViewUser == null) {
    notViewUser = "";
  }
  String notViewTable = request.getParameter("notViewTable");
  if (notViewTable == null) {
    notViewTable = "";
  }
  String dutyType = request.getParameter("dutyType");
  if (dutyType == null) {
    dutyType = "";
  }
  String lastVisitTime = request.getParameter("lastVisitTime");
  if (lastVisitTime == null) {
    lastVisitTime = "";
  }
  
  YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  String loginUserPriv = person.getUserPriv();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人员查询结果</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/grid.js"></script>
<script type="text/javascript">
var isAdmin = <%=person.isAdmin()%>;
var deptIdStr = "<%=deptIdStr%>";
var loginUserPriv = "<%=loginUserPriv%>";

function doInit() {
  var count = 0;
  var param = "userId=<%=userId%>&userName=<%=userName%>&byname=<%=byname%>&sex=<%=sex%>&deptId=<%=deptId%>&userPriv=<%=userPriv%>&postPriv=<%=postPriv%>&notLogin=<%=notLogin%>&notViewUser=<%=notViewUser%>&notViewTable=<%=notViewTable%>&dutyType=<%=dutyType%>&lastVisitTime=<%=lastVisitTime%>";
  param = encodeURI(param);
  var url =  contextPath + "/yh/core/funcs/person/act/YHPersonAct/getSearchPersonList.act?" + param;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var table=new Element('table',{ "width":"550","class":"TableList","align":"center"})
		.update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
				+"<td nowrap align='center' width='130'>部门</td>"				
				+"<td nowrap align='center' width='80'>用户名</td>"					
				+"<td nowrap align='center' width='80'>姓名</td>"	
				+"<td nowrap align='center' width='100'>角色</td>"	
				+"<td nowrap align='center' width='80'>管理</td>"	
				+"<td nowrap align='center' width='100'>操作</td></tr><tbody>");
		$('listDiv').appendChild(table);
  	for(var i = 0; i < rtJson.rtData.length; i++){
  	  count++;
  	  var seqId = rtJson.rtData[i].seqId;
  	  var userId = rtJson.rtData[i].userId;
  	  var deptId = rtJson.rtData[i].deptId;
  	  var userName = rtJson.rtData[i].userName;
  	  var userPriv = rtJson.rtData[i].userPriv;
  	  var postPriv = rtJson.rtData[i].postPriv;
  	  var notLogin = rtJson.rtData[i].notLogin;
  	  var password = rtJson.rtData[i].password;
  	  var isAdminStr = rtJson.rtData[i].isAdmin;
      var trColor = (count % 2 == 0) ? "TableLine1" : "TableLine2";
  	  var tr=new Element('tr',{'class':trColor});			
			table.firstChild.appendChild(tr);
			tr.update("<td align='center'>"					
			  + getDeptName(deptId, seqId, notLogin, password) + "</td><td align='center'>"	
				+ subjectRender(userId, seqId, notLogin, password) + "</td><td align='center'>"
				+ subjectUserName(userName, seqId, notLogin, password) + "</td><td align='center'>"
				+ getRoleName(userPriv, seqId, notLogin, password) + "</td><td align='center'>"
				+ getPostPriv(postPriv, seqId, notLogin, password) + "</td><td align='center'>"
				+ opts(seqId)+ "</td>"					
			);
  	}
  }else{
  	alert(rtJson.rtMsrg); 
  }
  if(count){
    showCntrl('listContainer');
    var mrs = " 共 "+ count + " 个符合条件且可管理的用户";
    WarningMsrg(mrs, 'msrg');
  }else{
    WarningMsrg('无符合条件且可管理的用户', 'msrg');
    $("listDiv").style.display = 'none';
  }
  
}  

function opts(seqId){
    return "<center><a href=\"javascript:queryResponsibleCustomers("+seqId+");\">查看负责客户</a></center>";
}

/**
 * 查询该人员所负责的客户
 */
function queryResponsibleCustomers(seqId){
  location.href = "<%=contextPath%>/customAction.do?op=toListCustomersByPerson&range=0&cusState=2&seqId="+seqId;
}

/**
 * 隐藏显示控件
 * 
 * @param cntrlId
 * @return
 */
function showCntrl(cntrlId) {
  if ($(cntrlId)) {
    if ($(cntrlId).style.display) {
      $(cntrlId).style.display = '';
    } else {
      $(cntrlId).style.display = 'none';
    }
  }
}
/**
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件


 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"380\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}

/**
 * @recordIndex         当前页面记录索引，从0开始计数

 */
function reader(recordIndex) {
  var diaId = this.getCellData(recordIndex,"seqId");
  showReader(diaId);
}
/**
 * @recordIndex         当前页面记录索引，从0开始计数

 */
function doEdit(perId) {
  location = "<%=contextPath%>/core/funcs/person/indutypersoninput.jsp?seqId="+ perId +"&deptIdStr="+deptIdStr;
}

function confirmDel(userId) {
  if(confirm("确认要删除用户"+userId+" 吗？")) {
    return true;
  }else {
    return false;
  }
}

function doDelete(perId,userId) {
  if(!confirmDel(userId)) {
   	return ;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/deleteUser.act";
  var rtJson = getJsonRs(url, "sumStrs=" + perId);
  if (rtJson.rtState == "0") {
    window.location.reload();
    window.parent.navigateFrame.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function confirmClear(userId) {
  if(confirm("确认要清空用户"+userId+"的密码吗？")) {
    return true;
  }else {
    return false;
  }
}

function doClear(perId,userId) {
  if(!confirmClear(userId)) {
    return ;
  }  
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/clesrUserPassword.act";
  var rtJson = getJsonRs(url, "seqIdStrs=" + perId);
  if (rtJson.rtState == "0") {
    window.location.reload();
    window.parent.navigateFrame.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}
/**
 * subject 描画
 */
function subjectRender(userId, seqId, notLogin, password) {
  if(notLogin == "1"){
    return "<center><div style=color:#C0C0C0>" + userId + "</div ></center>";
  }else if(password == ""){
    return "<center><font color=black>" + userId + "</font ></center>";
    }else{
    return "<center><font color=black>" + userId + "</font ></center>";
  }
  //}
}

function subjectUserName(userName, seqId, notLogin, password) {
  if(notLogin == "1"){
    return "<center><div style=color:#C0C0C0>" + userName + "</div ></center>";
  }else if(password == ""){
    return "<center><font color=black>" + userName + "</font ></center>";
    }else{
    return "<center><font color=black>" + userName + "</font ></center>";
  }
}


function getDeptName(deptId, seqId, notLogin, password){
  if(notLogin == "1"){
    var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getDeptName.act";
    var rtJsons = getJsonRs(urls , "deptId=" +  deptId);
    if(rtJsons.rtState == '0'){
      if(rtJsons.rtData == 0){
        rtJsons.rtData = "离职人员/外部人员"
      }
        return "<center><div style=color:#C0C0C0>" +rtJsons.rtData+ "</div ></center>";
    }
  }else if(password == ""){
    var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getDeptName.act";
    var rtJsons = getJsonRs(urls , "deptId=" +  deptId);
    if(rtJsons.rtState == '0'){
      if(rtJsons.rtData == 0){
        rtJsons.rtData = "离职人员/外部人员"
      }
      return "<center><font color=black>" +rtJsons.rtData+ "</font ></center>";
    }
  }else{
    var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getDeptName.act";
    var rtJsons = getJsonRs(urls , "deptId=" +  deptId);
    if(rtJsons.rtState == '0'){
      if(rtJsons.rtData == 0){
        rtJsons.rtData = "离职人员/外部人员"
      }
      return "<center><font color=black>" +rtJsons.rtData+ "</font ></center>";
    }
  }
}

function getRoleName(userPriv, seqId, notLogin, password){
  var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getRoleName.act";
  var rtJsons = getJsonRs(urls , "roleId=" +  userPriv);
  if(rtJsons.rtState == '0'){
    if(notLogin == "1"){
      return "<center><div style=color:#C0C0C0>" +rtJsons.rtData+ "</div ></center>";
    }else if(password == ""){
      return "<center><font color=black>" +rtJsons.rtData+ "</font ></center>";
    }else{
      return "<center><font color=black>" +rtJsons.rtData+ "</font ></center>";
    }
  }
}

function getPostPriv(postPriv, seqId, notLogin, password){
  if(notLogin == "1"){
    if(postPriv == "0"){
      return "<center><div style=color:#C0C0C0>本部门</div ></center>";
    }else if(postPriv == "1"){
      return "<center><div style=color:#C0C0C0>全体</div ></center>";
    }else if(postPriv == "2"){
      return "<center><div style=color:#C0C0C0>指定部门</div ></center>";
    }
  }else if(password == ""){
    if(postPriv == "0"){
      return "<center><font color=black>本部门</font ></center>";
    }else if(postPriv == "1"){
      return "<center><font color=black>全体</font ></center>";
    }else if(postPriv == "2"){
      return "<center><font color=black>指定部门</font ></center>";
    }
  }else{
    if(postPriv == "0"){
      return "<center><font color=black>本部门</font ></center>";
    }else if(postPriv == "1"){
      return "<center><font color=black>全体</font ></center>";
    }else if(postPriv == "2"){
      return "<center><font color=black>指定部门</font ></center>";
    }
  }
}

function support(groupNames, seqId){
  var URL = "/yh/core/funcs/person/supportIframe.jsp?deptId="+deptId;
  openDialog(URL,'650', '400');
}

function deleteAllUser() {
  if(!confirmDel()) {
   	return ;
  }  
  var deleteAllFlags = document.getElementsByName("deleteFlag");
  var idStrs = "";
  for(var i = 0; i < deleteAllFlags.length; i++) {
    if(deleteAllFlags[i].checked) {
      idStrs += deleteAllFlags[i].value + "," ;	
      flag = true;
 	}	  
  }
  var sumStrs = idStrs.substr(0,idStrs.length-1);
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/deleteUser.act";
  var rtJson = getJsonRs(url, "sumStrs=" + sumStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
    window.parent.navigateFrame.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 清空密码
 */
 
function clearPassword(){
  if(!confirmClear()) {
     return ;
  }  
  var deleteAllFlags = document.getElementsByName("deleteFlag");
  var idStrs = "";
  for(var i = 0; i < deleteAllFlags.length; i++) {
    if(deleteAllFlags[i].checked) {
      idStrs += deleteAllFlags[i].value + "," ; 
      flag = true;
    }   
  }
  var seqIdStrs = idStrs.substr(0,idStrs.length-1);
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/clesrUserPassword.act";
  var rtJson = getJsonRs(url, "seqIdStrs=" + seqIdStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
    window.parent.navigateFrame.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()" >
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle">
     <span class="big3"> 用户查询</span>
    </td>
  </tr>
</table>
<br/>

<div id="listDiv" align="center"></div>
<div id="listContainer" style="display:none">
</div>
<br>
<div id="msrg"></div>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="window.history.back();">
</div>
</body>
</html>