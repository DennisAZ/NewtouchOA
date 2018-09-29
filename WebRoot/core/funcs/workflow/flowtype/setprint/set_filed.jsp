
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/core/inc/header.jsp" %>
<%
String flowId=request.getParameter("flow_id");
%>

<html>
<head>
<title>设置编辑区</title>
<link  rel="stylesheet"  href  ="<%=contextPath%>/core/funcs/workflow/flowtype/setprint/js/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript"> 
jQuery.noConflict();
</script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/funcs/workflow/flowtype/setprint/js/print.js"  ></script>
<script>
  var flow_id=<%=flowId%>;
  var FONT_FAMILY = "宋体";
  var FONT_SIZE = "12pt";
  var FONT_COLOR = "#000000";
  var selectData;
	function doInit(){
	   $('font_color_link_menu').innerHTML=LoadForeColorTable('set_font_color');
	  addOption();
	  setItemName('1');
	}
	  function addOption(){
	        var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowReportAct/getListItemAct.act?fId="+flow_id;
	        var json = getJsonRs(url);
	        if(json.rtState == '0'){
	            selectData=json.rtData.data;
	        }
	    }

	
	function set_font_family(family)
	{
	   document.form1.FONT_FAMILY.value = family;
	   $('font_family_link').innerHTML = "<span>"+(family=="" ? "字体" : family)+"<img src='<%=imgPath %>/menu_arrow_down.gif' ></span>";
	   hideMenu();
	}
	function set_font_size(size, text)
	{
	   document.form1.FONT_SIZE.value = size;
	   $('font_size_link').innerHTML = "<span>"+text+"<img src='<%=imgPath %>/menu_arrow_down.gif' ></span>";
	   hideMenu();
	}
	function set_font_color(color)
	{
	   document.form1.FONT_COLOR.value = color;
	   $('font_color_link').style.color = color;
	   hideMenu();
	}

	function setField(){
		parent.setField($('ITEM_NAME').value,3,$('FONT_FAMILY').value,$('FONT_SIZE').value,$('FONT_COLOR').value,$('BORDER').value,$('HALIGN').value,$('VALIGN').value);
		}
  function setItemName(value) {
     if (value == "1") {
        $('itemNameTd').innerHTML = "<select name=\"ITEM_NAME\" id=\"ITEM_NAME\" class=\"SmallSelect\" style=\"width:150px;\"></select>";
        for(var i=0;i<selectData.length;i++){
          jQuery("#ITEM_NAME").append("<option value='"+selectData[i].text+"'>"+selectData[i].text+"</option>"); 
        }
       $('itemNameType').innerHTML = "表单字段";
     } else {
       $('itemNameTd').innerHTML ="<input type='text' id='ITEM_NAME' name='ITEM_NAME' value=''/>";
       $('itemNameType').innerHTML ="宏标记";
     }
  }
</script>
</head>

<body class="bodycolor" scroll=NO style="width:100%;height:100%;margin:0;padding:0;" onLoad="doInit();">
<br>
<form name="form1" align="center">
<table class="TableList" width="300px" align="center" style="margin:0;">
    <tr>
      <td nowrap class="TableHeader" colspan=2>
      <img src="<%=imgPath %>/green_arrow.gif" align="absMiddle" WIDTH="20" HEIGHT="18"> 请选择绑定字段：</img>
      </td>
    </tr>
     <tr>
      <td class="TableContent">字段类型</td>
      <td class="TableData">
        <select name="itemType" id="itemType" class="SmallSelect" onchange="setItemName(this.value)" style="width:150px;">
      <option value='1'>表单字段</option>
      <option value='2'>宏标记</option>
     </select>
      </td>
    </tr>
    <tr>
      <td class="TableContent" id="itemNameType">表单字段</td>
      <td class="TableData" id="itemNameTd">
      </td>
    </tr>
  
    <tr>
      <td class="TableContent">边框样式</td>
      <td class="TableData">
        <select name="BORDER" id="BORDER" class="SmallSelect">
         <option value="0">无边框</option>
         <option value="1" selected>3D边框</option>
         <option value="2">实线边框</option>
         <option value="3">下滑下边框</option>
        </select>
      </td>
    </tr>
    <tr>
      <td class="TableContent">文字水平对齐方式</td>
      <td class="TableData">
        <select name="HALIGN" id="HALIGN" class="SmallSelect">
         <option value="0" selected>左对齐</option>
         <option value="1">居中对齐</option>
         <option value="2">右对齐</option>
        </select>
      </td>
    </tr>
    <tr>
      <td class="TableContent">文字垂直对齐方式</td>
      <td class="TableData">
        <select name="VALIGN" id="VALIGN" class="SmallSelect">
         <option value="0">上对齐</option>
         <option value="1" selected>纵居中</option>
         <option value="2">下对齐</option>
        </select>
      </td>
    </tr>
    <tr>
      <td class="TableContent">字体</td>
      <td class="TableData">
        <input type="hidden" name="FONT_FAMILY" id="FONT_FAMILY" value="宋体">
        <a id="font_family_link" href="javascript:;" class="dropdown" onClick="showMenu(this.id, 1);" hidefocus="true" style="font-family:宋体;"><span>宋体<img src='<%=imgPath %>/menu_arrow_down.gif' ></span></a>&nbsp;&nbsp;
    </tr>
    <tr>
      <td class="TableContent">字号</td>
      <td class="TableData">
        <input type="hidden" name="FONT_SIZE" id="FONT_SIZE" value="12pt">
        <a id="font_size_link" href="javascript:;" class="dropdown" onClick="showMenu(this.id, 1);" hidefocus="true"><span>小四<img src='<%=imgPath %>/menu_arrow_down.gif' ></span></a>&nbsp;&nbsp;
    </tr>
    <tr>
      <td class="TableContent">字体颜色</td>
      <td class="TableData">
        <input type="hidden" name="FONT_COLOR" id="FONT_COLOR" value="#000000">
        <a id="font_color_link" href="javascript:;" class="dropdown" onClick="showMenu(this.id, 1);" hidefocus="true" style="color:#000000"><span>文字颜色<img src='<%=imgPath %>/menu_arrow_down.gif' ></span></a>&nbsp;&nbsp;
    </tr>
    <tr class="TableControl">
      <td align="center" colspan=2>
   
         <input type="button" class="SmallButton" onClick="setField();" value="确定">
         <input type="button" class="SmallButton" onClick="parent.HideDialog('setFrm')" value="关闭">
      </td>
    </tr>
</table>
</form>

<div id="font_family_link_menu" class="attach_div" style="font-size:14px;">
  <a href="javascript:set_font_family('宋体');" style="font-family:宋体;">宋体</a>
  <a href="javascript:set_font_family('黑体');" style="font-family:黑体;">黑体</a>
  <a href="javascript:set_font_family('楷体');" style="font-family:楷体;">楷体</a>
  <a href="javascript:set_font_family('隶书');" style="font-family:隶书;">隶书</a>
  <a href="javascript:set_font_family('幼圆');" style="font-family:幼圆;">幼圆</a>
  <a href="javascript:set_font_family('Arial');" style="font-family:Arial;">Arial</a>
  <a href="javascript:set_font_family('Fixedsys');" style="font-family:Fixedsys;">Fixedsys</a>
</div>
<div id="font_size_link_menu" class="attach_div">
  <a href="javascript:set_font_size('10pt', '五号');">五号</a>
  <a href="javascript:set_font_size('12pt', '小四');">小四</a>
  <a href="javascript:set_font_size('14pt', '四号');">四号</a>
  <a href="javascript:set_font_size('15pt', '小三');">小三</a>
  <a href="javascript:set_font_size('16pt', '三号');">三号</a>
  <a href="javascript:set_font_size('18pt', '小二');">小二</a>
  <a href="javascript:set_font_size('22pt', '二号');">二号</a>
  <a href="javascript:set_font_size('24pt', '小一');">小一</a>
  <a href="javascript:set_font_size('26pt', '一号');">一号</a>
</div>
<div id="font_color_link_menu" style="display:none;">
</div>
</body>
</html>