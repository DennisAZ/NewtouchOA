<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置树</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script src="../../dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script language="javascript"><!--
var contextPath = window.parent.parent.contextPath;
var dialog = window.parent ;
var oldId = "";
var widgetData = window.parent.parent.widgetData;
var oEditor = dialog.InnerDialogLoaded() ;
var FCKFunction = oEditor.FCKFunction;
var fckdocument =  oEditor.FCK.EditorDocument;
window.onload = function ()
{   
    // First of all, translate the dialog box texts
	//oEditor.FCKLanguageManager.TranslatePage( document ) ;

	LoadSelected() ;

	// Show the "Ok" button.
	dialog.SetOkButton( true ) ;

	// Select text field on load.
	SelectField( 'funName' ) ;
}

var eSelected = dialog.Selection.GetSelectedElement() ;

function LoadSelected()
{
	if ( !eSelected )
		return ;
    
	if ( eSelected.tagName == 'IMG' && eSelected.getAttribute("widgettype") == 'Function' ){
	  oldId = eSelected.id;
	  var parameters = widgetData[ oldId + "Data"];
	  var oParameters = eval('(' + parameters + ')');
	  $('funName').value = oParameters.funName;
	  $('funPar').value = oParameters.funPar;
	 
	  $('funCode').value = unescape(oParameters.funCode);
	}else{
	  eSelected == null ;
	}		
}


function Ok()
{
  if(!checkParameters()){
    return false;
  }
  var id = $F('funName');
  var a = "<img widgetType=\"Function\" id=\"" + id + "\" ";
  
  a += " src=\"<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/plugins/Function/Function.jpg\" width=\"20px\" height=\"20px\"/>";
  var parameters = "{funName:\""+ id +"\",funPar:\""+ $F('funPar') +"\",funCode:\"" + escape($F('funCode')) + "\"}"
  widgetData[ id + "Data"] = parameters;
  FCKFunction.AddFun(a) ;
  return true ;
}

function checkParameters(){
  if(!$('funName').present()){
	alert('你没有指定函数名');
	$('funName').focus();
	return false;
  }
  if(oldId && oldId != $F('funName')){
	  var isExistId = fckdocument.getElementById($F('funName'));
	  if(isExistId){
	    alert('函数名'+ $F('funName') +'与文档中一id重复，请换用其它函数名');
		$('funName').focus();
		return false;
	  }
  }
  //函数参数匹配
  return true;
}
//<div id="funParDiv-1"><input/><input button deleteFunPar/></div>
function addFunPars(parName){
   var div = document.createElement("div");
   var input = document.createElement("input");
   var button = document.createElement("button");
   var length =  $(funPars).getElementsByTagName("div").length;
   //0,1,2 length = 3;
   with(input){
     id = "funPar-" + length;
     name =  "funPar-" + length;
     type = "text";
     value = parName;
   }
   with(button){
     id = "funParButton-" + length;
     name =  "funParButton-" + length;
     type = "button";
     value = '删除';
	}
   Event.observe($(button),"click",deleteFunPar.bindAsEventListener(this));
   with(div){
	 id = 'funParDiv-' + length;
	 appendChild(input);
	 appendChild(button);
	}
   $('funPars').appendChild(div);
}
function deleteFunPar(){
  var button = arguments[1];
  var divNode = button.parentNode
  var parentNode = button.parentNode.parentNode;
  parentNode.removeChild(divNode);
}
--></script>
		</head>		
<body scroll="no" style="OVERFLOW: hidden">
		<table height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0">
			<tr class="TableLine1">
			<td>
				<span>函数名将做为ID因此不能与文档中的其它ID重复</span>
				</td>
				</tr>
				<tr  class="TableLine2">
				<td>
					<span>函数名：</span>
					<input id="funName"  class="BigInput"  name="funName" type="text">
					<span style="color:'red'">*</span>
		    	</td>
			</tr>
			<tr class="TableLine1">
			<td>
				<span>.....</span>
				</td>
				</tr>
				<tr  class="TableLine2">
				<td>
					<span>参数：</span>
					<input id="funPar"  class="BigInput"  name="funPar" type="text">
					<span>形如funPar1,funPar2,funPar3</span>
				</td>
			</tr>
			<tr class="TableLine1">
			<td>
				<span>.....</span>
				</td>
				</tr>
				<tr  class="TableLine2">
				<td>
					<span>代码：</span>
					<textarea rows="10" cols="50" id="funCode" name="funCode"></textarea>
		    	</td>
			</tr>
		</table>
	</body>
</html>