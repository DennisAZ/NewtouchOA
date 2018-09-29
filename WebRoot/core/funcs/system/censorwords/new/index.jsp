<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>词语过滤管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">

function commit(){
  var reg = /^[0-9]*$/;
  //var ddd = @"^(\d)+[-]?(\d){6,12}$";
  var telphone = /^((\(\d{3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}$/;
  var find = document.getElementById("find");
  var replacement = document.getElementById("replacement");
  if(find.value == ""){ 
    alert("不良词语不能为空！");
    find.focus();
    //find.select();
	return false;
  }
  if(find.value == replacement.value){ 
    alert("不良词语和替换的词语不能相同！");
    find.focus();
    return false;
  }
  var find = document.getElementById("find").value;
  var replacement = document.getElementById("replacement").value;
  if(document.getElementById("overwrite").checked == true){
    var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/updateSingleWords.act?find="+encodeURIComponent(find)+"&replacement="+encodeURIComponent(replacement);
    var rtJson = getJsonRs(url, mergeQueryString($("form1")));
    if(rtJson.rtState == "0"){
      location = "<%=contextPath %>/core/funcs/system/censorwords/new/submit.jsp?find="+encodeURIComponent(find);
    }else{
  	  alert(rtJson.rtMsrg);
    }
  }else{
    var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/addSingleWords.act?find="+encodeURIComponent(find)+"&replacement="+encodeURIComponent(replacement);
    var rtJson = getJsonRs(url, mergeQueryString($("form1")));
    if(rtJson.rtState == "0"){
      location = "<%=contextPath %>/core/funcs/system/censorwords/new/submit.jsp?find="+encodeURIComponent(find);
    }else{
	  alert(rtJson.rtMsrg);
    }
  }
}

function commitMore(){
  //var censorArray = new Array();
  if($("censorWords").value == ""){ 
    alert("不良词语不能为空！");
    $("censorWords").focus();
	return false;
  }
  var valStr = document.getElementById("censorWords").value;
  var censorVal = valStr.split('\n');  
  if(document.getElementById("overwrite2").checked == true){
    var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/deleteMore2Words.act?find="+encodeURIComponent(find)+"&replacement="+encodeURIComponent(replacement);
    var rtJson = getJsonRs(url, mergeQueryString($("form2")));
  }
  var findSum = "";
  var okCount = 0;
  var errCount = 0;
  var replacementSum = "";
  var sum = "";
  for(var i = 0; i < censorVal.length; i++){
    //alert(censorVal[i]);
    okCount++;
    //errCount++;
    var find = censorVal[i].substr(0,censorVal[i].indexOf("="));
    var replacement = censorVal[i].substr(censorVal[i].indexOf("=")+1, censorVal[i].length-1);
    //var find1 += find + ",";
    sum += censorVal[i] + ",";
    findSum += find + ",";
    replacementSum += replacement + ",";
    if(censorVal[i].indexOf("=") == -1){
      find = censorVal[i];
      replacement = "";
    }
    if(trim(find) == "" || find == replacement){//没意义的公式跳过
      continue;
    }
    if(trim(replacement) == ""){//REPLACEMENT为空，替换成**
      replacement = "**";
    }
    if(document.getElementById("overwrite2").checked == true){
      //alert("2");
      var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/updateMore2Words.act?find="+encodeURIComponent(find)+"&replacement="+encodeURIComponent(replacement);
      var rtJson = getJsonRs(url, mergeQueryString($("form2")));
      if(rtJson.rtState == "0"){
        location = "<%=contextPath %>/core/funcs/system/censorwords/new/import2.jsp?okCount="+okCount+"&errCount="+errCount;
      }else{
    	alert(rtJson.rtMsrg);
      }
    }else if(document.getElementById("overwrite1").checked == true){
      //alert("1");
      var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/updateMore1Words.act?find="+encodeURIComponent(find)+"&replacement="+encodeURIComponent(replacement);
      var rtJson = getJsonRs(url, mergeQueryString($("form2")));
      if(rtJson.rtState == "0"){
        location = "<%=contextPath %>/core/funcs/system/censorwords/new/import2.jsp?okCount="+okCount+"&errCount="+errCount;
      }else{
    	alert(rtJson.rtMsrg);
      }
    }else if(document.getElementById("overwrite0").checked == true){
      var url = "<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/updateMore0Words.act?find="+encodeURIComponent(find)+"&replacement="+encodeURIComponent(replacement);
      //location = url;
      location = "<%=contextPath %>/core/funcs/system/censorwords/new/import.jsp?sum="+sum+"&findSum="+findSum;
      //var rtJson = getJsonRs(url, mergeQueryString($("form2")));
    }
  }
}

</script>
</head>
<body class="" topmargin="5">
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" WIDTH="16" HEIGHT="16" align="absmiddle"><span class="big3">&nbsp;添加词语</span>
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
        <input type="text" class="BigInput" name="find" id="find" size="30">
      </td>
    </tr>
    <tr>
      <td class="TableContent">替换为：</td>
      <td class="TableData">
        <input type="text" class="BigInput" name="replacement" id="replacement" size="30">
      </td>
    </tr>
    <tr>
      <td class="TableContent">操作：</td>
      <td class="TableData">
        <input type="checkbox" name="overwrite" id="overwrite"><label for="OVERWRITE">覆盖已经存在的词语</label>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="添加词语" class="BigButton" onclick="commit()">&nbsp;&nbsp;
      </td>
    </tr>
</form>
</table>
 <table class="TableBlock" width="90%" align="center">
    <tr class="TableHeader">
      <td>技巧提示</td>
    </tr>
    <tr class="TableData">
      <td>
        <ul>
          <li>如需<b>禁止</b>发布包含某个词语的文字，而不是替换过滤，请将其对应的替换内容设置为<b>{BANNED}</b>即可；</li>
          <li>如需当用户发布包含某个词语的文字时，标记为需要<b>人工审核</b>，而不直接显示，请将其对应的替换内容设置为<b>{MOD}</b>即可。</li>
          <li>为不影响程序效率，请不要设置过多不需要的过滤内容。</li>
        </ul>
      </td>
    </tr>
 </table>
 <br />
 <form method="post" name="form2" id="form2">
 <table class="TableBlock" width="90%" align="center">
    <tr class="TableHeader">
      <td colspan="2">批量添加</td>
    </tr>
    <tr>
      <td class="TableContent" width="240">
         <b>添加格式：</b>
                        <br/>每行一组过滤词语。
         <br/>不良词语和替换词语之间使用“=”分割。
         <br/>如果只是想将某个词语直接替换成 **，<br />则只输入词语即可。
         <br />
         <b>例如：</b><br />
         词语1<br />
         词语2=替换词<br />
         词语3={BANNED}<br />
         词语4={MOD}<br />
         <b>上例的执行结果：</b><br />
         “词语1”被替换为“**”<br />
         “词语2”被替换为“替换词”<br />
         “词语3”被禁止发表<br />
         “词语4”被标记为需要人工审核
      </td>
      <td class="TableData">
        <textarea name="censorWords" id="censorWords" name="censorWords" class="BigInput" style="overflow: auto;" rows="15" cols="72"></textarea><br />
        <input type="radio" name="OVERWRITE" id="overwrite2" value="2"> <label for="overwrite2">清空当前词表后导入新词语，此操作不可恢复，建议首先 <b><u>导出词表</u></b> , 做好备份。<br /></label>
        <input type="radio" name="OVERWRITE" id="overwrite1" value="1" > <label for="overwrite1">使用新的设置覆盖已经存在的词语<br /></label>
        <input type="radio" name="OVERWRITE" id="overwrite0" value="0" checked> <label for="overwrite0"><b>不导入已经存在的词语</b><br /></label>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="批量添加" class="BigButton" onclick="commitMore()">&nbsp;&nbsp;
      </td>
    </tr>
</form>
</table>
</body>
</html>