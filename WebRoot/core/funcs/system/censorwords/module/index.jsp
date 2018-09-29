<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorModule" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.logic.YHCensorModuleLogic" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%
  ArrayList<YHCensorModule> moduleList = (ArrayList<YHCensorModule>)request.getAttribute("moduleList");
  int sumSize = moduleList.size();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>过滤模块设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/censorwords/js/censorWordsUtil.js"></script>
<script type="text/javascript">
var sumSize = "<%=sumSize%>";
function checkAll(field) {
  var deleteFlags = document.getElementsByName("deleteFlag");
  for(var i = 0; i < deleteFlags.length; i++) {
    deleteFlags[i].checked = field.checked;
  }
}

function deleteAllUser() {
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要删除模块，请至少选择其中一个。");
    return;
  }
  if(!confirmDel()) {
  	return ;
  }    
  var url = "<%=contextPath %>/yh/core/funcs/system/censorwords/act/YHCensorModuleAct/deleteModuleWords.act";
  var rtJson = getJsonRs(url, "idStrs=" + idStrs);
  if (rtJson.rtState == "0") {
      //location = "<%=contextPath %>/core/funcs/system/censorwords/manage/module.jsp?findName="+find;
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  } 
}
</script>
</head>
<body topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" WIDTH="16" HEIGHT="16" align="absmiddle"><span class="big3">&nbsp;添加过滤模块</span><br>
    </td>
  </tr>
</table>
<div align="center">
   <input type="button" class="BigButton" value="添加模块" onclick="location='<%=contextPath %>/core/funcs/system/censorwords/module/newmodule.jsp'">
</div>
<br>

<%
  if(moduleList.size()>0){ 
%>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" WIDTH="16" HEIGHT="16" align="absmiddle"><span class="big3">&nbsp;过滤模块管理</span>
    </td>
    <td align="center" class="small1">
    共<span class="big4">&nbsp;<%=sumSize%></span>&nbsp;个模块
    </td>
    </tr>
</table>
<%
  }else{
%>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="TableBlock">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/msg_back.gif" WIDTH="16" HEIGHT="16" align="absmiddle"><span class="big3">&nbsp; 过滤模块管理</span><br>
    </td>
  </tr>
</table>
<%} %>
<table class="TableBlock" width="100%">
<%
  String moduleName = "";
  String checkUser = "";
  String checkStr = "";
  for(int i = 0; i < moduleList.size(); i++) {
    YHCensorModule module = moduleList.get(i);
    YHCensorModuleLogic moduleLogic = new YHCensorModuleLogic();
    //System.out.println(module.getModuleCode()+"UTUGIUHIOJsd");
    if(module.getModuleCode().equals("0")){
      moduleName = "内部邮件";
    }else if(module.getModuleCode().equals("1")){
      moduleName = "内部短信";
    }else if(module.getModuleCode().equals("2")){
      moduleName = "手机短信";
    }
%>
    <tr class="<%= (i % 2 == 0) ? "TableLine1" : "TableLine2" %>">
      <td>&nbsp;<input type="checkbox" name="deleteFlag" id="deleteFlag" value="<%=module.getSeqId()%>"></td>
      <td><%=moduleName%></td>
      <td align="center"><%=module.getUseFlag().equals("1")?"<font color=green><b>√</b></font>":"<font color=red><b>×</b></font>"%></td>
      <td>
      <%   
        checkUser = moduleLogic.getUserId(((YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR)).getSysDbConn(), module.getCheckUser() );
        if(checkUser == null){
          checkStr = "&nbsp;";
        }else{
          if("".equals(checkUser)){
            checkStr = checkUser;
          }else{
            checkStr = checkUser.substring(0, checkUser.length() - 1);
          }
        }
      %>
       <%=checkStr%>
      </td>
      <td align="center"><%=module.getSmsRemind().equals("1")?"<font color=green><b>√</b></font>":"<font color=red><b>×</b></font>"%></td>
      <td align="center"><%=module.getSms2Remind().equals("1")?"<font color=green><b>√</b></font>":"<font color=red><b>×</b></font>"%></td>
      <td nowrap align="center"><a href="<%=contextPath%>/core/funcs/system/censorwords/module/newmodule.jsp?seqId=<%=module.getSeqId()%>">编辑</a></td>
    </tr>
<%
  } 
  if(moduleList.size()>0){ 
%>
<thead class="TableHeader">
      <td width="40">选</td>
      <td width="150">模块名称</td>
      <td width="80">启用过滤</td>
      <td>审核人员</td>
      <td width="100">内部短信提醒</td>
      <td width="100">手机短信提醒</td>
      <td width="60">操作</td>
    </thead>
<tr class="TableControl">
<td colspan="7">&nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);">
    <label for="checkAlls">全选</label> &nbsp;
    <img src="<%=imgPath%>/delete.gif" align="absMiddle"><a href="javascript:deleteAllUser();" title="删除所选模块">删除</a>&nbsp;
</td>
</tr>
</table>
<%
  }else{
%>
<table class="MessageBox" align="center" width="260">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">暂无添加模块</div>
    </td>
  </tr>
</table>
<% 
  }
%>
</body>
</html>