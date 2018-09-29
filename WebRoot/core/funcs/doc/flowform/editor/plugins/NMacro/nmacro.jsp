<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/flowform.css">
<link rel="stylesheet" href = "<%=cssPath%>/views.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script src="<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>使用宏</title>
<style> 
#tooltip {
  position:absolute;
  border:1px solid #333;
  background:#f7f5d1;
  padding:2px 5px;
  color:#333;
  display:none;
}
</style>
<script type="text/javascript">
var dialog  = window.parent;
var oEditor = dialog.InnerDialogLoaded();
var parent_window = oEditor;
function add(btn , tmp){
  var macro = btn.parentNode.previousSibling.innerHTML;
  if (!macro) {
    macro = tmp;
  }
  if(macro.indexOf("#[附件") >= 0 || macro.indexOf("#[会签意见") >= 0){
    var obj = $('sign_no');
    if (macro.indexOf("#[附件") >= 0) {
      obj = $('file_no');
    } 
    if((obj.value <= 0 || obj.value%1 != 0) && obj.value != ""){
      alert("请输入有效数字!");
      obj.value = "";
      return;
    }
    if(obj.id == "file_no")
      macro = "#[附件" + obj.value+"]";
    else if(obj.id == "sign_no")
    macro = "#[会签意见" + obj.value+SIGN_TYPE.value+"]" + "["+document.getElementById("style").value + "]";
    obj.value = "";
  }
  parent_window.FCKUndo.SaveUndoStep();
  var FCK = parent_window.FCKeditorAPI.GetInstance('FORM_CONTENT'); 
  var parent_doc = FCK.EditingArea.Window.document;
  FCK.EditingArea.Focus();
  var sel = parent_doc.selection.createRange();
  if (parent_doc.selection.type == 'Control'){
    alert('请先取消编辑区域里对控件的选中！');
    return;
  }
  sel.pasteHTML(macro);
}
 
window.onload = function(){
  dialog.SetAutoSize(true);
}

</script>
</head>
<body topmargin="0">
<table border="0" width="100%" class="TableList" align="center">
  <tr class="TableHeader">
      <td nowrap align="center">宏标记及说明</td>
      <td nowrap align="center">操作</td>
  </tr>
  <tr class="TableLine1" >
      <td nowrap align="center" title="说明：代表表单名称">#[表单]</td>
      <td nowrap align="center"><input class="SmallButton" type="button" value="添加" onclick="add(this , '#[表单]')">
  </tr>
  <tr class="TableLine2">
      <td nowrap align="center" title="说明：代表文号或说明">#[文号]</td>
      <td nowrap align="center"><input class="SmallButton" type="button" value="添加" onclick="add(this, '#[文号]')">
  </tr>
  <tr class="TableLine1">
      <td nowrap align="center" title="说明：代表文号计数器">#[文号计数器]</td>
      <td nowrap align="center"><input class="SmallButton" type="button" value="添加" onclick="add(this, '#[文号计数器]')">
  </tr>
  <tr class="TableLine2">
      <td nowrap align="center" title="说明：代表流程开始时间">#[时间]</td>
      <td nowrap align="center"><input class="SmallButton" type="button" value="添加" onclick="add(this, '#[时间]')">
  </tr>
  <tr class="TableLine1">
      <td nowrap align="center" title="说明：代表工作流水号">#[流水号]</td>
      <td nowrap align="center"><input class="SmallButton" type="button" value="添加" onclick="add(this, '#[流水号]')">
  </tr>
  <tr class="TableLine2">
      <td nowrap title="说明：将列出该工作所有的会签意见，或指定步骤编号的会签意见">#[会签意见<input type="text" class="SmallInput" id=sign_no size=2 value=""> <select name="SIGN_TYPE"><option value="">按步骤实际编号</option><option value="*">按步骤设计编号</option></select>]<br>
      格式表达式:<input type="text" id="style" size=15 class="SmallSelect"> <a href="#" onclick="javascript:if(tip.style.display=='none') tip.style.display='';else tip.style.display='none'">说明</a><br>
      <div id="tip" style="display:none">
      说明: {C}：表示意见内容<br>
            {Y}：表示年<br>
            {M}：表示月<br>
            {D}：表示日<br>
            {H}：表示时<br>
            {I}：表示分<br>
            {S}：表示秒<br>
            {U}：表示用户姓名<br>
            {R}：表示角色<br>
            {P}：表示步骤名称<br>
            {SD}：表示短部门<br>
            {LD}：表示长部门<br>
            {SH}：表示手写签章<br>
      例如:{C} {U} {Y}{M}{D} <br>
      则显示样式为: xxxx 张三 20090202
      </div>
      </td>
      <td nowrap align="center"><input class="SmallButton" type="button" value="添加" onclick="add(this , '#[会签意见')">
  </tr>
  <tr class="TableLine1">
      <td nowrap align="center" title="说明：列出所有附件，或者第N个附加，N=1,2,3... ">#[附件<input type="text" class="SmallInput" id=file_no size=2 value="">]</td>
      <td nowrap align="center"><input class="SmallButton" type="button" value="添加" onclick="add(this,  '#[附件')">
  </tr>
  <tr class="TableControl" style="display:none">
      <td colspan="2" align="center">
        <input type="button" class="BigButton" onclick="window.close();" value="关 闭" class="SmallButton">
      </td>
  </tr>
</table>
</body>
</html>