<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑词语</title>
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
var seqId = "<%=seqId%>";
function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/getCensorWordsId.act?seqId="+seqId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    bindJson2Cntrl(rtJson.rtData);
    for(var i = 0; i < rtJson.rtData.length; i++){
      document.getElementById("seqId").value = rtJson.rtData[i].seqId;
      document.getElementById("find").value = rtJson.rtData[i].find;
      if(rtJson.rtData[i].replacement == "null"){
        document.getElementById("replacement").value = "";
      }else{
        document.getElementById("replacement").value = rtJson.rtData[i].replacement;
      }
      findName = rtJson.rtData[i].find;
    }
  }else{
  	alert(rtJson.rtMsrg); 
   }
}

function check(){
  var find = document.getElementById("find");
  var replacement = document.getElementById("replacement");
  if(find.value == ""){ 
    alert("不良词语不能为空！");
    find.focus();
	return false;
  }
  if(find.value == replacement.value){ 
    alert("不良词语和替换的词语不能相同！");
    find.focus();
    return false;
  }
  return true;
}

function commit(){
  if(!check()){
    return;
  }
  var find = document.getElementById("find").value;
  var replacement = document.getElementById("replacement").value;
  var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/updateCensorWords.act?seqId="+seqId+"&find="+encodeURIComponent(find)+"&replacement="+encodeURIComponent(replacement);
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if (rtJson.rtState == "0") {
    location = "<%=contextPath %>/core/funcs/system/censorwords/manage/update.jsp?findName=" + encodeURIComponent(find);
  } else {
  alert(rtJson.rtMsrg);
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" WIDTH="16" HEIGHT="16" align="absmiddle"><span class="big3">&nbsp;编辑词语</span>
    </td>
  </tr>
</table>
<form method="post" name="form1" id="form1">
<input type="hidden" name="seqId" id="seqId" value="">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.censorwords.data.YHCensorWords.java"/>
<table class="TableBlock" width="90%" align="center">
    <tr class="TableHeader">
      <td colspan="2">单条添加</td>
    </tr>
    <tr>
      <td class="TableContent">不良词语：</td>
      <td class="TableData">
        <input type="text" class="BigInput" name="find" id="find" size="30" value="">
      </td>
    </tr>
    <tr>
      <td class="TableContent">替换为：</td>
      <td class="TableData">
        <input type="text" class="BigInput" name="replacement" id="replacement" size="30" value="">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" name="WORD_ID" value="<?=$WORD_ID?>">
        <input type="hidden" name="start" value="<?=$start?>">
        <input type="button" value="编辑" class="BigButton" onclick="commit()">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="window.history.back();">
      </td>
    </tr>
  </table>
</form>

<table class="small" cellpadding="3" cellspacing="1" width="90%" align="center">
    <tr class="TableHeader">
      <td>技巧提示</td>
    </tr>
    <tr class="TableData">
      <td>
        <ul>
          <li>如需<b>禁止</b>发布包含某个词语的文字，而不是替换过滤，请将其对应的替换内容设置为<b>{BANNED}</b>即可；</li>
          <li>如需当用户发布包含某个词语的文字时，标记为需要<b>人工审核</b>，而不直接显示，请将其对应的替换内容设置为<b>{MOD}</b>即可。</li>
        </ul>
      </td>
    </tr>
 </table>
</body>
</html>