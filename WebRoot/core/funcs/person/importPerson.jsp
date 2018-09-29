<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
  String flag = request.getParameter("flag");
  if(flag == null) {
    flag = "";
  }
  String data = request.getParameter("data");
  if(data == null) {
    data = "";
  }
  String isCount = request.getParameter("isCount");
  if(isCount == null) {
    isCount = "";
  }
  String updateCount = request.getParameter("updateCount");
  if(updateCount == null) {
    updateCount = "";
  }
  Object dataObj = request.getAttribute("contentList");
  if(dataObj == null) {
    dataObj = "";
  }
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入用户</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/person/js/personUtil.js"></script>
<script Language="JavaScript">
var isCount = "<%=isCount%>";
var updateCount = "<%=updateCount%>";
var flag = "<%=flag%>";
var dataObj = '<%=dataObj%>';
function doInit(){
  if(flag != "1"){
    var count = 0;
    dataObj = dataObj.evalJSON();
    if(dataObj){
      var table=new Element('table',{ "width":"540","class":"TableList","align":"center"})
          .update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
           +"<td nowrap align='center' width='50'>部门</td>"    
           +"<td nowrap align='center' width='50'>用户名</td>"     
           +"<td nowrap align='center' width='40'>姓名</td>" 
           +"<td nowrap align='center' width='40'>角色</td>" 
           +"<td nowrap align='center' width='30'>排序号</td>" 
           +"<td nowrap align='center' width='40'>管理范围</td>" 
           +"<td nowrap align='center' width='100'>信息</td></tr><tbody>");
     $('listDiv').appendChild(table);
     for(var i = 0; i < dataObj.length; i++){
       count++;
       var deptName = dataObj[i].deptName;
       var userId = dataObj[i].userId;
       var userName = dataObj[i].userName;
       var role = dataObj[i].role;
       var userNo = dataObj[i].userNo;
       var postPriv = dataObj[i].postPriv;
       var info = dataObj[i].info;
        var color = dataObj[i].color;
        var trColor = (count % 2 == 0) ? "TableLine1" : "TableLine2";
       var tr=new Element('tr',{'class':trColor});   
   table.firstChild.appendChild(tr);
   tr.update("<td align='center'><font color="+color+">"     
       + deptName + "</font></td><td align='center'><font color="+color+">" 
    + userId + "</font></td><td align='center'><font color="+color+">"
    + userName + "</font></td><td align='center'><font color="+color+">"
    + role + "</font></td><td align='center'><font color="+color+">"
    + userNo + "</font></td><td align='center'><font color="+color+">"
    + postPriv + "</font></td><td align='left'><font color="+color+">"
    + info + "</font></td>"     
   );
     }
    }
  }
  if(count > 0){
    $("cionDiv").style.display = 'none';
  }else{
    $("turnDiv").style.display = 'none';
  }
  if(flag != "1"){
    if(isCount != "0"){
      parent.navigateFrame.location.reload();
    }
    showCntrl('listContainer');
    var mrs = "共导入 "+ isCount + " 条数据, 更新" + updateCount + "条数据!";
    WarningMsrgLong(mrs, 'msrg');
  }
  
}
</script>
</head>
<body topmargin="5" onload="doInit();">
<div id="cionDiv">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
      <td class="Big"><img src="<%=imgPath%>/sys_config.gif" align="absmiddle"><span class="big3">&nbsp;导入用户</span><br>
      </td>
    </tr>
  </table>
  <br>
  <br>
  <div align="center" class="Big1">
  <b>请指定用于导入的CSV文件：</b>
  <form id="form1" name="form1" method="post" action="<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/importPerson.act" enctype="multipart/form-data">
    <input type="file" name="csvFile" id="csvFile" class="BigInput" size="30">
    <input type="hidden" name="FILE_NAME">
    <input type="hidden" name="GROUP_ID" value="">
    <input type="button" value="导入" class="BigButton" onclick="importPerson();">
  </form>
  </div>

<table class="MessageBox" align="center" width="500">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">导入的用户ID和用户名均不能重复，如果ID为空则系统自动生成，用户名不能为空</div>
    </td>
  </tr>
</table>
</div>
  <div id="listDiv" align="center"></div>
  <br>
  <div id="listContainer" style="display:none">
</div>
<div id="msrg"></div>
  <br>
  <div align="center" id="turnDiv">
   <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/core/funcs/person/importPerson.jsp?flag=1';" title="返回">
</div>
</body>
</body>
</html>