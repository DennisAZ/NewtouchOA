<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>词语过滤查询</title>
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
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function commit(){
  var find = document.getElementById("find").value;
  var replacement = document.getElementById("replacement").value;
  if(document.getElementById("OPERATION1").checked == true){
    var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/getCensorWordsSearch.act?find="+find+"&replacement="+replacement;
    location = url;
  }else if(document.getElementById("OPERATION2").checked == true){
    var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/exportToTxt.act?find="+find+"&replacement="+replacement;
    location = url;
  }else if(document.getElementById("OPERATION3").checked == true){
    var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/deleteSearchWords.act?find="+find+"&replacement="+replacement;
    location = url;
  }
}
</script>
</head>
<body topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3">&nbsp;词语过滤查询</span>
    </td>
  </tr>
</table>
<form name="form1" id="form1">
  <table class="TableBlock" width="400" align="center">
    <tr>
      <td nowrap class="TableData">不良词语：</td>
      <td class="TableData">
        <input type="text" name="find" id="find" size="30" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">替换词语：</td>
      <td class="TableData">
        <input type="text" name="replacement" id="replacement" size="30" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">操作：</td>
      <td class="TableData">
        <input type="radio" name="OPERATION" id="OPERATION1" value="1" checked><label for="OPERATION1">查询</label>&nbsp;
        <input type="radio" name="OPERATION" id="OPERATION2" value="2"></input><label for="OPERATION2">导出</label>&nbsp;
        <input type="radio" name="OPERATION" id="OPERATION3" value="3"><label for="OPERATION3">删除</label>
      </td>
    </tr>
    <tr >
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="button" value="查询" class="BigButton" title="确定" name="button" onclick="commit()">
      </td>
    </tr>
  </table>
 </form>
</body>
</html>