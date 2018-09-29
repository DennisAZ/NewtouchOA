<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.core.oaknow.data.*"%>
<html>
<%
	String toJson = (String)request.getAttribute("toJson");
%>
<head>
<title>用户管理</title>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/wiki.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript">
function doInit(){//初始化下拉框
  var catelogies = <%=toJson%>;  
  var sel = document.getElementById("categorieid");
  var options = "";
  for(var i=0; i<catelogies.length; i++){
		sel.options.add(new Option(catelogies[i].name, catelogies[i].seqId));
		for(var j=0; j<catelogies[i].list.length; j++){
			sel.options.add(new Option("  "+catelogies[i].list[j].name, catelogies[i].list[j].seqId));
		}
  }
}

function splitStr(str){//正在把多个空格转换为一个空格
  var arr = trim(str);
  var reg = /\s{2,}/g; 
  var newStr = arr.replace(reg," "); 
  return newStr;
} 

function editAsk(){
  var ask = $('ask').value;
  if(ask==null || ask ==""){ alert("问题不能空");return;}
  var str = $('tab').value;
  var len = splitStr(str).split(" ").length;
  if(len > 5){
		alert("标签的个数不能超过5个"); return;
  }else{
		$('tab').value = splitStr(str);	
		$('typeId').value = $('categorieid').value;
		document.getElementById("editform").submit();	
  }
}
</script>
</head>
<body class="mbodycolor" topmargin="5" onload="doInit();">
<div class="gt">知道录入</div>
<div style="text-align: center;">

<form name="form1" id="editform" action='<%=contextPath%>/yh/core/oaknow/act/YHOAKnowInputAct/input.act' method='post'>
  <table class="atb atb2" width="94%">
	  <tr align="left">
	    <td>问题：</td><td><input type="text" name="ask" id="ask" class="askincss2" value="" /></td>
	  </tr>
	  <tr align="left">
      <td nowrap width="10%">问题分类：</td>
      <td><select name="categorieid" id="categorieid">
         </select>       
     </td>
	  <tr align="left">
	    <td>问题描述：</td><td><textarea name="content" rows="6" class="BigInput" style="width:60%"></textarea></td>
	  </tr>
	  <tr align="left">

	    <td>问题答案：</td><td><textarea name="answer" rows="10" style="width:60%" class="BigInput"></textarea></td>
	  </tr>
	  <tr align="left">
	    <td>标签：</td><td><input type="text" name="tab" id="tab" value="" style="width:200px;" class="askincss2" />&nbsp;&nbsp;<br />(说明：用空格隔开多个标签，最多可填写 5 个，标签用于问题关联)</td>
	  </tr>
	</table>
  <input type="hidden" name="action" value="userinput">
  <input type="hidden" name="typeId" id="typeId" value="">
  <div class="opbar" style="width:94%"><input type="button" onclick="editAsk();" value="提交" class="SmallButton" /></div>
</form>
</div>
<!--  
<div class="gt">知道导入</div>
<div style="text-align: center;">
  <form ENCTYPE="multipart/form-data" ACTION="inport.php" METHOD="POST" name="form2" onsubmit="return CheckForm2();">
    <div class="opbar" style="width:94%;">
  	   <input NAME="INFILE" TYPE="file" style="height:25px;width:350px;">
  	   <input type="hidden" name="FILE_NAME">
  	   <input type="submit" value="知道导入" class="subbcss" />&nbsp;&nbsp;&nbsp;&nbsp;
    </div>
  </form>
<br />
<br />
-->
</body>
</html>