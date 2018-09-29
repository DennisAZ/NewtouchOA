<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>  
    <%
    String id = request.getParameter("deptId");
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门资源状况查询</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var deptId = "<%=id %>";
function doInit() {
  var url = contextPath + "/yh/core/funcs/system/resManage/act/YHResManageAct/getDeptRes.act";
  var json = getJsonRs(url , "deptId=" + deptId);
  if (json.rtState == "0") {
    var list = json.rtData;
    if (list.length <= 0) {
      $("noData").show();
      $("table").hide();
      return ;
    } 
   for (var i = 0 ;i < list.length ; i++ ) {
      var data = list[i];
      addTr(data);
    }
   $("table").show();
  }
}
function addTr(data) {
  var deptName = data.deptName;
  var userName = data.userName;
  var emailSize = data.emailSize;
  var fileSize = data.fileSize;
  var flag1 = data.flag1;
  var flag2 = data.flag2;
  
  var tr = new Element("tr" , {'class' : 'TableData'});
  $("dataList").appendChild(tr);
  
  td = new Element("td");
  td.align = 'center';
  td.update(deptName);
  tr.appendChild(td);

  td = new Element("td");
  td.align = 'center';
  td.update(userName);
  tr.appendChild(td);

  
  td = new Element("td");
  td.align = 'center';
  if (flag1 == '2') {
    emailSize = "<span style='color:red'>"+emailSize+"</span>";
  }
  td.update(emailSize);
  tr.appendChild(td);
  
  td = new Element("td");
  td.align = 'center';
  if (flag2 == '2') {
    fileSize = "<span style='color:red'>"+fileSize+"</span>";
  }
  td.update(fileSize);
  tr.appendChild(td);
}
</script>
</head>
<body onload="doInit()">

<table width="600" align="center">
  <tr>  
    <td align="right">
    </td>
  </tr>
  <tr>
    <td>
<table id="table" class="TableList" width="100%">
    <tr class="TableHeader">
      <td nowrap align="center">
       <b>部门</b>
      </td>
      <td nowrap align="center"><b>用户</b></td>
      <td nowrap align="center" ><b>内部邮件</b></td>
      <td nowrap align="center" ><b>个人文件柜</b></td>
    </tr>
    <tbody id="dataList"></tbody>
</table>
    </td>
  </tr>
</table>
<div id="noData" align=center style="display:none">
   <table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="msgInfo" class="msg info">此部门无用户
            </td>
        </tr>
    </tbody>
</table>
</div>
</body>
</html>