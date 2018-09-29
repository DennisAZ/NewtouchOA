<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>分组管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">

function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/system/address/act/YHAddressAct/getManageGroup.act";
  var rtJson = getJsonRs(url);
  var flag = 0;
  if(rtJson.rtState == "0"){
    var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"})
		.update("<tbody id='tbody'><tr class='TableHeader'>"
				+"<td nowrap width='120' align='center'>分组名称</td>"
				+"<td nowrap width='120' align='center'>开放部门</td>"				
				+"<td nowrap width='120' align='center'>开放角色</td>"				
				+"<td nowrap width='120' align='center'>开放人员</td>"				
				+"<td nowrap width='200' align='center'>操作</td></tr>"
				+"<tr class='TableData' align='center'>"
				+"<td nowrap align='center'>默认</td>"
				+"<td nowrap align='center'>全体部门</td>"
				+"<td nowrap align='center'>全体角色</td>"
				+"<td nowrap align='center'>全体人员</td>"
				+"<td nowrap align='left'>"
				+"<a href=\"javascript:clearGroup('0','默认');\">清空</a>&nbsp;&nbsp;"
				+"<a href="
				+ "javascript:importAddressPublic('0');"
                + ">导入</a>&nbsp;<a href="
                + "javascript:printFunc('0');"
                + ">打印</a></br>"
				+"<a href="
				+ "javascript:expFoxMail('0','默认');"
                + ">导出Foxmail格式</a>&nbsp;"
				+"<a href="
				+ "javascript:expOutLook('0','默认');"
                + ">导出OutLook格式</a>&nbsp;"
				+"</td>"
				+"</tr><tbody>");
		$('listDiv').appendChild(table);
  	for(var i = 0; i < rtJson.rtData.length; i++){
      flag++;
      var seqId = rtJson.rtData[i].seqId;
  	  var groupName = rtJson.rtData[i].groupName;
  	  var privDept = rtJson.rtData[i].privDept.trim();
  	  var privRole = rtJson.rtData[i].privRole.trim();
  	  var privUser = rtJson.rtData[i].privUser.trim();

  	  var deptName = rtJson.rtData[i].deptName;
  	  var roleName = rtJson.rtData[i].roleName;
  	  var userName = rtJson.rtData[i].userName;
  	  var tr=new Element('tr',{'class':'TableLine1'});			
			table.firstChild.appendChild(tr);
			tr.update("<td align='center'>"					
				+ groupName + "</td><td align='center' id='dept_"+flag+"Desc' name='dept_"+flag+"Desc'>"+deptName+"<input type='hidden' name='dept_"+flag+"' id='dept_"+flag+"' value='"+privDept+"'>"					
				+ "</td><td align='center' id='role_"+flag+"Desc' name='role_"+flag+"Desc'>"+roleName+"<input type='hidden' name='role_"+flag+"' id='role_"+flag+"' value='"+privRole+"'>"		
				+ "</td><td align='center' id='user_"+flag+"Desc' name='user_"+flag+"Desc'>"+userName+"<input type='hidden' name='user_"+flag+"' id='user_"+flag+"' value='"+privUser+"'>"	
				+ "</td><td align='left'>"		
				+ "<a href='edit.jsp?seqId=" + seqId + "&groupName=" + encodeURIComponent(groupName.escapeHTML().replace(/'/g,"\\\'").replace("\"","\\\"")) + "'>编辑</a>&nbsp;&nbsp;"
				+ "<a href="	
				+ "javascript:deleteGroup('" + seqId + "');"
				+ ">删除</a>&nbsp;&nbsp;"
				+ "<a href=\"javascript:void(0)\" onclick=\"clearGroup('" + seqId + "','" + groupName.escapeHTML().replace(/'/g,"\\\'").replace("\"","\\\"") + "')\">清空</a>&nbsp;&nbsp;"
				+ "<a href="
				+ "javascript:importAddressPublic('" + seqId + "');"
                + ">导入</a>&nbsp;<a href=\"javascript:void(0)\" onclick=\"support('" + groupName.escapeHTML().replace(/'/g,"\\\'").replace("\"","\\\"") + "','" + seqId + "')\">维护权限  </a></br>"
				+ "<a href="
                + "javascript:printFunc('" + seqId + "');"
                + "> 打印</a>&nbsp;"
				+ "<a  href=\"javascript:void(0)\" onclick=\"expFoxMail('" + seqId + "','" + groupName.escapeHTML().replace(/'/g,"\\\'").replace("\"","\\\"") + "')\">导出Foxmail格式</a>&nbsp;"
				+ "<a  href=\"javascript:void(0)\" onclick=\"expOutLook('"+seqId+"','" + groupName.escapeHTML().replace(/'/g,"\\\'").replace("\"","\\\"") + "')\">导出OutLook格式</a>&nbsp;"
				+ "</td>"					
			);
  	}
  }else{
  	alert(rtJson.rtMsrg); 
  }
}

function printFunc(groupId){
  var url = contextPath + "/core/funcs/system/address/manage/print.jsp?groupId="+groupId;
  window.open(url);
}

/**
 * 导入
 * 
 * @return
 */

function importAddressPublic(groupId){
  var flag = "1";
  location = contextPath + "/core/funcs/system/address/manage/importManage.jsp?flag="+flag+"&groupId="+groupId;
}

function importAddressPublic2(){
  if($("csvFile").value == ""){ 
    alert("请选择要导入的文件！");
    $("csvFile").focus();
    return false;
  }
  
  var csvFile = $("csvFile").value;
  var csvStr = csvFile.substr(csvFile.length - 3, csvFile.length);
  if(csvStr != "csv"){
    alert("错误,只能导入CSV文件!");
    $("csvFile").focus();
    $("csvFile").select();
    return false;
  }
  
  if($("csvFile").value != ""){
    $('form1').submit();
  }
}

/**
 * 导出FoxMail格式
 * 
 * @return
 */
function expFoxMail(seqId, groupName){
  location = contextPath + "/yh/core/funcs/address/act/YHAddressAct/exportCsvPublicAddress.act?seqId="+seqId+"&groupName="+encodeURIComponent(groupName);
}

/**
 * 导出OutLook格式
 * 
 * @return
 */
function expOutLook(seqId, groupName){
  location = contextPath + "/yh/core/funcs/address/act/YHAddressAct/exportCsvPublicAddress.act?seqId="+seqId+"&groupName="+encodeURIComponent(groupName);
}

function support(groupNames, seqId){
  var URL = "/yh/core/funcs/system/address/manage/supportIframe.jsp?seqId="+seqId+"&groupName="+encodeURIComponent(groupNames);
  openDialog(URL,'530', '330');
}

function confirmDelete() {
  if(confirm("确认要删除该分组吗？注意：该分组下的联系人将全部转入到默认分组中！")) {
    return true;
  }else{
    return false;
  }
}

function confirmClear(groupName) {
  if(confirm("确认要清空["+groupName+"]分组的联系人吗？")) {
    return true;
  }else{
    return false;
  }
}

//管理分组：删除
function deleteGroup(seqId){
  if(!confirmDelete()) {
  	return;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/deleteManageGroup.act";
  var rtJson = getJsonRs(url,"seqId="+seqId);
  if(rtJson.rtState == "0"){
    window.location.reload();
  }else{
	alert(rtJson.rtMsrg); 
  }
}

//管理分组：清空联系人
function clearGroup(seqId, groupName){
  if(!confirmClear(groupName)) {
  	return ;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/clearPublicManageGroup.act";
  var rtJson = getJsonRs(url,"seqId="+seqId);
  if(rtJson.rtState == "0"){
    window.location.reload();
  }else{
	alert(rtJson.rtMsrg); 
  }
}
//默认分组：清空联系人
function clearGroups(){
  window.location.reload();
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;新建分组</span><br>
    </td>
  </tr>
</table>

<div align="center">
<input type="button" value="新建分组" class="BigButton" onClick="location='<%=contextPath%>/core/funcs/system/address/manage/newgroup.jsp';" title="创建新的分组，录入相关信息">
</div>
<br>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3">&nbsp;管理分组</span>
    </td>
  </tr>
</table>
<br>
<div id="listDiv" align="center"></div>

</body>
</html>