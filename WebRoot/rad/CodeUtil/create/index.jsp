<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="/yh/core/js/datastructs.js"></script>
<script type="text/Javascript" src="/yh/core/js/sys.js" ></script>
<script type="text/Javascript" src="/yh/core/js/prototype.js" ></script>
<script type="text/Javascript" src="/yh/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="/yh/rad/codeSel/codeSel.js" ></script>
<script type="text/Javascript" src="/yh/rad/grid/grid.js" ></script>

<title>反向生成数据库表创建文件</title>
</head>
<body>
<script type="text/javascript">
function onSelectTable(){
  var curr = document.getElementById("pre4").value; 
	//selectCode ({sort:1,tableNo:"",codeField:"",nameField:"",filterField:"",filterValue:"",currValue:"",orderBy:""});
	selectCode ({sort:"2",tableNo:"99999",codeField:"代码",nameField:"tableNo",codeFieldNo:"编号",nameFieldNo:"tableName",filterField:"tableName",filterValue:"",currValue:curr,orderBy:""});
}
function onSelectPro(){
  var curr = document.getElementById("project").value;
  var arg = [window,"project"];
  //打开一个模式窗口
   window
        .showModalDialog(
            'folderBorwer.jsp',
            arg,
            'dialogWidth:430px;scroll:auto;dialogHeight:310px;help:no;directories:no;location:no;menubar:no;resizeable:no;status:no;toolbar:no;'); 
  //alert("select project");
}
function onSelectPac(){
  var arg = [window,"packageName"];
  window
  .showModalDialog(
      'folderBorwer.jsp',
      arg,
      'dialogWidth:430px;scroll:auto;dialogHeight:310px;help:no;directories:no;location:no;menubar:no;resizeable:no;status:no;toolbar:no;'); 
//alert("select project");
}
function onSubmit(){
  var url = "/yh/yh/rad/velocity/act/YHCodeUtilAct/create.act";
  var rtJson = getJsonRs(url, mergeQueryString($("f1")));
  alert(rtJson.rtMsrg);
}
</script>
<form id="f1">
 <font class="big3">反向生成数据库表创建文件</font>
 <br>
 <table id = "act" cellscpacing="1" cellpadding="3" width="450">
	   <tr class="TableLine1">
	    <td>选择所要创建的表编码：</td>
	    <td>
	        <input type = "text" id = "pre4" name = "tableNos" class="BigInput">
          <input type = "button" value = "选择..." onclick = "onSelectTable()">
	    </td>
	  </tr>
	  <tr class="TableLine2">
	    <td>选择模板文件：</td>
	    <td>
	      <input type = "text" id = "project" name="templateUrl" class="BigInput" value="D:\project\yh\templates\db">
        <!-- <input type = "button" value = "选择..." onclick = "onSelectPro()"><br>-->
	    </td>
	  </tr>
	  <tr class="TableLine1">
	    <td>生成文件输出目录 ：</td>
	    <td>
	      <input type = "text" id = "packageName" name = "outpath" class="BigInput" value="D:\">
        <!--<input type = "button" value = "选择..." onclick = "onSelectPac()">-->
	    </td>
	  </tr>
	  <tr class="TableLine2">
	    <td>选择要删除的sql格式：</td>
	    <td>
	        Orcale<input type="checkbox" name="dialect" value="Oracle" checked>
          MySql<input type="checkbox" name="dialect" value="MySql">
          MsSql<input type="checkbox" name="dialect" value="MsSql">
	    </td>
	  </tr>
  </table>
</form>
<input type="button" value="提交" onclick="onSubmit()" class="BigButton"></input>
</body>
</html>