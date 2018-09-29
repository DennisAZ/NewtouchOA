<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>单选控件</title>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
var dialog	= window.parent ;//应该是对话框外壳所对应的窗口 by dq 090520
var oEditor = dialog.InnerDialogLoaded() ;//获得最外层界面所对应的window，该函数在对话框外壳程序中(fckdialog.html)定义 by dq 090520

// Gets the document DOM
var oDOM = oEditor.FCK.EditorDocument ;//获得编辑区域的iframe对应的窗口的document（定义及赋值：fck.js第942行-->114行-->fc,editingarea.js第28行-->220行），它是直接可编辑的 by dq 090520
//Selection是什么？(在fckselection.js第24行定义)，GetSelectedElement函数在哪定义？(在fckselection_ie.js第48行定义) by dq 090520
var oActiveEl = dialog.Selection.GetSelectedElement() ;//获得编辑区域里被选中的控件对象 by dq 090520
var item_id = "";
window.onload = function()//对话框弹出时，最内层的对话框显示完成后，执行这个onload函数 by dq 090520
{
    window.resizeTo(300, 235);

	// First of all, translate the dialog box texts
	//oEditor.FCKLanguageManager.TranslatePage(document) ;//如果这句话注释掉，则最内层的iframe（当前控件的对话框内容）界面为英文  by dq 090520

	if ( oActiveEl && oActiveEl.tagName == 'IMG')//编辑一个编辑区域内的控件时，走这个分支 by dq 090520
	{
		GetE('txtName').value	= oActiveEl.getAttribute('title') ;
		item_id = oActiveEl.name.substr(5);
		
  	var DATA_FIELD = oActiveEl.getAttribute("radio_field");
  	var LV_CHECK = oActiveEl.getAttribute("radio_check");
  	var DATA_FIELD_ARRAY = DATA_FIELD.split("`");
  	
  	for (i=0; i<DATA_FIELD_ARRAY.length-1; i++)
  	{
  	    var item="item"+(i+1);
  	    var item_check="item_check"+(i+1);
  	    GetE(item).value = DATA_FIELD_ARRAY[i] ;
  	    if(GetE(item).value==LV_CHECK)
  	       GetE(item_check).checked =true;
  	}
  }
	else//在编辑区域内新建一个控件时，走这个分支 by dq 090520
		oActiveEl = null ;

	dialog.SetOkButton( true ) ;//该函数在fckdialog.html 683行定义 by dq 090520
	//dialog.SetAutoSize( true ) ;//该函数在fckdialog.html 213行定义 by dq 090520
	SelectField( 'txtName' ) ;//在编辑区域设置刚创建的控件为选中状态，该函数在fck_dialog_common.js 第100行定义 by dq 090520
}

function Ok()
{
  //为“撤销”操作做准备，记录当前编辑区域的状态（猜的） by dq 090520
  oEditor.FCKUndo.SaveUndoStep() ;
  if(txtName.value==""){
    alert("控件名称不能为空");
    return;
  }
  if(oActiveEl == null && !checkUnique(txtName.value,oDOM)){
    alert("控件名称必须唯一");
    return;
  }
  var DATA_FIELD="";
  var LV_CHECK="";
  var count=0;
  var b = new Array();
  for(i=1;i< 11;i++)
  {
     var item="item"+i;
     var item_check="item_check"+i;
      if(document.getElementById(item).value!="")
      {
      	for(var k=0;k< b.length;k++)
      	{
      	   if(b[k]==document.getElementById(item).value)
      	   {
      	      alert("菜单项目'"+b[k]+"'重复");
      	      return false;
      	   }
      	   else
      	   {
      	   	  continue;
      	   }
      	}
      	b.push(document.getElementById(item).value);
      	
        DATA_FIELD +=document.all(item).value+"`";
        count++;
        
        if(document.all(item_check).checked)
           LV_CHECK=document.all(item).value;
      }
  }
  
  if(count==0)
  {
     alert("菜单项目不能为空");
     return;
  }

  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("DATA",oDOM);
    idtr = 'DATA_' + sort;
  }else{ 
    idtr = oActiveEl.name;
  }
  oActiveEl = CreateNamedElement( oEditor, oActiveEl, 'IMG', {title: GetE('txtName').value.replace("\"","&quot;"),id:idtr,name:idtr,src:contextPath + "<%=moduleContextPath %>/flowform/image/radio.gif"});
  
  SetAttribute( oActiveEl, 'RADIO_FIELD', DATA_FIELD ) ;
	SetAttribute( oActiveEl, 'RADIO_CHECK', LV_CHECK ) ;
	SetAttribute( oActiveEl, 'class', 'RADIO' ) ;
	SetAttribute( oActiveEl, 'className', 'RADIO' ) ;

	return true ;
}
function check(val)
{
   for(var o=1;o< 11;o++)
   {
      item_check="item_check"+o;
      
      if(val==o)
         continue;
      else
         document.getElementById(item_check).checked=false;
   }
}

	</script>
</head>
<body style="overflow-x: hidden ;font-size:12pt">

<table width="100%" style="height: 100%">
		<tr>
			<td align="center">
				<table border="0">
					<tr>
						<td>
							<span>单选框名称</span>
						</td>
						<td>
							<input id="txtName" type="text" size="20" />
						</td>
					</tr>
					<tr>
						<td>
							<b>单选框菜单项目</b>
						</td>
						<td align="center">
							<b>设置默认选中</b>
						</td>
					</tr>	
					<tr>
						<td>
							<input type="text" id="item1">
						</td>
						<td align="center">
						  <input type="checkbox" id="item_check1" onclick="check(1)">
						</td>
					</tr>	
					<tr>
						<td>
							<input type="text" id="item2">
						</td>
						<td align="center">
							<input type="checkbox" id="item_check2" onclick="check(2)">
						</td>
					</tr>	
					<tr>
						<td>
							<input type="text" id="item3">
						</td>
						<td align="center">
							<input type="checkbox" id="item_check3" onclick="check(3)">
						</td>
					</tr>	
					<tr>
						<td>
							<input type="text" id="item4">
						</td>
						<td align="center">
							<input type="checkbox" id="item_check4" onclick="check(4)">
						</td>
					</tr>	
					<tr>
						<td>
							<input type="text" id="item5">
						</td>
						<td align="center">
							<input type="checkbox" id="item_check5" onclick="check(5)">
						</td>
					</tr>	
					<tr>
						<td>
							<input type="text" id="item6">
						</td>
						<td align="center">
							<input type="checkbox" id="item_check6" onclick="check(6)">
						</td>
					</tr>	
					<tr>
						<td>
							<input type="text" id="item7">
						</td>
						<td align="center">
							<input type="checkbox" id="item_check7" onclick="check(7)">
						</td>
					</tr>	
					<tr>
						<td>
							<input type="text" id="item8">
						</td>
						<td align="center">
							<input type="checkbox" id="item_check8" onclick="check(8)">
						</td>
					</tr>	
					<tr>
						<td>
							<input type="text" id="item9">
						</td>
						<td align="center">
							<input type="checkbox" id="item_check9" onclick="check(9)">
						</td>
					</tr>	
					<tr>
						<td>
							<input type="text" id="item10">
						</td>
						<td align="center">
							<input type="checkbox" id="item_check10" onclick="check(10)">
						</td>
					</tr>	
				</table>
			</td>
		</tr>
</table>
</body>
</html>