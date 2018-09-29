<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script src="/yh/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>日期控件</title>
<script type="text/javascript">
var dialog	= window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement();
window.onload = function(){
  oEditor.FCKLanguageManager.TranslatePage(document);
  if(oActiveEl && oActiveEl.tagName == 'IMG'){
    GetE('itemName').value	= oActiveEl.getAttribute("value");
    var dateFmt = oActiveEl.getAttribute('date_format');
		if(dateFmt == null)
		{
		    GetE('DATE_FORMAT').value = "yyyy-MM-dd";
		}
		else if(dateFmt=="yyyy-MM-dd")
		{
		    GetE('DATE_FORMAT').options[0].selected = true;
		}
		else if(dateFmt=="yyyy-MM-dd hh:mm:ss")
		{
		    GetE('DATE_FORMAT').options[1].selected = true;
		}
		else 
		{
		    GetE('DATE_FORMAT').value = "";
		    GetE('DEF_FORMAT').value = dateFmt;
		    GetE('DEF_FORMAT').style.display = "";
		}
  }else {
    oActiveEl = null;
  }
  dialog.SetOkButton(true);
  dialog.SetAutoSize(true);
  SelectField('itemName');
  //在编辑区域设置刚创建的控件为选中状态，该函数在fck_dialog_common.js 第100行定义 by dq 090520
}

function Ok(){
  var itemName = GetE('itemName');
  if(itemName.value==""){
    alert("控件名称不能为空");
    return;
  }
  if(oActiveEl == null && !checkUnique('日期控件:' + GetE('itemName').value,oDOM)){
    alert("控件名称必须唯一");
    return;
  }
  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("OTHER",oDOM);
    idtr = 'OTHER_' + sort;
  }else{ 
    idtr = oActiveEl.name;
  }
  oEditor.FCKUndo.SaveUndoStep();
  oActiveEl = CreateNamedElement(oEditor, oActiveEl, 'IMG', {title: '日期控件:' + GetE('itemName').value,id:idtr ,name:idtr ,src:"<%=contextPath%><%=moduleContextPath %>/flowform/editor/plugins/NCalendar/calendar.gif"});
//日期类型
	if(GetE("DATE_FORMAT").value=="")
	{
	    SetAttribute( oActiveEl, 'DATE_FORMAT', GetE("DEF_FORMAT").value ) ;
	}
	else
	    SetAttribute( oActiveEl, 'DATE_FORMAT', GetE("DATE_FORMAT").value ) ;
  SetAttribute(oActiveEl, 'value', itemName.value.replace("\"","&quot;"));
  SetAttribute(oActiveEl, 'border', 0);
  SetAttribute(oActiveEl, 'align', 'absMiddle');
  SetAttribute(oActiveEl, 'class', 'DATE');
  SetAttribute(oActiveEl, 'className', 'DATE');
  	//SetAttribute( oActiveEl, 'src', '/module/html_editor/editor/images/calendar.gif' ) ;
  oActiveEl.style.cursor = 'hand';
	//var bIsChecked = GetE('txtSelected').checked ;
	//SetAttribute( oActiveEl, 'checked', bIsChecked ? 'checked' : null ) ;	// For Firefox
	//oActiveEl.checked = bIsChecked ;
  return true;
}
function show_def(val)
{
    GetE("DEF_FORMAT").style.display = val=="" ? "" : "none";
}
</script>
</head>
<body style="overflow-x: hidden ;font-size:12pt">
<table width="100%" style="height: 100%">
		<tr>
			<td align="center">
				<table cellspacing="3" cellpadding="0" border="0">
          <tr>
              <td nowrap><b>对应的输入框控件名称</b>：
              </td>
              <td nowrap align=left>
               <Input id="itemName" type="text" size="20">
              </td>
          </tr>
          <tr>
                    <td nowrap align="right"><b>输入格式类型</b>：
                    </td>
                    <td nowrap>
                        <select id="DATE_FORMAT" onchange="show_def(this.value);">
                            <option value="yyyy-MM-dd">日期，形如：2010-09-09</option>
                            <option value="yyyy-MM-dd hh:mm:ss">日期，形如：2010-09-09 09:25:00</option>
                            <option value="">自定义格式</option>
                        </select>
                        <br/><input type="text" style="display:none;" id="DEF_FORMAT" value="">
                    </td>
                </tr>
          <tr>
              <td colspan="2" align=left>
              	<div  style="font-size: 10pt;font-family:宋体;color:blue;word-wrap:break-word;width:280px;">
              		<b>说明:</b>日历控件选择的日期、时间将回填到该输入框中
              	</div>
              </td>
          </tr>
				</table>
			</td>
		</tr>
</table>
</body>
</html>
