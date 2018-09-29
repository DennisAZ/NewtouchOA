<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script src="/yh/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>计算控件</title>
<script type="text/javascript">
var dialog = window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument ;
var oActiveEl = dialog.Selection.GetSelectedElement();
window.onload = function(){
  oEditor.FCKLanguageManager.TranslatePage(document);
  if(oActiveEl && oActiveEl.tagName == 'INPUT' && (oActiveEl.getAttribute('class') == 'CALC'||oActiveEl.getAttribute('className') == 'CALC')){
    GetE('itemName').value = oActiveEl.title;
    GetE('itemValue').value = oActiveEl.value;
    var txtPrec = oActiveEl.getAttribute('prec');
    if(txtPrec)
      GetE('itemPrec').value = txtPrec;
      var txtFontSizeFull = oActiveEl.style.fontSize;
      GetE('itemSize').value = txtFontSizeFull.substr(0, txtFontSizeFull.length - 2);
      var itemWidthFull = oActiveEl.style.width;
      GetE('itemWidth').value = itemWidthFull.substr(0, itemWidthFull.length - 2);
      var itemHeightFull = oActiveEl.style.height;
      GetE('itemHeight').value = itemHeightFull.substr(0, itemHeightFull.length - 2);
	}else
      oActiveEl = null;
      dialog.SetOkButton( true );
	  dialog.SetAutoSize( true );
	  SelectField( 'itemName' );
}

function Ok(){
  var itemName = GetE('itemName');
  var itemValue = GetE('itemValue');
  if(itemName.value == ""){
     alert("控件名称不能为空");
     return;
  }
  else if(itemValue.value == ""){
     alert("计算公式不能为空");
     return;
  }
  if(oActiveEl == null && !checkUnique(GetE('itemName').value,oDOM)){
    alert("控件名称必须唯一");
    return;
  }
  if (itemValue.value.indexOf("^") != -1) {
     alert("计算公式不支持幂运算");
     return ;
  }
  var reg1 = /[^\d]/g;
  str = GetE('itemPrec').value;
  if (str && str.match(reg1)) {
    alert("计算结果精度只能为数字！");
    return ;
  }
  str = GetE('itemSize').value;
  if (str && str.match(reg1)) {
    alert("字体大小只能为数字！");
    return ;
  }
  str = GetE('itemWidth').value;
  if (str && str.match(reg1)) {
    alert("控件宽度只能为数字！");
    return ;
  }
  str = GetE('itemHeight').value;
  if (str && str.match(reg1)) {
    alert("控件高度只能为数字！");
    return ;
  }
  if(itemValue.value.indexOf("(") >= 0){
  	var num1 = itemValue.value.split("(").length - 1;
  	var num2 = itemValue.value.split(")").length - 1;  	
  	if(num1 != num2){
  		alert("公式书写错误,请检查括号匹配！");
  		return;
  	}
  }
  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("DATA",oDOM);
    idtr = 'DATA_' + sort;
  }else{ 
    idtr = oActiveEl.name;
  }
  oEditor.FCKUndo.SaveUndoStep();
  oActiveEl = CreateNamedElement(oEditor, oActiveEl, 'INPUT', {title: GetE('itemName').value,id:idtr,name:idtr ,src: contextPath + "/core/funcs/workflow/flowform/editor/plugins/NCalcu/calc.gif" });
  SetAttribute( oActiveEl, 'class', 'CALC');//for firefox
  SetAttribute( oActiveEl, 'className', 'CALC') ;//for IE
  SetAttribute( oActiveEl, 'value', GetE('itemValue').value);
  SetAttribute( oActiveEl, 'prec', GetE('itemPrec').value);
  if(GetE('itemSize').value!="")
    oActiveEl.style.fontSize=GetE('itemSize').value + 'px';//加上px可能在firefox也会正确显示 
  if(GetE('itemWidth').value!="")
	oActiveEl.style.width=GetE('itemWidth').value + 'px';
  if(GetE('itemHeight').value!="")
	oActiveEl.style.height=GetE('itemHeight').value + 'px';
  return true ;
}

function read_more(){
  if(desc_text.style.display == ''){
    desc_text.style.display = 'none';
    document.getElementById('read_more_link').innerHTML="查看计算公式填写说明";
  }else{
    desc_text.style.display = '';
    document.getElementById('read_more_link').innerHTML="隐藏计算公式填写说明";
  }
}
</script>
</head>
<body style="overflow-x: hidden ;font-size:12pt">
<table width="100%" style="height: 100%">
		<tr>
			<td align="center">
				<table cellspacing="3" cellpadding="0" border="0">
          <tr>
              <td nowrap>控件名称：
              </td>
              <td nowrap  align=left>
               <Input id="itemName" type="text" size="20">
              </td>
          </tr>
          <tr>
              <td nowrap>计算公式：
              </td>
              <td align=left>
               <textarea id="itemValue" cols="40" rows="3"></textarea><br>
               <a id="read_more_link" href="#" onclick="javascript:read_more()">查看计算公式填写说明</a><br>
              </td>
          </tr>
          <tr id="desc_text" style="display:none">
          	<td>&nbsp;</td>
          	<td>
               <div style="font-size: 10pt;font-family:宋体;color:blue;">
              计算公式支持+ - * /和英文括号以及特定计算函数，例如：(数值1+数值2)*数值3-ABS(数值4)<br>
              其中数值1、数值2等为表单控件名称。<br>
              <b>当前版本所支持的计算函数：</b><br>
              1、MAX(数值1,数值2,数值3...) 输出最大值,英文逗号分割;<br>
              2、MIN(数值1,数值2,数值3...) 输出最小值,英文逗号分割;<br>
              3、ABS(数值1) 输出绝对值;<br>
              4、AVG(数值1,数值2,数值3) 输出平均值;<br>
              5、RMB(数值1) 输出人民币大写形式，数值范围0～9999999999.99;<br>
              6、DAY(日期1-日期2) 输出时间差的整数天数;<br>
              7、HOUR(日期1-日期2) 输出时间差的小时数;<br>
              8、DATE(日期1-日期2) 输出时间差，形如：xx天xx小时xx分xx秒;<br>
              <b>参与日期计算的控件必须为日期类型或者日期+时间类型。</b><br>
              </div>
         	</td>
          </tr>
          <tr>
              <td nowrap>计算结果精度：
              </td>
              <td nowrap  align=left>
               <Input id="itemPrec" type="text" size="20" value=4 title="默认保留小数点后4位"> 默认保留小数点后4位              </td>
          </tr>
          <tr>
              <td nowrap>字体大小：
              </td>
              <td nowrap align=left>
               <Input id="itemSize" type="text" size="20" title="">
              </td>
          </tr>
          <tr>
              <td nowrap>控件宽度：
              </td>
              <td nowrap  align=left>
               <Input id="itemWidth" type="text" size="20" title="">
              </td>
          </tr>
          <tr>
              <td nowrap>控件高度：
              </td>
              <td nowrap  align=left>
               <Input id="itemHeight" type="text" size="20" title="">
              </td>
          </tr>
				</table>
			</td>
		</tr>
</table>
</body>
</html>
