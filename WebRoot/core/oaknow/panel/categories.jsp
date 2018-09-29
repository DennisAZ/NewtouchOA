<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<%@ include file="../tempheader.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="yh.core.oaknow.data.*"%>
<%@ page  import="yh.core.funcs.person.data.YHPerson"%>
<html>
<head>
<title>OA知道</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Expires" CONTENT="0">  
<meta http-equiv="Cache-Control" CONTENT="no-cache">  
<meta http-equiv="Pragma" CONTENT="no-cache">
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/wiki.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<%
	int flag = (Integer)request.getAttribute("flag");
  String jsn = (String)request.getAttribute("toJson");
  if(jsn == null){
    jsn = "";
  }
%>
<script type="text/javascript">
function parseJson(json){
  var catelogies = json;
  var temp = "";
  for(var i=0; i<catelogies.length; i++){
		temp += "<li><a href=\"<%=contextPath%>/yh/core/oaknow/act/YHCategoriesAct/goToCategoty.act?seqId="+ catelogies[i].seqId+"\">"+ catelogies[i].name +"</a></li>";
		for(var j=0; j<catelogies[i].list.length; j++){
		  temp += "<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"<%=contextPath%>/yh/core/oaknow/act/YHCategoriesAct/goToCategoty.act?seqId="+catelogies[i].list[j].seqId+"\">"+ catelogies[i].list[j].name +"</a></li>";
		}
  }
  $('tree').innerHTML = temp;
}

function fillSel(json, typeId){
  var flag = <%=flag%>
  var catelogies = json;
  var options = "";
  var sel = document.getElementById("pearentid");
  sel.options.add(new Option("", "0"));
  for(var i=0; i<catelogies.length; i++){
    sel.options.add(new Option(catelogies[i].name, catelogies[i].seqId));
  } 

  if(${type.pearentId} == '0' && flag !=0){
    $('pearentid').update("");
    sel.options.add(new Option("", "0"));
    $('pearentid').value="0";
  }else{
    $('pearentid').value = ${type.pearentId}; 
  }  
}
function doInit(){
  var json = <%=jsn%>; 
  parseJson(json);
  fillSel(json);
}

function ajax(){
  var queryParam = $("form1").serialize();
  var url = "<%=contextPath %>/yh/core/oaknow/act/YHCategoriesAct/findCategory.act";
  if(document.form1.oderId.value==""){
    alert("排序号不能为空！");
	  document.form1.oderId.focus();
	  return false;
  }
  if(document.form1.typeName.value==""){
	  alert("分类名称不能为空！");
	  document.form1.typeName.focus();
	  return false;
  }
  
  var rtJson = getJsonRs(url, queryParam);
  if (rtJson.rtState == "0") {
    //window.location.reload();
      window.location.href="<%=contextPath%>/core/oaknow/saveok.jsp";
    
  }else {
    alert("保存失败");
    return;
  }
}

function gotofirst(){
  window.location.href="<%=contextPath%>/yh/core/oaknow/act/YHCategoriesAct/goToCategoty.act";
}

function deleteType(id){
  var msgstr = "此操作将删除该分类及分类中的数据，是否继续？";
	if(confirm(msgstr))
	{	
	  var url ="<%=contextPath%>/yh/core/oaknow/act/YHCategoriesAct/deleteType.act?seqId=" + id;
	  var rtJson = getJsonRs(url);
	  if (rtJson.rtState == "0") {
	    window.location.href = "<%=contextPath%>/yh/core/oaknow/act/YHCategoriesAct/goToCategoty.act?seqId=";
	  }else {
	    alert("保存失败");
	    return;
	  }
	}
}
function clearValue(){
  $('manage').value = "";
  $('managernames').value = "";
}
</script>
</head>
<body onload="doInit()" class="mbodycolor" style="margin:0pt;padding:0pt">
<div class="classtree" style="width: 200px;">	
	<div style="text-align:center">
		<br /><input type="button" class="BigButton" onclick="gotofirst();" value="新建分类" />

	</div>
  <div class="tree_type">
  	<br />
    <ul id="tree">
    </ul>
  </div>
</div>
<div style="width: 65%;float:left;">
	<div id="new_categorie">
    <form name="form1" id="form1" >
		<div class="typetitle">新建分类</div><br />
	  <div style="width:100%;">

	  	<table class="tbl" width="100%">
	  		<tr>
	  			<td>排序号：</td>
	  			<td><input type="text" id="oderId" name="oderId" value="${type.orderId}"></td>
	  		</tr>
	  		<tr>
	  			<td>分类名称：</td>
	  			<td><input type="text" id="typeName" name="typeName" value="${quot}"></td>

	  		</tr>
	  		<tr>
	  			<td>上级分类：</td>
	  			<td>				
	  				<select id="pearentid" name="pearentid">
					  </select>
				  </td>

	  		</tr>	
	  		<tr>
	  			<td nowrap>管理人员：</td>
	  			<td>
           <input type="hidden" id="manage" name="manage" value="${type.managers}">
           <textarea style="width:200px;" name="managernames" id="managernames" rows="2" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly>${type.managerNames}</textarea>
           <a href="javascript:void(0);" class="orgAdd" onclick="selectUser(['manage', 'managernames'])" title="添加收件人">添加</a>
           <a href="javascript:void(0);" class="orgClear" onclick="javascript:clearValue();" title="清空收件人">清空</a><br>	  					  				
	  			</td>
	  		</tr>	  		  		
	  	</table>
	  	<input type="hidden" name="seqId" value="${type.seqId}">
	  	</div>	  
	  <div class="opbar"><input type="button" value="保存" class="SmallButton" onclick="ajax();"/>		 
	  <%
	  		if(flag == 1){
	  		%>
	  		<input type="button" value="删除" onclick="deleteType(${type.seqId});" class="SmallButton" />
	  		<%  
	  		}
	  %> 		         
	  </div>
	</form>
	</div>
</div>
</body>

</html>