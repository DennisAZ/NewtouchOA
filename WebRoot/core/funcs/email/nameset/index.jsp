<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>签名设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript">
var name = "";
function showOrHide(id){
  if($(id).style.display == 'none'){
    $(id).style.display = '';
  }else{
    $(id).style.display = 'none';
  }  
}
function doInit() {
  var url = contextPath + "/yh/core/funcs/email/act/YHEmailNameAct/getName.act";
  var json = getJsonRs(url);
  if (json.rtState == '0') {
    name = json.rtData.name;
    var isUse = json.rtData.isUse;
    var NAME_ID = json.rtData.nameId;
    if (isUse == '1') {
      $('IS_USE').checked = true;
    } else {
      $('IS_USE').checked = false;
    }
    $('NAME_ID').value = NAME_ID;
  }
}
function FCKeditor_OnComplete( editorInstance ) {
  editorInstance.SetData( name ) ;
}
function doSave() {
  var url = contextPath + "/yh/core/funcs/email/act/YHEmailNameAct/saveName.act";

  var name;
  if (isTouchDevice) {
    name = encodeURIComponent($("contentTextarea").value);
  } else {
	  var oEditor = FCKeditorAPI.GetInstance('NAME') ;
    name = encodeURIComponent(oEditor.GetXHTML());
  }
  var IS_USE = "0";
  if ($('IS_USE').checked) {
    IS_USE = "1";
  }
  
  var json = getJsonRs(url , "IS_USE="+ IS_USE +"&NAME_ID="+ $('NAME_ID').value +"&NAME=" + name);
  if (json.rtState == '0') {
    alert(json.rtMsrg);
  }
}
</script>
</head>

<body onload="doInit()">
<table class="TableTop" width="80%">
   <tr>
      <td class="left"></td>
      <td class="center subject">设置个性签名</td>
      <td class="right"></td>
   </tr>
</table>

<form action="submit.php"  method="post" id="form1" name="form1">
<table class="TableBlock" width="80%">
   <tr>
      <td class="TableData" colspan="2" id="contentTd">     
     <script language=JavaScript>
      if (isTouchDevice) {
        $("contentTd").insert("<textarea id=\"contentTextarea\" cols=\"40\" rows=\"10\"></textarea>");
      } else {
        var oFCKeditor = new FCKeditor('NAME');
				oFCKeditor.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/email/js/fckconfig.js"; 
				oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
				oFCKeditor.Height = "300px";
				oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/default/' ; 
				oFCKeditor.Config["PluginsPath"] = "yh/core/funcs/email/editor/plugins/";
				oFCKeditor.ToolbarSet="EmailBar";
				oFCKeditor.Create();  
      }
      </script>  
     </td>
   </tr>
    <tr>
      <td nowrap class="TableData" colspan="2">是否启用：<input type="checkbox" cols=40 name="IS_USE" id="IS_USE" checked value="1"><label for="IS_USE">启用个性签名</label>
      &nbsp;&nbsp;
      <a href="javascript:showOrHide('tip');">查看公式说明</a>
      </td>
   </tr>
<TR id="tip" style="display:none" class=TableLine2>
<TD noWrap  width="100px">表达式说明：</TD>
<TD>表达式中可以使用以下特殊标记：<BR/>
{Y}：表示年 <BR/>
{M}：表示月<BR/>
{D}：表示日 <BR/>
{H}：表示时 <BR/>
{I}：表示分<BR/>
{S}：表示秒 <BR/>
{U}：表示用户姓名<BR/>
{SD}：表示短部门 <BR/>
{LD}：表示长部门<BR/>
{R}：表示角色 <BR/>
例如，表达式为：邮件（{Y}年{M}月{D}日{H}:{I}）{U}<BR/>
自动生成文号如：邮件（2006年01月01日10:30）张三
</TD></TR>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="hidden" name="NAME_ID"   id="NAME_ID"  value="0">
        <input type="button" onclick="doSave()" value="确定" class="BigButton">&nbsp;&nbsp;
    </td>
   </tr>
</table>
</form>
</body>
</html>