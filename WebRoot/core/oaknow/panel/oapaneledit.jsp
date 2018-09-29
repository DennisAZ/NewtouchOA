<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="yh.core.oaknow.data.*"%>
<%@ page  import="yh.core.oaknow.util.*"%>
<html>
<head>
<title>OA知道</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/wiki.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
function doInit(){//初始化下拉框
  var catelogies = ${toJson};
  var sel = document.getElementById("categorieid");
  var options = "";
  for(var i=0; i<catelogies.length; i++){
		sel.options.add(new Option(catelogies[i].name, catelogies[i].seqId));
		for(var j=0; j<catelogies[i].list.length; j++){
			sel.options.add(new Option("  "+catelogies[i].list[j].name, catelogies[i].list[j].seqId));
		}
  }
  $('categorieid').value = ${ask.typeId};
}

function splitStr(str){//正在把多个空格转换为一个空格
  var arr = trim(str);
  var reg = /\s{2,}/g; 
  var newStr = arr.replace(reg," "); 
  return newStr;
} 

function editAsk(){
  var str = $('tab').value;
  var ask = $("ask").value;
  if(ask == ""){
    alert("问题不能为空！");
    document.getElementById("ask").focus();
    return false;
  }
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
	<div class="gt">问题编辑</div>
	<div style="text-align: center;">		
	<form name="form1" id="editform" action='<%=contextPath%>/yh/core/oaknow/act/YHOAKnowPanelAct/editMyAsk.act' method='post'>
	  <table class="atb atb2" width="95%">
		  <tr align="left">
		    <td>问题：</td><td><input type="text" name="ask" id="ask" class="askincss2" value="${ask.ask}" /></td>
		  </tr>
		  <tr align="left">
	      <td>问题分类：</td>
	      <td>
	        <select name="categorieid" id="categorieid">             
          </select>         
        </td>

		  <tr align="left">
		    <td>问题描述：</td><td><textarea name="content" rows="16" cols="80">${ask.askComment}</textarea></td>
		  </tr>
		  <tr align="left">
		    <td>标签：</td>
		    <td>
		    	<input type="text" name="tab" id="tab" value="${ask.replyKeyWord}" style="width:200px;" class="askincss2" /> 
          &nbsp;&nbsp;(说明：用空格隔开多个标签，最多可填写 5 个，标签用于问题关联)		    	
		    </td>

		  </tr>
		</table>        
    <input type="hidden" name="typeId" id="typeId" value="">
    <input type="hidden" name="seqId" id="seqid" value="${ask.seqId}">
    <input type="hidden" name="commend" id="commend" value="${ask.commend}">
    <div class="opbar" style="width:95%">
    	<input type="button" value="提交" class="SmallButton" onclick="editAsk();"/> &nbsp;&nbsp;&nbsp;&nbsp;
    	<input type="button" value="返回" class="SmallButton" onclick="javascript:history.go(-1);" />
    </div>
  </form>
  </div>
</body>
</html>