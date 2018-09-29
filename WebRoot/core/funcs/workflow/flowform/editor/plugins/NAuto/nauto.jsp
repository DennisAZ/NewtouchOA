<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script src="<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>宏控件</title>
<script type="text/javascript">
var dialog = window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement();
window.onload = function(){
  oEditor.FCKLanguageManager.TranslatePage(document);
  if(oActiveEl){
    if(oActiveEl.tagName == 'INPUT'){
      GetE('itemName').value = oActiveEl.getAttribute('title');
 	  GetE('itemType').value = oActiveEl.getAttribute('datafld');
 	  if(oActiveEl.getAttribute('datasrc') == null){
 	    GetE('itemSql').value = "";
 	  }else{
 	    GetE('itemSql').value = oActiveEl.getAttribute('datasrc');
 	  }
      var txtFontSizeFull = oActiveEl.style.fontSize;//oActiveEl.getAttribute('style').fontSize;
  	  GetE('itemSize').value = txtFontSizeFull.substr(0, txtFontSizeFull.length - 2);//这里的substr是为了去掉末尾的'px' by dq 090522
      var txtSizeFull = oActiveEl.style.width;
      GetE('txtSize').value = txtSizeFull.substr(0, txtSizeFull.length - 2);
    }else{
      GetE('itemName').value = oActiveEl.getAttribute('title');
      GetE('itemType').value = oActiveEl.getAttribute('datafld');
      GetE('itemSql').value = oActiveEl.getAttribute('datasrc');
      var txtFontSizeFull = oActiveEl.style.fontSize;
      GetE('itemSize').value = txtFontSizeFull.substr(0, txtFontSizeFull.length - 2);//这里的substr是为了去掉末尾的'px' by dq 090522
    }
  }else{
    oActiveEl = null;
  }
  dialog.SetOkButton(true);
  dialog.SetAutoSize(true);
  SelectField('itemName');//在编辑区域设置刚创建的控件为选中状态  view_sql();
}
function Ok(){
  
  var itemName = GetE('itemName'); 
  var itemType = GetE('itemType');
  var itemSql = GetE('itemSql');
  if(itemName.value == ''){
    alert('控件名称不能为空');
    return false;
  }
  
  else if(itemType.value == ''){
    alert('宏控件类型不能为空');
    return false;
  }
  else if(itemType.value == 'SYS_SQL'||itemType.value == 'SYS_LIST_SQL'){
    if(itemSql.value == ''){
      alert('SQL查询语句不能为空');
      return false;
    }
    else if(!check_sql(false)){
      return false;
    }
  }
  
  if(oActiveEl == null && !checkUnique(GetE('itemName').value,oDOM)){
    alert("控件名称必须唯一");
    return;
  }
  var reg1 = /[^\d]/g;
  str = GetE('itemSize').value;
  if (str && str.match(reg1)) {
    alert("字体大小只能为数字！");
    return ;
  }
  str = GetE('txtSize').value;
  if (str && str.match(reg1)) {
    alert("控件大小只能为数字！");
    return ;
  }
  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("DATA",oDOM);
    idtr = 'DATA_' + sort;
  }else{ 
    idtr = oActiveEl.name;
  }
  oEditor.FCKUndo.SaveUndoStep();
  oActiveEl = null;
  if(itemType.value.indexOf('SYS_LIST') < 0){
    oActiveEl = CreateNamedElement( oEditor, oActiveEl, 'INPUT', {title: GetE('itemName').value.replace("\"","&quot;"), id:idtr,name: idtr, type: 'text'});
    SetAttribute(oActiveEl, 'value', '{宏控件}');
  }else{
    oActiveEl = CreateNamedElement( oEditor, oActiveEl, 'SELECT', {title: GetE('itemName').value.replace("\"","&quot;"), id:idtr,name:idtr, type: 'text'});
    //var objOption = new Option('{宏控件}', '');
    var objOption = document.createElement("option");;
    objOption.value = '';
    objOption.innerHTML = '{宏控件}';
    oActiveEl.appendChild(objOption);
   // oActiveEl.options[oActiveEl.options.length] = objOption;
  }
  //设置这个刚创建的宏控件的各个属性（根据创建时对话框里填写的内容） by dq 090520
  
  SetAttribute(oActiveEl, 'class', 'AUTO');//for firefox
  SetAttribute(oActiveEl, 'className', 'AUTO');//for IE
  if (GetE('itemType').value && GetE('itemType').value != 'null') {
    SetAttribute(oActiveEl, 'datafld', GetE('itemType').value);
  }
  if (GetE('itemSql').value && GetE('itemSql').value != 'null') {
    SetAttribute(oActiveEl, 'datasrc', GetE('itemSql').value);
  }
  
  if(GetE('itemSize').value != ''){
    oActiveEl.style.fontSize = GetE('itemSize').value + 'px';
  }
  if(GetE('txtSize').value != ""){
	oActiveEl.style.width = GetE('txtSize').value + 'px';
  }
  //SetAttribute( oActiveEl, 'dataitem', GetE('itemSize').value );
  //SetAttribute( oActiveEl, 'datatxt', GetE('txtSize').value );
  
  return true ;
}
function view_sql(){
  var itemType = GetE('itemType');
  var SQL_AREA = document.getElementById("SQL_AREA");
  if(itemType.value == "SYS_SQL" || itemType.value == "SYS_LIST_SQL"){
    SQL_AREA.style.display = "";
  }else{
    SQL_AREA.style.display = "none";
  }
}

function check_sql(check_flag){
  var itemSql = GetE('itemSql');
  var expr1 = /\n/g;   //检测单引号和回车
  var expr2 = /'/g;
  var expr3 = />/g;
  
  var seq = document.getElementById("itemSql").value;//$("itemSql").innerHTML;
  if(seq.match(expr1) || seq.match(expr2) || seq.match(expr3)){
    var msg = "您的sql语句中存在单引号和回车，不符合要求，是否进行替换？";
    if(window.confirm(msg)){
      seq = seq.replace(expr1,"");
      seq = seq.replace(expr2,"`");
      seq = seq.replace(expr3,"》");
      $(itemSql).innerHTML = seq;
    }else{
      return (false);
    }
  }
  if(check_flag){
    var seq = trim(seq);
    if(seq == ""){
      alert('失败：SQL语句为空!');
      return false;
    }
    if(seq.substr(0,6)!= "select"){
      alert('失败：非法SQL语句!');
      return false;
    }
    var url = "<%=contextPath%>/yh/core/funcs/workflow/util/YHFlowFormUtilAct/getSql.act?sql=" + encodeURIComponent(seq);
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      alert(rtJson.rtMsrg); 
    }else{
      alert(rtJson.rtMsrg); 
    }
    //_get("/general/system/workflow/flow_form/cool_form/test_sql.php?time=<?=time()?>&sql="+sql,"",function(req){alert(req.responseText);});
  }
  return true;
}

</script>
</head>
<body style="overflow: hidden ;font-size:12pt">
<table width="100%" style="height: 100%">
  <tr>
      <td nowrap>宏控件名称：
      </td>
      <td nowrap colspan=3>
       <Input id="itemName" type="text" size="20">
      </td>
  </tr>
  <tr>
      <td nowrap>宏控件类型：
      </td>
      <td nowrap>
       <select id="itemType" onchange="view_sql()">
        <optgroup label="----单行输入框----">
        <option value="SYS_DATE">当前日期，形如 1999-01-01</option>
        <option value="SYS_DATE_CN">当前日期，形如 2009年1月1日</option>
        <option value="SYS_DATE_CN_SHORT3">当前日期，形如 2009年</option>
        <option value="SYS_DATE_CN_SHORT4">当前年份，形如 2009</option>
        <option value="SYS_DATE_CN_SHORT1">当前日期，形如 2009年1月</option>
        <option value="SYS_DATE_CN_SHORT2">当前日期，形如 1月1日</option>
        <option value="SYS_TIME">当前时间</option>
        <option value="SYS_DATETIME">当前日期+时间</option>
        <option value="SYS_WEEK">当前星期中的第几天，形如 星期一</option>
        <option value="SYS_USERID">当前用户ID</option>
        <option value="SYS_USERNAME">当前用户姓名</option>
        <option value="SYS_DEPTNAME">当前用户部门(长名称)</option>
        <option value="SYS_DEPTNAME_SHORT">当前用户部门(短名称)</option>
        <option value="SYS_YEAR_DEPT">年+当前用户部门(短名称)</option>
        <option value="SYS_YEAR_DEPT_AUTONUM">年+当前用户部门(短名称)+文号计数器</option>
        <option value="SYS_USERPRIV">当前用户角色</option>
        <option value="SYS_USERNAME_DATE">当前用户姓名+日期</option>
        <option value="SYS_USERNAME_DATETIME">当前用户姓名+日期+时间</option>
        <option value="SYS_FORMNAME">表单名称</option>
        <option value="SYS_RUNNAME">工作名称/文号</option>
        <option value="SYS_RUNDATE">流程开始日期</option>
        <option value="SYS_RUNDATETIME">流程开始日期+时间</option>
        <option value="SYS_RUNID">流水号</option>
        <option value="SYS_AUTONUM">文号计数器</option>
        <option value="SYS_IP">经办人IP地址</option>
        <option value="SYS_MANAGER1">部门主管(本部门)</option>
        <option value="SYS_MANAGER2">部门主管(上级部门)</option>
        <option value="SYS_MANAGER3">部门主管(一级部门)</option>
        <option value="SYS_SQL">来自SQL查询语句</option>
        </optgroup>
        <optgroup label="----下拉菜单----">
        <option value="SYS_LIST_DEPT">部门列表</option>
        <option value="SYS_LIST_USER">人员列表</option>
        <option value="SYS_LIST_PRIV">角色列表</option>
        <option value="SYS_LIST_PRCSUSER1">流程设置所有经办人列表</option>
        <option value="SYS_LIST_PRCSUSER2">本步骤设置经办人列表</option>
        <option value="SYS_LIST_MANAGER1">部门主管(本部门)</option>
        <option value="SYS_LIST_MANAGER2">部门主管(上级部门)</option>
        <option value="SYS_LIST_MANAGER3">部门主管(一级部门)</option>
        <option value="SYS_LIST_SQL">来自SQL查询语句的列表</option>
        </optgroup>
       </select>
      </td>
  </tr>
  <tr id="SQL_AREA" style="display:none">
      <td nowrap>SQL查询语句<br>('号用`号替换)：
      </td>
      <td nowrap>
       <textarea id="itemSql" class="SmallInput" cols="38" rows="4" title="如宏控件类型选择来自SQL查询语句，则填写"></textarea>
       <br>
       <a href="#" onclick="check_sql('1');">测试</a>
      </td>
  </tr>
  <tr>
      <td nowrap>字体大小：      </td>
      <td nowrap>
       <Input id="itemSize" type="text" size="20" title="">
      </td>
  </tr>
	<tr>
		<td nowrap>
			控件大小：		</td>
		<td nowrap><input id="txtSize" type="text" size="10" /> px
		</td>
	</tr>
</table>

</body>
</html>