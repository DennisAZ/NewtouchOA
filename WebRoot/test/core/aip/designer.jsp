<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/core/inc/header.jsp" %>
<head>
<title>AIP Reader</title>
<style type="text/css">
.leftClass {
  float:left;
  width:300px;
  height:100%;
  border:1px solid #CCCCCC
}
#left-top {
  height:200px;
  border-bottom:1px solid #cccccc
}
#left-bottom {
  overflow:auto;
  height:400px;
  color:blue
}
#fieldDiv{
  width:250px;
  height: 150px;
  border: 1px solid #3063a8;
  margin-left: 0px;
  text-align: center;
  font-size: 12px;
  overflow:auto;
}
.item {
  height: 23px;
  font-size: 12px;
  color: #000000;
  background-color: #FFFFFF;
  border-bottom-width: 1px;
  border-bottom-style: solid;
  border-bottom-color: #669;
  cursor: pointer;
  padding-top: 3px;
}
.select {
  background-color: #CCDADF;
}
.hover {
  background-color: #E3EBEE;
}
.fieldName {
  float:left;
  width:100px;
  display:　block
}
.fieldDes {
  float:right;
  width:145px;
  display:　block
}
</style>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/index1.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/extResources/css/jq-yhtheme.css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.yh.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.window.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.tip.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.cardlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.borderlayout.js"></script>
<script type="text/javascript">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="designer.js"></script>
<script type="text/javascript">
var argObj = null;
var _userName = "sys_admin";
var notes = new Array();
</script>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyCtrlReady>
var content = loadAIP();
if (content) {
  // 控件"HWPostil1"的NotifyCtrlReady事件，一般在这个事件中完成初始化的动作
  var obj = $("HWPostil1");
  obj.ShowDefMenu = false; //隐藏菜单
  obj.ShowToolBar = false; //隐藏工具条
  obj.JSEnv = 1;
  obj.ShowScrollBarButton = 1;
  obj.LoadFileBase64(content);
}
</SCRIPT>
</head>
<body>
<div id="left" class="leftClass">
<div id="left-top">
<input type="button" class="SmallButtonC" value="添加映射区域" onclick="addField()" />
<input type="button" class="SmallButtonC" value="保存" onclick=""/>
<fieldset id="filedListField" style="display:none">
<LEGEND style="color:blue">填写单元列表</LEGEND>
<div id="fieldDiv">
</div>
<div style="float:left;width:35px;display:none">
<input value="上" type="button">
<input value="下" type="button">
</div>
</fieldset>

</div>
<div id="left-bottom">
<fieldset>
<LEGEND style="color:blue">单元信息</LEGEND>
<div id=descriptionDiv >
说明:<textarea rows="3" cols="20" id="description" name="description" onblur="setNoteDes(this)"></textarea>
</div>
<div>
<div style="display: block;width:135px" >
<input type="checkbox">
属于名单行的一部分
</div>
<div style="width:20px;display: block">
<input type="button" value="输入备注" class="SmallButtonC"/>
</div>
</div>
<div style="width:100%">
<fieldset>
<LEGEND style="color:blue">单元位置</LEGEND>
<div>X:<input value="" name="noteX" id="noteX" type="text" size="4"  onBlur="setNotePos()">
Y:<input value="" name="noteY" id="noteY"  type="text" size="4"  onBlur="setNotePos( )"></div><div>
W:<input value=""  name="noteW" id="noteW" type="text" size="4"  onBlur="setNotePos()">
H:<input value=""   name="noteH" id="noteH"   type="text" size="4" onBlur="setNotePos()"></div>
</fieldset>
</div>

<fieldset>
<LEGEND style="color:blue">单元字体</LEGEND>
<div>
字体：<span>宋体</span>字号：<span>9</span>对齐
</div>
</fieldset>

<fieldset>
<LEGEND style="color:blue">设置预填内容</LEGEND>
<div>
填写内容：<input type="radio" name="setContent" value="0"/>普通文本
<input type="radio" name="setContent" value="1"/>判断文本
</div>
<div>
填写方式：<select>
<option value="default">默认</option> 
</select>
文本有效长度：<select>
<option value="0">0</option> 
</select>
</div>
<div>
默认文本:<textarea rows="3" cols="15"></textarea>
</div>
<div>
填写内容：<textarea rows="3" cols="15"></textarea>
</div>
<div>
<select >
<option value="人员资料">人员资料</option>
</select>-<select><option value="出生地(英)">出生地(英)</option></select><input type="checkbox"/>领队
</div>
<div>
格式：<input type="text"/>
</div>
</fieldset>
<div><input type="checkbox"/>自动在每个字符之间插入空格<input type="button" value="刷新到列表"/></div>
</fieldset>
</div>
</div>
<div id="right"><OBJECT id=HWPostil1
 style="WIDTH:800px;HEIGHT:500px" 
 classid=clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567 
 codeBase='<%=contextPath %>/test/core/aip/HWPostil.cab#version=3,0,6,8' >
</OBJECT></div>
<input type="text" value="" name="FIELD_STR" id="FIELD_STR"/>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyLineAction(lPage,lStartPos,lEndPos)>
NotifyLineAction(lPage,lStartPos,lEndPos);
</SCRIPT>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=JSNotifyBeforeAction(lActionType,lType,strName,strValue)>
JSNotifyBeforeAction(lActionType,lType,strName,strValue);
</SCRIPT>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyPosChange(pcNoteName)>
setPosValue(pcNoteName , "Page1.");
</SCRIPT>
</body>
</html>