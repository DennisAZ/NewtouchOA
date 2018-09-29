<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
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
<title>数据导入</title>
<script type="text/javascript">
function doImp(){
  var fileName = $('sql_file').value;
  var index = fileName.lastIndexOf(".");
  var extName = "";
  if(index >= 0){
    extName = fileName.substring(index+1).toLowerCase().trim();
   }
  if(extName == "sql"){
    $('form1').action = contextPath + "/yh/core/funcs/mysqldb/act/YHMySqldbAct/importSql.act";
    $('form1').submit();
  }else{
    alert("请选择.sql脚本文件!");
    return;
  }
}
</script>
</head>

<body class="bodycolor" topmargin="5">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" align="absmiddle"><span class="big3"> 数据库脚本导入</span>（以下操作建议在软件技术支持人员指导下进行）<br>
    </td>
  </tr>
</table>
<br>
<br>

<div align="center" class="Big1">
<b>请指定用于数据库脚本导入的SQL脚本文件：</b>
<form method = "post" id = "form1" enctype = "multipart/form-data">
  <input type = "file" id = "sql_file" name = "sql_file" size = "50" class = "BigInput">
  <input type = "button" value = "开始导入" class = "BigButton" onclick = "doImp()">
</form>
</div>

</body>
</html>
