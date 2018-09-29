<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/core/inc/header.jsp" %>

<html>

<head>
<title>新建引擎</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>

<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/flowhook/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/flowhook/js/HookLogic.js"></script>

<script type="text/javascript">

function remove_plugin()
{
   $("plugin").value="";    
}

function selPlugin(){
	  var url = contextPath + "/yh/core/funcs/workflow/act/YHPluginAct/getPluginListTwo.act" ;
	  loc_y=loc_x=200;
	  loc_x=document.body.scrollLeft+event.clientX-event.offsetX;
	  loc_y=document.body.scrollTop+event.clientY-event.offsetY;
	  LoadDialogWindow(url,self,loc_x, loc_y, 380, 350);
	}



function checkForm()
{
  if($("hmodule").value=="")
  {
     alert("模块不能为空！");
     $("hmodule").focus();
     return (false);
  }
  if($("hname").value=="")
  {
     alert("名称不能为空！");
     $("hname").focus();
     return (false);
  }
  if($("hdesc").value=="")
  {
     alert("描述不能为空！");
     $("hdesc").focus();
     return (false);
  }
  if($("plugin").value=="")
  {
     alert("插件不能为空！");
     return (false);
  }
  return true;
}

function doSubmit(){
  if(checkForm()){
    var hmodule= $("hmodule").value;
    var status="";
    var mapTypeStr = document.getElementsByName("status");
    for(var i = 0; i < mapTypeStr.length; i++){
      if(mapTypeStr[i].checked)
        status = mapTypeStr[i].value;
    } 
    var hname= $("hname").value;
    var hdesc= $("hdesc").value;
    var plugin= $("plugin").value;
    var param="hmodule="+hmodule+"&status="+status+"&hname="+hname+"&hdesc="+hdesc+"&plugin="+plugin;
    var url=contextPath+"/yh/core/funcs/workflow/act/YHFlowHookAct/addHookAct.act";
    var rtJson = getJsonRs(url,param);
     
    if (rtJson.rtState == "0") {  
        alert("添加成功！");
        var parentWindowObj = window.dialogArguments;
        parentWindowObj.location.href="index.jsp";
        window.close();

    } else {
      alert(rtJson.rtMsrg); 
    }

  }
}


</script>

</head>

<body>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
        <td class="Big"> <img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3">  新建引擎</span>&nbsp;
        </td>
    </tr>
</table>

<form action="" enctype="multipart/form-data" method="post" id="form1" name="form1" onSubmit="">
<table width="95%" class="TableBlock" align="center">
    <tr>
      <td class="TableContent">模块</td>
      <td class="TableData">
        <input type="text" name="hmodule" id="hmodule" class="BigInput">
      </td>
    </tr>   
    <tr>
      <td class="TableContent">状态</td>
      <td class="TableData">
         <input type="radio" name="status" id="status1" value="1">必选
         <input type="radio" name="status" id="status2" value="2">可选
         <input type="radio" name="status" id="status0" value="0" checked>停用
      </td>
    </tr>   
    <tr>
      <td class="TableContent">名称</td>
      <td class="TableData">
        <input type="text" name="hname" id="hname" class="BigInput">
      </td>
    </tr>
    <tr>
      <td class="TableContent">描述</td>
      <td class="TableData">
        <input type="text" name="hdesc" id="hdesc" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">插件程序名称：</td>
      <td class="TableData">
        <input onClick="javascript:selPlugin();" type="text" name="plugin" id="plugin" size="20" maxlength="100" class="SmallStatic" value="" readonly> <a href="#" onClick="javascript:selPlugin();">添加</a>
        <a href="javascript:;" onClick="javascript:remove_plugin()">清空</a>
        一般无需设置
        &nbsp;<a href="javascript:;" title="如没有软件开发商特殊定制开发的的插件程序，请勿填写。<br>插件程序为PHP文件，例如：my_process.java，放置于src/yh/plugins/workflow/system下。插件程序将在本步骤执行完毕后被自动调用执行。">说明</a>
      </td>
    </tr>
    <tr>
      <td class="TableData" colspan="2" align="center">
        <input type="button" value="确定" onclick="doSubmit();" class="BigButton" >
        <input type="button" onClick="window.close()" class="BigButton" value="关闭">
      </td>
    </tr>
    </table>
</form>


</body>
</html>