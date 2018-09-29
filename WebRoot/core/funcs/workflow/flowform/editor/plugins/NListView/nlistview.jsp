<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<title>列表控件 </title>
<script type="text/javascript">
var dialog = window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement();
window.onload = function(){   
  oEditor.FCKLanguageManager.TranslatePage(document);
  for (var i = 1 ;i <= 50 ;i++ ) {
    addRow(i);
  }
  if(oActiveEl && oActiveEl.tagName == 'IMG'){
    GetE('itemName').value	= oActiveEl.title;
    var item_id = oActiveEl.name.substr(5);
    var LV_TITLE = oActiveEl.getAttribute("lv_title");
    var LV_SIZE = oActiveEl.getAttribute("lv_size");
    var LV_CAL =oActiveEl.getAttribute("lv_cal");
    var LV_ALIGN = oActiveEl.getAttribute("lv_align");
    if (!LV_ALIGN) {
      LV_ALIGN = "";
    }
    var LV_TITLE_ARRAY = LV_TITLE.split("`");
    var LV_SIZE_ARRAY = LV_SIZE.split("`");
    var LV_ALIGN_ARRAY = LV_ALIGN.split("`");
    if(oActiveEl.LV_SUM){
      var LV_SUM = oActiveEl.getAttribute("lv_sum");
      var LV_SUM_ARRAY = LV_SUM.split("`");
  	}
  	if(LV_CAL){
      var LV_CAL_ARRAY=LV_CAL.split("`");
    }
  	for(var i = 0; i < LV_TITLE_ARRAY.length - 1; i++){
      var item_str = "item_" + (i + 1);
      var size_str = "size_" + (i + 1);
      var sum_str = "sum_" + (i + 1);
      var cal_str = "cal_" + (i + 1);
      var align_str = "align_" + (i + 1);
      GetE(item_str).value = LV_TITLE_ARRAY[i];
      GetE(size_str).value = LV_SIZE_ARRAY[i];
      if(LV_SUM) GetE(sum_str).checked = LV_SUM_ARRAY[i] == 1?true:false;
      if(LV_CAL) GetE(cal_str).value = LV_CAL_ARRAY[i];
      var alignValue = "left";
      if (LV_ALIGN_ARRAY.length > i) {
        alignValue = LV_ALIGN_ARRAY[i];
      }
      if (!alignValue) {
        alignValue = "left";
      }
      $(align_str).value = alignValue;
  	}
  }else//在编辑区域内新建一个控件时，走这个分支 by dq 090520
    oActiveEl = null;
	dialog.SetOkButton( true );//该函数在fckdialog.html 683行定义 by dq 090520
    //dialog.SetAutoSize( true ) ;//该函数在fckdialog.html 213行定义 by dq 090520
	//SelectField(oActiveEl);//在编辑区域设置刚创建的控件为选中状态，该函数在fck_dialog_common.js 第100行定义 by dq 090520
}
function Ok(){
  var itemName = GetE('itemName');
  if(itemName.value == ""){
    alert("控件名称不能为空");
    return;
  }
  if(oActiveEl == null && !checkUnique(GetE('itemName').value,oDOM)){
    alert("控件名称必须唯一");
    return;
  }
  var LV_TITLE = "";
  var LV_SIZE = "";
  var LV_SUM = "";
  var LV_CAL = "";
  var LV_ALIGN = "";
  var count = 0;
  var reg1 = /[^\d]/g;
  
  for(var i = 1; i <= 50; i++){
    var item_str = "item_" + i;
    var size_str = "size_" + i;
    var sum_str = "sum_" + i;
    var cal_str = "cal_" + i;
    var align_str = "align_" + i;
    if(document.getElementById("item_"+i).value != ""){
      LV_TITLE += document.getElementById("item_"+i).value+"`";
      count++;
      LV_ALIGN += document.getElementById("align_"+i).value + "`";
      LV_CAL += document.getElementById("cal_"+i).value + "`";
    if(document.getElementById("size_"+i).value != "") {
      str = document.getElementById("size_"+i).value;
      if (str && str.match(reg1)) {
        alert("列宽度只能为数字！");
        return ;
      }
      LV_SIZE += document.getElementById("size_"+i).value + "`";
    }
    else
      LV_SIZE += "10`";
    if(document.getElementById("sum_"+i).checked == true)
      LV_SUM += "1`";
    else
     LV_SUM += "0`";
    }
  }
  if(count == 0){
    alert("表头项目不能为空");
    return;
  }
  //alert(sort);
  oEditor.FCKUndo.SaveUndoStep();
  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("DATA",oDOM);
    idtr = 'DATA_' + sort;
  }else{ 
    idtr = oActiveEl.name;
  }
  oActiveEl = CreateNamedElement( oEditor, oActiveEl, 'IMG', {title: GetE('itemName').value,id:idtr,name:idtr,src:"<%=contextPath%>/core/funcs/workflow/flowform/editor/plugins/NListView/listview.gif"});
  SetAttribute(oActiveEl, 'LV_TITLE', LV_TITLE);
  SetAttribute(oActiveEl, 'LV_SIZE', LV_SIZE);
  SetAttribute(oActiveEl, 'LV_SUM', LV_SUM);
  SetAttribute(oActiveEl, 'LV_CAL', LV_CAL);
  SetAttribute(oActiveEl, 'LV_ALIGN', LV_ALIGN);
  SetAttribute(oActiveEl, 'border', 0);
  SetAttribute(oActiveEl, 'align', 'absMiddle');
  SetAttribute(oActiveEl, 'class', 'LIST_VIEW');
  SetAttribute(oActiveEl, 'className', 'LIST_VIEW');
  //SetAttribute( oActiveEl, 'src', '/module/html_editor/editor/images/listview.gif' ) ;
  oActiveEl.style.cursor = 'hand';
  //var bIsChecked = GetE('txtAlign').checked ;
  //SetAttribute( oActiveEl, 'checked', bIsChecked ? 'checked' : null ) ;	// For Firefox
  //oActiveEl.checked = bIsChecked ;
  return true ;
}

function mytip(){
  if(cal_tip.style.display == "none")
    cal_tip.style.display = "";
  else
    cal_tip.style.display = "none";   
}
function addRow(i) {
  var tr = new Element("tr");
  $('tableList').insert(tr);
  var td =  new Element("td");
  td.align = "center";
  td.update(i);
  tr.appendChild(td);
  var td2 =  new Element("td");
  td2.title = "Tab键切换输入框";
  td2.align = "center";
  td2.update("<Input id=\"item_"+i +"\" type=\"text\" size=\"25\">");
  tr.appendChild(td2);

  var td3 =  new Element("td");
  td3.title = "Tab键切换输入框";
  td3.align = "center";
  td3.update("<Input id=\"size_"+i +"\" type=\"text\" size=\"5\" value=\"10\">");
  tr.appendChild(td3);

  var td4 =  new Element("td");
  td4.title = "Tab键切换输入框";
  td4.align = "center";
  td4.update("<Input type=\"checkbox\" id=\"sum_"+i +"\">");
  tr.appendChild(td4);

  var td5 =  new Element("td");
  td5.title = "Tab键切换输入框";
  td5.align = "center";
  td5.update("<Input id=\"cal_"+i +"\" type=\"text\" size=\"15\">");
  tr.appendChild(td5);

  var td6 =  new Element("td");
  td6.title = "Tab键切换输入框";
  td6.align = "center";
  td6.update("<select id=\"align_"+i +"\"><option value='left'>居左</option><option value='center'>居中</option><option value='right'>居右</option><select>");
  tr.appendChild(td6);
}
</script>
</head>
<body style="overflow-x: hidden ;font-size:12pt">
<table width="100%" style="height: 100%">
          <tr>
            <td nowrap colspan="6">控件名称：              <Input id="itemName" type="text" size="20">
              <input type="button" align="right" value="计算公式说明" onclick="mytip();">
            </td>
          </tr>
          <tr><td colspan="6"><hr></td></tr>
          <tr id="cal_tip" style="display:none;">
            <td nowrap colspan="6" style="font-size: 10pt;font-family:宋体;color:blue;">计算公式说明：<br>
            用[1] [2] [3]等代表某列的数值。运算符支持+,-,*,/,%等。<br>
            <hr>
            </td>
          </tr>
          <tr>
              <td nowrap align="center">序号</td>
              <td nowrap align="center" title="Tab键切换输入框">列表控件表头项目&nbsp;&nbsp;</td>
              <td nowrap align="center" title="Tab键切换输入框">宽度</td>
              <td nowrap align="center" title="Tab键切换输入框">合计</td>
              <td nowrap align="center" title="Tab键切换输入框">计算公式</td>
              <td nowrap align="center" title="Tab键切换输入框">位置</td>
          </tr>
          <tbody id="tableList"></tbody>
</table>
</body>
</html>