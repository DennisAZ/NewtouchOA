<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增双语标示</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function submitForm(){
  if(!$F('fileName')){
    alert('请选择EXCEL文件进行导入');
    $('fileName').select();
    return;
  }

  if(!(/\.(xls|xlsx)$/).exec($F('fileName').toLowerCase())){
    alert('格式有误,请选择xls/xlsx上传!');
    $('fileName').value = '';
    return;    
  }
  
  $('form1').submit();
}
</script>
</head>
<body>
<form method="post" name="form1" id="form1" action="<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHBilingualAct/batchAdd.act" enctype="multipart/form-data">
  <table style="width:100%;height:100%" class="TableList" align="center" >
    <tr>
      <td nowrap class="TableData" width=100> 类型：</td>
      <td nowrap class="TableData" width=220>
        <select name="type" id="type">
  <option value="0">职务职称</option>
  <option value="1">菜谱</option>
  <option value="2">标识标准</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=100> EXCEL文件：</td>
      <td nowrap class="TableData" width=220>
        <input class="BigInput" name="fileName" id="fileName" size="30" type="file"/>
      </td>
    </tr>
    <tr class="TableControl" align="center">
      <td colspan="2">
        <input type="button" class="BigButton" value="确认导入" onclick="submitForm()"/>
      </td>
    </tr>
  </table>
</form>
</body>
</html>