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
var oldId = "";
var widgetData = window.parent.parent.widgetData;
var contextPath = window.parent.parent.contextPath;
var dialog = window.parent ;
var oEditor = dialog.InnerDialogLoaded() ;
var FCKDTreeWidget = oEditor.FCKDTreeWidget;
var fckdocument =  oEditor.FCK.EditorDocument;
window.onload = function ()
{   
  getFunContext();
  LoadSelected() ;
  dialog.SetOkButton( true ) ;
  SelectField( 'treeId' ) ;
}

var eSelected = dialog.Selection.GetSelectedElement() ;

function LoadSelected()
{
	if( !eSelected )
		return ;
	if(eSelected.tagName == 'IMG'&& eSelected.getAttribute("widgettype")=='DTree'){
	  	$('treeId').value = eSelected.id;
		oldId =  eSelected.id
		
		//
		//var fun  = oParameters.checkboxPara.disCheckedFun;
		//alert(fun);
		var parameters = widgetData[ oldId + "Data"];
		var oParameters =  eval('(' + parameters + ')');
		//var fun  = oParameters.checkboxPara.disCheckedFun;
		//alert(fun());

		/*
		var index = parameters.indexOf("disCheckedFun:");
	    if(index != -1){
	      parameters = ChangeFunToString(parameters,"disCheckedFun:");
	      parameters = ChangeFunToString(parameters,"checkedFun:");
		}
	    var index = parameters.indexOf("clickFunc:");
	    if(index != -1){
	      parameters = ChangeFunToString(parameters,"clickFunc:");
	    }
		*/
		//var oParameters =  eval('(' + parameters + ')');
		
		$('bindToContainerId').value = oParameters.bindToContainerId;
		$('requestUrl').value = oParameters.requestUrl;
		$('isOnceLoad').checked = oParameters.isOnceLoad;
		var isNoTree = $('isNoTree');
		isNoTree.checked = oParameters.treeStructure.isNoTree;
		if(isNoTree.checked){
	  	$('regular').value = oParameters.treeStructure.regular;
	  		showDiv(isNoTree,'regularDiv');
    	}
		var isHaveCheckbox = $('isHaveCheckbox');
	     	if(oParameters.checkboxPara){
	      	isHaveCheckbox.checked = oParameters.checkboxPara.isHaveCheckbox;
	      	if(isHaveCheckbox.checked){
	  		  $('disCheckedFun').value = oParameters.checkboxPara.disCheckedFun();
	  		  $('checkedFun').value = oParameters.checkboxPara.checkedFun();	
	  		  showDiv(isHaveCheckbox,'checkboxDiv');
	  		}
     	}
      	
    	if(oParameters.linkPara){
         	var isHavelink = $('isHavelink');
         	isHavelink.checked = true;
         	if(oParameters.linkPara.clickFunc)
         	$("clickFunc").value = oParameters.linkPara.clickFunc();
			if(oParameters.linkPara.linkAddress)
          	$("linkAddress").value = oParameters.linkPara.linkAddress;
			if(oParameters.linkPara.target)
     	    $("target").value = oParameters.linkPara.target;
         	
    	   showDiv(isHavelink,'linkDiv');
  		}
    	
	}else{
	  
	  eSelected == null ;
	  
	}
		
}

function ChangeFunToString(parameters,funPar){
  var index = parameters.indexOf(funPar);
  var tailSub = parameters.substr(index + funPar.length);
  
  var i = tailSub.indexOf(',');
  var j = tailSub.indexOf('}');
  if(i == -1 || i>j){
    i = j;
  }
  
  var outTailSub = tailSub.substr(i);
  var fun = tailSub.substr(0,i);
  fun = "'" + fun + "'";
  var headSub = parameters.substr(0,index + funPar.length);
  parameters = headSub + fun + outTailSub;
  return parameters;
}

function Ok()
{
  if(!checkParameters()){
    return false;
  }
  var treeId = $F('treeId');
  var img = "<img widgettype=\"DTree\" id=\"" + treeId + "\" ";
  img += " src=\"<%=contextPath%>/raw/lh/fckeditor/editor/plugins/DtreeWidget/dtree.jpg\"/>";

  var oDtree = "{treeId:'" + $F('treeId') + "',bindToContainerId:'" + $F('bindToContainerId') + "'"
	 + ",requestUrl:'" + $F('requestUrl') + "'"
	 + ",isOnceLoad:" + $('isOnceLoad').checked 
	 + ",treeStructure:{isNoTree:" + $('isNoTree').checked
  if($('isNoTree').checked)
	oDtree  += ",regular:'" + $F('regular') + "'}"; 
	else 
	oDtree  += "}"; 
	if($('isHaveCheckbox').checked){
	oDtree += ",checkboxPara:{isHaveCheckbox:true";
	oDtree += ",disCheckedFun:" + $F('disCheckedFun');
	oDtree += ",checkedFun:" + $F('checkedFun') + "}";
	} 
	if($('clickFunc').present()){
	oDtree += ",linkPara:{clickFunc:" + $F('clickFunc') + "}";
	}else if($('linkAddress').present()){
	oDtree += ",linkPara:{linkAddress:'" + $F('linkAddress') + "'";
	if($('target').present()){
	  oDtree += ",target:'" + $F('target') + "'";
	}
	oDtree +=  "}";
	}
	oDtree +=  "}";
  widgetData[ treeId + "Data"] = oDtree;
  FCKDTreeWidget.AddDTree(img) ;
  return true ;
}

function checkParameters(){
  if(!$('treeId').present()){
	alert(' 没有指定树Id');
	$('treeId').focus();
	return false;
  }
 //为空的时候新建或者修改改变treeId时候
  if(!oldId||oldId != $F('treeId')){
     var isExistId = fckdocument.getElementById($F('treeId'));
     if(isExistId){
   		alert('此id文档中已有，请换用其它id');
   		$('treeId').focus();
   		return false;
     }
	}
  if($('isNoTree').checked&&!$('regular').present()){
    alert(' 没有指定规则');
    $('regular').focus();
    return false;
  }
  
  if($('isHaveCheckbox').checked){
	if(!$('checkedFun').present()||!$('disCheckedFun').present()){
      alert(' 没有指定选定checkbox时执行的函数');
      return false;
	}else{//检测函数
	  var isExistCheckedFun = fckdocument.getElementById($F('checkedFun'));
	  if(!isExistCheckedFun||isExistCheckedFun.getAttribute('widgettype') != 'Function'){
		alert(' 指定的函数'+ $F('checkedFun') +'不存在');
		$('checkedFun').focus();
		return false;
	  }
	  isExistCheckedFun = fckdocument.getElementById($F('disCheckedFun'));
	  if(!isExistCheckedFun||isExistCheckedFun.getAttribute('widgettype') != 'Function'){
		alert(' 指定的函数'+ $F('disCheckedFun') +'不存在');
		$('disCheckedFun').focus();
		return false;
	  }
	}
  }
  return true;
}
function showDiv(checkbox,div){
  if(checkbox.checked)
  	$(div).style.display = "";
  else
    $(div).style.display = "none"; 
}
--></script>
		</head>		
<body scroll="no" style="OVERFLOW: hidden">
		<table height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0">
			<tr  class="TableLine2">
				<td>
					<span>树ID：</span>
					<input id="treeId"  class="BigInput"  name="treeId" type="text">
					<span style="color:red">*</span>
		    	</td>
			</tr>
			<tr class="TableLine1">
			<td>
				<span>如果不指定，默为body,如果找不到容器ID，将会创造一DIV，id为容器ID</span>
				</td>
				</tr>
				<tr  class="TableLine2">
				<td>
					<span>容器ID：</span>
					<input id="bindToContainerId"  class="BigInput"  name="bindToContainerId" type="text">
		    	</td>
			</tr>
			<tr   class="TableLine2">
				<td>
					<span>请求地址：</span>
					<input id="requestUrl" value="" class="BigInput" name="requestUrl" type="text">
		    	</td>
			</tr>
			<tr  class="TableLine1">
			<td>
					<span>树节点是否一次加载 默认为false</span>
				</td>
				</tr>
				<tr   class="TableLine2">
				<td>
					<span>是否一次加载：</span>
					<input id="isOnceLoad" type="checkbox" class="BigInput" name="isOnceLoad">
		    	</td>
			</tr>
			<tr  class="TableLine1">
			<td>
					<span>树的节点的nodeId是否编码,如果树的节点的nodeId是否编码，需指字编码规则形如2,3,4,5</span>
				</td>
				</tr>
				<tr   class="TableLine2">
				<td>
					<span>树的结构：</span><span>
					<div style="margin-left:10px"><span>nodeId为编码型：</span><input id="isNoTree" type="checkbox" class="BigInput" name="isNoTree" onchange="showDiv(this,'regularDiv')"></div>
					<div id="regularDiv" style="display:none;margin-left:10px"><span>编码规则：</span><input id="regular" type="text" class="BigInput" name="regular"><span>形如2,3,4,5</span></div>
					</span>
		    	</td>
		    	
			</tr>
			<tr  class="TableLine1">
			<td>
					<span>设置选框，如果有选框，</span>
				</td>
				</tr>
				<tr   class="TableLine2">
				<td>
					<span>选框设置：</span><span>
					<div style="margin-left:10px"><span>是否有选框：</span><input id="isHaveCheckbox" type="checkbox" class="BigInput" name="isHaveCheckbox" onchange="showDiv(this,'checkboxDiv')"></div>
					<div id="checkboxDiv" style="display:none;margin-left:10px">
					<div>
					<span>选中执行：</span>
					<input id="checkedFun" type="text" class="BigInput" name="checkedFun">
					</div>
					<div>
					<span>取消执行：</span>
					<input id="disCheckedFun" type="text" class="BigInput" name="disCheckedFun">
					</div>
					</div>
					</span>
		    	</td>
		    	
			</tr>
			<tr  class="TableLine1">
			<td>
					<span>...</span>
				</td>
				</tr>
				<tr   class="TableLine2">
				<td>
					<span>链接设置：</span><span>
					<div style="margin-left:10px"><span>是否有链接：</span><input id="isHavelink" type="checkbox" class="BigInput" name="isHavelink" onchange="showDiv(this,'linkDiv')"></div>
					<div id="linkDiv" style="display:none;margin-left:10px">
					<div>
					<span>点击链接执行：</span>
					<input id="clickFunc" type="text" class="BigInput" name="clickFunc">
					</div>
					<div>
					<span>链接地址：</span>
					<input id="linkAddress" type="text" class="BigInput" name="linkAddress">
					<span>目标：</span>
					<input id="target" type="text" class="BigInput" name="target">
					</div>
					</div>
					</span>
		    	</td>
		    </tr>
		</table>
	</body>
</html>