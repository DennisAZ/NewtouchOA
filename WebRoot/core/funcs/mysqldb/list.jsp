<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.mysqldb.data.*"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  List<YHMySqlTabInfo> tabInfo = (List<YHMySqlTabInfo>)request.getAttribute("tabInfo");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<title>数据管理</title>
<script type="text/javascript" >
function getChecked(pre) {
  var className = "email_select";
  var ids= ""
  var checkArray = $$('input');
  for(var i = 0 ; i < checkArray.length ; i++){
    if(checkArray[i].name == className && checkArray[i].checked ){
      var obj = checkArray[i].value;
      var value = "";
      if(pre == 1){
        var values = obj.split(",");
        if(values.length > 1 && obj[1]){
          value = values[1];
        }else{
          value = values[0];
        }
      }else{
        value = obj;
      }
      
      if(!value){
        continue;
      }
      if(ids != ""){
        ids += ",";
      }
      ids += value;
    }
  }
   return ids;
}
function do_action(action){
  var delete_str = getChecked();
  var actionType = 1;
  if(delete_str == ""){
    alert("请至少选择其中一个数据表。");
    return;
  }
  if(action=="export"){
    if(!window.confirm("导出操作可能会进行较长时间，确定继续吗？")){
      return;
    }
    actionType = 1;
  } else {
     if(!window.confirm("操作前建议先备份数据库，确定继续吗？")){
        return;
     }
     actionType = 2;
  }
  var form1 = $('form1');
  form1.tables.value = delete_str;
  form1.actions.value = action;
  actionUrl(actionType,form1)
}

function actionUrl(type,form1){
  if(type == 1){
     url = contextPath + "/yh/core/funcs/mysqldb/act/YHMySqldbAct/export.act";
   }else{
     url = contextPath + "/core/funcs/mysqldb/manager.jsp";
   }
  form1.action = url;
  form1.submit();
}
function checkSearchAll(){
  var checkArray = $$('input');
  for(var i = 0 ; i < checkArray.length ; i++){
    if(checkArray[i].name == "email_select" ){
     checkArray[i].checked = $('allbox').checked ;
    }
  }
}
function checkSelf(){
   var allCheck = $('allbox');
   if(allCheck.checked){
     allCheck.checked = false;
   }
}
</script>
</head>
<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" align="absmiddle"><span class="big3" style="font-weight:bold"> 数据库管理</span>(以下操作建议在软件开发商指导下进行)</td>
  </tr>
</table>
<div class="small" style="margin-left:10px">
1、“数据库优化与修复”操作前，建议先进行“数据库热备份”。<br/>
<div style="height:4px"></div>
2、“数据库脚本导出”建议仅在特殊情况下使用，不建议作为日常备份方法，导出时可以选择数据表。<br/>
  <div style="height:4px"></div>
  
3、“数据库脚本导入”一般用于系统维护，建议在技术支持人员指导下操作。<br/>
  <div style="height:4px"></div>
  
4、“数据库热备份”是拷贝备份整个数据库目录(YH)，可作为日常数据备份方法。<br/>
  <div style="height:4px"></div>
  
5、详细的日常数据备份方法，请参考网站上的数据备份说明。<br/>
<br/>
<table class="TableList" align="center">
 <tr class="TableControl">
 <td colspan="5">
 	 <input type="checkbox" id="allbox" name="allbox" id="allbox_for"  onClick="checkSearchAll();"><label for="allbox_for">全选</label>
    <input type="button"  value="检查" class="SmallButton" onClick="do_action('check');" title="检查所选数据表"> &nbsp;&nbsp;
    <input type="button"  value="优化" class="SmallButton" onClick="do_action('optimize');" title="优化所选数据表"> &nbsp;&nbsp;
    <input type="button"  value="修复" class="SmallButton" onClick="do_action('repair');" title="修复所选数据表"> &nbsp;&nbsp;
    <input type="button"  value="数据库脚本导出" class="SmallButtonC" onClick="do_action('export');" title="导出所选数据表"> &nbsp;&nbsp;
 </td>
</tr>
 <tr class="TableHeader">
      <td nowrap align="center">选择</td>
      <td nowrap align="center">表名</td>
      <td nowrap align="center">记录数</td>
      <td nowrap align="center">大小</td>
      <td nowrap align="center">更新时间</td>
  </tr>
  <%for(int i = 0 ; i < tabInfo.size() ; i ++){ 
    YHMySqlTabInfo mytab = tabInfo.get(i);
  %>
    <tr class="TableData">
      <td nowrap align="center" width="40"><%if("myisam".equals(mytab.getEngine())){%>
    		<input type="checkbox" name="email_select" value="<%=mytab.getName() %>" onClick="checkSelf();"><% }%></td>
      <td nowrap width="150"><%=mytab.getName() %></td>
      <td nowrap align="right" width="100"><%=mytab.getRows() %></td>
      <td nowrap align="right" width="80"><%=mytab.getDataLength() %></td>
      <td nowrap align="center" width="150"><%=mytab.getUpdateTime() %></td>
    </tr>
    <% } %>
    <tr class="TableControl">
      <td nowrap align="center" width="40">总计</td>
      <td nowrap width="150"><%=tabInfo.size() %>个表</td>
      <td nowrap align="right" width="100"></td>
      <td nowrap align="right" width="80"></td>
      <td nowrap align="center" width="150"></td>
    </tr>
 <tr class="TableControl">
 <td colspan="5">
    <input type="button"  value="检查" class="SmallButton" onClick="do_action('check');" title="检查所选数据表"> &nbsp;&nbsp;
    <input type="button"  value="优化" class="SmallButton" onClick="do_action('optimize');" title="优化所选数据表"> &nbsp;&nbsp;
    <input type="button"  value="修复" class="SmallButton" onClick="do_action('repair');" title="修复所选数据表"> &nbsp;&nbsp;
    <input type="button"  value="数据库脚本导出" class="SmallButtonC" onClick="do_action('export');" title="导出所选数据表"> &nbsp;&nbsp;
 </td>
</tr>
</table>
<form id="form1" name="form1"  method="post">
   <input type="hidden" name="actions"  value="">
   <input type="hidden" name="tables" value="">
</form>
</div>
</body>
</html>