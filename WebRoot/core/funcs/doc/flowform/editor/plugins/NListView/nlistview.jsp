<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>列表控件 </title>
<script type="text/javascript">
var dialog = window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement();
window.onload = function(){   
  oEditor.FCKLanguageManager.TranslatePage(document);
  if(oActiveEl && oActiveEl.tagName == 'IMG'){
    GetE('itemName').value	= oActiveEl.title;
    var item_id = oActiveEl.name.substr(5);
    var LV_TITLE = oActiveEl.getAttribute("lv_title");
    var LV_SIZE = oActiveEl.getAttribute("lv_size");
    var LV_CAL =oActiveEl.getAttribute("lv_cal");
    var LV_TITLE_ARRAY = LV_TITLE.split("`");
    var LV_SIZE_ARRAY = LV_SIZE.split("`");
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
      GetE(item_str).value = LV_TITLE_ARRAY[i];
      GetE(size_str).value = LV_SIZE_ARRAY[i];
      if(LV_SUM) GetE(sum_str).checked = LV_SUM_ARRAY[i] == 1?true:false;
      if(LV_CAL) GetE(cal_str).value = LV_CAL_ARRAY[i];
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
  var count = 0;
  var reg1 = /[^\d]/g;
  
  for(var i = 1; i <= 50; i++){
    var item_str = "item_" + i;
    var size_str = "size_" + i;
    var sum_str = "sum_" + i;
    var cal_str = "cal_" + i;
    if(document.getElementById("item_"+i).value != ""){
      LV_TITLE += document.getElementById("item_"+i).value+"`";
      count++;
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
  oActiveEl = CreateNamedElement( oEditor, oActiveEl, 'IMG', {title: GetE('itemName').value,id:idtr,name:idtr,src:"<%=contextPath%><%=moduleContextPath %>/flowform/editor/plugins/NListView/listview.gif"});
  SetAttribute(oActiveEl, 'LV_TITLE', LV_TITLE);
  SetAttribute(oActiveEl, 'LV_SIZE', LV_SIZE);
  SetAttribute(oActiveEl, 'LV_SUM', LV_SUM);
  SetAttribute(oActiveEl, 'LV_CAL', LV_CAL);
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
</script>
</head>
<body style="overflow-x: hidden ;font-size:12pt">
<table width="100%" style="height: 100%">
		<tr>
			<td align="center">
				<table cellspacing="3" cellpadding="0" border="0">
          <tr>
            <td nowrap colspan="6">控件名称：
              <Input id="itemName" type="text" size="20">
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
          </tr>
          <tr>
              <td nowrap align="center">1</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_1" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_1" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_1">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_1" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">2</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_2" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_2" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_2">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_2" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">3</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_3" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_3" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_3">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_3" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">4</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_4" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_4" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_4">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_4" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">5</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_5" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_5" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_5">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_5" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">6</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_6" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_6" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_6">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_6" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">7</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_7" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_7" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_7">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_7" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">8</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_8" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_8" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_8">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_8" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">9</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_9" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_9" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_9">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_9" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">10</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_10" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_10" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_10" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">11</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_11" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_11" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_11">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_11" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">12</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_12" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_12" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_12">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_12" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">13</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_13" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_13" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_13">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_13" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">14</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_14" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_14" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_14">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_14" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">15</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_15" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_15" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_15">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_15" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">16</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_16" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_16" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_16">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_16" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">17</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_17" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_17" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_17">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_17" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">18</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_18" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_18" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_18">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_18" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">19</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_19" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_19" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_19">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_19" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">20</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_20" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_20" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_20">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_20" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">21</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_21" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_21" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_21">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_21" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">22</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_22" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_22" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_22">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_22" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">23</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_23" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_23" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_23">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_23" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">24</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_24" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_24" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_24">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_24" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">25</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_25" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_25" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_25" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">26</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_26" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_26" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_26">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_26" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">27</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_27" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_27" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_27">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_27" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">28</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_28" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_28" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_28">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_28" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">29</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_29" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_29" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_29">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_29" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">30</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_30" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_30" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_30">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_30" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">31</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_31" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_31" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_31">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_31" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">32</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_32" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_32" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_32">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_32" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">33</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_33" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_33" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_33">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_33" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">34</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_34" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_34" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_34">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_34" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">35</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_35" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_35" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_35">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_35" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">36</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_36" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_36" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_36">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_36" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">37</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_37" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_37" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_37">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_37" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">38</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_38" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_38" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_38">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_38" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">39</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_39" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_39" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_39">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_39" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">40</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_40" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_40" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_40">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_40" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">41</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_41" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_41" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_41">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_41" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">42</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_42" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_42" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_42">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_42" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">43</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_43" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_43" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_43">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_43" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">44</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_44" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_44" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_44">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_44" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">45</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_45" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_45" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_45">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_45" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">46</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_46" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_46" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_46">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_46" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">47</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_47" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_47" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_47">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_47" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">48</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_48" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_48" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_48">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_48" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">49</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_49" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_49" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_49">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_49" type="text" size="15">
              </td>
          </tr>
          <tr>
              <td nowrap align="center">50</td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="item_50" type="text" size="25">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="size_50" type="text" size="5" value="10">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input type="checkbox" id="sum_50">
              </td>
              <td nowrap align="center" title="Tab键切换输入框">
              	<Input id="cal_50" type="text" size="15">
              </td>
          </tr>					
				</table>
			</td>
		</tr>
</table>
</body>
</html>