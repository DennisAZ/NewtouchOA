<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/core/inc/header.jsp" %>
<HEAD>
<TITLE>设置字段</TITLE>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<SCRIPT>
function set_font_family(family) {   
  document.form1.FONT_FAMILY.value = family;   
   $('font_family_link').innerHTML = "<span>"+(family=="" ? "字体" : family)+'</span>';    
}
function set_font_size(size, text) { 
  document.form1.FONT_SIZE.value = size;  
  $('font_size_link').innerHTML = "<span>"+text+'</span>';   
} 
function set_font_color(color) {   
  document.form1.FONT_COLOR.value = color;   
  $('font_color_link').style.color = color;  
}
function setOk() {
  var flag = parent.setField(
      $('ITEM_NAME').value,
      3,
      $('FONT_FAMILY').value,
      $('FONT_SIZE').value,
      $('FONT_COLOR').value,
      $('BORDER').value,
      $('HALIGN').value,
      $('VALIGN').value);
  if (!flag) {
    closeWinow();
  }
}
function closeWinow() {
  parent.closeWinow();
}
var menuDataFont = [{ name:'<div  style="padding-top:5px;margin-left:10px">宋体</div>',action:setFontAction,extData:'宋体'}
,{ name:'<div  style="font-family:黑体;padding-top:5px;margin-left:10px">黑体</div>',action:setFontAction,extData:'黑体'}
,{ name:'<div  style="font-family:楷体;padding-top:5px;margin-left:10px">楷体</div>',action:setFontAction,extData:'楷体'}
,{ name:'<div  style="font-family:隶书;padding-top:5px;margin-left:10px">隶书</div>',action:setFontAction,extData:'隶书'}
,{ name:'<div  style="font-family:幼圆;padding-top:5px;margin-left:10px">幼圆</div>',action:setFontAction,extData:'幼圆'}
,{ name:'<div  style="font-family:Arial;padding-top:5px;margin-left:10px">Arial</div>',action:setFontAction,extData:'Arial'}
,{ name:'<div  style="font-family:Fixedsys;padding-top:5px;margin-left:10px">Fixedsys</div>',action:setFontAction,extData:'Fixedsys'}
];
/**
 * 创建右建菜单
 * 
 */
function createFontMenu(event , obj){
  var divStyle = {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:obj , menuData:menuDataFont , attachCtrl:true},divStyle);
  menu.show(event);
}
function setFontAction(){
  var act = arguments[2];
  $('font_family_link').innerHTML = act;
  $('font_family_link').style.fontFamily = act;
  $('FONT_FAMILY').value = act;
}


/**
 * 大小
 * @param event
 * @return
 */
var menuDataSize = [
   { name:'<div  style="padding-top:5px;margin-left:10px">五号</div>',action:setSizeAction,extData:'10pt,五号'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">小四</div>',action:setSizeAction,extData:'12pt,小四'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">四号</div>',action:setSizeAction,extData:'14pt,四号'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">小三</div>',action:setSizeAction,extData:'15pt,小三'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">三号</div>',action:setSizeAction,extData:'16pt,三号'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">小二</div>',action:setSizeAction,extData:'18pt,小二'}
   ];
/**
 * 创建右建菜单
 * 
 */
function createSizeMenu(event , obj){
  var divStyle = {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:obj , menuData:menuDataSize , attachCtrl:true},divStyle);
  menu.show(event);
}
function setSizeAction(){
  var act = arguments[2].split(",");
  $('font_size_link').innerHTML = act[1];
  $('font_size_link').style.fontSize = act[0];
  $('FONT_SIZE').value = act[0];
}
/**
 * 颜色
 * @param event
 * @return
 */
var  menuDataColor = [{ name:LoadForeColorTable('set_font_color')}];


function set_font_color(color){
   $("FONT_COLOR").value = color;
   $('font_color_link').style.color = color;
}

function LoadForeColorTable(ClickFunc){
  var tColor = "";
  var tRowNum = 8;
  var tColorAry = new Array();
  tColorAry[0]="#000000";tColorAry[1]="#993300";tColorAry[2]="#333300";tColorAry[3]="#003300";
  tColorAry[4]="#003366";tColorAry[5]="#000080";tColorAry[6]="#333399";tColorAry[7]="#333333";

  tColorAry[8]="#800000";tColorAry[9]="#FF6600";tColorAry[10]="#808000";tColorAry[11]="#008000";
  tColorAry[12]="#008080";tColorAry[13]="#0000FF";tColorAry[14]="#666699";tColorAry[15]="#808080";

  tColorAry[16]="#FF0000";tColorAry[17]="#FF9900";tColorAry[18]="#99CC00";tColorAry[19]="#339966";
  tColorAry[20]="#33CCCC";tColorAry[21]="#3366FF";tColorAry[22]="#800080";tColorAry[23]="#999999";

  tColorAry[24]="#FF00FF";tColorAry[25]="#FFCC00";tColorAry[26]="#FFFF00";tColorAry[27]="#00FF00";
  tColorAry[28]="#00FFFF";tColorAry[29]="#00CCFF";tColorAry[30]="#993366";tColorAry[31]="#CCCCCC";

  tColorAry[32]="#FF99CC";tColorAry[33]="#FFCC99";tColorAry[34]="#FFFF99";tColorAry[35]="#CCFFCC";
  tColorAry[36]="#CCFFFF";tColorAry[37]="#99CCFF";tColorAry[38]="#CC99FF";tColorAry[39]="#FFFFFF";

  var tColorTableHTML = '<table cellpadding="0" cellspacing="0" class="ColorTable">';
  tColorTableHTML += '  <tr>';
  for (var ti = 0; ti < tColorAry.length; ti++){
        tColorTableHTML +='    <td onmouseover="this.className=\'Selected\';" onmouseout="this.className=\'\';" onclick="' + ClickFunc + '(\'' + tColorAry[ti] + '\');"';
        if(tColor.toUpperCase() == tColorAry[ti])
           tColorTableHTML +=' class="Selected"';
        tColorTableHTML +='><div style="width:11px;height:11px;background-color:' + tColorAry[ti] + ';"></div></td>';
        if ((ti+1) % tRowNum == 0 && ti+1 != tColorAry.length){
          tColorTableHTML += '  </tr>';
          tColorTableHTML += '  <tr>';
        };
  }; 
  tColorTableHTML += '  </tr>';
  tColorTableHTML += '</table>';
  
  return tColorTableHTML;
}


function showColor(event , obj){
  var divStyle = {border:'1px solid #69F',width:'145px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:obj , menuData:menuDataColor, attachCtrl:true }, divStyle);
  menu.show(event);
}
</SCRIPT>
</HEAD>
<BODY style="PADDING-BOTTOM: 0px; 
MARGIN: 0px; PADDING-LEFT: 0px; 
WIDTH: 100%; PADDING-RIGHT: 0px;
 HEIGHT: 100%; PADDING-TOP: 0px" 
 class=bodycolor  scroll=no><BR>
<FORM name=form1 align="center">
<TABLE style="MARGIN: 0px" class=TableList width=300 align=center>
<TBODY>
<TR>
<TD class=TableContent>字段说明</TD>
<TD class=TableData>
<input id=ITEM_NAME type="text" name=ITEM_NAME> 
<TR>
<TD class=TableContent>边框样式</TD>
<TD class=TableData><SELECT id=BORDER class=SmallSelect name=BORDER> <OPTION value=0>无边框</OPTION> <OPTION selected value=1>3D边框</OPTION> <OPTION value=2>实线边框</OPTION> <OPTION value=3>下滑下边框</OPTION></SELECT> </TD></TR>
<TR>
<TD class=TableContent>文字水平对齐方式</TD>
<TD class=TableData><SELECT id=HALIGN class=SmallSelect name=HALIGN> <OPTION selected value=0>左对齐</OPTION> <OPTION value=1>居中对齐</OPTION> <OPTION value=2>右对齐</OPTION></SELECT> </TD></TR>
<TR>
<TD class=TableContent>文字垂直对齐方式</TD>
<TD class=TableData><SELECT id=VALIGN class=SmallSelect name=VALIGN> <OPTION value=0>上对齐</OPTION> <OPTION selected value=1>纵居中</OPTION> <OPTION value=2>下对齐</OPTION></SELECT> </TD></TR>
<TR>
<TD class=TableContent>字体</TD>
<TD class=TableData>
<INPUT id=FONT_FAMILY value=宋体 type=hidden name=FONT_FAMILY> 
<A hideFocus style="FONT-FAMILY: 宋体" id=font_family_link class=dropdown href='javascript:' onmouseover='createFontMenu(event , this)'><SPAN>宋体</SPAN></A>&nbsp;&nbsp; </TD>
<TR>
<TD class=TableContent>字号</TD>
<TD class=TableData><INPUT id=FONT_SIZE value=12pt type=hidden name=FONT_SIZE> <A hideFocus id=font_size_link class=dropdown onmouseover="createSizeMenu(event , this)" href="javascript:;"><SPAN>小四</SPAN></A>&nbsp;&nbsp; </TD>
<TR style="display:none">
<TD class=TableContent style="display:none">字体颜色</TD>
<TD class=TableData><INPUT id=FONT_COLOR value=#000000 type=hidden name=FONT_COLOR> 
<A hideFocus style="COLOR: #000000" id=font_color_link class=dropdown onmouseover="showColor(event, this);" href="javascript:;"><SPAN>文字颜色</SPAN></A>&nbsp;&nbsp; 
</TD>
<TR class=TableControl>
<TD colSpan=2 align=middle>
<INPUT type=hidden name=FIELD_STR>
 <INPUT class=SmallButtonA onclick="setOk();" value=确定 type=button>
  <INPUT class=SmallButtonA onclick="closeWinow();" value=关闭 type=button>
   </TD>
   </TR>
   </TBODY></TABLE></FORM>
   </BODY>
