<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/email.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
  function doInit(){
    var autoRunCfgList = getAutoRunCfgList();
    for(var i = 0; i < autoRunCfgList.length ; i ++ ){
      bindAutoRunCfg(autoRunCfgList[i]);
    }
    bindMainThreadStatus();
  }
  function getAutoRunCfgList(){
    var url = contextPath + "/yh/core/funcs/autorunmgr/act/YHAutoRunManagerAct/getAutoRunCfgs.act";
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      return rtJson.rtData;
    }
  }

  function bindAutoRunCfg(obj){
    if(obj.data.runTime){
      bindTimeTask(obj,"TimeTask");
    }else{
      bindSecondTask(obj,"secondTask");
    }
  }
  function bindSecondTask(obj,cntrlId){
    var id = obj.id;
    var data = obj.data; 
    var secondTime = data.intervalSecond/60;
    var html = "<tr class=\"TableData\"><td>&nbsp;&nbsp;&nbsp;&nbsp;" + data.name + "</td>"
      + "<td align=\"center\">" + secondTime + "</td>";
    if(data.isUsed == "1"){
      html += "<td align=\"center\" title=\"启用\"><img src=\"" + imgPath + "/correct.gif\"></td>"
      + "<td align=\"center\"><a href=\"edit.jsp?cfgname=" + id + "\"> 编辑</a>"
      + "<a href=\"javascript:execDelete('" + id + "');\"> 删除</a>"
      + "<a href=\"javascript:exec_task('" + id + "');\"> 立即执行</a></td></tr>";
    }else{
      html += "<td align=\"center\" title=\"停用\"><img src=\"" + imgPath + "/error.gif\"></td>"
      + "<td align=\"center\"><a href=\"edit.jsp?cfgname=" + id + "\"> 编辑</a>"
      + "<a href=\"javascript:execDelete('" + id + "');\"> 删除</a></td></tr>";
    }
    $(cntrlId).insert(html,"bottom");
  }
  function bindTimeTask(obj,cntrlId){
    var id = obj.id;
    var data = obj.data;
    var secondTime = data.intervalSecond/(60*60*24);
    var html = "<tr class=\"TableData\"><td>&nbsp;&nbsp;&nbsp;&nbsp;" + data.name + "</td>"
      + "<td align=\"center\">" + secondTime + "</td>"
      + "<td align=\"center\">" + data.runTime + "</td>";
    if(data.isUsed == "1"){
      html += "<td align=\"center\" title=\"启用\"><img src=\"" + imgPath + "/correct.gif\"></td>"
      + "<td align=\"center\"><a href=\"editbytime.jsp?cfgname=" + id + "\"> 编辑</a>"
      + "<a href=\"javascript:execDelete('" + id + "');\"> 删除</a>"
      + "<a href=\"javascript:exec_task('" + id + "');\"> 立即执行</a></td></tr>";
    }else{
      html += "<td align=\"center\" title=\"停用\"><img src=\"" + imgPath + "/error.gif\"></td>"
      + "<td align=\"center\"><a href=\"editbytime.jsp?cfgname=" + id + "\"> 编辑</a>"
      + "<a href=\"javascript:execDelete('" + id + "');\"> 删除</a></td></tr>";
    }
    $(cntrlId).insert(html,"bottom");
  }
  
  function execDelete(id){
    var msg = "确认要删除此服务吗?";
    if(!window.confirm(msg)){
      return;
    }
    var url = contextPath + "/yh/core/funcs/autorunmgr/act/YHAutoRunManagerAct/deleteAutoRunCfg.act";
    var rtJson = getJsonRs(url,"cfgname=" + id);
    if(rtJson.rtState == "0"){
      location.reload();
    }else{
      alert(rtJson.rtMsrg);
   }
  }
/**
 * 立即执行
 */
  function exec_task(id){
    var url = contextPath + "/yh/core/funcs/autorunmgr/act/YHAutoRunManagerAct/execSubThread.act?subid=" + id;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      alert(rtJson.rtMsrg);
    }else{
      alert("操作子服务失败:" + rtJson.rtMsrg);
    }
  }

  function execMainThread(type){
    var msg = "确认要停止主服务吗?";
    if(type == "1" && !window.confirm(msg)){
      return;
    }
    var url = contextPath + "/yh/core/funcs/autorunmgr/act/YHAutoRunManagerAct/execMainThread.act?type=" + type;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      alert(rtJson.rtMsrg);
      location.reload();
    }else{
      alert("操作主服务失败:" + rtJson.rtMsrg);
     }
  }

  function bindMainThreadStatus(){
    var url = contextPath + "/yh/core/funcs/autorunmgr/act/YHAutoRunManagerAct/getMainThreadStatus.act";
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      var html = "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;后台管理服务</td>";
      if(rtJson.rtData.isRunning == "1"){
        html += "<td align=\"center\"><font style=\"color:green\">已启动<font></td>"
          + "<td align=\"center\"><a href=\"javascript:execMainThread(1);\"> 停止</a></td></tr>";
       }else{
         html += "<td align=\"center\" ><font style=\"color:red\">已停止</font></td>"
           + "<td align=\"center\"><a href=\"javascript:execMainThread(0);\"> 启动</a></td></tr>";
       }
      $("mainThread").insert(html,"bottom");
    }
  }
</script>
<title>后台服务</title>
</head>
<body class="bodycolor" topmargin="10" onload="doInit()">
<br>
<table style="padding-left:15px" border="0" width="95%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/task.gif" align="absmiddle"><span class="big3"> <a name="bottom">后台管理服务控制</span>&nbsp;&nbsp;&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<table class="TableList" width="95%" align="center">
  <thead class="TableHeader" align="center">
    <td>任务名称</td>
    <td width="100">状态</td>
    <td width="150">操作</td>
  </thead>
  <tbody id="mainThread">
 </tbody>
</table>
<br>
<table style="padding-left:15px" border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/task.gif" align="absmiddle"><span class="big3"> <a name="bottom">间隔执行任务</span>&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button"  value="注册间隔服务" class="BigButtonC" onclick="location='add.jsp'">
    </td>
  </tr>
</table>
<br>
<table class="TableList" width="95%" align="center">
  <thead class="TableHeader" align="center">
    <td>任务名称</td>
    <td width="120">执行间隔(分钟)</td>
    <td width="100">服务启用状态</td>
    <td width="150">操作</td>
  </thead>
  <tbody id="secondTask">
  </tbody>
</table>
 
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>
 
 <br>
<table style="padding-left:15px" border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/task.gif" align="absmiddle"><span class="big3"> <a name="bottom">定点执行任务</span>&nbsp;&nbsp;&nbsp;&nbsp;
         <input type="button"  value="注册定点服务" class="BigButtonC" onclick="location='addbytime.jsp'">
    
    </td>
  </tr>
</table>
<br>
<table class="TableList" width="95%" align="center">
  <thead class="TableHeader" align="center">
    <td>任务名称</td>
    <td width="120">执行间隔(天)</td>
    <td width="100">执行时间</td>
    <td width="100">服务启用状态</td>
    <td width="150">操作</td>
  </thead>
  <tbody id="TimeTask">
 </tbody>
</table>
<br>

<div id="overlay"></div>
<div id="comment" class="ModalDialog" style="width:200px;">
  <div id="detail_body" class="body" style="text-align:center;">
     <img src="/images/loading.gif"><br><br>
     任务正在执行，请稍候...<br><br>
  </div>
</div>
</body>

</html>